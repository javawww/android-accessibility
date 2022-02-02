package com.example.android.basicandroidaccessibility.room.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.basicandroidaccessibility.R
import com.example.android.basicandroidaccessibility.room.entity.Examination

class ExaminationRVAdapter(
    val context: Context,
    private val examinationClickDeleteInterface: ExaminationClickDeleteInterface,
    private val examinationClickInterface: ExaminationClickInterface
) :
    RecyclerView.Adapter<ExaminationRVAdapter.ViewHolder>() {

    // on below line we are creating a
    // variable for our all examinations list.
    private val allExaminations = ArrayList<Examination>()

    // on below line we are creating a view holder class.
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // 正在创建一个初始化我们在布局文件中添加的所有变量。
        val examinationTV: TextView = itemView.findViewById<TextView>(R.id.idTVQuestion)
        val dateTV: TextView = itemView.findViewById<TextView>(R.id.idTVDate)
        val deleteIV: ImageView = itemView.findViewById<ImageView>(R.id.idIVDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflating our layout file for each item of recycler view.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.examination_rv_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 将数据设置为循环列表视图的项目.
        holder.examinationTV.text = allExaminations[position].question
        holder.dateTV.text = "更新于 : " + allExaminations[position].lastTime
        // on below line we are adding click listener to our delete image view icon.
        holder.deleteIV.setOnClickListener {
            // on below line we are calling a examination click
            // interface and we are passing a position to it.
            examinationClickDeleteInterface.onDeleteIconClick(allExaminations[position])
        }

        // on below line we are adding click listener
        // to our recycler view item.
        holder.itemView.setOnClickListener {
            // on below line we are calling a examination click interface
            // and we are passing a position to it.
            examinationClickInterface.onExaminationClick(allExaminations[position])
        }
    }

    override fun getItemCount(): Int {
        // on below line we are
        // returning our list size.
        return allExaminations.size
    }

    // below method is use to update our list of examinations.
    fun updateList(newList: List<Examination>) {
        // on below line we are clearing
        // our examinations array list
        allExaminations.clear()
        // on below line we are adding a
        // new list to our all examinations list.
        allExaminations.addAll(newList)
        // on below line we are calling notify data
        // change method to notify our adapter.
        notifyDataSetChanged()
    }
}

interface ExaminationClickDeleteInterface {
    // creating a method for click
    // action on delete image view.
    fun onDeleteIconClick(examination: Examination)
}

interface ExaminationClickInterface {
    // creating a method for click action
    // on recycler view item for updating it.
    fun onExaminationClick(examination: Examination)
}