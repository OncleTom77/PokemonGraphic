package com.badlogic.drop;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

import static com.badlogic.drop.Direction.*;

class Trainer extends AnimatedCollidableObject {
    private static final Rectangle RELATIVE_COLLISION_BOX = new Rectangle(0, 0, 1, 1);
    private static final float ANIMATION_DURATION = 1 / 6f;
    static final float STEP_PRECISION = 1 / 5f;
    static final float STEP_SIZE = STEP_PRECISION;

    private double animationState;
    private int nbTimeMove;
    private Direction direction;
    private Rectangle targetPosition;
    private final World world;

    Trainer(List<AnimationByStep> animations, Rectangle initialDrawingBox, Direction initialDirection, World world) {
        super(animations, initialDrawingBox, RELATIVE_COLLISION_BOX);

        animationState = 0;
        nbTimeMove = 0;
        direction = initialDirection;
        this.world = world;
    }

    @Override
    int getCurrentAnimationIndex() {
        switch (direction) {
            case UP:
                return 0;
            case RIGHT:
                return 1;
            case DOWN:
                return 2;
            case LEFT:
                return 3;
        }

        return -1;
    }

    @Override
    int getAnimationStep() {
        return nbTimeMove;
    }

    @Override
    boolean isInAnimationState() {
        return targetPosition != null;
    }

    void update(float deltaTime, Input input) {
        // if the target position is set, continue to move the trainer in the current direction
        if (targetPosition != null) {
            manageMovement(deltaTime, direction);
            return;
        }

        // If RIGHT key is pressed we set the target position to the next right tile
        if (input.isKeyPressed(Input.Keys.UP)) {
            trySetNewTargetPosition(deltaTime, UP);
        } else if (input.isKeyPressed(Input.Keys.RIGHT)) {
            trySetNewTargetPosition(deltaTime, RIGHT);
        } else if (input.isKeyPressed(Input.Keys.DOWN)) {
            trySetNewTargetPosition(deltaTime, DOWN);
        } else if (input.isKeyPressed(Input.Keys.LEFT)) {
            trySetNewTargetPosition(deltaTime, LEFT);
        } else {
            animationState = 0;
            nbTimeMove = 0;
        }
    }

    private void trySetNewTargetPosition(float deltaTime, Direction direction) {
        Rectangle newTargetPosition = direction.getNewTargetPosition(drawingBox);

        if (world.collides(newTargetPosition)) {
            this.direction = direction;
        } else {
            // Set new target position and start animation
            targetPosition = newTargetPosition;
            manageMovement(deltaTime, direction);
        }
    }

    private void manageMovement(float deltaTime, Direction direction) {
        if (this.direction != direction) {
            this.direction = direction;
            animationState = 0;
            nbTimeMove = 0;
        }

        animationState += deltaTime;

        while (animationState >= (nbTimeMove + 1) * STEP_PRECISION * ANIMATION_DURATION) {
            nbTimeMove++;
            Rectangle updatedDrawingBox = direction.updateRectPosition(drawingBox);
            setPosition(updatedDrawingBox.x, updatedDrawingBox.y);

            if (targetPosition == null) {
                System.err.println("Unstable trainer movement: targetPosition is null");
                break;
            }

            if (drawingBox.x == targetPosition.x && drawingBox.y == targetPosition.y) {
                targetPosition = null;
            }
        }
    }

    Rectangle getPosition() {
        return this.drawingBox;
    }
}
