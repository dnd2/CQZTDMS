package com.infodms.yxdms.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.yxdms.dao.MainTainDAO;
import com.infodms.yxdms.entity.maintain.TtAsYsqPartPO;
import com.infodms.yxdms.service.MainTainService;
import com.infodms.yxdms.utils.BaseUtils;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class MainTainServiceImpl extends MainTainDAO implements MainTainService{

	public PageResult<Map<String, Object>> keepFitTemplateData(RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.keepFitTemplateData(request,pageSize,currPage);
	}

	public PageResult<Map<String, Object>> addLabour(RequestWrapper request,Integer pageSize, Integer currPage) {
		return super.addLabour(request,pageSize,currPage);
	}

	public PageResult<Map<String, Object>> addPart(RequestWrapper request,
			Integer pageSize, Integer currPage) {
		return super.addPart(request,pageSize,currPage);
	}

	public int addMainTainCommit(RequestWrapper request, AclUserBean loginUser) {
		return super.addMainTainCommit(request,loginUser);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findLaboursById(RequestWrapper request) {
		return  super.pageQuery("select t.* from tt_AS_keep_Fit_labour t where t.keep_fit_id="+DaoFactory.getParam(request, "id"),null,getFunName());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findPartsById(RequestWrapper request) {
		return  super.pageQuery("select t.* from Tt_As_Keep_Fit_Part t where t.keep_fit_id="+DaoFactory.getParam(request, "id"),null,getFunName());
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> findKeepFit(RequestWrapper request) {
		Map<String, Object> ps = pageQueryMap("select * from tt_AS_keep_Fit_Template t,vw_material_group g where t.PACKAGE_CODE=g.PACKAGE_CODE(+) and  t.id="+DaoFactory.getParam(request, "id"), null, getFunName());
		return ps;
	}

	@SuppressWarnings("unchecked")
	public int publish(RequestWrapper request) {
		int res=1;
		try {
			this.update("update tt_AS_keep_Fit_Template t set t.status=18041002 where t.id="+DaoFactory.getParam(request, "id"),null);
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public int deleteMainTain(RequestWrapper request) {
		int res=1;
		try {
			this.update("update tt_AS_keep_Fit_Template t set t.is_del=1  where t.id="+DaoFactory.getParam(request, "id"),null);
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public Map<String,Object> checkIsFirst(RequestWrapper request) {
		String vin = DaoFactory.getParam(request, "vin");
		String in_mileage = DaoFactory.getParam(request, "in_mileage");
		Integer in_mileage_int =   Integer.parseInt(in_mileage);
		Map<String,Object> map = this.pageQueryMap("select nvl(t.free_times,0) as free_times ,ceil(sysdate - (select s.invoice_date from tt_dealer_actual_sales s where s.vehicle_id=t.vehicle_id)) as days,nvl(t.mileage,0) as mileage from  tm_vehicle t where t.vin='"+vin+"'", null, getFunName());
		BigDecimal free_times_temp = (BigDecimal) map.get("FREE_TIMES");
		BigDecimal mileage_temp = (BigDecimal) map.get("MILEAGE");
		BigDecimal days_temp = (BigDecimal)(map.get("DAYS"));
		int free_times = free_times_temp.intValue();
		Integer mileage = Integer.parseInt(mileage_temp.toString());
		if (in_mileage_int>mileage) {
			mileage=in_mileage_int;
		}
//		mileage =Integer.valueOf(in_mileage)>=mileage ? Integer.valueOf(in_mileage):mileage;
		int days = days_temp.intValue();
		Map<String,Object> mapParam =new HashMap<String, Object>();
		mapParam.put("free_times", free_times);
		if(mileage>5000 || days>=135){//加超期135天或者5000KM只能自费保养
			mapParam.put("isFree", "false");
		}else{
			mapParam.put("isFree", "true");
		}
		return mapParam;
	}


	public PageResult<Map<String, Object>> findLabPartRelation(RequestWrapper request, Integer pageSize, Integer currPage) {
		String part_id = DaoFactory.getParam(request, "part_id");
		String part_code = DaoFactory.getParam(request, "part_code");
		return super.findLabPartRelation(part_id,part_code,pageSize,currPage);
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> findaddLabour(RequestWrapper request, Integer pageSize, Integer currPage) {
		String part_id = DaoFactory.getParam(request, "part_id");
		String part_code = DaoFactory.getParam(request, "part_code");
		List<Map<String, Object>> list = this.pageQuery(" select a.lab_code from tt_AS_lab_part_relation a where a.part_id='"+part_id+"' and a.part_code='"+part_code+"'", null, getFunName());
		StringBuffer sb =new StringBuffer();
		if(DaoFactory.checkListNull(list)){
			for (Map<String, Object> map : list) {
				String lab_code = BaseUtils.checkNull(map.get("LAB_CODE"));
				sb.append(lab_code+",");
			}
		}
		return super.findaddLabour(pageSize,currPage,sb.toString());
	}


	public int insertRalation(RequestWrapper request, AclUserBean loginUser) {
		String str = DaoFactory.getParam(request, "str");    
		String part_code = DaoFactory.getParam(request, "part_code");
		String part_id = DaoFactory.getParam(request, "part_id");
		return super.insertRalation(str,loginUser,part_code,part_id);
	}

	@SuppressWarnings("unchecked")
	public int statusDel(RequestWrapper request) {
		int res=1;
		try {
			this.delete("delete from TT_AS_LAB_PART_RELATION t where t.id="+DaoFactory.getParam(request, "id"), null);
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}

	public PageResult<Map<String, Object>> ysqPartData(RequestWrapper request,Integer pageSize, Integer currPage) {
		return super.ysqPartData(request,pageSize,currPage);
	}

	public PageResult<Map<String, Object>> addYsqPartData(RequestWrapper request, Integer pageSize, Integer currPage) {
		String part_name = DaoFactory.getParam(request, "part_name");
		String part_code = DaoFactory.getParam(request, "part_code");
		return super.addYsqPartData(part_name,part_code,pageSize,currPage);
	}

	public int insertYsqPart(RequestWrapper request,AclUserBean loginUser) {
		return super.insertYsqPart(request,loginUser);
	}

	public PageResult<Map<String, Object>> emergencyMainTain(RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.emergencyMainTain(request,pageSize,currPage);
	}

	public int addEmergency(RequestWrapper request, AclUserBean loginUser) {
		return super.addEmergency(request,loginUser);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> viewEmergency(RequestWrapper request,String id) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.* from tt_as_emergency_MainTain t where 1=1 and t.is_delete is null ");
		DaoFactory.getsql(sb, "t.borrow_person", DaoFactory.getParam(request, "borrow_person"), 2);
		DaoFactory.getsql(sb, "t.consignee_person", DaoFactory.getParam(request, "consignee_person"), 2);
		DaoFactory.getsql(sb, "t.apply_dept", DaoFactory.getParam(request, "apply_dept"), 2);
		DaoFactory.getsql(sb, "t.id",id, 1);
		Map<String,Object> map = this.pageQueryMap(sb.toString(), null, getFunName());
		return map;
	}

	public PageResult<Map<String, Object>> mailList(RequestWrapper request,AclUserBean loginUser,Integer pageSize, Integer currPage) {
		return super.mailList(request,loginUser,pageSize,currPage);
	}

	public int addMailListSure(RequestWrapper request, AclUserBean loginUser) {
		return super.addMailListSure(request,loginUser);
	}

	public List findTtasrolabour(String trim) {
		return super.findTtasrolabour(trim);
	}

	public List findTtaswrwrlabinfo(String trim) {
		return super.findTtaswrwrlabinfo(trim);
	}

	public void insertlabpartRelation(RequestWrapper request,AclUserBean loginUser) {
		 super.insertlabpartRelation(request,loginUser);
	}

	@SuppressWarnings("unchecked")
	public void downExecl(ResponseWrapper response, RequestWrapper request,AclUserBean loginUser) {
			try {
				List<List<Object>> list = new ArrayList<List<Object>>();
				String[] listHead = new String[2];
				listHead[0]="配件代码";
				listHead[1]="工时代码";
				List list1=new ArrayList();
				String[]detail=new String[2];
				detail[0]="41330020-B40-B00";
				detail[1]="11010010101";
				list1.add(detail);
		    	com.infodms.dms.actions.claim.basicData.ToExcel.toExceUtil(response, request, listHead, list1,"配件工时维护关联维护模板.xls");
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}

	public PageResult<Map<String, Object>> queryPartCode(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.queryPartCode( request,  loginUser,  pageSize, currPage);
	}

	@SuppressWarnings("unchecked")
	public int delYsqPart(String id) {
		int res=-1;
		TtAsYsqPartPO p =new TtAsYsqPartPO();
		p.setId(Long.valueOf(id));
		List<TtAsYsqPartPO> select = this.select(p);
		p = select.get(0);
		String partCode = p.getPartCode();
		List<Map<String, Object>> pageQuery = this.pageQuery("select * from Tt_As_Wr_Ysq y where y.part_code = '"+partCode+"' and y.is_end<>-1", null, getFunName());
		if(DaoFactory.checkListNull(pageQuery)){
			res=-1;
		}else{
			this.delete(p);
			res=1;
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public int delYsqPart1(RequestWrapper request, AclUserBean loginUser) {
		return super.delYsqPart1(request,loginUser);
		
	}

	public int insertoldoutpart(RequestWrapper request, AclUserBean loginUser) {
		return super.insertoldoutpart(request,loginUser);
	}

	public PageResult<Map<String, Object>> queryWinterMaintenancelist(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return  super.queryWinterMaintenancelist( request,  loginUser,  pageSize, currPage);
	}

	public PageResult<Map<String, Object>> queryDealer(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage) {
		return super.queryDealer( request, loginUser,  pageSize,  currPage);
	}

	public String insertWinterMaintenDealer(RequestWrapper request,AclUserBean loginUser) {
		return super.insertWinterMaintenDealer( request, loginUser);
	}

	public PageResult<Map<String, Object>> queryWinterDetail(RequestWrapper request, AclUserBean loginUser) {
		return super.queryWinterDetail( request,  loginUser);
	}

	public String UpdateWinterMainten(RequestWrapper request, AclUserBean loginUser) {
		return super.UpdateWinterMainten( request,  loginUser);
	}

	public List<Map<String, Object>> winterMaintenView(RequestWrapper request,AclUserBean loginUser) {
		return super.winterMaintenView( request, loginUser) ;
	}

	public PageResult<Map<String, Object>> querychoosepageCode(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.querychoosepageCode( request,  loginUser,  pageSize, currPage);
	}

	public PageResult<Map<String, Object>> queryConfiguration(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.queryConfiguration( request,  loginUser,  pageSize, currPage);
	}

	public PageResult<Map<String, Object>> queryConfigurationBycode(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.queryConfigurationBycode( request,  loginUser,  pageSize, currPage);
	}

	public PageResult<Map<String, Object>> queryConfigurationById(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.queryConfigurationById( request,  loginUser,  pageSize, currPage);
	}

	public List<Map<String, Object>> queryWinterById(String str,AclUserBean loginUser) {
		return super.queryWinterById( str, loginUser);
	}

	public List<Map<String, Object>> queryWinterById(RequestWrapper request,AclUserBean loginUser) {
		return super.queryWinterById( request, loginUser);
	}

}
