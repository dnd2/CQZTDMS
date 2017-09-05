package com.infodms.yxdms.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivityProjectPO;
import com.infodms.dms.po.TtAsWrApplicationExtPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrNetitemPO;
import com.infodms.dms.po.TtAsWrOutrepairPO;
import com.infodms.yxdms.dao.ClaimDAO;
import com.infodms.yxdms.service.ClaimService;
import com.infodms.yxdms.utils.BaseUtils;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class ClaimServiceImpl extends ClaimDAO implements ClaimService{

	public PageResult<Map<String, Object>> pdiManageList(AclUserBean loginUser,RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.pdiManageList(loginUser,request,pageSize,currPage);
	}

	public int pdiAddSure(AclUserBean loginUser, RequestWrapper request) {
		return super.pdiAddSure(loginUser,request);
	}

	public Map<String, Object> pdiView(AclUserBean loginUser,RequestWrapper request) {
		return super.pdiView(loginUser,request);
	}

	public int pdiDelete(RequestWrapper request) {
		return super.pdiDelete(request);
	}

	public String checkPdiByVin(RequestWrapper request) {
		return super.checkPdiByVin(request);
	}

	public PageResult<Map<String, Object>> keepFitManageList(AclUserBean loginUser, RequestWrapper request, Integer pageSize,Integer currPage) {
		return super.keepFitManageList(loginUser,request,pageSize,currPage);
	}

	public int keepFitDelete(RequestWrapper request) {
		return super.keepFitDelete(request);
	}

	public int keepFitCancel(RequestWrapper request) {
		return super.keepFitCancel(request);
	}

	public int keepFitAddSure(AclUserBean loginUser, RequestWrapper request) {
		return super.keepFitAddSure(loginUser,request);
	}

	public PageResult<Map<String, Object>> findRoKeepFit(AclUserBean loginUser,RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.findRoKeepFit(loginUser,request,pageSize,currPage);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findLaboursById(RequestWrapper request) {
		return pageQuery("select l.*  from Tt_As_Wr_Labouritem l where l.id="+DaoFactory.getParam(request, "id"), null,getFunName());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findPartsById(RequestWrapper request) {
		return pageQuery("select p.* from Tt_As_Wr_Partsitem p where p.id="+DaoFactory.getParam(request, "id"), null,getFunName());
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> findClaimPoById(RequestWrapper request) {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.*,(select max(d.audit_remark) from Tt_As_Wr_App_Audit_Detail d where d.audit_date = (select max(d.audit_date) from Tt_As_Wr_App_Audit_Detail d where d.claim_id = a.id)  and d.claim_id=a.id ) as audit_remark,r.package_name,tm.dealer_shortname as shortname from Tt_As_Wr_Application a,\n ");
		sb.append("tt_as_repair_order r,tm_dealer tm  where a.ro_no=r.ro_no(+) and r.dealer_id=tm.dealer_id(+) and  a.id="+DaoFactory.getParam(request, "id"));
		return pageQueryMap(sb.toString(), null,getFunName());
	}

	public PageResult<Map<String, Object>> normalManageList(AclUserBean loginUser, RequestWrapper request, Integer pageSize,Integer currPage) {
		return super.normalManageList(loginUser,request,pageSize,currPage);
	}

	public List<FsFileuploadPO> queryAttById(String ywzj) {
		return super.queryAttById(ywzj);
	}

	public PageResult<Map<String, Object>> findRoBaseInfo(AclUserBean loginUser, RequestWrapper request, Integer pageSize,Integer currPage) {
		return super.findRoBaseInfo(loginUser,request,pageSize,currPage);
	}

	public PageResult<Map<String, Object>> addPartNormal(AclUserBean loginUser,RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.addPartNormal(loginUser,request,pageSize,currPage);
	}

	public int normalAddSure(AclUserBean loginUser, RequestWrapper request) {
		return super.normalAddSure(loginUser,request);
	}

	public PageResult<Map<String, Object>> supplierCodeByPartCode(AclUserBean loginUser,RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.supplierCodeByPartCode(loginUser,request,pageSize,currPage);
	}

	@SuppressWarnings("unchecked")
	public int normalDelete(RequestWrapper request) {
		int res=1;
		try {
			String claim_id = DaoFactory.getParam(request, "id");
			if(!"".equals(claim_id)){
				//====回滚配件的用的标示(根据配件的)
				StringBuffer sb= new StringBuffer();
				sb.append("select p.real_part_id,a.ro_no from tt_as_wr_application a,Tt_As_Wr_Partsitem p  where a.id=p.id(+) and a.id="+claim_id);
				List<Map<String,Object>> pageQuery = super.pageQuery(sb.toString(), null, getFunName());
				if(DaoFactory.checkListNull(pageQuery)){
					for (Map<String, Object> map : pageQuery) {
						StringBuffer sbUpdate= new StringBuffer();
						String ro_no = BaseUtils.checkNull(map.get("RO_NO"));
						String real_part_id = BaseUtils.checkNull(map.get("REAL_PART_ID"));
						sbUpdate.append("update tt_as_ro_repair_part p\n" );
						sbUpdate.append("   set p.is_use = null\n" );
						sbUpdate.append(" where p.ro_id = (select o.id from tt_as_repair_order o where o.ro_no = '"+ro_no+"')\n" );
						sbUpdate.append("   and p.real_part_id = '"+real_part_id+"'");
						this.update(sbUpdate.toString(), null);
					}
				}
				//=============
				this.delete("delete  from tt_as_wr_partsitem t where t.id="+claim_id, null);
				this.delete("delete  from tt_as_wr_labouritem t where t.id="+claim_id, null);
				this.delete("delete  from tt_as_wr_netitem t where t.id="+claim_id, null);
				this.delete("delete  from tt_claim_accessory_dtl d where d.claim_no=(select a.claim_no from tt_as_wr_application a where a.id="+claim_id+")", null);
				this.delete("delete  from tt_as_wr_compensation_app c where c.claim_no=(select a.claim_no from tt_as_wr_application a where a.id="+claim_id+")", null);
				this.delete("delete  from Tt_As_Wr_Outrepair_money t where t.id="+claim_id, null);
				this.delete("delete  from Tt_As_Wr_Outrepair t where t.id="+claim_id, null);
				this.update("update tt_as_repair_order o set o.free_ro=null where o.ro_no=(select a.free_ro from Tt_As_Wr_Application a where a.id="+claim_id+")", null);
				this.delete("delete  from tt_as_wr_application t where t.id="+claim_id, null);
				
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}

	public Map<String, Object> findoutrepairmoney(RequestWrapper request) {
		return super.findoutrepairmoney(request);
	}

	public Map<String, Object> findoutrepair(RequestWrapper request) {
		return super.findoutrepair(request);
	}

	public Map<String, Object> findComPoById(RequestWrapper request) {
		return super.findComPoById(request);
	}

	public List<Map<String, Object>> findAccPoById(RequestWrapper request) {
		return super.findAccPoById(request);
	}

	public int normalAudit(AclUserBean loginUser, RequestWrapper request) {
		return super.normalAudit(loginUser,request);
	}

	public PageResult<Map<String, Object>> findYsqBaseInfo(AclUserBean loginUser, RequestWrapper request, Integer pageSize,Integer currPage) {
		return super.findYsqBaseInfo(loginUser,request,pageSize,currPage);
	}

	public Map<String, Object> queryYsqDataById(AclUserBean loginUser,RequestWrapper request) {
		return super.queryYsqDataById(loginUser,request);
	}

	public Map<String, Object> ysqPartdataById(AclUserBean loginUser,RequestWrapper request) {
		return super.ysqPartdataById(loginUser,request);
	}

	public String checkClaim(RequestWrapper request,AclUserBean loginUser) {
		return super.checkClaim(request,loginUser);
	}

	public String pdiAddCheck(String param, RequestWrapper request,AclUserBean loginUser) {
		return super.pdiAddCheck(param,request,loginUser);
	}

	public int normalReportSubmit(String id) {
		return super.normalReportSubmit(id);
	}
	public PageResult<Map<String, Object>> ClaimAbandonedQuery(AclUserBean loginUser, RequestWrapper request, Integer pageSize,Integer currPage) {
		return super.ClaimAbandonedQuery(loginUser,request,pageSize,currPage);
	}

	public List<TtAsWrOutrepairPO> getOutDetail(String id) {
		return super.getOutDetail(id);
	}

	public List<TtAsWrNetitemPO> getOther(String id) {
		return super.getOther(id);
	}

	public List<TtAsActivityPO> getADetail(String id) {
		return super.getADetail(id);
	}

	public List<TtAsWrApplicationExtPO> getBaseBean(String id) {
		return super.getBaseBean( id) ;
	}

	public List<TtAsWrApplicationExtPO> getDetail(String id) {
		
		return super.getDetail(id);
	}

	public String getTimes(String vin) {
		return super.getTimes(vin);
	}

	public List<TtAsWrApplicationExtPO> getbaoy(String vin) {
		return super.getbaoy(vin);
	}

	public List<Map<String, Object>> getclaimAccessoryDtl(String claimNo) {
		return super.getclaimAccessoryDtl(claimNo);
	}

	public List<TtAsWrApplicationPO> selectttAsWrApplication(TtAsWrApplicationPO tawaPo) {
		return super.selectttAsWrApplication( tawaPo);
	}

	public List<Map<String, Object>> barcodePrintDoGet(RequestWrapper request) {
		return super.barcodePrintDoGet( request) ;
	}

	public void updatePrint(String dtlIds) {
		super.barcodePrintDoGet( dtlIds) ;
	}

	public List<TtAsActivityProjectPO> selectTtAsActivityProject(TtAsActivityProjectPO jp) {
		return super.selectTtAsActivityProject(jp);
	}

	public int updateprodutercode(RequestWrapper request) {
		return super.updateprodutercode(request);
	}

	public List<Map<String, Object>> showAllRecords(String id) {
		return super.showAllRecords(id);
	}

	public Map<String, Object> findamount(AclUserBean loginUser, RequestWrapper request) {
		return super.findamount(loginUser,request);
	}

	public void exportkeepFitManageList(PageResult<Map<String, Object>> list,ActionContext act, AclUserBean loginUser) {
		try {
		String[] head={"索赔单号","索赔类型","修改次数","退回次数","VIN","审核金额","车主","建单时间","申请状态"};
		List<Map<String, Object>> records = list.getRecords();
		List params=new ArrayList();
		if(records!=null &&records.size()>0){
			for (Map<String, Object> map : records) {
				String[] detail=new String[9];
				detail[0]=BaseUtils.checkNull(map.get("CLAIM_NO"));
				detail[1]=this.getTypeDesc(BaseUtils.checkNull(map.get("CLAIM_TYPE")));            
				detail[2]=BaseUtils.checkNull(map.get("SUBMIT_TIMES"));
				detail[3]=BaseUtils.checkNull(map.get("BACK_TIMES"));
				detail[4]=BaseUtils.checkNull(map.get("VIN"));
				detail[5]=BaseUtils.checkNull(map.get("BALANCE_AMOUNT"));
				detail[6]=BaseUtils.checkNull(map.get("OWNER_NAME"));
				detail[7]=BaseUtils.checkNull(map.get("CREATE_DATE"));
				detail[8]= this.getTypeDesc(BaseUtils.checkNull(map.get("STATUS")));  
				params.add(detail);
			}
		}
		    String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "保养索赔单数据导出"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	public void exportnormalManageList(PageResult<Map<String, Object>> list,ActionContext act, AclUserBean loginUser) {
		try {
			String[] head={"索赔单号","索赔类型","结算基地","修改次数","退回次数","VIN","审核金额","车主","建单时间","申请状态"};
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[10];
					detail[0]=BaseUtils.checkNull(map.get("CLAIM_NO"));
					detail[1]=this.getTypeDesc(BaseUtils.checkNull(map.get("CLAIM_TYPE")));            
					detail[2]=this.getTypeDesc(BaseUtils.checkNull(map.get("BALANCE_YIELDLY"))); 
					detail[3]=BaseUtils.checkNull(map.get("SUBMIT_TIMES"));
					detail[4]=BaseUtils.checkNull(map.get("BACK_TIMES"));
					detail[5]=BaseUtils.checkNull(map.get("VIN"));
					detail[6]=BaseUtils.checkNull(map.get("BALANCE_AMOUNT"));
					detail[7]= BaseUtils.checkNull(map.get("OWNER_NAME"));  
					detail[8]= BaseUtils.checkNull(map.get("CREATE_DATE"));  
					detail[9]= this.getTypeDesc(BaseUtils.checkNull(map.get("STATUS")));  
					params.add(detail);
				}
			}
			    String systemDateStr = BaseUtils.getSystemDateStr();
				BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "一般索赔单数据导出"+systemDateStr+".xls", "导出数据",null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
	}

	public void exportpdiManageList(PageResult<Map<String, Object>> list,ActionContext act, AclUserBean loginUser) {
		try {
			String[] head={"索赔单号","索赔类型","修改次数","退回次数","VIN","审核金额","是否完好","建单时间","申请状态"};
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			String is_agree="";
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[9];
					detail[0]=BaseUtils.checkNull(map.get("CLAIM_NO"));
					detail[1]=this.getTypeDesc(BaseUtils.checkNull(map.get("CLAIM_TYPE")));            
					detail[2]=BaseUtils.checkNull(map.get("SUBMIT_TIMES"));
					detail[3]=BaseUtils.checkNull(map.get("BACK_TIMES"));
					detail[4]=BaseUtils.checkNull(map.get("VIN"));
					detail[5]=BaseUtils.checkNull(map.get("BALANCE_AMOUNT"));
					BigDecimal is_agrees=(BigDecimal) map.get("IS_AGREE");
					if(null==is_agrees || "".equals(is_agrees) ||"1".equals(is_agrees.toString())){
			    		   is_agree ="是";
			    	  }else{
			    		   is_agree ="否";
			    	  }
					detail[6]=BaseUtils.checkNull(is_agree);
					detail[7]=BaseUtils.checkNull(map.get("CREATE_DATE"));
					detail[8]= this.getTypeDesc(BaseUtils.checkNull(map.get("STATUS")));   
					params.add(detail);
				}
			}
			    String systemDateStr = BaseUtils.getSystemDateStr();
				BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "PDI数据导出"+systemDateStr+".xls", "导出数据",null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
	}

	public int checkpartCode(AclUserBean loginUser, RequestWrapper request) {
		return super.checkpartCode(loginUser,request);
	}

	public int updatenewpartcode(AclUserBean loginUser, RequestWrapper request) {
		return super.updatenewpartcode(loginUser,request);
	}

	public PageResult<Map<String, Object>> viewbackaduitList(AclUserBean loginUser, RequestWrapper request, Integer pageSize,Integer currPage) {
		return super.viewbackaduitList( loginUser,  request,  pageSize, currPage);
	}

	public List<Map<String, Object>> findCarsystemById(RequestWrapper request) {
		return super.findCarsystemById(request);
	}

	public int updateIsReturnById(AclUserBean loginUser, RequestWrapper request) {
		return super.updateIsReturnById( loginUser,  request);
	}

	public List<Map<String, Object>> findCarsystemByvin(AclUserBean loginUser,RequestWrapper request) {
		return super.findCarsystemByvin( loginUser, request);
	}

	public Map<String, Object> findYsqPoById(RequestWrapper request) {
		return super.findYsqPoById(request);
	}

	
}
