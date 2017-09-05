<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>索赔抵扣单维护-查询</title>
<% String contextPath = request.getContextPath(); %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<body onload="doInit();">
<div class="navigation">
	<img src="<%=contextPath%>/img/nav.gif" />当前位置：
		售后服务管理&gt;索赔旧件管理&gt;索赔旧件抵扣单维护
</div>
<form method="post" name="fm">
	<table class="table_query">
		<tr>
			<td align="right" nowrap="true">经销商代码：</td>
			<td align="left" nowrap="true">
                <textarea rows="3" cols="23" id="dealerCode"  name="dealerCode"></textarea>
	            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
	            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/>
			</td>
			<td align="right" nowrap="true">经销商名称：</td>
			<td align="left" nowrap="true">
				<input type="text" name="dealerName" id="dealerName" datatype="1,is_digit_letter_cn,30" class="middle_txt" value=""/>
			</td>  
		</tr>
		<tr>
			<td align="right" nowrap="true" class="zi">通知日期：</td>
	        <td>
	         	<input type="text" name="NOTICE_DATE_START" id="NOTICE_DATE_START"
	            value="" type="text" class="short_txt" 
	            datatype="1,is_date,10" group="NOTICE_DATE_START,NOTICE_DATE_END" 
	            hasbtn="true" callFunction="showcalendar(event, 'NOTICE_DATE_START', false);"/>
	            &nbsp;至&nbsp;
				<input type="text" name="NOTICE_DATE_END" id="NOTICE_DATE_END" 
				value="" type="text" class="short_txt" datatype="1,is_date,10" 
				group="NOTICE_DATE_START,NOTICE_DATE_END" 
				hasbtn="true" callFunction="showcalendar(event, 'NOTICE_DATE_END', false);"/>
	        </td>
	        <td align="right" nowrap="true" class="zi">抵扣单号：</td>
			<td>
				<input type="text" name="TRANSPORT_NO" class="middle_txt" />
			</td>
	    </tr>
	    <tr>
	    	<td align="right" nowrap="true" class="zi">抵扣单状态：</td>
	    	<td>
	    		<select name="DEDUCT_STATUS" class="short_sel">
	    		    <option value="">全部</option>
	    			<option value="0">未结算</option>
	    			<option value="1">已结算</option>
	    		</select>
	    	</td>
	    	<td>&nbsp;</td>
	    	<td>&nbsp;</td>
	    </tr>
	    <tr>
			<td colspan="4" nowrap="true" align="center">
				<span class="zi"> 
					<input type="button" name="button12" value="查询" id="queryBtn" onclick="__extQuery__(1);" class="normal_btn" />
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
	   //抵扣通知信息查询链接
	   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartDeduceOrdManager/queryOldPartDeduce.json";			
	   var title = null;
	   var columns = [
	  				{header: "序号",align:'center',renderer:getIndex},
	  				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
	  				{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
	  				{header: "抵扣单号",dataIndex: 'DEDUCT_NO',align:'center',renderer:deductDetail},
	  				{header: "抵扣单状态",dataIndex: 'IS_BAL',align:'center',renderer:changeStatus},
	  				{header: "通知日期", dataIndex: 'NOTICE_DATE', align:'center'},
	  				{header: "差异旧件数量", dataIndex: 'PART_COUNT', align:'right'},
	  				{header: "索赔旧件数量", dataIndex: 'DEDUCT_COUNT', align:'right'},
	  				{header: "材料费扣款(元)", dataIndex: 'PART_AMOUNT', align:'center',renderer:formatCurrency},
	  				{header: "工时费扣款(元)", dataIndex: 'LABOUR_AMOUNT', align:'center',renderer:formatCurrency},
	  				{header: "其他项目扣款(元)", dataIndex: 'OTHER_AMOUNT', align:'center',renderer:formatCurrency},
	  				{header: "总计(元)", dataIndex: 'TOTALAMOUNT', align:'center',renderer:formatCurrency},
	  			    {id:'action',header: "操作",sortable: false,dataIndex: 'ID',align:'center',renderer:queryDetail}
	  		      ];

	 //查询抵扣明细
     function deductDetail(value,metaDate,record){
    	 var id=record.data.ID;
    	 return String.format("<a href='#' onclick='viewDetail("+id+");'>["+value+"]</a>");
     }
     function viewDetail(id){
    	 var mUrl = "<%=contextPath%>/jsp/claim/oldPart/oldPartDeductDetail.jsp?ID="+id;
  	     OpenHtmlWindow(mUrl,900,500);
     }
	 //抵扣单维护
     function queryDetail(value,metaDate,record){
         var mUrl = "<%=contextPath%>/claim/oldPart/ClaimOldPartDeduceOrdManager/queryDeductOrdMaintainInfo.do";
         var status = record.data.IS_BAL;
         var content = '';
         if(status=='0'){
        	 return String.format('<a href="' + mUrl + '?deduct_id=' + value + '&STATUS=' + status+ '">'+'[维护]</a>');
         }else if(status=='1'){
        	 return String.format('');
         }
     }
	 //将索赔单状态转换成中文
     function changeStatus(value,metaDate,record){
         var content = '';
         if(value=='0'){
             content = '未结算';
         }else if(value=='1'){
             content = '已结算';
         }
         return String.format(content);
     }

	 //初始化时间控件
     function doInit(){
  	    loadcalendar();
     }   

     //清空经销商框
	 function clearInput(){
		var target = document.getElementById('dealerCode');
		target.value = '';
	 } 
    </script>
</body>
</html>