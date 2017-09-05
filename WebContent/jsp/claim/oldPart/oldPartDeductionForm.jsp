<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>索赔旧件抵扣通知单查询</title>
<% String contextPath = request.getContextPath(); %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<body onload="doInit();">
   <div class="navigation">
   <img src="<%=contextPath%>/img/nav.gif" />当前位置： 售后服务管理&gt;索赔旧件管理&gt;索赔旧件抵扣通知单
   </div>
   <form method="post" name="fm">
	   <table class="table_query" >
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
		        <td align="right" nowrap="true" class="zi">抵扣单状态：</td>
		    	<td>
		    		<select name="DEDUCT_STATUS" class="short_sel">
		    		    <option value="">全部</option>
		    			<option value="0">未结算</option>
		    			<option value="1">已结算</option>
		    		</select>
		    	</td>
	            
	            <td nowrap="true">
	            </td>
	          </tr>
	          <tr>
		          <td align="right"  nowrap="nowrap" class="zi">抵扣单号：</td>
		          <td><input type="text" name="TRANSPORT_NO"   class="middle_txt"/></td>
		          <td align="right"  nowrap="nowrap" class="zi">旧件室电话：</td>
		          <td align="left" >023-67591601或023-67932693</td>
	          </tr>
	          <tr>
	          	  <td align="center" nowrap="true" colspan="4">
					<span class="zi">
						<input type="button" name="noticeQueryBtn" value="查询" id="queryBtn" onclick="__extQuery__(1);" class="normal_btn"/>
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
	   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartDeduceNoticeManager/queryOldPartDeduceNotice.json";			
	   var title = null;
	   var columns = [
	  				{header: "序号",align:'center',renderer:getIndex},
	  				{header: "抵扣单号",dataIndex: 'DEDUCT_NO',align:'center'},
	  				{header: "抵扣单状态",dataIndex: 'IS_BAL',align:'center',renderer:changeStatus},
	  				{header: "通知日期", dataIndex: 'NOTICE_DATE', align:'center'},
	  				{header: "索赔旧件数量", dataIndex: 'DEDUCT_COUNT', align:'right'},
	  				{header: "差异旧件数量", dataIndex: 'PART_COUNT', align:'right'},
	  				{header: "材料费扣款(元)", dataIndex: 'PART_AMOUNT', align:'center',renderer:formatCurrency},
	  				{header: "工时费扣款(元)", dataIndex: 'LABOUR_AMOUNT', align:'center',renderer:formatCurrency},
	  				{header: "其他项目扣款(元)", dataIndex: 'OTHER_AMOUNT', align:'center',renderer:formatCurrency},
	  				{header: "总计(元)", dataIndex: 'TOTALAMOUNT', align:'center',renderer:formatCurrency},
	  				{header: "复合申请单号", dataIndex: 'BALANCE_NO', align:'center'},
	  			    {id:'action',header: "操作",sortable: false,dataIndex: 'ID',align:'center',renderer:queryDetail}
	  		      ];


	 //查询抵扣单明细
     function queryDetail(value,metaDate,record){
         return String.format("<a href='#' onClick='detailView("+value+");'>"+"[明细]</a>");
     }
     function detailView(value){
		var width=900;
		var height=500;
		var screenW = window.screen.width-30;	
		var screenH = document.viewport.getHeight();
		if(screenW!=null && screenW!='undefined')
			width = screenW;
		if(screenH!=null && screenH!='undefined')
			height = screenH;
    	 var mUrl = "<%=contextPath%>/jsp/claim/oldPart/oldPartDeductDetail.jsp?ID="+value;
    	 OpenHtmlWindow(mUrl,width,height);
     }
	 //初始化时间控件
     function doInit(){
  	    loadcalendar();
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
    </script>
</body>
</html>