package com.cosmicode.mypass.service;

import com.cosmicode.mypass.domain.Folder;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface FolderApiEndpointInterface {

    @GET("folders")
    Call<List<Folder>> getFolders();

    @POST("folders")
    Call<Folder> createFolder(@Body Folder folder);

}
