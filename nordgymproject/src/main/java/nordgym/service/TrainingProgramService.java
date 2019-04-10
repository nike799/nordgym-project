package nordgym.service;

import nordgym.domain.models.service.TrainingProgramServiceModel;

import java.util.List;

public interface TrainingProgramService {
    Long registerTrainingProgram(TrainingProgramServiceModel trainingProgramServiceModel,String programImageName);
    TrainingProgramServiceModel findById(Long id);
    TrainingProgramServiceModel findLastTrainingProgram();
    List<TrainingProgramServiceModel> getAllTrainingPrograms();
}
