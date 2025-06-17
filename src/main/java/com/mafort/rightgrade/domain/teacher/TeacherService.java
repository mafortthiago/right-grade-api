package com.mafort.rightgrade.domain.teacher;

import com.mafort.rightgrade.domain.authentication.AccountConfirmationToken;
import com.mafort.rightgrade.domain.authentication.AccountConfirmationTokenRepository;
import com.mafort.rightgrade.domain.authentication.PasswordValidationRequest;
import com.mafort.rightgrade.domain.authentication.RefreshTokenRepository;
import com.mafort.rightgrade.domain.verificationCode.VerificationCode;
import com.mafort.rightgrade.domain.verificationCode.VerificationCodeRepository;
import com.mafort.rightgrade.domain.email.MailgunEmailService;
import com.mafort.rightgrade.infra.exception.InvalidCodeException;
import com.mafort.rightgrade.infra.exception.InvalidEmail;
import com.mafort.rightgrade.infra.exception.InvalidPasswordException;
import com.mafort.rightgrade.infra.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletionException;

@Service
public class TeacherService {

    @Value("${api.url}")
    private String API_URL;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private TeacherRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    VerificationCodeRepository codeRepository;
    @Autowired
    AccountConfirmationTokenRepository tokenRepository;
    private final MailgunEmailService mailgunEmailService;

    @Autowired
    public TeacherService(@Lazy MailgunEmailService mailgunEmailService) {
        this.mailgunEmailService = mailgunEmailService;
    }

    public TeacherResponse get(UUID id){
        Teacher teacher = this.findTeacherById(id);
        return new TeacherResponse(teacher.getName(), teacher.getEmail());
    }

    private Teacher findTeacherById(UUID id){
        Optional<Teacher> teacherOptional = this.repository.findById(id);
        if(teacherOptional.isEmpty()){
            throw new NotFoundException("There is no teacher with this id");
        }
        return teacherOptional.get();
    }

    public void update(UUID id, TeacherUpdateRequest teacherUpdateRequest){
        Teacher teacher = this.findTeacherById(id);
        teacher.setName(teacherUpdateRequest.name());
        this.repository.save(teacher);
    }

    public void  validatePassword(PasswordValidationRequest passwordValidationRequest){
        Teacher teacher = (Teacher) this.repository.findByEmail(passwordValidationRequest.email());
        if (teacher == null) {
            throw new NotFoundException("Teacher not found");
        }

        boolean isValid = passwordEncoder.matches(passwordValidationRequest.password(), teacher.getPassword());

        if(!isValid){
            throw new InvalidPasswordException("Incorrect password.");
        }
    }

    public void sendEmail(String email, String language, String templateName, Map<String, String> variables) {
        Teacher teacher = (Teacher) this.repository.findByEmail(email);
        if (teacher == null) {
            throw new NotFoundException(messageSource.getMessage("error.invalid.user", null, LocaleContextHolder.getLocale()));
        }

        try {
            mailgunEmailService
                    .sendEmail(email, variables.get("subject"), templateName, variables)
                    .join();
        } catch (CompletionException ex) {
            if (ex.getCause() instanceof InvalidEmail) {
                this.repository.deleteTeacherByEmail(email);
                throw new InvalidEmail("Error with email");
            }
            throw ex;
        }
    }


    public Map<String, String> getPasswordResetVariables(String language, String code) {
        Locale locale = language != null && language.equalsIgnoreCase("en") ? Locale.ENGLISH : new Locale("pt");

        Map<String, String> variables = new HashMap<>();
        variables.put("logo", messageSource.getMessage("logo.url", null, locale));
        variables.put("greeting", messageSource.getMessage("email.greeting", null, locale));
        variables.put("text", messageSource.getMessage("email.text", null, locale));
        variables.put("warning", messageSource.getMessage("email.warning", null, locale));
        variables.put("subject", messageSource.getMessage("email.subject", null, locale));

        for (int i = 0; i < code.length(); i++) {
            variables.put("code_" + (i + 1), String.valueOf(code.charAt(i)));
        }

        return variables;
    }

    public Map<String, String> getAccountConfirmationVariables(String language, String token) {
        Locale locale = language != null && language.equalsIgnoreCase("en") ? Locale.ENGLISH : new Locale("pt");

        Map<String, String> variables = new HashMap<>();
        variables.put("logo", messageSource.getMessage("logo.url", null, locale));
        variables.put("greeting", messageSource.getMessage("email.confirm.greeting", null, locale));
        variables.put("text1", messageSource.getMessage("email.confirm.text-1", null, locale));
        variables.put("text2", messageSource.getMessage("email.confirm.text-2", null, locale));
        variables.put("warning", messageSource.getMessage("email.confirm.warning", null, locale));
        variables.put("subject", messageSource.getMessage("email.confirm.subject", null, locale));
        variables.put("confirmationLink", API_URL + "/auth/confirm-account?token=" + token);

        return variables;
    }

    private VerificationCode verifyCode(String email, String code){
        Optional<VerificationCode> verificationCodeOptional = codeRepository.findByEmailAndCode(email, code);
        if(verificationCodeOptional.isEmpty()){
            throw new InvalidCodeException(messageSource.getMessage("error.invalidCode",null, LocaleContextHolder.getLocale()));
        }
        VerificationCode verificationCode = verificationCodeOptional.get();

        if (verificationCode.getExpiration().isBefore(Instant.now())) {
            throw new InvalidCodeException(messageSource.getMessage("error.expiredCode",null, LocaleContextHolder.getLocale()));
        }

        return verificationCode;

    }

    public String validateCode(String email, String code){
        VerificationCode verificationCode = this.verifyCode(email, code);
        String newCode = this.generateRandomCode();
        Instant expiration = Instant.now().plusSeconds(600);
        this.codeRepository.save(new VerificationCode(email, newCode, expiration));
        return newCode;
    }

    public void changePassword(String email, String newPassword, String code){
            VerificationCode verificationCode = this.verifyCode(email,code);
            Teacher teacher = (Teacher) repository.findByEmail(email);

            teacher.setPassword(passwordEncoder.encode(newPassword));
            repository.save(teacher);
            codeRepository.delete(verificationCode);
    }

    private String generateRandomCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append((int) (Math.random() * 10));
        }
        return code.toString();
    }

    public boolean confirmAccount(String token) {
        Optional<AccountConfirmationToken> optional = this.tokenRepository.findByToken(token);
        AccountConfirmationToken confirmation = optional.get();
        Teacher teacher = (Teacher) this.repository.findByEmail(confirmation.getEmail());

        if (optional.get().getExpiration().isBefore(Instant.now())) {
            this.repository.delete(teacher);
            return false;
        }

        if (teacher == null) {
            return false;
        }

        teacher.setIsActive(true);
        teacher.setCreatedAt(LocalDateTime.now());
        this.repository.save(teacher);
        this.tokenRepository.delete(confirmation);
        return true;
    }
    public void sendResetPasswordEmail(String email, String language) {
        Teacher teacher = (Teacher) this.repository.findByEmail(email);
        if(teacher == null){
            throw new NotFoundException(messageSource.getMessage("error.invalid.user", null, LocaleContextHolder.getLocale()));
        }
        String code = this.generateRandomCode();
        Instant expiration = Instant.now().plusSeconds(300);
        this.codeRepository.save(new VerificationCode(email, code, expiration));
        this.sendEmail(email,language, "confirm-password", getPasswordResetVariables(language, code));
    }

    public void sendConfirmAccountEmail(String email, String language) {
        String code = this.generateRandomCode();
        Instant expiration = Instant.now().plus(Duration.ofMinutes(15));
        tokenRepository.save(new AccountConfirmationToken(code, email, expiration));

        Map<String, String> variables = getAccountConfirmationVariables(language, code);
        this.sendEmail(email, language, "account-confirmation", variables);
    }

    @Transactional
    public void deleteTeacherByEmail(String email){
        this.repository.deleteTeacherByEmail(email);
    }

    @Transactional
    public void deleteTeacherById(UUID id) {
        Optional<Teacher> teacherOptional = this.repository.findById(id);
        if (teacherOptional.isEmpty()) {
            throw new NotFoundException("There is no teacher with this id");
        }
        Teacher teacher = teacherOptional.get();
        this.repository.delete(teacher);
        this.refreshTokenRepository.deleteByTeacherId(id);
        this.tokenRepository.deleteByEmail(teacher.getEmail());
        this.codeRepository.deleteByEmail(teacher.getEmail());
    }
}
