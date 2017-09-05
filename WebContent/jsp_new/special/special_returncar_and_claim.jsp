<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<head> 
<% 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<% String contextPath = request.getContextPath(); %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/zyw/jquery-1.3.2.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/js/jslib/zyw/jquery-calendar.js"></script> 
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/js/jslib/zyw/jquery-calendar.css" /> 

<title>退换车及善意索赔申请</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}

	
</script>
</head>
<form name="fm" id="fm" method="post">
<body>
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;特殊费用管理&gt;退换车及善意索赔费用申请
</div>
<input class="middle_txt" id="specialNo" value="${specialNo }" type="hidden"  />
<input type="hidden" id="dealerCode" value="${dealerCode }">
<input type="hidden" id="is_claim" value="${t.IS_CLAIM }"/>
<input class="middle_txt" id="type" value="${type }" type="hidden"  />
<input class="middle_txt" id="spe_id" value="${t.SPE_ID }" name="spe_id" type="hidden"  />
<input class="middle_txt" id="special_type" value="1" name="special_type" type="hidden"  />

<!-- 查询条件 end -->
<!-- 查询条件 begin -->
<table width=100% border="0"  cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="10%" nowrap="true" align="right"   >申请单号：</td>
		 <td width="5%"><input  type="text" id="apply_no" name="apply_no" /></td>
         <td width="10%" nowrap="true" align="right">VIN：</td>
		  <td width="5%"><input type="text" id="VIN" name="VIN"/></td>
		   <td width="10%" nowrap="true" align="right">申请类型：</td>
		 <td width="5%" nowrap="true">
	     <select name="spe_type" id="spe_type" >
     	       <option value="">-请选择-</option>
     	       <option value="1">善意索赔</option>
     	       <option value="0">退换车</option>
     	    </select>
	   </td>
	   <td width="10%" nowrap="true"></td>
	</tr>
	<tr>
	<td width="10%" nowrap="true" align="right">申请时间：</td>
	<td width="10%" nowrap="true">
			<div align="left">
            		<input class="short_txt" id="CREATE_DATE_S" name="CREATE_DATE_S" datatype="1,is_date,10"
	           		 maxlength="10" group="CREATE_DATE_S,CREATE_DATE_D"/>
	        	 	<input class="time_ico" value=" " onclick="showcalendar(event, 'CREATE_DATE_S', false);" type="button"/>
	          		 至
		        	 <input class="short_txt" id="CREATE_DATE_D" name="CREATE_DATE_D" datatype="1,is_date,10"
		           	  maxlength="10" group="CREATE_DATE_S,CREATE_DATE_D"/>
		        	 <input class="time_ico" value=" " onclick="showcalendar(event, 'CREATE_DATE_D', false);" type="button"/>
            	</div>		
         </td>
	  
	   <td width="10%" nowrap="true" align="right">
	   </td>
	   <td width="15%" nowrap="true">
	   </td>
	    <td width="10%" nowrap="true" align="right">
	     审核状态：
	   </td>
	    <td width="15%" nowrap="true">
	      <select id="status" name="status">
	          <option value="">-请选择-</option>
	          <option value="20501001">-未上报-</option>
	          <option value="20501002">-审核中-</option>
	          <option value="20501005">-审核通过-</option>
	          <option value="20501006">-审核退回-</option>
	          <option value="20501004">-拒绝-</option>
	       </select>
	   </td>
	   <td width="10%" nowrap="true"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		<input type="button"  name="bntAdd"  id="bntAdd"  value="新增" onclick="add();" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="btnQuery" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    	</td>
    </tr>
</table>
<div style="margin-right: 15px;display: none;">审批总金额：<input type="text" id="money" name="money"/></div>
<!-- 查询条件 end -->
 <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->

<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/SpecialAction/findSpecia.json";
	var title = null;
    var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink,align:'center'},
				{header: "申请单号", dataIndex: 'APPLY_NO', align:'center'},
				{header: "服务商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "状态", dataIndex: 'CODE_DESC', align:'center'},
				{header: "申请日期", dataIndex: 'APPLY_DATE', align:'center'},
				{header: "审核日期", dataIndex: 'AUDIT_DATE', align:'center'},
				{header: "申请类型",  align:'center',renderer:thisLink},
				{header: "申请金额",  align:'center',dataIndex: 'APPLY_AMOUNT'},
				{header: "审批金额", dataIndex: 'APPROVAL_AMOUNT', align:'center'}
	      ];
	function thisLink(value,meta,record){
	      var SPECIAL_TYPE=record.data.SPECIAL_TYPE;
	      if("1"==SPECIAL_TYPE){
	           var type = "善意索赔";
	         return type;
	      }
	       if("0"==SPECIAL_TYPE){
	           var type = "退换车";
	         return type;
	      }
	}
	function myLink(value,meta,record){ //,renderer:getItemValue
		var status=record.data.STATUS;
		var special_type=record.data.SPECIAL_TYPE;
		var ID=record.data.ID;
		var url="";
		if("20501001"==status){
			url+="<a href='#' onclick='del(\""+ID+"\");'>[删除]</a>";
			var urlUpdate='<%=contextPath%>/SpecialAction/viewSpecialApplyDetailed.do?id='+value+"&type=update&special_type="+special_type;
			url+="<a href='"+urlUpdate+"');'>[修改]</a>";
			url+="<a href='#' onclick='Report(\""+ID+"\");'>[提报]</a>";
		}
		if("20501006"==status){
			var urlUpdate='<%=contextPath%>/SpecialAction/viewSpecialApplyDetailed.do?id='+value+"&type=update";
			url+="<a href='"+urlUpdate+"');'>[修改]</a>";
			url+="<a href='#' onclick='Report(\""+ID+"\");'>[提报]</a>";
		}
		if("20501002"==status){
			url+="<a href='#' onclick='Revoke(\""+ID+"\");'>[撤销]</a>";
		}
		if(20331004==status||20331006==status||20331008==status||20331010==status||20331012==status){
			var urlUpdate='<%=contextPath%>/SpecialAction/updateSpeInit.do?id='+value+'&special_type='+special_type;
			url+="<a href='"+urlUpdate+"');'>[修改]</a>";
		}
		var urlView='<%=contextPath%>/SpecialAction/viewSpecialApplyDetailed.do?id='+value+'&special_type='+special_type;
		url+="<a href='"+urlView+"');'>[明细]</a>";
		return String.format(url);
	}
	function del(ID){
		var urlDel='<%=contextPath%>/SpecialAction/delSpecialapply.json?&id='+ID;
		sendAjax(urlDel,function(json){
			if(json.succ=="1"){
				MyAlert("提示：删除成功！");
				__extQuery__(1);
			}else{
				MyAlert("提示：删除失败！");
			}
		},'fm');
	}
	//提报
	function  Report(ID){
	   urlReport='<%=contextPath%>/SpecialAction/SpecialApplyReport.json?id='+ID+"&type=Report";
	   sendAjax(urlReport,function(json){
			if(json.succ=="1"){
				MyAlert("提示：操作成功！");
				__extQuery__(1);
			}else{
				MyAlert("提示：操作失败！");
			}
		},'fm');
	}
	//撤销
	function Revoke(ID){
	   urlReport='<%=contextPath%>/SpecialAction/SpecialApplyReport.json?id='+ID+"&type=Revoke";
	   sendAjax(urlReport,function(json){
			if(json.succ=="1"){
				MyAlert("提示：操作成功！");
				__extQuery__(1);
			}else{
				MyAlert("提示：操作失败！");
			}
		},'fm');
	}
	//新增
	function add(){
	  OpenHtmlWindow('<%=contextPath%>/jsp_new/special/special_add_init.jsp',400,200);
	}
	function chooseType(spe_type){
  	  window.location.href='<%=contextPath%>/SpecialAction/addspecialgoodwill.do?spe_type='+spe_type;
    }
//设置超链接 end
	
</script>
<!--页面列表 end -->

</body>
</form>
</html>