package com.cosmicode.mypass.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cosmicode.mypass.BaseActivity;
import com.cosmicode.mypass.domain.Secret;
import com.cosmicode.mypass.util.network.ApiServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecretService {
    private final static String TAG = "NotificationService";
    private Context context;
    private String authToken;
    private SecretServiceListener listener;

    public SecretService(Context context, SecretServiceListener listener) {
        this.context = context;
        this.listener = listener;
        this.authToken = ((BaseActivity) this.context).getJhiUsers().getAuthToken();
    }

    public void getUserScrets(){
        SecretApiEndpointInterface apiService = ApiServiceGenerator.createService(SecretApiEndpointInterface.class, authToken);

        Call<List<Secret>> call = apiService.getSecrets();

        call.enqueue(new Callback<List<Secret>>() {
            @Override
            public void onResponse(Call<List<Secret>> call, Response<List<Secret>> response) {
                if (response.code() == 200) { // OK
                    listener.OnGetSecretsSuccess(response.body());
                } else {
                    Log.e(TAG, Integer.toString(response.code()));
                    listener.OnGetSecretsError("ERROR getting resources");
                }
            }

            @Override
            public void onFailure(Call<List<Secret>> call, Throwable t) {
                Toast.makeText(context, "Something went wrong!",
                        Toast.LENGTH_LONG).show();
                listener.OnGetSecretsError("Something went wrong!");
            }
        });

    }

    public interface SecretServiceListener {
        void OnGetSecretsSuccess(List<Secret> notifications);
        void OnGetSecretsError(String error);
    }

}
