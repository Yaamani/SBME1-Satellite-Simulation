package com.yaamani.satellitesimulation;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import static com.yaamani.satellitesimulation.Constants.*;

public class SatelliteSimulationGame implements ApplicationListener {
	private ShapeRenderer shapeRenderer;

	private ExtendViewport staticViewport; // doesn't respond to camera movement
	private ExtendViewport viewport;

	private Controls controls;

	private Satellite mySatellite;

	@Override
	public void create () {
		staticViewport = new ExtendViewport(WORLD_SIZE, WORLD_SIZE);
		viewport = new ExtendViewport(WORLD_SIZE, WORLD_SIZE);

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);

		controls = new Controls(viewport);

		mySatellite = new Satellite(WORLD_SIZE / 100, Color.WHITE);
	}

	@Override
	public void resize(int width, int height) {
		float currentWorldHeightStatic = staticViewport.getWorldHeight();
	    float currentWorldHeight = viewport.getWorldHeight();

        if (height >= width) {
        	Gdx.graphics.setWindowedMode(height, height);
        	staticViewport.update(height, height);
			viewport.update(height,	height, false);
		}
        else {
			staticViewport.update(width, height, false);
			viewport.update(width, height, false);
		}

		if (currentWorldHeight != 0)  {
			float aspectRatioStatic = staticViewport.getWorldWidth() / staticViewport.getWorldHeight();
			float aspectRatio = viewport.getWorldWidth() / viewport.getWorldHeight();

			staticViewport.setWorldSize(aspectRatioStatic * currentWorldHeightStatic, currentWorldHeightStatic);
			viewport.setWorldSize(aspectRatio * currentWorldHeight, currentWorldHeight);
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		controls.update();

		staticViewport.apply();
		shapeRenderer.setProjectionMatrix(staticViewport.getCamera().combined);

		shapeRenderer.begin();
		shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

		shapeRenderer.setColor(Color.MAROON);
		shapeRenderer.rect(-WORLD_SIZE / 2, -WORLD_SIZE / 2, WORLD_SIZE, WORLD_SIZE);

		viewport.apply();
		shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

		shapeRenderer.setColor(Color.SKY);
		shapeRenderer.circle(0, 0, WORLD_SIZE / 10/*6371*/, 45);

		mySatellite.render(shapeRenderer);

		shapeRenderer.end();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose () {
		shapeRenderer.dispose();
	}
}
