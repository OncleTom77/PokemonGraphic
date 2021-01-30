package com.badlogic.pokemon.environment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.pokemon.StaticCollidableObject;

public class Rock extends StaticCollidableObject {

    private static final Rectangle COLLISION_BOX = new Rectangle(0, 0, 2, 1);

    public Rock(RectangleMapObject rectangleMapObject, Texture mainTexture) {
        super(rectangleMapObject,
                COLLISION_BOX,
                mainTexture,
                352,
                656
        );
    }
}
