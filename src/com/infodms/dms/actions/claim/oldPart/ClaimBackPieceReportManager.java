package com.infodms.dms.actions.claim.oldPart;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsWrBackListQryBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.claim.oldPart.ClaimReturnReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TtAsWrOldReturnedDetailPO;
import com.infodms.dms.po.TtAsWrOldReturnedPO;
import com.infodms.dms.po.TtAsWrReturnAuthitemPO;
import com.infodms.dms.po.TtAsWrReturnedOrderPO;
import com.infodms.yxdms.entity.ysq.TtAsComRecordPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 类说明：索赔旧件管理--回运清单上报
 * 作者：  赵伦达
 */
public class ClaimBackPieceReportManager {
	public Logger logger = Logger.getLogger(ClaimBackPieceBackListOrdManager.class);
	private ActionContext act = null;
	private AclUserBean logonUserBean = null;
	private RequestWrapper request=null;
	private ClaimReturnReportDao dao=null;
	
	//url导向
	private final String queryBackListOrdUrl = "/jsp/claim/oldPart/queryReturnReportList.jsp";
	private final String queryBackListPrint = "/jsp/claim/oldPart/queryReturnPrint.jsp";
	private final String queryBackListPrint2 = "/jsp/claim/oldPart/queryReturnPrint2.jsp";
	private final String oldPartSignQueryDetailPrint = "/jsp/claim/oldPart/oldPartSignQueryDetailPrint.jsp";//签收明细打印
	/**
	 * Function：索赔件回运清单上报--初始化
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-2 赵伦达
	 */
	public void queryListPage(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(queryBackListOrdUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单上报--初始化");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔件回运清单上报--按条件查询
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-2 赵伦达
	 */
	public void queryBackListByCondition(){
		act=ActionContext.getContext();
		request = act.getRequest();
		logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
		dao=ClaimReturnReportDao.getInstance();
		//查询条件
		String back_order_no = request.getParamValue("back_order_no");//回运清单号
		String freight_type = request.getParamValue("freight_type");//货运方式
		String create_start_date = request.getParamValue("create_start_date");//建单开始时间
		String create_end_date = request.getParamValue("create_end_date");//建单结束时间
		// 处理当前页
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage")) : 1;
		// 查询语句
		StringBuffer sqlStr = new StringBuffer();
		StringBuffer whereStr = new StringBuffer();
		StringBuffer orderByStr = new StringBuffer();
		try{
			sqlStr.append("select tawor.id,tawor.return_no,tawor.create_date,tawor.return_date,(select count(*) from tt_as_wr_old_returned_detail d where d.return_id=tawor.id and d.box_no is null) as box_number,tawor.tran_no,\n");
			sqlStr.append("tawor.wr_amount,tawor.part_item_amount,tawor.part_amount,tawor.parkage_amount,\n");
			sqlStr.append("tc.code_desc status_desc,tawor.status,tce.code_desc freight_type\n");
			sqlStr.append("from TT_AS_WR_OLD_RETURNED tawor,tc_code tc,tc_code tce\n");
			sqlStr.append("where tawor.status=tc.code_id and tawor.transport_type=tce.code_id\n");
			if(back_order_no!=null&&!"".equals(back_order_no))
				whereStr.append(" and tawor.return_no like '%"+back_order_no+"%'\n");
			if(freight_type!=null&&!"".equals(freight_type))
				whereStr.append(" and tawor.transport_type="+freight_type+"\n");
			if (create_start_date != null && !"".equals(create_start_date))
				whereStr.append(" and tawor.create_date>=to_date('" + create_start_date+ "','YYYY-MM-DD')\n");
			if (create_end_date != null && !"".equals(create_end_date))
				whereStr.append(" and tawor.create_date<=to_date('" + create_end_date+ "','YYYY-MM-DD')\n");
			whereStr.append(" and tawor.dealer_id="+logonUserBean.getDealerId()+" ");
			whereStr.append(" and tawor.status="+Constant.BACK_LIST_STATUS_01+"\n");
			orderByStr.append(" order by tawor.create_date desc\n");
			System.out.println("sql=="+orderByStr);
			PageResult<TtAsWrBackListQryBean> ps = dao.queryClaimBackList(sqlStr.toString()+ whereStr.toString() + orderByStr.toString(), curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(queryBackListOrdUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单上报--按条件查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔件回运清单上报--上报功能
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-10
	 */
	public void reportClaimInfo(){
		act=ActionContext.getContext();
		request = act.getRequest();
		logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		dao=ClaimReturnReportDao.getInstance();
		String claimId=request.getParamValue("report_id");//索赔物流单id
		try{
			TtAsWrReturnedOrderPO updateObj=new TtAsWrReturnedOrderPO();
			updateObj.setReturnDate(new Date());
			updateObj.setStatus(Constant.BACK_LIST_STATUS_02);
			updateObj.setUpdateBy(logonUserBean.getUserId());
			updateObj.setUpdateDate(new Date());
			dao.updateClaimBackOrdMainInfo(claimId, updateObj);
			act.setForword(queryBackListOrdUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "索赔件回运清单上报--上报功能");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-04-15
	public void reportClaimInfo11(){
		act=ActionContext.getContext();
		request = act.getRequest();
		logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		dao=ClaimReturnReportDao.getInstance();
		String claimId=request.getParamValue("report_id");//索赔物流单id
		try{
			StringBuffer sql = new StringBuffer();
			sql.append("update tt_as_wr_returned_order_detail a set a.box_no = 999 where a.return_id = '"+claimId+"' and a.box_no is null");
			dao.update(sql.toString(), null) ;
			TtAsWrReturnedOrderPO updateObj=new TtAsWrReturnedOrderPO();
			updateObj.setReturnDate(new Date());
			updateObj.setStatus(Constant.BACK_LIST_STATUS_02);
			updateObj.setUpdateBy(logonUserBean.getUserId());
			updateObj.setUpdateDate(new Date());
			int count = dao.updateClaimBackOrdMainInfo(claimId, updateObj);
			if(count>0){
				//添加上报记录表(1表示强制上报)
				ReturnStatusRecord.recordStatus1(Long.parseLong(claimId), logonUserBean.getUserId(), logonUserBean.getName(),logonUserBean.getOrgId(), Constant.BACK_LIST_STATUS_02,"1");
			}
			//act.setForword(queryBackListOrdUrl);
			ClaimOldPartApporoveStorageManager aa = new ClaimOldPartApporoveStorageManager();
			aa.queryListPage11();
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "索赔件回运清单上报--上报功能");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-04-15
	@SuppressWarnings("unchecked")
	public void reportClaimInfo22(){
		act=ActionContext.getContext();
		request = act.getRequest();
		logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		dao=ClaimReturnReportDao.getInstance();
		String claimId=request.getParamValue("report_id");//索赔物流单id
		try{
			TtAsWrReturnedOrderPO updateObj=new TtAsWrReturnedOrderPO();
			updateObj.setReturnDate(new Date());
			updateObj.setStatus(Constant.BACK_LIST_STATUS_02);
			updateObj.setUpdateBy(logonUserBean.getUserId());
			updateObj.setUpdateDate(new Date());
			int count = dao.updateClaimBackOrdMainInfo(claimId, updateObj);
			//========================================================================2015-7-16增加日志记录操作人
			TtAsComRecordPO asComRecordPO = new TtAsComRecordPO();
			asComRecordPO.setId(DaoFactory.getPkId());
			asComRecordPO.setBizId(Long.valueOf(claimId));
			asComRecordPO.setCreateBy(logonUserBean.getUserId());
			asComRecordPO.setCreateDate(new Date());
			TcUserPO po = new TcUserPO();
			po.setUserId(logonUserBean.getUserId());
			List<TcUserPO> po2 =dao.select(po);
			String name = po2.get(0).getName();
			asComRecordPO.setRemark("上报操作人:"+name);
			//========================================================================2015-7-16增加日志记录操作人
			if(count>0){
				//添加上报记录表(0表示正常上报)
				TtAsWrReturnAuthitemPO reA = new TtAsWrReturnAuthitemPO();
				reA.setReturnId(Long.parseLong(claimId));
				reA.setAuthStatus(Constant.BACK_LIST_STATUS_02);
				List list = dao.select(reA);
				if(list.size()>0){
					System.out.println("有上报记录!");
				}else{
					ReturnStatusRecord.recordStatus1(Long.parseLong(claimId), logonUserBean.getUserId(), logonUserBean.getName(),logonUserBean.getOrgId(), Constant.BACK_LIST_STATUS_02,"0");
				}
			}
//			ClaimBackPieceBackListOrdManager aa = new ClaimBackPieceBackListOrdManager();
//			aa.returnOrder();
			act.setOutData("SUCCESS", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "索赔件回运清单上报--上报功能");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔件回运清单上报--检查货运单是否填写
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-8-6
	 */
	public void checkTransNo(){
		act=ActionContext.getContext();
		request = act.getRequest();
		logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		dao=ClaimReturnReportDao.getInstance();
		String claimId=request.getParamValue("report_id");//索赔物流单id
		try{
            String flag=dao.checkReturnOrdIsFillTransNo(claimId);
            /**
    		 * 增加装箱总数以及实际装箱数的判断
    		 * @author KFQ
    		 */
            String str = "true";
    		TtAsWrOldReturnedPO rp = new TtAsWrOldReturnedPO();
    		rp.setId(Long.valueOf(claimId));
    		List<TtAsWrOldReturnedPO> list = dao.select(rp);
    		int amount = list.get(0).getParkageAmount();
    		rp = list.get(0);
    		String note ="";
    		List<TtAsWrOldReturnedDetailPO> list2 = dao.getPackageAmount(Long.valueOf(claimId));
    		if(amount!=list2.size()){
    			str = "false"; 
    		}
    		if(rp.getTel()==null){
    			note+="请补录三包员联系方式!\n";
    		}
    		if(rp.getTransportNo()==null){
    			note+="请补录发运单号!\n";
    		}
    		if(rp.getTransportType()==null){
    			note+="请补录物流货运方式!\n";
    		}
    		if(rp.getSendTime()==null){
    			note+="请补录发运时间!\n";
    		}
    		act.setOutData("price", rp.getPrice()==null?0:rp.getPrice());
    		act.setOutData("note", note);
    		act.setOutData("package", str);
            act.setOutData("report_id", claimId);
            act.setOutData("flag", flag);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "索赔件回运清单上报--检查货运单是否填写");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	
	
	/**
	 * Iverson update 2010-11-23
	 * Function：索赔件回运清单--打印
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-9-25
	 */
	public void roMainPrint(){
		try {
			act=ActionContext.getContext();
			request = act.getRequest();
			dao=ClaimReturnReportDao.getInstance();
			String id = request.getParamValue("id");//获取回运单ID 
			String boxNo = request.getParamValue("boxNo");//装箱单号
			
			List<Map<String, Object>>  list = dao.getReturnNo(id);//获取回运通知单的主表信息
//			List<Map<String, Object>>  list1 = dao.getReturnNo1(id);
			List<Map<String, Object>>  listDetail = dao.getReturnNoDetail(id,boxNo);//获取回运通知单的主表信息
			
			act.setOutData("list", list.get(0));
//			act.setOutData("list1", list1);
			act.setOutData("detailList", listDetail);
			
			act.setOutData("boxNo", boxNo);
			
			act.setForword(queryBackListPrint);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//签收明细打印
	public void oldPartSignQueryDetailPrint(){
		try {
			act=ActionContext.getContext();
			request = act.getRequest();
			dao=ClaimReturnReportDao.getInstance();
			String id = request.getParamValue("id");//获取回运单ID 
			
			List<Map<String, Object>>  list = dao.getReturnNo(id);//获取回运通知单的主表信息
			List<Map<String, Object>>  list1 = dao.getReturnNo1(id);
			act.setOutData("list", list.get(0));
			act.setOutData("list1", list1);
			act.setForword(oldPartSignQueryDetailPrint);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Iverson update 2013-6-17 09:17
	 * Function：索赔件回运清单--打印
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 */
	public void roMainPrint2(){
		try {
			act=ActionContext.getContext();
			request = act.getRequest();
			dao=ClaimReturnReportDao.getInstance();
			String id = request.getParamValue("id");//获取回运单ID 
			String boxNo = request.getParamValue("boxNo");//装箱单号
			
			List<Map<String, Object>>  list = dao.getReturnNo(id);//获取回运通知单的主表信息
			List<Map<String, Object>>  list1 = dao.getReturnNo1(id);
			List<Map<String, Object>>  listDetail = dao.getReturnNoDetail(id,boxNo);//获取回运通知单的主表信息
			
			act.setOutData("list", list.get(0));
			act.setOutData("list1", list1);
			act.setOutData("detailList", listDetail);
			
			act.setOutData("boxNo", boxNo);
			
			act.setForword(queryBackListPrint2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
