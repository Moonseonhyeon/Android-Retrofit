package com.linda.restrofitex01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textViewResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewResult = findViewById(R.id.text_view_result);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://yts.mx/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        //쿼리스트링
        Map<String, String> queryString = new HashMap<>();
        queryString.put("sort_by","rating");
        queryString.put("page", "1");

        Call<Yts> call = jsonPlaceHolderApi.getMovies(queryString);
        //통신 시작
        call.enqueue(new Callback<Yts>() {
            @Override
            public void onResponse(Call<Yts> call, Response<Yts> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                Yts yts = response.body();
                for (Yts.Data.Movie movie: yts.getData().getMovies()) {
                    String content = "";
                    content += "TITLE: " + movie.getTitle() + "\n";
                    content += "YEAR: " + movie.getYear() + "\n";
                    content += "RATING: " + movie.getRating() + "\n";
                    content += "SUMMARY: " + movie.getSummary() + "\n\n";
                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<Yts> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }


        });
    }
}