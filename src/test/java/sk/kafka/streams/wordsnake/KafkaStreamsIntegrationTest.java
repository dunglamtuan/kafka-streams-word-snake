package sk.kafka.streams.wordsnake;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.landoop.kafka.testing.KCluster;
import io.confluent.kafka.schemaregistry.avro.AvroCompatibilityLevel;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import java.io.IOException;
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

  private static MockSchemaRegistryClient mockSchemaRegistryClient = new MockSchemaRegistryClient();

  @BeforeAll
  static void beforeSetup() throws IOException, RestClientException {
    kafkaCluster = new KCluster(1, true, AvroCompatibilityLevel.BACKWARD);

    mockSchemaRegistryClient.register(INPUT_TOPIC + "-key", InputKey.AVRO_SCHEMA, 1, 1);
    mockSchemaRegistryClient.register(INPUT_TOPIC + "-value",Sentence.AVRO_SCHEMA, 1, 1);

    bootstrapServer = kafkaCluster.BrokersList();
    schemaRegistry = kafkaCluster.SchemaRegistryService().get().Endpoint();

    //kafkaCluster.createTopic(INPUT_TOPIC, 1, 1);
    //kafkaCluster.createTopic(OUTPUT_TOPIC, 1, 1);
  }

  @Test
  void inputDataTest () throws IOException, RestClientException, InterruptedException {

    mockSchemaRegistryClient.getAllSubjects().forEach(System.out::println);
    //setup
    KafkaProducer<GenericRecord, GenericRecord> producer = KafkaSupport
        .getProducer(bootstrapServer, mockSchemaRegistryClient);
    GenericRecord testKey = new GenericRecordBuilder(InputKey.AVRO_SCHEMA).set("sequence", 1)
        .set("description", "test").build();
    GenericRecord testValue = new GenericRecordBuilder(Sentence.AVRO_SCHEMA)
        .set(Sentence.CONTENT_FIELD_NAME, "hello world").build();

    // run
    System.out.println();
    producer.send(new ProducerRecord<>(INPUT_TOPIC, testKey, testValue));

    //verify
    Consumer<GenericRecord, GenericRecord> consumer = KafkaSupport
            .getConsumer(bootstrapServer, mockSchemaRegistryClient, INPUT_TOPIC);
    Thread.sleep(2000);

    boolean isWaiting = true;
    ConsumerRecords<GenericRecord, GenericRecord> records;
    while (isWaiting) {
      records = consumer.poll(Duration.ofMillis(1000));
      if (!records.isEmpty()) {
        isWaiting = false;
      } else {
        System.out.println("Waiting for 2s");
        Thread.sleep(2000);
      }
    }

    records = consumer.poll(Duration.ofMillis(1000));
    assertThat(records.count()).isEqualTo(1);
    ConsumerRecord<GenericRecord, GenericRecord> theRecord = records.iterator().next();

    assertThat(theRecord.value().get(Sentence.CONTENT_FIELD_NAME)).isEqualTo("hello world");
  }

  @AfterAll
  static void exitAll() {
    try {
      kafkaCluster.close();
    } catch (Exception e) {
      log.warn("Can not close embedded kafka cluster", e);
    }
  }

}
