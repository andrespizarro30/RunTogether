package com.afsoftwaresolutions.runtogether.utils;

import android.graphics.Color;

import java.util.Random;

public class ColorUtils {

    public static int getRandomPastelColor() {

        Random random = new Random();

        // Generate pastel color
        int base = 127; // Base value to ensure high values for pastel colors
        int red = base + random.nextInt(128); // [127, 255]
        int green = base + random.nextInt(128); // [127, 255]
        int blue = base + random.nextInt(128); // [127, 255]

        return Color.rgb(red, green, blue);
    }

}
