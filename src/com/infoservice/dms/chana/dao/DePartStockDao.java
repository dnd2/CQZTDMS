package com.infoservice.dms.chana.dao;

import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.po.TtPtDlrstockPO;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.vo.PartStockVO;

/**
 * @Title: DePartStockDao.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-27
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class DePartStockDao extends AbstractIFDao {
	public static Logger logger = Logger.getLogger(DePartStockDao.class);
	private static final DePartStockDao dao = new DePartStockDao ();
	
	public static final DePartStockDao getInstance() {
		return dao;
	}
	
	public Integer getCount(TtPtDlrstockPO po){
		Integer i = 0;
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT COUNT(*) COUNT\n" );
		sql.append("FROM TT_PT_DLRSTOCK\n" );
		sql.append("WHERE DEALER_ID = ").append(po.getDealerId()).append("\n");
		sql.append("AND PART_ID = ").append(po.getPartId()).append("\n");

		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		
		i = Integer.parseInt(String.valueOf(map.get("COUNT")));
		return i;
	}
	
	public String getDelaerId(PartStockVO vo){
		String delaerId = null;
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DEALER_ID\n" );
		sql.append("FROM TM_DEALER\n" );
		sql.append("WHERE DEALER_CODE = '").append(vo.getEntityCode()).append("'\n");

		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		
		delaerId = String.valueOf(map.get("DEALER_ID"));
		return delaerId;
	}
	
	public String getPartId(PartStockVO vo){
		String partId = "";
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT PART_ID\n" );
		sql.append("FROM TM_PT_PART_BASE A\n");
		sql.append("WHERE A.PART_CODE = '").append(vo.getPartNo().trim()).append("'\n");
		//sql.append("AND A.PART_NAME = '").append(vo.getPartName()).append("'\n");

		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		if (null == map) {
			throw new RpcException("Cann't find part info by partCode == " + vo.getPartNo());
		}
		partId = String.valueOf(map.get("PART_ID"));
		return partId;
	}

}
