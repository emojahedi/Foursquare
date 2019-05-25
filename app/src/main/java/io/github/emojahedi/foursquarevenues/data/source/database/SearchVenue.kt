package io.github.emojahedi.foursquarevenues.data.source.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "search_venue",
    primaryKeys = ["searchTerm", "venueId"],
    indices = [Index("venueId")],
    foreignKeys = [
        ForeignKey(
            entity = VenueEntry::class,
            parentColumns = ["id"],
            childColumns = ["venueId"]
        )
    ]
)

class SearchVenue(
    val searchTerm: String,
    val venueId: String,
    val orderInList: Int
)