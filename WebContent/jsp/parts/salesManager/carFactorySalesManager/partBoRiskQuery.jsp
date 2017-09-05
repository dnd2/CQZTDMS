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
    <title>配件BO风险报表</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="__extQuery__(1);loadcalendar();">
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 报表管理 &gt;配件报表&gt;本部销售报表&gt;
        BO风险报表
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <table class="table_query">
            <th colspan="6" width="100%"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
            <tr>

                <td width="10%"   align="right">订货单号：</td>
                <td width="25%"><input class="long_txt" type="text" name="ORDER_CODE" id="ORDER_CODE"/></td>
                <td width="10%"   align="right">销售单号：</td>
                <td width="25%"><input class="long_txt" type="text" name="SO_CODE" id="SO_CODE"/></td>
                <td width="10%"   align="right">服务站：</td>
                <td width="20%"><input class="middle_txt" type="text" name="DEALER_NAME" id="DEALER_NAME"/></td>
            </tr>
            <tr>

                <!-- <td width="10%"   align="right">地区：</td>
                <td width="20%"><input class="middle_txt" type="text" name="REGION" id="REGION"/></td>-->
                <td width="10%" align="right">供应商：</td>
	            <td width="25%">
	                <input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME" name="VENDER_NAME"/>
	                <input class="mark_btn" type="button" value="&hellip;"
	                       onclick="showPartVender('VENDER_NAME','VENDER_ID','false')"/>
	                <INPUT class=short_btn onclick="clearInput();" value=清除 type=button name=clrBtn>
	                <input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
	            </td>
	            <td width="10%"   align="right">BO生成日期：</td>
                <td width="25%">
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
                <td width="10%" align="right">库房：</td>
                <td width="20%">
                    <select id="WH_ID" name="WH_ID" class="short_sel">
                        <c:forEach items="${wareHouses}" var="wareHouse">
                            <option value="${wareHouse.whId }">${wareHouse.whName }</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td width="10%" align="right">配件编码：</td>
                <td width="25%"><input class="long_txt" type="text" name="PARTOLD_CODE" id="PARTOLD_CODE"/></td>
               <td width="10%" align="right">配件件号：</td>
                <td width="25%"><input class="long_txt" type="text" name="PART_CODE" id="PART_CODE"/></td>
                <td width="10%" align="right">配件类型：</td>
                <td width="20%">
                    <script type="text/javascript">
                    genSelBoxExp("PART_TYPE", <%=Constant.PART_BASE_PART_TYPES %>, "", true, "short_sel", "", "false", '');
                    </script>
                </td>
            </tr>
             <tr>
                 <td width="10%" align="right">排序方式：</td>
                 <td width="25%">
                     <select id="sortType" name="sortType" class="long_sel">
                         <option value="1">BO项数</option>
                         <option value="2">BO数量</option>
                         <option value="3">BO项数和数量</option>
                     </select>
                 </td>
                 <td colspan=2 align="left">
                <input type="radio" name="RADIO_SELECT" value="1"/>当前库存&lt月均销量
                <input type="radio" name="RADIO_SELECT" value="2"/>当前库存&lt安全库存数
                </td>
                
                 <td width="10%"   align="right">计划员：</td>
                 <td width="20%">
                     <select id="PLANER_ID" name="PLANER_ID" class="short_sel">
                         <option value="">-请选择-</option>
                         <c:forEach items="${planerList}" var="planerList">
                             <c:choose>
                                 <c:when test="${curUserId eq planerList.USER_ID}">
                                     <option selected="selected" value="${planerList.USER_ID }" >${planerList.USER_NAME }</option>
                                 </c:when>
                                 <c:otherwise>
                                     <option value="${planerList.USER_ID }" >${planerList.USER_NAME }</option>
                                 </c:otherwise>
                             </c:choose>
                         </c:forEach>
                     </select>
                 </td>
            </tr>
            <tr>
                <td width="10%"  align="right">配件是否有效：</td>
				 <td width="20%" >
					<script type="text/javascript">
					   	 genSelBoxExp("STATE",<%=Constant.STATUS%>,"",true,"short_sel","","false",'');
					</script>
				 </td>
            </tr>
            <tr>
                <td width="10%" align="right">BO总项数：</td>
                <td width="25%">
                    <input id="bozxs"  type="text" class="long_txt" value="" style="background-color: #99D775;" readonly="readonly"/>
                </td>
                <td width="10%" align="right">销售总项数：</td>
                <td width="25%">
                    <input id="xszxs"  type="text" class="long_txt" value="" style="background-color: #99D775;" readonly="readonly"/>
                </td>
                <td width="10%" align="right">BO率：</td>
                <td width="20%">
                    <input id="bolv" class="middle_txt" type="text" value="" style="background-color: #99D775;" readonly="readonly"/>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查询"
                           onclick="__extQuery__(1);"/>
                    <input class="normal_btn" type="button" value="导出" onclick="expPartBoRiskExcel();"/>
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        autoAlertException();//输出错误信息
        var myPage;
        var url = "<%=contextPath%>/report/partReport/partSalesReport/BoRiskReport/queryPartBoRisk.json";

        var title = null;

        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align:left',renderer: partOldCodeInput},
            {header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align:left'},
            {header: "配件件号", dataIndex: 'PART_CODE',  style: 'text-align:left'},
            {header: "配件类型", dataIndex: 'PART_TYPE', align:'center',renderer: getItemValue},
            {header: "当前可用库存", dataIndex: 'NORMAL_QTY', align:'center'},
            {header: "月平均销量", dataIndex: 'AVG_QTY', align: 'center'},
            {header: "安全库存", dataIndex: 'SAFETY_STOCK', align: 'center'},
            {header: "在途数", dataIndex: 'ZT_QTY', align: 'center'},
            {header: "待入库数", dataIndex: 'SPAREIN_QTY', align: 'center'},
            {header: "订货金额", dataIndex: 'BUY_AMOUNT', style: 'text-align:right'},
            {header: "订货数量", dataIndex: 'BUY_QTY', align: 'center'},
            {header: "已交货数量", dataIndex: 'SALES_QTY', align: 'center'},
            {header: "BO数量", dataIndex: 'BO_QTY', align: 'center',renderer:showToSDtl},
            {header: "BO金额", dataIndex: 'BO_AMOUNT', style: 'text-align:right'},
            {header: "BO项数", dataIndex: 'BOXS', align: 'center'},
            {header: "BO满足数", dataIndex: 'TOSAL_QTY', align: 'center'},
            {header: "默认供应商", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
           /* {header: "销售项数", dataIndex: 'XSXS', align: 'center'},*/
          /*  {header: "BO总项数", dataIndex: 'BOZXS', align: 'center'},
            {header: "销售总项数", dataIndex: 'SALZXS', align: 'center'},*/
            /*{header: "满足率", dataIndex: 'XLMZLV', align: 'center'},*/
            {header: "计划员", dataIndex: 'NAME', align: 'center'},
            {header: "配件是否有效", dataIndex: 'STATE', align:'center',renderer: getItemValue}
        ];

        function partOldCodeInput(value, metaDate, record){
        	var normal_qty = record.data.NORMAL_QTY;
        	var avg_qty = record.data.AVG_QTY;
        	var safety_qty = record.data.SAFETY_STOCK;
            if (normal_qty<avg_qty||normal_qty<safety_qty) {
                return String.format("<input style='background-color: F74040;border: none' type='text' value=\"" + value + "\"   readonly/> ");
            }else{
                return String.format("<input style='border: none' type='text' value=\"" + value + "\"   readonly/> ");
            }
        }
        
        //导出
        function expPartBoRiskExcel() {
            fm.action = "<%=contextPath%>/report/partReport/partSalesReport/BoRiskReport/expPartBoRiskExcel.do";
            fm.target = "_self";
            fm.submit();
        }
        function showToSDtl(value, metaDate, record){
        	var partId = record.data.PART_ID;
            return String.format("<a href=\"#\" onclick='view(\"" + partId + "\")'>" + value + "</a>");
        }
        function clearInput() {
            //清空选定供应商
            document.getElementById("VENDER_ID").value = '';
            document.getElementById("VENDER_NAME").value = '';
        }
        function view(partId){
            var startDate = $("t1").value;
            var endDate = $("t2").value;
        	OpenHtmlWindow("<%=contextPath%>/report/partReport/partSalesReport/BoRiskReport/queryInfoByPartIdInit.do?partId="+partId+"&startDate="+startDate+"&endDate="+endDate,900,400);
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