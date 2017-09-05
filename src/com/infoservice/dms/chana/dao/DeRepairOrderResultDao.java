package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infoservice.dms.chana.vo.ActivityVO;
import com.infoservice.dms.chana.vo.BaseVO;
import com.infoservice.dms.chana.vo.QueryRepairVO;

public class DeRepairOrderResultDao extends BaseDao<TtAsRepairOrderPO> {
	public static Logger logger = Logger.getLogger(DeRepairOrderResultDao.class);
	private static final DeRepairOrderResultDao dao = new DeRepairOrderResultDao ();
	
	public static final DeRepairOrderResultDao getInstance() {
		return dao;
	}
	
	protected BaseVO wrapperVO(ResultSet rs, int idx, int flag) {
		return null;
	}
	@Override
	protected TtAsRepairOrderPO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	* @Title: queryRepairOrder 
	* @Description: TODO(根据VIN和工单开始时间和结束时间查询 经销商) 
	* @param @param vo vin beginDate endDate entityCode
	* @return List<TtAsRepairOrderPO>    返回类型 
	* @throws
	 */
	public List<TtAsRepairOrderPO> queryRepairOrder(QueryRepairVO vo) {
		StringBuilder str = new StringBuilder();
		str.append("SELECT * FROM TT_AS_REPAIR_ORDER \n");
		str.append("WHERE VIN = '").append(vo.getVin()).append("' \n");
/*		if(Utility.testString(vo.getEntityCode())){
			str.append("AND DEALER_CODE = '").append(vo.getEntityCode()).append("' \n");
		}*/
		if (Utility.testString(vo.getBeginDate())) {
			str.append(" AND RO_CREATE_DATE >= TO_DATE('").append(vo.getBeginDate().trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(vo.getEndDate())) {
			str.append(" AND RO_CREATE_DATE <= TO_DATE('").append(vo.getEndDate().trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		List<TtAsRepairOrderPO> pos = select(TtAsRepairOrderPO.class, str.toString(), null);
		return pos;
	}

	
	
}
