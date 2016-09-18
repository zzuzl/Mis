package cn.zzuzl.common.util;

import cn.zzuzl.dto.QualityJsonBean;
import cn.zzuzl.model.ScoreVO;
import cn.zzuzl.model.TermScore;
import com.auth0.jwt.internal.org.apache.commons.lang3.time.DateFormatUtils;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.ResultSetMetaData;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExcelExportUtil {

    @javax.annotation.Resource
    Map<String, Resource> excelTemplates;

    private HSSFWorkbook createXls(Map<String, Object> context, HttpServletResponse response, String tplName) {
        XLSTransformer transformer = new XLSTransformer();
        InputStream template = null;
        HSSFWorkbook workbook = null;
        String templateName = (String) context.get("templateName");
        try {
            template = excelTemplates.get(templateName).getInputStream();
            workbook = (HSSFWorkbook) transformer.transformXLS(new BufferedInputStream(template), context);
            // workbook = splitBook(context);
            // 设置文件名
            String fileName = DateFormatUtils.format(new Date(), "yyyyMMdd");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(tplName + fileName, "UTF-8") + ".xls");
        } catch (IOException e) {

            throw new RuntimeException("读取文件模板失败，IO错误");

        } catch (ParsePropertyException e) {
            throw new RuntimeException("读取Excel文件模板中变量值失败，请检查Excel配置变量值");

        } catch (InvalidFormatException e) {
            throw new RuntimeException("转换Excel文件模板失败，请检查Excel格式");
        } finally {
            IOUtils.closeQuietly(template);
        }
        return workbook;
    }

    public HSSFWorkbook createScoreXls(Map<String, Object> context, HttpServletResponse response) {
        return createXls(context, response, "成绩单");
    }

    public HSSFWorkbook gen(List<QualityJsonBean> list, HttpServletResponse response) {
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("成绩单", "UTF-8") + ".xls");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("成绩单");
        HSSFRow row = sheet.createRow((short) 0);
        HSSFCell cell = null;
        HSSFRow scoreRow = null;
        int rowIndex = 1;

        boolean flag = false;
        for (QualityJsonBean bean : list) {
            scoreRow = sheet.createRow((short) rowIndex++);
            int index = 0, _index = 0;
            if (!flag) {
                row.createCell(index++, HSSFCell.CELL_TYPE_STRING).setCellValue("学号");
                row.createCell(index++, HSSFCell.CELL_TYPE_STRING).setCellValue("姓名");
            }
            scoreRow.createCell(_index++, HSSFCell.CELL_TYPE_STRING).setCellValue(bean.getSchoolNum());
            scoreRow.createCell(_index++, HSSFCell.CELL_TYPE_STRING).setCellValue(bean.getName());

            for (TermScore termScore : bean.getList()) {
                for (ScoreVO scoreVO : termScore.getScores()) {
                    if (!flag) {
                        row.createCell(index++, HSSFCell.CELL_TYPE_STRING).setCellValue(scoreVO.getTitle());
                    }
                    scoreRow.createCell(_index++, HSSFCell.CELL_TYPE_STRING).setCellValue(scoreVO.getScore());
                }
            }
            flag = true;
        }

        return workbook;
    }
}
