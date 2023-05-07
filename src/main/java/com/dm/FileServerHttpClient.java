package com.dm;

import com.dm.dto.FileList;
import com.google.gson.Gson;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CountingInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class FileServerHttpClient {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());


    public FileList index() {
        var client = HttpClient.newHttpClient();

        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/files/index"))
                .GET()
                .build();

        try {
            var response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            FileList obj = new Gson().fromJson(response.body(), FileList.class);
            return obj;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void download(String file) {
        URL url = null;
        long completeFileSize = 0;
        try {
            url = new URL(String.format("http://localhost:8080/api/files/get/%s", file));
            HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
            httpConnection.setRequestMethod("GET");
            completeFileSize = httpConnection.getContentLength();
            if (completeFileSize == -1) {
                throw new FileNotFoundException(String.format("File not found: %s", file));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStream inputStream = Objects.requireNonNull(url).openStream();
             CountingInputStream cis = new CountingInputStream(inputStream);
             FileOutputStream fos = new FileOutputStream(String.format("downloads/%s", file));
             ProgressBar pb = new ProgressBarBuilder()
                     .showSpeed(new DecimalFormat("#.##"))
                     .setUnit("k", 1000)
                     .setInitialMax(Math.floorDiv(completeFileSize, 1000))
                     .build()) {

            pb.setExtraMessage(" Downloading...");

            new Thread(() -> {
                try {
                    IOUtils.copyLarge(cis, fos);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            while (cis.getByteCount() < completeFileSize) {
                pb.stepTo(Math.floorDiv(cis.getByteCount(), 1000));
            }
            pb.stepTo(Math.floorDiv(cis.getByteCount(), 1000));
            LOGGER.info("File {} downloaded successfully!", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parallelDownload(List<String> files) {
        List<CompletableFuture<Void>> futuresList = new ArrayList<>();
        for (String file : files) {
            futuresList.add(CompletableFuture.supplyAsync(() -> {
                LOGGER.info("Download file {} on thread {}", file, Thread.currentThread().getName());
                this.download(file);
                return null;
            }));
        }
        CompletableFuture.allOf(futuresList.toArray(new CompletableFuture[0])).join();
    }

}

