package com.nordgym.domain.models.service;

import com.nordgym.domain.enums.GenderTarget;
import com.nordgym.domain.enums.TrainingLevel;

public class TrainingProgramServiceModel {
    private Long id;
    private String header;
    private String programImagePath;
    private String mainGoal;
    private String workoutType;
    private Integer programDuration;
    private Integer daysPerWeek;
    private TrainingLevel trainingLevel;
    private GenderTarget genderTarget;
    private String workoutDescription;

    public TrainingProgramServiceModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getProgramImagePath() {
        return programImagePath;
    }

    public void setProgramImagePath(String programImagePath) {
        this.programImagePath = programImagePath;
    }

    public String getMainGoal() {
        return mainGoal;
    }

    public void setMainGoal(String mainGoal) {
        this.mainGoal = mainGoal;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public Integer getProgramDuration() {
        return programDuration;
    }

    public void setProgramDuration(Integer programDuration) {
        this.programDuration = programDuration;
    }

    public Integer getDaysPerWeek() {
        return daysPerWeek;
    }

    public void setDaysPerWeek(Integer daysPerWeek) {
        this.daysPerWeek = daysPerWeek;
    }

    public TrainingLevel getTrainingLevel() {
        return trainingLevel;
    }

    public void setTrainingLevel(TrainingLevel trainingLevel) {
        this.trainingLevel = trainingLevel;
    }

    public GenderTarget getGenderTarget() {
        return genderTarget;
    }

    public void setGenderTarget(GenderTarget genderTarget) {
        this.genderTarget = genderTarget;
    }

    public String getWorkoutDescription() {
        return workoutDescription;
    }

    public void setWorkoutDescription(String workoutDescription) {
        this.workoutDescription = workoutDescription;
    }
}
