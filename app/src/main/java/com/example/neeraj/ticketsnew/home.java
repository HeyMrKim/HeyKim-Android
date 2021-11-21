package com.example.neeraj.ticketsnew;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonArray;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class home extends Fragment {
    View v;
/*
    ImageButton ib4;
    ImageButton ib2;
    ImageButton ib5;
    ImageButton ib7;
    ImageButton ib6;
    ImageButton bm;
  */


    Button ib4;
    Button ib7;
    Button bm;


    Button ib2;
    Button ib6;
    Button ib5;
    Button ib8;

    Button test1;
    Button test2;







    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private Context context;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://3.38.115.233:8080/all/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ApiService apiService = retrofit.create(ApiService.class);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_home, container, false);
        context = container.getContext();

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ib7 = v.findViewById(R.id.button14);
        //ib2 = (ImageButton) v.findViewById(R.id.button2);
        //ib5 = (ImageButton) v.findViewById(R.id.bm);
        ib4 = v.findViewById(R.id.button12);
        //ib6 = (ImageButton) v.findViewById(R.id.button7);
        bm =  v.findViewById(R.id.button9);


        ib6 =  v.findViewById(R.id.button14);
        ib2 =  v.findViewById(R.id.button14);
        //ib5 =  v.findViewById(R.id.button5);
        //ib4 =  v.findViewById(R.id.button2);
        //ib6 =  v.findViewById(R.id.button6);
        ib5 =   v.findViewById(R.id.button14);

        ib8 = v.findViewById(R.id.button14);

        test1 = v.findViewById(R.id.testbutton1);
        test2 = v.findViewById(R.id.testbutton2);









        ib8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.l1, new main());
                ft.commit();
            }
        });


        firebaseAuth=FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        bm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.imdb.com/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        ib6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.l1, new setting_frag());
                ft.commit();
            }
        });
        ib4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.l1, new about());
                ft.commit();
            }
        });
        ib7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                ab.setMessage("Are your sure you want to exit..?");
                ab.setPositiveButton("Yes, Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      getActivity().finish();
                    }
                });
                ab.setNegativeButton("Cancel",null);
                ab.setCancelable(false);
                ab.show();
            }
        });
        if(user!=null) {
            ib2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.l1, new main());
                    ft.commit();
                }
            });
        }
        ib5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View v2 = getActivity().getLayoutInflater().inflate(R.layout.ratingbar,null);
                RatingBar rb;
                AlertDialog.Builder ab= new AlertDialog.Builder(getContext());
                ab.setTitle("Rate us");
                rb=(RatingBar)v2.findViewById(R.id.rb);
                ab.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Thanx for rating!!", Toast.LENGTH_SHORT).show();
                    }
                });
                ab.setNegativeButton("cancel",null);
                ab.setCancelable(false);
                ab.setView(v2);
                ab.show();
            }
        });

/*
        test1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitClient retrofitClient = new RetrofitClient();
                Call<JsonArray> call = retrofitClient.apiService.getretrofitdata();
                call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Retrofit DATA get success!! ", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Fail ", Toast.LENGTH_SHORT).show();

                        }
                    }
                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                    }
                });
            }
        });
*/



        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // RetrofitClient retrofitClient2 = new RetrofitClient();

                HashMap<String,Object> input = new HashMap<>();

                input.put("username","Shabbir");
                input.put("password","testPW123");

                Log.d("TEST","checking2");



                apiService.postData(input).enqueue(new Callback<Post>() {
                    @Override

                    public void onResponse(Call<Post> call, Response<Post> response) {
                        Log.d("TEST","onResponse");


                        if(response.isSuccessful()){
                            Post data = response.body();
                            Log.d("TEST","POST 성공성공");






                        }else{
                            Log.d("TEST","POST FAIL"+response.body());
                            Log.e("연결이 비정상적 : ", "error code : " + response.code());

                        }
                        apiService.gettestdata("accessToken").enqueue(new Callback<List<Post>>() {
                            @Override
                            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                                if(response.isSuccessful()){
                                    List<Post> data = response.body();
                                    Log.d("TEST","get");
                                    Log.d("TEST",data.get(0).getAccessToken());
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Post>> call, Throwable t) {

                            }
                        });






                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        Log.d("TEST","FAIL");
                        t.printStackTrace();

                    }
                });









            }
        });











    }
}
