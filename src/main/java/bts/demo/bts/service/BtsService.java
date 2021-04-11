package bts.demo.bts.service;

import bts.demo.bts.domain.*;
import bts.demo.bts.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BtsService {
    private final TransactionRepository transactionRepository;
    private final DataUserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final LoanRepository loanRepository;
    private final PlanRepository planRepository;
    private static final Map<String, String> Transaction_TYPE_Enum = new HashMap<>();
    private static final String BRANCH_NAME = "吕昌泽的机构";

    static {
        Transaction_TYPE_Enum.put("2006", "到期柜面还款");
        Transaction_TYPE_Enum.put("2005", "到期批量还款");
        Transaction_TYPE_Enum.put("8765", "购买理财产品");
        Transaction_TYPE_Enum.put("2009", "缴纳罚款");
    }

    @Autowired
    public BtsService(TransactionRepository transactionRepository, DataUserRepository userRepository,
                      CustomerRepository customerRepository, AccountRepository accountRepository,
                      LoanRepository loanRepository, PlanRepository planRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.loanRepository = loanRepository;
        this.planRepository = planRepository;
    }

    public Map[] getTransactionInfo() {
        List<Transaction> transactionList = transactionRepository.listAll();
        Map[] infoList = new Map[transactionList.size()];
        int i = 0;
        for (Transaction transaction : transactionList) {
            Map<String, Object> transactionMap = new HashMap<>();
            transactionMap.put("transactionNum", transaction.getTransactionNum());
            transactionMap.put("branchName", transaction.getBranchName());
            transactionMap.put("transactionType", transaction.getTransactionType());
            transactionMap.put("amount", transaction.getAmount());
            transactionMap.put("account", transaction.getAccount());
            transactionMap.put("operatorTime", transaction.getOperatorTime());
            transactionMap.put("transactionCode", transaction.getTransactionCode());
            infoList[i] = transactionMap;
            i++;
        }
        return infoList;
    }

    public Boolean login(String username, String password) {
        DataUser user = userRepository.findByUsername(username);
        if (user == null)
            return false;
        return password.equals(user.getPassword());
    }

    public Customer verifyCustomer(String idNum) {
        return customerRepository.findByIdNum(idNum);
    }

    public Map[] getAccounts(String customerCode) {
        Customer customer = customerRepository.findByCode(customerCode);
        List<Account> accountList = accountRepository.findAllByCustomerId(customer.getId());
        Map[] response = new Map[accountList.size()];
        int i = 0;
        for (Account account : accountList) {
            Map<String, Object> map = new HashMap<>();
            map.put("accountNum", account.getAccountNum());
            map.put("voucherName", account.getVoucherName());
            map.put("accountTypeName", account.getAccountTypeName());
            map.put("balance", account.getBalance());
            map.put("state", account.getState());
            map.put("updateTime", account.getUpdateTime());
            response[i] = map;
            i++;
        }
        return response;
    }

    public Map[] getLoans(String accountNumber) {
        List<Loan> loanList = loanRepository.findAllByAccountNum(accountNumber);
        Map[] response = new Map[loanList.size()];
        int i = 0;
        for (Loan loan : loanList) {
            Map<String, Object> map = new HashMap<>();
            map.put("iouNum", loan.getIouNum());
            map.put("customerCode", loan.getCustomerCode());
            map.put("customerName", loan.getCustomerName());
            map.put("loanStatus", loan.getLoanStatus());
            map.put("productName", loan.getProductName());
            map.put("productCode", loan.getProductCode());
            map.put("overdueBalance", loan.getOverdueBalance());
            map.put("createTime", loan.getCreateTime());
            response[i] = map;
            i++;
        }
        return response;
    }

    public Map[] getBills(String iouNum) {
        List<Plan> billList = planRepository.findAllByIouNum(iouNum);
        Map[] response = new Map[billList.size()];
        int i = 0;
        for (Plan plan : billList) {
            Map<String, Object> map = new HashMap<>();
            map.put("planNum", plan.getPlanNum());
            map.put("remainPrincipal", plan.getRemainPrincipal());
            map.put("remainInterest", plan.getRemainInterest());
            map.put("remainAmount", plan.getRemainAmount());
            map.put("fine", plan.getPenaltyInterest());
            map.put("repaymentStatue", plan.getRepaymentStatus());
            map.put("planDate", plan.getDate());
            response[i] = map;
            i++;
        }
        return response;
    }

    public boolean payFine(String accountNum, String iouNum, int planNum) {
        Plan plan = planRepository.findByIouNumAndPlanNum(iouNum, planNum);
        double amount = plan.getPenaltyInterest();
        if (payFine(plan, accountNum)) {
            loadTransaction(accountNum, amount, BRANCH_NAME, "2009");
            return true;
        }
        return false;
    }

    private boolean payFine(Plan plan, String accountNum) {
        boolean isSuccess = plan.getRepaymentStatus() == Plan.OVERDUE && pay(accountNum, plan.getPenaltyInterest());
        if (isSuccess) {
            plan.setRepaymentStatus(Plan.UN_REPAY);
            plan.setPenaltyInterest(0);
            planRepository.save(plan);
        }
        return isSuccess;
    }

    public boolean repay(String accountNum, String iouNum, int planNum, double value) {
        Plan plan = planRepository.findByIouNumAndPlanNum(iouNum, planNum);
        if (repay(accountNum, value, plan)) {
            loadTransaction(accountNum, value, BRANCH_NAME, "2006");
            return true;
        }
        return false;
    }

    private boolean repay(String accountNum, double value, Plan plan) {
        boolean isSuccess = plan.getRepaymentStatus() == Plan.UN_REPAY && pay(accountNum, value);
        if (isSuccess) {
            double remainAmount = plan.getRemainAmount();
            double remainPrincipal = plan.getRemainPrincipal();
            double remainInterest = plan.getRemainInterest();
            if (value == remainAmount) {
                remainPrincipal = 0;
                remainInterest = 0;
                plan.setRepaymentStatus(Plan.HAVE_PAYED);
            } else if (value < remainInterest) {
                remainInterest -= value;
            } else {
                remainPrincipal = remainPrincipal - (value - remainInterest);
                remainInterest = 0;
            }
            plan.setRemainAmount(remainAmount - value);
            plan.setRemainPrincipal(remainPrincipal);
            plan.setRemainInterest(remainInterest);
            planRepository.save(plan);
        }
        return isSuccess;
    }

    private boolean pay(String accountNum, double money) {
        Account account = accountRepository.findByAccountNum(accountNum);
        double balance = account.getBalance();
        if (balance < money)
            return false;
        balance -= money;
        account.setBalance(balance);
        return true;
    }

    public boolean autoRepay() {
        boolean isSuccess = true;
        List<Plan> planList = planRepository.listAll();
        for (Plan plan : planList) {
            Loan loan = loanRepository.findByIouNum(plan.getIouNum());
            switch (plan.getRepaymentStatus()) {
                //这是有3个入口的一个执行流，break是故意没有的
                case Plan.OVERDUE:
                    if (!payFine(plan, loan.getAccountNum()))
                        isSuccess = false;
                case Plan.UN_REPAY:
                    if (!repay(loan.getAccountNum(), plan.getRemainAmount(), plan))
                        isSuccess = false;
                case Plan.HAVE_PAYED:
                    break;
            }
        }
        if (isSuccess)
            loadTransaction("", 0, BRANCH_NAME, "2005");
        return isSuccess;
    }

    private void loadTransaction(String account, double amount, String branchName, String transactionCode) {
        String type = Transaction_TYPE_Enum.get(transactionCode);
        DateTimeFormatter codeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        DateTimeFormatter storeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime dateTime = LocalDateTime.now();
        String operatorTime = dateTime.format(storeFormatter);
        String transactionNum = "lcz01" + transactionCode + "215" + dateTime.format(codeFormatter) + System.currentTimeMillis() % 10;
        Transaction transaction = new Transaction();
        transaction.setOperatorTime(operatorTime);
        transaction.setTransactionType(type);
        transaction.setTransactionNum(transactionNum);
        transaction.setTransactionCode(transactionCode);
        transaction.setBranchName(branchName);
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transactionRepository.save(transaction);
    }
}
