package com.yaamani.satellitesimulation.SatellitesOrrbits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

import static com.yaamani.satellitesimulation.Utilities.Constants.G;
import static com.yaamani.satellitesimulation.Utilities.Constants.M;

/**
 * Created by Yamani on 3/25/18.
 */
public class EllipticalOrbit extends Orbit {
    private float e;
    private float a;

    private final float c;

    private float b;

    public EllipticalOrbit(float a, float e) {
        this.a = a;
        this.e = e;

        c = (float) Math.sqrt(((G * M) / (a * a * a)));

        b = (float) (a * Math.sqrt(1 - e * e));

        setOrbitalPeriod();
    }

    @Override
    public void updateSatellite(Satellite satellite) {

        float t = (TimeUtils.nanoTime() - getStartTime()) * MathUtils.nanoToSec;
        float E = (float) (c * t + e * Math.sin((c * t))); //This is our solution to kepler equation
        double theta = 2 * Math.atan(Math.sqrt((1 + e) / (1 - e)) * Math.tan(E / 2)) * getSpeedMultiplier();
        float r = (float) (a * (1 - e * e) / (1 + e * Math.cos((float) theta)));


        satellite.setPosition((float) (r * Math.cos(theta) + 2 * a * e), (float) (r * Math.sin(theta))); //for some unknown reason i had to move the ellipse a bit because the satellite didn't move as supposed to
    }

    @Override
    public void drawPath(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.ellipse((a * e - a), -b, a * 2, b * 2, 50);
    }

    @Override
    protected void setOrbitalPeriod() {
        this.orbitalPeriod = 2.0d*Math.PI*Math.sqrt((double) (a*a*a)/(G*M));
        Gdx.app.log("EllipticalOrbit", "orbitalPeriod = " + orbitalPeriod);
    }

}
