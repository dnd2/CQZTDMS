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
import com.infodms.dms.dao.sales.storage.storagebase.FareSetDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesFarePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : reservoirRegionManage 
 * @Description   : 运费设定控制类 
 * @author        : ranjian
 * CreateDate     : 2013-4-5
 */
public class FareSet {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final FareSetDao reDao = FareSetDao.getInstance();
	
	private final String fareSetInitUrl = "/jsp/sales/storage/storagebase/fareSet/fareSetList.jsp";
	private final String addFareSetUrl = "/jsp/sales/storage/storagebase/fareSet/addFareSet.jsp";
	private final String editFareSetUrl = "/jsp/sales/storage/storagebase/fareSet/updateFareSet.jsp";
												

	/**
	 * 
	 * @Title      : 
	 * @Description: 运费设定初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void fareSetInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			//获取车系信息（查询页面车系下拉框用）
			List<Map<String, Object>> list_vchl =reDao.getVhclMsg(list_yieldly!=null?list_yieldly.get(0).get("AREA_ID").toString():"");
			act.setOutData("list_vchl", list_vchl);	
			act.setForword(fareSetInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运费设定初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 运费设定查询信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void fareSetQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY"));// 产地
			String groupId=CommonUtils.checkNull(request.getParamValue("GROUP_ID"));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("YIELDLY", yieldly);
			map.put("GROUP_ID", groupId);
			map.put("poseId", logonUser.getPoseId().toString());
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getFareSetQuery(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "运费设定信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 运费设定新增初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void addFareSetInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY"));// 产地
			String newYieldly=yieldly;//(这里获取以后当前登陆的所属的产地,注意修改)
			if(yieldly==null || "".equals(yieldly)){
				if(list_yieldly.size()>0){
					newYieldly=list_yieldly.get(0).get("AREA_ID").toString();
				}else{
					act.setException(new BizException(act,new Exception(),ErrorCodeConstant.QUERY_FAILURE_CODE,"该用户无产地"));
					return;
				}
			}
			//获取车系信息（查询页面车系下拉框用）
			List<Map<String, Object>> list_vchl =reDao.getVhclMsg(newYieldly);
			act.setOutData("list_vchl", list_vchl);	
				
			//初始化文本狂信息
			/*************************************************************************/
			List<Map<String, Object>> list = reDao.getFareMileageMsg(newYieldly,logonUser.getPoseId().toString());
			/*************************************************************************/
			act.setOutData("yieldly", newYieldly);
			act.setOutData("list_tt", list);
			act.setForword(addFareSetUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"运费设定新增初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 添加运费设定信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void addFareSet() {
		
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY"));// 产地
			String groupId = CommonUtils.checkNull(request.getParamValue("GROUP_ID"));// 车系
			//判断是否该产地该车系是否有运费设定，如果有，无法添加
			//判断库区名称是否有重复，重复不能添加
			TtSalesFarePO tpo=new TtSalesFarePO();
			tpo.setYieldly(Long.parseLong(yieldly));//产地
 			tpo.setGroupId(Long.parseLong(groupId));
			List list1=reDao.select(tpo);
			if(list1!=null && list1.size()>0){
				act.setOutData("returnValue", 3);//该产地下该车系已设定运费，无法添加
				return ;
			}
			//获取运费里程信息
			List<Map<String, Object>> list = reDao.getFareMileageMsg(yieldly,logonUser.getPoseId().toString());
			if(list!=null && list.size()>0){//
			//运费设定表
				for(int i=0;i<list.size();i++){
					String fareId = SequenceManager.getSequence(null);//运费设定序列
					Map<String, Object> fmap=(Map<String, Object>)list.get(i);
						String amount=request.getParamValue(fmap.get("MIL_ID").toString());
						TtSalesFarePO tsfp=new TtSalesFarePO();
						//tsmp.setMilId(Long.parseLong(milId));//运费里程ID
						tsfp.setFareId(Long.parseLong(fareId));//运费设定ID		
						tsfp.setMilId(Long.parseLong(fmap.get("MIL_ID").toString()));//里程ID
						tsfp.setGroupId(Long.parseLong(groupId));//车系ID
						tsfp.setYieldly(Long.parseLong(yieldly));//产地
						tsfp.setAmount(Float.parseFloat(amount));//金额
						tsfp.setCreateBy(logonUser.getUserId());//创建人
						tsfp.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//创建时间
						tsfp.setUpdateBy(logonUser.getUserId());//修改人
						tsfp.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//修改时间
						/**********************************************************/
						/*当前产地有运费设定信息后，进行判断该公里端是否已有数据*/
						/* 如果该公里段有数据，则修改该条数据*/
						/* 如果该公里段没有数据，则添加该数据*/
						/**********************************************************/
						//添加时首先需要判断运费表是否存在，如果存在修改数据，否则添加数据！（根据以下条件进行判断<里程ID><车系ID><产地>）
						List<Map<String, Object>> list_FN = reDao.getFareSetMsg(yieldly,fmap.get("MIL_ID").toString(),groupId);
						if(list_FN!=null &&  list_FN.size()>0){//
								TtSalesFarePO seachPO=new TtSalesFarePO();
								seachPO.setGroupId(Long.parseLong(groupId));
								seachPO.setMilId(Long.parseLong(fmap.get("MIL_ID").toString()));
								seachPO.setYieldly(Long.parseLong(yieldly));
								reDao.fareSetUpdate(seachPO,tsfp);//修改运费设定信息
							
						}else{
							reDao.fareSetAdd(tsfp);//添加运费设定信息
						}
					
				}
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 新增运费信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 运费设定修改初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void editFareSetInit(){ 
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));// 产地
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));//车系
			//获取车系信息（查询页面车系下拉框用）
			List<Map<String, Object>> list_vchl =reDao.getVhclMsg(yieldly);
			act.setOutData("list_vchl", list_vchl);	
			//初始化文本狂信息
			/*************************************************************************/
			List<Map<String, Object>> list = reDao.getFareMileageMsg(yieldly,logonUser.getPoseId().toString());
			/*************************************************************************/
			act.setOutData("list_tt", list);
			Map<String, Object> complaintMap = reDao.getFareSetMsg(groupId,yieldly,logonUser.getPoseId()); 
			act.setOutData("complaintMap", complaintMap);		
			act.setForword(editFareSetUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"运费设定修改初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 修改运费设定信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void editFareSet() {
		

		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY"));// 产地
			String groupId = CommonUtils.checkNull(request.getParamValue("GROUP_ID"));// 车系
			String groupIdOld = CommonUtils.checkNull(request.getParamValue("GROUP_ID_OLD"));// 车系(原始ID)
			/************************************************************************/
			/**1.没改变车系时候直接修改;**********************************************/
			/** 2改变车系ID时候，首先删除原有的车系，然后添加**************************/
			/************************************************************************/
			if(!groupId.equals(groupIdOld)){//
//				TtSalesFarePO tsfp=new TtSalesFarePO();
//				tsfp.setYieldly(Long.parseLong(yieldly));//产地
//				tsfp.setGroupId(Long.parseLong(groupIdOld));//旧的车系ID
//				//首先删除原有的车系信息
//				reDao.fareSetDelete(tsfp);
				TtSalesFarePO tpo=new TtSalesFarePO();
				tpo.setYieldly(Long.parseLong(yieldly));//产地
	 			tpo.setGroupId(Long.parseLong(groupId));
				List list1=reDao.select(tpo);
				if(list1!=null && list1.size()>0){
					act.setOutData("returnValue", 3);//该产地下该车系已设定运费，无法添加
					return ;
				}
			}
			//获取运费里程信息
			List<Map<String, Object>> list = reDao.getFareMileageMsg(yieldly,logonUser.getPoseId().toString());
			if(list!=null && list.size()>0){//
			//运费设定表
				for(int i=0;i<list.size();i++){
					String fareId = SequenceManager.getSequence(null);//运费设定序列
					Map<String, Object> fmap=(Map<String, Object>)list.get(i);
						String amount=request.getParamValue(fmap.get("MIL_ID").toString());
						TtSalesFarePO tsfp=new TtSalesFarePO();
						//tsmp.setMilId(Long.parseLong(milId));//运费里程ID
						tsfp.setFareId(Long.parseLong(fareId));//运费设定ID		
						tsfp.setMilId(Long.parseLong(fmap.get("MIL_ID").toString()));//里程ID
						tsfp.setGroupId(Long.parseLong(groupId));//车系ID
						tsfp.setYieldly(Long.parseLong(yieldly));//产地
						tsfp.setAmount(Float.parseFloat(amount));//金额
						tsfp.setCreateBy(logonUser.getUserId());//创建人
						tsfp.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//创建时间
						tsfp.setUpdateBy(logonUser.getUserId());//修改人
						tsfp.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//修改时间
						/**********************************************************/
						/*当前产地有运费设定信息后，进行判断该公里端是否已有数据*/
						/* 如果该公里段有数据，则修改该条数据*/
						/* 如果该公里段没有数据，则添加该数据*/
						/**********************************************************/
						//添加时首先需要判断运费表是否存在，如果存在修改数据，否则添加数据！（根据以下条件进行判断<里程ID><车系ID><产地>）
						List<Map<String, Object>> list_FN = reDao.getFareSetMsg(yieldly,fmap.get("MIL_ID").toString(),groupId);
						if(list_FN!=null &&  list_FN.size()>0){//
								TtSalesFarePO seachPO=new TtSalesFarePO();
								seachPO.setGroupId(Long.parseLong(groupId));
								seachPO.setMilId(Long.parseLong(fmap.get("MIL_ID").toString()));
								seachPO.setYieldly(Long.parseLong(yieldly));
								reDao.fareSetUpdate(seachPO,tsfp);//修改运费设定信息
							
						}else{
							reDao.fareSetAdd(tsfp);//添加运费设定信息
						}
					
				}
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 新增运费信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void getFareMileageMsg() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));// 产地
			List<Map<String, Object>> list = reDao.getFareMileageMsg(yieldly,logonUser.getPoseId().toString());
			act.setOutData("list", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "最大配额总量查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 删除运费设定信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void delFareSet() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));// 产地
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));// 车系
			TtSalesFarePO po =new TtSalesFarePO();
			po.setGroupId(Long.parseLong(groupId));
			po.setYieldly(Long.parseLong(yieldly));
			reDao.delete(po);
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			act.setOutData("returnValue", 2);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 删除运费设定信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
