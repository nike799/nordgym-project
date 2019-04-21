package com.nordgym.service;

import com.nordgym.errors.ResourceNotFoundException;
import com.nordgym.domain.entities.TrainingProgram;
import com.nordgym.domain.enums.GenderTarget;
import com.nordgym.domain.enums.TrainingLevel;
import com.nordgym.domain.models.service.TrainingProgramServiceModel;
import com.nordgym.errors.EmptyDataBaseException;
import com.nordgym.repository.TrainingProgramRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TrainingProgramServiceImplTest {
    @Autowired
    private TrainingProgramRepository trainingProgramRepository;
    private ModelMapper modelMapper;
    private TrainingProgramServiceImpl trainingProgramService;

    @Before
    public void setUp() throws Exception {
        this.modelMapper = new ModelMapper();
        this.trainingProgramService = new TrainingProgramServiceImpl(trainingProgramRepository, modelMapper);
    }

    @Test
    public void registerTrainingProgram() {
        Long id = this.trainingProgramService.registerTrainingProgram(getTrainingProgramServiceModel(), "image");
        Assert.assertNotNull(id);
    }

    @Test
    public void findById_returnsCorrect() {
        TrainingProgram trainingProgram = this.modelMapper.map(getTrainingProgramServiceModel(), TrainingProgram.class);
        TrainingProgramServiceModel expected = this.modelMapper.map(this.trainingProgramRepository.save(trainingProgram), TrainingProgramServiceModel.class);
        TrainingProgramServiceModel actual = this.trainingProgramService.findById(expected.getId());

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getHeader(), actual.getHeader());
        Assert.assertEquals(expected.getProgramImagePath(), actual.getProgramImagePath());
        Assert.assertEquals(expected.getMainGoal(), actual.getMainGoal());
        Assert.assertEquals(expected.getWorkoutType(), actual.getWorkoutType());
        Assert.assertEquals(expected.getDaysPerWeek(), actual.getDaysPerWeek());
        Assert.assertEquals(expected.getGenderTarget(), actual.getGenderTarget());
        Assert.assertEquals(expected.getProgramDuration(), actual.getProgramDuration());
        Assert.assertEquals(expected.getTrainingLevel(), actual.getTrainingLevel());
        Assert.assertEquals(expected.getWorkoutDescription(), actual.getWorkoutDescription());
    }

    @Test
    public void findLastTrainingProgram_returnsCorrect() {
        TrainingProgram trainingProgram1 = this.modelMapper.map(getTrainingProgramServiceModel(), TrainingProgram.class);
        trainingProgram1.setHeader("Header1");
        TrainingProgram trainingProgram2 = this.modelMapper.map(getTrainingProgramServiceModel(), TrainingProgram.class);
        trainingProgram2.setHeader("Header2");
        TrainingProgram trainingProgram3 = this.modelMapper.map(getTrainingProgramServiceModel(), TrainingProgram.class);
        trainingProgram3.setHeader("Header3");
        this.trainingProgramRepository.saveAll(
                new ArrayList<>() {{
                    add(trainingProgram1);
                    add(trainingProgram2);
                    add(trainingProgram3);
                }});
        TrainingProgramServiceModel expected = this.modelMapper.map(this.trainingProgramRepository.save(trainingProgram3), TrainingProgramServiceModel.class);
        TrainingProgramServiceModel actual = this.trainingProgramService.findLastTrainingProgram();

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getHeader(), actual.getHeader());
    }

    @Test
    public void getAllTrainingPrograms_returnsCorrect() {
        TrainingProgram trainingProgram1 = this.modelMapper.map(getTrainingProgramServiceModel(), TrainingProgram.class);
        TrainingProgram trainingProgram2 = this.modelMapper.map(getTrainingProgramServiceModel(), TrainingProgram.class);
        TrainingProgram trainingProgram3 = this.modelMapper.map(getTrainingProgramServiceModel(), TrainingProgram.class);
        this.trainingProgramRepository.saveAll(
                new ArrayList<>() {{
                    add(trainingProgram1);
                    add(trainingProgram2);
                    add(trainingProgram3);
                }});
        int expected = 3;
        int actual = this.trainingProgramService.getAllTrainingPrograms().size();

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = EmptyDataBaseException.class)
    public void getAllTrainingPrograms_ifDatabaseIsEmpty_throws_EmptyDataBaseException() {
        this.trainingProgramService.getAllTrainingPrograms();
    }

    @Test
    public void editTrainingProgram_isSuccess() throws IllegalAccessException, NoSuchFieldException, IOException {
        TrainingProgram trainingProgram = this.modelMapper.map(getTrainingProgramServiceModel(), TrainingProgram.class);
        TrainingProgramServiceModel expected = modelMapper.map(this.trainingProgramRepository.save(trainingProgram), TrainingProgramServiceModel.class);
        expected.setHeader("Changed Header");
        expected.setProgramImagePath("Changed /path");
        expected.setMainGoal("Changed Lose Fat");
        expected.setWorkoutType("Changed WorkoutType");
        expected.setProgramDuration(20);
        expected.setDaysPerWeek(3);
        expected.setTrainingLevel(TrainingLevel.BEGINNER);
        expected.setGenderTarget(GenderTarget.FEMALE);
        expected.setWorkoutDescription("Changed workoutDescription");
        MultipartFile image = new MockMultipartFile("files", "filename.txt", "text/plain", "hello".getBytes(StandardCharsets.UTF_8));

        this.trainingProgramService.editTrainingProgram(expected, expected.getId(), image);
        TrainingProgramServiceModel actual = modelMapper.map(this.trainingProgramRepository.findAll().get(0), TrainingProgramServiceModel.class);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getHeader(), actual.getHeader());
        Assert.assertEquals(expected.getProgramImagePath(), actual.getProgramImagePath());
        Assert.assertEquals(expected.getMainGoal(), actual.getMainGoal());
        Assert.assertEquals(expected.getWorkoutType(), actual.getWorkoutType());
        Assert.assertEquals(expected.getDaysPerWeek(), actual.getDaysPerWeek());
        Assert.assertEquals(expected.getGenderTarget(), actual.getGenderTarget());
        Assert.assertEquals(expected.getProgramDuration(), actual.getProgramDuration());
        Assert.assertEquals(expected.getTrainingLevel(), actual.getTrainingLevel());
        Assert.assertEquals(expected.getWorkoutDescription(), actual.getWorkoutDescription());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void editTrainingProgram_ifTrainingProgramWithSuchIdNotExists_throws_ResourceNotFoundException() throws IllegalAccessException, NoSuchFieldException, IOException {
        MultipartFile image = new MockMultipartFile("files", "filename.txt", "text/plain", "hello".getBytes(StandardCharsets.UTF_8));
        this.trainingProgramService.editTrainingProgram(new TrainingProgramServiceModel(), 100L, image);
    }

    @Test
    public void deleteTrainingProgram() {
        TrainingProgram trainingProgram = this.modelMapper.map(getTrainingProgramServiceModel(), TrainingProgram.class);
        this.trainingProgramRepository.save(trainingProgram);
        Assert.assertEquals(1, this.trainingProgramRepository.count());
        this.trainingProgramService.deleteTrainingProgram(trainingProgram.getId());
        Assert.assertEquals(0, this.trainingProgramRepository.count());
    }

    private TrainingProgramServiceModel getTrainingProgramServiceModel() {
        TrainingProgramServiceModel model = new TrainingProgramServiceModel();
        model.setHeader("Header");
        model.setProgramImagePath("/path");
        model.setMainGoal("Lose Fat");
        model.setWorkoutType("WorkoutType");
        model.setProgramDuration(10);
        model.setDaysPerWeek(4);
        model.setTrainingLevel(TrainingLevel.ADVANCED);
        model.setGenderTarget(GenderTarget.BOTH);
        model.setWorkoutDescription("WorkoutDescription");
        return model;
    }
}