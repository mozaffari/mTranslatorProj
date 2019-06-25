package dev.mozaffari.mtranslator.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Translation {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("from")
    @Expose
    private String translateFrom;
    @SerializedName("to")
    @Expose
    private String translateTo;
    @SerializedName("orignal_text")
    @Expose
    private String orignalText;
    @SerializedName("translated_text")
    @Expose
    private String translatedText;

    private String translateFromCode;

    private String translateToCode;

    public String getTranslateFromCode() {
        return translateFromCode;
    }

    public String getTranslateToCode() {
        return translateToCode;
    }

    public void setTranslateFromCode(String translateFromCode) {
        this.translateFromCode = translateFromCode;
    }

    public void setTranslateToCode(String translateToCode) {
        this.translateToCode = translateToCode;
    }

    public String getTranslateFrom() {
        return translateFrom;
    }

    public void setTranslateFrom(String translateFrom) {
        this.translateFrom = translateFrom;
    }

    public String getTranslateTo() {
        return translateTo;
    }

    public void setTranslateTo(String translateTo) {
        this.translateTo = translateTo;
    }

    public String getOrignalText() {
        return orignalText;
    }

    public void setOrignalText(String orignalText) {
        this.orignalText = orignalText;
    }

    public String getTranslatedText() {
        return this.translatedText != null ?translatedText:"";
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
