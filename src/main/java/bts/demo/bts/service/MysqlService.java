package bts.demo.bts.service;

import bts.demo.bts.domain.Account;
import bts.demo.bts.domain.Customer;
import bts.demo.bts.domain.Transaction;
import bts.demo.bts.repository.AccountRepository;
import bts.demo.bts.repository.CustomerRepository;
import bts.demo.bts.repository.TransactionRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MysqlService {
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    @Autowired
    public MysqlService(TransactionRepository transactionRepository, CustomerRepository customerRepository,
                        AccountRepository accountRepository){
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
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
    public List<Customer> getCustomers(){
        return customerRepository.listAll();
    }
}
