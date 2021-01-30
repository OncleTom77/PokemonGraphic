package com.badlogic.pokemon;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;
import java.util.Optional;

import static com.badlogic.pokemon.Direction.*;

class Trainer extends AnimatedCollidableObject {

    private static final Rectangle RELATIVE_COLLISION_BOX = new Rectangle(0, 0, 1, 1);
    static final float ANIMATION_DURATION = 1 / 6f; // In which time the animation takes to be completed. Little means faster
    static final float STEP_PRECISION = 1 / 8f; // How many different movement steps the trainer makes for one animation step.
    // Pay attention to the decimal result of the value. If it is infinite, it can lead to infinite movement

    private double animationState;
    private int nbTimeMove;
    private Direction direction;
    private Rectangle targetPosition;
    private final World world;
    private boolean wasOnStairway;

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
        wasOnStairway = world.isOnStairway(this);
        // if the target position is set, continue to move the trainer in the current direction
        if (targetPosition != null) {
            manageMovement(deltaTime, direction);
            return;
        }

        // If UP key is pressed we set the target position to the next up tile
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
            return;
        }

        // Set new target position and start animation
        targetPosition = newTargetPosition;
        manageMovement(deltaTime, direction);
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

//            if (targetPosition == null) {
//                System.err.println("Unstable trainer movement: targetPosition is null");
//                System.err.println("animationState: " + animationState);
//                System.err.println("nbTimeMove: " + nbTimeMove);
//                System.err.println("targetPosition: " + targetPosition);
//                System.err.println("drawingBox: " + drawingBox);
//                break;
//            }

            if (drawingBox.x == targetPosition.x && drawingBox.y == targetPosition.y) {
                System.out.println("animationState: " + animationState);
                System.out.println("nbTimeMove: " + nbTimeMove);
                System.out.println("targetPosition: " + targetPosition);
                System.out.println("drawingBox: " + drawingBox);
                targetPosition = null;
                break; // FIXME: fix to confirm
            }
        }
    }

    public boolean isEnteringOnStairway() {
        return targetPosition != null && world.isOnStairway(targetPosition);
    }

    public boolean isOnStairway() {
        return world.isOnStairway(this);
    }

    public Optional<Rectangle> getLeavingStairwayPosition() {
        return Optional.ofNullable(wasOnStairway && !world.isOnStairway(this) ? targetPosition : null);
    }
}
