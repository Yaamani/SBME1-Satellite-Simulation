package com.yaamani.satellitesimulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.yaamani.satellitesimulation.SatellitesOrrbits.CircularOrbit;
import com.yaamani.satellitesimulation.SatellitesOrrbits.EllipticalOrbit;
import com.yaamani.satellitesimulation.SatellitesOrrbits.Orbit;
import com.yaamani.satellitesimulation.SatellitesOrrbits.Satellite;
import com.yaamani.satellitesimulation.UI.Slider;
import com.yaamani.satellitesimulation.Utilities.MyShapeRenderer;

import static com.yaamani.satellitesimulation.Utilities.Constants.*;

/**
 * Created by Yamani on 3/25/18.
 */

public class MyHohmannTimeline extends Slider {


    private MyShapeRenderer myShapeRenderer;

    private Orbit leo;
    private Orbit gto;
    private Orbit geo;

    private Orbit controllingOrbit;

    private MyStage myStage;

    public MyHohmannTimeline(MyShapeRenderer shapeRenderer,
                             float lineWidth,
                             float lineHeight,
                             float knobRaduis,
                             Color lineColor,
                             Color knobColor,
                             Orbit leo,
                             Orbit gto,
                             Orbit geo,
                             MyStage myStage) {
        super(shapeRenderer, lineWidth, lineHeight, knobRaduis, lineColor, knobColor);

        this.myShapeRenderer = shapeRenderer;

        this.leo = leo;
        this.gto = gto;
        this.geo = geo;

        this.myStage = myStage;

        addDivider(1f/7f);
        addDivider(3f/7f);
    }

    @Override
    protected void onDragged() {
        determiningControllingOrbitAndDrawing();

        determiningOrbitTime();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (myStage.isPlay() & myStage.getState() == MyStage.State.TOTAL & !isDragging()) {

            float orbitPercent = determiningControllingOrbitAndDrawing();

            setPercentage(getPercentage() + (orbitPercent / controllingOrbit.getOrbitalPeriod() * myStage.getSpeedMultiplier()) * delta);

            if (getPercentage() >= 1) setPercentage(3f/7f);

            determiningOrbitTime();
        }

    }

    private float determiningControllingOrbitAndDrawing() {
        float orbitPercent;

        if (getPercentage() < 1f/7f) {
            controllingOrbit = leo;
            orbitPercent = 1f/7f;
        }
        else if (getPercentage() < 3f/7f) {
            controllingOrbit = gto;
            orbitPercent = 2f/7f;
        }
        else {
            controllingOrbit = geo;
            orbitPercent = 4f/7f;
        }

        myStage.getSsg().getMySatellite().setOrbit(controllingOrbit);

        return orbitPercent;
    }

    private void determiningOrbitTime() {
        if (controllingOrbit == leo) controllingOrbit.setCurrentTime((getPercentage() * 7 + 1f) * (controllingOrbit.getOrbitalPeriod()/2f));
        else if (controllingOrbit == gto) controllingOrbit.setCurrentTime((getPercentage() - 1f/7f) * (7f/2f) * controllingOrbit.getOrbitalPeriod()/2f);
        else controllingOrbit.setCurrentTime(((getPercentage() - 3f/7f) * (7f/4f) + .5f) * (controllingOrbit.getOrbitalPeriod()));
    }

    @Override
    public void onResize() {
        super.onResize();
        this.setPosition(getStage().getViewport().getWorldWidth() / 2 - getWidth() / 2, SLIDER_LINE_Y_POS);

    }

    public void drawHohmannPath(MyShapeRenderer myShapeRenderer) {
        if (myStage.getState() != MyStage.State.TOTAL) return;

        if (getPercentage() > 1f/7f) {
            myShapeRenderer.set(ShapeRenderer.ShapeType.Line);
            myShapeRenderer.setColor(new Color(COLOR_PATH));
            myShapeRenderer.ellipse((-((EllipticalOrbit) gto).getA() -((EllipticalOrbit) gto).getA()*((EllipticalOrbit) gto).getE()), -((EllipticalOrbit) gto).getB(), ((EllipticalOrbit) gto).getA() * 2, ((EllipticalOrbit) gto).getB() * 2, 50);

            myShapeRenderer.set(ShapeRenderer.ShapeType.Filled);
            myShapeRenderer.setColor(Color.BLACK);
            myShapeRenderer.arc((-((EllipticalOrbit) gto).getA()*((EllipticalOrbit) gto).getE()), 0, ((EllipticalOrbit) gto).getA(), 180, 180, 60);
        }

        if (getPercentage() >= 3f/7f) gto.getSatellite().setDrawPath(true);
        else gto.getSatellite().setDrawPath(false);


        myShapeRenderer.set(ShapeRenderer.ShapeType.Line);
        myShapeRenderer.setColor(new Color(COLOR_PATH));
        myShapeRenderer.arc(0, 0, ((CircularOrbit) leo).getOrbitalRadius(), 180, 180, 60);

        myShapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        myShapeRenderer.setColor(Color.BLACK);
        myShapeRenderer.rect(-((CircularOrbit) leo).getOrbitalRadius() - 20000, -50000, 2*((CircularOrbit) leo).getOrbitalRadius(), 100000);

    }
}
