package com.yaamani.satellitesimulation;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import static com.yaamani.satellitesimulation.Constants.*;

public class SatelliteSimulationGame extends GestureAdapter implements ApplicationListener {
	private ShapeRenderer shapeRenderer;
	private ExtendViewport viewport;

	@Override
	public void create () {
		viewport = new ExtendViewport(WORLD_SIZE, WORLD_SIZE);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);

		Gdx.input.setInputProcessor(new GestureDetector(this));
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

		keyboardControls();

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

	private void keyboardControls() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		Vector3 cameraPos = viewport.getCamera().position;
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			cameraPos.set(cameraPos.x - CAMERA_MOVEMENT_AMOUNT * deltaTime, cameraPos.y, cameraPos.z);
		} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			cameraPos.set(cameraPos.x + CAMERA_MOVEMENT_AMOUNT * deltaTime, cameraPos.y, cameraPos.z);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			cameraPos.set(cameraPos.x, cameraPos.y - CAMERA_MOVEMENT_AMOUNT * deltaTime, cameraPos.z);
		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			cameraPos.set(cameraPos.x, cameraPos.y + CAMERA_MOVEMENT_AMOUNT * deltaTime, cameraPos.z);
		}

		float aspectRatio = viewport.getWorldWidth() / viewport.getWorldHeight();
		float worldHeight = viewport.getWorldHeight();
		if (Gdx.input.isKeyPressed(Input.Keys.EQUALS) | Gdx.input.isKeyPressed(Input.Keys.PLUS)) {
			viewport.setWorldHeight(worldHeight - CAMERA_MOVEMENT_AMOUNT * deltaTime);
			viewport.setWorldWidth(aspectRatio * (worldHeight - CAMERA_MOVEMENT_AMOUNT * deltaTime));
		} else if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
			viewport.setWorldHeight(worldHeight + CAMERA_MOVEMENT_AMOUNT * deltaTime);
			viewport.setWorldWidth(aspectRatio * (worldHeight + CAMERA_MOVEMENT_AMOUNT * deltaTime));
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			cameraPos.set(0, 0, 0);
			viewport.setWorldHeight(WORLD_SIZE);
			viewport.setWorldWidth(aspectRatio * WORLD_SIZE);
		}
	}

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        Vector3 cameraPos = viewport.getCamera().position;
		float zoomFactor = viewport.getWorldHeight() / WORLD_SIZE;

        cameraPos.set(cameraPos.x - deltaX * zoomFactor, cameraPos.y + deltaY * zoomFactor, 0);
        return super.pan(x, y, deltaX, deltaY);
    }


}
