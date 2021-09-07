package com.example.retrofitsampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.retrofitsampleapp.databinding.ActivityMainBinding;

import java.util.List;

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getPost();
//        getComment();
    }

    private void getPost() {
        Call<List<Post>> call = jsonPlaceHolderApi.getPost(new Integer[]{1, 3, 4}, null, null);

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
        Call <List<Comment>> call = jsonPlaceHolderApi.getComment(3);

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
}