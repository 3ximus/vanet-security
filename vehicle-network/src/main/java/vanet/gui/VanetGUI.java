package vanet.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

public class VanetGUI implements ApplicationListener {
	private Stage stage;
	private ShapeRenderer renderer;
	private VehicleActor v;
	private VehicleActor v1;
	private VehicleActor v2;
	private VehicleActor v3;

	@Override
	public void create () {
	    Viewport viewport = new ScalingViewport(Scaling.fit, 1800, 1800, new OrthographicCamera());
		stage = new Stage(viewport);
    	Gdx.input.setInputProcessor(stage);

		// Corners
		v = new VehicleActor(0, 0);
		v1 = new VehicleActor(1780, 1780);
		v2 = new VehicleActor(0, 1780);
		v3 = new VehicleActor(1780, 0);
		stage.addActor(v);
		stage.addActor(v1);
		stage.addActor(v2);
		stage.addActor(v3);

		renderer = new ShapeRenderer();
	}

	@Override
	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		stage.draw();
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}
}
