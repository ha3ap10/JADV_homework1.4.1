package ru.netology;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class CallCenter {

    // Выбрал из-за неблокирующей работы
    // был ещё вариант LinkedBlockingQueue из-за двух ReentrantLock

    public Queue<Call> callsQueue = new ConcurrentLinkedQueue<>();
    private static final int WAIT_CALLS = 3; //подождать поступления звонков
    private static final int CALLS_IN_SECOND = 33;
    private static final int REPEAT_COUNT = 10;
    private static final int WAITING_TIME = 1000;
    private static final int ANSWERING_CALL = 3000;
    private static final int ANSWERING_CALL_RND = 2000;
    private AtomicInteger callsCount = new AtomicInteger(0);
    private ThreadLocal<Integer> answeredCalls = ThreadLocal.withInitial(() -> 0);

    public void runPBX() {
        for (int j = 0; j < REPEAT_COUNT; j++) {
            for (int i = 0; i < CALLS_IN_SECOND; i++) {
                callsQueue.offer(new Call("Звонок " + callsCount.incrementAndGet()));
            }
            System.out.printf("\nПоступило %d новых звонков\n", CALLS_IN_SECOND);
            try {
                Thread.sleep(WAITING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void expert() {
        Random random = new Random();
        int intRandom = random.nextInt(ANSWERING_CALL_RND);

        String expertName = Thread.currentThread().getName();

        while (callsCount.get() < WAIT_CALLS);
        while (!callsQueue.isEmpty()) {
            System.out.printf("%s взял в работу %s\n",
                    expertName,
                    callsQueue.poll().getName());
            try {
//                Thread.sleep(ANSWERING_CALL);
                Thread.sleep(ANSWERING_CALL + intRandom);
                System.out.printf("%s ответил клиенту\n",
                        expertName);
                answeredCalls.set(answeredCalls.get() + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.printf("%s закончил работать, ответил на %d звонков.\n", expertName, answeredCalls.get());
    }
}
