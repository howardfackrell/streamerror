package hlf.streamerror;

import org.junit.platform.commons.function.Try;

import java.util.function.Function;
import java.util.function.Predicate;

public class TryUtils {
  public static <T> Predicate<Try<T>> isSuccess() {
    return tr -> tr.toOptional().isPresent();
  }

  public static <T> T getUnsafe(Try<T> tr) {
    return tr.getOrThrow(e -> new RuntimeException(e));
  }

  public static <T> Function<T, Try<T>> of(Function<T, T> func) {
    return t -> Try.call(() -> func.apply(t));
  }
}
