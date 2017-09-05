package com.infodms.dms.actions.sysusermng.sysuserinfo;

import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TmUserInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sysusermng.SysUserInfoManagerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmQuickFuncPO;
import com.infodms.dms.util.ActionUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

public class SysShortcutKeyManager {
	public Logger logger = Logger.getLogger(SysShortcutKeyManager.class);
	private POFactory factory = POFactoryBuilder.getInstance();
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	
	private final String searchShortcutKeyHistoryInfoURL = "/jsp/sysusermng/SearchShortcutKeyHistoryInfo.jsp";
	//弹出框页面
	private final String searchSystemFunctionInfoURL = "/jsp/sysusermng/SearchSystemFunction.jsp";
	
	/**
	 * Function       :  快捷功能自定义初始化
	 * @throws        :  Exception
	 * LastUpdate     :  2010-1-12
	 */
	public void queryShortcutKeyHistoryInfoInit(){
		try {	
						
			//得到当前用户的ID;
			Long userId = logonUser.getUserId();
			Long poseId = logonUser.getPoseId();
			
			//查询出当前用户的快捷功能列表
			List<TmUserInfoBean> funcs = SysUserInfoManagerDao.findSystemFunctionNames(userId, poseId);
			String funcIds = "";
			if(funcs.size() > 0){
				for (int i = 0; i < funcs.size(); i++) {
					if(i < funcs.size() - 1){
						funcIds = funcIds + funcs.get(i).getFuncId() + ",";
					}else {
						funcIds = funcIds + funcs.get(i).getFuncId();
					}
				}
			}
			act.setOutData("FUNC_IDS", funcIds);
			act.setOutData("funcsList", funcs);		
			
			act.setForword(searchShortcutKeyHistoryInfoURL);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"快捷功能自定义初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function       :  快捷功能查询
	 * @throws        :  Exception
	 * LastUpdate     :  2010-1-12
	 */
	public void queryShortcutKeyHistoryInfo(){
		try {	
			act.getResponse().setContentType("application/json");
			
			RequestWrapper request = act.getRequest();
			String funcIds = request.getParamValue("FUNC_IDS");	
			
			List<TmUserInfoBean> funcs = SysUserInfoManagerDao.findSystemFunctionNamesByFuncIds(funcIds);
			act.setOutData("ps", funcs);
			
			act.setForword(searchShortcutKeyHistoryInfoURL);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"快捷功能查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function       :  查询出当前登录用户所对应职务下的所有的系统功能菜单初始化(子窗体)
	 * @throws        :  Exception
	 * LastUpdate     :  2010-1-18
	 */
	public void querySystemFunctionListInit(){
		try {	
			RequestWrapper request = act.getRequest();
			String funcIds = request.getParamValue("FUNC_IDS");	
			act.setOutData("FUNC_IDS",funcIds);
			
			act.setForword(searchSystemFunctionInfoURL);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"个人信息查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function       :  查询出当前登录用户所对应职务下的所有的系统功能菜单(子窗体 ：不包括已经设定为快捷菜单的功能)
	 * @throws        :  Exception
	 * LastUpdate     :  2010-1-18
	 */
	public void querySystemFunctionList(){
		try {	
			RequestWrapper request = act.getRequest();
			if("1".equals(request.getParamValue("command"))){ //json请求
				act.getResponse().setContentType("application/json");
				
				//获取排序字段和排序类型
				String orderName = request.getParamValue("orderCol");
				String da = request.getParamValue("order");
					
				Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(
						request.getParamValue("curPage")) : 1;
						
				//得到当前用户的ID;
				Long poseId = logonUser.getPoseId();
				String funcName = request.getParamValue("funcName");
				String funcIds = request.getParamValue("FUNC_IDS");	
				
				PageResult<TmUserInfoBean> ps = SysUserInfoManagerDao.findShortcutKeyHistoryInfo(poseId, funcName, funcIds, curPage, Constant.PAGE_SIZE, orderName, da);
				act.setOutData("ps",ps);
				act.setOutData("FUNC_IDS", funcIds);				
			}
			
			act.setForword(searchSystemFunctionInfoURL);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"个人信息查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function       :  保存当前设定的快捷菜单信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-1-18
	 */
	public void saveOrUpdateShortcutKeyHistoryInfo(){
		try {	
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			
			Enumeration<String> enu = request.getParamNames();
			String[] funcIds = null;
			String[] orderList = null;
			
			while(enu.hasMoreElements()){
				String pot = (String) enu.nextElement();
				if(pot.startsWith("FUNCID")){
					funcIds = request.getParamValues(pot);
				}
				
				if(pot.startsWith("FUNC_ORDER")){
					orderList = request.getParamValues(pot);
				}
			}
			
			//功能菜单不为空的情况下执行操作
			if(null != funcIds && !"".equals(funcIds)){
				//删除当前用户下所有已定义的快捷菜单
				SysUserInfoManagerDao.deleteShortcutKeyHistoryInfoByUserIdByPoseId(logonUser.getUserId(),logonUser.getPoseId());
				
				//保存新的快捷菜单				
				for (int i = 0; i < funcIds.length; i++) {
					TmQuickFuncPO qf = new TmQuickFuncPO();
					qf.setQuickFuncId(factory.getLongPK(qf));
					qf.setUserId(logonUser.getUserId());
					qf.setPoseId(logonUser.getPoseId());
					String fucId = funcIds[i];
					qf.setFuncId(new Long(fucId));
					if(null != orderList){
						String order = orderList[i];
						if(null != order && !"".equals(order)){
							qf.setFuncOrder(Integer.valueOf(order));
						}					
					}
					ActionUtil.setCreatePO(qf, logonUser);
					factory.insert(qf);
				}
			}			
			act.setOutData("ACTION_RESULT", "1");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"个人信息查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
