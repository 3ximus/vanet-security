package vanet.gui;

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

    public VehicleActor(float x, float y) {
        setWidth(WIDTH);
        setHeight(HEIGHT);
        updatePosition(x, y);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.end();

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(getX(), getY(), 0);

        renderer.begin(ShapeType.Filled);
        renderer.setColor(Color.BLUE);
        renderer.rect(0, 0, getWidth(), getHeight());
        renderer.end();

        batch.begin();
    }

    public void updatePosition(float x, float y) {
        setX(x);
        setY(Resources.DEFAULT_MAP_HEIGHT - y - getHeight()); // Makes top/left the 0/0
    }
}