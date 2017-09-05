<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商索赔申报审核明细</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
function doInit(){
   	loadcalendar();  //初始化时间控件
}
</script>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理&gt;服务商报表&gt;经销商索赔申报审核明细</div>
  <form method="post" name="fm" id="fm">
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
   <!-- 查询条件 begin -->
  <table class="table_query">
 		<tr>
 			<td width="7%" align="right" nowrap>索赔单号：</td>
            <td><input id ="balanceNo" name="balanceNo" class="middle_txt" value=""/></td>
            <td align="right" nowrap="nowrap">索赔类型：</td>
            <td nowrap="nowrap">
            	<script type="text/javascript">
				 	genSelBoxExp("claimType","<%=Constant.CLA_TYPE%>","",true,"short_sel","","true",'');
		    	</script> 	
            </td>
        </tr>
        <tr>
            <td align="right" nowrap="nowrap">索赔单据状态：</td>
            <td align="left" >
            	<script type="text/javascript">
				 	genSelBoxExp("STATUS","<%=Constant.CLAIM_APPLY_ORD_TYPE%>","",true,"short_sel","","true",'<%=Constant.CLAIM_APPLY_ORD_TYPE_01%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_02%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_03%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_05%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_06%>');
		    	</script> 	
			</td>
			<td align="right" nowrap="nowrap">审核通过起止日期： </td>
            <td nowrap="nowrap">
            	<div align="left">
            		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            		&nbsp;至&nbsp;
            		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            	</div>
			</td>
          </tr>
          <tr>
          	<td align="right" nowrap="nowrap">基地：</td>
            <td align="left" colspan="3" nowrap="nowrap">
            	<script type="text/javascript">
				 	genSelBoxExp("YIELDLY","<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>","",true,"short_sel","","true",'');
		    	</script> 
            </td>
          </tr>                
          <tr>
            <td align="center" colspan="4" nowrap="nowrap">
            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询"  onclick="__extQuery__(1)"/>&nbsp;
            </td>
          </tr>
  </table>
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>

<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	//查询路径
	var url = "<%=contextPath%>/report/jcafterservicereport/VehicleGoodRepairReport/queryDealerAppDetail.json";	
	var title = null;
	var columns = [
				{header: "序号", dataIndex: 'NUM', align:'center'},
				{header: "索赔单号",sortable: false,dataIndex: 'CLAIM_NO' ,align:'center',renderer:queryClaimNo},
				{header: "索赔类型", dataIndex: 'CLAIM_TYPE', align:'center',renderer:getItemValue},
				{header: "索赔单据状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "审核通过日期", dataIndex: 'AUDITING_DATE', align:'center'},
				{header: "申报配件金额", dataIndex: 'PART_AMOUNT', align:'center'},
				{header: "申报工时金额", dataIndex: 'LABOUR_AMOUNT', align:'center'},
				{header: "申报追加工时金额", dataIndex: 'APPLY_APPENDLABOUR_AMOUNT', align:'center'},
				{header: "申报其他费用金额", dataIndex: 'NETITEM_AMOUNT', align:'center'},
				{header: "申报服务活动金额", dataIndex: 'CAMPAIGN_FEE', align:'center'},
				{header: "申报保养金额", dataIndex: 'FREE_M_PRICE', align:'center'},
				{header: "申报特殊费用金额", dataIndex: 'APPLY_APPEND_AMPUNT', align:'center'},
				{header: "申报索赔单总金额", dataIndex: 'CLAIMTOTAL', align:'center'},
				
				{header: "结算配件金额", dataIndex: 'BALANCE_PART_AMOUNT', align:'center'},
				{header: "结算工时金额", dataIndex: 'BALANCE_LABOUR_AMOUNT', align:'center'},
				{header: "结算追加工时金额", dataIndex: 'APPENDLABOUR_AMOUNT', align:'center'},
				{header: "结算其他费用金额", dataIndex: 'BALANCE_NETITEM_AMOUNT', align:'center'},
				{header: "结算特殊费用金额", dataIndex: 'APPEND_AMOUNT', align:'center'},
				{header: "结算索赔单总金额", dataIndex: 'BALANCETOTAL', align:'center'},
				
				{header: "配件扣款", dataIndex: 'PARTBALANCE', align:'center'},
				{header: "工时扣款", dataIndex: 'APPENDAMOUNT', align:'center'},
				{header: "追加工时扣款", dataIndex: 'APPLYAMOUNT', align:'center'},
				{header: "其他费用扣款", dataIndex: 'ORTERAMOUNT', align:'center'},
				{header: "特殊费用扣款", dataIndex: 'TESHUAMOUNT', align:'center'},
				{header: "索赔单总扣款", dataIndex: 'SUBCLAIMTOTAL', align:'center'},
				{header: "结算厂家", dataIndex: 'YIELDLY', align:'center',renderer:getItemValue}
		      ];
		      
//设置超链接    
function queryClaimNo(value,meta,record){
	return String.format("<a href=\"#\" onclick=\"queryDetail("+record.data.ID+")\">[" + value + "]</a>");
}
//查询索赔单明细
function queryDetail(id){
	var tarUrl = "<%=contextPath%>/claim/application/ClaimBillStatusTrack/claimBillDetailForward.do?ID="+id;
	var width=900;
	var height=500;
	var screenW = window.screen.width-30;	
	var screenH = document.viewport.getHeight();
	if(screenW!=null && screenW!='undefined')
		width = screenW;
	if(screenH!=null && screenH!='undefined')
		height = screenH;
	
	OpenHtmlWindow(tarUrl,width,height);
}	
</script>
<!--页面列表 end -->
</body>
</html>