package com.automate;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 用来周期删除日志文件
 */
public class DeleteTempFile {
    /**
     * 删除临时文件
     * @param filePath  文件路径
     * @throws IOException  文件不存在抛出异常
     */
    public void deleteTempFile(String filePath) throws IOException {
        // 创建一个ScheduledExecutorService
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        // 调度任务以定时删除文件
        Runnable deletionTask = () -> {
            try {
                deleteFile(filePath);
            } catch (IOException e) {
                System.out.println("删除文件失败: " + e.getMessage());
            }
        };

        // 初始延迟时间，以毫秒为单位（例如，延迟5秒后删除文件）
        long initialDelay = 4000;

        // 使用定时任务调度删除任务
        executor.schedule(deletionTask, initialDelay, TimeUnit.MILLISECONDS);
    }

    /**
     * 执行删除文件操作
     * @param filePath 文件路径
     * @throws IOException  文件不存在抛出异常
     */
    private static void deleteFile(String filePath) throws IOException {
        File fileToDelete = new File(filePath);

        if (fileToDelete.exists()) {
            fileToDelete.delete();
            System.out.println("删除"+ filePath +"文件成功");

        } else {
            System.out.println("文件不存在: " + filePath);
            String filename = "src/log.txt";
            File file = new File(filename);
            file.createNewFile();
            System.out.println(file.getAbsoluteFile());
            System.out.println("新建"+ filename +"成功");
        }
    }
}
