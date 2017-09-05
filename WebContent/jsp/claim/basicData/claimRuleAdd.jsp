<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>三包规则维护</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
//日历控件初始化
	function doInit()
		{
		   loadcalendar();
		}
//新增三包规则 开始
	function checkedAdd(){
		if(!submitForm('fm')) {//表单校验
					return false;
			    }
		MyConfirm("是否确认添加？",claimRuleAdd);
	}
	function claimRuleAdd(){
		fm.action = "<%=contextPath%>/claim/basicData/ClaimRule/claimRuleAdd.do";
		fm.submit();
	}
//新增三包规则 结束
//返回主查询页面 开始
function goBack(){
		fm.action = "<%=contextPath%>/claim/basicData/ClaimRule/claimRuleQuery.do";
		fm.submit();
}
//返回主查询页面 结束
</script>
</head>

<body>
<div class="wbox">
  <div class="navigation">
        <img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;三包规则维护
  </div>
  <form method="post" name="fm" id="fm">
  <div class="form-panel">
		<h2>基础信息</h2>
			<div class="form-body">
     <table class="table_query" >
          <tr>
            <td class="table_edit_2Col_label_7Letter" style="text-align:right">三包规则代码：</td>
            <td align="left">
                <input type="text"  name="ruleCode"  id="ruleCode" class="middle_txt" datatype="0,is_null,20"/>
            </td>
           </tr>
           <tr>
           		<td class="table_edit_2Col_label_7Letter" style="text-align:right">三包规则名称：</td>
            	<td align="left">
                <input type="text"  name="ruleName"  id="ruleName" class="middle_txt" datatype="0,is_null,100"/>
            	</td>
           </tr>
           <tr>
            <td class="table_edit_2Col_label_7Letter" style="text-align:right">三包规则类型：</td>
            <td align="left">
                <input type="hidden" id="ruleType" name="ruleType" value="<%=Constant.RULE_TYPE_02%>"/> 
                <script type='text/javascript'>
				       var ruleType=getItemValue('<%=Constant.RULE_TYPE_02%>');
				       document.write(ruleType) ;
				</script>
            </td>
          </tr>  
          <tr>
          		<td class="table_edit_2Col_label_7Letter" style="text-align:right">三包规则状态：</td>
            	<td align="left">
	              	<script type="text/javascript">
	   					    	genSelBoxExp("ruleStatus",<%=Constant.STATUS%>,"",false,"","","false",'');
	  		      	</script>
            	</td>
          </tr>
          <tr>
            <td colspan="2" style="text-align:center">
		      <input type="button" name="bt_add" class="normal_btn" onclick="checkedAdd();" value="新增"/>
		      <input type="button" name="bt_back" class="normal_btn" onclick="goBack();" value="返回"/>
  </table>
  </div>
  </div>
  </form>
  </div>
</body>
</html>