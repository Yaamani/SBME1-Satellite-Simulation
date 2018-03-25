package com.yaamani.satellitesimulation.SatellitesOrrbits;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Yamani on 3/25/18.
 */
public abstract class Orbit {

    protected double orbitalPeriod;
    private long startTime;
    private float speedMultiplier = 1;

    public abstract void updateSatellite(Satellite satellite);
    public abstract void drawPath(ShapeRenderer shapeRenderer);
    protected abstract void setOrbitalPeriod();

    public double getOrbitalPeriod() {
        return orbitalPeriod;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }
}
