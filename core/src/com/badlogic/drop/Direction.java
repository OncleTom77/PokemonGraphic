package com.badlogic.drop;

import com.badlogic.gdx.math.Rectangle;

import java.util.function.Function;

import static com.badlogic.drop.PokemonTrainerScreen.*;

public enum Direction {

    UP(position -> {
        position.y = computeNewPositionWith2DigitsPrecisionValue(position.y + STEP_SIZE);
        return position;
    }),
    RIGHT(position -> {
        position.x = computeNewPositionWith2DigitsPrecisionValue(position.x + STEP_SIZE);
        return position;
    }),
    DOWN(position -> {
        position.y = computeNewPositionWith2DigitsPrecisionValue(position.y - STEP_SIZE);
        return position;
    }),
    LEFT(position -> {
        position.x = computeNewPositionWith2DigitsPrecisionValue(position.x - STEP_SIZE);
        return position;
    });

    private static float computeNewPositionWith2DigitsPrecisionValue(double newValue) {
        // Check screen boundaries for new value to avoid character to be off the screen.

        return (float) (Math.round((newValue) * 100) / 100.0);
    }

    private Function<Rectangle, Rectangle> updatePosition;

    Direction(Function<Rectangle, Rectangle> function) {
        updatePosition = function;
    }

    Rectangle updateRectPosition(Rectangle actualPosition) {
        return updatePosition.apply(actualPosition);
    }
}
