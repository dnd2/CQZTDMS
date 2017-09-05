package com.infodms.dms.actions.crm.oemleadsmanage;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.oemleadsmanage.OemLeadsManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TPcLeadsAllotPO;
import com.infodms.dms.po.TPcLeadsPO;
import com.infodms.dms.po.TPcLeadsTempPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @author chenh
 *
 */
public class OemLeadsManage extends BaseImport {

	public Logger logger = Logger.getLogger(OemLeadsManage.class);
	// 线索导入初始页面
	private final String oemLeadsManageImplortUrl = "/jsp/crm/oemleadsmanage/oemleadsmanageimport.jsp";
	// 线索导入成功页面
	private final String oemLeadsManageSuccessUrl = "/jsp/crm/oemleadsmanage/oemleadsmanagesuccess.jsp";
	// 线索导入失败页面
	private final String oemLeadsManageFailureUrl = "/jsp/crm/oemleadsmanage/oemleadsmanagefailure.jsp";
	// 线索导入完成页面
	private final String oemLeadsManageCompleteUrl = "/jsp/crm/oemleadsmanage/oemleadsmanageimportcomplete.jsp";
	// 线索分派初始页面
	private final String oemLeadsAllotUrl = "/jsp/crm/oemleadsmanage/oemleadsallotinit.jsp";
	// 线索分派选择经销商页面
	private final String oemLeadsAllotSelectDealerUrl = "/jsp/crm/oemleadsmanage/oemleadsallotselectdealer.jsp";
	// 线索查询初始页面
	private final String oemLeadsFindUrl = "/jsp/crm/oemleadsmanage/oemleadsfindinit.jsp";
	// 线索重新分派选择经销商页面
	private final String oemLeadsReAllotSelectDealerUrl = "/jsp/crm/oemleadsmanage/oemleadsreallotselectdealer.jsp";
	//重复导入提醒界面
	private final String oemLeadsManageErrorUrl = "/jsp/crm/oemleadsmanage/oemleadsmanageError.jsp";

	/**
	 * 初始使化导入页面
	 */
	public void leadsImport() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(oemLeadsManageImplortUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 销售线索导入临时表
	 */
	public void leadsManagePlanExcelOperate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		OemLeadsManageDao dao=OemLeadsManageDao.getInstance();
		
        try {
			RequestWrapper request = act.getRequest();
//			String year=CommonUtils.checkNull(request.getParamValue("year"));
//			String month=CommonUtils.checkNull(request.getParamValue("month"));
//			String areaId=CommonUtils.checkNull(request.getParamValue("buss_area"));
			TPcLeadsTempPO po=new TPcLeadsTempPO();
			po.setCreateBy(logonUser.getUserId().toString());
			List<Map<String, Object>> usercount=dao.getDaoUser(logonUser.getUserId().toString());
		   if(usercount.size()>0 ) {
		        act.setOutData("errorMsgUser", "该用户正在导入......请等待!");
		        act.setForword(oemLeadsManageErrorUrl);
		   }else{
			//清空临时表中的数据
			dao.delete(po);
			long maxSize=1024*1024*5;
			insertIntoTmp(request, "uploadFile",9,3,maxSize);
			List<ExcelErrors> el=getErrList();
			if(null!=el&&el.size()>0){
				act.setOutData("errorList", el);
				act.setForword(oemLeadsManageFailureUrl);
			}else{
				List<Map> list=getMapList();
				//将数据插入临时表
				insertTmpLeadsManage(list, logonUser.getUserId());
				//校验临时表数据
				List<ExcelErrors> errorList=checkData();
//				List errorList = null;
				if(null!=errorList){
					act.setOutData("errorList", errorList);
					act.setForword(oemLeadsManageFailureUrl);
				}else{
					//获取临时表数据，并显示到界面
					List<Map<String, Object>> tmpList=dao.oemSelectTmpLeadsManage(logonUser.getUserId());
					act.setOutData("leadsList", tmpList);
					act.setForword(oemLeadsManageSuccessUrl);
				}
			}
		}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}	
	/*
	 * 把所有导入记录插入临时表
	 */
	private void insertTmpLeadsManage(List<Map> list,Long userId) throws Exception{
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
				parseCells(key, cells, userId);
			}
		}
		
	}
	/*
	 * 每一行插入TPcLeadsTempPO（长安汽车）
	 */
	private void parseCells(String rowNum,Cell[] cells,Long userId) throws Exception{
			OemLeadsManageDao dao=new OemLeadsManageDao();
		    TPcLeadsTempPO po=new TPcLeadsTempPO();
			po.setRowNumber(rowNum.trim());
//			po.setOrgCode(subCell(cells[0].getContents().trim()));
			if(subCell(cells[0].getContents().trim())==null || "".equals(subCell(cells[0].getContents().trim()))) {
				po.setCustomerName("null");
			} else {
				po.setCustomerName(subCell(cells[0].getContents().trim()));
			}
			if(subCell(cells[1].getContents().trim())==null || "".equals(subCell(cells[1].getContents().trim()))) {
				po.setTelephone("null");
			} else {
				po.setTelephone(subCell(cells[1].getContents().trim()));
			}
			po.setProvince(subCell(cells[2].getContents().trim()));
			po.setCity(subCell(cells[3].getContents().trim()));
			po.setArea(subCell(cells[4].getContents().trim()));
//			// 线索来源 (60151000:错误数据;60151001:来电;60151002:来店;60151003:官网;60151004:两车平台;60151005:400)
//			int origin = 60151000;
//			if(subCell(cells[5].getContents().trim())=="来电" || "来电".equals(subCell(cells[5].getContents().trim()))) {
//				origin = 60151001;
//			} else if(subCell(cells[5].getContents().trim())=="来店" || "来店".equals(subCell(cells[5].getContents().trim()))) {
//				origin = 60151002;
//			} else if(subCell(cells[5].getContents().trim())=="官网" || "官网".equals(subCell(cells[5].getContents().trim()))) {
//				origin = 60151003;
//			} else if(subCell(cells[5].getContents().trim())=="两车平台" || "两车平台".equals(subCell(cells[5].getContents().trim()))) {
//				origin = 60151004;
//			} else if(subCell(cells[5].getContents().trim())=="400" || "400".equals(subCell(cells[5].getContents().trim()))) {
//				origin = 60151005;
//			} else {
//				origin = 60151000;
//			}
			if(subCell(cells[5].getContents().trim())==null || "".equals(subCell(cells[5].getContents().trim()))) {
				po.setLeadsOrigin("null");
			} else {
				po.setLeadsOrigin(subCell(cells[5].getContents().trim()));
			}
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Date date = sdf.parse(subCell(cells[6].getContents().trim()));
			if (cells[6].getType() == CellType.DATE) {
				//DateCell dc = (DateCell) cells[6];
                Date date = ((DateCell) cells[6]).getDate();
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String sDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                po.setCollectDate(subCell(sDate));
			} else {
				po.setCollectDate(subCell(cells[6].getContents().trim()));
			}
			
			po.setCreateDate(new Date());
			po.setDealerId(subCell(cells[7].getContents().trim()));
			if(subCell(cells[8].getContents().trim())==null || "".equals(subCell(cells[8].getContents().trim()))) {
				po.setIntentCar("null");
			} else {
				po.setIntentCar(subCell(cells[8].getContents().trim()));
			}
			// 线索类别为车厂录入
			po.setLeadsType(60141001);
			po.setCreateBy(userId.toString());
			dao.insert(po);
	}	
	/*
	 * 将输入字符截取最多30位
	 */
	private String subCell(String orgAmt){
		String newAmt="";
		if(null==orgAmt||"".equals(orgAmt)){
			return newAmt;
		}
		if(orgAmt.length()>30){
			newAmt=orgAmt.substring(0,30);
		}else{
			newAmt=orgAmt;
		}
		return newAmt;
	}
	/*
	 * 校验临时表中数据是否符合导入标准
	 */
	private List<ExcelErrors> checkData(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		OemLeadsManageDao dao=new OemLeadsManageDao();
		TPcLeadsTempPO tempPo=new TPcLeadsTempPO();
		tempPo.setCreateBy(logonUser.getUserId().toString());
		List<TPcLeadsTempPO> list=dao.select(tempPo);
		if(null==list){
			list=new ArrayList();
		}
		String series_name=null;
		
		List<Map<String, Object>> listmap=dao.getIntentCar();
		ExcelErrors errors=null;
		TPcLeadsTempPO po=null;
		StringBuffer errorInfo=new StringBuffer("");
		boolean isError=false;
		
		List<ExcelErrors> errorList = new ArrayList<ExcelErrors>();
		for(int i=0;i<list.size();i++){
			int count=0;
			errors=new ExcelErrors();
			//取得TPcLeadsTempPO
			po=list.get(i);
			//取得行号
			String rowNum=po.getRowNumber();
			
			//数据校验条件的MAP
			
			Map<String, Object> conMap=new HashMap<String, Object>();
			conMap.put("province", po.getProvince());
			conMap.put("city", po.getCity());
			conMap.put("area", po.getArea());
			conMap.put("dealerId", po.getDealerId());
			
			// 客户所在省
			List<Map<String, Object>> provinceDlrList = null;
			if(!"".equals(po.getProvince())&&po.getProvince()!=null) {
				provinceDlrList=dao.oemLeadsCheckProvince(conMap);
			}
			// 客户所在市
			List<Map<String, Object>> cityDlrList = null;
			if(!"".equals(po.getCity())&&po.getCity()!=null) {
				cityDlrList=dao.oemLeadsCheckCity(conMap);
			}
			// 客户所在区
			List<Map<String, Object>> areaDlrList = null;
			if(!"".equals(po.getArea())&&po.getArea()!=null) {
				areaDlrList=dao.oemLeadsCheckArea(conMap);
			}
			// 经销商编码
			List<Map<String, Object>> dealerList = null;
			if(!"".equals(po.getDealerId())&&po.getDealerId()!=null) {
				dealerList=dao.oemLeadsCheckDealer(conMap);
			}
			String collectDate = null;
			if(po.getCollectDate()==null) {
				collectDate = "2014-01-01";
			} else {
				collectDate = po.getCollectDate().toString();
			}
			//校验合计
			try {
				if(!isValidDate(collectDate)){
					isError=true;
					errorInfo.append("收集时间格式不正确,");
				} else if (po.getCustomerName().trim()=="null" || "null".equals(po.getCustomerName().trim())){
					isError=true;
					errorInfo.append("客户姓名不能为空,");
				} else if (po.getTelephone().trim()=="null" || "null".equals(po.getTelephone().trim())) {
					isError=true;
					errorInfo.append("联系电话不能为空,");
				} else if (!"天猫电商".equals(po.getLeadsOrigin().trim())&&!"客户中心".equals(po.getLeadsOrigin().trim())&&!"官网".equals(po.getLeadsOrigin().trim())&&!"网络媒体".equals(po.getLeadsOrigin().trim())&&!"车展".equals(po.getLeadsOrigin().trim())&&!"巡展".equals(po.getLeadsOrigin().trim())&&!"试乘试驾".equals(po.getLeadsOrigin().trim())&&!"上市活动".equals(po.getLeadsOrigin().trim())&&!"媒体".equals(po.getLeadsOrigin().trim())&&!"汽车之家".equals(po.getLeadsOrigin().trim())&&!"易车网".equals(po.getLeadsOrigin().trim())&&!"社会化媒体".equals(po.getLeadsOrigin().trim())) {
					isError=true;
					errorInfo.append("线索来源格式不正确,");
				}
				if((po.getProvince()!=null&&!"".equals(po.getProvince()))&&provinceDlrList.size()==0){
						isError=true;
						errorInfo.append("所在省代码不正确");
				}
				if((po.getCity()!=null&&!"".equals(po.getCity()))&&cityDlrList.size()==0){
						isError=true;
						errorInfo.append("所在市代码不正确");
				}
				if((po.getArea()!=null&&!"".equals(po.getArea()))&&areaDlrList.size()==0){
					isError=true;
					errorInfo.append("所在区代码错误");
				}
				if((po.getDealerId()!=null&&!"".equals(po.getDealerId()))&&dealerList.size()==0){
					isError=true;
					errorInfo.append("经销商代码错误");
				}
				
			
				
				for(int j=0;j<listmap.size();j++){
					series_name=listmap.get(j).get("SERIES_NAME").toString();
					if(po.getIntentCar()!=null&&!"".equals(po.getIntentCar())) {  
						if(series_name.equals(po.getIntentCar().trim())){
							count=1;
						}
					}
				}
				
				if(count==0){
					isError=true;
					errorInfo.append("意向车系格式不正确");
				}
						
				  
			} catch (Exception e) {
				e.printStackTrace();
				isError=true;
			}
			if(errorInfo.length()>0){
				String info=errorInfo.substring(0,errorInfo.length()-1);
				errors.setRowNum(new Integer(rowNum));
				errors.setErrorDesc(info);
				errorList.add(errors);
				errorInfo.delete(0, errorInfo.length());
			}
		}
		
		if(isError){
			return errorList;
		}else{
			return null;
		}
	}
	/*
	 * 导入业务表
	 */
	public void importExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			OemLeadsManageDao dao=OemLeadsManageDao.getInstance();
			//导入数据到销售线索表和线索分派表
			
			dao.insertLeads(logonUser.getUserId());
			
			act.setForword(oemLeadsManageCompleteUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 销售线索导入模板下载
	 */
	public void downloadTemple(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try{
			ResponseWrapper response = act.getResponse();
			
			// 用于下载传参的集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			
			//标题
			List<Object> listHead = new LinkedList<Object>();//导出模板第一列
			listHead.add("客户姓名");
			listHead.add("联系电话");
			listHead.add("客户所在省");
			listHead.add("客户所在市");
			listHead.add("客户所在区");
			listHead.add("线索来源");
			listHead.add("收集时间");
			listHead.add("经销商代码");
			listHead.add("意向车系");
			
			list.add(listHead);
			
			// 导出的文件名
			String fileName = "销售线索导入模板.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			os = response.getOutputStream();
//			CsvWriterUtil.writeCsv(list, os);
			CsvWriterUtil.createXlsFile(list, os);
			os.flush();			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	static int[] DAYS = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };  
	  
	/** 
	 * @param date yyyy-MM-dd HH:mm:ss 
	 * @return 
	 */  
	public static boolean isValidDate(String date) {  
	    try {
	    	//date 有可能是 yyyy/mm/dd 的格式，先把这种格式转换为 yyyy-mm-dd 的格式
	    	String newdate = date;
	    	if (newdate.indexOf("/") != -1) {
	    		newdate = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy/MM/dd").parse(date));
	    	}
	    	
	        int year = Integer.parseInt(newdate.substring(0, 4));  
	        if (year <= 0)  
	            return false;  
	        int month = Integer.parseInt(newdate.substring(5, 7));  
	        if (month <= 0 || month > 12)  
	            return false;  
	        int day = Integer.parseInt(newdate.substring(8, 10));  
	        if (day <= 0 || day > DAYS[month])  
	            return false;  
	        if (month == 2 && day == 29 && !isGregorianLeapYear(year)) {  
	            return false;  
	        }  
//	        int hour = Integer.parseInt(date.substring(11, 13));  
//	        if (hour < 0 || hour > 23)  
//	            return false;  
//	        int minute = Integer.parseInt(date.substring(14, 16));  
//	        if (minute < 0 || minute > 59)  
//	            return false;  
//	        int second = Integer.parseInt(date.substring(17, 19));  
//	        if (second < 0 || second > 59)  
//	            return false;  
	  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	        return false;  
	    }  
	    return true;  
	}  
	public static final boolean isGregorianLeapYear(int year) {  
	    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);  
	}  
	/**
	 * 初始线索分派页面
	 */
	public void leadsAllot() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(oemLeadsAllotUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * OEM初始线索分派查询
	 */
	public void leadsAllotQuery() {
		OemLeadsManageDao dao = new OemLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			//获取页面参数
			String customerName = CommonUtils.checkNull(request.getParamValue("customer_name"));
			String telephone = CommonUtils.checkNull(request.getParamValue("telephone"));
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			String leadsOrigin = CommonUtils.checkNull(request.getParamValue("leads_origin"));
//			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String dPro = CommonUtils.checkNull(request.getParamValue("dPro"));
			String dCity = CommonUtils.checkNull(request.getParamValue("dCity"));
			String dArea = CommonUtils.checkNull(request.getParamValue("dArea"));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.oemLeadsAllotQuery(customerName,
					telephone,startDate,endDate,leadsOrigin,dPro,dCity,dArea,
					curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "销售线索分派查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * OEM初始线索分派经销商选择页面
	 */
	public void oemLeadsAllotByOemInit() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String leadsCode = CommonUtils.checkNull(request.getParamValue("leadsCode"));
			String leadsGroup = CommonUtils.checkNull(request.getParamValue("leadsGroup"));
			act.setOutData("leadsCode", leadsCode);
			act.setOutData("leadsGroup", leadsGroup);
			act.setForword(oemLeadsAllotSelectDealerUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * OEM初始线索分派
	 */
	public void oemLeadsAllotByOem() {
		OemLeadsManageDao dao = new OemLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String leadsCode = CommonUtils.checkNull(request.getParamValue("leadsCode"));
			String leadsGroup = CommonUtils.checkNull(request.getParamValue("leadsGroup"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			
			//9：00-17:00之间分配给经销商的公司类信息，主管当日19:00前完成分配，顾问必须在当日完成处理
			//17:00-次日9:00分配给经销商的公司类信息，主管次日11:00前完成分配，顾问必须在次日12:00前完成处理。
			Date d1 = new Date(); //当前时间
			String d2 = "090000";
			String d3 = "170000";
			String remindDate = null;
			SimpleDateFormat f = new SimpleDateFormat("hhmmss"); //格式化为 hhmmss
			int d1Number = Integer.parseInt(f.format(d1).toString()); //将第一个时间格式化后转为int
			int d2Number = Integer.parseInt(d2); //将第二个时间格式化后转为int
			int d3Number = Integer.parseInt(d3); //将第三个时间格式化后转为int
			
			SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, 1);
								
			if(d1Number>=d2Number && d1Number<=d3Number){
				remindDate = f1.format(d1);
			} else {
				remindDate = f1.format(c.getTime());
			}
			
			//leadsGroup为空说明页面为单选线索进行分派
			if(leadsGroup==null || "".equals(leadsGroup)) {
				// 开始执行分派到选择的经销商
				// 获取销售线索信息
				TPcLeadsPO po = new TPcLeadsPO();
				po.setLeadsCode(Long.parseLong(leadsCode));
				TPcLeadsPO po2 = (TPcLeadsPO)dao.select(po).get(0);
				// 拆分经销商
				String [] dealerGroup= dealerCode.split(",");
				for(int i=0;i<dealerGroup.length;i++) {
					String dlrCode = dealerGroup[i];
					//取经销商ID
					TmDealerPO dealerPo = new TmDealerPO();
					dealerPo.setDealerCode(dlrCode);
					TmDealerPO dealerPo2 = (TmDealerPO)dao.select(dealerPo).get(0);
					//判断是否已建过档案（电话、姓名、经销商ID）
					List<Map<String, Object>> getHasList = null;
					getHasList=dao.getHasCustomer(po2.getTelephone(),po2.getCustomerName(),dealerPo2.getDealerId().toString());
					//已存在档案，自动分派到顾问，如果客户类型不属于战败、失效、保有将线索设为无效，并新增接触点信息
					if(getHasList.size()>0) {
						//获取已建档的分派顾问
						String adviser = getHasList.get(0).get("ADVISER").toString();
						String dealerId = getHasList.get(0).get("DEALER_ID").toString();
						String customerId = getHasList.get(0).get("CUSTOMER_ID").toString();
						String ctmType = getHasList.get(0).get("CTM_TYPE").toString();
						
						//修改销售线索主表状态为无效
//						TPcLeadsPO oldLeadsPo = new TPcLeadsPO();
//						oldLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
//						TPcLeadsPO newLeadsPo = new TPcLeadsPO();
//						newLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
//						if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
//							newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_04.longValue());
//						} else {
//							newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_01.longValue());
//						}
//						dao.update(oldLeadsPo, newLeadsPo);
						
						TPcLeadsAllotPO newPo = new TPcLeadsAllotPO();
						//线索分派主键ID
						Long allotCode = dao.getLongPK(newPo);
						newPo.setLeadsAllotId(allotCode);
						newPo.setLeadsCode(Long.parseLong(leadsCode));
						newPo.setCustomerName(po2.getCustomerName());
						newPo.setTelephone(po2.getTelephone());
						newPo.setRemark(null);
						newPo.setCreateDate(new Date());
						newPo.setCreateBy(logonUser.getUserId().toString());
						if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
							newPo.setStatus(Constant.STATUS_DISABLE);//设置为无效
						} else {
							newPo.setStatus(Constant.STATUS_ENABLE);//设置为有效
						}
						newPo.setDealerId(dealerPo2.getDealerId().toString());
						newPo.setAllotDealerDate(new Date());
						newPo.setAdviser(adviser);
						newPo.setAllotAdviserDate(new Date());
						newPo.setAllotAgain(Constant.IF_TYPE_NO);
						newPo.setOldLeadsCode(null);
						newPo.setIfConfirm(60321002);
						newPo.setConfirmDate(new Date());
						dao.insert(newPo);
						
						if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
							//增加接触点信息
							CommonUtils.addContackPoint(Constant.POINT_WAY_01, "重复线索", customerId, adviser, dealerId);
							CommonUtils.updateLeadStatus(leadsCode);
							String repeatLeads = SequenceManager.getSequence("");
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_20.toString(), repeatLeads, customerId, dealerId, adviser, f1.format(new Date()),"");
							//标记线索为重复线索
							CommonUtils.updateIfRepeat(allotCode.toString());
							CommonUtils.updateLeadStatus(leadsCode);
						} else {
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_03.toString(), allotCode.toString(), customerId, dealerId, adviser, f1.format(new Date()),"");
						}
						//新增提醒信息
						//CommonUtils.addRemindInfo(Constant.REMIND_TYPE_03.toString(), allotCode.toString(), "", dealerPo2.getDealerId().toString(), adviser, remindDate,"");
					} else {//未存在则新增分派，不自动分派到顾问
						TPcLeadsAllotPO newPo = new TPcLeadsAllotPO();
						//获取主键
						Long keyPk = dao.getLongPK(newPo);
						newPo.setLeadsAllotId(keyPk);
						newPo.setLeadsCode(Long.parseLong(leadsCode));
						newPo.setCustomerName(po2.getCustomerName());
						newPo.setTelephone(po2.getTelephone());
						newPo.setRemark(null);
						newPo.setCreateDate(new Date());
						newPo.setCreateBy(logonUser.getUserId().toString());
						newPo.setStatus(Constant.STATUS_ENABLE);
						newPo.setDealerId(dealerPo2.getDealerId().toString());
						newPo.setAllotDealerDate(new Date());
						newPo.setAdviser(null);
						newPo.setAllotAdviserDate(null);
						newPo.setAllotAgain(Constant.IF_TYPE_NO);
						newPo.setOldLeadsCode(null);
						dao.insert(newPo);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_01.toString(), keyPk.toString(), "", dealerPo2.getDealerId().toString(), "", remindDate,"");
					}
				}
				// 页面多选线索进行分派
			} else {
				// 拆分销售线索
				String[] leadsGroup2 = leadsGroup.split(",");
				for (int i = 0; i < leadsGroup2.length; i++) {
					String lgCode = leadsGroup2[i];
					// 获取销售线索信息
					TPcLeadsPO po = new TPcLeadsPO();
					po.setLeadsCode(Long.parseLong(lgCode));
					TPcLeadsPO po2 = (TPcLeadsPO)dao.select(po).get(0);
					// 拆分经销商
					String[] dealerGroup = dealerCode.split(",");
					for(int j=0;j<dealerGroup.length;j++) {
						String dlrCode = dealerGroup[j];
						//取经销商ID
						TmDealerPO dealerPo = new TmDealerPO();
						dealerPo.setDealerCode(dlrCode);
						TmDealerPO dealerPo2 = (TmDealerPO)dao.select(dealerPo).get(0);
						//判断是否已建过档案（电话、姓名、经销商ID）
						List<Map<String, Object>> getHasList = null;
						getHasList=dao.getHasCustomer(po2.getTelephone(),po2.getCustomerName(),dealerPo2.getDealerId().toString());
						//已存在档案，自动分派到顾问
						if(getHasList.size()>0) {
							//获取已建档的分派顾问
							String adviser = getHasList.get(0).get("ADVISER").toString();
							String dealerId = getHasList.get(0).get("DEALER_ID").toString();
							String customerId = getHasList.get(0).get("CUSTOMER_ID").toString();
							String ctmType = getHasList.get(0).get("CTM_TYPE").toString();
							
							//修改销售线索主表状态为无效
//							TPcLeadsPO oldLeadsPo = new TPcLeadsPO();
//							oldLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
//							TPcLeadsPO newLeadsPo = new TPcLeadsPO();
//							newLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
//							if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
//								newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_04.longValue());
//							} else {
//								newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_01.longValue());
//							}
//							dao.update(oldLeadsPo, newLeadsPo);
							
							TPcLeadsAllotPO newPo = new TPcLeadsAllotPO();
							//线索分派主键ID
							Long allotCode = dao.getLongPK(newPo);
							newPo.setLeadsAllotId(allotCode);
							newPo.setLeadsCode(Long.parseLong(lgCode));
							newPo.setCustomerName(po2.getCustomerName());
							newPo.setTelephone(po2.getTelephone());
							newPo.setRemark(null);
							newPo.setCreateDate(new Date());
							newPo.setCreateBy(logonUser.getUserId().toString());
							if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
								newPo.setStatus(Constant.STATUS_DISABLE);//设置为无效
							} else {
								newPo.setStatus(Constant.STATUS_ENABLE);//设置为有效
							}
							newPo.setDealerId(dealerPo2.getDealerId().toString());
							newPo.setAllotDealerDate(new Date());
							newPo.setAdviser(adviser);
							newPo.setAllotAdviserDate(new Date());
							newPo.setAllotAgain(Constant.IF_TYPE_NO);
							newPo.setOldLeadsCode(null);
							newPo.setIfConfirm(60321002);
							newPo.setConfirmDate(new Date());
							dao.insert(newPo);
							
							if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
								//增加接触点信息
								CommonUtils.addContackPoint(Constant.POINT_WAY_01, "重复线索", customerId, adviser, dealerId);
								CommonUtils.updateLeadStatus(leadsCode);
								String repeatLeads = SequenceManager.getSequence("");
								//新增提醒信息
								CommonUtils.addRemindInfo(Constant.REMIND_TYPE_20.toString(), repeatLeads, customerId, dealerId, adviser, f1.format(new Date()),"");
								//标记线索为重复线索
								CommonUtils.updateIfRepeat(allotCode.toString());
								CommonUtils.updateLeadStatus(leadsCode);
							} else {
								//新增提醒信息
								CommonUtils.addRemindInfo(Constant.REMIND_TYPE_03.toString(), allotCode.toString(), customerId, dealerId, adviser, f1.format(new Date()),"");
							}
							//新增提醒信息
							//CommonUtils.addRemindInfo(Constant.REMIND_TYPE_03.toString(), allotCode.toString(), "", dealerPo2.getDealerId().toString(), adviser, remindDate,"");
						} else {//未存在则新增分派，不自动分派到顾问
							TPcLeadsAllotPO newPo = new TPcLeadsAllotPO();
							//获取主键
							Long keyPk = dao.getLongPK(newPo);
							newPo.setLeadsAllotId(keyPk);
							newPo.setLeadsCode(Long.parseLong(lgCode));
							newPo.setCustomerName(po2.getCustomerName());
							newPo.setTelephone(po2.getTelephone());
							newPo.setRemark(null);
							newPo.setCreateDate(new Date());
							newPo.setCreateBy(logonUser.getUserId().toString());
							newPo.setStatus(Constant.STATUS_ENABLE);
							newPo.setDealerId(dealerPo2.getDealerId().toString());
							newPo.setAllotDealerDate(new Date());
							newPo.setAdviser(null);
							newPo.setAllotAdviserDate(null);
							newPo.setAllotAgain(Constant.IF_TYPE_NO);
							newPo.setOldLeadsCode(null);
							dao.insert(newPo);
							
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_01.toString(), keyPk.toString(), "", dealerPo2.getDealerId().toString(), "", remindDate,"");
						}
					}
				}
			}
			act.setOutData("leadsCode",leadsCode);
			act.setForword(oemLeadsAllotUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * OEM初始使线索查询页面
	 */
	public void leadsFind() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setOutData("orgId", logonUser.getOrgId());
			act.setForword(oemLeadsFindUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * OEM初始线索查询
	 */
	public void leadsFindQuery() {
		OemLeadsManageDao dao = new OemLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			//获取页面参数
			String customerName = CommonUtils.checkNull(request.getParamValue("customer_name"));
			String telephone = CommonUtils.checkNull(request.getParamValue("telephone"));
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			String leadsOrigin = CommonUtils.checkNull(request.getParamValue("leads_origin"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String dPro = CommonUtils.checkNull(request.getParamValue("dPro"));
			String dCity = CommonUtils.checkNull(request.getParamValue("dCity"));
			String dArea = CommonUtils.checkNull(request.getParamValue("dArea"));
			String allotStatus = CommonUtils.checkNull(request.getParamValue("allot_status"));
			String timeOut = CommonUtils.checkNull(request.getParamValue("time_out"));
			String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.oemLeadsFindQuery(customerName,
					telephone,startDate,endDate,leadsOrigin,dPro,dCity,dArea,dealerCode,allotStatus,seriesId,timeOut,
					curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "销售线索查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * OEM初始线索重新分派经销商选择页面
	 */
	public void oemLeadsReAllotByOemInit() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String leadsCode = CommonUtils.checkNull(request.getParamValue("leadsCode"));
			String leadsGroup = CommonUtils.checkNull(request.getParamValue("leadsGroup"));
			act.setOutData("leadsCode", leadsCode);
			act.setOutData("leadsGroup", leadsGroup);
			act.setForword(oemLeadsReAllotSelectDealerUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * OEM初始线索重新分派
	 */
	public void oemLeadsReAllotByOem() {
		OemLeadsManageDao dao = new OemLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String leadsCode = CommonUtils.checkNull(request.getParamValue("leadsCode"));
			String leadsGroup = CommonUtils.checkNull(request.getParamValue("leadsGroup"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			
			//9：00-17:00之间分配给经销商的公司类信息，主管当日19:00前完成分配，顾问必须在当日完成处理
			//17:00-次日9:00分配给经销商的公司类信息，主管次日11:00前完成分配，顾问必须在次日12:00前完成处理。
			Date d1 = new Date(); //当前时间
			String d2 = "090000";
			String d3 = "170000";
			String remindDate = null;
			SimpleDateFormat f = new SimpleDateFormat("hhmmss"); //格式化为 hhmmss
			int d1Number = Integer.parseInt(f.format(d1).toString()); //将第一个时间格式化后转为int
			int d2Number = Integer.parseInt(d2); //将第二个时间格式化后转为int
			int d3Number = Integer.parseInt(d3); //将第三个时间格式化后转为int
			
			SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, 1);
								
			if(d1Number>=d2Number && d1Number<=d3Number){
				remindDate = f1.format(d1);
			} else {
				remindDate = f1.format(c.getTime());
			}
			
			//leadsGroup为空说明页面为单选线索进行重新分派
			if(leadsGroup==null || "".equals(leadsGroup)) {
				// 开始执行重新分派到选择的经销商
				// 获取销售线索信息
				TPcLeadsPO po = new TPcLeadsPO();
				po.setLeadsCode(Long.parseLong(leadsCode));
				TPcLeadsPO po2 = (TPcLeadsPO)dao.select(po).get(0);
				// 拆分经销商
				String [] dealerGroup= dealerCode.split(",");
				for(int i=0;i<dealerGroup.length;i++) {
					String dlrCode = dealerGroup[i];
					//取经销商ID
					TmDealerPO dealerPo = new TmDealerPO();
					dealerPo.setDealerCode(dlrCode);
					TmDealerPO dealerPo2 = (TmDealerPO)dao.select(dealerPo).get(0);
					//判断是否已建过档案（电话、姓名、经销商ID）
					List<Map<String, Object>> getHasList = null;
					getHasList=dao.getHasCustomer(po2.getTelephone(),po2.getCustomerName(),dealerPo2.getDealerId().toString());
					//已存在档案，自动分派到顾问，如果客户类型不属于战败、失效、保有将线索设为无效，并新增接触点信息
					if(getHasList.size()>0) {
						//获取已建档的分派顾问
						String adviser = getHasList.get(0).get("ADVISER").toString();
						String dealerId = getHasList.get(0).get("DEALER_ID").toString();
						String customerId = getHasList.get(0).get("CUSTOMER_ID").toString();
						String ctmType = getHasList.get(0).get("CTM_TYPE").toString();
						
						//修改销售线索主表状态为无效
//						TPcLeadsPO oldLeadsPo = new TPcLeadsPO();
//						oldLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
//						TPcLeadsPO newLeadsPo = new TPcLeadsPO();
//						newLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
//						if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
//							newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_04.longValue());
//						} else {
//							newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_01.longValue());
//						}
//						dao.update(oldLeadsPo, newLeadsPo);
						
						TPcLeadsAllotPO newPo = new TPcLeadsAllotPO();
						//线索分派主键ID
						Long allotCode = dao.getLongPK(newPo);
						newPo.setLeadsAllotId(allotCode);
						newPo.setLeadsCode(Long.parseLong(leadsCode));
						newPo.setCustomerName(po2.getCustomerName());
						newPo.setTelephone(po2.getTelephone());
						newPo.setRemark(null);
						newPo.setCreateDate(new Date());
						newPo.setCreateBy(logonUser.getUserId().toString());
						if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
							newPo.setStatus(Constant.STATUS_DISABLE);//设置为无效
						} else {
							newPo.setStatus(Constant.STATUS_ENABLE);//设置为有效
						}
						newPo.setDealerId(dealerPo2.getDealerId().toString());
						newPo.setAllotDealerDate(new Date());
						newPo.setAdviser(adviser);
						newPo.setAllotAdviserDate(new Date());
						newPo.setAllotAgain(Constant.IF_TYPE_YES);
						newPo.setIfConfirm(60321002);
						newPo.setConfirmDate(new Date());
//						newPo.setOldLeadsCode(null);
						dao.insert(newPo);
						
						if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
							//增加接触点信息
							CommonUtils.addContackPoint(Constant.POINT_WAY_01, "重复线索", customerId, adviser, dealerId);
							String repeatLeads = SequenceManager.getSequence("");
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_20.toString(), repeatLeads, customerId, dealerId, adviser, f1.format(new Date()),"");
							//标记线索为重复线索
							CommonUtils.updateIfRepeat(allotCode.toString());
							CommonUtils.updateLeadStatus(leadsCode);
						} else {
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_03.toString(), allotCode.toString(), customerId, dealerId, adviser, f1.format(new Date()),"");
						}
						//新增提醒信息
						//CommonUtils.addRemindInfo(Constant.REMIND_TYPE_03.toString(), allotCode.toString(), "", dealerPo2.getDealerId().toString(), adviser, remindDate,"");
					} else {//未存在则新增重新分派，不自动重新分派到顾问
						TPcLeadsAllotPO newPo = new TPcLeadsAllotPO();
						//获取主键
						Long keyPk = dao.getLongPK(newPo);
						newPo.setLeadsAllotId(keyPk);
						newPo.setLeadsCode(Long.parseLong(leadsCode));
						newPo.setCustomerName(po2.getCustomerName());
						newPo.setTelephone(po2.getTelephone());
						newPo.setRemark(null);
						newPo.setCreateDate(new Date());
						newPo.setCreateBy(logonUser.getUserId().toString());
						newPo.setStatus(Constant.STATUS_ENABLE);
						newPo.setDealerId(dealerPo2.getDealerId().toString());
						newPo.setAllotDealerDate(new Date());
						newPo.setAdviser(null);
						newPo.setAllotAdviserDate(null);
						newPo.setAllotAgain(Constant.IF_TYPE_YES);
						newPo.setOldLeadsCode(null);
						dao.insert(newPo);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_01.toString(), keyPk.toString(), "", dealerPo2.getDealerId().toString(), "", remindDate,"");
					}
				}
				// 页面多选线索进行分派
			} else {
				// 拆分销售线索
				String[] leadsGroup2 = leadsGroup.split(",");
				for (int i = 0; i < leadsGroup2.length; i++) {
					String lgCode = leadsGroup2[i];
					// 获取销售线索信息
					TPcLeadsPO po = new TPcLeadsPO();
					po.setLeadsCode(Long.parseLong(lgCode));
					TPcLeadsPO po2 = (TPcLeadsPO)dao.select(po).get(0);
					// 拆分经销商
					String[] dealerGroup = dealerCode.split(",");
					for(int j=0;j<dealerGroup.length;j++) {
						String dlrCode = dealerGroup[j];
						//取经销商ID
						TmDealerPO dealerPo = new TmDealerPO();
						dealerPo.setDealerCode(dlrCode);
						TmDealerPO dealerPo2 = (TmDealerPO)dao.select(dealerPo).get(0);
						//判断是否已建过档案（电话、姓名、经销商ID）
						List<Map<String, Object>> getHasList = null;
						getHasList=dao.getHasCustomer(po2.getTelephone(),po2.getCustomerName(),dealerPo2.getDealerId().toString());
						//已存在档案，自动分派到顾问，如果客户类型不属于战败、失效、保有将线索设为无效，并新增接触点信息
						if(getHasList.size()>0) {
							//获取已建档的分派顾问
							String adviser = getHasList.get(0).get("ADVISER").toString();
							String dealerId = getHasList.get(0).get("DEALER_ID").toString();
							String customerId = getHasList.get(0).get("CUSTOMER_ID").toString();
							String ctmType = getHasList.get(0).get("CTM_TYPE").toString();
							
							//修改销售线索主表状态为无效
//							TPcLeadsPO oldLeadsPo = new TPcLeadsPO();
//							oldLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
//							TPcLeadsPO newLeadsPo = new TPcLeadsPO();
//							newLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
//							if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
//								newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_04.longValue());
//							} else {
//								newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_01.longValue());
//							}
//							dao.update(oldLeadsPo, newLeadsPo);
							
							TPcLeadsAllotPO newPo = new TPcLeadsAllotPO();
							//线索分派主键ID
							Long allotCode = dao.getLongPK(newPo);
							newPo.setLeadsAllotId(allotCode);
							newPo.setLeadsCode(Long.parseLong(lgCode));
							newPo.setCustomerName(po2.getCustomerName());
							newPo.setTelephone(po2.getTelephone());
							newPo.setRemark(null);
							newPo.setCreateDate(new Date());
							newPo.setCreateBy(logonUser.getUserId().toString());
							if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
								newPo.setStatus(Constant.STATUS_DISABLE);//设置为无效
							} else {
								newPo.setStatus(Constant.STATUS_ENABLE);//设置为有效
							}
							newPo.setDealerId(dealerPo2.getDealerId().toString());
							newPo.setAllotDealerDate(new Date());
							newPo.setAdviser(adviser);
							newPo.setAllotAdviserDate(new Date());
							newPo.setAllotAgain(Constant.IF_TYPE_NO);
							newPo.setOldLeadsCode(null);
							newPo.setIfConfirm(60321002);
							newPo.setConfirmDate(new Date());
							dao.insert(newPo);
							
							if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
								//增加接触点信息
								CommonUtils.addContackPoint(Constant.POINT_WAY_01, "重复线索", customerId, adviser, dealerId);
								String repeatLeads = SequenceManager.getSequence("");
								//新增提醒信息
								CommonUtils.addRemindInfo(Constant.REMIND_TYPE_20.toString(), repeatLeads, customerId, dealerId, adviser, f1.format(new Date()),"");
								//标记线索为重复线索
								CommonUtils.updateIfRepeat(allotCode.toString());
								CommonUtils.updateLeadStatus(leadsCode);
							} else {
								//新增提醒信息
								CommonUtils.addRemindInfo(Constant.REMIND_TYPE_03.toString(), allotCode.toString(), customerId, dealerId, adviser, f1.format(new Date()),"");
							}
							//新增提醒信息
							//CommonUtils.addRemindInfo(Constant.REMIND_TYPE_03.toString(), allotCode.toString(), "", dealerPo2.getDealerId().toString(), adviser, remindDate,"");
						} else {//未存在则新增分派，不自动分派到顾问
							TPcLeadsAllotPO newPo = new TPcLeadsAllotPO();
							//获取主键
							Long keyPk = dao.getLongPK(newPo);
							newPo.setLeadsAllotId(keyPk);
							newPo.setLeadsCode(Long.parseLong(lgCode));
							newPo.setCustomerName(po2.getCustomerName());
							newPo.setTelephone(po2.getTelephone());
							newPo.setRemark(null);
							newPo.setCreateDate(new Date());
							newPo.setCreateBy(logonUser.getUserId().toString());
							newPo.setStatus(Constant.STATUS_ENABLE);
							newPo.setDealerId(dealerPo2.getDealerId().toString());
							newPo.setAllotDealerDate(new Date());
							newPo.setAdviser(null);
							newPo.setAllotAdviserDate(null);
							newPo.setAllotAgain(Constant.IF_TYPE_NO);
							newPo.setOldLeadsCode(null);
							dao.insert(newPo);
							
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_01.toString(), keyPk.toString(), "", dealerPo2.getDealerId().toString(), "", remindDate,"");
						}
					}
				}
			}
			act.setOutData("leadsCode",leadsCode);
			act.setForword(oemLeadsFindUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}