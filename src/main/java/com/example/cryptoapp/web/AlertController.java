package com.example.cryptoapp.web;

import com.example.cryptoapp.crypto.alert.AddAlertDto;
import com.example.cryptoapp.crypto.alert.AlertDto;
import com.example.cryptoapp.crypto.alert.AlertService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alert")
public class AlertController {
    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping("")
    public ResponseEntity<AlertDto> addAlert(@RequestBody @Valid AddAlertDto dto){
        AlertDto alertDto = alertService.addAlert(dto);
        return new ResponseEntity<>(alertDto, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<AlertDto>> getUserAlerts(){
        List<AlertDto> userAlerts = alertService.getUserAlerts();
        return new ResponseEntity<>(userAlerts, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserAlert(@PathVariable Long id){
        alertService.deleteUserAlertById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/send/{id}")
    public ResponseEntity<?> sendAlert(@PathVariable Long id){
        alertService.sendAlertById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
