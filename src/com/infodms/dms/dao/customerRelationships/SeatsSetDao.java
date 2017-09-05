package com.infodms.dms.dao.customerRelationships;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtCrmSeatsPO;
import com.infodms.dms.po.TtCrmSeatsTeamPO;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class SeatsSetDao extends BaseDao{

	private static final SeatsSetDao dao = new SeatsSetDao();
	
	public static final SeatsSetDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public PageResult<Map<String,Object>> querySeatsSet(String stId, String name,String account,String isSeats,String level,String isAdmin,int pageSize,int curPage){
		
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct tuser.user_id tuserId,tseat.se_id SEID, tuser.name tuserName,tuser.acnt tuserAccount, \n");
		sql.append(" tseat.se_seats_no seSeatsNum, tseat.se_level seLevel,tseat.se_is_seats seIsSeats,tseat.se_status STATUS,tseat.ST_NAME stName\n"); // 艾春9.21修改
		sql.append(" from tm_org_custom a  ");
		sql.append(" inner join tm_org_cus_user_relation b on a.org_id = b.org_id  \n");
		sql.append(" left join tc_user tuser on tuser.user_id = b.user_id \n");
//		sql.append(" left join ( select * from Tt_Crm_Seats c where c.se_status = 10011001 ) tseat on tseat.se_user_id = b.user_id \n");
		// 艾春9.21修改
		sql.append(" left join ( select c.*, ST.ST_CODE, ST.ST_NAME from Tt_Crm_Seats c, TT_CRM_SEATS_TEAM ST WHERE C.ST_ID = ST.ST_ID(+) and c.se_status = 10011001 ) tseat on tseat.se_user_id = b.user_id \n");
		sql.append(" where a.org_id =2013070519381899  \n");

		if(StringUtil.notNull(name)){
			sql.append(" and tuser.name like '%"+name+"%'\n");
		}
		if(StringUtil.notNull(account)){
			sql.append(" and tseat.se_seats_no like '%"+account+"%'\n");
		}
		if(StringUtil.notNull(isSeats)){
			sql.append(" and tseat.se_is_seats = '"+isSeats+"'\n");
		}
		if(StringUtil.notNull(level)){
			sql.append(" and tseat.se_level = '"+level+"'\n");
		}
		if(StringUtil.notNull(isAdmin)){
			sql.append(" and tseat.se_is_manamger = '"+isAdmin+"'\n");
		}
		if(StringUtil.notNull(stId)){
			sql.append(" and tseat.st_id = "+stId+"\n");
		}
		
		sql.append(" ORDER BY TSEAT.SE_IS_SEATS, TSEAT.ST_NAME DESC, TSEAT.SE_LEVEL, TSEAT.SE_SEATS_NO \n");

		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}

	public TtCrmSeatsPO queryTtCrmSeatsTeamPOByUserId(long userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select tuser.user_id seUserId, tuser.name seName,tuser.acnt seAccount,tseat.se_seats_no seSeatsNo,tseat.se_level seLevel,tseat.se_is_seats seIsSeats, \n");
		sql.append(" tseat.se_ext seExt ,tseat.se_ip seIp,tseat.st_id stId,tseat.se_is_manamger SEISMANAGER \n");
		sql.append(" from tc_user tuser left join TT_CRM_SEATS tseat on tuser.user_id = tseat.se_user_id left join TT_CRM_SEATS_TEAM tseatTeam on tseat.st_id = tseatTeam.St_Id  \n");
		sql.append(" where tuser.user_id = "+userId);
		Map<String, Object> map  = this.pageQueryMap(sql.toString(), null, this.getFunName());
		if(map!=null&&map.size()>0){
			TtCrmSeatsPO ttCrmSeatsPO = new TtCrmSeatsPO();
			
			ttCrmSeatsPO.setSeUserId(((BigDecimal)map.get("SEUSERID")).longValue());
			ttCrmSeatsPO.setSeName((String)map.get("SENAME"));
			ttCrmSeatsPO.setSeAccount((String)map.get("SEACCOUNT"));
			ttCrmSeatsPO.setSeSeatsNo((String)map.get("SESEATSNO"));
			ttCrmSeatsPO.setSeSeatsNo((String)map.get("SEACCOUNT"));
			if(map.get("SELEVEL")!= null)ttCrmSeatsPO.setSeLevel(((BigDecimal)map.get("SELEVEL")).intValue());
			if(map.get("SEISSEATS")!= null)ttCrmSeatsPO.setSeIsSeats(((BigDecimal)map.get("SEISSEATS")).intValue());
			if(map.get("SEISMANAGER")!= null)ttCrmSeatsPO.setSeIsManamger(((BigDecimal)map.get("SEISMANAGER")).intValue());
			if(map.get("SEEXT")!= null)ttCrmSeatsPO.setSeExt(((BigDecimal)map.get("SEEXT")).longValue());
			if(map.get("SEIP")!= null)ttCrmSeatsPO.setSeIp((String)map.get("SEIP"));
			if(map.get("STID")!= null)ttCrmSeatsPO.setStId(((BigDecimal)map.get("STID")).longValue());
			return ttCrmSeatsPO;
		}
		return null;
	}
	
	public List<TtCrmSeatsPO> queryTtCrmSeatsPOByUserId(long userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * \n");
		sql.append(" from TT_CRM_SEATS tseat \n");
		sql.append(" where tseat.se_user_id = "+userId);
		List<TtCrmSeatsPO> list = this.select(TtCrmSeatsPO.class, sql.toString(), null);
		return list;
	}
	
	/**
	 * 查询非管理员的所有坐席
	 * @return
	 */
	public List<TtCrmSeatsPO> queryTtCrmSeatsPONOAdminAll(){
		String sql = " select * from TT_CRM_SEATS tseat where tseat.se_status="+Constant.STATUS_ENABLE+" and tseat.se_is_seats = "+Constant.IF_TYPE_YES+" and tseat.se_is_manamger = "+Constant.se_is_manamger_2;
		List<TtCrmSeatsPO> list = this.select(TtCrmSeatsPO.class, sql, null);
		return list;
	}
	
	/**
	 * 查询所有坐席
	 * @return
	 */
	public List<TtCrmSeatsPO> queryTtCrmSeatsPOAll(){
		String sql = " select * from TT_CRM_SEATS tseat where tseat.se_status="+Constant.STATUS_ENABLE+" and tseat.se_is_seats = "+Constant.IF_TYPE_YES;
		List<TtCrmSeatsPO> list = this.select(TtCrmSeatsPO.class, sql, null);
		return list;
	}
	
	/**
	 * 查询所有坐席组
	 * @return
	 */
	public List<Map<String,Object>> queryTtCrmSeatsTeamAll(){
		String sql = " SELECT T.ST_ID stId, T.ST_NAME stName FROM TT_CRM_SEATS_TEAM T WHERE T.STATUS ="+Constant.STATUS_ENABLE;
		return pageQuery(sql.toString(), null, this.getFunName());
	}
	
	public int queryIncomingCount(){
		StringBuffer sql = new StringBuffer();
		sql.append(" select to_char(count(tseat.se_id)) COUNTS from TT_CRM_SEATS tseat \n");
		sql.append(" where tseat.se_work_status= "+Constant.SEAT_INCOMING_TYPE +"\n");
		sql.append(" and tseat.se_status="+Constant.STATUS_ENABLE+" and tseat.se_is_seats = "+Constant.IF_TYPE_YES );
		List list = this.selectTmDataSet(sql, "COUNTS");
		if(list!=null&&list.size()>0) return Integer.parseInt((String)list.get(0));
		return 0;
	}
	
	public int queryFreeCount(){
		StringBuffer sql = new StringBuffer();
		sql.append(" select to_char(count(tseat.se_id)) COUNTS from TT_CRM_SEATS tseat \n");
		sql.append(" where tseat.se_work_status= "+Constant.SEAT_FREE_TYPE +"\n");
		sql.append(" and tseat.se_status="+Constant.STATUS_ENABLE+" and tseat.se_is_seats = "+Constant.IF_TYPE_YES );
		List list = this.selectTmDataSet(sql, "COUNTS");
		if(list!=null&&list.size()>0) return Integer.parseInt((String)list.get(0));
		return 0;
	}
	
	public int queryBusyCount(){
		StringBuffer sql = new StringBuffer();
		sql.append(" select to_char(count(tseat.se_id)) COUNTS from TT_CRM_SEATS tseat \n");
		sql.append(" where tseat.se_work_status in( "+Constant.SEAT_CALLING_TYPE +","+Constant.SEAT_BUSY_TYPE +")\n");
		sql.append(" and tseat.se_status="+Constant.STATUS_ENABLE+" and tseat.se_is_seats = "+Constant.IF_TYPE_YES );
		List list = this.selectTmDataSet(sql, "COUNTS");
		if(list!=null&&list.size()>0) return Integer.parseInt((String)list.get(0));
		return 0;
	}

	public void changWorkStatus(long userId, int seatStatus) {
		String sql ="update TT_CRM_SEATS t set t.se_work_status = "+seatStatus+" where t.se_is_seats = "+Constant.IF_TYPE_YES+" and t.se_status="+Constant.STATUS_ENABLE+" and t.se_user_id = "+userId;
		this.update(sql, null);
	}
	
	/**
	 * 根据用户ID查询是否是坐席
	 * @return boolean
	 */
	public boolean isSeat(long userid){
		String sql = " select * from TT_CRM_SEATS tseat where tseat.se_status="+Constant.STATUS_ENABLE+" and tseat.se_is_seats = "+Constant.IF_TYPE_YES+" and tseat.SE_USER_ID="+userid;
		List<TtCrmSeatsPO> list = this.select(TtCrmSeatsPO.class, sql, null);
		if(list != null && list.size()>0) return true;
		else return false;
	}
	
	public boolean isSeatManeger(long userid){
		String sql = " select * from TT_CRM_SEATS tseat where tseat.se_status="+Constant.STATUS_ENABLE+" and tseat.se_is_seats = "+Constant.IF_TYPE_NO+" and tseat.SE_USER_ID="+userid;
		List<TtCrmSeatsPO> list = this.select(TtCrmSeatsPO.class, sql, null);
		if(list != null && list.size()>0) return true;
		else return false;
	}


}
