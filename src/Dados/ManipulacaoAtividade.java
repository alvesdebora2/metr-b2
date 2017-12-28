package Dados;

import Atividade.RegistroAtividade;

import java.util.ArrayList;

/**
 * Created by Débora on 27/12/2017.
 */
public class ManipulacaoAtividade {


    /**
     * Método para fazer com que cada item do ArrayList corresponda a uma issue, somando as horas.
     */
    public ArrayList<RegistroAtividade> unificaAtividades(ArrayList<RegistroAtividade> atividades) {
        int i, j;

        if(atividades.size() > 0) {
            for(i = 0; i < atividades.size() - 1; i++) {
                for(j = i + 1; j < atividades.size(); j++) {

                    // Se for encontrado outro registro com o mesmo valor de IssueKey,
                    // soma a hora dos dois e apaga o segundo registro.
                    // Faz isso para cada par de elementos do ArrayList.
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

    /**
     * Método para identificar as issues de Repasse/Entendimento e repassar as horas referentes a essas tarefas a
     * tarefas produtivas de Especificação, Execução e Automação.
     */
    public ArrayList<RegistroAtividade> repassaHorasEntendimento(ArrayList<RegistroAtividade> atividades) {
        int i, j;
        if(atividades.size() > 0) {
            for (i = 0; i < atividades.size() - 1; i++) {
                for (j = 0; j < atividades.size(); j++) {

                    // A hora é repassada sse, para duas atividades diferentes, x e y:
                    // - Uma atividade x é produtiva;
                    // - Uma atividade y corresponde a uma atividade de repasse/entendimento;
                    // - x e y pertencem à mesma demanda, de um mesmo projeto (o registro deve ter demanda diferente de [Sem Demanda]);
                    if(atividadeProdutiva(atividades.get(i)) && atividadeRepasseEntedimento(atividades.get(j)) && mesmaDemanda(atividades, i, j)){
                        atividades.get(i).setHoras(atividades.get(i).getHoras() + atividades.get(j).getHoras());
                        atividades.remove(j);
                        //se o item j for removido e for menor que i, para continuar a avaliar o mesmo item atual de i, deve-se decrementar o valor de i.
                        if(j < i)
                            i--;
                        j--;
                    }
                }
            }
        }
        //imprimeLista(atividades);
        return atividades;
    }

    /**
     * Método para identificar se a atividade é de repasse/entendimento.
     *
     */
    private boolean atividadeRepasseEntedimento(RegistroAtividade atividade) {
        return (atividade.getAtividade().getNomeAtividade().toLowerCase().contains("entendimento") && !atividade.getDemanda().equals("[Sem Demanda]"));
    }

    /**
     * Método para identificar se a atividade é produtiva; categoria deve ser diferente de 'Outros'.
     */
    private boolean atividadeProdutiva(RegistroAtividade atividade){
        return (!atividade.getAtividade().getCategoria().equals("Outros"));
    }

    /**
     * Método para verificar se duas atividades pertencem à mesma demanda, de um mesmo sistema.
     */
    private boolean mesmaDemanda(ArrayList<RegistroAtividade> atividades, int i, int j) {
        return(atividades.get(i).getSistema().equals(atividades.get(j).getSistema()) && atividades.get(i).getDemanda().equals(atividades.get(j).getDemanda()));

    }

    /*
     * Método para teste da lista.
     *
    private void imprimeLista(ArrayList<RegistroAtividade> atividades) {
        for(int i = 0; i < atividades.size(); i++)
            System.out.println(atividades.get(i).getIssueKey() + " " + atividades.get(i).getHoras() + " " + atividades.get(i).getSistema() + " " + atividades.get(i).getAnalista());
    }*/
}
