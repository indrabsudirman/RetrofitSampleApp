package com.example.retrofitsampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.retrofitsampleapp.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding =ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        Gson gson = new GsonBuilder().serializeNulls().create();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request newRequest = originalRequest.newBuilder()
                            .addHeader("Indra", "Jabriko")
                            .build();
                    return chain.proceed(newRequest);
                })
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

//        getPost();
//        getComment();
//        createPost();
        updatePost();
//        deletePost();
    }

    private void getPost() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", "1");
        parameters.put("_sort", "id");
        parameters.put("_order", "desc");


        Call<List<Post>> call = jsonPlaceHolderApi.getPost(parameters);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    mainBinding.textResult.setText("Code " + response.code());
                    return;
                }
                List<Post> posts = response.body();

                for (Post post : posts) {
                    String content = "";
                    content += "ID: " + post.getId() + " \n";
                    content += "User ID: " + post.getUserId() + " \n";
                    content += "Title: " + post.getTitle() + " \n";
                    content += "Text: " + post.getText() + " \n\n";

                    mainBinding.textResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                mainBinding.textResult.setText(t.getMessage());
            }
        });
    }

    private void getComment() {
        Call <List<Comment>> call = jsonPlaceHolderApi
                .getComment("https://jsonplaceholder.typicode.com/posts/4/comments");

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (!response.isSuccessful()) {
                    mainBinding.textResult.setText("Code " + response.code());
                    return;
                }
                List<Comment> comments = response.body();

                for (Comment comment : comments) {
                    String content = "";
                    content += "ID: " + comment.getId() + " \n";
                    content += "Post ID: " + comment.getPostId() + " \n";
                    content += "Name: " + comment.getName() + " \n";
                    content += "Email: " + comment.getEmail() + " \n";
                    content += "Text: " + comment.getText() + " \n\n";

                    mainBinding.textResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                mainBinding.textResult.setText(t.getMessage());
            }
        });
    }

    private void createPost() {
        Post post = new Post(23, "Judul baru", "Text baru");

        Map<String, String> fields =new HashMap<>();
        fields.put("userId", "26");
        fields.put("title", "New Title nech");

        Call<Post> call = jsonPlaceHolderApi.createPost(fields);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    mainBinding.textResult.setText("Code " + response.code());
                    return;
                }

                Post postResponse = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "ID: " + postResponse.getId() + " \n";
                content += "User ID: " + postResponse.getUserId() + " \n";
                content += "Title: " + postResponse.getTitle() + " \n";
                content += "Text: " + postResponse.getText() + " \n\n";

                mainBinding.textResult.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

    private void updatePost() {
        Post post = new Post(12, null, "New Text");

        Map<String, String> headers = new HashMap<>();
        headers.put("Map-Header1", "Indra");
        headers.put("Map-Header2", "Lubna");

        Call<Post> call = jsonPlaceHolderApi.patchPost(headers,5, post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    mainBinding.textResult.setText("Code : " + response.code());
                    return;
                }

                Post postResponse = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "ID: " + postResponse.getId() + " \n";
                content += "User ID: " + postResponse.getUserId() + " \n";
                content += "Title: " + postResponse.getTitle() + " \n";
                content += "Text: " + postResponse.getText() + " \n\n";

                mainBinding.textResult.setText(content);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                mainBinding.textResult.setText(t.getMessage());
            }
        });
    }

    private void deletePost() {
        Call<Void> call = jsonPlaceHolderApi.deletePost(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mainBinding.textResult.setText("Code: " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mainBinding.textResult.setText(t.getMessage());
            }
        });
    }
}