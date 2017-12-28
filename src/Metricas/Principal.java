package Metricas;

import Dados.ManipulacaoArquivo;

import java.util.Scanner;

/**
 * Created by Débora on 27/12/2017.
 */
public class Principal {

    public static void main (String[] a)  {
        ManipulacaoArquivo arquivo = new ManipulacaoArquivo();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Caminho do arquivo (ex: C:/Users/..../nomeArquivo.xls): ");
        String nomeArquivo = scanner.nextLine();
        arquivo.trataDados(nomeArquivo);
        System.out.println("\nArquivo gerado na área de trabalho.");
    }
}
