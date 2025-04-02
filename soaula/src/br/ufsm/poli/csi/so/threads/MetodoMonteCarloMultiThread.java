package br.ufsm.poli.csi.so.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MetodoMonteCarloMultiThread {
    Random r = new Random();
    private List<ThreadCalculoPontos> threads = new ArrayList<>();

    public static void main(String[] args) {
      new MetodoMonteCarloMultiThread();
    }

    public MetodoMonteCarloMultiThread() {
        for (long i = 0; i < 6; i++) {
            ThreadCalculoPi calculoPi = new ThreadCalculoPi();
            Thread.ofPlatform().start(new ThreadCalculoPi());
        }
    }

    private class ThreadCalculoPi implements Runnable {
        @Override
        public void run() {
            long start = System.currentTimeMillis();
            while (true) {
                try {
                    Thread.sleep(10_000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                long totalPontos = 0;
                long pontosDentro = 0;
                for(ThreadCalculoPontos calculoPi : threads) {
                    totalPontos += calculoPi.totalPontos;
                    pontosDentro +=calculoPi.pontosDentro;
                }
                long milis = System.currentTimeMillis() - start;
                double pi = 4.0 * pontosDentro / totalPontos;
                System.out.println("PI - " + pi + " / tempo = " + milis + "ms");
            }
        }
    }

    private class ThreadCalculoPontos implements Runnable {
        public long totalPontos = 0;
        public long pontosDentro = 0;
        public void run() {
            for (long i = 0; i < 10_000_000_000L; i++) {
                while (true) {
                    double x = r.nextDouble();
                    double y = r.nextDouble();
                    if (x*x + y*y <= 1) {
                        pontosDentro++;
                    }
                    totalPontos++;
                }

            }
        }
    }
}
