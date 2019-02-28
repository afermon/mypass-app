package com.cosmicode.roomie.domain;

import com.cosmicode.roomie.domain.enumeration.NotificationType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("body")
    @Expose
    private String body;

    @SerializedName("type")
    @Expose
    private NotificationType type;

    @SerializedName("entityId")
    @Expose
    private Long entityId;

    public Notification() {
    }

    public Notification(Long id, String title, String body, NotificationType type, Long entityId) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.type = type;
        this.entityId = entityId;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }



}
