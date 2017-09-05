<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import="com.infodms.dms.util.CommonUtils" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.text.DecimalFormat"%>
<style media=print>
/* 应用这个样式的在打印时隐藏 */
.Noprint {
	display: none;
}

/* 应用这个样式的，从那个标签结束开始另算一页，之后在遇到再起一页，以此类推 */
hr {
	page-break-after: always;
}
</style>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>特殊费用打印</title>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
   		doCusChange();
	}

</script>
</head>
<body onload="doInit()">
	<object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
	<form name="fm" id="fm">
	<div style="font-weight: bold;" align="center"><font size="4">北汽幻速质量保证服务结算单( 特殊费用)</font></div>
	<br />
	<table class="tab_printsep2" align="center">
	  <tr>
	    <td width="16%">经销商代码：</td>
	    <td width="17%">${map.DEALER_CODE}</td>
	    <td width="16%">经销商名称：</td>
	    <td width="51%" colspan="3">${map.DEALER_NAME}</td>
	  </tr>
	  <tr>
	    <td>索赔单类型：</td>
	    <td >特殊费用</td>
	    <td>索赔单号：</td>
	    <td width="17%">${map.FEE_NO}</td>
	    <td width="16%">车系：</td>
	    <td width="18%">${map.SERIES_NAME}</td>
	  </tr>
	  <tr>
	    <td>费用类型：</td>
	    <td>${map.FEE_TYPE}</td>
	    <td>费用名称：</td>
	    <td><script type='text/javascript'>
					var name=getItemValue('<c:out value="${map.BALANCE_FEE_TYPE}"/>');
					document.write(name) ;
				</script></td>
	    <td>车辆生产厂家：</td>
	    <td>${map.YIELD}</td>
	  </tr>
	  <tr>
	    <td>车型：</td>
	    <td >${map.MODEL_NAME}</td>
	    <td>车型状态：</td>
	    <td>${map.PACKAGE_NAME}</td>
	    <td>购车日期：</td>
	    <td>${map.PRODUCT_DATE}</td>
	  </tr>
	  <tr>
	    <td>VIN：</td>
	    <td >${map.VIN}</td>
	    <td>发动机号：</td>
	    <td>${map.ENGINE_NO}</td>
	    <td>牌照号：</td>
	    <td>${map.LICENSE_NO}</td>
	  </tr>
	  <tr>
	    <td>联系人电话：</td>
	    <td >${map.LINKMAN_TEL}</td>
	    <td>联系姓名：</td>
	    <td>${map.LINKMAN}</td>
	    <td>出厂日期：</td>
	    <td>${map.FACTORY_DATE}</td>
	  </tr>
	  <tr>
	    <td>用户电话：</td>
	    <td >${map.MAIN_PHONE}</td>
	    <td>用户姓名：</td>
	    <td>${map.CUS_NAME}</td>
	    <td>上报费用日期：</td>
	    <td>${map.MAKE_DATE}</td>
	  </tr>
	  <tr>
	    <td>主因件代码：</td>
	    <td >${map.PART_CODE}</td>
	    <td>主因件名称：</td>
	    <td>${map.PART_NAME}</td>
	    <td>制造商名称：</td>
	    <td>${map.SUPPLIER_NAME}</td>
	  </tr>
	  <tr>
	    <td>授权时间：</td>
	    <td >${map.APP_DATE}</td>
	    <td>行驶里程(km)：</td>
	    <td>${map.MILEAGE}</td>
	    <td>维修次数：</td>
	    <td>${map.TOTAL}</td>
	  </tr>
	  <tr>
	    <td>装配代码：</td>
	    <td colspan="5" >${map.MAT_NAME}</td>
	  </tr>
	  <tr>
	    <td>用户地址：</td>
	    <td colspan="5" >${map.ADDRESS}</td>
	  </tr>
	  <tr>
	    <td>故障现象描述：</td>
	    <td colspan="5" ></td>
	  </tr>
	  <tr>
	    <td>故障原因：</td>
	    <td colspan="5" ></td>
	  </tr>
	  <tr>
	    <td>维修措施：</td>
	    <td colspan="5" ></td>
	  </tr>
	  <tr>
	    <td>申报费用原因：</td>
	    <td colspan="5" >${map.REMARK}　</td>
	  </tr>
	  <tr>
	    <td>最终授权人：</td>
	    <td ><script type='text/javascript'>
					var name=getItemValue('<c:out value="${map.O_STATUS}"/>');
					document.write(name) ;
				</script></td>
	    <td>总申报工时费：</td>
	    <td>${map.CL_AMOUNT}</td>
	    <td>总申报材料费：</td>
	    <td>${map.LF_AMOUNT}</td>
	  </tr>
	  <tr>
	    <td>总申报费用：</td>
	    <td >${map.AMOUNT}</td>
	    <td>总审核费用：</td>
	    <td>${map.DECLARE_SUM }</td>
	    <td>结算厂家：</td>
	    <td>${map.YIELD}</td>
	  </tr>
	</table>
	<center>
	<!--  ---------------   活动结束                           ---------------- -->
    <div style="width: 800px;">
      <div style="height: 5px;">&nbsp;</div>
      <tfoot style="display: table-footer-group;">
      <table width="100%">
        <tr>
          <td width="33%" class="tdpvoid" align="left">服务站盖章:</td>
          <td width="25%" class="tdpvoid" align="center">鉴定员: </td>
          <td width="17%" class="tdpvoid" align="center">&nbsp;</td>
          <td width="24%" class="tdpvoid" colspan="4" align="left">客户签字:</td>
        </tr>
      </table>
      </tfoot>
    </div>
	
	<table width="100%" cellpadding="1"  class="Noprint">
		<tr>
			<td width="100%" height="25" colspan="3"><div id="kpr"
					align="center">
					<input class="ipt" type="button" value="打印"
						onclick="javascript:printit();" /> <input class="ipt"
						type="button" value="打印页面设置" onclick="javascript:printsetup();" />
					<input class="ipt" type="button" value="打印预览"
						onclick="javascript:printpreview();" />
			</td>
		</tr>
	</table>
	</center>
	</form>
	<script language="javascript">
		function printsetup() {
			wb.execwb(8, 1); // 打印页面设置 
		}
		function printpreview() {
			wb.execwb(7, 1); // 打印页面预览       
		}
		function printit() {
			if (confirm('确定打印吗？')) {
				wb.execwb(6, 6)
			}
		}
	</script>
</body>
</html>

