package nordgym.domain.models.binding;

import nordgym.GlobalConstants;
import nordgym.domain.enums.GenderTarget;
import nordgym.domain.enums.TrainingLevel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TrainingProgramBindingModel {
    private String header;
    private String programImagePath;
    private String mainGoal;
    private String workoutType;
    private Integer programDuration;
    private Integer daysPerWeek;
    private TrainingLevel trainingLevel;
    private GenderTarget genderTarget;
    private String workoutDescription;

    public TrainingProgramBindingModel() {
    }

    @NotNull(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotEmpty(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotBlank(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    public String getHeader() {
        return header;
    }

    @NotNull(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotEmpty(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotBlank(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    public String getProgramImagePath() {
        return programImagePath;
    }

    @NotNull(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotEmpty(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotBlank(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    public String getMainGoal() {
        return mainGoal;
    }

    @NotNull(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotEmpty(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotBlank(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    public String getWorkoutType() {
        return workoutType;
    }

    @NotNull(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotEmpty(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotBlank(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    public Integer getProgramDuration() {
        return programDuration;
    }

    @NotNull(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotEmpty(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotBlank(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    public Integer getDaysPerWeek() {
        return daysPerWeek;
    }

    @NotNull(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotEmpty(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    public TrainingLevel getTrainingLevel() {
        return trainingLevel;
    }

    @NotNull(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotEmpty(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotBlank(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    public GenderTarget getGenderTarget() {
        return genderTarget;
    }

    @NotNull(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotEmpty(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotBlank(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
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
