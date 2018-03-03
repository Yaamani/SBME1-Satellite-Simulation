package com.yaamani.satellitesimulation;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import static com.yaamani.satellitesimulation.Constants.*;

public class SatelliteSimulationGame extends GestureAdapter implements ApplicationListener {
	private ShapeRenderer shapeRenderer;
	private ExtendViewport viewport;

	private float zoomFactor = 0;
	private float screenToWorldRatio = 0;

	private float aspectRatio = 0;
	private float worldHeight = 0;

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

		zoomFactor = viewport.getWorldHeight() / WORLD_SIZE;
		screenToWorldRatio = WORLD_SIZE / viewport.getScreenHeight();

		aspectRatio = viewport.getWorldWidth() / viewport.getWorldHeight();
		worldHeight = viewport.getWorldHeight();

		keyboardControls();
		flingReleased();
		isZoomStarted();

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

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			cameraPos.set(0, 0, 0);
			viewport.setWorldHeight(WORLD_SIZE);
			viewport.setWorldWidth(aspectRatio * WORLD_SIZE);
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
			Gdx.app.log("CameraPos", "" + cameraPos);
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
			viewport.setWorldHeight(worldHeight * 5f);
			viewport.setWorldWidth(aspectRatio * (worldHeight * 5f));
		}

		Gdx.app.log("Zoom Factor", "" + zoomFactor);
		//if (!isZoomAllowed()) return;

		if (Gdx.input.isKeyPressed(Input.Keys.EQUALS) | Gdx.input.isKeyPressed(Input.Keys.PLUS)) {
			if (isZoomInAllowed()) {
				viewport.setWorldHeight(worldHeight - CAMERA_MOVEMENT_AMOUNT * deltaTime * zoomFactor);
				viewport.setWorldWidth(aspectRatio * (worldHeight - CAMERA_MOVEMENT_AMOUNT * deltaTime * zoomFactor));
			}
		} else if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
			if (isZoomOutAllowed()) {
				viewport.setWorldHeight(worldHeight + CAMERA_MOVEMENT_AMOUNT * deltaTime * zoomFactor);
				viewport.setWorldWidth(aspectRatio * (worldHeight + CAMERA_MOVEMENT_AMOUNT * deltaTime * zoomFactor));
			}
		}
	}

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        Vector3 cameraPos = viewport.getCamera().position;

        cameraPos.set(cameraPos.x - deltaX * zoomFactor * screenToWorldRatio, cameraPos.y + deltaY * zoomFactor * screenToWorldRatio, 0);
        Gdx.app.log("pan", "x = " + x + ", y = " + y);
        return super.pan(x, y, deltaX, deltaY);
    }

	private Vector2 flingVelocity = new Vector2(0, 0);
	private Vector2 flingAcceleration = new Vector2(0, 0);
	private long flingStartTime = 0;
	private double flingEndTime = 0;

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {

		flingVelocity = new Vector2(zoomFactor * screenToWorldRatio * velocityX / FLING_VELOCITY_DIVIDER,zoomFactor * screenToWorldRatio * velocityY / FLING_VELOCITY_DIVIDER);
		flingStartTime = TimeUtils.nanoTime();

		double velocityNormalized = flingVelocity.len();
		Vector2 velocityUnit = new Vector2(flingVelocity.x / (float) velocityNormalized, flingVelocity.y / (float) velocityNormalized);
		flingAcceleration = new Vector2(velocityUnit.x * FLING_ACCELERATION * zoomFactor * screenToWorldRatio , velocityUnit.y * FLING_ACCELERATION * zoomFactor * screenToWorldRatio);
		double accelerationNormalized = -flingAcceleration.len();
		flingEndTime = -velocityNormalized / accelerationNormalized; //t = -vi/a when vf = 0

		Gdx.app.log("Fling", "velocityNormalized = " + velocityNormalized + ", accelerationNormalized = " + accelerationNormalized + ", velocity = " + flingVelocity + ", acceleration = " + flingAcceleration+ ", startTime = " + flingStartTime + ", endTime = " + flingEndTime);
		return super.fling(velocityX, velocityY, button);
	}

	private void flingReleased() {
		if (MathUtils.nanoToSec * (TimeUtils.nanoTime() - flingStartTime) <= flingEndTime) {
			Vector3 cameraPos = viewport.getCamera().position;
			flingVelocity.add(new Vector2(flingAcceleration.x * Gdx.graphics.getDeltaTime(), flingAcceleration.y * Gdx.graphics.getDeltaTime()));
			cameraPos.set(cameraPos.x - flingVelocity.x, cameraPos.y + flingVelocity.y, 0);
		}
	}

	private boolean zoomStarted = false;
	private float worldHeightZoomStarted = 0;

	@Override
	public boolean zoom(float initialDistance, float distance) {
		float ratio = initialDistance / distance;

		if (zoomStarted) {
			if (!isZoomInAllowed()) if (ratio < 1) return true;
			else if (!isZoomOutAllowed()) if (ratio >= 1) return true;

			viewport.setWorldHeight(worldHeightZoomStarted * ratio);
			viewport.setWorldWidth(aspectRatio * (worldHeightZoomStarted * ratio));
		}
		return super.zoom(initialDistance, distance);
	}

	private void isZoomStarted() {
		if (Gdx.input.isTouched(0) & Gdx.input.isTouched(1)) {
			if (!zoomStarted) {
				worldHeightZoomStarted = viewport.getWorldHeight();
			}
			zoomStarted = true;
		} else {
			zoomStarted = false;
		}
	}

	private boolean isZoomInAllowed() {
		return zoomFactor >= MIN_ZOOM_FACTOR;
	}

	private boolean isZoomOutAllowed() {
		return zoomFactor <= MAX_ZOOM_FACTOR;
	}
}
