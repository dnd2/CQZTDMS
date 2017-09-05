<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>配件BO单明细查询</title>
    <style>#DEALER_NAME{margin-left:4px}</style>
    <script type="text/javascript">
        var myPage;
        var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/queryPartBoDtl.json";
        var title = null;
        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "订货单位编码", dataIndex: 'DEALER_CODE', style: 'text-align:left'},
            {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
            {header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align:left'},
            {header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align:left'},
            //{header: "配件件号", dataIndex: 'PART_CODE',  style: 'text-align:left'},
            {header: "当前可用数量", dataIndex: 'NORMAL_QTY', align: 'center'},
            {header: "在途数量", dataIndex: 'ZT_QTY', align: 'center',renderer:getZTLink},
            {header: "BO剩余数量", dataIndex: 'BO_ODDQTY', align: 'center'},
            {header: "订货数量", dataIndex: 'BUY_QTY', align: 'center'},
          /*  {header: "满足数量", dataIndex: 'SALES_QTY', align: 'center'},*/
            {header: "BO数量", dataIndex: 'BO_QTY', align: 'center'},
            {header: "转销售数量", dataIndex: 'TOSAL_QTY', align: 'center'},
           /* {header: "关闭数量", dataIndex: 'CLOSE_QTY', align: 'center'},*/
            {header: "BO金额", dataIndex: 'AMOUNT', align: 'center'}
        ];

        //导出
        function expPartBoDtlExcel() {
            fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/expPartBoDtlExcel.do";
            fm.target = "_self";
            fm.submit();
        }

        function goback() {
            window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/boQueryInit.do';
        }
        
        Date.prototype.format = function(format)
        {
         var o = {
         "M+" : this.getMonth()+1, //month
         "d+" : this.getDate(),    //day
         "h+" : this.getHours(),   //hour
         "m+" : this.getMinutes(), //minute
         "s+" : this.getSeconds(), //second
         "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
         "S" : this.getMilliseconds() //millisecond
         }
         if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
         (this.getFullYear()+"").substr(4 - RegExp.$1.length));
         for(var k in o)if(new RegExp("("+ k +")").test(format))
         format = format.replace(RegExp.$1,
         RegExp.$1.length==1 ? o[k] :
         ("00"+ o[k]).substr((""+ o[k]).length));
         return format;
        }
        
        function showMonthFirstDay() {
            var Nowdate = new Date();
            var MonthFirstDay = new Date(Nowdate.getFullYear(),  Nowdate.getMonth(), 1);
            //return DateUtil.Format("yyyy-MM-dd", MonthFirstDay);
            return MonthFirstDay.format("yyyy-MM-dd");
        }
        function showMonthLastDay() {
            var Nowdate = new Date();
            var MonthNextFirstDay = new Date(Nowdate.getFullYear(), Nowdate.getMonth() + 1, 1);
            var MonthLastDay = new Date(MonthNextFirstDay - 86400000);
            //return DateUtil.Format("yyyy-MM-dd", MonthLastDay);
            return MonthLastDay.format("yyyy-MM-dd");
        }
     /*   $('t1').value = showMonthFirstDay();
        $('t2').value = showMonthLastDay();*/

        
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
        
        
        
        function myQuery() {
            if ($("#t1")[0].value == "" || $("#t2")[0].value == "") {
                MyAlert("请选择BO产生日期!");
                return false;
            } else
            // __extQuery__(1);
           makeNomalFormCall(url, getResultQuery, "fm") ;
        }
        function getResultQuery(json){
        	var allQty = json.allQty;
        	var amount = json.amount==null?0:json.amount;
            var bonum = json.bonum==null?0:json.bonum;
            var boqty = json.boqty==null?0:json.boqty;
			$("#BO_COUNT")[0].value	=allQty;
    		$("#BO_AMOUNT")[0].value=amount;
	        $("#BO_NUM")[0].value=bonum;
	        $("#BO_QTY")[0].value=boqty;
	        __extQuery__(1);
        }
        
        $(document).ready(function(){
        	myQuery();
        });
    </script>    
</head>
<body> <!-- onunload='javascript:destoryPrototype();' -->
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 配件管理 > 配件销售管理 >BO单查询>明细查询 </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table class="table_query">
	            <tr>
	                <td width="10%" class="right">BO单号：</td>
	                <td width="20%"><input class="middle_txt" type="text" name="BO_CODE" id="BO_CODE"/></td>
	                <td width="10%" class="right">BO产生日期：</td>
	                <td width="25%">
	                    <input name="startDate" id="t1" value="${old}" type="text" class="short_txt" datatype="1,is_date,10"
	                           group="t1,t2" style="width:80px;"/>
	                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"/>
	                    	至
	                    <input name="endDate" id="t2" value="${now}" type="text" class="short_txt" datatype="1,is_date,10"
	                           group="t1,t2" style="width:80px;"/>
	                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"/>
	                </td>
	                <td width="10%"   class="right">订货单号：</td>
	                <td width="20%"><input class="middle_txt" type="text" name="ORDER_CODE" id="ORDER_CODE"/></td>
	            </tr>
	            <tr>
	                <td width="10%"   class="right">订货单位编码：</td>
	                <td width="20%"><input class="middle_txt" type="text" name="DEALER_CODE" id="DEALER_CODE"/></td>
	                <td width="10%"   class="right">订货单位名称：</td>
	                <td width="20%"><input class="middle_txt" type="text" name="DEALER_NAME" id="DEALER_NAME"/></td>
	                <td width="10%"   class="right">销售单位：</td>
	                <td width="20%"><input class="middle_txt" type="text" name="SELLER_NAME" id="SELLER_NAME"/></td>
	            </tr>
	            <tr>
	                <td width="10%"   class="right">配件编码：</td>
	                <td width="20%"><input class="middle_txt" type="text" name="PARTOLD_CODE" id="PARTOLD_CODE"/></td>
	                <td width="10%"   class="right">BO单状态：</td>
	                <td width="20%">
	                    <script type="text/javascript">
	                        genSelBoxExp("STATE", <%=Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE%>, "", true, "", "", "false", '');
	                    </script>
	                </td>
	                <td width="10%"   class="right">订单类型：</td>
	                <td width="20%">
	                    <script type="text/javascript">
	                        genSelBoxExp("ORDER_TYPE", <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE%>, "", true, "", "", "false", '');
	                    </script>
	                </td>
	            </tr>
	            <tr>
	             <c:if test="${salerFlag}" >
	                <td width="10%"   class="right">销售人员：</td>
	                <td width="20%">
	                   <select  name="salerId" id = "salerId" class="u-select" >
				       		<option  value="">--请选择--</option>
						   	<c:forEach items="${salerList}" var="saler">
							  <c:choose> 
								<c:when test="${curUserId eq saler.USER_ID}">
								  <option selected="selected" value="${saler.USER_ID}">${saler.NAME}</option>
								</c:when>
								<c:otherwise>
								  <option  value="${saler.USER_ID}">${saler.NAME}</option>
								</c:otherwise>
							  </c:choose>
							</c:forEach>
				      	</select>
	                </td>
	                </c:if>
	            </tr>
	            <tr>
	                <td width="10%" class="right">BO总项数：</td>
	                <td width="20%">
	                    <input id="BO_COUNT" class="middle_txt" type="text" value="${mainInfo.ALLQTY }" style="background-color: #99D775;" readonly="readonly"/>
	                </td>
	                <td width="10%" class="right">BO总数量/有效BO总数量：</td>
	                <td width="20%">
	                    <input id="BO_QTY" class="short_txt" type="text" value="${mainInfo.BOQTY }" style="background-color: #99D775;" readonly="readonly"/>
	                    <input id="BO_NUM" class="short_txt" type="text" value="${mainInfo.BONUM }" style="background-color: #99D775;" readonly="readonly"/>
	                </td>
	                <td width="10%" class="right">BO总金额：</td>
	                <td width="20%">
	                    <input id="BO_AMOUNT" class="middle_txt" type="text" value="${mainInfo.AMOUNT }" style="background-color: #99D775;" readonly="readonly"/>
	                </td>
	            </tr>
	            <tr>
	                <td colspan="6" class="center">
	                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查询"
	                           onclick="myQuery();"/>
	                    <input class="normal_btn" type="button" value="明细导出" onclick="expPartBoDtlExcel();"/>
	                    <input class="normal_btn" type="button" value="返回" onclick="goback();"/>
	                </td>
	            </tr>
	        </table>
	    </div>
	    </div>    
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

</div>
</body>
</html>