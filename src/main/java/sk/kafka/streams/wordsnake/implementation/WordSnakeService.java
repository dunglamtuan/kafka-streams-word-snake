package sk.kafka.streams.wordsnake.implementation;

/**
 * Interface defines method to create a word snake from input sentence
 * Sentence is in format of String and words are separated by ' ' space
 */
public interface WordSnakeService {

  /**
   * Make a snake from sentence
   * @param sentence input String separated by space ' '
   * @return String representation of sentence
   */
  String makeSnake(String sentence);
}
