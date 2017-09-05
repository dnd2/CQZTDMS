/**********************************************************************
* <pre>
* FILE : SeriesShow.java
* CLASS : SeriesShow
*
* AUTHOR : LAX
*
* FUNCTION : 车系联动查询
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
* 		  |2009-11-3| LAX | Created |
* DESCRIPTION:
* 	车辆销售 > 可售车辆 > 可售车辆查询 
* </pre>
***********************************************************************/
/**
* $Id: SeriesShow.java,v 1.4 2010/12/09 11:42:36 zuoxj Exp $
*/
package com.infodms.dms.actions.common;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.SeriesBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.SeriesShowDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class SeriesShow {
	public Logger logger = Logger.getLogger(SeriesShow.class);
	public SeriesShowDao dao = SeriesShowDao.getInstance();
	
	private final String DIALOG_URL_INIT = "/dialog/showCarModel.jsp" ;
	
	/**
	 * @Function  :车系联动查询
	 * @author    :LAX
	 * @createDate:2009-11-3
	 * @version   :0.1
	 * @param     :request
	 * @return    :车系信息
	 * @throws    :Exception
	 * 根据车系ID查询车系下各个属性
	 */
	public void queryProsBySeries() throws Exception{
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		logger.info(logonUser.getUserId());
		try{
			// 公告代码
			String seriesId = CommonUtils.checkNull(request.getParamValue("SERIES_ID"));
			
//			 List<ShowKeyValueBean> vhclTypeList = CarPurchaseDAO.queryVhclTypeBySeries(seriesId);
//			 List<ShowKeyValueBean> dspmList = CarPurchaseDAO.queryDspmBySeries(seriesId);
//			 List<ShowKeyValueBean> gearBoxList = CarPurchaseDAO.queryGearBoxBySeries(seriesId);
//			 List<ShowKeyValueBean> modelYearList = CarPurchaseDAO.queryModelYearBySeries(seriesId);
//			 
//			 act.setOutData("VHCL_TYPE_LIST", vhclTypeList);
//			 act.setOutData("DSPM_LIST", dspmList);
//			 act.setOutData("GEAR_BOX_LIST", gearBoxList);
//			 act.setOutData("MODEL_YEAR_LIST", modelYearList);
			 
			}catch(Exception e){
				
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车系下各个属性");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
		}
	
	
	/*
	 * 车型弹出页面初始化
	 */
	public void carModelUrlInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper req = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String group_id = req.getParamValue("id");
			act.setForword(DIALOG_URL_INIT+"?id="+group_id);
		}catch(Exception e){
			
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车系下各个属性");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 从视图中取车系列表
	 */
	public List<SeriesBean> getSeries(){
		return dao.querySeries();
	}
	
	/*
	 * 根据车系,查询车型
	 */
	public void getCarModel(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper req = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			act.getResponse().setContentType("application/json");
			
			String group_id = req.getParamValue("id");
			/*String code = req.getParamValue("code");
			String name = req.getParamValue("name");
			StringBuffer sql = new StringBuffer("\n");
			sql.append("select group_id,group_code,group_name,status\n");
			sql.append("from tm_vhcl_material_group\n");
			sql.append("where parent_group_id=").append(group_id).append("\n");
			// 只查询group_type为车型的数据
			sql.append("and group_level=3\n");
			if(StringUtil.notNull(code))
				sql.append("and group_code like '%").append(code).append("%'\n");
			if(StringUtil.notNull(name))
				sql.append("and group_name like '%").append(name).append("%'\n");
			sql.append("order by group_code desc\n");*/
			String code = req.getParamValue("code");
			String name = req.getParamValue("name");
			
			int pageSize = 10 ;
			int curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<TmVhclMaterialPO> ps = dao.queryMaterial(group_id,code,name, pageSize, curPage);
			act.setOutData("ps", ps);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车系下各个属性");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

}
