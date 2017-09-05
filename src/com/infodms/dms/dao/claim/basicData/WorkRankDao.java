package com.infodms.dms.dao.claim.basicData;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName: WorkRankDao
 * @Description: TODO(工作等级维护DAO)
 * @author luole
 * @date 2013-1-16 上午11:26:20
 * 
 */
public class WorkRankDao extends BaseDao {
	public static Logger logger = Logger.getLogger(ClaimLaborDao.class);
	private final static WorkRankDao wrk = new WorkRankDao();

	public final static WorkRankDao getInstance() {
		return wrk;
	}
	/**
	 * 
	* @Title: getWorkRankNameList 
	* @Description: TODO(获得作业等级名称列表) 
	* @param @param rankType
	* @param @return
	* @return List
	* @throws
	 */
	public List getWorkRankNameList(String rankType) {
		String sql ="select * from tc_code t where t.type=?";
		List<Object> params = new LinkedList<Object>();
		params.add(rankType);
		List<Map<String,Object>> list = pageQuery(sql, params, getFunName());
		return list;
	}
	/**
	 * 
	* @Title: workRankQurey 
	* @Description: TODO(工作等级维护主页查询分页) 
	* @param @param pageSize    ：一页显示数量
	* @param @param curPage     ：当面页
	* @param @param whereSql	：SQL
	* @return PageResult<Map<String,Object>>  分页数据
	* @throws
	 */
	public PageResult<Map<String, Object>> workRankQurey(int pageSize, int curPage, String whereSql ){
		PageResult<Map<String, Object>> result =null;
		StringBuffer sb = new StringBuffer();
		sb.append("select td.LABOUR_CODE,td.LABOUR_NAME,TC.CODE_DESC ");
		sb.append("from TM_DEALER_LABOUR_DETAIL td,TC_CODE tc ");
		sb.append("where td.DEALER_LABOUR_TYPE=tc.CODE_ID ");
		if(whereSql!=null && !whereSql.equals("")){
			sb.append(whereSql.trim());
		}
		result =  pageQuery(sb.toString(), null, getFunName(), pageSize, curPage);
		return result;
	}
	/**
	 * 
	* @Title: labourListQuery 
	* @Description: TODO(新增工作等级时，对工行代码的查询分页) 
	* @param @param pageSize  ：一页显示数量
	* @param @param curPage   ：当面页
	* @param @param whereSql  ：SQL
	* @param @return
	* @return PageResult<Map<String,Object>>   分页数据
	* @throws
	 */
	public PageResult<Map<String, Object>> laborListQuery(int pageSize, int curPage, String whereSql){
		PageResult<Map<String, Object>> result =null;
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct tt.LABOUR_CODE,tt.CN_DES ");
		sb.append("from TT_AS_WR_WRLABINFO tt ");
		if(whereSql!=null && !whereSql.equals("")){
			sb.append("where ");
			sb.append(whereSql.trim());
		}
		System.out.println(sb.toString());
		result =  pageQuery(sb.toString(), null, getFunName(), pageSize, curPage);
		return result;
	}
	public PageResult<Map<String, Object>> partListQuery(int pageSize, int curPage, String partCode,String partName){
		PageResult<Map<String, Object>> result =null;
		StringBuffer sql = new StringBuffer();
		sql.append("select t.part_code,t.part_name from tm_pt_part_base t where t.is_del = 0 \n"); 
		if(Utility.testString(partName)){
			sql.append("and t.part_name like '%"+partName+"%'");
		}
		if(Utility.testString(partCode)){
			sql.append("and t.part_code like '%"+partCode+"%'");
		}
		result =  pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;
	}
	/**
	 * 
	* @Title: addWorkRank 
	* @Description: TODO(新增工作等级到数据库) 
	* @param @param labourCode  ：工时代码
	* @param @param labourName  ：工时名称
	* @param @param workRank    ：作业等级
	* @param @return
	* @return boolean
	* @throws
	 */
	public boolean addWorkRank(String labourCode,String labourName,List<String> workRank,long logonUserId){
		if(addWorkRankQuery(labourCode, labourName)){
			addWorkRankDelete(labourCode, labourName);
		}
		StringBuffer sql = new StringBuffer();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(String str : workRank){
			sql.append("insert into tm_dealer_labour_detail ");
			sql.append("values('"+SequenceManager.getSequence("")+"','");
			sql.append(str);
			sql.append("','"+labourCode+"','"+labourName+"',to_date('"+sdf.format(new Date())+"','yyyy-MM-dd'),"+logonUserId+",'','')");
			System.out.println(sql.toString());
			insert(sql.toString());
			sql.delete(0, sql.length());
		}
		return true;
	}
	//新增工作等级时，查询当前所要插入到数据库的记录是与以在数据库中，当查询到返回true否则返回false
	private boolean addWorkRankQuery(String labourCode,String labourName){
		StringBuffer sql = new StringBuffer();
		sql.append("select *  from  TM_DEALER_LABOUR_DETAIL tt ");
		sql.append("where tt.LABOUR_CODE ='"+labourCode+"' ");
		sql.append("and tt.LABOUR_NAME = '"+labourName+"'");
		List list =  select(sql.toString(), null, this.getClass());
		if(list!=null && list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	//删除当前以在数据库存在了工作等级
	private boolean addWorkRankDelete(String labourCode,String labourName){
		StringBuffer sql = new StringBuffer();
		sql.append("delete TM_DEALER_LABOUR_DETAIL tt ");
		sql.append("where tt.LABOUR_CODE ='"+labourCode+"' ");
		sql.append("and tt.LABOUR_NAME = '"+labourName+"'");
		int flag = delete(sql.toString(),null);
		return flag>0?true:false;
	}
	
	public void initialInventoryInto(String Id,String dealerId){
		
		
		//先删除以前的记录
		
		String sql1="delete initialInventory where DEALER_ID="+dealerId;
		
		String sql2="delete initialInventoryDetail where id=(select id from initialInventory where dealer_id="+dealerId+")";
		
		 delete(sql1,null);
		 
		 delete(sql2,null);
		
		StringBuffer sql = new StringBuffer();
		
		
		
		sql.append("insert into initialInventory(id,CREATE_DATE,WHETHER_CAN_HANDLE,IS_SUCCESS, DEALER_ID,dealer_code) values( "+Id+",  sysdate,  0,  0,"+dealerId+",(select dealer_code from tm_dealer where dealer_id="+dealerId+"))");
	
		insert(sql.toString());
	}
	
	public void initialInventoryIntoDetail(String Id,String str1,String str2){
		StringBuffer sql = new StringBuffer();
			
		sql.append("insert into initialInventoryDetail(Detail_id,id,CREATE_DATE,num,part_code,is_success)  values(f_getid(),"+Id+",sysdate,'"+str2+"','"+str1+"',0)");
		insert(sql.toString());
	}
	
	
	public String queryIshandle(String dealerId){
		String sql="select to_char(HANDLE_DATE+31,'yyyy-MM-dd') as HANDLE_DATE from initialInventory where WHETHER_CAN_HANDLE=0 and  dealer_id="+dealerId;
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		if(list!=null && list.size()>0){
			Map<String,Object> map = list.get(0);
			return (String)map.get("HANDLE_DATE");
		}else{
			return "";
		}
		
	}
	/**
	 * 
	* @Title: 验证配件是否在库存中
	* @Description: 
	* @param @param partCode
	* @param @param dealerId
	* @param @return true：在   false 不在
	* @date 2013-11-1下午4:58:51
	* @throws luole
	*
	 */
	public boolean checkPart(String partCode,Long dealerId){
		String sql =  "select 1 from Tt_As_Wr_Warehouse w, Tt_As_Wr_Warehouse_Detail wd where wd.warehouse_id = w.warehouse_id and wd.part_code like '"+partCode+"'  and w.dealer_id = "+dealerId;
		List list =  select(sql, null, this.getClass());
		if(list!=null && list.size()>0)
			return false;
		return true;
	}
	public int checkDataWJ(){
		String sql = "select t.code_id from tc_code t where t.type = 8008";
		List<Map<String,Object>> list =  pageQuery(sql, null, getFunName());
		Map<String,Object> map = list.get(0);
		/*if("80081002".equals(map.get("CODE_ID").toString()))
			return 0;
		return 1;*/
		if(!"80081001".equals(map.get("CODE_ID").toString())) //微车，轻型车
			return 1;
		return 0;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO 存根
		return null;
	}
	
}
