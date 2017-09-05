/**
 * <p>Title: InfoFrame3.0.Cc.V01</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date: 2010-5-28</p>
 *
 * @author andy 
 * @mail   andy.ten@tom.com
 * @version 1.0
 * @remark 通用弹出窗口管理
 */
/**
 * 订单新增选择物料树界面
 * inputId   : 回填页面物料组code域id
 * inputName ：回填页面物料组name域id
 * isMulti   : true值多选，否则单选
 * groupLevel：输出的物料组等级
 * isAllArea：true值查全部，否则过滤业务范围
 */
function showMaterialGroupByAddOrder(inputCode ,inputName ,isMulti ,ids,groupLevel, warehouseId)
{
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	if(!groupLevel){ groupLevel = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showMaterialGroupByAddOrder.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&IDS="+ids+"&GROUPLEVEL="+groupLevel+"&ISALLAREA="+warehouseId,950,500);
}

/*
 * 
 */
function showOrgDealerForZotye(inputCode ,inputId ,isMulti ,orgId, isAllLevel, isAllArea,isDealerType,inputName)
{
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	if(!isAllLevel){ isAllLevel = null;}
	if(!isAllArea){ isAllArea = null;}
	if(!isDealerType){ isDealerType = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showOrgDealerForZotye.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId+"&ISALLLEVEL="+isAllLevel+"&ISALLAREA="+isAllArea+"&isDealerType="+isDealerType+"&inputName="+inputName,800,500);
}


/*
 * 销售订单编号
 * */
function showInVoiceInfo(invoiceNum,lineNum)
{
	if(!invoiceNum){ 
		alert("销售订单编号为空");
	}
	OpenHtmlWindow(g_webAppName+'/dialog/showInVoiceInfo.jsp?invoiceNum='+invoiceNum+"&lineNum="+lineNum,800,500);
}

/**
 * 通用选择经销商界面
 * inputCode : 回填页面经销商Code域id
 * inputId   : 回填页面经销商Id域id 
 * orgId     : 组织id，如有，则表示根据orgId过滤下级所有经销商，否则不过滤
 */
function showOrgDealer(inputCode ,inputId ,isMulti ,orgId, isAllLevel, isAllArea,isDealerType,inputName)
{
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	if(!isAllLevel){ isAllLevel = null;}
	if(!isAllArea){ isAllArea = null;}
	if(!isDealerType){ isDealerType = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showOrgDealer.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId+"&ISALLLEVEL="+isAllLevel+"&ISALLAREA="+isAllArea+"&isDealerType="+isDealerType+"&inputName="+inputName,800,500);
}
function showOrgDealerForSCSJ(inputCode ,inputId ,isMulti ,orgId, isAllLevel, isAllArea,isDealerType,inputName)
{	
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	if(!isAllLevel){ isAllLevel = null;}
	if(!isAllArea){ isAllArea = null;}
	if(!isDealerType){ isDealerType = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showOrgDealerForSCSJ.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId+"&ISALLLEVEL="+isAllLevel+"&ISALLAREA="+isAllArea+"&isDealerType="+isDealerType+"&inputName="+inputName,800,500);
}

/**
 * 该方法主要用于配件销售人员区域设置时使用
 * @param userId 销售人员id
 * @param inputCode
 * @param inputId
 * @param isMulti
 * @param orgId
 * @param isAllLevel
 * @param isAllArea
 * @param isDealerType
 * @param inputName
 */
function showPartDealer(userId,inputCode ,inputId ,isMulti ,orgId, isAllLevel, isAllArea,isDealerType,inputName,userType){
	if(!userId){ userId = null;}
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	if(!isAllLevel){ isAllLevel = null;}
	if(!isAllArea){ isAllArea = null;}
	if(!isDealerType){ isDealerType = null;}
	if(!inputName){ inputName = null;}
	if(!userType){ userType = null;}
	OpenHtmlWindow(g_webAppName+'/jsp/parts/baseManager/partsBaseManager/partSalesScope/partOrgDealerShow.jsp?USERID='+userId+'&INPUTCODE='+inputCode+'&INPUTID='+inputId+'&ISMULTI='+isMulti+"&ORGID="+orgId+'&ISALLLEVEL='+isAllLevel+'&ISALLAREA='+isAllArea+'&isDealerType='+isDealerType+'&inputName='+inputName+'&userType='+userType,730,390);
}

/***********zhumingwei 2012-06-28  下发索赔工时弹出经销商**************************/
function showOrgDealer01(inputCode ,inputId ,isMulti ,orgId, isAllLevel, isAllArea,isDealerType,inputName)
{
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	if(!isAllLevel){ isAllLevel = null;}
	if(!isAllArea){ isAllArea = null;}
	if(!isDealerType){ isDealerType = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showOrgDealer01.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId+"&ISALLLEVEL="+isAllLevel+"&ISALLAREA="+isAllArea+"&isDealerType="+isDealerType+"&inputName="+inputName,730,390);
}

/***********zhumingwei 2011-11-22**************************/
function showOrgDealerZW(inputCode ,inputId ,isMulti ,orgId, isAllLevel, isAllArea,isDealerType,inputName)
{
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	if(!isAllLevel){ isAllLevel = null;}
	if(!isAllArea){ isAllArea = null;}
	if(!isDealerType){ isDealerType = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showOrgDealerZW.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId+"&ISALLLEVEL="+isAllLevel+"&ISALLAREA="+isAllArea+"&isDealerType="+isDealerType+"&inputName="+inputName,730,390);
}

/***********zhumingwei 2011-11-22**************************/
function showOrgDealerZWcon(inputCode ,inputId ,isMulti ,orgId, isAllLevel, isAllArea,isDealerType,inputName)
{
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	if(!isAllLevel){ isAllLevel = null;}
	if(!isAllArea){ isAllArea = null;}
	if(!isDealerType){ isDealerType = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showOrgDealerZWcon.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId+"&ISALLLEVEL="+isAllLevel+"&ISALLAREA="+isAllArea+"&isDealerType="+isDealerType+"&inputName="+inputName,730,390);
}

/*****xiongchuan 2011-10-13********/
function showOrgDealerNew(inputCode ,inputId ,isMulti ,orgId, isAllLevel, isAllArea,isDealerType,inputName)
{
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	if(!isAllLevel){ isAllLevel = null;}
	if(!isAllArea){ isAllArea = null;}
	if(!isDealerType){ isDealerType = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showOrgDealerNew.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId+"&ISALLLEVEL="+isAllLevel+"&ISALLAREA="+isAllArea+"&isDealerType="+isDealerType+"&inputName="+inputName,730,390);
}

//zhumingwei 20110702
function showOrgDealer111(inputCode ,inputId ,isMulti ,orgId, isAllLevel, isAllArea,isDealerType,inputName)
{
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	if(!isAllLevel){ isAllLevel = null;}
	if(!isAllArea){ isAllArea = null;}
	if(!isDealerType){ isDealerType = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showOrgDealer111.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId+"&ISALLLEVEL="+isAllLevel+"&ISALLAREA="+isAllArea+"&isDealerType="+isDealerType+"&inputName="+inputName,730,390);
}

/**
 * 通用选择经销商界面
 * inputCode : 回填页面经销商Code域id
 * inputId   : 回填页面经销商Id域id 
 * regCode     :省份CODE，如有，则表示根据regCode过滤下级所有经销商，否则不过滤
 */
function showRegDealer(inputCode ,inputId ,isMulti ,orgId ,regCode, isAllLevel, isAllArea,isDealerType,inputName)
{
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	if(!regCode || regCode == 'false' || regCode == 'true'){ regCode = null;}
	if(!isAllLevel){ isAllLevel = null;}
	if(!isAllArea){ isAllArea = null;}
	if(!isDealerType){ isDealerType = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showRegDealer.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId+"&regCode="+regCode+"&ISALLLEVEL="+isAllLevel+"&ISALLAREA="+isAllArea+"&isDealerType="+isDealerType+"&inputName="+inputName,730,390);
}
function showOrgDealer2(inputCode ,inputId ,isMulti ,orgId, isAllLevel, isAllArea,isDealerType,inputName)
{
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	if(!isAllLevel){ isAllLevel = null;}
	if(!isAllArea){ isAllArea = null;}
	if(!isDealerType){ isDealerType = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showOrgDealer2.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId+"&ISALLLEVEL="+isAllLevel+"&ISALLAREA="+isAllArea+"&isDealerType="+isDealerType+"&inputName="+inputName,730,390);
}
/**
 * 当前登录经销商的下级经销商
 */
function showNowLevelDealer(inputCode ,inputId ,isMulti ,orgId, isAllLevel, isAllArea)
{
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	if(!isAllLevel){ isAllLevel = null;}
	if(!isAllArea){ isAllArea = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/ShowLevelDealers.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId+"&ISALLLEVEL="+isAllLevel+"&ISALLAREA="+isAllArea,730,390);
}



function showNewOrgDealer(inputCode ,inputId ,isMulti ,orgId,inputName)
{
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showNewOrgDealer.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId,730,390);
}

/**
 * 通用选择物料组树界面
 * inputId   : 回填页面物料组code域id
 * inputName ：回填页面物料组name域id
 * isMulti   : true值多选，否则单选
 * groupLevel：输出的物料组等级
 * isAllArea：true值查全部，否则过滤业务范围
 */
function showMaterialGroup(inputCode ,inputName ,isMulti ,groupLevel, isAllArea)
{
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	if(!groupLevel){ groupLevel = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/material/showMaterialGroup.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&GROUPLEVEL="+groupLevel+"&ISALLAREA="+isAllArea,850,480);
}

/**
 * 通用选择配置树界面
 * inputId   : 回填页面配置code域id
 * inputName ：回填页面配置name域id
 * isMulti   : true值多选，否则单选
 * groupLevel：输出的配置等级
 * isAllArea：true值查全部，否则过滤业务范围
 * 2012-05-09 hxy
 */

function showMaterialGroupByConf(inputCode ,inputName ,isMulti ,groupLevel, areaId)
{
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	if(!groupLevel){ groupLevel = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showMaterialGroupByConf.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&GROUPLEVEL="+groupLevel+"&AREAID="+areaId,770,410);
}

/**
 * 供报表使用的的物料组树界面 2012-02-07 HXY
 * inputId   : 回填页面物料组code域id
 * inputName ：回填页面物料组name域id
 * isMulti   : true值多选，否则单选
 * groupLevel：输出的物料组等级
 * isAllArea：true值查全部，否则过滤业务范围
 */
function showMaterialGroupByReport(inputCode ,inputName ,isMulti ,groupLevel, isAllArea)
{
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	if(!groupLevel){ groupLevel = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showMaterialGroupByReport.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&GROUPLEVEL="+groupLevel+"&ISALLAREA="+isAllArea,770,410);
}

//zhumingwei add 2011-6-22
function showMaterialGroup1(inputCode ,inputName ,isMulti ,groupLevel, isAllArea)
{
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	if(!groupLevel){ groupLevel = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showMaterialGroup21.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&GROUPLEVEL="+groupLevel+"&ISALLAREA="+isAllArea,770,410);
}

function showMaterialGroupN(inputCode ,inputName ,isMulti ,groupLevel, isAllArea)
{
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	if(!groupLevel){ groupLevel = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showMaterialGroupN.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&GROUPLEVEL="+groupLevel+"&ISALLAREA="+isAllArea,770,410);
}

/**
 * 通用选择物料组树界面
 * inputId   : 回填页面物料组code域id
 * inputName ：回填页面物料组name域id
 * isMulti   : true值多选，否则单选
 * groupLevel：输出的物料组等级
 * isAllArea：true值查全部，否则过滤业务范围
 */
function showMaterialGroupToModel(inputCode ,inputName ,isMulti ,groupLevel, isAllArea)
{
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	if(!groupLevel){ groupLevel = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showMaterialGroupToModel.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&GROUPLEVEL="+groupLevel+"&ISALLAREA="+isAllArea,770,410);
}


/**
 * 服务活动车型：选择物料组树界面
 * inputId   : 回填页面物料组code域id
 * inputName ：回填页面物料组name域id
 * isMulti   : true值多选，否则单选
 * groupLevel：输出的物料组等级
 */
function serviceShowMaterialGroup(inputCode ,inputName ,isMulti ,groupLevel)
{
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	if(!groupLevel){ groupLevel = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/serviceShowMaterialGroup.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&GROUPLEVEL="+groupLevel,770,410);
}

/**
 * 通用选择物料组树界面:过滤已有物料组
 * inputId   : 回填页面物料组code域id
 * inputName ：回填页面物料组name域id
 * isMulti   : true值多选，否则单选
 * groupLevel：输出的物料组等级
 */
function showMaterialGroup_Sel(inputCode ,inputName ,isMulti ,groupLevel)
{
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	if(!groupLevel){ groupLevel = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showMaterialGroup_Sel2.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&GROUPLEVEL="+groupLevel,770,410);
}

//zhumingwei 2011-10-24
function showMaterialGroup_Sel_con(inputCode ,inputName ,isMulti ,groupLevel)
{
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	if(!groupLevel){ groupLevel = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showMaterialGroup_Sel3.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&GROUPLEVEL="+groupLevel,770,410);
}
/**
 * 市场活动：物料组选择
 * */
function showMaterialGroup_market(inputCode ,inputName ,isMulti ,groupLevel)
{
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	if(!groupLevel){ groupLevel = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showMaterialGroup_market.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&GROUPLEVEL="+groupLevel,770,410);
}

/**
 * 市场活动：选择车型
 * */
function showMaterialCarType_market(inputCode ,inputName ,isMulti ,groupLevel)
{
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	if(!groupLevel){ groupLevel = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showCheXing.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&GROUPLEVEL="+groupLevel,770,410);
}
/**
 * 通用选择物料树界面
 * inputId   : 回填页面物料组code域id
 * inputName ：回填页面物料组name域id
 * isMulti   : true值多选，否则单选
 * isAllArea   : true值查全部，否则过滤业务范围
 */
function showMaterial(inputCode ,inputName ,isMulti ,isAllArea)
{
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showMaterial2.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&ISALLAREA="+isAllArea,850,480);
}

function showMaterialforGroup(inputCode ,inputName ,inputColorName,isMulti ,isAllArea)
{
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	if(!inputColorName){ inputColorName = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showMaterial3.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&ISALLAREA="+isAllArea+"&inputColorName="+inputColorName,850,480);
}
/**
 * 通用选择物料树界面
 * inputId   : 回填页面物料组code域id
 * inputName ：回填页面物料组name域id
 * isMulti   : true值多选，否则单选
 * areaId   : 业务范围id
 * ids   : 已选、需要过滤掉的物料id
 * matType :物料类型
 * typeHide ：是否隐藏物料类型下拉框（true：隐藏；false:显示）
 */
function showMaterialByAreaId(inputCode ,inputName ,isMulti , areaId, ids, productId,matType,typeHide){
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showMaterialByAreaId2.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&areaId="+areaId+"&ids="+ids+"&productId="+productId+"&matType="+matType+"&typeHide="+typeHide,800,450);
}

/**
 * 查询可生产的物料列表
 * 
 * @param inputCode
 * @param inputName
 * @param isMulti
 * @param areaId
 * @param ids
 * @param productId
 * @param matType
 * @param typeHide
 * @author wangsw
 * 
 */
function showMaterialByAreaIdForProduct(inputCode ,inputName ,isMulti , areaId, ids, productId,matType,typeHide){
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showMaterialByAreaIdForProduct.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&areaId="+areaId+"&ids="+ids+"&productId="+productId+"&matType="+matType+"&typeHide="+typeHide,800,450);
}


/**
 * 可生产(大客户)
 * @param inputCode
 * @param inputName
 * @param isMulti
 * @param areaId
 * @param ids
 * @param productId
 * @param matType
 * @param typeHide
 * @return
 */

function showMaterialByAreaIdCanProDKF(inputCode ,inputName ,isMulti , areaId, ids, productId,matType,typeHide){
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showMaterialByAreaId4.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&areaId="+areaId+"&ids="+ids+"&productId="+productId+"&matType="+matType+"&typeHide="+typeHide,800,450);
}
/**
 * 通用选择物料树界面 2012-05-16 HXY
 * inputId   : 回填页面物料组code域id
 * inputName ：回填页面物料组name域id
 * isMulti   : true值多选，否则单选
 * areaId   : 业务范围id
 * gids   : 物料组id
 */
function showMaterialByAreaIdAndMaterialGroupId(inputCode, inputName, isMulti, areaId, gids){
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showMaterialByAreaIdAndGroupId.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&areaId="+areaId+"&gids="+gids,730,390);
}

/**
 * 通用选择物料树界面
 * inputId   : 回填页面物料组code域id
 * inputName ：回填页面物料组name域id
 * isMulti   : true值多选，否则单选
 * areaId   : 业务范围id
 * ids   : 已选、需要过滤掉的物料id
 * orderType   : 订单类型
 */
function showMaterialByAreaIdAndOrderType(inputCode ,inputName ,isMulti , areaId, ids, orderType, productId){
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showMaterialByAreaIdAndOrderType2.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&areaId="+areaId+"&ids="+ids+"&orderType="+orderType+"&productId="+productId,730,390);
}

/**
 * 通用选择物料树界面--微车
 * inputId   : 回填页面物料组code域id
 * inputName ：回填页面物料组name域id
 * isMulti   : true值多选，否则单选
 * areaId   : 业务范围id
 * ids   : 已选、需要过滤掉的物料id
 */
function showMaterialByAreaId_Mini(inputCode ,inputName ,isMulti , areaId, ids, productId){
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showMaterialByAreaId_Mini2.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&areaId="+areaId+"&ids="+ids+"&productId="+productId,730,390);
}
/**
 * 通用选择供应商
 * inputId   : 回填页供应商code域id
 * inputName ：回填页供应商id域id
 * isMulti   : true值多选，否则单选
 */
function showSuppliar(inputCode ,inputId ,isMulti ){
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showSupplier.jsp?INPUTCODE="+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti,730,390);
}

/**
 * 选择配件
 * isMulti   : true值多选，否则单选
 */
function showPartInfo(inputCode,inputOldCode,inputName ,inputId ,isMulti){
	if(!inputName){ inputName = null;}
	if(!inputId){ inputId = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/partSelectSingle.jsp?INPUTCODE="+inputCode+"&INPUTOLDCODE="+inputOldCode+"&INPUTNAME="+inputName+"&INPUTID="+inputId+"&ISMULTI="+isMulti,730,390);
}

/**
 * 选择配件
 * isMulti   : true值多选，否则单选
 */
function showPartInfo2(inputCode,inputOldCode,inputName ,inputId , inputDefBuyPrice, isMulti){
	if(!inputName){ inputName = null;}
	if(!inputId){ inputId = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/partSelectSingle.jsp?INPUTCODE="+inputCode+"&INPUTOLDCODE="+inputOldCode+"&INPUTNAME="+inputName+"&INPUTID="+inputId+"&INPUTDEFBUYPRICE="+inputDefBuyPrice+"&ISMULTI="+isMulti,730,450);
}

/**
 * 选择供应商
 * inputId   : 回填页供应商code域id
 * inputName ：回填页供应商id域id
 * isMulti   : true值多选，否则单选
 */
function showPartVender(inputCode ,inputId ,isMulti ){
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/venderSelectSingle.jsp?INPUTCODE="+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti,730,450);
}

/**
 * 选择制造商
 * inputId   : 回填页制造商code域id
 * inputName ：回填页制造商id域id
 * isMulti   : true值多选，否则单选
 */
function showPartMaker(inputCode ,inputId ,isMulti ){
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/makerSelectSingle.jsp?INPUTCODE="+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti,730,390);
}

/**
 * 通用选择配件大类
 * inputId   : 回填页供应商code域id
 * inputName ：回填页供应商id域id
 * isMulti   : true值多选，否则单选
 */
function showPartType(inputCode ,inputId ,isMulti ){
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showPartType.jsp?INPUTCODE="+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti,730,390);
}

/**
 * 通用选择组织界面
 * inputCode : 回填页面经销商Code域id
 * inputId   : 回填页面经销商Id域id 
 * orgId     : 组织id，如有，则表示根据orgId过滤下级所有组织，否则不过滤
 */
function showOrg(inputCode ,inputId ,isMulti ,orgId)
{
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showOrg.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId,730,390);
}

/*
 * 选择大区
 * */
function showOrgLargerEgion( )
{
	
	OpenHtmlWindow(g_webAppName+'/dialog/showOrgLargerEgion.jsp?',800,500);
}

/**
 * 通用选择组织界面,安大区
 * inputCode : 回填页面经销商Code域id
 * inputId   : 回填页面经销商Id域id 
 * orgId     : 组织id，如有，则表示根据orgId过滤下级所有组织，否则不过滤
 */
function showOrgArea(inputCode ,inputId ,isMulti ,orgId)
{
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showOrgArea.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId,730,390);
}

function _showOrgNew_(inputCode ,inputId , theName,isMulti ,orgId)
{
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!theName) { theName = null ; }
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showOrg.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&THENAME="+theName+"&ISMULTI="+isMulti+"&ORGID="+orgId,730,390);
}

/**
 * 通用选择省分界面 YH 2011.6.15
 * inputCode : 回填页面Code域id
 * inputId   : 回填页面Id域id 
 * dealer_type : 经销商类型
 */
function showRegion(inputCode ,inputId,isMulti,dealer_type)
{   
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!dealer_type){ dealer_type = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showRegion.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&DEALER_TYPE="+dealer_type,730,390);
}

function showCity(inputCode ,inputId,isMulti,dealer_type)
{   
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!dealer_type){ dealer_type = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showCity.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&DEALER_TYPE="+dealer_type,730,390);
}

/**
 * 通用选择省分界面 zhumingwei 2011-10-21
 * inputCode : 回填页面Code域id
 * inputId   : 回填页面Id域id 
 */
function showRegion111(inputCode ,inputId,isMulti ){   
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showRegion.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti,730,390);
}

/**
 * 通用选择组织界面  杨震查询所有的  编码和 名称
 * inputCode : 回填页面经销商Code域id
 * inputId   : 回填页面经销商Id域id 
 * orgId     : 组织id，如有，则表示根据orgId过滤下级所有组织，否则不过滤
 */
function showAllOrgs(inputCode ,inputId ,isMulti ,orgId,areaId)
{
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showAllOrgs.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId+"&INPUTAREAID="+areaId,730,390);
}

function showNewOrg(inputCode ,inputId ,isMulti ,orgId)
{
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showNewOrg.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId,730,390);
}

function showDivideGroup(inputId, inputCode, inputName, groupType, groupArea) {
	inputId = (!inputId) ? null : inputId ;
	inputCode = (!inputCode) ? null : inputCode ;
	inputName = (!inputName) ? null : inputName ;
	OpenHtmlWindow(g_webAppName+'/groups/DivideGroupsAction/groupsDialogInit.do?inputId=' + inputId + '&inputCode=' + inputCode+ '&inputName=' + inputName+ '&groupType=' + groupType + '&groupArea=' + groupArea,800,450) ;
}

function showCompanyByOther(inputName ,inputId ,isMulti ,orgId, dealerType)
{
	if(!inputName){ inputName = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId){ orgId = null;}
	if(!dealerType){ dealerType = null;}
	
	var regionCode = null ;
	if(document.getElementById("regionCode")) {
		regionCode = document.getElementById("regionCode").value ;
	}
	
	OpenHtmlWindow(g_webAppName+'/dialog/showCompanyByOrg.jsp?INPUTNAME='+inputName+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId+"&DEALERTYPE="+dealerType+"&REGIONCODE="+regionCode,730,390);
}

/**
 * 通用-选择所有功能模块界面
 * funcName : 回填页面功能名称域id
 * funcId   : 回填页面功能Id域id
 * isMulti	 : 是否多选(true/false)
 * 2011-12-31
 * HXY
 */
function showAllFunc(funcName, funcId, isMulti)
{
	OpenHtmlWindow(g_webAppName+'/dialog/showAllFunc.jsp?INPUTNAME=' + funcName + '&INPUTID=' + funcId + '&ISMULTI=' + isMulti, 750, 420);
}
/**
 * 选择里程区域
 * inputId   : 回填页供应商code域id
 * inputName ：回填页供应商id域id
 * isMulti   : true值多选，否则单选
 *  2013-4-9
 * ranjian
 */
function showCityMileage(inputCode ,inputId ,isMulti ){
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	
//	var disIds = null ;
//	if(document.getElementById("disIds_")) 
//	{
//		disIds = document.getElementById("disIds_").value ;
//	}
//	
	OpenHtmlWindow(g_webAppName+"/dialog/showCityMileage.jsp?INPUTCODE="+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti,800,460);
}

function showCityMileageForDealer(inputCode ,inputId ,isMulti ){
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	var PROVINCE = document.getElementById("PROVINCE_ID").value;
	OpenHtmlWindow(g_webAppName+"/dialog/showCityForDealer.jsp?PROVINCE="+PROVINCE+"&ISMULTI="+isMulti,800,460);
}

//合同选择经销商
function showOrgDealerHT(inputCode ,inputId ,isMulti ,orgId,  isAllLevel, isAllArea,isDealerType,inputName)
{
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	if(!isAllLevel){ isAllLevel = null;}
	if(!isAllArea){ isAllArea = null;}
	if(!isDealerType){ isDealerType = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showOrgDealerHT.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId+"&ISALLLEVEL="+isAllLevel+"&ISALLAREA="+isAllArea+"&isDealerType="+isDealerType+"&inputName="+inputName,730,390);
}
//合同选择车系
function showMaterialGroup_SelHT(inputCode ,inputName ,isMulti ,groupLevel,gId)
{
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	if(!groupLevel){ groupLevel = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showMaterialGroup_SelHT.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&gId="+gId+"&ISMULTI="+isMulti+"&GROUPLEVEL="+groupLevel,770,410);
}


/**
 * 返利明细弹出层
 * inputId   : 回填页面物料组code域id
 * inputName ：回填页面物料组name域id
 * isMulti   : true值多选，否则单选
 * areaId   : 业务范围id
 * ids   : 已选、需要过滤掉的物料id
 */

function CarSalesRebateGroup(inputCode ,inputId, rebid, yieldId, isMulti ){
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	var disIds = null ;
	if(document.getElementById("disIds_")) 
	{
		disIds = document.getElementById("disIds_").value ;
	}
	OpenHtmlWindow(g_webAppName+"/dialog/showcarSalesRebate.jsp?INPUTCODE="+inputCode+"&rebid= '"+rebid+"'&INPUTID="+inputId+"&yieldId="+yieldId+"&ISMULTI="+isMulti+"&disIds_o="+disIds,730,390);
}

/**

 * 车系弹出层
 * inputId   : 回填页面物料组code域id
 * inputName ：回填页面物料组name域id
 * isMulti   : true值多选，否则单选
 * areaId   : 业务范围id
 * ids   : 已选、需要过滤掉的物料id
 */
function CarsRebateGroup(inputCode ,inputName ,isMulti , areaId, ids, productId){
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showcarsRebate.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&areaId="+areaId+"&ids="+ids+"&productId="+productId,730,390);
}

function showExam(isMulti ,qrId)
{
	if(!isMulti){ isMulti = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showExam.jsp?ISMULTI='+isMulti+"&qrId="+qrId,730,390);
}

/**
 * 区域显示弹出层
 * 
 * @param type 弹出页面类型
 */
function showArea(type) {
	OpenHtmlWindow(g_webAppName+'/dialog/org/showArea.jsp?type=' + type,730,390);
}

/**
 * 经销商显示弹出层 可翻页复选
 */
function showDealer() {
	OpenHtmlWindow(g_webAppName+'/dialog/dealer/showDealer.jsp',730,390);
}

/**
 * 选择里程区域
 * inputId   : 回填页供应商code域id
 * inputName ：回填页供应商id域id
 * isMulti   : true值多选，否则单选
 *  2013-4-9
 * ranjian
 */
function showCityMileage4Part(inputCode ,inputId ,isMulti ){
    if(!inputCode){ inputCode = null;}
    if(!inputId){ inputId = null;}

//	var disIds = null ;
//	if(document.getElementById("disIds_"))
//	{
//		disIds = document.getElementById("disIds_").value ;
//	}
//
    OpenHtmlWindow(g_webAppName+"/dialog/showCityMileage4Part.jsp?INPUTCODE="+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti,800,460);
}

/**
 * 获取TcCode并展示
 * @param inputType
 * @param inputDesc
 * @param tcType
 * @param noShowTcType
 */
function showTcCode(inputType ,inputDesc ,tcType ,noShowTcType)
{
	OpenHtmlWindow(g_webAppName+'/claim/preAuthorization/Authorization/getTcCodeListInit.do?idInput='+inputType+"&nameInput="+inputDesc+"&CODE_TYPE="+tcType+"&NOT_CODE_ID="+noShowTcType,800,460);
}

function tcCodeConfirm(idInput, nameInput, ids, idName){
	if(null != idInput && idInput != ""){
		document.getElementById(idInput).value = ids;
	}
	if(null != idName && idName != ""){
		document.getElementById(nameInput).value = idName;
	}
	_hide();
}

/**
 * 通用选择顾问界面(销售线索分派用)
 * inputCode : 回填页面经销商Code域id
 * inputId   : 回填页面经销商Id域id 
 * orgId     : 组织id，如有，则表示根据orgId过滤下级所有经销商，否则不过滤
 */
function showAdviserByLeads(inputCode ,inputId , isMulti, leadsCode, leadsGroup,dealerId, orgId, isAllLevel, isAllArea,isDealerType,inputName)
{
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	if(!isAllLevel){ isAllLevel = null;}
	if(!isAllArea){ isAllArea = null;}
	if(!isDealerType){ isDealerType = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showAdviserByLeads.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId+"&ISALLLEVEL="+isAllLevel+"&ISALLAREA="+isAllArea+"&isDealerType="+isDealerType+"&inputName="+inputName+"&leadsCode="+leadsCode+"&leadsGroup="+leadsGroup+"&dealerId="+dealerId,450,200);
}