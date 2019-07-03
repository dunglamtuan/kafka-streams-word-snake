package sk.kafka.streams.wordsnake.transform;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import sk.kafka.streams.wordsnake.configuration.ApplicationKafkaStreamsConfiguration;

class SentenceProcessorTest {

  @Test
  void processSentenceTest() {
    // setup
    ApplicationKafkaStreamsConfiguration applicationConfig =
        mock(ApplicationKafkaStreamsConfiguration.class);
    when(applicationConfig.getWordsToEliminate()).thenReturn(".:,");

    SentenceProcessor sentenceProcessor = new SentenceProcessor(applicationConfig);
    String testSentence = "ASDasdasd, AasdaqIFJ, zxvcv: AOISdsfFO. ASOweqIF, IU, FLIASHF";
    String testSentence1 = "  ASDasdasd, AasdaqIFJ, zxvcv: AOISdsfFO. ASOweqIF, IU, FLIASHF";
    String testSentence2 = "ASDasdasd, AasdaqIFJ, zxvcv: AOISdsfFO. ASOweqIF, IU, FLIASHF  ";
    String testSentence3 = "  ASDasdasd, AasdaqIFJ, zxvcv: AOISdsfFO. ASOweqIF, IU, FLIASHF   ";

    String expectedResult = "ASDASDASD AASDAQIFJ ZXVCV AOISDSFFO ASOWEQIF IU FLIASHF";

    // run
    String result = sentenceProcessor.processSentence(testSentence);
    String result1 = sentenceProcessor.processSentence(testSentence1);
    String result2 = sentenceProcessor.processSentence(testSentence2);
    String result3 = sentenceProcessor.processSentence(testSentence3);

    // verify
    assertThat(result).isEqualTo(expectedResult);
    assertThat(result1).isEqualTo(expectedResult);
    assertThat(result2).isEqualTo(expectedResult);
    assertThat(result3).isEqualTo(expectedResult);
  }

}
