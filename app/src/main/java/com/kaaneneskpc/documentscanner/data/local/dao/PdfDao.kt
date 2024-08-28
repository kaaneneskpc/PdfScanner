package com.kaaneneskpc.documentscanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kaaneneskpc.documentscanner.data.model.Pdf
import kotlinx.coroutines.flow.Flow

@Dao
interface PdfDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPdf(pdf: Pdf): Long

    @Delete
    suspend fun deletePdf(pdf: Pdf): Int

    @Update
    suspend fun updatePdf(pdf: Pdf): Int

    @Query("SELECT * FROM pdfTable")
    fun getAllPdf(): Flow<List<Pdf>>
}