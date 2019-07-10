package sk.kafka.streams.wordsnake.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

@Builder
@AllArgsConstructor
@Data
public class Sentence  {

  public static final String CONTENT_FIELD_NAME = "content";

  public static final Schema AVRO_SCHEMA = SchemaBuilder.builder().record("Sentence").fields()
      .name(CONTENT_FIELD_NAME).type().optional().stringType()
      .endRecord();

  private String rawSentence;

}
