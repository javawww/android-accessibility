package com.example.android.server

import com.yanzhenjie.andserver.annotation.GetMapping
import com.yanzhenjie.andserver.annotation.RequestMapping
import com.yanzhenjie.andserver.annotation.RestController

/**
 * 测试接口
 */
@RestController
@RequestMapping(path = ["/hello"])
class HelloController {

    @GetMapping(path = ["/test"])
    fun test():String{
        return "Hello World"
    }
}