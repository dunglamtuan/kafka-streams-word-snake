package sk.kafka.streams.wordsnake;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KeyValueMapper;
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
    KeyValueMapper mapper = new SentenceTransformer(new DownRightUpSnake(), sentenceProcessor);

    streamsBuilder.stream(applicationConfig.getInputTopic())
        .peek((k, v) -> log.info("{} - value: {}", applicationConfig.getInputTopic(), v))
        .map(mapper)
        .to(applicationConfig.getOutputProcessedTopic());

    new KafkaStreams(streamsBuilder.build(),
        kafkaStreamsConfiguration.asProperties()).start();
  }

}
