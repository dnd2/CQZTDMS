<%-- 创建时间 : 2010.06.03
             创建人:xiuzhiming
             功能描述：索赔申请上报功能首页，用于查询和上报索赔申请单。 
--%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>索赔申请上报</title>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
    function doInit()
	{
	   loadcalendar();
	}
</script>
</head>
<body onload="doInit();">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
              当前位置： 售后服务管理&gt;索赔旧件管理&gt;上端抵扣扫描
  </div>
  <form method="post" name="fm">

    <TABLE align="center" class="table_query" >
          <tr>
            <td align="right" nowrap="true">条码：</td>
            <td>
            	<input name="barcodeNo" id="barcodeNo" value="" type="text" class="middle_txt">
            </td>
         </tr>
    	  <tr>
            <td align="center" colspan="4" nowrap="true">
            	<input class="normal_btn" type="button" name="queryBtn1" id="queryBtn" value="查询"  onClick="query()" >
			</td>
		</tr>
		
		
  </table>
  <script type="text/javascript" >


  function query(){
	  var barcodeNo = document.getElementById('barcodeNo').value;
	 
	  if(barcodeNo==''){
		  MyAlert("请输入条码查询");
			return;
		  }
	  
	
	  __extQuery__(1);
	  }
		var myPage;
		var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/queryScanningList.json";
		var title = null;
		
		var columns = [
					{header: "索赔申请单号",sortable: false,dataIndex: 'CLAIM_NO',align:'center',renderer:claimDetail},				
					{header: "配件代码",sortable: false,dataIndex: 'PART_CODE',align:'center'},
					{header: "配件名称",sortable: false,dataIndex: 'PART_NAME',align:'center'},
					{header: "箱号",sortable: false,dataIndex: 'BOX_NO',align:'center'},
					{header: "条码",sortable: false,dataIndex: 'BARCODE_NO',align:'center'},
					{header: "抵扣原因",sortable: false,dataIndex: 'DEDUCTIBLE_REASON_CODE',align:'center',renderer:getItemValue},
					{id:'action',header: "操作",dataIndex: 'BARCODE_NO',renderer:claimAuditing}
					
					
			      ];

		  function claimAuditing(value,meta,record){
		    	return String.format("<a href=\"#\" onclick=auditingClaim("+value+","+record.data.DEDUCTIBLE_REASON_CODE+",this) >[修改]</a>");
		    }

		  function auditingClaim(value,reasonCode,aobj){

			  var tarUrl = "<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/updateScanning.do?barcodeNo1="+value+"&reasonCode="+reasonCode;
				document.forms[0].method="post";
				document.forms[0].action=tarUrl;
				document.forms[0].submit();

			  }
		///索赔抵扣明细
		function claimDetail(value,meta,record){
	  		return String.format("<a href=\"#\" onclick=\"queryDetail("+record.data.CLAIM_ID+")\">[" + value + "]</a>");
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

		

		
  </script>
</form>
<!--分页  -->
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</body>
</html>