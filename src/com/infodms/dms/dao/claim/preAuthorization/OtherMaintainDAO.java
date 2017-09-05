package com.infodms.dms.dao.claim.preAuthorization;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrForeapprovalotheritemExtPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class OtherMaintainDAO extends BaseDao{
	
	public static Logger logger = Logger.getLogger(OtherMaintainDAO.class);
    private static final OtherMaintainDAO dao = null;
	
	public static final OtherMaintainDAO getInstance() {
	   if(dao==null) return new OtherMaintainDAO();
	   return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 
	* @Title: queryLabour 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param map
	* @param @param pageSize
	* @param @param curPage
	* @param @return    设定文件 
	* @return PageResult<TtAsWrForeapprovallabExtPO>    返回类型 
	* @throws
	 */
	public PageResult<TtAsWrForeapprovalotheritemExtPO> otherQuery(Long oemCompanyId ,Map<String,String> map,int pageSize,int curPage) {
		StringBuffer sqlStr=new StringBuffer();
		List params = new LinkedList();
		sqlStr.append("select t.*,g.FEE_NAME AS fee_name from TT_AS_WR_FOREAPPROVALOTHERITEM t \n");
		sqlStr.append(" left outer join TT_AS_WR_OTHERFEE g on g.FEE_CODE=t.ITEM_CODE  AND  G.is_del = 0 \n");
		sqlStr.append(" where t.oem_company_id="+oemCompanyId+" ");
		if (Utility.testString(map.get("ITEM_CODE"))) {
			sqlStr.append(" and t.ITEM_CODE = ? ");
			params.add(map.get("ITEM_CODE"));
		}
		if (Utility.testString(map.get("ITEM_DESC"))) {
			sqlStr.append(" and t.ITEM_DESC like ? ");
			params.add("%"+map.get("ITEM_DESC")+"%");
		}
		sqlStr.append(" ORDER BY t.id DESC ");
		PageResult<TtAsWrForeapprovalotheritemExtPO> ps = pageQuery(TtAsWrForeapprovalotheritemExtPO.class,sqlStr.toString(),params,pageSize,curPage);
		return ps;
	}

}
