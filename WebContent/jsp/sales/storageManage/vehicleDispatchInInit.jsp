<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
  <%@ page import="com.infodms.dms.common.Constant" %>
  <%@taglib uri="/jstl/cout" prefix="c" %>
  <% String contextPath = request.getContextPath(); %>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
    <title>退车车辆选择</title>
    <script type="text/javascript">
    var url =  "<%=contextPath%>/sales/storageManage/VehicleDispatch/vehicleDispatchInList.json?COMMAND=1";
    var path = '<%=contextPath%>/sales/storageManage/VehicleDispatch/';
	//MyAlert(url);
    var title = null;
    var columns = [{
        id: 'action',
        header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"transferIds\")' />",
        width: '6%',
        sortable: false,
        dataIndex: 'TRANSFER_ID',
        renderer: myCheckBox
    }, {
        header: "批发号",
        dataIndex: 'TRANSFER_NO',
        align: 'center'
    }, {
        header: "调入经销商",
        dataIndex: 'DEALER_SHORTNAME',
        align: 'center'
    }, {
        header: "调出经销商",
        dataIndex: 'OUT_DEALER_NAME',
        align: 'center'
    }, {
        header: "VIN",
        dataIndex: 'VIN',
        align: 'center'
    }, {
        header: "物料名称",
        dataIndex: 'MATERIAL_NAME',
        align: 'center'
    }, {
        header: "申请日期",
        dataIndex: 'APP_DATE',
        align: 'center'
    }, {
        header: "批发原因",
        dataIndex: 'TRANSFER_REASON',
        align: 'center'
    }, {
        header: "审批状态",
        dataIndex: 'CHECK_STATUS',
        align: 'center',
        renderer: getItemValue
    }];
    //全选checkbox
    function myCheckBox(value, metaDate, record){
        var input = "<input ";
        input += "type='checkbox' ";
        input += "name='transferIds' ";
        input += "value='" + record.data.TRANSFER_ID + "' ";
        input += " />";
        return String.format(input);
    }


    function DispatchIn(o){
        var chooseFlag = true;
        var aReturnInfo = document.getElementsByName("transferIds");
    	var warehouseId = document.getElementById("warehouseId");
        for (var i = 0; i < aReturnInfo.length; i++) {
            if (aReturnInfo[i].checked) {
                chooseFlag = false;
            }
        }
    	if (warehouseId.length == 0) {
            MyAlert("请选择入库仓库!");
            return false;
        }
    	
        if (chooseFlag) {
            MyAlert("请选择入库车辆!");
            return false;
        }
        else {
            MyConfirm("是否提交?", action);
        }
    }

    function action(){
        var aReturnInfo = document.getElementsByName("transferIds");
        var transferIds = "";//批发车辆
        for (var i = 0; i < aReturnInfo.length; i++) {
            if (aReturnInfo[i].checked) {
                transferIds = transferIds + aReturnInfo[i].value + ",";
            }
        }
        makeNomalFormCall(path + 'vehicleDispatchInSubmit.json?transferIds=' + transferIds, showResult, 'fm');
    }

    function showResult(json){
        if (json.returnValue == '1') {
            __extQuery__(1);
            document.getElementById("button2").disabled = false;
        }
        else {
            MyAlert("申请失败！请联系系统管理员！");
        }
    }
    </script>
  </head>
  <body>
    <div class="navigation">
      <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 库存管理 &gt; 批发入库
    </div>
    <form id="fm" name="fm" method="post">
      <input type="hidden" name="curPage" id="curPage" value="1" /><input type="hidden" id="dealerId" name="dealerId" value="" />
      <table class="table_query" border="0">
        <tr>
          <td width="20%" class="tblopt right">
              VIN：
          </td>
          <td width="39%">
            <textarea id="vin" name="vin" rows="2" class="form-control" style="width:150px;"></textarea>
          </td>
          <td class="table_query_3Col_input">
            <input type="hidden" name="exVeh" id="exVeh" value="" />
            	<input type="button"  class="u-button u-query" onclick="__extQuery__(1);" value="  查  询  " id="queryBtn" />
          </td>
        </tr>
      </table><jsp:include page="${contextPath}/queryPage/orderHidden.html" /><jsp:include page="${contextPath}/queryPage/pageDiv.html" />
      <table class="table_query" width="85%" align="center" border="0" id="roll">
        <tr>
          <td width="20%" class="tblopt right">
              入库仓库：
          </td>
          <td>
            <select id="warehouseId" name="warehouseId" class="u-select">
              <c:forEach var="item" items="${list}">
                <option id="${item.WAREHOUSE_ID}" value="${item.WAREHOUSE_ID}">${item.WAREHOUSE_NAME}</option>
              </c:forEach>
            </select>            
            <input name="button2" id="button2" type="button" class="u-button u-submit" onclick="DispatchIn(this);" value="批发入库" />
          </td>
          <td width="20%" align="left">
          </td>
        </tr>
      </table>
    </form>
  </body>
</html>