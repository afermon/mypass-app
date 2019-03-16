package com.cosmicode.mypass.service;

import com.cosmicode.mypass.domain.Folder;
import com.cosmicode.mypass.domain.Secret;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SecretApiEndpointInterface {

    @GET("secrets/user")
    Call<List<Secret>> getUserSecrets();

    @POST("secrets")
    Call<Secret> createSecret(@Body Secret secret);

    @PUT("secrets")
    Call<Secret> updateSecret(@Body Secret secret);

    @DELETE("secrets/{id}")
    Call<Void> deleteSecret(@Path("id") Long id);

}
