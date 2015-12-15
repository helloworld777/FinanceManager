package com.lu.momeymanager.manager;

import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.lu.momeymanager.bean.InOutBean;
import com.lu.momeymanager.bean.SimilarDateMoneyBean;
import com.lu.momeymanager.util.DateUtil;
import com.lu.momeymanager.util.FileUtil;

public class ExcelManager {
	@SuppressWarnings("deprecation")
	public static String excelToDisk(List<SimilarDateMoneyBean> similarDateMoneyBeans) {

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("表一");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("数目");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("银行");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("日期");
		cell.setCellStyle(style);

		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		int count = 0;
		for (int i = 0; i < similarDateMoneyBeans.size(); i++) {

			for (InOutBean bean : similarDateMoneyBeans.get(i).getBeans()) {

				row = sheet.createRow((int) count + 1);
				row.createCell((short) 0).setCellValue(bean.getNumber());
				row.createCell((short) 1).setCellValue(bean.getBank());
				row.createCell((short) 2).setCellValue(bean.getDate());
				count++;
			}
			// 第四步，创建单元格，并设置值

		}
		// 第六步，将文件存到指定位置
		String filename;
		try {
			filename=DateUtil.getDate3()+"_money.xls";
			FileOutputStream fout = new FileOutputStream(FileUtil.getExcelPath()+filename);
			wb.write(fout);
			fout.close();
			return FileUtil.getExcelPath()+filename;
		} catch (Exception e) {
			e.printStackTrace();
			filename=null;
			return null;
		}
		 
	}
}
