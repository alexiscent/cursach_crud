package courseWork.repository;

import java.util.List;

import courseWork.entity.Luggage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LuggageRepository extends JpaRepository<Luggage, Long> {
    List<Luggage> findByPoetId(Long id);
}
