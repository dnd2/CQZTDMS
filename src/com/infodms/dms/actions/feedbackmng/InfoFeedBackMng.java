package com.infodms.dms.actions.feedbackmng;

import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.VehicleSeriesBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.CommonDAO;
import com.infoservice.mvc.context.ActionContext;

public class InfoFeedBackMng {
private static Logger logger = Logger.getLogger(InfoFeedBackMng.class);
	/**
	 * Function：获得车系列表
	 * @param  ：	
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-5-21 赵伦达
	 */
	public static String getVehicleSeriesByDealerId(){
		ActionContext ctx = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)ctx.getSession().get(Constant.LOGON_USER);
		List<VehicleSeriesBean> seriesList = CommonDAO.queryVehicleSeriesByDealerId("");
		String retStr="";
		retStr+="<option value=\'\'>-请选择-</option>";
		for(int i=0;i<seriesList.size();i++){
			VehicleSeriesBean bean=new VehicleSeriesBean();
			bean=(VehicleSeriesBean)seriesList.get(i);
			retStr+="<option value=\'"+bean.getGroup_id()+"\'>"+bean.getVehicle_series()+"</option>";
		}
		return retStr;
	}
	
	/*
	 * 带默认值的下拉框选择
	 */
	public static String getVehicleSeriesByDealerId2(Long id){
		ActionContext ctx = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)ctx.getSession().get(Constant.LOGON_USER);
		List<VehicleSeriesBean> seriesList = CommonDAO.queryVehicleSeriesByDealerId("");
		String retStr="";
		for(int i=0;i<seriesList.size();i++){
			VehicleSeriesBean bean=new VehicleSeriesBean();
			bean=(VehicleSeriesBean)seriesList.get(i);
			if(bean.getGroup_id().equals(id))
				retStr+="<option value=\'"+bean.getGroup_id()+"\' selected>"+bean.getVehicle_series()+"</option>";
			else
				retStr+="<option value=\'"+bean.getGroup_id()+"\'>"+bean.getVehicle_series()+"</option>";
		}
		return retStr;
	}
}
