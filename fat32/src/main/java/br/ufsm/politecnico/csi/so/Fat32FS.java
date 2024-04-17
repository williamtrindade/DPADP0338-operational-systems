package br.ufsm.politecnico.csi.so;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Fat32FS {
    private static class EntradaArquivoDiretorio {
        private String nomeArquivo;
        private String extensao;
        private int tamanho;
        private int blocoInicial;

         EntradaArquivoDiretorio(
                String nomeArquivo,
                String extensao,
                int tamanho,
                int blocoInicial
        ) {
            this.nomeArquivo = nomeArquivo;
            if (this.nomeArquivo.length() > 8) {
                this.nomeArquivo = nomeArquivo.substring(0, 8);
            } else if (this.nomeArquivo.length() < 8) {
                do {
                    this.nomeArquivo += " ";
                } while (this.nomeArquivo.length() < 3);
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

        public byte[] toByteArray() {
            byte[] bytes = new byte[19];
            System.arraycopy(nomeArquivo.getBytes(StandardCharsets.ISO_8859_1), 0, bytes,0, 8);
            System.arraycopy(extensao.getBytes(StandardCharsets.ISO_8859_1), 0, bytes,8, 3);

            System.arraycopy(intToBytes(tamanho), 0, bytes,11, 4);
            System.arraycopy(intToBytes(blocoInicial), 0, bytes,15, 4);

            return bytes;
        }

        public byte[] intToBytes(int i) {
            ByteBuffer bb = ByteBuffer.allocate(4);
            bb.putInt(i);
            return bb.array();
        }

        public static void main(String[] args) {
            EntradaArquivoDiretorio entradaArquivoDiretorio = new EntradaArquivoDiretorio("arquivo", "txt")
        }
    }
}
