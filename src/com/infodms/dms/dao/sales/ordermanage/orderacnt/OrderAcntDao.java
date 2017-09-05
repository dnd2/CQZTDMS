package com.infodms.dms.dao.sales.ordermanage.orderacnt;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtil;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class OrderAcntDao  extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(OrderAcntDao.class);
	private static final OrderAcntDao dao = new OrderAcntDao();

	public static final OrderAcntDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public PageResult<Map<String, Object>> getOrderAcntQuery(String orderAcnt,String largeArea,String acntPose, String companyId,int curPage, int pageSize) throws Exception {


		StringBuffer sql = new StringBuffer();

		sql.append("SELECT  DISTINCT TU.USER_ID,               \n");
		sql.append("                TOO.ORDER_ORG_ID,          \n");
        sql.append("                TU.ACNT,          \n");
        sql.append("                TU.PASSWORD,      \n");
        sql.append("                TU.NAME,          \n");
        sql.append("                TU.USER_STATUS,   \n");
       /* sql.append("                TP.POSE_ID,       \n");
        sql.append("                TP.POSE_CODE,     \n");
        sql.append("                TP.POSE_NAME,     \n");
        sql.append("                TP.POSE_TYPE,     \n");*/
        sql.append("                TOO.ORG_ID,       \n");
        sql.append("                VVO.root_org_code,\n");
        sql.append("                VVO.root_org_name \n");
        sql.append("  FROM TC_USER TU,TM_ORDER_ORG TOO,VW_ORG_DEALER_ALL_NEW VVO \n");    
        sql.append(" WHERE 1=1 \n");   
        //sql.append("   AND TP.POSE_ID = TUP.POSE_ID \n");  
        sql.append("   AND TOO.USER_ID=TU.USER_ID \n");  
        sql.append("   AND TOO.ORG_ID=VVO.root_org_id \n");  
        if(orderAcnt!=null&&!"".equals(orderAcnt)){
        	sql.append("  AND TU.ACNT LIKE '%"+orderAcnt+"%' \n");
        }
        if(largeArea!=null&&!"".equals(largeArea)){
        	sql.append("  AND VVO.root_org_name LIKE '%"+largeArea+"%' \n");
        }
        /*if(acntPose!=null&&!"".equals(acntPose)){
        	sql.append("  AND TP.POSE_NAME '%"+acntPose+"%' \n");
        }*/
        sql.append("   AND TU.USER_STATUS = "+Constant.STATUS_ENABLE+" \n");  
        sql.append("   AND TU.COMPANY_ID = '"+companyId+"' \n");  
        

		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> getOrderAcntAddQuery(String orderAcnt,String acntName, String companyId,int curPage, int pageSize) throws Exception {


		StringBuffer sql = new StringBuffer();

		sql.append("SELECT DISTINCT TU.USER_ID,                      \n");
        sql.append("                TU.ACNT,                         \n");
        sql.append("                TU.PASSWORD,                     \n");
        sql.append("                TU.NAME,                         \n");
        sql.append("                TU.USER_STATUS                 \n");
       /* sql.append("                TP.POSE_ID,                      \n");
        sql.append("                TP.POSE_CODE,                    \n");
        sql.append("                TP.POSE_NAME,                    \n");
        sql.append("                TP.POSE_TYPE                     \n");*/
        sql.append("  FROM TC_USER TU \n");
        sql.append(" WHERE 1=1                 \n");
        //sql.append("   AND TP.POSE_ID = TUP.POSE_ID                  \n");
        if(orderAcnt!=null&&!"".equals(orderAcnt)){
        	sql.append("  AND TU.ACNT LIKE '%"+orderAcnt+"%' \n");
        }
        if(acntName!=null&&!"".equals(acntName)){
        	sql.append("  AND TU.NAME LIKE '%"+acntName+"%' \n");
        }
        sql.append("   AND TU.USER_STATUS = "+Constant.STATUS_ENABLE+" \n");  
        sql.append("   AND TU.COMPANY_ID = '"+companyId+"' \n");  
        

		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> getOrderLargeAddQuery(String largeId,String largeName,String largeCode,String companyId,int curPage, int pageSize) throws Exception {


		StringBuffer sql = new StringBuffer();

		sql.append("select * from tm_org where org_type="+Constant.ORG_TYPE_OEM+" and duty_type="+Constant.DUTY_TYPE_LARGEREGION+" \n");
      
        if(largeName!=null&&!"".equals(largeName)){
        	sql.append("  AND ORG_NAME LIKE '%"+largeName+"%' \n");
        }
        
        if(largeCode!=null&&!"".equals(largeCode)){
        	sql.append("  AND ORG_CODE LIKE '%"+largeCode+"%' \n");
        }
        if(largeId!=null&&!"".equals(largeId)){
        	sql.append("  AND ORG_ID NOT IN ("+largeId+") \n");
        }
        
        sql.append("   AND STATUS = "+Constant.STATUS_ENABLE+" \n");  
        sql.append("   AND PARENT_ORG_ID = '"+companyId+"' \n");  
        

		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public Map<String, Object> getOrderLargeAddMap(String largeId,String companyId) throws Exception {


		StringBuffer sql = new StringBuffer();

		sql.append("select * from tm_org where org_type="+Constant.ORG_TYPE_OEM+" and duty_type="+Constant.DUTY_TYPE_LARGEREGION+" \n");
      
        if(largeId!=null&&!"".equals(largeId)){
        	sql.append("  AND ORG_ID  IN ("+largeId+")\n");
        }
        
        sql.append("   AND STATUS = "+Constant.STATUS_ENABLE+" \n");  
        sql.append("   AND PARENT_ORG_ID = '"+companyId+"' \n");  
        

		Map<String, Object> ps = dao.pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	
	public List<Map<String, Object>> getOrderLargeByQuery(String userId,String companyId) throws Exception {


		StringBuffer sql = new StringBuffer();
/*
		sql.append("SELECT  DISTINCT TU.USER_ID,               \n");
        sql.append("                TU.ACNT,          \n");
        sql.append("                TU.PASSWORD,      \n");
        sql.append("                TU.NAME,          \n");
        sql.append("                TU.USER_STATUS,   \n");
        sql.append("                TP.POSE_ID,       \n");
        sql.append("                TP.POSE_CODE,     \n");
        sql.append("                TP.POSE_NAME,     \n");
        sql.append("                TP.POSE_TYPE,     \n");
        sql.append("                TOO.ORG_ID,       \n");
        sql.append("                VVO.root_org_code,\n");
        sql.append("                VVO.root_org_name \n");
        sql.append("  FROM TC_USER TU, TC_POSE TP, TR_USER_POSE TUP,TM_ORDER_ORG TOO,VW_ORG_DEALER_ALL_NEW VVO \n");    
        sql.append(" WHERE TU.USER_ID = TUP.USER_ID \n");   
        sql.append("   AND TP.POSE_ID = TUP.POSE_ID \n");  
        sql.append("   AND TOO.USER_ID=TU.USER_ID \n");  
        sql.append("   AND TOO.ORG_ID=VVO.root_org_id \n"); */ 
        sql.append("  SELECT * FROM TM_ORG WHERE ORG_ID IN (SELECT ORG_ID FROM TM_ORDER_ORG WHERE STATUS="+Constant.STATUS_ENABLE+" AND USER_ID="+userId+") and  duty_type="+Constant.DUTY_TYPE_LARGEREGION+"\n");
        sql.append("   AND COMPANY_ID = '"+companyId+"' \n");  
        

		List<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	

	
	
}
