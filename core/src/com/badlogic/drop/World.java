package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

class World {

    private TiledMap map;
    private List<CollidableObject> buildings;
    private List<CollidableObject> characters;

    World(TiledMap map, Texture mainTexture) {
        this.map = map;

        MapLayer building_boxes = map.getLayers().get("building_boxes");
        buildings = StreamSupport.stream(building_boxes.getObjects().spliterator(), true)
                .map(object -> (RectangleMapObject) object)
                .map((RectangleMapObject original) -> CollidableObject.fromObject(original, mainTexture))
                .collect(toList());
        characters = new ArrayList<>();
    }

    boolean collides(Rectangle rectangle) {
        return collideFence(rectangle)
                || collideBuilding(rectangle);
    }

    private boolean collideBuilding(Rectangle newTargetPosition) {
        return buildings.stream()
                .anyMatch(collidableObject -> collidableObject.overlaps(newTargetPosition));
    }

    private boolean collideFence(Rectangle position) {
        TiledMapTileLayer fenceLayer = (TiledMapTileLayer) map.getLayers().get("fence");
        return fenceLayer.getCell((int) position.x, (int) position.y) != null;
    }

    void draw(Batch batch) {
        Stream.of(buildings.stream(), characters.stream())
                .flatMap(collidableObjectStream -> collidableObjectStream)
                .sorted((o1, o2) -> (int) (o2.y - o1.y))
                .forEach(collidableObject -> collidableObject.draw(batch));
    }

    void addCharacter(CollidableObject character) {
        characters.add(character);
    }
}
