package bts.demo.bts.controller;

import bts.demo.bts.service.BtsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BtsController {
    @Autowired
    private BtsService btsService;
    @PostMapping("/getTransactionInfo")
    public ResponseEntity<Map<String, Object>> getTransactionInfo(){
        Map<String, Object> response = new HashMap<>();
        response.put("transactionList",btsService.getTransactionInfo());
        return ResponseEntity.ok(response);
    }

}
