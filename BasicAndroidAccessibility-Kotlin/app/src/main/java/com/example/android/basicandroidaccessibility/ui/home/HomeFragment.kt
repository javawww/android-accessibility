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

