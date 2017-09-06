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
<title>库存查询</title>
<script type="text/javascript">
	$(function(){
		stockSearch();
	});
    var myPage;
    var url = "";
    var title = null;
    var columns = null;
    function stockSearch() {
        url = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/stockQuerySearch.json";
        var searchType = document.getElementById("searchType").value;
        document.getElementById("sumFlag").value = "0";
        document.getElementById("groupQuery").style.cssText = "display: none;";
        document.getElementById("normalQuery").style.cssText = "display: block;";
        var heaerName = '';
        if('${parentOrgId}' != null){
        	if('${parentOrgId}' == '${oemOrgId}'){
        		heaerName = '计划价(元)';
        	}else{
        		heaerName = '采购价(元)';
        	}
        }
        if ("normal" == searchType) {
            columns = [
                {header: "序号", dataIndex: 'PART_ID', renderer: getIndex, style: 'text-align: center'},
                {id: 'action', header: "操作", sortable: false, dataIndex: 'PART_ID', renderer: myLink},
                {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: center', renderer: getFlag},
                {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: center'},
                //{header: "件号", dataIndex: 'PART_CODE',   style: 'text-align:left'},
                {header: "最小包装量", dataIndex: 'OEM_MIN_PKG'},
                {header: "单位", dataIndex: 'UNIT'},
                {header: "货位", dataIndex: 'LOC_NAME', renderer: getLCLink},
                //{header: "附属货位", dataIndex: 'SUB_LOC',   style: 'text-align:left'},
                {header: "可用库存", dataIndex: 'NORMAL_QTY', style: 'text-align: center'},
                {header: "占用库存", dataIndex: 'BOOKED_QTY_NEW', style: 'text-align:center', renderer: getZYLink},

                //{header: "正常封存", dataIndex: 'ZCFC_QTY',   style: 'text-align:center', renderer: getZCLink},
                //{header: "盘亏封存", dataIndex: 'PKFC_QTY',   style: 'text-align:center', renderer: getPDLink},
                {header: "账面库存", dataIndex: 'ITEM_QTY', style: 'text-align:center'},
                //{header: "盘盈封存", dataIndex: 'PDFC_QTY',   style: 'text-align:center', renderer: getPDLink},
                {header: "在途数量", dataIndex: 'ZT_QTY', style: 'text-align:center', renderer: getZTLink},
                //{header: "销售BO数量", dataIndex: 'BO_QTY',   style: 'text-align:center'},
//                 {header: heaerName, dataIndex: 'PRICE', style: 'text-align: right;' },//2170830 屏蔽
                {header: "是否锁定", dataIndex: 'IS_LOCKED', style: 'text-align:center', renderer: getLockState},
                {header: "是否有效", dataIndex: 'PDSTATE', style: 'text-align:center', renderer: getItemValue},
                {header: "仓库", dataIndex: 'WH_NAME', style: 'text-align:left'},
                {header: "配件备注", dataIndex: 'REMARK', style: 'text-align:left'}

            ];
        } else {
            var code = document.getElementById("dealerCode").value;
            var name = document.getElementById("dealerName").value;
            if ("" == code && "" == name) {
                //		MyAlert("请先至少填写经销商编码或经销商名称一个条件!");
                //		return false;
            }
            columns = [
                {header: "序号", dataIndex: 'PART_ID', renderer: getIndex, style: 'text-align: center'},
                {id: 'action', header: "操作", sortable: false, dataIndex: 'PART_ID', renderer: myLink},
                {header: "经销商编码", dataIndex: 'DEALER_CODE', style: 'text-align: center'},
                {header: "经销商名称", dataIndex: 'DEALER_NAME', style: 'text-align: center'},
                {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: center'},
                {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: center'},
                //{header: "件号", dataIndex: 'PART_CODE',   style: 'text-align: center'},
                {header: "最小包装量", dataIndex: 'OEM_MIN_PKG'},
                {header: "单位", dataIndex: 'UNIT'},
                {header: "货位", dataIndex: 'LOC_NAME', style: 'text-align: center'},
                {header: "可用库存", dataIndex: 'NORMAL_QTY', style: 'text-align:center'},
                {header: "占用库存", dataIndex: 'BOOKED_QTY_NEW', style: 'text-align:center'},
                //{header: "正常封存", dataIndex: 'ZCFC_QTY',   style: 'text-align:center'},
                //{header: "盘亏封存", dataIndex: 'PKFC_QTY',   style: 'text-align:center'},
                {header: "账面库存", dataIndex: 'ITEM_QTY', style: 'text-align:center'},
                //{header: "盘盈封存", dataIndex: 'PDFC_QTY',   style: 'text-align:center'},
                //{header: "BO数量", dataIndex: 'BO_QTY',   style: 'text-align:center'},
                {header: "采购价(元)", dataIndex: 'PRICE', style: 'text-align: right;'},
                {header: "是否锁定", dataIndex: 'IS_LOCKED', style: 'text-align:center', renderer: getLockState},
                {header: "是否有效", dataIndex: 'PDSTATE', style: 'text-align:center', renderer: getItemValue},
                {header: "仓库", dataIndex: 'WH_NAME', style: 'text-align: center'}

            ];
        }
        __extQuery__(1);
    }

    function stockAmountSearch() {
        url = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/stockGroupQuery.json";
        document.getElementById("normalQuery").style.cssText = "display: none;";
        document.getElementById("groupQuery").style.cssText = "display: block;";
        document.getElementById("orgSel").style.cssText = "display: table-row;";
        columns = [
            {header: "序号", dataIndex: 'GROUP_TYPE', renderer: getIndex},
            {header: "经销商代码", dataIndex: 'DEALER_CODE'},
            {header: "经销商名称", dataIndex: 'DEALER_NAME', style: 'text-align: center'},
            {header: "库房", dataIndex: 'WH_NAME', style: 'text-align: center'},
            {header: "总账面库存", dataIndex: 'GROUP_COUNT'},
            {header: "总在库含税金额(账面库存*经销商价格)", dataIndex: 'GROUP_AMOUNT', style: 'text-align:right;;'}
        ];
        __extQuery__(1);
    }

    function stockSearchAll() {
        var searchType = document.getElementById("searchType").value;
        document.getElementById("sumFlag").value = "1";
        url = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/stockQuerySearchAll.json";
        document.getElementById("groupQuery").style.cssText = "display: none;";
        document.getElementById("normalQuery").style.cssText = "display: block;";
        if ("normal" == searchType) {
            columns = [
                {header: "序号", dataIndex: 'PART_ID', renderer: getIndex, style: 'text-align: center'},
                {id: 'action', header: "操作", sortable: false, dataIndex: 'PART_ID', renderer: myLink1},
                {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: center', renderer: getFlag},
                {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: center'},
                //{header: "件号", dataIndex: 'PART_CODE',   style: 'text-align: center'},
                {header: "最小包装量", dataIndex: 'OEM_MIN_PKG'},
                {header: "单位", dataIndex: 'UNIT'},
               /*  {header: "批次号", dataIndex: 'BATCH_NO', style: 'text-align:center'}, */
                {header: "可用库存", dataIndex: 'NORMAL_QTY', style: 'text-align:center'},
                {header: "占用库存", dataIndex: 'BOOKED_QTY_NEW', style: 'text-align:center', renderer: getZYLink},
                {header: "封存库存", dataIndex: 'FC_QTY', style: 'text-align:center', renderer: getFCLink},
                {header: "账面库存", dataIndex: 'ITEM_QTY', style: 'text-align:center'},
                {header: "在途数量", dataIndex: 'ZT_QTY', style: 'text-align:center', renderer: getZTLink},
                <%--{header: "<c:if test="${parentOrgId != null}"><c:choose><c:when test="${parentOrgId  eq oemOrgId }">计划</c:when><c:otherwise>采购</c:otherwise></c:choose></c:if>价(元)", dataIndex: 'PRICE', style: 'text-align: right;'},--%>
                <c:if test="${parentOrgId  eq oemOrgId }">
                {header: "警戒库存", dataIndex: 'HALFY_QTY', style: 'text-align:center'},
                {header: "安全库存", dataIndex: 'SAFETY_QTY', style: 'text-align:center'},
                {header: "最大库存", dataIndex: 'MAX_QTY', style: 'text-align:center'},
                </c:if>
                {header: "是否锁定", dataIndex: 'IS_LOCKED', style: 'text-align:center', renderer: getLockState},
                {header: "是否有效", dataIndex: 'PDSTATE', style: 'text-align:center', renderer: getItemValue},
                {header: "仓库", dataIndex: 'WH_NAME', style: 'text-align: center'},
                {header: "配件备注", dataIndex: 'REMARK', style: 'text-align: center'}

            ];
        } else {

            var code = document.getElementById("dealerCode").value;
            var name = document.getElementById("dealerName").value;
            if ("" == code && "" == name) {
                //		MyAlert("请先至少填写经销商编码或经销商名称一个条件!");
                //		return false;
            }
            columns = [
                {header: "序号", dataIndex: 'PART_ID', renderer: getIndex, style: 'text-align: center'},
                {id: 'action', header: "操作", sortable: false, dataIndex: 'PART_ID', renderer: myLink},
                {header: "经销商编码", dataIndex: 'DEALER_CODE', style: 'text-align: center'},
                {header: "经销商名称", dataIndex: 'DEALER_NAME', style: 'text-align: center'},
                {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: center'},
                {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: center'},
                //{header: "件号", dataIndex: 'PART_CODE',   style: 'text-align: center'},
                {header: "最小包装量", dataIndex: 'OEM_MIN_PKG'},
                {header: "单位", dataIndex: 'UNIT'},
                {header: "批次号", dataIndex: 'BATCH_NO', style: 'text-align:center'},
                {header: "可用库存", dataIndex: 'NORMAL_QTY', style: 'text-align:center'},
                {header: "占用库存", dataIndex: 'BOOKED_QTY_NEW', style: 'text-align:center'},
                {header: "账面库存", dataIndex: 'ITEM_QTY', style: 'text-align:center'},
                {header: "采购价(元)", dataIndex: 'PRICE', style: 'text-align: right;'},
                {header: "是否锁定", dataIndex: 'IS_LOCKED', style: 'text-align:center', renderer: getLockState},
                {header: "是否有效", dataIndex: 'PDSTATE', style: 'text-align:center', renderer: getItemValue},
                {header: "仓库", dataIndex: 'WH_NAME', style: 'text-align: center'}

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
        return String.format("<a href=\"#\" onclick='showPDDetail(\"" + partId + "\",\"" + whId + "\",\"" + viewPage + "\",\"" + viewPage + "\")'>" + value + "</a>");
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
        var locId = record.data.LOC_ID;
        if (!locId) {
            locId = "";
        }
        var viewPage = "zyPage";
        return String.format("<a href=\"#\" onclick='showPDZYDetail(\"" + partId + "\",\"" + whId + "\",\"" + locId + "\",\"" + viewPage + "\")'>" + value + "</a>");
    }

    //返回占用明细链接
    function getFCLink(value, meta, record) {
        var partId = record.data.PART_ID;
        var whId = record.data.WH_ID;
        var locId = record.data.LOC_ID;
        if (!locId) {
            locId = "";
        }
        var viewPage = "fcPage";
        return String.format("<a href=\"#\" onclick='showPDZYDetail(\"" + partId + "\",\"" + whId + "\",\"" + locId + "\",\"" + viewPage + "\")'>" + value + "</a>");
    }
    //返回货位链接
    function getLCLink(value, meta, record) {
        var partId = record.data.PART_ID;
        var whId = record.data.WH_ID;
        var subLoc = record.data.SUB_LOC;
        if (subLoc) {
            return String.format("<a href=\"#\" onclick='showLCDetail(\"" + partId + "\",\"" + whId + "\")'>" + value + "</a>");
        }
        return String.format(value);
    }


    //返回详情链接
    function myLink(value, meta, record) {
        var partId = record.data.PART_ID;
        var whId = record.data.WH_ID;
        var locId = record.data.LOC_ID;
        if (!locId) {
            locId = "";
        }
        var inPage = "inPage";
        var outPage = "outPage";
        return String.format("<a href=\"#\" onclick='showPDDetail(\"" + partId + "\",\"" + whId + "\",\"" + locId + "\",\"" + inPage + "\")'>[入库明细]</a>&nbsp;<a href=\"#\" onclick='showPDDetail(\"" + partId + "\",\"" + whId + "\",\"" + locId + "\",\"" + outPage + "\")'>[出库明细]</a>");
    }

    //返回详情链接
    function myLink1(value, meta, record) {
        var partId = record.data.PART_ID;
        var whId = record.data.WH_ID;
        var locId = record.data.LOC_ID;
        if (!locId) {
            locId = "";
        }
        var inPage = "rcPage";
        return String.format("<a href=\"#\" onclick='showCRDetail(\"" + partId + "\",\"" + whId + "\",\"" + locId + "\",\"" + inPage + "\")'>[出入库明细]</a>");
    }
    //显示占用封存详情
    function showPDZYDetail(partId, whId, locId, viewPage) {
        OpenHtmlWindow('<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/showPDDetInit.do?partId=' + partId + '&whId=' + whId + '&locId=' + locId + '&viewPage=' + viewPage, 950, 500);
    }
    //显示详情
    function showPDDetail(partId, whId, locId, viewPage) {
        var sumFlag = document.getElementById("sumFlag").value;
        OpenHtmlWindow('<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/showPDDetInit.do?partId=' + partId + '&whId=' + whId + '&locId=' + locId + '&viewPage=' + viewPage + '&sumFlag=' + sumFlag, 950, 500);
    }

    function showCRDetail(partId, whId, locId, viewPage) {
        var sumFlag = document.getElementById("sumFlag").value;
        OpenHtmlWindow('<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/showPDDetInit.do?partId=' + partId + '&whId=' + whId + '&locId=' + locId + '&viewPage=' + viewPage + '&sumFlag=' + sumFlag + '&crFlag=1', 950, 500);
    }
    //显示货位详情
    function showLCDetail(partId, whId) {
        OpenHtmlWindow('<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/showLCDetInit.do?partId=' + partId + '&whId=' + whId, 950, 300);
    }

    function setSearchType(obj) {
        var searchTypeObj = document.getElementById("searchType");
        var dealerCodeObj = document.getElementById("dealerCode");
        var dealerNameObj = document.getElementById("dealerName");
        var whObj = document.getElementById("whId");
        var queryGBtn = document.getElementById("queryGBtn");
        var expGQuery = document.getElementById("expGQuery");
        var norDiv = document.getElementById("normaDiv");
        var specDiv = document.getElementById("specDiv");
        if (obj.checked) {
            //   		 queryGBtn.disabled = "disabled";
            //   		 expGQuery.disabled = "disabled";
            norDiv.style.cssText = "display: none;";
            specDiv.style.cssText = "display: block;";
            searchTypeObj.value = "specific";
            dealerCodeObj.disabled = "";
            dealerCodeObj.style.cssText = "background-color: #FFFFFF;";
            dealerNameObj.disabled = "";
            dealerNameObj.style.cssText = "background-color: #FFFFFF;";
            whObj.disabled = "disabled";
        }
        else {
            norDiv.style.cssText = "display: block;";
            specDiv.style.cssText = "display: none;";
            //   		 queryGBtn.disabled = "";
            //   		 expGQuery.disabled = "";
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
        document.fm.action = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/exportPartStockStatusExcel.do";
        document.fm.target = "_self";
        document.fm.submit();
    }
    //下载
    function exportPartStockExcelAll() {
        document.fm.action = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/exportPartStockStatusExcelAll.do";
        document.fm.target = "_self";
        document.fm.submit();
    }

    function expPartStockAmountExcel() {
        document.fm.action = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/expPartStockAmountExcel.do";
        document.fm.target = "_self";
        document.fm.submit();
    }
    function showUpload(divId) {
        var uploadDiv = document.getElementById(divId);
        if (uploadDiv.style.display == "block") {
            uploadDiv.style.display = "none";
        } else {
            uploadDiv.style.display = "block";
        }
    }
    //下载上传模板
    function exportExcelTemplate() {
        fm.action = g_webAppName + "/parts/storageManager/partStoInveManager/stockQueryAction/onwayExcelTemplate.do";
        fm.submit();
    }
    function onwayImportExcel() {
        if (fileVilidate("uploadFile")) {
            MyConfirm("确定导入选择的文件?", uploadExcel, []);
        }
    }
    //上传
    function uploadExcel() {
        document.fm.action = g_webAppName + "/parts/storageManager/partStoInveManager/stockQueryAction/onwayImportExcel.do";
        fm.submit();
    }
    function fileVilidate(fileId) {
        var importFileName = document.getElementById(fileId).value;
        if (importFileName == "") {
            MyAlert("请选择导入文件!");
            return false;
        }
        var index = importFileName.lastIndexOf(".");
        var suffix = importFileName.substr(index + 1, importFileName.length).toLowerCase();
        if (suffix != "xls" && suffix != "xlsx") {
            MyAlert("请选择Excel格式文件");
            return false;
        }
        return true;
    }
    function getFlag(value, meta, record) {
        var flag = record.data.FLAG;
        if ("1" == flag) {
            return String.format("<input style='font-style: italic;border: none;color: red;font-size: 15px; text-align: center;' title='已到进货点' readonly value='" + value + "'>");
        }
        return String.format(value);
    }

    //返回占用明细链接
    function getZTLink(value, meta, record) {
        var partId = record.data.PART_ID;
        var whId = record.data.WH_ID;
        var viewPage = "zTPage";
        return String.format("<a href=\"#\" onclick='showZTDetail(\"" + partId + "\",\"" + whId + "\",\"" + viewPage + "\")'>" + value + "</a>");
    }
    //显示盘点封存详情
    function showZTDetail(partId, whId, viewPage) {
        OpenHtmlWindow('<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/showPDDetInit.do?partId=' + partId + '&whId=' + whId + '&viewPage=' + viewPage, 950, 500);
    }
    function changeValue(obj) {
        if (obj.checked) {
            document.getElementById("jj").value = "1";
        } else {
            document.getElementById("jj").value = "0";
        }

        __extQuery__(1);
    }

</script>

</head>
<body>
	<div class="wbox">
		<form method="post" name="fm" id="fm" enctype="multipart/form-data">
			<input type="hidden" name="sumFlag" id="sumFlag" />
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息查询 &gt; 配件库存查询
				<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
				<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
				<input type="hidden" name="companyName" id="companyName" value="${companyName }" />
				<input type="hidden" name="searchType" id="searchType" value="normal" />
			</div>
			<div class="form-panel" id="normalQuery">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
					<table class="table_query">
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
								<select name="whId" id="whId" class="u-select" onchange="stockSearchAll()">
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
							<td class="right">货位：</td>
							<td>
								<input class="middle_txt" type="text" name="locCode" id="locCode" />
							</td>

							<td class="right">是否锁定：</td>
							<td>
								<select name="isLocked" id="isLocked" class="u-select" onchange="stockSearch()">
									<option value="">-请选择-</option>
									<option value="1">是</option>
									<option value="0">否</option>
								</select>
							</td>
							<td class="right">是否有效：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("STATE", <%=Constant.STATUS%> , "", true, "", "onchange='stockSearch()'", "false", '');
								</script>
							</td>
						</tr>
						<tr>
							<td class="right">是否占用：</td>
							<td>
								<select name="inType" id="inType" class="u-select" onchange="stockSearch()">
									<option value="">-请选择-</option>
									<option value="1">是</option>
									<option value="2">否</option>
								</select>
							</td>
							<td class="right">附属货位：</td>
							<td>
								<select name="sub" id="sub" class="u-select">
									<option value="">-请选择-</option>
									<option value="1">有</option>
									<option value="2">无</option>
								</select>
								<input class="short_txt" type="text" name="subCode" id="subCode" />
							</td>
							<td class="right">库存为零：</td>
							<td>
								<select name="showZero" id="showZero" class="u-select">
									<option value="1" selected>显示</option>
									<option value="2">不显示</option>
									<option value="3">只显示</option>
								</select>
							</td>
						</tr>
						<c:choose>
							<c:when test="${parentOrgId eq oemOrgId}">
								<tr id="orgSel">
									<td colspan="6" style="padding-left: 5%;" class="center">
										<input type="checkbox" name="jj" id="jj" value="0" onclick="changeValue(this);" />
										可用库存低于警戒
										<input type="checkbox" name="" id="" onclick="setSearchType(this)" />
										&nbsp;查看经销商库存 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 经销商编码：
										<input class="middle_txt" type="text" name="dealerCode" id="dealerCode" disabled="disabled" value="" style="background-color: #EAEAEA;" />
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 经销商名称：
										<input class="middle_txt" type="text" name="dealerName" id="dealerName" disabled="disabled" value="" style="background-color: #EAEAEA;" />
									</td>
								</tr>
							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose>
						<tr>
							<td class="center" colspan="6">
								<div id="normaDiv">
									<input class="u-button" type="button" value="查询" name="BtnQueryAll" id="BtnQueryAll" onclick="stockSearchAll()" />
									<input class="u-button" type="reset" value="重 置">
									<input class="u-button" type="button" value="导出" onclick="exportPartStockExcelAll()" />
									<input class="u-button" type="button" value="明细查询" name="BtnQuery1" id="queryBtn1" onclick="stockSearch()" />
									<input class="u-button" type="button" value="明细导出" onclick="exportPartStockExcel()" />
									<c:choose>
										<c:when test="${parentOrgId eq oemOrgId}">
											<input class="u-button" type="button" value="库存资金查询" name="BtnGQuery" id="queryGBtn" onclick="stockAmountSearch()" />
											<input class="u-button" type="button" value="资金汇总导出" id="expGQuery" onclick="expPartStockAmountExcel()" />
<!-- 											<input class="u-button" type="button" value="在途导入" id="onwayImport" onclick="showUpload('uploadDiv');" /> -->
										</c:when>
									</c:choose>

									<c:choose>
										<c:when test="${parentOrgId ne oemOrgId}">
											<div align="right">库存数量资金汇总：${qtyCnt}件:${qtySum}元</div>
										</c:when>
									</c:choose>
								</div>
								<div id="specDiv" style="display: none;">
									<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="stockSearch()" />
									<input class="u-button" type="reset" value="重 置">
									<input class="u-button" type="button" value="导 出" onclick="exportPartStockExcel()" />
								</div>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div id="groupQuery" style="display: none; width: 100%;" align="center">
				<input class="u-button" type="button" value="返回" name="BtnQueryAll" id="BtnQueryAll" onclick="stockSearchAll()" />
			</div>
			<div style="display: none; heigeht: 5px" id="uploadDiv">
				<table class="table_query">
					<th colspan="6"><img class="nav" src="/CQZTDMS/img/subNav.gif" /> 上传文件</th>
					<tr>
						<td colspan="2">
							<font color="red"> &nbsp;&nbsp;<input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate()" /> 文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;
							</font>
							<input type="file" name="uploadFile" id="uploadFile" style="width: 250px" datatype="0,is_null,2000" value="" />
							&nbsp;
							<input type="button" id="upbtn" class="normal_btn" value="确 定" onclick="onwayImportExcel();" />
						</td>
					</tr>
				</table>
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