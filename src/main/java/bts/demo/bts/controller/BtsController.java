package bts.demo.bts.controller;

import bts.demo.bts.service.BtsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BtsController {
    @Autowired
    private BtsService btsService;

    @PostMapping("/getTransactionInfo")
    public ResponseEntity<Map<String, Object>> getTransactionInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("transactionList", btsService.getTransactionInfo());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        if (btsService.login(username, password))
            return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/verifyCustomer")
    public ResponseEntity<?> verifyCustomer(@RequestBody Map<String, String> request) {
        String idNumber = request.get("idnumber");
        if (btsService.verifyCustomer(idNumber))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/getAccounts")
    public ResponseEntity<Map<String, Object>> getAccounts(@RequestBody Map<String, String> request) {
        String customerCode = request.get("customerCode");
        Map[] accountList = btsService.getAccounts(customerCode);
        Map<String, Object> response = new HashMap<>();
        response.put("accountList", accountList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/getLoans")
    public ResponseEntity<Map<String, Object>> getLoans(@RequestBody Map<String, String> request) {
        String accountNum = request.get("accountNum");
        Map[] loanList = btsService.getLoans(accountNum);
        Map<String, Object> response = new HashMap<>();
        response.put("accountList", loanList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/getBills")
    public ResponseEntity<Map<String, Object>> getBills(@RequestBody Map<String, String> request) {
        String iouNum = request.get("iouNum");
        Map[] billList = btsService.getBills(iouNum);
        Map<String, Object> response = new HashMap<>();
        response.put("billList", billList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/payFine")
    public ResponseEntity<?> payFine(@RequestBody Map<String, String> request) {
        String accountNum = request.get("accountNum");
        String iouNum = request.get("iouNum");
        int planNum = Integer.parseInt(request.get("planNum"));
        if (btsService.payFine(accountNum, iouNum, planNum))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/repayment")
    public ResponseEntity<?> repay(@RequestBody Map<String, String> request) {
        String accountNum = request.get("accountNum");
        String iouNum = request.get("iouNum");
        int planNum = Integer.parseInt(request.get("planNum"));
        double value = Double.parseDouble(request.get("value"));
        if (btsService.repay(accountNum, iouNum, planNum, value))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/autoPayBack")
    public ResponseEntity<?> autoPay() {
        if (btsService.autoRepay())
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
