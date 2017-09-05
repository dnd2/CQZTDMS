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
     <div class="navigation">
         <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;合同号维护
     </div>
  <!-- 查询条件 begin -->
  <form method="post" name="fm" id="fm">
    <table  class="table_query">
         	<tr>
 		<td width="10%" align="right">经销商代码：</td>
 		<td width="60%">
 			<textarea cols="50" rows="2" id="dealer_code" name="dealer_code"></textarea>&nbsp;
 			<input type="hidden" name="dealer_id" id="dealer_id"/>
 			<input type="button" value="..." class="mini_btn" onclick="showOrgDealer('dealer_code','dealer_id','true','','true','','10771002');"/>&nbsp;
 			<input type="button" value="清除" class="normal_btn" onclick="wrapOut();"/>
 		</td>
	</tr>
	<tr>
		<td width="10%" align="right">经销商名称：</td>
		<td width="20%"><input type="text" class="middle_txt" name="dealer_name"/></td>
	</tr>
	
	  <tr>
            <td class="table_query_2Col_label_7Letter">产地：</td>
            <td align="left">
		                <script type="text/javascript">
	   					    genSelBoxExp("YIELDLY",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'');
	  				    </script>
  		    </td>
          </tr>
		 <tr>
		    <td colspan="6" align="center">
			   <input class="normal_btn" type="button" name="button1" value="查询" onclick="__extQuery__(1);"/>
			   &nbsp;&nbsp;
			   <input class="normal_btn" type="button" name="button2" value="新增"  onclick="claimRuleAddInit();"/>	   
		   </td>
	     </tr>   
	     
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form> 
<!-- 查询条件 end -->
<br/>
<!--页面列表 begin -->
<script type="text/javascript" >

	var myPage;
//查询路径
	var url = "<%=contextPath%>/claim/basicData/dealerContractNumber/dealerContractNumberQuery.json";
				
	var title = null;

	var columns = [
				{header: "经销商编码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_NAME' ,align:'center'},
				{header: "产地 ",dataIndex: 'YIELDLY' ,align:'center',renderer:getItemValue},
				{header: "合同号",dataIndex: 'CONTRACT_NO' ,align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ID' ,align:'center'}
		      ];
	//修改的超链接设置
	function claimRuleUpdateInit(value,meta,record){
	    return String.format(
         "<a href=\"<%=contextPath%>/claim/basicData/ClaimRule/claimRuleUpdateInit.do?id="+ value + "\">[修改]</a>");
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