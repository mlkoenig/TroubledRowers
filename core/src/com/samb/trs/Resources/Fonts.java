package com.samb.trs.Resources;

public enum Fonts {
    DEFAULT("default-font", "OpenSans-Regular.ttf"),
    BOLD("bold", "OpenSans-ExtraBold.ttf"),
    CHLORINR("chlorinr", "CHLORINR.ttf");

    private final String name, fontName;

    Fonts(String name, String fontName){
        this.name = name;
        this.fontName = fontName;
    }

    public String getName(FontSizes s) {
        return name+"-"+s.getSize();
    }

    public String getFontName() {
        return fontName;
    }
}
