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
import com.infodms.dms.dao.sales.storage.storagebase.FareMileageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesFarePO;
import com.infodms.dms.po.TtSalesMilsetPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : reservoirRegionManage 
 * @Description   : 运费里程管理控制类 
 * @author        : ranjian
 * CreateDate     : 2013-4-5
 */
public class FareMileageManage {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final FareMileageDao reDao = FareMileageDao.getInstance();
	private final String fareMileageInitUrl = "/jsp/sales/storage/storagebase/fareMileage/fareMileageList.jsp";
	private final String addfareMileageUrl = "/jsp/sales/storage/storagebase/fareMileage/addFareMileage.jsp";
	private final String editfareMileageUrl = "/jsp/sales/storage/storagebase/fareMileage/updateFareMileage.jsp";
												

	/**
	 * 
	 * @Title      : 
	 * @Description: 运费里程管理初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void fareMileageInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			act.setForword(fareMileageInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运费里程管理初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 运费里程管理查询信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void fareMileageQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String milStart = CommonUtils.checkNull(request.getParamValue("MIL_START")); // 开始里程数
			String milEnd = CommonUtils.checkNull(request.getParamValue("MIL_END")); // 结束里程数
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY"));// 产地

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("MIL_START", milStart);
			map.put("MIL_END", milEnd);
			map.put("YIELDLY", yieldly);
			map.put("poseId", logonUser.getPoseId().toString());
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getFareMileageQuery(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "运费里程管理查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 运费里程新增初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void addFareMileageInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			act.setForword(addfareMileageUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"运费里程新增初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 运费里程修改初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void editFareMileageInit(){ 
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			String id = CommonUtils.checkNull(request.getParamValue("Id"));
			Map<String, Object> complaintMap = reDao.getFareMileageMsg(id); 
			act.setOutData("complaintMap", complaintMap);		
			act.setForword(editfareMileageUrl);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "运费里程修改信息初始化");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 添加运费里程信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void addFareMileage() {
		
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String milId = SequenceManager.getSequence(null);//运费里程序列
			String milStart = CommonUtils.checkNull(request.getParamValue("MIL_START")); // 开始里程数
			String milEnd = CommonUtils.checkNull(request.getParamValue("MIL_END")); // 结束里程数
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY"));// 产地
			
			TtSalesMilsetPO tsmp=new TtSalesMilsetPO();
			tsmp.setMilId(Long.parseLong(milId));//运费里程ID
			tsmp.setMilStart(Integer.parseInt(milStart));
			tsmp.setMilEnd(Integer.parseInt(milEnd));
			tsmp.setYieldly(Long.parseLong(yieldly));
			tsmp.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
			tsmp.setCreateBy(logonUser.getUserId());
			tsmp.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
			tsmp.setUpdateBy(logonUser.getUserId());
			//添运费里程信息 
			reDao.fareMileageAdd(tsmp);
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 新增运费信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 修改运费里程信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void editFareMileage() {
		
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String milId = CommonUtils.checkNull(request.getParamValue("MIL_ID")); //运费里程ID
			String milStart = CommonUtils.checkNull(request.getParamValue("MIL_START")); // 开始里程数
			String milEnd = CommonUtils.checkNull(request.getParamValue("MIL_END")); // 结束里程数
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY"));// 产地
			
			TtSalesMilsetPO tsmp=new TtSalesMilsetPO();
			tsmp.setMilStart(Integer.parseInt(milStart));
			tsmp.setMilEnd(Integer.parseInt(milEnd));
			tsmp.setYieldly(Long.parseLong(yieldly));
			tsmp.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
			tsmp.setUpdateBy(logonUser.getUserId());
			TtSalesMilsetPO seachpo=new TtSalesMilsetPO();
			seachpo.setMilId(Long.parseLong(milId));
			reDao.fareMileageUpdate(seachpo,tsmp);
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 修改运费里程信息 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 删除运费里程设定信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void delFareMileage() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String fId = CommonUtils.checkNull(request.getParamValue("Id"));// 里程ID
			TtSalesFarePO po1 =new TtSalesFarePO();
			po1.setMilId(Long.parseLong(fId));
			reDao.delete(po1);//删除运费设定表
			TtSalesMilsetPO po =new TtSalesMilsetPO();
			po.setMilId(Long.parseLong(fId));
			reDao.delete(po);//删除里程管理表
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			act.setOutData("returnValue", 2);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 删除运费里程信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}
