package Atividade;

import java.text.DecimalFormat;

/**
 * Created by Débora on 27/12/2017.
 */
public class RegistroAtividade {
    private Atividade atividade;
    private Analista analista;
    private String issueKey, mesPeriodo, anoPeriodo, demanda, sistema, assignee, nomeProjeto;
    private double horas;
    private int numReteste, numBugs, producaoRealizada;
    private boolean fechada;

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
        DecimalFormat df = new DecimalFormat("#.00");

        this.horas = Double.parseDouble(df.format(horas).replace(",", "."));
    }

    public String getAnalista() {
        return analista.getNome();
    }

    public void setAnalista(String nomeAnalista) {
        analista = new Analista();
        analista.setNome(nomeAnalista);
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

    public String getNomeProjeto() {
        return nomeProjeto;
    }

    public void setNomeProjeto(String nomeProjeto) {
        this.nomeProjeto = nomeProjeto;
    }

    public boolean isFechada() {
        return fechada;
    }

    public void setFechada(String status) {
        this.fechada = status.toLowerCase().equals("closed");
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
}

