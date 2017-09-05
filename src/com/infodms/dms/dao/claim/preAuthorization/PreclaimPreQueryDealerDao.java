/**   
* @Title: PreclaimPreQueryDealerDao.java 
* @Package com.infodms.dms.dao.claim.preAuthorization 
* @Description: TODO(索赔预申请状态查询DAO) 
* @author wangjinbao   
* @date 2010-6-25 下午07:34:30 
* @version V1.0   
*/
package com.infodms.dms.dao.claim.preAuthorization;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.ConditionBean;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: PreclaimPreQueryDealerDao 
 * @Description: TODO(索赔预申请状态查询DAO)(经销商端) 
 * @author wangjinbao 
 * @date 2010-6-25 下午07:34:30 
 *  
 */
public class PreclaimPreQueryDealerDao extends BaseDao {
	public static Logger logger = Logger.getLogger(PreclaimPreQueryDealerDao.class);
	private static final PreclaimPreQueryDealerDao dao = new PreclaimPreQueryDealerDao ();
	public static final PreclaimPreQueryDealerDao getInstance() {
		return dao;
	}
	/**
	 * 
	* @Title: preclaimPreQuery 
	* @Description: TODO(索赔预申请状态查询方法) 
	* @param @param pageSize
	* @param @param curPage
	* @param @param bean
	* @param @return
	* @param @throws Exception   
	* @return PageResult<Map<String,Object>>  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> preclaimPreQuery(Long companyId,String dealerId,int pageSize, int curPage, ConditionBean bean) throws Exception {
		PageResult<Map<String, Object>> result = null;
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append(" select t2.id,td.dealer_code,td.dealer_shortname,\n" );
		sql.append(" t1.vin,t1.ro_no,t1.approval_date,t1.approval_person,\n" );
		sql.append(" t2.item_type,t2.item_code,t2.item_desc,t2.status,t2.fid,t1.fo_no \n" );
		sql.append(" from TT_AS_WR_FOREAPPROVAL t1,tm_dealer td,TT_AS_WR_FOREAPPROVALITEM t2\n" );
		sql.append(" where t1.dealer_id = td.dealer_id\n" );
		sql.append(" and t2.status is not null\n");
		//modify at 2010-07-19 start 
		sql.append(" and t1.dealer_id = "+dealerId+"\n" );
		//modify end		
		sql.append(" and t1.id = t2.fid\n" );
		if(Utility.testString(bean.getConOne())){//经销商id不为空
			sql.append(" and t1.dealer_id = "+bean.getConOne()+"");	
		}		
		if(Utility.testString(bean.getConTwo())){//工单号
			sql.append(" and t1.ro_no like '%"+bean.getConTwo()+"%' \n");
		}
		if(Utility.testString(bean.getConNine())){//预授权单号
			sql.append(" and t2.fid like '%"+bean.getConNine()+"%' \n");
		}		
		if(Utility.testString(bean.getConThree())){//VIN
//			sql.append(" and t1.vin like '%"+bean.getConThree()+"%'\n");
			sql.append(GetVinUtil.getVins(bean.getConThree(),"t1"));
		}
		if(Utility.testString(bean.getConFour())){//项目编码
			sql.append(" and upper(t2.item_code) like '%"+bean.getConFour().toUpperCase()+"%'\n");
		}
		if(Utility.testString(bean.getConFive())){//项目类型
			sql.append(" and t2.item_type = "+bean.getConFive()+"\n");
		}
		if(Utility.testString(bean.getConSix())){//处理状态
			sql.append(" and t2.status ="+bean.getConSix()+"\n");
		}
		if(Utility.testString(bean.getConSeven())){//申请日期起
			sql.append(" and t1.approval_date >= to_date('"+bean.getConSeven()+"', 'yyyy-MM-dd')\n");
		}		
		if(Utility.testString(bean.getConEight())){//申请日期止
			sql.append(" and t1.approval_date <= to_date('"+bean.getConEight()+"', 'yyyy-MM-dd')\n");
		}		
		sql.append(" order by t1.id desc");

		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return result;
	}
	/**
	 * 
	* @Title: getPreclaimById 
	* @Description: TODO(根据子表ID查询索赔预申请的相关信息) 
	* @param @param id  ：TT_AS_WR_FOREAPPROVALITEM 表的id（子表）
	* @param @return   
	* @return HashMap  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public HashMap getPreclaimById(Long companyId,String dealerId,String id){
		HashMap map = new HashMap();
		StringBuffer sql= new StringBuffer();
		sql.append(" select t2.id,td.dealer_code,td.dealer_shortname, \n" );
		sql.append(" t1.vin,t1.ro_no,t1.approval_date, \n" );
		sql.append(" t2.item_type,t2.item_code,t2.item_desc,t2.status ,t2.CLAIM_TYPE,  \n" );
		sql.append(" t1.deliverer,t1.approval_person,t2.dealer_remark,t2.check_remark,t2.fid,  \n" );
		sql.append(" t1.license_no,t1.engine_no,t1.brand_code,t1.series_code,t1.model_code,\n" );
		sql.append(" t1.keep_beg_date,t1.yieldly,t1.dest_clerk,t1.in_factory_date,t1.in_mileage,\n" );
		sql.append(" t1.approval_phone,t1.approval_type,vm.BRAND_NAME,vm.SERIES_NAME,vm.MODEL_NAME\n" );
		sql.append(" from TT_AS_WR_FOREAPPROVAL t1,tm_dealer td,TT_AS_WR_FOREAPPROVALITEM t2,\n" );
		sql.append(" VW_MATERIAL_GROUP vm, TM_VEHICLE v\n" );
		sql.append(" where t1.dealer_id = td.dealer_id\n" );
		sql.append("and v.series_id = vm.SERIES_ID\n" );
		sql.append("and v.model_id = vm.MODEL_ID\n" );
		sql.append("and v.package_id = vm.PACKAGE_ID\n" );
		sql.append("and t1.vin = v.vin\n" );	
		//modify at 2010-07-19 start 
		sql.append(" and t1.dealer_id = "+dealerId+"\n" );
		//modify end		
		sql.append(" and t1.id = t2.fid\n" );
		sql.append(" and t2.id = "+id+" \n" );

		List<Map<String, Object>> relist = pageQuery(sql.toString(), null,getFunName());
		if(relist != null && relist.size() > 0){
			map = (HashMap)relist.get(0);
		}
		return map;
	}
	/**
	 *                                     
	* @Title: queryAttById 
	* @Description: TODO(通过ID查询附件) 
	* @param @param id
	* @param @return    设定文件 
	* @return List<FsFileuploadPO>    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List<FsFileuploadPO> queryAttById(String id) {
		StringBuffer sql = new StringBuffer();
		List<FsFileuploadPO> ls = new ArrayList<FsFileuploadPO>();
		sql.append(" SELECT A.* FROM FS_FILEUPLOAD A ");
		//sql.append(" LEFT OUTER JOIN FS_FILEUPLOAD B ON B.YWZJ = A.ID ");
		sql.append(" WHERE 1=1 " );
		sql.append(" AND A.YWZJ='"+id+"'");
		ls = select (FsFileuploadPO.class,sql.toString(),null);
		return ls;
	}	

	/* (非 Javadoc) 
	 * <p>Title: wrapperPO</p> 
	 * <p>Description: </p> 
	 * @param rs
	 * @param idx
	 * @return 
	 * @see com.infodms.dms.dao.common.BaseDao#wrapperPO(java.sql.ResultSet, int) 
	 */
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO 自动生成方法存根
		return null;
	}

}
