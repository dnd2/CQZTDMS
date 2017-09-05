/**
 * 
 */
package com.infodms.dms.actions.sales.planmanage.ProductPlan;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtil;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.planmanage.AreaResourceDao;
import com.infodms.dms.dao.sales.planmanage.MonthPlanDao;
import com.infodms.dms.dao.sales.planmanage.ProductPlanDao;
import com.infodms.dms.dao.sales.planmanage.ResourcesPlanDao;
import com.infodms.dms.dao.sales.planmanage.YearPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TmpVsProductionPlanPO;
import com.infodms.dms.po.TmpVsResourcesPO;
import com.infodms.dms.po.TtVsProductionPlanPO;
import com.infodms.dms.po.TtVsResourcesPlanPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

import flex.messaging.io.ArrayList;

/**
 * @author yangpo
 *
 */
public class ResourcesPlanImport extends BaseImport {

	public Logger logger = Logger.getLogger(ResourcesPlanImport.class);
	private final String ResourcesPlanImportCompleteUrl = "/jsp/sales/planmanage/quotaassign/resourscesplanimportcomplete.jsp";
	private final String resourcePlanCheckFailureUrl = "/jsp/sales/planmanage/quotaassign/areaImportCheck.jsp";
	private final String resourcePlanCheckSuccessureUrl = "/jsp/sales/planmanage/quotaassign/areaImportCheckSuccess.jsp";
	/*
	 * 查询是否存在导入未确认的分配资源
	 * 如果存在返回1
	 * 不存在返回0
	 */
	public void checkIsExistsPlan() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		AreaResourceDao dao=AreaResourceDao.getInstance();//获取实例
		//ProductPlanDao dao=ProductPlanDao.getInstance();buss_area
		try {
			RequestWrapper request = act.getRequest();
			String ymstr=request.getParamValue("plan_month");//导入分配资源月份
			
			String[] str=ymstr.split("-");
			String year=str[0];
			String month=str[1];//拆分年月
			String areaId=request.getParamValue("buss_area");//获取业务范围
			TtVsResourcesPlanPO po=new TtVsResourcesPlanPO();
			//TmpVsResourcesPO po=new TmpVsResourcesPO();	
			//TtVsProductionPlanPO po=new TtVsProductionPlanPO();
			po.setPlanYear(new Integer(year));//设置年份
			po.setPlanMonth(new Integer(month));//月份
			po.setCompanyId(logonUser.getCompanyId());//公司ID
			po.setAreaId(new Long(areaId));//业务范围
			po.setStatus(Constant.PLAN_MANAGE_UNCONFIRM);//导入数据待确认
			
			List<TtVsResourcesPlanPO> list=dao.select(po);
			
			int i=0;
			
			if(null!=list&&list.size()>0){
				i=1;
			}
			act.setOutData("isExists", i);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void resourcesPlanExcelOperate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ResourcesPlanDao dao=ResourcesPlanDao.getInstance();
        try {
			RequestWrapper request = act.getRequest();
			String ymstr=request.getParamValue("plan_month");
			String[] str=ymstr.split("-");
			String year=str[0];
			String month=str[1];
			String areaId=request.getParamValue("buss_area");
			TmpVsResourcesPO po=new TmpVsResourcesPO();
			po.setUserId(logonUser.getUserId().toString());
			//清空临时表中的数据
			dao.delete(po);
			long maxSize=1024*1024*5;
			insertIntoTmp(request, "uploadFile",3,3,maxSize);
			
			List<ExcelErrors> el=getErrList();
			if(null!=el&&el.size()>0){
				act.setOutData("errorList", el);
				act.setForword(resourcePlanCheckFailureUrl);
			}else{
				List<Map> list=getMapList();
				//将数据插入临时表
				insertTmp(list, logonUser.getUserId(),year,month);
				//校验临时表数据
				List<ExcelErrors> errorList=checkData(year,month,logonUser.getUserId(),logonUser.getOrgId(),new Long(areaId),logonUser.getCompanyId().toString());
				if(null!=errorList){
					act.setOutData("errorList", errorList);
					act.setForword(resourcePlanCheckFailureUrl);
				}else{
					TmDateSetPO conPo=new TmDateSetPO();
					conPo.setSetYear(year);
					conPo.setSetMonth(month);
					List<Map<String, Object>> weekList=dao.selectDateSetWeekList(conPo);
					po.setPlanYear(year);
					po.setPlanMonth(month);
					List<Map<String, Object>> tmpList=dao.selectCheckSuccessTmp(po, areaId, weekList);
					act.setOutData("tmpList", tmpList);
					act.setOutData("weekList", weekList);
					act.setOutData("year", year);
					act.setOutData("month", month);
					act.setOutData("buss_area", areaId);
					act.setForword(resourcePlanCheckSuccessureUrl);
				}
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 把所有导入记录插入TMP_YEARLY_PLAN
	 */
	private void insertTmp(List<Map> list,Long userId,String year,String month) throws Exception{
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
				parseCells(key, cells, userId, year,month);
			}
		}
		
	}
	/*
	 * 每一行插入TMP_YEARLY_PLAN
	 * 数字只截取30位
	 */
	private void parseCells(String rowNum,Cell[] cells,Long userId,String year,String month) throws Exception{
		    ResourcesPlanDao dao=new ResourcesPlanDao();
			TmpVsResourcesPO po=new TmpVsResourcesPO();
			po.setRowNumber(rowNum.trim());
			po.setPlanYear(year.trim());
			po.setPlanMonth(month);
			po.setGroupCode(subCell(cells[0].getContents().trim()));
			po.setPlanWeek(subCell(cells[1].getContents().trim()));
			po.setPlanAmt(subCell(cells[2].getContents().trim()));
			po.setUserId(userId.toString());
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
	private List<ExcelErrors> checkData(String year,String month,Long userId,Long orgId,Long areaId,String companyId){
		ResourcesPlanDao dao=new ResourcesPlanDao();
		TmpVsResourcesPO planConPo=new TmpVsResourcesPO();
		planConPo.setPlanYear(year);
		planConPo.setPlanMonth(month);
		planConPo.setUserId(userId.toString());
		List<TmpVsResourcesPO> list=dao.select(planConPo);
		if(null==list){
			list=new ArrayList();
		}
		
		ExcelErrors errors=null;
		TmpVsResourcesPO po=null;
		StringBuffer errorInfo=new StringBuffer("");
		boolean isError=false;
		
		List<ExcelErrors> errorList = new ArrayList();
		for(int i=0;i<list.size();i++){
			errors=new ExcelErrors();
			//取得TmpVsResourcesPO
			po=list.get(i);
			//取得行号
			String rowNum=po.getRowNumber();
			try {
				if(po.getPlanYear().indexOf("-")!=-1){
					isError=true;
					errorInfo.append("年度必须是正整数,");
				}
				new Integer(po.getPlanYear());
			} catch (Exception e) {
				isError=true;
				errorInfo.append("年度必须是正整数,");
			}
			try {
				if(po.getPlanMonth().indexOf("-")!=-1){
					isError=true;
					errorInfo.append("月度必须是正整数,");
				}
				new Integer(po.getPlanMonth());
			} catch (Exception e) {
				isError=true;
				errorInfo.append("月度必须是正整数,");
			}
			try {
				if(po.getPlanWeek().indexOf("-")!=-1){
					isError=true;
					errorInfo.append("周度必须是正整数,");
				}
				new Integer(po.getPlanWeek());
			} catch (Exception e) {
				isError=true;
				errorInfo.append("周度必须是正整数,");
			}
			try {
				if(po.getPlanAmt().indexOf("-")!=-1){
					isError=true;
					errorInfo.append("计划数量必须是正整数,");
				}
				new Integer(po.getPlanAmt());
			} catch (Exception e) {
				isError=true;
				errorInfo.append("计划数量必须是正整数,");
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
			ResourcesPlanDao dao=ResourcesPlanDao.getInstance();
			RequestWrapper request=act.getRequest();
			String year=CommonUtils.checkNull(request.getParamValue("year"));//取得目标年度
			String month=CommonUtils.checkNull(request.getParamValue("month"));
			String area=CommonUtils.checkNull(request.getParamValue("buss_area"));
			
			Map<String, Object> conMap=new HashMap<String, Object>();
			conMap.put("year", year);
			conMap.put("month", month);
			conMap.put("areaId", area);
			conMap.put("userId", logonUser.getUserId());
			conMap.put("companyId", logonUser.getCompanyId());
			
			if(null==year||"".equals(year)){
				throw new Exception("导入失败!");
			}
			if(null==month||"".equals(month)){
				throw new Exception("导入失败!");
			}
			
			//插入主表
			dao.insertResourcesPlan(conMap);
			//插入明细表
			dao.insertResourcesPlanDetail(conMap);
			//待分配资源确认
			resourcesTargetConfirmSubmint(year,month,area);
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setForword(ResourcesPlanImportCompleteUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 待分配资源确认
	 */
	public void resourcesTargetConfirmSubmint(String year,String month,String areaId){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ResourcesPlanDao dao=ResourcesPlanDao.getInstance();
		try {
			RequestWrapper request=act.getRequest();
			Integer planVer=dao.selectMaxPlanVer(year, month,areaId,logonUser.getCompanyId().toString());
			String plan_ver=String.valueOf(planVer+1);
			String plan_desc="";
			
			Integer maxVer=dao.selectMaxPlanVer(year, month, areaId,logonUser.getCompanyId().toString());
			if(maxVer.intValue()>=new Integer(plan_ver)){
				throw new Exception("更新冲突");
			}
			
			TtVsResourcesPlanPO conPo=new TtVsResourcesPlanPO();
			conPo.setPlanYear(new Integer(year));
			conPo.setPlanMonth(new Integer(month));
			conPo.setAreaId(new Long(areaId));
			conPo.setStatus(Constant.PLAN_MANAGE_UNCONFIRM);
			conPo.setCompanyId(logonUser.getCompanyId());
			
			TtVsResourcesPlanPO valPo=new TtVsResourcesPlanPO();
			valPo.setCompanyId(logonUser.getCompanyId());
			valPo.setPlanVer(new Integer(plan_ver));
			valPo.setPlanDesc(plan_desc);
			valPo.setStatus(Constant.PLAN_MANAGE_CONFIRM);
			
			dao.update(conPo, valPo);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	// 提供月度生产模版下载
	public void downloadTemple(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ResourcesPlanDao dao= ResourcesPlanDao.getInstance();
		RequestWrapper request = act.getRequest();
		OutputStream os = null;
		try{
			ResponseWrapper response = act.getResponse();
			
//			String areaId=CommonUtils.checkNull(request.getParamValue("buss_area"));
			
			// 公司ID
			String companyId = CommonUtils.checkNull(logonUser.getCompanyId());
			String areaId = request.getParamValue("buss_area");
			String year = request.getParamValue("plan_month").split("-")[0];
			String month = request.getParamValue("plan_month").split("-")[1];
			
			// 用于下载传参的集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			
			//标题
			List<Object> listHead = new LinkedList<Object>();
			listHead.add("配置代码");
			listHead.add("周次");
			listHead.add("数量");
			
			list.add(listHead);
			
			List<Map<String, Object>> monthList = dao.getTempDownload(companyId,areaId);
			List<Map<String, Object>> weekList = dao.getWeekofYear(year, month) ;
			for(int i=0; i<monthList.size();i++){
				for (int j=0; j<weekList.size(); j++) {
					List<Object> listValue = new LinkedList<Object>();
					listValue.add(monthList.get(i).get("GROUP_CODE") != null ? monthList.get(i).get("GROUP_CODE") : "");
					listValue.add(weekList.get(j).get("SET_WEEK")!= null ? weekList.get(j).get("SET_WEEK") : "");
					listValue.add("0");
					
					list.add(listValue);
				}
			}
			
			// 导出的文件名
			String fileName = "待分配资源导入模板.xls";
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
	
	public int getFirstWeek(int year,int month) throws Exception     { 
        try{ 
            java.util.Calendar   calendar   =   new   java.util.GregorianCalendar(year,month-1,1); 
            int   firstWeek   =   calendar.get(java.util.Calendar.WEEK_OF_YEAR); 
            return   firstWeek; 
            } 
          catch(Exception   e)   { 
            throw   new   Exception   (e.toString()); 
        } 
    }
}
