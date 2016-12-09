package vanet.gui;

import globals.AttackerEnum;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

import globals.Resources;


public class VehicleActor extends Actor {
    private ShapeRenderer renderer = new ShapeRenderer();
    private final int WIDTH = 35;
    private final int HEIGHT = 35;
    private AttackerEnum aType;

    public VehicleActor(float x, float y, AttackerEnum aType) {
        setWidth(WIDTH);
        setHeight(HEIGHT);
        updatePosition(x, y);
        aType = aType;
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.end();

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(getX(), getY(), 0);

        renderer.begin(ShapeType.Filled);
        if (aType == AttackerEnum.NO_ATTACKER)
            renderer.setColor(Color.BLUE);
        else
            renderer.setColor(Color.RED);
        renderer.rect(0, 0, getWidth(), getHeight());
        renderer.end();

        batch.begin();
    }

    public void updatePosition(float x, float y) {
        setX(x);
        setY(Resources.DEFAULT_MAP_HEIGHT - y - getHeight()); // Makes top/left the 0/0
    }
}