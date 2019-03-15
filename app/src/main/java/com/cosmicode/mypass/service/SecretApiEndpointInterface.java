package com.cosmicode.mypass.service;

import com.cosmicode.mypass.domain.Secret;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SecretApiEndpointInterface {

    @GET("secrets")
    Call<List<Secret>> getSecrets();

    @POST("secrets")
    Call<Secret> createSecret(@Body Secret secret);

}
