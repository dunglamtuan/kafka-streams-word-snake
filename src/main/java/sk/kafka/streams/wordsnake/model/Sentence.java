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

  public static final Schema avroSchema = SchemaBuilder.builder().record("Sentence").fields()
      .name("content").type().optional().stringType()
      .endRecord();

  private String rawSentence;

}
