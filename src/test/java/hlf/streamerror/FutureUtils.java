package hlf.streamerror;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class FutureUtils {

  public static <S, T> Function<S, CompletionStage<T>> of(Function<S, T> func) {
    return (S s) -> {
      try {
        return CompletableFuture.completedFuture(func.apply(s));
      } catch (Throwable throwable) {
        return CompletableFuture.failedStage(throwable);
      }
    };
  }

  public static <S, T> Function<CompletionStage<S>, CompletionStage<T>> lift(Function<S, T> func) {
    return (CompletionStage<S> sFut) -> sFut.thenApply(func);
  }
}
