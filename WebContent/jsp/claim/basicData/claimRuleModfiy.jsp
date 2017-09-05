<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.po.TtAsWrRulePO" %>
<%@page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
TtAsWrRulePO RulePO =(TtAsWrRulePO)request.getAttribute("RulePO");
%>
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
	//修改三包规则
	function subChecked() {
	        if(!submitForm('fm')) {//表单校验
				return false;
		    }
	         var id=document.getElementById("ruleId").value;
		     MyConfirm("是否确认保存？",claimRuleUpdate,[id]);
		} 
	//修改开始
	function claimRuleUpdate(id){
	   makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimRule/claimRuleUpdate.json?id='+id,returnBack,'fm','queryBtn');
	}
	//修改回调函数
	function returnBack(json){
		var del = json.returnValue;
		if(del==1){
			MyAlert("保存成功！");
		}else{
			MyAlert("保存失败！请联系管理员！");
		}
	}
	//修改结束
	//修改明细
function updateDetails(){
	var ruleId=document.getElementById("ruleId").value;
	OpenHtmlWindow("<%=contextPath%>/claim/basicData/ClaimRule/claimRuleUpdateQuery.do?ruleId="+ruleId,800,500);
}
//返回主查询页面
function goBack(){
		fm.action = "<%=contextPath%>/claim/basicData/ClaimRule/claimRuleQuery.do";
		fm.submit();
}
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
    <input type="hidden" id="ruleId" name="id" value="<%=RulePO.getId() %>"/>
    <table class="table_query" >
          <tr>
              <td class="table_edit_2Col_label_7Letter" style="text-align:right">三包规则代码：</td>
              <td align="left">
                <span class="zi"><%=RulePO.getRuleCode() %></span>
              </td>
          </tr>
          <tr>
          <td class="table_edit_2Col_label_7Letter"  style="text-align:right">三包规则名称：</td>
              <td align="left">
               <input type="text"  name="ruleName" id="ruleName" class="middle_txt" value="<%=RulePO.getRuleName() %>"  datatype="0,is_null,100"/>
              </td>
          </tr>
           <tr>
           	   <td class="table_edit_2Col_label_7Letter" style="text-align:right">三包规则类型：</td>
               <td align="left">
                  <script type='text/javascript'>
				       var ruleType=getItemValue('<%=RulePO.getRuleType() %>');
				       document.write(ruleType) ;
				  </script>
               </td>
          </tr>  
          <tr>
          <td class="table_edit_2Col_label_7Letter" style="text-align:right">三包规则状态：</td>
               <td align="left">
                <script type="text/javascript">
	   					    genSelBoxExp("ruleStatus",<%=Constant.STATUS%>,"<%=RulePO.getRuleStatus() %>",false,"","","false",'');
	  		    </script>
            </td>
          </tr>
          <tr>
	    <td colspan="2" style="text-align:center">
	       <input type="button" name="button3" value="保存" class="normal_btn" onclick="subChecked();" />
	       <input type="button" name="button4" value="返回"  class="normal_btn" onclick="goBack();" />
	       <input type="button" name="button2" value="修改明细" class="normal_btn" onclick="updateDetails();"/> 
	    </td>       
	  </tr>
  </table>
  </div>
  </div>
</form>
</div>
</body>
</html>