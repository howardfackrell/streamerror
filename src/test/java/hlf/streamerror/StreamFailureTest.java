package hlf.streamerror;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.function.Try;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static hlf.streamerror.Helpers.range;
import static hlf.streamerror.Helpers.take;
import static hlf.streamerror.Helpers.tee;
import static hlf.streamerror.Helpers.throwWhen;
import static org.assertj.core.api.Assertions.assertThat;

public class StreamFailureTest {

  Duration time = Duration.ofMillis(500);

  @Test
  public void testFailure() {
    List<Integer> list = null;
    Exception caught = null;
    try {
      list =
          range(0, 10)
              .map(throwWhen(t -> t == 5))
              .map(x -> x * 2)
              .map(take(time))
              .map(tee(System.out::println))
              .collect(Collectors.toList());
    } catch (RuntimeException e) {
      caught = e;
    }

    assertThat(list).isNull();
    assertThat(caught).isNotNull();
    caught.printStackTrace();
  }

  @Test
  public void testIgnoringFailure() {
    List<Integer> list =
        range(0, 10)
            .map(TryUtils.of(throwWhen(x -> x == 5)))
            .filter(TryUtils.isSuccess())
            .map(TryUtils::getUnsafe)
            .map(x -> x * 2)
            .map(take(time))
            .map(tee(System.out::println))
            .collect(Collectors.toList());

    assertThat(list).hasSize(9);
  }

  @Test
  public void testHandlingFailure() {
    Consumer<Try<Integer>> exceptionHandler = tr -> tr.ifFailure(e -> e.printStackTrace());

    List<Integer> list =
        range(0, 10)
            .map(TryUtils.of(throwWhen(x -> x == 5)))
            .map(tee(exceptionHandler))
            .filter(TryUtils.isSuccess())
            .map(TryUtils::getUnsafe)
            .map(x -> x * 2)
            .map(take(time))
            .map(tee(System.out::println))
            .collect(Collectors.toList());

    assertThat(list).hasSize(9);
  }

  @Test
  public void testHandlingCollecting() {
    Consumer<Try<Integer>> exceptionHandler = tr -> tr.ifFailure(e -> e.printStackTrace());

    Map<Boolean, List<Try<Integer>>> result =
        range(0, 10)
            .map(TryUtils.of(throwWhen(x -> x == 5)))
            .map(tr -> tr.andThenTry(x -> x * 2))
            .map(take(time))
            .map(tee(System.out::println))
            .collect(Collectors.groupingBy(tr -> TryUtils.<Integer>isSuccess().test(tr)));

    List<Integer> list =
        result.get(Boolean.TRUE).stream().map(TryUtils::getUnsafe).collect(Collectors.toList());

    result.get(Boolean.FALSE).stream().forEach(exceptionHandler);

    assertThat(list).hasSize(9);
  }
}
