package com.cosmicode.roomie.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import com.cosmicode.roomie.domain.enumeration.RoomType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Room {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("state")
    @Expose
    private RoomieState state;
    @SerializedName("created")
    @Expose
    private Instant created;
    @SerializedName("published")
    @Expose
    private Instant published;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("rooms")
    @Expose
    private Integer rooms;
    @SerializedName("roomType")
    @Expose
    private RoomType roomType;
    @SerializedName("apoinmentsNotes")
    @Expose
    private String apoinmentsNotes;
    @SerializedName("lookingForRoomie")
    @Expose
    private Boolean lookingForRoomie;
    @SerializedName("availableFrom")
    @Expose
    private LocalDate availableFrom;
    @SerializedName("isPremium")
    @Expose
    private Boolean isPremium;
    @SerializedName("addressId")
    @Expose
    private Long addressId;
    @SerializedName("roomies")
    @Expose
    private List<Roomie> roomies = null;
    @SerializedName("features")
    @Expose
    private List<RoomFeature> features = null;
    @SerializedName("ownerId")
    @Expose
    private Long ownerId;

    public Room() {
    }

    public Room(Long id, RoomieState state, Instant created, Instant published, String title, String description, Integer rooms, RoomType roomType, String apoinmentsNotes, Boolean lookingForRoomie, LocalDate availableFrom, Boolean isPremium, Long addressId, List<Roomie> roomies, List<RoomFeature> features, Long ownerId) {
        this.id = id;
        this.state = state;
        this.created = created;
        this.published = published;
        this.title = title;
        this.description = description;
        this.rooms = rooms;
        this.roomType = roomType;
        this.apoinmentsNotes = apoinmentsNotes;
        this.lookingForRoomie = lookingForRoomie;
        this.availableFrom = availableFrom;
        this.isPremium = isPremium;
        this.addressId = addressId;
        this.roomies = roomies;
        this.features = features;
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoomieState getState() {
        return state;
    }

    public void setState(RoomieState state) {
        this.state = state;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getPublished() {
        return published;
    }

    public void setPublished(Instant published) {
        this.published = published;
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

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public String getApoinmentsNotes() {
        return apoinmentsNotes;
    }

    public void setApoinmentsNotes(String apoinmentsNotes) {
        this.apoinmentsNotes = apoinmentsNotes;
    }

    public Boolean getLookingForRoomie() {
        return lookingForRoomie;
    }

    public void setLookingForRoomie(Boolean lookingForRoomie) {
        this.lookingForRoomie = lookingForRoomie;
    }

    public LocalDate getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(LocalDate availableFrom) {
        this.availableFrom = availableFrom;
    }

    public Boolean getPremium() {
        return isPremium;
    }

    public void setPremium(Boolean premium) {
        isPremium = premium;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public List<Roomie> getRoomies() {
        return roomies;
    }

    public void setRoomies(List<Roomie> roomies) {
        this.roomies = roomies;
    }

    public List<RoomFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<RoomFeature> features) {
        this.features = features;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}