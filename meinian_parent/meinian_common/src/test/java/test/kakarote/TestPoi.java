package test.kakarote;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.IOException;

/**
 * @Date: 2023/2/7 15:40
 * @Auther: Kakarotelu
 * @Description: 测试poi读取Excel中数据
 */

public class TestPoi {
    @Test
    public void readExcel() throws IOException {
        //1、创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook("D:\\kakarote.xlsx");
        //2、获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
        XSSFSheet sheet = workbook.getSheetAt(0);
        //3、遍历工作表获得行对象
        for (Row row : sheet) {
            //4、遍历行对象获取单元格对象（列对象）
            for (Cell cell : row) {
                //获得单元格中的值
                String value = cell.getStringCellValue();
                System.out.println("单元格的值为：" + value);
            }
        }
        //5、关闭工作簿
        workbook.close();
    }

    // 导出excel另一写法
    @Test
    public void exportExcel_lastRow() throws IOException {
        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook("D:\\kakarote.xlsx");
        //获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
        XSSFSheet sheet = workbook.getSheetAt(0);
        //获取当前工作表最后一行的行号，行号从0开始
        int lastRowNum = sheet.getLastRowNum();
        for(int i = 0; i <= lastRowNum; i++){
            //根据行号获取行对象
            XSSFRow row = sheet.getRow(i);
            // 再获取单元格对象
            short lastCellNum = row.getLastCellNum();
            for(short j = 0; j < lastCellNum; j++){
                // 获取单元格对象的值
                String value = row.getCell(j).getStringCellValue();
                System.out.println(value);
            }
        }
        workbook.close();
    }
}
