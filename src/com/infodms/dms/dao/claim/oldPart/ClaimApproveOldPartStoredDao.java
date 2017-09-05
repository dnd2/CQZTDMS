package com.infodms.dms.dao.claim.oldPart;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ClaimApproveAndStoredReturnInfoBean;
import com.infodms.dms.bean.ClaimOldPartApproveStoreListBean;
import com.infodms.dms.bean.TtAsWrOldPartSignDetailListBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.claim.application.DealerBalanceDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TmPtSupplierPO;
import com.infodms.dms.po.TtAsWrBarcodePartStockPO;
import com.infodms.dms.po.TtAsWrOldReturnedDetailPO;
import com.infodms.dms.po.TtAsWrOldReturnedPO;
import com.infodms.dms.po.TtAsWrPartStockPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;
/**
 * 类说明：索赔旧件管理--索赔旧件入库Dao层
 * 作者：  赵伦达
 */
@SuppressWarnings("unchecked")
public class ClaimApproveOldPartStoredDao extends BaseDao{
	
	public static Logger logger = Logger.getLogger(ClaimApproveOldPartStoredDao.class);
	protected POFactory factory = POFactoryBuilder.getInstance();
	private static final ClaimApproveOldPartStoredDao dao = ClaimApproveOldPartStoredDao.getInstance();
	private ActionContext act = null;
	private AclUserBean logonUserBean = null;
	public static final ClaimApproveOldPartStoredDao getInstance() {
	   if(dao==null) return new ClaimApproveOldPartStoredDao();
	   return dao;
	}
	/**
	 * Function：索赔旧件审批入库--按页面条件查询回运清单信息
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@param curPage
	 * @return:		@param pageSize
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-13
	 */
	public PageResult<ClaimOldPartApproveStoreListBean> queryClaimBackList(Map params,int curPage, int pageSize){
		//获取前台条件
		String company_id=ClaimTools.dealParamStr(params.get("company_id"));//登陆人所在companyId
		String dealer_code=ClaimTools.dealParamStr(params.get("i_dealer_code"));//经销商代码
		String dealer_name=ClaimTools.dealParamStr(params.get("i_dealer_name"));//经销商简称
		String return_no=ClaimTools.dealParamStr(params.get("i_return_no"));//回运清单号
		String in_start_date=ClaimTools.dealParamStr(params.get("i_create_start_date"));//入库开始日期
		String in_end_date=ClaimTools.dealParamStr(params.get("i_create_end_date"));//入库结束日期
		String return_start_date=ClaimTools.dealParamStr(params.get("i_submit_start_date"));//提报开始日期
		String return_end_date=ClaimTools.dealParamStr(params.get("i_submit_end_date"));//提报结束日期
		String transport_type=ClaimTools.dealParamStr(params.get("i_transport_type"));//货运方式
		String back_type=ClaimTools.dealParamStr(params.get("i_back_type"));//回运单状态
		String trans_no=ClaimTools.dealParamStr(params.get("i_trans_no"));//货运单号
		String yieldlys=ClaimTools.dealParamStr(params.get("yieldlys"));//用户拥有的产地权限
		String yieldly=ClaimTools.dealParamStr(params.get("yieldly"));//查询条件--产地
		
		StringBuffer sqlStr=new StringBuffer();
		StringBuffer whereStr=new StringBuffer();
		StringBuffer groupStr=new StringBuffer();
		StringBuffer orderStr=new StringBuffer();
		
		sqlStr.append("select tawor.is_Overduecheck,tawor.id,td.dealer_code dealer_id,td.dealer_shortname dealer_name,tawor.tel,\n" );
		sqlStr.append("tawor.return_no claim_no,to_char(tawor.in_warhouse_date,'YYYY-MM-DD') create_date,\n" );
		//sqlStr.append("to_char(tawor.in_warhouse_date,'YYYY-MM-DD') in_warhouse_date,");
		sqlStr.append("to_char(tawor.return_date,'YYYY-MM-DD') return_date,\n" );
		sqlStr.append("tawor.return_type,tc.code_desc return_desc,\n" );
		sqlStr.append("nvl(sum(tawor.wr_amount),0) wr_amount,\n" );
		sqlStr.append("nvl(sum(tawor.parkage_amount),0) parkage_amount,\n" );
		sqlStr.append("nvl(sum(tawor.part_amount),0) part_amount,tawor.status back_type,tcc.code_desc back_desc,tawor.tran_no trans_no\n" );
		sqlStr.append("from tt_as_wr_old_returned tawor,tm_dealer td,tc_code tc,tc_code tcc\n" );
		sqlStr.append("where tawor.dealer_id=td.dealer_id and tawor.transport_type=tc.code_id and tawor.is_status=0\n" );
		/**********2010-12-14**********/
		//sqlStr.append("and tawor.status not in('"+Constant.BACK_LIST_STATUS_04+"')\n" );
		/********************/
		sqlStr.append("and tawor.status=tcc.code_id\n" );
		sqlStr.append("and tawor.yieldly in ("+yieldlys+")\n" );
		//条件
		if(dealer_code!=null&&!"".equals(dealer_code)){
			String[] temp=dealer_code.split(",");
			String str="";
			if(temp.length>0){
				sqlStr.append(" and (");
				for(int count=0;count<temp.length;count++){
					str+=" td.dealer_code='"+temp[count].replaceAll("'", "\''")+"' or\n";
				}
				sqlStr.append(str.substring(0, str.length()-3));
				sqlStr.append(")\n");
			}
		}
		if(yieldly!=null&&!"".equals(yieldly))
			whereStr.append(" and tawor.yieldly = '"+yieldly+"'\n");
		if(dealer_name!=null&&!"".equals(dealer_name))
			whereStr.append(" and td.dealer_name like '%"+dealer_name.replaceAll("'", "\''")+"%'\n");
		if(return_no!=null&&!"".equals(return_no))
			whereStr.append(" and tawor.return_no like '%"+return_no.toUpperCase().replaceAll("'", "\''")+"%'\n");
		if(in_start_date!=null&&!"".equals(in_start_date))
			whereStr.append(" and tawor.in_warhouse_date>=to_date('" + in_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		if(in_end_date!=null&&!"".equals(in_end_date))
			whereStr.append(" and tawor.in_warhouse_date<=to_date('" + in_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		if(return_start_date!=null&&!"".equals(return_start_date))
			whereStr.append(" and tawor.return_date>=to_date('" + return_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		if(return_end_date!=null&&!"".equals(return_end_date))
			whereStr.append(" and tawor.return_date<=to_date('" + return_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		if(transport_type!=null&&!"".equals(transport_type))
			whereStr.append(" and tawor.transport_type="+transport_type+"\n");
		if(back_type!=null&&!"".equals(back_type)){
			whereStr.append(" and tawor.status="+back_type+"\n");
		}else{
			whereStr.append(" and (tawor.status="+Constant.BACK_LIST_STATUS_02+" or tawor.status="+Constant.BACK_LIST_STATUS_03+" or tawor.status="+Constant.BACK_LIST_STATUS_04+")\n");
		}
		if(trans_no!=null&&!"".equals(trans_no))
			whereStr.append(" and tawor.tran_no like '%"+trans_no.toUpperCase().replaceAll("'", "\''")+"%'\n");
		
		groupStr.append("group by tawor.id,td.dealer_code,td.dealer_shortname,tawor.return_no,tawor.in_warhouse_date,tawor.tel,\n" );
		groupStr.append("tawor.create_date,tawor.return_date,tawor.return_type,tc.code_desc,tawor.status,tcc.code_desc,tawor.tran_no\n");
		
		orderStr.append(" order by tawor.id desc\n");
		
		System.out.println(sqlStr.toString()+whereStr.toString()+groupStr.toString()+orderStr.toString());
		
		PageResult<ClaimOldPartApproveStoreListBean> pr = pageQuery(ClaimOldPartApproveStoreListBean.class,
				sqlStr.toString()+whereStr.toString()+groupStr.toString()+orderStr.toString(),null, pageSize, curPage);
		return pr;
	}
	
	//zhumingwei 2011-04-21
	public PageResult<ClaimOldPartApproveStoreListBean> queryClaimBackList11(Map params,int curPage, int pageSize){
		//获取前台条件
		String company_id=ClaimTools.dealParamStr(params.get("company_id"));//登陆人所在companyId
		String dealer_code=ClaimTools.dealParamStr(params.get("i_dealer_code"));//经销商代码
		String dealer_name=ClaimTools.dealParamStr(params.get("i_dealer_name"));//经销商简称
		String return_no=ClaimTools.dealParamStr(params.get("i_return_no"));//回运清单号
		String in_start_date=ClaimTools.dealParamStr(params.get("i_create_start_date"));//入库开始日期
		String in_end_date=ClaimTools.dealParamStr(params.get("i_create_end_date"));//入库结束日期
		String return_start_date=ClaimTools.dealParamStr(params.get("i_submit_start_date"));//提报开始日期
		String return_end_date=ClaimTools.dealParamStr(params.get("i_submit_end_date"));//提报结束日期
		String transport_type=ClaimTools.dealParamStr(params.get("i_transport_type"));//货运方式
		String back_type=ClaimTools.dealParamStr(params.get("i_back_type"));//回运单状态
		String transport_no=ClaimTools.dealParamStr(params.get("i_transport_no"));//货运单号
		String yieldly=ClaimTools.dealParamStr(params.get("yieldly"));//查询条件--产地
		
		String type_name=ClaimTools.dealParamStr(params.get("i_type_name"));//回运类型
		String start_date=ClaimTools.dealParamStr(params.get("return_start_date"));//回运开始时间
		String end_date=ClaimTools.dealParamStr(params.get("return_end_date"));//回运结束时间
		StringBuffer sqlStr=new StringBuffer();
		StringBuffer whereStr=new StringBuffer();
		StringBuffer groupStr=new StringBuffer();
		StringBuffer orderStr=new StringBuffer();
//		sqlStr.append("select * from (");
		
		sqlStr.append("select  /*+RULE*/tawor.id tawor_id,tawor.create_date as CREATE_DATES,tawor.yieldly,tawor.sign_no,tawor.transport_no as transport_no, tawor.id,td.dealer_code dealer_id,td.dealer_shortname dealer_name, tawor.tel,\n" );
		sqlStr.append("tawor.return_no claim_no,to_char(tawor.sign_date,'YYYY-MM-DD hh24:mi') create_date,\n" );
		sqlStr.append("(select count(*) from tt_as_wr_returned_order_detail d where d.return_id=tawor.id and d.box_no is null) as box_no,");
		sqlStr.append("TO_CHAR(tawor.WR_START_DATE, 'yyyy-mm-dd') || '至' || TO_CHAR(tawor.RETURN_END_DATE, 'yyyy-mm-dd') as wr_start_date,");
		sqlStr.append("to_char(tawor.return_date,'YYYY-MM-DD hh24:mi') return_date,\n" );
		sqlStr.append("tawor.return_type,tc.code_desc return_desc,\n" );
		sqlStr.append("nvl(tawor.wr_amount,0) wr_amount,\n" );
		sqlStr.append("nvl(tawor.parkage_amount,0) parkage_amount,\n" );
		sqlStr.append("nvl(tawor.part_amount,0) part_amount,tawor.status back_type,tcc.code_desc back_desc,tawor.tran_no trans_no,u.name in_warhouse_name\n" );
		sqlStr.append("from tt_as_wr_returned_order tawor, tc_user u  ,tm_dealer td,tc_code tc,tc_code tcc \n" );
		sqlStr.append("where tawor.dealer_id=td.dealer_id(+) and tawor.transport_type=tc.code_id(+) \n" );//and tawor.is_status=1
		sqlStr.append("and tawor.status=tcc.code_id   and tawor.sign_person=u.user_id(+) \n" );
		sqlStr.append("and tawor.status<>"+Constant.BACK_LIST_STATUS_01+" \n" );
//		whereStr.append(" and ( tawor.status="+Constant.BACK_LIST_STATUS_02+" or tawor.status="+Constant.BACK_LIST_STATUS_03+" or tawor.status="+Constant.BACK_LIST_STATUS_04+" or tawor.status="+Constant.BACK_LIST_STATUS_05+"or tawor.status="+Constant.BACK_LIST_STATUS_07+")\n");
//条件
		if(dealer_code!=null&&!"".equals(dealer_code)){
			sqlStr.append(" and td.dealer_code  like '%"+dealer_code.toUpperCase()+"%'");
		}
		if(yieldly!=null&&!"".equals(yieldly))
			whereStr.append(" and tawor.yieldly = '"+yieldly+"'\n");
		if(dealer_name!=null&&!"".equals(dealer_name))
			whereStr.append(" and td.dealer_name like '%"+dealer_name.replaceAll("'", "\''")+"%'\n");
		if(return_no!=null&&!"".equals(return_no))
			whereStr.append(" and tawor.return_no like '%"+return_no.toUpperCase().replaceAll("'", "\''")+"%'\n");
		if(in_start_date!=null&&!"".equals(in_start_date))
			whereStr.append(" and tawor.in_warhouse_date>=to_date('" + in_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		if(in_end_date!=null&&!"".equals(in_end_date))
			whereStr.append(" and tawor.in_warhouse_date<=to_date('" + in_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		if(return_start_date!=null&&!"".equals(return_start_date))
			whereStr.append(" and tawor.return_date>=to_date('" + return_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		if(return_end_date!=null&&!"".equals(return_end_date))
			whereStr.append(" and tawor.return_date<=to_date('" + return_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		if(transport_type!=null&&!"".equals(transport_type))
			whereStr.append(" and tawor.transport_type="+transport_type+"\n");
		if(back_type!=null&&!"".equals(back_type)){
			whereStr.append(" and tawor.status="+back_type+"\n");
		}
		if(type_name!=null&&!"".equals(type_name)){
			whereStr.append(" and tawor.return_type='"+type_name+"' \n");
		} 
		if(transport_no!=null&&!"".equals(transport_no))
			whereStr.append(" and tawor.transport_no like '%"+transport_no.toUpperCase().replaceAll("'", "\''")+"%'\n");
		
//		groupStr.append("group by tawor.id,tawor.transport_no,td.dealer_code,td.dealer_shortname,tawor.return_no,tawor.sign_date,tawor.tel,\n" );
//		groupStr.append("tawor.create_date,tawor.return_date,tawor.return_type,tawor.yieldly,tawor.sign_no,tc.code_desc,tawor.status,tcc.code_desc,tawor.tran_no,u.name\n");
		
		orderStr.append(" order by tawor.return_date desc\n");
		
//		orderStr.append(")  where wr_start_date is not null");
		
		System.out.println(sqlStr.toString()+whereStr.toString()+groupStr.toString()+orderStr.toString());
		
		PageResult<ClaimOldPartApproveStoreListBean> pr = pageQuery(ClaimOldPartApproveStoreListBean.class,
				sqlStr.toString()+whereStr.toString()+groupStr.toString()+orderStr.toString(),null, pageSize, curPage);
		return pr;
	}
	//旧件审核查询
	public PageResult<ClaimOldPartApproveStoreListBean> oldPartSignAuditQuery(Map params,int curPage, int pageSize){
		//获取前台条件
		String company_id=ClaimTools.dealParamStr(params.get("company_id"));//登陆人所在companyId
		String dealer_code=ClaimTools.dealParamStr(params.get("i_dealer_code"));//经销商代码
		String dealer_name=ClaimTools.dealParamStr(params.get("i_dealer_name"));//经销商简称
		String return_no=ClaimTools.dealParamStr(params.get("i_return_no"));//回运清单号
		String in_start_date=ClaimTools.dealParamStr(params.get("i_create_start_date"));//审核开始日期
		String in_end_date=ClaimTools.dealParamStr(params.get("i_create_end_date"));//审核结束日期
		String return_start_date=ClaimTools.dealParamStr(params.get("i_submit_start_date"));//提报开始日期
		String return_end_date=ClaimTools.dealParamStr(params.get("i_submit_end_date"));//提报结束日期
		String transport_type=ClaimTools.dealParamStr(params.get("i_transport_type"));//货运方式
		String back_type=ClaimTools.dealParamStr(params.get("i_back_type"));//回运单状态
		String trans_no=ClaimTools.dealParamStr(params.get("i_trans_no"));//货运单号
		String in_warhouse_name=ClaimTools.dealParamStr(params.get("in_warhouse_name"));//审核人
		String yieldly=ClaimTools.dealParamStr(params.get("yieldly"));//查询条件--产地
		
		StringBuffer sqlStr=new StringBuffer();
		StringBuffer whereStr=new StringBuffer();
		StringBuffer groupStr=new StringBuffer();
		StringBuffer orderStr=new StringBuffer();
		sqlStr.append("select tawor.sign_date as sign_date,");//2014-2-2 zyw 加了一个签收日期
		sqlStr.append(" tawor.create_date as CREATE_DATES,tawor.yieldly,tawor.audit_no,tawor.id,td.dealer_code dealer_id,td.dealer_shortname dealer_name,tawor.tel,\n" );
		sqlStr.append("tawor.return_no claim_no,to_char(tawor.in_warhouse_date,'YYYY-MM-DD hh24:mi') create_date,\n" );
		sqlStr.append("(select count(*) from tt_as_wr_returned_order_detail d where d.return_id=tawor.id and d.box_no is null) as box_no,");
		sqlStr.append("TO_CHAR(tawor.WR_START_DATE, 'yyyy-mm-dd') || '至' || TO_CHAR(tawor.RETURN_END_DATE, 'yyyy-mm-dd') as wr_start_date,");
		sqlStr.append("to_char(tawor.return_date,'YYYY-MM-DD hh24:mi') return_date,\n" );
		sqlStr.append("tawor.return_type,tc.code_desc return_desc,\n" );
		sqlStr.append("nvl(tawor.wr_amount,0) wr_amount,\n" ); 
		sqlStr.append("nvl(tawor.parkage_amount,0) parkage_amount,\n" );
		sqlStr.append("nvl(tawor.part_amount,0) part_amount,tawor.status back_type,tcc.code_desc back_desc,tawor.transport_no trans_no,u.name in_warhouse_name \n" );
		sqlStr.append("from tt_as_wr_returned_order tawor, tc_user u  ,tm_dealer td,tc_code tc,tc_code tcc \n" );
		sqlStr.append("where tawor.dealer_id=td.dealer_id(+)  and tawor.transport_type=tc.code_id(+) \n" );//and tawor.is_status=1
		sqlStr.append("and tawor.status=tcc.code_id(+)   and tawor.in_warhouse_by=u.user_id(+) \n" );

		if(dealer_code!=null&&!"".equals(dealer_code)){
			whereStr.append(" and td.dealer_code like '%"+dealer_code.toUpperCase()+"%'\n");
		}
		if(yieldly!=null&&!"".equals(yieldly))
			whereStr.append(" and tawor.yieldly = '"+yieldly+"'\n");
		if(in_warhouse_name!=null&&!"".equals(in_warhouse_name))
			whereStr.append(" and u.name like '%"+in_warhouse_name.replaceAll("'", "\''")+"%'\n");
		if(dealer_name!=null&&!"".equals(dealer_name))
			whereStr.append(" and td.dealer_name like '%"+dealer_name.replaceAll("'", "\''")+"%'\n");
		if(return_no!=null&&!"".equals(return_no))
			whereStr.append(" and tawor.return_no like '%"+return_no.toUpperCase().replaceAll("'", "\''")+"%'\n");
		if(in_start_date!=null&&!"".equals(in_start_date))
			whereStr.append(" and tawor.in_warhouse_date>=to_date('" + in_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		if(in_end_date!=null&&!"".equals(in_end_date))
			whereStr.append(" and tawor.in_warhouse_date<=to_date('" + in_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		if(return_start_date!=null&&!"".equals(return_start_date))
			whereStr.append(" and tawor.return_date>=to_date('" + return_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		if(return_end_date!=null&&!"".equals(return_end_date))
			whereStr.append(" and tawor.return_date<=to_date('" + return_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		if(transport_type!=null&&!"".equals(transport_type))
			whereStr.append(" and tawor.transport_type="+transport_type+"\n");
		if(back_type!=null&&!"".equals(back_type)){
			whereStr.append(" and tawor.status="+back_type+"\n");
		}else{
			whereStr.append(" and tawor.status in("+Constant.BACK_LIST_STATUS_03+","+Constant.BACK_LIST_STATUS_04+","+Constant.BACK_LIST_STATUS_05+")\n");
		}
		if(trans_no!=null&&!"".equals(trans_no))
			whereStr.append(" and tawor.transport_no like '%"+trans_no.toUpperCase().replaceAll("'", "\''")+"%'\n");
		
//		groupStr.append("group by tawor.id,tawor.yieldly,tawor.audit_no,td.dealer_code,td.dealer_shortname,tawor.return_no,tawor.in_warhouse_date,tawor.tel,\n" );
//		groupStr.append("tawor.create_date,tawor.return_date,tawor.return_type,tc.code_desc,tawor.status,tcc.code_desc,tawor.tran_no,u.name,tawor.parkage_amount,\n");
//		groupStr.append(" r.dealer_id,r.wr_amount,r.parkage_amount,r.part_amount,r.tran_no");
		orderStr.append(" order by tawor.create_date desc,tawor.sign_date \n");
		
		System.out.println(sqlStr.toString()+whereStr.toString()+groupStr.toString()+orderStr.toString());
		
		PageResult<ClaimOldPartApproveStoreListBean> pr = pageQuery(ClaimOldPartApproveStoreListBean.class,
				sqlStr.toString()+whereStr.toString()+groupStr.toString()+orderStr.toString(),null, pageSize, curPage);
		return pr;
	}
	
	
	
	//旧件审核查询
	public PageResult<ClaimOldPartApproveStoreListBean> oldPartSignAuditQueryZg(Map params,int curPage, int pageSize){
		//获取前台条件
		String company_id=ClaimTools.dealParamStr(params.get("company_id"));//登陆人所在companyId
		String dealer_code=ClaimTools.dealParamStr(params.get("i_dealer_code"));//经销商代码
		String dealer_name=ClaimTools.dealParamStr(params.get("i_dealer_name"));//经销商简称
		String return_no=ClaimTools.dealParamStr(params.get("i_return_no"));//回运清单号
		String in_start_date=ClaimTools.dealParamStr(params.get("i_create_start_date"));//审核开始日期
		String in_end_date=ClaimTools.dealParamStr(params.get("i_create_end_date"));//审核结束日期
		String return_start_date=ClaimTools.dealParamStr(params.get("i_submit_start_date"));//提报开始日期
		String return_end_date=ClaimTools.dealParamStr(params.get("i_submit_end_date"));//提报结束日期
		String transport_type=ClaimTools.dealParamStr(params.get("i_transport_type"));//货运方式
		String back_type=ClaimTools.dealParamStr(params.get("i_back_type"));//回运单状态
		String trans_no=ClaimTools.dealParamStr(params.get("i_trans_no"));//货运单号
		String in_warhouse_name=ClaimTools.dealParamStr(params.get("in_warhouse_name"));//审核人
		String yieldly=ClaimTools.dealParamStr(params.get("yieldly"));//查询条件--产地
		
		StringBuffer sqlStr=new StringBuffer();
		StringBuffer whereStr=new StringBuffer();
		StringBuffer groupStr=new StringBuffer();
		StringBuffer orderStr=new StringBuffer();
		
		sqlStr.append("select  /*+RULE*/tawor.create_date as CREATE_DATES,tawor.yieldly,tawor.audit_no,tawor.id,td.dealer_code dealer_id,td.dealer_shortname dealer_name,tawor.tel,\n" );
		sqlStr.append("tawor.return_no claim_no,to_char(tawor.in_warhouse_date,'YYYY-MM-DD hh24:mi') create_date,\n" );
		sqlStr.append("(select count(*) from tt_as_wr_old_returned_detail d where d.return_id=tawor.id and d.box_no is null) as box_no,");
		sqlStr.append("(select oo.WR_START_DATE || '至' || TO_CHAR(oo.RETURN_END_DATE, 'yyyy-mm-dd')  from tt_as_wr_returned_order oo,tr_return_logistics ll where oo.id=ll.return_id and ll.logictis_id=tawor.id) as wr_start_date,");
		sqlStr.append("to_char(tawor.return_date,'YYYY-MM-DD hh24:mi') return_date,\n" );
		sqlStr.append("tawor.return_type,tc.code_desc return_desc,\n" );
		sqlStr.append("nvl(sum(td.RETURN_AMOUNT),0) wr_amount,\n" );
		sqlStr.append("(nvl(sum(td.RETURN_AMOUNT),0)-nvl(sum(td.SIGN_AMOUNT),0)) part_amount,tawor.status back_type,tcc.code_desc back_desc,tawor.tran_no trans_no,u.name in_warhouse_name\n" );
		sqlStr.append("from tt_as_wr_old_returned tawor, tc_user u  ,tm_dealer td,tc_code tc,tc_code tcc \n" );
		sqlStr.append(",(select sum(t.RETURN_AMOUNT) RETURN_AMOUNT,\n" );
		sqlStr.append("       sum(t.SIGN_AMOUNT) SIGN_AMOUNT,\n" );
		sqlStr.append("       RETURN_ID\n" );
		sqlStr.append("  from tt_as_wr_old_returned_detail t\n" );
		sqlStr.append(" where t.RETURN_AMOUNT != t.SIGN_AMOUNT and t.Executive_director_sta = 0 \n" );
		sqlStr.append(" group by t.RETURN_ID ) td  ");

		
		sqlStr.append("where tawor.dealer_id=td.dealer_id and td.RETURN_ID= tawor.id and td.RETURN_ID is not null and tawor.transport_type=tc.code_id(+) and tawor.is_status=1\n" );
		sqlStr.append("and tawor.status=tcc.code_id   and tawor.in_warhouse_by=u.user_id(+) \n" );

		if(dealer_code!=null&&!"".equals(dealer_code)){
			whereStr.append(" and td.dealer_code like '%"+dealer_code.toUpperCase()+"%'\n");
		}
		if(yieldly!=null&&!"".equals(yieldly))
			whereStr.append(" and tawor.yieldly = '"+yieldly+"'\n");
		if(in_warhouse_name!=null&&!"".equals(in_warhouse_name))
			whereStr.append(" and u.name like '%"+in_warhouse_name.replaceAll("'", "\''")+"%'\n");
		if(dealer_name!=null&&!"".equals(dealer_name))
			whereStr.append(" and td.dealer_name like '%"+dealer_name.replaceAll("'", "\''")+"%'\n");
		if(return_no!=null&&!"".equals(return_no))
			whereStr.append(" and tawor.return_no like '%"+return_no.toUpperCase().replaceAll("'", "\''")+"%'\n");
		if(in_start_date!=null&&!"".equals(in_start_date))
			whereStr.append(" and tawor.in_warhouse_date>=to_date('" + in_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		if(in_end_date!=null&&!"".equals(in_end_date))
			whereStr.append(" and tawor.in_warhouse_date<=to_date('" + in_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		if(return_start_date!=null&&!"".equals(return_start_date))
			whereStr.append(" and tawor.return_date>=to_date('" + return_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		if(return_end_date!=null&&!"".equals(return_end_date))
			whereStr.append(" and tawor.return_date<=to_date('" + return_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		if(transport_type!=null&&!"".equals(transport_type))
			whereStr.append(" and tawor.transport_type="+transport_type+"\n");
		if(back_type!=null&&!"".equals(back_type)){
			whereStr.append(" and tawor.status="+back_type+"\n");
		}else{
			whereStr.append(" and (tawor.status="+Constant.BACK_LIST_STATUS_03+" or tawor.status="+Constant.BACK_LIST_STATUS_04+" or tawor.status="+Constant.BACK_LIST_STATUS_05+")\n");
		}
		if(trans_no!=null&&!"".equals(trans_no))
			whereStr.append(" and tawor.tran_no like '%"+trans_no.toUpperCase().replaceAll("'", "\''")+"%'\n");
		
		groupStr.append("group by tawor.id,tawor.yieldly,tawor.audit_no,td.dealer_code,td.dealer_shortname,tawor.return_no,tawor.in_warhouse_date,tawor.tel,\n" );
		groupStr.append("tawor.create_date,tawor.return_date,tawor.return_type,tc.code_desc,tawor.status,tcc.code_desc,tawor.tran_no,u.name\n");
		
		orderStr.append(" order by tawor.return_date desc\n");
		
		System.out.println(sqlStr.toString()+whereStr.toString()+groupStr.toString()+orderStr.toString());
		
		PageResult<ClaimOldPartApproveStoreListBean> pr = pageQuery(ClaimOldPartApproveStoreListBean.class,
				sqlStr.toString()+whereStr.toString()+groupStr.toString()+orderStr.toString(),null, pageSize, curPage);
		return pr;
	}
	
	
	public PageResult<ClaimOldPartApproveStoreListBean> oldPartSignQueryList(Map params,int curPage, int pageSize){
		//获取前台条件
		String company_id=ClaimTools.dealParamStr(params.get("company_id"));//登陆人所在companyId
		String dealer_code=ClaimTools.dealParamStr(params.get("i_dealer_code"));//经销商代码
		String dealer_name=ClaimTools.dealParamStr(params.get("i_dealer_name"));//经销商简称
		String return_no=ClaimTools.dealParamStr(params.get("i_return_no"));//回运清单号
		String return_start_date=ClaimTools.dealParamStr(params.get("i_submit_start_date"));//提报开始日期
		String return_end_date=ClaimTools.dealParamStr(params.get("i_submit_end_date"));//提报结束日期
		String transport_type=ClaimTools.dealParamStr(params.get("i_transport_type"));//货运方式
		String transport_no=(String) params.get("transport_no");//发运单号
		String yieldly=ClaimTools.dealParamStr(params.get("yieldly"));//查询条件--产地
		
		StringBuffer sqlStr=new StringBuffer();
		StringBuffer whereStr=new StringBuffer();
		StringBuffer groupStr=new StringBuffer();
		StringBuffer orderStr=new StringBuffer();
		
		sqlStr.append("select  /*+RULE*/tawor.id as tawor_id, tawor.create_date as CREATE_DATES,tawor.id,tawor.yieldly,tawor.sign_no,td.dealer_code dealer_id,tawor.transport_no as transport_no,td.dealer_shortname dealer_name,tawor.tel,\n" );
		sqlStr.append("tawor.return_no claim_no,to_char(tawor.sign_date,'YYYY-MM-DD hh24:mi') sign_date,\n" );
		sqlStr.append("(select count(*) from tt_as_wr_old_returned_detail d where d.return_id=tawor.id and d.box_no is null) as box_no,");
		sqlStr.append("(select oo.WR_START_DATE || '至' || TO_CHAR(oo.RETURN_END_DATE, 'yyyy-mm-dd')  from tt_as_wr_returned_order oo,tr_return_logistics ll where oo.id=ll.return_id and ll.logictis_id=tawor.id) as wr_start_date,");
		sqlStr.append("to_char(tawor.return_date,'YYYY-MM-DD hh24:mi') return_date,\n" );
		sqlStr.append("tawor.return_type,tc.code_desc return_desc,\n" );
		sqlStr.append("nvl(sum(tawor.wr_amount),0) wr_amount,\n" );
		sqlStr.append("nvl(sum(tawor.parkage_amount),0) parkage_amount,\n" );
		sqlStr.append("nvl(sum(tawor.part_amount),0) part_amount,tawor.status back_type,tcc.code_desc back_desc,tawor.tran_no trans_no,u.name sign_name\n" );
		sqlStr.append("from tt_as_wr_old_returned tawor, tc_user u  ,tm_dealer td,tc_code tc,tc_code tcc \n" );
		sqlStr.append("where tawor.dealer_id=td.dealer_id   and tawor.transport_type=tc.code_id(+) and tawor.is_status=1\n" );
		sqlStr.append("and tawor.status=tcc.code_id   and tawor.sign_person=u.user_id(+) \n" );
		//条件
		if(dealer_code!=null&&!"".equals(dealer_code)){
		
				sqlStr.append("and td.dealer_CODE like '%"+dealer_code.toUpperCase()+"%'\n");
			
		}
		if(yieldly!=null&&!"".equals(yieldly))
			whereStr.append(" and tawor.yieldly = '"+yieldly+"'\n");
		if(dealer_name!=null&&!"".equals(dealer_name))
			whereStr.append(" and td.dealer_name like '%"+dealer_name.replaceAll("'", "\''")+"%'\n");
		if(return_no!=null&&!"".equals(return_no))
			whereStr.append(" and tawor.return_no like '%"+return_no.toUpperCase().replaceAll("'", "\''")+"%'\n");
		
		
		
		
		
		
		if(return_start_date!=null&&!"".equals(return_start_date))
			whereStr.append(" and tawor.return_date>=to_date('" + return_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		if(return_end_date!=null&&!"".equals(return_end_date))
			whereStr.append(" and tawor.return_date<=to_date('" + return_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		if(transport_type!=null&&!"".equals(transport_type))
			whereStr.append(" and tawor.transport_type="+transport_type+"\n");
		
			whereStr.append(" and ( tawor.status="+Constant.BACK_LIST_STATUS_02+" or tawor.status="+Constant.BACK_LIST_STATUS_03+" or tawor.status="+Constant.BACK_LIST_STATUS_04+" or tawor.status="+Constant.BACK_LIST_STATUS_05+" or tawor.status="+Constant.BACK_LIST_STATUS_07+")\n");

		if(transport_no!=null&&!"".equals(transport_no))
			whereStr.append(" and tawor.transport_no like '%"+transport_no.replaceAll("'", "\''")+"%'\n");
		
		groupStr.append("group by tawor.id,td.dealer_code,td.dealer_shortname,tawor.return_no,tawor.sign_date,tawor.tel,\n" );
		groupStr.append("tawor.create_date,tawor.return_date,tawor.yieldly,tawor.sign_no,tawor.transport_no,tawor.return_type,tc.code_desc,tawor.status,tcc.code_desc,tawor.tran_no,u.name\n");
		
		orderStr.append(" order by tawor.return_date desc\n");
		
		System.out.println(sqlStr.toString()+whereStr.toString()+groupStr.toString()+orderStr.toString());
		
		PageResult<ClaimOldPartApproveStoreListBean> pr = pageQuery(ClaimOldPartApproveStoreListBean.class,
				sqlStr.toString()+whereStr.toString()+groupStr.toString()+orderStr.toString(),null, pageSize, curPage);
		return pr;
	}
	
	
	public PageResult<ClaimOldPartApproveStoreListBean> ugencyPartSignQueryList(Map params,int curPage, int pageSize){
		//获取前台条件
		String company_id=ClaimTools.dealParamStr(params.get("company_id"));//登陆人所在companyId
		String dealer_code=ClaimTools.dealParamStr(params.get("i_dealer_code"));//经销商代码
		String dealer_name=ClaimTools.dealParamStr(params.get("i_dealer_name"));//经销商简称
		String return_no=ClaimTools.dealParamStr(params.get("i_return_no"));//回运清单号
		String return_start_date=ClaimTools.dealParamStr(params.get("i_submit_start_date"));//提报开始日期
		String return_end_date=ClaimTools.dealParamStr(params.get("i_submit_end_date"));//提报结束日期
		String transport_type=ClaimTools.dealParamStr(params.get("i_transport_type"));//货运方式
		String trans_no=ClaimTools.dealParamStr(params.get("i_trans_no"));//货运单号
		String yieldly=ClaimTools.dealParamStr(params.get("yieldly"));//查询条件--产地
		
		StringBuffer sqlStr=new StringBuffer();
		StringBuffer whereStr=new StringBuffer();
		StringBuffer groupStr=new StringBuffer();
		StringBuffer orderStr=new StringBuffer();
		
		sqlStr.append("select  /*+RULE*/  bor.borrow_phone,bor.next_time,bor.borrow_person,tawor.create_date as CREATE_DATES,tawor.id,tawor.yieldly,tawor.sign_no,td.dealer_code dealer_id,td.dealer_shortname dealer_name,tawor.tel,\n" );
		sqlStr.append("tawor.return_no claim_no,to_char(tawor.sign_date,'YYYY-MM-DD hh24:mi') sign_date,\n" );
		sqlStr.append("(select count(*) from tt_as_wr_old_returned_detail d where d.return_id=tawor.id and d.box_no is null) as box_no,");
		sqlStr.append("(select oo.WR_START_DATE || '至' || TO_CHAR(oo.RETURN_END_DATE, 'yyyy-mm-dd')  from tt_as_wr_returned_order oo,tr_return_logistics ll where oo.id=ll.return_id and ll.logictis_id=tawor.id) as wr_start_date,");
		sqlStr.append("to_char(tawor.return_date,'YYYY-MM-DD hh24:mi') return_date,\n" );
		sqlStr.append("tawor.return_type,tc.code_desc return_desc,\n" );
		sqlStr.append("nvl(sum(tawor.wr_amount),0) wr_amount,\n" );
		sqlStr.append("nvl(sum(tawor.parkage_amount),0) parkage_amount,\n" );
		sqlStr.append("nvl(sum(tawor.part_amount),0) part_amount,tawor.status back_type,tcc.code_desc back_desc,tawor.tran_no trans_no,u.name sign_name\n" );
		sqlStr.append("from tt_as_wr_old_returned tawor, tc_user u  ,tm_dealer td,tc_code tc,tc_code tcc , TT_AS_WR_RETURNED_ORDER tawro, TT_AS_PART_BORROW     bor \n" );
		sqlStr.append("where tawor.dealer_id=td.dealer_id   and tawor.transport_type=tc.code_id(+) and tawor.is_status=1\n" );
		sqlStr.append(" and bor.id (+)=tawro.borrow_no ");
		sqlStr.append("	and tawor.tran_no =tawro.return_no(+)  ");
		sqlStr.append("and tawor.status=tcc.code_id   and tawor.sign_person=u.user_id(+) \n" );
		sqlStr.append("and tawor.return_type=10731001");
		//条件
		if(dealer_code!=null&&!"".equals(dealer_code)){
				sqlStr.append("and td.dealer_CODE like '%"+dealer_code.toUpperCase()+"%'\n");
		}
		if(yieldly!=null&&!"".equals(yieldly))
			whereStr.append(" and tawor.yieldly = '"+yieldly+"'\n");
		if(dealer_name!=null&&!"".equals(dealer_name))
			whereStr.append(" and td.dealer_name like '%"+dealer_name.replaceAll("'", "\''")+"%'\n");
		if(return_no!=null&&!"".equals(return_no))
			whereStr.append(" and tawor.return_no like '%"+return_no.toUpperCase().replaceAll("'", "\''")+"%'\n");
		
		if(return_start_date!=null&&!"".equals(return_start_date))
			whereStr.append(" and tawor.return_date>=to_date('" + return_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		if(return_end_date!=null&&!"".equals(return_end_date))
			whereStr.append(" and tawor.return_date<=to_date('" + return_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		if(transport_type!=null&&!"".equals(transport_type))
			whereStr.append(" and tawor.transport_type="+transport_type+"\n");
		
			whereStr.append(" and ( tawor.status="+Constant.BACK_LIST_STATUS_02+" or tawor.status="+Constant.BACK_LIST_STATUS_03+" or tawor.status="+Constant.BACK_LIST_STATUS_04+" or tawor.status="+Constant.BACK_LIST_STATUS_05+")\n");

		if(trans_no!=null&&!"".equals(trans_no))
			whereStr.append(" and tawor.tran_no like '%"+trans_no.toUpperCase().replaceAll("'", "\''")+"%'\n");
		
		groupStr.append("group by tawor.id,td.dealer_code,td.dealer_shortname,tawor.return_no,tawor.sign_date,tawor.tel,\n" );
		groupStr.append("tawor.create_date,tawor.return_date,tawor.yieldly,tawor.sign_no,tawor.return_type,tc.code_desc,tawor.status,tcc.code_desc,tawor.tran_no,  bor.borrow_person,bor.borrow_phone,bor.next_time,u.name\n");
		
		orderStr.append(" order by tawor.return_date desc\n");
		
		System.out.println(sqlStr.toString()+whereStr.toString()+groupStr.toString()+orderStr.toString());
		
		PageResult<ClaimOldPartApproveStoreListBean> pr = pageQuery(ClaimOldPartApproveStoreListBean.class,
				sqlStr.toString()+whereStr.toString()+groupStr.toString()+orderStr.toString(),null, pageSize, curPage);
		return pr;
	}
	
	
	public PageResult<ClaimOldPartApproveStoreListBean> queryClaimBackList12(Map params,int curPage, int pageSize){
		//获取前台条件
		String company_id=ClaimTools.dealParamStr(params.get("company_id"));//登陆人所在companyId
		String dealer_code=ClaimTools.dealParamStr(params.get("i_dealer_code"));//经销商代码
		String dealer_name=ClaimTools.dealParamStr(params.get("i_dealer_name"));//经销商简称
		String return_no=ClaimTools.dealParamStr(params.get("i_return_no"));//回运清单号
		String in_start_date=ClaimTools.dealParamStr(params.get("i_create_start_date"));//入库开始日期
		String in_end_date=ClaimTools.dealParamStr(params.get("i_create_end_date"));//入库结束日期
		String return_start_date=ClaimTools.dealParamStr(params.get("i_submit_start_date"));//提报开始日期
		String return_end_date=ClaimTools.dealParamStr(params.get("i_submit_end_date"));//提报结束日期
		String transport_type=ClaimTools.dealParamStr(params.get("i_transport_type"));//货运方式
		String back_type=ClaimTools.dealParamStr(params.get("i_back_type"));//回运单状态
		String trans_no=ClaimTools.dealParamStr(params.get("i_trans_no"));//货运单号
		String yieldlys=ClaimTools.dealParamStr(params.get("yieldlys"));//用户拥有的产地权限
		String yieldly=ClaimTools.dealParamStr(params.get("yieldly"));//查询条件--产地
		String sfsm=ClaimTools.dealParamStr(params.get("sfsm"));//查询条件--是否已扫描
		String sfcq=ClaimTools.dealParamStr(params.get("sfcq"));//查询条件--是否超期
		String sfshz=ClaimTools.dealParamStr(params.get("sfshz"));//查询条件--是否超期审核中
		String sfcqsh=ClaimTools.dealParamStr(params.get("sfcqsh"));//查询条件--是否经过超期审核
		
		StringBuffer sqlStr=new StringBuffer();
		StringBuffer whereStr=new StringBuffer();
		StringBuffer groupStr=new StringBuffer();
		StringBuffer orderStr=new StringBuffer();
		
		sqlStr.append("select /*+RULE+*/* From (select /*+RULE+*/  decode( tawor.is_overdue,10011001,'超期',10011002,'不超期') as is_overdue,tawor.is_overduecheck,tawor.create_date as CREATE_DATES,tawor.id,td.dealer_code dealer_id,td.dealer_shortname dealer_name,tawor.tel,\n" );
		sqlStr.append("tawor.return_no claim_no,to_char(tawor.in_warhouse_date,'YYYY-MM-DD') create_date,\n" );
		sqlStr.append("(select count(*) from tt_as_wr_old_returned_detail d where d.return_id=tawor.id and d.box_no is null) as box_no,");
		sqlStr.append("(select max(oo.wr_start_date) from tt_as_wr_returned_order oo,tr_return_logistics ll where oo.id=ll.return_id and ll.logictis_id=tawor.id) as wr_start_date,");
		sqlStr.append("to_char(tawor.return_date,'YYYY-MM-DD') return_date,\n" );
		sqlStr.append("tawor.return_type,tc.code_desc return_desc,\n" );
		sqlStr.append("tawor.wr_amount wr_amount,\n" );
		sqlStr.append("tawor.parkage_amount parkage_amount,\n" );
		sqlStr.append("tawor.part_amount part_amount,tawor.status back_type,tcc.code_desc back_desc,tawor.tran_no trans_no,decode( (select count(ta.is_upload)   from tt_as_wr_old_returned_detail ta  where ta.return_id = tawor.id), '0','未扫描',  '已扫描' ) as is_sm  ,(select count(ta.is_upload)             from tt_as_wr_old_returned_detail ta            where ta.return_id = tawor.id) as cou\n" );
		sqlStr.append("from tt_as_wr_old_returned tawor,tm_dealer td,tc_code tc,tc_code tcc\n" );
		sqlStr.append("where tawor.dealer_id=td.dealer_id   and tawor.transport_type=tc.code_id(+) and tawor.is_status=1\n" );
		/**********2010-12-14**********/
		//sqlStr.append("and tawor.status not in('"+Constant.BACK_LIST_STATUS_04+"')\n" );
		/********************/
		sqlStr.append("and tawor.status=tcc.code_id\n" );
		sqlStr.append("and tawor.yieldly in ("+yieldlys+")\n" );
		//条件
		if(dealer_code!=null&&!"".equals(dealer_code)){
			String[] temp=dealer_code.split(",");
			String str="";
			if(temp.length>0){
				sqlStr.append(" and (");
				for(int count=0;count<temp.length;count++){
					str+=" td.dealer_code='"+temp[count].replaceAll("'", "\''")+"' or\n";
				}
				sqlStr.append(str.substring(0, str.length()-3));
				sqlStr.append(")\n");
			}
		}
		if(yieldly!=null&&!"".equals(yieldly))
			whereStr.append(" and tawor.yieldly = '"+yieldly+"'\n");
		if(dealer_name!=null&&!"".equals(dealer_name))
			whereStr.append(" and td.dealer_name like '%"+dealer_name.replaceAll("'", "\''")+"%'\n");
		if(return_no!=null&&!"".equals(return_no))
			whereStr.append(" and tawor.return_no like '%"+return_no.toUpperCase().replaceAll("'", "\''")+"%'\n");
		if(in_start_date!=null&&!"".equals(in_start_date))
			whereStr.append(" and tawor.in_warhouse_date>=to_date('" + in_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		if(in_end_date!=null&&!"".equals(in_end_date))
			whereStr.append(" and tawor.in_warhouse_date<=to_date('" + in_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		if(return_start_date!=null&&!"".equals(return_start_date))
			whereStr.append(" and tawor.return_date>=to_date('" + return_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		if(return_end_date!=null&&!"".equals(return_end_date))
			whereStr.append(" and tawor.return_date<=to_date('" + return_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		if(transport_type!=null&&!"".equals(transport_type))
			whereStr.append(" and tawor.transport_type="+transport_type+"\n");
		if(back_type!=null&&!"".equals(back_type)){
			whereStr.append(" and tawor.status="+back_type+"\n");
		}
		//else{
			//whereStr.append(" and (tawor.status="+Constant.BACK_LIST_STATUS_01+" or tawor.status="+Constant.BACK_LIST_STATUS_02+" or tawor.status="+Constant.BACK_LIST_STATUS_03+" or tawor.status="+Constant.BACK_LIST_STATUS_04+" or tawor.status="+Constant.BACK_LIST_STATUS_05+")\n");
	//	}
		if(trans_no!=null&&!"".equals(trans_no)){
			whereStr.append(" and tawor.tran_no like '%"+trans_no.toUpperCase().replaceAll("'", "\''")+"%'\n");
		}
		
		if(sfsm.equals("ys")){
			
			whereStr.append(" and (select count(ta.is_upload)             from tt_as_wr_old_returned_detail ta            where ta.return_id = tawor.id) >0\n");
		}
		
				
		if(sfcq.equals("ycq")){
					
					whereStr.append(" and tawor.IS_OVERDUE= 10011001\n");
				}
		else if(sfcq.equals("wcq")){
			whereStr.append(" and tawor.IS_OVERDUE= 10011002\n");
		}
		
		if(sfshz.equals("yshz")){
			
			whereStr.append(" and tawor.IS_OVERDUECHECK= 10011001\n");
		}
		else if(sfshz.equals("wshz")){
		whereStr.append(" and tawor.IS_OVERDUECHECK= 10011002\n");
		}
		
		if(sfcqsh.endsWith("s")){
			whereStr.append(" and tawor.IS_OVERDUECHECKd= 1\n");
		}
		else if (sfcqsh.endsWith("f")){
			whereStr.append(" and tawor.IS_OVERDUECHECKd= 0\n");
			
		}
		
		//groupStr.append("group by tawor.id,td.dealer_code,td.dealer_shortname,tawor.return_no,tawor.in_warhouse_date,tawor.tel,\n" );
		//groupStr.append("tawor.create_date,tawor.return_date,tawor.return_type,tc.code_desc,tawor.status,tcc.code_desc,tawor.tran_no\n");
		
	//	orderStr.append(" )  order by cou,rowId desc \n");
		orderStr.append(" )  order by wr_start_date desc \n");
		
		
		System.out.println(sqlStr.toString()+whereStr.toString()+groupStr.toString()+orderStr.toString());
		
		PageResult<ClaimOldPartApproveStoreListBean> pr = pageQuery(ClaimOldPartApproveStoreListBean.class,
				sqlStr.toString()+whereStr.toString()+groupStr.toString()+orderStr.toString(),null, pageSize, curPage);
		return pr;
	}
	
	public PageResult<ClaimOldPartApproveStoreListBean> queryOverdueApproveList(Map params,int curPage, int pageSize){
		//获取前台条件
		String company_id=ClaimTools.dealParamStr(params.get("company_id"));//登陆人所在companyId
		String dealer_code=ClaimTools.dealParamStr(params.get("i_dealer_code"));//经销商代码
		String dealer_name=ClaimTools.dealParamStr(params.get("i_dealer_name"));//经销商简称
		String return_no=ClaimTools.dealParamStr(params.get("i_return_no"));//回运清单号
		String in_start_date=ClaimTools.dealParamStr(params.get("i_create_start_date"));//入库开始日期
		String in_end_date=ClaimTools.dealParamStr(params.get("i_create_end_date"));//入库结束日期
		String return_start_date=ClaimTools.dealParamStr(params.get("i_submit_start_date"));//提报开始日期
		String return_end_date=ClaimTools.dealParamStr(params.get("i_submit_end_date"));//提报结束日期
		String transport_type=ClaimTools.dealParamStr(params.get("i_transport_type"));//货运方式
		String back_type=ClaimTools.dealParamStr(params.get("i_back_type"));//回运单状态
		String trans_no=ClaimTools.dealParamStr(params.get("i_trans_no"));//货运单号
		String yieldlys=ClaimTools.dealParamStr(params.get("yieldlys"));//用户拥有的产地权限
		String yieldly=ClaimTools.dealParamStr(params.get("yieldly"));//查询条件--产地
		String sfsm=ClaimTools.dealParamStr(params.get("sfsm"));//查询条件--是否已扫描
		
		StringBuffer sqlStr=new StringBuffer();
		StringBuffer whereStr=new StringBuffer();
		StringBuffer groupStr=new StringBuffer();
		StringBuffer orderStr=new StringBuffer();
		
		sqlStr.append("select /*+RULE+*/* From (select /*+RULE+*/ tawor.create_date as CREATE_DATES,tawor.id,td.dealer_code dealer_id,td.dealer_shortname dealer_name,tawor.tel,\n" );
		sqlStr.append("tawor.return_no claim_no,to_char(tawor.in_warhouse_date,'YYYY-MM-DD') create_date,\n" );
		sqlStr.append("(select count(*) from tt_as_wr_old_returned_detail d where d.return_id=tawor.id and d.box_no is null) as box_no,");
		sqlStr.append("(select max(oo.wr_start_date) from tt_as_wr_returned_order oo,tr_return_logistics ll where oo.id=ll.return_id and ll.logictis_id=tawor.id) as wr_start_date,");
		sqlStr.append("to_char(tawor.return_date,'YYYY-MM-DD') return_date,\n" );
		sqlStr.append("tawor.return_type,tc.code_desc return_desc,\n" );
		sqlStr.append("tawor.wr_amount wr_amount,\n" );
		sqlStr.append("tawor.parkage_amount parkage_amount,\n" );
		sqlStr.append("tawor.part_amount part_amount,tawor.status back_type,tcc.code_desc back_desc,tawor.tran_no trans_no,decode( (select count(ta.is_upload)   from tt_as_wr_old_returned_detail ta  where ta.return_id = tawor.id), '0','未扫描',  '已扫描' ) as is_sm  ,(select count(ta.is_upload)             from tt_as_wr_old_returned_detail ta            where ta.return_id = tawor.id) as cou\n" );
		sqlStr.append("from tt_as_wr_old_returned tawor,tm_dealer td,tc_code tc,tc_code tcc\n" );
		sqlStr.append("where tawor.dealer_id=td.dealer_id   and tawor.transport_type=tc.code_id(+) and tawor.is_status=1\n" );
		/**********2010-12-14**********/
		//sqlStr.append("and tawor.status not in('"+Constant.BACK_LIST_STATUS_04+"')\n" );
		/********************/
		
		//必须为超期的
		sqlStr.append("and tawor.is_Overduecheck   = 10011001\n" );
		
		sqlStr.append("and tawor.status=tcc.code_id\n" );
		sqlStr.append("and tawor.yieldly in ("+yieldlys+")\n" );
		//条件
		if(dealer_code!=null&&!"".equals(dealer_code)){
			String[] temp=dealer_code.split(",");
			String str="";
			if(temp.length>0){
				sqlStr.append(" and (");
				for(int count=0;count<temp.length;count++){
					str+=" td.dealer_code='"+temp[count].replaceAll("'", "\''")+"' or\n";
				}
				sqlStr.append(str.substring(0, str.length()-3));
				sqlStr.append(")\n");
			}
		}
		if(yieldly!=null&&!"".equals(yieldly))
			whereStr.append(" and tawor.yieldly = '"+yieldly+"'\n");
		if(dealer_name!=null&&!"".equals(dealer_name))
			whereStr.append(" and td.dealer_name like '%"+dealer_name.replaceAll("'", "\''")+"%'\n");
		if(return_no!=null&&!"".equals(return_no))
			whereStr.append(" and tawor.return_no like '%"+return_no.toUpperCase().replaceAll("'", "\''")+"%'\n");
		if(in_start_date!=null&&!"".equals(in_start_date))
			whereStr.append(" and tawor.in_warhouse_date>=to_date('" + in_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		if(in_end_date!=null&&!"".equals(in_end_date))
			whereStr.append(" and tawor.in_warhouse_date<=to_date('" + in_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		if(return_start_date!=null&&!"".equals(return_start_date))
			whereStr.append(" and tawor.return_date>=to_date('" + return_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		if(return_end_date!=null&&!"".equals(return_end_date))
			whereStr.append(" and tawor.return_date<=to_date('" + return_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		if(transport_type!=null&&!"".equals(transport_type))
			whereStr.append(" and tawor.transport_type="+transport_type+"\n");
		if(back_type!=null&&!"".equals(back_type)){
			whereStr.append(" and tawor.status="+back_type+"\n");
		}
		//else{
			//whereStr.append(" and (tawor.status="+Constant.BACK_LIST_STATUS_01+" or tawor.status="+Constant.BACK_LIST_STATUS_02+" or tawor.status="+Constant.BACK_LIST_STATUS_03+" or tawor.status="+Constant.BACK_LIST_STATUS_04+" or tawor.status="+Constant.BACK_LIST_STATUS_05+")\n");
	//	}
		if(trans_no!=null&&!"".equals(trans_no)){
			whereStr.append(" and tawor.tran_no like '%"+trans_no.toUpperCase().replaceAll("'", "\''")+"%'\n");
		}
		
		if(sfsm.equals("ys")){
			
			whereStr.append(" and (select count(ta.is_upload)             from tt_as_wr_old_returned_detail ta            where ta.return_id = tawor.id) >0\n");
		}
		//groupStr.append("group by tawor.id,td.dealer_code,td.dealer_shortname,tawor.return_no,tawor.in_warhouse_date,tawor.tel,\n" );
		//groupStr.append("tawor.create_date,tawor.return_date,tawor.return_type,tc.code_desc,tawor.status,tcc.code_desc,tawor.tran_no\n");
		
	//	orderStr.append(" )  order by cou,rowId desc \n");
		orderStr.append(" )  order by wr_start_date desc \n");
		
		
		System.out.println(sqlStr.toString()+whereStr.toString()+groupStr.toString()+orderStr.toString());
		
		PageResult<ClaimOldPartApproveStoreListBean> pr = pageQuery(ClaimOldPartApproveStoreListBean.class,
				sqlStr.toString()+whereStr.toString()+groupStr.toString()+orderStr.toString(),null, pageSize, curPage);
		return pr;
	}
	/**
	 * Function：通过回运主键查询回运信息
	 * @param  ：	
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-13 赵伦达
	 */
	public ClaimApproveAndStoredReturnInfoBean getApproveAndStoredReturnInfo(Map params){
		String claim_id=ClaimTools.dealParamStr(params.get("i_claim_id"));
		if(!"".equals(claim_id)){
			//
			StringBuffer sqlStr= new StringBuffer();
			sqlStr.append("select tawor.id,tm.dealer_code,tm.dealer_name,tawor.tel,\n" );
			sqlStr.append("torg.org_name attach_area,tawor.transport_type,tc.code_desc transport_desc,\n" );
			sqlStr.append("sum(tawor.parkage_amount) parkage_amount,tawor.return_no,\n" );
			sqlStr.append("to_char(tawor.create_date,'YYYY-MM-DD') create_date,\n" );
			sqlStr.append("to_char(tawor.return_date,'YYYY-MM-DD') return_date,\n" );
			sqlStr.append("sum(tawor.wr_amount) wr_amount,sum(tawor.part_item_amount) part_item_amount,\n" );
			sqlStr.append("sum(tawor.part_amount) part_amount,tawor.return_type,\n" );
			sqlStr.append("tcc.code_desc return_desc,tawor.wr_start_date,tawor.tran_no tran_no\n" );
			sqlStr.append("from tt_as_wr_old_returned tawor,tm_dealer tm,tm_dealer_org_relation tdor,\n" );
			sqlStr.append("tm_org torg,tc_code tc,tc_code tcc\n" );
			sqlStr.append("where tawor.transport_type=tc.code_id and tawor.return_type=tcc.code_id\n" );
			sqlStr.append("and tawor.dealer_id=tm.dealer_id and f_get_pid(tawor.dealer_id)=tdor.dealer_id\n" );
			sqlStr.append("and tdor.org_id=torg.org_id\n" );
			sqlStr.append("and tawor.id="+claim_id+"\n" );
			sqlStr.append("group by tawor.id,tawor.dealer_id,tm.dealer_code,tm.dealer_name,torg.org_name,tawor.tel,\n" );
			sqlStr.append("tawor.transport_type,tc.code_desc,tawor.return_no,tawor.create_date,\n" );
			sqlStr.append("tawor.return_date,tawor.return_type,tcc.code_desc,tawor.wr_start_date,tawor.tran_no");
			System.out.println("sqlsql=="+sqlStr);
			PageResult<ClaimApproveAndStoredReturnInfoBean> pr = pageQuery(ClaimApproveAndStoredReturnInfoBean.class,sqlStr.toString(), null,10,1);
			
			if(pr!=null&&pr.getTotalPages()>0){
				return (ClaimApproveAndStoredReturnInfoBean)pr.getRecords().get(0);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	//zhumingwei 2011-04-27
	public ClaimApproveAndStoredReturnInfoBean getApproveAndStoredReturnInfo111(Map params){
		String claim_id=ClaimTools.dealParamStr(params.get("i_claim_id"));
		if(!"".equals(claim_id)){
			//
			StringBuffer sqlStr= new StringBuffer();
			sqlStr.append("select tawor.price,tawor.remark,tawor.auth_price new_price,tawor.id,tm.dealer_code,tm.dealer_name,tawor.tel,tawor.price_remark,\n" );
			sqlStr.append("torg.org_name attach_area,tawor.transport_type,tc.code_desc transport_desc,\n" );
			sqlStr.append("sum(tawor.parkage_amount) parkage_amount,tawor.return_no,\n" );
			sqlStr.append("to_char(tawor.create_date,'YYYY-MM-DD') create_date,\n" );
			sqlStr.append("to_char(tawor.return_date,'YYYY-MM-DD') return_date,\n" );
			sqlStr.append("sum(tawor.wr_amount) wr_amount,sum(tawor.part_item_amount) part_item_amount,\n" );
			sqlStr.append("sum(tawor.part_amount) part_amount,tawor.return_type,\n" );
			sqlStr.append("tcc.code_desc return_desc,tawor.wr_start_date,tawor.tran_no tran_no,to_char(tawor.send_time,'yyyy-mm-dd') send_time\n" );
			sqlStr.append("from tt_as_wr_old_returned tawor,tm_dealer tm,tm_dealer_org_relation tdor,\n" );
			sqlStr.append("tm_org torg,tc_code tc,tc_code tcc\n" );
			sqlStr.append("where tawor.transport_type=tc.code_id(+) and tawor.return_type=tcc.code_id(+)\n" );
			sqlStr.append("and tawor.dealer_id=tm.dealer_id and f_get_pid(tawor.dealer_id)=tdor.dealer_id\n" );
			sqlStr.append("and tdor.org_id=torg.org_id\n" );
			sqlStr.append("and tawor.id="+claim_id+"\n" );
			sqlStr.append("group by tawor.price,tawor.remark,tawor.auth_price,tawor.id,tawor.dealer_id,tm.dealer_code,tm.dealer_name,torg.org_name,tawor.tel,tawor.price_remark,\n" );
			sqlStr.append("tawor.transport_type,tc.code_desc,tawor.return_no,tawor.create_date,\n" );
			sqlStr.append("tawor.return_date,tawor.return_type,tawor.send_time,tcc.code_desc,tawor.wr_start_date,tawor.tran_no");
			System.out.println("sqlsql=="+sqlStr);
			PageResult<ClaimApproveAndStoredReturnInfoBean> pr = pageQuery(ClaimApproveAndStoredReturnInfoBean.class,sqlStr.toString(), null,10,1);
			
			if(pr!=null&&pr.getTotalPages()>0){
				return (ClaimApproveAndStoredReturnInfoBean)pr.getRecords().get(0);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	/**
	 * zyw 2014-1-29重构
	 * @param params
	 * @return
	 */
	public ClaimApproveAndStoredReturnInfoBean getApproveAndStoredReturnInfo11(Map params){
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
//			sb.append("       torg.org_name attach_area,\n" );
//			sb.append("       d.transport_name，d.transport_id,\n" );
			sb.append("       tawor.transport_type,\n" );
			sb.append("       f_get_tc_code(tawor.transport_type) as transport_desc,\n" );
			sb.append("       tawor.parkage_amount,\n" );
			sb.append("       tawor.return_no,\n" );
			sb.append("       tawor.remark,\n" );
			sb.append("       tawor.transport_company,\n" );
			sb.append("       tawor.transport_remark,\n" );
			sb.append("       to_char(tawor.Send_Time, 'YYYY-MM-DD') create_date,\n" );
			sb.append("       to_char(tawor.return_date, 'YYYY-MM-DD') return_date,\n" );
			sb.append("       tawor.wr_amount,\n" );
			sb.append("       tawor.part_item_amount,\n" );
			sb.append("       tawor.part_amount,\n" );
			sb.append("       tawor.return_type,\n" );
			sb.append("       tawor.out_part_packge,\n" );
			sb.append("       tawor.sign_remark,\n" );
			sb.append("       f_get_tc_code(tawor.return_type) as return_desc,\n" );
			sb.append("       to_char(tawor.wr_start_date,'yyyy-mm-dd')||'至'||to_char(tawor.return_end_date,'yyyy-mm-dd') wr_start_date,\n" );
			sb.append("       tawor.tran_no tran_no,\n" );
			sb.append("       tawor.transport_no,\n" );
			sb.append("       tawor.real_box_no,\n" );
			sb.append("       tawor.part_pakge,\n" );
			sb.append("       tawor.part_mark,tu.name in_warhouse_name,  \n" );
			sb.append("       tawor.part_detail\n" );
			sb.append("  from tt_as_wr_returned_order       tawor,\n" );
//			sb.append("       tm_oldpart_transport_detail d,\n" );
			sb.append("       tc_user tu,\n" );
			sb.append("       tm_dealer                   tm,\n" );
			sb.append("       tm_dealer_org_relation      tdor,\n" );
			sb.append("       tm_org                      torg\n" );
			sb.append(" where 1=1\n" );
			sb.append("   and tawor.dealer_id = tm.dealer_id(+)\n" );
//			sb.append("   AND d.detail_id(+) = tawor.tran_person\n" );
			sb.append("   and tawor.in_warhouse_by = tu.user_id(+)\n" );
			sb.append("   and f_get_pid(tawor.dealer_id) = tdor.dealer_id(+)\n" );
			sb.append("   and tdor.org_id = torg.org_id(+)\n");
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
	
	public ClaimApproveAndStoredReturnInfoBean oldPartSignQueryDetail(Map params){
		String claim_id=ClaimTools.dealParamStr(params.get("i_claim_id"));
		if(!"".equals(claim_id)){
			StringBuffer sqlStr= new StringBuffer();
			sqlStr.append("select tawor.status,tawor.price,tawor.auth_price new_Price,tawor.id,tm.dealer_code,tm.dealer_name,tawor.tel,tawor.sign_remark,\n" );
			sqlStr.append("torg.org_name attach_area,tawor.transport_type,tc.code_desc transport_desc,\n" );
			sqlStr.append("tawor.parkage_amount,tawor.return_no,\n" );
			sqlStr.append("to_char(tawor.create_date,'YYYY-MM-DD') create_date,\n" );
			sqlStr.append("to_char(tawor.return_date,'YYYY-MM-DD') return_date,\n" );
			sqlStr.append("tawor.wr_amount,tawor.part_item_amount,\n" );
			sqlStr.append("tawor.part_amount,tawor.return_type,d.transport_name,tawor.out_part_packge,\n" );
			sqlStr.append("tcc.code_desc return_desc,tawor.wr_start_date,tawor.tran_no tran_no,tawor.transport_no, tawor.real_box_no,tawor.part_pakge,tawor.part_mark,tawor.part_detail\n" );
			sqlStr.append("from tt_as_wr_old_returned tawor,tm_oldpart_transport_detail d ,tm_dealer tm,tm_dealer_org_relation tdor,\n" );
			sqlStr.append("tm_org torg,tc_code tc,tc_code tcc\n" );
			sqlStr.append("where tawor.transport_type=tc.code_id(+) and tawor.return_type=tcc.code_id\n" );
			sqlStr.append("and tawor.dealer_id=tm.dealer_id   AND d.detail_id(+) = tawor.tran_person and f_get_pid(tawor.dealer_id)=tdor.dealer_id\n" );
			sqlStr.append("and tdor.org_id=torg.org_id\n" );
			sqlStr.append("and tawor.id="+claim_id+"\n" );
			System.out.println("sqlsql=="+sqlStr);
			PageResult<ClaimApproveAndStoredReturnInfoBean> pr = pageQuery(ClaimApproveAndStoredReturnInfoBean.class,sqlStr.toString(), null,10,1);
			
			if(pr!=null&&pr.getTotalPages()>0){
				return (ClaimApproveAndStoredReturnInfoBean)pr.getRecords().get(0);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	public PageResult<TtAsWrOldPartSignDetailListBean> queryClaimBackDetailList(Map params,int curPage, int pageSize){
		String claim_id=ClaimTools.dealParamStr(params.get("i_claim_id"));
		if(!"".equals(claim_id)){
			StringBuffer sqlStr= new StringBuffer();
			sqlStr.append("select taword.id,taword.claim_no,taword.vin,\n" );
			sqlStr.append("tawp.part_code,tawp.part_name,nvl(sum(taword.return_amount),0) return_amount,\n" );
			sqlStr.append("nvl(sum(taword.sign_amount),0) sign_amount,\n" );
			sqlStr.append("taword.box_no,taword.warehouse_region,taword.deduct_remark,nvl(tc.code_desc,'') deduct_desc\n" );
			sqlStr.append("from tt_as_wr_old_returned tawor,\n" );
			sqlStr.append("tt_as_wr_old_returned_detail taword,\n" );
			sqlStr.append("tt_as_wr_partsitem tawp,tc_code tc\n" );
			sqlStr.append("where tawor.id=taword.return_id and taword.part_id=tawp.part_id and taword.deduct_remark=tc.code_id(+)\n" );
			sqlStr.append("and tawor.id="+claim_id+"\n" );
			sqlStr.append("group by taword.id,taword.claim_no,taword.vin,\n" );
			sqlStr.append("tawp.part_code,tawp.part_name,tc.code_desc,\n" );
			sqlStr.append("taword.box_no,taword.warehouse_region,taword.deduct_remark,taword.create_date\n");
			sqlStr.append("order by taword.create_date\n");
			
			PageResult<TtAsWrOldPartSignDetailListBean> pr = pageQuery(TtAsWrOldPartSignDetailListBean.class,sqlStr.toString(),null, pageSize, curPage);
			if(pr!=null){
				return pr;
			}else{
				return null;
			}
		}else{
			return null;
		}
		
	}
	
	public List<TtAsWrOldPartSignDetailListBean> queryClaimBackDetailList3(Map params){
		String claim_id=ClaimTools.dealParamStr(params.get("i_claim_id"));
		if(!"".equals(claim_id)){
			StringBuffer sqlStr= new StringBuffer();
			sqlStr.append("select taword.id,taword.claim_no,taword.vin,taword.in_warhouse_status,f_get_tc_code(taword.in_warhouse_status) in_warhouse_status_name,\n" );
			sqlStr.append("tawp.part_code,tawp.part_cname part_name,taword.producer_code,taword.return_amount,\n" );
			sqlStr.append("to_char(tawor.wr_start_date,'yyyy-mm-dd')||'至'||to_char(tawor.return_end_date,'yyyy-mm-dd') wr_start_date,\n" );
			sqlStr.append("taword.sign_amount,TAWORD.LOCAL_WAR_HOUSE||TAWORD.LOCAL_WAR_SHEL||TAWORD.LOCAL_WAR_LAYER Local_War_House,\n" );
			sqlStr.append("taword.box_no,taword.warehouse_region,taword.deduct_remark,taword.other_remark,nvl(tc.code_desc,'') deduct_desc\n" );
			sqlStr.append("from tt_as_wr_returned_order tawor,\n" );
			sqlStr.append("tt_as_wr_returned_order_detail taword,\n" );
			sqlStr.append("tt_as_wr_app_part tawp,tc_code tc\n" );
			sqlStr.append("where tawor.id=taword.return_id and taword.claim_part_id=tawp.claim_part_id and taword.DEDUCT_REMARK=tc.code_id(+)\n" );
			sqlStr.append("and tawor.id="+claim_id+"\n" );
			sqlStr.append("order by taword.create_date\n");
			
			List<TtAsWrOldPartSignDetailListBean> pr = this.select(TtAsWrOldPartSignDetailListBean.class,sqlStr.toString(),null);
			if(pr!=null){
				return pr;
			}else{
				return null;
			}
		}else{
			return null;
		}
		
	}
	
	public List<Map<String,Object>> queryClaimBackDetailList2(Map params){
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
			//sqlStr.append("order by  box_no,claim_no,nlssort(part_CODE,'NLS_SORT=SCHINESE_RADICAL_M') asc\n");
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
	
	
	public List<Map<String,Object>> queryClaimBackDetailListZg2(Map params){
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
			sqlStr.append("where tawor.id=taword.return_id and taword.RETURN_AMOUNT != taword.SIGN_AMOUNT and taword.Executive_director_sta=0   and  taword.part_id=tawp.part_id and taword.deduct_remark=tc.code_id(+)\n" );
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
			
			
			sqlStr.append(" )\n");
			//sqlStr.append("order by  box_no,claim_no,nlssort(part_CODE,'NLS_SORT=SCHINESE_RADICAL_M') asc\n");
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
	
	public PageResult<Map<String,Object>> queryClaimBackDetailList3(Map<String, String> map,int pageSize,int curPage ){

		String claim_id=ClaimTools.dealParamStr(map.get("i_claim_id"));
		String boxNo=ClaimTools.dealParamStr(map.get("boxNo"));
		String bar_code=ClaimTools.dealParamStr(map.get("bar_code"));
		String partName = ClaimTools.dealParamStr(map.get("partName"));
		String partCode = ClaimTools.dealParamStr(map.get("partCode"));
		String sing_num = ClaimTools.dealParamStr(map.get("sing_num"));
		String is_import = ClaimTools.dealParamStr(map.get("is_import"));
		String dealerCode = ClaimTools.dealParamStr(map.get("dealerCode"));
		String dealerName = ClaimTools.dealParamStr(map.get("dealerName"));
		String claim_no = ClaimTools.dealParamStr(map.get("claim_no"));
		String vin = ClaimTools.dealParamStr(map.get("vin"));
		String inWarhouseStatus = ClaimTools.dealParamStr(map.get("inWarhouseStatus"));
		String producerCode = ClaimTools.dealParamStr(map.get("producerCode"));
		String producerName = ClaimTools.dealParamStr(map.get("producerName"));
		if(!"".equals(claim_id)){
			StringBuffer sqlStr= new StringBuffer();
			//sqlStr.append("select * from (");
			sqlStr.append(" select  c.create_date,c.service_order_code ro_no,taword.id,TAWORD.Is_Main_Code,taword.is_out,taword.claim_no,taword.vin,c.id as claim_id,taword.PRODUCER_NAME,taword.producer_code,to_char(c.repair_date_begin,'yyyy-mm-dd') as RO_STARTDATE,c.apply_remark,del.dealer_code,c.repair_type,\n" );
			sqlStr.append("taword.in_warhouse_status,taword.claim_part_id,taword.part_id,taword.claim_supplier_code,taword.claim_supplier_name,tawp.old_part_code part_code,tawp.old_part_cname part_cname,nvl(taword.return_amount,0) return_amount,\n" );
			sqlStr.append("nvl(taword.sign_amount,0) sign_amount,c.is_bill,taword.barcode_no,decode( TAWORD.LOCAL_WAR_HOUSE,NULL,b.local_war_house,TAWORD.LOCAL_WAR_HOUSE) LOCAL_WAR_HOUSE,decode( TAWORD.LOCAL_WAR_SHEL,NULL,b.LOCAL_WAR_SHEL,TAWORD.LOCAL_WAR_SHEL) LOCAL_WAR_SHEL,decode( TAWORD.LOCAL_WAR_LAYER,NULL,b.LOCAL_WAR_LAYER,TAWORD.LOCAL_WAR_LAYER) LOCAL_WAR_LAYER,\n" );
			sqlStr.append("taword.box_no,taword.warehouse_region,taword.deduct_remark,taword.other_remark,decode(taword.deduct_remark,0,'--请选择--',null,'--请选择--',tc.code_desc) deduct_desc,TAWORD.Is_Import\n" );
			sqlStr.append("from tt_as_wr_returned_order tawor , tm_pt_part_base b ,\n" );
			sqlStr.append("tt_as_wr_returned_order_detail taword,\n" );
			sqlStr.append("tt_as_wr_app_part tawp,tc_code tc,tt_as_wr_application_claim c,tm_dealer del\n" );
			sqlStr.append("where tawor.id=taword.return_id  AND taword.part_code = b.part_code(+) AND c.id = taword.claim_id and taword.claim_part_id=tawp.claim_part_id and taword.deduct_remark=tc.code_id(+)\n" );
			sqlStr.append("and taword.is_sign=0 AND  tawor.id="+claim_id+"  and del.dealer_id=c.dealer_id\n" );
			if(Utility.testString(vin)){
				sqlStr.append("   and  taword.vin like '%"+vin.toUpperCase()+"%'\n" );
			}
			if(Utility.testString(inWarhouseStatus)){
				sqlStr.append("   and  taword.in_warhouse_status="+inWarhouseStatus+"\n" );
			}
			if(!"".equals(boxNo)){
				sqlStr.append("and taword.box_no='"+boxNo+"'\n" );
			}
			if(Utility.testString(partCode)){
				sqlStr.append(" and taword.part_code like'%"+partCode.toUpperCase()+"%'\n" );
			}
			if(Utility.testString(partName)){
				sqlStr.append(" and taword.part_Name like'%"+partName+"%'\n" );
			}
			if(Utility.testString(bar_code)){
				sqlStr.append(" and taword.barcode_no like'%"+bar_code.toUpperCase()+"%'\n" );
			}
			if(Utility.testString(sing_num)){
				sqlStr.append("   and  taword.sign_amount="+sing_num+"\n" );
			}
			if(Utility.testString(is_import)){
				sqlStr.append("   and  taword.is_import="+is_import+"\n" );
			}
			if(Utility.testString(dealerCode)){
				sqlStr.append("   and  del.dealer_code like '%"+dealerCode+"%'\n");
			}
			if(Utility.testString(dealerName)){
				sqlStr.append("   and  del.dealer_name like '%"+dealerName+"%'\n" );
			}
			//威旺代码和名称模糊查询条件 2014-6-25
//			if(Utility.testString(dealerCode)){
//				sqlStr.append("   and  c.WW_DEALER_CODE like  '%"+dealerCode+"%'\n");
//			}
//			if(Utility.testString(dealerName)){
//				sqlStr.append("   and  c.WW_DEALER_NAME like '%"+dealerName+"%'\n" );
//			}
			if(Utility.testString(claim_no)){
				sqlStr.append("   and  taword.claim_no like '%"+claim_no+"%'\n" );
			}
			if(Utility.testString(producerCode)){
				sqlStr.append("   and  taword.producer_code like '%"+producerCode+"%'\n" );
			}
			if(Utility.testString(producerName)){
				sqlStr.append("   and  taword.producer_name like '%"+producerName+"%'\n" );
			}
			//=============
			sqlStr.append("order by  taword.box_no,taword.ID\n");
			System.out.println("sql==="+sqlStr);
			PageResult<Map<String,Object>> pr = this.pageQuery(sqlStr.toString(),null,this.getFunName(), pageSize, curPage); 
			if(pr!=null){
				return pr;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	public PageResult<Map<String,Object>> queryClaimBackDetailList4(Map<String, String> map,int pageSize,int curPage ){

		String claim_id=ClaimTools.dealParamStr(map.get("i_claim_id"));
		String boxNo=ClaimTools.dealParamStr(map.get("boxNo"));
		String bar_code=ClaimTools.dealParamStr(map.get("bar_code"));
		String partName = ClaimTools.dealParamStr(map.get("partName"));
		String partCode = ClaimTools.dealParamStr(map.get("partCode"));
		String sing_num = ClaimTools.dealParamStr(map.get("sing_num"));
		String is_import = ClaimTools.dealParamStr(map.get("is_import"));
		String dealerCode = ClaimTools.dealParamStr(map.get("dealerCode"));
		String dealerName = ClaimTools.dealParamStr(map.get("dealerName"));
		String claim_no = ClaimTools.dealParamStr(map.get("claim_no"));
		String vin = ClaimTools.dealParamStr(map.get("vin"));
		String IS_IN_HOUSE = ClaimTools.dealParamStr(map.get("IS_IN_HOUSE"));
		if(!"".equals(claim_id)){
			StringBuffer sqlStr= new StringBuffer();
			//sqlStr.append("select * from (");
			sqlStr.append(" select taword.id,taword.is_scan,TAWORD.Is_Main_Code,taword.is_in_house,taword.is_out,taword.claim_no,taword.vin,c.id as claim_id,taword.PRODUCER_NAME,taword.producer_code,to_char(c.RO_STARTDATE,'yyyy-mm-dd') as RO_STARTDATE,c.REMARK,del.dealer_code,c.claim_type,\n" );
			sqlStr.append("tawp.part_code,tawp.part_name,nvl(taword.return_amount,0) return_amount,\n" );
			sqlStr.append("nvl(taword.sign_amount,0) sign_amount,c.Is_Invoice,taword.barcode_no,decode( TAWORD.LOCAL_WAR_HOUSE,NULL,b.local_war_house,TAWORD.LOCAL_WAR_HOUSE) LOCAL_WAR_HOUSE,decode( TAWORD.LOCAL_WAR_SHEL,NULL,b.LOCAL_WAR_SHEL,TAWORD.LOCAL_WAR_SHEL) LOCAL_WAR_SHEL,decode( TAWORD.LOCAL_WAR_LAYER,NULL,b.LOCAL_WAR_LAYER,TAWORD.LOCAL_WAR_LAYER) LOCAL_WAR_LAYER,\n" );
			sqlStr.append("taword.box_no,taword.warehouse_region,taword.deduct_remark,decode(taword.deduct_remark,0,'--请选择--',null,'--请选择--',tc.code_desc) deduct_desc,TAWORD.Is_Import\n" );
			sqlStr.append("from tt_as_wr_old_returned tawor , tm_pt_part_base b ,\n" );
			sqlStr.append("tt_as_wr_old_returned_detail taword,\n" );
			sqlStr.append("tt_as_wr_partsitem tawp,tc_code tc,TT_AS_WR_APPLICATION c,tm_dealer del\n" );
			sqlStr.append("where tawor.id=taword.return_id and taword.RETURN_AMOUNT != taword.SIGN_AMOUNT and taword.Executive_director_sta=0  AND taword.part_code = b.part_code(+) AND c.ID = tawp.ID AND c.claim_no = taword.claim_no and taword.part_id=tawp.part_id and taword.deduct_remark=tc.code_id(+)\n" );
			sqlStr.append("and taword.is_sign=0 AND  tawor.id="+claim_id+" and c.id = taword.claim_id and del.dealer_id=c.dealer_id\n" );
			if(Utility.testString(vin)){
				sqlStr.append("   and  taword.vin like '%"+vin.toUpperCase()+"%'\n" );
			}
			if(Utility.testString(IS_IN_HOUSE)){
				sqlStr.append("   and  taword.IS_IN_HOUSE="+IS_IN_HOUSE+"\n" );
			}
			if(!"".equals(boxNo)){
				sqlStr.append("and taword.box_no='"+boxNo+"'\n" );
			}
			if(Utility.testString(partCode)){
				sqlStr.append(" and taword.part_code like'%"+partCode.toUpperCase()+"%'\n" );
			}
			if(Utility.testString(partName)){
				sqlStr.append(" and taword.part_Name like'%"+partName+"%'\n" );
			}
			if(Utility.testString(bar_code)){
				sqlStr.append(" and taword.barcode_no like'%"+bar_code.toUpperCase()+"%'\n" );
			}
			if(Utility.testString(sing_num)){
				sqlStr.append("   and  taword.sign_amount="+sing_num+"\n" );
			}
			if(Utility.testString(is_import)){
				sqlStr.append("   and  taword.is_import="+is_import+"\n" );
			}
			//威旺代码和名称模糊查询条件 2014-6-25
			if(Utility.testString(dealerCode)){
				sqlStr.append("   and  c.WW_DEALER_CODE like  '%"+dealerCode+"%'\n");
			}
			if(Utility.testString(dealerName)){
				sqlStr.append("   and  c.WW_DEALER_NAME like '%"+dealerName+"%'\n" );
			}
			if(Utility.testString(claim_no)){
				sqlStr.append("   and  c.claim_no like '%"+claim_no+"%'\n" );
			}
			//=============
			sqlStr.append("order by  box_no,claim_no,part_name,barcode_no\n");
			System.out.println("sql==="+sqlStr);
			PageResult<Map<String,Object>> pr = this.pageQuery(sqlStr.toString(),null,this.getFunName(), pageSize, curPage); 
			if(pr!=null){
				return pr;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	
	public List<Map<String,Object>> queryClaimBackDetailList22(Map params){
		String claim_id=ClaimTools.dealParamStr(params.get("id"));
		String boxNo=ClaimTools.dealParamStr(params.get("boxNo"));
		if(!"".equals(claim_id)){
			StringBuffer sqlStr= new StringBuffer();
			sqlStr.append("select taword.id,taword.claim_no,taword.vin,c.id as claim_id,taword.PRODUCER_NAME,to_char(c.RO_STARTDATE,'yyyy-mm-dd') as RO_STARTDATE,c.REMARK,del.dealer_code,\n" );
			sqlStr.append("tawp.part_code,tawp.part_name,nvl(sum(taword.return_amount),0) return_amount,\n" );
			sqlStr.append("nvl(sum(taword.sign_amount),0) sign_amount,\n" );
			sqlStr.append("taword.box_no,taword.warehouse_region,taword.deduct_remark,nvl(tc.code_desc,'') deduct_desc\n" );
			sqlStr.append("from tt_as_wr_old_returned tawor,\n" );
			sqlStr.append("tt_as_wr_old_returned_detail taword,\n" );
			sqlStr.append("tt_as_wr_partsitem tawp,tc_code tc,TT_AS_WR_APPLICATION c,tm_dealer del\n" );
			sqlStr.append("where tawor.id=taword.return_id and taword.part_id=tawp.part_id and taword.deduct_remark=tc.code_id(+)\n" );
			sqlStr.append("and tawor.id="+claim_id+" and c.claim_no=taword.claim_no and del.dealer_id=c.dealer_id\n" );
			if(!"".equals(boxNo)){
				sqlStr.append("and taword.box_no='"+boxNo+"'\n" );
			}
			sqlStr.append("group by taword.id,taword.claim_no,taword.vin,c.id,taword.PRODUCER_NAME,c.RO_STARTDATE,c.REMARK,del.dealer_code,\n" );
			sqlStr.append("tawp.part_code,tawp.part_name,tc.code_desc,\n" );
			sqlStr.append("taword.box_no,taword.warehouse_region,taword.deduct_remark,taword.create_date\n");
			sqlStr.append("order by taword.box_no, tawp.part_name asc\n");
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
	
	/*********Iverson add By 2010-11-18 查询出旧件起止时间******************/
	public List<Map<String,Object>> queryClaimBackDetailList3(String claim_id){
		if(!"".equals(claim_id)){
			StringBuffer sqlStr= new StringBuffer();	
			sqlStr.append("SELECT OO.WR_START_DATE||'至'||to_char(oo.return_end_date,'yyyy-mm-dd') WR_START_DATE FROM TT_AS_WR_RETURNED_ORDER OO ,tr_return_logistics l where l.logictis_id='"+claim_id+"' and l.return_id=oo.id\n" );
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
	/**
	 * Function：签收回运单，修改回运明细表
	 * @param  ：	
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-13
	 */
	public int SignBackOldPartOper(Map params){
		String idStr=ClaimTools.dealParamStr(params.get("idStr"));
		String return_ord_id=ClaimTools.dealParamStr(params.get("return_ord_id"));
		String logonId=ClaimTools.dealParamStr(params.get("logonId"));
		String[] strArr=idStr.split(",");
		int updateNum=0;
		for(int count=0;count<strArr.length;count++){
			String signNum=ClaimTools.dealParamStr(params.get("signNum"+strArr[count]));//签收数
			String wrHouseNo=ClaimTools.dealParamStr(params.get("wrHouse"+strArr[count]));//库区
			String deduct=ClaimTools.dealParamStr(params.get("deduct"+strArr[count]));//抵扣原因
			
			TtAsWrOldReturnedDetailPO idObj=new TtAsWrOldReturnedDetailPO();
			idObj.setId(Long.parseLong(strArr[count]));
			
			TtAsWrOldReturnedDetailPO vo=new TtAsWrOldReturnedDetailPO();
			vo.setSignAmount(Integer.parseInt(signNum));
			vo.setWarehouseRegion(wrHouseNo);
			if(!"".equals(deduct)){
				vo.setDeductRemark(Integer.parseInt(deduct));
			}else{
				vo.setDeductRemark(0);
			}
			
			updateNum+=update(idObj,vo);
		}
		/**
		 *根据需求增加部分签收功能 modify by zhaolunda 2010-08-06 
		 */
		TtAsWrOldReturnedPO idObj=new TtAsWrOldReturnedPO();
		idObj.setId(Long.parseLong(return_ord_id));
		
		TtAsWrOldReturnedPO vo=new TtAsWrOldReturnedPO();
		vo.setStatus(Constant.BACK_LIST_STATUS_03);//部分签收
		vo.setUpdateBy(Long.parseLong(logonId));
		vo.setUpdateDate(new Date());
		update(idObj,vo);
		
		return updateNum;
	}
	/**
	 * Function：回运清单--签收完成操作
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-17
	 * 2010-10-13 修改 库存中加入产地限制
	 * 描述：
	 *     1、检测库存中是否存在该配件时，加入产地限制
	 *     2、记录库存记录是加入产地信息
	 */
	public int SignFinishOper(Map params){
		act=ActionContext.getContext();
		logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		TcCodePO tpo = new TcCodePO();
		tpo.setType(Constant.chana+"");
		DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
		TcCodePO tpoValue = (TcCodePO)balanceDao.select(tpo).get(0);
		String id=ClaimTools.dealParamStr(params.get("id"));
		String logonId=ClaimTools.dealParamStr(params.get("logonId"));
		int updateNum=0;
		
		//更改索赔回运清单表
		TtAsWrOldReturnedPO idObj=new TtAsWrOldReturnedPO();
		idObj.setId(Long.parseLong(id));
		TtAsWrOldReturnedPO vo=new TtAsWrOldReturnedPO();
		vo.setInWarhouseDate(new Date());
		vo.setUpdateBy(Long.parseLong(logonId));
		vo.setUpdateDate(new Date());
		updateNum=update(idObj,vo);//主表签收入库时间
		//更新旧件库存表
		StringBuffer sql= new StringBuffer();
		sql.append("update /*+RULE+*/Tt_As_Wr_Part_Stock s\n" );
		sql.append("   set s.return_amount = s.return_amount +\n" );
		sql.append("                         (select /*+RULE+*/sum(nvl(d.sign_amount, 0)) return_amount\n" );
		sql.append("                            from Tt_As_Wr_Old_Returned_Detail d,tm_pt_supplier ps,Tt_As_Wr_Old_Returned ro\n");
		sql.append("                           where d.return_id = ? and  ps.supplier_code = d.producer_code and ro.id = d.return_id\n" );
		sql.append("                             and d.part_code = s.part_code and s.producer_id = ps.supplier_id and s.yieldly = ro.yieldly)\n");
		sql.append("where  exists\n" );
		sql.append("      (select 1/*+RULE+*/\n" );
		sql.append("         from Tt_As_Wr_Old_Returned_Detail d,\n" );
		sql.append("              tm_pt_supplier               ps,\n" );
		sql.append("              Tt_As_Wr_Old_Returned        ro\n" );
		sql.append("        where d.return_id = ?\n" );
		sql.append("          and ps.supplier_code = d.producer_code\n" );
		sql.append("          and ro.id = d.return_id\n" );
		sql.append("          and d.part_code = s.part_code\n" );
		sql.append("          and s.producer_id = ps.supplier_id and s.yieldly = ro.yieldly)");

		List pps = new ArrayList();
		pps.add(id);
		pps.add(id);
		this.update(sql.toString(), pps);

		StringBuffer sql2= new StringBuffer();
		sql2.append("insert into Tt_As_Wr_Part_Stock\n" );
		sql2.append("  select /*+RULE+*/ f_getid,\n" );
		sql2.append("         ps.supplier_id,\n" );
		sql2.append("         pb.part_id,\n" );
		sql2.append("         d.part_code,\n" );
		sql2.append("         d.sign_amount,\n" );
		sql2.append("         0,\n" );
		sql2.append("         sysdate,\n" );
		sql2.append("         '"+logonUserBean.getUserId()+"',\n" );
		sql2.append("         '',\n" );
		sql2.append("         '',\n" );
		if(tpoValue.getCodeId().equals(Constant.chana_wc+"")){
			sql2.append("         '"+Constant.OEM_COM_SVC+"',\n" );
		}else{
			sql2.append("         '"+Constant.OEM_COM_JC+"',\n" );
			
		}
		sql2.append("         ro.yieldly\n" );
		sql2.append("    from Tt_As_Wr_Old_Returned_Detail d,\n" );
		sql2.append("         tm_pt_supplier               ps,\n" );
		sql2.append("         Tt_As_Wr_Old_Returned        ro,tm_pt_part_base pb\n" );
		sql2.append("   where ps.supplier_code = d.producer_code\n" );
		sql2.append("     and ro.id = d.return_id and pb.part_code=d.part_code\n" );
		sql2.append("     and d.return_id = "+id+"\n" );
		sql2.append("and not exists\n" );
		sql2.append("    (select /*+RULE+*/ *\n" );
		sql2.append("       from Tt_As_Wr_Part_Stock s  where ps.supplier_id=s.producer_id and ro.yieldly=s.yieldly and s.part_code=d.part_code\n" );
		sql2.append("      group by s.producer_id, s.yieldly, s.part_code)");
		this.insert(sql2.toString());
		
		return updateNum;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	/**
	 * 查看表中是否存在记录
	 * @param company_id 区分微车轿车
	 * @param partCode 配件代码
	 * @return
	 */
	public PageResult<TtAsWrPartStockPO> getPartStockData(String company_id,String partCode,String yieldly){
		
		if(yieldly==null || partCode==null)
			return null;
		
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select * from TT_AS_WR_PART_STOCK where 1=1\n");
		sqlStr.append(" and part_code='" + CommonUtils.checkNull(partCode)+"'\n");
		sqlStr.append(" and YIELDLY=" + yieldly);
		PageResult<TtAsWrPartStockPO> pr = pageQuery(TtAsWrPartStockPO.class,sqlStr.toString(),null, 10, 1);
		
		if(pr!=null&&pr.getTotalPages()>0){
			return pr;
		}else{
			return null;
		}
	}
	/**
	 * Function：通过供应商code获得供应商id
	 * @param  ：	
	 * @return:		@param supplier_code
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-17
	 */
	public Long getSupplyId(String supplier_code){
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select supplier_id from TM_PT_SUPPLIER where supplier_code='"+supplier_code+"' and is_del=0");
		PageResult<TmPtSupplierPO> pr = pageQuery(TmPtSupplierPO.class,sqlStr.toString(),null, 10, 1);
		if(pr!=null&&pr.getTotalRecords()>0){
			return ((TmPtSupplierPO)pr.getRecords().get(0)).getSupplierId();
		}else{
			return 0l;
		}
	}
	/**
	 * Function：通过索赔配件表的主键id
	 *           获得配件信息表中的配件基本信息
	 * @param  ：	
	 * @return:		@param part_id
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-17
	 */
	public TmPtPartBasePO getPartInfo(String part_id,String company_id){
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select * from TM_PT_PART_BASE where part_code=(");
		sqlStr.append("select part_code from tt_as_wr_partsitem where part_id="+part_id+")and is_del=0 \n");
		PageResult<TmPtPartBasePO> pr = pageQuery(TmPtPartBasePO.class,sqlStr.toString(),null, 10, 1);
		if(pr!=null&&pr.getTotalRecords()>0){
			return (TmPtPartBasePO)pr.getRecords().get(0);
		}else{
			return null;
		}
	}
	/**
	 * Function：检查签收工作是否完成
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-7-24 ZLD
	 */
	public String validateIsSignOper(Map params){
		String idStr=ClaimTools.dealParamStr(params.get("idStr"));//回运明细id
		StringBuffer sqlStr= new StringBuffer();
		
		if("".equals(idStr)){
			return "0";//不符合签收要求
		}
		String[] idArr=idStr.split(",");
		for(int count=0;count<idArr.length;count++){
			TtAsWrOldReturnedDetailPO chekObj=new TtAsWrOldReturnedDetailPO();
			sqlStr.delete(0, sqlStr.length());
			sqlStr.append("select * from tt_as_wr_old_returned_detail\n" );
			sqlStr.append("where id="+idArr[count]);
			List list=select(TtAsWrOldReturnedDetailPO.class, sqlStr.toString(), null);
			if(list!=null&&list.size()>0){
				chekObj=(TtAsWrOldReturnedDetailPO)list.get(0);
				Integer diff_numm=chekObj.getReturnAmount()-chekObj.getSignAmount();
				int dueduct_reason=chekObj.getDeductRemark().intValue();
				if(diff_numm>0&&dueduct_reason>0){//存在差异并且已在数据库中注明抵扣原因
					//return "1";//已完成签收操作
				}else if(diff_numm==0&&dueduct_reason==0){//不存在差异并且抵扣原因为空
					//return "1";//已完成签收操作
			    }else{
					return "0";//未完成
				}
			}else{
				return "0";
			}
		}
		return "1";
	}
	
	
	
	public List<Map<String,Object>> queryReturnOrder(String id){
		if(!"".equals(id)){
			StringBuffer sqlStr= new StringBuffer();	
			sqlStr.append("select u.dealer_id,u.yieldly ,u.wr_start_date,TO_CHAR(U.RETURN_END_DATE,'YYYY-MM-DD')END_DATE ,u.status,u.id from Tt_As_Wr_Returned_Order u  where u.id in(\n" );
			sqlStr.append("select distinct t.dealer_return_id from tt_as_wr_old_returned_detail t where t.return_id in(" );
			sqlStr.append("select w.id from tt_as_wr_old_returned  w where w.id='"+id+"'))" );
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
	
	public List<Map<String,Object>> queryMinDate(String id){
		if(!"".equals(id)){
			StringBuffer sqlStr= new StringBuffer();	
			sqlStr.append("select /*+rule+*/u.yieldly, min(u.wr_start_date) as wr_start_date,u.dealer_id from Tt_As_Wr_Returned_Order u  where u.id in(\n" );
			sqlStr.append("select distinct t.dealer_return_id from tt_as_wr_old_returned_detail t where t.return_id in(" );
			sqlStr.append("select w.id from tt_as_wr_old_returned  w where w.id='"+id+"'))" );
			sqlStr.append("group by u.wr_start_date,u.yieldly,u.dealer_id" );
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
	
	public int queryIsSign(long id){
		int count=0;
		if(!"".equals(id)){
			StringBuffer sqlStr= new StringBuffer();	
			sqlStr.append("select count(*) as count from tt_as_wr_old_returned_detail where is_sign=0 and return_id='"+id+"'\n" );
			List<Map<String,Object>> pr = this.pageQuery(sqlStr.toString(), null, this.getFunName());
			count=((BigDecimal)pr.get(0).get("COUNT")).intValue();
		}
		return count;
	}
	
	//修改tt_as_wr_old_returned_detail的入库状态
	public void updateIsstorage(String claim_id,String box_no){
		StringBuffer sql= new StringBuffer();
		sql.append("update tt_as_wr_old_returned_detail set IS_STORAGE="+Constant.STATUS_ENABLE+" where return_id="+claim_id+" and box_no="+box_no);
		
		factory.update(sql.toString(), null);
	}
	
	//查询入库状态
	public List<Map<String,Object>> queryIsstorage(String claim_id){
		StringBuffer sql= new StringBuffer();
		sql.append("select distinct  a.return_id,a.box_no,a.is_storage  From tt_as_wr_old_returned_detail a where  return_id="+claim_id);
		List<Map<String,Object>> pr = this.pageQuery(sql.toString(), null, this.getFunName());
		if(pr!=null){
			return pr;
		}else{
			return null;
		}
	}
	
	//修改回运单状态为部分签收
	public void updatePartReceipt(String claim_id){
		StringBuffer sql= new StringBuffer();
		sql.append("update tt_as_wr_old_returned set STATUS="+Constant.BACK_LIST_STATUS_03+" where ID="+claim_id);
		
		factory.update(sql.toString(), null);
	}
	
	//修改回运单状态为完全签收
	public void updateReceipt(String claim_id,Long userId,String price){
		StringBuffer sql= new StringBuffer();
		sql.append("update tt_as_wr_old_returned set STATUS="+Constant.BACK_LIST_STATUS_04+",in_warhouse_date=sysdate,Auth_Price="+price+",update_by="+userId+",update_date=sysdate where ID="+claim_id);
		
		factory.update(sql.toString(), null);
	}
	
	//更新tt_as_dealer_type的旧件截至时间
	public void updateReviewDate(String claim_id,String START_DATE){
		StringBuffer sql= new StringBuffer();
		sql.append("update tt_as_dealer_type set OLD_REVIEW_DATE =to_date('"+START_DATE+"','YYYY-MM-DD') where DEALER_ID=(SELECT DEALER_ID FROM tt_as_wr_old_returned WHERE ID="+claim_id+") and YIELDLY=(SELECT YIELDLY FROM tt_as_wr_old_returned WHERE ID="+claim_id+") ");
		
		factory.update(sql.toString(), null);
	}
	//加入库存信息
	
	public void insertStock(TtAsWrBarcodePartStockPO tawbp){
		
    	String Id = SequenceManager.getSequence("");
    	tawbp.setId(Long.valueOf(Id));
    	this.insert(tawbp);
		
		
	}
	
	//根据id查询回运单明细
	public List<Map<String,Object>> queryOldDetail(String claim_id,String box_no){
		StringBuffer sql= new StringBuffer();
		sql.append("select td1.yieldly, td.part_id,td.part_name,td.part_code,td.deductible_reason_code,td.barcode_no From tt_as_wr_old_returned_detail td,tt_as_wr_old_returned td1 where td.return_id=td1.id and td.is_storage is null  and td.return_id="+claim_id+" and td.box_no="+box_no+" and td.is_upload="+Constant.STATUS_ENABLE);
		List<Map<String,Object>> pr = this.pageQuery(sql.toString(), null, this.getFunName());
		System.out.println("1:"+pr.size());
		if(pr!=null){
			return pr;
		}else{
			return null;
		}
	}
	
	
	
	
	//根据id查询回运单明细
	public List<Map<String,Object>> queryDetail(String claim_id){
		StringBuffer sql= new StringBuffer();
		sql.append("select td1.yieldly, td.part_id,td.part_name,td.part_code,td.deductible_reason_code,td.barcode_no From tt_as_wr_old_returned_detail td,tt_as_wr_old_returned td1 where td.return_id=td1.id  and td.is_storage is null and td.return_id="+claim_id+"  and td.is_upload="+Constant.STATUS_ENABLE);
		List<Map<String,Object>> pr = this.pageQuery(sql.toString(), null, this.getFunName());
		if(pr!=null){
			return pr;
		}else{
			return null;
		}
	}
	
	
	//查询所有没有抵扣原因的件
	public List<Map<String,Object>> queryAllNotDeduct(String claim_id){
		StringBuffer sql= new StringBuffer();
		sql.append("select  * From tt_as_wr_old_returned_detail a where a.deduct_remark is null  and a.return_id="+claim_id);
		List<Map<String,Object>> pr = this.pageQuery(sql.toString(), null, this.getFunName());
		if(pr!=null){
			return pr;
		}else{
			return null;
		}
	}
	
	
	//查询这箱没有抵扣原因的件
	public List<Map<String,Object>> queryNotDeduct(String claim_id,String box_no){
		StringBuffer sql= new StringBuffer();
		sql.append("select  * From tt_as_wr_old_returned_detail a where a.deduct_remark is null  and a.return_id="+claim_id+" and a.box_no="+box_no);
		List<Map<String,Object>> pr = this.pageQuery(sql.toString(), null, this.getFunName());
		if(pr!=null){
			return pr;
		}else{
			return null;
		}
	}
	
	//超期审核为正常
	public void updateOverdue(String claim_id){
		StringBuffer sql= new StringBuffer();
			sql.append("update tt_as_wr_old_returned set is_Overduecheck=10011002,is_Overduecheckd=1 where id="+claim_id);
		
		factory.update(sql.toString(), null);
		
		String sql1=" update tt_as_wr_old_returned_detail  b set  b.is_upload=10011001,b.sign_amount=1,b.deduct_remark='',b.deductible_reason_code='' where b.return_id="+claim_id +" and deduct_remark="+Constant.OLDPART_DEDUCT_TYPE_10;
		factory.update(sql1.toString(), null);
	}
	
	//超期审核为正常
	public void updateOverdueCQ(String claim_id){
		StringBuffer sql= new StringBuffer();
			sql.append("update tt_as_wr_old_returned set is_Overduecheck=10011002,is_Overduecheckd=1 where id="+claim_id);
		
		factory.update(sql.toString(), null);
	
	}
	//易损易耗件维护
	public PageResult<TmPtPartBasePO> getAllPart(String code,String name, String no,String type,int pageSize, int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append(" select B.PART_ID ,B.PART_CODE,B.PART_NAME,B.PART_NO,B.IS_CLIAM\n ");
		sql.append("  from TM_PT_PART_BASE B\n");
		sql.append(" WHERE B.IS_DEL=0\n");
		if(Utility.testString(code)){
			sql.append(" and B.PART_CODE Like '%"+code.toUpperCase()+"%'\n");
		}
		if(Utility.testString(name)){
			sql.append(" and B.PART_NAME LIKE '%"+name+"%'\n");
		}
		if(Utility.testString(no)){
			sql.append(" and B.PART_NO = '"+no+"'\n");
		}
		if(Utility.testString(type)){
			sql.append(" and B.IS_CLIAM ="+type+"\n");
		}
		sql.append(" order by b.part_id ");
		return pageQuery(TmPtPartBasePO.class, sql.toString(), null,
				pageSize, curPage);
	}
	//配件索赔属性维护
	public PageResult<TmPtPartBasePO> getAllParts(String code,String name,int userType, String no,String type,int pageSize, int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append(" select B.PART_ID ,B.PART_CODE,B.PART_NAME,B.PART_NO,B.IS_CLIAM\n ");
		sql.append("  from TM_PT_PART_BASE B\n");
		sql.append(" WHERE B.IS_DEL=0 ");
		if(Utility.testString(code)){
			sql.append(" and B.PART_CODE Like '%"+code.toUpperCase()+"%'\n");
		}
		if(Utility.testString(name)){
			sql.append(" and B.PART_NAME LIKE '%"+name+"%'\n");
		}
		if(Utility.testString(no)){
			sql.append(" and B.PART_NO = '"+no+"'\n");
		}
		if(Utility.testString(type)){
			sql.append(" and B.IS_CLIAM ="+type+"\n");
		}
		if(userType==Constant.POSE_BUS_TYPE_WRD){
			sql.append(" and B.part_is_changhe ="+Constant.PART_IS_CHANGHE_02+"\n");
		}else{
			sql.append(" and B.part_is_changhe ="+Constant.PART_IS_CHANGHE_01+"\n");
		}
		sql.append("and b.part_code not in ('CV6000-00000','CV8000-00000','00-0000','00-000','CV11000-00000')"); 
		sql.append(" order by b.part_id ");
		return pageQuery(TmPtPartBasePO.class, sql.toString(), null,
				pageSize, curPage);
	}
	//配件回运维护
	public PageResult<TmPtPartBasePO> getAllReturn(String code,String name,int userType, String no,String type,int pageSize, int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append(" select B.PART_ID ,B.PART_CODE,B.PART_NAME,B.PART_NO,B.IS_RETURN\n ");
		sql.append("  from TM_PT_PART_BASE B\n");
		sql.append(" WHERE B.IS_DEL=0 \n");
		if(Utility.testString(code)){
			sql.append(" and B.PART_CODE Like '%"+code.toUpperCase()+"%'\n");
		}
		if(Utility.testString(name)){
			sql.append(" and B.PART_NAME LIKE '%"+name+"%'\n");
		}
		if(Utility.testString(no)){
			sql.append(" and B.PART_NO = '"+no+"'\n");
		}
		if(Utility.testString(type)){
			sql.append(" and B.IS_RETURN ="+type+"\n");
		}
		if(userType==Constant.POSE_BUS_TYPE_WRD){
			sql.append(" and B.part_is_changhe ="+Constant.PART_IS_CHANGHE_02+"\n");
		}else{
			sql.append(" and B.part_is_changhe ="+Constant.PART_IS_CHANGHE_01+"\n");
		}
		sql.append("and b.part_code not in ('CV6000-00000','CV8000-00000','00-0000','00-000','CV11000-00000')"); //过滤掉常量配件
		sql.append(" order by b.part_id ");
		return pageQuery(TmPtPartBasePO.class, sql.toString(), null,
				pageSize, curPage);
	}
	//查询物流旧件是否签收完成
	public int sureCount(String id){
			StringBuffer sql= new StringBuffer();
			sql.append("select * from Tt_As_Wr_Old_Returned_Detail  d\n");
			sql.append("where d.return_id="+id);
			sql.append("and d.sign_amount=0\n");
			sql.append("and (d.deduct_remark is null or d.deduct_remark=0 )"); 

			List<TtAsWrOldPartSignDetailListBean> pr = this.select(TtAsWrOldPartSignDetailListBean.class,sql.toString(),null);
			if(pr!=null){
				return pr.size();
			}else{
				return 0;
			}
	}
	public boolean checkWarHouse(String house){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT *\n");
		sql.append("FROM tt_as_create_old d\n");
		sql.append("WHERE d.code_old='"+house.substring(0,1).toUpperCase()+"'\n");
		List<Map<String,Object>> pr = this.pageQuery(sql.toString(), null, this.getFunName());
		
		StringBuffer sql2= new StringBuffer();
		sql2.append("SELECT *\n");
		sql2.append("FROM tt_as_create_old d\n");
		sql2.append("WHERE d.code_old='"+house.substring(1,4)+"'\n");
		List<Map<String,Object>> pr2 = this.pageQuery(sql2.toString(), null, this.getFunName());
		
		StringBuffer sql3= new StringBuffer();
		sql3.append("SELECT *\n");
		sql3.append("FROM tt_as_create_old d\n");
		sql3.append("WHERE d.code_old='"+house.substring(4,5)+"'\n");
		List<Map<String,Object>> pr3 = this.pageQuery(sql3.toString(), null, this.getFunName());
		
		if(pr.size()==0 ||pr2.size()==0||pr3.size()==0){
			return false;
		}else{
			return true;
		}
	}
	public List<Map<String,Object>> queryOtherLabour(Long claimId ,Long partId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT p.down_product_code ,MAX(p.wr_labourcode) wr_labourcode\n");
		sql.append("FROM Tt_As_Wr_Partsitem p  WHERE p.ID="+claimId+"\n");
		sql.append("AND p.wr_labourcode=(SELECT t.wr_labourcode FROM Tt_As_Wr_Partsitem t WHERE t.part_id="+partId+")\n");
		sql.append("GROUP BY p.down_product_code"); 

		List<Map<String,Object>> pr = this.pageQuery(sql.toString(), null, this.getFunName());
		if(pr!=null){
			return pr;
		}else{
			return null;
		}
	}
	
	public List<Map<String,String>> queryDeductLabour(Long claimId ,Long partId){
		 StringBuffer laSql = new StringBuffer();
		 laSql.append("SELECT l.labour_id,l.wr_labourcode,l.wr_labourname,l.labour_amount FROM  Tt_As_Wr_Labouritem l\n");
		 laSql.append("WHERE l.wr_labourcode IN (\n");
		 
		 
		 laSql.append("SELECT  p.wr_labourcode FROM Tt_As_Wr_Partsitem p\n");
		 laSql.append("WHERE 1=1  AND p.ID="+claimId+" AND p.part_id = "+partId+"  union all \n");
		 

		 laSql.append(" SELECT A.Wr_Labourcode FROM Tt_As_Wr_Partsitem p,Tt_As_Wr_Partsitem A \n");
		 laSql.append(" WHERE 1=1  AND p.ID="+claimId+"  and a.id ="+claimId+" \n");
		 laSql.append(" and p.part_id ="+partId+"  and p.down_part_code = a.main_part_code \n");
		 
		 
		 
		
		 
		 
		 
		 
		 laSql.append(") AND l.ID = "+claimId+""); 
		 List<Map<String,String>> pr = this.pageQuery(laSql.toString(), null, getFunName());
		if(pr!=null){
			return pr;
		}else{
			return null;
		}
	}
	public List<Map<String, Object>> oldPartSignAuditIn(String id) {
		String sql="SELECT  T.CLAIM_NO,T.PART_CODE FROM TT_AS_WR_OLD_RETURNED_DETAIL T  WHERE T.SIGN_AMOUNT=0  and t.BARCODE_NO='"+id+"' AND  (T.DEDUCT_REMARK=0 OR T.DEDUCT_REMARK IS NULL)";
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	public void oldPartSignAuditInUpdate(String id) {
		String upSql = "UPDATE tt_as_wr_old_returned_detail a SET a.is_in_house="+Constant.IF_TYPE_YES+",a.In_Date =sysdate WHERE (a.is_in_house="+Constant.IF_TYPE_NO+" OR a.is_in_house IS NULL) and a.RETURN_AMOUNT = a.SIGN_AMOUNT  and  a.ID='"+id+"'";
		dao.update(upSql, null);
		String upSql1 = "UPDATE tt_as_wr_old_returned_detail a SET a.is_in_house="+Constant.IF_TYPE_YES+",a.In_Date =sysdate WHERE (a.is_in_house="+Constant.IF_TYPE_NO+" OR a.is_in_house IS NULL) and a.RETURN_AMOUNT != a.SIGN_AMOUNT and a.Executive_director_sta =1 and a.ID='"+id+"'";
		dao.update(upSql1, null);
	}
	public List<Map<String, Object>> oldPartSignAuditIn1(String returnOrdId) {
		String sql="SELECT  T.BARCODE_NO,T.PART_CODE FROM TT_AS_WR_OLD_RETURNED_DETAIL T  WHERE 1=1 AND (T.SIGN_AMOUNT=0   AND  (T.DEDUCT_REMARK=0 OR T.DEDUCT_REMARK IS NULL)OR t.is_in_house = "+Constant.IF_TYPE_NO+" )and t.return_id="+returnOrdId+"";
		List<Map<String,Object>> ps=this.pageQuery(sql.toString(), null, this.getFunName());
		return ps;
	}
	//查询该件是否最后一个件,最后一个件则后续逻辑需要更新整个单据为已经审核
	public List<Map<String, Object>> oldPartSignAuditIn2(String returnOrdId,String claimNo,Long partId) {
		String sql="SELECT  T.BARCODE_NO,T.PART_CODE FROM TT_AS_WR_OLD_RETURNED_DETAIL T  WHERE 1=1 AND (T.SIGN_AMOUNT=0   AND  (T.DEDUCT_REMARK=0 OR T.DEDUCT_REMARK IS NULL)OR t.is_in_house = "+Constant.IF_TYPE_NO+" ) and t.claim_no<>'"+claimNo+"' and t.part_id<>"+partId+" and t.return_id="+returnOrdId+"";
		List<Map<String,Object>> ps=this.pageQuery(sql.toString(), null, this.getFunName());
		return ps;
	}
	/**
	 * 通用的查询序列
	 * @param seq
	 * @return
	 */
	public List<Map<String, Object>> getSignNo(String seq) {
		String sql="select "+seq+".NEXTVAL as num From DUAL";
		List<Map<String,Object>> ps=this.pageQuery(sql.toString(), null, this.getFunName());
		return ps;
	}
	public List<Map<String,Object>> approveAndStoredSure221(String returnOrdId) {
		String sql="SELECT  T.BARCODE_NO,T.PART_CODE FROM TT_AS_WR_OLD_RETURNED_DETAIL T  WHERE T.SIGN_AMOUNT=0  and t.return_id="+returnOrdId+" AND  (T.DEDUCT_REMARK=0 OR T.DEDUCT_REMARK IS NULL)";
		List<Map<String,Object>> ps=this.pageQuery(sql.toString(), null, this.getFunName());
		return ps;
	}
	/**
	 * 这一步更新索赔单结算数据 zyw 2014-8-18
	 * @param return_ord_id
	 */
	public int updateApplicationSal(String return_ord_id) {
		int res=0;
		try {
			StringBuffer sb= new StringBuffer();
			sb.append("select d.claim_no\n" );
			sb.append("  from tt_as_wr_old_returned_detail d\n" );
			sb.append(" where d.return_id in\n" );
			sb.append("       (select r.id from tt_as_wr_old_returned r where r.id = '"+return_ord_id+"')");
			List<String> selectTmDataSet = dao.selectTmDataSet(sb,"CLAIM_NO");
			for (String claim_no : selectTmDataSet) {
				StringBuffer sbClaim = UpdateApplicationSalSql(claim_no);
				dao.update(sbClaim.toString(), null);
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}
		return res;
	}
	/**
	 * 更新索赔单数据的sql zyw 2014-8-18
	 * @param claim_no
	 * @return
	 */
	private StringBuffer UpdateApplicationSalSql(String claim_no) {
		StringBuffer sbClaim= new StringBuffer();
		sbClaim.append("update Tt_As_Wr_Application t\n" );
		sbClaim.append("   set t.BALANCE_AMOUNT        = t.BALANCE_AMOUNT -\n" );
		sbClaim.append("                                 (nvl((SELECT sum(nvl(c.AMOUNT, 0) -\n" );
		sbClaim.append("                                                 nvl(c.apply_amount, 0))\n" );
		sbClaim.append("                                        from TT_AS_WR_PARTSITEM c\n" );
		sbClaim.append("                                       where c.ID = t.ID\n" );
		sbClaim.append("                                       GROUP by c.ID),\n" );
		sbClaim.append("                                      0) +\n" );
		sbClaim.append("                                 nvl((SELECT sum(nvl(f.labour_amount, 0) -\n" );
		sbClaim.append("                                                 nvl(F.Apply_Amount, 0))\n" );
		sbClaim.append("                                        from TT_AS_WR_LABOURITEM f\n" );
		sbClaim.append("                                       where f.ID = t.ID\n" );
		sbClaim.append("                                       group by f.ID),\n" );
		sbClaim.append("                                      0)),\n" );
		sbClaim.append("       t.BALANCE_PART_AMOUNT   = t.BALANCE_PART_AMOUNT -\n" );
		sbClaim.append("                                 nvl((SELECT sum(nvl(c.AMOUNT, 0) -\n" );
		sbClaim.append("                                                nvl(c.apply_amount, 0))\n" );
		sbClaim.append("                                       from TT_AS_WR_PARTSITEM c\n" );
		sbClaim.append("                                      where c.ID = t.ID\n" );
		sbClaim.append("                                      GROUP by c.ID),\n" );
		sbClaim.append("                                     0),\n" );
		sbClaim.append("       t.BALANCE_LABOUR_AMOUNT = t.BALANCE_LABOUR_AMOUNT -\n" );
		sbClaim.append("                                 nvl((SELECT sum(nvl(f.labour_amount, 0) -\n" );
		sbClaim.append("                                                nvl(F.apply_amount, 0))\n" );
		sbClaim.append("                                       from TT_AS_WR_LABOURITEM f\n" );
		sbClaim.append("                                      where f.ID = t.ID\n" );
		sbClaim.append("                                      group by f.ID),\n" );
		sbClaim.append("                                     0)\n" );
		sbClaim.append(" where t.claim_no = '"+claim_no+"'");
		return sbClaim;
	}
//	public List queryReturnType() {
//		StringBuffer sql=new StringBuffer();
//		sql.append("select * from tc_code t  where  t.type='1177' and status='10011001'");
//		List list=dao.selectTmDataSet(sql, null);
//		return list;
//	}
	public void updateDetailQHJData(String return_ord_id, AclUserBean loginUser) {
		StringBuffer sb= new StringBuffer();
		sb.append("update Tt_As_Wr_Old_Returned_Detail d\n" );
		sb.append("   set d.qhj_flag  = 1,\n" );
		sb.append("       d.kcdb_flag = 1,\n" );
		sb.append("       d.qhj_in_date = sysdate,\n" );
		sb.append("       d.qhj_in_by   = "+loginUser.getUserId()+"\n" );
		sb.append(" where 1=1 and d.is_in_house=10041001 and  d.claim_id in\n" );
		sb.append("       (select a.id\n" );
		sb.append("          from tt_as_wr_application a\n" );
		sb.append("         where 1 = 1\n" );
		sb.append("           and a.campaign_code in\n" );
		sb.append("               (select ac.activity_code\n" );
		sb.append("                  from Tt_As_Activity ac\n" );
		sb.append("                 where ac.activity_type = 10561005)\n" );
		sb.append("           and a.claim_type = 10661006 \n" );
		sb.append("           and a.id in (SELECT d.claim_id\n" );
		sb.append("                          FROM Tt_As_Wr_Old_Returned_Detail d\n" );
		sb.append("                         WHERE d.return_id = "+return_ord_id+"))");
		this.update(sb.toString(), null);
	}
	/**
	 * 修改旧件供应商修改备注
	 * @param request
	 * @param loginUser
	 */
	public void updateoldPartSupply(RequestWrapper request,AclUserBean loginUser) {
		String[] PartRemarks=  DaoFactory.getParams(request, "PartRemark"); //旧件修改备注
		System.out.println(PartRemarks.length);
		String remarkids=DaoFactory.getParam(request, "myarr");  //旧件明细id
		String[] ids = remarkids.split(",");
		TtAsWrOldReturnedDetailPO detailPO =  new TtAsWrOldReturnedDetailPO();
		TtAsWrOldReturnedDetailPO detailPO1 =  new TtAsWrOldReturnedDetailPO();
		for (int i = 0; i < ids.length; i++) {
			if (null!=PartRemarks[i]) {
				detailPO.setSupplierRemark(PartRemarks[i]);//供应商代码备注
				detailPO1.setId(Long.valueOf(ids[i]));
				this.update(detailPO1, detailPO);
			}
		}
	}
	
	public void oldPartDeduction(Long claimId,String Id,String labourId,String labourCode,String labourName){
		
		String deductionNo=SequenceManager.getSequence("DO");//生成单号
		StringBuffer sql= new StringBuffer();
		sql.append(" INSERT INTO TT_AS_WR_OLDPART_DEDUCTION A\n");
		sql.append("   (A.ID,\n");
		sql.append("    A.RETURNED_ID,\n");
		sql.append("    A.DEDUCTION_NO,\n");
		sql.append("    A.STATUS,\n");
		sql.append("    A.LABOUR_PRICE,\n");
		sql.append("    A.PART_PRICE,\n");
		sql.append("    A.OTHER_PRICE,\n");
		sql.append("    A.TOTAL_PRICE,\n");
		sql.append("    A.INVOICE_NO,\n");
		sql.append("    A.CREATE_DATE,\n");
		sql.append("    A.labour_Id,\n");
		sql.append("    A.labour_code,\n");
		sql.append("    A.labour_name,\n");
		sql.append("    A.part_id,\n");
		sql.append("    A.part_code,\n");
		sql.append("    A.part_name,\n");
		sql.append("    A.DEALER_ID)\n");
		sql.append("   (SELECT F_GETID,\n");
		sql.append("           "+Id+",\n");
		sql.append("           '"+deductionNo+"',\n");
		sql.append("           "+Constant.DEDUCTION_STATUS_1+",\n");
		sql.append("           T.LABOUR_AMOUNT - T.BALANCE_LABOUR_AMOUNT AS LOBOUR_AMOUNT,\n");
		sql.append("           T.PART_AMOUNT - T.BALANCE_PART_AMOUNT AS PART_AMOUNT,\n");
		sql.append("           T.NETITEM_AMOUNT - T.BALANCE_NETITEM_AMOUNT AS NETITEM_AMOUNT,\n");
		sql.append("           T.REPAIR_TOTAL - T.BALANCE_AMOUNT AS TOTAL_PRICE,\n");
		sql.append("           '1111111',\n");
		sql.append("           SYSDATE,\n");
		sql.append("           "+labourId+",\n");
		sql.append("           '"+labourCode+"',\n");
		sql.append("           '"+labourName+"',\n");
		sql.append("           t1.part_id,\n");
		sql.append("           t1.part_code,\n");
		sql.append("           t1.part_name,\n");
		sql.append("           T.DEALER_ID\n");
		sql.append("      FROM TT_AS_WR_APPLICATION T,Tt_As_Wr_Old_Returned_Detail t1\n");
		sql.append("		WHERE t.id=t1.claim_id\n");
		sql.append("     and  T1.ID = "+Id+")");
		
		//默认审核通过
		StringBuffer sqt= new StringBuffer();
		sqt.append(" UPDATE TT_AS_WR_OLD_RETURNED_DETAIL T\n");
		sqt.append("      SET T.EXECUTIVE_DIRECTOR_STA = 1, T.EXECUTIVE_DIRECTOR_DATE = sysdate,T.is_in_house=10041002\n");
		sqt.append("    WHERE T.ID = "+Id+"");
		dao.update(sqt.toString(), null);
		
		dao.insert(sql.toString());
	}
	
	public void oldPartDeductionDelete(String Id) {

		StringBuffer sql = new StringBuffer();
		StringBuffer sqt = new StringBuffer();
		sql.append(" DELETE FROM TT_AS_WR_OLDPART_DEDUCTION T WHERE T.RETURNED_ID = "+Id+"");

		dao.delete(sql.toString(), null);
		
		sqt.append(" UPDATE TT_AS_WR_OLD_RETURNED_DETAIL T\n");
		sqt.append("      SET T.EXECUTIVE_DIRECTOR_STA = 0, T.EXECUTIVE_DIRECTOR_DATE = sysdate,T.is_in_house=10041001\n");
		sqt.append("    WHERE T.ID = "+Id+"");
		dao.update(sqt.toString(), null);
	}
	
	public void updateInHouse(String Id){
		StringBuffer sqt= new StringBuffer();
		sqt.append(" UPDATE TT_AS_WR_OLD_RETURNED_DETAIL T\n");
		sqt.append("      SET T.EXECUTIVE_DIRECTOR_STA = 1, T.EXECUTIVE_DIRECTOR_DATE = sysdate,T.is_in_house=10041001\n");
		sqt.append("    WHERE T.return_id = "+Id+"");
		dao.update(sqt.toString(), null);
	}
	
	public PageResult<Map<String, Object>> producerInfoQuery(
			Map<String, Object> params, Integer pageSize, Integer curPage)
			throws Exception {
		String partId = CommonUtils.checkNull(params.get("partId"));
		String venderCode = CommonUtils.checkNull(params.get("venderCode"));
		String venderName = CommonUtils.checkNull(params.get("venderName"));
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT DISTINCT B.VENDER_ID VENDER_ID,\n") ;
		sql.append("       B.VENDER_CODE,\n") ;
		sql.append("       B.VENDER_NAME\n") ;
		sql.append("  FROM TT_PART_VENDER A\n") ;
		sql.append("  JOIN TT_PART_VENDER_DEFINE B\n") ;
		sql.append("    ON A.VENDER_ID = B.VENDER_ID\n") ;
		sql.append(" WHERE 1=1\n") ;
		
		if(!venderCode.equals("")){
			sql.append(" AND B.VENDER_CODE LIKE '%"+venderCode+"%'\n") ;
		}
		if(!venderName.equals("")){
			sql.append(" AND B.VENDER_NAME = '%"+venderName+"%'\n") ;
		}
		if(!partId.equals("")){
			sql.append(" AND A.PART_ID = "+partId+"\n") ;
		}else{
			sql.append(" AND 1=2\n") ;
		}
		
        System.out.println("sql:"+sql);
		
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
	}
	
	public List<Map<String, Object>> validateDeductRemark(Map<String, Object> params)
			throws Exception {
		String id = CommonUtils.checkNull(params.get("id"));
		String claimId = CommonUtils.checkNull(params.get("claimId"));
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT A.ID\n") ;
		sql.append("  FROM TT_AS_WR_RETURNED_ORDER_DETAIL A\n") ;
		sql.append(" WHERE 1=1\n") ;
		sql.append("   AND A.DEDUCT_REMARK = "+Constant.OLDPART_DEDUCT_TYPE_23+"\n") ;
		sql.append("   AND A.OTHER_REMARK IS NULL\n") ;
		sql.append("   AND ROWNUM <= 1\n") ;
		
		if(!id.equals("")){
			sql.append("   AND A.ID IN ("+id+")\n") ;
		}
		if(!claimId.equals("")){
			sql.append("   AND A.RETURN_ID = "+claimId+"\n") ;
		}
        System.out.println("sql:"+sql);
		
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
}
