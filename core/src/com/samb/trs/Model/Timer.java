package com.samb.trs.Model;

public class Timer {
    private float timer;
    private float stop;
    private boolean canStop;

    public Timer(){
        this(0);
    }

    public Timer(float timer){
        this.timer = timer;
        this.stop = timer;
        canStop = true;
    }

    /**
     * Sets the timer to zero.
     */
    public void reset(){
        set(0);
    }

    /**
     * Updates the timer with the current dt and runs runnables.
     * @param dt
     */
    public void update(float dt) {
        timer += dt;
    }

    /**
     * Creates a stop value of the current time.
     */
    public void stop(){
        if(canStop){
            this.stop = timer;
            canStop = false;
        }
    }

    /**
     * @return The current time.
     */
    public float getValue(){
        return timer;
    }

    /**
     * Returns the time distance between the current time and the time {@link #stop()} was last called.
     * @return {@link #timer} - {@link #stop}
     */
    public float timeSinceStop(){
        return timer - stop;
    }

    /**
     * Checks if the timer passed {@param time} periodically.
     * @param s - the time in seconds to check
     * @return
     */
    public boolean passed(float s){
        stop();
        boolean b = timeSinceStop() >= s;
        if(b) canStop = true;
        return b;
    }

    public boolean passedAbsolute(float s){
        return getValue() >= s;
    }

    /**
     * Sets the current timer.
     * @param timer
     */
    public void set(float timer){
        this.timer = timer;
        this.stop = timer;
        canStop = true;
    }

    /**
     * Returns the distance between two timers.
     * @param other
     * @return
     */
    public float timeDistance(Timer other){
        return Math.abs(timer - other.timer);
    }
}
