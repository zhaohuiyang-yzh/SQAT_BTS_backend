package bts.demo.bts;

import bts.demo.bts.domain.Customer;
import bts.demo.bts.domain.Loan;
import bts.demo.bts.service.MysqlService;
import bts.demo.bts.utils.DataInFileUtil;
import bts.demo.bts.utils.HTTPDataUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.List;


@SpringBootApplication
public class BtsApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(BtsApplication.class);

    @Value("${financialProduct}")
    private String financialProduct;
    @Value("${users}")
    private String usersFile;
    @Autowired
    private HTTPDataUtil httpDataUtil;
    @Autowired
    private MysqlService mysqlService;

    private static final String PROJECT_PATH = System.getProperty("user.dir");
    private static final String FILE_SEPARATOR = File.separator;
    private static final String FILE_DIR = PROJECT_PATH + FILE_SEPARATOR + "src" + FILE_SEPARATOR + "main" + FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "static" + FILE_SEPARATOR;

    public static void main(String[] args) {
        SpringApplication.run(BtsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Start！！！");
        JSONArray users = DataInFileUtil.readFile(FILE_DIR + usersFile);
        mysqlService.readUser(users);
        log.info("Connecting to remote BTS web ......");
        String token = httpDataUtil.getAuthorization();
        log.info("Connect success!");
        JSONObject transactions = httpDataUtil.sendGet("/transaction", "pageSize=50&pageNum=1&params=%7B%22orderBy%22:%22order+by+updateTime+DESC%22%7D", token);
        mysqlService.readTransaction(transactions);
        log.info("Transaction records have been saved...");
        JSONObject customers = httpDataUtil.sendGet("/customer", "pageSize=50&pageNum=1&params=%7B%22orderBy%22:%22order+by+updateTime+DESC%22%7D", token);
        mysqlService.readCustomers(customers);
        log.info("Customer records have been saved");
        readAccounts(token);
        log.info("Account records has been saved");
        JSONObject loans = httpDataUtil.sendGet("/loan", "pageSize=50&pageNum=1&params=%7B%22orderBy%22:%22order+by+dueDate+DESC%22%7D", token);
        mysqlService.readLoan(loans, token);
        log.info("Loan records have been saved");
        readPlans(token);
        log.info("Plan records have been saved");
        JSONArray financialProducts = DataInFileUtil.readFile(FILE_DIR + financialProduct);
        mysqlService.readFinalProduct(financialProducts);
        log.info("Financial product records have been saved");
        mysqlService.computeFine();
        log.info("Fine computing has completed");
    }

    private void readAccounts(String token) throws Exception {
        List<Customer> customers = mysqlService.getCustomers();
        for (Customer customer : customers) {
            String idNum = customer.getIdNum();
            JSONObject accounts = httpDataUtil.sendGet("/account", "IDNumber=" + idNum, token);
            mysqlService.readAccount(accounts);
        }
    }

    private void readPlans(String token) throws Exception {
        List<Loan> loans = mysqlService.getLoans();
        for (Loan loan : loans) {
            String iouNum = loan.getIouNum();
            JSONObject plans = httpDataUtil.sendGet("/loan/plan", "iouNum=" + iouNum, token);
            mysqlService.readPlan(plans);
        }
    }
}

