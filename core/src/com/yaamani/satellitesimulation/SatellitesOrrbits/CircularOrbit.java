package com.yaamani.satellitesimulation.SatellitesOrrbits;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

import static com.yaamani.satellitesimulation.Utilities.Constants.G;
import static com.yaamani.satellitesimulation.Utilities.Constants.M;

/**
 * Created by Yamani on 3/25/18.
 */
public class CircularOrbit implements Orbit {
    private float orbitalRadius;
    private float angularVelocity;
    private double theta;

    private long startTime;

    private float speedMultiplier = 1;

    public CircularOrbit(float orbitalRadius) {
        this.orbitalRadius = orbitalRadius;
        angularVelocity = (float) Math.sqrt(G * M / Math.pow(orbitalRadius, 3));
    }



    public void updateSatellite(Satellite satellite) {
        theta = angularVelocity * (double) (TimeUtils.nanoTime() - startTime) * MathUtils.nanoToSec * speedMultiplier;

        satellite.setPosition(orbitalRadius * (float) Math.cos(theta), orbitalRadius * (float) Math.sin(theta));
    }

    public void drawPath(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.circle(0, 0, orbitalRadius, 60);
    }

    @Override
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }
}
