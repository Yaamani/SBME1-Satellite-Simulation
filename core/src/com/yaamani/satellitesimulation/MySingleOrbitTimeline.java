package com.yaamani.satellitesimulation;

import com.badlogic.gdx.graphics.Color;
import com.yaamani.satellitesimulation.SatellitesOrrbits.Orbit;
import com.yaamani.satellitesimulation.UI.Slider;
import com.yaamani.satellitesimulation.Utilities.MyShapeRenderer;

import static com.yaamani.satellitesimulation.Utilities.Constants.*;

/**
 * Created by Yamani on 3/25/18.
 */

public class MySingleOrbitTimeline extends Slider {

    private MyShapeRenderer myShapeRenderer;

    private Orbit controllingOrbit;

    private MyStage myStage;

    public MySingleOrbitTimeline(MyShapeRenderer shapeRenderer, float lineWidth, float lineHeight, float knobRaduis, Color lineColor, Color knobColor, MyStage myStage) {
        super(shapeRenderer, lineWidth, lineHeight, knobRaduis, lineColor, knobColor);

        this.myShapeRenderer = shapeRenderer;
        this.myStage = myStage;
    }

    public void setControllingOrbit(Orbit controllingOrbit) {
        this.controllingOrbit = controllingOrbit;
        myStage.getSsg().getMySatellite().setOrbit(controllingOrbit);
    }

    public Orbit getControllingOrbit() {
        return controllingOrbit;
    }

    @Override
    public void onResize() {
        super.onResize();
        this.setPosition(getStage().getViewport().getWorldWidth() / 2 - getWidth() / 2, SLIDER_LINE_Y_POS);
    }

    @Override
    protected void onDragged() {
        controllingOrbit.setCurrentTime(getPercentage() * controllingOrbit.getOrbitalPeriod());
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (myStage.isPlay() & myStage.getState() == MyStage.State.INDIVIDUAL & !isDragging()) {

            controllingOrbit.getSatellite().setDrawPath(true);
            setPercentage(getPercentage() + (1f / controllingOrbit.getOrbitalPeriod() * myStage.getSpeedMultiplier()) * delta);

            myStage.getSsg().getMySatellite().setOrbit(controllingOrbit);

            if (getPercentage() >= 1) setPercentage(0);

            controllingOrbit.setCurrentTime(getPercentage() * controllingOrbit.getOrbitalPeriod());

        }
    }
}
