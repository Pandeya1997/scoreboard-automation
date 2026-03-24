package com.Scoreboard.automation.load;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.*;

public class LoadScript {

    private static final String URL = "https://scorebrdstageusr-ui.jaigovinda7.com/event001";
    private static final int TOTAL_REQUESTS = 1000;

    private static final String FILE_PATH =
            System.getProperty("user.dir") + "/src/main/resources/LoadTestResult.xlsx";

    public static void main(String[] args) throws Exception {

        List<String[]> results = new CopyOnWriteArrayList<>();

        File file = new File(FILE_PATH);
        if (file.exists()) {
            file.delete();
            System.out.println("🗑 Existing Excel file deleted");
        }

        ExecutorService executor = Executors.newFixedThreadPool(1000);
        HttpClient client = HttpClient.newHttpClient();

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(TOTAL_REQUESTS);

        for (int i = 1; i <= TOTAL_REQUESTS; i++) {
            final int requestNo = i;

            executor.submit(() -> {
                try {
                    startLatch.await();

                    long startTime = System.currentTimeMillis();

                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(URL))
                            .GET()
                            .build();

                    HttpResponse<String> response =
                            client.send(request, HttpResponse.BodyHandlers.ofString());

                    long endTime = System.currentTimeMillis();

                    results.add(new String[]{
                            String.valueOf(requestNo),
                            URL,
                            String.valueOf(response.statusCode()),
                            String.valueOf(endTime - startTime)
                    });

                    System.out.println("Request " + requestNo +
                            " | Status: " + response.statusCode());

                } catch (Exception e) {
                    results.add(new String[]{
                            String.valueOf(requestNo),
                            URL,
                            "FAILED",
                            e.getMessage()
                    });
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        System.out.println("Sending 1000 requests at once...");
        startLatch.countDown();

        finishLatch.await();
        executor.shutdown();

        createExcel(results);
        System.out.println("Excel report generated at: " + FILE_PATH);
    }

    private static void createExcel(List<String[]> results) throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Load Test Result");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Request No");
        header.createCell(1).setCellValue("URL");
        header.createCell(2).setCellValue("Status Code");
        header.createCell(3).setCellValue("Response Time (ms)");

        int rowNum = 1;
        for (String[] data : results) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < data.length; i++) {
                row.createCell(i).setCellValue(data[i]);
            }
        }

        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        FileOutputStream fos = new FileOutputStream(FILE_PATH);
        workbook.write(fos);
        fos.close();
        workbook.close();
    }
}
