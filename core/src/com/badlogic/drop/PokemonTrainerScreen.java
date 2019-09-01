package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.badlogic.drop.Direction.*;
import static java.util.stream.Collectors.toList;

public class PokemonTrainerScreen implements Screen {

    private static final float ANIMATION_DURATION = 1 / 6f;
    private static final float STEP_PRECISION = 1 / 5f;
    private static final int UNIT_SIZE = 16;
    static final float STEP_SIZE = STEP_PRECISION;
    private static final float ZOOM_VALUE = 25;

    private final Drop game;

    private final Texture walkUpTexture1;
    private final Texture standUpTexture;
    private final Texture walkUpTexture2;
    private final AnimationByStep walkUpAnimation;

    private final Texture walkRightTexture1;
    private final Texture standRightTexture;
    private final Texture walkRightTexture2;
    private final AnimationByStep walkRightAnimation;

    private final Texture walkDownTexture1;
    private final Texture standDownTexture;
    private final Texture walkDownTexture2;
    private final AnimationByStep walkDownAnimation;

    private final Texture walkLeftTexture1;
    private final Texture standLeftTexture;
    private final Texture walkLeftTexture2;
    private final AnimationByStep walkLeftAnimation;

    private final Texture pokemonCenterTexture;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private Rectangle pokemonCenter;

    private Rectangle pokemonTrainer;
    private double animationState;
    private int nbTimeMove;
    private Direction direction;
    private Rectangle targetPosition;

    private OrthographicCamera cam;
    private List<Rectangle> collisionBoxes;

    PokemonTrainerScreen(Drop game) {
        this.game = game;

        int frameDuration = (int) (1 / STEP_PRECISION);

        walkRightTexture1 = new Texture(Gdx.files.internal("trainer/right/walk_right_1.png"));
        standRightTexture = new Texture(Gdx.files.internal("trainer/right/stand_right.png"));
        walkRightTexture2 = new Texture(Gdx.files.internal("trainer/right/walk_right_2.png"));
        walkRightAnimation = new AnimationByStep(frameDuration, walkRightTexture1, standRightTexture, walkRightTexture2);

        walkLeftTexture1 = new Texture(Gdx.files.internal("trainer/left/walk_left_1.png"));
        standLeftTexture = new Texture(Gdx.files.internal("trainer/left/stand_left.png"));
        walkLeftTexture2 = new Texture(Gdx.files.internal("trainer/left/walk_left_2.png"));
        walkLeftAnimation = new AnimationByStep(frameDuration, walkLeftTexture1, standLeftTexture, walkLeftTexture2);

        walkUpTexture1 = new Texture(Gdx.files.internal("trainer/top/walk_top_1.png"));
        standUpTexture = new Texture(Gdx.files.internal("trainer/top/stand_top.png"));
        walkUpTexture2 = new Texture(Gdx.files.internal("trainer/top/walk_top_2.png"));
        walkUpAnimation = new AnimationByStep(frameDuration, walkUpTexture1, standUpTexture, walkUpTexture2);

        walkDownTexture1 = new Texture(Gdx.files.internal("trainer/bottom/walk_bottom_1.png"));
        standDownTexture = new Texture(Gdx.files.internal("trainer/bottom/stand_bottom.png"));
        walkDownTexture2 = new Texture(Gdx.files.internal("trainer/bottom/walk_bottom_2.png"));
        walkDownAnimation = new AnimationByStep(frameDuration, walkDownTexture1, standDownTexture, walkDownTexture2);

        pokemonTrainer = new Rectangle();
        pokemonTrainer.width = 1;
        pokemonTrainer.height = 1;
        pokemonTrainer.x = UNIT_SIZE;
        pokemonTrainer.y = UNIT_SIZE;

        pokemonCenterTexture = new Texture(Gdx.files.internal("pokemon_center.png"));
        pokemonCenter = new Rectangle();
        pokemonCenter.width = 5;
        pokemonCenter.height = 5 * 7f/8f;
        pokemonCenter.x = 5;
        pokemonCenter.y = 5;

        animationState = 0;
        nbTimeMove = 0;
        direction = RIGHT;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // Constructs a new OrthographicCamera, using the given viewport width and height
        // Height is multiplied by aspect ratio.
        cam = new OrthographicCamera();
        cam.setToOrtho(false, ZOOM_VALUE, ZOOM_VALUE * h / w);

//        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();

        map = new TmxMapLoader().load("first_map.tmx");

        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(map, 1f / UNIT_SIZE);

        MapLayer collision_boxes = map.getLayers().get("collision_boxes");
        collisionBoxes = StreamSupport.stream(collision_boxes.getObjects().spliterator(), true)
                .map(object -> new Rectangle(
                                object.getProperties().get("x", Float.class) / UNIT_SIZE,
                                object.getProperties().get("y", Float.class) / UNIT_SIZE,
                                object.getProperties().get("width", Float.class) / UNIT_SIZE,
                                object.getProperties().get("height", Float.class) / UNIT_SIZE
                        )
                )
                .collect(toList());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        cam.position.x = pokemonTrainer.x;
        cam.position.y = pokemonTrainer.y;

        cam.update();
//        game.batch.setProjectionMatrix(cam.combined);

        Gdx.gl.glClearColor(1, 1, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        orthogonalTiledMapRenderer.setView(cam);
        orthogonalTiledMapRenderer.render();

        Texture currentFrame = getPokemonTrainerTexture();

        Batch batch = orthogonalTiledMapRenderer.getBatch();
        batch.begin();

        if (pokemonTrainer.y > pokemonCenter.y) {
            batch.draw(currentFrame, pokemonTrainer.x, pokemonTrainer.y, pokemonTrainer.width, pokemonTrainer.width * ((float) currentFrame.getHeight() / currentFrame.getWidth()));
            batch.draw(pokemonCenterTexture, pokemonCenter.x, pokemonCenter.y, pokemonCenter.width, pokemonCenter.width * ((float) pokemonCenterTexture.getHeight() / pokemonCenterTexture.getWidth()));
        } else {
            batch.draw(pokemonCenterTexture, pokemonCenter.x, pokemonCenter.y, pokemonCenter.width, pokemonCenter.width * ((float) pokemonCenterTexture.getHeight() / pokemonCenterTexture.getWidth()));
            batch.draw(currentFrame, pokemonTrainer.x, pokemonTrainer.y, pokemonTrainer.width, pokemonTrainer.width * ((float) currentFrame.getHeight() / currentFrame.getWidth()));
        }

//        game.font.draw(game.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
        batch.end();
    }

    private Texture getPokemonTrainerTexture() {
        boolean isStanding = targetPosition == null;
        Texture currentFrame;
        if (direction == UP) {
            if (isStanding) {
                currentFrame = standUpTexture;
            } else {
                currentFrame = walkUpAnimation.getFrameForStep(nbTimeMove);
            }
        } else if (direction == RIGHT) {
            if (isStanding) {
                currentFrame = standRightTexture;
            } else {
                currentFrame = walkRightAnimation.getFrameForStep(nbTimeMove);
            }
        } else if (direction == DOWN) {
            if (isStanding) {
                currentFrame = standDownTexture;
            } else {
                currentFrame = walkDownAnimation.getFrameForStep(nbTimeMove);
            }
        } else {
            if (isStanding) {
                currentFrame = standLeftTexture;
            } else {
                currentFrame = walkLeftAnimation.getFrameForStep(nbTimeMove);
            }
        }
        return currentFrame;
    }

    private void update(float deltaTime) {

        // if the target position is set, continue to move the trainer in the current direction
        if (targetPosition != null) {
            manageMovement(deltaTime, direction);
            return;
        }

        // If RIGHT key is pressed we set the target position to the next right tile
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            trySetNewTargetPosition(deltaTime, UP);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            trySetNewTargetPosition(deltaTime, RIGHT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            trySetNewTargetPosition(deltaTime, DOWN);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            trySetNewTargetPosition(deltaTime, LEFT);
        } else {
            animationState = 0;
            nbTimeMove = 0;
        }
    }

    private void trySetNewTargetPosition(float deltaTime, Direction direction) {
        Rectangle newTargetPosition = direction.getNewTargetPosition(pokemonTrainer);

        Optional<Rectangle> collision = collisionBoxes.stream()
                .filter(newTargetPosition::overlaps)
                .findAny();

        if (!collision.isPresent()) {
            targetPosition = newTargetPosition;
            manageMovement(deltaTime, direction);
        } else {
            this.direction = direction;
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
            pokemonTrainer = direction.updateRectPosition(pokemonTrainer);

            if (targetPosition == null) {
                System.out.println("null");
                break;
            }

            if (pokemonTrainer.x == targetPosition.x && pokemonTrainer.y == targetPosition.y) {
                targetPosition = null;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        cam.viewportWidth = width / ZOOM_VALUE;
        cam.viewportHeight = cam.viewportWidth * height / width;
        cam.update();
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

        walkUpTexture1.dispose();
        standUpTexture.dispose();
        walkUpTexture2.dispose();

        walkDownTexture1.dispose();
        standDownTexture.dispose();
        walkDownTexture2.dispose();

        pokemonCenterTexture.dispose();
        orthogonalTiledMapRenderer.dispose();
        map.dispose();
    }
}
