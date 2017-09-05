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
    <title>直发销售报表(本部)</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="__extQuery__(1);loadcalendar();">
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 报表管理 &gt;配件报表&gt;配件销售报表&gt;
         油液销售报表
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <table class="table_query">
            <th colspan="6" width="100%"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
           
            <tr>
                <td width="10%" align="right">配件编码：</td>
                <td width="20%"><input class="middle_txt" type="text" name="PARTOLD_CODE" id="PARTOLD_CODE"/></td>

                 <td width="10%"   align="right">销售日期：</td>
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
                <td width="10%" align="right">服务商：</td>
                <td width="20%"><input class="middle_txt" type="text" name="DEALER_NAME" id="DEALER_NAME"/></td>
            </tr>
            
            <tr>
                <td colspan="6" align="center">
                <input type="radio" name="RADIO_SELECT" value="1" checked/>按单位汇总&nbsp;
                <input type="radio" name="RADIO_SELECT" value="2"/>按品种汇总&nbsp;
                <input type="radio" name="RADIO_SELECT" value="3"/>已销售明细&nbsp;
                <input type="radio" name="RADIO_SELECT" value="4"/>服务商未点入库明细
                </td>
            </tr>
            
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查询"
                           onclick="__extQuery__(1);"/>
                    <input class="normal_btn" type="button" value="导出" onclick="expPartSalesDExcel();"/>
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        autoAlertException();//输出错误信息
        var myPage;
        var url = "<%=contextPath%>/report/partReport/partSalesReport/PartSalesDirectReport/queryPartSalesDirect.json";

        var title = null;
        var columns = null;
        var len = 0;

       // var calculateConfig = {subTotalColumns:" |DEALER_NAME"};

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
        		
        		var chk = 0;
        		var chkObjs = document.getElementsByName("RADIO_SELECT");
        		for(var i=0;i<chkObjs.length;i++){
        	        if(chkObjs[i].checked){
        	            chk = i;
        	            break;
        	        }
        	    }
        		
        		if(chk==0){//按单位汇总
        			columns = [
        			            {header: "序号", align: 'center', renderer: getIndex},
        			            {header: "服务商编码", dataIndex: 'DEALER_CODE',  style: 'text-align:left'},
        			            {header: "服务商", dataIndex: 'DEALER_NAME',  style: 'text-align:left'},
        			            {header: "直发箱数", dataIndex: 'ZFXS', align: 'center'},
        			            {header: "直发数量", dataIndex: 'ZFSL', align: 'center'},
        			            {header: "直发金额(无税)", dataIndex: 'ZFJE', align: 'center'},
        			            {header: "正常箱数", dataIndex: 'FZFXS', align: 'center'},
        			            {header: "正常数量", dataIndex: 'FZFSL', align: 'center'},
        			            {header: "正常金额(无税)", dataIndex: 'FZFJE', align: 'center'},
        			            {header: "总箱数", dataIndex: 'XS', align: 'center'},
        			            {header: "总数量", dataIndex: 'SL', align: 'center'},
        			            {header: "总金额(无税)", dataIndex: 'JE', align: 'center'},
        			            {header: "未入库箱数", dataIndex: 'WRKXS', align: 'center'},
        			            {header: "未入库数量", dataIndex: 'WRKSL', align: 'center'},
        			            {header: "未入库金额(无税)", dataIndex: 'WRKJE', align: 'center'}
        			        ];
        			
        		}else if(chk==1){//按品种汇总
        			columns = [
       			            {header: "序号", align: 'center', renderer: getIndex},
       			            {header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align:left'},
       			            {header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align:left'},
       			            {header: "配件件号", dataIndex: 'PART_CODE',  style: 'text-align:left'},
       			            {header: "直发数量", dataIndex: 'ZFSL', align: 'center'},
       			            {header: "直发金额", dataIndex: 'ZFJE', align: 'center'},
       			            {header: "正常数量", dataIndex: 'FZFSL', align: 'center'},
       			            {header: "正常金额", dataIndex: 'FZFJE', align: 'center'},
       			            {header: "总数量", dataIndex: 'SL', align: 'center'},
       			            {header: "总金额", dataIndex: 'JE', align: 'center'},
       			            {header: "未入库数量", dataIndex: 'WRKSL', align: 'center'},
       			            {header: "未入库金额", dataIndex: 'WRKJE', align: 'center'}
       			        ];
       			
        		}else if(chk==2){//已销售明细
        			columns = [
          			            {header: "序号", align: 'center', renderer: getIndex},
          			            {header: "服务商编码", dataIndex: 'DEALER_CODE',  style: 'text-align:left'},
   			                    {header: "服务商", dataIndex: 'DEALER_NAME',  style: 'text-align:left'},
          			            {header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align:left'},
          			            {header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align:left'},
          			            {header: "配件件号", dataIndex: 'PART_CODE',  style: 'text-align:left'},
          			            {header: "销售日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate},
          			            {header: "销售类型", dataIndex: 'STYPE', align: 'center'},
          			            {header: "含税单价", dataIndex: 'SALE_PRICE', align: 'center'},
          			            {header: "销售数量", dataIndex: 'OUTSTOCK_QTY', align: 'center'},
          			            {header: "销售金额(含税)", dataIndex: 'SALE_AMOUNT', align: 'center'}
          			        ];
          			
           		}else{//服务商未点入库明细
           			columns = [
         			            {header: "序号", align: 'center', renderer: getIndex},
         			            {header: "服务商编码", dataIndex: 'DEALER_CODE',  style: 'text-align:left'},
  			                    {header: "服务商", dataIndex: 'DEALER_NAME',  style: 'text-align:left'},
         			            {header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align:left'},
         			            {header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align:left'},
         			            {header: "配件件号", dataIndex: 'PART_CODE',  style: 'text-align:left'},
         			            {header: "审核通过日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate},
         			            {header: "销售数量", dataIndex: 'OUTSTOCK_QTY', align: 'center'},
         			            {header: "销售金额", dataIndex: 'SALE_AMOUNT', align: 'center'}
         			        ];
           		}
        		
        		len = columns.length;
        		
        		$("_page").hide();
        		$('myGrid').show();
        		new createGrid(title,columns, $("myGrid"),ps).load();			
        		//分页
        		myPage = new showPages("myPage",ps,url);
        		myPage.printHtml();
        		//hiddenDocObject(2);
        		var tbl = $("myTable");
    			var rowObj = tbl.insertRow(tbl.rows.length);
    			rowObj.className = "table_list_row1";
    			if(chk==0){
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
       			    var cell15 = rowObj.insertCell(14);
       			    cell1.innerHTML = '<tr><td></td>';
       			    cell2.innerHTML = '<td><strong>合计：</strong></td>';
       			    cell3.innerHTML = '<td></td>';
       			    cell4.innerHTML = '<td><strong>'+json.totalzfxs+'</strong></td>';
       			    cell5.innerHTML = '<td><strong>'+json.totalzfsl+'</strong></td>';
       			    cell6.innerHTML = '<td><strong>'+json.totalzfje+'</strong></td>';
       			    cell7.innerHTML = '<td><strong>'+json.totalfzfxs+'</strong></td>';
       			    cell8.innerHTML = '<td><strong>'+json.totalfzfsl+'</strong></td>';
       			    cell9.innerHTML = '<td><strong>'+json.totalfzfje+'</strong></td>';
       			    cell10.innerHTML = '<td><strong style="color: red" title="总箱数">'+json.totalxs+'</strong></td>';
       			    cell11.innerHTML = '<td><strong style="color: red" title="总数量">'+json.totalsl+'</strong></td>';
       			    cell12.innerHTML = '<td ><strong style="color: red" title="总无税金额">'+json.totalje+'</strong></td>';
       			    cell13.innerHTML = '<td><strong>'+json.totalwrkxs+'</strong></td>';
       			    cell14.innerHTML = '<td><strong>'+json.totalwrksl+'</strong></td>';
       			    cell15.innerHTML = '<td><strong>'+json.totalwrkje+'</strong></td>';
    			}else if(chk==1){
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
       			    cell1.innerHTML = '<tr><td></td>';
       			    cell2.innerHTML = '<tr><td></td>';
       			    cell3.innerHTML = '<td><strong>合计：</strong></td>';
       			    cell4.innerHTML = '<td></td>';
       			    cell5.innerHTML = '<td><strong>'+json.totalzfsl+'</strong></td>';
       			    cell6.innerHTML = '<td><strong>'+json.totalzfje+'</strong></td>';
       			    cell7.innerHTML = '<td><strong>'+json.totalfzfsl+'</strong></td>';
       			    cell8.innerHTML = '<td><strong>'+json.totalfzfje+'</strong></td>';
       			    cell9.innerHTML = '<td><strong style="color: red" title="总数量">'+json.totalsl+'</strong></td>';
       			    cell10.innerHTML = '<td ><strong style="color: red" title="总金额">'+json.totalje+'</strong></td>';
       			    cell11.innerHTML = '<td><strong>'+json.totalwrksl+'</strong></td>';
       			    cell12.innerHTML = '<td><strong>'+json.totalwrkje+'</strong></td>';
    			}
        	}else{
        		$("_page").show();
        		$("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据!</div>";
        		$("myPage").innerHTML = "";
        		removeGird('myGrid');
        		$('myGrid').hide();
        		hiddenDocObject(1);
        	}
        }

      //格式化日期
        function formatDate(value, meta, record) {
            var output = value.substr(0, 10);
            return output;
        }
      
        //导出
        function expPartSalesDExcel() {
            fm.action = "<%=contextPath%>/report/partReport/partSalesReport/PartSalesDirectReport/expPartSalesDExcel.do";
            fm.target = "_self";
            fm.submit();
        }
        
    </script>
</div>
</body>
</html>