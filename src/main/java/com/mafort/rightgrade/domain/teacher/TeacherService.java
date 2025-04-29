package com.mafort.rightgrade.domain.teacher;

import com.mafort.rightgrade.domain.authentication.PasswordValidationRequest;
import com.mafort.rightgrade.domain.verificationCode.VerificationCode;
import com.mafort.rightgrade.domain.verificationCode.VerificationCodeRepository;
import com.mafort.rightgrade.domain.email.MailgunEmailService;
import com.mafort.rightgrade.infra.exception.InvalidCodeException;
import com.mafort.rightgrade.infra.exception.InvalidPasswordException;
import com.mafort.rightgrade.infra.exception.NotFoundException;
import com.mafort.rightgrade.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class TeacherService {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private TeacherRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailgunEmailService mailgunEmailService;
    @Autowired
    VerificationCodeRepository codeRepository;
    @Autowired
    private TokenService tokenService;

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

    public void sendEmail(String email, String language){
        Teacher teacher = (Teacher) this.repository.findByEmail(email);
        if(teacher == null){
            throw new NotFoundException(messageSource.getMessage("error.invalid.user", null, LocaleContextHolder.getLocale()));
        }

        String code = this.generateRandomCode();
        Instant expiration = Instant.now().plusSeconds(300);
        this.codeRepository.save(new VerificationCode(email, code, expiration));
        Map<String, String> variables = getVariables(language, code);
        mailgunEmailService.sendEmail(email,variables.get("subject"), "code-confirm", variables);
    }

    Map<String, String> getVariables(String language, String code){
        Locale locale = language != null && language.equalsIgnoreCase("en")
                ? Locale.ENGLISH
                : new Locale("pt");

        Map<String, String> variables = new HashMap<>();
        variables.put("logo", messageSource.getMessage("logo.url", null, locale));
        variables.put("greeting", messageSource.getMessage("email.greeting", null, locale));
        variables.put("text", messageSource.getMessage("email.text", null, locale));
        variables.put("warning", messageSource.getMessage("email.warning", null, locale));
        variables.put( "subject",messageSource.getMessage("email.subject",null, locale));

        for (int i = 0; i < code.length(); i++) {
            variables.put("code_" + (i + 1), String.valueOf(code.charAt(i)));
        }

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

}
