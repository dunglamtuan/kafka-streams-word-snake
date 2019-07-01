package sk.kafka.streams.wordsnake;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import sk.kafka.streams.wordsnake.configuration.ApplicationKafkaStreamsConfiguration;
import sk.kafka.streams.wordsnake.model.Sentence;
import sk.kafka.streams.wordsnake.transform.SentenceTransformer;

@AllArgsConstructor
@Builder
@Slf4j
public class ApplicationWordSnakeStreams {

  private final ApplicationKafkaStreamsConfiguration applicationConfig;
  private final KafkaStreamsConfiguration kafkaStreamsConfiguration;
  private final StreamsBuilder streamsBuilder = new StreamsBuilder();

  void setupTopology() {
    KStream<GenericRecord, Sentence> wordsnakeStream = streamsBuilder
        .stream(applicationConfig.getInputTopic())
        .map(((key, value) -> null))
        .peek((k, v) -> log.info("{} - value: {}", applicationConfig.getInputTopic(), v))
        .map(SentenceTransformer::new)
  }

}
