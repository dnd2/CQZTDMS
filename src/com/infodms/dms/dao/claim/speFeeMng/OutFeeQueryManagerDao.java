package com.infodms.dms.dao.claim.speFeeMng;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.CruiServiceDetailInfoBean;
import com.infodms.dms.bean.SpeFeeApproveLogListBean;
import com.infodms.dms.bean.SpeFeeQueryListBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class OutFeeQueryManagerDao extends BaseDao{
	
    public static Logger logger = Logger.getLogger(OutFeeQueryManagerDao.class);
	
	private static final OutFeeQueryManagerDao dao = null;
	
	public static final OutFeeQueryManagerDao getInstance() {
	   if(dao==null) return new OutFeeQueryManagerDao();
	   return dao;
	}
	/**
	 * Function：查询特殊费用列表
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@param curPage
	 * @return:		@param pageSize
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-7-19
	 */
	public PageResult<SpeFeeQueryListBean> getSpeFeeQueryListInfo(Map params,int curPage, int pageSize){
		String dealer_id=CommonUtils.checkNull(params.get("dealer_id"));
		String company_id=CommonUtils.checkNull(params.get("company_id"));
		String ts_order_no=CommonUtils.checkNull(params.get("ts_order_no"));
		String xh_order_no=CommonUtils.checkNull(params.get("xh_order_no"));
		String report_start_date=CommonUtils.checkNull(params.get("report_start_date"));
		String report_end_date=CommonUtils.checkNull(params.get("report_end_date"));
		String ord_status=CommonUtils.checkNull(params.get("ord_status"));
		
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select taws.id,taws.cr_id,'' dealer_code,'' dealer_name,taws.fee_no,tawc.cr_no,\n" );
		sqlStr.append("to_char(taws.make_date,'YYYY-MM-DD') make_date,tawc.cr_whither,tc.code_desc status_desc,\n" );
		sqlStr.append("taws.pass_fee+taws.traffic_fee+taws.quarter_fee+taws.eat_fee+taws.person_subside total_fee\n" );
		sqlStr.append("from tt_as_wr_speoutfee taws,tt_as_wr_cruise tawc,tc_code tc\n" );
		sqlStr.append("where taws.cr_id=tawc.id(+) and taws.status=tc.code_id\n");
		if(!"".equals(ts_order_no)){
			sqlStr.append("and taws.fee_no like'%"+ts_order_no+"%'\n" );
		}
		if(!"".equals(xh_order_no)){
			sqlStr.append("and tawc.cr_no like'%"+xh_order_no+"%'\n" );
		}
		if(!"".equals(report_start_date)){
			sqlStr.append(" and taws.make_date>=to_date('" + report_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		}
		if(!"".equals(report_end_date)){
			sqlStr.append(" and taws.make_date<=to_date('" + report_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		if(!"".equals(ord_status)){
			sqlStr.append("and taws.status="+ord_status+"\n" );
		}else{
			sqlStr.append("and taws.status<>"+Constant.SPE_OUTFEE_STATUS_01+"\n" );
		}
		sqlStr.append("and tawc.dealer_id="+dealer_id+"\n" );
		sqlStr.append("and tawc.company_id="+company_id+"\n");
		sqlStr.append("order by taws.id desc\n");
		PageResult<SpeFeeQueryListBean> pr=pageQuery(SpeFeeQueryListBean.class,
				sqlStr.toString(),null, pageSize, curPage);
		return pr;
	}
	/**
	 * Function：获得巡航服务路线明细信息
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@param curPage
	 * @return:		@param pageSize
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-7-13 赵伦达
	 */
	public CruiServiceDetailInfoBean getOutFeeDetailInfo(Map params){
		String ord_id=CommonUtils.checkNull(params.get("ord_id"));//工单id
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select tawc.id,td.dealer_code,td.dealer_shortname dealer_name,\n" );
		sqlStr.append("to_char(tawc.make_date,'YYYY-MM-DD') make_date,tawc.cr_whither,\n" );
		sqlStr.append("tawc.cr_mileage,tawc.cr_day,tawc.cr_principal,tawc.cr_phone,\n" );
		sqlStr.append("tawc.cr_cause\n" );
		sqlStr.append("from TT_AS_WR_CRUISE tawc,tm_dealer td\n" );
		sqlStr.append("where tawc.dealer_id=td.dealer_id\n" );
		sqlStr.append("and tawc.id="+ord_id);

		List<CruiServiceDetailInfoBean> list=select(CruiServiceDetailInfoBean.class, sqlStr.toString(),null);
		if(list!=null&&list.size()>0){
			return (CruiServiceDetailInfoBean)list.get(0);
		}else{
			return null;
		}
	}
	/**
     * Function：获得审核意见日志
     * @param  ：	
     * @return:		@param order_id
     * @return:		@return 
     * @throw：	
     * LastUpdate：	2010-7-20 赵伦达
     */
    public List<SpeFeeApproveLogListBean> getApproveLogList(String order_id){
    	if(order_id==null||"".equals(order_id)) return null;
    	StringBuffer sqlStr= new StringBuffer();
    	sqlStr.append("select to_char(tawsa.auditing_date,'YYYY-MM-DD') auditing_date,\n" );
    	sqlStr.append("tu.name user_name,tmo.org_name dept_name,tc.code_desc audit_status,tawsa.auditing_opinion\n" );
    	sqlStr.append("from tt_as_wr_speoutfee_auditing tawsa,tc_user tu,tm_org tmo,tc_code tc\n" );
    	sqlStr.append("where tawsa.auditing_person=tu.user_id and tawsa.preson_dept=tmo.org_id\n" );
    	sqlStr.append("and tawsa.status=tc.code_id\n" );
    	sqlStr.append("and tawsa.fee_id=(select id from tt_as_wr_speoutfee where cr_id="+order_id+")\n" );
    	sqlStr.append("order by tawsa.id desc");
    	
    	List<SpeFeeApproveLogListBean> list=select(SpeFeeApproveLogListBean.class, sqlStr.toString(), null);
    	return list;
    }
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
}
