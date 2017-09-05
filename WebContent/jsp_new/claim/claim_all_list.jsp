<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔单</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/ClaimBalanceAction/queryapplicationlist.json";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{id:'action',header: "选择",sortable: false,dataIndex: 'ID',renderer:mySelect,align:'center'},
				{header: "索赔单号", dataIndex: 'CLAIM_NO', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "索赔类型", dataIndex: 'CLAIM_TYPE', align:'center',renderer:getItemValue}
	      ];
		function mySelect(value,metaDate,record){
			return String.format("<input type='checkbox'  name='check' value='"+record.data.ID+",' />");
		}
		function bnt_Sure(){
			var checkids = document.getElementsByName('check');
		    var temp=0;
		    for(var i=0;i<checkids.length;i++){
			  if(checkids[i].checked){
				  temp++;
			  }
		    }
		    if(temp==0){
			   MyAlert("提示：请至少选择一个索赔单！");
			   return;
		    }
		    var str="";
			for(var i=0;i<checkids.length;i++){
				if(checkids[i].checked){
				   	str+=checkids[i].value;
				}
			}
			var strVal=document.getElementById("str");
			strVal.value="";
			strVal.value=str;
		   	var url='<%=contextPath%>/MainTainAction/insertoldoutpart.json';
		   	makeNomalFormCall(url,back,"fm");
		}
		function back(json){
			if(json.succ=="1"){
				MyAlert("提示：增加成功！");
				//parentContainer.setMainPartCode();
				parent._hide();
			}else{
				MyAlert("提示：增加失败！");
			}
		}
		
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔基础数据&gt;索赔单查询
</div>
<form name="fm" id="fm">
<input type="hidden" name="str" id="str"/>
<input type="hidden" name="part_type" id="part_type"/>
<input type="hidden" id="range_no" name="range_no" value="${range_no }">
<input type="hidden" id="out_time" name="out_time" value="${out_time }">
<input type="hidden" id="supply_code" name="supply_code" value="${supply_code}">
<input type="hidden" id="out_by" name="out_by" value="${out_by}">
<input type="hidden" id="supply_name" name="supply_name" value="${supply_name}">
<input type="hidden" id="out_type" name="out_type" value="${out_type}">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">索赔单号：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="claim_no"  name="claim_no" maxlength="30" type="text"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">VIN：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="vin"  name="vin" maxlength="30" type="text"/>
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