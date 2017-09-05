<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title>配件销售单查看</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">
	//获取选择框的值
	function getCode(value){
		var str = getItemValue(value);
		document.write(str);
	}
	//获取序号
	function getIdx(){
		document.write(document.getElementById("file").rows.length-2);
	}
	//获取序号
	function getIdx1(){
		document.write(document.getElementById("file1").rows.length-2);
	}
	//返回
	function goBack(){
		window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSalesOrderFinCheck/partSalesOrderFinCheckInit.do?flag=true';
	}
</script>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置:
		配件管理 > 配件销售管理  &gt;装箱单管理 >查看</div>
  <table class="table_query">
    <tr>
    <th colspan="6"><img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 销售单信息</th>
  </tr>
    <tr>
      <td width="13%"   align="right">销售单号:</td>
      <td width="20%" >${mainMap.SO_CODE}</td>
      <td width="13%"   align="right">销售日期:</td>
      <td width="20%">${mainMap.CREATE_DATE}</td>
      <td width="13%"   align="right">销售制单人:</td>
      <td width="20%">${mainMap.CREATE_BY_NAME}</td>
    </tr>
    <tr>
      <td align="right">销售单位:</td>
      <td>${mainMap.SELLER_NAME}</td>
      <td align="right">订货单位:</td>
      <td>${mainMap.DEALER_NAME}</td>
      <td align="right">订货制单人:</td>
      <td >${mainMap.BUYER_NAME}</td>
    </tr>
    <tr>
      <td   align="right">出库仓库:</td>
	      <td>
	      	${mainMap.WH_NAME}
	      </td>
      <td   align="right">接收单位:</td>
      <td>${mainMap.CONSIGNEES}</td>
       <td   align="right">&NBSP;</td>
      <td>&NBSP;</td>
    </tr>
    <tr>
      <td   align="right">接收地址:</td>
      <td>${mainMap.ADDR}</td>
      <td   align="right"> 接收人:</td>
      <td>${mainMap.RECEIVER}</td>
      <td   align="right"><span> 接收人电话:</span></td>
      <td>${mainMap.TEL}</td>
    </tr>
    <tr>
      <td   align="right">邮政编码:</td>
      <td>${mainMap.POST_CODE}</td>
      <td   align="right">到站名称:</td>
      <td>${mainMap.STATION}</td>
      <td   align="right">发运方式:</td>
      <td>
      	<script type="text/javascript">
				getCode(${mainMap.TRANS_TYPE});
		</script>
      </td>
    </tr>
    <tr>
      <td   align="right">付款方式:</td>
      <td>
		<script type="text/javascript">
				getCode(${mainMap.PAY_TYPE});
		</script>
	  </td>
      <td   align="right">订单类型:</td>
      <td>
      	<script type="text/javascript">
				getCode(${mainMap.ORDER_TYPE});
		</script>
      </td>
      <td   align="right">总金额:</td>
      <td>${mainMap.AMOUNT}</td>
    </tr>
    <tr>
      <td   align="right">运费支付方式:</td>
      <td>
      	<script type="text/javascript">
				getCode(${mainMap.TRANSPAY_TYPE});
		</script>
	  </td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
  <tr>
    <td   align="right">备注:</td>
    <td colspan="5">${mainMap.REMARK}</td>
  </tr>
</table>
  <table id="file" class="table_list" style="border-bottom:1px solid #DAE0EE">
    <tr>
      <th colspan="12" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />配件信息        </th>
    </tr>
    <tr bgcolor="#FFFFCC" >
      <td>序号</td>
      <td>件号</td>
      <td>配件编码</td>
      <td>配件名称</td>
      <td>最小包装量 </td>
      <td>单位 </td>
      <td>采购数量</td>
      <td>销售数量</td>
      <td>销售单价</td>
      <td>销售金额</td>
      <td>当前库存</td>
      <td>备注</td>
    </tr>
     <c:forEach items="${detailList}" var="data" >
     	<tr class="table_list_row1">
   			  <td align="center">&nbsp;
   			  	<script type="text/javascript">
       				getIdx();
				</script>
   			  </td>
   			  <td align="center"><c:out value="${data.PART_CODE}" /></td>
		      <td align="center"><c:out value="${data.PART_OLDCODE}" /></td>
		      <td align="center"><c:out value="${data.PART_CNAME}" /></td>
		      <td>&nbsp;<c:out value="${data.MIN_PACKAGE}" /></td>
		      <td>&nbsp;<c:out value="${data.UNIT}" /></td>
		      <td>&nbsp;<c:out value="${data.BUY_QTY}" /></td>
		      <td>&nbsp;<c:out value="${data.SALES_QTY}" /></td>
		      <td>&nbsp;<c:out value="${data.BUY_PRICE}" /></td>
		      <td>&nbsp;<c:out value="${data.BUY_AMOUNT}" /></td>
		      <td>&nbsp;<c:out value="${data.STOCK_QTY}" /></td>
		      <td>&nbsp;<c:out value="${data.REMARK}" /></td>
		 </tr>
	</c:forEach>
  </table>
<table id="file1" class="table_list" style="border-bottom:1px solid #DAE0EE">
    <tr>
      <th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />单据操作信息        </th>
    </tr>
    <tr bgcolor="#FFFFCC">
      <td align="center" width="3%">序号</td>
      <td align="center" width="10%">操作人</td>
      <td align="center" width="13%">操作时间</td>
      <td align="center" width="14%">环节</td>
      <td align="center" width="8%">状态</td>
    </tr>
    <c:forEach items="${historyList}" var="data" >
     	<tr class="table_list_row1">
   			  <td align="center">&nbsp;
   			  	<script type="text/javascript">
       				getIdx();
				</script>
   			  </td>
   			  <td align="center"><c:out value="${data.OPT_NAME}" /></td>
		      <td align="center"><c:out value="${data.OPT_DATE}" /></td>
		      <td align="center"><c:out value="${data.WHAT}" /></td>
		      <td>&nbsp;
				<script type="text/javascript">
					getCode(${data.STATUS});
	  			</script>
			  </td>
		</tr>
	</c:forEach>
  </table>
<table border="0" class="table_query">
  <tr align="center">
  <td><input class="normal_btn" type="button" value="返 回" onclick="history.back()"/></td>
  </tr>
  </table>
</div>
</body>
</html>

