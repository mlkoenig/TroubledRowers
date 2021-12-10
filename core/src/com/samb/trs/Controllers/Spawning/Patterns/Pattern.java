package com.samb.trs.Controllers.Spawning.Patterns;

import com.badlogic.gdx.utils.Array;

public class Pattern {
    public enum Type {Rock, Coin, Boost, Trunk, BasicFish}

    private int index;
    private Type type;

    public Pattern(int index, Type type){
        this.index = index;
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public Type getType(){
        return type;
    }

    public static Array<Pattern> createPattern(int[] indizes, Type[] types){
        if(indizes.length == types.length) {
            Array<Pattern> res = new Array<>();
            for (int i = 0; i < indizes.length; i++) {
                res.add(new Pattern(indizes[i], types[i]));
            }

            return res;
        }

        throw new IllegalArgumentException();
    }
}
