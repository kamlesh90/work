package com.cog.erms.helper;

import com.cog.erms.model.Employee;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelHelper {
    //create excel sheet name
    public static String SHEET_NAME;
    //excel sheet headers
    public static String [] HEADERS= {
            "id",
            "firstname",
            "lastname",
            "middleName",
            "addresses",
            "designations"
    };
    public static ByteArrayInputStream dataToExcel(List<Employee> list) throws IOException {
            //create workbook(excel file)
            Workbook workbook = new HSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            //create sheet
            Sheet sheet = workbook.createSheet();
            //create header row
            Row headerRow = sheet.createRow(0);

            //create cell
            for(int i=0;i<HEADERS.length;i++){
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
            }
            //create value row
            int rowIndex=1;
            for(Employee e : list){
                Row valueRow = sheet.createRow(rowIndex);
                rowIndex++;
                valueRow.createCell(0).setCellValue(e.getEmpId());
                valueRow.createCell(1).setCellValue(e.getFirstName());
                valueRow.createCell(2).setCellValue(e.getLastName());
                valueRow.createCell(3).setCellValue(e.getMiddleName());
//                valueRow.createCell(4).setCellValue((RichTextString) e.getAddresses().get(0));
//                valueRow.createCell(5).setCellValue((RichTextString) e.getDesignations().get(0));
            }
            //write workbook data to outputstream
            workbook.write(out);

            return  new ByteArrayInputStream(out.toByteArray());

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("failed to export data in excel");
        }finally {
            workbook.close();
            out.close();
        }
        return null;
    }
}
