package hlf.streamerror;

import hlf.streamerror.entity.WorkRepo;
import hlf.streamerror.entity.WorkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static hlf.streamerror.Helpers.range;
import static hlf.streamerror.Helpers.take;
import static hlf.streamerror.Helpers.tee;
import static hlf.streamerror.entity.Work.work;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StreamTransactionsTest {

  @Autowired private WorkRepo workRepo;

  @Autowired private WorkService workService;

  @BeforeEach
  void beforeEach() {
    workRepo.deleteAll();
  }

  @Test
  void loadContext() {
    workService.save(work(1));
    workService.save(work(2));

    var numbers = workRepo.findAll();
    System.out.println(numbers);
  }

  @Test
  void saveWorkForLoop() {
    for (int i = 0; i < 10; i++) {
      workService.save(work(i));
    }
    var list = workRepo.findAll();
    assertThat(list).hasSize(10);
  }

  @Test
  void saveWorkForEach() {
    range(0, 10).parallel().forEach(i -> workService.save(work(i)));
    var list = workRepo.findAll();
    assertThat(list).hasSize(10);
  }

  @Test
  void saveWorkInMap() {
    range(0, 10)
        //            .parallel()
        .map(tee(i -> workService.save(work(i))))
        .collect(Collectors.toList());
    var list = workRepo.findAll();
    System.out.println(list);
    assertThat(list).hasSize(10);
  }

  @Test
  void saveWithCollectionForEach() {
    List.of(1, 2, 3, 4, 5, 6, 7).forEach(i -> workService.save(work(i)));
  }


}
