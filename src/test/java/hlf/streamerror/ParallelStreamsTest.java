package hlf.streamerror;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

import static hlf.streamerror.Helpers.printThreadName;
import static hlf.streamerror.Helpers.range;
import static hlf.streamerror.Helpers.take;
import static org.assertj.core.api.Assertions.assertThat;

public class ParallelStreamsTest {

  static final Duration time = Duration.ofMillis(500);

  @Test
  public void testSequentialStream() {

    var timer = Stopwatch.start();
    var list = range(0, 10).map(x -> x * 2).map(take(time)).collect(Collectors.toList());

    assertThat(timer.elapsed()).isGreaterThan(Duration.ofSeconds(5));
    assertThat(list).hasSize(10);
  }

  @Test
  public void testParallelStream() {
    var timer = Stopwatch.start();
    var sum =
        range(0, 10)
            .parallel()
            .map(x -> x * 2)
            .map(take(time))
            .collect(Collectors.summingInt(Integer::intValue));

    var elapsed = timer.elapsed();
    assertThat(elapsed).isLessThan(Duration.ofSeconds(5));
    System.out.println("Parallel stream took: " + elapsed);
  }

  @Test
  public void testWhichThreadPool() {
    var parallel = range(0, 10).parallel().map(printThreadName()).collect(Collectors.toList());
  }

  @Test
  public void testWithForJoinPool() throws Exception {
    ForkJoinPool pool = new ForkJoinPool(3);
    var result =
        pool.submit(
            () -> range(0, 10).parallel().map(printThreadName()).collect(Collectors.toList()));

    Thread.sleep(Duration.ofSeconds(3).toMillis());
    pool.shutdown();
  }

  @Test
  public void testWithExecutor() throws Exception {
    var executor = Executors.newFixedThreadPool(3);
    executor.submit(
        () -> range(0, 10).parallel().map(printThreadName()).collect(Collectors.toList()));

    Thread.sleep(Duration.ofSeconds(3).toMillis());
    executor.shutdown();
  }
}
