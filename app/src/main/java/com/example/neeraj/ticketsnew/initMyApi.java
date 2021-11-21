package com.example.neeraj.ticketsnew;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface initMyApi {
    //@통신 방식("통신 API명")
    @POST("/all")
    Call<LoginResponse> getLoginResponse(@Body LoginRequest loginRequest);
}
