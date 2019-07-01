package sk.kafka.streams.wordsnake.model;

import lombok.experimental.UtilityClass;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

@UtilityClass
public class ApplicationSchema {

  public static final Schema SENTENCE_SCHEMA = SchemaBuilder.builder()
      .record("KAFKA_STREAMS_EXAMPLE_SENTENCE").fields()
      .name("content").type().stringType().noDefault()
      .endRecord();

}
