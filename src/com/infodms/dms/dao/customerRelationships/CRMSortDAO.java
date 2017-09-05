package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class CRMSortDAO  extends BaseDao{

    public static Logger logger = Logger.getLogger(CRMSortDAO.class);
    private static CRMSortDAO dao = new CRMSortDAO();
	public static final CRMSortDAO getInstance() {
		return dao;
	}

	/**
	 * @FUNCTION : 坐席排班查询实际执行
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
	public PageResult<Map<String, Object>> getMainList(JSONObject paraObject,String WT_TYPE, int pageSize, int curPage){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("select SS_ID,to_char(DUTY_DATE,'yyyy-mm-dd') as DUTY_DATE,t2.USER_ID,t2.ACNT,t2.NAME,t1.WT_TYPE,t1.SHIFT_KIND,t1.SHIFT_KIND_DESC,t1.STATUS,t3.NAME as SS_BY,to_char(t1.SS_DATE,'yyyy-mm-dd') as SS_DATE,to_char(t1.UPDATE_DATE,'yyyy-mm-dd') as UPDATE_DATE,t4.NAME UPDATENAME  \n");
		sql.append("from TT_CRM_SORT_SHIFT t1   ,TC_USER t2,TC_USER t3,TC_USER t4  \n");
		sql.append("where t1.USER_ID=t2.USER_ID and t1.SS_BY=t3.USER_ID and t1.UPDATE_BY =t4.USER_ID(+)   \n");
        if(!paraObject.get("ACNT").equals("")){
        	sql.append("and t2.ACNT like'%"+paraObject.get("ACNT")+"%'  \n");
        }
        if(!paraObject.get("ACNT2").equals("")){
        	sql.append("and t2.ACNT  = '"+paraObject.get("ACNT2")+"'  \n");
        }        
		if (!paraObject.get("checkSDate").equals("")){
			sql.append("and trunc(t1.DUTY_DATE)>=to_date('" + (String) paraObject.get("checkSDate") + "','yyyy-mm-dd')  \n");
		}
		if (!paraObject.get("checkEDate").equals("")){
			sql.append("and trunc(t1.DUTY_DATE)<=to_date('" + (String) paraObject.get("checkEDate") + "','yyyy-mm-dd')  \n");
		}
		if (!paraObject.get("STATUS").equals("0")){
			sql.append("and t1.STATUS="+paraObject.get("STATUS")+"  \n");
		}	
		
		if (Utility.testString(WT_TYPE)  ){
			sql.append("and t1.WT_TYPE=   \n" + WT_TYPE);
		}
		
		
		sql.append("   order by trunc(t1.DUTY_DATE), t2.ACNT \n");
		logger.info(sql.toString());
		return dao.pageQuery(sql.toString(), params, getFunName() + System.currentTimeMillis(), pageSize, curPage);
	}
	
	/**
	 * @FUNCTION : 坐席排班查看
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
	public Map<String,Object> getViewData(String ssId){
		StringBuffer sql = new StringBuffer();
        sql.append("select SS_ID,to_char(DUTY_DATE,'yyyy-mm-dd') as DUTY_DATE,t2.USER_ID,t2.ACNT,t2.NAME,WT_TYPE,SHIFT_KIND,SHIFT_KIND_DESC,STATUS,SS_BY,to_char(SS_DATE,'yyyy-mm-dd') as SS_DATE    \n");
        sql.append("from TT_CRM_SORT_SHIFT t1,TC_USER t2  \n");
        sql.append("where t1.USER_ID=t2.USER_ID and t1.SS_ID="+ssId+"  \n");
        List<Map<String,Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		if(null==list||list.size()<=0||list.get(0)==null){
			return null;
		}
		return list.get(0);
	}
	
	/**
	 * @FUNCTION : 坐席在排班日期中是否存在
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
	public Map<String,Object> getIsSort(long userId, String dutyDate){
		StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(1) RES FROM tt_crm_sort_shift t WHERE t.user_id = "+userId+" AND trunc(t.duty_date) = to_Date('"+dutyDate+"','yyyy-mm-dd') and t.status = "+Constant.STATUS_ENABLE+"\n");
        List<Map<String,Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		if(null==list||list.size()<=0||list.get(0)==null){
			return null;
		}
		return list.get(0);
	}
	
	/**
	 * @FUNCTION : 通过用户ID得到用户名字
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
	public Map<String,Object> getUserName(long userId){
		StringBuffer sql = new StringBuffer();
        sql.append("SELECT t.se_name FROM tt_crm_seats t WHERE t.se_user_id =  "+userId+"\n");
        List<Map<String,Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		if(null==list||list.size()<=0||list.get(0)==null){
			return null;
		}
		return list.get(0);
	}


	/**
	 * @FUNCTION : 获取排班人员列表
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
	public PageResult<Map<String, Object>> getMemberList(JSONObject paraObject, int pageSize, int curPage){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT U.USER_ID,U.ACNT,U.NAME   \n");
		sql.append("FROM TT_CRM_SEATS S, TC_USER U   \n");
		sql.append("WHERE S.SE_USER_ID = U.USER_ID AND S.SE_IS_MANAMGER = "+Constant.se_is_manamger_2+" \n");
        if(!paraObject.get("ACNT").equals("")){
        	sql.append("and U.ACNT like'%"+paraObject.get("ACNT")+"%'  \n");
        }
        if(!paraObject.get("NAME").equals("")){
        	sql.append("and U.NAME like'%"+paraObject.get("NAME")+"%'  \n");
        }		
		return dao.pageQuery(sql.toString(), params, getFunName() + System.currentTimeMillis(), pageSize, curPage);
	}
	/**
	 * @FUNCTION : 获取班次类型列表
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
    public List<Map<String, Object>> getWtTypeList() {
        StringBuffer sql = new StringBuffer();
        sql.append("select CODE_ID as WT_TYPE_ID,CODE_DESC as WT_TYPE from tc_code t where 1=1 and t.status="+Constant.STATUS_ENABLE+" and t.type="+Constant.SHIFT_TIMES+" order by NUM"  );       
        List<Map<String, Object>> wtTypeList = this.pageQuery(sql.toString(), null, this.getFunName());
        return wtTypeList;
    }

	/**
	 * @FUNCTION : 获取坐席业务列表
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
    public PageResult<Map<String, Object>> getShiftKindList(JSONObject paraObject, int pageSize, int curPage) {
    	List<Object> params = new LinkedList<Object>();
        StringBuffer sql = new StringBuffer();
        sql.append("select CODE_ID as SHIFT_KIND,CODE_DESC as SHIFT_KIND_DESC from tc_code t where 1=1 and t.status="+Constant.STATUS_ENABLE+" and t.type="+Constant.SHIFT_KIND+" order by NUM" );       
        return dao.pageQuery(sql.toString(), params, getFunName() + System.currentTimeMillis(), pageSize, curPage);
    }    
    
    
	/**
	 * 
	 * @Title      : 
	 * @Description: 删除 
	 * @param      : @param code      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void CRMSortDel(String ckids)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("update TT_CRM_SORT_SHIFT set STATUS ="+Constant.STATUS_DISABLE+" where SS_ID in ("+ckids+")");
		System.out.println(sql.toString());
		factory.delete(sql.toString(),null);		
	}    
    
	/**
	 * @FUNCTION : 导入excel文件
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
    public void ImportExcel(){
        StringBuffer sql = new StringBuffer();
        sql.append("select CODE_ID as WT_TYPE_ID,CODE_DESC as WT_TYPE from tc_code t where 1=1 and t.status="+Constant.STATUS_ENABLE+" and t.type="+Constant.SHIFT_TIMES );       
        dao.insert(sql.toString());
    }
    
    
	/**
	 * @FUNCTION : 根据ACNT获取USER_ID
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
	public Long getUserId(String acnt){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT U.USER_ID   \n");
		sql.append("FROM TT_CRM_SEATS S, TC_USER U   \n");
		sql.append("WHERE S.SE_USER_ID = U.USER_ID AND S.SE_IS_MANAMGER = "+Constant.se_is_manamger_2+" and U.ACNT='"+acnt+"' \n");
        List<Map<String,Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		if(null==list||list.size()<=0||list.get(0)==null){
			return null;
		}
		return Long.parseLong(list.get(0).get("USER_ID").toString());
	}
	
	/**
	 * @FUNCTION : 获取WT_TYPE
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
	public Integer getWtType(String wtName){
		StringBuffer sql = new StringBuffer();
        sql.append("select CODE_ID    \n");
        sql.append("from TC_CODE  \n");
        sql.append("where STATUS="+Constant.STATUS_ENABLE+" and TYPE="+Constant.SHIFT_TIMES+" and CODE_DESC='"+wtName+"'  \n");
        List<Map<String,Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		if(null==list||list.size()<=0||list.get(0)==null){
			return null;
		}
		return Integer.parseInt(list.get(0).get("CODE_ID").toString());
	}
	/**
	 * 判断该用户是否是当天的坐席排班
	 * @param userid
	 * @return boolean
	 */
	public boolean isSortCurrentDay(long userid){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * \r\n");
		sql.append(" from tt_crm_sort_shift a , TT_CRM_WORKTIME W\r\n");
//		sql.append(" where trunc(a.DUTY_DATE) = trunc(sysdate)  \r\n"); 
		
		// 艾春 9.18 添加坐席登录限制
		sql.append("WHERE A.WT_TYPE = W.WT_TYPE\n");
		sql.append("  AND TRUNC(SYSDATE, 'MI') >= TO_DATE(TO_CHAR(A.DUTY_DATE, 'YYYY-MM-DD') || ' ' ||W.WT_STA_ON_MINUTE || ':' || W.WT_STA_OFF_MINUTE2, 'yyyy-mm-dd hh24:mi')\n");
		sql.append("  AND trunc(SYSDATE, 'MI') <= CASE WHEN w.wt_end_on_minute < w.wt_sta_on_minute\n");
		sql.append("   THEN TO_DATE(TO_CHAR(A.DUTY_DATE+1, 'YYYY-MM-DD') || ' ' ||W.Wt_End_On_Minute || ':' || W.WT_END_OFF_MINUTE, 'yyyy-mm-dd hh24:mi')\n");
		sql.append("   ELSE TO_DATE(TO_CHAR(A.DUTY_DATE, 'YYYY-MM-DD') || ' ' ||W.Wt_End_On_Minute || ':' || W.WT_END_OFF_MINUTE, 'yyyy-mm-dd hh24:mi') END");

		sql.append(" and a.status = " +Constant.STATUS_ENABLE);
		sql.append(" and a.USER_ID = "+userid);
		
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		if(list != null && list.size()>0) return true;
		else return false;
	}
    //通过tc_code id 得到desc
	public String getCodeDesc(String id){
		String codeDesc = "";
		String sql = " select * from tc_code c where c.code_id in("+id+")";
		List<TcCodePO> list  =  this.select(TcCodePO.class,sql.toString(),null); 
		if(list!=null&& list.size()>0){
			for(int i=0;i<list.size();i++){
				codeDesc =codeDesc + list.get(i).getCodeDesc()+",";
			}
		}
		if(!"".equalsIgnoreCase(codeDesc)){
			codeDesc = codeDesc.substring(0, codeDesc.length()-1);
		}
		return codeDesc; 
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
