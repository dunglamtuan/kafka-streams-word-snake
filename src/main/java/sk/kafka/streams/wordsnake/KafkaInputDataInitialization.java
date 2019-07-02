package sk.kafka.streams.wordsnake;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.avro.generic.GenericData.Record;
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
public class KafkaInputDataInitialization {

  private final KafkaTemplate<GenericRecord, GenericRecord> kafkaTemplate;
  private final ApplicationKafkaStreamsConfiguration appConfig;

  @EventListener
  public void appReady(ApplicationReadyEvent event) {
    GenericRecord key = new GenericRecordBuilder(InputKey.avroSchema).set("sequence", 1)
        .set("description", "aaa").build();
    GenericRecord value = new GenericRecordBuilder(Sentence.avroSchema).set("content", "asdasd dasda asda")
        .build();

    kafkaTemplate.send(appConfig.getInputTopic(), key, value);
  }
}
