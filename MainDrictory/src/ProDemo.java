package com.automate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 服务器列表会放到一个`server.txt`文件	中，
 * 请从`server.txt`文件中读取到服务器列表并发送`http`请求确定服务器上的接收文件服务是否正常，
 * 并在控制台显示检测结果
 */
public class ProDemo02 {

    public static void detection(){
        String serverListFile = "src/server.txt"; // 服务器列表文件名

        try (BufferedReader reader = new BufferedReader(new FileReader(serverListFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String url =line;
                String regex = "^(http|https):\\/\\/([\\w-]+\\.)+[\\w]+(\\/[\\w- ./?%&=]*)?$|^((25[0-5]|2[0-4]\\d|[01]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[01]?\\d?\\d)){3})$";
                if (!url.matches(regex)) {
                    continue;
                }
                if (!line.startsWith("https://")){
                    url = "http://" + line;
                }

                // 用来检测服务器是否可达
                URL serverUrl = new URL(url);

                // 发送 HTTP 请求来检测服务器状态
                boolean isServerReachable = checkServerStatus(serverUrl);

                // 显示检测结果
                if (isServerReachable) {
                    System.out.println(serverUrl + " 正常.");
                    new FileOutputStream("src/log.txt", true).write(("服务器:" + serverUrl + "在 " + now() + "状态正常\n").getBytes());
                } else {
                    System.out.println(serverUrl + " 不正常.");
                    new FileOutputStream("src/log.txt", true).write(("服务器:" + serverUrl + "在 " + now() + "状态不正常\n").getBytes());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 使用 HttpURLConnection 发送 HTTP 请求检测服务器是否可达
    private static boolean checkServerStatus(URL serverUrl) {
        try {
            // openConnection() 方法只是创建一个 HttpURLConnection 对象，并不会实际发送 HTTP 请求
            // serverUrl.openConnection() 方法会根据 URL 协议的不同，返回不同的子类对象
            HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
            connection.setRequestMethod("HEAD"); // 使用 HEAD 请求，不下载实际内容
            // getResponseCode() 方法才会真正发送请求,并返回 HTTP 响应码
            int responseCode = connection.getResponseCode();

            // 只要 HTTP 响应码是 200，就表示服务器可达
            // Http_Ok返回值为200
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            return false;
        }
    }
    private static String now() {
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return format;
    }
}
