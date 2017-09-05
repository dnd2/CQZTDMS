<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单跟踪经销商选择</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body onunload='javascript:destoryPrototype();' background="<%=contextPath%>/img/chana/BJ-BJ.jpg">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：订单管理 &gt; > 销售订单管理　> 订单查询  >　销售订单跟踪 </div>
<form method="post" name="fm" id="fm">
<input value='' type='hidden' class="normal_btn" name='deptId' />
<input value='' type='hidden' class="normal_btn" name='poseId' />
	<table class="table_query" >
		<tr>
			 <td  height="100" align="left" valign="bottom" ><font color="red" >&nbsp;&nbsp;&nbsp;提示：<br/>
			 &nbsp;&nbsp;&nbsp;&nbsp;本功能可以对订单执行情况、订单车辆在途情况查询。<br/>
			 &nbsp;&nbsp;&nbsp;&nbsp;请按照如下步骤操作：<br/>
			 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1、选择一个具体的经销商<br/>
			 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、点击<input type="button"   class="cssbutton" value="订单查询" />按钮查询经销商订单执行情况<br/>
			 &nbsp;&nbsp;&nbsp;&nbsp;(注：请不要设置弹出窗口屏蔽功能)</font></td>
		</tr>
		<tr>
			<td align="center">经销商:
				<input type="text" class="middle_txt" name="dealerCode" size="15" id="dealerCode" value="" datatype="1,is_null,18"/>
				<input id="button3" name="button3" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','dealerId','false');" value="..." />
				<input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn" />&nbsp;<font color="red">*</font>
			</td>
		</tr>
	   	<tr>
		   	<td align="center">
				<input type="button" id="queryBtn2" name="queryBtn2" class="cssbutton" onclick="searchQuery();" value="订单查询"  />
		   	</td>
	    </tr>	 	
     </table>     
	<!-- 查询条件 end -->
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end --> 
</form>
<script type="text/javascript">
	var url;	
	var title = null;	
	var columns;		
	function searchQuery(){
		if($('dealerCode').value=="") {
			MyAlert("必须选择经销商!")
			return;
			};
		url = "<%=contextPath%>/sales/ordermanage/orderquery/OemOrderTrace/getDealerInfo.json";
		columns = [
				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center', renderer:myLink1} 
		      ];
		__extQuery__(1); 
	}
	//清空文本框内容
	function txtClr(txtId){
    	document.getElementById(txtId).value = "";
    }
	//页面显示经销商
	function myLink1(value,metaDate,record){
		callRedirect(record.data.DEALER_ID);
	  	return String.format("<a href=\"#\" onclick='callRedirect(\""+record.data.DEALER_ID+"\")'>"+value+"</a>");			 
	}
 	//调用OTD页面
	function callRedirect(dealer_Id) {
		var fm=document.fm;
		window.open("http://www.changan-car.com/oms/dealer/dealerMain.action?DEALER_ID="+dealer_Id,"订单跟踪查询", "height=700, width=1000, top=50,left=100, toolbar=no, menubar=no, scrollbars=no, resizable=yes,location=yes, status=no");
	}
	
	
</script>
</body>
</html>
