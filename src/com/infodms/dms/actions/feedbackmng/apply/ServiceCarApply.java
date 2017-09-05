package com.infodms.dms.actions.feedbackmng.apply;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.feedbackmng.InfoFeedBackMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.SeriesBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.common.SeriesShowDao;
import com.infodms.dms.dao.feedbackMng.ServiceCarApplyDAO;
import com.infodms.dms.dao.feedbackMng.ServiceInfoApplyDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtIfServicecarExtPO;
import com.infodms.dms.po.TtIfServicecarPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class ServiceCarApply {


	public Logger logger = Logger.getLogger(ServiceCarApply.class);
	private final String serviceCarApplyURL = "/jsp/feedbackMng/apply/serviceCarApply.jsp";//主页面
	private final String newServiceCarApplyURL = "/jsp/feedbackMng/apply/newServiceCarApply.jsp";//新增页面
	private final String modifyServiceCarApplyURL = "/jsp/feedbackMng/apply/modifyServiceCarApply.jsp";//修改页面
	private final String detailServiceCarApplyURL = "/jsp/feedbackMng/apply/serviceCarApplyDetail.jsp";
	private ServiceCarApplyDAO dao = ServiceCarApplyDAO.getInstance();
	/**
	 * 
	* @Title: servicecarapplyforward 
	* @Description: TODO(跳转到服务车申请表首页) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void servicecarapplyforward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId());
			act.setForword(serviceCarApplyURL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车申请表首页");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: newServiceCarApplyforward 
	* @Description: TODO(跳转到新增页面) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void newServiceCarApplyforward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			//modify by xiayanpeng begin 新增时自动带出经销商联系人，经销商电话，经销商传真
			// 从Session中获取dealerId
			String dealerId = String.valueOf(logonUser.getDealerId());
			ServiceInfoApplyDao infoDao = new ServiceInfoApplyDao();
			Map<String,Object>  map = infoDao.getDealerInfo(dealerId);
			act.setOutData("map", map);
			//modify by xiayanpeng end 
			act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId());
			
			SeriesShowDao dao2 = SeriesShowDao.getInstance();
			//List<SeriesBean> seriesList = dao2.querySeries() ;
			//act.setOutData("series", seriesList);
			List<TmVhclMaterialGroupPO> list = dao2.getMaterialGroup();
			act.setOutData("list", list);
			act.setForword(newServiceCarApplyURL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车申请表新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: servicecarapplyQuery 
	* @Description: TODO(经销商端查询) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void servicecarapplyQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String dealerId = logonUser.getDealerId();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			StringBuffer con = new StringBuffer();
			String queryType = request.getParamValue("queryType");
			String orderId = request.getParamValue("ORDER_ID");
			String strDate = request.getParamValue("CON_APPLY_DATE_START");
			String endDate = request.getParamValue("CON_APPLY_DATE_END");
			String modelId = request.getParamValue("MODEL_ID");
			// 如果是提报页面的查询queryType=submit则只查询待上报
			if (null!=queryType&&!"".equals(queryType)&&("submit").equals(queryType)) {
				con.append(" and (t.app_status='"+Constant.SERVICE_APPLY_ACTIVE_STATUS_UNREPORT+"' ");
				con.append(" or t.app_status='"+Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_REJECT+"' ");
				con.append(" or t.app_status='"+Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_REJECT+"' ");
				con.append(" or t.app_status='"+Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_REJECT+"') ");
			}
			//工单号like
			if (orderId!=null&&!"".equals(orderId)) {
				con.append(" and ORDER_ID like'%"+orderId+"%' "); //
			}
			//介于开始时间
			if (strDate!=null&&!"".equals(strDate)) {
				con.append(" and t.CREATE_DATE >= to_date('"+strDate+" 00:00:00"+"', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			//结束时间
			if (endDate!=null&&!"".equals(endDate)) {
				con.append(" and t.CREATE_DATE <= to_date('"+endDate+" 23:59:59"+"', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			//经销商代码
			if (dealerId!=null&&!"".equals(dealerId)) {
				con.append(" and t.DEALER_ID='"+dealerId+"' ");
			}
			//车型
			if (modelId!=null&&!"".equals(modelId)&&!("-1").equals(modelId)) {
				con.append(" and t.GROUP_ID='"+modelId+"' ");
				System.out.println(modelId+"++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			}
			PageResult<TtIfServicecarExtPO> list = dao.applyQuery(con.toString(),null,curPage,Constant.PAGE_SIZE);
			//List ls = list.getRecords();
			act.setOutData("ps", list);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务车申请表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: servicecarapplyAdd 
	* @Description: TODO(新增) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void servicecarapplyAdd() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
		Long userId = logonUser.getUserId();
		String dealerId = logonUser.getDealerId();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			
					String orderId = request.getParamValue("ORDER_ID");
					String linkMan = request.getParamValue("LINK_MAN");//联系人姓名
					String tel = request.getParamValue("TEL");//单位电话
					Long modelId = new Long(request.getParamValue("MODEL_ID"));//申请车型
					String fax = request.getParamValue("FAX");//传真 
					String content = request.getParamValue("CONTENT");//申请内容
					Double saleAmount =request.getParamValue("SALE_AMOUNT")==null?null:Double.parseDouble(request.getParamValue("SALE_AMOUNT")); 
					String status = request.getParamValue("STATUS");//状态（备注）
					TtIfServicecarPO tisp = new TtIfServicecarPO();
					tisp.setOrderId(SequenceManager.getSequence("FWAO"));
					tisp.setLinkMan(linkMan); //单位联系人
					tisp.setTel(tel); //单位电话
					tisp.setGroupId(modelId); //申请车型
					tisp.setFax(fax); //单位传真
					tisp.setContent(content); //申请内容
					tisp.setSaleAmount(saleAmount); //市场价格
					tisp.setCreateDate(new Date()); //创建日期
					tisp.setCreateBy(userId); //创建人
					tisp.setCreateDate(new Date());
					tisp.setStatus(status);
					tisp.setCompanyId(companyId);
					tisp.setAppStatus(Constant.SERVICE_APPLY_ACTIVE_STATUS_UNREPORT); //申请状态为待提交
					tisp.setDealerId(Utility.getLong(dealerId));
					//modify by xiayanpeng begin 加入IS_DEL  
					tisp.setIsDel(new Integer(Constant.IS_DEL_00));
					//modify by xiayapeng end
					dao.addRecord(tisp);
					servicecarapplyforward();
					//act.setForword(servicecarapplyforward.do");
			
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"服务车申请表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: servicecarapplySubmit 
	* @Description: TODO(上报) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void servicecarapplySubmit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String orderIds = request.getParamValue("orderIds");//要上报的工单号，以,隔开
			if (orderIds!=null&&!"".equals(orderIds)) {
				String [] orderIdArray = orderIds.split(","); //取得所有orderId放在数组中
				dao.submit(orderIdArray,logonUser);
			}
			act.setOutData("returnValue", 1);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"服务车申请表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: servicecarapplySubmit 
	* @Description: TODO(上报) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void rebackSubmit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");//要上报的工单号，以,隔开
			TtAsWrApplicationPO applicationPO = new TtAsWrApplicationPO();
			applicationPO.setId(Long.parseLong(id));
			List<TtAsWrApplicationPO> list= dao.select(applicationPO);
			applicationPO = list.get(0);
			String bug = "";
			if("10791003".equals(""+applicationPO.getStatus()))
			{
				TtAsWrApplicationPO applicationPO1 = new TtAsWrApplicationPO();
				TtAsWrApplicationPO applicationPO2 = new TtAsWrApplicationPO();
				applicationPO1.setId(Long.parseLong(id));
				applicationPO2.setStatus(10791001);
				dao.update(applicationPO1, applicationPO2);
			    bug = "撤销成功";
			}else
			{
				bug = "车厂已经审核不能撤销上报";
			}
			 act.setOutData("bug", bug);
			
			
			act.setOutData("returnValue", 1);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"服务车申请表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 
	* @Title: servicecarapplyDelete 
	* @Description: TODO(删除单子) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void servicecarapplyDelete() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String orderIds = request.getParamValue("orderIds");//要上报的工单号，以,隔开
			if (orderIds!=null&&!"".equals(orderIds)) {
				String [] orderIdArray = orderIds.split(","); //取得所有orderId放在数组中
				dao.deleteRecord(orderIdArray);
			}
			act.setOutData("returnValue", 1);
			
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"服务车申请表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 转化
	 */
	@SuppressWarnings("unchecked")
	public void servicecarapplyChange() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String ids = request.getParamValue("ids");//ids
			String type = request.getParamValue("type");//type
			if (ids!=null&&!"".equals(type)) {
				String [] idss = ids.split(","); 
				StringBuffer sb=new StringBuffer();
				sb.append("UPDATE TM_VEHICLE T ");
				if("1".equals(type)){
					sb.append(" SET T.IS_PDI=1 ");
				}
				if("3".equals(type)){
					sb.append(" SET T.IS_PDI=0 ");
				}
				if("2".equals(type)){
					sb.append(" SET T.IS_DOMESTIC=1 ");
				}
				if("4".equals(type)){
					sb.append(" SET T.IS_DOMESTIC=0 ");
				}
				String sql="";
				for (String id : idss) {
					sql = sb.toString();
					sql+=" WHERE T.VIN='"+id+"'";
					dao.update(sql, null);
				}
				
			}
			act.setOutData("returnValue", 1);
			
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"服务车申请表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: servicecarapplyUpdatePre 
	* @Description: TODO(修改申请表前置操作) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void servicecarapplyUpdatePre() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String orderId = request.getParamValue("ORDER_ID");//取得要修改的工单号
			TtIfServicecarPO tisp = dao.queryByOrderId(orderId);
			request.setAttribute("servicecarBean", tisp);
			
			TmVhclMaterialGroupPO mpo = new TmVhclMaterialGroupPO();
			mpo.setGroupId(tisp.getGroupId());
			mpo = dao.getMaterialByGID(mpo);
			
			TmVhclMaterialPO vpo = new TmVhclMaterialPO();
			vpo.setMaterialId(tisp.getGroupId());
			vpo = (TmVhclMaterialPO)dao.select(vpo).get(0);
			
			act.setOutData("vpo", vpo);
			act.setOutData("mpo", mpo);
			//act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId());
			//act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId2(mpo.getParentGroupId()));
			SeriesShowDao dao2 = SeriesShowDao.getInstance();
			List<TmVhclMaterialGroupPO> list = dao2.getMaterialGroup();
			act.setOutData("list", list);
			List<SeriesBean> series2 = dao2.getSeries(vpo.getMaterialId());
			if(series2.size()>0)
				act.setOutData("s", series2.get(0));
			//act.setOutData("series", seriesList);
			act.setForword(modifyServiceCarApplyURL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车申请表修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: servicecarapplyUpdate 
	* @Description: TODO(服务车申请单修改) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void servicecarapplyUpdate() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long userId = logonUser.getUserId();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String orderId = request.getParamValue("ORDER_ID");
			String linkMan = request.getParamValue("LINK_MAN");//联系人姓名
			String tel = request.getParamValue("TEL");//单位电话
			Long modelId = null;
			if(null!=request.getParamValue("MODEL_ID")){
			 modelId = new Long(request.getParamValue("MODEL_ID"));//申请车型
			}
			String fax = request.getParamValue("FAX");//传真 
			String content = request.getParamValue("CONTENT");//申请内容
			double saleAmount =Double.parseDouble(request.getParamValue("SALE_AMOUNT")); 
			String status = request.getParamValue("STATUS");//状态（备注）
			TtIfServicecarPO tisp = new TtIfServicecarPO();
			tisp.setOrderId(orderId);
			tisp.setLinkMan(linkMan);
			tisp.setTel(tel);
			tisp.setGroupId(modelId);
			tisp.setFax(fax);
			tisp.setContent(content);
			tisp.setSaleAmount(saleAmount);
			tisp.setUpdateBy(userId);
			tisp.setUpdateDate(new Date());
			tisp.setStatus(status);
			dao.updateRecord(orderId,tisp);
			request.setAttribute("orderId", orderId);
			servicecarapplyforward();
			//tisp.setStatus(status);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"服务车申请表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: servicecarapplyDetail 
	* @Description: TODO(通过点击工单号，查询申请表明细) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void servicecarapplyDetail() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String orderId = request.getParamValue("ORDER_ID");//取得要修改的工单号
			TtIfServicecarExtPO tisp = dao.queryDetailByOrderId(orderId);
			request.setAttribute("servicecarBean", tisp);
			List<TtIfServicecarExtPO> ls = dao.queryAuditDetailByOrderId(orderId);
			request.setAttribute("auditDetails", ls);
			act.setOutData("flag", request.getParamValue("flag"));
			act.setForword(detailServiceCarApplyURL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"服务车申请表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

}
