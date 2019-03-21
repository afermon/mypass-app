package com.cosmicode.mypass.service;

import android.content.Context;
import android.util.Log;

import com.cosmicode.mypass.BaseActivity;
import com.cosmicode.mypass.domain.Folder;
import com.cosmicode.mypass.util.EncryptionHelper;
import com.cosmicode.mypass.util.network.ApiServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FolderService {
    private final static String TAG = "FolderService";
    private Context context;
    private String authToken;
    private FolderServiceListener listener;

    public FolderService(Context context, FolderServiceListener listener) {
        this.context = context;
        this.listener = listener;
        this.authToken = ((BaseActivity) this.context).getJhiUsers().getAuthToken();
    }

    public void getUserFolders(boolean eagerload) {
        FolderApiEndpointInterface apiService = ApiServiceGenerator.createService(FolderApiEndpointInterface.class, authToken);

        Call<List<Folder>> call = apiService.getUserFolders(eagerload);

        call.enqueue(new Callback<List<Folder>>() {
            @Override
            public void onResponse(Call<List<Folder>> call, Response<List<Folder>> response) {
                if (response.code() == 200) { // OK
                    listener.OnGetFoldersSuccess(response.body());
                } else {
                    Log.e(TAG, response.toString());
                    listener.OnFolderActionError(Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Folder>> call, Throwable t) {
                Log.e(TAG, t.toString());
                listener.OnFolderActionError(t.getMessage());
            }
        });

    }

    public void createFolder(Folder folder) {

        folder.setKey(EncryptionHelper.generateRandomString(0)); // Generate encryption key for the folder secrets

        FolderApiEndpointInterface apiService = ApiServiceGenerator.createService(FolderApiEndpointInterface.class, authToken);

        Call<Folder> call = apiService.createFolder(folder);

        call.enqueue(new Callback<Folder>() {
            @Override
            public void onResponse(Call<Folder> call, Response<Folder> response) {
                if (response.code() == 201) { // OK
                    listener.OnCreateFolderSuccess(response.body());
                } else {
                    Log.e(TAG, response.toString());
                    listener.OnFolderActionError(Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Folder> call, Throwable t) {
                Log.e(TAG, t.toString());
                listener.OnFolderActionError(t.getMessage());
            }
        });
    }

    public void updateFolder(Folder folder) {
        FolderApiEndpointInterface apiService = ApiServiceGenerator.createService(FolderApiEndpointInterface.class, authToken);

        Call<Folder> call = apiService.updateFolder(folder);

        call.enqueue(new Callback<Folder>() {
            @Override
            public void onResponse(Call<Folder> call, Response<Folder> response) {
                if (response.code() == 200) { // OK
                    listener.OnUpdateFolderSuccess(response.body());
                } else {
                    Log.e(TAG, response.toString());
                    listener.OnFolderActionError(Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Folder> call, Throwable t) {
                Log.e(TAG, t.toString());
                listener.OnFolderActionError(t.getMessage());
            }
        });
    }

    public void deleteFolder(Folder folder) {
        FolderApiEndpointInterface apiService = ApiServiceGenerator.createService(FolderApiEndpointInterface.class, authToken);

        Call<Void> call = apiService.deleteFolder(folder.getId());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) { // OK
                    listener.OnDeleteFolderSuccess(folder.getId());
                } else {
                    Log.e(TAG, response.toString());
                    listener.OnFolderActionError(Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, t.toString());
                listener.OnFolderActionError(t.getMessage());
            }
        });
    }

    public void shareFolder(Folder folder, String email) {
        FolderApiEndpointInterface apiService = ApiServiceGenerator.createService(FolderApiEndpointInterface.class, authToken);

        Call<Folder> call = apiService.shareFolder(folder.getId(), email);

        call.enqueue(new Callback<Folder>() {
            @Override
            public void onResponse(Call<Folder> call, Response<Folder> response) {
                if (response.code() == 200) { // OK
                    listener.OnUpdateFolderSuccess(response.body());
                } else {
                    Log.e(TAG, response.toString());
                    listener.OnFolderActionError(Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Folder> call, Throwable t) {
                Log.e(TAG, t.toString());
                listener.OnFolderActionError(t.getMessage());
            }
        });
    }

    public interface FolderServiceListener {
        void OnGetFoldersSuccess(List<Folder> Folders);

        void OnCreateFolderSuccess(Folder folder);

        void OnUpdateFolderSuccess(Folder folder);

        void OnDeleteFolderSuccess(Long id);

        void OnShareFolderSuccess(Folder folder);

        void OnFolderActionError(String error);
    }
}
