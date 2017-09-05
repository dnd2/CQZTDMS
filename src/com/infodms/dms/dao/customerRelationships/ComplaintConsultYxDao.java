package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;


@SuppressWarnings("unchecked")
public class ComplaintConsultYxDao extends BaseDao{

	private static final ComplaintConsultYxDao dao = new ComplaintConsultYxDao();
	
	public static final ComplaintConsultYxDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	/**
	 * 
	 * @Title      : QueryComplaintInfoList
	 * @Description: 投诉咨询记录
	 * @param      : @param ctmid
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-15
	 */
	public List<Map<String, Object>> QueryComplaintInfoList(String ctmid) {
		StringBuffer sql = new StringBuffer();
		// 艾春9.25添加参数预编译信息
		List<Long> list = new ArrayList<Long>();
		
		sql.append(" select complaint.cp_id CPID, customer.ctm_id CTMID,customer.ctm_name CTMNAME,to_char(complaint.cp_acc_date,'yyyy-MM-dd') ACCDATE, ");		
		sql.append(" tcuser.name ACCUSER,status.Code_Desc STATUS,bizType.Code_Desc BIZTYPE,complaint.Cp_Content CPCONTENT ");		
		sql.append(" from TT_CRM_COMPLAINT complaint ");		
		sql.append(" left join tt_customer customer on customer.ctm_id = complaint.cp_cus_id ");		
		sql.append(" left join tc_code status on status.Code_Id = complaint.cp_status ");		
		sql.append(" left join tc_code bizType on bizType.Code_Id = complaint.Cp_Biz_Type ");	
		sql.append(" left join tc_user tcuser on tcuser.user_id = complaint.cp_acc_user");
		sql.append(" where customer.ctm_id=?");
		list.add(Long.parseLong(ctmid));
		return this.pageQuery(sql.toString(), list, this.getFunName());
	}
	/**
	 * 投诉咨询详细数据
	 * @param cpid 投诉咨询ID
	 * @param ctmid 客户ID
	 * @return
	 */
	public Map<String, Object> queryComplaintConsult(String cpid,
			String ctmid) {
		StringBuffer sql = new StringBuffer();
		// 艾春 9.18 修改
			sql.append("SELECT COMPLAINT.CP_NO CPNO,\n");
			sql.append("      COMPLAINT.CP_ID CPID,\n"); 
			sql.append("      COMPLAINT.CP_CONTENT CPCONT,\n"); 
			sql.append("      COMPLAINT.CP_NAME CTMNAME,\n"); 
			sql.append("      COMPLAINT.CP_SEAT_COMMENT CPSEATCOMMENT,\n");
			sql.append("      DEALORG.ORG_NAME CPDEALORG,\n"); 
			sql.append("      CUSTOMER.CTM_ID CTMID,\n"); 
			sql.append("      COMPLAINT.CP_PHONE CPPHONE,\n"); 
			sql.append("      COMPLAINT.CP_VIN VIN,\n"); 
			sql.append("      PRO.REGION_NAME PRO,\n"); 
			sql.append("      COMPLAINT.CP_DEAL_ORG CPDEALORGID,\n");
			sql.append("      COMPLAINT.CP_DEAL_DEALER CPDEALDEALER,\n");
			sql.append("      CITY.REGION_NAME CITY,\n"); 
			sql.append("      COMPLAINT.CP_MILEAGE MILEAGE,\n"); 
			sql.append("      VINUCODE.CODE_DESC VINUSE,\n"); 
			sql.append("      CPMODEL.GROUP_NAME MNAME,\n"); 
			sql.append("      TO_CHAR(COMPLAINT.CP_BUY_DATE, 'yyyy-MM-DD hh24:mi:ss') BUYDATE,\n"); 
			sql.append("      AUSER.NAME ACCISER,\n"); 
			sql.append("      TO_CHAR(COMPLAINT.CP_ACC_DATE, 'yyyy-MM-DD hh24:mi:ss') ACCDATE,\n"); 
			sql.append("      BIZCONT.CODE_DESC BIZCONT,\n"); 
			sql.append("      FAULTP.CODE_DESC FAULTP,\n"); 
			sql.append("      CPLEV.CODE_DESC CPLEV,\n"); 
			sql.append("      COMPLAINT.CP_LIMIT CPLIM,\n"); 
			sql.append("      TO_CHAR(COMPLAINT.CP_LIMIT + COMPLAINT.CP_TURN_DATE, 'yyyy-MM-DD hh24:mi:ss') SETCLOSEDATE,\n"); 
			sql.append("      TUSER.NAME TURNUSER,\n"); 
			sql.append("      TO_CHAR(COMPLAINT.CP_TURN_DATE, 'yyyy-MM-DD hh24:mi:ss') TURNDATE,\n"); 
			sql.append("      COMPLAINT.CP_CONTENT CPCONTENT,\n"); 
			sql.append("      COMPLAINT.CP_point_status,\n"); 
			sql.append("      COMPLAINT.CP_biz_type,\n"); 
			sql.append("      COMPLAINT.cp_deal_dealer,\n"); 
			//sql.append("      cpdo.name TUCOMP,\n"); 
			//wizard_lee 2014-04-23
//			sql.append("      Y.CR_CONTENT CLOSEREASE,\n"); 
//			
//			sql.append("         DECODE(COMPLAINT.Cp_Close_Date, NULL, '',\n"); 
//			sql.append("           CASE WHEN COMPLAINT.CP_SF="+Constant.PLEASED+" THEN '正常关闭'\n"); 
//			sql.append("                WHEN COMPLAINT.CP_SF="+Constant.YAWP+" THEN '不正常关闭'\n"); 
//			sql.append("                ELSE '' END) ISNORMALCLOSE, /*是否正常关闭*/\n");  
//			
//			sql.append("case when COMPLAINT.Cp_Status in("+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+") then\r\n" );
//			sql.append("  case when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_CLOSE+" then '及时关闭'\r\n" );
//			sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then '未及时关闭'\r\n" );
//			sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and trunc(X.BACK_DATE) <= trunc(nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0))) then '及时关闭'\r\n" );
//			sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and trunc(X.BACK_DATE) > trunc(nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0))) then '未及时关闭'\r\n" );
//			sql.append("          end\r\n" );
//			sql.append("end  ISTIMELYCLOSE, /*是否及时关闭*/");			
			sql.append("      CUSER.NAME CLOSEUSER,\n"); 
			sql.append("      TO_CHAR(COMPLAINT.CP_CLOSE_DATE, 'yyyy-MM-DD hh24:mi:ss') CLOSEDATE,\n"); 
			
			sql.append("   decode(complaint.CP_SF ,NULL,'',TO_CHAR(CEIL((Z.LASTORGDEALDATE - COMPLAINT.CP_TURN_DATE) * 24))) DURATIONCLOSE \n");
			
			sql.append(" FROM TT_CRM_INCOMING_CALL COMPLAINT\n"); 
			sql.append(" LEFT JOIN TT_CUSTOMER CUSTOMER ON CUSTOMER.CTM_ID = COMPLAINT.CP_CUS_ID\n"); 
			sql.append(" LEFT JOIN TM_REGION PRO ON PRO.REGION_CODE = COMPLAINT.CP_PROVINCE_ID\n"); 
			sql.append(" LEFT JOIN TM_REGION CITY ON CITY.REGION_CODE = COMPLAINT.CP_CITY_ID\n"); 
			sql.append(" LEFT JOIN TM_VHCL_MATERIAL_GROUP CPMODEL ON CPMODEL.GROUP_ID = COMPLAINT.CP_MODEL_ID\n"); 
			sql.append(" LEFT JOIN TC_CODE BIZCONT ON BIZCONT.CODE_ID = COMPLAINT.CP_BIZ_CONTENT\n"); 
			sql.append(" LEFT JOIN TC_CODE CPLEV ON CPLEV.CODE_ID = COMPLAINT.CP_LEVEL\n"); 
			sql.append(" LEFT JOIN TM_ORG DEALORG ON COMPLAINT.CP_DEAL_ORG = DEALORG.ORG_ID\n"); 
			sql.append(" LEFT JOIN TC_USER AUSER ON AUSER.USER_ID = COMPLAINT.CP_ACC_USER\n"); 
			sql.append(" LEFT JOIN TC_USER TUSER ON TUSER.USER_ID = COMPLAINT.CP_TURN_USER\n"); 
			sql.append(" LEFT JOIN TC_USER CUSER ON CUSER.USER_ID = COMPLAINT.CP_CLOSE_USER\n"); 
			sql.append(" LEFT JOIN TC_CODE VINUCODE ON VINUCODE.CODE_ID = COMPLAINT.CP_VIN_USE\n"); 
			sql.append(" LEFT JOIN TC_CODE FAULTP ON FAULTP.CODE_ID = COMPLAINT.FAULT_PART");
			//sql.append(" left join tt_crm_complaint_DEAL_OBJECT cpdo on cpdo.id = COMPLAINT.CP_DEAL_ORG ");
			sql.append(" LEFT JOIN (SELECT TO_CHAR(MAX(DR.CD_DATE), 'YYYY-MM-DD HH:MI:SS') LASTDEALDATE, DR.CP_ID, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CD_DATE,NULL)) BACK_DATE\n");
			sql.append("      FROM TT_CRM_INCOMING_DEAL_RECORD DR GROUP BY DR.CP_ID) X ON X.CP_ID = COMPLAINT.CP_ID");
			
			sql.append(" left join ( \r\n");
			sql.append(" SELECT R.CR_CONTENT, t.cp_id\r\n");
	
			sql.append("  FROM TT_CRM_INCOMING_CALL T\r\n");
			sql.append("  LEFT JOIN (SELECT T.*\r\n");
			sql.append("               FROM TT_CRM_INCOMING_RETURN_RECORD T\r\n");
			sql.append("              WHERE T.CR_DATE = (SELECT MAX(CRR.CR_DATE)\r\n");
			sql.append("                                   FROM TT_CRM_INCOMING_RETURN_RECORD CRR\r\n");
			sql.append("                                  WHERE CRR.CP_ID = "+cpid+")) R ON T.CP_ID =\r\n");
			sql.append("                                                                R.CP_ID\r\n");
			sql.append(" WHERE T.CP_CLOSE_DATE IS NOT NULL\r\n");
			sql.append("   AND T.CP_CLOSE_USER IS NOT NULL\r\n");
			sql.append("  ) Y on Y.cp_id = COMPLAINT.cp_id \r\n"); 

			sql.append("    LEFT JOIN (SELECT MAX(DRD.CREATE_DATE) LASTORGDEALDATE, DRD.CP_ID \n"); 
			sql.append("      FROM TT_CRM_INCOMING_DEAL_RECORD DRD where DRD.cp_status = "+Constant.COMPLAINT_STATUS_DOING_ALREADY+"  GROUP BY DRD.CP_ID) Z ON Z.CP_ID = COMPLAINT.CP_ID");


		sql.append("  where complaint.cp_id =  "+cpid);

  		return this.pageQueryMap(sql.toString(), null, this.getFunName());
	}

}
