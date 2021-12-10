package com.samb.trs.Controllers.Spawning.Patterns;

import com.badlogic.gdx.utils.Array;
import com.samb.trs.Controllers.Spawning.Patterns.Pattern.Type;

public class Patterns {
    private int size;
    private Array<Array<Pattern>> defs;
    private Array<Array<Pattern>> boosts;
    private Array<Array<Pattern>> fish;
    private Array<Array<Pattern>> coins;
    private Array<Array<Pattern>> trunks;

    public Patterns(){
        defs = new Array<>();
        boosts = new Array<>();
        coins = new Array<>();
        fish = new Array<>();
        trunks = new Array<>();

        defineDefaultPatterns();
        defineBoostPatterns();
        defineCoinPatterns();
        defineFishPatterns();
        defineTrunksPatterns();

        size = defs.size+ boosts.size+fish.size+coins.size+trunks.size;
    }

    private void defineDefaultPatterns(){
        add(new int[]{0, 1, 10, 14}, defs);
        add(new int[]{1, 2, 10, 12}, defs);
        add(new int[]{0, 4, 14}, defs);
        add(new int[]{2, 4, 12}, defs);
        add(new int[]{2, 3, 13}, defs);
        add(new int[]{0, 5, 13}, defs);
        add(new int[]{0, 1, 8}, defs);
        add(new int[]{1, 2, 6}, defs);
    }

    private void defineCoinPatterns(){
        add(new int[]{1, 2, 7, 4}, new Type[]{Type.Rock, Type.Rock, Pattern.Type.Rock, Type.Coin}, coins);
        add(new int[]{0, 1, 3, 9, 11}, new Type[]{Type.Rock, Type.Rock, Type.Coin, Type.Rock, Type.Rock}, coins);
        add(new int[]{0, 1, 3, 9, 11}, new Type[]{Type.Rock, Type.Rock, Type.Coin, Type.Rock, Type.Rock}, coins);
    }

    private void defineBoostPatterns(){
        add(new int[]{1, 2, 7, 4}, new Type[]{Type.Rock, Type.Rock, Pattern.Type.Rock, Type.Boost}, boosts);
        add(new int[]{0, 1, 3, 9, 11}, new Type[]{Type.Rock, Type.Rock, Type.Boost, Type.Rock, Type.Rock}, boosts);
        add(new int[]{0, 1, 3, 9, 11}, new Type[]{Type.Rock, Type.Rock, Type.Boost, Type.Rock, Type.Rock}, boosts);
    }

    private void defineFishPatterns(){
        add(new int[]{0, 1, 6}, new Type[]{Type.Rock, Type.Rock, Type.BasicFish}, fish);
        add(new int[]{1, 2, 4}, new Type[]{Type.Rock, Type.Rock, Type.BasicFish}, fish);
        add(new int[]{1, 2, 8, 10, 12}, new Type[]{Type.Rock, Type.Rock, Type.BasicFish, Type.Rock, Type.Rock}, fish);
        add(new int[]{0, 2, 4, 10, 12}, new Type[]{Type.Rock, Type.Rock, Type.BasicFish, Type.Rock, Type.Rock}, fish);
    }

    private void defineTrunksPatterns(){
        add(new int[]{0, 1, 2, 6}, new Type[]{Type.Rock, Type.Trunk, Type.Rock, Type.Rock}, trunks);
        add(new int[]{0, 1, 2, 4}, new Type[]{Type.Trunk, Type.Rock, Type.Rock, Type.Rock}, trunks);
        add(new int[]{1, 2, 4, 8}, new Type[]{Type.Rock, Type.Trunk, Type.Rock, Type.BasicFish}, trunks);
        add(new int[]{0, 1, 5}, new Type[]{Type.Trunk, Type.Rock, Type.Rock}, trunks);
    }

    public void add(int[] arr, Array<Array<Pattern>> list){
        list.add(DefaultPattern.createDefaultPattern(arr));
    }
    public void add(int[] arr, Pattern.Type[] types, Array<Array<Pattern>> list){
        list.add(Pattern.createPattern(arr, types));
    }

    public Array<Pattern> getRandom(Type type){
        switch (type){
            case Rock:
                return defs.random();
            case Coin:
                return coins.random();
            case Boost:
                return boosts.random();
            case BasicFish:
                return fish.random();
            case Trunk:
                return trunks.random();
        }

        return defs.random();
    }
}
