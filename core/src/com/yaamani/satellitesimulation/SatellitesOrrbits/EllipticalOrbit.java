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

    public EllipticalOrbit(Satellite satellite, float a, float e) {
        super(satellite);

        this.a = a;
        this.e = e;

        c = (float) Math.sqrt(((G * M) / (a * a * a)));

        b = (float) (a * Math.sqrt(1 - e * e));

        //setOrbitalPeriod();
    }

    @Override
    public void updateSatellite() {
        //setCurrentTime(time);

        float t = (float) getCurrentTime();
        float E = (float) (c * t + e * Math.sin((c * t))); //This is our solution to kepler equation
        double theta = 2 * Math.atan(Math.sqrt((1 + e) / (1 - e)) * Math.tan(E / 2)) /* * getSpeedMultiplier()*/;
        float r = (float) (a * (1 - e * e) / (1 + e * Math.cos((float) theta)));


        getSatellite().setPosition((float) (r * Math.cos(theta)), (float) (r * Math.sin(theta)));
    }

    @Override
    public void drawPath(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.ellipse((-a -a*e), -b, a * 2, b * 2, 50);
    }

    @Override
    protected void setOrbitalPeriod() {
        this.orbitalPeriod = 2.0d*Math.PI*Math.sqrt((double) (a*a*a)/(G*M));
        Gdx.app.log("EllipticalOrbit", "orbitalPeriod = " + orbitalPeriod);
    }

}
