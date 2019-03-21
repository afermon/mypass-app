package com.cosmicode.mypass.domain;

import android.util.Log;

import com.cosmicode.mypass.util.EncryptionHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Secret {

    private static final String TAG = "Secret";

    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("folderId")
    @Expose
    private Long folderId;
    @SerializedName("folderName")
    @Expose
    private String folderName;
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("ownerId")
    @Expose
    private Long ownerId;
    @SerializedName("ownerLogin")
    @Expose
    private String ownerLogin;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("username")
    @Expose
    private String username;

    private String newPassword;

    public Secret(String created, Long folderId, String folderName, Long id, String modified, String name, String notes, Long ownerId, String ownerLogin, String password, String url, String username, String newPassword) {
        this.created = created;
        this.folderId = folderId;
        this.folderName = folderName;
        this.id = id;
        this.modified = modified;
        this.name = name;
        this.notes = notes;
        this.ownerId = ownerId;
        this.ownerLogin = ownerLogin;
        this.password = password;
        this.url = url;
        this.username = username;
        this.newPassword = newPassword;
    }

    public Secret(Long folderId, String name, String notes, Long ownerId, String url, String username, String newPassword) {
        this.folderId = folderId;
        this.name = name;
        this.notes = notes;
        this.ownerId = ownerId;
        this.url = url;
        this.username = username;
        this.newPassword = newPassword;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordDecrypted(String folderKey) {
        try {
            return EncryptionHelper.decrypt(folderKey, this.password);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return "";
        }
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "Secret{" +
                "created='" + created + '\'' +
                ", folderId=" + folderId +
                ", folderName='" + folderName + '\'' +
                ", id=" + id +
                ", modified='" + modified + '\'' +
                ", name='" + name + '\'' +
                ", notes='" + notes + '\'' +
                ", ownerId=" + ownerId +
                ", ownerLogin='" + ownerLogin + '\'' +
                ", password='" + password + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
