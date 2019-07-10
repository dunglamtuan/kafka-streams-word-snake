package sk.kafka.streams.wordsnake;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import sk.kafka.streams.wordsnake.configuration.ApplicationKafkaStreamsConfiguration;
import sk.kafka.streams.wordsnake.implementation.DownRightSnake;
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
  private final StreamsBuilder streamsBuilder;

  void setupTopology() {

    String tmpTopic1 = UUID.randomUUID().toString() + applicationConfig.getPrefixOutputProcessedTopic() + DownRightUpSnake.TOPIC_SUFFIX;
    String tmpTopic2 = UUID.randomUUID().toString() + applicationConfig.getPrefixOutputProcessedTopic() + DownRightSnake.TOPIC_SUFFIX;

    KStream<GenericRecord, GenericRecord> inputRawStreams = streamsBuilder
        .stream(applicationConfig.getInputTopic());

    // creating duplicates tmp topics from input topic
    inputRawStreams.map(KeyValue::new).to(tmpTopic1);
    inputRawStreams.map(KeyValue::new).to(tmpTopic2);

    // creating 2 streams of data from input topic
    KStream<GenericRecord, GenericRecord> stream1= streamsBuilder.stream(tmpTopic1);
    KStream<GenericRecord, GenericRecord> stream2 = streamsBuilder.stream(tmpTopic2);

    // setup transformer for DownRightUp snake
    KStream<GenericRecord, GenericRecord> downRightUpSnake = transformByDownRightUpSnake(stream1);
    downRightUpSnake.to(applicationConfig.getPrefixOutputProcessedTopic() + DownRightUpSnake.TOPIC_SUFFIX);

    // setup transformer for DownRight snake
    KStream<GenericRecord, GenericRecord> downRightSnake = transformByDownRightSnake(stream2);
    downRightSnake.to(applicationConfig.getPrefixOutputProcessedTopic() + DownRightSnake.TOPIC_SUFFIX);

    new KafkaStreams(streamsBuilder.build(),
        kafkaStreamsConfiguration.asProperties()).start();
  }

  private KStream<GenericRecord, GenericRecord> transformByDownRightUpSnake(KStream<GenericRecord, GenericRecord> inputRawStreams) {
    return inputRawStreams.map(new SentenceTransformer(DownRightUpSnake.class, sentenceProcessor));
  }

  private KStream<GenericRecord, GenericRecord> transformByDownRightSnake(KStream<GenericRecord, GenericRecord> inputRawStreams) {
    return inputRawStreams.map(new SentenceTransformer(DownRightSnake.class, sentenceProcessor));
  }
}
