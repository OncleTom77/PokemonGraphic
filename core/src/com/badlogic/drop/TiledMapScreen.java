package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Align;

public class TiledMapScreen implements Screen {

    private static final int UNIT_SIZE = 16;
    private static final float ZOOM_VALUE = 20;

    private final OrthographicCamera cam;
    private final Drop game;

    private final OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private final TiledMap map;

    TiledMapScreen(Drop game) {
        this.game = game;
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        cam = new OrthographicCamera();
        cam.setToOrtho(false, (w / h) * ZOOM_VALUE, ZOOM_VALUE);
        cam.update();

        map = new TmxMapLoader().load("first_map.tmx");

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

        cam.update();
        orthogonalTiledMapRenderer.setView(cam);
        orthogonalTiledMapRenderer.render();

        Batch batch = game.batch;
        batch.begin();
        game.font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 30, 10 * (1 / (float) UNIT_SIZE), Align.bottomLeft, false);
        game.font.draw(batch, "x " + cam.position.x + ", y " + cam.position.y, 10, 15, 10 * (1 / (float) UNIT_SIZE), Align.bottomLeft, false);
        batch.end();
    }

    private void update(float deltaTime) {

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            cam.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            cam.zoom -= 0.02;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cam.position.y += 10 * deltaTime;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.position.x += 10 * deltaTime;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cam.position.y -= 10 * deltaTime;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.position.x -= 10 * deltaTime;
        }
    }

    @Override
    public void resize(int width, int height) {
        cam.viewportWidth = (float) width / ZOOM_VALUE;
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
        orthogonalTiledMapRenderer.dispose();
        map.dispose();
    }
}
