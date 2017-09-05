<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>索赔旧件二次抵扣-回运索赔单信息查询</title>
<% String contextPath = request.getContextPath(); %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<body onload="doInit();">
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />
  	当前位置： 售后服务管理&gt;索赔旧件管理&gt;索赔旧件二次抵扣
</div>
<form method="post" name="fm">
	<table class="table_query" >
		<tr>
			<td align="right" nowrap="true">经销商代码：</td>
			<td align="left" nowrap="true">
				<textarea id="dealerCode"  name="dealerCode" rows="3" cols="20" datatype="1,is_null,2000"></textarea>
	            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
	            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/>
			</td>
			<td align="right" nowrap="true">经销商名称：</td>
			<td align="left" nowrap="true">
				<input type="text" name="DEALER_NAME" id="DEALER_NAME" datatype="1,is_digit_letter_cn,30" class="middle_txt" value=""/>
			</td>
		</tr>
		<tr>
			<td align="right" nowrap>索赔单号：</td>
            <td>
            	<input name="CLAIM_NO" value="" type="text" class="middle_txt"/>
            </td>
			<td align="right" nowrap="true">索赔类型：</td>
			<td align="left" nowrap="true">
				<script type="text/javascript">
					 genSelBoxExp("CLAIM_TYPE",<%=Constant.CLA_TYPE%>,"",true,"short_sel","","true",'');
			    </script>
			</td>
		</tr>
		<tr>
			<td align="right" nowrap="true">工单号：</td>
			<td align="left" nowrap="true">
				<input name="RO_NO" class="middle_txt" type="text" value="" /> 
				
				<input name="LINE_NO" id="LINE_NO" value="" type="hidden" datatype="1,is_digit,3" class="mini_txt"/>
			</td>
            <td align="right" nowrap="true" rowspan="2">VIN：</td>
			<td align="left" rowspan="2">
				<textarea name="VIN" rows="3" cols="18"></textarea>
				<!-- 抵扣的申请单必需已经结算 -->
				<input type="hidden" name="CLAIM_STATUS" value="<%=Constant.CLAIM_APPLY_ORD_TYPE_07%>"/>
			</td>
		</tr>
		<tr>
			<td align="right" nowrap="true">申请日期：</td>
			<td align="left" nowrap="true">
				<input type="text" name="APPLY_DATE_START" id="APPLY_DATE_START"
	             value="<%=request.getAttribute("startDate") %>" type="text" class="short_txt" 
	             datatype="0,is_date,10" group="APPLY_DATE_START,APPLY_DATE_END" 
	             hasbtn="true" callFunction="showcalendar(event, 'APPLY_DATE_START', false);"/>
	             &nbsp;至&nbsp;
	 			<input type="text" name="APPLY_DATE_END" id="APPLY_DATE_END" 
	 			value="<%=request.getAttribute("endDate") %>" type="text" class="short_txt" datatype="0,is_date,10" 
	 			group="APPLY_DATE_START,APPLY_DATE_END" 
	 			hasbtn="true" callFunction="showcalendar(event, 'APPLY_DATE_END', false);"/>
			</td>
		</tr>
		
			<tr>
			<td align="right" nowrap="true">旧件条码：</td>
			<td align="left" nowrap="true">
				<input name="barcode_no" id="barcode_no" class="middle_txt" type="text" value="" /> 
			</td>
			</tr>
		<tr>
		  <td colspan="4" align="center">
	  		<span class="zi">
	    		<input type="button" name="button12" value="查询" id="queryBtn" onclick="__extQuery__(1);"  class="normal_btn"/>
			</span>
		  </td>  
		</tr>  
	</table>
</form>
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end --> 
	<script type="text/javascript">
		var myPage;
		var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartAgainDeduceManager/queryDeductClaim.json";
		var title = null;
		
		var columns = [
					{header: "经销商代码",sortable: false,dataIndex: 'DEALER_CODE',align:'center'},
					{header: "经销商名称",sortable: false,dataIndex: 'DEALER_NAME',align:'center'},	
					{id:'action',header: "索赔单号",sortable: false,dataIndex: 'CLAIM_NO',align:'center',renderer:claimDetail},			
					//{header: "工单号-行号",sortable: false,dataIndex: 'RO_NO',align:'center'},
					{header: "索赔类型",sortable: false,dataIndex: 'CLAIM_TYPE',align:'center',renderer:getItemValue}, 
					//{header: "VIN",sortable: false,dataIndex: 'VIN',align:'center'},
					{header: "申请日期",sortable: false,dataIndex: 'CREATE_DATE',align:'center'},
					{header: "索赔配件数量",sortable: false,dataIndex: 'QUANTITY',align:'right'},
					{header: "回运配件数量",sortable: false,dataIndex: 'RETURN_NUM',align:'right'},
					{header: "未回运数量",sortable: false,dataIndex: 'MISTAKECOUNT',align:'right'},
					{id:'action',header: "操作",dataIndex: 'CLAIM_NO',renderer:claimDeduct}
			      ];
	      
		//需要挂接索赔申请状态查询的明细查询功能
		function claimDetail(value,meta,record){
	  		return String.format("<a href=\"#\" onclick=\"queryDetail("+record.data.ID+")\">[" + value + "]</a>");
		}

		//查询索赔单明细
		function queryDetail(id){
			var tarUrl = "<%=contextPath%>/claim/application/ClaimBillStatusTrack/claimBillDetailForward.do?ID="+id;
			var width=900;
			var height=500;
			var screenW = window.screen.width-30;	
			var screenH = document.viewport.getHeight();
			if(screenW!=null && screenW!='undefined')
				width = screenW;
			if(screenH!=null && screenH!='undefined')
				height = screenH;
			
			OpenHtmlWindow(tarUrl,width,height);
		}

		//抵扣链接
	    function claimDeduct(value,meta,record){
	    	return String.format("<a href='#' onclick=\"isBalance('"+value+"');\">[抵扣]</a>");
	    }
	    //判断索赔单是否结算
	    function isBalance(claim_no){
	    	var isBalance_url="<%=contextPath%>/claim/oldPart/ClaimOldPartAgainDeduceManager/isBalanceOper.json?claim_no="+claim_no;
	    	makeNomalFormCall(isBalance_url,afterViewBalance,'fm','createOrdBtn');
	    }
	    //查看是否结算回调函数
	    function afterViewBalance(json){
	    	var isBalance=json.isBalance;
	    	var claim_no=json.claim_no;
	    	if(isBalance=='0'){
	           MyAlert("该索赔单还未结算，不能进行抵扣！");
	    	}else if(isBalance=='1'){
	    	   goToDeduct(claim_no);
	    	}
	    }
	    //进入二次抵扣页面
	    function goToDeduct(claim_no){
	    	var goDeductUrl="<%=contextPath%>/claim/oldPart/ClaimOldPartAgainDeduceManager/deducePreViewInfo.do?claim_no="
	    		      +claim_no;
	    	OpenHtmlWindow(goDeductUrl,900,500);
	    }
		//清空经销商框
		function clearInput(){
			var target = document.getElementById('dealerCode');
			target.value = '';
		}

		//初始化时间控件
	    function doInit(){
	  	   loadcalendar();
	    }  
	</script>
</body>
</html>