package com.znu.personalfinancetracker.navigation

import android.os.Bundle
import androidx.navigation.NavType
import com.znu.personalfinancetracker.data.model.transaction.Transaction
import kotlinx.serialization.json.Json

class TransactionNavType : NavType<Transaction?>(isNullableAllowed = true) {

    override fun get(bundle: Bundle, key: String): Transaction? {
        val jsonString = bundle.getString(key)
        return if (jsonString == null || jsonString == "null") {
            null
        } else {
            Json.decodeFromString(jsonString)
        }
    }

    override fun parseValue(value: String): Transaction? {
        return if (value == "null") {
            null
        } else {
            Json.decodeFromString(value)
        }
    }

    override fun put(bundle: Bundle, key: String, value: Transaction?) {
        val jsonString =
                if (value == null) {
                    "null"
                } else {
                    Json.encodeToString(value)
                }
        bundle.putString(key, jsonString)
    }

    override fun serializeAsValue(value: Transaction?): String {
        return if (value == null) {
            "null"
        } else {
            Json.encodeToString(value)
        }
    }
}
