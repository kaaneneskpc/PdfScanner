package com.kaaneneskpc.documentscanner.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Database
import androidx.room.TypeConverters
import com.kaaneneskpc.documentscanner.data.local.converters.DataTypeConverter
import com.kaaneneskpc.documentscanner.data.local.dao.PdfDao
import com.kaaneneskpc.documentscanner.data.model.Pdf

@Database(entities = [Pdf::class], version = 1, exportSchema = false)
@TypeConverters(DataTypeConverter::class)
abstract class PdfDatabase: RoomDatabase() {

    abstract val pdfDao: PdfDao

    companion object {
        @Volatile
        private var INSTANCE: PdfDatabase? = null
        fun getInstance(context: Context): PdfDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PdfDatabase::class.java,
                    "pdf_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}