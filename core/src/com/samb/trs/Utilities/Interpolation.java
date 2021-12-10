package com.samb.trs.Utilities;

public class Interpolation {
    public static float inverse(Inverse inverse, float y){
        float x = 0;
        switch (inverse){
            case fastSlow:
                // TODO: Fix y > 1 in KanuAnimation.java:140
                if (1 - y < 0) throw new IllegalArgumentException("NegativeSqrtException!");
                x = (float)(1-Math.sqrt(1-y));
                break;
            case slowFast:
                if (y < 0) throw new IllegalArgumentException("NegativeSqrtException!");
                x = (float) (Math.sqrt(y));
                break;
        }

        return x;
    }

    public enum Inverse {slowFast, fastSlow}

    private static Inverse map(com.badlogic.gdx.math.Interpolation interpolation){
        if(interpolation.equals(com.badlogic.gdx.math.Interpolation.slowFast)){
            return Inverse.slowFast;
        }else{
            return Inverse.fastSlow;
        }
    }

    public static float inverse(com.badlogic.gdx.math.Interpolation interpolation, float y){
        return inverse(map(interpolation), y);
    }
}
