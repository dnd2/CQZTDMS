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
var url = "<%=contextPath%>/MainTainAction/addYsqPart.json?query=true";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{id:'action',header: "选择",sortable: false,dataIndex: 'PART_ID',renderer:mySelect,align:'center'},
				{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "配件单价", dataIndex: 'CLAIM_PRICE_PARAM', align:'center'}
	      ];
		function mySelect(value,metaDate,record){
			return String.format("<input type='checkbox'  name='check' value='"+record.data.PART_ID+";"+record.data.PART_CODE+";"+record.data.PART_NAME+":' />");
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
			   MyAlert("提示：请至少选择一个配件！");
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
		   	var url='<%=contextPath%>/MainTainAction/insertYsqPart.json';
		   	makeNomalFormCall(url,back,"fm");
		}
		function back(json){
			if(json.succ=="1"){
				MyAlert("提示：增加成功！");
				parentContainer.setMainPartCode();
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
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔基础数据&gt;维修配件查询
</div>
<form name="fm" id="fm">
<input type="hidden" name="str" id="str"/>
<input type="hidden" name="part_type" id="part_type"/>
<!-- 查询条件 begin -->
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
    	<input type="radio" name="radio" id="Wearpart" onclick="radiocheck(this)" value="72311001" checked="checked" />易损件 
    	<input type="radio" name="radio" id="Remainpart" onclick="radiocheck(this)" value="72311002" />留存件 
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