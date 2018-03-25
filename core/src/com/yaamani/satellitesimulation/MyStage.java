package com.yaamani.satellitesimulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Version;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.yaamani.satellitesimulation.UI.Button;
import com.yaamani.satellitesimulation.UI.Slider;
import com.yaamani.satellitesimulation.Utilities.Resizable;
import com.yaamani.satellitesimulation.Utilities.MyShapeRenderer;

import static com.yaamani.satellitesimulation.Utilities.Constants.*;

/**
 * Created by Yamani on 3/13/18.
 */

public class MyStage extends Stage implements Resizable {

    private MyShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;

    private MyHohmannTimeline myHohmannTimeline;
    private Slider speed;

    private Button individualTotal_btn;
    private Button leo_btn;
    private Button gto_btn;
    private Button geo_btn;

    private Texture texture;


    public MyStage(ExtendViewport staticViewport, MyShapeRenderer shapeRenderer) {
        super(staticViewport);
        this.shapeRenderer = shapeRenderer;

        texture = new Texture(Gdx.files.internal("badlogic.jpg"));

        spriteBatch = new SpriteBatch();

        myHohmannTimeline = new MyHohmannTimeline(shapeRenderer,
                MAIN_SLIDER_LINE_WIDTH,
                SLIDER_LINE_HEIGHT,
                SLIDER_KNOB_RADUIS,
                new Color(COLOR_LIGHT),
                new Color(COLOR_DARK));

        addActor(myHohmannTimeline);

        speed = new Slider(shapeRenderer,
                SPEED_SLIDER_LINE_WIDTH,
                SLIDER_LINE_HEIGHT,
                SLIDER_KNOB_RADUIS,
                new Color(COLOR_LIGHT),
                new Color(COLOR_DARK));

        addActor(speed);

        individualTotal_btn = new Button(shapeRenderer, "Individual", 25, BUTTON_WIDTH + SLIDER_KNOB_RADUIS, SLIDER_KNOB_RADUIS*2, new Color(COLOR_LIGHT), new Color(COLOR_DARK), () -> {
            Gdx.app.log("individualTotal_btn clicked", "" + Version.VERSION);
            if (individualTotal_btn.getText().equals("Individual")) {
                individualTotal_btn.setText("Total");

                myHohmannTimeline.setVisible(false);
                leo_btn.setVisible(true);
                gto_btn.setVisible(true);
                geo_btn.setVisible(true);

            } else if (individualTotal_btn.getText().equals("Total")) {
                individualTotal_btn.setText("Individual");

                myHohmannTimeline.setVisible(true);
                leo_btn.setVisible(false);
                gto_btn.setVisible(false);
                geo_btn.setVisible(false);

            }
        });

        addActor(individualTotal_btn);

        individualTotal_btn.getFont().getData().setScale(WORLD_SIZE/getViewport().getScreenHeight());
        individualTotal_btn.getGlyphLayout().setText(individualTotal_btn.getFont(), individualTotal_btn.getText());

        leo_btn = new Button(shapeRenderer, "LEO", 25, BUTTON_WIDTH + SLIDER_KNOB_RADUIS, SLIDER_KNOB_RADUIS*2, new Color(COLOR_LIGHT), new Color(COLOR_DARK), () -> {
           Gdx.app.log("LEO_BTN", "Clicked !!");
        });

        addActor(leo_btn);
        leo_btn.setVisible(false);

        leo_btn.getFont().getData().setScale(WORLD_SIZE/getViewport().getScreenHeight());
        leo_btn.getGlyphLayout().setText(leo_btn.getFont(), leo_btn.getText());

        gto_btn = new Button(shapeRenderer, "GTO", 25, BUTTON_WIDTH + SLIDER_KNOB_RADUIS, SLIDER_KNOB_RADUIS*2, new Color(COLOR_LIGHT), new Color(COLOR_DARK), () -> {
           Gdx.app.log("GTO_BTN", "Clicked !!");
        });

        addActor(gto_btn);
        gto_btn.setVisible(false);

        gto_btn.getFont().getData().setScale(WORLD_SIZE/getViewport().getScreenHeight());
        gto_btn.getGlyphLayout().setText(gto_btn.getFont(), gto_btn.getText());

        geo_btn = new Button(shapeRenderer, "GEO", 25, BUTTON_WIDTH + SLIDER_KNOB_RADUIS, SLIDER_KNOB_RADUIS*2, new Color(COLOR_LIGHT), new Color(COLOR_DARK), () -> {
           Gdx.app.log("GEO_BTN", "Clicked !!");
        });

        addActor(geo_btn);
        geo_btn.setVisible(false);

        geo_btn.getFont().getData().setScale(WORLD_SIZE/getViewport().getScreenHeight());
        geo_btn.getGlyphLayout().setText(geo_btn.getFont(), geo_btn.getText());
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
        myHohmannTimeline.onResize();
        myHohmannTimeline.setPosition(getViewport().getWorldWidth() / 2 - myHohmannTimeline.getWidth() / 2, SLIDER_LINE_Y_POS);

        speed.onResize();
        speed.setPosition(getViewport().getWorldWidth() * 5/5 - getViewport().getWorldWidth() / 5/2 - speed.getWidth() / 2, SLIDER_LINE_Y_POS);

        individualTotal_btn.onResize();
        individualTotal_btn.setPosition(getViewport().getWorldWidth() * 1/5 - getViewport().getWorldWidth() / 5/2 - individualTotal_btn.getWidth() / 2, SLIDER_LINE_Y_POS + 2*SLIDER_LINE_HEIGHT);

        leo_btn.onResize();
        leo_btn.setPosition(getViewport().getWorldWidth() * 2/5 - getViewport().getWorldWidth() / 5/2 - individualTotal_btn.getWidth() / 2, SLIDER_LINE_Y_POS + 2*SLIDER_LINE_HEIGHT);

        gto_btn.onResize();
        gto_btn.setPosition(getViewport().getWorldWidth() * 3/5 - getViewport().getWorldWidth() / 5/2 - individualTotal_btn.getWidth() / 2, SLIDER_LINE_Y_POS + 2*SLIDER_LINE_HEIGHT);

        geo_btn.onResize();
        geo_btn.setPosition(getViewport().getWorldWidth() * 4/5 - getViewport().getWorldWidth() / 5/2 - individualTotal_btn.getWidth() / 2, SLIDER_LINE_Y_POS + 2*SLIDER_LINE_HEIGHT);
    }

    @Override
    public void dispose() {
        individualTotal_btn.dispose();
    }
}