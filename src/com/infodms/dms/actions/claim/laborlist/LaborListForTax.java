package com.infodms.dms.actions.claim.laborlist;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.claim.laborList.LaborListForTaxDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtAsWrClaimBalancePO;
import com.infodms.dms.po.TtTaxableServiceDetailPO;
import com.infodms.dms.po.TtTaxableServicePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 应税劳务清单
 * @author Administrator
 *
 */
public class LaborListForTax {
	private ActionContext act = null ;
	private RequestWrapper req = null ;
	private AclUserBean user = null ;
	private Logger logger = Logger.getLogger(LaborListForTax.class);
	private LaborListForTaxDao dao = LaborListForTaxDao.getInstanct() ;
	
	private final String MAIN_URL = "/jsp/claim/laborlist/laborlistForTax.jsp" ;
	private final String NOTICE_SEL_URL = "/jsp/claim/laborlist/showNotice.jsp" ;//开票通知单选择页面
	private final String NOTICE_CHANGE_URL = "/jsp/claim/laborlist/showNoticeChange.jsp" ;//开票通知单选择页面
	private final String TAXABLE_SERVIDE_URL = "/jsp/claim/laborlist/taxableServiceDetail.jsp" ;//应税劳务清单明细
	private final String TAXABLE_SERVIDE_ERROR_URL = "/jsp/claim/laborlist/taxableServiceError.jsp" ;
	/*
	 * 应税劳务清单主页面初始化
	 */
	public void mainUrlInit(){
		act = ActionContext.getContext();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(MAIN_URL);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单汇总报表");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	/*
	 * 开票通知单弹出框初始化
	 */
	public void noticeUrlInit(){
		act = ActionContext.getContext();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(NOTICE_SEL_URL);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单汇总报表");
			logger.error(user, be);
			act.setException(be);
		}
	}
	/*
	 * 开票单位变更初始化
	 */
	public void noticeChangeUrlInit(){
		act = ActionContext.getContext();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(NOTICE_CHANGE_URL);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单汇总报表");
			logger.error(user, be);
			act.setException(be);
		}
	}
	/*
	 * 应税劳务清单主页面主查询
	 */
	public void mainQuery(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			String ro_no = req.getParamValue("ro_no");//开票通知单号
			String yieldly = req.getParamValue("yieldly");//购买方
			String dealerName = req.getParamValue("saleName");//经销商名称
			String tax = req.getParamValue("tax");//税率
			String number = req.getParamValue("number");//号码
			String invoiceCode = req.getParamValue("invoiceCode");//发票号码
			String beginDate = req.getParamValue("beginDate");//维修开始时间
			String endDate = req.getParamValue("endDate");//维修结束时间
			
			StringBuffer con = new StringBuffer();
			if(StringUtil.notNull(ro_no))
				con.append("  and s.balance_no like '%").append(ro_no).append("%'\n");
			if(StringUtil.notNull(yieldly))
				con.append("  and s.purchaser_id = ").append(yieldly).append("\n");
			if(StringUtil.notNull(dealerName))
				con.append("  and d.dealer_name like '%").append(dealerName).append("%'\n");
			if(StringUtil.notNull(tax))
				con.append("  and s.tax_rate = ").append(tax).append("\n");
			if(StringUtil.notNull(number))
				con.append("  and s.no like '%").append(number).append("%'\n");
			if(StringUtil.notNull(invoiceCode))
				con.append("  and s.invoice_no like '%").append(invoiceCode).append("%'\n");
			if(StringUtil.notNull(beginDate))
				con.append("  and s.claim_end_date>=to_date('").append(beginDate).append(" 00:00:00','YYYY-MM-DD hh24:mi:ss')\n");
			if(StringUtil.notNull(endDate))
				con.append("  and s.claim_start_date<=to_date('").append(endDate).append(" 23:59:59','YYYY-MM-DD hh24:mi:ss')\n");
			con.append("  and s.dlr_id = ").append(user.getDealerId()).append("\n");
			
			int pageSize = 10;
			Integer curPage = req.getParamValue("curPage") !=null ? Integer.parseInt(req.getParamValue("curPage")) : 1;
			PageResult<Map<String,Object>> ps = dao.mainQuery(con.toString(), pageSize, curPage);
			act.setOutData("ps",ps);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "应税劳务清单");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	/*
	 * 开票通知单查询方法
	 */
	public void noticeQuery(){
		act = ActionContext.getContext();
		req = act.getRequest();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			String ro_no = req.getParamValue("ro_no");
			String dealerCode = req.getParamValue("dealerCode");
			String dealerName = req.getParamValue("dealerName");
			StringBuffer con = new StringBuffer();
			if(StringUtil.notNull(ro_no))
				con.append(" and b.balance_no like '%").append(ro_no).append("%'\n");
			if(StringUtil.notNull(dealerCode))
				con.append(" and c.dealer_code like '%").append(dealerCode).append("%'\n");
			if(StringUtil.notNull(dealerName))
				con.append(" and c.dealer_name like '%").append(dealerName).append("%'\n");
			//con.append(" and b.status=").append(Constant.ACC_STATUS_05).append("\n");
			
			//con.append(" and b.dealer_id=").append(user.getDealerId()).append("\n");
			con.append(" and b.kp_dealer_id=").append(user.getDealerId()).append("\n");
			
			int pageSize = 10;
			int curPage = req.getParamValue("curPage")!=null?Integer.parseInt(req.getParamValue("curPage")):1;
			PageResult<Map<String,Object>> ps = dao.getNotice(con.toString(), pageSize, curPage);
			act.setOutData("ps", ps);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单汇总报表");
			logger.error(user, be);
			act.setException(be);
		}
	}

	
	public void noticeChangeQuery(){
		act = ActionContext.getContext();
		req = act.getRequest();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			String ro_no = req.getParamValue("ro_no");
			String dealerCode = req.getParamValue("dealerCode");
			String dealerName = req.getParamValue("dealerName");
			StringBuffer con = new StringBuffer();
			if(StringUtil.notNull(ro_no))
				con.append(" and b.balance_no like '%").append(ro_no).append("%'\n");
			if(StringUtil.notNull(dealerCode))
				con.append(" and c.dealer_code like '%").append(dealerCode).append("%'\n");
			if(StringUtil.notNull(dealerName))
				con.append(" and c.dealer_name like '%").append(dealerName).append("%'\n");
			con.append("and b.status="+Constant.ACC_STATUS_04+"");
			//con.append(" and b.status=").append(Constant.ACC_STATUS_05).append("\n");
			
			con.append(" and b.dealer_id=").append(user.getDealerId()).append("\n");
			
			int pageSize = 10;
			int curPage = req.getParamValue("curPage")!=null?Integer.parseInt(req.getParamValue("curPage")):1;
			PageResult<Map<String,Object>> ps = dao.getNoticeChange(con.toString(), pageSize, curPage);
			act.setOutData("ps", ps);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单汇总报表");
			logger.error(user, be);
			act.setException(be);
		}
	}
	/*
	 * 针对生成按钮的操作
	 */
	public void getTaxableService(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ;
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER) ;
		try{
			String id = req.getParamValue("id"); //开票通知单ID
			//String saleId = req.getParamValue("saleId"); //销售方ID
			//String yieldly = req.getParamValue("yieldly"); //购买方ID
			String no = req.getParamValue("no"); //号码
			String invoice_no = req.getParamValue("no2"); //所属增值税专用发票代码
			String tax_rate = req.getParamValue("tax"); //税率
			Boolean falg = true;
			TtTaxableServicePO tspo = new TtTaxableServicePO();
			tspo.setBalanceId(Long.parseLong(id));
			List<TtTaxableServicePO> list = dao.select(tspo);
			if(list.size()==0){
				TtAsWrClaimBalancePO po = new TtAsWrClaimBalancePO();
				po.setId(Long.parseLong(id));
				List<TtAsWrClaimBalancePO> cbList = dao.select(po);
				if(cbList.size()>0)
					po = cbList.get(0);
				
				//调用存储过程时.输入参数
				List<Object> ins = new LinkedList<Object>();
				ins.add(po.getId());
				ins.add(po.getYieldly());
				ins.add(po.getKpDealerId());
				ins.add(invoice_no);
				ins.add(no);
				ins.add(Double.parseDouble(tax_rate));
				ins.add(user.getUserId());
				ins.add(user.getUserId());
				System.out.println(po.getId()+","+po.getYieldly()+","+po.getDealerId()+","+invoice_no+","+no+","+tax_rate+","+user.getUserId()+","+user.getUserId());
				TcCodePO code = new TcCodePO() ;
				code.setType("8008") ;
				List listCode = dao.select(code) ;
				if(listCode.size()>0){
					code = (TcCodePO)listCode.get(0);
					act.setOutData("code", code.getCodeId()) ;
					if(Constant.chana_jc==Integer.parseInt(code.getCodeId())){
						dao.callProcedure("p_statistics_checklist", ins, null);
					 }
					else{
						
						dao.callProcedure("p_statistics_checklist_wc", ins, null);
					}
				}
				
				List<Map<String,Object>> taxList = dao.getTaxGoodsAmount(id);
				if(Integer.valueOf(taxList.get(0).get("COU").toString())>0){
					falg=false;
					//dao.delDetail(id);//删除TT_TAXABLE_SERVICE_DETAIL表的相应数据
					//dao.delMain(id);//删除TT_TAXABLE_SERVICE表相应的数据
				}
			}
			list = dao.select(tspo);	
			if(list.size()>0){
				tspo = list.get(0);
				TtAsWrClaimBalancePO bal=new TtAsWrClaimBalancePO();
				bal.setId(Long.parseLong(id));
				bal=(TtAsWrClaimBalancePO)dao.select(bal).get(0);
				TmDealerPO dealer=new TmDealerPO();
				dealer.setDealerId(bal.getKpDealerId());
				dealer=(TmDealerPO)dao.select(dealer).get(0);
				tspo.setSalesName(dealer.getDealerName());
				//TtTaxableServiceDetailPO p2 = new TtTaxableServiceDetailPO();
				//p2.setTaxableServiceId(tspo.getTaxableServiceId());
				//List<TtTaxableServiceDetailPO> details = dao.select(p2);
				List<TtTaxableServiceDetailPO> details = dao.getDetail(tspo.getTaxableServiceId()) ;
				act.setOutData("list", details);
				act.setOutData("listSize", details.size());//明细表总条数，页面显示控制
				
				act.setOutData("tspo",tspo);
			}
			
				if(falg){
				act.setForword(TAXABLE_SERVIDE_URL);
				}else{
					
					act.setForword(TAXABLE_SERVIDE_ERROR_URL);
				}
			
			
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "应税劳务清单查看操作");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	/*
	 * 针对删除按钮的操作
	 */
	public void deleteTaxService(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ;
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER) ;
		try{
			String id = req.getParamValue("id"); //开票通知单ID
			
			dao.delDetail(id);//删除TT_TAXABLE_SERVICE_DETAIL表的相应数据
			dao.delMain(id);//删除TT_TAXABLE_SERVICE表相应的数据
			
			act.setOutData("json", true);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.DELETE_FAILURE_CODE, "应税劳务清单");
			logger.error(user, be);
			act.setException(be);
		}
	}
}
