<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script>
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
<title>索赔预申请状态查询</title>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔预授权&gt;索赔预申请状态查询</div>
  	<form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
	<table class="table_query">
		<tr>
	         <td class="table_query_3Col_label_4Letter">申请日期： </td>
	         <td>
	       		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
	       		&nbsp;至&nbsp;
	       		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
			 </td>
	         <td class="table_query_3Col_label_5Letter">维修工单号：</td>
	         <td><input name="ORDER_ID" id="ORDER_ID" datatype="1,is_null,30"  type="text" class="middle_txt"/></td>
	         <td class="table_query_3Col_label_5Letter">预授权单号：</td>
	         <td><input name="RO_NO" id="RO_NO" datatype="1,is_null,30"  type="text" class="middle_txt"/></td>		         	
		</tr>
		<tr>
	         <td class="table_query_3Col_label_4Letter" align="right">VIN：</td>
<%--	 style="width : 90px"         <td><input type="text" name="VIN" id="VIN" datatype="1,is_digit_letter,30"  class="middle_txt"/> </td>	         		--%>
				<td><textarea name="VIN" cols="18" rows="3" ></textarea></td>		
			<td class="table_query_3Col_label_5Letter">项目代码：</td>
			<td><input type="text" name="ITEMCODE" id="ITEMCODE" datatype="1,is_null,30"  class="middle_txt"/></td>
         	<td class="table_query_3Col_label_5Letter">项目类型：</td>
         	<td>
	         	<script type="text/javascript">
					genSelBoxExp("PRE_AUTH_ITEM",<%=Constant.PRE_AUTH_ITEM%>,"",true,"short_sel","","true",'');
		        </script>
         	</td>         				
		</tr>
		<tr>
         	<td class="table_query_3Col_label_4Letter" align="right">处理状态：</td>
         	<td>
	         	<script type="text/javascript">
					genSelBoxExp("PRECLAIM_AUDIT",<%=Constant.PRECLAIM_AUDIT%>,"",true,"short_sel","","true",'');
		        </script>
         	</td>         				
		</tr>			
	   <tr>
          <td colspan="6"  align="center">
          	<input class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1)"/>
          </td>
      </tr>
  </table>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>    
</form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/preAuthorization/PreclaimPreQueryDealer/preclaimPreQuery.json?COMMAND=1";

	var title = null;
	
	var columns = [
<%--				{header: "序号",sortable: false,dataIndex: 'ID',renderer:getIndex ,align:'center'},--%>
				{header: "VIN",sortable: false,dataIndex: 'VIN',align:'center'},
				{header: "预授权单号",sortable: false,dataIndex: 'FO_NO',align:'center'},
				{header: "维修工单号",sortable: false,dataIndex: 'RO_NO',align:'center'},
				{header: "申请日期",sortable: false,dataIndex: 'APPROVAL_DATE',align:'center',renderer:getDate},
				{header: "项目类型",sortable: false,dataIndex: 'ITEM_TYPE',align:'center',renderer:getItemValue},
				{header: "项目代码",sortable: false,dataIndex: 'ITEM_CODE',align:'center'},
				{header: "项目说明",sortable: false,dataIndex: 'ITEM_DESC',align:'center'},
				{header: "处理状态",sortable: false,dataIndex: 'STATUS',align:'center',renderer:getItemValue},
				{header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
		      ];

//设置超链接  begin      
	
	//操作的超链接设置
	function myLink(value,meta,record){
		    return String.format("<a href=\"#\" onclick='showDetail(\""+value+"\")'>[明细]</a>");			
	}
	//日期格式化：
	function getDate(value,metaDate,record){
		return String.format(value.substring(0,10));
	}
	//详细页面
	function showDetail(value){
		OpenHtmlWindow('<%=contextPath%>/claim/preAuthorization/PreclaimPreQueryDealer/preclaimDetail.do?ID='+value,900,500);
	}	
	
	//具体操作
	
//设置超链接 end
</script>
  </body>
</html>