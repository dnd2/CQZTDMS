<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
	String reFlag = (String)request.getAttribute("REFLAG");
	
%>
<script type="text/javascript">


var count=0;


</script>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>工时单价维护</title>
</head>
<body>
<div class="wbox">
<form name='fm' id='fm'>
<input type="hidden" name="wrmodelgrouplist" id="wrmodelgrouplist" value="<%=request.getAttribute("wrmodelgrouplist")%>"/>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;工时单价政策</div>
    <div class="form-panel">
		<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
   <table  class="table_query">
   			<tr>
            	<td style="text-align:right">政策编码：</td>
				<td><input name="policyNo" id="policyNo" value="" type="text" class="middle_txt"/></td>
            	 <td style="text-align:right">政策状态：</td>
			<td>
				<script type="text/javascript">
        			genSelBoxExp("changeStatus",<%=Constant.LABOUR_CHANG_STATUS%>,"",true,"","","false","");
        		</script>
			</td>
           </tr>
          <tr>
            <td style="text-align:right">变更类型：</td>
			<td>
				<script type="text/javascript">
        			genSelBoxExp("changeType",<%=Constant.LABOR_CHANGE_TYPE%>,"",true,"","","false","");
        		</script>
			</td>
            <td style="text-align:right">政策名称：</td>
			<td><input name="policyName" id="policyName" value="" type="text" class="middle_txt"/></td>
           </tr>
		   <tr>    
		   <td colspan="4"  style="text-align:center">
            <input class="normal_btn" type="button" name="queryBtn" id="queryBtn" value="查询"  onclick="__extQuery__(1)"/>
            <input class="normal_btn" type="button" value="新增" name="add" onclick="subFun();"/>
           </td>
           </tr>
  </table>
  </div>
  </div>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
</form>  
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/LaborPriceMain/laborPriceQuery.json";
	var title = null;
	
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'},
				{header: '政策编码',align:'center',dataIndex:'POLICY_NO'},
				{header: '变更类型',align:'center',dataIndex:'CHANG_TYPE',renderer:getItemValue},
				{header: "政策名称",sortable: false,dataIndex: 'POLICY_NAME',align:'center'},
				{header: "创建时间",sortable: false,dataIndex: 'CREATE_DATES',align:'center'},
				{header: "执行时间",sortable: false,dataIndex: 'GENERATE_DATES',align:'center'},
				{header: "生效时间",sortable: false,dataIndex: 'POLICY_START_DATE',align:'center',renderer:subDate},
				{header: "失效时间",sortable: false,dataIndex: 'POLICY_END_DATE',align:'center',renderer:subDate1},
				{header: "政策状态",dataIndex: 'CHANG_STATUS',align:'center',renderer:getItemValue},
				{header: "终止时间",dataIndex: 'END_DATE',align:'center'},
				{header: "终止人",dataIndex: 'NAME',align:'center'}
		      ];  
    
	
	function subDate(value,meta,record){
		var POLICY_START_DATE = record.data.POLICY_START_DATE;
		var aa = POLICY_START_DATE.substring(0,11);
	 	return String.format(aa);
	}
	function subDate1(value,meta,record){
		var POLICY_END_DATE = record.data.POLICY_END_DATE;
		var aa = POLICY_END_DATE.substring(0,11);
	 	return String.format(aa);
	}
//修改的超链接设置
function myLink(value,meta,record){
	var status=record.data.CHANG_STATUS;
	if(status==95461001){
		return String.format("<a  href='#' id='disabledMake' onclick='isCheck("+value+");'>[生效]</a>"); 
		<%-- <a href='<%=contextPath%>/claim/basicData/LaborPriceMain/updateLaborPrice.do?ID="+value+"'>[修改]</a> --%>
	}
	if(status==95461002){
		return String.format("<a  href='<%=contextPath%>/claim/basicData/LaborPriceMain/laborPriceInfo.do?ID="+value+"'>[明细]</a><a  href='#' id='endValue' onclick='endValue("+value+");'>[终止]</a>");
	}
	if(status==95461003){
		return String.format("<a  href='<%=contextPath%>/claim/basicData/LaborPriceMain/laborPriceInfo.do?ID="+value+"'>[明细]</a>");
	}
}
//设置超链接
function mySelect(value,meta,record){
 		return String.format(
        "<a href=\"#\" onclick='selbyid(\""+record.data.ID+"\")'>["+ value +"]</a>");
}
//详细页面
function selbyid(value){
	OpenHtmlWindow('<%=contextPath%>/claim/basicData/ClaimLaborMain/claimLaborDetail.do?ID='+value,900,500);
}
//新增
function subFun(){
	location="<%=contextPath%>/claim/basicData/LaborPriceMain/LaborPeiceAddInit.do";   
}
//清除经销商代码
function clr() {
	  document.getElementById('dealerCode').value = "";
}
function endValue(value){
	MyConfirm("确定终止?",conSubmit2,[value]);	
}

function conSubmit2(id){
	count=1;
	var url="<%=contextPath%>/claim/basicData/LaborPriceMain/laborPriceEnd.json?ID="+id;
	makeNomalFormCall(url,returnBack2,'fm','queryBtn');
}
function returnBack2(json){
	var ok = json.ok;
	if(ok=='ok'){
		MyAlert("终止成功!");
		__extQuery__(1);
	}
}
function isCheck(id){
	if(count<1){
		MyConfirm("确定生效?",conSubmit,[id]);	
	}
	else{
		MyAlert("程序正在处理，请不要重复生成");
	}
}
function conSubmit(id){
	count=1;
	var url="<%=contextPath%>/claim/basicData/LaborPriceMain/laborPriceSC.json?ID="+id;
	makeNomalFormCall(url,returnBack,'fm','queryBtn');
}
function returnBack(json){
	var ok = json.ok;
	if(ok=='ok'){
		MyAlert("成功生效!");
		__extQuery__(1);
	}
}
</script>
</body>
</html>