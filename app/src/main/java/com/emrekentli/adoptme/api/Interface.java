package com.emrekentli.adoptme.api;

import com.emrekentli.adoptme.model.PostModel;
import com.emrekentli.adoptme.model.UserModel;
import com.emrekentli.adoptme.model.VersionCheck;
import com.emrekentli.adoptme.model.dto.AnimalTypeDto;
import com.emrekentli.adoptme.model.dto.BreedDto;
import com.emrekentli.adoptme.model.dto.CityDto;
import com.emrekentli.adoptme.model.dto.DistrictDto;
import com.emrekentli.adoptme.model.request.LoginRequest;
import com.emrekentli.adoptme.model.request.PostRequest;
import com.emrekentli.adoptme.model.request.RegisterRequest;
import com.emrekentli.adoptme.model.response.AuthenticationResponse;
import com.emrekentli.adoptme.model.response.DataResponse;
import com.emrekentli.adoptme.model.response.ApiResponse;
import com.emrekentli.adoptme.model.response.UserDto;
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

    @GET("users/my-user")
    Call<ApiResponse<UserDto>> getUserSpecs(@Header("Authorization") String authToken);

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
    Call<Void> deleteAds(@Header("Authorization") String authToken,@Path("id") String id);
    @GET("cities")
    Call<ApiResponse<DataResponse<CityDto>>> getCities(@Header("Authorization") String authToken);
    @GET("animal-types")
    Call<ApiResponse<DataResponse<AnimalTypeDto>>> getAnimalTypes(@Header("Authorization") String authToken);

    @GET("breeds/filter")
    Call<ApiResponse<DataResponse<BreedDto>>> getBreeds(@Header("Authorization") String authToken,@Query("animalTypeId") String animalTypeId);
    @GET("districts/filter")
    Call<ApiResponse<DataResponse<DistrictDto>>> getDistricts(@Header("Authorization") String authToken, @Query("cityId") String cityId);
    @POST("posts")
    Call<ApiResponse<PostModel>> addPost(@Header("Authorization") String authToken, @Body PostRequest request);
}

