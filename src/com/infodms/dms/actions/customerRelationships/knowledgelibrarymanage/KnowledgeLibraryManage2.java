package com.infodms.dms.actions.customerRelationships.knowledgelibrarymanage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.KnowLedgeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TtCrmKnowledgePO;
import com.infodms.dms.po.TtCrmKnowledgeTypePO;
import com.infodms.dms.util.TC_CodeAddUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : KnowledgeLibraryManage 
 * @Description   : TODO 
 * @author        : guozg
 * CreateDate     : 2013-4-2
 */
public class KnowledgeLibraryManage2 {
	         
	private static Logger logger = Logger.getLogger(KnowledgeLibraryManage2.class);
	private final String knowledgeLibraryManageUrl = "/jsp/customerRelationships/knowledgeLibraryManage/knowledgeLibraryManage.jsp";
	private final String knowledgeLibraryUpdateUrl="/jsp/customerRelationships/knowledgeLibraryManage/knowledgeLibraryUpdate.jsp";
	private final String knowledgeLibraryAddUrl="/jsp/customerRelationships/knowledgeLibraryManage/knowledgeLibraryAdd.jsp";
	private final String knowledgeLibraryQueryUrl="/jsp/customerRelationships/knowledgeLibraryManage/knowledgeLibraryQuery.jsp";
	private final String klTypeManageUrl="/jsp/customerRelationships/knowledgeLibraryManage/klTypeManage.jsp";
	private final String klTypeAddUrl="/jsp/customerRelationships/knowledgeLibraryManage/klTypeAddInit.jsp";
	private final String klTypeUpdateUrl="/jsp/customerRelationships/knowledgeLibraryManage/klTypeUpdateInit.jsp";
	private final String knowledgeLibShow="/jsp/customerRelationships/knowledgeLibraryManage/knowledgeLibShow.jsp";
	private KnowLedgeDao dao = KnowLedgeDao.getInstance() ;
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 知识库管理页面 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void knowledgeLibraryManageInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {				
			String isAdmin = dao.IsAdmin(logonUser.getUserId());
			act.setOutData("isAdmin", isAdmin);
			String selectBox=TC_CodeAddUtil.genSelBoxExp(Constant.KNOW_MANAGE,"KG_KIND");
			List<Map<String,Object>> typeList = dao.getTypeList(logonUser.getUserId());
			act.setOutData("selectBox", selectBox);			
			act.setOutData("typeList", typeList);
			act.setForword(knowledgeLibraryManageUrl);
			} catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"知识库管理");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 知识库管理页面查询功能 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void knowledgeLibraryManageQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			
			String keyName = request.getParamValue("KEY_NAME");
			String knowType = request.getParamValue("KG_TYPE");
			String knowKind = request.getParamValue("KG_KIND");
			String isAdmin = dao.IsAdmin(logonUser.getUserId());
			System.out.println("-----isAdmin="+isAdmin);
			act.setOutData("isAdmin", isAdmin);
			int pageSize = 10 ;
			int curPage = request.getParamValue("curPage") != null ? 
					Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String,Object>> ps = dao.knowLedgeMainQuery( pageSize, curPage,keyName,knowType,knowKind,logonUser.getUserId()) ;
			
			act.setOutData("ps",ps);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"知识库查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 知识库管理页面修改 跳转
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void knolibUpdateInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();

		try{
			
			String kgId = request.getParamValue("kgid");
			List<Map<String,Object>> typeList = dao.getAddTypeList(logonUser.getUserId());
			act.setOutData("typeList", typeList);
			String selectBox=TC_CodeAddUtil.genSelBoxExp(Constant.KNOW_MANAGE,"KG_KIND",kgId);  
			Map<String, Object> map = dao.getKnowledgeInfobyId(kgId);
			FsFileuploadPO detail = new FsFileuploadPO();
			detail.setYwzj(Long.valueOf(kgId));
			List<FsFileuploadPO> lists = dao.select(detail);			
			act.setOutData("selectBox", selectBox);
			act.setOutData("lists",lists);
			act.setOutData("map", map);
			act.setForword(knowledgeLibraryUpdateUrl);
		}catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"知识库修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 	
	 * @Title      : 
	 * @Description: 知识库管理页面新增跳转
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void knowledgeAddInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		List<Map<String,Object>> typeList = dao.getAddTypeList(logonUser.getUserId());
		act.setOutData("typeList", typeList);
		String selectBox=TC_CodeAddUtil.genSelBoxExp(Constant.KNOW_MANAGE,"KG_KIND");
		try {	
			act.setOutData("selectBox", selectBox);			
			act.setForword(knowledgeLibraryAddUrl);
			} catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"知识库新增");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 知识库新增实现
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	@SuppressWarnings("unchecked")
	public void knowledgeAddFact()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String kgTopic = request.getParamValue("KG_TOPIC");
			String kgType = request.getParamValue("KG_TYPE");
			String kgSignTime = request.getParamValue("KG_SIGN_TIME");
			String kgMemo = request.getParamValue("KG_MEMO");
			kgTopic=kgTopic==null?"":kgTopic;
			kgType=kgType==null?"":kgType;
			kgSignTime=kgSignTime==null?"":kgSignTime;
			kgMemo=kgMemo==null?"":kgMemo;
			SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
			Date date = sdf.parse( kgSignTime);
			Long kg_Id=Long.parseLong(SequenceManager.getSequence(""));
			logger.info("-----------kg_Id="+kg_Id);
			Date createDate= new Date();
			
			TtCrmKnowledgePO po = new TtCrmKnowledgePO(); 
			po.setKgId(kg_Id);
			po.setKgTopic(kgTopic);
			po.setKgType(Long.parseLong(kgType));
			po.setKgMemo(kgMemo);
			po.setKgSignTime(date);
			po.setCreateBy(logonUser.getUserId());
			po.setCreateDate(createDate);
			po.setKgStatus(Constant.STATE_MANAGE_1);
			logger.info("-----insert start");
			dao.insert(po);
			
			//将附件保存
			kg_Id = po.getKgId();
			String[] fjids = request.getParamValues("fjid");//获取文件ID
				FileUploadManager.fileUploadByBusiness(kg_Id.toString(), fjids, logonUser);	
			act.setOutData("msg", "01");
			//act.setForword(knowledgeLibraryManageUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"知识库新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 知识库管理页面删除
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void knowledgeDelete()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String code [] = request.getParamValues("code");//获取一个集合要删除的ID
			dao.knowledgeDelete(code);
			act.setOutData("msg", "01");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"知识库删除");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 知识库管理页面审核 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void knowledgeAuthority()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String code [] = request.getParamValues("code");//获取一个集合要审批的ID
			dao.knowledgeAuthorityUpdate(code,logonUser);
			act.setOutData("msg", "01");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"知识库删除");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 	
	 * @Title      : 
	 * @Description: 知识库管理修改实现 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void knowledgeUpdateFact()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String kgid = request.getParamValue("KG_ID");
			String kgTopic = request.getParamValue("KG_TOPIC");
			String kgType = request.getParamValue("KG_TYPE");
			String kgSignTime = request.getParamValue("KG_SIGN_TIME");
			String kgMemo = request.getParamValue("KG_MEMO");
			kgTopic=kgTopic==null?"":kgTopic;
			kgType=kgType==null?"":kgType;
			kgSignTime=kgSignTime==null?"":kgSignTime;
			kgMemo=kgMemo==null?"":kgMemo;
			SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
			String sysdate=sdf.format(new Date());
			StringBuffer sql= new StringBuffer();
			sql.append(" update TT_CRM_KNOWLEDGE set KG_TOPIC='"+kgTopic+"',");
			sql.append(" KG_TYPE="+kgType+",");
			sql.append(" KG_STATUS="+Constant.STATE_MANAGE_1+",");
			sql.append(" KG_SIGN_TIME=to_date('"+kgSignTime+"','yyyy-MM-dd'),");
			sql.append(" UPDATE_DATE=to_date('"+sysdate+"','yyyy-MM-dd'),");
			sql.append(" KG_MEMO='"+kgMemo+"',");
			sql.append(" UPDATE_BY="+logonUser.getUserId());
			sql.append(" where KG_ID= "+kgid);
			System.out.println(sql.toString());
			dao.knowledgeUpdateBuId(sql.toString());
			
			String[] fjids = request.getParamValues("fjid");
			if(kgid!=null&&!kgid.equals("")){//修改的时候
				FileUploadManager.delAllFilesUploadByBusiness(kgid, fjids);
				FileUploadManager.fileUploadByBusiness(kgid, fjids, logonUser);	
			}else{
				FileUploadManager.fileUploadByBusiness(kgid, fjids, logonUser);	
			}
			act.setOutData("msg", "01");
		}catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"知识库修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 知识库查询页面跳转 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-3
	 */
	public void knowledgelibrarySearch()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		List<Map<String,Object>> typeList = dao.getTypeList(logonUser.getUserId());		
		String selectBox=TC_CodeAddUtil.genSelBoxExp(Constant.KNOW_MANAGE,"KG_KIND");
		try{
			act.setOutData("selectBox", selectBox);
			act.setOutData("typeList", typeList);
			act.setForword(knowledgeLibraryQueryUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"知识库查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 知识库查询实现 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void knowledgelibrarySearchDetail()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{			
			String kgtopic = request.getParamValue("KG_TOPIC");
			String knowType = request.getParamValue("KG_TYPE");
			String knowKind = request.getParamValue("KG_KIND");
			String knowStatus= request.getParamValue("KG_STATUS");
			String chkTitle = request.getParamValue("chkTitle");
			String chkText = request.getParamValue("chkText");
			int pageSize = 10 ;
			int curPage = request.getParamValue("curPage") != null ? 
					Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String,Object>> ps = dao.knowLedgeQuery( pageSize, curPage,kgtopic,knowType,knowKind,knowStatus,chkTitle,chkText,logonUser.getUserId()) ;
			act.setOutData("ps",ps);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"知识库查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 知识库类型管理页面跳转
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-3
	 */
	public void klTypeManage()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String selectBox=TC_CodeAddUtil.genSelBoxExp(Constant.KNOW_MANAGE,"KG_KIND");
			List<Map<String,Object>> typeList = dao.getTypeList(logonUser.getUserId());
			act.setOutData("selectBox", selectBox);
			String isAdmin = dao.IsAdmin(logonUser.getUserId());
			act.setOutData("isAdmin", isAdmin);
			act.setForword(klTypeManageUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"知识库类型管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 知识库类型管理查询 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void klTypeManageQuery()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{			
			String typeName = request.getParamValue("typeName");
			String kind = request.getParamValue("KG_KIND");
			String isAdmin = dao.IsAdmin(logonUser.getUserId());
			act.setOutData("isAdmin", isAdmin);
			int pageSize = 10 ;
			int curPage = request.getParamValue("curPage") != null ? 
					Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String,Object>> ps = dao.klTypeQuery( pageSize, curPage,typeName,kind, logonUser.getUserId()) ;
			act.setOutData("ps",ps);
		}catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"知识库类型管理查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 	
	 * @Title      : 
	 * @Description: 知识库类型新增页面跳转 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void klTypeAddInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		KnowLedgeDao dao=KnowLedgeDao.getInstance();
		try{
			List<Map<String,Object>> kindList = dao.getKindList(logonUser.getUserId());
			act.setOutData("kindList", kindList);
			act.setForword(klTypeAddUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"知识库类型新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 知识库类型新增实现 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	@SuppressWarnings("unchecked")
	public void klTypeAddFact()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			Long typeId=Long.parseLong(SequenceManager.getSequence(""));
			String typeName = request.getParamValue("TYPE_NAME");
			Long kindId =Long.parseLong(request.getParamValue("kindId"));
			typeName=typeName==null?"":typeName;
			Map<String,Object> num = dao.isExist(typeId,typeName);
			if(null!=num&&(Integer.parseInt(num.get("NUM").toString())>0)){
				act.setOutData("msg", "02");
			}else{
				Date createDate= new Date();
				TtCrmKnowledgeTypePO po= new TtCrmKnowledgeTypePO();
				po.setTypeId(typeId);
				po.setTypeName(typeName);
				po.setKind(kindId);
				po.setCreateBy(logonUser.getUserId());
				po.setCreateDate(createDate);
				po.setStatus(Constant.STATUS_ENABLE);
				dao.insert(po);
				act.setOutData("msg", "01");
			}
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"知识库新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 知识库类型 删除 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void klTypeDelete()
	{

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String code [] = request.getParamValues("code");//获取一个集合要删除的ID
			dao.klTypeDelete(code);
			act.setOutData("msg", "01");
		}catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"知识库新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 修改数据库类型页面跳转
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void klTypeUpdateInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{			
			String typeId = request.getParamValue("typeId");
			List<Map<String,Object>> kindList = dao.getKindList(logonUser.getUserId());
			act.setOutData("kindList", kindList);			
			Map<String, Object> map = dao.getklTypeInfobyId(typeId);
			act.setOutData("map", map);
			act.setForword(klTypeUpdateUrl);
		}catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"知识库类型修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 修改数据库类型实际操作
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void klTypeUpdateFact()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			Long typeId = Long.parseLong(request.getParamValue("typeId"));
			String typeName = request.getParamValue("TYPE_NAME");
			String kindId=request.getParamValue("kindId");
			typeName=typeName==null?"":typeName;
			kindId=kindId==null?"":kindId;
			Map<String,Object> num = dao.isExist(typeId,typeName);
			if(null!=num&&(Integer.parseInt(num.get("NUM").toString())>0)){
				act.setOutData("msg", "02");
			}else{
				SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
				String sysdate=sdf.format(new Date());
				StringBuffer sql= new StringBuffer();
				sql.append(" update tt_crm_knowledge_type set TYPE_NAME='"+typeName+"',KIND="+kindId+",   ");
				sql.append(" UPDATE_DATE=to_date('"+sysdate+"','yyyy-MM-dd'),");
				sql.append(" UPDATE_BY="+logonUser.getUserId());
				sql.append(" where TYPE_ID= "+typeId);
				dao.klUpdateById(sql.toString());
				act.setOutData("msg", "01");
			}
		}catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"知识库类型修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 知识库查询结果查看 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void knowledgeLibShow(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{			
			String kgId = request.getParamValue("kgid");
			Map<String, Object> map = dao.getKnowledgeInfobyId(kgId);
			FsFileuploadPO detail = new FsFileuploadPO();
			detail.setYwzj(Long.valueOf(kgId));
			List<FsFileuploadPO> lists = dao.select(detail);
			act.setOutData("lists",lists);
			act.setOutData("map", map);
			act.setForword(knowledgeLibShow);
		}catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"知识库修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
