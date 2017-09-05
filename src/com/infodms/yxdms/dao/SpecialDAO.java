package com.infodms.yxdms.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.yxdms.entity.special.TtAsWrSpeChangeCarPO;
import com.infodms.yxdms.entity.special.TtAsWrSpeGoodwillClaimPO;
import com.infodms.yxdms.entity.special.TtAsWrSpecialApplyPO;
import com.infodms.yxdms.entity.special.TtAsWrSpecialPO;
import com.infodms.yxdms.entity.special.TtAsWrSpecialRecordPO;
import com.infodms.yxdms.entity.ysq.TtAsComRecordPO;
import com.infodms.yxdms.utils.BaseUtils;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("rawtypes")
public class SpecialDAO extends IBaseDao{

	private static SpecialDAO dao = new SpecialDAO();
	public static final SpecialDAO getInstance(){
		dao = (dao==null)?new SpecialDAO():dao;
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> specialDealerList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*,vw.root_org_name,tm.dealer_shortname\n" );
		sb.append("  from tt_as_wr_Special t, tm_dealer tm, vw_data_by_vin v,vw_org_dealer_service vw\n" );
		sb.append(" where tm.dealer_id = t.dealer_id and t.dealer_id= vw.dealer_id\n" );
		sb.append("   and v.vin = t.vin \n");
		DaoFactory.getsql(sb, "t.dealer_id", loginUser.getDealerId(), 1);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public String getSpecialNo() {
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT 'TS'|| TO_CHAR(SYSDATE,'YYMM')|| LPAD(SEQ_SPECIAL_NO.NEXTVAL,4,'0') AS SPECIAL_NO FROM DUAL");
		Map<String,Object> map=this.pageQueryMap(sb.toString(), null, this.getFunName());
		return map.get("SPECIAL_NO").toString();
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> qureyAllClaim(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select a.id,a.claim_no,a.vin,a.ro_no,a.claim_type  from tt_as_wr_application a where 1=1 and  a.claim_type not in (10661011,10661006)\n");
		DaoFactory.getsql(sb, "a.dealer_id", loginUser.getDealerId(), 1);
		DaoFactory.getsql(sb, "a.claim_no", DaoFactory.getParam(request, "claim_no"), 2);
		DaoFactory.getsql(sb, "a.vin",DaoFactory.getParam(request, "vin"), 2);

		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> findDataByVin(String vin) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.* from vw_data_by_vin t where 1=1\n");
		DaoFactory.getsql(sb, "t.vin",vin, 1);
		Map<String, Object> map = this.pageQueryMap(sb.toString(), null, getFunName());
		return map;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> queryPartCode(RequestWrapper request, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.part_code,t.part_name From tm_pt_part_base t where 1=1\n");
		DaoFactory.getsql(sb, "t.part_code",DaoFactory.getParam(request, "part_code"), 2);
		DaoFactory.getsql(sb, "t.part_name",DaoFactory.getParam(request, "part_name"), 2);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> querySupplierCode(RequestWrapper request, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.maker_code,t.maker_shotname From tt_part_maker_define t where 1=1\n");
		DaoFactory.getsql(sb, "t.maker_code",DaoFactory.getParam(request, "maker_code"), 2);
		DaoFactory.getsql(sb, "t.maker_shotname",DaoFactory.getParam(request, "maker_shotname"), 2);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public int saveOrUpdate(RequestWrapper request, AclUserBean loginUser) {
		String spe_id = DaoFactory.getParam(request, "spe_id");
		String is_claim = DaoFactory.getParam(request, "is_claim");
		String part_code_dealer = DaoFactory.getParam(request, "part_code_dealer");
		String supply_code_dealer = DaoFactory.getParam(request, "supply_code_dealer");
		String claim_no = DaoFactory.getParam(request, "claim_no");
		String vin = DaoFactory.getParam(request, "vin");
		String mileage = DaoFactory.getParam(request, "mileage");
		String problem_date = DaoFactory.getParam(request, "problem_date");
		String job = DaoFactory.getParam(request, "job");
		String event_theme = DaoFactory.getParam(request, "event_theme");
		String complain_advice = DaoFactory.getParam(request, "complain_advice");
		String apply_no = DaoFactory.getParam(request, "apply_no");
		String dealer_contact = DaoFactory.getParam(request, "dealer_contact");
		String dealer_phone = DaoFactory.getParam(request, "dealer_phone");
		String dealer_code = DaoFactory.getParam(request, "dealer_code");
		String special_type = DaoFactory.getParam(request, "special_type");
		String identify = DaoFactory.getParam(request, "identify");
		String apply_money = DaoFactory.getParam(request, "apply_money");
		String[] fjids=request.getParamValues("fjid");
		
		String fill_dealer_code = DaoFactory.getParam(request, "fill_dealer_code");
		String fill_dealer_shortname = DaoFactory.getParam(request, "fill_dealer_shortname");
		String apply_person = DaoFactory.getParam(request, "apply_person");
		String user_name = DaoFactory.getParam(request, "user_name");
		String user_link = DaoFactory.getParam(request, "user_link");
		String apply_date = DaoFactory.getParam(request, "apply_date");
		String is_change = DaoFactory.getParam(request, "is_change");
		String change_reson = DaoFactory.getParam(request, "change_reson");
		
		int res=1;
		try {
			if("".equals(spe_id)){//新增
				TtAsWrSpecialPO spe=new TtAsWrSpecialPO();
				spe.setApplyNo(apply_no);
				spe.setDealerId(Long.valueOf(loginUser.getDealerId()));
				spe.setDealerCode(loginUser.getDealerCode());
				spe.setVin(vin);
				spe.setMileage(Double.parseDouble(mileage));
				Long specialId = DaoFactory.getPkId();
				spe.setSpeId(specialId);
				spe.setSpecialType(Integer.parseInt(special_type));
				spe.setCreateBy(loginUser.getUserId());
				spe.setCreateDate(new Date());
				spe.setApplyMoney(Double.parseDouble(apply_money));
				if("0".equals(identify)){
					spe.setStatus(Constant.SPE_STATUS_01);
				}else{
					spe.setStatus(Constant.SPE_STATUS_02);
					spe.setReportBy(loginUser.getUserId());
					spe.setReportDate(new Date());
				}
				this.insert(spe);
				delAndReinsetFile(loginUser, fjids, specialId.toString());
				
				if("1".equals(special_type)){//善意索赔
					TtAsWrSpeGoodwillClaimPO g =new TtAsWrSpeGoodwillClaimPO();
					g.setClaimNo(claim_no);
					g.setJob(job);
					if(!"".equals(problem_date)){
						problem_date=problem_date.replaceAll("\\+", " ");
						g.setProblemDate(BaseUtils.getDate(problem_date, 3));
					}
					g.setSpecialId(specialId);
					g.setId(DaoFactory.getPkId());
					g.setDealerContact(dealer_contact);
					g.setDealerPhone(dealer_phone);
					g.setEventTheme(event_theme);
					g.setComplainAdvice(complain_advice);
					g.setClaimNo(claim_no);
					g.setIsClaim(Integer.parseInt(is_claim));
					g.setSupplyCodeDealer(supply_code_dealer);
					g.setPartCodeDealer(part_code_dealer);
					this.insert(g);
				}
				if("0".equals(special_type)){//退换车
					TtAsWrSpeChangeCarPO c=new TtAsWrSpeChangeCarPO();
					if(!"".equals(apply_date)){
						apply_date=apply_date.replaceAll("\\+", " ");
						c.setApplyDate(BaseUtils.getDate(apply_date, 3));
					}
					c.setApplyPerson(apply_person);
					c.setUserLink(user_link);
					c.setUserName(user_name);
					c.setFillDealerCode(fill_dealer_code);
					c.setFillDealerShortname(fill_dealer_shortname);
					c.setIsChange(is_change);
					c.setChangeReson(change_reson);
					c.setSpecialId(specialId);
					c.setId(DaoFactory.getPkId());
					this.insert(c);
				}
			}else{//修改
				Long speId = Long.valueOf(spe_id);
				TtAsWrSpecialPO spe1=new TtAsWrSpecialPO();
				spe1.setSpeId(speId);
				
				TtAsWrSpecialPO spe2=new TtAsWrSpecialPO();
				spe2.setVin(vin);
				spe2.setMileage(Double.parseDouble(mileage));
				spe2.setApplyMoney(Double.parseDouble(apply_money));
				if("0".equals(identify)){
					spe2.setStatus(Constant.SPE_STATUS_01);
				}else{
					spe2.setStatus(Constant.SPE_STATUS_02);
					spe2.setReportBy(loginUser.getUserId());
					spe2.setReportDate(new Date());
				}
				this.update(spe1,spe2);
				delAndReinsetFile(loginUser, fjids, speId.toString());
				
				if("1".equals(special_type)){//善意索赔
					TtAsWrSpeGoodwillClaimPO g1 =new TtAsWrSpeGoodwillClaimPO();
					g1.setSpecialId(speId);
					TtAsWrSpeGoodwillClaimPO g2 =new TtAsWrSpeGoodwillClaimPO();
					g2.setClaimNo(claim_no);
					g2.setJob(job);
					if(!"".equals(problem_date)){
						problem_date=problem_date.replaceAll("\\+", " ");
						g2.setProblemDate(BaseUtils.getDate(problem_date, 3));
					}
					g2.setDealerContact(dealer_contact);
					g2.setDealerPhone(dealer_phone);
					g2.setEventTheme(event_theme);
					g2.setComplainAdvice(complain_advice);
					g2.setClaimNo(claim_no);
					g2.setIsClaim(Integer.parseInt(is_claim));
					g2.setSupplyCodeDealer(supply_code_dealer);
					g2.setPartCodeDealer(part_code_dealer);
					this.update(g1,g2);
				}
				if("0".equals(special_type)){//退换车
					TtAsWrSpeChangeCarPO c1=new TtAsWrSpeChangeCarPO();
					c1.setSpecialId(speId);
					TtAsWrSpeChangeCarPO c2=new TtAsWrSpeChangeCarPO();
					if(!"".equals(apply_date)){
						apply_date=apply_date.replaceAll("\\+", " ");
						c2.setApplyDate(BaseUtils.getDate(apply_date, 3));
					}
					c2.setApplyPerson(apply_person);
					c2.setUserLink(user_link);
					c2.setUserName(user_name);
					c2.setFillDealerCode(fill_dealer_code);
					c2.setFillDealerShortname(fill_dealer_shortname);
					c2.setIsChange(is_change);
					c2.setChangeReson(change_reson);
					this.update(c1,c2);
				}
			}
			
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public int delSpe(String special_type, String id) {
		int res=1;
		try {
			if(!"".equals(id)){
				this.delete("delete from Tt_As_Wr_Special t where t.spe_id="+id, null);
				if("1".equals(special_type)){
					this.delete("delete from tt_as_wr_Spe_Goodwill_claim t where t.special_id="+id, null);
				}else{
					this.delete("delete from tt_as_wr_Spe_change_car t where t.special_id="+id, null);
				}
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> findSpeData(String special_type, String id) {
		StringBuffer sb= new StringBuffer();
		if("1".equals(special_type)){
			sb.append("select *\n" );
			sb.append("  from Tt_As_Wr_Special t, tt_as_wr_Spe_Goodwill_claim g,vw_data_by_vin v,tm_dealer tm \n" );
			sb.append(" where t.spe_id = g.special_id and t.vin=v.vin and tm.dealer_id = t.dealer_id \n");
		}else{
			sb.append("select *\n" );
			sb.append("  from Tt_As_Wr_Special t, tt_as_wr_Spe_change_car c,vw_data_by_vin v,tm_dealer tm  \n" );
			sb.append(" where t.spe_id = c.special_id  and t.vin=v.vin and tm.dealer_id = t.dealer_id\n");
		}
		DaoFactory.getsql(sb, "t.spe_id", id, 1);
		return this.pageQueryMap(sb.toString(), null, getFunName());
	}
	private void delAndReinsetFile(AclUserBean loginUser, String[] fjids,String pkId) throws SQLException {
		FileUploadManager.delAllFilesUploadByBusiness(pkId, fjids);
		FileUploadManager.fileUploadByBusiness(pkId, fjids, loginUser);
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> specialServiceManagerList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*\n" );
		sb.append("  from tt_as_wr_Special t, tm_dealer tm, vw_data_by_vin v\n" );
		sb.append(" where tm.dealer_id = t.dealer_id\n" );
		sb.append("   and v.vin = t.vin and t.status=20331002 \n");
		DaoFactory.getsql(sb, "t.dealer_id", loginUser.getDealerId(), 1);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> specialClaimSettlementList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*,tm.dealer_shortname,vw.root_org_name\n" );
		sb.append("  from tt_as_wr_Special t, tm_dealer tm, vw_data_by_vin v,vw_org_dealer_service vw\n" );
		sb.append(" where tm.dealer_id = t.dealer_id and t.dealer_code=vw.root_dealer_code\n" );
		sb.append("   and v.vin = t.vin and t.status=20331009 \n");
		TcPosePO posePO = new TcPosePO();
		posePO.setPoseId(loginUser.getPoseId());
		List<TcPosePO> list1 =this.select(posePO);
		String posename = list1.get(0).getPoseName();
        if (!"索赔主管".equals(posename)) {
        	sb.append(CommonUtils.getOrgDealerLimitSqlByPose("tm", loginUser));
		}		
        DaoFactory.getsql(sb, "vw.dealer_name", DaoFactory.getParam(request, "dealer_shortname"), 2);
        DaoFactory.getsql(sb, "vw.dealer_code", DaoFactory.getParam(request, "dealer_code"), 2);
        DaoFactory.getsql(sb, "vw.root_org_name", DaoFactory.getParam(request, "root_org_name"), 2);
        DaoFactory.getsql(sb, "t.vin", DaoFactory.getParam(request, "vin"), 2);
        sb.append(" order by t.REPORT_DATE asc");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> specialTecSupportList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.* from (select t.*,vw.root_org_name,tm.dealer_shortname\n" );
		sb.append("  from tt_as_wr_Special t, tm_dealer tm, vw_data_by_vin v,vw_org_dealer_service vw\n" );
		sb.append(" where tm.dealer_id = t.dealer_id and t.dealer_id= vw.dealer_id\n" );
		sb.append("   and v.vin = t.vin\n" );
		sb.append("   and t.status = 20331007\n" );
		sb.append("   and t.apply_money >= 800\n" );
		sb.append("union all\n" );
		sb.append("select t.*,vw.root_org_name,tm.dealer_shortname\n" );
		sb.append("  from tt_as_wr_Special t, tm_dealer tm, vw_data_by_vin v,vw_org_dealer_service vw\n" );
		sb.append(" where tm.dealer_id = t.dealer_id and t.dealer_id= vw.dealer_id \n" );
		sb.append("   and v.vin = t.vin\n" );
		sb.append("   and t.status = 20331005\n" );
		sb.append("   and t.apply_money < 800) t where 1=1 \n");
        sb.append(CommonUtils.getOrgDealerLimitSqlByPose("t", loginUser));
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> specialRegionalDirectorList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*,vw.root_org_name,tm.dealer_shortname\n" );
		sb.append("  from tt_as_wr_Special t, tm_dealer tm, vw_data_by_vin v,vw_org_dealer_service vw\n" );
		sb.append(" where tm.dealer_id = t.dealer_id and t.dealer_id= vw.dealer_id\n" );
		sb.append("   and v.vin = t.vin and t.status=20331005 and t.apply_money >= 800\n");
        sb.append(CommonUtils.getOrgDealerLimitSqlByPose("tm", loginUser));
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> specialRegionalManagerList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*,vw.root_org_name,tm.dealer_shortname\n" );
		sb.append("  from tt_as_wr_Special t, tm_dealer tm, vw_data_by_vin v,vw_org_dealer_service vw\n" );
		sb.append(" where tm.dealer_id = t.dealer_id and t.dealer_id= vw.dealer_id\n" );
		sb.append("   and v.vin = t.vin and t.status=20331003\n");
		if (this.getPoseRoleId(loginUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
		       sb.append(CommonUtils.getOrgDealerLimitSqlByPose("tm", loginUser));
		}
		DaoFactory.getsql(sb, "t.dealer_id", loginUser.getDealerId(), 1);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	/**
	 * 通过职位ID获取角色ID
	 * @param poseId 职位ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getPoseRoleId(String poseId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM tr_role_pose p WHERE p.pose_id=" + poseId + "");
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		if (null == list || list.size() <= 0 || list.get(0) == null || list.get(0).get("ROLE_ID") == null) {
			return "0";
		}
		return CommonUtils.checkNull(list.get(0).get("ROLE_ID"));
	}
	
	@SuppressWarnings("unchecked")
	public int audit(RequestWrapper request, AclUserBean loginUser) {
		int res=1;
		String spe_id=DaoFactory.getParam(request, "spe_id");
		String status=DaoFactory.getParam(request, "status");
		String special_type=DaoFactory.getParam(request, "special_type");
		String service_manager_deal=DaoFactory.getParam(request, "service_manager_deal");
		String regional_manager_deal=DaoFactory.getParam(request, "regional_manager_deal");
		String regional_director_deal=DaoFactory.getParam(request, "regional_director_deal");
		String tec_support_dep_deal=DaoFactory.getParam(request, "tec_support_dep_deal");
		String tec_part_code=DaoFactory.getParam(request, "tec_part_code");
		String tec_supply_code=DaoFactory.getParam(request, "tec_supply_code");
		String claim_settlement_deal=DaoFactory.getParam(request, "claim_settlement_deal");
		String apply_money=DaoFactory.getParam(request, "apply_money");
		String audit_amount=DaoFactory.getParam(request, "audit_amount");
		String identify = DaoFactory.getParam(request, "identify");
		String responsibility_code=DaoFactory.getParam(request, "responsibility_code");
		String sales_policy_money=DaoFactory.getParam(request, "sales_policy_money");
		String is_warranty=DaoFactory.getParam(request, "is_warranty");
		String purchase_car_cost=DaoFactory.getParam(request, "purchase_car_cost");
		String original_car_price=DaoFactory.getParam(request, "original_car_price");
		String on_card_purchase_tax=DaoFactory.getParam(request, "on_card_purchase_tax");
		String on_card_licensing_fee=DaoFactory.getParam(request, "on_card_licensing_fee");
		String on_card_ohers=DaoFactory.getParam(request, "on_card_ohers");
		String is_insurance=DaoFactory.getParam(request, "is_insurance");
		String insurance_money=DaoFactory.getParam(request, "insurance_money");
		String others_money=DaoFactory.getParam(request, "others_money");
		String old_car_price=DaoFactory.getParam(request, "old_car_price");
		String discount_money=DaoFactory.getParam(request, "discount_money");
		String loss_discount_money=DaoFactory.getParam(request, "loss_discount_money");
		String policy_discount_money=DaoFactory.getParam(request, "policy_discount_money");
		String old_car_deal=DaoFactory.getParam(request, "old_car_deal");
		String old_car_accept_unit=DaoFactory.getParam(request, "old_car_accept_unit");
		String Approval_Money=DaoFactory.getParam(request, "audit_money");//lj2015-5-12
		String balance_Approval_Money = DaoFactory.getParam(request, "balance_Approval_Money");//lj2015-5-12

		Long userId = loginUser.getUserId();
		try {
			if("0".equals(identify)){//通过
				Long speId = Long.valueOf(spe_id);
				TtAsWrSpecialPO spe1=new TtAsWrSpecialPO();
				spe1.setSpeId(speId);
				
				TtAsWrSpecialPO spe2=new TtAsWrSpecialPO();
				if("20331002".equals(status)){//--服务站的经理审核
					spe2.setServiceManagerDeal(service_manager_deal);
					spe2.setManagerAuditBy(userId);
					spe2.setManagerAuditDate(new Date());
					spe2.setStatus(20331003);
				}
				if("20331003".equals(status)){//--区域经理
					spe2.setRegionalManagerDeal(regional_manager_deal);
					spe2.setRegionalAuditBy(userId);
					spe2.setRegionalAuditDate(new Date());
					spe2.setStatus(20331005);
					if(BaseUtils.notNull(Approval_Money)){
						spe2.setApprovalMoney(Double.parseDouble(Approval_Money));//lj2015-5-12
					}
				}
				if("20331005".equals(status) && Double.parseDouble(apply_money)>=800){//--到区域总监
					spe2.setRegionalDirectorDeal(regional_director_deal);
					spe2.setDirectorAuditBy(userId);
					spe2.setDirectorAuditDate(new Date());
					spe2.setStatus(20331007);
					if(BaseUtils.notNull(balance_Approval_Money)){
						spe2.setBalanceApprovalMoney(Double.parseDouble(balance_Approval_Money));//lj2015-5-12
					}
				}
				if("20331005".equals(status) && Double.parseDouble(apply_money)<800){//--到技术部
					spe2.setRegionalDirectorDeal(regional_director_deal);
					spe2.setDirectorAuditBy(userId);
					spe2.setDirectorAuditDate(new Date());
					spe2.setStatus(20331009);
				}
				if("20331007".equals(status)){
					spe2.setTecSupportDepDeal(tec_support_dep_deal);
					spe2.setTecAuditBy(userId);
					spe2.setTecAuditDate(new Date());
					spe2.setStatus(20331009);
					
				}
				if("20331009".equals(status)){
					spe2.setClaimSettlementDeal(claim_settlement_deal);
					spe2.setSettlementAuditBy(userId);
					spe2.setSettlementAuditDate(new Date());
					spe2.setStatus(20331011);
					if (BaseUtils.notNull(audit_amount)) {
						spe2.setAuditAmount(Double.parseDouble(audit_amount));
					}
				}
				this.update(spe1, spe2);
				if("1".equals(special_type)){//善意索赔审核
					if("20331007".equals(status)){//技术部再次选择配件代码和供应商
						TtAsWrSpeGoodwillClaimPO g1 =new TtAsWrSpeGoodwillClaimPO();
						g1.setSpecialId(speId);
						TtAsWrSpeGoodwillClaimPO g2 =new TtAsWrSpeGoodwillClaimPO();
						g2.setTecPartCode(tec_part_code);
						g2.setTecSupplyCode(tec_supply_code);
						this.update(g1, g2);
					}
				}else{//退换车
					if("20331002".equals(status)){//--服务站的经理审核
						TtAsWrSpeChangeCarPO c1 =new TtAsWrSpeChangeCarPO();
						c1.setSpecialId(speId);
						
						TtAsWrSpeChangeCarPO c2 =new TtAsWrSpeChangeCarPO();
						if (BaseUtils.notNull(discount_money)) {
							c2.setDiscountMoney(Double.parseDouble(discount_money));
						}
						if (BaseUtils.notNull(loss_discount_money)) {
							c2.setLossDiscountMoney(Double.parseDouble(loss_discount_money));
						}
						if (BaseUtils.notNull(policy_discount_money)) {
							c2.setPolicyDiscountMoney(Double.parseDouble(policy_discount_money));
						}
						if (BaseUtils.notNull(on_card_licensing_fee)) {
							c2.setOnCardLicensingFee(Double.parseDouble(on_card_licensing_fee));
						}
						if (BaseUtils.notNull(on_card_purchase_tax)) {
							c2.setOnCardPurchaseTax(Double.parseDouble(on_card_purchase_tax));
						}
						if (BaseUtils.notNull(on_card_ohers)) {
							c2.setOnCardOhers(Double.parseDouble(on_card_ohers));
						}
						if (BaseUtils.notNull(old_car_price)) {
							c2.setOldCarPrice(Double.parseDouble(old_car_price));
						}
						if (BaseUtils.notNull(purchase_car_cost)) {
							c2.setPurchaseCarCost(Double.parseDouble(purchase_car_cost)); 
						}
						if (BaseUtils.notNull(original_car_price)) {
							c2.setOriginalCarPrice(Double.parseDouble(original_car_price));
						}
						if (BaseUtils.notNull(others_money)) {
							c2.setOthersMoney(Double.parseDouble(others_money));
						}
						c2.setOldCarDeal(old_car_deal);
						c2.setIsInsurance(is_insurance);
						if(!"".equals(insurance_money)){
							c2.setInsuranceMoney(Double.parseDouble(insurance_money));
						}
						c2.setOldCarAcceptUnit(old_car_accept_unit);
						this.update(c1, c2);
					}
					if("20331007".equals(status)){//技术部选择责任方代码和是否享受质保
						TtAsWrSpeChangeCarPO c1 =new TtAsWrSpeChangeCarPO();
						c1.setSpecialId(speId);
						TtAsWrSpeChangeCarPO c2 =new TtAsWrSpeChangeCarPO();
						c2.setResponsibilityCode(responsibility_code);
						c2.setIsWarranty(is_warranty);
						this.update(c1, c2);
					}
					if("20331009".equals(status)){//结算室销售政策金额
						TtAsWrSpeChangeCarPO c1 =new TtAsWrSpeChangeCarPO();
						c1.setSpecialId(speId);
						TtAsWrSpeChangeCarPO c2 =new TtAsWrSpeChangeCarPO();
						if (BaseUtils.notNull(sales_policy_money)) {
							c2.setSalesPolicyMoney(Double.parseDouble(sales_policy_money));
						}
						this.update(c1, c2);
					}
				}
				//===========================
				this.insertAuditRecord(status, userId, speId);
				//===========================
			}else{//驳回
				Long speId = Long.valueOf(spe_id);
				TtAsWrSpecialPO spe1=new TtAsWrSpecialPO();
				spe1.setSpeId(speId);
				
				TtAsWrSpecialPO spe2=new TtAsWrSpecialPO();
				if("20331002".equals(status)){//--服务站的经理审核
					spe2.setServiceManagerDeal(service_manager_deal);
					spe2.setManagerAuditBy(userId);
					spe2.setManagerAuditDate(new Date());
					spe2.setStatus(20331004);
				}
				if("20331003".equals(status)){//--区域经理
					spe2.setRegionalManagerDeal(regional_manager_deal);
					spe2.setRegionalAuditBy(userId);
					spe2.setRegionalAuditDate(new Date());
					spe2.setStatus(20331006);
				}
				if("20331005".equals(status) && Double.parseDouble(apply_money)>=800){//--到区域总监
					spe2.setRegionalDirectorDeal(regional_director_deal);
					spe2.setDirectorAuditBy(userId);
					spe2.setDirectorAuditDate(new Date());
					spe2.setStatus(20331008);
				}
				if("20331005".equals(status) && Double.parseDouble(apply_money)<800){//--到技术部
					spe2.setRegionalDirectorDeal(regional_director_deal);
					spe2.setDirectorAuditBy(userId);
					spe2.setDirectorAuditDate(new Date());
					spe2.setStatus(20331010);
				}
				if("20331007".equals(status)){
					spe2.setTecSupportDepDeal(tec_support_dep_deal);
					spe2.setTecAuditBy(userId);
					spe2.setTecAuditDate(new Date());
					spe2.setStatus(20331010);
				}
				if("20331009".equals(status)){
					spe2.setClaimSettlementDeal(claim_settlement_deal);
					spe2.setSettlementAuditBy(userId);
					spe2.setSettlementAuditDate(new Date());
					if (BaseUtils.notNull(audit_amount)) {
						spe2.setAuditAmount(Double.parseDouble(audit_amount));
					}
					spe2.setStatus(20331012);
				}
				this.update(spe1, spe2);
				if("1".equals(special_type)){//善意索赔审核
					if("20331007".equals(status)){//技术部再次选择配件代码和供应商
						TtAsWrSpeGoodwillClaimPO g1 =new TtAsWrSpeGoodwillClaimPO();
						g1.setSpecialId(speId);
						TtAsWrSpeGoodwillClaimPO g2 =new TtAsWrSpeGoodwillClaimPO();
						g2.setTecPartCode(tec_part_code);
						g2.setTecSupplyCode(tec_supply_code);
						this.update(g1, g2);
					}
				}else{//退换车
					if("20331002".equals(status)){//--服务站的经理审核
						TtAsWrSpeChangeCarPO c1 =new TtAsWrSpeChangeCarPO();
						c1.setSpecialId(speId);
						
						TtAsWrSpeChangeCarPO c2 =new TtAsWrSpeChangeCarPO();
						if (BaseUtils.notNull(discount_money)) {
							c2.setDiscountMoney(Double.parseDouble(discount_money));
						}
						if (BaseUtils.notNull(loss_discount_money)) {
							c2.setLossDiscountMoney(Double.parseDouble(loss_discount_money));
						}
						if (BaseUtils.notNull(policy_discount_money)) {
							c2.setPolicyDiscountMoney(Double.parseDouble(policy_discount_money));
						}
						if (BaseUtils.notNull(on_card_licensing_fee)) {
							c2.setOnCardLicensingFee(Double.parseDouble(on_card_licensing_fee));
						}
						if (BaseUtils.notNull(on_card_purchase_tax)) {
							c2.setOnCardPurchaseTax(Double.parseDouble(on_card_purchase_tax));
						}
						if (BaseUtils.notNull(on_card_ohers)) {
							c2.setOnCardOhers(Double.parseDouble(on_card_ohers));
						}
						if (BaseUtils.notNull(old_car_price)) {
							c2.setOldCarPrice(Double.parseDouble(old_car_price));
						}
						if (BaseUtils.notNull(purchase_car_cost)) {
							c2.setPurchaseCarCost(Double.parseDouble(purchase_car_cost)); 
						}
						if (BaseUtils.notNull(original_car_price)) {
							c2.setOriginalCarPrice(Double.parseDouble(original_car_price));
						}
						if (BaseUtils.notNull(insurance_money)) {
							c2.setInsuranceMoney(Double.parseDouble(insurance_money));
						}
						if (BaseUtils.notNull(others_money)) {
							c2.setOthersMoney(Double.parseDouble(others_money));
						}
						c2.setOldCarDeal(old_car_deal);
						c2.setIsInsurance(is_insurance);
						c2.setOldCarAcceptUnit(old_car_accept_unit);
						this.update(c1, c2);
					}
					if("20331007".equals(status)){//技术部选择责任方代码和是否享受质保
						TtAsWrSpeChangeCarPO c1 =new TtAsWrSpeChangeCarPO();
						c1.setSpecialId(speId);
						TtAsWrSpeChangeCarPO c2 =new TtAsWrSpeChangeCarPO();
						c2.setResponsibilityCode(responsibility_code);
						c2.setIsWarranty(is_warranty);
						this.update(c1, c2);
					}
					if("20331009".equals(status)){//结算室销售政策金额
						TtAsWrSpeChangeCarPO c1 =new TtAsWrSpeChangeCarPO();
						c1.setSpecialId(speId);
						TtAsWrSpeChangeCarPO c2 =new TtAsWrSpeChangeCarPO();
						c2.setSalesPolicyMoney(Double.parseDouble(sales_policy_money));
						this.update(c1, c2);
					}
				}
				//===========================
				this.insertAuditRecord(status, userId, speId);
				//===========================
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}
	/**
	 * 插入审核日志
	 * @param status
	 * @param userId
	 * @param speId
	 */
	@SuppressWarnings("unchecked")
	private void insertAuditRecord(String status, Long userId, Long speId) {
		TtAsWrSpecialRecordPO po=new TtAsWrSpecialRecordPO();
		po.setAuditBy(userId);
		po.setAuditDate(new Date());
		po.setOperaStstus(Integer.parseInt(status));
		po.setId(DaoFactory.getPkId());
		po.setSpecialId(speId);
		this.insert(po);
	}
	//=====================================lj 2015-4-10
	@SuppressWarnings("unchecked")
	public int sureInsert(RequestWrapper request,String view,AclUserBean loguser) {//view 1 善意索赔
		   Long id = DaoFactory.getPkId();//取id
			try {
				String[] fjids=request.getParamValues("fjid");//获取附件id
				delAndReinsetFile(loguser, fjids, id.toString());
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		   StringBuffer sb= new StringBuffer();
		   String spe_id=DaoFactory.getParam(request, "SPE_ID");
		   String apply_no=DaoFactory.getParam(request, "appno");
		   String CLAIM_NO=DaoFactory.getParam(request, "CLAIM_NO");
		   String DEALER_ID=DaoFactory.getParam(request, "DEALER_ID");
		   String apply_amount=DaoFactory.getParam(request, "APPLY_MONEY");
		   if ("".equals(apply_amount)) {
			   apply_amount=null;
		   }
		   String settlement_settlement=DaoFactory.getParam(request, "address");
		   String VIN=DaoFactory.getParam(request, "VIN");
		   String special_type=DaoFactory.getParam(request, "type_claim");
		   String APPLY_REMARK=DaoFactory.getParam(request, "APPLY_REMARK");
		   sb.append("insert into tt_as_wr_special_apply\n");
		   sb.append("  (id,\n" );
		   sb.append("   special_id,\n");
		   sb.append("   dealer_id,\n" );
		   sb.append("   special_type,\n");
		   sb.append("   apply_no,\n");
		   sb.append("   apply_amount,\n");
		   sb.append("   yieldly,\n");
		   sb.append("   vin,\n");
		   sb.append("   apply_remark,\n");
		   sb.append("   status,\n");
		   sb.append("   apply_date,\n");
		   sb.append("   apply_by)");
		   sb.append("values("+id+","+spe_id+","+DEALER_ID+","+special_type+",'"+apply_no+"',"+apply_amount+",'"+settlement_settlement+"','"+VIN+"','"+APPLY_REMARK+"',"+view+",sysdate,"+loguser.getUserId()+")");
			this.insert(sb.toString());
			return Integer.parseInt(view);
		}
	     //审核查询返还车及善意索赔查询
	    @SuppressWarnings("unchecked")
		public PageResult<Map<String, Object>> findreturncarAndclaim(RequestWrapper request, Integer pageSize, Integer currPage) {
	    	String id=DaoFactory.getParam(request, "id");
	    	String apply_no=DaoFactory.getParam(request, "apply_no");
			String dealerCode=DaoFactory.getParam(request, "dealerCode");
			String VIN=DaoFactory.getParam(request, "VIN");
			String CREATE_DATE_S=DaoFactory.getParam(request, "CREATE_DATE_S");
			String CREATE_DATE_D=DaoFactory.getParam(request, "CREATE_DATE_D");
			String SPECIAL_TYPE=DaoFactory.getParam(request, "SPECIAL_TYPE");
			String supply_code_dealer=DaoFactory.getParam(request, "supply_code_dealer");
			String STATUS=DaoFactory.getParam(request, "STATUS");
			String ROOT_ORG_NAME=DaoFactory.getParam(request, "ROOT_ORG_NAME");
			StringBuffer sql= new StringBuffer();
			sql.append("\n" );
			sql.append("select a.* from (\n" );
			sql.append("(select tc.code_desc,\n" );
			sql.append("       tt.id,\n" );
			sql.append("       vw.ROOT_ORG_NAME,\n" );
			sql.append("       tm.DEALER_CODE,\n" );
			sql.append("       tm.DEALER_SHORTNAME,\n" );
			sql.append("       tt.APPLY_NO,\n" );
			sql.append("       tt.VIN,\n" );
			sql.append("       tt.APPLY_DATE,\n" );
			sql.append("       tt.AUDIT_DATE,\n" );
			sql.append("       cl.tec_supply_code SUPPLY_CODE_DEALER,\n" );
			sql.append("       tt.status,\n" );
			sql.append("       tt.SPECIAL_TYPE,\n" );
			sql.append("       tt.APPLY_AMOUNT,\n" );
			sql.append("        vv.MODEL_NAME,\n" );
			sql.append("       tt.APPROVAL_AMOUNT\n" );
			sql.append("  from tt_as_wr_special_apply tt,\n" );
			sql.append("       tm_dealer              tm ,\n" );
			sql.append("        tm_vehicle v,vw_material_group_service  vv,\n" );
			sql.append("       vw_org_dealer_service       vw,\n" );
			sql.append("       tt_as_wr_Spe_Goodwill_claim cl,\n" );
			sql.append("       tt_as_wr_special sp,\n" );
			sql.append("        tc_code tc\n" );
			sql.append(" where 1 = 1\n" );
			sql.append("   and tt.dealer_id = tm.dealer_id\n" );
			sql.append("   and vw.dealer_id = tt.dealer_id\n" );
			sql.append("   and tt.vin=v.vin\n" );
			sql.append("   and v.package_id=vv.PACKAGE_ID");
			sql.append("   and cl.special_id = tt.special_id\n" );
			sql.append("   and sp.SPE_ID = tt.SPECIAL_ID\n" );
			sql.append("   and tt.status = tc.code_id(+)\n" );
			DaoFactory.getsql(sql, "tt.APPLY_NO", apply_no, 2);
			DaoFactory.getsql(sql, "tm.dealer_code", dealerCode, 2);
			DaoFactory.getsql(sql, "tt.audit_DATE", CREATE_DATE_S, 3);
			DaoFactory.getsql(sql, "tt.audit_DATE", CREATE_DATE_D, 4);
			DaoFactory.getsql(sql, "tt.VIN", VIN, 2);
			DaoFactory.getsql(sql, "cl.supply_code_dealer", supply_code_dealer, 2);
			DaoFactory.getsql(sql, "vw.ROOT_ORG_NAME", ROOT_ORG_NAME, 1);
			DaoFactory.getsql(sql, "tt.STATUS", STATUS, 1);
			DaoFactory.getsql(sql, "tt.SPECIAL_TYPE", SPECIAL_TYPE, 1);
			sql.append("       ) union  all\n" );
			sql.append("       (select tc.code_desc,\n" );
			sql.append("       tt.id,\n" );
			sql.append("       vw.ROOT_ORG_NAME,\n" );
			sql.append("       tm.DEALER_CODE,\n" );
			sql.append("       tm.DEALER_SHORTNAME,\n" );
			sql.append("       tt.APPLY_NO,\n" );
			sql.append("       tt.VIN,\n" );
			sql.append("       tt.APPLY_DATE,\n" );
			sql.append("       tt.AUDIT_DATE,\n" );
			sql.append("       cll.responsibility_code    SUPPLY_CODE_DEALER,\n" );
			sql.append("       tt.status,\n" );
			sql.append("       tt.SPECIAL_TYPE,\n" );
			sql.append("       tt.APPLY_AMOUNT,\n" );
			sql.append("        vv.MODEL_NAME,\n" );
			sql.append("       tt.APPROVAL_AMOUNT\n" );
			sql.append("  from tt_as_wr_special_apply tt,\n" );
			sql.append("       tm_dealer              tm ,\n" );
			sql.append("        tm_vehicle v,vw_material_group_service  vv,\n" );
			sql.append("       vw_org_dealer_service       vw,\n" );
			sql.append("       tt_as_wr_spe_change_car cll,\n" );
			sql.append("       tt_as_wr_special sp,\n" );
			sql.append("        tc_code tc\n" );
			sql.append(" where 1 = 1\n" );
			sql.append("   and tt.dealer_id = tm.dealer_id\n" );
			sql.append("   and vw.dealer_id = tt.dealer_id\n" );
			sql.append("   and tt.vin=v.vin\n" );
			sql.append("   and v.package_id=vv.PACKAGE_ID");
			sql.append("   and cll.special_id = tt.special_id\n" );
			sql.append("   and sp.SPE_ID = tt.SPECIAL_ID\n" );
			sql.append("   and tt.status = tc.code_id(+)\n" );
			DaoFactory.getsql(sql, "tt.APPLY_NO", apply_no, 2);
			DaoFactory.getsql(sql, "tm.dealer_code", dealerCode, 2);
			DaoFactory.getsql(sql, "tt.audit_DATE", CREATE_DATE_S, 3);
			DaoFactory.getsql(sql, "tt.audit_DATE", CREATE_DATE_D, 4);
			DaoFactory.getsql(sql, "tt.VIN", VIN, 2);
			DaoFactory.getsql(sql, "cll.responsibility_code", supply_code_dealer, 2);
			DaoFactory.getsql(sql, "vw.ROOT_ORG_NAME", ROOT_ORG_NAME, 1);
			DaoFactory.getsql(sql, "tt.STATUS", STATUS, 1);
			DaoFactory.getsql(sql, "tt.SPECIAL_TYPE", SPECIAL_TYPE, 1);
			sql.append("     )) a\n" );

			sql.append(" order by a.status ");
			if ("20501005".equals(STATUS)) {
				sql.append("  ,a.audit_date desc ");
			}else {
				sql.append("  , a.apply_date asc ");
			}
			return this.pageQuery(sql.toString(), null, getFunName(), pageSize, currPage);
		}
		@SuppressWarnings("unchecked")
		public Map<String, Object> findreturncarByid(RequestWrapper request) {
			String id=DaoFactory.getParam(request, "id");
			String special_type=DaoFactory.getParam(request, "special_type");
			StringBuffer sql= new StringBuffer();
						sql.append("select t.*,ts.*,tm.*,d.*,tm.id as apply_id  \n" );
						sql.append("  from tt_as_wr_Special            t,\n" );
						if ("1".equals(special_type)) {
							sql.append("       tt_as_wr_Spe_Goodwill_claim ts,\n" );
						}else {
							sql.append("       tt_as_wr_spe_change_car ts,\n" );
						}
						sql.append("       tt_as_wr_special_apply      tm,\n" );
						sql.append("       tm_dealer                   d\n" );
						sql.append(" where t.spe_id = ts.special_id(+)\n" );
						sql.append("   and t.spe_id = tm.special_id(+)\n" );
						sql.append("   and t.dealer_id = d.dealer_id(+)\n" );
						sql.append("   and tm.id = " +id);
						return this.pageQueryMap(sql.toString(), null, getFunName());
		}
		@SuppressWarnings("unchecked")
		public int updateReturncar(RequestWrapper request, AclUserBean loginUser ,String view) {
			String id=DaoFactory.getParam(request, "id");
			String audit_reason=DaoFactory.getParam(request, "audit_reason");
			String AUDIT_AMOUNT=DaoFactory.getParam(request, "AUDIT_AMOUNT");//审核金额
			try {
				String[] fjids=request.getParamValues("fjid");//获取附件id
				delAndReinsetFile(loginUser, fjids, id.toString());
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			TtAsWrSpecialApplyPO applyPO = new TtAsWrSpecialApplyPO();
			TtAsWrSpecialApplyPO applyPO1 = new TtAsWrSpecialApplyPO();
			applyPO.setId(Long.valueOf(id));
			applyPO1.setAuditBy(loginUser.getUserId());
			if (BaseUtils.notNull(AUDIT_AMOUNT)) {
				applyPO1.setApprovalAmount(Double.valueOf(AUDIT_AMOUNT));
			}
			applyPO1.setAuditRemark(audit_reason);
			applyPO1.setAuditDate(new Date());
			applyPO1.setStatus(Integer.valueOf(view));
			return this.update(applyPO,applyPO1);
		}
		@SuppressWarnings("unchecked")
		public Map<String, Object> findDateapplyByUserid(Long userId) {
			StringBuffer sb= new StringBuffer();
			sb.append("select *\n");
			sb.append("  from tt_as_wr_Special tt\n");
			sb.append("  left join tm_dealer tm on tt.dealer_id = tm.dealer_id\n" );
			sb.append("  left join vw_data_by_vin v on v.vin = tt.vin\n");
			sb.append("  left join tt_as_wr_Spe_Goodwill_claim ww on tt.spe_id = ww.special_id\n");
			sb.append("  left join tc_user u on u.dealer_id = tt.dealer_id");
			sb.append("  where u.user_id="+userId);
			return this.pageQueryMap(sb.toString(),null, getFunName());
		}
		@SuppressWarnings("unchecked")
		public PageResult<Map<String, Object>> findspecialappno(RequestWrapper request,AclUserBean loguser ,Integer pageSize, Integer currPage) {
			StringBuffer sb= new StringBuffer();
			sb.append("select  * \n");
			sb.append("  from tt_as_wr_Special tt\n"); 
			sb.append("  left join tm_dealer tm on tt.dealer_id = tm.dealer_id\n" );
			sb.append("  left join vw_data_by_vin v on v.vin = tt.vin\n");
			sb.append("  left join tt_as_wr_Spe_Goodwill_claim ww on tt.spe_id = ww.special_id\n");
			sb.append("  left join tc_user u on u.dealer_id = tt.dealer_id");
			sb.append("  where u.user_id="+loguser.getUserId());
			sb.append("  and tt.status=20331011 ");
			sb.append("  and tt.apply_no  not in (select ap.apply_no from tt_as_wr_special_apply ap where tt.apply_no=ap.apply_no)");
			return this.pageQuery(sb.toString(),null,getFunName(), pageSize, currPage);
		}
		//索赔单号查询
		@SuppressWarnings("unchecked")
		public Map<String, Object> findDateByappno(RequestWrapper request) {
			StringBuffer sb= new StringBuffer();
			String appno=DaoFactory.getParam(request, "appno");
			sb.append("select *\n");
			sb.append("  from tt_as_wr_Special tt\n");
			sb.append("  left join tm_dealer tm on tt.dealer_id = tm.dealer_id\n" );
			sb.append("  left join vw_data_by_vin v on v.vin = tt.vin\n");
			sb.append("  left join tt_as_wr_Spe_Goodwill_claim ww on tt.spe_id = ww.special_id\n");
			sb.append("  left join tc_user u on u.dealer_id = tt.dealer_id");
			sb.append("  where tt.apply_no="+appno);
			return this.pageQueryMap(sb.toString(),null, getFunName());
		}
        //善意索赔申请查询
		@SuppressWarnings("unchecked")
		public PageResult<Map<String, Object>> findDatespecialapply(RequestWrapper request, AclUserBean loginUser,Integer pageSize, Integer currPage) {
			String loguser =loginUser.getUserId().toString();
			
			String id=DaoFactory.getParam(request, "id");
	    	String apply_no=DaoFactory.getParam(request, "apply_no");
			String dealerCode=DaoFactory.getParam(request, "dealerCode");
			String VIN=DaoFactory.getParam(request, "VIN");
			String CREATE_DATE_S=DaoFactory.getParam(request, "CREATE_DATE_S");
			String CREATE_DATE_D=DaoFactory.getParam(request, "CREATE_DATE_D");
			String SPECIAL_TYPE=DaoFactory.getParam(request, "SPECIAL_TYPE");
			String supply_code_dealer=DaoFactory.getParam(request, "supply_code_dealer");
			String STATUS=DaoFactory.getParam(request, "STATUS");
			String ROOT_ORG_NAME=DaoFactory.getParam(request, "ROOT_ORG_NAME");
			StringBuffer sql= new StringBuffer();
			sql.append("\n" );
			sql.append("select a.* from (\n" );
			sql.append("(select tc.code_desc,\n" );
			sql.append("       tt.id,\n" );
			sql.append("       vw.ROOT_ORG_NAME,\n" );
			sql.append("       tm.DEALER_CODE,\n" );
			sql.append("       tm.DEALER_SHORTNAME,\n" );
			sql.append("       tt.APPLY_NO,\n" );
			sql.append("       tt.VIN,\n" );
			sql.append("       tt.APPLY_DATE,\n" );
			sql.append("       tt.AUDIT_DATE,\n" );
			sql.append("       cl.tec_supply_code SUPPLY_CODE_DEALER,\n" );
			sql.append("       tt.status,\n" );
			sql.append("       tt.SPECIAL_TYPE,\n" );
			sql.append("       tt.APPLY_AMOUNT,\n" );
			sql.append("       tt.APPROVAL_AMOUNT\n" );
			sql.append("  from tt_as_wr_special_apply tt,\n" );
			sql.append("       tm_dealer              tm ,\n" );
			sql.append("       vw_org_dealer_service       vw,\n" );
			sql.append("       tt_as_wr_Spe_Goodwill_claim cl,\n" );
			sql.append("       tt_as_wr_special sp,\n" );
			sql.append("        tc_code tc\n" );
			sql.append(" where 1 = 1\n" );
			sql.append("   and tt.dealer_id = tm.dealer_id\n" );
			sql.append("   and vw.dealer_id = tt.dealer_id\n" );
			sql.append("   and cl.special_id = tt.special_id\n" );
			sql.append("   and sp.SPE_ID = tt.SPECIAL_ID\n" );
			sql.append("   and tt.status = tc.code_id\n" );
			DaoFactory.getsql(sql, "tt.APPLY_NO", apply_no, 2);
			DaoFactory.getsql(sql, "tm.dealer_code", dealerCode, 2);
			DaoFactory.getsql(sql, "tt.audit_DATE", CREATE_DATE_S, 3);
			DaoFactory.getsql(sql, "tt.audit_DATE", CREATE_DATE_D, 4);
			DaoFactory.getsql(sql, "tt.VIN", VIN, 2);
			DaoFactory.getsql(sql, "cl.supply_code_dealer", supply_code_dealer, 2);
			DaoFactory.getsql(sql, "vw.ROOT_ORG_NAME", ROOT_ORG_NAME, 1);
			DaoFactory.getsql(sql, "tt.STATUS", STATUS, 1);
			DaoFactory.getsql(sql, "tt.SPECIAL_TYPE", SPECIAL_TYPE, 1);
			DaoFactory.getsql(sql, "tt.apply_by", loguser, 1);
			sql.append("       ) union  all\n" );
			sql.append("       (select tc.code_desc,\n" );
			sql.append("       tt.id,\n" );
			sql.append("       vw.ROOT_ORG_NAME,\n" );
			sql.append("       tm.DEALER_CODE,\n" );
			sql.append("       tm.DEALER_SHORTNAME,\n" );
			sql.append("       tt.APPLY_NO,\n" );
			sql.append("       tt.VIN,\n" );
			sql.append("       tt.APPLY_DATE,\n" );
			sql.append("       tt.AUDIT_DATE,\n" );
			sql.append("       cll.responsibility_code    SUPPLY_CODE_DEALER,\n" );
			sql.append("       tt.status,\n" );
			sql.append("       tt.SPECIAL_TYPE,\n" );
			sql.append("       tt.APPLY_AMOUNT,\n" );
			sql.append("       tt.APPROVAL_AMOUNT\n" );
			sql.append("  from tt_as_wr_special_apply tt,\n" );
			sql.append("       tm_dealer              tm ,\n" );
			sql.append("       vw_org_dealer_service       vw,\n" );
			sql.append("       tt_as_wr_spe_change_car cll,\n" );
			sql.append("       tt_as_wr_special sp,\n" );
			sql.append("        tc_code tc\n" );
			sql.append(" where 1 = 1\n" );
			sql.append("   and tt.dealer_id = tm.dealer_id\n" );
			sql.append("   and vw.dealer_id = tt.dealer_id\n" );
			sql.append("   and cll.special_id = tt.special_id\n" );
			sql.append("   and sp.SPE_ID = tt.SPECIAL_ID\n" );
			sql.append("   and tt.status = tc.code_id\n" );
			DaoFactory.getsql(sql, "tt.APPLY_NO", apply_no, 2);
			DaoFactory.getsql(sql, "tm.dealer_code", dealerCode, 2);
			DaoFactory.getsql(sql, "tt.audit_DATE", CREATE_DATE_S, 3);
			DaoFactory.getsql(sql, "tt.audit_DATE", CREATE_DATE_D, 4);
			DaoFactory.getsql(sql, "tt.VIN", VIN, 2);
			DaoFactory.getsql(sql, "cll.responsibility_code", supply_code_dealer, 2);
			DaoFactory.getsql(sql, "vw.ROOT_ORG_NAME", ROOT_ORG_NAME, 1);
			DaoFactory.getsql(sql, "tt.STATUS", STATUS, 1);
			DaoFactory.getsql(sql, "tt.SPECIAL_TYPE", SPECIAL_TYPE, 1);
			DaoFactory.getsql(sql, "tt.apply_by", loguser, 1);
			sql.append("     )) a\n" );

			sql.append(" order by a.status ");
			if ("20501005".equals(STATUS)) {
				sql.append("  ,a.audit_date desc ");
			}else {
				sql.append("  , a.apply_date asc ");
			}
			
			return this.pageQuery(sql.toString(),null,getFunName(), pageSize, currPage);
		}
		@SuppressWarnings("unchecked")
		public Map<String, Object> viewSpecialApplyDetailed(RequestWrapper request) {
			String id=DaoFactory.getParam(request, "id");
			String special_type=DaoFactory.getParam(request, "special_type");
			StringBuffer sql= new StringBuffer();
						sql.append("select t.*,ts.*,tm.*,d.*,tm.id as apply_id,vw.MODEL_NAME, tc.code_desc  \n" );
						sql.append("  from tt_as_wr_Special            t,\n" );
						if ("1".equals(special_type)) {
							sql.append("       tt_as_wr_Spe_Goodwill_claim ts,\n" );
						}else {
							sql.append("       tt_as_wr_spe_change_car ts,\n" );
						}
						sql.append("       tt_as_wr_special_apply      tm,\n" );
						sql.append("       tm_dealer                   d,\n" );
						sql.append("       tm_vehicle v,\n" );
						sql.append("       tc_code tc,\n" );
						sql.append("       vw_material_group_service vw \n" );
						sql.append(" where t.spe_id = ts.special_id(+)\n" );
						sql.append("   and t.spe_id = tm.special_id(+)\n" );
						sql.append("   and t.vin=v.vin(+)\n");
						sql.append("    and tm.status=tc.code_id(+)\n");
						sql.append("   and v.package_id=vw.PACKAGE_ID(+)\n");
						sql.append("   and t.dealer_id = d.dealer_id(+)\n" );
						sql.append("   and tm.id = " +id);
						return this.pageQueryMap(sql.toString(), null, getFunName());
		}
	//=====================================
		@SuppressWarnings("unchecked")
		public List<Map<String,Object>> checkapplyno(RequestWrapper request, AclUserBean loginUser,Integer pageSize, Integer currPage) {
			StringBuffer sb= new StringBuffer();
			String apply_no=DaoFactory.getParam(request, "appno");
			sb.append("select * from tt_as_wr_special_apply where apply_no='"+apply_no+"'");
			sb.append(" and status not in (20501006)");
			return this.pageQuery(sb.toString(),null, getFunName());
		}
		//修改操作
		@SuppressWarnings("unchecked")
		public int specialapply(RequestWrapper request, AclUserBean loginUser,
				String type) {
			   String ID=DaoFactory.getParam(request, "ID");
			   String SPE_ID=DaoFactory.getParam(request, "SPE_ID");
			   String apply_no=DaoFactory.getParam(request, "appno");
			   String CLAIM_NO=DaoFactory.getParam(request, "CLAIM_NO");
			   String DEALER_ID=DaoFactory.getParam(request, "DEALER_ID");
			   String apply_amount=DaoFactory.getParam(request, "APPLY_MONEY");
			   String type_claim=DaoFactory.getParam(request, "type_claim");
			   String address=DaoFactory.getParam(request, "address");
			   String VIN=DaoFactory.getParam(request, "VIN");
			   String APPLY_REMARK=DaoFactory.getParam(request, "APPLY_REMARK");
			   TtAsWrSpecialApplyPO applyPO1 = new TtAsWrSpecialApplyPO();
			   TtAsWrSpecialApplyPO applyPO2 = new TtAsWrSpecialApplyPO();
			   applyPO1.setId(Long.parseLong(ID));
			   applyPO2.setId(Long.parseLong(ID));
			   applyPO2.setDealerId(Long.parseLong(DEALER_ID));
			   applyPO2.setSpecialId(Long.parseLong(SPE_ID));
			   applyPO2.setSpecialType(Integer.parseInt(type_claim));
			   applyPO2.setApplyNo(apply_no);
			   if (BaseUtils.notNull(apply_amount)) {
				  applyPO2.setApplyAmount(Double.parseDouble(apply_amount));
			    }
			   if (BaseUtils.notNull(address)) {
				   applyPO2.setYieldly(Long.parseLong(address));
			    }
			   applyPO2.setVin(VIN);
			   applyPO2.setApplyRemark(APPLY_REMARK);
			   applyPO2.setApplyDate(new Date());
			   applyPO2.setApplyBy(loginUser.getUserId());
			   applyPO2.setStatus(Integer.parseInt(type));
			   try {
				   String[] fjids=request.getParamValues("fjid");//获取附件id
				   delAndReinsetFile(loginUser, fjids, ID.toString());
			   } catch (Exception e) {
				   e.printStackTrace();
			   }
			   return this.update(applyPO1,applyPO2);
		}
		@SuppressWarnings("unchecked")
		public int delSpecialapply(String id) {
			StringBuffer sb= new StringBuffer();
			sb.append("delete from tt_as_wr_special_apply where id = "+id);
			return this.delete(sb.toString(), null);
		}
		@SuppressWarnings("unchecked")
		public int updatespecialapplyreport(RequestWrapper request,AclUserBean loginUser, String id) {
			StringBuffer sb= new StringBuffer();
		    String type = 	DaoFactory.getParam(request, "type");
			if ("Report".equals(type)) {
				type="20501002";
			}
			if ("Revoke".equals(type)) {
				type="20501001";
			}
			sb.append("update tt_as_wr_special_apply tt set status="+type +" where id = "+id);
			return this.update(sb.toString(), null);
		}
		@SuppressWarnings("unchecked")
		public Map<String, Object> queryByapplyno(AclUserBean loginUser, RequestWrapper request) {
			String apply_no = DaoFactory.getParam(request, "apply_no");
			String special_type = DaoFactory.getParam(request, "special_type");
			StringBuffer sql= new StringBuffer();
			sql.append("select t.*,ts.*,tm.*,d.*,tm.id as apply_id  \n" );
			sql.append("  from tt_as_wr_Special            t,\n" );
			if ("1".equals(special_type)) {
				sql.append("       tt_as_wr_Spe_Goodwill_claim ts,\n" );
			}else {
				sql.append("       tt_as_wr_spe_change_car ts,\n" );
			}
			sql.append("       tt_as_wr_special_apply      tm,\n" );
			sql.append("       tm_dealer                   d\n" );
			sql.append(" where t.spe_id = ts.special_id\n" );
			sql.append("   and t.spe_id = tm.special_id\n" );
			sql.append("   and t.dealer_id = d.dealer_id\n" );
			sql.append("  and t.apply_no='"+apply_no+"'");
			return this.pageQueryMap(sql.toString(), null, getFunName());
		}
		@SuppressWarnings("unchecked")
		public Map<String, Object> queryByCLAIMNO(AclUserBean loginUser,
				RequestWrapper request) {
			StringBuffer sb= new StringBuffer();
			String CLAIM_NO = DaoFactory.getParam(request, "CLAIM_NO");
			sb.append("select * from tt_as_wr_application  tt where tt.CLAIM_NO= '"+CLAIM_NO+"'");
			return this.pageQueryMap(sb.toString(), null, getFunName());
		}
		@SuppressWarnings("unchecked")
		public Map<String, Object> findtotleamount(RequestWrapper request,AclUserBean loginUser) {
			StringBuffer sb= new StringBuffer();
			sb.append("select sum(apply_amount) as totleamount from tt_as_wr_special_apply");
			return this.pageQueryMap(sb.toString(), null, getFunName());
		}
		@SuppressWarnings("unchecked")
		public Map<String, Object> findreturncarByvin(RequestWrapper request) {
			String vin = DaoFactory.getParam(request, "VIN");
			StringBuffer sb= new StringBuffer();
			sb.append("select count(vin) as VINCOUNT from tt_as_wr_special_apply where vin='"+vin+"'");
			return this.pageQueryMap(sb.toString(), null, getFunName());
		}
		@SuppressWarnings("unchecked")
		public Double Querycountspecil(AclUserBean loginUser,RequestWrapper request) {
			String id=DaoFactory.getParam(request, "id");
	    	String apply_no=DaoFactory.getParam(request, "apply_no");
			String dealerCode=DaoFactory.getParam(request, "dealerCode");
			String VIN=DaoFactory.getParam(request, "VIN");
			String CREATE_DATE_S=DaoFactory.getParam(request, "CREATE_DATE_S");
			String CREATE_DATE_D=DaoFactory.getParam(request, "CREATE_DATE_D");
			String SPECIAL_TYPE=DaoFactory.getParam(request, "SPECIAL_TYPE");
			String supply_code_dealer=DaoFactory.getParam(request, "supply_code_dealer");
			String STATUS=DaoFactory.getParam(request, "STATUS");
			String ROOT_ORG_NAME=DaoFactory.getParam(request, "ROOT_ORG_NAME");
			StringBuffer sql= new StringBuffer();
			sql.append("select sum(a.amount) as AMOUNT from (\n" );
			sql.append("(select\n" );
			sql.append("       sum(tt.APPLY_AMOUNT) as amount\n" );
			sql.append("\n" );
			sql.append("  from tt_as_wr_special_apply tt,\n" );
			sql.append("       tm_dealer              tm ,\n" );
			sql.append("       vw_org_dealer_service       vw,\n" );
			sql.append("       tt_as_wr_Spe_Goodwill_claim cl,\n" );
			sql.append("       tt_as_wr_special sp,\n" );
			sql.append("        tc_code tc\n" );
			sql.append(" where 1 = 1\n" );
			sql.append("   and tt.dealer_id = tm.dealer_id\n" );
			sql.append("   and vw.dealer_id = tt.dealer_id\n" );
			sql.append("   and cl.special_id = tt.special_id\n" );
			sql.append("   and sp.SPE_ID = tt.SPECIAL_ID\n" );
			sql.append("   and tt.status = tc.code_id\n" );
			DaoFactory.getsql(sql, "tt.APPLY_NO", apply_no, 2);
			DaoFactory.getsql(sql, "tm.dealer_code", dealerCode, 2);
			DaoFactory.getsql(sql, "tt.audit_DATE", CREATE_DATE_S, 3);
			DaoFactory.getsql(sql, "tt.audit_DATE", CREATE_DATE_D, 4);
			DaoFactory.getsql(sql, "tt.VIN", VIN, 2);
			DaoFactory.getsql(sql, "cl.tec_supply_code", supply_code_dealer, 2);
			DaoFactory.getsql(sql, "vw.ROOT_ORG_NAME", ROOT_ORG_NAME, 1);
			DaoFactory.getsql(sql, "tt.STATUS", STATUS, 1);
			DaoFactory.getsql(sql, "tt.SPECIAL_TYPE", SPECIAL_TYPE, 1);
			sql.append("   group by  tt.APPLY_NO,\n" );
			sql.append("       tm.dealer_code,\n" );
			sql.append("       tt.audit_DATE,\n" );
			sql.append("       tt.VIN,\n" );
			sql.append("       cl.supply_code_dealer,\n" );
			sql.append("       vw.ROOT_ORG_NAME,\n" );
			sql.append("       tt.STATUS,\n" );
			sql.append("       tt.SPECIAL_TYPE\n" );
			sql.append("       ) union  all\n" );
			sql.append("       (select  sum(tt.APPLY_AMOUNT) as  amount\n" );
			sql.append("  from tt_as_wr_special_apply tt,\n" );
			sql.append("       tm_dealer              tm ,\n" );
			sql.append("       vw_org_dealer_service       vw,\n" );
			sql.append("       tt_as_wr_spe_change_car cll,\n" );
			sql.append("       tt_as_wr_special sp,\n" );
			sql.append("        tc_code tc\n" );
			sql.append(" where 1 = 1\n" );
			sql.append("   and tt.dealer_id = tm.dealer_id\n" );
			sql.append("   and vw.dealer_id = tt.dealer_id\n" );
			sql.append("   and cll.special_id = tt.special_id\n" );
			sql.append("   and sp.SPE_ID = tt.SPECIAL_ID\n" );
			sql.append("   and tt.status = tc.code_id\n" );
			DaoFactory.getsql(sql, "tt.APPLY_NO", apply_no, 2);
			DaoFactory.getsql(sql, "tm.dealer_code", dealerCode, 2);
			DaoFactory.getsql(sql, "tt.audit_DATE", CREATE_DATE_S, 3);
			DaoFactory.getsql(sql, "tt.audit_DATE", CREATE_DATE_D, 4);
			DaoFactory.getsql(sql, "tt.VIN", VIN, 2);
			DaoFactory.getsql(sql, "cll.responsibility_code", supply_code_dealer, 2);
			DaoFactory.getsql(sql, "vw.ROOT_ORG_NAME", ROOT_ORG_NAME, 1);
			DaoFactory.getsql(sql, "tt.STATUS", STATUS, 1);
			DaoFactory.getsql(sql, "tt.SPECIAL_TYPE", SPECIAL_TYPE, 1);
			sql.append("   group by\n" );
			sql.append("       tt.APPLY_NO,\n" );
			sql.append("       tm.dealer_code,\n" );
			sql.append("       tt.audit_DATE,\n" );
			sql.append("       tt.VIN,\n" );
			sql.append("       cll.responsibility_code ,\n" );
			sql.append("       vw.ROOT_ORG_NAME,\n" );
			sql.append("       tt.STATUS,\n" );
			sql.append("       tt.SPECIAL_TYPE\n" );
			sql.append("       )) a");
		    Map map  =	this.pageQueryMap(sql.toString(), null, getFunName());
		    BigDecimal count = (BigDecimal) map.get("AMOUNT");
		    Double count1 = 0.0;
		    if ("".equals(count)||null==count) {
		    	return count1;
			}else {
				count1 = Double.parseDouble(count.toString());
			}
			return count1;
		}

		@SuppressWarnings("unchecked")
		public PageResult<Map<String, Object>> specialstatusTrackQuery(RequestWrapper request, AclUserBean loginUser,Integer pageSize, Integer currPage) {
			StringBuffer sql= new StringBuffer();
			sql.append("select s.*,tm.dealer_shortname,vw.root_org_name\n" );
			sql.append("  from tt_as_wr_special            s,\n" );
			sql.append("       tm_dealer                   tm,\n" );
			sql.append("       vw_org_dealer_service       vw\n" );
			sql.append(" where 1=1\n" );
			sql.append("   and tm.dealer_id = s.dealer_id\n" );
			sql.append("   and s.dealer_code = vw.root_dealer_code");
			DaoFactory.getsql(sql, "s.REPORT_DATE","2015-05-26 00:00:00", 3);
            DaoFactory.getsql(sql, "s.dealer_code", DaoFactory.getParam(request, "dealer_code"), 2);
            DaoFactory.getsql(sql, "tm.dealer_shortname", DaoFactory.getParam(request, "dealer_shortname"), 2);
            DaoFactory.getsql(sql, "s.vin", DaoFactory.getParam(request, "vin"), 2);
            DaoFactory.getsql(sql, "vw.root_org_name", DaoFactory.getParam(request, "root_org_name"), 2);
            DaoFactory.getsql(sql, "s.REPORT_DATE", DaoFactory.getParam(request, "beginTime"), 3);
            DaoFactory.getsql(sql, "s.REPORT_DATE", DaoFactory.getParam(request, "endTime"), 4);
            DaoFactory.getsql(sql, "s.TEC_AUDIT_DATE", DaoFactory.getParam(request, "auditbeginTime"), 3);
            DaoFactory.getsql(sql, "s.TEC_AUDIT_DATE", DaoFactory.getParam(request, "auditendTime"), 4);
            String status =  DaoFactory.getParam(request, "status");
            if (status.length()==8) {
            	DaoFactory.getsql(sql, "s.status", DaoFactory.getParam(request, "status"), 1);
			}else {
				DaoFactory.getsql(sql, "s.status", DaoFactory.getParam(request, "status"), 6);
			}
            sql.append("   order by s.SETTLEMENT_AUDIT_DATE desc ");
			return this.pageQuery(sql.toString(), null, getFunName(), pageSize, currPage);
		}
       /**
        * 技术部撤销审核
        * @param request
        * @param loginUser
        * @return
        */
		@SuppressWarnings("unchecked")
		public int CancelAudit(RequestWrapper request, AclUserBean loginUser) {
			//查询出该条数据
			StringBuffer sb = new StringBuffer();
			String id = DaoFactory.getParam(request, "id");
			sb.append("select * from tt_as_wr_special sp where 1=1  and (sp.status=20331009 or sp.status=20331010 )");
			DaoFactory.getsql(sb, "sp.spe_id", id, 1);
			List<TtAsWrSpecialPO> list = this.select(TtAsWrSpecialPO.class, sb.toString(), null);
			//修改
			int res =0;
			if (null!=list && list.size()>0) {
			TtAsWrSpecialPO SpecialPO = list.get(0);
			Double applymoney =  SpecialPO.getApplyMoney();
			TtAsWrSpecialPO specialPO3 = new TtAsWrSpecialPO();
			specialPO3.setSpeId(SpecialPO.getSpeId());
			TtAsWrSpecialPO specialPO2 = new TtAsWrSpecialPO();
			if (applymoney<800.0) {
				specialPO2.setStatus(Constant.SPE_STATUS_05);
			}else if (applymoney>=800.0) {
				specialPO2.setStatus(Constant.SPE_STATUS_07);
			}
			//日志记录
			TtAsComRecordPO recordPO = new TtAsComRecordPO();
			recordPO.setBizId(Long.valueOf(id));
			recordPO.setStatus(SpecialPO.getStatus());//撤销前状态
			recordPO.setCreateBy(loginUser.getUserId());
			recordPO.setCreateDate(new Date());
			recordPO.setId(DaoFactory.getPkId());
			recordPO.setRemark("特殊费用技术部撤销审核");
			
			this.insert(recordPO);//记录日志
		    res = this.update(specialPO3,specialPO2);
			}
			return res;
		}


}
