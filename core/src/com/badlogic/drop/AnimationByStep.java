package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;

class AnimationByStep {

    private final int frameDuration;
    private final int defaultFrameIndex;
    private final Texture[] frames;

    AnimationByStep(int frameDuration, int defaultFrameIndex, Texture... frames) {
        if (defaultFrameIndex >= frames.length) {
            throw new IndexOutOfBoundsException();
        }

        this.frameDuration = frameDuration;
        this.defaultFrameIndex = defaultFrameIndex;
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

    Texture getDefaultTexture() {
        return frames[defaultFrameIndex];
    }
}
