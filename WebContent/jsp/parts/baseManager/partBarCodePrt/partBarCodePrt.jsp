<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件条码打印</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <OBJECT ID="tecPrint" style="display:none"
            CLASSID="CLSID:AAB8479E-F25B-4C99-944C-5D9AEE4591A1"
            CODEBASE="<%=request.getContextPath()%>/jsp/ocx/tecBPrint.CAB#version=1,0,0,0">
    </OBJECT>
<script language="javascript" type="text/javascript">
    var barPrint = document.getElementById("tecPrint");
	 //选择
	 function sel1() {
	     OpenHtmlWindow('<%=contextPath%>/parts/baseManager/partBarCodePrt/partBarCodePrtAction/partQuery.do', 700, 520);
	 }
	
	 function sel2() {
		 var partId = $('PART_DATA').value;//配件编码ID
		 if(partId==""){
             MyAlert("请选择配件！");
             return false;
         }
	     OpenHtmlWindow('<%=contextPath%>/parts/baseManager/partBarCodePrt/partBarCodePrtAction/venderQuery.do?partId='+partId, 700, 520);
	 }

	 function printOrder(partId,barCode) {

		 var partId = $('#PART_DATA')[0].value;//配件编码ID
		 var venderId = $('#PART_DATA2')[0].value;//供应商ID
		 var WH_ID = $('#WH_ID')[0].value;//仓库ID

         if(partId==""){
             MyAlert("请选择配件！");
             return false;
         }
         if(venderId==""){
             MyAlert("请选择供应商!");
             return false;
         }
         if(WH_ID==""){
             MyAlert("请选择库房!");
             return false;
         }
         if($('prt_number').value ==""){
             MyAlert("打印张数必填!");
             return false;
         }
         if(confirm("确认打印?")){
		 var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderInManager/selectPartIdByPrint.json?partId=' + partId+'&venderId='+venderId+'&WH_ID='+WH_ID;
	     sendAjax(url, getResult, 'fm');
         }
	 }

	 function getResult(jsonObj) {
		    var vpoldcode = jsonObj.vpoldcode==null?"":jsonObj.vpoldcode;
		    var vpCname = jsonObj.vpCname==null?"":jsonObj.vpCname;
		    var vpEname = jsonObj.vpEname==null?"":jsonObj.vpEname;
		    var vpCode = jsonObj.vpCode==null?"":jsonObj.vpCode;
		    var vBarCode = jsonObj.barCode==null?"":jsonObj.barCode;
		    var vQty = 0;
			var start = $('#prt_number')[0].value;

			var keyMan = document.getElementsByName("keyMan");
			for(var i=0; i<keyMan.length; i++){
				if(keyMan[i].checked){
					if(keyMan[i].value==1){
						//$('min_package1').disabled="disabled";
						//$('min_package').disabled="";
						vQty = $('min_package').value;
					}
					if(keyMan[i].value==2){
						//$('min_package').disabled="disabled";
						//$('min_package1').disabled="";
						vQty = $('min_package1').value;
					}
				}
			}
		    	doPrint(vBarCode,vpoldcode,vpCname,vpEname,vpCode,vQty,padLeft(start,4));
		}

	 function doPrint(vBarCode, vpoldcode, vpCname, vpEname, vpCode, vQty, vpNum) {
	    //MyAlert(vpoldcode + ":" + vpCname + ":" + vpEname + ":" + vpCode + ":" + vQty + ":" + vpNum + ":" + vBarCode);
	    try {
	        // 注意：打印份数以0001,0002,0003的方式输入
	        //s.bPrint('条码号','编号','中文名称','英文名称','图号','数量','打印份数');
	        barPrint.bPrint(vBarCode, vpoldcode, vpCname, vpEname, vpCode, vQty, vpNum);
	    } catch (e) {
	    	//MyAlert(vpoldcode + ":" + vpCname + ":" + vpEname + ":" + vpCode + ":" + vQty + ":" + vpNum + ":" + vBarCode);
	        MyAlert('控件调用失败!');
	    }
	 }

	 function padLeft(str,lenght){
		    if(str.length >= lenght)
		        return str;
		    else
		        return padLeft("0" +str,lenght);
	 }

	 //zhumingwei 2013-11-18 手动填写配件的时候点击回车自动带出相关信息
	 window.document.onkeydown = function (){
		if(event.keyCode==13){
			var PART_OLDCODE= $('#PART_OLDCODE')[0].value;
			var WH_ID= $('#WH_ID')[0].value;
			var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderInManager/selectPartIdBy.json?PART_OLDCODE=' + PART_OLDCODE+'&WH_ID='+WH_ID;
		    sendAjax(url, getResult11, 'fm');
		};
	}
	 function getResult11(jsonObj) {
		    var PART_DATA = jsonObj.PART_DATA==null?"":jsonObj.PART_DATA;
		    var PART_OLDCODE = jsonObj.PART_OLDCODE==null?"":jsonObj.PART_OLDCODE;
		    var normalQty = jsonObj.normalQty==null?"":jsonObj.normalQty;
		    var min_package = jsonObj.min_package==null?"":jsonObj.min_package;
		    var locCode = jsonObj.locCode==null?"":jsonObj.locCode;
		    $('#PART_DATA')[0].value=PART_DATA;
		    $('#PART_OLDCODE')[0].value=PART_OLDCODE;
		    $('#min_package')[0].value=min_package;
		    $('#normalQty')[0].value=normalQty;
		    $('#locCode')[0].value=locCode;
		}
</script>
</head>
<body>
<div class="wbox">
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：基础信息管理 &gt; 配件基础信息维护 &gt; 配件条码打印
	</div>
	<form name='fm' id='fm' method="post">
		<div class="form-panel">
			<h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif" /> 打印信息</h2>
			<div class="form-body">
				<table class="table_edit">
					<th colspan="6"></th>
					<tr>
						<td width="10%" align="right">配件编码：</td>
						<td width="20%">
						<input class="middle_txt" id="PART_OLDCODE" name="PART_OLDCODE" type="text"/>
						<!-- <input class="middle_txt" id="PART_OLDCODE" name="PART_OLDCODE" type="text" readonly="readonly"/> -->
						<input name="BUTTON" type="button" class="mini_btn" onclick="sel1()" value="..." />
						<input type="hidden" name="PART_DATA" id="PART_DATA" /><font color="red">*</font>
						</td>
						
						<td width="10%" align="right"><input type="radio" name="keyMan" name="keyMan" value="1" checked="checked"/>最小包装量：</td>
						<td width="20%">
						<input class="middle_txt" id="min_package" name="min_package" type="text" value=""/>
						</td>
						<td width="10%" align="right"><input type="radio" name="keyMan" value="2"/>整包发运量：</td>
						<td width="20%">
							<input class="middle_txt" id="min_package1" name="min_package1" type="text" value=""/>
						</td>
						<!-- 
						<td width="10%" align="right">批次：</td>
						<td width="20%"><input type="text" class="middle_txt" id="batch" name="batch" /></td>
						-->
					</tr>
					
					<tr>
						<td width="10%" align="right">可用库存量：</td>
						<td width="20%">
						<input class="middle_txt" id="normalQty" name="normalQty" type="text" value="" readonly="readonly"/>
						</td>
						
						<td width="10%" align="right">货位编码：</td>
						<td width="20%">
						<input class="middle_txt" id="locCode" name="locCode" type="text" value="" readonly="readonly"/>
						</td>
						<td width="10%" align="right">打印张数：</td>
						<td width="20%">
						<input class="middle_txt" type="text" id="prt_number" name="prt_number"  />
						</td>
						<!-- 
						<td width="10%" align="right">批次：</td>
						<td width="20%"><input type="text" class="middle_txt" id="batch" name="batch" /></td>
						-->
					</tr>
					
					<tr>
						<td width="10%" align="right">供应商名称：</td>
						<td width="20%">
						<input class="long_txt middle_txt" type="text" id="PART_VENDER" name="PART_VENDER" value="重庆北汽幻速汽车有限公司"/>
						<input name="BUTTON" type="button" class="mini_btn" onclick="sel2()" value="..." />
						<input type="hidden" name="PART_DATA2" id="PART_DATA2" value="2014021594183445"/><font color="red">*</font>
						</td>
						<td width="10%" align="right">库房：</td>
						<td width="20%">
							<select id="WH_ID" name="WH_ID" class="short_sel u-select">
							<%-- <option value="">-请选择-</option>--%>
								<c:forEach items="${wareHouses}" var="wareHouse">
									<option value="${wareHouse.whId }">${wareHouse.whName }</option>
								</c:forEach>
							</select>
						</td>
						<td width="10%" align="right"></td>
						<td width="20%"></td>
					</tr>
					
					<tr style="line-height:30px;">
					<td colspan="6" style="text-align: center;">
						<input type="button" name="prtBtn" id="prtBtn" value="打 印" class="normal_btn" onclick="printOrder();" />
					</td>
					</tr>
				</table>
			</div>
		</div>
	</form>
</div>
	
</body>
</html>
