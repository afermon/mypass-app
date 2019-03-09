package com.cosmicode.mypass.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Folder {

    @SerializedName("icon")
    @Expose
    private String icon;
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
    @SerializedName("secrets")
    @Expose
    private List<Secret> secrets = null;
    @SerializedName("sharedWiths")
    @Expose
    private List<JhiAccount> sharedWiths = null;

    public Folder(String icon, Integer id, String key, String modified, String name, Integer ownerId, String ownerLogin, List<Secret> secrets, List<JhiAccount> sharedWiths) {
        this.icon = icon;
        this.id = id;
        this.key = key;
        this.modified = modified;
        this.name = name;
        this.ownerId = ownerId;
        this.ownerLogin = ownerLogin;
        this.secrets = secrets;
        this.sharedWiths = sharedWiths;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
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

    public List<Secret> getSecrets() {
        return secrets;
    }

    public void setSecrets(List<Secret> secrets) {
        this.secrets = secrets;
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
                "icon='" + icon + '\'' +
                ", id=" + id +
                ", key='" + key + '\'' +
                ", modified='" + modified + '\'' +
                ", name='" + name + '\'' +
                ", ownerId=" + ownerId +
                ", ownerLogin='" + ownerLogin + '\'' +
                '}';
    }
}
