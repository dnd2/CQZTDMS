package com.infodms.dms.actions.report.service;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;



import jxl.Workbook;
import jxl.biff.FontRecord;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.dao.report.serviceReport.ActivityBalanceDetailDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsActivitySubjectPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class ActivityBalanceDetail extends HttpServlet  
{
	private Logger logger = Logger.getLogger(ActivityBalanceDetailDao.class);
	private final ActivityBalanceDetailDao dao = ActivityBalanceDetailDao.getInstance();
	private final String ACTIVITY_BALANCE_DETAIL_YX = "/jsp/report/service/Activity_Balance_DetailYX.jsp";
	public void ActivityBalanceDetailJS()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			String Command = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(Command)){
				String dealerName = request.getParamValue("dealerName");//经销商名称
				String subject_id = request.getParamValue("subject_id");
				String StratDate = request.getParamValue("StratDate");
				String EndDate = request.getParamValue("EndDate");
				String YX = request.getParamValue("YX");
				//分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页	
			
				Integer pageSize = 20;
				PageResult<Map<String, Object>> ps = dao.ActivityBalanceDetail(dealerName, subject_id, YX, StratDate, EndDate, curPage, pageSize);
				//分页方法 end
				act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
			}else{
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT E.* from TT_AS_ACTIVITY_SUBJECT E where" +
						" E.ACTIVITY_TYPE != "+Constant.SERVICEACTIVITY_TYPE_01);
				List<TtAsActivitySubjectPO> list= dao.select(TtAsActivitySubjectPO.class, sql.toString(),null);
				act.setOutData("list", list);
				act.setOutData("type","JS" );
				act.setForword(ACTIVITY_BALANCE_DETAIL_YX);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void ActivityBalanceDetailYX()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			String Command = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(Command)){
				String dealerName = request.getParamValue("dealerName");//经销商名称
				String subject_id = request.getParamValue("subject_id");
				String StratDate = request.getParamValue("StratDate");
				String EndDate = request.getParamValue("EndDate");
				String YX = request.getParamValue("YX");
				//分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页	
			
				Integer pageSize = 20;
				PageResult<Map<String, Object>> ps = dao.ActivityBalanceDetail(dealerName, subject_id, YX, StratDate, EndDate, curPage, pageSize);
				//分页方法 end
				act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
			}else{
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT E.* from TT_AS_ACTIVITY_SUBJECT E where" +
						" E.ACTIVITY_TYPE != "+Constant.SERVICEACTIVITY_TYPE_01);
				List<TtAsActivitySubjectPO> list= dao.select(TtAsActivitySubjectPO.class, sql.toString(),null);
				act.setOutData("list", list);
				act.setOutData("type","YX" );
				act.setForword(ACTIVITY_BALANCE_DETAIL_YX);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void  imporActivityBalance()
	{
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealerName = request.getParamValue("dealerName");//经销商名称
			String subject_id = request.getParamValue("subject_id");
			String StratDate = request.getParamValue("StratDate");
			String EndDate = request.getParamValue("EndDate");
			String YX = request.getParamValue("YX");
			List<Map<String, Object>> dateList = dao.inDate(dealerName, subject_id, YX, StratDate, EndDate);
			
			ResponseWrapper response = act.getResponse();
			// 导出的文件名
			String fileName = "活动结算明细.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			WritableWorkbook wbook = Workbook.createWorkbook(response.getOutputStream());
			WritableCellFormat format2=new WritableCellFormat(); 
			//把水平对齐方式指定为居中 
			format2.setAlignment(jxl.format.Alignment.CENTRE); 
			//把垂直对齐方式指定为居中
			format2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
			//设置颜色
			format2.setBackground(Colour.YELLOW);
			//设置边框
			format2.setBorder(Border.ALL, BorderLineStyle.DASHED);
			if(dateList.size() > 0)
			{
				int i = 0;
				for(Map<String, Object> map : dateList)
				{
					List<Map<String, Object>> subList= dao.SubActivity(dealerName, subject_id, YX,map.get("REPORT_DATE").toString());
					if(subList.size() > 0 )
					{
					    List<Map<String, Object>> banlecList= dao.getDeaerAct(dealerName, subject_id, YX, map.get("REPORT_DATE").toString(), subList);
						WritableSheet wsheet = wbook.createSheet(map.get("REPORT_DATE").toString(),i);
						wsheet.setColumnView(0,30);
						Label excelTitle = new Label(0, 0, "站代码  \\  科目",format2);
						wsheet.addCell(excelTitle);
						wsheet.mergeCells(0,0, 0, 2);
						
						 
						for(int j = 0; j < subList.size();j++)
						{
							  wsheet.setColumnView(j+1,30);
							 excelTitle = new Label(j+1, 0,subList.get(j).get("SUBJECT_NAME").toString() ,format2);
							 wsheet.addCell(excelTitle);
							 wsheet.mergeCells(j+1,0, j+1, 2);
						}
						int y = 3;
						for(int k = 0 ;k< banlecList.size(); k++)
						{
							excelTitle = new Label(0, y,banlecList.get(k).get("DEALER_CODE").toString() );
							wsheet.addCell(excelTitle);
							for(int j = 0; j < subList.size();j++)
							{
								excelTitle = new Label(j+1, y,banlecList.get(k).get("SUBJECT_NAME"+j) != null ? banlecList.get(k).get("SUBJECT_NAME"+j).toString() : ""  );
								wsheet.addCell(excelTitle);
							}
							y++;
						}
						
					}
					i++;
				}
				
			}else
			{
				WritableSheet wsheet = wbook.createSheet("下载为空",0);
				wsheet.setColumnView(0,40);
				Label excelTitle = new Label(0, 0, "此范围没有活动审批单审批到最终状态",format2);
				wsheet.addCell(excelTitle);
				wsheet.mergeCells(0,0, 0, 2);
			}
			wbook.write(); 
        	wbook.close(); 
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void  expront()
	{
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealerCode = request.getParamValue("dealerCode");
			String dealerName = request.getParamValue("dealerName");
			String balance_oder = request.getParamValue("balance_oder");
			String startDate = request.getParamValue("startDate");
			String endDate = request.getParamValue("endDate");
			String comm = request.getParamValue("comm");
			String balance_yieldly = request.getParamValue("balance_yieldly");
			
			ResponseWrapper response = act.getResponse();
			// 导出的文件名
			String fileName = "付款清单.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			WritableWorkbook wbook = Workbook.createWorkbook(response.getOutputStream());
			WritableFont font = new WritableFont(WritableFont.ARIAL,20,WritableFont.NO_BOLD);
			WritableCellFormat format2=new WritableCellFormat(font); 
			format2.setAlignment(jxl.format.Alignment.CENTRE); 
			WritableSheet wsheet = wbook.createSheet("Sheet1",1);
			wsheet.setColumnView(0,30);
			
			wsheet.addCell(addCells(0, 0, "服务站三包转付款清单",format2));
			wsheet.mergeCells(0,0, 12, 1);
			
			wsheet.addCell(addCells(0, 2, "凭证编号",null));
			wsheet.addCell(addCells(1, 2, "结算编号",null));
			wsheet.addCell(addCells(2, 2, "上报批次",null));
			wsheet.addCell(addCells(3, 2, "服务站代码",null));
			wsheet.addCell(addCells(4, 2, "服务站简称",null));
			wsheet.addCell(addCells(5, 2, "税率",null));
			wsheet.addCell(addCells(6, 2, "劳务费",null));
			wsheet.addCell(addCells(7, 2, "材料费",null));
			wsheet.addCell(addCells(8, 2, "合计",null));
			wsheet.addCell(addCells(9, 2, "劳务费发票号码",null));
			wsheet.addCell(addCells(10, 2, "材料费发票号码",null));
			wsheet.addCell(addCells(11, 2, "打印时间",null));
			wsheet.addCell(addCells(12, 2, "备注",null));
			ClaimBillMaintainDAO maintainDAO = new ClaimBillMaintainDAO();
			List<Map<String,Object>> mapList= maintainDAO.get_payMent(dealerCode, dealerName, balance_oder, balance_yieldly, startDate, endDate, logonUser);
			if(mapList.size() > 0)
			{
				int j = 3;
				int k = 1;
				for(Map<String,Object> map : mapList)
				{
					String TAX_RATE = "";
					String AMOUNT_SUM = map.get("AMOUNT_SUM").toString();
					String LABOUR_AMOUNT= map.get("LABOUR_AMOUNT").toString();
				    String PART_AMOUNT = ""+(Arith.sub(Double.parseDouble(map.get("AMOUNT_SUM").toString()), Double.parseDouble(map.get("LABOUR_AMOUNT").toString())));
					
				   if(map.get("TAX_RATE").toString().equals("94121001"))
				   {
					   TAX_RATE = "17";
				   }else if(map.get("TAX_RATE").toString().equals("94121002")){
					   AMOUNT_SUM = "" + (Arith.add( (Arith.mul( Double.parseDouble(PART_AMOUNT), 0.9)  ), Double.parseDouble(""+LABOUR_AMOUNT))) ;
					   PART_AMOUNT = ""+(Arith.mul( Double.parseDouble(PART_AMOUNT), 0.9)  );
					   TAX_RATE = "6";
				   }else
				   {
					   AMOUNT_SUM =""+ (Arith.div( Double.parseDouble(AMOUNT_SUM) , Double.parseDouble("1.17")  , 2));
			           PART_AMOUNT =""+ (Arith.div( Double.parseDouble(PART_AMOUNT) , Double.parseDouble("1.17") , 2));
			           LABOUR_AMOUNT =""+ (Arith.div( Double.parseDouble(LABOUR_AMOUNT) , Double.parseDouble("1.17") , 2));
			           
					   TAX_RATE = "4";
				   }
					
				   
					wsheet.addCell(addCells(0, j, map.get("SERIAL_NUMBER").toString() ,null));
					wsheet.addCell(addCells(1, j, map.get("BALANCE_ODER").toString(),null));
					wsheet.addCell(addCells(2, j, map.get("DEALER_CODE").toString() + map.get("START_DATE").toString(),null));
					wsheet.addCell(addCells(3, j, map.get("DEALER_CODE").toString(),null));
					wsheet.addCell(addCells(4, j, map.get("DEALER_NAME").toString(),null));
					wsheet.addCell(addCells(5, j, TAX_RATE,null));
					wsheet.addCell(addCells(6, j,  LABOUR_AMOUNT,null));
					wsheet.addCell(addCells(7, j, ""+PART_AMOUNT,null));
					wsheet.addCell(addCells(8, j, AMOUNT_SUM ,null));
					wsheet.addCell(addCells(9, j, map.get("LABOUR_RECEIPT").toString(),null));
					wsheet.addCell(addCells(10, j, map.get("PART_RECEIPT").toString(),null));
					wsheet.addCell(addCells(11, j, map.get("CREAT_DATE").toString(),null));
					if( map.get("REMARK") != null)
					{
						wsheet.addCell(addCells(12, j, map.get("REMARK").toString(),null));
					}
					 
					j++;
					k++;
				}
				
				WritableCellFormat format3=new WritableCellFormat(); 
				format3.setAlignment(jxl.format.Alignment.CENTRE); 
				
				wsheet.addCell(addCells(0, j, "编制",format3));
				
				wsheet.addCell(addCells(1, j, logonUser.getName() ,format3));
				wsheet.mergeCells(1,j, 2,j );
				
			wsheet.addCell(addCells(3, j, "日期",format3));
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
 			wsheet.addCell(addCells(4, j,  format.format(new Date()) ,format3));
				
				wsheet.mergeCells(4, j, 6 ,j );
				
				wsheet.addCell(addCells(7, j, "审核",format3));
				wsheet.addCell(addCells(8, j, "",format3));
				wsheet.mergeCells(8, j, 9 ,j );
				wsheet.addCell(addCells(10, j, "会计会签",format3));
				
				wsheet.addCell(addCells(11, j, "",format3));
				
				wsheet.addCell(addCells(12, j, "",format3));
				
			}
			
			
			
			
			wbook.write(); 
        	wbook.close(); 
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public Label addCells(int i ,int j ,String name,WritableCellFormat fomat)
	{
		Label excelTitle = null;
		if(fomat == null)
		{
			 excelTitle = new Label(i, j, name);
		}else
		{
			 excelTitle = new Label(i, j, name,fomat);
		}
		
		return excelTitle;
	}

}
