<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
    String contextPath = request.getContextPath();
%>
<title></title>

<script type="text/javascript">
    var myPage;
    var valType = "query";

    var url = "<%=contextPath%>/parts/financeManager/partInvoiceQuery/invoiceQueryAction/invoicePrintQuery.json?";

    var title = null;

    var columns = [
        {header: "序号", dataIndex: '', renderer: getIndex, align: 'center'},
        {header: "选择", align: 'center', dataIndex: 'DEALER_ID', renderer: checkLink},
        {header: "订货单位编码", dataIndex: 'DEALER_CODE', align: 'center'},
        {header: "订货单位", dataIndex: 'DEALER_NAME', align: 'center'}
       /* {header: "总开票金额", dataIndex: 'AMOUNT', align: 'center'}*/
    ];
    function checkLink(value, meta, record) {
        return "<input id='cb' name='cb' type='radio' onclick='ck(this)' value='" + value + "' />";
    }
    function ckAll(obj) {
        var cb = jQuery("input[name='cb']");
        for (var i = 0; i < cb.length; i++) {
            if (cb[i].disabled) {
                continue;
            }
            cb[i].checked = obj.checked;
        }
    }
    function ck(obj) {
        var cb = jQuery("input[name='cb']");
        var flag = true;
        for (var i = 0; i < cb.length; i++) {
            if (!cb[i].checked) {
                flag = false;
            }
        }
        jQuery('#cbAll').attr('checked', flag);
    }
    $(function () {
        $(document).on('click', '#queryBtn', function () {
            __extQuery__(1);
        })
        $(document).on('click', '#closeBtn', function () {
            window.close();
        })
        $(document).on('click', '#cb', function () {
            var ids = "";
            $("input[name='cb'],[checked]").each(function () {
                if (this.checked) {
                    ids += "," + this.value;
                }
            });
            if (ids == "") {
                MyAlert('请选择!');
                return;
            }

            document.fm.action = "<%=contextPath%>/parts/financeManager/partInvoiceQuery/invoiceQueryAction/printInfo.do?dealerIds=" + ids;
            document.fm.target = "_blank";
            document.fm.submit();
        	
<%--             window.showModalDialog("<%=request.getContextPath()%>/parts/financeManager/partInvoiceQuery/invoiceQueryAction/printInfo.do?dealerIds=" + ids, "", 'edge: Raised; center: Yes; help: Yes; resizable: Yes; status: No;dialogHeight:540px;dialogWidth:850px'); --%>

        })
        __extQuery__(1);
    })

</script>
</head>
<body>
	<div id="div1" class="wbox">
		<form method="post" name="fm" id="fm">
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" alt="" />&nbsp;当前位置:配件管理&gt;配件销售管理&gt;打印发票邮寄信息
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">订货单位名称：</td>
							<td>
								<input class="middle_txt" type="text" name="dealerName" id="dealerName" />
							</td>
							<td class="right">订货单位代码：</td>
							<td>
								<input class="middle_txt" type="text" name="dealerCode" id="dealerCode" />
							</td>
							<td class="right">销售人员：</td>
							<td>
								<select name="salerId" id="salerId" class="u-select">
									<option value="">--请选择--</option>
									<c:forEach items="${salerList}" var="saler">
										<c:choose>
											<c:when test="${curUserId eq saler.USER_ID}">
												<option selected="selected" value="${saler.USER_ID}">${saler.NAME}</option>
											</c:when>
											<c:otherwise>
												<option value="${saler.USER_ID}">${saler.NAME}</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>

							<td class="right">是否有业务往来</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("is_bns",
								<%=Constant.IF_TYPE%>
									,
								<%=Constant.IF_TYPE_YES%>
									, true,
											"", "", "false", "");
								</script>
							</td>
							<td class="right">开票类型：</td>
							<td colspan="3">
								<script type="text/javascript">
									genSelBoxExp("dlrInvTpe",
								<%=Constant.DLR_INVOICE_TYPE%>
									,
								<%=Constant.DLR_INVOICE_TYPE_01%>
									,
											true, "", "", "false", "");
								</script>
							</td>
						</tr>
						<tr>
							<td colspan="6" class="center">
								<input name="BtnQuery" id="queryBtn" class="u-button" type="button" value="查 询" />
								&nbsp;
								<input name="closeBtn" id="closeBtn" class="u-button" type="button" onclick="_hide();" value="关 闭" />
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
			<%--   <table class="table_query">
        <tr align="center">
            <td colspan="4">
                <input class="u-button" id="closeBtn" type="button" value="关 闭"/>
            </td>
        </tr>
    </table>--%>
		</form>
	</div>
</body>
</html>