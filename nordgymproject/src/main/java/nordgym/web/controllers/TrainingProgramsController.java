package nordgym.web.controllers;

import nordgym.domain.models.binding.TrainingProgramBindingModel;
import nordgym.domain.models.service.TrainingProgramServiceModel;
import nordgym.domain.models.view.TrainingProgramSidebarModel;
import nordgym.service.TrainingProgramService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
public class TrainingProgramsController extends BaseController {
    private final TrainingProgramService trainingProgramService;
    private final ModelMapper modelMapper;

    public TrainingProgramsController(TrainingProgramService trainingProgramService, ModelMapper modelMapper) {
        this.trainingProgramService = trainingProgramService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/training-programs")
    public ModelAndView getTrainingProgram(ModelAndView modelAndView) {
        modelAndView.addObject("trainingProgramModel", this.trainingProgramService.findLastTrainingProgram());
        return this.view("training-programs", modelAndView);
    }

    @GetMapping("/training-programs/{id}")
    public ModelAndView getTrainingProgramById(@PathVariable Long id, ModelAndView modelAndView) {
        modelAndView.addObject("trainingProgramModel", this.trainingProgramService.findById(id));
        return this.view("training-programs", modelAndView);
    }
    @PostMapping("/training-programs/{id}")
    public ModelAndView editTrainingProgram(@ModelAttribute TrainingProgramBindingModel trainingProgramBindingModel,@PathVariable Long id, @RequestParam("programImage") MultipartFile image) throws IOException, NoSuchFieldException, IllegalAccessException {
        TrainingProgramServiceModel trainingProgramServiceModel = this.modelMapper.map(trainingProgramBindingModel,TrainingProgramServiceModel.class);
        this.trainingProgramService.editTrainingProgram(trainingProgramServiceModel,id,image);
        return this.redirect("/training-programs/"+id);
    }

    @GetMapping(value = "/fetch/training-programs-all", produces = "application/json")
    @ResponseBody
    public List<TrainingProgramSidebarModel> getAllTrainingPrograms() {
        return
                List.of(
                        this.modelMapper.map(
                                this.trainingProgramService.getAllTrainingPrograms().toArray(), TrainingProgramSidebarModel[].class));
    }
}
