package com.example.android.basicandroidaccessibility.room.repository
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.android.basicandroidaccessibility.room.dao.ExaminationsDao
import com.example.android.basicandroidaccessibility.room.entity.Examination
import java.util.concurrent.Flow

class ExaminationRepository(private val examinationsDao: ExaminationsDao) {

    // 列表创建一个变量，并且我们从 DAO 类中获取所有数据。
    val allExaminations: LiveData<List<Examination>> = examinationsDao.getAllExaminations()

    // 创建一个插入方法来将注释添加到我们的数据库中。
    suspend fun insert(examination: Examination) {
        examinationsDao.insert(examination)
    }

    // 创建一个删除方法来从数据库中删除我们的笔记。
    suspend fun delete(examination: Examination){
        examinationsDao.delete(examination)
    }

    // 创建一个更新方法来从数据库更新我们的笔记。
    suspend fun update(examination: Examination){
        examinationsDao.update(examination)
    }
}