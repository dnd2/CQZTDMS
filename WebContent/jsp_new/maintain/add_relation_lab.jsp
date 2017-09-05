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

<script type="text/javascript"  >

var myPage;
//查询路径
var url = "<%=contextPath%>/MainTainAction/findaddLabour.json";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\"  onclick='selectAll(this,\"check\")' />全选", align:'center',sortable:false, dataIndex:'LABOUR_CODE',width:'2%',renderer:checkBoxShow},
				{header: "工时代码", dataIndex: 'LABOUR_CODE', align:'center'},
				{header: "工时名称", dataIndex: 'CN_DES', align:'center'},
				{header: "工时数", dataIndex: 'LABOUR_HOUR', align:'center'}
	      ];
		function checkBoxShow(value,meta,record){
			return String.format("<input type='checkbox'  name='check' value='"+record.data.LABOUR_CODE+","+record.data.CN_DES+";' />");
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
			   MyAlert("提示：请至少选择一个工时！");
			   return;
		    }
		    var str="";
			for(var i=0;i<checkids.length;i++){
				if(checkids[i].checked){
				   	str+=checkids[i].value;
				}
			}
			var part_code=$("part_code").value;
			var part_id=$("part_id").value;
		   	var url='<%=contextPath%>/MainTainAction/insertRalation.json?str='+str+'&part_id='+part_id+'&part_code='+part_code;
		   	makeNomalFormCall(url,back,"fm");
		}
		function back(json){
			if(json.succ=="1"){
				MyAlert("提示：关系插入成功！");
				parentContainer.setLabourCode();
				parent._hide();
			}else{
				MyAlert("提示：关系插入失败！");
			}
		}
</script>
<!--页面列表 end -->
</head>
<body onload="__extQuery__(1);">
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔基础数据&gt;维修工时查询
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<input type="hidden" id="part_id" name="part_id" value="${part_id }"/>
<input type="hidden" id="part_code" name="part_code" value="${part_code }"/>
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">工时代码：</td>
      	<td width="15%" nowrap="true">
            <input name="labour_code" id="labour_code" type="text" class="middle_txt" maxlength="30" />
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">工时名称：</td>
      	<td width="15%" nowrap="true">
            <input name="cn_des" id="cn_des" type="text" class="middle_txt" maxlength="30" />
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