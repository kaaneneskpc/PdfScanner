package com.kaaneneskpc.documentscanner.data.model

import java.util.Date

data class Pdf(
    val id: String,
    val name: String? = "",
    val size: String? = "",
    val lastModifiedTime: Date,
)