package hlf.streamerror;

import java.time.Duration;
import java.time.Instant;

public class Stopwatch {
  private Instant start;

  private Stopwatch(Instant start) {
    this.start = start;
  }

  public Duration elapsed() {
    return Duration.ofMillis(Instant.now().toEpochMilli() - start.toEpochMilli());
  }

  public static Stopwatch start() {
    return new Stopwatch(Instant.now());
  }
}
