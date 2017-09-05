<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
    String contextPath = request.getContextPath();
%>
<title>封存查询</title>
<script type="text/javascript">
$(function(){
	stockSearch();
});
var myPage;
var url = "";
var title = null;
var columns = null;
function stockSearch(){
	url = "<%=contextPath%>/parts/baseManager/partBaseQuery/partFCStockQuery/partFCStockAction/partFCStockSearch.json";
      var searchType = document.getElementById("searchType").value;
//       var headerName = "";
//       if('${parentOrgId}' != ''){
//       	if('${parentOrgId}' == '${oemOrgId}'){
//       		headerName = "计划价(元)";
//       	}else{
//       		headerName = "采购价(元)";
//       	}
//       }
      
      if("normal" == searchType){
      	columns = [
      	           {header: "序号", dataIndex: 'PART_ID', renderer: getIndex,   style: 'text-align:left'},
      	           {header: "仓库", dataIndex: 'WH_NAME',   style: 'text-align:left'},
      	           {header: "配件编码", dataIndex: 'PART_OLDCODE',   style: 'text-align:left'},
      	           {header: "配件名称", dataIndex: 'PART_CNAME',   style: 'text-align:left'},
      	           //{header: "件号", dataIndex: 'PART_CODE',   style: 'text-align:left'},
      	           {header: "单位", dataIndex: 'UNIT'},
      	           {header: "货位", dataIndex: 'LOC_NAME',   style: 'text-align:left'},
      	           {header: "可用库存", dataIndex: 'NORMAL_QTY',   style: 'text-align:center'},
      	           {header: "占用库存", dataIndex: 'BOOKED_QTY',   style: 'text-align:center'},
//       	           {header: "来货错误", dataIndex: 'LHCW',   style: 'text-align:center'},
//       	           {header: "质量问题", dataIndex: 'ZLWT',   style: 'text-align:center'},
//       	           {header: "借条处理", dataIndex: 'JTCL',   style: 'text-align:center'},
      	           {header: "现场BO", dataIndex: 'XCBO',   style: 'text-align:center'},
      	           {header: "普通封存", dataIndex: 'PTFC',   style: 'text-align:center'},
      	           {header: "盘亏封存", dataIndex: 'PKFC_QTY',   style: 'text-align:center'},
      	           {header: "账面库存", dataIndex: 'ITEM_QTY',   style: 'text-align:center'},
      	           {header: "盘盈封存", dataIndex: 'PDFC_QTY',   style: 'text-align:center'},
      	           /* {header: "采购价(元)", dataIndex: 'PRICE',   style: 'text-align: right;'},2170830封存 */
      	           {header: "是否锁定", dataIndex: 'IS_LOCKED',   style: 'text-align:center', renderer: getLockState},
      	           {header: "是否有效", dataIndex: 'PDSTATE', style:'text-align:center',renderer:getItemValue}
      	       ];
      }else{
      	var code = document.getElementById("dealerCode").value;
  		var name = document.getElementById("dealerName").value;
  		if("" == code && "" == name)
  		{
  	//		MyAlert("请先至少填写服务商编码或服务商名称一个条件!");
  	//		return false;
  		}
      	columns = [
      	           {header: "序号", dataIndex: 'PART_ID', renderer: getIndex,   style: 'text-align:left'},
      	           {header: "服务商编码", dataIndex: 'DEALER_CODE',   style: 'text-align:left'},
      	           {header: "服务商名称", dataIndex: 'DEALER_NAME',   style: 'text-align:left'},
      	           {header: "仓库", dataIndex: 'WH_NAME',   style: 'text-align:left'},
      	           {header: "配件编码", dataIndex: 'PART_OLDCODE',   style: 'text-align:left'},
      	           {header: "配件名称", dataIndex: 'PART_CNAME',   style: 'text-align:left'},
      	           //{header: "件号", dataIndex: 'PART_CODE',   style: 'text-align:left'},
      	           {header: "单位", dataIndex: 'UNIT'},
      	           {header: "货位", dataIndex: 'LOC_NAME',   style: 'text-align:left'},
      	           {header: "可用库存", dataIndex: 'NORMAL_QTY',   style: 'text-align:center'},
      	           {header: "占用库存", dataIndex: 'BOOKED_QTY',   style: 'text-align:center', renderer: getZYLink},
      	           {header: "正常封存", dataIndex: 'ZCFC_QTY',   style: 'text-align:center', renderer: getZCLink},
      	           {header: "账面库存", dataIndex: 'ITEM_QTY',   style: 'text-align:center'},
      	           {header: "盘点封存", dataIndex: 'PDFC_QTY',   style: 'text-align:center', renderer: getPDLink},
      	           /* {header: "采购价(元)", dataIndex: 'PRICE',   style: 'text-align: right;'},20170830屏蔽 */
      	           {header: "是否锁定", dataIndex: 'IS_LOCKED',   style: 'text-align:center', renderer: getLockState},
      	           {header: "是否有效", dataIndex: 'PDSTATE', style:'text-align:center',renderer:getItemValue}
      	       ];
      }
      __extQuery__(1);
  }

  //获取锁定状态
  function getLockState(value, meta, record) {
      var lockState = "1";
      var str = "否";
      if (lockState == value) {
          str = "是";
          return String.format("<font color='red'>" + str + "</font>");
      }
      return String.format(str);
  }

  //返回盘点封存链接
  function getPDLink(value, meta, record) {
      var partId = record.data.PART_ID;
      var whId = record.data.WH_ID;
      var viewPage = "pdPage";
      return String.format("<a href=\"#\" onclick='showPDDetail(\"" + partId + "\",\"" + whId + "\",\"" + viewPage + "\")'>" + value + "</a>");
  }

  //返回正常封存链接
  function getZCLink(value, meta, record) {
      var partId = record.data.PART_ID;
      var whId = record.data.WH_ID;
      var viewPage = "zcPage";
      return String.format("<a href=\"#\" onclick='showPDDetail(\"" + partId + "\",\"" + whId + "\",\"" + viewPage + "\")'>" + value + "</a>");
  }

//返回占用明细链接
  function getZYLink(value, meta, record) {
      var partId = record.data.PART_ID;
      var whId = record.data.WH_ID;
      var viewPage = "zyPage";
      return String.format("<a href=\"#\" onclick='showPDDetail(\"" + partId + "\",\"" + whId + "\",\"" + viewPage + "\")'>" + value + "</a>");
  }
//返回详情链接
  function myLink(value, meta, record) {
      var partId = record.data.PART_ID;
      var whId = record.data.WH_ID;
      var inPage = "inPage";
      var outPage = "outPage";
      return String.format("<a href=\"#\" onclick='showPDDetail(\"" + partId + "\",\"" + whId + "\",\"" + inPage + "\")'>[入库明细]</a>&nbsp;<a href=\"#\" onclick='showPDDetail(\"" + partId + "\",\"" + whId + "\",\"" + outPage + "\")'>[出库明细]</a>");
  }

  //显示盘点封存详情
  function showPDDetail(partId, whId, viewPage) {
      OpenHtmlWindow('<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/showPDDetInit.do?partId=' + partId + '&whId=' + whId + '&viewPage=' + viewPage, 950, 500);
}

function setSearchType(obj){
	 var searchTypeObj = document.getElementById("searchType");
	 var dealerCodeObj = document.getElementById("dealerCode");
	 var dealerNameObj = document.getElementById("dealerName");
	 var whObj = document.getElementById("whId");
	 if(obj.checked){
		 searchTypeObj.value = "specific";
		 dealerCodeObj.disabled = "";
		 dealerCodeObj.style.cssText = "background-color: #FFFFFF;";
		 dealerNameObj.disabled = "";
		 dealerNameObj.style.cssText = "background-color: #FFFFFF;";
		 whObj.disabled = "disabled";
	 }else{
		 searchTypeObj.value = "normal";
		 dealerCodeObj.disabled = "disabled";
		 dealerCodeObj.style.cssText = "background-color: #EAEAEA;";
		 dealerNameObj.disabled = "disabled";
		 dealerNameObj.style.cssText = "background-color: #EAEAEA;";
		 whObj.disabled = "";
	 } 
}

//下载
function exportPartStockExcel() {
    document.fm.action = "<%=contextPath%>/parts/baseManager/partBaseQuery/partFCStockQuery/partFCStockAction/exportPartStockStatusExcel.do";
    document.fm.target = "_self";
    document.fm.submit();
}

function expPartStockAmountExcel(){
	document.fm.action = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/expPartStockAmountExcel.do";
    document.fm.target = "_self";
    document.fm.submit();
}

</script>

</head>
<body>
	<form method="post" name="fm" id="fm">
		<div class="wbox">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置： 配件管理 &gt; 基础信息管理 &gt; 配件基础信息查询 &gt; 配件封存查询
				<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
				<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
				<input type="hidden" name="companyName" id="companyName" value="${companyName }" />
				<input type="hidden" name="searchType" id="searchType" value="normal" />
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query" id="normalQuery">
						<tr>
							<td class="right">配件编码：</td>
							<td>
								<input class="middle_txt" type="text" name="partOldcode" id="partOldcode" />
							</td>
							<td class="right">配件名称：</td>
							<td>
								<input class="middle_txt" type="text" name="partCname" id="partCname" />
							</td>
							<td class="right">仓库：</td>
							<td>
								<select name="whId" id="whId" class="u-select" onchange="stockSearch()">
									<%-- <option value="">-请选择-</option>--%>
									<c:if test="${WHList!=null}">
										<c:forEach items="${WHList}" var="list">
											<option value="${list.WH_ID }">${list.WH_CNAME }</option>
										</c:forEach>
									</c:if>
								</select>
							</td>
						</tr>
						<tr>
							<!-- <td class="right">件号：</td>
                <td>
                    <input class="middle_txt" type="text" name="partCode" id="partCode"/>
                </td>
                 -->
							<td class="right">是否锁定：</td>
							<td>
								<select name="isLocked" id="isLocked" class="u-select" onchange="stockSearch()">
									<option value="">-请选择-</option>
									<option value="1">是</option>
									<option value="0">否</option>
								</select>
							</td>
							<td width="10%" class="right">是否有效：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("STATE", <%=Constant.STATUS%>,"", true, "u-select","onchange='stockSearch()'","false", '');
								</script>
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="stockSearch()" />
								<input class="u-button" type="reset" value="重 置">
								<input class="u-button" type="button" value="导 出" onclick="exportPartStockExcel()" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<!-- 查询条件 end -->
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
	</form>
	</div>
</body>
</html>