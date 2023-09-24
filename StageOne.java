/**
 * 服务器列表会放到一个`server.txt`文件	中，
 * 请从`server.txt`文件中读取到服务器列表并发送`http`请求确定服务器上的接收文件服务是否正常，
 * 并在控制台显示检测结果
 */
public class ProDemo {
    public static void main(String[] args) {
        String serverListFile = "server.txt"; // 服务器列表文件名

        try (BufferedReader reader = new BufferedReader(new FileReader(serverListFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 每行应包含服务器的 URL
                // 用来检测服务器是否可达
                URL serverUrl = new URL(line);

                // 发送 HTTP 请求来检测服务器状态
                boolean isServerReachable = isServerReachable(serverUrl);

                // 显示检测结果
                if (isServerReachable) {
                    System.out.println(serverUrl + " 正常.");
                } else {
                    System.out.println(serverUrl + " 不正常.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 使用 HttpURLConnection 发送 HTTP 请求检测服务器是否可达
    private static boolean isServerReachable(URL serverUrl) {
        try {
            // openConnection() 方法只是创建一个 HttpURLConnection 对象，并不会实际发送 HTTP 请求
            // serverUrl.openConnection() 方法会根据 URL 协议的不同，返回不同的子类对象
            HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
            connection.setRequestMethod("HEAD"); // 使用 HEAD 请求，不下载实际内容
            // getResponseCode() 方法才会真正发送请求,并返回 HTTP 响应码
            int responseCode = connection.getResponseCode();
            // 只要 HTTP 响应码是 200，就表示服务器可达
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            return false;
        }
    }
}
