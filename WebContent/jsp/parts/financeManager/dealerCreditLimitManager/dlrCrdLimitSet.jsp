<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<title>信用额维护</title>
<script language="javascript" type="text/javascript">
	function doInit(){
			__extQuery__(1);
	}
</script>
</head>
<body>
  <form method="post" enctype="multipart/form-data" name ="fm" id="fm">
	<div class="wbox">
		<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />
		  &nbsp;当前位置：配件财务管理 &gt; 服务商信用额维护
		  <input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
		  <input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }"/>
          <input type="hidden" name="childOrgId" id="childOrgId" value="${childOrgId }"/>
          <input type="hidden" name="childOrgCode" id="childOrgCode" value="${childOrgCode }"/>
          <input type="hidden" name="userRole" id="userRole" value="${userRole }"/>
		</div>
		<table class="table_query">
			<%-- <c:if test="${normalDlr ne userRole}"> --%>
			<th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 查询条件</th>
			<tr>
		      <td width="10%" align="right" >服务商编码：</td>
		      <td width="20%" >
		        <input class="middle_txt" type="text" name="dealerCode" id="dealerCode"/>
<!-- 	        <input name="BUTTON" type="button" class="_btn" onclick="sel()" value="..."/>   
		        <input type="hidden" name="dealerId" id="dealerId"/> -->
		      </td>
		      <td width="10%" align="right" >服务商名称：</td>
		      <td width="20%" >
		        <input class="middle_txt" type="text" name="dealerName" id="dealerName"/>
		      </td>
		      <td width="10%" align="right" >资金类型：</td>
		      <td width="20%" >
		        <script type="text/javascript">
					genSelBoxExp("accountKind",<%=Constant.FIXCODE_CURRENCY%>,<%=Constant.FIXCODE_CURRENCY_01%>,true,"short_sel","onchange=__extQuery__(1)","false","");
				</script>
		      </td>
	      	</tr>
            <%-- </c:if> --%>
            <tr>
		    	<td  align="center" colspan="6" >
		    	 <%--  <c:if test="${normalDlr ne userRole}"> --%>
		    	  <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)"/>
		    	  <%-- </c:if --%>
		    	  <input class="normal_btn" type="button" value="导出" onclick="exportPartExceptionExcel()"/>
		    	  <input class="normal_btn" type="button" name="BtnuploadTable" value="批量导入" onclick="showUpload();"/>
		    	</td>    
			</tr>
		</table>
	
	<table id="uploadTable" style="display: none">
        <tr>
            <td><font color="red">
                <input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate()"/>
                文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font>
                <input type="file" name="uploadFile" style="width: 250px" id="uploadFile" value=""/>
                &nbsp;
                <input type="button" id="upbtn" class="normal_btn" value="确定" onclick="uploadEx()"/></td>
        </tr>
    </table>
	</div>
	
	<!-- 查询条件 end -->
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end -->
  </form>
  <script type="text/javascript" >
	var myPage;

	var url = "<%=contextPath%>/parts/financeManager/dealerCreditLimitManager/partDlrCrdLimitSetAction/partDealerAccQuerySearch.json";
				
	var title = null;
	var columns = null;

	/* if("3" != ${userRole})
	{ */
		columns = [
					{header: "序号", dataIndex: 'SEQUENCE_ID', renderer:getIndex},
					{header: "服务商编码", dataIndex: 'DEALER_CODE'},
					{header: "服务商名称", dataIndex: 'DEALER_NAME',style:'text-align:left; '},
					{header: "销售单位编码", dataIndex: 'PARENTORG_CODE',style:'text-align:left;'},
					{header: "销售单位名称", dataIndex: 'PARENTORG_NAME',style:'text-align:left; '},
					{header: "资金类型", dataIndex: 'ACCOUNT_KIND',renderer:getItemValue},
					{header: "总可用余额", dataIndex: 'USEABLEACCOUNT', style:'text-align:right;'},
					{header: "预扣款金额", dataIndex: 'PREEMPTIONVALUE', style:'text-align:right; '},
					{header: "现金可用余额", dataIndex: 'CASH_KY', style:'text-align:right;'},
					{header: "信用额度", dataIndex: 'CREDIT_LIMIT_UFM',style:'text-align:right; ', renderer:getCrdLmtText},
					{header: "现金账户余额", dataIndex: 'ACCOUNT_SUM',style:'text-align:right; '},
	                {header: "已开票金额", dataIndex: 'ACCOUNT_INVO', style:'text-align:right; '},
	                {id:'action',header: "操作",sortable: false,dataIndex: 'ACCOUNT_ID',renderer:myLink ,align:'center'}
			      ];
	/* }
	else
	{
		columns = [
					{header: "序号", dataIndex: 'SEQUENCE_ID', renderer:getIndex},
//					{header: "服务商编码", dataIndex: 'DEALER_CODE',style:'text-align:left; '},
//					{header: "服务商名称", dataIndex: 'DEALER_NAME',style:'text-align:left; '},
					{header: "销售单位编码", dataIndex: 'PARENTORG_CODE',style:'text-align:left; '},
					{header: "销售单位名称", dataIndex: 'PARENTORG_NAME',style:'text-align:left; '},
					{header: "资金类型", dataIndex: 'ACCOUNT_KIND',renderer:getItemValue},
					{header: "总可用余额(元)", dataIndex: 'USEABLEACCOUNT', style:'text-align:right;'},
					{header: "预扣款金额(元)", dataIndex: 'PREEMPTIONVALUE', style:'text-align:right; '},
					{header: "现金可用余额(元)", dataIndex: 'CASH_KY', style:'text-align:right;'},
					{header: "信用额度(元)", dataIndex: 'CREDIT_LIMIT_UFM',style:'text-align:right; '},
					{header: "现金账户余额(元)", dataIndex: 'ACCOUNT_SUM',style:'text-align:right; '},
	                {header: "已开票金额(元)", dataIndex: 'ACCOUNT_INVO', style:'text-align:right; '}
			      ];
	}
 */
	//返回信用额文本框
	function getCrdLmtText(value,meta,record)
	{
		var acountId = record.data.ACCOUNT_ID;
		return String.format("<input type='text' name='CrdLimit_"+ acountId +"' id='CrdLimit_"+ acountId +"' onchange='dataCheck(this)' value='"+ value +"' />");
	}

	//返回信用额修改按钮
	function myLink(value,meta,record)
	{
		var acountId = record.data.ACCOUNT_ID;
		return String.format("<input type='button' class='normal_btn' onclick='ConfmUpdCrdLmt("+ acountId +")' value='修改' />");
	}

	//信用额修改确认
	function ConfmUpdCrdLmt(acountId)
	{
		var crdLmt = 0;
		var tmpVal = document.getElementById('CrdLimit_'+acountId).value;
		if(null != tmpVal && "" != tmpVal && tmpVal > 0)
		{
			crdLmt = tmpVal;
		} 
		else
		{
			MyAlert("请先设置该服务商信用额!");
			return false;
		}
		MyConfirm("确定修改该服务商的信用额?",commitUpdCrdLmt,[acountId]);
	}

	function commitUpdCrdLmt(acountId){
		btnDisable();
		var url = "<%=contextPath%>/parts/financeManager/dealerCreditLimitManager/partDlrCrdLimitSetAction/updateDlrCrdLmt.json?acountId="+ acountId +"&curPage=" + myPage.page;	
		sendAjax(url,getResult,'fm');
	}

	function getResult(json){
		btnEnable();
		if(null != json){
	        if (json.errorExist != null && json.errorExist.length > 0) {
	        	 MyAlert(json.errorExist);
	        	 __extQuery__(json.curPage);
	        } else if (json.success != null && json.success == "true") {
	        	MyAlert("操作成功!");
	        	__extQuery__(json.curPage);
	        } else {
	            MyAlert("操作失败，请联系管理员!");
	        }
		}
	}

	function dataCheck(obj)
	{
		var value = obj.value;
		if(isNaN(value)){
			MyAlert("请输入数字!");
			obj.value = "";
			return;
		}
		var re = /^[1-9]+[0-9]*[.]?[0-9]*$/;
		if(!re.test(obj.value)){
			MyAlert("请输入正确的金额数字!");
			obj.value = "";
			return;
		}
		
	}

	//失效按钮
	function btnDisable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });

	}

	//有效按钮
	function btnEnable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = "";
	    });

	}
     
	//下载
	function exportPartExceptionExcel(){
		document.fm.action="<%=contextPath%>/parts/financeManager/dealerCreditLimitManager/partDlrCrdLimitSetAction/exportPartDealerAccountExcel.do";
		document.fm.target="_self";
		document.fm.submit();
	}
	
	function showUpload() {
        if ($("uploadTable").style.display == "none") {
            $("uploadTable").style.display = "block";
        } else {
            $("uploadTable").style.display = "none";
        }
    }
	
    function exportExcelTemplate() {
        fm.action = "<%=contextPath%>/parts/financeManager/dealerCreditLimitManager/partDlrCrdLimitSetAction/exportExcelTemplate.do";
        fm.submit();
    }
    
    function uploadEx() {
        var filevalue = fm.uploadFile.value;
        if (filevalue == '') {
            MyAlert('导入文件不能空!');
            return false;
        }
        var fi = filevalue
                .substring(filevalue.length - 3, filevalue.length);
        if (fi != 'xls') {
            MyAlert('导入文件格式不对,请导入xls文件格式');
            return false;
        }
        fm.action = "<%=contextPath%>/parts/financeManager/dealerCreditLimitManager/partDlrCrdLimitSetAction/uploadPartBaseExcel.do";
        fm.submit();
    }
	//千分格式
	function addKannma(number) {  
	    var num = number + "";  
	    num = num.replace(new RegExp(",","g"),"");   
	    // 正负号处理   
	    var symble = "";   
	    if(/^([-+]).*$/.test(num)) {   
	        symble = num.replace(/^([-+]).*$/,"$1");   
	        num = num.replace(/^([-+])(.*)$/,"$2");   
	    }   
	  
	    if(/^[0-9]+(\.[0-9]+)?$/.test(num)) {   
	        var num = num.replace(new RegExp("^[0]+","g"),"");   
	        if(/^\./.test(num)) {   
	        num = "0" + num;   
	        }   
	  
	        var decimal = num.replace(/^[0-9]+(\.[0-9]+)?$/,"$1");   
	        var integer= num.replace(/^([0-9]+)(\.[0-9]+)?$/,"$1");   
	  
	        var re=/(\d+)(\d{3})/;  
	  
	        while(re.test(integer)){   
	            integer = integer.replace(re,"$1,$2");  
	        }   
	        return symble + integer + decimal;   
	  
	    } else {   
	        return number;   
	    }   
	}  
  </script>
</body>
</html>