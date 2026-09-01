package com.example.gym_tracker;

import com.example.gym_tracker.model.BodyMetrics;
import com.example.gym_tracker.model.CrowdLog;
import com.example.gym_tracker.model.Exercise;
import com.example.gym_tracker.model.WorkoutLog;
import com.example.gym_tracker.repository.BodyMetricsRepository;
import com.example.gym_tracker.repository.CrowdLogRepository;
import com.example.gym_tracker.repository.ExerciseRepository;
import com.example.gym_tracker.repository.WorkoutLogRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GymTrackerApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private WorkoutLogRepository workoutLogRepository;

    @Autowired
    private BodyMetricsRepository bodyMetricsRepository;

    @Autowired
    private CrowdLogRepository crowdLogRepository;


    @BeforeEach
    void cleanDatabase() {
        workoutLogRepository.deleteAll();
        bodyMetricsRepository.deleteAll();
        crowdLogRepository.deleteAll();
        exerciseRepository.deleteAll();
    }


    // --------------------------------------------------
    // Application startup
    // --------------------------------------------------

    @Test
    void contextLoads() {
    }


    // --------------------------------------------------
    // Exercise tests
    // --------------------------------------------------

    @Test
    void shouldCreateAndGetExercise() throws Exception {

        Exercise exercise = new Exercise();
        exercise.setName("Bench Press");
        exercise.setMuscleGroup("Chest");

        mockMvc.perform(post("/api/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exercise)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bench Press"))
                .andExpect(jsonPath("$.muscleGroup").value("Chest"))
                .andExpect(jsonPath("$.id").exists());

        mockMvc.perform(get("/api/exercises"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Bench Press"))
                .andExpect(jsonPath("$[0].muscleGroup").value("Chest"));
    }


    // --------------------------------------------------
    // Body metrics tests
    // --------------------------------------------------

    @Test
    void shouldCreateUpdateAndDeleteBodyMetrics() throws Exception {

        BodyMetrics metrics = new BodyMetrics();

        metrics.setWeight(75.0);
        metrics.setMuscleMass(32.0);
        metrics.setFatPercentage(18.0);

        String response = mockMvc.perform(
                        post("/api/body-metrics")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(metrics))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weight").value(75.0))
                .andExpect(jsonPath("$.muscleMass").value(32.0))
                .andExpect(jsonPath("$.fatPercentage").value(18.0))
                .andExpect(jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BodyMetrics saved =
                objectMapper.readValue(response, BodyMetrics.class);

        // Get history
        mockMvc.perform(get("/api/body-metrics/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].weight").value(75.0))
                .andExpect(jsonPath("$[0].muscleMass").value(32.0))
                .andExpect(jsonPath("$[0].fatPercentage").value(18.0));

        // Update weight
        BodyMetrics update = new BodyMetrics();
        update.setWeight(73.5);

        mockMvc.perform(
                        put("/api/body-metrics/" + saved.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(update))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weight").value(73.5))
                .andExpect(jsonPath("$.muscleMass").value(32.0))
                .andExpect(jsonPath("$.fatPercentage").value(18.0));

        // Delete
        mockMvc.perform(
                        delete("/api/body-metrics/" + saved.getId())
                )
                .andExpect(status().isOk());

        // Verify deletion
        mockMvc.perform(get("/api/body-metrics/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }


    // --------------------------------------------------
    // Workout tests
    // --------------------------------------------------

    @Test
    void shouldCreateWorkoutAndGetProgress() throws Exception {

        // Create an exercise first
        Exercise exercise = new Exercise();
        exercise.setName("Squat");
        exercise.setMuscleGroup("Legs");

        Exercise savedExercise =
                exerciseRepository.save(exercise);

        // Create workout
        WorkoutLog workout = new WorkoutLog();

        workout.setExercise(savedExercise);
        workout.setWeightLifted(100.0);
        workout.setReps(8);
        workout.setSets(3);

        String response = mockMvc.perform(
                        post("/api/workouts/log")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(workout))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weightLifted").value(100.0))
                .andExpect(jsonPath("$.reps").value(8))
                .andExpect(jsonPath("$.sets").value(3))
                .andExpect(jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        WorkoutLog savedWorkout =
                objectMapper.readValue(response, WorkoutLog.class);

        // Get exercise progress
        mockMvc.perform(
                        get("/api/workouts/progress/"
                                + savedExercise.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].weightLifted").value(100.0))
                .andExpect(jsonPath("$[0].reps").value(8))
                .andExpect(jsonPath("$[0].sets").value(3));

        // Update workout
        WorkoutLog update = new WorkoutLog();

        update.setWeightLifted(105.0);
        update.setReps(6);
        update.setSets(3);

        mockMvc.perform(
                        put("/api/workouts/log/"
                                + savedWorkout.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(update))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weightLifted").value(105.0))
                .andExpect(jsonPath("$.reps").value(6))
                .andExpect(jsonPath("$.sets").value(3));

        // Delete workout
        mockMvc.perform(
                        delete("/api/workouts/log/"
                                + savedWorkout.getId())
                )
                .andExpect(status().isOk());

        // Verify deletion
        mockMvc.perform(
                        get("/api/workouts/progress/"
                                + savedExercise.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }


    // --------------------------------------------------
    // Crowd report tests
    // --------------------------------------------------

    @Test
    void shouldCreateUpdateAndDeleteCrowdReport() throws Exception {

        CrowdLog crowdLog = new CrowdLog();

        crowdLog.setDayOfWeek("Monday");
        crowdLog.setTimeSlot("18:00-19:00");
        crowdLog.setCrowdLevel(8);

        String response = mockMvc.perform(
                        post("/api/crowd/report")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(crowdLog))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dayOfWeek").value("Monday"))
                .andExpect(jsonPath("$.timeSlot").value("18:00-19:00"))
                .andExpect(jsonPath("$.crowdLevel").value(8))
                .andExpect(jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CrowdLog saved =
                objectMapper.readValue(response, CrowdLog.class);

        // Get crowd statistics
        mockMvc.perform(
                        get("/api/crowd/stats/Monday")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].crowdLevel").value(8))
                .andExpect(jsonPath("$[0].timeSlot")
                        .value("18:00-19:00"));

        // Update report
        CrowdLog update = new CrowdLog();

        update.setCrowdLevel(5);
        update.setTimeSlot("17:00-18:00");
        update.setDayOfWeek("Monday");

        mockMvc.perform(
                        put("/api/crowd/report/" + saved.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(update))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.crowdLevel").value(5))
                .andExpect(jsonPath("$.timeSlot")
                        .value("17:00-18:00"));

        // Delete report
        mockMvc.perform(
                        delete("/api/crowd/report/" + saved.getId())
                )
                .andExpect(status().isOk());

        // Verify deletion
        mockMvc.perform(
                        get("/api/crowd/stats/Monday")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }


    // --------------------------------------------------
    // Crowd analytics tests
    // --------------------------------------------------

    @Test
    void shouldReturnOptimalCrowdTimeSlots() throws Exception {

        CrowdLog busySlot = new CrowdLog();

        busySlot.setDayOfWeek("Tuesday");
        busySlot.setTimeSlot("18:00-19:00");
        busySlot.setCrowdLevel(8);

        CrowdLog quietSlot = new CrowdLog();

        quietSlot.setDayOfWeek("Tuesday");
        quietSlot.setTimeSlot("17:00-18:00");
        quietSlot.setCrowdLevel(4);

        crowdLogRepository.save(busySlot);
        crowdLogRepository.save(quietSlot);

        mockMvc.perform(
                        get("/api/analytics/optimal-slots/Tuesday")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].timeSlot")
                        .value("17:00-18:00"))
                .andExpect(jsonPath("$[0].averageCrowdLevel")
                        .value(4.0));
    }
}