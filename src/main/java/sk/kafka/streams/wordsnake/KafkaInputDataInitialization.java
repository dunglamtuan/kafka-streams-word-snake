package sk.kafka.streams.wordsnake;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import sk.kafka.streams.wordsnake.configuration.ApplicationKafkaStreamsConfiguration;
import sk.kafka.streams.wordsnake.model.InputKey;
import sk.kafka.streams.wordsnake.model.Sentence;

@AllArgsConstructor
@Component
@Slf4j
public class KafkaInputDataInitialization {

  private final KafkaTemplate<GenericRecord, GenericRecord> kafkaTemplate;
  private final ApplicationKafkaStreamsConfiguration appConfig;

  @EventListener
  public void appReady(ApplicationReadyEvent event) {
    GenericRecord key = new GenericRecordBuilder(InputKey.AVRO_SCHEMA).set("sequence", 1)
        .set("description", "aaa").build();

    loadFileToSentences(appConfig.getInputPathFile()).forEach(sentence -> {
      GenericRecord value = new GenericRecordBuilder(Sentence.AVRO_SCHEMA)
          .set(Sentence.CONTENT_FIELD_NAME, sentence)
          .build();

      log.info("RawData {} - Sending to Kafka {} {}", sentence, key, value);
      kafkaTemplate.send(appConfig.getInputTopic(), key, value);
    });
  }

  private List<String> loadFileToSentences(String fileName) {
    try {
      return Files.readAllLines(Paths.get(fileName));
    } catch (IOException e) {
      log.error("Cannot load lines from file {}", fileName, e);
      throw new IllegalArgumentException(e);
    }
  }
}
