package courseWork.repository;

import java.util.List;

import courseWork.entity.Poet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoetRepository extends JpaRepository<Poet, Long> {
    List<Poet> findByOrderByName();
}
