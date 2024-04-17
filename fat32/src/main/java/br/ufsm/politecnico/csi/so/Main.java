package br.ufsm.politecnico.csi.so;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Iniciando disco");
        Disco disco = new Disco();
        System.out.println("Escrevendo um bloco");
        // disco.escreveBloco(0, "bytes escritos no bloco zero..." . getBytes(StandardCharsets.UTF_8));
        byte[] data = disco.leBloco(0);
        System.out.println(new String(data, StandardCharsets.UTF_8));
    }
}
