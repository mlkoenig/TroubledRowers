package com.samb.trs.Controllers;

import com.badlogic.gdx.utils.Array;
import com.samb.trs.Model.Timer;

public class TimeController extends Array<Timer> {

    public Timer newTimer(){
        Timer timer = new Timer();
        add(timer);
        return timer;
    }

    public void update(float dt){
        for (Timer timer : this)
            timer.update(dt);
    }

    public void reset() {
        for (Timer timer : this) {
            timer.reset();
        }
    }
}
