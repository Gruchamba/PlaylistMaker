package org.guru.playlistmaker.data.dto

data class TrackSearchResponse(val resultCount: String, val results: List<TrackDto>) : Response()