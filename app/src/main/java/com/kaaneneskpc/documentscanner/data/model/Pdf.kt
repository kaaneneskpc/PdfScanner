package com.kaaneneskpc.documentscanner.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "pdfTable")
data class Pdf(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pdfId")
    val id: String,
    val name: String? = "",
    val size: String? = "",
    val lastModifiedTime: Date,
)