package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfServicecarExtPO;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ServiceCarInfoApproveDao extends BaseDao {
	private ServiceCarInfoApproveDao(){
	}
	public static ServiceCarInfoApproveDao getInstance(){
		return new ServiceCarInfoApproveDao();
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * 主页面第一次查询
	 */
	@SuppressWarnings("unchecked")
	public PageResult<TtIfServicecarExtPO> getServiceCarInfo(String con,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select t.* ,d.dealer_code as dealer_code,t.app_status as audit_status,d.dealer_name as dealer_name,g.group_code as model_name\n");
		sql.append("from TT_IF_SERVICECAR t, tm_vhcl_material_group g,tm_dealer d,tm_vhcl_material_group_r r\n");
		sql.append("where  1=1\n");
		sql.append("and is_del=0\n");
		sql.append("and t.group_id=r.material_id\n");
		sql.append("and r.group_id=g.group_id\n");
		sql.append("and t.dealer_id=d.dealer_id\n");
		if(StringUtil.notNull(con))
			sql.append(con);
		
		return pageQuery(TtIfServicecarExtPO.class,sql.toString(), null,
				 pageSize, curPage);
	}

}
