package br.ufsm.politecnico.csi.so;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Fat32FS implements FileSystem {


    private Disco disco;

    private int[] fat = new int[16*1024];

    public Fat32FS() throws IOException {
        this.disco = new Disco();
        if (this.disco.init()) {
            leFat();
            leDiretorio();
        } else {
            criaFat();
            escreveFat();
        }

    }

    private void criaFat() {
        for (int i = 2; i < fat.length; i++) {
            fat[i] = -1;
        }
    }

    private void escreveFat() throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(64*1024);
        for (int f : fat) {
            bb.putInt(f);
        }
        byte[] blocoFat = bb.array();
        disco.escreveBloco(BLOCO_FAT, blocoFat);
    }

    private static final int BLOCO_FAT = 1;
    private void leFat() throws IOException {
        byte[] blocoFat = disco.leBloco(BLOCO_FAT);
        ByteBuffer bb = ByteBuffer.wrap(blocoFat);
        for (int i = 0; i < 16*1024; i++) {
            fat[i] = bb.getInt();
        }
    }

    @Override
    public void create(String fileName, byte[] data) {
        if (data.length > freeSpace()) {
            return;
        }

    }

    private int encontraBlocoLivre() {
        for (int i = 2; i < fat.length; i++) {
            if (fat[i] == -1) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void append(String fileName, byte[] data) {

    }

    @Override
    public byte[] read(String fileName, int offset, int limit) {
        return new byte[0];
    }


    @Override
    public void remove(String fileName) {

    }

    @Override
    public int freeSpace() {
        return 0;
    }

    private static class EntradaArquivoDiretorio {
        private String nomeArquivo;
        private String extensao;
        private int tamanho;
        private int blocoInicial;

        public EntradaArquivoDiretorio(String nomeArquivo,
                                       String extensao,
                                       int tamanho,
                                       int blocoInicial) {
            this.nomeArquivo = nomeArquivo;
            if (this.nomeArquivo.length() > 8) {
                this.nomeArquivo = nomeArquivo.substring(0, 8);
            } else if (this.nomeArquivo.length() < 8) {
                do {
                    this.nomeArquivo += " ";
                } while (this.nomeArquivo.length() < 8);
            }
            this.extensao = extensao;
            if (this.extensao.length() > 3) {
                this.extensao = extensao.substring(0, 3);
            } else if (this.extensao.length() < 3) {
                do {
                    this.extensao += " ";
                } while (this.extensao.length() < 3);
            }
            this.tamanho = tamanho;
            this.blocoInicial = blocoInicial;
            if (blocoInicial < 2 || blocoInicial >= Disco.NUM_BLOCOS) {
                throw new IllegalArgumentException("numero de bloco invalido");
            }
        }

        public byte[] toByteArray(ByteBuffer bb) {
            bb.put(nomeArquivo.getBytes(StandardCharsets.ISO_8859_1));
            bb.put(extensao.getBytes(StandardCharsets.ISO_8859_1));
            bb.putInt(tamanho);
            bb.putInt(blocoInicial);
            return bb.array();
        }

        private static int intFromBytes(byte[] data, int index) {
            ByteBuffer bb = ByteBuffer.wrap(data);
            return bb.getInt(index);
        }

        public static EntradaArquivoDiretorio fromBytes(byte[] bytes) {
            String nome = new String(bytes,
                    0, 8, StandardCharsets.ISO_8859_1);
            String extensao = new String(bytes,
                    8, 3, StandardCharsets.ISO_8859_1);
            int tamanho = intFromBytes(bytes, 11);
            int blocoInicial = intFromBytes(bytes, 15);
            System.out.println(nome);
            System.out.println(extensao);
            System.out.println(tamanho);
            System.out.println(blocoInicial);
            return new EntradaArquivoDiretorio(nome, extensao, tamanho, blocoInicial);
        }

        public static EntradaArquivoDiretorio fromStream(InputStream inputStream) throws IOException {
            byte[] bytes = new byte[19];
            inputStream.read(bytes);
            String nome = new String(bytes,
                    0, 8, StandardCharsets.ISO_8859_1);
            String extensao = new String(bytes,
                    8, 3, StandardCharsets.ISO_8859_1);
            int tamanho = intFromBytes(bytes, 11);
            int blocoInicial = intFromBytes(bytes, 15);
            System.out.println(nome);
            System.out.println(extensao);
            System.out.println(tamanho);
            System.out.println(blocoInicial);
            return new EntradaArquivoDiretorio(nome, extensao, tamanho, blocoInicial);
        }

    }

    private static final int BLOCO_DIRETORIO = 0;
    private List<EntradaArquivoDiretorio> diretorioRaiz = new ArrayList<>();

    private void leDiretorio() throws IOException {
        byte[] dirBytes = disco.leBloco(BLOCO_DIRETORIO);
        ByteArrayInputStream bin = new ByteArrayInputStream(dirBytes);
        EntradaArquivoDiretorio entrada = null;
        do {
            entrada = EntradaArquivoDiretorio.fromStream(bin);
            if (entrada.tamanho > 0) {
                diretorioRaiz.add(entrada);
            }
        } while(entrada.tamanho > 0);

    }

    private void escreveDiretorio() throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(Disco.TAM_BLOCO);
        for (EntradaArquivoDiretorio entrada : diretorioRaiz) {
            entrada.toByteArray(bb);
        }
        disco.escreveBloco(BLOCO_DIRETORIO, bb.array());
    }

}
