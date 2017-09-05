package com.infodms.yxdms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.exception.BizException;
import com.infodms.yxdms.entity.warranty.TtAsWrWarrantyManualPO;
import com.infodms.yxdms.entity.warranty.TtAsWrWarrantyManualSubPO;
import com.infodms.yxdms.utils.BaseUtils;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("rawtypes")
public class WarrantyManualDAO extends BaseDao {
	
	public static Logger logger = Logger.getLogger(WarrantyManualDAO.class);
	private static final WarrantyManualDAO dao = new WarrantyManualDAO();

	public static final WarrantyManualDAO getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> warrantyManualList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*\n" );
		sb.append("  from (select (select c.name from tc_user c where c.user_id = t.create_by) as create_name,\n" );
		sb.append("               (select c.name from tc_user c where c.user_id = t.audit_by) as audit_name,\n" );
		sb.append("               (select c.name from tc_user c where c.user_id = t.send_by) as send_name,\n" );
		sb.append("               (select v.root_org_name\n" );
		sb.append("                  from vw_org_dealer_service v\n" );
		sb.append("                 where v.dealer_id = tm.dealer_id) as root_org_name,\n" );
		sb.append("               tm.dealer_code,\n" );
		sb.append("               tm.dealer_shortname,\n" );
		sb.append("               tm.dealer_name,\n" );
		sb.append("               t.*\n" );
		sb.append("          from tt_as_wr_Warranty_Manual t, tm_dealer tm\n" );
		sb.append("         where t.dealer_id = tm.dealer_id) t\n" );
		sb.append(" where 1 = 1\n ");
		if(!"".equals(BaseUtils.checkNull(loginUser.getDealerId()))){
			DaoFactory.getsql(sb, "t.dealer_id", loginUser.getDealerId(), 1);
		}
		DaoFactory.getsql(sb, "t.dealer_contact_person", DaoFactory.getParam(request, "dealer_contact_person"), 2);
		DaoFactory.getsql(sb, "t.dealer_contact_phone", DaoFactory.getParam(request, "dealer_contact_phone"), 2);
		DaoFactory.getsql(sb, "t.dealer_shortname", DaoFactory.getParam(request, "dealer_shortname"), 2);
		DaoFactory.getsql(sb, "t.dealer_code", DaoFactory.getParam(request, "dealer_code"), 2);
		DaoFactory.getsql(sb, "t.status", DaoFactory.getParam(request, "status"), 1);
		DaoFactory.getsql(sb, "t.audit_name", DaoFactory.getParam(request, "audit_name"), 2);
		DaoFactory.getsql(sb, "t.audit_date", DaoFactory.getParam(request, "audit_date_begin"), 3);
		DaoFactory.getsql(sb, "t.audit_date", DaoFactory.getParam(request, "audit_date_end"), 4);
		DaoFactory.getsql(sb, "t.report_date", DaoFactory.getParam(request, "report_date_begin"), 3);
		DaoFactory.getsql(sb, "t.report_date", DaoFactory.getParam(request, "report_date_end"), 4);
		DaoFactory.getsql(sb, "t.is_send", DaoFactory.getParam(request, "is_send"), 2);
		DaoFactory.getsql(sb, "t.root_org_name", DaoFactory.getParam(request, "root_org_name"), 1);
		DaoFactory.getsql(sb, "t.report_no", DaoFactory.getParam(request, "report_no"), 2);
		return this.pageQuery(sb.toString(),null,this.getFunName(), pageSize, currPage); 
	}

	@SuppressWarnings("unchecked")
	public int addWarrantyManual(RequestWrapper request, AclUserBean loginUser) {
		int res=1;
		String dealerContactPerson = DaoFactory.getParam(request, "dealer_contact_person");
		String dealerContactPhone = DaoFactory.getParam(request, "dealer_contact_phone");
		String report_no = DaoFactory.getParam(request, "report_no");
		String id = DaoFactory.getParam(request, "id");
		String identify = DaoFactory.getParam(request, "identify");
		String remark = DaoFactory.getParam(request, "remark");
		String[] report_remarks = DaoFactory.getParams(request, "report_remark");
		String[] pay_types = DaoFactory.getParams(request, "pay_type");
		String[] vins = DaoFactory.getParams(request, "vin");
		String[] fjids=request.getParamValues("fjid");
		try {
			if("".equals(report_no)){//新增
				TtAsWrWarrantyManualPO t=new TtAsWrWarrantyManualPO();
				t.setDealerContactPerson(dealerContactPerson);
				t.setDealerContactPhone(dealerContactPhone);
				t.setRemark(remark);
				t.setCreateBy(loginUser.getUserId());
				t.setCreateDate(new Date());
				t.setReportNo(getReportNo());
				t.setDealerId(Long.valueOf(loginUser.getDealerId()));
				Long pkId = DaoFactory.getPkId();
				t.setId(pkId);
				t.setIsSend(0);
				if("0".equals(identify)){
					t.setStatus(95451001);
				}else{
					t.setStatus(95451002);
					t.setReportDate(new Date());
				}
				this.insert(t);
				int temp=0;
				for (String vin : vins) {
					TtAsWrWarrantyManualSubPO s=new TtAsWrWarrantyManualSubPO();
					s.setParentId(pkId);
					s.setId(DaoFactory.getPkId());
					s.setVin(vin);
					s.setReportRemark(report_remarks[temp]);
					s.setPayType(Integer.parseInt(pay_types[temp]));
					this.insert(s);
					temp++;
				}
				delAndReinsetFile(loginUser, fjids, pkId.toString());
			}else{
				TtAsWrWarrantyManualPO t1=new TtAsWrWarrantyManualPO();
				Long pkId = Long.valueOf(id);
				t1.setId(pkId);
				TtAsWrWarrantyManualPO t2=new TtAsWrWarrantyManualPO();
				t2.setDealerContactPerson(dealerContactPerson);
				t2.setDealerContactPhone(dealerContactPhone);
				t2.setRemark(remark);
				if("0".equals(identify)){
					t2.setStatus(95451001);
				}else{
					t2.setStatus(95451002);
					t2.setReportDate(new Date());
				}
				t2.setIsSend(0);
				this.update(t1, t2);
				TtAsWrWarrantyManualSubPO temp=new TtAsWrWarrantyManualSubPO();
				temp.setParentId(pkId);
				this.delete(temp);
				int i=0;
				for (String vin : vins) {
					TtAsWrWarrantyManualSubPO s=new TtAsWrWarrantyManualSubPO();
					s.setParentId(pkId);
					s.setId(DaoFactory.getPkId());
					s.setVin(vin);
					s.setReportRemark(report_remarks[i]);
					s.setPayType(Integer.parseInt(pay_types[i]));
					this.insert(s);
					i++;
				}
				delAndReinsetFile(loginUser, fjids, pkId.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			res=-1;
			throw new RuntimeException(e);
		}
		return res;
	}

	private void delAndReinsetFile(AclUserBean loginUser, String[] fjids,String pkId) throws SQLException {
		FileUploadManager.delAllFilesUploadByBusiness(pkId, fjids);
		FileUploadManager.fileUploadByBusiness(pkId, fjids, loginUser);
	}
	@SuppressWarnings("unchecked")
	private String getReportNo() {
		StringBuffer sb= new StringBuffer();
		sb.append("select to_char(sysdate,'yyyymm')|| LPAD(seq_Report_No.NEXTVAL,4,'0') as num From DUAL");
		List<Map<String,Object>> ps=this.pageQuery(sb.toString(), null, this.getFunName());
		return ps.get(0).get("NUM").toString();
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> findInfoByVin(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT A.VIN, A.ENGINE_NO, TO_CHAR(A.PURCHASED_DATE, 'YYYY-MM-DD') AS PURCHASED_DATE, nvl(A.MILEAGE,0) mileage, nvl(A.FREE_TIMES,0) free_times, A.SERIES_ID, A.MODEL_ID,\n" );
		sb.append("       a.license_no VEHICLE_NO, ba.area_name AS YIELDLY, ba.area_id AS Y_ID, D1.GROUP_NAME AS SERIES_CODE, D2.GROUP_NAME AS MODEL_CODE,\n" );
		sb.append("       E.CTM_NAME, E.MAIN_PHONE, E.CTM_ID, E.ADDRESS, F.GAME_CODE, F.ID AS GAME_ID, G.RULE_CODE\n" );
		sb.append("  FROM TM_VEHICLE A, TT_DEALER_ACTUAL_SALES B, tm_business_area ba, TM_VHCL_MATERIAL_GROUP D1, TM_VHCL_MATERIAL_GROUP D2,\n" );
		sb.append("       TT_CUSTOMER E, TT_AS_WR_GAME F, TT_AS_WR_RULE G\n" );
		sb.append(" WHERE A.VIN = '"+DaoFactory.getParam(request, "vin")+"'\n" );
		sb.append("   AND A.VEHICLE_ID = B.VEHICLE_ID(+)\n" );
		sb.append("   AND A.YIELDLY = ba.area_id(+)\n" );
		sb.append("   AND A.SERIES_ID = D1.GROUP_ID(+)\n" );
		sb.append("   AND A.MODEL_ID = D2.GROUP_ID(+)\n" );
		sb.append("   AND A.CLAIM_TACTICS_ID = F.ID(+)\n" );
		sb.append("   AND B.CTM_ID = E.CTM_ID(+)\n" );
		sb.append("   AND F.RULE_ID = G.ID(+)");
		Map<String,Object> map = this.pageQueryMap(sb.toString(), null, getFunName());
		return map;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> findWarrantyManual(AclUserBean loginUser,RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT t.*, tm.dealer_shortname, tm.dealer_code\n" );
		sb.append("  from tt_as_wr_Warranty_Manual t, tm_dealer tm\n" );
		sb.append(" where tm.dealer_id = t.dealer_id\n");
		DaoFactory.getsql(sb, "t.id", DaoFactory.getParam(request, "id"), 1);
		Map<String,Object> map = this.pageQueryMap(sb.toString(), null, getFunName());
		return map;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findWarrantyManualSub(AclUserBean loginUser, RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT s.*,\n" );
		sb.append("       A.ENGINE_NO,\n" );
		sb.append("       TO_CHAR(A.PURCHASED_DATE, 'YYYY-MM-DD') AS PURCHASED_DATE,\n" );
		sb.append("       nvl(A.MILEAGE, 0) mileage,\n" );
		sb.append("       nvl(A.FREE_TIMES, 0) free_times,\n" );
		sb.append("       A.SERIES_ID,\n" );
		sb.append("       A.MODEL_ID,\n" );
		sb.append("       a.license_no VEHICLE_NO,\n" );
		sb.append("       ba.area_name AS YIELDLY,\n" );
		sb.append("       ba.area_id AS Y_ID,\n" );
		sb.append("       D1.GROUP_NAME AS SERIES_CODE,\n" );
		sb.append("       D2.GROUP_NAME AS MODEL_CODE,\n" );
		sb.append("       E.CTM_NAME,\n" );
		sb.append("       E.MAIN_PHONE,\n" );
		sb.append("       E.CTM_ID,\n" );
		sb.append("       E.ADDRESS,\n" );
		sb.append("       F.GAME_CODE,\n" );
		sb.append("       F.ID AS GAME_ID,\n" );
		sb.append("       G.RULE_CODE\n" );
		sb.append("  FROM TM_VEHICLE                   A,\n" );
		sb.append("       TT_DEALER_ACTUAL_SALES       B,\n" );
		sb.append("       tm_business_area             ba,\n" );
		sb.append("       TM_VHCL_MATERIAL_GROUP       D1,\n" );
		sb.append("       TM_VHCL_MATERIAL_GROUP       D2,\n" );
		sb.append("       TT_CUSTOMER                  E,\n" );
		sb.append("       TT_AS_WR_GAME                F,\n" );
		sb.append("       TT_AS_WR_RULE                G,\n" );
		sb.append("       Tt_As_Wr_Warranty_Manual_Sub s\n" );
		sb.append(" WHERE 1 = 1\n" );
		sb.append("   and s.vin = a.vin\n" );
		sb.append("   AND A.VEHICLE_ID = B.VEHICLE_ID(+)\n" );
		sb.append("   AND A.YIELDLY = ba.area_id(+)\n" );
		sb.append("   AND A.SERIES_ID = D1.GROUP_ID(+)\n" );
		sb.append("   AND A.MODEL_ID = D2.GROUP_ID(+)\n" );
		sb.append("   AND A.CLAIM_TACTICS_ID = F.ID(+)\n" );
		sb.append("   AND B.CTM_ID = E.CTM_ID(+)\n" );
		sb.append("   AND F.RULE_ID = G.ID(+)\n");
		DaoFactory.getsql(sb, "s.parent_id", DaoFactory.getParam(request, "id"), 1);
		return this.pageQuery(sb.toString(), null, getFunName());
	}

	@SuppressWarnings("unchecked")
	public int auditWarrantyManual(AclUserBean loginUser, RequestWrapper request) {
		String send_no = DaoFactory.getParam(request, "send_no");
		String send_date = DaoFactory.getParam(request, "send_date");
		String id = DaoFactory.getParam(request, "id");
		String audit_remark = DaoFactory.getParam(request, "audit_remark");
		String[] pay_types = DaoFactory.getParams(request, "pay_type");
		String[] sub_ids = DaoFactory.getParams(request, "sub_id");
		String[] vins = DaoFactory.getParams(request, "vin");
		
		int res=1;
		try {
			TtAsWrWarrantyManualPO t1=new TtAsWrWarrantyManualPO();
			Long pkId = Long.valueOf(id);
			t1.setId(pkId);
			TtAsWrWarrantyManualPO t2=new TtAsWrWarrantyManualPO();
			if(!"".equals(send_no) && !"".equals(send_date)){
				t2.setSendNo(send_no);
				t2.setSendDate(BaseUtils.getDate(send_date.replaceAll("\\+", " "), 3));
				t2.setIsSend(1);
				t2.setSendBy(loginUser.getUserId());
			}
			t2.setStatus(95451004);
			t2.setAuditBy(loginUser.getUserId());
			t2.setAuditDate(new Date());
			t2.setAuditRemark(audit_remark);
			this.update(t1, t2);
			for (int i = 0; i < sub_ids.length; i++) {
				String sub_id = sub_ids[i];
				String operation = DaoFactory.getParam(request, "operation"+sub_id);
				String pay_type = pay_types[i];
				String vin = vins[i];
				TtAsWrWarrantyManualSubPO s1=new TtAsWrWarrantyManualSubPO();
				s1.setId(Long.valueOf(sub_id));
				TtAsWrWarrantyManualSubPO s2=new TtAsWrWarrantyManualSubPO();
				s2.setAuditBy(loginUser.getUserId());
				s2.setAuditDate(new Date());
				try {
					String partIdByVin = getPartIdByVin(vin);
					s2.setPartId(Long.valueOf(partIdByVin));
				} catch (Exception e) {
					throw new RuntimeException("  没有"+vin+"对应的质保手册，审核无法通过！ ");
				}
				int operationTemp = Integer.parseInt(operation);
				s2.setOperation(operationTemp);
				int payType = Integer.parseInt(pay_type);
				s2.setPayType(payType);
				this.update(s1, s2);
			}
			this.callBypayType(id, 94431001);
		} catch (Exception e) {
			e.printStackTrace();
			res=-1;
			throw new RuntimeException(e);
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	public String getPartIdByVin(String vin){
		StringBuffer sb= new StringBuffer();
		sb.append("select decode(a.MODEL_CODE, 'B2', 'S2', 'B3', 'S3','H2E','H2', a.MODEL_CODE) as MODEL_CODE\n" );
		sb.append("     from vw_material_group_mat a, tm_vehicle b\n" );
		sb.append("    where a.MATERIAL_ID = b.Material_Id \n");
		DaoFactory.getsql(sb, "b.vin", vin, 1);
		Map<String,Object> map = this.pageQueryMap(sb.toString(), null, getFunName());
		String model_code = BaseUtils.checkNull(map.get("MODEL_CODE"));
		
		StringBuffer sbTemp= new StringBuffer();
		sbTemp.append("SELECT D.PART_ID\n" );
		sbTemp.append("  FROM TT_PART_DEFINE D\n" );
		sbTemp.append(" WHERE (D.PART_CNAME LIKE '%质量担保手册%' OR D.PART_CNAME LIKE '%质保手册%')\n" );
//		sbTemp.append("   and d.state = 10011001\n");  设置是否有效暂时不用
		sbTemp.append("   and d.part_code in ('97990031-203','97990031-205','97990031-206','97990031-222','97990190-001')\n");
		DaoFactory.getsql(sbTemp, "d.part_cname", model_code, 2);
		Map<String,Object> mapTemp = this.pageQueryMap(sbTemp.toString(), null, getFunName());
		String id = BaseUtils.checkNull(mapTemp.get("PART_ID"));
		return id;
	}
	/**
	 * 调用老袁的接口数据
	 * @param sub_id
	 * @param payType
	 */
	@SuppressWarnings("unchecked")
	private void callBypayType(String id, int payType) {
		if(94431001==payType){
			POFactory poFactory = POFactoryBuilder.getInstance();
			List ins = new LinkedList<Object>();
			ins.add(0,id.toString());
			ins.add(1,Constant.PART_CODE_RELATION_59);
			ins.add(2,1);
			poFactory.callProcedure("P_CREATEORDER", ins, null);
		}
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> findWarrantyData(RequestWrapper request, AclUserBean loginUser, Integer pageSizeMax,Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*\n" );
		sb.append("  from (select (select c.name from tc_user c where c.user_id = t.create_by) as create_name,\n" );
		sb.append("               (select c.name from tc_user c where c.user_id = t.audit_by) as audit_name,\n" );
		sb.append("               (select c.name from tc_user c where c.user_id = t.send_by) as send_name,\n" );
		sb.append("               (select v.root_org_name\n" );
		sb.append("                  from vw_org_dealer_service v\n" );
		sb.append("                 where v.dealer_id = tm.dealer_id) as root_org_name,\n" );
		sb.append("               tm.dealer_code,\n" );
		sb.append("               tm.dealer_shortname,\n" );
		sb.append("               tm.dealer_name,(select c.code_desc from tc_code c where c.code_id=t.status) as status_name,\n" );
		sb.append("               t.*,decode(is_send,0,'否',1,'是') as is_send_temp,decode(s.operation,1,'同意',0,'不同意') as operation,(select c.code_desc from tc_code c where c.code_id=s.pay_type) as pay_type,s.vin,s.report_remark \n" );
		sb.append("          from tt_as_wr_Warranty_Manual t, tm_dealer tm,tt_as_wr_Warranty_Manual_sub s \n" );
		sb.append("         where t.dealer_id = tm.dealer_id and s.parent_id=t.id ) t\n" );
		sb.append(" where 1 = 1\n ");
		DaoFactory.getsql(sb, "t.dealer_contact_person", DaoFactory.getParam(request, "dealer_contact_person"), 2);
		DaoFactory.getsql(sb, "t.dealer_contact_phone", DaoFactory.getParam(request, "dealer_contact_phone"), 2);
		DaoFactory.getsql(sb, "t.dealer_shortname", DaoFactory.getParam(request, "dealer_shortname"), 2);
		DaoFactory.getsql(sb, "t.dealer_code", DaoFactory.getParam(request, "dealer_code"), 2);
		DaoFactory.getsql(sb, "t.status", DaoFactory.getParam(request, "status"), 1);
		DaoFactory.getsql(sb, "t.audit_name", DaoFactory.getParam(request, "audit_name"), 2);
		DaoFactory.getsql(sb, "t.audit_date", DaoFactory.getParam(request, "audit_date_begin"), 3);
		DaoFactory.getsql(sb, "t.audit_date", DaoFactory.getParam(request, "audit_date_end"), 4);
		DaoFactory.getsql(sb, "t.report_date", DaoFactory.getParam(request, "report_date_begin"), 3);
		DaoFactory.getsql(sb, "t.report_date", DaoFactory.getParam(request, "report_date_end"), 4);
		DaoFactory.getsql(sb, "t.is_send", DaoFactory.getParam(request, "is_send"), 2);
		DaoFactory.getsql(sb, "t.root_org_name", DaoFactory.getParam(request, "root_org_name"), 1);
		DaoFactory.getsql(sb, "t.report_no", DaoFactory.getParam(request, "report_no"), 2);
		return this.pageQuery(sb.toString(),null,this.getFunName(), pageSizeMax, currPage); 
	}

	@SuppressWarnings("unchecked")
	public void expotWarrantyData(ActionContext act,PageResult<Map<String, Object>> list) {
		try {
			String[] head={"大区","服务商代码","服务商简称","报告编号","VIN","状态","用户姓名","申请时间","审核人","审核时间","付费方式","是否同意","是否回运","发运人","发运单号"};
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String ORG_NAME = BaseUtils.checkNull(map.get("ROOT_ORG_NAME"));
					String DEALER_CODE = BaseUtils.checkNull(map.get("DEALER_CODE"));
					String DEALER_SHORTNAME = BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					String REPORT_NO = BaseUtils.checkNull(map.get("REPORT_NO"));
					String VIN = BaseUtils.checkNull(map.get("VIN"));
					String STATUS_NAME = BaseUtils.checkNull(map.get("STATUS_NAME"));
					String CREATE_NAME = BaseUtils.checkNull(map.get("CREATE_NAME"));
					String CREATE_DATE = BaseUtils.checkNull(map.get("CREATE_DATE"));
					String AUDIT_NAME = BaseUtils.checkNull(map.get("AUDIT_NAME"));
					String AUDIT_DATE = BaseUtils.checkNull(map.get("AUDIT_DATE"));
					String PAY_TYPE = BaseUtils.checkNull(map.get("PAY_TYPE"));
					String OPERATION = BaseUtils.checkNull(map.get("OPERATION"));
					String IS_SEND_TEMP = BaseUtils.checkNull(map.get("IS_SEND_TEMP"));
					String SEND_NAME = BaseUtils.checkNull(map.get("SEND_NAME"));
					String SEND_NO = BaseUtils.checkNull(map.get("SEND_NO"));
					String[] detail={ORG_NAME,DEALER_CODE,DEALER_SHORTNAME,
							REPORT_NO,VIN,STATUS_NAME,CREATE_NAME,CREATE_DATE,
							AUDIT_NAME,AUDIT_DATE,PAY_TYPE,OPERATION,IS_SEND_TEMP,
							SEND_NAME,SEND_NO};
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出质保手册"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
