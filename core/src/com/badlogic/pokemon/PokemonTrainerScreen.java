package com.badlogic.pokemon;

import com.badlogic.drop.Drop;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;

import static com.badlogic.pokemon.Direction.RIGHT;
import static com.badlogic.pokemon.Trainer.ANIMATION_DURATION;
import static com.badlogic.pokemon.Trainer.STEP_PRECISION;

public class PokemonTrainerScreen implements Screen {

    static final int UNIT_SIZE = 16;
    private static final float ZOOM_VALUE = 25;

    private final Texture walkUpTexture1;
    private final Texture standUpTexture;
    private final Texture walkUpTexture2;

    private final Texture walkRightTexture1;
    private final Texture standRightTexture;
    private final Texture walkRightTexture2;

    private final Texture walkDownTexture1;
    private final Texture standDownTexture;
    private final Texture walkDownTexture2;

    private final Texture walkLeftTexture1;
    private final Texture standLeftTexture;
    private final Texture walkLeftTexture2;

    private final TiledMap map;
    private final OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private final World world;
    private final Trainer trainer;

    private OrthographicCamera cam;
    private Rectangle camTargetPosition;
    private double animationState;
    private int nbMove;
    private Direction camTargetDirection;
    private boolean camFreeze;
    private boolean zoomIn;
    private int mapLevel = 0;

    private Texture mainTexture;

    public PokemonTrainerScreen(Drop game) {

        mainTexture = new Texture(Gdx.files.internal("pokemon_tileset.png"));
        map = new TmxMapLoader().load("first_map.tmx");

        world = new World(map, mainTexture);

        int frameDuration = (int) (1 / STEP_PRECISION);
        walkRightTexture1 = new Texture(Gdx.files.internal("trainer/right/walk_right_1.png"));
        standRightTexture = new Texture(Gdx.files.internal("trainer/right/stand_right.png"));
        walkRightTexture2 = new Texture(Gdx.files.internal("trainer/right/walk_right_2.png"));
        AnimationByStep walkRightAnimation = new AnimationByStep(frameDuration, 1, walkRightTexture1, standRightTexture, walkRightTexture2);

        walkLeftTexture1 = new Texture(Gdx.files.internal("trainer/left/walk_left_1.png"));
        standLeftTexture = new Texture(Gdx.files.internal("trainer/left/stand_left.png"));
        walkLeftTexture2 = new Texture(Gdx.files.internal("trainer/left/walk_left_2.png"));
        AnimationByStep walkLeftAnimation = new AnimationByStep(frameDuration, 1, walkLeftTexture1, standLeftTexture, walkLeftTexture2);

        walkUpTexture1 = new Texture(Gdx.files.internal("trainer/top/walk_top_1.png"));
        standUpTexture = new Texture(Gdx.files.internal("trainer/top/stand_top.png"));
        walkUpTexture2 = new Texture(Gdx.files.internal("trainer/top/walk_top_2.png"));
        AnimationByStep walkUpAnimation = new AnimationByStep(frameDuration, 1, walkUpTexture1, standUpTexture, walkUpTexture2);

        walkDownTexture1 = new Texture(Gdx.files.internal("trainer/bottom/walk_bottom_1.png"));
        standDownTexture = new Texture(Gdx.files.internal("trainer/bottom/stand_bottom.png"));
        walkDownTexture2 = new Texture(Gdx.files.internal("trainer/bottom/walk_bottom_2.png"));
        AnimationByStep walkDownAnimation = new AnimationByStep(frameDuration, 1, walkDownTexture1, standDownTexture, walkDownTexture2);

        trainer = new Trainer(
                Arrays.asList(walkUpAnimation, walkRightAnimation, walkDownAnimation, walkLeftAnimation),
                new Rectangle(UNIT_SIZE, UNIT_SIZE, 1, 1),
                RIGHT,
                world
        );

        world.addCharacter(trainer);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // Constructs a new OrthographicCamera, using the given viewport width and height
        // Height is multiplied by aspect ratio.
        cam = new OrthographicCamera();
        cam.setToOrtho(false, ZOOM_VALUE, ZOOM_VALUE * h / w);
        cam.update();
        camFreeze = false;

        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(map, 1f / UNIT_SIZE);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(1, 1, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        orthogonalTiledMapRenderer.setView(cam);
        orthogonalTiledMapRenderer.render();

        Batch batch = orthogonalTiledMapRenderer.getBatch();

        batch.begin();
        world.draw(batch);
//        game.font.draw(game.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
        batch.end();
    }

    private void update(float delta) {
        trainer.update(delta, Gdx.input);

        updateCamPosition(delta);
    }

    private void updateCamPosition(float delta) {
        trainer.getLeavingStairwayPosition().ifPresent(rectangle -> {
            if (rectangle.x == cam.position.x && rectangle.y == cam.position.y) {
                camFreeze = true;
            } else {
                // Initiate moving camera to trainer target position
                camTargetPosition = rectangle;
                camTargetDirection = Direction.getDirectionOfPosition(rectangle.getPosition(Vector2.Zero), cam.position);
                animationState = 0;
                nbMove = 0;
                mapLevel = mapLevel == 1 ? 0 : 1;
                cam.zoom = mapLevel == 0 ? 1 : (float) 0.95;
            }
        });

        if (cam.position.x == trainer.x && cam.position.y == trainer.y) {
            camFreeze = false;
        }

//        if (camTargetPosition != null) {
//            animationState += delta;
//            while (animationState >= (nbMove + 1) * STEP_PRECISION * ANIMATION_DURATION * 1 / 2) {
//                nbMove++;
//                Rectangle updatedRectPosition = camTargetDirection.updateRectPosition(new Rectangle(cam.position.x, cam.position.y, 0, 0));
//                cam.position.x = updatedRectPosition.x;
//                cam.position.y = updatedRectPosition.y;
//                cam.zoom += zoomIn ? -0.005 : 0.005;
//                cam.zoom = (float) (Math.round((cam.zoom) * 1000) / 1000.0);
//
//                if (cam.position.x == camTargetPosition.x && cam.position.y == camTargetPosition.y) {
//                    System.out.println("New zoom value : " + cam.zoom);
//                    camTargetPosition = null;
//                    break;
//                }
//            }
//        } else if (!trainer.isEnteringOnStairway() && !trainer.isOnStairway() && !camFreeze) {
            // Just follow trainer
            cam.position.x = trainer.x;
            cam.position.y = trainer.y;
//        }

        if (trainer.isOnStairway()) {
            cam.zoom = (float) 0.975;
        }

//        if (!lastCamPosition.equals(cam.position)) {
//            System.out.println(cam.position);
//        }
        cam.update();
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

        mainTexture.dispose();

        orthogonalTiledMapRenderer.dispose();
        map.dispose();
    }
}
