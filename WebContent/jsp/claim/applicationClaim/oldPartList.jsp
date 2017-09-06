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
<TITLE>旧件库存查询</TITLE>

<SCRIPT LANGUAGE="JavaScript">
//设置超链接  begin      
var myPage;
var title = null;
var url = "";
var columns;
	function hzSelect(){//汇总查询
		url="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/hzSelect.json?flag=t";
			__extQuery__(1);
			columns= [
						{header: "序号", renderer:getIndex,align:'center'},
						{header: "责任供应商代码", align:'center', dataIndex: 'PRODUCER_CODE'},
						{header: "责任供应商名称", align:'center', dataIndex:'PRODUCER_NAME'},
						{header: "配件代码", align:'center', dataIndex: 'PART_CODE'},
						{header: "配件名称", align:'center', dataIndex: 'PART_NAME'},
						{header: "库存数", align:'center', dataIndex: 'TOTAL'} 
			      ];
						
	}
		function dtSelect(){//单条查询
			url="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/dtSelect.json?flag=t";
				__extQuery__(1);
				columns= [
							{header: "序号", renderer:getIndex,align:'center'},
							{header: "索赔供应商代码", align:'center', dataIndex: 'CLAIM_SUPPLIER_CODE'},
							{header: "索赔供应商名称",align:'center', dataIndex: 'CLAIM_SUPPLIER_NAME'},
							{id:'action',header: "责任供应商代码", align:'center', dataIndex: 'PRODUCER_CODE',renderer:myLink},
							{header: "责任供应商名称", align:'center', dataIndex:'PRODUCER_NAME'},
							{header: "经销商代码", align:'center', dataIndex: 'DEALER_CODE'},
							{header: "经销商名称", align:'center', dataIndex: 'DEALER_NAME'},
							{id:'action',header: "索赔单号", align:'center', dataIndex: 'CLAIM_NO',renderer:myLinkClaimNO},
							{header: "配件代码", align:'center', dataIndex: 'PART_CODE'},
							{header: "配件名称", align:'center', dataIndex: 'PART_NAME'},
							{header: "是否主因件", align:'center', dataIndex: 'IS_MAIN_CODE',renderer:getItemValue},
							{header: "VIN", align:'center', dataIndex: 'VIN'}
							
				      ];
		}
	//超链接设置
	function myLink(value,meta,record){
		var claim_Id=record.data.CLAIM_ID;
		var part_Id=record.data.PART_ID;
		if(record.data.IS_MAIN_CODE=='10041001'){
			return String.format(""+value+"<input type=\"button\" class=\"mini_btn\" onclick=\"onSupply("+claim_Id+","+part_Id+");\" value=\"...\"/>");
		}else{
			return String.format(value);
		}		
	}
	function myLinkClaimNO(value,meta,record){	
		var claimId=record.data.CLAIM_ID;
		return String.format("<a href=\"###\" onclick=\"fmFind("+claimId+"); \">["+value+"]</a>");
	}

 	function fmFind(value){
	 var form = document.getElementById("fm");
		form.action ='<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/auditApp.do?flag=se&id='+value;
		form.submit();	
	}
	function onSupply(claim_Id,part_Id){	
		OpenHtmlWindow("<%=contextPath%>/jsp/claim/applicationClaim/old_part_supplier_code.jsp?part_Id="+part_Id+"&claimId="+claim_Id,800,500);
	}
	 
	function oemTxt(a,b){
			document.getElementById(a).value="";
			document.getElementById(b).value="";
	}
	function Back(){
		var form = document.getElementById("fm");
		form.action ='<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/oldPartInt.do';
		form.submit();
	}
</SCRIPT>
</HEAD>
<BODY>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;旧件库存查询</div>
    <form method="post" name ="fm" id="fm">
    <div class="form-panel">
	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
	<div class="form-body">
    <TABLE class="table_query" border="0">
    	<tr>
            <td style="text-align: right">选择经销商：</td>
        		<td>
        			<input name="dealerCode" id="dealerCode" type="text" class="middle_txt" onclick="showOrgDealer('dealerCode','dealerId','true','','true','','10771002');" readonly="readonly"/>
           			<input type="hidden" name="dealerId" id="dealerId" value=""/>    
           			<input class="normal_btn" type="button" value="清空" onclick="oemTxt('dealerId', 'dealerCode');"/> 
        		</td>
        	<td style="text-align: right">是否主因件：</td>
            	<td >
           		<script type="text/javascript"> 
          			genSelBoxExp("IS_MAIN_PART",<%=Constant.IF_TYPE%>,"","true","","","false",'');
        		</script>
	       		</td>       	
        </tr>
        <tr>
        	<td style="text-align: right">配件代码：</td>   
			<td ><input type="text" name="partCode" id="partCode" maxlength="20" class="middle_txt"/></td>
			<td style="text-align: right">配件名称：</td>
            <td >
				<input type="text" name="partName" id="partName" maxlength="20" class="middle_txt"/>
 		    </td> 
        </tr>
        <tr>
        	<td style="text-align: right">索赔供应商代码：</td>   
			<td ><input type="text" name="CLAIM_SUPPLIER_CODE" id="CLAIM_SUPPLIER_CODE" maxlength="20" class="middle_txt"/></td>
			<td style="text-align: right">索赔供应商名称：</td>
            <td >
				<input type="text" name="CLAIM_SUPPLIER_NAME" id="CLAIM_SUPPLIER_NAME" maxlength="20" class="middle_txt"/>
 		    </td> 
        </tr>
        <tr>           
	       	<td style="text-align: right">责任供应商代码：</td>
  			<td >
  				<input type="text" name="PRODUCER_CODE" id="PRODUCER_CODE" maxlength="20" class="middle_txt"/>
	       	</td>
	       	<td style="text-align: right">责任供应商名称：</td>
		   <td>
      			<input type="text" name="PRODUCER_NAME" id="PRODUCER_NAME" maxlength="20" class="middle_txt"/>
    	   </td>
        </tr>                  	    	
    	<tr>
            <td colspan="6" style="text-align: center">
            	<input name="queryBtn" type="button" class="u-button u-query" onclick="hzSelect();" value="汇总查 询" id="queryBtn1" /> &nbsp; 
            	<input name="queryBtn" type="button" onclick="__extQuery__(1)" value="汇总导出" id="queryBtn2" /> &nbsp;
            	<input name="queryBtn" type="button" class="u-button u-query" onclick="dtSelect();" value="查 询" id="queryBtn3" /> &nbsp; 
            	<input name="queryBtn" type="button" onclick="__extQuery__(1)" value="导出" id="queryBtn4" /> &nbsp;
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