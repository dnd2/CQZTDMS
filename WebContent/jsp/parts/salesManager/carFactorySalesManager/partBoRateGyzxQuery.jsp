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
    <title>BO率报表(供应中心)</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="loadcalendar();">
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 报表管理 &gt;配件报表&gt;供应中心报表&gt;
        BO率报表(供应中心)
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <table class="table_query">
            <th colspan="6" width="100%"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
            <tr>
               <c:if test="${flag eq 1 }">
			        <td width="10%"   align="right">供应中心代码：</td>
                    <td width="20%">
                    <input class="middle_txt" type="text" readonly="readonly" id="dealerCode" name="dealerCode" value="${venderCode }"/>
                    </td>
			  </c:if>
			  <c:if test="${flag eq 0 }">
                    <td width="10%"   align="right">供应中心代码：</td>
                    <td width="20%">
                    <input class="middle_txt" type="text" readonly="readonly" id="dealerCode" name="dealerCode" value=""/>
                 </td>
              </c:if>
	           
	           <c:if test="${flag eq 1 }">
                    <td width="10%"   align="right">供应中心：</td>
                    <td width="20%">
                    <input class="middle_txt" type="text" readonly="readonly" id="dealerName" name="dealerName" value="${venderName }"/>
                    <input type="hidden" id="dealerId" name="dealerId" value="${venderId }"/>
                    </td>
                 </c:if>
                 <c:if test="${flag eq 0 }">
                    <td width="10%"   align="right">供应中心：</td>
                    <td width="20%">
                    <input class="middle_txt" type="text" readonly="readonly"  id="dealerName" name="dealerName"/>
                    <input class="mark_btn" type="button" value="&hellip;"
                           onclick="showGyzx('dealerId','dealerName','dealerCode');"/>
                    <input class="mini_btn" onclick="clearGyzxInput();" value="清除" type="button" name="clrBtn"/>
                    <input type="hidden" id="dealerId" name="dealerId" value=""/>
                 </td>
                 </c:if>
                 
                 <td width="10%"   align="right">BO日期：</td>
                <td width="20%">
                    <input name="startDate" id="t1" value="${old}" type="text" class="short_txt" datatype="1,is_date,10"
                           group="t1,t2">
                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                           onclick="showcalendar(event, 't1', false);"/>
                    至
                    <input name="endDate" id="t2" value="${now}" type="text" class="short_txt" datatype="1,is_date,10"
                           group="t1,t2">
                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                           onclick="showcalendar(event, 't2', false);"/>
                </td>
            </tr>
           
            <tr>
                <td width="10%" align="right">配件编码：</td>
                <td width="20%"><input class="middle_txt" type="text" name="PARTOLD_CODE" id="PARTOLD_CODE"/></td>
                <td width="10%" align="right">配件名称：</td>
                <td width="20%"><input class="middle_txt" type="text" name="PART_CNAME" id="PART_CNAME"/></td>
                <td width="10%" align="right">配件件号：</td>
                <td width="20%"><input class="middle_txt" type="text" name="PART_CODE" id="PART_CODE"/></td>
            </tr>
            
            <tr>
                <td width="10%" align="right">BO总项数：</td>
                <td width="20%">
                    <input id="bozxs"  type="text" class="middle_txt" value="" style="background-color: #99D775;" readonly="readonly"/>
                </td>
                <td width="10%" align="right">销售总项数：</td>
                <td width="20%">
                    <input id="xszxs"  type="text" class="middle_txt" value="" style="background-color: #99D775;" readonly="readonly"/>
                </td>
                <td width="10%" align="right">BO率：</td>
                <td width="20%">
                    <input id="bolv" class="middle_txt" type="text" value="" style="background-color: #99D775;" readonly="readonly"/>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查询"
                           onclick="query();"/>
                    <input class="normal_btn" type="button" value="导出" onclick="expPartBoRateGExcel();"/>
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        autoAlertException();//输出错误信息
        var myPage;
        var url = "<%=contextPath%>/report/partReport/partSalesReport/PartBoRateReport/queryPartBoRateGyzx.json";

        var title = null;

        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "供应中心代码", dataIndex: 'SELLER_CODE',  style: 'text-align:left'},
            {header: "供应中心", dataIndex: 'SELLER_NAME',  style: 'text-align:left'},
            {header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align:left'},
            {header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align:left'},
            {header: "配件件号", dataIndex: 'PART_CODE',  style: 'text-align:left'},
            {header: "配件类型", dataIndex: 'PART_TYPE', align:'center'},
            {header: "当前可用库存", dataIndex: 'NORMAL_QTY', align:'center'},
            {header: "订货数量", dataIndex: 'BUY_QTY', align: 'center'},
            {header: "订货金额", dataIndex: 'BUY_AMOUNT', style: 'text-align:right'},
            {header: "已交货数量", dataIndex: 'SALES_QTY', align: 'center'},
            {header: "BO数量", dataIndex: 'BO_QTY', align: 'center'},
            {header: "BO金额", dataIndex: 'BO_AMOUNT', style: 'text-align:right'},
            {header: "BO项数", dataIndex: 'BOXS', align: 'center'},
            {header: "已满足数量", dataIndex: 'TOSAL_QTY', align: 'center'}
        ];

        function query(){
        	if(!$("dealerId").value){
        		MyAlert("请选择供应中心!");
        		return;
        	}
        	__extQuery__(1);
        }
        //导出
        function expPartBoRateGExcel() {
        	if(!$("dealerId").value){
        		MyAlert("请选择供应中心!");
        		return;
        	}
            fm.action = "<%=contextPath%>/report/partReport/partSalesReport/PartBoRateReport/expPartBoRateGExcel.do";
            fm.target = "_self";
            fm.submit();
        }
        

        function clearGyzxInput() {
            document.getElementById("dealerId").value = '';
            document.getElementById("dealerCode").value = '';
            document.getElementById("dealerName").value = '';
        }

        function showGyzx(venderId,venderName,venderCode) {
            if (!venderName) {
            	venderName = null;
            }
            if (!venderId) {
            	venderId = null;
            }
            OpenHtmlWindow("<%=contextPath%>/jsp/parts/salesManager/carFactorySalesManager/gyzxSelect.jsp?venderName=" + venderName + "&venderId=" + venderId+"&venderCode="+venderCode, 730, 390);
        }
        
        function callBack(json){
            var ps;
            var bozxs = json.bozxs;
            var salzxs = json.salzxs;
            var bolv = json.bolv;
            $("bozxs").value=bozxs;
            $("xszxs").value=salzxs;
            $("bolv").value=bolv;
            //设置对应数据
            if(Object.keys(json).length>0){
                keys = Object.keys(json);
                for(var i=0;i<keys.length;i++){
                    if(keys[i] =="ps"){
                        ps = json[keys[i]];
                        break;
                    }
                }
            }

            //生成数据集
            if(ps.records != null){
                $("_page").hide();
                $('myGrid').show();
                new createGrid(title,columns, $("myGrid"),ps).load();
                //分页
                myPage = new showPages("myPage",ps,url);
                myPage.printHtml();
                hiddenDocObject(2);
            }else{
                $("_page").show();
                $("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据!</div>";
                $("myPage").innerHTML = "";
                removeGird('myGrid');
                $('myGrid').hide();
                hiddenDocObject(1);
            }
        }
    </script>
</div>
</body>
</html>