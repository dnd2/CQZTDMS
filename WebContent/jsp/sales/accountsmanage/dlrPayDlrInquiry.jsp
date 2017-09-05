<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.bean.AclUserBean"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		__extQuery__(1);
	}

	var myPage;
	var url = "<%=contextPath%>/sales/accountsmanage/DlrPayInquiry/dlrPayInquiryInitQuery.json";
	var title = null;
	
	var columns = [
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "票号", dataIndex: 'TICKET_NO', align:'center'},
				{header: "往来单位名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "账户类型", dataIndex: 'TYPE_NAME', align:'center'},
				{header: "金额", dataIndex: 'PAY_SUM', align:'center'},
				{header: "付款日期", dataIndex: 'PAY_DATE', align:'center'},
				{id:'action',header: "操作", walign:'center',idth:70,dataIndex: 'TICKET_ID',renderer:myLink}
		      ];
		      
	function myLink(value,meta,record){ 
        	 return String.format(
        		 "<a href=\"#\" onclick='queryDlrPayInfo(\""+ value +"\")'>[查看]</a>");
    }
    
    //开票日期内容清空
	function clrTxt(valueId) {
		document.getElementById(valueId).value = '' ;
	}
    //查看信息
    function queryDlrPayInfo(value){
    	location = '<%=contextPath%>/sales/accountsmanage/DlrPayInquiry/dlrPayInfoDlrInquiry.do?TICKET_ID='+value;
    }
	//查询条件重置
	function requery() {		
		document.getElementById('ticketNo').value='';
		document.getElementById('payDate').value='';
		document.getElementById('dealerCode').value='';
		changeSelected();
	}
	
	 function changeSelected(){  
	        jsSelectItemByValue(document.getElementById("accountsType"),"");  
	    }  
	       
	    function jsSelectItemByValue(objSelect,objItemText) {  
	        for(var i=0;i<objSelect.options.length;i++) {  
	            if(objSelect.options[i].value == objItemText) {  
	                objSelect.options[i].selected = true;  
	                break;  
	            }  
	        }  
	    } 
	    
	function payAdd(){
		 window.self.location = "<%=contextPath%>/sales/accountsmanage/DlrPayInquiry/dlrPayAdd.do";
	}
</script>   

</head>
<body onunload="javascript:destoryPrototype();">
<div id="loader" style="position: absolute; z-index: 200; background-color: rgb(255, 204, 0); padding: 1px; top: 4px; left: 455px; display: none; background-position: initial initial; background-repeat: initial initial; "> 正在载入中... </div>

<title>经销商付款录入</title>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif">&nbsp;当前位置：  财务管理 &gt; 账务收款 &gt; 经销商付款录入</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1"/>
		<input type="hidden" name="dutyType" id="dutyType" value="${dutyType}"/>
		<div class="form-panel">
			<h2>经销商付款录入</h2>
			<div class="form-body">	
				<table class="table_query" border="0">
					<tbody>
					<tr>
						<td width="12%" class="tblopt right">票号：</td>
						<td class="left" width="10%">
							<input type="text" class="middle_txt" id="ticketNo" name="ticketNo" value=""/>
						</td>
						<td class="right">选选账户类型：</td>
				       <td>
					      <select name="accountsType" id="accountsType" class='u-select'>
						       <c:forEach items="${accountsTypeList}" var="accountsTypeList" >
						       		<option value="${accountsTypeList.TYPE_ID }">${accountsTypeList.TYPE_NAME}</option>
							   </c:forEach>
					      </select>
				   	   </td>
						
						<td class="tblopt right">付款日期：</td>
						<td class="left">
						
						<input name="payDate"  id="payDate" value="${date }" type="text" class="middle_txt" style="width:100px" onFocus="WdatePicker({el:$dp.$('payDate'), maxDate:'#F{$dp.$D(\'payDate1\')}'})" />
		      			<input  id="payDate1"  type="hidden"   />
		    			</td>
					</tr>
					<tr>
						<td class="center" colspan="6">
							<input type="hidden" name=invoiveId id="invoiveId"/>
							<input type="button" class=" u-button u-query" onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" style="border: 1px solid rgb(94, 118, 146); background-color: rgb(238, 240, 252); color: rgb(30, 57, 136); background-position: initial initial; background-repeat: initial initial; "/> 
							<input type="button" class=" u-button u-reset"  value="重 置" onclick="requery();"/>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	 </div>
	 <!-- 查询条件 end -->
 	 <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end --> 
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
	
</div>
 




<div id="checkMsgDiv0" style="visibility: hidden; " class="tipdiv"> 
 <table width="120" height="28" border="0" cellpadding="0" cellspacing="0" id="checkMsgTable">    
  <tbody>
	 <tr>       
	 	<td valign="bottom"><img src="<%=contextPath%>/js/validate/alert_top.gif" width="120" height="6"></td>    
 	</tr>    
 	<tr>       
 		<td valign="top">          
 			<table width="120" border="0" cellpadding="0" cellspacing="0" id="checkMsgTable" style="font:9pt 宋体;">           
				<tbody>
					<tr>                 
						<td width="136" valign="top" id="checkMsgDiv0_msg" background="<%=contextPath%>/js/validate/alert_middle.gif" align="center" style="font:9pt 宋体;">test </td>
			         </tr>          
			     </tbody>
	        </table>      
	    </td>    
	</tr>    
	<tr>     
		<td height="10" valign="top">
			<img src="<%=contextPath%>/js/validate/alert_bottom.gif" width="120"/>
		</td>    
	</tr>  
  </tbody>
</table>
</div>
<div>
	<embed id="lingoes_plugin_object" type="application/lingoes-npruntime-capture-word-plugin" hidden="true" width="0" height="0">
</div>
</body>
</html>
