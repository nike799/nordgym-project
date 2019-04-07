package nordgym.domain.models.binding;

import nordgym.domain.enums.GenderTarget;

public class TrainingProgramBindingModel {
    private String programImagePath;
    private String mainGoal;
    private String trainingType;
    private Integer programDuration;
    private Integer daysPerWeek;
    private Integer timePerWorkout;
    private GenderTarget genderTarget;
    private String workoutDescription;

    public TrainingProgramBindingModel() {
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

    public String getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
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

    public Integer getTimePerWorkout() {
        return timePerWorkout;
    }

    public void setTimePerWorkout(Integer timePerWorkout) {
        this.timePerWorkout = timePerWorkout;
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
