package br.ufsm.poli.csi.so.threads2;

import java.util.ArrayList;
import java.util.List;

public class OutroCalculo {

    //private Random rand = new Random();
    private final List<ThreadIncremento> threads = new ArrayList<>();
    private long i;
    public static void main(String[] args) {
        new OutroCalculo();
    }

    public OutroCalculo() {
        Thread.ofPlatform().start(new ConfereCalculo());
        for (int i = 0; i < 6; i++) {
            ThreadIncremento incremento = new ThreadIncremento();
            threads.add(incremento);
            Thread.ofPlatform().start(incremento);
        }
    }

    private class ConfereCalculo implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                var iLocalTotal = 0;
                for (ThreadIncremento incremento : threads) {
                    iLocalTotal += incremento.iLocal;
                }
                if (iLocalTotal != i) {
                    System.out.println("Erro: diferenca = " + (iLocalTotal - i));
                }
            }
        }
    }
    private class ThreadIncremento implements Runnable {

        private long iLocal = 0;

        @Override
        public void run() {
            while (true) {
                i++;
                iLocal++;
                if (iLocal % 100_000 == 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
