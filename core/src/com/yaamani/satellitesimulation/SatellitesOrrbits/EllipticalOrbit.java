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
public class EllipticalOrbit implements Orbit {
    private float e;
    private float a;

    private final float c;

    private float startTime;

    private float b;

    private float speedMultiplier = 1;

    public EllipticalOrbit(float a, float e) {
        this.a = a;
        this.e = e;

        c = (float) Math.sqrt(((G*M) / (a*a*a)));

        b = (float) (a*Math.sqrt(1-e*e));
    }

    @Override
    public void updateSatellite(Satellite satellite) {

        float t = (TimeUtils.nanoTime() - startTime) * MathUtils.nanoToSec;
        float E = (float) (c * t + e * Math.sin((c * t))); //This is our solution to kepler equation
        double theta = 2 * Math.atan(Math.sqrt((1+e)/(1-e)) * Math.tan(E/2)) * speedMultiplier;
        float r = (float) (a*(1-e*e) / (1+e*Math.cos((float)theta)));


        satellite.setPosition((float) (r * Math.cos(theta) + 2*a*e), (float) (r * Math.sin(theta))); //for some unknown reason i had to mave the ellipse a bit because the satellite didn't move as supposed to
    }

    @Override
    public void drawPath(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.ellipse((a*e-a), -b, a*2, b*2, 50);
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
