package com.infodms.dms.dao.report.serviceReport;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmPtPartBaseExtPO;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class ClaimReportDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(ClaimReportDao.class);
	public static ClaimReportDao dao = new ClaimReportDao();
	public static ClaimReportDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
public List<Map<String, Object>> getSmallOrgList(String id ){
	
	List<Map<String, Object>> list = null;
	StringBuffer sql = new StringBuffer();
	sql.append("SELECT TMO.ORG_ID, TMO.ORG_NAME FROM TM_ORG TMO\n");
	sql.append(" WHERE TMO.STATUS = "+Constant.STATUS_ENABLE+" AND TMO.ORG_TYPE =  "+Constant.ORG_TYPE_OEM+"\n");
	sql.append(" AND tmo.org_level=3\n");
	if(Utility.testString(id)){
		sql.append("and tmo.parent_org_id="+id+"\n"); 
	}
	sql.append(" ORDER  BY  TMO.ORG_ID"); 

	list=super.pageQuery(sql.toString(), null, getFunName());
	return list;
}
public List<Map<String, Object>> getBigOrgList( ){
	
	List<Map<String, Object>> list = null;
	StringBuffer sql = new StringBuffer();
	sql.append("SELECT TMO.ORG_ID ROOT_ORG_ID, TMO.ORG_NAME ROOT_ORG_NAME FROM TM_ORG TMO\n");
	sql.append(" WHERE TMO.STATUS = "+Constant.STATUS_ENABLE+" AND TMO.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");
	sql.append(" AND tmo.org_level=2\n");
	sql.append(" group by TMO.ORG_ID,\n");
	sql.append("       TMO.ORG_NAME\n");
	sql.append(" ORDER  BY  TMO.ORG_ID"); 

	list=super.pageQuery(sql.toString(), null, getFunName());
	return list;
}
public PageResult<Map<String,Object>> getDetailList(Map<String, String> map,int pageSize,int curPage ){
	
	List<Map<String, Object>> list = null;
	StringBuffer sql = new StringBuffer();
	sql.append("select  f.ro_no,f.fo_no,ds.org_name,d.dealer_shortname dealer_name,c.main_phone,c.ctm_name,gs.MODEL_NAME,\n");
	sql.append("v.vin,v.engine_no,to_char(v.product_date,'yyyy-mm-dd') product_time,to_char(v.purchased_date,'yyyy-mm-dd')purchased_time,\n");
	sql.append("f.in_mileage,o.repair_method,to_char(f.approval_date,'yyyy-mm-dd') apply_time,\n");
	sql.append("max(decode(fd.approval_level_code,'200',fd.audit_person)) dq_name,\n");
	sql.append("max(to_char(decode(fd.approval_level_code,'200',fd.audit_date) ,'yyyy-mm-dd')) dq_time,\n");
	sql.append("max(decode(fd.approval_level_code,'200',cc.code_desc)) dq_result,\n");
	sql.append("max(decode(fd.approval_level_code,'400',fd.audit_person)) zr_name,\n");
	sql.append("max(decode(fd.approval_level_code,'400',cc.code_desc)) zr_result,\n");
	sql.append("max(decode(fd.approval_level_code,'500',fd.audit_person)) cz_name,\n");
	sql.append("max(decode(fd.approval_level_code,'500',cc.code_desc)) cz_result,\n");
	sql.append("max(decode(fd.approval_level_code,'600',fd.audit_person)) fz_name,\n");
	sql.append("max(decode(fd.approval_level_code,'600',cc.code_desc)) fz_result,\n");
	sql.append("to_char(f.update_date,'yyyy-mm-dd') audit_time ,sum(decode( f.approval_type,"+Constant.REPAIR_TYPE_04+",mg.labour_price,nvl(l.labour_amount,0))) labour_amount,  sum(decode( f.approval_type,"+Constant.REPAIR_TYPE_04+",mg.part_price,nvl(p.part_cost_amount,0))) repair_part_amount,\n");
	sql.append("round(sum(decode( f.approval_type,"+Constant.REPAIR_TYPE_04+",mg.labour_price,nvl(l.labour_amount,0)))+sum(decode( f.approval_type,"+Constant.REPAIR_TYPE_04+",mg.part_price,nvl(p.part_cost_amount,0)))  ,2) all_mount ,\n");
	sql.append("p.part_name,c1.code_desc,m.mal_name\n");
	sql.append("from  Tt_As_Wr_Foreapproval f\n");
	sql.append("left join vw_org_dealer_service ds on ds.dealer_id = f.dealer_id and ds.dealer_type="+Constant.DEALER_TYPE_DWR+"\n");
	sql.append("left join tm_dealer d on d.dealer_id = f.dealer_id and d.dealer_id = ds.dealer_id\n");
	sql.append("left join tt_as_repair_order o on o.ro_no = f.ro_no\n");
	sql.append("left join tt_as_wr_foreauthdetail fd on fd.fid = f.id\n");
	sql.append("left join tc_code cc on fd.audit_result = cc.code_id\n");
	sql.append("left join tt_as_ro_repair_part p on p.ro_id = o.id and p.respons_nature="+Constant.RESPONS_NATURE_STATUS_01+"\n");
	sql.append("left join tt_as_ro_labour l on l.ro_id = o.id and p.labour = l.wr_labourcode\n");
	sql.append("left join tc_code c1 on c1.code_id=p.Part_Use_Type\n");
	sql.append("left join tt_as_wr_malfunction m on m.mal_id = l.mal_function,\n");
	sql.append("tm_vehicle v\n");
	sql.append("left join tt_dealer_actual_sales s on s.vehicle_id = v.vehicle_id  and s.is_return="+Constant.IF_TYPE_NO+"\n");
	sql.append("left join tt_customer c on s.ctm_id = c.ctm_id\n");
	sql.append("left join VW_MATERIAL_GROUP_service gs on gs.PACKAGE_ID = v.package_id\n");
	sql.append("left join TT_AS_WR_MODEL_ITEM mi on mi.model_id = v.package_id\n");
	sql.append("left join tt_as_wr_model_group mg on mg.wrgroup_id = mi.wrgroup_id and MG.WRGROUP_TYPE="+Constant.WR_MODEL_GROUP_TYPE_01+"\n"); 
	sql.append("where f.vin = v.vin and f.report_status = "+Constant.RO_FORE_02+"\n");
	if(Utility.testString(map.get("vin"))){
		sql.append(" and Upper(v.vin) ='"+map.get("vin").toUpperCase()+"'\n"); 
	}
	if(Utility.testString(map.get("model_name"))){
		sql.append(" and Upper(gs.MODEL_CODE) like '%"+map.get("model_name").toUpperCase()+"%'\n");
	}
	if(Utility.testString(map.get("dealer_name"))){
		sql.append("and d.dealer_shortname like '%"+map.get("dealer_name")+"%'\n"); 
	}
	if(Utility.testString(map.get("small_org"))){
		sql.append(" and ds.org_id ="+map.get("small_org")+"\n"); 
	}
	if(Utility.testString(map.get("part_name"))){
		sql.append(" and p.part_name like '%"+map.get("part_name")+"%'\n"); 
	}
	if(Utility.testString(map.get("engine_no"))){
		sql.append("	and Upper(v.engine_no) like '%"+map.get("engine_no").toUpperCase()+"%'\n"); 
	}
	if(Utility.testString(map.get("bDate"))){
		sql.append("and f.approval_date>=to_date('"+map.get("bDate")+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n"); 
	}
	if(Utility.testString(map.get("eDate"))){
		sql.append("and f.approval_date<=to_date('"+map.get("eDate")+"23:59:59','yyyy-mm-dd hh24:mi:ss')\n"); 
	}
	sql.append("group by  f.ro_no,f.fo_no,ds.org_name,d.dealer_shortname ,c.main_phone,c.ctm_name,gs.MODEL_NAME,\n");
	sql.append("v.vin,v.engine_no,to_char(v.product_date,'yyyy-mm-dd') ,to_char(v.purchased_date,'yyyy-mm-dd'),\n");
	sql.append("f.in_mileage,o.repair_method,to_char(f.approval_date,'yyyy-mm-dd') ,to_char(f.update_date,'yyyy-mm-dd'),\n");
	sql.append("o.labour_amount,o.repair_part_amount,p.part_name,c1.code_desc,m.mal_name\n");
	sql.append("order by f.fo_no,f.ro_no"); 
	return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage); 
	}
public static Object toExceVender(ResponseWrapper response,
		RequestWrapper request, String[] head, List<String[]> list,String name,String name2,String strSet)
		throws Exception {

	jxl.write.WritableWorkbook wwb = null;
	OutputStream out = null;
	try {
		response.setContentType("application/octet-stream");
	    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
		out = response.getOutputStream();
		wwb = Workbook.createWorkbook(out);
		jxl.write.WritableSheet ws = wwb.createSheet(name2, 0);
		WritableFont font = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD);
		WritableCellFormat wcf = new WritableCellFormat(font);
		wcf.setAlignment(Alignment.CENTRE);
		
		WritableFont font1 = new WritableFont(WritableFont.ARIAL,12,WritableFont.NO_BOLD);
		WritableCellFormat wcf1 = new WritableCellFormat(font1);
		wcf1.setAlignment(Alignment.CENTRE);
		if (head != null && head.length > 0) {
			for (int i = 0; i < head.length; i++) {
				ws.addCell(new Label(i, 0, head[i],wcf1));
			}
		}
		int pageSize=list.size()/30000;
		for (int z = 1; z < list.size() + 1; z++) {
			String[] str = list.get(z - 1);
			for (int i = 0; i < str.length; i++) {
				ws.addCell(new Label(i, z, str[i],wcf));
			}
		}
		if(!"".equalsIgnoreCase(strSet)){
			String[] ss = strSet.split(",");
			for(int i=0;i<ss.length;i++){
				ws.setColumnView(Integer.parseInt(ss[i]), 25);
			}
		}
		wwb.write();
		out.flush();
	} catch (Exception e) {
		e.printStackTrace();
		throw e;
	} finally {
		if (null != wwb) {
			wwb.close();
		}
		if (null != out) {
			out.close();
		}
	}
	return null;
}

public PageResult<Map<String,Object>> getClaimDetailList(Map<String, String> map,int pageSize,int curPage,int poseBusType ){
	
	StringBuffer sql = new StringBuffer();
	sql.append("select  1 flag, c.code_desc repair_type,  o.ro_no, a.claim_no,\n");
	sql.append(" case when a.SECOND_DEALER_ID is null  then  d.DEALER_CODE else d1.DEALER_CODE end dealer_code2,\n");
	sql.append(" case when a.SECOND_DEALER_ID is null  then  d.dealer_name else d1.dealer_name end dealer_name2,\n");
	sql.append(" case when a.SECOND_DEALER_ID is null  then  d.dealer_shortname else d1.dealer_shortname end dealer_shortname2,--如果二级站id不为空，则说明单据是二级站创建的,\n");
	sql.append(" case when a.SECOND_DEALER_ID is null  then  '' else d.dealer_code end dealer_code,\n");
	sql.append(" case when a.SECOND_DEALER_ID is null  then  '' else d.dealer_name end dealer_name,\n");
	sql.append(" case when a.SECOND_DEALER_ID is null  then  '' else d.dealer_shortname end dealer_shortname,-- 如果二级站不为空,显示出一级站\n");
	sql.append(" case when a.SECOND_DEALER_ID is null  then  c1.code_desc else c3.code_desc end image_level,\n");
	sql.append(" case when a.SECOND_DEALER_ID is null  then  c2.code_desc else c4.code_desc end image_level2,--如果二级站不为空,则显示二级站现象级别，否则显示一级站的\n");
	
	sql.append("       ds.root_org_name,  ds.org_name, gs.SERIES_NAME, gs.MODEL_CODE,\n");
	sql.append("       gs.PACKAGE_NAME, g.wrgroup_name,   v.license_no, v.vin,v.engine_no,\n");
	sql.append("       to_char(v.product_date,'yyyy-mm-dd') product_time,\n");
	sql.append("       to_char(v.purchased_date,'yyyy-mm-dd') purchased_time,\n");
	sql.append("       to_char(o.ro_create_date,'yyyy-mm-dd') ro_start_time,\n");
	sql.append("       to_char(o.for_balance_time,'yyyy-mm-dd') balance_time,\n");
	sql.append("       to_char(a.report_date,'yyyy-mm-dd')  report_time,\n");
	sql.append("       o.in_mileage,m.mal_name,c6.code_desc part_use_type ,wp.remark,wp.down_part_code,wp.down_part_name,pd.part_code part_box,\n");
	sql.append("       decode(wp.responsibility_type,"+Constant.RESPONS_NATURE_STATUS_01+",'是',"+Constant.RESPONS_NATURE_STATUS_02+",'否') is_main_code,wp.part_code,wp.part_name,\n");
	sql.append("       vm.color_name,nvl(wp.price,0) price,nvl(wp.quantity,0)quantity ,nvl(wp.amount,0)amount,\n");
	sql.append("       decode(wp.responsibility_type,"+Constant.RESPONS_NATURE_STATUS_01+",l.labour_amount,0) labour_amount,\n");
	sql.append("       decode(a.claim_type,"+Constant.CLA_TYPE_09+",a.netitem_amount,0) out_acount,\n");
	sql.append("       wp.down_product_code,wp.down_product_name, ct.ctm_name,ct.address,ct.main_phone,\n");
	sql.append("       decode(ap.pro_code,"+Constant.SERVICEACTIVITY_CAR_cms_06+",round(ap.amount/100,2),0) labour_down,\n");
	sql.append("       decode(ap.pro_code,"+Constant.SERVICEACTIVITY_CAR_cms_07+",round(ap.amount/100,2),0) part_down, c5.code_desc balance_yiedily,\n");
	sql.append("       nvl(wn.give_amount,0) give_amount,\n");
	sql.append("       l.wr_labourcode,nvl(L.LABOUR_PRICE,0) LABOUR_PRICE,AA.ACTIVITY_CODE,\n");
	sql.append("	  decode(a.claim_type,"+Constant.CLA_TYPE_02+",g.free,0)by_amount,\n");
	sql.append("      decode(a.claim_type,"+Constant.CLA_TYPE_02+",g.part_price,0)by_part_amount,\n");
	sql.append("      decode(a.claim_type,"+Constant.CLA_TYPE_02+",g.labour_price,0)by_labour_amount,0 as DECLARE_SUM1"); 

	sql.append("  from tt_as_wr_application a\n");
	sql.append("  left join tt_as_repair_order o on o.ro_no = a.ro_no and o.order_valuable_type ="+Constant.RO_PRO_STATUS_01+"\n");
	sql.append("  left join tc_code c on c.code_id = a.claim_type\n");
	sql.append("  left join tm_dealer d on d.dealer_id = a.dealer_id\n");
	sql.append("  left join tm_dealer d1 on d1.dealer_id = a.second_dealer_id\n");
	sql.append("  left join tc_code c1 on c1.code_id = d.image_level\n");
	sql.append("  left join tc_code c2 on c2.code_id = d.image_level2\n");
	sql.append("  left join tc_code c3 on c3.code_id = d1.image_level\n");
	sql.append("  left join tc_code c4 on c4.code_id = d1.image_level2\n");
	sql.append("  left join tc_code c5 on c5.code_id = a.balance_yieldly\n");
	sql.append("  left join vw_org_dealer_service ds on ds.dealer_id = d.dealer_id\n");
	sql.append("                                    and ds.dealer_type ="+Constant.DEALER_TYPE_DWR+"\n");
	sql.append("  left join tm_vehicle v on v.vin = a.vin\n");
	sql.append("  left join tt_dealer_actual_sales s on s.vehicle_id = v.vehicle_id  and s.is_return="+Constant.IF_TYPE_NO+"\n");
	sql.append("  left join tt_customer ct on s.ctm_id = ct.ctm_id\n");
	sql.append("  left join VW_MATERIAL_GROUP_service gs on gs.PACKAGE_ID = v.package_id\n");
	sql.append("  left join TT_AS_WR_MODEL_ITEM i on i.model_id = gs.MODEL_ID and i.wrgroup_id !=-1 \n");
	sql.append("  left join TT_AS_WR_MODEL_GROUP g on g.wrgroup_id = i.wrgroup_id and g.wrgroup_type="+Constant.WR_MODEL_GROUP_TYPE_01+"\n");
	sql.append("  left join  tm_vhcl_material vm on vm.material_id = v.material_id\n");
	sql.append("  left join tt_as_wr_partsitem wp  on a.id = wp.id\n");
	sql.append("  left join tt_as_wr_labouritem l on l.id = a.id and l.wr_labourcode =wp.wr_labourcode\n");
	sql.append("  left join tt_as_ro_repair_part pp on pp.ro_id = o.id  and pp.part_no=wp.down_part_code and pp.pay_type="+Constant.PAY_TYPE_02+"\n");
	sql.append("  left join tt_as_wr_malfunction m on m.mal_id = l.trouble_code\n");
	sql.append("  left join tc_code c6 on c6.code_id = pp.part_use_type\n");
	sql.append("  left join (select n.id, sum(decode(n.item_code,'"+Constant.SERVICEACTIVITY_CAR_cms_01+"',n.amount,'"+Constant.SERVICEACTIVITY_CAR_cms_04+"',n.amount,'"+Constant.SERVICEACTIVITY_CAR_cms_05+"',n.amount,0)) give_amount from tt_as_wr_netitem n group by n.id) wn on wn.id = a.id\n");
	sql.append("  left join Tt_Part_Define pd on pd.part_oldcode = wp.down_part_code and pd.part_oldcode = pp.part_no\n");
	sql.append("  left join tt_as_activity aa on aa.activity_code = a.campaign_code\n");
	sql.append("  left join Tt_As_Activity_Project ap on ap.activity_id = aa.activity_id and ap.paid="+Constant.PAY_TYPE_02+"\n");
	sql.append("where a.status!="+Constant.CLAIM_APPLY_ORD_TYPE_01+" and ds.org_id is not null \n");
	// 艾春 10.08 添加职位控制
    if(poseBusType == Constant.POSE_BUS_TYPE_WRD){
    	sql.append(" and a.balance_yieldly ="+Constant.PART_IS_CHANGHE_02+"\n");
    }
	if(Utility.testString(map.get("vin"))){
		sql.append(" and Upper(v.vin) ='"+map.get("vin").toUpperCase()+"'\n"); 
	}
	if(Utility.testString(map.get("model_name"))){
		sql.append(" and Upper(gs.MODEL_CODE) like '%"+map.get("model_name").toUpperCase()+"%'\n");
	}
	if(Utility.testString(map.get("dealer_name"))){
		sql.append("and d.dealer_name like '%"+map.get("dealer_name")+"%'\n"); 
	}
	if(Utility.testString(map.get("big_org"))){
		sql.append(" and ds.root_org_id ="+map.get("big_org")+"\n"); 
	}
	if(Utility.testString(map.get("small_org"))){
		sql.append(" and ds.org_id ="+map.get("small_org")+"\n"); 
	}
	if(Utility.testString(map.get("model_group"))){
		sql.append(" and g.wrgroup_name like '%"+map.get("model_group")+"%'\n"); 
	}
	if(Utility.testString(map.get("engine_no"))){
		sql.append("	and Upper(v.engine_no) like '%"+map.get("engine_no").toUpperCase()+"%'\n"); 
	}
	if(Utility.testString(map.get("supply_code"))){
		String[] str = map.get("supply_code").split(",");
		String ss = "";
		for(int i=0;i<str.length;i++){
			ss = ss+"'"+str[i]+"',";
		}
		if(!"".equalsIgnoreCase(ss)){
			ss = ss.substring(0,ss.length()-1);
		}
		
		sql.append(" and Upper(wp.down_product_code) in("+ss+")\n"); 
	}
	if(Utility.testString(map.get("part_code"))){
		String[] str = map.get("part_code").split(",");
		String ss = "";
		for(int i=0;i<str.length;i++){
			ss = ss+"'"+str[i]+"',";
		}
		if(!"".equalsIgnoreCase(ss)){
			ss = ss.substring(0,ss.length()-1);
		}
		sql.append("	and wp.down_part_code in("+ss+")\n"); 
	}
	
	if(Utility.testString(map.get("repairType"))){
		sql.append("and a.claim_type = "+map.get("repairType")+"\n"); 
	}
	if(Utility.testString(map.get("YIEDILY"))){
		sql.append(" and a.balance_yieldly ="+map.get("YIEDILY")+"\n"); 
	}
	if(Utility.testString(map.get("bDate"))){
		sql.append("and o.ro_create_date >= to_date('"+map.get("bDate")+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n"); 
	}
	if(Utility.testString(map.get("eDate"))){
		sql.append("and o.ro_create_date<=to_date('"+map.get("eDate")+" 23:59:59','yyyy-mm-dd hh24:mi:ss')\n"); 
	}
	if(Utility.testString(map.get("beDate"))){
		sql.append("and v.product_date>=to_date('"+map.get("beDate")+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n"); 
	}
	if(Utility.testString(map.get("enDate"))){
		sql.append("and v.product_date<=to_date('"+map.get("enDate")+" 23:59:59','yyyy-mm-dd hh24:mi:ss')\n"); 
	}
	
	if(Utility.testString(map.get("report_date"))){
		sql.append("and a.report_date >= to_date('"+map.get("report_date")+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n"); 
	}
	if(Utility.testString(map.get("report_date2"))){
		sql.append("and a.report_date<=to_date('"+map.get("report_date2")+" 23:59:59','yyyy-mm-dd hh24:mi:ss')\n"); 
	}
	if(Utility.testString(map.get("balance_date"))){
		sql.append("and a.fi_date>=to_date('"+map.get("balance_date")+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n"); 
	}
	if(Utility.testString(map.get("balance_date2"))){
		sql.append("and a.fi_date<=to_date('"+map.get("balance_date2")+" 23:59:59','yyyy-mm-dd hh24:mi:ss')\n"); 
	}
	if(Utility.testString(map.get("banlanceStatus"))){
		sql.append("and a.status = "+map.get("banlanceStatus")+"\n"); 
	}
	
	sql.append("union all\n");
	sql.append("select 2 flag,cc.code_desc repair_type,'' as ro_no,b.fee_no claim_no,\n");
	sql.append(" d.DEALER_CODE  dealer_code2,\n");
	sql.append(" d.dealer_name dealer_name2,\n");
	sql.append(" d.dealer_shortname  dealer_shortname2,--如果二级站id不为空，则说明单据是二级站创建的,\n");
	sql.append(" case when d.dealer_level ="+Constant.DEALER_LEVEL_01+"   then  '' else dd.dealer_code end dealer_code,\n");
	sql.append(" case when d.dealer_level ="+Constant.DEALER_LEVEL_01+"   then  '' else dd.dealer_name end dealer_name,\n");
	sql.append(" case when d.dealer_level ="+Constant.DEALER_LEVEL_01+"   then  '' else dd.dealer_shortname end dealer_shortname,-- 如果二级站不为空,显示出一级站\n");
	sql.append("'' image_level,\n");
	sql.append("'' image_level2,--如果二级站不为空,则显示二级站现象级别，否则显示一级站的\n");
	sql.append("  ds.root_org_name,  ds.org_name, gs.SERIES_NAME, gs.MODEL_CODE,\n");
	sql.append("       gs.PACKAGE_NAME, g.wrgroup_name,   v.license_no, v.vin,v.engine_no,\n");
	sql.append("       to_char(v.product_date,'yyyy-mm-dd') product_time,\n");
	sql.append("       to_char(v.purchased_date,'yyyy-mm-dd') purchased_time,\n");
	sql.append("       '' ro_start_time,\n");
	sql.append("      '' balance_time,\n");
	sql.append("       to_char(b.make_date,'yyyy-mm-dd')  report_time,\n");
	sql.append("       0 in_mileage,'' mal_name,'' part_use_type ,'' remark,b.part_code down_part_code,b.part_name down_part_name,'' part_box,\n");
	sql.append("       '否'  is_main_code,b.part_code,b.part_name,\n");
	sql.append("       vm.color_name,0 price,0 quantity , 0 amount,\n");
	sql.append("       0labour_amount,\n");
	sql.append("       0 out_acount,\n");
	sql.append("       b.supplier_code down_product_code,b.supplier_name down_product_name, ct.ctm_name,ct.address,ct.main_phone,\n");
	sql.append("       0 labour_down,\n");
	sql.append("       0 part_down, '昌河' balance_yiedily,\n");
	sql.append("       0 give_amount,'' wr_labourcode,\n");
	sql.append("       0 LABOUR_PRICE,'' ACTIVITY_CODE,\n");
	sql.append("\t     0 by_amount,\n");
	sql.append("       0 by_part_amount,\n");
	sql.append("       0 by_labour_amount,b.DECLARE_SUM1\n");
	sql.append("from TT_AS_WR_SPEFEE B\n");
	sql.append("left join tm_dealer d on b.dealer_id = d.dealer_id\n");
	sql.append("left join  tc_code cc on b.fee_type = cc.code_id\n");
	sql.append("left join tm_dealer dd on d.parent_dealer_d = dd.dealer_id\n");
	sql.append("  left join vw_org_dealer_service ds on ds.dealer_id = b.dealer_id\n");
	sql.append("                                    and ds.dealer_type ="+Constant.MSG_TYPE_2+"\n");
	sql.append("  left join tm_vehicle v on v.vin = b.vin\n");
	sql.append("  left join tt_dealer_actual_sales s on s.vehicle_id = v.vehicle_id  and s.is_return="+Constant.IF_TYPE_NO+"\n");
	sql.append("  left join tt_customer ct on s.ctm_id = ct.ctm_id\n");
	sql.append("  left join VW_MATERIAL_GROUP_service gs on gs.PACKAGE_ID = v.package_id\n");
	sql.append("  left join TT_AS_WR_MODEL_ITEM i on i.model_id = gs.MODEL_ID and i.wrgroup_id !=-1\n");
	sql.append("  left join TT_AS_WR_MODEL_GROUP g on g.wrgroup_id = i.wrgroup_id and g.wrgroup_type="+Constant.WR_MODEL_GROUP_TYPE_01+"\n");
	sql.append("  left join  tm_vhcl_material vm on vm.material_id = v.material_id\n");
	sql.append("where  1=1 and b.status = b.o_status\n");
	//获取页面的查询条件,除了 大区,小区,服务站名称,上报时间外,均不能查询数据。
	  if(poseBusType == Constant.POSE_BUS_TYPE_WRD||Utility.testString(map.get("part_code"))||Utility.testString(map.get("supply_code"))||
			  Utility.testString(map.get("engine_no"))||Utility.testString(map.get("vin"))||Utility.testString(map.get("model_name"))||
			  Utility.testString(map.get("model_group"))||Utility.testString(map.get("bDate"))||Utility.testString(map.get("eDate"))||
			  Utility.testString(map.get("beDate"))||Utility.testString(map.get("enDate"))||Utility.testString(map.get("balance_date"))||
			  Utility.testString(map.get("balance_date2"))||Utility.testString(map.get("banlanceStatus"))||Utility.testString(map.get("YIEDILY"))){
	    	sql.append(" and 1=2 \n");
	    }
		if(Utility.testString(map.get("dealer_name"))){
			sql.append("AND DECODE(d.dealer_level, "+Constant.DEALER_LEVEL_01+", dd.dealer_id, d.dealer_id) IN\n");
			sql.append("     (SELECT d1.DEALER_ID\n");
			sql.append("        FROM TM_DEALER D1\n");
			sql.append("       START WITH d1.DEALER_name like '%"+map.get("dealer_name")+"&'\n");
			sql.append("      CONNECT BY PRIOR d1.DEALER_ID = d1.PARENT_DEALER_D)"); 

		}
		if(Utility.testString(map.get("big_org"))){
			sql.append(" and ds.root_org_id ="+map.get("big_org")+"\n"); 
		}
		if(Utility.testString(map.get("small_org"))){
			sql.append(" and ds.org_id ="+map.get("small_org")+"\n"); 
		}
	
		if(Utility.testString(map.get("report_date"))){
			sql.append("and b.make_date >= to_date('"+map.get("report_date")+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n"); 
		}
		if(Utility.testString(map.get("report_date2"))){
			sql.append("and b.make_date<=to_date('"+map.get("report_date2")+" 23:59:59','yyyy-mm-dd hh24:mi:ss')\n"); 
		}
		
		if(Utility.testString(map.get("feeType"))){
			sql.append(" and b.fee_type ="+map.get("feeType")+"\n"); 
		}
     sql.append("       order by flag, claim_no,wr_labourcode"); 
	return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage); 
	}
//传入小区list 拼接为下拉框
public  String getStr(List<Map<String, Object>> list){
	String str = "<select class=\"short_sel\" id=\"small_org\" name=\"small_org\" >";
	str+=" <option value=\"\">--请选择--</option>";
	if(list!=null&&list.size()>0){
		for(int i=0;i<list.size();i++){
			str+=" <option value=\""+list.get(i).get("ORG_ID")+"\">"+list.get(i).get("ORG_NAME")+"</option>";
		}
	}
	return str;
}
/**
 * 服务活动小项明细报表数据
 * @param map
 * @param pageSize
 * @param curPage
 * @return
 */
public PageResult<Map<String,Object>> getActivityDetailList(Map<String, String> map,int pageSize,int curPage ){
	
	List<Map<String, Object>> list = null;
	StringBuffer sql = new StringBuffer();
	sql.append("SELECT m.DEALER_SHORTNAME, /*服务站简称*/\n");
	sql.append("       m.subject_name, /*主题名称*/\n");
	sql.append("       m.sdate, /*活动开始时间*/\n");
	sql.append("       m.edate, /*活动结束时间*/\n");
	sql.append("       m.owner_name, /*车主*/\n");
	sql.append("       m.owner_phone, /*车主电话*/\n");
	sql.append("       m.pro_name, /*活动项目*/\n");
	sql.append("       SUM(m.total) total, /*实际数量*/\n");
	sql.append("       SUM(m.amount) amount, /*实际金额(元)*/\n");
	// 艾春 13.11.13 修改 去掉小项小计金额
	sql.append("       m.remark/*备注*/\n");
//	sql.append("       decode(grouping_id(m.REMARK), 1, '合计金额(元)', decode(grouping_id(m.pro_name), 1, m.REMARK||'小计金额(元)', m.REMARK)) remark/*备注*/\n");
	sql.append(" FROM (\n");
	sql.append("    SELECT AIC.CODE_DESC pro_name, /*活动项目*/\n");
	sql.append("           1 TOTAL, /*实际数量*/\n");
	sql.append("           DECODE(RI.ADD_ITEM_CODE,'"+Constant.SERVICEACTIVITY_CAR_cms_05+"',NVL(RI.ADD_ITEM_AMOUNT,0),'"+Constant.SERVICEACTIVITY_CAR_cms_02+"',NVL(RI.DISCOUNT,0) - NVL(RI.ADD_ITEM_AMOUNT,0), '"+Constant.SERVICEACTIVITY_CAR_cms_07+"', NVL(P.PART_AMOUNT,0), '"+Constant.SERVICEACTIVITY_CAR_cms_06+"', NVL(L.LABOUR_AMOUNT,0), 0) AMOUNT, /*实际金额(元)*/\n");
	sql.append("           DECODE(RI.PAY_TYPE, "+Constant.PAY_TYPE_02+", '昌河汽车承担', DS.DEALER_SHORTNAME||'承担') REMARK,/*备注*/\n");
	sql.append("           s.subject_name,\n");
	sql.append("           to_char(s.fact_start_date,'yyyy-mm-dd') sdate,\n");
	sql.append("           to_char(s.fact_end_date,'yyyy-mm-dd') edate,\n");
	sql.append("           ro.owner_name,\n");
	sql.append("           c.main_phone owner_phone,\n");
	sql.append("           DS.DEALER_SHORTNAME/*服务站简称*/\n");
	sql.append("      FROM TT_AS_REPAIR_ORDER RO\n");
	sql.append("      JOIN TM_DEALER DS ON NVL(RO.SECOND_DEALER_ID, RO.DEALER_ID) = DS.DEALER_ID\n");
	sql.append("      JOIN TT_AS_ACTIVITY A ON RO.CAM_CODE = A.ACTIVITY_CODE\n");
	sql.append("      JOIN TT_AS_ACTIVITY_SUBJECT S ON A.SUBJECT_ID = S.SUBJECT_ID\n");
	sql.append("      LEFT JOIN tt_crm_customer c ON ro.vin = c.vin\n");
	sql.append("      LEFT JOIN (SELECT RO_ID, SUM(RP.PART_QUANTITY*NVL(RP.PART_COST_PRICE,0)-NVL(RP.PART_COST_AMOUNT,0)) PART_AMOUNT FROM TT_AS_RO_REPAIR_PART RP GROUP BY RP.RO_ID) P  ON RO.ID = P.RO_ID\n");
	sql.append("      LEFT JOIN (SELECT RL.RO_ID, SUM(NVL(RL.LABOUR_PRICE,0)*NVL(RL.STD_LABOUR_HOUR,0) - NVL(RL.LABOUR_AMOUNT,0)) LABOUR_AMOUNT FROM TT_AS_RO_LABOUR RL GROUP BY RL.RO_ID) L ON RO.ID = L.RO_ID\n");
	sql.append("      LEFT JOIN TT_AS_RO_ADD_ITEM RI ON RO.ID = RI.RO_ID\n");
	sql.append("      LEFT JOIN TC_CODE AIC ON AIC.CODE_ID = RI.ADD_ITEM_CODE AND AIC.TYPE = 3537\n");
	sql.append("      WHERE RO.REPAIR_TYPE_CODE = "+Constant.REPAIR_TYPE_05+"\n");
	sql.append("        AND S.ACTIVITY_TYPE IN ("+Constant.SERVICEACTIVITY_TYPE_02+", "+Constant.SERVICEACTIVITY_TYPE_03+")\n");
	sql.append("        AND RI.ADD_ITEM_CODE IN ('"+Constant.SERVICEACTIVITY_CAR_cms_02+"', '"+Constant.SERVICEACTIVITY_CAR_cms_05+"', '"+Constant.SERVICEACTIVITY_CAR_cms_06+"', '"+Constant.SERVICEACTIVITY_CAR_cms_07+"')\n");
	sql.append("        AND RO.CREATE_DATE >= TO_DATE('2013-08-26','YYYY-MM-DD')\n");
	sql.append("        AND RO.RO_STATUS = "+Constant.RO_STATUS_02+"\n");
	sql.append("        AND s.subject_id ="+map.get("avtivityCode")+"\n");
	sql.append("    UNION ALL\n");
	sql.append("    SELECT AR.PROJECT_NAME,\n");
	sql.append("           COUNT(DISTINCT RO.RO_NO) TOTAL,\n");
	sql.append("           SUM(NVL(AR.PROJECT_AMOUNT,0)) AMOUNT,\n");
	sql.append("           DECODE(P.PAID, "+Constant.PAY_TYPE_02+", '昌河汽车承担', DS.DEALER_SHORTNAME||'承担') REMARK,\n");
	sql.append("           s.subject_name, /*主题名称*/\n");
	sql.append("           to_char(s.fact_start_date,'yyyy-mm-dd') sdate, /*活动开始时间*/\n");
	sql.append("           to_char(s.fact_end_date,'yyyy-mm-dd') edate, /*活动结束时间*/\n");
	sql.append("           ro.owner_name,/*车主*/\n");
	sql.append("           c.main_phone,/*车主电话*/\n");
	sql.append("           DS.DEALER_SHORTNAME/*服务站简称*/\n");
	sql.append("    FROM TT_AS_REPAIR_ORDER RO\n");
	sql.append("      JOIN TM_DEALER DS ON NVL(RO.SECOND_DEALER_ID, RO.DEALER_ID) = DS.DEALER_ID\n");
	sql.append("      JOIN TT_AS_ACTIVITY A ON RO.CAM_CODE = A.ACTIVITY_CODE\n");
	sql.append("      JOIN TT_AS_ACTIVITY_SUBJECT S ON A.SUBJECT_ID = S.SUBJECT_ID\n");
	sql.append("      JOIN TT_AS_ACTIVITY_PROJECT P ON A.ACTIVITY_ID = P.ACTIVITY_ID\n");
	sql.append("      JOIN TT_AS_ACTIVITY_RELATION AR ON AR.ACTIVITY_ID = P.ACTIVITY_ID AND AR.LARGESS_TYPE = P.PRO_CODE\n");
	sql.append("      LEFT JOIN tt_crm_customer c ON ro.vin = c.vin\n");
	sql.append("      WHERE RO.REPAIR_TYPE_CODE = "+Constant.REPAIR_TYPE_05+"\n");
	sql.append("        AND S.ACTIVITY_TYPE IN ("+Constant.SERVICEACTIVITY_TYPE_02+", "+Constant.SERVICEACTIVITY_TYPE_03+")\n");
	sql.append("        AND AR.LARGESS_TYPE IN ("+Constant.SERVICEACTIVITY_CAR_cms_01+", "+Constant.SERVICEACTIVITY_CAR_cms_04+")\n");
	sql.append("        AND RO.CREATE_DATE >= TO_DATE('2013-08-26','YYYY-MM-DD')\n");
	sql.append("        AND RO.RO_STATUS = "+Constant.RO_STATUS_02+"\n");
	sql.append("        AND s.subject_id ="+map.get("avtivityCode")+"\n");
	sql.append("     GROUP BY DS.DEALER_SHORTNAME, DECODE(P.PAID, "+Constant.PAY_TYPE_02+", '昌河汽车承担', DS.DEALER_SHORTNAME||'承担'), s.subject_name, to_char(s.fact_start_date,'yyyy-mm-dd'), to_char(s.fact_end_date,'yyyy-mm-dd'), ro.owner_name, c.main_phone, AR.PROJECT_NAME\n");
	sql.append("     ) m\n");
	// 艾春 13.11.13 修改去掉小项小计
	sql.append(" GROUP BY m.DEALER_SHORTNAME, m.REMARK, m.subject_name, m.sdate, m.edate, m.owner_name, m.owner_phone, m.pro_name \n"); 
	sql.append(" order BY m.DEALER_SHORTNAME, m.REMARK, m.pro_name \n");
//	sql.append(" GROUP BY ROLLUP((m.DEALER_SHORTNAME, m.REMARK, m.subject_name, m.sdate, m.edate, m.owner_name, m.owner_phone), m.pro_name)"); 

	return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage); 
	}
public PageResult<Map<String,Object>> queryPartCode(AclUserBean user, Map<String, String> map,int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select b.part_code, b.part_name from tm_pt_part_base b \n");
		sql.append(" where 1=1 \n");
		if(user.getPoseType()==Constant.POSE_BUS_TYPE_WRD){
			sql.append(" and b.part_is_changhe="+Constant.PART_IS_CHANGHE_02+"\n");
		}else{
			sql.append(" and b.part_is_changhe="+Constant.PART_IS_CHANGHE_01+"\n");
		}
		if (Utility.testString(map.get("partCode"))) {
		      sql.append(" AND B.PART_CODE LIKE '%" + map.get("partCode").toUpperCase()
		          + "%' ");
		    }
	if (Utility.testString(map.get("partName"))) {
		      sql.append(" AND B.PART_NAME LIKE '%" + map.get("partName")
		          + "%' ");
		    }
		return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage); 
}
public PageResult<Map<String,Object>> allSuppQuery(String suppCode, String suppName,int pageSize, int curPage) {
	StringBuffer sql = new StringBuffer();
	sql.append("select d.maker_code,d.maker_name from tt_part_maker_define d \n ");
	sql.append("WHERE 1=1 \n");
	if(Utility.testString(suppCode)){
		sql.append("AND d.maker_code like '%"+suppCode.toUpperCase()+"%' \n");
	}
	if(Utility.testString(suppName)){
		sql.append("AND d.maker_name like '%"+suppName+"%'");
	}
	return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage); 
}
}
