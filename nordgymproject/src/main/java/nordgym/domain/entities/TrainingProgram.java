package nordgym.domain.entities;

import nordgym.domain.enums.GenderTarget;

import javax.persistence.*;

@Entity
@Table(name = "training_programs")
public class TrainingProgram extends BaseEntity {
    private String programImagePath;
    private String mainGoal;
    private String trainingType;
    private Integer programDuration;
    private Integer daysPerWeek;
    private Integer timePerWorkout;
    private GenderTarget genderTarget;
    private String workoutDescription;

    public TrainingProgram() {
    }

    @Column(name = "program_image_path")
    public String getProgramImagePath() {
        return programImagePath;
    }

    @Column(name = "main_goal")
    public String getMainGoal() {
        return mainGoal;
    }

    @Column(name = "training_type")
    public String getTrainingType() {
        return trainingType;
    }

    @Column(name = "program_duration")
    public Integer getProgramDuration() {
        return programDuration;
    }

    @Column(name = "days_per_week")
    public Integer getDaysPerWeek() {
        return daysPerWeek;
    }

    @Column(name = "time_per_workout")
    public Integer getTimePerWorkout() {
        return timePerWorkout;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_target")
    public GenderTarget getGenderTarget() {
        return genderTarget;
    }

    @Column(name = "workout_description", columnDefinition = "TEXT")
    public String getWorkoutDescription() {
        return workoutDescription;
    }

    public void setProgramImagePath(String programImagePath) {
        this.programImagePath = programImagePath;
    }

    public void setMainGoal(String mainGoal) {
        this.mainGoal = mainGoal;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    public void setProgramDuration(Integer programDuration) {
        this.programDuration = programDuration;
    }

    public void setDaysPerWeek(Integer daysPerWeek) {
        this.daysPerWeek = daysPerWeek;
    }

    public void setTimePerWorkout(Integer timePerWorkout) {
        this.timePerWorkout = timePerWorkout;
    }

    public void setGenderTarget(GenderTarget genderTarget) {
        this.genderTarget = genderTarget;
    }

    public void setWorkoutDescription(String workoutDescription) {
        this.workoutDescription = workoutDescription;
    }
}
