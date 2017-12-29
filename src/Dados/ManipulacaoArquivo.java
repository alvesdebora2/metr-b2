package Dados;

import Atividade.Atividade;
import Atividade.RegistroAtividade;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by Débora on 27/12/2017.
 */
public class ManipulacaoArquivo {


    /**
     * Realiza ações de leitura, tratamento dos dados e escrita em arquivo.
     */
    public void trataDados(String filePath) {
        ArrayList<RegistroAtividade> atividades;
        ManipulacaoAtividade manipulacaoAtividade = new ManipulacaoAtividade();

        atividades = leArquivo(filePath);
        atividades = manipulacaoAtividade.repassaHorasEntendimento(manipulacaoAtividade.unificaAtividades(atividades));
        criaPlanilhaSaida(atividades);

    }

    /**
     * Lê arquivo xls e insere registros em um ArrayList.
     */
    private ArrayList<RegistroAtividade> leArquivo(String filePath) {
        ArrayList<RegistroAtividade> atividadesLidas = new ArrayList<>();

        // Abrindo o arquivo e recuperando a planilha
        FileInputStream file;
        try {
            file = new FileInputStream(new File(filePath));
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);
            DataFormatter objDefaultFormat = new DataFormatter();
            FormulaEvaluator objFormulaEvaluator = new HSSFFormulaEvaluator(workbook);
            Iterator<Row> rowIterator = sheet.iterator();
            Row row;
            String tRetestes, tBugs, tProducao;
            int idPeriodo = 0, idDemanda = 0, idSistema = 0, idAnalista = 0, idHora = 0, idReteste = 0, idBugs = 0, idIssueKey = 0, idIssueType = 0, idNomeProjeto = 0;

            while (rowIterator.hasNext()) {
                row = rowIterator.next();

                // Identifica id das colunas.
                // Apenas Produção e Atividade que não são identificadas aqui.
                //  - Produção: existem três colunas de produção no arquivo de entrada, então o id da que deve ser utilizado foi fixado.
                //  - Atividade: o texto é identificado de acordo com a categoria: Especificação, Execução , Entendimento ou Outros.
                if(row.getRowNum() == 0){
                    Iterator<Cell> cellIterator = row.cellIterator();
                    Cell cell;
                    while(cellIterator.hasNext()){
                        cell = cellIterator.next();
                        cell.setCellType(CellType.STRING);
                        switch(cell.getStringCellValue().toLowerCase()) {
                            case "period":
                                idPeriodo = cell.getColumnIndex();
                                break;
                            case "identificador da demanda no cliente":
                                idDemanda = cell.getColumnIndex();
                                break;
                            case "sistema":
                                idSistema = cell.getColumnIndex();
                                break;
                            case "full name":
                                idAnalista = cell.getColumnIndex();
                                break;
                            case "hours":
                                idHora = cell.getColumnIndex();
                                break;
                            case "número de retestes":
                                idReteste = cell.getColumnIndex();
                                break;
                            case "número de bugs":
                                idBugs = cell.getColumnIndex();
                                break;
                            case "issue key":
                                idIssueKey = cell.getColumnIndex();
                                break;
                            case "issue type":
                                idIssueType = cell.getColumnIndex();
                                break;
                            case "project name":
                                idNomeProjeto = cell.getColumnIndex();
                                break;
                        }
                    }
                    continue;
                }

                RegistroAtividade registroAtividade = new RegistroAtividade();

                // Pega dados das colunas da linha atual.
                registroAtividade.setMesPeriodo(row.getCell(idPeriodo).getStringCellValue().substring(0,2));
                registroAtividade.setAnoPeriodo(row.getCell(idPeriodo).getStringCellValue().substring(2, 4));
                registroAtividade.setDemanda(row.getCell(idDemanda).getStringCellValue());
                registroAtividade.setSistema(row.getCell(idSistema).getStringCellValue());
                registroAtividade.setAtividade(identificaTipoAtividade(row, idIssueType));
                registroAtividade.setAnalista(row.getCell(idAnalista).getStringCellValue());
                registroAtividade.setHoras(row.getCell(idHora).getNumericCellValue());

                objFormulaEvaluator.evaluate(row.getCell(idReteste));
                tRetestes = objDefaultFormat.formatCellValue(row.getCell(idReteste),objFormulaEvaluator);
                registroAtividade.setNumReteste(verificaInteiro(tRetestes));

                objFormulaEvaluator.evaluate(row.getCell(idBugs));
                tBugs = objDefaultFormat.formatCellValue(row.getCell(idBugs),objFormulaEvaluator);
                registroAtividade.setNumBugs(verificaInteiro(tBugs));

                objFormulaEvaluator.evaluate(row.getCell(30));
                tProducao = objDefaultFormat.formatCellValue(row.getCell(30),objFormulaEvaluator);
                registroAtividade.setProducaoRealizada(verificaInteiro(tProducao));

                registroAtividade.setIssueKey(row.getCell(idIssueKey).getStringCellValue());
                registroAtividade.setNomeProjeto(row.getCell(idNomeProjeto).getStringCellValue());

                atividadesLidas.add(registroAtividade);
            }
            file.close();
            workbook.close();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return atividadesLidas;
    }

    /**
     * Cria xls utilizando ArrayList de 'RegistroAtividade' como entrada.
     */
    private void criaPlanilhaSaida(ArrayList<RegistroAtividade> atividades) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet firstSheet = workbook.createSheet("Aba1");
        FileSystemView system = FileSystemView.getFileSystemView();
        String nomeProjeto, periodo;

        FileOutputStream fos = null;

        try {
            nomeProjeto = atividades.get(0).getNomeProjeto();
            periodo = atividades.get(0).getMesPeriodo() + atividades.get(0).getAnoPeriodo();

            // Salva arquivo no desktop.
            fos = new FileOutputStream(new File(system.getHomeDirectory().getPath() + File.separator + "Metricas-" + nomeProjeto + "-" + periodo + ".xls"));

            // Definição da linha de cabeçalho.
            HSSFRow primeiraLinha = firstSheet.createRow(0);
            primeiraLinha.createCell(0).setCellValue("Período (mm/aaaa)");
            primeiraLinha.createCell(1).setCellValue("Demanda");
            primeiraLinha.createCell(2).setCellValue("Sistema");
            primeiraLinha.createCell(3).setCellValue("Atividade");
            primeiraLinha.createCell(4).setCellValue("Analista");
            primeiraLinha.createCell(5).setCellValue("Horas");
            primeiraLinha.createCell(6).setCellValue("Nº de Retestes");
            primeiraLinha.createCell(7).setCellValue("Nº de Bugs");
            primeiraLinha.createCell(8).setCellValue("Produção Realizada");
            primeiraLinha.createCell(9).setCellValue("Issue");

            // Linhas de conteúdo.
            for (int i = 1; i <= atividades.size(); i++) {
                HSSFRow linha = firstSheet.createRow(i);

                linha.createCell(0).setCellValue(atividades.get(i-1).getMesPeriodo() + "/20" + atividades.get(i-1).getAnoPeriodo());
                linha.createCell(1).setCellValue(atividades.get(i-1).getDemanda());
                linha.createCell(2).setCellValue(atividades.get(i-1).getSistema());
                linha.createCell(3).setCellValue(atividades.get(i-1).getAtividade().getNomeAtividade());
                linha.createCell(4).setCellValue(atividades.get(i-1).getAnalista());
                linha.createCell(5).setCellValue(atividades.get(i-1).getHoras());
                linha.createCell(6).setCellValue(atividades.get(i-1).getNumReteste());
                linha.createCell(7).setCellValue(atividades.get(i-1).getNumBugs());
                linha.createCell(8).setCellValue(atividades.get(i-1).getProducaoRealizada());
                linha.createCell(9).setCellValue(atividades.get(i-1).getIssueKey());

            }

            workbook.write(fos);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao exportar arquivo.");
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Verifica qual coluna deve ser lida de acordo com a categoria de atividade:
     * Especificação, Execução, Automação ou Outros.
     */
    private Atividade identificaTipoAtividade(Row linha, int issueType){
        Atividade atividade = new Atividade();

        if(linha.getCell(issueType).getStringCellValue().toLowerCase().contains("especificação")) {
            atividade.setNomeAtividade(linha.getCell(54).getStringCellValue());
            atividade.setCategoria("Especificação");
        }
        else if(linha.getCell(issueType).getStringCellValue().toLowerCase().contains("execução")) {
            atividade.setNomeAtividade(linha.getCell(44).getStringCellValue());
            atividade.setCategoria("Execução");
        }
        else if(linha.getCell(issueType).getStringCellValue().toLowerCase().contains("automação")) {
            atividade.setNomeAtividade(linha.getCell(55).getStringCellValue());
            atividade.setCategoria("Automação");
        }
        else if(linha.getCell(issueType).getStringCellValue().toLowerCase().contains("outras")) {
            atividade.setNomeAtividade(linha.getCell(56).getStringCellValue());
            atividade.setCategoria("Outros");
        }

        return atividade;
    }

    /**
     * Transforma String em inteiro; se string for vazia, retorna zero.
     */
    private int verificaInteiro(String str) {
        double numero;

        if(str.isEmpty())
            return 0;
        else {
            numero = Double.parseDouble(str);
            return (int) numero;
        }
    }
}
