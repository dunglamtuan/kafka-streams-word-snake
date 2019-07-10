package sk.kafka.streams.wordsnake.implementation;

import static sk.kafka.streams.wordsnake.implementation.WordSnakeUtils.*;

public class DownRightSnake implements WordSnakeService {

  private int actualHeight = Integer.MIN_VALUE;
  private int actualWidth = Integer.MIN_VALUE;

  @Override
  public String makeSnake(String sentence) {
    String[] words = sentence.split(" ");

    int maxPossibleWidth = findMaxPossibleWidth(words);
    int maxPossibleHeight = findMaxPossibleHeight(words);

    char[][] canvas = downRightSnake(words, maxPossibleHeight, maxPossibleWidth);

    return asString(canvas, actualHeight, actualWidth);
  }

  private char[][] downRightSnake(String[] words, int height, int width) {

    System.out.println("HEIGHT: " + height + " WIDTH: " + width);

    char[][] canvas = makeCanvasWithSpaces(height, width);

    int currentDirection = DOWN;
    int currentHorizontalPosition = 0;
    int currentVerticalPosition = 0;

    for (String word : words) {
      switch (currentDirection) {
        case RIGHT:
          for (char c : word.toCharArray()) {
            canvas[currentVerticalPosition][currentHorizontalPosition++] = c;
          }
          currentDirection = DOWN;
          actualWidth = updateActualWidth(actualWidth, currentHorizontalPosition);
          currentHorizontalPosition--;
          break;
        case DOWN:
          for (char c : word.toCharArray()) {
            canvas[currentVerticalPosition++][currentHorizontalPosition] = c;
          }
          currentDirection = RIGHT;
          currentVerticalPosition--;
          actualHeight = updateActualHeight(actualHeight, currentVerticalPosition);
          break;
        default:
          throw new IllegalStateException("DownRight snake cannot have direction of " + currentDirection);
      }
    }

    return canvas;
  }
}
