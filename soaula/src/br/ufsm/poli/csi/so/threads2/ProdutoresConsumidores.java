package br.ufsm.poli.csi.so.threads2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProdutoresConsumidores {
    private List<Integer> buffer = new ArrayList<>();
    private static final int TAMANHO_BUFFER = 10;

    private Semaphore mutex = new Semaphore(1);
    private Semaphore cheio = new Semaphore(0);
    private Semaphore vazio = new Semaphore(TAMANHO_BUFFER);

    public static void main(String[] args) {
        new ProdutoresConsumidores();
    }

    public ProdutoresConsumidores() {
        Thread.ofPlatform().start(new Produtor());
        Thread.ofPlatform().start(new Consumidor());
    }

    private class Produtor  implements Runnable {

        @Override
        public void run() {
            Random rand = new Random();
            while(true) {
                int num = rand.nextInt(100);
                try {
                    vazio.acquire();
                } catch (InterruptedException _) {
                }
                try {
                    mutex.acquire();
                } catch (InterruptedException _) {
                }

                // grava no buffer
                buffer.add(num);
                System.out.println("[Produtor] Produzindo item ("+num+")");
                mutex.release();
                cheio.release();
            }
        }
    }

    private class Consumidor  implements Runnable {

        @Override
        public void run() {
            while(true) {
                try {
                    cheio.acquire();
                } catch (InterruptedException _) {
                }
                try {
                    mutex.acquire();
                } catch (InterruptedException _) {
                }
                int num = buffer.remove(0);
                System.out.println("[Consumidor consumindo item ("+num+")");
                mutex.release();
                vazio.release();
                // consumir
            }
        }
    }
}
