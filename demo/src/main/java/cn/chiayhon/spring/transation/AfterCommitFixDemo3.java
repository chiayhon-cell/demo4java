package cn.chiayhon.spring.transation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class AfterCommitFixDemo3 {

    @Autowired
    private FixServiceA3 fixServiceA3;

    /*
     * Test for after commit
     * */
    public void test() {
        fixServiceA3.execute();
    }

}


@Service
@Slf4j
class FixServiceA3 {

    @Autowired
    private FixServiceB3 fixServiceB3;

    @Transactional
    public void execute() {
        log.info("the service A is executing");

        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    log.info("the transaction started by service A was committed");
                    //  create a new thread for starting a new transaction
                    fixServiceB3.execute();
                }
            });
        }
    }

}


@Service
@Slf4j
class FixServiceB3 {

    @Autowired
    private FixServiceC3 FixServiceC3;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void execute() {
        log.info("the service B is executing");
        FixServiceC3.execute();
    }

}

@Service
@Slf4j
class FixServiceC3 {

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
