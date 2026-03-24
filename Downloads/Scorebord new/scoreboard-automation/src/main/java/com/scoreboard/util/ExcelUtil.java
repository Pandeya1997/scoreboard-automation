package com.scoreboard.util;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelUtil {
    Logger logger = Logger.getLogger(ExcelUtil.class);

    public Map<String, List<Object>> excelData(String fileName, String path, String sheetName) {
        Map<String, List<Object>> map = new HashMap<>();
        try (FileInputStream fileInputStream = new FileInputStream(path);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.err.println("Sheet " + sheetName + " not found in file " + fileName);
                return map;
            }
            Iterator<Row> rowIterator = sheet.iterator();
            if (!rowIterator.hasNext()) return map;

            Row headerRow = rowIterator.next();
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                String header = cell.getStringCellValue();
                headers.add(header);
                map.put(header, new ArrayList<>());
            }
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    map.get(headers.get(i)).add(cell.toString());
                }
            }
        } catch (IOException e) {
            logger.error("Unable to find or read the excel file ::" + fileName + " on " + path);
        }
        return map;
    }
}
