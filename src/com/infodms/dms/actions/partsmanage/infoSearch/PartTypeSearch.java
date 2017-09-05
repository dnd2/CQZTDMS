package com.infodms.dms.actions.partsmanage.infoSearch;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.PartinfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.partinfo.PartinfoDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TmPtPartTypePO;
import com.infodms.dms.po.TtAsWrLabourPricePO;
import com.infodms.dms.po.TtAsWrModelGroupPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: PartInfoSearch.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-3
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class PartTypeSearch implements PTConstants {
	public Logger logger = Logger.getLogger(PartTypeSearch.class);
	

	
	public void partTypeEditInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(part_type_Url);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件大类信息初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	public void queryPartTypeSearch(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE")); //配件代码
			String partName = CommonUtils.checkNull(request.getParamValue("PART_NAME")); //配件名称
			String typeCode = CommonUtils.checkNull(request.getParamValue("PARTTYPE_CODE"));
			String typeId   = CommonUtils.checkNull(request.getParamValue("PARTTYPE_ID"));
			String isReturn = CommonUtils.checkNull(request.getParamValue("IS_RETURN"));   //是否回运
			System.out.println("LLLLLLLLLLLLLLLl: isReturn " + isReturn);
		  //String isMax    = request.getParamValue("IS_MAX");      //是否大件
			
			PartinfoDao dao = new PartinfoDao();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
					PageResult<Map<String, Object>> ps = dao.queryMainPartType(partCode,partName,typeId,isReturn, Constant.PAGE_SIZE, curPage) ;
			
			act.setOutData("ps", ps);			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件大类信息初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void partTypeAddInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(part_type_Add);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"新增配件大类信息初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 配件大类修改页面初始化
	 */
	public void partTypeUpdateInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			PartinfoDao dao = new PartinfoDao();
			String id = request.getParamValue("Id");			
			//配件大类信息
			TmPtPartTypePO dpo = new TmPtPartTypePO();
			dpo.setId(Long.parseLong(id));
			List<TmPtPartTypePO> dpoList = dao.select(dpo);
			if(dpoList.size()>0)
				act.setOutData("info", dpoList);
			
			//
			TmPtPartBasePO po = new TmPtPartBasePO();
			po.setPartTypeId(Long.parseLong(id));
			List<TmPtPartBasePO> lists = dao.select(po);
			act.setOutData("lists", lists);
			
			act.setForword(UPDATE_URL);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定->修改");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}	
/**
 * 配件信息弹出框
 * 
 */	
	public void queryPart(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(query_part);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"查询配件信息初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}	
	public void queryPartDialog(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.getResponse().setContentType("application/json");
			PartinfoDao dao = new PartinfoDao();
			String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE")); //配件代码
//			String partName = CommonUtils.checkNull(request.getParamValue("PART_NAME")); //配件名称

			StringBuffer con = new StringBuffer() ;
			if(StringUtil.notNull(partCode))
				con.append("and upper(part_code) like '%").append(partCode.toUpperCase()).append("%'\n") ;
			int pageSize = 30 ;
			int curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<TmPtPartBasePO> ps = dao.getPartInfo(con.toString(),pageSize, curPage) ;
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件基本信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: addPartType
	* @Description: TODO(更新配件大类信息) 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void addPartType() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			
			String typeCode = request.getParamValue("PARTTYPE_CODE");
			String typeName = request.getParamValue("PARTTYPE_NAME");
			String isReturn = request.getParamValue("IS_RETURN");   //是否回运
			String isMax    = request.getParamValue("IS_MAX");      //是否大件
			String[] partId = request.getParamValues("part_id");
			String id = request.getParamValue("id");
			TmPtPartTypePO po=null;
			TmPtPartTypePO po1=null;
			TmPtPartBasePO basepo= null;
			PartinfoDao dao = new PartinfoDao();
				if(StringUtil.notNull(typeCode)){
					po = new TmPtPartTypePO() ;
					po.setParttypeCode(typeCode);
					po1 = dao.existTypeCode(po);
					if(po1==null){
						po = new TmPtPartTypePO() ;
						po.setParttypeCode(typeCode);
						po.setParttypeName(typeName);
						po.setPartNum(new Long(partId.length));
						po.setIsMax(new Integer(isMax));
						po.setIsReturn(new Integer(isReturn));
						po.setOemCompanyId(logonUser.getCompanyId());
						po.setCreateBy(logonUser.getUserId());
						po.setCreateDate(new Date());
						po.setId(Utility.getLong(SequenceManager.getSequence("")));

						dao.insert(po);
						basepo = new TmPtPartBasePO();
						TmPtPartBasePO bpo2   = new TmPtPartBasePO();
						for(int i=0;i<partId.length;i++){
							bpo2.setPartId(new Long(partId[i]));
							basepo.setPartTypeId(po.getId());
							dao.update(bpo2, basepo);
						}
											
					}else{
						// 
						po.setUpdateBy(logonUser.getUserId());
						po.setUpdateDate(new Date());
						po.setParttypeName(typeName);
						po.setPartNum(new Long(partId.length));
						po.setIsMax(new Integer(isMax));
						po.setIsReturn(new Integer(isReturn));
						TmPtPartTypePO p1 = new TmPtPartTypePO() ;
						p1.setParttypeCode(typeCode);
						dao.update(p1, po);
						basepo = new TmPtPartBasePO();
						TmPtPartBasePO bpo2   = new TmPtPartBasePO();
						for(int i=0;i<partId.length;i++){
							bpo2.setPartId(new Long(partId[i]));
							basepo.setPartTypeId(Long.valueOf(id));
							dao.update(bpo2, basepo);
						}
					}
				}

			act.setForword(part_type_Url);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 新增配件大类");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void partCodeDel(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			act.getResponse().setContentType("application/json");
			String partId = request.getParamValue("partId");
			String idx = request.getParamValue("idx");
			PartinfoDao dao = new PartinfoDao();
			TmPtPartBasePO po = new TmPtPartBasePO();
			po.setPartTypeId(new Long(0));
			TmPtPartBasePO po1 = new TmPtPartBasePO();
			po1.setPartId(new Long(partId));
			dao.update(po1,po);
			
			act.setOutData("idx", idx);
			act.setOutData("flag", true);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE, "删除配件代码");
			logger.error(logonUser, be);
			act.setException(be);
			act.setOutData("flag", false);
		}
	}
		
}
