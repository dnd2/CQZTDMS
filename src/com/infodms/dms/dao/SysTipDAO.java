package com.infodms.dms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrGamePO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class SysTipDAO extends BaseDao<PO> {
	public static final Logger logger = Logger.getLogger(SysTipDAO.class);
	private static final SysTipDAO dao = new SysTipDAO();
	
	public static final SysTipDAO getInstance() {
		return dao;
	}
	
	public List<Map<String, Object>> getOverduNormal(Map<String, String> map) {
		String drlId = map.get("dealerId") ;
		String orderDate = map.get("orderDate") ;
		
		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select order_date, count(1) total\n") ;
		sql.append("  from (select distinct tvo.order_year || '年' || tvo.order_week || '周' order_date,\n") ;
		sql.append("                        tvo.order_id\n") ;
		sql.append("          from (select tvo.order_year,\n") ;
		sql.append("                       tvo.order_week,\n") ;
		sql.append("                       tvo.order_id,\n") ;
		sql.append("                       tvod.material_id,\n") ;
		sql.append("                       tvod.call_amount,\n") ;
		sql.append("                       tvod.check_amount\n") ;
		sql.append("                  from tt_vs_order tvo, tt_vs_order_detail tvod\n") ;
		sql.append("                 where tvo.order_id = tvod.order_id\n") ;
		sql.append("                   and tvo.order_org_id in (").append(drlId).append(")\n") ;
		sql.append("                   and tvo.order_type = 10201001\n") ;
		sql.append("                   and tvo.order_year >= 2013\n") ;
		sql.append("                   and nvl(tvod.call_amount, 0) < tvod.check_amount\n") ;
		sql.append("                   and tvo.order_year || decode(length(tvo.order_week), 2, ''||tvo.order_week, '0'||tvo.order_week) < ").append(orderDate).append(") tvo,\n") ;
		sql.append("               (select tvo.old_order_id,\n") ;
		sql.append("                       tvod.material_id,\n") ;
		sql.append("                       sum(tvod.call_amount) call_amount\n") ;
		sql.append("                  from tt_vs_order tvo, tt_vs_order_detail tvod\n") ;
		sql.append("                 where tvo.order_id = tvod.order_id\n") ;
		sql.append("                   and tvo.order_type = 10201002\n") ;
		sql.append("                   and tvo.old_order_id >= 1\n") ;
		sql.append("                 group by tvo.old_order_id, tvod.material_id) ttvo\n") ;
		sql.append("         where tvo.order_id = ttvo.old_order_id(+)\n") ;
		sql.append("           and tvo.material_id = ttvo.material_id(+)\n") ;
		sql.append("           and tvo.call_amount + nvl(ttvo.call_amount, 0) < tvo.check_amount) tmp\n") ;
		sql.append(" group by tmp.order_date\n") ;
		sql.append(" order by tmp.order_date\n") ;

		return super.pageQuery(sql.toString(), null, dao.getFunName()) ;
	}
	
	public Map<String, Object> getThisWeekNormal(Map<String, String> map) {
		String drlId = map.get("dealerId") ;
		String orderDate = map.get("orderDate") ;
		
		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select tvo.order_year || '年' || tvo.order_week || '周' order_date,\n") ;
		sql.append("       count(1) total\n") ;
		sql.append("  from tt_vs_order tvo\n") ;
		sql.append(" where 1 = 1\n") ;
		sql.append("   and tvo.order_type = 10201001\n") ;
		sql.append("   and tvo.order_org_id in (").append(drlId).append(")\n") ;
		sql.append("   and tvo.order_year || decode(length(tvo.order_week), 2, ''||tvo.order_week, '0'||tvo.order_week) = ").append(orderDate).append("\n") ;
		sql.append("   and exists (select 1\n") ;
		sql.append("          from tt_vs_order_detail tvod\n") ;
		sql.append("         where tvo.order_id = tvod.order_id\n") ;
		sql.append("           and tvod.check_amount > nvl(tvod.call_amount, 0))\n") ;
		sql.append(" group by tvo.order_year, tvo.order_week\n") ;
		sql.append(" order by tvo.order_year, tvo.order_week\n") ;

		return super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
	}
	
	//首页新闻只取前10条
	public List<Map<String, Object>> getTipNews(AclUserBean logonUser, String dealerId) throws Exception {
		List<Map<String, Object>> result = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.* ,ROWNUM R FROM (\n");
		StringBuffer col= new StringBuffer(); //列		
		 col.append("  oo.news_id,\n" );
		 col.append("  oo.news_code,\n" );
		 col.append("  oo.news_title,\n" );
		 col.append("  oo.news_type,\n" );
		 col.append("  oo.news_date,\n" );
		 col.append("  oo.org_id,\n" );
		 col.append("  oo.org_name,\n" );
	     //col.append("  to_char(oo.contents) contents,\n" );
		 col.append("  oo.oem_company_id,\n" );
		 col.append("  oo.create_date,\n" );
		 col.append("  oo.create_by,\n" );
		 col.append("  oo.update_date,\n" );
		 col.append("  oo.update_by,\n" );
		 col.append("  oo.status,\n" );
		 col.append("  oo.duty_type,\n" );
		 col.append("  oo.org_type_id,\n" );
		 col.append("  oo.voice_person,\n" );
		 col.append("  oo.msg_type,\n" );
		 col.append("  oo.is_private,\n" );
		 col.append("  oo.is_top");
		 if(dealerId==null||dealerId.equals("")){
			 //如果是OEM端			 
			 sql.append(" select "+col.toString()+" from tt_as_wr_news  oo where 1=1 and oo.status !="+Constant.NEWS_STATUS_3+" order by oo.is_top desc,oo.status, oo.create_date desc "); //YH 2010.12.21
		 }
		 else {
			 //如果是经销商端
			sql.append("select distinct "+col.toString()+" from ( select "+col.toString()+" from tt_as_wr_news  oo where 1=1");		 
			sql.append(" and oo.duty_type="+Constant.VIEW_NEWS_type_3+" and oo.status !="+Constant.NEWS_STATUS_3+"\n");  //YH 2010.12.21
			sql.append(" union all\n" );
			sql.append(" select "+col.toString()+"\n" );   //经销商没关闭指定区域指定售后或销售
			sql.append(" from tt_as_wr_news oo\n" );
			sql.append(" where 1 = 1\n" );
			sql.append(" and oo.duty_type = "+Constant.VIEW_NEWS_type_1+"\n" );
			sql.append(" and oo.status != "+Constant.NEWS_STATUS_3+"\n" );
				if(dealerId!=null){
					 List<Map<String, Object>> list = viewOrgIdDealerId(dealerId);
					 StringBuffer str=new StringBuffer("");
					 for(int i=0;i<list.size();i++){
						 str.append(list.get(0).get("ROOT_ORG_ID").toString());
						 if(i!=list.size()-1){
						 str.append(",");
						 }
				String realstr = str.toString().substring(0, str.toString().length()-1);		 
				sql.append(" and oo.org_type_id like'%"+realstr+"%'");
				sql.append(" and oo.is_private ='0'");
				
				if(null != logonUser.getDealerType() && Constant.DEALER_TYPE_DWR == logonUser.getDealerType()){
					sql.append(" and oo.msg_type ='"+Constant.MSG_TYPE_2+"'");
			    }else {
				 sql.append(" and oo.msg_type ='"+Constant.MSG_TYPE_1+"' \n");
			     }					
				sql.append("union all\n" );
				sql.append("select "+col.toString()+"\n" );
				sql.append("from tt_as_wr_news oo\n" );
				sql.append("where 1 = 1\n" );
				sql.append("and oo.duty_type = "+Constant.VIEW_NEWS_type_1+" \n" );
				sql.append("and oo.status != "+Constant.NEWS_STATUS_3+" \n" );
				sql.append("and oo.org_type_id like '%"+realstr+"%' \n" );
				sql.append("and oo.msg_type = '"+Constant.MSG_TYPE_3+"'");

		     }		
			sql.append("union all\n" ); //经销商没关闭全部区域 指定售后或销售
			sql.append("  select "+col.toString()+"\n" );
			sql.append(" from tt_as_wr_news oo\n" );
			sql.append("  where 1 = 1\n" );
			sql.append("  and oo.duty_type = "+Constant.VIEW_NEWS_type_1+" \n" );
			sql.append("  and oo.status != "+Constant.NEWS_STATUS_3+" \n" );
			sql.append("  and oo.org_type_id is null\n" );
			if(null != logonUser.getDealerType() && Constant.DEALER_TYPE_DWR == logonUser.getDealerType()){
				sql.append(" and oo.msg_type ='"+Constant.MSG_TYPE_2+"'");
		    }else {
			 sql.append(" and oo.msg_type ='"+Constant.MSG_TYPE_1+"' \n");
		    }		
			sql.append(" union all\n" );
			sql.append(" select "+col.toString()+"\n" );   //经销商没关闭全部区域消息
			sql.append(" from tt_as_wr_news oo\n" );
			sql.append(" where 1 = 1\n" );
			sql.append(" and oo.duty_type = "+Constant.VIEW_NEWS_type_1+"\n" );
			sql.append(" and oo.status != "+Constant.NEWS_STATUS_3+"\n" );
			sql.append(" and oo.org_type_id  is null");
			sql.append(" and oo.msg_type ='"+Constant.MSG_TYPE_3+"'");
			
			sql.append(" union all select "+col.toString()+"\n"); //经销商没关闭指定了经销商指定了经销商类型
			sql.append(" from tt_as_wr_news oo where oo.news_id in (\n" );
			sql.append("select O.NEWS_ID from tt_as_wr_news_org o where o.dealer_id like '%"+logonUser.getDealerId()+"%' ");
			if(null != logonUser.getDealerType() && Constant.DEALER_TYPE_DWR == logonUser.getDealerType()){
				sql.append(" and o.msg_type ='"+Constant.MSG_TYPE_2+"'");
		    }else {
			 sql.append(" and o.msg_type ='"+Constant.MSG_TYPE_1+"'");
		    }	
			sql.append(" ) and oo.status != "+Constant.NEWS_STATUS_3);  //YH 2010.12.21
		   }
		   sql.append(") oo order by is_top desc,status,create_date desc ) A\n");
		   sql.append(" WHERE ROWNUM <= 10\n");
	}		
		result = pageQuery(sql.toString(), null, getFunName());
		return result;
	}
	
	/********
	 * 根据经销商ID查询出ORG信息
	 * @param dealerId
	 * @return 
	 */
	public  List<Map<String, Object>> viewOrgIdDealerId(String dealerId){
		StringBuilder sb = new StringBuilder();
		sb.append("select * from VW_ORG_DEALER_SERVICE where dealer_id in ("+dealerId+")");
		sb.append(" UNION\n");
		sb.append("SELECT * FROM VW_ORG_DEALER VOD WHERE VOD.DEALER_ID IN ("+dealerId+")");
		logger.info(sb.toString());
		List<Map<String, Object>> list = pageQuery(sb.toString(), null,getFunName());
		return list;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		TtAsWrGamePO game = new TtAsWrGamePO();
		try {
			game.setId(rs.getLong("ID"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return game;
	}

}
