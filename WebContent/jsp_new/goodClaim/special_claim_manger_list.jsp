<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>质保手册</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>


<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/GoodClaimAction/specialDealerList.json?query=true";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink,align:'center'},
				{header: "申请单号", dataIndex: 'APPLY_NO', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "费用类型", dataIndex: 'SPECIAL_TYPE', align:'center',renderer:getType},
				{header: "申请金额", dataIndex: 'APPLY_MONEY', align:'center'},
				{header: "状态", dataIndex: 'STSTUS', align:'center'}
	      ];
	
	function getType(value,meta,record){
		var type="";
		if(1==value){
			type+="善意索赔";
		}
		if(0==value){
			type+="退换车";
		}
		return String.format(type);
	}
	function myLink(value,meta,record){
		var status=record.data.STATUS;
		var url="";
		if(20331001==status||20331004==status||20331006==status||20331008==status||20331010==status||20331012==status){
			url+="<a href='#' onclick='del(\""+value+"\");'>[删除]</a>";
			var urlUpdate='<%=contextPath%>/GoodClaimAction/updateWarrantyManual.do?id='+value;
			url+="<a href='"+urlUpdate+"');'>[修改]</a>";
		}
		var urlView='<%=contextPath%>/GoodClaimAction/viewWarrantyManual.do?id='+value;
		url+="<a href='"+urlView+"');'>[明细]</a>";
		return String.format(url);
	}
	function del(id){
		var urlDel='<%=contextPath%>/GoodClaimAction/delWarrantyManual.json?&id='+id;
		sendAjax(urlDel,function(json){
			if(json.succ=="1"){
				MyAlert("提示：删除成功！");
				__extQuery__(1);
			}else{
				MyAlert("提示：删除失败！");
			}
		},'fm');
	}
	function add(){
		OpenHtmlWindow('<%=contextPath%>/jsp_new/special/special_add_init.jsp',400,200);
	}
	function chooseType(spe_type){
  	  window.location.href='<%=contextPath%>/GoodClaimAction/addspecialDealerInit.do?spe_type='+spe_type;
    }
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;特殊费用管理&gt;特殊费用服务站查询
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">服务站简称：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="dealer_shortname"  name="dealer_shortname" maxlength="30" type="text"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">服务站代码：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="dealer_code"  name="dealer_code" maxlength="30" type="text"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">服务站联系人</td>
		<td width="15%" nowrap="true">
			<input class="middle_txt" id="dealer_contact_person"  name="dealer_contact_person" maxlength="30" type="text"/>
		</td>
		<td width="12.5%"></td>
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
<!-- 查询条件 end -->

<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
</html>