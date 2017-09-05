<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件_采购计划计算公式</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script language="javascript" type="text/javascript">
	function doInit(){
		loadcalendar();  //初始化时间控件
		getDate();
	}
</script>
</head>
<body onbeforeunload="returnBefore();">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;配件采购计划计算公式</div>
  <form name='fm' id='fm' method="post">
  <input type="hidden" id="planType" name="planType" value="92111001">
  <table class="table_query">
  	   <tr >
        <td width="10%" align="right">参考时间段：</td>
		<td width="22%"><input id="referSDate" class="short_txt"
			name="referSDate" datatype="1,is_date,10" maxlength="10"
			group="referSDate,referEDate" /> <input class="time_ico"
			onclick="showcalendar(event, 'referSDate', false);" value=" "
			type="button" />至 <input id="referEDate"
			class="short_txt" name="referEDate" datatype="1,is_date,10"
			maxlength="10" group="referSDate,referEDate" /> <input
			class="time_ico" onclick="showcalendar(event, 'referEDate', false);"
			value=" " type="button" />
		</td>
       </tr>
       <tr >            
        <td width="10%" align="right">配件种类：</td>
		<td width="30%">
		  <script type="text/javascript">
        	 genSelBoxExp("partType",<%=Constant.PART_BASE_PART_TYPES%>,"",true,"short_sel","","false",'');
          </script>
		</td>
       </tr>
       <tr >
        <td width="10%"   align="right">库房：</td>
		<td width="30%">
		  <select name="wh_id" id="wh_id" class="short_sel">
               <c:forEach items="${wareHouseList}" var="wareHouse">
                   <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
               </c:forEach>
           </select>
           <font color="red">*</font>
		</td>
       </tr>
       <tr >
        <td width="10%"   align="right">订货周期：
        </td>            
        <td width="30%">
			<input  class="middle_txt" id="planCycle"  name="planCycle" type="text" value="" onchange="dataTypeCheck(this)"/>
			<font color="red">*</font>
        </td>
       </tr>
       <tr >
        <td width="10%"   align="right">到货周期：
        </td>            
        <td width="30%">
			<input  class="middle_txt" id="arriveCycle"  name="arriveCycle" type="text" value="" onchange="dataTypeCheck(this)"/>
			<font color="red">*</font>
        </td>
       </tr>
       <tr >
        <td width="10%"   align="right">供应商：</td>
		<td width="30%">
		  <select id="venderId" name="venderId" class="long_sel">
              <option value="">-请选择-</option>
              <c:forEach items="${venderList}" var="venderList">
                  <option value="${venderList.VENDER_ID}">${venderList.VENDER_NAME}</option>
              </c:forEach>
          </select>
		</td>
       </tr>
       <tr >
        <td width="10%"   align="right">考虑BO：
        </td>            
        <td width="30%">
			<input type="radio" name="boRadio" value="1" checked="checked" /> 考虑BO
			&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="boRadio" value="0" /> 不考虑BO
        </td>
       </tr>
       <tr >
        <td width="10%" align="right">BO时间段：</td>
		<td width="22%"><input id="checkSDate" class="short_txt"
			name="checkSDate" datatype="1,is_date,10" maxlength="10"
			group="checkSDate,checkEDate" /> <input class="time_ico"
			onclick="showcalendar(event, 'checkSDate', false);" value=" "
			type="button" />至 <input id="checkEDate"
			class="short_txt" name="checkEDate" datatype="1,is_date,10"
			maxlength="10" group="checkSDate,checkEDate" /> <input
			class="time_ico" onclick="showcalendar(event, 'checkEDate', false);"
			value=" " type="button" />
		</td>
       </tr>
       <tr >
        <td width="10%"   align="right">默认供应商：
        </td>            
        <td width="30%">
			<input type="radio" name="venderRadio" value="1" checked="checked" /> 是
			&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="venderRadio" value="0" /> 否
        </td>
       </tr>
      <tr >
          <td width="10%"   align="right">过滤负数：
          </td>
          <td width="30%">
              <input type="radio" name="filtNeg" value="1"  checked="checked"/> 是
              &nbsp;&nbsp;&nbsp;&nbsp;
              <input type="radio" name="filtNeg" value="0" /> 否
          </td>
      </tr>
       <tr >
        <td width="10%"   align="right">计算公式：
        </td>
        <td width="30%">
        	<textarea rows="4" cols="30" style="text-align: left;" readonly="readonly">安全库存+日均销量*(订货周期(天)+到货周期(天))+BO数量-可用库存-在途（已下订单单未入库数量）</textarea>
        </td>
       </tr>
      <td width="10%"   align="right">提示：
      </td>
      <td width="30%">
          整个过程可能需要10-60秒，请耐心等待.....
      </td>

      </tr>
       <tr style="line-height: 30px;">
         <td colspan="6" align="center">

          <input class="normal_btn" type="button" value="计 算" name="BtnQuery" id="saveBtn" onclick="batchAddConfirm()"/>
		  <input class="normal_btn" type="button" name="button1" value="关 闭"  onclick="_hide();"/>
         </td>
       </tr>       
 	</table>
 	<br/>
 	<script type="text/javascript" >

 	function dataTypeCheck(obj)
	{
 		var value = obj.value;
	    if (isNaN(value)) {
	        MyAlert("请输入数字!");
	        obj.value = "";
	        return;
	    }
	    var re = /^[1-9]+[0-9]*]*$/;
	    if (!re.test(obj.value)) {
	        MyAlert("请输入正整数!");
	        obj.value = "";
	        return;
	    }
	}


 	function batchAddConfirm()
 	{
 		var whId = document.getElementById("wh_id").value;
 	 	var orderCycle = document.getElementById("planCycle").value;
 	 	var reachCycle = document.getElementById("arriveCycle").value;

 	 	if(null == whId || "" == whId)
 	 	{
 	 	 	MyAlert("库房不能为空!");
 	 	 	return false;
 	 	}
 	 	if(null == orderCycle || "" == orderCycle)
 	 	{
 	 	 	MyAlert("订货周期不能为空!");
 	 	 	return false;
 	 	}
 	 	if(null == reachCycle || "" == reachCycle)
 	 	{
 	 	 	MyAlert("到货周期不能为空!");
 	 	 	return false;
 	 	}
 	 	if(confirm("确认新增计划?"))
		{
 	 		batchAddCommit();
		}
 	}

 	function batchAddCommit()
 	{
 		btnDisable();
        var url = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/batchAddPlan.json";
        sendAjax(url, getResult, 'fm');
 	}
    function getResult(jsonObj) {
        if (jsonObj != null) {
            var success = jsonObj.success;
            var error = jsonObj.error;
            var exceptions = jsonObj.Exception;
            if (success) {
                MyAlert(success);
                _hide();
                __extQuery__(1);
            } else if (error) {
                MyAlert(error);
            } else if (exceptions) {
                MyAlert(exceptions.message);
            }
        }
    }
 	function showResult(json) {
		btnEnable();
	    if (json.errorExist != null && json.errorExist.length > 0) {
	    	MyAlert(json.errorExist);
	    } else if (json.success != null && json.success == "true") {
	    	MyAlert("批量新增成功!");
			_hide();        
	    } else {
	        MyAlert("批量新增失败，请联系管理员!");
	    }
	}

 	function getDate()
	{
		var dateS = "";
		var dateE = "";
		var myDate = new Date();
	    var year = myDate.getFullYear();   //获取完整的年份(4位,1970-????)
	    var moth = myDate.getMonth() ;      //获取当前月份(0-11,0代表1月)
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

	    document.getElementById("checkSDate").value = dateS;
	    document.getElementById("checkEDate").value = dateE;

	    document.getElementById("referSDate").value = dateE;
	    document.getElementById("referEDate").value = dateE;
	}
 	
 	//关闭
 	function returnBefore()
 	{
 		parentContainer.__extQuery__(1);	
 	}

 	//失效按钮
	function btnDisable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });

	}

	//有效按钮
	function btnEnable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = "";
	    });

	}
 	</script>
</form>
</body>
</html>