package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;

class AnimationByStep {

    private final int frameDuration;
    private final Texture[] frames;

    AnimationByStep(int frameDuration, Texture... frames) {
        this.frameDuration = frameDuration;
        this.frames = frames;
    }

    Texture getFrameForStep(int step) {
        int frameNumber = step / frameDuration;
        frameNumber = frameNumber % ((frames.length * 2) - 2);

        if (frameNumber >= frames.length) {
            frameNumber = frames.length - 2 - (frameNumber - frames.length);
        }

        return frames[frameNumber];
    }
}
