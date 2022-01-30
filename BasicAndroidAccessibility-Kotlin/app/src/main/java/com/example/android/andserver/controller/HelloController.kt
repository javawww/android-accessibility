package com.example.android.andserver.controller

import com.yanzhenjie.andserver.annotation.GetMapping
import com.yanzhenjie.andserver.annotation.RequestMapping
import com.yanzhenjie.andserver.annotation.RestController

@RestController
@RequestMapping("test")
class HelloController {

    @GetMapping("/hello")
    fun hello():String{
        return "Hello World"
    }
}