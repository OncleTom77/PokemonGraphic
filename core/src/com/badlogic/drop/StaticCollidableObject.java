package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

abstract class StaticCollidableObject extends CollidableObject {

    /**
     *
     * @param mapObject
     * @param relativeCollisionBox
     * @param mainTexture
     * @param textureX Left position of the texture
     * @param textureY Top position of the texture
     */
    StaticCollidableObject(RectangleMapObject mapObject, Rectangle relativeCollisionBox, Texture mainTexture, int textureX, int textureY) {
        super(mapObject, relativeCollisionBox);

        TextureRegion textureRegion = new TextureRegion(mainTexture, textureX, textureY, (int) mapObject.getRectangle().width, (int) mapObject.getRectangle().height);

        setTextureRegion(textureRegion);
    }
}
