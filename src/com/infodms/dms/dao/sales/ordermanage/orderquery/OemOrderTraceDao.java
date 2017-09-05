package com.infodms.dms.dao.sales.ordermanage.orderquery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmDealerPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.exceptions.DAOException;

public class OemOrderTraceDao extends BaseDao<PO>{
	public static Logger logger = Logger.getLogger(OrderReportDao.class);
	private static final OemOrderTraceDao dao = new OemOrderTraceDao();

	public static final OemOrderTraceDao getInstance() {
		return dao;
	}

	// 查看日期配置表中当天的记录
	public TmDateSetPO getTmDateSetPO(Date date, Long companyId) {
		SimpleDateFormat formate = new SimpleDateFormat("yyyyMMdd");
		String dayStr = formate.format(date);
		// 查看日期配置表中当天的记录
		TmDateSetPO dateSet = new TmDateSetPO();
		dateSet.setSetDate(dayStr);
		dateSet.setCompanyId(companyId);
		List<PO> list = select(dateSet);
		return list.size() != 0 ? (TmDateSetPO) list.get(0) : null;
	}

	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 年列表
	 * 
	 * @return
	 */
	public List<String> getYearList() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		List<String> list = new ArrayList<String>();
		list.add(Integer.toString(year - 1));
		list.add(Integer.toString(year));
		list.add(Integer.toString(year + 1));
		return list;
	}

	/**
	 * 月列表
	 * 
	 * @return
	 */
	public List<String> getMonthList() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 12; i++) {
			list.add(Integer.toString(i + 1));
		}
		return list;
	}

	/**
	 * 周列表
	 * 
	 * @return
	 */
	public List<String> getWeekList() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 53; i++) {
			list.add(Integer.toString(i + 1));
		}
		return list;
	}
	/**
	 * 获得经销商信息
	 * 
	 * @return
	 */
	public PageResult<Map<String, Object>> getDealerInfoByCode(String dealerCode,final int pageSize,final int curPage){
		List<Object> parList = new ArrayList<Object>();
		//设置查询SQL
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT D.DEALER_ID,D.DEALER_CODE,D.DEALER_NAME,D.DEALER_TYPE,D.DEALER_ORG_ID,D.OEM_COMPANY_ID,D.COMPANY_ID FROM TM_DEALER D ");
		sql.append(" WHERE D.DEALER_CODE = ? ");
		//设置SQL所需参数
		parList.add(dealerCode);
		//返回经销商信息列表
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), parList, getFunName(),pageSize, curPage);
		return ps;
		
	}
}
