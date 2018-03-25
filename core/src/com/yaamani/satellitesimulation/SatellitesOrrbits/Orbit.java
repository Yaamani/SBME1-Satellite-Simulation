package com.yaamani.satellitesimulation.SatellitesOrrbits;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Yamani on 3/25/18.
 */
public interface Orbit {
    void updateSatellite(Satellite satellite);
    void drawPath(ShapeRenderer shapeRenderer);
    void setStartTime(long startTime);
    void setSpeedMultiplier(float speedMultiplier);
}
