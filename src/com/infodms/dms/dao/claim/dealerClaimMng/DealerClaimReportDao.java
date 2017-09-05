/**********************************************************************
* <pre>
* FILE : DealerClaimReportDao.java
* CLASS : DealerClaimReportDao
* AUTHOR : XZM
* FUNCTION : 索赔申请上报数据库操作部分：
*            [索赔申请单查询]
*            [索赔申请单上报]
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-03| XZM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: DealerClaimReportDao.java,v 1.26 2013/03/01 01:35:25 yx Exp $
 */
package com.infodms.dms.dao.claim.dealerClaimMng;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmPtDealerPartPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsWrAppAuditDetailPO;
import com.infodms.dms.po.TtAsWrAppauthitemPO;
import com.infodms.dms.po.TtAsWrApplicationExtPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrPartsitemBarcodePO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.dao.ClaimDAO;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 索赔申请单上报-DAO
 * @author XZM
 */
@SuppressWarnings("rawtypes")
public class DealerClaimReportDao extends BaseDao{

	private static final DealerClaimReportDao dao = new DealerClaimReportDao();
	public static final DealerClaimReportDao getInstance(){
		if (dao == null) {
			return new DealerClaimReportDao();
		}
		return dao;
	}
    /** 索赔申请单表别名 */
	public String TT_AS_WR_APPLICATION = "A";
	/** 经销商表别名 */
	public String TM_DEALER = "B";
	
	/*
	 * (non-Javadoc)
	 * @see com.infodms.dms.dao.common.BaseDao#wrapperPO(java.sql.ResultSet, int)
	 */
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 * 上报索赔申请单
	 * @param conditionPO 更新条件
     * @return int 更新记录数
	 */
    public int reportClaim(){
    	
    	String sql = "UPDATE TT_AS_WR_APPLICATION"+
						" SET STATUS = ?,"+
						" UPDATE_BY = ?,"+
						" UPDATE_DATE = ?,"+
						" REPORTER = ?,"+
						" REPORT_DATE = ?,"+
						" SUBMIT_TIMES = NVL(SUBMIT_TIMES,0)+1"+
						" WHERE ID = ?";
    	List<Object> paramList = new ArrayList<Object>();
    	/*TtAsWrApplicationPO conditionPO
    	paramList.add(conditionPO.getStatus());
    	paramList.add(conditionPO.getUpdateBy());
    	paramList.add(conditionPO.getUpdateDate());
    	paramList.add(conditionPO.getReporter());
    	paramList.add(conditionPO.getReportDate());
    	paramList.add(conditionPO.getId());*/
    	
    	return this.update(sql, paramList);
    }
    
    /**
     * 修改工单状态
     * @param roNo 工单号
     * @param status 工单对应索赔单是否上报(0:未上报，1:已上报)
     */
    public void modifyWorkOrderStatus(String roNo,String status,Long userId){
    	
    	if(!Utility.testString(roNo))
    		return;
    	TtAsRepairOrderPO conditionPO = new TtAsRepairOrderPO();
    	conditionPO.setRoNo(roNo);
    	TtAsRepairOrderPO orderPO = new TtAsRepairOrderPO();
    	orderPO.setRoStatus(Integer.parseInt(status));
    	orderPO.setUpdateBy(userId);
    	orderPO.setUpdateDate(new Date());
    	
    	this.update(conditionPO, orderPO);
    }
    
	/**
	 * 拼查询条件，如果页面查询过来不为空，则拼装到查询条件中
	 * @param param 参数列 对应数据库中字段
	 * @param value 参数值
	 * @param oper 操作符
	 * @param sBuilder 拼装条件容器
	 * @param paramList 参数列表
	 * @param dataType 数据类型
	 *        1 : 时间
	 *        2 ：其他
	 * @param dataFormat 数据格式，现在只有时间类型需要添加格式
	 * @param table 标明表名或别名
	 * @return
	 */
	private void createWhereMap(String param,String oper,String value,
			StringBuilder sBuilder,List<Object> paramList,int dataType,
			String dataFormat,String table){
		if(Utility.testString(value)){
			param = table + "." + param;
			if(dataType==1) {//时间
				sBuilder.append(" and ").append(param).append(" ")
				.append(oper).append(" TO_DATE(" +"?" + ",'" + dataFormat + "')");
				paramList.add(value);
			}else{//其他
				sBuilder.append(" and ").append(param).append(" ").append(oper).append(" ?");
				if("LIKE".equalsIgnoreCase(oper)){//模糊查询
					paramList.add("%" +value +"%");
				}else{
					paramList.add(value);
				}
			}
		}
	}
	/****
	 * 
	 * 查询该车是否重复做首保
	 * @param id
	 * @param expStatus
	 * @return
	 */
    public List<Map<String,Object>> checkModeMaintain2(String id,String expStatus){
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT count(1) as COUNT\n" );
    	sql.append("FROM TT_AS_WR_APPLICATION A\n" );
    	sql.append("WHERE 1=1\n" );
    	sql.append("AND A.STATUS NOT IN ("+expStatus+")\n" );
    	sql.append("AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_02+"\n" );
    	sql.append("and a.free_m_amount=1");
    	sql.append("and (a.is_return ="+Constant.IF_TYPE_NO+" or a.is_return  is null ) ");
    	sql.append("and a.id not in (?)");
    	sql.append("AND A.VIN in (select vin from TT_AS_WR_APPLICATION where id=?)");
    	
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(id);
    	paramList.add(id);
    	return this.pageQuery(sql.toString(), paramList, this.getFunName());
    }
    /******
     * 根据索赔单ID 查询出车型 和 配置 状态 
     * @param claimId
     * @return
     */
    public List<Map<String,Object>> viewApplication(String claimId){
    	StringBuffer sql= new StringBuffer();
    	sql.append("select substr(mg.group_code,\n" );
    	sql.append("              instr(mg.group_code, '.', 1, 1) + 1,\n" );
    	sql.append("              length(mg.group_code)) as model_status,a.claim_type,\n" );
    	sql.append("       a.model_code,A.CREATE_DATE,A.RO_STARTDATE,A.VIN,A.DEALER_ID,v.purchased_date\n" );
    	sql.append("  from Tt_As_Wr_Application a, tm_vehicle v, tm_vhcl_material_group mg\n" );
    	sql.append(" where a.id = ?\n" );
    	sql.append("   and a.vin = v.vin\n" );
    	sql.append("   and v.package_id = mg.group_id");
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(claimId);
    	return this.pageQuery(sql.toString(), paramList, this.getFunName());
    }
    
    public List<Map<String,Object>> viewPartType(String claimId){
    	StringBuffer sql= new StringBuffer();
    	sql.append("select p.part_code,p.wr_labourcode,ppt.parttype_code from Tt_As_Wr_Partsitem p,tm_pt_part_base pb,tm_pt_part_type ppt  where p.id=?\n" );
    	sql.append("and  p.part_code = pb.part_code\n" );
    	sql.append("and pb.part_type_id = ppt.id(+)");
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(claimId);
    	return this.pageQuery(sql.toString(), paramList, this.getFunName());
    }
    public List<Map<String,Object>> viewLableDetail(String claimId){
    	StringBuffer sql= new StringBuffer();
    	sql.append("select l.wr_labourcode from Tt_As_Wr_Labouritem l where l.id="+claimId+"");
    	return this.pageQuery(sql.toString(), null, this.getFunName());
    }
    
    
    
    
    public List<Map<String,Object>> viewAuditing(String modelCode,String modelStatus,String labourCode ,String partTypeCode,String partCode){
        StringBuffer sql= new StringBuffer();
        sql.append("SELECT COUNT(1) AS COUNT, MAX(R.REMARK) AS REMARK\n" );
        sql.append("  FROM tm_pt_claim_rule R\n" );
        sql.append(" WHERE (R.MODE_CODE IS NULL OR R.MODE_CODE = '"+modelCode+"')\n" );
        sql.append("   AND (R.MODEL_STATUS IS NULL OR R.MODEL_STATUS = '"+modelStatus+"')\n" );
        sql.append("   AND (R.LABOUR_CODE IS NULL OR R.LABOUR_CODE = '"+labourCode+"')\n" );
        sql.append("and (r.part_code is null or r.part_code='"+partCode+"')");
        sql.append("   AND (R.PART_TYPE_CODE IS NULL OR R.PART_TYPE_CODE = '"+partTypeCode+"')\n" );
        sql.append(" GROUP BY R.REMARK");
    	return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public List<TmPtDealerPartPO> viewDealerPart(long id){
    	StringBuffer sql= new StringBuffer();
    	sql.append("select P.*\n" );
    	sql.append("  from tm_pt_dealer_part P, Tt_As_Wr_Application WA, Tt_As_Wr_Partsitem WP\n" );
    	sql.append(" WHERE WA.ID = "+id+"\n" );
    	sql.append("   AND WA.ID = WP.ID\n" );
    	sql.append("   AND WA.DEALER_ID = P.DEALER_ID\n" );
    	sql.append("   AND WP.PART_CODE = P.PART_CODE AND P.IS_DEL=1 AND P.STATUS=1");

    	return this.select(TmPtDealerPartPO.class, sql.toString(), null);
    	
    	
    }
    
    
    
    
	/****
	 * 
	 * 查询该车是否重复做首保
	 * @param id
	 * @param expStatus
	 * @return
	 */
    public List<TtAsWrApplicationPO> viewClaimType(String id){
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT claim_type as claim_type\n" );
    	sql.append("FROM TT_AS_WR_APPLICATION A\n" );
    	sql.append("WHERE 1=1\n" );
    	sql.append("and id= ?");
    	
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(id);
    	return this.select(TtAsWrApplicationPO.class, sql.toString(), paramList);
    }
    
    //重复做首保将状态置为拒绝
    public void updateClaimStatus (String id){
    	StringBuilder sql= new StringBuilder();
    	Date date = new Date();
    	 sql.append("update tt_as_wr_application set status="+Constant.CLAIM_APPLY_ORD_TYPE_05+",auditing_date=sysdate where id=?" );
    	List<Object> paramList = new ArrayList<Object>();
        paramList.add(id);
        this.update(sql.toString(), paramList);
    }
    /********/
    public void updateClaimDealerPart (String id){
    	StringBuilder sql= new StringBuilder();
    	Date date = new Date();
    	 sql.append("update tt_as_wr_application set status="+Constant.CLAIM_APPLY_ORD_TYPE_03+",auditing_date=sysdate,auth_code='200' where id=?" );
    	List<Object> paramList = new ArrayList<Object>();
        paramList.add(id);
        this.update(sql.toString(), paramList);
    }
    
    public void InsertClaimAudit(String claimId,Integer status){
    	TtAsWrAppauthitemPO appAuthPO = new TtAsWrAppauthitemPO();
		appAuthPO.setId(Long.parseLong(claimId));
		appAuthPO.setApprovalPerson("自动审核"+" [授权审核]");//自动审核 审核人员 固定为100
		appAuthPO.setApprovalLevelCode("100");//自动审核 授权角色 固定为100
		appAuthPO.setApprovalDate(new Date());
		appAuthPO.setApprovalResult(String.valueOf(status));//授权结果=索赔申请单状态
		appAuthPO.setRemark("该车辆存在首保，不能再做首保");//备注=审核不通过理由
		appAuthPO.setCreateBy(new Long(-1));
		appAuthPO.setCreateDate(new Date());
		this.insert(appAuthPO);
		
    }
    public void InsertClaimAudit2(String claimId,Integer status,String remark){
    	TtAsWrAppauthitemPO appAuthPO = new TtAsWrAppauthitemPO();
		appAuthPO.setId(Long.parseLong(claimId));
		appAuthPO.setApprovalPerson("自动审核"+" [授权审核]");//自动审核 审核人员 固定为100
		appAuthPO.setApprovalLevelCode("100");//自动审核 授权角色 固定为100
		appAuthPO.setApprovalDate(new Date());
		appAuthPO.setApprovalResult(String.valueOf(status));//授权结果=索赔申请单状态
		appAuthPO.setRemark(remark);//备注=审核不通过理由
		appAuthPO.setCreateBy(new Long(-1));
		appAuthPO.setCreateDate(new Date());
		this.insert(appAuthPO);
		
    }
    /***售前维修授权备注****/
    public void InsertClaimAudit3(String claimId,Integer status,String remark){
    	TtAsWrAppauthitemPO appAuthPO = new TtAsWrAppauthitemPO();
		appAuthPO.setId(Long.parseLong(claimId));
		appAuthPO.setApprovalPerson("自动审核"+" [授权审核]");//自动审核 审核人员 固定为100
		appAuthPO.setApprovalLevelCode("400");//自动审核 授权角色 固定为100
		appAuthPO.setApprovalDate(new Date());
		appAuthPO.setApprovalResult(String.valueOf(status));//授权结果=索赔申请单状态
		appAuthPO.setRemark(remark);//备注=审核不通过理由
		appAuthPO.setCreateBy(new Long(-1));
		appAuthPO.setCreateDate(new Date());
		this.insert(appAuthPO);
		
    }
    public List<TtAsWrApplicationPO> checkVinOnce(String vin,String vDate,String dealerId){
    	StringBuffer sql= new StringBuffer();
    	sql.append("\n" );
    	sql.append("select * from Tt_As_Wr_Application wa where wa.vin='"+vin+"'\n" );
    	sql.append(" and wa.RO_STARTDATE>=to_date('"+vDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')");
    	sql.append(" and wa.RO_STARTDATE<=to_date('"+vDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')");
    	sql.append("and wa.dealer_id="+dealerId+"\n");
        sql.append("and wa.claim_type in (10661001,10661007,10661009) and wa.status in (10791002,10791003,10791004,10791005,10791006,10791007,10791008)\n");
    	
    	
    	return this.select(TtAsWrApplicationPO.class,sql.toString(), null);
    }
    /******
     * 根据索赔单ID 查询出维修类型 
     * @param claimId
     * @return
     */
    public List<Map<String,Object>> ApplicationForRepair(String claimId){
    	StringBuffer sql= new StringBuffer();
    	sql.append("select t2.repair_type_code From TT_AS_WR_APPLICATION t1,tt_as_repair_order t2 where t1.ro_no=t2.ro_no and t1.id=?" );
    	
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(claimId);
    	return this.pageQuery(sql.toString(), paramList, this.getFunName());
    }

    /******
     * 根据索赔单ID 查询出明细ID与数量
     * @param claimId
     * @return
     */
    public List<Map<String,Object>> Partsitem(String claimId){
    	StringBuffer sql= new StringBuffer();
    	sql.append("select b.yieldly,a.part_id,a.quantity,D.dealer_id,D.DEALER_CODE From Tt_As_Wr_Partsitem a,TT_AS_WR_APPLICATION b ,TM_PT_PART_BASE P,TM_DEALER D where b.id=? AND B.DEALER_ID = D.DEALER_ID and a.id=b.id AND A.DOWN_PART_CODE NOT IN('NO_PARTS','00-000','00-0000') and a.AUDIT_STATUS="+Constant.PART_AUDIT_STATUS_03+" AND A.DOWN_PART_CODE = P.PART_CODE and  a.quantity>0  AND a.IS_RETURN = "+Constant.IS_RETURN_01 );
    	
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(claimId);
    	return this.pageQuery(sql.toString(), paramList, this.getFunName());
    }

    /******
     * 插入条码表信息
     * @param claimId
     * @return
     */
    public void insertBarcode(String partId,String yieldly,Long dealerId,String xuHao,String code){
    	TtAsWrPartsitemBarcodePO tpb=new TtAsWrPartsitemBarcodePO();
    	String barcodeId = SequenceManager.getSequence("");
    	tpb.setBarcodeId(Long.valueOf(barcodeId));
    	
    	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date  date = new Date();
		String str = sf.format(date);
    	String s1 = str.substring(2,4);
    	String s2 = str.substring(5,7);
    	String qz= code+s1+s2;//5位服务站代码+年+月= 5+2+2 = 9位
    	 System.out.println("qz:"+qz);
    	 qz = getQz(qz);
    	 String sql="INSERT INTO Tt_As_Wr_Partsitem_Barcode  (Create_By,Create_Date, Part_Id, Barcode_Id, Barcode_No,Serial_number) VALUES  ("+dealerId+",sysdate, "+partId+", "+barcodeId+",'"+qz+"','"+xuHao+"')";
    	 this.insert(sql);
    }
    public String getQz(String code){
    	String sql="select  '"+code+"'||seq_b.nextval  NUM  from dual ";
		List<Map<String,Object>> ps=this.pageQuery(sql.toString(), null, this.getFunName());
		return ps.get(0).get("NUM").toString();
		
    	
    }
    public List<Map<String,Object>> queryBarcode(String qz){
    	StringBuffer sql= new StringBuffer();
    	sql.append("select BARCODE_NO  From tt_as_wr_partsitem_barcode WHERE BARCODE_NO LIKE '"+qz+"%' order by BARCODE_NO desc" );
    	return this.pageQuery(sql.toString(), null, this.getFunName());
    }
    
    public String GenerateStockNo(String yieldly){
		String sql="select ((TO_CHAR(SYSDATE, 'YYYYMMDD')*100000)+seq_c.NEXTVAL) as num From DUAL";
		yieldly=yieldly.substring(0,8);
		System.out.println(yieldly);
		List<Map<String,Object>> ps=this.pageQuery(sql.toString(), null, this.getFunName());
		String StockNo=ps.get(0).get("NUM").toString();
		 //查询属于轿车或者微车
	   	 TcCodePO tcp=new TcCodePO();
	   	 tcp.setType(String.valueOf(Constant.chana));
	   	List<TcCodePO> ls= this.select(tcp);
	   	TcCodePO tcp1=new TcCodePO();
	   	tcp1= ls.get(0);
	   	String codeId= tcp1.getCodeId();
	 System.out.println(codeId);
	   	//取预留码
	   	if(codeId.equals(String.valueOf(Constant.chana_jc))){
	   		StockNo="CQJC"+StockNo;
	   	}
	   	else if(codeId.equals(String.valueOf(Constant.chana_wc))){
	   		if(yieldly.equals(Constant.SERVICEACTIVITY_CAR_YIELDLY_01.toString())){
	   			StockNo="CQCVS"+StockNo;
	   		}
	   		if(yieldly.equals(Constant.SERVICEACTIVITY_CAR_YIELDLY_02.toString())){
	   			StockNo="NJCA"+StockNo;
	   		}
	   		if(yieldly.equals(Constant.SERVICEACTIVITY_CAR_YIELDLY_03.toString())){
	   			StockNo="HBCA"+StockNo;
	   		}
	   		if(yieldly.equals(Constant.SERVICEACTIVITY_CAR_YIELDLY_03.toString())){
	   			StockNo="CHCA"+StockNo;
	   		}
	   		
	   	}
		return StockNo;
	}
    
    //根据索赔单ID查询是否已经有条码
    public List<Map<String,Object>> queryIsBarcode(String claimId){
    	StringBuffer sql= new StringBuffer();
    	sql.append("select a.id from TT_AS_WR_APPLICATION a,Tt_As_Wr_Partsitem b,Tt_As_Wr_Partsitem_Barcode c where a.id=b.id and b.part_id=c.part_id and a.id=?" );
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(claimId);
    
    	return this.pageQuery(sql.toString(), paramList, this.getFunName());
    }
    
    //现在校验码
    public void updateBarcodeNo(String barcode,Long barcodeId){
    	String SQL="update Tt_As_Wr_Partsitem_Barcode set barcode_NO="+barcode+" where barcode_id="+barcodeId;
    	this.update(SQL,null);
    }
    //质量信息反馈单基础数据
    public TtAsWrApplicationExtPO getBaseBean(String id){
    	StringBuilder sql= new StringBuilder();
    	sql.append("select  a.accessories_price,a.claim_no,a.fk_no,d.dealer_code,d.phone,a.dealer_shortname,a.model_code,tc.main_phone customer_phone,tc.ctm_name customer_name\n");
    	sql.append(",to_char(f.approval_date,'yyyy-mm-dd hh24:mi') app_date, to_char(f.update_date,'yyyy-mm-dd hh24:mi') agree_date,\n");
    	sql.append("to_char( v.purchased_date,'yyyy-mm-dd ') buy_date,to_char(v.factory_date,'yyyy-mm-dd') out_date,a.in_mileage,\n");
    	sql.append("v.vin,v.engine_no,a.claim_type,a.trouble_desc\n");
    	sql.append("from tt_as_wr_application a\n");
    	sql.append("left join tt_as_wr_foreapproval f on f.ro_no = a.ro_no,\n");
    	sql.append("tm_dealer d ,\n");
    	sql.append("tm_vehicle v left join tt_dealer_actual_sales sa on sa.vehicle_id = v.vehicle_id and sa.is_return="+Constant.IF_TYPE_NO+"\n");
    	sql.append("left join tt_customer tc on tc.ctm_id = sa.ctm_id \n");
    	sql.append("where  d.dealer_id = nvl(a.second_dealer_id,a.dealer_id)\n");
    	sql.append("and a.vin = v.vin\n");
    	sql.append("and a.id="+id); 
    	return (TtAsWrApplicationExtPO) this.select(TtAsWrApplicationExtPO.class, sql.toString(), null).get(0);
    }
    public List<TtAsWrApplicationExtPO> getDetail(String id){
    	StringBuffer sql= new StringBuffer();
    	sql.append("select a.accessories_price,p.part_code,p.part_name,p.quantity,p.price,p.amount,p.down_product_code supply_name,decode(p.quantity,0,'维修','更换') deal_type,\n");
    	sql.append("m.mal_name,l.wr_labourcode labour_code ,l.labour_hours,l.labour_amount\n");
    	sql.append("from tt_as_wr_partsitem p,tt_as_wr_labouritem l left join tt_as_wr_malfunction m on m.mal_id = l.trouble_code, tt_as_wr_application a\n");
    	sql.append("where a.id = p.id and a.id = l.id\n");
    	sql.append("and p.wr_labourcode = l.wr_labourcode\n");
    	sql.append("and a.id="+id); 

    	return this.select(TtAsWrApplicationExtPO.class,sql.toString(), null);
    }

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> queryClaim(RequestWrapper request,AclUserBean loginUser, Integer currPage, Integer pageSize) {
		String claimNo = DaoFactory.getParam(request,"CLAIM_NO");//索赔申请单号
		String lineNo = DaoFactory.getParam(request,"LINE_NO");//行号
		String claimType = DaoFactory.getParam(request,"CLAIM_TYPE");//索赔类型
		String vin = DaoFactory.getParam(request,"VIN");//车辆唯一标识码
		String applyStartDate = DaoFactory.getParam(request,"APPLY_DATE_START");//申请日期范围（开始时间）
		String applyEndDate = DaoFactory.getParam(request,"APPLY_DATE_END");//申请日期范围（结束时间）
		String dealerid = loginUser.getDealerId();//经销商ID
		
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT decode(o.is_claim_fore, 0, 1, 0) print,\n" );
		sb.append("       case\n" );
		sb.append("         when to_char(sysdate, 'yyyy-mm-dd') >\n" );
		sb.append("              to_char(last_day(A.CREATE_DATE), 'yyyy-mm-dd') then\n" );
		sb.append("          '索赔单已跨月请废弃重新生成'\n" );
		sb.append("       end remake,\n" );
		sb.append("       a.gross_credit,\n" );
		sb.append("       o.id REDID,\n" );
		sb.append("       A.CLAIM_NO,\n" );
		sb.append("       B.DEALER_CODE,\n" );
		sb.append("       B.DEALER_SHORTNAME DEALER_NAME,\n" );
		sb.append("       a.balance_yieldly,\n" );
		sb.append("       A.RO_NO AS RO_NO,\n" );
		sb.append("       A.LINE_NO,\n" );
		sb.append("       A.CLAIM_TYPE,\n" );
		sb.append("       NVL(A.SUBMIT_TIMES, 0) SUBMIT_TIMES,\n" );
		sb.append("       A.VIN,\n" );
		sb.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE,\n" );
		sb.append("       A.STATUS,\n" );
		sb.append("       A.ID\n" );
		sb.append("  FROM TT_AS_WR_APPLICATION A\n" );
		sb.append("  left join tt_as_repair_order o\n" );
		sb.append("    on o.ro_no = a.ro_no, TM_DEALER B\n" );
		sb.append(" WHERE 1 = 1\n" );
		sb.append("   AND DECODE(a.SECOND_DEALER_ID, null, b.DEALER_ID, a.SECOND_DEALER_ID) IN\n" );
		sb.append("       (SELECT d.DEALER_ID\n" );
		sb.append("          FROM TM_DEALER D\n" );
		sb.append("         START WITH d.DEALER_ID = '"+dealerid +"'\n");
		sb.append("        CONNECT BY PRIOR d.DEALER_ID = d.PARENT_DEALER_D)\n" );
		sb.append("   AND A.DEALER_ID = B.DEALER_ID\n" );
		sb.append("   AND A.STATUS in (10791006, 10791001)");
		DaoFactory.getsql(sb, "a.CLAIM_NO", claimNo, 2);
		DaoFactory.getsql(sb, "o.ro_no", claimNo, 2);
		DaoFactory.getsql(sb, "a.LINE_NO", lineNo, 2);
		DaoFactory.getsql(sb, "a.CLAIM_TYPE", claimType, 1);
		DaoFactory.getsql(sb, "a.CREATE_DATE", applyStartDate, 3);
		DaoFactory.getsql(sb, "a.CREATE_DATE", applyEndDate, 4);
		DaoFactory.getsql(sb, "a.vin", vin, 41);
		sb.append(" ORDER BY A.ID DESC");
		PageResult<Map<String, Object>> result =this.pageQuery(sb.toString(),null,getFunName(),pageSize,currPage);
		return result;
	}
	private static final String strSql = "update Tt_As_Wr_Application t set ";
	@SuppressWarnings("unchecked")
	public void doPassByClaimTypeAndId(String id, String auditOpinion, AclUserBean loginUser) {
		TtAsWrApplicationPO app =new TtAsWrApplicationPO();
		Long calim_id = BaseUtils.ConvertLong(id);
		app.setId(calim_id);
		List<TtAsWrApplicationPO> list=this.select(app);
		app = list.get(0);
		Integer claimType = app.getClaimType();
		String campaignCode = app.getCampaignCode();
		List<TtAsActivityPO> activityCodes = findActivityCodeByRepalce(campaignCode);//查出切换件的code
		int res = updateDataByTypeAndCode(String.valueOf(claimType), campaignCode, activityCodes);
		if(res==1){//为切换件服务活动 或者 
			doPassAndSetOtherMoney(id, auditOpinion,loginUser);
		}else if(res==2){//强保定检
			this.ChangeStatusPassByid(id, auditOpinion,loginUser);
			//暂时不做任何操作
		}else if (res==3) {//PDI
			doPassAndSetOtherMoneyPDI(id, auditOpinion,loginUser);
		}else{//其他都是有件的操作
			oldUpdateIdea(id,loginUser);
			this.ChangeStatusPassByid(id, auditOpinion,loginUser);
		}
		this.recordForapplication(auditOpinion, loginUser, calim_id);
	}
	/**
	 * 老逻辑
	 * @param id
	 * @param loginUser 
	 * @param loginUser 
	 */
	@SuppressWarnings("unchecked")
	public void oldUpdateIdea(String id, AclUserBean loginUser) {
		//配件
		StringBuffer sb= new StringBuffer();
		sb.append("update Tt_As_Wr_Partsitem p set p.audit_status= 95681003,p.audit_by='"+loginUser.getUserId()+"' where p.id ="+id);
		this.update(sb.toString(), null);
		//更新索赔单上回运标识
		String sql =    strSql+" T.DATA_TYPE = NVL((SELECT MIN(P.IS_RETURN) FROM TT_AS_WR_PARTSITEM A, TM_PT_PART_BASE P  WHERE A.PART_CODE = P.PART_CODE AND A.ID = T.ID), 95361002) WHERE T.ID="+id;
		this.update(sql, null);
		Long  sNum=Long.valueOf(0);
		Long aNum=Long.valueOf(0);
		//查询明细并添加条码明细
		//新增了限制条件，只选择配件是审核通过了的
		List<Map<String,Object>> Parts = this.Partsitem(id);
		if(Parts!=null&&Parts.size()>0){
			System.out.println(Parts.size()+"配件数量");
			for(int i=0;i<Parts.size();i++){
				Long quantity =Long.valueOf(Parts.get(i).get("QUANTITY").toString());
				aNum+=quantity;
			}
			System.out.println(aNum+"总数量");
			for(int i=0;i<Parts.size();i++){
				Long quantity =Long.valueOf(Parts.get(i).get("QUANTITY").toString());//配件数量
				String partId=Parts.get(i).get("PART_ID").toString();//配件明细ID
				String yieldly=Parts.get(i).get("YIELDLY").toString();//产地
				String dealer = Parts.get(i).get("DEALER_ID").toString();//索赔单中的经销商ID
				String dealerCode = Parts.get(i).get("DEALER_CODE").toString();//索赔单中的经销商ID
				for(int j=0;j<quantity;j++){
					if(quantity>0){
					sNum++;
					String xuHao=sNum.toString()+"/"+aNum.toString();
					TtAsWrPartsitemBarcodePO bp = new TtAsWrPartsitemBarcodePO();
					bp.setPartId(Long.valueOf(partId));
					bp.setSerialNumber(xuHao);
					List<TtAsWrPartsitemBarcodePO> list1 = this.select(bp);
					if(list1==null||list1.size()==0){
						this.insertBarcode(partId, yieldly, Long.valueOf(dealer),xuHao,dealerCode);
					}
				}
			  }
			}
		}
	}
	public int  updateDataByTypeAndCode(String claimType,String campaignCode,List<TtAsActivityPO> activityCodes){
		int res=0;
		//PDI 服务活动除切换件 强保定检 
		if("10661006".equals(claimType)){
			if(juge(activityCodes,campaignCode)){
				res=1;
			}
		}else if("10661011".equals(claimType)){//PDI
			res=3;
		}else if("10661002".equals(claimType)){
			res=2;
		}else{
			res=0;
		}
		return res;
		
	}
	private boolean juge(List<TtAsActivityPO> activityCodes, String campaignCode) {
		boolean flag=true;
		for (TtAsActivityPO tt : activityCodes) {
			String activityCode = tt.getActivityCode();
			if(activityCode.equals(campaignCode)){
				flag=false;
				break;
			}
		}
		return flag;
	}
	@SuppressWarnings("unchecked")
	private void doPassAndSetOtherMoney(String id, String auditOpinion, AclUserBean loginUser) {
		this.update(strSql+" t.balance_amount=t.repair_total where t.id="+id, null);
		ChangeStatusPassByid(id,auditOpinion,loginUser);
	}
	//PDI数据修改
	@SuppressWarnings("unchecked")
	private void doPassAndSetOtherMoneyPDI(String id, String auditOpinion, AclUserBean loginUser) {
		this.update(strSql+" t.balance_amount=t.repair_total,t.balance_netitem_amount=t.repair_total where t.id="+id, null);
		ChangeStatusPassByid(id,auditOpinion,loginUser);
	}
	/**
	 * 找到切换件活动code
	 * @param campaignCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<TtAsActivityPO> findActivityCodeByRepalce(String campaignCode) {
		TtAsActivityPO ac =new TtAsActivityPO();
		ac.setActivityType(10561005);
		return  this.select(ac);
	}
	/**
	 * 记录审核日志
	 * @param auditOpinion
	 * @param loginUser
	 * @param calim_id
	 */
	@SuppressWarnings("unchecked")
	private void recordForapplication(String auditOpinion, AclUserBean loginUser,Long calim_id) {
		TtAsWrAppAuditDetailPO record = new TtAsWrAppAuditDetailPO();
		record.setId(DaoFactory.getPkId());
		record.setAuditBy(loginUser.getUserId());
		record.setAuditDate(new Date());
		record.setAuditRemark(auditOpinion);
		record.setAuditResult(10791008);
		record.setClaimId(calim_id);
		record.setCreateBy(loginUser.getUserId());
		record.setCreateDate(new Date());
		record.setAuditType(1);
		this.insert(record);
	}
	/**
	 * 修改审核状态和审核备注
	 * @param id
	 * @param auditOpinion
	 * @param loginUser 
	 */
	@SuppressWarnings("unchecked")
	private void ChangeStatusPassByid(String id, String auditOpinion, AclUserBean loginUser) {
		this.update(strSql+" t.status=10791008,t.balance_amount=t.repair_total,t.audit_opinion='"+auditOpinion+"',report_date=sysdate,update_Date=sysdate,update_by='"+loginUser.getUserId()+"' where t.id="+id, null);
	}
	@SuppressWarnings("unchecked")
	public void doRebutByClaimId(String[] ids, String auditOpinion, AclUserBean loginUser) {
		for (String id : ids) {
			//-------lj 3.23  更新审核人、时间
			DealerClaimReportDao reportDao = new DealerClaimReportDao();
			StringBuffer sb = new StringBuffer();
			AclUserBean logonUser = loginUser;
			sb.append(" UPDATE  TT_AS_WR_APPLICATION  TT SET \n");
			sb.append("TT.AUDITING_MAN = "+logonUser.getUserId());
			sb.append(",\n" );
			sb.append("TT.AUDITING_DATE = sysdate " );
			sb.append("\n" );
			sb.append("where TT.ID ="+id);
			reportDao.update(sb.toString(), null);
			this.update(strSql+" t.status=10791006,t.audit_opinion='"+auditOpinion+"',report_date=sysdate,update_Date=sysdate,update_by='"+loginUser.getUserId()+"' where t.id="+id, null);
		}
	}
}

