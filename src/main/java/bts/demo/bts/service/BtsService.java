package bts.demo.bts.service;

import bts.demo.bts.domain.Transaction;
import bts.demo.bts.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BtsService {
    private final TransactionRepository transactionRepository;
    @Autowired
    public BtsService (TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }
    public Map[] getTransactionInfo(){
        List<Transaction> transactionList = transactionRepository.listAll();
        Map[] infoList = new Map[transactionList.size()];
        int i = 0;
        for (Transaction transaction:transactionList){
            Map<String,Object> transactionMap = new HashMap<>();
            transactionMap.put("transactionNum",transaction.getTransactionNum());
            transactionMap.put("branchName",transaction.getBranchName());
            transactionMap.put("transactionType",transaction.getTransactionType());
            transactionMap.put("amount",transaction.getAmount());
            transactionMap.put("account",transaction.getAccount());
            transactionMap.put("operatorTime",transaction.getOperatorTime());
            transactionMap.put("transactionCode",transaction.getTransactionCode());
            infoList[i] = transactionMap;
            i++;
        }
        return infoList;
    }
}
