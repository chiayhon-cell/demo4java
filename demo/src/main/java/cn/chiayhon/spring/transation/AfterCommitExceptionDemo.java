package cn.chiayhon.spring.transation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class AfterCommitExceptionDemo {

    @Autowired
    private ServiceA serviceA;

    /*
     * Test for after commit
     * */
    public void test() {
        serviceA.execute();
    }


}


/**
 * There are 3 service(A, B, C). Service A have a member variable service B and have a method with annotation @Transactional.
 * After the transaction started by service A committed, the service B will execute its method.
 * <p>
 * Service B have a member variable service C and have a method without annotation @Transactional.
 * At the end of the method of service B, the method of service C will be executed.
 * <p>
 * The service C don't have member variable but have a method with annotation @Transactional.
 * After the transaction started by service C committed, the final code will be executed.
 * <p>
 * it will cause that the @Transactional in the method of service C don't work;
 */

@Service
@Slf4j
class ServiceA {

    @Autowired
    private ServiceB serviceB;

    @Transactional
    public void execute() {
        log.info("the service A is executing");

        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            // register the transaction synchronization to the synchronization manager
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    log.info("the transaction started by service A was committed");
                    serviceB.execute();
                }
            });
        }
    }

}

@Service
@Slf4j
class ServiceB {

    @Autowired
    private ServiceC serviceC;

    public void execute() {
        log.info("the service B is executing");
        serviceC.execute();
    }

}

@Service
@Slf4j
class ServiceC {

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
