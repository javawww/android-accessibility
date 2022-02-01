package com.example.android.basicandroidaccessibility.room.repository
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.android.basicandroidaccessibility.room.dao.NotesDao
import com.example.android.basicandroidaccessibility.room.entity.Note
import java.util.concurrent.Flow

class NoteRepository(private val notesDao: NotesDao) {

    // 列表创建一个变量，并且我们从 DAO 类中获取所有数据。
    val allNotes: LiveData<List<Note>> = notesDao.getAllNotes()

    // 创建一个插入方法来将注释添加到我们的数据库中。
    suspend fun insert(note: Note) {
        notesDao.insert(note)
    }

    // 创建一个删除方法来从数据库中删除我们的笔记。
    suspend fun delete(note: Note){
        notesDao.delete(note)
    }

    // 创建一个更新方法来从数据库更新我们的笔记。
    suspend fun update(note: Note){
        notesDao.update(note)
    }
}