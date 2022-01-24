package com.example.android.basicandroidaccessibility.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.basicandroidaccessibility.databinding.FragmentHomeBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private var imageView: ImageView? = null  //图片
    // 声明执行器来解析 URL
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    // 该属性仅在 onCreateView 和onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 文本内容
        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        // 图片内容
        imageView = binding.imageView
        // 一旦 executor 解析 URL 并接收到图像，处理程序将在 ImageView 中加载它
        val handler = Handler(Looper.getMainLooper())
        // 初始化图像
        var image: Bitmap? = null
        // 仅用于后台进程（可能需要时间，具体取决于 Internet 速度）
        executor.execute{
            // Image URL
            val imageURL = "https://media.geeksforgeeks.org/wp-content/cdn-uploads/gfg_200x200-min.png"
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)
                // 仅用于在 UI 中进行更改
                handler.post {
                    imageView!!.setImageBitmap(image)
                }
            }
            // 如果 URL 不指向图像或任何其他类型的故障
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
        // 使用 arrayadapter 并定义一个数组
        val users = arrayOf(
                "Virat Kohli", "Rohit Sharma", "Steve Smith",
                "Kane Williamson", "Ross Taylor"
        )
        binding.userlist.adapter =
                context?.let { ArrayAdapter(it,android.R.layout.simple_list_item_1,users) }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}