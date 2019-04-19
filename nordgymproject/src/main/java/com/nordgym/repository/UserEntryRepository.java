package com.nordgym.repository;

import com.nordgym.domain.entities.UserEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntryRepository  extends JpaRepository<UserEntry,Long> {

}
