package courseWork.repository;

import java.util.List;

import courseWork.entity.CurrentContest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentContestRepository extends JpaRepository<CurrentContest, Long> {
    List<CurrentContest> findByOrderByPositionNumber();
}
