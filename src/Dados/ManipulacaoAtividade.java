package Dados;

import Atividade.RegistroAtividade;
import java.util.ArrayList;

/**
 * Created by Débora on 27/12/2017.
 */
public class ManipulacaoAtividade {


    /**
     * Faz com que cada item do ArrayList corresponda a uma issue, somando as horas.
     */
    public ArrayList<RegistroAtividade> filtraAtividades(ArrayList<RegistroAtividade> atividades) {
        int i, j;

        removeNaoFechadasNaoProdutivas(atividades);

        if(atividades.size() > 0) {
            for(i = 0; i < atividades.size() - 1; i++) {
                for(j = i + 1; j < atividades.size(); j++) {

                    // Se for encontrado outro registro com o mesmo valor de IssueKey,
                    // soma a hora dos dois, pega o período do registro mais recente, e apaga o segundo registro.
                    // Faz isso para cada par de elementos do ArrayList.
                    if(atividades.get(i).getIssueKey().equals(atividades.get(j).getIssueKey())){
                        atividades.get(i).setHoras(atividades.get(i).getHoras() + atividades.get(j).getHoras());
                        atividades.get(i).setAnoPeriodo(identificaAtividadePeriodoMaisRecente(atividades, i, j).getAnoPeriodo());
                        atividades.get(i).setMesPeriodo(identificaAtividadePeriodoMaisRecente(atividades, i, j).getMesPeriodo());
                        atividades.remove(j);
                        j--;
                    }
                }
            }
        }

        return atividades;
    }

    /**
     * Identifica as issues de Repasse/Entendimento/Massa de Dados e repassar as horas referentes a essas tarefas a
     * tarefas produtivas de Especificação, Execução e Automação.
     */
    public void repassaHorasEntendimento(ArrayList<RegistroAtividade> atividades) {
        int i, j;
        if(atividades.size() > 0) {
            for (i = 0; i < atividades.size() - 1; i++) {
                for (j = 0; j < atividades.size(); j++) {

                    // A hora é repassada sse, para duas atividades diferentes, x e y:
                    // - Uma atividade x é produtiva;
                    // - Uma atividade y corresponde a uma atividade de repasse/entendimento/massa de dados;
                    // - x e y pertencem à mesma demanda, de um mesmo projeto (o registro deve ter demanda diferente de [Sem Demanda]);
                    if(atividadeProdutiva(atividades.get(i)) && atividadeRepasseEntedimento(atividades.get(j)) && verificaDemanda(atividades, i, j)){
                        atividades.get(i).setHoras(atividades.get(i).getHoras() + atividades.get(j).getHoras());
                        atividades.get(i).setAnoPeriodo(identificaAtividadePeriodoMaisRecente(atividades, i, j).getAnoPeriodo());
                        atividades.get(i).setMesPeriodo(identificaAtividadePeriodoMaisRecente(atividades, i, j).getMesPeriodo());
                        atividades.remove(j);
                        //se o item j for removido e for menor que i, para continuar a avaliar o mesmo item atual de i, deve-se decrementar o valor de i.
                        if(j < i)
                            i--;
                        j--;
                    }
                }
            }
        }

        for (i = 0; i < atividades.size() - 1; i++) {
            if(atividadeRepasseEntedimento(atividades.get(i))) {
                atividades.remove(i);
                i--;
            }
        }

        imprimeLista(atividades);
    }

    /**
     * Remove as atividades que não possuem status Closed,
     * e que não são produtivas e não são de repasse.
     */
    private void removeNaoFechadasNaoProdutivas(ArrayList<RegistroAtividade> atividades) {
        for(int i = 0; i < atividades.size(); i++) {
            if(!atividades.get(i).isFechada() || (!atividadeProdutiva(atividades.get(i)) && !(atividadeRepasseEntedimento(atividades.get(i))))) {
                atividades.remove(i);
                i--;
            }
        }
    }

    /**
     * Identifica se a atividade é de repasse/entendimento/massa de dados.
     *
     */
    private boolean atividadeRepasseEntedimento(RegistroAtividade atividade) {
        return (atividade.getAtividade().getNomeAtividade().toLowerCase().contains("entendimento"));
    }

    /**
     * Identifica se a atividade é produtiva; categoria deve ser diferente de 'Outros'.
     */
    private boolean atividadeProdutiva(RegistroAtividade atividade){
        return (!atividade.getAtividade().getCategoria().equals("Outros"));
    }

    /**
     * Verifica se duas atividades pertencem à mesma demanda, de um mesmo sistema.
     */
    private boolean verificaDemanda(ArrayList<RegistroAtividade> atividades, int i, int j) {
        return(mesmoSistema(atividades, i, j) && mesmaDemandaNaoNula(atividades, i, j) && mesmaCategoria(atividades, i, j));

    }

    private RegistroAtividade identificaAtividadePeriodoMaisRecente(ArrayList<RegistroAtividade> atividades, int i, int j) {
        if(anoMaior(atividades, i, j) || (anoIgual(atividades, i, j) && mesMaior(atividades, i, j)))
            return atividades.get(i);
        else
            return atividades.get(j);
    }

    private boolean anoMaior(ArrayList<RegistroAtividade> atividades, int i, int j) {
        return Integer.parseInt(atividades.get(i).getAnoPeriodo()) > Integer.parseInt(atividades.get(j).getAnoPeriodo());
    }

    private boolean anoIgual(ArrayList<RegistroAtividade> atividades, int i, int j) {
        return Integer.parseInt(atividades.get(i).getAnoPeriodo()) == Integer.parseInt(atividades.get(j).getAnoPeriodo());
    }

    private boolean mesMaior(ArrayList<RegistroAtividade> atividades, int i, int j) {
        return Integer.parseInt(atividades.get(i).getMesPeriodo()) == Integer.parseInt(atividades.get(j).getMesPeriodo());
    }

    private boolean mesmoSistema(ArrayList<RegistroAtividade> atividades, int i, int j) {
        return atividades.get(i).getSistema().equals(atividades.get(j).getSistema());
    }

    private boolean mesmaDemandaNaoNula(ArrayList<RegistroAtividade> atividades, int i, int j) {
        return atividades.get(i).getDemanda().equals(atividades.get(j).getDemanda())&& !atividades.get(i).getDemanda().equals("[Sem Demanda]");
    }

    private boolean mesmaCategoria(ArrayList<RegistroAtividade> atividades, int i, int j) {
        return(atividades.get(j).getAtividade().getNomeAtividade().toLowerCase().contains(atividades.get(i).getAtividade().getCategoria().toLowerCase()));
    }

    /*
     * Método para teste da lista.
     **/
    private void imprimeLista(ArrayList<RegistroAtividade> atividades) {
        for(int i = 0; i < atividades.size(); i++)
            System.out.println(atividades.get(i).getIssueKey());

        // + " " + atividades.get(i).getHoras() + " " + atividades.get(i).getSistema() + " " + atividades.get(i).getAnalista()
    }
}
