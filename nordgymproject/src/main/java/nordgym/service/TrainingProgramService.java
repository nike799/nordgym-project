package nordgym.service;

import nordgym.domain.models.service.TrainingProgramServiceModel;

public interface TrainingProgramService {
    Long registerTrainingProgram(TrainingProgramServiceModel trainingProgramServiceModel,String programImageName);
    TrainingProgramServiceModel findById(Long id);
    TrainingProgramServiceModel findLastTrainingProgram();
}
