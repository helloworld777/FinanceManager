package com.lu.momeymanager.manager;

import com.lu.momeymanager.bean.InOutBean;
import com.lu.momeymanager.bean.SimilarDateMoneyBean;
import com.lu.momeymanager.util.DateUtil;
import com.lu.momeymanager.util.FileUtil;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;

public class ExcelManager {
	@SuppressWarnings("deprecation")
	public static String excelToDisk(List<SimilarDateMoneyBean> similarDateMoneyBeans) {

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("a");
		HSSFRow row = sheet.createRow((int) 0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("bbbbbb");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("ccccc");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("ddddd");
		cell.setCellStyle(style);

		int count = 0;
		for (int i = 0; i < similarDateMoneyBeans.size(); i++) {

			for (InOutBean bean : similarDateMoneyBeans.get(i).getBeans()) {

				row = sheet.createRow((int) count + 1);
				row.createCell((short) 0).setCellValue(bean.getNumber());
				row.createCell((short) 1).setCellValue(bean.getBank());
				row.createCell((short) 2).setCellValue(bean.getDate());
				count++;
			}

		}
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
