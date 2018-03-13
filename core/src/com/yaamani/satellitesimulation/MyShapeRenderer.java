package com.yaamani.satellitesimulation;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Yamani on 3/13/18.
 */

class MyShapeRenderer extends ShapeRenderer { // part of it from   https://gamedev.stackexchange.com/questions/73103/how-do-i-render-a-rounded-rectangle-in-libgdx
    /**
     * Draws a rectangle with rounded corners of the given radius.
     */
    public void roundedRect(float x, float y, float width, float height, float radius){
        float correctedRaduis = radius;
        float smallerDimension;

        if (width > height) smallerDimension = height;
        else smallerDimension = width;

        if (radius * 2 > smallerDimension) correctedRaduis = smallerDimension / 2;
        // Central rectangle
        super.rect(x + correctedRaduis, y + correctedRaduis, width - 2*correctedRaduis, height - 2*correctedRaduis);

        // Four side rectangles, in clockwise order
        super.rect(x + correctedRaduis, y, width - 2*correctedRaduis, correctedRaduis);
        super.rect(x + width - correctedRaduis, y + correctedRaduis, correctedRaduis, height - 2*correctedRaduis);
        super.rect(x + correctedRaduis, y + height - correctedRaduis, width - 2*correctedRaduis, correctedRaduis);
        super.rect(x, y + correctedRaduis, correctedRaduis, height - 2*correctedRaduis);

        // Four arches, clockwise too
        super.arc(x + correctedRaduis, y + correctedRaduis, correctedRaduis, 180f, 90f);
        super.arc(x + width - correctedRaduis, y + correctedRaduis, correctedRaduis, 270f, 90f);
        super.arc(x + width - correctedRaduis, y + height - correctedRaduis, correctedRaduis, 0f, 90f);
        super.arc(x + correctedRaduis, y + height - correctedRaduis, correctedRaduis, 90f, 90f);
    }
}