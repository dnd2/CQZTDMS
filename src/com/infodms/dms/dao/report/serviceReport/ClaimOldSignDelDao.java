package com.infodms.dms.dao.report.serviceReport;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ClaimOldSignDelDao extends BaseDao {
	public static final Logger logger = Logger.getLogger(ClaimOldSignDelDao.class);
	public static final ClaimOldSignDelDao dao = new ClaimOldSignDelDao();
	public static final ClaimOldSignDelDao getInstance(){
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO 自动生成的方法存根
		return null;
	}
	public  PageResult<Map<String, Object>> QueryClaimOldSignDel(Map<String,String> map,int pageSize,int curPage) {
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DS.DEALER_CODE, /*经销商代码*/\n" );
		sql.append("       DS.DEALER_NAME, /*经销商名称*/\n" );
		sql.append("       D.PC, /*回运批次*/\n" );
		sql.append("       DS.ORG_NAME, /*小区*/\n" );
		sql.append("       TT.CODE_DESC TRANSPORT_TYPE, /*货运方式*/\n" );
		sql.append("       RT.CODE_DESC RETURN_TYPE, /*回运类型*/\n" );
		sql.append("       R.TRANSPORT_NO, /*发运单号*/\n" );
		sql.append("       TO_CHAR(R.SEND_TIME, 'YYYY-MM-DD') SEND_TIME, /*发运时间*/\n" );
		sql.append("       R.PARKAGE_AMOUNT, /*装箱总数*/\n" );
		sql.append("       R.REAL_BOX_NO, /*实到箱数*/\n" );
		sql.append("       PP.CODE_DESC PART_PAKGE, /*包装情况*/\n" );
		sql.append("       R.PART_MARK,/*故障卡情况*/\n" );
		sql.append("       PD.CODE_DESC PART_DETAIL, /*清单情况*/\n" );
		sql.append("       U.NAME, /*签收人*/\n" );
		sql.append("       TO_CHAR(R.SIGN_DATE,'YYYY-MM-DD') SIGN_DATE, /*签收时间*/\n" );
		sql.append("       R.TEL, /*三包员电话*/\n" );
		sql.append("       R.SIGN_REMARK /*签收备注*/\n" );
		sql.append("  FROM TT_AS_WR_OLD_RETURNED R\n" );
		sql.append("  LEFT JOIN VW_ORG_DEALER_SERVICE DS ON R.DEALER_ID = DS.DEALER_ID\n" );
		sql.append("  JOIN (SELECT RD.RETURN_ID, TO_CHAR(A.REPORT_DATE, 'YYYYMM') PC\n" );
		sql.append("          FROM TT_AS_WR_OLD_RETURNED_DETAIL RD, TT_AS_WR_APPLICATION A\n" );
		sql.append("         WHERE RD.CLAIM_NO = A.CLAIM_NO AND A.CREATE_DATE >= TO_DATE('2013-08-26', 'YYYY-MM-DD')\n" );
		sql.append("         GROUP BY RD.RETURN_ID, TO_CHAR(A.REPORT_DATE, 'YYYYMM')) D ON R.ID = D.RETURN_ID\n" );
		sql.append("  LEFT JOIN TC_CODE TT ON R.TRANSPORT_TYPE = TT.CODE_ID AND TT.TYPE = 1177\n" );
		sql.append("  LEFT JOIN TC_CODE RT ON R.RETURN_TYPE = RT.CODE_ID AND RT.TYPE = "+Constant.BACK_TRANSPORT_TYPE+"\n" );
		sql.append("  LEFT JOIN TC_CODE PP ON R.Part_Pakge = PP.CODE_ID AND PP.TYPE = "+Constant.OLD_PART_PAKGE+"\n" );
		sql.append("  LEFT JOIN TC_CODE PM ON R.Part_Mark = PM.CODE_ID AND PM.TYPE = "+Constant.OLD_PART_MARK+"\n" );
		sql.append("  LEFT JOIN TC_CODE PD ON R.Part_Detail = PD.CODE_ID AND PD.TYPE = "+Constant.OLD_PART_DETAIL+"\n" );
		sql.append("  LEFT JOIN TC_USER U ON R.SIGN_PERSON = U.USER_ID\n" );
		sql.append(" WHERE R.STATUS IN ("+Constant.BACK_LIST_STATUS_03+", "+Constant.BACK_LIST_STATUS_04+", "+Constant.BACK_LIST_STATUS_05+")");
		if(Utility.testString(map.get("deliveryBatch"))){
			sql.append(" and D.PC like'%"+map.get("deliveryBatch")+"%'\n"); 
		}
		if(Utility.testString(map.get("smallorg"))){
			sql.append(" and DS.ORG_ID ='"+map.get("smallorg").toUpperCase()+"'\n"); 
		}
		if(Utility.testString(map.get("bigorg"))){
			sql.append(" and DS.ROOT_ORG_ID ='"+map.get("bigorg").toUpperCase()+"'\n"); 
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
		if(Utility.testString(map.get("dealerName"))){
			sql.append(" and  DS.DEALER_NAME like '%"+map.get("dealerName")+"%'\n"); 
		}
		if(Utility.testString(map.get("Signpeople"))){
			sql.append(" and U.NAME like '%"+map.get("Signpeople")+"%'\n"); 
		}
		if(Utility.testString(map.get("bDate"))){
			sql.append("and R.SEND_TIME>=to_date('"+map.get("bDate")+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n"); 
		}
		if(Utility.testString(map.get("eDate"))){
			sql.append("and R.SEND_TIME<=to_date('"+map.get("eDate")+" 23:59:59','yyyy-mm-dd hh24:mi:ss')\n"); 
		}
		if(Utility.testString(map.get("bgDate"))){
			sql.append("and R.SIGN_DATE>=to_date('"+map.get("bgDate")+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n"); 
		}
		if(Utility.testString(map.get("egDate"))){
			sql.append("and R.SIGN_DATE<=to_date('"+map.get("egDate")+" 23:59:59','yyyy-mm-dd hh24:mi:ss')\n"); 
		}


		PageResult<Map<String, Object>> ps= pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return ps;
	}

}
