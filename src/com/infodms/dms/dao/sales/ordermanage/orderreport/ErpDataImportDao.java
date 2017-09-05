/**
 * 
 */
package com.infodms.dms.dao.sales.ordermanage.orderreport;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDealerPO;
import com.infoservice.po3.bean.PO;

/**
 * @author Administrator
 * 
 */
public class ErpDataImportDao extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(ErpDataImportDao.class);
	private static final ErpDataImportDao dao = new ErpDataImportDao();

	public static final ErpDataImportDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Map<String, Object>> getTipVsDlvryDtlList(Long orderId, String materialCode) {

		StringBuffer sql = new StringBuffer();

		sql.append("select tvdd.*\n");
		sql.append("  from tip_vs_dlvry tvd, tip_vs_dlvry_dtl tvdd\n");
		sql.append(" where tvd.send_id = tvdd.head_id\n");
		sql.append("   and tvd.erp_order_id = " + orderId + "\n");
		sql.append("   and tvdd.material_code = '" + materialCode + "'\n");
		sql.append("   and tvd.is_err = '0'\n");
		sql.append("   and tvdd.is_err = '0'");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}

	public Map<String, Object> getTmpVwNumMap(Long orderId, Long materialId, String batchNo) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT *\n");
		sql.append("  FROM TMP_VW_NUM TVN\n");
		sql.append(" WHERE TVN.ERP_ORDER_ID = " + orderId + "\n");
		sql.append("   AND TVN.MATERIAL_ID = " + materialId + "\n");
		sql.append("   AND TVN.BATCH_NO = " + batchNo + "");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		if (list.size() != 0) {
			return list.get(0);
		}
		return null;
	}
	
	public TmDealerPO getTmDealerByDealerCode(String dealerCode) {
		TmDealerPO po = new TmDealerPO();
		po.setDealerCode(dealerCode);
		List<PO> list = select(po);
		return list.size() != 0 ? (TmDealerPO) list.get(0) : null;
	}
}
