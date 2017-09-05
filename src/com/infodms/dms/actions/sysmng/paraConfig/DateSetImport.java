package com.infodms.dms.actions.sysmng.paraConfig;

import java.util.Calendar;
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
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.paraConfigDao.DateSetImportDao;
import com.infodms.dms.dao.sales.planmanage.YearPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmpDateSetPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

import flex.messaging.io.ArrayList;

public class DateSetImport extends BaseImport {
	
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private final String dateSetImplortUrl = "/jsp/systemMng/paraConfig/datesetimport.jsp";
	private final String dateSetCheckSuccessUrl = "/jsp/systemMng/paraConfig/datesetchecksuccess.jsp";
	private final String dateSetCheckFailureUrl = "/jsp/systemMng/paraConfig/datesetcheckfailure.jsp";
	private final String dateSetImportCompleteUrl = "/jsp/systemMng/paraConfig/datesetimportcomplete.jsp";

	/**
	 * 初始使化导放页面
	 */
	public void dateSetImportInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Calendar calendar=Calendar.getInstance();
			Integer year=new Integer(calendar.get(Calendar.YEAR));
			if(null==year){
				year=new Integer(0);
			}
			act.setOutData("year", year);
			act.setForword(dateSetImplortUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 年度目标导入临时表
	 */
	public void dateSetExcelOperate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		DateSetImportDao dao=DateSetImportDao.getInstance();
        try {
			RequestWrapper request = act.getRequest();
			String year=request.getParamValue("year");
			TmpDateSetPO po=new TmpDateSetPO();
			//po.setSetYear(year);
			po.setUserId(logonUser.getUserId().toString());
			//清空临时表中的数据
			dao.delete(po);
			//设置文件校验大小
			long maxSize=1024*1024*5;
			int errNum=insertIntoTmp(request, "uploadFile",4,3,maxSize);
			List<Map> list=getMapList();
			if(errNum!=0){
				List<ExcelErrors> errorList=new ArrayList();
				ExcelErrors ees=new ExcelErrors();
				ees.setRowNum(0);
				switch (errNum) {
				case 1:
					ees.setErrorDesc("文件列数不正确");
					break;
				case 2:
					ees.setErrorDesc("空行不能大于三行");
					break;
				case 3:
					ees.setErrorDesc("文件不能为空");
					break;
				case 4:
					ees.setErrorDesc("文件不能为空");
					break;
				case 5:
					ees.setErrorDesc("文件不能大于"+maxSize);
					break;
				default:
					break;
				}
				errorList.add(ees);
				act.setOutData("errorList", errorList);
				act.setForword(dateSetCheckFailureUrl);
			}else{
				//将数据插入临时表
				insertTmp(list, logonUser.getUserId());
				//校验临时表数据
				List<ExcelErrors> errorList=checkData(year,logonUser.getUserId(),logonUser.getOrgId());
				if(null!=errorList){
					act.setOutData("errorList", errorList);
					act.setForword(dateSetCheckFailureUrl);
				}else{
					List<Map<String, Object>> tmpList=dao.selectTmpDateSet(logonUser.getUserId().toString());
					act.setOutData("tmpList", tmpList);
					act.setOutData("year", year);
					act.setForword(dateSetCheckSuccessUrl);
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
	private void insertTmp(List<Map> list,Long userId) throws Exception{
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
	 * 每一行插入TMP_YEARLY_PLAN
	 * 数字只截取30位
	 */
	private void parseCells(String rowNum,Cell[] cells,Long userId) throws Exception{
			TmpDateSetPO po=new TmpDateSetPO();
			po.setRownumber(rowNum.trim());
			po.setSetYear(cells[0].getContents().trim());
			po.setSetMonth(cells[1].getContents().trim());
			po.setSetWeek(cells[2].getContents().trim());
			po.setSetDate(cells[3].getContents().trim());
			po.setUserId(userId.toString());
			YearPlanDao dao=YearPlanDao.getInstance();
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
	 * 校验TMP_YEARLY_PLAN表中数据是否符合导入标准
	 * DEALER_CODE 是否存在
	 * GROUP_CODE 车系代码是否存在
	 * 合计是否等于12个月数量合
	 * 合计、月份数量是否是整是整数在写入临时表时校验，以异常形式处理
	 */
	private List<ExcelErrors> checkData(String year,Long userId,Long orgId){
		YearPlanDao dao=YearPlanDao.getInstance();
		TmpDateSetPO dateSetPo=new TmpDateSetPO();
		//dateSetPo.setSetYear(year);
		dateSetPo.setUserId(userId.toString());
		List<TmpDateSetPO> list=dao.select(dateSetPo);
		if(null==list){
			list=new ArrayList();
		}
		
		ExcelErrors errors=null;
		TmpDateSetPO po=null;
		StringBuffer errorInfo=new StringBuffer("");
		boolean isError=false;
		List<ExcelErrors> errorList = new LinkedList<ExcelErrors>();
		for(int i=0;i<list.size();i++){
			errors=new ExcelErrors();
			//取得TmpYearlyPlanPO
			po=list.get(i);
			//取得行号
			String rowNum=po.getRownumber();
			String setYear=po.getSetYear();
			String setMonth=po.getSetMonth();
			String setWeek=po.getSetWeek();
			if(!year.equals(setYear)){
				isError=true;
			    errorInfo.append("年度必须是"+year+",");
			}
			try {
				new Integer(setYear);
			} catch (Exception e) {
				isError=true;
			    errorInfo.append("年必须是数字,");
			}
			try {
				new Integer(setMonth);
				if(Integer.parseInt(setMonth)>12){
					isError=true;
				    errorInfo.append("月份不能大于12,");
				}
			} catch (Exception e) {
				isError=true;
			    errorInfo.append("月必须是数字,");
			}
			try {
				new Integer(setWeek);
			} catch (Exception e) {
				isError=true;
			    errorInfo.append("周必须是数字,");
			}
			if(year.length()!=4){
				isError=true;
			    errorInfo.append("年填写错误,");
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
			DateSetImportDao dao=DateSetImportDao.getInstance();
			RequestWrapper request=act.getRequest();
			String year=request.getParamValue("year");//取得目标年度
			if(null==year||"".equals(year)){
				throw new Exception("导入失败!");
			}
			//清空业务表中，该用户已导入未确认的年度目标
			TmDateSetPO clrPo=new TmDateSetPO();
			clrPo.setSetYear(year);
			dao.delete(clrPo);
			//查询要插入到业务表的临时表数据
			TmpDateSetPO conPo=new TmpDateSetPO();
			conPo.setSetYear(year);
			conPo.setUserId(logonUser.getUserId().toString());
			List<TmpDateSetPO> tmpList=dao.select(conPo);
			if(null==tmpList){
				tmpList=new ArrayList();
			}
			
			for(int i=0;i<tmpList.size();i++){
				TmpDateSetPO po=new TmpDateSetPO();
				po=tmpList.get(i);
				TmDateSetPO newPo=new TmDateSetPO();
				newPo.setSetYear(po.getSetYear());
				newPo.setSetMonth(po.getSetMonth());
				newPo.setSetWeek(po.getSetWeek());
				newPo.setSetDate(po.getSetDate());
				newPo.setCompanyId(logonUser.getCompanyId());
				dao.insert(newPo);
			}
			act.setOutData("year", year);
			act.setForword(dateSetImportCompleteUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
