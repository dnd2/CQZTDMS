<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
List list =(List)request.getAttribute("list_logi");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 车辆出库 </title>
</head>

<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理> 车辆出库
	</div>
<form name="fm" method="post" id="fm">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
 <tr class="csstr" align="center">	
 	<td class="right">产地：</td> 
	  <td align="left">
		 <select name="YIELDLY" id="YIELDLY" class="selectlist" >
		 <option value="">--请选择--</option>
				<c:if test="${list!=null}">
					<c:forEach items="${list}" var="list">
						<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
		</td> 
   <td class="right">组板日期：</td>
		<td align="left" nowrap="true">
			<input name="BO_STARTDATE" type="text" maxlength="20"  class="middle_txt" id="BO_STARTDATE" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'BO_STARTDATE', false);" />  	
             &nbsp;至&nbsp;
             <input name="BO_ENDDATE" type="text" maxlength="20"  class="middle_txt" id="BO_ENDDATE" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'BO_ENDDATE', false);" /> 
		</td>
		<td class="right">组板号：</td> 
	  <td align="left">
		  <input type="text" maxlength="20"  id=BO_NO name="BO_NO" datatype="1,is_digit_letter,30" maxlength="30" class="middle_txt" size="15" />
	  </td>	 
</tr>

  <tr align="center">
  <td colspan="6" class="table_query_4Col_input" style="text-align: center">
  		  <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="__extQuery__(1);" />   	 	
    </td>
  </tr>
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
<table border="0" class="table_query" id="tab_remark" style="display: none">
      <tr align="left">
        <td>备注：
          <textarea name="REMARK" id="REMARK" cols="50" rows="2" class="tb_list"></textarea>&nbsp;&nbsp;
          <input class="normal_btn" type="button" value="确定出库" onclick="retreat();"/></td>
      </tr>
  </table>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/sales/storage/sendmanage/RemovalStorage/boardStorage.json";
	var title = null;
	var columns = [
					{header: "序号",align:'center',renderer:getIndex},
					//{header: "批售单号",dataIndex: 'ORDER_NO',align:'center'},
					{header: "组板号",dataIndex: 'BO_NO',align:'center'},
					//{header: "发票号",dataIndex: 'INVOICE_NO', align:'center'},
					//{header: "经销商名称",dataIndex: 'DEALER_SHORTNAME',align:'center'},
					{header: "组板完成时间",dataIndex: 'BO_DATE',align:'center'},
					{header: "组板数量",dataIndex: 'BO_NUM',align:'center'},
					{header: "配车数量",dataIndex: 'ALLOCA_NUM',align:'center'},
					{header: "出库数量",dataIndex: 'OUT_NUM',align:'center'},
					{header: "组板状态",dataIndex: 'HANDLE_STATUS',align:'center', renderer: getItemValue},
					{
						header: "操作",dataIndex: '',
						align:'center',
						renderer:function(value,metaDate,record) {
							return String.format("<a href='javascript:;' onclick=\"openOutStorage('"+record.data.BO_ID+"')\">[车辆出库]</a>");
						}
					}
			      ];

	function openOutStorage(boId) {
		OpenHtmlWindow('<%=contextPath%>/sales/storage/sendmanage/RemovalStorage/boardStorageQueryInit.do?boId=' + boId,930,430);
	}
	function test() {
		MyAlert('test');
	}
	//初始化    
	function doInit(){
		//日期控件初始化
		//__extQuery__(1);
	}
	//处理隐藏备注框
	function customerFunc(){
		var arrayObj = new Array(); 
		arrayObj=document.getElementsByName("groupIds");
		if(arrayObj.length>0){//大于0表表示有数据，备注显示
			document.getElementById("tab_remark").style.display="";
		}else{
			document.getElementById("tab_remark").style.display="none";
		}
	}
	//清空数据
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
    function toChangeMaterial(parm){
		if(parm==1){
			document.getElementById("button1").disabled="";
			document.getElementById("button2").disabled="disabled";
		}else{
			document.getElementById("button1").disabled="disabled";
			document.getElementById("button2").disabled="";
		}
    }

</script>
</body>
</html>
