package team.loser.plantdiseasedetection.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import team.loser.plantdiseasedetection.models.Disease;

public interface ApiServices {
    public static String DOMAIN = "http://192.168.1.8:5000/";
    Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").create();

    ApiServices apiServices = new Retrofit.Builder()
            .baseUrl(DOMAIN)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServices.class);
    @Multipart
    @POST("upload")
    Call<Disease> sendImage(@Part MultipartBody.Part image);
}