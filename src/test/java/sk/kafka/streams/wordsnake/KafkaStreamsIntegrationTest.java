package sk.kafka.streams.wordsnake;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.landoop.kafka.testing.KCluster;
import io.confluent.kafka.schemaregistry.avro.AvroCompatibilityLevel;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sk.kafka.streams.wordsnake.model.InputKey;
import sk.kafka.streams.wordsnake.model.Sentence;

@Slf4j
public class KafkaStreamsIntegrationTest {

  private static final String INPUT_TOPIC = "INPUT_TOPIC";
  private static final String OUTPUT_TOPIC = "OUTPUT_TOPIC";

  private static KCluster kafkaCluster;
  private static String bootstrapServer;
  private static String schemaRegistry;

  @BeforeAll
  static void beforeSetup() {
    kafkaCluster = new KCluster(3, false, AvroCompatibilityLevel.BACKWARD);

    bootstrapServer = kafkaCluster.BrokersList();
    schemaRegistry = kafkaCluster.SchemaRegistryService().get().Endpoint();

    kafkaCluster.createTopic(INPUT_TOPIC, 1, 1);
    kafkaCluster.createTopic(OUTPUT_TOPIC, 1, 1);
  }

  @AfterAll
  static void exitAll() {
    try {
      kafkaCluster.close();
    } catch (Exception e) {
      log.warn("Can not close embedded kafka cluster", e);
    }
  }

  @Test
  void inputDataTest () {
    //setup
    KafkaProducer<GenericRecord, GenericRecord> producer = KafkaSupport
        .getProducer(bootstrapServer, schemaRegistry);
    GenericRecord testKey = new GenericRecordBuilder(InputKey.AVRO_SCHEMA).set("sequence", 1)
        .set("description", "test").build();
    GenericRecord testValue = new GenericRecordBuilder(Sentence.AVRO_SCHEMA)
        .set(Sentence.CONTENT_FIELD_NAME, "hello world").build();

    Consumer<GenericRecord, GenericRecord> consumer = KafkaSupport
        .getConsumer(bootstrapServer, INPUT_TOPIC);

    // run
    producer.send(new ProducerRecord<>(INPUT_TOPIC, testKey, testValue));

    //verify
    ConsumerRecords<GenericRecord, GenericRecord> records = consumer.poll(Duration.ofSeconds(1));
    assertThat(records.count()).isEqualTo(1);
    ConsumerRecord<GenericRecord, GenericRecord> theRecord = records.iterator().next();

    assertThat(theRecord.value().get(Sentence.CONTENT_FIELD_NAME)).isEqualTo("hello world");
  }

}
