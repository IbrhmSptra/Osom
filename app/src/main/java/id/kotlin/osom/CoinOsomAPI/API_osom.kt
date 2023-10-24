package id.kotlin.osom.CoinOsomAPI

import id.kotlin.osom.Auth.dataProfile
import retrofit2.Response
import retrofit2.http.*

interface API_osom {
    @GET("/rest/v1/osom?select=*")
    suspend fun get(
        @Header("Authorization") token: String,
        @Header("apikey") apiKey: String,
        @Query("id") query: String,
    ) : Response<List<dataOsom>>

    @PATCH("/rest/v1/osom")
    suspend fun update(
        @Header("Authorization") token: String,
        @Header("apikey") apiKey: String,
        @Query("id") query : String,
        @Body data: dataOsom
    ) : Response<Unit>
}