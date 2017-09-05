package com.infodms.dms.actions.publicInfoMng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.SeriesBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.SeriesShowDao;
import com.infodms.dms.dao.customerRelationships.CustomerManageDao;
import com.infodms.dms.dao.publicInfoMng.PublicInfoMngDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtAsRoAddItemPO;
import com.infodms.dms.po.TtAsRoRepairPartPO;
import com.infodms.dms.po.TtAsRoRepairitemPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import common.Logger;

/**
 * 公共信息管理
 * @author Administrator
 *
 */
public class PublicInfoMng {
	private ActionContext act = null ;
	private RequestWrapper req = null ;
	private AclUserBean user = null ;
	
	private Logger logger = Logger.getLogger(PublicInfoMng.class);
	private PublicInfoMngDao dao = PublicInfoMngDao.getInstance();
	
	private final String MAIN_URL = "/jsp/publicInfoMng/publicInfoMain.jsp" ;
	private final String DETAIL_URL = "/jsp/publicInfoMng/ctmInfoDetail.jsp" ;
	private final String HISTORY_URL = "/jsp/publicInfoMng/repairHistory.jsp" ;
	
	/*
	 * 公共信息管理主页面初始化
	 */
	public void mainUrlInit(){
		act = ActionContext.getContext() ;
		user = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			SeriesShowDao dao = SeriesShowDao.getInstance();
			//获得车系列表
			List<SeriesBean> seriesList = dao.querySeriesFromMG() ;
			
			act.setOutData("seriesList", seriesList);
			act.setForword(MAIN_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"公共信息管理页面初始化");
			logger.error(user,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 根据车系查询车型
	 */
	public void queryModel(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper req = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String id = req.getParamValue("id");
			SeriesShowDao dao = SeriesShowDao.getInstance();
			//获得车型列表
			List<TmVhclMaterialGroupPO> modelList = dao.getModel(id);
			StringBuffer modelStr = new StringBuffer("<select id='model' name='model' class='short_sel'><option value=''>-请选择-</option>");
			for(int i=0;i<modelList.size();i++){
				modelStr.append("<option value=").append(modelList.get(i).getGroupId()).append(">").append(modelList.get(i).getGroupName()).append("</option>");
			}
			modelStr.append("</select>");
			
			act.setOutData("modelStr", modelStr.toString());
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户信息查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 主页面主查询
	 */
	public void mainQuery(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ;
		user = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			StringBuffer con = new StringBuffer() ;
			String ctmName = CommonUtils.checkNull(req.getParamValue("ctmName"));//客户姓名
			String mainPhone = CommonUtils.checkNull(req.getParamValue("mainPhone"));//客户电话
			String ctmType = CommonUtils.checkNull(req.getParamValue("ctmType"));//客户类型
			String sex = CommonUtils.checkNull(req.getParamValue("sex"));//性别
			String guestStars = CommonUtils.checkNull(req.getParamValue("guestStars"));//客户星级
			String dateStart = CommonUtils.checkNull(req.getParamValue("dateStart"));//购车开始日期
			String dateEnd = CommonUtils.checkNull(req.getParamValue("dateEnd"));//购车结束日期
			String vin = CommonUtils.checkNull(req.getParamValue("vin"));//VIN
			String materialName = CommonUtils.checkNull(req.getParamValue("materialName"));//选择物料
			String yieldly = CommonUtils.checkNull(req.getParamValue("yieldly"));//生产基地
			String seriesId = req.getParamValue("series");//车系
			String modelId = req.getParamValue("model");//车型
			
			if(StringUtil.notNull(ctmName))
				con.append(" and A.CTM_NAME LIKE '%"+ctmName+"%'\n");
			if(StringUtil.notNull(mainPhone))
				con.append("and A.MAIN_PHONE LIKE '%"+mainPhone+"%'\n");
			if(StringUtil.notNull(ctmType))
				con.append("and A.CTM_TYPE = '"+ctmType+"'\n");
			if(StringUtil.notNull(sex))
				con.append("and A.SEX = '"+sex+"'\n");
			if(StringUtil.notNull(vin))
				con.append("and C.VIN LIKE '%"+vin+"%'\n");
			if(StringUtil.notNull(guestStars))
				con.append("and A.GUEST_STARS = '"+guestStars+"'\n");
			if(StringUtil.notNull(yieldly))
				con.append("and C.YIELDLY = '"+yieldly+"'\n");
			if(StringUtil.notNull(dateStart))
				con.append("and c.Purchased_Date>=to_date('"+dateStart+" 00:00:00','yyyy-MM-dd hh24:mi:ss') \n");
			if(StringUtil.notNull(dateEnd))
				con.append("and c.Purchased_Date<=to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') \n");
			if(StringUtil.notNull(seriesId))
				con.append("and s.group_id=").append(seriesId).append("\n");
			if(StringUtil.notNull(modelId))
				con.append("and g.group_id=").append(modelId).append("\n");
			
			int pageSize = 10 ;
			Integer curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")): 1; // 处理当前页
			PageResult<Map<String,Object>> ps = dao.mainQuery(con.toString(), pageSize, curPage) ;
			act.setOutData("ps", ps) ;
 		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"公共信息管理主页面查询");
			logger.error(user,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 明细按钮对应的操作
	 */
	public void showDetail(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ;
		user = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Long ctm_id = Long.parseLong(req.getParamValue("id")); //客户ID
			
			//客户信息
			CustomerManageDao dao = CustomerManageDao.getInstance();
			List<Map<String,Object>> resultList = dao.queryCustomerDetail(ctm_id);
			Map<String,Object> resultMap = new HashMap<String, Object>();
			if(resultList!=null && resultList.size()>0)
				resultMap = resultList.get(0);
			
			act.setOutData("customMap", resultMap);
			act.setForword(DETAIL_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"公共信息管理页面初始化");
			logger.error(user,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 车辆维修历史查询
	 */
	public void showHistory(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ;
		user = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String vin = req.getParamValue("vin"); 
			
			List<TtAsRoRepairPartPO> partList = dao.queryParts(vin) ;
			List<TtAsRoRepairitemPO> itemList = dao.queryItem(vin) ;
			List<TtAsRoAddItemPO> addItemList = dao.queryAddItem(vin) ;
			
			act.setOutData("parts", partList);
			act.setOutData("items", itemList);
			act.setOutData("addItems",addItemList);
			
			act.setForword(HISTORY_URL);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆维修历史查询");
			logger.error(user,e1);
			act.setException(e1);
		}
	}
}
