/**********************************************************************
* <pre>
* FILE : ServiceActivityManageModel.java
* CLASS : ServiceActivityManageModel
*
* AUTHOR : PGM
*
* FUNCTION :服务活动管理---车型列表.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-17| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ServiceActivityManageModel.java,v 1.9 2011/12/14 02:38:27 zmw Exp $
 */
package com.infodms.dms.actions.claim.serviceActivity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageModelDao;
import com.infodms.dms.dao.productmanage.ProductManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtAsActivityMgroupPO;
import com.infodms.dms.po.TtAsWrModelGroupPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * Function       :  服务活动管理--车型列表
 * @author        :  PGM
 * CreateDate     :  2010-06-02
 * @version       :  0.1
 */
public class ServiceActivityManageModel {
	
	private Logger logger = Logger.getLogger(ServiceActivityManageModel.class);
	
	private ServiceActivityManageModelDao dao = ServiceActivityManageModelDao.getInstance();
	
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	
	private final String ServiceActivityModelInitUrl = "/jsp/claim/serviceActivity/serviceActivityModel.jsp";//查询页面
	
	private final String ServiceActivityModelSuccessUrl = "/jsp/claim/serviceActivity/serviceShowMaterialGroup.jsp";//查询页面
	
	@SuppressWarnings("unused")
	private ProductManageDao pdao = ProductManageDao.getInstance();
	/**
	 * Function       :  根据条件查询服务活动管理中符合条件的信息，其中包括：车型列表 
	 * @param         :  request-车系、活动ID
	 * @return        :  服务活动管理---车型列表
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-02
	 */
	@SuppressWarnings("unchecked")
	public void serviceActivityManageModelQuery(){
		
		RequestWrapper request = act.getRequest();
		
		String vehicleSeriesList = request.getParamValue("groupIdXi");//车系
		
		String cxzid = request.getParamValue("cxzid");
		
		String cxname = request.getParamValue("cxname");
		String type = request.getParamValue("type");
		if(type != null)
		{
			act.setOutData("type", type);
		}
		
		String activityId = request.getParamValue("activityId");//活动ID
		
		String flag = request.getParamValue("flag");
		
		try {
			List<TmVhclMaterialGroupPO> list = dao.serviceActivityGroupName(); //车系及子车型组ID
			//List<TtAsActivityBean> ManageModelList=dao.getAllserviceActivityManageModelInfo(vehicleSeriesList);//车辆性质
			
			List cxandcxzid = new ArrayList();
			
			for(TmVhclMaterialGroupPO tpo : list){				
				Map map = new HashMap();
				List wrids = dao.getShCxzidsByCxid(tpo.getGroupId());
				map.put("TmVhclMaterialGroupPO", tpo);
				map.put("CXZIDS", wrids);
				cxandcxzid.add(map);
			}
			
			List<TtAsWrModelGroupPO> cxzlist = dao.getCXZName(); //得到车型组集合
			
			List<TmVhclMaterialGroupPO> cxlist = dao.getCXName(); //得到车型集合
			
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			
			 PageResult<Map<String, Object>> ps = dao.getAllserviceActivityManageModelInfo(vehicleSeriesList,cxzid,cxname,curPage,Constant.PAGE_SIZE );
						
			//pdao.getModelGroup(ps.get, Constant.WR_MODEL_GROUP_TYPE_01);
			
			List<TtAsActivityBean> ModelList=dao.getAllserviceActivityManageModelList(activityId);//车辆性质回显
			
			StringBuffer permissions = new StringBuffer();//全选回显
			
			Iterator<TtAsActivityBean> it= ModelList.iterator();
			
			while(it.hasNext()){
				
				TtAsActivityBean mb=(TtAsActivityBean) it.next();
				
				if(it.hasNext()){
					
					permissions.append(mb.getMaterialGroupId()+",");
					
				}else{
					
					permissions.append(mb.getMaterialGroupId());
				}
			}
		   List listId=ps.getRecords();
		   
		   Object  id="";
		   
		   String ids="";
		   
		   if(listId!=null){
			   for(int i=0 ;i<listId.size();i++){
				   
				   Map map=(Map) listId.get(i);
				   
				   if(i==listId.size()-1){
					   
					   id= map.get("GROUP_ID");
					   
					   ids+=id;
					   
				   }else{
					   
				     id= map.get("GROUP_ID")+",";
				     
				     ids+=id;
				     
					   }
			   }
		   }
		   
			request.setAttribute("listSeries", vehicleSeriesList);
			
			request.setAttribute("list", cxandcxzid);
			
			request.setAttribute("cxzlist", cxzlist);
			
			request.setAttribute("cxlist", cxlist);
			
	        request.setAttribute("ModelList", permissions.toString());
	        
			request.setAttribute("activityId", activityId);
			
			act.setOutData("ids", permissions.toString());
			
			act.setOutData("ps", ps);
						
			act.setForword(ServiceActivityModelInitUrl);
			
		} catch(Exception e){
			
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理--车型列表");
			
			logger.error(logonUser,e1);
			
			act.setException(e1);
		 }
		}
	/**
	 * Function       :  增加服务活动管理信息
	 * @param         :  request---活动ID
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-02
	 */
	@SuppressWarnings("static-access")
	public void serviceActivityManageModelOption2(){
		try {
			RequestWrapper request = act.getRequest();
			String activityId =request.getParamValue("activityId");         //活动ID
			String groupIds =request.getParamValue("groupIds");            //groupId
			String flag = request.getParamValue("groupIdXi");
			TtAsActivityMgroupPO MgroupPO=new TtAsActivityMgroupPO();
			MgroupPO.setActivityId(Long.parseLong(activityId));
			String [] groupIdsArray = null;
			if (groupIds!=null&&!"".equals(groupIds)) {
				groupIdsArray = groupIds.split(",");             //取得所有groupIds放在数组中
				// 首先将此车系下的所有车型删除,然后再执行插入操作
				// 如果车系是请选择，刚先清空主表再插入当前选择项
//				if("1".equals(flag))
//					dao.delete(MgroupPO);
//				else
//					dao.deleteAllModels(groupIdsArray[0]);
				// 将此页所选中的代码全部插入活动车型表中
				Date date = new Date();
				Long userId = logonUser.getUserId();
				
				MgroupPO.setCreateBy(userId);
				MgroupPO.setCreateDate(date);
				for (int i = 0;i<groupIdsArray.length;i++) {
					String sql = " select * from tt_as_activity_mgroup g where g.activity_id = "+activityId+" and g.material_group_id = "+groupIdsArray[i] ;
					if(dao.select(sql,null,TtAsActivityMgroupPO.class).size()<=0){
						MgroupPO.setId(Long.parseLong(SequenceManager.getSequence("")));
						MgroupPO.setMaterialGroupId(Long.parseLong(groupIdsArray[i]));
						dao.insert(MgroupPO);
					}
				}
				act.setOutData("success", "true");
			}else{
				dao.serviceActivityManageModelOption(groupIdsArray,MgroupPO);
				//act.setRedirect(ServiceActivityModelSuccessUrl);
				act.setOutData("success", "true");
			}
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理车型列表信息增加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  增加服务活动管理信息
	 * @param         :  request---活动ID
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-02
	 */
	@SuppressWarnings("static-access")
	public void serviceActivityManageModelOption(){
		try {
			RequestWrapper request = act.getRequest();
			String activityId =request.getParamValue("activityId");         //活动ID
			String groupIds =request.getParamValue("groupIds");            //groupId
			TtAsActivityMgroupPO MgroupPO=new TtAsActivityMgroupPO();
			String [] groupIdsArray = null;
			if (groupIds!=null&&!"".equals(groupIds)) {
				groupIdsArray = groupIds.split(",");             //取得所有groupIds放在数组中
				MgroupPO.setActivityId(Long.parseLong(activityId));
				MgroupPO.setCreateBy(logonUser.getUserId());
				MgroupPO.setCreateDate(new Date());
				MgroupPO.setUpdateBy(logonUser.getUserId());
				MgroupPO.setUpdateDate(new Date());
				dao.serviceActivityManageModelOption(groupIdsArray,MgroupPO);
				//act.setRedirect(ServiceActivityModelSuccessUrl);
				act.setOutData("success", "true");
			}else{
				dao.serviceActivityManageModelOption(groupIdsArray,MgroupPO);
				//act.setRedirect(ServiceActivityModelSuccessUrl);
				act.setOutData("success", "true");
			}
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理车型列表信息增加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void serviceActivityManageModeljuede()
	{
		Long poseId=logonUser.getPoseId();
		RequestWrapper request = act.getRequest();
		try{
			String activityId = CommonUtils.checkNull(request.getParamValue("activityId"));	//回显输入框ID
			StringBuffer sql= new StringBuffer();
			sql.append("SELECT * from tt_as_activity_mgroup t where t.ACTIVITY_ID =" + activityId);
			if(dao.select(TtAsActivityMgroupPO.class, sql.toString(),null).size() == 0)
			{
				act.setOutData("ret", "fasle");
			}else
			{
				act.setOutData("ret", "true");
			}
			

			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"页面物料组树查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 页面物料组树查询(服务活动管理)
	 */
	public void serviceMaterialGroupQuery(){
		Long poseId=logonUser.getPoseId();
		RequestWrapper request = act.getRequest();
		try{
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组名称
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			String groupLevel1 = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组等级
			List<TmVhclMaterialGroupPO> list=dao.getMaterialGroupTree(poseId,groupLevel1);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("GROUPLEVEL", groupLevel1);
			act.setOutData("ISMULTI", isMulti);
			act.setOutData("list", list);
			//act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"页面物料组树查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 服务活动管理---物料组树查询结果
	 */
	public void serviceGroupListQuery(){
		try{
			RequestWrapper request = act.getRequest();
			String activityId=request.getParamValue("ACTIVITYID");				//活动ID
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));	//物料组ID
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));	//物料组代码
			String groupName = CommonUtils.checkNull(request.getParamValue("groupName"));	//物料组名称
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组等级
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps=dao.getGroupList(groupId,groupCode,groupName,groupLevel,curPage, Constant.PAGE_SIZE);
			
			List<TtAsActivityBean> ModelList=dao.getAllserviceActivityManageModelList(activityId);//车型回显
			
			StringBuffer permissions = new StringBuffer();//全选回显
			Iterator<TtAsActivityBean> it= ModelList.iterator();
			while(it.hasNext()){
				TtAsActivityBean mb=(TtAsActivityBean) it.next();
				if(it.hasNext()){
					permissions.append(mb.getMaterialGroupId()+",");
				}else{
					permissions.append(mb.getMaterialGroupId());
				}
			}
		   List listId=ps.getRecords();
		   Object  id="";
		   String ids="";
		   for(int i=0 ;i<listId.size();i++){
			   Map map=(Map) listId.get(i);
			   if(i==listId.size()-1){
				   id= map.get("GROUP_ID");
				   ids+=id;
			   }else{
			     id= map.get("GROUP_ID")+",";
			     ids+=id;
				   }
		   }
			request.setAttribute("permissions", permissions.toString());
			act.setOutData("ps", ps);
			act.setOutData("ids", ids);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("GROUPLEVEL", groupLevel);
			act.setOutData("ISMULTI", isMulti);
			act.setForword(ServiceActivityModelSuccessUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"页面物料组树查询结果");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//删除选定车型
	public void deleteThisModelId(){
		RequestWrapper request = act.getRequest();
		try{
			String id = request.getParamValue("id");
			String activityId = request.getParamValue("activityId");
			String sql = "delete from tt_as_activity_mgroup g where g.activity_id = "+activityId+" and g.material_group_id = "+id ;
			dao.delete(sql, null);
			List<TtAsActivityBean> ModelList=dao.getAllserviceActivityManageModelList(activityId);//车辆性质回显
			
			StringBuffer permissions = new StringBuffer();//全选回显
			
			Iterator<TtAsActivityBean> it= ModelList.iterator();
			
			while(it.hasNext()){
				
				TtAsActivityBean mb=(TtAsActivityBean) it.next();
				
				if(it.hasNext()){
					
					permissions.append(mb.getMaterialGroupId()+",");
					
				}else{
					
					permissions.append(mb.getMaterialGroupId());
				}
			}
			act.setOutData("flag", true);
			act.setOutData("ids",permissions.toString() );
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"页面物料组树查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}