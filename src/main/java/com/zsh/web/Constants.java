package com.zsh.web;


import java.io.File;

public class Constants {
    public static final String WEB_ROOT =
            System.getProperty("user.dir") + File.separator  + "webroot";

    // 关闭Server的请求
    public static final String SHUTDOWN_COMMAND = "/SHUTDOWN";
}
