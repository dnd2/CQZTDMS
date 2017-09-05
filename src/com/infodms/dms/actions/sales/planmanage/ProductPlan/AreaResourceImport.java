package com.infodms.dms.actions.sales.planmanage.ProductPlan;

import java.util.HashMap;
import java.util.Iterator;
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
import com.infodms.dms.dao.sales.planmanage.AreaResourceDao;
import com.infodms.dms.dao.sales.planmanage.ProductPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmpVsProductionPlanPO;
import com.infodms.dms.po.TmpVsResourcesPO;
import com.infodms.dms.po.TtVsProductionPlanPO;
import com.infodms.dms.po.TtVsResourcePO;
import com.infodms.dms.po.TtVsResourcesPlanPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

import flex.messaging.io.ArrayList;

public class AreaResourceImport extends BaseImport{
	
	public Logger logger = Logger.getLogger(AreaResourceImport.class);
	private final String resourcePlanCheckFailureUrl = "/jsp/sales/planmanage/quotaassign/areaImportCheck.jsp";
	private final String resourcePlanCheckSuccessureUrl = "/jsp/sales/planmanage/quotaassign/areaImportCheckSuccess.jsp";
	private final String productPlanConfrimCompleteUrl = "/jsp/sales/planmanage/quotaassign/areaQuotaCalculateQuery.jsp";
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
			
			String[] str=ymstr.split(",");
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
	/*
	 * 物料导入插入临时表
	 */
	public void productPlanExcelOperate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		 AreaResourceDao  dao=AreaResourceDao.getInstance();//获取实例
        try {
        	
			RequestWrapper request = act.getRequest();
			String ymstr=request.getParamValue("plan_month");//获取月份
			String[] str=ymstr.split("-");//拆分年月
			String year=str[0];
			String month=str[1];
			String areaId=request.getParamValue("buss_area");//获取范围
			TmpVsResourcesPO po=new TmpVsResourcesPO();
			//TmpVsProductionPlanPO po=new TmpVsProductionPlanPO();
			po.setUserId(logonUser.getUserId().toString());
			//清空临时表中的数据
			dao.delete(po);//清空临时表中当前用户的所有数据
			long maxSize=1024*1024*5;
			insertIntoTmp(request, "uploadFile",3,3,maxSize);//将数据提取出来插入数组
			Integer planVer=dao.selectMaxPlanVer(year, month,areaId,logonUser.getCompanyId().toString());
			List<ExcelErrors> el=getErrList();//将错误信息出入错误数组中并提取出来
			if(null!=el&&el.size()>0){
				act.setOutData("errorList", el);
				act.setForword(resourcePlanCheckFailureUrl);//返回报错页面
			}else{
				List<Map> list=getMapList();//返回正确数据并存入数据库
				//将数据插入临时表
				insertTmp(list, logonUser.getUserId(),year,month);//对正确数据实现临时库做插入处理
				//校验临时表数据
				List<ExcelErrors> errorList=checkData(year,month,logonUser.getUserId(),logonUser.getOrgId(),new Long(areaId),logonUser.getCompanyId().toString());
				if(null!=errorList){
					productTargetConfirmSubmint(year,month,areaId,planVer,"");
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
	 * 把所有导入记录插入临时表tmp_vs_resources
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
	 * 月度任务确认
	 */
	public void productTargetConfirmSubmint(String year,String month,String areaId,Integer plan_ver,String plan_desc){
		AreaResourceDao  dao=AreaResourceDao.getInstance();//获取实例
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		//ProductPlanDao dao=ProductPlanDao.getInstance();
		try {
			RequestWrapper request=act.getRequest();
			
//			Integer maxVer=dao.selectMaxPlanVer(year, month, areaId,logonUser.getCompanyId().toString());
//			if(maxVer.intValue()>=new Integer(plan_ver)){
//				throw new Exception("更新冲突");
//			}
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
			act.setOutData("year", year);
			act.setOutData("month", month);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/*
	 * 每一行插入tmp_vs_resources
	 * 数字只截取30位
	 */
	private void parseCells(String rowNum,Cell[] cells,Long userId,String year,String month) throws Exception{
			AreaResourceDao dao=new AreaResourceDao();
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
		AreaResourceDao dao=new AreaResourceDao();
		TmpVsResourcesPO resourcePo=new TmpVsResourcesPO();
		resourcePo.setPlanYear(year);
		resourcePo.setPlanMonth(month);
		resourcePo.setUserId(userId.toString());
		List<TmpVsResourcesPO> list=dao.select(resourcePo);
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
			//取得TmpVsProductionPlanPO
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
       //数据校验条件的MAP
		
		Map<String, Object> conMap=new HashMap<String, Object>();
		conMap.put("year", year);
		conMap.put("month", month);
		conMap.put("areaId", areaId);
		conMap.put("userId", userId);
		conMap.put("companyId", companyId);
		String groupArea=PlanUtil.getAllGroupInArea( companyId,"2",4);//zxf
		conMap.put("groupArea", groupArea);
		
		//生产计划导入校验车系是否存在
		List<Map<String, Object>> notExistsGroupList=dao.oemProductPlanCheckGroup(conMap);
		if(null!=notExistsGroupList&&notExistsGroupList.size()>0){
			for(int i=0;i<notExistsGroupList.size();i++){
				Map<String, Object> map=notExistsGroupList.get(i);
				isError=true;
				ExcelErrors err=new ExcelErrors();
				err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
				err.setErrorDesc("配置代码不存在");
				errorList.add(err);
			}
		}
		//生产计划导入校验车系是否与业务范围一致
		List<Map<String, Object>> notExistsGroupAreaList=dao.oemProductPlanCheckGroupArea(conMap);
		if(null!=notExistsGroupAreaList&&notExistsGroupAreaList.size()>0){
			for(int i=0;i<notExistsGroupAreaList.size();i++){
				Map<String, Object> map=notExistsGroupAreaList.get(i);
				isError=true;
				ExcelErrors err=new ExcelErrors();
				err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
				err.setErrorDesc("配置与业务范围不一致");
				errorList.add(err);
			}
		}
		//校验周次在TM_DATE_SET工作日历中是否存在
		List<Map<String, Object>> checkDateSetWeek=dao.checkDateSetWeek(conMap);
		if(null!=checkDateSetWeek&&checkDateSetWeek.size()>0){
			for(int i=0;i<checkDateSetWeek.size();i++){
				Map<String, Object> map=checkDateSetWeek.get(i);
				isError=true;
				ExcelErrors err=new ExcelErrors();
				err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
				err.setErrorDesc("周次错误");
				errorList.add(err);
			}
		}
		//临时表校验重复数据
		List<Map<String, Object>> dumpList=dao.talbeCheckDump(conMap);
		if(null!=dumpList&&dumpList.size()>0){
			
			String r1="";
			String r2="";
			List<String> tmp=new ArrayList();
			String s1="";
			String s2="";
			for(int i=0;i<dumpList.size();i++){
				Map<String, Object> map=dumpList.get(i);
				r1=map.get("ROW_NUMBER1").toString();
				r2=map.get("ROW_NUMBER2").toString();
				s1=r1+","+r2;
				s2=r2+","+r1;
				if(tmp.contains(s1)||tmp.contains(s2)){
					continue;
				}else{
					isError=true;
					ExcelErrors err=new ExcelErrors();
					err.setRowNum(Integer.parseInt(r1));
					err.setErrorDesc("与"+r2+"行数据重复");
					errorList.add(err);
					tmp.add(s1);
				}
				
			}
		}
		if(isError){
			return errorList;
		}else{
			return null;
		}
	}
	
	
	
}
