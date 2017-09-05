<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>在途位置查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理> 在途位置查询</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td class="right">发运单号：</td>
			<td align="left">
				<input type="text" maxlength="20"  maxlength="20" name="shipNo" id="shipNo" class="middle_txt"/>
			</td>
			<td class="right">运单生成日期：</td>
			<td align="left">
				<input class="short_txt" readonly="readonly" type="text" maxlength="20"  id="t1" name="startDate" datatype="1,is_date,10" group="t1,t2"  value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't1', false);" value="&nbsp;" />&nbsp;至&nbsp;
				<input class="short_txt" readonly="readonly" type="text" maxlength="20"  id="t2" name="endDate" datatype="1,is_date,10" group="t1,t2" value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't2', false);" value="&nbsp;" />
			</td>
		</tr>
		<tr>
			<td class="right">承运商：</td>
			<td align="left" colspan="3">
		 	<select name="LOGI_NAME_SEACH" id="LOGI_NAME_SEACH" class="u-select" >
		 		<option value="">-请选择-</option>
				<c:if test="${list_logi!=null}">
					<c:forEach items="${list_logi}" var="list_logi">
						<option value="${list_logi.LOGI_ID}">${list_logi.LOGI_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
	  		</td>
		</tr>
		<%--
		 <tr class="csstr" align="center">	
 		<td class="right">省份：</td>  
		    <td align="left">
	  		<select class="u-select" id="txt1" name="PROVINCE_ID" onchange="_genCity(this,'txt2')"></select>
     	 </td> 
     	 <td class="right">地级市：</td>  
		    <td align="left">
	  		<select class="u-select" id="txt2" name="CITY_ID">
	  			<option value="">-请选择-</option>
	  		</select>
     	 </td>   
		 </tr>
		 
		 <tr class="csstr" align="center">	  
			<td class="right">区县：</td>
		   	<td align="left" colspan="3">
	  			<input type="text" maxlength="20"  id="COUNTY_ID" name="COUNTY_ID" class="middle_txt"  size="15" />
			 </td> 
		 </tr>
		  --%>
		 <tr>
			<td align="center" colspan="4">
				<input name="resetBtn" type="reset" class="normal_btn"  value="重置">
				<input name="queryBtn" id="queryBtn" type=button class="normal_btn" onClick="__extQuery__(1);" value="查询">
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
	var url = "<%=contextPath%>/sales/storage/sendmanage/CommprManage/locationMaintainQuery.json";
	var title = null;
	var columns = [
	              	{header: "承运商公司",dataIndex: 'LOGI_NAME',align:'center'},
	                {header: "发运单单号",dataIndex: 'BILL_NO',align:'center'},
	                {header: "发运经销商",dataIndex: 'DEALER_NAME',align:'center'},
	                {header: "发运经销商电话",dataIndex: 'PHONE',align:'center'},
	                {header: "发运经销商联系人",dataIndex: 'LINK_MAN',align:'center'},
	                {header: "发运地址",dataIndex: 'ADDRESS',align:'center'},
	                //{header: "发运省市",dataIndex: 'SEND_ADD',align:'center'},
	                {header: "运单生成日期",dataIndex: 'BILL_CRT_DATE',align:'center'},
					{header: "操作",sortable: false, dataIndex: 'BILL_ID', align:'center',renderer:myLink}
		      ];
	//超链接设置
    function myLink(value,meta,record){
    	return String.format("<a href='javascript:void(0);' onclick='toDetailCheck(\""+ value +"\")'>[查看]</a>");
	}
	//明细链接
	function toDetailCheck(value){
		//OpenHtmlWindow('<%=contextPath%>/sales/ordermanage/orderreport/SpecialNeedQuery/specialNeedDetailQuery.do?&reqId='+value+'&buttonFalg='+buttonFalg,800,500);
		window.location.href = '<%=contextPath%>/sales/storage/sendmanage/CommprManage/locationMaintainDetailInit.do?billId='+value+'&COMMO=3';
	}
	
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
	function txtClr(txtId){
    	document.getElementById(txtId).value = "";
    }

	//初始化    
	function doInit(){
		__extQuery__(1);
		  //初始化时间控件
		 genLocSel('txt1','','');//支持火狐
	}
	//清空数据
	function txtClr(valueId1,valueId2) {
		document.getElementById(valueId1).value = '' ;
		document.getElementById(valueId2).value = '' ;
	}
</script>
<!--页面列表 end -->
</body>
</html>