package dev.msiviero.client;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GravatarClient {

    @GET("/{hash}.json")
    Call<GravatarResponse> userProfile(@Path("hash") String hash);
}
