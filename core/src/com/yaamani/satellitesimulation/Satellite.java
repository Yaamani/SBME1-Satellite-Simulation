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

    // -------- Orbit --------
    private final float orbitalRadius = 2000 + 6371/*Earth's Radius*/; //KM
    private float angularVelocity;
    private double theta;

    private final double G = 6.674E-11;
    private final double M = 5.972E24; //Mass of the Earth


    public Satellite(float radius, Color color) {
        this.radius = radius;
        this.color = color;

        angularVelocity = (float) Math.sqrt(G * M / Math.pow(orbitalRadius * 1000, 3));
    }

    public void render(ShapeRenderer shapeRenderer) {
        update();

        shapeRenderer.set(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.circle(0, 0, orbitalRadius, 60);

        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(color);
        shapeRenderer.circle(position.x, position.y, radius, 25);


    }

    private final float startTime = TimeUtils.nanoTime();

    private void update() {
        theta = angularVelocity * (double) (TimeUtils.nanoTime() - startTime) * MathUtils.nanoToSec * 40/*To speed things up a bit*/;

        position.set(orbitalRadius * (float) Math.cos(theta), orbitalRadius * (float) Math.sin(theta));

//        Gdx.app.log("Satellite", "angularVelocity = " + angularVelocity + ", theta = " + theta + ", position = " + position + ", TimeUtils.millis() = " + TimeUtils.millis() + ", startTime = " + startTime);
    }
}
