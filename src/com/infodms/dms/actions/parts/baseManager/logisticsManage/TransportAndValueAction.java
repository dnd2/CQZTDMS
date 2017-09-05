package com.infodms.dms.actions.parts.baseManager.logisticsManage;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.parts.baseManager.logisticsManage.TransportAndValueDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesLogiAreaPO;
import com.infodms.dms.po.TtSalesLogiPO;
import com.infodms.dms.po.TtTransportValuationPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 运输方式与计价维护
 * @author hyy 
 * @version 2017-7-6
 * @see 
 * @since 
 */
public class TransportAndValueAction {
	private TransportAndValueDao transValuationDao = TransportAndValueDao.getInstance();
	
	public Logger logger = Logger.getLogger(TransportAndValueAction.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final String transportAndValueInitUrl = "/jsp/parts/baseManager/logisticsManage/transportAndValue.jsp";
	private final String transportAndValueAddUrl = "/jsp/parts/baseManager/logisticsManage/tansportAndValueAdd.jsp";
	private final String transportAndValueEditUrl = "/jsp/parts/baseManager/logisticsManage/transportAndValueUpd.jsp";

	
	public void transportAndValueInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(transportAndValueInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运输方式与计价维护初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询运输方式与计价方式
	 */
	public void transportAndValueQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String transportCode = act.getRequest().getParamValue("transportMode");//运费方式 
			String valuationCode = act.getRequest().getParamValue("valuationMode");//计价方式 

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("TRANSPORT_CODE", transportCode);
			map.put("VALUATION_CODE", valuationCode);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = transValuationDao.selTtransportValua(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "运输方式与计价维护查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 新增运输方式与计价方式初始化
	 */
	public void transportAndValueAddInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String tvId= "A"+DaoFactory.getPkId();

			act.setOutData("tvId",tvId);
			act.setForword(transportAndValueAddUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运输方式与计价维护初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增运输方式与计价方式
	 */
	public void transportAndValueAdd(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String tvId = act.getRequest().getParamValue("tvId");
			String tvName = act.getRequest().getParamValue("tvName");
			String transportCode = act.getRequest().getParamValue("transportMode");//运费方式 
			String valuationCode = act.getRequest().getParamValue("valuationMode");//计价方式 
			Integer status = Integer.parseInt(act.getRequest().getParamValue("isStatus")+"");//是否有效：有效、无效
			//查询运输方式和计价方式组合是否存在
			List<TtTransportValuationPO> list=transValuationDao.selTtransportValuaByCode(transportCode,valuationCode,null);
			//存在提示已存在，不存在则添加
			if(list.size()==0){
				TtTransportValuationPO po=new TtTransportValuationPO();
				po.setTvId(tvId);
				po.setTvName(tvName);
				po.setTransportCode(transportCode);
				po.setValuationCode(valuationCode);
				po.setStatus(status);
				po.setCreateDate(new Date());
				po.setCreateBy(logonUser.getUserId());
				transValuationDao.transportValueAdd(po);
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,"运输方式与计价维护新增错误");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 修改初始化
	 * @param tv_id 运输方式和计划关系表id
	 */
	public void transportAndValueUpdInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String tv_id= act.getRequest().getParamValue("tvId");
			Map<String, Object> map=transValuationDao.selTtransportValuaByTvId(tv_id);
			act.setOutData("transportValueMap", map);
			act.setForword(transportAndValueEditUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运输方式与计价维护初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void transportAndValueUpd() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String tvId = act.getRequest().getParamValue("tvId");
			String tvName = act.getRequest().getParamValue("tvName");
			String transportCode = act.getRequest().getParamValue("transportMode");//运费方式 
			String valuationCode = act.getRequest().getParamValue("valuationMode");//计价方式 
			Integer status = Integer.parseInt(act.getRequest().getParamValue("isStatus")+"");//是否有效：有效、无效
			
			TtTransportValuationPO po=new TtTransportValuationPO();
			po.setTvId(tvId);
			po.setTvName(tvName);
//			po.setTransportCode(transportCode);
//			po.setValuationCode(valuationCode);
			po.setStatus(status);
			po.setUpdateBy(logonUser.getUserId());//修改人
			po.setUpdateDate(new Date());//修改时间
			//查询运输方式和计价方式组合是否存在
			List<TtTransportValuationPO> list=transValuationDao.selTtransportValuaByCode(transportCode,valuationCode,tvId);
			//存在提示已存在，不存在则修改
			if(list.size()==0){
				transValuationDao.transportValueUpdate(po);
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"运输方式和计价方式信息保存失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 失效
	 * @param Id
	 */
//	public void updStatusDisable() {
//		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
//		try{
//			String tvId = act.getRequest().getParamValue("Id");//序号
//			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
//			transValuationDao.updStatusDisable(tvId);
//			if ("".equals(curPage)) {
//				curPage = "1";
//			}
//			act.setOutData("success", "失效成功");
//			act.setOutData("curPage", curPage);
//		}catch(Exception e) {//异常方法
//			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"运输方式和计价方式信息设置失败");
//			logger.error(logonUser,e1);
//			act.setException(e1);
//		}
//	}
//	/**
//	 * 有效
//	 * @param Id
//	 */
//	public void updStatusEnable() {
//		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
//		try{
//			String tvId = act.getRequest().getParamValue("Id");//序号
//			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
//			transValuationDao.updStatusEnable(tvId);
//			if ("".equals(curPage)) {
//				curPage = "1";
//			}
//			act.setOutData("success", "设置成功");
//			act.setOutData("curPage", curPage);
//		}catch(Exception e) {//异常方法
//			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"运输方式和计价方式信息设置失败");
//			logger.error(logonUser,e1);
//			act.setException(e1);
//		}
//	}
}
