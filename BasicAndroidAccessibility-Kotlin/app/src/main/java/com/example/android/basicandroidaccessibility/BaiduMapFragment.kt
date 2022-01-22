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

package com.example.android.basicandroidaccessibility

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.baidu.location.*
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.PoiInfo
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.geocode.*
import com.google.gson.Gson
import com.mumu.dialog.MMLoading


open class BaiduMapFragment : Fragment() {
    private var TAG = "BaiduMapFragment"
    private var dingwei: ImageView? = null  //定位图标
    private var position: TextView? = null // 定位信息
    private var mMapView: TextureMapView? = null
    private var mBaiduMap: BaiduMap? = null
    private var mLocationClient: LocationClient? = null
    private var mmLoading: MMLoading? = null
    //防止每次定位都重新设置中心点
    private var isFirstLocation = true

    //注册LocationListener监听器
    var myLocationListener = MyLocationListener()

    //经纬度
    private var lat = 30.605927
    private var lon = 114.278816

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(activity!!.applicationContext)
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView: ")
        return inflater.inflate(R.layout.fragment_baidu_map_area, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        mMapView = view!!.findViewById(R.id.bmapView)
        dingwei = view!!.findViewById(R.id.dingwei)
        position = view!!.findViewById(R.id.position_text_view)

        mBaiduMap = mMapView!!.map
        mBaiduMap!!.isMyLocationEnabled = true//开启地图的定位图层
        initMap()
        showLoading("定位中...")
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
        isFirstLocation = true //每次重新进入Fragement时修改为true，保证能够显示中心点
        mMapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在暂定时销毁定位，防止切换到其他Fragment再切回来时出现黑屏现象
        mLocationClient!!.unRegisterLocationListener(myLocationListener)
        mLocationClient!!.stop()
        // 关闭定位图层
        mBaiduMap!!.isMyLocationEnabled = false
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        // 退出时销毁定位
        mLocationClient!!.unRegisterLocationListener(myLocationListener)
        mLocationClient!!.stop()
        // 关闭定位图层
        mBaiduMap!!.isMyLocationEnabled = false
        mMapView!!.onDestroy()
        mMapView = null
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
            if (location.locType==61){
                hideLoading()
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
        //定位初始化
        mLocationClient = LocationClient(activity!!.applicationContext)
        //通过LocationClientOption设置LocationClient相关参数
        val option = LocationClientOption()
        option.isOpenGps = true // 打开gps
        option.setCoorType("bd09ll") // 设置坐标类型
        option.setScanSpan(5000)
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

    protected fun showLoading(msg: String?) {
        if (mmLoading == null) {
            val builder = MMLoading.Builder(context)
                    .setMessage(msg)
                    .setCancelable(false)
                    .setCancelOutside(false)
            mmLoading = builder.create()
        } else {
            mmLoading!!.dismiss()
            val builder = MMLoading.Builder(context)
                    .setMessage(msg)
                    .setCancelable(false)
                    .setCancelOutside(false)
            mmLoading = builder.create()
        }
        mmLoading!!.show()
    }

    protected fun hideLoading() {
        if (mmLoading != null && mmLoading!!.isShowing) {
            mmLoading!!.dismiss()
        }
    }
}
