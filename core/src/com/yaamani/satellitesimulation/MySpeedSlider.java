package com.yaamani.satellitesimulation;

import com.badlogic.gdx.graphics.Color;
import com.yaamani.satellitesimulation.SatellitesOrrbits.Orbit;
import com.yaamani.satellitesimulation.UI.Slider;
import com.yaamani.satellitesimulation.Utilities.MyShapeRenderer;

import java.util.ArrayList;

/**
 * Created by Yamani on 3/25/18.
 */

public class MySpeedSlider extends Slider {

    private float topSpeed;

    private MyStage myStage;


    public MySpeedSlider(MyShapeRenderer shapeRenderer,
                         float lineWidth,
                         float lineHeight,
                         float knobRadius,
                         Color lineColor,
                         Color knobColor,
                         float topSpeed,
                         MyStage myStage) {
        super(shapeRenderer, lineWidth, lineHeight, knobRadius, lineColor, knobColor);

        this.topSpeed = topSpeed;

        this.myStage = myStage;

        setPercentage(.5f);
        onDown();
    }

    @Override
    protected void onDragged() {
        onDown();
    }

    @Override
    protected void onDown() {
        myStage.setSpeedMultiplier(1 + (float) getPercentage() * (topSpeed-1));
    }
}
