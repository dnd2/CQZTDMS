package com.infodms.yxdms.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.omg.CORBA.Request;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivityProjectPO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsWrAppAuditDetailPO;
import com.infodms.dms.po.TtAsWrApplicationExtPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrCompensationAppPO;
import com.infodms.dms.po.TtAsWrLabouritemPO;
import com.infodms.dms.po.TtAsWrNetitemPO;
import com.infodms.dms.po.TtAsWrOutrepairPO;
import com.infodms.dms.po.TtAsWrPartsitemBarcodePO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infodms.dms.po.TtClaimAccessoryDtlPO;
import com.infodms.dms.po.TtDealerRelationPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infodms.yxdms.constant.MyConstant;
import com.infodms.yxdms.entity.claim.TtAsWrOutrepairMoneyPO;
import com.infodms.yxdms.entity.ysq.TtAsComRecordPO;
import com.infodms.yxdms.service.OrderService;
import com.infodms.yxdms.service.impl.OrderServiceImpl;
import com.infodms.yxdms.utils.BaseUtils;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("rawtypes")
public class ClaimDAO extends IBaseDao{

	private static final ClaimDAO dao = new ClaimDAO();
	public static final ClaimDAO getInstance(){
		if (dao == null) {
			return new ClaimDAO();
		}
		return dao;
	}
	
	private static final String strSql = "update Tt_As_Wr_Application t set ";
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> pdiManageList(AclUserBean loginUser,RequestWrapper request, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select a.id,a.claim_type,\n" );
		sb.append("       a.claim_no,\n" );
		sb.append("       a.balance_yieldly,\n" );
		sb.append("       a.vin,\n" );
		sb.append("       a.create_date,\n" );
		sb.append("       a.status,a.BALANCE_AMOUNT,\n" );
		sb.append("       a.submit_times,n.is_agree,\n" );
		sb.append("       (select count(1) from Tt_As_Wr_App_Audit_Detail d where d.audit_result=10791006 and  d.claim_id=a.id) back_times\n" );
		sb.append("  from Tt_As_Wr_Application a,Tt_As_Wr_Netitem n\n" );
		sb.append(" where a.id=n.id and a.claim_type = 10661011\n");
		DaoFactory.getsql(sb, "a.create_date", MyConstant.onlineDate, 3);//加入时间新分单节点
		DaoFactory.getsql(sb, "a.claim_no", DaoFactory.getParam(request, "claim_no"), 2);
		DaoFactory.getsql(sb, "a.create_date", DaoFactory.getParam(request, "beginTime"), 3);
		DaoFactory.getsql(sb, "a.create_date", DaoFactory.getParam(request, "endTime"), 4);
		DaoFactory.getsql(sb, "a.status", DaoFactory.getParam(request, "status"), 1);
		DaoFactory.getsql(sb, "a.vin", DaoFactory.getParam(request, "vin"), 6);
		DaoFactory.getsql(sb, "a.dealer_id", loginUser.getDealerId(), 1);
		sb.append("   order by a.create_date desc \n" );
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public int pdiAddSure(AclUserBean loginUser, RequestWrapper request) {
		int res=1;
		String identify = DaoFactory.getParam(request, "identify");
		String id = DaoFactory.getParam(request, "id");
		String vin = DaoFactory.getParam(request, "vin");
		String model_name = DaoFactory.getParam(request, "model_name");
		String package_name = DaoFactory.getParam(request, "package_name");
		String engine_no = DaoFactory.getParam(request, "engine_no");
		String color = DaoFactory.getParam(request, "color");
		String model_id = DaoFactory.getParam(request, "model_id");
		
		String pdi = DaoFactory.getParam(request, "pdi");
		String netitemId = DaoFactory.getParam(request, "netitemId");
		String amount = DaoFactory.getParam(request, "amount");
		String is_agree = DaoFactory.getParam(request, "is_agree");
		String remark = DaoFactory.getParam(request, "remark");
		String[] fjids=request.getParamValues("fjid");
		try {
			if("".equals(id)){
				TtAsWrApplicationPO a =new TtAsWrApplicationPO();
				String pdiNo = this.getPdiNo(loginUser.getDealerId(),loginUser.getDealerCode());
				a.setClaimNo(pdiNo);
				a.setDealerId(Long.valueOf(loginUser.getDealerId()));
				a.setVin(vin);
				a.setSubmitTimes(0);
				a.setEngineNo(engine_no);
				a.setModelName(model_name);
				a.setBalanceYieldly(Integer.valueOf(95411001));
				a.setYieldly("2010010100000001");
				Long pkId = DaoFactory.getPkId();
				a.setId(pkId);
				Long userId = Long.valueOf(loginUser.getUserId());
				a.setCreateBy(userId);
				a.setCreateDate(new Date());
				a.setBalanceAmount(Double.valueOf(amount));
				a.setRepairTotal(Double.valueOf(amount));
				a.setGrossCredit(Double.valueOf(amount));
				a.setNetitemAmount(Double.valueOf(amount));
				a.setIsImport(10041002);
				a.setAppColor(color);
				a.setRoNo(pdiNo);
				a.setClaimType(10661011);
				a.setAppPackageName(package_name);
				a.setModelId(Long.valueOf(model_id));
				a.setLineNo(Long.valueOf(1));
				if("0".equals(identify)){
					a.setStatus(10791001);
				}else{
					a.setStatus(10791003);
					a.setSubDate(new Date());
					if(pdiExistCheck(vin,request,loginUser)){
						throw new RuntimeException("该vin已上报");
					}
				}
				this.insert(a);
				
				TtAsWrNetitemPO n=new TtAsWrNetitemPO();
				n.setAmount(Double.valueOf(amount));
				n.setIsAgree(Integer.valueOf(is_agree));
				n.setRemark(remark);
				n.setId(pkId);
				n.setNetitemId(DaoFactory.getPkId());
				n.setItemCode(pdi);
				n.setCreateDate(new Date());
				n.setCreateBy(userId);
				this.insert(n);
				this.delAndReinsetFile(loginUser, fjids, pkId.toString());

			}else{
				TtAsWrNetitemPO n=new TtAsWrNetitemPO();
				n.setNetitemId(Long.valueOf(netitemId));
				TtAsWrNetitemPO n1=new TtAsWrNetitemPO();
				n1.setIsAgree(Integer.valueOf(is_agree));
				n1.setRemark(remark);
				this.update(n, n1);
				TtAsWrApplicationPO a =new TtAsWrApplicationPO();
				a.setId(Long.valueOf(id));
				TtAsWrApplicationPO a1 =new TtAsWrApplicationPO();
				if("0".equals(identify)){
					a1.setStatus(10791001);
				}else{
					a1.setStatus(10791003);
					a1.setSubDate(new Date());
					if(pdiExistCheck(vin,request,loginUser)){
						throw new RuntimeException("该vin已上报");
					}
				}
				this.update(a, a1);
				this.delAndReinsetFile(loginUser, fjids, id.toString());
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}

	private void delAndReinsetFile(AclUserBean loginUser, String[] fjids,
			String pkId) throws SQLException {
		FileUploadManager.delAllFilesUploadByBusiness(pkId, fjids);
		FileUploadManager.fileUploadByBusiness(pkId, fjids, loginUser);
	}
	@SuppressWarnings("unchecked")
	private String getPdiNo(String dealerId,String dealerCode){
		String claimNo="";
        StringBuilder sbSql = new StringBuilder();
        /*sql.append("SELECT '"+dealerCode+"'|| 'P' || TO_CHAR(SYSDATE, 'yymmdd') ||\n");
        sql.append("       LPAD(NVL(MAX(SUBSTR(T.CLAIM_NO, -3)), 0) + 1, 3, 0) as SEQ\n");
        sql.append("  FROM TT_AS_WR_APPLICATION T\n");
        sql.append(" WHERE T.DEALER_ID = '"+dealerId+"' and t.claim_type = 10661011 \n");
        sql.append("   AND TRUNC(T.CREATE_DATE) = TRUNC(SYSDATE)\n");*/
        sbSql.append("SELECT '"+dealerCode+"' ||'P'|| TO_CHAR(SYSDATE, 'yymmdd') ||\n");
        sbSql.append("       LPAD(NVL(MAX(SUBSTR(T.CLAIM_NO, -3)), 0) + 1, 3, 0) as SEQ\n");
        sbSql.append("  FROM (select claim_no\n");
        sbSql.append("          FROM TT_AS_WR_APPLICATION A\n");
        sbSql.append("         WHERE A.DEALER_ID = '"+dealerId+"'\n");
        sbSql.append("           AND TRUNC(A.CREATE_DATE) = TRUNC(SYSDATE)\n");
        sbSql.append("           AND A.claim_type =10661011\n");
        sbSql.append("        union\n");
        sbSql.append("        select claim_no\n");
        sbSql.append("          FROM TT_AS_WR_APPLICATION_BACKUP B\n");
        sbSql.append("         WHERE B.DEALER_ID = '"+dealerId+"'\n");
        sbSql.append("           AND TRUNC(B.CREATE_DATE) = TRUNC(SYSDATE)\n");
        sbSql.append("           AND B.claim_type =10661011) T"); 

        Map<String,Object> map = this.pageQueryMap(sbSql.toString(), null, getFunName());
        claimNo=map.get("SEQ").toString();
		return claimNo;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> pdiView(AclUserBean loginUser,RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("select a.claim_no,(select max(d.audit_remark) from Tt_As_Wr_App_Audit_Detail d where d.audit_date = (select max(d.audit_date) from Tt_As_Wr_App_Audit_Detail d where d.claim_id = a.id)) as audit_remark,\n" );
		sb.append("       a.app_color,\n" );
		sb.append("       a.app_package_name,\n" );
		sb.append("       a.vin,\n" );
		sb.append("       a.model_name,a.engine_no,\n" );
		sb.append("       n.*\n" );
		sb.append("  from Tt_As_Wr_Application a, tt_as_wr_netitem n\n" );
		sb.append(" where a.id=n.id  and  a.claim_type = 10661011");
		DaoFactory.getsql(sb, "a.dealer_id", loginUser.getDealerId(), 1);
		DaoFactory.getsql(sb, "a.id", DaoFactory.getParam(request, "id"), 1);
		return pageQueryMap(sb.toString(), null, getFunName());
	}

	@SuppressWarnings("unchecked")
	public int pdiDelete(RequestWrapper request) {
		int res=1;
		try {
			this.delete("delete from Tt_As_Wr_Application a where a.id="+DaoFactory.getParam(request, "id"), null);
			this.delete("delete from tt_as_wr_netitem n where n.id="+DaoFactory.getParam(request, "id"), null);
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public String checkPdiByVin(RequestWrapper request) {
		String res="";
		String vin = DaoFactory.getParam(request, "vin");
		TtAsWrApplicationPO t =new TtAsWrApplicationPO();
		t.setVin(vin);
		t.setClaimType(10661011);
		List<TtAsWrApplicationPO> select = this.select(t);
		if(select!=null && select.size()>0){
			res+="提示："+select.get(0).getDealerShortname()+" "+select.get(0).getClaimNo()+"已经做了PDI,请重新输入VIN";
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> keepFitManageList(AclUserBean loginUser, RequestWrapper request, Integer pageSize,Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select a.id,a.claim_type,\n" );
		sb.append("       a.claim_no,\n" );
		sb.append("       a.balance_yieldly,\n" );
		sb.append("       a.vin,\n" );
		sb.append("       a.create_date,a.balance_amount,\n" );
		sb.append("       a.status,\n" );
		sb.append("       (select r.owner_name from tt_as_repair_order r where r.ro_no= a.ro_no) owner_name,\n" );
		sb.append("       a.submit_times, \n" );
		sb.append("       (select count(1) from Tt_As_Wr_App_Audit_Detail d where d.audit_result=10791006 and  d.claim_id=a.id) back_times\n" );
		sb.append("  from Tt_As_Wr_Application a\n" );
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
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public int keepFitDelete(RequestWrapper request) {
		int res=1;
		try {
			this.delete("delete from Tt_As_Wr_Application a where a.id="+DaoFactory.getParam(request, "id"), null);
			this.delete("delete from Tt_As_Wr_Partsitem p where p.id="+DaoFactory.getParam(request, "id"), null);
			this.delete("delete from Tt_As_Wr_Labouritem l where l.id="+DaoFactory.getParam(request, "id"), null);
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public int keepFitCancel(RequestWrapper request) {
		int res=1;
		try {
			this.update("update Tt_As_Wr_Application a set a.status=10791001 where a.id="+DaoFactory.getParam(request, "id"), null);
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public int keepFitAddSure(AclUserBean loginUser, RequestWrapper request) {
		int res=1;
		String identify = DaoFactory.getParam(request, "identify");
		String id = DaoFactory.getParam(request, "id");
		String ro_no = DaoFactory.getParam(request, "ro_no");
		String vin = DaoFactory.getParam(request, "vin");
		String model_name = DaoFactory.getParam(request, "model_name");
		String package_name = DaoFactory.getParam(request, "package_name");
		String engine_no = DaoFactory.getParam(request, "engine_no");
		String color = DaoFactory.getParam(request, "color");
		String model_id = DaoFactory.getParam(request, "model_id");
		String guarantee_date = DaoFactory.getParam(request, "guarantee_date");
		String in_mileage = DaoFactory.getParam(request, "in_mileage");//行驶里程
		String model_code = DaoFactory.getParam(request, "model_code");//车型
		String winter_money = DaoFactory.getParam(request, "winter_money");//冬季保养费用补偿
		
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
		String[] fjids=request.getParamValues("fjid");
		Long pkId = DaoFactory.getPkId();
		try {
			if("".equals(id)){
				TtAsWrApplicationPO a =new TtAsWrApplicationPO();
				String keepFitNo = this.getKeepFitNo(loginUser.getDealerId(),loginUser.getDealerCode());
				a.setClaimNo(keepFitNo);
				a.setDealerId(Long.valueOf(loginUser.getDealerId()));
				a.setVin(vin);
				a.setSubmitTimes(0);
				a.setEngineNo(engine_no);
				a.setModelCode(model_code);
				a.setBalanceYieldly(Integer.valueOf(95411001));
				a.setYieldly("2010010100000001");
				a.setModelName(model_name);
				if(!"".equals(guarantee_date)){
					guarantee_date=guarantee_date.replaceAll("\\+", " ");
					a.setGuaranteeDate(BaseUtils.getDate(guarantee_date, 1));
				}
				a.setId(pkId);
				Long userId = Long.valueOf(loginUser.getUserId());
				a.setCreateBy(userId);
				a.setCreateDate(new Date());
				a.setIsImport(10041002);
				a.setAppColor(color);
				a.setRoNo(ro_no);
				a.setClaimType(10661002);
				a.setAppPackageName(package_name);
				a.setModelId(Long.valueOf(model_id));
				a.setLineNo(Long.valueOf(1));
				a.setInMileage(Double.valueOf(in_mileage));//增加里程
				if (null!=winter_money && !"".equals(winter_money)) {
					a.setWinterMoney(Double.valueOf(winter_money));//冬季保养补偿费
				}
				if("0".equals(identify)){
					a.setStatus(10791001);
				}else{
					a.setStatus(10791003);
					a.setSubDate(new Date());
				}
				insetPoAndSet(part_id_2, part_code_2, part_name_2,
						part_quotiety_2, claim_price_param_2, part_amont_2,
						pay_type_2, part_use_type_2, labour_code_2, cn_des_2,
						labour_quotiety_2, parameter_value_2, labour_fix_2,
						pay_type_labour_2, pkId, a);
				Double give_money=this.addwinterMoney(request,a.getBalanceAmount(),Long.valueOf(loginUser.getDealerId()),vin,package_name);
				a.setBalanceAmount(give_money);
				a.setRepairTotal(give_money);
				a.setFreeMPrice(give_money);
				a.setGrossCredit(give_money);
				this.insert(a);	
				delAndReinsetFile(loginUser, fjids, pkId.toString());
			}else{
				TtAsWrApplicationPO a =new TtAsWrApplicationPO();
				Long pkid = Long.valueOf(id);
				a.setId(pkid);
				this.delete("delete from Tt_As_Wr_Partsitem p where p.id="+pkid, null);
				this.delete("delete from Tt_As_Wr_Labouritem l where l.id="+pkid, null);
				TtAsWrApplicationPO a1 =new TtAsWrApplicationPO();
				insetPoAndSet(part_id_2, part_code_2, part_name_2,
						part_quotiety_2, claim_price_param_2, part_amont_2,
						pay_type_2, part_use_type_2, labour_code_2, cn_des_2,
						labour_quotiety_2, parameter_value_2, labour_fix_2,
						pay_type_labour_2, pkid, a1);
				if("0".equals(identify)){
					a1.setStatus(10791001);
				}else{
					a1.setStatus(10791003);
					a1.setSubDate(new Date());
				}
				Double give_money=this.addwinterMoney(request,a1.getBalanceAmount(),Long.valueOf(loginUser.getDealerId()),vin,package_name);
				a1.setBalanceAmount(give_money);
				a1.setRepairTotal(give_money);
				a1.setFreeMPrice(give_money);
				a1.setGrossCredit(give_money);
				this.update(a, a1);
				delAndReinsetFile(loginUser, fjids, pkid.toString());
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}
	
	/**
	 * 保养加冬季的补助钱
	 * @param balanceAmount
	 * @param dealerId 
	 * @param vin 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Double addwinterMoney(RequestWrapper request ,Double balanceAmount, Long dealerId, String vin,String package_name) {
		StringBuffer sb= new StringBuffer();
		sb.append("select distinct a.amount, a.model_code, p.package_code,vw.package_name\n" );
		sb.append("      from TT_AS_WR_WINTER_MAINTEN        a,\n" );
		sb.append("           vw_material_group_service      vw,\n" );
		sb.append("           tm_vehicle                     v,\n" );
		sb.append("           TT_AS_WR_WINTER_PACKAGE        p\n" );
		sb.append("     where 1=1\n" );
		sb.append("     and a.status=10681002\n" );
		sb.append("     and a.id=p.wintwe_id(+)\n" );
		sb.append("       and p.package_code = vw.PACKAGE_CODE(+)\n" );
		sb.append("       and vw.package_id = v.package_id(+)");
		sb.append("   and v.vin = '"+vin+"'\n" );
		sb.append("   and p.dealer_id='"+dealerId+"'");
		sb.append("   and vw.package_name='"+package_name+"'");
		DaoFactory.getsql(sb, "a.model_code", DaoFactory.getParam(request,"model_code"), 1);
		sb.append("   and a.start_date<=sysdate");
		sb.append("   and a.end_date>=sysdate");
		Map<String,Object> map = this.pageQueryMap(sb.toString(), null, getFunName());
		double doubleValue = 0.0d;
		if(map!=null){
			BigDecimal object = (BigDecimal) map.get("AMOUNT");
			 doubleValue = object.doubleValue();
		}
		return  Arith.add(balanceAmount, doubleValue) ;
	}

	@SuppressWarnings("unchecked")
	private String getKeepFitNo(String dealerId,String dealerCode) {
		String claimNo="";
        StringBuilder sbSql = new StringBuilder();
       /* sql.append("SELECT '"+dealerCode+"'|| 'B' || TO_CHAR(SYSDATE, 'yymmdd') ||\n");
        sql.append("       LPAD(NVL(MAX(SUBSTR(T.CLAIM_NO, -3)), 0) + 1, 3, 0) as SEQ\n");
        sql.append("  FROM TT_AS_WR_APPLICATION T\n");
        sql.append(" WHERE T.DEALER_ID = '"+dealerId+"' and t.claim_type = 10661002 \n");
        sql.append("   AND TRUNC(T.CREATE_DATE) = TRUNC(SYSDATE)\n");
        */
        //增加废弃索赔单判断逻辑
        sbSql.append("SELECT '"+dealerCode+"' || 'B' || TO_CHAR(SYSDATE, 'yymmdd') ||\n");
        sbSql.append("       LPAD(NVL(MAX(SUBSTR(T.CLAIM_NO, -3)), 0) + 1, 3, 0) as SEQ\n");
        sbSql.append("  FROM (select claim_no\n");
        sbSql.append("          FROM TT_AS_WR_APPLICATION A\n");
        sbSql.append("         WHERE A.DEALER_ID = '"+dealerId+"'\n");
        sbSql.append("           AND TRUNC(A.CREATE_DATE) = TRUNC(SYSDATE)\n");
        sbSql.append("           AND A.claim_type = 10661002\n");
        sbSql.append("        union\n");
        sbSql.append("        select claim_no\n");
        sbSql.append("          FROM TT_AS_WR_APPLICATION_BACKUP B\n");
        sbSql.append("         WHERE B.DEALER_ID = '"+dealerId+"'\n");
        sbSql.append("           AND TRUNC(B.CREATE_DATE) = TRUNC(SYSDATE)\n");
        sbSql.append("           AND B.claim_type = 10661002) T"); 

        Map<String,Object> map = this.pageQueryMap(sbSql.toString(), null, getFunName());
        claimNo=map.get("SEQ").toString();
		return claimNo;
	}

	@SuppressWarnings("unchecked")
	private void insetPoAndSet(String[] part_id_2, String[] part_code_2,
			String[] part_name_2, String[] part_quotiety_2,
			String[] claim_price_param_2, String[] part_amont_2,
			String[] pay_type_2, String[] part_use_type_2,
			String[] labour_code_2, String[] cn_des_2,
			String[] labour_quotiety_2, String[] parameter_value_2,
			String[] labour_fix_2, String[] pay_type_labour_2, Long pkId,
			TtAsWrApplicationPO a) {
		double partAmont=0.0d;
		Integer quotietys=0;
		if(part_code_2!=null && part_code_2.length>0){
			for (int i = 0; i < part_code_2.length; i++) {
				TtAsWrPartsitemPO p=new TtAsWrPartsitemPO();
				p.setId(pkId);
				p.setPartId(DaoFactory.getPkId());
				p.setPartCode(part_code_2[i]);
				p.setPartName(part_name_2[i]);
				p.setQuantity(Float.valueOf(part_quotiety_2[i]));
				p.setBalanceQuantity(Float.valueOf(part_quotiety_2[i]));
				p.setApplyQuantity(Double.valueOf(part_quotiety_2[i]));
				Integer quotiety = Integer.valueOf(part_quotiety_2[i]);
				p.setBalancePrice(Double.valueOf(claim_price_param_2[i]));
				p.setApplyPrice(Double.valueOf(claim_price_param_2[i]));
				p.setPrice(Double.valueOf(claim_price_param_2[i]));
				
				Double part_Amont_2 = Double.valueOf(part_amont_2[i]);
				p.setAmount(part_Amont_2);
				p.setBalanceAmount(part_Amont_2);
				p.setApplyAmount(part_Amont_2);
				
				p.setRealPartId(Long.valueOf(part_id_2[i]));
				Integer part_use_type = Integer.valueOf(part_use_type_2[i]);
				Integer partusetype=null;
				if(part_use_type==95431001){
					partusetype=0;
				}else{
					partusetype=1;
				}
				p.setPartUseType(partusetype);
				p.setPayType(Integer.valueOf(pay_type_2[i]));
				p.setIsReturn(95361002);//保养的默认不回远
				partAmont+=part_Amont_2;
				quotietys+=quotiety;
				this.insert(p);
			}
		}
		double labourAmont=0.0d;
		float labourHours=0.0f;
		if(labour_code_2!=null && labour_code_2.length>0){
			for (int i = 0; i < labour_code_2.length; i++) {
				TtAsWrLabouritemPO l=new TtAsWrLabouritemPO();
				l.setLabourId(DaoFactory.getPkId());
				l.setId(pkId);
				l.setLabourCode(labour_code_2[i]);
				l.setLabourName(cn_des_2[i]);
				l.setWrLabourcode(labour_code_2[i]);
				l.setWrLabourname(cn_des_2[i]);
				Float labourHour = Float.valueOf(labour_quotiety_2[i]);
				l.setLabourQuantity(labourHour);
				l.setBalanceQuantity(labourHour);
				l.setLabourHours(labourHour);
				l.setApplyQuantity(Double.valueOf(labour_quotiety_2[i]));
				
				l.setPayType(Integer.valueOf(pay_type_labour_2[i]));
				
				l.setApplyPrice(Double.valueOf(parameter_value_2[i]));
				l.setLabourPrice(Float.valueOf(parameter_value_2[i]));
				l.setBalancePrice(Double.valueOf(parameter_value_2[i]));
				
				Double labourAmont_2 = Double.valueOf(labour_fix_2[i]);
				l.setBalanceAmount(labourAmont_2);
				l.setLabourAmount(labourAmont_2);
				l.setApplyAmount(labourAmont_2);
				labourAmont+=labourAmont_2;
				labourHours+=labourHour;
				this.insert(l);
			}
		}
		double allAmount=0.0d;
		allAmount=Arith.add(allAmount, partAmont);
		allAmount=Arith.add(allAmount, labourAmont);
		a.setBalanceAmount(allAmount);
		a.setBalanceLabourAmount(labourAmont);
		a.setBalancePartAmount(partAmont);
		a.setGrossCredit(allAmount);
		a.setLabourHours(labourHours);
		a.setPartsCount(quotietys);
		a.setRepairTotal(allAmount);
		a.setFreeMPrice(allAmount);
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> findRoKeepFit(AclUserBean loginUser,RequestWrapper request, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select o.id,o.ro_no,\n" );
		sb.append("       o.balance_yieldly,\n" );
		sb.append("       o.license,\n" );
		sb.append("       o.vin,\n" );
		sb.append("       o.model_name,\n" );
		sb.append("       o.owner_name,\n" );
		sb.append("       to_char(o.ro_create_date,'yyyy-mm-dd') as ro_create_date,\n" );
		sb.append("       o.in_mileage,\n" );
		sb.append("       o.free_times,\n" );
		sb.append("       o.ro_status,\n" );
		sb.append("       o.is_warning,\n" );
		sb.append("       o.balance_amount\n" );
		sb.append("  from Tt_As_Repair_Order o\n" );
		sb.append(" where 1 = 1 and o.ro_status=11591002 \n" );
		sb.append("   and o.order_valuable_type = 13591001 and o.id in (select p.ro_id from tt_as_ro_repair_part p where p.repairtypecode=93331002)\n" );
		sb.append("   and o.dealer_id ="+loginUser.getDealerId());
		DaoFactory.getsql(sb, "o.ro_no", DaoFactory.getParam(request, "ro_no"), 2);
		DaoFactory.getsql(sb, "o.vin", DaoFactory.getParam(request, "vin01"), 1);
		DaoFactory.getsql(sb, "o.ro_create_date", DaoFactory.getParam(request, "beginTime"), 3);
		DaoFactory.getsql(sb, "o.ro_create_date", DaoFactory.getParam(request, "endTime"), 4);
		sb.append(" and o.ro_no not in (select a.ro_no from Tt_As_Wr_Application a  where a.claim_type=10661002)\n");
		sb.append("   order by o.create_date desc \n" );
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> normalManageList(AclUserBean loginUser, RequestWrapper request, Integer pageSize,Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select a.id,a.claim_type,\n" );
		sb.append("       a.claim_no,\n" );
		sb.append("       a.balance_yieldly,\n" );
		sb.append("       a.vin,\n" );
		sb.append("       a.create_date,\n" );
		sb.append("       a.status,a.is_import,a.balance_amount,\n" );
		sb.append("       (select r.owner_name from tt_as_repair_order r where r.ro_no= a.ro_no) owner_name,\n" );
		sb.append("       a.submit_times,(select count(1) from Tt_As_Wr_App_Audit_Detail d where d.claim_id=a.id) as audit_times,\n" );
		sb.append("       (select count(1) from Tt_As_Wr_App_Audit_Detail d where d.audit_result=10791006 and  d.claim_id=a.id) back_times\n" );
		sb.append("  from Tt_As_Wr_Application a\n" );
		sb.append(" where a.claim_type not in(10661011,10661002) ");
		DaoFactory.getsql(sb, "a.create_date", MyConstant.onlineDate, 3);//加入时间新分单节点
		DaoFactory.getsql(sb, "a.claim_no", DaoFactory.getParam(request, "claim_no"), 2);
		DaoFactory.getsql(sb, "a.create_date", DaoFactory.getParam(request, "beginTime"), 3);
		DaoFactory.getsql(sb, "a.create_date", DaoFactory.getParam(request, "endTime"), 4);
		DaoFactory.getsql(sb, "a.status", DaoFactory.getParam(request, "status"), 1);
		DaoFactory.getsql(sb, "a.vin", DaoFactory.getParam(request, "vin"), 6);
		DaoFactory.getsql(sb, "a.claim_type", DaoFactory.getParam(request, "claim_type"), 1);
		DaoFactory.getsql(sb, "a.dealer_id", loginUser.getDealerId(), 1);
		DaoFactory.getsql(sb, "a.ysq_no", DaoFactory.getParam(request, "ysq_no"), 2);
		DaoFactory.getsql(sb, "a.ro_no",DaoFactory.getParam(request, "ro_no") , 6);
		sb.append(" and a.claim_no not in (select v.claim_no from tt_as_wr_repair_claim v where v.dealer_id='"+loginUser.getDealerId()+"')");
		sb.append("   order by a.create_date desc \n" );
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<FsFileuploadPO> queryAttById(String ywzj) {
		StringBuffer sql = new StringBuffer();
		List<FsFileuploadPO> ls = new ArrayList<FsFileuploadPO>();
		sql.append(" SELECT A.* FROM FS_FILEUPLOAD A WHERE 1=1");
		DaoFactory.getsql(sql, "A.YWZJ", ywzj, 1);
		ls = select(FsFileuploadPO.class, sql.toString(), null);
		return ls;
	}
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> findRoBaseInfo(AclUserBean loginUser, RequestWrapper request, Integer pageSize,Integer currPage) {
		String claimType=DaoFactory.getParam(request, "claimType");
		PageResult<Map<String, Object>> list=null;
		String dealerId = loginUser.getDealerId();
		if("10661010".equals(claimType) || "10661013".equals(claimType)){//特殊服务/配件索赔 工单号选择必须是系统判定是自费，服务站手工改为索赔的
			StringBuffer special= new StringBuffer();
			special.append("select t.*\n" );
			special.append("  from (select o.*,\n" );
			special.append("               (select count(p.ro_id)\n" );
			special.append("                  from tt_as_ro_repair_part p\n" );
			special.append("                 where p.ro_id = o.id\n" );
			special.append("                   and p.is_use is null\n" );
			special.append("                   and p.is_gua = 0\n" );
			special.append("                   and p.pay_type = 11801002\n" );
			special.append("                   and p.repairtypecode = 93331001) as count_Part\n" );
			special.append("          from Tt_As_Repair_Order o\n" );
			special.append("         where 1 = 1\n" );
			special.append("           and o.ro_status = 11591002\n" );
			special.append("           and o.order_valuable_type = 13591001\n" );
			special.append(" and o.vin in (select vin from tm_vehicle where life_cycle=10321004)");//为实销
			special.append("   and o.dealer_id ="+dealerId);
			DaoFactory.getsql(special, "o.ro_no", DaoFactory.getParam(request, "ro_no"), 2);
			DaoFactory.getsql(special, "o.license", DaoFactory.getParam(request, "license"), 2);
			DaoFactory.getsql(special, "o.owner_name", DaoFactory.getParam(request, "owner_name"), 2);
			DaoFactory.getsql(special, "o.vin", DaoFactory.getParam(request, "vin"), 2);
			DaoFactory.getsql(special, "o.ro_create_date", DaoFactory.getParam(request, "beginTime"), 3);
			DaoFactory.getsql(special, "o.ro_create_date", DaoFactory.getParam(request, "endTime"), 4);
			DaoFactory.getsql(special, "o.create_date", MyConstant.onlineDate, 3);//加入时间新分单节点
			special.append("           and o.id  in (select p.ro_id\n" );
			special.append("                          from tt_as_ro_repair_part p\n" );
			special.append("                         where p.is_use is null\n" );
			special.append("                           and p.is_gua = 0 and p.part_camcode=0\n" );
			special.append("                           and p.pay_type = 11801002\n" );
			special.append("                           and p.repairtypecode = 93331001)\n" );
			special.append("   order by o.create_date desc) t where t.count_Part>0 \n" );
			list=pageQuery(special.toString(), null,getFunName(), pageSize, currPage);
		}else if("10661006".equals(claimType)){//服务活动的
			StringBuffer actitySql= new StringBuffer();
			actitySql.append("select t.* from (select o.*,(select count(p.ro_id)\n" );
			actitySql.append("          from tt_as_ro_repair_part p\n" );
			actitySql.append("         where p.ro_id = o.id and p.is_use is null and p.part_camcode =1\n" );
			actitySql.append("           and p.repairtypecode = '"+DaoFactory.getParam(request, "repairtypecode")+"') as count_Part ");
			actitySql.append("  from Tt_As_Repair_Order o\n" );
			actitySql.append(" where 1 = 1 and o.ro_status=11591002 \n" );
			actitySql.append("   and o.order_valuable_type = 13591001 ");
			actitySql.append("   and o.id in (select p.ro_id from tt_as_ro_repair_part p where p.is_use is null and p.part_camcode =1 and p.repairtypecode='"+DaoFactory.getParam(request, "repairtypecode")+"')\n" );
			actitySql.append("   and o.dealer_id ="+dealerId);
			DaoFactory.getsql(actitySql, "o.ro_no", DaoFactory.getParam(request, "ro_no"), 2);
			DaoFactory.getsql(actitySql, "o.license", DaoFactory.getParam(request, "license"), 2);
			DaoFactory.getsql(actitySql, "o.owner_name", DaoFactory.getParam(request, "owner_name"), 2);
			DaoFactory.getsql(actitySql, "o.vin", DaoFactory.getParam(request, "vin"), 2);
			DaoFactory.getsql(actitySql, "o.ro_create_date", DaoFactory.getParam(request, "beginTime"), 3);
			DaoFactory.getsql(actitySql, "o.ro_create_date", DaoFactory.getParam(request, "endTime"), 4);
			DaoFactory.getsql(actitySql, "o.create_date", MyConstant.onlineDate, 3);//加入时间新分单节点
			actitySql.append("   order by o.create_date desc) t where t.count_Part>0 \n" );
			list=pageQuery(actitySql.toString(), null,getFunName(), pageSize, currPage);
		}else if("10661007".equals(claimType)){//售前维修
			StringBuffer sb= new StringBuffer();
			sb.append("select t.* from (select o.*,(select count(p.ro_id)\n" );
			sb.append("          from tt_as_ro_repair_part p\n" );
			sb.append("         where p.ro_id = o.id and p.is_use is null \n" );
			sb.append("           and p.repairtypecode = '"+DaoFactory.getParam(request, "repairtypecode")+"') as count_Part ");
			sb.append("  from Tt_As_Repair_Order o\n" );
			sb.append(" where 1 = 1 and o.ro_status=11591002 \n" );
			sb.append("   and o.order_valuable_type = 13591001 ");
			sb.append(" and o.vin in (select vin from tm_vehicle where life_cycle<>10321004)");//不能为实销
			sb.append("   and o.vin  in (select a.vin from tt_as_wr_application  a where  a.claim_type=10661011 ) \n" );
			sb.append("   and o.id  in (select p.ro_id from tt_as_ro_repair_part p where  1=1 \n" );
			//不在已用的件和服务活动件上的正常维修上
			sb.append("   and (p.is_use is null  or  p.part_camcode=0) and p.repairtypecode='"+DaoFactory.getParam(request, "repairtypecode")+"')\n");
			sb.append("   and o.dealer_id ="+dealerId);
			DaoFactory.getsql(sb, "o.ro_no", DaoFactory.getParam(request, "ro_no"), 2);
			DaoFactory.getsql(sb, "o.license", DaoFactory.getParam(request, "license"), 2);
			DaoFactory.getsql(sb, "o.owner_name", DaoFactory.getParam(request, "owner_name"), 2);
			DaoFactory.getsql(sb, "o.vin", DaoFactory.getParam(request, "vin"), 2);
			DaoFactory.getsql(sb, "o.ro_create_date", DaoFactory.getParam(request, "beginTime"), 3);
			DaoFactory.getsql(sb, "o.ro_create_date", DaoFactory.getParam(request, "endTime"), 4);
			DaoFactory.getsql(sb, "o.create_date", MyConstant.onlineDate, 3);//加入时间新分单节点
			sb.append("   order by o.create_date desc) t where t.count_Part>0 \n" );
			list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		}else{//一般维修
			StringBuffer sb= new StringBuffer();
			sb.append("select t.* from (select o.*,(select count(p.ro_id)\n" );
			sb.append("          from tt_as_ro_repair_part p\n" );
			sb.append("         where p.ro_id = o.id and p.pay_type=11801002 and p.is_use is null \n" );
			sb.append("           and p.repairtypecode = '"+DaoFactory.getParam(request, "repairtypecode")+"') as count_Part ");
			sb.append("  from Tt_As_Repair_Order o\n" );
			sb.append(" where 1 = 1 and o.ro_status=11591002 \n" );
			sb.append("   and o.order_valuable_type = 13591001 ");
			sb.append(" and o.vin in (select vin from tm_vehicle where life_cycle=10321004)");//为实销
			sb.append("   and o.id  in (select p.ro_id from tt_as_ro_repair_part p where  1=1 \n" );
			//不在已用的件和服务活动件上的正常维修上
			sb.append("   and (p.is_use is null  or  p.part_camcode=0) and p.repairtypecode='"+DaoFactory.getParam(request, "repairtypecode")+"')\n");
			sb.append("   and o.dealer_id ="+dealerId);
			DaoFactory.getsql(sb, "o.ro_no", DaoFactory.getParam(request, "ro_no"), 2);
			DaoFactory.getsql(sb, "o.vin", DaoFactory.getParam(request, "vin"), 2);
			DaoFactory.getsql(sb, "o.ro_create_date", DaoFactory.getParam(request, "beginTime"), 3);
			DaoFactory.getsql(sb, "o.ro_create_date", DaoFactory.getParam(request, "endTime"), 4);
			DaoFactory.getsql(sb, "o.create_date", MyConstant.onlineDate, 3);//加入时间新分单节点
			sb.append("   order by o.create_date desc) t where t.count_Part>0 \n" );
			list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> addPartNormal(AclUserBean loginUser,RequestWrapper request, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select p.*,(select t.is_return from Tm_Pt_Part_Base t where t.part_code=p.part_no) as is_return from tt_as_ro_repair_part p where 1=1 and p.is_use is null "); 
		DaoFactory.getsql(sb, "p.real_part_id",DaoFactory.getParam(request, "part_id"),8);
		String claim_type = DaoFactory.getParam(request, "claim_type");
		if(claim_type.equals("10661001")||claim_type.equals("10661007")){//正常和售前的为索赔的件
			sb.append(" and (p.is_gua = 1 or p.is_gua = 0) and p.pay_type = 11801002 ");
		}
		if(claim_type.equals("10661010")||claim_type.equals("10661013")){//件的选择只能是系统判定是自费，服务站手工改为索赔的
			sb.append(" and (p.is_gua = 1 or p.is_gua = 0)  and p.pay_type = 11801002 "); 
		}
		sb.append(" and p.repairtypecode in (93331001,93331003) and  p.ro_id=(select o.id from tt_as_repair_order o where 1=1 "); 
		DaoFactory.getsql(sb, "o.ro_no",DaoFactory.getParam(request, "ro_no"),2);
		sb.append(")");
		DaoFactory.getsql(sb, "p.real_part_id",DaoFactory.getParam(request, "part_id"),8);
		DaoFactory.getsql(sb, "p.part_no",DaoFactory.getParam(request, "part_code"),2);
		DaoFactory.getsql(sb, "p.part_name",DaoFactory.getParam(request, "part_name"),2);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public int normalAddSure(AclUserBean loginUser, RequestWrapper request) {
		int res=1;
		String identify = DaoFactory.getParam(request, "identify");
		String id = DaoFactory.getParam(request, "id");
		String model_id = DaoFactory.getParam(request, "model_id");
		String series_id = DaoFactory.getParam(request, "series_id");
		String package_id = DaoFactory.getParam(request, "package_id");
		String wrgroup_id = DaoFactory.getParam(request, "wrgroup_id");
		String claim_no = DaoFactory.getParam(request, "claim_no");
		String vin = DaoFactory.getParam(request, "vin");
		String engine_no = DaoFactory.getParam(request, "engine_no");
		String ro_no = DaoFactory.getParam(request, "ro_no");
		String in_mileage = DaoFactory.getParam(request, "in_mileage");
		String color = DaoFactory.getParam(request, "color");
		String ysq_no = DaoFactory.getParam(request, "ysq_no");
		String guarantee_date = DaoFactory.getParam(request, "guarantee_date");
		String warning_level = DaoFactory.getParam(request, "warning_level");
		String claim_type = DaoFactory.getParam(request, "claim_type");
		String trouble_reason = DaoFactory.getParam(request, "trouble_reason");
		String trouble_desc = DaoFactory.getParam(request, "trouble_desc");
		String repair_method = DaoFactory.getParam(request, "repair_method");
		String submit_times = DaoFactory.getParam(request, "submit_times");
		String campaign_code = DaoFactory.getParam(request, "campaign_code");
		String model_Name = DaoFactory.getParam(request, "systemcar");//车型
		String freeRo = DaoFactory.getParam(request, "free_ro");
		//配件
		String[] part_ids = DaoFactory.getParams(request, "real_part_id");
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
		//辅料
		String[] workHourCodes = DaoFactory.getParams(request, "workHourCode");
		String[] workhour_names = DaoFactory.getParams(request, "workhour_name");
		String[] accessoriesPrices = DaoFactory.getParams(request, "accessoriesPrice");
		//补偿费
		String[] apply_amounts = DaoFactory.getParams(request, "apply_amount");
		String[] pass_amount = DaoFactory.getParams(request, "pass_amount");
		String[] remark = DaoFactory.getParams(request, "remark");
		String[] fjids=request.getParamValues("fjid");
		
		String outPerson = request.getParamValue("out_person");
		String outSite = request.getParamValue("out_site");
		String out_car = BaseUtils.CvParamsToStr(request.getParamValues("out_car"));
		String outLicenseno = request.getParamValue("out_licenseno");
		String fromAdress = request.getParamValue("from_adress");
		String endAdress = request.getParamValue("end_adress");
		String outMileage = request.getParamValue("out_mileage");
		String startTime = request.getParamValue("start_date");
		String endTime = request.getParamValue("end_date");
		
		String QT006_apply = DaoFactory.getParam(request, "QT006_apply");
		String QT007_apply = DaoFactory.getParam(request, "QT007_apply");
		String QT008_apply = DaoFactory.getParam(request, "QT008_apply");
		String QT009_apply = DaoFactory.getParam(request, "QT009_apply");
		String QT001_apply = DaoFactory.getParam(request, "QT001_apply");
		try {
			Long userId = loginUser.getUserId();
			Long pkId = DaoFactory.getPkId();
			if("".equals(id)){
				TtAsWrApplicationPO a =new TtAsWrApplicationPO();
				a.setId(pkId);
				String claimNoByClaimType = getClaimNoByClaimType(loginUser.getDealerId(),loginUser.getDealerCode());
				a.setClaimNo(claimNoByClaimType);
				a.setModelId(Long.valueOf(model_id));
				a.setSeriesId(Long.valueOf(series_id));
				a.setRoNo(ro_no);
				a.setVin(vin);
				a.setEngineNo(engine_no);
				a.setRepairMethod(repair_method);
				a.setTroubleDesc(trouble_desc);
				a.setTroubleReason(trouble_reason);
				a.setAppColor(color);
				a.setModelName(model_Name);//2016-01-18增加
				if(!"点击选择预授权".equals(ysq_no)){
					a.setYsqNo(ysq_no);
				}
				a.setPackageId(package_id);
				a.setWrgroupId(wrgroup_id);
				a.setWarningLevel(warning_level);
				a.setBalanceYieldly(Integer.valueOf(95411001));
				a.setYieldly("2010010100000001");
				Integer claimType = Integer.valueOf(claim_type);
				
				a.setClaimType(claimType);
				if(!"".equals(guarantee_date)){
					guarantee_date=guarantee_date.replaceAll("\\+", " ");
					a.setGuaranteeDate(BaseUtils.getDate(guarantee_date, 1));
				}
				a.setInMileage(Double.valueOf(in_mileage));
				a.setDealerId(Long.valueOf(loginUser.getDealerId()));
				a.setBalanceYieldly(95411001);
				a.setSubmitTimes(0);
				a.setCreateDate(new Date());
				a.setCreateBy(userId);
				a.setLineNo(Long.valueOf(1));
				TtAsRepairOrderPO ro=new TtAsRepairOrderPO();
				ro.setRoNo(ro_no);
				List<TtAsRepairOrderPO> roList = this.select(ro);
				ro = roList.get(0);
				a.setRoStartdate(ro.getRoCreateDate());
				a.setRoEnddate(ro.getForBalanceTime());
				if("0".equals(identify)){
					a.setStatus(10791001);
				}else{
					a.setStatus(10791003);
					a.setSubDate(new Date());
				}
				a.setCampaignCode(campaign_code);
				//============打标示在工单上
				if(!"".equals(freeRo)){
					this.markingRo(freeRo,claimNoByClaimType);
				}
				a.setFreeRo(freeRo);
				//=====================
				double partAmont=0.0d;
				Integer quotietys=0;
				//主因件
				String part_code_temp="";
				String part_name_temp ="";
				String producer_code_temp="";
				if(part_ids!=null && part_ids.length>0){
					for (int i=0; i<part_ids.length;i++ ) {
						TtAsWrPartsitemPO p=new TtAsWrPartsitemPO();
						p.setId(pkId);
						p.setPartId(DaoFactory.getPkId());
						p.setPartCode(part_codes[i]);
						p.setPartName(part_names[i]);
						p.setQuantity(Float.valueOf(part_quantitys[i]));
						p.setBalanceQuantity(Float.valueOf(part_quantitys[i]));
						p.setApplyQuantity(Double.valueOf(part_quantitys[i]));
						Integer quotiety = Integer.valueOf(part_quantitys[i]);
						p.setBalancePrice(Double.valueOf(part_cost_prices[i]));
						p.setApplyPrice(Double.valueOf(part_cost_prices[i]));
						p.setPrice(Double.valueOf(part_cost_prices[i]));
						Double part_Amont = Double.valueOf(part_cost_amounts[i]);
						p.setAmount(part_Amont);
						p.setBalanceAmount(part_Amont);
						p.setApplyAmount(part_Amont);
						p.setDownPartCode(old_part_codes[i]);
						Map<String,Object> map = this.pageQueryMap("select t.part_cname from tt_part_define t where t.part_oldcode='"+old_part_codes[i]+"'", null, getFunName());
						p.setDownPartName(BaseUtils.checkNull(map.get("PART_CNAME")));
						Integer responsibility = Integer.valueOf(responsibility_types[i]);
						
						if(responsibility==94001001){//主因件 只有一个
							 part_code_temp = part_codes[i];
							 part_name_temp = part_names[i];
							 producer_code_temp = producer_codes[i];
							 p.setWrLabourcode(part_code_temp);
							 p.setMainPartCode("-1");
						}
						p.setResponsibilityType(responsibility);
						p.setProducerCode(producer_codes[i]);
						p.setDownProductCode(producer_codes[i]);
						//========================//加入供应商信息 zyw 2015-5-26
						Map<String,Object> producer = this.pageQueryMap("select c.maker_name from tt_part_maker_define c where c.maker_code ='"+producer_codes[i]+"'", null, getFunName());
						p.setDownProductName(BaseUtils.checkNull(producer.get("MAKER_NAME")));
						p.setProducerName(BaseUtils.checkNull(producer.get("MAKER_NAME")));
						p.setRealPartId(Long.valueOf(part_ids[i]));
						p.setCreateDate(new Date());
						p.setCreateBy(userId);
						Integer paytype=null;
						if(claim_type.equals("10661013")){//配件索赔 自费 其他都索赔
							paytype=11801001;
						}else{
							paytype=11801002;
						}
						Integer is_return = Integer.valueOf(is_returns[i]);
						if(95361002==is_return){//95361002 0
							a.setIsOldAudit(0);//不回运的设置为0，结算JOB跑的时候判断 B.IS_RETURN = 95361002
						}else{ //95361001 1
							//a.setIsOldAudit(1);
							//--B.IS_RETURN = 95361001 and A.IS_OLD_AUDIT != 0 
						}
						p.setIsReturn(is_return);
						p.setPayType(paytype);
						Integer part_use_type = Integer.valueOf(part_use_types[i]);
						p.setPartUseType(part_use_type);
						partAmont+=part_Amont;
						quotietys+=quotiety;
						this.insert(p);
						StringBuffer sb= new StringBuffer();
						sb.append("update TT_AS_RO_REPAIR_PART p set p.is_use=1 where p.ro_id=\n" );
						sb.append("(select o.id from Tt_As_Repair_Order o where o.ro_no='"+ro_no+"'\n" );
						sb.append(")  and p.real_part_id='"+Long.valueOf(part_ids[i])+"'");
						this.update(sb.toString(), null);
					}
				}
				double labourAmont=0.0d;
				float labourHours=0.0f;
				if(labour_codes!=null && labour_codes.length>0){
					for (int i = 0; i < labour_codes.length; i++) {
						TtAsWrLabouritemPO l=new TtAsWrLabouritemPO();
						l.setLabourId(DaoFactory.getPkId());
						l.setId(pkId);
						l.setLabourCode(labour_codes[i]);
						if(null != labour_names[i])
							l.setLabourName(labour_names[i]);
						l.setWrLabourcode(labour_codes[i]);
						if(null != labour_names[i])
							l.setWrLabourname(labour_names[i]);
						Float labourHour = Float.valueOf(labour_quotietys[i]);
						l.setLabourQuantity(labourHour);
						l.setBalanceQuantity(labourHour);
						l.setLabourHours(labourHour);
						l.setApplyQuantity(Double.valueOf(labour_quotietys[i]));
						l.setApplyPrice(Double.valueOf(labour_prices[i]));
						l.setLabourPrice(Float.valueOf(labour_prices[i]));
						l.setBalancePrice(Double.valueOf(labour_prices[i]));
						l.setPayType(11801002);
						Double labouramont = Double.valueOf(labour_amounts[i]);
						l.setBalanceAmount(labouramont);
						l.setLabourAmount(labouramont);
						l.setApplyAmount(labouramont);
						//l.setCreateDate(new Date());
						l.setWrLabourcode(part_code_temp);
						l.setCreateBy(userId);
						labourAmont+=labouramont;
						labourHours+=labourHour;
						this.insert(l);
					}
				}
				Double accAmount=0.0d;
				if(BaseUtils.checkNull(workHourCodes)){
					for (int i = 0; i < workHourCodes.length; i++) {
						TtClaimAccessoryDtlPO acc=new TtClaimAccessoryDtlPO();
						acc.setId(DaoFactory.getPkId());
						acc.setWorkhourCode(workHourCodes[i]);
						acc.setWorkhourName(workhour_names[i]);
						Double amount = Double.valueOf(accessoriesPrices[i]);
						acc.setPrice(amount);
						acc.setAppPrice(amount);
						acc.setMainPartCode(part_code_temp);
						acc.setClaimNo(claimNoByClaimType);
						accAmount+=amount;
						this.insert(acc);
					}
				}
				double comAmont=0.0d;
				if(apply_amounts!=null && apply_amounts.length>0){
					for (int i = 0; i < apply_amounts.length; i++) {
						if(null !=apply_amounts[i] && !"".equals(apply_amounts[i]) && !"null".equals(apply_amounts[i])){
							TtAsWrCompensationAppPO t=new TtAsWrCompensationAppPO();
							t.setClaimNo(claimNoByClaimType);
							Double apply_amount = Double.valueOf(apply_amounts[i]);
							t.setApplyPrice(apply_amount);
							t.setPassPrice(Double.valueOf(pass_amount[i]));
							t.setRoNo(ro_no);
							t.setSupplierCode(producer_code_temp);
							t.setCreateDate(new Date());
							t.setPartCode(part_code_temp);
							t.setPartName(part_name_temp);
							t.setReason(remark[i]);
							t.setPkid(DaoFactory.getPkId());
							comAmont+=apply_amount;
							this.insert(t);
						}
					}
				}
				Float netItemAmount=0.0f;
				if((10661001==claimType|| 10661007==claimType)&&!"".equals(startTime)){//改变正常和售前的有这项
					if(!"".equals(out_car)){
					TtAsWrOutrepairPO tt=new TtAsWrOutrepairPO();
					tt.setCreateDate(new Date());
					tt.setCreateBy(userId);
					tt.setId(pkId);
					tt.setRoNo(ro_no);
					if(!"".equals(startTime)){
						startTime=startTime.replaceAll("\\+", " ");
						tt.setStartTime(Utility.getDate(startTime, 3));
					}
					if(!"".equals(endTime)){
						endTime=endTime.replaceAll("\\+", " ");
						tt.setEndTime(Utility.getDate(endTime, 3));
					}
					tt.setOutPerson(outPerson);
					tt.setOutSite(outSite);
					tt.setOutLicenseno(outLicenseno);
					tt.setFromAdress(fromAdress);
					tt.setEndAdress(endAdress);
					tt.setOutMileage(Utility.getDouble(outMileage));
					tt.setOutCar(out_car);
					
					this.insert(tt);
					TtAsWrOutrepairMoneyPO tw=new TtAsWrOutrepairMoneyPO();
					if(!"".equals(QT009_apply)){
						tw.setQt009Pass(Float.valueOf(QT009_apply));
						tw.setQt009Apply(Float.valueOf(QT009_apply));
						netItemAmount+=Float.valueOf(QT009_apply);
					}else{
						tw.setQt009Pass(0.0f);
						tw.setQt009Apply(0.0f);
					}
					tw.setOutId(pkId);
					tw.setId(DaoFactory.getPkId());
					if(!"".equals(QT001_apply)){
						tw.setQt001Apply(Float.valueOf(QT001_apply));
						tw.setQt001Pass(Float.valueOf(QT001_apply));
						netItemAmount+=Float.valueOf(QT001_apply);
					}else{
						tw.setQt001Apply(0.0f);
						tw.setQt001Pass(0.0f);
					}
					if(!"".equals(QT006_apply)){
						tw.setQt006Apply(Float.valueOf(QT006_apply));
						tw.setQt006Pass(Float.valueOf(QT006_apply));
						netItemAmount+=Float.valueOf(QT006_apply);
					}else{
						tw.setQt006Apply(0.0f);
						tw.setQt006Pass(0.0f);
					}
					if(!"".equals(QT007_apply)){
						tw.setQt007Apply(Float.valueOf(QT007_apply));
						tw.setQt007Pass(Float.valueOf(QT007_apply));
						netItemAmount+=Float.valueOf(QT007_apply);
					}else{
						tw.setQt007Apply(0.0f);
						tw.setQt007Pass(0.0f);
					}
					if(!"".equals(QT008_apply)){
						tw.setQt008Apply(Float.valueOf(QT008_apply));
						tw.setQt008Pass(Float.valueOf(QT008_apply));
						netItemAmount+=Float.valueOf(QT008_apply);
					}else{
						tw.setQt008Apply(0.0f);
						tw.setQt008Pass(0.0f);
					}
					this.insert(tw);
					}
				}
				double allAmount=0.0d;
				allAmount=Arith.add(allAmount, partAmont);
				allAmount=Arith.add(allAmount, labourAmont);
				allAmount=Arith.add(allAmount, accAmount);
				Double neitemAmount = Double.valueOf(netItemAmount);
				allAmount=Arith.add(allAmount, neitemAmount);
				allAmount=Arith.add(allAmount, comAmont);
				a.setNetitemAmount(neitemAmount);
				a.setBalanceNetitemAmount(neitemAmount);
				a.setBalanceAmount(allAmount);
				a.setBalanceLabourAmount(labourAmont);
				a.setBalancePartAmount(partAmont);
				a.setGrossCredit(allAmount);
				a.setLabourHours(labourHours);
				a.setPartsCount(quotietys);
				a.setRepairTotal(allAmount);
				a.setCompensationMoney(comAmont);
				a.setAccessoriesPrice(accAmount);
				this.insert(a);
				delAndReinsetFile(loginUser, fjids, pkId.toString());
			}else{
				this.delete("delete  from tt_as_wr_partsitem t where t.id="+DaoFactory.getParam(request, "id"), null);
				this.delete("delete  from tt_as_wr_labouritem t where t.id="+DaoFactory.getParam(request, "id"), null);
				this.delete("delete  from tt_as_wr_netitem t where t.id="+DaoFactory.getParam(request, "id"), null);
				this.delete("delete  from tt_claim_accessory_dtl d where d.claim_no=(select a.claim_no from tt_as_wr_application a where a.id="+DaoFactory.getParam(request, "id")+")", null);
				this.delete("delete  from tt_as_wr_compensation_app c where c.claim_no=(select a.claim_no from tt_as_wr_application a where a.id="+DaoFactory.getParam(request, "id")+")", null);
				this.delete("delete  from Tt_As_Wr_Outrepair t where t.id="+DaoFactory.getParam(request, "id"), null);
				this.delete("delete  from Tt_As_Wr_Outrepair_money t where t.out_id="+DaoFactory.getParam(request, "id"), null);
				TtAsWrApplicationPO a1 =new TtAsWrApplicationPO();
				Long pkIdUpate = Long.valueOf(id);
				a1.setId(pkIdUpate);
				
				TtAsWrApplicationPO a2 =new TtAsWrApplicationPO();
				//a2.setClaimNo(getClaimNoByClaimType(loginUser,claim_type));
				a2.setModelId(Long.valueOf(model_id));
				a2.setSeriesId(Long.valueOf(series_id));
				a2.setRoNo(ro_no);
				a2.setVin(vin);
				a2.setEngineNo(engine_no);
				a2.setRepairMethod(repair_method);
				a2.setTroubleDesc(trouble_desc);
				a2.setTroubleReason(trouble_reason);
				a2.setAppColor(color);
				a2.setYsqNo(ysq_no);
				a2.setPackageId(package_id);
				a2.setWrgroupId(wrgroup_id);
				a2.setWarningLevel(warning_level);
				a2.setBalanceYieldly(Integer.valueOf(95411001));
				a2.setYieldly("2010010100000001");
				Integer claimType = Integer.valueOf(claim_type);
				a2.setClaimType(claimType);
				if(!"".equals(guarantee_date)){
					guarantee_date=guarantee_date.replaceAll("\\+", " ");
					a2.setGuaranteeDate(BaseUtils.getDate(guarantee_date, 1));
				}
				TtAsRepairOrderPO ro=new TtAsRepairOrderPO();
				ro.setRoNo(ro_no);
				List<TtAsRepairOrderPO> roList = this.select(ro);
				ro = roList.get(0);
				a2.setRoStartdate(ro.getRoCreateDate());
				a2.setRoEnddate(ro.getForBalanceTime());
				a2.setInMileage(Double.valueOf(in_mileage));
				a2.setModelName(model_Name);//2016-01-18增加
				a2.setDealerId(Long.valueOf(loginUser.getDealerId()));
				a2.setSubmitTimes(Integer.valueOf(submit_times)+1);
				a2.setUpdateBy(userId);
				a2.setUpdateDate(new Date());
				a2.setLineNo(Long.valueOf(1));
				if("0".equals(identify)){
					a2.setStatus(10791001);
				}else{
					a2.setStatus(10791003);
					a2.setSubDate(new Date());
				}
				a2.setCampaignCode(campaign_code);
				//============打标示在工单上
				if(!"".equals(freeRo)){
					this.markingRo(freeRo,claim_no);
				}
				a2.setFreeRo(freeRo);
				//=====================
				double partAmont=0.0d;
				Integer quotietys=0;
				//主因件
				String part_code_temp="";
				String part_name_temp ="";
				String producer_code_temp="";
				if(part_ids!=null && part_ids.length>0){
					for (int i=0; i<part_ids.length;i++ ) {
						TtAsWrPartsitemPO p=new TtAsWrPartsitemPO();
						p.setId(pkIdUpate);
						p.setPartId(DaoFactory.getPkId());
						p.setPartCode(part_codes[i]);
						p.setPartName(part_names[i]);
						p.setQuantity(Float.valueOf(part_quantitys[i]));
						p.setBalanceQuantity(Float.valueOf(part_quantitys[i]));
						p.setApplyQuantity(Double.valueOf(part_quantitys[i]));
						Integer quotiety = Integer.valueOf(part_quantitys[i]);
						p.setBalancePrice(Double.valueOf(part_cost_prices[i]));
						p.setApplyPrice(Double.valueOf(part_cost_prices[i]));
						p.setPrice(Double.valueOf(part_cost_prices[i]));
						Double part_Amont = Double.valueOf(part_cost_amounts[i]);
						p.setAmount(part_Amont);
						p.setBalanceAmount(part_Amont);
						p.setApplyAmount(part_Amont);
						p.setDownPartCode(old_part_codes[i]);
						Map<String,Object> map = this.pageQueryMap("select t.part_cname from tt_part_define t where t.part_oldcode='"+old_part_codes[i]+"'", null, getFunName());
						p.setDownPartName(BaseUtils.checkNull(map.get("PART_CNAME")));
						Integer responsibility = Integer.valueOf(responsibility_types[i]);
						if(responsibility==94001001){//主因件 只有一个
							 part_code_temp = part_codes[i];
							 part_name_temp = part_names[i];
							 producer_code_temp = producer_codes[i];
							 p.setMainPartCode("-1");
						}
						p.setResponsibilityType(responsibility);
						p.setProducerCode(producer_codes[i]);
						p.setDownProductCode(producer_codes[i]);
						//========================//加入供应商信息 zyw 2015-5-26
						Map<String,Object> producer = this.pageQueryMap("select c.maker_name from tt_part_maker_define c where c.maker_code ='"+producer_codes[i]+"'", null, getFunName());
						p.setDownProductName(BaseUtils.checkNull(producer.get("MAKER_NAME")));
						p.setProducerName(BaseUtils.checkNull(producer.get("MAKER_NAME")));
						p.setRealPartId(Long.valueOf(part_ids[i]));
						p.setCreateDate(new Date());
						p.setCreateBy(userId);
						Integer paytype=null;
						if(claim_type.equals("10661013")){//配件索赔 自费 其他都索赔
							paytype=11801001;
						}else{
							paytype=11801002;
						}
						p.setIsReturn(Integer.valueOf(is_returns[i]));
						p.setPayType(paytype);
						Integer part_use_type = Integer.valueOf(part_use_types[i]);
						p.setPartUseType(part_use_type);
						partAmont+=part_Amont;
						quotietys+=quotiety;
						this.insert(p);
						StringBuffer sb= new StringBuffer();
						sb.append("update TT_AS_RO_REPAIR_PART p set p.is_use=1 where p.ro_id=\n" );
						sb.append("(select o.id from Tt_As_Repair_Order o where o.ro_no='"+ro_no+"'\n" );
						sb.append(")  and p.real_part_id='"+Long.valueOf(part_ids[i])+"'");
						this.update(sb.toString(), null);
					}
				}
				double labourAmont=0.0d;
				float labourHours=0.0f;
				if(labour_codes!=null && labour_codes.length>0){
					for (int i = 0; i < labour_codes.length; i++) {
						TtAsWrLabouritemPO l=new TtAsWrLabouritemPO();
						l.setLabourId(DaoFactory.getPkId());
						l.setId(pkIdUpate);
						l.setLabourCode(labour_codes[i]);
						if(null != labour_names[i])
							l.setLabourName(labour_names[i]);
						l.setWrLabourcode(labour_codes[i]);
						if(null != labour_names[i])
						l.setWrLabourname(labour_names[i]);
						Float labourHour = Float.valueOf(labour_quotietys[i]);
						l.setLabourQuantity(labourHour);
						l.setBalanceQuantity(labourHour);
						l.setLabourHours(labourHour);
						l.setApplyQuantity(Double.valueOf(labour_quotietys[i]));
						l.setApplyPrice(Double.valueOf(labour_prices[i]));
						l.setLabourPrice(Float.valueOf(labour_prices[i]));
						l.setBalancePrice(Double.valueOf(labour_prices[i]));
						l.setPayType(11801002);
						Double labouramont = Double.valueOf(labour_amounts[i]);
						l.setBalanceAmount(labouramont);
						l.setLabourAmount(labouramont);
						l.setApplyAmount(labouramont);
						l.setCreateDate(new Date());
						l.setCreateBy(userId);
						labourAmont+=labouramont;
						labourHours+=labourHour;
						this.insert(l);
					}
				}
				double comAmont=0.0d;
				if(apply_amounts!=null && apply_amounts.length>0){
					for (int i = 0; i < apply_amounts.length; i++) {
						TtAsWrCompensationAppPO t=new TtAsWrCompensationAppPO();
						t.setClaimNo(claim_no);
						Double apply_amount = Double.valueOf(apply_amounts[i]);
						t.setApplyPrice(apply_amount);
						t.setPassPrice(Double.valueOf(pass_amount[i]));
						t.setRoNo(ro_no);
						t.setSupplierCode(producer_code_temp);
						t.setCreateDate(new Date());
						t.setPartCode(part_code_temp);
						t.setPartName(part_name_temp);
						t.setReason(remark[i]);
						t.setPkid(DaoFactory.getPkId());
						comAmont+=apply_amount;
						this.insert(t);
					}
				}
				Double accAmount=0.0d;
				if(BaseUtils.checkNull(workHourCodes)){
					for (int i = 0; i < workHourCodes.length; i++) {
						TtClaimAccessoryDtlPO acc=new TtClaimAccessoryDtlPO();
						acc.setId(DaoFactory.getPkId());
						acc.setWorkhourCode(workHourCodes[i]);
						acc.setWorkhourName(workhour_names[i]);
						Double amount = Double.valueOf(accessoriesPrices[i]);
						acc.setPrice(amount);
						acc.setAppPrice(amount);
						acc.setMainPartCode(part_code_temp);
						acc.setClaimNo(claim_no);
						accAmount+=amount;
						this.insert(acc);
					}
				}
				Float netItemAmount=0.0f;
				if((10661001==claimType|| 10661007==claimType)&&!"".equals(startTime)){//改变正常和售前的有这项
					if(!"".equals(out_car)){
					TtAsWrOutrepairPO tt=new TtAsWrOutrepairPO();
					tt.setCreateDate(new Date());
					tt.setCreateBy(userId);
					tt.setId(pkIdUpate);
					tt.setRoNo(ro_no);
					if(!"".equals(startTime)){
						startTime=startTime.replaceAll("\\+", " ");
						tt.setStartTime(Utility.getDate(startTime, 3));
					}
					if(!"".equals(endTime)){
						endTime=endTime.replaceAll("\\+", " ");
						tt.setEndTime(Utility.getDate(endTime, 3));
					}
					tt.setOutPerson(outPerson);
					tt.setOutSite(outSite);
					tt.setOutLicenseno(outLicenseno);
					tt.setFromAdress(fromAdress);
					tt.setEndAdress(endAdress);
					tt.setOutMileage(Utility.getDouble(outMileage));
					tt.setOutCar(out_car);
					
					this.insert(tt);
					TtAsWrOutrepairMoneyPO tw=new TtAsWrOutrepairMoneyPO();
					if(!"".equals(QT009_apply)){
						tw.setQt009Pass(Float.valueOf(QT009_apply));
						tw.setQt009Apply(Float.valueOf(QT009_apply));
						netItemAmount+=Float.valueOf(QT009_apply);
					}else{
						tw.setQt009Pass(0.0f);
						tw.setQt009Apply(0.0f);
					}
					tw.setOutId(pkIdUpate);
					tw.setId(DaoFactory.getPkId());
					if(!"".equals(QT001_apply)){
						tw.setQt001Apply(Float.valueOf(QT001_apply));
						tw.setQt001Pass(Float.valueOf(QT001_apply));
						netItemAmount+=Float.valueOf(QT001_apply);
					}else{
						tw.setQt001Apply(0.0f);
						tw.setQt001Pass(0.0f);
					}
					if(!"".equals(QT006_apply)){
						tw.setQt006Apply(Float.valueOf(QT006_apply));
						tw.setQt006Pass(Float.valueOf(QT006_apply));
						netItemAmount+=Float.valueOf(QT006_apply);
					}else{
						tw.setQt006Apply(0.0f);
						tw.setQt006Pass(0.0f);
					}
					if(!"".equals(QT007_apply)){
						tw.setQt007Apply(Float.valueOf(QT007_apply));
						tw.setQt007Pass(Float.valueOf(QT007_apply));
						netItemAmount+=Float.valueOf(QT007_apply);
					}else{
						tw.setQt007Apply(0.0f);
						tw.setQt007Pass(0.0f);
					}
					if(!"".equals(QT008_apply)){
						tw.setQt008Apply(Float.valueOf(QT008_apply));
						tw.setQt008Pass(Float.valueOf(QT008_apply));
						netItemAmount+=Float.valueOf(QT008_apply);
					}else{
						tw.setQt008Apply(0.0f);
						tw.setQt008Pass(0.0f);
					}
					this.insert(tw);
					}
				}
				double allAmount=0.0d;
				allAmount=Arith.add(allAmount, partAmont);
				allAmount=Arith.add(allAmount, labourAmont);
				allAmount=Arith.add(allAmount, comAmont);
				allAmount=Arith.add(allAmount, accAmount);
				Double neitemAmount = Double.valueOf(netItemAmount);
				allAmount=Arith.add(allAmount, neitemAmount);
				a2.setNetitemAmount(neitemAmount);
				a2.setBalanceNetitemAmount(neitemAmount);
				a2.setBalanceAmount(allAmount);
				a2.setBalanceLabourAmount(labourAmont);
				a2.setBalancePartAmount(partAmont);
				a2.setGrossCredit(allAmount);
				a2.setLabourHours(labourHours);
				a2.setPartsCount(quotietys);
				a2.setRepairTotal(allAmount);
				a2.setCompensationMoney(comAmont);
				a2.setAccessoriesPrice(accAmount);
				this.update(a1,a2);
				delAndReinsetFile(loginUser, fjids, pkIdUpate.toString());
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}
	/**
	 * 打标识先删除工单的标示，再标示上去
	 * @param freeRo
	 * @param claim_no 
	 */
	@SuppressWarnings("unchecked")
	private void markingRo(String freeRo, String claim_no) {
		try {
			this.update("update tt_as_repair_order o set o.free_ro=null where o.ro_no='"+freeRo+"'",null);
			this.update("update tt_as_repair_order o set o.free_ro='"+claim_no+"' where o.ro_no='"+freeRo+"'", null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public String getClaimNoByClaimType(String dealerId, String dealerCode) {
		String claimNo="";
        StringBuilder sbSql = new StringBuilder();
        /*sql.append("SELECT '"+dealerCode+"' || TO_CHAR(SYSDATE, 'yymmdd') ||\n");
        sql.append("       LPAD(NVL(MAX(SUBSTR(T.CLAIM_NO, -3)), 0) + 1, 3, 0) as SEQ\n");
        sql.append("  FROM TT_AS_WR_APPLICATION T\n");
        sql.append(" WHERE T.DEALER_ID = '"+dealerId+"'\n");
        sql.append("   AND TRUNC(T.CREATE_DATE) = TRUNC(SYSDATE)\n");*/
        
      //增加废弃索赔单判断逻辑+除去保养和PDI单据类型的索赔单号规则
        sbSql.append("SELECT '"+dealerCode+"' || TO_CHAR(SYSDATE, 'yymmdd') ||\n");
        sbSql.append("       LPAD(NVL(MAX(SUBSTR(T.CLAIM_NO, -3)), 0) + 1, 3, 0) as SEQ\n");
        sbSql.append("  FROM (select claim_no\n");
        sbSql.append("          FROM TT_AS_WR_APPLICATION A\n");
        sbSql.append("         WHERE A.DEALER_ID = '"+dealerId+"'\n");
        sbSql.append("           AND TRUNC(A.CREATE_DATE) = TRUNC(SYSDATE)\n");
        sbSql.append("           AND A.claim_type not in (10661011, 10661002)\n");
        sbSql.append("        union\n");
        sbSql.append("        select claim_no\n");
        sbSql.append("          FROM TT_AS_WR_APPLICATION_BACKUP B\n");
        sbSql.append("         WHERE B.DEALER_ID = '"+dealerId+"'\n");
        sbSql.append("           AND TRUNC(B.CREATE_DATE) = TRUNC(SYSDATE)\n");
        sbSql.append("           AND B.claim_type not in (10661011, 10661002)) T"); 

        Map<String,Object> map = this.pageQueryMap(sbSql.toString(), null, getFunName());
		claimNo = map.get("SEQ").toString();
		return claimNo;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> supplierCodeByPartCode(AclUserBean loginUser, RequestWrapper request, Integer pageSize, Integer currPage) {
		String not_show = "902306,902309,902311";
		String partcode = request.getParamValue("partcode");
		StringBuffer sb= new StringBuffer();
		sb.append("select c.maker_id       as maker_id,\n" );
		sb.append("       c.maker_code     maker_code,\n" );
		sb.append("       c.maker_shotname maker_shotname\n" );
		sb.append("  from tt_part_maker_define c\n" );
		sb.append(" where 1 = 1\n" );
		sb.append("   and (c.maker_id in\n" );
		sb.append("       (select t.maker_id\n" );
		sb.append("           from tt_part_maker_relation t\n" );
		sb.append("          where t.part_id in\n" );
		sb.append("                (select pb.part_id\n" );
		sb.append("                   from tm_pt_part_base pb\n" );
		sb.append("                  where pb.part_code in ('"+partcode+"',\n" );
		sb.append("                                         '"+partcode.substring(0, partcode.length()-3)+"000',\n" );
		sb.append("                                         '"+partcode.substring(0, partcode.length()-3)+"B0Y',\n" );
		sb.append("                                         '"+partcode.substring(0, partcode.length()-3)+"B00'))) or\n" );
		sb.append("       c.MAKER_ID IN  (select c.maker_id\n" );
		sb.append("                        from tt_part_maker_define c\n" );
		sb.append("                      where c.maker_code='902307' ))");
		DaoFactory.getsql(sb, "c.maker_code", DaoFactory.getParam(request, "maker_code"), 2);
		DaoFactory.getsql(sb, "c.maker_shotname", DaoFactory.getParam(request, "maker_shotname"), 2);
		//dealerId为空时，是整车厂;不为空时,是经销商
		if (!"".equals(loginUser.getDealerId())) {
			DaoFactory.getsql(sb, "c.maker_code", not_show, 8);
		}
		sb.append(" order by c.maker_code ");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> findoutrepairmoney(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.* from Tt_As_Wr_Outrepair_money t where 1=1 and t.out_id='"+DaoFactory.getParam(request, "id")+"'");
		return pageQueryMap(sb.toString(), null, getFunName());
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> findoutrepair(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.* from Tt_As_Wr_Outrepair t where 1=1 and t.id='"+DaoFactory.getParam(request, "id")+"'");
		return pageQueryMap(sb.toString(), null, getFunName());
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> findComPoById(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("select c.* from tt_as_wr_compensation_app c where c.claim_no=(select a.claim_no from tt_as_wr_application a where a.id="+DaoFactory.getParam(request, "id")+")");
		return pageQueryMap(sb.toString(), null, getFunName());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findAccPoById(RequestWrapper request) {
		return pageQuery("select d.* from tt_claim_accessory_dtl d where d.claim_no=(select a.claim_no from tt_as_wr_application a where a.id="+DaoFactory.getParam(request, "id")+")", null,getFunName());
	}
	

	@SuppressWarnings("unchecked")
	public int normalAudit(AclUserBean loginUser, RequestWrapper request) {
		int res=1;
		String id = DaoFactory.getParam(request,"id");
		String pass_amount = DaoFactory.getParam(request,"pass_amount");//补偿费
		String oldStatus = DaoFactory.getParam(request,"status");
		String auditRemark = DaoFactory.getParam(request,"audit_remark");
		String identify = DaoFactory.getParam(request,"identify");
		String auditAmount = DaoFactory.getParam(request,"auditAmount")==""?"0.00":DaoFactory.getParam(request,"auditAmount");//审核金额
		String qt006Pass = DaoFactory.getParam(request,"qt006Pass")==""?"0.00":DaoFactory.getParam(request,"qt006Pass");
		String qt007Pass = DaoFactory.getParam(request,"qt007Pass")==""?"0.00":DaoFactory.getParam(request,"qt007Pass");
		String qt008Pass = DaoFactory.getParam(request,"qt008Pass")==""?"0.00":DaoFactory.getParam(request,"qt008Pass");
		String qt009Pass = DaoFactory.getParam(request,"qt009Pass")==""?"0.00":DaoFactory.getParam(request,"qt009Pass");
		String qt001Pass = DaoFactory.getParam(request,"qt001Pass")==""?"0.00":DaoFactory.getParam(request,"qt001Pass");
		String troubledesc = DaoFactory.getParam(request,"trouble_desc"); //原因分析及处理结果
		String troublereason = DaoFactory.getParam(request,"trouble_reason");//故障现象
		troubledesc = StringEscapeUtils.escapeSql(troubledesc);//防止sql注入
		troublereason = StringEscapeUtils.escapeSql(troublereason);
		String status = getStatus(identify);
		String[]  workHourCode = DaoFactory.getParams(request, "workHourCode");//辅料代码
		String[]  accessoriesID = DaoFactory.getParams(request, "accessoriesID");//辅料id
		String[]  accessoriesPrice = DaoFactory.getParams(request, "accessoriesPrice");//单个辅料结算金额
		String[]  labour_amount = DaoFactory.getParams(request, "labour_amount");//单个索赔结算工时金额
		String[]  labour_code = DaoFactory.getParams(request, "labour_code");//单个索赔结算工时代码
		String  balance_labour_amount = DaoFactory.getParam(request, "balance_labour_amount");//索赔结算工时总金额
		String  accessoriesPriceSum = DaoFactory.getParam(request, "accessoriesPriceSum");//索赔结算辅料总金额
		try {
			//=======================修改工时
			if (labour_code!=null && labour_code.length>0) {//遍历修改工时代码
				for (int i = 0; i < labour_code.length; i++) {
					String	str=" update tt_as_wr_labouritem l set l.balance_amount="+labour_amount[i]+" where l.id="+DaoFactory.getParam(request, "id")+" and l.labour_code='"+labour_code[i]+"'";
					this.update(str, null);
				}
				this.update("update tt_as_wr_application a set a.balance_labour_amount= "+balance_labour_amount+" where a.id="+DaoFactory.getParam(request, "id"), null);//修改工时结算金额
			}
			//======================修改辅料金额
			if (workHourCode!=null && workHourCode.length>0) {//遍历修改辅料代码
				for (int i = 0; i < workHourCode.length; i++) {
					String	str=" update tt_claim_accessory_dtl l set l.PRICE="+accessoriesPrice[i]+" where l.id="+accessoriesID[i]+" and l.workhour_code='"+workHourCode[i]+"'";
					this.update(str, null);
				}
				this.update("update tt_as_wr_application a set a.accessories_price= "+accessoriesPriceSum+" where a.id="+DaoFactory.getParam(request, "id"), null);//修改辅料结算金额
			}
			if(!"".equals(pass_amount)){
				this.update("update tt_as_wr_compensation_app c set c.pass_price="+pass_amount+" where c.claim_no=(select a.claim_no from tt_as_wr_application a where a.id="+DaoFactory.getParam(request, "id")+")", null);
				this.update("update tt_as_wr_application a set a.status="+status+",a.audit_opinion='"+auditRemark+"',a.balance_amount=(a.balance_amount-nvl(a.compensation_money,0)+nvl(decode("+pass_amount+",'',0,"+pass_amount+"),0))  where a.id="+DaoFactory.getParam(request, "id")+"", null);
				this.update("update tt_as_wr_application a set a.balance_amount = "+auditAmount+", a.compensation_money= "+pass_amount+"  where a.id="+DaoFactory.getParam(request, "id")+"", null);
			}else{
				this.update("update tt_as_wr_application a set a.status="+status+", a.balance_amount = "+auditAmount+", a.trouble_reason = '"+troublereason+"', a.trouble_desc = '"+troubledesc+"',a.audit_opinion='"+auditRemark+"' where a.id="+DaoFactory.getParam(request, "id")+"", null);
			}
			
		    List<Map<String, Object>> list= this.pageQuery("select * from TT_AS_WR_OUTREPAIR_MONEY d where d.out_id in (select t.id from tt_as_wr_application t where t.id="+DaoFactory.getParam(request, "id")+")", null, getFunName());
			if(DaoFactory.checkListNull(list)){
				//更新外派费用审核金额
				StringBuffer sql = new StringBuffer();
				sql.append(" UPDATE  TT_AS_WR_OUTREPAIR_MONEY  T2 \n");
				sql.append(" SET \n");
				sql.append(" T2.QT006_PASS  =  "+qt006Pass+", \n");
				sql.append(" T2.QT007_PASS  =  "+qt007Pass+", \n");
				sql.append(" T2.QT008_PASS  =  "+qt008Pass+", \n");
				sql.append(" T2.QT009_PASS  =  "+qt009Pass+", \n");
				sql.append(" T2.QT001_PASS  =  "+qt001Pass+" \n");
				sql.append(" WHERE \n");
				sql.append(" T2.OUT_ID  =  "+id+" \n");
				this.update(sql.toString(),null);
				Double OUTREPAIR_MONEY = Double.valueOf(qt006Pass)+Double.valueOf(qt007Pass)+Double.valueOf(qt008Pass)+Double.valueOf(qt009Pass)+Double.valueOf(qt001Pass);//外出费用结算到索赔单中
				this.update("update tt_as_wr_application a set  a.balance_netitem_amount = "+OUTREPAIR_MONEY+" where a.id="+DaoFactory.getParam(request, "id")+"", null);
			}
			oldUpdateIdea(id,loginUser);//审核通过的情况下新增标签
			//*************2016-03-28新增
//			if ( Constant.CLAIM_APPLY_ORD_TYPE_02.toString().equals(status)) {//如果为撤销审核时，那么遍历删除标签表中已经生成的标签
//				deleteBarcode(id);
//			}
			//****************
			this.ChangeStatusPassByid(id, auditRemark,loginUser);//修改审核备注
			insertClaimRecord(loginUser,auditRemark,status,id);//修改为审核的状态
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}
     /**
      * 根据索赔单的id删除标签表中的配件生成的标签
      * @param id
      */
	@SuppressWarnings("unchecked")
	private void deleteBarcode(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select p.*\n");
		sql.append("  from tt_as_wr_partsitem p\n");
		sql.append(" where 1=1 ");
		DaoFactory.getsql(sql, "p.id", id, 1);
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, getFunName());
		if (list != null && list.size() > 0) {
			StringBuffer sq = new StringBuffer();
			sq.append("delete from\n");
			sq.append("    Tt_As_Wr_Partsitem_Barcode  b where b.part_id='" + list.get(0).get("PART_ID").toString()+ "'");
		}
	}

	/**
	 * 修改审核状态和审核备注
	 * @param id
	 * @param auditOpinion
	 * @param loginUser 
	 */
	@SuppressWarnings("unchecked")
	private void ChangeStatusPassByid(String id, String auditOpinion, AclUserBean loginUser) {
		this.update(strSql+" t.audit_opinion='"+auditOpinion+"',report_date=sysdate,update_Date=sysdate,update_by='"+loginUser.getUserId()+"' where t.id="+id, null);
	}

	private String getStatus(String identify) {
		String status = "";
		if("0".equals(identify)){
			//审核通过
			status = Constant.CLAIM_APPLY_ORD_TYPE_08.toString();
			
		}else if("1".equals(identify)){
			//审核退回
			status = Constant.CLAIM_APPLY_ORD_TYPE_06.toString();
			
		}else if("2".equals(identify)){
			//撤销审核
			status = Constant.CLAIM_APPLY_ORD_TYPE_02.toString();
		}else if("3".equals(identify)){
			//审核拒绝
			status = Constant.CLAIM_APPLY_ORD_TYPE_05.toString();
		}
		return status;
	}

	@SuppressWarnings("unchecked")
	public void insertClaimRecord(AclUserBean logonUser,String auditRemark,String status,String id) throws Exception{
		/**
		 * 记录审核记录（专门针对外出维修的进行结算金额修改等操作）
		 */
		TtAsWrAppAuditDetailPO dp = new TtAsWrAppAuditDetailPO();
		dp.setId(DaoFactory.getPkId());
		dp.setAuditBy(logonUser.getUserId());
		dp.setAuditDate(new Date());
		dp.setAuditRemark(auditRemark);
		dp.setAuditResult(Integer.parseInt(status));
		dp.setClaimId(Long.parseLong(id));
		dp.setCreateBy(logonUser.getUserId());
		dp.setCreateDate(new Date());
		dp.setAuditType(2);//整单审核
		this.insert(dp);
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
					if(list1==null||list1.size()==0){//没有标签的才添加
						this.insertBarcode(partId, yieldly, Long.valueOf(dealer),xuHao,dealerCode);
					}
				}
			  }
			}
		}
	}
	 /******
     * 根据索赔单ID 查询出明细ID与数量
     * @param claimId
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String,Object>> Partsitem(String claimId){
    	StringBuffer sql= new StringBuffer();
    	sql.append("select b.yieldly,a.part_id,a.quantity,D.dealer_id,D.DEALER_CODE From Tt_As_Wr_Partsitem a,TT_AS_WR_APPLICATION b ,TM_PT_PART_BASE P,TM_DEALER D where b.id=? AND B.DEALER_ID = D.DEALER_ID and a.id=b.id AND A.DOWN_PART_CODE NOT IN('NO_PARTS','00-000','00-0000') and a.AUDIT_STATUS="+Constant.PART_AUDIT_STATUS_03+" AND A.DOWN_PART_CODE = P.PART_CODE   and a.part_use_type = 1 and  a.quantity>0  AND a.IS_RETURN = "+Constant.IS_RETURN_01 );
    	
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
    	Long barcodeId = DaoFactory.getPkId();
		tpb.setBarcodeId(barcodeId);
    	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date  date = new Date();
		String str = sf.format(date);
    	String s1 = str.substring(2,4);
    	String s2 = str.substring(5,7);
    	String qz= code+s1+s2;//5位服务站代码+年+月= 5+2+2 = 9位
    	 System.out.println("qz:"+qz);
    	 qz = getQz(qz);
    	 String sql="insert into tt_as_wr_partsitem_barcode  (create_by,create_date, part_id, barcode_id, barcode_no,serial_number) values  ("+dealerId+",sysdate, "+partId+", "+barcodeId+",'"+qz+"','"+xuHao+"')";
    	 this.insert(sql);
    }
    @SuppressWarnings("unchecked")
	public String getQz(String code){
    	String sql="select  '"+code+"'||seq_b.nextval  NUM  from dual ";
		List<Map<String,Object>> ps=this.pageQuery(sql.toString(), null, this.getFunName());
		return ps.get(0).get("NUM").toString();
    }

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> findYsqBaseInfo(AclUserBean loginUser, RequestWrapper request, Integer pageSize,Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select a.*, v.engine_no\n" );
		sb.append("  from Tt_As_Wr_Ysq a, tm_vehicle v\n" );
		sb.append(" where a.vin = v.vin\n" );
		sb.append("   and a.is_end = -1\n" );
		sb.append("   and a.status<>93461010\n" );//不能为拒赔的预授权
		sb.append("   and a.ysq_no not in\n" );
		sb.append("       (select ysq_no from Tt_As_Wr_Application where ysq_no is not null)\n" );
		sb.append("   and a.part_id in");
		//============================================通用判断：选预授权的时候进行判断是否在工单号里面有这个件，
		//-----------通过part_id检查
		sb.append("       (select p.real_part_id\n" );
		sb.append("          from tt_as_ro_repair_part p\n" );
		sb.append("         where p.ro_id in\n" );
		sb.append("               (select o.id from Tt_As_Repair_Order o where o.ro_no = '"+DaoFactory.getParam(request, "ro_no")+"') and p.is_use is null)");
		//-----------通过part_code检查   
		/*** 同时满足part_id 和part_code 避免服务站在预授权时改变了配件代码，而从索赔单带过来的工单配件的part_id没有改变*/
		sb.append("   and a.part_code in");
		sb.append("       (select p.part_no\n" );
		sb.append("          from tt_as_ro_repair_part p\n" );
		sb.append("         where p.ro_id in\n" );
		sb.append("               (select o.id from Tt_As_Repair_Order o where o.ro_no = '"+DaoFactory.getParam(request, "ro_no")+"') and p.is_use is null)");
		//============================================
		sb.append("   and a.dealer_id ="+loginUser.getDealerId());
		DaoFactory.getsql(sb, "a.ysq_no", DaoFactory.getParam(request, "ysq_no"), 2);
		DaoFactory.getsql(sb, "a.vin", DaoFactory.getParam(request, "vin"), 2);
		DaoFactory.getsql(sb, "a.vin", DaoFactory.getParam(request, "VIN"), 2);//工单上信息的Vin
		DaoFactory.getsql(sb, "a.claim_type", changeClaimCodeToRoCode(DaoFactory.getParam(request, "claim_type")), 1);
		DaoFactory.getsql(sb, "a.create_date", DaoFactory.getParam(request, "beginTime"), 3);
		DaoFactory.getsql(sb, "a.create_date", DaoFactory.getParam(request, "endTime"), 4);
		DaoFactory.getsql(sb, "a.claim_type", DaoFactory.getParam(request, "claim_type"), 1);//lj2015-5-28 限制预授权类型
		sb.append("  order by a.create_date desc \n" );
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryYsqDataById(AclUserBean loginUser,RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("select a.*,pt.*, v.engine_no from Tt_As_Wr_Ysq a , tm_vehicle v, ");
		sb.append("tt_as_ysq_part pt");
		sb.append(" where  a.vin=v.vin  and a.is_end = -1 ");
		sb.append(" and a.part_id =pt.part_id(+)  ");
		sb.append("   and a.dealer_id ="+loginUser.getDealerId());
		DaoFactory.getsql(sb, "a.id", DaoFactory.getParam(request, "id"), 1);
		DaoFactory.getsql(sb, "a.claim_type", DaoFactory.getParam(request, "claim_type"), 1);
		return pageQueryMap(sb.toString(), null, getFunName());
	}
	 /**
	  * 转换类型 工单和索赔单
	  * @param repairType
	  * @return
	  */
	public String changeRoCodeToClaimCode(String repairType) {
		String claimType="";
		if(repairType.equals(Constant.REPAIR_TYPE_01)){
			claimType=String.valueOf(Constant.CLA_TYPE_01);
		}
		if(repairType.equals(Constant.REPAIR_TYPE_02)){
			claimType=String.valueOf(Constant.CLA_TYPE_09);
		}
		if(repairType.equals(Constant.REPAIR_TYPE_03)){
			claimType=String.valueOf(Constant.CLA_TYPE_07);
		}
		if(repairType.equals(Constant.REPAIR_TYPE_04)){
			claimType=String.valueOf(Constant.CLA_TYPE_02);
		}
		if(repairType.equals(Constant.REPAIR_TYPE_05)){
			claimType=String.valueOf(Constant.CLA_TYPE_06);
		}
		if(repairType.equals(Constant.REPAIR_TYPE_06)){
			claimType=String.valueOf(Constant.CLA_TYPE_10);
		}
		if(repairType.equals(Constant.REPAIR_TYPE_07)){
			claimType=String.valueOf(Constant.CLA_TYPE_12);
		}
		if(repairType.equals(Constant.REPAIR_TYPE_08)){
			claimType=String.valueOf(Constant.CLA_TYPE_11);
		}
		if(repairType.equals(Constant.REPAIR_TYPE_09)){
			claimType=String.valueOf(Constant.CLA_TYPE_13);
		}
		return claimType;
	}
	/**
	  * 转换类型索赔单 >>工单
	  * @param repairType
	  * @return
	  */
	public String changeClaimCodeToRoCode(String claimType) {
		String repairType="";
		if(claimType.equals(Constant.CLA_TYPE_01)){
			repairType=String.valueOf(Constant.REPAIR_TYPE_01);
		}
		if(claimType.equals(Constant.CLA_TYPE_09)){
			repairType=String.valueOf(Constant.REPAIR_TYPE_02);
		}
		if(claimType.equals(Constant.CLA_TYPE_07)){
			repairType=String.valueOf(Constant.REPAIR_TYPE_03);
		}
		if(claimType.equals(Constant.CLA_TYPE_02)){
			repairType=String.valueOf(Constant.REPAIR_TYPE_04);
		}
		if(claimType.equals(Constant.CLA_TYPE_06)){
			repairType=String.valueOf(Constant.REPAIR_TYPE_05);
		}
		if(claimType.equals(Constant.CLA_TYPE_10)){
			repairType=String.valueOf(Constant.REPAIR_TYPE_06);
		}
		if(claimType.equals(Constant.CLA_TYPE_12)){
			repairType=String.valueOf(Constant.REPAIR_TYPE_07);
		}
		if(claimType.equals(Constant.CLA_TYPE_11)){
			repairType=String.valueOf(Constant.REPAIR_TYPE_08);
		}
		if(claimType.equals(Constant.CLA_TYPE_13)){
			repairType=String.valueOf(Constant.REPAIR_TYPE_09);
		}
		return repairType;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> ysqPartdataById(AclUserBean loginUser,RequestWrapper request) {
		Map<String, Object> queryYsqDataById = this.queryYsqDataById(loginUser, request);
		String part_id = queryYsqDataById.get("PART_ID").toString();
		
		StringBuffer sb= new StringBuffer();
		sb.append("select  a.*,\n" );
		sb.append("       case\n" );
		sb.append("         when A.IS_DEL = 0 and s.is_del = 0 AND S.PART_CODE IS NOT NULL THEN\n" );
		sb.append("          10041001\n" );
		sb.append("         ELSe\n" );
		sb.append("          10041002\n" );
		sb.append("       end fore,\n" );
		sb.append("       case\n" );
		sb.append("         when A.IS_DEL = 0 and s.is_del = 0 AND S.PART_CODE IS NOT NULL THEN\n" );
		sb.append("          s.approval_level\n" );
		sb.append("         ELSe\n" );
		sb.append("          null\n" );
		sb.append("       end part_Level,\n" );
		sb.append("       CASE\n" );
		sb.append("         WHEN (trunc(sysdate) >= sp.valid_date and sp.history_flag = 1) or\n" );
		sb.append("              sp.history_flag = 0 THEN\n" );
		sb.append("          round(nvl(sp.sale_price1, 0) *\n" );
		sb.append("                (1 + nvl(m.parameter_value, 0) / 100),\n" );
		sb.append("                2)\n" );
		sb.append("         ELSE\n" );
		sb.append("          round(nvl(decode(sp.history_price,\n" );
		sb.append("                           null,\n" );
		sb.append("                           sp.sale_price1,\n" );
		sb.append("                           sp.history_price),\n" );
		sb.append("                    0) * (1 + nvl(m.parameter_value, 0) / 100),\n" );
		sb.append("                2)\n" );
		sb.append("       END claim_Price_param\n" );
		sb.append("  from TM_PT_PART_BASE a\n" );
		sb.append("  left join tt_part_sales_price sp\n" );
		sb.append("    on sp.part_id = a.part_id\n" );
		sb.append("  LEFT JOIN TT_AS_WR_AUTHMONITORPART s\n" );
		sb.append("    ON s.part_code = a.part_code\n" );
		sb.append("   and a.is_del = 0, tm_down_parameter m, TT_AS_RELATION_PART_SERIES ps\n" );
		sb.append(" where 1 = 1\n" );
		sb.append("   and m.dealer_id = "+loginUser.getDealerId()+"\n" );
		sb.append("   and m.parameter_code = 10421009\n" );
		sb.append("   AND A.Part_Is_Changhe = '95411001'\n" );
		sb.append("   and ps.part_id = a.part_id\n" );
		DaoFactory.getsql(sb, "a.part_id", part_id, 1);
		return pageQueryMap(sb.toString(), null, getFunName());
	}

	private OrderService orderservice=new OrderServiceImpl();
	
	@SuppressWarnings("unchecked")
	public String checkClaim(RequestWrapper request, AclUserBean loginUser) {
		String res="";
		String vin = DaoFactory.getParam(request, "vin");
		String in_mileage = DaoFactory.getParam(request, "in_mileage");
		String guarantee_date = DaoFactory.getParam(request, "guarantee_date");
		//配件
		String[] part_ids = DaoFactory.getParams(request, "real_part_id");
		String[] part_codes = DaoFactory.getParams(request, "part_code");
		String[] responsibility_types = DaoFactory.getParams(request, "responsibility_type");
		String claim_type = DaoFactory.getParam(request, "claim_type");
		if("10661001".equals(claim_type)){//正常维修才判断
			if(part_ids!=null && part_ids.length>0){
				for (int i=0; i<part_ids.length;i++ ) {
					//判断是否三包 true 索赔 false 自费
					Map<String, Object> guaFlag = orderservice.getGuaFlag(part_codes[i], vin, in_mileage, guarantee_date);
					String object = guaFlag.get("flag").toString();
					if(!"true".equals(object)){//自费 做索赔单默认为索赔
						String responsibility_type = responsibility_types[i];
						if("94001001".equals(responsibility_type)){//并且选择了主因件
							res+=part_codes[i]+" 因为工单选择配件是自费后被改为索赔的自费工单,只能被选择为次因件. ";
						}
					}
				}
			}
		}
		
		if("10661007".equals(claim_type)){//售前维修才判断
			StringBuffer sb= new StringBuffer();
			sb.append("select a.dealer_id from Tt_As_Wr_Application a where a.claim_type=10661011 ");
			DaoFactory.getsql(sb, "a.vin", vin, 1);
			List<Map<String, Object>> list=this.pageQuery(sb.toString(), null, this.getFunName());
			if(DaoFactory.checkListNull(list)){
				Map<String, Object> map = list.get(0);
				BigDecimal object = (BigDecimal) map.get("DEALER_ID");
				Long dealerId = object.longValue();
				if(!dealerId.toString().equals(loginUser.getDealerId())){
					StringBuffer sql = new StringBuffer();
					sql.append("select * from TT_SERVICE_DEALER_BIND bin where bin.bind_flag="+dealerId);
					sql.append(" and status =10041001 ");
					List<Map<String, Object>> map2 = this.pageQuery(sql.toString(), null, getFunName());
					if(DaoFactory.checkListNull(map2)){
						BigDecimal BIND_DEALER_ID = (BigDecimal) map.get("BIND_DEALER_ID");
						Long dealerId1 = BIND_DEALER_ID.longValue();
						if (!dealerId1.equals(loginUser.getDealerId())) {
							res+=" 售前只能由让PDI的服务站才维修 ";
						}
					}
				}
			}
		}
		//====================================暂时去掉工时与配件的关系
		/*if("".equals(res)){//上面判断没有问题再判断下面的检查
			String[] labour_codes = DaoFactory.getParams(request, "labour_code");//判断如果工时选择了是否在对应配件关系里都选择了
			if(part_ids!=null && part_ids.length>0){
				for (int i=0; i<part_ids.length;i++ ) {
					res+=this.checkLabBypartId(part_ids[i],part_codes[i],labour_codes);
				}
			}
		}*/
		//====================================库存验证
		
	/*	String dealerId = loginUser.getDealerId();
		if("".equals(res)){//上面判断没有问题再判断下面的检查
			if(part_ids!=null && part_ids.length>0){
				for (int i=0; i<part_ids.length;i++ ) {
					 res += this.checkStoreBypartCodeAndDealerid(part_codes[i],Long.parseLong(dealerId));
				}
			}
		}*/
		
		return res;
	}

	/**
	 * 库存验证
	 * @param partid
	 * @param dealerid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String checkStoreBypartCodeAndDealerid(String part_code,Long dealerid) {
		String res="";
		StringBuffer sb= new StringBuffer();
		sb.append("select c.* from vw_part_dlr_stock_check c where c.DEALER_ID="+dealerid+" and c.PART_CODE='"+part_code+"'");
		List<Map<String,Object>> list=pageQuery(sb.toString(), null,getFunName());
		if(DaoFactory.checkListNull(list)){
			Map<String, Object> map = list.get(0);
			BigDecimal b=(BigDecimal) map.get("KY_QTY");
			if(b.intValue()<=0){
				res+="库存不足，请检查或维护！";
			}
		}else{
			res+="库存不足，请检查或维护！";
		}
		return res;
	}
	@SuppressWarnings({ "unchecked", "unused" })
	private String checkLabBypartId(String part_id,String part_code, String[] labour_codes) {
		String str="";
		StringBuffer sb= new StringBuffer();
		sb.append("select t.part_code\n" );
		sb.append("  from tt_AS_lab_part_relation t\n" );
		sb.append(" where 1=1 and t.part_id ='"+part_id+"'\n" );
		DaoFactory.getsql(sb, "t.lab_code", DaoFactory.getSqlByarrIn(labour_codes), 6);
		List<Map<String, Object>> list = this.pageQuery(sb.toString(), null, getFunName());
		if(!DaoFactory.checkListNull(list)){//如果没有这样的关系
			str+=" ["+part_code+"没有选择对应维护关系的工时] ";
		}
		return str;
	}

	@SuppressWarnings("unchecked")
	public String pdiAddCheck(String vin, RequestWrapper request,AclUserBean loginUser)  {
		String res="";
		if("".equals(vin)){
			return "接收参数失败，请刷新再试！！！";
		}
		String dealer_id = loginUser.getDealerId();
		TmVehiclePO tmVehiclePO1 = new TmVehiclePO();
		tmVehiclePO1.setVin(vin);
		tmVehiclePO1.setLifeCycle(10321003);
		List<TmVehiclePO > listpo =  this.select(tmVehiclePO1);
		if (listpo==null || listpo.size()==0) {
			return "该车没有在经销商库存中，不能做PDI!!";
		}
		if(listpo.size()>1){
			return "VIN查出的车辆有"+listpo.size()+"辆!请联系系统管理员核实数据!";
		}
		TtDealerRelationPO relationPO = new TtDealerRelationPO();
		relationPO.setShDealerId(Long.valueOf(dealer_id));
		List<TtDealerRelationPO> l = this.select(relationPO);
		int temp=0;
		if (DaoFactory.checkListNull(l)) {
			if (null!=l && l.size()>=0) {
				for (int i = 0; i < l.size(); i++) {
					TtDealerRelationPO ps =  (TtDealerRelationPO) l.get(i);//获取关联表
					TmVehiclePO tmVehiclePO = new TmVehiclePO();
					tmVehiclePO.setVin(vin);
					tmVehiclePO.setDealerId(ps.getXsDealerId());
					List<TmVehiclePO> li= this.select(tmVehiclePO);
					if (null!=li && li.size()>0) {
						temp++;
					}
				}
			}
		}
		// 2015.12.21  判断网销的车以收车的经销商绑定的售后服务站做PDI  开始
		// 通过车辆接收dealer查询服务站,如果存在服务站与当前服务站一致,则算作可以做PDI
		TmVehiclePO vehicle = listpo.get(0);
		Long sxDealer = vehicle.getRecDealerId();
		TtDealerRelationPO rp = new TtDealerRelationPO();
		rp.setXsDealerId(sxDealer);
		List<TtDealerRelationPO> rplist = select(rp);
		if(!CommonUtils.isNullList(rplist)){
			for (TtDealerRelationPO dr : rplist) {
				if (Long.parseLong(dealer_id)==dr.getShDealerId()) {
					temp++;
				}
			}
		}
		// 2015.12.21  判断网销的车以收车的经销商绑定的售后服务站做PDI  结束
		if (temp<=0) {
			return "服务站只能做对应经销商的PDI!";
		}
		StringBuffer sb= new StringBuffer();
		sb.append("select t.* from tt_AS_wr_application t where t.claim_type=10661011 and t.status is not null  and  t.status not in (10791005,10791006,10791001)\n" );
		DaoFactory.getsql(sb, "t.vin", vin, 1);
		List<Map<String, Object>> list = pageQuery(sb.toString(), null, getFunName());
		if(DaoFactory.checkListNull(list)){
			res+="该VIN已经做过PDI了";
		}
		if("".equals(res)){
			StringBuffer sb1= new StringBuffer();
			sb1.append("select t.* from tt_AS_wr_application t where t.claim_type=10661007 and t.status<>10791005  \n" );
			DaoFactory.getsql(sb1, "t.vin", vin, 1);
			List<Map<String, Object>> list1 = pageQuery(sb1.toString(), null, getFunName());
			if(DaoFactory.checkListNull(list1)){
				res+="做了售前索赔的不能做pdi工单";
			}
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public int normalReportSubmit(String id) {
		int res=1;
		try {
			StringBuffer sb= new StringBuffer();
			sb.append("update tt_AS_wr_application t set t.status=10791003,t.sub_date=sysdate  where  t.id='"+id+"' \n" );
			this.update(sb.toString(), null);
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}

	/**
	 * 索赔单废弃查询
	 * @param loginUser
	 * @param request
	 * @param pageSize
	 * @param currPage
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> ClaimAbandonedQuery(AclUserBean loginUser, RequestWrapper request, Integer pageSize,Integer currPage) {
		        StringBuffer sb= new StringBuffer();
				sb.append("select a.id,a.claim_type,\n" );
				sb.append("       a.claim_no,\n" );
				sb.append("       a.balance_yieldly,\n" );
				sb.append("       a.vin,\n" );
				sb.append("       a.create_date,\n" );
				sb.append("       a.status,\n" );
				sb.append("       a.submit_times\n" );
				sb.append("  from tt_as_wr_application_backup a\n" );
				sb.append(" where a.claim_type not in(10661011,10661002) ");
				DaoFactory.getsql(sb, "a.claim_no", DaoFactory.getParam(request, "claim_no"), 2);
				DaoFactory.getsql(sb, "a.create_date", DaoFactory.getParam(request, "beginTime"), 3);
				DaoFactory.getsql(sb, "a.create_date", DaoFactory.getParam(request, "endTime"), 4);
				DaoFactory.getsql(sb, "a.status", DaoFactory.getParam(request, "status"), 1);
				DaoFactory.getsql(sb, "a.vin", DaoFactory.getParam(request, "vin"), 6);
				DaoFactory.getsql(sb, "a.claim_type", DaoFactory.getParam(request, "claim_type"), 1);
				DaoFactory.getsql(sb, "a.dealer_id", loginUser.getDealerId(), 1);
				sb.append("   order by a.create_date desc \n" );
				PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
				return list;
	}

	//外出人明细明细
	 @SuppressWarnings("unchecked")
	public List<TtAsWrOutrepairPO> getOutDetail(String id){
	    	StringBuffer sql= new StringBuffer();
	    	sql.append("select t.* from tt_as_wr_outrepair t,tt_as_wr_application a\n");
	    	sql.append("where a.ro_no = t.ro_no\n");
	    	sql.append("and a.id="+id); 
	    	return this.select(TtAsWrOutrepairPO.class,sql.toString(), null);
	    }

	public List<TtAsWrNetitemPO> getOther(String id) {
		return null;
	}

	public List<TtAsActivityPO> getADetail(String id) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> barcodePrintDoGet(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT distinct ba.area_name,\n" );
		sb.append("                case M.Is_Cliam\n" );
		sb.append("                  when 10011002 then\n" );
		sb.append("                   '是'\n" );
		sb.append("                  else\n" );
		sb.append("                   ('否')\n" );
		sb.append("                end as Is_Cliam,a.in_mileage,\n" ); 
		sb.append("                d.model_code,\n" );
		sb.append("                G.SERIAL_NUMBER,\n" );
		sb.append("                a.remark,\n" );
		sb.append("                a.trouble_reason as f_reason,\n" );
		sb.append("                F.producer_code,\n" );
		sb.append("                P.DELIVERER,\n" );
		sb.append("                P.DELIVERER_PHONE,\n" );
		sb.append("                F.DOWN_PRODUCT_CODE DC_NAME,\n" );
		sb.append("                F.DOWN_PART_CODE,\n" );
		sb.append("                f.down_part_name,\n" );
		sb.append("                to_char(A.AUDITING_DATE, 'yyyy-MM-dd') AUDITING_DATES,\n" );
		sb.append("                a.trouble_reason as f_reason,\n" );
		sb.append("                a.trouble_desc as F_REMARK,\n" );
		sb.append("                a.claim_no,\n" );
		sb.append("                a.vin,\n" );
		sb.append("                a.engine_no,\n" );
		sb.append("                a.model_code,\n" );
		sb.append("                b.dealer_shortname,\n" );
		sb.append("                F.PRODUCER_NAME YIELDLY_NAME,\n" );
		sb.append("                B.DEALER_CODE,\n" );
		sb.append("                B.DEALER_SHORTNAME DEALER_NAME,\n" );
		sb.append("                E.CODE_DESC CLAIM_NAME,\n" );
		sb.append("                D.PACKAGE_CODE PACKAGE_NAME,\n" );
		sb.append(" (case  a.is_import  when 10041001 then to_char(a.guarantee_date , 'yy-MM-dd')\n" );
		sb.append("                 when 10041002\n" );
		sb.append("                then to_char(C.PURCHASED_DATE, 'yy-MM-dd') end ) as PURCHASED_DATE,\n");
		sb.append("                G.BARCODE_NO,\n" );
		sb.append("                ba.area_name || '轿车' as JDJW,\n" );
		sb.append("                TO_CHAR(C.PRODUCT_DATE, 'YY-MM-DD') PRODUCT_DATE,\n" );
		sb.append("                TO_CHAR(p.ro_create_date, 'YY-MM-DD') RO_CREATE_DATE,\n" );
		sb.append("                b.is_dqv,\n" );
		sb.append("                b.phone,\n" );
		sb.append("                C.LICENSE_NO,\n" );
		sb.append("                m.is_return AS is_return1\n" );
		sb.append("  FROM TT_AS_WR_APPLICATION       A,\n" );
		sb.append("       TM_DEALER                  B,\n" );
		sb.append("       TM_VEHICLE                 C,\n" );
		sb.append("       vw_material_group          d,\n" );
		sb.append("       TC_CODE                    E,\n" );
		sb.append("       Tt_As_Wr_Partsitem         F,\n" );
		sb.append("       Tt_As_Wr_Partsitem_Barcode G,\n" );
		sb.append("       TT_AS_REPAIR_ORDER         P,\n" );
		sb.append("       TM_PT_PART_BASE            M,\n" );
		sb.append("       tm_business_area           ba\n" );
		sb.append(" WHERE 1 = 1\n" );
		sb.append("   AND ba.area_id(+) = A.YIELDLY\n" );
		sb.append("   AND f.DOWN_PART_CODE not in\n" );
		sb.append("       ('00-000', 'CV6000-00000', 'CV8000-00000', 'CV11000-00000')\n" );
		sb.append("   and F.PART_CODE = M.PART_CODE(+)\n" );
		sb.append("   AND A.RO_NO = P.RO_NO(+)\n" );
		sb.append("   AND F.ID = A.ID\n" );
		sb.append("   AND F.PART_ID = G.PART_ID\n" );
		sb.append("   AND A.DEALER_ID = B.DEALER_ID(+)\n" );
		sb.append("   AND A.VIN = C.VIN(+)\n" );
		sb.append("   AND C.PACKAGE_ID = D.PACKAGE_ID\n" );
		sb.append("   AND A.CLAIM_TYPE = E.CODE_ID(+)\n" );
		sb.append("   and F.QUANTITY > 0\n" );
		sb.append("   AND A.ID in ("+DaoFactory.getParam(request, "dtlIds")+")\n" );
		sb.append(" order by G.BARCODE_NO");

		 return this.pageQuery(sb.toString(), null, this.getFunName());
	}

	@SuppressWarnings("unchecked")
	public List<TtAsWrApplicationPO> selectttAsWrApplication(TtAsWrApplicationPO tawaPo) {
		return this.select(tawaPo);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getclaimAccessoryDtl(String claimNo) {
		StringBuffer sql = new StringBuffer("\n");
		sql.append(" select * from tt_claim_accessory_dtl where claim_NO='");
		sql.append(claimNo).append("'");
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
	}

	@SuppressWarnings("unchecked")
	public List<TtAsActivityProjectPO> selectTtAsActivityProject(TtAsActivityProjectPO jp) {
		return this.select(jp);
	}

	@SuppressWarnings("unchecked")
	public List<TtAsWrApplicationExtPO> getDetail(String id) {
		StringBuffer sql= new StringBuffer();
    	sql.append("select p.part_code,p.part_name,p.down_part_code down_code,p.down_part_name down_name,p.quantity, l.wr_labourcode labour_code,\n");
    	sql.append("l.wr_labourname labour_name,p.remark,p.amount,l.labour_hours,l.labour_amount\n");
    	sql.append("from  tt_as_wr_application a left join  tt_as_wr_partsitem p on a.id=p.id left join  tt_as_wr_labouritem l on  a.id = l.id\n");
    	sql.append("where 1=1\n");
    	sql.append("and p.id="+id); 
    	return this.select(TtAsWrApplicationExtPO.class,sql.toString(), null);
	}

	@SuppressWarnings("unchecked")
	public List<TtAsWrApplicationExtPO> getBaseBean(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select  v.product_date, vw.model_code,tmd.dealer_name,a.balance_Yieldly,g2.group_name series_name, a.claim_type,a.dealer_name, a.dealer_shortname,a.claim_no,to_char(o.ro_create_date,'yyyy-mm-dd hh24:mi') ro_date,d.dealer_code,\n");
		sql.append(" a.labour_hours,a.parts_count,a.repair_total, a.report_date,");
		sql.append("  (select uu.name  from  tc_user uu where uu.user_id= (select aa.create_by from tt_as_wr_application aa  where  aa.id="+id+")) as service_Advisor,\n");
		sql.append("a.model_code,to_char(v.purchased_date,'yyyy-mm-dd ') buy_date,v.vin,v.engine_no,v.license_no,\n");
		sql.append("decode(o.deliverer_phone,null,o.deliverer_mobile,o.deliverer_phone) deliver_phone,o.deliverer ,o.service_advisor ,\n");
		sql.append("tc.main_phone customer_phone,tc.ctm_name customer_name,to_char(v.factory_date,'yyyy-mm-dd ') out_date,\n");
		sql.append("to_char(v.product_date,'yyyy-mm-dd hh24:mi') ,to_char(o.for_balance_time,'yyyy-mm-dd hh24:mi') banlance_date,\n");
		sql.append("o.in_mileage,a.trouble_desc  miao_shu,a.trouble_reason reason,o.repair_method,o.remarks,f.opinion,u.name audit_name,\n");
		sql.append("ba.area_name,m.material_code,g.group_code,to_char(f.update_date,'yyyy-mm-dd hh24:mi') agree_date,tc.address,a.labour_hours,a.labour_amount,\n");
		sql.append(" a.part_amount,a.gross_credit, o.free_times,a.appendlabour_num,a.appendlabour_amount\n");
		sql.append(" from  tt_as_wr_application a left join tt_as_repair_order o  on a.ro_no = o.ro_no\n");
		sql.append("   left join tm_dealer tmd  on tmd.dealer_id= a.dealer_id\n");
		sql.append("left join tt_as_wr_foreapproval f on f.ro_no=o.ro_no\n");
		sql.append("left join tc_user u on u.user_id = f.audit_person, tm_dealer d ,\n");
		sql.append("tm_vehicle v left join  tt_dealer_actual_sales sa on sa.vehicle_id = v.vehicle_id  and sa.is_return="+Constant.IF_TYPE_NO+"\n");
		sql.append("left join vw_material_group vw on v.PACKAGE_ID = vw.PACKAGE_ID \n");
		sql.append("left join tt_customer tc on tc.ctm_id = sa.ctm_id \n");
		sql.append("left join tm_business_area ba on ba.area_id = v.yieldly\n");
		sql.append("left join tm_vhcl_material m on m.material_id = v.material_id\n");
		sql.append("left join tm_vhcl_material_group g on g.group_id = v.package_id\n");
		sql.append("left join tm_vhcl_material_group g2 on g2.group_id = v.series_id\n"); 
		sql.append("where 1=1\n");
		sql.append("and a.vin = v.vin and nvl(a.second_dealer_id,a.dealer_id) = d.dealer_id\n");
		sql.append("and a.id="+id); 
		return this.select(TtAsWrApplicationExtPO.class, sql.toString(), null);
	}

	@SuppressWarnings("unchecked")
	public String getTimes(String vin) {
		StringBuffer sb = new StringBuffer();
		sb.append("select max(p.cur_times) num  from tt_as_wr_vin_part_repair_times p where p.vin='"+vin+"'");
		List<TtAsWrApplicationExtPO> ps=this.select(TtAsWrApplicationExtPO.class,sb.toString(), null);
		System.out.println(ps.size());
		if(ps!=null&&ps.size()>0){
			if(ps.get(0).getNum()==null){
				return "0";
			}
			return ps.get(0).getNum();
		}
		return "0";
	}

	@SuppressWarnings("unchecked")
	public List<TtAsWrApplicationExtPO> getbaoy(String vin) {
		StringBuffer sql = new StringBuffer();
		sql.append("select mg.free num ,mg.part_price,mg.labour_price FROM TT_AS_WR_MODEL_ITEM mi, tm_vehicle v ,tt_as_wr_model_group mg\n");
		sql.append("where v.package_id = mi.model_id\n");
		sql.append("and mg.wrgroup_id = mi.wrgroup_id\n");
		sql.append("and v.vin='"+vin+"'  AND MG.WRGROUP_TYPE=10451001"); 
		return this.select(TtAsWrApplicationExtPO.class,sql.toString(), null);
	}

	@SuppressWarnings("unchecked")
	public void barcodePrintDoGet(String dtlIds) {
		  StringBuffer sql = new StringBuffer("\n");
		   sql.append("update TT_AS_WR_APPLICATION set is_print  ="+Constant.IF_TYPE_YES+" where id in  ("+dtlIds+") ");
		   this.update(sql.toString(), null);
	}

	@SuppressWarnings("unchecked")
	public int updateprodutercode(RequestWrapper request) {
		StringBuffer sb = new StringBuffer();
		sb.append("update Tt_As_Wr_Partsitem tt\n");
		sb.append("    set tt.producer_code ='"+DaoFactory.getParam(request, "produtercode")+"'\n");
		sb.append(" where 1=1     ");
		DaoFactory.getsql(sb, "tt.id", DaoFactory.getParam(request, "id"), 1);
		DaoFactory.getsql(sb, "tt.down_part_code", DaoFactory.getParam(request, "partcode"), 1);
		return this.update(sb.toString(), null);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> showAllRecords(String id) {
		StringBuffer sb= new StringBuffer();
		sb.append("select d.audit_date,\n" );
		sb.append("       c.name as user_Name,\n" );
		sb.append("       d.audit_remark,\n" );
		sb.append("       (select tc.code_desc\n" );
		sb.append("          from tc_code tc\n" );
		sb.append("         where tc.code_id = d.audit_result) as audit_result\n" );
		sb.append("  from tt_as_wr_app_audit_detail d, tc_user c\n" );
		sb.append(" where d.audit_by = c.user_id\n");
		DaoFactory.getsql(sb, "d.claim_id", id, 1);
		return this.pageQuery(sb.toString(), null, getFunName());
	}
	@SuppressWarnings("unchecked")  
	public Map<String, Object> findamount(AclUserBean loginUser, RequestWrapper request) {
		Map<String, Object> map=null;
		//pdi
		String type =DaoFactory.getParam(request, "type");
		if ("pdi".equals(type)) {
			StringBuffer sb= new StringBuffer();
			sb.append("select sum(a.balance_amount) as sumamount\n" );
			sb.append("  from Tt_As_Wr_Application a,Tt_As_Wr_Netitem n\n" );
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
			sb.append("  from Tt_As_Wr_Application a\n" );
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
			sb.append("  from Tt_As_Wr_Application a\n" );
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
			sb.append(" and a.claim_no not in (select v.claim_no from tt_as_wr_repair_claim v where v.dealer_id='"+loginUser.getDealerId()+"')");
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
			sb.append("          from tt_as_wr_application a, tm_dealer b,vw_material_group_mat vm, tm_vehicle tv\n" );
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

	@SuppressWarnings("unchecked")
	public int checkpartCode(AclUserBean loginUser, RequestWrapper request) {
		int res = -1;
		StringBuffer sql= new StringBuffer();
		sql.append("select *\n" );
		sql.append("  from tt_as_ysq_part p\n" );
		sql.append(" where 1 = 1\n" );
		sql.append("  and p.part_type="+Constant.YSQ_PART_TYPE_02 );
		DaoFactory.getsql(sql,"p.part_code", DaoFactory.getParam(request,"arr"), 6);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(),null,getFunName());
		if (null!=list &&list.size()>0) {
			res=1;
			
		}else {
			res=-1;
		}
		return res;
	}
    //修改旧件代码
	@SuppressWarnings("unchecked")
	public int updatenewpartcode(AclUserBean loginUser, RequestWrapper request) {
		TtAsWrPartsitemPO partsitemPO = new TtAsWrPartsitemPO();
		TtAsWrPartsitemPO partsitemPO1 = new TtAsWrPartsitemPO();
		partsitemPO.setDownPartCode(DaoFactory.getParam(request, "newpartcode"));
		partsitemPO1.setDownPartCode(DaoFactory.getParam(request, "oldpartcode"));
		partsitemPO.setId(Long.valueOf(DaoFactory.getParam(request, "id")));
		return this.update(partsitemPO, partsitemPO1);
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> viewbackaduitList(AclUserBean loginUser, RequestWrapper request, Integer pageSize,Integer currPage) {
		StringBuffer sql= new StringBuffer();
		sql.append("\n" );
		sql.append("select d.*, tc1.code_desc audit_types, tc2.name \n" );
		sql.append("  from Tt_As_Wr_App_Audit_Detail d, tc_code tc1, tc_user tc2\n" );
		sql.append(" where 1 = 1\n" );
		sql.append("  and d.audit_result=10791006 \n" );
		sql.append("   and d.audit_result = tc1.code_id(+)\n" );
		sql.append("   and  d.audit_by=tc2.user_id(+)");

		DaoFactory.getsql(sql, "d.claim_id", DaoFactory.getParam(request,"bizId"), 1);
		return this.pageQuery(sql.toString(), null, getFunName(), pageSize,currPage );
	}

	

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findCarsystemById(RequestWrapper request) {
		StringBuffer sql= new StringBuffer();
		sql.append("select vw.*\n" );
		sql.append("  from tt_as_wr_application a, tm_vehicle v, vw_material_group_service vw\n" );
		sql.append(" where a.vin = v.vin\n" );
		sql.append("   and v.package_id = vw.PACKAGE_ID");
        DaoFactory.getsql(sql, "a.id", DaoFactory.getParam(request, "id"), 1);
        List<Map<String, Object>> list =this.pageQuery(sql.toString(), null, getFunName());
		return list;
	}

	@SuppressWarnings("unchecked")
	public int updateIsReturnById(AclUserBean loginUser, RequestWrapper request) {
		TtAsComRecordPO comRecordPO = new TtAsComRecordPO();
		String is_return = DaoFactory.getParam(request,"is_return");
		String id = DaoFactory.getParam(request, "id");
		comRecordPO.setId(DaoFactory.getPkId());
		comRecordPO.setBizId(Long.valueOf(id));
		comRecordPO.setCreateBy(loginUser.getUserId());
		comRecordPO.setCreateDate(new Date());
		comRecordPO.setStatus(Integer.valueOf(is_return));
		this.insert(comRecordPO);//记录日志
		
		StringBuffer sb =new StringBuffer();
		sb.append("update tt_as_wr_partsitem p set p.is_return='"+is_return+"' where 1=1 and p.part_id='"+id+"'");
		return this.update(sb.toString(), null);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findCarsystemByvin(AclUserBean loginUser,RequestWrapper request) {
		StringBuffer sql= new StringBuffer();
		sql.append("select vw.*\n" );
		sql.append("  from tm_vehicle v, vw_material_group_service vw\n" );
		sql.append(" where 1=1\n" );
		sql.append("   and v.package_id = vw.PACKAGE_ID");
        DaoFactory.getsql(sql, "v.vin", DaoFactory.getParam(request, "vin"), 1);
        List<Map<String, Object>> list =this.pageQuery(sql.toString(), null, getFunName());
		return list;
	}


	/**
	 * 验证vin的车辆是否已上报pdi
	 * @param vin
	 * @return
	 * @author chenyub@yonyou.com
	 */
	public boolean pdiExistCheck(String vin, RequestWrapper request,AclUserBean loginUser){
		boolean flag = true;
		String pdiCheck = pdiAddCheck(vin,request,loginUser);
		if(pdiCheck==null ||"".equals(pdiCheck)){
			flag = false;
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> findYsqPoById(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("select ysq.* from tt_as_wr_ysq ysq where ysq.ysq_no=(select a.ysq_no from tt_as_wr_application a where a.id="+DaoFactory.getParam(request, "id")+")");
		return pageQueryMap(sb.toString(), null, getFunName());
	}
	
}
