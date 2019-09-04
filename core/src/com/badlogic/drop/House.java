package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

class House extends StaticCollidableObject {

    private static Rectangle COLLISION_BOX = new Rectangle(0, 2, 5, 3);

    House(RectangleMapObject rectangleMapObject, Texture mainTexture) {
        super(rectangleMapObject,
                COLLISION_BOX,
                mainTexture,
                544,
                1232
        );
    }
}
