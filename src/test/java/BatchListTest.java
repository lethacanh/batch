//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class BatchListTest {
//    @Disabled
//    @Test
//    public void testt() throws Exception {
//
//        List<Integer> result = action(7);
//
//        assertThat(result)
//                .containsExactlyInAnyOrderElementsOf(getAllIds());
//
//    }
//
//    private List<Integer> action(int numberOfThreads) {
//        List<Integer> result = new ArrayList<>();
//        List<Integer> allIds = getAllIds();
//        Integer totalSize = allIds.size();
//        List<Thread> threadlist = new ArrayList<Thread>();
//        final int sizeofsublist = totalSize / numberOfThreads;
//
//        for (int i = 1; i <= numberOfThreads; i++) {
//            int firstindex = i * sizeofsublist;
//            int lastindex = i * sizeofsublist + sizeofsublist;
//            if (i == numberOfThreads)
//                lastindex = totalSize;
//
//            List<Integer> subList = allIds.subList(firstindex, lastindex);
//
//            Thread th = new Thread(() -> {
//                try {
//                    subList.stream().forEach(item -> {
//                        result.add(item + 100_000);
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//
//            threadlist.add(th);
//        }
//        threadlist.forEach(th -> {
//            th.start();
//        });
//
//        return result;
//    }
//
//    private List<Integer> getAllIds() {
//        List<Integer> ids = new ArrayList<>();
//        for (int i = 1; i < 2000; i++) {
//            ids.add(i);
//        }
//        return ids;
//    }
//}
