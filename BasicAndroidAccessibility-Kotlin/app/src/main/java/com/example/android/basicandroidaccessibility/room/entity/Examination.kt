package com.example.android.basicandroidaccessibility.room.entity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//指定我们的表名
@Entity(tableName = "examinationsTable")
class Examination (
    @ColumnInfo(name = "uuid")val uuid :String,
    @ColumnInfo(name = "deviceId")val deviceId :String,
    @ColumnInfo(name = "lastTime")val lastTime :String,
    @ColumnInfo(name = "question")val question :String,
    @ColumnInfo(name = "label")val label :String,
    @ColumnInfo(name = "image")val image :String,
    @ColumnInfo(name = "ans1")val ans1 :String,
    @ColumnInfo(name = "ans2")val ans2 :String,
    @ColumnInfo(name = "ans3")val ans3 :String,
    @ColumnInfo(name = "ans4")val ans4 :String,
    @ColumnInfo(name = "correct")val correct :Int,
    @ColumnInfo(name = "analyze")val analyze :String,
        ) {
    // 主键，自增长，初始值指定为 0
    @PrimaryKey(autoGenerate = true) var id = 0
}