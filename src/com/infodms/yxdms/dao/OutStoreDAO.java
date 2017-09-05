package com.infodms.yxdms.dao;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.dao.claim.oldPart.ClaimOldPartOutStorageDao;
import com.infodms.dms.po.TtAsWrOldOutPartPO;
import com.infodms.dms.po.TtAsWrRangeSinglePO;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("rawtypes")
public class OutStoreDAO extends IBaseDao{

	private static OutStoreDAO dao = new OutStoreDAO();
	public static final OutStoreDAO getInstance(){
		dao = (dao==null)?new OutStoreDAO():dao;
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> showOutStoreNum(RequestWrapper request) {
		String supply_name=DaoFactory.getParam(request,"supply_name");
		String supply_code=DaoFactory.getParam(request,"supply_code");
		String dealer_code=DaoFactory.getParam(request,"dealer_code");
		String dealer_name=DaoFactory.getParam(request,"dealer_name");
		String part_code=DaoFactory.getParam(request,"part_code");
		String part_name=DaoFactory.getParam(request,"part_name");
		String claim_no=DaoFactory.getParam(request,"claim_no");
		String in_start_date = DaoFactory.getParam(request, "in_start_date");
		String in_end_date=DaoFactory.getParam(request, "in_end_date");
		
		ClaimOldPartOutStorageDao outStorageDao = new ClaimOldPartOutStorageDao();
		String sql = outStorageDao.findInfoByPartCodeAndSupplyCode(supply_name, supply_code,
				dealer_name, dealer_code, part_code, part_name, claim_no,
				in_start_date, in_end_date,null);
		StringBuffer sb= new StringBuffer();
		String aliasName = "oldsql";
		sb.append(" select sum(").append(aliasName).append(".retrun_AMOUNT) retrun_AMOUNT ");
		sb.append(" ,sum(").append(aliasName).append(".no_retrun_amount) NO_RETURN_AMOUNT ");
		sb.append(" from (").append(sql).append(" ) oldsql ");
		/*sb.append("select sum(d.retrun_amount) as retrun_amount, sum(d.no_return_amount) as no_return_amount  from (select c.part_code,\n" );
		sb.append("       (select b.part_name\n" );
		sb.append("          from TM_PT_PART_BASE b\n" );
		sb.append("         where b.part_code = c.part_code) as PART_NAME,\n" );
		sb.append("       c.SUPPLY_CODE,\n" );
		sb.append("       (select min(m.MAKER_SHOTNAME)\n" );
		sb.append("          from tt_part_maker_define m, tt_part_maker_relation r\n" );
		sb.append("         where r.maker_id = m.maker_id\n" );
		sb.append("           and m.maker_code = c.SUPPLY_CODE) as SUPPLY_name,\n" );
		sb.append("       sum(c.retrun_amount) retrun_amount,\n" );
		sb.append("       sum(c.no_retrun_amount) no_return_amount\n" );
		sb.append("  from (\n" );
		//库存（有件）
		sb.append("              SELECT sum(D.SIGN_AMOUNT) retrun_AMOUNT, 0 no_retrun_amount,\n" );
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
		//==================zyw 加标示切换件和库存调拨
		sb.append("   AND (D.qhj_flag = 0 or (D.qhj_flag = 1 and d.KCDB_FLAG=2))\n");
		DaoFactory.getsql(sb, "d.in_date", in_start_date, 3);
		DaoFactory.getsql(sb, "d.in_date", in_end_date, 4);
		sb.append("           and r.yieldly = 95411001\n" );
		sb.append("           and (r.status = 10811005 or\n" );
		sb.append("               (r.status = 10811004 and d.is_in_house = 10041001))\n" );
		sb.append("           AND D.SIGN_AMOUNT > 0\n" );
		sb.append("           AND (D.IS_CLIAM = 0 OR D.IS_CLIAM IS NULL or D.IS_CLIAM = 2)\n" );
		DaoFactory.getsql(sb, "d.producer_name", supply_name, 2);
		DaoFactory.getsql(sb, "d.producer_code", supply_code, 2);
		DaoFactory.getsql(sb, "dd.dealer_name", dealer_name, 2);
		DaoFactory.getsql(sb, "dd.dealer_code", dealer_code, 2);
		DaoFactory.getsql(sb, "d.part_code", part_code, 2);
		DaoFactory.getsql(sb, "d.part_name", part_name, 2);
		DaoFactory.getsql(sb, "d.claim_no", claim_no, 2);
		sb.append("           AND D.IS_OUT in (0,2)\n" );
		sb.append("         GROUP BY D.CLAIM_NO,\n" );
		sb.append("                  d.PART_CODE,\n" );
		sb.append("                  d.PART_NAME,\n" );
		sb.append("                  d.part_id,\n" );
		sb.append("                  d.producer_code,\n" );
		sb.append("                  d.producer_name\n" );
		sb.append("        UNION \n" );
		//库存（无件）
		sb.append("              SELECT  0 retrun_AMOUNT,sum(D.SIGN_AMOUNT) no_retrun_amount,\n" );
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
		//==================zyw 加标示切换件和库存调拨
		sb.append("   AND (d.qhj_flag=1 and d.kcdb_flag<>2) \n");
		DaoFactory.getsql(sb, "d.in_date", in_start_date, 3);
		DaoFactory.getsql(sb, "d.in_date", in_end_date, 4);
		sb.append("           and r.yieldly = 95411001\n" );
		sb.append("           and (r.status = 10811005 or\n" );
		sb.append("               (r.status = 10811004 and d.is_in_house = 10041001))\n" );
		sb.append("           AND D.SIGN_AMOUNT > 0\n" );
		sb.append("           AND (D.IS_CLIAM = 0 OR D.IS_CLIAM IS NULL)\n" );
		DaoFactory.getsql(sb, "d.producer_name", supply_name, 2);
		DaoFactory.getsql(sb, "d.producer_code", supply_code, 2);
		DaoFactory.getsql(sb, "dd.dealer_name", dealer_name, 2);
		DaoFactory.getsql(sb, "dd.dealer_code", dealer_code, 2);
		DaoFactory.getsql(sb, "d.part_code", part_code, 2);
		DaoFactory.getsql(sb, "d.part_name", part_name, 2);
		DaoFactory.getsql(sb, "d.claim_no", claim_no, 2);
		sb.append("           AND D.IS_OUT = 0\n" );
		sb.append("         GROUP BY D.CLAIM_NO,\n" );
		sb.append("                  d.PART_CODE,\n" );
		sb.append("                  d.PART_NAME,\n" );
		sb.append("                  d.part_id,\n" );
		sb.append("                  d.producer_code,\n" );
		sb.append("                  d.producer_name\n" );
		
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
		DaoFactory.getsql(sb, "P.DOWN_PRODUCT_NAME", supply_name, 2);
		DaoFactory.getsql(sb, "P.DOWN_PRODUCT_CODE", supply_code, 2);
		DaoFactory.getsql(sb, "d.dealer_name", dealer_name, 2);
		DaoFactory.getsql(sb, "d.dealer_code", dealer_code, 2);
		DaoFactory.getsql(sb, "p.DOWN_PART_CODE", part_code, 2);
		DaoFactory.getsql(sb, "p.DOWN_PART_NAME", part_name, 2);
		DaoFactory.getsql(sb, "a.claim_no", claim_no, 2);		
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
		DaoFactory.getsql(sb, "P.DOWN_PRODUCT_NAME", supply_name, 2);
		DaoFactory.getsql(sb, "P.DOWN_PRODUCT_CODE", supply_code, 2);
		DaoFactory.getsql(sb, "d.dealer_name", dealer_name, 2);
		DaoFactory.getsql(sb, "d.dealer_code", dealer_code, 2);
		DaoFactory.getsql(sb, "p.DOWN_PART_CODE", part_code, 2);
		DaoFactory.getsql(sb, "p.DOWN_PART_NAME", part_name, 2);
		DaoFactory.getsql(sb, "a.claim_no", claim_no, 2);		
		sb.append("           AND P.RESPONSIBILITY_TYPE = 94001001\n" );
		sb.append("         GROUP BY A.CLAIM_NO,\n" );
		sb.append("                  P.DOWN_PART_CODE,\n" );
		sb.append("                  P.DOWN_PART_NAME,\n" );
		sb.append("                  P.PART_ID,\n" );
		sb.append("                  P.DOWN_PRODUCT_CODE,\n" );
		sb.append("                  P.DOWN_PRODUCT_NAME\n" );
		sb.append("        having sum(p.quantity) > 0) c\n" );
		sb.append(" group by c.part_code, c.SUPPLY_CODE\n" );
		sb.append(" order by c.part_code) d");*/
		List<Map<String, Object>> list = this.pageQuery(sb.toString(), null, getFunName());
		return list; 
	}
	/**
	 * 入库序号R+4位年月+6位顺序号：R1410000001【顺序号按月重新计算】，“旧件入库明细”按入库序号倒序排列
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getNewSignNo() {
		List<Map<String,Object>> ps=this.pageQuery("select ('R'||to_char(sysdate,'yymm')||LPAD(seq_c.NEXTVAL, 6, '0')) as seq from dual", null, this.getFunName());
		return ps.get(0).get("SEQ").toString();
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> findDataOldAudit(RequestWrapper request, Integer pageSizeMax, Integer currPage) {
		String claim_id=request.getParamValue("CLAIM_ID");//此处为物流单ID
		StringBuffer sb= new StringBuffer();
		if(!"".equals(claim_id)){
			sb.append(" select   taword.SUPPLIER_REMARK,taword.Executive_director_ram, case when taword.Executive_director_sta=0  and taword.RETURN_AMOUNT != taword.SIGN_AMOUNT then '主管未审' when taword.Executive_director_sta=1 then '主管审核同意扣件' when taword.Executive_director_sta=2 then '主管审核驳回扣件' end Executive_director_sta  , taword.id,taword.is_scan,TAWORD.Is_Main_Code,taword.is_in_house,taword.is_out,taword.claim_no,taword.vin,c.id as claim_id,taword.PRODUCER_NAME,taword.producer_code,to_char(c.RO_STARTDATE,'yyyy-mm-dd') as RO_STARTDATE,c.REMARK,del.dealer_code,c.claim_type,\n" );
			sb.append("tawp.part_code,tawp.part_name,nvl(taword.return_amount,0) return_amount,\n" );
			sb.append("nvl(taword.sign_amount,0) sign_amount,c.Is_Invoice,taword.barcode_no,decode( TAWORD.LOCAL_WAR_HOUSE,NULL,b.local_war_house,TAWORD.LOCAL_WAR_HOUSE) LOCAL_WAR_HOUSE,decode( TAWORD.LOCAL_WAR_SHEL,NULL,b.LOCAL_WAR_SHEL,TAWORD.LOCAL_WAR_SHEL) LOCAL_WAR_SHEL,decode( TAWORD.LOCAL_WAR_LAYER,NULL,b.LOCAL_WAR_LAYER,TAWORD.LOCAL_WAR_LAYER) LOCAL_WAR_LAYER,\n" );
			sb.append("taword.box_no,taword.warehouse_region,taword.deduct_remark,taword.other_remark,decode(taword.deduct_remark,0,'--请选择--',null,'--请选择--',tc.code_desc) deduct_desc,TAWORD.Is_Import\n" );
			sb.append("from tt_as_wr_old_returned tawor , tm_pt_part_base b ,\n" );
			sb.append("tt_as_wr_old_returned_detail taword,\n" );
			sb.append("tt_as_wr_partsitem tawp,tc_code tc,TT_AS_WR_APPLICATION c,tm_dealer del\n" );
			sb.append("where tawor.id=taword.return_id  AND taword.part_code = b.part_code(+) AND c.ID = tawp.ID AND c.claim_no = taword.claim_no and taword.part_id=tawp.part_id and taword.deduct_remark=tc.code_id(+)\n" );
			sb.append("and taword.is_sign=0 AND  tawor.id="+claim_id+" and c.id = taword.claim_id and del.dealer_id=c.dealer_id\n" );
			DaoFactory.getsql(sb, "taword.vin", DaoFactory.getParam(request, "vin"), 2);
			DaoFactory.getsql(sb, "taword.is_in_house", DaoFactory.getParam(request, "IS_IN_HOUSE"), 1);
			DaoFactory.getsql(sb, "taword.box_no", DaoFactory.getParam(request, "boxNo"), 1);
			DaoFactory.getsql(sb, "taword.part_code", DaoFactory.getParam(request, "part_code"), 2);
			DaoFactory.getsql(sb, "taword.part_name", DaoFactory.getParam(request, "part_name"), 2);
			DaoFactory.getsql(sb, "taword.barcode_no", DaoFactory.getParam(request, "bar_code"), 2);
			DaoFactory.getsql(sb, "taword.sign_amount", DaoFactory.getParam(request, "sing_num"), 1);
			DaoFactory.getsql(sb, "taword.is_import", DaoFactory.getParam(request, "is_import"), 1);
			DaoFactory.getsql(sb, "c.ww_dealer_code", DaoFactory.getParam(request, "dealerCode"), 2);
			DaoFactory.getsql(sb, "c.ww_dealer_name", DaoFactory.getParam(request, "dealerName"), 2);
			DaoFactory.getsql(sb, "c.claim_no", DaoFactory.getParam(request, "claim_no"), 2);
			DaoFactory.getsql(sb, "taword.producer_code", DaoFactory.getParam(request, "producer_code"), 2);
			DaoFactory.getsql(sb, "taword.producer_name", DaoFactory.getParam(request, "producer_name"), 2);
			sb.append("order by  box_no,claim_no,part_name,barcode_no\n");
		}
		return this.pageQuery(sb.toString(),null,this.getFunName(), pageSizeMax, currPage); 
	}

	@SuppressWarnings("unchecked")
	public int diyOutPartSure(RequestWrapper request, AclUserBean loginUser) {
		int res=1;
		String supplierCode = DaoFactory.getParam(request, "supplierCode");
		String supplierName = DaoFactory.getParam(request, "supplierName");
		String[] part_code = DaoFactory.getParams(request, "part_code");
		String[] part_name = DaoFactory.getParams(request, "part_name");
		String[] unit = DaoFactory.getParams(request, "unit");
		String[] fill_num = DaoFactory.getParams(request, "fill_num");
		String[] labour_amount = DaoFactory.getParams(request, "labour_amount");
		String[] relation_amount = DaoFactory.getParams(request, "relation_amount");
		String[] out_amount = DaoFactory.getParams(request, "out_amount");
		String[] part_amount = DaoFactory.getParams(request, "part_amount");
		String[] amount = DaoFactory.getParams(request, "amount");
		String[] print_mun = DaoFactory.getParams(request, "print_mun");
		String[] remark = DaoFactory.getParams(request, "remark");
		// 保存出库明细
		String no = GenerateStockNo();
		TtAsWrOldOutPartPO pp = new TtAsWrOldOutPartPO();
		Long userId = loginUser.getUserId();
		pp.setCreateBy(userId);
		pp.setCreateDate(new Date());
		pp.setId(DaoFactory.getPkId());
		pp.setOutBy(userId);
		pp.setOutDate(new Date());
		pp.setOutNo(no);
		//pp.setClaimNo(null);
		//pp.setOutPartCode();
		//pp.setOutPartName();
		pp.setOutType(95331001);
		pp.setRemark(null);
		pp.setOutAmout(1);
		pp.setSupplyCode(supplierCode);
		pp.setSupplyName(supplierName);
		pp.setYieldly(95411001);
		pp.setOutPartType(1);
		pp.setRelationalOutNo(null);
		pp.setRangeNo(no);
		pp.setOutFlag(0);
		pp.setDiyFlag(1);
		this.insert(pp);
		if(part_code!=null && part_code.length>0){
			for (int i = 0; i < part_code.length; i++) {
				TtAsWrRangeSinglePO t=new TtAsWrRangeSinglePO();
				t.setCreateBy(userId);
				t.setCreateDate(new Date());
				t.setId(DaoFactory.getPkId());
				t.setLabourAmount(Double.valueOf(labour_amount[i]));
				t.setPartAmount(Double.valueOf(part_amount[i]));
				t.setPartCode(part_code[i]);
				t.setPartName(part_name[i]);
				t.setPartUnit(unit[i]);
				t.setPrintNum(Integer.valueOf(print_mun[i]));
				t.setRemark(remark[i]);
				t.setOutNo(no);
				t.setRangeNo(no);
				t.setPartQuantity(Integer.valueOf(fill_num[i]));
				t.setRelatedLosses(Double.valueOf(relation_amount[i]));
				t.setSmallAmount(Double.valueOf(amount[i]));
				t.setOutAmount(Double.valueOf(out_amount[i]));
				t.setSupplyCode(supplierCode);
				t.setSupplyName(supplierName);
				t.setOutType(0);
				t.setPrintPart(Double.valueOf(part_amount[i])*Integer.valueOf(fill_num[i]));
				this.insert(t);
			}
		}
		return res;
	}
	//生成出库单号
	@SuppressWarnings("unchecked")
	public String GenerateStockNo(){
		String sql="select TO_CHAR(SYSDATE, 'YYYYMM')||LPAD(seq_c.NEXTVAL,3,'0')  as num  From DUAL";
		List<Map<String,Object>> ps=this.pageQuery(sql.toString(), null, this.getFunName());
		return ps.get(0).get("NUM").toString();
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> linkByPartCode(String partCode) {
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
		sb.append("       sum(c.no_retrun_amount) no_return_amount\n" );
		sb.append("  from (SELECT sum(D.SIGN_AMOUNT) retrun_AMOUNT,\n" );
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
		sb.append("           AND (D.qhj_flag = 0 or (D.qhj_flag = 1 and d.KCDB_FLAG = 2))\n" );
		sb.append("           and r.yieldly = 95411001\n" );
		sb.append("           and (r.status = 10811005 or\n" );
		sb.append("               (r.status = 10811004 and d.is_in_house = 10041001))\n" );
		sb.append("           AND D.SIGN_AMOUNT > 0\n" );
		sb.append("           AND (D.IS_CLIAM = 0 OR D.IS_CLIAM IS NULL or D.IS_CLIAM = 2)\n" );
		DaoFactory.getsql(sb, "d.part_code", partCode, 1);
		sb.append("           AND D.IS_OUT in (0, 2)\n" );
		sb.append("         GROUP BY D.CLAIM_NO,\n" );
		sb.append("                  d.PART_CODE,\n" );
		sb.append("                  d.PART_NAME,\n" );
		sb.append("                  d.part_id,\n" );
		sb.append("                  d.producer_code,\n" );
		sb.append("                  d.producer_name\n" );
		sb.append("        UNION\n" );
		sb.append("        SELECT 0 retrun_AMOUNT,\n" );
		sb.append("               sum(D.SIGN_AMOUNT) no_retrun_amount,\n" );
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
		sb.append("           AND (d.qhj_flag = 1 and d.kcdb_flag <> 2)\n" );
		sb.append("           and r.yieldly = 95411001\n" );
		sb.append("           and (r.status = 10811005 or\n" );
		sb.append("               (r.status = 10811004 and d.is_in_house = 10041001))\n" );
		sb.append("           AND D.SIGN_AMOUNT > 0\n" );
		sb.append("           AND (D.IS_CLIAM = 0 OR D.IS_CLIAM IS NULL)\n" );
		DaoFactory.getsql(sb, "d.part_code", partCode, 1);
		sb.append("           AND D.IS_OUT = 0\n" );
		sb.append("         GROUP BY D.CLAIM_NO,\n" );
		sb.append("                  d.PART_CODE,\n" );
		sb.append("                  d.PART_NAME,\n" );
		sb.append("                  d.part_id,\n" );
		sb.append("                  d.producer_code,\n" );
		sb.append("                  d.producer_name\n" );
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
		DaoFactory.getsql(sb, "p.DOWN_PART_CODE", partCode, 1);
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
		DaoFactory.getsql(sb, "p.DOWN_PART_CODE", partCode, 1);
		sb.append("           AND P.RESPONSIBILITY_TYPE = 94001001\n" );
		sb.append("         GROUP BY A.CLAIM_NO,\n" );
		sb.append("                  P.DOWN_PART_CODE,\n" );
		sb.append("                  P.DOWN_PART_NAME,\n" );
		sb.append("                  P.PART_ID,\n" );
		sb.append("                  P.DOWN_PRODUCT_CODE,\n" );
		sb.append("                  P.DOWN_PRODUCT_NAME\n" );
		sb.append("        having sum(p.quantity) > 0) c\n" );
		sb.append(" group by c.part_code, c.SUPPLY_CODE\n" );
		sb.append(" order by c.part_code");
		List<Map<String, Object>> list = this.pageQuery(sb.toString(), null, getFunName());
		return list;
	}
}
