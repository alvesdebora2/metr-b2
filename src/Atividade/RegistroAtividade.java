package Atividade;

import java.util.Date;

/**
 * Created by Débora on 27/12/2017.
 */
public class RegistroAtividade {
    private Atividade atividade;
    private String issueKey, mesPeriodo, anoPeriodo, demanda, sistema, analista;
    private double horas;
    private int numReteste, numBugs, producaoRealizada;

    public RegistroAtividade() {
        atividade = new Atividade();
    }

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    public String getMesPeriodo() {
        return mesPeriodo;
    }

    public void setMesPeriodo(String mesPeriodo) {
        this.mesPeriodo = mesPeriodo;
    }

    public String getAnoPeriodo() {
        return anoPeriodo;
    }

    public void setAnoPeriodo(String anoPeriodo) {
        this.anoPeriodo = anoPeriodo;
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public String getDemanda() {
        return demanda;
    }

    public void setDemanda(String demanda) {

        if(!demanda.isEmpty() || !demanda.equals("")) {
            this.demanda = demanda.toUpperCase();
        }
        else
            this.demanda = "[Sem Demanda]";
    }

    public String getSistema() {
        return sistema;
    }

    public void setSistema(String sistema) {
        if(!sistema.isEmpty() || !sistema.equals(""))
            this.sistema = sistema;
        else
            this.sistema = "[Sem Sistema]";
    }

    public double getHoras() {
        return horas;
    }

    public void setHoras(double horas) {
        this.horas = horas;
    }

    public String getAnalista() {
        return analista;
    }

    public void setAnalista(String analista) {
        this.analista = analista;
    }

    public int getNumReteste() {
        return numReteste;
    }

    public void setNumReteste(int numReteste) {
        this.numReteste = numReteste;
    }

    public int getNumBugs() {
        return numBugs;
    }

    public void setNumBugs(int numBugs) {
        this.numBugs = numBugs;
    }

    public int getProducaoRealizada() {
        return producaoRealizada;
    }

    public void setProducaoRealizada(int producaoRealizada) {
        this.producaoRealizada = producaoRealizada;
    }
}

