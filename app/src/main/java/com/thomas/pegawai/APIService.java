//02
package com.thomas.pegawai;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {
    @GET("/post")
    Call<ValueData<List<Post>>> getPost();

    @FormUrlEncoded
    @POST("/auth/login")
    Call<ValueNoData> login(
                            @Field("username")String username,
                            @Field("password")String password);
    @FormUrlEncoded
    @POST("/auth/register")
    Call<ValueNoData> register(
                            @Field("username")String username,
                            @Field("password")String password);

    @FormUrlEncoded
    @POST("/post")
    Call<ValueNoData> addPost( @Field("gambar") String gambar,
                                @Field("nama")String nama,
                               @Field("umur")int umur,
                              @Field("jabatan")String jabatan,
                                @Field("keterangan")String keterangan);
    @FormUrlEncoded
    @PUT("/post")
    Call<ValueNoData> updatePost(@Field("gambar") String gambar,
                                @Field("_id") String id,
                                @Field("nama")String nama,
                                 @Field("umur")int umur,
                                 @Field("jabatan")String jabatan,
                                 @Field("keterangan")String keterangan);

    @DELETE("/post/{id}")
    Call<ValueNoData> deletePost(@Path("id") String id);
}
