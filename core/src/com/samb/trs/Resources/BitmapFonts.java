package com.samb.trs.Resources;

public enum BitmapFonts {
    SCORE(Fonts.CHLORINR, FontSizes.F150),
    BOLD150(Fonts.BOLD, FontSizes.F150),
    BOLD50(Fonts.BOLD, FontSizes.F50);

    private final Fonts font;
    private final FontSizes fontSize;


    BitmapFonts(Fonts font, FontSizes fontSize) {
        this.font = font;
        this.fontSize = fontSize;
    }

    public Fonts getFont() {
        return font;
    }

    public FontSizes getFontSize() {
        return fontSize;
    }
}
