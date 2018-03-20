package com.yaamani.satellitesimulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import static com.yaamani.satellitesimulation.Constants.*;

/**
 * Created by Yamani on 3/13/18.
 */

public class MyStage extends Stage {

    private MyShapeRenderer shapeRenderer;

    private Slider timeLine;
    private Slider speed;

    public MyStage(ExtendViewport staticViewport, MyShapeRenderer shapeRenderer) {
        super(staticViewport);
        this.shapeRenderer = shapeRenderer;

        timeLine = new Slider(shapeRenderer,
                MAIN_SLIDER_LINE_WIDTH,
                SLIDER_LINE_HEIGHT,
                SLIDER_KNOB_RADUIS,
                new Color(SLIDER_LINE_COLOR),
                new Color(SLIDER_KNOB_COLOR));

        addActor(timeLine);

        speed = new Slider(shapeRenderer,
                SPEED_SLIDER_LINE_WIDTH,
                SLIDER_LINE_HEIGHT,
                SLIDER_KNOB_RADUIS,
                new Color(SLIDER_LINE_COLOR),
                new Color(SLIDER_KNOB_COLOR));

        addActor(speed);

        //timeLine.addDivider(1f/3f);
    }

    @Override
    public void draw() {
        getRoot().draw(getBatch(), 1);

        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 1, 0, 1);
        //shapeRenderer.rect(0, 0, SLIDER_LINE_WIDTH + SLIDER_KNOB_RADUIS * 2, SLIDER_KNOB_RADUIS * 2);
    }

    @Override
    public void act() {
        super.act();
    }

    public void onResize() {
        timeLine.onResize();
        timeLine.setPosition(getViewport().getWorldWidth() / 2 - timeLine.getWidth() / 2, SLIDER_LINE_Y_POS);

        speed.onResize();
        speed.setPosition(getViewport().getWorldWidth() * 5/5 - getViewport().getWorldWidth() / 5/2 - speed.getWidth() / 2, SLIDER_LINE_Y_POS);
    }
}
