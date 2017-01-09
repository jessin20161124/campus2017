package task2;

import org.apache.poi.hssf.usermodel.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExchangeRate{
    /**
     * 对数据文件进行解析，封装成二维list并返回
     * @param dataFile 数据文件名
     * @param moneyCnt 货币的种类数
     * @return 二维List
     * @throws FileNotFoundException
     */
    public static List<List<String>> getData(String dataFile, int moneyCnt) throws FileNotFoundException {
        List<List<String>> data = new ArrayList<List<String>>();
        Scanner scanner = new Scanner(new File(dataFile));
        while(scanner.hasNext()){
            List<String> row = new ArrayList<String>();
            for(int i = 0; i < moneyCnt; i++){
                String date = scanner.next();
                String rate = scanner.next();
                if(i == 0){
                    row.add(date);
                }
                row.add(rate);
            }
            data.add(row);
        }
        return data;
    }

    /**
     * 利用表头和数据创建一张excel表
     * @param dataTitle 表头
     * @param data 表具体数据
     * @param excelFileName 保存的excel文件名
     * @param sheetName 子表名
     * @throws IOException
     */
    public static void createExcel(String[] dataTitle, List<List<String>> data, String excelFileName, String sheetName) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建一个sheet
        HSSFSheet sheet = workbook.createSheet(sheetName);
        // 指定excel数据居中
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        int rowId = 0, columnId = 0;
        // 创建表头
        HSSFRow rowTitle = sheet.createRow(rowId++);
        for(String field : dataTitle){
            HSSFCell cell = rowTitle.createCell(columnId++);
            cell.setCellValue(field);
            cell.setCellStyle(style);
        }
        // 创建数据
        for(List<String> rowList : data){
            HSSFRow rowData = sheet.createRow(rowId++);
            columnId = 0;
            for(String field : rowList){
                HSSFCell cell = rowData.createCell(columnId++);
                cell.setCellValue(field);
                //cell.setCellStyle(style);
            }
        }
        // 写到文件
        FileOutputStream fout = new FileOutputStream(excelFileName);
        workbook.write(fout);
        fout.close();
    }

    // 测试
    public static void main(String[] args){
        String[] dataTitle = {"日期", "美元", "欧元", "港币"};
        int moneyCnt = dataTitle.length - 1;
        String dataFile = "task2_data.txt";
        String excelFileName = "task2_result.xls";
        String sheetName = "sheet1";
        try {
            List<List<String>> data = getData(dataFile, moneyCnt);
            createExcel(dataTitle, data, excelFileName, sheetName);
            System.out.println("success");
        } catch (FileNotFoundException e) {
            System.out.println("file " + dataFile + " not found");
        } catch (IOException e) {
            System.out.println("IO exception : " + e.getMessage());
        }
    }
}