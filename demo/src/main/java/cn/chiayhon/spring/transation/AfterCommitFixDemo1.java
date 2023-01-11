package cn.chiayhon.spring.transation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class AfterCommitFixDemo1 {

    @Autowired
    private FixServiceA1 fixServiceA1;

    /*
     * Test for after commit
     * */
    public void test() {
        fixServiceA1.execute();
    }


}

@Service
@Slf4j
class FixServiceA1 {

    @Autowired
    private FixServiceC1 fixServiceC1;

    @Transactional
    public void execute() {
        log.info("the service A is executing");

        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    log.info("the transaction started by service A was committed");
                    //  create a new thread for starting a new transaction
                    new Thread(() -> fixServiceC1.execute()).start();
                }
            });
        }
    }

}

@Service
@Slf4j
class FixServiceC1 {

    @Transactional
    public void execute() {
        log.info("the service C is executing");
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    log.info("the transaction started by service C was committed");
                }
            });
        }
    }

}
