package com.infodms.dms.dao.sales.ordermanage.orderreport;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class DealerPriceQueryDAO extends BaseDao {
	public static Logger logger = Logger.getLogger(DealerPriceQueryDAO.class);
	private static final DealerPriceQueryDAO dao = new DealerPriceQueryDAO();

	public static final DealerPriceQueryDAO getInstance() {
		return dao;
	}

	public static PageResult<Map<String, Object>> getDealerPriceList(String dealerId, String modelCode, int pageSize, int curPage) {
		StringBuffer sqlStr = new StringBuffer("\n");

		sqlStr.append("select dpr1.price_id price_id\n");
		sqlStr.append("  from vw_TM_DEALER_PRICE_RELATION dpr1\n");
		sqlStr.append(" where dpr1.is_default = ").append(Constant.IF_TYPE_YES).append("\n");
		sqlStr.append("   and dpr1.dealer_id IN (").append(dealerId).append(")\n");

		List<Map<String, Object>> listStr = dao.pageQuery(sqlStr.toString(), null, dao.getFunName());

		StringBuffer priceStr = null;

		if (!CommonUtils.isNullList(listStr)) {
			priceStr = new StringBuffer("");
			int len = listStr.size();

			for (int i = 0; i < len; i++) {
				if (0 == priceStr.length()) {
					priceStr.append(listStr.get(i).get("PRICE_ID"));
				} else {
					priceStr.append(",").append(listStr.get(i).get("PRICE_ID"));
				}
			}
		}

		if (null == priceStr) {
			priceStr = new StringBuffer("");
			priceStr.append("0.1"); // 0.1表示无效价格列表id
		}

		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TVMG1.GROUP_CODE MODEL_CODE,\n" );
		sql.append("       TVMG1.GROUP_NAME MODEL_NAME,\n" );
		sql.append("       TVMG.GROUP_CODE,\n" );
		sql.append("       TVMG.GROUP_NAME,\n" );
		sql.append("       T.OPERAND SALES_PRICE\n" );
		sql.append("  FROM CUX_DMS_MATERIAL_PRICE_V@DMS2EBS2 T,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP            TVMG,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP            TVMG1,\n" );
		sql.append("       TM_DEALER                         TD\n" );
		sql.append(" WHERE TVMG.GROUP_CODE = T.CATEGORIES\n" );
		sql.append("   AND TD.DEALER_CODE = T.PARTY_NUMBER\n" );
		sql.append("   AND TVMG.PARENT_GROUP_ID = TVMG1.GROUP_ID\n" );
		//sql.append("   AND PARTY_NUMBER = '1261'");
		 sql.append(" AND TD.DEALER_ID IN ("+dealerId+")\n");
		if (!"".equals(modelCode)) {
			sql.append("   AND TVMG1.GROUP_CODE LIKE ('" + modelCode.trim() + "%')\n");
		}

		// sql.append(" AND R.IS_DEFAULT = "+Constant.IF_TYPE_YES+"\n");
		sql.append(" ORDER BY MODEL_CODE, GROUP_CODE\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
	}

	/**
	 * 经销商价格查询 -- 查询
	 * 
	 * @param dealerId
	 *            经销商ID
	 * @param priceId
	 *            价格ID
	 * @param modelCode
	 *            车型代码 2012-06-28
	 */
	public static PageResult<Map<String, Object>> getDealerPriceListOEM(String dealerId, String modelCode, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMD.DEALER_SHORTNAME,\n");
		sql.append("       TMD.DEALER_CODE,\n");
		sql.append("       G1.GROUP_CODE MODEL_CODE,\n");
		sql.append("       G1.GROUP_NAME MODEL_NAME,\n");
		sql.append("       G.GROUP_CODE,\n");
		sql.append("       G.GROUP_NAME,\n");
		sql.append("       D.SALES_PRICE,\n");
		sql.append("       TO_CHAR(D.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE\n");
		sql.append("  FROM TT_VS_PRICE_DTL          D,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   G1,\n");
		sql.append("       TM_DEALER                TMD,\n");
		sql.append("       TM_DEALER_PRICE_RELATION PR\n");
		sql.append(" WHERE D.GROUP_ID = G.GROUP_ID\n");
		sql.append("   AND D.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND G.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND G.FORCAST_FLAG = 1\n");
		sql.append("   AND G.PARENT_GROUP_ID = G1.GROUP_ID\n");
		sql.append("   AND TMD.DEALER_ID = PR.DEALER_ID\n");
		sql.append("   AND PR.PRICE_ID = D.PRICE_ID\n");
		sql.append("   AND G.PARENT_GROUP_ID = G1.GROUP_ID\n");

		if (modelCode != null && !"".equals(modelCode)) {
			sql.append("   AND G.GROUP_CODE LIKE '" + modelCode + "%' ");
			sql.append("\n");
		}
		if (dealerId != null && !"".equals(dealerId)) {
			sql.append("   AND TMD.DEALER_ID = ");
			sql.append(dealerId);
			sql.append("\n");
		}

		sql.append(" ORDER BY MODEL_CODE, GROUP_CODE\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
	}

	public static List<Map<String, Object>> getLowerDealerList(String dealerId) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMD.DEALER_SHORTNAME,TMD.DEALER_CODE, TMD.DEALER_ID\n");
		sql.append("  FROM TM_DEALER TMD\n");
		sql.append(" WHERE TMD.PARENT_DEALER_D IN (" + dealerId + ")\n");
		sql.append(" ORDER BY TMD.DEALER_CODE\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getDealerId(String dealerCode) {
		StringBuffer sql = new StringBuffer("");
		sql.append("select td.dealer_id from tm_dealer td\n");
		if (dealerCode != null && !"".equals(dealerCode)) {
			sql.append("where td.dealer_code = ('");
			sql.append(dealerCode);
			sql.append("')\n");
		}
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getPriceId(String dealerId) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT DPR1.PRICE_ID PRICE_ID, DPR1.DEALER_ID DEALER_ID\n");
		sql.append("  FROM VW_TM_DEALER_PRICE_RELATION DPR1\n");
		sql.append(" WHERE DPR1.IS_DEFAULT = ");
		sql.append(Constant.IF_TYPE_YES);
		sql.append("\n");

		if (dealerId != null && !"".equals(dealerId)) {
			sql.append("   AND DPR1.DEALER_ID = ");
			sql.append(dealerId);
			sql.append("\n");
		}
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
}
