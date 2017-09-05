package com.infodms.yxdms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.yxdms.dao.ActivityDAO;
import com.infodms.yxdms.service.ActivityService;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class ActivityServiceImpl extends ActivityDAO implements ActivityService{

	public PageResult<Map<String, Object>> ActivityVinManageData(RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.ActivityVinManageData(request,pageSize,currPage);
	}

	public PageResult<Map<String, Object>> ActivityVinData(RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.ActivityVinData(request,pageSize,currPage);
	}

	public int bntDelAll(RequestWrapper request) {
		return super.bntDelAll(request);
	}

	public void expotActivityVinData(ActionContext act,PageResult<Map<String, Object>> list) {
		super.expotActivityVinData(act,list);
	}

	public PageResult<Map<String, Object>> openSubject(RequestWrapper request,Integer pageSize, Integer currPage) {
		return super.openSubject(request,pageSize,currPage);
	}

	public PageResult<Map<String, Object>> vinByDetail(Long dealerId,RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.vinByDetailData(dealerId,request, pageSize, currPage);
	}

	public PageResult<Map<String, Object>> vinDetailByCount(Long dealerId,RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.vinDetailByCount(dealerId,request, pageSize, currPage);
	}

	public PageResult<Map<String, Object>> vinDetailByautomobile(Long dealerId, RequestWrapper request, Integer pageSize,Integer currPage) {
		return super.vinDetailByautomobile(dealerId,request, pageSize, currPage);
	}

	public PageResult<Map<String, Object>> vinDetailByCountFactory(String dealer_code, RequestWrapper request, Integer pageSize,Integer currPage) {
		return super.vinDetailByCountFactory(dealer_code,request, pageSize, currPage);
	}

	public PageResult<Map<String, Object>> openActivity(RequestWrapper request,Integer pageSize, Integer currPage) {
		return super.openActivity(request,pageSize,currPage);
	}

	public PageResult<Map<String, Object>> activityTemplet(RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.activityTemplet(request,pageSize,currPage);
	}

	public int templetAddSure(RequestWrapper request, AclUserBean loginUser) {
		return super.templetAddSure(request,loginUser);
	}

	@SuppressWarnings("unchecked")
	public int deleteTemplet(RequestWrapper request) {
		String templet_id = DaoFactory.getParam(request, "id");
		int res=1;
		try {
			if(!"".equals(templet_id)){
				this.delete("delete from tt_as_wr_activity_templet t where t.id="+templet_id, null);
				this.delete("delete from tt_as_wr_activity_templet_lab t where t.templet_id="+templet_id, null);
				this.delete("delete from tt_as_wr_activity_templet_part t where t.templet_id="+templet_id, null);
				this.delete("delete from tt_as_wr_activity_templet_acc t where t.templet_id="+templet_id, null);
				this.delete("delete from tt_as_wr_activity_templet_com t where t.templet_id="+templet_id, null);
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public int templetPublish(RequestWrapper request) {
		String templet_id = DaoFactory.getParam(request, "id");
		int res=1;
		try {
			if(!"".equals(templet_id)){
				this.update("update  tt_as_wr_activity_templet t  set t.status=18041002  where t.id="+templet_id, null);
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findTempletMain(RequestWrapper request) {
		String templet_id = DaoFactory.getParam(request, "id");
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT t.*, s.subject_no, s.subject_name，s.activity_type,s.fact_start_date,s.fact_end_date\n" );
		sb.append("  from tt_as_activity_subject s, tt_as_wr_activity_templet t\n" );
		sb.append(" where s.subject_id = t.subject_id\n" );
		sb.append("   and t.is_del = 0 and t.id="+templet_id+"\n" );
		sb.append(" order by t.create_date desc");
		return this.pageQuery(sb.toString(), null,getFunName());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findTempletLab(RequestWrapper request) {
		String templet_id = DaoFactory.getParam(request, "id");
		return  this.pageQuery("select t.* from tt_as_wr_activity_templet_lab t where t.templet_id="+templet_id, null,getFunName());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findTempletPart(RequestWrapper request) {
		String templet_id = DaoFactory.getParam(request, "id");
		return this.pageQuery("select t.* from tt_as_wr_activity_templet_part t where t.templet_id="+templet_id, null,getFunName());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findTempletAcc(RequestWrapper request) {
		String templet_id = DaoFactory.getParam(request, "id");
		return this.pageQuery("select t.* from tt_as_wr_activity_templet_acc t where t.templet_id="+templet_id, null,getFunName());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findTempletCom(RequestWrapper request) {
		String templet_id = DaoFactory.getParam(request, "id");
		return this.pageQuery("select t.* from tt_as_wr_activity_templet_com t where t.templet_id="+templet_id, null,getFunName());
	}

	public PageResult<Map<String, Object>> showSubject(RequestWrapper request,Integer pageSize, Integer currPage) {
		return super.showSubject(request,pageSize,currPage);
	}

	public int activityAddSure(RequestWrapper request, AclUserBean loginUser) {
		return super.activityAddSure(request,loginUser);
	}

	public PageResult<Map<String, Object>> activityList(RequestWrapper request,Integer pageSize, Integer currPage) {
		return super.activityList(request,pageSize,currPage);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> findActivity(RequestWrapper request) {
		return this.pageQueryMap(" select t.*,s.subject_no,s.subject_name from tt_as_activity t,tt_as_activity_subject s  where s.subject_id=t.subject_id and  t.activity_id="+DaoFactory.getParam(request, "activity_id"), null, getFunName());
	}

	public PageResult<Map<String, Object>> relationShow(RequestWrapper request,Integer pageSize, Integer currPage) {
		return super.relationShow(request,pageSize,currPage);
	}

	@SuppressWarnings("unchecked")
	public int cancelAcSure(RequestWrapper request) {
		int res=1;
		String activity_id = DaoFactory.getParam(request, "activity_id");
		//String templet_id = DaoFactory.getParam(request, "templet_id");
		try {
			if(!"".equals(activity_id)){
				this.delete("delete from  tt_as_activity t where t.activity_id="+activity_id, null);
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public String findTempletByCamCode(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.id\n" );
		sb.append("  from tt_as_wr_activity_templet t, tt_as_activity a\n" );
		sb.append(" where t.id = a.templet_id\n" );
		sb.append("   and a.activity_code ='"+DaoFactory.getParam(request, "cam_code")+"'");
		Map<String,Object> map = this.pageQueryMap(sb.toString(), null, getFunName());
		String id = BaseUtils.checkNull(map.get("ID"));
		return id;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findTempletMain(String id) {
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT t.*, s.subject_no, s.subject_name，s.activity_type,s.fact_start_date,s.fact_end_date\n" );
		sb.append("  from tt_as_activity_subject s, tt_as_wr_activity_templet t\n" );
		sb.append(" where s.subject_id = t.subject_id\n" );
		sb.append("   and t.is_del = 0 and t.id="+id+"\n" );
		sb.append(" order by t.create_date desc");
		return this.pageQuery(sb.toString(), null,getFunName());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findTempletLab(String id) {
		return  this.pageQuery("select t.* from tt_as_wr_activity_templet_lab t where t.templet_id="+id, null,getFunName());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findTempletPart(String id) {
		return this.pageQuery("select t.* from tt_as_wr_activity_templet_part t where t.templet_id="+id, null,getFunName());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findTempletAcc(String id) {
		return this.pageQuery("select t.* from tt_as_wr_activity_templet_acc t where t.templet_id="+id, null,getFunName());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findTempletCom(String id) {
		return this.pageQuery("select t.* from tt_as_wr_activity_templet_com t where t.templet_id="+id, null,getFunName());
	}
    //=====================lj2015-5-11
	@SuppressWarnings("unchecked")
	public void exportToexcel(ActionContext act,Long currDealerId, RequestWrapper request,Integer pageSizeMax, Integer currPage) {
		PageResult<Map<String, Object>> list = super.vinDetailByCount(currDealerId, request,  pageSizeMax,  currPage);
		try {
		String[] head=new String[]{"VIN","是否维修"};
		List<Map<String, Object>> records = list.getRecords();
		List params=new ArrayList();
		if(records!=null &&records.size()>0){
			for (Map<String, Object> map : records) {
				String[] detail=new String[2];
				detail[0]=BaseUtils.checkNull(map.get("VIN"));
				detail[1]=BaseUtils.checkNull(map.get("IS_REPAIR"));
				params.add(detail);
			}
		}
		  String systemDateStr = BaseUtils.getSystemDateStr();
		  BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "服务活动VIN明细"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public PageResult<Map<String, Object>> QueryactivityByvin(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.QueryactivityByvin(request,loginUser,pageSize,currPage);
	}
}
