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
<TITLE>索赔单审核查询</TITLE>

<SCRIPT LANGUAGE="JavaScript">
//设置超链接  begin      
function doInit()
{	
	__extQuery__(1);
	loadcalendar();
}

var myPage;
	//查询路径
	var url = "";
		url = "<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/applicationClaimOne.json?flag=t";
	var num;
	var title = null;
	var idArray = new Array();
	var columns = [
					{header: "序号", renderer:getIndex,align:'center'},
					{id:'action',header: "<input type='checkbox' id = \"selectedAll\" onclick='selectAll(this,\"ids\")'/>选择",sortable: false,dataIndex: 'ID',renderer:myCheckBox},
					{id:'action',header: "操作",sortable: false,dataIndex: 'ID',align:'center',renderer:myLink},
					{header: "状态", align:'center', dataIndex: 'STATUS_NAME'},
					{header: "索赔单号", align:'center', dataIndex: 'APP_CLAIM_NO'},
					{header: "工单号", align:'center', dataIndex: 'SERVICE_ORDER_CODE'},
					{header: "工单维修类型",align:'center', dataIndex: 'REPAIR_TYPE',renderer:getItemValue},
					{header: "提报经销商", align:'center', dataIndex: 'DEALER_NAME'},
					{header: "VIN", align:'center', dataIndex: 'VIN'},
					{header: "车牌号", align:'center', dataIndex:'LICENSE_NO'},
					{header: "车主姓名", align:'center', dataIndex: 'CTM_NAME'},
					{header: "进厂里程", align:'center', dataIndex: 'MILEAGE'}/* ,
					{header: "配件申请费用", align:'center', dataIndex: 'PART_APPLY_AMOUNT'},
					{header: "工时申请费用", align:'center', dataIndex: 'HOURS_APPLY_AMOUNT'},
					{header: "配件申请费用", align:'center', dataIndex: 'PART_APPLY_AMOUNT'},
					{header: "工时申请费用", align:'center', dataIndex: 'HOURS_APPLY_AMOUNT'},
					{header: "申请总费用", align:'center', dataIndex: 'APPLY_TOTAL_AMOUNT'} */
		      ];
	//超链接设置
	function myLink(value,meta,record){
		var status=record.data.STATUS;
		var audit=document.getElementById("audit").value;
		if(audit=="one"){//一级审核
			if(status==19991002){
				return String.format("<a href=\"###\" onclick=\"fmFind("+value+"); \">[查看]</a>"+
				           "<a href=\"###\" onclick=\"auditApp("+value+",'"+audit+"'); \">[审核]</a>");
			}else{
				return String.format("<a href=\"###\" onclick=\"fmFind("+value+"); \">[查看]</a>");
			}
		}else{//二级审核
			if(status==19991004){
				return String.format("<a href=\"###\" onclick=\"fmFind("+value+"); \">[查看]</a>"+
				           "<a href=\"###\" onclick=\"auditApp("+value+",'"+audit+"'); \">[审核]</a>");
			}else{
				return String.format("<a href=\"###\" onclick=\"fmFind("+value+"); \">[查看]</a>");
			}
		}
		
	}
	function myCheckBox(value,metaData,record){
		var status=record.data.STATUS;
		var audit=document.getElementById("audit").value;
		if(audit=="one"){//一级审核
			if(status==19991002){
				return String.format("<input type='checkbox' id='ids' name='ids' value='" + value + "'/>");
			}else{
				return String.format("");
			}
		}else{//二级审核
			if(status==19991004){
				return String.format("<input type='checkbox' id='ids' name='ids' value='" + value + "'/>");
			}else{
				return String.format("");
			}
		}
	}
	function Sedocument(){
		var id=document.getElementsByName("ids");
		if(id.length>0){
			for(var i=0;i<id.length;i++)
			{		
				if(id[i].checked==true){
					idArray.push([id[i].value]);//选中cid的存入数组
					num=1;
				}
			}
		}
	}
	//是否同意
	 function agree(){
		 num=0;
		Sedocument();
		if(num==1){
				MyConfirm("确认同意？",agreeInfo);	
		}else{
				MyAlert("请选择至少一个进行操作！");
		}
	} 
	 function agreeInfo(){
		 idArray = new Array();
		 Sedocument();
		 var audit=document.getElementById("audit").value;
			var url="<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/auditApp.json?flag=t&s=1&idArray="+idArray+"&audit="+audit;
			makeNomalFormCall(url,callBackagree,"fm");
	}
	 function callBackagree(json){
			var msg=json.msg;
			if(msg=="0"){				
				MyConfirm("操作成功！",Back);
			}else{
				MyAlert("操作失败，请重试！");
			}		
	}
	 function auditApp(value,audit){
		 var form = document.getElementById("fm");
			form.action ="<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/auditApp.do?flag=au&audit="+audit+"&id="+value;
			form.submit();	
	} 
 function fmFind(value){
	 var form = document.getElementById("fm");
		form.action ="<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/auditApp.do?flag=se&id="+value;
		form.submit();	
} 
	function oemTxt(a,b){
			document.getElementById(a).value="";
			document.getElementById(b).value="";
	}
	function Back(){
		__extQuery__(1);
	}
</SCRIPT>
</HEAD>
<BODY>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔审核管理&gt;索赔一级审核查询</div>
    <form method="post" name ="fm" id="fm">
    <input type="hidden" name="audit" id="audit" maxlength="20" value="${audit }"/>
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
        	<td style="text-align: right">经销商：</td>
        		<td>
        			<input name="dealerCode" id="dealerCode" type="text" class="middle_txt" onclick="showOrgDealer('dealerCode','dealerId','true','','true','','10771002');" readonly="readonly"/>
           			<input type="hidden" name="dealerId" id="dealerId" value=""/>    
           			<input class="normal_btn" type="button" value="清空" onclick="oemTxt('dealerId', 'dealerCode');"/> 
        		</td>
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
            <input type="button" onClick="agree();" id="save_but" class="u-button u-submit" value="同意" />&nbsp;&nbsp;
			审核意见：<input type="text" name="auditRemark" id="auditRemark" maxlength="100" style="width: 220px;" class="middle_txt"/>&nbsp;
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