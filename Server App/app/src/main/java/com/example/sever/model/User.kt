package com.example.sever.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
data class User(
    @PrimaryKey
    @ColumnInfo("user_id")
    val id: Int,
    @ColumnInfo("name")
    val name: String
) : Parcelable