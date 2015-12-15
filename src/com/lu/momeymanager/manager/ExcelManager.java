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

		// ��һ��������һ��webbook����Ӧһ��Excel�ļ�
		HSSFWorkbook wb = new HSSFWorkbook();
		// �ڶ�������webbook�����һ��sheet,��ӦExcel�ļ��е�sheet
		HSSFSheet sheet = wb.createSheet("��һ");
		// ����������sheet����ӱ�ͷ��0��,ע���ϰ汾poi��Excel����������������short
		HSSFRow row = sheet.createRow((int) 0);
		// ���Ĳ���������Ԫ�񣬲�����ֵ��ͷ ���ñ�ͷ����
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // ����һ�����и�ʽ

		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("��Ŀ");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("����");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("����");
		cell.setCellStyle(style);

		// ���岽��д��ʵ������ ʵ��Ӧ������Щ���ݴ����ݿ�õ���
		int count = 0;
		for (int i = 0; i < similarDateMoneyBeans.size(); i++) {

			for (InOutBean bean : similarDateMoneyBeans.get(i).getBeans()) {

				row = sheet.createRow((int) count + 1);
				row.createCell((short) 0).setCellValue(bean.getNumber());
				row.createCell((short) 1).setCellValue(bean.getBank());
				row.createCell((short) 2).setCellValue(bean.getDate());
				count++;
			}
			// ���Ĳ���������Ԫ�񣬲�����ֵ

		}
		// �����������ļ��浽ָ��λ��
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
