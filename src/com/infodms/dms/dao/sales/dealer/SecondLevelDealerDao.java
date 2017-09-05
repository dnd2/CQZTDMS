package com.infodms.dms.dao.sales.dealer;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class SecondLevelDealerDao extends BaseDao {
	private static SecondLevelDealerDao dao = new SecondLevelDealerDao ();
	public static final SecondLevelDealerDao getInstance() {
		if (dao == null) {
			dao = new SecondLevelDealerDao();
		}
		return dao;
	}
	private SecondLevelDealerDao() {}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 二级经销商申请初始化查询
	 */
	public PageResult<Map<String, Object>> secondLevelDealerApplyInitQuery(Map<String, Object> map) throws Exception{
		
		String dealerCode = CommonUtils.checkNull(map.get("DEALER_CODE"));
		String dealerName = CommonUtils.checkNull(map.get("DEALER_NAME"));
		String IMAGE_COMFIRM_LEVEL = CommonUtils.checkNull(map.get("IMAGE_COMFIRM_LEVEL"));
		String AUTHORIZATION_TYPE = CommonUtils.checkNull(map.get("AUTHORIZATION_TYPE"));
		String WORK_TYPE = CommonUtils.checkNull(map.get("WORK_TYPE"));
		String serviceStatus = CommonUtils.checkNull(map.get("SERVICE_STATUS"));
		String AUTHORIZATION_SCREATE_DATE = CommonUtils.checkNull(map.get("AUTHORIZATION_SCREATE_DATE"));
		String AUTHORIZATION_ECREATE_DATE = CommonUtils.checkNull(map.get("AUTHORIZATION_ECREATE_DATE"));
		Integer curPage = (Integer) map.get("curPage");
		Integer pageSize = (Integer) map.get("pageSize");
		String dealerId = (String) map.get("logonUserDealerId");
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("select a.dealer_id, a.dealer_code,\n");
		sbSql.append("       a.dealer_name,\n");
		sbSql.append("       a.DEALER_SHORTNAME,\n");
		sbSql.append("       a.status,\n");
		sbSql.append("       e.COMPANY_NAME,\n");
		sbSql.append("		 c.dealer_code parent_dealer_code,\n");
		sbSql.append("		 c.dealer_name parent_dealer_name,\n"); 
		sbSql.append("       o.org_name,\n");
		sbSql.append("       a.dealer_level,\n");
		sbSql.append("       b.AUTHORIZATION_TYPE,\n");
		sbSql.append("       b.AUTHORIZATION_DATE,\n");
		sbSql.append("       b.WORK_TYPE,\n");
		sbSql.append("       DECODE(b.SHOP_TYPE,'直营','重点客户','代理','代理') SHOP_TYPE, \n");
		sbSql.append("       b.IMAGE_LEVEL,\n");
		sbSql.append("       b.IMAGE_COMFIRM_LEVEL,\n");
		sbSql.append("       a.service_status,\n");
		sbSql.append("       g.ROOT_ORG_NAME,\n");
		sbSql.append("       g.ROOT_ORG_ID,\n");
		sbSql.append("       f.REGION_NAME\n");
		
		sbSql.append("  from tm_dealer              a,\n");
		sbSql.append("       tm_dealer_detail       b,\n");
		sbSql.append("       tm_dealer              c,\n");
		sbSql.append("       tm_org                 o,\n");
		sbSql.append("       tm_company             e,\n");
		sbSql.append("       tm_region             f,\n");
		sbSql.append("       vw_org_dealer_service            g\n");
		sbSql.append(" where a.dealer_id = b.fk_dealer_id(+)\n");
		sbSql.append(" and a.dealer_id = g.dealer_id(+)\n");
		sbSql.append("   and a.parent_dealer_d = c.dealer_id(+)\n");
		sbSql.append("   and o.org_id = a.dealer_org_id\n");
		sbSql.append("   and a.company_id = e.company_id\n");
		sbSql.append("   and a.province_id = f.region_code\n");  

        if (StringUtils.isNotEmpty(AUTHORIZATION_SCREATE_DATE)) {
        	sbSql.append(" and AUTHORIZATION_DATE>= to_date('").append(AUTHORIZATION_SCREATE_DATE).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (StringUtils.isNotEmpty(AUTHORIZATION_ECREATE_DATE)) {
        	sbSql.append(" and AUTHORIZATION_DATE<= to_date('").append(AUTHORIZATION_ECREATE_DATE).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if(StringUtils.isNotEmpty(dealerName)){
			sbSql.append("   and (a.dealer_name like ? or a.pinyin like ?  )\n");
			params.add("%"+dealerName+"%");
			params.add("%"+dealerName+"%");
		}
		
		if(StringUtils.isNotEmpty(dealerCode)){
			sbSql.append("   and a.dealer_code like ?\n");
			params.add("%"+dealerCode+"%");
		}
		
		if(StringUtils.isNotEmpty(serviceStatus)) {
			sbSql.append("   and a.service_status = ?\n");
			params.add(serviceStatus);
		}
		if(StringUtils.isNotEmpty(IMAGE_COMFIRM_LEVEL)) {
			sbSql.append("   and b.IMAGE_COMFIRM_LEVEL = ?\n");
			params.add(IMAGE_COMFIRM_LEVEL);
		}
		if(StringUtils.isNotEmpty(WORK_TYPE)) {
			sbSql.append("   and b.SHOP_TYPE = ?\n");
			params.add(WORK_TYPE);
		}
		if(StringUtils.isNotEmpty(AUTHORIZATION_TYPE)) {
			sbSql.append("   and b.AUTHORIZATION_TYPE = ?\n");
			params.add(AUTHORIZATION_TYPE);
		}
		sbSql.append(" AND a.PARENT_DEALER_D=").append(dealerId).append(" \n");
		sbSql.append("   order by g.ROOT_ORG_ID,g.org_id,g.dealer_id");
		return dao.pageQuery(sbSql.toString(), params, getFunName(),pageSize, curPage);
	}
	
}
