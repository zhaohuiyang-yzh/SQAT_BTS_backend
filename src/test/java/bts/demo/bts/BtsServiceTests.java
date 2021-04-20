package bts.demo.bts;

import bts.demo.bts.domain.Account;
import bts.demo.bts.domain.Customer;
import bts.demo.bts.domain.Plan;
import bts.demo.bts.repository.AccountRepository;
import bts.demo.bts.repository.PlanRepository;
import bts.demo.bts.service.BtsService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BtsServiceTests {
    @Autowired
    BtsService btsService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PlanRepository planRepository;

    @Test
    @Order(1)
    void login() {
        boolean result = btsService.login("JT2104073056", "imbus123");
        assertTrue(result);
        result = btsService.login("JW", "imbus123");
        assertFalse(result);
        result = btsService.login("JT2104073056", "123");
        assertFalse(result);
        assertNotEquals(0, btsService.getTransactionInfo().length);
    }

    @Test
    @Order(2)
    void getter() {
        Customer customer = btsService.verifyCustomer("100000000000000");
        assertEquals(21, customer.getId());
        assertNull(btsService.verifyCustomer("1"));
        assertEquals(1, btsService.getAccounts(customer.getCode()).length);
        assertNull(btsService.getAccounts("lcz01202104079"));
        assertEquals(3, btsService.getLoans("6161754432644526488").length);
        assertNull(btsService.getLoans("6161754432644526489"));
        assertEquals(10, btsService.getBills("L2104071504571").length);
        assertNull(btsService.getBills("L2104071504573"));
        assertEquals(7, btsService.getProductList().length);
    }

    @Test
    @Order(3)
    void product() {
        Account account = accountRepository.findByAccountNum("6161754432644526488");
        Plan plan = planRepository.findByIouNumAndPlanNum("L2103071504571", 1);
        assertTrue(btsService.hasFine(account.getAccountNum()));
        planRepository.save(plan);
        double balance = account.getBalance();
        assertEquals(2, btsService.judgeCreditLevel(account.getAccountNum()));
        account.setBalance(900000);
        accountRepository.save(account);
        assertEquals(1, btsService.judgeCreditLevel(account.getAccountNum()));
        account.setBalance(0);
        accountRepository.save(account);
        assertEquals(3, btsService.judgeCreditLevel(account.getAccountNum()));
        assertFalse(btsService.hasFine(account.getAccountNum()));
        assertFalse(btsService.buyProduct(account.getAccountNum(), "广发价值领先混合", "2021-04-07", 10, 1));
        account.setBalance(1000);
        accountRepository.save(account);
        assertTrue(btsService.buyProduct(account.getAccountNum(), "广发价值领先混合", "2021-04-07", 10, 1));
        assertEquals(1, btsService.getProductList(account.getAccountNum()).length);
        account.setBalance(balance);
        accountRepository.save(account);
    }

    @Test
    @Order(4)
    void pay() {
        Plan plan1 = planRepository.findByIouNumAndPlanNum("L2103071504571", 1);
        Plan plan2 = planRepository.findByIouNumAndPlanNum("L2103071504571", 2);
        assertTrue(btsService.payFine("6161754432644526488", "L2103071504571", 1));
        assertFalse(btsService.payFine("6161754432644526488", "L2103071504571", 2));
        assertTrue(btsService.repay("6161754432644526488", "L2103071504571", 1, 5000));
        assertTrue(btsService.repay("6161754432644526488", "L2103071504571", 2, 200));
        assertTrue(btsService.repay("6161754432644526488", "L2103071504571", 2, 4700));
        Account account = accountRepository.findByAccountNum("6161754432644526488");
        double balance = account.getBalance();
        account.setBalance(0);
        accountRepository.save(account);
        assertFalse(btsService.repay("6161754432644526488", "L2103071504571", 2, 50));
        planRepository.save(plan1);
        planRepository.save(plan2);
        account.setBalance(balance);
        accountRepository.save(account);
    }

    @Test
    @Order(5)
    void autoRepay() {
        Account account = accountRepository.findByAccountNum("6161754432644526488");
        double balance = account.getBalance();
        account.setBalance(0);
        accountRepository.save(account);
        assertFalse(btsService.autoRepay("2022-02-08"));
        account.setBalance(balance);
        accountRepository.save(account);
        assertTrue(btsService.autoRepay("2021-04-06"));
        assertTrue(btsService.autoRepay("2021-08-07"));
        assertTrue(btsService.autoRepay("2022-02-08"));
    }

    @Test
    @Order(6)
    void dateTest() {
        btsService.afterDate("", "2021-01-01");
    }

}
