package com.badlogic.pokemon;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.function.Function;

public enum Direction {

    UP(
            position -> {
                position.y = computeNewPositionWith2DigitsPrecisionValue(position.y + Trainer.STEP_PRECISION);
                return position;
            },
            position -> {
                return new Rectangle(position.x, position.y + 1, position.width, position.height);
            }
    ),
    RIGHT(
            position -> {
                position.x = computeNewPositionWith2DigitsPrecisionValue(position.x + Trainer.STEP_PRECISION);
                return position;
            },
            position -> {
                return new Rectangle(position.x + 1, position.y, position.width, position.height);
            }
    ),
    DOWN(
            position -> {
                position.y = computeNewPositionWith2DigitsPrecisionValue(position.y - Trainer.STEP_PRECISION);
                return position;
            },
            position -> {
                return new Rectangle(position.x, position.y - 1, position.width, position.height);
            }
    ),
    LEFT(
            position -> {
                position.x = computeNewPositionWith2DigitsPrecisionValue(position.x - Trainer.STEP_PRECISION);
                return position;
            },
            position -> {
                return new Rectangle(position.x - 1, position.y, position.width, position.height);
            }
    );

    private static float computeNewPositionWith2DigitsPrecisionValue(double newValue) {
        return (float) (Math.round((newValue) * 1000) / 1000.0);
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

    static Direction getDirectionOfPosition(Vector2 position, Vector3 ref) {
        if (position.y < ref.y) {
            return DOWN;
        } else if (position.y > ref.y) {
            return UP;
        } else if (position.x < ref.x) {
            return LEFT;
        }
        return RIGHT;
    }
}
