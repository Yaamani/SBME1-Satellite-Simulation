package com.yaamani.satellitesimulation.SatellitesOrrbits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

import static com.yaamani.satellitesimulation.Utilities.Constants.COLOR_PATH;
import static com.yaamani.satellitesimulation.Utilities.Constants.G;
import static com.yaamani.satellitesimulation.Utilities.Constants.M;

/**
 * Created by Yamani on 3/25/18.
 */
public class CircularOrbit extends Orbit {

    private float orbitalRadius;
    private float angularVelocity;
    private double theta;

    public CircularOrbit(Satellite satellite, float orbitalRadius) {
        super(satellite);

        this.orbitalRadius = orbitalRadius;
        angularVelocity = (float) Math.sqrt(G * M / Math.pow(orbitalRadius, 3));

        //setOrbitalPeriod();
    }



    public void updateSatellite() {
        //setCurrentTime(time);

        theta = angularVelocity * getCurrentTime()/* * getSpeedMultiplier()*/;

        getSatellite().setPosition(orbitalRadius * (float) Math.cos(theta), orbitalRadius * (float) Math.sin(theta));
    }

    public void drawPath(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(COLOR_PATH));
        shapeRenderer.circle(0, 0, orbitalRadius, 60);
    }

    @Override
    protected void setOrbitalPeriod() {
        this.orbitalPeriod = 2.0d*Math.PI*Math.sqrt((double) (orbitalRadius*orbitalRadius*orbitalRadius) / (G*M));
        //Gdx.app.log("CircularOrbit", "orbitalPeriod = " + orbitalPeriod);
    }

    public float getOrbitalRadius() {
        return orbitalRadius;
    }
}
