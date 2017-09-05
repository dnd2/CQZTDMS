package com.infodms.dms.actions.report.dmsReport;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.claim.basicData.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.common.tag.ExcelUtil;
import com.infodms.dms.dao.claim.dealerClaimMng.DealerClaimReportDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.orgmng.DlrInfoMngDAO;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TtAsQuelityFollowPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infodms.yxdms.constant.MyConstant;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


@SuppressWarnings("rawtypes")
public class ApplicationDao extends BaseDao{

	private static final ApplicationDao dao = new ApplicationDao();
	public static final ApplicationDao getInstance(){
		if (dao == null) {
			return new ApplicationDao();
		}
		return dao;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>>   report1(RequestWrapper request,AclUserBean loginUser, int pageSize, int currPage) {
		String beginTime = DaoFactory.getParam(request, "beginTime");
		String endTime = DaoFactory.getParam(request, "endTime");
		String claim_type = DaoFactory.getParam(request, "claim_type");
		String dealerCode = DaoFactory.getParam(request, "dealerCode");
		String carModel = DaoFactory.getParam(request, "carModel");
		
		StringBuffer sb= new StringBuffer();
		sb.append("    	  select distinct w.dealer_code, t.id,t.ro_no,\n" );
		sb.append("       w.dealer_shortname,se.root_org_name, t.report_date,\n" );
		sb.append("       t.claim_no,t.campaign_code, \n" );
		sb.append("       decode(t.model_code, 'B2', 'S2', 'B3', 'S3',t.model_code,t.model_code) as model, \n" );
		sb.append("       t.vin, \n" );
		sb.append("       (select c.code_desc from tc_code c where c.code_id = t.claim_type) as claim_type, \n" );
		sb.append("       nvl(t.balance_labour_amount,0) as labour_amount, \n" );//工时费用
		sb.append("       nvl(t.balance_part_amount,0) as part_amount, \n" );//配件费用
		sb.append("       nvl(t.balance_netitem_amount,0) as netitem_amount, \n" );//其他费用
		sb.append("       nvl(t.free_m_price,0) as free_m_price, \n" );//保养费
		sb.append("       nvl(t.accessories_price,0) as accessories_price, \n" );//辅料费
		sb.append("       nvl(t.compensation_money,0) as compensation_money, \n" );//补偿费
		sb.append("       t.balance_amount \n" );
		

		//判断开始时间结束时间是否在当前月
		if(beginTime!=""||endTime!=""){
			try {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
				Date currentMonth = sdf.parse(sdf.format(new Date()));
				int flag=0;
				
				if(beginTime.length()>0){
					Date beginMonth=sdf.parse(beginTime.substring(0,7));
					if(beginMonth.compareTo(currentMonth)==0 ){
						flag=1;
					}
				}
				if(endTime.length()>0){
					Date endMonth = sdf.parse(endTime.substring(0,7));
					if(endMonth.compareTo(currentMonth)==0 ){
						flag=1;
					}
				}
				if(flag==1){
					sb.append("  from tt_as_wr_application t, tm_dealer w ,vw_material_group_service vw,vw_org_dealer_service se,  tm_vehicle vv\n" );
				}else{
					sb.append("  from tt_as_wr_application_fixed t, tm_dealer w ,vw_material_group_service vw,vw_org_dealer_service se,  tm_vehicle vv\n" );
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			sb.append("  from tt_as_wr_application_fixed t, tm_dealer w ,vw_material_group_service vw,vw_org_dealer_service se,  tm_vehicle vv \n" );
		}
		sb.append(" where w.dealer_id = t.dealer_id and t.vin=vv.vin and vw.package_id=vv.package_id \n" );
		sb.append("   and t.dealer_id=se.dealer_id \n" );
		sb.append("   and t.claim_type in\n" );
		sb.append("       (10661001, 10661006, 10661007, 10661012, 10661010, 10661009) and t.status>=10791007 and  t.is_import=10041002\n" );
		
		if (this.getPoseRoleId(loginUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
		       sb.append(CommonUtils.getOrgDealerLimitSqlByPose("w", loginUser));
		}
		String part_code = DaoFactory.getParam(request, "part_code");
		String producer_code = DaoFactory.getParam(request, "producer_code");
		String labour_code = DaoFactory.getParam(request, "labour_code");
		if(BaseUtils.testString(part_code)){
			sb.append("   and t.id in (select p.id from Tt_As_Wr_Partsitem p where 1=1 \n" );
			DaoFactory.getsql(sb, "p.part_code", part_code, 2);
			sb.append("     )\n" );
		}
		if(BaseUtils.testString(producer_code)){
			sb.append("   and t.id in (select p.id from Tt_As_Wr_Partsitem p where 1=1 \n" );
			DaoFactory.getsql(sb, "p.producer_code", producer_code, 2);
			sb.append("     )\n" );
		}
		if(BaseUtils.testString(labour_code)){
			sb.append("   and t.id in (select l.id from Tt_As_Wr_Labouritem l where 1=1 \n" );
			DaoFactory.getsql(sb, "l.labour_code", labour_code, 2);
			sb.append("     )\n" );
		}
		//============
		DaoFactory.getsql(sb, "t.claim_type", claim_type, 1);
		DaoFactory.getsql(sb, "w.dealer_code", dealerCode, 6);
		DaoFactory.getsql(sb, "vw.model_code", DaoFactory.getSqlByarrIn(carModel), 6);
		DaoFactory.getsql(sb, "t.report_date",beginTime, 3);
		DaoFactory.getsql(sb, "t.report_date", endTime, 4);
		DaoFactory.getsql(sb, "t.campaign_code", DaoFactory.getParam(request, "activity_code"), 1);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>>  report2(RequestWrapper request,AclUserBean logonUser,int pageSize,int currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.vin,\n" );
		sb.append("       t.id,\n" );
		sb.append("       t.ro_no,\n" );
		sb.append("       (select c.code_desc from tc_code c where c.code_id = t.claim_type) as claim_type,\n" );
		sb.append("       t.claim_no,\n" );
		sb.append("        (select tm.dealer_shortname from tm_dealer tm where tm.dealer_id= t.dealer_id) as dealer_shortname,\n" );
		sb.append("     ( select tc.name from tc_user tc where tc.user_id=  t.create_by) as name ,\n" );
		sb.append("       t.report_date,\n" );
		sb.append("       nvl(t.free_m_price, 0) as free_m_price,\n" );
		sb.append("       nvl(m.amount, 0) as amount\n" );
		sb.append("  from tt_as_wr_application t,\n" );
		sb.append("       Tt_As_Wr_Netitem     m\n" );
		sb.append(" where 1=1\n" );
		 if (this.getPoseRoleId(logonUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
             sb.append(CommonUtils.getOrgDealerLimitSqlByPose("t", logonUser));
         }
		sb.append("   and t.id = m.id(+)\n" );
		sb.append("   and t.claim_type in (10661002, 10661011)\n" );
		sb.append("   and t.status >= 10791007 and  t.is_import=10041002");

		String beginTime = DaoFactory.getParam(request, "beginTime");
		String endTime = DaoFactory.getParam(request, "endTime");
		String claim_type = DaoFactory.getParam(request, "claim_type");
		DaoFactory.getsql(sb, "t.report_date",beginTime, 3);
		DaoFactory.getsql(sb, "t.report_date", endTime, 4);
		DaoFactory.getsql(sb, "t.claim_type", claim_type, 1);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> report3(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage) {
		String startDate=request.getParamValue("startDate");
		String endDate=request.getParamValue("endDate");
		StringBuffer sb= new StringBuffer();
		sb.append("  select t.*,\n" );
		sb.append("        (t.achiveNumself + t.helpAchiveTemp) as repairNum,\n" );
		sb.append("        (t.achiveNumself + t.helpAchive) as repairNumAll,\n" );
		sb.append("       round((t.achiveNumself + t.helpAchiveTemp)/ t.countAllByDealer, 2) * 100 || '%' as percentBydealer\n" );
		sb.append("  from (select vd.root_org_name as org_name,\n" );
		sb.append("               vd.region_name,\n" );
		sb.append("               vd.dealer_id,\n" );
		sb.append("               vd.dealer_code,\n" );
		sb.append("               tm.dealer_shortname,\n" );
		sb.append("               arc.activity_code,\n" );
		sb.append("               arc.activity_id,\n" );
		sb.append("               (select v.activity_name\n" );
		sb.append("                  from Tt_As_Activity v\n" );
		sb.append("                 where v.activity_id = arc.activity_id) as activity_name,\n" );
		sb.append("               count(arc.vin) as countAllByDealer, --服务站分配数量\n" );
		sb.append("               (select count(*)\n" );
		sb.append("                  from tt_as_wr_application awa\n" );
		sb.append("                 where awa.dealer_id = vd.dealer_id\n" );
		sb.append("                   and awa.vin in\n" );
		sb.append("                       (select ar.vin\n" );
		sb.append("                          from tt_as_activity_relation_code ar\n" );
		sb.append("                         where ar.activity_id = arc.activity_id\n" );
		sb.append("                           and ar.dealer_code = vd.dealer_code)\n" );
		sb.append("                   and awa.campaign_code = arc.activity_code\n" );
		sb.append("                   and awa.claim_type = 10661006\n" );
		sb.append("                   and awa.status not in\n" );
		sb.append("                       (10791001, 10791002, 10791003, 10791005, 10791006)) as achiveNumself, --自店完成数\n" );
		sb.append("               (select count(*)\n" );
		sb.append("                  from tt_as_wr_application awa\n" );
		sb.append("                 where awa.dealer_id <> vd.dealer_id\n" );
		sb.append("                   and awa.vin  in\n" );
		sb.append("                       (select ar.vin\n" );
		sb.append("                          from tt_as_activity_relation_code ar\n" );
		sb.append("                         where ar.activity_id = arc.activity_id\n" );
		sb.append("                           and ar.dealer_code = vd.dealer_code)\n" );
		sb.append("                   and awa.campaign_code = arc.activity_code\n" );
		sb.append("                   and awa.claim_type = 10661006\n" );
		sb.append("                   and awa.status not in\n" );
		sb.append("                       (10791001, 10791002, 10791003, 10791005, 10791006)) as helpAchiveTemp, --别人对他自店完成数\n" );
		sb.append("               (select count(*)\n" );
		sb.append("                  from tt_as_wr_application awa\n" );
		sb.append("                 where awa.dealer_id = vd.dealer_id\n" );
		sb.append("                   and awa.vin in\n" );
		sb.append("                       (select ar.vin\n" );
		sb.append("                          from tt_as_activity_relation_code ar\n" );
		sb.append("                         where ar.activity_id = arc.activity_id\n" );
		sb.append("                           and ar.dealer_code <> vd.dealer_code)\n" );
		sb.append("                   and awa.campaign_code = arc.activity_code\n" );
		sb.append("                   and awa.claim_type = 10661006\n" );
		sb.append("                   and awa.status not in\n" );
		sb.append("                       (10791001, 10791002, 10791003, 10791005, 10791006)) as helpAchive --他对别人帮助完成数\n" );
		sb.append("          from tt_as_activity_relation_code arc,\n" );
		sb.append("               vw_org_dealer_service        vd,\n" );
		sb.append("               tm_dealer        tm,\n" );
		sb.append("               tt_as_activity               a\n" );
		sb.append("         where arc.dealer_code = vd.dealer_code\n" );
		sb.append("        and    arc.dealer_code = tm.dealer_code \n" );
		sb.append("           and arc.activity_id = a.activity_id\n" );
		sb.append("           and a.status in (10681002, 10681004)\n" );

		 if (this.getPoseRoleId(loginUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
             sb.append(CommonUtils.getOrgDealerLimitSqlByPose("vd", loginUser));
         }
		if(startDate!=null){
			sb.append("   and arc.create_date >= to_date('"+startDate+"','yyyy-MM-dd') \n");
		}
		if(endDate!=null){
			sb.append(" and to_date(to_char(arc.create_date,'yyyy-MM-dd'),'yyyy-MM-dd') <= to_date('"+endDate+"','yyyy-MM-dd') \n");
		}
		sb.append("         group by vd.root_org_name,\n" );
		sb.append("                  vd.region_name,\n" );
		sb.append("                  vd.dealer_id,\n" );
		sb.append("                  vd.dealer_code,\n" );
		sb.append("                  vd.dealer_name,\n" );
		sb.append("                  tm.dealer_shortname,\n" );
		sb.append("                  arc.activity_code,\n" );
		sb.append("                  arc.activity_id) t\n" );
		sb.append(" where 1=1   ");

		String dealerCode = DaoFactory.getParam(request, "dealerCode");
		DaoFactory.getsql(sb, "t.dealer_code", dealerCode, 6);
		DaoFactory.getsql(sb, "t.activity_code", DaoFactory.getParam(request, "activity_code"), 1);
		sb.append("  and t.countAllByDealer <> 0 ");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> report4(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage) {
		String startdate =DaoFactory.getParam(request, "beginTime");
		String enddate =DaoFactory.getParam(request, "endTime");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date startdate1=null;
		Date enddate1=null;
		Date onlineDate=null;
		Long start=null;
		Long end =null;
		Long online=null;
		try {
			startdate1 =dateFormat.parse(startdate);
			enddate1 =dateFormat.parse(enddate);
			 onlineDate =dateFormat.parse(MyConstant.onlineDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 start = startdate1.getTime();
		 end = enddate1.getTime();
		 online = onlineDate.getTime();
		 
		StringBuffer sb= new StringBuffer();
		sb.append("select t.ro_no,\n" );
		sb.append("       t.status,\n" );
		sb.append("       t.id,\n" );
		sb.append("       case\n" );
		sb.append("         when t.campaign_code is not null then\n" );
		sb.append("          nvl(t.balance_amount, 0)\n" );
		sb.append("         else\n" );
		sb.append("          0\n" );
		sb.append("       end as activity_amount,\n" );
		sb.append("       to_char(t.report_date, 'yyyy-mm-dd') as report_date,\n" );
		sb.append("       (select tm.dealer_code\n" );
		sb.append("          from tm_dealer tm\n" );
		sb.append("         where tm.dealer_id = t.dealer_id) as dealer_code,\n" );
		sb.append("       (select tm.dealer_shortname\n" );
		sb.append("          from tm_dealer tm\n" );
		sb.append("         where tm.dealer_id = t.dealer_id) as dealer_shortname,\n" );
		sb.append("       t.claim_no,\n" );
		sb.append("       (select c.code_desc from tc_code c where c.code_id = t.claim_type) as claim_type,\n" );
		sb.append("       t.vin,\n" );
		sb.append("       t.campaign_code cam_code,\n" );
		sb.append("       vw.model_name,\n" );
		sb.append("       to_char(t.ro_startdate, 'yyyy-mm-dd') as ro_startdate,\n" );
		sb.append("       p.part_code,\n" );
		sb.append("       p.producer_code,\n" );
		sb.append("       p.part_name,\n" );
		sb.append("       p.remark,\n" );
		sb.append("       p.down_part_code,\n" );
		sb.append("       nvl(p.balance_amount, 0) as balance_amount1,\n" );
		
		if (start<=online && end<=online) {//新分单时间节点
			sb.append("       (select nvl(l.balance_amount, 0)\n" );
			sb.append("          from tt_as_wr_labouritem l\n" );
			sb.append("         where l.id = t.id\n" );
			sb.append("           and l.wr_labourcode = p.wr_labourcode) as balance_amount2,\n" );
			}else if (start>online && end>online) {
				sb.append(" nvl((select sum(awl.balance_amount)  from tt_as_wr_labouritem awl where awl.id = t.id),0) as balance_amount2,\n" );
			}
		sb.append("       nvl(t.balance_netitem_amount, 0) as netitem_amount,\n" );
		sb.append("       nvl(t.accessories_price, 0) as accessories_price,\n" );
		sb.append("       nvl(t.compensation_money, 0) as compensation_money,\n" );
		sb.append("\n" );
		sb.append("       nvl(p.balance_amount, 0) +\n" );
		
		if (start<=online && end<=online) {//新分单时间节点
			sb.append("       (select nvl(l.balance_amount, 0)\n" );
			sb.append("          from tt_as_wr_labouritem l\n" );
			sb.append("         where l.id = t.id\n" );
			sb.append("           and l.wr_labourcode = p.wr_labourcode) +\n" );
			}else if (start>online && end>online) {
				sb.append(" nvl((select sum(awl.balance_amount)  from tt_as_wr_labouritem awl where awl.id = t.id),0) +\n" );
			}
		sb.append("       nvl(t.balance_netitem_amount, 0) + nvl(t.accessories_price, 0) +\n" );
		sb.append("       nvl(t.compensation_money, 0) as countAll\n" );
		sb.append("  from Tt_As_Wr_Partsitem p, tm_dealer tm, tt_as_wr_application t,tm_vehicle v,vw_material_group_service vw\n" );
		sb.append(" where 1 = 1\n" );
		sb.append("   and t.dealer_id = tm.dealer_id\n" );
		sb.append("   and p.id = t.id\n" );
		sb.append("   and  t.vin= v.vin\n" );
		sb.append("   and vw.package_id = v.package_id\n" );
		sb.append("   and p.responsibility_type=94001001\n" );
		sb.append("   and t.status > 10791001\n" );
		sb.append("   and t.is_import = 10041002");
		 if (this.getPoseRoleId(loginUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
             sb.append(CommonUtils.getOrgDealerLimitSqlByPose("tm", loginUser));
         }

		String beginTime = DaoFactory.getParam(request, "beginTime");
		String endTime = DaoFactory.getParam(request, "endTime");
		String claim_no = DaoFactory.getParam(request, "claim_no");
		String part_code = DaoFactory.getParam(request, "part_code");
		String down_part_code = DaoFactory.getParam(request, "down_part_code");
		String claim_type = DaoFactory.getParam(request, "claim_type");
		String status = DaoFactory.getParam(request, "status");
		String dealerCode = DaoFactory.getParam(request, "dealerCode");
		DaoFactory.getsql(sb, "t.report_date",beginTime, 3);
		DaoFactory.getsql(sb, "t.report_date", endTime, 4);
		DaoFactory.getsql(sb, "t.claim_no", claim_no, 2);
		DaoFactory.getsql(sb, "p.part_code", part_code, 2);
		DaoFactory.getsql(sb, "p.down_part_code", down_part_code, 2);
		DaoFactory.getsql(sb, "tm.dealer_code", dealerCode, 6);
		DaoFactory.getsql(sb, "t.claim_type", claim_type, 1);
		DaoFactory.getsql(sb, "t.status", status, 1);

		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> report5(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage) {
			StringBuffer sb= new StringBuffer();
			sb.append("select a.ro_startdate,\n" );
			sb.append("       a.ro_no,\n" );
			sb.append("       a.id,\n" );
			sb.append("       a.parts_count,\n" );
			sb.append("       (select ds.root_org_name\n" );
			sb.append("          from vw_org_dealer_service ds\n" );
			sb.append("         where ds.dealer_id = a.dealer_id) as org_name,\n" );
			sb.append("       a.claim_no,\n" );
			sb.append("       a.vin,\n" );
			sb.append("       r.dealer_code,\n" );
			sb.append("       a.campaign_code as cam_code,\n" );
			sb.append("       a.engine_no,\n" );
			sb.append("       (select c.code_desc from tc_code c where c.code_id = a.claim_type) as claim_type,\n" );
			sb.append("       a.in_mileage,\n" );
			sb.append("       (select tm.dealer_shortname\n" );
			sb.append("          from tm_dealer tm\n" );
			sb.append("         where tm.dealer_id = a.dealer_id) as dealer_shortname,\n" );
			sb.append("       a.trouble_desc as TROUBLE_REASON,\n" );
			sb.append("       a.trouble_reason as trouble_desc,\n" );
			sb.append("       n.deal_method as repair_method,\n" );
			sb.append("       n.down_part_name,\n" );
			sb.append("       n.part_code,\n" );
			sb.append("       (select d.maker_name from tt_part_maker_define d where d.maker_code=n.producer_code) as producer_name,\n" );
			sb.append("       n.producer_code,\n" );
			sb.append("       vw.PACKAGE_NAME package_name,\n" );
			sb.append("       c.purchased_date,\n" );
			sb.append("       (select v.group_name\n" );
			sb.append("          from tm_vhcl_material_group v\n" );
			sb.append("         where v.group_id = c.model_id) as model_name,\n" );
			sb.append("       c.product_date\n" );
			sb.append("  from tt_as_wr_application      a,tt_as_repair_order r,\n" );
			sb.append("       tt_as_wr_partsitem        n,\n" );
			sb.append("       tm_vehicle                c,\n" );
			sb.append("       vw_org_dealer_service        v,\n" );
			sb.append("       vw_material_group_service vw\n" );
			sb.append(" where 1 = 1\n" );
			sb.append("   and a.ro_no=r.ro_no(+) and a.vin = c.vin(+)\n" );
			sb.append("   and c.package_id = vw.PACKAGE_ID(+)\n" );
			sb.append("   and a.id = n.id(+)\n" );
			sb.append("   and a.dealer_id = v.dealer_id(+)\n" );
			sb.append("   and a.status in (10791007, 10791008)\n" );
			sb.append("   and a.claim_type in (10661007, 10661001, 10661009, 10661006)");

			if (this.getPoseRoleId(loginUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
	             sb.append(CommonUtils.getOrgDealerLimitSqlByPose("a", loginUser));
	        }
			String beginTime = DaoFactory.getParam(request, "beginTime");
			String carModel = DaoFactory.getParam(request, "carModel");
			DaoFactory.getsql(sb, "vw.model_code", DaoFactory.getSqlByarrIn(carModel), 6);
			String endTime = DaoFactory.getParam(request, "endTime");
			String claim_type = DaoFactory.getParam(request, "claim_type");
			DaoFactory.getsql(sb, "A.REPORT_DATE",beginTime, 3);
			DaoFactory.getsql(sb, "A.REPORT_DATE", endTime, 4);
			DaoFactory.getsql(sb, "A.CLAIM_TYPE", claim_type, 1);
			DaoFactory.getsql(sb, "A.vin", DaoFactory.getParam(request, "vin"), 2); 
			DaoFactory.getsql(sb, "n.part_code", DaoFactory.getParam(request, "part_code"), 2);
			DaoFactory.getsql(sb, "r.dealer_code", DaoFactory.getParam(request, "dealerCode"), 6);
			DaoFactory.getsql(sb, "a.dealer_name", DaoFactory.getParam(request, "dealer_name"), 2);
			DaoFactory.getsql(sb, " n.producer_code", DaoFactory.getParam(request, "producer_code"), 2);
			DaoFactory.getsql(sb, " v.root_org_name", DaoFactory.getParam(request, "org_name"), 2);
			DaoFactory.getsql(sb, " n.DOWN_PART_NAME", DaoFactory.getParam(request, "part_name"), 2);
			return pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
	}
	
	
	
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>>   report7(RequestWrapper request,AclUserBean loginUser, int pageSize, int currPage) {
		StringBuffer sql= new StringBuffer();
		sql.append("select * from (\n" );
		sql.append("select tm.dealer_id,\n" );
		sql.append("       org_name,\n" );
		sql.append("       dealer_code,\n" );
		sql.append("       tm.dealer_shortname,org_id,root_org_id,\n" );
		sql.append("       nvl(count_score,0) count_score,\n" );
		sql.append("       nvl(in_time_score,0) in_time_score,\n" );
		sql.append("       nvl(add_number_score,0) add_number_score,\n" );
		sql.append("       nvl(add_quality_score,0) add_quality_score,\n" );
		sql.append("       nvl(count_num,0) count_num,\n" );
		sql.append("       nvl(app_num,0) app_num,\n" );
		sql.append("       TO_CHAR(round(nvl(count_num,0) / case\n" );
		sql.append("                       when nvl(app_num,0) = 0 then\n" );
		sql.append("                        1\n" );
		sql.append("                       else\n" );
		sql.append("                        app_num\n" );
		sql.append("                     end,\n" );
		sql.append("                     4) * 100,\n" );
		sql.append("               'fm999990.99999') || '%' as percent\n" );
		sql.append("  from ((select tm.dealer_id, tm.dealer_code, tm.dealer_shortname,vw.root_org_name as org_name,vw.org_id,vw.root_org_id\n" );
		sql.append("           from tm_dealer tm,vw_org_dealer_service vw\n" );
		sql.append("          where 1 = 1\n" );
		sql.append("          and tm.dealer_id=vw.dealer_id(+)\n" );
		sql.append("            and tm.dealer_type = 10771002\n" );
		sql.append("            and tm.status = 10011001\n" );
		sql.append("            and tm.service_status = 13691002) tm left join\n" );
		if (this.getPoseRoleId(loginUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
            sql.append(CommonUtils.getOrgDealerLimitSqlByPose("tm", loginUser));
        }//权限
		sql.append("        (select count(1) as app_num, a.dealer_id\n" );
		sql.append("           from tt_as_wr_application a\n" );
		sql.append("          where 1 = 1\n" );
		sql.append("            and a.claim_type in (10661001, 10661007, 10661009)\n" );
		sql.append("            and a.IS_IMPORT = 10041002\n" );
		DaoFactory.getsql(sql,"a.sub_date", DaoFactory.getParam(request, "startDate"), 3);
		DaoFactory.getsql(sql,"a.sub_date", DaoFactory.getParam(request, "endDate"), 4);
		
		sql.append("          group by a.dealer_id) t on tm.dealer_id = t.dealer_id left join ---保修数量\n" );
		sql.append("        (select t.dealer_id,\n" );
		sql.append("                round(avg(t.count_score), 2) as count_score,\n" );
		sql.append("                round(avg(t.in_time_score), 2) as in_time_score,\n" );
		sql.append("                round(avg(t.add_number_score), 2) as add_number_score,\n" );
		sql.append("                round(avg(t.add_quality_score), 2) as add_quality_score,\n" );
		sql.append("                count(t.quality_id) as count_num\n" );
		sql.append("           from Tt_Sales_Quality_Report_Info t\n" );
		sql.append("          where 1 = 1\n" );
		DaoFactory.getsql(sql,"to_date(t.report_date_btn,'yyyy-mm-dd hh24:mi:ss')", DaoFactory.getParam(request, "startDate"), 3);
		DaoFactory.getsql(sql,"to_date(t.report_date_btn,'yyyy-mm-dd hh24:mi:ss')", DaoFactory.getParam(request, "endDate"), 4);
		sql.append("            and t.verify_status = 95531003\n" );
		sql.append("          group by t.dealer_id) t1 on t.dealer_id = t1.dealer_id) )DD where 1=1 ");
		DaoFactory.getsql(sql,"DD.dealer_code", DaoFactory.getParam(request, "dealerCode"), 2);
		DaoFactory.getsql(sql,"DD.root_org_id", DaoFactory.getParam(request, "__large_org"), 2);
		DaoFactory.getsql(sql,"DD.org_id", DaoFactory.getParam(request, "__province_org"), 2);


		PageResult<Map<String, Object>> list=pageQuery(sql.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>>   report8(RequestWrapper request,int pageSize, int currPage) {
		
		String startDate=request.getParamValue("startDate");
		String endDate=request.getParamValue("endDate");
		StringBuffer sb= new StringBuffer();
		sb.append("select a.*\n" );
		sb.append("  from (select t.vin,\n" );
		sb.append("               (select g.group_code\n" );
		sb.append("                  from tm_vhcl_material_group g\n" );
		sb.append("                 where g.group_id = (select v.model_id\n" );
		sb.append("                                       from tm_vehicle v\n" );
		sb.append("                                      where v.vin = t.vin)) as model_name,\n" );
		sb.append("               (select count(1)\n" );
		sb.append("                  from Tt_Sales_Quality_Report_Info t1\n" );
		sb.append("                 where t1.vin = t.vin\n" );
		sb.append("                   and t1.verify_status >= 95531002) as report_num,\n" );
		sb.append("               (select count(1)\n" );
		sb.append("                  from Tt_Sales_Quality_Report_Info t1\n" );
		sb.append("                 where t1.vin = t.vin\n" );
		sb.append("                    and t1.verify_status in ( 95531003,95531005,95531006 )) as pass_num,\n" );
		sb.append("               (select count(1)\n" );
		sb.append("                  from Tt_Sales_Quality_Report_Info t1\n" );
		sb.append("                 where t1.vin = t.vin\n" );
		sb.append("                   and t1.verify_status = 95531004) as rebut_num\n" );
		sb.append("          from Tt_Sales_Quality_Report_Info t where 1=1\n" );
		if(startDate!=null){
			sb.append("         and to_date(t.report_date_btn,'yyyy-mm-dd hh24:mi:ss') >=  to_date('"+startDate+"','yyyy-MM-dd') ");
		}
		if(endDate!=null){
			sb.append("         and to_date(t.report_date_btn,'yyyy-mm-dd hh24:mi:ss') <=  to_date('"+endDate+"','yyyy-MM-dd')  ");
		}
		String vin = DaoFactory.getParam(request, "VIN");
		String passNum = DaoFactory.getParam(request, "PASS_NUM");
		DaoFactory.getsql(sb, "t.vin", vin, 2);
		sb.append("         group by t.vin) a where 1=1\n" );
		DaoFactory.getsql(sb, "a.pass_num", passNum, 1);
		sb.append(" order by a.pass_num desc");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>>   report9(RequestWrapper request,int pageSize, int currPage) {
		StringBuffer sb= new StringBuffer();
		String startDate= request.getParamValue("startDate");
		String endDate= request.getParamValue("endDate");
		sb.append("select c.root_org_name, round(avg(count_score), 2) as count_score,\n" );
		sb.append("round(avg(in_time_score), 2) as in_time_score,\n" );
		sb.append("round(avg(add_number_score), 2) as add_number_score,\n" );
		sb.append("round(avg(add_quality_score), 2) as add_quality_score, count(*) as COUNT_NUM, sum(countall) as APP_NUM,\n" );
		sb.append("round(count(*)/sum(countall),2) * 100 || '%' as percent --百分比\n" );
		sb.append("  from (select (select s.root_org_name\n" );
		sb.append("                  from vw_org_dealer_service s\n" );
		sb.append("                 where s.dealer_id = t.dealer_id) as root_org_name,\n" );
		sb.append("               t.count_score as count_score,\n" );
		sb.append("               t.in_time_score as in_time_score,\n" );
		sb.append("               t.add_number_score as add_number_score,\n" );
		sb.append("               t.add_quality_score as add_quality_score,\n" );
		sb.append("               (case when(select count(1) from tt_as_wr_application a\n" );
		sb.append("               where a.dealer_id = t.dealer_id and a.claim_type <> 10661006\n" );
		sb.append("               and a.IS_IMPORT = 10041002)=0 then 1 else (select count(1)\n" );
		sb.append("               from tt_as_wr_application a\n" );
		sb.append("               where a.dealer_id = t.dealer_id\n" );
		sb.append("               and a.claim_type <> 10661006\n" );
		sb.append("               and a.IS_IMPORT = 10041002) end ) as countall\n" );
		sb.append("          from Tt_Sales_Quality_Report_Info t   ");
		sb.append("         where 1=1   ");
		if(startDate!=null){
			sb.append("         and to_date(t.report_date_btn,'yyyy-mm-dd hh24:mi:ss') >=  to_date('"+startDate+"','yyyy-MM-dd') ");
		}
		if(endDate!=null){
			sb.append("         and to_date(t.report_date_btn,'yyyy-mm-dd hh24:mi:ss') <=  to_date('"+endDate+"','yyyy-MM-dd')  ");
		}
		
		
		sb.append("		) c where 1=1 \n" );
		String rootOrgName = DaoFactory.getParam(request, "root_org_name");
		DaoFactory.getsql(sb, "c.root_org_name", rootOrgName, 2);
		sb.append(" group by c.root_org_name");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	@SuppressWarnings("unchecked")
	public void expotData1(ActionContext act, PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[15];
			head[0]="大区";
			head[1]="商家代码";
			head[2]="商家简称";
			head[3]="索赔单号";
			head[4]="车型";
			head[5]="VIN";
			head[6]="申报时间";
			head[7]="索赔类型";
			head[8]="工时费";
			head[9]="材料费 ";
			head[10]="外出费 ";
			head[11]="辅料费";
			head[12]="补偿费";
			head[13]="结算金额";
			head[14]="服务活动编码";
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[15];
					detail[0]=BaseUtils.checkNull(map.get("ROOT_ORG_NAME"));
					detail[1]=BaseUtils.checkNull(map.get("DEALER_CODE"));
					detail[2]=BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					detail[3]=BaseUtils.checkNull(map.get("CLAIM_NO"));
					detail[4]=BaseUtils.checkNull(map.get("MODEL"));
					detail[5]=BaseUtils.checkNull(map.get("VIN"));
					detail[6]=BaseUtils.checkNull(map.get("REPORT_DATE"));
					detail[7]=BaseUtils.checkNull(map.get("CLAIM_TYPE"));
					detail[8]=BaseUtils.checkNull(map.get("LABOUR_AMOUNT"));
					detail[9]=BaseUtils.checkNull(map.get("PART_AMOUNT"));
					detail[10]=BaseUtils.checkNull(map.get("NETITEM_AMOUNT"));
					detail[11]=BaseUtils.checkNull(map.get("ACCESSORIES_PRICE"));
					detail[12]=BaseUtils.checkNull(map.get("COMPENSATION_MONEY"));
					detail[13]=BaseUtils.checkNull(map.get("BALANCE_AMOUNT"));
					detail[14]=BaseUtils.checkNull(map.get("CAMPAIGN_CODE"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出单车索赔金额数据"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void expotDealerRelationData(ActionContext act, PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[14];
			head[0]="销售经销商代码";
			head[1]="销售经销商名称";
			head[2]="销售经销商公司";
			head[3]="售后经销商代码";
			head[4]="售后经销商名称";
			head[5]="售后经销商公司";
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[14];
					detail[0]=BaseUtils.checkNull(map.get("XS_DEALER_CODE"));
					detail[1]=BaseUtils.checkNull(map.get("XS_DEALER_NAME"));
					detail[2]=BaseUtils.checkNull(map.get("XS_COMPANY_NAME"));
					detail[3]=BaseUtils.checkNull(map.get("SH_DEALER_CODE"));
					detail[4]=BaseUtils.checkNull(map.get("SH_DEALER_NAME"));
					detail[5]=BaseUtils.checkNull(map.get("SH_COMPANY_NAME"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "销售经销商与售后经销商关系"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void expotDataCPV(ActionContext act, PageResult<Map<String, Object>> list) {
		try {
		
			String[] head=new String[13];
			head[0]="年份";
			head[1]="月份";
			head[2]="车型";
			head[3]="在报台数";
			head[4]="索赔费用";
			head[5]="CPV";
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[13];
					detail[0]=BaseUtils.checkNull(map.get("VDATEYEAR"));
					detail[1]=BaseUtils.checkNull(map.get("VDATEMONTH"));
					detail[2]=BaseUtils.checkNull(map.get("GROUP_NAME"));
					detail[3]=BaseUtils.checkNull(map.get("VCOUNT"));
					detail[4]=BaseUtils.checkNull(map.get("RMOUNT"));
					detail[5]=BaseUtils.checkNull(map.get("CPV"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出CPV数据"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void expotData3CS(ActionContext act, PageResult<Map<String, Object>> list) {
		try {
		
			String[] head=new String[13];
			head[0]="年份";
			head[1]="月份";
			head[2]="生产车辆";
			head[3]="销售车辆";
			head[4]="索赔数量";
			head[5]="3CS值";
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[13];
					detail[0]=BaseUtils.checkNull(map.get("VDATEYEAR"));
					detail[1]=BaseUtils.checkNull(map.get("VDATEMONTH"));
					detail[2]=BaseUtils.checkNull(map.get("PCOUNT"));
					detail[3]=BaseUtils.checkNull(map.get("SCOUNT"));
					detail[4]=BaseUtils.checkNull(map.get("RCOUNT"));
					detail[5]=BaseUtils.checkNull(map.get("PT"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出3CS数据"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void expotData7(ActionContext act, PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[10];
			head[0]="大区";
			head[1]="服务商代码";
			head[2]="服务商简称";
			head[3]="总分";
			head[4]="速度得分";
			head[5]="质量得分";
			head[6]="数量得分";
			head[7]="品质情报数量";
			head[8]="保修数量";
			head[9]="品质情报上报率 ";
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[12];
					detail[0]=BaseUtils.checkNull(map.get("ORG_NAME"));
					detail[1]=BaseUtils.checkNull(map.get("DEALER_CODE"));
					detail[2]=BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					detail[3]=BaseUtils.checkNull(map.get("COUNT_SCORE"));
					detail[4]=BaseUtils.checkNull(map.get("IN_TIME_SCORE"));
					detail[5]=BaseUtils.checkNull(map.get("ADD_QUALITY_SCORE"));
					detail[6]=BaseUtils.checkNull(map.get("ADD_NUMBER_SCORE"));
					detail[7]=BaseUtils.checkNull(map.get("COUNT_NUM"));
					detail[8]=BaseUtils.checkNull(map.get("APP_NUM"));
					detail[9]=BaseUtils.checkNull(map.get("PERCENT"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出品质情报报表数据"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void expotData9_2(ActionContext act, PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[8];
			head[0]="大区";
			head[1]="总分";
			head[2]="速度得分";
			head[3]="质量得分";
			head[4]="数量得分";
			head[5]="品质情报数量";
			head[6]="保修数量";
			head[7]="品质情报上报率 ";
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[12];
					detail[0]=BaseUtils.checkNull(map.get("ROOT_ORG_NAME"));
					detail[1]=BaseUtils.checkNull(map.get("COUNT_SCORE"));
					detail[2]=BaseUtils.checkNull(map.get("IN_TIME_SCORE"));
					detail[3]=BaseUtils.checkNull(map.get("ADD_QUALITY_SCORE"));
					detail[4]=BaseUtils.checkNull(map.get("ADD_NUMBER_SCORE"));
					detail[5]=BaseUtils.checkNull(map.get("COUNT_NUM"));
					detail[6]=BaseUtils.checkNull(map.get("APP_NUM"));
					detail[7]=BaseUtils.checkNull(map.get("PERCENT"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出按大区统计报表数据"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void expotData8(ActionContext act, PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[8];
			head[0]="车架号";
			head[1]="车型";
			head[2]="上报数量";
			head[3]="通过数量";
			head[4]="驳回数量";
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[12];
					detail[0]=BaseUtils.checkNull(map.get("VIN"));
					detail[1]=BaseUtils.checkNull(map.get("MODEL_NAME"));
					detail[2]=BaseUtils.checkNull(map.get("REPORT_NUM"));
					detail[3]=BaseUtils.checkNull(map.get("PASS_NUM"));
					detail[4]=BaseUtils.checkNull(map.get("REBUT_NUM"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出单台车品质汇报报表数据"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	

	@SuppressWarnings("unchecked")
	public void expotData2(ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[8];
			head[0]="车架号";
			head[1]="单据类型";
			head[2]="索赔单号";
			head[3]="保养商家简称";
			head[4]="用户姓名";
			head[5]="开单时间";
			head[6]="首保金额";
			head[7]="PDI金额 ";
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[8];
					detail[0]=BaseUtils.checkNull(map.get("VIN"));
					detail[1]=BaseUtils.checkNull(map.get("CLAIM_TYPE"));
					detail[2]=BaseUtils.checkNull(map.get("CLAIM_NO"));
					detail[3]=BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					detail[4]=BaseUtils.checkNull(map.get("NAME"));
					detail[5]=BaseUtils.checkNull(map.get("REPORT_DATE"));
					detail[6]=BaseUtils.checkNull(map.get("FREE_M_PRICE"));
					detail[7]=BaseUtils.checkNull(map.get("AMOUNT"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出保养台次数据"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public void expotData3(ActionContext act,PageResult<Map<String, Object>> list) {
		try {
			String[] head={"大区","服务商代码","服务商简称","活动代码","活动名称","服务站分配数量","自店完成数","他店帮助完成数","帮助他店完成数","自店总完成数","修理总数量","完成率"};
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			List<Map<Integer, String>> listCount=new ArrayList<Map<Integer,String>>();
			/*Map<Integer, String> mapList =new HashMap<Integer, String>();
			Long countallbydealer=0L;
			Long achivenumself=0L;
			Long helpachive=0L;
			Long repairnum=0L;*/
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String ORG_NAME = BaseUtils.checkNull(map.get("ORG_NAME"));
					String DEALER_CODE = BaseUtils.checkNull(map.get("DEALER_CODE"));
					String DEALER_SHORTNAME = BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					String ACTIVITY_CODE = BaseUtils.checkNull(map.get("ACTIVITY_CODE"));
					String ACTIVITY_NAME = BaseUtils.checkNull(map.get("ACTIVITY_NAME"));
					String COUNTALLBYDEALER = BaseUtils.checkNull(map.get("COUNTALLBYDEALER"));
					String ACHIVENUMSELF = BaseUtils.checkNull(map.get("ACHIVENUMSELF"));
					String HELPACHIVETEMP = BaseUtils.checkNull(map.get("HELPACHIVETEMP"));
					String HELPACHIVE = BaseUtils.checkNull(map.get("HELPACHIVE"));
					String REPAIRNUM = BaseUtils.checkNull(map.get("REPAIRNUM"));
					String REPAIRNUMALL = BaseUtils.checkNull(map.get("REPAIRNUMALL"));
					String PERCENTBYDEALER = BaseUtils.checkNull(map.get("PERCENTBYDEALER"));
					String[] detail={ORG_NAME,DEALER_CODE,DEALER_SHORTNAME,
							ACTIVITY_CODE,ACTIVITY_NAME,
							COUNTALLBYDEALER,ACHIVENUMSELF,HELPACHIVETEMP,
							HELPACHIVE,REPAIRNUM,REPAIRNUMALL,PERCENTBYDEALER};
					params.add(detail);
					/*BigDecimal l1 = (BigDecimal)map.get("COUNTALLBYDEALER");
					BigDecimal l2 = (BigDecimal)map.get("ACHIVENUMSELF");
					BigDecimal l3 = (BigDecimal)map.get("HELPACHIVE");
					BigDecimal l4 = (BigDecimal)map.get("REPAIRNUM");
					countallbydealer+=l1.longValue();
					achivenumself+=l2.longValue();
					helpachive+=l3.longValue();
					repairnum+=l4.longValue();*/
				}
			}
			/*mapList.put(0, "合计");
			mapList.put(5, String.valueOf(countallbydealer));
			mapList.put(6, String.valueOf(achivenumself));
			mapList.put(7, String.valueOf(helpachive));
			mapList.put(8, String.valueOf(repairnum));
			listCount.add(mapList);*/
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出服务活动完成率数据"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@SuppressWarnings("unchecked")
	public void expotData4(ActionContext act,PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[16];
			head[0]="月份";
			head[1]="商家代码";
			head[2]="商家简称";
			head[3]="索赔单号";
			head[4]="索赔类型";
			head[5]="VIN";
			head[6]="车型";
			head[7]="维修日期";
			head[8]="配件编码";
			head[9]="配件名称";
			head[10]="原因分析";
			head[11]="供应商代码";
			head[12]="工时费";
			head[13]="材料费";
			head[14]="费用合计";
			head[15]="活动费用";
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[16];
					detail[0]=BaseUtils.checkNull(map.get("REPORT_DATE"));
					detail[1]=BaseUtils.checkNull(map.get("DEALER_CODE"));
					detail[2]=BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					detail[3]=BaseUtils.checkNull(map.get("CLAIM_NO"));
					detail[4]=BaseUtils.checkNull(map.get("CLAIM_TYPE"));
					detail[5]=BaseUtils.checkNull(map.get("VIN"));
					detail[6]=BaseUtils.checkNull(map.get("MODEL_NAME"));
					detail[7]=BaseUtils.checkNull(map.get("RO_STARTDATE"));
					detail[8]=BaseUtils.checkNull(map.get("PART_CODE"));
					detail[9]=BaseUtils.checkNull(map.get("PART_NAME"));
					detail[10]=BaseUtils.checkNull(map.get("REMARK"));
					detail[11]=BaseUtils.checkNull(map.get("DOWN_PART_CODE"));
					detail[12]=BaseUtils.checkNull(map.get("BALANCE_AMOUNT2"));
					detail[13]=BaseUtils.checkNull(map.get("BALANCE_AMOUNT1"));
					detail[14]=BaseUtils.checkNull(map.get("COUNTALL"));
					detail[15]=BaseUtils.checkNull(map.get("ACTIVITY_AMOUNT"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "索赔结算清单数据"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	@SuppressWarnings("unchecked")
	public void expotData5(ActionContext act,PageResult<Map<String, Object>> list) {
		try {
			String[] head={"所属区域","经销商名称","经销商代码","索赔申请单号","VIN","车型","发动机号","配置","工单开始时间","车辆生产日期","配件结算数量","索赔类型","车辆购车日期","行驶里程"," 故障描述","故障原因","维修措施","换下配件名称","换上配件代码","供应商信息","活动编号"};
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String ORG_NAME = BaseUtils.checkNull(map.get("ORG_NAME"));
					String DEALER_SHORTNAME = BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					String DEALER_CODE = BaseUtils.checkNull(map.get("DEALER_CODE"));
					String CLAIM_NO = BaseUtils.checkNull(map.get("CLAIM_NO"));
					String VIN = BaseUtils.checkNull(map.get("VIN"));
					String MODEL_NAME= BaseUtils.checkNull(map.get("MODEL_NAME"));
					String ENGINE_NO = BaseUtils.checkNull(map.get("ENGINE_NO"));
					String PACKAGE_NAME = BaseUtils.checkNull(map.get("PACKAGE_NAME"));
					String RO_STARTDATE = BaseUtils.checkNull(map.get("RO_STARTDATE"));
					String PRODUCT_DATE = BaseUtils.checkNull(map.get("PRODUCT_DATE"));
					String PARTS_COUNT = BaseUtils.checkNull(map.get("PARTS_COUNT"));
					String CLAIM_TYPE = BaseUtils.checkNull(map.get("CLAIM_TYPE"));
					String PURCHASED_DATE = BaseUtils.checkNull(map.get("PURCHASED_DATE"));
					String IN_MILEAGE = BaseUtils.checkNull(map.get("IN_MILEAGE"));
					String TROUBLE_DESC = BaseUtils.checkNull(map.get("TROUBLE_DESC"));
					String TROUBLE_REASON = BaseUtils.checkNull(map.get("TROUBLE_REASON"));
					String REPAIR_METHOD = BaseUtils.checkNull(map.get("REPAIR_METHOD"));
					String DOWN_PART_NAME = BaseUtils.checkNull(map.get("DOWN_PART_NAME"));
					String PART_CODE = BaseUtils.checkNull(map.get("PART_CODE"));
					String PRODUCER_NAME = BaseUtils.checkNull(map.get("PRODUCER_NAME"));
					String CAM_CODE = BaseUtils.checkNull(map.get("CAM_CODE"));
					String[] detail={ORG_NAME,DEALER_SHORTNAME,DEALER_CODE,
							CLAIM_NO,VIN,MODEL_NAME,ENGINE_NO,
							PACKAGE_NAME,RO_STARTDATE,
							PRODUCT_DATE,PARTS_COUNT,
							CLAIM_TYPE,PURCHASED_DATE,
							IN_MILEAGE,TROUBLE_DESC,
							TROUBLE_REASON,REPAIR_METHOD,
							DOWN_PART_NAME,PART_CODE,
							PRODUCER_NAME,CAM_CODE};
					params.add(detail);
				}
			}
			this.toExcel(act, head, params,null,"索赔结算清单数据");
		} catch (Exception e) {
			e.printStackTrace();
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
		}
	}
	
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> report6(RequestWrapper request,Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		String return_no = DaoFactory.getParam(request, "return_no");
		sb.append("select taword.id,\n" );
		sb.append("       taword.is_scan,\n" );
		sb.append("       TAWORD.Is_Main_Code,\n" );
		sb.append("       taword.is_out,\n" );
		sb.append("       taword.claim_no,\n" );
		sb.append("       taword.vin,\n" );
		sb.append("       c.id as claim_id,\n" );
		sb.append("       taword.PRODUCER_NAME,\n" );
		sb.append("       taword.producer_code,\n" );
		sb.append("       to_char(c.RO_STARTDATE, 'yyyy-mm-dd') as RO_STARTDATE,\n" );
		sb.append("       c.REMARK,\n" );
		sb.append("       del.dealer_code,\n" );
		sb.append("       c.claim_type,\n" );
		sb.append("       tawp.part_code,\n" );
		sb.append("       tawp.part_name,\n" );
		sb.append("       nvl(taword.return_amount, 0) return_amount,\n" );
		sb.append("       nvl(taword.sign_amount, 0) sign_amount,\n" );
		sb.append("       c.Is_Invoice,\n" );
		sb.append("       taword.barcode_no,\n" );
		sb.append("       decode(TAWORD.LOCAL_WAR_HOUSE,\n" );
		sb.append("              NULL,\n" );
		sb.append("              b.local_war_house,\n" );
		sb.append("              TAWORD.LOCAL_WAR_HOUSE) LOCAL_WAR_HOUSE,\n" );
		sb.append("       decode(TAWORD.LOCAL_WAR_SHEL,\n" );
		sb.append("              NULL,\n" );
		sb.append("              b.LOCAL_WAR_SHEL,\n" );
		sb.append("              TAWORD.LOCAL_WAR_SHEL) LOCAL_WAR_SHEL,\n" );
		sb.append("       decode(TAWORD.LOCAL_WAR_LAYER,\n" );
		sb.append("              NULL,\n" );
		sb.append("              b.LOCAL_WAR_LAYER,\n" );
		sb.append("              TAWORD.LOCAL_WAR_LAYER) LOCAL_WAR_LAYER,\n" );
		sb.append("       taword.box_no,\n" );
		sb.append("       taword.warehouse_region,\n" );
		sb.append("       taword.deduct_remark,\n" );
		sb.append("       decode(taword.deduct_remark,\n" );
		sb.append("              0,\n" );
		sb.append("              '--请选择--',\n" );
		sb.append("              null,\n" );
		sb.append("              '--请选择--',\n" );
		sb.append("              tc.code_desc) deduct_desc,\n" );
		sb.append("       TAWORD.Is_Import\n" );
		sb.append("  from tt_as_wr_old_returned        tawor,\n" );
		sb.append("       tm_pt_part_base              b,\n" );
		sb.append("       tt_as_wr_old_returned_detail taword,\n" );
		sb.append("       tt_as_wr_partsitem           tawp,\n" );
		sb.append("       tc_code                      tc,\n" );
		sb.append("       TT_AS_WR_APPLICATION         c,\n" );
		sb.append("       tm_dealer                    del\n" );
		sb.append(" where tawor.id = taword.return_id\n" );
		sb.append("   AND taword.part_code = b.part_code(+)\n" );
		sb.append("   AND c.ID = tawp.ID\n" );
		sb.append("   AND c.claim_no = taword.claim_no\n" );
		sb.append("   and taword.part_id = tawp.part_id\n" );
		sb.append("   and taword.deduct_remark = tc.code_id(+)\n" );
		sb.append("   and taword.is_sign = 0\n" );
		sb.append("   and c.id = taword.claim_id\n" );
		sb.append("   and del.dealer_id = c.dealer_id\n" );
		DaoFactory.getsql(sb, "tawor.return_no", return_no, 1);
		sb.append(" order by box_no, claim_no, part_name, barcode_no");

		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public void expotData6(ActionContext act,PageResult<Map<String, Object>> list) {
		try {
		String[] head={"主键列","索赔申请单","回运数","签收数","装箱单号","扣除原因","责任性质","配件代码","配件名称","编号","存放库位","供应商代码","维修日期","索赔单类型"};
		List<Map<String, Object>> records = list.getRecords();
		List params=new ArrayList();
		if(records!=null &&records.size()>0){
			for (Map<String, Object> map : records) {
				String[] detail=new String[14];
				detail[0]=BaseUtils.checkNull(map.get("ID"));
				detail[1]=BaseUtils.checkNull(map.get("CLAIM_NO"));
				detail[2]=BaseUtils.checkNull(map.get("RETURN_AMOUNT"));
				detail[3]=BaseUtils.checkNull(map.get("SIGN_AMOUNT"));
				detail[4]=BaseUtils.checkNull(map.get("BOX_NO"));
				detail[5]=this.getTypeDesc(BaseUtils.checkNull(map.get("DEDUCT_REMARK")));
				detail[6]=this.getTypeDesc(BaseUtils.checkNull(map.get("IS_MAIN_CODE")));
				detail[7]=BaseUtils.checkNull(map.get("PART_CODE"));
				detail[8]=BaseUtils.checkNull(map.get("PART_NAME"));
				detail[9]=BaseUtils.checkNull(map.get("BARCODE_NO"));
				detail[10]=BaseUtils.checkNull(map.get("LOCAL_WAR_HOUSE"));
				detail[11]=BaseUtils.checkNull(map.get("PRODUCER_CODE"));
				detail[12]=BaseUtils.checkNull(map.get("RO_STARTDATE"));
				detail[13]=this.getTypeDesc(BaseUtils.checkNull(map.get("CLAIM_TYPE")));
				params.add(detail);
			}
		}
		this.toExcel(act, head, params,null,"通用导入导出旧件修改");
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
	
	@SuppressWarnings("unchecked")
	public String getTypeDesc(String codeId){
		TcCodePO t=new TcCodePO();
		t.setCodeId(codeId);
		List<TcCodePO> list= dao.select(t);
		if(list!=null && list.size()>0){
			t=list.get(0);
			return t.getCodeDesc();
		}else{
			return "无";
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public void report6_1(RequestWrapper request, ActionContext act) {
		try {
			BaseImport b=new BaseImport();
			long maxSize=1024*1024*5;
			int errNum=b.insertIntoTmp(request, "importFile",18,6,maxSize);
			if(errNum!=0){
				List<ExcelErrors> errorList=new ArrayList();
				ExcelErrors ees=new ExcelErrors();
				ees.setRowNum(0);
				switch (errNum) {
				case 1:
					ees.setErrorDesc("文件列数过多");
					break;
				case 2:
					ees.setErrorDesc("空行不能大于三行");
					break;
				case 3:
					ees.setErrorDesc("文件不能为空");
					break;
				case 4:
					ees.setErrorDesc("文件不能为空");
					break;
				case 5:
					ees.setErrorDesc("文件不能大于"+maxSize);
					break;
				case 6:
					ees.setErrorDesc("文件内容格式不正确");
					break;
				default:
					break;
				}
				String ServiceActivityVinFailureUrl = "/jsp/claim/serviceActivity/serviceActivityVinFailure.jsp";//VIN导入清单失败页面

				errorList.add(ees);
				act.setOutData("errorList", errorList);
				if(1==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ServiceActivityVinFailureUrl);
				}
				if(2==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ServiceActivityVinFailureUrl);
				}
				if(3==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ServiceActivityVinFailureUrl);
				}
				if(4==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ServiceActivityVinFailureUrl);
				}
				if(5==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ServiceActivityVinFailureUrl);
				}
				if(6==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ServiceActivityVinFailureUrl);
				}
				
			}
			FileObject uploadFile = request.getParamObject("importFile");
			ExcelUtil t =new ExcelUtil();
			String result = t.readExcel(uploadFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> selectSupplierCode(RequestWrapper request, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		String maker_code = DaoFactory.getParam(request, "maker_code");
		String maker_shotname = DaoFactory.getParam(request, "maker_shotname");
		sb.append("select c.maker_id   as supplier_id,  c.maker_code, c.maker_shotname\n" );
		sb.append("    from tt_part_maker_define  c where  1=1 and c.state=10011001 " );
		DaoFactory.getsql(sb, "c.maker_code", maker_code, 2);
		DaoFactory.getsql(sb, "c.maker_shotname", maker_shotname, 2);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> partNumDetai1Data(RequestWrapper request, Integer pageSizeMax,Integer currPage) {
		String id = DaoFactory.getParam(request, "id");
		StringBuffer sb= new StringBuffer();
		sb.append("select (select ds.root_org_name\n" );
		sb.append("          from vw_org_dealer_service ds\n" );
		sb.append("         where ds.dealer_id = a.dealer_id) as org_name,\n" );
		sb.append("       (select tm.dealer_code\n" );
		sb.append("          from tm_dealer tm\n" );
		sb.append("         where tm.dealer_id = a.dealer_id) as dealer_code,\n" );
		sb.append("       (select tm.dealer_shortname\n" );
		sb.append("          from tm_dealer tm\n" );
		sb.append("         where tm.dealer_id = a.dealer_id) as dealer_shortname,\n" );
		sb.append("       (select g.group_name\n" );
		sb.append("          from tm_vhcl_material_group g\n" );
		sb.append("         where g.group_id = a.series_id) as series_name,\n" );
		sb.append("       (select g.group_name\n" );
		sb.append("          from tm_vhcl_material_group g\n" );
		sb.append("         where g.group_id = a.model_id) as model_name,\n" );
		sb.append("       a.vin,\n" );
		sb.append("        t.product_date,\n" );
		sb.append("       t.manufacturedate ,\n" );
		sb.append("       a.in_mileage as mileage,\n" );
		sb.append("       p.part_code,\n" );
		sb.append("       p.part_name,p.remark, \n" );
		sb.append("       p.producer_code,\n" );
		sb.append("       o.ro_create_date,\n" );
		sb.append("       o.for_balance_time,a.ro_startdate,a.ro_enddate,\n" );
		sb.append("       p.apply_quantity\n" );
		sb.append("  from Tt_As_Wr_Application a, tt_as_repair_order o, Tt_As_Wr_Partsitem p,tm_vehicle t\n" );
		sb.append(" where 1 = 1\n" );
		sb.append("   and a.ro_no = o.ro_no\n" );
		sb.append("   and a.vin=t.vin\n" );
		sb.append("   and p.id = a.id \n" );
		sb.append("   and a.claim_type<>10661006 and p.pay_type=11801002 ");
		TtAsQuelityFollowPO t=new TtAsQuelityFollowPO();
		t.setId(BaseUtils.ConvertLong(id));
		t=(TtAsQuelityFollowPO) dao.select(t).get(0);
		DaoFactory.getsql(sb, "p.down_part_code",BaseUtils.checkNull(t.getPartCode()), 1);
		if(t.getRoRepairDateOne()!=null){
			DaoFactory.getsql(sb, "a.ro_startdate",BaseUtils.checkNull(BaseUtils.printDateTime(t.getRoRepairDateOne())), 3);
		}
		if(t.getRoRepairDateTwo()!=null){
			DaoFactory.getsql(sb, "a.ro_startdate",BaseUtils.checkNull(BaseUtils.printDateTime(t.getRoRepairDateTwo())), 4);
		}
		DaoFactory.getsql(sb, "t.series_id",BaseUtils.checkNull(t.getCarTieId()==-1L?"":t.getCarTieId()), 1);
		DaoFactory.getsql(sb, "t.model_id",BaseUtils.checkNull(t.getCarTypeId()==-1L?"":t.getCarTypeId()), 1);
		//DaoFactory.getsql(sb, "a.vin",BaseUtils.checkNull(t.getVin()), 2);
		if(t.getRoCreateDate()!=null){
			DaoFactory.getsql(sb, "t.manufacturedate",BaseUtils.checkNull(BaseUtils.printDateTime(t.getRoCreateDate())), 31);
			DaoFactory.getsql(sb, "t.manufacturedate",BaseUtils.checkNull(BaseUtils.printDateTime(new Date())), 41);
		}
		DaoFactory.getsql(sb, "p.producer_code",BaseUtils.checkNull(t.getMakerCode()), 1);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSizeMax, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public void partNumDetai1Data(ActionContext act,PageResult<Map<String, Object>> list) {
		try {
			String[] head={"大区","服务商代码","服务商简称","车系","车型","VIN","生产日期","总装上线时间","行驶公里数","主故障件代码","主故障件名称","故障件数量","故障现象","部件厂代码","起始修理日期","终止修理日期"};
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[16];
					detail[0]=BaseUtils.checkNull(map.get("ORG_NAME"));
					detail[1]=BaseUtils.checkNull(map.get("DEALER_CODE"));
					detail[2]=BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					detail[3]=BaseUtils.checkNull(map.get("SERIES_NAME"));
					detail[4]=BaseUtils.checkNull(map.get("MODEL_NAME"));
					detail[5]=BaseUtils.checkNull(map.get("VIN"));
					detail[6]=BaseUtils.checkNull(map.get("PRODUCT_DATE"));
					detail[7]=BaseUtils.checkNull(map.get("MANUFACTUREDATE"));
					detail[8]=BaseUtils.checkNull(map.get("MILEAGE"));
					detail[9]=BaseUtils.checkNull(map.get("PART_CODE"));
					detail[10]=BaseUtils.checkNull(map.get("PART_NAME"));
					detail[11]=BaseUtils.checkNull(map.get("APPLY_QUANTITY"));
					detail[12]=BaseUtils.checkNull(map.get("REMARK"));//故障描述
					detail[13]=BaseUtils.checkNull(map.get("PRODUCER_CODE"));
					detail[14]=BaseUtils.checkNull(map.get("RO_STARTDATE"));
					detail[15]=BaseUtils.checkNull(map.get("RO_ENDDATE"));
					params.add(detail);
				}
			}
			this.toExcel(act, head, params,null,"导出质改索赔明细");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void expotData9(ActionContext act, PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[8];
			head[0]="大区";
			head[1]="总分";
			head[2]="速度得分";
			head[3]="质量得分";
			head[4]="数量得分";
			head[5]="品质情报数量";
			head[6]="保修数量";
			head[7]="品质情报上报率 ";
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[12];
					detail[0]=BaseUtils.checkNull(map.get("ROOT_ORG_NAME"));
					detail[1]=BaseUtils.checkNull(map.get("COUNT_SCORE"));
					detail[2]=BaseUtils.checkNull(map.get("IN_TIME_SCORE"));
					detail[3]=BaseUtils.checkNull(map.get("ADD_QUALITY_SCORE"));
					detail[4]=BaseUtils.checkNull(map.get("ADD_NUMBER_SCORE"));
					detail[5]=BaseUtils.checkNull(map.get("COUNT_NUM"));
					detail[6]=BaseUtils.checkNull(map.get("APP_NUM"));
					detail[7]=BaseUtils.checkNull(map.get("PERCENT"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出按大区统计报表数据"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> report1Sum(RequestWrapper request,AclUserBean loginUser,
			Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		String beginTime = DaoFactory.getParam(request, "beginTime");
		String endTime = DaoFactory.getParam(request, "endTime");
		sb.append("   with tb_1 as ( 	  select distinct w.dealer_code, t.id,t.ro_no,\n" );
		sb.append("       w.dealer_shortname, t.report_date,\n" );
		sb.append("       t.claim_no,t.campaign_code, \n" );
		sb.append("       decode(t.model_code, 'B2', 'S2', 'B3', 'S3') as model, \n" );
		sb.append("       t.vin, \n" );
		sb.append("       (select c.code_desc from tc_code c where c.code_id = t.claim_type) as claim_type, \n" );
		sb.append("       nvl(t.balance_labour_amount,0) as labour_amount, \n" );//工时费用
		sb.append("       nvl(t.balance_part_amount,0) as part_amount, \n" );//配件费用
		sb.append("       nvl(t.balance_netitem_amount,0) as netitem_amount, \n" );//其他费用
		sb.append("       nvl(t.free_m_price,0) as free_m_price, \n" );//保养费
		sb.append("       nvl(t.accessories_price,0) as accessories_price, \n" );//辅料费
		sb.append("       nvl(t.compensation_money,0) as compensation_money, \n" );//补偿费
		sb.append("       t.balance_amount \n" );
		
		
		//判断开始时间结束时间是否在当前月
		if(beginTime!=""||endTime!=""){
			try {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
				Date currentMonth = sdf.parse(sdf.format(new Date()));
				int flag=0;
				
				if(beginTime.length()>0){
					Date beginMonth=sdf.parse(beginTime.substring(0,7));
					if(beginMonth.compareTo(currentMonth)==0 ){
						flag=1;
					}
				}
				if(endTime.length()>0){
					Date endMonth = sdf.parse(endTime.substring(0,7));
					if(endMonth.compareTo(currentMonth)==0 ){
						flag=1;
					}
				}
				if(flag==1){
					sb.append("  from tt_as_wr_application t, tm_dealer w ,vw_material_group_service vw,  tm_vehicle vv\n" );
				}else{
					sb.append("  from tt_as_wr_application_fixed t, tm_dealer w ,vw_material_group_service vw,  tm_vehicle vv\n" );
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			sb.append("  from tt_as_wr_application_fixed t, tm_dealer w ,vw_material_group_service vw,  tm_vehicle vv\n" );
		}
		sb.append(" where w.dealer_id = t.dealer_id and t.vin=vv.vin and vw.package_id=vv.package_id\n" );
		sb.append("   and t.claim_type in\n" );
		sb.append("       (10661001, 10661006, 10661007, 10661012, 10661010, 10661009) and t.status>=10791007 and  t.is_import=10041002\n" );
		if (this.getPoseRoleId(loginUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
		       sb.append(CommonUtils.getOrgDealerLimitSqlByPose("w", loginUser));
		}
		String claim_type = DaoFactory.getParam(request, "claim_type");
		String dealerCode = DaoFactory.getParam(request, "dealerCode");
		String carModel = DaoFactory.getParam(request, "carModel");
		DaoFactory.getsql(sb, "t.claim_type", claim_type, 1);
		DaoFactory.getsql(sb, "w.dealer_code", dealerCode, 6);
		DaoFactory.getsql(sb, "vw.model_code", DaoFactory.getSqlByarrIn(carModel), 6);
		DaoFactory.getsql(sb, "t.report_date",beginTime, 3);
		DaoFactory.getsql(sb, "t.report_date", endTime, 4);
		DaoFactory.getsql(sb, "t.campaign_code", DaoFactory.getParam(request, "activity_code"), 1);
		String part_code = DaoFactory.getParam(request, "part_code");
		String producer_code = DaoFactory.getParam(request, "producer_code");
		String labour_code = DaoFactory.getParam(request, "labour_code");
		if(BaseUtils.testString(part_code)){
			sb.append("   and t.id in (select p.id from Tt_As_Wr_Partsitem p where 1=1 \n" );
			DaoFactory.getsql(sb, "p.part_code", part_code, 2);
			sb.append("     )\n" );
		}
		if(BaseUtils.testString(producer_code)){
			sb.append("   and t.id in (select p.id from Tt_As_Wr_Partsitem p where 1=1 \n" );
			DaoFactory.getsql(sb, "p.producer_code", producer_code, 2);
			sb.append("     )\n" );
		}
		if(BaseUtils.testString(labour_code)){
			sb.append("   and t.id in (select l.id from Tt_As_Wr_Labouritem l where 1=1 \n" );
			DaoFactory.getsql(sb, "l.labour_code", labour_code, 2);
			sb.append("     )\n" );
		}
		sb.append(")");
		
		sb.append(" select   \n");
		sb.append("  sum(balance_amount) as balance_amount, \n");
		sb.append("  sum(compensation_money) as compensation_money, \n");
		sb.append("  sum(labour_amount) as balance_labour_amount, \n");
		sb.append("  sum(part_amount) as balance_part_amount, \n");
		sb.append("  sum(netitem_amount) as balance_netitem_amount, \n");
		sb.append("  sum(free_m_price) as free_m_price, \n");
		sb.append("  sum(accessories_price) as accessories_price \n");
		sb.append("  from tb_1 \n");
		
		
		Map<String, Object> list= dao.pageQueryMap(sb.toString(), null, getFunName());
		return list;
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object>  report2Sum(RequestWrapper request,AclUserBean logonUser,int pageSize,int currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select sum(nvl(a.free_m_price, 0)) as free_m_price, \n");
		sb.append("sum(nvl(a.amount, 0)) as amount,count(1) DEALER_NUM from (select \n" );
		sb.append("       sum(nvl(t.free_m_price, 0)) as free_m_price,\n" );
		sb.append("       sum(nvl(m.amount, 0)) as amount,\n" );
		sb.append("       t.dealer_id\n" );
		sb.append("  from tt_as_wr_application t,\n" );
		sb.append("       Tt_As_Wr_Netitem     m\n" );
		sb.append(" where 1=1\n" );
	   if (this.getPoseRoleId(logonUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
           sb.append(CommonUtils.getOrgDealerLimitSqlByPose("t", logonUser));
       }

		sb.append("   and t.id = m.id(+)\n" );
		sb.append("   and t.claim_type in (10661002, 10661011)\n" );
		sb.append("   and t.status >= 10791007 and  t.is_import=10041002");

		String beginTime = DaoFactory.getParam(request, "beginTime");
		String endTime = DaoFactory.getParam(request, "endTime");
		String claim_type = DaoFactory.getParam(request, "claim_type");
		DaoFactory.getsql(sb, "t.report_date",beginTime, 3);
		DaoFactory.getsql(sb, "t.report_date", endTime, 4);
		DaoFactory.getsql(sb, "t.claim_type", claim_type, 1);
		sb.append("   group by t.dealer_id ) a");
		
		Map<String, Object> list= dao.pageQueryMap(sb.toString(), null, getFunName());
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object>  report3Sum(RequestWrapper request,int pageSize,int currPage) {
		String startDate=request.getParamValue("startDate");
		String endDate=request.getParamValue("endDate");
		
		StringBuffer sb= new StringBuffer();
		sb.append("with table1  as( select t.*,(t.achiveNumself + t.helpAchive) as repairNum \n" );
		sb.append("  from (select vd.root_org_name as org_name,\n" );
		sb.append("               vd.region_name,\n" );
		sb.append("               vd.dealer_id,\n" );
		sb.append("               vd.dealer_code,\n" );
		sb.append("               vd.dealer_name as dealer_shortname,\n" );
		sb.append("               arc.activity_code,\n" );
		sb.append("               arc.activity_id,\n" );
		sb.append("               (select v.activity_name\n" );
		sb.append("                  from Tt_As_Activity v\n" );
		sb.append("                 where v.activity_id = arc.activity_id) as activity_name,\n" );
		sb.append("               count(arc.vin) as countAllByDealer, --服务站分配数量\n" );
		sb.append("               (select count(*)\n" );
		sb.append("                  from tt_as_wr_application awa\n" );
		sb.append("                 where awa.dealer_id = vd.dealer_id\n" );
		sb.append("                   and awa.vin in\n" );
		sb.append("                       (select ar.vin\n" );
		sb.append("                          from tt_as_activity_relation_code ar\n" );
		sb.append("                         where ar.activity_id = arc.activity_id\n" );
		sb.append("                           and ar.dealer_code = vd.dealer_code)\n" );
		sb.append("                   and awa.campaign_code = arc.activity_code\n" );
		sb.append("                   and awa.claim_type = 10661006\n" );
		sb.append("                   and awa.status not in\n" );
		sb.append("                       (10791001, 10791002, 10791003, 10791005, 10791006)) as achiveNumself, --自店完成数\n" );
		sb.append("               (select count(*)\n" );
		sb.append("                  from tt_as_wr_application awa\n" );
		sb.append("                 where awa.dealer_id <> vd.dealer_id\n" );
		sb.append("                   and awa.vin  in\n" );
		sb.append("                       (select ar.vin\n" );
		sb.append("                          from tt_as_activity_relation_code ar\n" );
		sb.append("                         where ar.activity_id = arc.activity_id\n" );
		sb.append("                           and ar.dealer_code = vd.dealer_code)\n" );
		sb.append("                   and awa.campaign_code = arc.activity_code\n" );
		sb.append("                   and awa.claim_type = 10661006\n" );
		sb.append("                   and awa.status not in\n" );
		sb.append("                       (10791001, 10791002, 10791003, 10791005, 10791006)) as helpAchiveTemp, --别人对他自店完成数\n" );
		sb.append("               (select count(*)\n" );
		sb.append("                  from tt_as_wr_application awa\n" );
		sb.append("                 where awa.dealer_id = vd.dealer_id\n" );
		sb.append("                   and awa.vin in\n" );
		sb.append("                       (select ar.vin\n" );
		sb.append("                          from tt_as_activity_relation_code ar\n" );
		sb.append("                         where ar.activity_id = arc.activity_id\n" );
		sb.append("                           and ar.dealer_code <> vd.dealer_code)\n" );
		sb.append("                   and awa.campaign_code = arc.activity_code\n" );
		sb.append("                   and awa.claim_type = 10661006\n" );
		sb.append("                   and awa.status not in\n" );
		sb.append("                       (10791001, 10791002, 10791003, 10791005, 10791006)) as helpAchive --他对别人帮助完成数\n" );
		sb.append("          from tt_as_activity_relation_code arc,\n" );
		sb.append("               vw_org_dealer_service        vd,\n" );
		sb.append("               tt_as_activity               a\n" );
		sb.append("         where arc.dealer_code = vd.dealer_code\n" );
		sb.append("           and arc.activity_id = a.activity_id\n" );
		sb.append("           and a.status in (10681002, 10681004)\n" );
		if(startDate!=null){
			sb.append("   and arc.create_date >= to_date('"+startDate+"','yyyy-MM-dd') \n");
		}
		if(endDate!=null){
			sb.append(" and to_date(to_char(arc.create_date,'yyyy-MM-dd'),'yyyy-MM-dd') <= to_date('"+endDate+"','yyyy-MM-dd') \n");
		}
		sb.append("         group by vd.root_org_name,\n" );
		sb.append("                  vd.region_name,\n" );
		sb.append("                  vd.dealer_id,\n" );
		sb.append("                  vd.dealer_code,\n" );
		sb.append("                  vd.dealer_name,\n" );
		sb.append("                  arc.activity_code,\n" );
		sb.append("                  arc.activity_id) t where 1=1 \n" );
		String dealerCode = DaoFactory.getParam(request, "dealerCode");
		DaoFactory.getsql(sb, "t.dealer_code", dealerCode, 6);
		DaoFactory.getsql(sb, "t.activity_code", DaoFactory.getParam(request, "activity_code"), 1);
		sb.append(" and  t.countAllByDealer <> 0 )  select  sum(t.repairNum) as achiveNum,sum(t.countAllByDealer) as countAll,sum(t.countAllByDealer-t.repairNum) as notachivenum, ");
		sb.append("   round(sum(t.repairNum)/sum(t.countAllByDealer), 2) * 100 || '%' as percentAll  from table1 t ");

		Map<String, Object> list= dao.pageQueryMap(sb.toString(), null, getFunName());
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object>  report4Sum(RequestWrapper request,int pageSize,int currPage) {
		String startdate =DaoFactory.getParam(request, "beginTime");
		String enddate =DaoFactory.getParam(request, "endTime");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date startdate1=null;
		Date enddate1=null;
		Date onlineDate=null;
		try {
			 startdate1 =dateFormat.parse(startdate);
			 enddate1 =dateFormat.parse(enddate);
			 onlineDate =dateFormat.parse(MyConstant.onlineDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Long start = startdate1.getTime();
		Long end = enddate1.getTime();
		Long online = onlineDate.getTime();
		StringBuffer sb= new StringBuffer();
		sb.append("with tb_1 as ( select t.ro_no,\n" );
		sb.append("       t.status,\n" );
		sb.append("       t.id,\n" );
		sb.append("       case\n" );
		sb.append("         when t.campaign_code is not null then\n" );
		sb.append("          nvl(t.balance_amount, 0)\n" );
		sb.append("         else\n" );
		sb.append("          0\n" );
		sb.append("       end as activity_amount,\n" );
		sb.append("       to_char(t.report_date, 'yyyy-mm-dd') as report_date,\n" );
		sb.append("       (select tm.dealer_code\n" );
		sb.append("          from tm_dealer tm\n" );
		sb.append("         where tm.dealer_id = t.dealer_id) as dealer_code,\n" );
		sb.append("       (select tm.dealer_shortname\n" );
		sb.append("          from tm_dealer tm\n" );
		sb.append("         where tm.dealer_id = t.dealer_id) as dealer_shortname,\n" );
		sb.append("       t.claim_no,\n" );
		sb.append("       (select c.code_desc from tc_code c where c.code_id = t.claim_type) as claim_type,\n" );
		sb.append("       t.vin,\n" );
		sb.append("       t.campaign_code cam_code,\n" );
		sb.append("       t.model_name,\n" );
		sb.append("       to_char(t.ro_startdate, 'yyyy-mm-dd') as ro_startdate,\n" );
		sb.append("       p.part_code,\n" );
		sb.append("       p.producer_code,\n" );
		sb.append("       p.part_name,\n" );
		sb.append("       p.remark,\n" );
		sb.append("       p.down_part_code,\n" );
		sb.append("       nvl(p.balance_amount, 0) as balance_amount1,\n" );
		if (start<=online && end<=online) {//新分单时间节点
			sb.append("       (select nvl(l.balance_amount, 0)\n" );
			sb.append("          from tt_as_wr_labouritem l\n" );
			sb.append("         where l.id = t.id\n" );
			sb.append("           and l.wr_labourcode = p.wr_labourcode) as balance_amount2,\n" );
			}else if (start>online && end>online) {
				sb.append(" nvl((select sum(awl.balance_amount)  from tt_as_wr_labouritem awl where awl.id = t.id),0) as balance_amount2,\n" );
			}
		sb.append("       nvl(t.balance_netitem_amount, 0) as netitem_amount,\n" );
		sb.append("       nvl(t.accessories_price, 0) as accessories_price,\n" );
		sb.append("       nvl(t.compensation_money, 0) as compensation_money,\n" );
		sb.append("\n" );
		sb.append("       nvl(p.balance_amount, 0) +\n" );
		
		if (start<=online && end<=online) {//新分单时间节点
			sb.append("       (select nvl(l.balance_amount, 0)\n" );
			sb.append("          from tt_as_wr_labouritem l\n" );
			sb.append("         where l.id = t.id\n" );
			sb.append("           and l.wr_labourcode = p.wr_labourcode) +\n" );
			}else if (start>online && end>online) {
				sb.append(" nvl((select sum(awl.balance_amount)  from tt_as_wr_labouritem awl where awl.id = t.id),0) +\n" );
			}
		sb.append("       nvl(t.balance_netitem_amount, 0) + nvl(t.accessories_price, 0) +\n" );
		sb.append("       nvl(t.compensation_money, 0) as countAll\n" );
		sb.append("  from Tt_As_Wr_Partsitem p, tm_dealer tm, tt_as_wr_application t\n" );
		sb.append(" where 1 = 1\n" );
		sb.append("   and t.dealer_id = tm.dealer_id\n" );
		sb.append("   and p.id = t.id\n" );
		sb.append("   and (p.main_part_code = '无' or p.main_part_code = '-1')\n" );
		sb.append("   and t.status > 10791001\n" );
		sb.append("   and t.is_import = 10041002");
		String beginTime = DaoFactory.getParam(request, "beginTime");
		String endTime = DaoFactory.getParam(request, "endTime");
		String claim_no = DaoFactory.getParam(request, "claim_no");
		String part_code = DaoFactory.getParam(request, "part_code");
		String down_part_code = DaoFactory.getParam(request, "down_part_code");
		String claim_type = DaoFactory.getParam(request, "claim_type");
		String status = DaoFactory.getParam(request, "status");
		String dealerCode = DaoFactory.getParam(request, "dealerCode");
		DaoFactory.getsql(sb, "t.report_date",beginTime, 3);
		DaoFactory.getsql(sb, "t.report_date", endTime, 4);
		DaoFactory.getsql(sb, "t.claim_no", claim_no, 2);
		DaoFactory.getsql(sb, "p.part_code", part_code, 2);
		DaoFactory.getsql(sb, "p.down_part_code", down_part_code, 2);
		DaoFactory.getsql(sb, "tm.dealer_code", dealerCode, 6);
		DaoFactory.getsql(sb, "t.claim_type", claim_type, 1);
		DaoFactory.getsql(sb, "t.status", status, 1);
		sb.append(")");
		sb.append(" select   \n");
		sb.append("  sum(activity_amount) as activity_amount, \n");
		sb.append("  sum(balance_amount1) as balance_amount1, \n");
		sb.append("  sum(balance_amount2) as balance_amount2, \n");
		sb.append("  sum(netitem_amount) as netitem_amount, \n");
		sb.append("  sum(accessories_price) as accessories_price, \n");
		sb.append("  sum(compensation_money) as compensation_money, \n");
		sb.append("  sum(countAll) as countAll \n");
		sb.append("  from tb_1 \n");
		
		Map<String, Object> list= dao.pageQueryMap(sb.toString(), null, getFunName());
		return list;
	}
	/**
	 * 插入标签操作
	 * @param loginUser
	 */
	@SuppressWarnings("unchecked")
	public void expotData11(AclUserBean loginUser) {
		int temp=0;
		DealerClaimReportDao dao=new DealerClaimReportDao();
		List<Map<String, Object>> pageQuery = dao.pageQuery("select c.* from  ccc c",null, getFunName());
		for (Map<String, Object> map : pageQuery) {
			BigDecimal b= (BigDecimal) map.get("ID");
			long longValue = b.longValue();
			dao.oldUpdateIdea(String.valueOf(longValue),loginUser);
			temp++;
		}
		System.out.println(temp);
		}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> report11(RequestWrapper request,Integer page, Integer currPage, String year) {
		StringBuffer sb= new StringBuffer();
		sb.append("--年度二次索赔汇总\n" );
		sb.append("with s as\n" );
		sb.append(" (select to_char(t.audit_date, 'mm') || '月' ym,\n" );
		//sb.append(" (select to_char(t.audit_date, 'mm')  ym,\n" );
		sb.append("         sum(t.small_amount) small_amount\n" );
		sb.append("    from tt_as_wr_range_single t\n" );
		sb.append("   where trunc(t.audit_date) between\n" );
		sb.append("         to_date("+year+" || '-01-01', 'yyyy-mm-dd') and\n" );
		sb.append("         to_date("+year+" || '-12-31', 'yyyy-mm-dd')\n" );
		sb.append("   group by to_char(t.audit_date, 'mm') || '月'\n" );
		sb.append("   order by to_char(t.audit_date, 'mm') || '月' asc)\n" );
		sb.append("select *\n" );
		sb.append("  from s\n" );
		sb.append("union all--结果汇总\n" );
		sb.append("select '合计', sum(s.small_amount) from s");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public void expotData11(ActionContext act,PageResult<Map<String, Object>> list) {
		try {
			
			String[] head=new String[13];
			head[0]="月份";
			head[1]="二次索赔金额";
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[13];
					detail[0]=BaseUtils.checkNull(map.get("YM"));
					detail[1]=BaseUtils.checkNull(map.get("SMALL_AMOUNT"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出二次索赔报表"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> report17(RequestWrapper request,
			Integer page, Integer currPage, Map<String,String> params) {
		StringBuffer sb= new StringBuffer();
		sb.append("--IPTV/3CS报表sql（市场质量部）\n" );
		sb.append("select SUBSTR(vdate,1,4) as vdateyear,--年月\n" );
		sb.append("      SUBSTR(vdate,6,7) as vdatemonth,--车型\n" );
		sb.append("       sum(pcount) pcount, --生产数量\n" );
		sb.append("       sum(scount) scount, --销售数量\n" );
		sb.append("       sum(rcount) rcount, --索赔数量\n" );
		sb.append("       case sum(scount) when 0 then 0 else trunc(sum(rcount) / sum(scount) * 1000, 2) end pt --IPTV/3CS\n" );
		sb.append("  from (with vd as (select to_char(to_date( '"+params.get("startDate")+"','yyyy-mm') + rownum - 1, 'yyyy-mm') vdate, --生成月份列表\n" );
		sb.append("                           count(1)\n" );
		sb.append("                      from user_objects\n" );
		sb.append("                     where rownum <= (to_date( '"+params.get("endDate")+"', 'yyyy-mm') - to_date('"+params.get("startDate")+"', 'yyyy-mm') + 1)\n" );
		sb.append("                     group by to_char(to_date('"+params.get("startDate")+"', 'yyyy-mm') + rownum - 1, 'yyyy-mm'))\n" );
		sb.append("         --按月份统计生产数量，取值逻辑生产时间在当前月之前N-1月内生产的车辆，N为变量输入\n" );
		sb.append("         select vd.vdate vdate, count（ vv.vin ）pcount, 0 scount, 0 rcount\n" );
		sb.append("           from tm_vehicle vv, vd\n" );
		sb.append("          where 1=1 ");
		sb.append("          and  trunc(vv.product_date) between\n" );
		if(params.get("N")!=null){
			sb.append("                last_day(add_months(to_date(vd.vdate, 'yyyy-mm'), -"+params.get("N")+")) + 1 and\n" );
			sb.append("                last_day(add_months(to_date(vd.vdate, 'yyyy-mm'), -0))\n" );
		}
		if(params.get("model")!=null){
			sb.append("            and vv.model_id in ("+params.get("model")+")\n" );
		}
		sb.append("          group by vd.vdate\n" );
		sb.append("         union all\n" );
		sb.append("         --按月份统计销售数量，生产数量统计范围内的车辆中在当月实销的数量\n" );
		sb.append("         select vd.vdate vdate,\n" );
		sb.append("                0 pcount,\n" );
		sb.append("                count(das.vehicle_id) scount,\n" );
		sb.append("                0 rcount\n" );
		sb.append("           from tm_vehicle vv, tt_dealer_actual_sales das, vd\n" );
		sb.append("          where trunc(das.sales_date) between\n" );
		sb.append("                last_day(add_months(to_date(vd.vdate, 'yyyy-mm'), -1)) + 1 and\n" );
		sb.append("                last_day(add_months(to_date(vd.vdate, 'yyyy-mm'), -0))\n" );
		sb.append("            and vv.life_cycle = 10321004\n" );
		sb.append("            and vv.vehicle_id = das.vehicle_id\n" );
		if(params.get("N")!=null){
		sb.append("            and trunc(vv.product_date) between\n" );
		sb.append("                last_day(add_months(to_date(vd.vdate, 'yyyy-mm'), -"+params.get("N")+"  )) + 1 and\n" );
		sb.append("                last_day(add_months(to_date(vd.vdate, 'yyyy-mm'), -0))\n" );
		}
		
		if(params.get("model")!=null){
			sb.append("            and vv.model_id in ("+params.get("model")+")\n" );
		}
		sb.append("          group by vd.vdate\n" );
		sb.append("         union all\n" );
		sb.append("         --按月份统计索赔数量，销售数量统计范围内的车辆在当前月之后N-1月内发生的审核通过的索赔单次数\n" );
		sb.append("         select vd.vdate vdate, 0 pcount, 0 scount, count（ap.id） rcount\n" );
		sb.append("           from tm_vehicle             vv,\n" );
		sb.append("                tt_dealer_actual_sales das,\n" );
		sb.append("                tt_as_wr_application   ap,\n" );
		sb.append("                vd\n" );
		sb.append("          where 1=1 ");
		
		if(params.get("math")!=null){
			if(params.get("math").equals("TPTV")){
				
				Integer n=Integer.parseInt(params.get("N"))-1;
				sb.append(" and trunc(ap.report_date) between\n" );
				sb.append("                last_day(add_months(to_date(vd.vdate, 'yyyy-mm'), -1)) + 1 \n" );
				sb.append("            and last_day(add_months(to_date(vd.vdate, 'yyyy-mm'), +"+n+"))\n" );
			}else{
				Integer a=Integer.parseInt(params.get("A"))-1;
				sb.append(" and trunc(ap.report_date) between\n" );
				sb.append("                last_day(add_months(to_date(vd.vdate, 'yyyy-mm'), -1)) + 1 \n" );
				sb.append("            and last_day(add_months(to_date(vd.vdate, 'yyyy-mm'), +"+a+"))\n" );
			}
		}
		sb.append("            and ap.status = 10791008\n" );
		sb.append("            and vv.vin = ap.vin\n" );
		sb.append("            and vv.life_cycle = 10321004\n" );
		sb.append("            and vv.vehicle_id = das.vehicle_id\n" );
		sb.append("            and trunc(vv.product_date) between\n" );
		if(params.get("N")!=null){
			Integer n=Integer.parseInt(params.get("N"))+1;
		sb.append("                last_day(add_months(to_date(vd.vdate, 'yyyy-mm'), -"+n+")) + 1 \n" );
		}
		sb.append("            and last_day(add_months(to_date(vd.vdate, 'yyyy-mm'), -0))\n" );
		sb.append("            and trunc(das.sales_date) between\n" );
		sb.append("                last_day(add_months(to_date(vd.vdate, 'yyyy-mm'), -1)) + 1 and\n" );
		sb.append("                last_day(add_months(to_date(vd.vdate, 'yyyy-mm'), -0))\n" );
		if(params.get("model")!=null){
		sb.append("            and vv.model_id in ("+params.get("model")+")\n" );
		}
		sb.append("          group by vd.vdate)\n" );
		sb.append("          group by vdate\n" );
		sb.append("          order by vdate");

		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> CPVReport(RequestWrapper request,
			Integer page, Integer currPage, Map<String, String> params) {
		StringBuffer sb= new StringBuffer();
		sb.append("--CPV报表（市场质量部）SQL\n" );
		sb.append("select SUBSTR(vdate,1,4) as vdateyear,--年月\n" );
		sb.append("      SUBSTR(vdate,6,7) as vdatemonth,--车型\n" );
		sb.append("       group_name,--车型\n" );
		sb.append("       sum(vcount) vcount,--在保台数\n" );
		sb.append("       sum(ramount) rmount,--索赔费用\n" );
		sb.append("       case sum(vcount) when 0 then 0 else trunc(sum(ramount)/sum(vcount),2) end cpv\n" );
		sb.append(" from （with vd as (select to_char(to_date('"+params.get("startDate")+"', 'yyyy-mm') + rownum - 1,'yyyy-mm') vdate,--生成查询年月清单\n" );
		sb.append("                           count(1)\n" );
		sb.append("                      from user_objects\n" );
		sb.append("                     where rownum <= (to_date('"+params.get("endDate")+"', 'yyyy-mm') - to_date('"+params.get("startDate")+"', 'yyyy-mm') + 1)\n" );
		sb.append("                     group by to_char(to_date('"+params.get("startDate")+"', 'yyyy-mm') + rownum - 1, 'yyyy-mm'))\n" );
		sb.append("        --按月统计在保台数\n" );
		sb.append("        select vd.vdate vdate,vmg.group_name group_name,count(das.vehicle_id) vcount,0 ramount\n" );
		sb.append("          from tt_dealer_actual_sales das,tm_vehicle vv,tm_vhcl_material_group vmg,vd\n" );
		sb.append("         where vv.life_cycle=10321004\n" );
		sb.append("           and vv.vehicle_id=das.vehicle_id\n" );
		sb.append("           and vmg.group_id=vv.model_id\n" );
		sb.append("           and trunc(das.invoice_date)<=case vd.vdate when to_char(sysdate,'yyyy-mm') then trunc(sysdate) else last_day(to_date(vd.vdate,'yyyy-mm')) end\n" );
		sb.append("           and das.is_return=10041002\n" );
		sb.append("           and add_months(trunc(das.invoice_date),+"+params.get("sDate")+")-1>=case vd.vdate when to_char(sysdate,'yyyy-mm') then trunc(sysdate) else last_day(to_date(vd.vdate,'yyyy-mm')) end\n" );
		sb.append("           and vv.mileage<="+params.get("sMile")+"\n" );
		sb.append("           and vv.model_id in ("+params.get("model")+")\n" );
		sb.append("         group by vd.vdate,vmg.group_name\n" );
		sb.append("        union all\n" );
		sb.append("        --按月统计在保车辆的索赔费用（不含PDI、保养、厂家活动）\n" );
		sb.append("        select vd.vdate vdate,vmg.group_name group_name,0 vcount,trunc(sum(awp.balance_amount),2) ramount\n" );
		sb.append("          from tt_dealer_actual_sales das,tm_vehicle vv,tm_vhcl_material_group vmg,tt_as_wr_application awp,vd\n" );
		sb.append("         where awp.status in (10791007,10791008)\n" );
		sb.append("           and awp.claim_type in (10661001,10661007,10661009,10661010,10661012,10661013)\n" );
		sb.append("           and trunc(awp.report_date)<=case vd.vdate when to_char(sysdate,'yyyy-mm') then trunc(sysdate) else last_day(to_date(vd.vdate,'yyyy-mm')) end\n" );
		sb.append("           and awp.vin=vv.vin\n" );
		sb.append("           and vv.life_cycle=10321004\n" );
		sb.append("           and vv.vehicle_id=das.vehicle_id\n" );
		sb.append("           and vmg.group_id=vv.model_id\n" );
		sb.append("           and trunc(das.invoice_date)<=case vd.vdate when to_char(sysdate,'yyyy-mm') then trunc(sysdate) else last_day(to_date(vd.vdate,'yyyy-mm')) end\n" );
		sb.append("            and das.is_return=10041002\n" );
		sb.append("            and add_months(trunc(das.invoice_date),+"+params.get("sDate")+")-1>=case vd.vdate when to_char(sysdate,'yyyy-mm') then trunc(sysdate) else last_day(to_date(vd.vdate,'yyyy-mm')) end\n" );
		sb.append("            and vv.mileage<="+params.get("sMile")+"\n" );
		sb.append("            and vv.model_id in ("+params.get("model")+")\n" );
		sb.append("         group by vd.vdate,vmg.group_name）\n" );
		sb.append("         group by vdate,group_name\n" );
		sb.append("         order by vdate,group_name asc");

		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public String getCarId(String model) {
		String sql="select group_id from tm_vhcl_material_group where group_code ='"+model+"'";
		List<Map<String, Object>> strs=dao.pageQuery(sql, null, null);
		if(strs.size()>0){
			return  strs.get(0).get("GROUP_ID").toString();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> report12(RequestWrapper request,
			Integer page, Integer currPage, Map<String, String> params) {
		
		StringBuffer sb= new StringBuffer();
		sb.append("with s as\n" );
		sb.append(" (select t.out_no out_no,\n" );
		sb.append("         t.supply_code supply_code,\n" );
		sb.append("         t.supply_name supply_name,\n" );
		sb.append("         sum(t.print_part) part_amount,--材料费\n" );
		sb.append("         sum(t.related_losses) + sum(t.out_amount) + sum(t.labour_amount) labour_and_other,--工时及其他（工时、关联、外出）\n" );
		sb.append("         sum(t.small_amount) small_amount,--合计\n" );
		sb.append("         t.supply_name duty_supply_name\n" );
		sb.append("    from tt_as_wr_range_single t\n" );
		sb.append("   where trunc(t.audit_date) between\n" );
		sb.append("         last_day(add_months(to_date('"+params.get("date")+"', 'yyyy-mm'), -1)) + 1 and\n" );
		sb.append("         last_day(to_date('"+params.get("date")+"', 'yyyy-mm'))\n" );
		if(params.get("FaCode")!=null&&!params.get("FaCode").equals("")){
			sb.append("    and t.supply_code like '%"+params.get("FaCode")+"%'--供应商代码条件\n" );
		}
		if((params.get("FaShortName")!=null)&&!params.get("FaShortName").equals("")){
			sb.append("    and t.supply_name like '%"+params.get("FaShortName")+"%'--供应商简称条件\n" );
		}
		sb.append("   group by t.out_no, t.supply_code, t.supply_name)\n" );
		sb.append("select s.*, '' remark\n" );
		sb.append("  from s\n" );
		sb.append("union all--结果汇总\n" );
		sb.append("select '',\n" );
		sb.append("       '合计',\n" );
		sb.append("       '',\n" );
		sb.append("       sum(s.part_amount),\n" );
		sb.append("       sum(s.labour_and_other),\n" );
		sb.append("       sum(s.small_amount),\n" );
		sb.append("       '',\n" );
		sb.append("       ''\n" );
		sb.append("  from s");

		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> report13(RequestWrapper request,
			Integer page, Integer currPage, Map<String, String> params) {
		StringBuffer sb= new StringBuffer();
		sb.append("with s as\n" );
		sb.append(" (select t.out_no out_no,\n" );
		sb.append("         t.supply_code supply_code,\n" );
		sb.append("         t.supply_name supply_name,\n" );
		sb.append("         t.part_code part_code,\n" );
		sb.append("         t.part_name part_name,\n" );
		sb.append("         t.part_unit part_unit,\n" );
		sb.append("         sum(t.print_num) part_quantity,--数量\n" );
		sb.append("         sum(t.labour_amount) labour_amount,--工时金额\n" );
		sb.append("         sum(t.related_losses) related_losses,--关联损失\n" );
		sb.append("         sum(t.out_amount) out_amount,--外出费用\n" );
		sb.append("         sum(t.print_part) part_amount,--材料费\n" );
		sb.append("         sum(t.small_amount) small_amount,--合计\n" );
		sb.append("         t.supply_name duty_supply_name,t.remark\n" );
		sb.append("    from tt_as_wr_range_single t\n" );
		sb.append("   where trunc(t.audit_date) between\n" );
		sb.append("         last_day(add_months(to_date('"+params.get("date")+"', 'yyyy-mm'), -1)) + 1 and\n" );
		sb.append("         last_day(to_date('"+params.get("date")+"', 'yyyy-mm'))\n" );
		if(params.get("FaCode")!=null&&!params.get("FaCode").equals("")){
			sb.append("     and t.supply_code like '%"+params.get("FaCode")+"%'--供应商代码条件\n" );
		}
		if(params.get("FaShortName")!=null){
			sb.append("     and t.supply_name like '%"+params.get("FaShortName")+"%'--供应商简称条件\n" );
		}
		if((params.get("detailCode")!=null)&&!params.get("detailCode").equals("")){
			sb.append("     and t.part_code like '%"+params.get("detailCode")+"%' --配件代码条件\n" );
		}
		if((params.get("detailName")!=null)&&!params.get("detailName").equals("")){
			sb.append("     and t.part_name like '%"+params.get("detailName")+"%' --配件名称条件\n" );
		}
		sb.append("   group by t.out_no,\n" );
		sb.append("            t.supply_code,\n" );
		sb.append("            t.supply_name,\n" );
		sb.append("            t.part_code,\n" );
		sb.append("            t.part_name,\n" );
		sb.append("            t.part_unit,t.remark)\n" );
		sb.append("select s.* \n" );
		sb.append("  from s\n" );
		sb.append("union all--结果汇总\n" );
		sb.append("select '',\n" );
		sb.append("       '',\n" );
		sb.append("       '合计',\n" );
		sb.append("       '',\n" );
		sb.append("       '',\n" );
		sb.append("       '',\n" );
		sb.append("       sum(s.part_quantity),\n" );
		sb.append("       sum(s.labour_amount),\n" );
		sb.append("       sum(s.related_losses),\n" );
		sb.append("       sum(s.out_amount),\n" );
		sb.append("       sum(s.part_amount),\n" );
		sb.append("       sum(s.small_amount),\n" );
		sb.append("       '',\n" );
		sb.append("       ''\n" );
		sb.append("  from s");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public void expotData12(ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[13];
			head[0]="出库单号";
			head[1]="部件厂代码";
			head[2]="部件厂名称";
			head[3]="材料费";
			head[4]="工时及其他";
			head[5]="合计";
			head[6]="责任单位";
			head[7]="备注";
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[13];
					detail[0]=BaseUtils.checkNull(map.get("OUT_NO"));
					detail[1]=BaseUtils.checkNull(map.get("SUPPLY_CODE"));
					detail[2]=BaseUtils.checkNull(map.get("SUPPLY_NAME"));
					detail[3]=BaseUtils.checkNull(map.get("PART_AMOUNT"));
					detail[4]=BaseUtils.checkNull(map.get("LABOUR_AND_OTHER"));
					detail[5]=BaseUtils.checkNull(map.get("SMALL_AMOUNT"));
					detail[6]=BaseUtils.checkNull(map.get("DUTY_SUPPLY_NAME"));
					detail[7]=BaseUtils.checkNull(map.get("REMARK"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出部件厂月汇总表"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		
	}
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> reportOutStore(RequestWrapper request, AclUserBean loginUser, Integer page, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select c.* from (select t.range_no,\n" );
		sb.append("       t.supply_code,\n" );
		sb.append("       t.supply_name,\n" );
		sb.append("       t.out_part_code,\n" );
		sb.append("       t.out_part_name,\n" );
		sb.append("       t.claim_no,\n" );
		sb.append("       (select a.vin\n" );
		sb.append("          from tt_as_wr_application a\n" );
		sb.append("         where a.claim_no = t.claim_no) as vin,\n" );
		sb.append("       t.out_amout,\n" );
		sb.append("       (select p.trouble_reason\n" );
		sb.append("          from tt_as_wr_partsitem p\n" );
		sb.append("         where p.id in (select a.id\n" );
		sb.append("                          from tt_as_wr_application a\n" );
		sb.append("                         where a.claim_no = t.claim_no)\n" );
		sb.append("           and p.down_part_code = t.out_part_code) as trouble_reason,\n" );
		sb.append("       (select p.remark\n" );
		sb.append("          from tt_as_wr_partsitem p\n" );
		sb.append("         where p.id in (select a.id\n" );
		sb.append("                          from tt_as_wr_application a\n" );
		sb.append("                         where a.claim_no = t.claim_no)\n" );
		sb.append("           and p.down_part_code = t.out_part_code) as remark,\n" );
		sb.append("           t.out_date\n" );
		sb.append("  from Tt_As_Wr_Old_Out_Part t,\n" );
		sb.append("       (SELECT out_no, out_type\n" );
		sb.append("          FROM TT_as_WR_range_single\n" );
		sb.append("         GROUP BY out_no, out_type) s\n" );
		sb.append(" where t.out_no = s.out_no and t.range_no is not null ) c where 1=1 ");

		DaoFactory.getsql(sb, "c.supply_code", DaoFactory.getParam(request, "supply_code"), 2);
		DaoFactory.getsql(sb, "c.supply_name", DaoFactory.getParam(request, "supply_name"), 2);
		DaoFactory.getsql(sb, "c.out_part_code", DaoFactory.getParam(request, "out_part_code"), 2);
		DaoFactory.getsql(sb, "c.out_part_name", DaoFactory.getParam(request, "out_part_name"), 2);
		DaoFactory.getsql(sb, "c.claim_no", DaoFactory.getParam(request, "claim_no"), 2);
		DaoFactory.getsql(sb, "c.range_no", DaoFactory.getParam(request, "range_no"), 2);
		DaoFactory.getsql(sb, "c.out_date", DaoFactory.getParam(request, "out_date_start"), 3);
		DaoFactory.getsql(sb, "c.out_date", DaoFactory.getParam(request, "out_date_end"), 4);
		
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}

	
	@SuppressWarnings("unchecked")
	public void expotData13(ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[20];
			head[0]="出库单号";
			head[1]="部件厂代码";
			head[2]="部件厂名称";
			head[3]="零部件代码";
			head[4]="零部件名称";
			head[5]="单位";
			head[6]="数量";
			head[7]="工时金额";
			head[8]="关联损失";
			head[9]="外出费用";
			head[10]="材料费";
			head[11]="合计";
			head[12]="备注";
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[20];
					detail[0]=BaseUtils.checkNull(map.get("OUT_NO"));
					detail[1]=BaseUtils.checkNull(map.get("SUPPLY_CODE"));
					detail[2]=BaseUtils.checkNull(map.get("SUPPLY_NAME"));
					detail[3]=BaseUtils.checkNull(map.get("PART_CODE"));
					detail[4]=BaseUtils.checkNull(map.get("PART_NAME"));
					detail[5]=BaseUtils.checkNull(map.get("PART_UNIT"));
					detail[6]=BaseUtils.checkNull(map.get("PART_QUANTITY"));
					detail[7]=BaseUtils.checkNull(map.get("LABOUR_AMOUNT"));
					detail[8]=BaseUtils.checkNull(map.get("RELATED_LOSSES"));
					detail[9]=BaseUtils.checkNull(map.get("OUT_AMOUNT"));
					detail[10]=BaseUtils.checkNull(map.get("PART_AMOUNT"));
					detail[11]=BaseUtils.checkNull(map.get("SMALL_AMOUNT"));
					detail[12]=BaseUtils.checkNull(map.get("REMARK"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出零部件月汇总表"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		
	}
	
	
	@SuppressWarnings("unchecked")
	public void expotData14(ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[20];
			head[0]="出库单号";
			head[1]="部件厂代码";
			head[2]="部件厂名称";
			head[3]="零部件代码";
			head[4]="零部件名称";
			head[5]="单位";
			head[6]="数量";
			head[7]="工时金额";
			head[8]="关联损失";
			head[9]="外出费用";
			head[10]="材料费";
			head[11]="合计";
			head[12]="是否估价";
			head[13]="备注";
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[20];
					detail[0]=BaseUtils.checkNull(map.get("OUT_NO"));
					detail[1]=BaseUtils.checkNull(map.get("SUPPLY_CODE"));
					detail[2]=BaseUtils.checkNull(map.get("SUPPLY_NAME"));
					detail[3]=BaseUtils.checkNull(map.get("PART_CODE"));
					detail[4]=BaseUtils.checkNull(map.get("PART_NAME"));
					detail[5]=BaseUtils.checkNull(map.get("PART_UNIT"));
					detail[6]=BaseUtils.checkNull(map.get("PART_QUANTITY"));
					detail[7]=BaseUtils.checkNull(map.get("LABOUR_AMOUNT"));
					detail[8]=BaseUtils.checkNull(map.get("RELATED_LOSSES"));
					detail[9]=BaseUtils.checkNull(map.get("OUT_AMOUNT"));
					detail[10]=BaseUtils.checkNull(map.get("PART_AMOUNT"));
					detail[11]=BaseUtils.checkNull(map.get("SMALL_AMOUNT"));
					detail[12]=BaseUtils.checkNull(map.get("IS_ASS"));
					detail[13]=BaseUtils.checkNull(map.get("REMARK"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出无价和暂估二次索赔明细报表"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void expotReportOutStore(ActionContext act,PageResult<Map<String, Object>> list) {
		try {
			String[] head={"退赔单号","供应商代码","供应商名称","零件号","零件名称","索赔单号","车架号","数量","故障现象","原因分析","车辆购车日期","行驶里程"," 故障描述","故障原因","出库时间"};
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String RANGE_NO = BaseUtils.checkNull(map.get("RANGE_NO"));
					String SUPPLY_CODE = BaseUtils.checkNull(map.get("SUPPLY_CODE"));
					String SUPPLY_NAME = BaseUtils.checkNull(map.get("SUPPLY_NAME"));
					String OUT_PART_CODE = BaseUtils.checkNull(map.get("OUT_PART_CODE"));
					String OUT_PART_NAME = BaseUtils.checkNull(map.get("OUT_PART_NAME"));
					String CLAIM_NO = BaseUtils.checkNull(map.get("CLAIM_NO"));
					String VIN = BaseUtils.checkNull(map.get("VIN"));
					String OUT_AMOUT = BaseUtils.checkNull(map.get("OUT_AMOUT"));
					String TROUBLE_REASON = BaseUtils.checkNull(map.get("TROUBLE_REASON"));
					String REMARK = BaseUtils.checkNull(map.get("REMARK"));
					String OUT_DATE = BaseUtils.checkNull(map.get("OUT_DATE"));
					
					String[] detail={RANGE_NO,SUPPLY_CODE,SUPPLY_NAME,OUT_PART_CODE,OUT_PART_NAME,CLAIM_NO,VIN,OUT_AMOUT,TROUBLE_REASON,REMARK,OUT_DATE};
					params.add(detail);
				}
			}
			this.toExcel(act, head, params,null,"出库明细报表");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> report14(RequestWrapper request,
			Integer page, Integer currPage, Map<String, String> params) {
		StringBuffer sb= new StringBuffer();
		sb.append("--无价和暂估二次索赔明细报表(年.月.日-年.月.日）\n" );
		sb.append("with s as\n" );
		sb.append(" (select t.out_no out_no,\n" );
		sb.append("         t.supply_code supply_code,\n" );
		sb.append("         t.supply_name supply_name,\n" );
		sb.append("         t.part_code part_code,\n" );
		sb.append("         t.part_name part_name,\n" );
		sb.append("         t.part_unit part_unit,\n" );
		sb.append("         sum(t.print_num) part_quantity,\n" );
		sb.append("         sum(t.labour_amount) labour_amount,\n" );
		sb.append("         sum(t.related_losses) related_losses,\n" );
		sb.append("         sum(t.out_amount) out_amount,\n" );
		sb.append("         sum(t.print_part) part_amount,\n" );
		sb.append("         sum(t.small_amount) small_amount,\n" );
		sb.append("         t.supply_name duty_supply_name\n" );
		sb.append("    from tt_as_wr_range_single t\n" );
		sb.append("   where 1=1 ");
		if((params.get("startDate")!=null&&!params.get("startDate").equals("") )&&   (params.get("endDate")!=null&&!params.get("endDate").equals("") )  ){
			sb.append("		    and  trunc(t.create_date) between\n" );
			sb.append("         to_date('"+params.get("startDate")+"', 'yyyy-mm-dd') and\n" );
			sb.append("         to_date('"+params.get("endDate")+"', 'yyyy-mm-dd')\n" );
		}
		sb.append("         and t.audit_date is null\n" );
		sb.append("         and ((t.part_quantity<>0 or t.part_quantity is not null) or (t.print_num<>0 or t.print_num is not null))\n" );
		sb.append("         and ((t.part_amount=0 or t.part_amount is null) or (t.print_part=0 or t.print_part is null))\n" );
		if(params.get("FaCode")!=null&&!params.get("FaCode").equals("")){
			sb.append("     and t.supply_code like '%"+params.get("FaCode")+"%'--供应商代码条件\n" );
		}
		if((params.get("FaShortName")!=null)&&!params.get("FaShortName").equals("")){
			sb.append("     and t.supply_name like '%"+params.get("FaShortName")+"%'--供应商简称条件\n" );
		}
		if((params.get("detailCode")!=null)&&!params.get("detailCode").equals("")){
			sb.append("     and t.part_code like '%"+params.get("detailCode")+"%' --配件代码条件\n" );
		}
		if((params.get("detailName")!=null)&&!params.get("detailName").equals("")){
			sb.append("     and t.part_name like '%"+params.get("detailName")+"%' --配件名称条件\n" );
		}
		sb.append("   group by t.out_no,\n" );
		sb.append("            t.supply_code,\n" );
		sb.append("            t.supply_name,\n" );
		sb.append("            t.part_code,\n" );
		sb.append("            t.part_name,\n" );
		sb.append("            t.part_unit)\n" );
		sb.append("select s.*, '' is_ass,'' remark\n" );
		sb.append("  from s\n" );
		sb.append("union all--结果汇总\n" );
		sb.append("select '',\n" );
		sb.append("       '',\n" );
		sb.append("       '合计',\n" );
		sb.append("       '',\n" );
		sb.append("       '',\n" );
		sb.append("       '',\n" );
		sb.append("       sum(s.part_quantity),\n" );
		sb.append("       sum(s.labour_amount),\n" );
		sb.append("       sum(s.related_losses),\n" );
		sb.append("       sum(s.out_amount),\n" );
		sb.append("       sum(s.part_amount),\n" );
		sb.append("       sum(s.small_amount),\n" );
		sb.append("       '',\n" );
		sb.append("       '',\n" );
		sb.append("       ''\n" );
		sb.append("  from s");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> report15(RequestWrapper request,
			Integer page, Integer currPage, String year) {
		StringBuffer sb= new StringBuffer();
		sb.append("select *\n" );
		sb.append("  from (with a as --入库数量\n" );
		sb.append("        (select to_char(t.in_date, 'mm') || '月' ym,\n" );
		sb.append("                sum(t.sign_amount) in_num\n" );
		sb.append("           from tt_as_wr_old_returned_detail t, tt_as_wr_application awa\n" );
		sb.append("          where trunc(t.in_date) between\n" );
		sb.append("                to_date( '"+year+"' || '-01-01', 'yyyy-mm-dd') and\n" );
		sb.append("                to_date( '"+year+"' || '-12-31', 'yyyy-mm-dd')\n" );
		sb.append("            and t.claim_id = awa.id\n" );
		DaoFactory.getsql(sb, "t.PRODUCER_NAME ", DaoFactory.getParam(request, "producer_name"), 2);
		sb.append("          group by to_char(t.in_date, 'mm') || '月'\n" );
		sb.append("          order by to_char(t.in_date, 'mm') || '月' asc), b as --拒赔数量\n" );
		sb.append("        (select to_char(t.executive_director_date, 'mm') || '月' ym,\n" );
		sb.append("                sum(t.return_amount) refuse_num\n" );
		sb.append("           from tt_as_wr_old_returned_detail t, tt_as_wr_application awa\n" );
		sb.append("          where trunc(t.executive_director_date) between\n" );
		sb.append("                to_date( '"+year+"' || '-01-01', 'yyyy-mm-dd') and\n" );
		sb.append("                to_date( '"+year+"' || '-12-31', 'yyyy-mm-dd')\n" );
		sb.append("            and t.claim_id = awa.id\n" );
		DaoFactory.getsql(sb, "t.PRODUCER_NAME ", DaoFactory.getParam(request, "producer_name"), 2);
		sb.append("            and t.sign_amount = 0\n" );
		sb.append("            and t.executive_director_sta = 1\n" );
		sb.append("          group by to_char(t.executive_director_date, 'mm') || '月'\n" );
		sb.append("          order by to_char(t.executive_director_date, 'mm') || '月' asc), c as --收件家数\n" );
		sb.append("        (select to_char(t.sign_date, 'mm') || '月' ym,\n" );
		sb.append("                count(distinct t.dealer_id) dealer_num\n" );
		sb.append("           from tt_as_wr_old_returned t  left join tt_as_wr_old_returned_detail de on t.id=de.return_id\n" );
		sb.append("          where trunc(t.sign_date) between\n" );
		sb.append("                to_date( '"+year+"' || '-01-01', 'yyyy-mm-dd') and\n" );
		sb.append("                to_date( '"+year+"' || '-12-31', 'yyyy-mm-dd')\n" );
		DaoFactory.getsql(sb, "de.PRODUCER_NAME ", DaoFactory.getParam(request, "producer_name"), 2);
		sb.append("          group by to_char(t.sign_date, 'mm') || '月'\n" );
		sb.append("          order by to_char(t.sign_date, 'mm') || '月' asc), d as --签收数量\n" );
		sb.append("        (select to_char(t.sign_date, 'mm') || '月' ym,\n" );
		sb.append("                sum(tt.return_amount) sige_num\n" );
		sb.append("           from tt_as_wr_old_returned_detail tt,\n" );
		sb.append("                tt_as_wr_application         awa,\n" );
		sb.append("                tt_as_wr_old_returned        t\n" );
		sb.append("          where trunc(t.sign_date) between\n" );
		sb.append("                to_date( '"+year+"' || '-01-01', 'yyyy-mm-dd') and\n" );
		sb.append("                to_date( '"+year+"' || '-12-31', 'yyyy-mm-dd')\n" );
		sb.append("            and awa.id = tt.claim_id\n" );
		sb.append("            and tt.return_id = t.id\n" );
		DaoFactory.getsql(sb, "tt.PRODUCER_NAME ", DaoFactory.getParam(request, "producer_name"), 2);
		sb.append("          group by to_char(t.sign_date, 'mm') || '月'\n" );
		sb.append("          order by to_char(t.sign_date, 'mm') || '月' asc)\n" );
		sb.append("         select a.ym,\n" );
		sb.append("                nvl(c.dealer_num, 0) dealer_num, --收件家数\n" );
		sb.append("                nvl(d.sige_num, 0) sige_num, --收货件数（零件数量）\n" );
		sb.append("                nvl(a.in_num, 0) in_num, --正常入库件数\n" );
		sb.append("                nvl(b.refuse_num, 0) refuse_num, --拒赔件数\n" );
		sb.append("                '' remark\n" );
		sb.append("           from a, b, c, d\n" );
		sb.append("          where a.ym = b.ym(+)\n" );
		sb.append("            and a.ym = c.ym(+)\n" );
		sb.append("            and a.ym = d.ym(+)\n" );
		sb.append("         union all\n" );
		sb.append("         select '合计',\n" );
		sb.append("                sum(nvl(c.dealer_num, 0)) dealer_num, --收件家数\n" );
		sb.append("                sum(nvl(d.sige_num, 0)) sige_num, --收货件数（零件数量）\n" );
		sb.append("                sum(nvl(a.in_num, 0)) in_num, --正常入库件数\n" );
		sb.append("                sum(nvl(b.refuse_num, 0)) refuse_num, --拒赔件数\n" );
		sb.append("                '' remark\n" );
		sb.append("           from a, b, c, d\n" );
		sb.append("          where a.ym = b.ym(+)\n" );
		sb.append("            and a.ym = c.ym(+)\n" );
		sb.append("            and a.ym = d.ym(+))\n" );
		sb.append("          order by ym asc");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public void expotData15(ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[13];
			
			head[0]="月份";
			head[1]="收件家数";
			head[2]="收货件数（零件数量）";
			head[3]="正常入库件数";
			head[4]="拒赔件数";
			head[5]="备注";
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[13];
					detail[0]=BaseUtils.checkNull(map.get("YM"));
					detail[1]=BaseUtils.checkNull(map.get("DEALER_NUM"));
					detail[2]=BaseUtils.checkNull(map.get("SIGE_NUM"));
					detail[3]=BaseUtils.checkNull(map.get("IN_NUM"));
					detail[4]=BaseUtils.checkNull(map.get("REFUSE_NUM"));
					detail[5]=BaseUtils.checkNull(map.get("REMARK"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出年度入库分析总表"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	//销售店

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> reportDealer(RequestWrapper request,
			Integer page, Integer currPage, Map<String, String> params) {
		
		String dealer_code = request.getParamValue("dealer_code");
		String dealer_name = request.getParamValue("dealer_name");
		
		StringBuffer sb= new StringBuffer();
		sb.append("select *\n" );
		sb.append("  from tm_dealer d\n" );
		sb.append(" where d.dealer_id in\n" );
		sb.append("       (select distinct t.dealer_id\n" );
		sb.append("          from tt_as_wr_old_returned t\n" );
		sb.append("         where trunc(t.sign_date) between\n" );
		sb.append("               last_day(add_months(to_date('"+params.get("date")+"', 'yyyy-mm'), -1)) + 1 and\n" );
		sb.append("               last_day(to_date('"+params.get("date")+"', 'yyyy-mm')))");
		if(dealer_code!=null){
			sb.append("and dealer_code like '%"+dealer_code+"%'");
		}
		if(dealer_name!=null){
			sb.append(" and dealer_name like '%"+dealer_name+"%'	");
		}
		
		
		
		
		
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public void expotDataDealer(ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[13];
			
			head[0]="销售店编码";
			head[1]="销售店名称";
			head[2]="地址";
			head[3]="电话";
			head[4]="邮编";
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[13];
					detail[0]=BaseUtils.checkNull(map.get("DEALER_ID"));
					detail[1]=BaseUtils.checkNull(map.get("DEALER_NAME"));
					detail[2]=BaseUtils.checkNull(map.get("ADDRESS"));
					detail[3]=BaseUtils.checkNull(map.get("PHONE"));
					detail[4]=BaseUtils.checkNull(map.get("ZIP_CODE"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出查询经销商明细"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> reportRige(RequestWrapper request,
			Integer page, Integer currPage, Map<String, String> params) {
		
		String supply_code = request.getParamValue("supply_code");
		String supply_name = request.getParamValue("supply_name");
		String claim_no = request.getParamValue("claim_no");
		String vin = request.getParamValue("vin");
		
		StringBuffer sb= new StringBuffer();
		sb.append("select tt.claim_id,tt.claim_no,tt.vin,tt.return_amount,tt.part_code,tt.part_name, tt.producer_code,tt.producer_name\n" );
		sb.append("  from tt_as_wr_old_returned_detail tt,\n" );
		sb.append("       tt_as_wr_application         awa,\n" );
		sb.append("       tt_as_wr_old_returned        t\n" );
		sb.append(" where trunc(t.sign_date) between\n" );
		sb.append("       last_day(add_months(to_date('"+params.get("date")+"', 'yyyy-mm'), -1)) + 1 and\n" );
		sb.append("       last_day(to_date('"+params.get("date")+"', 'yyyy-mm'))\n" );
		sb.append("   and awa.id = tt.claim_id\n" );
		sb.append("   and tt.return_id = t.id");
		
		if(supply_code!=null){
			sb.append(" and producer_code like '%"+supply_code+"%' \n");
		}
		if(supply_name!=null){
			sb.append(" and producer_name like '%"+supply_name+"%' \n");
		}
		if(claim_no!=null){
			sb.append(" and tt.claim_no like '%"+claim_no+"%' \n");
		}
		if(vin!=null){
			sb.append(" and tt.vin like '%"+vin+"%' \n");
		}


		
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public void expotDataRige(ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[13];
			head[0]="索赔单号";
			head[1]="收件数";
			head[2]="车架号";
			head[3]="供应商名称";
			head[4]="供应商编号";
			head[5]="配件名称";
			head[6]="配件编号";
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[13];
					detail[0]=BaseUtils.checkNull(map.get("CLAIM_ID"));
					detail[1]=BaseUtils.checkNull(map.get("RETURN_AMOUNT"));
					detail[2]=BaseUtils.checkNull(map.get("VIN"));
					detail[3]=BaseUtils.checkNull(map.get("PRODUCER_NAME"));
					detail[4]=BaseUtils.checkNull(map.get("PRODUCER_CODE"));
					detail[5]=BaseUtils.checkNull(map.get("PART_CODE"));
					detail[6]=BaseUtils.checkNull(map.get("PART_NAME"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出查询签收明细"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	//入库
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> reportIn(RequestWrapper request,
			Integer page, Integer currPage, Map<String, String> params) {
		String supply_code = request.getParamValue("supply_code");
		String supply_name = request.getParamValue("supply_name");
		String part_code = request.getParamValue("part_code");
		String part_name = request.getParamValue("part_name");
		String claim_no = request.getParamValue("claim_no");
		String vin = request.getParamValue("vin");
		StringBuffer sb= new StringBuffer();
		sb.append("select t.claim_id,t.claim_no,t.vin,t.sign_amount,t.part_code,t.part_name, t.producer_code,t.producer_name\n" );
		sb.append("  from tt_as_wr_old_returned_detail t, tt_as_wr_application awa\n" );
		sb.append(" where trunc(t.in_date) between\n" );
		sb.append("       last_day(add_months(to_date('"+params.get("date")+"', 'yyyy-mm'), -1)) + 1 and\n" );
		sb.append("       last_day(to_date('"+params.get("date")+"', 'yyyy-mm'))\n" );
		sb.append("   and t.sign_amount = 1\n" );
		sb.append("   and t.claim_id = awa.id");
		
		
		if(supply_code!=null){
			sb.append(" and producer_code like '%"+supply_code+"%' \n");
		}
		if(supply_name!=null){
			sb.append(" and producer_name like '%"+supply_name+"%' \n");
		}
		if(claim_no!=null){
			sb.append(" and t.claim_no like '%"+claim_no+"%' \n");
		}
		if(vin!=null){
			sb.append(" and t.vin like '%"+vin+"%' \n");
		}
		if(part_code!=null){
			sb.append(" and part_code like '%"+part_code+"%' \n");
		}
		if(part_name!=null){
			sb.append(" and part_name like '%"+part_name+"%' \n");
		}

		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public void expotDataIn(ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[13];
			head[0]="索赔单号";
			head[1]="入库数";
			head[2]="车架号";
			head[3]="供应商名称";
			head[4]="供应商编号";
			head[5]="配件名称";
			head[6]="配件编号";
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[13];
					detail[0]=BaseUtils.checkNull(map.get("CLAIM_ID"));
					detail[1]=BaseUtils.checkNull(map.get("SIGN_AMOUNT"));
					detail[2]=BaseUtils.checkNull(map.get("VIN"));
					detail[3]=BaseUtils.checkNull(map.get("PRODUCER_NAME"));
					detail[4]=BaseUtils.checkNull(map.get("PRODUCER_CODE"));
					detail[5]=BaseUtils.checkNull(map.get("PART_CODE"));
					detail[6]=BaseUtils.checkNull(map.get("PART_NAME"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出查询签收明细"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> reportRefuse(RequestWrapper request,
			Integer page, Integer currPage, Map<String, String> params) {
		
		String supply_code = request.getParamValue("supply_code");
		String supply_name = request.getParamValue("supply_name");
		String part_code = request.getParamValue("part_code");
		String part_name = request.getParamValue("part_name");
		String claim_no = request.getParamValue("claim_no");
		String vin = request.getParamValue("vin");
		StringBuffer sb= new StringBuffer();
		sb.append("select t.claim_id,t.claim_no,t.vin,t.return_amount,t.part_code,t.part_name, t.producer_code,t.producer_name");
		sb.append("  from tt_as_wr_old_returned_detail t\n" );
		sb.append(" where trunc(t.executive_director_date) between\n" );
		sb.append("       last_day(add_months(to_date('"+params.get("date")+"', 'yyyy-mm'), -1)) + 1 and\n" );
		sb.append("       last_day(to_date('"+params.get("date")+"', 'yyyy-mm'))\n" );
		sb.append("   and t.sign_amount = 0\n" );
		sb.append("   and t.executive_director_sta = 1\n" );
		if(supply_code!=null){
			sb.append(" and producer_code like '%"+supply_code+"%' \n");
		}
		if(supply_name!=null){
			sb.append(" and producer_name like '%"+supply_name+"%' \n");
		}
		if(claim_no!=null){
			sb.append(" and t.claim_no like '%"+claim_no+"%' \n");
		}
		if(vin!=null){
			sb.append(" and t.vin like '%"+vin+"%' \n");
		}
		if(part_code!=null){
			sb.append(" and part_code like '%"+part_code+"%' \n");
		}
		if(part_name!=null){
			sb.append(" and part_name like '%"+part_name+"%' \n");
		}
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public void expotDataRefuse(ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[13];
			head[0]="索赔单号";
			head[1]="拒赔";
			head[2]="车架号";
			head[3]="供应商名称";
			head[4]="供应商编号";
			head[5]="配件名称";
			head[6]="配件编号";
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[13];
					detail[0]=BaseUtils.checkNull(map.get("CLAIM_ID"));
					detail[1]=BaseUtils.checkNull(map.get("RETURN_AMOUNT"));
					detail[2]=BaseUtils.checkNull(map.get("VIN"));
					detail[3]=BaseUtils.checkNull(map.get("PRODUCER_NAME"));
					detail[4]=BaseUtils.checkNull(map.get("PRODUCER_CODE"));
					detail[5]=BaseUtils.checkNull(map.get("PART_CODE"));
					detail[6]=BaseUtils.checkNull(map.get("PART_NAME"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出查询拒赔明细"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> reportOldPart(
			RequestWrapper request, Integer page, Integer currPage,
			Map<String, String> params) {
		StringBuffer sb= new StringBuffer();
		sb.append("select * from (\n" );
		sb.append("with r as--初始化入库明细，更新空供应商，主因件更新次因件\n" );
		sb.append(" (select t.claim_id claim_id,\n" );
		sb.append("         t.return_id return_id,\n" );
		sb.append("         t.sign_amount sign_amount,\n" );
		sb.append("         t.part_code part_code,\n" );
		sb.append("         t.in_date in_date,\n" );
		sb.append("         case\n" );
		sb.append("           when t.producer_code is null then\n" );
		sb.append("            (select td.producer_code\n" );
		sb.append("               from tt_as_wr_old_returned_detail td, tt_as_wr_partsitem p\n" );
		sb.append("              where p.id = t.claim_id\n" );
		sb.append("                and p.part_code = t.part_code\n" );
		sb.append("                and p.main_part_code = td.part_code\n" );
		sb.append("                and td.claim_id = p.id)\n" );
		sb.append("           else\n" );
		sb.append("            t.producer_code\n" );
		sb.append("         end producer_code\n" );
		sb.append("    from tt_as_wr_old_returned_detail t),\n" );
		sb.append("o as--初始化出库明细，更新空供应商，主因件更新次因件\n" );
		sb.append(" (select t.out_amout out_amount,\n" );
		sb.append("         t.out_part_code out_part_code,\n" );
		sb.append("         t.out_date out_date,\n" );
		sb.append("         case\n" );
		sb.append("           when t.supply_code is null then\n" );
		sb.append("            (select td.supply_code\n" );
		sb.append("               from tt_as_wr_old_out_part td,\n" );
		sb.append("                    tt_as_wr_partsitem    p,\n" );
		sb.append("                    tt_as_wr_application  awa\n" );
		sb.append("              where awa.claim_no = t.claim_no\n" );
		sb.append("                and awa.id = p.id\n" );
		sb.append("                and p.part_code = t.out_part_code\n" );
		sb.append("                and p.main_part_code = td.out_part_code\n" );
		sb.append("                and t.claim_no = td.claim_no)\n" );
		sb.append("           else\n" );
		sb.append("            t.supply_code\n" );
		sb.append("         end supply_code\n" );
		sb.append("    from tt_as_wr_old_out_part t),\n" );
		sb.append("amount as--汇总截止上月底入库数量\n" );
		sb.append("（select r.producer_code supply_code,r.part_code part_code,nvl(sum(r.sign_amount),0) in_amount,0 out_amount,0 in_amount_tm,0 out_amount_tm\n" );
		sb.append("    from r,\n" );
		sb.append("         tt_as_wr_application awa,\n" );
		sb.append("         tt_as_wr_old_returned t\n" );
		sb.append("   where r.claim_id = awa.id\n" );
		sb.append("     and r.return_id = t.id\n" );
		sb.append("     and t.sign_date is not null\n" );
		sb.append("     and trunc(r.in_date) <= last_day(add_months(to_date('"+params.get("date")+"','yyyy-mm'),-1))\n" );
		sb.append("   group by r.producer_code,r.part_code\n" );
		sb.append("  union all--汇总截止上月底出库数量\n" );
		sb.append("  select o.supply_code supply_code,o.out_part_code part_code,0 in_amount,nvl(sum(o.out_amount), 0) out_amount,0 in_amount_tm,0 out_amount_tm\n" );
		sb.append("    from o\n" );
		sb.append("   where trunc(o.out_date) <= last_day(add_months(to_date('"+params.get("date")+"','yyyy-mm'), -1))\n" );
		sb.append("   group by o.supply_code, o.out_part_code\n" );
		sb.append("  union all--汇总本月入库数量\n" );
		sb.append("  select r.producer_code supply_code,r.part_code part_code,0 in_amount,0 out_amount,nvl(sum(r.sign_amount), 0) in_amount_tm,0 out_amount_tm\n" );
		sb.append("    from r,\n" );
		sb.append("         tt_as_wr_application awa,\n" );
		sb.append("         tt_as_wr_old_returned t\n" );
		sb.append("   where r.claim_id = awa.id\n" );
		sb.append("     and r.return_id = t.id\n" );
		sb.append("     and t.sign_date is not null\n" );
		sb.append("     and trunc(r.in_date) between last_day(add_months(to_date('"+params.get("date")+"','yyyy-mm'), -1)) + 1\n" );
		sb.append("     and last_day(to_date('"+params.get("date")+"','yyyy-mm'))\n" );
		sb.append("   group by r.producer_code, r.part_code\n" );
		sb.append("  union all--汇总本月出库数量\n" );
		sb.append("  select o.supply_code supply_code,o.out_part_code part_code,0 in_amount,0 out_amount,0 in_amount_tm,nvl(sum(o.out_amount), 0) out_amount_tm\n" );
		sb.append("    from o\n" );
		sb.append("   where trunc(o.out_date) between last_day(add_months(to_date('"+params.get("date")+"','yyyy-mm'), -1)) + 1\n" );
		sb.append("     and last_day(to_date('"+params.get("date")+"','yyyy-mm'))\n" );
		sb.append("   group by o.supply_code, o.out_part_code)\n" );
		sb.append("select amount.supply_code supply_code,\n" );
		sb.append("       pmd.maker_name supply_name,\n" );
		sb.append("       amount.part_code part_code,\n" );
		sb.append("       pd.part_cname part_name,\n" );
		sb.append("       nvl(sum(amount.in_amount), 0) - nvl(sum(amount.out_amount), 0) qc_amount,--期初结存\n" );
		sb.append("       nvl(sum(amount.in_amount_tm), 0) in_amount,--本期收入\n" );
		sb.append("       nvl(sum(amount.out_amount_tm), 0) out_amount,--本期发出\n" );
		sb.append("       nvl(sum(amount.in_amount), 0) - nvl(sum(amount.out_amount), 0) + nvl(sum(amount.in_amount_tm), 0) - nvl(sum(amount.out_amount_tm), 0) qm_amount,--期末结存\n" );
		sb.append("       '' remark\n" );
		sb.append("  from amount, tt_part_define pd, tt_part_maker_define pmd\n" );
		sb.append(" where pd.part_oldcode = amount.part_code\n" );
		sb.append("   and pmd.maker_code = amount.supply_code\n" );
		
		
		if(params.get("FaCode")!=null&&!params.get("FaCode").equals("")){
			sb.append("   and pmd.maker_code like '%"+params.get("FaCode")+"%'--供应商编码条件\n" );
			
		}
		if((params.get("FaShortName")!=null)&&!params.get("FaShortName").equals("")){
			sb.append("   and pmd.maker_name like '%"+params.get("FaShortName")+"%'--供应商名称条件\n" );
		}
		if((params.get("detailCode")!=null)&&!params.get("detailCode").equals("")){
			sb.append("   and pd.part_oldcode like '%"+params.get("detailCode")+"%'--旧件编码条件\n" );
		}
		if((params.get("detailName")!=null)&&!params.get("detailName").equals("")){
			sb.append("   and pd.part_cname like '%"+params.get("detailName")+"%'--旧件名称条件\n" );
		}
		sb.append(" group by amount.supply_code,pmd.maker_name,amount.part_code,pd.part_cname\n" );
		sb.append(" union all--结果汇总\n" );
		sb.append("select '',\n" );
		sb.append("       '合计',\n" );
		sb.append("       '',\n" );
		sb.append("       '',\n" );
		sb.append("       nvl(sum(amount.in_amount), 0) - nvl(sum(amount.out_amount), 0) qc_amount,\n" );
		sb.append("       nvl(sum(amount.in_amount_tm), 0) in_amount,\n" );
		sb.append("       nvl(sum(amount.out_amount_tm), 0) out_amount,\n" );
		sb.append("       nvl(sum(amount.in_amount), 0) - nvl(sum(amount.out_amount), 0) + nvl(sum(amount.in_amount_tm), 0) - nvl(sum(amount.out_amount_tm), 0) qm_amount,\n" );
		sb.append("       ''\n" );
		sb.append("  from amount, tt_part_define pd, tt_part_maker_define pmd\n" );
		sb.append(" where pd.part_oldcode = amount.part_code\n" );
		sb.append("   and pmd.maker_code = amount.supply_code\n" );
		if(params.get("FaCode")!=null&&!params.get("FaCode").equals("")){
			sb.append("   and pmd.maker_code like '%"+params.get("FaCode")+"%'--供应商编码条件\n" );
			
		}
		if((params.get("FaShortName")!=null)&&!params.get("FaShortName").equals("")){
			sb.append("   and pmd.maker_name like '%"+params.get("FaShortName")+"%'--供应商名称条件\n" );
		}
		if((params.get("detailCode")!=null)&&!params.get("detailCode").equals("")){
			sb.append("   and pd.part_oldcode like '%"+params.get("detailCode")+"%'--旧件编码条件\n" );
		}
		if((params.get("detailName")!=null)&&!params.get("detailName").equals("")){
			sb.append("   and pd.part_cname like '%"+params.get("detailName")+"%'--旧件名称条件\n" );
		}
		sb.append(" )order by supply_code,part_code asc\n" );

		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public void expotDataOldPart(ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[10];
			head[0]="部件厂代码";
			head[1]="部件厂名称";
			head[2]="零部件代码";
			head[3]="零部件名称";
			head[4]="期初结存";
			head[5]="本期收入";
			head[6]="本期发出";
			head[7]="本期结存";
			head[8]="备注";
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[10];
					detail[0]=BaseUtils.checkNull(map.get("SUPPLY_CODE"));
					detail[1]=BaseUtils.checkNull(map.get("SUPPLY_NAME"));
					detail[2]=BaseUtils.checkNull(map.get("PART_CODE"));
					detail[3]=BaseUtils.checkNull(map.get("PART_NAME"));
					detail[4]=BaseUtils.checkNull(map.get("QC_AMOUNT"));
					detail[5]=BaseUtils.checkNull(map.get("IN_AMOUNT"));
					detail[6]=BaseUtils.checkNull(map.get("OUT_AMOUNT"));
					detail[7]=BaseUtils.checkNull(map.get("QM_AMOUNT"));
					detail[8]=BaseUtils.checkNull(map.get("REMARK"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出旧件库存报表"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> reportCarSupply(
			RequestWrapper request, Integer page, Integer currPage,
			Map<String, String> params) {
		StringBuffer sb= new StringBuffer();
		sb.append("--条件在一定期限内单个零部件车申报汇总（按降序排列） 这个以report_date_btn为查询时间\n" );
		sb.append("select t.first_problem_code, --零件代码\n" );
		sb.append("       t.first_problem_name, --零部件名称\n" );
		sb.append("\t   t.report_date_btn,\n" );
		sb.append("       (select max(de.part_cname)\n" );
		sb.append("          from tt_part_define de\n" );
		sb.append("         where de.part_code = t.first_problem_code) as part_cname,\n" );
		sb.append("       max(t.first_problem_supplier_code) as maker_code, --供应商代码*/\n" );
		sb.append("       count(1) as changeNum --更换频次\n" );
		sb.append("  from Tt_Sales_Quality_Report_Info t\n" );
		sb.append("  where 1=1\n" );
		if(params.get("startDate")!=null&&params.get("endDate")!=null){
//			sb.append("  and to_date(substr(t.report_date_btn,1,10)) like '%"+params.get("date")+"%'\n" );
			sb.append("and to_date(substr(t.report_date_btn,1,10),'yyyy-MM-dd') between to_date('"+params.get("startDate")+"','yyyy-MM-dd') and to_date('"+params.get("endDate")+"','yyyy-MM-dd')");
		}
		if(params.get("partCode")!=null){
			sb.append("  and first_problem_code like '%"+params.get("partCode")+"%'\n" );
		}
		if(params.get("partName")!=null){
			sb.append("  and first_problem_name like '%"+params.get("partName")+"%'\n" );
		}
		sb.append(" group by t.first_problem_code,t.first_problem_name,report_date_btn order by t.first_problem_code\n" );
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public void expotDataCarSupply(ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[10];
			head[0]="零部件代码";
			head[1]="零部件名称";
			head[2]="日期";
			head[3]="部件名称";
			head[4]="供应商代码";
			head[5]="更换频率";
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){  
				for (Map<String, Object> map : records) {
					String[] detail=new String[10];
					detail[0]=BaseUtils.checkNull(map.get("FIRST_PROBLEM_CODE"));
					detail[1]=BaseUtils.checkNull(map.get("FIRST_PROBLEM_NAME"));
					detail[2]=BaseUtils.checkNull(map.get("REPORT_DATE_BTN"));
					detail[3]=BaseUtils.checkNull(map.get("PART_CNAME"));
					detail[4]=BaseUtils.checkNull(map.get("MAKER_CODE"));
					detail[5]=BaseUtils.checkNull(map.get("CHANGENUM"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出零部件车申报汇总报表"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> reportCarFix(RequestWrapper request,
			AclUserBean loginUser, Integer page, Integer currPage, Map<String, String> params) {
		StringBuffer sb= new StringBuffer();
		sb.append("\n" );
		sb.append("select o.vin,\n" );
		sb.append("       (select s.root_org_name\n" );
		sb.append("          from vw_org_dealer_service s\n" );
		sb.append("         where s.dealer_id = o.dealer_id\n" );
	//	sb.append("    and root_org_name='六大区'\n" );
		sb.append(" )\n" );
		sb.append(" as root_org_name，\n" );
		sb.append("       tm.dealer_code,\n" );
		sb.append("       tm.dealer_shortname,\n" );
		sb.append("       o.ro_create_date,\n" );
		sb.append("       o.for_balance_time,\n" );
		sb.append("       (select tc.code_desc\n" );
		sb.append("          from tc_code tc\n" );
		sb.append("         where tc.code_id = o.repair_type_code) as repair_type_code\n" );
		sb.append("  from tt_as_repair_order o, tm_dealer tm\n" );
		sb.append(" where 1 = 1\n" );
		sb.append("   and o.dealer_id = tm.dealer_id\n" );
		if(params.get("dealerCode")!=null&&!params.get("dealerCode").equals("")){
			sb.append("  and tm.dealer_code in ("+params.get("dealerCode")+")\n" );
		}
		if(params.get("FaShortName")!=null&&!params.get("FaShortName").equals("")){
			sb.append("  and tm.dealer_name like '%"+params.get("FaShortName")+"%'\n" );
		}
		if(params.get("VIN")!=null&&!params.get("VIN").equals("")){
			sb.append("  and vin like '%"+params.get("VIN")+"%'\n" );
		}
		sb.append("   and o.vin in (select v.vin from TT_AS_ACTIVITY_VEHICLE v) \n");
		
		if(params.get("startDate")!=null){
			sb.append("  and to_date(to_char(o.ro_create_date,'yyyy-MM-dd'),'yyyy-MM-dd') >=to_date('"+params.get("startDate")+"','yyyy-MM-dd') \n");
		}
		if(params.get("endDate")!=null){
			sb.append("  and o.ro_create_date <=to_date('"+params.get("endDate")+" 23:59:59', 'yyyy-MM-dd hh24:mi:ss') \n");
		}
		

		if (this.getPoseRoleId(loginUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
		       sb.append(CommonUtils.getOrgDealerLimitSqlByPose("tm", loginUser));
		}
		
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public void expotDataCarfix(ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[10];
			head[0]="VIN";
			head[1]="大区";
			head[2]="经销商编号";
			head[3]="经销商名称";
			head[4]="工单开据日期";
			head[5]="工单结算日期";
			head[6]="工单类型";
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[10];
					detail[0]=BaseUtils.checkNull(map.get("VIN"));
					detail[1]=BaseUtils.checkNull(map.get("ROOT_ORG_NAME"));
					detail[2]=BaseUtils.checkNull(map.get("DEALER_CODE"));
					detail[3]=BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					detail[4]=BaseUtils.checkNull(map.get("RO_CREATE_DATE"));
					detail[5]=BaseUtils.checkNull(map.get("FOR_BALANCE_TIME"));
					detail[6]=BaseUtils.checkNull(map.get("REPAIR_TYPE_CODE"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "车辆维修履历报表 "+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> reportQualityRegion(
			RequestWrapper request, Integer page, Integer currPage,
			Map<String, String> params) {
	    String startDate=request.getParamValue("startDate");	
	    String endDate=request.getParamValue("endDate");	
		
		StringBuffer sb= new StringBuffer();
		sb.append("--标题（品质情报通过率【注：根据大区分组】） 大区\n" );
		sb.append("--序号(自己页面取) 大区\t品质情报上报数量\t通过数量\t驳回数量\t审核通过率\n" );
		sb.append("select t.root_org_name,\n" );
		sb.append("       t.report_Num,\n" );
		sb.append("       t.pass_num,\n" );
		sb.append("       t.rebut_num,\n" );
		sb.append("       round(t.pass_num / t.report_Num, 2) * 100 || '%' as percent\n" );
		sb.append("  from (select v.root_org_name,\n" );
		sb.append("               count(1) as report_Num,\n" );
		sb.append("               (select count(1)\n" );
		sb.append("                  from Tt_Sales_Quality_Report_Info i\n" );
		sb.append("                 where i.dealer_id in\n" );
		sb.append("                       (select s.dealer_id\n" );
		sb.append("                          from vw_org_dealer_service s\n" );
		sb.append("                         where s.root_org_name = v.root_org_name)\n" );
		sb.append("                   and i.verify_status in (95531003, 95531005, 95531006)) as pass_num,\n" );
		sb.append("               (select count(1)\n" );
		sb.append("                  from Tt_Sales_Quality_Report_Info i\n" );
		sb.append("                 where i.dealer_id in\n" );
		sb.append("                       (select s.dealer_id\n" );
		sb.append("                          from vw_org_dealer_service s\n" );
		sb.append("                         where s.root_org_name = v.root_org_name)\n" );
		sb.append("                   and i.verify_status = 95531004) as rebut_num\n" );
		sb.append("          from Tt_Sales_Quality_Report_Info t, vw_org_dealer_service v\n" );
		sb.append("         where v.dealer_id = t.dealer_id and  t.report_date_btn is not null and t.verify_status> 95531002\n" );
		
		if(startDate!=null){
			sb.append("         and to_date(t.report_date_btn,'yyyy-mm-dd hh24:mi:ss') >=  to_date('"+startDate+"','yyyy-MM-dd') ");
		}
		if(endDate!=null){
			sb.append("         and to_date(t.report_date_btn,'yyyy-mm-dd hh24:mi:ss') <=  to_date('"+endDate+"','yyyy-MM-dd')  ");
		}
		sb.append("         group by v.root_org_name) t");
		
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public void expotDataQualityRegion(ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[10];
			head[0]="大区";
			head[1]="品质情报上报数量";
			head[2]="通过数量";
			head[3]="驳回数量";
			head[4]="审核通过率";
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[10];
					detail[0]=BaseUtils.checkNull(map.get("ROOT_ORG_NAME"));
					detail[1]=BaseUtils.checkNull(map.get("REPORT_NUM"));
					detail[2]=BaseUtils.checkNull(map.get("PASS_NUM"));
					detail[3]=BaseUtils.checkNull(map.get("REBUT_NUM"));
					detail[4]=BaseUtils.checkNull(map.get("PERCENT"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出品质情报通过率报表"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> reportQualityDealer(
			RequestWrapper request, AclUserBean loginUser, Integer page, Integer currPage,
			Map<String, String> params) {
		StringBuffer sb= new StringBuffer();
		sb.append("--标题（品质情报通过率【注：根据经销商分组】） 查询条件为经销商代码，大区\n" );
		sb.append("--序号(自己页面取0  大区  服务商代码  服务商简称  品质情报上报数量  通过数量  驳回数量  审核通过率\n" );
		sb.append("select t.root_org_name,\n" );
		sb.append("       t.dealer_code,\n" );
		sb.append("       t.dealer_shortname,\n" );
		sb.append("       t.report_Num,\n" );
		sb.append("       t.pass_num,\n" );
		sb.append("       t.rebut_num,\n" );
		sb.append("       round(t.pass_num / decode(t.report_Num, 0, 1, t.report_Num), 2) * 100 || '%' as percent\n" );
		sb.append("  from (select (select v.root_org_name\n" );
		sb.append("                  from vw_org_dealer_service v\n" );
		sb.append("                 where v.dealer_id = tm.dealer_id) as root_org_name,\n" );
		sb.append("               tm.dealer_code,\n" );
		sb.append("               tm.dealer_shortname,\n" );
		sb.append("               (select count(1)\n" );
		sb.append("                  from Tt_Sales_Quality_Report_Info i\n" );
		sb.append("                 where i.dealer_id = tm.dealer_id\n" );
		DaoFactory.getsql(sb, "to_date(i.report_date_btn,'yyyy-mm-dd hh24:mi:ss')", params.get("startDate"), 3);
		DaoFactory.getsql(sb, "to_date(i.report_date_btn,'yyyy-mm-dd hh24:mi:ss')", params.get("endDate"), 4);
		sb.append("                   and i.verify_status >= 95531002) as report_Num,\n" );
		sb.append("               (select count(1)\n" );
		sb.append("                  from Tt_Sales_Quality_Report_Info i\n" );
		sb.append("                 where i.dealer_id = tm.dealer_id\n" );
		DaoFactory.getsql(sb, "to_date(i.report_date_btn,'yyyy-mm-dd hh24:mi:ss')", params.get("startDate"), 3);
		DaoFactory.getsql(sb, "to_date(i.report_date_btn,'yyyy-mm-dd hh24:mi:ss')", params.get("endDate"), 4);
		sb.append("                   and i.verify_status in (95531003, 95531005, 95531006)) as pass_num,\n" );
		sb.append("               (select count(1)\n" );
		sb.append("                  from Tt_Sales_Quality_Report_Info i\n" );
		sb.append("                 where i.dealer_id = tm.dealer_id\n" );
		DaoFactory.getsql(sb, "to_date(i.report_date_btn,'yyyy-mm-dd hh24:mi:ss')", params.get("startDate"), 3);
		DaoFactory.getsql(sb, "to_date(i.report_date_btn,'yyyy-mm-dd hh24:mi:ss')", params.get("endDate"), 4);
		sb.append("                   and i.verify_status = 95531004) as rebut_num\n" );
		sb.append("          from Tt_Sales_Quality_Report_Info t, tm_dealer tm\n" );
		sb.append("         where tm.dealer_id = t.dealer_id(+)\n" );
		sb.append("           and tm.dealer_type = 10771002\n" );
		sb.append("           and tm.dealer_code not in ('802399-F', '5551-F', '101001-F') and  t.report_date_btn is not null and t.verify_status> 95531002\n" );

		if (this.getPoseRoleId(loginUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
		       sb.append(CommonUtils.getOrgDealerLimitSqlByPose("tm", loginUser));
		}
		if(params.get("dealerCode")!=null){
			sb.append(" and tm.dealer_code in ("+params.get("dealerCode")+")");
		}
		DaoFactory.getsql(sb, "to_date(t.report_date_btn,'yyyy-mm-dd hh24:mi:ss')", params.get("startDate"), 3);
		DaoFactory.getsql(sb, "to_date(t.report_date_btn,'yyyy-mm-dd hh24:mi:ss')", params.get("endDate"), 4);
		sb.append("         group by tm.dealer_id, tm.dealer_code, tm.dealer_shortname) t ");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}

	

	@SuppressWarnings("unchecked")
	public void expotDataQualityDealer(ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head={"大区","服务商代码","服务商简称","品质情报上报数量","通过数量","驳回数量","审核通过率"};
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[7];
					detail[0]=BaseUtils.checkNull(map.get("ROOT_ORG_NAME"));
					detail[1]=BaseUtils.checkNull(map.get("DEALER_CODE"));
					detail[2]=BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					detail[3]=BaseUtils.checkNull(map.get("REPORT_NUM"));
					detail[4]=BaseUtils.checkNull(map.get("PASS_NUM"));
					detail[5]=BaseUtils.checkNull(map.get("REBUT_NUM"));
					detail[6]=BaseUtils.checkNull(map.get("PERCENT"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出品质情报通过率报表"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> reportQualityMonth(
			RequestWrapper request, Integer page, Integer currPage,
			Map<String, String> params) {
		String startDate=request.getParamValue("startDate");
		String endDate=request.getParamValue("endDate");
		StringBuffer sb= new StringBuffer();
		sb.append("--标题（品质情报通过率【注：根据月份分组】）  查询条件 月份\n" );
		sb.append("--月份  品质情报上报数量  通过数量  驳回数量  审核通过率\n" );
		sb.append("\n" );
		sb.append("select t.report_date,\n" );
		sb.append("       t.report_Num,\n" );
		sb.append("       t.pass_num,\n" );
		sb.append("       t.rebut_num,\n " );
		sb.append("       round(t.pass_num / t.report_Num, 2) * 100 || '%' as percent\n" );
		sb.append("  from (select to_char(to_date(t.report_date_btn, 'yyyy-mm-dd hh24:mi:ss'),\n" );
		sb.append("                       'yyyy-mm') as report_date,\n" );
		sb.append("               count(1) as report_Num,\n" );
		sb.append("               (select count(1)\n" );
		sb.append("                  from Tt_Sales_Quality_Report_Info i\n" );
		sb.append("                 where 1 = 1\n" );
		sb.append("                   and i.verify_status in (95531003, 95531005, 95531006)\n" );
		sb.append("                   and to_char(to_date(i.report_date_btn,\n" );
		sb.append("                                       'yyyy-mm-dd hh24:mi:ss'),\n" );
		sb.append("                               'yyyy-mm') =\n" );
		sb.append("                       to_char(to_date(t.report_date_btn,\n" );
		sb.append("                                       'yyyy-mm-dd hh24:mi:ss'),\n" );
		sb.append("                               'yyyy-mm')) as pass_num,\n" );
		sb.append("               (select count(1)\n" );
		sb.append("                  from Tt_Sales_Quality_Report_Info i\n" );
		sb.append("                 where 1 = 1\n" );
		sb.append("                   and i.verify_status = 95531004\n" );
		sb.append("                   and to_char(to_date(i.report_date_btn,\n" );
		sb.append("                                       'yyyy-mm-dd hh24:mi:ss'),\n" );
		sb.append("                               'yyyy-mm') =\n" );
		sb.append("                       to_char(to_date(t.report_date_btn,\n" );
		sb.append("                                       'yyyy-mm-dd hh24:mi:ss'),\n" );
		sb.append("                               'yyyy-mm')) as rebut_num\n" );
		sb.append("          from Tt_Sales_Quality_Report_Info t\n" );
		sb.append("         where t.report_date_btn is not null and t.verify_status> 95531002 \n" );
		if(startDate!=null){
			sb.append("         and to_date(t.report_date_btn,'yyyy-mm-dd hh24:mi:ss') >=  to_date('"+startDate+"','yyyy-MM-dd') ");
		}
		if(endDate!=null){
			sb.append("         and to_date(t.report_date_btn,'yyyy-mm-dd hh24:mi:ss') <=  to_date('"+endDate+"','yyyy-MM-dd')  ");
		}
		
		sb.append("         group by to_char(to_date(t.report_date_btn, 'yyyy-mm-dd hh24:mi:ss'),\n" );
		sb.append("                          'yyyy-mm')) t\n" );
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}

	

	@SuppressWarnings("unchecked")
	public void expotDataQualityMonth(ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[10];
			head[0]="月份";
			head[1]="品质情报上报数量";
			head[2]="通过数量";
			head[3]="驳回数量";
			head[4]="审核通过率";
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[10];
					detail[0]=BaseUtils.checkNull(map.get("REPORT_DATE"));
					detail[1]=BaseUtils.checkNull(map.get("REPORT_NUM"));
					detail[2]=BaseUtils.checkNull(map.get("PASS_NUM"));
					detail[3]=BaseUtils.checkNull(map.get("REBUT_NUM"));
					detail[4]=BaseUtils.checkNull(map.get("PERCENT"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出品质情报通过率报表"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> reportQualityDate(
			RequestWrapper request, Integer page, Integer currPage,
			Map<String, String> params) {
		StringBuffer sb= new StringBuffer();
		sb.append("--标题（品质情报通过率【注：根据月份分组】）  查询条件 月份\n" );
		sb.append("--月份  品质情报上报数量  通过数量  驳回数量  审核通过率\n" );
		sb.append("\n" );
		sb.append("select t.report_date,\n" );
		sb.append("       t.report_Num,\n" );
		sb.append("       t.pass_num,\n" );
		sb.append("       t.rebut_num,\n " );
		sb.append("       round(t.pass_num / t.report_Num, 2) * 100 || '%' as percent\n" );
		sb.append("  from (select to_char(to_date(t.report_date_btn, 'yyyy-mm-dd hh24:mi:ss'),\n" );
		sb.append("                       'yyyy-mm') as report_date,\n" );
		sb.append("               count(1) as report_Num,\n" );
		sb.append("               (select count(1)\n" );
		sb.append("                  from Tt_Sales_Quality_Report_Info i\n" );
		sb.append("                 where 1 = 1\n" );
		sb.append("                   and i.verify_status in (95531003, 95531005, 95531006)\n" );
		sb.append("                   and to_char(to_date(i.report_date_btn,\n" );
		sb.append("                                       'yyyy-mm-dd hh24:mi:ss'),\n" );
		sb.append("                               'yyyy-mm') =\n" );
		sb.append("                       to_char(to_date(t.report_date_btn,\n" );
		sb.append("                                       'yyyy-mm-dd hh24:mi:ss'),\n" );
		sb.append("                               'yyyy-mm')) as pass_num,\n" );
		sb.append("               (select count(1)\n" );
		sb.append("                  from Tt_Sales_Quality_Report_Info i\n" );
		sb.append("                 where 1 = 1\n" );
		sb.append("                   and i.verify_status = 95531004\n" );
		sb.append("                   and to_char(to_date(i.report_date_btn,\n" );
		sb.append("                                       'yyyy-mm-dd hh24:mi:ss'),\n" );
		sb.append("                               'yyyy-mm') =\n" );
		sb.append("                       to_char(to_date(t.report_date_btn,\n" );
		sb.append("                                       'yyyy-mm-dd hh24:mi:ss'),\n" );
		sb.append("                               'yyyy-mm')) as rebut_num\n" );
		sb.append("          from Tt_Sales_Quality_Report_Info t\n" );
		sb.append("         where t.report_date_btn is not null\n" );
		sb.append("         group by to_char(to_date(t.report_date_btn, 'yyyy-mm-dd hh24:mi:ss'),\n" );
		sb.append("                          'yyyy-mm')) t\n" );
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}





	





	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> reportStockRemoveal(RequestWrapper request, Integer page, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		String FaCode = DaoFactory.getParam(request, "FaCode");
		String FaShortName = DaoFactory.getParam(request,"FaShortName");
		String partCode = DaoFactory.getParam(request,"partCode");
		String partName = DaoFactory.getParam(request,"partName");
		String rangeNo = DaoFactory.getParam(request,"rangeNo");
		String claimNo = DaoFactory.getParam(request,"claimNo");
		String beginTime = DaoFactory.getParam(request,"beginTime");
		String endTime = DaoFactory.getParam(request,"endTime");
		String out_type = DaoFactory.getParam(request,"out_type");
		String is_qhj = DaoFactory.getParam(request,"is_qhj");
		
		sb.append("--出库查询\n" );
		sb.append("select oop.range_no,--退赔单号\n" );
		sb.append("       oop.supply_code,--供应商代码\n" );
		sb.append("       oop.supply_name,--供应商名称\n" );
		sb.append("       oop.out_part_code,--零件号\n" );
		sb.append("       oop.out_part_name,--零件名称\n" );
		sb.append("       oop.claim_no,--索赔单号\n" );
		sb.append("       awa.vin,--车架号\n" );
		sb.append("       oop.out_amout,--数量\n" );
		sb.append("       oop.out_date--出库时间\n" );
		// =================2015.09.18增加是否切换件=================开始
		sb.append(" ,decode(p.activity_type,10561005,'是','否') \"IS_QHJ\" ");
		// =================2015.09.18增加是否切换件=================结束
		sb.append("  from tt_as_wr_old_out_part oop, tt_as_wr_application awa\n" );
		// =================2015.09.18增加是否切换件=================开始
		sb.append(" ,tt_as_activity a,tt_as_activity_subject p ");
		// =================2015.09.18增加是否切换件=================结束
		sb.append(" where oop.claim_no = awa.claim_no\n" );
		// =================2015.09.18增加是否切换件=================开始
		sb.append(" and a.activity_code(+)=awa.campaign_code ");
		sb.append(" and a.subject_id=p.subject_id(+) ");
		// =================2015.09.18增加是否切换件=================结束
		DaoFactory.getsql(sb, "oop.supply_code", FaCode, 2);
		DaoFactory.getsql(sb, "oop.supply_name", FaShortName, 2);
		DaoFactory.getsql(sb, "oop.out_part_code", partCode, 2);
		DaoFactory.getsql(sb, "oop.out_part_name", partName, 2);
		DaoFactory.getsql(sb, "oop.range_no", rangeNo, 2);
		DaoFactory.getsql(sb, "oop.claim_no", claimNo, 2);
		DaoFactory.getsql(sb, "oop.out_date", beginTime, 3);
		DaoFactory.getsql(sb, "oop.out_date", endTime, 4);
		DaoFactory.getsql(sb, "oop.out_type", out_type, 1);
		DaoFactory.getsql(sb, "p.activity_type", is_qhj, 1);
		sb.append(" order by oop.out_date,oop.supply_code,oop.out_part_code ");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public void expotDataStockRemoveal(ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[10];
			head[0]="退赔单号";
			head[1]="供应商代码";
			head[2]="供应商名称";
			head[3]="零件号";
			head[4]="零件名称";
			head[5]="索赔单号";
			head[6]="车架号";
			head[7]="数量";
			head[8]="出库时间";
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[10];
					detail[0]=BaseUtils.checkNull(map.get("RANGE_NO"));
					detail[1]=BaseUtils.checkNull(map.get("SUPPLY_CODE"));
					detail[2]=BaseUtils.checkNull(map.get("SUPPLY_NAME"));
					detail[3]=BaseUtils.checkNull(map.get("OUT_PART_CODE"));
					detail[4]=BaseUtils.checkNull(map.get("OUT_PART_NAME"));
					detail[5]=BaseUtils.checkNull(map.get("CLAIM_NO"));
					detail[6]=BaseUtils.checkNull(map.get("VIN"));
					detail[7]=BaseUtils.checkNull(map.get("OUT_AMOUT"));
					detail[8]=BaseUtils.checkNull(map.get("OUT_DATE"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出出库查询报表"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> reportRefuseQuestions(
			RequestWrapper request, AclUserBean loginUser, Integer page, Integer currPage,
			Map<String, String> params) {
		StringBuffer sb= new StringBuffer();
		sb.append("--服务站拒赔问题汇总\n" );
		sb.append("\n" );
		sb.append("select d.dealer_code, --服务站号\n" );
		sb.append("       d.dealer_shortname, --服务站简称\n" );
		sb.append("       a.transport_no, --货运票号\n" );
		sb.append("       nvl(a.tran_person, transport_type) tran_person, --到货方式\n" );
		sb.append("       a.real_box_no, --总箱数\n" );
		sb.append("       a.return_amount, --拒赔数量\n" );
		sb.append("       a.in_warhouse_by, --鉴定员\n" );
		sb.append("       a.return_cycle, --旧件月份\n" );
		sb.append("       a.in_no, --入库编号\n" );
		sb.append("       a.deduct_remark, --存在问题\n" );
		sb.append("       a.part_pakge || ';' || a.part_mark || ';' || a.part_detail evaluation --验收评价\n" );
		sb.append("  from (with ord as\n" );
		sb.append("       （select t.return_id return_id, t.deduct_remark deduct_remark, sum(t.return_amount) return_amount\n" );
		sb.append("           from tt_as_wr_old_returned_detail t\n" );
		sb.append("          where t.sign_amount = 0\n" );
		sb.append("            and t.executive_director_sta = 1\n" );
		sb.append("          group by t.return_id, t.deduct_remark)\n" );
		sb.append("        select ort.dealer_id dealer_id,\n" );
		sb.append("               ort.transport_no transport_no,\n" );
		sb.append("               (select c.code_desc from tc_code c where c.code_id = ort.transport_type) transport_type,\n" );
		sb.append("               (select t.transport_name from tm_oldpart_transport_detail t where t.detail_id = ort.tran_person) tran_person,\n" );
		sb.append("               ort.real_box_no real_box_no,\n" );
		sb.append("               ord.return_amount return_amount,\n" );
		sb.append("               (select u.name from tc_user u where u.user_id = ort.in_warhouse_by) in_warhouse_by,\n" );
		sb.append("               (select ro.wr_start_date || '至' || to_char(ro.return_end_date, 'yyyy-mm-dd') from tt_as_wr_returned_order ro where ro.return_no = ort.tran_no) return_cycle,\n" );
		sb.append("               '' in_no,\n" );
		sb.append("               case ord.deduct_remark\n" );
		sb.append("                 when 0 then\n" );
		sb.append("                  '其他'\n" );
		sb.append("                 else\n" );
		sb.append("                  (select c.code_desc from tc_code c where c.code_id = ord.deduct_remark)\n" );
		sb.append("               end deduct_remark,\n" );
		sb.append("               (select c.type_name || ':' || c.code_desc from tc_code c where c.code_id = ort.part_pakge) part_pakge,\n" );
		sb.append("               (select c.type_name || ':' || c.code_desc from tc_code c where c.code_id = ort.part_mark) part_mark,\n" );
		sb.append("               (select c.type_name || ':' || c.code_desc from tc_code c where c.code_id = ort.part_detail) part_detail\n" );
		sb.append("          from tt_as_wr_old_returned ort, ord\n" );
		sb.append("         where ort.id = ord.return_id\n" );

		
		if(params.get("startDate")!=null&&params.get("endDate")!=null){
			sb.append("              and trunc(ort.in_warhouse_date) between to_date('"+params.get("startDate")+"','yyyy-mm-dd') and to_date('"+params.get("endDate")+"','yyyy-mm-dd')\n" );
		}
		
		sb.append("           and ort.status = 10811005) a, tm_dealer d\n" );
		sb.append("         where a.dealer_id = d.dealer_id\n" );
		if (this.getPoseRoleId(loginUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
	       sb.append(CommonUtils.getOrgDealerLimitSqlByPose("d", loginUser));
		}
		if(params.get("FaCode")!=null&&!params.get("FaCode").equals("")){
			sb.append("           and d.dealer_code like '%"+params.get("FaCode")+"%'\n" );
		}
		if(params.get("FaShortName")!=null&&!params.get("FaShortName").equals("")){
			sb.append("           and d.dealer_name like '%"+params.get("FaShortName")+"%'\n" );
		}
		sb.append("         order by d.dealer_code, a.transport_no, a.in_warhouse_by asc");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public void expotDataRefuseQuestions(ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[12];
			head[0]="服务站号";
			head[1]="服务站简称";
			head[2]="货运票号";
			head[3]="到货方式";
			head[4]="总箱数";
			head[5]="拒赔数量";
			head[6]="鉴定员";
			head[7]="旧件月份";
			head[8]="入库编号";
			head[9]="存在问题";
			head[10]="验收评价";
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[12];
					detail[0]=BaseUtils.checkNull(map.get("DEALER_CODE"));
					detail[1]=BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					detail[2]=BaseUtils.checkNull(map.get("TRANSPORT_NO"));
					detail[3]=BaseUtils.checkNull(map.get("TRAN_PERSON"));
					detail[4]=BaseUtils.checkNull(map.get("REAL_BOX_NO"));
					detail[5]=BaseUtils.checkNull(map.get("RETURN_AMOUNT"));
					detail[6]=BaseUtils.checkNull(map.get("IN_WARHOUSE_BY"));
					detail[7]=BaseUtils.checkNull(map.get("RETURN_CYCLE"));
					detail[8]=BaseUtils.checkNull(map.get("IN_NO"));
					detail[9]=BaseUtils.checkNull(map.get("DEDUCT_REMARK"));
					detail[10]=BaseUtils.checkNull(map.get("EVALUATION"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出服务站拒赔问题汇总报表"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> reportClaimParts(RequestWrapper request, AclUserBean loginUser, Integer page, Integer currPage,Map<String, String> params) {
		String startdate =params.get("startDate");
		String enddate =params.get("endDate");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date startdate1=null;
		Date enddate1=null;
		Date onlineDate=null;
		try {
			 startdate1 =dateFormat.parse(startdate);
			 enddate1 =dateFormat.parse(enddate);
			 onlineDate =dateFormat.parse(MyConstant.onlineDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Long start = startdate1.getTime();
		Long end = enddate1.getTime();
		Long online = onlineDate.getTime();
	
		StringBuffer sb= new StringBuffer();
		sb.append("--索赔单零件查询\n" );
		sb.append("with a as(\n" );
		sb.append("select awa.claim_no claim_no,--索赔单号\n" );
		sb.append("       d.dealer_code dealer_code,--商家代码\n" );
		sb.append("       d.dealer_shortname dealer_shortname,--商家简称\n" );
		sb.append("       p.group_name model_name, --车型\n" );
		sb.append("       (select  c.maker_shotname maker_shotname from tt_part_maker_define c where c.maker_code=awp.producer_code)  producer_name,--供应商名称\n" );
		sb.append("       awp.producer_code producer_code,--供应商代码\n" );
		sb.append("       awp.down_part_code part_code,--零件代码\n" );
		sb.append("       awp.down_part_name part_name,--零件名称\n" );
		sb.append("       awp.balance_quantity balance_quantity,--零件数量\n" );
		sb.append("       awa.vin vin,--车架号\n" );
		sb.append("       awa.claim_type claim_type,--索赔类型\n" );
		sb.append("       nvl(awp.balance_amount,0) part_amount,--材料费\n" );
		sb.append("       case awp.responsibility_type\n" );
		sb.append("         when 94001001 then\n" );
		if (start<=online && end<=online) {//新分单时间节点
		sb.append("          nvl((select awl.balance_amount + nvl((select sum(awl.balance_amount) balance_amount from tt_as_wr_labouritem awl where awl.id = awp.id and awl.first_part is null),0) balance_amount\n" );
		sb.append("             from tt_as_wr_labouritem awl\n" );
		sb.append("            where awl.id = awp.id\n" );
		sb.append("              and awl.wr_labourcode = awp.wr_labourcode),0)\n" );
		}else if (start>online && end>online) {
			sb.append(" nvl((select sum(awl.balance_amount)  from tt_as_wr_labouritem awl where awl.id = awa.id),0)\n" );
		}
		sb.append("         else\n" );
		sb.append("          nvl((select awl.balance_amount from tt_as_wr_labouritem awl where awl.id = awp.id and awl.wr_labourcode = awp.wr_labourcode),0)\n" );
		sb.append("       end labour_amount,--工时费\n" );
		sb.append("       nvl((select sum(nvl(awn.balance_amount,0)) from tt_as_wr_netitem awn where awn.item_code not in('QT011','QT003','QT004','QT005') and awn.id=awp.id),0) out_amount,--外出费\n" );
		sb.append("       case awp.responsibility_type\n" );
		sb.append("         when 94001001 then\n" );
		sb.append("          nvl((select sum(nvl(acc.price,0)) from tt_claim_accessory_dtl acc where acc.claim_no=awa.claim_no),0)\n" );
		sb.append("         else 0 end acc_amount,--辅料费\n" );
		sb.append("       case awp.responsibility_type\n" );
		sb.append("         when 94001001 then\n" );
		sb.append("          nvl((select sum(nvl(awn.balance_amount,0)) from tt_as_wr_netitem awn where awn.item_code='QT005' and awn.id=awp.id),0)+\n" );
		sb.append("          nvl((select sum(nvl(ca.pass_price,0)) from tt_as_wr_compensation_app ca where ca.claim_no=awa.claim_no),0)\n" );
		sb.append("         else 0 end other_amount--其他\n" );
		sb.append("  from  tm_dealer d, tt_as_wr_partsitem awp,tt_as_wr_application awa  left join  tm_vehicle b on   awa.vin = b.vin  left  join     tm_vhcl_material_group  p  on b.Model_Id = p.group_id \n" );
		sb.append(" where awa.dealer_id = d.dealer_id\n" );
		sb.append("   and awa.id = awp.id\n" );
		if(params.get("carId")!=null){
			sb.append("   and awa.model_id='"+params.get("carId")+"'--车型条件\n" );
		}
		if(params.get("claim_type")!=null){
			sb.append("   and awa.claim_type="+params.get("claim_type")+"--索赔类型条件\n" );
		}
		if(params.get("startDate")!=null&&params.get("endDate")!=null){
			sb.append("   and trunc(awa.report_date) between to_date('"+params.get("startDate")+"','yyyy-mm-dd') and to_date('"+params.get("endDate")+"','yyyy-mm-dd')\n" );
		}
		if(params.get("claim_type")!=null){
			sb.append("   and awa.claim_type="+params.get("claim_type")+"--索赔类型条件\n" );
		}
		if(params.get("dealerCode")!=null){
			sb.append("   and d.dealer_code in ( "+params.get("dealerCode")+")--服务站条件\n" );
		}
		
		if(params.get("partCode")!=null){
			sb.append("   and awp.down_part_code like '%"+params.get("partCode")+"%'--零件代码条件\n" );
		}
		if(params.get("partName")!=null){
			sb.append("   and awp.down_part_name like '%"+params.get("partName")+"%'--零件名称条件\n" );
		}
		if(params.get("claim_type")!=null){
			sb.append("   and awa.claim_type ="+params.get("claim_type")+" --零件名称条件\n" );
		}
		String pose=this.getPoseRoleId(loginUser.getPoseId().toString());
		 if (pose.equals(Constant.FWJL_ROLE_ID)) {
             sb.append(CommonUtils.getOrgDealerLimitSqlByPose("d", loginUser));
         }
		sb.append("  order by awa.report_date,awa.claim_no asc\n" );
		sb.append(")\n" );
		sb.append("select a.claim_no,\n" );
		sb.append("       a.dealer_code,\n" );
		sb.append("       a.model_name,\n" );
		sb.append("       a.producer_code,\n" );
		sb.append("       a.producer_name,\n" );
		sb.append("       a.dealer_shortname,\n" );
		sb.append("       a.part_code,\n" );
		sb.append("       a.part_name,\n" );
		sb.append("       a.balance_quantity,\n" );
		sb.append("       a.vin,\n" );
		sb.append("       (select c.code_desc from tc_code c where c.code_id = a.claim_type) claim_type,\n" );
		sb.append("       a.part_amount,\n" );
		sb.append("       a.labour_amount,\n" );
		sb.append("       a.out_amount,\n" );
		sb.append("       a.acc_amount,\n" );
		sb.append("       a.other_amount,\n" );
		sb.append("       a.part_amount + a.labour_amount + a.out_amount + a.acc_amount + a.other_amount amount\n" );
		sb.append("  from a\n" );
		sb.append("union all\n" );
		sb.append("select '合计','','','','','','','',sum(a.balance_quantity),'','',sum(a.part_amount),sum(a.labour_amount),sum(a.out_amount),sum(a.acc_amount),sum(a.other_amount),sum(a.part_amount+a.labour_amount+a.out_amount+a.acc_amount+a.other_amount) from a\n" );
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), Constant.PAGE_SIZE, currPage);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public void expotDataClaimParts(ActionContext act,PageResult<Map<String, Object>> list) {
		// 索赔单号	商家代码	商家简称	零件代码	零件名称	零件数量	车架号	索赔类型	工时费	材料费	外出费	辅料费	其他	合计
		try {
			String[] head=new String[17];
			head[0]="索赔单号";
			head[1]="商家代码";
			head[2]="商家简称";
			head[3]="车型";
			head[4]="供应商代码";
			head[5]="供应商名称";
			head[6]="零件代码";
			head[7]="零件名称";
			head[8]="零件数量";
			head[9]="车架号";
			head[10]="索赔类型";
			head[11]="工时费";
			head[12]="材料费";
			head[13]="外出费";
			head[14]="辅料费";
			head[15]="其他";
			head[16]="合计";
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[17];
					detail[0]=BaseUtils.checkNull(map.get("CLAIM_NO"));
					detail[1]=BaseUtils.checkNull(map.get("DEALER_CODE"));
					detail[2]=BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					detail[3]=BaseUtils.checkNull(map.get("MODEL_NAME"));
					detail[4]=BaseUtils.checkNull(map.get("PRODUCER_CODE"));
					detail[5]=BaseUtils.checkNull(map.get("PRODUCER_NAME"));
					detail[6]=BaseUtils.checkNull(map.get("PART_CODE"));
					detail[7]=BaseUtils.checkNull(map.get("PART_NAME"));
					detail[8]=BaseUtils.checkNull(map.get("BALANCE_QUANTITY"));
					detail[9]=BaseUtils.checkNull(map.get("VIN"));
					detail[10]=BaseUtils.checkNull(map.get("CLAIM_TYPE"));
					detail[11]=BaseUtils.checkNull(map.get("PART_AMOUNT"));
					detail[12]=BaseUtils.checkNull(map.get("LABOUR_AMOUNT"));
					detail[13]=BaseUtils.checkNull(map.get("OUT_AMOUNT"));
					detail[14]=BaseUtils.checkNull(map.get("ACC_AMOUNT"));
					detail[15]=BaseUtils.checkNull(map.get("OTHER_AMOUNT"));
					detail[16]=BaseUtils.checkNull(map.get("AMOUNT"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出索赔单零件查询报表"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> reportproductCar(
			RequestWrapper request, Integer page, Integer currPage,
			Map<String, String> params) {
		StringBuffer sb= new StringBuffer();
		sb.append("select a.vin,\n" );
		sb.append("       VMGM.SERIES_NAME,\n" );
		sb.append("       VMGM.MODEL_NAME,\n" );
		sb.append("       VMGM.PACKAGE_NAME,\n" );
		sb.append("       VMGM.MATERIAL_CODE,\n" );
		sb.append("       VMGM.MATERIAL_NAME\n" );
		sb.append("  from tm_vehicle a, VW_MATERIAL_GROUP_MAT VMGM\n" );
		sb.append(" where a.material_id = VMGM.MATERIAL_ID\n" );
		sb.append("   and trunc(a.product_date) between\n" );
		sb.append("       last_day(add_months(to_date('"+params.get("date")+"', 'yyyy-mm'), -"+params.get("N")+")) + 1 and\n" );
		sb.append("       last_day(add_months(to_date('"+params.get("date")+"', 'yyyy-mm'), -0))");
		if(params.get("VIN")!=null){
			sb.append(" and a.vin like  '%"+params.get("VIN")+"%'");
		}
		


		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> reportsaleCar(
			RequestWrapper request, Integer page, Integer currPage,
			Map<String, String> params) {
		StringBuffer sb= new StringBuffer();
		sb.append("select b.vin,\n" );
		sb.append("       to_char(a.sales_date,'yyyy-MM-dd') as sales_date,\n" );
		sb.append("       VMGM.SERIES_NAME,\n" );
		sb.append("       VMGM.MODEL_NAME,\n" );
		sb.append("       VMGM.PACKAGE_NAME,\n" );
		sb.append("       VMGM.MATERIAL_CODE,\n" );
		sb.append("       VMGM.MATERIAL_NAME\n" );
		sb.append("  from tt_dealer_actual_sales a, tm_vehicle b, VW_MATERIAL_GROUP_MAT VMGM\n" );
		sb.append(" where a.vehicle_id = b.vehicle_id\n" );
		sb.append("   and VMGM.MATERIAL_ID = b.material_id\n" );
		sb.append("\tand b.life_cycle = 10321004\n" );
		sb.append("   --and is_return = 10041002\n" );
		sb.append("  and trunc(b.product_date) between\n" );
		sb.append("       last_day(add_months(to_date('"+params.get("date")+"', 'yyyy-mm'), -"+params.get("N")+")) + 1 and\n" );
		sb.append("       last_day(add_months(to_date('"+params.get("date")+"', 'yyyy-mm'), -0))\n" );
		sb.append("\tand trunc(a.sales_date) between\n" );
		sb.append("               last_day(add_months(to_date('"+params.get("date")+"', 'yyyy-mm'), -1)) + 1 and\n" );
		sb.append("               last_day(add_months(to_date('"+params.get("date")+"', 'yyyy-mm'), -0))");
		if(params.get("VIN")!=null){
			sb.append("and vin like '%"+params.get("VIN")+"%'");
		}
		
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public void expotDataProductCar (ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[7];
			head[0]="车架号";
			head[1]="车系";
			head[2]="车型";
			head[3]="配置";
			head[4]="编号";
			head[5]="车辆信息";
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[7];
					
					detail[0]=BaseUtils.checkNull(map.get("VIN"));
					detail[1]=BaseUtils.checkNull(map.get("SERIES_NAME"));
					detail[2]=BaseUtils.checkNull(map.get("MODEL_NAME"));
					detail[3]=BaseUtils.checkNull(map.get("PACKAGE_NAME"));
					detail[4]=BaseUtils.checkNull(map.get("MATERIAL_CODE"));
					detail[5]=BaseUtils.checkNull(map.get("MATERIAL_NAME"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出生产车辆报表"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public void expotDataSaleCar (ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[7];
			head[0]="车架号";
			head[1]="销售时间";
			head[2]="车系";
			head[3]="车型";
			head[4]="配置";
			head[5]="编号";
			head[6]="车辆信息";
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[7];
					detail[0]=BaseUtils.checkNull(map.get("VIN"));
					detail[1]=BaseUtils.checkNull(map.get("SALES_DATE"));
					detail[2]=BaseUtils.checkNull(map.get("SERIES_NAME"));
					detail[3]=BaseUtils.checkNull(map.get("MODEL_NAME"));
					detail[4]=BaseUtils.checkNull(map.get("PACKAGE_NAME"));
					detail[5]=BaseUtils.checkNull(map.get("MATERIAL_CODE"));
					detail[6]=BaseUtils.checkNull(map.get("MATERIAL_NAME"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出销售车辆报表"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> reportClaimCar(
			RequestWrapper request, Integer page, Integer currPage,
			Map<String, String> params) {
		StringBuffer sb= new StringBuffer();
		sb.append("select ap.claim_no,vv.vin,ap.ro_no,ap.ro_startdate, ");
		sb.append(" ( select co.code_desc from tc_code co where co.code_id=ap.claim_type) as code_desc ");

		sb.append("from tm_vehicle             vv,\n" );
		sb.append("               tt_dealer_actual_sales das,\n" );
		sb.append("               tt_as_wr_application   ap\n" );
		sb.append("           --    vd\n" );
		sb.append("         where 1=1  and trunc(ap.report_date) between\n" );
		sb.append("               last_day(add_months(to_date('"+params.get("date")+"', 'yyyy-mm'), -1)) + 1\n" );
		Integer N_1=Integer.parseInt( params.get("N"))-1;
		sb.append("           and last_day(add_months(to_date('"+params.get("date")+"', 'yyyy-mm'), +"+N_1+"))\n" );
		sb.append("           and ap.status = 10791008\n" );
		sb.append("           and vv.vin = ap.vin\n" );
		sb.append("           and vv.life_cycle = 10321004\n" );
		sb.append("           and vv.vehicle_id = das.vehicle_id\n" );
		sb.append("           and trunc(vv.product_date) between\n" );
		sb.append("               last_day(add_months(to_date('"+params.get("date")+"', 'yyyy-mm'), -"+params.get("N")+")) + 1\n" );
		sb.append("           and last_day(add_months(to_date('"+params.get("date")+"', 'yyyy-mm'), -0))\n" );
		sb.append("           and trunc(das.sales_date) between\n" );
		sb.append("               last_day(add_months(to_date('"+params.get("date")+"', 'yyyy-mm'), -1)) + 1 and\n" );
		sb.append("               last_day(add_months(to_date('"+params.get("date")+"', 'yyyy-mm'), -0))\n" );
		sb.append("           and vv.model_id in ("+params.get("model")+")");

		if(params.get("VIN")!=null){
			sb.append("and vv.vin like '%"+params.get("VIN")+"%'");
		}
		
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public void expotDataClaimCar (ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[7];
			
			head[0]="车架号";
			head[1]="索赔单号";
			head[2]="工单号";
			head[3]="开单日期";
			head[4]="索赔类型";
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[7];
					
					detail[0]=BaseUtils.checkNull(map.get("VIN"));
					detail[1]=BaseUtils.checkNull(map.get("CLAIM_NO"));
					detail[2]=BaseUtils.checkNull(map.get("RO_NO"));
					detail[3]=BaseUtils.checkNull(map.get("RO_STARTDATE"));
					detail[4]=BaseUtils.checkNull(map.get("CODE_DESC"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出索赔车辆报表"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> reportMonthUgencyParts(
			RequestWrapper request, AclUserBean loginUser, Integer page, Integer currPage,
			Map<String, String> params) {
		StringBuffer sb= new StringBuffer();
		sb.append("--月紧急调件汇总表\n" );
		sb.append("with tb_1 as (");
		sb.append("select vpb.DEALER_CODE,\n" );
		sb.append("       vpb.DEALER_SHORTNAME,\n" );
		sb.append("       vpb.CLAIM_NO,\n" );
		sb.append("       vpb.VIN,\n" );
		sb.append("       vpb.PART_NAME,\n" );
		sb.append("       vpb.PART_CODE,\n" );
		sb.append("       vpb.ISSUED_PERSON,\n" );
		sb.append("       trunc(vpb.issued_date) issued_date,\n" );
		sb.append("       trunc(vpb.require_send_date) require_send_date,\n" );
		sb.append("       trunc(vpb.REQUIRE_ARRIVED_DATE) require_arrived_date,\n" );
		sb.append("       vpb.delay_return_days,\n" );
		sb.append("       decode(vpb.status,0,'调件未下发',1,'件未回运',2,'货运在途',3,'件已入库',4,'件已借出',5,'件已归还',6,'已签收未审核',7,'已扣件') status,\n" );
		sb.append("       vpb.BORROW_REASON,\n" );
		sb.append("       trunc(vpb.send_date) send_date,\n" );
		sb.append("       vpb.TRANS_NO,\n" );
		sb.append("       vpb.TRANS_COMP,\n" );
		sb.append("       trunc(vpb.in_date) in_date,\n" );
		sb.append("       vpb.BORROW_MAN,\n" );
		sb.append("       trunc(vpb.borrow_date) borrow_date,\n" );
		sb.append("       vpb.BORROW_PERSON,\n" );
		sb.append("       vpb.RETURN_MAN,\n" );
		sb.append("       trunc(vpb.return_date) return_date,\n" );
		sb.append("       vpb.RETURN_PERSION, vpb.ISDIS\n" );
		sb.append("  from vw_part_borrow vpb\n" );
		sb.append(" where 1 = 1\n" );
		sb.append(" order by vpb.delay_return_days desc");
		sb.append(")");
		sb.append("select * from tb_1 where 1=1 ");

		if(params.get("dealerCode")!=null){
			sb.append(" and  DEALER_CODE like '%"+params.get("dealerCode")+"%' \n");
		}
		if(params.get("dealerName")!=null){
			sb.append(" and DEALER_SHORTNAME like '%"+params.get("dealerName")+"%' \n");
		}
		if(params.get("partCode")!=null){
			sb.append(" and PART_CODE like '%"+params.get("partCode")+"%' \n");
		}
		if(params.get("partName")!=null){
			sb.append(" and PART_NAME like '%"+params.get("partName")+"%' \n");
		}
		if(params.get("tranNo")!=null){
			sb.append(" and TRANS_NO like '%"+params.get("tranNo")+"%' \n");
		}
		if(params.get("claimNo")!=null){
			sb.append(" and CLAIM_NO like '%"+params.get("claimNo")+"%' \n");
		}
		if(params.get("VIN")!=null){
			sb.append(" and VIN  like '%"+params.get("VIN")+"%' \n");
		}
		if(params.get("delayDay")!=null){
			sb.append(" and DELAY_RETURN_DAYS  >="+params.get("delayDay")+" \n");
		}
		if(params.get("transSituation")!=null){
			sb.append(" and STATUS  ='"+params.get("transSituation")+"' \n");
		}
		if(params.get("publishStartDate")!=null){
			sb.append(" and to_date(to_char(ISSUED_DATE,'yyyy-MM-dd'),'yyyy-MM-dd') >= to_date('"+params.get("publishStartDate")+"','yyyy-MM-dd') \n");
		}
		if(params.get("publishEndDate")!=null){
			sb.append(" and to_date(to_char(ISSUED_DATE,'yyyy-MM-dd'),'yyyy-MM-dd') <= to_date('"+params.get("publishEndDate")+"','yyyy-MM-dd') \n");
		}
		if(params.get("transStartDate")!=null){
			sb.append(" and to_date(to_char(SEND_DATE,'yyyy-MM-dd'),'yyyy-MM-dd') >= to_date('"+params.get("transStartDate")+"','yyyy-MM-dd') \n");
		}
		if(params.get("transEndDate")!=null){
			sb.append(" and to_date(to_char(SEND_DATE,'yyyy-MM-dd'),'yyyy-MM-dd') <= to_date('"+params.get("transEndDate")+"','yyyy-MM-dd') \n");
		}
		if(params.get("inStartDate")!=null){
			sb.append(" and to_date(to_char(IN_DATE,'yyyy-MM-dd'),'yyyy-MM-dd') >= to_date('"+params.get("inStartDate")+"','yyyy-MM-dd') \n");
		}
		if(params.get("inEndDate")!=null){
			sb.append(" and to_date(to_char(IN_DATE,'yyyy-MM-dd'),'yyyy-MM-dd') <= to_date('"+params.get("inEndDate")+"','yyyy-MM-dd') \n");
		}
		if(params.get("borrowStartDate")!=null){
			sb.append(" and to_date(to_char(BORROW_DATE,'yyyy-MM-dd'),'yyyy-MM-dd') >= to_date('"+params.get("borrowStartDate")+"','yyyy-MM-dd') \n");
		}
		if(params.get("borrowEndDate")!=null){
			sb.append(" and to_date(to_char(BORROW_DATE,'yyyy-MM-dd'),'yyyy-MM-dd') <= to_date('"+params.get("borrowEndDate")+"','yyyy-MM-dd') \n");
		}
		if(params.get("returnStartDate")!=null){
			sb.append(" and to_date(to_char(RETURN_DATE,'yyyy-MM-dd'),'yyyy-MM-dd') >= to_date('"+params.get("returnStartDate")+"','yyyy-MM-dd') \n");
		}
		if(params.get("returnEndDate")!=null){
			sb.append(" and to_date(to_char(RETURN_DATE,'yyyy-MM-dd'),'yyyy-MM-dd') <= to_date('"+params.get("returnEndDate")+"','yyyy-MM-dd') \n");
		}
		if(params.get("issued_person")!=null){
			sb.append(" and issued_person  like'%"+params.get("issued_person")+"%' \n");
		}
		String isDis = DaoFactory.getParam(request, "isDis");
		if(!"".equals(isDis)){
			DaoFactory.getsql(sb, "ISDIS",isDis , 1);
		}else{
			DaoFactory.getsql(sb, "ISDIS","18041001,18041002" , 6);
		}
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public void expotDataMonthUgencyParts(ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[23];
	
			
			head[0]="经销商代码";
			head[1]="经销商简称";
			head[2]="索赔单号";
			head[3]="车辆VIN号";
			head[4]="故障件名称";
			
			head[5]="故障件代码";
			head[6]="下发人";
			head[7]="下发时间";
			head[8]="要求启运时间";
			head[9]="要求到货时间";
			head[10]="延误返件天数";
			head[11]="调件情况";
			head[12]="调件原因";
			head[13]="服务商返回时间";
			head[14]="货运单号";
			head[15]="货运公司";
			head[16]="到库时间";
			head[17]="借件人";
			head[18]="借件时间";
			head[19]="借件确认人";
			head[20]="还件人";
			head[21]="还件时间";
			head[22]="还件确认人";
			
			
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[23];
					
					
					detail[0]=BaseUtils.checkNull(map.get("DEALER_CODE"));
					detail[1]=BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					detail[2]=BaseUtils.checkNull(map.get("CLAIM_NO"));
					detail[3]=BaseUtils.checkNull(map.get("VIN"));
					detail[4]=BaseUtils.checkNull(map.get("PART_NAME"));
					
					detail[5]=BaseUtils.checkNull(map.get("PART_CODE"));
					detail[6]=BaseUtils.checkNull(map.get("ISSUED_PERSON"));
					detail[7]=BaseUtils.checkNull(map.get("ISSUED_DATE"));
					detail[8]=BaseUtils.checkNull(map.get("REQUIRE_SEND_DATE"));
					detail[9]=BaseUtils.checkNull(map.get("REQUIRE_ARRIVED_DATE"));
					detail[10]=BaseUtils.checkNull(map.get("DELAY_RETURN_DAYS"));
					detail[11]=BaseUtils.checkNull(map.get("STATUS"));
					detail[12]=BaseUtils.checkNull(map.get("BORROW_REASON"));
					detail[13]=BaseUtils.checkNull(map.get("SEND_DATE"));
					detail[14]=BaseUtils.checkNull(map.get("TRANS_NO"));
					detail[15]=BaseUtils.checkNull(map.get("TRANS_COMP"));
					detail[16]=BaseUtils.checkNull(map.get("IN_DATE"));
					detail[17]=BaseUtils.checkNull(map.get("BORROW_MAN"));
					detail[18]=BaseUtils.checkNull(map.get("BORROW_DATE"));
					detail[19]=BaseUtils.checkNull(map.get("BORROW_PERSON"));
					detail[20]=BaseUtils.checkNull(map.get("RETURN_MAN"));
					detail[21]=BaseUtils.checkNull(map.get("RETURN_DATE"));
					detail[22]=BaseUtils.checkNull(map.get("RETURN_PERSION"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出月紧急调件汇总报表"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


		@SuppressWarnings("unchecked")
		public void repairOrderQueryExport(ActionContext act,PageResult<Map<String, Object>> list) {
			 try {
			    String[] head={"大区","经销商代码","经销商简称","工单号","活动代码","结算基地","车牌号","购车日期","VIN","物料名称","车辆颜色","车系","车型","车主","车主电话","送修人","送修人电话","工单开始时间","进场里程数","工单状态","单据保养次数"};
				List<Map<String, Object>> records = list.getRecords();
				List params=new ArrayList();
				if(records!=null &&records.size()>0){
					for (Map<String, Object> map : records) {
						String[] detail=new String[21];
						detail[0]=BaseUtils.checkNull(map.get("AREA_NAME"));
						detail[1]=BaseUtils.checkNull(map.get("DEALER_CODES"));
						detail[2]=BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
						detail[3]=BaseUtils.checkNull(map.get("RO_NO"));
						detail[4]=BaseUtils.checkNull(map.get("CAMPAIGN_CODE"));
						detail[5]=this.getTypeDesc(BaseUtils.checkNull(map.get("BALANCE_YIELDLY")));//结算地
						detail[6]=BaseUtils.checkNull(map.get("LICENSE"));
						detail[7]=BaseUtils.checkNull(map.get("PURCHASED_DATE"));
						detail[8]=BaseUtils.checkNull(map.get("VIN"));
						detail[9]=BaseUtils.checkNull(map.get("MATERIAL_NAME"));
						detail[10]=BaseUtils.checkNull(map.get("COLOR_NAME"));
						detail[11]=BaseUtils.checkNull(map.get("GROUP_NAME"));
						detail[12]=BaseUtils.checkNull(map.get("MODEL"));
						detail[13]=BaseUtils.checkNull(map.get("OWNER_NAME"));
						detail[14]=BaseUtils.checkNull(map.get("OWNER_PHONE"));
						detail[15]=BaseUtils.checkNull(map.get("DELIVERER"));
						detail[16]=BaseUtils.checkNull(map.get("DELIVERER_PHONE"));
						detail[17]=BaseUtils.checkNull(map.get("RO_CREATE_DATE"));
						detail[18]=BaseUtils.checkNull(map.get("IN_MILEAGE"));
						detail[19]=this.getTypeDesc(BaseUtils.checkNull(map.get("RO_STATUS")));
						detail[20]=BaseUtils.checkNull(map.get("FREE_TIMES"));
						params.add(detail);
					}
				}
				    String systemDateStr = BaseUtils.getSystemDateStr();
					BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "维修工单数据导出"+systemDateStr+".xls", "导出数据",null);
				} catch (Exception e) {
					e.printStackTrace();
				}
		}

		public PageResult<Map<String, Object>> ClaimoldpartQuery(RequestWrapper request, AclUserBean loginUser, Integer page,Integer currPage) {
			
			return null;
		}

		@SuppressWarnings("unchecked")
		public PageResult<Map<String, Object>> reportClaimParts1(RequestWrapper request, AclUserBean loginUser, Integer page,Integer currPage, Map<String, String> params) {
			String startdate =params.get("startDate");
			String enddate =params.get("endDate");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date startdate1=null;
			Date enddate1=null;
			Date onlineDate=null;
			try {
				 startdate1 =dateFormat.parse(startdate);
				 enddate1 =dateFormat.parse(enddate);
				 onlineDate =dateFormat.parse(MyConstant.onlineDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Long start = startdate1.getTime();
			Long end = enddate1.getTime();
			Long online = onlineDate.getTime();
			
				
			
				
		
			StringBuffer sb= new StringBuffer();
			sb.append("--索赔单零件查询\n" );
			sb.append("with a as(\n" );
			sb.append("select awa.claim_no claim_no,--索赔单号\n" );
			sb.append("       d.dealer_code dealer_code,--商家代码\n" );
			sb.append("       d.dealer_shortname dealer_shortname,--商家简称\n" );
			sb.append("       p.group_name model_name, --车型\n" );
			sb.append("       (select  c.maker_shotname maker_shotname from tt_part_maker_define c where c.maker_code=awp.producer_code)  producer_name,--供应商名称\n" );
			sb.append("       awp.producer_code producer_code,--供应商代码\n" );
			sb.append("       awp.down_part_code part_code,--零件代码\n" );
			sb.append("       awp.down_part_name part_name,--零件名称\n" );
			sb.append("       awp.balance_quantity balance_quantity,--零件数量\n" );
			sb.append("       awa.vin vin,--车架号\n" );
			sb.append("       awa.claim_type claim_type,--索赔类型\n" );
			sb.append("       nvl(awp.balance_amount,0) part_amount,--材料费\n" );
			sb.append("       case awp.responsibility_type\n" );
			sb.append("         when 94001001 then\n" );
			if (start<=online && end<=online) {//新分单时间节点
			sb.append("          nvl((select awl.balance_amount + nvl((select sum(awl.balance_amount) balance_amount from tt_as_wr_labouritem awl where awl.id = awp.id and awl.first_part is null),0) balance_amount\n" );
			sb.append("             from tt_as_wr_labouritem awl\n" );
			sb.append("            where awl.id = awp.id\n" );
			sb.append("              and awl.wr_labourcode = awp.wr_labourcode),0)\n" );
			}else if (start>online && end>online) {
				sb.append(" nvl((select sum(awl.balance_amount)  from tt_as_wr_labouritem awl where awl.id = awa.id),0)\n" );
			}
			sb.append("         else\n" );
			sb.append("          nvl((select awl.balance_amount from tt_as_wr_labouritem awl where awl.id = awp.id and awl.wr_labourcode = awp.wr_labourcode),0)\n" );
			sb.append("       end labour_amount,--工时费\n" );
			sb.append("       nvl((select sum(nvl(awn.balance_amount,0)) from tt_as_wr_netitem awn where awn.item_code not in('QT011','QT003','QT004','QT005') and awn.id=awp.id),0) out_amount,--外出费\n" );
			sb.append("       case awp.responsibility_type\n" );
			sb.append("         when 94001001 then\n" );
			sb.append("          nvl((select sum(nvl(acc.price,0)) from tt_claim_accessory_dtl acc where acc.claim_no=awa.claim_no),0)\n" );
			sb.append("         else 0 end acc_amount,--辅料费\n" );
			sb.append("       case awp.responsibility_type\n" );
			sb.append("         when 94001001 then\n" );
			sb.append("          nvl((select sum(nvl(awn.balance_amount,0)) from tt_as_wr_netitem awn where awn.item_code='QT005' and awn.id=awp.id),0)+\n" );
			sb.append("          nvl((select sum(nvl(ca.pass_price,0)) from tt_as_wr_compensation_app ca where ca.claim_no=awa.claim_no),0)\n" );
			sb.append("         else 0 end other_amount--其他\n" );
			sb.append("  from  tm_dealer d, tt_as_wr_partsitem awp,tt_as_wr_application awa  left join  tm_vehicle b on   awa.vin = b.vin  left  join     tm_vhcl_material_group  p  on b.Model_Id = p.group_id \n" );
			sb.append(" where awa.dealer_id = d.dealer_id\n" );
			sb.append("   and awa.id = awp.id\n" );
			if(params.get("carId")!=null){
				sb.append("   and awa.model_id='"+params.get("carId")+"'--车型条件\n" );
			}
			if(params.get("claim_type")!=null){
				sb.append("   and awa.claim_type="+params.get("claim_type")+"--索赔类型条件\n" );
			}
			if(params.get("startDate")!=null&&params.get("endDate")!=null){
				sb.append("   and trunc(awa.report_date) between to_date('"+params.get("startDate")+"','yyyy-mm-dd') and to_date('"+params.get("endDate")+"','yyyy-mm-dd')\n" );
			}
			if(params.get("claim_type")!=null){
				sb.append("   and awa.claim_type="+params.get("claim_type")+"--索赔类型条件\n" );
			}
			if(params.get("dealerCode")!=null){
				sb.append("   and d.dealer_code in ( "+params.get("dealerCode")+")--服务站条件\n" );
			}
			
			if(params.get("partCode")!=null){
				sb.append("   and awp.down_part_code like '%"+params.get("partCode")+"%'--零件代码条件\n" );
			}
			if(params.get("partName")!=null){
				sb.append("   and awp.down_part_name like '%"+params.get("partName")+"%'--零件名称条件\n" );
			}
			if(params.get("claim_type")!=null){
				sb.append("   and awa.claim_type ="+params.get("claim_type")+" --零件名称条件\n" );
			}
			String pose=this.getPoseRoleId(loginUser.getPoseId().toString());
			 if (pose.equals(Constant.FWJL_ROLE_ID)) {
	             sb.append(CommonUtils.getOrgDealerLimitSqlByPose("d", loginUser));
	         }
			sb.append("  order by awa.report_date,awa.claim_no asc\n" );
			sb.append(")\n" );
			sb.append("select a.claim_no,\n" );
			sb.append("       a.dealer_code,\n" );
			sb.append("       a.model_name,\n" );
			sb.append("       a.producer_code,\n" );
			sb.append("       a.producer_name,\n" );
			sb.append("       a.dealer_shortname,\n" );
			sb.append("       a.part_code,\n" );
			sb.append("       a.part_name,\n" );
			sb.append("       a.balance_quantity,\n" );
			sb.append("       a.vin,\n" );
			sb.append("       (select c.code_desc from tc_code c where c.code_id = a.claim_type) claim_type,\n" );
			sb.append("       a.part_amount,\n" );
			sb.append("       a.labour_amount,\n" );
			sb.append("       a.out_amount,\n" );
			sb.append("       a.acc_amount,\n" );
			sb.append("       a.other_amount,\n" );
			sb.append("       (a.part_amount + a.labour_amount + a.out_amount + a.acc_amount + a.other_amount) amount\n" );
			sb.append("  from a\n" );
			PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
			return list;
			
		}

		@SuppressWarnings("unchecked")
		public PageResult<Map<String, Object>> SettlementReportQuery(RequestWrapper request, AclUserBean loginUser, Integer page,Integer currPage) {
			StringBuffer sql= new StringBuffer();
			sql.append("select distinct vs.root_org_name,\n" );
			sql.append("                tm.dealer_shortname,\n" );
			sql.append("                a.claim_no,\n" );
			sql.append("                (select c.code_desc\n" );
			sql.append("                   from tc_code c\n" );
			sql.append("                  where c.code_id = a.claim_type) as claim_type， a.report_date,\n" );
			sql.append("                vw.MODEL_NAME,\n" );
			sql.append("                a.vin,a.ro_no,\n" );
			sql.append("                cc.ctm_name,\n" );
			sql.append("                cc.main_phone,\n" );
			sql.append("                p.part_code,\n" );
			sql.append("                p.part_name,\n" );
			sql.append("                a.balance_part_amount,\n" );
			sql.append("                a.balance_labour_amount,\n" );
			sql.append("                nvl((nvl(a.accessories_price, 0) +\n" );
			sql.append("                    nvl(a.compensation_money, 0) +\n" );
			sql.append("                    nvl(a.balance_netitem_amount, 0)),\n" );
			sql.append("                    0) as other_amount,\n" );
			sql.append("                nvl(a.accessories_price, 0) + nvl(a.compensation_money, 0) +\n" );
			sql.append("                nvl(a.balance_netitem_amount, 0) +\n" );
			sql.append("                nvl(a.balance_part_amount, 0) +\n" );
			sql.append("                nvl(a.balance_labour_amount, 0) as count_all\n" );
			sql.append("  from Tt_As_Wr_Application      a,\n" );
			sql.append("       tt_as_wr_partsitem        p,\n" );
			sql.append("       tm_vehicle                v,\n" );
			sql.append("       vw_material_group_service vw,\n" );
			sql.append("       tm_dealer                 tm,\n" );
			sql.append("       vw_org_dealer_service     vs,\n" );
			sql.append("       tt_dealer_actual_sales    s,\n" );
			sql.append("       tt_customer               cc\n" );
			sql.append(" where a.vin = v.vin\n" );
			sql.append("   and a.id = p.id\n" );
			sql.append("   and v.vehicle_id = s.vehicle_id(+)\n" );
			sql.append("   and s.ctm_id = cc.ctm_id(+)\n" );
			sql.append("   and a.dealer_id = vs.dealer_id\n" );
			sql.append("   and v.package_id = vw.PACKAGE_ID\n" );
			sql.append("   and a.dealer_id = tm.dealer_id \n");
			String dealerid = loginUser.getDealerId();
			if (BaseUtils.notNull(dealerid)) {
				DaoFactory.getsql(sql, "a.dealer_id", dealerid, 1);
			}else {
				sql.append( CommonUtils.getOrgDealerLimitSqlByPose("tm", loginUser) );//设置权限
			}
			DaoFactory.getsql(sql, "vs.root_org_name", DaoFactory.getParam(request, "root_org_name"), 2);
			DaoFactory.getsql(sql, "a.claim_no", DaoFactory.getParam(request, "claim_no"), 2);
			DaoFactory.getsql(sql, "a.ro_no", DaoFactory.getParam(request, "ro_no"), 2);
			DaoFactory.getsql(sql, "a.vin", DaoFactory.getParam(request, "vin"), 2);
			DaoFactory.getsql(sql, "p.part_code", DaoFactory.getParam(request, "part_code"), 2);
			DaoFactory.getsql(sql, "vw.MODEL_NAME", DaoFactory.getParam(request, "model_name"), 2);
			DaoFactory.getsql(sql, " a.report_date", MyConstant.onlineDate, 3);//查新分单后的
			DaoFactory.getsql(sql, " a.report_date", DaoFactory.getParam(request, "start_date"), 3);
			DaoFactory.getsql(sql, " a.report_date", DaoFactory.getParam(request, "end_date"), 4);
			return this.pageQuery(sql.toString(), null, getFunName(), page, currPage);
		}


		@SuppressWarnings("unchecked")
		public void SettlementExport(ActionContext act, RequestWrapper request,AclUserBean loginUser, Integer page, Integer currPage) {
			PageResult<Map<String, Object>> list = this.SettlementReportQuery(request, loginUser, page, currPage);
			 try {													 	
				    String[] head={"大区","服务站简称","工单号","索赔单号","索赔类型","审核日期","车型","VIN","客户名","客户电话","配件代码","配件名称","材料费","工时费","其他费用","合计"};
					List<Map<String, Object>> records = list.getRecords();
					List params=new ArrayList();
					if(records!=null &&records.size()>0){
						for (Map<String, Object> map : records) {
							String[] detail=new String[16];
							detail[0]=BaseUtils.checkNull(map.get("ROOT_ORG_NAME"));
							detail[1]=BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
							detail[2]=BaseUtils.checkNull(map.get("RO_NO"));
							detail[3]=BaseUtils.checkNull(map.get("CLAIM_NO"));
							detail[4]=BaseUtils.checkNull(map.get("CLAIM_TYPE"));
							detail[5]=BaseUtils.checkNull(map.get("REPORT_DATE"));
							detail[6]=BaseUtils.checkNull(map.get("MODEL_NAME"));
							detail[7]=BaseUtils.checkNull(map.get("VIN"));
							detail[8]=BaseUtils.checkNull(map.get("CTM_NAME"));
							detail[9]=BaseUtils.checkNull(map.get("MAIN_PHONE"));
							detail[10]=BaseUtils.checkNull(map.get("PART_CODE"));
							detail[11]=BaseUtils.checkNull(map.get("PART_NAME"));
							detail[12]=BaseUtils.checkNull(map.get("BALANCE_PART_AMOUNT"));
							detail[13]=BaseUtils.checkNull(map.get("BALANCE_LABOUR_AMOUNT"));
							detail[14]=BaseUtils.checkNull(map.get("OTHER_AMOUNT"));
							detail[15]=BaseUtils.checkNull(map.get("COUNT_ALL"));
							params.add(detail);
						}
					}
					    String systemDateStr = BaseUtils.getSystemDateStr();
						BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "结算汇总报表数据导出"+systemDateStr+".xls", "导出数据",null);
					} catch (Exception e) {
						e.printStackTrace();
					}
		}

		@SuppressWarnings("unchecked")
		public PageResult<Map<String, Object>> OldpartSummaryReportQuery(RequestWrapper request, AclUserBean loginUser, Integer page,Integer currPage) {
			StringBuffer sql= new StringBuffer();
			sql.append("select p.part_code,\n" );
			sql.append("       p.part_name,\n" );
			sql.append("       count(p.part_code) as part_code_count,\n" );
			sql.append("       max(p.price) as price ,\n" );
			sql.append("       sum(p.balance_amount) as balance_amount\n" );
			sql.append("  from Tt_As_Wr_Partsitem p where 1=1 \n" );
			DaoFactory.getsql(sql, "p.part_code",DaoFactory.getParam(request, "part_code"), 2);
			DaoFactory.getsql(sql, "p.part_name",DaoFactory.getParam(request, "part_name"), 2);
			sql.append(" group by p.part_code, p.part_name order by balance_amount desc");
			return this.pageQuery(sql.toString(), null, getFunName(), page, currPage);
		}
		@SuppressWarnings("unchecked")
		public void OldpartSummaryExport(RequestWrapper request,ActionContext act, AclUserBean loginUser, Integer pageSizeMax,Integer currPage) {
			PageResult<Map<String, Object>> list = this.OldpartSummaryReportQuery(request, loginUser, pageSizeMax, currPage);
			 try {				
				    String[] head={"配件代码","配件名称","配件金额","申报数量","申报总金额"};
					List<Map<String, Object>> records = list.getRecords();
					List params=new ArrayList();
					if(records!=null &&records.size()>0){
						for (Map<String, Object> map : records) {
							String[] detail=new String[16];
							detail[0]=BaseUtils.checkNull(map.get("PART_CODE"));
							detail[1]=BaseUtils.checkNull(map.get("PART_NAME"));
							detail[2]=BaseUtils.checkNull(map.get("PRICE"));
							detail[3]=BaseUtils.checkNull(map.get("PART_CODE_COUNT"));
							detail[4]=BaseUtils.checkNull(map.get("BALANCE_AMOUNT"));
							params.add(detail);
						}
					}
					    String systemDateStr = BaseUtils.getSystemDateStr();
						BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "旧件汇总金额查询报表数据导出"+systemDateStr+".xls", "导出数据",null);
					} catch (Exception e) {
						e.printStackTrace();
					}
		}

		@SuppressWarnings("unchecked")
		public PageResult<Map<String, Object>> ClaimDataTrackingQuery(RequestWrapper request, AclUserBean loginUser,Integer pageSizeMax, Integer currPage) {
			StringBuffer sql= new StringBuffer();
			sql.append("  select  * from vw_report_claim_out t where 1=1 \n" );
            DaoFactory.getsql(sql, "t.claim_no",DaoFactory.getParam(request, "claim_no") , 2);
            DaoFactory.getsql(sql, "t.part_code",DaoFactory.getParam(request, "part_code") , 2);
            DaoFactory.getsql(sql, "t.part_name",DaoFactory.getParam(request, "part_name") , 2);
            DaoFactory.getsql(sql, "t.part_use_type",DaoFactory.getParam(request, "part_use_type") , 1);
            DaoFactory.getsql(sql, "t.producer_code",DaoFactory.getParam(request, "producer_code") , 2);
            DaoFactory.getsql(sql, "t.producer_name",DaoFactory.getParam(request, "producer_name") , 2);
            DaoFactory.getsql(sql, "t.is_invoice",DaoFactory.getParam(request, "is_invoice") , 2);
            DaoFactory.getsql(sql, "t.balance_status",DaoFactory.getParam(request, "balance_status") , 2);
            DaoFactory.getsql(sql, "t.is_out",DaoFactory.getParam(request, "is_out") , 2);
            DaoFactory.getsql(sql, "t.STATUS",DaoFactory.getParam(request, "STATUS") , 1);
            DaoFactory.getsql(sql, "t.vin",DaoFactory.getParam(request, "vin") , 2);
            DaoFactory.getsql(sql, "t.sub_date",DaoFactory.getParam(request, "beginTime") , 3);
            DaoFactory.getsql(sql, "t.sub_date",DaoFactory.getParam(request, "endTime") , 4);
            DaoFactory.getsql(sql, "t.report_date",DaoFactory.getParam(request, "auditbeginTime") , 3);
            DaoFactory.getsql(sql, "t.report_date",DaoFactory.getParam(request, "auditendTime") ,4);
            DaoFactory.getsql(sql, "t.dealer_id",DaoFactory.getParam(request, "dealerId") , 2);
            DaoFactory.getsql(sql, "t.OLD_STATUS",DaoFactory.getParam(request, "OLD_STATUS") , 2);

			return this.pageQuery(sql.toString(), null, getFunName(), pageSizeMax, currPage);
		}

		@SuppressWarnings("unchecked")
		public void ClaimDataTrackingQuery(RequestWrapper request,ActionContext act, AclUserBean loginUser, Integer pageSizeMax,Integer currPage) {
			PageResult<Map<String, Object>> list = this.ClaimDataTrackingQuery(request, loginUser, pageSizeMax, currPage);
		   try {				
			    String[] head={"索赔单号","索赔类型","VIN","发动机号","车型","送修人姓名","送修人电话","故障现象","原因分析及处理结果","里程","购车日期","活动代码","工单号","配置","审核状态","旧件代码","旧件名称","数量","维修方式","旧件状态","审件状态","供应代码","供应商名称","工时费","材料费","其他费","补偿费","辅料费","申请总金额","审核总金额","差额","开票状态","票据状态","二次退赔"};
				List<Map<String, Object>> records = list.getRecords();
				List params=new ArrayList();
				if(records!=null &&records.size()>0){

					for (Map<String, Object> map : records) {
						String[] detail=new String[34];
						detail[0]=BaseUtils.checkNull(map.get("CLAIM_NO"));
						detail[1]=BaseUtils.checkNull(map.get("CLAIM_TYPE"));
						detail[2]=BaseUtils.checkNull(map.get("VIN"));
						detail[3]=BaseUtils.checkNull(map.get("ENGINE_NO"));
						detail[4]=BaseUtils.checkNull(map.get("MODEL_NAME"));
						detail[5]=BaseUtils.checkNull(map.get("DELIVERER"));
						detail[6]=BaseUtils.checkNull(map.get("DELIVERER_PHONE"));
						detail[7]=BaseUtils.checkNull(map.get("TROUBLE_REASON"));
						detail[8]=BaseUtils.checkNull(map.get("TROUBLE_DESC"));
						detail[9]=BaseUtils.checkNull(map.get("IN_MILEAGE"));
						detail[10]=BaseUtils.checkNull(map.get("GUARANTEE_DATE"));
						detail[11]=BaseUtils.checkNull(map.get("CAMPAIGN_CODE"));
						detail[12]=BaseUtils.checkNull(map.get("RO_NO"));
						detail[13]=BaseUtils.checkNull(map.get("PACKAGE_NAME"));
						detail[14]=BaseUtils.checkNull(map.get("STATUS"));
						detail[15]=BaseUtils.checkNull(map.get("PART_CODE"));
						detail[16]=BaseUtils.checkNull(map.get("PART_NAME"));
						detail[17]=BaseUtils.checkNull(map.get("BALANCE_QUANTITY"));
						String  str=map.get("PART_USE_TYPE").toString();
						String st="";
						 if("1".equals(str)){
				              st+="更换";
				         }else if("0".equals(str)){
				              st+="维修";
				         }
						detail[18]=st;
						detail[19]=this.getTypeDesc(BaseUtils.checkNull(map.get("OLD_STATUS")));
						detail[20]=BaseUtils.checkNull(map.get("IS_KJ"));
						detail[21]=BaseUtils.checkNull(map.get("PRODUCER_CODE"));
						detail[22]=BaseUtils.checkNull(map.get("PRODUCER_NAME"));
						detail[23]=BaseUtils.checkNull(map.get("BALANCE_LABOUR_AMOUNT"));
						detail[24]=BaseUtils.checkNull(map.get("BALANCE_PART_AMOUNT"));
						detail[25]=BaseUtils.checkNull(map.get("BALANCE_NETITEM_AMOUNT"));
						detail[26]=BaseUtils.checkNull(map.get("COMPENSATION_MONEY"));
						detail[27]=BaseUtils.checkNull(map.get("ACCESSORIES_PRICE"));
						detail[28]=BaseUtils.checkNull(map.get("REPAIR_TOTAL"));
						detail[29]=BaseUtils.checkNull(map.get("BALANCE_AMOUNT"));
						detail[30]=BaseUtils.checkNull(map.get("DIFFERENT_MONEY"));
						detail[31]=BaseUtils.checkNull(map.get("IS_INVOICE"));
						detail[32]=BaseUtils.checkNull(map.get("BALANCE_STATUS"));
						detail[33]=BaseUtils.checkNull(map.get("IS_OUT"));
						params.add(detail);
					}
				}
				    String systemDateStr = BaseUtils.getSystemDateStr();
					BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "索赔数据跟踪导出"+systemDateStr+".xls", "导出数据",null);
				} catch (Exception e) {
					e.printStackTrace();
				}
		
		}

		@SuppressWarnings("unchecked")
		public PageResult<Map<String, Object>> authorizationRateQuery(RequestWrapper request, AclUserBean loginUser,Integer pageSize, Integer currPage) {
			StringBuffer sql= new StringBuffer();
			sql.append("select  tt.*,(tt.pass_sum - tt.not_one_pass_sum) as one_pass_sum, --一次通过数量\n" );
			sql.append("                        round((tt.pass_sum /\n" );
			sql.append("                              decode(tt.report_sum, 0, 1, tt.report_sum)),\n" );
			sql.append("                              2) * 100 || '%' as percent_pass, --通过率\n" );
			sql.append("                        round((tt.pass_sum - tt.not_one_pass_sum) /\n" );
			sql.append("                              decode(tt.pass_sum, 0, 1, tt.pass_sum),\n" );
			sql.append("                              2) * 100 || '%' as percent_one_pass --一次通过率\n" );
			sql.append("    from ( select ysq.dealer_id,\n" );
			sql.append("vw.root_org_name,tm.dealer_shortname,tm.dealer_code,\n" );
			sql.append("nvl(c.report_sum,0) report_sum,nvl(ccccc.Refusal_sum,0) Refusal_sum,nvl(cccc.rebut_sum,0) rebut_sum,\n" );
			sql.append("nvl(ccc.not_one_pass_sum,0) not_one_pass_sum,nvl(cc.pass_sum,0) pass_sum\n" );
			sql.append(" from (Tt_As_Wr_ysq ysq left join\n" );
			sql.append("  tm_dealer             tm\n" );
			sql.append("   on ysq.dealer_id=tm.dealer_id left join\n" );
			sql.append("    vw_org_dealer_service vw   on ysq.dealer_id=vw.dealer_id\n" );
			sql.append("    left join\n" );
			sql.append(" (select count(1) report_sum,t.dealer_id\n" );
			sql.append("        from Tt_As_Wr_ysq t\n" );
			sql.append("                         where  t.is_delete is null\n" );
			sql.append("                         and t.status >= 93461002\n" );
			sql.append("                           and t.id in\n" );
			sql.append("                               (select biz_id\n" );
			sql.append("                                  from (select d.biz_id,\n" );
			sql.append("                                               min(d.create_date) create_date\n" );
			sql.append("                                          from Tt_As_Com_Record d\n" );
			sql.append("                                         group by d.biz_id) tt\n" );
			sql.append("                                 where 1=1\n" );
			DaoFactory.getsql(sql, "tt.create_date", DaoFactory.getParam(request, "beginTime"), 3);
			DaoFactory.getsql(sql, "tt.create_date", DaoFactory.getParam(request, "endTime"), 4);
			sql.append("     )   group by t.dealer_id) c  --上报数量\n" );
			sql.append("     on ysq.dealer_id=c.dealer_id\n" );
			sql.append("     left join\n" );
			sql.append("     (select count(1) pass_sum,t.dealer_id from Tt_As_Wr_ysq t\n" );
			sql.append("                         where t.is_delete is null\n" );
			sql.append("                           and t.status in (93461003, 93461005)\n" );
			sql.append("                           and t.is_end = -1\n" );
			sql.append("                           and t.id in\n" );
			sql.append("                               (select biz_id\n" );
			sql.append("                                  from (select d.biz_id,min(d.create_date) create_date--上报时间\n" );
			sql.append("                                          from Tt_As_Com_Record d group by d.biz_id) tt\n" );
			sql.append("                                 where 1=1\n" );
			DaoFactory.getsql(sql, "tt.create_date", DaoFactory.getParam(request, "beginTime"), 3);
			DaoFactory.getsql(sql, "tt.create_date", DaoFactory.getParam(request, "endTime"), 4);
			sql.append("  )\n" );
			DaoFactory.getsql(sql, "t.create_date", DaoFactory.getParam(request, "beginTime"), 3);
			DaoFactory.getsql(sql, "t.create_date", DaoFactory.getParam(request, "endTime"), 4);
			sql.append("  group by t.dealer_id\n" );
			sql.append("   ) cc --通过数量\n" );
			sql.append("   on ysq.dealer_id=cc.dealer_id\n" );
			sql.append("   left join\n" );
			sql.append("   (select count(1) not_one_pass_sum,t.dealer_id\n" );
			sql.append("                          from Tt_As_Wr_ysq t\n" );
			sql.append("                         where t.is_delete is null\n" );
			sql.append("                           and t.status in (93461003, 93461005)\n" );
			sql.append("                           and t.is_end = -1\n" );
			sql.append("                           and t.id in\n" );
			sql.append("                               (select biz_id from  (select biz_id ,min(re.create_date) create_date from Tt_As_Com_Record  re\n" );
			sql.append("where re.biz_id in (select r.biz_id\n" );
			sql.append("              from Tt_As_Com_Record r\n" );
			sql.append("               where r.status in (93461004, 93461006)\n" );
			sql.append("           )  and 1=1\n" );
			sql.append(" group by re.biz_id) tt where 1=1\n" );
			sql.append("\n" );
			DaoFactory.getsql(sql, "tt.create_date", DaoFactory.getParam(request, "beginTime"), 3);
			DaoFactory.getsql(sql, "tt.create_date", DaoFactory.getParam(request, "endTime"), 4);
			sql.append("     )\n" );
			DaoFactory.getsql(sql, "t.create_date", DaoFactory.getParam(request, "beginTime"), 3);
			DaoFactory.getsql(sql, "t.create_date", DaoFactory.getParam(request, "endTime"), 4);
			sql.append("     group by t.dealer_id\n" );
			sql.append("     ) ccc--非一次性\n" );
			sql.append("     on ysq.dealer_id=ccc.dealer_id\n" );
			sql.append("     left join\n" );
			sql.append("      (select count(1) rebut_sum,t.dealer_id\n" );
			sql.append("                          from Tt_As_Wr_ysq t\n" );
			sql.append("                         where t.is_delete is null\n" );
			sql.append("                           and t.status in (93461004, 93461006)\n" );
			sql.append("                           and t.id in\n" );
			sql.append("                               (select biz_id\n" );
			sql.append("                                  from (select d.biz_id,\n" );
			sql.append("                                               min(d.create_date) create_date\n" );
			sql.append("                                          from Tt_As_Com_Record d\n" );
			sql.append("                                         group by d.biz_id) tt\n" );
			sql.append("                                 where 1=1\n" );
			DaoFactory.getsql(sql, "tt.create_date", DaoFactory.getParam(request, "beginTime"), 3);
			DaoFactory.getsql(sql, "tt.create_date", DaoFactory.getParam(request, "endTime"), 4);
			sql.append(" )\n" );
			sql.append("     group by t.dealer_id ) cccc--驳回数量\n" );
			sql.append("     on ysq.dealer_id=cccc.dealer_id\n" );
			sql.append("     left join\n" );
			sql.append("      (select count(1) Refusal_sum,t.dealer_id\n" );
			sql.append("                          from Tt_As_Wr_ysq t\n" );
			sql.append("                         where t.is_delete is null\n" );
			sql.append("                           and t.status in (93461010)\n" );
			sql.append("                           and t.id in\n" );
			sql.append("                               (select biz_id\n" );
			sql.append("                                  from (select d.biz_id,\n" );
			sql.append("                                               min(d.create_date) create_date\n" );
			sql.append("                                          from Tt_As_Com_Record d\n" );
			sql.append("                                         group by d.biz_id) tt\n" );
			sql.append("                                 where 1=1\n" );
			DaoFactory.getsql(sql, "tt.create_date", DaoFactory.getParam(request, "beginTime"), 3);
			DaoFactory.getsql(sql, "tt.create_date", DaoFactory.getParam(request, "endTime"), 4);
			sql.append("  )\n" );
			sql.append("  group by t.dealer_id  ) ccccc--拒赔数量\n" );
			sql.append("  on  ysq.dealer_id=ccccc.dealer_id\n" );
			sql.append("    ) group by ysq.dealer_id,c.report_sum,ccccc.Refusal_sum,\n" );
			sql.append("    cccc.rebut_sum,ccc.not_one_pass_sum,cc.pass_sum,vw.root_org_name,tm.dealer_shortname,tm.dealer_code\n" );
			sql.append("    )  tt");

			sql.append(" where 1 = 1");
			DaoFactory.getsql(sql, "tt.root_org_name",DaoFactory.getParam(request, "__large_org") , 2);
			DaoFactory.getsql(sql, "tt.dealer_shortname",DaoFactory.getParam(request, "dealer_shortname") , 2);
			DaoFactory.getsql(sql, "tt.dealer_code",DaoFactory.getParam(request, "dealerCode") , 6);
	        return this.pageQuery(sql.toString(), null, getFunName(), pageSize, currPage);
		}

		@SuppressWarnings("unchecked")
		public void ExportauthorizationRate(RequestWrapper request,ActionContext act, AclUserBean loginUser, Integer pageSizeMax,Integer currPage) {
			PageResult<Map<String, Object>> list = this.authorizationRateQuery(request, loginUser, pageSizeMax, currPage);
			   try {				
				    String[] head={"大区","服务商代码","服务商简称","索赔预授权申报数量","通过数量","一次通过数量","驳回数量","拒赔数量","预授权索赔通过率","一次通过率"};
					List<Map<String, Object>> records = list.getRecords();
					List params=new ArrayList();
					if(records!=null &&records.size()>0){

						for (Map<String, Object> map : records) {
							String[] detail=new String[10];
							detail[0]=BaseUtils.checkNull(map.get("ROOT_ORG_NAME"));
							detail[1]=BaseUtils.checkNull(map.get("DEALER_CODE"));
							detail[2]=BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
							detail[3]=BaseUtils.checkNull(map.get("REPORT_SUM"));
							detail[4]=BaseUtils.checkNull(map.get("PASS_SUM"));
							detail[5]=BaseUtils.checkNull(map.get("NOT_ONE_PASS_SUM"));
							detail[6]=BaseUtils.checkNull(map.get("REBUT_SUM"));
							detail[7]=BaseUtils.checkNull(map.get("REFUSAL_SUM"));
							detail[8]=BaseUtils.checkNull(map.get("PERCENT_PASS"));
							detail[9]=BaseUtils.checkNull(map.get("PERCENT_ONE_PASS"));
							params.add(detail);
						}
					}
					    String systemDateStr = BaseUtils.getSystemDateStr();
						BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "预授权通过率报表导出"+systemDateStr+".xls", "导出数据",null);
					} catch (Exception e) {
						e.printStackTrace();
					}
			
		}

		@SuppressWarnings("unchecked")
		public PageResult<Map<String, Object>> queryApplication(RequestWrapper request, AclUserBean loginUser,Integer pageSize, Integer currPage) {
			String dealerCode = DaoFactory.getParam(request,"DEALER_CODE");
			String dealerName = DaoFactory.getParam(request,"DEALER_NAME");
			String roNo = DaoFactory.getParam(request,"RO_NO");
			String lineNo = DaoFactory.getParam(request,"LINE_NO");
			String claimNo = DaoFactory.getParam(request,"CLAIM_NO");
			String claimType = DaoFactory.getParam(request,"CLAIM_TYPE");
			String vin = DaoFactory.getParam(request,"VIN");
			String roStartdate = DaoFactory.getParam(request,"RO_STARTDATE");
			String roEnddate = DaoFactory.getParam(request,"RO_ENDDATE");
			String approveDate = DaoFactory.getParam(request,"approve_date");// 审核时间
			String approveDate2 = DaoFactory.getParam(request,"approve_date2");
			String status = DaoFactory.getParam(request,"STATUS");
			String person = DaoFactory.getParam(request,"PERSON");
			String yieldly = DaoFactory.getParam(request,"YIELDLY");// 查询条件--产地
			String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());// 该用户拥有的产地权限
			String isImport = DaoFactory.getParam(request,"is_import");
			String foreAuthPerson = DaoFactory.getParam(request,"foreAuthPerson");
			String model = DaoFactory.getParam(request,"model");
			String partCode = DaoFactory.getParam(request,"partCode");
			String wrLabourCode = DaoFactory.getParam(request,"wrLabourCode");
			
			StringBuffer sb= new StringBuffer();
			sb.append("select t.*\n" );
			sb.append("  from (select b.dealer_code,\n" );
			sb.append("               b.dealer_shortname,\n" );
			sb.append("               a.ro_no,a.id,\n" );
			sb.append("               a.claim_no,a.report_date,\n" );
			sb.append("               a.claim_type,\n" );
			sb.append("               (select tt.area_name\n" );
			sb.append("               from tm_business_area tt\n" );
			sb.append("               where tt.area_id = a.yieldly) as yieldly,\n");
			sb.append("               a.submit_times,\n" );
			sb.append("               a.vin,\n" );
			sb.append("               tc1.code_desc claim_type_name,\n" );
			sb.append("               tc2.code_desc claim_status,\n" );
			sb.append("               a.sub_date,\n" );
			sb.append("               a.status,\n" );
			sb.append("               vm.model_code,\n" );
			sb.append("               vm.model_name,\n" );
			sb.append("               a.model_id,\n" );
			sb.append("               a.balance_amount,\n" );
			sb.append("               a.report_date as auditing_date,\n" );
			sb.append("       (select count(1) from Tt_As_Wr_App_Audit_Detail d where d.audit_result=10791006 and  d.claim_id=a.id) back_times,\n" );//退回次数
			
			sb.append("(select c.name from tc_user c where c.user_id =\n" );
			sb.append("              (select max(d.audit_by) from Tt_As_Wr_App_Audit_Detail d\n" );
			sb.append("              where d.claim_id=a.id and d.audit_date=(select max(d.audit_date)\n" );
			sb.append("              from Tt_As_Wr_App_Audit_Detail d where d.claim_id =a.id))) as auditing_man,");
			sb.append("               (select case\n" );
			sb.append("                         when max(h.id) is null then\n" );
			sb.append("                          '未扣件'\n" );
			sb.append("                         else\n" );
			sb.append("                          '扣件'\n" );
			sb.append("                       end\n" );
			sb.append("                  from tt_as_wr_partsitem h\n" );
			sb.append("                 where h.id = a.id\n" );
			sb.append("                   and h.balance_quantity < h.quantity\n" );
			sb.append("                   and h.is_return = 95361001) kou_jian\n" );
			sb.append("          from tt_as_wr_application_fixed a left join tc_code tc1 on a.claim_type=tc1.code_id left join tc_code tc2 on a.status=tc2.code_id , tm_dealer b,vw_material_group_mat vm, tm_vehicle tv\n" );
			sb.append("         where 1 = 1 and vm.MATERIAL_ID = tv.material_id and tv.vin=a.vin \n" );
			sb.append("           and a.status not in (10791001)\n" );
			sb.append("           and b.dealer_id = nvl(a.second_dealer_id, a.dealer_id)\n" );
			DaoFactory.getsql(sb, "A.YIELDLY", yieldly, 1);
			// 2015.09.14 增加历史的经销商名称查询
//			DaoFactory.getsql(sb, "b.dealer_shortname", dealerName, 2);
			DlrInfoMngDAO dimDAO = new DlrInfoMngDAO();
			if(BaseUtils.notNull(dealerName)){
				sb.append(" and (b.dealer_shortname like '%"+dealerName+"%'\n");
				String idSql = dimDAO.queryHisDealerIdsByNameSql(dealerName);
				if(idSql!=null&&idSql.length()>0){
					sb.append(" or b.dealer_id in ( ");
					sb.append(idSql);
					sb.append(" ) ");
				}
				sb.append(" ) ");
			}
			DaoFactory.getsql(sb, "a.create_date", MyConstant.onlineDate, 3);//加入时间新分单节点
			// 2015.09.14 增加历史的经销商code查询
//			DaoFactory.getsql(sb, "b.dealer_Code", dealerCode, 6);
			if(BaseUtils.notNull(dealerCode)){
				String[] split = dealerCode.split(",");
				StringBuffer sbTemp=new StringBuffer(); 
				int length = split.length;
				if(length>0){
					for (int i = 0; i < length; i++) {
						String str = split[i];
						if(i==length-1){
							sbTemp.append("'"+str+"'");
						}else{
							sbTemp.append("'"+str+"',");
						}
					}
					sb.append(" and (b.dealer_Code in ("+sbTemp.toString()+")\n");
					String idSql = dimDAO.queryHisDealerIdsByCodesSql(dealerCode);
					if(idSql!=null&&idSql.length()>0){
						sb.append(" or b.dealer_id in ( ");
						sb.append(idSql);
						sb.append(" ) ");
					}
					sb.append(" ) ");
				}
			}
			DaoFactory.getsql(sb, "A.LINE_NO", lineNo, 1);
			DaoFactory.getsql(sb, "A.CLAIM_TYPE", claimType, 6);
			DaoFactory.getsql(sb, "A.CREATE_DATE", roStartdate, 3);
			DaoFactory.getsql(sb, "A.CREATE_DATE", roEnddate, 4);
			DaoFactory.getsql(sb, "A.STATUS", status, 1);
			DaoFactory.getsql(sb, "A.claim_No", claimNo.toUpperCase(), 2);
			DaoFactory.getsql(sb, "A.RO_NO", roNo.toUpperCase(), 2);
			DaoFactory.getsql(sb, "vm.model_code", model, 1);
			if(Utility.testString(partCode)){
				sb.append("   AND A.ID in ( SELECT TAWP.ID FROM TT_AS_WR_PARTSITEM TAWP WHERE TAWP.PART_CODE LIKE '%"+partCode+"%' GROUP BY TAWP.ID )  \n");
			}
			if(Utility.testString(wrLabourCode)){
				sb.append("   AND A.ID in ( SELECT TAWL.ID FROM TT_AS_WR_LABOURITEM TAWL WHERE TAWL.WR_LABOURCODE LIKE '%"+wrLabourCode+"%' GROUP BY TAWL.ID )  \n");
			}
			// 车辆VIN码
			if (Utility.testString(vin)) {sb.append(GetVinUtil.getVins(vin.replaceAll("'","\''"), "A"));
			}
			DaoFactory.getsql(sb, "A.is_import", isImport, 1);
			sb.append(" order by a.id desc ) t where 1=1 \n");
			DaoFactory.getsql(sb, "t.auditing_man", foreAuthPerson, 2);
			DaoFactory.getsql(sb, "t.report_date", approveDate, 3);
			DaoFactory.getsql(sb, "t.report_date", approveDate2, 4);
			PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
			return list;
		}

		@SuppressWarnings("unchecked")
		public void ClaimsReportExport(ActionContext act,RequestWrapper request,AclUserBean loginUser, Integer pageSizeMax, Integer currPage) {
			PageResult<Map<String, Object>> list1 = this.queryApplication( request,  loginUser, pageSizeMax,  currPage);
			List<Map<String, Object>> records =list1.getRecords();
			 try {				
				    String[] head={"服务商代码","服务商简称","工单号","索赔单号","车型","索赔类型","结算地","修改次数","退回次数","VIN","申请日期","申请状态","是否扣件","索赔总金额","审核人","审核时间"};
					List params=new ArrayList();
					if(records!=null &&records.size()>0){
						for (Map<String, Object> map : records) {
							String[] detail=new String[16];
							detail[0]=BaseUtils.checkNull(map.get("DEALER_CODE"));
							detail[1]=BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
							detail[2]=BaseUtils.checkNull(map.get("RO_NO"));
							detail[3]=BaseUtils.checkNull(map.get("CLAIM_NO"));
							detail[4]=BaseUtils.checkNull(map.get("MODEL_NAME"));
							detail[5]=BaseUtils.checkNull(map.get("CLAIM_TYPE_NAME"));
							detail[6]=BaseUtils.checkNull(map.get("YIELDLY"));
							detail[7]=BaseUtils.checkNull(map.get("SUBMIT_TIMES"));
							detail[8]=BaseUtils.checkNull(map.get("BACK_TIMES"));
							detail[9]=BaseUtils.checkNull(map.get("VIN"));
							detail[10]=BaseUtils.checkNull(map.get("SUB_DATE"));
							detail[11]=BaseUtils.checkNull(map.get("CLAIM_STATUS"));
							detail[12]=BaseUtils.checkNull(map.get("KOU_JIAN"));
							detail[13]=BaseUtils.checkNull(map.get("BALANCE_AMOUNT"));
							detail[14]=BaseUtils.checkNull(map.get("AUDITING_MAN"));
							detail[15]=BaseUtils.checkNull(map.get("AUDITING_DATE"));
							params.add(detail);
						}
					}
					    String systemDateStr = BaseUtils.getSystemDateStr();
						BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "索赔单冻结报表导出"+systemDateStr+".xls", "导出数据",null);
					} catch (Exception e) {
						e.printStackTrace();
					}
			
		}
       /**
        * 索赔单冻结报表总金额
        * @param loginUser
        * @param request
        * @return
        */
		@SuppressWarnings("unchecked")
		public Map<String, Object> SumClaimAmount(AclUserBean loginUser,RequestWrapper request) {
			Map<String, Object> map=null;
			//pdi
			String type =DaoFactory.getParam(request, "type");
			if ("pdi".equals(type)) {
				StringBuffer sb= new StringBuffer();
				sb.append("select sum(a.balance_amount) as sumamount\n" );
				sb.append("  from tt_as_wr_application_fixed a,Tt_As_Wr_Netitem n\n" );
				sb.append(" where a.id=n.id and a.claim_type = 10661011\n");
				DaoFactory.getsql(sb, "a.create_date", MyConstant.onlineDate, 3);//加入时间新分单节点
				DaoFactory.getsql(sb, "a.claim_no", DaoFactory.getParam(request, "claim_no"), 2);
				DaoFactory.getsql(sb, "a.create_date", DaoFactory.getParam(request, "beginTime"), 3);
				DaoFactory.getsql(sb, "a.create_date", DaoFactory.getParam(request, "endTime"), 4);
				DaoFactory.getsql(sb, "a.status", DaoFactory.getParam(request, "status"), 1);
				DaoFactory.getsql(sb, "a.vin", DaoFactory.getParam(request, "vin"), 6);
				DaoFactory.getsql(sb, "a.dealer_id", loginUser.getDealerId(), 1);
				sb.append("   order by a.create_date desc \n" );
				map = this.pageQueryMap(sb.toString(), null, getFunName());
			}else if ("keep".equals(type)) {
				StringBuffer sb= new StringBuffer();
				sb.append("select sum(a.balance_amount) as sumamount\n" );
				sb.append("  from tt_as_wr_application_fixed a\n" );
				sb.append(" where  a.claim_type = 10661002\n");
				DaoFactory.getsql(sb, "a.create_date", MyConstant.onlineDate, 3);//加入时间新分单节点
				DaoFactory.getsql(sb, "a.claim_no", DaoFactory.getParam(request, "claim_no"), 2);
				DaoFactory.getsql(sb, "a.create_date", DaoFactory.getParam(request, "beginTime"), 3);
				DaoFactory.getsql(sb, "a.create_date", DaoFactory.getParam(request, "endTime"), 4);
				DaoFactory.getsql(sb, "a.status", DaoFactory.getParam(request, "status"), 1);
				DaoFactory.getsql(sb, "a.vin", DaoFactory.getParam(request, "vin"), 6);
				DaoFactory.getsql(sb, "a.dealer_id", loginUser.getDealerId(), 1);
				DaoFactory.getsql(sb, "a.ro_no",DaoFactory.getParam(request, "ro_no") , 6);
				sb.append("   order by a.create_date desc \n" );
				map= this.pageQueryMap(sb.toString(), null, getFunName());
			} else if ("normal".equals(type)) {
				StringBuffer sb= new StringBuffer();
				sb.append("select sum(a.balance_amount) as sumamount\n" );
				sb.append("  from tt_as_wr_application_fixed a\n" );
				sb.append(" where a.claim_type not in(10661011,10661002) ");
				DaoFactory.getsql(sb, "a.create_date", MyConstant.onlineDate, 3);//加入时间新分单节点
				DaoFactory.getsql(sb, "a.claim_no", DaoFactory.getParam(request, "claim_no"), 2);
				DaoFactory.getsql(sb, "a.create_date", DaoFactory.getParam(request, "beginTime"), 3);
				DaoFactory.getsql(sb, "a.create_date", DaoFactory.getParam(request, "endTime"), 4);
				DaoFactory.getsql(sb, "a.status", DaoFactory.getParam(request, "status"), 1);
				DaoFactory.getsql(sb, "a.vin", DaoFactory.getParam(request, "vin"), 6);
				DaoFactory.getsql(sb, "a.claim_type", DaoFactory.getParam(request, "claim_type"), 1);
				DaoFactory.getsql(sb, "a.dealer_id", loginUser.getDealerId(), 1);
				DaoFactory.getsql(sb, "a.ro_no",DaoFactory.getParam(request, "ro_no") , 6);
				sb.append("   order by a.create_date desc \n" );
				map= this.pageQueryMap(sb.toString(), null, getFunName());
			}else if ("audit".equals(type)) {
				String dealerCode = DaoFactory.getParam(request,"DEALER_CODE");
				String dealerName = DaoFactory.getParam(request,"DEALER_NAME");
				String roNo = DaoFactory.getParam(request,"RO_NO");
				String lineNo = DaoFactory.getParam(request,"LINE_NO");
				String claimNo = DaoFactory.getParam(request,"CLAIM_NO");
				String claimType = DaoFactory.getParam(request,"CLAIM_TYPE");
				String vin = DaoFactory.getParam(request,"VIN");
				String roStartdate = DaoFactory.getParam(request,"RO_STARTDATE");
				String roEnddate = DaoFactory.getParam(request,"RO_ENDDATE");
				String approveDate = DaoFactory.getParam(request,"approve_date");// 审核时间
				String approveDate2 = DaoFactory.getParam(request,"approve_date2");
				String status = DaoFactory.getParam(request,"STATUS");
				String person = DaoFactory.getParam(request,"PERSON");
				String yieldly = DaoFactory.getParam(request,"YIELDLY");// 查询条件--产地
				String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());// 该用户拥有的产地权限
				String isImport = DaoFactory.getParam(request,"is_import");
				String foreAuthPerson = DaoFactory.getParam(request,"foreAuthPerson");
				String model = DaoFactory.getParam(request,"model");
				String partCode = DaoFactory.getParam(request,"partCode");
				String wrLabourCode = DaoFactory.getParam(request,"wrLabourCode");
				
				StringBuffer sb= new StringBuffer();
				sb.append("select  sum(t.balance_amount) as sumamount\n" );
				sb.append("  from (select b.dealer_code,\n" );
				sb.append("               b.dealer_shortname,\n" );
				sb.append("               a.ro_no,a.id,\n" );
				sb.append("               a.claim_no,a.report_date,\n" );
				sb.append("               a.claim_type,\n" );
				sb.append("               (select tt.area_name\n" );
				sb.append("               from tm_business_area tt\n" );
				sb.append("               where tt.area_id = a.yieldly) as yieldly,\n");
				sb.append("               a.submit_times,\n" );
				sb.append("               a.vin,\n" );
				sb.append("               a.sub_date,\n" );
				sb.append("               a.status,\n" );
				sb.append("               vm.model_code,\n" );
				sb.append("               vm.model_name,\n" );
				sb.append("               a.model_id,\n" );
				sb.append("               a.balance_amount,\n" );
				sb.append("               a.report_date as auditing_date,\n" );
				sb.append("(select c.name from tc_user c where c.user_id =\n" );
				sb.append("              (select max(d.audit_by) from Tt_As_Wr_App_Audit_Detail d\n" );
				sb.append("              where d.claim_id=a.id and d.audit_date=(select max(d.audit_date)\n" );
				sb.append("              from Tt_As_Wr_App_Audit_Detail d where d.claim_id =a.id))) as auditing_man,");
				sb.append("               (select case\n" );
				sb.append("                         when max(h.id) is null then\n" );
				sb.append("                          '未扣件'\n" );
				sb.append("                         else\n" );
				sb.append("                          '扣件'\n" );
				sb.append("                       end\n" );
				sb.append("                  from tt_as_wr_partsitem h\n" );
				sb.append("                 where h.id = a.id\n" );
				sb.append("                   and h.balance_quantity < h.quantity\n" );
				sb.append("                   and h.is_return = 95361001) kou_jian\n" );
				sb.append("          from tt_as_wr_application_fixed a, tm_dealer b,vw_material_group_mat vm, tm_vehicle tv\n" );
				sb.append("         where 1 = 1 and vm.MATERIAL_ID = tv.material_id and tv.vin=a.vin \n" );
				sb.append("           and a.status not in (10791001)\n" );
				sb.append("           and b.dealer_id = nvl(a.second_dealer_id, a.dealer_id)\n" );
				DaoFactory.getsql(sb, "A.YIELDLY", yieldly, 1);
				DaoFactory.getsql(sb, "b.dealer_shortname", dealerName, 2);
				DaoFactory.getsql(sb, "a.create_date", MyConstant.onlineDate, 3);//加入时间新分单节点
				DaoFactory.getsql(sb, "b.dealer_Code", dealerCode, 6);
				DaoFactory.getsql(sb, "A.LINE_NO", lineNo, 1);
				DaoFactory.getsql(sb, "A.CLAIM_TYPE", claimType, 6);
				DaoFactory.getsql(sb, "A.CREATE_DATE", roStartdate, 3);
				DaoFactory.getsql(sb, "A.CREATE_DATE", roEnddate, 4);
				DaoFactory.getsql(sb, "A.STATUS", status, 1);
				DaoFactory.getsql(sb, "A.claim_No", claimNo.toUpperCase(), 2);
				DaoFactory.getsql(sb, "A.RO_NO", roNo.toUpperCase(), 2);
				DaoFactory.getsql(sb, "vm.model_code", model, 1);
				if(Utility.testString(partCode)){
					sb.append("   AND A.ID in ( SELECT TAWP.ID FROM TT_AS_WR_PARTSITEM TAWP WHERE TAWP.PART_CODE LIKE '%"+partCode+"%' GROUP BY TAWP.ID )  \n");
				}
				if(Utility.testString(wrLabourCode)){
					sb.append("   AND A.ID in ( SELECT TAWL.ID FROM TT_AS_WR_LABOURITEM TAWL WHERE TAWL.WR_LABOURCODE LIKE '%"+wrLabourCode+"%' GROUP BY TAWL.ID )  \n");
				}
				// 车辆VIN码
				if (Utility.testString(vin)) {sb.append(GetVinUtil.getVins(vin.replaceAll("'","\''"), "A"));
				}
				DaoFactory.getsql(sb, "A.is_import", isImport, 1);
				sb.append(" order by a.id desc ) t where 1=1 \n");
				DaoFactory.getsql(sb, "t.auditing_man", foreAuthPerson, 2);
				DaoFactory.getsql(sb, "t.report_date", approveDate, 3);
				DaoFactory.getsql(sb, "t.report_date", approveDate2, 4);
				map= this.pageQueryMap(sb.toString(), null, getFunName());
			}
			return map;
		}

		
}
