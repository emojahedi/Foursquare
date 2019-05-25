package io.github.emojahedi.foursquarevenues.data.source.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "venue_table")
class VenueEntry(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,

    @ColumnInfo(name = "cache_date") val cacheDate: Date?,

    @ColumnInfo(name = "name") val name: String,

    @ColumnInfo(name = "contact_phone") val contactPhone: String,
    @ColumnInfo(name = "contact_facebookUsername") val contactFacebookUsername: String,

    @ColumnInfo(name = "rating") val rating: Float = 0F,

    @ColumnInfo(name = "photo0_prefix") val photo0_prefix: String = "",
    @ColumnInfo(name = "photo0_suffix") val photo0_suffix: String = "",
    @ColumnInfo(name = "photo0_width") val photo0_width: Int = 0,
    @ColumnInfo(name = "photo0_height") val photo0_height: Int = 0,

    @ColumnInfo(name = "formatted_address_line0") val formatted_address_line0: String = "",
    @ColumnInfo(name = "formatted_address_line1") val formatted_address_line1: String = "",
    @ColumnInfo(name = "formatted_address_line2") val formatted_address_line2: String = ""
)
