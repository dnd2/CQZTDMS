/**
 * @Title: ProductManageDao.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-1
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.dao.sales.marketmanage.activity;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TmVhclMaterialGroupRPO;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.po.TmpVsPriceDtlPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 * 
 */
public class ActivityMakerDao extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(ActivityMakerDao.class);
	private static final ActivityMakerDao dao = new ActivityMakerDao();

	public static final ActivityMakerDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 物料组维护查询列表
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getActivityMakerList(
			Map<String, String> map, int curPage, int pageSize) {

		String makerCode = map.get("makerCode");
		String makerName =  map.get("makerName");
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TM.MAKER_ID, TM.MAKER_CODE, TM.CREATE_DATE, TM.MAKER_NAME,\n" );
		sql.append(" TM.PHONE, TM.LINK_MAN,  TM.CREATE_BY, TM.STATUS\n" );
		sql.append("  FROM TM_MARKET_MAKER TM WHERE 1=1 ");

		if (null!=makerCode&&!makerCode.equals("")) {
			sql.append("   AND TM.MAKER_CODE LIKE '%" + makerCode + "%'\n");
		}
		if (null!=makerName&&!makerName.equals("")) {
			sql.append("   AND TM.MAKER_NAME LIKE '%" + makerName + "%'\n");
		}

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				dao.getFunName(),
				pageSize, curPage);
		return ps;
	}

	
}
