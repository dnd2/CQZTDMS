<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>工单</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/ClaimAction/addPartNormal.json?query=true";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\"  onclick='selectAll(this,\"check\")' />全选", align:'center',sortable:false, dataIndex:'REAL_PART_ID',width:'2%',renderer:checkBoxShow},
				{header: "配件代码", dataIndex: 'PART_NO', align:'center'},
				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "配件数量", dataIndex: 'PART_QUANTITY', align:'center'},
				{header: "配件单价", dataIndex: 'PART_COST_PRICE', align:'center'},
				{header: "配件金额", dataIndex: 'PART_COST_AMOUNT', align:'center'},
				{header: "是否回运", dataIndex: 'IS_RETURN', align:'center',renderer:getItemValue}
	      ];
		function checkBoxShow(value,meta,record){
			return String.format("<input type='checkbox'  name='check' value='"+record.data.REAL_PART_ID+";"+record.data.PART_NO+";"+record.data.PART_NAME+";"+record.data.PART_QUANTITY+";"+record.data.PART_COST_PRICE+";"+record.data.PART_COST_AMOUNT+";"+record.data.PART_USE_TYPE+";"+record.data.IS_RETURN+"' />");
		}
		function setMainPartCode(v1,v2,v3,v4,v5,v6,v7,v8){
			 //调用父页面方法
			if(v1==null||v1=="null"){
			 	v1 = "";
			 }
			 if(v2==null||v2=="null"){
			 	v2 = "";
			 }
			 if(v3==null||v3=="null"){
			 	v3 = "";
			 }
			 if(v4=="null"||v4==null){
			 	v4 = "";
			 }
			 if(v5=="null"||v5==null){
				v5 = "";
			 }
			 if(v6=="null"||v6==null){
				v6 = "";
			 }
			 if(v7=="null"||v7==null){
				v7 = "";
			}
			 if(v8=="null"||v8==null){
				v8 = "";
			}
	 		if (parent.$('inIframe')) {
	 			parentContainer.setMainPartCode(v1,v2,v3,v4,v5,v6,v7,v8);
	 		} else {
				parent.setMainPartCode(v1,v2,v3,v4,v5,v6,v7,v8);
			}
		};
		function bnt_Sure(){
			var checkids = document.getElementsByName('check');
		    var temp=0;
		    for(var i=0;i<checkids.length;i++){
			  if(checkids[i].checked){
				  temp++;
			  }
		    }
		    if(temp==0){
			   MyAlert("提示：请至少选择一个工单里的配件！");
			   return;
		    }
			for(var i=0;i<checkids.length;i++){
				if(checkids[i].checked){
			   	var arr=checkids[i].value.split(";");
				setMainPartCode(arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],arr[6],arr[7]);
				}
			}
			parent._hide(); 
		};
</script>
<!--页面列表 end -->
</head>
<body onload="__extQuery__(1);">
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;经销商索赔单管理&gt;工单配件列表查询页面
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<input class="middle_txt" id="ro_no" value="${ro_no }" name="ro_no" type="hidden"  />
<input class="middle_txt" id="part_id" value="${part_id }" name="part_id" type="hidden"  />
<input class="middle_txt" id="claim_type" value="${claim_type }" name="claim_type" type="hidden"  />
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">配件代码：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="part_code"  name="part_code" maxlength="30" type="text"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">配件名称：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="part_name"  name="part_name" maxlength="30" type="text"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;
    		<input type="button"  name="bntSure" id="bntSure"  value="确定" class="normal_btn"  onclick="bnt_Sure();"/>
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntClose" id="bntClose" value="关闭"  onclick="_hide();" class="normal_btn" />
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