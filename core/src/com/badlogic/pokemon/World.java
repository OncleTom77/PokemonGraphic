package com.badlogic.pokemon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.badlogic.pokemon.PokemonTrainerScreen.UNIT_SIZE;
import static java.util.stream.Collectors.toList;

class World {

    private TiledMap map;
    private List<CollidableObject> buildings;
    private List<CollidableObject> trees;
    private List<CollidableObject> characters;
    private List<Rectangle> stairwayFreeways;

    World(TiledMap map, Texture mainTexture) {
        this.map = map;

        MapLayer building_boxes = map.getLayers().get("building_boxes");
        buildings = StreamSupport.stream(building_boxes.getObjects().spliterator(), true)
                .map(object -> (RectangleMapObject) object)
                .map((RectangleMapObject original) -> CollidableObject.fromObject(original, mainTexture))
                .collect(toList());
        MapLayer tree_boxes = map.getLayers().get("tree_boxes");
        trees = StreamSupport.stream(tree_boxes.getObjects().spliterator(), true)
                .map(object -> (RectangleMapObject) object)
                .map((RectangleMapObject original) -> CollidableObject.fromObject(original, mainTexture))
                .collect(toList());
        MapLayer stairway_freeway_boxes = map.getLayers().get("stairway_freeway_boxes");
        stairwayFreeways = StreamSupport.stream(stairway_freeway_boxes.getObjects().spliterator(), true)
                .map(object -> ((RectangleMapObject) object).getRectangle())
                .map(rectangle -> new Rectangle(
                        rectangle.x / UNIT_SIZE,
                        rectangle.y / UNIT_SIZE,
                        rectangle.width / UNIT_SIZE,
                        rectangle.height / UNIT_SIZE
                ))
                .collect(toList());
        characters = new ArrayList<>();
    }

    boolean collides(Rectangle rectangle) {
        return !isOnStairway(rectangle)
                && (collidesFence(rectangle)
                || collidesMountain(rectangle)
                || collidesEnvironment(rectangle));
    }

    private boolean collidesEnvironment(Rectangle newTargetPosition) {
        return Stream.of(buildings.stream(), trees.stream())
                .flatMap(collidableObjects -> collidableObjects)
                .anyMatch(collidableObject -> collidableObject.overlaps(newTargetPosition));
    }

    private boolean collidesFence(Rectangle position) {
        TiledMapTileLayer fenceLayer = (TiledMapTileLayer) map.getLayers().get("fence");
        return fenceLayer.getCell((int) position.x, (int) position.y) != null;
    }

    private boolean collidesMountain(Rectangle position) {
        TiledMapTileLayer mountain = (TiledMapTileLayer) map.getLayers().get("mountain");
        return mountain.getCell((int) position.x, (int) position.y) != null;
    }

    boolean isOnStairway(Rectangle position) {
        return stairwayFreeways.stream()
                .anyMatch(mapObject -> mapObject.x == position.x && mapObject.y == position.y);
    }

    void draw(Batch batch) {
        Stream.of(buildings.stream(), trees.stream(), characters.stream())
                .flatMap(collidableObjectStream -> collidableObjectStream)
                .sorted((o1, o2) -> (int) (o2.y - o1.y))
                .forEach(collidableObject -> collidableObject.draw(batch));
    }

    void addCharacter(CollidableObject character) {
        characters.add(character);
    }
}
