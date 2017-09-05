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
</script>
</head>

<body>
<div class="wbox">
     <div class="navigation">
         <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;配件三包规则维护
     </div>
  <!-- 查询条件 begin -->
  <form method="post" name="fm" id="fm">
  <div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
    <table  class="table_query">
          <tr>
            <td class="table_query_2Col_label_7Letter" style="text-align:right">三包规则代码：</td>
            <td align="left">
            	<input type="text"  name="ruleCode"  id="ruleCode"  class="middle_txt"/>
            </td>
            <td class="table_query_2Col_label_7Letter" style="text-align:right">三包规则名称：</td>
            <td align="left">
          	   <input type="text"  name="ruleName"  id="ruleName"  class="middle_txt"/>
            </td>
          </tr>
          <tr>
            <td class="table_query_2Col_label_7Letter" style="text-align:right">规则类型：</td>
            <td align="left">
		                <script type="text/javascript">
	   					    genSelBoxExp("ruleType",<%=Constant.RULE_TYPE%>,"",true,"","","false",'');
	  				    </script>
  		    </td>
            <td class="table_query_2Col_label_7Letter" style="text-align:right">状态：</td>
            <td align="left">
	              <script type="text/javascript">
	   					    genSelBoxExp("ruleStatus",<%=Constant.STATUS%>,"",true,"","","false",'');
	  		      </script>
            </td>
          </tr>
		 <tr>
		    <td colspan="4" style="text-align:center">
			   <input class="normal_btn" type="button" id="queryBtn" name="queryBtn" value="查询" onclick="__extQuery__(1);"/>
			   &nbsp;&nbsp;
			   <input class="normal_btn" type="button" name="button2" value="新增"  onclick="claimRuleAddInit();"/>	   
		   </td>
	     </tr>   
  </table>
  </div>
  </div>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form> 
</div>
<!-- 查询条件 end -->
<br/>
<!--页面列表 begin -->
<script type="text/javascript" >

	var myPage;
//查询路径
	var url = "<%=contextPath%>/claim/basicData/ClaimRule/claimRuleQuery.json";
				
	var title = null;

	var columns = [
				{header: "序号",align:'center', renderer:getIndex, width:'7%'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:claimRuleUpdateInit ,align:'center'},
				{header: "三包规则代码", dataIndex: 'RULE_CODE', align:'center'},
				{header: "三包规则名称",dataIndex: 'RULE_NAME' ,align:'center'},
				{header: "类型 ",dataIndex: 'RULE_TYPE' ,align:'center',renderer:getItemValue},
				{header: "状态",dataIndex: 'RULE_STATUS' ,align:'center',renderer:getItemValue},
				{header: "新增时间",dataIndex: 'CREATE_DATE' ,align:'center'},
				{header: "包含配件数量",dataIndex: 'PARTSNUM' ,align:'center'}
		      ];
	//修改的超链接设置
	function claimRuleUpdateInit(value,meta,record){
	    return String.format(
         "<a  href=\"<%=contextPath%>/claim/basicData/ClaimRule/claimRuleUpdateInit.do?id="+ 
         	value + "\">[修改]</a>");
         	//<a href=\"#\" onclick='sel(\""+value+"\")'>[删除]</a>");
	}
	
	function sel(str){
	MyConfirm("是否确认删除？",del,[str]);
}  
//删除
function del(str){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimRule/claimRuleDelete.json?did='+str,delBack,'fm','');
}
//删除回调方法：
function delBack(json) {
	if(json.success != null && json.success == "true") {
		MyAlert("删除成功！");
		__extQuery__(1);
	} else {
		MyAlert("删除失败！请联系管理员！");
	}
} 
	//新增三包规则页面
	function claimRuleAddInit(){
		fm.action = "<%=contextPath%>/claim/basicData/ClaimRule/claimRuleAddInit.do";
		fm.submit();
	}
</script>
<!--页面列表 end -->
</body>
</html>