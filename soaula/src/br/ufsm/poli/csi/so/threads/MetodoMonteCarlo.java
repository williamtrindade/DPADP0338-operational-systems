package br.ufsm.poli.csi.so.threads;

import java.util.Random;

public class MetodoMonteCarlo {
    public static void main(String[] args) {
        long totalPontos = 0;
        long pontosDentro = 0;

        Random r = new Random();

        long milis = System.currentTimeMillis();

        for (long i = 0; i < 10_000_000_000L; i++) {
            double x = r.nextDouble();
            double y = r.nextDouble();
            if (x*x + y*y <= 1) {
                pontosDentro++;
            }
            totalPontos++;
        }
        double pi = 4.0 * pontosDentro / totalPontos;
        System.out.println(pi);
        System.out.println("t: "+(System.currentTimeMillis() - milis)+"ms");
    }
}
