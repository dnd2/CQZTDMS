package com.infodms.dms.actions.claim.basicData;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.AreaProvinceBean;
import com.infodms.dms.bean.MessErr;
import com.infodms.dms.bean.PriceAdjustBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.claim.basicData.ClaimBasicLaborPriceDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtAsMailPrintDetailPO;
import com.infodms.dms.po.TtAsMileageChangePO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrLabourPricePO;
import com.infodms.dms.po.TtAsWrMalfunctionPositionPO;
import com.infodms.dms.po.TtAsWrModelGroupPO;
import com.infodms.dms.po.TtAsWrModelPricePO;
import com.infodms.dms.po.TtAsWrVinRulePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 索赔工时单价设定
 * @author Administrator
 *
 */

public class ClaimBasicLabourPrice  extends BaseImport{
	private ActionContext act = null ;
	private RequestWrapper req = null ;
	private AclUserBean user = null ;
	
	private Logger logger = Logger.getLogger(ClaimBasicLabourPrice.class);
	private ClaimBasicLaborPriceDao dao = ClaimBasicLaborPriceDao.getInstance() ;
	
	//轿车
	private final String MAIN_URL = "/jsp/claim/basicData/claimBasicData.jsp" ;
	private final String ADD_URL = "/jsp/claim/basicData/claimBasicDataAdd.jsp" ;
	private final String LABOR_LIST_URL = "/jsp/claim/basicData/laborListSel.jsp" ;
	private final String LABOR_LIST_URL_1 = "/jsp/claim/basicData/laborListSel1.jsp" ;
	private final String LABOR_LIST_URL_2 = "/jsp/claim/basicData/laborListSel2.jsp" ;
	private final String UPDATE_URL = "/jsp/claim/basicData/claimBasicDataModify.jsp" ; 
	//微车
	private final String WC_MAIN_URL = "/jsp/claim/basicData/claimBasicDataWC.jsp" ;
	private final String WC_MODIFY_URL = "/jsp/claim/basicData/claimBasicDataModifyWC.jsp" ;
	private final String ADJST_URL = "/jsp/claim/basicData/adjustLaborHoursetpre.jsp" ;
	private final String ADJST_MODIFY_URL="/jsp/claim/basicData/adjustModify.jsp";
	//旧件库区库位维护
	private final String PART_RESER_URL= "/jsp/claim/basicData/partReserQuery.jsp";
	//批量导入
	private final String INPORT_PER="/jsp/claim/basicData/importPer.jsp";
	private final String INPORT_PER_RULE="/jsp/claim/basicData/inportPerRule.jsp";
	private  final String INPUT_ERRORR_URL= "/jsp/claim/basicData/inputerrorR.jsp";
	private  final String INPUT_ERROR_URL= "/jsp/claim/basicData/inputerror.jsp";//数据导入出错页面
	private  final String INPORT_SURE = "/jsp/claim/basicData/importSureDo.jsp";//数据导入确认页面
	private  final String INPORT_SURER = "/jsp/claim/basicData/importSureRDo.jsp";//数据导入确认页面
	//未维护基础数据的经销商查询
	private final String noBaseDataPer = "/jsp/claim/basicData/noBaseDataPer.jsp" ;//
	private final String mailPrintPer = "/jsp/claim/basicData/mailPrintPer.jsp" ;//信封打印
	private final String mailPrintPer2 = "/jsp/claim/basicData/mailPrintDetail.jsp" ;//信封打印
	private final String mailPrint = "/jsp/claim/basicData/mailPrint.jsp" ;
	private final String mailPrint2 = "/jsp/claim/basicData/mailPrint2.jsp" ;//ems
	
	//单据里程修改
	private final String mailModPer = "/jsp/claim/basicData/mailModPer.jsp" ;//单据里程修改
	private final String mailModAddPer = "/jsp/claim/basicData/mailModAddPer.jsp" ;//单据里程修改新增
	private final String modelPricePer = "/jsp/claim/basicData/modelPricePer.jsp" ;
	private final String modelPriceAdd = "/jsp/claim/basicData/modelPriceAdd.jsp" ;//单据里程修改新增
	
	/*
	 * 索赔工时单价设定主页面初始化
	 */
	public void labourPriceInit(){
		act = ActionContext.getContext() ;
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			List<TtAsWrLabourPricePO> lists = dao.getExistLaborList01(user.getCompanyId()) ;
			act.setOutData("lists", lists);
			act.setForword(MAIN_URL);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(user, be);
			act.setException(be);
		}
	}
	public void laborListQuery1(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ;
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			act.getResponse().setContentType("application/json");
			String code = req.getParamValue("wrgroup_code");
			StringBuffer con = new StringBuffer() ;
			if(StringUtil.notNull(code))
				con.append("and upper(wrgroup_code) like '%").append(code.toUpperCase()).append("%'\n") ;
			//con.append("and p.oem_company_id=").append(user.getCompanyId()).append("\n");
			int pageSize = 30 ;
			int curPage = req.getParamValue("curPage") != null ? Integer.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<TtAsWrModelGroupPO> ps = dao.getLaborList01(con.toString(),pageSize, curPage) ;
			act.setOutData("ps", ps);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定->查询");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	/**
	 * 批量导入跳转
	 */
	public void inportPer(){
		act = ActionContext.getContext() ;
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(INPORT_PER);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(user, be);
			act.setException(be);
		}
	}
	/**
	 * 批量导入预警规则跳转
	 */
	public void inportPerRule(){
		act = ActionContext.getContext() ;
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(INPORT_PER_RULE);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(user, be);
			act.setException(be);
		}
	}
	/**
	 * 批量导入模板下载
	 */
	@SuppressWarnings("unchecked")
	public void download(){
		act = ActionContext.getContext() ;
		user =(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		OutputStream os = null;
		try {
			ResponseWrapper response = act.getResponse();
			List<List<Object>> list = new ArrayList<List<Object>>();
			String[] listHead = new String[3];
			listHead[0]="经销商代码";
			listHead[1]="车系大类";
			listHead[2]="工时单价";
			
			List list1=new ArrayList();
			String[]detail=new String[3];
			detail[0]="JS00212";
			detail[1]="A101";
			detail[2]="22";
			list1.add(detail);
	    	com.infodms.dms.actions.claim.basicData.ToExcel.toExceLabourPrice(ActionContext.getContext().getResponse(), request, listHead, list1);
	   /**
	    *  以下 是 只有表头，木有示例 数据的生成方法
	    */
//			// 导出的文件名
//			String fileName = "索赔工时单价设定.xls";
//			// 导出的文字编码
//			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
//			response.setContentType("Application/text/xls");
//		    response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
//			os = response.getOutputStream();
//			CsvWriterUtil.createXlsFile(list, os);
//			os.flush();	
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔工时单价设定");
			logger.error(user, e1);
			act.setException(e1);
		}
	}
	
	
	
	public void downloadVin(){
		act = ActionContext.getContext() ;
		user =(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		OutputStream os = null;
		try {
			ResponseWrapper response = act.getResponse();
			List<List<Object>> list = new ArrayList<List<Object>>();
			String[] listHead = new String[2];
			listHead[0]="VIN";
			listHead[1]="服务站代码";
			List list1=new ArrayList();
			String[]detail=new String[2];
			detail[0]="LVFAB2ADXDG128922";
			detail[1]="202302";
			list1.add(detail);
	    	com.infodms.dms.actions.claim.basicData.ToExcel.toExceLabourPrice2(ActionContext.getContext().getResponse(), request, listHead, list1);
	   /**
	    *  以下 是 只有表头，木有示例 数据的生成方法
	    */
//			// 导出的文件名
//			String fileName = "索赔工时单价设定.xls";
//			// 导出的文字编码
//			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
//			response.setContentType("Application/text/xls");
//		    response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
//			os = response.getOutputStream();
//			CsvWriterUtil.createXlsFile(list, os);
//			os.flush();	
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔工时单价设定");
			logger.error(user, e1);
			act.setException(e1);
		}
	}
	
   
	
	public void downloadDearle(){
		act = ActionContext.getContext() ;
		user =(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		OutputStream os = null;
		try {
			ResponseWrapper response = act.getResponse();
			List<List<Object>> list = new ArrayList<List<Object>>();
			String[] listHead = new String[1];
			listHead[0]="经销商代码";
			
			List list1=new ArrayList();
			String[]detail=new String[1];
			detail[0]="S601715";
			list1.add(detail);
	    	com.infodms.dms.actions.claim.basicData.ToExcel.toExceDearle(ActionContext.getContext().getResponse(), request, listHead, list1);
	   /**
	    *  以下 是 只有表头，木有示例 数据的生成方法
	    */
//			// 导出的文件名
//			String fileName = "索赔工时单价设定.xls";
//			// 导出的文字编码
//			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
//			response.setContentType("Application/text/xls");
//		    response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
//			os = response.getOutputStream();
//			CsvWriterUtil.createXlsFile(list, os);
//			os.flush();	
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔工时单价设定");
			logger.error(user, e1);
			act.setException(e1);
		}
	}
	
	
	
	
	public void downloadRule(){
		act = ActionContext.getContext() ;
		user =(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		OutputStream os = null;
		try {
			ResponseWrapper response = act.getResponse();
			List<List<Object>> list = new ArrayList<List<Object>>();
			String[] listHead = new String[6];
			listHead[0]="导入类型";
			listHead[1]="预警等级";
			listHead[2]="配件代码或者关注部位代码";
			listHead[3]="法定次数";
			listHead[4]="预警次数";
			listHead[5]="法规";
			
			List list1=new ArrayList();
			String[]detail=new String[6];
			detail[0]="关注部位";
			detail[1]="一级";
			detail[2]="BW103";
			detail[3]="1";
			detail[4]="2";
			detail[5]="***********";
			String[]detail1=new String[6];
			detail1[0]="易损件";
			detail1[1]="三级";
			detail1[2]="********";
			detail1[3]="5";
			detail1[4]="2";
			detail1[5]="***********";
			list1.add(detail);
			list1.add(detail1);
	    	com.infodms.dms.actions.claim.basicData.ToExcel.toExceLabourPrice1(ActionContext.getContext().getResponse(), request, listHead, list1);
	   /**
	    *  以下 是 只有表头，木有示例 数据的生成方法
	    */
//			// 导出的文件名
//			String fileName = "索赔工时单价设定.xls";
//			// 导出的文字编码
//			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
//			response.setContentType("Application/text/xls");
//		    response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
//			os = response.getOutputStream();
//			CsvWriterUtil.createXlsFile(list, os);
//			os.flush();	
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔工时单价设定");
			logger.error(user, e1);
			act.setException(e1);
		}
	}
	
	public String judeRule(String PART_WR_TYPE)
	{
		if(PART_WR_TYPE.equals("关注部位"))
		{
			PART_WR_TYPE = "94031003";
		}else if(PART_WR_TYPE.equals("易损件"))
		{
			PART_WR_TYPE = "94031002";
		}else
		{
			PART_WR_TYPE = "此功能只对易损件和关注部位进行导入";
		}
		return PART_WR_TYPE;
	}
	
	public String VR_LEVEL(String VR_LEVEL,String PART_WR_TYPE)
	{
		if(PART_WR_TYPE.equals("94031003"))
		{
			if(VR_LEVEL.equals("一级"))
			{
				VR_LEVEL = "94011001";
			}else
			{
				VR_LEVEL = "此等级不符合规范";
			}
		}else if(PART_WR_TYPE.equals("94031002"))
		{
			if(VR_LEVEL.equals("一级"))
			{
				VR_LEVEL = "94011001";
			}else if(VR_LEVEL.equals("二级"))
			{
				VR_LEVEL = "94011002";
			}else if(VR_LEVEL.equals("三级"))
			{
				VR_LEVEL = "94011003";
			}else
			{
				VR_LEVEL = "此等级不符合规范";
			}
		}
		
		return VR_LEVEL;
	}
	
	public boolean number(String num)
	{
		String regex = "^[0-9]*$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(num);
		return matcher.find();
	}
	
	public void excelOperateRerRule()
	{
		act = ActionContext.getContext();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try {
			List<MessErr> listM = new ArrayList<MessErr>();
			List<TtAsWrVinRulePO> listR = new ArrayList<TtAsWrVinRulePO>();
			RequestWrapper request = act.getRequest();
			FileObject uploadFile = request.getParamObject("uploadFile");
			String fileName = uploadFile.getFileName();
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
			ByteArrayInputStream is = new ByteArrayInputStream(uploadFile.getContent());
			Workbook wb = Workbook.getWorkbook(is);
			Sheet[] sheets = wb.getSheets();
			Sheet sheet = null;
			Cell cell = null;
			for (int i = 0; i < sheets.length; i++) {
				sheet = sheets[i];
				int totalRows = sheet.getRows();
				for (int j = 1; j < totalRows; j++) {
					Cell[] cells = sheet.getRow(j);
					TtAsWrVinRulePO rulePO = new TtAsWrVinRulePO();
					String PART_WR_TYPE = "";
					String VR_LEVEL = "";
					for (int k = 0; k < cells.length; k++) {
						cell = cells[k];
						if(k == 0)
						{
							if(!Utility.testString(cell.getContents()))
							{
								MessErr err = new MessErr();
								err.setHead("第"+(j+1)+"行"+"第"+k+"列");
								err.setName(sheet.getRow(0)[k].getContents());
								err.setMess("此行不能不空");
								listM.add(err);
							}else
							{
								PART_WR_TYPE = judeRule(cell.getContents().trim());
								if(PART_WR_TYPE.equals("此功能只对易损件和关注部位进行导入"))
								{
									MessErr err = new MessErr();
									err.setHead("第"+(j+1)+"行"+"第"+k+"列");
									err.setName(sheet.getRow(0)[k].getContents());
									err.setMess(PART_WR_TYPE);
									listM.add(err);
								}else
								{
									rulePO.setPartWrType(Integer.parseInt(PART_WR_TYPE));
								}
							}
							
						}
						else if(k == 1)
						{
							if(!Utility.testString(cell.getContents()))
							{
								MessErr err = new MessErr();
								err.setHead("第"+(j+1)+"行"+"第"+k+"列");
								err.setName(sheet.getRow(0)[k].getContents());
								err.setMess("此行不能不空");
								listM.add(err);
							}else{
								VR_LEVEL = VR_LEVEL(cell.getContents().trim(),PART_WR_TYPE);
								if(VR_LEVEL.equals("此等级不符合规范"))
								{
									MessErr err = new MessErr();
									err.setHead("第"+(j+1)+"行"+"第"+k+"列");
									err.setName(sheet.getRow(0)[k].getContents());
									err.setMess(VR_LEVEL);
									listM.add(err);
								}else
								{
									rulePO.setVrLevel(Integer.parseInt(VR_LEVEL));
								}
							}
							
						}
						else if(k == 2 )
						{
							if(!Utility.testString(cell.getContents()))
							{
								MessErr err = new MessErr();
								err.setHead("第"+(j+1)+"行"+"第"+k+"列");
								err.setName(sheet.getRow(0)[k].getContents());
								err.setMess("此行不能不空");
								listM.add(err);
							}else
							{
								if(PART_WR_TYPE.endsWith("94031002"))
								{
									TmPtPartBasePO basePO = new TmPtPartBasePO();
									basePO.setPartCode(cell.getContents().trim());
									if(dao.select(basePO).size() == 0)
									{
										MessErr err = new MessErr();
										err.setHead("第"+(j+1)+"行"+"第"+k+"列");
										err.setName(sheet.getRow(0)[k].getContents());
										err.setMess("配件代码不存在");
										listM.add(err);
									}else
									{
										rulePO.setVrPartCode(cell.getContents().trim());
									}
									
									
								}else if(PART_WR_TYPE.endsWith("94031003"))
								{
									TtAsWrMalfunctionPositionPO positionPO = new TtAsWrMalfunctionPositionPO();
									positionPO.setPosCode(cell.getContents().trim());
									if(dao.select(positionPO).size() == 0)
									{
										MessErr err = new MessErr();
										err.setHead("第"+(j+1)+"行"+"第"+k+"列");
										err.setName(sheet.getRow(0)[k].getContents());
										err.setMess("关注部位代码不存在");
										listM.add(err);
									}else
									{
										rulePO.setVrPartCode(cell.getContents().trim());
									}
									
								}
							}
							
						}
						else if(k == 3)
						{
							if(!Utility.testString(cell.getContents()))
							{
								MessErr err = new MessErr();
								err.setHead("第"+(j+1)+"行"+"第"+k+"列");
								err.setName(sheet.getRow(0)[k].getContents());
								err.setMess("此行不能不空");
								listM.add(err);
							}else
							{
								if(!(number(cell.getContents().trim())))
								{
									MessErr err = new MessErr();
									err.setHead("第"+(j+1)+"行"+"第"+k+"列");
									err.setName(sheet.getRow(0)[k].getContents());
									err.setMess("此数据无效");
									listM.add(err);
								}
								else if(PART_WR_TYPE.equals("94031003"))
								{
									if(!(cell.getContents().trim().equals("1") || cell.getContents().trim().equals("2")))
									{
										MessErr err = new MessErr();
										err.setHead("第"+(j+1)+"行"+"第"+k+"列");
										err.setName(sheet.getRow(0)[k].getContents());
										err.setMess("关注部法定天数只能为1 或者 2");
										listM.add(err);
									}else
									{
										rulePO.setVrLaw(Integer.parseInt(cell.getContents().trim()));
									}
								}else
								{
									    rulePO.setVrLaw(Integer.parseInt(cell.getContents().trim()));
								}
							}
							
							
						}else if(k == 4)
						{
							if(!Utility.testString(cell.getContents()))
							{
								MessErr err = new MessErr();
								err.setHead("第"+(j+1)+"行"+"第"+k+"列");
								err.setName(sheet.getRow(0)[k].getContents());
								err.setMess("此行不能不空");
								listM.add(err);
							}
							else if(!(number(cell.getContents().trim())))
							{
								MessErr err = new MessErr();
								err.setHead("第"+(j+1)+"行"+"第"+k+"列");
								err.setName(sheet.getRow(0)[k].getContents());
								err.setMess("此数据无效");
								listM.add(err);
							}
							else 
							{
								rulePO.setVrWarranty(Integer.parseInt(cell.getContents().trim()));
							}
							
						}else if(k == 5)
						{
							if(!Utility.testString(cell.getContents()))
							{
								MessErr err = new MessErr();
								err.setHead("第"+(j+1)+"行"+"第"+k+"列");
								err.setName(sheet.getRow(0)[k].getContents());
								err.setMess("此行不能不空");
								listM.add(err);
							}else{
								rulePO.setVrLawStandard(cell.getContents().trim());
							}
							
						}
						}
					listR.add(rulePO);
					}
				
				}
			if(listM.size()>0){
				act.setOutData("errorInfo", listM);
				act.setForword(INPUT_ERRORR_URL);
			}else{
				act.setOutData("list", listR);
				act.setForword(INPORT_SURER);
			}
			
		} catch (Exception e) {// 异常方法
			BizException e1 = null;
			if(e instanceof BizException){
				e1 = (BizException)e;
			}else{
				new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"文件读取错误");
			}
			logger.error(user,e1);
			act.setException(e1);
		}
	}
	
	public void excelOperate(){
		act = ActionContext.getContext();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		try {
			List<Map<String,String>> errorInfo= new ArrayList<Map<String,String>>();
			RequestWrapper request = act.getRequest();
			long maxSize=1024*1024*5;
			int errNum = insertIntoTmp(request, "uploadFile",3,3,maxSize);
			
			String err="";
			
			if(errNum!=0){
				switch (errNum) {
				case 1:
					err+="文件列数过多!";
					break;
				case 2:
					err+="空行不能大于三行!";
					break;
				case 3:
					err+="文件不能为空!";
					break;
				case 4:
					err+="文件不能为空!";
					break;
				case 5:
					err+="文件不能大于!";
					break;
				default:
					break;
				}
			}
			if(!"".equals(err)){
				act.setOutData("error", err);
				act.setForword(INPUT_ERROR_URL);
			}else{
				List<Map> list=getMapList();
				List<Map<String,String>> voList = new ArrayList<Map<String,String>>();
				loadVoList(voList,list, errorInfo);
				if(errorInfo.size()>0){
					act.setOutData("errorInfo", errorInfo);
					act.setForword(INPUT_ERROR_URL);
				}else{
					act.setOutData("list", voList);
					act.setForword(INPORT_SURE);
				}
				
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = null;
			if(e instanceof BizException){
				e1 = (BizException)e;
			}else{
				new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"文件读取错误");
			}
			logger.error(user,e1);
			act.setException(e1);
		}
	}
	private void loadVoList(List<Map<String,String>> voList,List<Map> list,List<Map<String,String>> errorInfo){
		if(null==list){
			list=new ArrayList();
		}
		for(int i=0;i<list.size();i++){
			Map map=list.get(i);
			if(null==map){
				map=new HashMap<String, Cell[]>();
			}
			Set<String> keys=map.keySet();
			Iterator it=keys.iterator();
			String key="";
			while(it.hasNext()){
				key=(String)it.next();
				Cell[] cells=(Cell[])map.get(key);
					Map<String, String> tempmap = new HashMap<String, String>();
					if ("".equals(cells[0].getContents().trim())) {
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "经销商代码");
						errormap.put("3", "空");
						errorInfo.add(errormap);
					} else {
						List<Map<String,Object>> dealerList = dao.checkDealer(cells[0].getContents().trim());
						if (dealerList.size() != 1) {
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key+ "行");
							errormap.put("2", "经销商代码");
							errormap.put("3", "不存在");
							errorInfo.add(errormap);
						} else {
							tempmap.put("4", dealerList.get(0).get("DEALER_ID").toString());
							tempmap.put("5", dealerList.get(0).get("DEALER_NAME").toString());
						}
					}
					tempmap.put("1", cells[0].getContents().trim());
					if ("".equals(cells[1].getContents().trim())) {
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "车型大类");
						errormap.put("3", "空");
						errorInfo.add(errormap);
					} else {
						List<Map<String,Object>> vehl = dao.checkVeh(cells[1].getContents().trim());
						if (vehl.size() != 1) {
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key+ "行");
							errormap.put("2", "车型大类");
							errormap.put("3", "不存在");
							errorInfo.add(errormap);
						} else {
							tempmap.put("x", cells[1].getContents().trim());
						}
					}
					tempmap.put("2", cells[1].getContents().trim());
					if (cells.length < 2|| CommonUtils.isEmpty(cells[1].getContents())) {
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "工时单价");
						errormap.put("3", "空");
						errorInfo.add(errormap);
					} else {
						String accTemp = cells[2].getContents().trim();
						if(null == accTemp || "".equals(accTemp))
						{
							accTemp = "0.00";
						}
						else
						{
							accTemp = accTemp.replace(",", "");
						}
						String regex = "((^[0]([.]{1}(\\d)*)?$)|(^[1-9]+(\\d)*([.]{1}(\\d)*)?$))";
						Pattern pattern = Pattern.compile(regex);
						Matcher matcher = pattern.matcher(accTemp);
						if(matcher.find())
						{
							Double amount = Double.parseDouble(accTemp);
							NumberFormat numberFormat = NumberFormat.getNumberInstance();  
							numberFormat.setMinimumFractionDigits(2);
							numberFormat.setMaximumFractionDigits(2);
							numberFormat.setMaximumIntegerDigits(10);  
							tempmap.put("3", numberFormat.format(amount));
						}
						else
						{
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
							errormap.put("2", "工时单价");
							errormap.put("3", "为非法数值!");
							errorInfo.add(errormap);
						}
					}
					voList.add(tempmap);
			}	
		}
	}
	//工时单价 确认导入
	@SuppressWarnings("unchecked")
	public void labourImportAdd(){
		act = ActionContext.getContext();
		 user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		act.getResponse().setContentType("application/json");
		try {
			RequestWrapper  request = act.getRequest();
			String strCount = CommonUtils.checkNull(request.getParamValue("count")); //导入数据总条数
			
			int count = Integer.parseInt(strCount);
			for(int i=1;i<count; i++){
				String model = CommonUtils.checkNull(request.getParamValue("model"+i));//车型大类
				String dealerId = CommonUtils.checkNull(request.getParamValue("id"+i));//经销商ID
				String price = CommonUtils.checkNull(request.getParamValue("price"+i));//工时单价
				 
				TtAsWrLabourPricePO po = new TtAsWrLabourPricePO() ;
				po.setDealerId(Long.parseLong(dealerId));
				po.setModeType(model);
				po = dao.existLaborPrice(po);
				// 此经销商对应的工时大类不存在则执行添加操作
				if(po==null){
					
					StringBuffer sql= new StringBuffer();
					sql.append("\n" );
					sql.append("SELECT a.GROUP_CODE from  TM_VHCL_MATERIAL_GROUP a  ,TT_AS_WR_MODEL_GROUP b where\n" );
					sql.append("a.PARENT_GROUP_ID =\n" );
					sql.append("(  SELECT t.GROUP_ID from TM_VHCL_MATERIAL_GROUP t where t.GROUP_CODE = '"+model+"')\n" );
					sql.append("and a.GROUP_CODE = b.WRGROUP_CODE");
					List<TmVhclMaterialGroupPO>  list= dao.select(TmVhclMaterialGroupPO.class, sql.toString(), null);
					
					if(list.size() > 0)
					{
						for(TmVhclMaterialGroupPO groupPO : list)
						{
							po = new TtAsWrLabourPricePO() ;
							po.setDealerId(Long.parseLong(dealerId));
							po.setSeriesCode(model);
							po.setModeType(groupPO.getGroupCode());
							po.setOemCompanyId(user.getCompanyId());
							po.setCreateBy(user.getUserId());
							po.setCreateDate(new Date());
							po.setId(Utility.getLong(SequenceManager.getSequence("")));
							po.setLabourPrice(Double.parseDouble(price));
							dao.insert(po);
						}
						
					}
					
					
				}else{
					// 此经销商对应的工时大类存在则执行修改操作
					TtAsWrLabourPricePO po2 = new TtAsWrLabourPricePO() ;
					po2.setUpdateBy(user.getUserId());
					po2.setUpdateDate(new Date());
					po2.setLabourPrice(Double.parseDouble(price));
					TtAsWrLabourPricePO p1 = new TtAsWrLabourPricePO() ;
					p1.setSeriesCode(po.getSeriesCode());
					p1.setDealerId(po.getDealerId());
					dao.update(p1, po2);
				}
			}
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时单价导入到数据库中");
			logger.error(user,e1);
			act.setException(e1);
		}
}
	
	//预警规则 确认导入
	@SuppressWarnings("unchecked")
	public void labourImportRAdd(){
		act = ActionContext.getContext();
		 user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		act.getResponse().setContentType("application/json");
		try {
				RequestWrapper  request = act.getRequest();
				String[] partWrTypes= request.getParamValues("partWrType");
				String[] vrLevels= request.getParamValues("vrLevel");
				String[] vrPartCodes= request.getParamValues("vrPartCode");
				String[] vrLaws= request.getParamValues("vrLaw");
				String[] vrWarrantys= request.getParamValues("vrWarranty");
				String[] vrLawStandards= request.getParamValues("vrLawStandard");
				if(partWrTypes != null && partWrTypes.length >0)
				{
					for(int i = 0 ;i < partWrTypes.length; i++)
					{
						if(partWrTypes[i].equals(Constant.PART_WR_TYPE_3))
						{
							TtAsWrVinRulePO rulePO = new TtAsWrVinRulePO();
							rulePO.setPartWrType(Integer.parseInt(partWrTypes[i]));
							rulePO.setVrLevel(Integer.parseInt(vrLevels[i]));
							rulePO.setVrLaw(Integer.parseInt(vrLaws[i]));
							rulePO.setVrPartCode(vrPartCodes[i]);
							if(dao.select(rulePO).size() >0 )
							{
								rulePO = (TtAsWrVinRulePO)dao.select(rulePO).get(0);
								TtAsWrVinRulePO rulePO1 = new TtAsWrVinRulePO();
								rulePO1.setVrId(rulePO.getVrId());
								TtAsWrVinRulePO rulePO2 = new TtAsWrVinRulePO();
								rulePO2.setVrWarranty(Integer.parseInt(vrWarrantys[i]));
								rulePO2.setVrLawStandard(vrLawStandards[i]);
								dao.update(rulePO1, rulePO2);
							}else
							{
								rulePO.setVrWarranty(Integer.parseInt(vrWarrantys[i]));
								rulePO.setVrLawStandard(vrLawStandards[i]);
								long vrid = Utility.getLong(SequenceManager.getSequence(""));
								rulePO.setVrId(vrid);
								rulePO.setVrCode("SBYJ"+vrid);
								rulePO.setVrType(94021002);
								dao.insert(rulePO);
							}
							
						}else
						{
							TtAsWrVinRulePO rulePO = new TtAsWrVinRulePO();
							rulePO.setPartWrType(Integer.parseInt(partWrTypes[i]));
							rulePO.setVrLevel(Integer.parseInt(vrLevels[i]));
							rulePO.setVrPartCode(vrPartCodes[i]);
							if(dao.select(rulePO).size() >0 )
							{
								rulePO = (TtAsWrVinRulePO)dao.select(rulePO).get(0);
								TtAsWrVinRulePO rulePO1 = new TtAsWrVinRulePO();
								rulePO1.setVrId(rulePO.getVrId());
								TtAsWrVinRulePO rulePO2 = new TtAsWrVinRulePO();
								rulePO2.setVrWarranty(Integer.parseInt(vrWarrantys[i]));
								rulePO2.setVrLawStandard(vrLawStandards[i]);
								rulePO2.setVrLaw(Integer.parseInt(vrLaws[i]));
								dao.update(rulePO1, rulePO2);
							}else
							{
								rulePO.setVrLaw(Integer.parseInt(vrLaws[i]));
								rulePO.setVrWarranty(Integer.parseInt(vrWarrantys[i]));
								rulePO.setVrLawStandard(vrLawStandards[i]);
								long vrid = Utility.getLong(SequenceManager.getSequence(""));
								rulePO.setVrId(vrid);
								rulePO.setVrCode("SBYJ"+vrid);
								rulePO.setVrType(94021002);
								dao.insert(rulePO);
							}
						}
					}
				}
				act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时单价导入到数据库中");
			logger.error(user,e1);
			act.setException(e1);
		}
}
	
	/*
	 * 索赔工时单价设定新增页面初始化
	 */
	public void labourPriceAddInit(){
		act = ActionContext.getContext() ;
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			List<TtAsWrLabourPricePO> lists = dao.getExistLaborList(user.getCompanyId()) ;
			act.setOutData("list", lists);
			act.setForword(ADD_URL);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定->新增");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	/*
	 * 车型大类选择弹出框初始化
	 */
	public void laborListInit(){
		act = ActionContext.getContext() ;
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(LABOR_LIST_URL);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定->工时大类查询");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	
	/*
	 * 车型大类选择弹出框初始化
	 */
	public void laborListInit1(){
		act = ActionContext.getContext() ;
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(LABOR_LIST_URL_1);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定->工时大类查询");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	
	public void laborListInit2(){
		act = ActionContext.getContext() ;
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(LABOR_LIST_URL_2);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定->工时大类查询");
			logger.error(user, be);
			act.setException(be);
		}
	}

	/*
	 * 车型大类选择弹出框查询主方法
	 */
	public void laborListQuery(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ;
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			act.getResponse().setContentType("application/json");
			String code = req.getParamValue("wrgroup_code");
			StringBuffer con = new StringBuffer() ;
			if(StringUtil.notNull(code))
				con.append("and upper(t.GROUP_CODE) like '%").append(code.toUpperCase()).append("%'\n") ;
			int pageSize = 30 ;
			int curPage = req.getParamValue("curPage") != null ? Integer.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<TmVhclMaterialGroupPO> ps = dao.getLaborList(con.toString(),pageSize, curPage) ;
			act.setOutData("ps", ps);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定->查询");
			logger.error(user, be);
			act.setException(be);
		}
	}

	/*
	 * 索赔工时单价设定新增操作
	 */
	@SuppressWarnings("unchecked")
	public void laborPriceAdd(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ;
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			String dealerId = req.getParamValue("dealer_id");
			String[] laborCodes = req.getParamValues("labor_code");
			String[] prices = req.getParamValues("price");
			
			String[] dealerIds = dealerId.split(",");
			// 向TT_AS_WR_LABOUR_PRICE表中添加数据
			TtAsWrLabourPricePO po = null ;
			for(int i = 0 ; i<dealerIds.length; i++){
				for(int j = 0;j<laborCodes.length;j++){
					if(StringUtil.notNull(prices[j])){
						po = new TtAsWrLabourPricePO() ;
						po.setDealerId(Long.parseLong(dealerIds[i]));
						po.setSeriesCode(laborCodes[j]);
						po = dao.existLaborPrice(po);
						// 此经销商对应的工时大类不存在则执行添加操作
						if(po==null){
							StringBuffer sql= new StringBuffer();

							sql.append("SELECT a.GROUP_CODE\n") ;
							sql.append("  from TM_VHCL_MATERIAL_GROUP a,\n") ;
							sql.append("       TT_AS_WR_MODEL_GROUP   b,\n") ;
							sql.append("       tt_as_wr_model_item    c\n") ;
							sql.append(" where a.group_id = c.model_id\n") ;
							sql.append("   and b.wrgroup_id = c.wrgroup_id\n") ;
							sql.append("   and a.group_id in (SELECT t.PACKAGE_ID\n") ;
							sql.append("                        from vw_material_group_service t\n") ;
							sql.append("                       where t.SERIES_CODE = '"+laborCodes[j]+"')\n") ;

							/*sql.append("\n" );
							sql.append("SELECT a.GROUP_CODE from  TM_VHCL_MATERIAL_GROUP a  ,TT_AS_WR_MODEL_GROUP b where\n" );
							sql.append("a.PARENT_GROUP_ID =\n" );
							sql.append("(  SELECT t.GROUP_ID from TM_VHCL_MATERIAL_GROUP t where t.GROUP_CODE = '"+laborCodes[j]+"')\n" );
							sql.append("and a.GROUP_CODE = b.WRGROUP_CODE");*/
							List<TmVhclMaterialGroupPO>  list= dao.select(TmVhclMaterialGroupPO.class, sql.toString(), null);
							if(list.size()> 0 )
							{
								for(TmVhclMaterialGroupPO groupPO  : list)
								{
									po = new TtAsWrLabourPricePO() ;
									po.setDealerId(Long.parseLong(dealerIds[i]));
									po.setSeriesCode(laborCodes[j]);
									po.setModeType(groupPO.getGroupCode());
									po.setOemCompanyId(user.getCompanyId());
									po.setCreateBy(user.getUserId());
									po.setCreateDate(new Date());
									po.setId(Utility.getLong(SequenceManager.getSequence("")));
									po.setLabourPrice(Double.parseDouble(prices[j]));
									dao.insert(po);
								}
								
							}

							
							
						}else{
							// 此经销商对应的工时大类存在则执行修改操作
							po = new TtAsWrLabourPricePO();
							po.setUpdateBy(user.getUserId());
							po.setUpdateDate(new Date());
							po.setLabourPrice(Double.parseDouble(prices[j]));
							TtAsWrLabourPricePO p1 = new TtAsWrLabourPricePO() ;
							p1.setDealerId(Long.parseLong(dealerIds[i]));
							p1.setSeriesCode(laborCodes[j]);
							dao.update(p1, po);
						}
					}
				}
			}
			
			List ins = new LinkedList<Object>();
			ins.add(0,dealerId);
			dao.callProcedure("P_LABOUR_PRICE_INFO", ins, null) ;
			act.setOutData("success", "保存成功!");
			this.labourPriceInit();
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔基本参数设定->新增");
			logger.error(user, be);
			act.setException(be);
		}
	}

	/*
	 * 索赔工时单价设定主页面第一次主查询
	 */
	public void laborPriceMainQuery(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ;
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			String dealer = "'";
			String dealer_name = req.getParamValue("dealer_name");
			String dealer_code = req.getParamValue("dealer_code");
			String dealer_id = req.getParamValue("dealer_id");
			/*
			 * 此处添加了页面手输经销商代码无法讲dealer_id传过来，而使用code进行拼接sql
			 */
			if(dealer_code != null && dealer_id==null){
				String[] str = dealer_code.split(",");
				for(int i=0;i<str.length;i++){
					dealer += str[i]+"','";
				}
				dealer  = dealer.substring(0, dealer.length()-2);
			}
			List<TtAsWrLabourPricePO> lists = dao.getExistLaborList01(user.getCompanyId()) ;
			StringBuffer con = new StringBuffer("\n");
			con.append("select b.dealer_id,b.dealer_code,b.dealer_shortname dealer_name ");
			for(int i=0;i<lists.size();i++){
				con.append(",max(decode(SERIES_CODE,'"+lists.get(i).getSeriesCode()+"',labour_price,'')) \""+StringUtil.fmtSpecialStr(lists.get(i).getSeriesCode())+"\"\n");
			}
			con.append("from tt_as_wr_labour_price a,tm_dealer b\n");
			con.append("where a.dealer_id = b.dealer_id and b.status = 10011001 \n"); //yh 2011.2.12
			if(StringUtil.notNull(dealer_name))
				con.append("and b.dealer_name like '%").append(dealer_name).append("%'\n");
			if(StringUtil.notNull(dealer_id)){
				con.append("and b.dealer_id in (").append(dealer_id).append(")\n");
			}
			if(StringUtil.notNull(dealer)&&!"'".equalsIgnoreCase(dealer)&& StringUtil.isNull(dealer_id)){
				con.append("and b.dealer_code in (").append(dealer).append(")\n");
			}
			con.append("group by (b.dealer_id,b.dealer_code,b.dealer_shortname)\n");
			con.append("order by b.dealer_code\n");
			
			int pageSize = 15 ;
			int curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")): 1; // 处理当前页
			PageResult<Map<String,Object>> ps = dao.mainQuery(con.toString(), pageSize, curPage) ;
			
			act.setOutData("ps", ps);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
/**
 * 工时单价设定删除.按经销商删除
 * @author KFQ
 * @serialData 2013-4-2 10:10
 */
	public void labourPriceDelete(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrLabourPricePO pp = new TtAsWrLabourPricePO();
		try {
			RequestWrapper request = act.getRequest();
			String id=request.getParamValue("did");//待修改的主键ID
			pp.setDealerId(Long.valueOf(id));
			dao.delete(pp);
	
			List ins = new LinkedList<Object>();
			ins.add(0,id);
			dao.callProcedure("P_LABOUR_PRICE_INFO", ins, null) ;
			
			act.setOutData("success", "true");

		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"索赔工时单价维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}	
	
	/*
	 * 索赔工时单价设定修改页面初始化
	 */
	public void labourPriceUpdateInit(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ;
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			String dealerId = req.getParamValue("did");
			
			//经销商信息
			TmDealerPO dpo = new TmDealerPO();
			dpo.setDealerId(Long.parseLong(dealerId));
			List<TmDealerPO> dpoList = dao.select(dpo);
			if(dpoList.size()>0)
				act.setOutData("dealer", dpoList.get(0));
			
			StringBuffer sql= new StringBuffer();
			sql.append("select t.SERIES_CODE,min(t.LABOUR_PRICE) LABOUR_PRICE from tt_as_wr_labour_price t  where t.DEALER_ID ="+dealerId+"  group by t.SERIES_CODE");

			//经销商对应的车型大类单价信息
			
			List<TtAsWrLabourPricePO> lists = dao.select(TtAsWrLabourPricePO.class, sql.toString(), null);
			act.setOutData("lists", lists);
			
			act.setForword(UPDATE_URL);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定->修改");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	/*
	 * 根据经销商与车型大类删除相应的数据
	 */
	public void laborPriceDel(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ;
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			act.getResponse().setContentType("application/json");
			String dealerId = req.getParamValue("did");
			String modeType = req.getParamValue("modeType");
			String idx = req.getParamValue("idx");
			
			TtAsWrLabourPricePO po = new TtAsWrLabourPricePO();
			po.setDealerId(Long.parseLong(dealerId));
			po.setSeriesCode(modeType);
			dao.delete(po);
			
			List ins = new LinkedList<Object>();
			ins.add(0,dealerId);
			dao.callProcedure("P_LABOUR_PRICE_INFO", ins, null) ;
			
			act.setOutData("idx", idx);
			act.setOutData("flag", true);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE, "索赔基本参数设定");
			logger.error(user, be);
			act.setException(be);
			act.setOutData("flag", false);
		}
	}
	
	
	/********************** 微车 **********************/
	
	/*
	 * 主页面初始化
	 */
	public void wcLaborPriceUrl(){
		act = ActionContext.getContext() ;
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			List<AreaProvinceBean> list = dao.getList() ;
			act.setOutData("list",list);
			act.setForword(WC_MAIN_URL);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	/*
	 * 主页面主查询
	 */
	public void wcLaborPriceMainQuery(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ;
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			String code = req.getParamValue("groupCode");
			String name = req.getParamValue("groupName");
			StringBuffer con = new StringBuffer() ;
			if(StringUtil.notNull(code))
				con.append(" and lower(a.wrgroup_code) like '%").append(code.toLowerCase()).append("%'\n");
			if(StringUtil.notNull(name))
				con.append(" and a.wrgroup_name like '%").append(name).append("%'\n");
			
			int pageSize = 30 ;
			int curPage = req.getParamValue("curPage") != null ? 
					Integer.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String,Object>> ps = dao.wcMainQuery(con.toString(), pageSize, curPage) ;
			act.setOutData("ps",ps);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	/*
	 * 修改页面初始化
	 */
	public void wcLabourPriceUpdateInit(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ; 
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			String modeType = req.getParamValue("modeType");
			String con = " and a.wrgroup_code = '"+modeType+"'\n" ;
			Map<String,Object> map = dao.wcMainQuery(con,1,1).getRecords().get(0);
			List<AreaProvinceBean> list = dao.getList() ;
			act.setOutData("map",map);
			act.setOutData("list",list);
			act.setForword(WC_MODIFY_URL);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	/*
	 * 修改操作
	 */
	public void wcLabourPriceUpdate(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ; 
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			String modeType = req.getParamValue("modeType");
			String con = " and a.wrgroup_code = '"+modeType+"'\n" ;
			Map<String,Object> map = dao.wcMainQuery(con,1,1).getRecords().get(0);
			
			List<AreaProvinceBean> list = dao.getList() ;
			
			String[] js = req.getParamValues("js");
			
			for(int i=0;i<js.length;i++){
				if(!new BigDecimal(js[i]).equals(map.get(list.get(i).getOrgCode()))){
					TtAsWrLabourPricePO po = new TtAsWrLabourPricePO();
					po.setModeType(modeType);
					po.setAreaLevel(list.get(i).getAreaLevel());
					List<TtAsWrLabourPricePO> list2 = dao.select(po);
					TtAsWrLabourPricePO po2 = null ;
					for(int j=0;j<list2.size();j++){
						po2 = list2.get(j) ;
						double d1 = Double.parseDouble(js[i]) ; //现在的单价
						double d2 = po2.getBasicLabourPrice()-d1 ; //差价
						po2.setBasicLabourPrice(d1);  //修改基础价
						po2.setLabourPrice(po2.getLabourPrice()-d2) ; //修改最终价
						po2.setUpdateBy(user.getUserId());
						po2.setUpdateDate(new Date());
						po = new TtAsWrLabourPricePO() ;
						po.setId(po2.getId());
						dao.update(po,po2);
					}
				}
			}
				
			act.setOutData("flag",true);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "索赔基本参数设定");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	public void adjustLaborHoursetpre()
	{
		act = ActionContext.getContext() ;		
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			List<PriceAdjustBean>	lvList=dao.getAdjustPrice(String.valueOf(Constant.ADJUST_MODE_TYPE_01));
			List<PriceAdjustBean>	clsList=dao.getAdjustPrice(String.valueOf(Constant.ADJUST_MODE_TYPE_02));			
			act.setOutData("clsList", clsList);
			act.setOutData("lvList", lvList);				
			act.setForword(ADJST_URL);			
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "索赔基本参数设定");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	public void getAdjustById()
	{
		act = ActionContext.getContext() ;
		req = act.getRequest() ; 
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{				
			String id=req.getParamValue("id");
			PriceAdjustBean adjust=dao.getAdjustById(id);
			act.setOutData("adjust", adjust);	
			act.setOutData("lv", Constant.ADJUST_MODE_TYPE_01);
			act.setOutData("cls", Constant.ADJUST_MODE_TYPE_02);
			act.setForword(ADJST_MODIFY_URL);			
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "索赔基本参数设定");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	public void adjustUpdate()
	{
		act = ActionContext.getContext() ;
		req = act.getRequest() ; 
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{				
			String id=req.getParamValue("id");
			String adjustPrice=req.getParamValue("adjustPrice");
			dao.adjustUpdate(id,adjustPrice,user.getUserId());
			adjustLaborHoursetpre();
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "索赔基本参数设定");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	public void updatePrice()
	{
		act = ActionContext.getContext() ;	
		act.getResponse().setContentType("application/json");
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{	
			List<TtAsWrLabourPricePO> list=dao.getLabourPrice();
			if(list!=null)
			{
				if(list.size()>0)
				{
					for(int i=0;i<list.size();i++)
					{
						dao.updatePrice(list.get(i),user.getUserId());
					}
				}
			}
			act.setOutData("flag", true);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "索赔基本参数设定");
			logger.error(user, be);
			act.setException(be);
		}
	}	
	//未维护基础数据的经销商查询
	public void noBaseDataPer()
	{
		act = ActionContext.getContext() ;
		req = act.getRequest() ; 
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{				
			act.setForword(noBaseDataPer);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "索赔基本参数设定");
			logger.error(user, be);
			act.setException(be);
		}
	}
	public void noBaseDataQuery()
	{
		act = ActionContext.getContext() ;
		req = act.getRequest() ; 
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{				
			act.getResponse().setContentType("application/json");
			String dealerCode = req.getParamValue("dealer_code");
			String dealerName = req.getParamValue("dealer_name");
			String dealerType = req.getParamValue("dealerType");
			int curPage = req.getParamValue("curPage") != null ? Integer.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String,Object>> ps = dao.getDealerInfo(dealerCode,dealerName,dealerType,Constant.PAGE_SIZE, curPage) ;
			act.setOutData("ps", ps);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "未维护经销商基础数据");
			logger.error(user, be);
			act.setException(be);
		}
	}
	//经销商信封打印
	public void mailPrint()
	{
		act = ActionContext.getContext() ;
		req = act.getRequest() ; 
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{				
			act.setForword(mailPrintPer);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "经销商信封打印");
			logger.error(user, be);
			act.setException(be);
		}
	}
	//查询所有售后经销商
	public void mailPrintPerQuery()
	{
		act = ActionContext.getContext() ;
		req = act.getRequest() ; 
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{				
			act.getResponse().setContentType("application/json");
			String dealerCode = req.getParamValue("dealer_code");
			String dealerName = req.getParamValue("dealer_name");
			int curPage = req.getParamValue("curPage") != null ? Integer.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String,Object>> ps = dao.getDealerInfo2(dealerCode,dealerName,Constant.PAGE_SIZE, curPage) ;
			act.setOutData("ps", ps);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "未维护经销商基础数据");
			logger.error(user, be);
			act.setException(be);
		}
	}
	//打印
	@SuppressWarnings("unchecked")
	public void mailPrintDo()
	{
		act = ActionContext.getContext() ;
		req = act.getRequest() ; 
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{				
			act.getResponse().setContentType("application/json");
			String id  = req.getParamValue("id");
			String dept = new String(req.getParamValue("dept").getBytes("ISO-8859-1"),"UTF-8");
			String remark =new String(req.getParamValue("remark").getBytes("ISO-8859-1"),"UTF-8");
			String type = req.getParamValue("type");
			char  a =' ';
			char b=' ';
			char c=' ';
			char g=' ';
			char e=' ';
			char f=' ';
			TmDealerPO d  = new TmDealerPO();
			d.setDealerId(Long.valueOf(id));
			d = (TmDealerPO) dao.select(d).get(0);
			act.setOutData("dept", dept);
			act.setOutData("remark", remark);
			//新增打印记录明细
			TtAsMailPrintDetailPO dp = new TtAsMailPrintDetailPO();
			dp.setCreateBy(user.getUserId());
			dp.setCreateDate(new Date());
			dp.setId(Utility.getLong(SequenceManager.getSequence("")));
			dp.setPrintBy(user.getUserId());
			dp.setPrintDate(new Date());
			dp.setPrintInDept(dept);
			dp.setPrintRemark(remark);
			dp.setPrintToDealer(Long.valueOf(id));
				String zip = d.getZipCode();
				if(zip!=null && !"".equalsIgnoreCase(zip)){
					a=zip.charAt(0);
					b=zip.charAt(1);
					c=zip.charAt(2);
					g=zip.charAt(3);
					e=zip.charAt(4);
					f=zip.charAt(5);
				}
				act.setOutData("a", a);//服务站邮编
				act.setOutData("b", b);
				act.setOutData("c", c);
				act.setOutData("g", g);
				act.setOutData("e", e);
				act.setOutData("f", f);
				act.setOutData("bean", d);
			if("1".equalsIgnoreCase(type)){
				dp.setPrintType("信封打印");
				dao.insert(dp);
				act.setForword(mailPrint);
			}else if("2".equalsIgnoreCase(type)){
				TcUserPO p= new TcUserPO();
				p.setUserId(user.getUserId());
				p = (TcUserPO) dao.select(p).get(0);
				dp.setPrintType("EMS打印");
				dao.insert(dp);
				act.setOutData("user", p);
				act.setOutData("bean", d);
				act.setForword(mailPrint2);
			}
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "信封EMS打印");
			logger.error(user, be);
			act.setException(be);
		}
	}
	//信封打印记录查询
	public void mailPrintDetail(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ; 
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{				
			act.setForword(mailPrintPer2);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "经销商信封打印记录");
			logger.error(user, be);
			act.setException(be);
		}
	}
	//经销商信封打印记录查询
	public void mailPrintDetailQuery()
	{
		act = ActionContext.getContext() ;
		req = act.getRequest() ; 
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{				
			act.getResponse().setContentType("application/json");
			String dealerCode = req.getParamValue("dealer_code");
			String dealerName = req.getParamValue("dealer_name");
			String bDate = req.getParamValue("bDate");
			String eDate = req.getParamValue("eDate");
			int curPage = req.getParamValue("curPage") != null ? Integer.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String,Object>> ps = dao.getMailPrintDetail(dealerCode,dealerName,bDate,eDate,Constant.PAGE_SIZE, curPage) ;
			act.setOutData("ps", ps);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "未维护经销商基础数据");
			logger.error(user, be);
			act.setException(be);
		}
	}
	//单据里程变更明细
	public void mileageModify(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ; 
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{				
			act.setForword(mailModPer);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "单据里程变更");
			logger.error(user, be);
			act.setException(be);
		}
	}
	//单据里程变更新增跳转
	public void modAdd(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ; 
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{				
			act.setForword(mailModAddPer);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "单据里程变更");
			logger.error(user, be);
			act.setException(be);
		}
	}
	//里程修改,通过工单号获取相关信息
	public void getDetail(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ; 
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{	
			String roNo = req.getParamValue("roNo");//工单号
			Map<String, Object> map =dao.getRoDetail(roNo);
			act.setOutData("roInfo", map);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "单据里程变更");
			logger.error(user, be);
			act.setException(be);
		}
	}
	// 执行更新数据,单据数据修改
	public void update(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ; 
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{	
			String roNo = req.getParamValue("roNo");//工单号
			String before = req.getParamValue("mod_before");
			String dealerId = req.getParamValue("dealerId");
			String after = req.getParamValue("mod_after");
			String sys = req.getParamValue("mod_system");
			String vin = req.getParamValue("vin");
			//插入记录
			TtAsMileageChangePO p = new TtAsMileageChangePO();
			p.setId(Utility.getLong(SequenceManager.getSequence("")));
			p.setModAfter(Integer.parseInt(after));
			p.setModBefore(Integer.parseInt(before));
			p.setModBy(user.getUserId());
			p.setModDate(new Date());
			p.setModSystem(Integer.parseInt(sys));
			p.setDealerId(Long.valueOf(dealerId));
			p.setRoNo(roNo);
			p.setVin(vin);
			dao.insert(p);
			//更新车辆表信息
			TmVehiclePO v  = new TmVehiclePO();
			TmVehiclePO v2  = new TmVehiclePO();
			v.setVin(vin);
			v2.setMileage(Double.valueOf(after));
			dao.update(v, v2);
			//更新工单表
			TtAsRepairOrderPO o = new TtAsRepairOrderPO();
			TtAsRepairOrderPO o2 = new TtAsRepairOrderPO();
			o.setRoNo(roNo);
			o2.setInMileage(Double.valueOf(after));
			dao.update(o, o2);
			//更新索赔单表
			TtAsWrApplicationPO a = new TtAsWrApplicationPO();
			TtAsWrApplicationPO a2 = new TtAsWrApplicationPO();
			a.setRoNo(roNo);
			a2.setInMileage(Double.valueOf(after));
			dao.update(a, a2);
			act.setOutData("ok", "ok");
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "单据里程变更");
			logger.error(user, be);
			act.setException(be);
		}
	}
	//工单里程修改记录
	public void modQuery()
	{
		act = ActionContext.getContext() ;
		req = act.getRequest() ; 
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{				
			act.getResponse().setContentType("application/json");
			String roNo = req.getParamValue("roNo");
			String vin = req.getParamValue("vin");
			int curPage = req.getParamValue("curPage") != null ? Integer.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String,Object>> ps = dao.getModDetail(roNo,vin,Constant.PAGE_SIZE, curPage) ;
			act.setOutData("ps", ps);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "未维护经销商基础数据");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	//二次索赔车型维护
	public void modelPricePer(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ; 
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{				
			act.setForword(modelPricePer);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "单据里程变更");
			logger.error(user, be);
			act.setException(be);
		}
	}
	public void modelPriceQuery()
	{
		act = ActionContext.getContext() ;
		req = act.getRequest() ; 
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{				
			act.getResponse().setContentType("application/json");
			String modelCode = req.getParamValue("modelCode");
			String modelName = req.getParamValue("modelName");
			String status = req.getParamValue("status");
			int curPage = req.getParamValue("curPage") != null ? Integer.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String,Object>> ps = dao.getModelList(modelCode,modelName,status,Constant.PAGE_SIZE, curPage) ;
			act.setOutData("ps", ps);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "未维护经销商基础数据");
			logger.error(user, be);
			act.setException(be);
		}
	}
	//
	public void modelPriceAdd(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ; 
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			String type = req.getParamValue("type");
			String id = req.getParamValue("id");
			if(Utility.testString(id)){
				TtAsWrModelPricePO pp = new TtAsWrModelPricePO();
				pp.setId(Long.valueOf(id));
				pp = (TtAsWrModelPricePO) dao.select(pp).get(0);
				act.setOutData("bean", pp);
			}else{
				act.setOutData("bean", null);
			}
			act.setOutData("type", type);
			act.setOutData("id", id);
			act.setForword(modelPriceAdd);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "单据里程变更");
			logger.error(user, be);
			act.setException(be);
		}
	}
	@SuppressWarnings("unchecked")
	public void modelPriceSave(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ; 
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{	
			String modelCode = req.getParamValue("modelCode");//工单号
			String modelName = req.getParamValue("modelName");
			String modelPrice = req.getParamValue("modelPrice");
			String status = req.getParamValue("status");
			String type = req.getParamValue("type");
			String id = req.getParamValue("id");
			String msg = "";
			if("0".equalsIgnoreCase(type)){
				TtAsWrModelPricePO pp = new TtAsWrModelPricePO();
				pp.setModelCode(modelCode);
				List<TtAsWrModelPricePO> list = dao.select(pp);
				if(list.size()>0){
					msg="车型代码已经存在,请修改!";
				}
			}
			if(!Utility.testString(msg)){
				TtAsWrModelPricePO pp = new TtAsWrModelPricePO();
				TtAsWrModelPricePO pp2 = new TtAsWrModelPricePO();
				pp2.setModelCode(modelCode.toUpperCase());
				pp2.setModelName(modelName);
				pp2.setModelPrice(Float.valueOf(modelPrice));
				pp2.setStatus(Integer.parseInt(status));
				if("0".equalsIgnoreCase(type)){
					pp2.setId(Utility.getLong(SequenceManager.getSequence("")));
					pp2.setCreateBy(user.getUserId());
					pp2.setCreateDate(new Date());
					dao.insert(pp2);
				}else{
					pp.setId(Long.valueOf(id));
					pp2.setUpdateBy(user.getUserId());
					pp2.setUpdateDate(new Date());
					dao.update(pp, pp2);
				}
			}
			
			act.setOutData("ok", msg);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "单据里程变更");
			logger.error(user, be);
			act.setException(be);
		}
	}
	//旧件库区库位维护
	public void oldPartReserInit(){
		act = ActionContext.getContext();
		req = act.getRequest();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
		act.setForword(PART_RESER_URL);
		} catch (Exception e) {
			BizException be = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "旧件库区库位维护");
			logger.error(user, be);
			act.setException(be);
		}
	}
}
