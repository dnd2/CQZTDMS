package com.infodms.dms.dao.report.feedbackmngreport;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.MarketBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDealerOrgRelationPO;
import com.infodms.dms.po.TtAsWrInformationqualityPO;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 市场处理工单处理汇总报表数据库控制
 * @author 
 *
 */
@SuppressWarnings("unchecked")
public class WorkSummaryDao extends BaseDao {
	
	public static Logger logger = Logger.getLogger(WorkSummaryDao.class);
	private static final WorkSummaryDao dao = new WorkSummaryDao();

	public static final WorkSummaryDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 市场处理工单汇总查询
	 * @param startDate
	 * @param endDate
	 * @param orgId
	 * @param dealerId
	 * @param status
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String,Object>> query(String startDate,String endDate,String orgId,String dealerId,String status,int pageSize,int curPage){
		StringBuffer sql= new StringBuffer("");		
		sql.append("select (select org.org_name\n" );
		sql.append("          from TM_ORG org\n" ); 
		sql.append("          join tm_dealer_org_relation dor on dor.org_id = org.org_id\n" ); 
		sql.append("         where dor.dealer_id = im.dealer_id) as ORG_NAME,\n" ); 
		sql.append("       (select d.dealer_name\n" ); 
		sql.append("          from tm_dealer d\n" ); 
		sql.append("         where d.dealer_id = im.dealer_id) as DEALER_NAME,\n" ); 
		sql.append("       im.order_date,\n" ); 
		sql.append("       (select vm.MODEL_CODE\n" ); 
		sql.append("          from tm_vhcl_material_group vm\n" ); 
		sql.append("          join TM_VEHICLE v on vm.group_id = v.model_id\n" ); 
		sql.append("         where v.vin = im.vin) as MODEL_CODE,\n" ); 
		sql.append("       im.VIN,\n" ); 
		sql.append("       (select ct.ctm_name\n" );
	    sql.append("          from tt_customer ct, TT_DEALER_ACTUAL_SALES das\n" ); 
		sql.append("         where das.dealer_id = im.dealer_id\n" );
		sql.append("           and ct.ctm_id = das.ctm_id) as CTM_NAME," );
		sql.append("       im.COMP_TYPE,\n" ); 
		sql.append("       im.MONEY,\n" ); 
		sql.append("       (select qyu.name\n" ); 
		sql.append("          from TT_IF_MARKET_AUDIT qy, tc_user qyu\n" ); 
		sql.append("         where qy.order_id = im.order_id\n" ); 
		sql.append("           and qyu.user_id = qy.audit_by\n" ); 
		sql.append("           and qy.audit_status = '"+Constant.MARKET_BACK_STATUS_AREA_PASS+"'\n" ); 
		sql.append("           and qy.audit_date =\n" ); 
	    sql.append("               (select max(iqy.audit_date)\n" ); 
		sql.append("                  from TT_IF_MARKET_AUDIT iqy\n" ); 
		sql.append("                 where iqy.order_id = im.order_id\n" ); 
		sql.append("                   and iqy.audit_status = '"+Constant.MARKET_BACK_STATUS_AREA_PASS+"')) as AUDIT_BY_QY,\n" ); 
		sql.append("       (select dqu.name\n" ); 
		sql.append("          from TT_IF_MARKET_AUDIT dq, tc_user dqu\n" ); 
		sql.append("         where dq.order_id = im.order_id\n" ); 
		sql.append("           and dqu.user_id = dq.audit_by\n" ); 
		sql.append("           and dq.audit_status = '"+Constant.MARKET_BACK_STATUS_AREA_DIRECTOR_PASS+"'\n" ); 
		sql.append("           and dq.audit_date =\n" ); 
	    sql.append("               (select max(idq.audit_date)\n" ); 
		sql.append("                  from TT_IF_MARKET_AUDIT idq\n" ); 
		sql.append("                 where idq.order_id = im.order_id\n" ); 
		sql.append("                   and idq.audit_status = '"+Constant.MARKET_BACK_STATUS_AREA_DIRECTOR_PASS+"')) as AUDIT_BY_DQ,\n" ); 
		sql.append("       (select kfu.name\n" ); 
		sql.append("          from TT_IF_MARKET_AUDIT kf, tc_user kfu\n" ); 
		sql.append("         where kf.order_id = im.order_id\n" ); 
		sql.append("           and kfu.user_id = kf.audit_by\n" ); 
		sql.append("           and kf.audit_status = '"+Constant.MARKET_BACK_STATUS_TECH_PASS+"'\n" ); 
		sql.append("           and kf.audit_date =\n" ); 
		sql.append("               (select max(ikf.audit_date)\n" ); 
		sql.append("                  from TT_IF_MARKET_AUDIT ikf\n" ); 
		sql.append("                 where ikf.order_id = im.order_id\n" ); 
		sql.append("                   and ikf.audit_status = '"+Constant.MARKET_BACK_STATUS_TECH_PASS+"')) as AUDIT_BY_KF\n" ); 
		sql.append("  from TT_IF_MARKET im where 1=1\n");
		if(StringUtil.notNull(startDate)){
			sql.append("and im.order_date>=to_date('"+startDate+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n");
		}
		if(StringUtil.notNull(endDate)){
			sql.append("and im.order_date<=to_date('"+endDate+" 23:59:59','yyyy-mm-dd hh24:mi:ss')\n");
		}	
		if(StringUtil.notNull(dealerId)){
			sql.append("and im.dealer_id='"+dealerId+"'\n");
		}
		if(StringUtil.notNull(status)){
			sql.append("and im.status='"+status+"'\n");
		}
		if(StringUtil.notNull(orgId)){
			String dealerIds=getDealerByOrg(orgId);
			if(StringUtil.notNull(dealerIds)){
				sql.append("and im.dealer_id in ("+dealerIds+")\n");				
				return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage) ;
			}	
			else return null;
		}
		else
		{
			return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage) ;
		}
	}
	
	/**
	 * 报表导出
	 * @param startDate
	 * @param endDate
	 * @param orgId
	 * @param dealerId
	 * @param status
	 * @return
	 */
	public List<MarketBean> download(String startDate,String endDate,String orgId,String dealerId,String status){
		StringBuffer sql= new StringBuffer("");		
		sql.append("select (select org.org_name\n" );
		sql.append("          from TM_ORG org\n" ); 
		sql.append("          join tm_dealer_org_relation dor on dor.org_id = org.org_id\n" ); 
		sql.append("         where dor.dealer_id = im.dealer_id) as ORG_NAME,\n" ); 
		sql.append("       (select d.dealer_name\n" ); 
		sql.append("          from tm_dealer d\n" ); 
		sql.append("         where d.dealer_id = im.dealer_id) as DEALER_NAME,\n" ); 
		sql.append("       im.order_date,\n" ); 
		sql.append("       (select vm.MODEL_CODE\n" ); 
		sql.append("          from tm_vhcl_material_group vm\n" ); 
		sql.append("          join TM_VEHICLE v on vm.group_id = v.model_id\n" ); 
		sql.append("         where v.vin = im.vin) as MODEL_CODE,\n" ); 
		sql.append("       im.VIN,\n" ); 
		sql.append("       (select ct.ctm_name\n" );
	    sql.append("          from tt_customer ct, TT_DEALER_ACTUAL_SALES das\n" ); 
		sql.append("         where das.dealer_id = im.dealer_id\n" );
		sql.append("           and ct.ctm_id = das.ctm_id) as CTM_NAME," );
		sql.append("       im.COMP_TYPE,\n" ); 
		sql.append("       im.MONEY,\n" ); 
		sql.append("       (select qyu.name\n" ); 
		sql.append("          from TT_IF_MARKET_AUDIT qy, tc_user qyu\n" ); 
		sql.append("         where qy.order_id = im.order_id\n" ); 
		sql.append("           and qyu.user_id = qy.audit_by\n" ); 
		sql.append("           and qy.audit_status = '"+Constant.MARKET_BACK_STATUS_AREA_PASS+"'\n" ); 
		sql.append("           and qy.audit_date =\n" ); 
	    sql.append("               (select max(iqy.audit_date)\n" ); 
		sql.append("                  from TT_IF_MARKET_AUDIT iqy\n" ); 
		sql.append("                 where iqy.order_id = im.order_id\n" ); 
		sql.append("                   and iqy.audit_status = '"+Constant.MARKET_BACK_STATUS_AREA_PASS+"')) as AUDIT_BY_QY,\n" ); 
		sql.append("       (select dqu.name\n" ); 
		sql.append("          from TT_IF_MARKET_AUDIT dq, tc_user dqu\n" ); 
		sql.append("         where dq.order_id = im.order_id\n" ); 
		sql.append("           and dqu.user_id = dq.audit_by\n" ); 
		sql.append("           and dq.audit_status = '"+Constant.MARKET_BACK_STATUS_AREA_DIRECTOR_PASS+"'\n" ); 
		sql.append("           and dq.audit_date =\n" ); 
	    sql.append("               (select max(idq.audit_date)\n" ); 
		sql.append("                  from TT_IF_MARKET_AUDIT idq\n" ); 
		sql.append("                 where idq.order_id = im.order_id\n" ); 
		sql.append("                   and idq.audit_status = '"+Constant.MARKET_BACK_STATUS_AREA_DIRECTOR_PASS+"')) as AUDIT_BY_DQ,\n" ); 
		sql.append("       (select kfu.name\n" ); 
		sql.append("          from TT_IF_MARKET_AUDIT kf, tc_user kfu\n" ); 
		sql.append("         where kf.order_id = im.order_id\n" ); 
		sql.append("           and kfu.user_id = kf.audit_by\n" ); 
		sql.append("           and kf.audit_status = '"+Constant.MARKET_BACK_STATUS_TECH_PASS+"'\n" ); 
		sql.append("           and kf.audit_date =\n" ); 
		sql.append("               (select max(ikf.audit_date)\n" ); 
		sql.append("                  from TT_IF_MARKET_AUDIT ikf\n" ); 
		sql.append("                 where ikf.order_id = im.order_id\n" ); 
		sql.append("                   and ikf.audit_status = '"+Constant.MARKET_BACK_STATUS_TECH_PASS+"')) as AUDIT_BY_KF\n" ); 
		sql.append("  from TT_IF_MARKET im where 1=1\n");
		if(StringUtil.notNull(startDate)){
			sql.append("and im.order_date>=to_date('"+startDate+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n");
		}
		if(StringUtil.notNull(endDate)){
			sql.append("and im.order_date<=to_date('"+endDate+" 23:59:59','yyyy-mm-dd hh24:mi:ss')\n");
		}	
		if(StringUtil.notNull(dealerId)){
			sql.append("and im.dealer_id='"+dealerId+"'\n");
		}
		if(StringUtil.notNull(status)){
			sql.append("and im.status='"+status+"'\n");
		}
		if(StringUtil.notNull(orgId)){
			String dealerIds=getDealerByOrg(orgId);
			if(StringUtil.notNull(dealerIds)){
				sql.append("and im.dealer_id in ("+dealerIds+")\n");				
				return this.select(MarketBean.class, sql.toString(), null);
				
			}	
			else return null;
		}
		else
		{
			return this.select(MarketBean.class, sql.toString(), null);			
		}
	}
	
	/**
	 * 通过区域Id获得经销商的数组字符
	 * @param orgId
	 * @return
	 */
	public String getDealerByOrg(String orgId)
	{		
		String sql= "select dealer_id from tm_dealer_org_relation where org_id=?";
		List<Object> params = new LinkedList<Object>();
		params.add(orgId);
		List<TmDealerOrgRelationPO> list = this.select(TmDealerOrgRelationPO.class, sql.toString(), params);
		if(list!=null)
		{
			if(list.size()>0)
			{
				String str="";
				for(int i=0;i<list.size();i++)
				{
					if(i==0)str+=String.valueOf(list.get(i).getDealerId());
					else str=str+","+String.valueOf(list.get(i).getDealerId());
				}
				return str;
			}
			else return null;
		}
		else return null;
	}

}