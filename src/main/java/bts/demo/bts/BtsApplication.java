package bts.demo.bts;

import bts.demo.bts.domain.Customer;
import bts.demo.bts.utils.HTTPDataUtil;
import bts.demo.bts.service.MysqlService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;


@SpringBootApplication
public class BtsApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(BtsApplication.class);

    @Autowired
    private HTTPDataUtil httpDataUtil;
    @Autowired
    private MysqlService mysqlService;

    public static void main(String[] args) {
        SpringApplication.run(BtsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("启动！！！");
        log.info("Connecting to remote BTS web ......");
        String token = httpDataUtil.getAuthorization();
        JSONObject transactions = httpDataUtil.sendGet("/transaction", "pageSize=50&pageNum=1&params=%7B%22orderBy%22:%22order+by+updateTime+DESC%22%7D", token);
        mysqlService.readTransaction(transactions);
        log.info("Transaction records has been saved...");
        JSONObject customers = httpDataUtil.sendGet("/customer", "pageSize=50&pageNum=1&params=%7B%22orderBy%22:%22order+by+updateTime+DESC%22%7D", token);
        mysqlService.readCustomers(customers);
        log.info("Customer records has been saved");
        readAccounts(token);
        log.info("Account records has been saved");
    }
    private void readAccounts(String  token) throws Exception{
        List<Customer> customers = mysqlService.getCustomers();
        for(Customer customer:customers){
            String idNum = customer.getIdNum();
            JSONObject accounts = httpDataUtil.sendGet("/account","IDNumber="+idNum,token);
            mysqlService.readAccount(accounts);
        }
    }

}

