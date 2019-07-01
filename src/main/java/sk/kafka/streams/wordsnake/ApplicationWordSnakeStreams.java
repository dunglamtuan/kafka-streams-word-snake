package sk.kafka.streams.wordsnake;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
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

  final Serde<GenericRecord> keyGenericAvroSerde = new GenericAvroSerde();
  final Serde<GenericRecord> valueGenericAvroSerde = new GenericAvroSerde();

  void setupTopology() {
    KStream<GenericRecord, GenericRecord> wordsnakeStream = streamsBuilder
        .stream(applicationConfig.getInputTopic())
        .peek((k, v) -> log.info("{} - value: {}", applicationConfig.getInputTopic(), v))
        .map(SentenceTransformer::apply)
        .to(applicationConfig.getOutputProcessedTopic());
  }

}
