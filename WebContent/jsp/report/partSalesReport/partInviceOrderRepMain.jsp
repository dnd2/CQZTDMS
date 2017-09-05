<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <script type="text/javascript">
    	jQuery.noConflict();
        var myPage;
        var url = "<%=contextPath%>/report/partReport/partSalesReport/PartInvoiceOrderRep/query.json";
        var title = null;
																
        var columns = [
 			{header: "序号", align:"center", renderer: getIndex},
        	{header: "服务商代码", dataIndex: "DEALER_CODE", align:"center"},
        	{header: "服务商名称", dataIndex: "DEALER_NAME", align:"center"},
        	{header: "是否出口", dataIndex: "IS_EXPORT", align:"center"},
        	{header: "销售单号", dataIndex: "SO_CODE", align:"center"},
        	{header: "销售退货单号", dataIndex: "RETURN_CODE", align:"center"},
        	{header: "配件代码", dataIndex: "PART_OLDCODE", align:"center"},
        	{header: "配件名称", dataIndex: "PART_CNAME", align:"center"},
        	{header: "件号", dataIndex: "PART_CODE", align:"center"},
        	{header: "发票号码", dataIndex: "INVOICE_NO", align:"center"},
        	{header: "实际出库数量", dataIndex: "OUTSTOCK_QTY", align:"center"},
        	{header: "计划价", dataIndex: "SALE_PRICE3", align:"center"},
        	{header: "计划金额", dataIndex: "AMOUNT", align:"center"},
            {header: "销售单价", dataIndex: "SALE_PRICE", align:"center"},
            {header: "销售金额", dataIndex: "SALE_AMOUNT", align:"center"},
        	{header: "车型", dataIndex: "MODEL_NAME", align:"center"},
        	{header: "配件类型", dataIndex: "PART_TYPE", align:"center"},
        	{header: "备注", dataIndex: "REMARK", align:"center"}
      ];
        //ready事件
        jQuery(function(){
        	jQuery("#queryBtn").click(function(){
        		__extQuery__(1);
        	});
        	jQuery("#exportBtn").click(function(){
        		exportExcel();
        	});
        	loadcalendar();
        	autoAlertException();
        	//__extQuery__(1);
        })
       function query(){
    	   __extQuery__(1);
       }
       function exportExcel(){
    	   fm.action = "<%=contextPath%>/report/partReport/partSalesReport/PartInvoiceOrderRep/exportExcel.do";
           fm.submit();
       }

        function getDate()
        {
            var dateS = "";
            var dateE = "";
            var myDate = new Date();
            var year = myDate.getFullYear();   //获取完整的年份(4位,1970-????)
            var moth = myDate.getMonth();      //获取当前月份(0-11,0代表1月)
            if(moth < 10)
            {
            	if(0 < moth)
        	    {
        	    	moth = "0" + moth;
        	    }
        	    else
        	    {
        	    	year = myDate.getFullYear() - 1;
        	    	moth = moth + 12;
        	    	if(moth < 10)
        		    {
        	    		moth = "0" + moth;
        		    }
        	    }
            }
            var day = myDate.getDate();       //获取当前日(1-31)
            if(day < 10)
            {
                day = "0" + day;
            }

            dateS = year + "-" + moth + "-" + day;

            moth = myDate.getMonth() + 1;	//获取当前月份(0-11,0代表1月)
            if(moth < 10)
            {
                moth = "0" + moth;
            }

            dateE = myDate.getFullYear() + "-" + moth + "-" + day;

           /* document.getElementById("beginTime").value = dateS;
            document.getElementById("endTime").value = dateE;*/
        }
        function callBack(json){
            var ps;
            var chkCount = json.outline_id;
            var chkNum = json.outstock_qty;
            var planAmount = json.plan_amount==null?0:json.plan_amount;
            var chkfreight = json.freight==null?0:json.freight;
            var chkAmount = json.sale_amount==null?0:json.sale_amount;

            $("INV_COUNT").value=chkCount;
            $("INV_NUM").value=chkNum;
            $("INV_AMOUNT").value=chkAmount;
            $("INV_FREIGHT").value=chkfreight;
            $("AMOUNT").value=(chkAmount+chkfreight).toFixed(2);
            $("PLAN_AMOUNT").value=planAmount;
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
</head>
<body enctype="multipart/form-data" onload="getDate();">
<form name="fm" id="fm" method="post">
    <div id="div1" class="wbox">
        <div class="navigation"><img src="<%=contextPath %>/img/nav.gif" alt=""/>&nbsp;当前位置： 报表管理 &gt;配件报表&gt;本部销售报表&gt;已开票销售统计</div>
        <table border="0" class="table_query">
            <th colspan="6" width="100%"><img class="nav" src="<%=contextPath %>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <td width="10%"   align="right">服务商编码：</td>
                <td width="20%">
					<input type="text" id="orgCode" name="orgCode" />
				</td>
                <td width="10%"   align="right">订货单位名称：</td>
                <td width="20%">
					<input type="text" id="orgName" name="orgName" />
				</td>
				<td width="10%"   align="right">销售单号：</td>
               	<td width="20%">
					<input type="text" id="soCode" name="soCode" />
				</td>
            </tr>
            <tr>
                <td width="10%" align="right">开票日期：</td>
                <td width="20%">
                    <input name="beginTime" id="beginTime" value="${old}" type="text" class="short_txt" datatype="1,is_date,10" group="beginTime,endTime">
                    <input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 'beginTime', false);"/>
                    &nbsp;至&nbsp;
                    <input name="endTime" id="endTime" value="${now}" type="text" class="short_txt" datatype="1,is_date,10" group="beginTime,endTime">
                    <input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 'endTime', false);"/>
                </td>
				<td width="10%"   align="right">发票号：</td>
               	<td width="20%">
					<input type="text" id="invoiceNo" name="invoiceNo" />
				</td>
                <td width="10%"   align="right">销售退货单号：</td>
                <td width="20%">
                    <input type="text" id="returnCode" name="returnCode" />
                </td>
            </tr>
            <tr>
                <td width="10%"   align="right">配件编码：</td>
                <td width="20%">
					<input type="text" id="partOldcode" name="partOldcode" />
				</td>
                <td width="10%"   align="right">配件名称：</td>
                <td width="20%">
					<input type="text" id="partCname" name="partCname" />
				</td>
				<td width="10%"   align="right">配件件号：</td>
               	<td width="20%">
					<input type="text" id="partCode" name="partCode" />
				</td>
            </tr>
            <tr>
                <td width="10%"   align="right">车型：</td>
                <td width="20%">
					<input type="text" id="modelName" name="modelName" />
				</td>
                <td width="10%"   align="right">配件类型：</td>
                <td width="20%">
					<script type="text/javascript">
                    	genSelBoxExp("partType", <%=Constant.PART_BASE_PART_TYPES%>, "", true, "short_sel", "", "false", '');
               		</script>
				</td>
                <td width="10%"   align="right">是否出口：</td>
                <td width="20%">
                    <script type="text/javascript">
                        genSelBoxExp("isExport", <%=Constant.IF_TYPE%>, "", true, "short_sel", "", "false", '');
                    </script>
                </td>
				</td>
            </tr>
            <tr>
                <td width="10%" align="right">总项数&总数量：</td>
                <td width="20%">
                    <input id="INV_COUNT" type="text" class="short_txt" value="" style="background-color: #99D775;" readonly="readonly"/>
                    <input id="INV_NUM"  type="text" class="short_txt" value="" style="background-color: #99D775;" readonly="readonly"/>
                </td>
                <td width="10%" align="right">总计划金额&总运费&总配件金额：</td>
                <td width="20%">
                    <input id="PLAN_AMOUNT"  type="text" class="short_txt" value="" style="background-color: #99D775;" readonly="readonly"/>
                    <input id="INV_FREIGHT"  type="text" class="short_txt" value="" style="background-color: #99D775;" readonly="readonly"/>
                    <input id="INV_AMOUNT"  type="text" class="short_txt" value="" style="background-color: #99D775;" readonly="readonly"/>
                </td>
                <td width="10%" align="right">总开票金额：</td>
                <td width="20%">
                    <input id="AMOUNT" class="middle_txt" type="text" value="" style="background-color: #99D775;" readonly="readonly"/>
                </td>
            </tr>
           <tr>
                <td colspan="6" align="center">
            	    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button"  value="查 询" />&nbsp;
                	<input name="BtnQuery" id="exportBtn" class="normal_btn" type="button"  value="导 出" />
                    <font style="color: red">此处金额为无税金额</font>
                </td>
            </tr>
        </table>
    </div>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>
</body>
</html>
