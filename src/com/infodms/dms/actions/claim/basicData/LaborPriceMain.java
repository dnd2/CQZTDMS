/**   
* @Title: LaborPriceMain.java 
* @Package com.infodms.dms.actions.claim.basicData 
* @Description: TODO(工时单价维护Action) 
* @author zhumingwei   
* @date 2011-6-30 上午11:28:38 
* @version V1.0   
*/
package com.infodms.dms.actions.claim.basicData;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.support.DaoSupport;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.basicData.ClaimLaborDao;
import com.infodms.dms.dao.claim.other.BonusDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TcFuncPO;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmFineNewsPO;
import com.infodms.dms.po.TmRegionPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TrChangePriceDetailPO;
import com.infodms.dms.po.TtAsChangePriceDetailPO;
import com.infodms.dms.po.TtAsChangePricePO;
import com.infodms.dms.po.TtAsLabourPriceInfoPO;
import com.infodms.dms.po.TtAsRelationPartSeriesPO;
import com.infodms.dms.po.TtAsWrLabourPricePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: ClaimLaborMain 
 * @Description: TODO(索赔工时维护Action) 
 * @author Administrator 
 * @date 2010-6-1 上午11:05:38 
 *  
 */
public class LaborPriceMain {
	private Logger logger = Logger.getLogger(ClaimLaborMain.class);
	private final ClaimLaborDao dao = ClaimLaborDao.getInstance();
	private final BonusDAO dao1 = BonusDAO.getInstance();
	private final String LABOR_PRICE_URL = "/jsp/claim/basicData/laborPriceIndex.jsp";//主页面（查询）
	private final String LABOR_PRICE_DAY = "/jsp/claim/basicData/caimDay.jsp";//主页面（查询）
	private final String LABOR_PRICE_ADD_URL = "/jsp/claim/basicData/LaborPriceAdd.jsp";//新增页面
	private final String LABOR_PRICE_UPDATE_URL = "/jsp/claim/basicData/laborPriceUpdate.jsp";//修改页
	private final String LABOR_PRICE_INFO_URL = "/jsp/claim/basicData/laborPriceInfo.jsp";//明细页
	private final String PART_SERIES_URL = "/jsp/claim/basicData/part_series.jsp";
	private final String PART_SERIES_UPDATE_URL = "/jsp/claim/basicData/part_series_update.jsp";
	private final String OPEN_GROUP_URL = "/jsp/claim/basicData/open_group.jsp";
	private final String ADD_PART_SERIES = "/jsp/claim/basicData/add_part_series.jsp";
	private final String OPEN_PART_URL = "/jsp/claim/basicData/open_part.jsp";
	
	
	
	/**
	 * 
	* @Title: claimLaborInit 
	* @Description: TODO(索赔工时维护初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void part_series(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String COMMAND = request.getParamValue("COMMAND");
			String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
			String PART_CNAME = request.getParamValue("PART_CNAME");
			String PART_CODE = request.getParamValue("PART_CODE");
			String PART_TYPE = request.getParamValue("PART_TYPE");
			String GROUP_CODE = request.getParamValue("GROUP_CODE");
			String GROUP_NAME = request.getParamValue("GROUP_NAME");
			String PART_IS_CHANGHE = request.getParamValue("PART_IS_CHANGHE");
			TcPosePO posePO = new TcPosePO();
			posePO.setPoseId(logonUser.getPoseId());
			List<TcPosePO> list= dao.select(posePO);
			String Byld = "95411001";
			if(list.size() > 0 )
			{
				if((""+list.get(0).getPoseBusType()).equals("10781011"))
				{
					Byld = "95411002";
				}
			}
			
			if(COMMAND != null )
			{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;//处理当前页
				PageResult<Map<String, Object>> ps = dao.part_series(Constant.PAGE_SIZE, curPage,PART_OLDCODE,PART_CNAME,PART_CODE,PART_TYPE,GROUP_CODE,GROUP_NAME,PART_IS_CHANGHE,Byld);
				act.setOutData("ps", ps);
			}else
			{
				act.setForword(PART_SERIES_URL);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件车系关系出错");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void delet_part_series()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String[] ids = request.getParamValues("part_serier_id");
			for(String id : ids)
			{
				TtAsRelationPartSeriesPO partSeriesPO1 = new TtAsRelationPartSeriesPO();
				partSeriesPO1.setId(Long.parseLong(id));
				dao.delete(partSeriesPO1);
			}
			act.setForword(PART_SERIES_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件车系关系出错");
			 logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void updateintpart_series()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");
			String GROUP_ID = request.getParamValue("GROUP_ID");
			TtAsRelationPartSeriesPO partSeriesPO1 = new TtAsRelationPartSeriesPO();
			partSeriesPO1.setId(Long.parseLong(id));
			TtAsRelationPartSeriesPO partSeriesPO2 = new TtAsRelationPartSeriesPO();
			
			TmVhclMaterialGroupPO groupPO = new TmVhclMaterialGroupPO();
			groupPO.setGroupId(Long.parseLong(GROUP_ID));
			List<TmVhclMaterialGroupPO> list = dao.select(groupPO);
			partSeriesPO2.setSeriesId(list.get(0).getParentGroupId());
			partSeriesPO2.setModelId(Long.parseLong(GROUP_ID));
			partSeriesPO2.setUpdateBy(logonUser.getUserId());
			partSeriesPO2.setUpdateDate(new Date());
			dao.update(partSeriesPO1, partSeriesPO2);
		} catch (Exception e) {
			act.setOutData("cont","该配件已经属于该车系了" );
			//BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件车系关系出错");
			// logger.error(logonUser,e1);
			//act.setException(e1);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void insertpart_series()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");
			String GROUP_ID = request.getParamValue("GROUP_ID");
			String PART_ID = request.getParamValue("PART_ID");
			if(GROUP_ID != null && GROUP_ID.length() > 0)
			{
				TtAsRelationPartSeriesPO partSeriesPO = new TtAsRelationPartSeriesPO();
				partSeriesPO.setPartId(Long.parseLong(PART_ID));
				partSeriesPO.setModelId(Long.parseLong(GROUP_ID));
				if(dao.select(partSeriesPO).size() > 0)
				{
					act.setOutData("cont", "该配件已经属于该车系了" );
				}else
				{
					TtAsRelationPartSeriesPO partSeriesPO1 = new TtAsRelationPartSeriesPO();
					partSeriesPO1.setId(Long.parseLong(SequenceManager.getSequence("")));
					partSeriesPO1.setPartId(Long.parseLong(PART_ID));
					partSeriesPO1.setModelId(Long.parseLong(GROUP_ID));
					
					TmVhclMaterialGroupPO groupPO = new TmVhclMaterialGroupPO();
					groupPO.setGroupId(Long.parseLong(GROUP_ID));
					List<TmVhclMaterialGroupPO> list = dao.select(groupPO);
					partSeriesPO1.setSeriesId(list.get(0).getParentGroupId());
					partSeriesPO1.setCreateBy(logonUser.getUserId());
					partSeriesPO1.setCreateDate(new Date());
					dao.insert(partSeriesPO1);
				}
				
			}else
			{
				TmVhclMaterialGroupPO groupPO = new TmVhclMaterialGroupPO();
				groupPO.setGroupLevel(2);
				List<TmVhclMaterialGroupPO> list= dao.select(groupPO);
				for(TmVhclMaterialGroupPO  materialGroupPO : list)
				{
					TtAsRelationPartSeriesPO partSeriesPO = new TtAsRelationPartSeriesPO();
					partSeriesPO.setPartId(Long.parseLong(PART_ID));
					partSeriesPO.setModelId(materialGroupPO.getGroupId());
					List<TtAsRelationPartSeriesPO> cList = dao.select(partSeriesPO);
					if(cList!=null && cList.size()>0){
						act.setOutData("cont","该配件已经属于该车系了" );
					}else
					{
						TmVhclMaterialGroupPO groupPO01 = new TmVhclMaterialGroupPO();
						groupPO01.setParentGroupId(materialGroupPO.getGroupId());
						List<TmVhclMaterialGroupPO> list01 = dao.select(groupPO01);//查询车型,并且循环插入
						for(TmVhclMaterialGroupPO  materialGroupPO2 : list01){
							TtAsRelationPartSeriesPO partSeriesPO1 = new TtAsRelationPartSeriesPO();
							partSeriesPO1.setId(Long.parseLong(SequenceManager.getSequence("")));
							partSeriesPO1.setPartId(Long.parseLong(PART_ID));
							partSeriesPO1.setSeriesId(materialGroupPO.getGroupId());//将车系ID插入
							partSeriesPO1.setCreateBy(logonUser.getUserId());
							partSeriesPO1.setCreateDate(new Date());
							partSeriesPO1.setModelId(materialGroupPO2.getGroupId());
							dao.insert(partSeriesPO1);
						}
					}
				}
			}
			
		} catch (Exception e) {
			act.setOutData("cont","该配件已经属于该车系了" );
			//BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件车系关系出错");
			// logger.error(logonUser,e1);
			//act.setException(e1);
		}
	}
	
	public void updatepart_series(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");
			StringBuffer sql= new StringBuffer();
			sql.append("select b.PART_OLDCODE,\n" );
			sql.append("       b.PART_CNAME,\n" );
			sql.append("       b.PART_TYPE,\n" );
			sql.append("       c.GROUP_CODE,\n" );
			sql.append("       c.GROUP_ID,\n" );
			sql.append("       c.GROUP_NAME\n" );
			sql.append("  from tt_as_relation_part_series a,\n" );
			sql.append("       TT_PART_DEFINE             b,\n" );
			sql.append("       tm_vhcl_material_group     c\n" );
			sql.append(" where a.PART_ID = b.PART_ID\n" );
			sql.append("   and a.MODEL_ID = c.GROUP_ID\n" );
			sql.append("   and a.ID = "+ id);
			Map<String,Object > map= dao.pageQueryMap(sql.toString(), null, dao.getFunName());
            act.setOutData("map", map);
			act.setOutData("id", id);
			act.setForword(PART_SERIES_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件车系关系出错");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void open_part()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String COMM = request.getParamValue("COMM");
			String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
			String PART_CNAME = request.getParamValue("PART_CNAME");
			if(COMM != null )
			{
				TcPosePO posePO = new TcPosePO();
			      posePO.setPoseId(logonUser.getPoseId());
			      List<TcPosePO> list= dao.select(posePO);
			      String Byld = "95411001";
			      if(list.size() > 0 )
			      {
			        if((""+list.get(0).getPoseBusType()).equals("10781011"))
			        {
			          Byld = "95411002";
			        }
			      }
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;//处理当前页
				StringBuffer sql= new StringBuffer();
				sql.append("select t.* from tt_part_define t where t.PART_TYPE != 92021001\n" );
				sql.append("\n" );
				if(Utility.testString(PART_OLDCODE))
					sql.append(" and  t.PART_OLDCODE like '%"+PART_OLDCODE+"%'  "); 
				if(Utility.testString(PART_CNAME))
					sql.append(" and  t.PART_CNAME like '%"+PART_CNAME+"%'  "); 
				sql.append(" and t.PART_IS_CHANGHE = '"+Byld+"'");
				sql.append("UNION all\n" );
				sql.append("\n" );
				sql.append("SELECT t.* from TT_PART_DEFINE t where 1=1 \n" );
				//sql.append("and t.PART_ID not in (SELECT DISTINCT(a.PART_ID) from  tt_as_relation_part_series a  ) ");
				if(Utility.testString(PART_OLDCODE))
					sql.append(" and  t.PART_OLDCODE like '%"+PART_OLDCODE+"%'  "); 
				if(Utility.testString(PART_CNAME))
					sql.append(" and  t.PART_CNAME like '%"+PART_CNAME+"%'  "); 
				sql.append(" and t.PART_IS_CHANGHE = '"+Byld+"'");
				act.setOutData("ps", dao.pageQuery(sql.toString(), null, dao.getFunName(), 15, curPage));
				
			}else
			{
				act.setForword(OPEN_PART_URL);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	public void open_group()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String COMM = request.getParamValue("COMM");
			String GROUP_CODE = request.getParamValue("GROUP_CODE");
			String GROUP_NAME = request.getParamValue("GROUP_NAME");
			if(COMM != null )
			{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;//处理当前页
				StringBuffer sql= new StringBuffer();
				sql.append("select * from tm_vhcl_material_group t where t.Group_level = 3");
				if(Utility.testString(GROUP_CODE))
					sql.append(" and  t.GROUP_CODE like '%"+GROUP_CODE+"%'  "); 
				if(Utility.testString(GROUP_NAME))
					sql.append(" and  t.GROUP_NAME like '%"+GROUP_NAME+"%'  "); 
				act.setOutData("ps", dao.pageQuery(sql.toString(), null, dao.getFunName(), 15, curPage));
				
			}else
			{
				act.setForword(OPEN_GROUP_URL);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void LaborPriceInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(LABOR_PRICE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void add_part_series()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(ADD_PART_SERIES);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"新增配件车系关系失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void claimDay()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			TcCodePO tc = new TcCodePO();
			tc.setCodeId("94051001");
			List<TcCodePO> list= dao.select(tc);
			tc = list.get(0);
			act.setOutData("tc",tc);
			TcCodePO tc2 = new TcCodePO();
			tc2.setCodeId("94051002");
			List<TcCodePO> list2= dao.select(tc2);
			tc2 = list2.get(0);
			act.setOutData("tc2",tc2);
			TcCodePO tc3 = new TcCodePO();
			tc3.setCodeId("94051003");
			List<TcCodePO> list3= dao.select(tc3);
			tc3 = list3.get(0);
			act.setOutData("tc3",tc3);
			act.setForword(LABOR_PRICE_DAY);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔自动结算天数初始话失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	@SuppressWarnings("unchecked")
	public void updateDay()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String type= request.getParamValue("type");
			String code_id= request.getParamValue("code_id");
			String type2= request.getParamValue("type2");
			String code_id2= request.getParamValue("code_id2");
			String type3= request.getParamValue("type3");
			String code_id3= request.getParamValue("code_id3");
			TcCodePO tc = new TcCodePO();
			tc.setCodeId(code_id);
			TcCodePO tc1 = new TcCodePO();
			tc1.setCodeDesc(type);
			dao.update(tc, tc1);
			//索赔单上报时间限制
			TcCodePO c = new TcCodePO();
			TcCodePO c1 = new TcCodePO();
			c.setCodeId(code_id2);
			c1.setCodeDesc(type2);
			dao.update(c, c1);
			//工单到索赔单时间限制
			TcCodePO c2 = new TcCodePO();
			TcCodePO c3 = new TcCodePO();
			c2.setCodeId(code_id3);
			c3.setCodeDesc(type3);
			dao.update(c2, c3);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔自动结算天数修改失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	
	public void LaborPeiceAddInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(LABOR_PRICE_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void save(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String changeType = request.getParamValue("changeType");//变更类型
			String policyName = request.getParamValue("policyName");//政策名称
			String moneyType = request.getParamValue("moneyType");//加价方式
			double price = 0L;
			if(Constant.MONEY_TYPE_01.toString().equals(moneyType)){
				price = Double.parseDouble(request.getParamValue("money"));
			}
			if(Constant.MONEY_TYPE_02.toString().equals(moneyType)){
				price = Double.parseDouble(request.getParamValue("money1"))*0.01;
			}
			String policytrueTime = request.getParamValue("policytrueTime");//政策生效时间
			String policyfalseTime = request.getParamValue("policyfalseTime");//政策失效时间
			String remark = request.getParamValue("remark");//备注
			
			String[] newsIds = request.getParamValues("newsId");
			
			TtAsChangePricePO po = new TtAsChangePricePO();
			po.setId(CommonUtils.parseLong(SequenceManager.getSequence("")));
			po.setPolicyNo(SequenceManager.getSequence("PO"));
			po.setChangType(Long.parseLong(changeType));
			po.setPolicyName(policyName);
			po.setAddType(Long.parseLong(moneyType));
			po.setChangValue(price);
			po.setChangStatus(Constant.LABOUR_CHANG_STATUS_01);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				po.setPolicyStartDate(sdf.parse(policytrueTime));
				po.setPolicyEndDate(sdf.parse(policyfalseTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			//存首页新闻Id
			if(newsIds!=null){
				for(int i = 0; i < newsIds.length; i++){
					TmFineNewsPO tfn = new TmFineNewsPO();
					tfn.setFineNewsId(CommonUtils.parseLong(SequenceManager.getSequence("")));
					tfn.setFineId(po.getId());
					tfn.setNewsId(Long.valueOf(newsIds[i]));
					tfn.setCreateBy(logonUser.getUserId());
					tfn.setCreateDate(new Date());
					dao.insert(tfn);
				}
			}
			po.setRemark(remark);
			//po.setMakeDate(makeDate);
			//po.setStatus(status);//是保存或是生成
			po.setCreateBy(logonUser.getUserId());
			po.setCreateDate(new Date());
			dao.insert(po);
			
			if(changeType.equals(Constant.LABOR_CHANGE_TYPE_01.toString())){
				String [] changeId = request.getParamValues("dealer_id");	
				if(changeId!=null){
					for(int i=0;i<changeId.length;i++){
						TrChangePriceDetailPO po1 = new TrChangePriceDetailPO();
						po1.setId(CommonUtils.parseLong(SequenceManager.getSequence("")));
						po1.setLabourId(po.getId());
						po1.setChangeId(Long.parseLong(changeId[i]));
						po1.setCreateBy(logonUser.getUserId());
						po1.setCreateDate(new Date());
						dao.insert(po1);
						dao.createPriceDetail(changeId[i],po.getId(),price,policytrueTime,policyfalseTime,logonUser.getUserId(),moneyType);
					}
				}
			}
			
			if(changeType.equals(Constant.LABOR_CHANGE_TYPE_02.toString())){
				String [] region_code = request.getParamValues("region_code");
				if(region_code!=null){
					for(int i=0;i<region_code.length;i++){
						TrChangePriceDetailPO po1 = new TrChangePriceDetailPO();
						po1.setId(CommonUtils.parseLong(SequenceManager.getSequence("")));
						po1.setLabourId(po.getId());
						po1.setChangeId(Long.parseLong(region_code[i]));
						po1.setCreateBy(logonUser.getUserId());
						po1.setCreateDate(new Date());
						dao.insert(po1);
						dao.createPriceDetail1(region_code[i],po.getId(),price,policytrueTime,policyfalseTime,logonUser.getUserId(),moneyType);
					}
				}
			}
			if(changeType.equals(Constant.LABOR_CHANGE_TYPE_03.toString())){
				String [] wrgroup_id = request.getParamValues("wrgroup_id");
				if(wrgroup_id!=null){
					for(int i=0;i<wrgroup_id.length;i++){
						TrChangePriceDetailPO po1 = new TrChangePriceDetailPO();
						po1.setId(CommonUtils.parseLong(SequenceManager.getSequence("")));
						po1.setLabourId(po.getId());
						po1.setChangeId(Long.parseLong(wrgroup_id[i]));
						po1.setCreateBy(logonUser.getUserId());
						po1.setCreateDate(new Date());
						dao.insert(po1);
						dao.createPriceDetail2(wrgroup_id[i],po.getId(),price,policytrueTime,policyfalseTime,logonUser.getUserId(),moneyType);
					}
				}
			}
			act.setOutData("ok", "ok");
		}catch (Exception e) {
			act.setOutData("no", "no");
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void laborPriceQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String changeType = request.getParamValue("changeType");
			String policyName = request.getParamValue("policyName");
			String policyNo = request.getParamValue("policyNo");
			String changeStatus = request.getParamValue("changeStatus");
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.laborPriceQuery(Constant.PAGE_SIZE, curPage,changeType,policyName,policyNo,changeStatus);
			act.setOutData("ps", ps);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void laborPriceSC() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		act.getResponse().setContentType("application/json");
		try {
			String id = request.getParamValue("ID");
			long userId=logonUser.getUserId();
			//调用存储过程
			POFactory poFactory = POFactoryBuilder.getInstance();
			List ins = new LinkedList<Object>();
			ins.add(id);
			ins.add(userId);
			poFactory.callProcedure("PRICE_INFO",ins,null);
			
			TtAsChangePricePO po = new TtAsChangePricePO();
			po.setId(Long.parseLong(id));
			TtAsChangePricePO poValue = new TtAsChangePricePO();
			poValue.setChangStatus(Constant.LABOUR_CHANG_STATUS_02);
			dao.update(po, poValue);
			act.setOutData("ok", "ok");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void laborPriceEnd() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		act.getResponse().setContentType("application/json");
		try {
			String id = request.getParamValue("ID");
			long userId=logonUser.getUserId();
			TtAsLabourPriceInfoPO ip = new TtAsLabourPriceInfoPO();
			ip.setStrategyId(Long.valueOf(id));
			dao.delete(ip);
			TtAsChangePricePO po = new TtAsChangePricePO();
			po.setId(Long.parseLong(id));
			TtAsChangePricePO poValue = new TtAsChangePricePO();
			poValue.setChangStatus(Constant.LABOUR_CHANG_STATUS_03);
			poValue.setEndsBy(userId);
			poValue.setEndsDate(new Date());
			dao.update(po, poValue);
			act.setOutData("ok", "ok");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void updateLaborPrice(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		act.getResponse().setContentType("application/json");
		String id = request.getParamValue("ID");
		TtAsChangePricePO po = new TtAsChangePricePO();
		po.setId(Long.parseLong(id));
		TtAsChangePricePO poValue = (TtAsChangePricePO)dao.select(po).get(0);
		act.setOutData("poValue", poValue);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		act.setOutData("startDate", sdf.format(poValue.getPolicyStartDate()));
		act.setOutData("endDate", sdf.format(poValue.getPolicyEndDate()));
		
		if(poValue.getChangType()==Constant.LABOR_CHANGE_TYPE_01.longValue()){
			List<Map<String,Object>> changeDetail = dao.changeDetail(Long.parseLong(id));
			String dealerId = "";
			String dealerCode = "";
			for (int i=0;i<changeDetail.size();i++){
				Map map = (Map)changeDetail.get(i);
				dealerId = dealerId+map.get("DEALER_ID").toString()+",";
				dealerCode = dealerCode+map.get("DEALER_CODE").toString()+",";
			}
			act.setOutData("dealerId", dealerId);
			act.setOutData("dealerCode", dealerCode);
		}
		if(poValue.getChangType()==Constant.LABOR_CHANGE_TYPE_02.longValue()){
			List<Map<String,Object>> changeDetail = dao.changeDetail1(Long.parseLong(id));
			String regionCode = "";
			String regionName = "";
			for (int i=0;i<changeDetail.size();i++){
				Map map = (Map)changeDetail.get(i);
				regionCode = regionCode+map.get("REGION_CODE").toString()+",";
				regionName = regionName+map.get("REGION_NAME").toString()+",";
			}
			act.setOutData("regionCode", regionCode);
			act.setOutData("regionName", regionName);
		}
		
		if(poValue.getChangType()==Constant.LABOR_CHANGE_TYPE_03.longValue()){
			List<Map<String,Object>> changeDetail = dao.changeDetail2(Long.parseLong(id));
			String wrGroupId = "";
			String wrGroupName = "";
			for (int i=0;i<changeDetail.size();i++){
				Map map = (Map)changeDetail.get(i);
				wrGroupId = wrGroupId+map.get("WRGROUP_ID").toString()+",";
				wrGroupName = wrGroupName+map.get("WRGROUP_NAME").toString()+",";
			}
			act.setOutData("wrGroupId", wrGroupId);
			act.setOutData("wrGroupName", wrGroupName);
		}
		
		List<Map<String,Object>> listNews = dao1.dlrQueryEncourageNewsDetail(id);
		act.setOutData("listNews", listNews);
		
		act.setOutData("ID", id);
		act.setForword(LABOR_PRICE_UPDATE_URL);
	}
	
	public void update(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String id = request.getParamValue("changeId");
			
			String changeType = request.getParamValue("changeType");//变更类型
			String policyName = request.getParamValue("policyName");//政策名称
			String moneyType = request.getParamValue("moneyType");//加价方式
			double price = 0L;
			if(Constant.MONEY_TYPE_01.toString().equals(moneyType)){
				price = Double.parseDouble(request.getParamValue("money"));
			}
			if(Constant.MONEY_TYPE_02.toString().equals(moneyType)){
				price = Double.parseDouble(request.getParamValue("money1"))*0.01;
			}
			String policytrueTime = request.getParamValue("policytrueTime");//政策生效时间
			String policyfalseTime = request.getParamValue("policyfalseTime");//政策失效时间
			String remark = request.getParamValue("remark");//备注
			
			String[] newsIds = request.getParamValues("newsId");
			
			TtAsChangePricePO po = new TtAsChangePricePO();
			po.setId(Long.parseLong(id));
			TtAsChangePricePO poValue = new TtAsChangePricePO();
			
			
			poValue.setChangType(Long.parseLong(changeType));
			poValue.setPolicyName(policyName);
			poValue.setAddType(Long.parseLong(moneyType));
			poValue.setChangValue(price);
			poValue.setChangStatus(Constant.LABOUR_CHANG_STATUS_01); 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				poValue.setPolicyStartDate(sdf.parse(policytrueTime));
				poValue.setPolicyEndDate(sdf.parse(policyfalseTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			//首页新闻			
//			TmFineNewsPO tfnpo = new TmFineNewsPO();
//			tfnpo.setFineId(po.getId());
//			dao.delete(tfnpo);
			
//			if(newsIds!=null){
//				for(int i = 0; i < newsIds.length; i++){
//					TmFineNewsPO tfn = new TmFineNewsPO();
//					tfn.setFineNewsId(CommonUtils.parseLong(SequenceManager.getSequence("")));
//					tfn.setFineId(po.getId());
//					tfn.setNewsId(Long.valueOf(newsIds[i]));
//					tfn.setCreateBy(logonUser.getUserId());
//					tfn.setCreateDate(new Date());
//					dao.insert(tfn);
//				}
//			}
			poValue.setRemark(remark);
			poValue.setUpdateBy(logonUser.getUserId());
			poValue.setUpdateDate(new Date());
			dao.update(po,poValue);
			
			TrChangePriceDetailPO dpo = new TrChangePriceDetailPO();
			dpo.setLabourId(po.getId());
			dao.delete(dpo);
			
			TtAsChangePriceDetailPO cppo = new TtAsChangePriceDetailPO();
			cppo.setLabourId(po.getId());
			dao.delete(cppo);
			
			if(changeType.equals(Constant.LABOR_CHANGE_TYPE_01.toString())){
				String [] changeId = request.getParamValues("dealer_id");
				if(changeId!=null){
					for(int i=0;i<changeId.length;i++){
						TrChangePriceDetailPO po1 = new TrChangePriceDetailPO();
						po1.setId(CommonUtils.parseLong(SequenceManager.getSequence("")));
						po1.setLabourId(po.getId());
						po1.setChangeId(Long.parseLong(changeId[i]));
						po1.setCreateBy(logonUser.getUserId());
						po1.setCreateDate(new Date());
						dao.insert(po1);
						dao.createPriceDetail(changeId[i],po.getId(),price,policytrueTime,policyfalseTime,logonUser.getUserId(),moneyType);
					}
				}
			}
			
			if(changeType.equals(Constant.LABOR_CHANGE_TYPE_02.toString())){
				String [] region_code = request.getParamValues("region_code");	
				if(region_code!=null){
					for(int i=0;i<region_code.length;i++){
						TrChangePriceDetailPO po1 = new TrChangePriceDetailPO();
						po1.setId(CommonUtils.parseLong(SequenceManager.getSequence("")));
						po1.setLabourId(po.getId());
						po1.setChangeId(Long.parseLong(region_code[i]));
						po1.setCreateBy(logonUser.getUserId());
						po1.setCreateDate(new Date());
						dao.insert(po1);
						dao.createPriceDetail1(region_code[i],po.getId(),price,policytrueTime,policyfalseTime,logonUser.getUserId(),moneyType);
					}
				}
			}
			if(changeType.equals(Constant.LABOR_CHANGE_TYPE_03.toString())){
				String [] wrgroup_id = request.getParamValues("wrgroup_id");
				if(wrgroup_id!=null){
					for(int i=0;i<wrgroup_id.length;i++){
						TrChangePriceDetailPO po1 = new TrChangePriceDetailPO();
						po1.setId(CommonUtils.parseLong(SequenceManager.getSequence("")));
						po1.setLabourId(po.getId());
						po1.setChangeId(Long.parseLong(wrgroup_id[i]));
						po1.setCreateBy(logonUser.getUserId());
						po1.setCreateDate(new Date());
						dao.insert(po1);
						dao.createPriceDetail2(wrgroup_id[i],po.getId(),price,policytrueTime,policyfalseTime,logonUser.getUserId(),moneyType);
					}
				}
			}
			act.setOutData("ok", "ok");
		}catch (Exception e) {
			act.setOutData("no", "no");
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void VerificationName(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String policyName = request.getParamValue("policyName");
		List<Map<String,Object>> getSize = dao.VerificationName(policyName);
		if(getSize.size()>0){
			act.setOutData("error", "error");
		}
	}
	public void laborPriceInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		act.getResponse().setContentType("application/json");
		String id = request.getParamValue("ID");
		TtAsChangePricePO po = new TtAsChangePricePO();
		po.setId(Long.parseLong(id));
		TtAsChangePricePO poValue = (TtAsChangePricePO)dao.select(po).get(0);
		act.setOutData("poValue", poValue);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		act.setOutData("startDate", sdf.format(poValue.getPolicyStartDate()));
		act.setOutData("endDate", sdf.format(poValue.getPolicyEndDate()));
		
		if(poValue.getChangType()==Constant.LABOR_CHANGE_TYPE_01.longValue()){
			List<Map<String,Object>> changeDetail = dao.changeDetail(Long.parseLong(id));
			String dealerId = "";
			String dealerCode = "";
			for (int i=0;i<changeDetail.size();i++){
				Map map = (Map)changeDetail.get(i);
				dealerId = dealerId+map.get("DEALER_ID").toString()+",";
				dealerCode = dealerCode+map.get("DEALER_CODE").toString()+",";
			}
			act.setOutData("dealerId", dealerId);
			act.setOutData("dealerCode", dealerCode);
		}
		if(poValue.getChangType()==Constant.LABOR_CHANGE_TYPE_02.longValue()){
			List<Map<String,Object>> changeDetail = dao.changeDetail1(Long.parseLong(id));
			String regionCode = "";
			String regionName = "";
			for (int i=0;i<changeDetail.size();i++){
				Map map = (Map)changeDetail.get(i);
				regionCode = regionCode+map.get("REGION_CODE").toString()+",";
				regionName = regionName+map.get("REGION_NAME").toString()+",";
			}
			act.setOutData("regionCode", regionCode);
			act.setOutData("regionName", regionName);
		}
		
		if(poValue.getChangType()==Constant.LABOR_CHANGE_TYPE_03.longValue()){
			List<Map<String,Object>> changeDetail = dao.changeDetail2(Long.parseLong(id));
			String wrGroupId = "";
			String wrGroupName = "";
			for (int i=0;i<changeDetail.size();i++){
				Map map = (Map)changeDetail.get(i);
				wrGroupId = wrGroupId+map.get("WRGROUP_ID").toString()+",";
				wrGroupName = wrGroupName+map.get("WRGROUP_NAME").toString()+",";
			}
			act.setOutData("wrGroupId", wrGroupId);
			act.setOutData("wrGroupName", wrGroupName);
		}
		
		List<Map<String,Object>> listNews = dao1.dlrQueryEncourageNewsDetail(id);
		act.setOutData("listNews", listNews);
		
		act.setOutData("ID", id);
		act.setForword(LABOR_PRICE_INFO_URL);
	}
}