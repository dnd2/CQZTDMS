package com.infodms.dms.job.erp.dealer;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class ErpDealerSyncJobDao extends BaseDao {

	private static final ErpDealerSyncJobDao dao = new ErpDealerSyncJobDao();
	
	public static final ErpDealerSyncJobDao getInstance(){
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {

		return null;
	}

	/**
	 * 查询需要同步的数据
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getSyncDealerList() {
		
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT * FROM Z_DEALER WHERE STATUS = '0'"); 
		
		return dao.pageQuery(sbSql.toString(), null, getFunName());
	}

	/**
	 * 公司代码是否在DMS系统中已经存在
	 * 
	 * @param string
	 * @return
	 */
	public boolean companyExist(String string) {
		
		return false;
	}

	/**
	 * 经销商代码是否在DMS系统中已经存在
	 * @param string
	 * @return
	 */
	public boolean dealerExist(String string) {

		return false;
	}

}
