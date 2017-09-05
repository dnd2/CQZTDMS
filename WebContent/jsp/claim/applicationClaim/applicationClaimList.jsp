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
<TITLE>索赔单查询</TITLE>

<SCRIPT LANGUAGE="JavaScript">
//设置超链接  begin      
function doInit()
{	
	__extQuery__(1);
	loadcalendar();
}

var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/applicationClaim.json?flag=t";
	
				
	var title = null;

	var columns = [
					{header: "序号", renderer:getIndex,align:'center'},
					{id:'action',header: "选择",sortable: false,dataIndex: 'ID',align:'center',renderer:myLink},
					{header: "状态", align:'center', dataIndex: 'STATUS',renderer:getItemValue},
					{header: "索赔单号", align:'center', dataIndex: 'APP_CLAIM_NO'},
					{header: "工单号", align:'center', dataIndex: 'SERVICE_ORDER_CODE'},
					{header: "工单维修类型",align:'center', dataIndex: 'REPAIR_TYPE',renderer:getItemValue},
					{header: "VIN", align:'center', dataIndex: 'VIN'},
					{header: "车牌号", align:'center', dataIndex:'LICENSE_NO'},
					{header: "车主姓名", align:'center', dataIndex: 'CTM_NAME'},
					{header: "进厂里程", align:'center', dataIndex: 'MILEAGE'},
					{header: "配件申请费用", align:'center', dataIndex: 'PART_APPLY_AMOUNT'},
					{header: "工时申请费用", align:'center', dataIndex: 'HOURS_APPLY_AMOUNT'},
					{header: "申请总费用", align:'center', dataIndex: 'APPLY_TOTAL_AMOUNT'}
		      ];
	//超链接设置
	function myLink(value,meta,record){
		var repair_type=record.data.REPAIR_TYPE;
		var status=record.data.STATUS;
		if(status==19991001 || status==19991003){
			if(repair_type=='11441008'){//PDI
				return String.format("<a href=\"###\" onclick=\"fmFind("+value+"); \">[查看]</a>"+				          
				           "<a href=\"###\" onclick=\"report("+value+"); \">[上报]</a>");
			}else{
				return String.format("<a href=\"###\" onclick=\"fmFind("+value+"); \">[查看]</a>"+
				           "<a href=\"###\" onclick=\"updateInfo("+value+","+repair_type+"); \">[修改]</a>"+
				           "<a href=\"###\" onclick=\"report("+value+"); \">[上报]</a>");
			}			
		}else{
			return String.format("<a href=\"###\" onclick=\"fmFind("+value+"); \">[查看]</a>");
		}
	}
	function updateInfo(value,repair_type){
		var form = document.getElementById("fm");
		form.action ="<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/appUpdate.do?flag=t&repair_type="+repair_type+"&sp_id="+value;
		form.submit();
} 
 function report(value){
	 MyConfirm("确定上报吗？",reportInfo,[value]);
	 
}
 function reportInfo(value){
	 var url='';
		url='<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/appReport.json?id='+value;
		sendAjax(url,callBackreport,'fm');	
}
 function callBackreport(json){
		var msg=json.msg;
		if(msg=="01"){
			MyAlert("已存在相同上报信息！");
		}else if(msg=="00"){
			MyConfirm("上报成功！",Back);
		}	
}
 function Back(){
	 __extQuery__(1);
	}
 function fmFind(value){
	 var form = document.getElementById("fm");
		form.action ='<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/auditApp.do?flag=se&id='+value;
		form.submit();	
} 
	function oemTxt(a,b){
			document.getElementById(a).value="";
			document.getElementById(b).value="";
	}
</SCRIPT>
</HEAD>
<BODY>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;经销商索赔管理&gt;索赔单查询</div>
    <form method="post" name ="fm" id="fm">
    <div class="form-panel">
	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
	<div class="form-body">
    <TABLE class="table_query" border="0">
    	<tr>
            <td style="text-align: right">索赔单号：</td>
            <td><input type="text" name="APP_CLAIM_NO" id="APP_CLAIM_NO" maxlength="20" class="middle_txt"/></td>	    
			<td style="text-align: right">VIN：</td>   
			<td ><input type="text" name="VIN" id="VIN" maxlength="20" class="middle_txt"/></td>
			<td style="text-align: right">创建时间：</td>
		    <td>
      			<input id="creatDate" name="creatDate" readonly="readonly" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="display: inline-block;min-width: 60px;width: 90px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
	     		至
	 			<input id="outPlantDate" name="outPlantDate" readonly="readonly" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="display: inline-block;min-width: 60px;width: 90px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
	 			<input type="button" class="normal_btn" onclick="oemTxt('creatDate','outPlantDate');" value="清 空" id="clrBtn"/> 
    	   </td>      	
        </tr>
        <tr>
           <td style="text-align: right">工单号：</td>
  			<td >
  				<input type="text" name="SERVICE_ORDER_CODE" id="SERVICE_ORDER_CODE" maxlength="18" class="middle_txt"/>
	       	</td>	       		       	
    	   <td style="text-align: right">车牌号：</td>
            <td >
				<input type="text" name="LICENSE_NO" id="LICENSE_NO" maxlength="20" class="middle_txt"/>
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