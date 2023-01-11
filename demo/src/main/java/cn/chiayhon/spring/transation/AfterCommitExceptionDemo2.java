package cn.chiayhon.spring.transation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class AfterCommitExceptionDemo2 {

    @Autowired
    private ServiceA2 serviceA2;

    /*
     * Test for after commit
     * */
    public void test() {
        serviceA2.execute();
    }


}

@Service
@Slf4j
class ServiceA2 {

    @Autowired
    private ServiceC2 serviceC2;

    @Transactional
    public void execute() {
        log.info("the service A is executing");

        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            // register the transaction synchronization to the synchronization manager
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    log.info("the transaction started by service A was committed");
                    serviceC2.execute();
                }
            });
        }
    }

}

@Service
@Slf4j
class ServiceC2 {

    @Transactional
    public void execute() {
        log.info("the service C is executing");
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    // this code don't execute
                    log.info("the transaction started by service C was committed");
                }
            });
        }
    }

}
