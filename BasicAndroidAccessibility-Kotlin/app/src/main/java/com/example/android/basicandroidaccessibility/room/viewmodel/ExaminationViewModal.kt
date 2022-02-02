package com.example.android.basicandroidaccessibility.room.viewmodel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.android.basicandroidaccessibility.room.database.ExaminationDatabase
import com.example.android.basicandroidaccessibility.room.entity.Examination
import com.example.android.basicandroidaccessibility.room.repository.ExaminationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExaminationViewModal (application: Application) :AndroidViewModel(application) {

    // 所有笔记列表和存储库创建一个变量
    val allExaminations : LiveData<List<Examination>>
    val repository : ExaminationRepository

    // 初始化我们的 dao、存储库和所有注释
    init {
        val dao = ExaminationDatabase.getDatabase(application).getExaminationsDao()
        repository = ExaminationRepository(dao)
        allExaminations = repository.allExaminations
    }

    // 创建一个删除笔记的新方法。从我们的存储库中调用一个删除方法来删除我们的笔记。
    fun deleteExamination (examination: Examination) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(examination)
    }

    // 创建一种更新便笺的新方法。从我们的存储库中调用一个更新方法来更新我们的笔记。
    fun updateExamination(examination: Examination) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(examination)
    }


    // 创建一个新方法来向我们的数据库添加，从我们的存储库中调用一个方法来添加新注释
    fun addExamination(examination: Examination) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(examination)
    }
}