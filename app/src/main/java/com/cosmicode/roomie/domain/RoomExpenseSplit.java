package com.cosmicode.roomie.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoomExpenseSplit {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("amount")
    @Expose
    private Double amount;

    @SerializedName("expenseId")
    @Expose
    private Long expenseId;

    @SerializedName("roomieId")
    @Expose
    private Long roomieId;

    public RoomExpenseSplit() {
    }

    public RoomExpenseSplit(Long id, Double amount, Long expenseId, Long roomieId) {
        this.id = id;
        this.amount = amount;
        this.expenseId = expenseId;
        this.roomieId = roomieId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long roomExpenseId) {
        this.expenseId = roomExpenseId;
    }

    public Long getRoomieId() {
        return roomieId;
    }

    public void setRoomieId(Long roomieId) {
        this.roomieId = roomieId;
    }

}
