package Metricas;

import Dados.ManipulacaoArquivo;

/**
 * Created by Débora on 27/12/2017.
 */
public class Principal {

    public static void main (String[] a)  {
        ManipulacaoArquivo arquivo = new ManipulacaoArquivo();
        arquivo.trataDados("teste04.xls");
    }
}
