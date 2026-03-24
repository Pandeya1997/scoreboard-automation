package com.scoreboard.util;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ReadResources {

    private final Logger logger = Logger.getLogger(ReadResources.class);

    private Document domDocument;
    private String resourceFolder = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources";

    public Map<String, String> getValuesFromXml(String fileName, String tagName) {
        domDocument = parseXMl(fileName);
        Map<String, String> elementXml = new HashMap<>();
        NodeList nodeList = domDocument.getElementsByTagName(tagName);
        for (int j = 0; j < nodeList.item(0).getChildNodes().getLength(); j++) {
            if (j % 2 != 0) {
                elementXml.put(nodeList.item(0).getChildNodes().item(j).getNodeName(),
                        nodeList.item(0).getChildNodes().item(j).getTextContent());
            }
        }
        return elementXml;
    }

    private Document parseXMl(String fileName) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            domDocument = builder.parse(getFilePath(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return domDocument;
    }

    public String getFilePath(String fileName) {
        String sPath = null;
        if (fileName.contains(".json")) {
            sPath = System.getProperty("user.dir") + "\\src\\main\\resources\\jsons\\" + fileName;
        } else {
            sPath = System.getProperty("user.dir") + "\\src\\main\\resources\\" + fileName;
        }
        sPath = sPath.replace('\\', '/');
        File file = new File(sPath);
        if (file.exists()) {
            logger.info("The File is Present wih the Path" + sPath);
        } else {
            logger.error("File not Found");
        }
        return sPath;
    }

    public String getResourceFolder() {
        return resourceFolder;
    }

    public void setResourceFolder(String resourceFolder) {
        this.resourceFolder = resourceFolder;
    }

    public Map<String, String> getValuesFromProperties(String fileName) throws IOException {
        Map<String, String> elementProperties = new HashMap<String, String>();
        ;
        Properties properties = new Properties();
        InputStream inputStream = new FileInputStream(new ReadResources().getFilePath((fileName)));
        properties.load(inputStream);

        Enumeration<Object> allKeys = properties.keys();
        while (allKeys.hasMoreElements()) {
            String key = (String) allKeys.nextElement();
            elementProperties.put(key.trim(), properties.getProperty(key).trim());
        }
        return elementProperties;
    }

    public List<Path> getAllPropertiesFiles() throws IOException {
        return getAllFilesInFolder(resourceFolder).stream().filter(path -> path.getFileName().toString().contains(".properties")).collect(Collectors.toList());
    }

    public List<Path> getAllFilesInFolder(String path) throws IOException {
        return Files.walk(Paths.get(path))
                .filter(Files::isRegularFile).collect(Collectors.toList());
    }

    private static final Map<String, Properties> propertyCache = new HashMap<>();

    public String getDataFromPropertyFile(String fileName, String key) {
        Properties properties = propertyCache.computeIfAbsent(fileName, f -> {
            String filePath = System.getProperty("user.dir") + "/src/main/resources/" + f + ".properties";
            try (FileInputStream fis = new FileInputStream(filePath)) {
                Properties prop = new Properties();
                prop.load(fis);
                return prop;
            } catch (IOException e) {
                logger.error("Failed to load properties from: " + filePath, e);
                throw new RuntimeException(e);
            }
        });

        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Key '" + key + "' not found in file: " + fileName);
            return "";
        }
        return value.trim();
    }

}
