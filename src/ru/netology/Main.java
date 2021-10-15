package ru.netology;

public class Main {

    public static void main(String[] args) {

        CallCenter callCenter = new CallCenter();

        Thread pbx = new Thread(null, callCenter::runPBX, "АТС");

        Thread expert1 = new Thread(null, callCenter::expert, "Специалист 1");
        Thread expert2 = new Thread(null, callCenter::expert, "Специалист 2");
        Thread expert3 = new Thread(null, callCenter::expert, "Специалист 3");

        pbx.start();

        expert1.start();
        expert2.start();
        expert3.start();

        try {
            pbx.join();
            expert1.join();
            expert2.join();
            expert3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Рабочий день закончился!");
    }
}
