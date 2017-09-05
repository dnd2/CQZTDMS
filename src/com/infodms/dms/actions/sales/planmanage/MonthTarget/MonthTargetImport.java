/**
 * 
 */
package com.infodms.dms.actions.sales.planmanage.MonthTarget;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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
import com.infodms.dms.dao.sales.planmanage.MonthPlanDao;
import com.infodms.dms.dao.sales.planmanage.YearPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TmpVsMonthlyPlanPO;
import com.infodms.dms.po.TtVsMonthlyPlanPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;


/**
 * @author yangpo
 *
 */
public class MonthTargetImport extends BaseImport {

	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private final String monthPlanExcelImplortUrl = "/jsp/sales/planmanage/monthplan/monthplanimport.jsp";
	private final String monthPlanCheckSuccessUrl = "/jsp/sales/planmanage/monthplan/monthplanchecksuccess.jsp";
	private final String monthPlanCheckFailureUrl = "/jsp/sales/planmanage/monthplan/monthplancheckfailure.jsp";
	private final String monthPlanImportCompleteUrl = "/jsp/sales/planmanage/monthplan/monthplanimportcomplete.jsp";
	
	
	/**
	 * 月度目标初始使化导放页面
	 */
	public void monthPlanImportInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String yearOptions=PlanUtil.getPlanYearString("");
			act.setOutData("yearOptions", yearOptions);
			String monthOptions=PlanUtil.getMonthOptions("");
			act.setOutData("monthOptions", monthOptions);
		    List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaBusList", areaBusList);
			String curYear=PlanUtil.getCurrentYear();
			String curMonth=PlanUtil.getCurrentMonth();
			act.setOutData("curYear", curYear);
			act.setOutData("curMonth", curMonth);
			act.setForword(monthPlanExcelImplortUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 月度目标导入临时表
	 * 模板增加：经销商名称\车系名称
	 * modify by wjb at 2010-08-18
	 */
	public void monthPlanExcelOperate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		MonthPlanDao dao=MonthPlanDao.getInstance();
		String dealerSql = dao.getDealerIdByPostSql(logonUser);
        try {
			RequestWrapper request = act.getRequest();
			String year=CommonUtils.checkNull(request.getParamValue("year"));
			String month=CommonUtils.checkNull(request.getParamValue("month"));
			String areaId=CommonUtils.checkNull(request.getParamValue("buss_area"));
			//任务类型：批售计划和零售计划
			String planType = CommonUtils.checkNull(request.getParamValue("plan_type"));
			TmpVsMonthlyPlanPO po=new TmpVsMonthlyPlanPO();
			po.setPlanYear(year);//目标年度
			po.setPlanMonth(month);
			po.setUserId(logonUser.getUserId().toString());
//			po.setPlanType(planType);
			String companyId = CommonUtils.checkNull(logonUser.getCompanyId());
			List<Map<String, Object>> orgGroupList = dao.getGroupByOrg("", companyId, null);
			int count=4;
			count=count+orgGroupList.size();
			//清空临时表中目标年度的数据
			dao.delete(po);
			long maxSize=1024*1024*5;
			insertIntoTmp(request, "uploadFile",count,3,maxSize);
			List<ExcelErrors> el=getErrList();
			if(null!=el&&el.size()>0){
				act.setOutData("errorList", el);
				act.setForword(monthPlanCheckFailureUrl);
			}else{
				List<Map> list=getMapList();
				//将数据插入临时表
				insertTmpYearlyPlan(orgGroupList,list, logonUser.getUserId(),year,month);
				//校验临时表数据
				List<ExcelErrors> errorList=checkData(year,month,logonUser.getUserId(),logonUser.getOrgId(),dealerSql,logonUser.getCompanyId().toString());
//				List errorList = null;
				if(null!=errorList){
					act.setOutData("errorList", errorList);
					act.setForword(monthPlanCheckFailureUrl);
				}else{
					List<Map<String, Object>> tmpList=dao.oemSelectTmpMonthPlan(year,month,logonUser.getUserId());
					act.setOutData("planList", tmpList);
					act.setOutData("year", year);
					act.setOutData("month", month);
					act.setOutData("buss_area", areaId);
					act.setOutData("planType", planType);
					act.setForword(monthPlanCheckSuccessUrl);
				}
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}	
	/*
	 * 把所有导入记录插入
	 */
	private void insertTmpYearlyPlan(List<Map<String, Object>> list1,List<Map> list,Long userId,String year,String month) throws Exception{
		if(null==list){
			list=new ArrayList();
		}
		int tesize = list.size();
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
				parseCells(list1,key, cells, userId, year,month);
			}
		}
		
	}
	/*
	 * 每一行插入TMP_YEARLY_PLAN
	 * 数字只截取30位
	 */
	private void parseCellsOld(String rowNum,Cell[] cells,Long userId,String year,String month) throws Exception{
		    MonthPlanDao dao=new MonthPlanDao();
			TmpVsMonthlyPlanPO po=new TmpVsMonthlyPlanPO();
			po.setRowNumber(rowNum.trim());
			po.setOrgCode(subCell(cells[0].getContents().trim()));
			po.setPlanYear(year.trim());
			po.setPlanMonth(month);
			po.setGroupCode(subCell(cells[1].getContents().trim()));
			po.setSumAmt(subCell(cells[2].getContents().trim()));
			po.setUserId(userId.toString());
			dao.insert(po);
	}
	
	/*
	 * 每一行插入TMP_YEARLY_PLAN（长安汽车）
	 * 数字只截取30位
	 * modify by wjb at 2010-08-18
	 * 增加列头：经销商名称（模板第2列）和车系名称（模板第4列），但不插入临时表
	 */
	private void parseCells(List<Map<String, Object>> orgGroupList,String rowNum,Cell[] cells,Long userId,String year,String month) throws Exception{
		    MonthPlanDao dao=new MonthPlanDao();
		    for(int i=0;i<orgGroupList.size();i++){
			TmpVsMonthlyPlanPO po=new TmpVsMonthlyPlanPO();
			po.setRowNumber(rowNum.trim());
//			po.setOrgCode(subCell(cells[0].getContents().trim()));
		    po.setDealerCode(subCell(cells[2].getContents().trim()));
			po.setPlanYear(year.trim());
			po.setPlanMonth(month);
			po.setGroupCode(orgGroupList.get(i).get("GROUP_CODE") != null ?orgGroupList.get(i).get("GROUP_CODE").toString():"");//车系代码对应模板第四列
            if (cells.length>i+4) {
                po.setSumAmt(subCell(cells[4+i].getContents().trim()));//月度任务对应模板第六列
            }
			po.setUserId(userId.toString());
			dao.insert(po);
		    }
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
	 * 校验TMP_monthly_PLAN表中数据是否符合导入标准
	 */
	private List<ExcelErrors> checkData(String year,String month,Long userId,Long orgId,String dealerSql,String companyId){
		MonthPlanDao dao=new MonthPlanDao();
		TmpVsMonthlyPlanPO planConPo=new TmpVsMonthlyPlanPO();
		planConPo.setPlanYear(year);
		planConPo.setPlanMonth(month);
		planConPo.setUserId(userId.toString());
		List<TmpVsMonthlyPlanPO> list=dao.select(planConPo);
		if(null==list){
			list=new ArrayList();
		}
		ExcelErrors errors=null;
		TmpVsMonthlyPlanPO po=null;
		StringBuffer errorInfo=new StringBuffer("");
		boolean isError=false;
		List<ExcelErrors> errorList = new ArrayList<ExcelErrors>();
		for(int i=0;i<list.size();i++){
			errors=new ExcelErrors();
			//取得TmpYearlyPlanPO
			po=list.get(i);
			//取得行号
			String rowNum=po.getRowNumber();
			//校验合计
			try {
				if(po.getSumAmt().indexOf("-")!=-1){
					isError=true;
					errorInfo.append("任务数量必须是正整数,");
				}
				new Integer(po.getSumAmt());
			} catch (Exception e) {
				isError=true;
				errorInfo.append("任务数量必须是正整数,");
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
		conMap.put("dealerSql", dealerSql);
		conMap.put("userId", userId);
		conMap.put("companyId", companyId);
		//String groupArea=PlanUtil.getAllGroupInArea(dealerSql, companyId,"2",2);//zxf
		String groupArea=PlanUtil.getAllGroupInArea( companyId,"2",2);
		conMap.put("groupArea", groupArea);
		//月度任务校验组织是否存在
//		List<Map<String, Object>> notExistsOrgList=dao.oemMonthPlanCheckOrg(conMap);
//		if(null!=notExistsOrgList&&notExistsOrgList.size()>0){
//			for(int i=0;i<notExistsOrgList.size();i++){
//				Map<String, Object> map=notExistsOrgList.get(i);
//				isError=true;
//				ExcelErrors err=new ExcelErrors();
//				err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
//				err.setErrorDesc("组织代码不存在");
//				errorList.add(err);
//			}
//		}
//		//月度任务校验组织是否与业务范围一致
//		List<Map<String, Object>> notExistsOrgAreaList=dao.oemMonthPlanCheckOrgArea(conMap);
//		if(null!=notExistsOrgAreaList&&notExistsOrgAreaList.size()>0){
//			for(int i=0;i<notExistsOrgAreaList.size();i++){
//				Map<String, Object> map=notExistsOrgAreaList.get(i);
//				isError=true;
//				ExcelErrors err=new ExcelErrors();
//				err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
//				err.setErrorDesc("组织不在业务范围内");
//				errorList.add(err);
//			}
//		}
		//月度任务校验经销商是否存在
		List<Map<String, Object>> notExistsDlrList=dao.oemMonthPlanCheckDlr(conMap);
		if(null!=notExistsDlrList&&notExistsDlrList.size()>0){
			for(int i=0;i<notExistsDlrList.size();i++){
				Map<String, Object> map=notExistsDlrList.get(i);
				isError=true;
				ExcelErrors err=new ExcelErrors();
				err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
				err.setErrorDesc("经销商代码不存在");
				errorList.add(err);
			}
		}
		//月度任务验经销商是否与业务范围一致
		List<Map<String, Object>> notExistsDlrAreaList=dao.sbuMonthPlanCheckDlrArea(conMap);
		if(null!=notExistsDlrAreaList&&notExistsDlrAreaList.size()>0){
			for(int i=0;i<notExistsDlrAreaList.size();i++){
				Map<String, Object> map=notExistsDlrAreaList.get(i);
				isError=true;
				ExcelErrors err=new ExcelErrors();
				err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
				err.setErrorDesc("经销商不在业务范围内");
				errorList.add(err);
			}
		}
		//月度任务校验车系是否存在
		List<Map<String, Object>> notExistsGroupList=dao.oemMonthPlanCheckGroup(conMap);
		if(null!=notExistsGroupList&&notExistsGroupList.size()>0){
			for(int i=0;i<notExistsGroupList.size();i++){
				Map<String, Object> map=notExistsGroupList.get(i);
				isError=true;
				ExcelErrors err=new ExcelErrors();
				err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
				err.setErrorDesc("车系代码不存在");
				errorList.add(err);
			}
		}
		//月度任务校验车系是否与业务范围一致
		List<Map<String, Object>> notExistsGroupAreaList=dao.oemMonthPlanCheckGroupArea(conMap);
		if(null!=notExistsGroupAreaList&&notExistsGroupAreaList.size()>0){
			for(int i=0;i<notExistsGroupAreaList.size();i++){
				Map<String, Object> map=notExistsGroupAreaList.get(i);
				isError=true;
				ExcelErrors err=new ExcelErrors();
				err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
				err.setErrorDesc("车系与业务范围不一致");
				errorList.add(err);
			}
		}
		//临时表校验重复数据
		List<Map<String, Object>> dumpList=dao.talbeCheckDump(year, month, userId.toString());
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
	/*
	 * 导入业务表
	 */
	public void importExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			MonthPlanDao dao=MonthPlanDao.getInstance();
			String dealersql = dao.getDealerIdByPostSql(logonUser);
			RequestWrapper request=act.getRequest();
			String year=CommonUtils.checkNull(request.getParamValue("year"));
			String month=CommonUtils.checkNull(request.getParamValue("month"));
			// String area=CommonUtils.checkNull(request.getParamValue("buss_area"));
			// String planType = CommonUtils.checkNull(request.getParamValue("planType"));
			if(null==year||"".equals(year)){
				throw new Exception("导入失败!");
			}
			if(null==month||"".equals(month)){
				throw new Exception("导入失败!");
			}
			//数据校验条件的MAP
			Map<String, Object> conMap=new HashMap<String, Object>();
			conMap.put("year", year);
			conMap.put("month", month);
			conMap.put("dealersql", dealersql);
			// conMap.put("areaId", area);
			conMap.put("userId", logonUser.getUserId().toString());
			conMap.put("companyId", logonUser.getCompanyId().toString());
			// conMap.put("planType", planType);
			//增加导入的组织类型：（是区域还是经销商导入的）
			conMap.put("dutyType", logonUser.getDutyType(logonUser.getDealerId()));
			dao.insertPlan(conMap);
			dao.insertPlanDetail(conMap);
			/*//清空业务表中，该用户已导入未确认的年度目标
			TtVsMonthlyPlanPO clrPo=new TtVsMonthlyPlanPO();
			clrPo.setPlanYear(new Integer(year));
			clrPo.setPlanMonth(new Integer(month));
			clrPo.setAreaId(new Long(area));
			clrPo.setStatus(Constant.PLAN_MANAGE_UNCONFIRM);
			//查询要删除的结果集
			List<TtVsMonthlyPlanPO> clrList=dao.select(clrPo);
			if(null!=clrList&&clrList.size()>0){
				TtVsMonthlyPlanDetailPO detailPo=null;
				TtVsMonthlyPlanPO actPo=null;
				for(int i=0;i<clrList.size();i++){
					actPo=clrList.get(i);
					detailPo=new TtVsMonthlyPlanDetailPO();
					detailPo.setPlanId(actPo.getPlanId());
					//删除明细表
					dao.delete(detailPo);
					//删除业务表
					dao.clearUserMonthPlan(actPo);
				}
			}
			//查询要插入到业务表的临时表数据
			TmpVsMonthlyPlanPO planConPo=new TmpVsMonthlyPlanPO();
			planConPo.setPlanYear(year);
			planConPo.setPlanMonth(month);
			planConPo.setUserId(logonUser.getUserId().toString());
			List<TmpVsMonthlyPlanPO> tmpList=dao.select(planConPo);
			if(null==tmpList){
				tmpList=new ArrayList();
			}
			
			for(int i=0;i<tmpList.size();i++){
				TmpVsMonthlyPlanPO po=new TmpVsMonthlyPlanPO();
				po=tmpList.get(i);
				TtVsMonthlyPlanPO newPo=new TtVsMonthlyPlanPO();
				newPo=getCommonPo(po, year,month, logonUser,area);
				dao.insert(newPo);
				TtVsMonthlyPlanDetailPO detailPo=new TtVsMonthlyPlanDetailPO();
				detailPo.setDetailId(new Long(SequenceManager.getSequence("")));
				detailPo.setPlanId(newPo.getPlanId());
				detailPo.setMaterialGroupid(getGroupId(po.getGroupCode()));
				detailPo.setSaleAmount(po.getSumAmt()==null?new Integer(0):new Integer(po.getSumAmt()));
				detailPo.setCreateBy(logonUser.getUserId());
				detailPo.setCreateDate(new Date());
				dao.insert(detailPo);
			}*/
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setForword(monthPlanImportCompleteUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//取得业务表的PO，这里ORGTYPE需要确定
	private TtVsMonthlyPlanPO getCommonPo(TmpVsMonthlyPlanPO po,String year,String month,AclUserBean logonUser,String areaId){
		MonthPlanDao dao=MonthPlanDao.getInstance();
		TtVsMonthlyPlanPO ttPo=new TtVsMonthlyPlanPO();
		String seq=SequenceManager.getSequence("");
		ttPo.setPlanId(new Long(seq));//目标ID
		ttPo.setAreaId(new Long(areaId));
		ttPo.setCompanyId(logonUser.getCompanyId());
		ttPo.setPlanYear(new Integer(year));
		ttPo.setPlanMonth(new Integer(month));
		ttPo.setOrgType(Constant.ORG_TYPE_OEM);//区域
		//查询组织
		TmOrgPO conOrgPo=new TmOrgPO();
		conOrgPo.setOrgCode(po.getOrgCode());
		List<TmOrgPO> orgList=dao.select(conOrgPo);
		if(null==orgList||orgList.size()==0){
			return null;
		}
		TmOrgPO orgPo=orgList.get(0);
		ttPo.setOrgId(orgPo.getOrgId());
		
		
		//目标类型，目前写死为采购计划
		ttPo.setPlanType(Constant.PLAN_TYPE_BUY);
		ttPo.setStatus(Constant.PLAN_MANAGE_UNCONFIRM);
		ttPo.setVer(new Integer(0));
		ttPo.setCreateDate(new Date());
		ttPo.setCreateBy(logonUser.getUserId());
		return ttPo;
	}
	
	private Long getGroupId(String groupCode){
		MonthPlanDao dao=MonthPlanDao.getInstance();
		TmVhclMaterialGroupPO conPo=new TmVhclMaterialGroupPO();
		conPo.setGroupCode(groupCode);
		List<TmVhclMaterialGroupPO> groupList=dao.select(conPo);
		if(null==groupList||groupList.size()==0){
			return null;
		}else{
			return groupList.get(0).getGroupId();
		}
	}
	
	/**
	 * 月度任务导入模板下载
	 * @author zouchao
	 * @since  2010-08-20
	 */
	public void downloadTemple(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		YearPlanDao dao=YearPlanDao.getInstance();
		String dealerId = dao.getDealerIdByPostSql(logonUser);
		RequestWrapper request = act.getRequest();
		OutputStream os = null;
		try{
			ResponseWrapper response = act.getResponse();
			// 公司ID
			String companyId = CommonUtils.checkNull(logonUser.getCompanyId());
			// String areaId = request.getParamValue("buss_area");//业务范围
//			String areaId=CommonUtils.checkNull(request.getParamValue("buss_area"));
			// 用于下载传参的集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			//标题
			List<Object> listHead = new LinkedList<Object>();//导出模板第一列
			listHead.add("省份");
			listHead.add("大区名称");
			listHead.add("经销商代码");
			listHead.add("经销商名称");
			List<Map<String, Object>> orgGroupList = dao.getGroupByOrg("", companyId, null);
			for(int j=0;j<orgGroupList.size() ;j++){
				listHead.add(orgGroupList.get(j).get("GROUP_CODE") != null ?orgGroupList.get(j).get("GROUP_NAME")+"("+ orgGroupList.get(j).get("GROUP_CODE")+")" : "");
			}
			list.add(listHead);
			List<Map<String, Object>> orgSeriesList = dao.getTempDownloadByOrg("",companyId, dealerId);
			for(int i=0; i<orgSeriesList.size();i++){
				List<Object> listValue = new LinkedList<Object>();
				listValue.add(orgSeriesList.get(i).get("REGION_NAME") != null ? orgSeriesList.get(i).get("REGION_NAME") : "");
				listValue.add(orgSeriesList.get(i).get("ORG_NAME") != null ? orgSeriesList.get(i).get("ORG_NAME") : "");
				listValue.add(orgSeriesList.get(i).get("DEALER_CODE") != null ? orgSeriesList.get(i).get("DEALER_CODE") : "");
				listValue.add(orgSeriesList.get(i).get("DEALER_NAME") != null ? orgSeriesList.get(i).get("DEALER_NAME") : "");
				for(int j=0;j<orgGroupList.size() ;j++){
					listValue.add("0");
				}
				list.add(listValue);
			}
			
			// 导出的文件名
			String fileName = "月度任务导入模板.xls";
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
}
