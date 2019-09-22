package com.badlogic.pokemon;

import com.badlogic.gdx.math.Rectangle;

import java.util.function.Function;

import static com.badlogic.pokemon.Trainer.STEP_SIZE;

public enum Direction {

    UP(
            position -> {
                position.y = computeNewPositionWith2DigitsPrecisionValue(position.y + STEP_SIZE);
                return position;
            },
            position -> {
                return new Rectangle(position.x, position.y + 1, position.width, position.height);
            }
    ),
    RIGHT(
            position -> {
                position.x = computeNewPositionWith2DigitsPrecisionValue(position.x + STEP_SIZE);
                return position;
            },
            position -> {
                return new Rectangle(position.x + 1, position.y, position.width, position.height);
            }
    ),
    DOWN(
            position -> {
                position.y = computeNewPositionWith2DigitsPrecisionValue(position.y - STEP_SIZE);
                return position;
            },
            position -> {
                return new Rectangle(position.x, position.y - 1, position.width, position.height);
            }
    ),
    LEFT(
            position -> {
                position.x = computeNewPositionWith2DigitsPrecisionValue(position.x - STEP_SIZE);
                return position;
            },
            position -> {
                return new Rectangle(position.x - 1, position.y, position.width, position.height);
            }
    );

    private static float computeNewPositionWith2DigitsPrecisionValue(double newValue) {
        return (float) (Math.round((newValue) * 100) / 100.0);
    }

    private final Function<Rectangle, Rectangle> updatePosition;
    private final Function<Rectangle, Rectangle> getNewTargetPosition;

    Direction(Function<Rectangle, Rectangle> updatePositionFunction, Function<Rectangle, Rectangle> getNewTargetPositionFunction) {
        updatePosition = updatePositionFunction;
        getNewTargetPosition = getNewTargetPositionFunction;
    }

    Rectangle updateRectPosition(Rectangle actualPosition) {
        return updatePosition.apply(actualPosition);
    }

    Rectangle getNewTargetPosition(Rectangle actualPosition) {
        return getNewTargetPosition.apply(actualPosition);
    }
}
