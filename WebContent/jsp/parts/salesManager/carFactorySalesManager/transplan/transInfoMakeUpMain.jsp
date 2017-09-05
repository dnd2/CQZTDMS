<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
	String curPage = (String)request.getAttribute("curPage");
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
    <title>发运信息补录</title>
    <style>#uploadDiv .table_query,#uploadDiv table.table_query th{background-color: transparent;}#uploadDiv .table_query{margin-bottom: 10px}</style>
</head>
<body onload="__extQuery__(1);"> <!--  onunload="javascript:destoryPrototype()" -->
<div class="wbox">
    <div class="navigation">
    	<img src="<%=contextPath %>/img/nav.gif" alt=""/>&nbsp;当前位置：配件管理&gt;总部销售管理&gt;发运信息补录
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
		        <table class="table_query">
		            <tr>
		            	<td class="right">发运单号：</td>
		                <td width="24%">
		                	<input class="middle_txt" type="text" id="TRANS_CODE" name="TRANS_CODE" value="${transCode}"/>
		               	</td>
		               	
		            	<td class="right">订单单号：</td>
		                <td width="24%">
		                	<input class="middle_txt" type="text" id="ORDER_CODE" name="ORDER_CODE" value="${orderCode}"/>
		               	</td>
		               	
		               	<td class="right">补录状态：</td>
		                <td width="24%">
		                	<select id="STATE" class="u-select" name="STATE">
		                		<option <c:if test="${state=='0'}">selected="selected"</c:if> value="0">--请选择--</option>
		                		<option <c:if test="${state=='1' || empty state}">selected="selected"</c:if> value="1">未补录</option>
		                		<option <c:if test="${state=='2'}">selected="selected"</c:if> value="2">已补录</option>
		                	</select>
		               	</td>
		            </tr>
		            <tr>
		            	<td class="right">发运日期：</td>
		                <td>
							<input name="EstartDate" type="text" style="width: 80px;" class="middle_txt" id="EstartDate" value="${old}" />
							<input name="button2" type="button" class="time_ico" />
							至
							<input name="EendDate" type="text" style="width: 80px;" class="middle_txt" id="EendDate" value="${now}" />
							<input name="button2" type="button" class="time_ico" />
						</td>
		            </tr>
		            <tr>
		                <td colspan="10" class="center">
		                	<input class="normal_btn" type="button" value="查 询" onclick="__extQuery__(1);"/>&nbsp;
		                    <input class="normal_btn" type="button" value="批量补录" id="upload_button" name="button1" onclick="showUpload('uploadDiv');">&nbsp;
		                    <input id="showbtn_id" class="normal_btn" type="button" value="重置" onclick="clears()">&nbsp;
		                </td>
		            </tr>
		        </table>
		        <div style="display:none;" id="uploadDiv" class="upload-divide">
		        	<table class="table_query">
			            <th colspan="6"><img class="nav" src="<%=contextPath %>/img/subNav.gif"/> 上传文件</th>
			            <tr>
			                <td colspan="2">
			                    <input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate();" />
			                    <font color="red">选择文件点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font>
			                    <input type="file" name="uploadFile" id="uploadFile" style="width: 250px" value=""/>
			                    <font color="red">*</font>
			                    <input type="button" id="upbtn" class="normal_btn" value="确 定" onclick="confirmUpload()"/>
			                </td>
			            </tr>
			            <c:if test="${not empty info}">
				            <tr>
				            	 <td colspan="2">
				            	 	<input class="normal_btn" type="button" value="显示/隐藏" onclick="showUpload('errinfoTr');">
				            	 	<font color="red" style="font-size: 16px;">${info}</font>
				            	 </td>
				            </tr>
				            <tr id="errinfoTr">
				            	 <td colspan="2">
				            	 	<c:forEach items="${errList}" var="list" varStatus="v">
				            	 		<label>${v.count}-->${list}</label>
				            	 	</c:forEach>
				            	 </td>
				            </tr>
			            </c:if>
			        </table>
			    </div>
		    </div>
        </div>

        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>
    <script type="text/javascript">
        var myPage;
        var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTrans/getTransInfoMakeUp.json";
        var title = null;
        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {id:'action',header: "操作",sortable: false,dataIndex: 'TRANS_ID',renderer:myLink , align: 'center'},
            {header: "发运单号", dataIndex: 'TRANS_CODE',  align: 'center'},
            {header: "出库单号", dataIndex: 'OUT_CODE',  align: 'center'},
            {header: "订单单号", dataIndex: 'ORDER_CODE',  align: 'center'},
            {header: "订单类型", dataIndex: 'ORDER_TYPE',  align: 'center',renderer:getItemValue},
            {header: "订货单位代码", dataIndex: 'DEALER_CODE',  align: 'center'},
            {header: "订货单位名称", dataIndex: 'DEALER_NAME',  align: 'center'},
            {header: "销售单位代码", dataIndex: 'SELLER_CODE',  align: 'center'},
            {header: "销售单位名称", dataIndex: 'SELLER_NAME',  align: 'center'},
            {header: "发运日期", dataIndex: 'CREATE_DATE',  align: 'center'},
            {header: "补录人", dataIndex: 'UPDATE_BY',  align: 'center'},
            {header: "补录日期", dataIndex: 'UPDATE_DATE',  align: 'center'},
            {header: "备注", dataIndex: 'REMARK2',  align: 'center'}
            
        ];

        //设置超链接  begin
		function myLink(value, meta, record){
        	var str = '<a href="javascript:void(0)" onclick="toUpdateMakeUp(\''+value+'\')">[修改]</a>&nbsp;';
        	return str;
        }
        
        //设置超链接  end
        //禁用所有按钮
        //======
		function disBtnAll(){
			jQuery('input[type="button"]').prop('disabled',true);
			jQuery('#showbtn_id').prop('disabled',false);
		}
		//起用所有按钮
		function enBtnAll(){
			jQuery('input[type="button"]').prop('disabled',false);
		}
		//======
		//跳转到修改页面
		function toUpdateMakeUp(id){
			//&transCode='+$('TRANS_CODE').value+'&orderCode='+$('ORDER_CODE').value+'&state='+$('STATE').value
			var transCode = jQuery('#TRANS_CODE').val();
			var orderCode = jQuery('#ORDER_CODE').val();
			var state = jQuery('#STATE').val();
			var str = '&transCode='+transCode+'&orderCode='+orderCode+'&state='+state;
			var urlkey = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTrans/toUpdateMakeUp.do?id='+id+str;
			location = urlkey;
		}
		//======
        //操作结果
        function getResult(json){
        	var success = json.success;
        	if(success!='' && success!=null && success!='null' && success!='undefined'){
        		MyAlert(success);
        	}else{
        		MyAlert(json.error);
        	}
        	enBtnAll();
        	__extQuery__(1);
        }
        //根据id显示页面
        function showUpload(id){
        	jQuery('#'+id).toggle();
        }
		//下载变更模板
		function exportExcelTemplate(){
			fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTrans/exportExcelTemplate.do";
            fm.submit();
		}
		//上传模板
		function confirmUpload(){
			//验证文件格式
        	var file = jQuery('#uploadFile').val();
        	if(file=='' || file==null){
        		MyAlert('请选择上传文件');
        		return;
        	}
        	var start = file.lastIndexOf('.');
        	var gsStr = file.substring(start,start.length).toUpperCase();
        	if(gsStr=='.XLS' || gsStr=='.XLSX'){
        		disBtnAll();
        		MyConfirm('确认上传文件？',confirmResult,[],noConfirmResult,[]);
        	}else{
        		MyAlert('只支持.XLS,.XLSX格式的文件，不支持'+gsStr+'格式的文件');
        		return;
        	}
		}
		
		function confirmResult(){
			fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTrans/uploadXlsFileByTransInfo.do";
		    fm.submit();
		}
		function noConfirmResult(){
			enBtnAll();
		}
		//清空
		function clears(){
			$('#TRANS_CODE')[0].value='';
			$('#ORDER_CODE')[0].value='';
			enBtnAll();
		}
		if('${info}'!=null && '${info}'!='' && '${info}'!='null'){
        	showUpload('uploadDiv');
        }
		if('${successInfo}'!=null && '${successInfo}'!='' && '${successInfo}'!='null'){
			MyAlert('${successInfo}');
		}
    </script>
</div>
</body>
</html>