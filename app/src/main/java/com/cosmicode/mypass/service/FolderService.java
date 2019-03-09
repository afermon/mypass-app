package com.cosmicode.mypass.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cosmicode.mypass.BaseActivity;
import com.cosmicode.mypass.domain.Folder;
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

    public void getUserFolders(){
        FolderApiEndpointInterface apiService = ApiServiceGenerator.createService(FolderApiEndpointInterface.class, authToken);

        Call<List<Folder>> call = apiService.getUserFolders();

        call.enqueue(new Callback<List<Folder>>() {
            @Override
            public void onResponse(Call<List<Folder>> call, Response<List<Folder>> response) {
                if (response.code() == 200) { // OK
                    listener.OnGetFoldersSuccess(response.body());
                } else {
                    Log.e(TAG, Integer.toString(response.code()));
                    listener.OnGetFoldersError("ERROR getting resources");
                }
            }

            @Override
            public void onFailure(Call<List<Folder>> call, Throwable t) {
                Toast.makeText(context, "Something went wrong!",
                        Toast.LENGTH_LONG).show();
                listener.OnGetFoldersError("Something went wrong!");
            }
        });

    }

    public interface FolderServiceListener {
        void OnGetFoldersSuccess(List<Folder> Folders);
        void OnGetFoldersError(String error);
    }

}
