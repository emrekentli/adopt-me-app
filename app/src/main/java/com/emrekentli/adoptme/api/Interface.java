package com.emrekentli.adoptme.api;

import com.emrekentli.adoptme.model.PostModel;
import com.emrekentli.adoptme.model.UserModel;
import com.emrekentli.adoptme.model.VersionCheck;
import com.emrekentli.adoptme.model.request.LoginRequest;
import com.emrekentli.adoptme.model.request.RegisterRequest;
import com.emrekentli.adoptme.model.response.AuthenticationResponse;
import com.emrekentli.adoptme.model.response.DataResponse;
import com.emrekentli.adoptme.model.response.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Interface {


    @GET("ads")
    Call<List<PostModel>> getAdsSearch();

    @GET("ads/others")
    Call<List<PostModel>> getOtherAdsSearch();

    @GET("search/{tags}")
    Call<List<PostModel>> getSearch(@Path("tags") String tags);


    @GET("search/{city}/{tags}")
    Call<List<PostModel>> getAdsSearchWithCity(@Path("tags") String tags,  @Path("city") String city);


    @DELETE("ads/id/{id}")
    Call<PostModel> deleteAds(@Path("id") String id);


    @PUT("ads/id/confirmation/{id}")
    Call<PostModel> unConfirmation(@Path("id") String id);

    @GET("version")
    Call<List<VersionCheck>> checkVersion();


    @FormUrlEncoded
    @PUT("ads/id/{id}")
    Call<PostModel> editAds  (@Path("id") String id,
                              @Field("ad_ownerid") String ad_ownerid,
                              @Field("ad_ownername") String ad_ownername,
                              @Field("ad_ownertelephone") String ad_ownertelephone,
                              @Field("ad_type") String ad_type,
                              @Field("ad_name") String ad_name,
                              @Field("ad_detail") String ad_detail,
                              @Field("ad_category") String ad_category,
                              @Field("ad_altcategory") String ad_altcategory,
                              @Field("ad_age") String ad_age,
                              @Field("confirmation") Integer confirmation,
                              @Field("country") String country,
                              @Field("reason") String reason,
                              @Field("ad_image") String ad_image,
                              @Field("ad_image2") String ad_image2,
                              @Field("tags") String tags,
                              @Field("date") String date,
                              @Field("ad_sex") String ad_sex);



    @PUT("ads/view/{id}")
    Call<PostModel> view  (@Path("id") Integer id);






    @GET("users/{id}")
    Call<List<UserModel>>getUserSpecs(@Path("id") String id);

    @FormUrlEncoded
    @POST("new")
    Call<PostModel> addNewAdd(@Field("ad_ownerid") String ad_ownerid,
                              @Field("ad_ownername") String ad_ownername,
                              @Field("ad_ownertelephone") String ad_ownertelephone,
                              @Field("ad_type") String ad_type,
                              @Field("ad_name") String ad_name,
                              @Field("ad_detail") String ad_detail,
                              @Field("ad_category") String ad_category,
                              @Field("ad_altcategory") String ad_altcategory,
                              @Field("ad_age") String ad_age,
                              @Field("confirmation") String confirmation,
                              @Field("country") String country,
                              @Field("reason") String reason,
                              @Field("ad_image") String ad_image,
                              @Field("ad_image2") String ad_image2,
                              @Field("tags") String tags,
                              @Field("date") String date,
                              @Field("ad_sex") String ad_sex,
                              @Field("ad_view") Integer ad_view);













    //////////


    @POST("auth/register")
    Call<ApiResponse<Void>> registerUser(@Body RegisterRequest registerRequest);

    @POST("auth/login")
    Call<ApiResponse<AuthenticationResponse>> loginUser(@Body LoginRequest loginReq);

    @GET("posts/dogs")
    Call<ApiResponse<DataResponse<PostModel>>> getDogAds(@Header("Authorization") String authToken);
    @GET("posts/cats")
    Call<ApiResponse<DataResponse<PostModel>>> getCatAds(@Header("Authorization") String authToken);
    @GET("posts/others")
    Call<ApiResponse<DataResponse<PostModel>>> getOtherAds(@Header("Authorization") String authToken);

    @GET("posts")
    Call<ApiResponse<DataResponse<PostModel>>> getAds(@Header("Authorization") String authToken);

    @GET("posts/{id}")
    Call<ApiResponse<PostModel>> getById(@Header("Authorization") String authToken,@Path("id") String id);

    @GET("posts/my-user")
    Call<ApiResponse<DataResponse<PostModel>>> getOwnAds(@Header("Authorization") String authToken);

}

