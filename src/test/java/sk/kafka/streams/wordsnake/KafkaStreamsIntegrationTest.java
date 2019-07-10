package sk.kafka.streams.wordsnake;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sk.kafka.streams.wordsnake.configuration.ApplicationKafkaStreamsConfiguration;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableKafka
@ActiveProfiles("test")
class KafkaStreamsIntegrationTest {
  private static final String TEST_INPUT_FILE_PATH = "it-test-input.txt";
  private static final String TEST_OUTPUT_FILE_PATH = "it-test-output.txt";

  private static final String TEST_INPUT_SENTENCE = "hellO World";

  @Autowired
  private ApplicationKafkaStreamsConfiguration appConfig;

  @BeforeAll
  static void beforeSetup() throws IOException {

    String nowS = Instant.now().toString().replaceAll("[.:-]", "_");
    String testInputTopic = nowS + "_input";
    String testOutTopic = nowS + "_output";

    System.setProperty("test.input.topic", testInputTopic);
    System.setProperty("test.output.topic", testOutTopic);

    File testInputFilePath = new File(TEST_INPUT_FILE_PATH);
    try (OutputStream outputStream = new FileOutputStream(testInputFilePath)) {
      outputStream.write(TEST_INPUT_SENTENCE.getBytes());
    }

    System.setProperty("test.input.file.path", TEST_INPUT_FILE_PATH);
    System.setProperty("test.output.file.path", TEST_OUTPUT_FILE_PATH);
  }

  @AfterAll
  static void deleteTestFiles() {
    deleteFile(TEST_INPUT_FILE_PATH);
    deleteFile(TEST_OUTPUT_FILE_PATH);
  }

  @Test
  void inputDataTest() throws IOException, InterruptedException {
    //setup
    String expectedSnake =
          "H    \n"
        + "E    \n"
        + "L    \n"
        + "L    \n"
        + "OORLD\n";

    // run
    // by setting metadata.max.age.ms in properties for consumer, we reduce the time, that consumer
    // retries to get topics metadata (default is 5min)
    int counter = 0;
    Path outputPath = Paths.get(appConfig.getOutputFilePath());
    while (!Files.exists(outputPath) && counter < 10) {
      log.info("Waiting for output path 2s");
      counter++;
      Thread.sleep(2000);
    }

    if (counter == 10)
      throw new IllegalStateException("After 20s waiting for outputPath, but none file has been created");

    // verify
    String fileName = appConfig.getOutputFilePath();
    List<String> outputLines = Files.readAllLines(Paths.get(fileName));

    String result = String.join("\n", outputLines);

    assertThat(result).isEqualTo(expectedSnake);
  }

  private static void deleteFile(String fileName) {
    File file = new File(fileName);
    if (file.delete()) {
      log.warn("Cannot delete file {}", fileName);
    } else {
      log.info("File {} has been deleted", fileName);
    }
  }

}
