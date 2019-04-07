package nordgym.web.controllers;

import nordgym.service.TrainingProgramService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/trainings")
public class TrainingsController extends BaseController {
    private final TrainingProgramService trainingProgramService;
    private final ModelMapper modelMapper;

    public TrainingsController(TrainingProgramService trainingProgramService, ModelMapper modelMapper) {
        this.trainingProgramService = trainingProgramService;
        this.modelMapper = modelMapper;
    }
    @GetMapping("/advanced")
    public ModelAndView getTrainingsAdvanced( ModelAndView modelAndView) {
        modelAndView.addObject("trainingProgramModel", this.trainingProgramService.findLastTrainingProgram());
        return this.view("trainings-advanced");
    }

    @GetMapping("/advanced/{id}")
    public ModelAndView getTrainingsAdvancedWithId(@PathVariable Long id, ModelAndView modelAndView) {
        modelAndView.addObject("trainingProgramModel", this.trainingProgramService.findById(id));
        return this.view("trainings-advanced");
    }
}
