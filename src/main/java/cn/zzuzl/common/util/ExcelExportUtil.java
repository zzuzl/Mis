package cn.zzuzl.common.util;

import cn.zzuzl.dto.QualityJsonBean;
import cn.zzuzl.model.ScoreVO;
import cn.zzuzl.model.TermScore;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Component
public class ExcelExportUtil {

    @javax.annotation.Resource
    Map<String, Resource> excelTemplates;

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

            if(bean.getList() != null) {
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
        }

        return workbook;
    }
}
