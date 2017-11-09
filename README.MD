路畅科技IVI SDK的实例应用
==============================================

一、背景介绍
------------
针对路畅科技安卓软件平台 Roadrover IVI SDK使用，编写了各个模块的实例应用。
每个模块的实例应用中添加了主要接口部分的视图，直观的通过sdk与路畅科技IVI系统对接。

二、Demo概述
------------
Demo包含了原车、车载信息、系统、收音机、蓝牙、多媒体、音频、外部输入、设置、存储设备和语音几个模块。

三、目录组织说明
----------------
+ utils：      工具类目录
+ ui.activity：安卓应用activity目录
+ ui.view：    自定义控件目录

四、Demo源码说明
----------------
+ ScreenUtils： 屏幕相关的辅助工具类。
+ IVIButton：   自定义按钮。
+ BaseActivity：所有实例应用Activity的基类，封装了AppCompatActivity标题设置、文本内容提示、返回功能、视图添加、模块跳转接口等功能。
+ SDKActivity： 实现了ConnectListener接口类，以便于和sdk的Manager交互使用，提示与路畅科技IVI系统连接状态，以及暴露视图有效、无效的回调接口。
+ MainActivity：实例应用的主页入口。
+ ...