package com.infodms.dms.dao.report.serviceReport;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class OldPieceAuditDelDao  extends BaseDao{
	public static final Logger logger = Logger.getLogger(OldPieceAuditDelDao.class);
	public static final OldPieceAuditDelDao dao = new OldPieceAuditDelDao();
	public static final OldPieceAuditDelDao getInstance(){
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO 自动生成的方法存根
		return null;
	}
	public  PageResult<Map<String, Object>> QueryOldPieceAuditDel(Map<String,String> map,int pageSize,int curPage) {
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DS.DEALER_CODE, /*经销商代码*/\n" );
		sql.append("       DS.DEALER_NAME, /*经销商名称*/\n" );
		sql.append("       DS.ORG_NAME, /*小区*/\n" );
		sql.append("       A.MODEL_NAME, /*车型*/\n" );
		sql.append("       A.VIN, /*VIN*/\n" );
		sql.append("       RD.PART_CODE, /*故障件代码*/\n" );
		sql.append("       RD.PART_NAME, /*故障件名称*/\n" );
		sql.append("       RD.CLAIM_NO,\n");
		sql.append("       RD.PRICE,\n" );
		sql.append("       1 RETURN_NUM, /*回运数*/\n" );
		sql.append("       RD.SIGN_AMOUNT SIGN_NUM, /*签收数*/\n" );
		sql.append("       DR.CODE_DESC DEDUCT_REMARK, /*扣件原因*/\n" );
		sql.append("       RD.BARCODE_NO, /*扣件条码*/\n" );
		sql.append("       TO_CHAR(R.IN_WARHOUSE_DATE, 'yyyy-mm-dd') REPORT_DATE /*结算上报日期*/\n" );
		sql.append("  FROM TT_AS_WR_OLD_RETURNED R\n" );
		sql.append("  LEFT JOIN VW_ORG_DEALER_SERVICE DS ON R.DEALER_ID = DS.DEALER_ID\n" );
		sql.append("  JOIN TT_AS_WR_OLD_RETURNED_DETAIL RD ON R.ID = RD.RETURN_ID\n" );
		sql.append("  JOIN TT_AS_WR_APPLICATION A ON RD.CLAIM_NO = A.CLAIM_NO\n" );
		sql.append("  JOIN TT_AS_WR_PARTSITEM P ON A.ID = P.ID AND RD.PART_CODE = P.PART_CODE\n" );
		sql.append("  JOIN TT_AS_WR_PARTSITEM_BARCODE PB ON RD.BARCODE_NO = PB.BARCODE_NO AND PB.PART_ID = P.PART_ID\n" );
		sql.append("  LEFT JOIN TC_CODE DR ON RD.DEDUCT_REMARK = DR.CODE_ID AND DR.TYPE = "+Constant.OLDPART_DEDUCT_TYPE+"\n" );
		sql.append(" WHERE RD.SIGN_AMOUNT = 0\n" );
		sql.append("   AND A.CREATE_DATE >= TO_DATE('2013-08-26', 'YYYY-MM-DD')");
		if(Utility.testString(map.get("settlementNo"))){
			sql.append(" and RD.CLAIM_NO like '%"+map.get("settlementNo")+"%'\n"); 
		}
		if(Utility.testString(map.get("vin"))){
			sql.append(" and A.VIN like '%"+map.get("vin")+"%'\n"); 
		}
		if(Utility.testString(map.get("OldpartDeductType"))){
			sql.append(" AND DR.CODE_ID = '"+map.get("OldpartDeductType")+"'\n"); 
		}
		if (null != map.get("dealercode") && !"".equals(map.get("dealercode"))) {
			String[] array = map.get("dealercode").toString().split(",");
			sql.append("   AND DS.DEALER_CODE IN ( \n");
			if(array.length>0){
				for (int i = 0; i < array.length; i++) {
					sql.append("'"+array[i]+"'");
						if (i != array.length - 1) {
							sql.append(",");
						}	
				}
			}else{
				sql.append("''");//放空置，防止in里面报错
			}
			sql.append(")\n");
		}
		if(Utility.testString(map.get("smallorg"))){
			sql.append(" and DS.ORG_ID ='"+map.get("smallorg").toUpperCase()+"'\n"); 
		}
		if(Utility.testString(map.get("dealerName"))){
			sql.append(" and  DS.DEALER_NAME like '%"+map.get("dealerName")+"%'\n"); 
		}
		if(Utility.testString(map.get("bDate"))){
			sql.append("and r.in_warhouse_date>=to_date('"+map.get("bDate")+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n"); 
		}
		if(Utility.testString(map.get("eDate"))){
			sql.append("and r.in_warhouse_date<=to_date('"+map.get("eDate")+" 23:59:59','yyyy-mm-dd hh24:mi:ss')\n"); 
		}


		PageResult<Map<String, Object>> ps= pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return ps;
	}
		
}
