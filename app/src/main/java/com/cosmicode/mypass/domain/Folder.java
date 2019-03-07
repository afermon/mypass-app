package com.cosmicode.mypass.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Folder {

    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("icon")
    @Expose
    private Long icon;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ownerId")
    @Expose
    private Integer ownerId;
    @SerializedName("ownerLogin")
    @Expose
    private String ownerLogin;
    @SerializedName("sharedWiths")
    @Expose
    private List<JhiAccount> sharedWiths = null;

    public Folder(String created, Long icon, Integer id, String key, String modified, String name, Integer ownerId, String ownerLogin, List<JhiAccount> sharedWiths) {
        this.created = created;
        this.icon = icon;
        this.id = id;
        this.key = key;
        this.modified = modified;
        this.name = name;
        this.ownerId = ownerId;
        this.ownerLogin = ownerLogin;
        this.sharedWiths = sharedWiths;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Long getIcon() {
        return icon;
    }

    public void setIcon(Long icon) {
        this.icon = icon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public List<JhiAccount> getSharedWiths() {
        return sharedWiths;
    }

    public void setSharedWiths(List<JhiAccount> sharedWiths) {
        this.sharedWiths = sharedWiths;
    }

    @Override
    public String toString() {
        return "Folder{" +
                "created='" + created + '\'' +
                ", icon=" + icon +
                ", id=" + id +
                ", key='" + key + '\'' +
                ", modified='" + modified + '\'' +
                ", name='" + name + '\'' +
                ", ownerId=" + ownerId +
                ", ownerLogin='" + ownerLogin + '\'' +
                ", sharedWiths=" + sharedWiths.toString() +
                '}';
    }
}
