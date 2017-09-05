package com.infodms.yxdms.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.entity.maintain.TtAsYsqPartPO;
import com.infodms.yxdms.entity.ysq.TtAsComRecordPO;
import com.infodms.yxdms.entity.ysq.TtAsWrYsqPO;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("rawtypes")
public class YsqDAO extends IBaseDao{

	private static YsqDAO dao = new YsqDAO();
	public static final YsqDAO getInstance(){
		dao = (dao==null)?new YsqDAO():dao;
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> ysqEngineList(RequestWrapper request, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select *\n" );
		sb.append("  from (select t.*, vw.root_org_name, record_date\n" );
		sb.append("\n" );
		sb.append("          from Tt_As_Wr_ysq t,\n" );
		sb.append("               vw_org_dealer_service vw,\n" );
		sb.append("               (select r.biz_id, min(r.create_date) record_date\n" );
		sb.append("                  from Tt_As_Com_Record r\n" );
		sb.append("                 group by r.biz_id) bz\n" );
		sb.append("         where 1 = 1\n" );
		sb.append("           and t.dealer_id = vw.dealer_id(+)\n" );
		sb.append("           and t.id = bz.biz_id(+)\n" );
		sb.append("           and t.is_delete is null\n" );
		sb.append("           and t.status > 93461001\n" );
		sb.append("         order by record_date desc) s\n" );
		sb.append(" where 1 = 1");

		DaoFactory.getsql(sb, "s.create_date", DaoFactory.getParam(request, "audit_date_start"), 3);//审核时间
		DaoFactory.getsql(sb, "s.create_date", DaoFactory.getParam(request, "audit_date_end"), 4);
		DaoFactory.getsql(sb, "s.vin", DaoFactory.getParam(request, "vin"), 2);
		DaoFactory.getsql(sb, "s.claim_type", DaoFactory.getParam(request, "claim_type"),1);
		DaoFactory.getsql(sb, "s.status", DaoFactory.getParam(request, "status"), 1);
		DaoFactory.getsql(sb, "s.ysq_no", DaoFactory.getParam(request, "ysq_no"), 2);
		DaoFactory.getsql(sb, "s.record_date", DaoFactory.getParam(request, "begin_time"), 3);//上报时间
		DaoFactory.getsql(sb, "s.record_date", DaoFactory.getParam(request, "end_time"), 4);//上报时间
		DaoFactory.getsql(sb, "s.root_org_name", DaoFactory.getParam(request, "org_name"),2);//审核时间
		return this.pageQuery(sb.toString(), null, getFunName(),pageSize,currPage);
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> ysqDealerList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.* from Tt_As_Wr_ysq t where 1=1 and t.is_delete is null ");
		DaoFactory.getsql(sb, "t.create_date", DaoFactory.getParam(request, "begin_time"), 3);
		DaoFactory.getsql(sb, "t.create_date", DaoFactory.getParam(request, "end_time"), 4);
		DaoFactory.getsql(sb, "t.vin", DaoFactory.getParam(request, "vin"), 2);
		DaoFactory.getsql(sb, "t.dealer_id", loginUser.getDealerId(), 1);
		DaoFactory.getsql(sb, "t.claim_type", DaoFactory.getParam(request, "claim_type"), 1);
		DaoFactory.getsql(sb, "t.status", DaoFactory.getParam(request, "status"), 1);
		DaoFactory.getsql(sb, "t.ysq_no", DaoFactory.getParam(request, "ysq_no"), 2);
		sb.append(" order by t.create_date  ");
		return this.pageQuery(sb.toString(), null, getFunName(),pageSize,currPage);
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> ysqTechList(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		
		sb.append("select *\n" );
		sb.append("  from (select t.*,\n" );
		sb.append("               vw.root_org_name,\n" );
		sb.append("               tm.dealer_shortname,\n" );
		sb.append("               tm.dealer_code,\n" );
		sb.append("               record_date\n" );
		sb.append("          from Tt_As_Wr_ysq t,\n" );
		sb.append("               vw_org_dealer_service vw,\n" );
		sb.append("               tm_dealer tm,\n" );
		sb.append("               (select r.biz_id, min(r.create_date) record_date\n" );
		sb.append("                  from Tt_As_Com_Record r\n" );
		sb.append("                 group by r.biz_id) bz\n" );
		sb.append("         where 1 = 1\n" );
		sb.append("           and tm.dealer_id = vw.dealer_id(+)\n" );
		sb.append("           and vw.dealer_id = t.dealer_id(+)\n" );
		sb.append("           and t.id = bz.biz_id(+)\n" );
		sb.append("           and t.is_delete is null\n" );
		sb.append("           and t.status > 93461001");
		DaoFactory.getsql(sb, "t.create_date", DaoFactory.getParam(request, "audit_date_start"), 3);//审核时间
		DaoFactory.getsql(sb, "t.create_date", DaoFactory.getParam(request, "audit_date_end"), 4);//审核时间
		DaoFactory.getsql(sb, "t.vin", DaoFactory.getParam(request, "vin"), 2);
		DaoFactory.getsql(sb, "t.claim_type", DaoFactory.getParam(request, "claim_type"), 1);
		DaoFactory.getsql(sb, "t.status", DaoFactory.getParam(request, "status"), 1);
		DaoFactory.getsql(sb, "vw.root_org_name", DaoFactory.getParam(request, "org_name"), 2);
		DaoFactory.getsql(sb, "t.ysq_no", DaoFactory.getParam(request, "ysq_no"), 2);
		DaoFactory.getsql(sb, "tm.dealer_shortname", DaoFactory.getParam(request, "dealer_name"), 2);//
		DaoFactory.getsql(sb, "tm.dealer_code", DaoFactory.getParam(request, "dealerCode"), 6);//经销商代码
        sb.append(CommonUtils.getOrgDealerLimitSqlByPose("t", loginUser));
		sb.append(" order by record_date desc  ");
		sb.append(" ) where 1=1  ");
		DaoFactory.getsql(sb, "record_date", DaoFactory.getParam(request, "begin_time"), 3);//上报时间
		DaoFactory.getsql(sb, "record_date", DaoFactory.getParam(request, "end_time"), 4);//上报时间
		sb.append(" or  record_date is null");
		
		return this.pageQuery(sb.toString(), null, getFunName(),pageSize,currPage);
	}
	/**
	 * 通过职位ID获取角色ID
	 * @param poseId 职位ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getPoseRoleId(String poseId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM tr_role_pose p WHERE p.pose_id=" + poseId + "");
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		if (null == list || list.size() <= 0 || list.get(0) == null || list.get(0).get("ROLE_ID") == null) {
			return "0";
		}
		return CommonUtils.checkNull(list.get(0).get("ROLE_ID"));
	}
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> ysqTechDirectorList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select s.*\n" );
		sb.append("  from (select t.*,\n" );
		sb.append("               vw.root_org_name,record_date\n" );
		sb.append("          from Tt_As_Wr_ysq t, vw_org_dealer_service vw, (select r.biz_id,min(r.create_date) record_date\n" );
		sb.append("                  from Tt_As_Com_Record r\n" );
		sb.append("                  group by r.biz_id )  bz\n" );
		sb.append("         where 1 = 1\n" );
		sb.append("           and t.dealer_id = vw.dealer_id(+)\n" );
		sb.append("           and t.id=bz.biz_id(+)\n" );
		sb.append("           and t.is_delete is null\n" );
		sb.append("           and t.status > 93461001\n" );
		sb.append("         order by t.is_end desc) s\n" );
		sb.append(" where 1 = 1");

		DaoFactory.getsql(sb, "s.create_date", DaoFactory.getParam(request, "audit_date_start"), 3);//审核时间
		DaoFactory.getsql(sb, "s.create_date", DaoFactory.getParam(request, "audit_date_end"), 4);//审核时间
		DaoFactory.getsql(sb, "s.vin", DaoFactory.getParam(request, "vin"), 2);
		DaoFactory.getsql(sb, "s.claim_type", DaoFactory.getParam(request, "claim_type"), 1);
		DaoFactory.getsql(sb, "s.status", DaoFactory.getParam(request, "status"), 1);
		DaoFactory.getsql(sb, "s.ysq_no", DaoFactory.getParam(request, "ysq_no"), 2);
		DaoFactory.getsql(sb, "s.record_date", DaoFactory.getParam(request, "begin_time"), 3);//上报时间
		DaoFactory.getsql(sb, "s.record_date", DaoFactory.getParam(request, "end_time"), 4);//上报时间
		DaoFactory.getsql(sb, "s.root_org_name", DaoFactory.getParam(request, "org_name"), 2);
		
		return this.pageQuery(sb.toString(), null, getFunName(),pageSize,currPage);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getPartInfo(String mainPartId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.*,(select t.is_return from Tm_Pt_Part_Base p where p.part_code=t.part_code) as is_return from tm_pt_part_base t \n");
		sql.append(" where 1 = 1 \n");
		DaoFactory.getsql(sql, "t.part_id", mainPartId, 1);
		List<Map<String, Object>> list= this.pageQuery(sql.toString(), null, getFunName());
		if(DaoFactory.checkListNull(list)){
			return list.get(0);
		}else{
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public int ysqAddSure(RequestWrapper request, AclUserBean loginUser) {
		String id = DaoFactory.getParam(request, "id");
		String relation_ro = DaoFactory.getParam(request, "relation_ro");
		String series_id = DaoFactory.getParam(request, "series_id");
		String model_id = DaoFactory.getParam(request, "model_id");
		String part_id = DaoFactory.getParam(request, "part_id");
		String producer_name = DaoFactory.getParam(request, "producer_name");
		String mileage = DaoFactory.getParam(request, "mileage");
		String vin = DaoFactory.getParam(request, "vin");
		String claim_type = DaoFactory.getParam(request, "claim_type");
		String part_code = DaoFactory.getParam(request, "part_code");
		String part_name = DaoFactory.getParam(request, "part_name");
		String producer_code = DaoFactory.getParam(request, "producer_code");
		String max_estimate = DaoFactory.getParam(request, "max_estimate");
		String is_return = DaoFactory.getParam(request, "is_return");
		String trouble_desc = DaoFactory.getParam(request, "trouble_desc");
		String trouble_reason = DaoFactory.getParam(request, "trouble_reason");
		String com_apply = DaoFactory.getParam(request, "com_apply");
		String com_remark = DaoFactory.getParam(request, "com_remark");
		String bc_apply = DaoFactory.getParam(request, "bc_apply");
		//String com_pass = DaoFactory.getParam(request, "com_pass");
		//String bc_pass = DaoFactory.getParam(request, "bc_pass");
		String bc_remark = DaoFactory.getParam(request, "bc_remark");	
		String identify = DaoFactory.getParam(request, "identify");	
		String bc_mileage = DaoFactory.getParam(request, "bc_mileage");
	    List<TtAsWrYsqPO> list =  this.checkYsqByVindealerId(request,loginUser);
		    

		String[] fjids=request.getParamValues("fjid");
		int res=1;
		try {
			if (null==list||list.size()<=0) {//检测到五分钟内没有添加
			if("".equals(id)){
				Long pkId = DaoFactory.getPkId();
				TtAsWrYsqPO t=new TtAsWrYsqPO();
				t.setId(pkId);
				String ysq_no = SequenceManager.getSequence2("YSQ");
				t.setYsqNo(ysq_no);
				if(null != bc_apply && !"".equals(bc_apply)){
					t.setBcApply(Float.parseFloat(bc_apply));
					t.setBcPass(Float.parseFloat(bc_apply));
					t.setBcRemark(bc_remark);
				}
				if(null != com_apply && !"".equals(com_apply)){
					t.setComApply(Float.parseFloat(com_apply));
					t.setComPass(Float.parseFloat(com_apply));
					t.setComRemark(com_remark);
				}
				if(null != bc_mileage && !"".equals(bc_mileage)){
					t.setBcMileage(Double.parseDouble(bc_mileage));
				}
				t.setTroubleDesc(trouble_desc);
				t.setTroubleReason(trouble_reason);
				t.setClaimType(Integer.parseInt(claim_type));
				t.setSeriesId(Long.parseLong(series_id));
				t.setModelId(Long.parseLong(model_id));
				t.setPartCode(part_code);
				t.setPartName(part_name);
				t.setIsReturn(Integer.parseInt(is_return));
				t.setRelationRo(relation_ro);
				t.setPartId(Long.parseLong(part_id));
				t.setProducerCode(producer_code);
				t.setProducerName(producer_name);
				t.setMileage(Double.parseDouble(mileage));
				t.setVin(vin);
				t.setMaxEstimate(Float.parseFloat(max_estimate));
				if("0".equals(identify)){
					t.setStatus(93461001);//预授权未上报
				}else{
					t.setStatus(93461002);//预授权待审核
					t.setIsEnd(1);
					insertWfRecord("服务站"+loginUser.getDealerCode(), 93461002, loginUser, "上报车厂审核",pkId);
				}
				t.setCreateDate(new Date());
				t.setCreateBy(loginUser.getUserId());
				t.setDealerId(Long.parseLong(loginUser.getDealerId()));
				int jugeRes=this.jugeYsqMakeWflow(Long.parseLong(part_id), Float.parseFloat(max_estimate));
				t.setJugeRes(jugeRes);
				this.insert(t);
				DaoFactory.delAndReinsetFile(loginUser, fjids, pkId.toString());
			}else{
				TtAsWrYsqPO t1=new TtAsWrYsqPO();
				Long pkid = Long.valueOf(id);
				t1.setId(pkid);
				TtAsWrYsqPO t2=new TtAsWrYsqPO();
				if(null != bc_apply && !"".equals(bc_apply)){
					t2.setBcApply(Float.parseFloat(bc_apply));
					t2.setBcPass(Float.parseFloat(bc_apply));
					t2.setBcRemark(bc_remark);
				}
				if(null != bc_mileage && !"".equals(bc_mileage)){
					t2.setBcMileage(Double.parseDouble(bc_mileage));
				}
				t2.setTroubleDesc(trouble_desc);
				t2.setTroubleReason(trouble_reason);
				if(null != com_apply && !"".equals(com_apply)){
					t2.setComApply(Float.parseFloat(com_apply));
					t2.setComPass(Float.parseFloat(com_apply));
					t2.setComRemark(com_remark);
				}else if (null == com_apply || "".equals(com_apply)) {
					t2.setComApply(Float.parseFloat("0.0"));
					t2.setComPass(Float.parseFloat("0.0"));
					t2.setComRemark("");
				}
				t2.setClaimType(Integer.parseInt(claim_type));
				t2.setSeriesId(Long.parseLong(series_id));
				t2.setModelId(Long.parseLong(model_id));
				t2.setPartCode(part_code);
				t2.setPartName(part_name);
				t2.setIsReturn(Integer.parseInt(is_return));
				t2.setRelationRo(relation_ro);
				t2.setPartId(Long.parseLong(part_id));
				t2.setProducerCode(producer_code);
				t2.setProducerName(producer_name);
				t2.setMileage(Double.parseDouble(mileage));
				t2.setVin(vin);
				t2.setMaxEstimate(Float.parseFloat(max_estimate));
				t2.setCreateDate(new Date());
				t2.setCreateBy(loginUser.getUserId());
				t2.setDealerId(Long.parseLong(loginUser.getDealerId()));
				if("0".equals(identify)){
					t2.setStatus(93461001);//预授权未上报
				}else{
					t2.setStatus(93461002);//预授权待审核
					t2.setIsEnd(1);
					insertWfRecord("服务站"+loginUser.getDealerCode(), 93461002, loginUser, "上报车厂审核",pkid);
				}
				int jugeRes=this.jugeYsqMakeWflow(Long.parseLong(part_id), Float.parseFloat(max_estimate));
				t2.setJugeRes(jugeRes);
				this.update(t1, t2);
				DaoFactory.delAndReinsetFile(loginUser, fjids, id);
			}
		}else {
				res=2;
		 }
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}
	/**
	 * 用于检测预授权重复报
	 */
	@SuppressWarnings("unchecked")
	private List<TtAsWrYsqPO> checkYsqByVindealerId(RequestWrapper request, AclUserBean loginUser) {
		    Calendar  calendar  = 	Calendar.getInstance();
		    String datestr="";
		    Date dd =null;
		    try {
		    	Date date = new Date();
			    Long d = date.getTime()-5*60*1000;
			    calendar.setTimeInMillis(d);
//			    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			    dd=format.format(calendar.getTime());
			    int YY = calendar.get(Calendar.YEAR) ;
			    int MM = calendar.get(Calendar.MONTH)+1;
			    int DD = calendar.get(Calendar.DATE);
			    int hour = calendar.get(Calendar.HOUR_OF_DAY);
			    int minute = calendar.get(Calendar.MINUTE);//前五分钟
			    int second = calendar.get(Calendar.SECOND);
			     datestr = YY+"-"+MM+"-"+DD+" "+hour+":"+minute+":"+second;
			} catch (Exception e) {
				e.printStackTrace();
			}
			StringBuffer sql= new StringBuffer();
			sql.append("select y.create_date\n" );
			sql.append("  from Tt_As_Wr_Ysq Y\n" );
			sql.append(" WHERE 1=1\n" );
			DaoFactory.getsql(sql, "Y.PART_CODE", DaoFactory.getParam(request, "part_code"), 1);
			DaoFactory.getsql(sql, "y.vin", DaoFactory.getParam(request, "vin"), 1);
			DaoFactory.getsql(sql, "y.dealer_id", loginUser.getDealerId(), 1);
			sql.append("   AND Y.CREATE_DATE >=\n" );
			sql.append("       to_date('"+datestr+"', 'yyyy-mm-dd hh24:mi:ss')\n" );
			sql.append("   AND Y.CREATE_DATE <=sysdate\n" );
			return this.pageQuery(sql.toString(), null, getFunName());
		    
		   
	}

	/**
	 * 
	 * @param part_id
	 * @param max_estimate
	 * 
	 */
	@SuppressWarnings("unchecked")
	private int jugeYsqMakeWflow(Long part_id, Float max_estimate) {
		int res=0;//1 2 4 8 枚举模式
		StringBuffer sb= new StringBuffer();
		sb.append("select t.* from tt_AS_ysq_part t where 1=1 and t.part_type=72311001 and t.part_id='"+part_id+"'");
		List<?> list = this.pageQuery(sb.toString(), null, getFunName());
		if(DaoFactory.checkListNull(list)){
			res+=1;//有件
		}else{
			res+=2;//无
		}
		if(3000<=max_estimate){
			res+=4;//有3000 
		}else{
			res+=8;//无
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findYsqData(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.* from Tt_As_Wr_ysq t where 1=1  and t.is_delete is null  ");
		DaoFactory.getsql(sb, "t.id", DaoFactory.getParam(request, "id"), 1);
		List<Map<String, Object>> list = this.pageQuery(sb.toString(), null, getFunName());
		return list;
	}

	@SuppressWarnings("unchecked")
	public String jugeYsq(RequestWrapper request) {
		String id = DaoFactory.getParam(request, "id");
		String msg="";
		Float maxEstimate =null;
		Long partId =null;
		Integer status=null;
		Integer isEnd=null;
		TtAsWrYsqPO t=new TtAsWrYsqPO();
		t.setId(Long.valueOf(id));
		List<TtAsWrYsqPO> pos = this.select(t);
		if(DaoFactory.checkListNull(pos)){
			TtAsWrYsqPO po = pos.get(0);
			status = po.getStatus();
			maxEstimate = po.getMaxEstimate();
			partId = po.getPartId();
			isEnd=po.getIsEnd();
		}
		int res=this.jugeYsqMakeWflow(partId, maxEstimate);
		if(res==9){//有维护件 无3000
			if(status==93461002){
				msg+="提示:该预授权有维护件,小于3000的预估金额，审核通过将到发动机部门审核！";
			}
			if(status==93461003 && isEnd==2){
				msg+="提示:该预授权有维护件,小于3000的预估金额，发动机部门通过将再次到技术部审核！";
			}
			if(status==93461007 && isEnd==3){
				msg+="提示:该预授权有维护件,小于3000的预估金额，技术部再次审核将关闭流程！";
			}
		}
		if(res==5){//有维护件 有3000
			if(status==93461002){
				msg+="提示:该预授权有维护件并且大于3000的预估金额，审核通过将到发动机部门审核！";
			}
			if(status==93461003 && isEnd==2){
				msg+="提示:该预授权有维护件并且大于3000的预估金额，发动机部门审核通过，将到技术部审核！";
			}
			if(status==93461007 && isEnd==3){
				msg+="提示:该预授权有维护件并且大于3000的预估金额，技术部审核再次审核后将到技术部主管审核！";
			}
			if(status==93461003 && isEnd==4){
				msg+="提示:该预授权有维护件并且大于3000的预估金额，技术部主管审核通过将关闭流程！";
			}
		}
		if(res==10){//无维护件 无3000
			if(status==93461002){
				msg+="提示:该预授权无维护件或者大于3000的预估金额，审核完毕后将结束流程！";
			}
		}
		if(res==6){//无维护件 有3000 
			if(status==93461002){
				msg+="提示:该预授权无维护件,但是大于3000的预估金额，审核通过后将走到技术部主管审核！";
			}
			if(status==93461003  && isEnd==2){
				msg+="提示:该预授权无维护件,但是大于3000的预估金额，主管审核完毕后将结束流程！";
			}
		}
		return msg;
	}

	@SuppressWarnings("unchecked")
	public int auditYsq(RequestWrapper request, AclUserBean loginUser) {
		int result=1;
		try {
			String com_pass = DaoFactory.getParam(request, "com_pass");
			String bc_pass = DaoFactory.getParam(request, "bc_pass");
			String id = DaoFactory.getParam(request, "id");
			String type = DaoFactory.getParam(request, "type");
			String remark = DaoFactory.getParam(request, "text_remark");
			Float maxEstimate =null;
			Long partId =null;
			Integer status=null;
			Integer isEnd=null;
			TtAsWrYsqPO t=new TtAsWrYsqPO();
			Long bizId = Long.valueOf(id);
			t.setId(bizId);
			List<TtAsWrYsqPO> pos = this.select(t);
			if(DaoFactory.checkListNull(pos)){
				TtAsWrYsqPO po = pos.get(0);
				status = po.getStatus();
				maxEstimate = po.getMaxEstimate();
				partId = po.getPartId();
				isEnd=po.getIsEnd();
			}
			
			int res=this.jugeYsqMakeWflow(partId, maxEstimate);
			TtAsWrYsqPO t2=new TtAsWrYsqPO();
			if("tech".equals(type) || "director".equals(type)){
				if(!"".equals(bc_pass)){
					t2.setBcPass(Float.parseFloat(bc_pass));
				}
				if(!"".equals(com_pass)){
					t2.setComPass(Float.parseFloat(com_pass));
				}
				
			}
			if(res==9){//有维护件 无3000
				Integer [] arr={93461002,93461003,93461007,93461003};
				if(status==93461002){
					t2.setStatus(arr[isEnd]);
					t2.setIsEnd(2);
					this.update(t, t2);
					insertWfRecord("技术部",arr[isEnd],loginUser,remark,bizId);
				}
				if(status==93461003 && isEnd==2){
					t2.setStatus(arr[isEnd]);
					t2.setIsEnd(3);
					this.update(t, t2);
					insertWfRecord("发动机部",arr[isEnd],loginUser,remark,bizId);
				}
				if(status==93461007 && isEnd==3){
					t2.setStatus(arr[isEnd]);
					t2.setIsEnd(-1);
					this.update(t, t2);
					insertWfRecord("技术部",arr[isEnd],loginUser,remark,bizId);
				}
			}
			if(res==5){//有维护件 有3000
				Integer [] arr={93461002,93461003,93461007,93461003,93461005};
				if(status==93461002){
					t2.setStatus(arr[isEnd]);
					t2.setIsEnd(2);
					this.update(t, t2);
					insertWfRecord("技术部",arr[isEnd],loginUser,remark,bizId);
				}
				if(status==93461003 && isEnd==2){
					t2.setStatus(arr[isEnd]);
					t2.setIsEnd(3);
					this.update(t, t2);
					insertWfRecord("发动机部",arr[isEnd],loginUser,remark,bizId);
				}
				if(status==93461007 && isEnd==3){
					t2.setStatus(arr[isEnd]);
					t2.setIsEnd(4);
					this.update(t, t2);
					insertWfRecord("技术部",arr[isEnd],loginUser,remark,bizId);
				}
				if(status==93461003 && isEnd==4){
					t2.setStatus(arr[isEnd]);
					t2.setIsEnd(-1);//结束
					this.update(t, t2);
					insertWfRecord("技术部主管",arr[isEnd],loginUser,remark,bizId);
				}
			}
			if(res==10){//无维护件 无3000
				if(status==93461002){
					t2.setStatus(93461003);
					t2.setIsEnd(-1);
					this.update(t, t2);
					insertWfRecord("技术部",93461003,loginUser,remark,bizId);
				}
			}
			if(res==6){//无维护件 有3000 
				Integer [] arr={93461002,93461003,93461005};
				if(status==93461002){
					t2.setStatus(93461003);
					t2.setIsEnd(arr[isEnd]);
					t2.setIsEnd(2);
					this.update(t, t2);
					insertWfRecord("技术部",arr[isEnd],loginUser,remark,bizId);
				}
				if(status==93461003){
					t2.setStatus(arr[isEnd]);
					t2.setIsEnd(-1);//代表结束
					this.update(t, t2);
					insertWfRecord("技术部主管",arr[isEnd],loginUser,remark,bizId);
				}
			}
		} catch (Exception e) {
			result=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}
	/**
	 * 记录日志
	 * @param role
	 * @param status
	 * @param loginUser
	 */
	@SuppressWarnings("unchecked")
	private void insertWfRecord(String role, Integer status,AclUserBean loginUser,String remark,Long bizId) {
		TtAsComRecordPO po=new TtAsComRecordPO();
		po.setCreateBy(loginUser.getUserId());
		po.setCreateDate(new Date());
		po.setId(DaoFactory.getPkId());
		po.setRoleName(role);
		po.setStatus(status);
		po.setRemark(remark);
		po.setBizId(bizId);
		this.insert(po);
	}

	@SuppressWarnings("unchecked")
	public int showWorkFlow(RequestWrapper request) {
		String id = DaoFactory.getParam(request, "id");
		Float maxEstimate =null;
		Long partId =null;
		TtAsWrYsqPO t=new TtAsWrYsqPO();
		Long bizId = Long.valueOf(id);
		t.setId(bizId);
		List<TtAsWrYsqPO> pos = this.select(t);
		if(DaoFactory.checkListNull(pos)){
			TtAsWrYsqPO po = pos.get(0);
			maxEstimate = po.getMaxEstimate();
			partId = po.getPartId();
		}
		int res=this.jugeYsqMakeWflow(partId, maxEstimate);
		return res;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findYsqRecords(RequestWrapper request) {
		String id = DaoFactory.getParam(request, "id");
		String type = DaoFactory.getParam(request, "type");
		StringBuffer sb =new StringBuffer();
		if(type.equals("claim")){
			sb.append("select t.*, c.name\n" );
			sb.append("  from Tt_As_Com_Record t, tc_user c\n" );
			sb.append(" where c.user_id = t.create_by\n" );
			sb.append("   and t.biz_id = （\n" );
			sb.append("  select (select y.id from Tt_As_Wr_Ysq y where y.ysq_no = a.ysq_no)\n" );
			sb.append("          from Tt_As_Wr_Application a\n" );
			sb.append("         where a.ysq_no is not null\n" );
			sb.append("           and a.id = '"+id+"')\n" );
			sb.append("         order by t.create_date");
		}else{
			sb.append("select t.*,c.name from Tt_As_Com_Record t,tc_user c  where c.user_id=t.create_by and  t.biz_id="+id+" order by t.create_date ");
		}
		return this.pageQuery(sb.toString(), null, getFunName());
	}

	@SuppressWarnings("unchecked")
	public int auditRebut(RequestWrapper request, AclUserBean loginUser) {
		int result=1;
		try {
			String com_pass = DaoFactory.getParam(request, "com_pass");
			String bc_pass = DaoFactory.getParam(request, "bc_pass");
			String id = DaoFactory.getParam(request, "id");
			String type = DaoFactory.getParam(request, "type");
			String status = DaoFactory.getParam(request, "status");
			String remark =DaoFactory.getParam(request, "text_remark");
		    
			TtAsWrYsqPO t=new TtAsWrYsqPO();
			Long bizId = Long.valueOf(id);
			t.setId(bizId);
			
			TtAsWrYsqPO t2=new TtAsWrYsqPO();
			t2.setStatus(Integer.parseInt(status));
			t2.setIsEnd(1);
			if ("93461010".equals(status)) {//拒赔
				t2.setIsEnd(-1);
			}
			if("tech".equals(type) || "director".equals(type)){
				if(!"".equals(bc_pass)){
					t2.setBcPass(Float.parseFloat(bc_pass));
				}
				if(!"".equals(com_pass)){
					t2.setComPass(Float.parseFloat(com_pass));
				}
			}
			this.update(t, t2);
			String roleName="";
			if("tech".equals(type)){
				roleName="技术部";
			}else{
				roleName="技术部主管";
			}
			insertWfRecord(roleName,Integer.parseInt(status),loginUser,remark,bizId);
		} catch (Exception e) {
			result=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public int ysqReportSubmit(String id,RequestWrapper request,AclUserBean loginUser) {
		int res=1;
		try {
			StringBuffer sb= new StringBuffer();
			sb.append("update Tt_As_Wr_Ysq t set t.status=93461002,t.is_end=1,t.create_date=sysdate where  t.id='"+id+"' \n" );
			this.update(sb.toString(), null);
			insertWfRecord("服务站"+loginUser.getDealerCode(), 93461002, loginUser, "上报车厂审核",Long.valueOf(id));
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findYsqRecords(String id) {
		return this.pageQuery("select t.*,c.name from Tt_As_Com_Record t,tc_user c  where c.user_id=t.create_by  and t.status not in (93461007) and  t.biz_id="+id+" order by t.create_date ", null, getFunName());
	}

	@SuppressWarnings("unchecked")
	public String changeByYsqRo(String ysqNo) {
		String id = "";
		TtAsWrYsqPO ttAsWrYsqPO=null;
		try {
			TtAsWrYsqPO t=new TtAsWrYsqPO();
			t.setYsqNo(ysqNo);
			List<TtAsWrYsqPO> list = this.select(t);
			ttAsWrYsqPO = list.get(0);
			id=ttAsWrYsqPO.getId().toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return id;
	}

	@SuppressWarnings("unchecked")
	public Map<String,Object> judgePartUseType(RequestWrapper request) {
		Map<String,Object> map=new HashMap<String, Object>();
		StringBuffer sb= new StringBuffer();
		sb.append("select p.part_use_type,p.part_quantity\n" );
		sb.append("  from tt_as_repair_order o, tt_as_ro_repair_part p\n" );
		sb.append(" where p.ro_id = o.id\n" );
		sb.append("   and p.part_no =\n" );
		sb.append("       (select y.part_code from Tt_As_Wr_Ysq y where y.ysq_no = '"+DaoFactory.getParam(request, "ysq_no")+"')");
		DaoFactory.getsql(sb, "o.ro_no", DaoFactory.getParam(request, "ro_no"), 1);
		Map<String,Object> pageQueryMap = this.pageQueryMap(sb.toString(), null, getFunName());
		int intValue=0;
		int part_quantity=0;
		if(pageQueryMap!=null){
			BigDecimal object = (BigDecimal) pageQueryMap.get("PART_USE_TYPE");
			intValue = object.intValue();
			BigDecimal part_quantity_temp = (BigDecimal) pageQueryMap.get("PART_QUANTITY");
			part_quantity = part_quantity_temp.intValue();
		}else{
			intValue=95431002;
		}
		map.put("partUseType", intValue);
		map.put("partQuantity", part_quantity);
		return map;
	}

	@SuppressWarnings("unchecked")
	public int auditEidt(RequestWrapper request, AclUserBean loginUser) {
		TtAsWrYsqPO po = new TtAsWrYsqPO();
		TtAsWrYsqPO po1 = new TtAsWrYsqPO();
		po.setId(Long.parseLong(DaoFactory.getParam(request, "id")));
	    List<TtAsWrYsqPO> list =this.select(po);
	    String YsqNo = list.get(0).getYsqNo();
	    Integer isend = list.get(0).getIsEnd();
	    Integer status = list.get(0).getStatus();
	    TtAsWrApplicationPO applicationPO = new TtAsWrApplicationPO();
	    applicationPO.setYsqNo(YsqNo);
	    List<TtAsWrApplicationPO> list2 =  this.select(applicationPO);
	    Long bizId = Long.valueOf(DaoFactory.getParam(request, "id"));
	    if (null!=list2 && list2.size()>0) {//预授权被用
	    	return -1;
		}else {
			Float maxEstimate =null;
			Long partId =null;
			Integer status1=null;
			Integer isEnd=null;
			TtAsWrYsqPO t=new TtAsWrYsqPO();
			t.setId(bizId);
			List<TtAsWrYsqPO> pos = this.select(t);
			if(DaoFactory.checkListNull(pos)){
				TtAsWrYsqPO po2 = pos.get(0);
				status1 = po2.getStatus();
				maxEstimate = po2.getMaxEstimate();
				partId = po2.getPartId();
				isEnd=po2.getIsEnd();
			}
		int res=this.jugeYsqMakeWflow(partId, maxEstimate);
		if ( Constant.STATUS_YSQ_03.equals(status)) {//技术室撤销审核
			if (res==9&&isEnd==2) {
				po.setStatus(Constant.STATUS_YSQ_03);
				po1.setStatus(Constant.STATUS_YSQ_02);
				po1.setIsEnd(1);//设置为上报时候的状态
				insertWfRecord("技术室",Constant.STATUS_YSQ_08,loginUser,"技术室撤销审核",bizId);//记录日志
				return this.update(po, po1);
			}else if (res==9&&isEnd==-1) {
				po.setStatus(Constant.STATUS_YSQ_03);
				po1.setStatus(Constant.STATUS_YSQ_07);
				po1.setIsEnd(3);//设置为上报时候的状态
				insertWfRecord("技术室",Constant.STATUS_YSQ_08,loginUser,"技术室撤销审核",bizId);//记录日志
				return this.update(po, po1);
			}else if (res==5&&isEnd==2) {
				po.setStatus(Constant.STATUS_YSQ_03);
				po1.setStatus(Constant.STATUS_YSQ_02);
				po1.setIsEnd(1);//设置为上报时候的状态
				insertWfRecord("技术室",Constant.STATUS_YSQ_08,loginUser,"技术室撤销审核",bizId);//记录日志
				return this.update(po, po1);
			}else if (res==5&&isEnd==4) {
				po.setStatus(Constant.STATUS_YSQ_03);
				po1.setStatus(Constant.STATUS_YSQ_07);
				po1.setIsEnd(3);//设置为上报时候的状态
				insertWfRecord("技术室",Constant.STATUS_YSQ_08,loginUser,"技术室撤销审核",bizId);//记录日志
				return this.update(po, po1);
			}else if (res==6&&isEnd==2) {
				po.setStatus(Constant.STATUS_YSQ_03);
				po1.setStatus(Constant.STATUS_YSQ_02);
				po1.setIsEnd(1);//设置为上报时候的状态
				insertWfRecord("技术室",Constant.STATUS_YSQ_08,loginUser,"技术室撤销审核",bizId);//记录日志
				return this.update(po, po1);
			}else if (res==10&&isEnd==-1) {
				po.setStatus(Constant.STATUS_YSQ_03);
				po1.setStatus(Constant.STATUS_YSQ_02);
				po1.setIsEnd(1);//设置为上报时候的状态
				insertWfRecord("技术室",Constant.STATUS_YSQ_08,loginUser,"技术室撤销审核",bizId);//记录日志
				return this.update(po, po1);
			}else {
				return -1;
			}
			}else if ( Constant.STATUS_YSQ_05.equals(status)) {//技术室主管撤销审核
				if (res==6&&isEnd==-1) {
					po.setStatus(Constant.STATUS_YSQ_05);
					po1.setStatus(Constant.STATUS_YSQ_03);
					po1.setIsEnd(2);//设置为上报时候的状态
					insertWfRecord("技术主管",Constant.STATUS_YSQ_09,loginUser,"技术主管撤销审核",bizId);//记录日志
					return this.update(po, po1);
				}else if (res==5&&isEnd==-1) {
					po.setStatus(Constant.STATUS_YSQ_05);
					po1.setStatus(Constant.STATUS_YSQ_03);
					po1.setIsEnd(4);//设置为上报时候的状态
					insertWfRecord("技术主管",Constant.STATUS_YSQ_09,loginUser,"技术主管撤销审核",bizId);//记录日志
					return this.update(po, po1);
				}
				
				po.setStatus(Constant.STATUS_YSQ_05);
				po1.setStatus(Constant.STATUS_YSQ_03);
				insertWfRecord("技术主管",Constant.STATUS_YSQ_09,loginUser,"技术室主管撤销审核",bizId);//记录日志
				return this.update(po, po1);
			}else {
				return -1;
			} 
		}
	}

	@SuppressWarnings("unchecked")
	public List<TtAsYsqPartPO> checkpartCodeToysq(AclUserBean loginUser,RequestWrapper request) {
		List<TtAsYsqPartPO> list=null;
		StringBuffer sql= new StringBuffer();
		sql.append("select *\n" );
		sql.append("  from tt_as_ysq_part p\n" );
		sql.append(" where 1 = 1\n" );
		sql.append("  and p.part_type="+Constant.YSQ_PART_TYPE_02 );
		DaoFactory.getsql(sql,"p.part_code", DaoFactory.getParam(request,"part_code"), 1);
        list = this.select(TtAsYsqPartPO.class, sql.toString(), null);
		return list;
	}
	
}
