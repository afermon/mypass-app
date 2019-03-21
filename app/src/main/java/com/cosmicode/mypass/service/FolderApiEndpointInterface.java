package com.cosmicode.mypass.service;

import com.cosmicode.mypass.domain.Folder;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FolderApiEndpointInterface {

    @GET("folders/user")
    Call<List<Folder>> getUserFolders(@Query("eagerload") Boolean eagerload);

    @POST("folders")
    Call<Folder> createFolder(@Body Folder folder);

    @PUT("folders")
    Call<Folder> updateFolder(@Body Folder folder);

    @DELETE("folders/{id}")
    Call<Void> deleteFolder(@Path("id") Long id);

    @POST("folders/share/{id}")
    Call<Folder> shareFolder(@Path("id") Long id, @Body String email);
}
