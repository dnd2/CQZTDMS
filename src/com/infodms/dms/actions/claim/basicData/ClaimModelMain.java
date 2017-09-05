/**   
* @Title: ClaimModelMain.java 
* @Package com.infodms.dms.actions.claim.basicData 
* @Description: TODO(车型组维护) 
* @author wangjinbao   
* @date 2010-6-4 上午08:50:26 
* @version V1.0   
*/
package com.infodms.dms.actions.claim.basicData;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.basicData.ClaimModelDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmGroupColorPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtAsWrModelGroupPO;
import com.infodms.dms.po.TtAsWrModelItemPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.dms.chana.actions.OSA11;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: ClaimModelMain 
 * @Description: TODO(车型组维护) 
 * @author wangjinbao 
 * @date 2010-6-4 上午08:50:26 
 *  
 */
public class ClaimModelMain {
	private Logger logger = Logger.getLogger(ClaimModelMain.class);
	private final ClaimModelDao dao = ClaimModelDao.getInstance();
	private final String CLAIM_MODEL_URL = "/jsp/claim/basicData/claimModelIndex.jsp";//主页面（查询）
	private final String CLAIM_MODEL_ADD_URL = "/jsp/claim/basicData/claimModelAdd.jsp";//增加页面
	private final String CLAIM_MODEL_MODIFY_URL = "/jsp/claim/basicData/claimModelModfiy.jsp";//修改页面
	private final String CLAIM_MODEL_DETAIL_URL = "/jsp/claim/basicData/claimModelDetail.jsp";//详细页面
	
	private final String MATERIAL_GROUP_MANAGE_QUERY_URL = "/jsp/claim/basicData/materialGroupManageQuery.jsp";// 物料组维护查询页面
	private final String MATERIAL_GROUP_MANAGE_ADD_URL = "/jsp/claim/basicData/materialGroupManageAdd.jsp";// 物料组维护新增页面
	private final String MATERIAL_GROUP_MANAGE_MOD_URL = "/jsp/claim/basicData/materialGroupManageMod.jsp";// 物料组维护修改页面
	
	/**
	 * 
	* @Title: claimModelInit 
	* @Description: TODO(车型组维护页面初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimModelInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(CLAIM_MODEL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车型组维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimModelQuery 
	* @Description: TODO(车型组维护查询) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimModelQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		List<Object> params = new LinkedList<Object>();
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				
				String wrgroupType = request.getParamValue("WRGROUP_TYPE");//车型组类型
				String wrgroupCode = request.getParamValue("WRGROUP_CODE");//车型组代码
				
				//拼sql的查询条件
				if (Utility.testString(wrgroupType)) {
					sb.append(" and tg.wrgroup_type = ? ");
					params.add(wrgroupType);
				}
				if (Utility.testString(wrgroupCode)) {
					sb.append(" and upper(tg.wrgroup_code) like ? ");
					params.add("%"+wrgroupCode.toUpperCase()+"%");
				}				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				
				PageResult<Map<String, Object>> ps = dao.claimModelQuery(Constant.PAGE_SIZE, curPage,sb.toString(),params,oemCompanyId);
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车型组维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: claimModelAddInit 
	* @Description: TODO(车型组维护新增初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimModelAddInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setOutData("list", dao.firstMainConditionQuery());
			act.setForword(CLAIM_MODEL_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车型组维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimModelAdd 
	* @Description: TODO(车型组维护新增) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimModelAdd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		TtAsWrModelGroupPO selpo = null;
		TtAsWrModelGroupPO addpo = null;
		String errorMsg = null;
		try {
			RequestWrapper request = act.getRequest();
			String wrgroupType = request.getParamValue("WRGROUP_TYPE");  //车型组类型
			String wrgroupCode = request.getParamValue("WRGROUP_CODE");  //车型组代码
			String wrgroupName = request.getParamValue("WRGROUP_NAME");  //车型组名称
			String free = request.getParamValue("FREE");  
			String labourPrice = request.getParamValue("LABOUR_PRICE");  
			String partPrice = request.getParamValue("PART_PRICE");  
			String textNewCarFee = request.getParamValue("textNewCarFee");  
			String selFirstMainCondition = request.getParamValue("selFirstMainCondition");  
			
			selpo = new TtAsWrModelGroupPO();
			selpo.setWrgroupCode(wrgroupCode);
			selpo.setWrgroupType(Integer.valueOf(wrgroupType));
			//判断是否存在输入的车型组代码
			List list = dao.select(selpo);
			if(list !=null && list.size() >0){//存在
				errorMsg = wrgroupCode;
				act.setOutData("error", errorMsg);
			}else{//不存在新增一条车型组记录
				addpo = new TtAsWrModelGroupPO();
				addpo.setCreateBy(logonUser.getUserId());
				addpo.setCreateDate(new Date());
				addpo.setWrgroupCode(wrgroupCode);
				addpo.setWrgroupName(wrgroupName);
				addpo.setFree(Double.parseDouble(free));
				addpo.setLabourPrice(Double.parseDouble(labourPrice));
				addpo.setPartPrice(Double.parseDouble(partPrice));
				addpo.setNewCarFee(Double.parseDouble(textNewCarFee));
				addpo.setQamaintainId(Long.parseLong(selFirstMainCondition));
				addpo.setWrgroupType(Constant.WR_MODEL_GROUP_TYPE_01);
				addpo.setWrgroupType(new Integer(wrgroupType));
				addpo.setWrgroupId(Long.parseLong(SequenceManager.getSequence("")));
				addpo.setOemCompanyId(oemCompanyId);
				dao.insert(addpo);
				act.setOutData("success", "true");
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车型组维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: 
	* @Description: TODO(修改车型组初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimModelUpdateInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String wrgroupId = request.getParamValue("WRGROUP_ID");  //车型组id
			TtAsWrModelGroupPO selpo = new TtAsWrModelGroupPO();
			TtAsWrModelGroupPO listpo = new TtAsWrModelGroupPO();
			selpo.setWrgroupId(new Long(wrgroupId));
			List list = dao.select(selpo);
			if(list != null && list.size() > 0){
				listpo = (TtAsWrModelGroupPO)list.get(0);
			}
			
			act.setOutData("list", dao.firstMainConditionQuery());
			act.setOutData("QamaintainId", listpo.getQamaintainId()) ;
			act.setOutData("MODELGROUP", listpo);//存储要修改的po
			act.setForword(CLAIM_MODEL_MODIFY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车型组维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimModelUpdate 
	* @Description: TODO(车型组修改) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimModelUpdate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String wrgroupId = request.getParamValue("WRGROUP_ID");  //车型组id
			String wrgroupType = request.getParamValue("WRGROUP_TYPE");  //车型组类型
			String wrgroupName = request.getParamValue("WRGROUP_NAME");  //车型组名称	
			String free = request.getParamValue("FREE");
			String labourPrie = request.getParamValue("LABOUR_PRICE");
			String partPrice = request.getParamValue("PART_PRICE");
			
			String textNewCarFee = request.getParamValue("textNewCarFee");  
			String selFirstMainCondition = request.getParamValue("selFirstMainCondition");  
			
			TtAsWrModelGroupPO selpo = new TtAsWrModelGroupPO();
			TtAsWrModelGroupPO updatepo = new TtAsWrModelGroupPO();
			selpo.setWrgroupId(new Long(wrgroupId));
			updatepo.setWrgroupType(new Integer(wrgroupType));
			updatepo.setWrgroupName(wrgroupName);
			updatepo.setUpdateBy(logonUser.getUserId());
			updatepo.setUpdateDate(new Date());
			updatepo.setFree(Double.valueOf(free));
			updatepo.setLabourPrice(Double.valueOf(labourPrie));
			updatepo.setPartPrice(Double.valueOf(partPrice));
			updatepo.setNewCarFee(Double.parseDouble(textNewCarFee));
			updatepo.setQamaintainId(Long.parseLong(selFirstMainCondition));
			dao.claimModelUpdate(selpo,updatepo);
			act.setOutData("ACTION_RESULT","1");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车型组维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimModelDetail 
	* @Description: TODO(车型组对应的车型详细信息) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimModelDetail() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String wrgroupId = request.getParamValue("WRGROUP_ID");
			String wrgroupType = request.getParamValue("WRGROUP_TYPE");
			List addlist =  dao.getClaimModel(new Long(wrgroupId),new Integer(wrgroupType));
			
			act.setOutData("ADDLIST", addlist);//车型组里的车型详细信息
			act.setForword(CLAIM_MODEL_DETAIL_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"车型组维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	/**
	 * 
	* @Title: claimModelSend 
	* @Description: TODO(车型组下发) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimModelSend(){
		
	}
	// 售后物料组维护pre
	public void materialGroupManageQueryPre() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			//查询参数传 递
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCodeArg"));
			String groupName = java.net.URLDecoder.decode(CommonUtils.checkNull(request.getParamValue("groupNameArg")),"utf-8");
			String parentGroupCode = CommonUtils.checkNull(request.getParamValue("parentGroupCodeArg"));
			String status = CommonUtils.checkNull(request.getParamValue("statusArg"));
			
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));
			//查询参数据传递
			act.setOutData("groupCode", groupCode);
			act.setOutData("groupName", groupName);
			act.setOutData("status", status);
			act.setOutData("parentGroupCode", parentGroupCode);
			act.setOutData("curPage", curPage);
			act.setForword(MATERIAL_GROUP_MANAGE_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料组维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	// 物料组维护查询
	public void materialGroupManageQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String groupName = java.net.URLDecoder.decode(CommonUtils.checkNull(request.getParamValue("groupName")),"utf-8");
			String parentGroupCode = CommonUtils.checkNull(request.getParamValue("parentGroupCode"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			//新增过滤条件是否在产 2012-05-08 hxy
			String forcast_flag = CommonUtils.checkNull(request.getParamValue("forcast_flag"));
			String companyId = logonUser.getCompanyId().toString();
			String ifType = request.getParamValue("IF_TYPE");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("groupCode", groupCode);
			map.put("groupName", groupName);
			map.put("parentGroupCode", parentGroupCode);
			map.put("status", status);
			map.put("companyId", companyId);
			map.put("ifType", ifType);
			map.put("forcast_flag", forcast_flag);
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao
					.getMaterialGroupManageQueryList(map, curPage,
							Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料组维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	// 物料组维护修改pre
	public void materialGroupManageModPre() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long companyId = logonUser.getCompanyId();
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String groupName = java.net.URLDecoder.decode(CommonUtils.checkNull(request.getParamValue("groupName")),"utf-8");
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String parentGroupCode = CommonUtils.checkNull(request.getParamValue("parentGroupCode"));
			String page = CommonUtils.checkNull(request.getParamValue("page"));
			TmVhclMaterialGroupPO po = new TmVhclMaterialGroupPO();
			po.setGroupId(new Long(groupId));
			po = dao.getTmVhclMaterialGroupPO(po);

			TmVhclMaterialGroupPO parent = new TmVhclMaterialGroupPO();
			parent.setGroupId(po.getParentGroupId());
			parent = dao.getTmVhclMaterialGroupPO(parent);
			
			TmGroupColorPO colorPOValue = new TmGroupColorPO();
			if(po!=null){
				TmGroupColorPO colorPO = new TmGroupColorPO();
				colorPO.setGroupId(Long.valueOf(groupId));
				List<PO> list = dao.select(colorPO);
				if(list!=null && list.size()>0){
					colorPOValue = (TmGroupColorPO)list.get(0);
				}
			}
			if(colorPOValue!=null){
				act.setOutData("colorId", colorPOValue.getColorId());
				act.setOutData("colorCode", colorPOValue.getColorCode());
				act.setOutData("colorName", colorPOValue.getColorName());
			}

			// 判断物料级别是否是车型
			if (po.getGroupLevel().intValue() == 4) {
				Map<String, Object> group1 = dao.getModelGroup(po.getGroupId(),
						Constant.WR_MODEL_GROUP_TYPE_01);// 索赔工时车型组
				Map<String, Object> group2 = dao.getModelGroup(po.getGroupId(),
						Constant.WR_MODEL_GROUP_TYPE_02);// 配件车型组
				act.setOutData("group1", group1);
				act.setOutData("group2", group2);
			}

			List<Map<String, Object>> groups1 = dao.getModelGroupList(
					Constant.WR_MODEL_GROUP_TYPE_01, companyId);// 索赔工时车型组
			List<Map<String, Object>> groups2 = dao.getModelGroupList(
					Constant.WR_MODEL_GROUP_TYPE_02, companyId);// 配件车型组
			
			List<Map<String, Object>> colorList = dao.getMaterialGroupColor(groupId);
			
			act.setOutData("po", po);
			act.setOutData("parent", parent);
			act.setOutData("groups1", groups1);
			act.setOutData("groups2", groups2);
			act.setOutData("colorList", colorList);
			//查询参数据传递
			act.setOutData("groupCode", groupCode);
			act.setOutData("groupName", groupName);
			act.setOutData("status", status);
			act.setOutData("parentGroupCode", parentGroupCode);
			act.setOutData("page", page);
			act.setForword(MATERIAL_GROUP_MANAGE_MOD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料组维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	// 物料组维护新增pre
	public void materialGroupManageAddPre() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long companyId = logonUser.getCompanyId();
			List<Map<String, Object>> groups1 = dao.getModelGroupList(
					Constant.WR_MODEL_GROUP_TYPE_01, companyId);// 索赔工时车型组
			List<Map<String, Object>> groups2 = dao.getModelGroupList(
					Constant.WR_MODEL_GROUP_TYPE_02, companyId);// 配件车型组

			act.setOutData("groups1", groups1);
			act.setOutData("groups2", groups2);
			act.setForword(MATERIAL_GROUP_MANAGE_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料组维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	// 物料组维护新增
	public void materialGroupManageAdd() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String groupName = CommonUtils.checkNull(request.getParamValue("groupName"));
			String parentGroupCode = CommonUtils.checkNull(request.getParamValue("parentGroupCode"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String ifType=request.getParamValue("ifType");
			
			String colorCode[] = request.getParamValues("colorCode") == null ? new String[] {} : request.getParamValues("colorCode");
			String colorName[] = request.getParamValues("colorName") == null ? new String[] {} : request.getParamValues("colorName");

			// 获得上级物料组
			TmVhclMaterialGroupPO parent = new TmVhclMaterialGroupPO();
			parent.setGroupCode(parentGroupCode);
			parent = dao.getTmVhclMaterialGroupPO(parent);

			// 物料组保存
			long groupId = new Long(SequenceManager.getSequence(""));
			TmVhclMaterialGroupPO po = new TmVhclMaterialGroupPO();
			po.setGroupId(groupId);
			po.setGroupCode(groupCode);
			po.setGroupName(groupName);
			if (!parentGroupCode.equals("")) {
				po.setGroupLevel(new Integer(parent.getGroupLevel().intValue() + 1));
				String treeCode = dao.getMaterialGroupTreeCode(parent.getGroupId().toString());
				po.setTreeCode(treeCode);
				po.setParentGroupId(parent.getGroupId());
			} else {
				String treeCode = dao.getMaterialGroupTreeCode("");
				po.setTreeCode(treeCode);
				po.setParentGroupId(-1L);
				po.setGroupLevel(new Integer(1));
			}
			if(ifType.equals(Constant.IF_TYPE_YES.toString())){
				po.setForcastFlag(1);
			}else{
				po.setForcastFlag(0);
			}
			po.setStatus(new Integer(status));
			po.setCreateDate(new Date());
			po.setCreateBy(logonUser.getUserId());
			po.setCompanyId(logonUser.getCompanyId());
			po.setIfStatus(new Integer(0));// 接口状态

			dao.insert(po);
			
			if(colorCode.length>0){
				dao.deleteColorByGroupId(String.valueOf(groupId));
				for (int i = 0; i < colorCode.length; i++) {
						TmGroupColorPO colorPOValue = new TmGroupColorPO();
						colorPOValue.setColorId(new Long(SequenceManager.getSequence("")));
						colorPOValue.setGroupId(groupId);
						colorPOValue.setColorCode(colorCode[i]);
						colorPOValue.setColorName(colorName[i]);
						colorPOValue.setCreateBy(logonUser.getUserId());
						colorPOValue.setCreateDate(new Date(System.currentTimeMillis()));
						colorPOValue.setUpdateBy(logonUser.getUserId());
						colorPOValue.setUpdateDate(new Date(System.currentTimeMillis()));
						dao.insert(colorPOValue); 
				}
			}
			
			// 判断上级物料组是否是车系
			if (parent != null && parent.getGroupLevel().intValue() == 3) {
				String modelGroup1 = CommonUtils.checkNull(request.getParamValue("modelGroup1"));
				String modelGroup2 = CommonUtils.checkNull(request.getParamValue("modelGroup2"));

				// 索赔工时车型组车型关系保存
				TtAsWrModelItemPO itemPo = new TtAsWrModelItemPO();
				itemPo.setModelId(po.getGroupId());
				itemPo.setWrgroupId(new Long(modelGroup1));
				itemPo.setCreateDate(new Date());
				itemPo.setCreateBy(logonUser.getUserId());
				dao.insert(itemPo);

				// 配件车型组车型关系保存
				if(modelGroup2 != null && !"".equals(modelGroup2)) {
					itemPo = new TtAsWrModelItemPO();
					itemPo.setModelId(po.getGroupId());
					itemPo.setWrgroupId(new Long(modelGroup2));
					itemPo.setCreateDate(new Date());
					itemPo.setCreateBy(logonUser.getUserId());
					dao.insert(itemPo);
				}
			}
			/* 此处需调用接口 
			OSA11 os = new OSA11();
			os.execute(po);//接口*/
			act.setOutData("returnValue", 1);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料组维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	// 物料组维护修改
	public void materialGroupManageMod() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String groupName = CommonUtils.checkNull(request.getParamValue("groupName"));
			String parentGroupCode = CommonUtils.checkNull(request.getParamValue("parentGroupCode"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String ifType=request.getParamValue("ifType");
			
			String colorCode[] = request.getParamValues("colorCode") == null ? new String[] {} : request.getParamValues("colorCode");
			String colorName[] = request.getParamValues("colorName") == null ? new String[] {} : request.getParamValues("colorName");

			TmVhclMaterialGroupPO parent = new TmVhclMaterialGroupPO();
			parent.setGroupCode(parentGroupCode);
			parent = dao.getTmVhclMaterialGroupPO(parent);

			List<Map<String,Object>> groupList = dao.getAllMaterialGroup(groupId);
			if (null != groupList && groupList.size()>0) {
				for (int i = 0; i < groupList.size(); i++) {
					String group_Id = String.valueOf(groupList.get(i).get("GROUP_ID"));
					TmVhclMaterialGroupPO tempPO = new TmVhclMaterialGroupPO();
					tempPO.setGroupId(Long.parseLong(group_Id));
					
					TmVhclMaterialGroupPO valuePO = new TmVhclMaterialGroupPO();
					valuePO.setStatus(Integer.parseInt(status));
					if(ifType.equals(Constant.IF_TYPE_YES.toString())){
						valuePO.setForcastFlag(1);
					}else{
						valuePO.setForcastFlag(0);
					}
					dao.update(tempPO, valuePO);
				}
			}
			
			TmVhclMaterialGroupPO condition = new TmVhclMaterialGroupPO();
			condition.setGroupId(new Long(groupId));

			
			TmVhclMaterialGroupPO value = new TmVhclMaterialGroupPO();
			//value.setGroupCode(groupCode);
			value.setGroupName(groupName);
			
			
			if (!parentGroupCode.equals("")) {
				value.setGroupLevel(new Integer(parent.getGroupLevel().intValue() + 1));
				String treeCode = dao.getMaterialGroupTreeCode(parent.getGroupId().toString());
				value.setTreeCode(treeCode);
				value.setParentGroupId(parent.getGroupId());
			} else {
				String treeCode = dao.getMaterialGroupTreeCode("");
				value.setTreeCode(treeCode);
				value.setGroupLevel(new Integer(1));
				value.setParentGroupId(null);
			}
			value.setStatus(new Integer(status));
			value.setIfStatus(new Integer(0));// 接口状态
			value.setUpdateDate(new Date());
			value.setUpdateBy(logonUser.getUserId());
			if(ifType.equals(Constant.IF_TYPE_YES.toString())){
				value.setForcastFlag(1);
			}else{
				value.setForcastFlag(0);
			}
			dao.update(condition, value);
			
			if(colorCode.length>0){
				dao.deleteColorByGroupId(groupId);
				for (int i = 0; i < colorCode.length; i++) {
						TmGroupColorPO colorPOValue = new TmGroupColorPO();
						colorPOValue.setColorId(new Long(SequenceManager.getSequence("")));
						colorPOValue.setGroupId(Long.parseLong(groupId));
						colorPOValue.setColorCode(colorCode[i]);
						colorPOValue.setColorName(colorName[i]);
						colorPOValue.setCreateBy(logonUser.getUserId());
						colorPOValue.setCreateDate(new Date(System.currentTimeMillis()));
						colorPOValue.setUpdateBy(logonUser.getUserId());
						colorPOValue.setUpdateDate(new Date(System.currentTimeMillis()));
						dao.insert(colorPOValue); 
				}
			}
			

			// 删除车型组车型关系
			TtAsWrModelItemPO itemPo = new TtAsWrModelItemPO();
			itemPo.setModelId(new Long(groupId));
			dao.delete(itemPo);

			// 判断上级物料组是否是车系
			if (parent != null && parent.getGroupLevel().intValue() == 3) {
				String modelGroup1 = CommonUtils.checkNull(request.getParamValue("modelGroup1"));
				String modelGroup2 = CommonUtils.checkNull(request.getParamValue("modelGroup2"));

				// 索赔工时车型组车型关系保存
				itemPo = new TtAsWrModelItemPO();
				itemPo.setModelId(new Long(groupId));
				itemPo.setWrgroupId(new Long(modelGroup1));
				itemPo.setCreateDate(new Date());
				itemPo.setCreateBy(logonUser.getUserId());
				dao.insert(itemPo);
			}

			/* 此处需调用接口 */
//			OSA11 os = new OSA11();
//			os.execute(value);//接口
			act.setOutData("returnValue", 1);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.UPDATE_FAILURE_CODE, "物料组维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
		
		//========================================================================lj 2015-4-14
//		dao.insertTtAsWrLabourPrice(logonUser,request);
	}

	// 物料组维护查询
	public void getModelGroup() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long companyId = logonUser.getCompanyId();
			String groupCode = CommonUtils.checkNull(request
					.getParamValue("groupCode"));

			TmVhclMaterialGroupPO group = new TmVhclMaterialGroupPO();
			group.setGroupCode(groupCode);
			List<PO> list = dao.select(group);
			group = list.size() == 0 ? null : (TmVhclMaterialGroupPO) list
					.get(0);

			// 判断上级物料组是否是车系
			if (group != null && group.getGroupLevel().intValue() == 3) {
				List<Map<String, Object>> groups1 = dao.getModelGroupList(
						Constant.WR_MODEL_GROUP_TYPE_01, companyId);// 索赔工时车型组
				List<Map<String, Object>> groups2 = dao.getModelGroupList(
						Constant.WR_MODEL_GROUP_TYPE_02, companyId);// 配件车型组
				act.setOutData("returnValue", 1);
				act.setOutData("groups1", groups1);
				act.setOutData("groups2", groups2);
			} else {
				act.setOutData("returnValue", 2);
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料组维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
}
