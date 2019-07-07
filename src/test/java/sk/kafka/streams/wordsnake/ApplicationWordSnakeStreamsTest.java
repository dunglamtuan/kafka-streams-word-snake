package sk.kafka.streams.wordsnake;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import java.util.Map;
import java.util.Properties;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.test.ConsumerRecordFactory;
import org.junit.jupiter.api.Test;
import sk.kafka.streams.wordsnake.configuration.ApplicationKafkaStreamsConfiguration;
import sk.kafka.streams.wordsnake.implementation.DownRightUpSnake;
import sk.kafka.streams.wordsnake.model.InputKey;
import sk.kafka.streams.wordsnake.model.Sentence;
import sk.kafka.streams.wordsnake.transform.SentenceProcessor;
import sk.kafka.streams.wordsnake.transform.SentenceTransformer;

class ApplicationWordSnakeStreamsTest {

  private static final String INPUT_TOPIC = "input";
  private static final String OUTPUT_TOPIC = "output";

  private static final Map<String, Schema> topicSchemaMaps = ImmutableMap.<String, Schema> builder()
      .put("input-key", InputKey.AVRO_SCHEMA)
      .put("output-key", InputKey.AVRO_SCHEMA)
      .put("input-value", Sentence.AVRO_SCHEMA)
      .put("output-value", Sentence.AVRO_SCHEMA)
      .build();

  private MockSchemaRegistryClient schemaRegistry = new MockSchemaRegistryClient(topicSchemaMaps);

  @Test
  void basicTest() {

    ApplicationKafkaStreamsConfiguration appConfig = mock(ApplicationKafkaStreamsConfiguration.class);
    when(appConfig.getInputTopic()).thenReturn(INPUT_TOPIC);
    when(appConfig.getOutputProcessedTopic()).thenReturn(OUTPUT_TOPIC);
    when(appConfig.getCharactersToEliminate()).thenReturn(".:/");

    KeyValueMapper mapper = new SentenceTransformer(DownRightUpSnake.class,
        new SentenceProcessor(appConfig));
    GenericAvroSerde serde = new GenericAvroSerde(schemaRegistry);
    StreamsBuilder builder = new StreamsBuilder();
    builder.stream(INPUT_TOPIC, Consumed.with(serde, serde))
        .map(mapper).to(OUTPUT_TOPIC, Produced.with(serde, serde));

    TopologyTestDriver driver = new TopologyTestDriver(builder.build(), driverConfig());

    GenericRecord testKey = new GenericRecordBuilder(InputKey.AVRO_SCHEMA).set("sequence", 1)
        .set("description", "test").build();
    GenericRecord testValue = new GenericRecordBuilder(Sentence.AVRO_SCHEMA)
        .set(Sentence.CONTENT_FIELD_NAME, "hello world").build();

    ConsumerRecordFactory<GenericRecord, GenericRecord> factory =
        new ConsumerRecordFactory(INPUT_TOPIC,
            new KafkaAvroSerializer(schemaRegistry), new KafkaAvroSerializer(schemaRegistry));

    driver.pipeInput(factory.create(INPUT_TOPIC, testKey, testValue));

    ProducerRecord<byte[], byte[]> record = driver.readOutput(OUTPUT_TOPIC);
    assertThat(record).isNotNull();
  }

  private static Properties driverConfig() {
    Properties config = new Properties();
    config.put(StreamsConfig.APPLICATION_ID_CONFIG, "test");
    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "test:1234");

    return config;
  }
}
