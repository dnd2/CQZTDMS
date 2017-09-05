package com.infodms.dms.dao.report;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class FLEETCONTRACTDao extends BaseDao{
	public  static final Logger logger = Logger.getLogger(FLEETCONTRACTDao.class);
	public static final FLEETCONTRACTDao dao = new FLEETCONTRACTDao();
	public static FLEETCONTRACTDao getInstance(){
		return dao;
		
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<Map<String, Object>> getFLEETCONTRACTDaoSelect(Map<String, String> map){
		String checkDate1 = map.get("checkDate1");
		String checkDate2 = map.get("checkDate2");
		String orgId = map.get("orgId");
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer("\n");

		sql.append("SELECT /*+ all_rows */\n") ;
		sql.append("distinct(tfc.contract_id), nvl(TMO.ORG_NAME, 0) ORG_NAME, --区域事业部\n") ;
		sql.append("         NVL(TMR.REGION_NAME, '未知') REGION_NAME, --省份\n") ;
		sql.append("         TO_CHAR(TMF.SUBMIT_DATE, 'YYYY-MM-DD') SUBMIT_DATE, --提报日期\n") ;
		sql.append("         TMC.COMPANY_SHORTNAME, --提报单位\n") ;
		sql.append("         TMF.FLEET_NAME, --客户名称\n") ;
		sql.append("         TC1.CODE_DESC FLEET_TYPE, --客户类型\n") ;
		sql.append("         TFC.CONTRACT_NO, --合同编号\n") ;
		sql.append("         TFC.BUY_FROM, --买方\n") ;
		sql.append("         TFC.SELL_TO, --卖方\n") ;
		sql.append("         TO_CHAR(TFC.CHECK_DATE, 'YYYY-MM-DD') CHECK_DATE, --合同签订日期\n") ;
		sql.append("         TO_CHAR(TFC.START_DATE, 'YYYY-MM-DD') START_DATE, --有效期起\n") ;
		sql.append("         TO_CHAR(TFC.END_DATE, 'YYYY-MM-DD') END_DATE, --有效期止\n") ;
		sql.append("         NVL(TVMG.GROUP_NAME, '全系') GROUP_NAME, --签约车系\n") ;
		sql.append("         NVL(TFIN.INTENT_COUNT, 0) INTENT_COUNT, --数量\n") ;
		sql.append("         NVL(TFIN.NORM_AMOUNT, 0) NORM_AMOUNT, --标准价格\n") ;
		sql.append("         NVL(TFIN.COUNT_AMOUNT, 0) COUNT_AMOUNT, --合计金额\n") ;
		sql.append("         NVL(TFIN.INTENT_POINT, 0) INTENT_POINT, --折扣点\n") ;
		sql.append("         NVL(TFC.OTHER_AMOUNT, 0) OTHER_AMOUNT, --特殊金额\n") ;
		sql.append("         NVL(TFC.DIS_AMOUNT, 0) DIS_AMOUNT, --折让总额\n") ;
		sql.append("         TFC.OTHER_REMARK, --特殊需求\n") ;
		sql.append("         TO_CHAR(TFC.AUDIT_DATE, 'YYYY-MM-DD') AUDIT_DATE --审核日期\n") ;
		sql.append("  FROM TT_FLEET_CONTRACT      TFC,\n") ;
		sql.append("       TM_FLEET               TMF,\n") ;
		sql.append("       TM_COMPANY             TMC,\n") ;
		sql.append("       TM_DEALER              TMD,\n") ;
		sql.append("       TM_DEALER_ORG_RELATION TDOR,\n") ;
		sql.append("       TM_ORG                 TMO,\n") ;
		sql.append("       TM_REGION              TMR,\n") ;
		sql.append("       TC_CODE                TC1,\n") ;
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n") ;
		sql.append("       TT_FLEET_INTENT_NEW    TFIN\n") ;
		sql.append(" WHERE TFC.FLEET_ID = TMF.FLEET_ID\n") ;
		sql.append("   AND TFIN.contract_id = tfc.contract_id\n") ;
		sql.append("   AND TVMG.GROUP_ID(+) = TMF.SERIES_ID\n") ;
		sql.append("   AND TC1.CODE_ID = TMF.FLEET_TYPE\n") ;
		sql.append("   AND TMF.DLR_COMPANY_ID = TMC.COMPANY_ID\n") ;
		sql.append("   AND TMD.COMPANY_ID = TMC.COMPANY_ID\n") ;
		sql.append("   AND f_get_pid(TMD.DEALER_ID) = TDOR.DEALER_ID\n") ;
		sql.append("   AND TMO.ORG_ID = TDOR.ORG_ID\n") ;
		sql.append("   AND TMR.REGION_CODE(+) = TMF.REGION\n") ;
		sql.append("   AND TFC.STATUS IN (").append(Constant.FLEET_CON_STATUS_03).append(")\n") ;
		sql.append("   AND TMF.STATUS = ").append(Constant.FLEET_INFO_TYPE_03).append("\n") ;

		if(Utility.testString(checkDate1)){
			sql.append("   AND TO_DATE(TO_CHAR(TFC.AUDIT_DATE, 'YYYY-MM-DD'),'yyyy-mm-dd') >=  TO_DATE('"+checkDate1+"','yyyy-mm-dd')\n");
		}
		if(Utility.testString(checkDate2)){
			sql.append("   AND (TO_DATE('"+checkDate2+"','yyyy-mm-dd')) >=  TO_DATE(TO_CHAR(TFC.AUDIT_DATE, 'YYYY-MM-DD'),'yyyy-mm-dd')\n");
		}
		if(Utility.testString(orgId)){
			sql.append("   AND TMO.ORG_ID IN ("+orgId+")\n");
		}
		sql.append("   AND TMF.CON_STATUS = 1\n");
		sql.append("  order by ORG_NAME,REGION_NAME,COMPANY_SHORTNAME\n");
		
		
		list = super.pageQuery(sql.toString(), null, getFunName());
		return list;
		
	}
}