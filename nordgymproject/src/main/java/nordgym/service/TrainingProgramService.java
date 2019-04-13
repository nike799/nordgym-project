package nordgym.service;

import nordgym.domain.models.service.TrainingProgramServiceModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TrainingProgramService {
    Long registerTrainingProgram(TrainingProgramServiceModel trainingProgramServiceModel,String programImageName);
    TrainingProgramServiceModel findById(Long id);
    TrainingProgramServiceModel findLastTrainingProgram();
    List<TrainingProgramServiceModel> getAllTrainingPrograms();
    Long editTrainingProgram(TrainingProgramServiceModel trainingProgramServiceModel, Long id, MultipartFile image) throws IOException, NoSuchFieldException, IllegalAccessException;

    void deleteTrainingProgram(Long id);
}
