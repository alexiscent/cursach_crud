package courseWork.repository;

import java.util.List;

import courseWork.entity.Published;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublishedRepository extends JpaRepository<Published, Long> {
    List<Published> findByPoetIdOrderByDate(Long id);

    List<Published> findByOrderByName();
}
