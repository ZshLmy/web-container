package com.zsh.web;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

public class ServletProcessor1 {

    public void process(Request request, Response response) {
        // 获取servlet名字
        String uri = request.getUri();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);

        // 初始化URLClassLoader
        ClassLoader loader = ServletProcessor1.class.getClassLoader();

        // 用classLoader加载上面的servlet
        Class myClass = null;
        try {
            assert loader != null;
            myClass = loader.loadClass("com.zsh.web."+servletName);
        }
        catch (ClassNotFoundException e) {
            System.out.println(e.toString());
        }

        // 将加载到的class转成Servlet，并调用service方法处理
        Servlet servlet = null;
        try {
            servlet = (Servlet) myClass.newInstance();
            servlet.service((ServletRequest) request, (ServletResponse) response);
        } catch (Exception e) {
            System.out.println(e.toString());
        } catch (Throwable e) {
            System.out.println(e.toString());
        }

    }
}