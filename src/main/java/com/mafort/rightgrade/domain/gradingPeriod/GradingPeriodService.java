package com.mafort.rightgrade.domain.gradingPeriod;

import com.mafort.rightgrade.domain.group.Group;
import com.mafort.rightgrade.domain.group.GroupRepository;
import com.mafort.rightgrade.infra.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GradingPeriodService {
    @Autowired
    private GradingPeriodRepository gradingPeriodRepository;
    @Autowired
    private GroupRepository groupRepository;

    public GradingPeriod create(CreateGradingPeriod createGradingPeriod){
        Optional<Group> group = groupRepository.findById(createGradingPeriod.groupId());
        if (group.isEmpty()) {
            throw new NotFoundException("The group id is invalid");
        }
        GradingPeriod gradingPeriod = new GradingPeriod(createGradingPeriod, group.get());
        this.gradingPeriodRepository.save(gradingPeriod);
        return gradingPeriod;
    }

    public List<GradingPeriod> getAll(){
        return this.gradingPeriodRepository.findAll();
    }

    public void edit(EditGradingPeriod editGradingPeriod, UUID gradingPeriodId){
        Optional<GradingPeriod> optionalGradingPeriod = gradingPeriodRepository.findById(gradingPeriodId);
        if(optionalGradingPeriod.isEmpty()){
            throw new NotFoundException("Grading period not found for the provided ID.");
        }
        GradingPeriod existingGradingPeriod = optionalGradingPeriod.get();

        existingGradingPeriod.setName(editGradingPeriod.name());
        gradingPeriodRepository.save(existingGradingPeriod);
    }
}
