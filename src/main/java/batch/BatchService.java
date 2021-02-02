package batch;

import com.google.common.base.Stopwatch;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Service
public class BatchService {
    private final SavingsAccountRepository repository;
    private final SavingsAccountService savingsAccountDomainService;

    public BatchService(SavingsAccountRepository repository, SavingsAccountService savingsAccountDomainService) {
        this.repository = repository;
        this.savingsAccountDomainService = savingsAccountDomainService;
    }

    @Transactional
    public void increaseBalanceAll(int numberOfThreads, int itemPerBatch) throws InterruptedException{
        List<Long> allIds = repository.allIds();

        List<Callable<Void>> callableTasks = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);


        for (int threadIndex = 1; threadIndex <= numberOfThreads; threadIndex++) {
            List<Long> subList = subListChiaDeuThread(allIds, numberOfThreads, threadIndex);

            Callable<Void> callableTask = () -> {

                increaseBalance(subList, itemPerBatch);
                return null;
            };
            callableTasks.add(callableTask);
        }
        executorService.invokeAll(callableTasks);
        executorService.shutdown();
    }

    @Transactional
    public void increaseBalanceAll() throws InterruptedException{
        List<Long> allIds = repository.allIds();
        increaseBalance(allIds,100);

    }

    private List<Long> subListChiaDeuThread(List<Long> allIds, int numberOfThreads, int threadNo) {
        int lastThreadNo = numberOfThreads;
        final int sizeOfSubList = allIds.size() / numberOfThreads;
        int firstIndex = (threadNo - 1) * sizeOfSubList;
        int lastIndex = (threadNo - 1) * sizeOfSubList + sizeOfSubList;
        if (threadNo == lastThreadNo)
            lastIndex = allIds.size();

        return allIds.subList(firstIndex, lastIndex);
    }

    private void increaseBalance(List<Long> list, int itemPerBatch) {
        list.forEach(savingsAccountId -> {
            SavingsAccount savingsAccount = repository.findById(savingsAccountId).orElseThrow(RuntimeException::new);
            savingsAccount.setBalance(savingsAccount.balance().add(BigDecimal.valueOf(100_000)));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repository.save(savingsAccount);
        });
    }

    @PostConstruct
    void runWithMultiThread() throws InterruptedException{
        setupData();
        Stopwatch stopwatch = Stopwatch.createStarted();
        increaseBalanceAll(7, 3);
        long millis = stopwatch.elapsed(MILLISECONDS);

        System.out.println("time multi Thread: " + stopwatch);

        validate();
    }

    @PostConstruct
    void runOneThread() throws InterruptedException{
        setupData();
        Stopwatch stopwatch = Stopwatch.createStarted();
        increaseBalanceAll();
        long millis = stopwatch.elapsed(MILLISECONDS);

        System.out.println("time one Thread: " + stopwatch);

        validate();
    }

    @PostConstruct
    void ensureMultiThreadCorrectOnNotEquallyDistributed() throws InterruptedException{
        setupData();
        Stopwatch stopwatch = Stopwatch.createStarted();
        increaseBalanceNotEquallyDistributedPerThread();
        long millis = stopwatch.elapsed(MILLISECONDS);

        System.out.println("time NotEquallyDistributedPerThread: " + stopwatch);

        validate();
    }

    private void increaseBalanceNotEquallyDistributedPerThread() throws InterruptedException {
        System.out.println("NotEquallyDistributedPerThread finish");
        List<Long> allIds = repository.allIds();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        List<Callable<Void>> callableTasks = new ArrayList<>();


        Callable<Void> callableTask1 = () -> {
            increaseBalance(allIds.subList(0,90), 1);
            Thread.sleep(500);
            return null;
        };

        Callable<Void> callableTask2 = () -> {
            increaseBalance(allIds.subList(90,98), 1);
            return null;
        };
        Callable<Void> callableTask3 = () -> {
            increaseBalance(allIds.subList(98,100), 1);
            return null;
        };
        callableTasks.add(callableTask1);
        callableTasks.add(callableTask2);
        callableTasks.add(callableTask3);

        executorService.invokeAll(callableTasks);
        executorService.shutdown();
    }

    private void validate() {
        List<SavingsAccount> all = repository.findAll();
        all.forEach(item -> {
            if(validBalance(item)){
//                System.out.println("OK!");
            } else {
                System.out.println("!!!!!!!!!!NOK!");

                throw new RuntimeException();
            }
        });
        System.out.println("Everyhing is OK!");
    }

    private boolean validBalance(SavingsAccount item) {
        return item.balance().compareTo(BigDecimal.valueOf(101_000)) < 0 &&
                item.balance().compareTo(BigDecimal.valueOf(100_000)) > 0;
    }


    @Transactional
    void setupData() throws InterruptedException {
        repository.deleteAll();
        for (int i = 1; i <= 100; i++) {
            SavingsAccount acc = new SavingsAccount(
                    BigDecimal.valueOf(i));
            repository.save(acc);
        }


    }
}
