package com.yaamani.satellitesimulation;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import static com.yaamani.satellitesimulation.Constants.*;

public class SatelliteSimulationGame implements ApplicationListener {
	private ShapeRenderer shapeRenderer;
	private ExtendViewport viewport;

	private Controls controls;


	@Override
	public void create () {
		viewport = new ExtendViewport(WORLD_SIZE, WORLD_SIZE);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);

		controls = new Controls(viewport);


	}

	@Override
	public void resize(int width, int height) {
	    float currentWorldHeight = viewport.getWorldHeight();

        if (height >= width) {
        	Gdx.graphics.setWindowedMode(height, height);
			viewport.update(height,	height, false);
		}
        else viewport.update(width, height, false);

		if (currentWorldHeight != 0)  {
			float aspectRatio = viewport.getWorldWidth() / viewport.getWorldHeight();
			float worldHeight = viewport.getWorldHeight();

			viewport.setWorldSize(aspectRatio * currentWorldHeight, currentWorldHeight);
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		controls.update();

		viewport.apply();

		shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

		shapeRenderer.begin();
		shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

		shapeRenderer.setColor(Color.DARK_GRAY);
		shapeRenderer.rect(-WORLD_SIZE, -WORLD_SIZE, WORLD_SIZE, WORLD_SIZE);

		shapeRenderer.setColor(Color.SKY);
		shapeRenderer.circle(0, 0, WORLD_SIZE/10);

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
