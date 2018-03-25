package com.yaamani.satellitesimulation.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.yaamani.satellitesimulation.Utilities.MyShapeRenderer;
import com.yaamani.satellitesimulation.Utilities.Resizable;

import java.util.ArrayList;

import static com.yaamani.satellitesimulation.Utilities.Constants.*;
/**
 * Created by Yamani on 3/13/18.
 */

public class Slider extends Group implements Resizable {

    private MyShapeRenderer shapeRenderer;

    private Line line;
    private Knob knob;
    private ArrayList<Divider> dividers;

    private float lineWidth;

    private float percentage = 0; // 1 = 100%, 0 = 0%

    public Slider(MyShapeRenderer shapeRenderer,
                  final float lineWidth,
                  float lineHeight,
                  float knobRaduis,
                  Color lineColor,
                  Color knobColor) {
        this.shapeRenderer = shapeRenderer;
        this.lineWidth = lineWidth;

        setSize(lineWidth + knobRaduis * 2, knobRaduis * 2);

        line = new Line(lineWidth, lineHeight, lineColor);
        addActor(line);
        line.setBounds(0, 0, line.getWidth(), line.getHeight());
        line.setPosition(SLIDER_KNOB_RADUIS, SLIDER_KNOB_RADUIS);


        knob = new Knob(knobRaduis, knobColor);
        addActor(knob);
        knob.setBounds(0, 0, knob.getWidth(), knob.getHeight());
        knob.setPosition(0, 0);

        setBounds(0, 0, getWidth(), getHeight());

        addListener(new ClickListener() {
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                float shiftedX = x - getKnob().getRadius();

                if (shiftedX > getLine().getWidth()) {
                    setPercentage(1);
                    //super.touchDragged(event, x, y, pointer);
                } else if (shiftedX < 0) {
                    setPercentage(0);
                    //super.touchDragged(event, x, y, pointer);
                } else {
                    setPercentage(shiftedX / getLine().getWidth());
                    //super.touchDragged(event, x, y, pointer);
                }

                onDragged();
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                float shiftedX = x - getKnob().getRadius();

                if (shiftedX > getLine().getWidth()) {
                    setPercentage(1);
                } else if (shiftedX < 0) {
                    setPercentage(0);
                    //return super.touchDown(event, x, y, pointer, button);
                } else {
                    setPercentage(shiftedX / getLine().getWidth());
                    //return super.touchDown(event, x, y, pointer, button);
                }

                onDown();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        /*knob.addListener(new ClickListener() {
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
            }
        });*/
        /*knob.addListener(new ActorGestureListener() {
            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                percentage = x / (lineWidth - SLIDER_KNOB_RADUIS - getX());
                Gdx.app.log("Knob Pan", "Knob Pan ..... , getX() = " + getX() + ", x = " + x);
                super.pan(event, x, y, deltaX, deltaY);
            }
        });*/

    }

    protected void onDragged() {

    }

    protected void onDown() {

    }

    public void onResize() {
        line.setWidth(getStage().getViewport().getWorldWidth() / WORLD_SIZE * lineWidth);

        setSize(line.getWidth() + knob.radius * 2, knob.radius * 2);
    }

    public void addDivider(float dividingPercentage) {
        if (dividers == null) dividers = new ArrayList<Divider>();

        Divider divider = new Divider(dividingPercentage);
        dividers.add(divider);
        addActor(divider);
        divider.setZIndex(1);
        divider.setSize(SLIDER_DIVIDER_WIDTH, SLIDER_DIVIDER_HEIGHT);
        divider.setBounds(0, 0, divider.getWidth(), divider.getHeight());
        divider.setPosition(line.getWidth() * dividingPercentage - divider.getWidth() / 2, getHeight() / 2 - divider.getHeight() / 2);
        divider.addTouchArea();

        /*divider.touchArea.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Divider", "Clicked");
                super.clicked(event, x, y);
            }
        });*/
    }

    public Line getLine() {
        return line;
    }

    public Knob getKnob() {
        return knob;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setColor(1, 1, 1, .3f);
        //shapeRenderer.roundedRect(getX(), getY() + SLIDER_LINE_HEIGHT/2, getWidth(), getHeight(), getHeight()/2);

        Gdx.gl.glDisable(GL20.GL_BLEND);

        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }



    public class Line extends Actor {
        private Color lineColor;

        private Line(float width, float height, Color lineColor) {
            setSize(width, height);
            this.lineColor = lineColor;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            shapeRenderer.setColor(lineColor);
            shapeRenderer.roundedRect(getParent().getX() + getX(),
                    getParent().getY() + getY(),
                    getWidth() * getScaleX(),
                    getHeight() * getScaleY(),
                    getHeight() / 2);
        }
    }




    public class Knob extends Actor {

        private Color knobColor;

        private float radius;

        private Knob(float radius, Color knobColor) {
            this.radius = radius;
            this.knobColor = knobColor;
            setSize(radius * 2, radius * 2);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            shapeRenderer.setColor(knobColor);
            //shapeRenderer.setColor(1, 0, 0, .5f);
            shapeRenderer.circle(getParent().getX() + getX() + radius,
                    getParent().getY() + getY() + radius,
                    radius);
        }

        @Override
        public void act(float delta) {
            super.act(delta);

            setPosition(percentage * line.getWidth(), line.getHeight() / 2);
        }

        public float getRadius() {
            return radius;
        }
    }




    public class Divider extends Actor {

        private float dividingPercentage;
        private Color color;
        private Actor touchArea;

        public Divider(float dividingPercentage) {
            this.dividingPercentage = dividingPercentage;
            color = new Color(COLOR_LIGHT);
        }

        private void addTouchArea() {
            touchArea = new Actor();
            getParent().addActor(touchArea);
            touchArea.setZIndex(1);
            touchArea.setSize(line.getWidth() / 20, getHeight());
            touchArea.setBounds(0, 0, touchArea.getWidth(), touchArea.getHeight());
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            shapeRenderer.setColor(color);
            /*shapeRenderer.setColor(0, 1, 0, .5f);
            shapeRenderer.rect(touchArea.getX() + getParent().getX(), touchArea.getY() + getParent().getY(), touchArea.getWidth(), touchArea.getHeight());*/
            shapeRenderer.roundedRect(getParent().getX() + getX(),
                    getParent().getY() + getY(),
                    getWidth() * getScaleX(),
                    getHeight() * getScaleY(),
                    getWidth() / 2);
        }

        @Override
        public void act(float delta) {
            super.act(delta);

            setPosition(dividingPercentage * line.getWidth() + knob.getWidth() / 2 - getWidth() / 2,
                    getParent().getHeight() / 2 - getHeight() / 2 + line.getHeight() / 2);

            touchArea.setPosition(getX() + getWidth() / 2 - touchArea.getWidth() / 2, getY());
        }
    }
}
