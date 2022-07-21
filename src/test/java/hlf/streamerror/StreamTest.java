package hlf.streamerror;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import static java.util.stream.Collectors.*;

import static hlf.streamerror.Helpers.range;
import static hlf.streamerror.Helpers.take;
import static hlf.streamerror.Helpers.tee;
import static org.assertj.core.api.Assertions.assertThat;

public class StreamTest {

  Duration time = Duration.of(500, ChronoUnit.MILLIS);

  @Test
  public void testCollect() {
    List<Integer> list = range(0, 10).map(x -> x * 2).collect(toList());
    assertThat(list).hasSize(10);
  }

  @Test
  public void testCollectWaiting() {
    var timer = Stopwatch.start();

    List<Integer> list =
        range(0, 10)
            .map(x -> x * 2)
            .map(take(time))
            .map(tee(System.out::println))
            .collect(toList());

    assertThat(list).hasSize(10);
    assertThat(timer.elapsed()).isGreaterThan(Duration.ofSeconds(5));
  }

  @Test
  public void buildResultInForEach() {
    Predicate<Integer> isOdd = i -> i % 2 == 1;
    Set<Integer> results = new HashSet<>();
    range(0, 10)
      .map(x -> x * 3)
      .filter(isOdd)
      .forEach(results::add);

    results = range(0, 10)
      .map(x -> x * 3)
      .filter(isOdd)
      .collect(toSet());
  }

  @Test
  void compareFirstNullResults() {
//    assertThat(<Integer>firstNonNull()).equals(<Integer>firstNonNull2());
    assertThat(firstNonNull((String) null)).isEqualTo(firstNonNull2((String) null));
    assertThat(firstNonNull(null, "one")).isEqualTo(firstNonNull2(null, "one"));
    assertThat(firstNonNull("one", "two")).isEqualTo(firstNonNull2("one", "two"));
    assertThat(firstNonNull("one", null, "two")).isEqualTo(firstNonNull2("one", null, "two"));
  }

  private static <T> T firstNonNull(T... ts) {
    return Arrays.stream(ts).filter(t -> t != null).findFirst().orElse(null);
  }

  private static <T> T firstNonNull2(T... ts) {
    T found = null;
    if (ts != null) {
      for (T t : ts) {
        if (t != null) {
          found = t;
          break;
        }
      }
    }
    return found;
  }
}
