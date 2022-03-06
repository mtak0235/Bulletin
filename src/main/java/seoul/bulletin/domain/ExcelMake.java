package seoul.bulletin.domain;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelMake {
    public static void main(String[] args) throws IOException {
        Workbook workbook = new HSSFWorkbook();//액셀 파일 생성
        Sheet sheet = workbook.createSheet("posts"); //시트 생성
        File file = new File("data.xls");//파일 생성
        FileOutputStream fos = new FileOutputStream(file);

        int rowNo = 0;

        String[][] data = { // 생성할 데이터
                { "동해물과", "백두산이", "마르고", "닳도록" }, //
                { "하느님이", "보우하사", "우리나라", "만세" }, //
                { "무궁화", "삼천리", "화려강산", "~~" }, //
                { "대한사람", "대한으로", "길이", "보전하세" }, //
        };


        Row headerRow = sheet.createRow(rowNo++);
        headerRow.createCell(0).setCellValue("id");
        headerRow.createCell(1).setCellValue("title");
        headerRow.createCell(2).setCellValue("content");
        headerRow.createCell(3).setCellValue("author");

        for (String[] d : data) {
            Row row = sheet.createRow(rowNo++);
            System.out.println("d = " + d);
            headerRow.createCell(0).setCellValue(d[0]);
            headerRow.createCell(1).setCellValue(d[1]);
            headerRow.createCell(2).setCellValue(d[2]);
            headerRow.createCell(3).setCellValue(d[3]);
        }
        workbook.write(fos);
        fos.close();

    }
}
