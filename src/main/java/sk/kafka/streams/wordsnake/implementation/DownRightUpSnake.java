package sk.kafka.streams.wordsnake.implementation;

import static sk.kafka.streams.wordsnake.implementation.WordSnakeUtils.DOWN;
import static sk.kafka.streams.wordsnake.implementation.WordSnakeUtils.RIGHT;
import static sk.kafka.streams.wordsnake.implementation.WordSnakeUtils.UP;
import static sk.kafka.streams.wordsnake.implementation.WordSnakeUtils.asString;
import static sk.kafka.streams.wordsnake.implementation.WordSnakeUtils.makeCanvasWithSpaces;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Concrete implementation of WordSnakeService
 * This implementation is limited, because the result if right-orientated. That means that the snake
 * starts at position [0,0] and it can move DOWN, UP or RIGHT
 */
@Slf4j
@NoArgsConstructor
public class DownRightUpSnake extends SnakeMovement implements WordSnakeService {

  public static final String TOPIC_SUFFIX = "_down_right_up_snake";

  @Override
  public String makeSnake(String sentence) {
    String[] words = sentence.split(" ");

    int maxPossibleWidth = WordSnakeUtils.findMaxPossibleWidth(words);
    int maxPossibleHeight = WordSnakeUtils.findMaxPossibleHeight(words);

    log.debug("MAX_HEIGHT: {} MAX_WIDTH: {} for {}", maxPossibleHeight, maxPossibleWidth, words);

    // algorithm used
    char[][] canvas = downRightUpSnake(words, maxPossibleHeight, maxPossibleWidth);

    return asString(canvas, actualHeight, actualWidth);
  }

  private char[][] downRightUpSnake(String[] words, int height, int width) {
    char[][] canvas = makeCanvasWithSpaces(height, width);

    int currentDirection;
    int previousDirection = RIGHT;
    AtomicInteger currentHorizontalPosition = new AtomicInteger(0);
    AtomicInteger currentVerticalPosition = new AtomicInteger(0);
    for (String word : words) {
      currentDirection = nextMove(previousDirection, currentVerticalPosition.get(), height);

      switch (currentDirection) {
        case UP:
          moveUp(canvas, word, currentHorizontalPosition, currentVerticalPosition);
          previousDirection = UP;
          break;
        case DOWN:
          moveDown(canvas, word, currentHorizontalPosition, currentVerticalPosition);
          previousDirection = DOWN;
          break;
        case RIGHT:
          moveRight(canvas, word, currentHorizontalPosition, currentVerticalPosition);
          previousDirection = RIGHT;
          break;
        default:
          throw new IllegalStateException("Current direction can not be " + currentDirection);
      }
    }

    return canvas;
  }

  private int nextMove(int previousDirection, int currentPosition, int maxHeight) {
    int result;
    switch (previousDirection) {
      case UP:
      case DOWN:
        result = RIGHT;
        break;
      case RIGHT:
        result = nextHorizontalDirection(currentPosition, maxHeight);
        break;
      default:
        throw new IllegalArgumentException("Previous direction cannot be " + previousDirection);
    }
    return result;
  }

  private int nextHorizontalDirection(int currentPosition, int maxHeight) {
    return (currentPosition > maxHeight / 2 - 1) ? UP : DOWN;
  }
}
