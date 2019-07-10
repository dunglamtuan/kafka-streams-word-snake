package sk.kafka.streams.wordsnake.implementation;

import static sk.kafka.streams.wordsnake.implementation.WordSnakeUtils.*;

import java.util.concurrent.atomic.AtomicInteger;

public class DownRightSnake extends SnakeMovement implements WordSnakeService {

  public static final String TOPIC_SUFFIX = "_down_right_snake";

  @Override
  public String makeSnake(String sentence) {
    String[] words = sentence.split(" ");

    int maxPossibleWidth = findMaxPossibleWidth(words);
    int maxPossibleHeight = findMaxPossibleHeight(words);

    char[][] canvas = downRightSnake(words, maxPossibleHeight, maxPossibleWidth);

    return asString(canvas, actualHeight, actualWidth);
  }

  private char[][] downRightSnake(String[] words, int height, int width) {

    char[][] canvas = makeCanvasWithSpaces(height, width);

    int currentDirection = DOWN;
    AtomicInteger currentHorizontalPosition = new AtomicInteger(0);
    AtomicInteger currentVerticalPosition = new AtomicInteger(0);

    for (String word : words) {
      switch (currentDirection) {
        case RIGHT:
          moveRight(canvas, word, currentHorizontalPosition, currentVerticalPosition);
          currentDirection = DOWN;
          break;
        case DOWN:
          moveDown(canvas, word, currentHorizontalPosition, currentVerticalPosition);
          currentDirection = RIGHT;
          break;
        default:
          throw new IllegalStateException("DownRight snake cannot have direction of " + currentDirection);
      }
    }
    return canvas;
  }
}
