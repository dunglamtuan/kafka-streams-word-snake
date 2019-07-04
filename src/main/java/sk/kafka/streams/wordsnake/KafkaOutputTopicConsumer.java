package sk.kafka.streams.wordsnake;

import java.io.FileWriter;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import sk.kafka.streams.wordsnake.configuration.ApplicationKafkaStreamsConfiguration;
import sk.kafka.streams.wordsnake.model.Sentence;

@Slf4j
@Component
@AllArgsConstructor
public class KafkaOutputTopicConsumer {

  private final ApplicationKafkaStreamsConfiguration appConfig;

  @KafkaListener(topics = {"${application.streams-config.output-processed-topic}"})
  public void receive(ConsumerRecord<GenericRecord, GenericRecord> consumerRecord) {
    GenericRecord sentence = consumerRecord.value();

    log.info("Content:\n{}", sentence.get(Sentence.CONTENT_FIELD_NAME));
    try (FileWriter outputFile = new FileWriter(appConfig.getOutputFilePath(), true)) {
      outputFile.write(sentence.get(Sentence.CONTENT_FIELD_NAME).toString() + '\n');
    } catch (IOException e) {
      log.error("Cannot open file {} to append to", appConfig.getOutputFilePath(), e);
    }
  }
}
