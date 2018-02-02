package com.songzi.channel.service.util;

import com.songzi.channel.domain.JhiOrder;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    private String fileName = "1111.xls";




    public final void renderMergedOutputModel(HttpServletRequest request, HttpServletResponse response, List<JhiOrder> orders) throws Exception {
        Workbook workbook = this.createWorkbook();
        this.buildExcelDocument(workbook, request, response, orders);
        this.renderWorkbook(workbook, response);
    }

    protected Workbook createWorkbook() {
        return new HSSFWorkbook();
    }

    protected void renderWorkbook(Workbook workbook, HttpServletResponse response) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
    }


    protected void buildExcelDocument(Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response,
                                      List<JhiOrder> orders) throws Exception {
        String agent = response.getHeader("User-Agent");

        // change the file name
        response.setHeader("content-disposition", "attachment;fileName="+fileName);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");

        response.setCharacterEncoding("utf-8");

        // create excel xls sheet
        Sheet sheet = workbook.createSheet("Spring MVC AbstractXlsView");

        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("订单号");
        header.createCell(1).setCellValue("产品名称");
        header.createCell(2).setCellValue("生日信息");
        header.createCell(3).setCellValue("金额");
        header.createCell(4).setCellValue("状态");
        header.createCell(5).setCellValue("付款方式");
        header.createCell(6).setCellValue("购买时间");
        header.createCell(7).setCellValue("购买渠道");

        // Create data cells
        int rowCount = 1;
        for (JhiOrder order : orders){
            Row courseRow = sheet.createRow(rowCount++);
            courseRow.createCell(0).setCellValue(order.getCode());
            courseRow.createCell(1).setCellValue(order.getProductName());
            courseRow.createCell(2).setCellValue(order.getBirthInfo());
            courseRow.createCell(3).setCellValue(order.getPrice());
            courseRow.createCell(4).setCellValue(order.getStatus() + "");
            courseRow.createCell(5).setCellValue(order.getPayType() + "");
            courseRow.createCell(6).setCellValue(order.getOrderDate() + "");
            courseRow.createCell(7).setCellValue(order.getChannelName());
        }
    }
}
