package com.nordgym.web.controllers;

import com.nordgym.domain.models.binding.TrainingProgramBindingModel;
import com.nordgym.domain.models.service.TrainingProgramServiceModel;
import com.nordgym.domain.models.view.TrainingProgramSidebarModel;
import com.nordgym.service.TrainingProgramService;
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

    @PostMapping(value = "/training-programs/{id}", params = "update")
    public ModelAndView editTrainingProgram(@ModelAttribute TrainingProgramBindingModel trainingProgramBindingModel, @PathVariable Long id, @RequestParam("programImage") MultipartFile image) throws IllegalAccessException, NoSuchFieldException, IOException {
        TrainingProgramServiceModel trainingProgramServiceModel = this.modelMapper.map(trainingProgramBindingModel, TrainingProgramServiceModel.class);
        this.trainingProgramService.editTrainingProgram(trainingProgramServiceModel, id, image);
        return this.redirect("/training-programs/" + id);
    }

    @PostMapping(value = "/training-programs/{id}", params = "delete")
    public ModelAndView deleteTrainingProgram(@PathVariable Long id) {
        this.trainingProgramService.deleteTrainingProgram(id);
        return this.redirect("/training-programs");
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
