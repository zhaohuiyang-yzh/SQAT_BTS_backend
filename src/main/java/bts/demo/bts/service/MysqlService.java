package bts.demo.bts.service;

import bts.demo.bts.domain.*;
import bts.demo.bts.repository.*;
import bts.demo.bts.utils.HTTPDataUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class MysqlService {
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final LoanRepository loanRepository;
    private final PlanRepository planRepository;
    private final FinancialProductRepo financialProductRepo;
    private final HTTPDataUtil httpDataUtil;
    private final DataUserRepository userRepository;

    private final Logger log = LoggerFactory.getLogger(MysqlService.class);

    @Autowired
    public MysqlService(TransactionRepository transactionRepository, CustomerRepository customerRepository,
                        AccountRepository accountRepository, LoanRepository loanRepository, PlanRepository planRepository,
                        FinancialProductRepo financialProductRepo, HTTPDataUtil httpDataUtil,
                        DataUserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.loanRepository = loanRepository;
        this.planRepository = planRepository;
        this.financialProductRepo = financialProductRepo;
        this.httpDataUtil = httpDataUtil;
        this.userRepository = userRepository;
    }

    public void readTransaction(JSONObject transactions) throws JSONException {
        JSONArray transactionList = new JSONArray(transactions.get("list").toString());
        for (int i = 0; i < transactionList.length(); i++) {
            JSONObject t = transactionList.getJSONObject(i);
            Transaction transaction = new Transaction();
            transaction.setAccount(t.get("account").toString());
            String amountString = t.get("amount").toString();
            transaction.setAmount(amountString.equals("null") ? 0 : Double.parseDouble(amountString));
            transaction.setBranchName(t.get("branchName").toString());
            transaction.setTransactionCode(t.get("transactionCode").toString());
            transaction.setTransactionNum(t.get("transactionNum").toString());
            transaction.setTransactionType(t.get("transactionType").toString());
            transaction.setOperatorTime(t.get("operatorTime").toString());
            transactionRepository.save(transaction);
        }
    }
    public void readCustomers(JSONObject customers) throws JSONException{
        JSONArray customerList = new JSONArray(customers.get("list").toString());
        for(int i = 0;i < customerList.length();i++){
            JSONObject jsonObject = customerList.getJSONObject(i);
            Customer customer = new Customer();
            customer.setAddress(jsonObject.getString("address"));
            customer.setAlterNateName(jsonObject.getString("alternateName"));
            customer.setAlterNatePhone(jsonObject.getString("alternatePhone"));
            customer.setBranchNum(jsonObject.getString("branchNum"));
            customer.setCode(jsonObject.getString("code"));
            customer.setCreateTime(jsonObject.getString("createTime"));
            customer.setCreator(jsonObject.getInt("creator"));
            customer.setEmail(jsonObject.getString("email"));
            customer.setId(jsonObject.getInt("id"));
            customer.setIdNum(jsonObject.getString("idnumber"));
            customer.setIdType(jsonObject.getInt("idtype"));
            customer.setName(jsonObject.getString("name"));
            customer.setPermanentAddress(jsonObject.getString("permanentAddress"));
            customer.setPhone(jsonObject.getString("phone"));
            customer.setSex(jsonObject.getInt("sex"));
            String updaterStr = jsonObject.getString("updater");
            customer.setUpdater(updaterStr.equals("null")?0:jsonObject.getInt("updater"));
            customer.setUpdateTime(jsonObject.getString("updateTime"));
            customerRepository.save(customer);
        }
    }
    public void readAccount(JSONObject accounts) throws JSONException{
        JSONArray accountData = new JSONArray(accounts.getString("data"));
        /*account接口返回值为二维数组
        * 第一层每一项为一个customer
        * 第二层为每一个customer的所有account
        * 在请求参数为idNumber的情况下，第一层只有一项*/
        for(int i = 0;i < accountData.length();i++){
            JSONObject customerJson = accountData.getJSONObject(i);
            int customerId = customerJson.getInt("id");
            JSONArray accountList = new JSONArray(customerJson.getString("accountDtos"));
            for (int j = 0;j < accountList.length();j++){
                JSONObject accountJson = accountList.getJSONObject(j);
                Account account = new Account();
                account.setAccountNum(accountJson.getString("accountNum"));
                account.setAccountType(accountJson.getInt("accountType"));
                account.setAccountTypeName(accountJson.getString("accountTypeName"));
                account.setBalance(accountJson.getDouble("balance"));
                account.setCreateTime(accountJson.getString("createTime"));
                account.setCreator(accountJson.getInt("creator"));
                account.setCustomerId(customerId);
                account.setId(accountJson.getInt("id"));
                account.setLoanProduct(accountJson.getInt("loanProduct"));
                account.setPassword(accountJson.getString("password"));
                account.setState(accountJson.getInt("state"));
                account.setUpdateTime(accountJson.getString("updateTime"));
                account.setVoucherName(accountJson.getString("voucherName"));
                account.setVoucherType(accountJson.getInt("voucherType"));
                accountRepository.save(account);
            }
        }


    }

    public List<Customer> getCustomers() {
        return customerRepository.listAll();
    }

    public void readLoan(JSONObject loans, String token) throws Exception {
        JSONArray loanList = new JSONArray(loans.getString("list"));
        for (int i = 0; i < loanList.length(); i++) {
            JSONObject loanJson = loanList.getJSONObject(i);
            Loan loan = new Loan();
            loan.setBalance(loanJson.getDouble("balance"));
            loan.setCreateTime(loanJson.getString("createTime"));
            loan.setCreator(loanJson.getInt("creator"));
            loan.setCustomerCode(loanJson.getString("customerCode"));
            loan.setCustomerName(loanJson.getString("customerName"));
            String iouNum = loanJson.getString("iouNum");
            loan.setIouNum(iouNum);
            JSONObject iouJson = httpDataUtil.sendGet("/loan/" + iouNum, "", token);
            JSONObject iouData = iouJson.getJSONObject("data");
            loan.setAccountNum(iouData.getString("accountNum"));
            loan.setLoanStatus(loanJson.getInt("loanStatus"));
            loan.setOverdueBalance(loanJson.getDouble("overdueBalance"));
            loan.setProductCode(loanJson.getString("productCode"));
            loan.setProductName(loanJson.getString("productName"));
            loan.setRepaymentMethod(loanJson.getInt("repaymentMethod"));
            loan.setTotalAmount(loanJson.getDouble("totalAmount"));
            loan.setTotalInterest(loanJson.getDouble("totalInterest"));
            loanRepository.save(loan);
        }
    }

    public List<Loan> getLoans() {
        return loanRepository.listAll();
    }

    public void readPlan(JSONObject plans) throws Exception {
        JSONArray planData = new JSONArray(plans.getString("data"));
        for (int i = 0; i < planData.length(); i++) {
            JSONObject planJson = planData.getJSONObject(i);
            Plan plan = new Plan();
            plan.setAmount(planJson.getDouble("planAmount"));
            plan.setCreator(planJson.getInt("creator"));
            plan.setDate(planJson.getString("planDate"));
            plan.setId(planJson.getInt("id"));
            plan.setInterest(planJson.getDouble("planInterest"));
            plan.setIouNum(planJson.getString("iouNum"));
            plan.setPenaltyInterest(planJson.getDouble("penaltyInterest"));
            plan.setPrincipal(planJson.getDouble("planPrincipal"));
            plan.setRepaymentStatus(planJson.getInt("repaymentStatus"));
            plan.setPlanNum(planJson.getInt("planNum"));
            String payMethod = planJson.getString("payMethod");
            plan.setPayMethod(payMethod.equals("null") ? 0 : planJson.getInt("payMethod"));
            plan.setRemainAmount(planJson.getDouble("remainAmount"));
            plan.setRemainInterest(planJson.getDouble("remainInterest"));
            plan.setRemainPrincipal(planJson.getDouble("remainPrincipal"));
            planRepository.save(plan);
        }
    }

    public void readFinalProduct(JSONArray products) throws Exception {
        for (int i = 0; i < products.length(); i++) {
            JSONObject productJson = products.getJSONObject(i);
            FinancialProduct product = new FinancialProduct();
            product.setName(productJson.getString("productName"));
            product.setSinglePrice(productJson.getDouble("singlePrice"));
            product.setTimeLimit(productJson.getString("timeLimit"));
            product.setType(productJson.getString("type"));
            product.setYearlyBenefit(productJson.getString("yearlyBenefit"));
            financialProductRepo.save(product);
        }
    }

    public void readUser(JSONArray users) throws Exception {
        for (int i = 0; i < users.length(); i++) {
            JSONObject userJson = users.getJSONObject(i);
            DataUser user = new DataUser();
            user.setUsername(userJson.getString("username"));
            user.setPassword(userJson.getString("password"));
            userRepository.save(user);
        }
    }

    public void computeFine() throws Exception {
        Iterable<Plan> planIterable = planRepository.findAll();
        LocalDate today = LocalDate.now();
        for (Plan plan : planIterable) {
            LocalDate planDate = LocalDate.parse(plan.getDate());
            if (today.isAfter(planDate)) {
                plan.setPenaltyInterest(plan.getPrincipal() * Plan.PENALTY_RATE);
                plan.setRepaymentStatus(Plan.OVERDUE);
                planRepository.save(plan);
            }
        }
    }
}
