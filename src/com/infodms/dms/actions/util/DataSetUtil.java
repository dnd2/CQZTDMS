package com.infodms.dms.actions.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.common.DataSetDAO;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;

/**
 * 公共方法，获取TmDataSet的年份、月份和周度
 * @author HXY
 * @update 2013-1-30
 * */
public class DataSetUtil {

	private static DataSetDAO dao = DataSetDAO.getInstance();
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	public Logger logger = Logger.getLogger(DataSetUtil.class);
	/**
	 * 获取TM_DATA_SET表中所有月份
	 * @param year 选择的年份
	 * @return 月份列表
	 * */
	public List<String> getAllYear() {
		return dao.getAllYear();
	}
	
	/**
	 * 获取TM_DATA_SET表中所有月份
	 * @param year 选择的年份
	 * @return 月份列表
	 * */
	public List<String> getAllMonthByYear(String year) {
		return dao.getAllMonthByYear(year);
	}
	
	/**
	 * 获取TM_DATA_SET表中所有周度
	 * @param year 选择的年份
	 * @return 周度列表
	 * */
	public List<String> getAllWeekByYear(String year) {
		return dao.getAllWeekByYear(year);
	}
	
	
	/**
	 * 级联获取月份
	 * @param year
	 * */
	public void getCascadeMonthsOrWeeks() {
		try {
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			String year = act.getRequest().getParamValue("year");
			String id = act.getRequest().getParamValue("id");
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				if(year != null && !"".equals(year)) {
					act.setOutData("weeks", getAllWeekByYear(year));
				}
			} else if(Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				if(year != null && !"".equals(year)) {
					act.setOutData("monthList", getAllMonthByYear(year));
				}
			}
			act.setOutData("para", para.toUpperCase());
			act.setOutData("id", id);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"获取月份或周度失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}
