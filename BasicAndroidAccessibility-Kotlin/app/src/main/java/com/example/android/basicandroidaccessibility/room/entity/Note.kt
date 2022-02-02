package com.example.android.basicandroidaccessibility.room.entity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//指定我们的表名
@Entity(tableName = "notesTable")
class Note (
    @ColumnInfo(name = "title")val noteTitle :String,
    @ColumnInfo(name = "description")val noteDescription :String,
    @ColumnInfo(name = "timestamp")val timeStamp :String
        ) {
    // 主键，自增长，初始值指定为 0
    @PrimaryKey(autoGenerate = true) var id = 0
}