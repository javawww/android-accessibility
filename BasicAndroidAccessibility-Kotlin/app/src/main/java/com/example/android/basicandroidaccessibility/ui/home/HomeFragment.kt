package com.example.android.basicandroidaccessibility.ui.home

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.android.basicandroidaccessibility.R
import com.example.android.basicandroidaccessibility.databinding.FragmentHomeBinding
import com.example.android.basicandroidaccessibility.ui.user.UserDetailFragment
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.android.basicandroidaccessibility.ui.user.LoginFragment
import com.example.android.basicandroidaccessibility.ui.user.SignupFragment


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    // 该属性仅在 onCreateView 和onDestroyView
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var imageView: ImageView? = null  //图片
    // 声明执行器来解析 URL
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    // 列表数据
    private lateinit var listView: ListView
    // 创建一个字符串类型数组（fruitNames），其中包含不同水果图像的名称
    private val fruitNames=arrayOf("Banana","Grape","Guava","Mango","Orange","Watermelon")
    // 创建一个整数类型数组（fruitImageIds），其中包含不同水果图像的 ID
    private val fruitImageIds=arrayOf(
            R.drawable.abc_vector_test,
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
        // 渲染列表数据
        listView = binding.listView
        // 创建 HashMap 的 ArrayList。HashMap 的 key 是 String，VALUE 是任何数据类型（Any）
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
        // 创建一个整数类型数组（到），其中包含
        // 列表每一行中每个视图的id，这个数组（表单）是SimpleAdapter的第五个参数
        val to= intArrayOf(R.id.textView,R.id.imageView)
        // 创建一个 SimpleAdapter 类的对象并传递所有必需的参数
        val simpleAdapter= SimpleAdapter(context,list,R.layout.list_row_items,from,to)
        listView.adapter = simpleAdapter
        // listView点击事件
        listView.onItemClickListener = AdapterView.OnItemClickListener {
            parent, view, position, id ->
            Log.d("AdapterView", "OnItemClickListener: ")
            val selectedItemText = parent.getItemAtPosition(position)
            textView.text = "Selected : $selectedItemText"
            // 页面跳转
        }

        // 登陆注册
//        var tab_toolbar =  binding.toolbar
        var tab_viewpager = binding.tabViewpager
        var tab_tablayout = binding.tabTablayout
//        (activity as AppCompatActivity?)!!.setSupportActionBar(tab_toolbar)
        setupViewPager(tab_viewpager)
        tab_tablayout.setupWithViewPager(tab_viewpager)
        return root
    }

    private fun setupViewPager(viewpager: ViewPager) {
        var adapter: ViewPagerAdapter? = fragmentManager?.let { ViewPagerAdapter(it) }

        // LoginFragment is the name of Fragment and the Login
        // is a title of tab
        adapter?.addFragment(LoginFragment(), "Login")
        adapter?.addFragment(SignupFragment(), "Signup")
        // setting adapter to view pager.
        viewpager.adapter = adapter
    }

    class ViewPagerAdapter : FragmentStatePagerAdapter {
        // objects of arraylist. One is of Fragment type and
        // another one is of String type.*/
        private final var fragmentList1: ArrayList<Fragment> = ArrayList()
        private final var fragmentTitleList1: ArrayList<String> = ArrayList()
        // this is a secondary constructor of ViewPagerAdapter class.
        public constructor(supportFragmentManager: FragmentManager)
                : super(supportFragmentManager)
        // returns which item is selected from arraylist of fragments.
        override fun getItem(position: Int): Fragment {
            return fragmentList1.get(position)
        }

        // returns which item is selected from arraylist of titles.
        @Nullable
        override fun getPageTitle(position: Int): CharSequence {
            return fragmentTitleList1.get(position)
        }
        // returns the number of items present in arraylist.
        override fun getCount(): Int {
            return fragmentList1.size
        }
        // this function adds the fragment and title in 2 separate  arraylist.
        fun addFragment(fragment: Fragment, title: String) {
            fragmentList1.add(fragment)
            fragmentTitleList1.add(title)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

