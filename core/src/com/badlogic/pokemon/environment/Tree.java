package com.badlogic.pokemon.environment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.pokemon.StaticCollidableObject;

public class Tree extends StaticCollidableObject {

    private static Rectangle COLLISION_BOX = new Rectangle(0, 1, 1, 1);

    public Tree(RectangleMapObject rectangleMapObject, Texture mainTexture) {
        super(rectangleMapObject,
                COLLISION_BOX,
                mainTexture,
                160,
                192
        );
    }
}
