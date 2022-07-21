package hlf.streamerror;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

@SpringBootTest
class StreamerrorApplicationTests {

  public static final Long KB = 1024L;
  public static final Long MB = 1024 * KB;

  static {
    GenericContainer<?> postgres =
        new GenericContainer<>(DockerImageName.parse("postgres:alpine"))
            .withExposedPorts(5432)
            .withSharedMemorySize(500 * MB)
            .withEnv("POSTGRES_PASSWORD", "password");

    postgres.setPortBindings(List.of("5432:5432"));
    postgres.setDockerImageName(StreamerrorApplicationTests.class.getName() + "postgres");
    postgres.start();
  }

  @Test
  void contextLoads() {}
}
