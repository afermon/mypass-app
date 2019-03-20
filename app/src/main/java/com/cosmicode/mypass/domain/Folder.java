package com.cosmicode.mypass.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Folder {

    @SerializedName("id")
    @Expose
    private Long id;
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
    private Long ownerId;
    @SerializedName("ownerLogin")
    @Expose
    private String ownerLogin;
    @SerializedName("secrets")
    @Expose
    private List<Secret> secrets = null;
    @SerializedName("sharedWiths")
    @Expose
    private List<JhiAccount> sharedWiths = null;

    public Folder(Long id, String key, String modified, String name, Long ownerId, String ownerLogin, List<Secret> secrets, List<JhiAccount> sharedWiths) {
        this.id = id;
        this.key = key;
        this.modified = modified;
        this.name = name;
        this.ownerId = ownerId;
        this.ownerLogin = ownerLogin;
        this.secrets = secrets;
        this.sharedWiths = sharedWiths;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
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
                "id=" + id +
                ", key='" + key + '\'' +
                ", modified='" + modified + '\'' +
                ", name='" + name + '\'' +
                ", ownerId=" + ownerId +
                ", ownerLogin='" + ownerLogin + '\'' +
                '}';
    }
}
