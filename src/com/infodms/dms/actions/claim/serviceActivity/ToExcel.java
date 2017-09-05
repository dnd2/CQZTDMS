package com.infodms.dms.actions.claim.serviceActivity;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import jxl.Workbook;
import jxl.write.Label;

public class ToExcel {
	/**
	 * toExcel
	 * 
	 * @param response
	 * @param request
	 * @param head
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public static Object toExcel(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {
		OutputStream out = null;
		String name = "生产商再索赔结算费用清单.xls";
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
					ws.addCell(new Label(i, z, str[i]));
				}
			}
			wwb.write();
			wwb.close();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != out) {
				out.close();
			}
		}
		return null;
	}

}
