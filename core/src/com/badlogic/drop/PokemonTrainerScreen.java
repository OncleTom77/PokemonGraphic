package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;

import static com.badlogic.drop.Direction.*;

public class PokemonTrainerScreen implements Screen {

    private static final float ANIMATION_DURATION = 1 / 6f;
    private static final float STEP_PRECISION = 1 / 10f;
    private static final int UNIT_SIZE = 16;
    static final float STEP_SIZE = STEP_PRECISION * UNIT_SIZE;

    private final Drop game;

    private final Texture walkTopTexture1;
    private final Texture standTopTexture;
    private final Texture walkTopTexture2;
    private final Animation<Texture> walkTopAnimation;

    private final Animation<Texture> walkRightAnimation;
    private final Texture walkRightTexture1;
    private final Texture standRightTexture;
    private final Texture walkRightTexture2;

    private final Texture walkBottomTexture1;
    private final Texture standBottomTexture;
    private final Texture walkBottomTexture2;
    private final Animation<Texture> walkBottomAnimation;

    private final Animation<Texture> walkLeftAnimation;
    private final Texture walkLeftTexture1;
    private final Texture standLeftTexture;
    private final Texture walkLeftTexture2;

    private final Texture pokemonCenterTexture;
    private Rectangle pokemonCenter;

    private Rectangle pokemonTrainer;
    private float animationState;
    private float lastTimeMove;
    private boolean isMakingStep;
    private boolean isStanding;
    private Direction direction;

    PokemonTrainerScreen(Drop game) {
        this.game = game;

        walkRightTexture1 = new Texture(Gdx.files.internal("trainer/right/walk_right_1.png"));
        standRightTexture = new Texture(Gdx.files.internal("trainer/right/stand_right.png"));
        walkRightTexture2 = new Texture(Gdx.files.internal("trainer/right/walk_right_2.png"));
        walkRightAnimation = new Animation<>(ANIMATION_DURATION, walkRightTexture1, standRightTexture, walkRightTexture2);
        walkRightAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        walkLeftTexture1 = new Texture(Gdx.files.internal("trainer/left/walk_left_1.png"));
        standLeftTexture = new Texture(Gdx.files.internal("trainer/left/stand_left.png"));
        walkLeftTexture2 = new Texture(Gdx.files.internal("trainer/left/walk_left_2.png"));
        walkLeftAnimation = new Animation<>(ANIMATION_DURATION, walkLeftTexture1, standLeftTexture, walkLeftTexture2);
        walkLeftAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        walkTopTexture1 = new Texture(Gdx.files.internal("trainer/top/walk_top_1.png"));
        standTopTexture = new Texture(Gdx.files.internal("trainer/top/stand_top.png"));
        walkTopTexture2 = new Texture(Gdx.files.internal("trainer/top/walk_top_2.png"));
        walkTopAnimation = new Animation<>(ANIMATION_DURATION, walkTopTexture1, standTopTexture, walkTopTexture2);
        walkTopAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        walkBottomTexture1 = new Texture(Gdx.files.internal("trainer/bottom/walk_bottom_1.png"));
        standBottomTexture = new Texture(Gdx.files.internal("trainer/bottom/stand_bottom.png"));
        walkBottomTexture2 = new Texture(Gdx.files.internal("trainer/bottom/walk_bottom_2.png"));
        walkBottomAnimation = new Animation<>(ANIMATION_DURATION, walkBottomTexture1, standBottomTexture, walkBottomTexture2);
        walkBottomAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        pokemonTrainer = new Rectangle();
        pokemonTrainer.width = 2;
        pokemonTrainer.height = 2;
        pokemonTrainer.x = UNIT_SIZE;
        pokemonTrainer.y = UNIT_SIZE;

        pokemonCenterTexture = new Texture(Gdx.files.internal("pokemon_center.png"));
        pokemonCenter = new Rectangle();
        pokemonCenter.width = 10;
        pokemonCenter.height = 8;
        pokemonCenter.x = 5 * UNIT_SIZE;
        pokemonCenter.y = 5 * UNIT_SIZE;

        animationState = 0;
        lastTimeMove = 0;
        isStanding = true;
        isMakingStep = false;
        direction = RIGHT;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        Texture currentFrame;
        if (direction == TOP) {
            if (isStanding) {
                currentFrame = standTopTexture;
            } else {
                currentFrame = walkTopAnimation.getKeyFrame(animationState, true);
            }
        } else if (direction == RIGHT) {
            if (isStanding) {
                currentFrame = standRightTexture;
            } else {
                currentFrame = walkRightAnimation.getKeyFrame(animationState, true);
            }
        } else if (direction == BOTTOM) {
            if (isStanding) {
                currentFrame = standBottomTexture;
            } else {
                currentFrame = walkBottomAnimation.getKeyFrame(animationState, true);
            }
        } else {
            if (isStanding) {
                currentFrame = standLeftTexture;
            } else {
                currentFrame = walkLeftAnimation.getKeyFrame(animationState, true);
            }
        }

        game.batch.begin();
        game.batch.draw(pokemonCenterTexture, pokemonCenter.x, pokemonCenter.y, pokemonCenter.width * UNIT_SIZE, pokemonCenter.height * UNIT_SIZE * ((float) pokemonCenterTexture.getHeight() / pokemonCenterTexture.getWidth()));
        game.batch.draw(currentFrame, pokemonTrainer.x, pokemonTrainer.y, pokemonTrainer.width * UNIT_SIZE, pokemonTrainer.height * UNIT_SIZE * ((float) currentFrame.getHeight() / currentFrame.getWidth()));
        game.batch.end();
    }

    private void update(float deltaTime) {

        if ((pokemonTrainer.x * 10) % (STEP_SIZE * 10) != 0 || (pokemonTrainer.y * 10) % (STEP_SIZE * 10) != 0) {
            System.out.println("ERROR !! " + pokemonTrainer.x + " : " + pokemonTrainer.y);
        }

        // If RIGHT key is pressed or the trainer is currently in middle state of animation (not in the standing sprite),
        // we move to the right
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || (direction == TOP && isMakingStep)) {
            manageMovement(deltaTime, TOP, walkTopAnimation);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || (direction == RIGHT && isMakingStep)) {
            manageMovement(deltaTime, RIGHT, walkRightAnimation);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || (direction == BOTTOM && isMakingStep)) {
            manageMovement(deltaTime, BOTTOM, walkBottomAnimation);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || (direction == LEFT && isMakingStep)) {
            manageMovement(deltaTime, LEFT, walkLeftAnimation);
        } else {
            isStanding = true;
            lastTimeMove = 0;
            animationState = 0;

            if (pokemonTrainer.x % UNIT_SIZE != 0) {
                System.out.println("ERROR !!");
            }
        }
    }

    private void manageMovement(float deltaTime, Direction direction, Animation animation) {
        if (this.direction != direction) {
            this.direction = direction;
            animationState = 0;
            lastTimeMove = 0;
        }

        animationState += deltaTime;
        lastTimeMove += deltaTime;

        // Move the trainer each 1/10 of the animation of 1/10 unit of the game (1/10 of 16px)
        // Thus, the trainer moves to 16px per animation step
        if (lastTimeMove >= STEP_PRECISION * ANIMATION_DURATION) {
            lastTimeMove = 0;
            pokemonTrainer = direction.updateRectPosition(pokemonTrainer);
        }

        int index = animation.getKeyFrameIndex(animationState);
        isMakingStep = index != 1;
        isStanding = false;
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
        walkRightTexture1.dispose();
        standRightTexture.dispose();
        walkRightTexture2.dispose();

        walkLeftTexture1.dispose();
        standLeftTexture.dispose();
        walkLeftTexture2.dispose();

        walkTopTexture1.dispose();
        standTopTexture.dispose();
        walkTopTexture2.dispose();

        walkBottomTexture1.dispose();
        standBottomTexture.dispose();
        walkBottomTexture2.dispose();

        pokemonCenterTexture.dispose();
    }
}
