package com.example.neeraj.ticketsnew;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface initMyApi2 {
    //@통신 방식("통신 API명")
    @POST("/info")
    Call<SignupResponse> getSignupResponse(@Body SignupRequest SignupRequest);
}
