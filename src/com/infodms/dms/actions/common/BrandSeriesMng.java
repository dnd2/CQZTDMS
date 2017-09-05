package com.infodms.dms.actions.common;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.po.TmBrandPO;
import com.infodms.dms.po.TmSeriesPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

/**
 * 厂家车系选择公用模块
 * @author ZhaoLi
 *
 */
public class BrandSeriesMng {
	public void queryBrands(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		String brandName = request.getParamValue("brand");
		Map<String,List<TmBrandPO>> map = CommonDAO.selBrands(brandName);
		act.setOutData("brands", map);
	}
	
	/**
	 *  根据DealerId和品牌的关系进行过滤
	 */
	public void queryBrandsByDlr()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		act.getSession().get(Constant.LOGON_USER);
		//String brandName = request.getParamValue("brand");
		Long companyId = logonUser.getCompanyId();
		List<TmBrandPO> list = CommonDAO.selBrandsByDlr(companyId);
		act.setOutData("brands", list);
	}
	
	public void querySeries(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		String brandId = request.getParamValue("brandId");
		List<TmSeriesPO> list = CommonDAO.selSeries(new Long(brandId));
		act.setOutData("series", list);
	}
	
	/**
	 * 
	 * Function    : 根据DealerId和品牌的关系进行过滤
	 * LastUpdate  : 2009-9-17
	 */
	public void querySeriesByDlr()
	{
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		String brandId = request.getParamValue("brand");
		List<TmSeriesPO> list = null;
		if (null != brandId && !brandId.equals(""))
		{
			list = CommonDAO.selSeries(new Long(brandId));
		}
		act.setOutData("series", list);
	}	
}
