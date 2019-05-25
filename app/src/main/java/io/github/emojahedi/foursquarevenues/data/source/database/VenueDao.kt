package io.github.emojahedi.foursquarevenues.data.source.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.*


@Dao
interface VenueDao {

    @Query("SELECT * FROM venue_table WHERE id = :id")
    fun getVenueById(id: String): VenueEntry?

    // TODO: check if order by works correctly
    @Query(
        """
            SELECT venue_table.*
            FROM venue_table, search_venue
            WHERE venue_table.id = search_venue.venueId
            AND search_venue.searchTerm = :locationName
            ORDER BY search_venue.orderInList
            LIMIT 10"""
    )
    fun getVenuesForSearchTerm(locationName: String): List<VenueEntry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(venueEntry: VenueEntry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun bulkInsert(venueEntries: List<VenueEntry>)

//    TODO: delete expired
//    @Query("DELETE FROM venue_table WHERE venue_table.cache_date >= :threshold")
//    fun deleteOlderThan(threshold: Date)
}