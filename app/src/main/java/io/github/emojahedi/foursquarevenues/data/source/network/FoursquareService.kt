package io.github.emojahedi.foursquarevenues.data.source.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FoursquareService {

    @GET("venues/search?v=20190509&limit=10&radius=1000")
    fun getNearByVenues(
        @Query("near") near: String,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String
    ): Observable<FoursquareVenueListJSON>


    @GET("venues/{VENUE_ID}/?v=20190509")
    fun getVenueDetail(
        @Path("VENUE_ID") id: String,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String
    ): Observable<FoursquareVenueDetailJSON>

}