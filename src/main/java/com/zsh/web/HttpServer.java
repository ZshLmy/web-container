package com.zsh.web;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class HttpServer {

    // 存放静态资源的位置


    // 是否关闭Server
    private boolean shutdown = false;

    // 主入口
    public static void main(String[] args) {


        System.out.println(Constants.WEB_ROOT);
        HttpServer server = new HttpServer();
        server.await();
    }


    //这个函数目前只能处理静态资源的访问
    public void await() {
        // 启动ServerSocket
        ServerSocket serverSocket = null;
        int port = 8080;
        try {
            serverSocket =  new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // 循环等待一个Request请求
        while (!shutdown) {
            Socket socket = null;
            InputStream input = null;
            OutputStream output = null;
            try {
                // 创建socket
                socket = serverSocket.accept();
                input = socket.getInputStream();
                output = socket.getOutputStream();


                //Request对象和Response对象是容器负责创建的，容器会将创建的两个对象穿给Servlet，程序员可以在开发Servlet的时候使用
                //Servlet对象是Servlet容器创建的，生命周期方法都是由容器(目前我们使用的是Tomcat)调用的。
                // 这一点和我们之前所编写的代码有很大不同。在今后的学习中我们会看到，越来越多的对象交给容器或框架来创建，越来越多的方法由容器或框架来调用，
                // 开发人员要尽可能多的将精力放在业务逻辑的实现上。(web编程时专注于开发Servlet)


                // 封装input至request, 并处理请求
                Request request = new Request(input);
                request.parse();

                // 封装output至response
                Response response = new Response(output);
                response.setRequest(request);
                // 不再有response自己处理
                //response.sendStaticResource();
                // 而是如果以/servlet/开头，则委托ServletProcessor处理
                if (request.getUri().startsWith("/servlet/")) {
                    ServletProcessor1 processor = new ServletProcessor1();
                    processor.process(request, response);
                } else {
                    // 原有的静态资源处理
                    StaticResourceProcessor processor = new StaticResourceProcessor();
                    processor.process(request, response);
                }


                // 关闭socket
                socket.close();

                // 如果接受的是关闭请求，则设置关闭监听request的标志
                shutdown = request.getUri().equals(Constants.SHUTDOWN_COMMAND);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}