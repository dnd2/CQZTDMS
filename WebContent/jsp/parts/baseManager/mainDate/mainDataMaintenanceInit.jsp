<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件赠品查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
function doInit(){
	loadcalendar();  //初始化时间控件
//	getDate();
	__extQuery__(1);
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件基础信息管理&gt; 配件基础信息维护 &gt;配件赠送设置查询</div>
<form method="post" name="fm" id="fm">
<input type="hidden" name="giftTypeM" id="giftTypeM" value="">
<!-- 查询条件 begin -->
	<table class="table_query" >

		<tr>
		  <td width="10%" align="right">赠品描述：</td>
   		  <td width="20%" align="left">
		    <input type="text" id="GIFT_TYPE_SEARCH" name="GIFT_TYPE_SEARCH" class="middle_txt" />
	   	  </td> 
	  	  <td width="10%" align="right">赠送方式：</td> 
	  	  <td width="20%" align="left">
		    <script type="text/javascript">
		   	 genSelBoxExp("giftWay",<%=Constant.PART_GIFT_WAY%>,"",true,"short_sel","","false",'');
			</script>
	  	  </td>  
	  	  <td width="10%" align="right">是否本部发起：</td> 
		  <td width="20%" align="left">
			<script type="text/javascript">
		   	 genSelBoxExp("isOemStart",<%=Constant.IF_TYPE%>,"",true,"short_sel","","false",'');
			</script>
		  </td> 
		</tr>
		<tr>
         <td width="10%" align="right" >开始日期：</td>
	     <td width="20%" >
	       <input id="checkSDate" class="middle_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" readonly="readonly" />
	       <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
	     </td>
	     <td width="10%" align="right" >结束日期：</td>
	     <td width="20%" >
	        <input id="checkEDate" class="middle_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" readonly="readonly" />
	        <input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
	     </td>
	     <td width="10%" align="right"></td>
		 <td width="20%" ></td>
	    </tr>
		<tr align="center">
	
			<td colspan="6" align="center">
				<input type="button" name="BtnQuery" id="queryBtn" class="cssbutton" onClick="__extQuery__(1);" value="查 询">
    	        <input type="button" id="queryBtn1" class="cssbutton"  value="新 增" onclick="addReservoir();" /> 
			</td>
		</tr>
	</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>

<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url = "<%=request.getContextPath()%>/parts/baseManager/mainData/mainDataMaintenance/aMainDataMaintenanceQuery.json";
	var title = null;
	var columns = [
				{header: "序号", dataIndex: '', renderer:getIndex,align:'center'},
                {header: "操作",sortable: false, dataIndex: 'GIFT_TYPE', align:'center',renderer:myLink},
				{header: "赠品描述",dataIndex: 'GIFT_TYPE', style: 'text-align:left'},
				{header: "赠送方式",dataIndex: 'GIFT_WAY',renderer:getItemValue},
				{header: "开始日期",dataIndex: 'START_DATE'},
				{header: "结束日期", dataIndex: 'END_DATE'},
				{header: "是否本部发起", dataIndex: 'IS_OEM_START',renderer:getItemValue}

		      ]; 


	function myLink(value,meta,record){
        return String.format("<a href='#' onclick='goModPage(\""+ value +"\")'>[维护]</a>");

	}

	function goModPage(giftType)
	{
		document.getElementById("giftTypeM").value = giftType;
		btnDisable();
		fm.action = "<%=contextPath%>/parts/baseManager/mainData/mainDataMaintenance/updateMainDataMaintenance.do";
		fm.submit();
	}

	function addReservoir(){
		btnDisable();
		fm.action = "<%=contextPath%>/parts/baseManager/mainData/mainDataMaintenance/addMainDataMaintenance.do";
		fm.submit();
	}

	//失效按钮
	function btnDisable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });

	}

	//有效按钮
	function btnEnable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = "";
	    });

	}
</script>
<!--页面列表 end -->
</body>
</html>