package com.infodms.dms.dao.sales.fleetmanage.fleetinfomanage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.FleetInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtFleetEditLogPO;
import com.infoservice.po3.bean.PageResult;


/**
 * 
 * <p>FleetEditLogDao.java</p>
 *
 * <p>Description: 集团客户更改记录持久化层</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-6-17</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class FleetEditLogDao extends BaseDao<TtFleetEditLogPO>{
	
	private static final FleetEditLogDao dao = new FleetEditLogDao();
	
	public static final FleetEditLogDao getInstance() {
		return dao;
	}
	protected TtFleetEditLogPO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
    
	/**
	 * 插入日志表信息
	 * @param po
	 */
	public void inserLog(TtFleetEditLogPO po){
		dao.insert(po);
	}
	
	
	/**
	 * 更新日志表信息
	 * @param po1
	 * @param po2
	 */
	public void updatelog(TtFleetEditLogPO po1,TtFleetEditLogPO po2){
		dao.update(po1, po2);
	}
	
	/**
	 * 集团客户报备更改审核查询
	 * @param bean
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String, Object>> queryInfoForModify(FleetInfoBean bean,int pageSize,int curPage){
		StringBuffer sql= new StringBuffer();

		sql.append("SELECT DISTINCT TFEL.EDIT_ID,\n" );
		sql.append("       TFEL.FLEET_ID,\n" );
		sql.append("       TFEL.FLEET_NAME,\n" );
		sql.append("       TFEL.FLEET_TYPE,\n" );
		sql.append("       TFEL.REGION,\n" );
		sql.append("       TFEL.STATUS,\n" );
		sql.append("       TO_CHAR(TFEL.AUDIT_DATE, 'YYYY-MM-DD') AUDIT_DATE,\n" );
		sql.append("       TFEL.AUDIT_REMARK,\n" );
		sql.append("       TCOM.COMPANY_SHORTNAME,\n" );
		sql.append("       TO_CHAR(TFEL.CREATE_DATE,'YYYY-MM-DD') AS CREATE_DATE\n" );
		sql.append("  FROM TT_FLEET_EDIT_LOG TFEL,TM_ORG O,TM_COMPANY TCOM,tm_dealer d,vw_org_dealer VOD,tm_dealer_org_relation dor\n" );
		sql.append(" WHERE TFEL.IS_DEL = 0\n" );
		sql.append("   AND TFEL.DLR_COMPANY_ID = TCOM.COMPANY_ID\n" );
		sql.append("   and d.company_id = tfel.dlr_company_id\n" );
		sql.append("   AND VOD.DEALER_ID = d.dealer_id");
		sql.append("   and dor.dealer_id = VOD.ROOT_DEALER_ID\n" );
		sql.append("   AND dor.ORG_ID = VOD.ROOT_ORG_ID ");
		sql.append("   AND O.ORG_ID= VOD.ROOT_ORG_ID ");
		sql.append("   AND O.IS_COUNT = 0\n");
		sql.append("   AND TCOM.COMPANY_TYPE = ").append(Constant.COMPANY_TYPE_DEALER).append("\n");
		sql.append("   AND TFEL.DATA_TYPE = ").append(Constant.DATA_TYPE_01).append("\n");
		if(Utility.testString(bean.getIsDel())){
			sql.append("   AND TFEL.STATUS = ").append(Constant.FLEET_INFO_TYPE_02).append("\n");
		}
		if(!"".equals(bean.getFleetName())){
			sql.append("   AND TFEL.FLEET_NAME LIKE '%" );
			sql.append(bean.getFleetName());
			sql.append("%'\n");
		}
		
		if(!"".equals(bean.getFleetType())){
			sql.append("   AND TFEL.FLEET_TYPE =\n" );
			sql.append(bean.getFleetType());
			sql.append("\n");
		}
		
		if(!"".equals(bean.getBeginTime())){
			sql.append("   AND TFEL.CREATE_DATE >= TO_DATE('" );
			sql.append(bean.getBeginTime());
			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		if(!"".equals(bean.getEndTime())){
			sql.append("   AND TFEL.CREATE_DATE <= TO_DATE('");
			sql.append(bean.getEndTime());
			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		
		if(null!= bean.getDlrCompanyId()&&!"".equals(bean.getDlrCompanyId())){
			sql.append("   AND TFEL.DLR_COMPANY_ID IN(");
			sql.append(bean.getDlrCompanyId());
			sql.append(")\n");
		}
		
		if(Utility.testString(bean.getDutyType())){
			if(bean.getDutyType().equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))){
				sql.append("AND dor.ORG_ID = ").append(bean.getOrgId());
			}
		}
		
		List<Object> params = new LinkedList<Object>();

		if(null!= bean.getDlrCompanyCode()&&!"".equals(bean.getDlrCompanyCode())){
			sql.append("  AND TFEL.DLR_COMPANY_ID IN\n" );
			sql.append("       (SELECT DISTINCT TD.COMPANY_ID\n" );
			sql.append("          FROM TM_DEALER TD\n" );
			sql.append("         WHERE TD.DEALER_CODE IN (").append(bean.getDlrCompanyCode()).append(")\n");
			sql.append("       )\n");
		}
		
		//sql.append("  ORDER BY TFEL.FLEET_ID,TFEL.CREATE_DATE");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	/**
	 * 根据修改的记录ID查询修改过的记录
	 * @param editId
	 * @return
	 */
	public Map<String, Object> getModifyInfobyId(String editId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TL.EDIT_ID,\n" );
		sql.append("          TL.FLEET_ID,\n" );
		sql.append("          TL.FLEET_NAME,\n" );
		sql.append("          TL.FLEET_TYPE,\n" );
		sql.append("          TL.REGION,\n" );
		sql.append("          TL.MAIN_LINKMAN,\n" );
		sql.append("          TL.MAIN_PHONE,\n" );
		sql.append("          TL.SUBMIT_USER,\n" );
		sql.append("          TO_CHAR(TL.SUBMIT_DATE,'YYYY-MM-DD') SUBMIT_DATE,\n" );
		sql.append("          TL.MAIN_BUSINESS,\n" );
		sql.append("          TL.FUND_SIZE,\n" );
		sql.append("          TL.STAFF_SIZE,\n" );
		sql.append("          TL.PURPOSE,\n" );
		sql.append("          TL.ADDRESS,\n" );
		sql.append("          TL.MAIN_JOB,\n" );
		sql.append("          TL.ZIP_CODE,\n" );
		sql.append("          TL.MAIN_EMAIL,\n" );
		sql.append("          TL.STATUS,\n" );
		sql.append("          TL.OTHER_LINKMAN,\n" );
		sql.append("          TL.SERIES_ID,\n" );
		sql.append("          NVL(D.GROUP_NAME, '全系') GROUP_NAME,\n" );
		sql.append("          TL.SERIES_COUNT,\n" );
		sql.append("          TL.OTHER_PHONE,\n" );
		sql.append("          TL.OTHER_JOB,\n" );
		sql.append("          TL.OTHER_EMAIL,\n" );
		sql.append("          TL.REQ_REMARK,\n" );
		sql.append("          TL.AUDIT_DATE,\n" );
		sql.append("          TO_CHAR(TL.VISIT_DATE, 'YYYY-MM-DD') VISIT_DATE,\n" );
		sql.append("          TL.AUDIT_USER_ID,\n" );
		sql.append("          TL.AUDIT_REMARK,\n" );
		sql.append("          TL.PACT_ID,\n" );
		sql.append("          TL.IS_PACT,\n" );
		sql.append("          TCP.PACT_NAME,\n" );
		sql.append("          TL.IS_DEL,\n" );
		sql.append("          TL.PACT_MANAGE NAME,\n" );
		sql.append("          TL.PACT_MANAGE_PHONE,\n" );
		sql.append("          TD.DEALER_SHORTNAME\n" );
		sql.append("     FROM TT_FLEET_EDIT_LOG TL,TC_USER TU,TM_DEALER TD, TM_VHCL_MATERIAL_GROUP D, Tm_Company_Pact tcp\n" );
		sql.append("    WHERE TL.IS_DEL = " );
		sql.append(Constant.IS_DEL_00);
		sql.append(" AND TL.SERIES_ID = D.GROUP_ID(+)\n");
		sql.append(" AND TL.PACT_ID = TCP.PACT_ID(+)\n");
		sql.append("\n");
		sql.append("    AND   TL.DATA_TYPE = " );
		sql.append(Constant.DATA_TYPE_01);
		sql.append("\n");
		sql.append("    AND   TL.SUBMIT_USER = TU.USER_ID(+)\n" );
		sql.append("    AND   TL.DLR_COMPANY_ID = TD.COMPANY_ID(+)\n" );
		sql.append("    AND   TL.EDIT_ID =");
		sql.append(editId);

		
		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	
	
	/**
	 * 根据集团客户ID在日志表中查找状态为未确认的数据
	 * @param fleetId
	 * @return
	 */
	public Map<String, Object> getRecordByFleetId(String fleetId){
		
		String sql = "SELECT TFEL.EDIT_ID FROM TT_FLEET_EDIT_LOG TFEL WHERE TFEL.DATA_TYPE = "+Constant.DATA_TYPE_01+" AND TFEL.STATUS = "+Constant.FLEET_INFO_TYPE_02+" AND TFEL.FLEET_ID ="+fleetId;
		
		Map<String, Object> map = pageQueryMap(sql,null,getFunName());
		return map;
	}
}
