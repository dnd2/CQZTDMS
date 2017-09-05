package com.infodms.dms.dao.sales.storageManage;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class StoreQueryDAO extends BaseDao {
	public static Logger logger = Logger.getLogger(StorageQueryDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static final CheckVehicleDAO dao = new CheckVehicleDAO ();
	/*
	 * 返回加载数据
	 */
	public  static PageResult <Map<String,Object>>getStoreList(String dealerId,String lowDelId,Long dealerCompanyId,String warehouseName, String warehouseType, String warehouseStatus,int curPage, int pageSize) {
		StringBuffer sql=new StringBuffer("select rownum as NUM,tdw.WAREHOUSE_ID,tdw.WAREHOUSE_NAME,tdw.WAREHOUSE_TYPE,tdw.STATUS,tdw.REMARK from tm_dealer_warehouse tdw where 1=1  and  tdw.dealer_comany_id='"+dealerCompanyId+"'");
		if(warehouseName!=""&&warehouseName!=null){
		sql.append(" and tdw.warehouse_name like '%"+warehouseName+"%'\n");
		}
		if(warehouseType!=""&&warehouseType!=null){
		sql.append(" and tdw.warehouse_type='"+warehouseType+"'\n");
		}
		if(warehouseStatus!=""&&warehouseStatus!=null){
		sql.append(" and tdw.status='"+warehouseStatus+"'\n");
		}
		if(dealerId != null && !dealerId.equals("")) {
			sql.append(" and tdw.dealer_id='"+dealerId+"'\n");
		}
		if(lowDelId != null && !lowDelId.equals("") && !Constant.DEALER_WAREHOUSE_TYPE_01.toString().equals(warehouseType)) {
			sql.append(" and tdw.manage_dealer_id='"+lowDelId+"'\n");
		}
		return dao.pageQuery(sql.toString(),null,"com.infodms.dms.dao.sales.storageManage.StoreQueryDAO.getStoreList", pageSize, curPage);
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
