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
import sk.kafka.streams.wordsnake.implementation.DownRightSnake;
import sk.kafka.streams.wordsnake.implementation.DownRightUpSnake;
import sk.kafka.streams.wordsnake.model.Sentence;

@Slf4j
@Component
@AllArgsConstructor
public class KafkaOutputTopicConsumer {

  private final ApplicationKafkaStreamsConfiguration appConfig;

  @KafkaListener(topics = {"#{'${application.streams-config.prefix-output-processed-topic}' + '_down_right_snake'}", "#{'${application.streams-config.prefix-output-processed-topic}' + '_down_right_up_snake'}"})
  public void receive(ConsumerRecord<GenericRecord, GenericRecord> consumerRecord) {

    GenericRecord sentenceValue = consumerRecord.value();
    String sentence = sentenceValue.get(Sentence.CONTENT_FIELD_NAME).toString();
    log.debug("Content:\n{}", sentence);
    if (consumerRecord.topic().endsWith(DownRightUpSnake.TOPIC_SUFFIX)) {
      insertIntoFile(appConfig.getPrefixOutputFilePath() + DownRightUpSnake.TOPIC_SUFFIX, sentence);
    } else {
      insertIntoFile(appConfig.getPrefixOutputFilePath() + DownRightSnake.TOPIC_SUFFIX, sentence);
    }
  }

  private void insertIntoFile(String fileName, String sentence) {
    try (FileWriter outputFile = new FileWriter(fileName, true)) {
      outputFile.write(sentence + '\n');
    } catch (IOException e) {
      log.error("Cannot open file {} to append to", fileName, e);
    }
  }
}
