package sk.kafka.streams.wordsnake.transform;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import sk.kafka.streams.wordsnake.model.Sentence;

@Slf4j
@NoArgsConstructor
public class SentenceTransformer implements
    KeyValueMapper<GenericRecord, GenericRecord, KeyValue<GenericRecord, GenericRecord>> {

  @Override
  public KeyValue<GenericRecord, GenericRecord> apply(GenericRecord key,
      GenericRecord value) {
    Object content = value.get("content");
    log.info("{}", content.toString());
    value.put("content", "adohoj");
    return new KeyValue<>(key, value);
  }
}
