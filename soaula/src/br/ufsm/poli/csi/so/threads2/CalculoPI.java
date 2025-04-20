package br.ufsm.poli.csi.so.threads2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CalculoPI {

    //private Random rand = new Random();
    private List<ThreadCalculoPontos> threads = new ArrayList<>();

    public static void main(String[] args) {
        new CalculoPI();
    }

    public CalculoPI() {
        Thread.ofPlatform().start(new ThreadCalculoPI());
        for (int i = 0; i < 6; i++) {
            ThreadCalculoPontos calculoPI = new ThreadCalculoPontos();
            threads.add(calculoPI);
            Thread.ofPlatform().start(calculoPI);
        }

    }

    private class ThreadCalculoPI implements Runnable {

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            while (true) {
                try { Thread.sleep(10_000); } catch (InterruptedException e) { }
                long total = 0;
                long pontosDentro = 0;
                for (ThreadCalculoPontos calculoPI : threads) {
                    total += calculoPI.totalPontos;
                    pontosDentro += calculoPI.pontosDentro;
                }
                long milis = System.currentTimeMillis() - start;
                double pi = 4.0 * pontosDentro / total;
                System.out.println("PI = " + pi + " / tempo = " + milis + "ms.");
            }
        }
    }
    private class ThreadCalculoPontos implements Runnable {

        private Random rand = new Random();
        private long totalPontos = 0;
        private long pontosDentro = 0;

        @Override
        public void run() {
            while (true) {
                double x = rand.nextDouble();
                double y = rand.nextDouble();
                if (x*x + y*y <= 1) {
                    pontosDentro++;
                }
                totalPontos++;
            }
        }
    }
}
