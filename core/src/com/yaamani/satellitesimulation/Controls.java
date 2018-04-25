package com.yaamani.satellitesimulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import static com.yaamani.satellitesimulation.Utilities.Constants.*;

/**
 * Created by mahmo on 03/03/2018.
 */

public class Controls implements GestureListener, InputProcessor {
    private ExtendViewport viewport;

    private float zoomFactor = 0;
    private float screenToWorldRatio = 0;

    private float aspectRatio = 0;
    private float worldHeight = 0;

    private float yLimit;
    private float xLimit;

    private MyStage myStage;

    public Controls(ExtendViewport viewport, MyStage myStage) {
        this.viewport = viewport;
        this.myStage = myStage;

        reset();
    }

    public void update() {
        zoomFactor = viewport.getWorldHeight() / WORLD_SIZE;
        screenToWorldRatio = WORLD_SIZE / viewport.getScreenHeight();

        aspectRatio = viewport.getWorldWidth() / viewport.getWorldHeight();
        worldHeight = viewport.getWorldHeight();

        yLimit = (WORLD_SIZE / 2) * MAX_ZOOM_FACTOR;
        xLimit = (WORLD_SIZE * aspectRatio / 2) * MAX_ZOOM_FACTOR;

        keyboardControls();
        flingReleased();
        isZoomPinchStarted();
    }

    // ---------------------------- KEYBOARD ---------------------------------

    private void keyboardControls() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        short horizontalAxis = 0;
        short verticalAxis = 0;

        Vector3 cameraPos = viewport.getCamera().position;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            horizontalAxis = -1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            horizontalAxis = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            verticalAxis = -1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            verticalAxis = 1;
        }

        cameraPos.set(cameraPos.x + CAMERA_MOVEMENT_AMOUNT * deltaTime * horizontalAxis, cameraPos.y + CAMERA_MOVEMENT_AMOUNT * deltaTime * verticalAxis, cameraPos.z);

        cameraBoundaries(-horizontalAxis, verticalAxis);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            reset();
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
        flingVelocity = new Vector2(zoomFactor * screenToWorldRatio * velocityX / FLING_VELOCITY_DIVIDER,
                                    zoomFactor * screenToWorldRatio * velocityY / FLING_VELOCITY_DIVIDER);
        flingStartTime = TimeUtils.nanoTime();

        double velocityNormalized = flingVelocity.len();
        Vector2 velocityUnit = new Vector2(flingVelocity.x / (float) velocityNormalized,
                                            flingVelocity.y / (float) velocityNormalized);

        flingAcceleration = new Vector2(velocityUnit.x * FLING_ACCELERATION * zoomFactor * screenToWorldRatio ,
                                        velocityUnit.y * FLING_ACCELERATION * zoomFactor * screenToWorldRatio);
        double accelerationNormalized = -flingAcceleration.len();

        flingEndTime = -velocityNormalized / accelerationNormalized; //t = -vi/a when vf = 0

        //Gdx.app.log("Fling", "velocityNormalized = " + velocityNormalized + ", accelerationNormalized = " + accelerationNormalized + ", velocity = " + flingVelocity + ", acceleration = " + flingAcceleration+ ", startTime = " + flingStartTime + ", endTime = " + flingEndTime);
        Gdx.app.log("Fling", "button = " + button);
        return false;
    }

    private void flingReleased() {
        if (MathUtils.nanoToSec * (TimeUtils.nanoTime() - flingStartTime) <= flingEndTime) {
            Vector3 cameraPos = viewport.getCamera().position;

            flingVelocity.add(new Vector2(flingAcceleration.x * Gdx.graphics.getDeltaTime(),
                                            flingAcceleration.y * Gdx.graphics.getDeltaTime()));

            cameraPos.set(cameraPos.x - flingVelocity.x,
                            cameraPos.y + flingVelocity.y, 0);

            cameraBoundaries(flingVelocity.x, flingVelocity.y);
        }
    }

    // ---------------------------- PAN ---------------------------------

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        Vector3 cameraPos = viewport.getCamera().position;
        //Gdx.app.log("pan", "WORLD_SIZE = " + WORLD_SIZE + ", worldHeight = " + worldHeight + ", cameraPos.x = " + cameraPos.x + ", cameraPos.y = " + cameraPos.y + ", deltaX = " + deltaX + ", deltaY = " + deltaY);

        cameraPos.set(cameraPos.x - deltaX * zoomFactor * screenToWorldRatio, cameraPos.y + deltaY * zoomFactor * screenToWorldRatio, 0);

        cameraBoundaries(deltaX, deltaY);

        return false;
    }

    // ---------------------------- ZOOM ---------------------------------

    private boolean zoomPinchStarted = false;
    private float worldHeightZoomStarted = 0;

    @Override
    public boolean zoom(float initialDistance, float distance) {
        float ratio = initialDistance / distance;

        if (zoomPinchStarted) {
            if (!isZoomOutAllowed()) if (ratio > 1) return false;
            if (!isZoomInAllowed()) if (ratio < 1) return false;

            zoom(worldHeightZoomStarted * ratio);
        }
        return false;
    }

    private void isZoomPinchStarted() {
        if (Gdx.input.isTouched(0) & Gdx.input.isTouched(1)) {
            if (!zoomPinchStarted) {
                worldHeightZoomStarted = viewport.getWorldHeight();
            }
            zoomPinchStarted = true;
        } else {
            zoomPinchStarted = false;
        }
    }

    //---------------------------- MOUSE WHEEL ZOOM ---------------------------------

    @Override
    public boolean scrolled(int amount) {
        if (!isZoomOutAllowed()) if (amount >= 1) {
            return false;
        }

        if (!isZoomInAllowed()) if (amount < 1) {
            return false;
        }

        zoom(worldHeight + worldHeight * amount * MOUSE_WHEEL_SENSITIVITY);
        //Gdx.app.log("Mouse Wheel", "" + amount);
        return false;
    }

    // ---------------------- Pinch --------------------------

    @Override
    public boolean longPress(float x, float y) {
        reset();
        return false;
    }

    private int pinchCount = 0;
    private Vector2 lastPointer1 = new Vector2(Vector2.Zero);
    private Vector2 lastPointer2 = new Vector2(Vector2.Zero);
    private long lastPinchTime = 0;
    float deltaTime;
    private float deltaX;
    private float deltaY;

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        Gdx.app.log("Pinch parameters", "initialPointer = " + initialPointer1 + ", initialPointer2 = " + initialPointer2 +
                ", pointer1 = " + pointer1 + ", lastPointer1 = " + lastPointer1 +
                ", pointer2 = " + pointer2 + ", lastPointer2 = " + lastPointer2);

        if(pinchCount == 0) {
            lastPointer1.set(pointer1);
            lastPointer2.set(pointer2);
        } else {
            deltaX = pointer1.x - lastPointer1.x;
            deltaY = pointer1.y - lastPointer1.y;

            deltaTime = (TimeUtils.nanoTime() - lastPinchTime) * MathUtils.nanoToSec;

            pan(pointer1.x, pointer1.y, deltaX, deltaY);


            //Gdx.app.log("Pinch average pointers - counter", "averageLastPointer = " + averageLastPointer + ", averagePointer = " + averagePointer + ", pinchCount = " + pinchCount);
        }

        lastPinchTime = TimeUtils.nanoTime();

        lastPointer1.set(pointer1);
        lastPointer2.set(pointer2);

        pinchCount++;

        return false;
    }

    @Override
    public void pinchStop() {
        //TODO: Fling when pinch stops.
        Gdx.app.log("PinchStop", "Stopped");
        pinchCount = 0;
    }

    // -------------------- Zoom Func ------------------------
    public void zoom(float height) {
        viewport.setWorldHeight(height);
        viewport.setWorldWidth(aspectRatio * (height));
    }

    private boolean isZoomInAllowed() {
        return zoomFactor >= MIN_ZOOM_FACTOR;
    }

    private boolean isZoomOutAllowed() {
        return zoomFactor <= MAX_ZOOM_FACTOR;
    }

    public void reset() {
        viewport.getCamera().position.set(0, 0, 0);
        zoom(WORLD_SIZE * 1.5f);
    }

    // --------------------- Camera Boundaries -----------------

    private void cameraBoundaries(float deltaX, float deltaY) {
        Vector3 cameraPos = viewport.getCamera().position;

        if (deltaY < 0 & cameraPos.y <= -yLimit) {
            cameraPos.y = -yLimit;
        } else if (deltaY > 0 & cameraPos.y >= yLimit) {
            cameraPos.y = yLimit;
        }

        if (deltaX > 0 & cameraPos.x <= -xLimit) {
            cameraPos.x = -xLimit;
        } else if (deltaX < 0 & cameraPos.x >= xLimit) {
            cameraPos.x = xLimit;
        }
    }












    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        /*if (myStage.isPlay()) myStage.setPlay(false);
        else myStage.setPlay(true);*/

        return false;
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
