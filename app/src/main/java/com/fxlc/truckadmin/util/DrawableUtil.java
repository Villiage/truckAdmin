package com.fxlc.truckadmin.util;

import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

/**
 * Created by cyd on 2017/4/19.
 */

public class DrawableUtil {
    public static StateListDrawable createSelecotorDrawable(int radius) {

        StateListDrawable stateDrawable = new StateListDrawable();
        ShapeDrawable selectedShapeDrawable =   createShapeDrawable(radius, Color.GRAY);


        ShapeDrawable unSelectedShapeDrawable = createShapeDrawable(radius, Color.WHITE);

        stateDrawable.addState(new int[] { android.R.attr.state_pressed },
                selectedShapeDrawable);
        stateDrawable.addState(new int[] { -android.R.attr.state_pressed },
                unSelectedShapeDrawable);

        return stateDrawable;

    }

    public static ShapeDrawable createShapeDrawable(
            float radius, int color) {
        float[] outerRadii = new float[8];
        float[] innerRadii = new float[8];

        for (int i = 0; i < 8; i++) {
            outerRadii[i] = radius;
            innerRadii[i] = radius;
        }

        ShapeDrawable sd = new ShapeDrawable(new RoundRectShape(outerRadii,
                new RectF(), innerRadii));

         sd.getPaint().setColor(color);

        return sd;
    }
}
