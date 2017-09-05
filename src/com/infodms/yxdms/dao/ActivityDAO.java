package com.infodms.yxdms.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.yxdms.entity.activity.TtAsWrActivityTempletAccPO;
import com.infodms.yxdms.entity.activity.TtAsWrActivityTempletComPO;
import com.infodms.yxdms.entity.activity.TtAsWrActivityTempletLabPO;
import com.infodms.yxdms.entity.activity.TtAsWrActivityTempletPO;
import com.infodms.yxdms.entity.activity.TtAsWrActivityTempletPartPO;
import com.infodms.yxdms.utils.BaseUtils;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("rawtypes")
public class ActivityDAO extends IBaseDao{

	private static final ActivityDAO dao = new ActivityDAO();
	public static final ActivityDAO getInstance(){
		if (dao == null) {
			return new ActivityDAO();
		}
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> ActivityVinManageData(RequestWrapper request, Integer pageSize, Integer currPage) {
		String activity_code = DaoFactory.getParam(request, "activity_code");
		String subjectId = DaoFactory.getParam(request, "subjectId");
		String status = DaoFactory.getParam(request, "status");
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT A.ACTIVITY_ID,\n" );
		sb.append("       A.ACTIVITY_NAME,\n" );
		sb.append("       A.ACTIVITY_CODE,\n" );
		sb.append("       A.STATUS,\n" );
		sb.append("       B.SUBJECT_NO,\n" );
		sb.append("       B.SUBJECT_ID,\n" );
		sb.append("       B.SUBJECT_NAME\n" );
		sb.append("  FROM TT_AS_ACTIVITY A, TT_AS_ACTIVITY_SUBJECT B\n" );
		sb.append(" where A.SUBJECT_ID = B.SUBJECT_ID\n" );
		sb.append("   and 1 = 1\n" );
		sb.append("   AND A.IS_DEL = 0\n" );
		DaoFactory.getsql(sb, "A.ACTIVITY_CODE", activity_code, 1);
		DaoFactory.getsql(sb, "B.SUBJECT_ID", subjectId, 1);
		DaoFactory.getsql(sb, "A.STATUS", status, 1);
		sb.append(" ORDER BY A.ACTIVITY_ID desc");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> ActivityVinData(RequestWrapper request, Integer pageSize, Integer currPage) {
		String dealerCode = DaoFactory.getParam(request, "dealerCode");
		String root_org_id = DaoFactory.getParam(request, "root_org_id");
		String repair_status = DaoFactory.getParam(request, "repair_status");
		String activity_id = DaoFactory.getParam(request, "activity_id");
		String dealerCodeReal = DaoFactory.getParam(request, "dealerCodeReal");
		String vin = DaoFactory.getParam(request, "vin");
		StringBuffer sb= new StringBuffer();
		sb.append("select c.*\n" );
		sb.append("  from (select a.vin,a.activity_id,\n" );
		sb.append("               b.dealer_code,\n" );
		sb.append("               (select tm.dealer_shortname\n" );
		sb.append("                  from tm_dealer tm\n" );
		sb.append("                 where tm.dealer_code = b.dealer_code) as dealer_shortname,\n" );
		sb.append("               (select v.root_org_id\n" );
		sb.append("                  from vw_org_dealer_service v\n" );
		sb.append("                 where v.dealer_id =\n" );
		sb.append("                       (select tm.dealer_id\n" );
		sb.append("                          from tm_dealer tm\n" );
		sb.append("                         where tm.dealer_code = b.dealer_code)) as root_org_id,\n" );
		sb.append("               (select v.root_org_name\n" );
		sb.append("                  from vw_org_dealer_service v\n" );
		sb.append("                 where v.dealer_id =\n" );
		sb.append("                       (select tm.dealer_id\n" );
		sb.append("                          from tm_dealer tm\n" );
		sb.append("                         where tm.dealer_code = b.dealer_code)) as root_org_name,\n" );
		sb.append("               (CASE\n" );
		sb.append("                 WHEN (select count(*)\n" );
		sb.append("                         from tm_vehicle t\n" );
		sb.append("                        where life_cycle = 10321004\n" );
		sb.append("                          and vehicle_id =\n" );
		sb.append("                              (select vehicle_id\n" );
		sb.append("                                 from tt_dealer_actual_sales tdac\n" );
		sb.append("                                where tdac.vehicle_id = t.vehicle_id\n" );
		sb.append("                                  and tdac.is_return = 10041002)\n" );
		sb.append("                          and vin = a.vin) > 0 THEN\n" );
		sb.append("                  10491001\n" );
		sb.append("                 ELSE\n" );
		sb.append("                  10491002\n" );
		sb.append("               END) SALE_STATUS,\n" );
		sb.append("               (CASE\n" );
		sb.append("                 WHEN (SELECT count(*)\n" );
		sb.append("                         FROM Tt_As_Repair_Order TVRO\n" );
		sb.append("                        WHERE TVRO.VIN = a.vin\n" );
		sb.append("                          and TVRO.Cam_Code =\n" );
		sb.append("                              (SELECT TAA.ACTIVITY_CODE\n" );
		sb.append("                                 FROM TT_AS_ACTIVITY TAA\n" );
		sb.append("                                WHERE TAA.ACTIVITY_ID = a.activity_id)) > 0 THEN\n" );
		sb.append("                  10481001\n" );
		sb.append("                 ELSE\n" );
		sb.append("                  10481002\n" );
		sb.append("               END) REPAIR_STATUS,\n" );
		sb.append("               (select max(o.dealer_code)\n" );
		sb.append("                  from Tt_As_Repair_Order o\n" );
		sb.append("                 where o.repair_type_code = 11441005\n" );
		sb.append("                   and o.vin = a.vin\n" );
		sb.append("                   and o.cam_code = (SELECT TAA.ACTIVITY_CODE FROM TT_AS_ACTIVITY TAA WHERE TAA.ACTIVITY_ID = a.activity_id)) as dealer_code_real,\n" );
		sb.append("               (select max(o.dealer_shortname)\n" );
		sb.append("                  from Tt_As_Repair_Order o\n" );
		sb.append("                 where o.repair_type_code = 11441005\n" );
		sb.append("                   and o.vin = a.vin\n" );
		sb.append("                   and o.cam_code = (SELECT TAA.ACTIVITY_CODE FROM TT_AS_ACTIVITY TAA WHERE TAA.ACTIVITY_ID = a.activity_id)) as dealer_shortname_real\n" );
		sb.append("          from TT_AS_ACTIVITY_VEHICLE a, Tt_As_Activity_Relation_Code b\n" );
		sb.append("         where 1 = 1\n" );
		sb.append("           and a.activity_id = b.activity_id(+)\n" );
		sb.append("           and a.vin = b.vin(+)) c \n" );
		sb.append(" where 1 = 1");
		DaoFactory.getsql(sb, "c.dealer_code", dealerCode, 6);
		DaoFactory.getsql(sb, "c.root_org_id", root_org_id, 1);
		DaoFactory.getsql(sb, "c.repair_status", repair_status, 1);
		DaoFactory.getsql(sb, "c.activity_id", activity_id, 1);
		DaoFactory.getsql(sb, "c.dealer_code_real", dealerCodeReal, 6);
		DaoFactory.getsql(sb, "c.vin", vin, 1);
		sb.append(" order by c.activity_id desc ");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public int bntDelAll(RequestWrapper request) {
		String ids = DaoFactory.getParam(request, "ids");
		int res=1;
		try {
			String[] params = StringUtils.split(ids, ";");
			for (String param : params) {
				String[] activityVin = StringUtils.split(param, ",");
				String actiityId = activityVin[0];
				String vin = activityVin[1];
				this.delete("delete TT_AS_ACTIVITY_VEHICLE aa where aa.vin='"+vin+"' and aa.activity_id="+actiityId, null);
				this.delete("delete Tt_As_Activity_Relation_Code aa where aa.vin='"+vin+"' and aa.activity_id="+actiityId, null);
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public void expotActivityVinData(ActionContext act,PageResult<Map<String, Object>> list) {
		try {
			String[] head={"VIN","关联商家代码","关联商家简称","大区","是否销售","是否维修","实际维修商家代码","实际维修商家简称"};
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(DaoFactory.checkListNull(records)){
				for (Map<String, Object> map : records) {
					String VIN = BaseUtils.checkNull(map.get("VIN"));
					String DEALER_CODE = BaseUtils.checkNull(map.get("DEALER_CODE"));
					String DEALER_SHORTNAME = BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					String ROOT_ORG_NAME = BaseUtils.checkNull(map.get("ROOT_ORG_NAME"));
					
					String saleStatus = BaseUtils.checkNull(map.get("SALE_STATUS"));
					String SALE_STATUS="";
					if("10491001".equals(saleStatus)){
						SALE_STATUS="已售";
					}else if("10491002".equals(saleStatus)){
						SALE_STATUS="未售";
					}else{
						SALE_STATUS="无";
					}
					String REPAIR_STATUS="";
					String repairStatus = BaseUtils.checkNull(map.get("REPAIR_STATUS"));
					if("10481001".equals(repairStatus)){
						REPAIR_STATUS="已修";
					}else if("10481002".equals(repairStatus)){
						REPAIR_STATUS="未修";
					}else{
						REPAIR_STATUS="无";
					}
					String DEALER_CODE_REAL = BaseUtils.checkNull(map.get("DEALER_CODE_REAL"));
					String DEALER_SHORTNAME_REAL = BaseUtils.checkNull(map.get("DEALER_SHORTNAME_REAL"));
					String[] detail={VIN,DEALER_CODE,DEALER_SHORTNAME,ROOT_ORG_NAME,SALE_STATUS,REPAIR_STATUS,DEALER_CODE_REAL,DEALER_SHORTNAME_REAL};
					params.add(detail);
				}
			}
			this.toExcel(act, head, params,null,"服务活动VIN管理 ");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	/**
	 * 公用导出设置
	 * @param act
	 * @param head
	 * @param params
	 * @param name
	 */
	@SuppressWarnings("unchecked")
	public void toExcel(ActionContext act,String[] head,List params,List<Map<Integer, String>> listCount,String name){
		String systemDateStr = BaseUtils.getSystemDateStr();
		try {
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, name+systemDateStr+".xls", "导出数据",listCount);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> openSubject(RequestWrapper request,Integer pageSize, Integer currPage) {
		String subject_no = DaoFactory.getParam(request, "subject_no");
		String subject_name = DaoFactory.getParam(request, "subject_name");
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT * FROM TT_AS_ACTIVITY_SUBJECT T WHERE 1=1 AND  T.IS_DEL = 0 ");
		DaoFactory.getsql(sb, "t.activity_type", DaoFactory.getParam(request, "activity_type"), 1);
		DaoFactory.getsql(sb, "t.subject_no", subject_no, 2);
		DaoFactory.getsql(sb, "t.subject_name", subject_name, 2);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> vinByDetailData(Long dealerId,RequestWrapper request, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select c.* from (select c.activity_code,\n" );
		sb.append("       (select a.activity_name\n" );
		sb.append("          from Tt_As_Activity a\n" );
		sb.append("         where a.activity_code = c.activity_code) as activity_name,\n" );
		sb.append("       count(1) as count_vin \n" );
		sb.append("  from Tt_As_Activity_Relation_Code c\n" );
		sb.append(" where 1 = 1\n" );
		sb.append("   and c.dealer_code =\n" );
		sb.append("       (select tm.dealer_code\n" );
		sb.append("          from tm_dealer tm\n" );
		sb.append("         where tm.dealer_id = '"+dealerId+"')\n" );
		sb.append(" group by c.activity_code) c where 1=1 ");
		DaoFactory.getsql(sb, "c.activity_code", DaoFactory.getParam(request, "activity_code"), 2);
		DaoFactory.getsql(sb, "c.activity_name", DaoFactory.getParam(request, "activity_name"), 2);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> vinDetailByCount(Long dealerId,RequestWrapper request, Integer pageSize, Integer currPage) {
		String   is_repair= DaoFactory.getParam(request, "is_repair");
		if ("0".equals(is_repair)) {
			is_repair="已修";
		}else if ("1".equals(is_repair)) {
			is_repair="未修";
		}
		StringBuffer sb= new StringBuffer();
		sb.append("select * from (");
		sb.append("select c.vin,(case when  (select count(*) from tt_as_repair_order o where o.vin = c.vin\n" );
		sb.append(" and o.cam_code =c.activity_code) > 0 then '已修'else '未修' end) as is_repair\n" );
		sb.append("  from tt_as_activity_relation_code c\n" );
		sb.append(" where c.activity_code = '"+DaoFactory.getParam(request, "activity_code")+"'\n" );
		sb.append("   and c.dealer_code =\n" );
		sb.append("       (select tm.dealer_code\n" );
		sb.append("          from tm_dealer tm\n" );
		sb.append("         where tm.dealer_id = '"+dealerId+"')");
		sb.append(") c where 1=1 ");
		DaoFactory.getsql(sb, "c.is_repair", is_repair.trim(), 1);
		DaoFactory.getsql(sb, "c.vin", DaoFactory.getParam(request, "vin"), 2);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> vinDetailByautomobile(Long dealerId,RequestWrapper request, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.* from (select c.dealer_code,\n" );
		sb.append("       (select tm.dealer_shortname\n" );
		sb.append("          from tm_dealer tm\n" );
		sb.append("         where tm.dealer_code = c.dealer_code) as dealer_shortname,\n" );
		sb.append("       c.activity_code,\n" );
		sb.append("       (select a.activity_name\n" );
		sb.append("          from Tt_As_Activity a\n" );
		sb.append("         where a.activity_code = c.activity_code) as activity_name,\n" );
		sb.append("       (select s.subject_no\n" );
		sb.append("          from Tt_As_Activity_Subject s\n" );
		sb.append("         where s.subject_id =\n" );
		sb.append("               (select a.subject_id\n" );
		sb.append("                  from Tt_As_Activity a\n" );
		sb.append("                 where a.activity_code = c.activity_code)) as subject_no,\n" );
		sb.append("       (select s.subject_name\n" );
		sb.append("          from Tt_As_Activity_Subject s\n" );
		sb.append("         where s.subject_id =\n" );
		sb.append("               (select a.subject_id\n" );
		sb.append("                  from Tt_As_Activity a\n" );
		sb.append("                 where a.activity_code = c.activity_code)) as subject_name,\n" );
		sb.append("       (select a.status\n" );
		sb.append("          from Tt_As_Activity a\n" );
		sb.append("         where a.activity_code = c.activity_code) as status,\n" );
		sb.append("         count(1) as count_vin\n" );
		sb.append("  from tt_as_activity_relation_code c\n" );
		sb.append("  group by c.dealer_code,c.activity_code) t where 1=1 ");
		DaoFactory.getsql(sb, "t.activity_code", DaoFactory.getParam(request, "activity_code"), 2);
		DaoFactory.getsql(sb, "t.activity_name", DaoFactory.getParam(request, "activity_name"), 2);
		DaoFactory.getsql(sb, "t.dealer_code", DaoFactory.getParam(request, "dealerCode"), 6);
		DaoFactory.getsql(sb, "t.subject_name", DaoFactory.getParam(request, "subjectName"), 2);
		DaoFactory.getsql(sb, "t.status", DaoFactory.getParam(request, "status"), 1);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> vinDetailByCountFactory(String dealer_code, RequestWrapper request, Integer pageSize,Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select c.vin,(case when  (select count(*) from tt_as_repair_order o where o.vin = c.vin\n" );
		sb.append(" and o.cam_code =c.activity_code) > 0 then '已修'else '未修' end) as is_repair\n" );
		sb.append("  from tt_as_activity_relation_code c\n" );
		sb.append(" where c.activity_code = '"+DaoFactory.getParam(request, "activity_code")+"'\n" );
		sb.append("   and c.dealer_code ='"+dealer_code+"'" );
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> openActivity(RequestWrapper request,Integer pageSize, Integer currPage) {
		String activity_code = DaoFactory.getParam(request, "activity_code");
		String activity_name = DaoFactory.getParam(request, "activity_name");
		StringBuffer sb= new StringBuffer();
		sb.append("select * from tt_as_activity t where t.is_del=0 and t.status=10681002");
		DaoFactory.getsql(sb, "t.activity_type", DaoFactory.getParam(request, "activity_type"), 1);
		DaoFactory.getsql(sb, "t.activity_code", activity_code, 2);
		DaoFactory.getsql(sb, "t.activity_name", activity_name, 2);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> activityTemplet(RequestWrapper request, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select a.id,\n" );
		sb.append("       a.templet_name,\n" );
		sb.append("       a.templet_no,\n" );
		sb.append("       a.status,\n" );
		sb.append("       b.subject_no,\n" );
		sb.append("       b.subject_id,\n" );
		sb.append("       b.subject_name,\n" );
		sb.append("       b.activity_type\n" );
		sb.append("  from tt_as_wr_activity_templet a, tt_as_activity_subject b\n" );
		sb.append(" where a.subject_id = b.subject_id\n" );
		sb.append("   and 1 = 1\n" );
		sb.append("   and a.is_del = 0\n" );
		DaoFactory.getsql(sb, "a.templet_no", DaoFactory.getParam(request, "templet_no"), 2);
		DaoFactory.getsql(sb, "a.templet_name", DaoFactory.getParam(request, "templet_name"), 2);
		DaoFactory.getsql(sb, "a.status", DaoFactory.getParam(request, "status"), 1);
		DaoFactory.getsql(sb, "b.subject_id", DaoFactory.getParam(request, "subjectId"), 1);
		sb.append(" order by a.create_date desc");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public int templetAddSure(RequestWrapper request, AclUserBean loginUser) {
		int res=1;
		String identify = DaoFactory.getParam(request, "identify");
		String id = DaoFactory.getParam(request, "id");
		String case_remark = DaoFactory.getParam(request, "case_remark");
		String apply_remark = DaoFactory.getParam(request, "apply_remark");
		String templet_no = DaoFactory.getParam(request, "templet_no");
		String templet_name = DaoFactory.getParam(request, "templet_name");
		String subject_id = DaoFactory.getParam(request, "subject_id");
		String subject_no = DaoFactory.getParam(request, "subject_no");
		String subject_name = DaoFactory.getParam(request, "subject_name");
		String trouble_desc = DaoFactory.getParam(request, "trouble_desc");
		String trouble_reason = DaoFactory.getParam(request, "trouble_reason");
		/*String repair_method = DaoFactory.getParam(request, "repair_method");
		String app_remark = DaoFactory.getParam(request, "app_remark");*/
		String is_tips = DaoFactory.getParam(request, "is_tips");
		String is_return = DaoFactory.getParam(request, "is_return_temp");
		//配件
		String[] part_ids = DaoFactory.getParams(request, "part_id");
		String[] part_codes = DaoFactory.getParams(request, "part_code");
		String[] part_names = DaoFactory.getParams(request, "part_name");
		String[] old_part_codes = DaoFactory.getParams(request, "old_part_code");
		String[] part_cost_prices = DaoFactory.getParams(request, "part_cost_price");
		String[] part_quantitys = DaoFactory.getParams(request, "part_quantity");
		String[] part_cost_amounts = DaoFactory.getParams(request, "part_cost_amount");
		String[] responsibility_types = DaoFactory.getParams(request, "responsibility_type");
		String[] part_use_types = DaoFactory.getParams(request, "part_use_type");
		String[] producer_codes = DaoFactory.getParams(request, "producer_code");
		String[] is_returns = DaoFactory.getParams(request, "is_return");
		//工时
		String[] labour_codes = DaoFactory.getParams(request, "labour_code");
		String[] labour_names = DaoFactory.getParams(request, "labour_name");
		String[] labour_quotietys = DaoFactory.getParams(request, "labour_quotiety");
		String[] labour_prices = DaoFactory.getParams(request, "labour_price");
		String[] labour_amounts = DaoFactory.getParams(request, "labour_amount");
		//补偿费
		String[] apply_amounts = DaoFactory.getParams(request, "apply_amount");
		String[] remarks = DaoFactory.getParams(request, "remark");
		//辅料
		String[] workHourCodes = DaoFactory.getParams(request, "workHourCode");
		String[] workhour_names = DaoFactory.getParams(request, "workhour_name");
		String[] accessoriesPrices = DaoFactory.getParams(request, "accessoriesPrice");
		Long userId = loginUser.getUserId();
		try {
			if("".equals(id)){//保存
				Long pkId = DaoFactory.getPkId();
				TtAsWrActivityTempletPO t=new TtAsWrActivityTempletPO();
				t.setId(pkId);
				t.setCaseRemark(case_remark);
				t.setApplyRemark(apply_remark);
				t.setSubjectId(Long.valueOf(subject_id));
				t.setCreateBy(userId.toString());
				t.setCreateDate(new Date());
				t.setIsDel(0);
				t.setTempletNo(templet_no);
				t.setTempletName(templet_name);
				t.setTroubleDesc(trouble_desc);
				t.setTroubleReason(trouble_reason);
				/*t.setRepairMethod(repair_method);
				t.setAppRemark(app_remark);*/
				t.setSubjectNo(subject_no);
				t.setSubjectName(subject_name);
				t.setIsReturn(Integer.parseInt(is_return));
				t.setIsTips(Integer.parseInt(is_tips));
				if("0".equals(identify)){//保存
					t.setStatus(18041001);
				}else{
					t.setStatus(18041002);
				}
				this.setPo(part_ids, part_codes, part_names, old_part_codes,
						part_cost_prices, part_quantitys, part_cost_amounts,
						responsibility_types, part_use_types, producer_codes,
						is_returns, labour_codes, labour_names,
						labour_quotietys, labour_prices, labour_amounts,
						apply_amounts, remarks, workHourCodes, workhour_names,
						accessoriesPrices, pkId, t);
				this.insert(t);
			}else{//修改
				Long pkId =Long.valueOf(id);
				TtAsWrActivityTempletPO t1=new TtAsWrActivityTempletPO();
				t1.setId(pkId);
				TtAsWrActivityTempletPO t2=new TtAsWrActivityTempletPO();
				t2.setCaseRemark(case_remark);
				t2.setApplyRemark(apply_remark);
				t2.setSubjectId(Long.valueOf(subject_id));
				t2.setCreateBy(userId.toString());
				t2.setCreateDate(new Date());
				t2.setIsDel(0);
				t2.setTempletNo(templet_no);
				t2.setSubjectNo(subject_no);
				t2.setSubjectName(subject_name);
				t2.setTempletName(templet_name);
				t2.setTroubleDesc(trouble_desc);
				t2.setTroubleReason(trouble_reason);
				/*t2.setRepairMethod(repair_method);
				t2.setAppRemark(app_remark);*/
				t2.setIsReturn(Integer.parseInt(is_return));
				t2.setIsTips(Integer.parseInt(is_tips));
				if("0".equals(identify)){//保存
					t2.setStatus(18041001);
				}else{
					t2.setStatus(18041002);
				}
				this.delete("delete from tt_as_wr_activity_templet_lab t where t.templet_id="+pkId, null);
				this.delete("delete from tt_as_wr_activity_templet_part t where t.templet_id="+pkId, null);
				this.delete("delete from tt_as_wr_activity_templet_acc t where t.templet_id="+pkId, null);
				this.delete("delete from tt_as_wr_activity_templet_com t where t.templet_id="+pkId, null);

				this.setPo(part_ids, part_codes, part_names, old_part_codes,
						part_cost_prices, part_quantitys, part_cost_amounts,
						responsibility_types, part_use_types, producer_codes,
						is_returns, labour_codes, labour_names,
						labour_quotietys, labour_prices, labour_amounts,
						apply_amounts, remarks, workHourCodes, workhour_names,
						accessoriesPrices, pkId, t2);
				this.update(t1, t2);
			}
		} catch (Exception e) {
			e.printStackTrace();
			res=-1;
			throw new RuntimeException(e);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	private void setPo(String[] part_ids, String[] part_codes,
			String[] part_names, String[] old_part_codes,
			String[] part_cost_prices, String[] part_quantitys,
			String[] part_cost_amounts, String[] responsibility_types,
			String[] part_use_types, String[] producer_codes,
			String[] is_returns, String[] labour_codes, String[] labour_names,
			String[] labour_quotietys, String[] labour_prices,
			String[] labour_amounts, String[] apply_amounts, String[] remarks,
			String[] workHourCodes, String[] workhour_names,
			String[] accessoriesPrices, Long pkId, TtAsWrActivityTempletPO t) {
		//主因件
		String part_code_temp="";
		Double partAmount=0.0d;
		if(BaseUtils.checkNull(part_ids)){
			for (int i = 0; i < part_ids.length; i++) {
				TtAsWrActivityTempletPartPO part=new TtAsWrActivityTempletPartPO();
				part.setId(DaoFactory.getPkId());
				part.setIsReturn(Integer.valueOf(is_returns[i]));
				part.setOldPartCode(old_part_codes[i]);
				//part.setOldPartName(old);
				part.setPartCode(part_codes[i]);
				part.setPartName(part_names[i]);
				part.setPartCostPrice(Double.valueOf(part_cost_prices[i]));
				part.setPartQuantity(Integer.valueOf(part_quantitys[i]));
				part.setPartUseType(Integer.valueOf(part_use_types[i]));
				part.setProducerCode(producer_codes[i]);
				part.setPartId(Long.valueOf(part_ids[i]));
				Integer responsibility_type = Integer.valueOf(responsibility_types[i]);
				if(responsibility_type==94001001){//主因件 只有一个
					 part_code_temp = part_codes[i];
				}
				part.setResponsibilityType(responsibility_type);
				Double amount = Double.valueOf(part_cost_amounts[i]);
				part.setPartCostAmount(amount);
				part.setTempletId(pkId);
				partAmount+=amount;
				this.insert(part);
			}
		}
		Double lapAmount=0.0d;
		if(BaseUtils.checkNull(labour_codes)){
			for (int i = 0; i < labour_codes.length; i++) {
				TtAsWrActivityTempletLabPO lap=new TtAsWrActivityTempletLabPO();
				lap.setId(DaoFactory.getPkId());
				lap.setLabourCode(labour_codes[i]);
				lap.setLabourName(labour_names[i]);
				lap.setLabourPrice(Double.valueOf(labour_prices[i]));
				lap.setLabourQuotiety(Double.valueOf(labour_quotietys[i]));
				Double amount = Double.valueOf(labour_amounts[i]);
				lap.setLabourAmount(amount);
				lap.setMainPartCode(part_code_temp);
				lap.setTempletId(pkId);
				lapAmount+=amount;
				this.insert(lap);
			}
		}
		Double comAmount=0.0d;
		if(BaseUtils.checkNull(apply_amounts)){
			for (int i = 0; i < part_ids.length; i++) {
				TtAsWrActivityTempletComPO com=new TtAsWrActivityTempletComPO();
				Double amount = Double.valueOf(apply_amounts[i]);
				com.setComPrice(amount);
				com.setTempletId(pkId);
				com.setId(DaoFactory.getPkId());
				com.setMainPartCode(part_code_temp);
				com.setRemark(remarks[i]);
				comAmount+=amount;
				this.insert(com);
			}
		}
		Double accAmount=0.0d;
		if(BaseUtils.checkNull(workHourCodes)){
			for (int i = 0; i < part_ids.length; i++) {
				TtAsWrActivityTempletAccPO acc=new TtAsWrActivityTempletAccPO();
				acc.setId(DaoFactory.getPkId());
				acc.setAccCode(workHourCodes[i]);
				acc.setAccName(workhour_names[i]);
				Double amount = Double.valueOf(accessoriesPrices[i]);
				acc.setAccPrice(amount);
				acc.setMainPartCode(part_code_temp);
				acc.setTempletId(pkId);
				accAmount+=amount;
				this.insert(acc);
			}
		}
		t.setAccAmount(accAmount);
		t.setPartAmount(partAmount);
		t.setLabourAmount(lapAmount);
		t.setComAmount(comAmount);
		t.setAllAmount(accAmount+partAmount+lapAmount+comAmount);
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> showSubject(RequestWrapper request,Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		String subject_no = DaoFactory.getParam(request, "subject_no");
		String subject_name = DaoFactory.getParam(request, "subject_name");
		String templet_no = DaoFactory.getParam(request, "templet_no");
		sb.append("SELECT t.*,s.activity_type\n" );
		sb.append("  from tt_as_activity_subject s, tt_as_wr_activity_templet t\n" );
		sb.append(" where s.subject_id = t.subject_id\n" );
		sb.append("   and t.is_del = 0  and t.status=18041002 \n" );
		DaoFactory.getsql(sb, "t.subject_no", subject_no, 2);
		DaoFactory.getsql(sb, "t.subject_name", subject_name, 2);
		DaoFactory.getsql(sb, "t.templet_no", templet_no, 2);
		sb.append(" order by t.create_date desc");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public int activityAddSure(RequestWrapper request, AclUserBean loginUser) {
		int res=1;
		String identify = DaoFactory.getParam(request, "identify");
		String trouble_desc = DaoFactory.getParam(request, "trouble_desc");
		String trouble_reason = DaoFactory.getParam(request, "trouble_reason");
		String repair_method = DaoFactory.getParam(request, "repair_method");
		String app_remark = DaoFactory.getParam(request, "app_remark");
		
		String activityName = DaoFactory.getParam(request, "activityName");    //活动名称
		String startdate = DaoFactory.getParam(request, "startDate");          //活动开始日期
		String enddate = DaoFactory.getParam(request, "endDate");              //活动结束日期
		String factstartdate = DaoFactory.getParam(request, "factstartdate");          //活动开始日期
		String factenddate = DaoFactory.getParam(request, "factenddate");              //活动结束日期
		String activityId = DaoFactory.getParam(request, "activityId");             //活动ID
		String subjectId = DaoFactory.getParam(request, "subjectId");
		String yieldly = DaoFactory.getParam(request, "yieldly");
		String maxCar = DaoFactory.getParam(request, "car_max");             //单经销商活动总活动次数
		String tow_type_activity = DaoFactory.getParam(request, "tow_type_activity");
		String solution = DaoFactory.getParam(request, "solution");            //解决方案说明
		String claimGuide = DaoFactory.getParam(request, "claimGuide");        //索赔申请填写指导
		String activity_type = DaoFactory.getParam(request, "activityType");
		String [] vehicle = request.getParamValues("vehicle");  
		String defaultA = DaoFactory.getParam(request, "default");            //结算指向
		String templet_id = DaoFactory.getParam(request, "templet_id"); 
		String templet_no = DaoFactory.getParam(request, "templet_no"); 
		Long userId = loginUser.getUserId();
		try {
			if("".equals(activityId)){
				TtAsActivityPO po=new TtAsActivityPO();
				po.setActivityId(DaoFactory.getPkId());
				po.setSubjectId(Long.parseLong(subjectId));
				po.setActivityType(Integer.valueOf(activity_type));
				po.setActivityName(activityName);
				po.setTroubleDesc(trouble_desc);
				po.setTroubleReason(trouble_reason);
				String activityCode="";
				if(activity_type.equals("10561005")){//切换件
					activityCode=Utility.GetClaimBillNo("","YX","THJ");
				}else if(activity_type.equals("10561006")){//模板
					activityCode=Utility.GetClaimBillNo("","YX","ZDY");
				}else{
					activityCode=Utility.GetClaimBillNo("","YX","N");
				}
				if(Utility.testString(tow_type_activity))
					po.setTowTypeActivity(Integer.valueOf(tow_type_activity));
				
				
				po.setActivityCode(activityCode);
				po.setRepairMethod(repair_method);
				po.setAppRemark(app_remark);
				po.setStartdate(BaseUtils.getDate(startdate, 1));
				po.setEnddate(BaseUtils.getDate(enddate, 1));
				po.setFactstartdate(BaseUtils.getDate(factstartdate, 1));
				po.setFactenddate(BaseUtils.getDate(factenddate, 1));
				po.setClaimGuide(claimGuide);
				po.setSolution(solution);
				if("1".equals(identify)){
					po.setStatus(10681002);
				}else{
					po.setStatus(10681001);
				}
				po.setSetDirect(Long.parseLong(defaultA));
				po.setCompanyId(loginUser.getCompanyId());//公司ID
				po.setCreateBy(userId);
				po.setCreateDate(new Date());
				//po.setNewId(newsid);
				po.setUpdateBy(userId);
				po.setUpdateDate(new Date());
				po.setIsDel(0);//IS_DEL_01 = "1":逻辑删除;IS_DEL_00 = "0":逻辑未删除
				Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);     //公司ID
				po.setCompanyId(companyId);
				po.setMaxCar(Integer.parseInt(maxCar));
				po.setSetDirect(Long.valueOf(95411001));
				po.setMilageConfine(1);
				po.setVehicleArea(BaseUtils.Convert(vehicle));
				po.setTempletId(Long.valueOf(templet_id));
				po.setTempletNo(templet_no);
				this.insert(po);
			}else{
				TtAsActivityPO po1=new TtAsActivityPO();
				po1.setActivityId(Long.parseLong(activityId));
				TtAsActivityPO po2=new TtAsActivityPO();
				po2.setSubjectId(Long.parseLong(subjectId));
				po2.setActivityType(Integer.valueOf(activity_type));
				po2.setActivityName(activityName);
				po2.setTroubleDesc(trouble_desc);
				po2.setTroubleReason(trouble_reason);
				String activityCode=DaoFactory.getParam(request, "activityCode");
				po2.setActivityCode(activityCode);
				po2.setRepairMethod(repair_method);
				po2.setAppRemark(app_remark);
				if(Utility.testString(tow_type_activity))
					po2.setTowTypeActivity(Integer.valueOf(tow_type_activity));
				po2.setStartdate(BaseUtils.getDate(startdate, 1));
				po2.setEnddate(BaseUtils.getDate(enddate, 1));
				po2.setFactstartdate(BaseUtils.getDate(factstartdate, 1));
				po2.setFactenddate(BaseUtils.getDate(factenddate, 1));
				po2.setClaimGuide(claimGuide);
				po2.setSolution(solution);
				if("1".equals(identify)){
					po2.setStatus(10681002);
				}else{
					po2.setStatus(10681001);
				}
				po2.setSetDirect(Long.parseLong(defaultA));
				po2.setCompanyId(loginUser.getCompanyId());//公司ID
//				po2.setCreateBy(userId);
//				po2.setCreateDate(new Date());
				//po.setNewId(newsid);
				po2.setUpdateBy(userId);
				po2.setUpdateDate(new Date());
				po2.setIsDel(0);//IS_DEL_01 = "1":逻辑删除;IS_DEL_00 = "0":逻辑未删除
				Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);     //公司ID
				po2.setCompanyId(companyId);
				po2.setMaxCar(Integer.parseInt(maxCar));
				po2.setSetDirect(Long.valueOf(95411001));
				po2.setMilageConfine(1);
				po2.setVehicleArea(BaseUtils.Convert(vehicle));
				po2.setTempletId(Long.valueOf(templet_id));
				po2.setTempletNo(templet_no);
				this.update(po1, po2);
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> activityList(RequestWrapper request,Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT A.ACTIVITY_ID,\n" );
		sb.append("       A.ACTIVITY_NAME,\n" );
		sb.append("       A.ACTIVITY_CODE,\n" );
		sb.append("       A.STATUS,\n" );
		sb.append("       B.SUBJECT_NO,\n" );
		sb.append("       B.SUBJECT_ID,\n" );
		sb.append("       B.SUBJECT_NAME,b.activity_type,\n" );
		sb.append("       t.templet_no,t.id as templet_id,\n" );
		sb.append("       t.templet_name\n" );
		sb.append("  FROM TT_AS_ACTIVITY            A,\n" );
		sb.append("       TT_AS_ACTIVITY_SUBJECT    B,\n" );
		sb.append("       tt_as_wr_activity_templet t\n" );
		sb.append(" where A.SUBJECT_ID = B.SUBJECT_ID\n" );
		sb.append("   and a.subject_id = t.subject_id \n" );
		sb.append("   and a.templet_id=t.id and t.status=18041002 \n" );
		sb.append("   AND A.IS_DEL = 0\n" );
		DaoFactory.getsql(sb, "t.templet_no", DaoFactory.getParam(request, "templet_no"), 2);
		DaoFactory.getsql(sb, "t.templet_name", DaoFactory.getParam(request, "templet_name"), 2);
		DaoFactory.getsql(sb, "a.activity_Code", DaoFactory.getParam(request, "activityCode"), 2);
		DaoFactory.getsql(sb, "a.activity_name", DaoFactory.getParam(request, "activity_name"), 2);
		DaoFactory.getsql(sb, "b.subject_id", DaoFactory.getParam(request, "subjectId"), 1);
		DaoFactory.getsql(sb, "a.status", DaoFactory.getParam(request, "status"), 1);

		sb.append(" ORDER BY trim(A.ACTIVITY_CODE) desc");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> relationShow(RequestWrapper request,Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.* from   TT_AS_ACTIVITY_SUBJECT t where t.is_del=0\n");
		DaoFactory.getsql(sb, "t.subject_no", DaoFactory.getParam(request, "subject_no"), 2);
		DaoFactory.getsql(sb, "t.subject_name", DaoFactory.getParam(request, "subject_name"), 2);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

    /**
     * 根据vin查询活动总数,完成数，未完成数
     * @param request
     * @param loginUser
     * @param pageSize
     * @param currPage
     * @return
     */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> QueryactivityByvin(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sql= new StringBuffer();
		sql.append("select vin,(countall - countcam) as count, countcam, countall\n" );
		sql.append("  from (select v.vin, count(*) as countall,\n" );
		sql.append("               (select count(1)\n" );
		sql.append("                  from Tt_As_Repair_Order o\n" );
		sql.append("                 where o.vin = v.vin\n" );
		sql.append("                   and o.cam_code is not null) as countcam\n" );
		sql.append("          from TT_AS_ACTIVITY_VEHICLE v \n" );
		sql.append("         where 1 = 1\n" );
		sql.append("\n" );
		DaoFactory.getsql(sql, "v.vin", DaoFactory.getParam(request,"vin"), 2);
		sql.append("         group by v.vin)");
		PageResult<Map<String, Object>> list=pageQuery(sql.toString(), null,getFunName(), pageSize, currPage);
			return list;
	}
	
}
