//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class BatchListTestv2 {
//
////    private List<Integer> result = Collections.synchronizedList(new ArrayList<>());
//    private List<Integer> result = (new ArrayList<>());
//
//    @Test
//    public void testt() throws Exception {
//        action(3);
//
//        assertThat(this.result)
//                .containsExactlyInAnyOrderElementsOf(
//                        getAllIds().stream()
//                                .map(item -> {
//                                    return item + 100_000;
//                                }).collect(Collectors.toList()));
//
//    }
//
//    private void action(int numberOfThreads) throws InterruptedException {
//        List<Integer> allIds = getAllIds();
//        Integer totalSize = allIds.size();
//
//        List<Callable<Void>> callableTasks = new ArrayList<>();
//
//        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
//
//        final int sizeofsublist = totalSize / numberOfThreads;
//
//        for (int i = 1; i <= numberOfThreads; i++) {
//            int firstindex = (i - 1) * sizeofsublist;
//            System.out.println("first index " + firstindex);
//            int lastindex = (i - 1) * sizeofsublist + sizeofsublist;
//            if (i == numberOfThreads)
//                lastindex = totalSize;
//
//            System.out.println("first lastindex " + lastindex);
//            List<Integer> subList = allIds.subList(firstindex, lastindex);
//
//            Callable<Void> callableTask = () -> {
//
//                change(subList);
//                return null;
//            };
//            callableTasks.add(callableTask);
//        }
//        executorService.invokeAll(callableTasks);
//        executorService.shutdown();
//    }
//
//    private void change(List<Integer> subList) {
//        subList.forEach(item -> {
//            result.add(item + 100_000);
//        });
//    }
//
//    private List<Integer> getAllIds() {
//        List<Integer> ids = new ArrayList<>();
//        for (int i = 1; i < 80; i++) {
//            ids.add(i);
//        }
//        return ids;
//    }
//}
