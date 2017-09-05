<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" >var $J=$.noConflict(); </script>
<title>在途位置维护</title>
</head>
<body >
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>在途位置维护</div>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input type="hidden" name="dtl_ids" id="dtl_ids" />
<div class="form-panel">
	<h2>发运信息</h2>
	<div class="form-body">
	<input type="hidden" name="errcode" id="errcode" value="${errcode }" />
	<table class="table_query">
			<tr>
				<td class="right">承运商名称：</td>
				<td align="left">${logiName}</td>
				<td class="right">交接单号：</td>
				<td align="left">${billNo}</td>
				<td class="right">交接日期：</td>
				<td align="left">${billCrtDate}</td>
			</tr>
	</table>
	</div>
</div>
	<table class=table_list style="border-bottom:1px solid #DAE0EE" >  
		<tr class=cssTable >
			<th nowrap="nowrap">序号</th>
			<th nowrap="nowrap">操作</th>
			<th nowrap="nowrap"><input type="checkbox" id="checkAlldtl" name="checkAlldtl" onclick="checkAll(this);" />选择</th>
			<th nowrap="nowrap">车系</th>
			<th nowrap="nowrap">车型配置</th>
			<th nowrap="nowrap">VIN</th>
			<th nowrap="nowrap">订单号</th>
			<th nowrap="nowrap">详细收货地址</th>
			<th nowrap="nowrap">当前位置</th>
			
		</tr>
		<c:forEach items="${list}" var="po" varStatus="i">
    		<tr class="table_list_row2">
		      <td>${i.index+1 }</td>
		      <td align="center"><a href='javascript:void(0);' onclick='showAddress(${po.DTL_ID});'>[查看]</a></td>
		      <td align="center">
		        <input type="checkbox" name="DTL_ID"  <c:if test="${po.STATUS==20521002 }">disabled="disabled"</c:if>  value="${po.DTL_ID }">
		      </td>
		      <td align="center">${po.SERIES_NAME}</td>
		      <td align="center">${po.PACKAGE_NAME}</td>
		      <td align="center">
		      <a href="#" onclick="showlog(${po.DTL_ID });">${po.VIN}</a>
		      </td>
		      <td align="center">${po.ORDER_NO}</td>
		      <td align="center">
		       <a href="#" title="${po.ADDRESS}" >
		         <script type="text/javascript">
		             var  address = " ${po.ADDRESS}";
		             document.write(address.substr(0,10));
		         </script>
		       </a>
		     </td>
		      <td align="center">
		       <a href="#" title="${po.THIS_ADDRESS}" >
		         <script type="text/javascript">
		             var  address = "${po.THIS_ADDRESS}";
		             if(address.length>10){
		            	 address =address.substr(0,10)+"..";
		             }
		             document.write(address);
		         </script>
		       </a>
		      </td>
		      
		    </tr>
    	</c:forEach>
	</table>
	<br />
	<table class=table_query>
		<tr>
        	<th nowrap="" align="left" colspan="6"><img src="<%=request.getContextPath()%>/img/subNav.gif" class="nav"> 物流信息维护</th>
        </tr>
		<tr class=cssTable>
			<td class="right" nowrap="nowrap">车牌号：</td>
			<td align="left">
				<input type="text" maxlength="20"  name="cardNo" value="${chepaiNo }" maxlength="10" datatype="1,is_null,30" maxlength="30" id="cardNo" class="middle_txt"/><span style="PADDING-LEFT: 2px; WIDTH: 7px; HEIGHT: 18px; COLOR: red; FONT-SIZE: 9pt">*</span>
			</td>
			<td class="right">司机：</td>
			<td align="left"><input type="text" maxlength="20"  maxlength="20" value="${siji }" datatype="1,is_null,30" maxlength="30"  name="Driver" id="Driver" class="middle_txt"/><span style="PADDING-LEFT: 2px; WIDTH: 7px; HEIGHT: 18px; COLOR: red; FONT-SIZE: 9pt">*</span></td>
			<td class="right" nowrap="nowrap">联系方式：</td>
			<td align="left">
				<input type="text" maxlength="20"  maxlength="30" name="Contact" value="${tel }" datatype="1,is_phone,30" maxlength="30" id="Contact" class="middle_txt"/><span style="PADDING-LEFT: 2px; WIDTH: 7px; HEIGHT: 18px; COLOR: red; FONT-SIZE: 9pt">*</span>
			</td>
		</tr>
		<tr class=cssTable >
			<td colspan="6" class="table_query_4Col_input" style="text-align: center">
			
				<input type="hidden" name="billId" id="billId" value="${billId}"/>
				
				<input type="button" name="button1" class="normal_btn" onclick="confirmAdd();" value="保存物流信息" id="queryBtn1" />
				<input class="normal_btn" name="button2" type="button" onclick="materialShow();" value ="添加位置" id="queryBtn2"/>
				<input type="button" name="button3" class="normal_btn" onclick="showUpload();" value="批量导入位置/交车" id="queryBtn3"/>
				<input type="button" name="button3" class="normal_btn" onclick="toBack();" value="返回" id="queryBtn3" /> 
			</td>
		</tr>
	</table>
	<br/>
	<table class="table_edit" id="uploadTable"  style="display: none">
    	<tr>
        	<td>
        		<font color="red">
                    <input type="button" class="normal_btn" value="模版下载" onclick="exportVenderTemplate()"/>文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;
                </font>
                    <input type="file" name="uploadFile" style="width: 250px" id="uploadFile" value=""/> &nbsp;
                    <input type="button" id="upbtn" class="normal_btn" value="确定" onclick="uploadExcel()"/>
            </td>
        </tr>
    </table>
    <br/>
	<table class=table_query id="materialShow"  style="display: none" width="100%">
    	<tr>
        	<th nowrap="" align="left" colspan="6"><img src="<%=request.getContextPath()%>/img/subNav.gif" class="nav">地址上报</th>
        </tr>
    	<tr>
		<td class="right">位置：</td>
		<td width="80%"><textarea maxlength="1000" name="address" id="address" datatype="0,is_null" class="form-control" style="width: 95%"></textarea></td>
    	<td width="10%" align="left">
           <input type="button" id="upbtn" class="normal_btn" value="确定" onclick="subaddress();"/>
        </td>
        </tr>
    </table>
    <br>
</form>
<script type="text/javascript">
	$J(document).ready(function(){ 
		if($J("#errcode").val() !=""){
			OpenHtmlWindow('<%=request.getContextPath()%>/jsp/sales/storage/sendmanage/commpr/show_error_import_vin.jsp',800,300);
		}
	});
	//提交校验
	function confirmAdd(){
		var cardNo = document.getElementById("cardNo").value;
		if(cardNo==""){
			MyAlert("请输入车牌号!");
			return false;
		}
		
		var Driver = document.getElementById("Driver").value;
		if(Driver==""){
			MyAlert("请输入司机!");
			return false;
		}
		var Contact = document.getElementById("Contact").value;
		if(Contact==""){
			MyAlert("请输入联系方式!");
			return false;
		}
		MyConfirm("确认提交?",toAdd);
	}
	//提交
	function toAdd(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/storage/sendmanage/CommprManage/saveInfo.json',showResult,'fm');
	}
	//全选
	function checkAll(obj){
		var che = $J(obj).attr("checked");
		if(che=="checked"){
			$J("input[name='DTL_ID']").each(function(){
				if(!$J(this).attr("disabled")){
				   $J(this).attr("checked",true);
				}
			});	
		}else{
			$J("input[name='DTL_ID']").each(function(){
				$J(this).attr("checked",false);
			});	
		}
	}
	//回调方法
	function showResult(json){
		if(json.returnValue == '1'){
			MyAlert("保存成功!");
			//fm.action='<%=request.getContextPath()%>/sales/ordermanage/orderaudit/SpecialNeedPriceCheck/specialNeedPriceCheckdqInit.do';
			//fm.submit();
		}else{
			MyAlert("保存失败,请联系系统管理员!");
		}
	}
	
	function toDetailCheck(value,billId){
		if(confirm("确定删除?")){
			var url = "<%=request.getContextPath()%>/sales/storage/sendmanage/CommprManage/delInfo.json?detailId=" + value+"&billId="+billId;
			sendAjax(url,getResult,'fm');
		}
	}
	//回调方法
	function getResult(json){
		var billId=json.billId;
		if(json.returnValue == '1'){
			MyAlert("删除成功!");
			fm.action='<%=request.getContextPath()%>/sales/storage/sendmanage/CommprManage/locationMaintainChInit.do?billId='+billId;
			fm.submit();
		}else{
			MyAlert("删除失败,请联系系统管理员!");
			return;
		}
	}

	//弹出层用于添加位置
	function materialShow(){
		 var len = $J("input[name='DTL_ID']:checked").length;
		 if(len==0){
			 MyAlert("请至少选择一条记录添加位置信息！");
			 return;
		 }
		 var dtl_ids = "";
		 $J("input[name='DTL_ID']:checked").each(function(){
			 dtl_ids = dtl_ids  +$J(this).val() +",";
		 });
		 dtl_ids = dtl_ids.substring(0,dtl_ids.length-1);
		var billId = document.getElementById("billId").value;
// 		document.getElementById("dtl_ids").vaule=dtl_ids;
		$J("#dtl_ids").val(dtl_ids);
		if(document.getElementById("materialShow").style.display == "none"){
	        document.getElementById("materialShow").style.display = "block";
	    }else {
	        document.getElementById("materialShow").style.display = "none";
	    }
	}
	
	function  subaddress(){
		var address = document.getElementById("address").value;
		if (address == "") {
			MyAlert("请填写位置!");
			return;
		}
		
		MyConfirm("确定添加位置？", editSubmitActions);
	}

	function editSubmitActions() {
		makeNomalFormCall("<%=request.getContextPath()%>/sales/storage/sendmanage/OnTheWayAction/saveAddAddress.json",showValue,'fm');
	}
	function showValue(json){
		var ok = json.OK;
		if (ok == 1) {
			MyAlertForFun("保存成功！",function(){
				window.location.reload();
			});
		} else {
			var errinfo = json.errinfo;
			if ("" != errinfo) {
				MyAlert("勾选的车辆存在未绑定的车辆，请先绑定再上报位置！");
			}
		}
	}
	//返回
	function toBack(){
		window.location.href = '<%=request.getContextPath()%>/sales/storage/sendmanage/CommprManage/locationMaintainInit.do';
	}

	/**
	function fromwordInit(billId){
		window.location.reload();
	}
	*/
	function showUpload(){
	    if(document.getElementById("uploadTable").style.display == "none"){
	        document.getElementById("uploadTable").style.display = "block";
	    }else {
	        document.getElementById("uploadTable").style.display = "none";
	    }
	}
	//下载模板
	function exportVenderTemplate() {
	    fm.action = "<%=request.getContextPath()%>/sales/storage/sendmanage/OnTheWayAction/exportVenderTemplate.do";
	    fm.submit();
	}
	//通过excel导入供应商信息
	function uploadExcel() {
	    var fileValue = document.getElementById("uploadFile").value;

	    if (fileValue == "") {
	        MyAlert("请选择文件!");
	        return;
	    }
	    var fi = fileValue.substring(fileValue.length - 3, fileValue.length);
	    if (fi != 'xls') {
	        MyAlert('导入文件格式不对,请导入xls文件格式');
	        return false;
	    }
	    
	    MyConfirm("确定导入吗？",impVin);
	}
	
	function impVin() {
		var billId = document.getElementById("billId").value;
    	fm.action = "<%=request.getContextPath()%>/sales/storage/sendmanage/OnTheWayAction/uploadVenderExcel.do?&billId="+billId;
        fm.submit();
	}

	//回调方法
	function getResult22(json){
		MyAlert(json.Exception.message);
		var billId=json.billId;
		if(json.returnValue == '1'){
			MyAlert("导入成功!");
			fm.action='<%=request.getContextPath()%>/sales/storage/sendmanage/CommprManage/locationMaintainChInit.do?billId='+billId;
			fm.submit();
		}else{
			MyAlert("导入失败,请联系系统管理员!");
			return;
		}
	}
	
	function showAddress(dtl_id){
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/storage/sendmanage/OnTheWayAction/showOntheWayAddress.do?dtl_id='+dtl_id,800,300);
	}
	
	function showlog(dtl_id){
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/storage/sendmanage/OnTheWayAction/showbindCarlog.do?dtl_id='+dtl_id,800,300);
	}
</script>
</body>
</html>