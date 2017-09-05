package com.infodms.dms.dao.claim.basicData;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.infodms.dms.bean.TreeGuaranteesStrategyBean;
import com.infodms.dms.bean.TtAsWrGameBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrGamefeePO;
import com.infodms.dms.po.TtAsWrRulePO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 三包策略DAO
 * @author XZM
 */
@SuppressWarnings("unchecked")
public class TreeGuaranteesStrategyDao extends BaseDao {

	private static TreeGuaranteesStrategyDao stategyDao = new TreeGuaranteesStrategyDao();
	
	public static TreeGuaranteesStrategyDao getInstance(){
		if(stategyDao==null)
			stategyDao = new TreeGuaranteesStrategyDao();
		return stategyDao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	/**
	 * 查询三包策略
	 * @param conditionVO
	 * @return
	 */
	public PageResult<Map<String,Object>> strategyQuery(TreeGuaranteesStrategyBean conditionVO){
		
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder sBuilder = new StringBuilder();
		
		sBuilder.append("SELECT A.ID,A.RULE_ID,A.GAME_CODE,A.GAME_NAME,A.GAME_TYPE,A.MAINTAIN_NUM, \n");
		sBuilder.append("TO_CHAR(A.START_DATE,'YYYY-MM-DD') START_DATE,TO_CHAR(A.END_DATE,'YYYY-MM-DD') END_DATE,\n"); 
		sBuilder.append("A.GAME_STATUS,TO_CHAR(A.CREATE_DATE,'YYYY-MM-DD') CREATE_DATE,A.COMPANY_ID, \n");
		sBuilder.append("B.RULE_CODE,B.RULE_NAME \n");
		sBuilder.append("FROM TT_AS_WR_GAME A,TT_AS_WR_RULE B \n");
		sBuilder.append("WHERE 1=1 \n");
		sBuilder.append("AND A.RULE_ID = B.ID(+) \n");
		
		this.createWhereMap("COMPANY_ID", "=", conditionVO.getCompanyId().toString(), sBuilder, paramList, 2, "", "A");
		this.createWhereMap("GAME_CODE", "LIKE", conditionVO.getGameCode(), sBuilder, paramList, 2, "", "A");
		this.createWhereMap("GAME_NAME", "LIKE", conditionVO.getGameName(), sBuilder, paramList, 2, "", "A");
		this.createWhereMap("GAME_STATUS", "=", conditionVO.getStatus(), sBuilder, paramList, 2, "", "A");
		this.createWhereMap("GAME_TYPE", "=", conditionVO.getGameType(), sBuilder, paramList, 2, "", "A");
		this.createWhereMap("MAINTAIN_NUM", "=", conditionVO.getMaintaimNum(), sBuilder, paramList, 2, "", "A");
		this.createWhereMap("RULE_CODE", "LIKE", conditionVO.getRuleCode(), sBuilder, paramList, 2, "", "B");
		this.createWhereMap("RULE_NAME", "LIKE", conditionVO.getRuleName(), sBuilder, paramList, 2, "", "B");
		
		sBuilder.append(" ORDER BY A.ID DESC ");
		
		return this.pageQuery(sBuilder.toString(),
				paramList,
				this.getClass().getName()+".strategyQuery()",
				conditionVO.getPageSize(),
				conditionVO.getCurPage());
	}
	public void updateVehicle(String id,String WR_MONTHS)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE TM_VEHICLE t set t.WR_END_DATE =add_months(t.PURCHASED_DATE,"+WR_MONTHS+")  WHERE t.CLAIM_TACTICS_ID = "+id+" and t.PURCHASED_DATE is not null");
		stategyDao.update(sql.toString(), null);
	}
	/**
	 * 根据ID查询对应三包策略
	 * @param id 三包策略ID
	 * @return TtAsWrGameBean
	 */
	public TtAsWrGameBean queryStrategyById(Long id){
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(id);
		
		String sql = "SELECT  A.ID,A.RULE_ID,A.GAME_CODE,A.GAME_NAME,A.GAME_TYPE,A.VEHICEL_PROPERTY,A.MAINTAIN_NUM," +
						"TO_CHAR(A.START_DATE,'YYYY-MM-DD') START_TIME,TO_CHAR(A.END_DATE,'YYYY-MM-DD') END_TIME," +
						"A.GAME_STATUS,A.CREATE_BY,A.CREATE_DATE,A.COMPANY_ID,A.*, B.RULE_CODE," +
						" B.RULE_NAME,A.REMARK, C.CODE_DESC STATUS,A.is_for_busi ,a.vehicle_pro_busi,a.vehicle_pro_busi_desc\n"+
						"FROM TT_AS_WR_GAME A,TT_AS_WR_RULE B,TC_CODE C \n"+
						"WHERE 1=1 \n"+
						"AND A.RULE_ID = B.ID(+) \n"+
						"AND A.GAME_STATUS = C.CODE_ID(+) \n" +
						"AND A.ID = ?";
		
		TtAsWrGameBean resBean = null;
		List<TtAsWrGameBean> resList = this.select(TtAsWrGameBean.class, sql, paramList);
		if(resList!=null && resList.size()>0)
			resBean = resList.get(0);
		
		return resBean;
	}
	
	/**
	 * 三包规则查询(有效的业务规则)
	 * @param companyId
	 * @param ruleType
	 * @return List<TtAsWrGamePO>
	 */
	public List<TtAsWrRulePO> ruleQuery(Long companyId,Integer ruleType){
		List<TtAsWrRulePO> result = null;
		
		TtAsWrRulePO conditionVO = new TtAsWrRulePO();
		conditionVO.setRuleStatus(Constant.STATUS_ENABLE);
		String sql = "SELECT ID, RULE_NAME, RULE_CODE " +
				" FROM TT_AS_WR_RULE " +
				" WHERE  1=1 " +
				" AND RULE_STATUS= ? " +
				" AND COMPANY_ID=? ";
				
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(Constant.STATUS_ENABLE);//状态有效
		paramList.add(companyId);//限制公司
		result = this.select(TtAsWrRulePO.class, sql, paramList);
		
		return result;
	}
	
	/**
	 * 查询三包策略对应费用
	 * @param gameId 三包策略ID
	 * @return List<TtAsWrGamefeePO>
	 */
	public List<TtAsWrGamefeePO> queryStrategyAmount(Long gameId){
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(gameId);
		
		String sql = "SELECT ID,GAME_ID,MAINTAINFEE_ORDER,MANINTAIN_FEE \n"+ 
						 "FROM TT_AS_WR_GAMEFEE \n"+ 
						 "WHERE GAME_ID = ? \n"+ 
						 "ORDER BY MAINTAINFEE_ORDER";
		return this.select(TtAsWrGamefeePO.class, sql, paramList);
	}
	
	/**
	 * 查询三包策略对应车型
	 * @param gameId 三包策略ID
	 * @param curPage 当前页
	 * @param pageSize 每页记录数
	 * @return PageResult<Map<String,Object>>
	 */
	public PageResult<Map<String,Object>> queryStrategyModel(Long gameId,Integer curPage,Integer pageSize){
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(gameId);
		
		String sql = "SELECT A.ID,A.GAME_ID,B.GROUP_CODE MODEL_CODE,B.GROUP_NAME MODEL_NAME \n"+
						 " FROM TT_AS_WR_GAMEMODEL A, TM_VHCL_MATERIAL_GROUP B \n"+
						 " WHERE A.MODEL_ID = B.GROUP_ID(+) \n"+
						 " AND GAME_ID = ? \n"+
						 " ORDER BY A.ID DESC";
		
		return this.pageQuery(sql,
				paramList,
				this.getClass().getName()+".queryStrategyModel()",
				pageSize,
				curPage);
	}
	
	/**
	 * 查询三包策略对应省份
	 * @param gameId 三包策略ID
	 * @param curPage 当前页
	 * @param pageSize 每页记录数
	 * @return PageResult<Map<String,Object>>
	 */
	public PageResult<Map<String,Object>> queryStrategyProvince(Long gameId,Integer curPage,Integer pageSize){
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(gameId);
		
		String sql = " SELECT A.ID, B.REGION_CODE,B.REGION_NAME \n"+
							" FROM TT_AS_WR_GAMEPRO A,TM_REGION B \n"+
							" WHERE A.PROVINCE_ID = B.REGION_CODE(+) \n"+
							" AND A.GAME_ID = ? \n"+
							" ORDER BY A.ID DESC";
		
		return this.pageQuery(sql,
				paramList,
				this.getClass().getName()+".queryStrategyProvince()",
				pageSize,
				curPage);
	}
	
	/**
	 * 删除某一三包策略设定的保养费用
	 * @param gameId 三包策略ID
	 * @return int 
	 */
	public int deleteStrategyAmount(Long gameId){
		TtAsWrGamefeePO conditionPO = new TtAsWrGamefeePO();
		conditionPO.setGameId(gameId);
		int count = this.delete(conditionPO);
		return count;
	}
	
	/**
	 * 三包策略车型信息查询，对应策略已经设定过的不再显示
	 * @param groupId    : 车系、车型、配置ID
	 * @param groupCode  : 车系、车型、配置CODE
	 * @param groupName  : 车系、车型、配置名称
	 * @param groupLevel : 所属物料组层次
	 * @param curPage    ：当前页码
	 * @param pageSize   : 每页记录数
	 * @param companyId  ：所属公司ID
	 * @param strategyId : 三包策略ID 
	 * @see MaterialGroupManagerDao.getGroupList()
	 * @return PageResult<Map<String, Object>>
	 */
	public PageResult<Map<String, Object>> guaranteeGroupListQuery(String groupId,String groupCode,
			String groupName,String groupLevel,String wrGroupId,
			int curPage,int pageSize,Long companyId,Long strategyId){
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT T.GROUP_ID,\n");
		sql.append("       T.GROUP_CODE,\n");  
		sql.append("       T.GROUP_NAME,\n");  
		sql.append("       T.PARENT_GROUP_ID,\n");  
		sql.append("       T.TREE_CODE\n");  
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP T\n");  
		sql.append(" WHERE T.COMPANY_ID = "+companyId+"\n");  
		if(!"".equals(groupCode)&&groupCode!=null)
		{
		sql.append("   AND T.GROUP_CODE LIKE '%"+groupCode+"%'\n");	
		}
		if(!"".equals(groupName)&&groupName!=null)
		{
		sql.append("   AND T.GROUP_NAME LIKE '%"+groupName+"%'\n"); 
		}
		if(!"".equals(groupLevel)&&groupLevel!=null&&!"null".equals(groupLevel))
		{
		sql.append("   AND T.GROUP_LEVEL = "+groupLevel+"\n");
		}
		sql.append("   AND T.STATUS = "+Constant.STATUS_ENABLE+"\n");
		
		if(Utility.testString(wrGroupId)){//车型组现在
			sql.append("AND EXISTS (SELECT M.WRGROUP_ID FROM TT_AS_WR_MODEL_ITEM M\n" );
			sql.append("WHERE 1=1\n" );
			sql.append("AND M.MODEL_ID = T.GROUP_ID\n" );
			sql.append("AND M.WRGROUP_ID = '"+wrGroupId+"')\n");
		}
		//限制不显示已经设定过车型信息
		sql.append(" AND NOT EXISTS (SELECT M.GAME_ID FROM TT_AS_WR_GAMEMODEL M WHERE M.GAME_ID = ")
		.append(strategyId)
		.append( " AND M.MODEL_ID = T.GROUP_ID)\n");
		if(!"".equals(groupId)&&groupId!=null)
		{
			if("".equals(groupLevel)||groupLevel==null||"null".equals(groupLevel))
		  {
				sql.append(" AND  T.PARENT_GROUP_ID = "+groupId+"\n");  
		  }else
		  {
			   sql.append(" START WITH T.GROUP_ID = "+groupId+"\n");  
			   sql.append("CONNECT BY PRIOR T.GROUP_ID = T.PARENT_GROUP_ID\n"); 	  
		  }
		
		}
		
		sql.append(" ORDER BY T.GROUP_ID\n");
		PageResult<Map<String, Object>> rs = this.pageQuery(sql.toString(), null, this.getFunName(),pageSize, curPage);
		return rs;
	}
	
	/**
	 * 保存三包策略设定的车型
	 * @param modelId 设置车型ID集合
	 * @param strategyId 三包策略ID
	 * @param userId
	 * @param dateTime 日期（YYYY-MM-DD HH24:MI:SS）
	 */
	public void saveGuaranteeStrategyModel(String modelId,String strategyId,Long userId,String dateTime){
		StringBuilder sql= new StringBuilder();
		sql.append("INSERT INTO TT_AS_WR_GAMEMODEL(ID,GAME_ID,MODEL_CODE,MODEL_NAME,CREATE_BY,CREATE_DATE,MODEL_ID)\n" );
		sql.append("SELECT F_GETID() ID," + strategyId + " GAME_ID,A.GROUP_CODE MODEL_CODE,A.GROUP_NAME MODEL_NAME,\n" );
		sql.append(userId + " CREATE_BY,TO_DATE('" + dateTime + "','YYYY-MM-DD HH24:MI:SS') CREATE_DATE,A.GROUP_ID MODEL_ID\n" );
		sql.append("FROM TM_VHCL_MATERIAL_GROUP A\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.GROUP_ID IN (" + modelId + ")\n");
		sql.append("AND NOT EXISTS (SELECT B.MODEL_ID FROM TT_AS_WR_GAMEMODEL B WHERE B.GAME_ID = " +strategyId+ "" +
						" AND B.MODEL_ID = A.GROUP_ID) ");
		
		this.update(sql.toString(), null);
	}
	
	/**
	 * 根据主键删除对应设定三包策略车型
	 * @param modelIds 设定三包策略车型对应记录ID
	 * @return
	 */
	public int deleteGuaranteeStrategyModel(String modelIds){
		int count = 0;
		String sql = "DELETE FROM TT_AS_WR_GAMEMODEL WHERE ID IN ("
					+ modelIds + ")";
		count = this.delete(sql, null);
		return count;
	}
	
	/**
	 * 查询省份信息
	 * @param regionType
	 * @param curPage
	 * @param pageSize
	 * @param strategyId 三包策略ID
	 * @return
	 */
	public PageResult<Map<String,Object>> queryProvince(Integer regionType,
			Integer curPage,Integer pageSize,String strategyId){
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(regionType);
		paramList.add(Constant.STATUS_ENABLE);
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT * FROM TM_REGION A\n" );
		sql.append("WHERE A.REGION_TYPE = ?\n");
		sql.append("AND A.STATUS = ?\n");
		
		if(strategyId!=null && !"".equals(strategyId)){
			paramList.add(strategyId);
			sql.append("AND NOT EXISTS (SELECT B.PROVINCE_ID FROM TT_AS_WR_GAMEPRO B " +
					"WHERE B.PROVINCE_ID = A.REGION_CODE " +
					"AND B.GAME_ID = ?)\n");
		}
		
		return this.pageQuery(sql.toString(),
				paramList,
				this.getClass().getName()+".queryProvince()",
				pageSize,
				curPage);
	}
	
	/**
	 * 保存三包策略设定的省份
	 * @param provinceId 设置省份ID集合
	 * @param strategyId 三包策略ID
	 * @param userId
	 * @param dateTime 日期（YYYY-MM-DD HH24:MI:SS）
	 */
	public void saveGuaranteeStrategyProvince(String provinceId,String strategyId,Long userId,String dateTime){
		StringBuilder sql= new StringBuilder();
		sql.append("INSERT INTO TT_AS_WR_GAMEPRO(ID,GAME_ID,PROVINCE_ID,CREATE_BY,CREATE_DATE)\n");
		sql.append("SELECT F_GETID() ID," + strategyId + " GAME_ID,A.REGION_CODE PROVINCE_ID," 
				+userId+ " CREATE_BY,TO_DATE('"+dateTime+"','YYYY-MM-DD HH24:MI:SS') CREATE_DATE\n" );
		sql.append("FROM TM_REGION A\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.REGION_CODE IN (" +provinceId+ ")\n");
		sql.append("AND NOT EXISTS (SELECT B.ID FROM TT_AS_WR_GAMEPRO B WHERE B.GAME_ID ="+strategyId +
				" AND B.PROVINCE_ID = A.REGION_ID)");
		
		this.update(sql.toString(), null);
	}
	
	/**
	 * 保存三包策略设定的省份
	 * @param provinceId 设置省份ID集合
	 * @param strategyId 三包策略ID
	 * @param userId
	 * @param dateTime 日期（YYYY-MM-DD HH24:MI:SS）
	 */
	public void saveGuaranteeStrategyYieldly(String yieldlyId,String strategyId,Long userId,String dateTime){
		StringBuilder sql= new StringBuilder();
		sql.append("INSERT INTO TT_AS_WR_GAMEYIELDLY(ID,GAME_ID,YIELDLY_ID,YIELDLY_NAME,CREATE_BY,CREATE_DATE)\n");
		sql.append("SELECT F_GETID() ID," + strategyId + " GAME_ID,A.AREA_ID YIELDLY_ID,A.AREA_NAME YIELDLY_NAME," 
				+userId+ " CREATE_BY,TO_DATE('"+dateTime+"','YYYY-MM-DD HH24:MI:SS') CREATE_DATE\n" );
		sql.append("FROM tm_business_area A\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.AREA_ID IN (" +yieldlyId+ ")\n");
		sql.append("AND NOT EXISTS (SELECT B.ID FROM TT_AS_WR_GAMEYIELDLY B WHERE B.GAME_ID ="+strategyId +
				" AND B.YIELDLY_ID = A.AREA_ID)");
		
		this.update(sql.toString(), null);
	}
	
	/**
	 * 根据主键删除对应设定三包策略省份
	 * @param provinceIds 设定三包策略省份对应记录ID
	 * @return
	 */
	public int deleteGuaranteeStrategyProvince(String provinceIds){
		int count = 0;
		String sql = "DELETE FROM TT_AS_WR_GAMEPRO WHERE ID IN ("
					+ provinceIds + ")";
		count = this.delete(sql, null);
		return count;
	}
	
	/**
	 * 根据主键删除对应设定三包策略产地
	 * @param provinceIds 设定三包策略产地对应记录ID
	 * @return
	 */
	public int deleteGuaranteeStrategyYieldly(String yieldlyIds){
		int count = 0;
		String sql = "DELETE FROM TT_AS_WR_GAMEYIELDLY WHERE ID IN ("
					+ yieldlyIds + ")";
		count = this.delete(sql, null);
		return count;
	}
	
	/**
	 * 查询产地信息
	 * 注：对应策略设定过的产地将不在显示
	 * @param regionType
	 * @param curPage
	 * @param pageSize
	 * @param strategyId 三包策略ID
	 * @return
	 */
	public PageResult<Map<String,Object>> queryYieldly(Integer curPage,Integer pageSize,String strategyId){
		List<Object> paramList = new ArrayList<Object>();
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT A.* FROM tm_business_area A\n" );
		if(strategyId!=null && !"".equals(strategyId)){
			paramList.add(strategyId);
			sql.append(" where NOT EXISTS (SELECT B.YIELDLY_ID FROM TT_AS_WR_GAMEYIELDLY B " +
					"WHERE B.YIELDLY_ID = A.AREA_ID " +
					"AND B.GAME_ID = ?)\n");
		}
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) super.pageQuery(sql.toString(), paramList,  getFunName(), pageSize, curPage);
		
		return ps;
	}
	
	/**
	 * 查询三包策略对应产地
	 * @param gameId 三包策略ID
	 * @param curPage 当前页
	 * @param pageSize 每页记录数
	 * @return PageResult<Map<String,Object>>
	 */
	public PageResult<Map<String,Object>> queryStrategyYieldly(Long gameId,Integer curPage,Integer pageSize){
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(gameId);
		
		String sql = " SELECT A.ID, A.YIELDLY_ID,A.YIELDLY_NAME \n"+
							" FROM TT_AS_WR_GAMEYIELDLY A\n"+
							" WHERE 1=1\n"+
							" AND A.GAME_ID = ? \n"+
							" ORDER BY A.ID DESC";
		
		return this.pageQuery(sql,
				paramList,
				this.getClass().getName()+".queryStrategyProvince()",
				pageSize,
				curPage);
	}
	
	/**
	 * 拼查询条件，如果页面查询过来不为空，则拼装到查询条件中
	 * @param param 参数列 对应数据库中字段
	 * @param value 参数值
	 * @param oper 操作符
	 * @param sBuilder 拼装条件容器
	 * @param paramList 参数列表
	 * @param dataType 数据类型
	 *        1 : 时间
	 *        2 ：其他
	 * @param dataFormat 数据格式，现在只有时间类型需要添加格式
	 * @param table 标明表名或别名
	 * @return
	 */
	private void createWhereMap(String param,String oper,String value,
			StringBuilder sBuilder,List<Object> paramList,int dataType,
			String dataFormat,String table){
		if(Utility.testString(value)){
			param = table + "." + param;
			if(dataType==1) {//时间
				sBuilder.append(" AND ").append(param).append(" ")
				.append(oper).append(" TO_DATE(" +"?" + ",'" + dataFormat + "')");
				paramList.add(value);
			}else if("IN".equalsIgnoreCase(oper)){
				StringTokenizer st = new StringTokenizer(value,",");
				
				boolean flag = true;
				while(st.hasMoreTokens()){
					String tempValue = st.nextToken();
					if(Utility.testString(tempValue)){
						if(flag)//保证只加一次AND COL IN (
							sBuilder.append(" AND ").append(param).append(" ").append(oper).append(" (");
						flag = false;
						sBuilder.append("?,");
						paramList.add(tempValue);
					}
				}
				if(!flag)
					sBuilder.append("'')");//加入后半个括号，同时多加一个空''
			}else{//其他
				sBuilder.append(" AND ").append(param).append(" ").append(oper).append(" ?");
				if("LIKE".equalsIgnoreCase(oper)){//模糊查询
					paramList.add("%" +value +"%");
				} else{
					paramList.add(value);
				}
			}
		}
	}
	public List<Map<String,Object>> checkVehi(String str,String id) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from Tt_As_Wr_Game g where instr(g.vehicle_pro_busi,'"+str+"')>0 and g.id!="+id);

		List<Map<String,Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
}
