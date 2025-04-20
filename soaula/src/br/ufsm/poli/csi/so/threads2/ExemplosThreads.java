package br.ufsm.poli.csi.so.threads2;

import java.util.Random;

public class ExemplosThreads {
    public static void main(String[] args) throws InterruptedException {
        for (long i = 0; i < 1_000_000_000_000L; i++) {
            long finalI = i;
            Thread.ofVirtual().name("t-"+i).start(() -> {
                try {
                    Thread.sleep(1_000_000_000);
                } catch (InterruptedException e) { }
            });
            System.out.println("i = " + finalI);
        }

    }
}
