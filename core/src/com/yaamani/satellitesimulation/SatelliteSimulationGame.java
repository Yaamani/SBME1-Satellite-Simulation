package com.yaamani.satellitesimulation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import static com.yaamani.satellitesimulation.Constants.*;

public class SatelliteSimulationGame implements ApplicationListener {
	private ShapeRenderer shapeRenderer;
	private ExtendViewport viewport;

	@Override
	public void create () {
		viewport = new ExtendViewport(WORLD_SIZE, WORLD_SIZE);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, false);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		keybordControls();

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

	private void keybordControls() {
		Vector3 cameraPos = viewport.getCamera().position;
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			cameraPos.set(cameraPos.x - CAMERA_MOVEMENT_AMOUNT, cameraPos.y, cameraPos.z);
		} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			cameraPos.set(cameraPos.x + CAMERA_MOVEMENT_AMOUNT, cameraPos.y, cameraPos.z);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			cameraPos.set(cameraPos.x, cameraPos.y - CAMERA_MOVEMENT_AMOUNT, cameraPos.z);
		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			cameraPos.set(cameraPos.x, cameraPos.y + CAMERA_MOVEMENT_AMOUNT, cameraPos.z);
		}
	}
}
