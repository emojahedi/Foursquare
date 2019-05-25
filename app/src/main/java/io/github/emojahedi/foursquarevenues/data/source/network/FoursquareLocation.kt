package io.github.emojahedi.foursquarevenues.data.source.network

data class FoursquareLocation(
    var address: String,
    var lat: Double = 0.toDouble(),
    var lng: Double = 0.toDouble(),
    var distance: Int = 0
) {


}