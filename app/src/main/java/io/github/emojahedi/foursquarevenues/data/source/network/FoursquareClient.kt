package io.github.emojahedi.foursquarevenues.data.source.network

import android.content.Context
import io.reactivex.Observable
import io.github.emojahedi.foursquarevenues.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class FoursquareClient(context: Context) {

    private val BASE_URL = "https://api.foursquare.com/v2/"
    private var foursquareService: FoursquareService

    private var CLIENT_ID: String
    private var CLIENT_SECRET: String


    companion object {
        @Volatile
        private var INSTANCE: FoursquareClient? = null

        fun getInstance(context: Context): FoursquareClient {
            return INSTANCE ?: synchronized(this) {
                val instance = FoursquareClient(context)
                INSTANCE = instance
                return instance
            }
        }
    }


    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()

        foursquareService = retrofit.create(FoursquareService::class.java)
        CLIENT_ID = context.resources.getString(R.string.foursquare_client_id)
        CLIENT_SECRET = context.resources.getString(R.string.foursquare_client_secret)
    }

    fun getNearByVenues(locationName: String): Observable<FoursquareVenueListJSON> {
        return foursquareService.getNearByVenues(locationName, CLIENT_ID, CLIENT_SECRET)
    }

    fun getVenueDetail(id: String): Observable<FoursquareVenueDetailJSON> {
        return foursquareService.getVenueDetail(id, CLIENT_ID, CLIENT_SECRET)
    }

}