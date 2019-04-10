package nordgym.web.controllers;

import nordgym.domain.models.view.TrainingProgramSidebarModel;
import nordgym.service.TrainingProgramService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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

    @GetMapping(value = "/fetch/training-programs-all", produces = "application/json")
    @ResponseBody
    public List<TrainingProgramSidebarModel> getAllTrainingPrograms() {
        return
                List.of(
                        this.modelMapper.map(
                                this.trainingProgramService.getAllTrainingPrograms().toArray(), TrainingProgramSidebarModel[].class));
    }
}
