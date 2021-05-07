package ru.runov.client;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class FileInfoClient {
    public enum FileType {
        FILE("F"),DIR("D");
        private String name;

        public String getName() {
            return name;
        }
        FileType(String name) {
            this.name=name;
        }
    }
    private String fileName;
    private FileType type;
    private long size;
    private LocalDateTime lastModifiedTime;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public FileInfoClient(Path path) {
        this.fileName = String.valueOf(path.getFileName());
        this.type = Files.isDirectory(path) ? FileType.DIR:FileType.FILE;
        try {
            if(this.type == FileType.DIR)
                this.size = -1;
            else
                this.size = Files.size(path);
            this.lastModifiedTime = LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(), ZoneOffset.ofHours(0));

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
