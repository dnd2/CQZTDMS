package com.infodms.yxdms.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.yxdms.entity.special.TtAsSpecialAmountRangePO;
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
public class GoodClaimDAO extends IBaseDao{

	private static GoodClaimDAO dao = new GoodClaimDAO();
	public static final GoodClaimDAO getInstance(){
		dao = (dao==null)?new GoodClaimDAO():dao;
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> specialDealerList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		String d=request.getParamValue("creatDate");
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*,vw.root_org_name,tm.dealer_shortname\n" );
		sb.append("  from tt_as_wr_Special t, tm_dealer tm, vw_data_by_vin v,vw_org_dealer_service vw\n" );
		sb.append(" where tm.dealer_id = t.dealer_id and t.dealer_id= vw.dealer_id\n" );
		sb.append("   and v.vin = t.vin \n");
		
		DaoFactory.getsql(sb, "t.DEALER_ID", loginUser.getDealerId(), 1);
		DaoFactory.getsql(sb, "t.APPLY_NO", request.getParamValue("APPLY_NO"), 2);
		DaoFactory.getsql(sb, "t.SETMENT_NO", request.getParamValue("SETMENT_NO"), 2);
		DaoFactory.getsql(sb, "t.STATUS", request.getParamValue("STATUS"), 1);
		if(d!=null && !"".equals(d)){
			sb.append(" and to_date(to_char(t.CREATE_DATE,'yyyy-mm-dd'),'yyyy-mm-dd') = to_date('"+d+"','yyyy-mm-dd') \n");
		}
		
		sb.append("   order by t.status asc,t.create_date desc \n");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public String getSpecialNo() {
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT 'STSY'|| TO_CHAR(SYSDATE,'YYMMDD')|| LPAD(SEQ_SPECIAL_NO.NEXTVAL,4,'0') AS SPECIAL_NO FROM DUAL");
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
		/*StringBuffer sb= new StringBuffer();
		sb.append("select t.maker_code,t.maker_shotname From tt_part_maker_define t where 1=1\n");*/
		StringBuffer sql= new StringBuffer();
		sql.append("select t.maker_code, t.maker_shotname\n" );
		sql.append("  From tt_part_maker_define t\n" );
		sql.append(" inner join tt_part_vender v\n" );
		sql.append("    on t.maker_id = v.vender_id\n" );
		sql.append(" inner join tt_part_define p\n" );
		sql.append("    on v.part_id = p.part_id\n" );
		
		sql.append(" where v.state=10041001 and t.state=10011001 and p.part_code = '62070410-B40-000'");		
		//sql.append(" where v.state=10041001 and t.state=10011001 and p.part_code = '"+request.getParamValue("old_code")+"'");
		
		DaoFactory.getsql(sql, "t.maker_code",DaoFactory.getParam(request, "maker_code"), 2);
		DaoFactory.getsql(sql, "t.maker_shotname",DaoFactory.getParam(request, "maker_shotname"), 2);
		PageResult<Map<String, Object>> list=pageQuery(sql.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public int saveOrUpdate(RequestWrapper request, AclUserBean loginUser) {
		String spe_id = DaoFactory.getParam(request, "spe_id");
		String is_claim = DaoFactory.getParam(request, "is_claim");
		String[] part_code_dealer = DaoFactory.getParams(request, "part_code_dealer");
		String[] part_name_dealer = DaoFactory.getParams(request, "part_name_dealer");
		String[] supply_code_dealer = DaoFactory.getParams(request, "supply_code_dealer");
		String[] supply_name_dealer = DaoFactory.getParams(request, "supply_name_dealer");
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
		String special_type = DaoFactory.getParam(request, "special_type");
		String identify = DaoFactory.getParam(request, "identify");
		String apply_money = DaoFactory.getParam(request, "apply_money");
		String[] fjids=request.getParamValues("fjid");
		
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
				if(!"".equals(claim_no)){
					spe.setClaimNo(claim_no);
				}
				
				if("0".equals(identify)){
					spe.setStatus(Constant.SPE_STATUS_01);
				}else{
					spe.setStatus(Constant.SPE_STATUS_02);
					spe.setReportBy(loginUser.getUserId());
					spe.setReportDate(new Date());
				}
				this.insert(spe);
				delAndReinsetFile(loginUser, fjids, specialId.toString());
				for(int i=0;i<part_code_dealer.length-1;i++){
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
					g.setSupplyCodeDealer(supply_code_dealer[i]);
					g.setSupplyNameDealer(supply_name_dealer[i]);
					g.setPartCodeDealer(part_code_dealer[i]);
					g.setPartNameDealer(part_name_dealer[i]);
					this.insert(g);
				}
				
			}else{//修改
				Long speId = Long.valueOf(spe_id);
				TtAsWrSpecialPO spe1=new TtAsWrSpecialPO();
				spe1.setSpeId(speId);
				
				TtAsWrSpecialPO spe2=new TtAsWrSpecialPO();
				spe2.setVin(vin);
				if(!"".equals(claim_no)){
					spe2.setClaimNo(claim_no);
				}
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
				//先删除
				TtAsWrSpeGoodwillClaimPO detpo=new TtAsWrSpeGoodwillClaimPO();
    	        detpo.setSpecialId(speId);
    	        this.delete(detpo);
    	        for(int i=0;i<part_code_dealer.length-1;i++){
					TtAsWrSpeGoodwillClaimPO g =new TtAsWrSpeGoodwillClaimPO();
					g.setClaimNo(claim_no);
					g.setJob(job);
					if(!"".equals(problem_date)){
						problem_date=problem_date.replaceAll("\\+", " ");
						g.setProblemDate(BaseUtils.getDate(problem_date, 3));
					}
					g.setSpecialId(speId);
					g.setId(DaoFactory.getPkId());
					g.setDealerContact(dealer_contact);
					g.setDealerPhone(dealer_phone);
					g.setEventTheme(event_theme);
					g.setComplainAdvice(complain_advice);
					g.setClaimNo(claim_no);
					g.setIsClaim(Integer.parseInt(is_claim));
					g.setSupplyCodeDealer(supply_code_dealer[i]);
					g.setSupplyNameDealer(supply_name_dealer[i]);
					g.setPartCodeDealer(part_code_dealer[i]);
					g.setPartNameDealer(part_name_dealer[i]);
					this.insert(g);
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
				this.delete("delete from tt_as_wr_Spe_Goodwill_claim t where t.special_id="+id, null);				
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> findSpeData(String id) {
		StringBuffer sb= new StringBuffer();
			sb.append("select *\n" );
			sb.append("  from Tt_As_Wr_Special t, tt_as_wr_Spe_Goodwill_claim g,vw_data_by_vin v,tm_dealer tm \n" );
			sb.append(" where t.spe_id = g.special_id and t.vin=v.vin and tm.dealer_id = t.dealer_id \n");
		DaoFactory.getsql(sb, "t.spe_id", id, 1);
		return this.pageQueryMap(sb.toString(), null, getFunName());
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findPartSupply(String id) {
		StringBuffer sb= new StringBuffer();
			sb.append("select *\n" );
			sb.append("  from tt_as_wr_Spe_Goodwill_claim g \n" );
			sb.append(" where 1=1 \n");
		DaoFactory.getsql(sb, "g.SPECIAL_ID", id, 1);
		return this.pageQuery(sb.toString(), null, getFunName());
	}
	public List<Map<String, Object>> specialRecord(String id) {
		StringBuffer sql= new StringBuffer();
		sql.append("select g.id,\n" );
		sql.append("       g.special_id,\n" );
		sql.append("       g.special_type,\n" );
		sql.append("       g.audit_record,\n" );
		sql.append("       TO_CHAR(g.audit_date, 'YYYY-MM-DD hh24:mi:ss') audit_date,\n" );
		sql.append("       g.audit_by,\n" );
		sql.append("       u.name,\n" );
		sql.append("       (SELECT c.code_desc FROM tc_code c where c.code_id=g.opera_ststus) as opera_ststus\n" );
		sql.append("  from tt_as_wr_special_record g left join tc_user u on g.audit_by=u.user_Id \n" );
		sql.append(" where 1=1 ");

		DaoFactory.getsql(sql, "g.SPECIAL_ID", id, 1);
		return this.pageQuery(sql.toString(), null, getFunName());
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
	public PageResult<Map<String, Object>> specialClaimSettlementList(TtAsSpecialAmountRangePO po,RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*,tm.dealer_name,tm.dealer_shortname,vw.root_org_name\n" );
		sb.append("  from tt_as_wr_Special t, tm_dealer tm, vw_data_by_vin v,vw_org_dealer_service vw\n" );
		sb.append(" where tm.dealer_id = t.dealer_id and t.dealer_code=vw.root_dealer_code\n" );
		sb.append("   and v.vin = t.vin and t.status=20331007 \n");
		sb.append("   and t.apply_money >= "+po.getAmountOffline()+"\n" );		
		if(StringUtils.isNotBlank((String) request.getParamValue("dealerId"))){
			sb.append("and t.dealer_id in ("+request.getParamValue("dealerId")+")");	
		}
		DaoFactory.getsql(sb, "t.APPLY_NO", request.getParamValue("APPLY_NO"), 2);
		DaoFactory.getsql(sb, "t.STATUS", request.getParamValue("STATUS"), 1);
		DaoFactory.getsql(sb, "t.REPORT_DATE", request.getParamValue("creatDate"), 31);
        sb.append(CommonUtils.getOrgDealerLimitSqlByPose("t", loginUser));
        sb.append("   order by t.status asc,t.REPORT_DATE desc\n" );
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> specialTecSupportList(TtAsSpecialAmountRangePO po,RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*,vw.root_org_name,tm.DEALER_NAME\n" );
		sb.append("  from tt_as_wr_Special t, tm_dealer tm, vw_data_by_vin v,vw_org_dealer_service vw\n" );
		sb.append(" where tm.dealer_id = t.dealer_id and t.dealer_id= vw.dealer_id\n" );
		sb.append("   and v.vin = t.vin\n" );
		sb.append("   and t.status = 20331002\n" );
		sb.append("   and t.apply_money >= "+po.getAmountOffline()+"\n" );		
		if(StringUtils.isNotBlank((String) request.getParamValue("dealerId"))){
			sb.append("and t.dealer_id in ("+request.getParamValue("dealerId")+")");	
		}
		DaoFactory.getsql(sb, "t.APPLY_NO", request.getParamValue("APPLY_NO"), 2);
		DaoFactory.getsql(sb, "t.STATUS", request.getParamValue("STATUS"), 1);
		DaoFactory.getsql(sb, "t.REPORT_DATE", request.getParamValue("creatDate"), 31);
        sb.append(CommonUtils.getOrgDealerLimitSqlByPose("t", loginUser));
        sb.append("   order by t.status asc,t.REPORT_DATE desc\n" );
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> specialTecSeSupportList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*,vw.root_org_name,tm.DEALER_NAME\n" );
		sb.append("  from tt_as_wr_Special t, tm_dealer tm, vw_data_by_vin v,vw_org_dealer_service vw\n" );
		sb.append(" where tm.dealer_id = t.dealer_id and t.dealer_id= vw.dealer_id\n" );
		sb.append("   and v.vin = t.vin\n" );
		sb.append("   and t.status not in (20331001)\n" );	
		if(StringUtils.isNotBlank((String) request.getParamValue("dealerId"))){
			sb.append("and t.dealer_id in ("+request.getParamValue("dealerId")+")");	
		}
		DaoFactory.getsql(sb, "t.APPLY_NO", request.getParamValue("APPLY_NO"), 2);
		DaoFactory.getsql(sb, "t.STATUS", request.getParamValue("STATUS"), 1);
		DaoFactory.getsql(sb, "t.REPORT_DATE", request.getParamValue("creatDate"), 31);
        sb.append(CommonUtils.getOrgDealerLimitSqlByPose("t", loginUser));
        sb.append("   order by t.status asc,t.REPORT_DATE desc\n" );
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> specialRegionalDirectorList(TtAsSpecialAmountRangePO po,RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*,vw.root_org_name,tm.dealer_name,tm.dealer_shortname\n" );
		sb.append("  from tt_as_wr_Special t, tm_dealer tm, vw_data_by_vin v,vw_org_dealer_service vw\n" );
		sb.append(" where tm.dealer_id = t.dealer_id and t.dealer_id= vw.dealer_id\n" );
		sb.append("   and v.vin = t.vin and t.status=20331005 \n");
        sb.append("   and t.apply_money >= "+po.getAmountOffline()+"\n" );		
		if(StringUtils.isNotBlank((String) request.getParamValue("dealerId"))){
			sb.append("and t.dealer_id in ("+request.getParamValue("dealerId")+")");	
		}
		DaoFactory.getsql(sb, "t.APPLY_NO", request.getParamValue("APPLY_NO"), 2);
		DaoFactory.getsql(sb, "t.STATUS", request.getParamValue("STATUS"), 1);
		DaoFactory.getsql(sb, "t.REPORT_DATE", request.getParamValue("creatDate"), 31);
        sb.append(CommonUtils.getOrgDealerLimitSqlByPose("t", loginUser));
        sb.append("   order by t.status asc,t.REPORT_DATE desc\n" );
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> specialRegionalManagerList(TtAsSpecialAmountRangePO po,RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*,vw.root_org_name,tm.DEALER_NAME,tm.dealer_shortname\n" );
		sb.append("  from tt_as_wr_Special t, tm_dealer tm, vw_data_by_vin v,vw_org_dealer_service vw\n" );
		sb.append(" where tm.dealer_id = t.dealer_id and t.dealer_id= vw.dealer_id\n" );
		sb.append("   and v.vin = t.vin and t.status=20331003\n");
		sb.append("   and t.apply_money >= "+po.getAmountOffline()+"\n" );		
		if(StringUtils.isNotBlank((String) request.getParamValue("dealerId"))){
			sb.append("and t.dealer_id in ("+request.getParamValue("dealerId")+")");	
		}
		DaoFactory.getsql(sb, "t.APPLY_NO", request.getParamValue("APPLY_NO"), 2);
		DaoFactory.getsql(sb, "t.STATUS", request.getParamValue("STATUS"), 1);
		DaoFactory.getsql(sb, "t.REPORT_DATE", request.getParamValue("creatDate"), 31);
        sb.append(CommonUtils.getOrgDealerLimitSqlByPose("t", loginUser));
        sb.append("   order by t.status asc,t.REPORT_DATE desc\n" );
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
		String type=DaoFactory.getParam(request, "type");//判断是几级审核
		String service_manager_deal=DaoFactory.getParam(request, "service_manager_deal");//审核意见
		String identify = DaoFactory.getParam(request, "identify");//判断是通过拒绝还是退回
		String[] spje=DaoFactory.getParams(request, "spje");
		String[] percent=DaoFactory.getParams(request, "bfb");
		String audit_money=DaoFactory.getParam(request, "audit_money");
		Long userId = loginUser.getUserId();
		try {
			if("1".equals(type)){//一级
				if("0".equals(identify)){//通过
					Long speId = Long.valueOf(spe_id);
					TtAsWrSpecialPO spe1=new TtAsWrSpecialPO();
					spe1.setSpeId(speId);				
					TtAsWrSpecialPO spe2=new TtAsWrSpecialPO();
					//审核
					spe2.setServiceManagerDeal(service_manager_deal);
					spe2.setAuditAmount(Double.parseDouble(audit_money));
					spe2.setManagerAuditBy(userId);
					spe2.setManagerAuditDate(new Date());
					spe2.setStatus(20331003);				
					this.update(spe1, spe2);
					//修改索赔金额
					List<Map<String, Object>> psList=dao.findPartSupply(spe_id);
	    	        for(int i=0;i<psList.size();i++){
	    	        	for(int j=0;j<spje.length;j++){
	    	        		if(i==j){
	    	        			TtAsWrSpeGoodwillClaimPO g =new TtAsWrSpeGoodwillClaimPO();
	    	        			g.setId(Long.parseLong(psList.get(i).get("ID").toString()));
	    	        			TtAsWrSpeGoodwillClaimPO upg =new TtAsWrSpeGoodwillClaimPO();
	    	        			upg.setClaimAmount(Double.parseDouble(spje[j]));
	    	        			upg.setPercent(Integer.parseInt(percent[j]));
	    						this.update(g,upg);
	    	        		}
	    	        	}
						
					}
					//审核日志
					this.insertAuditRecord(Constant.SPE_STATUS_03.toString(), userId, speId,service_manager_deal);
				}else if("1".equals(identify)){//驳回
					Long speId = Long.valueOf(spe_id);
					TtAsWrSpecialPO spe1=new TtAsWrSpecialPO();
					spe1.setSpeId(speId);				
					TtAsWrSpecialPO spe2=new TtAsWrSpecialPO();
						spe2.setServiceManagerDeal(service_manager_deal);
						spe2.setManagerAuditBy(userId);
						spe2.setManagerAuditDate(new Date());
						spe2.setStatus(20331004);
					this.update(spe1, spe2);
					//审核日志
					this.insertAuditRecord(Constant.SPE_STATUS_04.toString(), userId, speId,service_manager_deal);
				}
			}else if("2".equals(type)){//二级
				if("0".equals(identify)){//通过
					Long speId = Long.valueOf(spe_id);
					TtAsWrSpecialPO spe1=new TtAsWrSpecialPO();
					spe1.setSpeId(speId);				
					TtAsWrSpecialPO spe2=new TtAsWrSpecialPO();
					//审核
					spe2.setServiceManagerDeal(service_manager_deal);
					spe2.setAuditAmount(Double.parseDouble(audit_money));
					spe2.setManagerAuditBy(userId);
					spe2.setManagerAuditDate(new Date());
					spe2.setStatus(20331005);				
					this.update(spe1, spe2);
					//修改索赔金额
					List<Map<String, Object>> psList=dao.findPartSupply(spe_id);
	    	        for(int i=0;i<psList.size();i++){
	    	        	for(int j=0;j<spje.length;j++){
	    	        		if(i==j){
	    	        			TtAsWrSpeGoodwillClaimPO g =new TtAsWrSpeGoodwillClaimPO();
	    	        			g.setId(Long.parseLong(psList.get(i).get("ID").toString()));
	    	        			TtAsWrSpeGoodwillClaimPO upg =new TtAsWrSpeGoodwillClaimPO();
	    	        			upg.setClaimAmount(Double.parseDouble(spje[j]));
	    	        			upg.setPercent(Integer.parseInt(percent[j]));
	    						this.update(g,upg);
	    	        		}
	    	        	}
						
					}
					//审核日志
					this.insertAuditRecord(Constant.SPE_STATUS_05.toString(), userId, speId,service_manager_deal);
				}else if("1".equals(identify)){//驳回
					Long speId = Long.valueOf(spe_id);
					TtAsWrSpecialPO spe1=new TtAsWrSpecialPO();
					spe1.setSpeId(speId);				
					TtAsWrSpecialPO spe2=new TtAsWrSpecialPO();
						spe2.setServiceManagerDeal(service_manager_deal);
						spe2.setManagerAuditBy(userId);
						spe2.setManagerAuditDate(new Date());
						spe2.setStatus(20331006);
					this.update(spe1, spe2);
					//审核日志
					this.insertAuditRecord(Constant.SPE_STATUS_06.toString(), userId, speId,service_manager_deal);
				}
			}else if("3".equals(type)){//三级
				if("0".equals(identify)){//通过
					Long speId = Long.valueOf(spe_id);
					TtAsWrSpecialPO spe1=new TtAsWrSpecialPO();
					spe1.setSpeId(speId);				
					TtAsWrSpecialPO spe2=new TtAsWrSpecialPO();
					//审核
					spe2.setServiceManagerDeal(service_manager_deal);
					spe2.setAuditAmount(Double.parseDouble(audit_money));
					spe2.setManagerAuditBy(userId);
					spe2.setManagerAuditDate(new Date());
					spe2.setStatus(20331007);				
					this.update(spe1, spe2);
					//修改索赔金额
					List<Map<String, Object>> psList=dao.findPartSupply(spe_id);
	    	        for(int i=0;i<psList.size();i++){
	    	        	for(int j=0;j<spje.length;j++){
	    	        		if(i==j){
	    	        			TtAsWrSpeGoodwillClaimPO g =new TtAsWrSpeGoodwillClaimPO();
	    	        			g.setId(Long.parseLong(psList.get(i).get("ID").toString()));
	    	        			TtAsWrSpeGoodwillClaimPO upg =new TtAsWrSpeGoodwillClaimPO();
	    	        			upg.setClaimAmount(Double.parseDouble(spje[j]));
	    	        			upg.setPercent(Integer.parseInt(percent[j]));
	    						this.update(g,upg);
	    	        		}
	    	        	}
						
					}
					//审核日志
					this.insertAuditRecord(Constant.SPE_STATUS_07.toString(), userId, speId,service_manager_deal);
				}else if("1".equals(identify)){//驳回
					Long speId = Long.valueOf(spe_id);
					TtAsWrSpecialPO spe1=new TtAsWrSpecialPO();
					spe1.setSpeId(speId);				
					TtAsWrSpecialPO spe2=new TtAsWrSpecialPO();
						spe2.setServiceManagerDeal(service_manager_deal);
						spe2.setManagerAuditBy(userId);
						spe2.setManagerAuditDate(new Date());
						spe2.setStatus(20331008);
					this.update(spe1, spe2);
					//审核日志
					this.insertAuditRecord(Constant.SPE_STATUS_08.toString(), userId, speId,service_manager_deal);
				}
			}else if("4".equals(type)){//四级
				if("0".equals(identify)){//通过
					Long speId = Long.valueOf(spe_id);
					TtAsWrSpecialPO spe1=new TtAsWrSpecialPO();
					spe1.setSpeId(speId);				
					TtAsWrSpecialPO spe2=new TtAsWrSpecialPO();
					//审核
					spe2.setServiceManagerDeal(service_manager_deal);
					spe2.setAuditAmount(Double.parseDouble(audit_money));
					spe2.setManagerAuditBy(userId);
					spe2.setManagerAuditDate(new Date());
					spe2.setStatus(20331014);				
					this.update(spe1, spe2);
					//修改索赔金额
					List<Map<String, Object>> psList=dao.findPartSupply(spe_id);
	    	        for(int i=0;i<psList.size();i++){
	    	        	for(int j=0;j<spje.length;j++){
	    	        		if(i==j){
	    	        			TtAsWrSpeGoodwillClaimPO g =new TtAsWrSpeGoodwillClaimPO();
	    	        			g.setId(Long.parseLong(psList.get(i).get("ID").toString()));
	    	        			TtAsWrSpeGoodwillClaimPO upg =new TtAsWrSpeGoodwillClaimPO();
	    	        			upg.setClaimAmount(Double.parseDouble(spje[j]));
	    	        			upg.setPercent(Integer.parseInt(percent[j]));
	    						this.update(g,upg);
	    	        		}
	    	        	}
						
					}
					//审核日志
					this.insertAuditRecord(Constant.SPE_STATUS_14.toString(), userId, speId,service_manager_deal);
				}else if("1".equals(identify)){//驳回
					Long speId = Long.valueOf(spe_id);
					TtAsWrSpecialPO spe1=new TtAsWrSpecialPO();
					spe1.setSpeId(speId);				
					TtAsWrSpecialPO spe2=new TtAsWrSpecialPO();
						spe2.setServiceManagerDeal(service_manager_deal);
						spe2.setManagerAuditBy(userId);
						spe2.setManagerAuditDate(new Date());
						spe2.setStatus(20331013);
					this.update(spe1, spe2);
					//审核日志
					this.insertAuditRecord(Constant.SPE_STATUS_13.toString(), userId, speId,service_manager_deal);
				}
			}
			
				if("2".equals(identify)){//拒绝
				Long speId = Long.valueOf(spe_id);
				TtAsWrSpecialPO spe1=new TtAsWrSpecialPO();
				spe1.setSpeId(speId);				
				TtAsWrSpecialPO spe2=new TtAsWrSpecialPO();
					spe2.setServiceManagerDeal(service_manager_deal);
					spe2.setManagerAuditBy(userId);
					spe2.setManagerAuditDate(new Date());
					spe2.setStatus(20331015);
				this.update(spe1, spe2);
				//审核日志
				this.insertAuditRecord(Constant.SPE_STATUS_15.toString(), userId, speId,service_manager_deal);
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
	private void insertAuditRecord(String status, Long userId, Long speId,String c) {
		TtAsWrSpecialRecordPO po=new TtAsWrSpecialRecordPO();
		po.setAuditBy(userId);
		po.setAuditDate(new Date());
		po.setOperaStstus(Integer.parseInt(status));
		po.setId(DaoFactory.getPkId());
		po.setSpecialId(speId);
		po.setAuditRecord(c);
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
