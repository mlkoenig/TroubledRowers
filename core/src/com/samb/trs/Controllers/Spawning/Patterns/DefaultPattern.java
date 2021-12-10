package com.samb.trs.Controllers.Spawning.Patterns;

import com.badlogic.gdx.utils.Array;

public class DefaultPattern extends Pattern {
    public DefaultPattern(int i){
        super(i, Type.Rock);
    }

    public static Array<Pattern> createDefaultPattern(int[] arr){
        Array<Pattern> res = new Array<>();
        for(int i = 0; i < arr.length; i++){
            res.add(new DefaultPattern(arr[i]));
        }

        return res;
    }
}
