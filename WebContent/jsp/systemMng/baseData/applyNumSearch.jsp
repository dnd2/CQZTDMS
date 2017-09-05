<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/globalVariable.jsp" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/mootools.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/jslib/my-grid-pager.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/validate/validate.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/web/function_tree.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath%>/js/web/dtree.js"></script>
<title>使用次数查询</title>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：系统管理 &gt;系统运行情况&gt;使用次数查询</div>
<form id="fm" name="fm" method="post">
<input id="SECTION"  type="hidden" name="SECTION" value=""/> <!-- 链接 -->
<input id="TYPE"  type="hidden" name="TYPE" value=""/> <!-- 菜单层级 -->
<input type="hidden" id="orderCol" name="orderCol" value="" />
<input type="hidden" id="order" name="order" value="" />
<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
<table class="table_info" >
<tr>
		<td >年份：</td>
		<td ><span id="sp1">
			<select  name="YEAR" id="idYear">
				<option value="">请选择</option>
				<option value="2008">2008年</option>
                <option value="2009">2009年</option>
			</select></span>
		</td>
		<td >月份：</td>
		<td ><span id="sp2">
			<select name="MONTH" id="idMonth">
				<option value="">请选择</option>
                <option value="01">1月</option>
				<option value="02">2月</option>
				<option value="03">3月</option>
				<option value="04">4月</option>
				<option value="05">5月</option>
				<option value="06">6月</option>
				<option value="07">7月</option>
				<option value="08">8月</option>
				<option value="09">9月</option>
				<option value="10">10月</option>
				<option value="11">11月</option>
				<option value="12">12月</option>
			</select></span>
		</td>
		<td >日期：</td>
			<td ><span id="sp3">
				<select name="DAY" id="idDay">
				<option value="">请选择</option>
				<option value="01">01</option>
				<option value="02">02</option>
				<option value="03">03</option>
				<option value="04">04</option>
				<option value="05">05</option>
				<option value="06">06</option>
				<option value="07">07</option>
				<option value="08">08</option>
				<option value="09">09</option>
				<option value="10">10</option>
				<option value="11">11</option>
				<option value="12">12</option>
				<option value="13">13</option>
				<option value="14">14</option>
				<option value="15">15</option>
				<option value="16">16</option>
				<option value="17">17</option>
				<option value="18">18</option>
				<option value="19">19</option>
				<option value="20">20</option>
				<option value="21">21</option>
				<option value="22">22</option>
				<option value="23">23</option>
				<option value="24">24</option>
				<option value="25">25</option>
				<option value="26">26</option>
				<option value="27">27</option>
				<option value="28">28</option>
				<option value="29">29</option>
				<option value="30">30</option>
				<option value="31">31</option>
			</select></span>
			</td>
			<td >功能类型：</td>
			<td  nowrap="nowrap">
		          <select onchange="selType(this,<%=Constant.SYS_USER_SGM%>,<%=Constant.SYS_USER_DEALER%>)" class="short_sel" name="funType" id="funType">
		            <option value="10021002" selected="selected">经销商</option>
		            <option value="10021001">SGM</option>
		          </select>
			</td>
			<td >功能选择：</td>
			<td  >
			<input class="middle_txt" value="" id="FUNNAME" name="FUNNAME" onblur="isCloseTreeDiv(event,this,'dtree')" onclick="showFUNC()" style="cursor: pointer;" readonly="readonly" type="text"  />
			</td>
		<tr>	
		<td  class="table_query_last" nowrap="nowrap" colspan="10" align="center">
			<input  class="normal_btn" type="button" id="queryBtn" value="查 询" onclick="__extQuery__(1);"/>
            &nbsp;
            <input class="normal_btn" type="button" value="下 载" onclick="download();"/>
         <td>
	</tr>
</table>
</form>
</div>
<div id="_page" style="margin-top:15px;display:none;"></div>
<div id="myGrid" ></div>
<div id="myPage" class="pages"></div>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<script type="text/javascript" >
validateConfig.divCount = 3;
validateConfig.isOnBlur = false;

var func_tree_url = "<%=contextPath%>/sysmng/systemRunSituation/ApplyCount/initFunTree.json";

function selType(obj,sgmCode,dealerCode) {
	if(obj.value == sgmCode) {
		$('FUNNAME').value="";
		$('SECTION').value="";
		sendAjax(func_tree_url,createTree,'fm');
	}else if(obj.value == dealerCode) {
		$('FUNNAME').value="";
		$('SECTION').value="";
		sendAjax(func_tree_url,createTree,'fm');
	}
}

var myPage;
function __extQuery__(page){
	var st = decide();
	if(st != true) {
		return false;
	}
	$("queryBtn").disabled = "disabled";
	showMask();
	sendAjax(url+(url.lastIndexOf("?") == -1?"?":"&")+"curPage="+page,callBack,'fm');
}
	var url = "<%=contextPath%>/sysmng/systemRunSituation/ApplyCount/applyCountSearch.json?COMMAND=1";

	var title = "Action使用次数统计";
	
	//设置列字段
	var columns =  [
					{header: "序号", align:'center', renderer:getIndex,width:30},
					{header: "Action使用日期", dataIndex: 'month', align:'center',orderCol:"ACDATA"},
					{header: "Action使用次数", dataIndex: 'acCount', align:'center',orderCol:"COUNT"}
				  ];
	
   function  decide(){
	   var flag = true;
	   var  year=$("idYear").value;
	   var  month=$("idMonth").value;
	   var  day=$("idDay").value;
	   var funname = $('FUNNAME').value;
	   if(month!="" && year==""){
		   showErrMsg('idYear','请输入年份！','30');
		   flag = false;
	   }
	   if(day!="" && month==""){
		   showErrMsg('idMonth','请输入月份！','30');
		   flag = false;
	   }
	   if(day!="" && year==""){
		   showErrMsg('idYear','请输入年份！','30');
		   flag = false;
	   }
	   if(year=="" && month=="" && day!=""){
		   showErrMsg('idYear','请输入年份！','30');
		   showErrMsg('idMonth','请输入月份！','30');
		   flag = false;
	   }
	   if( funname == ""){
		   showErrMsg('FUNNAME','请选择功能！','30');
		   flag = false;
	   }
	   return	flag;
   }
   function download(){
	   var st = decide();
		if(st != true) {
			return false;
		}
		fm.action="<%=contextPath%>/sysmng/systemRunSituation/ApplyCount/actionCountDownload.do";
		fm.submit();
   }
</script>
</body>
</html>