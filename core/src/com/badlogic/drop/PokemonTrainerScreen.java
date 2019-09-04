package com.badlogic.drop;

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

import java.util.Arrays;

import static com.badlogic.drop.Direction.RIGHT;
import static com.badlogic.drop.Trainer.STEP_PRECISION;

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

    private final Texture pokemonCenterTexture;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private final World world;
    private final Trainer trainer;

    private Rectangle pokemonCenter;

    private OrthographicCamera cam;

    private Texture mainTexture;

    PokemonTrainerScreen(Drop game) {

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

        pokemonCenterTexture = new Texture(Gdx.files.internal("pokemon_center.png"));
        pokemonCenter = new Rectangle();
        pokemonCenter.width = 5;
        pokemonCenter.height = 5 * 7f / 8f;
        pokemonCenter.x = 5;
        pokemonCenter.y = 5;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // Constructs a new OrthographicCamera, using the given viewport width and height
        // Height is multiplied by aspect ratio.
        cam = new OrthographicCamera();
        cam.setToOrtho(false, ZOOM_VALUE, ZOOM_VALUE * h / w);
        cam.update();

        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(map, 1f / UNIT_SIZE);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        cam.position.x = trainer.getPosition().x;
        cam.position.y = trainer.getPosition().y;

        cam.update();
//        game.batch.setProjectionMatrix(cam.combined);

        Gdx.gl.glClearColor(1, 1, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        orthogonalTiledMapRenderer.setView(cam);
        orthogonalTiledMapRenderer.render();

        Batch batch = orthogonalTiledMapRenderer.getBatch();
        batch.begin();

//        batch.draw(pokemonCenterTexture, pokemonCenter.x, pokemonCenter.y, pokemonCenter.width, pokemonCenter.width * ((float) pokemonCenterTexture.getHeight() / pokemonCenterTexture.getWidth()));

//        trainer.draw(batch);
        world.draw(batch);

//        collidableObjects.stream()
//                .filter(collidableObject -> collidableObject.hides(pokemonTrainer))
//                .forEach(collidableObject -> collidableObject.reDraw(batch));

//        game.font.draw(game.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
        batch.end();
    }

    private void update(float delta) {
        trainer.update(delta, Gdx.input);
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

        pokemonCenterTexture.dispose();
        orthogonalTiledMapRenderer.dispose();
        map.dispose();
    }
}
