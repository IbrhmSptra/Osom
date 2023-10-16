package id.kotlin.osom

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetorfitHelper {
    val baseurl = "https://qiuihpnuloyjuuylrqoi.supabase.co"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }
}