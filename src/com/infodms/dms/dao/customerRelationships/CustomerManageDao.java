package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.TtRelationBean;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtCrManagePO;
import com.infodms.dms.po.TtCustomerPO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class CustomerManageDao extends BaseDao{
	
	private static final CustomerManageDao dao = new CustomerManageDao();
	
	public static final CustomerManageDao getInstance() {
		return dao;
	}

	@Override
	protected TtCrManagePO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public PageResult<Map<String,Object>> queryComplaintDisposal(String sql,int pageSize,int curPage){
		
		return (PageResult<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}

	public TtCustomerPO queryAnaById(TtCustomerPO po){
		List<TtCustomerPO> lists = this.select(po);
		if(lists.size()>0) return lists.get(0);
		return null ;
	}
	
	public TtRelationBean getBean(String sql){
		List<TtRelationBean> list = pageQuery(TtRelationBean.class,sql,null,5,1).getRecords();
		if(list.size()>0)return list.get(0);
		else return null ;
	}

	/**
	 * 查询客户信息明细
	 * @param ctmId
	 * @return
	 */
	public List<Map<String,Object>> queryCustomerDetail(Long ctmId){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT A.CTM_ID,A.CARD_NUM,A.CTM_TYPE,A.CARD_TYPE,A.MAIN_PHONE,A.SEX,A.GUEST_STARS,A.CTM_NAME,\n" );
		sql.append("D.VIN,C.MATERIAL_CODE,C.MATERIAL_ID,C.MATERIAL_NAME,TO_CHAR(D.PURCHASED_DATE,'YYYY-MM-DD') PURCHASED_DATE,\n" );
		sql.append("D.YIELDLY,E.MODEL_ID,E.MODEL_CODE,E.MODEL_NAME,E.SERIES_ID,E.SERIES_CODE,\n" );
		sql.append("E.SERIES_NAME\n" );
		sql.append("  FROM TT_CUSTOMER            A,\n" );
		sql.append("       TT_DEALER_ACTUAL_SALES B,\n" );
		sql.append("       TM_VHCL_MATERIAL       C,\n" );
		sql.append("       TM_VEHICLE             D,\n" );
		sql.append("       vw_material_group      E\n" );
		sql.append(" WHERE A.CTM_ID = B.CTM_ID\n" );
		sql.append("   AND B.VEHICLE_ID = D.VEHICLE_ID\n" );
		sql.append("   AND D.MATERIAL_ID = C.MATERIAL_ID\n" );
		sql.append("   AND E.PACKAGE_ID = D.PACKAGE_ID\n" );
		sql.append("   AND A.CTM_ID = ?");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(ctmId);
		
		return this.pageQuery(sql.toString(), paramList, this.getFunName());
	}
}