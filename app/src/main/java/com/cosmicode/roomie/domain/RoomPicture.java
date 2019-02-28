package com.cosmicode.roomie.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoomPicture {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("isMain")
    @Expose
    private Boolean isMain;

    @SerializedName("roomId")
    @Expose
    private Long roomId;

    public RoomPicture() {
    }

    public RoomPicture(Long id, String url, Boolean isMain, Long roomId) {
        this.id = id;
        this.url = url;
        this.isMain = isMain;
        this.roomId = roomId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean isIsMain() {
        return isMain;
    }

    public void setIsMain(Boolean isMain) {
        this.isMain = isMain;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

}
