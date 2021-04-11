package bts.demo.bts.controller;

import bts.demo.bts.domain.Customer;
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
        Customer customer = btsService.verifyCustomer(idNumber);
        if (customer == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(customer);
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
        response.put("loanList", loanList);
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
    public ResponseEntity<?> autoPay(@RequestBody Map<String, String> request) {
        String date = request.get("date");
        if (btsService.autoRepay(date))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/getCreditLevel")
    public ResponseEntity<Map<String, Integer>> getCreditLevel(@RequestBody Map<String, String> request) {
        String accountNum = request.get("accountNum");
        Map<String, Integer> response = new HashMap<>();
        response.put("creditLevel", btsService.judgeCreditLevel(accountNum));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/getProductList")
    public ResponseEntity<Map<String, Object>> getProductsList() {
        Map[] productList = btsService.getProductList();
        Map<String, Object> response = new HashMap<>();
        response.put("productList", productList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/judgeFine")
    public ResponseEntity<?> judgeFine(@RequestBody Map<String, String> request) {
        String accountNum = request.get("accountNum");
        if (btsService.hasFine(accountNum))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/buyProduct")
    public ResponseEntity<?> buyProduct(@RequestBody Map<String, Object> request) {
        String accountNum = request.get("accountNum").toString();
        String name = request.get("productName").toString();
        String date = request.get("date").toString();
        int num = Integer.parseInt(request.get("buyInNum").toString());
        double price = Double.parseDouble(request.get("singlePrice").toString());
        if (btsService.buyProduct(accountNum, name, date, num, price))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/getAccountFinancialProduct")
    public ResponseEntity<Map<String, Object>> getAccountProductsList(@RequestBody Map<String, String> request) {
        String accountNum = request.get("accountNum");
        Map[] productList = btsService.getProductList(accountNum);
        Map<String, Object> response = new HashMap<>();
        response.put("productList", productList);
        return ResponseEntity.ok(response);
    }
}
