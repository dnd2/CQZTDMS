<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%String contextPath = request.getContextPath();%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>销售退货报表(本部)</title>
    <script language="JavaScript">
        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
        }
    </script>
</head>
<body onunload='javascript:destoryPrototype();'>
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 报表管理&gt;配件报表&gt;本部销售报表&gt;销售退货报表(本部)
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <table class="table_query">
            <th colspan="6" width="100%"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
            <tr>
            	<td width="10%" class="table_query_right" align="right">退货日期：</td>
	             <td width="25%">
	                <input name="balBeginTime" id="balBeginTime" value="${old}" type="text" class="short_txt" datatype="1,is_date,10"
	                       group="balBeginTime,balEndTime">
	                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
	                       onclick="showcalendar(event, 'balBeginTime', false);"/>
	                                           至
	                <input name="balEndTime" id="balEndTime" value="${now }" type="text" class="short_txt" datatype="1,is_date,10"
	                       group="balBeginTime,balEndTime">
	                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
	                       onclick="showcalendar(event, 'balEndTime', false);"/>
	            </td>
		        <td width="10%" align="right">退货单号：</td>
	            <td width="20%"><input name="returnCode" type="text" class="middle_txt" id="returnCode"/></td>
		        <td width="10%" align="right">服务商：</td>
	            <td width="20%"><input name=dealerName type="text" class="middle_txt" id="dealerName"/></td>
	             
        </tr>
        
       <tr>
            <td width="10%" align="right">配件编码：</td>
            <td width="25%"><input name="PART_OLDCODE" type="text" class="long_txt" id="PART_OLDCODE"/></td>
            <td width="10%" align="right">配件名称：</td>
            <td width="20%"><input name="PART_CNAME" type="text" class="middle_txt" id="PART_CNAME"/></td>
            <td width="10%" align="right">配件件号：</td>
            <td width="20%"><input name="PART_CODE" type="text" class="middle_txt" id="PART_CODE"/></td>
            
        </tr>
            
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查询" onclick="__extQuery__(1);"/>
                    <input class="normal_btn" type="button" value="导出" onclick="expPartSalesExcel();"/>
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        autoAlertException();//输出错误信息
        var myPage;
        var url = "<%=contextPath%>/report/partReport/partSalesReport/PartSalesReport/queryPartSalesReturnInfo.json";

        var title = null;

        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "退货单号", dataIndex: 'RETURN_CODE', align:'center'},//,renderer: getItemValue
            {header: "服务商代码", dataIndex: 'DEALER_CODE'},
            {header: "服务商名称", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
            {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
            {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
            {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
            {header: "单位", dataIndex: 'UNIT', align: 'center'},
            {header: "配件类型", dataIndex: 'PART_TYPE', align: 'center'},
            {header: "退货数量", dataIndex: 'RETURN_QTY'},
            {header: "单价", dataIndex: 'BUY_PRICE', style: 'text-align:right'},
            {header: "退货金额", dataIndex: 'BUY_AMOUNT', style: 'text-align:right'},
            {header: "退货原因", dataIndex: 'REMARK', style: 'text-align:left'},
            {header: "退货日期", dataIndex: 'CREATE_DATE', align: 'center'}
        ];



        
        //导出
        function expPartSalesExcel() {
            fm.action = "<%=contextPath%>/report/partReport/partSalesReport/PartSalesReport/expPartSalesReturnExcel.do";
            fm.target = "_self";
            fm.submit();
        }

        function callBack(json){
        	var ps;
        	
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
        		var tbl = $("myTable");
        		var rowObj = tbl.insertRow(tbl.rows.length);
        		rowObj.className = "table_list_row1";
        		var cell1 = rowObj.insertCell(0);
       		    var cell2 = rowObj.insertCell(1);
       		    var cell3 = rowObj.insertCell(2);
       		    var cell4 = rowObj.insertCell(3);
       		    var cell5 = rowObj.insertCell(4);
       		    var cell6 = rowObj.insertCell(5);
       		    var cell7 = rowObj.insertCell(6);
       		    var cell8 = rowObj.insertCell(7);
       		    var cell9 = rowObj.insertCell(8);
       		    var cell10 = rowObj.insertCell(9);
       		    var cell11 = rowObj.insertCell(10);
       		    var cell12 = rowObj.insertCell(11);
       		    var cell13 = rowObj.insertCell(12);
       		    var cell14 = rowObj.insertCell(13);
       		    cell1.innerHTML = '<tr><td></td>';
       		    cell2.innerHTML = '<td><strong>合计：</strong></td>';
       		    cell3.innerHTML = '<td></td>';
       		    cell4.innerHTML = '<td></td>';
       		    cell5.innerHTML = '<td></td>';
       		    cell6.innerHTML = '<td></td>';
       		    cell7.innerHTML = '<td></td>';
       		    cell8.innerHTML = '<td></td>';
       		    cell9.innerHTML = '<td></td>';
       		    cell10.innerHTML = '<td></td>';
       		    cell11.innerHTML = '<td></td>';
       		    cell12.innerHTML = '<td><strong>'+json.allSalesAmount+'</strong></td>';
       		    cell13.innerHTML = '<td></td>';
       		    cell14.innerHTML = '<td></td>';
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