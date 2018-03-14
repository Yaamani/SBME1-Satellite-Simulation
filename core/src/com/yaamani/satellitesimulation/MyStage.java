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

    public MyStage(ExtendViewport staticViewport, MyShapeRenderer shapeRenderer) {
        super(staticViewport);
        this.shapeRenderer = shapeRenderer;

        timeLine = new Slider(shapeRenderer, SLIDER_LINE_WIDTH, SLIDER_LINE_HEIGHT, SLIDER_KNOB_RADUIS, new Color(SLIDER_LINE_COLOR), new Color(SLIDER_KNOB_COLOR));
        Gdx.app.log("Stage", "timeLine.getWidth() = " + timeLine.getWidth());

        addActor(timeLine);

        timeLine.setBounds(0, 0, timeLine.getWidth(), timeLine.getHeight());

        timeLine.setPosition(getViewport().getWorldWidth() / 2 - SLIDER_LINE_WIDTH / 2, SLIDER_LINE_Y_POS);

        timeLine.addListener(new ClickListener() {
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                float shiftedX = x - timeLine.getKnob().getRadius();

                Gdx.app.log("timeline touchDragged", "shiftedX = " + shiftedX + ", timeLine.getLine().getWidth() = " + timeLine.getLine().getWidth());

                if (shiftedX > timeLine.getLine().getWidth()) {
                    timeLine.setPercentage(1);
                    super.touchDragged(event, x, y, pointer);
                } else if (shiftedX < 0) {
                    timeLine.setPercentage(0);
                    super.touchDragged(event, x, y, pointer);
                } else {
                    timeLine.setPercentage(shiftedX / timeLine.getLine().getWidth());
                    super.touchDragged(event, x, y, pointer);
                }
            }
        });

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
    }
}
