package cn.zzuzl.controller;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by zhanglei53 on 2016/8/26.
 */
public class ExcelBuilder extends AbstractXlsView {

    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        workbook = (HSSFWorkbook) model.get("workbook");
        response.setContentType("application/vnd.ms-excel");
        // Flush byte array to servlet output stream.
        ServletOutputStream out = response.getOutputStream();
        if(workbook != null) {
            workbook.write(out);
            out.flush();
        }
    }
}
