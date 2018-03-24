package com.yaamani.satellitesimulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Version;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

public class MyStage extends Stage implements Resizable{

    private MyShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;

    private Slider timeLine;
    private Slider speed;

    private Button button;

    private Texture texture;


    public MyStage(ExtendViewport staticViewport, MyShapeRenderer shapeRenderer) {
        super(staticViewport);
        this.shapeRenderer = shapeRenderer;

        texture = new Texture(Gdx.files.internal("badlogic.jpg"));

        spriteBatch = new SpriteBatch();

        timeLine = new Slider(shapeRenderer,
                MAIN_SLIDER_LINE_WIDTH,
                SLIDER_LINE_HEIGHT,
                SLIDER_KNOB_RADUIS,
                new Color(COLOR_LIGHT),
                new Color(COLOR_DARK));

        addActor(timeLine);

        speed = new Slider(shapeRenderer,
                SPEED_SLIDER_LINE_WIDTH,
                SLIDER_LINE_HEIGHT,
                SLIDER_KNOB_RADUIS,
                new Color(COLOR_LIGHT),
                new Color(COLOR_DARK));

        addActor(speed);

        //timeLine.addDivider(1f/3f);

        button = new Button(shapeRenderer, "Individual", 25, BUTTON_WIDTH + SLIDER_KNOB_RADUIS, SLIDER_KNOB_RADUIS*2, new Color(COLOR_LIGHT), new Color(COLOR_DARK), () -> {
            Gdx.app.log("button clicked", "" + Version.VERSION);
            button.getGlyphLayout().setText(button.getFont(), "");
        });

        addActor(button);

        button.getFont().getData().setScale(WORLD_SIZE/getViewport().getScreenHeight());
        button.getGlyphLayout().setText(button.getFont(), button.getText());
    }

    @Override
    public void draw() {

        getViewport().getCamera().update();
        spriteBatch.setProjectionMatrix(getViewport().getCamera().combined);
        spriteBatch.begin();
        //spriteBatch.draw(texture, 0, 0, WORLD_SIZE/10, WORLD_SIZE/10);

        getRoot().draw(spriteBatch, 1);
        //Gdx.app.log("MyStage", "spriteBatch = " + spriteBatch);
        spriteBatch.end();

        /*shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 1, 0, 1);
        shapeRenderer.rect(0, 0, MAIN_SLIDER_LINE_WIDTH + SLIDER_KNOB_RADUIS * 2, SLIDER_KNOB_RADUIS * 2);*/
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

        button.onResize();
        button.setPosition(getViewport().getWorldWidth() * 1/5 - getViewport().getWorldWidth() / 5/2 - button.getWidth() / 2, SLIDER_LINE_Y_POS + 2*SLIDER_LINE_HEIGHT);
    }

    @Override
    public void dispose() {
        button.dispose();
    }
}

interface Resizable {
    void onResize();
}
