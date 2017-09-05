package com.infodms.yxdms.dao;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ClaimApproveAndStoredReturnInfoBean;
import com.infodms.dms.bean.TtAsWrBackListQryBean;
import com.infodms.dms.bean.TtAsWrOldPartBackListDetailBean;
import com.infodms.dms.bean.TtAsWrOldPartDetailListBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.oldPart.ClaimTools;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOldpartTransportPO;
import com.infodms.dms.po.TtAsPartBorrowPO;
import com.infodms.dms.po.TtAsPartBorrowSubclassPO;
import com.infodms.dms.po.TtAsWrOldReturnedDetailPO;
import com.infodms.dms.po.TtAsWrOldReturnedPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.XHBUtil;
import com.infodms.yxdms.entity.oldpart.TtAsOldReturnApplyPO;
import com.infodms.yxdms.entity.ysq.TtAsComRecordPO;
import com.infodms.yxdms.utils.BaseUtils;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("rawtypes")
public class OldReturnDAO extends IBaseDao{

	private static OldReturnDAO dao = new OldReturnDAO();
	public static final OldReturnDAO getInstance(){
		dao = (dao==null)?new OldReturnDAO():dao;
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> findReturnData(String id) {
		StringBuffer sb= new StringBuffer();
		sb.append("select tawor.yieldly,\n" );
		sb.append("       tawor.status,\n" );
		sb.append("       tawor.price,\n" );
		sb.append("       tawor.auth_price new_price,\n" );
		sb.append("       tawor.id,\n" );
		sb.append("       tm.dealer_code,\n" );
		sb.append("       tm.dealer_name,\n" );
		sb.append("       tawor.tel,\n" );
		sb.append("       tawor.price_remark,\n" );
		sb.append("       torg.org_name attach_area,\n" );
		sb.append("       tawor.transport_company transport_name,\n" );
		sb.append("       tawor.transport_type,\n" );
		sb.append("       tc.code_desc transport_desc,\n" );
		sb.append("       tawor.parkage_amount,\n" );
		sb.append("       tawor.return_no,\n" );
		sb.append("       tawor.remark,\n" );
		sb.append("       to_char(tawor.Send_Time, 'YYYY-MM-DD') create_date,\n" );
		sb.append("       to_char(tawor.return_date, 'YYYY-MM-DD') return_date,\n" );
		sb.append("       tawor.wr_amount,\n" );
		sb.append("       tawor.part_item_amount,\n" );
		sb.append("       tawor.part_amount,\n" );
		sb.append("       tawor.return_type,\n" );
		sb.append("       tawor.out_part_packge,\n" );
		sb.append("       tawor.sign_remark,\n" );
		sb.append("       tcc.code_desc return_desc,\n" );
		sb.append("       to_char(tawor.wr_start_date,'yyyy-mm-dd')||'至'||to_char(tawor.return_end_date,'yyyy-mm-dd') as wr_start_date,\n" );
		sb.append("       tawor.tran_no tran_no,\n" );
		sb.append("       tawor.transport_no,\n" );
		sb.append("       tawor.real_box_no,\n" );
		sb.append("       tawor.part_pakge,\n" );
		sb.append("       tawor.part_mark,\n" );
		sb.append("       tawor.part_detail\n" );
		sb.append("  from tt_as_wr_returned_order       tawor,\n" );
//		sb.append("       tt_as_wr_returned_order_detail d,\n" );
		sb.append("       tm_dealer                   tm,\n" );
		sb.append("       tm_dealer_org_relation      tdor,\n" );
		sb.append("       tm_org                      torg,\n" );
		sb.append("       tc_code                     tc,\n" );
		sb.append("       tc_code                     tcc\n" );
		sb.append(" where tawor.transport_type = tc.code_id(+)\n" );
		sb.append("   and tawor.return_type = tcc.code_id\n" );
		sb.append("   and tawor.dealer_id = tm.dealer_id(+)\n" );
//		sb.append("   AND tawor.id = d.return_id(+)\n" );
		sb.append("   and f_get_pid(tawor.dealer_id) = tdor.dealer_id\n" );
		sb.append("   and tdor.org_id = torg.org_id");
		DaoFactory.getsql(sb, "tawor.id",id, 1);
		return this.pageQueryMap(sb.toString(), null, getFunName());
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> returnAmountAuditListData(RequestWrapper request, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select tawor.id tawor_id,\n" );
		sb.append("       tawor.yieldly,\n" );
		sb.append("       tawor.sign_no,\n" );
		sb.append("       tawor.transport_no as transport_no,\n" );
		sb.append("       tawor.id,\n" );
		sb.append("       td.dealer_code,\n" );
		sb.append("       td.dealer_shortname dealer_name,\n" );
		sb.append("       tawor.tel,\n" );
		sb.append("       tawor.return_no,\n" );
		sb.append("       to_char(tawor.sign_date, 'YYYY-MM-DD hh24:mi') create_date,\n" );
		sb.append("       (select count(*)\n" );
		sb.append("          from tt_as_wr_returned_order_detail d\n" );
		sb.append("         where d.return_id = tawor.id\n" );
		sb.append("           and d.box_no is null) as box_no,\n" );
		sb.append("       TO_CHAR(tawor.WR_START_DATE, 'yyyy-mm-dd') || '至' ||TO_CHAR(tawor.RETURN_END_DATE, 'yyyy-mm-dd') as wr_start_date,\n" );
		sb.append("       to_char(tawor.return_date, 'YYYY-MM-DD hh24:mi') return_date,\n" );
		sb.append("       tawor.return_type,\n" );
		sb.append("       tc.code_desc return_desc,\n" );
		sb.append("       nvl(tawor.wr_amount, 0) wr_amount,\n" );
		sb.append("       nvl(tawor.parkage_amount, 0) parkage_amount,\n" );
		sb.append("       nvl(part_amount, 0) part_amount,\n" );
		sb.append("       tawor.status back_type,\n" );
		sb.append("       tcc.code_desc back_desc,\n" );
		sb.append("       tawor.tran_no trans_no,\n" );
		sb.append("       u.name in_warhouse_name\n" );
		sb.append("  from tt_as_wr_returned_order tawor,\n" );
		sb.append("       tc_user               u,\n" );
		sb.append("       tm_dealer             td,\n" );
		sb.append("       tc_code               tc,\n" );
		sb.append("       tc_code               tcc\n" );
		sb.append(" where tawor.dealer_id = td.dealer_id(+)\n" );
		sb.append("   and tawor.transport_type = tc.code_id(+)\n" );
//		sb.append("   and tawor.is_status = 1\n" );
		sb.append("   and tawor.status = tcc.code_id\n" );
		sb.append("   and tawor.sign_person = u.user_id(+)\n" );
//		sb.append("   and tawor.yieldly = '95411001'\n" );
		sb.append("   and tawor.status = "+Constant.BACK_LIST_STATUS_05+"\n" );//已审核
//		sb.append("   and tawor.sign_no is not null\n" );
		DaoFactory.getsql(sb, "td.dealer_code", DaoFactory.getParam(request, "dealerCode").toUpperCase(), 2);
		DaoFactory.getsql(sb, "td.dealer_shortname", DaoFactory.getParam(request, "dealerName"), 2);
		DaoFactory.getsql(sb, "tawor.return_no", DaoFactory.getParam(request, "back_order_no"), 2);
		DaoFactory.getsql(sb, "tawor.in_warhouse_date", DaoFactory.getParam(request, "create_start_date"), 3);
		DaoFactory.getsql(sb, "tawor.in_warhouse_date", DaoFactory.getParam(request, "create_end_date"), 4);
		DaoFactory.getsql(sb, "tawor.return_date", DaoFactory.getParam(request, "report_start_date"), 3);
		DaoFactory.getsql(sb, "tawor.return_date", DaoFactory.getParam(request, "report_end_date"), 4);
		DaoFactory.getsql(sb, "tawor.transport_type", DaoFactory.getParam(request, "freight_type"), 1);
		DaoFactory.getsql(sb, "tawor.transport_no", DaoFactory.getParam(request, "transport_no"), 2);
		DaoFactory.getsql(sb, "tawor.return_type", DaoFactory.getParam(request, "type_name"), 1);
//		sb.append(" group by tawor.id,\n" );
//		sb.append("          tawor.transport_no,\n" );
//		sb.append("          td.dealer_code,\n" );
//		sb.append("          td.dealer_shortname,\n" );
//		sb.append("          tawor.return_no,\n" );
//		sb.append("          tawor.sign_date,\n" );
//		sb.append("          tawor.tel,\n" );
//		sb.append("          tawor.create_date,\n" );
//		sb.append("          tawor.return_date,\n" );
//		sb.append("          tawor.return_type,\n" );
//		sb.append("          tawor.yieldly,\n" );
//		sb.append("          tawor.sign_no,\n" );
//		sb.append("          tc.code_desc,\n" );
//		sb.append("          tawor.status,\n" );
//		sb.append("          tcc.code_desc,\n" );
//		sb.append("          tawor.tran_no,\n" );
//		sb.append("          u.name\n" );
		sb.append(" order by tawor.create_date desc");
		PageResult<Map<String, Object>> list = pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public int returnAmountAudit(String id, String price, String signRemark, AclUserBean loginUser) {
		int res=1;
		try {
			StringBuffer sb= new StringBuffer();
			if(!"".equals(id)){
				signRemark=new String(signRemark.getBytes("ISO-8859-1"),"GBK");
				sb.append("update tt_as_wr_old_returned t set t.status=10811003,t.sign_remark='"+signRemark+"',t.auth_price='"+price+"' where t.id='"+id+"'");
				this.update(sb.toString(), null);
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public String checkBoxTheSame(RequestWrapper request) {
		String ids = DaoFactory.getParam(request, "ids");
		StringBuffer sb= new StringBuffer();
		sb.append("select count(*) as count from (select d.id,\n" );
		sb.append("               d.create_date,\n" );
		sb.append("               d.is_in_house,\n" );
		sb.append("               d.sign_amount,\n" );
		sb.append("               d.is_out,\n" );
		sb.append("               (select a.claim_type\n" );
		sb.append("                  from tt_as_wr_application a\n" );
		sb.append("                 where a.claim_no = d.claim_no) as claim_type,\n" );
		sb.append("               d.claim_no\n" );
		sb.append("          from tt_as_wr_old_returned_detail d) c\n" );
		sb.append(" where 1 = 1\n" );
		sb.append("   and c.claim_type is not null and c.claim_type =10661006  ");
		DaoFactory.getsql(sb, "c.id", DaoFactory.getSqlByarrIn(ids), 6);
		Map<String,Object> map = this.pageQueryMap(sb.toString(), null, getFunName());
		String count = BaseUtils.checkNull(map.get("COUNT"));
		String[] split = ids.split(",");
		String res="";
		if(Integer.parseInt(count)!=0){
			if(Integer.parseInt(count)!=split.length){
				res+="服务活动的件不能和其他的类型的件";
			}
		}
		return res;
	}
     //未上报和常规回运
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> oldPartApplyList(RequestWrapper request, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select distinct  t.*,\n" );
		sb.append("       c.name as user_name,\n" );
		sb.append("       tm.dealer_code,\n" );
		sb.append("       tm.dealer_shortname\n" );
		sb.append("   from tt_AS_old_return_apply t, tc_user c, tm_dealer tm left join tt_as_wr_returned_order r on r.dealer_id=tm.dealer_id \n" );
		sb.append("   where 1 = 1\n" );
		sb.append("   and t.apply_id = c.user_id\n" );
		sb.append("   and tm.dealer_id = c.dealer_id\n");
		sb.append("   and r.status = '13121001' ");
		sb.append("    and r.return_type = '10731002' ");
		DaoFactory.getsql(sb, "t.status", DaoFactory.getParam(request, "status"), 1);
		DaoFactory.getsql(sb, "t.create_date", DaoFactory.getParam(request, "start_date"), 3);
		DaoFactory.getsql(sb, "t.create_date", DaoFactory.getParam(request, "end_date"), 4);
		DaoFactory.getsql(sb, "c.name", DaoFactory.getParam(request, "user_name"), 2);
		PageResult<Map<String, Object>> list = pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> oldPartApplyList1(RequestWrapper request, Integer pageSize, Integer currPage) {
		// 处理当前页
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage")) : 1;
		// 查询语句
		StringBuffer sqlStr = new StringBuffer();
		StringBuffer whereStr = new StringBuffer();
		StringBuffer orderByStr = new StringBuffer();
		sqlStr.append("select tawor.id,tawor.return_no,tawor.create_date,tawor.return_date,\n");
		sqlStr.append("tawor.wr_amount,tawor.part_item_amount,tawor.arrive_date,tawor.part_amount,tawor.parkage_amount,\n");
		sqlStr.append("tc.code_desc status_desc,tawor.status,tce.code_desc freight_type\n");
		sqlStr.append("from TT_AS_WR_OLD_RETURNED tawor,tc_code tc,tc_code tce\n");
		sqlStr.append("where tawor.status=tc.code_id(+) and tawor.transport_type=tce.code_id(+)\n");
		PageResult<Map<String, Object>> list = pageQuery(sqlStr.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public int oldPartApplyAddSure(RequestWrapper request, AclUserBean loginUser) {
		int res=1;
		String identify = DaoFactory.getParam(request, "identify");
		String id = DaoFactory.getParam(request, "id");
		String start_date = DaoFactory.getParam(request, "start_date");
		String end_date = DaoFactory.getParam(request, "end_date");
		String apply_reason = DaoFactory.getParam(request, "apply_reason");
		String claim_singular = DaoFactory.getParam(request, "Claim_singular");//索赔申请单数
		String accessories_num = DaoFactory.getParam(request, "accessories_num");//配件个数
		try {
			if("".equals(id)){
				TtAsOldReturnApplyPO t=new TtAsOldReturnApplyPO();
				t.setDealerId(BaseUtils.ConvertLong(loginUser.getDealerId()));
				t.setApplyId(loginUser.getUserId());
				t.setApplyReason(apply_reason);
				t.setStartDate(CommonUtils.parseDate(start_date, "yyyy-MM"));
				t.setEndDate(CommonUtils.parseDate(end_date, "yyyy-MM"));
				t.setCreateDate(new Date());
				t.setId(DaoFactory.getPkId());
				if("0".equals(identify)){
					t.setStatus(93451001);
				}else{
					t.setStatus(93451002);
				}
				this.insert(t);
			}else{
				TtAsOldReturnApplyPO t1=new TtAsOldReturnApplyPO();
				t1.setId(BaseUtils.ConvertLong(id));
				TtAsOldReturnApplyPO t2=new TtAsOldReturnApplyPO();
				t2.setApplyReason(apply_reason);
				t2.setStartDate(CommonUtils.parseDate(start_date, "yyyy-MM"));
				t2.setEndDate(CommonUtils.parseDate(end_date, "yyyy-MM"));
				if("0".equals(identify)){
					t2.setStatus(93451001);
				}else{
					t2.setStatus(93451002);
				}
				this.update(t1, t2);
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public int oldPartApplyAudit(RequestWrapper request) {
		int res=1;
		try {
			String status = DaoFactory.getParam(request, "status");
			String rebut_reason = DaoFactory.getParam(request, "rebut_reason");
			this.update("update Tt_As_Old_Return_Apply t set t.status='"+status+"',t.rebut_reason='"+rebut_reason+"'", null);
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean checkDateOldReturn(RequestWrapper request, AclUserBean loginUser) {
		boolean flag=true;
		String start_date = DaoFactory.getParam(request, "start_date");
		String dealer_id = DaoFactory.getParam(request, "dealer_id");
		String type = DaoFactory.getParam(request, "type");
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*\n" );
		sb.append("  from (select t.*\n" );
		sb.append("          from (select to_date(tawor.wr_start_date, 'yyyy-mm-dd') as wr_start_date,\n" );
		sb.append("                       tawor.return_end_date,\n" );
		sb.append("                       tawor.create_date,\n" );
		sb.append("                       tawor.return_type\n" );
		sb.append("                  from TT_AS_WR_RETURNED_ORDER tawor\n" );
		sb.append("                 where 1=1 \n" );
		if("add".equals(type)){
			sb.append("  and tawor.DEALER_ID = '"+loginUser.getDealerId()+"'\n" );
		}else{
			sb.append("  and  tawor.DEALER_ID = '"+dealer_id+"'\n" );
		}
		sb.append("                   and tawor.return_type = 10731002\n" );
		sb.append("                 order by tawor.create_date desc) t\n" );
		sb.append("         where rownum = 1) t\n" );
		sb.append(" where 1 = 1 ");
		DaoFactory.getsql(sb, "t.return_end_date", start_date+"-26", 3);
		List<Map<String, Object>> list = this.pageQuery(sb.toString(), null, getFunName());
		if(DaoFactory.checkListNull(list)){//如果有值就代表在范围内
			flag=false;
		}
		return flag;
	}

	public Long insertreturnapply(RequestWrapper request) {
		System.out.println("申请延期执行插入");
		String RETURN_NO = DaoFactory.getParam(request, "RETURN_NO");
		String DEALER_ID = DaoFactory.getParam(request, "DEALER_ID");
		String WR_AMOUNT = DaoFactory.getParam(request, "WR_AMOUNT");
		String PARKAGE_AMOUNT =DaoFactory.getParam(request, "PARKAGE_AMOUNT");
		String PART_AMOUNT = DaoFactory.getParam(request, "PART_AMOUNT");
		String CREATE_DATE = DaoFactory.getParam(request, "CREATE_DATE");
		String STATUS_DESC = DaoFactory.getParam(request, "STATUS_DESC");
		AclUserBean loginUser = (AclUserBean) ActionContext.getContext().getSession().get(Constant.LOGON_USER);
		Long applyid =  loginUser.getUserId();
		String WR_START_DATE = DaoFactory.getParam(request, "WR_START_DATE");
		String applyreson = DaoFactory.getParam(request, "remark");
		String RETURN_END_DATE = DaoFactory.getParam(request, "RETURN_END_DATE");
		StringBuffer sql  = new StringBuffer();
		Long pkid = DaoFactory.getPkId();
		sql.append("insert into tt_as_old_return_apply(ID,DEALER_ID,STATUS,APPLY_REASON,START_DATE,END_DATE,APPLY_ID,CREATE_DATE,RETURN_NO,PART_AMOUNT,WR_AMOUNT,PARKAGE_AMOUNT,Apply_date)\n ");
		sql.append("values("+pkid+","+DEALER_ID+","+93451001+",'"+applyreson +"',to_date('"+WR_START_DATE+"','yyyy-mm-dd'),to_date('"+RETURN_END_DATE+"','yyyy-mm-dd'),"+applyid+",to_date('"+CREATE_DATE+"','yyyy-mm-dd HH24:mi:ss '),to_char('"+RETURN_NO+"'),"+PART_AMOUNT+","+WR_AMOUNT+","+PARKAGE_AMOUNT+",sysdate");
		sql.append(")");
		this.insert(sql.toString());
		return pkid;
		
		
	}
    
	@SuppressWarnings("unchecked")
	public int oldParReport(RequestWrapper request) {
		String str = DaoFactory.getParam(request, "id");
		String[] ss = str.split(",");
		StringBuffer sql  = new StringBuffer();
		sql.append("update tt_AS_old_return_apply tt set tt.status = 93451002 where tt.id ="+ss[0]);
		int a =	this.update(sql.toString(), null);
	    return a;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> finddatabyreturnno(RequestWrapper request) {
		String id = DaoFactory.getParam(request, "id");
		Map<String, Object> map = null;
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select a.*,tm.DEALER_NAME,tc.code_desc,u.name as USER_NAME,us.name as audit_man1 from  tt_AS_old_return_apply a left join tc_code tc on tc.code_id=a.status left join tc_user u on a.apply_id = u.user_id left join  tc_user us on  a.Audit_man=us.user_id left join tm_dealer tm on tm.dealer_id=a.dealer_id where 1=1\n");
		sqlStr.append(" and a.id = '"+id+"'");
		map = this.pageQueryMap(sqlStr.toString(), null, getFunName());
		return map;
	}
    //车厂
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> oldPartApplyAuditList(
			RequestWrapper request, Integer pageSize, Integer currPage) {
		PageResult<Map<String, Object>> list=null;
		String start_date = DaoFactory.getParam(request, "start_date");
		String end_date = DaoFactory.getParam(request, "end_date");
		String user_name = DaoFactory.getParam(request, "user_name");
		try {
			user_name =	new String(user_name.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String status = DaoFactory.getParam(request, "status");
		String RETURN_NO = DaoFactory.getParam(request, "RETURN_NO");
		StringBuffer sqlStr = new StringBuffer();
		
		sqlStr.append("select a.*,tm.DEALER_SHORTNAME,tm.DEALER_CODE,tc.code_desc,u.name as USER_NAME,us.name as audit_man1 from  tt_AS_old_return_apply a left join tc_code tc on tc.code_id=a.status left join tc_user u on a.apply_id = u.user_id left join  tc_user us on  a.Audit_man=us.user_id ");
		   sqlStr.append(" left join tm_dealer tm on tm.dealer_id=a.dealer_id");
		   sqlStr.append(" where 1=1\n");
		    DaoFactory.getsql(sqlStr, "a.start_date", start_date, 3);
			DaoFactory.getsql(sqlStr, "tc.CODE_DESC", status, 1);
			DaoFactory.getsql(sqlStr, "a.end_date",end_date, 4);
			DaoFactory.getsql(sqlStr, "u.name", user_name, 2);
			DaoFactory.getsql(sqlStr, "a.RETURN_NO", RETURN_NO, 2);
		list = this.pageQuery(sqlStr.toString(), null, getFunName(), pageSize, currPage);
		return list;
	}
	//服务站
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> oldPartApplyAuditList1(
			RequestWrapper request, Integer pageSize, Integer currPage) {
		PageResult<Map<String, Object>> list=null;
		String start_date = DaoFactory.getParam(request, "start_date");
		String end_date = DaoFactory.getParam(request, "end_date");
		String user_name = DaoFactory.getParam(request, "user_name");
		try {
			user_name =	new String(user_name.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String status = DaoFactory.getParam(request, "status");
		String RETURN_NO = DaoFactory.getParam(request, "RETURN_NO");
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select a.*,tm.DEALER_SHORTNAME,tm.DEALER_CODE,tc.code_desc,u.name as USER_NAME,us.name as audit_man1 from  tt_AS_old_return_apply a left join tc_code tc on tc.code_id=a.status left join tc_user u on a.apply_id = u.user_id left join  tc_user us on  a.Audit_man=us.user_id ");
		   sqlStr.append(" left join tm_dealer tm on tm.dealer_id=a.dealer_id");
		   sqlStr.append(" where 1=1\n");
		   AclUserBean loginUser = (AclUserBean) ActionContext.getContext().getSession().get(Constant.LOGON_USER);
			String deaclerid =  loginUser.getDealerId();
		   sqlStr.append(" and tm.dealer_id='"+deaclerid+"'");
		    DaoFactory.getsql(sqlStr, "a.start_date", start_date, 3);
			DaoFactory.getsql(sqlStr, "tc.CODE_DESC", status, 1);
			DaoFactory.getsql(sqlStr, "a.end_date",end_date, 4);
			DaoFactory.getsql(sqlStr, "u.name", user_name, 2);
			DaoFactory.getsql(sqlStr, "a.RETURN_NO", RETURN_NO, 2);
		list = this.pageQuery(sqlStr.toString(), null, getFunName(), pageSize, currPage);
		return list;
	}
	@SuppressWarnings("unchecked")
	public int updateOldPartApplybyID(RequestWrapper request) {
		String id = DaoFactory.getParam(request, "ID");
		StringBuffer sqlStr = new StringBuffer();
        try {
        	String REBUT_REASON1 = DaoFactory.getParam(request, "REBUT_REASON");
        	String REBUT_REASON = new String(REBUT_REASON1.getBytes("iso8859-1"),"GBK");
			sqlStr.append("update tt_as_old_return_apply tt\n");
			AclUserBean loginUser = (AclUserBean) ActionContext.getContext().getSession().get(Constant.LOGON_USER);
			Long Audit_man =  loginUser.getUserId();
			sqlStr.append("   set tt.rebut_reason = '"+REBUT_REASON+"',tt.Audit_date=sysdate,tt. Audit_man="+Audit_man+", tt.status = 93451003\n" );
			sqlStr.append(" where tt.id ="+id);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		return this.update(sqlStr.toString(), null);
	}

	@SuppressWarnings("unchecked")
	public int updateOldPartApplybyID1(RequestWrapper request) {
		String id = DaoFactory.getParam(request, "ID");
		StringBuffer sqlStr = new StringBuffer();
		AclUserBean loginUser = (AclUserBean) ActionContext.getContext().getSession().get(Constant.LOGON_USER);
		Long Audit_man =  loginUser.getUserId();
        try {
        	String REBUT_REASON1 = DaoFactory.getParam(request, "REBUT_REASON");
        	String REBUT_REASON = new String(REBUT_REASON1.getBytes("iso8859-1"),"GBK");
			sqlStr.append("update tt_as_old_return_apply tt\n");
			sqlStr.append("   set tt.rebut_reason = '"+REBUT_REASON+"',tt.Audit_date=sysdate,tt. Audit_man="+Audit_man+", tt.status = 93451004\n" );
			sqlStr.append(" where tt.id ="+id);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		return this.update(sqlStr.toString(), null);
	}
   //修改回运主表状态
	@SuppressWarnings("unchecked")
	public int updateOldreturnbyID(RequestWrapper request) {
		System.out.println("审核通过修改回运主表状态");
		String RETURN_NO = DaoFactory.getParam(request, "RETURN_NO");
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("update tt_as_wr_old_returned d set  d.status=10811006,d.is_delay=2 where d.return_no='"+RETURN_NO+"'");
		return this.update(sqlStr.toString(), null);
	}
      //检测是否已上报
	@SuppressWarnings("unchecked")
	public int finddatabyreturnnoAndid(RequestWrapper request) {
		System.out.println("开始检测是否已上报");
		String str = DaoFactory.getParam(request, "id");
		String[] ss = str.split(",");
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select * from   tt_as_old_return_apply a where  a.return_no='"+ss[1]+"' and a.status in (93451002,93451003 )");
        List<Object> list = this.pageQuery(sqlStr.toString(),null,getFunName());
		return list.size();
	}

	@SuppressWarnings("unchecked")
	public int updateOldreturnapplybyID(RequestWrapper request) {
		String str = DaoFactory.getParam(request, "id");
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("update   tt_as_old_return_apply   a  set a.status='93451001' where a.id="+str);
		return this.update(sqlStr.toString(), null);
	}
    //修改回运主表状态是否延期
	@SuppressWarnings("unchecked")
	public int updatepart_old_returned(RequestWrapper request) {
		String str = DaoFactory.getParam(request, "id");
		String[] parms = str.split(",");//
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("update   tt_as_wr_old_returned   a  set a.IS_DELAY= 2  where a.return_no = '"+parms[1]+"'");
		return this.update(sqlStr.toString(),null);
	}
	@SuppressWarnings("unchecked")
	public int updatepart_old_returned1(RequestWrapper request) {
		String RETURN_NO = DaoFactory.getParam(request, "RETURN_NO");
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("update   tt_as_wr_old_returned   a  set a.IS_DELAY= 1  where a.return_no = '"+RETURN_NO+"'");
		return this.update(sqlStr.toString(),null);
	}

	@SuppressWarnings("unchecked")
	public int updateOldPartApplyresonbyID(RequestWrapper request) {
		String id = DaoFactory.getParam(request, "id");
		String apply_reason = DaoFactory.getParam(request, "apply_reason");
		try {
			apply_reason = new String(apply_reason.getBytes("iso8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("update tt_as_old_return_apply tt set tt.apply_reason = '"+apply_reason+"' where tt.id="+id);
		return this.update(sqlStr.toString(), null);
	}

	@SuppressWarnings("unchecked")
	public int oldPartcheck(RequestWrapper request) {
		String value = DaoFactory.getParam(request, "return_no");
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select * from   tt_as_old_return_apply a where  a.return_no='"+value+"' and a.status in (93451001,93451002,93451003 )");
        List<Object> list = this.pageQuery(sqlStr.toString(),null,getFunName());
        
		return list.size();
	}

	@SuppressWarnings("unchecked")
	public int updateOldreturnstatus(RequestWrapper request) {
		String id = DaoFactory.getParam(request, "id");
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("update   tt_as_wr_old_returned tt     set tt.IS_DELAY= 1  where  tt.return_no in (select b.return_no from tt_as_old_return_apply b where b.id = '"+id+"' )" );
		return this.update(sqlStr.toString(),null);
	}

	@SuppressWarnings("unchecked")
	public int updatereturnedstatusbyreturnno(Long pkid) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("update   tt_as_wr_old_returned tt     set tt.IS_DELAY= 2  where  tt.return_no in (select b.return_no from tt_as_old_return_apply b where b.id = '"+pkid+"' )" );
		return this.update(sqlStr.toString(),null);
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> QueryEmergencyTracking(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		    String dealer_id = DaoFactory.getParam(request, "dealerId");
			String borrowPerson = DaoFactory.getParam(request, "borrowPerson");
			String borrowDept = DaoFactory.getParam(request, "borrowDept");
			String consigneePerson = DaoFactory.getParam(request, "consigneePerson");
			String beginTime = DaoFactory.getParam(request, "beginTime");
			String endTime = DaoFactory.getParam(request, "endTime");
			String status = DaoFactory.getParam(request, "status");
			String is_return = DaoFactory.getParam(request, "is_return");
			StringBuffer sb= new StringBuffer();
			sb.append("SELECT T.*,tt.claim_no,tt.part_code,tt.part_name, se.root_org_name,(SELECT TM.DEALER_SHORTNAME FROM TM_DEALER TM WHERE TM.DEALER_ID=T.DEALER_ID) AS DEALER_SHORTNAME FROM TT_AS_PART_BORROW T,TT_AS_PART_BORROW_SUBCLASS tt,vw_org_dealer_service se  WHERE 1=1  and t.id=tt.parent_id(+) and t.dealer_code=se.dealer_code(+) ");
			DaoFactory.getsql(sb, "T.DEALER_ID", dealer_id, 6);
			DaoFactory.getsql(sb, "T.BORROW_PERSON", borrowPerson, 2);
			DaoFactory.getsql(sb, "T.BORROW_DEPT", borrowDept, 2);
			DaoFactory.getsql(sb, "T.STATUS", status, 1);
			DaoFactory.getsql(sb, "T.CONSIGNEE_PERSON", consigneePerson, 2);
			DaoFactory.getsql(sb, "T.NEXT_TIME", beginTime, 3);
			DaoFactory.getsql(sb, "T.NEXT_TIME", endTime, 4);
			DaoFactory.getsql(sb, "T.IS_RETURN", is_return,1);
			String claim_no = DaoFactory.getParam(request, "claim_no");
			if(BaseUtils.testString(claim_no)){
				sb.append(" and t.id in (select d.parent_id from \n" );
				sb.append("               TT_AS_PART_BORROW_SUBCLASS d where 1=1 \n" );
				DaoFactory.getsql(sb, "d.claim_no", claim_no, 2);
				sb.append("   ) \n");
			}
			String borrow_man = DaoFactory.getParam(request, "borrow_man");
			/**
			 * 增加借出人查询条件 2015-2-3
			 */
			if(BaseUtils.testString(borrow_man)){
				sb.append(" and t.id in (select d.parent_id from Tt_As_Part_Borrow_Store s,\n" );
				sb.append("               TT_AS_PART_BORROW_SUBCLASS d where\n" );
				sb.append("               s.id = d.claim_id \n");
				DaoFactory.getsql(sb, "s.borrow_man", borrow_man, 2);
				sb.append("   ) \n");
			}
			sb.append(" ORDER BY T.CREATE_DATE DESC ");
			PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
			return list;
	}

	@SuppressWarnings("unchecked")
	public int addsure(RequestWrapper request, AclUserBean loginUser) {
		TtAsPartBorrowPO po = new TtAsPartBorrowPO();
		TtAsPartBorrowPO po1 = new TtAsPartBorrowPO();
		po.setId(Long.valueOf(DaoFactory.getParam(request, "id")));
		List<TtAsPartBorrowPO> borrowPO =  this.select(po);
	    String remark =	DaoFactory.getParam(request, "remark");
			if (BaseUtils.notNull(remark)) {
				po1.setRemark(remark);
			}
		int res =this.update(po, po1);//添加备注
		//插入日志
		TtAsPartBorrowPO borrow = borrowPO.get(0);
	    Long borrowid =	borrow.getId();
	    Integer type = loginUser.getUserType();
	    Long status = borrow.getStatus();
	    TtAsComRecordPO recordPO = new TtAsComRecordPO();
	    recordPO.setId(DaoFactory.getPkId());
	    recordPO.setBizId(borrowid);
	    recordPO.setCreateBy(loginUser.getUserId());
	    recordPO.setCreateDate(new Date());
	    recordPO.setRemark("添加备注");
	    recordPO.setStatus(Integer.valueOf(status.toString()));
	    recordPO.setRoleName(type.toString());
	    this.insert(recordPO);
		return res; 
	}

	@SuppressWarnings("unchecked")
	public List<TtAsPartBorrowPO> queryEmergencyremarkByid(RequestWrapper request, AclUserBean loginUser) {
	  	TtAsPartBorrowPO asPartBorrowPO = new TtAsPartBorrowPO();
	  	asPartBorrowPO.setId(Long.valueOf(DaoFactory.getParam(request, "id")));
	  	List<TtAsPartBorrowPO> list =this.select(asPartBorrowPO);
		return list;
	}

	@SuppressWarnings("unchecked")
	public ClaimApproveAndStoredReturnInfoBean getApproveAndStoredReturnInfo11(Map<String, String> params) {
		String claim_id=ClaimTools.dealParamStr(params.get("i_claim_id"));
		if(!"".equals(claim_id)){
			StringBuffer sb= new StringBuffer();
			sb.append("select tawor.yieldly,\n" );
			sb.append("       tawor.status,\n" );
			sb.append("       tawor.price,\n" );
			sb.append("       tawor.auth_price new_price,\n" );
			sb.append("       tawor.id,\n" );
			sb.append("       tm.dealer_code,\n" );
			sb.append("       tm.dealer_name,\n" );
			sb.append("       tawor.tel,\n" );
			sb.append("       tawor.price_remark,\n" );
			sb.append("       torg.org_name attach_area,\n" );
			sb.append("       d.transport_name，d.transport_id,\n" );
			sb.append("       tawor.transport_type,\n" );
			sb.append("       (select c.code_desc from tc_code c where c.code_id=tawor.transport_type) as transport_desc,\n" );
			sb.append("       tawor.parkage_amount,\n" );
			sb.append("       tawor.return_no,\n" );
			sb.append("       tawor.remark,\n" );
			sb.append("       to_char(tawor.Send_Time, 'YYYY-MM-DD') create_date,\n" );
			sb.append("       to_char(tawor.return_date, 'YYYY-MM-DD') return_date,\n" );
			sb.append("       tawor.wr_amount,\n" );
			sb.append("       tawor.part_item_amount,\n" );
			sb.append("       tawor.part_amount,\n" );
			sb.append("       tawor.return_type,\n" );
			sb.append("       tawor.out_part_packge,\n" );
			sb.append("       tawor.sign_remark,\n" );
			sb.append("        (select c.code_desc from tc_code c where c.code_id=tawor.return_type) as return_desc,\n" );
			sb.append("       tawor.wr_start_date,\n" );
			sb.append("       tawor.tran_no tran_no,\n" );
			sb.append("       tawor.transport_no,\n" );
			sb.append("       tawor.real_box_no,\n" );
			sb.append("       tawor.part_pakge,\n" );
			sb.append("       tawor.part_mark,tawor.IN_WARHOUSE_NAME,  \n" );
			sb.append("       tawor.part_detail\n" );
			sb.append("  from tt_as_wr_old_returned       tawor,\n" );
			sb.append("       tm_oldpart_transport_detail d,\n" );
			sb.append("       tm_dealer                   tm,\n" );
			sb.append("       tm_dealer_org_relation      tdor,\n" );
			sb.append("       tm_org                      torg\n" );
			sb.append(" where 1=1\n" );
			sb.append("   and tawor.dealer_id = tm.dealer_id\n" );
			sb.append("   AND d.detail_id(+) = tawor.tran_person\n" );
			sb.append("   and f_get_pid(tawor.dealer_id) = tdor.dealer_id\n" );
			sb.append("   and tdor.org_id = torg.org_id");
			DaoFactory.getsql(sb, "tawor.id", claim_id, 1);
			PageResult<ClaimApproveAndStoredReturnInfoBean> pr = pageQuery(ClaimApproveAndStoredReturnInfoBean.class,sb.toString(), null,10,1);
			if(pr!=null&&pr.getTotalPages()>0){
				return (ClaimApproveAndStoredReturnInfoBean)pr.getRecords().get(0);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryClaimBackDetailList2(Map<String, String> params) {
		String claim_id=ClaimTools.dealParamStr(params.get("i_claim_id"));
		String boxNo=ClaimTools.dealParamStr(params.get("boxNo"));
		String partName = ClaimTools.dealParamStr(params.get("partName"));
		String partCode = ClaimTools.dealParamStr(params.get("partCode"));
		if(!"".equals(claim_id)){
			StringBuffer sqlStr= new StringBuffer();
			sqlStr.append("select * from (select taword.id,taword.claim_no,taword.vin,c.id as claim_id,taword.PRODUCER_NAME,taword.producer_code,to_char(c.RO_STARTDATE,'yyyy-mm-dd') as RO_STARTDATE,c.REMARK,del.dealer_code,\n" );
			sqlStr.append("tawp.part_code,tawp.part_name,nvl(sum(taword.return_amount),0) return_amount,\n" );
			sqlStr.append("nvl(sum(taword.sign_amount),0) sign_amount,\n" );
			sqlStr.append("taword.box_no,taword.warehouse_region,taword.deduct_remark,nvl(tc.code_desc,'') deduct_desc,taword.barcode_no\n" );
			sqlStr.append("from tt_as_wr_old_returned tawor,\n" );
			sqlStr.append("tt_as_wr_old_returned_detail taword,\n" );
			sqlStr.append("tt_as_wr_partsitem tawp,tc_code tc,TT_AS_WR_APPLICATION c,tm_dealer del\n" );
			sqlStr.append("where tawor.id=taword.return_id and taword.part_id=tawp.part_id and taword.deduct_remark=tc.code_id(+)\n" );
			sqlStr.append("and taword.is_sign=0 and tawor.id="+claim_id+" and c.id = taword.claim_id and del.dealer_id=c.dealer_id\n" );
			if(!"".equals(boxNo)){
				sqlStr.append("and taword.box_no='"+boxNo+"'\n" );
			}
			if(Utility.testString(partCode)){
				sqlStr.append(" and taword.part_code like'%"+partCode+"%'\n" );
			}
			if(Utility.testString(partName)){
				sqlStr.append(" and taword.part_Name like'%"+partName+"%'\n" );
			}
			sqlStr.append("group by taword.id,taword.claim_no,taword.vin,c.id,taword.PRODUCER_NAME,taword.barcode_no,taword.producer_code,c.RO_STARTDATE,c.REMARK,del.dealer_code,\n" );
			sqlStr.append("tawp.part_code,tawp.part_name,tc.code_desc,\n" );
			sqlStr.append("taword.box_no,taword.warehouse_region,taword.deduct_remark,taword.create_date\n");
			
			sqlStr.append("union all\n" );
			
			sqlStr.append("select taword.id,taword.claim_no,taword.vin,c.id as claim_id,taword.PRODUCER_NAME,taword.producer_code,to_char(c.RO_STARTDATE,'yyyy-mm-dd') as RO_STARTDATE,c.REMARK,del.dealer_code,\n" );
			sqlStr.append("tawp.part_code,tawp.part_name,nvl(sum(taword.return_amount),0) return_amount,\n" );
			sqlStr.append("nvl(sum(taword.sign_amount),0) sign_amount,\n" );
			sqlStr.append("taword.box_no,taword.warehouse_region,taword.deduct_remark,nvl(tc.code_desc,'') deduct_desc,taword.barcode_no\n" );
			sqlStr.append("from tt_as_wr_old_returned tawor,\n" );
			sqlStr.append("tt_as_wr_old_returned_detail taword,\n" );
			sqlStr.append("tt_as_wr_partsitem tawp,tc_code tc,tt_as_wr_application_backup c,tm_dealer del\n" );
			sqlStr.append("where tawor.id=taword.return_id and taword.part_id=tawp.part_id and taword.deduct_remark=tc.code_id(+)\n" );
			sqlStr.append("and taword.is_sign=0 and tawor.id="+claim_id+" and  c.id = taword.claim_id and del.dealer_id=c.dealer_id\n" );
			if(!"".equals(boxNo)){
				sqlStr.append("and taword.box_no='"+boxNo+"'\n" );
			}
			if(Utility.testString(partCode)){
				sqlStr.append(" and taword.part_code like'%"+partCode+"%'\n" );
			}
			if(Utility.testString(partName)){
				sqlStr.append(" and taword.part_Name like'%"+partName+"%'\n" );
			}
			sqlStr.append("group by taword.id,taword.claim_no,taword.barcode_no,taword.vin,taword.barcode_no,c.id,taword.PRODUCER_NAME,taword.producer_code,c.RO_STARTDATE,c.REMARK,del.dealer_code,\n" );
			sqlStr.append("tawp.part_code,tawp.part_name,tc.code_desc,\n" );
			sqlStr.append("taword.box_no,taword.warehouse_region,taword.deduct_remark,taword.create_date)\n");
			sqlStr.append("order by  box_no,claim_no,part_name,barcode_no ");
			System.out.println("sql==="+sqlStr);
			List<Map<String,Object>> pr = this.pageQuery(sqlStr.toString(), null, this.getFunName());
			if(pr!=null){
				return pr;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryClaimBackDetailList3(String claimId) {
		if(!"".equals(claimId)){
			StringBuffer sqlStr= new StringBuffer();	
			sqlStr.append("SELECT OO.WR_START_DATE||'至'||to_char(oo.return_end_date,'yyyy-mm-dd') WR_START_DATE FROM TT_AS_WR_RETURNED_ORDER OO ,tr_return_logistics l where l.logictis_id='"+claimId+"' and l.return_id=oo.id\n" );
			List<Map<String,Object>> pr = this.pageQuery(sqlStr.toString(), null, this.getFunName());
			if(pr!=null){
				return pr;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
		//得到抵扣原因的数据
		@SuppressWarnings("unchecked")
		public List<Map<String,Object>> getDeductList(){
				StringBuffer sqlStr= new StringBuffer();	
				sqlStr.append("SELECT  C.CODE_ID,C.CODE_DESC FROM TC_CODE C WHERE C.TYPE = "+Constant.OLDPART_DEDUCT_TYPE+"\n" );
				List<Map<String,Object>> pr = this.pageQuery(sqlStr.toString(), null, this.getFunName());
				if(pr!=null){
					return pr;
				}else{
					return null;
				}
		}

		@SuppressWarnings("unchecked")
		public int oldPartSignAuditIn(RequestWrapper request,AclUserBean loginUser) {
			String id =DaoFactory.getParam(request, "claimId");
			String type =DaoFactory.getParam(request, "type");
			String remark =DaoFactory.getParam(request, "remark");
			TtAsWrOldReturnedPO oldReturnedPO = new TtAsWrOldReturnedPO();
			TtAsWrOldReturnedPO oldReturnedPO1 = new TtAsWrOldReturnedPO();
			oldReturnedPO.setId(Long.valueOf(id));
//			List<TtAsWrOldReturnedPO> list = this.select(oldReturnedPO);//查询修改前状态
//			Integer status = list.get(0).getStatus();//审核状态
		    TtAsComRecordPO recordPO = new TtAsComRecordPO();
		    recordPO.setCreateBy(loginUser.getUserId());
		    recordPO.setCreateDate(new Date());
		    recordPO.setId(DaoFactory.getPkId());
		    recordPO.setRemark(remark);
		    recordPO.setBizId(Long.valueOf(id));
		    recordPO.setRoleName("质量部");
			if ("1".equals(type)) {
				oldReturnedPO1.setStatus(Constant.BACK_LIST_STATUS_07);//质量部通过
				recordPO.setStatus(Constant.BACK_LIST_STATUS_07);
			}else if ("2".equals(type)) {
//				recordPO.setStatus(Constant.BACK_LIST_STATUS_08);
//				oldReturnedPO1.setStatus(Constant.BACK_LIST_STATUS_08);//质量部退回
			}
			
			TtAsWrOldReturnedDetailPO asWrOldReturnedDetailPO =  new TtAsWrOldReturnedDetailPO();
			asWrOldReturnedDetailPO.setReturnId(Long.valueOf(id));
            List<TtAsWrOldReturnedDetailPO> list2 =this.select(asWrOldReturnedDetailPO);
            TtAsWrOldReturnedDetailPO detailPO =null;
            TtAsWrOldReturnedDetailPO detailPO1 =null;
            for (int i = 0; i < list2.size(); i++) {
            	detailPO = new TtAsWrOldReturnedDetailPO();
            	detailPO1 = new TtAsWrOldReturnedDetailPO();
            	Long id1 = list2.get(i).getId();
            	detailPO.setId(id1);
            	detailPO1.setIsInHouse(Constant.IF_TYPE_NO);//设置为未入库
            	this.update(detailPO,detailPO1);
			}
			this.insert(recordPO);//插入日志
			return this.update(oldReturnedPO,oldReturnedPO1);
		}

		@SuppressWarnings("unchecked")
		public List historyoldreturnquery(RequestWrapper request, AclUserBean loginUser) {
			StringBuffer sql= new StringBuffer();
			sql.append("\n" );
			sql.append("select d.*, tc.code_desc, t.name\n" );
			sql.append("  from tt_as_com_record d, tc_code tc, tc_user t\n" );
			sql.append(" where d.create_by = t.user_id\n" );
			sql.append("   and d.status = tc.code_id");
            DaoFactory.getsql(sql, "biz_id", DaoFactory.getParam(request, "id"), 1);
			return this.pageQuery(sql.toString(), null, getFunName());
		}

		@SuppressWarnings("unchecked")
		public PageResult<Map<String, Object>> QualityAuditlistQuery(RequestWrapper request, AclUserBean loginUser,Integer pageSize, Integer currPage) {
			Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
			String    yieldly = request.getParamValue("YIELDLY_TYPE");//查询条件=产地
			String     dealerCode =	DaoFactory.getParam(request, "dealerCode");
			String     dealerName =	DaoFactory.getParam(request, "dealerName");
			String     back_order_no =	DaoFactory.getParam(request, "back_order_no");
			String     report_start_date =	DaoFactory.getParam(request, "report_start_date");
			String     report_end_date =	DaoFactory.getParam(request, "report_end_date");
			String      create_start_date=	DaoFactory.getParam(request, "create_start_date");
			String      create_end_date=	DaoFactory.getParam(request, "create_end_date");
			String     freight_type =	DaoFactory.getParam(request, "freight_type");
			String      back_type=	DaoFactory.getParam(request, "back_type");
			String      trans_no=	DaoFactory.getParam(request, "trans_no");
			String     in_warhouse_name =	DaoFactory.getParam(request, "in_warhouse_name");
			StringBuffer sqlStr= new StringBuffer();
			sqlStr.append("select (select d.sign_date from tt_as_wr_old_returned d where d.id = tawor.id) as sign_date,");//2014-2-2 zyw 加了一个签收日期
			sqlStr.append(" tawor.create_date as CREATE_DATES,tawor.yieldly,tawor.audit_no,tawor.id,td.dealer_code dealer_id,td.dealer_shortname dealer_name,tawor.tel,\n" );
			sqlStr.append("tawor.return_no claim_no,to_char(tawor.in_warhouse_date,'YYYY-MM-DD hh24:mi') create_date,\n" );
			sqlStr.append("(select count(*) from tt_as_wr_old_returned_detail d where d.return_id=tawor.id and d.box_no is null) as box_no,");
			sqlStr.append("(select oo.WR_START_DATE || '至' || TO_CHAR(oo.RETURN_END_DATE, 'yyyy-mm-dd')  from tt_as_wr_returned_order oo,tr_return_logistics ll where oo.id=ll.return_id and ll.logictis_id=tawor.id) as wr_start_date,");
			sqlStr.append("to_char(tawor.return_date,'YYYY-MM-DD hh24:mi') return_date,\n" );
			sqlStr.append("tawor.return_type,tc.code_desc return_desc,\n" );
			sqlStr.append("nvl(r.wr_amount,0) wr_amount,\n" ); 
			sqlStr.append("nvl(tawor.parkage_amount,0) parkage_amount,\n" );
			sqlStr.append("nvl(r.part_amount,0) part_amount,tawor.status back_type,tcc.code_desc back_desc,tawor.tran_no trans_no,max(tawor.in_warhouse_name) in_warhouse_name ,u.name \n" );
			sqlStr.append("from tt_as_wr_old_returned tawor, tc_user u  ,tm_dealer td,tc_code tc,tc_code tcc,tt_as_wr_returned_order r \n" );
			sqlStr.append("where tawor.dealer_id=td.dealer_id  and r.return_no=tawor.tran_no(+)  and tawor.transport_type=tc.code_id(+) and tawor.is_status=1\n" );
			sqlStr.append("and tawor.status=tcc.code_id   and tawor.in_warhouse_by=u.user_id(+) \n" );
             DaoFactory.getsql(sqlStr, "td.dealer_code", dealerCode, 2);
             DaoFactory.getsql(sqlStr, "tawor.yieldly", yieldly, 1);
             DaoFactory.getsql(sqlStr, "u.name", in_warhouse_name, 2);
             DaoFactory.getsql(sqlStr, "td.dealer_name", dealerName, 2);
             DaoFactory.getsql(sqlStr, "tawor.return_no", back_order_no, 2);
             DaoFactory.getsql(sqlStr, "tawor.in_warhouse_date", create_start_date, 3);
             DaoFactory.getsql(sqlStr, "tawor.in_warhouse_date", create_end_date, 4);
             DaoFactory.getsql(sqlStr, "tawor.return_date", report_start_date, 3);
             DaoFactory.getsql(sqlStr, "tawor.return_date", report_end_date, 4);
             DaoFactory.getsql(sqlStr, "tawor.transport_type", freight_type, 1);
             DaoFactory.getsql(sqlStr, "tawor.tran_no", trans_no, 2);
			if(BaseUtils.notNull(back_type)){
				sqlStr.append(" and tawor.status="+back_type+"\n");
			}else{
				sqlStr.append(" and (tawor.status="+Constant.BACK_LIST_STATUS_05+" )\n");
			}
			sqlStr.append("group by tawor.id,tawor.yieldly,tawor.audit_no,td.dealer_code,td.dealer_shortname,tawor.return_no,tawor.in_warhouse_date,tawor.tel,\n" );
			sqlStr.append("tawor.create_date,tawor.return_date,tawor.return_type,tc.code_desc,tawor.status,tcc.code_desc,tawor.tran_no,u.name,tawor.parkage_amount,\n");
			sqlStr.append(" r.dealer_id,r.wr_amount,r.parkage_amount,r.part_amount,r.tran_no");
			sqlStr.append(" order by tawor.return_date desc\n");
			return this.pageQuery(sqlStr.toString(), null, getFunName(), pageSize, currPage);
		}

		@SuppressWarnings("unchecked")
		public PageResult<Map<String, Object>> returnAmountAuditListNew(RequestWrapper request, AclUserBean loginUser,Integer pageSize, Integer currPage) {
			
			StringBuffer sql= new StringBuffer();
			sql.append("select nvl(a.auth_price, 0) as auth_price,\n" );
			sql.append("       b.wr_start_date,\n" );
			sql.append("       to_char(b.return_end_date, 'yyyy-mm-dd') return_end_date,\n" );
			sql.append("       (b.wr_start_date || '至' || to_char(b.return_end_date, 'yyyy-mm-dd')) return_date,\n" );
			sql.append("       a.return_no,\n" );
			sql.append("       d.dealer_code,\n" );
			sql.append("       a.id,\n" );
			sql.append("        d.dealer_shortname\n" );
			sql.append("  from tt_as_wr_old_returned   a,\n" );
			sql.append("       tt_as_wr_returned_order b,\n" );
			sql.append("       tr_return_logistics     c,\n" );
			sql.append("       tm_dealer               d\n" );
			sql.append(" where b.id = c.return_id\n" );
			sql.append("   and a.id = c.logictis_id\n" );
			sql.append("   and a.dealer_id = d.dealer_id\n" );
			sql.append("   and a.is_invoice = 0\n" );
			sql.append("   and a.auth_price is not null\n" );
			sql.append("   and a.status = 10811005 \n" );
			sql.append("   and (a.return_type = 10731001 or a.return_type = 10731002)");
			
			
			
			
			DaoFactory.getsql(sql, "d.dealer_code", DaoFactory.getParam(request,"dealerCode"), 2);
			DaoFactory.getsql(sql, "d.dealer_shortname", DaoFactory.getParam(request,"dealerName"), 2);
			DaoFactory.getsql(sql, "a.return_no", DaoFactory.getParam(request,"back_order_no"), 2);//回运清单号
			DaoFactory.getsql(sql, "a.return_type", DaoFactory.getParam(request,"type_name"), 2);//回运类型
			DaoFactory.getsql(sql, "a.transport_no", DaoFactory.getParam(request,"transport_no"),2);//回运单号
			DaoFactory.getsql(sql, "a.transport_type", DaoFactory.getParam(request,"freight_type"),2);//回运单号
			DaoFactory.getsql(sql, "a.return_date", DaoFactory.getParam(request,"report_start_date"),3);
			DaoFactory.getsql(sql, "a.return_date", DaoFactory.getParam(request,"report_end_date"),4);
			return this.pageQuery(sql.toString(), null, getFunName(), pageSize, currPage);
		}

		@SuppressWarnings("unchecked")
		public int returnAmountAuditNew(RequestWrapper request,AclUserBean loginUser) {
			int res=1;
			String id = DaoFactory.getParam(request, "id");
			String price = DaoFactory.getParam(request, "price");
			String signRemark = DaoFactory.getParam(request, "signRemark1");
			try {
				StringBuffer sb= new StringBuffer();
				if(!"".equals(id)&&null!=id){
					sb.append("update tt_as_wr_old_returned t set t.status=10811007,t.sign_remark='"+signRemark+"',t.auth_price='"+price+"' where t.id='"+id+"'");
					this.update(sb.toString(), null);
				}
			} catch (Exception e) {
				res=-1;
				e.printStackTrace();
			}
			return res;
		}

		@SuppressWarnings("unchecked")
		public PageResult<TtAsWrBackListQryBean> queryClaimBackList(RequestWrapper request,ActionContext act,AclUserBean loginUser, Integer currPage, Integer pageSize) {
			// 查询语句
			StringBuffer sqlStr = new StringBuffer();
			sqlStr.append("select bb.status as re_status, bb.IS_DELAY, bor.borrow_person,tawor.borrow_no as borrow_no, bor.borrow_phone,bor.require_date  , bb.price,bb.auth_price auth_price,bb.id as return_id,bb.return_no as old_no,bb.arrive_date,bb.status as old_status,(select count(*) from tt_as_wr_old_returned_detail d where d.return_id=bb.id and d.box_no is null) as box_no,tawor.id,tawor.return_no,tawor.create_date,TAWOR.WR_START_DATE||'至'||to_char(tawor.return_end_date,'yyyy-mm-dd') WR_START_DATE,bb.return_date,\n");
			sqlStr.append("tawor.wr_amount,tawor.part_item_amount,tawor.part_amount,tawor.parkage_amount,tawor.dealer_shortname,\n");
			sqlStr.append("tc.code_desc status_desc,tawor.status,tawor.top_dealer_id dealer_id,\n");
			sqlStr.append("tcc.code_desc yieldly_name, tawor.yieldly,tawor.dealer_level,"+loginUser.getDealerId()+" self_dealer_id,u1.name  auth_person_name ,tawor.return_type,u.name sign_name\n");
			sqlStr.append("from TT_AS_WR_RETURNED_ORDER tawor  left join TT_AS_PART_BORROW bor  on tawor.return_no = bor.return_no   left join tc_code tcc on tcc.code_id = tawor.yieldly,tr_return_logistics aa,tt_as_wr_old_returned bb  left join tc_user u on u.user_id = bb.sign_person  left join tc_user u1 on u1.user_id = bb.in_warhouse_by,tm_dealer d,tc_code tc\n");
			sqlStr.append("where tawor.status=tc.code_id and aa.return_id=tawor.id and bb.id=aa.logictis_id\n");
			sqlStr.append("and tawor.dealer_id = d.dealer_id");
			DaoFactory.getsql(sqlStr, "bb.return_no", DaoFactory.getParam(request,"back_order_no"), 2);//回运清单号
			DaoFactory.getsql(sqlStr, "tawor.DEALER_ID", DaoFactory.getParam(request,"dealeId"), 1);//选择经销商
			DaoFactory.getsql(sqlStr, "tawor.create_date", DaoFactory.getParam(request,"create_start_date"), 3);//建单开始时间
			DaoFactory.getsql(sqlStr, "tawor.create_date", DaoFactory.getParam(request,"create_end_date"), 4);//建单结束时间
			DaoFactory.getsql(sqlStr, "tawor.return_date", DaoFactory.getParam(request,"report_start_date"), 3);// 提报起始日期
			DaoFactory.getsql(sqlStr, "tawor.return_date", DaoFactory.getParam(request,"report_end_date"), 4);// 提报结束日期
			DaoFactory.getsql(sqlStr, "bb.status", DaoFactory.getParam(request,"ord_status"), 1);//处理状态
			DaoFactory.getsql(sqlStr, "bb.return_type", DaoFactory.getParam(request,"return_type"), 1);
			Integer dealerLevel = Constant.DEALER_LEVEL_02;
			//21、查询对应经销商信息
			if(Utility.testString(loginUser.getDealerId())){
				TmDealerPO conditionPO = new TmDealerPO();
				conditionPO.setDealerId(Long.parseLong(loginUser.getDealerId()));
				List<TmDealerPO> dealerList = this.select(conditionPO);
				if(dealerList!=null && dealerList.size()>0){
					TmDealerPO tempPO = dealerList.get(0);
					if(tempPO.getDealerLevel()!=null)
						dealerLevel = tempPO.getDealerLevel();
					    /************Iverson add By 2010-11-16 查询的当前登录经销商的code,用来判断一二级经销商**********/
						act.setOutData("dealerLevel", dealerLevel);
						/************Iverson add By 2010-11-16 查询的当前登录经销商的code,用来判断一二级经销商**********/
				}
				sqlStr.append(" and tawor.DEALER_ID in("+loginUser.getDealerId()+","+dealerList.get(0).getParentDealerD()+")\n");
			}
			    sqlStr.append(" order by bb.status asc,tawor.id desc,bb.return_no desc\n");
			     
			return this.pageQuery(TtAsWrBackListQryBean.class, sqlStr.toString(), null, pageSize, currPage);
		}

		@SuppressWarnings("unchecked")
		public TtAsWrOldPartBackListDetailBean getClaimBackInfo(RequestWrapper request, AclUserBean loginUser) {
			
			//根据回运主键查询索赔配件清单表信息
			StringBuffer sqlStr=new StringBuffer();
			sqlStr.append(" select tawor.id,tawor.return_no,to_char(tawor.return_date,'YYYY-MM-DD') return_date,tawor.wr_amount,\n" );
			sqlStr.append(" tawor.part_item_amount,tawor.part_amount,tawor.parkage_amount,tawor.arrive_date,\n" );
			sqlStr.append(" tawor.return_type,tawor.tel,tccc.code_desc return_desc,tawor.status,decode(tawor.remark,null,'',tawor.remark) remark,\n" );
			sqlStr.append(" tc.code_desc status_desc,tawor.transport_type,tcc.code_desc transport_desc,decode(tawor.price,null,0,tawor.price) price,\n" );
			sqlStr.append(" to_char(tawor.create_date,'YYYY-MM-DD') create_date,tu.name creator,tawor.tran_no,TO_CHAR(tawor.SEND_TIME,'YYYY-MM-DD') send_date\n" );
			sqlStr.append(" from tt_as_wr_old_returned tawor,tc_code tc,tc_code tcc,\n" );
			sqlStr.append(" tc_code tccc,tc_user tu\n" );
			sqlStr.append(" where tawor.status=tc.code_id(+) and tawor.transport_type=tcc.code_id(+)\n" );
			sqlStr.append(" and tawor.create_by=tu.user_id(+) and tawor.return_type=tccc.code_id(+)\n" );
			DaoFactory.getsql(sqlStr, "tawor.id", DaoFactory.getParam(request, "ORDER_ID"), 1);
			PageResult<TtAsWrOldPartBackListDetailBean> pr = null;
			TtAsWrOldPartBackListDetailBean resultPO = null;
	    	pr = this.pageQuery(TtAsWrOldPartBackListDetailBean.class,sqlStr.toString(), null, 1, 1);
	    	if(pr!=null && pr.getRecords()!=null && pr.getRecords().size()>0)
	    		resultPO = (TtAsWrOldPartBackListDetailBean)pr.getRecords().get(0);
	    	return resultPO;
		}

		@SuppressWarnings("unchecked")
		public List<TtAsWrOldPartDetailListBean> queryClaimBackDetailList(RequestWrapper request, AclUserBean loginUser,TtAsWrOldPartBackListDetailBean detailInfoBean) {
			String oper=DaoFactory.getParam(request, "oper");
			String claimId=DaoFactory.getParam(request, "ORDER_ID");
			String partCode=DaoFactory.getParam(request, "part_code");
			String partName=DaoFactory.getParam(request, "part_name");
			StringBuffer sb =new StringBuffer();
			sb.append(" select * from  (");
			//根据回运主键查询索赔配件明细表信息
			sb.append("select taword.barcode_no,(select a.claim_type from tt_as_wr_application a where a.claim_no=taword.claim_no) as claim_type,taword.id,taword.claim_no,taword.vin,taword.part_id,tawp.down_part_code as part_code,\n");
			if ("query".equals(oper) || "export".equals(oper)) {
				sb.append("tawp.down_part_name as part_name,taword.n_return_amount,taword.return_amount,\n");
			}else{
				if(Constant.BACK_TRANSPORT_TYPE_01.equals(detailInfoBean.getReturn_type())){//紧急回运
					sb.append("tawp.down_part_name as part_name,nvl(tawp.quantity,0)-nvl(tawp.return_num,0)+nvl(taword.return_amount,0) n_return_amount,taword.return_amount,\n");
				}else{//常规回运
					sb.append("tawp.down_part_name as part_name,taword.n_return_amount,taword.return_amount,\n");
				}
			}
			sb.append("taword.box_no,taword.warehouse_region,");
			sb.append("decode(taword.deduct_remark,null,'',tc.code_desc) deduct_remark,ba.area_name proc_factory\n");
			sb.append("from tt_as_wr_old_returned_detail taword,\n");
			sb.append("tt_as_wr_partsitem tawp,tc_code tc,tm_business_area ba\n");
			sb.append("where taword.part_id=tawp.part_id\n");
			sb.append("and taword.deduct_remark=tc.code_id(+)\n");
			sb.append("and taword.proc_factory=ba.area_id(+)\n");
			sb.append("and taword.return_id="+claimId+"\n");
			DaoFactory.getsql(sb, "tawp.part_code", partCode, 2);
			DaoFactory.getsql(sb, "tawp.part_name", partName, 2);
			sb.append(" ORDER BY taword.box_no,taword.claim_no,taword.part_name,taword.barcode_no ");
			//根据排序让服务活动的排在最前面
			sb.append("    )  t where 1 = 1 order by case when t.claim_type = 10661006 then 1 else 2 end asc ");
			return this.pageQuery(sb.toString(), null, getFunName());
		}

		@SuppressWarnings("unchecked")
		public List<TtAsWrOldReturnedDetailPO> getOldReturnedDetailPOByReturnId(RequestWrapper request, int i, int j) {
			StringBuffer buffer  = new StringBuffer();
			String backId=DaoFactory.getParam(request, "i_back_id");//获取回运清单的修改主键
			buffer.append("select * from tt_as_wr_old_returned_detail where 1=1   ");
            DaoFactory.getsql(buffer, "return_id", backId, 1);
			return this.pageQuery(buffer.toString(), null, getFunName());
		}
		@SuppressWarnings("unchecked")
		public List<TtAsWrOldReturnedDetailPO> getOldReturnedDetailPOByReturnId1(RequestWrapper request, int i, int j) {
			StringBuffer buffer  = new StringBuffer();
			String claimId=request.getParamValue("claimId");//物流单ID
			buffer.append("select * from tt_as_wr_old_returned_detail where 1=1   ");
            DaoFactory.getsql(buffer, "return_id", claimId, 1);
			return this.pageQuery(buffer.toString(), null, getFunName());
		}
		@SuppressWarnings("unchecked")
		public int updateClaimBackOrdMainInfo(RequestWrapper request,	AclUserBean loginUser) {
			
			String return_no=request.getParamValue("i_return_no");//获取回运清单的修改主键
			String boxTotalNum=request.getParamValue("i_boxTotalNum");//装箱总数
			String price = request.getParamValue("price");//获取申报运费
			String remark = request.getParamValue("remark");
			if(remark==null || "".equalsIgnoreCase(remark)){
				remark=" ";
			}
			//保存索赔回运清单表信息
			TtAsWrOldReturnedPO mainPo1=new TtAsWrOldReturnedPO();
			mainPo1.setReturnNo(return_no);
			TtAsWrOldReturnedPO mainPo=new TtAsWrOldReturnedPO();
			if( BaseUtils.notNull(boxTotalNum)){//装箱总数量
				mainPo.setParkageAmount(Integer.parseInt(boxTotalNum));
			}
			mainPo.setUpdateBy(loginUser.getUserId());
			mainPo.setUpdateDate(new Date());
			if (null!=price&&!"".equals(price)&&!"null".equals(price)) {
				mainPo.setPrice(Float.valueOf(price)); 
			}
			mainPo.setRemark(remark);
			int res = this.update(mainPo1, mainPo);
			
			return res;
		}

		@SuppressWarnings("unchecked")
		public TtAsWrOldPartBackListDetailBean getClaimBackInfo(RequestWrapper request, int i, int j) {
			StringBuffer sqlStr=new StringBuffer();
			sqlStr.append("select tawor.id,tawor.return_no,to_char(tawor.return_date,'YYYY-MM-DD') return_date,tawor.wr_amount,\n" );
			sqlStr.append("tawor.part_item_amount,tawor.part_amount,tawor.parkage_amount,tawor.arrive_date,\n" );
			sqlStr.append("tawor.return_type,tccc.code_desc return_desc,tawor.status,\n" );
			sqlStr.append("tc.code_desc status_desc,tawor.transport_type,tcc.code_desc freight_type,\n" );
			sqlStr.append("to_char(tawor.create_date,'YYYY-MM-DD') create_date,tu.name creator\n" );
			sqlStr.append("from tt_as_wr_old_returned tawor,tc_code tc,tc_code tcc,\n" );
			sqlStr.append("tc_code tccc,tc_user tu\n" );
			sqlStr.append("where tawor.status=tc.code_id(+) and tawor.transport_type=tcc.code_id(+)\n" );
			sqlStr.append("and tawor.create_by=tu.user_id(+) and tawor.return_type=tccc.code_id(+)\n" );
			DaoFactory.getsql(sqlStr, "tawor.id", DaoFactory.getParam(request, "ORDER_ID"), 1);//获取回运清单的修改主键
			
			TtAsWrOldPartBackListDetailBean resultPO = null;
			PageResult<TtAsWrOldPartBackListDetailBean> pr = null;
	    	pr = pageQuery(TtAsWrOldPartBackListDetailBean.class,sqlStr.toString(), null, i, j);
	    	if(pr!=null && pr.getRecords()!=null && pr.getRecords().size()>0)
	    		resultPO = (TtAsWrOldPartBackListDetailBean)pr.getRecords().get(0);
	    	return resultPO;
		}

		@SuppressWarnings("unchecked")
		public List<TtAsWrOldPartDetailListBean> queryClaimBackDetailList(RequestWrapper request) {
			StringBuffer sqlStr=new StringBuffer();
			sqlStr.append("select taword.id,taword.claim_no,taword.vin,taword.part_id,");
			sqlStr.append("tawp.part_name,taword.n_return_amount,taword.return_amount,");
			sqlStr.append("taword.box_no,taword.warehouse_region,");
			sqlStr.append("decode(taword.deduct_remark,null,'',tc.code_desc) deduct_remark ");
			sqlStr.append("from tt_as_wr_old_returned_detail taword,");
			sqlStr.append("tt_as_wr_partsitem tawp,tc_code tc ");
			sqlStr.append("where taword.part_id=tawp.part_id ");
			sqlStr.append("and taword.deduct_remark=tc.code_id(+) ");
			DaoFactory.getsql(sqlStr, "taword.return_id", DaoFactory.getParam(request, "i_back_id"), 1);
			sqlStr.append(" order by taword.create_date ");
			return this.select(TtAsWrOldPartDetailListBean.class, sqlStr.toString(), null);
		}

		@SuppressWarnings("unchecked")
		public void insertTtAsComRecord(RequestWrapper request,AclUserBean loginUser) {
			String  backId = DaoFactory.getParam(request, "i_back_id");
			TtAsComRecordPO asComRecordPO = new TtAsComRecordPO();
			asComRecordPO.setId(DaoFactory.getPkId());
			asComRecordPO.setBizId(Long.valueOf(backId));
			asComRecordPO.setCreateBy(loginUser.getUserId());
			asComRecordPO.setCreateDate(new Date());
			TcUserPO po = new TcUserPO();
			po.setUserId(loginUser.getUserId());
			List<TcUserPO> po2 =dao.select(po);
			String name = po2.get(0).getName();
			asComRecordPO.setRemark("装箱操作人:"+name);
			this.insert(asComRecordPO);
		}

		@SuppressWarnings("unchecked")
		public List<TmOldpartTransportPO> queryGetTransPList(Long currDealerId) {
			 StringBuffer sql  = new StringBuffer();
			 sql.append("select t.* from tm_oldpart_transport t\n");
			 sql.append("WHERE t.check_date IS NOT NULL\n");
			 sql.append("AND t.transport_status="+Constant.SP_JJ_TRANSPORT_STATUS_03+"\n");
			 DaoFactory.getsql(sql, "t.dealer_id", currDealerId+"", 1);
			 sql.append("order by t.check_date DESC \n");
			return this.select(TmOldpartTransportPO.class, sql.toString(), null);
		}

		@SuppressWarnings("unchecked")
		public List<Map<String, Object>> getStr(Long id) {
			  List<Map<String, Object>> list = null;
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT d.detail_id,d.transport_name\n");
				sql.append(" FROM tm_oldpart_transport_detail d\n");
				sql.append("WHERE   d.status=1 "); 
				DaoFactory.getsql(sql, "d.transport_id", id.toString(), 1);
				list=this.pageQuery(sql.toString(), null, getFunName());
			return list;
		}

		@SuppressWarnings("unchecked")
		public List<Map<String, Object>> getStr(List<TmOldpartTransportPO> sList, String returnType) {
			List<Map<String, Object>> list = null;
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT d.detail_id,d.transport_name\n");
			sql.append(" FROM tm_oldpart_transport_detail d\n");
			sql.append("WHERE   d.status=1 "); 
			DaoFactory.getsql(sql, "d.return_Type", returnType, 1);
			if(sList.size()>0){
				sql.append(" and d.transport_id in (");
			}
			int size = sList.size();
			for (int i = 0; i < size; i++) {
				if(size-1==i){
					sql.append(sList.get(i).getTransportId()+"");
				}else{
					sql.append(sList.get(i).getTransportId()+",");
				}
			}
			sql.append(")");
			list=super.pageQuery(sql.toString(), null, getFunName());
			return list;
		}

		@SuppressWarnings("unchecked")
		public int updateTtAsWrOldReturned(RequestWrapper request,AclUserBean loginUser,List<TtAsWrOldReturnedDetailPO> returnDetaiList) {
			String claimId=request.getParamValue("claimId");//物流单ID
			String transNo=request.getParamValue("transNo");//货运单号
			String sendDate = request.getParamValue("sendDate");//发运日期
			String arriveDate = request.getParamValue("arriveDate");//预计到货时间
			String freight_type=request.getParamValue("freight_type");//货运方式
			String price=request.getParamValue("price");//运费
			String return_type=request.getParamValue("backType");//回运类型
			String tel = request.getParamValue("tel");//三包员电话
			String transportNo = request.getParamValue("TRANSPORT_NO");//物流发运单号
			String info = request.getParamValue("info");//备注
			String tranPerson = request.getParamValue("tranPerson");//备注
			int updateReturnNum=0;//记录修改的回运配件数
			for(int count=0;count<returnDetaiList.size();count++){
				if(Constant.BACK_TRANSPORT_TYPE_01.equals(Integer.valueOf(return_type))){//保存紧急回运类型修改回运数
					updateReturnNum+=returnDetaiList.get(count).getReturnAmount();
				}
			}
			//保存物流单表信息


		TtAsWrOldReturnedPO mainPo = new TtAsWrOldReturnedPO();
		mainPo
				.setTranNo(transNo == null || "".equals(transNo) ? "''"
						: transNo);// 货运单号
		if (Utility.testString(sendDate)) {
			Date sendTime;
			try {
				sendTime = Utility.parseString2Date(sendDate, "yyyy-MM-dd");
				mainPo.setSendTime(sendTime);// 发运日期
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!XHBUtil.IsNull(arriveDate)) {
			Date arriveTime;
			try {
				arriveTime = Utility.parseString2Date(arriveDate, "yyyy-MM-dd");
				mainPo.setArriveDate(arriveTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mainPo.setTransportType(Integer.parseInt(freight_type));// 货运方式
		mainPo.setTransportNo(transportNo);
		if (Utility.testString(tranPerson)) {
			mainPo.setTranPerson(Long.valueOf(tranPerson));
		} else {
			mainPo.setTranPerson(0L);
		}

		// 运费
		if (Utility.testString(price)) {
			mainPo.setPrice(Float.parseFloat(price));
			mainPo.setAuthPrice(0d);
		} else {
			mainPo.setPrice(0f);
			mainPo.setAuthPrice(0d);
		}
		if (Constant.BACK_TRANSPORT_TYPE_01
				.equals(Integer.valueOf(return_type))) {// 保存紧急回运类型修改主表配件数
			mainPo.setPartAmount(updateReturnNum);
		}
		mainPo.setTel(tel);
		mainPo.setRemark(info);
		mainPo.setUpdateBy(loginUser.getUserId());
		mainPo.setUpdateDate(new Date());
		TtAsWrOldReturnedPO po1 = new TtAsWrOldReturnedPO();
		po1.setId(Long.parseLong(claimId));
		int res = dao.update(po1, mainPo);
			return res;
		}

		@SuppressWarnings("unchecked")
		public void insertTtAsComRecord1(RequestWrapper request,AclUserBean loginUser) {
			String claimId=request.getParamValue("claimId");//物流单ID
			TtAsComRecordPO asComRecordPO = new TtAsComRecordPO();
			asComRecordPO.setId(DaoFactory.getPkId());
			asComRecordPO.setBizId(Long.valueOf(claimId));
			asComRecordPO.setCreateBy(loginUser.getUserId());
			asComRecordPO.setCreateDate(new Date());
			TcUserPO tcpo = new TcUserPO();
			tcpo.setUserId(loginUser.getUserId());
			List<TcUserPO> po2 =this.select(tcpo);
			String name = po2.get(0).getName();
			asComRecordPO.setRemark("补录操作人:"+name);
			dao.insert(asComRecordPO);
		}

		@SuppressWarnings("unchecked")
		public TtAsWrOldPartBackListDetailBean getClaimBackInfo1(RequestWrapper request, int i, int j) {
			//根据回运主键查询索赔配件清单表信息
			StringBuffer sqlStr=new StringBuffer();
			sqlStr.append(" select tawor.id,tawor.return_no,to_char(tawor.return_date,'YYYY-MM-DD') return_date,tawor.wr_amount,\n" );
			sqlStr.append(" tawor.part_item_amount,tawor.part_amount,tawor.parkage_amount,tawor.arrive_date,\n" );
			sqlStr.append(" tawor.return_type,tawor.tel,tccc.code_desc return_desc,tawor.status,decode(tawor.remark,null,'',tawor.remark) remark,\n" );
			sqlStr.append(" tc.code_desc status_desc,tawor.transport_type,tcc.code_desc transport_desc,decode(tawor.price,null,0,tawor.price) price,\n" );
			sqlStr.append(" to_char(tawor.create_date,'YYYY-MM-DD') create_date,tu.name creator,tawor.tran_no,TO_CHAR(tawor.SEND_TIME,'YYYY-MM-DD') send_date\n" );
			sqlStr.append(" from tt_as_wr_old_returned tawor,tc_code tc,tc_code tcc,\n" );
			sqlStr.append(" tc_code tccc,tc_user tu\n" );
			sqlStr.append(" where tawor.status=tc.code_id(+) and tawor.transport_type=tcc.code_id(+)\n" );
			sqlStr.append(" and tawor.create_by=tu.user_id(+) and tawor.return_type=tccc.code_id(+)\n" );
			DaoFactory.getsql(sqlStr, "tawor.id", DaoFactory.getParam(request, "ORDER_ID"), 1);//获取物流单ID
			TtAsWrOldPartBackListDetailBean resultPO = null;
			PageResult<TtAsWrOldPartBackListDetailBean> pr = null;
	    	pr = this.pageQuery(TtAsWrOldPartBackListDetailBean.class,sqlStr.toString(), null, i, j);
	    	if(pr!=null && pr.getRecords()!=null && pr.getRecords().size()>0)
	    		resultPO = (TtAsWrOldPartBackListDetailBean)pr.getRecords().get(0);
	    	return resultPO;
		}

		@SuppressWarnings("unchecked")
		public List<TtAsWrOldPartDetailListBean> queryClaimBackDetailList1(RequestWrapper request, AclUserBean loginUser,TtAsWrOldPartBackListDetailBean detailInfoBean) {
			String claimId=DaoFactory.getParam(request,"ORDER_ID");//获取物流单ID
			String borrow_no=DaoFactory.getParam(request,"borrow_no");//borrow_no
			String partCode = DaoFactory.getParam(request,"part_code");
			String partName = DaoFactory.getParam(request,"part_name");
			String oper=DaoFactory.getParam(request,"oper");//获取操作动作
			StringBuffer sb =new StringBuffer();
			sb.append(" select * from  (");
			//根据回运主键查询索赔配件明细表信息
			sb.append("select taword.barcode_no,(select a.claim_type from tt_as_wr_application a where a.claim_no=taword.claim_no) as claim_type,taword.id,taword.claim_no,taword.vin,taword.part_id,tawp.down_part_code as part_code,\n");
			if ("query".equals(oper) || "export".equals(oper)) {
				sb.append("tawp.down_part_name as part_name,taword.n_return_amount,taword.return_amount,\n");
			}else{
				if(Constant.BACK_TRANSPORT_TYPE_01.equals(detailInfoBean.getReturn_type())){//紧急回运
					sb.append("tawp.down_part_name as part_name,nvl(tawp.quantity,0)-nvl(tawp.return_num,0)+nvl(taword.return_amount,0) n_return_amount,taword.return_amount,\n");
				}else{//常规回运
					sb.append("tawp.down_part_name as part_name,taword.n_return_amount,taword.return_amount,\n");
				}
			}
			sb.append("taword.box_no,taword.warehouse_region,");
			sb.append("decode(taword.deduct_remark,null,'',tc.code_desc) deduct_remark,ba.area_name proc_factory\n");
			sb.append("from tt_as_wr_old_returned_detail taword,\n");
			sb.append("tt_as_wr_partsitem tawp,tc_code tc,tm_business_area ba\n");
			sb.append("where taword.part_id=tawp.part_id\n");
			sb.append("and taword.deduct_remark=tc.code_id(+)\n");
			sb.append("and taword.proc_factory=ba.area_id(+)\n");
			DaoFactory.getsql(sb, "taword.return_id", claimId, 2);
			DaoFactory.getsql(sb, "tawp.part_code", partCode, 2);
			DaoFactory.getsql(sb, "tawp.part_name", partName, 2);
			sb.append(" ORDER BY taword.box_no,taword.claim_no,taword.part_name,taword.barcode_no ");
			//根据排序让服务活动的排在最前面
			sb.append("    )  t where 1 = 1 order by case when t.claim_type = 10661006 then 1 else 2 end asc ");
			return this.select(TtAsWrOldPartDetailListBean.class, sb.toString(), null);
		}

		public void authPriceAudit(Map<String, Object> params) throws Exception {
			String id = CommonUtils.checkNull(params.get("id"));
			String authPrice = CommonUtils.checkNull(params.get("authPrice"));
			String priceRemark = CommonUtils.checkNull(params.get("priceRemark"));
			
			String updateBy = CommonUtils.checkNull(params.get("updateBy"));
			List<Object> parList = new ArrayList<Object>();
			
			StringBuffer sql = new StringBuffer();
			//更新售后服务工单表状态
			sql = new StringBuffer();
			sql.append( "UPDATE TT_AS_WR_RETURNED_ORDER A\n" +
						"   SET A.AUTH_PRICE = ?,\n" + 
						"       A.PRICE_REMARK = ?,\n" + 
						"       A.STATUS = ?,\n" + 
						"       A.UPDATE_BY = ?,\n" + 
						"       A.UPDATE_DATE = SYSDATE\n" + 
						" WHERE A.ID = ?\n");
			parList.clear();
			parList.add(authPrice);
			parList.add(priceRemark);
			parList.add(Constant.BACK_LIST_STATUS_07);
			parList.add(updateBy);
			parList.add(id);
			dao.update(sql.toString(),parList);
		}

}
