package com.kakarote.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kakarote.constant.MessageConstant;
import com.kakarote.results.Result;
import com.kakarote.service.MemberService;
import com.kakarote.service.ReportService;
import com.kakarote.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Date: 2023/2/20 21:05
 * @Auther: Kakarotelu
 * @Description: 制作Echarts图表
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    /**
     * 会员数量统计
     * *返回Result(flag,message,data):data=Map<String,Object>
     *map：  key             value
     *     months          List<String>   (["2022-12","2022-01","2022-02","2022-03","2022-04","2022-05"])
     *    memberCount     List<Integer>  ([5, 20, 36, 40, 50, 60])
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        try {
            // 获取日历对象
            Calendar calendar = Calendar.getInstance();
            //根据当前时间，获取前12个月的日历(当前日历2023-02，12个月前，日历时间2022-03)
            //第一个参数，日历字段
            //第二个参数，要添加到字段中的日期或时间（向前偏移12个月）
            calendar.add(Calendar.MONTH,-12);

            List<String> months = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            for(int i = 0; i < 12; i++){
                //第一个参数是月份 2022-2
                //第二个参数是月份+1个月
                calendar.add(Calendar.MONTH,1);
                Date date = calendar.getTime();
                String month = sdf.format(date);
                //把过去12个月添加进list集合中
                months.add(month);
            }

            Map<String,Object> map = new HashMap<>();
            // 把过去12个月的日期存储到map里面
            map.put("months",months);

            // 查询所有的会员数量
            List<Integer> memberCount = memberService.findMemberCountByMonth(months);
            map.put("memberCount",memberCount);

            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }


    /**
     * 统计套餐预约人数占比（饼图）
     * @return
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){

        Map<String, Object> map = null;
        //查询套餐及其预定次数
        try {
            List<Map<String, Object>> list = setmealService.findSetmealCount();
            map = new HashMap<>();
            map.put("setmealCount", list);

            //获得每个套餐对应的套餐名
            List<String> setmealNames = new ArrayList<>();
            for (Map<String, Object> stringObjectMap : list) {
                String SetmealName = (String) stringObjectMap.get("name");
                setmealNames.add(SetmealName);
            }
            map.put("setmealNames", setmealNames);
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }

    /**
     * 获得运营统计数据
     * Map数据格式：
     *      reportDate（当前时间）--String
     *      todayNewMember（今日新增会员数） -> number
     *      totalMember（总会员数） -> number
     *      thisWeekNewMember（本周新增会员数） -> number
     *      thisMonthNewMember（本月新增会员数） -> number
     *      todayOrderNumber（今日预约数） -> number
     *      todayVisitsNumber（今日出游数） -> number
     *      thisWeekOrderNumber（本周预约数） -> number
     *      thisWeekVisitsNumber（本周出游数） -> number
     *      thisMonthOrderNumber（本月预约数） -> number
     *      thisMonthVisitsNumber（本月出游数） -> number
     *      hotSetmeal（热门套餐（取前4）） -> List<Map<String, Object>>
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String, Object> map = reportService.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 导出Excel报表
     * @param request
     * @param response
     */
    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            //远程调用报表服务获取报表数据
            Map<String, Object> result = reportService.getBusinessReportData();

            //1、取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //2、获得Excel模板文件绝对路径
            //file.separator这个代表系统目录中的间隔符，说白了就是斜线。
            String temlateRealPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";

            //3.读取模板文件创建Excel表格对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(temlateRealPath)));
            XSSFSheet sheet = workbook.getSheetAt(0);
            //4.往工作薄中写入数据
            XSSFRow row = sheet.getRow(2);//第三行
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4); //第五行
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5); //第六行
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7); //第八行
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日出游数

            row = sheet.getRow(8); //第九行
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周出游数

            row = sheet.getRow(9); //第十行
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月出游数

            int rowNum = 12;//从12行开始迭代
            for(Map map : hotSetmeal){//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //5、通过输出流进行文件下载
            ServletOutputStream out = response.getOutputStream();
            // 下载的数据类型（excel类型）
            response.setContentType("application/vnd.ms-excel");
            // 设置下载形式(通过附件的形式下载)
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(out);
            out.flush();
            out.close();
            workbook.close();
        }catch (Exception e){
            //跳转错误页面
            e.printStackTrace();
            request.getRequestDispatcher("/pages/error/downloadError.html").forward(request,response);
        }
    }
}
