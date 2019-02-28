package com.cosmicode.roomie.domain;

import java.time.LocalDate;
import java.util.List;

import com.cosmicode.roomie.domain.enumeration.Gender;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Roomie {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("birthDate")
    @Expose
    private LocalDate birthDate;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("gender")
    @Expose
    private Gender gender;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("biography")
    @Expose
    private String biography;
    @SerializedName("mobileDeviceID")
    @Expose
    private String mobileDeviceID;
    @SerializedName("userId")
    @Expose
    private Long userId;
    @SerializedName("stateId")
    @Expose
    private Long stateId;
    @SerializedName("addressId")
    @Expose
    private Long addressId;
    @SerializedName("configurationId")
    @Expose
    private Long configurationId;
    @SerializedName("lifestyles")
    @Expose
    private List<RoomFeature> lifestyles = null;

    public Roomie() {
    }

    public Roomie(Long id, LocalDate birthDate, String picture, Gender gender, String phone, String biography, String mobileDeviceID, Long userId, Long stateId, Long addressId, Long configurationId, List<RoomFeature> lifestyles) {
        this.id = id;
        this.birthDate = birthDate;
        this.picture = picture;
        this.gender = gender;
        this.phone = phone;
        this.biography = biography;
        this.mobileDeviceID = mobileDeviceID;
        this.userId = userId;
        this.stateId = stateId;
        this.addressId = addressId;
        this.configurationId = configurationId;
        this.lifestyles = lifestyles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobileDeviceID() {
        return mobileDeviceID;
    }

    public void setMobileDeviceID(String mobileDeviceID) {
        this.mobileDeviceID = mobileDeviceID;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(Long configurationId) {
        this.configurationId = configurationId;
    }

    public List<RoomFeature> getLifestyles() {
        return lifestyles;
    }

    public void setLifestyles(List<RoomFeature> lifestyles) {
        this.lifestyles = lifestyles;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}

