/**********************************************************************
* <pre>
* FILE : BackChangeApplyDealerQueryDao.java
* CLASS : BackChangeApplyDealerQueryDao
* 
* AUTHOR : WangJinBao
*
* FUNCTION : 退换车申请书经销商端查询DAO.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-21| WangJinBao  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.BackChangeApplyMantainBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.FsFileuploadPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * Function       :  退换车申请书经销商端查询DAO
 * @author        :  wangjinbao
 * CreateDate     :  2010-05-21
 * @version       :  0.1
 */
public class BackChangeApplyDealerQueryDao extends BaseDao {
	public static Logger logger = Logger.getLogger(BackChangeApplyDealerQueryDao.class);
	private static final BackChangeApplyDealerQueryDao dao = new BackChangeApplyDealerQueryDao ();
	public static final BackChangeApplyDealerQueryDao getInstance() {
		return dao;
	}
	/**
	 * 退换车申请书经销商查询：
	 * @param pageSize          ：每页显示的条数
	 * @param curPage           ：当前页
	 * @param whereSql          ：SQL的查询条件  
	 * @param params            ：SQL的查询条件对应的参数
	 * @return                  
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> backChangeApplyQuery(int pageSize, int curPage, String whereSql,List<Object> params) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sb = new StringBuffer();
		sb.append(" select d.dealer_code,d.dealer_name,t.order_id,t.vin,g.group_name,t.ex_type,t.ex_date,t.ex_status ");
		sb.append(" from tt_if_exchange t left outer join tm_dealer d on t.dealer_id = d.dealer_id ");  
	    sb.append(" left outer join tm_vehicle v on t.vin = v.vin "); 
		sb.append(" left outer join tm_vhcl_material_group g on v.series_id = g.group_id  ");
		sb.append(" where t.is_del = 0 ");
		//modify by xiayanpeng begin 加入 工单类型<>待上报
		sb.append(" and t.ex_status<>"+Constant.MARKET_BACK_STATUS_UNREPORT);
		//modify by xiayanpeng end
		if(whereSql != null && !"".equals(whereSql.trim())){
			sb.append(whereSql);
		}
		
		sb.append(" order by ex_date desc");
		result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(), params, "com.infodms.dms.dao.feedbackMng.BackChangeApplyDealerQueryDao.backChangeApplyQuery", pageSize, curPage);
		return result;
	}
	
	/**
	 * 根据订单号查询退换车申请书明细；
	 * @param orderId               ： 订单号
	 * @return                           
	 */
	@SuppressWarnings("unchecked")
	public  BackChangeApplyMantainBean queryDetailByOrderId(String orderId) {
		StringBuilder sb = new StringBuilder();
		List<Object> params = new LinkedList<Object>();
		sb.append(" select t.id,t.order_id as order_id,d.dealer_code as dealer_code,d.dealer_name as dealer_name,");
		sb.append(" t.link_manager as link_manager,t.link_man as link_man,t.ex_type as ex_type,t.vin as vin,g.group_name as group_name, ");
		sb.append(" v.engine_no as engine_no,v.product_date as production_date,v.purchased_date as sell_time,");
		sb.append(" v.history_mile as mileage,tc.ctm_name as customer_name,tc.main_phone as curt_phone, ");
		sb.append(" tc.address as curt_address, t.problem_describe,t.user_request,t.advice_deal_mode,t.cost_detail from TT_IF_EXCHANGE t ");
		sb.append(" left outer join TT_IF_EXCHANGE_AUDIT ta on t.order_id = ta.order_id ");
		sb.append(" left outer join TM_DEALER d on t.dealer_id = d.dealer_id ");
		sb.append(" left outer join TC_USER u on ta.audit_by = u.user_id ");
		sb.append(" left outer join TM_VEHICLE v on t.vin = v.vin ");
		sb.append(" left outer join TT_DEALER_ACTUAL_SALES s on v.vehicle_id = s.vehicle_id ");
		sb.append(" left outer join tt_customer tc on tc.ctm_id=s.ctm_id ");
		sb.append(" left outer join tm_vhcl_material_group g on v.series_id = g.group_id  ");
		sb.append(" where  t.is_del = 0 ");
		if (orderId != null && !("").equals(orderId)){
			sb.append(" AND t.order_id = ? ");
			params.add(orderId);
		}
		BackChangeApplyMantainBean bcam = new BackChangeApplyMantainBean();
		PageResult<BackChangeApplyMantainBean> rs = pageQuery(BackChangeApplyMantainBean.class,sb.toString(), params,
				 10, 1);
		List<BackChangeApplyMantainBean> ls = rs.getRecords();
		if (ls!=null){
			if (ls.size()>0) {
				bcam = ls.get(0);
			}
		}
		return bcam;
	}
	/**
	 * 查询明细所需的审批信息：
	 * @param orderId     ：工单号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  List  getAuditInfoList(String orderId){
		StringBuilder sb = new StringBuilder();
		List<Object> params = new LinkedList<Object>();
		sb.append(" select t.order_id,to_char(ta.audit_date,'yyyy-mm-dd') audit_date,u.name as audit_by,ta.audit_status as ex_status,ta.audit_content,o.org_name as dept_name from TT_IF_EXCHANGE t ");
		sb.append(" left outer join TT_IF_EXCHANGE_AUDIT ta on t.order_id = ta.order_id ");
		sb.append(" left outer join TM_ORG o on ta.ORG_ID = o.ORG_ID");
		sb.append(" left outer join TC_USER u on ta.AUDIT_BY=u.USER_ID ");
		sb.append(" left outer join TM_COMPANY com on  u.COMPANY_ID =com.COMPANY_ID ");
		sb.append(" left outer join TC_CODE code on com.COMPANY_TYPE =code.CODE_ID ");
		sb.append("  where  t.is_del = 0 ");
		if (orderId != null && !("").equals(orderId)){
			sb.append(" AND t.ORDER_ID = ? ");
			params.add(orderId);
		}
		sb.append(" order by ta.audit_date desc");
		PageResult<BackChangeApplyMantainBean> rs = pageQuery(BackChangeApplyMantainBean.class,sb.toString(), params, 10, 1);
		List<BackChangeApplyMantainBean> list = rs.getRecords();
		return list;
	}
	/* （非 Javadoc）
	 * @see com.infodms.dms.dao.common.BaseDao#wrapperPO(java.sql.ResultSet, int)
	 */
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO 自动生成方法存根
		return null;
	}
	/**
     * Function：获得附件信息列表
     * @param  ：	
     * @return:		@param id
     * @return:		@return 
     * @throw：	
     * LastUpdate：	2010-7-15
     */
    public List<FsFileuploadPO> queryAttachFileInfo(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT A.* FROM FS_FILEUPLOAD A ");
		sql.append(" WHERE A.YWZJ='"+id+"'");
		List<FsFileuploadPO> ls= select (FsFileuploadPO.class,sql.toString(),null);
		return ls;
	}
}
