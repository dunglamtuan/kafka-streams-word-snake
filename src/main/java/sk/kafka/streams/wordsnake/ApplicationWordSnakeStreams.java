package sk.kafka.streams.wordsnake;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import sk.kafka.streams.wordsnake.configuration.ApplicationKafkaStreamsConfiguration;
import sk.kafka.streams.wordsnake.implementation.DownRightUpSnake;
import sk.kafka.streams.wordsnake.transform.SentenceProcessor;
import sk.kafka.streams.wordsnake.transform.SentenceTransformer;

@AllArgsConstructor
@Builder
@Slf4j
public class ApplicationWordSnakeStreams {

  private final ApplicationKafkaStreamsConfiguration applicationConfig;
  private final KafkaStreamsConfiguration kafkaStreamsConfiguration;
  private final SentenceProcessor sentenceProcessor;
  private final StreamsBuilder streamsBuilder = new StreamsBuilder();

  void setupTopology() {

    KStream<GenericRecord, GenericRecord> inputRawStreams = streamsBuilder
        .stream(applicationConfig.getInputTopic());

    transformByDownRightUpSnake(inputRawStreams);

    new KafkaStreams(streamsBuilder.build(),
        kafkaStreamsConfiguration.asProperties()).start();
  }

  private void transformByDownRightUpSnake(KStream<GenericRecord, GenericRecord> inputRawStreams) {
    inputRawStreams.map(new SentenceTransformer(DownRightUpSnake.class, sentenceProcessor))
        .to(applicationConfig.getOutputProcessedTopic());
  }

}
