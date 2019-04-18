package nordgym.domain.models.binding;
import nordgym.constants.GlobalConstants;
import nordgym.domain.enums.GenderTarget;
import nordgym.domain.enums.TrainingLevel;
import javax.validation.constraints.*;

public class TrainingProgramBindingModel {
    private Long id;
    private String header;
    private String mainGoal;
    private String workoutType;
    private Integer programDuration;
    private Integer daysPerWeek;
    private TrainingLevel trainingLevel;
    private GenderTarget genderTarget;
    private String workoutDescription;

    public TrainingProgramBindingModel() {
    }

    public Long getId() {
        return id;
    }

    @NotNull(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotBlank(message = GlobalConstants.WRONG_FORMAT)
    public String getHeader() {
        return header;
    }

    @NotNull(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotBlank(message = GlobalConstants.WRONG_FORMAT)
    public String getMainGoal() {
        return mainGoal;
    }

    @NotNull(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotBlank(message = GlobalConstants.WRONG_FORMAT)
    public String getWorkoutType() {
        return workoutType;
    }

    @NotNull(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @Min(value = 4,message = GlobalConstants.CANT_BE_LESS_THEN_FOUR)
    @Max(value = 24,message = GlobalConstants.CANT_BE_MORE_THEN_TWENTY_FOUR)
    public Integer getProgramDuration() {
        return programDuration;
    }

    @NotNull(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @Min(value = 1,message = GlobalConstants.CANT_BE_LESS_THEN_ONE)
    @Max(value = 7, message = GlobalConstants.CANT_BE_MORE_THEN_SEVEN)
    public Integer getDaysPerWeek() {
        return daysPerWeek;
    }

    @NotNull(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    public TrainingLevel getTrainingLevel() {
        return trainingLevel;
    }

    @NotNull(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    public GenderTarget getGenderTarget() {
        return genderTarget;
    }

    @NotNull(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotBlank(message = GlobalConstants.WRONG_FORMAT)
    public String getWorkoutDescription() {
        return workoutDescription;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setHeader(String header) {
        this.header = header;
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
