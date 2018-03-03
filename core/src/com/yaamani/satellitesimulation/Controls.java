package com.yaamani.satellitesimulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import static com.yaamani.satellitesimulation.Constants.*;

/**
 * Created by mahmo on 03/03/2018.
 */

public class Controls implements GestureListener, InputProcessor {
    private ExtendViewport viewport;

    private float zoomFactor = 0;
    private float screenToWorldRatio = 0;

    private float aspectRatio = 0;
    private float worldHeight = 0;

    private InputMultiplexer multiplexer;

    public Controls(ExtendViewport viewport) {
        this.viewport = viewport;

        multiplexer = new InputMultiplexer(this, new GestureDetector(this));
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void update() {
        zoomFactor = viewport.getWorldHeight() / WORLD_SIZE;
        screenToWorldRatio = WORLD_SIZE / viewport.getScreenHeight();

        aspectRatio = viewport.getWorldWidth() / viewport.getWorldHeight();
        worldHeight = viewport.getWorldHeight();

        keyboardControls();
        flingReleased();
        isZoomStarted();
    }

    // ---------------------------- KEYBOARD ---------------------------------

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
            zoom(WORLD_SIZE);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            Gdx.app.log("CameraPos", "" + cameraPos);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            zoom(worldHeight * 5.0f);
        }

        //Gdx.app.log("Zoom Factor", "" + zoomFactor);

        if (Gdx.input.isKeyPressed(Input.Keys.EQUALS) | Gdx.input.isKeyPressed(Input.Keys.PLUS)) {
            if (isZoomInAllowed()) {
                zoom(worldHeight - CAMERA_MOVEMENT_AMOUNT * deltaTime * zoomFactor);
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            if (isZoomOutAllowed()) {
                zoom(worldHeight + CAMERA_MOVEMENT_AMOUNT * deltaTime * zoomFactor);
            }
        }
    }

    // ---------------------------- FLING ---------------------------------

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

        //Gdx.app.log("Fling", "velocityNormalized = " + velocityNormalized + ", accelerationNormalized = " + accelerationNormalized + ", velocity = " + flingVelocity + ", acceleration = " + flingAcceleration+ ", startTime = " + flingStartTime + ", endTime = " + flingEndTime);
        return false;
    }

    private void flingReleased() {
        if (MathUtils.nanoToSec * (TimeUtils.nanoTime() - flingStartTime) <= flingEndTime) {
            Vector3 cameraPos = viewport.getCamera().position;

            flingVelocity.add(new Vector2(flingAcceleration.x * Gdx.graphics.getDeltaTime(), flingAcceleration.y * Gdx.graphics.getDeltaTime()));

            cameraPos.set(cameraPos.x - flingVelocity.x, cameraPos.y + flingVelocity.y, 0);
        }
    }

    // ---------------------------- PAN ---------------------------------

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        Vector3 cameraPos = viewport.getCamera().position;

        cameraPos.set(cameraPos.x - deltaX * zoomFactor * screenToWorldRatio, cameraPos.y + deltaY * zoomFactor * screenToWorldRatio, 0);
        //Gdx.app.log("pan", "x = " + x + ", y = " + y);
        return false;
    }

    // ---------------------------- ZOOM ---------------------------------

    private boolean zoomStarted = false;
    private float worldHeightZoomStarted = 0;

    @Override
    public boolean zoom(float initialDistance, float distance) {
        float ratio = initialDistance / distance;

        if (zoomStarted) {
            //Gdx.app.log("Zoom Ratio", "" + ratio);
            if (!isZoomOutAllowed()) if (ratio > 1) return false;
            if (!isZoomInAllowed()) if (ratio < 1) return false;

            zoom(worldHeightZoomStarted * ratio);
        }
        return false;
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

    //---------------------------- MOUSE WHEEL ZOOM ---------------------------------

    @Override
    public boolean scrolled(int amount) {
        if (!isZoomOutAllowed()) if (amount >= 1) return false;
        if (!isZoomInAllowed()) if (amount < 1) return false;

        zoom(worldHeight + worldHeight * amount * MOUSE_WHEEL_SENSITIVITY);
        Gdx.app.log("Mouse Wheel", "" + amount);
        return false;
    }

    // -------------------- Zoom Func ------------------------
    private void zoom(float height) {
        viewport.setWorldHeight(height);
        viewport.setWorldWidth(aspectRatio * (height));
    }

    private boolean isZoomInAllowed() {
        return zoomFactor >= MIN_ZOOM_FACTOR;
    }

    private boolean isZoomOutAllowed() {
        return zoomFactor <= MAX_ZOOM_FACTOR;
    }














    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }
}
