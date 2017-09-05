package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import oracle.jdbc.Const;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtCrmComplaintDelayRecordPO;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class ComplaintAcceptDao extends BaseDao{

	private static final ComplaintAcceptDao dao = new ComplaintAcceptDao();
	
	public static final ComplaintAcceptDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public PageResult<Map<String,Object>> queryConsultInfo(String vin,String name,String tele,String accuser,
			String dateStart,String dateEnd,long userid,int pageSize,int curPage){
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select complaint.CP_ID CPID, complaint.cp_name CTMNAME,complaint.CP_PHONE PHONE,");
		sql.append(" to_char(complaint.CP_ACC_DATE,'yyyy-MM-dd hh24:mi') ACCDATE, tcuser.name ACCUSER,");
		sql.append(" complaint.CP_DEAL_MODE DEALMODE, complaint.CP_CONTENT CONTENT,'"+Constant.TYPE_CONSULT_NAME+"' BIZTYPE");
		sql.append(" from TT_CRM_COMPLAINT complaint ");
		sql.append(" left join tt_customer customer on customer.ctm_id = complaint.CP_CUS_ID ");
		sql.append(" left join tc_user tcuser on tcuser.user_id = complaint.CP_ACC_USER");
		// 艾春9.17添加 待处理咨询查询
		sql.append(" where  complaint.CP_BIZ_TYPE = '"+Constant.TYPE_CONSULT+"' and complaint.CP_STATUS =" +Constant.CONSULT_STATUS_WAIT );

		if(StringUtil.notNull(vin)){
			sql.append(" and complaint.CP_VIN like '%"+vin+"%'\n");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and complaint.cp_name like '%"+name+"%'\n");
		}
		if(StringUtil.notNull(tele)){
			sql.append(" and complaint.CP_PHONE like '%"+tele+"%'\n");
		}
		if(StringUtil.notNull(accuser)){
			sql.append(" and tcuser.name like '%"+accuser+"%' \n");
		}
		if(StringUtil.notNull(dateStart)){
			sql.append(" and complaint.CP_ACC_DATE >= to_date('"+dateStart+"','yyyy-MM-dd')\n");
		}
		if(StringUtil.notNull(dateEnd)){
			sql.append(" and complaint.CP_ACC_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
		}
		
		sql.append(" order by complaint.CP_ACC_DATE desc");
		
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	//咨询查询
	public PageResult<Map<String,Object>> queryConsultInfo(String vin,String name,String tele,String status,
			String type,String pro,String dealName,String cont,
			String dateStart,String dateEnd,boolean isAdmin,long userid,int pageSize,int curPage){
		
		StringBuffer sql = new StringBuffer();
		sql.append("select complaint.CP_ID CPID,\r\n");
		sql.append("       complaint.cp_name CTMNAME,\r\n");
		sql.append("       complaint.CP_PHONE PHONE,\r\n");
		sql.append("       to_char(complaint.CP_ACC_DATE, 'yyyy-MM-dd hh24:mi') ACCDATE,\r\n");
		sql.append("       tcuser.name ACCUSER,\r\n");
		sql.append("       complaint.CP_DEAL_MODE DEALMODE,\r\n");
		sql.append("       complaint.CP_STATUS CPSTATUS,\r\n");
		sql.append("       complaint.CP_CONTENT CONTENT,\r\n");
		sql.append("       tdr.CP_CONTENT CPCONTENT,\r\n");
		sql.append("       '"+Constant.TYPE_CONSULT_NAME+"' BIZTYPE,\r\n");
		sql.append("       complaint.update_date,\r\n");
		sql.append("       complaint.CREATE_DATE\r\n");
		sql.append("  from TT_CRM_COMPLAINT complaint\r\n");
		sql.append("  left join tt_customer customer\r\n");
		sql.append("    on customer.ctm_id = complaint.CP_CUS_ID\r\n");
		sql.append("  left join tc_user tcuser\r\n");
		sql.append("    on tcuser.user_id = complaint.CP_ACC_USER\r\n");
		sql.append("  left join (select tt.cp_id, tt.cp_content \r\n");
		sql.append("               from TT_CRM_COMPLAINT_DEAL_RECORD tt\r\n");
		sql.append("              inner join (select dr.cp_id, max(dr.create_date) cdate\r\n");
		sql.append("                           from TT_CRM_COMPLAINT_DEAL_RECORD dr\r\n");
		sql.append("                          group by dr.cp_id) bb\r\n");
		sql.append("                 on bb.cp_id = tt.cp_id\r\n");
		sql.append("                and bb.cdate = tt.create_date) tdr\r\n");
		sql.append("    on tdr.CP_ID = complaint.CP_ID"); 
		sql.append(" where  complaint.CP_BIZ_TYPE = '"+Constant.TYPE_CONSULT+"' ");
		
		if(StringUtil.notNull(vin)){
			sql.append(" and complaint.CP_VIN like '%"+vin+"%'\n");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and complaint.cp_name like '%"+name+"%'\n");
		}
		if(StringUtil.notNull(tele)){
			sql.append(" and complaint.CP_PHONE like '%"+tele+"%'\n");
		}
		if(StringUtil.notNull(status)){
			sql.append(" and complaint.CP_STATUS = '"+status+"' \n");
		}
		if(StringUtil.notNull(type)){
			sql.append(" and complaint.CP_BIZ_CONTENT = '"+type+"'\n");
		}
		if(StringUtil.notNull(pro)){
			sql.append(" and complaint.CP_PROVINCE_ID = '"+pro+"' \n");
		}
		if(StringUtil.notNull(dealName)){
			sql.append(" and tcuser.name like '%"+dealName+"%'\n");
		}
		if(StringUtil.notNull(cont)){
			sql.append(" and complaint.CP_CONTENT like '%"+cont+"%'\n");
		}	
		if(StringUtil.notNull(dateStart)){
			sql.append(" and complaint.CP_ACC_DATE >= to_date('"+dateStart+"','yyyy-MM-dd')\n");
		}
		if(StringUtil.notNull(dateEnd)){
			sql.append(" and complaint.CP_ACC_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
		}
		sql.append(" order by complaint.cp_acc_date desc ");
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	//详细信息查看 翻译转换了
	public Map<String, Object> queryConsultInfoWatch(long cpid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct complaint.CP_ID CPID,\r\n");
		sql.append("                complaint.CP_CONTENT CPCONT,\r\n");
		sql.append("                complaint.cp_name CTMNAME,\r\n");
		sql.append("                complaint.CP_PHONE PHONE,\r\n");
		sql.append("                complaint.CP_VIN VIN,\r\n");
		sql.append("                pro.REGION_NAME PRO,\r\n");
		sql.append("                city.REGION_NAME CITY,\r\n");
		sql.append("                complaint.CP_MILEAGE MILEAGE,\r\n");
		sql.append("                dealM.code_desc DEALMODE,\r\n");
		sql.append("                sgroup.group_name SGROUP,\r\n");
		sql.append("                mgroup.group_name MGROUP,\r\n");
		sql.append("                to_char(complaint.CP_BUY_DATE, 'yyyy-MM-dd hh24:mi:ss') BUYDATE,\r\n");
		sql.append("                to_char(complaint.CP_PRODUCT_DATE, 'yyyy-MM-dd  HH24:mi:ss') PRDATE,\r\n");
		sql.append("                '"+Constant.TYPE_CONSULT_NAME+"' BIZTYPE,\r\n");
		sql.append("                bizCon.code_desc BIZCONT,\r\n");
		sql.append("                cplevel.code_desc CPLEVEL,\r\n");
		sql.append("                nvl(orgObj.ORG_NAME, reObj.DEALER_ID) COBJ,\r\n");
		sql.append("                (select replace(za_concat('处理时间:' ||  to_char(dr.cd_date, 'yyyy-MM-dd')  ||\r\n");
		sql.append("                                                '处理内容:' || dr.cp_content),\r\n");
		sql.append("                                ',',\r\n");
		sql.append("                                '</br>')\r\n");
		sql.append("                   from (select *\r\n");
		sql.append("                           from TT_CRM_COMPLAINT_DEAL_RECORD\r\n");
		sql.append("                          where cp_id = '"+cpid+"'\r\n");
		sql.append("                          order by cd_date desc) dr) RCONT,\r\n");
		sql.append("                to_char(complaint.CP_ACC_DATE, 'yyyy-MM-dd  HH24:mi:ss') ACCDATE,\r\n");
		sql.append("                accuser.name ACCUSER,\r\n");
		sql.append("                tdr.CD_USER RUSER,\r\n");
		sql.append("                to_char(tdr.CD_DATE, 'yyyy-MM-dd  HH24:mi:ss') RDATE,\r\n");
		sql.append("                mrange.code_desc MILEAGERANGE,\r\n");
		sql.append("                vinuser.code_desc VINUSER\r\n");
		sql.append("  from TT_CRM_COMPLAINT complaint\r\n");
		sql.append("  left join tt_customer customer on customer.ctm_id = complaint.CP_CUS_ID\r\n");
		sql.append("  left join tm_region pro on pro.REGION_CODE = complaint.CP_PROVINCE_ID\r\n");
		sql.append("  left join tm_region city on city.REGION_CODE = complaint.CP_CITY_ID\r\n");
		sql.append("  left join tm_vhcl_material_group sgroup on sgroup.group_id = complaint.CP_SERIES_ID\r\n");
		sql.append("  left join tm_vhcl_material_group mgroup on mgroup.group_id = complaint.CP_MODEL_ID\r\n");
		sql.append("  left join tc_code cplevel on cplevel.code_id = complaint.CP_LEVEL\r\n");
		sql.append("  left join tc_code bizCon on bizCon.code_id = complaint.CP_BIZ_CONTENT\r\n");
		sql.append("  left join tm_org_custom orgObj on orgObj.ORG_ID = complaint.CP_OBJECT\r\n");
		sql.append("  left join (select tt.*\r\n");
		sql.append("               from TT_CRM_COMPLAINT_DEAL_RECORD tt\r\n");
		sql.append("              inner join (select dr.cp_id, max(dr.create_date) cdate\r\n");
		sql.append("                           from TT_CRM_COMPLAINT_DEAL_RECORD dr\r\n");
		sql.append("                          group by dr.cp_id\r\n");
		sql.append("                         having dr.cp_id = '"+cpid+"') bb\r\n");
		sql.append("                 on bb.cp_id = tt.cp_id\r\n");
		sql.append("                and bb.cdate = tt.create_date) tdr\r\n");
		sql.append("    on tdr.CP_ID = complaint.CP_ID\r\n");
		sql.append("  left join tm_dealer reObj on reObj.DEALER_ID = complaint.CP_OBJECT\r\n");
		sql.append("  left join tc_code mrange on mrange.code_id = complaint.CP_MILEAGE_RANGE\r\n");
		sql.append("  left join tc_code vinuser on vinuser.code_id = complaint.CP_VIN_USE\r\n");
		sql.append("  left join tc_code dealM on dealM.code_id = complaint.CP_DEAL_MODE\r\n");
		sql.append("  left join tc_user accuser on accuser.user_id = complaint.CP_ACC_USER\r\n");
		sql.append(" where complaint.CP_ID="+cpid);
		return this.pageQueryMap(sql.toString(), null, null);
	}
	
	public Map<String, Object> queryConsultInfo(long cpid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select complaint.CP_ID CPID,\r\n");
		sql.append("       complaint.CP_CONTENT CPCONT,\r\n");
		sql.append("       complaint.cp_name CTMNAME,\r\n");
		sql.append("       complaint.CP_PHONE PHONE,\r\n");
		sql.append("       complaint.CP_VIN VIN,\r\n");
		sql.append("       pro.REGION_NAME PRO,\r\n");
		sql.append("       city.REGION_NAME CITY,\r\n");
		sql.append("       complaint.CP_MILEAGE MILEAGE,\r\n");
		sql.append("       complaint.CP_DEAL_MODE DEALMODE,\r\n");
		sql.append("       sgroup.group_name SGROUP,\r\n");
		sql.append("       mgroup.group_name MGROUP,\r\n");
		sql.append("       to_char(complaint.CP_BUY_DATE, 'yyyy-MM-dd') BUYDATE,\r\n");
		sql.append("       complaint.CP_PRODUCT_DATE PRDATE,\r\n");
		sql.append("       complaint.CP_BIZ_TYPE BIZTYPE,\r\n");
		sql.append("       complaint.CP_BIZ_CONTENT BIZCONT,\r\n");
		sql.append("       tdr.CP_CONTENT RCONT,\r\n");
		sql.append("       to_char(complaint.CP_ACC_DATE, 'yyyy-MM-dd') ACCDATE,\r\n");
		sql.append("       complaint.CP_ACC_USER ACCUSER,\r\n");
		sql.append("       tdr.CD_USER RUSER,\r\n");
		sql.append("       to_char(tdr.CD_DATE, 'yyyy-MM-dd') RDATE,\r\n");
		sql.append("       mrange.code_desc MILEAGERANGE,\r\n");
		sql.append("       vinuser.code_desc VINUSER,\r\n");
		sql.append("       complaint.VAR VAR\r\n");
		sql.append("  from TT_CRM_COMPLAINT complaint\r\n");
		sql.append("  left join tt_customer customer\r\n");
		sql.append("    on customer.ctm_id = complaint.CP_CUS_ID\r\n");
		sql.append("  left join tm_region pro\r\n");
		sql.append("    on pro.REGION_CODE = complaint.CP_PROVINCE_ID\r\n");
		sql.append("  left join tm_region city\r\n");
		sql.append("    on city.REGION_CODE = complaint.CP_CITY_ID\r\n");
		sql.append("  left join tm_vhcl_material_group sgroup\r\n");
		sql.append("    on sgroup.group_id = complaint.CP_SERIES_ID\r\n");
		sql.append("  left join tm_vhcl_material_group mgroup\r\n");
		sql.append("    on mgroup.group_id = complaint.CP_MODEL_ID\r\n");
		sql.append("  left join tc_code cplevel\r\n");
		sql.append("    on cplevel.code_id = complaint.CP_LEVEL\r\n");
		sql.append("  left join tm_org_custom orgObj\r\n");
		sql.append("    on orgObj.ORG_ID = complaint.CP_OBJECT\r\n");
		sql.append("  left join (select tt.*\r\n");
		sql.append("               from TT_CRM_COMPLAINT_DEAL_RECORD tt\r\n");
		sql.append("              inner join (select dr.cp_id, max(dr.create_date) cdate\r\n");
		sql.append("                           from TT_CRM_COMPLAINT_DEAL_RECORD dr\r\n");
		sql.append("                          group by dr.cp_id\r\n");
		sql.append("                         having dr.cp_id = "+cpid+") bb\r\n");
		sql.append("                 on bb.cp_id = tt.cp_id\r\n");
		sql.append("                and bb.cdate = tt.create_date) tdr\r\n");
		sql.append("    on tdr.CP_ID = complaint.CP_ID\r\n");
		sql.append("  left join tm_region reObj\r\n");
		sql.append("    on reObj.REGION_CODE = complaint.CP_OBJECT\r\n");
		sql.append("  left join tc_code mrange\r\n");
		sql.append("    on mrange.code_id = complaint.CP_MILEAGE_RANGE\r\n");
		sql.append("  left join tc_code vinuser\r\n");
		sql.append("    on vinuser.code_id = complaint.CP_VIN_USE"); 
		sql.append(" where complaint.CP_ID="+cpid);
		return this.pageQueryMap(sql.toString(), null, null);
	}
	
	public PageResult<Map<String, Object>> queryWaitComplaintInfo(String vin,
			String name, String tele, String accuser, String dateStart,
			String dateEnd,String dealStart,String dealEnd,String status,
			String region,String pro,String state,String delaystatus,String advanced, String model, Integer pageSize, Integer curPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct customer.ctm_id CTMID,\r\n");
		sql.append("                complaint.CP_ID CPID,\r\n");
		sql.append("                complaint.cp_name CTMNAME,\r\n");
		sql.append("                complaint.CP_PHONE PHONE,\r\n");
		sql.append("                to_char(complaint.cp_cl_date, 'yyyy-MM-dd') yanqi,\r\n");
		sql.append("                to_char(complaint.CP_ACC_DATE, 'yyyy-MM-dd') ACCDATE,\r\n");
		sql.append("                accuser.name ACCUSER,\r\n");
		sql.append("                complaint.CP_STATUS STATUS,\r\n");
		sql.append("                complaint.CP_CONTENT CONTENT,\r\n");
		sql.append("                '"+Constant.TYPE_COMPLAIN_NAME+"' BIZTYPE,\r\n");
		sql.append("				complaint.UPDATE_DATE,\r\n ");
		sql.append("				complaint.CREATE_DATE, \r\n");
		sql.append("				complaint.CP_CL_STATUS CPCLSTATUS, \r\n");
		sql.append("				B.cp_defer_type CPDEFERTYPE \r\n");
		sql.append("  from TT_CRM_COMPLAINT complaint\r\n");
		sql.append("  left join tt_customer customer\r\n");
		sql.append("    on customer.ctm_id = complaint.CP_CUS_ID\r\n");
		sql.append("  left join tc_user accuser\r\n");
		sql.append("    on accuser.user_id = complaint.CP_ACC_USER\r\n");
		sql.append("  left join tm_vehicle v\r\n");
		sql.append("    on v.vin = complaint.cp_vin\r\n");
		sql.append("  left join (select tt.*\r\n");
		sql.append("               from TT_CRM_COMPLAINT_DEAL_RECORD tt\r\n");
		sql.append("              inner join (select dr.cp_id, max(dr.create_date) cdate\r\n");
		sql.append("                           from TT_CRM_COMPLAINT_DEAL_RECORD dr\r\n");
		sql.append("                          group by dr.cp_id) bb\r\n");
		sql.append("                 on bb.cp_id = tt.cp_id\r\n");
		sql.append("                and bb.cdate = tt.create_date) tdr\r\n");
		sql.append("    on tdr.CP_ID = complaint.CP_ID\r\n"); 
		
		sql.append("  left join (\r\n" );
		sql.append("	select cp_defer_type,cp_id from (\r\n" );
		sql.append("		select d.cp_defer_type,d.cp_id, rank() over(partition by d.cp_id order by d.create_date desc) mm from TT_CRM_COMPLAINT_DELAY_RECORD d )\r\n" );
		sql.append("		where mm = 1");
		sql.append("  ) B on B.CP_ID = COMPLAINT.CP_ID");
		
		sql.append(" where complaint.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+" \r\n" );
		
		sql.append("  and complaint.CP_STATUS in("+state+") ");
		
		if(StringUtil.notNull(region)){
			sql.append(" and complaint.CP_DEAL_ORG = "+ region );
		}
		if(StringUtil.notNull(pro)){
			sql.append(" and complaint.CP_PROVINCE_ID = "+pro);
		}
		if(StringUtil.notNull(model)){
			sql.append(" and complaint.CP_MODEL_ID = "+model);
		}
		if(StringUtil.notNull(vin)){
			sql.append(" and complaint.CP_VIN like '%"+vin+"%'\n");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and complaint.cp_name like '%"+name+"%'\n");
		}
		if(StringUtil.notNull(tele)){
			sql.append(" and complaint.CP_PHONE like '%"+tele+"%'\n");
		}
		if(StringUtil.notNull(accuser)){
			sql.append(" and accuser.name like '%"+accuser+"%'\n");
		}
		if(StringUtil.notNull(dateStart)){
			sql.append(" and complaint.CP_ACC_DATE >= to_date('"+dateStart+"','yyyy-MM-dd')\n");
		}
		if(StringUtil.notNull(dateEnd)){
			sql.append(" and complaint.CP_ACC_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
		}
		if(StringUtil.notNull(dealStart)){
			sql.append(" and tdr.CD_DATE >= to_date('"+dealStart+"','yyyy-MM-dd')\n");
		}
		if(StringUtil.notNull(dealEnd)){
			sql.append(" and tdr.CD_DATE <= to_date('"+dealEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
		}
		if(StringUtil.notNull(status) && status.equals("unClose")){
			sql.append(" and complaint.cp_status not in ('"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+"','"+Constant.COMPLAINT_STATUS_CLOSE+"') ");
		}else if(StringUtil.notNull(status) && !status.equals("unClose")){
			sql.append(" and complaint.cp_status = '"+status+"' ");
		}
		if(StringUtil.notNull(delaystatus) && Constant.NOT_APPLY.toString().equals(delaystatus)){
			sql.append(" and (complaint.CP_CL_STATUS = "+delaystatus+" or complaint.CP_CL_STATUS is null)");
		}else if(StringUtil.notNull(delaystatus) && "delayAdmin".equals(delaystatus)){
			sql.append(" and (complaint.CP_CL_STATUS = "+Constant.PASS_Manager_03+" and B.cp_defer_type = '"+Constant.DEFER_TYPE_02+"' )");
		}else if(StringUtil.notNull(delaystatus)){
			sql.append(" and complaint.CP_CL_STATUS = "+delaystatus);
		}
		if(StringUtil.notNull(advanced)){
			sql.append(" and complaint.cp_status in ('"+Constant.COMPLAINT_STATUS_WAIT+"','"+Constant.COMPLAINT_STATUS_DOING_ALREADY+"','"+Constant.COMPLAINT_STATUS_WAIT_CLOSE+"') ");
		}
		sql.append(" order by complaint.UPDATE_DATE desc,complaint.CREATE_DATE desc ");
		
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	//待回访查询
	public PageResult<Map<String, Object>> queryComplaintInfo(String vin,
			String name, String tele, String accuser, String dateStart,
			String dateEnd,String dealStart,String dealEnd,String userId,
			String advancedSearch,String region,String pro,String status, Integer pageSize, Integer curPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct customer.ctm_id CTMID,\r\n");
		sql.append("                complaint.CP_ID CPID,\r\n");
		sql.append("                complaint.cp_name CTMNAME,\r\n");
		sql.append("                complaint.CP_PHONE PHONE,\r\n");
		sql.append("                to_char(complaint.CP_ACC_DATE, 'yyyy-MM-dd') ACCDATE,\r\n");
		sql.append("                accuser.name ACCUSER,\r\n");
		sql.append("                complaint.CP_STATUS STATUS,\r\n");
		sql.append("                complaint.CP_CONTENT CONTENT,\r\n");
		sql.append("                '"+Constant.TYPE_COMPLAIN_NAME+"' BIZTYPE,\r\n");
		sql.append("				complaint.UPDATE_DATE,\r\n ");
		sql.append("				complaint.CREATE_DATE \r\n");
		sql.append("  from TT_CRM_COMPLAINT complaint\r\n");
		sql.append("  left join tt_customer customer\r\n");
		sql.append("    on customer.ctm_id = complaint.CP_CUS_ID\r\n");
		sql.append("  left join tc_user accuser\r\n");
		sql.append("    on accuser.user_id = complaint.CP_ACC_USER\r\n");
		sql.append("  left join tm_vehicle v\r\n");
		sql.append("    on v.vin = complaint.cp_vin\r\n");
		sql.append("  left join (select tt.*\r\n");
		sql.append("               from TT_CRM_COMPLAINT_DEAL_RECORD tt\r\n");
		sql.append("              inner join (select dr.cp_id, max(dr.create_date) cdate\r\n");
		sql.append("                           from TT_CRM_COMPLAINT_DEAL_RECORD dr\r\n");
		sql.append("                          group by dr.cp_id) bb\r\n");
		sql.append("                 on bb.cp_id = tt.cp_id\r\n");
		sql.append("                and bb.cdate = tt.create_date) tdr\r\n");
		sql.append("    on tdr.CP_ID = complaint.CP_ID\r\n"); 
		sql.append(" where complaint.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+" \r\n" );
		sql.append(" and complaint.CP_STATUS = "+Constant.COMPLAINT_STATUS_WAIT_FEEDBACK+" \r\n" );
		
		if(advancedSearch.equals("true")){
			
			if(StringUtil.notNull(region)){
				sql.append(" and complaint.CP_DEAL_ORG = "+ region );
			}
			if(StringUtil.notNull(pro)){
				sql.append(" and complaint.CP_PROVINCE_ID = "+pro);
			}
		}else if(advancedSearch.equals("")){
			//sql.append(" and complaint.CP_DEAL_USER = '"+userId+"' ");
		}
		if(StringUtil.notNull(vin)){
			sql.append(" and complaint.CP_VIN like '%"+vin+"%'\n");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and complaint.cp_name like '%"+name+"%'\n");
		}
		if(StringUtil.notNull(tele)){
			sql.append(" and complaint.CP_PHONE like '%"+tele+"%'\n");
		}
		if(StringUtil.notNull(accuser)){
			sql.append(" and accuser.name like '%"+accuser+"%'\n");
		}
		if(StringUtil.notNull(dateStart)){
			sql.append(" and complaint.CP_ACC_DATE >= to_date('"+dateStart+"','yyyy-MM-dd')\n");
		}
		if(StringUtil.notNull(dateEnd)){
			sql.append(" and complaint.CP_ACC_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
		}
		if(StringUtil.notNull(dealStart)){
			sql.append(" and tdr.CD_DATE >= to_date('"+dealStart+"','yyyy-MM-dd')\n");
		}
		if(StringUtil.notNull(dealEnd)){
			sql.append(" and tdr.CD_DATE <= to_date('"+dealEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
		}
		if(StringUtil.notNull(status)){
			sql.append(" and complaint.CP_STATUS = '"+status+"'\n");
		}
		sql.append(" order by complaint.UPDATE_DATE desc,complaint.CREATE_DATE desc ");
		
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	
	public Map<String, Object> queryComplaintInfo(long cpid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct complaint.CP_ID CPID,\r\n");
		sql.append("                complaint.CP_CONTENT CPCONT,\r\n");
		sql.append("                complaint.cp_name CTMNAME,\r\n");
		sql.append("                complaint.CP_PHONE PHONE,\r\n");
		sql.append("                complaint.CP_VIN VIN,\r\n");
		sql.append("                pro.REGION_NAME PRO,\r\n");
		sql.append("                city.REGION_NAME CITY,\r\n");
		sql.append("                pro.REGION_CODE PROCODE,\r\n");
		sql.append("                city.REGION_CODE CITYCODE,\r\n");
		sql.append("                complaint.CP_MILEAGE MILEAGE,\r\n");
		sql.append("                complaint.CP_DEAL_MODE DEALMODE,\r\n");
		sql.append("                sgroup.group_name SGROUP,\r\n");
		sql.append("                mgroup.group_name MGROUP,\r\n");
		sql.append("                to_char(complaint.CP_BUY_DATE, 'yyyy-MM-dd') BUYDATE,\r\n");
		sql.append("                complaint.CP_PRODUCT_DATE PRDATE,\r\n");
		sql.append("                '"+Constant.TYPE_COMPLAIN+"' BIZTYPE,\r\n");
		sql.append("                bizCon.code_id BIZCONT,\r\n");
		sql.append("                cplevel.code_id CPLEVEL,\r\n");
		sql.append("				complaint.CP_LIMIT CPLIMIT,\r\n");
		sql.append("                complaint.CP_OBJECT_ORG COBJ,\r\n");
		sql.append("                complaint.CP_OBJECT_SMALL_ORG SOBJ,\r\n");
		sql.append("                complaint.CP_OBJECT VCOBJ,\r\n");
		sql.append("                (select replace(za_concat('处理时间:' || dr.cd_date ||\r\n");
		sql.append("                                                '处理内容:' || dr.cp_content),\r\n");
		sql.append("                                ',',\r\n");
		sql.append("                                '</br>')\r\n");
		sql.append("                   from (select *\r\n");
		sql.append("                           from TT_CRM_COMPLAINT_DEAL_RECORD\r\n");
		sql.append("                          where cp_id = '"+cpid+"'\r\n");
		sql.append("                          order by cd_date desc) dr) RCONT,\r\n");
		sql.append("                to_char(complaint.CP_ACC_DATE, 'yyyy-MM-dd') ACCDATE,\r\n");
		sql.append("                complaint.CP_ACC_USER ACCUSER,\r\n");
		sql.append("                tdr.CD_USER RUSER,\r\n");
		sql.append("                to_char(tdr.CD_DATE, 'yyyy-MM-dd') RDATE,\r\n");
		sql.append("                mrange.code_desc MILEAGERANGE,\r\n");
		sql.append("                vinuser.code_desc VINUSER\r\n");
		sql.append("  from TT_CRM_COMPLAINT complaint\r\n");
		sql.append("  left join tt_customer customer\r\n");
		sql.append("    on customer.ctm_id = complaint.CP_CUS_ID\r\n");
		sql.append("  left join tm_region pro\r\n");
		sql.append("    on pro.REGION_CODE = complaint.CP_PROVINCE_ID\r\n");
		sql.append("  left join tm_region city\r\n");
		sql.append("    on city.REGION_CODE = complaint.CP_CITY_ID\r\n");
		sql.append("  left join tm_vhcl_material_group sgroup\r\n");
		sql.append("    on sgroup.group_id = complaint.CP_SERIES_ID\r\n");
		sql.append("  left join tm_vhcl_material_group mgroup\r\n");
		sql.append("    on mgroup.group_id = complaint.CP_MODEL_ID\r\n");
		sql.append("  left join tc_code cplevel\r\n");
		sql.append("    on cplevel.code_id = complaint.CP_LEVEL\r\n");
		sql.append("  left join tc_code bizCon\r\n");
		sql.append("    on bizCon.code_id = complaint.CP_BIZ_CONTENT\r\n");
		sql.append("  left join (select tt.*\r\n");
		sql.append("               from TT_CRM_COMPLAINT_DEAL_RECORD tt\r\n");
		sql.append("              inner join (select dr.cp_id, max(dr.create_date) cdate\r\n");
		sql.append("                           from TT_CRM_COMPLAINT_DEAL_RECORD dr\r\n");
		sql.append("                          group by dr.cp_id\r\n");
		sql.append("                         having dr.cp_id = "+cpid+") bb\r\n");
		sql.append("                 on bb.cp_id = tt.cp_id\r\n");
		sql.append("                and bb.cdate = tt.create_date) tdr\r\n");
		sql.append("    on tdr.CP_ID = complaint.CP_ID\r\n");
		sql.append("  left join tc_code mrange\r\n");
		sql.append("    on mrange.code_id = complaint.CP_MILEAGE_RANGE\r\n");
		sql.append("  left join tc_code vinuser\r\n");
		sql.append("    on vinuser.code_id = complaint.CP_VIN_USE"); 
		sql.append(" where complaint.CP_ID="+cpid);
		return this.pageQueryMap(sql.toString(), null, null);
	}
	
	//投诉受理翻译
	public Map<String, Object> queryComplaintInformation(long cpid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct complaint.CP_ID CPID,\r\n");
		sql.append("                complaint.CP_CONTENT CPCONT,\r\n");
		sql.append("                complaint.cp_name CTMNAME,\r\n");
		sql.append("                complaint.CP_PHONE PHONE,\r\n");
		sql.append("                complaint.CP_VIN VIN,\r\n");
		sql.append("                faultpart.code_desc FAULTPART,\r\n");
		sql.append("                pro.REGION_CODE PRO,\r\n");
		sql.append("                city.REGION_NAME CITY,\r\n");
		sql.append("                complaint.CP_MILEAGE MILEAGE,\r\n");
		sql.append("                complaint.CP_DEAL_MODE DEALMODE,\r\n");
		sql.append("                sgroup.group_name SGROUP,\r\n");
		sql.append("                mgroup.group_name MGROUP,\r\n");
		sql.append("                to_char(complaint.CP_BUY_DATE, 'yyyy-MM-dd') BUYDATE,\r\n");
		sql.append("                complaint.CP_PRODUCT_DATE PRDATE,\r\n");
		sql.append("                '"+Constant.TYPE_COMPLAIN_NAME+"' BIZTYPE,\r\n");
		sql.append("                bizCon.code_desc BIZCONT,\r\n");
		sql.append("                cplevel.code_desc CPLEVEL,\r\n");
		sql.append("				complaint.CP_LIMIT CPLIMIT,\r\n");
		sql.append("                nvl(orgObj.ORG_NAME, reObj.DEALER_NAME) COBJ,\r\n");
		sql.append("                (select replace(za_concat('处理时间:' || dr.cd_date ||\r\n");
		sql.append("                                                '处理内容:' || dr.cp_content),\r\n");
		sql.append("                                ',',\r\n");
		sql.append("                                '</br>')\r\n");
		sql.append("                   from (select *\r\n");
		sql.append("                           from TT_CRM_COMPLAINT_DEAL_RECORD\r\n");
		sql.append("                          where cp_id = '"+cpid+"'\r\n");
		sql.append("                          order by cd_date desc) dr) RCONT,\r\n");
		sql.append("                to_char(complaint.CP_ACC_DATE, 'yyyy-MM-dd') ACCDATE,\r\n");
		sql.append("                complaint.CP_ACC_USER ACCUSER,\r\n");
		sql.append("                tdr.CD_USER RUSER,\r\n");
		sql.append("                to_char(tdr.CD_DATE, 'yyyy-MM-dd') RDATE,\r\n");
		sql.append("                mrange.code_desc MILEAGERANGE,\r\n");
		sql.append("                vinuser.code_desc VINUSER\r\n");
		sql.append("  from TT_CRM_COMPLAINT complaint\r\n");
		sql.append("  left join tt_customer customer\r\n");
		sql.append("    on customer.ctm_id = complaint.CP_CUS_ID\r\n");
		sql.append("  left join tm_region pro\r\n");
		sql.append("    on pro.REGION_CODE = complaint.CP_PROVINCE_ID\r\n");
		sql.append("  left join tm_region city\r\n");
		sql.append("    on city.REGION_CODE = complaint.CP_CITY_ID\r\n");
		sql.append("  left join tm_vhcl_material_group sgroup\r\n");
		sql.append("    on sgroup.group_id = complaint.CP_SERIES_ID\r\n");
		sql.append("  left join tm_vhcl_material_group mgroup\r\n");
		sql.append("    on mgroup.group_id = complaint.CP_MODEL_ID\r\n");
		sql.append("  left join tc_code cplevel\r\n");
		sql.append("    on cplevel.code_id = complaint.CP_LEVEL\r\n");
		sql.append("  left join tc_code bizCon\r\n");
		sql.append("    on bizCon.code_id = complaint.CP_BIZ_CONTENT\r\n");
		sql.append("  left join tm_org_custom orgObj\r\n");
		sql.append("    on orgObj.ORG_ID = complaint.CP_OBJECT\r\n");
		sql.append("  left join (select tt.*\r\n");
		sql.append("               from TT_CRM_COMPLAINT_DEAL_RECORD tt\r\n");
		sql.append("              inner join (select dr.cp_id, max(dr.create_date) cdate\r\n");
		sql.append("                           from TT_CRM_COMPLAINT_DEAL_RECORD dr\r\n");
		sql.append("                          group by dr.cp_id\r\n");
		sql.append("                         having dr.cp_id = "+cpid+") bb\r\n");
		sql.append("                 on bb.cp_id = tt.cp_id\r\n");
		sql.append("                and bb.cdate = tt.create_date) tdr\r\n");
		sql.append("    on tdr.CP_ID = complaint.CP_ID\r\n");
		sql.append("  left join tm_dealer reObj\r\n");
		sql.append("    on reObj.DEALER_ID = complaint.CP_OBJECT\r\n");
		sql.append("  left join tc_code mrange\r\n");
		sql.append("    on mrange.code_id = complaint.CP_MILEAGE_RANGE\r\n");
		sql.append("  left join tc_code vinuser\r\n");
		sql.append("    on vinuser.code_id = complaint.CP_VIN_USE"); 
		sql.append("  left join tc_code faultpart\r\n");
		sql.append("    on faultpart.code_id = complaint.FAULT_PART"); 
		sql.append(" where complaint.CP_ID="+cpid);
		return this.pageQueryMap(sql.toString(), null, null);
	}
	
	
	/**
	 * 查询处理结果数据
	 * @param cpid
	 * @return
	 */
	public List<Map<String, Object>> queryDealRecord(long cpid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select to_char(t.cd_date, 'yyyy-MM-dd hh24:mi') CDDATE,\r\n");
		sql.append("       t.cp_content CDCONT,\r\n");
		sql.append("       t.cd_user USERNAME,\r\n");
		sql.append("       t.CP_NEXT_DEAL_ID USERID,\r\n");//2014-04-22 增加处理人ID
		sql.append("       nvl(a.org_name,nvl(b.dealer_name,nvl(c.org_name,d.name))) NEXTNAME\r\n");
		sql.append("  from TT_CRM_COMPLAINT_DEAL_RECORD t\r\n");
		sql.append("  left join tm_org a on a.org_id = t.CP_NEXT_DEAL_ID \r\n");
		sql.append("  left join tm_dealer b on b.dealer_id = t.CP_NEXT_DEAL_ID \r\n");
		sql.append("  left join tm_org_custom c on c.org_id = t.CP_NEXT_DEAL_ID \r\n");
		sql.append("  left join tc_user d on d.user_id = t.CP_NEXT_DEAL_ID \r\n");
		sql.append(" where t.cp_id = "+cpid);
		sql.append(" order by t.cd_date desc"); 
		return this.pageQuery(sql.toString(), null, null);
	}
	/**
	 * 查询用户回访数据
	 * @param cpid
	 * @return
	 */
	public List<Map<String, Object>> queryReturnRecord(long cpid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select to_char(t.CR_DATE, 'yyyy-MM-dd hh24:mi') CRDATE, CR_ID CRID, \r\n");
		sql.append("       t.CR_CONTENT CRCONT,\r\n");
		sql.append("       tcode.code_desc CONFIRMOPTION,\r\n");
		sql.append("       t.cr_user CRUSER\r\n");
		sql.append("  from TT_CRM_COMPLAINT_RETURN_RECORD t\r\n");
		sql.append("  left join tc_code tcode\r\n");
		sql.append("    on tcode.code_id = t.cr_confirm_opinion"); 
		sql.append(" where t.cp_id = "+cpid); 
		sql.append(" order by t.CR_DATE desc"); 
		return this.pageQuery(sql.toString(), null, null);
	}
	/**
	 * 查询申请审核记录
	 * @param parseLong
	 * @return
	 */
	public List<Map<String, Object>> queryVerifyRecord(long cpid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select to_char(t.CL_DATE, 'yyyy-MM-dd') CLDATE,\r\n");
		sql.append("       case when t.CL_DATE-to_date(to_char(NVL(complaint.CP_LIMIT,0) + complaint.CP_TURN_DATE,'yyyy-MM-dd'),'yyyy-MM-dd') < 0 then 0 ");
		sql.append("            else t.CL_DATE-to_date(to_char(NVL(complaint.CP_LIMIT,0) + complaint.CP_TURN_DATE,'yyyy-MM-dd'),'yyyy-MM-dd') end DAYS,\r\n");
		sql.append("       t.CL_CONTENT CLCONT,\r\n");
		sql.append("       t.CL_USER CLUSER,\r\n");
		sql.append("       to_char(t.CL_VERIFY_DATE, 'yyyy-MM-dd hh24:mi') CLVERIFYDATE,\r\n");
		sql.append("       t.CL_VERIFY_CONTENT CLVERIFYCONTENT,\r\n");
		sql.append("       t.CL_VERIFY_USER CLVERIFYUSER,\r\n");
		sql.append("       t.cp_defer_type  CPDEFERTYPE ,\r\n");
		sql.append("       tcode.code_desc CLVERIFYSTATUS, \r\n");
		// 艾春 2013.11.26 修改为延期主表上的第一次申请时刻 CP_CL_ONCE_DATE, 2013.12.3 修改为每次延期的申请时刻
//		sql.append("       to_char(complaint.CP_CL_ONCE_DATE, 'yyyy-MM-dd hh24:mi') CREATEDATE, \r\n");
		sql.append("       to_char(m.CREATE_DATE, 'yyyy-MM-dd hh24:mi') CREATEDATE, \r\n");
		sql.append("       nvl(a.org_name,nvl(b.dealer_name,nvl(c.org_name,d.name))) \r\n");
		sql.append("       || case when t.CP_NEXT_DEAL_ID = 2013070519381898 and t.CL_VERIFY_STATUS = "+Constant.PASS_Manager_01+" then '(审核)' \r\n");
		sql.append("               when t.CP_NEXT_DEAL_ID = 2013070519381898 and t.CL_VERIFY_STATUS in ("+Constant.ALREADY_APPLY+","+Constant.PASS_APPLY+") then '(初审)' \r\n");
		sql.append("               else '' end NEXTNAME \r\n");
		sql.append("  from TT_CRM_COMPLAINT_DELAY_RECORD t\r\n");
		sql.append("  left join TT_CRM_COMPLAINT complaint on complaint.cp_id = t.cp_id");
		sql.append("  left join tc_code tcode\r\n");
		sql.append("    on tcode.code_id = t.CL_VERIFY_STATUS"); 
		
		sql.append("  left join tm_org a on a.org_id = t.CP_NEXT_DEAL_ID \r\n");
		sql.append("  left join tm_dealer b on b.dealer_id = t.CP_NEXT_DEAL_ID \r\n");
		sql.append("  left join tm_org_custom c on c.org_id = t.CP_NEXT_DEAL_ID \r\n");
		sql.append("  left join tc_user d on d.user_id = t.CP_NEXT_DEAL_ID \r\n");
		// 艾春 2013.12.3 修改延期申请时刻取值
		sql.append("  LEFT JOIN (SELECT NVL(TO_CHAR(r.CL_DATE, 'yyyy-mm-dd'), '0') CL_DATE, r.CP_DEFER_TYPE, MIN(r.CREATE_DATE) CREATE_DATE \r\n");
		sql.append("  FROM TT_CRM_COMPLAINT_DELAY_RECORD r WHERE r.CP_ID = "+cpid+" GROUP BY r.CL_DATE, r.CP_DEFER_TYPE) M  \r\n");
		sql.append("  ON M.CL_DATE = NVL(TO_CHAR(t.CL_DATE, 'yyyy-mm-dd'), '0') AND m.CP_DEFER_TYPE = t.CP_DEFER_TYPE \r\n");
		// 艾春 2013.12.3 修改延期申请时刻取值
		sql.append(" where t.cp_id = "+cpid);
		sql.append(" order by t.cl_id desc ");
		return this.pageQuery(sql.toString(), null, null);
	}
	/**
	 * 查询所有的咨询 
	 * @param vin
	 * @param name
	 * @param tele
	 * @param status
	 * @param type
	 * @param pro
	 * @param dealName
	 * @param cont
	 * @param dateStart
	 * @param dateEnd
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> queryConsultInfo(String vin, String name,
			String tele, String status, String type, String pro,
			String dealName, String cont, String dateStart, String dateEnd,boolean isAdmin,long userid) {
		StringBuffer sql = new StringBuffer();	
		sql.append("select complaint.CP_ID CPID,\r\n");
		sql.append("       complaint.cp_name CTMNAME,\r\n");
		sql.append("       complaint.CP_PHONE PHONE,\r\n");
		sql.append("       to_char(complaint.CP_ACC_DATE, 'yyyy-MM-dd') ACCDATE,\r\n");
		sql.append("       tcuser.name ACCUSER,\r\n");
		sql.append("       complaint.CP_DEAL_MODE DEALMODE,\r\n");
		sql.append("       complaint.CP_STATUS CPSTATUS,\r\n");
		sql.append("       cc.code_desc status_name,\r\n");
		sql.append("       complaint.CP_CONTENT CONTENT,\r\n");
		sql.append("       tdr.CP_CONTENT CPCONTENT,\r\n");
		sql.append("       '"+Constant.TYPE_CONSULT_NAME+"' BIZTYPE,\r\n");
		sql.append("       complaint.update_date,\r\n");
		sql.append("       complaint.CREATE_DATE\r\n");
		sql.append("  from TT_CRM_COMPLAINT complaint\r\n");
		sql.append("  left join tc_code cc on cc.code_id = complaint.CP_STATUS\n");
		sql.append("  left join tt_customer customer\r\n");
		sql.append("    on customer.ctm_id = complaint.CP_CUS_ID\r\n");
		sql.append("  left join tc_user tcuser\r\n");
		sql.append("    on tcuser.user_id = complaint.CP_ACC_USER\r\n");
		sql.append("  left join (select tt.*\r\n");
		sql.append("               from TT_CRM_COMPLAINT_DEAL_RECORD tt\r\n");
		sql.append("              inner join (select dr.cp_id, max(dr.create_date) cdate\r\n");
		sql.append("                           from TT_CRM_COMPLAINT_DEAL_RECORD dr\r\n");
		sql.append("                          group by dr.cp_id) bb\r\n");
		sql.append("                 on bb.cp_id = tt.cp_id\r\n");
		sql.append("                and bb.cdate = tt.create_date) tdr\r\n");
		sql.append("    on tdr.CP_ID = complaint.CP_ID"); 
		sql.append(" where  complaint.CP_BIZ_TYPE = '"+Constant.TYPE_CONSULT+"' ");
		
		if(StringUtil.notNull(vin)){
			sql.append(" and complaint.CP_VIN like '%"+vin+"%'\n");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and complaint.cp_name like '%"+name+"%'\n");
		}
		if(StringUtil.notNull(tele)){
			sql.append(" and complaint.CP_PHONE like '%"+tele+"%'\n");
		}
		if(StringUtil.notNull(status)){
			sql.append(" and complaint.CP_STATUS = '"+status+"' \n");
		}
		if(StringUtil.notNull(type)){
			sql.append(" and complaint.CP_BIZ_CONTENT = '"+type+"'\n");
		}
		if(StringUtil.notNull(pro)){
			sql.append(" and complaint.CP_PROVINCE_ID = '"+pro+"' \n");
		}
		if(StringUtil.notNull(dealName)){
			sql.append(" and tcuser.name like '%"+dealName+"%'\n");
		}
		if(StringUtil.notNull(cont)){
			sql.append(" and complaint.CP_CONTENT like '%"+cont+"%'\n");
		}	
		if(StringUtil.notNull(dateStart)){
			sql.append(" and complaint.CP_ACC_DATE >= to_date('"+dateStart+"','yyyy-MM-dd')\n");
		}
		if(StringUtil.notNull(dateEnd)){
			sql.append(" and complaint.CP_ACC_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
		}
		sql.append(" order by complaint.CP_ACC_DATE desc ");
		return this.pageQuery(sql.toString(), null, getFunName());
	}

	public PageResult<Map<String, Object>> queryComplaintInfo(String cpNo, String vin,
			String name, String tele, String level,String dealUser, String accUser, String biztype,
			String status, String dealStart, String dealEnd, String dateStart,
			String dateEnd, String region, String pro, String checkStart,
			String checklEnd,boolean isAdmin,String CP_START_DATE,String CP_END_DATE,String KH_START_DATE,String KH_END_DATE,
			long userid,String delaystatus,String repeatComplaint,String is_close,String is_one_close,String model, Integer pageSize, Integer curPage) {
//		StringBuffer sql = new StringBuffer();
//		// 艾春 9.23 修改 投诉查询语句
//		sql.append(" SELECT COMPLAINT.CP_NO        CPNO, /*单号*/\n");
//		sql.append("         COMPLAINT.CP_ID        CPID, /*ID*/\n"); 
//		sql.append("         COMPLAINT.CP_STATUS    STATUS, /*状态*/\n"); 
//		sql.append("         COMPLAINT.CP_CL_STATUS CLSTATUS, /*延期状态*/\n"); 
//		sql.append("         COMPLAINT.CP_LEVEL     CPLEVEL, /*需转换*/\n"); 
//		sql.append("         COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n"); 
//		sql.append("         TRP.REGION_NAME      PRO, /*省份*/\n"); 
//		sql.append("         TRC.REGION_NAME      CITY, /*城市*/\n"); 
//		sql.append("         COMPLAINT.CP_NAME    CTMNAME, /*客户名称*/\n"); 
//		sql.append("         COMPLAINT.CP_CUS_ID  CTMID, /*客户ID*/\n"); 
//		sql.append("         COMPLAINT.CP_PHONE   PHONE,  /*电话*/\n"); 
//		sql.append("         COMPLAINT.CP_CONTENT CPCONT,  /*内容类型*/\n"); 
//		sql.append("         NVL(ORGOBJ.ORG_NAME, REOBJ.DEALER_NAME) CPOBJECT,/*抱怨对象*/\n"); 
//		sql.append("         COMPLAINT.CREATE_DATE CREATEDATE,/*创建日期*/\n"); 
//		sql.append("         ACUSER.NAME ACUSER,/*受理人*/\n"); 
//		sql.append("         SGROUP.GROUP_NAME SNAME,/*车种*/\n"); 
//		sql.append("         COMPLAINT.CP_VIN VIN,\n"); 
//		sql.append("         TO_CHAR(COMPLAINT.CP_BUY_DATE, 'yyyy-MM-dd hh24:mi:ss') BDATE,/*购买日期*/\n"); 
//		sql.append("         COMPLAINT.CP_MILEAGE CPMILEAGE, /*行驶里程*/\n"); 
//		sql.append("         COMPLAINT.CP_MILEAGE_RANGE, /*需转换*/\n"); 
//		sql.append("         ORGPRO.ORG_NAME ORGNAME,/*大区*/\n"); 
//		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DEALUSER,/*处理人*/\n"); 
//		sql.append("         COMPLAINT.CP_ACC_DATE  CPACCDATE,/*受理日期*/\n"); 
//		sql.append("         COMPLAINT.CP_TURN_DATE TURNDATE,/*转出日期*/\n"); 
//		sql.append("         COMPLAINT.CP_SF CPSF, /*需转换*/\n"); 
//		sql.append("         case when COMPLAINT.CP_CL_ONCE_DATE is null then ''      ");
//		sql.append("         when  COMPLAINT.CP_CL_ONCE_DATE is not null and COMPLAINT.CP_CL_ONCE_DATE <= (COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT)  and COMPLAINT.CP_CLOSE_DATE <= COMPLAINT.CP_CL_DATE and COMPLAINT.CP_STATUS in (95291005,95291007) then '正常'    ");
//		sql.append("         when  COMPLAINT.CP_CL_ONCE_DATE is not null and COMPLAINT.CP_CL_ONCE_DATE > (COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT) and COMPLAINT.CP_STATUS in (95291005,95291007) then '申请超期'    ");
//		sql.append("         when  COMPLAINT.CP_CL_ONCE_DATE is not null  and COMPLAINT.CP_CLOSE_DATE > COMPLAINT.CP_CL_DATE  and COMPLAINT.CP_STATUS in (95291005,95291007) then '关闭超期' ");
//		sql.append("         when  COMPLAINT.CP_CL_ONCE_DATE is not null and COMPLAINT.CP_STATUS not  in (95291005,95291007) then '未关闭超期'  end    ");
//		sql.append("       DELAY_TYPE,  COMPLAINT.CP_CLOSE_DATE CPCDATE, /*关闭日期*/\n"); 
//		// zhangyu 加上已关闭和强制关闭才加上时长
//		sql.append("   decode(complaint.CP_SF ,NULL,'',TO_CHAR(round((x.LASTORGDEALDATE - COMPLAINT.CP_TURN_DATE) * 24,2))) DEALTIME,");
//		sql.append("         COMPLAINT.CP_LIMIT CPLIMIT, /*规定时限*/\n"); 
//
//		sql.append("ceil(\r\n" );
//		sql.append(" case when COMPLAINT.Cp_Cl_Date is null then 0\r\n" );
//		sql.append("      else case when COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) >= COMPLAINT.Cp_Cl_Once_Date\r\n" );
//		sql.append("                then case when COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) >0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) else 0 end\r\n" );
//		sql.append("                else case when COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date>0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date else 0 end  end\r\n" );
//		sql.append("      end\r\n" );
//		sql.append(")DELAYDATE,/*延期天数(天) 取整*/");
//
//		sql.append("       round(case when nvl(X.BACK_DATE,sysdate)-(nvl(COMPLAINT.CP_CL_DATE,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT, 0))) >0 \r\n" );
//		sql.append("                  then nvl(X.BACK_DATE,sysdate)-(nvl(COMPLAINT.CP_CL_DATE,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT, 0)))  ");
//		sql.append("                  else null end) EXCEEDDATE, /*超期天数(天) 取整*/\r\n" );
//		
//		sql.append("         /*COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT SHOULDCLOSETIME, */\n"); 
//		sql.append("         /*CEIL((COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT - SYSDATE)*24) OWNTIME, */\n"); 
//
//		sql.append("case when COMPLAINT.Cp_Close_Date is null then\r\n" );
//		sql.append("        case when sysdate<=nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '及时关闭'\r\n" );
//		sql.append("        when sysdate>nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '未及时关闭'\r\n" );
//		sql.append("        end\r\n" );
//		sql.append("else case when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_CLOSE+" then '及时关闭'\r\n" );
//		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date>COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then '未及时关闭'\r\n" );
//		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE <= nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '及时关闭'\r\n" );
//		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE > nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '未及时关闭'\r\n" );
//		sql.append("          end\r\n" );
//		sql.append("end  ISTIMELYCLOSE, /*是否及时关闭*/");
//
//		sql.append("         COMPLAINT.CP_IS_ONCE_SF CPISONCESF, /*需转换*/\n"); 
//		sql.append("         DECODE(COMPLAINT.Cp_Close_Date, NULL, '',\n"); 
//		sql.append("           CASE WHEN COMPLAINT.CP_SF="+Constant.PLEASED+" THEN '正常关闭'\n"); 
//		sql.append("                WHEN COMPLAINT.CP_SF="+Constant.YAWP+" THEN '不正常关闭'\n"); 
//		sql.append("                ELSE '' END) ISNORMALCLOSE, /*是否正常关闭*/\n"); 
//		sql.append("         X.BACK_DATE LASTDEALDATE, /*最终反馈时间*/\n"); 
//		sql.append("         COMPLAINT.CP_STATUS CPSTATUS, /*状态*/\n"); 
//		// 艾春 2013.12.2 显示延期次数统计字段
//		sql.append("         Y.TIMES, /*延期次数*/\n"); 
//		// 艾春 2013.12.2 显示延期次数统计字段
//		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DUSER /*当前处理人*/\n"); 
//		sql.append("    FROM TT_CRM_COMPLAINT COMPLAINT\n"); 
//		sql.append("    LEFT JOIN TM_REGION TRP ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID AND TRP.REGION_TYPE = "+Constant.REGION_TYPE_02+"\n"); 
//		sql.append("    LEFT JOIN TM_REGION TRC ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID AND TRP.REGION_TYPE = "+Constant.REGION_TYPE_03+"\n"); 
//		sql.append("    LEFT JOIN tm_org_custom ORGOBJ ON ORGOBJ.ORG_ID = COMPLAINT.CP_OBJECT /*报怨对象处*/\n"); 
//		sql.append("    LEFT JOIN TM_DEALER REOBJ ON REOBJ.DEALER_ID = COMPLAINT.CP_OBJECT \n"); 
//		sql.append("    LEFT JOIN TC_USER ACUSER ON ACUSER.USER_ID = COMPLAINT.CP_ACC_USER\n"); 
//		sql.append("    LEFT JOIN TM_VHCL_MATERIAL_GROUP SGROUP ON SGROUP.GROUP_ID = COMPLAINT.CP_SERIES_ID AND SGROUP.GROUP_LEVEL = 2\n"); 
//		sql.append("    LEFT JOIN TM_ORG ORGPRO ON COMPLAINT.CP_OBJECT_ORG = ORGPRO.ORG_ID AND ORGPRO.DUTY_TYPE = "+Constant.DUTY_TYPE_LARGEREGION+"\n"); 
//		sql.append("    LEFT JOIN (SELECT TO_CHAR(MAX(DR.CD_DATE), 'yyyy-MM-dd hh24:mi:ss') LASTDEALDATE, DR.CP_ID, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CD_DATE,NULL)) BACK_DATE, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CREATE_DATE,NULL)) LASTORGDEALDATE\n");
//		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DR GROUP BY DR.CP_ID) X ON X.CP_ID = COMPLAINT.CP_ID");
//		// 艾春 2013.12.2 拼接延期次数统计字段
//		sql.append("    LEFT JOIN (SELECT T.CP_ID, count(DISTINCT decode(t.cl_date, null, '0',TO_CHAR(T.CL_DATE, 'yyyy-mm-dd'))) TIMES FROM TT_CRM_COMPLAINT_DELAY_RECORD T WHERE T.CL_VERIFY_STATUS = "+Constant.PASS_Manager_03+" GROUP BY T.CP_ID) Y ON Y.CP_ID = COMPLAINT.CP_ID");
//		// 艾春 2013.12.2 拼接延期次数统计字段
////		sql.append("    LEFT JOIN (SELECT MAX(DRD.CREATE_DATE) LASTORGDEALDATE, DRD.CP_ID \n"); 
////		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DRD where DRD.cp_status = "+Constant.COMPLAINT_STATUS_DOING_ALREADY+"  GROUP BY DRD.CP_ID) Y ON Y.CP_ID = COMPLAINT.CP_ID");
//		
//		sql.append(" where complaint.cp_biz_type = "+Constant.TYPE_COMPLAIN);
//		if(StringUtil.notNull(cpNo)){
//			sql.append(" and complaint.cp_no like '%"+cpNo+"%' ");
//		}
//		if(StringUtil.notNull(vin)){
//			sql.append(" and complaint.cp_vin like '%"+vin+"%' ");
//		}
//		if(StringUtil.notNull(name)){
//			sql.append(" and complaint.cp_name like '%"+name+"%' ");
//		}
//		if(StringUtil.notNull(tele)){
//			sql.append(" and complaint.cp_phone like '%"+tele+"%' ");
//		}
//		if(StringUtil.notNull(level)){
//			sql.append(" and complaint.cp_level = '"+level+"' ");
//		}
//		if(StringUtil.notNull(dealUser)){
//			sql.append(" and COMPLAINT.CP_DEAL_USER_NAME like '%"+dealUser+"%' ");
//		}
//		
//		
//		if(StringUtil.notNull(CP_START_DATE)){
//			sql.append(" and complaint.CP_CL_DATE >= to_date('"+CP_START_DATE+"','yyyy-mm-dd') ");
//		}
//		if(StringUtil.notNull(CP_END_DATE)){
//			sql.append(" and COMPLAINT.CP_CL_DATE <= to_date('"+CP_END_DATE+" 23:59:59','yyyy-mm-dd hh24:mi:ss')");
//		}
//		//一次性关闭
//		if(StringUtil.notNull(is_one_close))
//		{
//			sql.append("  and  COMPLAINT.CP_IS_ONCE_SF =  " + is_one_close+"  and COMPLAINT.cp_status in ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+")  and COMPLAINT.cp_cl_date is null  ");
//			
//		}
//		
//		if(StringUtil.notNull(is_close) )
//		{
//			if(is_close.equals(""+Constant.IF_TYPE_YES))
//			{
//				sql.append(" and  case when COMPLAINT.CP_STATUS  not in ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.COMPLAINT_STATUS_CLOSE+") then\r\n" );
//				sql.append("          case when sysdate<=nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) \r\n" );
//				sql.append("    then 1  else 0 end  \r\n");
//				sql.append("     else case when COMPLAINT.Cp_Status ="+Constant.COMPLAINT_STATUS_CLOSE+" then 1\r\n" );
//				sql.append("               when  COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and trunc(X.BACK_DATE) <= trunc(nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0))) then 1  else 0 end end \r\n" );
//				sql.append(" = 1 ");
//				
//			}else
//			{
//				sql.append(" and  case when COMPLAINT.CP_STATUS  not in ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.COMPLAINT_STATUS_CLOSE+") then\r\n" );
//				sql.append("          case when sysdate<=nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) \r\n" );
//				sql.append("    then 1  else 0 end  \r\n");
//				sql.append("     else case when COMPLAINT.Cp_Status ="+Constant.COMPLAINT_STATUS_CLOSE+" then 1\r\n" );
//				sql.append("               when  COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and trunc(X.BACK_DATE) <= trunc(nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0))) then 1  else 0 end end \r\n" );
//				sql.append(" = 0 ");
//			}
//		}
//		
//		
//		
//		// 艾春 2013.11.27 添加需考核日期查询条件
////		if(StringUtil.notNull(KH_END_DATE)){
////			sql.append(" and complaint.CP_TURN_DATE <= to_date('"+KH_END_DATE+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
////		}
//		
////		if(StringUtil.notNull(KH_START_DATE)){
////			sql.append(" AND ((complaint.cp_cl_date is null and (complaint.CP_TURN_DATE+complaint.cp_limit) >= to_date('"+KH_START_DATE+"','yyyy-MM-dd')) ");
////			sql.append(" or (complaint.cp_cl_date is not null and complaint.cp_cl_date >= to_date('"+KH_START_DATE+"','yyyy-MM-dd'))) ");
////		}
//		
//		if(StringUtil.notNull(KH_END_DATE) && StringUtil.notNull(KH_START_DATE))
//		{
//			  sql.append("      and ((complaint.CP_TURN_DATE <= to_date('"+KH_END_DATE+" 23:59:59','yyyy-MM-dd hh24:mi:ss') AND " );
//		      sql.append("        ((complaint.cp_cl_date is null and (complaint.CP_TURN_DATE+complaint.cp_limit) <= to_date('"+KH_END_DATE+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and (complaint.CP_TURN_DATE+complaint.cp_limit) >= to_date('"+KH_START_DATE+"','yyyy-MM-dd')) " );
//		      sql.append("          or (complaint.cp_cl_date is not null and complaint.cp_cl_date <= to_date('"+KH_END_DATE+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and complaint.cp_cl_date >= to_date('"+KH_START_DATE+"','yyyy-MM-dd'))))) ");
//		}
//		
//		
//		// 艾春 2013.11.27 添加需考核日期查询条件
//		
//		
//		if(StringUtil.notNull(accUser)){
//			sql.append(" and acuser.name like '%"+accUser+"%' ");
//		}
//		if(StringUtil.notNull(biztype)){
//			sql.append(" and complaint.CP_BIZ_CONTENT = '"+biztype+"' ");
//		}
//		if(StringUtil.notNull(status) && status.equals("unClose")){
//			sql.append(" and complaint.cp_status not in ('"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+"','"+Constant.COMPLAINT_STATUS_CLOSE+"') ");
//		}else if(StringUtil.notNull(status) && !status.equals("unClose")){
//			sql.append(" and complaint.cp_status = '"+status+"' ");
//		}
//		if(StringUtil.notNull(dateStart)){
//			sql.append(" and complaint.cp_acc_date >=to_date('"+dateStart+"','yyyy-MM-dd') ");
//		}
//		if(StringUtil.notNull(dateEnd)){
//			sql.append(" and complaint.cp_acc_date<=to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
//		}
//		if(StringUtil.notNull(dealStart)){
//			sql.append(" and complaint.CP_TURN_DATE >=to_date('"+dealStart+"','yyyy-MM-dd') ");
//		}
//		if(StringUtil.notNull(dealEnd)){
//			sql.append(" and complaint.CP_TURN_DATE<=to_date('"+dealEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
//		}
//		if(StringUtil.notNull(region)){
//			sql.append(" and complaint.CP_DEAL_ORG = "+ region );
//		}
//		if(StringUtil.notNull(pro)){
//			sql.append(" and complaint.CP_PROVINCE_ID = "+pro);
//		}
//		if(StringUtil.notNull(delaystatus) && Constant.NOT_APPLY.toString().equals(delaystatus)){
//			sql.append(" and (complaint.CP_CL_STATUS = "+delaystatus+" or complaint.CP_CL_STATUS is null)");
//		}else if(StringUtil.notNull(delaystatus)){
//			sql.append(" and complaint.CP_CL_STATUS = "+delaystatus);
//		}
//		if(repeatComplaint.equals("true")){
//			sql.append(" order by complaint.CP_VIN desc, complaint.CP_ACC_DATE desc ");
//		}else{
//			sql.append(" order by complaint.CP_ACC_DATE desc ");
//		}
		

		StringBuffer sql = new StringBuffer();
		// 艾春 9.23 修改 投诉查询语句
		sql.append(" SELECT COMPLAINT.CP_NO        CPNO, /*单号*/\n");
		sql.append("         COMPLAINT.CP_ID        CPID, /*ID*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS    STATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_CL_STATUS CLSTATUS, /*延期状态*/\n"); 
		sql.append("         COMPLAINT.CP_LEVEL     CPLEVEL, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n"); 
		sql.append("         TRP.REGION_NAME      PRO, /*省份*/\n"); 
		sql.append("         TRC.REGION_NAME      CITY, /*城市*/\n"); 
		sql.append("         COMPLAINT.CP_NAME    CTMNAME, /*客户名称*/\n"); 
		sql.append("         COMPLAINT.CP_CUS_ID  CTMID, /*客户ID*/\n"); 
		sql.append("         COMPLAINT.CP_PHONE   PHONE,  /*电话*/\n"); 
		sql.append("         COMPLAINT.CP_CONTENT CPCONT,  /*内容类型*/\n"); 
		sql.append("         NVL(ORGOBJ.ORG_NAME, REOBJ.DEALER_NAME) CPOBJECT,/*抱怨对象*/\n"); 
		sql.append("         COMPLAINT.CREATE_DATE CREATEDATE,/*创建日期*/\n"); 
		sql.append("         ACUSER.NAME ACUSER,/*受理人*/\n"); 
		sql.append("         SGROUP.GROUP_NAME SNAME,/*车种*/\n"); 
		sql.append("         COMPLAINT.CP_VIN VIN,\n"); 
		sql.append("         TO_CHAR(COMPLAINT.CP_BUY_DATE, 'yyyy-MM-dd hh24:mi:ss') BDATE,/*购买日期*/\n"); 
		sql.append("         COMPLAINT.CP_MILEAGE CPMILEAGE, /*行驶里程*/\n"); 
		sql.append("         COMPLAINT.CP_MILEAGE_RANGE MILEAGERANGE, /*需转换*/\n"); 
		sql.append("         ORGPRO.ORG_NAME ORGNAME,/*大区*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DEALUSER,/*处理人*/\n"); 
		sql.append("         COMPLAINT.CP_ACC_DATE  CPACCDATE,/*受理日期*/\n"); 
		sql.append("         COMPLAINT.CP_TURN_DATE TURNDATE,/*转出日期*/\n"); 
		sql.append("         decode(COMPLAINT.CP_STATUS,"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.PLEASED+","+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.YAWP+",null) CPSF, /*需转换*/\n"); 
		sql.append("         to_char(COMPLAINT.CP_CL_ONCE_DATE,'yyyy-mm-dd') CP_CL_ONCE_DATE,      ");
		
		sql.append("         case when  COMPLAINT.CP_CL_ONCE_DATE is null  then  to_char((COMPLAINT.CP_TURN_DATE + nvl(COMPLAINT.CP_LIMIT,0)),'yyyy-mm-dd')        ");
		sql.append("         when COMPLAINT.CP_CL_ONCE_DATE is not  null then  ");
		sql.append("         case when  COMPLAINT.CP_CL_DATE is not null then to_char(COMPLAINT.CP_CL_DATE,'yyyy-mm-dd' )   ");
		sql.append("         when COMPLAINT.CP_CL_DATE is  null  then  to_char((COMPLAINT.CP_TURN_DATE + nvl(COMPLAINT.CP_LIMIT,0)),'yyyy-mm-dd')  end  ");
		sql.append("         end   CP_CL_DATE , ");
		
		
		
		sql.append("         COMPLAINT.CP_CLOSE_DATE CPCDATE, /*关闭日期*/\n"); 
		// zhangyu 加上已关闭和强制关闭才加上时长
		sql.append("   decode(complaint.CP_SF ,NULL,'',TO_CHAR(round((X.LASTORGDEALDATE - COMPLAINT.CP_TURN_DATE) * 24,2))) DEALTIME,");
		sql.append("         COMPLAINT.CP_LIMIT CPLIMIT, /*规定时限*/\n"); 

		sql.append("ceil(\r\n" );
		sql.append(" case when COMPLAINT.Cp_Cl_Date is null then 0\r\n" );
		sql.append("      else case when COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) >= COMPLAINT.Cp_Cl_Once_Date\r\n" );
		sql.append("                then case when COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) >0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) else 0 end\r\n" );
		sql.append("                else case when COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date>0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date else 0 end  end\r\n" );
		sql.append("      end\r\n" );
		sql.append(")DELAYDATE,/*延期天数(天) 取整*/");

		sql.append("       round(case when nvl(X.BACK_DATE,sysdate)-(nvl(COMPLAINT.CP_CL_DATE,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT, 0))) >0 \r\n" );
		sql.append("                  then nvl(X.BACK_DATE,sysdate)-(nvl(COMPLAINT.CP_CL_DATE,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT, 0)))  ");
		sql.append("                  else null end) EXCEEDDATE, /*超期天数(天) 取整*/\r\n" );
		
		sql.append("           to_char(COMPLAINT.CP_TURN_DATE + nvl(COMPLAINT.CP_LIMIT,0),'yyyy-mm-dd') SHOULDCLOSETIME, \n"); 
		sql.append("         /*CEIL((COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT - SYSDATE)*24) OWNTIME, */\n"); 
        
		// 艾春 2013.12.5 修改投诉及时性判断
		if(!"".equals(KH_START_DATE) && null != KH_START_DATE && !"".equals(KH_END_DATE) && null != KH_END_DATE){
			sql.append("case when COMPLAINT.Cp_Status not in("+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+") then\r\n" );
			sql.append("        case when sysdate<=nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '及时关闭'\r\n" );
			sql.append("        when sysdate>nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '未及时关闭'\r\n" );
			sql.append("        end\r\n" );
			sql.append("else case when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_CLOSE+" then '及时关闭'\r\n" );
			sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then '未及时关闭'\r\n" );
			sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and trunc(X.BACK_DATE) <= trunc(nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0))) then '及时关闭'\r\n" );
			sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and trunc(X.BACK_DATE) > trunc(nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0))) then '未及时关闭'\r\n" );
			sql.append("          end\r\n" );
			sql.append("end  ISTIMELYCLOSE, /*是否及时关闭*/");			
		}else{
			sql.append("case when COMPLAINT.Cp_Status in("+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+") then\r\n" );
			sql.append("  case when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_CLOSE+" then '及时关闭'\r\n" );
			sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then '未及时关闭'\r\n" );
			sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and trunc(X.BACK_DATE) <= trunc(nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0))) then '及时关闭'\r\n" );
			sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and trunc(X.BACK_DATE) > trunc(nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0))) then '未及时关闭'\r\n" );
			sql.append("          end\r\n" );
			sql.append("end  ISTIMELYCLOSE, /*是否及时关闭*/");			
		}
		// 艾春 2013.12.5 修改投诉及时性判断

		sql.append("         COMPLAINT.CP_IS_ONCE_SF CPISONCESF, /*需转换*/\n"); 
		sql.append("         DECODE(COMPLAINT.Cp_Close_Date, NULL, '',\n"); 
		sql.append("           CASE WHEN COMPLAINT.CP_SF="+Constant.PLEASED+" THEN '正常关闭'\n"); 
		sql.append("                WHEN COMPLAINT.CP_SF="+Constant.YAWP+" THEN '不正常关闭'\n"); 
		sql.append("                ELSE '' END) ISNORMALCLOSE, /*是否正常关闭*/\n"); 
		sql.append("         X.BACK_DATE LASTDEALDATE, /*最终反馈时间*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS CPSTATUS, /*状态*/\n"); 
		// 艾春 2013.12.2 显示延期次数统计字段
		sql.append("         Y.TIMES, /*延期次数*/\n"); 
		// 艾春 2013.12.2 显示延期次数统计字段
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DUSER /*当前处理人*/\n"); 
		sql.append("    FROM TT_CRM_COMPLAINT COMPLAINT\n"); 
		sql.append("    LEFT JOIN TM_REGION TRP ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID AND TRP.REGION_TYPE = "+Constant.REGION_TYPE_02+"\n"); 
		sql.append("    LEFT JOIN TM_REGION TRC ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID AND TRC.REGION_TYPE = "+Constant.REGION_TYPE_03+"\n"); 
		sql.append("    LEFT JOIN tm_org_custom ORGOBJ ON ORGOBJ.ORG_ID = COMPLAINT.CP_OBJECT /*报怨对象处*/\n"); 
		sql.append("    LEFT JOIN TM_DEALER REOBJ ON REOBJ.DEALER_ID = COMPLAINT.CP_OBJECT \n"); 
		sql.append("    LEFT JOIN TC_USER ACUSER ON ACUSER.USER_ID = COMPLAINT.CP_ACC_USER\n"); 
		sql.append("    LEFT JOIN TM_VHCL_MATERIAL_GROUP SGROUP ON SGROUP.GROUP_ID = COMPLAINT.CP_SERIES_ID AND SGROUP.GROUP_LEVEL = 2\n"); 
		sql.append("    LEFT JOIN TM_ORG ORGPRO ON COMPLAINT.CP_OBJECT_ORG = ORGPRO.ORG_ID AND ORGPRO.DUTY_TYPE = "+Constant.DUTY_TYPE_LARGEREGION+"\n"); 
		sql.append("    LEFT JOIN (SELECT TO_CHAR(MAX(DR.CD_DATE), 'yyyy-MM-dd hh24:mi:ss') LASTDEALDATE, DR.CP_ID, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CD_DATE,NULL)) BACK_DATE, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CREATE_DATE,NULL)) LASTORGDEALDATE\n");
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DR GROUP BY DR.CP_ID) X ON X.CP_ID = COMPLAINT.CP_ID");
		// 艾春 2013.12.2 拼接延期次数统计字段
		sql.append("    LEFT JOIN (SELECT T.CP_ID, count(DISTINCT decode(t.cl_date, null, '0',TO_CHAR(T.CL_DATE, 'yyyy-mm-dd'))) TIMES FROM TT_CRM_COMPLAINT_DELAY_RECORD T WHERE T.CL_VERIFY_STATUS = "+Constant.PASS_Manager_03+" GROUP BY T.CP_ID) Y ON Y.CP_ID = COMPLAINT.CP_ID");
		// 艾春 2013.12.2 拼接延期次数统计字段
		
		sql.append(" where complaint.cp_biz_type = "+Constant.TYPE_COMPLAIN);
		if(StringUtil.notNull(cpNo)){
			sql.append(" and complaint.cp_no like '%"+cpNo+"%' ");
		}
		if(StringUtil.notNull(vin)){
			sql.append(" and complaint.cp_vin like '%"+vin+"%' ");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and complaint.cp_name like '%"+name+"%' ");
		}
		if(StringUtil.notNull(tele)){
			sql.append(" and complaint.cp_phone like '%"+tele+"%' ");
		}
		if(StringUtil.notNull(level)){
			sql.append(" and complaint.cp_level = '"+level+"' ");
		}
		if(StringUtil.notNull(dealUser)){
			sql.append(" and COMPLAINT.CP_DEAL_USER_NAME like '%"+dealUser+"%' ");
		}
		// 艾春 2013.12.7 添加车型查询条件
		if(StringUtil.notNull(model)){
			sql.append(" and COMPLAINT.CP_MODEL_ID = "+model+" ");
		}
		// 艾春 2013.12.7 添加车型查询条件
		
		if(StringUtil.notNull(CP_START_DATE)){
			sql.append(" and complaint.CP_CL_DATE >= to_date('"+CP_START_DATE+"','yyyy-mm-dd') ");
		}
		if(StringUtil.notNull(CP_END_DATE)){
			sql.append(" and COMPLAINT.CP_CL_DATE <= to_date('"+CP_END_DATE+" 23:59:59','yyyy-mm-dd hh24:mi:ss')");
		}
		//一次性关闭
		if(StringUtil.notNull(is_one_close))
		{
			sql.append("  and  COMPLAINT.CP_IS_ONCE_SF =  " + is_one_close+"  and COMPLAINT.cp_status in ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+")  and COMPLAINT.cp_cl_date is null  ");
			
		}
		
		if(StringUtil.notNull(is_close) )
			{
				if(is_close.equals(""+Constant.IF_TYPE_YES))
				{
					sql.append(" and  case when COMPLAINT.CP_STATUS  not in ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.COMPLAINT_STATUS_CLOSE+") then\r\n" );
					sql.append("          case when sysdate<=nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) \r\n" );
					sql.append("    then 1  else 0 end  \r\n");
					sql.append("     else case when COMPLAINT.Cp_Status ="+Constant.COMPLAINT_STATUS_CLOSE+" then 1\r\n" );
					sql.append("               when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then 0\r\n");
					sql.append("               when  COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and trunc(X.BACK_DATE) <= trunc(nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0))) then 1  else 0 end end \r\n" );
					sql.append(" = 1 ");
					
				}else
				{
					sql.append(" and  case when COMPLAINT.CP_STATUS  not in ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.COMPLAINT_STATUS_CLOSE+") then\r\n" );
					sql.append("          case when sysdate<=nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) \r\n" );
					sql.append("    then 1  else 0 end  \r\n");
					sql.append("     else case when COMPLAINT.Cp_Status ="+Constant.COMPLAINT_STATUS_CLOSE+" then 1\r\n" );
					sql.append("               when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then 0\r\n");
					sql.append("               when  COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and trunc(X.BACK_DATE) <= trunc(nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0))) then 1  else 0 end end \r\n" );
					sql.append(" = 0 ");
				}
			}
			
			
			
			// 艾春 2013.11.27 添加需考核日期查询条件
//			if(StringUtil.notNull(KH_END_DATE)){
//				sql.append(" and complaint.CP_TURN_DATE <= to_date('"+KH_END_DATE+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
//			}
			
//			if(StringUtil.notNull(KH_START_DATE)){
//				sql.append(" AND ((complaint.cp_cl_date is null and (complaint.CP_TURN_DATE+complaint.cp_limit) >= to_date('"+KH_START_DATE+"','yyyy-MM-dd')) ");
//				sql.append(" or (complaint.cp_cl_date is not null and complaint.cp_cl_date >= to_date('"+KH_START_DATE+"','yyyy-MM-dd'))) ");
//			}
			
			if(StringUtil.notNull(KH_END_DATE) && StringUtil.notNull(KH_START_DATE))
			{
				  sql.append("      and ((complaint.CP_TURN_DATE <= to_date('"+KH_END_DATE+" 23:59:59','yyyy-MM-dd hh24:mi:ss') AND " );
			      sql.append("        ((complaint.cp_cl_date is null and (complaint.CP_TURN_DATE+complaint.cp_limit) <= to_date('"+KH_END_DATE+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and (complaint.CP_TURN_DATE+complaint.cp_limit) >= to_date('"+KH_START_DATE+"','yyyy-MM-dd')) " );
			      sql.append("          or (complaint.cp_cl_date is not null and complaint.cp_cl_date <= to_date('"+KH_END_DATE+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and complaint.cp_cl_date >= to_date('"+KH_START_DATE+"','yyyy-MM-dd'))))) ");
			}
			
			
			// 艾春 2013.11.27 添加需考核日期查询条件
		
		
		if(StringUtil.notNull(accUser)){
			sql.append(" and acuser.name like '%"+accUser+"%' ");
		}
		if(StringUtil.notNull(biztype)){
			sql.append(" and complaint.CP_BIZ_CONTENT = '"+biztype+"' ");
		}
		if(StringUtil.notNull(status) && status.equals("unClose")){
			sql.append(" and complaint.cp_status not in ('"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+"','"+Constant.COMPLAINT_STATUS_CLOSE+"') ");
		}else if(StringUtil.notNull(status) && !status.equals("unClose")){
			sql.append(" and complaint.cp_status = '"+status+"' ");
		}
		if(StringUtil.notNull(dateStart)){
			sql.append(" and complaint.cp_acc_date >=to_date('"+dateStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dateEnd)){
			sql.append(" and complaint.cp_acc_date<=to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(dealStart)){
			sql.append(" and complaint.CP_TURN_DATE >=to_date('"+dealStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dealEnd)){
			sql.append(" and complaint.CP_TURN_DATE<=to_date('"+dealEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(region)){
			sql.append(" and complaint.CP_DEAL_ORG = "+ region );
		}
		if(StringUtil.notNull(pro)){
			sql.append(" and complaint.CP_PROVINCE_ID = "+pro);
		}
		if(StringUtil.notNull(delaystatus) && Constant.NOT_APPLY.toString().equals(delaystatus)){
			sql.append(" and (complaint.CP_CL_STATUS = "+delaystatus+" or complaint.CP_CL_STATUS is null)");
		}else if(StringUtil.notNull(delaystatus)){
			sql.append(" and complaint.CP_CL_STATUS = "+delaystatus);
		}
		if(repeatComplaint.equals("true")){
			sql.append(" order by complaint.CP_VIN desc, complaint.CP_ACC_DATE desc ");
		}else{
			sql.append(" order by complaint.CP_ACC_DATE desc ");
		}
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}

	public List<Map<String, Object>> queryComplaintInfo(String cpNo,String vin,
			String name, String tele, String level,String dealUser,String accUser, String biztype,
			String status, String dealStart, String dealEnd, String dateStart,
			String dateEnd, String region, String pro, String checkStart,
			String checklEnd,boolean isAdmin,String CP_START_DATE,String CP_END_DATE , String KH_START_DATE,String KH_END_DATE ,
			long userid,String delaystatus,String repeatComplaint,String is_close, String is_one_close, String model) {
		StringBuffer sql = new StringBuffer();
		// 艾春 9.23 修改 投诉查询语句
		sql.append(" SELECT COMPLAINT.CP_NO        CPNO, /*单号*/\n");
		sql.append("         COMPLAINT.CP_ID        CPID, /*ID*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS    STATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_CL_STATUS CLSTATUS, /*延期状态*/\n"); 
		sql.append("         COMPLAINT.CP_LEVEL     CPLEVEL, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n"); 
		sql.append("         TRP.REGION_NAME      PRO, /*省份*/\n"); 
		sql.append("         TRC.REGION_NAME      CITY, /*城市*/\n"); 
		sql.append("         COMPLAINT.CP_NAME    CTMNAME, /*客户名称*/\n"); 
		sql.append("         COMPLAINT.CP_CUS_ID  CTMID, /*客户ID*/\n"); 
		sql.append("         COMPLAINT.CP_PHONE   PHONE,  /*电话*/\n"); 
		sql.append("         COMPLAINT.CP_CONTENT CPCONT,  /*内容类型*/\n"); 
		sql.append("         NVL(ORGOBJ.ORG_NAME, REOBJ.DEALER_NAME) CPOBJECT,/*抱怨对象*/\n"); 
		sql.append("         COMPLAINT.CREATE_DATE CREATEDATE,/*创建日期*/\n"); 
		sql.append("         ACUSER.NAME ACUSER,/*受理人*/\n"); 
		sql.append("         SGROUP.GROUP_NAME SNAME,/*车种*/\n"); 
		sql.append("         COMPLAINT.CP_VIN VIN,\n"); 
		sql.append("         TO_CHAR(COMPLAINT.CP_BUY_DATE, 'yyyy-MM-dd hh24:mi:ss') BDATE,/*购买日期*/\n"); 
		sql.append("         COMPLAINT.CP_MILEAGE CPMILEAGE, /*行驶里程*/\n"); 
		sql.append("         COMPLAINT.CP_MILEAGE_RANGE MILEAGERANGE, /*需转换*/\n"); 
		sql.append("         ORGPRO.ORG_NAME ORGNAME,/*大区*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DEALUSER,/*处理人*/\n"); 
		sql.append("         COMPLAINT.CP_ACC_DATE  CPACCDATE,/*受理日期*/\n"); 
		sql.append("         COMPLAINT.CP_TURN_DATE TURNDATE,/*转出日期*/\n"); 
		sql.append("         decode(COMPLAINT.CP_STATUS,"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.PLEASED+","+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.YAWP+",null) CPSF, /*需转换*/\n"); 
		sql.append("         to_char(COMPLAINT.CP_CL_ONCE_DATE,'yyyy-mm-dd') CP_CL_ONCE_DATE,      ");
		
		sql.append("         case when  COMPLAINT.CP_CL_ONCE_DATE is null  then  to_char((COMPLAINT.CP_TURN_DATE + nvl(COMPLAINT.CP_LIMIT,0)),'yyyy-mm-dd')        ");
		sql.append("         when COMPLAINT.CP_CL_ONCE_DATE is not  null then  ");
		sql.append("         case when  COMPLAINT.CP_CL_DATE is not null then to_char(COMPLAINT.CP_CL_DATE,'yyyy-mm-dd' )   ");
		sql.append("         when COMPLAINT.CP_CL_DATE is  null  then  to_char((COMPLAINT.CP_TURN_DATE + nvl(COMPLAINT.CP_LIMIT,0)),'yyyy-mm-dd')  end  ");
		sql.append("         end   CP_CL_DATE , ");
		
		
		
		sql.append("         COMPLAINT.CP_CLOSE_DATE CPCDATE, /*关闭日期*/\n"); 
		// zhangyu 加上已关闭和强制关闭才加上时长
		sql.append("   decode(complaint.CP_SF ,NULL,'',TO_CHAR(round((X.LASTORGDEALDATE - COMPLAINT.CP_TURN_DATE) * 24,2))) DEALTIME,");
		sql.append("         COMPLAINT.CP_LIMIT CPLIMIT, /*规定时限*/\n"); 

		sql.append("ceil(\r\n" );
		sql.append(" case when COMPLAINT.Cp_Cl_Date is null then 0\r\n" );
		sql.append("      else case when COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) >= COMPLAINT.Cp_Cl_Once_Date\r\n" );
		sql.append("                then case when COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) >0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) else 0 end\r\n" );
		sql.append("                else case when COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date>0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date else 0 end  end\r\n" );
		sql.append("      end\r\n" );
		sql.append(")DELAYDATE,/*延期天数(天) 取整*/");

		sql.append("       round(case when nvl(X.BACK_DATE,sysdate)-(nvl(COMPLAINT.CP_CL_DATE,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT, 0))) >0 \r\n" );
		sql.append("                  then nvl(X.BACK_DATE,sysdate)-(nvl(COMPLAINT.CP_CL_DATE,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT, 0)))  ");
		sql.append("                  else null end) EXCEEDDATE, /*超期天数(天) 取整*/\r\n" );
		
		sql.append("           to_char(COMPLAINT.CP_TURN_DATE + nvl(COMPLAINT.CP_LIMIT,0),'yyyy-mm-dd') SHOULDCLOSETIME, \n"); 
		sql.append("         /*CEIL((COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT - SYSDATE)*24) OWNTIME, */\n"); 

		// 艾春 2013.12.5 修改投诉及时性判断
		if(!"".equals(KH_START_DATE) && null != KH_START_DATE && !"".equals(KH_END_DATE) && null != KH_END_DATE){
			sql.append("case when COMPLAINT.Cp_Status not in("+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+") then\r\n" );
			sql.append("        case when sysdate<=nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '及时关闭'\r\n" );
			sql.append("        when sysdate>nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '未及时关闭'\r\n" );
			sql.append("        end\r\n" );
			sql.append("else case when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_CLOSE+" then '及时关闭'\r\n" );
			sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then '未及时关闭'\r\n" );
			sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and trunc(X.BACK_DATE) <= trunc(nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0))) then '及时关闭'\r\n" );
			sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and trunc(X.BACK_DATE) > trunc(nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0))) then '未及时关闭'\r\n" );
			sql.append("          end\r\n" );
			sql.append("end  ISTIMELYCLOSE, /*是否及时关闭*/");			
		}else{
			sql.append("case when COMPLAINT.Cp_Status in("+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+") then\r\n" );
			sql.append("  case when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_CLOSE+" then '及时关闭'\r\n" );
			sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then '未及时关闭'\r\n" );
			sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and trunc(X.BACK_DATE) <= trunc(nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0))) then '及时关闭'\r\n" );
			sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and trunc(X.BACK_DATE) > trunc(nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0))) then '未及时关闭'\r\n" );
			sql.append("          end\r\n" );
			sql.append("end  ISTIMELYCLOSE, /*是否及时关闭*/");			
		}
		// 艾春 2013.12.5 修改投诉及时性判断

		sql.append("         COMPLAINT.CP_IS_ONCE_SF CPISONCESF, /*需转换*/\n"); 
		sql.append("         DECODE(COMPLAINT.Cp_Close_Date, NULL, '',\n"); 
		sql.append("           CASE WHEN COMPLAINT.CP_SF="+Constant.PLEASED+" THEN '正常关闭'\n"); 
		sql.append("                WHEN COMPLAINT.CP_SF="+Constant.YAWP+" THEN '不正常关闭'\n"); 
		sql.append("                ELSE '' END) ISNORMALCLOSE, /*是否正常关闭*/\n"); 
		sql.append("         X.BACK_DATE LASTDEALDATE, /*最终反馈时间*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS CPSTATUS, /*状态*/\n"); 
		// 艾春 2013.12.2 显示延期次数统计字段
		sql.append("         Y.TIMES, /*延期次数*/\n"); 
		// 艾春 2013.12.2 显示延期次数统计字段
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DUSER /*当前处理人*/\n"); 
		sql.append("    FROM TT_CRM_COMPLAINT COMPLAINT\n"); 
		sql.append("    LEFT JOIN TM_REGION TRP ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID AND TRP.REGION_TYPE = "+Constant.REGION_TYPE_02+"\n"); 
		sql.append("    LEFT JOIN TM_REGION TRC ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID AND TRC.REGION_TYPE = "+Constant.REGION_TYPE_03+"\n"); 
		sql.append("    LEFT JOIN tm_org_custom ORGOBJ ON ORGOBJ.ORG_ID = COMPLAINT.CP_OBJECT /*报怨对象处*/\n"); 
		sql.append("    LEFT JOIN TM_DEALER REOBJ ON REOBJ.DEALER_ID = COMPLAINT.CP_OBJECT \n"); 
		sql.append("    LEFT JOIN TC_USER ACUSER ON ACUSER.USER_ID = COMPLAINT.CP_ACC_USER\n"); 
		sql.append("    LEFT JOIN TM_VHCL_MATERIAL_GROUP SGROUP ON SGROUP.GROUP_ID = COMPLAINT.CP_SERIES_ID AND SGROUP.GROUP_LEVEL = 2\n"); 
		sql.append("    LEFT JOIN TM_ORG ORGPRO ON COMPLAINT.CP_OBJECT_ORG = ORGPRO.ORG_ID AND ORGPRO.DUTY_TYPE = "+Constant.DUTY_TYPE_LARGEREGION+"\n"); 
		sql.append("    LEFT JOIN (SELECT TO_CHAR(MAX(DR.CD_DATE), 'yyyy-MM-dd hh24:mi:ss') LASTDEALDATE, DR.CP_ID, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CD_DATE,NULL)) BACK_DATE, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CREATE_DATE,NULL)) LASTORGDEALDATE\n");
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DR GROUP BY DR.CP_ID) X ON X.CP_ID = COMPLAINT.CP_ID");
		// 艾春 2013.12.2 拼接延期次数统计字段
		sql.append("    LEFT JOIN (SELECT T.CP_ID, count(DISTINCT decode(t.cl_date, null, '0',TO_CHAR(T.CL_DATE, 'yyyy-mm-dd'))) TIMES FROM TT_CRM_COMPLAINT_DELAY_RECORD T WHERE T.CL_VERIFY_STATUS = "+Constant.PASS_Manager_03+" GROUP BY T.CP_ID) Y ON Y.CP_ID = COMPLAINT.CP_ID");
		// 艾春 2013.12.2 拼接延期次数统计字段
		
		sql.append(" where complaint.cp_biz_type = "+Constant.TYPE_COMPLAIN);
		if(StringUtil.notNull(cpNo)){
			sql.append(" and complaint.cp_no like '%"+cpNo+"%' ");
		}
		if(StringUtil.notNull(vin)){
			sql.append(" and complaint.cp_vin like '%"+vin+"%' ");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and complaint.cp_name like '%"+name+"%' ");
		}
		if(StringUtil.notNull(tele)){
			sql.append(" and complaint.cp_phone like '%"+tele+"%' ");
		}
		if(StringUtil.notNull(level)){
			sql.append(" and complaint.cp_level = '"+level+"' ");
		}
		if(StringUtil.notNull(dealUser)){
			sql.append(" and COMPLAINT.CP_DEAL_USER_NAME like '%"+dealUser+"%' ");
		}
		// 艾春 2013.12.7 添加车型查询条件
		if(StringUtil.notNull(model)){
			sql.append(" and COMPLAINT.CP_MODEL_ID = "+model+" ");
		}
		// 艾春 2013.12.7 添加车型查询条件
		
		if(StringUtil.notNull(CP_START_DATE)){
			sql.append(" and complaint.CP_CL_DATE >= to_date('"+CP_START_DATE+"','yyyy-mm-dd') ");
		}
		if(StringUtil.notNull(CP_END_DATE)){
			sql.append(" and COMPLAINT.CP_CL_DATE <= to_date('"+CP_END_DATE+" 23:59:59','yyyy-mm-dd hh24:mi:ss')");
		}
		//一次性关闭
		if(StringUtil.notNull(is_one_close))
		{
			sql.append("  and  COMPLAINT.CP_IS_ONCE_SF =  " + is_one_close+"  and COMPLAINT.cp_status in ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+")  and COMPLAINT.cp_cl_date is null  ");
			
		}
		
		if(StringUtil.notNull(is_close) )
		{
			if(is_close.equals(""+Constant.IF_TYPE_YES))
			{
				sql.append(" and  case when COMPLAINT.CP_STATUS  not in ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.COMPLAINT_STATUS_CLOSE+") then\r\n" );
				sql.append("          case when sysdate<=nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) \r\n" );
				sql.append("    then 1  else 0 end  \r\n");
				sql.append("     else case when COMPLAINT.Cp_Status ="+Constant.COMPLAINT_STATUS_CLOSE+" then 1\r\n" );
				sql.append("               when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then 0\r\n");
				sql.append("               when  COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and trunc(X.BACK_DATE) <= trunc(nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0))) then 1  else 0 end end \r\n" );
				sql.append(" = 1 ");
				
			}else
			{
				sql.append(" and  case when COMPLAINT.CP_STATUS  not in ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.COMPLAINT_STATUS_CLOSE+") then\r\n" );
				sql.append("          case when sysdate<=nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) \r\n" );
				sql.append("    then 1  else 0 end  \r\n");
				sql.append("     else case when COMPLAINT.Cp_Status ="+Constant.COMPLAINT_STATUS_CLOSE+" then 1\r\n" );
				sql.append("               when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then 0\r\n");
				sql.append("               when  COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and trunc(X.BACK_DATE) <= trunc(nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0))) then 1  else 0 end end \r\n" );
				sql.append(" = 0 ");
			}
		}

			// 艾春 2013.11.27 添加需考核日期查询条件
			
			if(StringUtil.notNull(KH_END_DATE) && StringUtil.notNull(KH_START_DATE))
			{
				  sql.append("      and ((complaint.CP_TURN_DATE <= to_date('"+KH_END_DATE+" 23:59:59','yyyy-MM-dd hh24:mi:ss') AND " );
			      sql.append("        ((complaint.cp_cl_date is null and (complaint.CP_TURN_DATE+complaint.cp_limit) <= to_date('"+KH_END_DATE+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and (complaint.CP_TURN_DATE+complaint.cp_limit) >= to_date('"+KH_START_DATE+"','yyyy-MM-dd')) " );
			      sql.append("          or (complaint.cp_cl_date is not null and complaint.cp_cl_date <= to_date('"+KH_END_DATE+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and complaint.cp_cl_date >= to_date('"+KH_START_DATE+"','yyyy-MM-dd'))))) ");
			}
			
			
			// 艾春 2013.11.27 添加需考核日期查询条件
		
		
		if(StringUtil.notNull(accUser)){
			sql.append(" and acuser.name like '%"+accUser+"%' ");
		}
		if(StringUtil.notNull(biztype)){
			sql.append(" and complaint.CP_BIZ_CONTENT = '"+biztype+"' ");
		}
		if(StringUtil.notNull(status) && status.equals("unClose")){
			sql.append(" and complaint.cp_status not in ('"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+"','"+Constant.COMPLAINT_STATUS_CLOSE+"') ");
		}else if(StringUtil.notNull(status) && !status.equals("unClose")){
			sql.append(" and complaint.cp_status = '"+status+"' ");
		}
		if(StringUtil.notNull(dateStart)){
			sql.append(" and complaint.cp_acc_date >=to_date('"+dateStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dateEnd)){
			sql.append(" and complaint.cp_acc_date<=to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(dealStart)){
			sql.append(" and complaint.CP_TURN_DATE >=to_date('"+dealStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dealEnd)){
			sql.append(" and complaint.CP_TURN_DATE<=to_date('"+dealEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(region)){
			sql.append(" and complaint.CP_DEAL_ORG = "+ region );
		}
		if(StringUtil.notNull(pro)){
			sql.append(" and complaint.CP_PROVINCE_ID = "+pro);
		}
		if(StringUtil.notNull(delaystatus) && Constant.NOT_APPLY.toString().equals(delaystatus)){
			sql.append(" and (complaint.CP_CL_STATUS = "+delaystatus+" or complaint.CP_CL_STATUS is null)");
		}else if(StringUtil.notNull(delaystatus)){
			sql.append(" and complaint.CP_CL_STATUS = "+delaystatus);
		}
		if(repeatComplaint.equals("true")){
			sql.append(" order by complaint.CP_VIN desc, complaint.CP_ACC_DATE desc ");
		}else{
			sql.append(" order by complaint.CP_ACC_DATE desc ");
		}
		
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	//大区投诉查询
	public PageResult<Map<String, Object>> queryComplaintInfoByOrgids(String vin,
			String name, String tele, String level,String dealUser, String accUser, String biztype,
			String status, String dealStart, String dealEnd, String dateStart,
			String dateEnd, String region, String pro, String checkStart,
			String checklEnd,String orgids, Integer pageSize, Integer curPage) {
		StringBuffer sql = new StringBuffer();
		// 艾春 9.23 修改 投诉查询语句
		sql.append(" SELECT COMPLAINT.CP_NO        CPNO, /*单号*/\n");
		sql.append("         COMPLAINT.CP_ID        CPID, /*ID*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS    STATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_CL_STATUS CLSTATUS, /*延期状态*/\n"); 
		sql.append("         COMPLAINT.CP_LEVEL     CPLEVEL, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n"); 
		sql.append("         TRP.REGION_NAME      PRO, /*省份*/\n"); 
		sql.append("         TRC.REGION_NAME      CITY, /*城市*/\n"); 
		sql.append("         COMPLAINT.CP_NAME    CTMNAME, /*客户名称*/\n"); 
		sql.append("         COMPLAINT.CP_CUS_ID  CTMID, /*客户ID*/\n"); 
		sql.append("         COMPLAINT.CP_PHONE   PHONE,  /*电话*/\n"); 
		sql.append("         COMPLAINT.CP_CONTENT CPCONT,  /*内容类型*/\n"); 
		sql.append("         NVL(ORGOBJ.ORG_NAME, REOBJ.DEALER_NAME) CPOBJECT,/*抱怨对象*/\n"); 
		sql.append("         COMPLAINT.CREATE_DATE CREATEDATE,/*创建日期*/\n"); 
		sql.append("         ACUSER.NAME ACUSER,/*受理人*/\n"); 
		sql.append("         SGROUP.GROUP_NAME SNAME,/*车种*/\n"); 
		sql.append("         COMPLAINT.CP_VIN VIN,\n"); 
		sql.append("         TO_CHAR(COMPLAINT.CP_BUY_DATE, 'yyyy-MM-dd hh24:mi:ss') BDATE,/*购买日期*/\n"); 
		sql.append("         COMPLAINT.CP_MILEAGE CPMILEAGE, /*行驶里程*/\n"); 
		sql.append("         /*COMPLAINT.CP_MILEAGE_RANGE, /*需转换*/\n"); 
		sql.append("         ORGPRO.ORG_NAME ORGNAME,/*大区*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DEALUSER,/*处理人*/\n"); 
		sql.append("         COMPLAINT.CP_ACC_DATE  CPACCDATE,/*受理日期*/\n"); 
		sql.append("         COMPLAINT.CP_TURN_DATE TURNDATE,/*转出日期*/\n"); 
		sql.append("         decode(COMPLAINT.CP_STATUS,"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.PLEASED+","+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.YAWP+",null) CPSF, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_CLOSE_DATE CPCDATE, /*关闭日期*/\n"); 
		sql.append("         TO_CHAR(CEIL((Y.LASTORGDEALDATE - COMPLAINT.CP_TURN_DATE) * 24)) DEALTIME,");
		sql.append("         COMPLAINT.CP_LIMIT CPLIMIT, /*规定时限*/\n"); 

		sql.append("ceil(\r\n" );
		sql.append(" case when COMPLAINT.Cp_Cl_Date is null then 0\r\n" );
		sql.append("      else case when COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) >= COMPLAINT.Cp_Cl_Once_Date\r\n" );
		sql.append("                then case when COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) >0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) else 0 end\r\n" );
		sql.append("                else case when COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date>0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date else 0 end  end\r\n" );
		sql.append("      end\r\n" );
		sql.append(")DELAYDATE,/*延期天数(天) 取整*/");

		sql.append("         /*COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT SHOULDCLOSETIME, */\n"); 
		sql.append("         /*CEIL((COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT - SYSDATE)*24) OWNTIME, */\n"); 

		sql.append("case when COMPLAINT.Cp_Status in("+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+") then\r\n" );
		sql.append("  case when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_CLOSE+" then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then '未及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE <= nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE > nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '未及时关闭'\r\n" );
		sql.append("          end\r\n" );
		sql.append("end  ISTIMELYCLOSE, /*是否及时关闭*/");			

		sql.append("         COMPLAINT.CP_IS_ONCE_SF CPISONCESF, /*需转换*/\n"); 
		sql.append("         DECODE(COMPLAINT.Cp_Close_Date, NULL, '',\n"); 
		sql.append("           CASE WHEN COMPLAINT.Cp_Close_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) THEN '不正常关闭'\n"); 
		sql.append("                WHEN COMPLAINT.Cp_Close_Date <= COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) THEN '正常关闭'\n"); 
		sql.append("                ELSE '' END) ISNORMALCLOSE, /*是否正常关闭*/\n"); 
		sql.append("         X.BACK_DATE LASTDEALDATE, /*最终反馈时间*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS CPSTATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DUSER /*当前处理人*/\n"); 
		sql.append("    FROM TT_CRM_COMPLAINT COMPLAINT\n"); 
		sql.append("    LEFT JOIN TM_REGION TRP ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID AND TRP.REGION_TYPE = "+Constant.REGION_TYPE_02+"\n"); 
		sql.append("    LEFT JOIN TM_REGION TRC ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID AND TRC.REGION_TYPE = "+Constant.REGION_TYPE_03+"\n"); 
		sql.append("    LEFT JOIN tm_org_custom ORGOBJ ON ORGOBJ.ORG_ID = COMPLAINT.CP_OBJECT\n"); 
		sql.append("    LEFT JOIN TM_DEALER REOBJ ON REOBJ.DEALER_ID = COMPLAINT.CP_OBJECT \n"); 
		sql.append("    LEFT JOIN TC_USER ACUSER ON ACUSER.USER_ID = COMPLAINT.CP_ACC_USER\n"); 
		sql.append("    LEFT JOIN TM_VHCL_MATERIAL_GROUP SGROUP ON SGROUP.GROUP_ID = COMPLAINT.CP_SERIES_ID AND SGROUP.GROUP_LEVEL = 2\n"); 
		sql.append("    LEFT JOIN TM_ORG ORGPRO ON COMPLAINT.CP_OBJECT_ORG = ORGPRO.ORG_ID AND ORGPRO.DUTY_TYPE = "+Constant.DUTY_TYPE_LARGEREGION+"\n"); 
		sql.append("    LEFT JOIN (SELECT TO_CHAR(MAX(DR.CD_DATE), 'yyyy-MM-dd hh24:mi:ss') LASTDEALDATE, DR.CP_ID, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CD_DATE,NULL)) BACK_DATE\n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DR GROUP BY DR.CP_ID) X ON X.CP_ID = COMPLAINT.CP_ID");
		
		sql.append("    LEFT JOIN (SELECT MAX(DRD.CREATE_DATE) LASTORGDEALDATE, DRD.CP_ID \n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DRD where DRD.cp_status = "+Constant.COMPLAINT_STATUS_DOING_ALREADY+"  GROUP BY DRD.CP_ID) Y ON Y.CP_ID = COMPLAINT.CP_ID");

		sql.append(" where complaint.cp_biz_type = "+Constant.TYPE_COMPLAIN);
		
		sql.append(" and complaint.CP_DEAL_ORG in('"+orgids+"') ");
		if(StringUtil.notNull(vin)){
			sql.append(" and complaint.cp_vin like '%"+vin+"%' ");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and complaint.cp_name like '%"+name+"%' ");
		}
		if(StringUtil.notNull(tele)){
			sql.append(" and complaint.cp_phone like '%"+tele+"%' ");
		}
		if(StringUtil.notNull(level)){
			sql.append(" and complaint.cp_level = '"+level+"' ");
		}
		if(StringUtil.notNull(dealUser)){
			sql.append(" and COMPLAINT.CP_DEAL_USER_NAME like '%"+dealUser+"%' ");
		}
		if(StringUtil.notNull(accUser)){
			sql.append(" and acuser.name like '%"+accUser+"%' ");
		}
		if(StringUtil.notNull(biztype)){
			sql.append(" and complaint.CP_BIZ_CONTENT = '"+biztype+"' ");
		}
		if(StringUtil.notNull(status) && status.equals("unClose")){
			sql.append(" and complaint.cp_status not in ('"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+"','"+Constant.COMPLAINT_STATUS_CLOSE+"') ");
		}else if(StringUtil.notNull(status) && !status.equals("unClose")){
			sql.append(" and complaint.cp_status = '"+status+"' ");
		}
		if(StringUtil.notNull(dateStart)){
			sql.append(" and complaint.cp_acc_date >=to_date('"+dateStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dateEnd)){
			sql.append(" and complaint.cp_acc_date<=to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(dealStart)){
			sql.append(" and complaint.CP_TURN_DATE >=to_date('"+dealStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dealEnd)){
			sql.append(" and complaint.CP_TURN_DATE<=to_date('"+dealEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(region)){
			sql.append(" and complaint.CP_DEAL_ORG = "+ region );
		}
		if(StringUtil.notNull(pro)){
			sql.append(" and complaint.CP_PROVINCE_ID = "+pro);
		}
		sql.append(" order by complaint.CP_ACC_DATE desc ");

		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	//投诉经销商查询
	public PageResult<Map<String, Object>> queryComplaintDealerSearch(String vin,
			String name, String tele, String level,String dealUser, String accUser, String biztype,
			String status, String dealStart, String dealEnd, String dateStart,
			String dateEnd, String region, String pro, String checkStart,
			String checklEnd,String orgids, Integer pageSize, Integer curPage) {
		StringBuffer sql = new StringBuffer();
		// 艾春 9.23 修改 投诉查询语句
		sql.append(" SELECT COMPLAINT.CP_NO        CPNO, /*单号*/\n");
		sql.append("         COMPLAINT.CP_ID        CPID, /*ID*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS    STATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_CL_STATUS CLSTATUS, /*延期状态*/\n"); 
		sql.append("         COMPLAINT.CP_LEVEL     CPLEVEL, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n"); 
		sql.append("         TRP.REGION_NAME      PRO, /*省份*/\n"); 
		sql.append("         TRC.REGION_NAME      CITY, /*城市*/\n"); 
		sql.append("         COMPLAINT.CP_NAME    CTMNAME, /*客户名称*/\n"); 
		sql.append("         COMPLAINT.CP_CUS_ID  CTMID, /*客户ID*/\n"); 
		sql.append("         COMPLAINT.CP_PHONE   PHONE,  /*电话*/\n"); 
		sql.append("         COMPLAINT.CP_CONTENT CPCONT,  /*内容类型*/\n"); 
		sql.append("         NVL(ORGOBJ.ORG_NAME, REOBJ.DEALER_NAME) CPOBJECT,/*抱怨对象*/\n"); 
		sql.append("         COMPLAINT.CREATE_DATE CREATEDATE,/*创建日期*/\n"); 
		sql.append("         ACUSER.NAME ACUSER,/*受理人*/\n"); 
		sql.append("         SGROUP.GROUP_NAME SNAME,/*车种*/\n"); 
		sql.append("         COMPLAINT.CP_VIN VIN,\n"); 
		sql.append("         TO_CHAR(COMPLAINT.CP_BUY_DATE, 'yyyy-MM-dd hh24:mi:ss') BDATE,/*购买日期*/\n"); 
		sql.append("         COMPLAINT.CP_MILEAGE CPMILEAGE, /*行驶里程*/\n"); 
		sql.append("         /*COMPLAINT.CP_MILEAGE_RANGE, /*需转换*/\n"); 
		sql.append("         ORGPRO.ORG_NAME ORGNAME,/*大区*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DEALUSER,/*处理人*/\n"); 
		sql.append("         COMPLAINT.CP_ACC_DATE  CPACCDATE,/*受理日期*/\n"); 
		sql.append("         COMPLAINT.CP_TURN_DATE TURNDATE,/*转出日期*/\n"); 
		sql.append("         decode(COMPLAINT.CP_STATUS,"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.PLEASED+","+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.YAWP+",null) CPSF, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_CLOSE_DATE CPCDATE, /*关闭日期*/\n"); 
		sql.append("         TO_CHAR(CEIL((Y.LASTORGDEALDATE - COMPLAINT.CP_TURN_DATE) * 24)) DEALTIME,");
		sql.append("         COMPLAINT.CP_LIMIT CPLIMIT, /*规定时限*/\n"); 

		sql.append("ceil(\r\n" );
		sql.append(" case when COMPLAINT.Cp_Cl_Date is null then 0\r\n" );
		sql.append("      else case when COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) >= COMPLAINT.Cp_Cl_Once_Date\r\n" );
		sql.append("                then case when COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) >0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) else 0 end\r\n" );
		sql.append("                else case when COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date>0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date else 0 end  end\r\n" );
		sql.append("      end\r\n" );
		sql.append(")DELAYDATE,/*延期天数(天) 取整*/");

		sql.append("         /*COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT SHOULDCLOSETIME, */\n"); 
		sql.append("         /*CEIL((COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT - SYSDATE)*24) OWNTIME, */\n"); 

		sql.append("case when COMPLAINT.Cp_Status in("+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+") then\r\n" );
		sql.append("  case when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_CLOSE+" then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then '未及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE <= nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE > nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '未及时关闭'\r\n" );
		sql.append("          end\r\n" );
		sql.append("end  ISTIMELYCLOSE, /*是否及时关闭*/");			

		sql.append("         COMPLAINT.CP_IS_ONCE_SF CPISONCESF, /*需转换*/\n"); 
		sql.append("         DECODE(COMPLAINT.Cp_Close_Date, NULL, '',\n"); 
		sql.append("           CASE WHEN COMPLAINT.CP_SF="+Constant.PLEASED+" THEN '正常关闭'\n"); 
		sql.append("                WHEN COMPLAINT.CP_SF="+Constant.YAWP+" THEN '不正常关闭'\n"); 
		sql.append("                ELSE '' END) ISNORMALCLOSE, /*是否正常关闭*/\n"); 
		sql.append("         X.BACK_DATE LASTDEALDATE, /*最终反馈时间*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS CPSTATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DUSER /*当前处理人*/\n"); 
		sql.append("    FROM TT_CRM_COMPLAINT COMPLAINT\n"); 
		sql.append("    LEFT JOIN TM_REGION TRP ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID AND TRP.REGION_TYPE = "+Constant.REGION_TYPE_02+"\n"); 
		sql.append("    LEFT JOIN TM_REGION TRC ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID AND TRC.REGION_TYPE = "+Constant.REGION_TYPE_03+"\n"); 
		sql.append("    LEFT JOIN tm_org_custom ORGOBJ ON ORGOBJ.ORG_ID = COMPLAINT.CP_OBJECT\n"); 
		sql.append("    LEFT JOIN TM_DEALER REOBJ ON REOBJ.DEALER_ID = COMPLAINT.CP_OBJECT \n"); 
		sql.append("    LEFT JOIN TC_USER ACUSER ON ACUSER.USER_ID = COMPLAINT.CP_ACC_USER\n"); 
		sql.append("    LEFT JOIN TM_VHCL_MATERIAL_GROUP SGROUP ON SGROUP.GROUP_ID = COMPLAINT.CP_SERIES_ID AND SGROUP.GROUP_LEVEL = 2\n"); 
		sql.append("    LEFT JOIN TM_ORG ORGPRO ON COMPLAINT.CP_OBJECT_ORG = ORGPRO.ORG_ID AND ORGPRO.DUTY_TYPE = "+Constant.DUTY_TYPE_LARGEREGION+"\n"); 
		sql.append("    LEFT JOIN (SELECT TO_CHAR(MAX(DR.CD_DATE), 'yyyy-MM-dd hh24:mi:ss') LASTDEALDATE, DR.CP_ID, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CD_DATE,NULL)) BACK_DATE\n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DR GROUP BY DR.CP_ID) X ON X.CP_ID = COMPLAINT.CP_ID");

		sql.append("    LEFT JOIN (SELECT MAX(DRD.CREATE_DATE) LASTORGDEALDATE, DRD.CP_ID \n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DRD where DRD.cp_status = "+Constant.COMPLAINT_STATUS_DOING_ALREADY+"  GROUP BY DRD.CP_ID) Y ON Y.CP_ID = COMPLAINT.CP_ID");

		
		sql.append(" where complaint.cp_biz_type = "+Constant.TYPE_COMPLAIN);
		sql.append(" and complaint.CP_DEAL_DEALER in('"+orgids+"') ");
		if(StringUtil.notNull(vin)){
			sql.append(" and complaint.cp_vin like '%"+vin+"%' ");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and complaint.cp_name like '%"+name+"%' ");
		}
		if(StringUtil.notNull(tele)){
			sql.append(" and complaint.cp_phone like '%"+tele+"%' ");
		}
		if(StringUtil.notNull(level)){
			sql.append(" and complaint.cp_level = '"+level+"' ");
		}
		if(StringUtil.notNull(dealUser)){
			sql.append(" and COMPLAINT.CP_DEAL_USER_NAME like '%"+dealUser+"%' ");
		}
		if(StringUtil.notNull(accUser)){
			sql.append(" and acuser.name like '%"+accUser+"%' ");
		}
		if(StringUtil.notNull(biztype)){
			sql.append(" and complaint.CP_BIZ_CONTENT = '"+biztype+"' ");
		}
		if(StringUtil.notNull(status) && status.equals("unClose")){
			sql.append(" and complaint.cp_status not in ('"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+"','"+Constant.COMPLAINT_STATUS_CLOSE+"') ");
		}else if(StringUtil.notNull(status) && !status.equals("unClose")){
			sql.append(" and complaint.cp_status = '"+status+"' ");
		}
		if(StringUtil.notNull(dateStart)){
			sql.append(" and complaint.cp_acc_date >=to_date('"+dateStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dateEnd)){
			sql.append(" and complaint.cp_acc_date<=to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(dealStart)){
			sql.append(" and complaint.CP_TURN_DATE >=to_date('"+dealStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dealEnd)){
			sql.append(" and complaint.CP_TURN_DATE<=to_date('"+dealEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(region)){
			sql.append(" and complaint.CP_DEAL_ORG = "+ region );
		}
		if(StringUtil.notNull(pro)){
			sql.append(" and complaint.CP_PROVINCE_ID = "+pro);
		}
		sql.append(" order by complaint.CP_ACC_DATE desc ");

		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	//投诉经销商查询
	public List<Map<String, Object>> queryComplaintDealerSearchList(String vin,
			String name, String tele, String level,String dealUser, String accUser, String biztype,
			String status, String dealStart, String dealEnd, String dateStart,
			String dateEnd, String region, String pro, String checkStart,
			String checklEnd,String orgids) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT COMPLAINT.CP_NO        CPNO, /*单号*/\n");
		sql.append("         COMPLAINT.CP_ID        CPID, /*ID*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS    STATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_CL_STATUS CLSTATUS, /*延期状态*/\n"); 
		sql.append("         COMPLAINT.CP_LEVEL     CPLEVEL, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n"); 
		sql.append("         TRP.REGION_NAME      PRO, /*省份*/\n"); 
		sql.append("         TRC.REGION_NAME      CITY, /*城市*/\n"); 
		sql.append("         COMPLAINT.CP_NAME    CTMNAME, /*客户名称*/\n"); 
		sql.append("         COMPLAINT.CP_CUS_ID  CTMID, /*客户ID*/\n"); 
		sql.append("         COMPLAINT.CP_PHONE   PHONE,  /*电话*/\n"); 
		sql.append("         COMPLAINT.CP_CONTENT CPCONT,  /*内容类型*/\n"); 
		sql.append("         NVL(ORGOBJ.ORG_NAME, REOBJ.DEALER_NAME) CPOBJECT,/*抱怨对象*/\n"); 
		sql.append("         COMPLAINT.CREATE_DATE CREATEDATE,/*创建日期*/\n"); 
		sql.append("         ACUSER.NAME ACUSER,/*受理人*/\n"); 
		sql.append("         SGROUP.GROUP_NAME SNAME,/*车种*/\n"); 
		sql.append("         COMPLAINT.CP_VIN VIN,\n"); 
		sql.append("         TO_CHAR(COMPLAINT.CP_BUY_DATE, 'yyyy-MM-dd hh24:mi:ss') BDATE,/*购买日期*/\n"); 
		sql.append("         COMPLAINT.CP_MILEAGE CPMILEAGE, /*行驶里程*/\n"); 
		sql.append("         /*COMPLAINT.CP_MILEAGE_RANGE, /*需转换*/\n"); 
		sql.append("         ORGPRO.ORG_NAME ORGNAME,/*大区*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DEALUSER,/*处理人*/\n"); 
		sql.append("         COMPLAINT.CP_ACC_DATE  CPACCDATE,/*受理日期*/\n"); 
		sql.append("         COMPLAINT.CP_TURN_DATE TURNDATE,/*转出日期*/\n"); 
		sql.append("         decode(COMPLAINT.CP_STATUS,"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.PLEASED+","+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.YAWP+",null) CPSF, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_CLOSE_DATE CPCDATE, /*关闭日期*/\n"); 
		sql.append("         TO_CHAR(CEIL((Y.LASTORGDEALDATE - COMPLAINT.CP_TURN_DATE) * 24)) DEALTIME,");
		sql.append("         COMPLAINT.CP_LIMIT CPLIMIT, /*规定时限*/\n"); 

		sql.append("ceil(\r\n" );
		sql.append(" case when COMPLAINT.Cp_Cl_Date is null then 0\r\n" );
		sql.append("      else case when COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) >= COMPLAINT.Cp_Cl_Once_Date\r\n" );
		sql.append("                then case when COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) >0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) else 0 end\r\n" );
		sql.append("                else case when COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date>0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date else 0 end  end\r\n" );
		sql.append("      end\r\n" );
		sql.append(")DELAYDATE,/*延期天数(天) 取整*/");

		sql.append("         /*COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT SHOULDCLOSETIME, */\n"); 
		sql.append("         /*CEIL((COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT - SYSDATE)*24) OWNTIME, */\n"); 


		sql.append("case when COMPLAINT.Cp_Status in("+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+") then\r\n" );
		sql.append("  case when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_CLOSE+" then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then '未及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE <= nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE > nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '未及时关闭'\r\n" );
		sql.append("          end\r\n" );
		sql.append("end  ISTIMELYCLOSE, /*是否及时关闭*/");			

 
		sql.append("         COMPLAINT.CP_IS_ONCE_SF CPISONCESF, /*需转换*/\n"); 
		sql.append("         DECODE(COMPLAINT.Cp_Close_Date, NULL, '',\n"); 
		sql.append("           CASE WHEN COMPLAINT.Cp_Close_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) THEN '不正常关闭'\n"); 
		sql.append("                WHEN COMPLAINT.Cp_Close_Date <= COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) THEN '正常关闭'\n"); 
		sql.append("                ELSE '' END) ISNORMALCLOSE, /*是否正常关闭*/\n"); 
		sql.append("         X.BACK_DATE LASTDEALDATE, /*最终反馈时间*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS CPSTATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DUSER /*当前处理人*/\n"); 
		sql.append("    FROM TT_CRM_COMPLAINT COMPLAINT\n"); 
		sql.append("    LEFT JOIN TM_REGION TRP ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID AND TRP.REGION_TYPE = "+Constant.REGION_TYPE_02+"\n"); 
		sql.append("    LEFT JOIN TM_REGION TRC ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID AND TRC.REGION_TYPE = "+Constant.REGION_TYPE_03+"\n"); 
		sql.append("    LEFT JOIN tm_org_custom ORGOBJ ON ORGOBJ.ORG_ID = COMPLAINT.CP_OBJECT\n"); 
		sql.append("    LEFT JOIN TM_DEALER REOBJ ON REOBJ.DEALER_ID = COMPLAINT.CP_OBJECT \n"); 
		sql.append("    LEFT JOIN TC_USER ACUSER ON ACUSER.USER_ID = COMPLAINT.CP_ACC_USER\n"); 
		sql.append("    LEFT JOIN TM_VHCL_MATERIAL_GROUP SGROUP ON SGROUP.GROUP_ID = COMPLAINT.CP_SERIES_ID AND SGROUP.GROUP_LEVEL = 2\n"); 
		sql.append("    LEFT JOIN TM_ORG ORGPRO ON COMPLAINT.CP_OBJECT_ORG = ORGPRO.ORG_ID AND ORGPRO.DUTY_TYPE = "+Constant.DUTY_TYPE_LARGEREGION+"\n"); 
		sql.append("    LEFT JOIN (SELECT TO_CHAR(MAX(DR.CD_DATE), 'yyyy-MM-dd hh24:mi:ss') LASTDEALDATE, DR.CP_ID, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CD_DATE,NULL)) BACK_DATE\n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DR GROUP BY DR.CP_ID) X ON X.CP_ID = COMPLAINT.CP_ID");

		sql.append("    LEFT JOIN (SELECT MAX(DRD.CREATE_DATE) LASTORGDEALDATE, DRD.CP_ID \n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DRD where DRD.cp_status = "+Constant.COMPLAINT_STATUS_DOING_ALREADY+"  GROUP BY DRD.CP_ID) Y ON Y.CP_ID = COMPLAINT.CP_ID");

		sql.append(" where complaint.cp_biz_type = "+Constant.TYPE_COMPLAIN);
		sql.append(" and complaint.CP_DEAL_DEALER in('"+orgids+"') ");
		if(StringUtil.notNull(vin)){
			sql.append(" and complaint.cp_vin like '%"+vin+"%' ");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and complaint.cp_name like '%"+name+"%' ");
		}
		if(StringUtil.notNull(tele)){
			sql.append(" and complaint.cp_phone like '%"+tele+"%' ");
		}
		if(StringUtil.notNull(level)){
			sql.append(" and complaint.cp_level = '"+level+"' ");
		}
		if(StringUtil.notNull(dealUser)){
			sql.append(" and COMPLAINT.CP_DEAL_USER_NAME like '%"+dealUser+"%' ");
		}
		if(StringUtil.notNull(accUser)){
			sql.append(" and acuser.name like '%"+accUser+"%' ");
		}
		if(StringUtil.notNull(biztype)){
			sql.append(" and complaint.CP_BIZ_CONTENT = '"+biztype+"' ");
		}
		if(StringUtil.notNull(status) && status.equals("unClose")){
			sql.append(" and complaint.cp_status not in ('"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+"','"+Constant.COMPLAINT_STATUS_CLOSE+"') ");
		}else if(StringUtil.notNull(status) && !status.equals("unClose")){
			sql.append(" and complaint.cp_status = '"+status+"' ");
		}
		if(StringUtil.notNull(dateStart)){
			sql.append(" and complaint.cp_acc_date >=to_date('"+dateStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dateEnd)){
			sql.append(" and complaint.cp_acc_date<=to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(dealStart)){
			sql.append(" and complaint.CP_TURN_DATE >=to_date('"+dealStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dealEnd)){
			sql.append(" and complaint.CP_TURN_DATE<=to_date('"+dealEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(region)){
			sql.append(" and complaint.CP_DEAL_ORG = "+ region );
		}
		if(StringUtil.notNull(pro)){
			sql.append(" and complaint.CP_PROVINCE_ID = "+pro);
		}
		sql.append(" order by complaint.CP_ACC_DATE desc ");

		return (List<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName());
	}
	
	public List<Map<String, Object>> queryComplaintInfoByOrgids(String vin,
			String name, String tele, String level,String dealUser, String accUser, String biztype,
			String status, String dealStart, String dealEnd, String dateStart,
			String dateEnd, String region, String pro, String checkStart,
			String checklEnd,String orgids) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT COMPLAINT.CP_NO        CPNO, /*单号*/\n");
		sql.append("         COMPLAINT.CP_ID        CPID, /*ID*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS    STATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_CL_STATUS CLSTATUS, /*延期状态*/\n"); 
		sql.append("         COMPLAINT.CP_LEVEL     CPLEVEL, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n"); 
		sql.append("         TRP.REGION_NAME      PRO, /*省份*/\n"); 
		sql.append("         TRC.REGION_NAME      CITY, /*城市*/\n"); 
		sql.append("         COMPLAINT.CP_NAME    CTMNAME, /*客户名称*/\n"); 
		sql.append("         COMPLAINT.CP_CUS_ID  CTMID, /*客户ID*/\n"); 
		sql.append("         COMPLAINT.CP_PHONE   PHONE,  /*电话*/\n"); 
		sql.append("         COMPLAINT.CP_CONTENT CPCONT,  /*内容类型*/\n"); 
		sql.append("         NVL(ORGOBJ.ORG_NAME, REOBJ.DEALER_NAME) CPOBJECT,/*抱怨对象*/\n"); 
		sql.append("         COMPLAINT.CREATE_DATE CREATEDATE,/*创建日期*/\n"); 
		sql.append("         ACUSER.NAME ACUSER,/*受理人*/\n"); 
		sql.append("         SGROUP.GROUP_NAME SNAME,/*车种*/\n"); 
		sql.append("         COMPLAINT.CP_VIN VIN,\n"); 
		sql.append("         TO_CHAR(COMPLAINT.CP_BUY_DATE, 'yyyy-MM-dd hh24:mi:ss') BDATE,/*购买日期*/\n"); 
		sql.append("         COMPLAINT.CP_MILEAGE CPMILEAGE, /*行驶里程*/\n"); 
		sql.append("         /*COMPLAINT.CP_MILEAGE_RANGE, /*需转换*/\n"); 
		sql.append("         ORGPRO.ORG_NAME ORGNAME,/*大区*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DEALUSER,/*处理人*/\n"); 
		sql.append("         COMPLAINT.CP_ACC_DATE  CPACCDATE,/*受理日期*/\n"); 
		sql.append("         COMPLAINT.CP_TURN_DATE TURNDATE,/*转出日期*/\n"); 
		sql.append("         decode(COMPLAINT.CP_STATUS,"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.PLEASED+","+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.YAWP+",null) CPSF, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_CLOSE_DATE CPCDATE, /*关闭日期*/\n"); 
		sql.append("         TO_CHAR(CEIL((Y.LASTORGDEALDATE - COMPLAINT.CP_TURN_DATE) * 24)) DEALTIME,");
		sql.append("         COMPLAINT.CP_LIMIT CPLIMIT, /*规定时限*/\n"); 

		sql.append("ceil(\r\n" );
		sql.append(" case when COMPLAINT.Cp_Cl_Date is null then 0\r\n" );
		sql.append("      else case when COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) >= COMPLAINT.Cp_Cl_Once_Date\r\n" );
		sql.append("                then case when COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) >0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) else 0 end\r\n" );
		sql.append("                else case when COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date>0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date else 0 end  end\r\n" );
		sql.append("      end\r\n" );
		sql.append(")DELAYDATE,/*延期天数(天) 取整*/");

		sql.append("         /*COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT SHOULDCLOSETIME, */\n"); 
		sql.append("         /*CEIL((COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT - SYSDATE)*24) OWNTIME, */\n"); 

		sql.append("case when COMPLAINT.Cp_Status in("+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+") then\r\n" );
		sql.append("  case when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_CLOSE+" then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then '未及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE <= nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE > nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '未及时关闭'\r\n" );
		sql.append("          end\r\n" );
		sql.append("end  ISTIMELYCLOSE, /*是否及时关闭*/");			

		sql.append("         COMPLAINT.CP_IS_ONCE_SF CPISONCESF, /*需转换*/\n"); 
		sql.append("         DECODE(COMPLAINT.Cp_Close_Date, NULL, '',\n"); 
		sql.append("           CASE WHEN COMPLAINT.CP_SF="+Constant.PLEASED+" THEN '正常关闭'\n"); 
		sql.append("                WHEN COMPLAINT.CP_SF="+Constant.YAWP+" THEN '不正常关闭'\n"); 
		sql.append("                ELSE '' END) ISNORMALCLOSE, /*是否正常关闭*/\n"); 
		sql.append("         X.BACK_DATE LASTDEALDATE, /*最终反馈时间*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS CPSTATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DUSER /*当前处理人*/\n"); 
		sql.append("    FROM TT_CRM_COMPLAINT COMPLAINT\n"); 
		sql.append("    LEFT JOIN TM_REGION TRP ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID AND TRP.REGION_TYPE = "+Constant.REGION_TYPE_02+"\n"); 
		sql.append("    LEFT JOIN TM_REGION TRC ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID AND TRC.REGION_TYPE = "+Constant.REGION_TYPE_03+"\n"); 
		sql.append("    LEFT JOIN tm_org_custom ORGOBJ ON ORGOBJ.ORG_ID = COMPLAINT.CP_OBJECT\n"); 
		sql.append("    LEFT JOIN TM_DEALER REOBJ ON REOBJ.DEALER_ID = COMPLAINT.CP_OBJECT \n"); 
		sql.append("    LEFT JOIN TC_USER ACUSER ON ACUSER.USER_ID = COMPLAINT.CP_ACC_USER\n"); 
		sql.append("    LEFT JOIN TM_VHCL_MATERIAL_GROUP SGROUP ON SGROUP.GROUP_ID = COMPLAINT.CP_SERIES_ID AND SGROUP.GROUP_LEVEL = 2\n"); 
		sql.append("    LEFT JOIN TM_ORG ORGPRO ON COMPLAINT.CP_OBJECT_ORG = ORGPRO.ORG_ID AND ORGPRO.DUTY_TYPE = "+Constant.DUTY_TYPE_LARGEREGION+"\n"); 
		sql.append("    LEFT JOIN (SELECT TO_CHAR(MAX(DR.CD_DATE), 'yyyy-MM-dd hh24:mi:ss') LASTDEALDATE, DR.CP_ID, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CD_DATE,NULL)) BACK_DATE\n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DR GROUP BY DR.CP_ID) X ON X.CP_ID = COMPLAINT.CP_ID");

		sql.append("    LEFT JOIN (SELECT MAX(DRD.CREATE_DATE) LASTORGDEALDATE, DRD.CP_ID \n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DRD where DRD.cp_status = "+Constant.COMPLAINT_STATUS_DOING_ALREADY+"  GROUP BY DRD.CP_ID) Y ON Y.CP_ID = COMPLAINT.CP_ID");

		sql.append(" where complaint.cp_biz_type = "+Constant.TYPE_COMPLAIN);
		sql.append(" and complaint.CP_DEAL_ORG in('"+orgids+"') ");
		if(StringUtil.notNull(vin)){
			sql.append(" and complaint.cp_vin like '%"+vin+"%' ");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and complaint.cp_name like '%"+name+"%' ");
		}
		if(StringUtil.notNull(tele)){
			sql.append(" and complaint.cp_phone like '%"+tele+"%' ");
		}
		if(StringUtil.notNull(level)){
			sql.append(" and complaint.cp_level = '"+level+"' ");
		}
		if(StringUtil.notNull(dealUser)){
			sql.append(" and COMPLAINT.CP_DEAL_USER_NAME like '%"+dealUser+"%' ");
		}
		if(StringUtil.notNull(accUser)){
			sql.append(" and acuser.name like '%"+accUser+"%' ");
		}
		if(StringUtil.notNull(biztype)){
			sql.append(" and complaint.CP_BIZ_CONTENT = '"+biztype+"' ");
		}
		if(StringUtil.notNull(status) && status.equals("unClose")){
			sql.append(" and complaint.cp_status not in ('"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+"','"+Constant.COMPLAINT_STATUS_CLOSE+"') ");
		}else if(StringUtil.notNull(status) && !status.equals("unClose")){
			sql.append(" and complaint.cp_status = '"+status+"' ");
		}
		if(StringUtil.notNull(dateStart)){
			sql.append(" and complaint.cp_acc_date >=to_date('"+dateStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dateEnd)){
			sql.append(" and complaint.cp_acc_date<=to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(dealStart)){
			sql.append(" and complaint.CP_TURN_DATE >=to_date('"+dealStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dealEnd)){
			sql.append(" and complaint.CP_TURN_DATE<=to_date('"+dealEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(region)){
			sql.append(" and complaint.CP_DEAL_ORG = "+ region );
		}
		if(StringUtil.notNull(pro)){
			sql.append(" and complaint.CP_PROVINCE_ID = "+pro);
		}
		sql.append(" order by complaint.CP_ACC_DATE desc ");

		return this.pageQuery(sql.toString(), null, getFunName());
	}
	//投诉大区查询
	public PageResult<Map<String, Object>> queryComplaintInfo(String vin,
			String name, String tele, String level,String dealUser, String accUser, String biztype,
			String status, String dealStart, String dealEnd, String dateStart,
			String dateEnd, String region, String pro, String checkStart,
			String checklEnd,String userids,String state, Integer pageSize, Integer curPage) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT COMPLAINT.CP_NO        CPNO, /*单号*/\n");
		sql.append("         COMPLAINT.CP_ID        CPID, /*ID*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS    STATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_CL_STATUS CLSTATUS, /*延期状态*/\n"); 
		sql.append("         COMPLAINT.CP_LEVEL     CPLEVEL, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n"); 
		sql.append("         TRP.REGION_NAME      PRO, /*省份*/\n"); 
		sql.append("         TRC.REGION_NAME      CITY, /*城市*/\n"); 
		sql.append("         COMPLAINT.CP_NAME    CTMNAME, /*客户名称*/\n"); 
		sql.append("         COMPLAINT.CP_CUS_ID  CTMID, /*客户ID*/\n"); 
		sql.append("         COMPLAINT.CP_PHONE   PHONE,  /*电话*/\n"); 
		sql.append("         COMPLAINT.CP_CONTENT CPCONT,  /*内容类型*/\n"); 
		sql.append("         NVL(ORGOBJ.ORG_NAME, REOBJ.DEALER_NAME) CPOBJECT,/*抱怨对象*/\n"); 
		sql.append("         COMPLAINT.CREATE_DATE CREATEDATE,/*创建日期*/\n"); 
		sql.append("         ACUSER.NAME ACUSER,/*受理人*/\n"); 
		sql.append("         SGROUP.GROUP_NAME SNAME,/*车种*/\n"); 
		sql.append("         COMPLAINT.CP_VIN VIN,\n"); 
		sql.append("         TO_CHAR(COMPLAINT.CP_BUY_DATE, 'yyyy-MM-dd hh24:mi:ss') BDATE,/*购买日期*/\n"); 
		sql.append("         COMPLAINT.CP_MILEAGE CPMILEAGE, /*行驶里程*/\n"); 
		sql.append("         /*COMPLAINT.CP_MILEAGE_RANGE, /*需转换*/\n"); 
		sql.append("         ORGPRO.ORG_NAME ORGNAME,/*大区*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DEALUSER,/*处理人*/\n"); 
		sql.append("         COMPLAINT.CP_ACC_DATE  CPACCDATE,/*受理日期*/\n"); 
		sql.append("         COMPLAINT.CP_TURN_DATE TURNDATE,/*转出日期*/\n"); 
		sql.append("         decode(COMPLAINT.CP_STATUS,"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.PLEASED+","+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.YAWP+",null) CPSF, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_CLOSE_DATE CPCDATE, /*关闭日期*/\n"); 
		sql.append("         TO_CHAR(CEIL((Y.LASTORGDEALDATE - COMPLAINT.CP_TURN_DATE) * 24)) DEALTIME,");
		sql.append("         COMPLAINT.CP_LIMIT CPLIMIT, /*规定时限*/\n"); 

		sql.append("ceil(\r\n" );
		sql.append(" case when COMPLAINT.Cp_Cl_Date is null then 0\r\n" );
		sql.append("      else case when COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) >= COMPLAINT.Cp_Cl_Once_Date\r\n" );
		sql.append("                then case when COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) >0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) else 0 end\r\n" );
		sql.append("                else case when COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date>0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date else 0 end  end\r\n" );
		sql.append("      end\r\n" );
		sql.append(")DELAYDATE,/*延期天数(天) 取整*/");
 
		sql.append("         /*COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT SHOULDCLOSETIME, */\n"); 
		sql.append("         /*CEIL((COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT - SYSDATE)*24) OWNTIME, */\n"); 

		sql.append("case when COMPLAINT.Cp_Status in("+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+") then\r\n" );
		sql.append("  case when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_CLOSE+" then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then '未及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE <= nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE > nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '未及时关闭'\r\n" );
		sql.append("          end\r\n" );
		sql.append("end  ISTIMELYCLOSE, /*是否及时关闭*/");			

		sql.append("         COMPLAINT.CP_IS_ONCE_SF CPISONCESF, /*需转换*/\n"); 
		sql.append("         DECODE(COMPLAINT.Cp_Close_Date, NULL, '',\n"); 
		sql.append("           CASE WHEN COMPLAINT.Cp_Close_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) THEN '不正常关闭'\n"); 
		sql.append("                WHEN COMPLAINT.Cp_Close_Date <= COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) THEN '正常关闭'\n"); 
		sql.append("                ELSE '' END) ISNORMALCLOSE, /*是否正常关闭*/\n"); 
		sql.append("         X.BACK_DATE LASTDEALDATE, /*最终反馈时间*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS CPSTATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DUSER /*当前处理人*/\n"); 
		sql.append("    FROM TT_CRM_COMPLAINT COMPLAINT\n"); 
		sql.append("    LEFT JOIN TM_REGION TRP ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID AND TRP.REGION_TYPE = "+Constant.REGION_TYPE_02+"\n"); 
		sql.append("    LEFT JOIN TM_REGION TRC ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID AND TRC.REGION_TYPE = "+Constant.REGION_TYPE_03+"\n"); 
		sql.append("    LEFT JOIN tm_org_custom ORGOBJ ON ORGOBJ.ORG_ID = COMPLAINT.CP_OBJECT\n"); 
		sql.append("    LEFT JOIN TM_DEALER REOBJ ON REOBJ.DEALER_ID = COMPLAINT.CP_OBJECT \n"); 
		sql.append("    LEFT JOIN TC_USER ACUSER ON ACUSER.USER_ID = COMPLAINT.CP_ACC_USER\n"); 
		sql.append("    LEFT JOIN TM_VHCL_MATERIAL_GROUP SGROUP ON SGROUP.GROUP_ID = COMPLAINT.CP_SERIES_ID AND SGROUP.GROUP_LEVEL = 2\n"); 
		sql.append("    LEFT JOIN TM_ORG ORGPRO ON COMPLAINT.CP_OBJECT_ORG = ORGPRO.ORG_ID AND ORGPRO.DUTY_TYPE = "+Constant.DUTY_TYPE_LARGEREGION+"\n"); 
		sql.append("    LEFT JOIN (SELECT TO_CHAR(MAX(DR.CD_DATE), 'yyyy-MM-dd hh24:mi:ss') LASTDEALDATE, DR.CP_ID, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CD_DATE,NULL)) BACK_DATE\n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DR GROUP BY DR.CP_ID) X ON X.CP_ID = COMPLAINT.CP_ID");

		sql.append("    LEFT JOIN (SELECT MAX(DRD.CREATE_DATE) LASTORGDEALDATE, DRD.CP_ID \n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DRD where DRD.cp_status = "+Constant.COMPLAINT_STATUS_DOING_ALREADY+"  GROUP BY DRD.CP_ID) Y ON Y.CP_ID = COMPLAINT.CP_ID");
		
		sql.append(" where complaint.cp_biz_type = "+Constant.TYPE_COMPLAIN);
		sql.append(" and complaint.CP_DEAL_ORG in ('"+userids+"') and (complaint.CP_DEAL_DEALER is null or complaint.CP_DEAL_DEALER = -1) and complaint.CP_STATUS in('"+state+"') ");
		if(StringUtil.notNull(vin)){
			sql.append(" and complaint.cp_vin like '%"+vin+"%' ");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and complaint.cp_name like '%"+name+"%' ");
		}
		if(StringUtil.notNull(tele)){
			sql.append(" and complaint.cp_phone like '%"+tele+"%' ");
		}
		if(StringUtil.notNull(level)){
			sql.append(" and complaint.cp_level = '"+level+"' ");
		}
		if(StringUtil.notNull(dealUser)){
			sql.append(" and COMPLAINT.CP_DEAL_USER_NAME like '%"+dealUser+"%' ");
		}
		if(StringUtil.notNull(accUser)){
			sql.append(" and acuser.name like '%"+accUser+"%' ");
		}
		if(StringUtil.notNull(biztype)){
			sql.append(" and complaint.CP_BIZ_CONTENT = '"+biztype+"' ");
		}
		if(StringUtil.notNull(status) && status.equals("unClose")){
			sql.append(" and complaint.cp_status not in ('"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+"','"+Constant.COMPLAINT_STATUS_CLOSE+"') ");
		}else if(StringUtil.notNull(status) && !status.equals("unClose")){
			sql.append(" and complaint.cp_status = '"+status+"' ");
		}
		if(StringUtil.notNull(dateStart)){
			sql.append(" and complaint.cp_acc_date >=to_date('"+dateStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dateEnd)){
			sql.append(" and complaint.cp_acc_date<=to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(dealStart)){
			sql.append(" and complaint.CP_TURN_DATE >=to_date('"+dealStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dealEnd)){
			sql.append(" and complaint.CP_TURN_DATE<=to_date('"+dealEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(region)){
			sql.append(" and complaint.CP_DEAL_ORG = "+ region );
		}
		if(StringUtil.notNull(pro)){
			sql.append(" and complaint.CP_PROVINCE_ID = "+pro);
		}
		sql.append(" order by complaint.CP_ACC_DATE desc ");
		
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	//经销商投诉处理List
	public List<Map<String, Object>> queryComplaintDealerDealList(String vin,
			String name, String tele, String level,String dealUser, String accUser, String biztype,
			String status, String dealStart, String dealEnd, String dateStart,
			String dateEnd, String region, String pro, String checkStart,
			String checklEnd,String userids,String state) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT COMPLAINT.CP_NO        CPNO, /*单号*/\n");
		sql.append("         COMPLAINT.CP_ID        CPID, /*ID*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS    STATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_CL_STATUS CLSTATUS, /*延期状态*/\n"); 
		sql.append("         COMPLAINT.CP_LEVEL     CPLEVEL, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n"); 
		sql.append("         TRP.REGION_NAME      PRO, /*省份*/\n"); 
		sql.append("         TRC.REGION_NAME      CITY, /*城市*/\n"); 
		sql.append("         COMPLAINT.CP_NAME    CTMNAME, /*客户名称*/\n"); 
		sql.append("         COMPLAINT.CP_CUS_ID  CTMID, /*客户ID*/\n"); 
		sql.append("         COMPLAINT.CP_PHONE   PHONE,  /*电话*/\n"); 
		sql.append("         COMPLAINT.CP_CONTENT CPCONT,  /*内容类型*/\n"); 
		sql.append("         NVL(ORGOBJ.ORG_NAME, REOBJ.DEALER_NAME) CPOBJECT,/*抱怨对象*/\n"); 
		sql.append("         COMPLAINT.CREATE_DATE CREATEDATE,/*创建日期*/\n"); 
		sql.append("         ACUSER.NAME ACUSER,/*受理人*/\n"); 
		sql.append("         SGROUP.GROUP_NAME SNAME,/*车种*/\n"); 
		sql.append("         COMPLAINT.CP_VIN VIN,\n"); 
		sql.append("         TO_CHAR(COMPLAINT.CP_BUY_DATE, 'yyyy-MM-dd hh24:mi:ss') BDATE,/*购买日期*/\n"); 
		sql.append("         COMPLAINT.CP_MILEAGE CPMILEAGE, /*行驶里程*/\n"); 
		sql.append("         /*COMPLAINT.CP_MILEAGE_RANGE, /*需转换*/\n"); 
		sql.append("         ORGPRO.ORG_NAME ORGNAME,/*大区*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DEALUSER,/*处理人*/\n"); 
		sql.append("         COMPLAINT.CP_ACC_DATE  CPACCDATE,/*受理日期*/\n"); 
		sql.append("         COMPLAINT.CP_TURN_DATE TURNDATE,/*转出日期*/\n"); 
		sql.append("         decode(COMPLAINT.CP_STATUS,"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.PLEASED+","+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.YAWP+",null) CPSF, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_CLOSE_DATE CPCDATE, /*关闭日期*/\n"); 
		sql.append("         TO_CHAR(CEIL((Y.LASTORGDEALDATE - COMPLAINT.CP_TURN_DATE) * 24)) DEALTIME,");
		sql.append("         COMPLAINT.CP_LIMIT CPLIMIT, /*规定时限*/\n"); 

		sql.append("ceil(\r\n" );
		sql.append(" case when COMPLAINT.Cp_Cl_Date is null then 0\r\n" );
		sql.append("      else case when COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) >= COMPLAINT.Cp_Cl_Once_Date\r\n" );
		sql.append("                then case when COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) >0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) else 0 end\r\n" );
		sql.append("                else case when COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date>0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date else 0 end  end\r\n" );
		sql.append("      end\r\n" );
		sql.append(")DELAYDATE,/*延期天数(天) 取整*/");

		sql.append("         /*COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT SHOULDCLOSETIME, */\n"); 
		sql.append("         /*CEIL((COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT - SYSDATE)*24) OWNTIME, */\n"); 

		sql.append("case when COMPLAINT.Cp_Status in("+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+") then\r\n" );
		sql.append("  case when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_CLOSE+" then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then '未及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE <= nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE > nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '未及时关闭'\r\n" );
		sql.append("          end\r\n" );
		sql.append("end  ISTIMELYCLOSE, /*是否及时关闭*/");			

		sql.append("         COMPLAINT.CP_IS_ONCE_SF CPISONCESF, /*需转换*/\n"); 
		sql.append("         DECODE(COMPLAINT.Cp_Close_Date, NULL, '',\n"); 
		sql.append("           CASE WHEN COMPLAINT.CP_SF="+Constant.PLEASED+" THEN '正常关闭'\n"); 
		sql.append("                WHEN COMPLAINT.CP_SF="+Constant.YAWP+" THEN '不正常关闭'\n"); 
		sql.append("                ELSE '' END) ISNORMALCLOSE, /*是否正常关闭*/\n"); 
		sql.append("         X.BACK_DATE LASTDEALDATE, /*最终反馈时间*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS CPSTATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DUSER /*当前处理人*/\n"); 
		sql.append("    FROM TT_CRM_COMPLAINT COMPLAINT\n"); 
		sql.append("    LEFT JOIN TM_REGION TRP ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID AND TRP.REGION_TYPE = "+Constant.REGION_TYPE_02+"\n"); 
		sql.append("    LEFT JOIN TM_REGION TRC ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID AND TRC.REGION_TYPE = "+Constant.REGION_TYPE_03+"\n"); 
		sql.append("    LEFT JOIN tm_org_custom ORGOBJ ON ORGOBJ.ORG_ID = COMPLAINT.CP_OBJECT\n"); 
		sql.append("    LEFT JOIN TM_DEALER REOBJ ON REOBJ.DEALER_ID = COMPLAINT.CP_OBJECT \n"); 
		sql.append("    LEFT JOIN TC_USER ACUSER ON ACUSER.USER_ID = COMPLAINT.CP_ACC_USER\n"); 
		sql.append("    LEFT JOIN TM_VHCL_MATERIAL_GROUP SGROUP ON SGROUP.GROUP_ID = COMPLAINT.CP_SERIES_ID AND SGROUP.GROUP_LEVEL = 2\n"); 
		sql.append("    LEFT JOIN TM_ORG ORGPRO ON COMPLAINT.CP_OBJECT_ORG = ORGPRO.ORG_ID AND ORGPRO.DUTY_TYPE = "+Constant.DUTY_TYPE_LARGEREGION+"\n"); 
		sql.append("    LEFT JOIN (SELECT TO_CHAR(MAX(DR.CD_DATE), 'yyyy-MM-dd hh24:mi:ss') LASTDEALDATE, DR.CP_ID, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CD_DATE,NULL)) BACK_DATE\n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DR GROUP BY DR.CP_ID) X ON X.CP_ID = COMPLAINT.CP_ID");

		sql.append("    LEFT JOIN (SELECT MAX(DRD.CREATE_DATE) LASTORGDEALDATE, DRD.CP_ID \n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DRD where DRD.cp_status = "+Constant.COMPLAINT_STATUS_DOING_ALREADY+"  GROUP BY DRD.CP_ID) Y ON Y.CP_ID = COMPLAINT.CP_ID");
		
		sql.append(" where complaint.cp_biz_type = "+Constant.TYPE_COMPLAIN);
		sql.append(" and complaint.CP_DEAL_DEALER in ('"+userids+"') and complaint.CP_STATUS in('"+state+"') ");
		if(StringUtil.notNull(vin)){
			sql.append(" and complaint.cp_vin like '%"+vin+"%' ");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and complaint.cp_name like '%"+name+"%' ");
		}
		if(StringUtil.notNull(tele)){
			sql.append(" and complaint.cp_phone like '%"+tele+"%' ");
		}
		if(StringUtil.notNull(level)){
			sql.append(" and complaint.cp_level = '"+level+"' ");
		}
		if(StringUtil.notNull(dealUser)){
			sql.append(" and COMPLAINT.CP_DEAL_USER_NAME like '%"+dealUser+"%' ");
		}
		if(StringUtil.notNull(accUser)){
			sql.append(" and acuser.name like '%"+accUser+"%' ");
		}
		if(StringUtil.notNull(biztype)){
			sql.append(" and complaint.CP_BIZ_CONTENT = '"+biztype+"' ");
		}
		if(StringUtil.notNull(status) && status.equals("unClose")){
			sql.append(" and complaint.cp_status not in ('"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+"','"+Constant.COMPLAINT_STATUS_CLOSE+"') ");
		}else if(StringUtil.notNull(status) && !status.equals("unClose")){
			sql.append(" and complaint.cp_status = '"+status+"' ");
		}
		if(StringUtil.notNull(dateStart)){
			sql.append(" and complaint.cp_acc_date >=to_date('"+dateStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dateEnd)){
			sql.append(" and complaint.cp_acc_date<=to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(dealStart)){
			sql.append(" and complaint.CP_TURN_DATE >=to_date('"+dealStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dealEnd)){
			sql.append(" and complaint.CP_TURN_DATE<=to_date('"+dealEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(region)){
			sql.append(" and complaint.CP_DEAL_ORG = "+ region );
		}
		if(StringUtil.notNull(pro)){
			sql.append(" and complaint.CP_PROVINCE_ID = "+pro);
		}
		sql.append(" order by complaint.CP_ACC_DATE desc ");
		
		return (List<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName());
	}
	
	//经销商投诉处理
	public PageResult<Map<String, Object>> queryComplaintDealerDeal(String vin,
			String name, String tele, String level,String dealUser, String accUser, String biztype,
			String status, String dealStart, String dealEnd, String dateStart,
			String dateEnd, String region, String pro, String checkStart,
			String checklEnd,String userids,String state, Integer pageSize, Integer curPage) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT COMPLAINT.CP_NO        CPNO, /*单号*/\n");
		sql.append("         COMPLAINT.CP_ID        CPID, /*ID*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS    STATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_CL_STATUS CLSTATUS, /*延期状态*/\n"); 
		sql.append("         COMPLAINT.CP_LEVEL     CPLEVEL, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n"); 
		sql.append("         TRP.REGION_NAME      PRO, /*省份*/\n"); 
		sql.append("         TRC.REGION_NAME      CITY, /*城市*/\n"); 
		sql.append("         COMPLAINT.CP_NAME    CTMNAME, /*客户名称*/\n"); 
		sql.append("         COMPLAINT.CP_CUS_ID  CTMID, /*客户ID*/\n"); 
		sql.append("         COMPLAINT.CP_PHONE   PHONE,  /*电话*/\n"); 
		sql.append("         COMPLAINT.CP_CONTENT CPCONT,  /*内容类型*/\n"); 
		sql.append("         NVL(ORGOBJ.ORG_NAME, REOBJ.DEALER_NAME) CPOBJECT,/*抱怨对象*/\n"); 
		sql.append("         COMPLAINT.CREATE_DATE CREATEDATE,/*创建日期*/\n"); 
		sql.append("         ACUSER.NAME ACUSER,/*受理人*/\n"); 
		sql.append("         SGROUP.GROUP_NAME SNAME,/*车种*/\n"); 
		sql.append("         COMPLAINT.CP_VIN VIN,\n"); 
		sql.append("         TO_CHAR(COMPLAINT.CP_BUY_DATE, 'yyyy-MM-dd hh24:mi:ss') BDATE,/*购买日期*/\n"); 
		sql.append("         COMPLAINT.CP_MILEAGE CPMILEAGE, /*行驶里程*/\n"); 
		sql.append("         /*COMPLAINT.CP_MILEAGE_RANGE, /*需转换*/\n"); 
		sql.append("         ORGPRO.ORG_NAME ORGNAME,/*大区*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DEALUSER,/*处理人*/\n"); 
		sql.append("         COMPLAINT.CP_ACC_DATE  CPACCDATE,/*受理日期*/\n"); 
		sql.append("         COMPLAINT.CP_TURN_DATE TURNDATE,/*转出日期*/\n"); 
		sql.append("         decode(COMPLAINT.CP_STATUS,"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.PLEASED+","+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.YAWP+",null) CPSF, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_CLOSE_DATE CPCDATE, /*关闭日期*/\n"); 
		sql.append("         TO_CHAR(CEIL((Y.LASTORGDEALDATE - COMPLAINT.CP_TURN_DATE) * 24)) DEALTIME,");
		sql.append("         COMPLAINT.CP_LIMIT CPLIMIT, /*规定时限*/\n"); 

		sql.append("ceil(\r\n" );
		sql.append(" case when COMPLAINT.Cp_Cl_Date is null then 0\r\n" );
		sql.append("      else case when COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) >= COMPLAINT.Cp_Cl_Once_Date\r\n" );
		sql.append("                then case when COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) >0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) else 0 end\r\n" );
		sql.append("                else case when COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date>0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date else 0 end  end\r\n" );
		sql.append("      end\r\n" );
		sql.append(")DELAYDATE,/*延期天数(天) 取整*/");

		sql.append("         /*COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT SHOULDCLOSETIME, */\n"); 
		sql.append("         /*CEIL((COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT - SYSDATE)*24) OWNTIME, */\n"); 

		sql.append("case when COMPLAINT.Cp_Status in("+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+") then\r\n" );
		sql.append("  case when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_CLOSE+" then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then '未及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE <= nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE > nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '未及时关闭'\r\n" );
		sql.append("          end\r\n" );
		sql.append("end  ISTIMELYCLOSE, /*是否及时关闭*/");			

		sql.append("         COMPLAINT.CP_IS_ONCE_SF CPISONCESF, /*需转换*/\n"); 
		sql.append("         DECODE(COMPLAINT.Cp_Close_Date, NULL, '',\n"); 
		sql.append("           CASE WHEN COMPLAINT.CP_SF="+Constant.PLEASED+" THEN '正常关闭'\n"); 
		sql.append("                WHEN COMPLAINT.CP_SF="+Constant.YAWP+" THEN '不正常关闭'\n"); 
		sql.append("                ELSE '' END) ISNORMALCLOSE, /*是否正常关闭*/\n"); 
		sql.append("         X.BACK_DATE LASTDEALDATE, /*最终反馈时间*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS CPSTATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DUSER /*当前处理人*/\n"); 
		sql.append("    FROM TT_CRM_COMPLAINT COMPLAINT\n"); 
		sql.append("    LEFT JOIN TM_REGION TRP ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID AND TRP.REGION_TYPE = "+Constant.REGION_TYPE_02+"\n"); 
		sql.append("    LEFT JOIN TM_REGION TRC ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID AND TRC.REGION_TYPE = "+Constant.REGION_TYPE_03+"\n"); 
		sql.append("    LEFT JOIN tm_org_custom ORGOBJ ON ORGOBJ.ORG_ID = COMPLAINT.CP_OBJECT\n"); 
		sql.append("    LEFT JOIN TM_DEALER REOBJ ON REOBJ.DEALER_ID = COMPLAINT.CP_OBJECT \n"); 
		sql.append("    LEFT JOIN TC_USER ACUSER ON ACUSER.USER_ID = COMPLAINT.CP_ACC_USER\n"); 
		sql.append("    LEFT JOIN TM_VHCL_MATERIAL_GROUP SGROUP ON SGROUP.GROUP_ID = COMPLAINT.CP_SERIES_ID AND SGROUP.GROUP_LEVEL = 2\n"); 
		sql.append("    LEFT JOIN TM_ORG ORGPRO ON COMPLAINT.CP_OBJECT_ORG = ORGPRO.ORG_ID AND ORGPRO.DUTY_TYPE = "+Constant.DUTY_TYPE_LARGEREGION+"\n"); 
		sql.append("    LEFT JOIN (SELECT TO_CHAR(MAX(DR.CD_DATE), 'yyyy-MM-dd hh24:mi:ss') LASTDEALDATE, DR.CP_ID, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CD_DATE,NULL)) BACK_DATE\n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DR GROUP BY DR.CP_ID) X ON X.CP_ID = COMPLAINT.CP_ID");

		sql.append("    LEFT JOIN (SELECT MAX(DRD.CREATE_DATE) LASTORGDEALDATE, DRD.CP_ID \n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DRD where DRD.cp_status = "+Constant.COMPLAINT_STATUS_DOING_ALREADY+"  GROUP BY DRD.CP_ID) Y ON Y.CP_ID = COMPLAINT.CP_ID");
		
		sql.append(" where complaint.cp_biz_type = "+Constant.TYPE_COMPLAIN);
		sql.append(" and complaint.CP_DEAL_DEALER in ('"+userids+"') and complaint.CP_STATUS in('"+state+"') ");
		if(StringUtil.notNull(vin)){
			sql.append(" and complaint.cp_vin like '%"+vin+"%' ");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and complaint.cp_name like '%"+name+"%' ");
		}
		if(StringUtil.notNull(tele)){
			sql.append(" and complaint.cp_phone like '%"+tele+"%' ");
		}
		if(StringUtil.notNull(level)){
			sql.append(" and complaint.cp_level = '"+level+"' ");
		}
		if(StringUtil.notNull(dealUser)){
			sql.append(" and COMPLAINT.CP_DEAL_USER_NAME like '%"+dealUser+"%' ");
		}
		if(StringUtil.notNull(accUser)){
			sql.append(" and acuser.name like '%"+accUser+"%' ");
		}
		if(StringUtil.notNull(biztype)){
			sql.append(" and complaint.CP_BIZ_CONTENT = '"+biztype+"' ");
		}
		if(StringUtil.notNull(status) && status.equals("unClose")){
			sql.append(" and complaint.cp_status not in ('"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+"','"+Constant.COMPLAINT_STATUS_CLOSE+"') ");
		}else if(StringUtil.notNull(status) && !status.equals("unClose")){
			sql.append(" and complaint.cp_status = '"+status+"' ");
		}
		if(StringUtil.notNull(dateStart)){
			sql.append(" and complaint.cp_acc_date >=to_date('"+dateStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dateEnd)){
			sql.append(" and complaint.cp_acc_date<=to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(dealStart)){
			sql.append(" and complaint.CP_TURN_DATE >=to_date('"+dealStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dealEnd)){
			sql.append(" and complaint.CP_TURN_DATE<=to_date('"+dealEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(region)){
			sql.append(" and complaint.CP_DEAL_ORG = "+ region );
		}
		if(StringUtil.notNull(pro)){
			sql.append(" and complaint.CP_PROVINCE_ID = "+pro);
		}
		sql.append(" order by complaint.CP_ACC_DATE desc ");
		
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	
	public PageResult<Map<String, Object>> queryComplaintInfo01(String vin,
			String name, String tele, String level,String dealUser, String accUser, String biztype,
			String status, String dealStart, String dealEnd, String dateStart,
			String dateEnd, String region, String pro, String checkStart,
			String checklEnd,String userid,String stauts, Integer pageSize, Integer curPage) {
		StringBuffer sql = new StringBuffer();
	
		sql.append(" SELECT COMPLAINT.CP_NO        CPNO, /*单号*/\n");
		sql.append("         COMPLAINT.CP_ID        CPID, /*ID*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS    STATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_CL_STATUS CLSTATUS, /*延期状态*/\n"); 
		sql.append("         COMPLAINT.CP_LEVEL     CPLEVEL, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n"); 
		sql.append("         TRP.REGION_NAME      PRO, /*省份*/\n"); 
		sql.append("         TRC.REGION_NAME      CITY, /*城市*/\n"); 
		sql.append("         COMPLAINT.CP_NAME    CTMNAME, /*客户名称*/\n"); 
		sql.append("         COMPLAINT.CP_CUS_ID  CTMID, /*客户ID*/\n"); 
		sql.append("         COMPLAINT.CP_PHONE   PHONE,  /*电话*/\n"); 
		sql.append("         COMPLAINT.CP_CONTENT CPCONT,  /*内容类型*/\n"); 
		sql.append("         NVL(ORGOBJ.ORG_NAME, REOBJ.DEALER_NAME) CPOBJECT,/*抱怨对象*/\n"); 
		sql.append("         COMPLAINT.CREATE_DATE CREATEDATE,/*创建日期*/\n"); 
		sql.append("         ACUSER.NAME ACUSER,/*受理人*/\n"); 
		sql.append("         SGROUP.GROUP_NAME SNAME,/*车种*/\n"); 
		sql.append("         COMPLAINT.CP_VIN VIN,\n"); 
		sql.append("         TO_CHAR(COMPLAINT.CP_BUY_DATE, 'yyyy-MM-dd hh24:mi:ss') BDATE,/*购买日期*/\n"); 
		sql.append("         COMPLAINT.CP_MILEAGE CPMILEAGE, /*行驶里程*/\n"); 
		sql.append("         /*COMPLAINT.CP_MILEAGE_RANGE, /*需转换*/\n"); 
		sql.append("         ORGPRO.ORG_NAME ORGNAME,/*大区*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DEALUSER,/*处理人*/\n"); 
		sql.append("         COMPLAINT.CP_ACC_DATE  CPACCDATE,/*受理日期*/\n"); 
		sql.append("         COMPLAINT.CP_TURN_DATE TURNDATE,/*转出日期*/\n"); 
		sql.append("         decode(COMPLAINT.CP_STATUS,"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.PLEASED+","+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.YAWP+",null) CPSF, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_CLOSE_DATE CPCDATE, /*关闭日期*/\n"); 
		sql.append("         TO_CHAR(CEIL((Y.LASTORGDEALDATE - COMPLAINT.CP_TURN_DATE) * 24)) DEALTIME,");
		sql.append("         COMPLAINT.CP_LIMIT CPLIMIT, /*规定时限*/\n"); 

		sql.append("ceil(\r\n" );
		sql.append(" case when COMPLAINT.Cp_Cl_Date is null then 0\r\n" );
		sql.append("      else case when COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) >= COMPLAINT.Cp_Cl_Once_Date\r\n" );
		sql.append("                then case when COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) >0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) else 0 end\r\n" );
		sql.append("                else case when COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date>0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date else 0 end  end\r\n" );
		sql.append("      end\r\n" );
		sql.append(")DELAYDATE,/*延期天数(天) 取整*/");

		sql.append("         /*COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT SHOULDCLOSETIME, */\n"); 
		sql.append("         /*CEIL((COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT - SYSDATE)*24) OWNTIME, */\n"); 

		sql.append("case when COMPLAINT.Cp_Status in("+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+") then\r\n" );
		sql.append("  case when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_CLOSE+" then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then '未及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE <= nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE > nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '未及时关闭'\r\n" );
		sql.append("          end\r\n" );
		sql.append("end  ISTIMELYCLOSE, /*是否及时关闭*/");			

		sql.append("         COMPLAINT.CP_IS_ONCE_SF CPISONCESF, /*需转换*/\n"); 
		sql.append("         DECODE(COMPLAINT.Cp_Close_Date, NULL, '',\n"); 
		sql.append("           CASE WHEN COMPLAINT.CP_SF="+Constant.PLEASED+" THEN '正常关闭'\n"); 
		sql.append("                WHEN COMPLAINT.CP_SF="+Constant.YAWP+" THEN '不正常关闭'\n"); 
		sql.append("                ELSE '' END) ISNORMALCLOSE, /*是否正常关闭*/\n"); 
		sql.append("         X.BACK_DATE LASTDEALDATE, /*最终反馈时间*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS CPSTATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DUSER /*当前处理人*/\n"); 
		sql.append("    FROM TT_CRM_COMPLAINT COMPLAINT\n"); 
		sql.append("    LEFT JOIN TM_REGION TRP ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID AND TRP.REGION_TYPE = "+Constant.REGION_TYPE_02+"\n"); 
		sql.append("    LEFT JOIN TM_REGION TRC ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID AND TRC.REGION_TYPE = "+Constant.REGION_TYPE_03+"\n"); 
		sql.append("    LEFT JOIN tm_org_custom ORGOBJ ON ORGOBJ.ORG_ID = COMPLAINT.CP_OBJECT\n"); 
		sql.append("    LEFT JOIN TM_DEALER REOBJ ON REOBJ.DEALER_ID = COMPLAINT.CP_OBJECT \n"); 
		sql.append("    LEFT JOIN TC_USER ACUSER ON ACUSER.USER_ID = COMPLAINT.CP_ACC_USER\n"); 
		sql.append("    LEFT JOIN TM_VHCL_MATERIAL_GROUP SGROUP ON SGROUP.GROUP_ID = COMPLAINT.CP_SERIES_ID AND SGROUP.GROUP_LEVEL = 2\n"); 
		sql.append("    LEFT JOIN TM_ORG ORGPRO ON COMPLAINT.CP_OBJECT_ORG = ORGPRO.ORG_ID AND ORGPRO.DUTY_TYPE = "+Constant.DUTY_TYPE_LARGEREGION+"\n"); 
		sql.append("    LEFT JOIN (SELECT TO_CHAR(MAX(DR.CD_DATE), 'yyyy-MM-dd hh24:mi:ss') LASTDEALDATE, DR.CP_ID, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CD_DATE,NULL)) BACK_DATE\n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DR GROUP BY DR.CP_ID) X ON X.CP_ID = COMPLAINT.CP_ID\n");
		sql.append("    JOIN (SELECT A.ORG_ID ORGID,\n");
		sql.append("          CASE WHEN B.OCULR_LEVEL = "+Constant.Level_Manager_01+" THEN "+Constant.ALREADY_APPLY+"\n"); 
		sql.append("               WHEN B.OCULR_LEVEL = "+Constant.Level_Manager_02+" THEN "+Constant.PASS_Manager_01+"\n"); 
		sql.append("               WHEN B.OCULR_LEVEL = "+Constant.Level_Manager_03+" THEN "+Constant.PASS_Manager_02+"\n"); 
		sql.append("               ELSE NULL END CLSTAUTS\n"); 
		sql.append("     FROM TM_ORG_CUSTOM A, TM_ORG_CUS_USER_LEVEL_RELATION B\n"); 
		sql.append("    WHERE B.ORG_ID = A.ORG_ID\n"); 
		sql.append("      AND B.USER_ID = "+userid+" ) ORGLEVEL\n"); 
		sql.append("     ON (COMPLAINT.CP_CL_TURN_ORG = ORGLEVEL.ORGID OR ORGLEVEL.ORGID = 2010010100070674) /*副总审核时，不分部门*/ \n");
		sql.append("    AND COMPLAINT.CP_CL_STATUS = ORGLEVEL.CLSTAUTS");

		sql.append("    LEFT JOIN (SELECT MAX(DRD.CREATE_DATE) LASTORGDEALDATE, DRD.CP_ID \n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DRD where DRD.cp_status = "+Constant.COMPLAINT_STATUS_DOING_ALREADY+"  GROUP BY DRD.CP_ID) Y ON Y.CP_ID = COMPLAINT.CP_ID");

	    sql.append(" where 1=1 \r\n");
	    
		//sql.append("  and complaint.CP_STATUS in('"+stauts+"') ");
		if(StringUtil.notNull(vin)){
			sql.append(" and complaint.cp_vin like '%"+vin+"%' ");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and complaint.cp_name like '%"+name+"%' ");
		}
		if(StringUtil.notNull(tele)){
			sql.append(" and complaint.cp_phone like '%"+tele+"%' ");
		}
		if(StringUtil.notNull(level)){
			sql.append(" and complaint.cp_level = '"+level+"' ");
		}
		if(StringUtil.notNull(dealUser)){
			sql.append(" and COMPLAINT.CP_DEAL_USER_NAME like '%"+dealUser+"%' ");
		}
		if(StringUtil.notNull(accUser)){
			sql.append(" and acuser.name like '%"+accUser+"%' ");
		}
		if(StringUtil.notNull(biztype)){
			sql.append(" and complaint.CP_BIZ_CONTENT = '"+biztype+"' ");
		}
		if(StringUtil.notNull(status)){
			sql.append(" and complaint.cp_status = '"+status+"' ");
		}
		if(StringUtil.notNull(dateStart)){
			sql.append(" and complaint.cp_acc_date >=to_date('"+dateStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dateEnd)){
			sql.append(" and complaint.cp_acc_date<=to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(dealStart)){
			sql.append(" and complaint.CP_TURN_DATE >=to_date('"+dealStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dealEnd)){
			sql.append(" and complaint.CP_TURN_DATE<=to_date('"+dealEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(region)){
			sql.append(" and complaint.CP_DEAL_ORG = "+ region );
		}
		if(StringUtil.notNull(pro)){
			sql.append(" and complaint.CP_PROVINCE_ID = "+pro);
		}
		sql.append(" order by complaint.CP_ACC_DATE desc ");
		
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	public List<Map<String, Object>> queryComplaintInfo01List(String vin,
			String name, String tele, String level,String dealUser, String accUser, String biztype,
			String status, String dealStart, String dealEnd, String dateStart,
			String dateEnd, String region, String pro, String checkStart,
			String checklEnd,String userid,String stauts) {
		StringBuffer sql = new StringBuffer();
		// 艾春 9.23 修改 投诉查询语句
		sql.append(" SELECT COMPLAINT.CP_NO        CPNO, /*单号*/\n");
		sql.append("         COMPLAINT.CP_ID        CPID, /*ID*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS    STATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_CL_STATUS CLSTATUS, /*延期状态*/\n"); 
		sql.append("         COMPLAINT.CP_LEVEL     CPLEVEL, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n"); 
		sql.append("         TRP.REGION_NAME      PRO, /*省份*/\n"); 
		sql.append("         TRC.REGION_NAME      CITY, /*城市*/\n"); 
		sql.append("         COMPLAINT.CP_NAME    CTMNAME, /*客户名称*/\n"); 
		sql.append("         COMPLAINT.CP_CUS_ID  CTMID, /*客户ID*/\n"); 
		sql.append("         COMPLAINT.CP_PHONE   PHONE,  /*电话*/\n"); 
		sql.append("         COMPLAINT.CP_CONTENT CPCONT,  /*内容类型*/\n"); 
		sql.append("         NVL(ORGOBJ.ORG_NAME, REOBJ.DEALER_NAME) CPOBJECT,/*抱怨对象*/\n"); 
		sql.append("         COMPLAINT.CREATE_DATE CREATEDATE,/*创建日期*/\n"); 
		sql.append("         ACUSER.NAME ACUSER,/*受理人*/\n"); 
		sql.append("         SGROUP.GROUP_NAME SNAME,/*车种*/\n"); 
		sql.append("         COMPLAINT.CP_VIN VIN,\n"); 
		sql.append("         TO_CHAR(COMPLAINT.CP_BUY_DATE, 'yyyy-MM-dd hh24:mi:ss') BDATE,/*购买日期*/\n"); 
		sql.append("         COMPLAINT.CP_MILEAGE CPMILEAGE, /*行驶里程*/\n"); 
		sql.append("         /*COMPLAINT.CP_MILEAGE_RANGE, /*需转换*/\n"); 
		sql.append("         ORGPRO.ORG_NAME ORGNAME,/*大区*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DEALUSER,/*处理人*/\n"); 
		sql.append("         COMPLAINT.CP_ACC_DATE  CPACCDATE,/*受理日期*/\n"); 
		sql.append("         COMPLAINT.CP_TURN_DATE TURNDATE,/*转出日期*/\n"); 
		sql.append("         decode(COMPLAINT.CP_STATUS,"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.PLEASED+","+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.YAWP+",null) CPSF, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_CLOSE_DATE CPCDATE, /*关闭日期*/\n"); 
		sql.append("         TO_CHAR(CEIL((Y.LASTORGDEALDATE - COMPLAINT.CP_TURN_DATE) * 24)) DEALTIME,");
		sql.append("         COMPLAINT.CP_LIMIT CPLIMIT, /*规定时限*/\n"); 

		sql.append("ceil(\r\n" );
		sql.append(" case when COMPLAINT.Cp_Cl_Date is null then 0\r\n" );
		sql.append("      else case when COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) >= COMPLAINT.Cp_Cl_Once_Date\r\n" );
		sql.append("                then case when COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) >0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) else 0 end\r\n" );
		sql.append("                else case when COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date>0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date else 0 end  end\r\n" );
		sql.append("      end\r\n" );
		sql.append(")DELAYDATE,/*延期天数(天) 取整*/");

		sql.append("         /*COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT SHOULDCLOSETIME, */\n"); 
		sql.append("         /*CEIL((COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT - SYSDATE)*24) OWNTIME, */\n"); 

		sql.append("case when COMPLAINT.Cp_Status in("+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+") then\r\n" );
		sql.append("  case when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_CLOSE+" then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then '未及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE <= nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE > nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '未及时关闭'\r\n" );
		sql.append("          end\r\n" );
		sql.append("end  ISTIMELYCLOSE, /*是否及时关闭*/");			

		sql.append("         COMPLAINT.CP_IS_ONCE_SF CPISONCESF, /*需转换*/\n"); 
		sql.append("         DECODE(COMPLAINT.Cp_Close_Date, NULL, '',\n"); 
		sql.append("           CASE WHEN COMPLAINT.CP_SF="+Constant.PLEASED+" THEN '正常关闭'\n"); 
		sql.append("                WHEN COMPLAINT.CP_SF="+Constant.YAWP+" THEN '不正常关闭'\n"); 
		sql.append("                ELSE '' END) ISNORMALCLOSE, /*是否正常关闭*/\n");  
		sql.append("         X.BACK_DATE LASTDEALDATE, /*最终反馈时间*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS CPSTATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DUSER /*当前处理人*/\n"); 
		sql.append("    FROM TT_CRM_COMPLAINT COMPLAINT\n"); 
		sql.append("    LEFT JOIN TM_REGION TRP ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID AND TRP.REGION_TYPE = "+Constant.REGION_TYPE_02+"\n"); 
		sql.append("    LEFT JOIN TM_REGION TRC ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID AND TRC.REGION_TYPE = "+Constant.REGION_TYPE_03+"\n"); 
		sql.append("    LEFT JOIN tm_org_custom ORGOBJ ON ORGOBJ.ORG_ID = COMPLAINT.CP_OBJECT\n"); 
		sql.append("    LEFT JOIN TM_DEALER REOBJ ON REOBJ.DEALER_ID = COMPLAINT.CP_OBJECT \n"); 
		sql.append("    LEFT JOIN TC_USER ACUSER ON ACUSER.USER_ID = COMPLAINT.CP_ACC_USER\n"); 
		sql.append("    LEFT JOIN TM_VHCL_MATERIAL_GROUP SGROUP ON SGROUP.GROUP_ID = COMPLAINT.CP_SERIES_ID AND SGROUP.GROUP_LEVEL = 2\n"); 
		sql.append("    LEFT JOIN TM_ORG ORGPRO ON COMPLAINT.CP_OBJECT_ORG = ORGPRO.ORG_ID AND ORGPRO.DUTY_TYPE = "+Constant.DUTY_TYPE_LARGEREGION+"\n"); 
		sql.append("    LEFT JOIN (SELECT TO_CHAR(MAX(DR.CD_DATE), 'yyyy-MM-dd hh24:mi:ss') LASTDEALDATE, DR.CP_ID, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CD_DATE,NULL)) BACK_DATE\n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DR GROUP BY DR.CP_ID) X ON X.CP_ID = COMPLAINT.CP_ID");
		
		sql.append("    JOIN (SELECT A.ORG_ID ORGID,\n");
		sql.append("          CASE WHEN B.OCULR_LEVEL = "+Constant.Level_Manager_01+" THEN "+Constant.ALREADY_APPLY+"\n"); 
		sql.append("               WHEN B.OCULR_LEVEL = "+Constant.Level_Manager_02+" THEN "+Constant.PASS_Manager_01+"\n"); 
		sql.append("               WHEN B.OCULR_LEVEL = "+Constant.Level_Manager_03+" THEN "+Constant.PASS_Manager_02+"\n"); 
		sql.append("               ELSE NULL END CLSTAUTS\n"); 
		sql.append("     FROM TM_ORG_CUSTOM A, TM_ORG_CUS_USER_LEVEL_RELATION B\n"); 
		sql.append("    WHERE B.ORG_ID = A.ORG_ID\n"); 
		sql.append("      AND B.USER_ID = "+userid+" ) ORGLEVEL\n"); 
		sql.append("     ON (COMPLAINT.CP_CL_TURN_ORG = ORGLEVEL.ORGID OR ORGLEVEL.ORGID = 2010010100070674) /*副总审核时，不分部门*/ \n");
		sql.append("    AND COMPLAINT.CP_CL_STATUS = ORGLEVEL.CLSTAUTS");

		sql.append("    LEFT JOIN (SELECT MAX(DRD.CREATE_DATE) LASTORGDEALDATE, DRD.CP_ID \n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DRD where DRD.cp_status = "+Constant.COMPLAINT_STATUS_DOING_ALREADY+"  GROUP BY DRD.CP_ID) Y ON Y.CP_ID = COMPLAINT.CP_ID");
		
		sql.append(" where complaint.cp_biz_type = "+Constant.TYPE_COMPLAIN);
		if(StringUtil.notNull(vin)){
			sql.append(" and complaint.cp_vin like '%"+vin+"%' ");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and complaint.cp_name like '%"+name+"%' ");
		}
		if(StringUtil.notNull(tele)){
			sql.append(" and complaint.cp_phone like '%"+tele+"%' ");
		}
		if(StringUtil.notNull(level)){
			sql.append(" and complaint.cp_level = '"+level+"' ");
		}
		if(StringUtil.notNull(dealUser)){
			sql.append(" and COMPLAINT.CP_DEAL_USER_NAME like '%"+dealUser+"%' ");
		}
		if(StringUtil.notNull(accUser)){
			sql.append(" and acuser.name like '%"+accUser+"%' ");
		}
		if(StringUtil.notNull(biztype)){
			sql.append(" and complaint.CP_BIZ_CONTENT = '"+biztype+"' ");
		}
		if(StringUtil.notNull(status)){
			sql.append(" and complaint.cp_status = '"+status+"' ");
		}
		if(StringUtil.notNull(dateStart)){
			sql.append(" and complaint.cp_acc_date >=to_date('"+dateStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dateEnd)){
			sql.append(" and complaint.cp_acc_date<=to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(dealStart)){
			sql.append(" and complaint.CP_TURN_DATE >=to_date('"+dealStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dealEnd)){
			sql.append(" and complaint.CP_TURN_DATE<=to_date('"+dealEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(region)){
			sql.append(" and complaint.CP_DEAL_ORG = "+ region );
		}
		if(StringUtil.notNull(pro)){
			sql.append(" and complaint.CP_PROVINCE_ID = "+pro);
		}
		sql.append(" order by complaint.CP_ACC_DATE desc ");
		
		return (List<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName());
	}
	
	
	//经销商延期大区审核
	public PageResult<Map<String, Object>> queryDealerComplaintInfo(String vin,
			String name, String tele, String level,String dealUser, String accUser, String biztype,
			String status, String dealStart, String dealEnd, String dateStart,
			String dateEnd, String region, String pro, String checkStart,
			String checklEnd,String orgid,String stauts, Integer pageSize, Integer curPage) {
		StringBuffer sql = new StringBuffer();
		// 艾春 9.23 修改 投诉查询语句
		sql.append(" SELECT COMPLAINT.CP_NO        CPNO, /*单号*/\n");
		sql.append("         COMPLAINT.CP_ID        CPID, /*ID*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS    STATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_CL_STATUS CLSTATUS, /*延期状态*/\n"); 
		sql.append("         COMPLAINT.CP_LEVEL     CPLEVEL, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n"); 
		sql.append("         TRP.REGION_NAME      PRO, /*省份*/\n"); 
		sql.append("         TRC.REGION_NAME      CITY, /*城市*/\n"); 
		sql.append("         COMPLAINT.CP_NAME    CTMNAME, /*客户名称*/\n"); 
		sql.append("         COMPLAINT.CP_CUS_ID  CTMID, /*客户ID*/\n"); 
		sql.append("         COMPLAINT.CP_PHONE   PHONE,  /*电话*/\n"); 
		sql.append("         COMPLAINT.CP_CONTENT CPCONT,  /*内容类型*/\n"); 
		sql.append("         NVL(ORGOBJ.ORG_NAME, REOBJ.DEALER_NAME) CPOBJECT,/*抱怨对象*/\n"); 
		sql.append("         COMPLAINT.CREATE_DATE CREATEDATE,/*创建日期*/\n"); 
		sql.append("         ACUSER.NAME ACUSER,/*受理人*/\n"); 
		sql.append("         SGROUP.GROUP_NAME SNAME,/*车种*/\n"); 
		sql.append("         COMPLAINT.CP_VIN VIN,\n"); 
		sql.append("         TO_CHAR(COMPLAINT.CP_BUY_DATE, 'yyyy-MM-dd hh24:mi:ss') BDATE,/*购买日期*/\n"); 
		sql.append("         COMPLAINT.CP_MILEAGE CPMILEAGE, /*行驶里程*/\n"); 
		sql.append("         /*COMPLAINT.CP_MILEAGE_RANGE, /*需转换*/\n"); 
		sql.append("         ORGPRO.ORG_NAME ORGNAME,/*大区*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DEALUSER,/*处理人*/\n"); 
		sql.append("         COMPLAINT.CP_ACC_DATE  CPACCDATE,/*受理日期*/\n"); 
		sql.append("         COMPLAINT.CP_TURN_DATE TURNDATE,/*转出日期*/\n"); 
		sql.append("         decode(COMPLAINT.CP_STATUS,"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.PLEASED+","+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.YAWP+",null) CPSF, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_CLOSE_DATE CPCDATE, /*关闭日期*/\n"); 
		sql.append("         TO_CHAR(CEIL((Y.LASTORGDEALDATE - COMPLAINT.CP_TURN_DATE) * 24)) DEALTIME,");
		sql.append("         COMPLAINT.CP_LIMIT CPLIMIT, /*规定时限*/\n"); 

		sql.append("ceil(\r\n" );
		sql.append(" case when COMPLAINT.Cp_Cl_Date is null then 0\r\n" );
		sql.append("      else case when COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) >= COMPLAINT.Cp_Cl_Once_Date\r\n" );
		sql.append("                then case when COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) >0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) else 0 end\r\n" );
		sql.append("                else case when COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date>0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date else 0 end  end\r\n" );
		sql.append("      end\r\n" );
		sql.append(")DELAYDATE,/*延期天数(天) 取整*/");

		sql.append("         /*COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT SHOULDCLOSETIME, */\n"); 
		sql.append("         /*CEIL((COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT - SYSDATE)*24) OWNTIME, */\n"); 

		sql.append("case when COMPLAINT.Cp_Status in("+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+") then\r\n" );
		sql.append("  case when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_CLOSE+" then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then '未及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE <= nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE > nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '未及时关闭'\r\n" );
		sql.append("          end\r\n" );
		sql.append("end  ISTIMELYCLOSE, /*是否及时关闭*/");			

		sql.append("         COMPLAINT.CP_IS_ONCE_SF CPISONCESF, /*需转换*/\n"); 
		sql.append("         DECODE(COMPLAINT.Cp_Close_Date, NULL, '',\n"); 
		sql.append("           CASE WHEN COMPLAINT.CP_SF="+Constant.PLEASED+" THEN '正常关闭'\n"); 
		sql.append("                WHEN COMPLAINT.CP_SF="+Constant.YAWP+" THEN '不正常关闭'\n"); 
		sql.append("                ELSE '' END) ISNORMALCLOSE, /*是否正常关闭*/\n"); 
		sql.append("         X.BACK_DATE LASTDEALDATE, /*最终反馈时间*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS CPSTATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DUSER /*当前处理人*/\n"); 
		sql.append("    FROM TT_CRM_COMPLAINT COMPLAINT\n"); 
		sql.append("    LEFT JOIN TM_REGION TRP ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID AND TRP.REGION_TYPE = "+Constant.REGION_TYPE_02+"\n"); 
		sql.append("    LEFT JOIN TM_REGION TRC ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID AND TRC.REGION_TYPE = "+Constant.REGION_TYPE_03+"\n"); 
		sql.append("    LEFT JOIN tm_org_custom ORGOBJ ON ORGOBJ.ORG_ID = COMPLAINT.CP_OBJECT\n"); 
		sql.append("    LEFT JOIN TM_DEALER REOBJ ON REOBJ.DEALER_ID = COMPLAINT.CP_OBJECT \n"); 
		sql.append("    LEFT JOIN TC_USER ACUSER ON ACUSER.USER_ID = COMPLAINT.CP_ACC_USER\n"); 
		sql.append("    LEFT JOIN TM_VHCL_MATERIAL_GROUP SGROUP ON SGROUP.GROUP_ID = COMPLAINT.CP_SERIES_ID AND SGROUP.GROUP_LEVEL = 2\n"); 
		sql.append("    LEFT JOIN TM_ORG ORGPRO ON COMPLAINT.CP_OBJECT_ORG = ORGPRO.ORG_ID AND ORGPRO.DUTY_TYPE = "+Constant.DUTY_TYPE_LARGEREGION+"\n"); 
		sql.append("    LEFT JOIN (SELECT TO_CHAR(MAX(DR.CD_DATE), 'yyyy-MM-dd hh24:mi:ss') LASTDEALDATE, DR.CP_ID, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CD_DATE,NULL)) BACK_DATE\n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DR GROUP BY DR.CP_ID) X ON X.CP_ID = COMPLAINT.CP_ID");

		sql.append("    LEFT JOIN (SELECT MAX(DRD.CREATE_DATE) LASTORGDEALDATE, DRD.CP_ID \n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DRD where DRD.cp_status = "+Constant.COMPLAINT_STATUS_DOING_ALREADY+"  GROUP BY DRD.CP_ID) Y ON Y.CP_ID = COMPLAINT.CP_ID");

		sql.append(" where complaint.cp_biz_type = "+Constant.TYPE_COMPLAIN+"\n");
		sql.append(" and complaint.CP_DEAL_DEALER is not null \n");
		sql.append(" and (complaint.CP_CL_TURN_ORG is null or complaint.CP_CL_TURN_ORG = -1 ) /*投诉没有转到相应部门*/");
		sql.append(" and complaint.cp_cl_status is not null /*经销商已做延期申请*/ ");
		sql.append(" and complaint.CP_DEAL_ORG in ('"+orgid+"')");
		if(StringUtil.notNull(vin)){
			sql.append(" and complaint.cp_vin like '%"+vin+"%' ");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and complaint.cp_name like '%"+name+"%' ");
		}
		if(StringUtil.notNull(tele)){
			sql.append(" and complaint.cp_phone like '%"+tele+"%' ");
		}
		if(StringUtil.notNull(level)){
			sql.append(" and complaint.cp_level = '"+level+"' ");
		}
		if(StringUtil.notNull(dealUser)){
			sql.append(" and COMPLAINT.CP_DEAL_USER_NAME like '%"+dealUser+"%' ");
		}
		if(StringUtil.notNull(accUser)){
			sql.append(" and acuser.name like '%"+accUser+"%' ");
		}
		if(StringUtil.notNull(biztype)){
			sql.append(" and complaint.CP_BIZ_CONTENT = '"+biztype+"' ");
		}
		if(StringUtil.notNull(status)){
			sql.append(" and complaint.cp_status = '"+status+"' ");
		}
		if(StringUtil.notNull(dateStart)){
			sql.append(" and complaint.cp_acc_date >=to_date('"+dateStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dateEnd)){
			sql.append(" and complaint.cp_acc_date<=to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(dealStart)){
			sql.append(" and complaint.CP_TURN_DATE >=to_date('"+dealStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dealEnd)){
			sql.append(" and complaint.CP_TURN_DATE<=to_date('"+dealEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(region)){
			sql.append(" and complaint.CP_DEAL_ORG = "+ region );
		}
		if(StringUtil.notNull(pro)){
			sql.append(" and complaint.CP_PROVINCE_ID = "+pro);
		}
		sql.append(" order by complaint.CP_ACC_DATE desc ");

		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	public List<Map<String, Object>> queryDealerComplaintInfoList(String vin,
			String name, String tele, String level,String dealUser, String accUser, String biztype,
			String status, String dealStart, String dealEnd, String dateStart,
			String dateEnd, String region, String pro, String checkStart,
			String checklEnd,String orgid,String stauts) {
		StringBuffer sql = new StringBuffer();
		// 艾春 9.23 修改 投诉查询语句
		sql.append(" SELECT COMPLAINT.CP_NO        CPNO, /*单号*/\n");
		sql.append("         COMPLAINT.CP_ID        CPID, /*ID*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS    STATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_CL_STATUS CLSTATUS, /*延期状态*/\n"); 
		sql.append("         COMPLAINT.CP_LEVEL     CPLEVEL, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n"); 
		sql.append("         TRP.REGION_NAME      PRO, /*省份*/\n"); 
		sql.append("         TRC.REGION_NAME      CITY, /*城市*/\n"); 
		sql.append("         COMPLAINT.CP_NAME    CTMNAME, /*客户名称*/\n"); 
		sql.append("         COMPLAINT.CP_CUS_ID  CTMID, /*客户ID*/\n"); 
		sql.append("         COMPLAINT.CP_PHONE   PHONE,  /*电话*/\n"); 
		sql.append("         COMPLAINT.CP_CONTENT CPCONT,  /*内容类型*/\n"); 
		sql.append("         NVL(ORGOBJ.ORG_NAME, REOBJ.DEALER_NAME) CPOBJECT,/*抱怨对象*/\n"); 
		sql.append("         COMPLAINT.CREATE_DATE CREATEDATE,/*创建日期*/\n"); 
		sql.append("         ACUSER.NAME ACUSER,/*受理人*/\n"); 
		sql.append("         SGROUP.GROUP_NAME SNAME,/*车种*/\n"); 
		sql.append("         COMPLAINT.CP_VIN VIN,\n"); 
		sql.append("         TO_CHAR(COMPLAINT.CP_BUY_DATE, 'yyyy-MM-dd hh24:mi:ss') BDATE,/*购买日期*/\n"); 
		sql.append("         COMPLAINT.CP_MILEAGE CPMILEAGE, /*行驶里程*/\n"); 
		sql.append("         /*COMPLAINT.CP_MILEAGE_RANGE, /*需转换*/\n"); 
		sql.append("         ORGPRO.ORG_NAME ORGNAME,/*大区*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DEALUSER,/*处理人*/\n"); 
		sql.append("         COMPLAINT.CP_ACC_DATE  CPACCDATE,/*受理日期*/\n"); 
		sql.append("         COMPLAINT.CP_TURN_DATE TURNDATE,/*转出日期*/\n"); 
		sql.append("         decode(COMPLAINT.CP_STATUS,"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.PLEASED+","+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.YAWP+",null) CPSF, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_CLOSE_DATE CPCDATE, /*关闭日期*/\n"); 
		sql.append("         TO_CHAR(CEIL((Y.LASTORGDEALDATE - COMPLAINT.CP_TURN_DATE) * 24)) DEALTIME,");
		sql.append("         COMPLAINT.CP_LIMIT CPLIMIT, /*规定时限*/\n"); 

		sql.append("ceil(\r\n" );
		sql.append(" case when COMPLAINT.Cp_Cl_Date is null then 0\r\n" );
		sql.append("      else case when COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) >= COMPLAINT.Cp_Cl_Once_Date\r\n" );
		sql.append("                then case when COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) >0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) else 0 end\r\n" );
		sql.append("                else case when COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date>0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date else 0 end  end\r\n" );
		sql.append("      end\r\n" );
		sql.append(")DELAYDATE,/*延期天数(天) 取整*/");

		sql.append("         /*COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT SHOULDCLOSETIME, */\n"); 
		sql.append("         /*CEIL((COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT - SYSDATE)*24) OWNTIME, */\n"); 

		sql.append("case when COMPLAINT.Cp_Status in("+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+") then\r\n" );
		sql.append("  case when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_CLOSE+" then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then '未及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE <= nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE > nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '未及时关闭'\r\n" );
		sql.append("          end\r\n" );
		sql.append("end  ISTIMELYCLOSE, /*是否及时关闭*/");			

		sql.append("         COMPLAINT.CP_IS_ONCE_SF CPISONCESF, /*需转换*/\n"); 
		sql.append("         DECODE(COMPLAINT.Cp_Close_Date, NULL, '',\n"); 
		sql.append("           CASE WHEN COMPLAINT.CP_SF="+Constant.PLEASED+" THEN '正常关闭'\n"); 
		sql.append("                WHEN COMPLAINT.CP_SF="+Constant.YAWP+" THEN '不正常关闭'\n"); 
		sql.append("                ELSE '' END) ISNORMALCLOSE, /*是否正常关闭*/\n"); 
		sql.append("         X.BACK_DATE LASTDEALDATE, /*最终反馈时间*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS CPSTATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DUSER /*当前处理人*/\n"); 
		sql.append("    FROM TT_CRM_COMPLAINT COMPLAINT\n"); 
		sql.append("    LEFT JOIN TM_REGION TRP ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID AND TRP.REGION_TYPE = "+Constant.REGION_TYPE_02+"\n"); 
		sql.append("    LEFT JOIN TM_REGION TRC ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID AND TRC.REGION_TYPE = "+Constant.REGION_TYPE_03+"\n"); 
		sql.append("    LEFT JOIN tm_org_custom ORGOBJ ON ORGOBJ.ORG_ID = COMPLAINT.CP_OBJECT\n"); 
		sql.append("    LEFT JOIN TM_DEALER REOBJ ON REOBJ.DEALER_ID = COMPLAINT.CP_OBJECT \n"); 
		sql.append("    LEFT JOIN TC_USER ACUSER ON ACUSER.USER_ID = COMPLAINT.CP_ACC_USER\n"); 
		sql.append("    LEFT JOIN TM_VHCL_MATERIAL_GROUP SGROUP ON SGROUP.GROUP_ID = COMPLAINT.CP_SERIES_ID AND SGROUP.GROUP_LEVEL = 2\n"); 
		sql.append("    LEFT JOIN TM_ORG ORGPRO ON COMPLAINT.CP_OBJECT_ORG = ORGPRO.ORG_ID AND ORGPRO.DUTY_TYPE = "+Constant.DUTY_TYPE_LARGEREGION+"\n"); 
		sql.append("    LEFT JOIN (SELECT TO_CHAR(MAX(DR.CD_DATE), 'yyyy-MM-dd hh24:mi:ss') LASTDEALDATE, DR.CP_ID, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CD_DATE,NULL)) BACK_DATE\n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DR GROUP BY DR.CP_ID) X ON X.CP_ID = COMPLAINT.CP_ID");

		sql.append("    LEFT JOIN (SELECT MAX(DRD.CREATE_DATE) LASTORGDEALDATE, DRD.CP_ID \n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DRD where DRD.cp_status = "+Constant.COMPLAINT_STATUS_DOING_ALREADY+"  GROUP BY DRD.CP_ID) Y ON Y.CP_ID = COMPLAINT.CP_ID");
		
		sql.append(" where complaint.cp_biz_type = "+Constant.TYPE_COMPLAIN+"\n");
		sql.append(" and complaint.CP_DEAL_DEALER is not null \n");
		sql.append(" and (complaint.CP_CL_TURN_ORG is null or complaint.CP_CL_TURN_ORG = -1 ) /*投诉没有转到相应部门*/");
		sql.append(" and complaint.cp_cl_status is not null /*经销商已做延期申请*/ ");
		sql.append(" and complaint.CP_DEAL_ORG in ('"+orgid+"')");
		
		if(StringUtil.notNull(vin)){
			sql.append(" and complaint.cp_vin = '"+vin+"' ");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and complaint.cp_name like '%"+name+"%' ");
		}
		if(StringUtil.notNull(tele)){
			sql.append(" and complaint.cp_phone = '"+tele+"' ");
		}
		if(StringUtil.notNull(level)){
			sql.append(" and complaint.cp_level = '"+level+"' ");
		}
		if(StringUtil.notNull(dealUser)){
			sql.append(" and COMPLAINT.CP_DEAL_USER_NAME like '%"+dealUser+"%' ");
		}
		if(StringUtil.notNull(accUser)){
			sql.append(" and acuser.name like '%"+accUser+"%' ");
		}
		if(StringUtil.notNull(biztype)){
			sql.append(" and complaint.cp_biz_type = '"+biztype+"' ");
		}
		if(StringUtil.notNull(status)){
			sql.append(" and complaint.cp_status = '"+status+"' ");
		}
		if(StringUtil.notNull(dateStart)){
			sql.append(" and complaint.cp_acc_date >=to_date('"+dateStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dateEnd)){
			sql.append(" and complaint.cp_acc_date<=to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(dealStart)){
			sql.append(" and complaint.CP_TURN_DATE >=to_date('"+dealStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dealEnd)){
			sql.append(" and complaint.CP_TURN_DATE<=to_date('"+dealEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(region)){
			sql.append(" and complaint.CP_DEAL_ORG = "+ region );
		}
		if(StringUtil.notNull(pro)){
			sql.append(" and complaint.CP_PROVINCE_ID = "+pro);
		}
		sql.append(" order by complaint.CP_ACC_DATE desc ");
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	
	
	//拼接投诉处理详细列表数据
	private StringBuffer appendQueryComplaintInfoSQL(StringBuffer sql){
		sql.append("select distinct complaint.cp_no CPNO,\r\n");
		sql.append("                complaint.cp_id CPID,\r\n");
		sql.append("                complaint.CP_STATUS STATUS,\r\n");
		sql.append("                complaint.CP_CL_STATUS CLSTATUS,\r\n");
		sql.append("                lc.code_desc CPLEVEL,\r\n");
		sql.append("                bizC.Code_Desc BIZCONT,\r\n");
		sql.append("                trP.Region_Name PRO,\r\n");
		sql.append("                trC.Region_Name CITY,\r\n");
		sql.append("                complaint.cp_name CTMNAME,\r\n");
		sql.append("                customer.ctm_id CTMID,\r\n");
		sql.append("                complaint.cp_phone PHONE,\r\n");
		sql.append("                complaint.CP_CONTENT CPCONT,\r\n");
		sql.append("                nvl(orgObj.ORG_NAME, reObj.dealer_name) CPOBJECT,\r\n");
		sql.append("                complaint.CREATE_DATE CREATEDATE,\r\n");
		sql.append("                acuser.name ACUSER,\r\n");
		sql.append("                sgroup.group_name SNAME,\r\n");
		sql.append("                complaint.cp_vin VIN,\r\n");
		sql.append("                to_char(complaint.cp_buy_date, 'yyyy-MM-dd hh24:mi:ss') BDATE,\r\n");
		sql.append("                complaint.cp_mileage CPMILEAGE,\r\n");
		sql.append("                mcode.code_desc MILEAGERANGE,\r\n");
		sql.append("                orgPro.root_org_name ORGNAME,\r\n");
		sql.append("                COMPLAINT.CP_DEAL_USER_NAME DEALUSER,\r\n");
		sql.append("                complaint.CP_ACC_DATE CPACCDATE,\r\n");
		sql.append("                complaint.CP_TURN_DATE TURNDATE,\r\n");
		sql.append("                sfcode.code_desc CPSF,\r\n");
		sql.append("                rrecode.cr_user CUSER,\r\n");
		sql.append("                rrecode.cr_date CDATE,\r\n");
		sql.append("                complaint.Cp_Close_Date CPCDATE,\r\n");
		
		sql.append("         TO_CHAR(CEIL((Y.LASTORGDEALDATE - COMPLAINT.CP_TURN_DATE) * 24)) DEALTIME,");
		sql.append("                complaint.CP_LIMIT CPLIMIT,\r\n");
		
		sql.append("ceil(\r\n" );
		sql.append(" case when COMPLAINT.Cp_Cl_Date is null then 0\r\n" );
		sql.append("      else case when COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) >= COMPLAINT.Cp_Cl_Once_Date\r\n" );
		sql.append("                then case when COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) >0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) else 0 end\r\n" );
		sql.append("                else case when COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date>0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date else 0 end  end\r\n" );
		sql.append("      end\r\n" );
		sql.append(")DELAYDATE,/*延期天数(天) 取整*/");
		
		sql.append("                complaint.CP_TURN_DATE + complaint.CP_LIMIT SHOULDCLOSETIME,\r\n");
		sql.append("                ceil((complaint.CP_TURN_DATE + complaint.CP_LIMIT - sysdate)*24) OWNTIME,\r\n");
		
		sql.append("case when COMPLAINT.Cp_Status in("+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+") then\r\n" );
		sql.append("  case when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_CLOSE+" then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then '未及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE <= nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE > nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '未及时关闭'\r\n" );
		sql.append("          end\r\n" );
		sql.append("end  ISTIMELYCLOSE, /*是否及时关闭*/");			
		
		sql.append("                onceSF.Code_Desc CPISONCESF,\r\n");
		
		sql.append("         DECODE(COMPLAINT.Cp_Close_Date, NULL, '',\n"); 
		sql.append("           CASE WHEN COMPLAINT.CP_SF="+Constant.PLEASED+" THEN '正常关闭'\n"); 
		sql.append("                WHEN COMPLAINT.CP_SF="+Constant.YAWP+" THEN '不正常关闭'\n"); 
		sql.append("                ELSE '' END) ISNORMALCLOSE, /*是否正常关闭*/\n"); 
		
		sql.append("                dealcontent.datecontent DEALCONTENT,\r\n");
		sql.append("                to_char(tdr.CD_DATE, 'yyyy-MM-dd hh24:mi:ss') LASTDEALDATE,\r\n");
		sql.append("                scode.code_desc CPSTATUS,\r\n");
		sql.append("                COMPLAINT.CP_DEAL_USER_NAME DUSER,\r\n");
		sql.append(" 				complaint.UPDATE_DATE,\r\n");
		sql.append(" 				dealApplayRecode.CP_DEFER_TYPE DEFER_TYPE\r\n");
		sql.append("  from TT_CRM_COMPLAINT complaint\r\n");
		sql.append("  left join TT_CUSTOMER customer\r\n");
		sql.append("    on customer.ctm_id = complaint.CP_CUS_ID\r\n");
		sql.append("  left join tc_code lc\r\n");
		sql.append("    on lc.code_id = complaint.cp_level\r\n");
		sql.append("  left join tc_code bizC\r\n");
		sql.append("    on bizC.code_id = complaint.CP_BIZ_CONTENT\r\n");
		sql.append("  left join tm_region trP\r\n");
		sql.append("    on trP.Region_Code = complaint.Cp_Province_Id\r\n");
		sql.append("  left join tm_region trC\r\n");
		sql.append("    on trC.Region_Code = complaint.Cp_City_Id\r\n");
		sql.append("  left join tm_org_custom orgObj\r\n");
		sql.append("    on orgObj.ORG_ID = complaint.CP_OBJECT\r\n");
		sql.append("  left join tm_dealer reObj\r\n");
		sql.append("    on reObj.dealer_id = complaint.CP_OBJECT\r\n");
		sql.append("  left join tc_user acuser\r\n");
		sql.append("    on acuser.user_id = complaint.cp_acc_user\r\n");
		sql.append("  left join tm_vhcl_material_group sgroup\r\n");
		sql.append("    on sgroup.group_id = complaint.cp_series_id\r\n");
		sql.append("  left join tc_code mcode\r\n");
		sql.append("    on mcode.code_id = complaint.cp_mileage_range\r\n");
		sql.append("  left join tc_code scode\r\n");
		sql.append("    on scode.code_id = complaint.CP_STATUS\r\n");
		sql.append("  left join TT_CRM_COMPLAINT_DEAL_RECORD dealRecode\r\n");
		sql.append("    on dealRecode.Cp_Id = complaint.cp_id\r\n");
		sql.append("  left join TT_CRM_COMPLAINT_DELAY_RECORD dealApplayRecode \r\n");
		sql.append("    on dealApplayRecode.CP_ID = complaint.cp_id and dealApplayRecode.CP_DEFER_TYPE = "+ Constant.DEFER_TYPE_02 );
		sql.append("  left join (select vod.org_id,\r\n");
		sql.append("                    vod.root_org_name,\r\n");
		sql.append("                    vod.root_org_id,\r\n");
		sql.append("                    vehicle.vin\r\n");
		sql.append("               from tm_vehicle vehicle\r\n");
		sql.append("              inner join vw_org_dealer_service vod\r\n");
		sql.append("                 on vod.dealer_id = vehicle.dealer_id) orgPro\r\n");
		sql.append("    on orgPro.Vin = complaint.cp_vin\r\n");
		sql.append("  left join tc_code sfcode\r\n");
		sql.append("    on sfcode.code_id = complaint.CP_SF\r\n");
		sql.append("  left join (select tt.*\r\n");
		sql.append("               from TT_CRM_COMPLAINT_RETURN_RECORD tt\r\n");
		sql.append("              inner join (select dr.cp_id, max(dr.create_date) cdate\r\n");
		sql.append("                           from TT_CRM_COMPLAINT_RETURN_RECORD dr\r\n");
		sql.append("                          group by dr.cp_id) bb\r\n");
		sql.append("                 on bb.cp_id = tt.cp_id\r\n");
		sql.append("                and bb.cdate = tt.create_date) rrecode\r\n");
		sql.append("    on rrecode.CP_ID = complaint.CP_ID\r\n");
		sql.append("  left join (select tt.*\r\n");
		sql.append("               from TT_CRM_COMPLAINT_DEAL_RECORD tt\r\n");
		sql.append("              inner join (select dr.cp_id, max(dr.create_date) cdate\r\n");
		sql.append("                           from TT_CRM_COMPLAINT_DEAL_RECORD dr\r\n");
		sql.append("                          group by dr.cp_id) bb\r\n");
		sql.append("                 on bb.cp_id = tt.cp_id\r\n");
		sql.append("                and bb.cdate = tt.create_date) tdr\r\n");
		sql.append("    on tdr.CP_ID = complaint.CP_ID\r\n");
		sql.append("  left join (select dr.cp_id,\r\n");
		sql.append("                      za_concat('处理时间:' ||\r\n");
		sql.append("                                    to_char(dr.cd_date,\r\n");
		sql.append("                                            'yyyy-MM-dd hh24:mi:ss') ||\r\n");
		sql.append("                                    '处理内容:' || dr.cp_content) datecontent\r\n");
		sql.append("               from TT_CRM_COMPLAINT_DEAL_RECORD dr\r\n");
		sql.append("              group by dr.cp_id) dealcontent\r\n");
		sql.append("    on dealcontent.cp_id = complaint.CP_ID\r\n");
		sql.append("  left join (select distinct d.parent_org_id, c.user_id\r\n");
		sql.append(" 		from vw_org_dealer_service t, TM_COMPANY A, TM_DEALER B, TC_USER C,tm_org d\r\n");
		sql.append("			where A.COMPANY_ID = B.COMPANY_ID\r\n");
		sql.append("  			AND A.COMPANY_ID = C.COMPANY_ID\r\n");
		sql.append("  			and t.root_dealer_id = b.dealer_id\r\n");
		sql.append("			and t.org_id = d.org_id"); 
		sql.append(" ) orgUser on orgUser.Parent_Org_Id=complaint.cp_deal_user"); 
		sql.append("  left join tc_code onceSF\r\n");
		sql.append("   	on onceSF.Code_Id = complaint.cp_is_once_sf \r\n"); 
		sql.append("    LEFT JOIN (SELECT TO_CHAR(MAX(DR.CD_DATE), 'yyyy-MM-dd hh24:mi:ss') LASTDEALDATE, DR.CP_ID, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CD_DATE,NULL)) BACK_DATE\n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DR GROUP BY DR.CP_ID) X ON X.CP_ID = COMPLAINT.CP_ID");

		sql.append("    LEFT JOIN (SELECT MAX(DRD.CREATE_DATE) LASTORGDEALDATE, DRD.CP_ID \n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DRD where DRD.cp_status = "+Constant.COMPLAINT_STATUS_DOING_ALREADY+"  GROUP BY DRD.CP_ID) Y ON Y.CP_ID = COMPLAINT.CP_ID");
		
		sql.append(" where 1 = 1"); 
		return sql;
	}

	public List<Map<String, Object>> queryComplaintInfoList(String vin,
			String name, String tele, String level,String dealUser, String accUser, String biztype,
			String status, String dealStart, String dealEnd, String dateStart,
			String dateEnd, String region, String pro, String checkStart,
			String checklEnd,String userids,String state) {
		StringBuffer sql = new StringBuffer();
		// 艾春 9.23 修改 投诉查询语句
		sql.append(" SELECT COMPLAINT.CP_NO        CPNO, /*单号*/\n");
		sql.append("         COMPLAINT.CP_ID        CPID, /*ID*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS    STATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_CL_STATUS CLSTATUS, /*延期状态*/\n"); 
		sql.append("         COMPLAINT.CP_LEVEL     CPLEVEL, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n"); 
		sql.append("         TRP.REGION_NAME      PRO, /*省份*/\n"); 
		sql.append("         TRC.REGION_NAME      CITY, /*城市*/\n"); 
		sql.append("         COMPLAINT.CP_NAME    CTMNAME, /*客户名称*/\n"); 
		sql.append("         COMPLAINT.CP_CUS_ID  CTMID, /*客户ID*/\n"); 
		sql.append("         COMPLAINT.CP_PHONE   PHONE,  /*电话*/\n"); 
		sql.append("         COMPLAINT.CP_CONTENT CPCONT,  /*内容类型*/\n"); 
		sql.append("         NVL(ORGOBJ.ORG_NAME, REOBJ.DEALER_NAME) CPOBJECT,/*抱怨对象*/\n"); 
		sql.append("         COMPLAINT.CREATE_DATE CREATEDATE,/*创建日期*/\n"); 
		sql.append("         ACUSER.NAME ACUSER,/*受理人*/\n"); 
		sql.append("         SGROUP.GROUP_NAME SNAME,/*车种*/\n"); 
		sql.append("         COMPLAINT.CP_VIN VIN,\n"); 
		sql.append("         TO_CHAR(COMPLAINT.CP_BUY_DATE, 'yyyy-MM-dd hh24:mi:ss') BDATE,/*购买日期*/\n"); 
		sql.append("         COMPLAINT.CP_MILEAGE CPMILEAGE, /*行驶里程*/\n"); 
		sql.append("         /*COMPLAINT.CP_MILEAGE_RANGE, /*需转换*/\n"); 
		sql.append("         ORGPRO.ORG_NAME ORGNAME,/*大区*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DEALUSER,/*处理人*/\n"); 
		sql.append("         COMPLAINT.CP_ACC_DATE  CPACCDATE,/*受理日期*/\n"); 
		sql.append("         COMPLAINT.CP_TURN_DATE TURNDATE,/*转出日期*/\n"); 
		sql.append("         decode(COMPLAINT.CP_STATUS,"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.PLEASED+","+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.YAWP+",null) CPSF, /*需转换*/\n"); 
		sql.append("         COMPLAINT.CP_CLOSE_DATE CPCDATE, /*关闭日期*/\n"); 
		sql.append("         TO_CHAR(CEIL((Y.LASTORGDEALDATE - COMPLAINT.CP_TURN_DATE) * 24)) DEALTIME,");
		sql.append("         COMPLAINT.CP_LIMIT CPLIMIT, /*规定时限*/\n"); 

		sql.append("ceil(\r\n" );
		sql.append(" case when COMPLAINT.Cp_Cl_Date is null then 0\r\n" );
		sql.append("      else case when COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) >= COMPLAINT.Cp_Cl_Once_Date\r\n" );
		sql.append("                then case when COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) >0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.CP_TURN_DATE - NVL(COMPLAINT.CP_LIMIT,0) else 0 end\r\n" );
		sql.append("                else case when COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date>0 then COMPLAINT.Cp_Cl_Date-COMPLAINT.Cp_Cl_Once_Date else 0 end  end\r\n" );
		sql.append("      end\r\n" );
		sql.append(")DELAYDATE,/*延期天数(天) 取整*/");

		sql.append("         /*COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT SHOULDCLOSETIME, */\n"); 
		sql.append("         /*CEIL((COMPLAINT.CP_TURN_DATE + COMPLAINT.CP_LIMIT - SYSDATE)*24) OWNTIME, */\n"); 

		sql.append("case when COMPLAINT.Cp_Status in("+Constant.COMPLAINT_STATUS_CLOSE+","+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+") then\r\n" );
		sql.append("  case when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_CLOSE+" then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and COMPLAINT.Cp_Cl_Once_Date > COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0) then '未及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE <= nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '及时关闭'\r\n" );
		sql.append("          when COMPLAINT.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and X.BACK_DATE > nvl(COMPLAINT.Cp_Cl_Date,COMPLAINT.CP_TURN_DATE + NVL(COMPLAINT.CP_LIMIT,0)) then '未及时关闭'\r\n" );
		sql.append("          end\r\n" );
		sql.append("end  ISTIMELYCLOSE, /*是否及时关闭*/");			

		sql.append("         COMPLAINT.CP_IS_ONCE_SF CPISONCESF, /*需转换*/\n"); 
		sql.append("         DECODE(COMPLAINT.Cp_Close_Date, NULL, '',\n"); 
		sql.append("           CASE WHEN COMPLAINT.CP_SF="+Constant.PLEASED+" THEN '正常关闭'\n"); 
		sql.append("                WHEN COMPLAINT.CP_SF="+Constant.YAWP+" THEN '不正常关闭'\n"); 
		sql.append("                ELSE '' END) ISNORMALCLOSE, /*是否正常关闭*/\n"); 
		sql.append("         X.BACK_DATE LASTDEALDATE, /*最终反馈时间*/\n"); 
		sql.append("         COMPLAINT.CP_STATUS CPSTATUS, /*状态*/\n"); 
		sql.append("         COMPLAINT.CP_DEAL_USER_NAME DUSER /*当前处理人*/\n"); 
		sql.append("    FROM TT_CRM_COMPLAINT COMPLAINT\n"); 
		sql.append("    LEFT JOIN TM_REGION TRP ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID AND TRP.REGION_TYPE = "+Constant.REGION_TYPE_02+"\n"); 
		sql.append("    LEFT JOIN TM_REGION TRC ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID AND TRC.REGION_TYPE = "+Constant.REGION_TYPE_03+"\n"); 
		sql.append("    LEFT JOIN tm_org_custom ORGOBJ ON ORGOBJ.ORG_ID = COMPLAINT.CP_OBJECT\n"); 
		sql.append("    LEFT JOIN TM_DEALER REOBJ ON REOBJ.DEALER_ID = COMPLAINT.CP_OBJECT \n"); 
		sql.append("    LEFT JOIN TC_USER ACUSER ON ACUSER.USER_ID = COMPLAINT.CP_ACC_USER\n"); 
		sql.append("    LEFT JOIN TM_VHCL_MATERIAL_GROUP SGROUP ON SGROUP.GROUP_ID = COMPLAINT.CP_SERIES_ID AND SGROUP.GROUP_LEVEL = 2\n"); 
		sql.append("    LEFT JOIN TM_ORG ORGPRO ON COMPLAINT.CP_OBJECT_ORG = ORGPRO.ORG_ID AND ORGPRO.DUTY_TYPE = "+Constant.DUTY_TYPE_LARGEREGION+"\n"); 
		sql.append("    LEFT JOIN (SELECT TO_CHAR(MAX(DR.CD_DATE), 'yyyy-MM-dd hh24:mi:ss') LASTDEALDATE, DR.CP_ID, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CD_DATE,NULL)) BACK_DATE\n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DR GROUP BY DR.CP_ID) X ON X.CP_ID = COMPLAINT.CP_ID");

		sql.append("    LEFT JOIN (SELECT MAX(DRD.CREATE_DATE) LASTORGDEALDATE, DRD.CP_ID \n"); 
		sql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DRD where DRD.cp_status = "+Constant.COMPLAINT_STATUS_DOING_ALREADY+"  GROUP BY DRD.CP_ID) Y ON Y.CP_ID = COMPLAINT.CP_ID");
		
		sql.append(" where complaint.cp_biz_type = "+Constant.TYPE_COMPLAIN);
		sql.append(" and complaint.CP_DEAL_ORG in ('"+userids+"') and (complaint.CP_DEAL_DEALER is null or complaint.CP_DEAL_DEALER = -1) and complaint.CP_STATUS in('"+state+"') ");
		if(StringUtil.notNull(vin)){
			sql.append(" and complaint.cp_vin like '%"+vin+"%' ");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and complaint.cp_name like '%"+name+"%' ");
		}
		if(StringUtil.notNull(tele)){
			sql.append(" and complaint.cp_phone like '%"+tele+"%' ");
		}
		if(StringUtil.notNull(level)){
			sql.append(" and complaint.cp_level = '"+level+"' ");
		}
		if(StringUtil.notNull(dealUser)){
			sql.append(" and COMPLAINT.CP_DEAL_USER_NAME like '%"+dealUser+"%' ");
		}
		if(StringUtil.notNull(accUser)){
			sql.append(" and acuser.name like '%"+accUser+"%' ");
		}
		if(StringUtil.notNull(biztype)){
			sql.append(" and complaint.CP_BIZ_CONTENT = '"+biztype+"' ");
		}
		if(StringUtil.notNull(status) && status.equals("unClose")){
			sql.append(" and complaint.cp_status not in ('"+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+"','"+Constant.COMPLAINT_STATUS_CLOSE+"') ");
		}else if(StringUtil.notNull(status) && !status.equals("unClose")){
			sql.append(" and complaint.cp_status = '"+status+"' ");
		}
		if(StringUtil.notNull(dateStart)){
			sql.append(" and complaint.cp_acc_date >=to_date('"+dateStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dateEnd)){
			sql.append(" and complaint.cp_acc_date<=to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(dealStart)){
			sql.append(" and complaint.CP_TURN_DATE >=to_date('"+dealStart+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(dealEnd)){
			sql.append(" and complaint.CP_TURN_DATE<=to_date('"+dealEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(region)){
			sql.append(" and complaint.CP_DEAL_ORG = "+ region );
		}
		if(StringUtil.notNull(pro)){
			sql.append(" and complaint.CP_PROVINCE_ID = "+pro);
		}
		sql.append(" order by complaint.CP_ACC_DATE desc ");
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	/**
	 * 查询延期申请记录
	 * @param cpid
	 */
	public TtCrmComplaintDelayRecordPO queryApplayDelay(Long cpid,String status,String applayType) {
		TtCrmComplaintDelayRecordPO ttCrmComplaintDelayRecordPO = new TtCrmComplaintDelayRecordPO();
		ttCrmComplaintDelayRecordPO.setCpId(cpid);
		ttCrmComplaintDelayRecordPO.setClVerifyStatus(Integer.parseInt(status));
		if(applayType!=null && (!"".equals(applayType))){
			ttCrmComplaintDelayRecordPO.setApplyType(Integer.parseInt(applayType));
		}
		
		List<TtCrmComplaintDelayRecordPO> list = this.select(ttCrmComplaintDelayRecordPO);
		//根据Collections.sort重载方法来实现   按创建时间倒序
		Collections.sort(list,new Comparator<TtCrmComplaintDelayRecordPO>(){   
	        public int compare(TtCrmComplaintDelayRecordPO TtCrmComplaintDelayRecordPO1, TtCrmComplaintDelayRecordPO TtCrmComplaintDelayRecordPO2) {   
	                return TtCrmComplaintDelayRecordPO2.getCreateDate().compareTo(TtCrmComplaintDelayRecordPO1.getCreateDate());   
			}             
		});
		if(list!=null&&list.size()>0) return list.get(0);
		else return null;
	}
	/**
	 * 查询延期申请交给管理员审核的记录
	 * @param cpid
	 */
	public TtCrmComplaintDelayRecordPO queryApplayDelayToAdmin(Long cpid) {
		TtCrmComplaintDelayRecordPO ttCrmComplaintDelayRecordPO = new TtCrmComplaintDelayRecordPO();
		ttCrmComplaintDelayRecordPO.setCpId(cpid);
		ttCrmComplaintDelayRecordPO.setClVerifyStatus(Constant.PASS_Manager_03);
		List<TtCrmComplaintDelayRecordPO> list = this.select(ttCrmComplaintDelayRecordPO);
		//根据Collections.sort重载方法来实现   按创建时间倒序
		Collections.sort(list,new Comparator<TtCrmComplaintDelayRecordPO>(){   
	        public int compare(TtCrmComplaintDelayRecordPO TtCrmComplaintDelayRecordPO1, TtCrmComplaintDelayRecordPO TtCrmComplaintDelayRecordPO2) {   
	                return TtCrmComplaintDelayRecordPO2.getCreateDate().compareTo(TtCrmComplaintDelayRecordPO1.getCreateDate());   
			}             
		}); 
		if(list!=null&&list.size()>0) return list.get(0);
		else return null;
	}
	
	public List<Map<String,Object>> queryRejectDealRecord(Long cpid){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select * from TT_CRM_COMPLAINT_DEAL_RECORD t where t.cp_status ="+Constant.COMPLAINT_STATUS_DOING_REJECT+" and t.cp_id = "+cpid); 
		return this.pageQuery(sbSql.toString(), null, this.getFunName());
	}
	
	public List<Map<String,Object>> queryAlreadyDealRecord(Long cpid){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select * from TT_CRM_COMPLAINT_DEAL_RECORD t where t.cp_status ="+Constant.COMPLAINT_STATUS_DOING_ALREADY+" and t.cp_id = "+cpid); 
		return this.pageQuery(sbSql.toString(), null, this.getFunName());
	}
	
	public List<Map<String,Object>> queryPid(Long cpid){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT t.parent_org_id FROM tm_org t WHERE t.org_id = "+cpid); 
		return this.pageQuery(sbSql.toString(), null, this.getFunName());
	}
	
	public List<Map<String,Object>> queryDealRecordList(Long cpid){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select to_char(t.cp_id) CPID,to_char(t.cd_date,'yyyy-mm-dd hh24:mi') CDDATE,Cp_Content CDCONTENT from TT_CRM_COMPLAINT_DEAL_RECORD t where t.cp_id = "+cpid+"  ORDER by t.cd_date,t.CD_ID "); 
		return this.pageQuery(sbSql.toString(), null, this.getFunName());
	}
	
	public List<Map<String,Object>> queryReturnRecordList(Long cpid){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select to_char(t.cp_id) CPID,to_char(t.cr_date,'yyyy-mm-dd hh24:mi') CRDATE ,t.CR_USER ,t.cr_content CRCONTENT from Tt_Crm_Complaint_Return_Record t where t.cp_id = "+cpid+ " ORDER by t.cr_date,t.CR_ID "); 
		return this.pageQuery(sbSql.toString(), null, this.getFunName());
	}
	
	public List<Map<String,Object>> queryVeriftyRecordList(Long cpid){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select to_char(t.cp_id) CPID,to_char(t.CL_DATE, 'yyyy-MM-dd') CLDATE, \r\n"); 
		// 艾春 2013.11.26 修改延期天数
//		sbSql.append("       to_char(t.CL_DATE-to_date(to_char(complaint.CP_LIMIT + complaint.CP_TURN_DATE,'yyyy-MM-dd'),'yyyy-MM-dd')) DAYS,\r\n");
		sbSql.append("       to_char(round(t.CL_DATE-NVL(complaint.CP_CL_ONCE_DATE,to_date(to_char(complaint.CP_LIMIT + complaint.CP_TURN_DATE,'yyyy-MM-dd hh24:mi'),'yyyy-MM-dd hh24:mi')),2)) DAYS,\r\n");
		// 艾春 2013.11.26 修改延期天数
		sbSql.append("       to_char(t.CL_CONTENT) CLCONT,\r\n");
		sbSql.append("       to_char(t.CL_USER) CLUSER,\r\n");
		sbSql.append("       to_char(t.CL_VERIFY_DATE, 'yyyy-MM-dd hh24:mi') CLVERIFYDATE,\r\n");
		sbSql.append("       to_char(t.CL_VERIFY_CONTENT) CLVERIFYCONTENT,\r\n");
		sbSql.append("       to_char(t.CL_VERIFY_USER) CLVERIFYUSER,\r\n");
		sbSql.append("       to_char(dt.code_desc)  CPDEFERTYPE ,\r\n");
		sbSql.append("       to_char(tcode.code_desc) CLVERIFYSTATUS, \r\n");
		sbSql.append("       to_char(t.CREATE_DATE, 'yyyy-MM-dd hh24:mi') CREATEDATE \r\n");
		sbSql.append("  from tt_crm_complaint_delay_record t \r\n");
		sbSql.append("  left join TT_CRM_COMPLAINT complaint on complaint.cp_id = t.cp_id\r\n");
		sbSql.append("  left join tc_code tcode on tcode.code_id = t.CL_VERIFY_STATUS\r\n");
		sbSql.append("  left join tc_code dt on dt.code_id = t.cp_defer_type\r\n"); 
		sbSql.append("	where t.cp_id = "+cpid+ "  order by t.CL_DATE,t.CL_ID ");
		return this.pageQuery(sbSql.toString(), null, this.getFunName());
	}

}
