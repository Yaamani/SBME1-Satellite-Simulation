package com.yaamani.satellitesimulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.sun.org.apache.xpath.internal.functions.Function;

import static com.yaamani.satellitesimulation.Constants.*;

import java.util.Vector;

/**
 * Created by Yamani on 3/20/18.
 */

public class Button extends Actor implements Disposable, Resizable{
    private MyShapeRenderer shapeRenderer;

    private SpriteBatch spriteBatch;
    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arcon/Arcon-Regular.otf"));
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    private String text;
    private GlyphLayout glyphLayout;
    private BitmapFont font;

    private Color bgColor;
    private Color textColor;

    private Texture texture;

    private float width;

    public Button(MyShapeRenderer shapeRenderer, String text, int fontSize, float width, float height, Color textColor, Color bgColor, final Clickable myClickable) {
        this.shapeRenderer = shapeRenderer;
        this.text = text;
        this.bgColor = bgColor;
        this.textColor = textColor;
        this.width = width;

        texture = new Texture(Gdx.files.internal("badlogic.jpg"));


        spriteBatch = new SpriteBatch();

        parameter.size = fontSize;
        parameter.color = textColor;
        font = generator.generateFont(parameter);
        glyphLayout = new GlyphLayout();

        setSize(width, height);
        setBounds(0, 0, width, height);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                myClickable.onClicked();
                super.clicked(event, x, y);
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        /*shapeRenderer.setColor(bgColor.r, bgColor.g, bgColor.b, .5f);
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.roundedRect(getParent().getX() + getX(),
                getParent().getY() + getY(),
                getWidth() * getScaleX(),
                getHeight() * getScaleY(),
                getHeight()/2);*/

        spriteBatch.setProjectionMatrix(getStage().getViewport().getCamera().combined);
        spriteBatch.begin();
        //spriteBatch.draw(texture, getParent().getX() + getX() + 0, getParent().getY() + getY() + 0, WORLD_SIZE/5, WORLD_SIZE/5);
        font.draw(spriteBatch,
                glyphLayout,
                getParent().getX() + getX() + getWidth()/2 - glyphLayout.width/2,
                getParent().getY() + getY() + getHeight()/2 + glyphLayout.height/2);
        spriteBatch.end();

        super.draw(batch, parentAlpha);
    }


    @Override
    public void dispose() {
        texture.dispose();
    }

    @Override
    public void onResize() {
        setWidth(getStage().getViewport().getWorldWidth() / WORLD_SIZE * width);
        //TODO: change the size of the text in parameters and generate a new font to match the change in size
    }

    public BitmapFont getFont() {
        return font;
    }

    public GlyphLayout getGlyphLayout() {
        return glyphLayout;
    }

    public String getText() {
        return text;
    }
}



interface Clickable {
    void onClicked();
}