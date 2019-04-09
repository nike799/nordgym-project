package nordgym.web.controllers;

import nordgym.service.TrainingProgramService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TrainingsController extends BaseController {
    private final TrainingProgramService trainingProgramService;

    public TrainingsController(TrainingProgramService trainingProgramService) {
        this.trainingProgramService = trainingProgramService;
    }
    @GetMapping("/training-programs")
    public ModelAndView getTrainingsAdvanced( ModelAndView modelAndView) {
        modelAndView.addObject("trainingProgramModel", this.trainingProgramService.findLastTrainingProgram());
        return this.view("training-programs",modelAndView);
    }

    @GetMapping("/training-programs/{id}")
    public ModelAndView getTrainingsAdvancedWithId(@PathVariable Long id, ModelAndView modelAndView) {
        modelAndView.addObject("trainingProgramModel", this.trainingProgramService.findById(id));
        return this.view("training-programs",modelAndView);
    }
}
