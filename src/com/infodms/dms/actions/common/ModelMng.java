package com.infodms.dms.actions.common;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ModelBean;
import com.infodms.dms.bean.TmModelBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmModelPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.convert.JsonConverter;
import com.infoservice.po3.bean.PageResult;

/**
 * 经销商选择公用模块
 * @author ZhaoLi
 *
 */
public class ModelMng {
	public Logger logger = Logger.getLogger(OrgMng.class);
	public void queryPro() throws Exception{
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
//		回调父页面方法的名称
		String funcname = CommonUtils.checkNull(request.getParamValue("funcname"));
		List<ModelBean> list = CommonDAO.selProduct();
		HashMap<String,Object> pa = new HashMap<String,Object>();
		pa.put("product", list);
		JsonConverter jc = new JsonConverter();
		act.setOutData("product", new String(jc.sourceToDest(pa),"utf-8"));
		act.setOutData("funcname", funcname);
		act.setForword("/dialog/productList.jsp");
	}
	/**
	 * 配售支持申请调用
	 * @throws Exception
	 */
	public void queryProWholeSale() throws Exception{
		ActionContext act = ActionContext.getContext();
		List<ModelBean> list = CommonDAO.selProduct();
		HashMap<String,Object> pa = new HashMap<String,Object>();
		pa.put("product", list);
		JsonConverter jc = new JsonConverter();
		act.setOutData("product", new String(jc.sourceToDest(pa),"utf-8"));
		act.setForword("/dialog/productListWholeSale.jsp");
	}
	public void queryModelWholeSale(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String proId = request.getParamValue("proId");
			String modelCode = request.getParamValue("modelCode");
			String modelName = request.getParamValue("modelName");
			String orderName = request.getParamValue("orderCol2");
			String da = request.getParamValue("order2");
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<TmModelBean> list = CommonDAO.getModelByProIdWholeSale(proId,modelCode,modelName, Constant.PAGE_SIZE,curPage,orderName,da);
			act.setOutData("ps", list);
		
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void queryModel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String proId = request.getParamValue("proId");
			String modelCode = request.getParamValue("modelCode");
			String modelName = request.getParamValue("modelName");
			String orderName = request.getParamValue("orderCol2");
			String da = request.getParamValue("order2");
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<TmModelPO> list = CommonDAO.getModelByProId(proId,modelCode,modelName, Constant.PAGE_SIZE,curPage,orderName,da);
			act.setOutData("ps", list);
		
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
}
