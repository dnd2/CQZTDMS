<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
%>
<%@ page import="com.infodms.dms.common.Constant"%>
<!-- 日历类 -->
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/jmstyle/jslib/datepicker/WdatePicker.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/change" prefix="change" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>工单转索赔单查询</TITLE>

<SCRIPT LANGUAGE="JavaScript">
//设置超链接  begin      
function doInit()
{	
	__extQuery__(1);
}

var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/orderClaim.json?flag=t";
	
				
	var title = null;

	var columns = [
					{header: "序号", renderer:getIndex,align:'center'},
					{id:'action',header: "选择",sortable: false,dataIndex: 'SERVICE_ORDER_ID',align:'center',renderer:myLink,},
					{header: "工单号", align:'center', dataIndex: 'SERVICE_ORDER_CODE'},
					{header: "维修类型",align:'center', dataIndex: 'REPAIR_TYPE',renderer:getItemValue},
					{header: "VIN", align:'center', dataIndex: 'VIN'},
					{header: "车牌号", align:'center', dataIndex:'LICENSE_NO'},
					{header: "车主姓名", align:'center', dataIndex: 'CTM_NAME'},
					{header: "进厂里程", align:'center', dataIndex: 'MILEAGE'}
		      ];
	//超链接设置
	function myLink(value,meta,record){
		var repair_type=record.data.REPAIR_TYPE;
		var activity_type=record.data.ACTIVITY_TYPE;
		return String.format("<input type='radio' onclick='onClaim("+value+","+repair_type+","+activity_type+")'/>");
	}
	//跳转新增页面
	function onClaim(value,repair_type,activity_type){
		var form = document.getElementById("fm");
		form.action ="<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/orderClaimAdd.do?flag=t&repair_type="+repair_type+"&activity_type="+activity_type+"&so_id="+value;
		form.submit();
	} 
	function oemTxt(a,b){
			document.getElementById(a).value="";
			document.getElementById(b).value="";
	}
</SCRIPT>
</HEAD>
<BODY>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;经销商索赔管理&gt;工单转索赔单查询</div>
    <form method="post" name ="fm" id="fm">
    <div class="form-panel">
	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
	<div class="form-body">
    <TABLE class="table_query" border="0">
    	<tr>
            <td style="text-align: right">工单号：</td>
            <td><input type="text" name="SERVICE_ORDER_CODE" id="SERVICE_ORDER_CODE" maxlength="20" class="middle_txt"/></td>	    
			<td style="text-align: right">VIN：</td>   
			<td ><input type="text" name="VIN" id="VIN" maxlength="20" class="middle_txt"/></td>
			<td style="text-align: right">车牌号：</td>
            <td >
				<input type="text" name="LICENSE_NO" id="LICENSE_NO" maxlength="20" class="middle_txt"/>
 		    </td>        	
        </tr>
        <tr>
           <td style="text-align: right">维修类型：</td>
           <td >
           		<script type="text/javascript"> 
          			genSelBoxExp("repairType",<%=Constant.REPAIR_TYPE%>,"","true","","","false",'');
        		</script>
	       	</td>
	       	<td style="text-align: right">车主姓名：</td>
  			<td >
  				<input type="text" name="CTM_NAME" id="CTM_NAME" maxlength="10" class="middle_txt"/>
	       	</td>
	       	<td style="text-align: right">创建时间：</td>
		   <td>
      			<input id="creatDate" name="creatDate" readonly="readonly" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="display: inline-block;min-width: 60px;width: 90px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
	     		至
	 			<input id="outPlantDate" name="outPlantDate" readonly="readonly" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="display: inline-block;min-width: 60px;width: 90px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
	 			<input type="button" class="normal_btn" onclick="oemTxt('creatDate','outPlantDate');" value="清 空" id="clrBtn"/> 
    	   </td>
        </tr>                  	    	
    	<tr>
            <td colspan="6" style="text-align: center">
            	<input name="queryBtn" type="button" class="u-button u-query" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> &nbsp; 
				<input type="reset" class="u-button u-cancel" value="重 置"/> &nbsp; 
			</td>
        </tr>
  </table>
  </div>
  </div>
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
</BODY>
</html>