/**
 * 
 */
package com.infodms.dms.actions.sales.planmanage.YearTarget;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
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
import com.infodms.dms.dao.sales.planmanage.YearPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TmpVsYearPlanNewPO;
import com.infodms.dms.po.TmpVsYearlyPlanPO;
import com.infodms.dms.po.TtVsYearlyPlanDetailPO;
import com.infodms.dms.po.TtVsYearlyPlanPO;
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
public class YearTargetImport extends BaseImport {

	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private final String yearPlanExcelImplortUrl = "/jsp/sales/planmanage/yearplan/yearplanimport.jsp";
	private final String yearPlanCheckSuccessUrl = "/jsp/sales/planmanage/yearplan/yearplanchecksuccess.jsp";
	private final String yearPlanCheckFailureUrl = "/jsp/sales/planmanage/yearplan/yearplancheckfailure.jsp";
	private final String yearPlanImportCompleteUrl = "/jsp/sales/planmanage/yearplan/yearlyplanimportcomplete.jsp";
	private String areaId;
	
	private String getAreaId() {
		return areaId;
	}
	private void setAreaId(String areaId) {
		this.areaId = areaId.trim();
	}
	/**
	 * 初始使化导放页面
	 */
	public void yearPlanImportInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaBusList", areaBusList);
			String options=getPlanYearString();
			act.setOutData("options", options);
			act.setForword(yearPlanExcelImplortUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 取得当年和下一年的option列表
	 * @return
	 */
	private  String getPlanYearString(){
		StringBuffer sbf=new StringBuffer("");
		Calendar calendar=Calendar.getInstance();
		sbf.append("<option value='"+calendar.get(Calendar.YEAR)+"'>");
		sbf.append(calendar.get(Calendar.YEAR)+"");
		sbf.append("</option>");
		calendar.add(Calendar.YEAR, 1);
		sbf.append("<option value='"+calendar.get(Calendar.YEAR)+"'>");
		sbf.append(calendar.get(Calendar.YEAR)+"");
		sbf.append("</option>");
		return sbf.toString();
	}

	/**
	 * 年度目标导入临时表
	 */
	@SuppressWarnings("unchecked")
	public void yearPlanExcelOperate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		YearPlanDao dao=YearPlanDao.getInstance();
        try {
			RequestWrapper request = act.getRequest();
			String year=CommonUtils.checkNull(request.getParamValue("year"));
			String areaId=CommonUtils.checkNull(request.getParamValue("buss_area"));
			String planType=CommonUtils.checkNull(request.getParamValue("planType"));
			setAreaId(areaId);
			TmpVsYearPlanNewPO po=new TmpVsYearPlanNewPO();
			po.setPlanYear(year);//目标年度
			po.setUserId(logonUser.getUserId().toString());
			//清空临时表中目标年度的数据
			dao.delete(po);
			long maxSize=1024*1024*5;
			//校验excel
			String companyId = CommonUtils.checkNull(logonUser.getCompanyId());
			List<Map<String, Object>> orgGroupList = dao.getGroupByOrg("", companyId, areaId);
			insertIntoTmp(request, "uploadFile",orgGroupList.size()+4,3,maxSize);
			List<ExcelErrors> el=getErrList();
			if(null!=el&&el.size()>0){
				act.setOutData("errorList", el);
				act.setForword(yearPlanCheckFailureUrl);
			}else{
				List<Map> list=getMapList();
				//将数据插入临时表
				insertTmpYearlyPlan(list, logonUser.getUserId(),year,planType,orgGroupList);
				//校验临时表数据
				List<ExcelErrors> errorList=checkData(year,logonUser.getUserId(),logonUser.getOrgId(),logonUser.getCompanyId().toString(),areaId);
				if(null!=errorList){
					act.setOutData("errorList", errorList);
					act.setForword(yearPlanCheckFailureUrl);
				}else{
					List<Map<String, Object>> tmpList=dao.oemSelectTmpMonthPlan(year,logonUser.getUserId());
					act.setOutData("planList", tmpList);
					act.setOutData("year", year);
					act.setOutData("buss_area", areaId);
					act.setOutData("planType", planType);
					act.setForword(yearPlanCheckSuccessUrl);
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
	private void insertTmpYearlyPlan(List<Map> list,Long userId,String year,String planType,List<Map<String, Object>> orgGroupList) throws Exception{
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
				parseCells(key, cells, userId, year,planType,orgGroupList);
			}
		}
		
	}
	/*aa
	 * 每一行插入TMP_YEARLY_PLAN
	 * 数字只截取30位
	 */
	@SuppressWarnings("unchecked")
	private void parseCells(String rowNum,Cell[] cells,Long userId,String year,String planType,List<Map<String, Object>> orgGroupList) throws Exception{
		for (int i=0;i<orgGroupList.size();i++) {
			TmpVsYearPlanNewPO po=new TmpVsYearPlanNewPO();
			po.setUserId(userId.toString());
			po.setRowNumber(rowNum.trim());
			po.setDealerCode(cells[2].getContents().trim());
			po.setPlanYear(year.trim());
			po.setPlanType(planType.trim());
			po.setDealerName(cells[3].getContents());
			po.setGroupCode(orgGroupList.get(i).get("GROUP_CODE")==null?"":orgGroupList.get(i).get("GROUP_CODE").toString());
			po.setGroupName(orgGroupList.get(i).get("GROUP_NAME")==null?"":orgGroupList.get(i).get("GROUP_NAME").toString());
			if (cells.length>i+4) {
	           po.setSumAmt(subAmt(cells[4+i].getContents().trim()));
	        }
			YearPlanDao dao=YearPlanDao.getInstance();
	        dao.insert(po);	
		}
			/*po.setJanAmt(subAmt(cells[5].getContents().trim()));
			po.setFebAmt(subAmt(cells[6].getContents().trim()));
			po.setMarAmt(subAmt(cells[7].getContents().trim()));
			po.setAprAmt(subAmt(cells[8].getContents().trim()));
			po.setMayAmount(subAmt(cells[9].getContents().trim()));
			po.setJunAmt(subAmt(cells[10].getContents().trim()));
			po.setJulAmt(subAmt(cells[11].getContents().trim()));
			po.setAugAmt(subAmt(cells[12].getContents().trim()));
			po.setSepAmt(subAmt(cells[13].getContents().trim()));
			po.setOctAmt(subAmt(cells[14].getContents().trim()));
			po.setNovAmt(subAmt(cells[15].getContents().trim()));
			po.setDecAmt(subAmt(cells[16].getContents().trim()));*/
	}
	/*
	 * 将输入字符截取最多30位
	 */
	private String subAmt(String orgAmt){
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
	 * 校验TMP_YEARLY_PLAN表中数据是否符合导入标准
	 * DEALER_CODE 是否存在
	 * GROUP_CODE 车系代码是否存在
	 * 合计是否等于12个月数量合
	 * 合计、月份数量是否是整是整数在写入临时表时校验，以异常形式处理
	 */
	private List<ExcelErrors> checkData(String year,Long userId,Long orgId,String companyId,String areaId){
		YearPlanDao dao=YearPlanDao.getInstance();
		TmpVsYearPlanNewPO planConPo=new TmpVsYearPlanNewPO();
		planConPo.setPlanYear(year);
		planConPo.setUserId(userId.toString());
		List<TmpVsYearPlanNewPO> list=dao.selectTmpYearlyPlanNew(planConPo);
		if(null==list){
			list=new ArrayList();
		}
		ExcelErrors errors=null;
		TmpVsYearPlanNewPO po=null;
		StringBuffer errorInfo=new StringBuffer("");
		boolean isError=false;
		List<ExcelErrors> errorList = new LinkedList<ExcelErrors>();
		for(int i=0;i<list.size();i++){
			errors=new ExcelErrors();
			//取得TmpYearlyPlanPO
			po=list.get(i);
			//取得行号
			String rowNum=po.getRowNumber();
			Map<String, Object> conMap=new HashMap<String, Object>();
			conMap.put("year", year);
			conMap.put("areaId", areaId);
			conMap.put("userId", userId);
			conMap.put("companyId", companyId);
			String groupArea=PlanUtil.getAllGroupInArea( companyId,"2",2);
			conMap.put("groupArea", groupArea);
			//校验经销商是否存在
			List<Map<String, Object>> notExistsDlrList=dao.oemMonthPlanCheckDlr(conMap);
			if(null!=notExistsDlrList&&notExistsDlrList.size()>0){
				for(int j=0;j<notExistsDlrList.size();j++){
					Map<String, Object> map=notExistsDlrList.get(j);
					isError=true;
					ExcelErrors err=new ExcelErrors();
					err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
					err.setErrorDesc("经销商代码不存在");
					errorList.add(err);
				}
			}
			//校验经销商是否与业务范围一致
			List<Map<String, Object>> notExistsDlrAreaList=dao.sbuMonthPlanCheckDlrArea(conMap);
			if(null!=notExistsDlrAreaList&&notExistsDlrAreaList.size()>0){
				for(int k=0;k<notExistsDlrAreaList.size();k++){
					Map<String, Object> map=notExistsDlrAreaList.get(k);
					isError=true;
					ExcelErrors err=new ExcelErrors();
					err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
					err.setErrorDesc("经销商不在业务范围内");
					errorList.add(err);
				}
			}
			//年度任务校验车系是否存在
			List<Map<String, Object>> notExistsGroupList=dao.oemMonthPlanCheckGroup(conMap);
			if(null!=notExistsGroupList&&notExistsGroupList.size()>0){
				for(int l=0;l<notExistsGroupList.size();l++){
					Map<String, Object> map=notExistsGroupList.get(l);
					isError=true;
					ExcelErrors err=new ExcelErrors();
					err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
					err.setErrorDesc("车系代码不存在");
					errorList.add(err);
				}
			}
			//年度任务校验车系是否与业务范围一致
			List<Map<String, Object>> notExistsGroupAreaList=dao.oemMonthPlanCheckGroupArea(conMap);
			if(null!=notExistsGroupAreaList&&notExistsGroupAreaList.size()>0){
				for(int p=0;p<notExistsGroupAreaList.size();p++){
					Map<String, Object> map=notExistsGroupAreaList.get(p);
					isError=true;
					ExcelErrors err=new ExcelErrors();
					err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
					err.setErrorDesc("车系与业务范围不一致");
					errorList.add(err);
				}
			}
			//临时表校验重复数据
			List<Map<String, Object>> dumpList=dao.talbeCheckDump(year, userId.toString());
			if(null!=dumpList&&dumpList.size()>0){
				String r1="";
				String r2="";
				List<String> tmp=new ArrayList();
				String s1="";
				String s2="";
				for(int o=0;o<dumpList.size();o++){
					Map<String, Object> map=dumpList.get(o);
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
			/*//校验合计
			String numCheck=checkSumAmt(po);
			if(numCheck.length()!=0){
				isError=true;
				errorInfo.append(numCheck);
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
		conMap.put("areaId", areaId);
		conMap.put("userId", userId);
		conMap.put("companyId", companyId);
		String groupArea=PlanUtil.getAllGroupInArea(areaId.toString(), companyId,"2",2);
		conMap.put("groupArea", groupArea);
		
		//年度目标校验组织是否存在
		List<Map<String, Object>> notExistsOrgList=dao.oemYearPlanCheckOrg(conMap);
		if(null!=notExistsOrgList&&notExistsOrgList.size()>0){
			for(int i=0;i<notExistsOrgList.size();i++){
				Map<String, Object> map=notExistsOrgList.get(i);
				isError=true;
				ExcelErrors err=new ExcelErrors();
				err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
				err.setErrorDesc("组织代码不存在");
				errorList.add(err);
			}
		}
		//年度目标校验组织是否与业务范围一致
		List<Map<String, Object>> notExistsOrgAreaList=dao.oemYearPlanCheckOrgArea(conMap);
		if(null!=notExistsOrgAreaList&&notExistsOrgAreaList.size()>0){
			for(int i=0;i<notExistsOrgAreaList.size();i++){
				Map<String, Object> map=notExistsOrgAreaList.get(i);
				isError=true;
				ExcelErrors err=new ExcelErrors();
				err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
				err.setErrorDesc("组织不在业务范围内");
				errorList.add(err);
			}
		}
		//年度目标校验车系是否存在
		List<Map<String, Object>> notExistsGroupList=dao.oemYearPlanCheckGroup(conMap);
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
		//年度目标校验车系是否与业务范围一致
		List<Map<String, Object>> notExistsGroupAreaList=dao.oemYearPlanCheckGroupArea(conMap);
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
		List<Map<String, Object>> dumpList=dao.oemTalbeCheckDump(year, userId.toString());
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
				
			}*/
		}
		//年度目标校验车系是否与经销商的业务范围相同,由于这个校验比较耗时,所以有其他错误时不优先校验该项
		/*if(errorList.size()==0){
			List<Map<String, Object>> groupAreaCheckRsList=dao.deptTalbeCheckGroupArea(conMap);
			if(null!=groupAreaCheckRsList&&groupAreaCheckRsList.size()>0){
				for(int i=0;i<groupAreaCheckRsList.size();i++){
					Map<String, Object> map=groupAreaCheckRsList.get(i);
					isError=true;
					ExcelErrors err=new ExcelErrors();
					err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
					err.setErrorDesc("车系与组织的业务范围不符");
					errorList.add(err);
				}
			}
		}*/
		if(isError){
			return errorList;
		}else{
			return null;
		}
		
	}
	/*
	 * 校验合计
	 */
	private String checkSumAmt(TmpVsYearlyPlanPO po){
	    Integer sumTmp=new Integer(0);
	    StringBuffer errors=new StringBuffer("");
	    try {
			Integer janAmt=new Integer(po.getJanAmt());
			sumTmp+=janAmt;
		} catch (Exception e) {
			errors.append("1月数量必须是整数,");
		}
	    try {
			Integer febAmt=new Integer(po.getFebAmt());
			sumTmp+=febAmt;
		} catch (Exception e) {
			errors.append("2月数量必须是整数,");
		}
	    try {
			Integer marAmt=new Integer(po.getMarAmt());
			sumTmp+=marAmt;
		} catch (Exception e) {
			errors.append("3月数量必须是整数,");
		}
	    try {
			Integer aprAmt=new Integer(po.getAprAmt());
			sumTmp+=aprAmt;
		} catch (Exception e) {
			errors.append("4月数量必须是整数,");
		}
	    try {
			Integer mayAmt=new Integer(po.getMayAmount());
			sumTmp+=mayAmt;
		} catch (Exception e) {
			errors.append("5月数量必须是整数,");
		}
	    try {
			Integer junAmt=new Integer(po.getJunAmt());
			sumTmp+=junAmt;
		} catch (Exception e) {
			errors.append("6月数量必须是整数,");
		}
	    try {
			Integer julAmt=new Integer(po.getJulAmt());
			sumTmp+=julAmt;
		} catch (Exception e) {
			errors.append("7月数量必须是整数,");
		}
	    try {
			Integer augAmt=new Integer(po.getAugAmt());
			sumTmp+=augAmt;
		} catch (Exception e) {
			errors.append("8月数量必须是整数,");
		}
	    try {
			Integer sepAmt=new Integer(po.getSepAmt());
			sumTmp+=sepAmt;
		} catch (Exception e) {
			errors.append("9月数量必须是整数,");
		}
	    try {
			Integer octAmt=new Integer(po.getOctAmt());
			sumTmp+=octAmt;
		} catch (Exception e) {
			errors.append("10月数量必须是整数,");
		}
	    try {
			Integer novAmt=new Integer(po.getNovAmt());
			sumTmp+=novAmt;
		} catch (Exception e) {
			errors.append("11月数量必须是整数,");
		}
	    try {
			Integer decAmt=new Integer(po.getDecAmt());
			sumTmp+=decAmt;
		} catch (Exception e) {
			errors.append("12月数量必须是整数,");
		}
		if(errors.length()==0){
			if(sumTmp.longValue()!=new Long(po.getSumAmt()).longValue()){
				errors.append("合计错误,");
			}
		}
		return errors.toString();
	}
	/*
	 * 导入业务表
	 */
	public void importExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			YearPlanDao dao=YearPlanDao.getInstance();
			RequestWrapper request=act.getRequest();
			String year=CommonUtils.checkNull(request.getParamValue("year"));//取得目标年度
			//String areaId=CommonUtils.checkNull(request.getParamValue("buss_area"));
			// String planType=CommonUtils.checkNull(request.getParamValue("planType"));
			// setAreaId(areaId);
			String dealerSql = dao.getDealerIdByPostSql(logonUser);
			if(null==year||"".equals(year)){
				throw new Exception("导入失败!");
			}
			Map<String, Object> conMap=new HashMap<String, Object>();
			conMap.put("companyId", logonUser.getCompanyId().toString());
			conMap.put("userId", logonUser.getUserId().toString());
			conMap.put("dealerSql", dealerSql);
			conMap.put("year", year);
			// conMap.put("planType", planType);
			conMap.put("orgType", Constant.ORG_TYPE_OEM);
			dao.insertPlan(conMap);
			dao.insertPlanDetail(conMap);
			/*dao.clrTable(conMap);
			dao.clrDetailTable(conMap);
			//清空业务表中，该用户已导入未确认的年度目标
			TtVsYearlyPlanPO clrPo=new TtVsYearlyPlanPO();
			clrPo.setPlanYear(new Integer(year));
			clrPo.setCreateBy(logonUser.getUserId());
			clrPo.setStatus(Constant.PLAN_MANAGE_UNCONFIRM);
			//查询要删除的结果集
			List<TtVsYearlyPlanPO> clrList=dao.select(clrPo);
			if(null!=clrList&&clrList.size()>0){
				TtVsYearlyPlanDetailPO detailPo=null;
				TtVsYearlyPlanPO actPo=null;
				for(int i=0;i<clrList.size();i++){
					actPo=clrList.get(i);
					detailPo=new TtVsYearlyPlanDetailPO();
					detailPo.setPlanId(actPo.getPlanId());
					//删除明细表
					dao.delete(detailPo);
					//删除业务表
					dao.clearUserYearlyPlan(actPo);
				}
			}
			//查询要插入到业务表的临时表数据
			TmpVsYearlyPlanPO planConPo=new TmpVsYearlyPlanPO();
			planConPo.setPlanYear(year);
			planConPo.setUserId(logonUser.getUserId().toString());
			List<TmpVsYearlyPlanPO> tmpList=dao.select(planConPo);
			if(null==tmpList){
				tmpList=new ArrayList();
			}
			
			for(int i=0;i<tmpList.size();i++){
				TmpVsYearlyPlanPO po=new TmpVsYearlyPlanPO();
				po=tmpList.get(i);
				TtVsYearlyPlanPO newPo=new TtVsYearlyPlanPO();
				newPo=getCommonPo(po, year,planType, logonUser);
				dao.insert(newPo);
				TtVsYearlyPlanDetailPO detailPo=null;
				for(int j=1;j<13;j++){
					switch (j) {
					case 1:
						insertDetailPo(po.getGroupCode(), newPo.getPlanId(), new Integer(1),new Integer(po.getJanAmt()),logonUser.getUserId());
						break;
					case 2:
						insertDetailPo(po.getGroupCode(), newPo.getPlanId(), new Integer(2),new Integer(po.getFebAmt()),logonUser.getUserId());
						break;
					case 3:
						insertDetailPo(po.getGroupCode(), newPo.getPlanId(), new Integer(3),new Integer(po.getMarAmt()),logonUser.getUserId());
						break;
					case 4:
						insertDetailPo(po.getGroupCode(), newPo.getPlanId(), new Integer(4),new Integer(po.getAprAmt()),logonUser.getUserId());
						break;
					case 5:
						insertDetailPo(po.getGroupCode(), newPo.getPlanId(), new Integer(5),new Integer(po.getMayAmount()),logonUser.getUserId());
						break;
					case 6:
						insertDetailPo(po.getGroupCode(), newPo.getPlanId(), new Integer(6),new Integer(po.getJunAmt()),logonUser.getUserId());
						break;
					case 7:
						insertDetailPo(po.getGroupCode(), newPo.getPlanId(), new Integer(7),new Integer(po.getJulAmt()),logonUser.getUserId());
						break;
					case 8:
						insertDetailPo(po.getGroupCode(), newPo.getPlanId(), new Integer(8),new Integer(po.getAugAmt()),logonUser.getUserId());
						break;
					case 9:
						insertDetailPo(po.getGroupCode(), newPo.getPlanId(), new Integer(9),new Integer(po.getSepAmt()),logonUser.getUserId());
						break;
					case 10:
						insertDetailPo(po.getGroupCode(), newPo.getPlanId(), new Integer(10),new Integer(po.getOctAmt()),logonUser.getUserId());
						break;
					case 11:
						insertDetailPo(po.getGroupCode(), newPo.getPlanId(), new Integer(11),new Integer(po.getNovAmt()),logonUser.getUserId());
						break;
					default:
						insertDetailPo(po.getGroupCode(), newPo.getPlanId(), new Integer(12),new Integer(po.getDecAmt()),logonUser.getUserId());
						break;
					}
				}
			}
			*/
			act.setOutData("year", year);
			act.setForword(yearPlanImportCompleteUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//取得业务表的PO，这里ORGTYPE需要确定
	private TtVsYearlyPlanPO getCommonPo(TmpVsYearlyPlanPO po,String year,String planType,AclUserBean logonUser){
		YearPlanDao dao=YearPlanDao.getInstance();
		TtVsYearlyPlanPO ttPo=new TtVsYearlyPlanPO();
		String seq=SequenceManager.getSequence("");
		ttPo.setPlanId(new Long(seq));//目标ID
		ttPo.setCompanyId(logonUser.getCompanyId());
		ttPo.setPlanYear(new Integer(year));
		ttPo.setOrgType(Constant.ORG_TYPE_OEM);//区域
		ttPo.setAreaId(new Long(getAreaId()));
		//查询代理商
		TmOrgPO conOrgPo=new TmOrgPO();
		conOrgPo.setOrgCode(po.getOrgCode());
		List<TmOrgPO> orgList=dao.select(conOrgPo);
		if(null==orgList||orgList.size()==0){
			return null;
		}
		TmOrgPO orgPo=orgList.get(0);
		ttPo.setOrgId(orgPo.getOrgId());
		
		//目标类型，目前写死为采购计划
		ttPo.setPlanType(new Integer(planType));
		ttPo.setStatus(Constant.PLAN_MANAGE_UNCONFIRM);
		ttPo.setVer(new Integer(0));
		ttPo.setCreateDate(new Date());
		ttPo.setCreateBy(logonUser.getUserId());
		return ttPo;
	}
	
	private Long getGroupId(String groupCode){
		YearPlanDao dao=YearPlanDao.getInstance();
		TmVhclMaterialGroupPO conPo=new TmVhclMaterialGroupPO();
		conPo.setGroupCode(groupCode);
		List<TmVhclMaterialGroupPO> groupList=dao.select(conPo);
		if(null==groupList||groupList.size()==0){
			return null;
		}else{
			return groupList.get(0).getGroupId();
		}
	}
	
	private void insertDetailPo(String groupCode,Long planId,Integer month,Integer saleAmt,Long userId){
		YearPlanDao dao=YearPlanDao.getInstance();
		TtVsYearlyPlanDetailPO detailPo=new TtVsYearlyPlanDetailPO();
		detailPo.setDetailId(new Long(SequenceManager.getSequence("")));
		detailPo.setPlanId(planId);
		detailPo.setPlanMonth(month);
		detailPo.setSaleAmount(saleAmt);
		Long groupId=getGroupId(groupCode);
		detailPo.setMaterialGroupid(groupId==null?new Long(0):groupId);
		detailPo.setCreateBy(userId);
		detailPo.setCreateDate(new Date());
		dao.insertTtYearlyPlanDetial(detailPo);
	}
	
	/**
	 * 年度目标导入模板下载
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
//			String areaId=CommonUtils.checkNull(request.getParamValue("buss_area"));
			// 公司ID
			String companyId = CommonUtils.checkNull(logonUser.getCompanyId());
			// String areaId = request.getParamValue("buss_area");
			// 用于下载传参的集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			//标题
			List<Object> listHead = new LinkedList<Object>();//导出模板第一列
			listHead.add("省份");
			listHead.add("大区名称");
			listHead.add("经销商代码");
			listHead.add("经销商名称");
			List<Map<String, Object>> orgGroupList = dao.getGroupByOrg("", companyId, dealerId);
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
			
			/*//标题
			List<Object> listHead = new LinkedList<Object>();
			listHead.add("区域代码");
			listHead.add("区域名称");
			listHead.add("车系代码");
			listHead.add("车系名称");
			listHead.add("年度总量");
			listHead.add("1月数量");
			listHead.add("2月数量");
			listHead.add("3月数量");
			listHead.add("4月数量");
			listHead.add("5月数量");
			listHead.add("6月数量");
			listHead.add("7月数量");
			listHead.add("8月数量");
			listHead.add("9月数量");
			listHead.add("10月数量");
			listHead.add("11月数量");
			listHead.add("12月数量");
			
			list.add(listHead);
			
			List<Map<String, Object>> orgSeriesList = dao.getTempDownload(companyId, areaId);
			for(int i=0; i<orgSeriesList.size();i++){
				List<Object> listValue = new LinkedList<Object>();
				listValue.add(orgSeriesList.get(i).get("ORG_CODE") != null ? orgSeriesList.get(i).get("ORG_CODE") : "");
				listValue.add(orgSeriesList.get(i).get("ORG_NAME") != null ? orgSeriesList.get(i).get("ORG_NAME") : "");
				listValue.add(orgSeriesList.get(i).get("GROUP_CODE") != null ? orgSeriesList.get(i).get("GROUP_CODE") : "");
				listValue.add(orgSeriesList.get(i).get("GROUP_NAME") != null ? orgSeriesList.get(i).get("GROUP_NAME") : "");
				listValue.add("0");
				listValue.add("0");
				listValue.add("0");
				listValue.add("0");
				listValue.add("0");
				listValue.add("0");
				listValue.add("0");
				listValue.add("0");
				listValue.add("0");
				listValue.add("0");
				listValue.add("0");
				listValue.add("0");
				listValue.add("0");
				list.add(listValue);
			}*/
			
			// 导出的文件名
			String fileName = "年度目标导入模板.xls";
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
