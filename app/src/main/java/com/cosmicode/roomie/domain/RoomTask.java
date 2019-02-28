package com.cosmicode.roomie.domain;

import com.cosmicode.roomie.domain.enumeration.RoomTaskState;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;

public class RoomTask {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("created")
    @Expose
    private Instant created;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("deadline")
    @Expose
    private Instant deadline;

    @SerializedName("state")
    @Expose
    private RoomTaskState state;

    @SerializedName("roomId")
    @Expose
    private Long roomId;

    public RoomTask() {
    }

    public RoomTask(Long id, Instant created, String title, String description, Instant deadline, RoomTaskState state, Long roomId) {
        this.id = id;
        this.created = created;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.state = state;
        this.roomId = roomId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
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

    public Instant getDeadline() {
        return deadline;
    }

    public void setDeadline(Instant deadline) {
        this.deadline = deadline;
    }

    public RoomTaskState getState() {
        return state;
    }

    public void setState(RoomTaskState state) {
        this.state = state;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

}
