package com.yaamani.satellitesimulation.SatellitesOrrbits;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Yamani on 3/25/18.
 */
public abstract class Orbit {

    private Satellite satellite;
    protected double orbitalPeriod;
    private long startTime;
    private double currentTime;

    public Orbit(Satellite satellite) {
        this.satellite = satellite;
    }

    public abstract void updateSatellite();
    public abstract void drawPath(ShapeRenderer shapeRenderer);
    protected abstract void setOrbitalPeriod();

    public double getOrbitalPeriod() {
        if (orbitalPeriod == 0) setOrbitalPeriod();
        return orbitalPeriod;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(double currentTime) {
        this.currentTime = currentTime;
    }

    public Satellite getSatellite() {
        return satellite;
    }

    public void setSatellite(Satellite satellite) {
        this.satellite = satellite;
    }
}
