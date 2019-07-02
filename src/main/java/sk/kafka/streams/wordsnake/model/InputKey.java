package sk.kafka.streams.wordsnake.model;

import lombok.Builder;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

@Builder
public class InputKey {

  private int sequence;
  private String description;

  public static final Schema avroSchema = SchemaBuilder.builder().record("InputKey").fields()
      .name("sequence").type().optional().intType()
      .name("description").type().optional().stringType()
      .endRecord();

}
