package com.yaamani.satellitesimulation;

/**
 * Created by mahmo on 02/03/2018.
 */

public class Constants {
    public static final float WORLD_SIZE = 63710000;
    public static final float CAMERA_MOVEMENT_AMOUNT = WORLD_SIZE / 4;

    public static final float FLING_ACCELERATION = -100.0f;
    public static final float FLING_VELOCITY_DIVIDER = 70.0f;

    public static final float MAX_ZOOM_FACTOR = 5.0f;
    public static final float MIN_ZOOM_FACTOR = 0.3f;
    public static final float MOUSE_WHEEL_SENSITIVITY = 0.1f;

    public static final float UI_HORIZONTAL_GAPS = WORLD_SIZE / 25;
    
    public static final float MAIN_SLIDER_LINE_WIDTH = WORLD_SIZE * 3f/5f - UI_HORIZONTAL_GAPS;
    public static final float BUTTON_WIDTH = WORLD_SIZE * 1f/5f - UI_HORIZONTAL_GAPS;
    public static final float SPEED_SLIDER_LINE_WIDTH = BUTTON_WIDTH;

    public static final float SLIDER_LINE_HEIGHT = WORLD_SIZE / 200;
    public static final float SLIDER_KNOB_RADUIS = WORLD_SIZE / 50;
    public static final float SLIDER_LINE_Y_POS = WORLD_SIZE / 12;
    public static final float SLIDER_DIVIDER_WIDTH = SLIDER_LINE_HEIGHT;
    public static final float SLIDER_DIVIDER_HEIGHT = SLIDER_KNOB_RADUIS * 2 + WORLD_SIZE / 100;
    public static final int COLOR_LIGHT = 0xECF9FEFF;
    public static final int COLOR_DARK = 0x3F93B8FF;

    public static final double G = 6.674E-11;
    public static final double M = 5.972E24;
}
