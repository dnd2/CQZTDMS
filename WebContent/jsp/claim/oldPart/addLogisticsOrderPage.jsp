<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔件回运清单新增</title>
<% String contextPath = request.getContextPath(); %>
</head>
<script type="text/javascript">
	function doInit(){
	   loadcalendar();
	}
</script>
<body onload="doInit();" onkeydown="keyListnerResp();">
	<form method="post" name ="fm" id="fm">
		<table width="100%">
		<tr><td>
	 	<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理 &gt;索赔旧件管理 &gt;索赔回运清单维护
		</div>
		<table class="table_edit">
	    	<tr>
		    	<th><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
	        </tr>
        </table>
        <table class="table_edit">
        	<tr>
        		<td align="right" nowrap="nowrap">货运单号：</td>
        		<td align="left">
        			<input name="backOrderNo" id="backOrderNo" value="" type="text" class="middle_txt" datatype="1,is_null,16">
        		</td>
        		<td align="right" nowrap="nowrap">发运日期：</td>
        		<td align="left">
        			<input name="sendDate" id="sendDate" value="" type="text" class="short_txt" datatype="1,is_date,10"
        			hasbtn="true" callFunction="showcalendar(event, 'sendDate', false);">
        		</td>
        	</tr>
        	<tr>
        		<td align="right" nowrap="nowrap">货运方式：</td>
        		<td align="left">
		          <script type="text/javascript">
		            genSelBoxExp("freightType",<%=Constant.OLD_RETURN_STATUS%>,"",false,"short_sel","","true",'');
		          </script>
        		</td>
        		<td align="right" nowrap="nowrap">装箱总数：</td>
        		<td align="left">
        			<input name="boxNum" id="boxNum" value="" type="text" class="middle_txt" datatype="1,is_null,16">
        		</td>
        	</tr>
        	<tr>
        		<td align="right" nowrap="nowrap">回运类型：</td>
        		<td align="left">
			        <script type="text/javascript">
			            genSelBoxExp("backType",<%=Constant.BACK_TRANSPORT_TYPE%>,"",false,"short_sel","","true",'<%=Constant.BACK_TRANSPORT_TYPE_01%>');
			        </script>
        		</td>
        		<td align="right" nowrap="nowrap">发运地点：</td>
        		<td align="left">
		           <script type="text/javascript">
		               genSelBoxExp("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'');
		           </script>
		           <font color="red">&nbsp;*</font>
        		</td>
        	</tr>
        </table>
        <!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
        <!--分页 end --> 
        <script type="text/javascript">
	        var myPage;
	        //查询路径
	        var url = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryReturnOrderForLogistics.json";
	     				
	        var title = null;
	
	        var columns = [
						{id:'action',header: "选择<input type=\"checkbox\" name=\"returnIds\" onclick=\"selectAll(this,'returnId')\">",dataIndex: 'id',renderer:selectReturnOrder},
	       				{header: "经销商简称",dataIndex: 'dealer_name',align:'center'},
	       				{header: "旧件清单号", dataIndex: 'return_no', align:'center'},
	       				{header: "完成日期", dataIndex: 'create_date', align:'center',renderer:formatDate},
	       				{header: "索赔单数量", dataIndex: 'wr_amount', align:'center'},
	       				{header: "配件项数", dataIndex: 'part_item_amount', align:'center'},
	       				{header: "配件数量", dataIndex: 'part_amount', align:'center'},
	       				{header: "处理状态", dataIndex: 'status_desc', align:'center'}
	       		      ];

	        //格式化时间为YYYY-MM-DD
	        function formatDate(value,meta,record) {
		     	 if (value==""||value==null) {
		     		return "";
		     	 }else {
		     		return value.substr(0,10);
		     	 }
	        }
	        
	        function doCusChange(value){
	        	__extQuery__(1);
	        }

	        function selectReturnOrder(value,meta,record){
		        var returnId = record.data.id;
		        return String.format("<input type=\"checkbox\" name=\"returnId\" value=\""+returnId+"\"/>");
	        }
	        
            //返回回运单查询页
	        function hisBack(){
		        location.href = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryListPage.do";
	        }

	        function createLogisticsOrderConfirm(){
		        var yieldlyEle = $('yieldly');
		        var yieldly = '';
		        if(yieldlyEle)
		        	yieldly = yieldlyEle.value;
		        if(yieldly && ''!=yieldly){
			        MyConfirm("是否确认！",createLogisticsOrder,[]);
		        }else{
			        MyAlert("请选择发运地点！");
		        }
	        }

            //创建物流单（由一级经销商向车厂发起回运）
	        function createLogisticsOrder(){
		        var turl = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/createLogisticsOrder.json";
		        makeNomalFormCall(turl,showDetail,'fm','createOrdBtn');
	        }

	        function showDetail(json){
		        var status = json.SUCCESS;
		        if('DEALED'==status){
			        MyAlert("其他人正在创建物流单！");
			        hisBack();
		        }else if('SUCCESS'!=status){
		        	MyAlert("操作失败！");
		        	__extQuery__(1);
		        }else{
		        	__extQuery__(1);
		        }
	        }
        </script>
        <br>
        <table class="table_edit">
        	<tr>
        		<td align="center">
					<input class="normal_btn" type="button" id="createOrdBtn" name="createOrdBtn" value="确认" onclick="createLogisticsOrderConfirm();">&nbsp;&nbsp;
		            <input class="normal_btn" type="button" name="qryButton2" value="返回" onclick="hisBack();">
            	</td>
           </tr>
        </table>
        </td></tr>
        </table>
	</form>
</body>
</html>