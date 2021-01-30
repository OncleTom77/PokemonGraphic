package com.badlogic.pokemon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

public abstract class StaticCollidableObject extends CollidableObject {

    /**
     *
     * @param mapObject
     * @param relativeCollisionBox
     * @param mainTexture
     * @param textureX Left position of the texture
     * @param textureY Top position of the texture
     */
    protected StaticCollidableObject(RectangleMapObject mapObject, Rectangle relativeCollisionBox, Texture mainTexture, int textureX, int textureY) {
        super(mapObject, relativeCollisionBox);

        TextureRegion textureRegion = new TextureRegion(mainTexture, textureX, textureY, (int) mapObject.getRectangle().width, (int) mapObject.getRectangle().height);

        setTextureRegion(textureRegion);
    }
}
