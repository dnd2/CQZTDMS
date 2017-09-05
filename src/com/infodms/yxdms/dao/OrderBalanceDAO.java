package com.infodms.yxdms.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.reqquery.ReqQueryDao;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.XHBUtil;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


public class OrderBalanceDAO extends BaseDao {
	public static Logger logger = Logger.getLogger(OrderBalanceDAO.class);
	private static final OrderBalanceDAO dao = new OrderBalanceDAO();

	public static final OrderBalanceDAO getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Map<String,String>> getRoParts(String id){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select a.real_part_id, a.part_no, a.part_name, b.id, b.ro_no,a.REPAIRTYPECODE,a.PAY_TYPE,a.id repair_part_id\n");
		sbSql.append("  from Tt_As_Ro_Repair_Part a, tt_as_repair_order b\n");
		sbSql.append(" where a.ro_id = b.id\n");
		sbSql.append("   and b.id = ").append(id); 
		List<Map<String,String>> ps = this.pageQuery(sbSql.toString(), null, getFunName());
		return ps;
	}
	public boolean checkRoApplication(String id){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select t.ro_no\n");
		sbSql.append("  from tt_as_wr_application t\n");
		sbSql.append(" where t.ro_no = (select s.ro_no\n");
		sbSql.append("                    from tt_as_repair_order s\n");
		sbSql.append("                   where id=\n").append(id);
		sbSql.append("                  )"); 
		List<Map<String,String>> ps = this.pageQuery(sbSql.toString(), null, getFunName());
		if(ps!=null && ps.size()>0){
			return false;
		}
		return true;
	}
	public PageResult<Map<String, Object>> orderList(AclUserBean loginUser, RequestWrapper request,Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select o.id,o.ro_no,\n" );
		sb.append("       o.balance_yieldly,\n" );
		sb.append("       o.license,\n" );
		sb.append("       o.vin,\n" );
		sb.append("       o.model_name,\n" );
		sb.append("       o.owner_name,\n" );
		sb.append("       to_char(o.ro_create_date,'yyyy-mm-dd') as ro_create_date,\n" );
		sb.append("       o.in_mileage,\n" );
		sb.append("       o.free_times,\n" );
		sb.append("       o.ro_status,\n" );
		sb.append("       o.is_warning,\n" );
		sb.append("       o.balance_amount\n" );
		sb.append("  from Tt_As_Repair_Order o\n" );
		sb.append(" where 1 = 1\n" );
		sb.append("   and o.order_valuable_type = 13591001\n" );
		sb.append("   and o.dealer_id ="+loginUser.getDealerId());
		DaoFactory.getsql(sb, "o.ro_no", DaoFactory.getParam(request, "ro_no"), 2);
		DaoFactory.getsql(sb, "o.vin", DaoFactory.getParam(request, "vin"), 2);
		DaoFactory.getsql(sb, "o.ro_create_date", DaoFactory.getParam(request, "beginTime"), 3);
		DaoFactory.getsql(sb, "o.ro_create_date", DaoFactory.getParam(request, "endTime"), 4);
		DaoFactory.getsql(sb, "o.ro_status", DaoFactory.getParam(request, "ro_status"), 1);
		DaoFactory.getsql(sb, "o.is_warning", DaoFactory.getParam(request, "is_warning"), 1);
		DaoFactory.getsql(sb, "o.balance_yieldly", DaoFactory.getParam(request, "balance_yieldly"), 1);
		DaoFactory.getsql(sb, "o.license", DaoFactory.getParam(request, "license"), 2);
		sb.append("   order by o.create_date desc \n" );
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
}