package com.samb.trs.Resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class Constants {

    // GENERAL CONSTANTS
    public static class General {
        public static final boolean DEBUGGING = true;
    }

    public static class Camera {
        // CAMERA CONSTANTS
        public static final float VELOCITY_INCREASE_TIMER = 20;
        public static final float VELOCITY_INCREASE = 2;
        public static final float START_VELOCITY = 8;
        public static final float MAX_VELOCITY = 20;
    }

    public static class Spawning {

        // SPAWN CONSTANTS
        public static final float SPAWN_RATE = 2.3f * Camera.START_VELOCITY;
        public static final int QUADRANT_DEPTH_HORIZONTAL = 3;
        public static final int QUADRANT_DEPTH_VERTICAL = 5;
        public static final float COIN_PROBABILITY = 0.4f;
        public static final float FISH_PROBABILITY = 1f;
        public static final float BOOST_PROBABILITY = 0.3f;
        public static final float TRUNK_PROBABILITY = 0.6f;
        public static final float COIN_SPAWN_TIMER = 7f * Camera.START_VELOCITY;
        public static final float BOOST_SPAWN_TIMER = 12f * Camera.START_VELOCITY;
        public static final float FISH_SPAWN_TIMER = 5f * Camera.START_VELOCITY;
        public static final float TRUNK_SPAWN_TIMER = 4f * Camera.START_VELOCITY;
    }

    public static class Collision {

        // CONTACT CONSTANTS
        public static final float BOOST_PROBABILITY = 0.4f;
        public static final short STAGE_BIT = 2;
        public static final short BOAT_BIT = 4;
        public static final short ROCK_BIT = 8;
        public static final short COIN_BIT = 16;
        public static final short SHIELD_BIT = 32;
        public static final short QUADRANT_BIT = 64;
        public static final short FISH_BIT = 128;
        public static final short BOOST_BIT = 256;
        public static final short TRUNK_BIT = 512;
        public static final short PADDEL_BIT = 1024;
        private static final float VOLUME = 0.5f;
    }

    public static class Rendering {

        // RENDER CONSTANTS
        public static int WorldWidth = 1440;
        public static int WorldHeight = calculateHeight(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        public static int CREATION_HEIGHT = WorldHeight + 4000;
        public static float PPM = 100;
        public static float PPM_INV = 0.01f;
        public static float TIMESTEP = 1 / 60f;

        public static int calculateHeight(int width, int height) {
            return ((int) (height / ((float) width) * WorldWidth));
        }
    }

    public static class Fonts {
        public static final Color DEFAULT_COLOR = Color.WHITE;
    }

}
