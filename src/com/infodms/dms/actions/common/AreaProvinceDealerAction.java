package com.infodms.dms.actions.common;

import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.AreaProvinceBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.AreaProvinceDealerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmRegionPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

/**
 * 大区与省份级联测试
 * @author Administrator
 *
 */ 
public class AreaProvinceDealerAction {
	private ActionContext act = null ;
	private RequestWrapper req = null ;
	private AclUserBean user = null ;
	private AreaProvinceDealerDao dao = AreaProvinceDealerDao.getInstance() ;
	private Logger logger = Logger.getLogger(AreaProvinceDealerAction.class);
	
	private final String TEST_URL = "/jsp/areaProvinceDealer.jsp" ;
	
	/*
	 * 页面初始化
	 */
	public void mainUrlInit(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ;
		user = (AclUserBean)act.getSession().get(Constant.LOGON_USER) ;
		try{
			List<TmOrgPO> areaList = dao.getArea() ;
			List<AreaProvinceBean> provinceList = dao.getAreaProvince() ;
			
			req.setAttribute("area",areaList );
			req.setAttribute("province", provinceList);
			act.setForword(TEST_URL) ;
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"页面初始化出问题");
			logger.error(be);
			System.out.println(e);
			act.setException(be);
		}
	}
}
