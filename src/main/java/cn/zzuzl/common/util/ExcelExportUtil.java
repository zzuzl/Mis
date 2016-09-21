package cn.zzuzl.common.util;

import cn.zzuzl.dto.QualityJsonBean;
import cn.zzuzl.model.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExcelExportUtil {

    @javax.annotation.Resource
    Map<String, Resource> excelTemplates;

    public HSSFWorkbook genQualityXls(List<QualityJsonBean> scoreList,
                                      List<Activity> activityList,
                                      List<Project> projectList,
                                      HttpServletResponse response,
                                      boolean showScore,
                                      boolean showDetail) {
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("成绩单", "UTF-8") + ".xls");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("综测表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = null;
        HSSFRow scoreRow = null;
        int rowIndex = 2;
        int colIndex = 2;
        Map<String, Integer> colMap = new HashMap<String, Integer>();
        Map<String, Integer> rowMap = new HashMap<String, Integer>();

        // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
        row.createCell(0, HSSFCell.CELL_TYPE_STRING).setCellValue("学号");
        row.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue("姓名");
        row = sheet.createRow(1);
        // 填写成绩部分
        if (showScore) {
            for (QualityJsonBean bean : scoreList) {
                // 记录学号对应的行
                rowMap.put(bean.getSchoolNum(), rowIndex);
                scoreRow = sheet.createRow(rowIndex++);
                scoreRow.createCell(0, HSSFCell.CELL_TYPE_STRING).setCellValue(bean.getSchoolNum());
                scoreRow.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue(bean.getName());

                if (bean.getList() != null) {
                    for (TermScore termScore : bean.getList()) {
                        for (ScoreVO scoreVO : termScore.getScores()) {
                            if (!colMap.containsKey(scoreVO.getTitle())) {
                                row.createCell(colIndex, HSSFCell.CELL_TYPE_STRING).setCellValue(scoreVO.getTitle());
                                colMap.put(scoreVO.getTitle(), colIndex);
                                colIndex++;
                            }
                            scoreRow.createCell(colMap.get(scoreVO.getTitle()), HSSFCell.CELL_TYPE_STRING).setCellValue(scoreVO.getScore());
                        }
                    }
                }
            }
        }

        int firstIndex = colIndex;
        int secondIndex = colIndex;
        // 填充活动标题部分
        if (projectList != null) {
            for (Project project : projectList) {
                List<Item> items = project.getItemList();
                if (items == null || items.size() <= 0) {
                    continue;
                }
                sheet.addMergedRegion(new CellRangeAddress(0, 0, firstIndex, firstIndex + items.size() - 1));
                sheet.getRow(0).createCell(firstIndex, HSSFCell.CELL_TYPE_STRING).setCellValue(project.getTitle());
                for (Item item : items) {
                    colMap.put(item.getTitle(), secondIndex);
                    if (sheet.getRow(1) == null) {
                        sheet.createRow(1);
                    }
                    sheet.getRow(1).createCell(secondIndex, HSSFCell.CELL_TYPE_STRING).setCellValue(item.getTitle());
                    secondIndex++;
                }
                firstIndex += items.size();
            }
        }

        // 填充活动分数
        if (activityList != null) {
            for (Activity activity : activityList) {
                String schoolNum = activity.getStudent().getSchoolNum();
                if (!rowMap.containsKey(schoolNum)) {
                    rowMap.put(schoolNum, rowIndex);
                    sheet.createRow(rowIndex++);
                }
                row = sheet.getRow(rowMap.get(schoolNum));

                if (activity.getItem() != null && activity.getItem().getTitle() != null) {
                    int col = colMap.get(activity.getItem().getTitle());
                    HSSFCell c = row.getCell(col);
                    // 如果不存在该表格则创建一个
                    if (c == null) {
                        // 如果需要显示明细，则使用string，否则使用数字
                        if (showDetail) {
                            c = row.createCell(col, HSSFCell.CELL_TYPE_STRING);
                            c.getCellStyle().setWrapText(true);
                            c.setCellValue(activity.getTitle() + " " + activity.getScore());
                        } else {
                            row.createCell(col, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(activity.getScore());
                        }
                    } else {
                        if (showDetail) {
                            c.getCellStyle().setWrapText(true);
                            c.setCellValue(new HSSFRichTextString(c.getStringCellValue() + "\r\n" + activity.getTitle() + " " + activity.getScore()));
                        } else {
                            c.setCellValue(c.getNumericCellValue() + activity.getScore());
                        }
                    }
                }
            }
        }

        for (int i = 0; i < Math.max(colIndex, secondIndex); i++) {
            // 设置单元格宽度
            sheet.setColumnWidth(i, 4000);
        }

        return workbook;
    }
}
