package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

class Tree extends StaticCollidableObject {

    private static Rectangle COLLISION_BOX = new Rectangle(0, 1, 1, 1);

    Tree(RectangleMapObject rectangleMapObject, Texture mainTexture) {
        super(rectangleMapObject,
                COLLISION_BOX,
                mainTexture,
                160,
                192
        );
    }
}
