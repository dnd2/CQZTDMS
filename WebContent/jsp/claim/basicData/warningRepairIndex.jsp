<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%String contextPath = request.getContextPath();%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>三包预警规则设置</title>
</head>
<body>
<div class="wbox">
<form method="post" name='fm' id='fm'>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基本数据&gt;三包预警规则设置&gt;三包预警规则设置</div>
  <div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
   <table class="table_query">
          <tr>
            <td style="text-align:right">规则代码：</td>
            <td>
                 <input name="WARNING_CODE" id="WARNING_CODE"  type="text" class="middle_txt"/>
            </td>
            <td style="text-align:right">预警说明：</td>
            <td>
				<input name="WAINING_REMARK" id="WAINING_REMARK"  type="text" class="middle_txt"/>
            </td>
            <td style="text-align:right">预警等级：</td>
          	<td>
            	<script type="text/javascript">
	              genSelBoxExp("WAINING_LEVEL",<%=Constant.SWANINGTIME_LEVEL%>,"",true,"","","",'');
	            </script>
	        </td>
          </tr>
          <tr>
            <td style="text-align:right">预警类别：</td>
            <td>
            	<script type="text/javascript">
	              genSelBoxExp("WARNING_TYPE",<%=Constant.WANINGTIME_TYPE%>,"",true,"","","",'');
	            </script>
            </td>
            <td style="text-align:right">是否累计：</td>
            <td>
            	<script type="text/javascript">
	              genSelBoxExp("is_Accumulative",<%=Constant.IF_TYPE%>,"<%=Constant.IF_TYPE_NO%>",true,"","","",'');
	            </script>
	        </td>
            <td style="text-align:right">状态：</td>
            <td>
            	<script type="text/javascript">
	              genSelBoxExp("STATUS",<%=Constant.STATUS%>,<%=Constant.STATUS_ENABLE%>,true,"","","",'');
	            </script>
            </td>                  
           </tr>
		   <tr>    
			   <td colspan="6"  style="text-align:center">
	            <input class="normal_btn" type="button" name="queryBtn" id="queryBtn" value="查询"  onclick="__extQuery__(1) ;"/>
	            <input class="normal_btn" type="button" value="新增" name="add" onclick="subFun();"/>
	            <input class="normal_btn" type="button" value="失效" name="OnDel" onclick="OnDel1();"/>
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
	var url = "<%=request.getContextPath()%>/claim/basicData/WarningRepairMain/WarningRepairQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "操作",sortable: false,dataIndex: 'WARNING_REPAIR_ID',renderer:myLink ,align:'center'},
				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"recesel\")' />全选", align:'center',sortable:false, dataIndex:'WARNING_REPAIR_ID',width:'2%',renderer:checkBoxShow},
				{header: "规则代码",sortable: false,dataIndex: 'WARNING_CODE',align:'center'},				
				{header: "预警说明",sortable: false,dataIndex: 'WAINING_REMARK',align:'center'},
				{header: "预警等级",sortable: false,dataIndex: 'WAINING_LEVEL',align:'center',renderer:warningTypeImg},
				{header: "预警类别",sortable: false,dataIndex: 'WARNING_TYPE',align:'center',renderer:getItemValue},
				{header: "是否累计",sortable: false,dataIndex: 'IS_ACCUMULATIVE',align:'center',renderer:getItemValue},
				{header: "预警起次数",sortable: false,dataIndex: 'WARNING_NUM_START',align:'center'},
				{header: "预警止次数",sortable: false,dataIndex: 'WARNING_NUM_END',align:'center'},
				{header: "有效期(月)",sortable: false,dataIndex: 'VALID_DATE',align:'center'},
				{header: "有效里程",sortable: false,dataIndex: 'VALID_MILEAGE',align:'center'},
				{header: "法定条款",sortable: false,dataIndex: 'CLAUSE_STATUTE',align:'center'},
				{header: "状态",sortable: false,dataIndex: 'STATUS',align:'center',renderer:getItemValue}
		      ];

//设置超链接  begin      
function checkBoxShow(value,meta,record){
	return String.format("<input type='checkbox' id='recesel' name='recesel' value='" + record.data.WARNING_REPAIR_ID + "' />");
}

//控制图片
function warningTypeImg(value,meta,record){
	if(value == <%=Constant.SWANINGTIME_LEVEL_01 %>)
	{
		return String.format("<img src='<%=contextPath%>/img/red.jpg'/>");
	}
	else if(value == <%=Constant.SWANINGTIME_LEVEL_02 %>)
	{
		return String.format("<img src='<%=contextPath%>/img/ced.jpg'/>");
	}
	else
	{
		return String.format("<img src='<%=contextPath%>/img/yellow.jpg'/>");
	}
}

//新增
function subFun(){
	var url = "<%=contextPath%>/claim/basicData/WarningRepairMain/WarningRepairAddInit.do"; 
	OpenHtmlWindow(url, 1000, 550, '三包预警规则新增');
	
    <%-- location="<%=contextPath%>/claim/basicData/WarningRepairMain/WarningRepairAddInit.do";    --%>
}

function updateIt(value) {
	var url = "<%=contextPath%>/claim/basicData/WarningRepairMain/warningRepairUpdateInit.do?warningId=" + value;
	OpenHtmlWindow(url, 1000, 550, '三包预警规则修改');
	
	/* fm.action = url ;
	fm.submit() ; */
}

//修改
function myLink(value,meta,record){
	return String.format("<a href='#' onclick='updateIt(" + value + ")'>[修改]</a>");
}
//设置超链接 end

//删除
function OnDel1() {
	var allChecks = document.getElementsByName("recesel");
	var allFlag = false;
	for(var i = 0;i<allChecks.length;i++){
		if(allChecks[i].checked){
			allFlag = true;
		}
	}
	if(allFlag){
		MyConfirm("确认失效选中的预警规则?",changeSubmit);
	}else{
		MyAlert("请选择后再点击操作失效按钮!");
	}
}
function changeSubmit() {
	var url="<%=contextPath%>/claim/basicData/WarningRepairMain/warningRepairDel.json";
	makeNomalFormCall(url,showResult,'fm');
}
function showResult(json){
	var msg=json.msg;
	if(msg=='01'){
		MyAlert('失效成功!');
		__extQuery__(1);
	}else{
		MyAlert('操作失败!');
	}
}
</script>
</div>
</body>
</html>