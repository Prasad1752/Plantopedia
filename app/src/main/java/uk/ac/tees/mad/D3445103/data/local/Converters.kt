package uk.ac.tees.mad.D3445103.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uk.ac.tees.mad.D3445103.models.remote.DefaultImage

class Converters {
    @TypeConverter
    fun fromDefaultImage(defaultImage: DefaultImage): String {
        return Gson().toJson(defaultImage)
    }

    @TypeConverter
    fun toDefaultImage(defaultImageString: String): DefaultImage {
        return Gson().fromJson(defaultImageString, DefaultImage::class.java)
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toStringList(listString: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(listString, listType)
    }
}