package com.infodms.dms.dao.claim.auditing;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.infodms.dms.actions.claim.auditing.rule.custom.RuleVO;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmDownParameterPO;
import com.infodms.dms.po.TmPtPartTypePO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtAsActivityEvaluatePO;
import com.infodms.dms.po.TtAsWrAppauthitemPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrAuthinfoPO;
import com.infodms.dms.po.TtAsWrAuthmonitorlabPO;
import com.infodms.dms.po.TtAsWrAuthmonitorpartPO;
import com.infodms.dms.po.TtAsWrAuthmonitortypePO;
import com.infodms.dms.po.TtAsWrClaimAutoPO;
import com.infodms.dms.po.TtAsWrGamePO;
import com.infodms.dms.po.TtAsWrGamefeePO;
import com.infodms.dms.po.TtAsWrLabouritemPO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infodms.dms.po.TtAsWrRuleitemPO;
import com.infodms.dms.po.TtAsWrRulemappingPO;
import com.infodms.dms.po.TtAsWrWrauthorizationPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;

/**
 * 索赔申请单自动审核DAO
 * @author XZM
 */
@SuppressWarnings("unchecked")
public class ClaimAuditingDao extends BaseDao {
	
	private static ClaimAuditingDao auditiongDao;
	
	private ClaimAuditingDao(){
		super();
	}
	
	public static ClaimAuditingDao getInstance(){
		if(auditiongDao==null){
			auditiongDao = new ClaimAuditingDao();
		}
		return auditiongDao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	/**
	 * 根据状态查询索赔申请单信息(上报时间小于)
	 * 修改  2010-10-24  现在不限制上报的索赔单数
	 * @param status 索赔申请单状态
	 * @param hour 上报时间距离当前时间小时数
	 * @return List<PO> 
	 */
	public List<TtAsWrApplicationPO> queryReportedClaim(Integer status,Integer hour){
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT *\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A\n" );
		sql.append("WHERE 1=1\n" );
		//sql.append("AND A.REPORT_DATE < (SYSDATE-"+hour+"/24)\n" );
		sql.append("AND A.STATUS = " + status);
		
		return this.select(TtAsWrApplicationPO.class, sql.toString(), null);
	}
	
	/**
	 * 根据索赔申请单ID查询索赔申请单信息
	 * @param claimId
	 * @return TtAsWrApplicationPO
	 */
	public TtAsWrApplicationPO queryClaimById(String claimId){
		
		TtAsWrApplicationPO resultPO = null;
		TtAsWrApplicationPO conditionPO = new TtAsWrApplicationPO();
		conditionPO.setId(Long.parseLong(claimId));
		
		List<PO> resultList = this.select(conditionPO);
		
		if(resultList!=null && resultList.size()>0){
			resultPO = (TtAsWrApplicationPO) resultList.get(0);
		}
		
		return resultPO;
	}

	/**
	 * 查询对应索赔申请单需要预授权的配件信息
	 * 注：要满足已下发条件
	 * @param claimId 索赔申请单ID
	 * @param wrgroupId 配件车型组ID
	 * @param mainFlag 主要配件标识别 null或-1：不限制 其他：按传入状态查询
	 * @param companyId 所属公司ID区分轿车和微车
	 * @return List<Map<String, Object>>
	 *         信息字段：ID[索赔配件ID]、PART_CODE[配件代码]、PART_NAME[配件名称]、IS_MAINPART[主要配件标识]
	 */
    public List<Map<String, Object>> queryNeedAuthPartByClaimId(String claimId,String wrgroupId,Integer mainFlag,Long companyId){
    	
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT A.ID, A.PART_CODE, A.PART_NAME, A.IS_MAINPART\n" );
    	sql.append("FROM TT_AS_WR_PARTSITEM A\n" );
    	sql.append("WHERE 1 = 1\n" );
    	sql.append("AND A.ID = ?\n" );
    	sql.append("AND EXISTS (SELECT B.PART_CODE\n" );
    	sql.append("FROM TT_AS_WR_FOREAPPROVALPT B\n" );
    	sql.append("WHERE B.PART_CODE = A.PART_CODE\n" );
    	sql.append("AND B.MODEL_GROUP = ?\n" );
    	sql.append("AND B.IS_SEND = ?)\n");

    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(claimId);
    	paramList.add(CommonUtils.checkNull(wrgroupId));
    	paramList.add(Constant.DOWNLOAD_CODE_STATUS_03);//查询已下发的
    	
    	if(mainFlag!=null && mainFlag!=-1){//不限制配件是否为主要配件或附加配件
    		sql.append("AND A.IS_MAINPART = ?");
    		paramList.add(mainFlag);
    	}

    	return this.pageQuery(sql.toString(), paramList, this.getFunName());
    }
    
    /**
     * 查询对应索赔申请单需要预授权的工时信息
     * 注：要满足已下发条件
     * @param claimId 索赔申请单ID
     * @param wrgroupId 车型组ID
     * @param mainFlag 主要配件标识别 null或-1：不限制 其他：按传入状态查询
     * @param companyId 所属公司ID区分轿车和微车
     * @return List<Map<String, Object>>
     *         信息字段：ID[索赔工时ID]、WR_LABOURCODE[工时代码]、WR_LABOURNAME[工时名称]、
     *         IS_MAINLABOUR[主要工时标识]
     */
    public List<Map<String, Object>> queryNeedAuthManHourByClaimId(String claimId,String wrgroupId,
    		Integer mainFlag,Long companyId){
    	
    	String sql = "SELECT A.ID,A.WR_LABOURCODE,A.WR_LABOURNAME,A.IS_MAINLABOUR\n" +
			    		" FROM TT_AS_WR_LABOURITEM A,TT_AS_WR_FOREAPPROVALLAB B\n" +
			    		" WHERE 1=1\n" +
			    		" AND A.WR_LABOURCODE = B.OPERATION_CODE\n" +
			    		" AND A.ID = ?\n" +
			    		" AND B.IS_SEND = ?\n" +
			    		" AND B.WRGROUP_ID = ?\n"  ;
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(claimId);
    	paramList.add(Constant.DOWNLOAD_CODE_STATUS_03);//查询已下发的
    	paramList.add(CommonUtils.checkNull(wrgroupId));
    	
    	if(mainFlag!=null && mainFlag!=-1){//不限制配件是否为主要工时或附加工时
    		sql = sql + " AND A.IS_MAINLABOUR = ?\n";
    		paramList.add(mainFlag);
    	}

    	return this.pageQuery(sql, paramList, this.getFunName());
    }
    
    /**
     * 查询对应索赔申请单需要预授权的其他项目信息
     * 注：要满足已下发条件
     * modify 2010-07-23 XZM : 索赔预授权 其他项目 不需要控制下发状态
     * @param claimId 索赔申请单ID
     * @param companyId 所属公司ID区分轿车和微车
     * @return List<Map<String, Object>>
     *         信息字段：ID[其他项目ID]、ITEM_CODE[项目代码]、ITEM_DESC[项目名称]
     */
    public List<Map<String, Object>> queryNeedAuthOtherByClaimId(String claimId,Long companyId){
    	
    	String sql = "SELECT A.ID,A.ITEM_CODE,A.ITEM_DESC\n" +
    		" FROM TT_AS_WR_NETITEM A,TT_AS_WR_FOREAPPROVALOTHERITEM B\n" +
    		" WHERE 1=1\n" +
    		" AND A.ITEM_CODE = B.ITEM_CODE\n" +
    		" AND A.ID = ?\n" ;
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(claimId);
    	//paramList.add(Constant.DOWNLOAD_CODE_STATUS_03);//查询已下发的

    	return this.pageQuery(sql, paramList, this.getFunName());
    }
    
    /**
     * 查询某索赔申请单授权项目明细
     * 包括：配件\工时\其他项目
     * @param roNo 工单号
     * @param itemType 项目类型
     * @param status 预授权审核状态
     * @param vin 车辆代码
     * @param companyId 所属公司ID区分轿车和微车
     * @return List<Map<String, Object>>
     *         信息字段：ID[预授权ID]、RO_NO[工单号]、ITEM_CODE[项目代码]、ITEM_DESC[项目名称]、
     *         AUTH_CODE[授权代码]、ITEM_TYPE[项目类型]
     */
    public List<Map<String, Object>> queryAuthInfoByClaimId(String roNo,String itemType,
    		String status,String vin,Long companyId){
    	
    	String sql = "SELECT A.ID,A.RO_NO,B.ITEM_CODE,B.ITEM_DESC,B.AUTH_CODE,B.ITEM_TYPE\n"+
						" FROM TT_AS_WR_FOREAPPROVAL A,TT_AS_WR_FOREAPPROVALITEM B\n"+
						" WHERE 1=1\n"+
						" AND A.ID = B.FID\n"+
						" AND A.RO_NO = ?\n"+
						" AND B.ITEM_TYPE = ?\n" +
						" AND B.STATUS = ?\n" +
						" AND A.VIN = ?\n";
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(CommonUtils.checkNull(roNo));
    	paramList.add(CommonUtils.checkNull(itemType));
    	paramList.add(CommonUtils.checkNull(status));
    	paramList.add(CommonUtils.checkNull(vin));

    	return this.pageQuery(sql, paramList, this.getFunName());
    }
    
    /**
     * 按三包策略查询指定业务类型三包规则，并取得指定配件质保期信息
     * 相同配件设定的规则取对应最大的质保时间和质保里程
     * 注：现在保养费用设定更改为使用三包规则
     * @param ruleStatus 三包规则状态
     * @param claimId 索赔申请单ID
     * @param modelId 车型ID
     * @param provinceId 省份ID
     * @param purchasedDate 实效日期
     * @param companyId 所属公司ID区分轿车和微车
     * @return List<Map<String, Object>>
     *         信息字段：PART_CODE[配件代码]、GURN_MONTH[质保时间]、GURN_MILE[质保里程]
     */
    public List<Map<String, Object>> queryQualityByPart(Integer ruleStatus,Long claimId,
    		Long modelId,Long provinceId,Date purchasedDate,Long companyId){
    	
    	List<Map<String, Object>> resultList = null;
    	if(ruleStatus==null || claimId==null 
    			|| modelId==null || provinceId==null || purchasedDate ==null)
    		return resultList;
    	
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT B.PART_CODE,\n" );
    	sql.append("       MAX(CLAIM_MONTH) GURN_MONTH,\n" );
    	sql.append("       MAX(CLAIM_MELIEAGE) GURN_MILE\n" );
    	sql.append("  FROM TT_AS_WR_RULE A, TT_AS_WR_RULE_LIST B\n" );
    	sql.append(" WHERE A.ID = B.RULE_ID\n" );
    	sql.append("   AND A.RULE_STATUS = ?\n" );
    	sql.append("   AND A.RULE_TYPE = ?\n" );
    	sql.append("   AND A.COMPANY_ID = ?\n" );
    	sql.append("   AND EXISTS (SELECT A1.ID\n" );
    	sql.append("          FROM TT_AS_WR_PARTSITEM A1\n" );
    	sql.append("         WHERE 1 = 1\n" );
    	sql.append("           AND A1.PART_CODE = B.PART_CODE\n" );
    	sql.append("           AND A1.ID = ?)\n" );
    	sql.append("   AND EXISTS (SELECT A2.ID\n" );
    	sql.append("          FROM TT_AS_WR_GAME A2\n" );
    	sql.append("         WHERE A2.ID IN (SELECT MAX(A1.ID)\n" );
    	sql.append("                           FROM TT_AS_WR_GAME A1\n" );
    	sql.append("                          WHERE 1 = 1\n" );
    	sql.append("                            AND EXISTS (SELECT C1.ID\n" );
    	sql.append("                                   FROM TT_AS_WR_GAMEMODEL C1\n" );
    	sql.append("                                  WHERE C1.GAME_ID = A1.ID\n" );
    	sql.append("                                    AND C1.MODEL_ID = ?)\n" );
    	sql.append("                            AND EXISTS (SELECT D1.ID\n" );
    	sql.append("                                   FROM TT_AS_WR_GAMEPRO D1\n" );
    	sql.append("                                  WHERE D1.GAME_ID = A1.ID\n" );
    	sql.append("                                    AND D1.PROVINCE_ID = ?)\n" );
    	sql.append("                            AND A1.START_DATE <= ?\n" );
    	sql.append("                            AND A1.END_DATE >= ?)\n" );
    	sql.append("           AND A2.RULE_ID = A.ID)\n" );
    	sql.append(" GROUP BY B.PART_CODE");

    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(ruleStatus);
    	paramList.add(Constant.RULE_TYPE_02);//业务类型规则
    	paramList.add(companyId);//限制公司
    	paramList.add(claimId);
    	paramList.add(modelId);
    	paramList.add(provinceId);
    	paramList.add(purchasedDate);
    	paramList.add(purchasedDate);
    	
    	resultList = this.pageQuery(sql.toString(), paramList, this.getFunName());
    	return resultList;
    }

    /**
     * 按三包策略查询指定系统类型三包规则，并取得指定配件质保期信息
     * 相同配件设定的规则取对应最大的质保时间和质保里程
     * 注：现在保养费用设定更改为使用三包规则
     * @param ruleStatus 三包规则状态
     * @param claimId 索赔申请单ID
     * @param companyId 所属公司ID区分轿车和微车
     * @return List<Map<String, Object>>
     *         信息字段：PART_CODE[配件代码]、GURN_MONTH[质保时间]、GURN_MILE[质保里程]
     */
    public List<Map<String, Object>> querySystemQualityByPart(Integer ruleStatus,Long claimId,Long companyId){
    	
    	List<Map<String, Object>> resultList = null;
    	if(ruleStatus==null || claimId==null)
    		return resultList;
    	
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT B.PART_CODE,MAX(CLAIM_MONTH) GURN_MONTH,MAX(CLAIM_MELIEAGE) GURN_MILE\n" );
    	sql.append("  FROM TT_AS_WR_RULE A, TT_AS_WR_RULE_LIST B\n" );
    	sql.append(" WHERE A.ID = B.RULE_ID\n" );
    	sql.append("   AND A.RULE_STATUS = ?\n" );
    	sql.append("   AND A.RULE_TYPE = ?\n" );
    	sql.append("   AND A.COMPANY_ID = ?\n" );
    	sql.append("   AND EXISTS (SELECT A1.ID FROM TT_AS_WR_PARTSITEM A1\n" );
    	sql.append("              WHERE 1=1\n" );
    	sql.append("              AND A1.PART_CODE = B.PART_CODE\n" );
    	sql.append("              AND A1.ID = ?)\n" );
    	sql.append("GROUP BY B.PART_CODE");

    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(ruleStatus);
    	paramList.add(Constant.RULE_TYPE_01);//系统类型规则
    	paramList.add(companyId);//限制公司
    	paramList.add(claimId);
    	
    	resultList = this.pageQuery(sql.toString(), paramList, this.getFunName());
    	return resultList;
    }
    
    /**
     * 查询索赔机基本参数
     * @param dealerId 经销商ID
     * @param paramCode 参数类型
     * @param companyId 所属公司ID区分轿车和微车
     * @return TmDownParameterPO
     */
    public TmDownParameterPO queryDownParameter(Long dealerId,Integer paramCode,Long companyId){
    	
    	TmDownParameterPO parameterPO = new TmDownParameterPO();
    	parameterPO.setDealerId(dealerId);
    	parameterPO.setParameterCode(paramCode.toString());
    	parameterPO.setOemCompanyId(companyId);
    	
    	List<PO> resList = this.select(parameterPO);
    	
    	TmDownParameterPO resultPO = new TmDownParameterPO();
    	if(resList!=null && resList.size()>0){
    		resultPO = (TmDownParameterPO) resList.get(0);
    	}
    	
    	return resultPO;
    }
    
    /**
     * 查询索赔申请单中索赔工时同故障代码的关系
     * 注：索赔工时需要根据车型组合工时代码才能确定一条记录
     * <pre>
     * 查询结果：
	 *         KEY: "MANHOUR"  设定过故障代码对应工时代码集合 
	 *              "RELATION" 工时代码和故障代码对应关系
	 *         VALUE:
	 *             MANHOUR- {Map<String,String>：KEY[工时代码],VALUE[工时代码]}
	 *             RELATION- {Map<String,String>：KEY[工时代码+"_#$#$_"+故障代码],VALUE[故障代码]}
	 * </pre>
	 * @param modelId 车型组ID
     * @param claimId 索赔申请单ID
     * @param companyId 所属公司ID区分轿车和微车
     * @return Map<String,Map<String,String>>
     */
    public Map<String,Map<String,String>> queryRelationOfManhourAndTrouble(String modelId,
    		String claimId,Long companyId){
    	
    	String sql = "SELECT A.WR_LABOURCODE,D.CODE,A.IS_MAINLABOUR \n"+
						" FROM TT_AS_WR_LABOURITEM A,TT_AS_WR_WRLABINFO B,TT_AS_WR_TROBLE_MAP C,TM_BUSINESS_CHNG_CODE D\n"+
						" WHERE 1=1\n"+
						" AND A.WR_LABOURCODE = B.LABOUR_CODE\n"+
						" AND B.ID = C.LABOR_ID\n"+
						" AND C.TROUBLE_ID = D.BUSINESS_CODE_ID\n"+
						" AND B.WRGROUP_ID =? \n" +
						" AND A.ID = ?";
    	
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(CommonUtils.checkNull(modelId));
    	paramList.add(claimId);
    	
    	List<Map<String, Object>> relationList = this.pageQuery(sql, paramList, this.getFunName());
    	
    	Map<String,String> manhourMap = new HashMap<String, String>();//设定过故障代码对应工时代码集合
    	Map<String,String> relaMap = new HashMap<String, String>();   //工时代码和故障代码对应关系
    	
    	if(relationList!=null && relationList.size()>0){
    		for (Map<String, Object> map : relationList) {
    			String labourCode = (String)map.get("WR_LABOURCODE");
    			String troubleCode = (String)map.get("CODE");
    			relaMap.put(labourCode+"_#$#$_"+troubleCode, troubleCode);
    			manhourMap.put(labourCode, labourCode);
			}
    	}
    	
    	Map<String,Map<String,String>> resMap = new HashMap<String, Map<String,String>>();
    	resMap.put("MANHOUR", manhourMap);
    	resMap.put("RELATION", relaMap);
    	
    	return resMap;
    }
    
    /**
     * 根据VIN查询对应车辆信息和所属车型组信息
     * @param vin 车辆唯一标识码
     * @param wrGroupType 车型组类型（索赔车型组或配件车型组）
     * @param companyId 所属公司ID区分轿车和微车
     * @return Map<String,Object> 
     *         信息字段：VIN、MODEL_ID[车型ID]、SERIES_ID[车系ID]、PACKAGE_ID[配件ID]、
     *         WRGROUP_ID[车型组]、WRGROUP_CODE[车型组代码]、WRGROUP_NAME[车型组名称]、
     *         WRGROUP_TYPE[车型组类型]
     */
    public Map<String,Object> queryVehicleByVin(String vin,String wrGroupType,Long companyId){
    	
    	String sql = "SELECT A.VIN,A.MODEL_ID,A.SERIES_ID,A.PACKAGE_ID,B.WRGROUP_ID,B.WRGROUP_CODE," +
    			        " B.WRGROUP_NAME,B.WRGROUP_TYPE\n"+
						" FROM TM_VEHICLE A,TT_AS_WR_MODEL_GROUP B,TT_AS_WR_MODEL_ITEM C\n"+
						" WHERE 1=1\n"+
						" AND A.package_id = C.MODEL_ID\n"+
						" AND C.WRGROUP_ID = B.WRGROUP_ID\n"+
						" AND B.WRGROUP_TYPE = ?\n"+
						" AND A.VIN = ?\n";
    	
    	List<Object> paramsList = new ArrayList<Object>();
    	paramsList.add(CommonUtils.checkNull(wrGroupType));
    	paramsList.add(CommonUtils.checkNull(vin));
    	
    	List<Map<String,Object>> resultList = this.pageQuery(sql, paramsList, this.getFunName());
    	Map<String,Object> resultMap = new HashMap<String, Object>();
    	
    	if(resultList!=null && resultList.size()>0){
    		resultMap = resultList.get(0);
    	}
    	
    	return resultMap;
    }

    /**
     * 查询对应索赔申请单对应的索赔工时信息
     * @param claimId 索赔申请单ID
     * @param mainFlag 工时标识 null或-1 查询全部，其他查询对应状态工时
     * @return List<PO>
     */
    public List<PO> queryManhourByClaimid(String claimId,Integer mainFlag){
    	
    	TtAsWrLabouritemPO paramPO = new TtAsWrLabouritemPO();
    	paramPO.setId(Long.parseLong(claimId));
    	
    	if(mainFlag!=null && mainFlag!=-1){
    		paramPO.setIsMainlabour(mainFlag);
    	}
    	
    	List<PO> resultList = this.select(paramPO);
    	return resultList;
    }
    
    /**
     * 查询对应索赔申请单对应的索赔配件信息
     * @param claimId 索赔申请单ID
     * @param mainFlag 配件标识 null或-1 查询全部，其他查询对应状态配件
     * @return List<PO> 
     */
    public List<PO> queryPartsByClaimid(String claimId,Integer mainFlag){
    	
    	TtAsWrPartsitemPO paramPO = new TtAsWrPartsitemPO();
    	paramPO.setId(Long.parseLong(claimId));

    	if(mainFlag!=null && mainFlag!=-1){
    		paramPO.setIsMainpart(mainFlag);
    	}
    	
    	List<PO> resultList = this.select(paramPO);
    	return resultList;
    }

    /**
     * 查询对应索赔单中工时是否在工时表中
     * @param modeId 车型组ID
     * @param claimId 索赔申请单ID
     * @param isMain null或-1：检测全部工时 其他：检测主要工时或附加工时
     * @param companyId 所属公司ID区分轿车和微车
     * @return List<TtAsWrWrlabinfoPO>
     */
    public List<TtAsWrLabouritemPO> queryLabInfo(String modeId,Long claimId,Integer isMain,Long companyId){
    	
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT A.*\n" );
    	sql.append("  FROM TT_AS_WR_LABOURITEM A\n" );
    	sql.append(" WHERE 1 = 1\n" );
    	sql.append("   AND EXISTS (SELECT *\n" );
    	sql.append("          FROM TT_AS_WR_WRLABINFO B\n" );
    	sql.append("         WHERE 1 = 1\n" );
    	sql.append("           AND B.LABOUR_CODE = A.WR_LABOURCODE\n" );
    	sql.append("           AND B.WRGROUP_ID = ?\n" );
    	sql.append("           AND B.IS_DEL = ?)\n" );
    	sql.append("   AND A.ID = ?\n");

    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(modeId);
    	paramList.add(Constant.IS_DEL_00);//未删除
    	paramList.add(claimId);

    	if(isMain!=null && isMain!=-1){
        	sql.append("   AND A.IS_MAINLABOUR = ?");
        	paramList.add(isMain);
    	}
    	
    	return this.select(TtAsWrLabouritemPO.class, sql.toString(), paramList);
    }
    
    /**
     * 查询索赔申请单中配件在配件基础数据中存在的配件信息
     * 注：用于验证该配件是否存在
     * @param vin 车辆唯一标识
     * @param parts 配件代码集合，以","分隔
     * @param companyId 所属公司ID（区分轿车和微车公司）
     * @return Map<String,String>
     *         信息字段：KEY:PART_CODE[配件代码]，VALUE:PART_NAME[配件名称]
     */
    public Map<String,String> queryPartsFromPartBase(String vin,String parts,Long companyId){
    	
    	List<Map<String,Object>> resList = this.queryPartsDetailFromPartBase(vin, parts,companyId);
    	
    	Map<String,String> result = new HashMap<String, String>();
    	
    	if(resList!=null && resList.size()>0){
    		for (Map<String, Object> map : resList) {
    			result.put((String)map.get("PART_CODE"), (String)map.get("PART_NAME"));
			}
    	}
    	
    	return result;
    }
    
    /**
     * 查询索赔申请单中配件在配件基础数据中存在的配件明细
     * @param vin 车辆唯一标识
     * @param parts 配件代码集合，以","分隔
     * @param companyId 所属公司ID（区分轿车和微车公司）
     * @return List<Map<String,Object>>
     *         信息字段：TM_PT_PART_BASE表字段信息
     */
    public List<Map<String,Object>> queryPartsDetailFromPartBase(String vin,String parts,Long companyId){
    	
    	/*//[修改] 2010-08-04 XZM 长安项目配件暂时不同车型关联
    	 * String sql = "SELECT B.* \n"+
		" FROM TM_VEHICLE A,TM_PT_PART_BASE B\n"+
		" WHERE 1=1\n"+
		" AND A.MODEL_ID = B.GROUP_ID \n"+
		" AND B.PART_CODE IN (" + CommonUtils.checkNull(parts) + ")\n"+
		" AND A.VIN = ?\n" +
		" AND B.IS_DEL = ?";*/
    	String sql = "SELECT B.* \n"+
		" FROM TM_PT_PART_BASE B\n"+
		" WHERE 1=1\n"+
		" AND B.PART_CODE IN (" + CommonUtils.checkNull(parts) + ")\n"+
		" AND B.IS_DEL = ?\n";

		List<Object> paramList = new ArrayList<Object>();
		//paramList.add(CommonUtils.checkNull(vin));
		paramList.add(Constant.IS_DEL_00);//查询未删除
		
		List<Map<String,Object>> resList = this.pageQuery(sql, paramList, this.getFunName());
		
		return resList;
    }
    
    /**
     * 根据VIN查询车辆信息
     * @param vin 车辆唯一标识码
     * @return TmVehiclePO
     */
    public TmVehiclePO queryVehicle(String vin){
    	
    	TmVehiclePO paramPO = new TmVehiclePO();
    	paramPO.setVin(vin);
try {
	CommonUtils.jugeVinNull(vin);
} catch (Exception e) {
	// TODO: handle exception
}
    	List<PO> resList = this.select(paramPO);
    	
    	TmVehiclePO resultPO = null;
    	
    	if(resList!=null && resList.size()>0){
    		resultPO = (TmVehiclePO) resList.get(0);
    	}
    	
    	return resultPO;
    }

    /**
     * 查询指定索赔申请单 之前 申请 的最大里程数和 之后 申请 的最小里程
     * @param createDate 该申请单申请时间(上报时间)
     * @param vin 车辆唯一标识
     * @param claimStatus 索赔申请单状态（排除该类型的）
     * @param companyId 所属公司ID区分轿车和微车
     * @return List<Map<String,Object>>
     *         信息字段：Map<String,Object> KEY: TYPE[BEFORE or AFTER]\IN_MILEAGE[里程数]
     */
    public List<Map<String,Object>> checkClaimMileage(Date createDate,String vin,
    		String claimStatus,Long companyId){
    	
    	String sql = "SELECT 'BEFORE' TYPE,MAX(IN_MILEAGE) AS IN_MILEAGE\n"+
						" FROM TT_AS_WR_APPLICATION \n"+
						" WHERE 1=1\n"+
						" AND REPORT_DATE <= ?\n"+
						" AND STATUS NOT IN (" + claimStatus + ")\n"+
						" AND VIN = ?\n"+
						" GROUP BY VIN\n"+
						" UNION ALL\n"+
						" SELECT 'AFTER' TYPE,MIN(IN_MILEAGE) AS IN_MILEAGE\n"+
						" FROM TT_AS_WR_APPLICATION \n"+
						" WHERE 1=1"+
						" AND REPORT_DATE >= ?\n"+
						" AND STATUS NOT IN (" + claimStatus + ")\n"+
						" AND VIN = ?\n"+
						" GROUP BY VIN\n";
    	
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(createDate);
    	paramList.add(CommonUtils.checkNull(vin));
    	paramList.add(createDate);
    	paramList.add(CommonUtils.checkNull(vin));
    	
    	List<Map<String,Object>> resList = this.pageQuery(sql, paramList, this.getFunName());
    	return resList;
    }

    /**
     * 查询重复的索赔申请单工单信息(排除指定状态)
     * @param conditionPO 工单号、行号、经销商ID
     * @param status 索赔申请单状态
     * @param companyId 所属公司ID区分轿车和微车
     * @return List<TtAsWrApplicationPO>
     */
    public List<TtAsWrApplicationPO> queryRepeatClaim(TtAsWrApplicationPO conditionPO,String status,Long companyId){
    	
    	String sql = "SELECT * \n" + 
						" FROM TT_AS_WR_APPLICATION\n" + 
						" WHERE 1=1\n" + 
						" AND DEALER_ID = ?\n" + 
						" AND RO_NO = ?\n" + 
						" AND LINE_NO = ?\n" + 
						" AND STATUS IN (" + status + ")\n";
    	
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(conditionPO.getDealerId());
    	paramList.add(CommonUtils.checkNull(conditionPO.getRoNo()));
    	paramList.add(conditionPO.getLineNo());
    	
    	List<TtAsWrApplicationPO> resList = this.select(TtAsWrApplicationPO.class, sql.toString(), paramList);
    	
    	return resList;
    }
    
    /**
     * 查询某辆车的主要工时维修记录
     * @param vin 车辆唯一标识码
     * @param isMain 是否检测主要工时 1主工时：0附加工时
     * @param companyId 所属公司ID区分轿车和微车
     * @param exptClaimStatus 排除的索赔单状态
     * @param claimId 索赔申请单ID
     * @return List<Map<String,Object>>
     *         信息数据：WR_LABOURCODE[工时代码]、REPAIRCOUNT[索赔次数]
     */
    public List<Map<String,Object>> checkRepeatClaimManhour(String vin,Integer isMain,
    		Long companyId,String exptClaimStatus,Long claimId){
    	
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT A.WR_LABOURCODE, COUNT(*) REPAIRCOUNT\n" );
    	sql.append("  FROM TT_AS_WR_LABOURITEM A, TT_AS_WR_APPLICATION B\n" );
    	sql.append(" WHERE 1 = 1\n" );
    	sql.append("   AND A.ID = B.ID\n" );
    	sql.append("   AND B.VIN = ?\n" );
    	sql.append("   AND A.IS_MAINLABOUR = ?\n" );
    	sql.append("   AND EXISTS (SELECT A1.WR_LABOURCODE\n" );
    	sql.append("          FROM TT_AS_WR_LABOURITEM A1\n" );
    	sql.append("         WHERE 1 = 1\n" );
    	sql.append("           AND A1.ID = ?\n" );
    	sql.append("           AND A1.IS_MAINLABOUR = ?\n" );
    	sql.append("           AND A1.WR_LABOURCODE = A.WR_LABOURCODE)\n" );
    	sql.append(" GROUP BY A.WR_LABOURCODE");
    	
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(CommonUtils.checkNull(vin));
    	paramList.add(isMain==null?-1:isMain);
    	paramList.add(claimId);
    	paramList.add(isMain==null?-1:isMain);
    	
    	List<Map<String,Object>> resList = this.pageQuery(sql.toString(), paramList, this.getFunName());
    	
    	return resList;
    }
    
    /**
     * 查询指定状态的经销商是否存在
     * @param dealerId 经销商ID
     * @return List<PO>
     */
    public List<PO> queryDealerById(Long dealerId){
    	
    	TmDealerPO dealerPO = new TmDealerPO();
    	dealerPO.setDealerId(dealerId);
    	
    	return this.select(dealerPO);
    }
    
    /**
     * 查询根据工单ID看看这个工单是否有特殊费用
     * @param dealerId 工单ID
     * @return List<PO>
     */
    public int queryDealerById1(String id){
    	String sql="SELECT COUNT(*) as count FROM tt_as_wr_spefee_claim WHERE CLAIM_ID='"+id+"'";
    	List<Map<String,Object>> list = this.pageQuery(sql, null, this.getFunName());
    	return ((BigDecimal)list.get(0).get("COUNT")).intValue();
    }
    
    /**
     * 查询索赔监控工时信息
     * @param wrGroupCode 车型组ID
     * @param claimId 索赔申请单ID
     * @param companyId 所属公司ID区分轿车和微车
     * @param isMain 查询工时类型 1 ：主要工时 0: 附加工时 其他：无
     * @return List<TtAsWrAuthmonitorlabPO>
     */
    public List<TtAsWrAuthmonitorlabPO> queryMonitorLabour(String wrGroupCode,
    		Long companyId,Long claimId,Integer isMain){
    	
    	List<TtAsWrAuthmonitorlabPO> resultList = null;
    	if(wrGroupCode==null || companyId==null || claimId==null)
    		return resultList;
    	
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT A.*\n" );
    	sql.append("  FROM TT_AS_WR_AUTHMONITORLAB A\n" );
    	sql.append(" WHERE 1 = 1\n" );
    	sql.append("   AND A.MODEL_GROUP = ?\n" );
    	sql.append("   AND A.IS_DEL = ?\n" );
    	sql.append("   AND EXISTS (SELECT B.ID\n" );
    	sql.append("          FROM TT_AS_WR_LABOURITEM B\n" );
    	sql.append("         WHERE 1 = 1\n" );
    	sql.append("           AND B.WR_LABOURCODE = A.LABOUR_OPERATION_NO\n" );
    	sql.append("           AND B.IS_MAINLABOUR = ?\n" );
    	sql.append("           AND B.ID = ?)");
    	
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(Long.parseLong(wrGroupCode));
    	paramList.add(Constant.IS_DEL_00);//未删除的监控工时
    	paramList.add(isMain);
    	paramList.add(claimId);
    	
    	resultList = this.select(TtAsWrAuthmonitorlabPO.class, sql.toString(), paramList);
    	return resultList;
    }
    
    /**
     * 查询索赔监控配件信息
     * @param companyId 所属公司ID区分轿车和微车
     * @param wrGroupCode 车型组信息
     * @param claimId 索赔申请单ID
     * @param isMain 是否是主损配件标识
     * @return List<TtAsWrAuthmonitorpartPO>
     */
    public List<TtAsWrAuthmonitorpartPO> queryMonitorPart(Long companyId,String wrGroupCode,
    		Long claimId,Integer isMain){

    	List<TtAsWrAuthmonitorpartPO> resultList = null;
    	if(wrGroupCode==null || companyId==null || claimId==null)
    		return resultList;

    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT A.*\n" );
    	sql.append("  FROM TT_AS_WR_AUTHMONITORPART A\n" );
    	sql.append(" WHERE 1 = 1\n" );
    	sql.append("   AND A.MODEL_GROUP = ?\n" );
    	sql.append("   AND A.IS_DEL = ?\n" );
    	sql.append("   AND EXISTS (SELECT B.ID\n" );
    	sql.append("          FROM TT_AS_WR_PARTSITEM B\n" );
    	sql.append("         WHERE 1 = 1\n" );
    	sql.append("           AND B.PART_CODE = A.PART_CODE\n" );
    	sql.append("           AND B.IS_MAINPART = ?\n" );
    	sql.append("           AND B.ID = ?)");
  	
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(Long.parseLong(wrGroupCode));
    	paramList.add(Constant.IS_DEL_00);//未删除的监控工时
    	paramList.add(isMain);
    	paramList.add(claimId);
    	
    	return this.select(TtAsWrAuthmonitorpartPO.class, sql.toString(), paramList);
    }
    
    /**
     * 查询索赔监控配件信息
     * @param companyId 所属公司ID区分轿车和微车
     * @param wrGroupCode 车型组信息
     * @param claimId 索赔申请单ID
     * @param isMain 是否是主损配件标识
     * @return List<TtAsWrAuthmonitorpartPO>
     */
    public List<TtAsWrAuthmonitorpartPO> queryMonitorPart(Long companyId,
    		Long claimId,Integer isMain){

    	List<TtAsWrAuthmonitorpartPO> resultList = null;
    	if(companyId==null || claimId==null)
    		return resultList;

    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT A.*\n" );
    	sql.append("  FROM TT_AS_WR_AUTHMONITORPART A\n" );
    	sql.append(" WHERE 1 = 1\n" );
    	sql.append("   AND A.IS_DEL = ?\n" );
    	sql.append("   AND EXISTS (SELECT B.ID\n" );
    	sql.append("          FROM TT_AS_WR_PARTSITEM B\n" );
    	sql.append("         WHERE 1 = 1\n" );
    	sql.append("           AND B.PART_CODE = A.PART_CODE\n" );
    	sql.append("           AND B.IS_MAINPART = ?\n" );
    	sql.append("           AND B.ID = ?)");
  	
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(Constant.IS_DEL_00);//未删除的监控工时
    	paramList.add(isMain);
    	paramList.add(claimId);
    	
    	return this.select(TtAsWrAuthmonitorpartPO.class, sql.toString(), paramList);
    }
    
    /**
     * Iverson update 2010-11-10
     * 查询索赔监控配件大类信息
     * @param companyId 所属公司ID区分轿车和微车
     * @param claimId 索赔申请单ID
     * @return List<TtAsWrAuthmonitortypePO>
     */
    public List<TmPtPartTypePO> queryMonitorPartBig(Long companyId,
    		Long claimId){
    	List<TmPtPartTypePO> resultList = null;
    	if(companyId==null || claimId==null){
    		return resultList;
    	}	
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT A.*\n" );
    	sql.append("  FROM tm_pt_part_type A\n" );
    	sql.append(" WHERE 1 = 1\n" );
    	sql.append("   AND A.Is_Max = 1 \n" );
    	sql.append("   AND EXISTS (SELECT B.ID\n" );
    	sql.append("          FROM TT_AS_WR_PARTSITEM B,Tm_Pt_Part_Base d\n" );
    	sql.append("         WHERE 1 = 1\n" );
    	sql.append("           AND a.id = d.part_type_id\n" );
    	sql.append("           AND d.PART_CODE = B.PART_CODE\n" );
    	sql.append("           AND B.ID = ?)");
    	List<Object> paramList = new ArrayList<Object>();
   
    	paramList.add(claimId);
    	return this.select(TmPtPartTypePO.class, sql.toString(), paramList);
    }
    
    /**
     * 根据物料组ID查询对应明细
     * @param groupId 物料组ID
     * @return TmVhclMaterialGroupPO
     */
    public TmVhclMaterialGroupPO queryMaterialGroupById(Long groupId){
    	
    	TmVhclMaterialGroupPO resultPO = null;
    	if(groupId == null)
    		return resultPO;
    	
    	TmVhclMaterialGroupPO conditingVO = new TmVhclMaterialGroupPO();
    	conditingVO.setGroupId(groupId);
    	
    	List<PO> resList = this.select(conditingVO);
    	
    	if(resList!=null && resList.size()>0){
    		resultPO = (TmVhclMaterialGroupPO) resList.get(0);
    	}
    	return resultPO;
    }
    
    /**
     * 加载 授权项
     * 授权项 为授权规则维护中 授权项信息
     * @param type 对应TC_CODE中的类别
     * @return List<PO> TcCodePO
     */
    public List<PO> loadAuthItem(String type){
    	
    	TcCodePO conditionPO = new TcCodePO();
    	conditionPO.setType(type);
    	
    	return this.select(conditionPO);
    }
    
    /**
     * 查询授权规则
     * @param wrGroupId 车型组ID
     * @param companyId 所属公司ID区分轿车和微车
     * @param type 0 技术授权 1 结算授权
     * @return List<PO>
     */
    public List<PO> queryAuthRule(String wrGroupId,Long companyId,String type){
    	
    	if(wrGroupId==null)
    		return null;
    	
    	//查询规则信息
    	TtAsWrRulemappingPO conditionPO = new TtAsWrRulemappingPO();
    	conditionPO.setWarrantyGroup(Long.parseLong(wrGroupId));
    	conditionPO.setOemCompanyId(companyId);
    	conditionPO.setType(CommonUtils.checkNull(type));
    	
    	List<PO> ruleList = this.select(conditionPO);
    	return ruleList;
    }
    
    /**
     * 查询授权规则对应条件明细
     * @param wrGroupId 车型组ID
     * @param companyId 所属公司ID区分轿车和微车
     * @param type 0 技术授权 1 结算授权
     * @return List<TtAsWrRuleitemPO>
     */
    public List<TtAsWrRuleitemPO> queryAuthRuleItem(String wrGroupId,Long companyId,String type){
    	
    	if(wrGroupId==null)
    		return null;
    	
    	//查询规则对应条件信息
    	String sql = "SELECT B.* \n"+
						" FROM TT_AS_WR_RULEMAPPING A,TT_AS_WR_RULEITEM B\n"+
						" WHERE 1=1\n"+
						" AND A.RULE_ELEMENT = B.RULE_NO\n"+
						" AND A.WARRANTY_GROUP = ?\n" +
						" AND A.TYPE = ?\n" +
						" ORDER BY B.RULE_NO,B.ELEMENT_POSITION";
    	
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(wrGroupId);
    	paramList.add(CommonUtils.checkNull(type));
    	List<TtAsWrRuleitemPO> ruleItemList = this.select(TtAsWrRuleitemPO.class, sql, paramList);
    	
    	return ruleItemList;
    }
    
    /**
     * 查询授权规则明细（将对应规则的条件明细封装到授权规则对象中）
     * @param wrGroupId 车型组ID
     * @param companyId 所属公司ID区分轿车和微车
     * @param type 0 技术授权 1 结算授权
     * @return Map<String,RuleVO>
     */
    public Map<String,RuleVO> loadAuthRuleDetail(String wrGroupId,Long companyId,String type){
    	
    	Map<String,RuleVO> resultMap = new HashMap<String, RuleVO>();//存在规则
    	
    	//查询授权规则信息
    	List<PO> ruleList = this.queryAuthRule(wrGroupId,companyId,type);
    	
    	if(ruleList==null || ruleList.size()<=0)
    		return resultMap;
    	//查询授权规则对应明细信息
    	List<TtAsWrRuleitemPO> ruleItemList = this.queryAuthRuleItem(wrGroupId,companyId,type);
    	
    	if(ruleItemList==null || ruleItemList.size()<=0)
    		return resultMap;
    	
    	Map<String,Map<Integer,TtAsWrRuleitemPO>> tempMap = new HashMap<String, Map<Integer,TtAsWrRuleitemPO>>();
    	//将规则对应的条件明细封装到对应规则下
    	for (TtAsWrRuleitemPO ttAsWrRuleitemPO : ruleItemList) {
			String roNO = ttAsWrRuleitemPO.getRuleNo().toString();//规则号
			Map<Integer,TtAsWrRuleitemPO> temp = null;//存在规则明细
			if(tempMap.containsKey(roNO)){
				temp = tempMap.get(roNO);
			}else{
				temp = new TreeMap<Integer, TtAsWrRuleitemPO>();//要求带排序
				tempMap.put(roNO, temp);
			}
			temp.put(ttAsWrRuleitemPO.getElementPosition(), ttAsWrRuleitemPO);
		}
    	
    	//将规则同条件明细封装到一起
    	for (PO po : ruleList) {
			TtAsWrRulemappingPO rulePO = (TtAsWrRulemappingPO)po;
    		String roNO = rulePO.getRuleElement().toString();
			RuleVO ruleVO = new RuleVO();
			ruleVO.setRuleVO(rulePO);
			if(tempMap.containsKey(roNO))
				ruleVO.setConditiongMap(tempMap.get(roNO));
			
			resultMap.put(rulePO.getRuleElement().toString(), ruleVO);
    	}
    	
    	return resultMap;
    }

    /**
     * 查询有效的并且启用的索赔申请单状态
     * 注意：只查询IS_USE状态为有效的规则
     * @param status 是否有效
     * @param autoType 规则类型（退回/拒绝）
     * @param companyId 所属公司ID区分轿车和微车
     * @return
     */
    public List<PO> queryFixAuthRule(String status,String autoType,Long companyId){
    	TtAsWrClaimAutoPO conditionPO = new TtAsWrClaimAutoPO();
    	conditionPO.setStatus(Integer.parseInt(status));
    	conditionPO.setAutoType(Integer.parseInt(autoType));
    	conditionPO.setOemCompanyId(companyId);
    	conditionPO.setIsUse(Constant.STATUS_ENABLE);
    	return this.select(conditionPO);
    }
    
    /**
     * 更新索赔申请单状态
     * @param status 索赔申请单状态
     * @param authType 审核类型（自动/人工）
     * @param updateId 更新记录人员
     * @param claimId 索赔申请单ID
     * @param firstRole 第一个需要审核的角色
     * @param advice 审批建议
     * @return
     */
    public int updateClaimOrderStatus(String status,char authType,
    		String updateId,String claimId,String firstRole,String advice){
    	
    	int res = 0;
    	TtAsWrApplicationPO conditionPO = new TtAsWrApplicationPO();
    	conditionPO.setId(Long.parseLong(claimId));
    	TtAsWrApplicationPO parameterPO = new TtAsWrApplicationPO();
    	parameterPO.setStatus(Integer.parseInt(status));
    	parameterPO.setUpdateBy(Long.parseLong(updateId));
    	parameterPO.setUpdateDate(new Date());
    	parameterPO.setAuthCode(firstRole);
    	parameterPO.setOemOption(CommonUtils.checkNull(advice));
    	parameterPO.setAuditingDate(new Date());
    	parameterPO.setAuditingMan(-1l);
    	parameterPO.setLastStatus(Integer.parseInt(status));
    	
    	res = this.update(conditionPO, parameterPO);
    	return res;
    }
    
    /**
     * 记录索赔申请单的授权状态
     * 注：对应审核信息按指定规则格式化
     * @param appAuthPO
     * @return Long ID
     */
    public void insertClaimAppAuth(TtAsWrAppauthitemPO appAuthPO){
    	//格式化备注信息
    	this.formatAuthRemark(appAuthPO);
    	//将审核记录中的授权角色，修改成角色名称
    	this.changeToRoleName(appAuthPO);
    	//索赔授权信息，无主键
    	this.insert(appAuthPO);
    }
    
    /**
     * 将审核记录中的授权角色，修改成角色名称
     * @param appAuthPO
     */
    public void changeToRoleName(TtAsWrAppauthitemPO appAuthPO){
    	
    	if(Utility.testString(appAuthPO.getApprovalLevelCode())){
    		String role = appAuthPO.getApprovalLevelCode();
    		String roles[] = role.split(",");
    		TtAsWrAuthinfoPO conditionPO = new TtAsWrAuthinfoPO();
    		List<TtAsWrAuthinfoPO> authList = this.select(conditionPO);
    		StringBuilder roleSb = new StringBuilder();
    		if(roles!=null && roles.length>0 && authList!=null){
    			
    			Map<String,String> roleMap = new HashMap<String, String>();
    			for(TtAsWrAuthinfoPO authPO:authList){
    				roleMap.put(authPO.getApprovalLevelCode(), authPO.getApprovalLevelName());
    			}
    			
	    		for (int i = 0; i < roles.length; i++) {
					String temp = roles[i];
					if(Utility.testString(temp)){
						if(roleMap.containsKey(temp)){
							roleSb.append(roleMap.get(temp)).append(",");
						}else{
							roleSb.append(temp).append(",");
						}
					}
				}
	    		
	    		String roleName = roleSb.toString();
	    		if(roleName.endsWith(",")){
	    			roleName = roleName.substring(0,roleName.length()-1);
	    		}
	    		if(roleName.length()>100){
	    			roleName = roleName.substring(0,95)+"...";
	    		}
	    		appAuthPO.setApprovalLevelCode(roleName);
    		}
    	}else{
    		appAuthPO.setApprovalLevelCode("--");
    	}
    }
    
    /**
     * 格式化审核备注信息
     * @param appAuthPO
     */
    private void formatAuthRemark(TtAsWrAppauthitemPO appAuthPO){
    	String remark = "";
    	if(appAuthPO!=null && appAuthPO.getRemark()!=null && appAuthPO.getRemark().length()>0){
    		remark = appAuthPO.getRemark();
    		int strlength = remark.length();
    		String lastChar = remark.substring(strlength-1,strlength);
    		if("\n".equals(lastChar))
    			remark = remark.substring(0,strlength-1);
    		remark = remark.replace("\n", "<br/>");
    		appAuthPO.setRemark(remark);
    	}else{
    		appAuthPO.setRemark(remark);
    	}
    }
    
    /**
     * 记录索赔申请单需要人工审核的原因和需要经过的审核级别
     * 注：每次记录前需要先删除对应申请单的对应记录，保证索赔单
     *     在被退回后仍可以记录对应数据（该表使用索赔申请单ID唯一）
     * @param authPO
     */
    public void insertClaimAuth(TtAsWrWrauthorizationPO authPO){
    	//删除之前记录
    	TtAsWrWrauthorizationPO delPO = new TtAsWrWrauthorizationPO();
    	delPO.setId(authPO.getId());
    	this.delete(delPO);
        //记录本次记录
    	this.insert(authPO);
    }

    /**
     * 查询一条超过指定月数没有回运的索赔清单
     * @param dealerId 经销商ID
     * @param claimStatus 索赔单状态
     * @param months 最大允许不回运月数
     * @param companyId 所属公司ID区分轿车和微车
     * @return List<Map<String,Object>>
     */
    public List<Map<String,Object>> queryNoReturnClaim(Long dealerId,Integer claimStatus,
    		Integer months,Long modelId,Long companyId){
    	
    	if(modelId==null || months ==null)
    		return null;
    	
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT A.ID \n" );
    	sql.append("  FROM TT_AS_WR_APPLICATION A \n" );
    	sql.append(" WHERE 1 = 1 \n" );
    	sql.append("   AND EXISTS (SELECT B.ID \n" );
    	sql.append("          FROM TT_AS_WR_PARTSITEM B, TM_PT_PART_BASE C \n" );
    	sql.append("         WHERE 1 = 1 \n" );
    	sql.append("           AND B.ID = A.ID \n" );
    	sql.append("           AND B.PART_CODE = C.PART_CODE \n" );
    	//sql.append("           AND C.GROUP_ID = ?\n" );//XZM 2010-08-04 长安现在不区分车型
    	sql.append("           AND C.IS_RETURN = ?\n" );
    	sql.append("           AND B.QUANTITY > B.RETURN_NUM)\n" );
    	sql.append("   AND A.REPORT_DATE < ADD_MONTHS(SYSDATE, ?)\n" );
    	sql.append("   AND A.DEALER_ID = ?\n" );
    	sql.append("   AND A.STATUS = ?\n" );
    	sql.append("   AND ROWNUM < 2");

    	List<Object> paramList = new ArrayList<Object>();
    	//paramList.add(modelId);
    	paramList.add(Constant.IS_NEED_RETURN);//需要回运的配件
    	paramList.add(months);
    	paramList.add(dealerId);
    	paramList.add(claimStatus);
    	
    	List<Map<String,Object>> resList = this.pageQuery(sql.toString(), paramList, this.getFunName());
    	return resList;
    }
    
    /**
     * 查询授权项对应比较符信息
     * @param typeList 比较符类别
     * @return Map<String,String> 
     *         KEY:TC_CODE.CODE_ID
     *         VALUE:TC_CODE.CODE_DESC
     */
    public Map<String,String> loadCompareOperator(List<Integer> typeList){
    	
    	Map<String,String> resMap = new HashMap<String, String>();
    	if(typeList==null || typeList.size()<=0)
    		return resMap;
    	
    	for (Integer type : typeList) {
			List<PO> coList = this.loadAuthItem(type.toString());
			if(coList!=null && coList.size()>0){
				for (PO po : coList) {
					TcCodePO codePO = (TcCodePO) po;
					resMap.put(codePO.getCodeId(), codePO.getCodeDesc());
				}
			}
		}
    	
    	return resMap;
    }
    
    /**
     * 查询保养费用,如果实销日期、省份信息和车型信息存在空的，则返回空
     * 注：只取满足条件最新三包策略对应保养费用
     * @param purchasedDate 实销日期
     * @param provinceId 省份信息
     * @param modelId 车型信息
     * @param companyId 所属公司ID区分轿车和微车
     * @return List<TtAsWrGamefeePO>
     */
    public List<TtAsWrGamefeePO> queryGuaranteeAmount(Date purchasedDate,Long provinceId,
    		Long modelId,Long companyId){
    	
    	List<TtAsWrGamefeePO> resultList = null;
    	
    	if(purchasedDate==null || provinceId==null || modelId==null)
    		return resultList;
    	
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT E.*\n" );
    	sql.append("  FROM TT_AS_WR_GAMEFEE E,TT_AS_WR_GAME F\n" );
    	sql.append(" WHERE E.GAME_ID = F.ID\n" );
    	sql.append(" AND E.MAINTAINFEE_ORDER <= F.MAINTAIN_NUM\n" );
    	sql.append(" AND E.GAME_ID IN\n" );
    	sql.append("       (SELECT MAX(A.ID)\n" );
    	sql.append("         FROM TT_AS_WR_GAME A\n" );
    	sql.append("        WHERE 1 = 1\n" );
    	sql.append("          AND A.START_DATE <= ?\n" );
    	sql.append("          AND A.END_DATE >= ?\n" );
    	sql.append("          AND A.COMPANY_ID = ?\n" );
    	sql.append("          AND EXISTS (SELECT C.ID\n" );
    	sql.append("                 FROM TT_AS_WR_GAMEMODEL C\n" );
    	sql.append("                WHERE C.GAME_ID = A.ID\n" );
    	sql.append("                  AND C.MODEL_ID = ?)\n" );
    	sql.append("          AND EXISTS (SELECT D.ID\n" );
    	sql.append("                 FROM TT_AS_WR_GAMEPRO D\n" );
    	sql.append("                WHERE D.GAME_ID = A.ID\n" );
    	sql.append("                  AND D.PROVINCE_ID = ?))");
    	sql.append(" AND F.COMPANY_ID = ?");

    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(purchasedDate);
    	paramList.add(purchasedDate);
    	paramList.add(companyId);
    	paramList.add(modelId);
    	paramList.add(provinceId);
    	paramList.add(companyId);
    	
    	resultList = this.select(TtAsWrGamefeePO.class,sql.toString(),paramList);
    	
    	return resultList;
    }
    
    /**
     * 根据三包策略ID查询保养费用,如果三包策略ID为空则返回空
     * @param gameId 三包策略ID
     * @return List<TtAsWrGamefeePO>
     */
    public List<TtAsWrGamefeePO> queryGuaranteeAmount(Long gameId){
    	
    	List<TtAsWrGamefeePO> resultList = null;
    	
    	if(gameId==null)
    		return resultList;
    	
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT *\n" );
    	sql.append("  FROM TT_AS_WR_GAMEFEE E\n" );
    	sql.append(" WHERE GAME_ID =?\n" );

    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(gameId);
    	
    	resultList = this.select(TtAsWrGamefeePO.class,sql.toString(),paramList);
    	
    	return resultList;
    }
	public List<Map<String, Object>> viewFreeModel(String vin ){
		  StringBuilder sql= new StringBuilder();
		  sql.append("select mg.free FROM TT_AS_WR_MODEL_ITEM mi, tm_vehicle v ,tt_as_wr_model_group mg\n");
		  sql.append("where v.package_id = mi.model_id\n" );
		  sql.append("and mg.wrgroup_id = mi.wrgroup_id\n" );
		  sql.append("and v.vin='"+vin+"'");
		  sql.append("AND MG.WRGROUP_TYPE="+Constant.WR_MODEL_GROUP_TYPE_01+"");
		  System.out.println("----------------------"+sql.toString());
		  return this.pageQuery(sql.toString(), null, this.getFunName());
	}
    
    
    /**
     * 查询保养次数,如果实销日期、省份信息和车型信息存在空的，则返回空
     * 注：只取满足条件最新三包策略对应保养费用
     * @param purchasedDate 实销日期
     * @param provinceId 省份信息
     * @param modelId 车型信息
     * @param companyId 所属公司ID区分轿车和微车
     * @return
     */
    public List<TtAsWrGamePO> queryGuaranteeCount(Date purchasedDate,Long provinceId,
    		Long modelId,Long companyId){
    	
    	List<TtAsWrGamePO> resultList = null;
    	
    	if(purchasedDate==null || provinceId==null || modelId==null)
    		return resultList;
    	
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT *\n" );
    	sql.append("  FROM TT_AS_WR_GAME E\n" );
    	sql.append(" WHERE ID IN\n" );
    	sql.append("       (SELECT MAX(A.ID)\n" );
    	sql.append("         FROM TT_AS_WR_GAME A\n" );
    	sql.append("        WHERE 1 = 1\n" );
    	sql.append("          AND A.START_DATE <= ?\n" );
    	sql.append("          AND A.END_DATE >= ?\n" );
    	sql.append("          AND A.COMPANY_ID = ?\n" );
    	sql.append("          AND A.GAME_TYPE = ?\n" );
    	sql.append("          AND EXISTS (SELECT C.ID\n" );
    	sql.append("                 FROM TT_AS_WR_GAMEMODEL C\n" );
    	sql.append("                WHERE C.GAME_ID = A.ID\n" );
    	sql.append("                  AND C.MODEL_ID = ?)\n" );
    	sql.append("          AND EXISTS (SELECT D.ID\n" );
    	sql.append("                 FROM TT_AS_WR_GAMEPRO D\n" );
    	sql.append("                WHERE D.GAME_ID = A.ID\n" );
    	sql.append("                  AND D.PROVINCE_ID = ?))");
    	sql.append(" AND E.COMPANY_ID=?");

    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(purchasedDate);
    	paramList.add(purchasedDate);
    	paramList.add(companyId);
    	paramList.add(Constant.GAME_TYPE_02);//查询当前策略
    	paramList.add(modelId);
    	paramList.add(provinceId);
    	paramList.add(companyId);
    	
    	resultList = this.select(TtAsWrGamePO.class,sql.toString(),paramList);
    	
    	return resultList;
    }
    
    /**
     * 查询保养次数,如果实销日期、省份信息和车型信息存在空的，则返回空
     * 注：只取满足条件最新三包策略对应保养费用
     * @param purchasedDate 实销日期
     * @param provinceId 省份信息(CODE)
     * @param modelId 车型信息
     * @param companyId 所属公司ID区分轿车和微车
     * @param yieldly 产地CODE
     * @return
     */
    public List<TtAsWrGamePO> queryGuaranteeCount(Date purchasedDate,Long provinceId,
    		Long modelId,Long companyId,Long yieldly){
    	
    	List<TtAsWrGamePO> resultList = null;
    	
    	if(purchasedDate==null || companyId==null)
    		return resultList;
    	if(modelId==null)
    		modelId = -1L;
    	if(yieldly==null)
    		yieldly = -1L;
    	if(provinceId==null)
    		provinceId = -1L;
    	
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT *\n" );
    	sql.append("FROM TT_AS_WR_GAME\n" );
    	sql.append("WHERE ID IN (\n" );
    	sql.append("SELECT MAX(A.ID)\n" );
    	sql.append("  FROM TT_AS_WR_GAME A\n" );
    	sql.append(" WHERE 1 = 1\n" );
    	sql.append("   AND (EXISTS (SELECT B.GAME_ID\n" );
    	sql.append("                  FROM TT_AS_WR_GAMEMODEL B\n" );
    	sql.append("                 WHERE 1 = 1\n" );
    	sql.append("                   AND B.GAME_ID = A.ID\n" );
    	sql.append("                   AND B.MODEL_ID = ?) OR NOT EXISTS\n" );
    	sql.append("        (SELECT B.ID FROM TT_AS_WR_GAMEMODEL B WHERE B.GAME_ID = A.ID))\n" );
    	sql.append("   AND (EXISTS (SELECT C.GAME_ID\n" );
    	sql.append("                  FROM TT_AS_WR_GAMEPRO C\n" );
    	sql.append("                 WHERE 1 = 1\n" );
    	sql.append("                   AND C.GAME_ID = A.ID\n" );
    	sql.append("                   AND C.PROVINCE_ID = ?) OR NOT EXISTS\n" );
    	sql.append("        (SELECT C.GAME_ID FROM TT_AS_WR_GAMEPRO C WHERE C.GAME_ID = A.ID))\n" );
    	sql.append("   AND (EXISTS (SELECT D.GAME_ID\n" );
    	sql.append("                  FROM TT_AS_WR_GAMEYIELDLY D\n" );
    	sql.append("                 WHERE 1 = 1\n" );
    	sql.append("                   AND D.GAME_ID = A.ID\n" );
    	sql.append("                   AND D.YIELDLY_ID = ?) OR NOT EXISTS\n" );
    	sql.append("        (SELECT D.GAME_ID FROM TT_AS_WR_GAMEYIELDLY D WHERE D.GAME_ID = A.ID))\n" );
    	sql.append("   AND A.START_DATE <= ?\n" );
    	sql.append("   AND A.END_DATE >= ?\n" );
    	sql.append("   AND A.COMPANY_ID = ?\n" );
    	sql.append("   AND A.GAME_TYPE = ?)");

    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(modelId);
    	paramList.add(provinceId);
    	paramList.add(yieldly);
    	paramList.add(purchasedDate);
    	paramList.add(purchasedDate);
    	paramList.add(companyId);
    	paramList.add(Constant.GAME_TYPE_02);//查询当前策略
    	
    	resultList = this.select(TtAsWrGamePO.class,sql.toString(),paramList);
    	
    	return resultList;
    }
    
    /**
     * 查询对应车辆对应次数保养索赔记录（免费保养）
     * @param vin 车辆标识
     * @param timers 保养次数
     * @param companyId 所属公司ID区分轿车和微车
     * @param expClaimStatus 排除所检测传入状态的索赔单，多个以","分隔;null 没有需要排除的状态
     * @return List<TtAsWrApplicationPO>
     */
    public List<TtAsWrApplicationPO> queryGuaranteeRecord(String vin,Integer timers,
    		Long companyId,String expClaimStatus){
    	
    	List<TtAsWrApplicationPO> resultList = null;
    	if(vin==null || timers==null) 
    		return resultList;
    	
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT A.*\n" );
    	sql.append("  FROM TT_AS_WR_APPLICATION A\n" );
    	sql.append(" WHERE 1 = 1\n" );
    	sql.append("   AND A.VIN = ?\n" );
    	sql.append("   AND A.FREE_M_AMOUNT = ?\n" );
    	sql.append("   AND A.CLAIM_TYPE = ?\n" );

    	TtAsWrApplicationPO conditionPO = new TtAsWrApplicationPO();
    	conditionPO.setVin(vin);
    	conditionPO.setFreeMAmount(timers);
    	conditionPO.setClaimType(Constant.CLA_TYPE_02);
    	
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(vin);
    	paramList.add(timers);
    	paramList.add(companyId);
    	paramList.add(Constant.CLA_TYPE_02);//索赔单状态为免费保养
    	
    	if(expClaimStatus!=null){
    		sql.append("   AND A.STATUS NOT IN (" + expClaimStatus + ")");
    	}

    	resultList = this.select(TtAsWrApplicationPO.class,sql.toString(),paramList);
    	return resultList;
    }
    
    /**
     * 查询物料详细信息（品牌、车系、车型、配置）
     * @param packageId 配置ID
     * @return Map<String,Object>
     *        信息字段：(品牌)BRAND_ID、BRAND_CODE、BRAND_NAME、
     *                  (车系)SERIES_ID、SERIES_CODE、SERIES_NAME、
     *                  (车型)MODEL_ID、MODEL_CODE、MODEL_NAME、
     *                  (配置)PACKAGE_ID、PACKAGE_CODE、PACKAGE_NAME
     */
    public Map<String,Object> queryVechileDetail(Long packageId){
    	
    	Map<String,Object> resMap = null;
    	
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT *\n" );
    	sql.append("  FROM VW_MATERIAL_GROUP\n" );
    	sql.append(" WHERE 1 = 1\n" );
    	sql.append("   AND PACKAGE_ID = ?");

    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(packageId);
    	
    	List<Map<String,Object>> resList = this.pageQuery(sql.toString(), paramList, this.getFunName());
    	if(resList!=null && resList.size()>0){
    		resMap = resList.get(0);
    	}
    	
    	return resMap;
    }
    
    /**
     * 查询服务活动评估信息
     * @param activityNo 服务活动标号
     * @param companyId 所属公司ID区分轿车和微车
     * @param dealerId 经销商ID
     * @param activityStatus 服务活动状态
     * @return List<TtAsActivityEvaluatePO>
     */
    public List<TtAsActivityEvaluatePO> queryActivityEvaluate(String activityNo,Long companyId,
    		Long dealerId,Integer activityStatus){
    	
    	if(activityNo==null || "".equals(activityNo))
    		return null;
    	
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT B.*\n" );
    	sql.append("  FROM TT_AS_ACTIVITY A, TT_AS_ACTIVITY_EVALUATE B\n" );
    	sql.append(" WHERE 1 = 1\n" );
    	sql.append("   AND A.ACTIVITY_ID = B.ACTIVITY_ID\n" );
    	sql.append("   AND A.COMPANY_ID = ?\n" );
    	sql.append("   AND B.DEALER_ID = ?\n" );
    	sql.append("   AND A.IS_DEL = ?\n" );
    	sql.append("   AND A.STATUS = ?\n");
    	sql.append("   AND A.ACTIVITY_CODE = ?");
    	
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(companyId);
    	paramList.add(dealerId);
    	paramList.add(Constant.IS_DEL_00);//未删除的服务活动
    	paramList.add(activityStatus);
    	paramList.add(activityNo);

    	return this.select(TtAsActivityEvaluatePO.class, sql.toString(), paramList);
    }
    
    /**
     * 查询某辆车的主要配件维修记录
     * @param vin 车辆唯一标识码
     * @param isMain 是否检测主损配件 1主配件：0附加配件
     * @param companyId 所属公司ID区分轿车和微车
     * @param exptClaimStatus 排除的索赔单状态
     * @param claimId 索赔申请单ID
     * @return List<Map<String,Object>>
     *         信息数据：PART_CODE[配件代码]、REPAIRCOUNT[索赔次数]
     */
    public List<Map<String,Object>> checkRepeatClaimPart(String vin,Integer isMain,
    		Long companyId,String exptClaimStatus,Long claimId){
    	
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT A.PART_CODE, COUNT(*) REPAIRCOUNT\n" );
    	sql.append("  FROM TT_AS_WR_PARTSITEM A, TT_AS_WR_APPLICATION B\n" );
    	sql.append(" WHERE 1 = 1\n" );
    	sql.append("   AND A.ID = B.ID\n" );
    	sql.append("   AND B.VIN = ?\n" );
    	sql.append("   AND A.IS_MAINPART = ?\n" );
    	sql.append("   AND EXISTS (SELECT A1.PART_CODE\n" );
    	sql.append("          FROM TT_AS_WR_PARTSITEM A1\n" );
    	sql.append("         WHERE 1 = 1\n" );
    	sql.append("           AND A1.ID = ?\n" );
    	sql.append("           AND A1.IS_MAINPART = ?\n" );
    	sql.append("           AND A1.PART_CODE = A.PART_CODE)\n" );
    	sql.append(" GROUP BY A.PART_CODE\n");
    	
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(CommonUtils.checkNull(vin));
    	paramList.add(isMain==null?-1:isMain);
    	paramList.add(claimId);
    	paramList.add(isMain==null?-1:isMain);
    	
    	List<Map<String,Object>> resList = this.pageQuery(sql.toString(), paramList, this.getFunName());
    	
    	return resList;
    }
    
    /**
     * 统计并回写同一辆车同一工时维修次数
     * @param vin 车辆底盘号
     * @param claimId 索赔单ID
     * @param expertStatus 需要统计的索赔单状态
     * @param companyId 所属公司ID区分轿车和微车
     */
    public void statisLabourCount(String vin,Long claimId,String expertStatus,Long companyId){
    	
    	StringBuilder sql= new StringBuilder();
    	sql.append("UPDATE TT_AS_WR_LABOURITEM C\n" );
    	sql.append("   SET LABOUR_COUNT = NVL((SELECT COUNT(*)\n" );
    	sql.append("                         FROM TT_AS_WR_APPLICATION A, TT_AS_WR_LABOURITEM B\n" );
    	sql.append("                        WHERE 1 = 1\n" );
    	sql.append("                          AND A.ID = B.ID\n" );
    	sql.append("                         and (b.is_agree is null or b.is_agree=10041001)\n" );
    	 
    	sql.append("                          AND A.VIN = ?\n" );
    	sql.append("                          AND A.STATUS IN (").append(CommonUtils.checkNull(expertStatus)).append(")\n" );
    	sql.append("                          AND B.WR_LABOURCODE = C.WR_LABOURCODE)+1,0)\n" );
    	sql.append(" WHERE 1 = 1\n" );
    	sql.append("   AND ID = ?");
    	
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(CommonUtils.checkNull(vin));
    	paramList.add(claimId);

    	this.update(sql.toString(), paramList);
    }
    
    /**
     * 查询系统策略
     * @param companyId
     * @return
     */
    public List<TtAsWrGamePO> querySystemStrategy(Long companyId){
    	
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT ID,RULE_ID,GAME_CODE,GAME_NAME,MAINTAIN_NUM,START_DATE,\n" );
    	sql.append("END_DATE,GAME_STATUS,UPDATE_BY,UPDATE_DATE,CREATE_BY,CREATE_DATE,\n" );
    	sql.append("COMPANY_ID,GAME_TYPE\n" );
    	sql.append("FROM TT_AS_WR_GAME\n" );
    	sql.append("WHERE ID IN ( SELECT MAX(A.ID)\n" );
    	sql.append("FROM TT_AS_WR_GAME A\n" );
    	sql.append("WHERE 1=1\n" );
    	sql.append("AND A.GAME_TYPE = ?\n" );
    	sql.append("AND A.COMPANY_ID = ?)");
    	
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(Constant.GAME_TYPE_01);//基础策略
    	paramList.add(companyId);


    	return this.select(TtAsWrGamePO.class,sql.toString(), paramList);
    }
    
    /**
     * 查询对应经销商工时费用
     * @param dealerId 经销商ID
     * @param companyId 公司ID
     * @param wrGroupCode 车型组代码
     * @return List<TmDownParameterPO>
     */
    public List<TmDownParameterPO> getLabourPrice(Long dealerId,Long companyId,String wrGroupCode){
    	
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT A.*\n" );
    	sql.append("FROM TM_DOWN_PARAMETER A,TM_DOWN_PARAMETER B\n" );
    	sql.append("WHERE 1=1\n" );
    	sql.append("AND A.AREA_CODE = B.AREA_CODE\n" );
    	sql.append("AND A.PARAMETER_CODE = "+Constant.CLAIM_BASIC_PARAMETER_01+"\n" );
    	sql.append("AND B.PARAMETER_CODE = "+Constant.CLAIM_BASIC_PARAMETER_10+"\n" );
    	sql.append("AND B.PARAMETER_VALUE = ?\n" );
    	sql.append("AND A.DEALER_ID = ?");
    	
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(wrGroupCode);
    	paramList.add(dealerId);

    	return this.select(TmDownParameterPO.class, sql.toString(), paramList);
    }
    
    /**
     * 检测DQV经销商申请的索赔是否包含监控工时
     * @param claimId
     * @return
     */
    public List<Map<String,Object>> existsDqvMonitorLabour(String claimId){
    	
    	if(!Utility.testString(claimId))
    		return null;
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT A.ID\n" );
    	sql.append("  FROM TT_AS_WR_LABOURITEM A\n" );
    	sql.append(" WHERE 1 = 1\n" );
    	sql.append("   AND EXISTS (SELECT B.WR_LABOURCODE\n" );
    	sql.append("          FROM TT_AS_WR_DQV_MONITOR_LABOUR B\n" );
    	sql.append("         WHERE B.WR_LABOURCODE = A.WR_LABOURCODE)\n" );
    	sql.append("   AND A.ID = ?");

    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(claimId);
    	
    	return this.pageQuery(sql.toString(), paramList, this.getFunName());
    }
    
    /**
     * 检测对应车辆是否参加过多次服务活动
     * @param vin 
     * @param activityNO 服务活动编号
     * @param expStatus 排除的索赔单状态 (以","分隔)
     * @return
     */
    public List<TtAsWrApplicationPO> checkModeActivity(String vin,String activityNO,String expStatus){
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT A.ID\n" );
    	sql.append("FROM TT_AS_WR_APPLICATION A\n" );
    	sql.append("WHERE 1=1\n" );
    	sql.append("AND A.STATUS NOT IN ("+expStatus+")\n" );
    	sql.append("AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_06+"\n" );
    	sql.append("AND A.CAMPAIGN_CODE = ?\n" );
    	sql.append("AND A.VIN = ?");
    	
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(activityNO);
    	paramList.add(vin);
    	
    	return this.select(TtAsWrApplicationPO.class,sql.toString(), paramList);
    }
    
    /**
     * 检测对应车辆是否参加过多次免费保养
     * @param vin 
     * @param expStatus 排除的索赔单状态 (以","分隔)
     * @return
     */
    public List<TtAsWrApplicationPO> checkModeMaintain(String vin,String expStatus){
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT A.ID\n" );
    	sql.append("FROM TT_AS_WR_APPLICATION A\n" );
    	sql.append("WHERE 1=1\n" );
    	sql.append("AND A.STATUS NOT IN ("+expStatus+")\n" );
    	sql.append("AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_02+"\n" );
    	sql.append("AND (A.IS_RETURN = "+Constant.IF_TYPE_NO+" or A.IS_RETURN IS NULL) ");
    	sql.append("AND A.VIN = ?");
    	
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(vin);
    	
    	return this.select(TtAsWrApplicationPO.class,sql.toString(), paramList);
    }
}
