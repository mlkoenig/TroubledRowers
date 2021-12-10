package com.samb.trs.Resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Saves {
    public static final FileHandle RSA = Gdx.files.local("rsa.ser");
    public static final FileHandle RSA_SAVE = Gdx.files.local("rsa_save.ser");
    public static final FileHandle ACCOUNT = Gdx.files.local("Saves/data.ser");
    public static final String GAMESTATE = "GAMESTATE";
}
