package com.venkatesh.zohousers.data.remote.model


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class UserResponseModel(
    @SerializedName("info")
    val info: Info,
    @SerializedName("results")
    val results: List<Result>
)

data class Info(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: Int,
    @SerializedName("seed")
    val seed: String,
    @SerializedName("version")
    val version: String
)

@Entity(tableName = "users")
data class Result(
    @SerializedName("cell")
    val cell: String,
    @SerializedName("dob")
    @Embedded
    val dob: Dob,
    @PrimaryKey
    @SerializedName("email")
    val email: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("id")
    @Embedded
    val id: Id,
    @SerializedName("location")
    @Embedded
    val location: Location,
    @SerializedName("login")
    @Embedded
    val login: Login,
    @SerializedName("name")
    @Embedded
    val name: Name,
    @SerializedName("nat")
    val nat: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("picture")
    @Embedded
    val picture: Picture,
    @Embedded
    @SerializedName("registered")
    val registered: Registered
)

data class Coordinates(
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String
)

data class Dob(
    @SerializedName("age")
    val age: Int,
    @SerializedName("date")
    val date: String
)

data class Id(
    @SerializedName("name")
    val name: String,
)

data class Location(
    @SerializedName("city")
    val city: String,
    @SerializedName("coordinates")
    @Embedded
    val coordinates: Coordinates,
    @SerializedName("country")
    val country: String,
    @SerializedName("postcode")
    val postcode: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("street")
    @Embedded
    val street: Street,
    @SerializedName("timezone")
    @Embedded
    val timezone: Timezone
)

data class Login(
    @SerializedName("md5")
    val md5: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("salt")
    val salt: String,
    @SerializedName("sha1")
    val sha1: String,
    @SerializedName("sha256")
    val sha256: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("uuid")
    val uuid: String
)

data class Name(
    @SerializedName("first")
    val first: String,
    @SerializedName("last")
    val last: String,
    @SerializedName("title")
    val title: String
)


data class Picture(
    @SerializedName("large")
    val large: String,
    @SerializedName("medium")
    val medium: String,
    @SerializedName("thumbnail")
    val thumbnail: String
)

data class Registered(
    @SerializedName("age")
    @ColumnInfo(name = "reg_age")
    val age: Int,
    @ColumnInfo(name = "reg_date")
    @SerializedName("date")
    val date: String
)

data class Street(
    @SerializedName("name")
    @ColumnInfo(name = "street_name")
    val name: String,
    @SerializedName("number")
    val number: Int
)

data class Timezone(
    @SerializedName("description")
    val description: String,
    @SerializedName("offset")
    val offset: String
)