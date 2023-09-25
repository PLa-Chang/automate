``` java
package NightTest;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FileDeletionScheduler {
    public static void main(String[] args) {
        // 创建一个ScheduledExecutorService
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        // 要删除的文件路径
        String filePath = "service.txt"; // 替换为你要删除的文件的路径

        // 调度任务以定时删除文件
        Runnable deletionTask = () -> {
            try {
                deleteFile(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        // 初始延迟时间，以毫秒为单位（例如，延迟5秒后删除文件）
        long initialDelay = 5000;

        // 使用定时任务调度删除任务
        executor.schedule(deletionTask, initialDelay, TimeUnit.MILLISECONDS);
    }

    private static void deleteFile(String filePath) throws IOException {
        File fileToDelete = new File(filePath);

        if (fileToDelete.exists()) {
            fileToDelete.delete();
            System.out.println("删除成功");
                
        } else {
            System.out.println("文件不存在: " + filePath);
            String filename = "service.txt";
            File file = new File(filename);
            file.createNewFile();
            System.out.println(file.getAbsoluteFile());
            System.out.println("新建成功");
        }
    }
}

```
