/**********************************************************************
* <pre>
* FILE : CarTypeInfo.java
* CLASS : CarTypeInfo
*
* AUTHOR : wry
*
* FUNCTION : 基础数据维护
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-08-19| wry  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.actions.sysmng.sysData;
/**
 * function:基础数据导入 
 * author: wry
 * CreateDate: 2009-8-19
 * @version:0.1
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.VhclModelInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.baseData.CarTypeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmModelPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.csv.ReaderUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.exceptions.DAOException;

public class CarTypeInfo {
	public Logger logger = Logger.getLogger(CarTypeInfo.class);
	private POFactory factory = POFactoryBuilder.getInstance();
	public final static int pageSize = 10;
	/**
	 * function:车型配置查询 
	 * @param:request-车型代码 开始时间 结束时间 当前登陆人
	 * @return:满足条件的信息
	 * author: wry
	 * @throws BizException 
	 * date: 2009-09-20
	 */
	public void queryCar(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		try{
		    logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request = act.getRequest();
//			if("1".equals(request.getParamValue("COMMAND"))){ //json请求
			String startDate = request.getParamValue("startDate")==null?null:request.getParamValue("startDate").trim();
			String endDate = request.getParamValue("endDate")==null?null:request.getParamValue("endDate").trim();
			String operator = request.getParamValue("operator")==null?null:request.getParamValue("operator").trim();
			String modelCode = request.getParamValue("modelCode")==null?null:request.getParamValue("modelCode").trim();
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			logger.debug("modelCode--"+modelCode);
			//获取排序字段和排序类型
			String orderName = request.getParamValue("orderCol");
			String da = request.getParamValue("order");
	
//			String dealerId = logonUser.getDealerId();
			//查询结果集
			PageResult<VhclModelInfoBean> ps = CarTypeDao.getCarInfo(modelCode, startDate, endDate,operator,request.getRequestURI(), logonUser,orderName, da, curPage);
			logger.debug("条数："+ps.getTotalRecords());
			act.setOutData("ps", ps);
//			}
			if("1".equals(request.getParamValue("falt"))){
			act.setForword("/jsp/systemMng/baseData/carTypeSearch.jsp");
			}else{
			act.setForword("/jsp/systemMng/baseData/carTypeImprotTxt.jsp");
			}
		}catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"操作");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * function:导入配置信息 
	 * author: wry
	 * @throws BizException 
	 * date: 2009-08-21
	 */
	public void carTypeImport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		try{
		    logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request = act.getRequest();
			FileObject importFile = request.getParamObject("importFile");
//			String COMMAND = request.getParamValue("COMMAND");
			act.setForword("/jsp/systemMng/baseData/carTypeImprotTxt.jsp");
			if(importFile != null){ //json请求
				logger.debug("----command come on --");
				ReaderUtil txtReader = new ReaderUtil(importFile.getContent(),"\t");
				//执行导入报表的数据检查，检查通过则插入到数据库中，如果检查不通过，则将错误的信息返回
				act.setOutData("fileName", importFile.getFileName());
				act.setOutData("goback", "ok");
				insertDatabae(txtReader,act);
			}
		}catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.WRITE_CSV_FORMAT_FALSENESS);
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	/**
	 * 执行插入数据库
	 * author: wry
	 * @throws BizException 
	 * date: 2009-08-21
	 */
	private void insertDatabae(ReaderUtil txtReader,ActionContext act) throws Exception{
		//全局标识，如果为ture,则执行插入动作，如果为false，则只进行检查；
		boolean allFlag = true;
//		List<TmModelPkgPO> rightList = new LinkedList<TmModelPkgPO>();
//		TmModelPkgPO modelPkgPO = null;
		List<TmModelPO> modelList = factory.select("select model_Code,model_Id from TM_MODEL",null,new DAOCallback<TmModelPO>() {
			public TmModelPO wrapper(ResultSet rs, int idx){
				TmModelPO bean  = new TmModelPO();
				try{
					bean.setModelCode(rs.getString("model_Code"));
					bean.setModelId(rs.getLong("model_Id"));
					return bean;
				}catch(SQLException e){
					throw new DAOException(e);
				}
			}
		});
		VhclModelInfoBean vmBean = null;
		Date startDate = new Date();
		AclUserBean logonUser = null;
		try{
		    logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
//			JSONArray array = new JSONArray();
		    List array = new ArrayList();
			logger.debug("-----getColNum-----"+txtReader.getColNum());
			if(txtReader.getColNum()!=2){
				throw new BizException(act,ErrorCodeConstant.WRITE_CSV_FORMAT_FALSENESS,"");
			}
			logger.debug("-----getRowNum-----"+txtReader.getRowNum());
			for(int i=1;i<txtReader.getRowNum();i++){
				logger.debug("-----array.length()-----"+array.size());
				if(array.size()<Constant.errorNum){
					logger.debug("%%%%%%%%"+txtReader.getRow(i));
					String checkStr = checkDataOfRow(i,modelList,txtReader);
					if(checkStr !=null){
						allFlag = false;
						vmBean = new VhclModelInfoBean();
						vmBean.setModelCode(txtReader.getString(i, 0));
						vmBean.setStandardPkg(txtReader.getString(i, 1));
						vmBean.setRow(String.valueOf(i+1));
						vmBean.setMark(checkStr);
						//转化成json格式
//						JSONObject object = new JSONObject(vmBean);
						array.add(vmBean);
					}
					//如果发现有错误的特殊车辆信息，将不再进行数据库的插入。
					if(allFlag){
							Long  modelId= null;
							String modelCode = txtReader.getString(i, 0);
							for(TmModelPO model: modelList){
								if(model.getModelCode()!=null&&model.getModelCode().equals(modelCode)){
									modelId = model.getModelId();
									break;
								}
							}
							//		检查车型配置代码
							String standarPkg = txtReader.getString(i, 1);
//							TmModelPkgPO tmModeldPkgPO = new TmModelPkgPO();
//							tmModeldPkgPO.setModelId(modelId);
//							tmModeldPkgPO.setStandardPkg(standarPkg);
//							List<TmModelPkgPO> modelPkgList = factory.select(tmModeldPkgPO);
//							logger.debug("modelPkgList---"+modelPkgList.size());
							if(/**modelPkgList.size()*/ 0 == 0){
////								modelPkgPO = new TmModelPkgPO();
////								modelPkgPO.setModelPkgId(factory.getStringPK(modelPkgPO));
////								modelPkgPO.setModelId(modelId);
////								modelPkgPO.setStandardPkg(txtReader.getString(i, 1));
////								//初使化值
////								ActionUtil.setCreatePOTrack(modelPkgPO,logonUser);
////								ActionUtil.setUpatePOTrack(modelPkgPO,logonUser);
//////								modelPkgPO.setUpdateDate(null);
////								factory.insert(modelPkgPO);
//								//rightList.add(modelPkgPO);
								
							}
					}
				}else{
					break;
				}
			}
			//对数据检查完成之后，对数据进行处理，如果有错误，则将错误的特殊车辆信息返回到页面中
			//如果没有错误，则将数据插入到数据库中。
			if(!allFlag){
				logger.debug("message:"+array.toString());
				logger.debug("message:"+array.size());
				act.setOutData("errorList", array);
				act.setOutData("errorSize", array.size());
//				throw new BizException("error");
			}else{
				//factory.insert(rightList);
				Date endDate = new Date();
				act.setOutData("startDate", DateTimeUtil.parseDateToString(startDate));
				act.setOutData("endDate", DateTimeUtil.parseDateToString(endDate));
				act.setOutData("operator", ((AclUserBean)act.getSession().get(Constant.LOGON_USER)).getUserId());
			}
				act.setOutData("falt", "1");
		}catch(BizException e){
			throw e;
		}catch(Exception e){
			throw e;
		}
	}
	/**
	 * 检查数据的合法性
	 * author: wry
	 * @throws BizException 
	 * date: 2009-08-22
	 */
	private String checkDataOfRow(int row,List<TmModelPO> modelList,ReaderUtil textReader){
		String rowContent = textReader.getRow(row);
		String[] cols = rowContent.split("\t");
		String modelCode = cols[0].trim();
		String standarPkg = cols[1];
		StringBuffer sb = new StringBuffer();
		
		//检查车型代码
		boolean flag = false;
//		TmModelPO tmModeldPO =null;
		for(TmModelPO model:modelList){
			if(model.getModelCode()!=null&&(model.getModelCode().equals(modelCode))){
				flag = true;
//				tmModeldPO = model;
				break;
			}
		}
		if(!flag){
			String modelCodeStr ="您导入的车型代码不存在！";
			sb.append(modelCodeStr).append(";");
		}
//		检查车型配置代码
//		if(tmModeldPO!=null){
//			TmModelPkgPO tmModeldPkgPO = new TmModelPkgPO();
//			tmModeldPkgPO.setModelId(tmModeldPO.getModelId());
//			tmModeldPkgPO.setStandardPkg(standarPkg);
//			List<TmModelPkgPO> modelPkgList = factory.select(tmModeldPkgPO);
//			if(modelPkgList.size() > 0){
//				String modelStr ="您导入的该车型的配置描述已存在！";
//				sb.append(modelStr).append(";");
//			}
//		}
		if(CheckUtil.checkNull(standarPkg)){
			String noNull = "配置描述不能为空";
			sb.append(noNull).append(";");
		}
//		String[] content2 = null;
//		for(int i=0;i<textReader.getRowNum();i++){
//			if(i!=row){
//				content2 = textReader.getRow(i).split("\t");
//				if(modelCode.equals(content2[0])&&standarPkg.equals(content2[1])){
//					String modelStr ="您导入的该车型的配置描述已存在！";
//					sb.append(modelStr).append(";");
//				}
//			}
//		}
		//检查描述
//		String descStr =CheckUtil.checkDescAll(cols[1], 50, "配置描述", false);
//		if(descStr.length()>0){
//			sb.append(descStr).append(";");
//		}
		if(sb.length()>0){
			return sb.substring(0,(sb.length()-1)).toString();
		}else{
			return null;
		}
		
	}
	

}
