package com.cosmicode.roomie.domain;

import com.cosmicode.roomie.domain.enumeration.CurrencyType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPreferences {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("currency")
    @Expose
    private CurrencyType currency;

    @SerializedName("todoListNotifications")
    @Expose
    private Boolean todoListNotifications;

    @SerializedName("calendarNotifications")
    @Expose
    private Boolean calendarNotifications;

    @SerializedName("paymentsNotifications")
    @Expose
    private Boolean paymentsNotifications;

    @SerializedName("appointmentsNotifications")
    @Expose
    private Boolean appointmentsNotifications;

    public UserPreferences() {
    }

    public UserPreferences(Long id, CurrencyType currency, Boolean todoListNotifications, Boolean calendarNotifications, Boolean paymentsNotifications, Boolean appointmentsNotifications) {
        this.id = id;
        this.currency = currency;
        this.todoListNotifications = todoListNotifications;
        this.calendarNotifications = calendarNotifications;
        this.paymentsNotifications = paymentsNotifications;
        this.appointmentsNotifications = appointmentsNotifications;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CurrencyType getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }

    public Boolean isTodoListNotifications() {
        return todoListNotifications;
    }

    public void setTodoListNotifications(Boolean todoListNotifications) {
        this.todoListNotifications = todoListNotifications;
    }

    public Boolean isCalendarNotifications() {
        return calendarNotifications;
    }

    public void setCalendarNotifications(Boolean calendarNotifications) {
        this.calendarNotifications = calendarNotifications;
    }

    public Boolean isPaymentsNotifications() {
        return paymentsNotifications;
    }

    public void setPaymentsNotifications(Boolean paymentsNotifications) {
        this.paymentsNotifications = paymentsNotifications;
    }

    public Boolean isAppointmentsNotifications() {
        return appointmentsNotifications;
    }

    public void setAppointmentsNotifications(Boolean appointmentsNotifications) {
        this.appointmentsNotifications = appointmentsNotifications;
    }
}
