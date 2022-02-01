package com.example.android.basicandroidaccessibility.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.basicandroidaccessibility.room.entity.Note

// dao 类的注解
@Dao
interface NotesDao {

    //数据库添加新条目的插入方法
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note : Note)

    //删除笔记的delete方法
    @Delete
    suspend fun delete(note: Note)

    // 查询并id排序
    @Query("Select * from notesTable order by id ASC")
    fun getAllNotes(): LiveData<List<Note>>

    // 以下方法用于更新笔记
    @Update
    suspend fun update(note: Note)

}