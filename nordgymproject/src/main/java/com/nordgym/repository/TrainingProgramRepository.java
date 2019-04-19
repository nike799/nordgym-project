package com.nordgym.repository;

import com.nordgym.domain.entities.TrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingProgramRepository extends JpaRepository<TrainingProgram,Long> {

}
