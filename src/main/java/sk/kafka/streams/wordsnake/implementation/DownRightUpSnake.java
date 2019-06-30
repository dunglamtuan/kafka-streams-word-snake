package sk.kafka.streams.wordsnake.implementation;

import static sk.kafka.streams.wordsnake.implementation.WordSnakeUtils.*;

import java.util.stream.IntStream;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Concrete implementation of WordSnakeService
 * This implementation is limited, because the result if right-orientated. That means that the snake
 * starts at position [0,0] and it can move DOWN, UP or RIGHT
 */
@Slf4j
@NoArgsConstructor
public class DownRightUpSnake implements WordSnakeService {

  private int actualHeight = Integer.MIN_VALUE;
  private int actualWidth = Integer.MIN_VALUE;

  @Override
  public String makeSnake(String sentence) {
    String[] words = sentence.split(" ");

    int maxPossibleWidth = IntStream.range(0, words.length)
        .filter(index -> index % 2 == 1)
        .mapToObj(even -> words[even].length())
        .reduce(1, Integer::sum);

    int maxPossibleHeight = IntStream.range(0, words.length)
        .filter(index -> index % 2 == 0)
        .mapToObj(even -> words[even].length())
        .reduce(1, Integer::sum);

    log.info("MAX_HEIGHT: {} MAX_WIDTH: {}", maxPossibleHeight, maxPossibleWidth);

    // algorithm used
    char[][] canvas = downRightUpSnake(words, maxPossibleHeight, maxPossibleWidth);

    return asString(canvas, actualHeight, actualWidth);
  }

  private char[][] downRightUpSnake(String[] words, int height, int width) {
    char[][] canvas = makeCanvasWithSpaces(height, width);

    int currentDirection;
    int previousDirection = RIGHT;
    int currentHorizontalPosition = 0;
    int currentVerticalPosition = 0;
    for (String word : words) {
      currentDirection = nextMove(previousDirection, currentVerticalPosition, height);

      switch (currentDirection) {
        case UP:
          for (char c : word.toCharArray()) {
            canvas[currentVerticalPosition--][currentHorizontalPosition] = c;
          }
          previousDirection = UP;
          currentVerticalPosition++;
          break;
        case DOWN:
          for (char c : word.toCharArray()) {
            canvas[currentVerticalPosition++][currentHorizontalPosition] = c;
          }
          previousDirection = DOWN;
          currentVerticalPosition--;
          actualHeight = updateActualHeight(actualHeight, currentVerticalPosition);
          break;
        case RIGHT:
          for (char c : word.toCharArray()) {
            canvas[currentVerticalPosition][currentHorizontalPosition++] = c;
          }
          previousDirection = RIGHT;
          actualWidth = updateActualWidth(actualWidth, currentHorizontalPosition);
          currentHorizontalPosition--;
          break;
        default:
          throw new IllegalStateException("Current direction can not be " + currentDirection);
      }
    }

    return canvas;
  }

  private int updateActualWidth(int currentActualWidth, int currentHorizontalPosition) {
    return currentActualWidth < currentHorizontalPosition ?
        currentHorizontalPosition : currentActualWidth;
  }

  private static int updateActualHeight(int currentActualHeight, int currentVerticalPosition) {
    return currentActualHeight < currentVerticalPosition ?
        currentVerticalPosition : currentActualHeight;
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
