package com.nordgym.domain.entities;

import com.nordgym.domain.enums.GenderTarget;
import com.nordgym.domain.enums.TrainingLevel;

import javax.persistence.*;

@Entity
@Table(name = "training_programs")
public class TrainingProgram extends BaseEntity {
    private String header;
    private String programImagePath;
    private String mainGoal;
    private String workoutType;
    private Integer programDuration;
    private Integer daysPerWeek;
    private TrainingLevel trainingLevel;
    private GenderTarget genderTarget;
    private String workoutDescription;

    public TrainingProgram() {
    }

    @Column(name = "header",nullable = false)
    public String getHeader() {
        return header;
    }

    @Column(name = "program_image_path",nullable = false)
    public String getProgramImagePath() {
        return programImagePath;
    }

    @Column(name = "main_goal",nullable = false)
    public String getMainGoal() {
        return mainGoal;
    }

    @Column(name = "workout_type",nullable = false)
    public String getWorkoutType() {
        return workoutType;
    }

    @Column(name = "program_duration",nullable = false)
    public Integer getProgramDuration() {
        return programDuration;
    }

    @Column(name = "days_per_week",nullable = false)
    public Integer getDaysPerWeek() {
        return daysPerWeek;
    }

    @Enumerated(value = EnumType.STRING)
    @Column(name = "training_level",nullable = false)
    public TrainingLevel getTrainingLevel() {
        return trainingLevel;
    }


    @Enumerated(EnumType.STRING)
    @Column(name = "gender_target",nullable = false)
    public GenderTarget getGenderTarget() {
        return genderTarget;
    }

    @Column(name = "workout_description", columnDefinition = "TEXT",nullable = false)
    public String getWorkoutDescription() {
        return workoutDescription;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setProgramImagePath(String programImagePath) {
        this.programImagePath = programImagePath;
    }

    public void setMainGoal(String mainGoal) {
        this.mainGoal = mainGoal;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public void setProgramDuration(Integer programDuration) {
        this.programDuration = programDuration;
    }

    public void setDaysPerWeek(Integer daysPerWeek) {
        this.daysPerWeek = daysPerWeek;
    }

    public void setTrainingLevel(TrainingLevel trainingLevel) {
        this.trainingLevel = trainingLevel;
    }

    public void setGenderTarget(GenderTarget genderTarget) {
        this.genderTarget = genderTarget;
    }

    public void setWorkoutDescription(String workoutDescription) {
        this.workoutDescription = workoutDescription;
    }
}
