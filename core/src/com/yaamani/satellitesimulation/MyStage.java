package com.yaamani.satellitesimulation;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Version;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.yaamani.satellitesimulation.SatellitesOrrbits.Orbit;
import com.yaamani.satellitesimulation.SatellitesOrrbits.Satellite;
import com.yaamani.satellitesimulation.UI.Button;
import com.yaamani.satellitesimulation.UI.Slider;
import com.yaamani.satellitesimulation.Utilities.Resizable;
import com.yaamani.satellitesimulation.Utilities.MyShapeRenderer;

import java.util.ArrayList;

import static com.yaamani.satellitesimulation.Utilities.Constants.*;

/**
 * Created by Yamani on 3/13/18.
 */

public class MyStage extends Stage implements Resizable {

    enum State {INDIVIDUAL, TOTAL}
    private State state = State.TOTAL;

    SatelliteSimulationGame ssg;

    private MyShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;

    private MyHohmannTimeline myHohmannTimeline;
    private MySingleOrbitTimeline mySingleOrbitTimeline;
    private MySpeedSlider mySpeedSlider;

    private Button individualTotal_btn;
    private Button leo_btn;
    private Button gto_btn;
    private Button geo_btn;

    private Button zoomIn_btn;
    private Button zoomOut_btn;
    private Button reset_btn;

    private Texture texture;

    private float speedMultiplier;

    private boolean play;

    private int fontSize = 25;


    public MyStage(ExtendViewport staticViewport, MyShapeRenderer shapeRenderer, SatelliteSimulationGame ssg) {
        super(staticViewport);
        this.shapeRenderer = shapeRenderer;

        texture = new Texture(Gdx.files.internal("badlogic.jpg"));

        spriteBatch = new SpriteBatch();

        speedMultiplier = 0;
        play = true;

        this.ssg = ssg;

        //if (Gdx.app.getType() == Application.ApplicationType.Android) fontSize = 50;
        fontSize = (int) (((float)Gdx.graphics.getHeight() / 500) * 25);

        initializeMyHohmannTimeline(ssg, this);

        initializeLeo_btn();

        initializeGto_btn();

        initializeGeo_btn();

        initializeIndividualTotal_btn();

        initializeMySingleOrbitTimeline(this);

        initializeMySpeedSlider(ssg, this);

        initializeZoomIn_btn();

        initializeZoomOut_btn();

        initializeReset_btn();
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

        mySingleOrbitTimeline.onResize();

        mySpeedSlider.onResize();
        mySpeedSlider.setPosition(getViewport().getWorldWidth() * 5/5 - getViewport().getWorldWidth() / 5/2 - mySpeedSlider.getWidth() / 2, SLIDER_LINE_Y_POS);

        individualTotal_btn.onResize();
        individualTotal_btn.setPosition(getViewport().getWorldWidth() * 1/5 - getViewport().getWorldWidth() / 5/2 - individualTotal_btn.getWidth() / 2, SLIDER_LINE_Y_POS);

        leo_btn.onResize();
        leo_btn.setPosition(getViewport().getWorldWidth() * 2/5 - getViewport().getWorldWidth() / 5/2 - individualTotal_btn.getWidth() / 2, 2*SLIDER_LINE_Y_POS);

        gto_btn.onResize();
        gto_btn.setPosition(getViewport().getWorldWidth() * 3/5 - getViewport().getWorldWidth() / 5/2 - individualTotal_btn.getWidth() / 2, 2*SLIDER_LINE_Y_POS);

        geo_btn.onResize();
        geo_btn.setPosition(getViewport().getWorldWidth() * 4/5 - getViewport().getWorldWidth() / 5/2 - individualTotal_btn.getWidth() / 2, 2*SLIDER_LINE_Y_POS);

        zoomIn_btn.onResize();
        zoomIn_btn.setPosition(SLIDER_KNOB_RADUIS * .7f,
                getViewport().getWorldHeight() /2 + SLIDER_KNOB_RADUIS * 2);

        zoomOut_btn.onResize();
        zoomOut_btn.setPosition(SLIDER_KNOB_RADUIS * .7f,
                getViewport().getWorldHeight() /2 - SLIDER_KNOB_RADUIS * 2);

        reset_btn.onResize();
        reset_btn.setPosition(getViewport().getWorldWidth() - SLIDER_KNOB_RADUIS * 3.7f,
                getViewport().getWorldHeight() /2);
    }

    @Override
    public void dispose() {
        individualTotal_btn.dispose();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }

    public SatelliteSimulationGame getSsg() {
        return ssg;
    }

    public MyHohmannTimeline getMyHohmannTimeline() {
        return myHohmannTimeline;
    }

    public Button getLeo_btn() {
        return leo_btn;
    }

    public Button getGto_btn() {
        return gto_btn;
    }

    public Button getGeo_btn() {
        return geo_btn;
    }

    public Button getIndividualTotal_btn() {
        return individualTotal_btn;
    }

    public Button getZoomIn_btn() {
        return zoomIn_btn;
    }

    public Button getZoomOut_btn() {
        return zoomOut_btn;
    }

    public Button getReset_btn() {
        return reset_btn;
    }

    // ------------- Initializers ------------

    private void initializeMyHohmannTimeline(SatelliteSimulationGame ssg, MyStage myStage) {
        myHohmannTimeline = new MyHohmannTimeline(shapeRenderer,
                MAIN_SLIDER_LINE_WIDTH,
                SLIDER_LINE_HEIGHT,
                SLIDER_KNOB_RADUIS,
                new Color(COLOR_LIGHT),
                new Color(COLOR_DARK),
                ssg.getLeo(),
                ssg.getGto(),
                ssg.getGeo(),
                myStage);

        addActor(myHohmannTimeline);
    }

    private void initializeMySingleOrbitTimeline(MyStage myStage) {
        mySingleOrbitTimeline = new MySingleOrbitTimeline(shapeRenderer,
                MAIN_SLIDER_LINE_WIDTH,
                SLIDER_LINE_HEIGHT,
                SLIDER_KNOB_RADUIS,
                new Color(COLOR_LIGHT),
                new Color(COLOR_DARK),
                myStage);

        mySingleOrbitTimeline.setControllingOrbit(ssg.getLeo());

        addActor(mySingleOrbitTimeline);
        mySingleOrbitTimeline.setVisible(false);
    }

    private void initializeMySpeedSlider(SatelliteSimulationGame ssg, MyStage myStage) {

        mySpeedSlider = new MySpeedSlider(shapeRenderer,
                SPEED_SLIDER_LINE_WIDTH,
                SLIDER_LINE_HEIGHT,
                SLIDER_KNOB_RADUIS,
                new Color(COLOR_LIGHT),
                new Color(COLOR_DARK),
                10000,
                myStage);

        addActor(mySpeedSlider);
    }

    private void initializeIndividualTotal_btn() {
        individualTotal_btn = new Button(shapeRenderer,
                "Individual",
                fontSize,
                BUTTON_WIDTH + SLIDER_KNOB_RADUIS,
                SLIDER_KNOB_RADUIS*3,
                new Color(COLOR_LIGHT),
                new Color(COLOR_DARK),
                () -> {
                    Gdx.app.log("individualTotal_btn clicked", "" + Version.VERSION);
                    if (individualTotal_btn.getText().equals("Individual")) {
                        individualTotal_btn.setText("Total");

                        myHohmannTimeline.setVisible(false);
                        leo_btn.setVisible(true);
                        gto_btn.setVisible(true);
                        geo_btn.setVisible(true);
                        mySingleOrbitTimeline.setVisible(true);

                        setState(State.INDIVIDUAL);

                    } else if (individualTotal_btn.getText().equals("Total")) {
                        individualTotal_btn.setText("Individual");

                        myHohmannTimeline.setVisible(true);
                        leo_btn.setVisible(false);
                        gto_btn.setVisible(false);
                        geo_btn.setVisible(false);
                        mySingleOrbitTimeline.setVisible(false);

                        setState(State.TOTAL);
                    }

                    setPlay(true);
                }
        );

        addActor(individualTotal_btn);

        individualTotal_btn.getFont().getData().setScale(WORLD_SIZE/getViewport().getScreenHeight());
        individualTotal_btn.getGlyphLayout().setText(individualTotal_btn.getFont(), individualTotal_btn.getText());
    }

    private void initializeLeo_btn() {
        leo_btn = new Button(shapeRenderer,
                "LEO",
                fontSize,
                BUTTON_WIDTH + SLIDER_KNOB_RADUIS,
                SLIDER_KNOB_RADUIS*3,
                new Color(COLOR_LIGHT),
                new Color(COLOR_DARK),
                () -> {
                    Gdx.app.log("LEO_BTN", "Clicked !!");
                    mySingleOrbitTimeline.setControllingOrbit(ssg.getLeo());
                }
        );

        addActor(leo_btn);
        leo_btn.setVisible(false);

        leo_btn.getFont().getData().setScale(WORLD_SIZE/getViewport().getScreenHeight());
        leo_btn.getGlyphLayout().setText(leo_btn.getFont(), leo_btn.getText());
    }

    private void initializeGto_btn() {
        gto_btn = new Button(shapeRenderer,
                "GTO",
                fontSize,
                BUTTON_WIDTH + SLIDER_KNOB_RADUIS,
                SLIDER_KNOB_RADUIS*3,
                new Color(COLOR_LIGHT),
                new Color(COLOR_DARK),
                () -> {
                    Gdx.app.log("GTO_BTN", "Clicked !!");
                    mySingleOrbitTimeline.setControllingOrbit(ssg.getGto());
                }
        );

        addActor(gto_btn);
        gto_btn.setVisible(false);

        gto_btn.getFont().getData().setScale(WORLD_SIZE/getViewport().getScreenHeight());
        gto_btn.getGlyphLayout().setText(gto_btn.getFont(), gto_btn.getText());
    }

    private void initializeGeo_btn() {
        geo_btn = new Button(shapeRenderer,
                "GEO",
                fontSize,
                BUTTON_WIDTH + SLIDER_KNOB_RADUIS,
                SLIDER_KNOB_RADUIS*3,
                new Color(COLOR_LIGHT),
                new Color(COLOR_DARK),
                () -> {
                    Gdx.app.log("GEO_BTN", "Clicked !!");
                    mySingleOrbitTimeline.setControllingOrbit(ssg.getGeo());
                }
        );

        addActor(geo_btn);
        geo_btn.setVisible(false);

        geo_btn.getFont().getData().setScale(WORLD_SIZE/getViewport().getScreenHeight());
        geo_btn.getGlyphLayout().setText(geo_btn.getFont(), geo_btn.getText());
    }

    private void initializeZoomIn_btn() {
        zoomIn_btn = new Button(shapeRenderer,
                "+",
                fontSize*2,
                SLIDER_KNOB_RADUIS*3,
                SLIDER_KNOB_RADUIS*3,
                new Color(COLOR_LIGHT),
                new Color(COLOR_DARK),
                () -> {
                    ssg.getControls().zoom(ssg.getViewport().getWorldHeight() + getViewport().getWorldHeight() * -1 * MOUSE_WHEEL_SENSITIVITY);
                }) {

            @Override
            public void drawText(SpriteBatch spriteBatch) {
                if (zoomIn_btn.isDrawText()) {
                    zoomIn_btn.getFont().draw(spriteBatch,
                            zoomIn_btn.getGlyphLayout(),
                            getParent().getX() + getX() + getWidth() / 2 - zoomIn_btn.getGlyphLayout().width / 2,
                            getParent().getY() + getY() + getHeight() / 2 + zoomIn_btn.getGlyphLayout().height / 1.65f);
                }
            }

            @Override
            public void onResize() {

            }
        };

        addActor(zoomIn_btn);
        zoomIn_btn.getFont().getData().setScale(WORLD_SIZE/getViewport().getScreenHeight());
        zoomIn_btn.getGlyphLayout().setText(zoomIn_btn.getFont(), zoomIn_btn.getText());
    }

    private void initializeZoomOut_btn() {
        zoomOut_btn = new Button(shapeRenderer,
                "-",
                fontSize*2,
                SLIDER_KNOB_RADUIS*3,
                SLIDER_KNOB_RADUIS*3,
                new Color(COLOR_LIGHT),
                new Color(COLOR_DARK),
                () -> {
                    ssg.getControls().zoom(ssg.getViewport().getWorldHeight() + getViewport().getWorldHeight() * 1 * MOUSE_WHEEL_SENSITIVITY);
                }) {

            @Override
            public void drawText(SpriteBatch spriteBatch) {
                if (zoomOut_btn.isDrawText()) {
                    zoomOut_btn.getFont().draw(spriteBatch,
                            zoomOut_btn.getGlyphLayout(),
                            getParent().getX() + getX() + getWidth() / 2 - zoomOut_btn.getGlyphLayout().width / 2,
                            getParent().getY() + getY() + getHeight() / 2 + zoomOut_btn.getGlyphLayout().height / 1.65f);
                }
            }

            @Override
            public void onResize() {

            }
        };

        addActor(zoomOut_btn);
        zoomOut_btn.getFont().getData().setScale(WORLD_SIZE/getViewport().getScreenHeight());
        zoomOut_btn.getGlyphLayout().setText(zoomOut_btn.getFont(), zoomOut_btn.getText());
    }

    private void initializeReset_btn() {
        reset_btn = new Button(shapeRenderer,
                "R",
                (int)(fontSize*1.5f),
                SLIDER_KNOB_RADUIS*3,
                SLIDER_KNOB_RADUIS*3,
                new Color(COLOR_LIGHT),
                new Color(COLOR_DARK),
                () -> {
                    ssg.getControls().reset();
                }) {

            @Override
            public void onResize() {

            }
        };

        addActor(reset_btn);
        reset_btn.getFont().getData().setScale(WORLD_SIZE/getViewport().getScreenHeight());
        reset_btn.getGlyphLayout().setText(reset_btn.getFont(), reset_btn.getText());
    }
}