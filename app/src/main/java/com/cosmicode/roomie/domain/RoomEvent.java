package com.cosmicode.roomie.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;

public class RoomEvent {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("isPrivate")
    @Expose
    private Boolean isPrivate;

    @SerializedName("startTime")
    @Expose
    private Instant startTime;

    @SerializedName("endTime")
    @Expose
    private Instant endTime;

    @SerializedName("roomId")
    @Expose
    private Long roomId;

    @SerializedName("organizerId")
    @Expose
    private Long organizerId;

    public RoomEvent() {
    }

    public RoomEvent(Long id, String title, String description, Boolean isPrivate, Instant startTime, Instant endTime, Long roomId, Long organizerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isPrivate = isPrivate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomId = roomId;
        this.organizerId = organizerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }
}
