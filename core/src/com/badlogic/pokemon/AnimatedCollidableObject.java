package com.badlogic.pokemon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

abstract class AnimatedCollidableObject extends CollidableObject {

    private final List<AnimationByStep> animations;

    AnimatedCollidableObject(List<AnimationByStep> animations, Rectangle initialDrawingBox, Rectangle relativeCollisionBox) {
        super(initialDrawingBox, relativeCollisionBox);

        this.animations = animations;
    }

    abstract int getCurrentAnimationIndex();

    abstract int getAnimationStep();

    abstract boolean isInAnimationState();

    @Override
    void draw(Batch batch) {
        setCurrentTexture();
        super.draw(batch);
    }

    private void setCurrentTexture() {
        int currentAnimationIndex = getCurrentAnimationIndex();
        AnimationByStep currentAnimation = animations.get(currentAnimationIndex);

        Texture currentFrame;
        if (isInAnimationState()) {
            int currentStep = getAnimationStep();
            currentFrame = currentAnimation.getFrameForStep(currentStep);
        } else {
            currentFrame = currentAnimation.getDefaultTexture();
        }

        setTexture(currentFrame);
    }
}
