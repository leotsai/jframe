package org.jframe.infrastructure.logging;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jframe.AppContext;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by leo on 2017-06-16.
 */
public class FileLogger implements Logger{
    private final static int IntervalSeconds = 5;
    private final static long MaxPerFileBytes = 1024000;
    private final Map<String, LoggingGroup> map;
    private final ScheduledThreadPoolExecutor pool;

    public FileLogger() {
        this.map = new HashedMap();
        this.pool = new ScheduledThreadPoolExecutor(1);
    }

    public void start(){
        this.pool.scheduleAtFixedRate(() -> onTimerElapsed(), IntervalSeconds, IntervalSeconds, TimeUnit.SECONDS);
    }

    public void stop(){
        this.pool.shutdown();
    }

    private void onTimerElapsed(){
        try{
            List<WritingItem> items = new ArrayList<>();
            synchronized (map){
                for(String key : this.map.keySet()){
                    LoggingGroup group = this.map.get(key);
                    if(group.length() == 0){
                        continue;
                    }
                    items.add(new WritingItem(group));
                    group.clear();
                }
            }
            if(items.size() == 0) {
                return;
            }
            this.writeToFile(items);
            synchronized (map){
                for(WritingItem item : items){
                    LoggingGroup group = this.map.get(item.getGroup());
                    group.setLastDate(item.getLastDate());
                    group.setLastFilePath(item.getLastFilePath());
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void entry(String group, String message) {
        synchronized (this.map) {
            if (!this.map.containsKey(group)) {
                this.map.put(group, new LoggingGroup(group));
            }
            this.map.get(group).append("\r\n" + message + "\r\n\r\n");
        }
    }

    private void writeToFile(List<WritingItem> items) {
        synchronized (this) {
            for (WritingItem item : items) {
                try {
                    String date = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
                    File file;
                    if(item.getLastDate().equals(date)){
                        file = new File(item.getLastFilePath());
                        File parent = file.getParentFile();
                        if (parent.exists() == false) {
                            parent.mkdirs();
                        }
                        if(file.exists() && file.length() > MaxPerFileBytes){
                            String yearMonth = DateFormatUtils.format(new Date(), "yyyy-MM");
                            String date2 = DateFormatUtils.format(new Date(), "yyyy-MM-dd-HHmmss");
                            String relativePath = String.format("\\_logs\\%s\\%s\\%s.log", item.group, yearMonth, date2);
                            file = new File(AppContext.getRootFolder() + relativePath);
                        }
                    }
                    else{
                        String yearMonth = DateFormatUtils.format(new Date(), "yyyy-MM");
                        String relativePath = String.format("\\_logs\\%s\\%s\\%s.log", item.getGroup(), yearMonth, date);
                        file = new File(AppContext.getRootFolder() + relativePath);
                        File parent = file.getParentFile();
                        if (parent.exists() == false) {
                            parent.mkdirs();
                        }
                    }
                    FileWriter fw = new FileWriter(file, true);
                    fw.append(item.getText());
                    fw.close();

                    item.setLastDate(date);
                    item.setLastFilePath(file.getPath());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private class WritingItem{
        private final String group;
        private final String text;
        private String lastDate;
        private String lastFilePath;

        public WritingItem(LoggingGroup group){
            this.group = group.key;
            this.text = group.getString();
            this.lastDate = group.getLastDate();
            this.lastFilePath = group.getLastFilePath();
        }

        public String getGroup() {
            return group;
        }

        public String getText() {
            return text;
        }

        public String getLastDate() {
            return lastDate;
        }

        public void setLastDate(String lastDate) {
            this.lastDate = lastDate;
        }

        public String getLastFilePath() {
            return lastFilePath;
        }

        public void setLastFilePath(String lastFilePath) {
            this.lastFilePath = lastFilePath;
        }
    }

    private class LoggingGroup{
        private final String key;
        private final StringBuilder sb;
        private String lastDate;
        private String lastFilePath;

        public LoggingGroup(String key){
            this.key = key;
            this.sb = new StringBuilder();
            this.lastDate = "";
            this.lastFilePath = "";
        }

        public int length(){
            return this.sb.length();
        }

        public void clear(){
            this.sb.delete(0, this.sb.length());
        }

        public void append(String text) {
            this.sb.append(text);
        }

        public String getString(){
            return sb.toString();
        }

        public String getKey() {
            return key;
        }

        public StringBuilder getSb() {
            return sb;
        }

        public String getLastDate() {
            return lastDate;
        }

        public void setLastDate(String lastDate) {
            this.lastDate = lastDate;
        }

        public String getLastFilePath() {
            return lastFilePath;
        }

        public void setLastFilePath(String lastFilePath) {
            this.lastFilePath = lastFilePath;
        }
    }

}
