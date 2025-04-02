package br.ufsm.poli.csi.so.threads;

public class ExemplosThreads {
    public static void main(String[] args) {
        for (int i = 0; i < 10000000  ; i++) {
            int finalI = i;
            Thread.ofVirtual().name("t-" + i).start(() -> {
                System.out.println("i = " + finalI);
                try {
                    Thread.sleep(1_000_000_000);
                } catch (InterruptedException e) {
                    //throw new RuntimeException(e);
                }
            });
//            try {
//                Thread.sleep(5);
//            } catch (InterruptedException e) {
//                //throw new RuntimeException(e);
//            }
        }
    }
}
