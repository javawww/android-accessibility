package com.example.android.basicandroidaccessibility.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.adapters.ViewBindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.android.basicandroidaccessibility.R
import com.example.android.basicandroidaccessibility.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    // 群组列表
    private lateinit var groupListView: ListView
    // 创建一个字符串类型数组（fruitNames），其中包含不同水果图像的名称
    private val fruitNames=arrayOf("Banana","Grape","Guava","Mango","Orange","Watermelon")
    // 创建一个整数类型数组（fruitImageIds），其中包含不同水果图像的 ID
    private val fruitImageIds=arrayOf(
        "https://media.geeksforgeeks.org/wp-content/cdn-uploads/gfg_200x200-min.png",
        R.drawable.bn_dest_blue,
        R.drawable.bn_dest_blue,
        R.drawable.bn_dest_blue,
        R.drawable.bn_dest_blue,
        R.drawable.bn_dest_blue)


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        // 群组列表渲染
        groupListView = binding.groupListView
        val list=ArrayList<HashMap<String,Any>>()
        // 通过一个for循环，在HashMap中输入不同类型的数据，
        // 并将包含其数据的地图添加到 ArrayList
        // 作为列表项，此列表是 SimpleAdapter 的第二个参数
        for(i in fruitNames.indices){
            val map=HashMap<String,Any>()
            // HashMap 中的数据输入
            map["fruitName"] = fruitNames[i]
            map["fruitImage"]=fruitImageIds[i]
            // 将 HashMap 添加到 ArrayList
            list.add(map)
        }
        // 创建一个字符串类型数组（来自），其中包含列表每一行中每个视图的列名
        // 这个数组（表单）是 SimpleAdapter 的第四个参数
        val from=arrayOf("fruitName","fruitImage")
        // 创建一个整数类型数组（到），其中包含列表每一行中每个视图的id，这个数组（表单）是SimpleAdapter的第五个参数
        val to= intArrayOf(R.id.textView,R.id.imageView)
        // 创建一个 SimpleAdapter 类的对象并传递所有必需的参数
        val simpleAdapter= SimpleAdapter(context,list,R.layout.list_row_items,from,to)
        simpleAdapter.viewBinder = CustomViewBinder()
        groupListView.adapter = simpleAdapter
        // listView点击事件
        groupListView.onItemClickListener = AdapterView.OnItemClickListener {
                parent, view, position, id ->
            Log.d("AdapterView", "OnItemClickListener:")
            val selectedItemText = parent.getItemAtPosition(position)
            textView.text = "Selected : $selectedItemText"
            // 页面跳转
        }

        return root
    }

    inner class CustomViewBinder : SimpleAdapter.ViewBinder {
        override fun setViewValue(view: View?, data: Any?, text: String?): Boolean {
            Log.d("自定义视图", "setViewValue:$data , $text")
           if(view is ImageView){
               var iv = view
               context?.let {
                   Glide.with(it).load(data).into(iv)
               }
               return true
           }
            return false
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}