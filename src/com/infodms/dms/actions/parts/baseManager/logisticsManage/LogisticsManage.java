package com.infodms.dms.actions.parts.baseManager.logisticsManage;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.parts.baseManager.logisticsManage.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesLogiAreaPO;
import com.infodms.dms.po.TtSalesLogiPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * <p>ClassName: LogisticsManage</p>
 * <p>Description: 物流商维护</p>
 * <p>Author: MEpaper</p>
 * <p>Date: 2017年8月17日</p>
 */
public class LogisticsManage {

	public Logger logger = Logger.getLogger(LogisticsManage.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final LogisticsDao reDao = LogisticsDao.getInstance();
	private final String logisticsInitUrl = "/jsp/parts/baseManager/logisticsManage/logisticsList.jsp";
	private final String addLogisticsUrl = "/jsp/parts/baseManager/logisticsManage/addLogistics.jsp";
	private final String editLogisticsUrl = "/jsp/parts/baseManager/logisticsManage/updateLogistics.jsp";
//	private final String logiDealerRelationUrl = "/jsp/sales/storage/storagebase/logistics/logiDealerRelation.jsp";
//	private final String logiDealerRelationSetUrl = "/jsp/sales/storage/storagebase/logistics/logiDealerRelationSet.jsp";
//	private final String logiDealerRelationSetedUrl = "/jsp/sales/storage/storagebase/logistics/logiDealerRelationSeted.jsp";
					
	/**
	 * 物流商初始化
	 */
	public void logisticsInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			act.setForword(logisticsInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物流商管理初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询物流商
	 */
	public void logisticsQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String logiCode = request.getParamValue("LOGI_CODE"); // 物流商代码
			String logiFullName = request.getParamValue("LOGI_FULL_NAME"); //物流商名称
			String yieldly = request.getParamValue("YIELDLY");// 产地
			String status = request.getParamValue("STATUS"); // 状态
			String conPer = request.getParamValue("CON_PER");// 联系人

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("LOGI_CODE", logiCode);
			map.put("LOGI_FULL_NAME", logiFullName);
			map.put("YIELDLY", yieldly);
			map.put("STATUS", status);
			map.put("CON_PER", conPer);
			map.put("poseId", logonUser.getPoseId().toString());
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getLogisticsQuery(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物流商管理查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 物流商管理新增初始化
	 */
	public void addLogisticsInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
		    logonUser.getPoseBusType();
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);            
			act.setOutData("poseType", Constant.SYS_USER_SGM);//职位类别为车厂
			act.setForword(addLogisticsUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"物流商维护新增初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增物流商
	 */
	public void addLogistics() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String logiId=SequenceManager.getSequence("");
			String yieldaly = request.getParamValue("YIELDLY"); //产地
			String logiCode = request.getParamValue("LOGI_CODE"); //物流商编码
			String logiName = request.getParamValue("LOGI_NAME"); //物流商简称
			String logiFullName = request.getParamValue("LOGI_FULL_NAME"); //物流商全称
			
			String corporation = request.getParamValue("CORPORATION"); //法人
			String conPer = request.getParamValue("CON_PER"); //联系人
			String conTel = request.getParamValue("CON_TEL"); //联系人电话
			String status = request.getParamValue("STATUS"); //状态
			String address = request.getParamValue("ADDRESS"); //地址
			String remark = request.getParamValue("REMARK"); //备注
			
			String disIds = request.getParamValue("disIds_"); //运费里程IDS
			
			TtSalesLogiPO tslp=new TtSalesLogiPO();//物流商管理表
			tslp.setLogiId(Long.parseLong(logiId));
			tslp.setYieldly(Long.parseLong(yieldaly));
			tslp.setLogiCode(logiCode);
			tslp.setLogiName(logiName);
			tslp.setLogiFullName(logiFullName);
			tslp.setCorporation(corporation);
			tslp.setConPer(conPer);
			tslp.setConTel(conTel);
			tslp.setStatus(Long.parseLong(status));
			tslp.setAddress(address);
			tslp.setRemark(remark);
			tslp.setCreateBy(logonUser.getUserId());//创建人
			tslp.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//创建时间
			
			 reDao.logisticsAdd(tslp);//物流商管理表
			if (null != disIds && !"".equals(disIds)) {
//				List<Map<String,Object>> disList=reDao.getDisByDisIDS(disIds,logiId);//根据里程IDS获取里程相关信息
				String[] array = disIds.split(",");
//				 if(disList!=null && disList.size()>0){
//					 act.setOutData("returnValue", 2);//添加失败，该地市已有物流商管理
//					 act.setOutData("disList", disList);
//				  }	else{
					 
					  for (int i = 0; i < array.length; i++) {
							if(null != array[i] && !"".equals(array[i])){//过滤第一个
							String logiAreaId=SequenceManager.getSequence("");
							TtSalesLogiAreaPO tslap=new TtSalesLogiAreaPO();//物流商管理与区域表
							tslap.setLogiAreaId(Long.parseLong(logiAreaId));//物流商管理与区域ID
							tslap.setLogiId(Long.parseLong(logiId));//物流商ID
							tslap.setDisId(Long.parseLong(array[i]));//里程ID
							tslap.setCreateBy(logonUser.getUserId());//创建人
							tslap.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//创建时间
							reDao.logisticsAreaAdd(tslap);
							}
						} 
//				 }
			}
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 新增物流商信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	

	/**
	 * 物流商修改初始化
	 */
	public void editLogisticsInit(){ 
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			String id = request.getParamValue("Id");
			Map<String, Object> complaintMap = reDao.getSalesLogiMsg(id);
			TtSalesLogiAreaPO tslap=new TtSalesLogiAreaPO();//物流商管理与区域表
			tslap.setLogiId(Long.parseLong(id));
			List<TtSalesLogiAreaPO> listMata= reDao.getLogisticsMata(tslap);
			String disIds="";
			if(listMata.size()>0){
				for(int i=0;i<listMata.size();i++){
					TtSalesLogiAreaPO tsale=(TtSalesLogiAreaPO)listMata.get(i);
					disIds+=tsale.getDisId()+",";
				}
				disIds=disIds.substring(0,disIds.length()-1);//去掉最后一个逗号
			}
			
			act.setOutData("complaintMap", complaintMap);	
			act.setOutData("parmlog", disIds);	
			act.setForword(editLogisticsUrl);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "物流商管理修改信息初始化");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	/**
	 * 物流商修改保存
	 */
	public void editLogistics() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String logiId=request.getParamValue("LOGI_ID");//物流商ID
			String yieldaly = request.getParamValue("YIELDLY"); //产地
			String logiCode = request.getParamValue("LOGI_CODE"); //物流商编码
			String logiName = request.getParamValue("LOGI_NAME"); //物流商简称
			String logiFullName = request.getParamValue("LOGI_FULL_NAME"); //物流商全称
			
			String corporation = request.getParamValue("CORPORATION"); //法人
			String conPer = request.getParamValue("CON_PER"); //联系人
			String conTel = request.getParamValue("CON_TEL"); //联系人电话
			String status = request.getParamValue("STATUS"); //状态
			String address = request.getParamValue("ADDRESS"); //地址
			String remark = request.getParamValue("REMARK"); //备注
			
			String disIds = request.getParamValue("disIds_"); //运费里程IDS
			TtSalesLogiPO tslp=new TtSalesLogiPO();//物流商管理表
			tslp.setYieldly(Long.parseLong(yieldaly));
			tslp.setLogiCode(logiCode);
			tslp.setLogiName(logiName);
			tslp.setLogiFullName(logiFullName);
			tslp.setCorporation(corporation);
			tslp.setConPer(conPer);
			tslp.setConTel(conTel);
			tslp.setStatus(Long.parseLong(status));
			tslp.setAddress(address);
			tslp.setRemark(remark);
			tslp.setUpdateBy(logonUser.getUserId());//修改人
			tslp.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//修改时间
			tslp.setIsStatus(3);//已修改状态
			TtSalesLogiPO tslpseach=new TtSalesLogiPO();//物流商管理表查询条件
			tslpseach.setLogiId(Long.parseLong(logiId));
			
			//删除对应的关联数据
			TtSalesLogiAreaPO tslapseach= new TtSalesLogiAreaPO();
			tslapseach.setLogiId(Long.parseLong(logiId));

		   reDao.logisticsUpdate(tslpseach, tslp);
			//添加新增数据道关系表
			if (null != disIds && !"".equals(disIds)) {
//				List<Map<String,Object>> disList=reDao.getDisByDisIDS(disIds,logiId);//根据里程IDS获取里程相关信息
				String[] array = disIds.split(",");
//				 if(disList!=null && disList.size()>0){
//					 act.setOutData("returnValue", 2);//修改失败，该地市已有物流商管理
//					 act.setOutData("disList", disList);
//				  }	else{
				    reDao.logisticsAreaDelete(tslapseach);
					for (int i = 0; i < array.length; i++) {
						if(null != array[i] && !"".equals(array[i])){//过滤第一个
						String logiAreaId=SequenceManager.getSequence("");
						TtSalesLogiAreaPO tslap=new TtSalesLogiAreaPO();//物流商管理与区域表
						tslap.setLogiAreaId(Long.parseLong(logiAreaId));//物流商管理与区域ID
						tslap.setLogiId(Long.parseLong(logiId));//物流商ID
						tslap.setDisId(Long.parseLong(array[i]));//里程ID
						tslap.setCreateBy(logonUser.getUserId());//创建人
						tslap.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//创建时间
						reDao.logisticsAreaAdd(tslap);
						}
					}
//			   }
			}
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 修改物流商信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}

