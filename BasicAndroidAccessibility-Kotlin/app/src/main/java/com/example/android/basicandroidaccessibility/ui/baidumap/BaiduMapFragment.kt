/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.basicandroidaccessibility.ui.baidumap

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.PoiInfo
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.geocode.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.android.basicandroidaccessibility.R
import com.example.android.basicandroidaccessibility.databinding.FragmentNavigationBaidumapBinding
import com.google.gson.Gson


open class BaiduMapFragment : Fragment() {
    private var TAG = "BaiduMapFragment"
    private lateinit var baiduMapViewModel: BaiduMapViewModel
    private var _binding: FragmentNavigationBaidumapBinding? = null
    // 此属性仅在 onCreateView 和 onDestroyView 之间有效。
    private val binding get() = _binding!!
    private var dingwei: ImageView? = null  //定位图标
    private var position: TextView? = null // 定位信息
    private var mMapView: TextureMapView? = null
    private var mBaiduMap: BaiduMap? = null
    private var mLocationClient: LocationClient? = null

    //防止每次定位都重新设置中心点
    private var isFirstLocation = true
    // 设置marker图标
    private var bitmap: BitmapDescriptor? = null
    //注册LocationListener监听器
    private var myLocationListener = MyLocationListener()
    // 注册地图点击监听器
    private var myLocationClickListener = MyLocationClickListener()

    inner class MyLocationClickListener: BaiduMap.OnMapClickListener {
        override fun onMapClick(latLng: LatLng?) {
            Log.d("地图点击", "onMapClick: ")
            var latitude = latLng?.latitude
            var longitude = latLng?.longitude
            Log.d(TAG, "经度：$longitude，纬度：$latitude")
        }
        override fun onMapPoiClick(mapPoi: MapPoi?) {
            Log.d("地图点击", "onMapPoiClick: ")
            var poiname = mapPoi?.name
            Log.d(TAG, "名称: $poiname")
            var latLng = mapPoi?.position
            var latitude = latLng?.latitude
            var longitude = latLng?.longitude
            Log.d(TAG, "经度：$longitude，纬度：$latitude")
            // 先清除图层
            mBaiduMap?.clear()
            // 定义Marker坐标点
            var point: LatLng = LatLng(latitude!!,longitude!!)
            // 构建MarkerOption，用于在地图上添加Marker
            var options: OverlayOptions = MarkerOptions()
                .position(point).icon(bitmap)
            // 在地图上面添加marker并显示
            mBaiduMap?.addOverlay(options)
        }
    }

    //经纬度 珠江新城  113.333566,23.125733
    private var lat = 23.125733
    private var lon = 113.333566

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(requireActivity().applicationContext)
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL)

        /*var permissionList = ArrayList<String>()
        if(context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if(context?.let { ContextCompat.checkSelfPermission(it,Manifest.permission.READ_PHONE_STATE) } != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE)
        }

        if(context?.let { ContextCompat.checkSelfPermission(it,Manifest.permission.WRITE_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if(permissionList.isNotEmpty()) {
            var permissions = permissionList.toArray(arrayOfNulls<String>(permissionList.size))
            ActivityCompat.requestPermissions(context as Activity,permissions,1)
        }*/

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView: ")
        baiduMapViewModel = ViewModelProvider(this).get(BaiduMapViewModel::class.java)
        _binding = FragmentNavigationBaidumapBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        mMapView = binding.bmapView
        dingwei = binding.dingwei
        position = binding.positionTextView

        mBaiduMap = mMapView!!.map
        mMapView!!.showScaleControl(false) // 不显示地图比例尺
        mMapView!!.showZoomControls(false) // 不显示地图缩放控件
        mBaiduMap!!.isMyLocationEnabled = true//开启地图的定位图层
        // c1 定位当前位置为中心
        // initMap()
        // c2 指定经纬度为中心
        val cenpt = LatLng(lat, lon) //设定中心点坐标
        val mMapStatus: MapStatus  = MapStatus.Builder()//定义地图状态
            .target(cenpt)
            .zoom(14F)
            .build() //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        val mMapStatusUpdate:MapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap!!.setMapStatus(mMapStatusUpdate);//改变地图状态

        // 点击地图监听器
        val imageURL = "https://media.geeksforgeeks.org/wp-content/cdn-uploads/gfg_200x200-min.png"
        Glide.with(this)
            .asBitmap()
            .load(R.drawable.ic_baseline_location_on_24)
            .override(100,100)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Log.d(TAG, "加载网络图片成功")
//                    imageView.setImageBitmap(resource)
                    bitmap = BitmapDescriptorFactory.fromBitmap(resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    //当 imageView 在生命周期调用或其他原因被清除时调用。
                    // 如果您在此 imageView 以外的其他地方引用位图，
                    // 请在此处清除它，因为您不能再拥有该位图
                }
            })
        mBaiduMap?.setOnMapClickListener(myLocationClickListener)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated: ")
        dingwei!!.setOnClickListener {
            mLocationClient!!.requestLocation()
            Log.d(TAG, "onActivityCreated: $lat:::$lon")
            val ll = LatLng(lat, lon)
            val u = MapStatusUpdateFactory.newLatLng(ll)
            mBaiduMap!!.animateMapStatus(u)
            mBaiduMap!!.setMapStatus(u)
            val u1 = MapStatusUpdateFactory.zoomTo(2f) //缩放
            mBaiduMap!!.animateMapStatus(u1)
        }
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        //每次重新进入Fragement时修改为true，保证能够显示中心点
//        isFirstLocation = true
//        mMapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在暂定时销毁定位，防止切换到其他Fragment再切回来时出现黑屏现象
//        mLocationClient!!.unRegisterLocationListener(myLocationListener)
//        mLocationClient!!.stop()
        // 关闭定位图层
//        mBaiduMap!!.isMyLocationEnabled = false
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
//        mMapView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        // 退出时销毁定位
//        mLocationClient!!.unRegisterLocationListener(myLocationListener)
//        mLocationClient!!.stop()
        // 关闭定位图层
//        mBaiduMap!!.isMyLocationEnabled = false
//        mMapView!!.onDestroy()
//        mMapView = null
    }

    inner class MyLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return
            }
            if (location.locType==167){
                return
            }
            lat = location.latitude
            lon = location.longitude
            var json = Gson().toJson(location)
            Log.d(TAG, "onReceiveLocation:${json}")
            val locData = MyLocationData.Builder()
                    .accuracy(location.radius) // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.direction)
                    .latitude(location.latitude)
                    .longitude(location.longitude).build()
            val u1 = MapStatusUpdateFactory.zoomTo(18f) //缩放
            mBaiduMap!!.animateMapStatus(u1)
            //设置并显示中心点
            mBaiduMap!!.setMyLocationData(locData)
            //这个判断是为了防止每次定位都重新设置中心点和marker
            if (isFirstLocation) {
                isFirstLocation = false
                val ll = LatLng(location.latitude,location.longitude)
                val u = MapStatusUpdateFactory.newLatLng(ll)
                mBaiduMap!!.animateMapStatus(u)
                getdata(ll)
            }
        }
    }

    private fun getdata(ll: LatLng) {
        // DialogUtil.hideLoading()
        val geoCoder = GeoCoder.newInstance()
        val listener: OnGetGeoCoderResultListener = object : OnGetGeoCoderResultListener {
            // 反地理编码查询结果回调函数
            override fun onGetReverseGeoCodeResult(result: ReverseGeoCodeResult) {
                if (result == null
                        || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                    Toast.makeText(context, "抱歉，未能找到结果",Toast.LENGTH_LONG).show()
                    return
                }
                val poiInfos: List<PoiInfo> = result.poiList as ArrayList<PoiInfo>
                val address = poiInfos[0].address
                position!!.text = address
                position!!.visibility = View.VISIBLE
            }

            // 地理编码查询结果回调函数
            override fun onGetGeoCodeResult(result: GeoCodeResult) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                }
            }
        }
        // 设置地理编码检索监听者
        geoCoder.setOnGetGeoCodeResultListener(listener)
        geoCoder.reverseGeoCode(ReverseGeoCodeOption().location(ll))
    }

    fun initMap() {
        // context?.let { DialogUtil.showLoading("定位中...", it) }
        //定位初始化
        mLocationClient = LocationClient(requireActivity().applicationContext)
        //通过LocationClientOption设置LocationClient相关参数
        val option = LocationClientOption()
        option.isOpenGps = true // 打开gps
        option.setCoorType("bd09ll") // 设置坐标类型
//        option.setScanSpan(5000)
        option.setIsNeedAddress(true)

        //设置locationClientOption
        mLocationClient!!.locOption = option
        mLocationClient!!.registerLocationListener(myLocationListener)
        //开启地图定位图层
        mLocationClient!!.start()

        //定位图标样式，这里使用默认图标，但不显示精度外圈
        val myLocationConfiguration = MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null as BitmapDescriptor?)
        myLocationConfiguration.accuracyCircleFillColor = 0x00000000
        myLocationConfiguration.accuracyCircleStrokeColor = 0x00000000
        mBaiduMap!!.setMyLocationConfiguration(myLocationConfiguration)
    }

    companion object {
        fun newInstance(): Fragment {
            return BaiduMapFragment()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty()) {
                    for (result in grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(context, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show()
                            return
                        }
                    }
                } else {
                    Toast.makeText(context, "发生未知错误", Toast.LENGTH_SHORT).show()
                }
            }

            else -> {

            }
        }
    }
}
