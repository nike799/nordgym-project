package nordgym.repository;

import nordgym.domain.entities.UserEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntryRepository  extends JpaRepository<UserEntry,Long> {

}
