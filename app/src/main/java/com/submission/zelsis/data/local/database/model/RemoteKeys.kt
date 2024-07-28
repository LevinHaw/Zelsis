package com.submission.zelsis.data.local.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys (
    @PrimaryKey
    val id: String,
    val previousKey: Int?,
    val nextKey: Int?,

)