package Dados;

import Atividade.RegistroAtividade;

import java.util.ArrayList;

/**
 * Created by Débora on 27/12/2017.
 */
public class ManipulacaoAtividade {


    public ArrayList<RegistroAtividade> unificaAtividades(ArrayList<RegistroAtividade> atividades) {
        int i, j;

        if(atividades.size() > 0) {
            for(i = 0; i < atividades.size() - 1; i++) {
                for(j = i + 1; j < atividades.size(); j++) {
                    if(atividades.get(i).getIssueKey().equals(atividades.get(j).getIssueKey())){
                        atividades.get(i).setHoras(atividades.get(i).getHoras() + atividades.get(j).getHoras());
                        atividades.remove(j);
                        j--;
                    }
                }
            }
        }

        return atividades;
    }

    public ArrayList<RegistroAtividade> repassaHorasEntendimentoEspecificacao(ArrayList<RegistroAtividade> atividades) {
        int i, j;
        if(atividades.size() > 0) {
            for (i = 0; i < atividades.size() - 1; i++) {
                for (j = 0; j < atividades.size(); j++) {
                    if(atividadeProdutiva(atividades.get(i)) && atividadeEntedimentoEspecificacao(atividades.get(j)) && mesmaDemanda(atividades, i, j) && i != j){
                        atividades.get(i).setHoras(atividades.get(i).getHoras() + atividades.get(j).getHoras());
                        atividades.remove(j);
                        if(j < i)
                            i--; //testar
                        j--;
                    }
                }
            }
        }
        imprimeLista(atividades);
        return atividades;
    }

    private boolean atividadeEntedimentoEspecificacao(RegistroAtividade atividade) {
        if(atividade.getAtividade().getNomeAtividade().contains("Entendimento/Repasse") && !atividade.getDemanda().equals("[Sem Demanda]"))
            return true;
        return false;
    }

    private boolean atividadeProdutiva(RegistroAtividade atividade){
        if(!atividade.getAtividade().getCategoria().equals("Outros"))
            return true;

        return false;
    }

    private boolean mesmaDemanda(ArrayList<RegistroAtividade> atividades, int i, int j) {
        if(atividades.get(i).getSistema().equals(atividades.get(j).getSistema()) && atividades.get(i).getDemanda().equals(atividades.get(j).getDemanda()))
            return true;
        return false;
    }

    private void imprimeLista(ArrayList<RegistroAtividade> atividades) {
        for(int i = 0; i < atividades.size(); i++)
            System.out.println(atividades.get(i).getIssueKey() + " " + atividades.get(i).getHoras() + " " + atividades.get(i).getSistema() + " " + atividades.get(i).getAnalista());
    }
}
