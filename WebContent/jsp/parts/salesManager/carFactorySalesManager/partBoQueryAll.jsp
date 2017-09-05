<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%String contextPath = request.getContextPath();%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>配件BO单汇总查询</title>
<script type="text/javascript">
        var myPage;
        var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/queryAllPartBo.json";
        var title = null;
        var columns = [
                       {header: "序号", align: 'center', renderer: getIndex},
                       {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
                       {header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align:left'},
                       //{header: "件号", dataIndex: 'PART_CODE',  style: 'text-align:left'},
                       {header: "当前可用库存", dataIndex: 'NORMAL_QTY',  style: 'text-align:left'},
                       {header: "BO汇总数量", dataIndex: 'BO_QTY',  style: 'text-align:left'},
                       {header: "有效BO汇总", dataIndex: 'BO_ODDQTY', align: 'center'},
                       {header: "BO汇总次数", dataIndex: 'BO_CNT', align: 'center'}
                   ];
        
        //导出
        function expPartBoAllExcel() {
            fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/expPartBoAllExcel.do";
            fm.target = "_self";
            fm.submit();
        }

        function goback() {
            window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/boQueryInit.do';
        }
        function myQuery() {
            if ($("#t1")[0].value == "" || $("#t2")[0].value == "") {
                MyAlert("请选择BO产生日期!");
                return false;
            } else
                __extQuery__(1);
        }
        Date.prototype.format = function(format){
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
         RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
         return format;
        }
        
        function showMonthFirstDay() {
            var Nowdate = new Date();
            var MonthFirstDay = new Date(Nowdate.getFullYear(),  Nowdate.getMonth()-3, 1);
            return MonthFirstDay.format("yyyy-MM-dd");
        }
        function showMonthLastDay() {
            var Nowdate = new Date();
            var MonthNextFirstDay = new Date(Nowdate.getFullYear(), Nowdate.getMonth() + 1, 1);
            var MonthLastDay = new Date(MonthNextFirstDay - 86400000);
            return MonthLastDay.format("yyyy-MM-dd");
        }

        $(function() {
            $('#t1')[0].value = showMonthFirstDay();
            $('#t2')[0].value = showMonthLastDay();
        });
        
    </script>    
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 配件管理 > 配件销售管理 >BO单查询>汇总查询</div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        
		<div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table class="table_query">
	            <tr>
	            	<td class="right">配件编码：</td>
	                <td ><input class="middle_txt" type="text" name="PARTOLD_CODE" id="PARTOLD_CODE"/></td>
	                <td class="right">配件名称：</td>
	                <td ><input class="middle_txt" type="text" name="PARTOLD_NAME" id="PARTOLD_NAME"/></td>
	                <!-- 
	                <td class="right">件号：</td>
	                <td ><input class="middle_txt" type="text" name="PART_CODE" id="PART_CODE"/></td>
	                 -->
	            </tr>
	            <tr>
	                <td class="right">排序：</td>
	                <td >
	                    <select name="desc" id="desc" class="u-select">
	 						<option value="1" selected>BO数量</option>
	 						<option value="2">BO次数</option>
						</select>
	                </td>
	                <td width="10%" class="right">BO产生日期：</td>
	                <td width="26%">
	                    <input name="startDate" id="t1" value="${old }" type="text" style="width:80px;" class="short_txt" datatype="1,is_date,10"
	                           group="t1,t2">
	                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"/>
	                                                     至
	                    <input name="endDate" id="t2" value="${now }" type="text" style="width:80px;" class="short_txt" datatype="1,is_date,10"
	                           group="t1,t2">
	                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"/>
	                </td>
	                <TD width="10%"   class="right">过滤条件</TD>
	                <TD>
	                    <select name="fliter" id="fliter" class="u-select">
	                        <option value="1" selected>未满足且库存不足</option>
	                        <option value="2">不限制</option>
	                    </select>
	                </TD>
	            </tr>
	            <tr>
	                <td colspan="6" class="center">
	                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查询" onclick="myQuery();"/>
	                    <input class="normal_btn" type="button" value="汇总导出" onclick="expPartBoAllExcel();"/>
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