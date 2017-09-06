package com.infodms.dms.dao.claim.oldPart;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.report.dmsReport.ApplicationDao;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ClaimOldPartOutPreListBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TtAsPartBorrowStorePO;
import com.infodms.dms.po.TtAsWrBarcodePartStockLPO;
import com.infodms.dms.po.TtAsWrOldReturnedDetailPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class ClaimOldPartStorageManagerDao extends BaseDao {
	public static Logger logger = Logger.getLogger(ClaimOldPartStorageManagerDao.class);

	private static final ClaimOldPartStorageManagerDao dao = null;

	public static final ClaimOldPartStorageManagerDao getInstance() {
		if (dao == null)
			return new ClaimOldPartStorageManagerDao();
		return dao;
	}
	/**
	 * Function：查询库存信息
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@param curPage
	 * @return:		@param pageSize
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-18
	 */
public PageResult<ClaimOldPartOutPreListBean> getCurStoreList(RequestWrapper request,int curPage, int pageSize){
			String supply_code = DaoFactory.getParam(request, "supply_code");
			String supply_name = DaoFactory.getParam(request,"supply_name");
			String part_code  =  DaoFactory.getParam(request,"part_code");
			String part_name  =  DaoFactory.getParam(request,"part_name");
			String isMainCode =  DaoFactory.getParam(request,"IS_MAIN_CODE");
			String yieldly    =  DaoFactory.getParam(request,"YIELDLY_TYPE");// 产地
			
			StringBuffer sb= new StringBuffer();
			sb.append("select * from (select f.down_product_code supply_code,\n" );
			sb.append("       f.down_product_name supply_name,\n" );
			sb.append("       f.down_part_code part_code,\n" );
			sb.append("       f.down_part_name part_name,\n" );
			sb.append("       sum(case\n" );
			sb.append("             when d.qhj_flag != 1 then\n" );
			sb.append("              1\n" );
			sb.append("             when d.qhj_flag = 1 and d.kcdb_flag = 2 then\n" );
			sb.append("              1\n" );
			sb.append("             else\n" );
			sb.append("              0\n" );
			sb.append("           end) all_in_amount,\n" );
			sb.append("       max(r.yieldly) yieldly,\n" );
			sb.append("\n" );
			sb.append("       sum(case\n" );
			sb.append("             when d.is_out = 0 and d.is_cliam = 0 and d.qhj_flag != 1 then\n" );
			sb.append("              1\n" );
			sb.append("             when d.is_out = 0 and d.is_cliam = 0 and d.qhj_flag = 1 and\n" );
			sb.append("                  d.kcdb_flag = 2 then\n" );
			sb.append("              1\n" );
			sb.append("             else\n" );
			sb.append("              0\n" );
			sb.append("           end) all_amount,\n" );
			sb.append("\n" );
			sb.append("sum(case\n" );
			sb.append("\n" );
			sb.append("                     when d.is_out = 0 and d.is_cliam = 0 and d.qhj_flag = 1 and\n" );
			sb.append("                          (d.kcdb_flag = 0 or d.kcdb_flag = 1)  then\n" );
			sb.append("                      1\n" );
			sb.append("                     else\n" );
			sb.append("                      0\n" );
			sb.append("                   end) all_amount_kc,");
			sb.append("       sum(case\n" );
			sb.append("             when d.is_out = 1 then\n" );
			sb.append("              1\n" );
			sb.append("             else\n" );
			sb.append("              0\n" );
			sb.append("           end) all_out_amount,\n" );
			sb.append("       sum(f.balance_amount) part_Price\n" );
			sb.append("  from tt_as_wr_old_returned_detail d\n" );
			sb.append("  left join tt_as_wr_partsitem f\n" );
			sb.append("    on d.claim_id = f.id\n" );
			sb.append("   and d.part_code = f.down_part_code, tt_as_wr_old_returned r,\n" );
			sb.append(" tm_dealer td\n" );
			sb.append(" where 1 = 1 and d.sign_amount = 1\n" );
			DaoFactory.getsql(sb, "d.producer_code", supply_code, 2);
			DaoFactory.getsql(sb, "d.producer_name", supply_name, 2);
			DaoFactory.getsql(sb, "d.is_Main_Code", isMainCode, 1);
			DaoFactory.getsql(sb, "r.yieldly", yieldly, 1);
			DaoFactory.getsql(sb, "d.part_code", part_code.toUpperCase(), 2);
			DaoFactory.getsql(sb, "d.part_name", part_name.toUpperCase(), 2);
			sb.append("   and td.dealer_id = r.dealer_id\n" );
			sb.append("   and d.return_id = r.id\n" );
			sb.append("   and (r.status = 10811005 or\n" );
			sb.append("       (r.status = 10811004 and d.is_in_house = 10041001))\n" );
			sb.append("   and r.yieldly = 95411001\n" );
			sb.append(" group by f.down_product_code,\n" );
			sb.append("          f.down_product_name,\n" );
			sb.append("          f.down_part_code,\n" );
			sb.append("          f.down_part_name) m");
			PageResult<ClaimOldPartOutPreListBean> pr=pageQuery(ClaimOldPartOutPreListBean.class, sb.toString(), null, pageSize, curPage);
			return pr;
    }



public  Map<String, Object>  getCurStoreList01(RequestWrapper request,int curPage, int pageSize){
	String supply_code = DaoFactory.getParam(request, "supply_code");
	String supply_name = DaoFactory.getParam(request,"supply_name");
	String part_code  =  DaoFactory.getParam(request,"part_code");
	String part_name  =  DaoFactory.getParam(request,"part_name");
	String isMainCode =  DaoFactory.getParam(request,"IS_MAIN_CODE");
	String yieldly    =  DaoFactory.getParam(request,"YIELDLY_TYPE");// 产地
	
	StringBuffer sql= new StringBuffer();
	sql.append("select\n" );
	sql.append("       sum(case\n" );
	sql.append("             when d.is_out = 0 and d.is_cliam = 0 and d.qhj_flag != 1 then\n" );
	sql.append("              1\n" );
	sql.append("             when d.is_out = 0 and d.is_cliam = 0 and d.qhj_flag = 1 and\n" );
	sql.append("                  d.kcdb_flag = 2 then\n" );
	sql.append("              1\n" );
	sql.append("             else\n" );
	sql.append("              0\n" );
	sql.append("           end) ALL_OUT_AMOUNT,\n" );
	sql.append("sum(case\n" );
	sql.append("\n" );
	sql.append("                     when d.is_out = 0 and d.is_cliam = 0 and d.qhj_flag = 1 and\n" );
	sql.append("                          (d.kcdb_flag = 0 or d.kcdb_flag = 1)  then\n" );
	sql.append("                      1\n" );
	sql.append("                     else\n" );
	sql.append("                      0\n" );
	sql.append("                   end) all_amount_kc,");

	sql.append("  sum(f.balance_amount) partPrice\n" );
	sql.append("\n" );
	sql.append("from  tt_as_wr_old_returned_detail d  left join tt_as_wr_partsitem f\n" );
	sql.append("on  d.claim_id = f.id and d.part_code = f.down_part_code\n" );
	sql.append(" ,tt_as_wr_old_returned r , tm_dealer td    where\n" );
	sql.append(" 1=1 and d.sign_amount = 1\n" );
	DaoFactory.getsql(sql, "d.producer_code", supply_code, 2);
	DaoFactory.getsql(sql, "d.producer_name", supply_name, 2);
	DaoFactory.getsql(sql, "d.is_Main_Code", isMainCode, 1);
	DaoFactory.getsql(sql, "r.yieldly", yieldly, 1);
	DaoFactory.getsql(sql, "d.part_code", part_code.toUpperCase(), 2);
	DaoFactory.getsql(sql, "d.part_name", part_name.toUpperCase(), 2);
	sql.append("  and td.dealer_id  = r.dealer_id\n" );
	sql.append(" and d.return_id = r.id and (r.status=10811005 or (r.status=10811004 and d.is_in_house=10041001))\n" );
	sql.append(" and  r.yieldly = 95411001  \n" );
	
	Map<String, Object> pr=pageQueryMap( sql.toString(), null,getFunName());
	return pr;
}
	public List<Map<String, Object>> getCurStoreTotalExportList(Map params){
		String company_id=ClaimTools.dealParamStr(params.get("company_id"));
		String supply_name=ClaimTools.dealParamStr(params.get("supply_name"));//查询条件--供应商简称
		String part_code=ClaimTools.dealParamStr(params.get("part_code"));//查询条件--配件代码
		String part_name=ClaimTools.dealParamStr(params.get("part_name"));//查询条件--配件名称
		String yieldlys = ClaimTools.dealParamStr(params.get("yieldlys"));//用户拥有的产地权限
		String yieldly =  ClaimTools.dealParamStr(params.get("yieldly"));//用户拥有的产地权限
		StringBuffer sql=new StringBuffer();
		sql.append("select sum(d.sign_amount) all_amount,\n");
		sql.append("       c.code_desc yieldly,\n");
		sql.append("       PS.Maker_Name supply_name,\n");
		sql.append("       b.part_code,\n");
		sql.append("       b.part_name,\n");
		sql.append("       d.claim_no,\n");
		sql.append("       gs.MODEL_NAME,\n");
		sql.append("       dd.dealer_code,\n");
		sql.append("       dd.dealer_name\n");
		sql.append("  from tt_as_wr_old_returned_detail d,\n");
		sql.append("       tt_part_maker_define         ps,\n");
		sql.append("       Tm_Pt_Part_Base              b,\n");
		sql.append("       tt_as_wr_old_returned        r,\n");
		sql.append("       tm_vehicle                   v,\n");
		sql.append("       vw_material_group_service    gs,\n");
		sql.append("       tm_dealer                    dd,tc_code c \n");
		sql.append(" where 1 = 1\n");
		sql.append("   and v.vin = d.vin\n");
		sql.append("   and dd.dealer_id = r.dealer_id\n");
		sql.append("   and gs.PACKAGE_ID = v.package_id\n");
		sql.append("   and r.id = d.return_id\n");
		sql.append("   and d.producer_code = PS.Maker_Code(+)\n");
		sql.append("   and d.part_code = b.part_code(+)"); 
		sql.append("  and b.is_cliam="+Constant.IS_CLAIM_01);
		sql.append("  and r.status="+Constant.BACK_LIST_STATUS_05);
		sql.append("  and b.is_del=0 and d.sign_amount>0\n" );
		sql.append("  and (d.is_cliam =0 or d.is_cliam is null )\n");
		sql.append("  and d.is_out=0 and c.code_id = r.yieldly\n");
		if(Utility.testString(supply_name)){
			sql.append("  and ps.supplier_name like '%"+supply_name+"%'\n");
		}
		if(Utility.testString(yieldly)){
			sql.append("  and r.yieldly ="+yieldly+"\n");
		}
		if(Utility.testString(part_code)){
			sql.append("  and b.part_code like '%"+part_code+"%'\n");
		}
		if(Utility.testString(part_name)){
			sql.append("  and b.part_name like '%"+part_name+"%'\n");
		}
		sql.append("group by c.code_desc,\n");
		sql.append("          ps.maker_name,\n");
		sql.append("          b.part_code,\n");
		sql.append("          b.part_name,\n");
		sql.append("          d.claim_no,\n");
		sql.append("          gs.MODEL_NAME,\n");
		sql.append("          dd.dealer_code,\n");
		sql.append("          dd.dealer_name\n");
		sql.append(" order by b.part_code"); 
		List<Map<String, Object>> list = pageQuery(sql.toString(), null,getFunName());
        return list;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	
	
	public PageResult<Map<String, Object>>   getBarcodeCurStoreList(Map params,int curPage, int pageSize){
		String stock_type=ClaimTools.dealParamStr(params.get("stock_type"));//查询条件--供应商简称
		String part_name=ClaimTools.dealParamStr(params.get("part_name"));//查询条件--配件名称
		String yieldly=ClaimTools.dealParamStr(params.get("yieldly"));//查询条件--产地
		String part_code=ClaimTools.dealParamStr(params.get("part_code"));//查询条件--配件代码
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select a.yieldly,a.part_name, a.part_code,count(a.id) as count_all from TT_AS_WR_BARCODE_PART_STOCK a where a.is_library is  null " );
				
		if(Utility.testString(stock_type)){
			sqlStr.append("and    a.stock_type="+stock_type+" \n" );
		}
		if(!"".equals(part_code)){
			sqlStr.append("and a.part_code like '%"+part_code.replaceAll("'", "\''")+"%'\n" );
		}
		if(Utility.testString(part_name)){
			sqlStr.append("and a.part_name like'%"+part_name.replaceAll("'", "\''")+"%'\n" );
		}
		if(Utility.testString(yieldly))
			sqlStr.append("and a.yieldly ='"+yieldly+"'\n" );								
				sqlStr.append("group by a.yieldly,a.part_name, a.part_code\n" );
				PageResult<Map<String, Object>> ps = pageQuery(sqlStr.toString(), null, getFunName(), pageSize, curPage);
				return ps;
    }
	
	
	
	
	public PageResult<Map<String, Object>>   queryScanningList(String barcodeNo,int curPage, int pageSize){
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select a.claim_no,a.claim_id,a.part_code,a.part_name,a.box_no,a.barcode_no,a.deductible_reason_code From tt_as_wr_old_returned_detail a where  a.is_upload is not null  and a.is_storage is null and  a.barcode_no= '"+ barcodeNo+"'");
		PageResult<Map<String, Object>> ps = pageQuery(sqlStr.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public void updateScanningSave(String barcodeNo,String CLAIM_TYPE){
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("update tt_as_wr_old_returned_detail set DEDUCTIBLE_REASON_CODE="+CLAIM_TYPE+",SIGN_AMOUNT=0,deduct_remark="+CLAIM_TYPE+" where barcode_No="+barcodeNo);
		update(sqlStr.toString(), null);
	}
	
	public void updateScanningSaveZC(String barcodeNo){
		
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("update tt_as_wr_old_returned_detail set DEDUCTIBLE_REASON_CODE='',SIGN_AMOUNT=1,deduct_remark='' where barcode_No="+barcodeNo);
		update(sqlStr.toString(), null);
	}
	
	
	public void outOfStore(String num,String exitScrap,String partCodeStr){
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("update TT_AS_WR_BARCODE_PART_STOCK set is_library='"+Constant.STATUS_ENABLE+"',STOCK_TYPE= '"+exitScrap+"' where part_code='"+partCodeStr+"' and   is_library is null  and  rownum <="+num);
		update(sqlStr.toString(), null);
	}
	
	
	public void outOfStoreDetail(String num,String exitScrap,String partCodeStr,Long userId){
		TmPtPartBasePO tppb=new TmPtPartBasePO();
		tppb.setPartCode(partCodeStr);
		List<TmPtPartBasePO> ls=select(tppb);
		
		
		TtAsWrBarcodePartStockLPO tabpsl=new TtAsWrBarcodePartStockLPO();
		
		String Id = SequenceManager.getSequence("");
    	
		tabpsl.setCreateBy(userId);
		tabpsl.setId(Long.valueOf(Id));
		tabpsl.setCreateDate(new Date());
		tabpsl.setPartCode(partCodeStr);
		tabpsl.setPartName(ls.get(0).getPartName());
		tabpsl.setStockNum(Long.valueOf(num));
		tabpsl.setStockType(Long.valueOf(exitScrap));
		tabpsl.setStockDate(new Date());
		
		insert(tabpsl);
	}
	
	public PageResult<Map<String, Object>> StockDetailQuery(String partName,String partCode,String stockType,String create_start_date,String create_end_date,int curPage, int pageSize){
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select PART_NAME,PART_CODE,STOCK_TYPE,TO_CHAR(STOCK_DATE,'YYYY-MM-DD') AS STOCK_DATE,STOCK_NUM From TT_AS_WR_BARCODE_PART_STOCK_L where 1=1 ");
		if(Utility.testString(partName)){
			sqlStr.append("and    PART_NAME='"+partName+"'\n" );
		}
		if(Utility.testString(stockType)){
			sqlStr.append("and    STOCK_TYPE="+stockType+" \n" );
		}
		if(Utility.testString(partCode)){
			sqlStr.append("and    PART_CODE='"+partCode+"'\n" );
		}
		
		if(create_start_date!=null&&!"".equals(create_start_date))
			sqlStr.append(" and STOCK_DATE>=to_date('" + create_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		if(create_end_date!=null&&!"".equals(create_end_date))
			sqlStr.append(" and STOCK_DATE<=to_date('" + create_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		
		PageResult<Map<String, Object>> ps = pageQuery(sqlStr.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>>   BJMaterialMaintainQuery(String groupCode,String groupName,int curPage, int pageSize){
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select b.ID,a.group_code,a.group_name From TM_VHCL_MATERIAL_GROUP a,tt_as_wr_BJ_barcode_MATERIAL b where a.group_id=b.group_id ");
		
		if(Utility.testString(groupCode)){
			sqlStr.append(" and    a.group_code='"+groupCode+"'\n" );
			
		}
		
		if(Utility.testString(groupName)){
			sqlStr.append(" and    a.group_name='"+groupName+"'\n" );
			
		}
		PageResult<Map<String, Object>> ps = pageQuery(sqlStr.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>>   BJMaterialMaintainAddQuery(String groupCode,String groupName,int curPage, int pageSize){
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append(" select a.group_id,a.group_code,a.group_name From TM_VHCL_MATERIAL_GROUP a where a.group_level=3 and not  exists(select group_id from tt_as_wr_BJ_barcode_MATERIAL b where a.group_id=b.group_id) ");
		
		if(Utility.testString(groupCode)){
			sqlStr.append(" and    a.group_code='"+groupCode+"'\n" );
			
		}
		
		if(Utility.testString(groupName)){
			sqlStr.append(" and    a.group_name='"+groupName+"'\n" );
			
		}
		PageResult<Map<String, Object>> ps = pageQuery(sqlStr.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public void BJMaterialMaintainAddSave(String id,Long userId){
		String sql="insert into tt_as_wr_BJ_barcode_MATERIAL(id,group_id,create_by,create_date) values (f_getid(),'"+id+"','"+userId+"',sysdate)";
		
		this.insert(sql);
	}
	
	public void BJMaterialMaintainDelete(String id){
		String sql="delete tt_as_wr_BJ_barcode_MATERIAL where id="+id;
		this.delete(sql, null);
	}
	
	//得到页面的基础信息
	public List<ClaimOldPartOutPreListBean> getBaseBean(String partCode,String code,String yieldly,Long companyId){
		StringBuffer sql=new StringBuffer();
		sql.append("select distinct d.producer_name supply_name, r. yieldly  ,d.part_code,d.part_name\n");
		sql.append("from  tt_as_wr_old_returned_detail d ,tt_as_wr_old_returned r\n");
		sql.append("where d.return_id = r.id\n");
		sql.append(" and r.yieldly="+yieldly+"\n");
		sql.append(" and d.part_code='"+partCode+"'\n");
		sql.append(" and d.producer_code='"+code+"'\n");
		return this.select(ClaimOldPartOutPreListBean.class, sql.toString(), null);
	}
	//索赔单明细查询
	public List<ClaimOldPartOutPreListBean> getCliamDetail(String partCode,String code,String yieldly,Long companyId,String main){
		StringBuffer sql = new StringBuffer(); 
		sql.append("select sum(d.sign_amount) all_amount,r.return_type,d.claim_no,a.id,dd.dealer_code,a.claim_type,dd.dealer_shortname dealer_name,A.MODEL_cODE MODEL_NAME,a.vin,\n");
		sql.append(" D.PRODUCER_NAME SUPPLY_NAME,\n");
		sql.append(" D.PRODUCER_CODE SUPPLY_CODE,  D.PART_CODE, D.PART_NAME\n");
		// ===================2015.09.18增加是否切换件=============开始
		sql.append(" ,d.qhj_flag \"QHJ_FLAG\",decode(d.qhj_flag,1,'是','否') \"IS_QHJ\" ");
		// ===================2015.09.18增加是否切换件=============结束
		sql.append("  from tt_as_wr_old_returned_detail d,\n");
		sql.append("       tt_as_wr_old_returned        r,\n");
		sql.append("       tm_dealer dd,\n");
		sql.append("      TT_AS_WR_APPLICATION A\n");
		sql.append(" where 1 = 1\n");
		sql.append(" and A.CLAIM_NO = D.CLAIM_NO\n");
		sql.append("   and dd.dealer_id = r.dealer_id\n");
		sql.append("   and r.id = d.return_id and (a.urgent = 0 or a.urgent = 2)\n"); //加入标识
		/*sql.append("   AND d.is_main_code="+main+"\n");*/
		sql.append("  and (r.status="+Constant.BACK_LIST_STATUS_05+" or (r.status="+Constant.BACK_LIST_STATUS_04+" and d.is_in_house="+Constant.IF_TYPE_YES+"))\n");
		sql.append("   and d.sign_amount > 0\n");
		sql.append("   and (d.is_cliam = 0 or d.is_cliam is null)\n");
		sql.append("   and d.is_out = 0\n");
		sql.append("   and d.part_code='"+partCode+"'\n");
		sql.append("   and d.producer_code='"+code+"'\n");
		sql.append("   and r.yieldly="+yieldly+"\n");
		sql.append("   group by d.claim_no,dd.dealer_code,dd.dealer_shortname,r.return_type,a.id,A.MODEL_cODE,a.claim_type,a.vin,D.PRODUCER_NAME , D.PRODUCER_CODE , D.PART_CODE, D.PART_NAME");
		// ===================2015.09.18增加是否切换件=============开始
		sql.append(" ,d.qhj_flag ");
		// ===================2015.09.18增加是否切换件=============结束 
		return this.select(ClaimOldPartOutPreListBean.class, sql.toString(), null);
	}
	//索赔单明细查询
	public List<ClaimOldPartOutPreListBean> getCliamDetai2(String partCode,String code,String yieldly,Long companyId,String main){
		StringBuffer sql = new StringBuffer(); 
		sql.append("select sum(d.sign_amount) all_amount,d.claim_no,a.id,dd.dealer_code,a.claim_type,dd.dealer_shortname dealer_name,A.MODEL_cODE MODEL_NAME,a.vin,\n");
		sql.append(" D.PRODUCER_NAME SUPPLY_NAME,\n");
		sql.append(" D.PRODUCER_CODE SUPPLY_CODE,  D.PART_CODE, D.PART_NAME\n"); 
		
		sql.append("  from tt_as_wr_old_returned_detail d,\n");
		sql.append("       tt_as_wr_old_returned        r,\n");
		sql.append("       tm_dealer dd,\n");
		sql.append("      TT_AS_WR_APPLICATION A\n");
		sql.append(" where 1 = 1\n");
		sql.append(" and A.CLAIM_NO = D.CLAIM_NO\n");
		sql.append("   and dd.dealer_id = r.dealer_id\n");
		sql.append("   and r.id = d.return_id and a.urgent = 1\n"); //加入标识
		sql.append("   AND d.is_main_code="+main+"\n");
		sql.append("  and (r.status="+Constant.BACK_LIST_STATUS_05+" or (r.status="+Constant.BACK_LIST_STATUS_04+" and d.is_in_house="+Constant.IF_TYPE_YES+"))\n");
		sql.append("   and d.sign_amount > 0\n");
		sql.append("   and (d.is_cliam = 0 or d.is_cliam is null)\n");
		sql.append("   and d.is_out = 0\n");
		sql.append("   and d.part_code='"+partCode+"'\n");
		sql.append("   and d.producer_code='"+code+"'\n");
		sql.append("   and r.yieldly="+yieldly+"\n");
		sql.append("   group by d.claim_no,dd.dealer_code,dd.dealer_shortname,a.id,A.MODEL_cODE,a.claim_type,a.vin,D.PRODUCER_NAME , D.PRODUCER_CODE , D.PART_CODE, D.PART_NAME"); 
		return this.select(ClaimOldPartOutPreListBean.class, sql.toString(), null);
	}
	/***
	 * 查询紧接调件的库存
	 * @param request
	 * @param pageSize
	 * @param currPage
	 * @return
	 */
	public PageResult<ClaimOldPartOutPreListBean> queryCurBorrowStoreList(RequestWrapper request, Integer pageSize, Integer currPage) {
		String supply_code = DaoFactory.getParam(request, "supply_code");
		String supply_name = DaoFactory.getParam(request, "supply_name");
		String part_code = DaoFactory.getParam(request, "part_code");
		String part_name = DaoFactory.getParam(request, "part_name");
		String isMainCode = DaoFactory.getParam(request, "IS_MAIN_CODE");
		String yieldly = DaoFactory.getParam(request, "YIELDLY_TYPE");// 产地
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT SUM(D.SIGN_AMOUNT) ALL_AMOUNT,\n");
		sql.append("  D.PRODUCER_CODE SUPPLY_CODE,\n");
		sql.append("  D.PART_CODE,\n");
		sql.append("  D.PART_NAME,\n");
		sql.append("  D.PRODUCER_NAME SUPPLY_NAME,\n");
		sql.append("  R.YIELDLY,\n");
		sql.append("  SUM(CASE WHEN TO_CHAR(R.IN_WARHOUSE_DATE, 'yyyy-mm-dd') =\n");
		sql.append("   TO_CHAR(SYSDATE, 'yyyy-MM-dd') THEN D.SIGN_AMOUNT  ELSE \t0  END) DAY_IN, --当天入库数量\n");
		sql.append("  SUM(CASE \t WHEN R.IN_WARHOUSE_DATE >= TRUNC(SYSDATE, 'MM') THEN\n");
		sql.append("  D.SIGN_AMOUNT  ELSE \t0  END) MONTH_IN ,--本月入库数量\n");
		if(Constant.RESPONS_NATURE_STATUS_01.toString().equals(isMainCode)){
			sql.append("     ROUND(SUM(NVL(L.Balance_Amount, 0)) * max(nvl(mdr.labour_xs,0)), 2) +\n");
			sql.append("   SUM(NVL(NE.AMOUNT, 0))   +\tsum(nvl(reg.part_amount,0)) +\n");
			sql.append("   sum(decode(reg.wr_labourcode,p.wr_labourcode,0,nvl(reg.labour_amount,0))) +\n");
			sql.append("     SUM(NVL(AAD.PRICE,0))+SUM(nvl(com.pass_price,0))+\n");
			sql.append("  round(SUM(D.SIGN_AMOUNT)*nvl(mdr.claim_price,0)*nvl(mdr.part_xs,0),2) ");
		}else{
			sql.append( " 0 " );
		}
		sql.append("  part_price\n");
		
		sql.append(" FROM TT_AS_WR_OLD_RETURNED_DETAIL D\n");
		sql.append(" LEFT JOIN TT_AS_WR_APPLICATION A ON d.claim_no = A.CLAIM_NO  \n");
		sql.append("     LEFT JOIN (SELECT substr(pd.part_oldcode,0,length(pd.part_oldcode)-3) part_code ,md.maker_code ,\n");
		sql.append("  max(mr.claim_price)claim_price,max(mr.part_xs) part_xs,max(mr.labour_xs)labour_xs\n");
		sql.append("    FROM tt_part_define pd\n");
		sql.append("  LEFT JOIN tt_part_maker_relation mr ON mr.part_id = pd.part_id\n");
		sql.append("  LEFT JOIN tt_part_maker_define md ON md.maker_id = mr.maker_id\n");
		sql.append("  GROUP BY substr(pd.part_oldcode,0,length(pd.part_oldcode)-3)  ,md.maker_code ) mdr  ON mdr.part_code = substr(d.part_code,0,length(d.part_code)-3) AND mdr.maker_code = d.producer_code\n");
		sql.append("         LEFT JOIN (SELECT N.ID, N.MAIN_PART_CODE, SUM(N.AMOUNT) AMOUNT\n");
		sql.append("               FROM TT_AS_WR_NETITEM N\n");
		sql.append("              GROUP BY N.ID, N.MAIN_PART_CODE) NE ON A.ID = NE.ID  \n");
		sql.append("                                                 AND d.part_code =\n");
		sql.append("                                                     NE.MAIN_PART_CODE\n");
		sql.append("             LEFT JOIN (SELECT SUM(NVL(AD.PRICE,0)) PRICE,AD.CLAIM_NO ,ad.main_part_code\n");
		sql.append("  FROM TT_CLAIM_ACCESSORY_DTL AD GROUP BY AD.CLAIM_NO,ad.main_part_code ) AAD   ON AAD.CLAIM_NO = A.CLAIM_NO and aad.main_part_code=d.part_code\n");
		sql.append("  left join (select sum(nvl(awp.balance_amount,0)) part_amount,sum(nvl(wl.balance_amount,0))labour_amount,\n");
		sql.append("  a.claim_no,awp.down_part_code,awp.wr_labourcode\n");
		sql.append("   from TT_AS_WR_PARTSITEM    AWP,  TT_AS_WR_LABOURITEM   WL, Tt_As_Wr_Application a\n");
		sql.append("  where AWP.WR_LABOURCODE = WL.WR_LABOURCODE\n");
		sql.append("              AND AWP.ID = WL.ID and a.id = awp.id and a.id = wl.id   and awp.responsibility_type=94001002\n");
		sql.append("  group by a.claim_no,awp.down_part_code,awp.wr_labourcode\n");
		sql.append("  ) reg  on reg.claim_no=d.claim_no\n");
		sql.append("  LEFT JOIN (SELECT SUM(nvl(t.pass_price,0)) pass_price,t.part_code,t.claim_no\n");
		sql.append("  FROM TT_AS_WR_COMPENSATION_APP T\n");
		sql.append("  GROUP BY t.part_code,t.claim_no)  com ON com.claim_no =d.claim_no  AND com.part_code = d.part_code,\n");
		sql.append("        TT_AS_WR_PARTSITEM P,\n");
		sql.append("       TT_AS_WR_LABOURITEM L,\n");
		sql.append("  TT_AS_WR_OLD_RETURNED R\n");
		sql.append(" WHERE 1=1\n");
		sql.append("   AND A.ID = P.ID\n");
		sql.append("   AND P.WR_LABOURCODE = L.WR_LABOURCODE\n");
		sql.append("   AND A.ID = L.ID \n");
		sql.append("   AND d.part_code= P.DOWN_PART_CODE\n");
		sql.append("\t AND R.ID = D.RETURN_ID"); 

		sql.append("  and (r.status="+Constant.BACK_LIST_STATUS_05+" or (r.status="+Constant.BACK_LIST_STATUS_04+" and d.is_in_house="+Constant.IF_TYPE_YES+"))\n");
		sql.append("  and  d.sign_amount>0\n" );
		sql.append("  and (d.is_cliam =0 or d.is_cliam is null )\n");
		sql.append("  and d.is_out=0 and a.urgent =1\n");
		//==================zyw 加标示切换件和库存调拨
		sql.append("   AND (D.qhj_flag = 0 or (D.qhj_flag = 1 and d.KCDB_FLAG=2))\n");
		DaoFactory.getsql(sql, "d.producer_code", supply_code, 2);
		DaoFactory.getsql(sql, "d.producer_name", supply_name, 2);
		DaoFactory.getsql(sql, "d.is_main_code", isMainCode, 1);
		DaoFactory.getsql(sql, "r.yieldly", yieldly, 1);
		DaoFactory.getsql(sql, "d.part_code", part_code.toUpperCase(), 2);
		DaoFactory.getsql(sql, "d.part_name", part_name, 2);
		sql.append("  group by d.producer_code ,  d.PART_CODE, d.PART_NAME, d.producer_name , R.YIELDLY,	nvl(mdr.claim_price,0),nvl(mdr.part_xs,0)\n"); 
		PageResult<ClaimOldPartOutPreListBean> ps=pageQuery(ClaimOldPartOutPreListBean.class, sql.toString(), null, pageSize, currPage);
		return ps;
	}
	public PageResult<Map<String, Object>> queryListBorrowCallOutData(RequestWrapper request, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select taword.id,taword.claim_no,taword.vin,\n" );
		sb.append("tawp.part_code,tawp.part_name,tawp.producer_code,taword.return_amount,\n" );
		sb.append("taword.sign_amount,TAWORD.LOCAL_WAR_HOUSE||TAWORD.LOCAL_WAR_SHEL||TAWORD.LOCAL_WAR_LAYER Local_War_House,\n" );
		sb.append("taword.box_no,taword.warehouse_region,taword.deduct_remark,nvl(tc.code_desc,'') deduct_desc\n" );
		sb.append("from tt_as_wr_old_returned tawor,\n" );
		sb.append("tt_as_wr_old_returned_detail taword,\n" );
		sb.append("tt_as_wr_partsitem tawp,tc_code tc\n" );
		sb.append("where tawor.id=taword.return_id and taword.part_id=tawp.part_id and taword.DEDUCT_REMARK=tc.code_id(+)\n" );
		sb.append("and tawor.return_type=10731001\n" );
		sb.append("and tawor.status=10811005\n" );
		sb.append("order by taword.create_date");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	public PageResult<Map<String, Object>> queryListBorrowCallOut(RequestWrapper request, Integer pageSize, Integer currPage) {
		String partCode = request.getParamValue("partCode");
		String partName =request.getParamValue("partName");
		String dealerCode = request.getParamValue("dealerCode");
		String dealerName =request.getParamValue("dealerName");
		String supplyCode = request.getParamValue("supplyCode");
		String supplyName =request.getParamValue("supplyName");
		String VIN = request.getParamValue("VIN");
		String claimNo =request.getParamValue("claimNo");
		String start_date=request.getParamValue("start_date");
		String end_date=request.getParamValue("end_date");
		
		
		
		StringBuffer sb= new StringBuffer();
		sb.append("select t.* from Tt_As_Part_Borrow_Store t where t.status=0 ");
		if(partCode!=null){
			sb.append(" and t.part_code like '%"+partCode+"%' ");
		}
		if(partName!=null){
			sb.append(" and t.part_name like '%"+partName+"%' ");
		}
		if(dealerCode!=null){
			sb.append(" and t.dealer_code like '%"+dealerCode+"%' ");
		}
		if(dealerName!=null){
			sb.append(" and t.dealer_name like '%"+dealerName+"%' ");
		}
		if(supplyCode!=null){
			sb.append(" and t.supply_code like '%"+supplyCode+"%' ");
		}
		if(supplyName!=null){
			sb.append(" and t.supply_name like '%"+supplyName+"%' ");
		}
		if(VIN!=null){
			sb.append(" and t.vin like '%"+VIN+"%' ");
		}
		if(claimNo!=null){
			sb.append(" and t.claim_no like '%"+claimNo+"%' ");
		}
		if(start_date!=null){
			sb.append(" and t.create_date>= to_date('"+start_date+"','yyyy-MM-dd')");
		}
		if(end_date!=null){
			sb.append(" and to_date(to_char(t.create_date,'yyyy-MM-dd'),'yyyy-MM-dd')<= to_date('"+end_date+"','yyyy-MM-dd')");
		}
			
		
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	/***
	 * 根据索赔单id去修改 索赔单urgent 2  再次添加到临时表
	 * @param request
	 * @param loginUser 
	 * @return
	 */
	public int callOutById(RequestWrapper request, AclUserBean loginUser) {
		int res=0;
		try {
			String borrowMan = request.getParamValue("bm");
			ClaimOldPartStorageManagerDao dao=new ClaimOldPartStorageManagerDao();
			
			String ids = DaoFactory.getParam(request, "ids");
			String[] split = ids.split(",");
			
			
			for (String id : split) {
				StringBuffer sb = queryDate(request);
				DaoFactory.getsql(sb, "a.id", id, 1);
				PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), 3, 1);
				this.insetPoTemp(list, id, loginUser);
			}
			
			for (String id : split) {
				TtAsWrOldReturnedDetailPO detail =new TtAsWrOldReturnedDetailPO();
				detail.setClaimId(Long.parseLong(id));
				List<TtAsWrOldReturnedDetailPO> details=dao.select(detail);
				if(details.size()>0){
					detail=details.get(0);
					String claimNo=detail.getClaimNo();
					TtAsPartBorrowStorePO store=new TtAsPartBorrowStorePO();
					store.setClaimNo(claimNo);
					List<TtAsPartBorrowStorePO> stores=dao.select(store);
					if(stores.size()>0){
						TtAsPartBorrowStorePO store1=stores.get(0);
						store1.setBorrowMan(borrowMan);
						dao.update(store, store1);
					}
					
				}
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}
		return res;
	}
	/**
	 * 入临时表
	 * @param list
	 */
	private void insetPoTemp(PageResult<Map<String, Object>> list,String id,AclUserBean loginUser) {
		Map<String, Object> map = list.getRecords().get(0);
		String  ID =BaseUtils.checkNull(map.get("ID"));
		String  ALL_AMOUNT =BaseUtils.checkNull(map.get("ALL_AMOUNT"));
		String  CLAIM_NO =BaseUtils.checkNull(map.get("CLAIM_NO"));
		String  DEALER_CODE =BaseUtils.checkNull(map.get("DEALER_CODE"));
		String  CLAIM_TYPE =BaseUtils.checkNull(map.get("CLAIM_TYPE"));
		String  DEALER_NAME =BaseUtils.checkNull(map.get("DEALER_NAME"));
		String  MODEL_NAME =BaseUtils.checkNull(map.get("MODEL_NAME"));
		String  VIN =BaseUtils.checkNull(map.get("VIN"));
		String  SUPPLY_NAME =BaseUtils.checkNull(map.get("SUPPLY_NAME"));
		String  SUPPLY_CODE =BaseUtils.checkNull(map.get("SUPPLY_CODE"));
		String  PART_CODE =BaseUtils.checkNull(map.get("PART_CODE"));
		String  PART_NAME =BaseUtils.checkNull(map.get("PART_NAME"));
		ClaimOldPartStorageManagerDao dao = new ClaimOldPartStorageManagerDao();
		TtAsPartBorrowStorePO t =new TtAsPartBorrowStorePO();
		t.setPkid(DaoFactory.getPkId());
		t.setId(BaseUtils.ConvertLong(ID));
		t.setAllAmount(BaseUtils.ConvertInt(ALL_AMOUNT));
		t.setClaimNo(CLAIM_NO);
		t.setDealerCode(DEALER_CODE);
		t.setDealerName(DEALER_NAME);
		t.setClaimType(BaseUtils.ConvertInt(CLAIM_TYPE));
		t.setModelName(MODEL_NAME);
		t.setPartName(PART_NAME);
		t.setPartCode(PART_CODE);
		t.setStatus(0);//设置状态
		t.setSupplyCode(SUPPLY_CODE);
		t.setSupplyName(SUPPLY_NAME);
		t.setVin(VIN);
		t.setCreateDate(new Date());
		t.setCreatePerson(loginUser.getName());
		dao.insert(t);
		dao.update("update TT_AS_WR_APPLICATION a set a.urgent=2 where a.id="+id, null);
	}
	public PageResult<Map<String, Object>> queryListBorrowCallOutTempData(RequestWrapper request, Integer pageSize, Integer currPage) {
		StringBuffer sb = queryDate(request);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	private StringBuffer queryDate(RequestWrapper request) {
		String partCode = request.getParamValue("partCode");
		String partName =request.getParamValue("partName");
		String dealerCode = request.getParamValue("dealerCode");
		String dealerName =request.getParamValue("dealerName");
		String supplyCode = request.getParamValue("supplyCode");
		String supplyName =request.getParamValue("supplyName");
		String VIN = request.getParamValue("VIN");
		String claimNo =request.getParamValue("claimNo");
		
		StringBuffer sb= new StringBuffer();
		sb.append("select a.* from (select sum(d.sign_amount) all_amount,\n" );
		sb.append("       d.claim_no,\n" );
		sb.append("       a.id,\n" );
		sb.append("       dd.dealer_code,\n" );
		sb.append("       a.claim_type,\n" );
		sb.append("       dd.dealer_shortname dealer_name,\n" );
		sb.append("       A.MODEL_cODE MODEL_NAME,\n" );
		sb.append("       a.vin,\n" );
		sb.append("       D.PRODUCER_NAME SUPPLY_NAME,\n" );
		sb.append("       D.PRODUCER_CODE SUPPLY_CODE,\n" );
		sb.append("       D.PART_CODE,\n" );
		sb.append("       D.PART_NAME\n" );
		sb.append("  from tt_as_wr_old_returned_detail d,\n" );
		sb.append("       tt_as_wr_old_returned        r,\n" );
		sb.append("       tm_dealer                    dd,\n" );
		sb.append("       TT_AS_WR_APPLICATION         A\n" );
		sb.append(" where 1 = 1\n" );
		sb.append("   and A.CLAIM_NO = D.CLAIM_NO\n" );
		sb.append("   and dd.dealer_id = r.dealer_id\n" );
		sb.append("   and r.id = d.return_id\n" );
		sb.append("   and a.urgent = 1\n" );
		sb.append("   AND d.is_main_code = 94001001\n" );
		sb.append("   and (r.status = 10811005 or\n" );
		sb.append("       (r.status = 10811004 and d.is_in_house = 10041001))\n" );
		sb.append("   and d.sign_amount > 0\n" );
		sb.append("   and (d.is_cliam = 0 or d.is_cliam is null)\n" );
		sb.append("   and d.is_out = 0\n" );
		sb.append("      --and d.part_code = '36120100-C04-B00'\n" );
		sb.append("      --and d.producer_code = '101005'\n" );
		sb.append("   and r.yieldly = 95411001\n" );
		sb.append(" group by d.claim_no,\n" );
		sb.append("          dd.dealer_code,\n" );
		sb.append("          dd.dealer_shortname,\n" );
		sb.append("          a.id,\n" );
		sb.append("          A.MODEL_cODE,\n" );
		sb.append("          a.claim_type,\n" );
		sb.append("          a.vin,\n" );
		sb.append("          D.PRODUCER_NAME,\n" );
		sb.append("          D.PRODUCER_CODE,\n" );
		sb.append("          D.PART_CODE,\n" );
		sb.append("          D.PART_NAME) a where 1=1 ");
		
		
		
		if(partCode!=null){
			sb.append(" and part_code like '%"+partCode+"%' ");
		}
		if(partName!=null){
			sb.append(" and part_name like '%"+partName+"%' ");
		}
		if(dealerCode!=null){
			sb.append(" and dealer_code like '%"+dealerCode+"%' ");
		}
		if(dealerName!=null){
			sb.append(" and dealer_name like '%"+dealerName+"%' ");
		}
		if(supplyCode!=null){
			sb.append(" and supply_code like '%"+supplyCode+"%' ");
		}
		if(supplyName!=null){
			sb.append(" and supply_name like '%"+supplyName+"%' ");
		}
		if(VIN!=null){
			sb.append(" and vin like '%"+VIN+"%' ");
		}
		if(claimNo!=null){
			sb.append(" and claim_no like '%"+claimNo+"%' ");
		}
		
		return sb;
	}
	/**
	 * 调入修改标识 为0 改紧急w为正常
	 * @param request
	 * @param loginUser
	 * @return
	 */
	public int callInById(RequestWrapper request, AclUserBean loginUser) {
		int res=0;
		try {
			String borrowMan = request.getParamValue("bm");
			String ids = DaoFactory.getParam(request, "ids");
			String[] split = ids.split(",");
			ClaimOldPartStorageManagerDao dao = new ClaimOldPartStorageManagerDao();
			
			for (String id : split) {
				TtAsWrOldReturnedDetailPO detail =new TtAsWrOldReturnedDetailPO();
				detail.setClaimId(Long.parseLong(id));
				List<TtAsWrOldReturnedDetailPO> details=dao.select(detail);
				if(details.size()>0){
					detail=details.get(0);
					String claimNo=detail.getClaimNo();
					TtAsPartBorrowStorePO store=new TtAsPartBorrowStorePO();
					store.setClaimNo(claimNo);
					List<TtAsPartBorrowStorePO> stores=dao.select(store);
					if(stores.size()>0){
						TtAsPartBorrowStorePO store1=stores.get(0);
						store1.setReturnMan(borrowMan);
						dao.update(store, store1);
					}
					
				}
			}
			
			
			
			for (String id : split) {
				TtAsPartBorrowStorePO t =new TtAsPartBorrowStorePO();
				t.setId(BaseUtils.ConvertLong(id));
				TtAsPartBorrowStorePO t1 =new TtAsPartBorrowStorePO();
				t1.setNextDate(new Date());
				t1.setNextPerson(loginUser.getName());
				t1.setStatus(2);
				dao.update(t, t1);
				//--正常库存 设置还原标识
				//dao.update("update tt_as_wr_application a set a.urgent=0 where a.id="+id, null);
				/*StringBuffer sb1= new StringBuffer();
				sb1.append("update TT_AS_WR_OLD_RETURNED t set  t.return_type=10731002\n" );
				sb1.append("where t.id=(select d.return_id from  TT_AS_WR_OLD_RETURNED_DETAIL d\n" );
				sb1.append("where d.claim_id ='"+id+"')");
				dao.update(sb1.toString(), null);*/
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}
		return res;
	}
	public List<Map<String, Object>> getCurStoreList2(Map params){
		String supply_code = ClaimTools.dealParamStr(params.get("supply_code"));//查询条件--供应商代码
		String supply_name=ClaimTools.dealParamStr(params.get("supply_name"));//查询条件--供应商简称
		String part_code=ClaimTools.dealParamStr(params.get("part_code"));//查询条件--配件代码
		String part_name=ClaimTools.dealParamStr(params.get("part_name"));//查询条件--配件名称
		String yieldly =  ClaimTools.dealParamStr(params.get("yieldly"));//用户拥有的产地权限
		String isMainCode =  ClaimTools.dealParamStr(params.get("isMainCode"));
		StringBuffer sql= new StringBuffer();
		sql.append("select * from (\n" );
		
		sql.append("select\n" );
		sql.append("f.down_product_code supply_code ,f.down_product_name supply_name,f.down_part_code part_code,f.down_part_name part_name ,\n" );
		sql.append("\n" );
		sql.append(" sum(  case when d.qhj_flag != 1 then 1 when  d.qhj_flag = 1 and d.kcdb_flag = 2 then 1 else 0 end ) all_in_amount,  max(r.yieldly) yieldly ,\n" );

		sql.append("\n" );
		
		sql.append(" sum( case when d.is_out =0  and d.is_cliam =0\n" );
		sql.append("    and d.qhj_flag != 1\n" );
		sql.append("  then 1\n" );
		sql.append("    when d.is_out =0  and d.is_cliam =0\n" );
		sql.append("      and d.qhj_flag = 1 and d.kcdb_flag = 2\n" );
	    sql.append("     then 1\n" );
	   sql.append("       else 0\n" );
       sql.append("  end \n" );
	   sql.append("    ) all_amount ,\n" );
		sql.append("\n" );
		sql.append(" sum( case when d.is_out =1   then 1\n" );
		sql.append("  else 0 end ) all_out_amount,\n" );
		sql.append("  sum(f.balance_amount) part_Price \n" );
		sql.append("\n" );
		sql.append("from  tt_as_wr_old_returned_detail d  left join tt_as_wr_partsitem f\n" );
		sql.append("on  d.claim_id = f.id and d.part_code = f.down_part_code\n" );
		sql.append(" ,tt_as_wr_old_returned r , tm_dealer td    where\n" );
		sql.append(" 1=1  and d.sign_amount = 1 \n" );
		DaoFactory.getsql(sql, "d.producer_code", supply_code, 2);
		DaoFactory.getsql(sql, "d.producer_name", supply_name, 2);
		DaoFactory.getsql(sql, "d.is_Main_Code", isMainCode, 1);
		DaoFactory.getsql(sql, "r.yieldly", yieldly, 1);
		DaoFactory.getsql(sql, "d.part_code", part_code.toUpperCase(), 2);
		DaoFactory.getsql(sql, "d.part_name", part_name.toUpperCase(), 2);
		sql.append("  and td.dealer_id  = r.dealer_id\n" );
		sql.append(" and d.return_id = r.id and (r.status=10811005 or (r.status=10811004 and d.is_in_house=10041001))\n" );
		sql.append("   and  r.yieldly = 95411001  group by\n" );
		sql.append(" f.down_product_code,f.down_product_name,f.down_part_code,f.down_part_name ) M where m.all_amount > 0 ");
		
		List<Map<String, Object>> pr= pageQuery(sql.toString(), null, getFunName());
		return pr;
}
	public void exportClaimDetail3(ActionContext act,
			List<ClaimOldPartOutPreListBean> list) {
		try {
			ApplicationDao appdao= ApplicationDao.getInstance();
			String[] head=new String[11];
			head[0]="配件名称";
			head[1]="索赔单号";
			head[2]="经销商代码";
			head[3]="经销商名称";
			head[4]="供应商代码";
			head[5]="供应商名称";
			head[6]="VIN";
			head[7]="车型";
			head[8]="库存";
			head[9]="回运类型";
			head[10]="索赔类型";
			List params=new ArrayList();
			for (ClaimOldPartOutPreListBean claim : list) {
				String[] detail=new String[11];
				detail[0]=BaseUtils.checkNull(claim.getPart_name());
				detail[1]=BaseUtils.checkNull(claim.getClaim_no());
				detail[2]=BaseUtils.checkNull(claim.getDealer_code());
				detail[3]=BaseUtils.checkNull(claim.getDealer_name());
				detail[4]=BaseUtils.checkNull(claim.getSupply_code());
				detail[5]=BaseUtils.checkNull(claim.getSupply_name());
				detail[6]=BaseUtils.checkNull(claim.getVin());
				detail[7]=BaseUtils.checkNull(claim.getModel_name());
				detail[8]=BaseUtils.checkNull(claim.getAll_amount());
				String returnType=claim.getReturnType().toString();
				returnType= appdao.getTypeDesc(claim.getReturnType().toString());
				detail[9]=BaseUtils.checkNull(returnType);
				String claimType= appdao.getTypeDesc(claim.getClaimType().toString());
				detail[10]=BaseUtils.checkNull(claimType);
				params.add(detail);
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出旧件库存明细报表"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
