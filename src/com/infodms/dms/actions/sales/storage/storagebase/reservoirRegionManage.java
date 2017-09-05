package com.infodms.dms.actions.sales.storage.storagebase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.sales.storage.storagebase.ReservoirRegionDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesAreaMatPO;
import com.infodms.dms.po.TtSalesAreaPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : reservoirRegionManage 
 * @Description   : 库区管理控制类 
 * @author        : ranjian
 * CreateDate     : 2013-4-1
 */
public class reservoirRegionManage {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final ReservoirRegionDao reDao = ReservoirRegionDao.getInstance();
	
	private final String reservoirRegionInitUrl = "/jsp/sales/storage/storagebase/reservoirRegionManage/reservoirRegionList.jsp";
	private final String addReservoirRegionInitUrl = "/jsp/sales/storage/storagebase/reservoirRegionManage/addReservoirRegion.jsp";
	private final String editReservoirRegionInitUrl = "/jsp/sales/storage/storagebase/reservoirRegionManage/updateReservoirRegion.jsp";

	/**
	 * 
	 * @Title      : 
	 * @Description: 库区管理初始化查询条件
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-1
	 */
	public void reservoirRegionInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			act.setForword(reservoirRegionInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"库区信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 库区管理查询信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-1
	 */
	public void reservoirRegionQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String areaCode = request.getParamValue("AREA_CODE"); // 库区代码
			String areaName = request.getParamValue("AREA_NAME"); // 库区名称
			String dutyPer = request.getParamValue("DUTY_PER"); // 责任人
			String dutyTel = request.getParamValue("DUTY_TEL"); // 责任人电话
			String type = request.getParamValue("TYPE"); // 库区类型
			String yieldly = request.getParamValue("YIELDLY");// 产地
			String remark = request.getParamValue("REMARK");// 库区备注

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("AREA_CODE", areaCode);
			map.put("AREA_NAME", areaName);
			map.put("DUTY_PER", dutyPer);
			map.put("DUTY_TEL", dutyTel);
			map.put("TYPE", type);
			map.put("YIELDLY", yieldly);
			map.put("poseId", logonUser.getPoseId().toString());
			map.put("REMARK", remark);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getReservoirRegionQuery(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "库区信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 库区信息新增初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public void addReservoirRegionInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			act.setForword(addReservoirRegionInitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"库区信息新增初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 库区信息修改初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public void editReservoirRegionInit(){ 
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			String id = request.getParamValue("Id");
			//获取所有车系信息
			Map<String, Object> complaintMap = reDao.getReservoirMsg(id); 
			//得到库区与车系关联的ID的得到车系ID
			TtSalesAreaMatPO tpo=new TtSalesAreaMatPO();
			tpo.setAreaId(Long.parseLong(id));
			List<TtSalesAreaMatPO> listMata= reDao.getReservoirMata(tpo);
			
			act.setOutData("complaintMap", complaintMap);
			act.setOutData("listMata", listMata);
			
			act.setForword(editReservoirRegionInitUrl);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "库区修改信息初始化");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 添加库区信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public void addReservoirRegion() {
		
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String areaId = SequenceManager.getSequence(null);//库区序列
			String areaCode =  CommonUtils.checkNull(request.getParamValue("AREA_CODE")); //库区代码
			String areaName =  CommonUtils.checkNull(request.getParamValue("AREA_NAME")); //库区名称
			String dutyPer =  CommonUtils.checkNull(request.getParamValue("DUTY_PER"));  //责任人
			String dutyTel =  CommonUtils.checkNull(request.getParamValue("DUTY_TEL")); //责任人电话
			String inStatus =  CommonUtils.checkNull(request.getParamValue("IN_STATUS")); //入库状态
			String outStatus =  CommonUtils.checkNull(request.getParamValue("OUT_STATUS")); //出库状态
			String yieldly =  CommonUtils.checkNullNum(request.getParamValue("YIELDLY")); //产地
			String type = CommonUtils.checkNull(request.getParamValue("TYPE")); //库区类型
			String remark = CommonUtils.checkNull(request.getParamValue("REMARK")); //库区备注
			//判断库区名称是否有重复，重复不能添加
			TtSalesAreaPO tpo=new TtSalesAreaPO();
			tpo.setYieldly(Long.parseLong(yieldly));//产地
 			tpo.setAreaName(areaName);//库区名称
			List list=reDao.select(tpo);
			if(list!=null && list.size()>0){
				act.setOutData("returnValue", 3);//该库区名字已被占用，请重新输入
				return ;
			}
			String groupIds = request.getParamValue("groupIdss");//选中的车系
			String[] strarr=null;
			if(groupIds!=null && !"".equals(groupIds)){
				strarr=groupIds.split(",");
			}
			
			//库区信息
			TtSalesAreaPO tapo=new TtSalesAreaPO();			
			tapo.setAreaId(Long.parseLong(areaId));
			tapo.setAreaCode(areaCode);
			tapo.setAreaName(areaName);
			tapo.setDutyPer(dutyPer);
			tapo.setDutyTel(dutyTel);
			tapo.setInStatus(Long.parseLong(inStatus));
			tapo.setOutStatus(Long.parseLong(outStatus));
			tapo.setYieldly(Long.parseLong(yieldly));
			tapo.setType(Long.parseLong(type));
			tapo.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
			tapo.setCreateBy(logonUser.getUserId());
			tapo.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
			tapo.setUpdateBy(logonUser.getUserId());
			tapo.setRemark(remark);
			
			tapo.setStatus(Constant.STATUS_ENABLE.longValue());
				//添加库区基本信息
				reDao.reservoirSalesAreaAdd(tapo);
				if(strarr!=null  && strarr.length>0){
					for(int i=0;i<strarr.length;i++){
						String areaMataId = SequenceManager.getSequence(null);//库区与车系关系序列
						//库区与车系关系
						TtSalesAreaMatPO tsamp=new  TtSalesAreaMatPO();
						tsamp.setAreaMatId(Long.parseLong(areaMataId));
						tsamp.setAreaId(Long.parseLong(areaId));
						tsamp.setMatId(Long.parseLong(strarr[i]));
						tsamp.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
						tsamp.setCreateBy(logonUser.getUserId());
						tsamp.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
						tsamp.setUpdateBy(logonUser.getUserId());
						reDao.reservoirSalesAreaMataAdd(tsamp);
					}
				}
			//reservoirRegionInit();
				act.setOutData("returnValue", 1);
		}catch(RuntimeException e){
			BizException e1 = new BizException(act,ErrorCodeConstant.SPECIAL_MEG,2,new Object[]{e.getMessage()});
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 新增库区信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 修改库区信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public void editReservoirRegion() {
		
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String areaId = CommonUtils.checkNull(request.getParamValue("AREA_ID")); //库区编码
			String areaCode = CommonUtils.checkNull(request.getParamValue("AREA_CODE")); //库区代码
			String areaName = CommonUtils.checkNull(request.getParamValue("AREA_NAME")); //库区名称
			String dutyPer = CommonUtils.checkNull(request.getParamValue("DUTY_PER"));  //责任人
			String dutyTel = CommonUtils.checkNull(request.getParamValue("DUTY_TEL")); //责任人电话
			String inStatus = CommonUtils.checkNull(request.getParamValue("IN_STATUS")); //入库状态
			String outStatus = CommonUtils.checkNull(request.getParamValue("OUT_STATUS")); //出库状态
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); //产地
			String type = CommonUtils.checkNull(request.getParamValue("TYPE")); //库区类型
			String oldArea = CommonUtils.checkNull(request.getParamValue("OLD_AREA")); //隐藏库区名称
			String remark = CommonUtils.checkNull(request.getParamValue("REMARK")); //库区备注
			String status = CommonUtils.checkNull(request.getParamValue("STATUS")); //状态
			if(!areaName.equals(oldArea)){//跟以前一样不需要判断重复
				//判断库区名称是否有重复，重复不能添加
				TtSalesAreaPO tpo=new TtSalesAreaPO();
				tpo.setYieldly(Long.parseLong(yieldly));//产地
	 			tpo.setAreaName(areaName);//库区名称
				List list=reDao.select(tpo);
				if(list!=null && list.size()>0){
					act.setOutData("returnValue", 3);//该库区名字已被占用，请重新输入
					return ;
				}
			}
			String groupIds = request.getParamValue("groupIdss");//选中的车系
			String[] strarr=null;
			if(groupIds!=null && !"".equals(groupIds)){
				strarr=groupIds.split(",");
			}			
			//库区信息
			TtSalesAreaPO tapo=new TtSalesAreaPO();			
			tapo.setAreaId(Long.parseLong(areaId));
			tapo.setAreaCode(areaCode);
			tapo.setAreaName(areaName);
			tapo.setDutyPer(dutyPer);
			tapo.setDutyTel(dutyTel);
			tapo.setInStatus(Long.parseLong(inStatus));
			tapo.setOutStatus(Long.parseLong(outStatus));
			tapo.setYieldly(Long.parseLong(yieldly));
			tapo.setType(Long.parseLong(type));
			tapo.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
			tapo.setCreateBy(logonUser.getUserId());
			tapo.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
			tapo.setUpdateBy(logonUser.getUserId());
			tapo.setRemark(remark);
			
			tapo.setStatus(Long.parseLong(status));
				//条件
				TtSalesAreaPO seachpo=new TtSalesAreaPO();
				seachpo.setAreaId(Long.parseLong(areaId));
				reDao.reservoirSalesAreaUpdate(seachpo,tapo);
				//首先删除关联数据
				reDao.reservoirSalesAreaDelete(seachpo);
				//然后添加关系
				if(strarr!=null && strarr.length>0){
					for(int i=0;i<strarr.length;i++){
						String areaMataId = SequenceManager.getSequence(null);//库区与车系关系序列
						//库区与车系关系
						TtSalesAreaMatPO tsamp=new  TtSalesAreaMatPO();
						tsamp.setAreaMatId(Long.parseLong(areaMataId));
						tsamp.setAreaId(Long.parseLong(areaId));
						tsamp.setMatId(Long.parseLong(strarr[i]));
						tsamp.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
						tsamp.setCreateBy(logonUser.getUserId());
						tsamp.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
						tsamp.setUpdateBy(logonUser.getUserId());
						reDao.reservoirSalesAreaMataAdd(tsamp);
					}
				}
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 修改库区信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 库区管理查询车系信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-1
	 */
	public void reservoirRegionQueryByMata() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String yeId=CommonUtils.checkNull(request.getParamValue("YEID"));//产地
			Map<String, Object> map =new HashMap<String, Object>();
			map.put("yeId", yeId);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getReservoirRegionQueryByMata(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "库区信息查询车系信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
