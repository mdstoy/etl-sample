package com.example.demo;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    private static final String ZIP_URL = "https://www.post.japanpost.jp/zipcode/dl/utf/zip/utf_ken_all.zip"; // 日本郵便の実際のURLに置き換えてください

    public void downloadAndSaveAddresses() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(new HttpGet(ZIP_URL));
             InputStream zipInputStream = response.getEntity().getContent();
             ZipInputStream zis = new ZipInputStream(zipInputStream)) {

            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                if (!zipEntry.isDirectory() && zipEntry.getName().endsWith(".csv")) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(zis));
                         CSVParser csvParser = CSVFormat.DEFAULT.parse(reader)) {

                        for (CSVRecord record : csvParser) {
                            Address address = new Address();
                            address.setPostalCode(record.get(2)); // CSVのカラム名に置き換えてください
                            address.setAddress(record.get(3));       // CSVのカラム名に置き換えてください
                            addressRepository.save(address);
                        }
                    }
                }
                zipEntry = zis.getNextEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to download or parse CSV file", e);
        }
    }
}

