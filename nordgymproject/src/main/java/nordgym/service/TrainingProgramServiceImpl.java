package nordgym.service;


import nordgym.GlobalConstants;
import nordgym.domain.entities.TrainingProgram;
import nordgym.domain.models.service.TrainingProgramServiceModel;
import nordgym.domain.models.view.TrainingProgramSidebarModel;
import nordgym.error.EmptyDataBaseException;
import nordgym.error.ResourceNotFoundException;
import nordgym.repository.TrainingProgramRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingProgramServiceImpl implements TrainingProgramService {
    private TrainingProgramRepository trainingProgramRepository;
    private ModelMapper modelMapper;

    @Autowired
    public TrainingProgramServiceImpl(TrainingProgramRepository trainingProgramRepository, ModelMapper modelMapper) {
        this.trainingProgramRepository = trainingProgramRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Long registerTrainingProgram(TrainingProgramServiceModel trainingProgramServiceModel, String programImageName) {
        TrainingProgram trainingProgram = this.modelMapper.map(trainingProgramServiceModel, TrainingProgram.class);
        trainingProgram.setProgramImagePath(programImageName);
        return this.trainingProgramRepository.save(trainingProgram).getId();
    }

    @Override
    public TrainingProgramServiceModel findById(Long id) {
        TrainingProgram trainingProgram = this.trainingProgramRepository.findById(id).orElse(null);
        if (trainingProgram == null) {
            throw new ResourceNotFoundException(String.format(GlobalConstants.SUCH_TRAINING_PROGRAM_DOESNT_EXISTS, id));
        }
        return this.modelMapper.map(trainingProgram, TrainingProgramServiceModel.class);
    }

    @Override
    public TrainingProgramServiceModel findLastTrainingProgram() {
        TrainingProgram trainingProgram = null;
        if (this.trainingProgramRepository.findAll().size() > 0) {
            trainingProgram = this.trainingProgramRepository.findAll().get(this.trainingProgramRepository.findAll().size() - 1);
        }
        if(trainingProgram == null){throw new EmptyDataBaseException(GlobalConstants.SORRY_NO_UPLOADED_RESOURCES_AT_THIS_MOMENT);}
        return this.modelMapper.map(trainingProgram,TrainingProgramServiceModel.class);
    }

    @Override
    public List<TrainingProgramServiceModel> getAllTrainingPrograms() {
        List<TrainingProgramServiceModel> trainingProgramServiceModels =
                List.of(this.modelMapper.map(this.trainingProgramRepository.findAll().toArray(), TrainingProgramServiceModel[].class));
        if (trainingProgramServiceModels.size() == 0){throw new EmptyDataBaseException(GlobalConstants.SORRY_NO_UPLOADED_RESOURCES_AT_THIS_MOMENT);}
        return trainingProgramServiceModels;
    }
}
