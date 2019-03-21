package com.cosmicode.mypass.service;

import android.content.Context;
import android.util.Log;

import com.cosmicode.mypass.BaseActivity;
import com.cosmicode.mypass.domain.Secret;
import com.cosmicode.mypass.util.EncryptionHelper;
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

    public void getUserScrets() {
        SecretApiEndpointInterface apiService = ApiServiceGenerator.createService(SecretApiEndpointInterface.class, authToken);

        Call<List<Secret>> call = apiService.getUserSecrets();

        call.enqueue(new Callback<List<Secret>>() {
            @Override
            public void onResponse(Call<List<Secret>> call, Response<List<Secret>> response) {
                if (response.code() == 200) { // OK
                    listener.OnGetSecretsSuccess(response.body());
                } else {
                    Log.e(TAG, response.toString());
                    listener.OnSecretActionError(Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Secret>> call, Throwable t) {
                Log.e(TAG, t.toString());
                listener.OnSecretActionError(t.getMessage());
            }
        });

    }

    public void createSecret(Secret secret, String folderKey) {

        if (secret.getNewPassword() != null) {
            try {
                secret.setPassword(EncryptionHelper.encrypt(folderKey, secret.getNewPassword()));
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        SecretApiEndpointInterface apiService = ApiServiceGenerator.createService(SecretApiEndpointInterface.class, authToken);

        Call<Secret> call = apiService.createSecret(secret);

        call.enqueue(new Callback<Secret>() {
            @Override
            public void onResponse(Call<Secret> call, Response<Secret> response) {
                if (response.code() == 201) { // OK
                    listener.OnCreateSecretSuccess(response.body());
                } else {
                    Log.e(TAG, response.toString());
                    listener.OnSecretActionError(Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Secret> call, Throwable t) {
                Log.e(TAG, t.toString());
                listener.OnSecretActionError(t.getMessage());
            }
        });
    }

    public void updateSecret(String folderKey, Secret secret) {

        if (secret.getNewPassword() != null && !secret.getNewPassword().equals("")) {
            try {
                secret.setPassword(EncryptionHelper.encrypt(folderKey, secret.getNewPassword()));
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        SecretApiEndpointInterface apiService = ApiServiceGenerator.createService(SecretApiEndpointInterface.class, authToken);

        Call<Secret> call = apiService.updateSecret(secret);

        call.enqueue(new Callback<Secret>() {
            @Override
            public void onResponse(Call<Secret> call, Response<Secret> response) {
                if (response.code() == 200) { // OK
                    listener.OnUpdateSecretSuccess(response.body());
                } else {
                    Log.e(TAG, response.toString());
                    listener.OnSecretActionError(Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Secret> call, Throwable t) {
                Log.e(TAG, t.toString());
                listener.OnSecretActionError(t.getMessage());
            }
        });
    }

    public void deleteSecret(Secret secret) {
        SecretApiEndpointInterface apiService = ApiServiceGenerator.createService(SecretApiEndpointInterface.class, authToken);

        Call<Void> call = apiService.deleteSecret(secret.getId());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) { // OK
                    listener.OnDeleteSecretSuccess(secret.getId());
                } else {
                    Log.e(TAG, response.toString());
                    listener.OnSecretActionError(Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, t.toString());
                listener.OnSecretActionError(t.getMessage());
            }
        });
    }

    public interface SecretServiceListener {
        void OnGetSecretsSuccess(List<Secret> secrets);

        void OnCreateSecretSuccess(Secret secret);

        void OnUpdateSecretSuccess(Secret secret);

        void OnDeleteSecretSuccess(Long id);

        void OnSecretActionError(String error);
    }

}
