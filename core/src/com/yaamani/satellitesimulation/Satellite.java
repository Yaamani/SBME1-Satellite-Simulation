package com.yaamani.satellitesimulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import static com.yaamani.satellitesimulation.Constants.*;

/**
 * Created by mahmo on 03/03/2018.
 */

public class Satellite {
    private Vector2 position = new Vector2();
    private float radius;
    private Color color;

    private Orbit currentOrbit;

    public Satellite(float radius, Color color) {
        this.radius = radius;
        this.color = color;
    }

    public void render(ShapeRenderer shapeRenderer) {
        update();

        shapeRenderer.set(ShapeRenderer.ShapeType.Line);

        if (currentOrbit != null) currentOrbit.drawPath(shapeRenderer);

        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(color);
        shapeRenderer.circle(position.x, position.y, radius, 25);


    }

    private void update() {
        if (currentOrbit != null) currentOrbit.updateSatellite(this);
    }

    public void setOrbit(Orbit orbit) {
        orbit.setStartTime(TimeUtils.nanoTime());
        this.currentOrbit = orbit;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }
}

interface Orbit {
    void updateSatellite(Satellite satellite);
    void drawPath(ShapeRenderer shapeRenderer);
    void setStartTime(long startTime);
}






class CircularOrbit implements Orbit {
    private float orbitalRadius;
    private float angularVelocity;
    private double theta;

    private long startTime;

    public CircularOrbit(float orbitalRadius) {
        this.orbitalRadius = orbitalRadius;
        angularVelocity = (float) Math.sqrt(G * M / Math.pow(orbitalRadius, 3));
    }



    public void updateSatellite(Satellite satellite) {
        theta = angularVelocity * (double) (TimeUtils.nanoTime() - startTime) * MathUtils.nanoToSec;

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
}


class EllipticalOrbit implements Orbit {
    private float e;
    private float a;

    private final double c;

    private float startTime;

    public EllipticalOrbit(float a, float e) {
        this.a = a;
        this.e = e;

        c = Math.sqrt(((G*M) / (a*a*a)));
    }

    @Override
    public void updateSatellite(Satellite satellite) {

        float t = (TimeUtils.nanoTime() - startTime) * MathUtils.nanoToSec;
        float E = (float) (c * t + e * Math.sin((c * t)));
        double theta = 2 * Math.atan(Math.sqrt((1+e)/(1-e)) * Math.tan(E/2));
        float r = (float) (a*(1-e*e) / (1+e*Math.cos((float)theta)));

        satellite.setPosition((float) (r * Math.cos(theta)), (float) (r * Math.sin(theta)));
    }

    @Override
    public void drawPath(ShapeRenderer shapeRenderer) {

    }

    @Override
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
