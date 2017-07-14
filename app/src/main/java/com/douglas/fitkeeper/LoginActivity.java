package com.douglas.fitkeeper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.douglas.fitkeeper.example.github.Contributor;
import com.douglas.fitkeeper.example.github.GithubClient;
import com.douglas.fitkeeper.example.github.ServiceGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Douglas on 1/26/2016.
 */
public class LoginActivity extends Activity {
    private static String LOG_TAG = "LoginActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginButton = (Button)findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URLs.REDDIT_OAUTH2));
                //startActivity(intent);
                GithubClient client = ServiceGenerator.createService(GithubClient.class);
                Call<List<Contributor>> call = client.contributors("DougSig", "fit_keeper");
                call.enqueue(new Callback<List<Contributor>>() {
                    @Override
                    public void onResponse(Response<List<Contributor>> response, Retrofit retrofit) {
                        if(response.isSuccess()) {
                            List<Contributor> contributors = response.body();
                            if(contributors != null) {
                                for (Contributor contributor : contributors) {
                                    Log.d(LOG_TAG, contributor.login + " (" + contributor.contributions + ")");
                                }
                            } else {
                                Log.d(LOG_TAG, "null");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.d(LOG_TAG, t.toString());
                    }
                });
            }
        });
    }
}
