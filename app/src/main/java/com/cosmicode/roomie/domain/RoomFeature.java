package com.cosmicode.roomie.domain;

import com.cosmicode.roomie.domain.enumeration.FeatureType;
import com.cosmicode.roomie.domain.enumeration.Lang;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoomFeature {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("lang")
    @Expose
    private Lang lang;

    @SerializedName("type")
    @Expose
    private FeatureType type;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("desciription")
    @Expose
    private String desciription;

    public RoomFeature() {
    }

    public RoomFeature(Long id, Lang lang, FeatureType type, String name, String icon, String desciription) {
        this.id = id;
        this.lang = lang;
        this.type = type;
        this.name = name;
        this.icon = icon;
        this.desciription = desciription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public FeatureType getType() {
        return type;
    }

    public void setType(FeatureType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDesciription() {
        return desciription;
    }

    public void setDesciription(String desciription) {
        this.desciription = desciription;
    }

}
