package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

import static com.badlogic.drop.PokemonTrainerScreen.UNIT_SIZE;

abstract class CollidableObject extends RectangleMapObject {

    private final Rectangle collisionBox;
    private TextureRegion textureRegion;

    CollidableObject(RectangleMapObject original, Texture mainTexture, int textureX, int textureY, Rectangle collisionBox) {
        super(original.getRectangle().x / UNIT_SIZE,
                original.getRectangle().y / UNIT_SIZE,
                original.getRectangle().width / UNIT_SIZE,
                original.getRectangle().height / UNIT_SIZE
        );

        this.setColor(original.getColor());
        this.setName(original.getName());
        this.setOpacity(original.getOpacity());
        this.setVisible(original.isVisible());
        this.getProperties().putAll(original.getProperties());

        textureRegion = new TextureRegion(mainTexture, textureX, textureY, (int) original.getRectangle().width, (int) original.getRectangle().height);
        this.collisionBox = new Rectangle(getRectangle().x + collisionBox.x, getRectangle().y + collisionBox.y, collisionBox.width, collisionBox.height);
    }

    static CollidableObject fromObject(RectangleMapObject original, Texture mainTexture) {
        switch (original.getName()) {
            case "PokemonCenter":
                return new PokemonCenter(original, mainTexture);
            case "House":
                return new House(original, mainTexture);
            default:
                throw new IllegalArgumentException("Unknown collidable object '" + original.getName() + "'.");
        }
    }

    void reDraw(Batch batch) {
        batch.draw(textureRegion, getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
    }

    boolean overlaps(Rectangle rectangle) {
        return collisionBox.overlaps(rectangle);
    }

    boolean hides(Rectangle rectangle) {
        return getRectangle().overlaps(rectangle)
                && collisionBox.y < rectangle.y;
    }
}
