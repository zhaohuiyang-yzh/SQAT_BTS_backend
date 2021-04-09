package bts.demo.bts;


import bts.demo.bts.domain.Transaction;
import bts.demo.bts.repository.TransactionRepository;
import bts.demo.bts.utils.HTTPDataUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;


import javax.annotation.Resource;
import java.io.*;


@SpringBootApplication
public class BtsApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(BtsApplication.class);
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private HTTPDataUtil httpDataUtil;
    @Autowired
    private TransactionRepository transactionRepository;

    private static final String currentPath = System.getProperty("user.dir");
    private static final String fileSeparator = File.separator;

    public static void main(String[] args) {
        SpringApplication.run(BtsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("启动！！！");
        log.info("Connecting to remote BTS web ......");
        String token = httpDataUtil.getAuthorization();
        log.info("token: \n" + token);
        JSONObject transactions = httpDataUtil.sendGet("/transaction","pageSize=50&pageNum=1&params=%7B%22orderBy%22:%22order+by+updateTime+DESC%22%7D",token);
        readTransaction(transactions);
        log.info("Transaction records has been saved...");
    }

    private void readTransaction(JSONObject transactions) throws JSONException {
        JSONArray transactionList = new JSONArray(transactions.get("list").toString());
        for( int i = 0;i < transactionList.length();i++){
            JSONObject t = transactionList.getJSONObject(i);
            Transaction transaction = new Transaction();
            transaction.setAccount(t.get("account").toString());
            String amountString = t.get("amount").toString();
            transaction.setAmount(amountString.equals("null") ?0:Double.parseDouble(amountString));
            transaction.setBranchName(t.get("branchName").toString());
            transaction.setTransactionCode(t.get("transactionCode").toString());
            transaction.setTransactionNum(t.get("transactionNum").toString());
            transaction.setTransactionType(t.get("transactionType").toString());
            transaction.setOperatorTime(t.get("operatorTime").toString());
            transactionRepository.save(transaction);
        }

    }
}

