package com.badlogic.pokemon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

import static com.badlogic.pokemon.PokemonTrainerScreen.UNIT_SIZE;

abstract class CollidableObject extends Rectangle {

    final Rectangle drawingBox;
    private final Rectangle relativeCollisionBox;
    private TextureRegion textureRegion;
    private Texture texture;

    CollidableObject(RectangleMapObject mapObject, Rectangle relativeCollisionBox) {
        this(
                // Convert pixel unit rectangle to our world unit rectangle
                new Rectangle(
                        mapObject.getRectangle().x / UNIT_SIZE,
                        mapObject.getRectangle().y / UNIT_SIZE,
                        mapObject.getRectangle().width / UNIT_SIZE,
                        mapObject.getRectangle().height / UNIT_SIZE
                ),
                relativeCollisionBox
        );
    }

    /**
     *
     * @param drawingBox in World Unit dimension. (x, y) represents the bottom left
     * @param relativeCollisionBox in World Unit dimension, relative to the origin (x, y) of the drawingBox.
     *                             (x, y) represents the bottom left of the collision box
     */
    CollidableObject(Rectangle drawingBox, Rectangle relativeCollisionBox) {
        this.drawingBox = drawingBox;
        this.relativeCollisionBox = relativeCollisionBox;
        this.textureRegion = null;
        this.texture = null;

        Rectangle collisionBox = new Rectangle(
                drawingBox.x + relativeCollisionBox.x,
                drawingBox.y + relativeCollisionBox.y,
                relativeCollisionBox.width,
                relativeCollisionBox.height
        );
        this.set(collisionBox);
    }

    static CollidableObject fromObject(RectangleMapObject original, Texture mainTexture) {
        switch (original.getName()) {
            case "PokemonCenter":
                return new PokemonCenter(original, mainTexture);
            case "House":
                return new House(original, mainTexture);
            case "Tree":
                return new Tree(original, mainTexture);
            default:
                throw new IllegalArgumentException("Unknown collidable object '" + original.getName() + "'.");
        }
    }

    void draw(Batch batch) {
        if (textureRegion != null) {
            batch.draw(textureRegion, drawingBox.x, drawingBox.y, drawingBox.width, drawingBox.height);
        } else if (texture != null) {
//            batch.draw(texture, drawingBox.x, drawingBox.y, drawingBox.width, drawingBox.height);
            batch.draw(texture, drawingBox.x, drawingBox.y, drawingBox.width, drawingBox.width * ((float) texture.getHeight() / texture.getWidth()));
        } else {
            throw new IllegalStateException("Draw method has been called before setting a TextureRegion or a Texture");
        }
    }

    void setTexture(Texture texture) {
        this.texture = texture;
    }

    void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    @Override
    public Rectangle setPosition(float x, float y) {
        drawingBox.x = x;
        drawingBox.y = y;

        return super.setPosition(
                x + relativeCollisionBox.x,
                y + relativeCollisionBox.y
        );
    }
}
