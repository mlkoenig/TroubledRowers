package com.samb.trs.Pools;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.samb.trs.Model.Sprites.SpriteBody;

public abstract class AbstractPool<T extends SpriteBody> extends Pool<T> implements Disposable {
    protected float width, height;
    protected Viewport viewport;

    public AbstractPool(int initialCap, float width, float height, Viewport viewport){
        super(initialCap);
        this.viewport = viewport;
        this.width = width;
        this.height = height;
    }

    public T obtain(float x, float y){
        T t = obtain();
        t.redefineBody(width, height);
        t.setPositionForBodyAndSprite(x, y);
        return t;
    }

    @Override
    public void dispose() {
        for(int i = 0; i < getFree(); i++){
            obtain().dispose();
        }
    }

    protected void init(int count){
        for(int i = 0; i < count; i++){
            T t = newObject();
            t.destroyBody();
            free(t);
        }
    }
}
