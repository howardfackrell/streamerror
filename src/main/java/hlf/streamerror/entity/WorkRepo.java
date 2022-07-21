package hlf.streamerror.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkRepo extends JpaRepository<Work, Integer> {}
