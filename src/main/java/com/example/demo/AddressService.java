package com.example.demo;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public void saveAddressesFromCSV(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVParser csvParser = CSVFormat.DEFAULT.parse(reader)) {

            for (CSVRecord record : csvParser) {
                Address address = new Address();
                address.setPostalCode(record.get(2));
                address.setAddress(record.get(3) + record.get(4) + record.get(5));
                addressRepository.save(address);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV file", e);
        }
    }
}

