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
import com.infodms.dms.dao.sales.planmanage.MonthPlanDao;
import com.infodms.dms.dao.sales.planmanage.ProductPlanDao;
import com.infodms.dms.dao.sales.planmanage.YearPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TmpVsProductionPlanPO;
import com.infodms.dms.po.TtVsProductionPlanPO;
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
public class ProductPlanImport extends BaseImport {

	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private final String productPlanExcelImplortUrl = "/jsp/sales/planmanage/productplan/productplanimport.jsp";
	private final String productPlanCheckSuccessUrl = "/jsp/sales/planmanage/productplan/productplanchecksuccess.jsp";
	private final String productPlanCheckFailureUrl = "/jsp/sales/planmanage/productplan/productplancheckfailure.jsp";
	private final String productPlanImportCompleteUrl = "/jsp/sales/planmanage/productplan/productplanimportcomplete.jsp";
	private final String productPlanImportOutDateUrl = "/jsp/sales/planmanage/productplan/productplanimportoutdate.jsp";
	/**
	 * 初始使化导放页面
	 */
	public void productPlanImportInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			int curDay=PlanUtil.getCurrentDay();
			String codeStr="2011,2012";
			Map<String, Object> map=PlanUtil.selectBussinessPara(codeStr, logonUser.getCompanyId().toString());
			int startDay=Integer.parseInt(map.get("20121001").toString());
			int endDay=Integer.parseInt(map.get("20121002").toString());
			
			if(startDay>curDay||endDay<curDay){
				act.setForword(productPlanImportOutDateUrl);
			}else{
				String year=PlanUtil.getCurrentYear();
				int month=Integer.parseInt(PlanUtil.getCurrentMonth());
				int pre=Integer.parseInt(map.get("20111001").toString());
				List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
				act.setOutData("areaBusList", areaBusList);
				act.setOutData("year", year);
				act.setOutData("month", month+pre+"");
				act.setForword(productPlanExcelImplortUrl);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 查询是否存在导入未确认的生产计划
	 */
	public void checkIsExistsPlan() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ProductPlanDao dao=ProductPlanDao.getInstance();
		try {
			RequestWrapper request = act.getRequest();
			String ymstr=request.getParamValue("plan_month");
			
			String[] str=ymstr.split(",");
			String year=str[0];
			String month=str[1];
			String areaId=request.getParamValue("buss_area");
			
			TtVsProductionPlanPO po=new TtVsProductionPlanPO();
			po.setPlanYear(new Integer(year));
			po.setPlanMonth(new Integer(month));
			po.setCompanyId(logonUser.getCompanyId());
			po.setAreaId(new Long(areaId));
			po.setStatus(Constant.PLAN_MANAGE_UNCONFIRM);
			
			List<TtVsProductionPlanPO> list=dao.select(po);
			
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
	/**
	 * 年度目标导入临时表
	 */
	public void productPlanExcelOperate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ProductPlanDao dao=ProductPlanDao.getInstance();
        try {
			RequestWrapper request = act.getRequest();
			String ymstr=request.getParamValue("plan_month");
			String[] str=ymstr.split(",");
			String year=str[0];
			String month=str[1];
			String areaId=request.getParamValue("buss_area");
			TmpVsProductionPlanPO po=new TmpVsProductionPlanPO();
			po.setUserId(logonUser.getUserId().toString());
			//清空临时表中的数据
			dao.delete(po);
			long maxSize=1024*1024*5;
			insertIntoTmp(request, "uploadFile",3,3,maxSize);
			
			List<ExcelErrors> el=getErrList();
			if(null!=el&&el.size()>0){
				act.setOutData("errorList", el);
				act.setForword(productPlanCheckFailureUrl);
			}else{
				List<Map> list=getMapList();
				//将数据插入临时表
				insertTmp(list, logonUser.getUserId(),year,month);
				//校验临时表数据
				List<ExcelErrors> errorList=checkData(year,month,logonUser.getUserId(),logonUser.getOrgId(),new Long(areaId),logonUser.getCompanyId().toString());
				if(null!=errorList){
					act.setOutData("errorList", errorList);
					act.setForword(productPlanCheckFailureUrl);
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
					act.setForword(productPlanCheckSuccessUrl);
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
		    ProductPlanDao dao=new ProductPlanDao();
			TmpVsProductionPlanPO po=new TmpVsProductionPlanPO();
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
		ProductPlanDao dao=new ProductPlanDao();
		TmpVsProductionPlanPO planConPo=new TmpVsProductionPlanPO();
		planConPo.setPlanYear(year);
		planConPo.setPlanMonth(month);
		planConPo.setUserId(userId.toString());
		List<TmpVsProductionPlanPO> list=dao.select(planConPo);
		if(null==list){
			list=new ArrayList();
		}
		
		ExcelErrors errors=null;
		TmpVsProductionPlanPO po=null;
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
		String groupArea=PlanUtil.getAllGroupInArea(companyId,"2",4);
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
	/*
	 * 导入业务表
	 */
	public void importExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			ProductPlanDao dao=ProductPlanDao.getInstance();
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
			/*//清空业务表中，该用户已导入未确认的年度目标
			TtVsProductionPlanPO clrPo=new TtVsProductionPlanPO();
			clrPo.setPlanYear(new Integer(year));
			clrPo.setPlanMonth(new Integer(month));
			clrPo.setAreaId(new Long(area));
			clrPo.setCreateBy(logonUser.getUserId());
			clrPo.setStatus(Constant.PLAN_MANAGE_UNCONFIRM);
			//查询要删除的结果集
			List<TtVsProductionPlanPO> clrList=dao.select(clrPo);
			if(null!=clrList&&clrList.size()>0){
				TtVsProductionPlanDetailPO detailPo=null;
				TtVsProductionPlanPO actPo=null;
				for(int i=0;i<clrList.size();i++){
					actPo=clrList.get(i);
					detailPo=new TtVsProductionPlanDetailPO();
					detailPo.setPlanId(actPo.getPlanId());
					//删除明细表
					dao.delete(detailPo);
					//删除业务表
					dao.delete(actPo);
				}
			}*/
			/*//查询要插入到业务表的临时表数据
			TmpVsProductionPlanPO planConPo=new TmpVsProductionPlanPO();
			planConPo.setPlanYear(year);
			planConPo.setPlanMonth(month);
			planConPo.setUserId(logonUser.getUserId().toString());
			List<TmpVsProductionPlanPO> tmpList=dao.select(planConPo);
			if(null==tmpList){
				tmpList=new ArrayList();
			}
			
			for(int i=0;i<tmpList.size();i++){
				TmpVsProductionPlanPO po=new TmpVsProductionPlanPO();
				po=tmpList.get(i);
				TtVsProductionPlanPO newPo=new TtVsProductionPlanPO();
				newPo=getCommonPo(po, year,month, logonUser,area);
				dao.insert(newPo);
				TtVsProductionPlanDetailPO detailPo=new TtVsProductionPlanDetailPO();
				detailPo.setDetailId(new Long(SequenceManager.getSequence("")));
				detailPo.setPlanId(newPo.getPlanId());
				detailPo.setGroupId(getGroupId(po.getGroupCode()));
				detailPo.setPlanAmount(po.getPlanAmt()==null?new Integer(0):new Integer(po.getPlanAmt()));
				detailPo.setCreateBy(logonUser.getUserId());
				detailPo.setCreateDate(new Date());
				dao.insert(detailPo);
			}*/
			//插入主表
			dao.insertProductPlan(conMap);
			//插入明细表
			dao.insertProductPlanDetail(conMap);
			
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setForword(productPlanImportCompleteUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//取得业务表的PO，这里ORGTYPE需要确定
	private TtVsProductionPlanPO getCommonPo(TmpVsProductionPlanPO po,String year,String month,AclUserBean logonUser,String areaId){
		TtVsProductionPlanPO ttPo=new TtVsProductionPlanPO();
		String seq=SequenceManager.getSequence("");
		ttPo.setPlanId(new Long(seq));//目标ID
		ttPo.setAreaId(new Long(areaId));
		ttPo.setCompanyId(logonUser.getCompanyId());
		ttPo.setPlanYear(new Integer(year));
		ttPo.setPlanMonth(new Integer(month));
		ttPo.setPlanWeek(new Integer(po.getPlanWeek()));
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
	// 提供月度生产模版下载
	public void downloadTemple(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ProductPlanDao dao= ProductPlanDao.getInstance();
		RequestWrapper request = act.getRequest();
		OutputStream os = null;
		try{
			ResponseWrapper response = act.getResponse();
			
//			String areaId=CommonUtils.checkNull(request.getParamValue("buss_area"));
			
			// 公司ID
			String companyId = CommonUtils.checkNull(logonUser.getCompanyId());
			String areaId = request.getParamValue("buss_area");
			String year = request.getParamValue("plan_month").split(",")[0];
			String month = request.getParamValue("plan_month").split(",")[1];
			
			// 用于下载传参的集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			
			//标题
			List<Object> listHead = new LinkedList<Object>();
			listHead.add("物料代码");
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
			String fileName = "月度生产计划导入模板.xls";
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
