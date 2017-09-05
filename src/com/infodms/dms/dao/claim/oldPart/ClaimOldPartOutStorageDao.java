package com.infodms.dms.dao.claim.oldPart;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ClaimOldPartOutPreListBean;
import com.infodms.dms.bean.SpefeeBaseBean;
import com.infodms.dms.bean.TtAsWrMainPartClaimBean;
import com.infodms.dms.bean.TtAsWrOldOutDetailBean;
import com.infodms.dms.bean.TtAsWrOldOutDoorBean;
import com.infodms.dms.bean.TtAsWrOldOutNoticeBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmAsWrBarcodePartStockPO;
import com.infodms.dms.po.TmPtSupplierPO;
import com.infodms.dms.po.TtAsSecondInStoreDetailPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrDiscountPO;
import com.infodms.dms.po.TtAsWrModelGroupPO;
import com.infodms.dms.po.TtAsWrOldOutDoorDetailPO;
import com.infodms.dms.po.TtAsWrOldOutNoticeDetailPO;
import com.infodms.dms.po.TtAsWrOldOutNoticePO;
import com.infodms.dms.po.TtAsWrOldOutPartPO;
import com.infodms.dms.po.TtAsWrOldPartLabourPO;
import com.infodms.dms.po.TtAsWrOldReturnedDetailExtendPO;
import com.infodms.dms.po.TtAsWrOldReturnedDetailPO;
import com.infodms.dms.po.TtAsWrPartStockLogPO;
import com.infodms.dms.po.TtAsWrPartStockPO;
import com.infodms.dms.po.TtAsWrPartsitemBarcodePO;
import com.infodms.dms.po.TtDeliveryOrderPO;
import com.infodms.dms.po.TtPartDefinePO;
import com.infodms.dms.po.TtPartMakerDefinePO;
import com.infodms.dms.po.TtPartMakerProblemDtlPO;
import com.infodms.dms.po.TtPartMakerProblemPO;
import com.infodms.dms.po.TtPartMakerRelationPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 类说明：索赔旧件出库Dao
 * 作者：  赵伦达
 */
@SuppressWarnings("rawtypes")
public class ClaimOldPartOutStorageDao extends BaseDao{
	
    public static Logger logger = Logger.getLogger(ClaimOldPartOutStorageDao.class);
	
	private static  ClaimOldPartOutStorageDao dao = null;
	
	public static final ClaimOldPartOutStorageDao getInstance() {
	   if(dao==null) return new ClaimOldPartOutStorageDao();
	   return dao;
	}
	/**
	 * Function：索赔旧件出库--出库记录查询
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@param curPage
	 * @return:		@param pageSize
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-18
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> getOutStorelogList(Long companyId,RequestWrapper request,int curPage, int pageSize){
		String supply_name=DaoFactory.getParam(request, "supply_name");//查询条件--供应商简称
		String out_start_date=DaoFactory.getParam(request, "out_start_date");//查询条件--配件代码
		String out_end_date=DaoFactory.getParam(request, "out_end_date");//查询条件--配件代码
		String supply_code=DaoFactory.getParam(request, "supply_code");//查询条件--供应商代码
		String out_type=DaoFactory.getParam(request, "out_claim_type");//查询条件--出库类型
		String range_no = DaoFactory.getParam(request, "range_no");//查询条件--出库单号
		String yieldly = DaoFactory.getParam(request, "yieldly");
		String claimNo = DaoFactory.getParam(request, "claim_no");
//		String type = DaoFactory.getParam(request, "type");
		String diy_flag = DaoFactory.getParam(request, "diy_flag");
		StringBuffer sql= new StringBuffer();
		sql.append("select  t.line_num,t.out_no,max(t.diy_flag) as diy_flag,t.range_no,t.relational_out_no, sum(decode(t.out_part_type,0,t.out_amout,1,t.out_amout,0)) part_amount ,t.out_type,\n");
		sql.append("MAX(t.supply_code) supply_code,MAX(t.supply_name)supply_name, to_char(t.out_date,'yyyy-mm-dd') out_time,u.name,\n");
		sql.append("CASE WHEN (t.out_type="+Constant.OUT_CLAIM_TYPE_01+" or t.out_type is null ) AND s.out_no IS NULL THEN 1\n");
		sql.append("     ELSE CASE WHEN (t.out_type="+Constant.OUT_CLAIM_TYPE_01+" or t.out_type is null ) AND s.out_no IS NOT NULL THEN 2\n");
		sql.append("     ELSE 3 END END  flag ,	 nvl(s.out_type,0) del_flag ,\n"); 
		sql.append(" 	count(*) all_AMOUNT,--出库数量(数据条数)\n");
		sql.append(" SUM(decode(t.remark,NULL,t.out_amout,0)) AMOUNT,--出库实物配件数\n");
		sql.append(" SUM(decode(t.remark,NULL,0,t.out_amout)) relation_AMOUNT, --关联配件数量\n"); 
		sql.append("  SUM(decode(t.hand_mark, NULL, 0,t.hand_mark)) hand_mark --手工添加数\n"); 

		sql.append("from tt_as_wr_old_out_part t,(SELECT out_no,out_type FROM TT_as_WR_range_single GROUP BY out_no,out_type)s,tc_user u\n");
		sql.append("WHERE t.out_by = u.user_id(+) and t.yieldly="+yieldly+"\n");
		sql.append("  AND t.out_no=s.out_no(+)\n");
		DaoFactory.getsql(sql, "t.supply_name", supply_name, 2);
		DaoFactory.getsql(sql, "t.supply_code", supply_code.toUpperCase(), 2);
		DaoFactory.getsql(sql, "t.out_type", out_type, 1);
		DaoFactory.getsql(sql, "t.range_no", range_no, 2);
		DaoFactory.getsql(sql, "t.claim_no", claimNo, 2);
		if(diy_flag.equals("0")){
			sql.append("  AND t.diy_flag is null \n");
		}else{
			DaoFactory.getsql(sql, "t.diy_flag", diy_flag, 1);
		}
		
		if(Utility.testString(out_start_date)){
			sql.append(" and to_char(t.out_date,'yyyy-mm-dd')>='"+ out_start_date+"' \n");
		}
		if(Utility.testString(out_end_date)){
			sql.append(" and to_char(t.out_date,'yyyy-mm-dd')<='" + out_end_date+ "' \n");
		}
		sql.append("GROUP BY t.line_num, t.out_no,t.range_no,t.out_type, t.relational_out_no, to_char(t.out_date,'yyyy-mm-dd'),u.name,s.out_no,s.out_type order by t.out_no desc \n"); 
		return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage); 
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getOutStorelogListSum(Long companyId,RequestWrapper request,int curPage, int pageSize){
		String supply_name=DaoFactory.getParam(request, "supply_name");//查询条件--供应商简称
		String out_start_date=DaoFactory.getParam(request, "out_start_date");//查询条件--配件代码
		String out_end_date=DaoFactory.getParam(request, "out_end_date");//查询条件--配件代码
		String supply_code=DaoFactory.getParam(request, "supply_code");//查询条件--供应商代码
		String out_type=DaoFactory.getParam(request, "out_claim_type");//查询条件--出库类型
		String range_no = DaoFactory.getParam(request, "range_no");//查询条件--出库单号
		
		String claim_no = DaoFactory.getParam(request, "claim_no");//查询条件-- 索赔单号
		String yieldly = DaoFactory.getParam(request, "yieldly");
		
//		String type = DaoFactory.getParam(request, "type");
		StringBuffer sql= new StringBuffer();
		sql.append("with tb_1 as (");
		sql.append("select  t.out_no,t.range_no,t.relational_out_no, sum(decode(t.out_part_type,0,t.out_amout,1,t.out_amout,0)) part_amount ,t.out_type,\n");
		sql.append("MAX(t.supply_code) supply_code,MAX(t.supply_name)supply_name, to_char(t.out_date,'yyyy-mm-dd') out_time,u.name,\n");
		sql.append("CASE WHEN (t.out_type="+Constant.OUT_CLAIM_TYPE_01+" or t.out_type is null ) AND s.out_no IS NULL THEN 1\n");
		sql.append("     ELSE CASE WHEN (t.out_type="+Constant.OUT_CLAIM_TYPE_01+" or t.out_type is null ) AND s.out_no IS NOT NULL THEN 2\n");
		sql.append("     ELSE 3 END END  flag ,	 nvl(s.out_type,0) del_flag ,\n"); 
		sql.append(" 	count(*) all_AMOUNT,--出库数量(数据条数)\n");
		sql.append(" SUM(decode(t.remark,NULL,t.out_amout,0)) AMOUNT,--出库实物配件数\n");
		sql.append(" SUM(decode(t.remark,NULL,0,t.out_amout)) relation_AMOUNT --关联配件数量\n"); 

		sql.append("from tt_as_wr_old_out_part t,(SELECT out_no,out_type FROM TT_as_WR_range_single GROUP BY out_no,out_type)s,tc_user u\n");
		sql.append("WHERE t.out_by = u.user_id(+) and t.yieldly="+yieldly+"\n");
		sql.append("  AND t.out_no=s.out_no(+)\n");
		DaoFactory.getsql(sql, "t.supply_name", supply_name, 2);
		DaoFactory.getsql(sql, "t.supply_code", supply_code.toUpperCase(), 2);
		DaoFactory.getsql(sql, "t.out_type", out_type, 1);
		DaoFactory.getsql(sql, "t.range_no", range_no, 2);
		DaoFactory.getsql(sql, "t.claim_no", claim_no, 2);
		if(Utility.testString(out_start_date)){
			sql.append(" and to_char(t.out_date,'yyyy-mm-dd')>='"+ out_start_date+"' \n");
		}
		if(Utility.testString(out_end_date)){
			sql.append(" and to_char(t.out_date,'yyyy-mm-dd')<='" + out_end_date+ "' \n");
		}
		sql.append("GROUP BY t.out_no,t.range_no,t.out_type, t.relational_out_no, to_char(t.out_date,'yyyy-mm-dd'),u.name,s.out_no,s.out_type order by t.out_no desc \n"); 
		sql.append(") ");
		sql.append("select sum(ALL_AMOUNT) ALL_AMOUNT_SUM,sum(AMOUNT) AMOUNT_SUM,  sum(RELATION_AMOUNT) RELATION_AMOUNT_SUM from tb_1");
		dao=ClaimOldPartOutStorageDao.getInstance();
		Map<String, Object> list= dao.pageQueryMap(sql.toString(), null, getFunName());
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public PageResult<ClaimOldPartOutPreListBean> noPartIsClaimQuery(Map params,int curPage, int pageSize){
		String supply_name=ClaimTools.dealParamStr(params.get("supply_name"));//查询条件--供应商简称
		String supply_code=ClaimTools.dealParamStr(params.get("supply_code"));//查询条件--供应商代码
		String partCode = ClaimTools.dealParamStr(params.get("partCode"));
		String partName = ClaimTools.dealParamStr(params.get("partName"));
		String modelCode = ClaimTools.dealParamStr(params.get("modelCode"));
		String modelName = ClaimTools.dealParamStr(params.get("modelName"));
		String yieldly = ClaimTools.dealParamStr(params.get("yieldly"));
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT  min(A.IS_NOTICE) IS_NOTICE,min(A.ID) ID,p.down_product_code supply_code,p.down_product_name supply_name,p.part_code,p.part_name ,a.model_code model_name,sum(1) all_amount \n");
		sql.append("  FROM TT_AS_WR_APPLICATION A,TT_AS_WR_PARTSITEM P,TM_PT_PART_BASE PB,Tt_As_Wr_Partsitem_Barcode pb1\n");
		sql.append("  WHERE 1=1\n");
		sql.append("  AND A.ID = P.ID\n");
		sql.append("  AND P.DOWN_PART_CODE = PB.PART_CODE  and p.part_id=pb1.part_id\n");
		sql.append("  AND PB.IS_RETURN ="+Constant.IS_RETURN_02+" \n");
		sql.append("     and pb.is_cliam="+Constant.IS_CLAIM_01+"\n");
		sql.append("  AND PB.PART_IS_CHANGHE = "+yieldly+"\n");
		sql.append("  AND NVL(P.BALANCE_QUANTITY,0) > NVL(P.RETURN_NUM,0)\n");
		sql.append("     and p.quantity>0\n");
		sql.append("  AND A.STATUS !="+Constant.CLAIM_APPLY_ORD_TYPE_01+"\n");
		if(Utility.testString(supply_name)){
			sql.append("     and  p.down_product_name like '%"+supply_name+"%'\n");
		}
		if(Utility.testString(supply_code)){
			sql.append("     and  p.down_product_code like '%"+supply_code.toUpperCase()+"%'\n");
		}
		if(Utility.testString(partCode)){
			sql.append("     and  pb.part_code like '%"+partCode.toUpperCase()+"%'\n");
		}
		if(Utility.testString(partName)){
			sql.append("     and  pb.part_name like '%"+partName+"%'\n");
		}
		if(Utility.testString(modelCode)){
			sql.append("     and a.model_code like '%"+modelCode.toUpperCase()+"%'\n");
		}
		if(Utility.testString(modelName)){
			sql.append("     and a.model_name like '%"+modelName+"%'\n");
		}
		sql.append("group by p.down_product_code,p.down_product_name,p.part_code,p.part_name,a.model_code"); 


		PageResult<ClaimOldPartOutPreListBean> pr=pageQuery(ClaimOldPartOutPreListBean.class, sql.toString(), null, pageSize, curPage);
		return pr;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> noPartIsClaimQuery(String part_code){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT  min(A.ID) ID,p.down_product_code supply_code,min(C.CLAIM_PRICE) PRICE,min(B.TEL) TEL,p.down_product_name supply_name,p.part_code,p.part_name ,a.model_code model_name,sum(1) all_amount \n");
		sql.append("  FROM TT_AS_WR_APPLICATION A,TT_AS_WR_PARTSITEM P left join tt_part_maker_define B on  p.down_product_code = B.MAKER_CODE");
		sql.append(",TM_PT_PART_BASE PB,Tt_As_Wr_Partsitem_Barcode pb1,tt_part_maker_relation C\n");
		sql.append("  WHERE 1=1\n");
		sql.append("  AND P.REAL_PART_ID = C.PART_ID ");
		sql.append("  AND A.ID = P.ID\n");
		sql.append("  AND P.DOWN_PART_CODE = PB.PART_CODE  and p.part_id=pb1.part_id\n");
		sql.append("  AND PB.IS_RETURN ="+Constant.IS_RETURN_02+" \n");
		sql.append("     and pb.is_cliam="+Constant.IS_CLAIM_01+"\n");
		sql.append("  AND NVL(P.BALANCE_QUANTITY,0) > NVL(P.RETURN_NUM,0)\n");
		sql.append("     and p.quantity>0\n");
		sql.append("  AND A.STATUS !="+Constant.CLAIM_APPLY_ORD_TYPE_01+"\n");
		if(Utility.testString(part_code)){
			sql.append("     and  pb.part_code like '%"+part_code+"%'\n");
		}
		sql.append("group by p.down_product_code,p.down_product_name,p.part_code,p.part_name,a.model_code");
		System.out.println(sql.toString());
	    List<Map<String, Object>> list= super.pageQuery(sql.toString(), null, this.getFunName());
		return list.get(0);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<TtAsWrOldOutDetailBean> getOutDetail(String outNo,String code,Long oemCompanyId,String yieldlys){
		StringBuffer sql = new StringBuffer();
		sql.append("  select u.name out_name,d.supplay_name,to_char(d.out_date, 'yyyy-mm-dd') out_time,\n");
		sql.append("               d.out_type,rd.part_code,rd.part_name,\n");
		sql.append(" sum(d.out_amount) out_amount,d.remark,odd.model_name\n");
		sql.append("          from tt_as_wr_old_out_detail d\n");
		sql.append("          left join tc_user u on u.user_id = d.out_by\n");
		sql.append("          left join tt_as_wr_old_returned_detail rd on rd.barcode_no =d.barcode_no\n");
		sql.append("          left join tt_as_wr_application a on a.claim_no = d.claim_no\n");
		sql.append("left join tt_as_wr_old_out_door od on od.out_no = d.out_no\n"); 

		sql.append("left join tt_as_wr_old_out_door_detail odd on odd.part_code = d.out_part_code and odd.door_id = od.door_id\n"); 

		sql.append("         where d.out_no = '"+outNo+"'\n");
		sql.append("           and d.supplay_code = '"+code+"'\n");
		sql.append("		and d.is_dikou=0\n");
		sql.append("         group by u.name,D.SUPPLAY_CODE,to_char(d.out_date, 'yyyy-mm-dd'),odd.model_name,d.remark,d.out_type, rd.part_code, rd.part_name, d.supplay_name\n");

		return this.select(TtAsWrOldOutDetailBean.class, sql.toString(), null);
	}
	
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> getOutDetail2(RequestWrapper request, Integer pageSize, Integer currPage){
		StringBuffer sb= new StringBuffer();
		sb.append("select distinct c.*\n" );
		sb.append("  from (select o.out_no,a.id,\n" );
		sb.append("               a.claim_no,\n" );
		sb.append("(case when a.is_import=10041001 then a.ww_dealer_name  else tm.dealer_shortname end) as dealer_shortname,");
		sb.append("              a.claim_type as claimType,\n" );
		sb.append("(select tc.code_desc\n" );
		sb.append("   from tc_code tc\n" );
		sb.append("  where tc.code_id = a.claim_type) as claim_type,");
		sb.append("               a.vin,\n" );
		sb.append("               a.model_name,\n" );
		sb.append("               a.ro_startdate,\n" );
		sb.append("               p.part_code,\n" );
		sb.append("               p.part_name,\n" );
		sb.append("               decode(p.responsibility_type,\n" );
		sb.append("                      94001001,\n" );
		sb.append("                      '主因件',\n" );
		sb.append("                      94001002,\n" );
		sb.append("                      '次因件') as responsibility_type,\n" );
		sb.append("               p.trouble_reason,\n" );
		sb.append("               p.producer_code as producer_code1, o.supply_code as producer_code,o.supply_name as producer_name,\n" );
		sb.append("               p.producer_name as producer_name1,\n" );
		sb.append("               nvl((select sum(nvl(l.balance_amount,0))\n" );
		sb.append("                     from Tt_As_Wr_Labouritem l\n" );
		sb.append("                    where l.wr_labourcode = p.wr_labourcode\n" );
		sb.append("                      and l.id = p.id\n" );
		sb.append("                      and p.responsibility_type = 94001001),\n" );
		sb.append("                   0) as labour_amount,\n" );
		sb.append("               nvl(case\n" );
		sb.append("                     when a.claim_type = 10661009 then\n" );
		sb.append("                      (select sum(n.balance_amount)\n" );
		sb.append("                         from tt_as_wr_netitem n\n" );
		sb.append("                        where n.id in p.id\n" );
		sb.append("                          and p.responsibility_type = 94001001\n" );
		sb.append("                          and p.part_code = n.main_part_code\n" );
		sb.append("                          and n.main_part_code <> '-1')\n" );
		sb.append("                     else\n" );
		sb.append("                      0\n" );
		sb.append("                   end,\n" );
		sb.append("                   0) as out_amount,\n" );
		sb.append("               nvl((select sum(c.pass_price)\n" );
		sb.append("                     from TT_AS_WR_COMPENSATION_APP c\n" );
		sb.append("                    where c.claim_no in a.claim_no\n" );
		sb.append("                      and p.responsibility_type = 94001001),\n" );
		sb.append("                   0) as com_amount,\n" );
		sb.append("               nvl((select sum(d.app_price)\n" );
		sb.append("                     from tt_claim_accessory_dtl d\n" );
		sb.append("                    where d.claim_no in a.claim_no\n" );
		sb.append("                      and p.responsibility_type = 94001001),\n" );
		sb.append("                   0) as acc_amount,\n" );
		sb.append("               nvl(case\n" );
		sb.append("                     when a.claim_type <> 10661009 then\n" );
		sb.append("                      (select sum(n.balance_amount)\n" );
		sb.append("                         from tt_as_wr_netitem n\n" );
		sb.append("                        where n.id in p.id)\n" );
		sb.append("                     else\n" );
		sb.append("                      0\n" );
		sb.append("                   end,\n" );
		sb.append("                   0) as other_amount,\n" );
		sb.append("               (case\n" );
		sb.append("                 when a.claim_type = 10661006 then\n" );
		sb.append("                  a.balance_amount\n" );
		sb.append("                 else\n" );
		sb.append("                  0\n" );
		sb.append("               end) as activity_amount\n" );
		sb.append("          from tt_as_wr_application  a,\n" );
		sb.append("               Tt_As_Wr_Partsitem    p,\n" );
		sb.append("               Tt_As_Wr_Old_Out_Part o,\n" );
		sb.append("               tm_dealer             tm\n" );
		sb.append("         where a.id = p.id\n" );
		sb.append("           and tm.dealer_id = a.dealer_id\n" );
		sb.append("           and o.claim_no = a.claim_no\n" );
		DaoFactory.getsql(sb, "p.responsibility_type", DaoFactory.getParam(request, "responsibility_type"), 1);
		DaoFactory.getsql(sb, "p.part_code", DaoFactory.getParam(request, "part_code"), 2);
		DaoFactory.getsql(sb, "p.part_name", DaoFactory.getParam(request, "part_name"), 2);
		if ("0".equals(DaoFactory.getParam(request,"hand_mark"))) {
			sb.append("           and o.hand_mark is null\n" );
		}else if("1".equals(DaoFactory.getParam(request,"hand_mark"))){
			DaoFactory.getsql(sb, "o.hand_mark", DaoFactory.getParam(request, "hand_mark"),1);
		}
		sb.append("         ) c\n" );
		sb.append(" where 1 = 1");
		DaoFactory.getsql(sb, "c.out_no", DaoFactory.getParam(request, "outNo"), 1);
		DaoFactory.getsql(sb, "c.claimtype", DaoFactory.getParam(request, "CLAIM_TYPE"), 1);
		DaoFactory.getsql(sb, "c.claim_no", DaoFactory.getParam(request, "claim_no"), 2);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public void exportOutDetail2 (ActionContext act,
			PageResult<Map<String, Object>> list) {
		try {
			String[] head={"索赔单号","商家简称","索赔类型","VIN","车型","维修日期","配件编码","配件名称","主次","原因分析","供应商编码","供应商简称","工时费","外出费","辅料费","补偿费","其他","活动费用"};
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String claim_no = BaseUtils.checkNull(map.get("CLAIM_NO"));
					String dealer_shortname = BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					String claim_type = BaseUtils.checkNull(map.get("CLAIM_TYPE"));
					String vin = BaseUtils.checkNull(map.get("VIN"));
					String model_name = BaseUtils.checkNull(map.get("MODEL_NAME"));
					String ro_startdate = BaseUtils.checkNull(map.get("RO_STARTDATE"));
					String part_code = BaseUtils.checkNull(map.get("PART_CODE"));
					String part_name = BaseUtils.checkNull(map.get("PART_NAME"));
					String responsibility_type = BaseUtils.checkNull(map.get("RESPONSIBILITY_TYPE"));
					String trouble_reason = BaseUtils.checkNull(map.get("TROUBLE_REASON"));
					String producer_code = BaseUtils.checkNull(map.get("PRODUCER_CODE"));
					String producer_name = BaseUtils.checkNull(map.get("PRODUCER_NAME"));
					String labour_amount = BaseUtils.checkNull(map.get("LABOUR_AMOUNT"));
					String out_amount = BaseUtils.checkNull(map.get("OUT_AMOUNT"));
					String acc_amount = BaseUtils.checkNull(map.get("ACC_AMOUNT"));
					String com_amount = BaseUtils.checkNull(map.get("COM_AMOUNT"));
					String other_amount = BaseUtils.checkNull(map.get("OTHER_AMOUNT"));
					String activity_amount = BaseUtils.checkNull(map.get("ACTIVITY_AMOUNT"));
					String [] detail={claim_no,dealer_shortname,claim_type,vin,model_name,ro_startdate,part_code,part_name,responsibility_type,trouble_reason,	producer_code,producer_name,labour_amount,out_amount,acc_amount,com_amount,other_amount,activity_amount};
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出旧件出库明细清单 "+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getOutDetail2(String outNo,String code){
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT p.out_no,\n" );
		sb.append("       p.range_no,\n" );
		sb.append("       p.out_by,\n" );
		sb.append("       t.ro_no,t.id,\n" );
		sb.append("       u.name,\n" );
		sb.append("       to_char(p.out_date, 'yyyy-mm-dd hh24:mi') out_time,\n" );
		sb.append("       p.out_type,\n" );
		sb.append("       p.supply_code,\n" );
		sb.append("       p.supply_name,\n" );
		sb.append("       p.out_amout,\n" );
		sb.append("       p.out_part_code,\n" );
		sb.append("       p.out_part_name,\n" );
		sb.append("       p.claim_no,\n" );
		sb.append("       p.remark,\n" );
		sb.append("       CASE\n" );
		sb.append("         WHEN P.OUT_PART_TYPE = 0 AND p.out_amout = 0 THEN\n" );
		sb.append("          '无实物返厂'\n" );
		sb.append("         ELSE\n" );
		sb.append("          CASE\n" );
		sb.append("            WHEN p.out_part_type = 2 THEN\n" );
		sb.append("             '特殊费用'\n" );
		sb.append("            ELSE\n" );
		sb.append("             '实物返厂'\n" );
		sb.append("          END\n" );
		sb.append("       END HAS_PART\n" );
		sb.append("\n" );
		sb.append("  FROM Tt_As_Wr_Old_Out_Part p, tc_user u, Tt_As_Wr_Application t\n" );
		sb.append(" where p.claim_no = t.claim_no(+)");
		sb.append("and p.out_no='"+outNo+"'\n");
		sb.append("AND p.out_by=u.user_id"); 
		return  this.pageQuery(sb.toString(), null, this.getFunName());
	}
	@SuppressWarnings("unchecked")
	public PageResult<TtDeliveryOrderPO> queryBatchStockList(Map params,int curPage, int pageSize){
		String Stock_type=ClaimTools.dealParamStr(params.get("Stock_type"));//查询条件--出库类型
		String out_start_date=ClaimTools.dealParamStr(params.get("out_start_date"));//查询条件--出库开始时间
		String out_end_date=ClaimTools.dealParamStr(params.get("out_end_date"));//查询条件--出库结束时间
		String stock_no=ClaimTools.dealParamStr(params.get("stock_no"));//查询条件--出库单号
		
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select Stock_id,stock_no, stock_date, star_time, end_time,stock_type,stock_number  From TT_Delivery_order a where a.stock_type  in ("+Constant.Stock_type_1+","+Constant.Stock_type_2+") " );
		if(Utility.testString(out_start_date)){
			sqlStr.append(" and a.STOCK_DATE>=to_date('" + out_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		}
		if(Utility.testString(out_end_date)){
			sqlStr.append(" and a.STOCK_DATE<=to_date('" + out_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		if(Utility.testString(Stock_type)){
			sqlStr.append("and a.stock_type ='"+Stock_type+"'\n" );
		}
		
		if(Utility.testString(stock_no)){
			sqlStr.append("and a.Stock_no like'%"+stock_no.replaceAll("'", "\''")+"%'\n" );
		}
		
		sqlStr.append(" order by Stock_date desc");
		
		PageResult<TtDeliveryOrderPO> pr=pageQuery(TtDeliveryOrderPO.class, sqlStr.toString(), null, pageSize, curPage);
		return pr;
	}
	
	@SuppressWarnings("unchecked")
	public PageResult<TtDeliveryOrderPO> queryScanningStockList(Map params,int curPage, int pageSize){
		String SUPPLIER_NAME=ClaimTools.dealParamStr(params.get("SUPPLIER_NAME"));//查询条件--供应商名称
		String SUPPLIER_CODE=ClaimTools.dealParamStr(params.get("SUPPLIER_CODE"));//查询条件--供应商代码
		String out_start_date=ClaimTools.dealParamStr(params.get("out_start_date"));//查询条件--出库开始时间
		String out_end_date=ClaimTools.dealParamStr(params.get("out_end_date"));//查询条件--出库结束时间
		String stock_no=ClaimTools.dealParamStr(params.get("stock_no"));//查询条件--出库单号
		String is_stock=ClaimTools.dealParamStr(params.get("is_stock"));//查询条件--是否出库
		
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select a.stock_id,a.stock_no,a.stock_date,a.supplier_id,a.supplier_name,a.supplier_code,a.stock_type,a.stock_state,a.end_time,a.star_time,a.remark,a.stock_number From TT_Delivery_order a  where    a.stock_type  = "+Constant.Stock_type_3+" " );
		if(Utility.testString(out_start_date)){
			sqlStr.append(" and a.STOCK_DATE>=to_date('" + out_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		}
		if(Utility.testString(out_end_date)){
			sqlStr.append(" and a.STOCK_DATE<=to_date('" + out_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		
		
		if(Utility.testString(stock_no)){
			sqlStr.append("and a.Stock_no like'%"+stock_no.replaceAll("'", "\''")+"%'\n" );
		}
		
		if(Utility.testString(is_stock)){
			sqlStr.append("and a.STOCK_STATE ='"+is_stock+"'\n" );
		}
		
		if(Utility.testString(SUPPLIER_NAME)){
			sqlStr.append("and a.SUPPLIER_NAME like'%"+SUPPLIER_NAME.replaceAll("'", "\''")+"%'\n" );
		}
		
		if(Utility.testString(SUPPLIER_CODE)){
			sqlStr.append("and a.SUPPLIER_CODE like'%"+SUPPLIER_CODE.replaceAll("'", "\''")+"%'\n" );
		}
		
		sqlStr.append("    order by stock_id desc" );
		PageResult<TtDeliveryOrderPO> pr=pageQuery(TtDeliveryOrderPO.class, sqlStr.toString(), null, pageSize, curPage);
		return pr;
	}
	
	@SuppressWarnings("unchecked")
	public PageResult<TmAsWrBarcodePartStockPO> addStockListQuery(Map params,int curPage, int pageSize){
		
		String out_start_date=ClaimTools.dealParamStr(params.get("out_start_date"));//查询条件--出库开始时间
		String out_end_date=ClaimTools.dealParamStr(params.get("out_end_date"));//查询条件--出库结束时间
		
		
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select a.part_name,a.part_code,count(id) as COUNT  From tt_as_wr_barcode_part_stock a where a.is_library =0   and a.is_cliam = 10011001   and a.status = 10011001 " );
		if(Utility.testString(out_start_date)){
			sqlStr.append(" and a.CREATE_DATE>=to_date('" + out_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		}
		if(Utility.testString(out_end_date)){
			sqlStr.append(" and a.CREATE_DATE<=to_date('" + out_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		sqlStr.append("group by  a.part_name,a.part_code");
		
		PageResult<TmAsWrBarcodePartStockPO> pr=pageQuery(TmAsWrBarcodePartStockPO.class, sqlStr.toString(), null, pageSize, curPage);
		return pr;
	}
	
	
@SuppressWarnings("unchecked")
public PageResult<TmAsWrBarcodePartStockPO> addOffsetStockListQuery(Map params,int curPage, int pageSize){
		
		String out_start_date=ClaimTools.dealParamStr(params.get("out_start_date"));//查询条件--出库开始时间
		String out_end_date=ClaimTools.dealParamStr(params.get("out_end_date"));//查询条件--出库结束时间
		
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select c.part_id,       c.barcode_no,       c.part_name,       c.part_code,       c.create_date,      count(c.id) as count      From tt_as_wr_deduct_detail       a,");
				sqlStr.append(" tt_as_wr_old_returned_detail b,       tt_as_wr_barcode_part_stock  c,       tt_as_wr_deduct d where a.part_id = b.id   and b.barcode_no = c.barcode_no   and c.is_library =0   and c.is_cliam =10011002   and d.id=a.deduct_id   and c.status = 10011001   and d.is_count=2" );
		if(Utility.testString(out_start_date)){
			sqlStr.append(" and c.CREATE_DATE>=to_date('" + out_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		}
		if(Utility.testString(out_end_date)){
			sqlStr.append(" and c.CREATE_DATE<=to_date('" + out_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		sqlStr.append("group by  c.part_id, c.barcode_no, c.part_name, c.part_code, c.create_date");
		
		PageResult<TmAsWrBarcodePartStockPO> pr=pageQuery(TmAsWrBarcodePartStockPO.class, sqlStr.toString(), null, pageSize, curPage);
		return pr;
	}

	
	//根据出库选择的条码查询索赔单
	@SuppressWarnings("unchecked")
	public List<TtAsWrApplicationPO> getClaimList(String barNo){
		StringBuffer sql=new StringBuffer();
		sql.append("select  a.fi_date,a.claim_no,a.status,decode(a.VERSEON,null,9999,a.VERSEON) VERSEON\n");
		sql.append("from Tt_As_Wr_Application a ,Tt_As_Wr_Partsitem p ,Tt_As_Wr_Partsitem_Barcode b\n");
		sql.append("where a.id = p.id and b.part_id = p.part_id\n");
		sql.append("and b.barcode_no in ("+barNo+")\n");
		sql.append("group by  a.fi_date,a.claim_no,a.status,a.VERSEON"); 
		List<TtAsWrApplicationPO> pr= this.select(TtAsWrApplicationPO.class, sql.toString(), null);
		return pr;
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getOutNo(String suppCode,String suppName){
		StringBuffer sql = new StringBuffer();
		sql.append("select t.out_no\n");
		sql.append("from tt_as_wr_old_out_part t\n");
		sql.append("where 1=1\n");
		if(Utility.testString(suppCode)||Utility.testString(suppName)){
			if(Utility.testString(suppCode)){
				sql.append("and t.supply_code like '%"+suppCode.toUpperCase()+"%'\n");
			}
			if(Utility.testString(suppName)){
				sql.append("and t.supply_name like '%"+suppName+"%'\n");
			}
		}else {
			sql.append(" and  1=2\n");
		}
		sql.append("and t.supply_code is not null\n");
		sql.append("group by t.supply_code,t.supply_name,t.out_no\n"); 

		sql.append("order by t.out_no desc"); 
		List<Map<String, Object>> list=super.pageQuery(sql.toString(), null, getFunName());
		
		return list;
	}
	//根据出库选择的条码查询索赔单
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getPartItemList(String barNo){
		StringBuffer sql=new StringBuffer();
		sql.append("select  a.claim_no,p.down_part_code,p.quantity,p.balance_quantity\n");
		sql.append("from Tt_As_Wr_Application a ,Tt_As_Wr_Partsitem p \n");
		sql.append("where a.id = p.id \n");
		sql.append("and a.claim_no in ("+barNo+")\n");
		sql.append("group by  a.claim_no,p.down_part_code,p.quantity,p.balance_quantity\n"); 

		List<Map<String, Object>> result = super.pageQuery(sql.toString(),null, this.getFunName());
		return result;
	}
	
	/**
	 * 
	 * @param claimNo
	 * @param partCode
	 * @return 0标识工时材料出库    1标识只出工时和关联费用  2标识只出材料费  -1标识其他不处理
	 */
	@SuppressWarnings("unchecked")
	public int checkOutPartIsQHJ(String claimNo,String partCode){
		TtAsWrOldReturnedDetailPO pp = new TtAsWrOldReturnedDetailPO();
		pp.setClaimNo(claimNo);
		pp.setPartCode(partCode);
		List<TtAsWrOldReturnedDetailPO> tList = ClaimOldPartOutStorageDao.getInstance().select(pp);
		if(null!=tList && tList.size()>0){
			TtAsWrOldReturnedDetailPO tmpPO = tList.get(0);
			if(tmpPO.getQhjFlag().toString().equals("1")&&
					tmpPO.getKcdbFlag().toString().equals("2")){//已调拨的切换件
				if(tmpPO.getIsOut().toString().equals("0")){//未出库
					return 0;
				}else if(tmpPO.getIsOut().toString().equals("1")){//已经出库属于错误数据
					return -1;
				}else if(tmpPO.getIsOut().toString().equals("2")){//部分出库标识已经出过工时,本次只出材料费
					return 2;
				}
			}else if(tmpPO.getQhjFlag().toString().equals("1")&&
					(!tmpPO.getKcdbFlag().toString().equals("2"))){//未调拨的切换件,以无件形式出库，此时只能出工时和关联费用
					return 1;
			}else{
				return 0;
			}
		}
		return -1;
	}
	/**
	 * Function：出库操作
	 *           修改旧件库存表信息，新增出库记录表
	 * @param  ：	
	 * @return:		@param request 
	 * @throw：	
	 * LastUpdate：	2010-6-18
	 */
	@SuppressWarnings("unchecked")
	public void saveOutOfStoreLogOper(RequestWrapper request,AclUserBean logonUserBean){
    	String idStr=request.getParamValue("idStr");
    	ClaimOldPartOutStorageDao daos = ClaimOldPartOutStorageDao.getInstance();
    	if(BaseUtils.testString(idStr)){
    		//String no = GenerateStockNo();//生产出库单号 
    		String noType = request.getParamValue("noType");//出库方式 (0混合出库/1分单号出库)
    		String isHs = request.getParamValue("isHs");//is_house
    		String outType = request.getParamValue("OUT_CLAIM_TYPE");//出库类型
    		String yieldly = request.getParamValue("yieldly");//基地
    		String relationOutNo = request.getParamValue("relationOutNo");//关联退赔单号
    		if("-1".equalsIgnoreCase(relationOutNo)||"".equalsIgnoreCase(relationOutNo)){
    			relationOutNo=null;
    		}
    		String rangeNo = getrangeNo(logonUserBean, daos, noType, isHs);
    		String[] arr=idStr.split(",");
    		for(int i=0;i<arr.length;i++){
    			String claimNo = request.getParamValue("claimNo"+arr[i]);
    			String partCode = request.getParamValue("partCode"+arr[i]);
    			String partName = request.getParamValue("partName"+arr[i]);
    			String supplyCode = request.getParamValue("supplyCode"+arr[i]);
    			String supplyName = request.getParamValue("supplyName"+arr[i]);
    			String allAmount = request.getParamValue("allAmount"+arr[i]);//出库数
    			String remark = request.getParamValue("remark"+arr[i]);
    			String partReturn = request.getParamValue("partReturn"+arr[i]);//主因件类型
    			/**
    			 * 保存出库明细
    			 */
    			TtAsWrOldOutPartPO pp = new TtAsWrOldOutPartPO();
    			pp.setClaimNo(claimNo);
    			pp.setCreateBy(logonUserBean.getUserId());
    			pp.setCreateDate( new Date());
    			pp.setId(Long.parseLong(SequenceManager.getSequence("")));
    			pp.setOutAmout(Integer.parseInt(allAmount));
    			pp.setOutBy(logonUserBean.getUserId());
    			pp.setOutDate( new Date());
    			pp.setOutNo(rangeNo);
    			pp.setOutPartCode(partCode);
    			pp.setOutPartName(partName);
    			pp.setOutType(Integer.parseInt(outType));
    			pp.setRemark(remark);
    			pp.setSupplyCode(supplyCode);
    			pp.setSupplyName(supplyName);
    			pp.setYieldly(Integer.parseInt(yieldly));
    			pp.setOutPartType(Integer.parseInt(partReturn));
    			pp.setRelationalOutNo(relationOutNo);
    			pp.setRangeNo(rangeNo);
    			// 0标识工时材料出库    1标识只出工时和关联费用  2标识只出材料费  -1标识其他不处理
    			int outFlag = checkOutPartIsQHJ(claimNo,partCode);
    			if(outFlag!=-1){
    				pp.setOutFlag(outFlag);
    				if(outFlag==1){//如果是只出工时,则设置为无件出库
    					partReturn = "0";
    					pp.setOutPartType(0);
    				}
    			}
    			//得到真实配件代码的ID
    			TtPartDefinePO d  = new TtPartDefinePO();
    			d.setPartOldcode(partCode);
    			d = (TtPartDefinePO) daos.select(d).get(0);
    			if(Constant.OUT_CLAIM_TYPE_01.toString().equals(outType)){//出库类型=供应商开票
    				String partCode2 = "";
					String partCode3 = "";
					String partCode4 = "";
    				if(partCode.length()==18){
    					//17010510-C01-B00-3 18位的包含B00的件取17010510-C01-000-3的价格
    					if("B00".equals(partCode.substring(13,16))){
    						partCode2 = partCode.substring(0, 13)+"000"+partCode.substring(16, 18);
    						partCode3 = partCode2;
    						partCode4 = partCode2;
    					}else{
    						partCode2 = partCode.substring(0, partCode.length()-3)+"000";
    						partCode3 = partCode.substring(0, partCode.length()-3)+"B00";
    						partCode4 = partCode.substring(0, partCode.length()-3)+"B0Y";
    					}
    				}else{
    					partCode2 = partCode.substring(0, partCode.length()-3)+"000";
    					partCode3 = partCode.substring(0, partCode.length()-3)+"B00";
    					partCode4 = partCode.substring(0, partCode.length()-3)+"B0Y";
    				}
    				/*String partCode2 = partCode.substring(0, partCode.length()-3)+"000";
    				String partCode3 = partCode.substring(0, partCode.length()-3)+"B00";
    				String partCode4 = partCode.substring(0, partCode.length()-3)+"B0Y";*/
    				StringBuffer sql = new StringBuffer();
    				//将配件的最后3为分别替换
    				sql.append("SELECT max(r.part_id) part_id,max(r.maker_id) maker_id  FROM tt_part_maker_relation r\n");
    				sql.append("WHERE r.part_id  in (SELECT d.part_id FROM tt_part_define d WHERE d.part_oldcode in ('"+partCode+"','"+partCode2+"','"+partCode3+"','"+partCode4+"'))\n");
    				sql.append("AND r.maker_id = (SELECT md.maker_id  FROM tt_part_maker_define md WHERE md.maker_code='"+supplyCode+"') group by part_id,maker_id"); 
    				List<Map<String,Object>> lit = daos.pageQuery(sql.toString(), null, daos.getFunName());
    				if(lit!=null&&lit.size()>0){
    					TtPartMakerRelationPO rp = new TtPartMakerRelationPO();
    					rp.setPartId(Long.valueOf(String.valueOf(lit.get(0).get("PART_ID"))));
    					rp.setMakerId(Long.valueOf(String.valueOf(lit.get(0).get("MAKER_ID"))));
    					rp = (TtPartMakerRelationPO) daos.select(rp).get(0);
    					String insql = "select * from range_temp t WHERE t.out_no='"+rangeNo+"' AND t.part_id="+d.getPartId()+" AND t.maker_id ="+rp.getMakerId()+"";
    					List<Map<String,Object>> list = daos.pageQuery(insql, null, daos.getFunName());
    					
    					if(list==null||list.size()<1){
    						String sqll = "INSERT INTO range_temp VALUES(f_getid(),'"+rangeNo+"',"+d.getPartId()+","+rp.getMakerId()+","+logonUserBean.getUserId()+","+rp.getClaimPrice()+","+rp.getPartXs()+","+rp.getLabourXs()+")";
    						daos.insert(sqll);
    					}
    				}else{//增加强制出库逻辑
    					sql.delete(0, sql.toString().length());
						sql.append("select Max(claim_price) claim_price,max(PART_XS) PART_XS,MAX(LABOUR_XS) LABOUR_XS,part_id\n");
						sql.append("  from tt_part_maker_relation\n");
						sql.append(" where part_id in\n");
						sql.append("       (select part_id\n");
						sql.append("          from tt_part_define\n");
						sql.append("         where part_oldcode in ('"+partCode+"','"+partCode2+"','"+partCode3+"','"+partCode4+"'))\n");
						sql.append(" group by part_id"); 
						List<Map<String,Object>> tList = daos.pageQuery(sql.toString(), null, this.getFunName());
						if(null != tList && tList.size()>0){
							TtPartMakerDefinePO mDefine = new TtPartMakerDefinePO();
							mDefine.setMakerCode(supplyCode);
							List<TtPartMakerDefinePO> makerDefineList = dao.select(mDefine);
							TtPartMakerDefinePO tmpMaker = makerDefineList.get(0);
							String sqll = "INSERT INTO range_temp VALUES(f_getid(),'"+rangeNo+"',"+d.getPartId()+","+tmpMaker.getMakerId()+","+logonUserBean.getUserId()+","+tList.get(0).get("CLAIM_PRICE")+","+tList.get(0).get("PART_XS")+","+tList.get(0).get("LABOUR_XS")+")";
							daos.insert(sqll);
						}
    					//new Exception("没有找到供应商配件对应关系!");
    				}
        		}
    			
    			
    			//此处不执行插入,如果是误判时,不操作无实物件以及特殊单
    			//(1实物返件,0无件返厂2特殊费用)
    			if("1".equalsIgnoreCase(partReturn)){//如果选择的是旧件回运单中的数据,则要进行相应的操作。如果是不回运或者维修的的单子则直接进行
    				this.insert(pp); 
    			/**
    			 * 选择的主因件出库保存后,还要查询其次因件同时将其修改为出库状态并加入出库明细
    			 */
    			StringBuffer sqlStr = new StringBuffer();
    			sqlStr.append("SELECT d.claim_no,d.part_code,d.part_name,SUM(d.sign_amount)  amount\n");
    			sqlStr.append("FROM Tt_As_Wr_Old_Returned_Detail d\n");
    			sqlStr.append("WHERE 1=1\n");
    			sqlStr.append("AND d.is_main_code="+Constant.RESPONS_NATURE_STATUS_02+"\n");
    			sqlStr.append("AND d.is_out=0 AND d.is_cliam=0 and d.is_in_house="+Constant.IF_TYPE_YES+"\n");
    			sqlStr.append("AND d.sign_amount>0\n");
    			sqlStr.append("AND d.main_part_code='"+partCode+"'\n");
    			sqlStr.append(" AND D.claim_no='"+claimNo+"'\n");
    			sqlStr.append(" GROUP BY d.claim_no,d.part_code,d.part_name"); 
    			List<Map<String,Object>> resList = this.pageQuery(sqlStr.toString(), null, this.getFunName());
    			if(resList!=null && resList.size()>0){//如果查询结果集有数据,说明该件下含有次因件，此时就将这些次因件也出库，并且插入到出库明细中
    				for(int k=0;k<resList.size();k++){
    					StringBuffer sqlr = new StringBuffer();//更新次因件的出库状态
    	    			sqlr.append(" update tt_as_wr_old_returned_detail d set d.is_out=1,d.is_cliam='1' WHERE d.claim_no='"+resList.get(k).get("CLAIM_NO")+"' and d.is_out in(0,2) AND d.part_code='"+resList.get(k).get("PART_CODE")+"' AND d.sign_amount>0");
    	    			daos.update(sqlr.toString(), null);
    	    			//开始保存次因件的出库记录
    	    			TtAsWrOldOutPartPO pp2 = new TtAsWrOldOutPartPO();
    	    			pp2.setClaimNo(resList.get(k).get("CLAIM_NO").toString());
    	    			pp2.setCreateBy(logonUserBean.getUserId());
    	    			pp2.setCreateDate( new Date());
    	    			pp2.setId(Long.parseLong(SequenceManager.getSequence("")));
    	    			pp2.setOutAmout(Integer.parseInt(resList.get(k).get("AMOUNT").toString()));
    	    			pp2.setOutBy(logonUserBean.getUserId());
    	    			pp2.setOutDate( new Date());
    	    			pp2.setOutNo(rangeNo);
    	    			pp2.setOutPartCode(resList.get(k).get("PART_CODE").toString());
    	    			pp2.setOutPartName(resList.get(k).get("PART_NAME").toString());
    	    			pp2.setOutType(Integer.parseInt(outType));
    	    			pp2.setRemark(partCode+"责任连带");
    	    			pp2.setYieldly(Integer.parseInt(yieldly));
    	    			pp2.setOutPartType(Integer.parseInt(partReturn));
    	    			pp2.setRelationalOutNo(relationOutNo);
    	    			pp2.setRangeNo(rangeNo);
    	    			this.insert(pp2); 
    				}
    			}
    			 /**
    			  * 如果是误判出库,则还要将二次抵扣数据保存。
    			  */
    			 if(Constant.OUT_CLAIM_TYPE_03.toString().equals(outType)){//误判=出库类型
    				 
    				 TtAsWrApplicationPO ap = new TtAsWrApplicationPO();
    				 ap.setClaimNo(claimNo);
    				 ap = (TtAsWrApplicationPO) this.select(ap).get(0);
    				 
    				//得到选择的抵扣件相关信息
    				 StringBuffer ss = new StringBuffer();
    				 ss.append("SELECT  L.WR_LABOURCODE,L.WR_LABOURNAME,L.LABOUR_AMOUNT,P.DOWN_PART_CODE,P.DOWN_PART_NAME,P.QUANTITY,P.PRICE,P.AMOUNT\n");
    				 ss.append("FROM TT_AS_WR_PARTSITEM P ,TT_AS_WR_LABOURITEM L\n");
    				 ss.append("WHERE L.ID = P.ID AND P.ID="+ap.getId()+" AND P.PART_CODE='"+partCode+"' AND P.WR_LABOURCODE = L.WR_LABOURCODE"); 
    				 List<Map<String,String>> sList = this.pageQuery(ss.toString(), null, getFunName());
    				 
    				 
    				 TtAsWrDiscountPO ddp = new TtAsWrDiscountPO();
    				 ddp.setClaimId(ap.getId());
    				 ddp.setClaimNo(ap.getClaimNo());
    				 ddp.setCreateBy(logonUserBean.getUserId());
    				 ddp.setCreateDate(new Date());
    				 ddp.setDealerId(ap.getDealerId());
    				 ddp.setDiscountDate(new Date());
    				 ddp.setDeductReson(remark);
    				 ddp.setDownPartCode(partCode);
    				 ddp.setDownPartName(partName);
	    			 //查询供应商ID
    				 ddp.setDownProductCode(supplyCode);
    				 ddp.setDownProductName(supplyName);
    				 ddp.setId(Long.valueOf(SequenceManager.getSequence("")));
	    			 //查询工时
    				 ddp.setWrLabourcode(sList.get(0).get("WR_LABOURCODE"));
    				 ddp.setWrLabourname(sList.get(0).get("WR_LABOURNAME"));
    				 
    				 /**
    				  * 通过迭代查询出,与该配件有关的次因件对应的所有工时,
    				  */
    				 StringBuffer laSql = new StringBuffer();
    				 laSql.append("SELECT l.labour_id,l.wr_labourcode,l.wr_labourname,l.labour_amount FROM  Tt_As_Wr_Labouritem l\n");
    				 laSql.append("WHERE l.wr_labourcode IN (\n");
    				 laSql.append("SELECT  p.wr_labourcode FROM Tt_As_Wr_Partsitem p\n");
    				 laSql.append("WHERE 1=1  AND p.ID="+ap.getId()+" AND p.responsibility_type="+Constant.RESPONS_NATURE_STATUS_02+"\n");
    				 laSql.append("START WITH p.ID="+ap.getId()+" AND p.part_code = '"+partCode+"'\n");
    				 laSql.append("CONNECT BY PRIOR  p.down_part_code = p.main_part_code\n");
    				 laSql.append(") AND l.ID = "+ap.getId()+""); 
    				 List<Map<String,String>> list = this.pageQuery(laSql.toString(), null, getFunName());
    				
    				 //判断索赔单状态,如果是结算审核中,则直接操作子表数据。如果不是结算审核中，则直接扣掉服务站的材料费和工时费
    				 if(ap.getStatus().intValue()==Constant.CLAIM_APPLY_ORD_TYPE_08.intValue()){
    					 ddp.setDiscountPriec(0.0);
        				 ddp.setDiscountSum(Integer.parseInt(allAmount));
        				 ddp.setDiscount(0.0);
        				 ddp.setLabourPrice(0.0);
    					 if(list !=null && list.size()>0){//如果有数据结果集
        					 for(int l =0; l<list.size();l++){//循环处理
        						 /**
        						  * 第一步：将工时对应的价格扣完
        						  * 第二步：将工时下面的所有配件进行扣除,并将有旧件的设置为出库状态
        						  * 第三步：将关联件也保存为抵扣明细
        						  */
        						 String ll = " UPDATE Tt_As_Wr_Labouritem l SET l.apply_amount=0,l.balance_amount=0,l.apply_quantity=0,l.balance_quantity=0 WHERE l.labour_id="+list.get(l).get("LABOUR_ID")+" AND l.apply_amount>0";
        						 this.update(ll, null);
        						 String pl = "UPDATE Tt_As_Wr_Partsitem p SET  p.apply_quantity=0,p.apply_amount=0,p.balance_amount=0,p.balance_quantity=0 WHERE p.ID="+ap.getId()+" AND p.wr_labourcode='"+list.get(l).get("WR_LABOURCODE")+"' AND p.apply_amount>0";
        						 this.update(pl, null);
        						 String ol = "UPDATE Tt_As_Wr_Old_Returned_Detail d SET d.is_out=1,d.is_cliam=1  WHERE d.claim_id ="+ap.getId()+" AND d.part_id IN (SELECT p.part_id FROM Tt_As_Wr_Partsitem p WHERE p.ID="+ap.getId()+"  AND p.wr_labourcode='"+list.get(l).get("WR_LABOURCODE")+"' AND d.is_out in(0,2) AND  p.part_code NOT IN('"+partCode+"'))";
        						 this.update(ol, null);
        						 
        						 String ppl = "select P.PART_ID,P.QUANTITY,P.AMOUNT,P.PRICE,P.DOWN_PART_CODE,P.DOWN_PART_NAME,P.DOWN_PRODUCT_CODE,P.DOWN_PRODUCT_NAME from Tt_As_Wr_Partsitem p where p.id="+ap.getId()+" and p.wr_labourcode='"+list.get(l).get("WR_LABOURCODE")+"' AND  p.part_code NOT IN('"+partCode+"')";
        						 List<Map<String,String>> pList = this.pageQuery(ppl.toString(), null, getFunName());
        						 if(pList!=null && pList.size()>0){//该工时下面的所有配件进行抵扣明细保存
        							 for(int p=0;p<pList.size();p++){
        								 //保存抵扣明细
                						 TtAsWrDiscountPO dip = new TtAsWrDiscountPO();
                    	    			 dip.setClaimId(ap.getId());
                    	    			 dip.setClaimNo(ap.getClaimNo());
                    	    			 dip.setCreateBy(logonUserBean.getUserId());
                    	    			 dip.setCreateDate(new Date());
                    	    			 dip.setDealerId(ap.getDealerId());
                    	    			 dip.setDiscountDate(new Date());
                    	    			 dip.setDeductReson(partCode+"连带抵扣");
                    	    			 dip.setDiscountPriec(0.0);
                    	    			 dip.setDiscountSum(Integer.parseInt(String.valueOf(pList.get(p).get("QUANTITY"))));
                    	    			 dip.setDiscount(0.0);
                    	    			 dip.setDownPartCode(pList.get(p).get("DOWN_PART_CODE"));
                    	    			 dip.setDownPartName(pList.get(p).get("DOWN_PART_NAME"));
                    	    			 //查询供应商ID
                    	    			 dip.setDownProductCode(pList.get(p).get("DOWN_PRODUCT_CODE"));
                    	    			 dip.setDownProductName(pList.get(p).get("DOWN_PRODUCT_NAME"));
                    	    			 dip.setId(Long.valueOf(SequenceManager.getSequence("")));
                    	    			 //查询工时
                    	    			 dip.setWrLabourcode(list.get(l).get("WR_LABOURCODE"));
                    	    			 dip.setWrLabourname(list.get(l).get("WR_LABOURNAME"));
                    	    			 dip.setLabourPrice(0.0);
                    	    			 this.insert(dip);
        							 }
        						 }
        					 }
        				 }
    				 }else{
    					 ddp.setDiscountPriec(Double.valueOf(String.valueOf(sList.get(0).get("PRICE"))));
        				 ddp.setDiscountSum(Integer.parseInt(allAmount));
        				 ddp.setDiscount(Double.valueOf(String.valueOf(sList.get(0).get("AMOUNT"))));
        				 ddp.setLabourPrice(Double.valueOf(String.valueOf(sList.get(0).get("LABOUR_AMOUNT"))));
    					 if(list !=null && list.size()>0){//如果有数据结果集
        					 for(int l =0; l<list.size();l++){//循环处理
        						 /**
        						  * 第一步：由于索赔单已经结算了,所以现在只需要将相关抵扣信息保存明细
        						  * 第二步：将抵扣的旧件设置已经出库状态
        						  */
        						 String ol = "UPDATE Tt_As_Wr_Old_Returned_Detail d SET d.is_out=1,d.is_cliam=1  WHERE d.claim_id ="+ap.getId()+" AND d.part_id IN (SELECT p.part_id FROM Tt_As_Wr_Partsitem p WHERE p.ID="+ap.getId()+"  AND d.is_out in(0,2) AND p.wr_labourcode='"+list.get(l).get("WR_LABOURCODE")+"' AND  p.part_code NOT IN('"+partCode+"'))";
        						 this.update(ol, null);
        						 
        						 String ppl = "select P.PART_ID,P.QUANTITY,P.AMOUNT,P.PRICE,P.DOWN_PART_CODE,P.DOWN_PART_NAME,P.DOWN_PRODUCT_CODE,P.DOWN_PRODUCT_NAME from Tt_As_Wr_Partsitem p where p.id="+ap.getId()+" and p.wr_labourcode='"+list.get(l).get("WR_LABOURCODE")+"' AND  p.part_code NOT IN('"+partCode+"')";
        						 List<Map<String,String>> pList = this.pageQuery(ppl.toString(), null, getFunName());
        						 if(pList!=null && pList.size()>0){//该工时下面的所有配件进行抵扣明细保存
        							 for(int p=0;p<pList.size();p++){
        								 //保存抵扣明细
                						 TtAsWrDiscountPO dip = new TtAsWrDiscountPO();
                    	    			 dip.setClaimId(ap.getId());
                    	    			 dip.setClaimNo(ap.getClaimNo());
                    	    			 dip.setCreateBy(logonUserBean.getUserId());
                    	    			 dip.setCreateDate(new Date());
                    	    			 dip.setDealerId(ap.getDealerId());
                    	    			 dip.setDiscountDate(new Date());
                    	    			 dip.setDeductReson(partCode+"连带抵扣");
                        	    		 dip.setDiscountPriec(Double.valueOf(String.valueOf(pList.get(p).get("PRICE"))));
                    	    			 dip.setDiscountSum(Integer.parseInt(String.valueOf(pList.get(p).get("QUANTITY"))));
                    	    			 dip.setDiscount(Double.valueOf(String.valueOf(pList.get(p).get("AMOUNT"))));
                    	    			 dip.setDownPartCode(pList.get(p).get("DOWN_PART_CODE").toString());
                    	    			 dip.setDownPartName(pList.get(p).get("DOWN_PART_NAME").toString());
                    	    			 //查询供应商ID
                    	    			 dip.setDownProductCode(pList.get(p).get("DOWN_PRODUCT_CODE").toString());
                    	    			 dip.setDownProductName(pList.get(p).get("DOWN_PRODUCT_NAME").toString());
                    	    			 dip.setId(Long.valueOf(SequenceManager.getSequence("")));
                    	    			 //查询工时
                    	    			 dip.setWrLabourcode(list.get(l).get("WR_LABOURCODE"));
                    	    			 dip.setWrLabourname(list.get(l).get("WR_LABOURNAME"));
                    	    			 dip.setLabourPrice(Double.valueOf(String.valueOf(list.get(l).get("LABOUR_AMOUNT"))));
                    	    			 this.insert(dip);
        							 }
        							 }
        						 }
        					 }
    					 }
    				 this.insert(ddp);
    				 }
    			 	/**
	    			 * 修改旧件回运明细的旧件状态
	    			 */
	    			StringBuffer sql = new StringBuffer();
	    			sql.append(" update tt_as_wr_old_returned_detail d set d.is_out=1,d.is_cliam='1' WHERE d.claim_no='"+claimNo+"' AND d.is_out in(0,2) AND d.part_code='"+partCode+"' AND d.sign_amount>0");
	    			this.update(sql.toString(), null);
	    			/**
	    			 * 如果索赔单已经枷锁，则将其释放掉
	    			 */
	    			 StringBuffer sqlStr2 = new StringBuffer();
	    			 sqlStr2.append("update tt_as_wr_application t set t.verseon=null where t.claim_no='"+claimNo+"'\n");
	    			 this.update(sqlStr2.toString(), null);
    			 }else if("0".equalsIgnoreCase(partReturn)&& !Constant.OUT_CLAIM_TYPE_03.toString().equals(outType)){//如果选择的是不返件或者是维修的索赔单,并且出库类型选择的不是 误判
    				 this.insert(pp); //执行主因件出库明细插入
    				 
    				 //更新索赔单配件字表
    				 StringBuffer sql = new StringBuffer();
    	    			sql.append(" update TT_AS_WR_PARTSITEM p set p.is_notice="+Constant.IF_TYPE_YES+" ,p.Notice_Date=sysdate WHERE  p.ID IN (SELECT ID FROM Tt_As_Wr_Application a WHERE a.claim_no='"+claimNo.trim()+"') AND p.down_part_code = '"+partCode.trim().toUpperCase()+"'");
    	    			this.update(sql.toString(), null);
    	    			List<Map<String,Object>> list = this.getpartList(partCode, claimNo);
    	    			if(list!=null && list.size()>0){//如果查询结果集有数据,说明该件下含有次因件，此时就将这些次因件也出库，并且插入到出库明细中
    	    				for(int k=0;k<list.size();k++){
    	    					StringBuffer sqlr = new StringBuffer();//更新次因件的出库状态
    	    					sqlr.append(" update TT_AS_WR_PARTSITEM p set p.is_notice="+Constant.IF_TYPE_YES+" ,p.Notice_Date=sysdate where p.part_id="+list.get(k).get("PART_ID").toString());
    	    	    			this.update(sqlr.toString(), null);
    	    	    			//开始保存次因件的出库记录
    	    	    			TtAsWrOldOutPartPO pp2 = new TtAsWrOldOutPartPO();
    	    	    			pp2.setClaimNo(list.get(k).get("CLAIM_NO").toString());
    	    	    			pp2.setCreateBy(logonUserBean.getUserId());
    	    	    			pp2.setCreateDate( new Date());
    	    	    			pp2.setId(Long.parseLong(SequenceManager.getSequence("")));
    	    	    			pp2.setOutAmout(Integer.parseInt(list.get(k).get("AMOUNT").toString()));
    	    	    			pp2.setOutBy(logonUserBean.getUserId());
    	    	    			pp2.setOutType(Integer.parseInt(outType));
    	    	    			pp2.setOutDate( new Date());
    	    	    			pp2.setOutNo(rangeNo);
    	    	    			pp2.setOutPartCode(list.get(k).get("PART_CODE").toString());
    	    	    			pp2.setOutPartName(list.get(k).get("PART_NAME").toString());
    	    	    			pp2.setRemark(partCode+"责任连带");
    	    	    			pp2.setYieldly(Integer.parseInt(yieldly));
    	    	    			pp2.setOutPartType(Integer.parseInt(partReturn));
    	    	    			pp2.setRelationalOutNo(relationOutNo);
    	    	    			pp2.setRangeNo(rangeNo);
    	    	    			this.insert(pp2); 
    	    				}
    	    			}
    	    			/**
    	    			 * wizard_lee 2014-12-25
    	    			 * 如果是切换件以无件的形式出库，则更新出库标识为部分出库
    	    			 */
    	    			if(outFlag==1){
	    	    			StringBuffer sql1 = new StringBuffer();
	    	    			//切换件无件出库的时候is_out和is_cliam设置为中间状态2
	    	    			sql1.append(" update tt_as_wr_old_returned_detail d set d.is_out=2,d.is_cliam='2' WHERE d.claim_no='"+claimNo+"' AND d.is_out in(0) AND d.part_code='"+partCode+"' AND d.sign_amount>0");
	    	    			this.update(sql1.toString(), null);
    	    			}
    				}else if ("2".equalsIgnoreCase(partReturn)&& !Constant.OUT_CLAIM_TYPE_03.toString().equals(outType)){//如果是特殊费用,更新特殊费用相关数据
    					 this.insert(pp); //执行主因件出库明细插入
    					String sql = "UPDATE Tt_As_Wr_Spefee s SET s.Is_Notice = "+Constant.IF_TYPE_YES+" ,s.notice_date=SYSDATE WHERE s.fee_no='"+claimNo+"'";
    					this.update(sql, null);
    				}
			}
    		callRangeSingele(logonUserBean, rangeNo, outType);
    		}
    	}
	/**
	 * Function：全部出库操作
	 *           修改旧件库存表信息，新增出库记录表
	 * @param  ：	
	 * @return:		@param request 
	 * @throw：	
	 * LastUpdate：	2010-6-18
	 */
	@SuppressWarnings("unchecked")
	public int saveAllOutOfStoreLogOper(RequestWrapper request,AclUserBean logonUserBean){
    	String idStr=request.getParamValue("idStr");
    	StringBuffer sqlStr=new StringBuffer();
    	int updateNum=0;
    	int outNum=0;
    	if(idStr!=null&&!"".equals(idStr)){
    		String[] arr=idStr.split(",");
    		for(int count=0;count<arr.length;count++){
    			sqlStr.delete(0, sqlStr.length());
    			sqlStr.append("select * from TT_AS_WR_PART_STOCK where id="+arr[count]);
    			PageResult<TtAsWrPartStockPO> pr=pageQuery(TtAsWrPartStockPO.class, sqlStr.toString(), null, 10, 1);
    			if(pr!=null&&pr.getTotalRecords()>0){
    				//插入出库记录表
    				TtAsWrPartStockPO existOutInfo=(TtAsWrPartStockPO)pr.getRecords().get(0);
    				outNum=existOutInfo.getReturnAmount()-existOutInfo.getOutAmount();
    				TtAsWrPartStockLogPO insertObj=new TtAsWrPartStockLogPO();
    				insertObj.setId(Long.parseLong(SequenceManager.getSequence("")));
    				insertObj.setProducerId(existOutInfo.getProducerId());
    				insertObj.setPartId(existOutInfo.getPartId());
    				insertObj.setReturnAmount(existOutInfo.getReturnAmount());
    				insertObj.setOutBeforeAmount(outNum);
    				insertObj.setOutAmount(outNum);
    				insertObj.setOutDate(new Date());
    				insertObj.setCreateDate(new Date());
    				insertObj.setCreateBy(logonUserBean.getUserId());
    				insert(insertObj);
    				//修改旧件库存表信息
    				TtAsWrPartStockPO idObj=new TtAsWrPartStockPO();
    				idObj.setId(existOutInfo.getId());
    				TtAsWrPartStockPO vo=new TtAsWrPartStockPO();
    				vo.setOutAmount(existOutInfo.getOutAmount()+outNum);
    				vo.setUpdateBy(logonUserBean.getUserId());
    				vo.setUpdateDate(new Date());
    				updateNum+=update(idObj, vo);
    			}
    		}
    	}
    	return updateNum;
    }
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	//生成出库单号(退赔单号需改动时间值“每月的20号到次月的19号”为此单号的排列顺序。如不能屏蔽单号乱窜，则单号的编号建议为“1501000001”共10位。)
	@SuppressWarnings("unchecked")
	public String GenerateStockNo(){
		String sql="select TO_CHAR(SYSDATE, 'YYYYMM')||LPAD(seq_c.NEXTVAL,6,'0')  as num  From DUAL";
		List<Map<String,Object>> ps=this.pageQuery(sql.toString(), null, this.getFunName());
		return ps.get(0).get("NUM").toString();
	}
	@SuppressWarnings("unchecked")
	public String GenerateStockNo2(){
		String sql="select TO_CHAR(SYSDATE, 'YYMM') as num  From DUAL";
		List<Map<String,Object>> ps=this.pageQuery(sql.toString(), null, this.getFunName());
		return ps.get(0).get("NUM").toString();
	}
	//查询抵扣金额
	@SuppressWarnings("unchecked")
	public Double getDeductAmount(String barNo){
		StringBuffer sql = new StringBuffer();
		sql.append("select  round(nvl(p.amount,0)/nvl(p.quantity,0),2) price from Tt_As_Wr_Partsitem p ,Tt_As_Wr_Partsitem_Barcode b\n");
		sql.append("where b.barcode_no='"+barNo+"' and p.part_id = b.part_id"); 
		List<Map<String,Object>> ps=this.pageQuery(sql.toString(), null, this.getFunName());
		return Double.valueOf(ps.get(0).get("PRICE").toString());
	}
	@SuppressWarnings("unchecked")
	public String getDeductLabour(String barNo){
		StringBuffer sql = new StringBuffer();
		sql.append("select  p.wr_labourcode labour from Tt_As_Wr_Partsitem p ,Tt_As_Wr_Partsitem_Barcode b\n");
		sql.append("where b.barcode_no='"+barNo+"' and p.part_id = b.part_id"); 
		List<Map<String,Object>> ps=this.pageQuery(sql.toString(), null, this.getFunName());
		return ps.get(0).get("LABOUR").toString();
	}
	/**
	 * 得到旧件条码
	 * @param params
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ClaimOldPartOutPreListBean> getOldPartBarnoList(Map params){
//		String company_id=ClaimTools.dealParamStr(params.get("company_id"));
		String supply_name=ClaimTools.dealParamStr(params.get("supply_name"));//查询条件--供应商简称
		String part_code=ClaimTools.dealParamStr(params.get("part_code"));//查询条件--配件代码
		String supplyCode=ClaimTools.dealParamStr(params.get("supplyCode"));//查询条件--供应商代码
		String claimNo = ClaimTools.dealParamStr(params.get("claimNo"));
		String model_name = ClaimTools.dealParamStr(params.get("model_name"));
		String dealer_code = ClaimTools.dealParamStr(params.get("dealer_code"));
		StringBuffer sql=new StringBuffer();
		sql.append("select d.claim_no,ps.Maker_Code supplier_code, ps.Maker_Name supplier_name,b.part_code,d.barcode_no,B.PART_NAME \n");
		sql.append("  from tt_as_wr_old_returned_detail d,TT_PART_MAKER_DEFINE ps,Tm_Pt_Part_Base b,tt_as_wr_old_returned r ,Tt_As_Wr_Application a,tm_dealer  td\n");
		sql.append("  where  1=1 and r.id = d.return_id and td.dealer_id = a.dealer_id and td.dealer_id = r.dealer_id \n");
		sql.append("and a.claim_no = d.claim_no\n");
		sql.append(" and a.create_date>=to_date('2013-08-26','yyyy-mm-dd')\n"); 
		sql.append("  and d.producer_code = ps.Maker_Code(+)\n");
		sql.append("  and d.part_code = b.part_code(+)\n");
		sql.append("  and b.is_cliam="+Constant.IS_CLAIM_01);
		sql.append("  and (r.status="+Constant.BACK_LIST_STATUS_05+" or (r.status="+Constant.BACK_LIST_STATUS_04+" and d.is_in_house="+Constant.IF_TYPE_YES+"))\n");
		sql.append("  and b.is_del=0 and d.sign_amount>0\n" );
		sql.append("  and d.is_out=0\n" );
		sql.append("  and (d.is_cliam =0 or d.is_cliam is null )\n");
		if(Utility.testString(supply_name)){
			sql.append("  and ps.Maker_Name like '%"+supply_name+"%'\n");
		}
		if(Utility.testString(model_name)){
			sql.append("  and a.model_code like '%"+model_name.toUpperCase()+"%'\n");
		}
		if(Utility.testString(claimNo)){
			sql.append(" and  d.claim_no like '%"+claimNo.toUpperCase()+"%'\n");
		}
		if(Utility.testString(dealer_code)){
			sql.append(" and  td.dealer_code like '%"+dealer_code.toUpperCase()+"%'\n");
		}
		if(Utility.testString(part_code)){
			sql.append("  and b.part_code ='"+part_code.toUpperCase()+"'\n");
		}
		if(Utility.testString(supplyCode)){
			sql.append("  and ps.Maker_Code like '%"+supplyCode.toUpperCase()+"%'\n");
		}
		sql.append(" order by d.barcode_no" );
		List<ClaimOldPartOutPreListBean> pr= this.select(ClaimOldPartOutPreListBean.class, sql.toString(), null);
		return pr;
	}
	
	//出库明细索赔单明细
	@SuppressWarnings("unchecked")
	public List<TtAsWrOldOutDetailBean> getClaimDetail(String outNo,String code,Long oemCompanyId,String partCode){
		StringBuffer sql = new StringBuffer();
		sql.append("select d.claim_no,d.part_code,d.part_name,dd.dealer_code,dd.dealer_name,sum(od.out_amount)out_amount,G.MODEL_NAME,\n");
		sql.append("       (select sum(dq.sign_amount) all_amount\n");
		sql.append("          from tt_as_wr_old_returned_detail dq,\n");
		sql.append("               TM_PT_SUPPLIER               ps,\n");
		sql.append("               Tm_Pt_Part_Base              b,\n");
		sql.append("               tt_as_wr_old_returned        rq\n");
		sql.append("         where 1 = 1 and rq.id = dq.return_id\n");
		sql.append("           and dq.producer_code = ps.supplier_code(+)\n");
		sql.append("           and dq.part_code = b.part_code(+)\n");
		sql.append("           and b.is_cliam="+Constant.IS_CLAIM_01);
		sql.append("           and rq.status="+Constant.BACK_LIST_STATUS_05);
		sql.append("           and b.is_del = 0\n");
		sql.append("           and dq.sign_amount > 0\n");
		sql.append("           AND Dq.PART_CODE =d.part_code\n");
		sql.append("           and dq.claim_no = d.claim_no\n");
		sql.append("           ) all_amount\n");
		sql.append("  from tt_as_wr_old_returned_detail d,\n");
		sql.append("       tt_as_wr_old_returned        r,\n");
		sql.append("       tm_dealer                    dd,\n");
		sql.append("       TT_AS_WR_OLD_OUT_DETAIL      OD,\n");
		sql.append("       TM_VEHICLE                   V,\n");
		sql.append("       VW_MATERIAL_GROUP            G\n");
		sql.append(" where r.id = d.return_id\n");
		sql.append("   AND V.VIN = D.VIN\n");
		sql.append("   AND G.PACKAGE_ID = V.PACKAGE_ID\n");
		sql.append("   and dd.dealer_id = r.dealer_id\n");
		sql.append("   and d.part_code = '"+partCode+"'\n");
		sql.append("   AND D.SIGN_AMOUNT > 0\n");
		sql.append("   and r.status="+Constant.BACK_LIST_STATUS_05);
		sql.append("   and OD.OUT_NO = '"+outNo+"'\n");
		sql.append("   AND OD.SUPPLAY_CODE = '"+code+"'\n");
		sql.append("   and od.out_part_code = '"+partCode.toUpperCase()+"'\n");
		sql.append("   and d.barcode_no = od.barcode_no\n");
		sql.append(" group by d.claim_no,d.part_code,dd.dealer_code,dd.dealer_name,\n");
		sql.append("          G.MODEL_NAME,d.part_name"); 

		return this.select(TtAsWrOldOutDetailBean.class, sql.toString(), null);
	}
	//查询打印明细的基础数据
	@SuppressWarnings("unchecked")
	public List<TtAsWrOldOutDetailBean> getBaseBean(String outNo,String code,Long oemCompanyId,String yieldlys){
		StringBuffer sql = new StringBuffer();
		sql.append("select d.out_no,d.out_serial,c.code_desc out_type_name,d.out_part_type,\n");
		sql.append("    (select to_char(sysdate,'yyyy-mm-dd') from dual ) out_time,\n");
		sql.append("       b.Maker_Name||'*'||b.Maker_Code supplay_Name,\n");
		sql.append("       b.Maker_Code supplay_code,b.Tel\n");
		sql.append("  from tt_as_wr_old_out_detail d left join TT_PART_MAKER_DEFINE b on b.Maker_Code=d.supplay_code  \n"); 
		sql.append("	left join tc_code c on c.code_id = d.out_type \n"); 
		sql.append("where 1=1\n");
		sql.append("and d.out_serial='"+outNo+"'\n");
		sql.append("and d.supplay_code='"+code+"'\n");
		sql.append("    group by d.out_no,d.out_serial,b.Maker_Name,b.Maker_Code ,d.out_part_type,b.tel,c.code_desc "); 
		return this.select(TtAsWrOldOutDetailBean.class, sql.toString(), null);
	}
	
	//查询生成通知单的基础数据
	@SuppressWarnings("unchecked")
	public List<TtAsWrOldOutNoticePO> getBaseBeanForNotice(String outNo,String code,Long oemCompanyId){
		StringBuffer sql = new StringBuffer();
		sql.append("select  d.notice_no ,d.out_company notice_company,d.OUT_COMPANY_CODE  ,d.out_company_tel notice_Company_By_Tel\n");
		sql.append("from tt_as_wr_old_out_door d\n");
		sql.append("where  \n");
		if(Utility.testString(outNo)){
			sql.append(" d.OUT_NO='"+outNo+"'\n"); 
		}
		if(Utility.testString(code)){
			sql.append(" and  d.OUT_COMPANY_CODE='"+code+"'\n"); 
		}
		return this.select(TtAsWrOldOutNoticePO.class, sql.toString(), null);
	}
	@SuppressWarnings("unchecked")
	public List<TtAsWrOldOutDetailBean> getPrintDetail(String outNo,String code,Long oemCompanyId,String yieldlys){
		StringBuffer sql = new StringBuffer();
		sql.append("select  da.part_code out_part_code,da.part_name,cl.part_oldcode part_code,\n");
		sql.append("        sum(da.out_amount) out_amount ,da.model_name,nvl(pl.labour_hours,0)labour_hours,\n");
		sql.append("       sum( da.out_amount* nvl(pl.labour_hours,0)*nvl(mp.model_price,0)) LABOUR_AMOUNT,\n");
		sql.append("       cl.claim_price,da.out_part_type\n");
		sql.append(" from (\n");
		sql.append("select d.out_no ,d.out_company,d.out_company_code,dd.part_code,\n");
		sql.append("       dd.model_name,dd.part_name,dd.out_part_type,dd.out_num-dd.remand_num out_amount\n");
		sql.append("          from Tt_As_Wr_Old_Out_Door        d,  Tt_As_Wr_Old_Out_Door_Detail dd\n");
		sql.append("         where  d.door_id = dd.door_id  and D.OUT_NO = '"+outNo+"'\n");
		sql.append("           AND D.out_company_code = '"+code+"' ) da\n");
		sql.append("    left join (select mr.claim_price, pd.part_oldcode, md.maker_code\n");
		sql.append("    from tt_part_maker_define   md, tt_part_maker_relation mr,  tt_part_define         pd\n");
		sql.append("              where md.maker_id = mr.maker_id   and mr.part_id = pd.part_id\n");
		sql.append("                and mr.is_default = "+Constant.IF_TYPE_YES+") cl on cl.part_oldcode = da.part_code and cl.maker_code = da.out_company_code\n");
		sql.append("     left join (\n");
		sql.append("          select pl.part_code,pl.part_name,pl.labour_code,pl.labour_name,pl.labour_price,pl.labour_hours\n");
		sql.append("           from tt_as_wr_old_part_labour pl\n");
		sql.append("     ) pl on pl.part_code = da.part_code\n");
		sql.append("	left join tt_as_wr_model_price mp on mp.model_code=da.model_name\n"); 

		sql.append("     group by  da.part_code,da.part_name,cl.part_oldcode,da.model_name,pl.labour_hours,da.out_part_type,cl.claim_price"); 

		return this.select(TtAsWrOldOutDetailBean.class, sql.toString(), null);
	}
	
	//打印通知单明细
	//查询打印明细的基础数据
	@SuppressWarnings("unchecked")
	public List<TtAsWrOldOutNoticeBean> getBaseBeanForNoticeOut(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT N.*,to_char(sysdate,'yyyy')||'年'||to_char(sysdate,'MM')||'月'||to_char(sysdate,'dd')||'日' out_time\n");
		sql.append(" FROM TT_AS_WR_OLD_OUT_NOTICE N\n");
		sql.append(" WHERE N.notice_id ='"+id+"'\n");

		return this.select(TtAsWrOldOutNoticeBean.class, sql.toString(), null);
	}
	@SuppressWarnings("unchecked")
	public List<TtAsWrOldOutNoticeBean> getBaseBeanForNoticeAppOut(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT N.*,to_char(sysdate,'yyyy')||'年'||to_char(sysdate,'MM')||'月'||to_char(sysdate,'dd')||'日' out_time\n");
		sql.append(" FROM TT_AS_WR_OLD_OUT_NOTICE N\n");
		sql.append(" WHERE N.SPEFEE_ID ='"+id+"'\n");
		return this.select(TtAsWrOldOutNoticeBean.class, sql.toString(), null);
	}
	@SuppressWarnings("unchecked")
	public List<TtAsWrOldOutNoticeDetailPO> getPrintDetailOut(String id,Long oemCompanyId,String yieldlys){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT nd.*\n");
		sql.append(" FROM TT_AS_WR_OLD_OUT_NOTICE N ,tt_as_wr_old_out_notice_detail nd\n");
		sql.append(" WHERE n.notice_id = nd.notice_id\n");
		sql.append(" AND N.NOTICE_id='"+id+"'\n");
		return this.select(TtAsWrOldOutNoticeDetailPO.class, sql.toString(), null);
	}
	@SuppressWarnings("unchecked")
	public List<TtAsWrOldOutDoorBean> getPrintBaseBean(String outNo,String code){
		StringBuffer sql = new StringBuffer();
		sql.append("select d.* ,(select to_char(sysdate,'yyyy-mm-dd') from dual )  out_time\n");
		sql.append("from tt_as_wr_old_out_door d\n");
		sql.append("where d.out_no='"+outNo+"'\n");
		sql.append("and d.out_company_code='"+code+"'\n"); 
		return this.select(TtAsWrOldOutDoorBean.class, sql.toString(), null);
	}
	@SuppressWarnings("unchecked")
	public List<TtAsWrOldOutDoorDetailPO> getPrintList(String outNo,String code){
		StringBuffer sql = new StringBuffer();
		sql.append(" select  d.part_name,d.model_name, sum(d.out_num) out_num,d.out_remark ,d.part_code\n");
		sql.append("from tt_as_wr_old_out_door_detail d\n");
		sql.append("where d.door_id =\n");
		sql.append(" (select door_id from tt_as_wr_old_out_door od where od.out_no='"+outNo+"' and od.out_company_code='"+code+"')   group by  d.part_name,d.model_name,d.out_remark,d.part_code"); 
		return this.select(TtAsWrOldOutDoorDetailPO.class, sql.toString(), null);
	}
	//主因件索赔查询主数据
	@SuppressWarnings("unchecked")
	public PageResult<ClaimOldPartOutPreListBean> mainPartClaimQuery(Map params,int curPage, int pageSize){
		String supply_name=ClaimTools.dealParamStr(params.get("supply_name"));//查询条件--供应商简称
		String supply_code=ClaimTools.dealParamStr(params.get("supply_code"));//查询条件--供应商代码
		String yieldly = ClaimTools.dealParamStr(params.get("yieldly"));
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT  p.down_product_code supply_code,p.down_product_name supply_name,sum(1) all_amount \n");
		sql.append("  FROM TT_AS_WR_APPLICATION A,TT_AS_WR_PARTSITEM P,TM_PT_PART_BASE PB\n");
		sql.append("  WHERE 1=1\n");
		sql.append("  AND A.ID = P.ID\n");
		sql.append("  AND P.DOWN_PART_CODE = PB.PART_CODE \n");
		sql.append("     and pb.is_cliam="+Constant.IS_CLAIM_01+"\n");
		sql.append("	   and p.responsibility_type="+Constant.RESPONS_NATURE_STATUS_01+"\n");
		sql.append("  AND PB.PART_IS_CHANGHE = "+yieldly+"\n");
		sql.append("  AND A.STATUS !="+Constant.CLAIM_APPLY_ORD_TYPE_01+"\n");
		if(Utility.testString(supply_name)){
			sql.append("     and  pb.down_product_name like '%"+supply_name+"%'\n");
		}
		if(Utility.testString(supply_code)){
			sql.append("     and  pb.down_product_code like '%"+supply_code.toUpperCase()+"%'\n");
		}
	
		sql.append("group by p.down_product_code,p.down_product_name "); 


		PageResult<ClaimOldPartOutPreListBean> pr=pageQuery(ClaimOldPartOutPreListBean.class, sql.toString(), null, pageSize, curPage);
		return pr;
	}
	//主因件索赔查询明细
	@SuppressWarnings("unchecked")
	public PageResult <ClaimOldPartOutPreListBean> mainPartClaimDetail(String code ,String yieldly,String partName,String isPrint,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT p.down_product_code supply_code,p.down_product_name supply_name,p.part_code,p.part_name ,a.vin ,a.claim_no, p.WR_LABOURCODE wr_labourcode,p.IS_OLD_CLAIM_PRINT is_print \n");
		sql.append("  FROM TT_AS_WR_APPLICATION A,TT_AS_WR_PARTSITEM P,TM_PT_PART_BASE PB\n");
		sql.append("  WHERE 1=1\n");
		sql.append("  AND A.ID = P.ID\n");
		sql.append("  AND P.DOWN_PART_CODE = PB.PART_CODE  \n");
		sql.append("     and pb.is_cliam="+Constant.IS_CLAIM_01+"\n");
		sql.append("	   and p.responsibility_type="+Constant.RESPONS_NATURE_STATUS_01+"\n");
		sql.append("  AND PB.PART_IS_CHANGHE = "+yieldly+"\n");
		sql.append("  AND A.STATUS !="+Constant.CLAIM_APPLY_ORD_TYPE_01+"\n");
		if(Utility.testString(code)){
			sql.append("     and  p.down_product_code = '"+code.toUpperCase()+"'\n");
		}
		if(Utility.testString(partName)){
			sql.append("     and  p.part_name like '%"+partName+"%'\n");
		}
		if(Utility.testString(isPrint)){
			sql.append("     and  p.is_old_claim_print = '"+isPrint+"'\n");
		}
		sql.append("group by  p.down_product_code ,p.down_product_name ,p.part_code,p.part_name ,a.vin ,a.claim_no, p.WR_LABOURCODE,p.IS_OLD_CLAIM_PRINT"); 
		PageResult<ClaimOldPartOutPreListBean> pr=pageQuery(ClaimOldPartOutPreListBean.class, sql.toString(), null, pageSize, curPage);
		return pr;
	}
	@SuppressWarnings("unchecked")
	public List<TtAsWrMainPartClaimBean> getMainPartClaimBean(String claimNo,String code,String yieldly){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.claim_no,to_char(a.report_date,'yyyy-mm-dd hh24:mi') report_date,to_char(a.ACCOUNT_DATE,'yyyy-mm-dd hh24:mi') audit_date,\n");
		sql.append("       d.dealer_code,d.dealer_name,d.phone dealer_tel,a.model_code,decode(tc.ctm_name,null,'售前',to_char(v.purchased_date,'yyyy-mm-dd hh24:mi')) buy_date,\n");
		sql.append("       to_char(v.factory_date,'yyyy-mm-dd hh24:mi')out_date,v.mileage,tc.ctm_name owner_name,tc.main_phone owner_tel,v.vin,v.engine_no,\n");
		sql.append("       p.part_name,p.down_product_name supply_name, a.claim_type,p.remark question\n");
		sql.append("  FROM TT_AS_WR_APPLICATION A,TT_AS_WR_PARTSITEM P,TM_PT_PART_BASE PB,tm_dealer d ,tm_vehicle v\n");
		sql.append("  left join tt_dealer_actual_sales ass on ass.vehicle_id=v.vehicle_id  and ass.is_return="+Constant.IF_TYPE_NO+"\n");
		sql.append("  left join tt_customer tc   on tc.ctm_id=ass.ctm_id\n");
		sql.append("  WHERE 1=1\n");
		sql.append("  AND A.ID = P.ID\n");
		sql.append("  AND P.DOWN_PART_CODE = PB.PART_CODE\n");
		sql.append("  and a.dealer_id = d.dealer_id\n");
		sql.append("  and v.vin = a.vin\n");
		sql.append("     and pb.is_cliam="+Constant.IS_CLAIM_01+"\n");
		sql.append("    and p.responsibility_type="+Constant.RESPONS_NATURE_STATUS_01+"\n");
		sql.append("  AND PB.PART_IS_CHANGHE = "+yieldly+"\n");
		sql.append("  AND A.STATUS !="+Constant.CLAIM_APPLY_ORD_TYPE_01+"\n");
		if(Utility.testString(claimNo)){
			sql.append("and a.claim_no='"+claimNo+"'\n"); 
		}
		if(Utility.testString(code)){
			sql.append("and  p.wr_labourcode='"+code+"'\n"); 
		}
		return this.select(TtAsWrMainPartClaimBean.class, sql.toString(), null);
	}
	@SuppressWarnings("unchecked")
	public List<TtAsWrMainPartClaimBean> getMainPartClaimList(String claimNo,String code,String yieldly){
		StringBuffer sql = new StringBuffer();
		sql.append("select p.quantity part_num,p.part_code,p.part_name, decode(p.quantity,0,'维修','更换') part_type, round(p.quantity*p.price,2) part_price\n");
		sql.append(" from tt_as_wr_application a , tt_as_wr_partsitem p,tm_pt_part_base b\n");
		sql.append("where a.id = p.id\n");
		sql.append("and p.down_part_code = b.part_code\n");
		sql.append(" and b.is_cliam="+Constant.IS_CLAIM_01+"\n");
		sql.append("  AND b.PART_IS_CHANGHE = "+yieldly+"\n");
		sql.append("and p.responsibility_type="+Constant.RESPONS_NATURE_STATUS_02+"\n");
		sql.append("and a.claim_no='"+claimNo+"'\n"); 
		if(Utility.testString(code)){
			sql.append("and  p.wr_labourcode='"+code+"'\n"); 
		}
		return this.select(TtAsWrMainPartClaimBean.class, sql.toString(), null);
	}
	@SuppressWarnings("unchecked")
	public PageResult<TtAsWrOldOutNoticeBean> getNoticeInfo(Map params,int curPage, int pageSize){
		String supply_name=ClaimTools.dealParamStr(params.get("supply_name"));//查询条件--供应商简称
		String supply_code=ClaimTools.dealParamStr(params.get("supply_code"));//查询条件--供应商代码
//		String yieldly = ClaimTools.dealParamStr(params.get("yieldly"));
		String NOTICE_NO = ClaimTools.dealParamStr(params.get("NOTICE_NO"));
		StringBuffer sql= new StringBuffer();
		sql.append("select t.*,case  when to_char(t.create_date, 'yyyy-mm') =  to_char(sysdate, 'yyyy-mm') then   1  else   0   end del_flag from tt_as_wr_old_out_notice t\n");
		sql.append("where 1=1\n");
		
		if(Utility.testString(supply_code)){
			sql.append("and t.notice_code like '%"+supply_code+"%'\n");
		}
		
			sql.append(" and t.type="+Constant.OUT_PART_TYPE_03+"\n");
		if(Utility.testString(supply_name)){
			sql.append("     and  t.notice_company like '%"+supply_name+"%'\n");
		}
		if(Utility.testString(NOTICE_NO)){
			sql.append("     and  t.notice_no like '%"+NOTICE_NO+"%'\n");
		}
		PageResult<TtAsWrOldOutNoticeBean> pr=pageQuery(TtAsWrOldOutNoticeBean.class, sql.toString(), null, pageSize, curPage);
		return pr;
	}
	//无旧件索赔通知单查询
	@SuppressWarnings("unchecked")
	public PageResult<TtAsWrOldOutNoticeBean> getNoticeInfo2(Map params,int curPage, int pageSize){
		String supply_name=ClaimTools.dealParamStr(params.get("supply_name"));//查询条件--供应商简称
		String supply_code=ClaimTools.dealParamStr(params.get("supply_code"));//查询条件--供应商代码
		String yieldly = ClaimTools.dealParamStr(params.get("yieldly"));
		String NOTICE_NO = ClaimTools.dealParamStr(params.get("NOTICE_NO"));
		StringBuffer sql= new StringBuffer();
		sql.append("select t.* , case  when to_char(t.create_date, 'yyyy-mm') =  to_char(sysdate, 'yyyy-mm') then   1  else   0   end del_flag from tt_as_wr_old_out_notice t\n");
		sql.append("where 1=1\n");
		
		if(Utility.testString(supply_code)){
			sql.append("and t.notice_code like '%"+supply_code+"%'\n");
		}
		sql.append("and t.yieldly ="+yieldly); 

		sql.append(" and t.type="+Constant.OUT_PART_TYPE_04+"\n");
		if(Utility.testString(supply_name)){
			sql.append("     and  t.notice_company like '%"+supply_name+"%'\n");
		}
		if(Utility.testString(NOTICE_NO)){
			sql.append("     and  t.notice_no like '%"+NOTICE_NO+"%'\n");
		}
		PageResult<TtAsWrOldOutNoticeBean> pr=pageQuery(TtAsWrOldOutNoticeBean.class, sql.toString(), null, pageSize, curPage);
		return pr;
	}
	//特殊单查询
	@SuppressWarnings("unchecked")
	public PageResult <SpefeeBaseBean> getSpefeeList(String supply_name ,String spefee_no,String SUPPLY_CODE,String status,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("select  t.id,t.fee_no,t.vin,t.fee_type,t.status,t.part_code,t.part_name,m.maker_code supply_code,m.maker_name supply_name,\n");
		sql.append("d.dealer_code,d.dealer_shortname dealer_name, vw.MODEL_CODE,r.claim_price,m.tel\n");
		sql.append("from tt_as_wr_spefee t\n");
		sql.append("    left join tt_part_maker_define m on m.maker_code=t.supplier_code\n");
		sql.append("    left join tt_part_define pd on pd.part_oldcode = t.part_code\n");
		sql.append("    left join tt_part_maker_relation r on r.maker_id = m.maker_id and r.part_id=pd.part_id ,tm_dealer d ,tm_vehicle v\n");
		sql.append(" LEFT OUTER JOIN VW_MATERIAL_GROUP_service vw ON vw.package_id =\n");
		sql.append("                                                  v.package_id\n");
		sql.append("	where t.dealer_id = d.dealer_id\n");
		sql.append("	and t.vin = v.vin\n");
		sql.append("	and t.supplier_code is not null"); 
		sql.append(" 	and t.status !="+Constant.SPEFEE_STATUS_01+"\n");
		if(Utility.testString(supply_name)){
			sql.append("    and m.maker_name like '%"+supply_name+"%'\n");
		}
		if(Utility.testString(status)){
			sql.append("    and t.status = '"+status+"'\n");
		}
		if(Utility.testString(spefee_no)){
			sql.append("    and t.fee_no like '%"+spefee_no+"%'\n");
		}
		if(Utility.testString(SUPPLY_CODE)){
			sql.append("     and m.maker_code like '%"+SUPPLY_CODE+"%'\n");
		}
		sql.append("  and t.is_notice="+Constant.IF_TYPE_NO);
		PageResult<SpefeeBaseBean> pr=pageQuery(SpefeeBaseBean.class, sql.toString(), null, pageSize, curPage);
		return pr;
	}
	
	//	
	@SuppressWarnings("unchecked")
	public List<SpefeeBaseBean> getSpefeeBean(String id){
	StringBuffer sql = new StringBuffer();
	sql.append("select  t.id,t.fee_no,t.vin,t.fee_type,t.status,t.part_code,t.part_name,m.maker_code supply_code,m.maker_name supply_name,\n");
	sql.append("vw.MODEL_CODE,r.claim_price,m.tel\n");
	sql.append("from tt_as_wr_spefee t\n");
	sql.append("    left join tt_part_maker_define m on m.maker_code=t.supplier_code\n");
	sql.append("    left join tt_part_define pd on pd.part_oldcode = t.part_code\n");
	sql.append("    left join tt_part_maker_relation r on r.maker_id = m.maker_id and r.part_id=pd.part_id ,tm_vehicle v\n");
	sql.append(" LEFT OUTER JOIN VW_MATERIAL_GROUP_service vw ON vw.package_id =\n");
	sql.append("                                                  v.package_id\n");
	sql.append("where t.vin = v.vin\n");
	sql.append("and t.supplier_code is not null\n");
	sql.append("and t.id ="+id); 

	return this.select(SpefeeBaseBean.class, sql.toString(), null);
}
	/**
	 * 二次入库查询
	 * @param request
	 * @param currPage
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageResult <TtAsWrOldReturnedDetailPO> getoldPartList(RequestWrapper request, Integer currPage, Integer pageSize){
		String supply_name = DaoFactory.getParam(request,"supply_name");
		String supply_code = DaoFactory.getParam(request,"SUPPLY_CODE");
		String partCode = DaoFactory.getParam(request,"part_code");
		String partName = DaoFactory.getParam(request,"part_name");
		String claim_no = DaoFactory.getParam(request,"claim_no");
		String barcode_no = DaoFactory.getParam(request,"barcode_no");
		String yieldly = DaoFactory.getParam(request,"yieldly");
		String type = DaoFactory.getParam(request,"type");
		String isInvoice = DaoFactory.getParam(request,"isInvoice");
		String isMainCode = DaoFactory.getParam(request,"is_main_code");
		StringBuffer sql= new StringBuffer();
		sql.append("select d.id, d.part_code,d.part_name,d.is_main_code,d.producer_code,d.producer_name,d.claim_no,d.barcode_no,d.deduct_remark\n");
		sql.append(" from tt_as_wr_old_returned_detail d,tt_as_wr_old_returned r,Tt_As_Wr_Application a \n");
		sql.append(" where 1=1  and a.id=d.claim_id\n");
		sql.append(" and  d.is_cliam =0 and d.is_out =0\n");
		sql.append(" and d.return_id = r.id and r.status="+Constant.BACK_LIST_STATUS_07+"\n");
		if("reIn".equalsIgnoreCase(type)){
			sql.append(" and d.sign_amount=0"); 
		}else if("modify".equalsIgnoreCase(type)){
			sql.append(" and d.sign_amount=1"); 
		}
		DaoFactory.getsql(sql, "r.yieldly", yieldly, 1);
		DaoFactory.getsql(sql, "d.producer_name", supply_name, 2);
		DaoFactory.getsql(sql, "d.producer_code", supply_code, 2);
		DaoFactory.getsql(sql, "d.part_code", partCode, 2);
		DaoFactory.getsql(sql, "d.part_name", partName, 2);
		DaoFactory.getsql(sql, "d.claim_no", claim_no, 2);
		DaoFactory.getsql(sql, "d.barcode_no", barcode_no, 2);
		DaoFactory.getsql(sql, "a.is_invoice", isInvoice, 1);
		DaoFactory.getsql(sql, "d.is_main_code", isMainCode, 1);
		sql.append("  order by d.id desc "); 
		PageResult<TtAsWrOldReturnedDetailPO> pr=pageQuery(TtAsWrOldReturnedDetailPO.class, sql.toString(), null, pageSize, currPage);
		return pr;
	}
	/**
	 * 不包含入库时间条件的供应商修改查询
	 * @param yieldly
	 * @param supply_name
	 * @param supply_code
	 * @param partCode
	 * @param partName
	 * @param claim_no
	 * @param type
	 * @param dealerCode
	 * @param part_type
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @author chenyub@yonyou.com
	 * 2015年12月11日  下午5:19:37
	 */
	@SuppressWarnings("unchecked")
	public PageResult <TtAsWrOldReturnedDetailExtendPO> getoldPartList2(String yieldly,String supply_name,String supply_code,String partCode,
			String partName,String claim_no,String type,String dealerCode,String part_type, int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		if (BaseUtils.notNull(part_type)) {
			sql.append("select * from (");
		}
		sql.append("select d.part_code,d.part_name,d.producer_code,d.producer_name,d.claim_no,d.claim_id,td.dealer_shortname dealer_name,td.dealer_code,sum(d.sign_amount)sign_amount,\n");
		
		sql.append("decode((select count(1) from Tt_As_Wr_Application a where a.id=d.claim_id\n");
		sql.append("and a.campaign_code in (select aa.activity_code from  Tt_As_Activity aa\n");
		sql.append("where aa.activity_type=10561005)),1,'切换件',0,'常规件') as  part_type ");
		
		sql.append(" from tt_as_wr_old_returned_detail d,tt_as_wr_old_returned r,tm_dealer td\n");
		sql.append(" where 1=1 \n");
		sql.append(" and  d.is_cliam =0 and d.is_out =0\n");
		sql.append(" and d.return_id = r.id and (r.status="+Constant.BACK_LIST_STATUS_05+" or r.status="+Constant.BACK_LIST_STATUS_07+" or (r.status="+Constant.BACK_LIST_STATUS_04+" and d.is_in_house="+Constant.IF_TYPE_YES+"))\n");
			sql.append(" and d.sign_amount=1"); 
			sql.append(" and td.dealer_id = r.dealer_id");
			sql.append("  AND d.is_main_code="+Constant.RESPONS_NATURE_STATUS_01+" \n");
		if(Utility.testString(yieldly)){
			sql.append("     and r.yieldly = "+yieldly+"\n");
		}
		if(Utility.testString(supply_name)){
			sql.append("    and d.producer_name  like '%"+supply_name+"%'\n");
		}
		if(Utility.testString(supply_code)){
			sql.append("     and d.producer_code  like '%"+supply_code+"%'\n");
		}
		if(Utility.testString(partCode)){
			sql.append("    and d.part_code like '%"+partCode+"%'\n");
		}
		if(Utility.testString(partName)){
			sql.append("     and d.part_name like '%"+partName+"%'\n");
		}
		if(Utility.testString(claim_no)){
			sql.append("    and d.claim_no like '%"+claim_no+"%'\n");
		}
		if(Utility.testString(dealerCode)) {
			sql.append(" and td.dealer_code in ('"+ dealerCode.replace(",", "','") +"')\n");
		}
		DaoFactory.getsql(sql, "", "", 1);
		sql.append(" GROUP BY D.PART_CODE, D.PART_NAME, D.PRODUCER_CODE, D.PRODUCER_NAME, D.CLAIM_NO,d.claim_id, TD.DEALER_SHORTNAME, td.dealer_code\n");
		sql.append("  order by min(d.In_Date), D.CLAIM_NO  "); 
		if (BaseUtils.notNull(part_type)) {
			sql.append(" ) vv where  vv.part_type='"+part_type+"'");
		}
		PageResult<TtAsWrOldReturnedDetailExtendPO> pr=pageQuery(TtAsWrOldReturnedDetailExtendPO.class, sql.toString(), null, pageSize, curPage);
		return pr;
	}
	/**
	 * 包含入库时间条件的供应商修改查询
	 * @param yieldly
	 * @param supply_name
	 * @param supply_code
	 * @param partCode
	 * @param partName
	 * @param claim_no
	 * @param type
	 * @param dealerCode
	 * @param part_type
	 * @param indate_s
	 * @param indate_e
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @author chenyub@yonyou.com
	 * 2015年12月11日  下午5:19:14
	 */
	@SuppressWarnings("unchecked")
	public PageResult <TtAsWrOldReturnedDetailExtendPO> getoldPartList2Indate(String yieldly,String supply_name,String supply_code,String partCode,
			String partName,String claim_no,String type,String dealerCode,String part_type,String indate_s,String indate_e, int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		if (BaseUtils.notNull(part_type)) {
			sql.append("select * from (");
		}
		
		sql.append("select d.part_code,d.part_name,d.producer_code,d.producer_name,d.claim_no,d.claim_id,td.dealer_shortname dealer_name,td.dealer_code,sum(d.sign_amount)sign_amount,\n");
		
		sql.append("decode((select count(1) from Tt_As_Wr_Application a where a.id=d.claim_id\n");
		sql.append("and a.campaign_code in (select aa.activity_code from  Tt_As_Activity aa\n");
		sql.append("where aa.activity_type=10561005)),1,'切换件',0,'常规件') as  part_type ");
		
		sql.append(" from tt_as_wr_old_returned_detail d,tt_as_wr_old_returned r,tm_dealer td\n");
		sql.append(" where 1=1 \n");
		sql.append(" and  d.is_cliam =0 and d.is_out =0\n");
		sql.append(" and d.return_id = r.id and (r.status="+Constant.BACK_LIST_STATUS_05+" or r.status="+Constant.BACK_LIST_STATUS_07+" or (r.status="+Constant.BACK_LIST_STATUS_04+" and d.is_in_house="+Constant.IF_TYPE_YES+"))\n");
			sql.append(" and d.sign_amount=1"); 
			sql.append(" and td.dealer_id = r.dealer_id");
			sql.append("  AND d.is_main_code="+Constant.RESPONS_NATURE_STATUS_01+" \n");
		if(Utility.testString(yieldly)){
			sql.append("     and r.yieldly = "+yieldly+"\n");
		}
		if(Utility.testString(supply_name)){
			sql.append("    and d.producer_name  like '%"+supply_name+"%'\n");
		}
		if(Utility.testString(supply_code)){
			sql.append("     and d.producer_code  like '%"+supply_code+"%'\n");
		}
		if(Utility.testString(partCode)){
			sql.append("    and d.part_code like '%"+partCode+"%'\n");
		}
		if(Utility.testString(partName)){
			sql.append("     and d.part_name like '%"+partName+"%'\n");
		}
		if(Utility.testString(claim_no)){
			sql.append("    and d.claim_no like '%"+claim_no+"%'\n");
		}
		if(Utility.testString(dealerCode)) {
			sql.append(" and td.dealer_code in ('"+ dealerCode.replace(",", "','") +"')\n");
		}
		String dateMatch = "yyyy-mm-dd";
		if(StringUtils.isNotEmpty(indate_s)){
			sql.append(" and trunc(d.in_date)>=to_date('").append(indate_s).append("','").append(dateMatch).append("') ");
		}
		if(StringUtils.isNotEmpty(indate_e)){
			sql.append(" and trunc(d.in_date)>=to_date('").append(indate_e).append("','").append(dateMatch).append("') ");
		}
		DaoFactory.getsql(sql, "", "", 1);
		sql.append(" GROUP BY D.PART_CODE, D.PART_NAME, D.PRODUCER_CODE, D.PRODUCER_NAME, D.CLAIM_NO,d.claim_id, TD.DEALER_SHORTNAME, td.dealer_code\n");
		sql.append("  order by min(d.In_Date), D.CLAIM_NO  "); 
		if (BaseUtils.notNull(part_type)) {
			sql.append(" ) vv where  vv.part_type='"+part_type+"'");
		}
		PageResult<TtAsWrOldReturnedDetailExtendPO> pr=pageQuery(TtAsWrOldReturnedDetailExtendPO.class, sql.toString(), null, pageSize, curPage);
		return pr;
	}
	/**
	 * 供应商修改,无旧件或者不反运旧件查询
	 * @param partCode
	 * @param yieldly
	 * @param supplierCode
	 * @param supplierName
	 * @param count
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageResult <TtAsWrOldReturnedDetailExtendPO> getPartList(String yieldly,String supply_name,String supply_code,String partCode,
			String partName,String claim_no,String type,String dealerCode, int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT P.DOWN_PART_CODE PART_CODE,\n");
		sql.append("   P.DOWN_PART_NAME  PART_NAME,\n");
		sql.append("   P.DOWN_PRODUCT_CODE PRODUCER_CODE,\n");
		sql.append("  P.DOWN_PRODUCT_NAME PRODUCER_NAME,\n");
		sql.append("  A.CLAIM_NO,\n");
		sql.append("  A.ID CLAIM_ID,\n");
		sql.append("  D.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("  D.DEALER_CODE,\n");
		sql.append("  P.APPLY_QUANTITY sign_amount\n");
		sql.append(" FROM TT_AS_WR_PARTSITEM   P,\n");
		sql.append("  TM_PT_PART_BASE      B,\n");
		sql.append("  TT_AS_WR_APPLICATION A,\n");
		sql.append("  TM_DEALER            D\n");
		sql.append(" WHERE P.PART_CODE = B.PART_CODE(+)\n");
		sql.append("  AND A.ID = P.ID\n");
		sql.append("	AND p.is_notice="+Constant.IF_TYPE_NO+"\n");
		sql.append("  AND A.DEALER_ID = D.DEALER_ID\n");
		sql.append("  AND ((B.IS_RETURN = "+Constant.IS_RETURN_02+" AND B.IS_CLIAM = "+Constant.IS_CLAIM_01+" AND\n");
		sql.append("  P.PART_USE_TYPE = 1) OR\n");
		sql.append("  (P.PART_USE_TYPE = 0 AND B.IS_CLIAM = "+Constant.IS_CLAIM_01+" AND P.QUANTITY = 0))\n");
		sql.append("  AND P.DOWN_PART_CODE NOT IN ('00-000', 'NO_PARTS', '00-0000')\n");
		sql.append("  AND (A.URGENT = 0 OR A.URGENT = 2)\n");
		sql.append("  AND A.STATUS NOT IN ("+Constant.CLAIM_APPLY_ORD_TYPE_01+", "+Constant.CLAIM_APPLY_ORD_TYPE_03+", "+Constant.CLAIM_APPLY_ORD_TYPE_05+", "+Constant.CLAIM_APPLY_ORD_TYPE_06+")"); 
		
		if(Utility.testString(supply_name)){
			sql.append("    and P.DOWN_PRODUCT_NAME like '%"+supply_name+"%'\n");
		}
		if(Utility.testString(supply_code)){
			sql.append("     and upper(P.DOWN_PRODUCT_CODE)  like '%"+supply_code.toUpperCase()+"%'\n");
		}
		if(Utility.testString(partCode)){
			sql.append("    and upper(P.DOWN_PART_CODE) like '%"+partCode.toUpperCase()+"%'\n");
		}
		if(Utility.testString(partName)){
			sql.append("     and P.DOWN_PART_NAME like '%"+partName+"%'\n");
		}
		if(Utility.testString(claim_no)){
			sql.append("    and a.claim_no like '%"+claim_no+"%'\n");
		}
		if(Utility.testString(dealerCode)) {
			sql.append(" and d.dealer_code in ('"+ dealerCode.replace(",", "','") +"')\n");
		}
		PageResult<TtAsWrOldReturnedDetailExtendPO> pr=pageQuery(TtAsWrOldReturnedDetailExtendPO.class, sql.toString(), null, pageSize, curPage);
		return pr;
	}
	//查询供应商 zyw2015-3-18 
	@SuppressWarnings("unchecked")
	public PageResult<TmPtSupplierPO> querySupplier(String partCode,String partCodeTemp,String yieldly,String supplierCode,String supplierName,String count,int curPage,int pageSize) {
		StringBuffer sb= new StringBuffer();
		sb.append(" select c.* from ( select c.maker_id   as supplier_id,  c.maker_code supplier_code, c.maker_name supplier_name,\n" );
		sb.append(" (select count(1) from tt_part_maker_relation r where r.maker_id =c.maker_id and r.part_id=\n" );
		sb.append("       ( select pb.part_id\n" );
		sb.append("                  from tm_pt_part_base pb\n" );
		sb.append("                 where pb.part_is_changhe = 95411001 and pb.part_code='"+partCodeTemp+"')) as is_del\n ");
		sb.append("    from tt_part_maker_define  c \n");
		sb.append(" where 1=1\n");
		sb.append("and c.maker_id in (\n");
		sb.append("  select t.maker_id  from tt_part_maker_relation t  where t.part_id in (\n" );
		sb.append("	select pb.part_id  from tm_pt_part_base pb where pb.part_is_changhe="+yieldly+" \n" );
		if(Utility.testString(partCode)){
			if (Integer.valueOf(count)==1) {
				String partCode2 = "";
				String partCode3 = "";
				String partCode4 = "";
				if(partCode.length()==18){
					//17010510-C01-B00-3 18位的包含B00的件取17010510-C01-000-3的价格
					if("B00".equals(partCode.substring(13,16))){
						partCode2 = partCode.substring(0, 13)+"000"+partCode.substring(16, 18);
						partCode3 = partCode2;
						partCode4 = partCode2;
					}else{
						partCode2 = partCode.substring(0, partCode.length()-3)+"000";
						partCode3 = partCode.substring(0, partCode.length()-3)+"B00";
						partCode4 = partCode.substring(0, partCode.length()-3)+"B0Y";
					}
				}else{
					partCode2 = partCode.substring(0, partCode.length()-3)+"000";
					partCode3 = partCode.substring(0, partCode.length()-3)+"B00";
					partCode4 = partCode.substring(0, partCode.length()-3)+"B0Y";
				}
				sb.append("  and pb.part_code in ('"+partCode+"','"+partCode2+"','"+partCode3+"','"+partCode4+"')\n" );
			}
		}
		sb.append(")\n" );
		sb.append(")");
		DaoFactory.getsql(sb, "c.maker_code", supplierCode, 2);
		DaoFactory.getsql(sb, "c.maker_name", supplierName, 2);
		sb.append(") c order by  c.is_del desc");
		PageResult<TmPtSupplierPO> ps = pageQuery(TmPtSupplierPO.class,sb.toString(), null, pageSize, curPage);
		return ps;
	}
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> getNoPartStoreList(Map params,int curPage, int pageSize){
		String supply_name=ClaimTools.dealParamStr(params.get("supply_name"));//查询条件--供应商简称
		String supply_code=ClaimTools.dealParamStr(params.get("supply_code"));//查询条件--供应商简称
		String part_code=ClaimTools.dealParamStr(params.get("part_code"));//查询条件--配件代码
		String part_name=ClaimTools.dealParamStr(params.get("part_name"));//查询条件--配件名称
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT a.claim_no,p.down_part_code,p.down_part_name,p.part_id ID ,p.down_product_code,p.down_product_name,p.quantity\n");
		sql.append("  FROM TT_AS_WR_PARTSITEM   P,\n");
		sql.append("       TM_PT_PART_BASE      B,\n");
		sql.append("       TT_AS_WR_APPLICATION A\n");
		sql.append(" WHERE P.PART_CODE = B.PART_CODE(+)\n");
		sql.append("   AND A.ID = P.ID\n");
		sql.append("   AND B.IS_RETURN = "+Constant.IS_RETURN_02+"\n");
		sql.append("   AND B.IS_CLIAM = "+Constant.IS_CLAIM_01+"\n");
		sql.append("   AND P.DOWN_PART_CODE NOT IN ('00-000', 'NO_PARTS', '00-0000')\n");
		sql.append("   AND P.IS_NOTICE = "+Constant.IF_TYPE_NO+"\n");
		sql.append("   AND A.STATUS NOT IN ( "+Constant.CLAIM_APPLY_ORD_TYPE_01+","+Constant.CLAIM_APPLY_ORD_TYPE_03+","+Constant.CLAIM_APPLY_ORD_TYPE_06+")"); 
		sql.append("   AND  p.responsibility_type="+Constant.RESPONS_NATURE_STATUS_01+"\n");
		if(Utility.testString(supply_name)){
			sql.append("  and p.down_product_name like '%"+supply_name+"%'\n");
		}
		if(Utility.testString(supply_code)){
			sql.append("  and p.down_product_code like '%"+supply_code.toUpperCase()+"%'\n");
		}
		if(Utility.testString(part_code)){
			sql.append("  and p.down_part_code like '%"+part_code.toUpperCase()+"%'\n");
		}
		if(Utility.testString(part_name)){
			sql.append("  and p.down_part_name like '%"+part_name+"%'\n");
		}
		PageResult<Map<String,Object>> pr = this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage); 
		return pr;
	}
	//无旧件开票基础数据
	@SuppressWarnings("unchecked")
	public List<TtAsWrOldOutNoticePO> getBaseBeanForNotice(String supplyCode){
		StringBuffer sql = new StringBuffer();
		sql.append("select d.maker_name||'*'||d.maker_code out_company, d.maker_name notice_company,d.tel notice_Company_By_Tel\n");
		sql.append("from tt_part_maker_define d where d.maker_code = '"+supplyCode+"'"); 

		return this.select(TtAsWrOldOutNoticePO.class, sql.toString(), null);
	}
	@SuppressWarnings("unchecked")
	public List<TtAsWrOldOutDetailBean> getNoPartDetali(String partCode,String modelCode,String supplyCode,String yieldly){
		StringBuffer sql = new StringBuffer();
//		sql.append("select  b.part_code,b.part_name ,\n");
//		sql.append("        sum(p.quantity) out_amount,\n");
//		sql.append("        a.model_code model_name ,\n");
//		sql.append("        nvl(r.claim_price,0) claim_price,\n");
//		sql.append("        sum(l.labour_hours) labour_hours,\n");
//		sql.append("       sum(l.labour_amount) labour_amount\n");
//		sql.append("from Tt_As_Wr_Partsitem p,Tt_As_Wr_Labouritem l,\n");
//		sql.append("     tm_pt_part_base b ,Tt_As_Wr_Application a , tt_part_maker_relation     r\n");
//		sql.append("where p.down_part_code = b.part_code\n");
//		sql.append("and p.wr_labourcode = l.wr_labourcode\n");
//		sql.append("and p.id = l.id\n");
//		sql.append("and r.part_id = b.part_id\n");
//		sql.append("and a.id = p.id and a.id = l.id\n");
//		sql.append(" and b.is_cliam= "+Constant.IS_CLAIM_01+" and b.is_return ="+Constant.IS_RETURN_02+" \n");
//		sql.append("and r.is_default = "+Constant.IF_TYPE_YES+"\n");
//		sql.append(" and p.down_part_code not in ('00-000','NO_PARTS','00-0000')\n");
//		sql.append("and p.create_date>= to_date('2013-08-26','yyyy-mm-dd hh24:mi:ss')\n");
//		sql.append("and b.part_is_changhe="+yieldly+"\n");
//		sql.append("and  p.down_product_code = '"+supplyCode+"'\n");
//		sql.append("and b.part_code in ("+partCode+")"); 
//		sql.append(" and a.model_code in("+modelCode+")");
//		sql.append("group by b.part_code,b.part_name ,a.model_code,r.claim_price"); 
		
		sql.append("select  b.part_code,b.part_name ,r.claim_price,\n");
		sql.append("        1 out_amount,\n");
		sql.append("       0 labour_hours,\n");
		sql.append("       0 labour_amount\n");
		sql.append("from Tt_As_Wr_Partsitem p,Tt_As_Wr_Labouritem l,\n");
		sql.append("     tm_pt_part_base b ,Tt_As_Wr_Application a , tt_part_maker_relation     r\n");
		sql.append("where p.down_part_code = b.part_code\n");
		sql.append("and p.wr_labourcode = l.wr_labourcode\n");
		sql.append("and p.id = l.id\n");
		sql.append("and r.part_id = b.part_id\n");
		sql.append("and a.id = p.id and a.id = l.id\n");
		sql.append(" and b.is_cliam= "+Constant.IS_CLAIM_01+" and b.is_return ="+Constant.IS_RETURN_02+" \n");
		sql.append("and r.is_default = "+Constant.IF_TYPE_YES+"\n");
		sql.append(" and p.down_part_code not in ('00-000','NO_PARTS','00-0000')\n");
		sql.append("and p.create_date>= to_date('2013-08-26','yyyy-mm-dd hh24:mi:ss')\n");
		sql.append("and b.part_is_changhe="+yieldly+"\n");
		sql.append("and  p.down_product_code = '"+supplyCode+"'\n");
		sql.append("and b.part_code in ("+partCode+")"); 
		sql.append("group by b.part_code,b.part_name,r.claim_price"); 
		return this.select(TtAsWrOldOutDetailBean.class, sql.toString(), null);
	}
	//d得到旧件条码明细,为了更新状态
	@SuppressWarnings("unchecked")
	public List<TtAsWrPartsitemBarcodePO> getBarNo(String partCode,String supplyCode,String yieldly){
		StringBuffer sql = new StringBuffer();
		sql.append(" select pb.barcode_no \n");
		sql.append(" from tt_as_wr_partsitem p,tm_pt_part_base b ,tt_as_wr_partsitem_barcode pb ,tt_as_wr_application a,tt_as_wr_labouritem l,\n");
		sql.append(" tt_part_maker_relation r\n");
		sql.append(" where 1=1\n");
		sql.append(" and r.part_id = b.part_id\n");
		sql.append(" and a.id = p.id\n");
		sql.append(" and l.id = a.id\n");
		sql.append(" and p.wr_labourcode = l.wr_labourcode\n");
		sql.append(" and pb.part_id = p.part_id\n");
		sql.append("	and r.is_default="+Constant.IF_TYPE_YES); 
		sql.append(" and p.down_part_code = b.part_code\n");
		sql.append(" and b.part_is_changhe ="+yieldly+" and  pb.is_notice ="+Constant.IF_TYPE_NO);
		sql.append(" and b.is_cliam= "+Constant.IS_CLAIM_01+" and b.is_return ="+Constant.IS_RETURN_02+" and p.quantity>0\n");
		sql.append("  AND A.STATUS !="+Constant.CLAIM_APPLY_ORD_TYPE_01);
		sql.append("and p.down_product_code = '"+supplyCode+"'\n"); 
		sql.append("and p.down_part_code ='"+partCode+"'"); 
		return this.select(TtAsWrPartsitemBarcodePO.class, sql.toString(), null);
	}
	//新增出门证,查询出库明细
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getOutStoreList(Map params,int curPage, int pageSize){
		String supply_name=ClaimTools.dealParamStr(params.get("supply_name"));//查询条件--供应商简称
		String part_code=ClaimTools.dealParamStr(params.get("part_code"));//查询条件--配件代码
		String part_name=ClaimTools.dealParamStr(params.get("part_name"));//查询条件--配件名称
		String model_code=ClaimTools.dealParamStr(params.get("model_code"));//查询条件--车型代码
		String yieldly = ClaimTools.dealParamStr(params.get("yieldly"));
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT pd.part_id||a.model_code ID,PD.PART_OLDCODE PART_CODE,'"+model_code.toUpperCase()+"' model_code2, PD.PART_CNAME PART_NAME,D.SUPPLAY_CODE,D.SUPPLAY_NAME,A.MODEL_CODE,SUM(D.OUT_AMOUNT) OUT_TOTAL,D.OUT_PART_TYPE\n");
		sql.append("FROM TT_AS_WR_OLD_OUT_DETAIL  D\n");
		sql.append("LEFT JOIN TT_PART_DEFINE PD ON PD.PART_OLDCODE = D.OUT_PART_CODE,\n");
		sql.append(" TT_AS_WR_APPLICATION A\n");
		sql.append(" WHERE  D.CLAIM_NO = A.CLAIM_NO\n");
		sql.append(" AND A.CREATE_DATE>=TO_DATE('2013-08-26','YYYY-MM-DD')\n");
		sql.append("AND D.OUT_TYPE="+Constant.OUT_CLAIM_TYPE_01+"\n");
		sql.append(" and d.yieldly="+yieldly+"\n");
		sql.append(" and d.is_out_door =0 \n");
		
		if(Utility.testString(supply_name)){
			sql.append("  and d.SUPPLAY_NAME like '%"+supply_name+"%'\n");
		}
		if(Utility.testString(part_code)){
			sql.append("  and pd.PART_OLDCODE like '%"+part_code.toUpperCase()+"%'\n");
		}
		if(Utility.testString(part_name)){
			sql.append("  and pd.PART_CNAME like '%"+part_name+"%'\n");
		}
		if(Utility.testString(model_code)){
			sql.append("  and a.model_code like '%"+model_code.toUpperCase()+"%'\n");
		}
		sql.append("GROUP BY PD.PART_OLDCODE,PD.PART_CNAME,D.SUPPLAY_CODE,D.SUPPLAY_NAME,A.MODEL_CODE,D.OUT_PART_TYPE,pd.part_id"); 

		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>)pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		
		return ps;
	}
	// 出门证查询
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getOutDoorList(Map params,int curPage, int pageSize){
		String supply_name=ClaimTools.dealParamStr(params.get("supply_name"));//查询条件--供应商简称
		String supply_code=ClaimTools.dealParamStr(params.get("supply_code"));//查询条件--供应商代码
		String yieldly = ClaimTools.dealParamStr(params.get("yieldly"));
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT d.door_id id ,D.OUT_NO,U.NAME ,TO_CHAR(D.CREATE_DATE,'YYYY-MM-DD HH24:MI') OUT_TIME,substr(d.out_company, 1, instr(d.out_company, '*') -1) OUT_COMPANY,D.OUT_COMPANY_CODE,\n");
		sql.append("decode(n.out_no,null,0,1) del_flag,\n");
		sql.append("       case when to_char(d.create_date,'yyyy-mm') = to_char(sysdate,'yyyy-mm') then 1 else 0 end del_flag2\n"); 

		sql.append("FROM TT_AS_WR_OLD_OUT_DOOR D  left join Tt_As_Wr_Old_Out_Notice n  on n.out_no = d.out_no,TC_USER U\n");
		sql.append("WHERE U.USER_ID = D.CREATE_BY"); 

		if(Utility.testString(supply_name)){
			sql.append(" and d.OUT_COMPANY like '%"+supply_name+"%'\n");
		}
		if(Utility.testString(supply_code)){
			sql.append(" and d.OUT_COMPANY_CODE like '%"+supply_code.toUpperCase()+"%'\n");
		}
		sql.append(" and d.yieldly="+yieldly);
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>)pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPartList(Map params,int curPage, int pageSize){
		String partCode=ClaimTools.dealParamStr(params.get("partCode"));//查询条件--供应商简称
		String partName=ClaimTools.dealParamStr(params.get("partName"));//查询条件--配件代码
		String labourCode=ClaimTools.dealParamStr(params.get("labourCode"));//查询条件--配件名称
		String labourName=ClaimTools.dealParamStr(params.get("labourName"));//查询条件--车型代码
		String pos = ClaimTools.dealParamStr(params.get("pos"));
		StringBuffer sql=new StringBuffer();
		sql.append(" select b.part_code,b.part_name,b.part_is_changhe ,p.labour_code,p.labour_name,p.labour_price,p.labour_hours\n");
		sql.append(" ,u.name,to_char(p.update_date,'yyyy-mm-dd hh24:mi') update_time,to_char(p.create_date,'yyyy-mm-dd hh24:mi') create_time\n");
		sql.append(" from tm_pt_part_base b\n");
		sql.append(" left join Tt_As_Wr_Old_Part_Labour p on p.part_code = b.part_code\n");
		sql.append(" left join tc_user u on  nvl(p.update_by,p.create_by)=u.user_id\n");
		sql.append(" where 1=1\n"); 
		if(Utility.testString(pos)){
			if(Integer.parseInt(pos)==Constant.POSE_BUS_TYPE_WRD){
				sql.append(" and b.part_is_changhe="+Constant.PART_IS_CHANGHE_02+"\n");
			}
		}
		if(Utility.testString(partCode)){
			sql.append("  and b.PART_CODE like '%"+partCode.toUpperCase()+"%'\n");
		}
		if(Utility.testString(partName)){
			sql.append("  and b.PART_NAME like '%"+partName+"%'\n");
		}
		if(Utility.testString(labourCode)){
			sql.append("  and p.labour_code like '%"+labourCode.toUpperCase()+"%'\n");
		}
		if(Utility.testString(labourName)){
			sql.append("  and p.labour_name like '%"+labourName+"%'\n");
		}
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>)pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		
		return ps;
	}
	@SuppressWarnings("unchecked")
	public List<TtAsWrOldPartLabourPO> getPartDetail(String partCode){
		StringBuffer sql = new StringBuffer();
		sql.append(" select b.part_code,b.part_name ,p.labour_code,p.labour_name,p.labour_price,p.labour_hours\n");
		sql.append(" from tm_pt_part_base b\n");
		sql.append(" left join Tt_As_Wr_Old_Part_Labour p on p.part_code = b.part_code\n");
		sql.append(" where 1=1\n"); 
		sql.append(" and b.part_code='"+partCode+"'");
		return this.select(TtAsWrOldPartLabourPO.class, sql.toString(), null);
	}
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getLabourList(Map params,int curPage, int pageSize){
		String labourCode=ClaimTools.dealParamStr(params.get("labourCode"));//查询条件--配件名称
		String labourName=ClaimTools.dealParamStr(params.get("labourName"));//查询条件--车型代码
		StringBuffer sql=new StringBuffer();
		sql.append("select t.labour_code,t.cn_des labour_name,t.labour_hour\n");
		sql.append("from tt_as_wr_wrlabinfo t\n");
		sql.append("where t.tree_code=3\n");
		if(Utility.testString(labourCode)){
			sql.append("  and t.labour_code like '%"+labourCode.toUpperCase()+"%'\n");
		}
		if(Utility.testString(labourName)){
			sql.append("  and t.cn_des like '%"+labourName+"%'\n");
		}
		sql.append("group by t.labour_code,t.cn_des,t.labour_hour\n");
		sql.append("order by t.labour_code"); 
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>)pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		
		return ps;
	}
	@SuppressWarnings("unchecked")
	public List<TtAsWrOldReturnedDetailPO> getDetail(String partCode,String claimId){
		StringBuffer sql = new StringBuffer();
		sql.append("select  t.part_code,t.part_name,t.claim_no,t.claim_id,t.producer_code,t.producer_name,SUM(t.return_amount) return_amount\n");
		sql.append("from tt_as_wr_old_returned_detail t\n");
		sql.append("WHERE t.claim_id='"+claimId+"' AND t.part_code='"+partCode+"'\n");
		sql.append("GROUP BY t.part_code,t.part_name,t.claim_no,t.producer_code,t.producer_name,t.claim_id"); 
		return this.select(TtAsWrOldReturnedDetailPO.class, sql.toString(), null);
	}
	@SuppressWarnings("unchecked")
	public List<TtAsWrOldReturnedDetailPO> getDetail2(String partCode,String claimId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT p.down_part_code PART_CODE,\n");
		sql.append("   p.down_part_name  PART_NAME,\n");
		sql.append(" a.CLAIM_NO,\n");
		sql.append("  a.ID CLAIM_ID,\n");
		sql.append("  p.down_product_code PRODUCER_CODE,\n");
		sql.append("  p.down_product_name PRODUCER_NAME,\n");
		sql.append(" p.apply_quantity RETURN_AMOUNT\n");
		sql.append(" FROM tt_as_wr_application a ,Tt_As_Wr_Partsitem p\n");
		sql.append(" WHERE a.ID = '"+claimId+"'\n");
		sql.append(" AND p.down_part_code = '"+partCode+"'"); 

		return this.select(TtAsWrOldReturnedDetailPO.class, sql.toString(), null);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> saveRengePer(String outNo){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT T.OUT_NO,\n");
		sql.append("       T.SUPPLY_CODE,\n");
		sql.append("       T.SUPPLY_NAME,\n");
		sql.append("       T.OUT_PART_CODE,\n");
		sql.append("       T.OUT_PART_NAME,\n");
		sql.append("       D.UNIT,\n");
		sql.append("       SUM(T.OUT_AMOUT) NUM,\n");
		sql.append("       ROUND(SUM(NVL(L.LABOUR_AMOUNT, 0)) * 1.5, 2) LABOUR_AMOUNT,\n");
		sql.append("       SUM(NVL(NE.AMOUNT, 0)) OUT_AMOUNT,\n");
		sql.append("\n");
		sql.append("       NVL((SELECT SUM(NVL(AWP.AMOUNT, 0)) + SUM(NVL(WL.LABOUR_AMOUNT, 0))\n");
		sql.append("             FROM TT_AS_WR_OLD_OUT_PART P,\n");
		sql.append("                  TT_AS_WR_PARTSITEM    AWP,\n");
		sql.append("                  TT_AS_WR_LABOURITEM   WL\n");
		sql.append("            WHERE AWP.DOWN_PART_CODE = P.OUT_PART_CODE\n");
		sql.append("              AND AWP.WR_LABOURCODE = WL.WR_LABOURCODE\n");
		sql.append("              AND AWP.ID = WL.ID\n");
		sql.append("              AND T.OUT_NO = P.OUT_NO\n");
		sql.append("              AND T.OUT_PART_CODE =\n");
		sql.append("                  SUBSTR(P.REMARK, 1, INSTR(P.REMARK, '责') - 1)\n");
		sql.append("              AND T.OUT_PART_CODE = AWP.MAIN_PART_CODE),\n");
		sql.append("           0) RELATED_LOSSES\n");
		sql.append("  FROM TT_AS_WR_OLD_OUT_PART T\n");
		sql.append("  LEFT JOIN tT_AS_WR_APPLICATION A ON A.CLAIM_NO = T.CLAIM_NO\n");
		sql.append("  LEFT JOIN\n");
		sql.append("       (SELECT N.ID, N.MAIN_PART_CODE, SUM(N.AMOUNT) AMOUNT\n");
		sql.append("          FROM TT_AS_WR_NETITEM N\n");
		sql.append("         GROUP BY N.ID, N.MAIN_PART_CODE) NE ON   A.ID = NE.ID AND T.OUT_PART_CODE = NE.MAIN_PART_CODE\n");
		sql.append(" LEFT JOIN\n");
		sql.append("       TT_PART_DEFINE D ON T.OUT_PART_CODE = D.PART_OLDCODE ,\n");
		sql.append("       TT_AS_WR_LABOURITEM L,\n");
		sql.append("       TT_AS_WR_PARTSITEM WP\n");
		sql.append("       WHERE 1=1\n");
		sql.append("   AND A.ID = WP.ID\n");
		sql.append("   AND A.ID = L.ID\n");
		sql.append("   AND T.OUT_PART_CODE = WP.DOWN_PART_CODE\n");
		sql.append("   AND WP.WR_LABOURCODE = L.WR_LABOURCODE\n");
		sql.append("   AND (T.SUPPLY_CODE IS NOT NULL OR T.REMARK IS NULL)\n");
		sql.append(" AND t.Out_No='"+outNo+"'\n");
		sql.append(" GROUP BY T.OUT_NO,\n");
		sql.append("          T.SUPPLY_CODE,\n");
		sql.append("          T.SUPPLY_NAME,\n");
		sql.append("          T.OUT_PART_CODE,\n");
		sql.append("          T.OUT_PART_NAME,\n");
		sql.append("          D.UNIT\n");

		
		return  this.pageQuery(sql.toString(), null, this.getFunName());
	}
	/**
	 * 打印明细
	 * @param outNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> printRenge(String outNo){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT s.range_no,S.OUT_NO,\n");
		sql.append("       S.SUPPLY_CODE ,\n");
		sql.append("       S.SUPPLY_NAME,\n");
		sql.append("       S.PART_CODE,\n");
		sql.append("       S.PART_NAME,\n");
		sql.append("       S.PART_UNIT,\n");
		sql.append("       nvl(S.Print_Num,0) PART_QUANTITY,\n");
		sql.append("       nvl(S.LABOUR_AMOUNT,0) LABOUR_AMOUNT,\n");
		sql.append("       nvl(S.RELATED_LOSSES,0) RELATED_LOSSES ,\n");
		sql.append("       nvl(S.OUT_AMOUNT,0) OUT_AMOUNT,\n");
		sql.append("       nvl(S.Print_Part,0) PART_AMOUNT,\n");
		sql.append("       nvl(S.SMALL_AMOUNT,0) SMALL_AMOUNT,\n");
		sql.append("       S.REMARK,s.id,s.out_type,nvl(S.PRINT_NUM,0) PRINT_NUM,S.PRINT_PART\n");
		sql.append("  FROM TT_AS_WR_RANGE_SINGLE S\n");
		sql.append(" WHERE S.OUT_NO = '"+outNo+"' order by S.PART_CODE desc "); 

		return  this.pageQuery(sql.toString(), null, this.getFunName());
	}
	/**
	 * 打印页面的备注内容
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getprintRemark(List<Map<String,Object>> list){
		StringBuffer sql = new StringBuffer();
		if(list!=null&&list.size()>0){
			sql.append("select  \n");
			for(int i=0;i<list.size();i++){
				if(Double.valueOf(String.valueOf(BaseUtils.checkNull(list.get(i).get("RELATED_LOSSES"))))>0){
					sql.append(" max('第'||"+(i+1)+"||'项关联索赔单：'|| \n");
					sql.append(" case when p.out_no='"+String.valueOf(list.get(i).get("OUT_NO"))+"' AND p.out_part_code='"+String.valueOf(list.get(i).get("PART_CODE"))+"'\n");
					sql.append(" then to_char(wm_concat(p.claim_no)) end ) ||'......' remark"+i+",\n");
				}
			}
			sql.append(" 1 num \n");
			sql.append(" from Tt_As_Wr_Old_Out_Part p\n");
			sql.append(" where 1=1\n");
			sql.append(" and p.out_no="+String.valueOf(list.get(0).get("OUT_NO"))+"\n");
			//索赔单号连接超过varchar2最大范围4000的限制会报错,因此加上该限制预估4000/17(索赔单号长度)
			sql.append(" and rownum<=230 \n");
			sql.append("group by p.out_no,p.out_part_code"); 

			return  this.pageQuery(sql.toString(), null, this.getFunName());
		}else{
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getpartList(String partCode,String claimNo){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.CLAIM_NO,P.DOWN_PART_CODE PART_CODE ,P.DOWN_PART_NAME PART_NAME,P.QUANTITY AMOUNT,P.PART_ID\n");
		sql.append("FROM  Tt_As_Wr_Partsitem P,Tt_As_Wr_Application A\n");
		sql.append("WHERE P.MAIN_PART_CODE='"+partCode.toUpperCase()+"'\n");
		sql.append("AND  A.ID = P.ID\n");
		sql.append("AND A.CLAIM_NO='"+claimNo.toUpperCase()+"'\n");
		sql.append("AND P.DOWN_PART_CODE NOT IN ('00-000', 'NO_PARTS', '00-0000')\n");
		sql.append("AND P.IS_NOTICE = "+Constant.IF_TYPE_NO+"\n");
		sql.append("AND p.responsibility_type="+Constant.RESPONS_NATURE_STATUS_02+"\n"); 

		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryPartClaim(String partCode,String claimNo){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT d.claim_no,d.part_code,d.part_name,d.producer_code,d.producer_name,SUM(d.sign_amount) sign_amount,D.IS_MAIN_CODE\n");
		sql.append("FROM TT_AS_WR_OLD_RETURNED_DETAIL d\n");
		sql.append("WHERE d.claim_no='"+claimNo+"'\n");
		//sql.append("AND (d.main_part_code='"+partCode+"' OR d.part_code='"+partCode+"')\n");
		sql.append("AND d.main_part_code='"+partCode+"' \n");//此處只查詢次因件
		sql.append("AND d.is_out=0 AND (d.is_cliam=0 OR d.is_cliam IS NULL )\n");
		sql.append("AND d.sign_amount>0\n");
		sql.append("GROUP BY d.claim_no,d.part_code,d.part_name,d.producer_code,d.producer_name,D.IS_MAIN_CODE\n");
		sql.append("ORDER BY d.is_main_code"); 
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> printPartCode(String code,String dcode,String partCode,String claimNo,String name,String dname,String partName){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT  RD.CLAIM_NO,RD.PART_CODE,RD.PART_NAME,RD.PRODUCER_CODE,RD.PRODUCER_NAME,\n");
		sql.append("RD.MAIN_PART_CODE,SUM(RD.SIGN_AMOUNT) SIGN_AMOUNT\n");
		sql.append("FROM TT_AS_WR_OLD_RETURNED_DETAIL RD ,TM_DEALER D ,TT_AS_WR_OLD_RETURNED R\n");
		sql.append("WHERE RD.RETURN_ID = R.ID AND R.DEALER_ID = D.DEALER_ID\n");
		sql.append("AND RD.SIGN_AMOUNT>0 AND RD.IS_OUT=0 AND (RD.IS_CLIAM=0 OR RD.IS_CLIAM IS NULL)\n");
		sql.append("AND RD.IS_MAIN_CODE="+Constant.RESPONS_NATURE_STATUS_02+"\n");
		if(Utility.testString(dcode)){
			sql.append("AND D.DEALER_CODE LIKE '%"+code.toUpperCase()+"%'\n");
		}
		if(Utility.testString(dname)){
			sql.append("AND D.DEALER_NAME LIKE '%"+dname+"%'\n");
		}
		if(Utility.testString(code)){
			sql.append("AND RD.PRODUCER_CODE LIKE '%"+code.toUpperCase()+"%'\n");
		}
		if(Utility.testString(name)){
			sql.append("AND RD.PRODUCER_NAME LIKE '%"+name+"%'\n");
		}
		if(Utility.testString(partCode)){
			sql.append("AND RD.PART_CODE LIKE '%"+partCode.toUpperCase()+"%'\n");
		}
		if(Utility.testString(partName)){
			sql.append("AND RD.PART_NAME LIKE '%"+partName+"%'\n");
		}
		if(Utility.testString(claimNo)){
			sql.append("AND RD.CLAIM_NO LIKE '%"+claimNo.toUpperCase()+"%'\n");
		}
		sql.append("GROUP BY RD.CLAIM_NO,RD.PART_CODE,RD.PART_NAME,RD.PRODUCER_CODE,RD.PRODUCER_NAME,\n");
		sql.append("RD.MAIN_PART_CODE\n"); 
		sql.append("ORDER BY rd.main_part_code");
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> queryRangeList(RequestWrapper request,int curPage, int pageSize){
		StringBuffer sb= new StringBuffer();
		sb.append("select s.range_no,\n" );
		sb.append("       s.out_no,\n" );
		sb.append("       Sum(s.print_num) part_quantity,\n" );
		sb.append("       sum(s.small_amount) small_amount,\n" );
		sb.append("       s.supply_code,\n" );
		sb.append("(select d.maker_name from tt_part_maker_define d where d.maker_code= s.supply_code) as supply_name，");
		sb.append("       to_char(s.create_date, 'yyyy-mm-dd hh24:mi') create_time,\n" );
		sb.append("       u.name,\n" );
		sb.append("       to_char(s.audit_date, 'yyyy-mm-dd hh24:mi') audit_time\n" );
		sb.append("  from Tt_As_Wr_Range_Single s, tc_user u\n" );
		sb.append(" where 1 = 1\n" );
		DaoFactory.getsql(sb, "s.supply_name", DaoFactory.getParam(request, "supply_name"), 2);
		DaoFactory.getsql(sb, "s.supply_code", DaoFactory.getParam(request, "supply_code").toUpperCase(), 2);
		DaoFactory.getsql(sb, "s.range_no", DaoFactory.getParam(request, "range_no").toUpperCase(), 2);
		sb.append("   and s.audit_by = u.user_id(+)\n" );
		sb.append("   and s.out_type in (1, 2) \n" );
		sb.append(" group by s.RANGE_NO,\n" );
		sb.append("          s.out_no,\n" );
		sb.append("          s.supply_code,\n" );
		sb.append("          u.name,\n" );
		sb.append("          to_char(s.audit_date, 'yyyy-mm-dd hh24:mi'),\n" );
		sb.append("          to_char(s.create_date, 'yyyy-mm-dd hh24:mi') order by to_char(s.audit_date, 'yyyy-mm-dd hh24:mi') desc ");
		return this.pageQuery(sb.toString(),null,this.getFunName(), pageSize, curPage); 
	}
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> inHouseDetailList(Map params,int curPage, int pageSize){
		String dealer_name=ClaimTools.dealParamStr(params.get("dealer_name"));
		String part_name=ClaimTools.dealParamStr(params.get("part_name"));
		String dealer_code = ClaimTools.dealParamStr(params.get("dealer_code"));
		String part_code=ClaimTools.dealParamStr(params.get("part_code"));
		String claim_no=ClaimTools.dealParamStr(params.get("claim_no"));
		String vin = ClaimTools.dealParamStr(params.get("vin"));
		String in_by_name=ClaimTools.dealParamStr(params.get("in_by_name"));
		String in_start_date=ClaimTools.dealParamStr(params.get("in_start_date"));
		String in_end_date = ClaimTools.dealParamStr(params.get("in_end_date"));
		String IS_MAIN_CODE=ClaimTools.dealParamStr(params.get("IS_MAIN_CODE"));
		String is_ok = ClaimTools.dealParamStr(params.get("is_ok"));
		String engine_no = ClaimTools.dealParamStr(params.get("engine_no"));
		String producer_name = ClaimTools.dealParamStr(params.get("producer_name"));//==增加供应商模糊查询2015-6-12lj

		
		StringBuffer sql= new StringBuffer();
		sql.append("select * from (SELECT R.SIGN_NO,\n" );
		sql.append("       TD.DEALER_CODE,\n" );
		sql.append("       TD.DEALER_SHORTNAME DEALER_NAME,\n" );
		sql.append("       A.CLAIM_NO,\n" );
		sql.append("       D.SIGN_AMOUNT,\n" );
		sql.append("       D.DEDUCT_REMARK,\n" );
		sql.append("       D.PART_CODE,\n" );
		sql.append("       D.PART_NAME,\n" );
		sql.append("       D.PRODUCER_CODE,\n" );
		sql.append("       D.PRODUCER_NAME,\n" );
		sql.append("       a.trouble_desc as REMARK, \n" );
		sql.append("       A.MODEL_NAME,\n" );
		sql.append("       A.VIN,\n" );
		sql.append("       R.IN_WARHOUSE_NAME,\n" );
		sql.append("       (select c.NAME from TC_USER c where c.user_id = IN_WARHOUSE_BY) as NAME,\n" );
		sql.append("       TO_CHAR(d.in_date, 'YYYY-MM-DD') IN_TIME,\n" );
		sql.append("       D.IS_MAIN_CODE,\n" );
		sql.append("       A.CLAIM_TYPE,\n" );
		sql.append("       R.RETURN_NO,\n" );
		sql.append("       D.BARCODE_NO,\n" );
		sql.append("       d.CLAIM_ID\n" );
		sql.append("  FROM TT_AS_WR_OLD_RETURNED_DETAIL D,\n" );
		sql.append("       TT_AS_WR_OLD_RETURNED        R,\n" );
		sql.append("       TM_DEALER                    TD,\n" );
		sql.append("       TT_AS_WR_APPLICATION         A,\n" );
		sql.append("       TT_AS_WR_PARTSITEM           P\n" );
		sql.append(" where D.RETURN_ID = R.ID(+)\n" );
		sql.append("   and TD.DEALER_ID = R.DEALER_ID\n" );
		sql.append("   and d.CLAIM_NO = a.CLAIM_NO(+)\n" );
		sql.append("   and P.ID = A.ID\n" );
		sql.append("   and P.ID = D.CLAIM_ID\n" );
		sql.append("   and P.PART_CODE = D.PART_CODE\n" );
		sql.append("   AND (R.STATUS = 10811005 OR R.STATUS = 10811007 OR\n" );
		sql.append("       (R.STATUS = 10811004 AND D.IS_IN_HOUSE = 10041001))\n");
		DaoFactory.getsql(sql, "D.IS_MAIN_CODE", IS_MAIN_CODE, 1);
		if(Utility.testString(is_ok)){
			if("1".equalsIgnoreCase(is_ok)){
				sql.append(" and D.SIGN_AMOUNT=1\n");
			}else{
				sql.append(" and D.SIGN_AMOUNT=0\n");
			}
		}
		DaoFactory.getsql(sql, "td.dealer_name", dealer_name, 2);
		DaoFactory.getsql(sql, "td.dealer_code", dealer_code.toUpperCase(), 2);
		DaoFactory.getsql(sql, "d.part_name", part_name, 2);
		DaoFactory.getsql(sql, "d.part_code", part_code.toUpperCase(), 2);
		DaoFactory.getsql(sql, "a.claim_no", claim_no.toUpperCase(), 2);
		DaoFactory.getsql(sql, "a.vin", vin.toUpperCase(), 2);
		DaoFactory.getsql(sql, "a.engine_no", engine_no.toUpperCase(), 2);
		DaoFactory.getsql(sql, "d.in_date", in_start_date, 3);
		DaoFactory.getsql(sql, "d.in_date", in_end_date, 4);
		DaoFactory.getsql(sql, "d.producer_name", producer_name, 2);//==2015-6-12增加供应商模糊查询
		sql.append("order by r.sign_no desc ) t where 1=1 \n "); 
		DaoFactory.getsql(sql, "t.name", in_by_name, 2);
		PageResult<Map<String,Object>> pr = this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage); 
		if(pr!=null){
			return pr;
		}else{
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> inHouseDetailList2(Map params){
		String dealer_name=ClaimTools.dealParamStr(params.get("dealer_name"));
		String part_name=ClaimTools.dealParamStr(params.get("part_name"));
		String dealer_code = ClaimTools.dealParamStr(params.get("dealer_code"));
		String part_code=ClaimTools.dealParamStr(params.get("part_code"));
		String claim_no=ClaimTools.dealParamStr(params.get("claim_no"));
		String vin = ClaimTools.dealParamStr(params.get("vin"));
		String in_by_name=ClaimTools.dealParamStr(params.get("in_by_name"));
		String in_start_date=ClaimTools.dealParamStr(params.get("in_start_date"));
		String in_end_date = ClaimTools.dealParamStr(params.get("in_end_date"));
		String IS_MAIN_CODE=ClaimTools.dealParamStr(params.get("IS_MAIN_CODE"));
		String is_ok = ClaimTools.dealParamStr(params.get("is_ok"));
		String engine_no = ClaimTools.dealParamStr(params.get("engine_no"));
		String producer_name = ClaimTools.dealParamStr(params.get("producer_name"));//==增加供应商模糊查询2015-6-12lj

		
		StringBuffer sql= new StringBuffer();
		sql.append("select * from (SELECT R.SIGN_NO,\n" );
		sql.append("       TD.DEALER_CODE,\n" );
		sql.append("       TD.DEALER_SHORTNAME DEALER_NAME,\n" );
		sql.append("       A.CLAIM_NO,\n" );
		sql.append("       D.SIGN_AMOUNT,\n" );
		sql.append("       D.PART_CODE,\n" );
		sql.append("       D.PART_NAME,\n" );
		sql.append("       D.PRODUCER_CODE,\n" );
		sql.append("       D.PRODUCER_NAME,\n" );
		sql.append("       a.trouble_desc as REMARK,\n" );
		sql.append("       A.MODEL_NAME,\n" );
		sql.append("       A.VIN,\n" );
		sql.append("       R.IN_WARHOUSE_NAME,\n" );
		sql.append("        tc4.name,\n" );
		sql.append("       TO_CHAR(d.in_date, 'YYYY-MM-DD') IN_TIME,\n" );
		sql.append("       R.RETURN_NO,\n" );
		sql.append("       D.BARCODE_NO,\n" );
		sql.append("      tc1.code_desc  IS_MAIN_CODE,\n" );
		sql.append("      tc2.code_desc  DEDUCT_REMARK,\n" );
		sql.append("      tc3.code_desc CLAIM_TYPE,\n" );
		sql.append("       d.CLAIM_ID\n" );
		sql.append("  FROM TT_AS_WR_OLD_RETURNED_DETAIL D,\n" );
		sql.append("       TT_AS_WR_OLD_RETURNED        R,\n" );
		sql.append("       TM_DEALER                    TD,\n" );
		sql.append("       TT_AS_WR_APPLICATION         A,\n" );
		sql.append("       TT_AS_WR_PARTSITEM           P,\n" );
		sql.append("       tc_code           tc1,\n" );
		sql.append("       tc_code           tc2,\n" );
		sql.append("       tc_code           tc3,\n" );
		sql.append("       tc_user            tc4\n" );
		sql.append(" where D.RETURN_ID = R.ID(+)\n" );
		sql.append("   and TD.DEALER_ID = R.DEALER_ID\n" );
		sql.append("   and d.CLAIM_NO = a.CLAIM_NO(+)\n" );
		sql.append("   and P.ID = A.ID\n" );
		sql.append("   and P.ID = D.CLAIM_ID\n" );
		sql.append("   and P.PART_CODE = D.PART_CODE\n" );
		sql.append(" and D.IS_MAIN_CODE =tc1.code_id(+)\n" );
		sql.append(" and D.DEDUCT_REMARK= tc2.code_id(+)\n" );
		sql.append(" and IN_WARHOUSE_BY= tc4.user_id(+)\n" );
		sql.append(" and a.CLAIM_TYPE =tc3.code_id(+)   AND (R.STATUS = 10811005 OR  R.STATUS = 10811007 OR\n" );

		sql.append("       (R.STATUS = 10811004 AND D.IS_IN_HOUSE = 10041001))\n");
		DaoFactory.getsql(sql, "D.IS_MAIN_CODE", IS_MAIN_CODE, 1);
		if(Utility.testString(is_ok)){
			if("1".equalsIgnoreCase(is_ok)){
				sql.append(" and D.SIGN_AMOUNT=1\n");
			}else{
				sql.append(" and D.SIGN_AMOUNT=0\n");
			}
		}
		DaoFactory.getsql(sql, "td.dealer_name", dealer_name, 2);
		DaoFactory.getsql(sql, "td.dealer_code", dealer_code.toUpperCase(), 2);
		DaoFactory.getsql(sql, "d.part_name", part_name, 2);
		DaoFactory.getsql(sql, "d.part_code", part_code.toUpperCase(), 2);
		DaoFactory.getsql(sql, "a.claim_no", claim_no.toUpperCase(), 2);
		DaoFactory.getsql(sql, "a.vin", vin.toUpperCase(), 2);
		DaoFactory.getsql(sql, "a.engine_no", engine_no.toUpperCase(), 2);
		DaoFactory.getsql(sql, "d.in_date", in_start_date, 3);
		DaoFactory.getsql(sql, "d.in_date", in_end_date, 4);
		DaoFactory.getsql(sql, "d.producer_name", producer_name, 2);//==2015-6-12增加供应商模糊查询
		sql.append("order by r.sign_no desc ) t where 1=1 \n "); 
		DaoFactory.getsql(sql, "t.name", in_by_name, 2);
		PageResult pr = this.pageQuery(sql.toString(), null, getFunName(), Constant.PAGE_SIZE_MAX.intValue(), 1);
		if(pr!=null){
			return pr;
		}else{
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> oldPartReturnDetailList(Map params,int curPage, int pageSize){
		String dealer_name=ClaimTools.dealParamStr(params.get("dealer_name"));
		String part_name=ClaimTools.dealParamStr(params.get("part_name"));
		String dealer_code = ClaimTools.dealParamStr(params.get("dealer_code"));
		String part_code=ClaimTools.dealParamStr(params.get("part_code"));
		String vin = ClaimTools.dealParamStr(params.get("vin"));
		String return_status=ClaimTools.dealParamStr(params.get("return_status"));
		String in_start_date=ClaimTools.dealParamStr(params.get("in_start_date"));
		String in_end_date = ClaimTools.dealParamStr(params.get("in_end_date"));
		String is_ok = ClaimTools.dealParamStr(params.get("is_ok"));
		String claim_type = ClaimTools.dealParamStr(params.get("claim_type"));
		String return_no = ClaimTools.dealParamStr(params.get("return_no"));
		String claim_no = ClaimTools.dealParamStr(params.get("claim_no"));
		String beginTime=ClaimTools.dealParamStr(params.get("beginTime"));
		String endTime=  ClaimTools.dealParamStr(params.get("endTime"));
		String root_org_name=ClaimTools.dealParamStr(params.get("root_org_name"));
		String urgent=ClaimTools.dealParamStr(params.get("urgent"));
		String is_out=ClaimTools.dealParamStr(params.get("is_out"));
		String producer_name=ClaimTools.dealParamStr(params.get("producer_name"));
		String inhouse_start_date=ClaimTools.dealParamStr(params.get("inhouse_start_date"));
		String inhouse_end_date=ClaimTools.dealParamStr(params.get("inhouse_end_date"));
		StringBuffer sql= new StringBuffer();
		
		sql.append("SELECT  distinct t.* from (SELECT \tA.ID,\n");
		sql.append(" A.CLAIM_NO,\n");
		sql.append("  A.DEALER_ID,a.urgent,\n");
		sql.append("(select v.root_org_name from vw_org_dealer_service v  where v.dealer_id=A.DEALER_ID) as root_org_name,");
		sql.append("  A.DEALER_CODE,\n");
		sql.append("  A.DEALER_NAME,\n");
		sql.append("  A.VIN,\n");
		sql.append("   A.RO_NO,\n");
		sql.append("  A.PART_CODE,\n");
		sql.append("  A.PART_NAME,\n");
		sql.append("  A.DOWN_PRODUCT_CODE,\n");
		sql.append("  A.DOWN_PRODUCT_NAME,\n");
		sql.append("  A.RESPONSIBILITY_TYPE,\n");
		sql.append("  A.BARCODE_NO,\n");
		sql.append("  A.CLAIM_TYPE,\n");
		sql.append("   A.RESPONSIBILITY_TYPE_NAME,\n");
		sql.append("   A.CLAIM_TYPE_NAME,\n");
		sql.append("   r.in_warhouse_date,\n");
		sql.append("  A.MODEL_NAME,TO_CHAR(a.sub_date,'YYYY-MM-DD HH24:MI') sub_TIME,\n");
		sql.append("  R.RETURN_NO,TO_CHAR(r.return_date,'YYYY-MM-DD HH24:MI') IN_TIME,TO_CHAR(a.report_date,'YYYY-MM-DD HH24:MI') report_TIME,\n");
		sql.append(" CASE WHEN r.status  in ("+Constant.BACK_LIST_STATUS_03+","+Constant.BACK_LIST_STATUS_04+","+Constant.BACK_LIST_STATUS_05+","+Constant.BACK_LIST_STATUS_07+") THEN '已签收'\n");
		sql.append(" WHEN  r.status  in ("+Constant.BACK_LIST_STATUS_02+") THEN '在途' ELSE\n");
		sql.append("  '未返运' end  return_status,\n ");
		sql.append(" case when  rD.SIGN_AMOUNT=1 and rd.is_in_house="+Constant.IF_TYPE_YES+" then '已审核'  \n");
		sql.append(" when 	rD.SIGN_AMOUNT=0 and rd.is_in_house="+Constant.IF_TYPE_YES+" then '拒赔' else '' end audit_status,\n");
		sql.append(" CASE  WHEN    rD.SIGN_AMOUNT=0  and  rd.is_in_house=10041001  then  TC.CODE_DESC \n");
		sql.append("   ELSE  ''  END  DEDUCT_DESC \n");
		sql.append(" FROM (SELECT A.CLAIM_NO,\n");
		sql.append(" A.ID,a.urgent,\n");
		sql.append("  D.DEALER_ID,\n");
		sql.append("  D.DEALER_CODE,\n");
		sql.append("  D.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("  A.VIN,\n");
		sql.append("  P.DOWN_PART_CODE PART_CODE,\n");
		sql.append("  P.DOWN_PART_NAME PART_NAME,\n");
		sql.append("  P.DOWN_PRODUCT_CODE,\n");
		sql.append("  P.DOWN_PRODUCT_NAME,\n");
		sql.append("  P.RESPONSIBILITY_TYPE,\n");
		sql.append("  B.BARCODE_NO,\n");
		sql.append("  A.CLAIM_TYPE,\n");
		sql.append("  A.MODEL_NAME,\n");
		sql.append("   A.RO_NO,\n");
		sql.append("   tc1.code_desc RESPONSIBILITY_TYPE_NAME ,\n");
		sql.append("    tc2.code_desc CLAIM_TYPE_NAME,\n");
		sql.append(" a.report_date,\n");
		sql.append(" a.sub_date\n");
		sql.append(" FROM TT_AS_WR_APPLICATION       A,\n");
		sql.append("  TT_AS_WR_PARTSITEM         P,\n");
		sql.append("  TT_AS_WR_PARTSITEM_BARCODE B,\n");
		sql.append("  TM_DEALER                  D,\n");
		sql.append("  tc_code tc1,\n");
		sql.append("  tc_code tc2\n");
		sql.append("  WHERE A.ID = P.ID\n");
		sql.append(" AND P.PART_ID = B.PART_ID\n");
		sql.append("  AND A.DEALER_ID = D.DEALER_ID\n");
		sql.append(" AND P.RESPONSIBILITY_TYPE= tc1.code_id\n");
		sql.append(" AND A.CLAIM_TYPE= tc2.code_id\n");
		sql.append(" 	AND a.status IN ("+Constant.CLAIM_APPLY_ORD_TYPE_07+","+Constant.CLAIM_APPLY_ORD_TYPE_08+","+Constant.CLAIM_APPLY_ORD_TYPE_13+") \n ");
		if(Utility.testString(producer_name)){//2015 -6-12lj供应商查询
			sql.append(" and p.DOWN_PRODUCT_NAME like '%"+producer_name+"%'\n");
		}
		if(Utility.testString(dealer_code)){
			sql.append(" and d.dealer_code like '%"+dealer_code.toUpperCase()+"%'\n");
		}
		if(Utility.testString(claim_no)){
			sql.append(" and A.claim_no like '%"+claim_no.toUpperCase()+"%'\n");
		}
		if(Utility.testString(urgent)){
			sql.append(" and A.urgent ="+urgent+" \n");
		}
		if(Utility.testString(dealer_name)){
			sql.append(" and (d.dealer_name like '%"+dealer_name+"%' OR d.dealer_shortname LIKE '%"+dealer_name+"%')\n");
		}
		if(Utility.testString(part_name)){
			sql.append(" and P.DOWN_PART_NAME like '%"+part_name+"%'\n");
		}
		if(Utility.testString(claim_type)){
			sql.append(" and A.claim_type ="+claim_type+" \n");
		}
		
		
		if(Utility.testString(part_code)){
			sql.append(" and P.DOWN_PART_CODE like '%"+part_code.toUpperCase()+"%'\n");
		}
		if(Utility.testString(vin)){
			sql.append(" and a.vin like '%"+vin.toUpperCase()+"%'\n");
		}
		if(Utility.testString(in_start_date)){
			sql.append(" and  A.sub_date>=to_date('"+in_start_date+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n");
		}
		
		if(Utility.testString(in_end_date)){
			sql.append(" and  A.sub_date<=to_date('"+in_end_date+" 23:59:59','yyyy-mm-dd hh24:mi:ss')\n");
		}
		if(Utility.testString(beginTime)){
			sql.append(" and  A.report_date>=to_date('"+beginTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n");
		}
		
		if(Utility.testString(endTime)){
			sql.append(" and  A.report_date<=to_date('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss')\n");
		}
		sql.append(" ) A"); 
	
		sql.append(" LEFT JOIN TT_AS_WR_OLD_RETURNED_DETAIL RD ON   RD.CLAIM_NO = A.CLAIM_NO"); 
		sql.append(" LEFT JOIN TT_AS_WR_OLD_RETURNED R ON R.DEALER_ID = A.DEALER_ID and RD.RETURN_ID = R.ID\n");
		sql.append(" LEFT JOIN TC_CODE TC ON TC.CODE_ID = RD.Deduct_Remark\n");
		
		sql.append(" where 1=1 \n");
		DaoFactory.getsql(sql, "rd.is_out", is_out, 1);
		DaoFactory.getsql(sql, "r.in_warhouse_date",inhouse_start_date , 3);
		DaoFactory.getsql(sql, "r.in_warhouse_date",inhouse_end_date , 4);
		if(Utility.testString(return_status)){
			if("0".equalsIgnoreCase(return_status)){
				sql.append(" and  r.status  in ("+Constant.BACK_LIST_STATUS_03+","+Constant.BACK_LIST_STATUS_04+","+Constant.BACK_LIST_STATUS_05+","+Constant.BACK_LIST_STATUS_07+")\n");
			}else if("1".equalsIgnoreCase(return_status)){
				sql.append(" and  r.status  in ("+Constant.BACK_LIST_STATUS_02+")\n");
				}else if("2".equalsIgnoreCase(return_status)){
				sql.append(" and  rd.CLAIM_NO is null  \n");
			} 
		}
		if(Utility.testString(is_ok)&&"0".equalsIgnoreCase(return_status)){
			if("0".equalsIgnoreCase(is_ok)){
				sql.append(" and rD.SIGN_AMOUNT=1 and rd.is_in_house="+Constant.IF_TYPE_YES+"\n");
			}else if("1".equalsIgnoreCase(is_ok)){
				sql.append(" and rd.is_in_house="+Constant.IF_TYPE_NO+"\n");
			}else if("2".equalsIgnoreCase(is_ok)){
				sql.append(" and rD.SIGN_AMOUNT=0 and rd.is_in_house="+Constant.IF_TYPE_YES+"\n");
			}
		}
		if(Utility.testString(return_no)){
			sql.append(" and R.return_no like '%"+return_no+"%' \n");
		}
		sql.append(" order by a.id ) t where 1=1 \n");

		DaoFactory.getsql(sql, "t.root_org_name", root_org_name, 1);
		PageResult<Map<String,Object>> pr = this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage); 
		if(pr!=null){
			return pr;
		}else{
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> oldPartReturnDetailList2(Map params){
		String dealer_name=ClaimTools.dealParamStr(params.get("dealer_name"));
		String part_name=ClaimTools.dealParamStr(params.get("part_name"));
		String dealer_code = ClaimTools.dealParamStr(params.get("dealer_code"));
		String part_code=ClaimTools.dealParamStr(params.get("part_code"));
		String vin = ClaimTools.dealParamStr(params.get("vin"));
		String return_status=ClaimTools.dealParamStr(params.get("return_status"));
		String in_start_date=ClaimTools.dealParamStr(params.get("in_start_date"));
		String in_end_date = ClaimTools.dealParamStr(params.get("in_end_date"));
		String is_ok = ClaimTools.dealParamStr(params.get("is_ok"));
		String claim_type = ClaimTools.dealParamStr(params.get("claim_type"));
		String claim_no = ClaimTools.dealParamStr(params.get("claim_no"));
		String return_no = ClaimTools.dealParamStr(params.get("return_no"));
		StringBuffer sql= new StringBuffer();
		
		sql.append("SELECT \tA.ID,\n");
		sql.append(" A.CLAIM_NO,\n");
		sql.append("  A.DEALER_ID,\n");
		sql.append("  A.DEALER_CODE,\n");
		sql.append(" (select v.root_org_name from vw_org_dealer_service v where v.dealer_id=A.DEALER_ID) as root_org_name,\n");
		sql.append("  A.DEALER_NAME,\n");
		sql.append("  A.VIN,\n");
		sql.append("  A.PART_CODE,\n");
		sql.append("  A.PART_NAME,\n");
		sql.append("  A.DOWN_PRODUCT_CODE,\n");
		sql.append("  A.DOWN_PRODUCT_NAME,\n");
		sql.append("  A.MAIN_CODE_NAME,\n");
		sql.append("  A.CLAIM_TYPE_NAME,\n");
		sql.append("  A.RESPONSIBILITY_TYPE,\n");
		sql.append("  A.BARCODE_NO,\n");
		sql.append("  A.CLAIM_TYPE,\n");
		sql.append("  A.MODEL_NAME,\n");
		sql.append("  R.RETURN_NO,TO_CHAR(r.return_date,'YYYY-MM-DD HH24:MI') IN_TIME,TO_CHAR(a.report_date,'YYYY-MM-DD HH24:MI') report_TIME,\n");
		sql.append(" CASE WHEN r.status  in ("+Constant.BACK_LIST_STATUS_03+","+Constant.BACK_LIST_STATUS_04+","+Constant.BACK_LIST_STATUS_05+") THEN '已签收'\n");
		sql.append(" WHEN  r.status  in ("+Constant.BACK_LIST_STATUS_02+") THEN '在途' ELSE\n");
		sql.append("  '未返运' end  return_status,\n ");
		sql.append(" case when  rD.SIGN_AMOUNT=1 and rd.is_in_house="+Constant.IF_TYPE_YES+" then '已审核'  \n");
		sql.append(" when 	rD.SIGN_AMOUNT=0 and rd.is_in_house="+Constant.IF_TYPE_YES+" then '拒赔' else '' end audit_status,\n");
		sql.append(" CASE  WHEN    rD.SIGN_AMOUNT=0  and  rd.is_in_house=10041001  then  TC.CODE_DESC \n");
		sql.append("   ELSE  ''  END  DEDUCT_DESC \n");
		sql.append(" FROM (SELECT A.CLAIM_NO,\n");
		sql.append(" A.ID,\n");
		sql.append("  D.DEALER_ID,\n");
		sql.append("  D.DEALER_CODE,\n");
		sql.append("  D.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("  A.VIN,\n");
		sql.append("  P.DOWN_PART_CODE PART_CODE,\n");
		sql.append("  P.DOWN_PART_NAME PART_NAME,\n");
		sql.append("  P.DOWN_PRODUCT_CODE,\n");
		sql.append("  P.DOWN_PRODUCT_NAME,\n");
		sql.append("  P.RESPONSIBILITY_TYPE,\n");
		sql.append("  B.BARCODE_NO,\n");
		sql.append("  A.CLAIM_TYPE,\n");
		sql.append("  A.MODEL_NAME,\n");
		sql.append("  c.code_desc MAIN_CODE_NAME,\n");
		sql.append("  cc.code_desc CLAIM_TYPE_NAME,\n");
		sql.append(" a.report_date");
		sql.append(" FROM TT_AS_WR_APPLICATION       A,\n");
		sql.append("  TT_AS_WR_PARTSITEM         P,\n");
		sql.append("  TT_AS_WR_PARTSITEM_BARCODE B,\n");
		sql.append("  TM_DEALER                  D,\n");
		sql.append("  tc_code c,\n");
		sql.append("  tc_code cc   \n");
		sql.append("  WHERE A.ID = P.ID\n");
		sql.append(" AND P.PART_ID = B.PART_ID\n");
		sql.append("  AND A.DEALER_ID = D.DEALER_ID\n");
		sql.append(" AND P.RESPONSIBILITY_TYPE =c.code_id\n");
		sql.append("  AND A.CLAIM_TYPE = cc.code_id\n");
		if(Utility.testString(dealer_code)){
			sql.append(" and d.dealer_code like '%"+dealer_code.toUpperCase()+"%'\n");
		}
		if(Utility.testString(dealer_name)){
			sql.append(" and (d.dealer_name like '%"+dealer_name+"%' OR d.dealer_shortname LIKE '%"+dealer_name+"%')\n");
		}
		if(Utility.testString(part_name)){
			sql.append(" and P.DOWN_PART_NAME like '%"+part_name+"%'\n");
		}
		if(Utility.testString(claim_type)){
			sql.append(" and A.claim_type = "+claim_type+" \n");
		}
		if(Utility.testString(claim_no)){
			sql.append(" and A.claim_no like '%"+claim_no.toUpperCase()+"%'\n");
		}
		
		if(Utility.testString(part_code)){
			sql.append(" and P.DOWN_PART_CODE like '%"+part_code.toUpperCase()+"%'\n");
		}
		if(Utility.testString(vin)){
			sql.append(" and a.vin like '%"+vin.toUpperCase()+"%'\n");
		}
		if(Utility.testString(in_start_date)){
			sql.append(" and  A.REPORT_DATE>=to_date('"+in_start_date+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n");
		}
		
		if(Utility.testString(in_end_date)){
			sql.append(" and  A.REPORT_DATE<=to_date('"+in_end_date+" 23:59:59','yyyy-mm-dd hh24:mi:ss')\n");
		}
		sql.append(" ) A"); 
	
		sql.append(" LEFT JOIN TT_AS_WR_OLD_RETURNED_DETAIL RD ON   RD.CLAIM_NO = A.CLAIM_NO"); 
		sql.append(" LEFT JOIN TT_AS_WR_OLD_RETURNED R ON R.DEALER_ID = A.DEALER_ID and RD.RETURN_ID = R.ID\n");
		sql.append(" LEFT JOIN TC_CODE TC ON TC.CODE_ID = RD.Deduct_Remark\n");
		sql.append(" where 1=1 \n");
		if(Utility.testString(return_status)){
			if("0".equalsIgnoreCase(return_status)){
				sql.append(" and  r.status  in ("+Constant.BACK_LIST_STATUS_03+","+Constant.BACK_LIST_STATUS_04+","+Constant.BACK_LIST_STATUS_05+")\n");
			}else if("1".equalsIgnoreCase(return_status)){
				sql.append(" and  r.status  in ("+Constant.BACK_LIST_STATUS_02+")\n");
				}else if("2".equalsIgnoreCase(return_status)){
				sql.append(" and  rd.claom_no is null  \n");
			} 
		}
		if(Utility.testString(is_ok)&&"0".equalsIgnoreCase(return_status)){
			if("0".equalsIgnoreCase(is_ok)){
				sql.append(" and rD.SIGN_AMOUNT=1 and rd.is_in_house="+Constant.IF_TYPE_YES+"\n");
			}else if("1".equalsIgnoreCase(is_ok)){
				sql.append(" and rd.is_in_house="+Constant.IF_TYPE_NO+"\n");
			}else if("2".equalsIgnoreCase(is_ok)){
				sql.append(" and rD.SIGN_AMOUNT=0 and rd.is_in_house="+Constant.IF_TYPE_YES+"\n");
			}
		}
		if(Utility.testString(return_no)){
			sql.append(" and R.return_no like '%"+return_no+"%' \n");
		}
		sql.append(" order by a.id \n");
		List<Map<String,Object>> pr = this.pageQuery(sql.toString(), null, getFunName());
		if(pr!=null){
			return pr;
		}else{
			return null;
		}
	}
	/**
	 * 更新所有关于主因件的数据的金额  zyw 2014-9-5 
	 * @param id 
	 * @param type 1 主 0次 
	 * @param loginUser 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int updateAmountByMainPartCode(String id,int type, AclUserBean loginUser) {
		int res=1;
		try {
			dao=new ClaimOldPartOutStorageDao();
			TtAsWrOldReturnedDetailPO po=new TtAsWrOldReturnedDetailPO();
			po.setId(BaseUtils.ConvertLong(id));
			po = (TtAsWrOldReturnedDetailPO) dao.select(po).get(0);
			if(type==1){//主因件
				int count=dao.findisMainCode(po);
				if(count!=0){//只入主因件材料费
					Double amount=dao.findinStroeMainCodeMoney(po);
					List<Map<String,Object>> list=dao.findInsertPoValues(id);
					if(list!=null && list.size()>0){
						this.insertPo(amount, list,loginUser,"是");
					}
				}else{//什么钱都加
					Double amount=dao.findinStroeMainCodeMoneyAll(po);
					List<Map<String,Object>> list=dao.findInsertPoValues(id);
					List<Map<String, Object>> findhypoByMainCode = dao.findhypoByMainCode(po);
					if(findhypoByMainCode!=null && findhypoByMainCode.size()>0){
						
						for (Map<String, Object> map : findhypoByMainCode) {
							String wr_labourcode = dao.findinStroehypo(po,map.get("PART_CODE").toString());
							List<Map<String,Object>> partsitemCodes = dao.findPartsitem(wr_labourcode,po);
							if(partsitemCodes!=null&&partsitemCodes.size()==1){
								Double hypoMoney=dao.findhypoMoney(po);
								amount= Arith.add(amount,hypoMoney);
							}else{
								int count1=dao.findCountAllByPartCode(partsitemCodes,po);
								if(count1==0){//两个次因件挂的同一个工时，并且这个工时没有挂主因件
									boolean flag=false;
									for (Map<String, Object> map2 : partsitemCodes) {
										if(!(map2.get("PART_CODE").toString().equals(po.getPartCode()))){
//											int counr=dao.findCountByNotequals(po,map2.get("PART_CODE").toString());
											if(count1!=1){
												flag=true;
												break;
											}
										}
									}
									if(!flag){
										Double hypoMoney=dao.findhypoMoney(po);
										amount= Arith.add(amount,hypoMoney);
									}
								}
							}
						}
						
					}
					if(list!=null && list.size()>0){
						this.insertPo(amount, list,loginUser,"是");
					}
				}
			}else{//次因件
				Double amount=dao.findinStroehypoMoney(po);
				String wr_labourcode = dao.findinStroehypo(po);
				List<Map<String,Object>> partsitemCodes = dao.findPartsitem(wr_labourcode,po);
				if(partsitemCodes!=null&&partsitemCodes.size()==1){
					Double hypoMoney=dao.findhypoMoney(po);
					amount= Arith.add(amount,hypoMoney);
				}else{
					
					Double hypoMoney1=dao.findhypoMoney(po);
					amount= Arith.add(amount,hypoMoney1);
					int count=dao.findCountAllByPartCode(partsitemCodes,po);
					if(count==0){//两个次因件挂的同一个工时，并且这个工时没有挂主因件
						boolean flag=false;
						for (Map<String, Object> map2 : partsitemCodes) {
							if(!(map2.get("PART_CODE").toString().equals(po.getPartCode()))){
//								int counr=dao.findCountByNotequals(po,map2.get("PART_CODE").toString());
								if(count!=1){
									flag=true;
									break;
								}
							}
						}
						if(!flag){
							Double hypoMoney=dao.findhypoMoney(po);
							amount= Arith.add(amount,hypoMoney);
						}
					}
				}
				List<Map<String,Object>> list=dao.findInsertPoValues(id);
				if(list!=null && list.size()>0){
					this.insertPo(amount, list,loginUser,"是");
				}
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}
		return res;
	}
	@SuppressWarnings("unchecked")
	private String findinStroehypo(TtAsWrOldReturnedDetailPO po, String  part_code) {
		String res="";
		StringBuffer sb= new StringBuffer();
		sb.append("select p.wr_labourcode  from Tt_As_Wr_Partsitem p where p.part_code='"+part_code+"' and p.id="+po.getClaimId());
		List<Map<String,Object>> pageQuery = this.pageQuery(sb.toString(), null, getFunName());
		if(pageQuery!=null && pageQuery.size()>0){
			res=(String) pageQuery.get(0).get("WR_LABOURCODE");
		}
		return res;
	}
	@SuppressWarnings("unchecked")
	private List<Map<String,Object>> findhypoByMainCode(TtAsWrOldReturnedDetailPO po) {
		StringBuffer sb= new StringBuffer();
		sb.append("select  p.part_code from Tt_As_Wr_Partsitem p where p.id="+po.getClaimId()+" and p.main_part_code='"+po.getPartCode()+"'");
		return  this.pageQuery(sb.toString(), null, getFunName());
	}
	@SuppressWarnings("unchecked")
	private int findCountByNotequals(TtAsWrOldReturnedDetailPO po, String string) {
		int res=0;
		StringBuffer sb= new StringBuffer();
		sb.append("select count(*) as countAll from  tt_as_wr_old_returned_detail d where d.id="+po.getId() );
		sb.append(" and d.part_code='"+po.getPartCode()+"' and d.sign_amount<>d.return_amount");
		List<Map<String,Object>> pageQuery = this.pageQuery(sb.toString(), null, getFunName());
		if(pageQuery!=null && pageQuery.size()>0){
			BigDecimal b = (BigDecimal) pageQuery.get(0).get("COUNTALL");
			res=b.intValue();
		}
		return res;
	}
	@SuppressWarnings("unchecked")
	private int findCountAllByPartCode(List<Map<String, Object>> partsitemCodes,TtAsWrOldReturnedDetailPO po) {
		int res=0;
		StringBuffer sb= new StringBuffer();
		StringBuffer sub=new StringBuffer();
		int temp=0;
		for (Map<String, Object> map : partsitemCodes) {
			String part_code = (String) map.get("PART_CODE");
			if(temp==partsitemCodes.size()-1){
				sub.append(part_code);
			}else{
				sub.append(part_code+",");
			}
			temp++;
		}
		sb.append("select count(*) as countAll from Tt_As_Wr_Partsitem p where 1=1 and  (p.main_part_code='-1' or  p.main_part_code='无') and  p.id="+po.getClaimId());
		DaoFactory.getsql(sb, "p.part_code", sub.toString(), 6);
		List<Map<String,Object>> pageQuery = this.pageQuery(sb.toString(), null, getFunName());
		if(pageQuery!=null && pageQuery.size()>0){
			BigDecimal b = (BigDecimal) pageQuery.get(0).get("COUNTALL");
			res=b.intValue();
		}
		return res;
	}
	@SuppressWarnings("unchecked")
	private Double findhypoMoney(TtAsWrOldReturnedDetailPO po) {
		Double amount=0d;
		StringBuffer sb= new StringBuffer();
		sb.append("select nvl(l.labour_amount,0) as labour_amount  from Tt_As_Wr_Partsitem p,Tt_As_Wr_Labouritem l  where p.id=l.id and l.wr_labourcode=p.wr_labourcode and  p.part_code='"+po.getPartCode()+"' and p.id="+po.getId());
		List<Map<String,Object>> pageQuery = this.pageQuery(sb.toString(), null, getFunName());
		if(pageQuery!=null && pageQuery.size()>0){
			BigDecimal b = (BigDecimal) pageQuery.get(0).get("PART_AMOUNT");
			amount=b.doubleValue();
		}
		return amount;
	}
	@SuppressWarnings("unchecked")
	private List<Map<String,Object>> findPartsitem(String wr_labourcode,TtAsWrOldReturnedDetailPO po) {
		StringBuffer sb= new StringBuffer();
		sb.append("select p.part_code from Tt_As_Wr_Partsitem p where p.wr_labourcode='"+wr_labourcode+"' and p.id="+po.getId());
		List<Map<String,Object>> pageQuery = this.pageQuery(sb.toString(), null, getFunName());
		return pageQuery;
	}
	@SuppressWarnings("unchecked")
	private String findinStroehypo(TtAsWrOldReturnedDetailPO po) {
		String res="";
		StringBuffer sb= new StringBuffer();
		sb.append("select p.wr_labourcode  from Tt_As_Wr_Partsitem p where p.part_code='"+po.getPartCode()+"' and p.id="+po.getClaimId());
		List<Map<String,Object>> pageQuery = this.pageQuery(sb.toString(), null, getFunName());
		if(pageQuery!=null && pageQuery.size()>0){
			res=(String) pageQuery.get(0).get("WR_LABOURCODE");
		}
		return res;
	}
	@SuppressWarnings("unchecked")
	private Double findinStroehypoMoney(TtAsWrOldReturnedDetailPO po) {
		Double amount=0d;
		StringBuffer sb= new StringBuffer();
		sb.append("select\n" );
		sb.append("   (select nvl(p.amount/p.quantity, 0)\n" );
		sb.append("      from Tt_As_Wr_Partsitem p\n" );
		sb.append("     where p.id = d.claim_id\n" );
		sb.append("       and p.part_code = d.part_code) as part_amount\n" );
		sb.append("    from tt_as_wr_old_returned_detail d  where d.id="+po.getClaimId());
		List<Map<String,Object>> pageQuery = this.pageQuery(sb.toString(), null, getFunName());
		if(pageQuery!=null && pageQuery.size()>0){
			BigDecimal b = (BigDecimal) pageQuery.get(0).get("PART_AMOUNT");
			amount=b.doubleValue();
		}
		return amount;
	}
	@SuppressWarnings("unchecked")
	private Double findinStroeMainCodeMoneyAll(TtAsWrOldReturnedDetailPO po) {
		Double amount=0d;
		StringBuffer sb= new StringBuffer();
		sb.append("select sum(a.res_amount) as res_amount  from\n" );
		sb.append("(select (nvl(res.labour_amount, 0) + nvl(res.part_amount, 0) +\n" );
		sb.append("       nvl(res.other_amount, 0) + nvl(res.dtl_amount, 0) +\n" );
		sb.append("       nvl(res.COMPENSATION_amount, 0)) as res_amount\n" );
		sb.append("  from (select (select sum(nvl(l.labour_amount, 0))\n" );
		sb.append("                  from Tt_As_Wr_Labouritem l\n" );
		sb.append("                 where l.id = d.claim_id\n" );
		sb.append("                   and l.wr_labourcode in\n" );
		sb.append("                       (select p.wr_labourcode\n" );
		sb.append("                          from Tt_As_Wr_Partsitem p\n" );
		sb.append("                         where p.id = d.claim_id\n" );
		sb.append("                           and p.part_code = d.part_code)) as labour_amount,\n" );
		sb.append("               (select sum(nvl(p.amount, 0))\n" );
		sb.append("                  from Tt_As_Wr_Partsitem p\n" );
		sb.append("                 where p.id = d.claim_id\n" );
		sb.append("                   and p.part_code = d.part_code) as part_amount,\n" );
		sb.append("               (select sum(nvl(n.amount, 0))\n" );
		sb.append("                  from Tt_As_Wr_Netitem n\n" );
		sb.append("                 where n.id = d.claim_id\n" );
		sb.append("                   and n.item_code = d.part_code) as other_amount,\n" );
		sb.append("               (select sum(nvl(dtl.app_price, 0))\n" );
		sb.append("                  from tt_claim_accessory_dtl dtl\n" );
		sb.append("                 where dtl.claim_no = d.claim_no\n" );
		sb.append("                   and dtl.main_part_code = d.part_code) as dtl_amount,\n" );
		sb.append("               (select sum(nvl(c.apply_price, 0))\n" );
		sb.append("                  from TT_AS_WR_COMPENSATION_APP c\n" );
		sb.append("                 where c.claim_no = d.claim_no\n" );
		sb.append("                   and c.part_code = d.part_code) as compensation_amount\n" );
		sb.append("          from tt_as_wr_old_returned_detail d\n" );
		sb.append("         where d.id = "+po.getId()+") res ) a");
		List<Map<String,Object>> pageQuery = this.pageQuery(sb.toString(), null, getFunName());
		if(pageQuery!=null && pageQuery.size()>0){
			BigDecimal b = (BigDecimal) pageQuery.get(0).get("RES_AMOUNT");
			amount=b.doubleValue();
		}
		return amount;
	}
	@SuppressWarnings("unchecked")
	private void insertPo(Double amount, List<Map<String, Object>> list, AclUserBean loginUser, String isBc) {
		Map<String, Object> map = list.get(0);
		String claimId = BaseUtils.checkNull( map.get("CLAIM_ID"));
		String isMainCode = BaseUtils.checkNull( map.get("IS_MAIN_CODE"));
		String dealerId = BaseUtils.checkNull( map.get("DEALER_ID"));
		String dealerCode = BaseUtils.checkNull(map.get("DEALER_CODE"));
		String partCode = BaseUtils.checkNull( map.get("PART_CODE"));
		TtAsSecondInStoreDetailPO t=new TtAsSecondInStoreDetailPO();
		t.setId(DaoFactory.getPkId());
		t.setAmount(amount);
		t.setClaimId(claimId);
		t.setDealerCode(dealerCode);
		t.setDealerId(dealerId);
		t.setPartCode(partCode);
		t.setIsMainCode(isMainCode);
		t.setCreateDate(new Date());
		t.setIsBc(isBc);
		t.setCreateBy(loginUser.getUserId());
		this.insert(t);
	}
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> findInsertPoValues(String id) {
		StringBuffer sb= new StringBuffer();
		sb.append("select d.claim_id,\n" );
		sb.append("  (select a.dealer_id from Tt_As_Wr_Application a where a.id=d.claim_id) as  dealer_id,\n" );
		sb.append("    (select tm.dealer_code from tm_dealer tm where tm.dealer_id=(select a.dealer_id\n" );
		sb.append("         from Tt_As_Wr_Application a where a.id=d.claim_id)) as  dealer_code, d.part_code, d.id,d.is_main_code\n" );
		sb.append("  from tt_as_wr_old_returned_detail d where d.id="+id);
		return this.pageQuery(sb.toString(), null, getFunName());
	}
	/**
	 * 查询配件的钱
	 * @param po
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Double findinStroeMainCodeMoney(TtAsWrOldReturnedDetailPO po) {
		Double amount=0d;
		StringBuffer sb= new StringBuffer();
		sb.append("select (p.amount/p.quantity) as amount  from  tt_as_wr_partsitem p where p.part_code='"+po.getPartCode()+"' and p.id="+po.getId());
		List<Map<String,Object>> pageQuery = this.pageQuery(sb.toString(), null, getFunName());
		if(pageQuery!=null && pageQuery.size()>0){
			BigDecimal b = (BigDecimal) pageQuery.get(0).get("AMOUNT");
			amount=b.doubleValue();
		}
		return amount;
	}
	@SuppressWarnings("unchecked")
	private int findisMainCode(TtAsWrOldReturnedDetailPO po) {
		int res=0;
		StringBuffer sb= new StringBuffer();
		sb.append("select count(*) as countAll from tt_as_wr_old_returned_detail d where d.claim_id='"+po.getClaimId()+"'\n" );
		sb.append("and d.part_code='"+po.getPartCode()+"'\n" );
		sb.append("and d.return_amount=d.sign_amount");
		List<Map<String,Object>> pageQuery = this.pageQuery(sb.toString(), null, getFunName());
		if(pageQuery!=null && pageQuery.size()>0){
			BigDecimal b = (BigDecimal) pageQuery.get(0).get("COUNTALL");
			res=b.intValue();
		}
		return res;
	}
	/**
	 * 插入二次入库数据 zyw 2014-9-5 
	 * @param list
	 * @param type
	 */
	@SuppressWarnings("unchecked")
	private void insertPoBytype(List<Map<String, Object>> list,int type) {
		Map<String, Object> map = list.get(0);
		Double amount =0.0;
		if(type==1){
			 BigDecimal res=  (BigDecimal) map.get("RES_AMOUNT");
			 amount=res.doubleValue();
		}else{
			BigDecimal res=  (BigDecimal) map.get("PART_AMOUNT");
			amount=res.doubleValue();
		}
		String claimId = BaseUtils.checkNull( map.get("CLAIM_ID"));
		String isMainCode = BaseUtils.checkNull( map.get("MAIN_PART_CODE"));
		String dealerId = BaseUtils.checkNull( map.get("DEALER_ID"));
		String dealerCode = BaseUtils.checkNull(map.get("DEALER_CODE"));
		String partCode = BaseUtils.checkNull( map.get("PART_CODE"));
		TtAsSecondInStoreDetailPO po=new TtAsSecondInStoreDetailPO();
		po.setId(DaoFactory.getPkId());
		po.setAmount(amount);
		po.setClaimId(claimId);
		po.setDealerCode(dealerCode);
		po.setDealerId(dealerId);
		po.setPartCode(partCode);
		po.setIsMainCode(isMainCode);
		dao.insert(po);
	}
	/**
	 * 汇总出库查询
	 * @param companyId
	 * @param request
	 * @param currPage
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> queryPreOutStoreListByMix(Long companyId, RequestWrapper request, Integer pageSize,Integer currPage) {
		String supply_name=DaoFactory.getParam(request,"supply_name");
		String supply_code=DaoFactory.getParam(request,"supply_code");
		String dealer_code=DaoFactory.getParam(request,"dealer_code");
		String dealer_name=DaoFactory.getParam(request,"dealer_name");
		String part_code=DaoFactory.getParam(request,"part_code");
		String part_name=DaoFactory.getParam(request,"part_name");
//		String model_code=DaoFactory.getParam(request,"model_code");
//		String model_name=DaoFactory.getParam(request,"model_name");
		String yieldly = DaoFactory.getParam(request,"yieldly");
		String claim_no=DaoFactory.getParam(request,"claim_no");
//		String page_amount=DaoFactory.getParam(request,"page_amount");
		String in_start_date = DaoFactory.getParam(request, "in_start_date");
		String in_end_date=DaoFactory.getParam(request, "in_end_date");
		
		
		StringBuffer sb= new StringBuffer();
		sb.append("select c.part_code,\n" );
		sb.append("       (select b.part_name\n" );
		sb.append("          from TM_PT_PART_BASE b\n" );
		sb.append("         where b.part_code = c.part_code) as PART_NAME,\n" );
		sb.append("       c.SUPPLY_CODE,\n" );
		sb.append("       (select min(m.MAKER_SHOTNAME)\n" );
		sb.append("          from tt_part_maker_define m, tt_part_maker_relation r\n" );
		sb.append("         where r.maker_id = m.maker_id\n" );
		sb.append("           and m.maker_code = c.SUPPLY_CODE) as SUPPLY_name,\n" );
		sb.append("       sum(c.retrun_amount) retrun_amount,\n" );
		sb.append("       sum(c.no_retrun_amount) no_return_amount,  sum(nvl(countPart,0)) countPart\n" );
		sb.append("  from (\n" );
		sb.append(findInfoByPartCodeAndSupplyCode(supply_name, supply_code,
				dealer_name, dealer_code, part_code, part_name, claim_no,
				in_start_date, in_end_date,yieldly));
		sb.append(" ) c\n" );
		sb.append(" group by c.part_code, c.SUPPLY_CODE\n" );
		sb.append(" order by c.part_code");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	@SuppressWarnings("unchecked")
	public PageResult<ClaimOldPartOutPreListBean> getPreOutStoreList(RequestWrapper request, Integer currPage) {
		
		String supply_name=DaoFactory.getParam(request, "supply_name");
		String supply_code=DaoFactory.getParam(request, "supply_code");
		String dealer_code=DaoFactory.getParam(request, "dealer_code");
		String dealer_name=DaoFactory.getParam(request, "dealer_name");
		String part_code=DaoFactory.getParam(request, "part_code");
		String part_name=DaoFactory.getParam(request, "part_name");
//		String model_code=DaoFactory.getParam(request, "model_code");
//		String model_name=DaoFactory.getParam(request, "model_name");
		String yieldly = DaoFactory.getParam(request, "yieldly");
		String claim_no=DaoFactory.getParam(request, "claim_no");
		String in_start_date = DaoFactory.getParam(request, "in_start_date");
		String in_end_date=DaoFactory.getParam(request, "in_end_date");
		
		String page_amount=DaoFactory.getParam(request, "page_amount");
		String sql = findInfoByPartCodeAndSupplyCode(supply_name, supply_code,
				dealer_name, dealer_code, part_code, part_name, claim_no,
				in_start_date, in_end_date,yieldly);
		return pageQuery(ClaimOldPartOutPreListBean.class, sql.toString(), null, Integer.parseInt(page_amount), currPage);
	}
	/**
	 * 查询实件返厂的需出库明细
	 * @param request
	 * @param currPage
	 * @return
	 * @author chenyub@yonyou.com
	 * 2015年12月14日  下午3:20:15
	 */
	@SuppressWarnings("unchecked")
	public PageResult<ClaimOldPartOutPreListBean> getReturnPreOutStoreList(RequestWrapper request,Integer pageSize,Integer curPage) {

		String yieldly = DaoFactory.getParam(request, "yieldly");
		String supply_name = DaoFactory.getParam(request, "supply_name");
		String supply_code = DaoFactory.getParam(request, "SUPPLY_CODE");
		String partCode = DaoFactory.getParam(request, "part_code");
		String partName = DaoFactory.getParam(request, "part_name");
		String claim_no = DaoFactory.getParam(request, "claim_no");
		String dealerCode = DaoFactory.getParam(request, "dealerCode");
		String part_type = DaoFactory.getParam(request, "part_type");
		String indate_s = DaoFactory.getParam(request, "indate_s");
		String indate_e = DaoFactory.getParam(request, "indate_e");
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from (");
		sql.append(partReturnSql1(supply_name, supply_code, null, dealerCode,
				partCode, partName, claim_no, indate_s, indate_e, yieldly));
		sql.append(" union all ");
		sql.append(partReturnSql2(supply_name, supply_code, null, dealerCode,
				partCode, partName, claim_no, indate_s, indate_e, yieldly));
		sql.append(" ) rtsql  ");
		sql.append(" where 1=1 ");
		if(StringUtils.isNotEmpty(part_type)){
			sql.append(" and rtsql.qhj_flag='").append(part_type).append("' ");
		}
		return pageQuery(ClaimOldPartOutPreListBean.class, sql.toString(), null, pageSize, curPage);
	}	
	
	/**
	 * 查询无件返厂的需出库明细
	 * @param request
	 * @param currPage
	 * @return
	 * @author chenyub@yonyou.com
	 * 2015年12月14日  下午3:20:15
	 */
	@SuppressWarnings("unchecked")
	public PageResult<ClaimOldPartOutPreListBean> getNoReturnPreOutStoreList(RequestWrapper request,Integer pageSize,Integer curPage) {

		String yieldly = DaoFactory.getParam(request, "yieldly");
		String supply_name = DaoFactory.getParam(request, "supply_name");
		String supply_code = DaoFactory.getParam(request, "SUPPLY_CODE");
		String partCode = DaoFactory.getParam(request, "part_code");
		String partName = DaoFactory.getParam(request, "part_name");
		String claim_no = DaoFactory.getParam(request, "claim_no");
		String dealerCode = DaoFactory.getParam(request, "dealerCode");
		String part_type = DaoFactory.getParam(request, "part_type");
		String indate_s = DaoFactory.getParam(request, "indate_s");
		String indate_e = DaoFactory.getParam(request, "indate_e");
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from (");
		sql.append(partNoReturnSql(supply_name, supply_code, null, dealerCode,
				partCode, partName, claim_no, indate_s, indate_e, yieldly));
		sql.append(" union all ");
		sql.append(partSpecialSql(supply_name, supply_code, null, dealerCode,
				partCode, partName, claim_no, indate_s, indate_e, yieldly));
		sql.append(" ) rtsql  ");
		sql.append(" where 1=1 ");
		if(StringUtils.isNotEmpty(part_type)){
			sql.append(" and rtsql.qhj_flag='").append(part_type).append("' ");
		}
		return pageQuery(ClaimOldPartOutPreListBean.class, sql.toString(), null, pageSize, curPage);
	}
	/**
	 * 汇总出库
	 * @param request
	 * @param loginUser
	 * @param rangeNo 
	 * @return
	 */
	@SuppressWarnings("finally")
	public int doChangeByVal(RequestWrapper request, AclUserBean loginUser, String rangeNo) {
		int res=1;
		try {
			String val = DaoFactory.getParam(request, "val");
			String[] vals = StringUtils.split(val,",");
			//String no = GenerateStockNo();//生产出库单号 
//			String noType = request.getParamValue("noType");//出库方式 (0混合出库/1分单号出库)
//			String isHs = request.getParamValue("isHs");//is_house
			String outType = request.getParamValue("OUT_CLAIM_TYPE");//出库类型
			String no =rangeNo;
			// 2015.11.03增加日期的限制
			String in_start_date=request.getParamValue("in_start_date");
			String in_end_date = request.getParamValue("in_end_date");
			for (String params : vals) {
				String[] param = StringUtils.split(params,"_");
				if(param!=null && param.length>0){
					String partCode = param[0];
					String supplyCode = param[1];
					String return_num = param[2];
					String no_return_num = param[3];
					if(!"0".equals(return_num)){
						List<Map<String, Object>> listInfo=//this.findInfoByPartCodeAndSupplyCode(partCode,supplyCode,"1");
								this.findSupplierNew(partCode, supplyCode, "1", in_start_date, in_end_date);
						if(listInfo!=null && listInfo.size()>0){
							int temp=0;
							for (Map<String, Object> map : listInfo) {
								String CLAIM_NO = map.get("CLAIM_NO").toString();
								String PART_CODE = map.get("PART_CODE").toString();
								String PART_NAME = map.get("PART_NAME").toString();
								String SUPPLY_CODE = map.get("SUPPLY_CODE").toString();
								String SUPPLY_NAME = map.get("SUPPLY_NAME").toString();
								String RETURN_AMOUNT = map.get("RETRUN_AMOUNT").toString();
								String PART_RETURN = "1";
								int return_numTemp = Integer.parseInt(return_num);
								if(!StringUtils.equals(partCode, PART_CODE)){
									continue;
								}
								if(return_numTemp==temp){
									break;
								}else{
									String allAmount ="1";//出库数
							    	//res = doInsertStore(request, loginUser, CLAIM_NO,PART_CODE, PART_NAME, SUPPLY_CODE,SUPPLY_NAME, PART_RETURN,no,rangeNo,allAmount);
									for(int i=0;i<Integer.parseInt(RETURN_AMOUNT);i++){
										if(return_numTemp==temp){
											break;
										}
										res = doInsertStore(request, loginUser, CLAIM_NO,PART_CODE, PART_NAME, SUPPLY_CODE,SUPPLY_NAME, PART_RETURN,no,rangeNo,allAmount);
										temp++;
									}
								}
							//	temp++;
							}
							//this.callRangeSingele(loginUser, no, outType);
						}
					}
			    	if(!"0".equals(no_return_num)){
			    		List<Map<String, Object>> listInfo=//this.findInfoByPartCodeAndSupplyCode(partCode,supplyCode,"0");
						this.findSupplierNew(partCode, supplyCode, "0", in_start_date, in_end_date);
			    		if(listInfo!=null && listInfo.size()>0){
							int temp=0;
							for (Map<String, Object> map : listInfo) {
								int return_numTemp = Integer.parseInt(no_return_num);
								String CLAIM_NO = map.get("CLAIM_NO").toString();
								String PART_CODE = map.get("PART_CODE").toString();
								String PART_NAME = map.get("PART_NAME").toString();
								String SUPPLY_CODE = map.get("SUPPLY_CODE").toString();
								String SUPPLY_NAME = map.get("SUPPLY_NAME").toString();
								String PART_RETURN = "0";
								if(return_numTemp==temp){
									break;
								}else{
									String allAmount ="1";//出库数
									res =doInsertStore(request, loginUser, CLAIM_NO,PART_CODE, PART_NAME, SUPPLY_CODE,SUPPLY_NAME, PART_RETURN,no,rangeNo,allAmount);
								}
								temp++;
							}
						}
			    	}
				}
				//this.delete("delete from range_temp t where t.out_no='"+no+"'",null);
				//this.callRangeSingele(loginUser, no, outType);
			}
			this.callRangeSingele(loginUser, no, outType);
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally{
			return res;
		}
	}
	/**
	 * 以前逻辑
	 * @param request
	 * @param loginUser
	 * @param CLAIM_NO
	 * @param PART_CODE
	 * @param PART_NAME
	 * @param SUPPLY_CODE
	 * @param SUPPLY_NAME
	 * @param PART_RETURN
	 * @param no 
	 */
	@SuppressWarnings("unchecked")
	private int doInsertStore(RequestWrapper request, AclUserBean loginUser,
			String claimNo, String partCode,
			String partName,String supplyCode, 
			String supplyName, String partReturn, 
			String no,String rangeNo,String allAmount) {
			int res=1;
			try {
//				String noType = request.getParamValue("noType");//出库方式 (0混合出库/1分单号出库)
//				String isHs = request.getParamValue("isHs");//is_house
				String outType = request.getParamValue("OUT_CLAIM_TYPE");//出库类型
				String yieldly = request.getParamValue("yieldly");//基地
				String relationOutNo = request.getParamValue("relationOutNo");//关联退赔单号
				if("-1".equalsIgnoreCase(relationOutNo)||"".equalsIgnoreCase(relationOutNo)){
					relationOutNo=null;
				}
					/**
					 * 保存出库明细
					 */
					TtAsWrOldOutPartPO pp = new TtAsWrOldOutPartPO();
					pp.setClaimNo(claimNo);
					pp.setCreateBy(loginUser.getUserId());
					pp.setCreateDate( new Date());
					pp.setId(Long.parseLong(SequenceManager.getSequence("")));
					pp.setOutAmout(Integer.parseInt(allAmount));
					pp.setOutBy(loginUser.getUserId());
					pp.setOutDate( new Date());
					pp.setOutNo(no);
					pp.setOutPartCode(partCode);
					pp.setOutPartName(partName);
					pp.setOutType(Integer.parseInt(outType));
					pp.setRemark(null);
					pp.setSupplyCode(supplyCode);
					pp.setSupplyName(supplyName);
					pp.setYieldly(Integer.parseInt(yieldly));
					pp.setOutPartType(Integer.parseInt(partReturn));
					pp.setRelationalOutNo(relationOutNo);
					pp.setRangeNo(rangeNo);
					int outFlag = checkOutPartIsQHJ(claimNo,partCode);
					if(outFlag!=-1){
	    				pp.setOutFlag(outFlag);
	    				if(outFlag==1){//如果是只出工时,则设置为无件出库
	    					partReturn = "0";
	    					pp.setOutPartType(0);
	    				}
	    			}
					
					//得到真实配件代码的ID
					TtPartDefinePO d  = new TtPartDefinePO();
					d.setPartOldcode(partCode);
					d = (TtPartDefinePO) this.select(d).get(0);
					if(Constant.OUT_CLAIM_TYPE_01.toString().equals(outType)){//出库类型=供应商开票
						String partCode2 = "";
						String partCode3 = "";
						String partCode4 = "";
						if(partCode.length()==18){
							//17010510-C01-B00-3 18位的包含B00的件取17010510-C01-000-3的价格
							if("B00".equals(partCode.substring(13,16))){
								partCode2 = partCode.substring(0, 13)+"000"+partCode.substring(16, 18);
								partCode3 = partCode2;
								partCode4 = partCode2;
							}else{
								partCode2 = partCode.substring(0, partCode.length()-3)+"000";
								partCode3 = partCode.substring(0, partCode.length()-3)+"B00";
								partCode4 = partCode.substring(0, partCode.length()-3)+"B0Y";
							}
						}else{
							partCode2 = partCode.substring(0, partCode.length()-3)+"000";
							partCode3 = partCode.substring(0, partCode.length()-3)+"B00";
							partCode4 = partCode.substring(0, partCode.length()-3)+"B0Y";
						}
						StringBuffer sql = new StringBuffer();
						//将配件的最后3为分别替换
						sql.append("SELECT max(r.part_id) part_id,max(r.maker_id) maker_id  FROM tt_part_maker_relation r\n");
						sql.append("WHERE r.part_id  in (SELECT d.part_id FROM tt_part_define d WHERE d.part_oldcode in ('"+partCode+"','"+partCode2+"','"+partCode3+"','"+partCode4+"'))\n");
						sql.append("AND r.maker_id = (SELECT md.maker_id  FROM tt_part_maker_define md WHERE md.maker_code='"+supplyCode+"') group by part_id,maker_id"); 
						List<Map<String,Object>> lit = this.pageQuery(sql.toString(), null, this.getFunName());
						if(lit!=null&&lit.size()>0){
							TtPartMakerRelationPO rp = new TtPartMakerRelationPO();
							rp.setPartId(Long.valueOf(String.valueOf(lit.get(0).get("PART_ID"))));
							rp.setMakerId(Long.valueOf(String.valueOf(lit.get(0).get("MAKER_ID"))));
							rp = (TtPartMakerRelationPO) this.select(rp).get(0);
							String insql = "select * from range_temp t WHERE t.out_no='"+no+"' AND t.part_id="+d.getPartId()+" AND t.maker_id ="+rp.getMakerId()+"";
							List<Map<String,Object>> list = this.pageQuery(insql, null, this.getFunName());
							
							if(list==null||list.size()<1){
								String sqll = "INSERT INTO range_temp VALUES(f_getid(),'"+no+"',"+d.getPartId()+","+rp.getMakerId()+","+loginUser.getUserId()+","+rp.getClaimPrice()+","+rp.getPartXs()+","+rp.getLabourXs()+")";
								this.insert(sqll);
							}
						}else{//如果没找到显示关系对应的价格，则取其他供应商的最大价格
							sql.delete(0, sql.toString().length());
							sql.append("select Max(claim_price) claim_price,max(PART_XS) PART_XS,MAX(LABOUR_XS) LABOUR_XS,part_id\n");
							sql.append("  from tt_part_maker_relation\n");
							sql.append(" where part_id in\n");
							sql.append("       (select part_id\n");
							sql.append("          from tt_part_define\n");
							sql.append("         where part_oldcode in ('"+partCode+"','"+partCode2+"','"+partCode3+"','"+partCode4+"'))\n");
							sql.append(" group by part_id"); 
							List<Map<String,Object>> tList = this.pageQuery(sql.toString(), null, this.getFunName());
							if(null != tList && tList.size()>0){
								TtPartMakerDefinePO mDefine = new TtPartMakerDefinePO();
								mDefine.setMakerCode(supplyCode);
								List<TtPartMakerDefinePO> makerDefineList = dao.select(mDefine);
								TtPartMakerDefinePO tmpMaker = makerDefineList.get(0);
								String sqll = "INSERT INTO range_temp VALUES(f_getid(),'"+no+"',"+d.getPartId()+","+tmpMaker.getMakerId()+","+loginUser.getUserId()+","+tList.get(0).get("CLAIM_PRICE")+","+tList.get(0).get("PART_XS")+","+tList.get(0).get("LABOUR_XS")+")";
								this.insert(sqll);
							}
							//new Exception("没有找到供应商配件对应关系!");
						}
					}
					
					
					//此处不执行插入,如果是误判时,不操作无实物件以及特殊单
					//(1实物返件,0无件返厂2特殊费用)
					if("1".equalsIgnoreCase(partReturn)){//如果选择的是旧件回运单中的数据,则要进行相应的操作。如果是不回运或者维修的的单子则直接进行
						this.insert(pp); 
					/**
					 * 选择的主因件出库保存后,还要查询其次因件同时将其修改为出库状态并加入出库明细
					 */
					StringBuffer sqlStr = new StringBuffer();
					sqlStr.append("SELECT d.claim_no,d.part_code,d.part_name,SUM(d.sign_amount)  amount\n");
					sqlStr.append("FROM Tt_As_Wr_Old_Returned_Detail d\n");
					sqlStr.append("WHERE 1=1\n");
					sqlStr.append("AND d.is_main_code="+Constant.RESPONS_NATURE_STATUS_02+"\n");
					sqlStr.append("AND d.is_out=0 AND d.is_cliam=0 and d.is_in_house="+Constant.IF_TYPE_YES+"\n");
					sqlStr.append("AND d.sign_amount>0\n");
					sqlStr.append("AND d.main_part_code='"+partCode+"'\n");
					sqlStr.append(" AND D.claim_no='"+claimNo+"'\n");
					sqlStr.append(" GROUP BY d.claim_no,d.part_code,d.part_name"); 
					List<Map<String,Object>> resList = this.pageQuery(sqlStr.toString(), null, this.getFunName());
					if(resList!=null && resList.size()>0){//如果查询结果集有数据,说明该件下含有次因件，此时就将这些次因件也出库，并且插入到出库明细中
						for(int k=0;k<resList.size();k++){
							StringBuffer sqlr = new StringBuffer();//更新次因件的出库状态
							sqlr.append(" update tt_as_wr_old_returned_detail d set d.is_out=1,d.is_cliam='1' WHERE d.claim_no='"+resList.get(k).get("CLAIM_NO")+"' AND d.is_out in(0,2) AND d.part_code='"+resList.get(k).get("PART_CODE")+"' AND d.sign_amount>0");
							this.update(sqlr.toString(), null);
							//开始保存次因件的出库记录
							TtAsWrOldOutPartPO pp2 = new TtAsWrOldOutPartPO();
							pp2.setClaimNo(resList.get(k).get("CLAIM_NO").toString());
							pp2.setCreateBy(loginUser.getUserId());
							pp2.setCreateDate( new Date());
							pp2.setId(Long.parseLong(SequenceManager.getSequence("")));
							pp2.setOutAmout(Integer.parseInt(resList.get(k).get("AMOUNT").toString()));
							pp2.setOutBy(loginUser.getUserId());
							pp2.setOutDate( new Date());
							pp2.setOutNo(no);
							pp2.setOutPartCode(resList.get(k).get("PART_CODE").toString());
							pp2.setOutPartName(resList.get(k).get("PART_NAME").toString());
							pp2.setOutType(Integer.parseInt(outType));
							pp2.setRemark(partCode+"责任连带");
							pp2.setYieldly(Integer.parseInt(yieldly));
							pp2.setOutPartType(Integer.parseInt(partReturn));
							pp2.setRelationalOutNo(relationOutNo);
							pp2.setRangeNo(rangeNo);
							pp2.setSupplyCode(supplyCode);
							pp2.setSupplyName(supplyName);
							this.insert(pp2); 
						}
					}
					 /**
					  * 如果是误判出库,则还要将二次抵扣数据保存。
					  */
					 if(Constant.OUT_CLAIM_TYPE_03.toString().equals(outType)){//误判=出库类型
						 
						 TtAsWrApplicationPO ap = new TtAsWrApplicationPO();
						 ap.setClaimNo(claimNo);
						 ap = (TtAsWrApplicationPO) this.select(ap).get(0);
						 
						//得到选择的抵扣件相关信息
						 StringBuffer ss = new StringBuffer();
						 ss.append("SELECT  L.WR_LABOURCODE,L.WR_LABOURNAME,L.LABOUR_AMOUNT,P.DOWN_PART_CODE,P.DOWN_PART_NAME,P.QUANTITY,P.PRICE,P.AMOUNT\n");
						 ss.append("FROM TT_AS_WR_PARTSITEM P ,TT_AS_WR_LABOURITEM L\n");
						 ss.append("WHERE L.ID = P.ID AND P.ID="+ap.getId()+" AND P.PART_CODE='"+partCode+"' AND P.WR_LABOURCODE = L.WR_LABOURCODE"); 
						 List<Map<String,String>> sList = this.pageQuery(ss.toString(), null, getFunName());
						 
						 
						 TtAsWrDiscountPO ddp = new TtAsWrDiscountPO();
						 ddp.setClaimId(ap.getId());
						 ddp.setClaimNo(ap.getClaimNo());
						 ddp.setCreateBy(loginUser.getUserId());
						 ddp.setCreateDate(new Date());
						 ddp.setDealerId(ap.getDealerId());
						 ddp.setDiscountDate(new Date());
						 ddp.setDeductReson(null);
						 ddp.setDownPartCode(partCode);
						 ddp.setDownPartName(partName);
						 //查询供应商ID
						 ddp.setDownProductCode(supplyCode);
						 ddp.setDownProductName(supplyName);
						 ddp.setId(Long.valueOf(SequenceManager.getSequence("")));
						 //查询工时
						 ddp.setWrLabourcode(sList.get(0).get("WR_LABOURCODE"));
						 ddp.setWrLabourname(sList.get(0).get("WR_LABOURNAME"));
						 
						 /**
						  * 通过迭代查询出,与该配件有关的次因件对应的所有工时,
						  */
						 StringBuffer laSql = new StringBuffer();
						 laSql.append("SELECT l.labour_id,l.wr_labourcode,l.wr_labourname,l.labour_amount FROM  Tt_As_Wr_Labouritem l\n");
						 laSql.append("WHERE l.wr_labourcode IN (\n");
						 laSql.append("SELECT  p.wr_labourcode FROM Tt_As_Wr_Partsitem p\n");
						 laSql.append("WHERE 1=1  AND p.ID="+ap.getId()+" AND p.responsibility_type="+Constant.RESPONS_NATURE_STATUS_02+"\n");
						 laSql.append("START WITH p.ID="+ap.getId()+" AND p.part_code = '"+partCode+"'\n");
						 laSql.append("CONNECT BY PRIOR  p.down_part_code = p.main_part_code\n");
						 laSql.append(") AND l.ID = "+ap.getId()+""); 
						 List<Map<String,String>> list = this.pageQuery(laSql.toString(), null, getFunName());
						
						 //判断索赔单状态,如果是结算审核中,则直接操作子表数据。如果不是结算审核中，则直接扣掉服务站的材料费和工时费
						 if(ap.getStatus().intValue()==Constant.CLAIM_APPLY_ORD_TYPE_08.intValue()){
							 ddp.setDiscountPriec(0.0);
							 ddp.setDiscountSum(Integer.parseInt(allAmount));
							 ddp.setDiscount(0.0);
							 ddp.setLabourPrice(0.0);
							 if(list !=null && list.size()>0){//如果有数据结果集
								 for(int l =0; l<list.size();l++){//循环处理
									 /**
									  * 第一步：将工时对应的价格扣完
									  * 第二步：将工时下面的所有配件进行扣除,并将有旧件的设置为出库状态
									  * 第三步：将关联件也保存为抵扣明细
									  */
									 String ll = " UPDATE Tt_As_Wr_Labouritem l SET l.apply_amount=0,l.balance_amount=0,l.apply_quantity=0,l.balance_quantity=0 WHERE l.labour_id="+list.get(l).get("LABOUR_ID")+" AND l.apply_amount>0";
									 this.update(ll, null);
									 String pl = "UPDATE Tt_As_Wr_Partsitem p SET  p.apply_quantity=0,p.apply_amount=0,p.balance_amount=0,p.balance_quantity=0 WHERE p.ID="+ap.getId()+" AND p.wr_labourcode='"+list.get(l).get("WR_LABOURCODE")+"' AND p.apply_amount>0";
									 this.update(pl, null);
									 String ol = "UPDATE Tt_As_Wr_Old_Returned_Detail d SET d.is_out=1,d.is_cliam=1  WHERE d.claim_id ="+ap.getId()+" AND d.part_id IN (SELECT p.part_id FROM Tt_As_Wr_Partsitem p WHERE p.ID="+ap.getId()+" AND d.is_out in(0,2) AND p.wr_labourcode='"+list.get(l).get("WR_LABOURCODE")+"' AND  p.part_code NOT IN('"+partCode+"'))";
									 this.update(ol, null);
									 
									 String ppl = "select P.PART_ID,P.QUANTITY,P.AMOUNT,P.PRICE,P.DOWN_PART_CODE,P.DOWN_PART_NAME,P.DOWN_PRODUCT_CODE,P.DOWN_PRODUCT_NAME from Tt_As_Wr_Partsitem p where p.id="+ap.getId()+" and p.wr_labourcode='"+list.get(l).get("WR_LABOURCODE")+"' AND  p.part_code NOT IN('"+partCode+"')";
									 List<Map<String,String>> pList = this.pageQuery(ppl.toString(), null, getFunName());
									 if(pList!=null && pList.size()>0){//该工时下面的所有配件进行抵扣明细保存
										 for(int p=0;p<pList.size();p++){
											 //保存抵扣明细
				    						 TtAsWrDiscountPO dip = new TtAsWrDiscountPO();
				        	    			 dip.setClaimId(ap.getId());
				        	    			 dip.setClaimNo(ap.getClaimNo());
				        	    			 dip.setCreateBy(loginUser.getUserId());
				        	    			 dip.setCreateDate(new Date());
				        	    			 dip.setDealerId(ap.getDealerId());
				        	    			 dip.setDiscountDate(new Date());
				        	    			 dip.setDeductReson(partCode+"连带抵扣");
				        	    			 dip.setDiscountPriec(0.0);
				        	    			 dip.setDiscountSum(Integer.parseInt(String.valueOf(pList.get(p).get("QUANTITY"))));
				        	    			 dip.setDiscount(0.0);
				        	    			 dip.setDownPartCode(pList.get(p).get("DOWN_PART_CODE"));
				        	    			 dip.setDownPartName(pList.get(p).get("DOWN_PART_NAME"));
				        	    			 //查询供应商ID
				        	    			 dip.setDownProductCode(pList.get(p).get("DOWN_PRODUCT_CODE"));
				        	    			 dip.setDownProductName(pList.get(p).get("DOWN_PRODUCT_NAME"));
				        	    			 dip.setId(Long.valueOf(SequenceManager.getSequence("")));
				        	    			 //查询工时
				        	    			 dip.setWrLabourcode(list.get(l).get("WR_LABOURCODE"));
				        	    			 dip.setWrLabourname(list.get(l).get("WR_LABOURNAME"));
				        	    			 dip.setLabourPrice(0.0);
				        	    			 this.insert(dip);
										 }
									 }
								 }
							 }
						 }else{
							 ddp.setDiscountPriec(Double.valueOf(String.valueOf(sList.get(0).get("PRICE"))));
							 ddp.setDiscountSum(Integer.parseInt(allAmount));
							 ddp.setDiscount(Double.valueOf(String.valueOf(sList.get(0).get("AMOUNT"))));
							 ddp.setLabourPrice(Double.valueOf(String.valueOf(sList.get(0).get("LABOUR_AMOUNT"))));
							 if(list !=null && list.size()>0){//如果有数据结果集
								 for(int l =0; l<list.size();l++){//循环处理
									 /**
									  * 第一步：由于索赔单已经结算了,所以现在只需要将相关抵扣信息保存明细
									  * 第二步：将抵扣的旧件设置已经出库状态
									  */
									 String ol = "UPDATE Tt_As_Wr_Old_Returned_Detail d SET d.is_out=1,d.is_cliam=1  WHERE d.claim_id ="+ap.getId()+" AND d.part_id IN (SELECT p.part_id FROM Tt_As_Wr_Partsitem p WHERE p.ID="+ap.getId()+"  AND p.wr_labourcode='"+list.get(l).get("WR_LABOURCODE")+"' AND d.is_out in(0,2) AND  p.part_code NOT IN('"+partCode+"'))";
									 this.update(ol, null);
									 
									 String ppl = "select P.PART_ID,P.QUANTITY,P.AMOUNT,P.PRICE,P.DOWN_PART_CODE,P.DOWN_PART_NAME,P.DOWN_PRODUCT_CODE,P.DOWN_PRODUCT_NAME from Tt_As_Wr_Partsitem p where p.id="+ap.getId()+" and p.wr_labourcode='"+list.get(l).get("WR_LABOURCODE")+"' AND  p.part_code NOT IN('"+partCode+"')";
									 List<Map<String,String>> pList = this.pageQuery(ppl.toString(), null, getFunName());
									 if(pList!=null && pList.size()>0){//该工时下面的所有配件进行抵扣明细保存
										 for(int p=0;p<pList.size();p++){
											 //保存抵扣明细
				    						 TtAsWrDiscountPO dip = new TtAsWrDiscountPO();
				        	    			 dip.setClaimId(ap.getId());
				        	    			 dip.setClaimNo(ap.getClaimNo());
				        	    			 dip.setCreateBy(loginUser.getUserId());
				        	    			 dip.setCreateDate(new Date());
				        	    			 dip.setDealerId(ap.getDealerId());
				        	    			 dip.setDiscountDate(new Date());
				        	    			 dip.setDeductReson(partCode+"连带抵扣");
				            	    		 dip.setDiscountPriec(Double.valueOf(String.valueOf(pList.get(p).get("PRICE"))));
				        	    			 dip.setDiscountSum(Integer.parseInt(String.valueOf(pList.get(p).get("QUANTITY"))));
				        	    			 dip.setDiscount(Double.valueOf(String.valueOf(pList.get(p).get("AMOUNT"))));
				        	    			 dip.setDownPartCode(pList.get(p).get("DOWN_PART_CODE").toString());
				        	    			 dip.setDownPartName(pList.get(p).get("DOWN_PART_NAME").toString());
				        	    			 //查询供应商ID
				        	    			 dip.setDownProductCode(pList.get(p).get("DOWN_PRODUCT_CODE").toString());
				        	    			 dip.setDownProductName(pList.get(p).get("DOWN_PRODUCT_NAME").toString());
				        	    			 dip.setId(Long.valueOf(SequenceManager.getSequence("")));
				        	    			 //查询工时
				        	    			 dip.setWrLabourcode(list.get(l).get("WR_LABOURCODE"));
				        	    			 dip.setWrLabourname(list.get(l).get("WR_LABOURNAME"));
				        	    			 dip.setLabourPrice(Double.valueOf(String.valueOf(list.get(l).get("LABOUR_AMOUNT"))));
				        	    			 this.insert(dip);
										 }
										 }
									 }
								 }
							 }
						 this.insert(ddp);
						 }
					 	/**
						 * 修改旧件回运明细的旧件状态
						 */
						StringBuffer sql = new StringBuffer();
						sql.append(" update tt_as_wr_old_returned_detail d set d.is_out=1,d.is_cliam='1' WHERE d.claim_no='"+claimNo+"' AND d.part_code='"+partCode+"' AND d.sign_amount>0 AND d.IS_OUT IN(0,2) AND ROWNUM=1 ");
						this.update(sql.toString(), null);
						/**
						 * 如果索赔单已经枷锁，则将其释放掉
						 */
						 StringBuffer sqlStr2 = new StringBuffer();
						 sqlStr2.append("update tt_as_wr_application t set t.verseon=null where t.claim_no='"+claimNo+"'\n");
						 this.update(sqlStr2.toString(), null);
					 }else if("0".equalsIgnoreCase(partReturn)&& !Constant.OUT_CLAIM_TYPE_03.toString().equals(outType)){//如果选择的是不返件或者是维修的索赔单,并且出库类型选择的不是 误判
						 this.insert(pp); //执行主因件出库明细插入
						 
						 //更新索赔单配件字表
						 StringBuffer sql = new StringBuffer();
							sql.append(" update TT_AS_WR_PARTSITEM p set p.is_notice="+Constant.IF_TYPE_YES+" ,p.Notice_Date=sysdate WHERE  p.ID IN (SELECT ID FROM Tt_As_Wr_Application a WHERE a.claim_no='"+claimNo.trim()+"') AND p.down_part_code = '"+partCode.trim().toUpperCase()+"'");
							this.update(sql.toString(), null);
							List<Map<String,Object>> list = this.getpartList(partCode, claimNo);
							if(list!=null && list.size()>0){//如果查询结果集有数据,说明该件下含有次因件，此时就将这些次因件也出库，并且插入到出库明细中
								for(int k=0;k<list.size();k++){
									StringBuffer sqlr = new StringBuffer();//更新次因件的出库状态
									sqlr.append(" update TT_AS_WR_PARTSITEM p set p.is_notice="+Constant.IF_TYPE_YES+" ,p.Notice_Date=sysdate where p.part_id="+list.get(k).get("PART_ID").toString());
					    			this.update(sqlr.toString(), null);
					    			//开始保存次因件的出库记录
					    			TtAsWrOldOutPartPO pp2 = new TtAsWrOldOutPartPO();
					    			pp2.setClaimNo(list.get(k).get("CLAIM_NO").toString());
					    			pp2.setCreateBy(loginUser.getUserId());
					    			pp2.setCreateDate( new Date());
					    			pp2.setId(Long.parseLong(SequenceManager.getSequence("")));
					    			pp2.setOutAmout(Integer.parseInt(list.get(k).get("AMOUNT").toString()));
					    			pp2.setOutBy(loginUser.getUserId());
					    			pp2.setOutType(Integer.parseInt(outType));
					    			pp2.setOutDate( new Date());
					    			pp2.setOutNo(no);
					    			pp2.setOutPartCode(list.get(k).get("PART_CODE").toString());
					    			pp2.setOutPartName(list.get(k).get("PART_NAME").toString());
					    			pp2.setRemark(partCode+"责任连带");
					    			pp2.setYieldly(Integer.parseInt(yieldly));
					    			pp2.setOutPartType(Integer.parseInt(partReturn));
					    			pp2.setRelationalOutNo(relationOutNo);
					    			pp2.setRangeNo(rangeNo);
					    			pp2.setSupplyCode(supplyCode);
									pp2.setSupplyName(supplyName);
					    			this.insert(pp2); 
								}
							}
							/**
	    	    			 * wizard_lee 2014-12-25
	    	    			 * 如果是切换件以无件的形式出库，则更新出库标识为部分出库
	    	    			 */
	    	    			if(outFlag==1){
		    	    			StringBuffer sql1 = new StringBuffer();
		    	    			//切换件无件出库的时候 is_claim和is_out设置为中间值2

		    	    			sql1.append(" update tt_as_wr_old_returned_detail d set d.is_out=2,d.is_cliam='2' WHERE d.claim_no='"+claimNo+"' AND d.part_code='"+partCode+"' AND d.sign_amount>0 AND d.IS_OUT IN(0) AND ROWNUM=1");

		    	    			//增加rownum=1的目的是当一个索赔单的同一个主因件有多个的时候会批量更新到该单据的该件为已出库状态
		    	    			this.update(sql1.toString(), null);
	    	    			}
	    	    			
						}else if ("2".equalsIgnoreCase(partReturn)&& !Constant.OUT_CLAIM_TYPE_03.toString().equals(outType)){//如果是特殊费用,更新特殊费用相关数据
							 this.insert(pp); //执行主因件出库明细插入
							String sql = "UPDATE Tt_As_Wr_Spefee s SET s.Is_Notice = "+Constant.IF_TYPE_YES+" ,s.notice_date=SYSDATE WHERE s.fee_no='"+claimNo+"'";
							this.update(sql, null);
						}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				res=-1;
				throw new RuntimeException(e);
			}
			return res;
	}
	@SuppressWarnings("unchecked")
	private void callRangeSingele(AclUserBean loginUser, String no,
			String outType) {
		if(Constant.OUT_CLAIM_TYPE_01.toString().equals(outType)){
			POFactory poFactory = POFactoryBuilder.getInstance();
			List ins = new LinkedList<Object>();
			ins.add(no);
			ins.add(loginUser.getUserId());
			poFactory.callProcedure("P_CREATE_RANGE_SINGLE",ins,null);
		}
	}
	@SuppressWarnings("unchecked")
	public String getrangeNo(AclUserBean loginUser,
			ClaimOldPartOutStorageDao daos, String noType, String isHs) {
		String ywNo="";
		if("1".equalsIgnoreCase(noType)){
			if("".equalsIgnoreCase(isHs)){
				ywNo="HSF";
			}else{
				ywNo=isHs;
			}
		}else{
			ywNo="HSF";
		}
		String rangeNo="";
		String seleSql = "SELECT NUM FROM tt_AS_wr_No_range WHERE 1=1  AND Yw_No='"+ywNo+"'";
		List<Map<String,Object>> rList = this.pageQuery(seleSql.toString(), null, this.getFunName());
		if(rList!=null &&rList.size()>0){
		 //rangeNo=	GenerateStockNo2()+String.format("%03d", Integer.parseInt(rList.get(0).get("NUM").toString()));
		 rangeNo=	 GenerateStockNo();//需求改变的退赔单号
		 String sqlr = " update tt_AS_wr_NO_range a set a.num=nvl(a.num,0)+1 where a.yw_no='"+ywNo+"'";
		 this.update(sqlr, null);
		}else{
			//rangeNo=GenerateStockNo2()+String.format("%03d", 1);
			 rangeNo=	 GenerateStockNo();
			String insql="insert into tt_AS_wr_NO_range(id,create_by,create_date,num,yw_no) values(f_getid(),"+loginUser.getUserId()+",sysdate,1,'"+ywNo+"')";
			this.insert(insql);
		}
		/*if(!"HSF".equalsIgnoreCase(ywNo)){
			rangeNo=ywNo+rangeNo;
		}*/
		if(rangeNo==""||rangeNo==null ||GenerateStockNo2().equalsIgnoreCase(rangeNo) ){
			 new Exception("获取退赔单号失败!");
		}
		return rangeNo;
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> findInfoByPartCodeAndSupplyCode(String partCode, String supplyCode,String partReturn) {
		StringBuffer sb= new StringBuffer();
		sb.append("select c.* from (SELECT sum(D.SIGN_AMOUNT) retrun_AMOUNT,\n" );
		sb.append("               0 no_retrun_amount,\n" );
		sb.append("               D.CLAIM_NO,\n" );
		sb.append("               d.PART_CODE,\n" );
		sb.append("               d.PART_NAME,\n" );
		sb.append("               d.part_id ID,\n" );
		sb.append("               d.producer_code SUPPLY_CODE,\n" );
		sb.append("               d.producer_name SUPPLY_NAME,\n" );
		sb.append("               1 PART_RETURN\n" );
		sb.append("          FROM TT_AS_WR_OLD_RETURNED_DETAIL D,\n" );
		sb.append("               TM_DEALER                    DD,\n" );
		sb.append("               TT_AS_WR_OLD_RETURNED        R\n" );
		sb.append("         WHERE 1 = 1\n" );
		sb.append("           AND R.ID = D.RETURN_ID --and r.return_type=10731002\n" );
		sb.append("           AND d.is_main_code = 94001001\n" );
		sb.append("           AND R.DEALER_ID = DD.DEALER_ID\n" );
		sb.append("           and r.yieldly = 95411001\n" );
		sb.append("           and (r.status = 10811005 or\n" );
		sb.append("               (r.status = 10811004 and d.is_in_house = 10041001))\n" );
		sb.append("           AND D.SIGN_AMOUNT > 0\n" );
		sb.append("           AND (D.IS_CLIAM = 0 OR D.IS_CLIAM IS NULL OR D.IS_CLIAM=2)\n" );
		sb.append("           AND D.IS_OUT in(0,2)\n" );
		//增加库存出库标识 未出库的和部分出库的纳入出库范围  只有在实物出库的时候才会存在0、2两种状态
		sb.append("           AND (D.QHJ_FLAG = 0 OR (D.QHJ_FLAG=1 AND D.KCDB_FLAG=2)) \n" );
		/**保留原来的旧件出库逻辑 并增加切换件调拨到旧件库房的逻辑**/
		sb.append("         GROUP BY D.CLAIM_NO,\n" );
		sb.append("                  d.PART_CODE,\n" );
		sb.append("                  d.PART_NAME,\n" );
		sb.append("                  d.part_id,\n" );
		sb.append("                  d.producer_code,\n" );
		sb.append("                  d.producer_name\n" );
		sb.append("        UNION ALL\n" );
		/**增加切换件为调拨的作为无件出库的条件**/
		sb.append("SELECT 0 retrun_AMOUNT,\n");
		sb.append("              SUM(D.SIGN_AMOUNT) no_retrun_amount,\n");
		sb.append("              D.CLAIM_NO,\n");
		sb.append("              d.PART_CODE,\n");
		sb.append("              d.PART_NAME,\n");
		sb.append("              d.part_id ID,\n");
		sb.append("              d.producer_code SUPPLY_CODE,\n");
		sb.append("              d.producer_name SUPPLY_NAME,\n");
		sb.append("              0 PART_RETURN\n");
		sb.append("         FROM TT_AS_WR_OLD_RETURNED_DETAIL D,\n");
		sb.append("              TM_DEALER                    DD,\n");
		sb.append("              TT_AS_WR_OLD_RETURNED        R\n");
		sb.append("        WHERE 1 = 1\n");
		sb.append("          AND R.ID = D.RETURN_ID --and r.return_type=10731002\n");
		sb.append("          AND d.is_main_code = 94001001\n");
		sb.append("          AND R.DEALER_ID = DD.DEALER_ID\n");
		sb.append("          and r.yieldly = 95411001\n");
		sb.append("          and (r.status = 10811005 or\n");
		sb.append("              (r.status = 10811004 and d.is_in_house = 10041001))\n");
		sb.append("          AND D.SIGN_AMOUNT > 0\n");
		sb.append("          AND (D.IS_CLIAM = 0 OR D.IS_CLIAM IS NULL)\n");
		sb.append("          AND D.IS_OUT = 0\n");
		sb.append("          AND (D.QHJ_FLAG=1 AND D.KCDB_FLAG<>2)"); 
		sb.append("         GROUP BY D.CLAIM_NO,\n" );
		sb.append("                  d.PART_CODE,\n" );
		sb.append("                  d.PART_NAME,\n" );
		sb.append("                  d.part_id,\n" );
		sb.append("                  d.producer_code,\n" );
		sb.append("                  d.producer_name\n" );
		/**增加切换件为调拨的作为无件出库的条件**/
		sb.append("        UNION ALL\n" );
		sb.append("        SELECT 0 retrun_amount,\n" );
		sb.append("               count(P.QUANTITY) no_retrun_amount,\n" );
		sb.append("               A.CLAIM_NO,\n" );
		sb.append("               P.DOWN_PART_CODE PART_CODE,\n" );
		sb.append("               P.DOWN_PART_NAME PART_NAME,\n" );
		sb.append("               P.PART_ID ID,\n" );
		sb.append("               P.DOWN_PRODUCT_CODE SUPPLY_CODE,\n" );
		sb.append("               P.DOWN_PRODUCT_NAME SUPPLY_NAME,\n" );
		sb.append("               0 PART_RETURN\n" );
		sb.append("          FROM TT_AS_WR_PARTSITEM   P,\n" );
		sb.append("               TM_PT_PART_BASE      B,\n" );
		sb.append("               TT_AS_WR_APPLICATION A,\n" );
		sb.append("               tm_dealer            d\n" );
		sb.append("         WHERE P.PART_CODE = B.PART_CODE(+)\n" );
		sb.append("           AND A.ID = P.ID\n" );
		sb.append("           AND a.dealer_id = d.dealer_id\n" );
		sb.append("           AND ((B.IS_RETURN = 95361002 AND B.IS_CLIAM = 95321001 AND\n" );
		sb.append("               P.PART_USE_TYPE = 1) OR\n" );
		sb.append("               (P.PART_USE_TYPE = 0 and B.IS_CLIAM = 95321001 AND\n" );
		sb.append("               p.quantity = 0))\n" );
		sb.append("           AND P.DOWN_PART_CODE NOT IN ('00-000', 'NO_PARTS', '00-0000')\n" );
		sb.append("           AND P.IS_NOTICE = 10041002\n" );
		sb.append("           AND (A.URGENT = 0 or A.URGENT = 2)\n" );
		sb.append("           AND A.STATUS NOT IN (10791001, 10791003, 10791006, 10791005)\n" );
		sb.append("           AND P.RESPONSIBILITY_TYPE = 94001001\n" );
		sb.append("         GROUP BY A.CLAIM_NO,\n" );
		sb.append("                  P.DOWN_PART_CODE,\n" );
		sb.append("                  P.DOWN_PART_NAME,\n" );
		sb.append("                  P.PART_ID,\n" );
		sb.append("                  P.DOWN_PRODUCT_CODE,\n" );
		sb.append("                  P.DOWN_PRODUCT_NAME\n" );
		sb.append("        having sum(p.quantity) = 0\n" );
		sb.append("        UNION ALL\n" );
		sb.append("        SELECT 0 retrun_amount,\n" );
		sb.append("               SUM(P.QUANTITY) not_retrun_amount,\n" );
		sb.append("               A.CLAIM_NO,\n" );
		sb.append("               P.DOWN_PART_CODE PART_CODE,\n" );
		sb.append("               P.DOWN_PART_NAME PART_NAME,\n" );
		sb.append("               P.PART_ID ID,\n" );
		sb.append("               P.DOWN_PRODUCT_CODE SUPPLY_CODE,\n" );
		sb.append("               P.DOWN_PRODUCT_NAME SUPPLY_NAME,\n" );
		sb.append("               0 PART_RETURN\n" );
		sb.append("          FROM TT_AS_WR_PARTSITEM   P,\n" );
		sb.append("               TM_PT_PART_BASE      B,\n" );
		sb.append("               TT_AS_WR_APPLICATION A,\n" );
		sb.append("               tm_dealer            d\n" );
		sb.append("         WHERE P.PART_CODE = B.PART_CODE(+)\n" );
		sb.append("           AND A.ID = P.ID\n" );
		sb.append("           AND a.dealer_id = d.dealer_id\n" );
		sb.append("           AND ((B.IS_RETURN = 95361002 AND B.IS_CLIAM = 95321001 AND\n" );
		sb.append("               P.PART_USE_TYPE = 1) OR\n" );
		sb.append("               (P.PART_USE_TYPE = 0 and B.IS_CLIAM = 95321001 AND\n" );
		sb.append("               p.quantity = 0))\n" );
		sb.append("           AND P.DOWN_PART_CODE NOT IN ('00-000', 'NO_PARTS', '00-0000')\n" );
		sb.append("           AND P.IS_NOTICE = 10041002\n" );
		sb.append("           AND (A.URGENT = 0 or A.URGENT = 2)\n" );
		sb.append("           AND A.STATUS NOT IN (10791001, 10791003, 10791006, 10791005)\n" );
		sb.append("           AND P.RESPONSIBILITY_TYPE = 94001001\n" );
		sb.append("         GROUP BY A.CLAIM_NO,\n" );
		sb.append("                  P.DOWN_PART_CODE,\n" );
		sb.append("                  P.DOWN_PART_NAME,\n" );
		sb.append("                  P.PART_ID,\n" );
		sb.append("                  P.DOWN_PRODUCT_CODE,\n" );
		sb.append("                  P.DOWN_PRODUCT_NAME\n" );
		sb.append("        having sum(p.quantity) > 0 ) c where 1=1 \n" );
		DaoFactory.getsql(sb, "c.PART_RETURN", partReturn, 1);
		DaoFactory.getsql(sb, "c.PART_CODE", partCode, 1);
		DaoFactory.getsql(sb, "c.SUPPLY_CODE", supplyCode, 1);
		sb.append("        order by c.id desc");
		List<Map<String, Object>> list = this.pageQuery(sb.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * 新的查询供应商配件数量
	 * @param partCode
	 * @param supplyCode
	 * @param partReturn
	 * @param in_start_date
	 * @param in_end_date
	 * @return
	 * @author chenyub@yonyou.com
	 * 下午4:10:25
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findSupplierNew(String partCode,
			String supplyCode, String partReturn, String in_start_date,
			String in_end_date) {
		String sql = findInfoByPartCodeAndSupplyCode(null, supplyCode, null, null, partCode, null, null, in_start_date, in_end_date,null);
		List<Map<String, Object>> list = this.pageQuery(sql, null, getFunName());
		return list;
	}
	
	/**
	 * 从queryPreOutStoreListByMix扒的子查询
	 * @param supply_name
	 * @param supply_code
	 * @param dealer_name
	 * @param dealer_code
	 * @param part_code
	 * @param part_name
	 * @param claim_no
	 * @param in_start_date
	 * @param in_end_date
	 * @return
	 * @author chenyub@yonyou.com
	 * 下午4:02:43
	 */
	public String findInfoByPartCodeAndSupplyCode(String supply_name, String supply_code
			,String dealer_name,String dealer_code,String part_code,String part_name,String claim_no
			,String in_start_date,String in_end_date,String yieldly) {
		supply_name = CommonUtils.checkNull(supply_name);
		supply_code = CommonUtils.checkNull(supply_code);
		dealer_name = CommonUtils.checkNull(dealer_name);
		dealer_code = CommonUtils.checkNull(dealer_code);
		part_code = CommonUtils.checkNull(part_code);
		part_name = CommonUtils.checkNull(part_name);
		claim_no = CommonUtils.checkNull(claim_no);
		in_start_date = CommonUtils.checkNull(in_start_date);
		in_end_date = CommonUtils.checkNull(in_end_date);
		yieldly = CommonUtils.checkNull(yieldly);
		
		StringBuffer sql=new StringBuffer();
		sql.append(partReturnSql1(supply_name, supply_code, dealer_name, dealer_code, part_code, part_name, claim_no, in_start_date, in_end_date, yieldly));
		sql.append(" UNION  \n");
		sql.append(partReturnSql2(supply_name, supply_code, dealer_name, dealer_code, part_code, part_name, claim_no, in_start_date, in_end_date, yieldly));
		sql.append("UNION ALL\n");
		sql.append(partNoReturnSql(supply_name, supply_code, dealer_name, dealer_code, part_code, part_name, claim_no, in_start_date, in_end_date, yieldly));
		//无件 特殊费用
		sql.append("UNION ALL\n");
		sql.append(partSpecialSql(supply_name, supply_code, dealer_name, dealer_code, part_code, part_name, claim_no, in_start_date, in_end_date, yieldly));
		return sql.toString();
	}

	/**
	 * 实件返厂的sql
	 * @param supply_name
	 * @param supply_code
	 * @param dealer_name
	 * @param dealer_code
	 * @param part_code
	 * @param part_name
	 * @param claim_no
	 * @param in_start_date
	 * @param in_end_date
	 * @param yieldly
	 * @return
	 * @author chenyub@yonyou.com
	 * 2015年12月14日  下午2:59:41
	 */
	public String partReturnSql1(String supply_name, String supply_code
			,String dealer_name,String dealer_code,String part_code,String part_name,String claim_no
			,String in_start_date,String in_end_date,String yieldly){
		StringBuffer sql = new StringBuffer();
		//有件（库存）
		sql.append("SELECT sum(nvl(D.SIGN_AMOUNT,0)) ALL_AMOUNT,sum(nvl(D.SIGN_AMOUNT,0)) RETRUN_AMOUNT,\n");
		sql.append(" 0 no_retrun_amount, ");
		sql.append(" d.claim_id claim_id,D.CLAIM_NO,\n");
		sql.append(" dd.dealer_code,dd.dealer_name, ");
		sql.append("       d.PART_CODE,\n");
		sql.append("       d.PART_NAME,\n");
		sql.append("       d.part_id ID,\n");
		sql.append("       d.producer_code SUPPLY_CODE,\n");
		sql.append("       d.producer_name SUPPLY_NAME,1 PART_RETURN\n");
		//================2015.09.18增加切换件标识开始
		sql.append(" ,d.qhj_flag \"QHJ_FLAG\",decode(d.qhj_flag,1,'是','否') \"IS_QHJ\" ");
		//2015.09.18增加切换件标识==================结束
		//================2015.10.29增加单据类型 1:索赔单;2:特殊费用;开始
		sql.append(" ,'1' \"BILL_TYPE\" ,'' \"FEE_TYPE\" ");
		//================2015.10.29增加单据类型 1:索赔单;2:特殊费用;结束
		sql.append(" ,(select count(1) from TT_AS_WR_OLD_RETURNED_DETAIL g where g.claim_no = d.claim_no and g.main_part_code = d.PART_CODE) countPart ");
		sql.append("  FROM TT_AS_WR_OLD_RETURNED_DETAIL D\n");
		sql.append("  left join TT_AS_WR_OLD_RETURNED R on R.ID = D.RETURN_ID\n");
		sql.append("  left join TM_DEALER DD on R.DEALER_ID = DD.DEALER_ID\n");
		// 新增表连接,查询索赔单和经销商的信息===============开始
//		sql.append("  left join tt_as_wr_partsitem pi on d.claim_id=pi.id and d.part_id=pi.part_id ");
//		sql.append("  left join tt_as_wr_application wa on wa.id=d.claim_id ");
		// 新增表连接,查询索赔单和经销商的信息===============结束
		sql.append(" WHERE 1 = 1\n");
		sql.append(" AND d.is_main_code="+Constant.RESPONS_NATURE_STATUS_01+"\n"); 
		if(StringUtils.isNotEmpty(yieldly)){
			sql.append("  and r.yieldly="+yieldly+"\n");
		}
		sql.append("  and (r.status in(").append(Constant.BACK_LIST_STATUS_05);
		sql.append(",").append(Constant.BACK_LIST_STATUS_07);
		sql.append(" )or (r.status="+Constant.BACK_LIST_STATUS_04+" and d.is_in_house="+Constant.IF_TYPE_YES+"))\n");
		sql.append("   AND D.SIGN_AMOUNT > 0\n");
		sql.append("   AND (D.IS_CLIAM = 0 OR D.IS_CLIAM IS NULL or D.IS_CLIAM = 2 )\n");
		sql.append("   and d.is_out in (0,2) \n");
		//==================zyw 加标示切换件和库存调拨
		sql.append("   AND  (D.qhj_flag = 0 or (d.qhj_flag=1 and d.kcdb_flag=2)) \n");
		DaoFactory.getsql(sql, "d.producer_code", supply_code.toUpperCase(), 2);
		DaoFactory.getsql(sql, "d.producer_name", supply_name, 2);
		DaoFactory.getsql(sql, "dd.dealer_name", dealer_name, 2);
		DaoFactory.getsql(sql, "dd.dealer_code", dealer_code.toUpperCase(), 2);
		DaoFactory.getsql(sql, "d.part_code", part_code.toUpperCase(), 2);
		DaoFactory.getsql(sql, "d.part_name", part_name, 2);
		DaoFactory.getsql(sql, "d.claim_no", claim_no.toUpperCase(), 2);
		DaoFactory.getsql(sql, "d.in_date", in_start_date, 3);
		DaoFactory.getsql(sql, "d.in_date", in_end_date, 4);
		
		sql.append(" GROUP BY  D.CLAIM_NO,\n");
		sql.append("       d.PART_CODE,\n");
		sql.append("       d.PART_NAME,\n");
		sql.append("       d.part_id ,\n");
		sql.append("       d.producer_code ,\n");
		sql.append("       d.producer_name\n"); 
		// 新增表连接,查询索赔单和经销商的信息===============开始
		sql.append(" ,d.claim_id ");
		sql.append(" ,dd.dealer_code,dd.dealer_name ");
		// 新增表连接,查询索赔单和经销商的信息===============结束
		//================2015.09.18增加切换件标识开始
		sql.append(" ,d.qhj_flag ");
		//2015.09.18增加切换件标识==================结束
		return sql.toString();
	}
	
	/**
	 * 实件返厂的sql
	 * @param supply_name
	 * @param supply_code
	 * @param dealer_name
	 * @param dealer_code
	 * @param part_code
	 * @param part_name
	 * @param claim_no
	 * @param in_start_date
	 * @param in_end_date
	 * @param yieldly
	 * @return
	 * @author chenyub@yonyou.com
	 * 2015年12月14日  下午2:59:55
	 */
	public String partReturnSql2(String supply_name, String supply_code
			,String dealer_name,String dealer_code,String part_code,String part_name,String claim_no
			,String in_start_date,String in_end_date,String yieldly){
		StringBuffer sql = new StringBuffer();
		//无件（库存）
		sql.append("SELECT sum(nvl(D.SIGN_AMOUNT,0)) ALL_AMOUNT,0 RETRUN_AMOUNT,\n");
		sql.append(" sum(nvl(D.SIGN_AMOUNT,0)) no_retrun_amount, ");
		sql.append(" d.claim_id claim_id,D.CLAIM_NO,\n");
		sql.append(" dd.dealer_code,dd.dealer_name, ");
		sql.append("       d.PART_CODE,\n");
		sql.append("       d.PART_NAME,\n");
		sql.append("       d.part_id ID,\n");
		sql.append("       d.producer_code SUPPLY_CODE,\n");
		sql.append("       d.producer_name SUPPLY_NAME,0 PART_RETURN\n");
		//================2015.09.18增加切换件标识开始
		sql.append(" ,d.qhj_flag \"QHJ_FLAG\",decode(d.qhj_flag,1,'是','否') \"IS_QHJ\" ");
		//2015.09.18增加切换件标识==================结束
		//================2015.10.29增加单据类型 1:索赔单;2:特殊费用;开始
		sql.append(" ,'1' \"BILL_TYPE\"  ,'' \"FEE_TYPE\" ");
		//================2015.10.29增加单据类型 1:索赔单;2:特殊费用;结束
		sql.append(" , (select count(1) from  TT_AS_WR_OLD_RETURNED_DETAIL g  where g.claim_no = d.claim_no and g.main_part_code = d.PART_CODE) countPart ");
		sql.append("  FROM TT_AS_WR_OLD_RETURNED_DETAIL D\n");
		sql.append("  left join TT_AS_WR_OLD_RETURNED R on R.ID = D.RETURN_ID\n");
		sql.append("  left join TM_DEALER DD on R.DEALER_ID = DD.DEALER_ID\n");
		// 新增表连接,查询索赔单和经销商的信息===============开始
//		sql.append("  left join tt_as_wr_partsitem pi on d.claim_id=pi.id and d.part_id=pi.part_id ");
//		sql.append("  left join tt_as_wr_application wa on wa.id=d.claim_id ");
		// 新增表连接,查询索赔单和经销商的信息===============结束
		sql.append(" WHERE 1 = 1\n");
		sql.append("   AND R.ID = D.RETURN_ID  --and r.return_type=10731002 \n");//去掉了回运类型
		sql.append(" AND d.is_main_code="+Constant.RESPONS_NATURE_STATUS_01+"\n"); 
		sql.append("   AND R.DEALER_ID = DD.DEALER_ID\n");
		if(StringUtils.isNotEmpty(yieldly)){
			sql.append("  and r.yieldly="+yieldly+"\n");
		}
		sql.append("  and (r.status in(").append(Constant.BACK_LIST_STATUS_05);
		sql.append(",").append(Constant.BACK_LIST_STATUS_07);
		sql.append(" )or (r.status="+Constant.BACK_LIST_STATUS_04+" and d.is_in_house="+Constant.IF_TYPE_YES+"))\n");
		sql.append("   AND D.SIGN_AMOUNT > 0\n");
		sql.append("   AND (D.IS_CLIAM = 0 OR D.IS_CLIAM IS NULL)\n");
		sql.append("   AND D.IS_OUT = 0\n");
		//==================zyw 加标示切换件和库存调拨
		sql.append("   AND  (d.qhj_flag=1 and d.kcdb_flag<>2)  \n");
		DaoFactory.getsql(sql, "d.producer_code", supply_code.toUpperCase(), 2);
		DaoFactory.getsql(sql, "d.producer_name", supply_name, 2);
		DaoFactory.getsql(sql, "dd.dealer_name", dealer_name, 2);
		DaoFactory.getsql(sql, "dd.dealer_code", dealer_code.toUpperCase(), 2);
		DaoFactory.getsql(sql, "d.part_code", part_code.toUpperCase(), 2);
		DaoFactory.getsql(sql, "d.part_name", part_name, 2);
		DaoFactory.getsql(sql, "d.claim_no", claim_no.toUpperCase(), 2);
		DaoFactory.getsql(sql, "d.in_date", in_start_date, 3);
		DaoFactory.getsql(sql, "d.in_date", in_end_date, 4);
		
		sql.append(" GROUP BY  D.CLAIM_NO,\n");
		sql.append("       d.PART_CODE,\n");
		sql.append("       d.PART_NAME,\n");
		sql.append("       d.part_id ,\n");
		sql.append("       d.producer_code ,\n");
		sql.append("       d.producer_name\n"); 
		// 新增表连接,查询索赔单和经销商的信息===============开始
		sql.append("  ,d.claim_id ");
		sql.append(" ,dd.dealer_code,dd.dealer_name ");
		// 新增表连接,查询索赔单和经销商的信息===============结束
		//================2015.09.18增加切换件标识开始
		sql.append(" ,d.qhj_flag ");
		//2015.09.18增加切换件标识==================结束
		return sql.toString();
	}

	/**
	 * 无件返厂的配件
	 * @param supply_name
	 * @param supply_code
	 * @param dealer_name
	 * @param dealer_code
	 * @param part_code
	 * @param part_name
	 * @param claim_no
	 * @param in_start_date
	 * @param in_end_date
	 * @param yieldly
	 * @return
	 * @author chenyub@yonyou.com
	 * 2015年12月14日  下午3:00:05
	 */
	public String partNoReturnSql(String supply_name, String supply_code
			,String dealer_name,String dealer_code,String part_code,String part_name,String claim_no
			,String in_start_date,String in_end_date,String yieldly){
		StringBuffer sql = new StringBuffer();
		//无件（索赔单）
		sql.append("       SELECT SUM(nvl(P.QUANTITY,0)) ALL_AMOUNT,0 ");
		sql.append(" ,SUM(P.QUANTITY) no_retrun_amount ");
		sql.append(" ,a.id as claim_id,A.CLAIM_NO ");
		sql.append(" ,d.dealer_code,d.dealer_name, ");
		sql.append(" P.DOWN_PART_CODE PART_CODE,P.DOWN_PART_NAME PART_NAME,\n");
		sql.append("       P.PART_ID ID ,P.DOWN_PRODUCT_CODE SUPPLY_CODE,P.DOWN_PRODUCT_NAME SUPPLY_NAME,\n");
		sql.append("     0 PART_RETURN\n");
		//================2015.09.18增加切换件标识开始
		sql.append(" ,rd.qhj_flag \"QHJ_FLAG\",decode(rd.qhj_flag,1,'是','否') \"IS_QHJ\" ");
		//2015.09.18增加切换件标识==================结束
		//================2015.10.29增加单据类型 1:索赔单;2:特殊费用;开始
		sql.append(" ,'1' \"BILL_TYPE\"  ,'' \"FEE_TYPE\" ");
		//================2015.10.29增加单据类型 1:索赔单;2:特殊费用;结束
		sql.append(" ,( select count(1) from TT_AS_WR_PARTSITEM  S where S.ID = A.ID and S.Main_Part_Code = P.Part_Code)  ");
		sql.append("  FROM TT_AS_WR_PARTSITEM   P \n");
		sql.append(" left join TM_PT_PART_BASE B on P.PART_CODE = B.PART_CODE \n");
		sql.append(" left join TT_AS_WR_APPLICATION A on  A.ID = P.ID ");
		sql.append(" left join tm_dealer d on a.dealer_id = d.dealer_id \n");
		//================2015.09.18增加切换件标识开始
		sql.append(" left join tt_as_wr_old_returned_detail rd on rd.claim_id = a.id ");
		//2015.09.18增加切换件标识==================结束
		sql.append(" left join tt_as_wr_old_out_part op on a.claim_no=op.claim_no and p.part_code=op.out_part_code");
		sql.append(" WHERE ");
		sql.append("  ((p.IS_RETURN = "+Constant.IS_RETURN_02+" AND B.IS_CLIAM = "+Constant.IS_CLAIM_01+" AND P.PART_USE_TYPE=1)) ");//OR (P.PART_USE_TYPE=0 and B.IS_CLIAM = "+Constant.IS_CLAIM_01+" AND p.quantity=0))\n");
		sql.append("   AND P.DOWN_PART_CODE NOT IN ('00-000', 'NO_PARTS', '00-0000')\n");
		sql.append("   AND (P.IS_NOTICE = "+Constant.IF_TYPE_NO+" or P.IS_NOTICE is null) \n");
		sql.append("   AND (A.URGENT=0 or A.URGENT=2 ) AND  A.STATUS NOT IN ( 10791001,10791003,10791006,10791005)   AND  P.RESPONSIBILITY_TYPE="+Constant.RESPONS_NATURE_STATUS_01+"\n");
		sql.append(" and op.id is null ");
		//上面一句加了标识 
		DaoFactory.getsql(sql, "P.DOWN_PRODUCT_CODE", supply_code.toUpperCase(), 2);
		DaoFactory.getsql(sql, "P.producer_name", supply_name, 2);
		DaoFactory.getsql(sql, "d.dealer_name", dealer_name, 2);
		DaoFactory.getsql(sql, "d.dealer_code", dealer_code.toUpperCase(), 2);
		DaoFactory.getsql(sql, "P.DOWN_PART_CODE", part_code.toUpperCase(), 2);
		DaoFactory.getsql(sql, "P.DOWN_PART_NAME", part_name, 2);
		DaoFactory.getsql(sql, "a.claim_no", claim_no.toUpperCase(), 2);
		
		sql.append("   GROUP BY A.CLAIM_NO,P.PART_CODE,a.id,P.DOWN_PART_CODE ,P.DOWN_PART_NAME ,\n");
		sql.append("       P.PART_ID  ,P.DOWN_PRODUCT_CODE ,P.DOWN_PRODUCT_NAME\n"); 
		//================2015.09.18增加切换件标识开始
		sql.append(" ,rd.qhj_flag ");
		sql.append(" ,d.dealer_code,d.dealer_name ");
		//2015.09.18增加切换件标识==================结束
		return sql.toString();
	}
	
	/**
	 * 特殊费用善意索赔的无件返厂
	 * @param supply_name
	 * @param supply_code
	 * @param dealer_name
	 * @param dealer_code
	 * @param part_code
	 * @param part_name
	 * @param claim_no
	 * @param in_start_date
	 * @param in_end_date
	 * @param yieldly
	 * @return
	 * @author chenyub@yonyou.com
	 * 2015年12月14日  下午3:00:55
	 */
	public String partSpecialSql(String supply_name, String supply_code
			,String dealer_name,String dealer_code,String part_code,String part_name,String claim_no
			,String in_start_date,String in_end_date,String yieldly){
		StringBuffer sql = new StringBuffer();
		sql.append("     SELECT 0 ALL_AMOUNT,0,0,(select max(a.id) from Tt_As_Wr_Application a where a.claim_no= S.FEE_NO) as claim_id,S.FEE_NO CLAIM_NO ");
		sql.append(" ,d.dealer_code,d.dealer_name, ");
		sql.append(" S.PART_CODE,S.PART_NAME,S.ID, S.SUPPLIER_CODE SUPPLY_CODE,\n");
		sql.append("     S.SUPPLIER_NAME SUPPLY_NAME,2 PART_RETURN\n");
		//================2015.09.18增加切换件标识开始
		sql.append(" ,0 \"QHJ_FLAG\",'否' \"IS_QHJ\" ");
		//2015.09.18增加切换件标识==================结束
		//================2015.10.29增加单据类型 1:索赔单;2:特殊费用;开始
		sql.append(" ,'2' \"BILL_TYPE\"  ,S.FEE_TYPE||'' \"FEE_TYPE\" ");
		//================2015.10.29增加单据类型 1:索赔单;2:特殊费用;结束
		sql.append(" ,0 ");
		sql.append("     FROM TT_AS_WR_SPEFEE S ,tm_dealer d\n");
		sql.append("     WHERE 1=1 AND s.dealer_id = d.dealer_id\n");
		sql.append("     AND S.STATUS = S.O_STATUS\n");
		sql.append("     AND S.PART_CODE  NOT IN ('00-000','00-0000')\n");
		sql.append("     AND S.PART_CODE IS NOT NULL  AND S.SUPPLIER_CODE IS NOT NULL\n");
		sql.append("     AND S.IS_NOTICE = "+Constant.IF_TYPE_NO+"\n"); 
		
		DaoFactory.getsql(sql, "S.SUPPLIER_CODE", supply_code.toUpperCase(), 2);
		DaoFactory.getsql(sql, "s.SUPPLIER_NAME", supply_name, 2);
		DaoFactory.getsql(sql, "d.dealer_name", dealer_name, 2);
		DaoFactory.getsql(sql, "d.dealer_code", dealer_code.toUpperCase(), 2);
		DaoFactory.getsql(sql, "S.PART_CODE", part_code.toUpperCase(), 2);
		DaoFactory.getsql(sql, "S.PART_NAME", part_name, 2);
		DaoFactory.getsql(sql, "S.FEE_NO", claim_no.toUpperCase(), 2);
		return sql.toString();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public  PageResult<Map<String, Object>>  queryBelowConditionDealer(
			RequestWrapper request,  Integer page, Integer currPage,Map<String, String> params) {
		StringBuffer sb= new StringBuffer();
		sb.append("select * from NO_SATISFACTION_INVOICE sat \n");
		sb.append("where 1=1 \n");
		if(params.get("dealerCode")!=null){
			sb.append(" and   sat.DEALER_CODE in("+params.get("dealerCode")+") \n"); 
		}
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
		return list;
	}
	@SuppressWarnings("unchecked")
	public void exportBelowConditionDealer(ActionContext act,
			PageResult<Map<String, Object>> ps) {
		try {
			String[] head=new String[7];
			head[0]="服务站代码";
			head[1]="是否有旧件";
			head[2]="服务站名称";
			head[3]="开始时间";
			head[4]="结束时间";
			head[5]="原因";
			List<Map<String, Object>> records = ps.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[7];
					detail[0]=BaseUtils.checkNull(map.get("DEALER_CODE"));
					detail[1]=BaseUtils.checkNull(map.get("CLAIM_IS"));
					detail[2]=BaseUtils.checkNull(map.get("DEALER_NAME"));
					detail[3]=BaseUtils.checkNull(map.get("SDATE"));
					detail[4]=BaseUtils.checkNull(map.get("EDATE"));
					detail[5]=BaseUtils.checkNull(map.get("REASON"));
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
	public List<Map<String,Object>> getLabourDan(/*String dealerId,String modelId,String oemCompanyId*/)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT pr.*\n");
		sql.append("             from tt_as_wr_labour_price pr\n");
		sql.append("             left outer join tm_dealer td on td.dealer_id = pr.dealer_id\n");
		sql.append("            where 1 = 1\n");
		//sql.append("              AND pr.DEALER_ID = "+dealerId+"\n");
		sql.append("              and pr.mode_type in\n");
		sql.append("                  (SELECT WRGROUP_CODE\n");
		sql.append("                     FROM tt_as_wr_model_group\n");
		sql.append("                    WHERE WRGROUP_ID in\n");
		//sql.append("                          (SELECT WRGROUP_ID\n");
		sql.append("                          (SELECT DISTINCT WRGROUP_ID\n");
		sql.append("                             FROM tt_as_wr_model_item)\n");
		//sql.append("                            WHERE MODEL_ID = "+modelId+")\n");
		sql.append("                      and wrgroup_type = "+Constant.WR_MODEL_GROUP_TYPE_01+")"); 

		return this.pageQuery(sql.toString(), null,this.getFunName());
	}
	
	//查询物料组集合
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> getGroup(String series,String brand,Integer curPage){
		StringBuffer sql = new StringBuffer();
		sql.append("select t.group_id,t.group_code,t.group_name\n");
		sql.append("from tm_vhcl_material_group t\n");
		sql.append("where 1=1\n");
		
		if(Utility.testString(series)){
			sql.append("and t.group_level =3\n");
			sql.append("and t.group_id in (\n");
			sql.append("select TVMG.group_id  from tm_vhcl_material_group TVMG   start with TVMG.Group_id="+series+"\n");
			sql.append("       connect by prior TVMG.group_id =TVMG.parent_group_id\n");
			sql.append(")"); 

		}else if(Utility.testString(brand)&&!Utility.testString(series)){
			sql.append("and t.group_level =2\n");
			sql.append("and t.group_id in (\n");
			sql.append("select TVMG.group_id  from tm_vhcl_material_group TVMG   start with TVMG.Group_id="+brand+"\n");
			sql.append("       connect by prior TVMG.group_id =TVMG.parent_group_id\n");
			sql.append(")"); 

		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), Constant.PAGE_SIZE, curPage);
		return ps;
	}
	
	/**
	 * 
	 * @Title: queryChngCodeByType
	 * @Description: (车型组下拉框)
	 * @param
	 * @param typeCode
	 * @param
	 * @return 设定文件
	 * @return List<TmBusinessChngCodePO> 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public List<TtAsWrModelGroupPO> queryGroupName() {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select t.* from TT_AS_WR_MODEL_GROUP t where 1=1 ");
		sqlStr.append(" and WRGROUP_TYPE=" + Constant.WR_MODEL_GROUP_TYPE_01
				+ " "); // 索赔工时车型组
		// sqlStr.append(" and T.type_code = "+typeCode+"");
		List<TtAsWrModelGroupPO> list = select(TtAsWrModelGroupPO.class, sqlStr
				.toString(), null);
		return list;
	}
	@SuppressWarnings("unused")
	public List checkChangeByVal(RequestWrapper request, AclUserBean loginUser, String rangeNo) {
		List<Map> list=new ArrayList<Map>();
		int res=1;
		try {
			String val = DaoFactory.getParam(request, "val");
			String[] vals = StringUtils.split(val,";");
			//String no = GenerateStockNo();//生产出库单号 
			String noType = request.getParamValue("noType");//出库方式 (0混合出库/1分单号出库)
			String isHs = request.getParamValue("isHs");//is_house
			String outType = request.getParamValue("OUT_CLAIM_TYPE");//出库类型
			String no =rangeNo;//一致出库单号 zyw 2014-12-22
			// 2015.11.03增加日期的限制
			String in_start_date=request.getParamValue("in_start_date");
			String in_end_date = request.getParamValue("in_end_date");
			for (String params : vals) {
				String[] param = StringUtils.split(params,",");
				if(param!=null && param.length>0){
					String partCode = param[0];
					String supplyCode = param[1];
					String return_num = param[2];
					String no_return_num = param[3];
					if(!"0".equals(return_num)){
						List<Map<String, Object>> listInfo=//this.findInfoByPartCodeAndSupplyCode(partCode,supplyCode,"1");
								this.findSupplierNew(partCode, supplyCode, "1", in_start_date, in_end_date);
						if(listInfo!=null && listInfo.size()>0){
							int temp=0;
							for (Map<String, Object> map : listInfo) {
								String CLAIM_NO = map.get("CLAIM_NO").toString();
								String PART_CODE = map.get("PART_CODE").toString();
								String PART_NAME = map.get("PART_NAME").toString();
								String SUPPLY_CODE = map.get("SUPPLY_CODE").toString();
								String SUPPLY_NAME = map.get("SUPPLY_NAME").toString();
								String PART_RETURN = "1";
								int return_numTemp = Integer.parseInt(return_num);
								if(return_numTemp==temp){
									break;
								}else{
									String allAmount ="1";//出库数
									Map map1=doCheckStore(request, loginUser, CLAIM_NO,PART_CODE, PART_NAME, SUPPLY_CODE,SUPPLY_NAME, PART_RETURN,no,rangeNo,allAmount);
									if(map1!=null){
										list.add(map1);
									}
								}
								temp++;
							}
							//this.callRangeSingele(loginUser, no, outType);
						}
					}
			    	if(!"0".equals(no_return_num)){
			    		List<Map<String, Object>> listInfo=//this.findInfoByPartCodeAndSupplyCode(partCode,supplyCode,"0");
						this.findSupplierNew(partCode, supplyCode, "0", in_start_date, in_end_date);
			    		if(listInfo!=null && listInfo.size()>0){
							int temp=0;
							for (Map<String, Object> map : listInfo) {
								int return_numTemp = Integer.parseInt(no_return_num);
								String CLAIM_NO = map.get("CLAIM_NO").toString();
								String PART_CODE = map.get("PART_CODE").toString();
								String PART_NAME = map.get("PART_NAME").toString();
								String SUPPLY_CODE = map.get("SUPPLY_CODE").toString();
								String SUPPLY_NAME = map.get("SUPPLY_NAME").toString();
								String PART_RETURN = "0";
								if(return_numTemp==temp){
									break;
								}else{
									String allAmount ="0";//出库数
									Map map1=doCheckStore(request, loginUser, CLAIM_NO,PART_CODE, PART_NAME, SUPPLY_CODE,SUPPLY_NAME, PART_RETURN,no,rangeNo,allAmount);
									if(map1!=null){
										list.add(map1);
									}
								}
								temp++;
							}
						}
			    	}
				}
				//this.delete("delete from range_temp t where t.out_no='"+no+"'",null);
		//		this.callRangeSingele(loginUser, no, outType);
			}
			
		} catch (NumberFormatException e) {
			res=-1;
			e.printStackTrace();
		}
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	private Map doCheckStore(RequestWrapper request, AclUserBean loginUser,
			String claimNo, String partCode,
			String partName,String supplyCode, 
			String supplyName, String partReturn, 
			String no,String rangeNo,String allAmount) {
//			List<Map> list=new ArrayList<Map>();
					
//			String noType = request.getParamValue("noType");//出库方式 (0混合出库/1分单号出库)
//			String isHs = request.getParamValue("isHs");//is_house
			String outType = request.getParamValue("OUT_CLAIM_TYPE");//出库类型
			String yieldly = request.getParamValue("yieldly");//基地
			String relationOutNo = request.getParamValue("relationOutNo");//关联退赔单号
			if("-1".equalsIgnoreCase(relationOutNo)||"".equalsIgnoreCase(relationOutNo)){
				relationOutNo=null;
			}
			/**
			 * 保存出库明细
			 */
			TtAsWrOldOutPartPO pp = new TtAsWrOldOutPartPO();
			pp.setClaimNo(claimNo);
			pp.setCreateBy(loginUser.getUserId());
			pp.setCreateDate( new Date());
			pp.setId(Long.parseLong(SequenceManager.getSequence("")));
			pp.setOutAmout(Integer.parseInt(allAmount));
			pp.setOutBy(loginUser.getUserId());
			pp.setOutDate( new Date());
			pp.setOutNo(no);
			pp.setOutPartCode(partCode);
			pp.setOutPartName(partName);
			pp.setOutType(Integer.parseInt(outType));
			pp.setRemark(null);
			pp.setSupplyCode(supplyCode);
			pp.setSupplyName(supplyName);
			pp.setYieldly(Integer.parseInt(yieldly));
			pp.setOutPartType(Integer.parseInt(partReturn));
			pp.setRelationalOutNo(relationOutNo);
			pp.setRangeNo(rangeNo);
			//得到真实配件代码的ID
			TtPartDefinePO d  = new TtPartDefinePO();
			d.setPartOldcode(partCode);
			d = (TtPartDefinePO) this.select(d).get(0);
			if(Constant.OUT_CLAIM_TYPE_01.toString().equals(outType)){//出库类型=供应商开票
				return doCheckPartSupplyRelProblems(partCode,supplyCode);
				/*String partCode2 = partCode.substring(0, partCode.length()-3)+"000";
				String partCode3 = partCode.substring(0, partCode.length()-3)+"B00";
				String partCode4 = partCode.substring(0, partCode.length()-3)+"B0Y";
				StringBuffer sql = new StringBuffer();
				//将配件的最后3为分别替换
				sql.append("SELECT max(r.part_id) part_id,max(r.maker_id) maker_id  FROM tt_part_maker_relation r\n");
				sql.append("WHERE r.part_id  in (SELECT d.part_id FROM tt_part_define d WHERE d.part_oldcode in ('"+partCode+"','"+partCode2+"','"+partCode3+"','"+partCode4+"'))\n");
				sql.append("AND r.maker_id = (SELECT md.maker_id  FROM tt_part_maker_define md WHERE md.maker_code='"+supplyCode+"')"); 
				List<Map<String,Object>> lit = this.pageQuery(sql.toString(), null, this.getFunName());
				if(lit.get(0).get("PART_ID")==null &&lit.get(0).get("MAKER_ID")==null){
					Map map=new HashMap();
					map.put("partCode", partCode);
					map.put("supplyCode", supplyCode);
					return map;
				}*/
			}
			return null;
	}
	
	@SuppressWarnings("unchecked")
	public Map doCheckPartSupplyRelProblems(String partCode,String supplyCode){
		StringBuffer sql = new StringBuffer();
		int flag = 0;//#0标识没有任何显示关系  #1标识有明确关系并且有价格  #2标识存在非当前供应商的显示关系且有价格 #3标识有关系但无有效价格信息
		//1可以正常出库  2可以强制出库
		String partCode2 = "";
		String partCode3 = "";
		String partCode4 = "";
		if(partCode.length()==18){
			//17010510-C01-B00-3 18位的包含B00的件取17010510-C01-000-3的价格
			if("B00".equals(partCode.substring(13,16))){
				partCode2 = partCode.substring(0, 13)+"000"+partCode.substring(16, 18);
				partCode3 = partCode2;
				partCode4 = partCode2;
			}else{
				partCode2 = partCode.substring(0, partCode.length()-3)+"000";
				partCode3 = partCode.substring(0, partCode.length()-3)+"B00";
				partCode4 = partCode.substring(0, partCode.length()-3)+"B0Y";
			}
		}else{
			partCode2 = partCode.substring(0, partCode.length()-3)+"000";
			partCode3 = partCode.substring(0, partCode.length()-3)+"B00";
			partCode4 = partCode.substring(0, partCode.length()-3)+"B0Y";
		}
		List<Map<String,Object>> tmpPart = this.pageQuery("select part_id,part_cname from tt_part_define where part_oldcode in('"+partCode+"','"+partCode2+"','"+partCode3+"','"+partCode4+"')", null, this.getFunName());
		sql.append(" select * from tt_part_maker_relation where part_id in( select part_id from tt_part_define where part_oldcode in('"+partCode+"','"+partCode2+"','"+partCode3+"','"+partCode4+"'))");
		List<Map<String,Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		
		StringBuffer sbSql = new StringBuffer();
		sbSql.append(" select * from tt_part_maker_define where maker_code='"+supplyCode+"'");
		List<Map<String,Object>> listMaker = this.pageQuery(sbSql.toString(), null, this.getFunName());
		Map map=new HashMap();
		if(list != null && list.size()>0){
			//该件存在和供应商的关系
			for(int i=0;i<list.size();i++){
				//如果存在件和选择的供应商关系就正常出库，否则就提示是否需要强制出库
				if(list.get(i).get("MAKER_ID").toString().equals(listMaker.get(0).get("MAKER_ID").toString())){
					if(null != list.get(i).get("CLAIM_PRICE") && Double.parseDouble(list.get(i).get("CLAIM_PRICE").toString())>0){
						flag = 1;//该种类的件和该供应商有显示关系并且有价格
						break;
					}else{//当前供应商的显示关系存在但价格是无效  不能出库
						flag = 3;
						map.put("partCode", partCode);
						map.put("supplyCode", supplyCode);
						map.put("flag", 3);
						continue;
					}
				}else{
					if(null != list.get(i).get("CLAIM_PRICE") && Double.parseDouble(list.get(i).get("CLAIM_PRICE").toString())>0){
						flag = 2;
						map.put("partCode", partCode);
						map.put("supplyCode", supplyCode);
						map.put("flag", 2);
						break;
					}else{//不能出库
						map.put("partCode", partCode);
						map.put("supplyCode", supplyCode);
						map.put("flag", 0);
						continue;
					}
				}
			}
		}
		if(flag!=1)
		{
			//插入异常数据标识该配件没有任何配件和供应商关系无法出库
			TtPartMakerProblemPO pmPPO = new TtPartMakerProblemPO();
			TtPartMakerProblemDtlPO pmpDtlPO = new TtPartMakerProblemDtlPO();
			AclUserBean LogonUser=(AclUserBean) ActionContext.getContext().getSession().get(Constant.LOGON_USER);
			pmPPO.setCreateBy(LogonUser.getUserId());
			pmPPO.setCreateDate(new Date());
			pmPPO.setNum(1);
			pmPPO.setPartCode(partCode);
			pmPPO.setPartId(Long.parseLong(tmpPart.get(0).get("PART_ID").toString()));
			pmPPO.setPartName(tmpPart.get(0).get("PART_CNAME").toString());
			pmPPO.setRemark("");
			pmPPO.setStatus(99971002);
			pmPPO.setSupplyCode(supplyCode);
			pmPPO.setSupplyName(listMaker.get(0).get("MAKER_NAME").toString());
			pmPPO.setSupplyId(Long.parseLong(listMaker.get(0).get("MAKER_ID").toString()));
			int type = 0;
			if(flag==2||flag==0){
				type = 0;
				pmPPO.setType(0);
			}else{//3
				type = 1;
				pmPPO.setType(1);
			}
			pmPPO.setUpdateBy(LogonUser.getUserId());
			pmPPO.setUpdateDate(new Date());
			//明细
			pmpDtlPO.setCreateBy(LogonUser.getUserId());
			pmpDtlPO.setCreateDate(new Date());
			pmpDtlPO.setDtlId(Long.parseLong(SequenceManager.getSequence("")));
			pmpDtlPO.setPartCode(partCode);
			pmpDtlPO.setPartId(Long.parseLong(tmpPart.get(0).get("PART_ID").toString()));
			pmpDtlPO.setPartName(tmpPart.get(0).get("PART_CNAME").toString());
			pmpDtlPO.setSupplyCode(supplyCode);
			pmpDtlPO.setSupplyId(Long.parseLong(listMaker.get(0).get("MAKER_ID").toString()));
			pmpDtlPO.setSupplyName(listMaker.get(0).get("MAKER_NAME").toString());
			doInsertPartSupplyRelProblems(type,pmPPO,pmpDtlPO);
			return map;
		}
		return null;
	}
	/**
	 * 增加日志操作
	 * @param type
	 * @param pmPPO
	 * @param pmpDtlPO
	 */
	@SuppressWarnings("unchecked")
	public void doInsertPartSupplyRelProblems(int type,TtPartMakerProblemPO pmPPO,TtPartMakerProblemDtlPO pmpDtlPO ){
		TtPartMakerProblemPO tempPPPO = new TtPartMakerProblemPO();
		tempPPPO.setPartCode(pmPPO.getPartCode());
		tempPPPO.setSupplyCode(pmPPO.getSupplyCode());
		tempPPPO.setType(type);
		List<TtPartMakerProblemPO> pPMPPO = dao.select(tempPPPO);
		if(null != pPMPPO && pPMPPO.size()>0){
			//pmPPO.setProblemId(Long.parseLong(SequenceManager.getSequence("")));
			pmPPO.setNum(pPMPPO.get(0).getNum()+1);
			dao.update(tempPPPO, pmPPO);
			pmpDtlPO.setProblemId(pPMPPO.get(0).getProblemId());
		}else{
			pmPPO.setNum(1);
			pmPPO.setProblemId(Long.parseLong(SequenceManager.getSequence("")));
			dao.insert(pmPPO);
			pmpDtlPO.setProblemId(pmPPO.getProblemId());
		}
		dao.insert(pmpDtlPO);
	}
	
	
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> querymakerProblem(RequestWrapper request, Integer page,
			Integer currPage, Map params) {
		String part_name=request.getParamValue("part_name");
		String part_code=request.getParamValue("part_code");
		String supply_name=request.getParamValue("supply_name");
		String supply_code=request.getParamValue("supply_code");
		String type=request.getParamValue("type");
		String status=request.getParamValue("status");
		
		
		
		
		StringBuffer sb= new StringBuffer();
		sb.append("select p.PROBLEM_ID,\n" );
		sb.append("       p.SUPPLY_CODE,\n" );
		sb.append("       p.SUPPLY_NAME,\n" );
		sb.append("       p.PART_NAME,\n" );
		sb.append("       p.PART_CODE,\n" );
		sb.append("      p.NUM,\n" );
		sb.append("      p.CREATE_DATE,\n" );
		sb.append("      p.UPDATE_DATE,\n" );
		sb.append("\t  p.type,\n" );
		sb.append("      ( select code_desc\n" );
		sb.append("        from tc_code c where c.code_id=p.STATUS) as STATUS,\n" );
		sb.append("\t\t   REMARK from TT_PART_MAKER_PROBLEM p ");
		sb.append(" where 1=1");
		if(part_name!=null){
			sb.append(" and p.part_name like '%"+part_name+"%'");
		}
		if(part_code!=null){
			sb.append(" and p.part_code like '%"+part_code+"%'");
		}
		if(supply_name!=null){
			sb.append(" and p.supply_name like '%"+supply_name+"%'");
		}
		if(supply_code!=null){
			sb.append(" and p.supply_code like '%"+supply_code+"%'");
		}
		if(type!=null){
			sb.append(" and p.type ="+type+" ");
		}
		if(status!=null){
			sb.append(" and status = "+status+"");
		}
		
		PageResult<Map<String, Object>> ps = pageQuery(sb.toString(), null, getFunName(), Constant.PAGE_SIZE, currPage);
		return ps;
		
	}
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> querymakerProblemDetail(RequestWrapper request, Integer page,
			Integer currPage, Map params) {
		String problem_id=request.getParamValue("problem_id");
		String part_name=request.getParamValue("part_name");
		String part_code=request.getParamValue("part_code");
		String supply_name=request.getParamValue("supply_name");
		String supply_code=request.getParamValue("supply_code");
		
		
		
		StringBuffer sb=new StringBuffer();
		sb.append("select * from TT_PART_MAKER_PROBLEM_DTL \n");
		sb.append("where 1=1 \n");
		sb.append("and problem_id= "+problem_id+" \n");
		
		
		if(part_name!=null){
			sb.append(" and part_name like '%"+part_name+"%'");
		}
		if(part_code!=null){
			sb.append(" and part_code like '%"+part_code+"%'");
		}
		if(supply_name!=null){
			sb.append(" and supply_name like '%"+supply_name+"%'");
		}
		if(supply_code!=null){
			sb.append(" and supply_code like '%"+supply_code+"%'");
		}
		PageResult<Map<String, Object>> ps = pageQuery(sb.toString(), null, getFunName(), Constant.PAGE_SIZE, currPage);
		return ps;
		
		
	}
	@SuppressWarnings("unused")
	public void updateProblemRemark(RequestWrapper request) {
		String problem_id=request.getParamValue("problem_id");
		String remark=request.getParamValue("remark");
	}
	@SuppressWarnings("unchecked")
	public String findAllSignNumSum(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("select sum(t.sign_amount) as sign_amount\n" );
		sb.append("  from (select d.part_code,\n" );
		sb.append("               d.part_name,\n" );
		sb.append("               d.producer_code,\n" );
		sb.append("               d.producer_name,\n" );
		sb.append("               d.claim_no,\n" );
		sb.append("               d.claim_id,\n" );
		sb.append("               td.dealer_shortname dealer_name,\n" );
		sb.append("               td.dealer_code,\n" );
		sb.append("               sum(d.sign_amount) sign_amount\n" );
		sb.append("          from tt_as_wr_old_returned_detail d,\n" );
		sb.append("               tt_as_wr_old_returned        r,\n" );
		sb.append("               tm_dealer                    td\n" );
		sb.append("         where 1 = 1\n" );
		DaoFactory.getsql(sb, "td.dealer_code", DaoFactory.getParam(request, "dealerCode"), 6);
		DaoFactory.getsql(sb, "d.producer_name", DaoFactory.getParam(request, "supply_name"), 2);
		DaoFactory.getsql(sb, "d.claim_no", DaoFactory.getParam(request, "claim_no"), 2);
		DaoFactory.getsql(sb, "d.part_code", DaoFactory.getParam(request, "part_code"), 2);
		DaoFactory.getsql(sb, "d.part_name", DaoFactory.getParam(request, "part_name"), 2);
		DaoFactory.getsql(sb, "d.producer_code", DaoFactory.getParam(request, "SUPPLY_CODE"), 2);
		sb.append(" AND (D.qhj_flag = 0 or (D.qhj_flag = 1 and d.KCDB_FLAG = 2))\n" );
		sb.append("           and r.yieldly = 95411001\n" );
		sb.append("           and (r.status = 10811005 or\n" );
		sb.append("               (r.status = 10811004 and d.is_in_house = 10041001))\n" );
		sb.append("           AND (D.IS_CLIAM = 0 OR D.IS_CLIAM IS NULL or D.IS_CLIAM = 2)\n" );
		sb.append("           AND D.IS_OUT in (0, 2)\n" );
		sb.append("           and d.return_id = r.id\n" );
		sb.append("           and d.sign_amount = 1\n" );
		sb.append("           and td.dealer_id = r.dealer_id\n" );
		sb.append("           AND d.is_main_code = 94001001\n");
		sb.append("         GROUP BY D.PART_CODE,\n" );
		sb.append("                  D.PART_NAME,\n" );
		sb.append("                  D.PRODUCER_CODE,\n" );
		sb.append("                  D.PRODUCER_NAME,\n" );
		sb.append("                  D.CLAIM_NO,\n" );
		sb.append("                  d.claim_id,\n" );
		sb.append("                  TD.DEALER_SHORTNAME,\n" );
		sb.append("                  td.dealer_code) t");
		
		String sign_amount="";
		List<Map<String,Object>> list = this.pageQuery(sb.toString(), null, getFunName());
		if(com.infodms.yxdms.utils.DaoFactory.checkListNull(list)){
			 sign_amount = BaseUtils.checkNull(list.get(0).get("SIGN_AMOUNT"));
		}
		return sign_amount;
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findOthersPartsByOutNo(String outNo) {
		StringBuffer sb= new StringBuffer();
		sb.append("select p.part_code, p.part_name, count(1) as part_num\n" );
		sb.append("  from Tt_As_Wr_Partsitem p, Tt_As_Wr_Old_Out_Part t\n" );
		sb.append(" where p.responsibility_type = 94001002\n" );
		sb.append("   and p.part_code = t.out_part_code\n" );
		sb.append("   and t.claim_no =\n" );
		sb.append("       (select a.claim_no from Tt_As_Wr_Application a where a.id = p.id)\n" );
		sb.append("   and t.out_no in (select s.out_no\n" );
		sb.append("                      from TT_AS_WR_RANGE_SINGLE s\n" );
		sb.append("                     where s.out_no = '"+outNo+"')\n" );
		sb.append(" group by p.part_code, p.part_name");
		return  this.pageQuery(sb.toString(), null, this.getFunName());
	}
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>>  findLogInfoByclaimNo(RequestWrapper request, Integer pageSize, Integer currPage) {
			String claimNo=DaoFactory.getParam(request, "claimNo");
			StringBuffer sb = new StringBuffer();
			sb.append("select  * from Log_Upate_Part_Product_Code c left join Tc_user tc on tc.user_id = c.user_id\n");
			sb.append(" where 1=1 ");
			sb.append(" and  c.claim_id=(select a.id from Tt_As_Wr_Application a where a.claim_no='"+claimNo+"')");
			return this.pageQuery(sb.toString(), null, getFunName(), pageSize, currPage);
	}
	@SuppressWarnings("unchecked")
	public PageResult<TmPtSupplierPO> querySupplier1(RequestWrapper request,Integer currPage, Integer pageSize) {
		String partCode =DaoFactory.getParam(request, ("partCode"));// request.getParamValue("partCode");
		String partCodeTemp =DaoFactory.getParam(request, ("partCodeTemp"));
		String yieldly = request.getParamValue("yieldly");
		String supplierCode = request.getParamValue("SUPPLIER_CODE"); //供应商代码
		String supplierName = request.getParamValue("SUPPLIER_NAME"); //供应商名称
		String count = request.getParamValue("count"); //判断是否全查
		StringBuffer sql= new StringBuffer();
		sql.append("select distinct * from ( ");
		sql.append("(select c.* from ( select c.maker_id   as supplier_id,  c.maker_code supplier_code, c.maker_name supplier_name,\n" );
		sql.append(" (select count(1)+1 from tt_part_maker_relation r where r.maker_id =c.maker_id and r.part_id=\n" );
		sql.append("       ( select pb.part_id\n" );
		sql.append("                  from tm_pt_part_base pb\n" );
		sql.append("                 where pb.part_is_changhe = 95411001 and pb.part_code='')) as is_del\n" );
		sql.append("     from tt_part_maker_define  c\n" );
		sql.append(" where 1=1");
		sql.append("and c.maker_id in (\n");
		sql.append("  select t.maker_id  from tt_part_maker_relation t  where t.part_id in (\n" );
		sql.append("	select pb.part_id  from tm_pt_part_base pb where pb.part_is_changhe="+yieldly+" \n" );
		if(Utility.testString(partCode)){
			if (Integer.valueOf(count)==1) {
				String partCode2 = "";
				String partCode3 = "";
				String partCode4 = "";
				if(partCode.length()==18){
					//17010510-C01-B00-3 18位的包含B00的件取17010510-C01-000-3的价格
					if("B00".equals(partCode.substring(13,16))){
						partCode2 = partCode.substring(0, 13)+"000"+partCode.substring(16, 18);
						partCode3 = partCode2;
						partCode4 = partCode2;
					}else{
						partCode2 = partCode.substring(0, partCode.length()-3)+"000";
						partCode3 = partCode.substring(0, partCode.length()-3)+"B00";
						partCode4 = partCode.substring(0, partCode.length()-3)+"B0Y";
					}
				}else{
					partCode2 = partCode.substring(0, partCode.length()-3)+"000";
					partCode3 = partCode.substring(0, partCode.length()-3)+"B00";
					partCode4 = partCode.substring(0, partCode.length()-3)+"B0Y";
				}
				sql.append("  and pb.part_code in ('"+partCode+"','"+partCode2+"','"+partCode3+"','"+partCode4+"')\n" );
			}
		}
		sql.append("))\n" );
		DaoFactory.getsql(sql, "c.maker_code", supplierCode, 2);
		DaoFactory.getsql(sql, "c.maker_name", supplierName, 2);
		sql.append(") c  )");
		
		sql.append("union all (select c.* from ( select c.maker_id   as supplier_id,  c.maker_code supplier_code, c.maker_name supplier_name,\n" );
		sql.append(" (select count(1) from tt_part_maker_relation r where r.maker_id =c.maker_id and r.part_id=\n" );
		sql.append("       ( select pb.part_id\n" );
		sql.append("                  from tm_pt_part_base pb\n" );
		sql.append("                 where pb.part_is_changhe = 95411001 and pb.part_code='"+partCodeTemp+"')) as is_del\n ");
		sql.append("     from tt_part_maker_define  c\n" );
		sql.append(" where 1=1");
		sql.append("and c.maker_id in (\n" );
		sql.append("  select t.maker_id  from tt_part_maker_relation t  where t.part_id in (\n" );
		sql.append("	select pb.part_id  from tm_pt_part_base pb where pb.part_is_changhe="+yieldly+" \n" );
		sql.append("))\n" );
		DaoFactory.getsql(sql, "c.maker_code", supplierCode, 2);
		DaoFactory.getsql(sql, "c.maker_name", supplierName, 2);
		sql.append(") c\n" );
		sql.append(")");
		sql.append(") a order by a.is_del desc");
		

		
		PageResult<TmPtSupplierPO> ps = pageQuery(TmPtSupplierPO.class,sql.toString(), null, pageSize, currPage);
		return ps;
	}
	public void insertRecodes(String id, AclUserBean loginUser) {
		List<Map<String,Object>> list=this.findInsertPoValues(id);
		String isBc="否";
		this.insertPo(0.0, list, loginUser,isBc);
	}
	

}
