package id.kotlin.osom.Auth

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface API_profile {

    @GET("/rest/v1/profile?select=*&order=coin.desc")
    suspend fun getall(
        @Header("Authorization") token: String,
        @Header("apikey") apiKey: String,
    ) : Response<List<dataProfile>>

    @GET("/rest/v1/profile?select=*")
    suspend fun getemail(
        @Header("Authorization") token: String,
        @Header("apikey") apiKey: String,
        @Query("email") query: String
    ) : Response<List<dataProfile>>

    @GET("/rest/v1/profile?select=*")
    suspend fun getusername(
        @Header("Authorization") token: String,
        @Header("apikey") apiKey: String,
        @Query("username") query: String
    ) : Response<List<dataProfile>>

    @PATCH("/rest/v1/profile")
    suspend fun update(
        @Header("Authorization") token: String,
        @Header("apikey") apiKey: String,
        @Query("email") query : String,
        @Body data: dataProfile
    ) : Response<Unit>

    @POST("/rest/v1/profile")
    suspend fun create(
        @Header("Authorization") token: String,
        @Header("apikey") apiKey: String,
        @Body data : dataProfile
    )
}