package com.lifeos.core.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Test(val ss: String)

val TestNavType = object : NavType<Test>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Test? {
        val value = bundle.getString(key) ?: return null
        return Json.decodeFromString(value)
    }

    override fun parseValue(value: String): Test {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun serializeAsValue(value: Test): String {
        return Uri.encode(Json.encodeToString(value))
    }

    override fun put(bundle: Bundle, key: String, value: Test) {
        bundle.putString(key, Json.encodeToString(value))
    }
}
