package dev.mozaffari.mtranslator.models;

public class LanguageItem {
    private String languageName;
    private int flagImage;
    private String languageCode;

    public String getLanguageName() {
        return languageName;
    }

    public int getFlagImage() {
        return flagImage;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setFlagImage(int flagImage) {
        this.flagImage = flagImage;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public LanguageItem(String languageName, String languageCode, int flagImage) {
        this.languageName = languageName;
        this.flagImage = flagImage;
        this.languageCode = languageCode;
    }
}
