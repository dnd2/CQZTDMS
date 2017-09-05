package com.infodms.yxdms.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TtAsWrOldOutPartPO;
import com.infodms.dms.po.TtAsWrWinterMaintenDealerPO;
import com.infodms.dms.po.TtAsWrWinterMaintenPO;
import com.infodms.dms.po.TtAsWrWrlabinfoPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.yxdms.entity.maintain.TtAsEmergencyMaintainPO;
import com.infodms.yxdms.entity.maintain.TtAsKeepFitLabourPO;
import com.infodms.yxdms.entity.maintain.TtAsKeepFitPartPO;
import com.infodms.yxdms.entity.maintain.TtAsKeepFitTemplatePO;
import com.infodms.yxdms.entity.maintain.TtAsLabPartRelationPO;
import com.infodms.yxdms.entity.maintain.TtAsMailListPO;
import com.infodms.yxdms.entity.maintain.TtAsWrWinterPackagePO;
import com.infodms.yxdms.entity.maintain.TtAsYsqPartBackupPO;
import com.infodms.yxdms.entity.maintain.TtAsYsqPartDelLogPO;
import com.infodms.yxdms.entity.maintain.TtAsYsqPartPO;
import com.infodms.yxdms.entity.ysq.TtAsWrYsqPO;
import com.infodms.yxdms.utils.BaseUtils;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("rawtypes")
public class MainTainDAO extends IBaseDao{

	private static MainTainDAO dao = new MainTainDAO();
	public static final MainTainDAO getInstance(){
		dao = (dao==null)?new MainTainDAO():dao;
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> keepFitTemplateData(RequestWrapper request, Integer pageSize, Integer currPage) {
		String vin = DaoFactory.getParam(request, "vin");
		StringBuffer sb= new StringBuffer();
		if("".equals(vin)){
			sb.append("select t.*,vw.model_name from tt_AS_keep_Fit_Template t,vw_material_group_service vw where t.package_code = vw.package_code(+) and 1=1 and t.is_del=0 ");
			DaoFactory.getsql(sb, "t.keep_fit_no", DaoFactory.getParam(request, "keep_fit_no"), 2);
			DaoFactory.getsql(sb, "t.keep_fit_name", DaoFactory.getParam(request, "keep_fit_name"), 2);
			DaoFactory.getsql(sb, "t.status", DaoFactory.getParam(request, "status"), 1);
			DaoFactory.getsql(sb, "t.package_code", DaoFactory.getParam(request, "package_code"), 1);
			DaoFactory.getsql(sb, "vw.model_name", DaoFactory.getParam(request, "model_name"), 2);
			sb.append(" order by t.create_date desc");
		}else{
			Map<String,Object> map = this.pageQueryMap("select nvl(t.free_times,0) as free_times ,t.vin from  tm_vehicle t where t.vin='"+vin+"'", null, getFunName());
			BigDecimal object = (BigDecimal) map.get("FREE_TIMES");
			if(object.intValue()==0){//是否首保
				sb.append("select t.*\n" );
				sb.append("  from (select (select count(1)\n" );
				sb.append("                  from tt_as_repair_order o\n" );
				sb.append("                 where o.keep_fit_no = t.keep_fit_no\n" );
				sb.append("                   and o.vin = '"+vin+"') as countNum,\n" );
				sb.append("               t.*\n" );
				sb.append("          from tt_AS_keep_Fit_Template t\n" );
				sb.append("         where 1 = 1 and t.status=18041002 and t.package_code in \n" );//加上是否实销
				sb.append("           (select vw.package_code from vw_material_group_service vw,tm_vehicle v  where  vw.package_id = v.package_id and life_cycle =10321004  and v.vin='"+vin+"') and t.is_del = 0) t\n" );
				sb.append(" where 1 = 1 \n" );
				sb.append("   and countNum = 0 ");
				DaoFactory.getsql(sb, "t.keep_fit_no", DaoFactory.getParam(request, "keep_fit_no"), 2);
				DaoFactory.getsql(sb, "t.keep_fit_name", DaoFactory.getParam(request, "keep_fit_name"), 2);
				sb.append(" order by t.create_date desc");
			}else{
				sb.append("select t.*\n" );
				sb.append("  from (select (select count(1)\n" );
				sb.append("                  from tt_as_repair_order o\n" );
				sb.append("                 where o.keep_fit_no = t.keep_fit_no\n" );
				sb.append("                   and o.vin = '"+vin+"') as countNum,\n" );
				sb.append("               t.*\n" );
				sb.append("          from tt_AS_keep_Fit_Template t\n" );
				sb.append("         where 1 = 1 and t.status=18041002 and t.choose_type=93431002  and t.package_code in \n" );//加上是否实销
				sb.append("           (select vw.package_code from vw_material_group_service vw,tm_vehicle v  where  vw.package_id = v.package_id and life_cycle =10321004 and v.vin='"+vin+"') and t.is_del = 0) t\n" );
				sb.append(" where 1 = 1\n" );
				sb.append("   and countNum = 0 ");
				DaoFactory.getsql(sb, "t.keep_fit_no", DaoFactory.getParam(request, "keep_fit_no"), 2);
				DaoFactory.getsql(sb, "t.keep_fit_name", DaoFactory.getParam(request, "keep_fit_name"), 2);
				sb.append(" order by t.create_date desc");
			}
			
		}
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> addPart(RequestWrapper request,Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select a.*,\n" );
		sb.append("       CASE\n" );
		sb.append("         WHEN (trunc(sysdate) >= sp.valid_date and sp.history_flag = 1) or\n" );
		sb.append("              sp.history_flag = 0 THEN\n" );
		sb.append("          round(nvl(sp.sale_price1, 0) *\n" );
		sb.append("                (1 + 0 / 100),\n" );
		sb.append("                2)\n" );
		sb.append("         ELSE\n" );
		sb.append("          round(nvl(decode(sp.history_price,\n" );
		sb.append("                           null,\n" );
		sb.append("                           sp.sale_price1,\n" );
		sb.append("                           sp.history_price),\n" );
		sb.append("                    0) * (1 + 0 / 100),\n" );
		sb.append("                2)\n" );
		sb.append("       END claim_Price_param\n" );
		sb.append("  from TM_PT_PART_BASE a\n" );
		sb.append("  left join tt_part_sales_price sp\n" );
		sb.append("    on sp.part_id = a.part_id\n" );
		sb.append(" where 1 = 1 and a.is_del = 0\n" );
		sb.append("   AND A.Part_Is_Changhe = '95411001'");
		DaoFactory.getsql(sb, "a.part_code", DaoFactory.getParam(request, "part_code"), 2);
		DaoFactory.getsql(sb, "a.part_name", DaoFactory.getParam(request, "part_name"), 2);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> addLabour(RequestWrapper request,Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*,\n" );
		sb.append("       t.labour_quotiety * t.labour_hour as labour_fix,\n" );
		sb.append("       nvl((SELECT max(pr.labour_price) as labour_price\n" );
		sb.append("             from tt_as_wr_labour_price pr\n" );
		sb.append("             left outer join tm_dealer td\n" );
		sb.append("               on td.dealer_id = pr.dealer_id),\n" );
		sb.append("           0) AS parameter_value,\n" );
		sb.append("       b.approval_level\n" );
		sb.append("  from TT_AS_WR_WRLABINFO t\n" );
		sb.append("  left join tt_as_wr_authmonitorlab b\n" );
		sb.append("    on b.labour_operation_no = t.labour_code\n" );
		sb.append("   and b.model_group = t.wrgroup_id\n" );
		sb.append(" where 1 = 1\n" );
		sb.append("   and t.IS_DEL = 0\n" );
		sb.append("   and T.TREE_CODE = '3'\n" );
		DaoFactory.getsql(sb, "t.labour_code", DaoFactory.getParam(request, "labour_code"), 2);
		DaoFactory.getsql(sb, "t.cn_des", DaoFactory.getParam(request, "cn_des"), 2);
		sb.append(" order by t.labour_code");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	@SuppressWarnings("unchecked")
	public int addMainTainCommit(RequestWrapper request, AclUserBean loginUser) {
		int res=1;
		String id = DaoFactory.getParam(request, "id");
		String keepFitNo = DaoFactory.getParam(request, "keepFitNo");
		String keepFitName = DaoFactory.getParam(request, "keepFitName");
		String choose_type = DaoFactory.getParam(request, "choose_type");
		String package_code = DaoFactory.getParam(request, "package_code");
		try {
			if("".equals(id)){
				TtAsKeepFitTemplatePO t=new TtAsKeepFitTemplatePO();
				t.setCreateBy(String.valueOf(loginUser.getUserId()));
				t.setCreateDate(new Date());
				Long pkId = DaoFactory.getPkId();
				t.setId(pkId);
				t.setIsDel(0);
				t.setKeepFitNo(keepFitNo);
				t.setKeepFitName(keepFitName);
				t.setStatus(18041001);
				t.setChooseType(Integer.valueOf(choose_type));
				t.setPackageCode(package_code);
				double allAmount = insetKeepFit(request, t, pkId);
				t.setKeepFitAmount(allAmount);
				this.insert(t);
			}else{
				TtAsKeepFitTemplatePO t1=new TtAsKeepFitTemplatePO();
				Long pkid = Long.valueOf(id);
				t1.setId(pkid);
				TtAsKeepFitTemplatePO t2=new TtAsKeepFitTemplatePO();
				t2.setKeepFitNo(keepFitNo);
				t2.setKeepFitName(keepFitName);
				t2.setChooseType(Integer.valueOf(choose_type));
				t2.setPackageCode(package_code);
				this.delete("delete from Tt_As_Keep_Fit_Part t where t.keep_fit_id="+id, null);
				this.delete("delete from tt_AS_keep_Fit_labour t where t.keep_fit_id="+id, null);
				double allAmount = insetKeepFit(request, t2, pkid);
				t2.setKeepFitAmount(allAmount);
				this.update(t1, t2);
			}
		} catch (NumberFormatException e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	private double insetKeepFit(RequestWrapper request, TtAsKeepFitTemplatePO t,Long pkId) {
		//配件的 （三种类型）
		String[] part_id_2 = DaoFactory.getParams(request, "part_id_2");
		String[] part_code_2 = DaoFactory.getParams(request, "part_code_2");
		String[] part_name_2 = DaoFactory.getParams(request, "part_name_2");
		String[] part_quotiety_2 = DaoFactory.getParams(request, "part_quotiety_2");
		String[] claim_price_param_2 = DaoFactory.getParams(request, "claim_price_param_2");
		String[] part_amont_2 = DaoFactory.getParams(request, "part_amont_2");
		String[] pay_type_2 = DaoFactory.getParams(request, "pay_type_2");
		String[] part_use_type_2 = DaoFactory.getParams(request, "part_use_type_2");
		//工时的(三种类型)
		String[] labour_code_2 = DaoFactory.getParams(request, "labour_code_2");
		String[] cn_des_2 = DaoFactory.getParams(request, "cn_des_2");
		String[] labour_quotiety_2 = DaoFactory.getParams(request, "labour_quotiety_2");
		String[] parameter_value_2 = DaoFactory.getParams(request, "parameter_value_2");
		String[] labour_fix_2 = DaoFactory.getParams(request, "labour_fix_2");
		String[] pay_type_labour_2 = DaoFactory.getParams(request, "pay_type_labour_2");
		double allAmount=0.0d;
		try {
			double partAmont=0.0d;
			if(part_code_2!=null && part_code_2.length>0){
				for (int i = 0; i < part_code_2.length; i++) {
					TtAsKeepFitPartPO p=new TtAsKeepFitPartPO();
					p.setId(DaoFactory.getPkId());
					p.setKeepFitId(pkId);
					p.setPartCode(part_code_2[i]);
					p.setPartName(part_name_2[i]);
					p.setPartNum(Integer.valueOf(part_quotiety_2[i]));
					p.setPrice(Double.valueOf(claim_price_param_2[i]));
					Double part_Amont_2 = Double.valueOf(part_amont_2[i]);
					p.setAmount(part_Amont_2);
					p.setRealPartId(part_id_2[i]);
					p.setPartUseType(part_use_type_2[i]);
					p.setPayType(pay_type_2[i]);
					partAmont+=part_Amont_2;
					this.insert(p);
				}
			}
			double labourAmont=0.0d;
			if(labour_code_2!=null && labour_code_2.length>0){
				for (int i = 0; i < labour_code_2.length; i++) {
					TtAsKeepFitLabourPO l=new TtAsKeepFitLabourPO();
					l.setId(DaoFactory.getPkId());
					l.setKeepFitId(pkId);
					l.setLabourCode(labour_code_2[i]);
					l.setLabourName(cn_des_2[i]);
					l.setLabourNum(Double.valueOf(labour_quotiety_2[i]));
					l.setPayType(pay_type_labour_2[i]);
					l.setPrice(Double.valueOf(parameter_value_2[i]));
					Double labourAmont_2 = Double.valueOf(labour_fix_2[i]);
					l.setAmount(labourAmont_2);
					labourAmont+=labourAmont_2;
					this.insert(l);
				}
			}
			allAmount = 0.0d;
			allAmount=Arith.add(allAmount, partAmont);
			allAmount=Arith.add(allAmount, labourAmont);
			t.setKeepFitAmount(allAmount);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return allAmount;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> findLabPartRelation(String part_id,String part_code, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.* from tt_AS_lab_part_relation t where 1=1 and t.is_del=0 ");
		DaoFactory.getsql(sb,"t.part_id", part_id, 1);
		DaoFactory.getsql(sb,"t.part_code", part_code, 1);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> findaddLabour(Integer pageSize,Integer currPage, String labour_codes) {
		StringBuffer sb= new StringBuffer();
		sb.append("select distinct t.labour_code,t.cn_des,t.labour_hour\n" );
		sb.append("  from TT_AS_WR_WRLABINFO t\n" );
		sb.append(" where 1 = 1\n" );
		sb.append("   and t.IS_DEL = 0\n" );
		sb.append("   and T.TREE_CODE = '3'\n" );
		DaoFactory.getsql(sb, "t.labour_code", labour_codes, 8);
		sb.append(" order by t.labour_code");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public int insertRalation(String str, AclUserBean loginUser, String part_code, String part_id) {
		int res=1;
		try {
			String[] splits = str.split(";");
			for (String strs : splits) {
				String[] labs = strs.split(",");
				String lab_code = labs[0];
				String lab_cns = labs[1];
				TtAsLabPartRelationPO t=new TtAsLabPartRelationPO();
				t.setId(DaoFactory.getPkId());
				t.setPartId(BaseUtils.ConvertLong(part_id));
				t.setPartCode(part_code);
				t.setLabCode(lab_code);
				t.setLabName(new String(lab_cns.getBytes("ISO-8859-1"),"GBK"));
				t.setCreateDate(new Date());
				t.setCreateBy(BaseUtils.checkNull(loginUser.getUserId()));
				t.setIsDel(0);
				this.insert(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
			res=-1;
			throw new RuntimeException(e);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> ysqPartData(RequestWrapper request, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.* from tt_AS_ysq_part t where 1=1 ");
		DaoFactory.getsql(sb, "t.part_code", DaoFactory.getParam(request, "part_code"), 2);
		DaoFactory.getsql(sb, "t.part_name", DaoFactory.getParam(request, "part_name"), 2);
		DaoFactory.getsql(sb, "t.part_type", DaoFactory.getParam(request, "part_type"), 1);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> addYsqPartData(String part_name,String part_code, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select a.*,\n" );
		sb.append("       CASE\n" );
		sb.append("         WHEN (trunc(sysdate) >= sp.valid_date and sp.history_flag = 1) or\n" );
		sb.append("              sp.history_flag = 0 THEN\n" );
		sb.append("          round(nvl(sp.sale_price1, 0) *\n" );
		sb.append("                (1 + 0 / 100),\n" );
		sb.append("                2)\n" );
		sb.append("         ELSE\n" );
		sb.append("          round(nvl(decode(sp.history_price,\n" );
		sb.append("                           null,\n" );
		sb.append("                           sp.sale_price1,\n" );
		sb.append("                           sp.history_price),\n" );
		sb.append("                    0) * (1 + 0 / 100),\n" );
		sb.append("                2)\n" );
		sb.append("       END claim_Price_param\n" );
		sb.append("  from TM_PT_PART_BASE a\n" );
		sb.append("  left join tt_part_sales_price sp\n" );
		sb.append("    on sp.part_id = a.part_id\n" );
		sb.append(" where 1 = 1 and a.is_del = 0\n" );
		sb.append("   AND A.Part_Is_Changhe = '95411001' and a.part_id not in (select t.part_id from tt_AS_ysq_part t)\n");
		DaoFactory.getsql(sb, "a.part_code", part_code, 2);
		DaoFactory.getsql(sb, "a.part_name", part_name, 2);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public int insertYsqPart(RequestWrapper request, AclUserBean loginUser) {
		String str = DaoFactory.getParam(request, "str");
		String part_type = DaoFactory.getParam(request, "radio");
//		String is_returned = DaoFactory.getParam(request, "is_returned");
		int res=1;
		try {
			String[] splits = str.split(":");
			for (String strs : splits) {
				String[] labs = strs.split(";");
				String part_id = labs[0];
				String part_code = labs[1];
				String part_name = labs[2];
				TtAsYsqPartPO t=new TtAsYsqPartPO();
				t.setCreateBy(loginUser.getUserId());
				t.setCreateDate(new Date());
				t.setId(DaoFactory.getPkId());
				t.setPartId(BaseUtils.ConvertLong(part_id));
				t.setPartCode(part_code);
				t.setPartName(part_name);
				t.setPartType(Long.valueOf(part_type));//增加件类型
				t.setCreateDate(new Date());
//				t.setIsReturned(Long.parseLong(is_returned));//增加是否回运 2015-8-11
				this.insert(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
			res=-1;
			throw new RuntimeException(e);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> emergencyMainTain(RequestWrapper request, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.* from tt_as_emergency_MainTain t where 1=1 and t.is_delete is null ");
		DaoFactory.getsql(sb, "t.borrow_person", DaoFactory.getParam(request, "borrow_person"), 2);
		DaoFactory.getsql(sb, "t.consignee_person", DaoFactory.getParam(request, "consignee_person"), 2);
		DaoFactory.getsql(sb, "t.apply_dept", DaoFactory.getParam(request, "apply_dept"), 2);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public int addEmergency(RequestWrapper request, AclUserBean loginUser) {
		int res=1;
		try {
			String productAddr = DaoFactory.getParam(request, "productAddr");
			String applyDept = DaoFactory.getParam(request, "applyDept");
			String borrowPerson = DaoFactory.getParam(request, "borrowPerson");
			String borrowNo = DaoFactory.getParam(request, "borrowNo");
			String borrowDept = DaoFactory.getParam(request, "borrowDept");
			String consigneePerson = DaoFactory.getParam(request, "consigneePerson");
			String consigneePhone = DaoFactory.getParam(request, "consigneePhone");
			String consigneeAddr = DaoFactory.getParam(request, "consigneeAddr");
			String consigneeEmail = DaoFactory.getParam(request, "consigneeEmail");
			String borrowReason = DaoFactory.getParam(request, "borrowReason");
			String borrowPhone = DaoFactory.getParam(request, "borrowPhone");
			String requireDate = DaoFactory.getParam(request, "requireDate");
			Long pkId = DaoFactory.getPkId();
			TtAsEmergencyMaintainPO t=new TtAsEmergencyMaintainPO();
			t.setId(pkId);
			t.setProductAddr(productAddr);
			t.setApplyDept(applyDept);
			t.setBorrowPerson(borrowPerson);
			t.setBorrowNo(borrowNo);
			t.setBorrowDept(borrowDept);
			t.setBorrowReason(borrowReason);
			t.setConsigneeAddr(consigneeAddr);
			t.setConsigneeEmail(consigneeEmail);
			t.setConsigneePerson(consigneePerson);
			t.setConsigneePhone(consigneePhone);
			t.setConsigneePerson(consigneePerson);
			t.setCreateBy(loginUser.getUserId());
			t.setCreateDate(new Date());
			t.setBorrowPhone(borrowPhone);
			t.setNextTime(new Date());
			this.insert(t);
		} catch (Exception e) {
			e.printStackTrace();
			res=-1;
			throw new RuntimeException(e);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> mailList(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*\n" );
		sb.append("  from tt_AS_mail_List t\n" );
		sb.append(" where 1 = 1\n" );
		if(null!=loginUser.getDealerId()){//服务站
			sb.append("   and t.area in\n" );
			sb.append("       (select v.root_org_name\n" );
			sb.append("          from tm_dealer tm, vw_org_dealer_service v\n" );
			sb.append("         where tm.dealer_id = v.dealer_id and tm.dealer_id ='"+loginUser.getDealerId()+"') \n");
		}
		DaoFactory.getsql(sb, "t.user_name", DaoFactory.getParam(request, "user_name"), 2);
		DaoFactory.getsql(sb, "t.user_phone", DaoFactory.getParam(request, "user_phone"), 2);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public int addMailListSure(RequestWrapper request, AclUserBean loginUser) {
		int res=1;
		try {
			String area = DaoFactory.getParam(request, "area");
			String user_name = DaoFactory.getParam(request, "user_name");
			String user_phone = DaoFactory.getParam(request, "user_phone");
			String position_name= DaoFactory.getParam(request, "position_name");
			String position_duty= DaoFactory.getParam(request, "position_duty");
			Long pkId = DaoFactory.getPkId();
			TtAsMailListPO t=new TtAsMailListPO();
			t.setId(pkId);
			t.setCreateBy(loginUser.getUserId());
			t.setCreateDate(new Date());
			t.setArea(area);
			t.setUserName(user_name);
			t.setUserPhone(user_phone);
			t.setPositionName(position_name);
			t.setPositionDuty(position_duty);
			this.insert(t);
		} catch (Exception e) {
			e.printStackTrace();
			res=-1;
			throw new RuntimeException(e);
		}
		return res;
	}
	//=========================2015.4.22 lj
	@SuppressWarnings("unchecked")
	public void insertlabpartRelation(RequestWrapper request,AclUserBean loginUser) {
		String strCount = CommonUtils.checkNull(request.getParamValue("count")); //导入数据总条数
		int count = Integer.parseInt(strCount);

		for(int i=1;i<count; i++){
			String part_code = CommonUtils.checkNull(request.getParamValue("cxz"+i));//配件代码
			String labour_code = CommonUtils.checkNull(request.getParamValue("gsdm"+i));//工时代码
			
			StringBuffer sq = new StringBuffer();
			sq.append("select * from tt_AS_lab_part_relation where 1=1 ");
			DaoFactory.getsql(sq, "part_code", part_code, 1);
			DaoFactory.getsql(sq, "lab_code", labour_code, 1);
			List<TtAsLabPartRelationPO> li = this.select(TtAsLabPartRelationPO.class, sq.toString(), null);
		   if (li.size()<1) {
			StringBuffer sb = new StringBuffer();
			sb.append("select part_id from TM_PT_PART_BASE where 1=1 ");
			DaoFactory.getsql(sb, "part_code", part_code, 1);
			List<TmPtPartBasePO> list = this.select(TmPtPartBasePO.class, sb.toString(), null);
			Long part_id= list.get(0).getPartId();
			
			StringBuffer sql = new StringBuffer();
			sql.append("select CN_DES from TT_AS_WR_WRLABINFO where 1=1 ");
			DaoFactory.getsql(sql, "labour_code", labour_code, 1);
			List<TtAsWrWrlabinfoPO> list1 = this.select(TtAsWrWrlabinfoPO.class, sql.toString(), null);
			String CnDes= list1.get(0).getCnDes();
			
			TtAsLabPartRelationPO po = new TtAsLabPartRelationPO();
			   po.setId(DaoFactory.getPkId());
			   po.setCreateBy(BaseUtils.checkNull(loginUser.getUserId()));
			   po.setCreateDate(new Date());
			   po.setIsDel(0);
			   po.setLabCode(labour_code);
			   po.setLabName(CnDes);
			   po.setPartCode(part_code);
			   po.setPartId(part_id);
				   this.insert(po);
			    }
		}
	}

	@SuppressWarnings("unchecked")
	public List findTtasrolabour(String trim) {
		StringBuffer sb = new StringBuffer();
		sb.append( "select * from TM_PT_PART_BASE  tt where 1=1 ");
		DaoFactory.getsql(sb, "tt.part_code", trim, 1);
		return this.select(TmPtPartBasePO.class, sb.toString(), null);
	}
	@SuppressWarnings("unchecked")
	public List findTtaswrwrlabinfo(String trim) {
		StringBuffer sb = new StringBuffer();
		sb.append( "select * from TT_AS_WR_WRLABINFO  tt where 1=1 ");
		DaoFactory.getsql(sb, "tt.labour_code", trim, 1);
		return this.select(TtAsWrWrlabinfoPO.class, sb.toString(), null);
	}
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> queryPartCode(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sqlStr = new StringBuffer();
		String PART_CODE = request.getParamValue("PART_CODE");
		String PART_NAME = request.getParamValue("PART_NAME");
		String activity_type = request.getParamValue("activity_type");
		sqlStr.append("select t.* from TT_PART_SPECIAL_DEFINE t where t.isneed_flag=10041001 and t.part_type=95621001 and t.start_date<=sysdate and t.end_date>=sysdate  and t.status=1 and t.state=10011001 ");
		if(Utility.testString(activity_type)){
			sqlStr.append(" and t.activity_type="+activity_type);
		}
	    if(Utility.testString(PART_CODE)){
	    	sqlStr.append(" and t.part_code like '%"+PART_CODE+"%'");
		}
	    if(Utility.testString(PART_NAME)){
	    	sqlStr.append(" and t.part_name like '%"+PART_NAME+"%'");
	    }
	    return pageQuery(sqlStr.toString(), null,getFunName(), pageSize, currPage);
	}

	@SuppressWarnings("unchecked")
	public int delYsqPart1(RequestWrapper request, AclUserBean loginUser) {
		int res=-1;
		TtAsYsqPartPO p =new TtAsYsqPartPO();
		String id =DaoFactory.getParam(request, "id");
		p.setId(Long.valueOf(id));
		List<TtAsYsqPartPO> select = this.select(p);
		p = select.get(0);
		String partCode = p.getPartCode();
		StringBuffer sql= new StringBuffer();
		sql.append("select p.part_code, y.part_type,p.part_id\n" );
		sql.append("  from tt_as_wr_partsitem p, tt_as_wr_application a, tt_as_ysq_part y\n" );
		sql.append(" where p.part_code = y.part_code(+)\n" );
		sql.append("   and p.id = a.id(+)\n" );
		sql.append("   and a.ysq_no is not null\n" );
		sql.append("   and a.status in (10791004,10791005,10791008)\n" );
		sql.append("   and y.part_type = 72311002");
		DaoFactory.getsql(sql, "p.part_code",partCode , 1);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,getFunName());
		if(DaoFactory.checkListNull(list)){
			res=-1;
		}else{
			TtAsYsqPartBackupPO backupPO = new TtAsYsqPartBackupPO();
			backupPO.setId(p.getId());
			backupPO.setCreateBy(p.getCreateBy());
			backupPO.setCreateDate(p.getCreateDate());
			backupPO.setPartCode(p.getPartCode());
			backupPO.setPartId(p.getPartId());
			backupPO.setPartName(p.getPartName());
			backupPO.setPartType(p.getPartType());
			this.insert(backupPO);///插入备份表
			TtAsYsqPartDelLogPO logPO = new TtAsYsqPartDelLogPO();
			logPO.setId(DaoFactory.getPkId());
			logPO.setYsqId(p.getId());
			logPO.setCreateBy(p.getCreateBy());
			logPO.setCreateDate(p.getCreateDate());
			logPO.setPartCode(p.getPartCode());
			logPO.setPartId(p.getPartId());
			logPO.setPartName(p.getPartName());
			logPO.setPartType(p.getPartType());
			logPO.setDelBy(loginUser.getUserId());
			logPO.setDelDate(new Date());
			this.insert(logPO);//插入日志表
			this.delete(p);
			res=1;
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public int insertoldoutpart(RequestWrapper request, AclUserBean loginUser) {
		String[] str = DaoFactory.getParam(request, "str").split(",");
		String out_by = DaoFactory.getParam(request, "out_by");
		String range_no = DaoFactory.getParam(request, "range_no");
		String out_time = DaoFactory.getParam(request, "out_time");
		String supply_code = DaoFactory.getParam(request, "supply_code");
		String supply_name = DaoFactory.getParam(request, "supply_name");
		String out_type = DaoFactory.getParam(request, "out_type");
		StringBuffer sql= new StringBuffer();
		sql.append("select distinct a.*,\n" );
		sql.append("                d.part_code,\n" );
		sql.append("                vd.maker_name,\n" );
		sql.append("                vd.maker_code,\n" );
		sql.append("                de.part_code  as code,\n" );
		sql.append("                de.part_cname as name\n" );
		sql.append("  from tt_as_wr_application   a,\n" );
		sql.append("       tt_as_wr_partsitem     d,\n" );
		sql.append("       tt_part_define         de,\n" );
		sql.append("       tt_part_maker_define   vd,\n" );
		sql.append("       TT_PART_MAKER_RELATION p\n" );
		sql.append(" where a.id = d.id(+)\n" );
		sql.append("   and d.part_code = de.part_code(+)\n" );
		sql.append("   and de.part_id = p.part_id(+)\n" );
		sql.append("   and P.MAKER_ID = VD.Maker_Id(+)\n" );
		DaoFactory.getsql(sql,"a.id", DaoFactory.getSqlByarrIn(str),6);
		DaoFactory.getsql(sql, "vd.maker_code", DaoFactory.getParam(request, "supply_code"), 1);
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, getFunName());
        TtAsWrOldOutPartPO outPartPO =null;
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (int i = 0; i < list.size(); i++) {
        	outPartPO=new TtAsWrOldOutPartPO();	
        	 outPartPO.setId(DaoFactory.getPkId());
        	 outPartPO.setClaimNo(list.get(i).get("CLAIM_NO").toString());
        	 outPartPO.setSupplyCode(list.get(i).get("MAKER_CODE").toString());
        	 outPartPO.setSupplyName(list.get(i).get("MAKER_NAME").toString());
        	 outPartPO.setOutType(Integer.valueOf(out_type));
        	 try {
        		 if (BaseUtils.notNull(out_time)) {
        			 Date date = formatter.parse(out_time);
        			 outPartPO.setOutDate(date);
        			 outPartPO.setCreateDate(date);
				  }
			} catch (Exception e) {
				e.printStackTrace();
			}
        	 outPartPO.setOutBy(Long.valueOf(out_by));
        	 outPartPO.setCreateBy(Long.valueOf(out_by));
        	 outPartPO.setOutNo(range_no);
        	 outPartPO.setRangeNo(range_no);
        	 outPartPO.setOutAmout(0);
        	 outPartPO.setOutPartCode(list.get(i).get("CODE").toString());
        	 outPartPO.setOutPartName(list.get(i).get("NAME").toString());
        	 outPartPO.setYieldly(Constant.PART_IS_CHANGHE_01);
        	 outPartPO.setOutPartType(1);
        	 outPartPO.setOutFlag(0);
        	 outPartPO.setHandMark(1);//1表示手工添加
        	 this.insert(outPartPO);
		}
		return 1;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> queryWinterMaintenancelist(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sb= new StringBuffer();
		
		sb.append("select tt.id,\n" );
		sb.append("       tt.amount,\n" );
		sb.append("       tt.start_date,\n" );
		sb.append("       tt.end_date,\n" );
		sb.append("       tt.create_by,\n" );
		sb.append("       tt.create_date,\n" );
		sb.append("       (select g.group_name from tm_vhcl_material_group g where  g.group_code=tt.model_code and g.group_level = 3 and g.status=10011001 ) group_name ,\n" );
		sb.append("       tt.status \n" );
		sb.append("  from TT_AS_WR_WINTER_MAINTEN tt  \n" );
		sb.append(" 	where 1=1");
		DaoFactory.getsql(sb, "tt.start_date", DaoFactory.getParam(request, "start_date"), 3);
		DaoFactory.getsql(sb, "tt.end_date", DaoFactory.getParam(request, "end_date"), 4);
		DaoFactory.getsql(sb, "tt.amount", DaoFactory.getParam(request, "amount"), 1);
		DaoFactory.getsql(sb, "tt.model_code", DaoFactory.getParam(request, "model"), 1);
		DaoFactory.getsql(sb, "tt.status", DaoFactory.getParam(request, "status"), 1);
		sb.append(" 	order by  tt.create_date desc");
		return this.pageQuery(sb.toString(), null, getFunName(), pageSize, currPage);
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> queryDealer(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage) {
		String dealerCode = request.getParamValue("DEALER_CODE");
		PageResult<Map<String, Object>> list=null;
	    StringBuffer sb= new StringBuffer();
		sb.append("select tm.dealer_id,\n" );
		sb.append("       tm.dealer_code,\n" );
		sb.append("       tm.dealer_name,\n" );
		sb.append("       vod.root_org_name,\n" );
		sb.append("       vod.org_name\n" );
		sb.append("  from tm_dealer tm\n" );
		sb.append("  left join vw_org_dealer_service vod\n" );
		sb.append("    on tm.dealer_id = vod.dealer_id\n" );
		sb.append(" where 1=1 \n");
		if (null==dealerCode || "".equals(dealerCode)) {
			DaoFactory.getsql(sb, "tm.dealer_code", "-1", 6);
		}else if (dealerCode!=null) {
			String[] code = dealerCode.split(",");
		    DaoFactory.getsql(sb, "tm.dealer_code", DaoFactory.getSqlByarrIn(code), 6);
		}
		list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		
		return list;
	}

	@SuppressWarnings("unchecked")
	public String insertWinterMaintenDealer(RequestWrapper request,AclUserBean loginUser) {
		String str ="";
		String amount = request.getParamValue("amount");
		String startDate = request.getParamValue("start_date");
		String endDate = request.getParamValue("end_date");
		String dealerId = request.getParamValue("dealerId");
		String status = request.getParamValue("status");
		String model_code = request.getParamValue("model_code");
		String Configuration_code = request.getParamValue("Configuration_code");//配置
		try {
			TtAsWrWinterMaintenPO po = new TtAsWrWinterMaintenPO();
			TtAsWrWinterMaintenDealerPO po2 = new TtAsWrWinterMaintenDealerPO();
			TtAsWrWinterPackagePO package1 = new TtAsWrWinterPackagePO();
			Long pkId = DaoFactory.getPkId();
			po.setId(pkId);
			po.setAmount(Double.valueOf(amount));
			po.setStartDate(Utility.getDate(startDate, 1));
			po.setEndDate(Utility.getDate(endDate, 1));
			po.setCreateDate(new Date());
			po.setModelCode(model_code);
			if ("10681001".equals(status)) {//未发布
				po.setStatus(Constant.SERVICEACTIVITY_STATUS_01);
			}else if ("10681002".equals(status)) {//已发布
				po.setStatus(Constant.SERVICEACTIVITY_STATUS_02);
				
			}
			po.setCreateBy(loginUser.getName());
			po2.setId(pkId);
			String[] code = dealerId.split(",");
			String[] Configurationcode = Configuration_code.split(",");
			package1.setWintweId(pkId);//冬季保养主键
			TtAsWrWinterPackagePO package2 = new TtAsWrWinterPackagePO();//用于判断是否已经添加
			for (int i = 0; i < code.length; i++) {
				po2.setDealerId(Long.parseLong(code[i]));
				package1.setDealerId(Long.parseLong(code[i]));//配置表经销商id
				for (int j = 0; j < Configurationcode.length; j++) {
					package1.setPackageCode(Configurationcode[j]);//配置
					package2.setPackageCode(Configurationcode[j]);
					package2.setDealerId(Long.parseLong(code[i]));
					List<Map<String, Object>> list = this.select(package2);//查询该配置是否已添加
					if (DaoFactory.checkListNull(list)) {
						str+=Configurationcode[j]+"  选择的配置对应的服务站已经维护冬季保养，请重新选择保存！";
						return str;
					}else {
						dao.insert(package1);
					}
					dao.insert(po2);
				}
			}
			dao.insert(po);//插入冬季保养模板
			str+="success";
		} catch (Exception e) {
			str+="添加失败，请联系管理员！";
		}
		return str;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> queryWinterDetail(RequestWrapper request, AclUserBean loginUser) {
		StringBuffer sb= new StringBuffer();
			sb.append("select tt.id,\n" );
			sb.append("       tt.amount,\n" );
			sb.append("       tt.start_date,\n" );
			sb.append("       tt.end_date,\n" );
			sb.append("       tt.create_by,\n" );
			sb.append("       tt.create_date,\n" );
			sb.append("       tt.status,tm.dealer_id,\n" );
			sb.append("       tm.dealer_code,\n" );
			sb.append("       vw.root_org_name,\n" );
			sb.append("       vw.org_name,\n" );
			sb.append("      ( select distinct t.group_name from tm_vhcl_material_group t where t.group_code = tt.model_code and  t.group_level = 3 and t.status=10011001 ) group_name ,\n" );
			sb.append("       tm.dealer_name\n" );
			sb.append("  from TT_AS_WR_WINTER_MAINTEN tt\n" );
			sb.append("  left join TT_AS_WR_WINTER_MAINTEN_DEALER ttd\n" );
			sb.append(" on tt.id = ttd.id \n");
			sb.append("  left join vw_org_dealer_service vw \n" );
			sb.append(" on ttd.dealer_id = vw.dealer_id \n");
			sb.append("  left join tm_dealer tm\n" );
			sb.append(" on ttd.dealer_id = tm.dealer_id where 1= 1");
			DaoFactory.getsql(sb, "tt.id",DaoFactory.getParam(request, "id"), 1);
			PageResult<Map<String, Object>> resList =  this.pageQuery(sb.toString(), null,getFunName(), Constant.PAGE_SIZE, 1);;
			return resList;
	}

	@SuppressWarnings("unchecked")
	public String UpdateWinterMainten(RequestWrapper request, AclUserBean loginUser) {
		String res = "";
		try {
			String id =DaoFactory.getParam(request, "id");
			String endDate = DaoFactory.getParam(request,"end_date");
			String startDate = request.getParamValue("start_date");
			String amount = DaoFactory.getParam(request,"amount");
			String dealerId = request.getParamValue("dealerId");//经销商id
			String[] code = dealerId.split(",");
			String Configuration_code = request.getParamValue("Configuration_code");//配置
			String[] Configurationcode = Configuration_code.split(",");//配置
			TtAsWrWinterPackagePO package2 = new TtAsWrWinterPackagePO();//用于判断是否已经添加
			for (int i = 0; i < code.length; i++) {
				package2.setDealerId(Long.valueOf(code[i]));
				for (int j = 0; j < Configurationcode.length; j++) {
					package2.setPackageCode(Configurationcode[j]);
					List<Map<String, Object>> list = this.select(package2);//查询该配置是否已添加
					if (DaoFactory.checkListNull(list)) {
						res+=Configurationcode[j]+"  选择的配置对应的服务站已经维护冬季保养，请重新选择保存！";
						return res;
					}
				}
			}
			TtAsWrWinterMaintenPO po = new TtAsWrWinterMaintenPO();
			TtAsWrWinterMaintenPO po2 = new TtAsWrWinterMaintenPO();
			TtAsWrWinterMaintenDealerPO pod = new TtAsWrWinterMaintenDealerPO();
			TtAsWrWinterPackagePO package1 = new TtAsWrWinterPackagePO();
			po.setId(Long.parseLong(id));
			po2.setAmount(Double.valueOf(amount));
			po2.setStartDate(Utility.getDate(startDate, 1));
			po2.setEndDate(Utility.getDate(endDate, 1));
			
		    dao.update(po, po2);
			pod.setId(Long.parseLong(id));
			dao.delete("delete from TT_AS_WR_WINTER_MAINTEN_DEALER where id=" + id, null);
			dao.delete("delete from TT_AS_WR_WINTER_PACKAGE where wintwe_id=" + id, null);
			package1.setWintweId(Long.valueOf(id));//冬季保养主键
			for (int i=0;i<code.length;i++) {
				pod.setDealerId(Long.parseLong(code[i]));
				dao.insert(pod);
				package1.setDealerId(Long.parseLong(code[i]));//配置表经销商id
				for (int j = 0; j < Configurationcode.length; j++) {
					package1.setPackageCode(Configurationcode[j]);//配置
					dao.insert(package1);
				}
			}
			res="1";
		} catch (Exception e) {
			res="修改失败，请联系管理员！";
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> winterMaintenView(RequestWrapper request,AclUserBean loginUser) {
		StringBuffer sb= new StringBuffer();
		sb.append("select tt.id,\n" );
		sb.append("       tt.amount,\n" );
		sb.append("       tt.start_date,\n" );
		sb.append("       tt.end_date,\n" );
		sb.append("       tt.create_by,\n" );
		sb.append("       tt.create_date,\n" );
		sb.append("       tt.status,\n" );
		sb.append("       tm.dealer_code,\n" );
		sb.append("       vw.root_org_name,\n" );
		sb.append("       vw.org_name,\n" );
		sb.append("       ( select distinct t.group_name from tm_vhcl_material_group t where t.group_code = tt.model_code and  t.group_level = 3 and t.status=10011001 ) group_name  ,\n" );
		sb.append("       tm.dealer_name\n" );
		sb.append("  from TT_AS_WR_WINTER_MAINTEN tt\n" );
		sb.append("  left join TT_AS_WR_WINTER_MAINTEN_DEALER ttd\n" );
		sb.append(" on tt.id = ttd.id \n");
		sb.append("  left join vw_org_dealer_service vw \n" );
		sb.append(" on ttd.dealer_id = vw.dealer_id \n");
		sb.append("  left join tm_dealer tm\n" );
		sb.append(" on ttd.dealer_id = tm.dealer_id where 1=1 " );
		DaoFactory.getsql(sb, "tt.id", DaoFactory.getParam(request, "id"), 1);
		List<Map<String, Object>> resList = pageQuery(sb.toString(), null,getFunName());
		
		return resList;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> querychoosepageCode(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
	  StringBuffer sb = new StringBuffer();
	  sb.append("select g.package_code,g.package_name from vw_material_group g group by g.package_code,g.package_name");
	  DaoFactory.getsql(sb, "g.package_code", DaoFactory.getParam(request, "package_code"), 2);
	  DaoFactory.getsql(sb, "g.package_name", DaoFactory.getParam(request, "package_name"), 2);
	  return this.pageQuery(sb.toString(), null, getFunName(), pageSize, currPage);
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> queryConfiguration(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sql= new StringBuffer();
		sql.append("select * from vw_material_group g where 1=1 ");
		DaoFactory.getsql(sql, "g.model_code", DaoFactory.getParam(request, "model_code"), 1);
		DaoFactory.getsql(sql, "g.package_code", DaoFactory.getParam(request, "package_code"), 2);
		DaoFactory.getsql(sql, "g.package_name", DaoFactory.getParam(request, "package_name"), 2);
		return   this.pageQuery(sql.toString(), null, getFunName(), pageSize, currPage);
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> queryConfigurationBycode(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sql= new StringBuffer();
		sql.append("select * from vw_material_group g where 1=1 ");
		DaoFactory.getsql(sql, "g.model_code", DaoFactory.getParam(request, "model_code"), 1);
		DaoFactory.getsql(sql, "g.package_code", DaoFactory.getParam(request, "package_code"), 6);
		return   this.pageQuery(sql.toString(), null, getFunName(), pageSize, currPage);
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> queryConfigurationById(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT p.*, tm.dealer_shortname, vw.PACKAGE_NAME\n" );
		sql.append("  FROM TT_AS_WR_WINTER_PACKAGE p, tm_dealer tm, vw_material_group vw\n" );
		sql.append(" where p.dealer_id = tm.dealer_id\n" );
		sql.append("   and p.package_code = vw.PACKAGE_CODE");
		DaoFactory.getsql(sql, "p.wintwe_id", DaoFactory.getParam(request, "id"), 1);
		return this.pageQuery(sql.toString(), null, getFunName(), pageSize, currPage);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryWinterById(String str,AclUserBean loginUser) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT distinct p.package_code, vw.PACKAGE_NAME\n" );
		sql.append("  FROM TT_AS_WR_WINTER_PACKAGE p, tm_dealer tm, vw_material_group vw\n" );
		sql.append(" where p.dealer_id = tm.dealer_id\n" );
		sql.append("   and p.package_code = vw.PACKAGE_CODE");
		DaoFactory.getsql(sql, "p.wintwe_id", str, 1);
		return this.pageQuery(sql.toString(), null, getFunName());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryWinterById(RequestWrapper request,AclUserBean loginUser) {
		StringBuffer sb= new StringBuffer();
		sb.append("select \n" );
		sb.append("       tm.dealer_code,tm.dealer_id\n" );
		sb.append("  from TT_AS_WR_WINTER_MAINTEN tt\n" );
		sb.append("  left join TT_AS_WR_WINTER_MAINTEN_DEALER ttd\n" );
		sb.append(" on tt.id = ttd.id \n");
		sb.append("  left join vw_org_dealer_service vw \n" );
		sb.append(" on ttd.dealer_id = vw.dealer_id \n");
		sb.append("  left join tm_dealer tm\n" );
		sb.append(" on ttd.dealer_id = tm.dealer_id where 1= 1");
		DaoFactory.getsql(sb, "tt.id",DaoFactory.getParam(request, "id"), 1);
		sb.append(" group by  tm.dealer_code,tm.dealer_id");
		List<Map<String, Object>> resList =  this.pageQuery(sb.toString(), null, getFunName());
		return resList;
	}

}
