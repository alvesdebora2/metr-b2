package Dados;

import Atividade.Atividade;
import Atividade.RegistroAtividade;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by Débora on 27/12/2017.
 */
public class ManipulacaoArquivo {


    public void trataDados(String filePath) {
        ArrayList<RegistroAtividade> atividades;
        ManipulacaoAtividade manipulacaoAtividade = new ManipulacaoAtividade();

        atividades = leArquivo(filePath);
        atividades = manipulacaoAtividade.unificaAtividades(atividades);
        atividades = manipulacaoAtividade.repassaHorasEntendimentoEspecificacao(atividades);
        criaPlanilhaSaida(atividades);

    }

    private ArrayList<RegistroAtividade> leArquivo(String filePath) {
        ArrayList<RegistroAtividade> atividadesLidas = new ArrayList<>();

        // Abrindo o arquivo e recuperando a planilha
        FileInputStream file = null;
        try {
            file = new FileInputStream(new File(filePath));
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);
            DataFormatter objDefaultFormat = new DataFormatter();
            FormulaEvaluator objFormulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
            Iterator<Row> rowIterator = sheet.iterator();
            Row row;
            String tRetestes, tBugs, tProducao;
            int idPeriodo = 0, idDemanda = 0, idSistema = 0, idAnalista = 0, idHora = 0, idReteste = 0, idBugs = 0, idIssueKey = 0, idIssueType = 0;

            while (rowIterator.hasNext()) {
                row = rowIterator.next();

                // Identifica id das colunas.
                // Apenas Produção e Atividade que não são identificadas aqui;
                //  - Produção: existem três colunas de produção no arquivo de entrada, então o id da que deve ser utilizado foi fixado.
                //  - Atividade: o texto é
                if(row.getRowNum() == 0){
                    Iterator<Cell> cellIterator = row.cellIterator();
                    Cell cell;
                    while(cellIterator.hasNext()){
                        cell = cellIterator.next();
                        cell.setCellType(CellType.STRING);
                        if(cell.getStringCellValue().toLowerCase().equals("period"))
                            idPeriodo = cell.getColumnIndex();
                        else if(cell.getStringCellValue().toLowerCase().equals("identificador da demanda no cliente"))
                            idDemanda = cell.getColumnIndex();
                        else if(cell.getStringCellValue().toLowerCase().equals("sistema"))
                            idSistema = cell.getColumnIndex();
                        else if(cell.getStringCellValue().toLowerCase().equals("full name"))
                            idAnalista = cell.getColumnIndex();
                        else if(cell.getStringCellValue().toLowerCase().equals("hours"))
                            idHora = cell.getColumnIndex();
                        else if(cell.getStringCellValue().toLowerCase().equals("número de retestes"))
                            idReteste = cell.getColumnIndex();
                        else if(cell.getStringCellValue().toLowerCase().equals("número de bugs"))
                            idBugs = cell.getColumnIndex();
                        else if(cell.getStringCellValue().toLowerCase().equals("issue key"))
                            idIssueKey = cell.getColumnIndex();
                        else if(cell.getStringCellValue().toLowerCase().equals("issue type"))
                            idIssueType = cell.getColumnIndex();
                    }
                    continue;
                }

                // Pega dados das colunas de uma linha específica.
                RegistroAtividade registroAtividade = new RegistroAtividade();
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

                atividadesLidas.add(registroAtividade);
            }
            file.close();
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return atividadesLidas;
    }

    private void criaPlanilhaSaida(ArrayList<RegistroAtividade> atividades) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet firstSheet = workbook.createSheet("Aba1");

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(new File("planilha-metricas.xls"));

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
            System.out.println("Erro ao exportar arquivo");
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Atividade identificaTipoAtividade(Row linha, int issueType){
        Atividade atividade = new Atividade();

        if(linha.getCell(issueType).getStringCellValue().toLowerCase().contains("execução")) {
            atividade.setNomeAtividade(linha.getCell(44).getStringCellValue());
            atividade.setCategoria("Execução");
        }
        else if(linha.getCell(issueType).getStringCellValue().toLowerCase().contains("especificação")) {
            atividade.setNomeAtividade(linha.getCell(54).getStringCellValue());
            atividade.setCategoria("Especificação");
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

    private int verificaInteiro(String str) {
        double numero = 0;

        if(str.isEmpty())
            return 0;
        else {
            numero = Double.parseDouble(str);
            return (int) numero;
        }
    }
}
