package com.infodms.dms.dao.moduleMng;

import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcFuncActionPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ModuleActionDAO extends BaseDao<PO>{

    public static Logger logger = Logger.getLogger(ModuleActionDAO.class);
    private static ModuleActionDAO dao = new ModuleActionDAO();
	public static final ModuleActionDAO getInstance() {
		return dao;
	}

	/**
	 * @FUNCTION : 模块操作查询实际执行
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-09-02
	 */
	public PageResult<Map<String, Object>> getModuleActionList(JSONObject paraObject, int pageSize, int curPage){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
        sql.append("select t1.ACTION_ID,t2.FUNC_ID,t2.FUNC_CODE,t2.FUNC_NAME,t1.ACTION_CODE,t1.ACTION_NAME,t1.PARA_CODE \n");
        sql.append("from TC_FUNC_ACTION t1,TC_FUNC t2 \n");
        sql.append("where t1.FUNC_ID=t2.FUNC_ID \n");        
		if (!paraObject.get("moduleName").equals("")){
			sql.append("and t2.FUNC_NAME like'%" + paraObject.get("moduleName") + "%'  \n");
		}
		sql.append("order by t2.FUNC_ID \n");
		logger.info(sql.toString());
		return dao.pageQuery(sql.toString(), params, getFunName() + System.currentTimeMillis(), pageSize, curPage);
	}	

	/**
	 * @FUNCTION : 模块操作保存实际执行
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-09-03
	 */
	public void ActionSave(AclUserBean logonUser,JSONObject dataObject){
		String actionId=dataObject.getString("ACTION_ID");
		TcFuncActionPO po=new TcFuncActionPO();
		TcFuncActionPO oldpo=new TcFuncActionPO();
		if(!actionId.equals("")){
		    po.setActionId(Long.parseLong(actionId));
		    oldpo.setActionId(Long.parseLong(actionId));
		}else{
			Map<String,Object> map=this.getMaxActionId(dataObject.getLong("MODULE_ID"));
			logger.info("-----------ACTION_ID="+map.get("ACTION_ID").toString());
			if(map==null||map.get("ACTION_ID").toString().equals("0")){
				po.setActionId(Long.parseLong(dataObject.getString("MODULE_ID")+"01"));
			}else{
				po.setActionId(Long.parseLong(map.get("ACTION_ID").toString())+1);
			}
			//po.setActionId(Long.parseLong(SequenceManager.getSequence("")));
		}			
		po.setActionCode(dataObject.getString("ACTION_CODE"));
		po.setActionName(dataObject.getString("ACTION_NAME"));
		po.setFuncId(dataObject.getLong("MODULE_ID"));
		po.setActionType(dataObject.getLong("ACTION_TYPE"));
		if(!actionId.equals("")){
		    po.setUpdateBy(logonUser.getUserId());
		    po.setUpdateDate(new Date());
		}else{
		    po.setCreateBy(logonUser.getUserId());
		    po.setCreateDate(new Date());				
		}
		if(!actionId.equals("")){			    
		    dao.update(oldpo,po);
		}else{
			dao.insert(po);
		}		
	}
	
	/**
	 * @FUNCTION : 模块操作查看
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-09-03
	 */
	public Map<String,Object> getViewData(String actionId){
		StringBuffer sql = new StringBuffer();
        sql.append("select t1.ACTION_ID,t2.FUNC_ID,t2.FUNC_NAME,t1.ACTION_CODE,t1.ACTION_NAME,t1.ACTION_TYPE \n");
        sql.append("from TC_FUNC_ACTION t1,TC_FUNC t2   \n");
        sql.append("where t1.FUNC_ID=t2.FUNC_ID and ACTION_ID="+actionId+"  \n");
        List<Map<String,Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		if(null==list||list.size()<=0||list.get(0)==null){
			return null;
		}
		return list.get(0);
	}	

	/**
	 * @FUNCTION : 根据模块ID获取最大ACTION_ID
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-10-30
	 */
	public Map<String,Object> getMaxActionId(Long moduleId){
		StringBuffer sql = new StringBuffer();
        sql.append("select nvl(max(ACTION_ID),0) as ACTION_ID \n");
        sql.append("from TC_FUNC_ACTION  \n");
        sql.append("where FUNC_ID="+moduleId+"  \n");
        List<Map<String,Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		if(null==list||list.size()<=0||list.get(0)==null){
			return null;
		}
		return list.get(0);
	}	
	
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
