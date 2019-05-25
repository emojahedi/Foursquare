package io.github.emojahedi.foursquarevenues.data.source.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.*

@Dao
interface SearchVenueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun bulkInsert(searchVenues: List<SearchVenue>)

//    TODO: delete expired
//    @Query("DELETE FROM venue_table WHERE venue_table.cache_date >= :threshold")
//    fun deleteOlderThan(threshold: Date)
}