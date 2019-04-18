package nordgym.service;

import nordgym.constants.GlobalConstants;
import nordgym.constants.GlobalUpdateEntity;
import nordgym.domain.entities.TrainingProgram;
import nordgym.domain.models.service.TrainingProgramServiceModel;
import nordgym.error.EmptyDataBaseException;
import nordgym.error.ResourceNotFoundException;
import nordgym.repository.TrainingProgramRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class TrainingProgramServiceImpl implements TrainingProgramService {
    private static final String IMAGES = "/images/";
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
        if (trainingProgram == null) {
            throw new EmptyDataBaseException(GlobalConstants.SORRY_NO_UPLOADED_RESOURCES_AT_THIS_MOMENT);
        }
        return this.modelMapper.map(trainingProgram, TrainingProgramServiceModel.class);
    }

    @Override
    public List<TrainingProgramServiceModel> getAllTrainingPrograms() {
        List<TrainingProgramServiceModel> trainingProgramServiceModels =
                List.of(this.modelMapper.map(this.trainingProgramRepository.findAll().toArray(), TrainingProgramServiceModel[].class));
        if (trainingProgramServiceModels.size() == 0) {
            throw new EmptyDataBaseException(GlobalConstants.SORRY_NO_UPLOADED_RESOURCES_AT_THIS_MOMENT);
        }
        return trainingProgramServiceModels;
    }

    @Override
    public Long editTrainingProgram(TrainingProgramServiceModel trainingProgramServiceModel, Long id, MultipartFile image) throws IOException, NoSuchFieldException, IllegalAccessException {
        TrainingProgram trainingProgram = this.trainingProgramRepository.findById(id).orElse(null);
        if (trainingProgram == null) {
            throw new ResourceNotFoundException(String.format(GlobalConstants.SUCH_TRAINING_PROGRAM_DOESNT_EXISTS, id));
        }
        if (image.getOriginalFilename()!= null && !image.getOriginalFilename().isEmpty()) {
            File dest = new File(GlobalConstants.IMAGES_PATH + image.getOriginalFilename());
            image.transferTo(dest);
            trainingProgramServiceModel.setProgramImagePath(IMAGES.concat(image.getOriginalFilename()));
        }
        GlobalUpdateEntity.updateEntity(trainingProgramServiceModel,trainingProgram);
        return this.trainingProgramRepository.save(trainingProgram).getId();
    }

    @Override
    public void deleteTrainingProgram(Long id) {
      if(this.trainingProgramRepository.findById(id).isEmpty()){
          throw new ResourceNotFoundException(String.format(GlobalConstants.SUCH_TRAINING_PROGRAM_DOESNT_EXISTS,id));
      }
      this.trainingProgramRepository.deleteById(id);
    }

}
