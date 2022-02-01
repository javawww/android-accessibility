package com.example.android.basicandroidaccessibility.room.viewmodel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.android.basicandroidaccessibility.room.database.NoteDatabase
import com.example.android.basicandroidaccessibility.room.entity.Note
import com.example.android.basicandroidaccessibility.room.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModal (application: Application) :AndroidViewModel(application) {

    // 所有笔记列表和存储库创建一个变量
    val allNotes : LiveData<List<Note>>
    val repository : NoteRepository

    // 初始化我们的 dao、存储库和所有注释
    init {
        val dao = NoteDatabase.getDatabase(application).getNotesDao()
        repository = NoteRepository(dao)
        allNotes = repository.allNotes
    }

    // 创建一个删除笔记的新方法。从我们的存储库中调用一个删除方法来删除我们的笔记。
    fun deleteNote (note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }

    // 创建一种更新便笺的新方法。从我们的存储库中调用一个更新方法来更新我们的笔记。
    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }


    // 创建一个新方法来向我们的数据库添加，从我们的存储库中调用一个方法来添加新注释
    fun addNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }
}