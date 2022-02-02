package com.example.android.basicandroidaccessibility.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.basicandroidaccessibility.room.entity.Examination

// dao 类的注解
@Dao
interface ExaminationsDao {

    //数据库添加新条目
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(examination : Examination)

    //delete方法
    @Delete
    suspend fun delete(examination: Examination)

    // 查询并id排序
    @Query("select * from examinationsTable order by id desc")
    fun getAllExaminations(): LiveData<List<Examination>>

    // 更新方法
    @Update
    suspend fun update(examination: Examination)

}