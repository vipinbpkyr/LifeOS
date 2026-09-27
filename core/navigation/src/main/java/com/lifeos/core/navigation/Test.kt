package com.lifeos.core.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Test(val ss: String)

/**
 * Reusable NavType generator for any @Serializable Kotlin class in Navigation Compose.
 */
inline fun <reified T : Any> serializableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json
): NavType<T> = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String): T? {
        val value = bundle.getString(key) ?: return null
        return json.decodeFromString(value)
    }

    override fun parseValue(value: String): T {
        return json.decodeFromString(Uri.decode(value))
    }

    override fun serializeAsValue(value: T): String {
        return Uri.encode(json.encodeToString(value))
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, json.encodeToString(value))
    }
}

val TestNavType: NavType<Test> = serializableType<Test>()
