package com.example.android.basicandroidaccessibility.room.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android.basicandroidaccessibility.room.dao.ExaminationsDao
import com.example.android.basicandroidaccessibility.room.entity.Examination

@Database(entities = [Examination::class], version = 1, exportSchema = false)
abstract class ExaminationDatabase : RoomDatabase() {

    abstract fun getExaminationsDao(): ExaminationsDao

    companion object {
        // Singleton 可防止同时打开多个数据库实例。
        @Volatile
        private var INSTANCE: ExaminationDatabase? = null

        fun getDatabase(context: Context): ExaminationDatabase {
            // 如果INSTANCE不为null，则返回，如果是，则创建数据库
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExaminationDatabase::class.java,
                    "examination_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}