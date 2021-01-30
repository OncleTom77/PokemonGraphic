package com.badlogic.pokemon.environment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.pokemon.StaticCollidableObject;

public class PokemonCenter extends StaticCollidableObject {

    private static Rectangle COLLISION_BOX = new Rectangle(0, 2, 5, 3);

    public PokemonCenter(RectangleMapObject rectangleMapObject, Texture mainTexture) {
        super(rectangleMapObject,
                COLLISION_BOX,
                mainTexture,
                928,
                1104
        );
    }
}
