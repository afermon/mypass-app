package com.cosmicode.mypass.service;

import com.cosmicode.mypass.domain.Notification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NotificationApiEndpointInterface {

    @GET("notifications")
    Call<List<Notification>> getNotifications();

    @POST("notification")
    Call<Notification> createNotification(@Body Notification notification);

}
