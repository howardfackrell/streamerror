package hlf.streamerror;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Helpers {

  public static Stream<Integer> range(int startInclusive, int endExclusive) {
    return IntStream.range(startInclusive, endExclusive).boxed();
  }

  public static <T> Function<T, T> tee(Consumer<T> sideEffect) {
    return (T t) -> {
      sideEffect.accept(t);
      return t;
    };
  }

  public static <T> Function<T, T> println() {
    return tee(System.out::println);
  }

  public static <T> Function<T, T> take(Duration duration) {
    return tee(
        t -> {
          try {
            Thread.sleep(duration.toMillis());
          } catch (InterruptedException e) {
            // ignore
          }
        });
  }

  public static <T> Function<T, T> throwWhen(Predicate<T> predicate) {
    return tee(
        t -> {
          if (predicate.test(t)) {
            throw new RuntimeException("Failing value " + t);
          }
        });
  }

  public static <T> Function<T, T> printThreadName() {
    return tee(
        t -> {
          var currentThread = Thread.currentThread();
          var tname = currentThread.getThreadGroup().getName() + ": " + currentThread.getName();
          System.out.println(tname);
        });
  }

}
