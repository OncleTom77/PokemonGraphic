package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationScreen implements Screen {

    private final Drop game;

    // Constant rows and columns of the sprite sheet
    private static final int FRAME_COLS = 6, FRAME_ROWS = 5;

    // Objects used
    private Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
    private Texture walkSheet;

    // A variable for tracking elapsed time for the animation
    private float stateTime;

    private boolean isWalking = false;
    private float positionX;
    private float frameWidth;

    AnimationScreen(final Drop game) {
        this.game = game;

        // Load the sprite sheet as a Texture
        walkSheet = new Texture(Gdx.files.internal("animation_sheet.png"));

        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        // Initialize the Animation with the frame interval and array of frames
        walkAnimation = new Animation<TextureRegion>(1/60f, walkFrames);
//        walkAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        // Instantiate a SpriteBatch for drawing and reset the elapsed animation
        // time to 0
        stateTime = 0f;
        positionX = 20;
        frameWidth = walkSheet.getWidth() / FRAME_COLS;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        update(Gdx.graphics.getDeltaTime());

        // Get current frame of animation for the current stateTime

        TextureRegion currentFrame;
        if (isWalking) {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        } else {
            currentFrame = walkAnimation.getKeyFrame(0, true);
        }

        game.batch.begin();
        game.batch.draw(currentFrame, positionX, 50); // Draw current frame at (50, 50)
        game.batch.end();
    }

    private void update(float deltaTime) {
        isWalking = false;

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            isWalking = true;
            positionX += 200 * deltaTime;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//            isWalking = true;
            positionX -= 200 * deltaTime;
            stateTime = 0;
        }

        if (positionX > Gdx.graphics.getWidth() - frameWidth) {
            positionX = Gdx.graphics.getWidth() - frameWidth;
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        walkSheet.dispose();
    }
}
