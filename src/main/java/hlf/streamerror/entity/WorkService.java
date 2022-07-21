package hlf.streamerror.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;

@Service
public class WorkService {

  @Autowired private WorkRepo workRepo;

  @Transactional
  public Work save(Work work) {

    sleep(Duration.ofMillis(500));
    return workRepo.save(work);
  }

  public static void sleep(Duration duration) {
    try {
      Thread.sleep(duration.toMillis());
    } catch (Exception e) {
      // ignore e
    }
  }

}
