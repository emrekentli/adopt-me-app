package com.emrekentli.adoptme.api;

import com.emrekentli.adoptme.model.PostModel;
import com.emrekentli.adoptme.model.UserModel;
import com.emrekentli.adoptme.model.VersionCheck;
import com.emrekentli.adoptme.model.dto.CityDto;
import com.emrekentli.adoptme.model.request.LoginRequest;
import com.emrekentli.adoptme.model.request.RegisterRequest;
import com.emrekentli.adoptme.model.response.AuthenticationResponse;
import com.emrekentli.adoptme.model.response.DataResponse;
import com.emrekentli.adoptme.model.response.ApiResponse;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

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
import retrofit2.http.Query;

public interface Interface {




    @PUT("ads/id/confirmation/{id}")
    Call<PostModel> unConfirmation(@Path("id") String id);


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






    @GET("users/my-user")
    Call<List<UserModel>>getUserSpecs();

    @FormUrlEncoded
    @POST("posts")
    Call<PostModel> addNewAdd(@Field("ad_ownerid") String ad_ownerid,
                              @Field("ad_ownername") String ad_ownername,
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

    @GET("posts")
    Call<ApiResponse<DataResponse<PostModel>>> getAllPosts(@Header("Authorization") String authToken);

    @GET("posts/dogs")
    Call<ApiResponse<DataResponse<PostModel>>> getDogAds(@Header("Authorization") String authToken);
    @GET("posts/cats")
    Call<ApiResponse<DataResponse<PostModel>>> getCatAds(@Header("Authorization") String authToken);
    @GET("posts/others")
    Call<ApiResponse<DataResponse<PostModel>>> getOtherAds(@Header("Authorization") String authToken);

    @GET("posts")
    Call<ApiResponse<DataResponse<PostModel>>> getAds(@Header("Authorization") String authToken);
    @GET("posts/filter")
    Call<ApiResponse<DataResponse<PostModel>>> getAdsSearchWithCity(@Header("Authorization") String authToken,@Query("title") String title,@Query("searchValue") String searchValue,@Query("cityName") String cityName);

    @GET("posts/{id}")
    Call<ApiResponse<PostModel>> getById(@Header("Authorization") String authToken,@Path("id") String id);

    @GET("posts/my-user")
    Call<ApiResponse<DataResponse<PostModel>>> getOwnAds(@Header("Authorization") String authToken);
    @DELETE("posts/{id}")
    Call<Void> deleteAds(@Path("id") String id);
    @GET("cities")
    Call<ApiResponse<DataResponse<CityDto>>> getCities(@Header("Authorization") String authToken);
}

