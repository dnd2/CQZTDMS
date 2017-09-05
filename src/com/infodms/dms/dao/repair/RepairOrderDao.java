package com.infodms.dms.dao.repair;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.po3.bean.PO;


public class RepairOrderDao extends BaseDao {
	
	public static Logger logger = Logger.getLogger(RepairOrderDao.class);
	private static final RepairOrderDao dao = new RepairOrderDao();
	//工单子表集合
	private final String[] tables = {"TT_AS_RO_ADD_ITEM", "TT_AS_RO_LABOUR", "TT_AS_RO_REPAIR_PART", "TT_AS_RO_MANAGE"};
	public static final RepairOrderDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		TtAsRepairOrderPO ttAsRepairOrderPO = new TtAsRepairOrderPO();
		try {
			ttAsRepairOrderPO.setId(rs.getLong("ID"));
		} catch (Exception e) {
			throw new RpcException(e);
		}
		return null;
	}
	
	
	/**
	 * 
	* @Title: delCascade 
	* @Description: TODO(先根据dealer_code和ro_no删除工单主表,再根据工单主表ID删除相关联子表) 
	* @param @param po    
	* @return void    返回类型 
	* @throws
	 */
	public void delCascade(TtAsRepairOrderPO po) {
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM TT_AS_REPAIR_ORDER\n");
		sql.append("WHERE DEALER_CODE = '").append(po.getDealerCode()).append("'\n");
		sql.append("  AND RO_NO = '").append(po.getRoNo()).append("'\n");
		int count = delete(sql.toString(), null);
		logger.warn("根据DEALER_CODE和RO_NO删除工单表 " + po.getDealerCode() + ", " + po.getRoNo() + ". 删除了" + count + "条");
		if (count > 0) {
			delCasadeTable(po);
		}
	}
	/**
	 * 
	* @Title: delCasadeTable 
	* @Description: TODO(级联删除相关联子表) 
	* @param @param po    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void delCasadeTable(TtAsRepairOrderPO po) {
		for (String table : tables) {
			StringBuffer sql = new StringBuffer();
			sql.append("DELETE FROM " + table + "\n");
			sql.append("WHERE RO_ID = ").append(po.getId());
			delete(sql.toString(), null);
		}
	}
	/**
	 * 
	* @Title: queryUnSendRepair 
	* @Description: TODO(查询未下发的工单) 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List<TtAsRepairOrderPO> queryUnSendRepair() {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT * FROM TT_AS_REPAIR_ORDER\n" );
		sql.append(" WHERE NVL(IF_STATUS, 0) = 0");
		List<TtAsRepairOrderPO> pos = select(TtAsRepairOrderPO.class, sql.toString(), null);
		return pos;
	}
}
