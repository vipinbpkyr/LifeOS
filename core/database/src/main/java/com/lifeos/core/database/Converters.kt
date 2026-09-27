package com.lifeos.core.database

import androidx.room.TypeConverter
import com.lifeos.core.model.ItineraryDay
import com.lifeos.core.model.LearningTopic
import com.lifeos.core.model.PackingItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromTopicsList(value: List<LearningTopic>): String = json.encodeToString(value)

    @TypeConverter
    fun toTopicsList(value: String): List<LearningTopic> =
        if (value.isBlank()) emptyList() else json.decodeFromString(value)

    @TypeConverter
    fun fromItineraryDaysList(value: List<ItineraryDay>): String = json.encodeToString(value)

    @TypeConverter
    fun toItineraryDaysList(value: String): List<ItineraryDay> =
        if (value.isBlank()) emptyList() else json.decodeFromString(value)

    @TypeConverter
    fun fromPackingList(value: List<PackingItem>): String = json.encodeToString(value)

    @TypeConverter
    fun toPackingList(value: String): List<PackingItem> =
        if (value.isBlank()) emptyList() else json.decodeFromString(value)

    @TypeConverter
    fun fromStringList(value: List<String>): String = json.encodeToString(value)

    @TypeConverter
    fun toStringList(value: String): List<String> =
        if (value.isBlank()) emptyList() else json.decodeFromString(value)
}
