package com.afsoftwaresolutions.runtogether.utils;

import static com.afsoftwaresolutions.runtogether.utils.ColorUtils.getRandomPastelColor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class CreateCustomMarker {

    public CreateCustomMarker(){

    }

    public Bitmap createCustomMarker(Context context, String initials) {
        // Define the size of the marker
        int width = 100;
        int height = 100;

        // Create a bitmap with specified width and height
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Create a canvas to draw on the bitmap
        Canvas canvas = new Canvas(bitmap);

        // Define the paint and style for the text
        Paint paint = new Paint();
        paint.setTextSize(40);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);

        // Define the background circle
        Paint circlePaint = new Paint();
        circlePaint.setColor(getRandomPastelColor());
        circlePaint.setStyle(Paint.Style.FILL);

        // Draw the circle background
        canvas.drawCircle(width / 2, height / 2, width / 2, circlePaint);

        // Draw the initials on the canvas
        Rect bounds = new Rect();
        paint.getTextBounds(initials, 0, initials.length(), bounds);
        int x = width / 2;
        int y = (height / 2) - bounds.centerY();
        canvas.drawText(initials, x, y, paint);

        return bitmap;
    }

}
