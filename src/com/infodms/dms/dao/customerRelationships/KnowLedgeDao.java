package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.AreaProvinceBean;
import com.infodms.dms.bean.PriceAdjustBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrLabourPricePO;
import com.infodms.dms.po.TtAsWrModelGroupPO;
import com.infodms.dms.po.TtAsWrPriceAdjustPO;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.POContext;
/**
 * 
 * @ClassName     : KnowLedgeDao 
 * @Description   : TODO 
 * @author        : guozg
 * CreateDate     : 2013-4-6
 */
public class KnowLedgeDao extends BaseDao {
	private KnowLedgeDao(){}
	protected POFactory factory = POFactoryBuilder.getInstance();
	public static KnowLedgeDao getInstance(){
		return new KnowLedgeDao();
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}	

	/**
	 * 
	 * @Title      : 
	 * @Description: 知识库管理页面查询 
	 * @param      : @param pageSize
	 * @param      : @param curPage
	 * @param      : @param keyName
	 * @param      : @param knowType
	 * @param      : @return
	 * @param      : @throws Exception      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> knowLedgeMainQuery(int pageSize, int curPage, String keyName,String knowType,String knowKind,Long userId) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select ROWNUM as NUM ,to_char(ge.kg_id) kg_id, TYPE_NAME,KIND,ge.kg_topic,to_char(ge.kg_sign_time,'yyyy-mm-dd') kg_sign_time, kg_status  \n");
		sql.append(" from tt_crm_knowledge ge,TT_CRM_KNOWLEDGE_TYPE TT ");
		sql.append(" where ge.KG_TYPE=tt.TYPE_ID ");
		sql.append(" and (tt.KIND="+Constant.KNOW_MANAGE_1+" or (tt.KIND="+Constant.KNOW_MANAGE_2+" and  ge.create_by="+userId+")) ");
		if(null!=keyName&&!(keyName.equals(""))){
			sql.append(" and KG_TOPIC like '%"+keyName+"%'");
		}
		if(null!=knowType&&(!knowType.equals(""))&&(!"0".equals(knowType))){
			sql.append(" and KG_TYPE = "+knowType+"");
		}
		if(null!=knowKind&&(!knowKind.equals(""))&&(!"0".equals(knowKind))){
			sql.append(" and KIND = "+knowKind+"");
		}
		
		System.out.println(sql.toString());
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;		
	
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 知识库类型查询 
	 * @param      : @param pageSize
	 * @param      : @param curPage
	 * @param      : @param codeDesc
	 * @param      : @return
	 * @param      : @throws Exception      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>>klTypeQuery(int pageSize, int curPage, String typeName,String kind, Long userId) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select t.TYPE_ID,t.TYPE_NAME,KIND from tt_crm_knowledge_type t where 1=1 and ");
		sql.append(" (t.kind = decode((select SE_IS_MANAMGER from tt_crm_seats where SE_USER_ID="+userId);
		sql.append(" ),"+Constant.se_is_manamger_1+", kind ,"+Constant.KNOW_MANAGE_1);
		sql.append(" ) or T.CREATE_BY = DECODE((SELECT SE_IS_MANAMGER FROM TT_CRM_SEATS WHERE SE_USER_ID = "+userId);
		sql.append(" ), "+Constant.se_is_manamger_1+", T.CREATE_BY, "+userId+") ) and t.status="+Constant.STATUS_ENABLE);
		if(null!=typeName&&(!"".equals(typeName))){
			sql.append(" and t.TYPE_NAME like '%"+typeName+"%'");
		}
		if(null!=kind&&(!"".equals(kind))&&(!kind.equals("0"))){
			sql.append(" and t.KIND='"+kind+"'");
		}		
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;		
	}
	
    public List<Map<String, Object>> getKindList(Long userId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select CODE_ID as KIND_ID,CODE_DESC as KIND_NAME from tc_code t where 1=1 and t.status="+Constant.STATUS_ENABLE+" and t.type="+Constant.KNOW_MANAGE +" and CODE_ID=decode((select SE_IS_MANAMGER from tt_crm_seats where SE_USER_ID="+userId+"),"+Constant.se_is_manamger_1+",CODE_ID,"+Constant.KNOW_MANAGE_2+")" );       
        List<Map<String, Object>> kindList = this.pageQuery(sql.toString(), null, this.getFunName());
        return kindList;
    }	
    
    public List<Map<String, Object>> getAddTypeList(Long userId) {
    	StringBuffer sql = new StringBuffer();
        sql.append("select TYPE_ID,TYPE_NAME from TT_CRM_KNOWLEDGE_TYPE t where 1=1 and t.status="+Constant.STATUS_ENABLE);    
		//2013-11-18 wangming 坐席也可以新增公共知识库
        //sql.append(" t.kind = decode((select SE_IS_MANAMGER from tt_crm_seats where SE_USER_ID="+userId);
		//sql.append(" ),"+Constant.se_is_manamger_1+", kind ,"+Constant.KNOW_MANAGE_2 + ") and t.status="+Constant.STATUS_ENABLE);

        List<Map<String, Object>> typeList = this.pageQuery(sql.toString(), null, this.getFunName());
        return typeList;
    }	
	
    public List<Map<String, Object>> getTypeList(Long userId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select TYPE_ID,TYPE_NAME from TT_CRM_KNOWLEDGE_TYPE t where 1=1 and ");    
		sql.append(" (t.kind = decode((select SE_IS_MANAMGER from tt_crm_seats where SE_USER_ID="+userId);
		sql.append(" ),"+Constant.se_is_manamger_1+", kind ,"+Constant.KNOW_MANAGE_1);
		sql.append(" ) or T.CREATE_BY = DECODE((SELECT SE_IS_MANAMGER FROM TT_CRM_SEATS WHERE SE_USER_ID = "+userId);
		sql.append(" ), "+Constant.se_is_manamger_1+", T.CREATE_BY, "+userId+") ) and t.status="+Constant.STATUS_ENABLE);
        List<Map<String, Object>> typeList = this.pageQuery(sql.toString(), null, this.getFunName());
        return typeList;
    }	    
    
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 知识库查询 
	 * @param      : @param pageSize
	 * @param      : @param curPage
	 * @param      : @param kgtopic
	 * @param      : @param knowType
	 * @param      : @param knowStatus
	 * @param      : @param chkTitle
	 * @param      : @param chkText
	 * @param      : @return
	 * @param      : @throws Exception      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */ 
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> knowLedgeQuery(int pageSize, int curPage, String kgtopic,String knowType,String knowKind,String knowStatus,String chkTitle,String chkText,Long userId) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select ROWNUM as NUM,to_char(ge.kg_id) kg_id, TYPE_NAME,KIND,ge.kg_topic,to_char(ge.kg_sign_time,'yyyy-mm-dd') kg_sign_time, kg_status  \n");
		sql.append(" from tt_crm_knowledge ge,TT_CRM_KNOWLEDGE_TYPE TT ");
		sql.append(" where ge.KG_TYPE=tt.TYPE_ID ");
		sql.append(" and (tt.KIND="+Constant.KNOW_MANAGE_1+" or (tt.KIND="+Constant.KNOW_MANAGE_2+" and  ge.create_by="+userId+")) ");
		if((null!=knowType)&&(!knowType.equals(""))&&(!knowType.equals("0"))){
			sql.append(" and KG_TYPE = "+knowType+"");
		}
		if((null!=knowKind)&&(!knowKind.equals(""))&&(!knowKind.equals("0"))){
			sql.append(" and KIND = "+knowKind+"");
		}		
		if((null!=knowStatus)&&(!knowStatus.equals(""))){
			sql.append(" and KG_STATUS = "+knowStatus+"");
		}
		if(((null!=chkTitle)&&(!"".equals(chkTitle)))&&((null!=chkText)&&(!"".equals(chkText)))){
			if((null!=kgtopic)&&!(kgtopic.equals(""))){
				sql.append(" and( KG_TOPIC like '%"+kgtopic+"%'");
				sql.append(" or KG_MEMO like '%"+kgtopic+"%')");
			}
		}else if((null!=chkTitle)&&(!"".equals(chkTitle)))
		{
			if((null!=kgtopic)&&!(kgtopic.equals(""))){
				sql.append(" and KG_TOPIC like '%"+kgtopic+"%'");
			}
		}else if((null!=chkText)&&(!"".equals(chkText)))
		{
			if((null!=kgtopic)&&(!kgtopic.equals(""))){
				sql.append(" and KG_MEMO like '%"+kgtopic+"%'");
			}
		}else{
			if((null!=kgtopic)&&(!kgtopic.equals(""))){
				sql.append(" and KG_TOPIC like '%"+kgtopic+"%'");
			}
		}
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;		
	
	}	
	/**
	 * 
	 * @Title      : 
	 * @Description: 知识库审核 
	 * @param      : @param code      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public  void knowledgeAuthorityUpdate(String[] code,AclUserBean logonUser)
	{
		StringBuffer sql= new StringBuffer();
		sql.append(" update TT_CRM_KNOWLEDGE set KG_STATUS="+Constant.STATE_MANAGE_2+" ,CHECK_BY="+logonUser.getUserId()+" ,CHECK_DATE=SYSDATE where KG_ID in(");
		for(String c:code)
		{
			sql.append(c);
			sql.append(",");
		}
		sql.setCharAt(sql.lastIndexOf(","), ')');
		System.out.println(sql.toString());
		factory.update(sql.toString(),null);
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 知识库删除 
	 * @param      : @param code      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void  knowledgeDelete(String[] code)
	{
		StringBuffer sql= new StringBuffer();
		sql.append(" delete  from  TT_CRM_KNOWLEDGE  where KG_ID in(");
		for(String c:code)
		{
			sql.append(c);
			sql.append(",");
		}
		sql.setCharAt(sql.lastIndexOf(","), ')');
		factory.delete(sql.toString(),null);
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据ID查询知识库 
	 * @param      : @param kgid
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public  Map<String, Object> getKnowledgeInfobyId(String kgid)
	{
		StringBuffer sql= new StringBuffer();
		sql.append(" select to_char(kl.kg_id) kg_id,TYPE_ID, TYPE_NAME ,KIND,kl.kg_topic,to_char(kl.kg_sign_time,'yyyy-mm-dd') kg_sign_time,kl.kg_memo kg_memo from tt_crm_knowledge kl,tt_crm_knowledge_type tt where kl.kg_type=tt.type_id  ");
		if((kgid!=null)&&!("".equals(kgid))){
			sql.append(" and  kl.kg_id="+kgid);
		}
		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 根据codeId 查询知识库类型名称
	 * @param      : @param codeId
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public  Map<String, Object> getklTypeInfobyId(String typeId)
	{
		StringBuffer sql= new StringBuffer();
		sql.append(" select t.type_id,t.type_name,t.KIND from tt_crm_knowledge_type t where 1=1  ");
		if((null!=typeId)&&(!"".equals(typeId))){
			sql.append(" and  t.type_id="+typeId);
		}
		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 知识库修改 
	 * @param      : @param sql      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void knowledgeUpdateBuId(String sql)
	{
		factory.update(sql,null);
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 知识库类型删除 
	 * @param      : @param code      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void klTypeDelete(String[] code)
	{
		StringBuffer sql= new StringBuffer();
		sql.append(" update  tt_crm_knowledge_type set  status ="+Constant.STATUS_DISABLE+"  where TYPE_ID in(");
		for(String c:code)
		{
			sql.append(c);
			sql.append(",");
		}
		sql.setCharAt(sql.lastIndexOf(","), ')');
		System.out.println(sql.toString());
		factory.delete(sql.toString(),null);
		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 知识库类型修改 
	 * @param      : @param sql    修改 语句
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void klUpdateById(String sql)
	{
		factory.update(sql,null);
	}

	public Map<String, Object> isExist(Long typeId,String typeName){
		Map<String, Object>  map = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select count(c.TYPE_NAME) as num from tt_crm_knowledge_type c where 1=1 ");
		sql.append(" and c.status ="+Constant.STATUS_ENABLE);
		sql.append(" and c.TYPE_NAME ='"+typeName+"' and TYPE_ID!="+typeId);
		
			map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 判断是否为Admin用户
	 * @param      : @param sql    修改 语句
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public String IsAdmin(Long userId)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("select count(*) as NUM from tt_crm_seats where SE_IS_MANAMGER=95221001 and SE_USER_ID="+userId);
		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		
		if(map.get("NUM").toString().equals("0")){
			return "0";
		}else{
			return "1";
		}
	}	
}
