package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/download-and-save")
    public ResponseEntity<String> downloadAndSaveAddresses() {
        addressService.downloadAndSaveAddresses();
        return ResponseEntity.status(HttpStatus.OK).body("Addresses downloaded and saved successfully!");
    }
}
