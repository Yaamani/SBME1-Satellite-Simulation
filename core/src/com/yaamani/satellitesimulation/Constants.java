package com.yaamani.satellitesimulation;

/**
 * Created by mahmo on 02/03/2018.
 */

public class Constants {
    public static final float WORLD_SIZE = 63710; //KM
    public static final float CAMERA_MOVEMENT_AMOUNT = WORLD_SIZE / 4;

    public static final float FLING_ACCELERATION = -100.0f;
    public static final float FLING_VELOCITY_DIVIDER = 70.0f;

    public static final float MAX_ZOOM_FACTOR = 2.0f;
    public static final float MIN_ZOOM_FACTOR = 0.3f;
    public static final float MOUSE_WHEEL_SENSITIVITY = 0.1f;
}
