package sk.kafka.streams.wordsnake.transform;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import sk.kafka.streams.wordsnake.model.Sentence;

@Slf4j
@NoArgsConstructor
public class SentenceTransformer implements
    Transformer<GenericRecord, Sentence, KeyValue<GenericRecord, Sentence>> {

  @Override
  public void init(ProcessorContext processorContext) {
    //no - op
  }

  @Override
  public KeyValue<GenericRecord, Sentence> transform(GenericRecord key, Sentence value) {
    log.info("Value: {}", value.getRawSentence());
    return null;
  }

  @Override
  public void close() {
    //no - op
  }

}
