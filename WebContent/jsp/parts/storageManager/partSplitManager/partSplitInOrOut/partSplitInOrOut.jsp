<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件拆合件出库</title>

</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);loadcalendar();">
<div class="wbox">
	<div class="navigation"> <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
	 配件管理&gt;配件仓库管理  >配件拆合件管理&gt;配件拆合件出入库
	</div>
<form name="fm" id="fm" method="post" >
	<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" >
		<th colspan="4"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />查询条件</th>
	    <tr >
	       <td width="20%" class="table_query_right" align="right"> 拆合单号：</td>
	       <td width="30%"   align="left"><input class="middle_txt" type="text" name="SPCPD_CODE" id="SPCPD_CODE"/></td>
	       <td width="20%" class="table_query_right"  align="right">制单日期：</td>
	       <td width="30%" align="left">
           <div align="left">
           		<input name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't1', false);"/>
           		&nbsp;至&nbsp;
           		<input name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't2', false);"/>
            </div>
           </td>
      </tr>    
	  <tr>
	   <td   colspan="4" align="center">
	   		<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查 询" onclick="__extQuery__(1);"/>
       </td>
      </tr>
	</table>
  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  
</form>
<script type="text/javascript" >
autoAlertException();//输出错误信息
	var myPage;

	var url = "<%=contextPath%>/parts/storageManager/partSplitManager/PartSplitInOrOutManager/queryPartSpiltApplyInfo.json";
				
	var title = null;

	var columns = [
				{header: "序号", align:'center',renderer:getIndex},
				{header: "拆合单号", dataIndex: 'SPCPD_CODE', align:'center'},
				{header: "制单单位", dataIndex: 'ORG_CNAME', align:'center'},
				{header: "制单人", dataIndex: 'CREATE_NAME', align:'center'},
				{header: "制单日期", dataIndex: 'CREATE_DATE', align:'center',renderer:formatDate},
				{header: "仓库", dataIndex: 'WH_CNAME', align:'center'},
				{header: "货位", dataIndex: 'LOC_NAME', align:'center'},
				{header: "总成件件号", dataIndex: 'PART_CODE', align:'center'},
				{header: "总成件编码", dataIndex: 'PART_OLDCODE', align:'center'},
				{header: "总成件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "拆合类型", dataIndex: 'SPCPD_TYPE', align:'center',renderer:getItemValue},
				{header: "拆合数量", dataIndex: 'QTY', align:'center'},
				{header: "总成件库存数量", dataIndex: 'NORMAL_QTY', align:'center'},
				{header: "状态", dataIndex: 'STATE', align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'SPCPD_ID',renderer:myLink ,align:'center'}
		      ];
	    
//设置超链接  begin    
	  
	
	//设置超链接
	function myLink(value,meta,record){
		var state = record.data.STATE;
		var stype = record.data.SPCPD_TYPE;
		var flag = 0;//出入库标志
		if(state==<%=Constant.PART_SPCPD_STATUS_04%>){
			return String.format("<a href=\"#\" onclick='view(\""+value+"\")'>[查看]</a><a href=\"#\" onclick='printInOrOut("+value+")'>[打印]</a>");
		}
		if(stype==<%=Constant.PART_SPCPD_TYPE_01%>){
			flag = 2;
			return String.format("<a href=\"#\" onclick='view1(\""+value+"\","+state+")'>[查看]</a><a href=\"#\" onclick='outOrInPart(\""+value+"\","+flag+")'>[出库]</a>");
		}
		if(stype==<%=Constant.PART_SPCPD_TYPE_02%>){
			flag = 1;
			return String.format("<a href=\"#\" onclick='view1(\""+value+"\","+state+")'>[查看]</a><a href=\"#\" onclick='outOrInPart(\""+value+"\","+flag+")'>[入库]</a>");
		}
	}
	
	//查看申请详细页面
	function view1(value,state)
	{
		var flag = 0;
		if(state==<%=Constant.PART_SPCPD_STATUS_02%>){
			flag = 1;
		}
		window.location.href = '<%=contextPath%>/parts/storageManager/partSplitManager/PartSpiltApplyManager/querySpiltApplyDetailInit.do?spcpdId='+value+'&flag='+flag+'&flag1=1';
	}
	
	//查看
	function view(value)
	{
		window.location.href = '<%=contextPath%>/parts/storageManager/partSplitManager/PartSplitInOrOutManager/querySpiltInOrOutDetailInit.do?spcpdId='+value;
	}
	//出库
	function outOrInPart(value,flag)
	{
		if(confirm("确定这样操作?")){
			btnDisable();
			var url = '<%=contextPath%>/parts/storageManager/partSplitManager/PartSplitInOrOutManager/outOrInPartMain.json?spcpdId='+value+'&flag='+flag;
			sendAjax(url,getResult,'fm');
		}   
	}

	function getResult(jsonObj) {
		btnEnable();
		  if(jsonObj!=null){
		     var success = jsonObj.success;
		     var error = jsonObj.error;
		     if(success){
		    	 MyAlert(success);
			 }
		     if(error){
		    	 MyAlert(error);
			 }
		     window.location.href = '<%=contextPath%>/parts/storageManager/partSplitManager/PartSplitInOrOutManager/queryPartSplitApplyInit.do';
		  }
	 }

	//打印
	function printInOrOut(value)
	{
		OpenHtmlWindow("<%=contextPath%>/parts/storageManager/partSplitManager/PartSplitInOrOutManager/opPrintHtml.do?spcpdId="+value,1100,400);
	}

	
	//格式化日期
	function formatDate(value,meta,record){
		var output = value.substr(0,10);
		return output;
	}
//设置超链接 end
</script>
</div>
</body>
</html>