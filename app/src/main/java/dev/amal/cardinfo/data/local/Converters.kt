package dev.amal.cardinfo.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import dev.amal.cardinfo.data.remote.dto.Bank
import dev.amal.cardinfo.data.remote.dto.Country
import dev.amal.cardinfo.data.remote.dto.Number
import dev.amal.cardinfo.data.util.JsonParser

@ProvidedTypeConverter
class Converters(
    private val jsonParser: JsonParser
) {
    @TypeConverter
    fun fromBankJson(json: String): Bank? =
        jsonParser.fromJson<Bank>(json, object : TypeToken<Bank>() {}.type)

    @TypeConverter
    fun toBankJson(bank: Bank?): String? =
        jsonParser.toJson(bank, object : TypeToken<Bank>() {}.type)

    @TypeConverter
    fun fromCountryJson(json: String): Country? =
        jsonParser.fromJson<Country>(json, object : TypeToken<Country>() {}.type)

    @TypeConverter
    fun toCountryJson(country: Country?): String? =
        jsonParser.toJson(country, object : TypeToken<Country>() {}.type)

    @TypeConverter
    fun fromNumberJson(json: String): Number? =
        jsonParser.fromJson<Number>(json, object : TypeToken<Number>() {}.type)

    @TypeConverter
    fun toCountryJson(number: Number?): String? =
        jsonParser.toJson(number, object : TypeToken<Number>() {}.type)
}