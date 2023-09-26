package com.automate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * 服务器列表会放到一个`server.txt`文件	中，请从`server.txt`文件中读取到服务器列表并发送`http`请求确定服务器上的接收文件服务是否正常，并在控制台显示检测结果
 * @Author: PlaZe、PlaChang、PlaHui、PlaTao
 * @Date: 2023/9/24 —— 2023/9/28
 * @goal: 自动化项目
 */
public class ProDemo {
    private static DeleteTempFile deleteTempFile = null;
    /**
     * 启动服务器线程
     */
    public static void startThread() {
        new Thread(() -> {
            while (true) {
                synchronized (ProDemo.class){
                    try {
                        Thread.sleep(1000);
                        ProDemo.detection();
                    } catch (InterruptedException e) {
                        System.out.println("线程启动失败" + e.getMessage());
                    }
                }
            }
        }).start();
    }

    /**
     * 状态检测程序
     *  1. 读取服务器列表文件名
     *  2. 用来检测服务器是否可达
     *  3. 发送 HTTP 请求来检测服务器状态
     *  4. 显示检测结果
     *  5. 周期删除文件
     */
    private static void detection() {
        // 读取服务器列表文件名
        String serverListFile = "src/server.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(serverListFile))) {
            String line;
            while ((line = reader.readLine()) != null) {

                String url =line;
                String regex = "^(http|https):\\/\\/([\\w-]+\\.)+[\\w]+(\\/[\\w- ./?%&=]*)?$|^((25[0-5]|2[0-4]\\d|[01]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[01]?\\d?\\d)){3})$";

                if (!url.matches(regex)) {
                    continue;
                }
                if (!line.startsWith("https://")) {
                    url = "http://" + line;
                }

                // 用来检测服务器是否可达
                URL serverUrl = new URL(url);

                // 发送 HTTP 请求来检测服务器状态
                boolean isServerReachable = checkServerStatus(serverUrl);

                // 显示检测结果
                if (isServerReachable) {
                    System.out.println(serverUrl + " 正常.");
                    new FileOutputStream("src/log.txt", true).write(("服务器: " + serverUrl + "在 " + now() + "状态正常\n").getBytes());
                } else {
                    // 服务器不正常的情况
                    new FileOutputStream("src/log.txt", true).write(("服务器: " + serverUrl + "在 " + now() + "状态不正常\n").getBytes());
                }
                deleteTempFile = new DeleteTempFile();
                // 周期删除文件
                deleteTempFile.deleteTempFile("src/log.txt");
            }
        } catch (IOException e) {
            System.out.println("读取文件失败: " + e.getMessage());
        }
    }

    /**
     * 使用 HttpURLConnection 发送 HTTP 请求检测服务器是否可达
     * @param serverUrl 服务器地址
     * @return  只要 HTTP 响应码是 200，就表示服务器可达
     */
    private static boolean checkServerStatus(URL serverUrl) {
        try {
            // openConnection() 方法只是创建一个 HttpURLConnection 对象，并不会实际发送 HTTP 请求
            HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();// serverUrl.openConnection() 方法会根据 URL 协议的不同，返回不同的子类对象

            connection.setRequestMethod("HEAD");

            // getResponseCode() 方法才会真正发送请求,并返回 HTTP 响应码
            int responseCode = connection.getResponseCode();

            // 只要 HTTP 响应码是 200，就表示服务器可达
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            System.out.println("服务器不可达: " + e.getMessage());
            return false;
        }
    }

    /**
     * 获取当前日期时间
     * @return  用于日志输出
     */
    private static String now() {
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));
        return format;
    }
}
