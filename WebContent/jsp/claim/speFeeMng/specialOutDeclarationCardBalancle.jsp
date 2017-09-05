<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>特殊外出费用结算单</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
//日历控件初始化
function doInit()
	{
	   loadcalendar();
	}
		//清除方法
 function clr() {
	document.getElementById('dealerCode').value = "";
  }
</script>
</head>

<body>
  <div class="navigation">
      <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;特殊费用管理&gt;特殊外出费用结算单
  </div>
  <!-- 查询条件 begin -->
  <form method="post" name="fm" id="fm">
   <table class="table_query" >
	  <tr>
	    <td class="table_query_2Col_label_7Letter">费用单据编码：</td>
	    <td align="left">
	      <input name="feeNo" id="feeNo" value="" type="text" size="17" class="middle_txt"/>
	    </td>
	    <td class="table_query_2Col_label_7Letter">巡航单据编码：</td>
	    <td align="left">
	       <input name="crNo" id="crNo" value="" type="text" size="17" class="middle_txt"/>
	    </td>
	  </tr>
	  <tr>
	    <td class="table_query_2Col_label_7Letter">经销商代码：</td>
	    <td align="left">
             <textarea rows="3" cols="23" id="dealerCode"  name="dealerCode"></textarea>
             <input class="mini_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCode','','true','',true)" />
             <input name="button2" type="button" class="normal_btn" onclick="clr();" value="清除"/>
  	    </td>
  	    <td class="table_query_2Col_label_7Letter">经销商名称：</td>
	    <td align="left">
             <input class="middle_txt" id="dealerName" name="dealerName" type="text" />
  	    </td>
	  </tr>
	  <tr>
	  <td class="table_query_2Col_label_7Letter">费用单据上报时间：</td>
	    <td align="left">
	        <div align="left">
            	 <input name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
            	 至
            	 <input name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
            </div>	
	    </td>
	  </tr>
	  <tr>
	    <td colspan="6" align="center">
	      <input class="normal_btn" type="button" name="button2" value="查询" onclick="__extQuery__(1);"/>
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
	var url = "<%=contextPath%>/claim/speFeeMng/SpecialOutDeclarationCardBalancle/SpecialBalancleQuery.json";
				
	var title = null;

	var columns = [
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_SHORTNAME' ,align:'center'},
				{header: "费用单据编号",dataIndex: 'FEE_NO' ,align:'center'},
				{header: "巡航单据编码",dataIndex: 'CR_NO' ,align:'center'},
				{header: "上报时间",dataIndex: 'MAKE_DATE' ,align:'center'},
				{header: "巡航目的地",dataIndex: 'CR_WHITHER' ,align:'center'},
				{header: "总费用(元)",dataIndex: 'FEE' ,align:'center',renderer:amountFormat},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:SpecialBalancleUpdateQueryInit ,align:'center'}
		      ];
	//结算的超链接设置
	function SpecialBalancleUpdateQueryInit(value,meta,record){
		
	    return String.format(
         "<a href='#' onClick='balanceOper("+value+");'>[结算]</a>");
	}
	function balanceOper(value){
		var goUrl="<%=contextPath%>/claim/speFeeMng/SpecialOutDeclarationCardBalancle/SpecialBalancleUpdateQueryInit.do?id="+value;
		OpenHtmlWindow(goUrl,900,500);
	}
</script>
<!--页面列表 end -->
</body>
</html>