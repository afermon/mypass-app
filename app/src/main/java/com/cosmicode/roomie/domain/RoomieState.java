package com.cosmicode.roomie.domain;

import com.cosmicode.roomie.domain.enumeration.AccountState;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class RoomieState {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("state")
    @Expose
    private AccountState state;

    @SerializedName("suspendedDate")
    @Expose
    private LocalDate suspendedDate;

    public RoomieState() {
    }

    public RoomieState(Long id, AccountState state, LocalDate suspendedDate) {
        this.id = id;
        this.state = state;
        this.suspendedDate = suspendedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountState getState() {
        return state;
    }

    public void setState(AccountState state) {
        this.state = state;
    }

    public LocalDate getSuspendedDate() {
        return suspendedDate;
    }

    public void setSuspendedDate(LocalDate suspendedDate) {
        this.suspendedDate = suspendedDate;
    }



}
