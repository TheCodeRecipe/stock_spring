package com.stockproject.stock_analysis.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class FileDownloadService {

    @Value("${file.download.path}")
    private String downloadPath;
    
    @Value("${python.server.url}")
    private String pythonServerUrl;

    public void downloadAndProcessFiles() throws Exception {
    	String fileUrl = pythonServerUrl + "/download/korea-analysis-combined";
        String zipFileUrl = pythonServerUrl + "/download/folder";
        String csvFileName = "korea_analysis_combined.csv";
        String zipFileName = "korea_stocks_data_parts.zip";
        String extractFolderName = "korea_stocks_data_parts";

        // 1. 파일 다운로드: korea_analysis_combined.csv
        downloadFile(fileUrl, csvFileName);

        // 2. 기존 폴더 삭제: korea_stocks_data_parts
        File existingFolder = new File(downloadPath, extractFolderName);
        if (existingFolder.exists()) {
            deleteFolder(existingFolder);
        }

        // 3. ZIP 파일 다운로드
        downloadFile(zipFileUrl, zipFileName);

        // 4. ZIP 파일 압축 해제
        unzipFile(zipFileName, extractFolderName);
    }

    private void downloadFile(String fileUrl, String fileName) throws IOException {
        InputStream inputStream = new URL(fileUrl).openStream();
        File directory = new File(downloadPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(directory, fileName);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            StreamUtils.copy(inputStream, outputStream);
        }
    }

    private void unzipFile(String zipFileName, String extractFolderName) throws IOException {
        File zipFile = new File(downloadPath, zipFileName);
        File extractDir = new File(downloadPath, extractFolderName);
        if (!extractDir.exists()) {
            extractDir.mkdirs();
        }

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                File extractedFile = new File(extractDir, entry.getName());
                if (entry.isDirectory()) {
                    extractedFile.mkdirs();
                } else {
                    try (FileOutputStream fos = new FileOutputStream(extractedFile)) {
                        StreamUtils.copy(zipInputStream, fos);
                    }
                }
                zipInputStream.closeEntry();
            }
        }
    }

    private void deleteFolder(File folder) {
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                deleteFolder(file);
            }
        }
        folder.delete();
    }
    
    public int callPythonServer(String pythonUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(pythonUrl).openConnection();
        connection.setRequestMethod("POST");
        return connection.getResponseCode();
    }
}
