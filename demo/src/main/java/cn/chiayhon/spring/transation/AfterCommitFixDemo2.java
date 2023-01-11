package cn.chiayhon.spring.transation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class AfterCommitFixDemo2 {

    @Autowired
    private FixServiceA2 fixServiceA2;

    /*
     * Test for after commit
     * */
    public void test() {
        fixServiceA2.execute();
    }

}

@Service
@Slf4j
class FixServiceA2 {

    @Autowired
    private FixServiceC2 fixServiceC2;

    @Transactional
    public void execute() {
        log.info("the service A is executing");

        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    log.info("the transaction started by service A was committed");
                    //  create a new thread for starting a new transaction
                    fixServiceC2.execute();
                }
            });
        }
    }

}

@Service
@Slf4j
class FixServiceC2 {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
