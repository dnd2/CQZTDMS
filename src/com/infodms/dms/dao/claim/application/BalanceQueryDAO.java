package com.infodms.dms.dao.claim.application;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrBalanceExtPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
* @ClassName: BalanceQueryDAO 
* @Description: TODO(结算清单查询) 
* @author wangchao 
* @date Jun 18, 2010 9:26:35 AM 
*
 */
public class BalanceQueryDAO extends BaseDao{
	
	public static Logger logger = Logger.getLogger(BalanceQueryDAO.class);
	private static final BalanceQueryDAO dao = new BalanceQueryDAO();

	public static final BalanceQueryDAO getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	* @Title: balanceQuery 
	* @Description: TODO(查询结算清单) 
	* @param @param map
	* @param @param pageSize
	* @param @param curPage
	* @param @return    设定文件 
	* @return PageResult<TtAsWrBalanceExtPO>    返回类型 
	* @throws
	 */
	public PageResult<TtAsWrBalanceExtPO> balanceQuery(Map<String,String> map,int pageSize,int curPage) {
		StringBuffer sql = new StringBuffer();
		List params = new LinkedList();
		sql.append("select t.*,d.dealer_name as dealer_name,d.dealer_code as dealer_code,d.dealer_shortname as dealer_shortname from TT_AS_WR_BALANCE t ");
		sql.append(" left outer join tm_dealer d on t.dealer_id=d.dealer_id ");
		sql.append(" where 1=1 ");
		if (Utility.testString(map.get("dealerCode"))) {
			sql.append(Utility.getConSqlByParamForEqual(map.get("dealerCode"), params, "d", "dealer_code"));
		}
		if (Utility.testString(map.get("dealerId"))) {
			sql.append(" and d.dealer_id = '"+map.get("dealerId")+"' ");
		}
		if (Utility.testString(map.get("dealerName"))) {
			sql.append(" and d.dealer_name like '%"+map.get("dealerName")+"%' ");
		}
		if (Utility.testString(map.get("yieldly"))) {
			sql.append(" and t.proc_factory = '"+map.get("yieldly")+"' ");
		}
		if (Utility.testString(map.get("reduction"))) {
			sql.append(" and t.reduction_flag =1 ");
		}else {
			sql.append(" and (t.reduction_flag IS  NULL OR T.REDUCTION_FLAG <> 1) ");
		}
		if (Utility.testString(map.get("balanceNo"))) {
			sql.append(" and t.balance_no like '%"+map.get("balanceNo")+"%' ");
		}
		//if (Utility.testString(map.get("balanceDateStr"))) {
		//	sql.append(" and t.balance_date >= to_date('"+map.get("balanceDateStr")+" 00:00:00"+"', 'yyyy-mm-dd hh24:mi:ss') ");
		//}
		if (Utility.testString(map.get("balanceDateEnd"))) {
			sql.append(" and t.balance_date <= to_date('"+map.get("balanceDateEnd")+" 23:59:59"+"', 'yyyy-mm-dd hh24:mi:ss') ");
		}
		sql.append(" order by t.id desc ");
		PageResult<TtAsWrBalanceExtPO> ps = pageQuery(TtAsWrBalanceExtPO.class, sql.toString(), params, pageSize, curPage);
		return ps;
	}
	
	
}
