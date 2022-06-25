package courseWork.repository;

import java.util.List;

import courseWork.entity.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestRepository extends JpaRepository<Contest, Long> {
    List<Contest> findByOrderByDate();
}
