package com.infodms.dms.actions.sales.storage.storagebase;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.storage.storagebase.FareRatioDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmFareRatioPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.util.URLDecoder;
/**
 * 
 * @ClassName     : FareRatioSet 
 * @Description   : 运费系数设置控制类 
 * @author        : syh
 * CreateDate     : 2017-7-4
 */
public class FareRatioSet {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final FareRatioDao reDao = FareRatioDao.getInstance();
	
	private final String fareRatioInitUrl = "/jsp/sales/storage/storagebase/fareRatio/fareRatioSetList.jsp";
	private final String addFareRatioUrl = "/jsp/sales/storage/storagebase/fareRatio/addFareRatio.jsp";
	private final String editFareSetUrl = "/jsp/sales/storage/storagebase/fareRatio/updateFareRatio.jsp";
												

	/**
	 * 运费系数设定初始化
	 */
	public void fareRatioSetInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//获取车系信息（查询页面车系下拉框用）
			List<Map<String, Object>> list_vchl =reDao.getVhclMsg("");
			act.setOutData("list_vchl", list_vchl);		
			act.setForword(fareRatioInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运费系数设定初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 运费系数查询
	 */
	public void fareRatioQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String groupId=CommonUtils.checkNull(request.getParamValue("GROUP_ID"));//车系ID
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getFareRatioQuery(groupId, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "运费系数查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 运费系数设定新增初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void addFareRatioInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			//获取车系信息（查询页面车系下拉框用）
			List<Map<String, Object>> list_vchl =reDao.getVhclMsg("");
			act.setOutData("list_vchl", list_vchl);	
			act.setForword(addFareRatioUrl);
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
	public void addFareRatio() {
		
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String ratioNum = CommonUtils.checkNull(request.getParamValue("RATIO_NUM"));//运费系数
			String groupId = CommonUtils.checkNull(request.getParamValue("GROUP_ID"));// 车系
			//判断该车系是否已设定运费系数，若已设定，无法新增
			TmFareRatioPO tpo=new TmFareRatioPO();
 			tpo.setSeriesId(Long.parseLong(groupId));
			List list1=reDao.select(tpo);
			if(list1!=null && list1.size()>0){
				act.setOutData("returnValue", 2);
				return ;
			}
			TmFareRatioPO tpo2=new TmFareRatioPO();
			tpo2.setSetId(Long.valueOf(SequenceManager.getSequence(null)));
			tpo2.setSeriesId(Long.parseLong(groupId));
			tpo2.setRatioNum(Double.parseDouble(ratioNum));
			tpo2.setCreateBy(Long.valueOf(logonUser.getUserId()));
			tpo2.setCreateDate(new Date());
			reDao.insert(tpo2);
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 新增运费系数信息");
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
	public void editFareRatioInit(){ 
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String setId = CommonUtils.checkNull(request.getParamValue("setId"));//设置ID
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));//车系
			String ratioNum = CommonUtils.checkNull(request.getParamValue("ratioNum"));//运费系数
			act.setOutData("setId", setId);	
			act.setOutData("groupId", groupId);	
			act.setOutData("ratioNum", ratioNum);	
			//获取车系信息（查询页面车系下拉框用）
			List<Map<String, Object>> list_vchl =reDao.getVhclMsg("");
			act.setOutData("list_vchl", list_vchl);		
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
	public void editFareRatio() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String setId = CommonUtils.checkNull(request.getParamValue("SET_ID"));// 设置ID
			String ratioNum = CommonUtils.checkNull(request.getParamValue("RATIO_NUM"));//运费系数
			TmFareRatioPO t1=new TmFareRatioPO();
			t1.setSetId(Long.valueOf(setId));
			TmFareRatioPO t2=new TmFareRatioPO();
			t2.setSetId(Long.valueOf(setId));
			t2.setRatioNum(Double.parseDouble(ratioNum));
			reDao.update(t1, t2);
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
	 * @Description: 删除运费设定信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void delFareRatio() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String setId = CommonUtils.checkNull(request.getParamValue("setId"));//设置ID
			TmFareRatioPO po =new TmFareRatioPO();
			po.setSetId(Long.valueOf(setId));
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
