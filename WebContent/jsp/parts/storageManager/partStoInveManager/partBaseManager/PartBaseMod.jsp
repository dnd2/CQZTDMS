<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>配件基础信息修改</title>
<script language="javascript" type="text/javascript">
   function checkDropListItem(obj, itemvalue) {
       /*
        默认选中下拉列表框中的具有某个值的一项
        obj：下拉列表框对象
        itemvalue：该项值
        */
       for (i = 0; i < obj.options.length; i++) {
           if (obj.options[i].value == itemvalue)
               obj.selectedIndex = i;
       }
   }
   function goback() {
       parent.window._hide();
   }
   function savePart() {
	  	if(""==$("#PART_CODE")[0].value){
			MyAlert("请填写件号信息!");
			return;
		}
		if(""==$("#PART_CNAME")[0].value){
			MyAlert("请填写配件名称!");
			return;
		}
		if(""==$("#PRODUCE_STATE")[0].value){
			MyAlert("请选择配件种类!");
			return;
		}
		if(""==$("#PART_CATEGORY")[0].value){
			MyAlert("请选择配件类别!");
			return;
		}
		if(""==$("#MIN_PURCHASE")[0].value || $("#MIN_PURCHASE")[0].value < 1){
			MyAlert("请填写最小采购数量!");
			return;
		}
		if(""==$("#MIN_SALE")[0].value || $("#MIN_SALE")[0].value < 1){
			MyAlert("请填写最小销售数量!");
			return;
		}
		if(""==$("#MAX_SALE_VOLUME")[0].value || $("#MAX_SALE_VOLUME")[0].value < 1){
			MyAlert("请填写最大销售量!");
			return;
		}
		if($("#MIN_PURCHASE")[0].value > $("#MAX_SALE_VOLUME")[0].value){
			MyAlert("最小销售数量不能大于最大销售量!");
			return;
		}
		if(""==$("#IS_PROTOCOL_PACK")[0].value){
			MyAlert("请选择是否协议包装!");
			return;
		}
		if(""==$("#IS_MAG_BATCH")[0].value){
			MyAlert("请选择是否批次管理!");
			return;
		}
		     if(""==$("#IS_PART_DISABLE")[0].value){
			MyAlert("请选择是否停用!");
			return;
		}
// 	     if (submitForm('fm')) {
	         sendAjax('<%=contextPath%>/parts/storageManager/partStoInveManager/partBaseManager/partBaseMod.json', veiwParts, 'fm');
	
// 		}

   }
   function veiwParts(json) {
       if (json.succeed != null && json.succeed == "succeed") {
           layer.msg('修改成功!', {icon: 1});
           __parent().__extQuery__('${curPage}');
           parent._hide();
       } else {
           MyAlert(json.error);
       }
   }
   function genSelBoxMy(id, type, selectedKey, setAll, _class_, _script_, nullFlag, expStr) {
       var str = "";
       var arr;
       if (expStr.indexOf(",") > 0)
           arr = expStr.split(",");
       else {
           expStr = expStr + ",";
           arr = expStr.split(",");
       }
       str += "<select disabled id='" + id + "' name='" + id + "' class='" + _class_ + "' " + _script_;
       // modified by lishuai@infoservice.com.cn 2010-05-18 解决select下拉框中增加属性datatype判断bug begin
       if (nullFlag && nullFlag == "true") {
           str += " datatype='0,0,0' ";
       }
       // end
       str += " onChange=doCusChange(this.value);> ";
       if (setAll) {
           str += genDefaultOpt();
       }
       for (var i = 0; i < codeData.length; i++) {
           var flag = true;
           for (var j = 0; j < arr.length; j++) {
               if (codeData[i].codeId == arr[j]) {
                   flag = false;
               }
           }
           if (codeData[i].type == type && flag) {
               str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '" + codeData[i].codeDesc + "' >" + codeData[i].codeDesc + "</option>";
           }
       }
       str += "</select>";

       document.write(str);

   }
   function validateNum(obj) {
       var re = /^[1-9]+[0-9]*]*$/;
       if (!re.test(obj.value)) {
           layer.msg("请输入正整数!", {icon: 15});
           obj.value = "";
           return;
       }
   }
   function sel() {
       var selType = $("partCarTypes").value;
       for (var i = 0; i < selType.split(",").length; i++) {
           $("#checkType_" + selType.split(",")[i])[0].checked = true;
       }
       var selType2 = $("partCarSeries").value;
       for (var i = 0; i < selType2.split(",").length; i++) {
           $("#carSeries_" + selType2.split(",")[i])[0].checked = true;
       }
   }
   
	// 将复选框值通过“，”连接
   function joinTypes(tagName, hiddenId, hiddenName) {
       var chk = document.getElementsByName(tagName);
       var joinWords = ""
       var joinValues = ""
       for (var i = 0; i < chk.length; i++) {
           if (chk[i].checked) {
               var carType = chk[i].value;
               joinWords += carType + ",";
               joinValues += chk[i].id.replace(tagName+'_', '') + ',';
           }
       }
       document.getElementById(hiddenId).value = joinValues.substr(0, joinValues.length - 1);
       document.getElementById(hiddenName).value = joinWords.substr(0, joinWords.length - 1);
   }
   
// 修改是否协议包装时，是否批次包装值需变化
  function chageBaseFlag(obj){
  	var value = obj.value;
  	// 是协议包装时或类型时辅料时，不能批次包装
  	var protocolVal = $('#IS_PROTOCOL_PACK')[0].value;
  	var categoryVal = $('#PART_CATEGORY')[0].value;
  	if(value == <%=Constant.IF_TYPE_YES%> || value == <%=Constant.ZT_PB_PART_CATEGORY_ACCESSORIES%>){
		$("#IS_MAG_BATCH")[0].value = <%=Constant.IF_TYPE_NO%>;
		$("#IS_MAG_BATCH")[0].disabled = true;
		$('#IS_MAG_BATCH').css('background-color', '#cccccc');
	}else if(protocolVal != <%=Constant.IF_TYPE_YES%> && categoryVal != <%=Constant.ZT_PB_PART_CATEGORY_ACCESSORIES%>){
		$("#IS_MAG_BATCH")[0].disabled = false;
		$('#IS_MAG_BATCH').css('background-color', '#ffffff');
	}
}
</script>
</head>
<body>
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：
        配件管理 &gt; 基础信息管理 &gt;配件基础信息维护&gt; 配件主数据维护&gt; 修改
    </div>
    <form name="fm" id="fm" method="post">
        <input type="hidden" id="PART_ID" name="PART_ID" value="<c:out value="${ps.PART_ID}"/>"/>
        <input type="hidden" name="curPage" id="curPage" value="${curPage}"/>
        <input id="partCarTypes" name="partCarTypes" type="hidden" value="${partCarTypes}"/>
        <input id="partCarSeries" name="partCarSeries" type="hidden" value="${ps.SERIES_NAME}"/>
		<table class="table_query">
			<tr>
				<td class="right">件号：</td>
				<td>
					<input class="middle_txt" type="text" id="PART_CODE" name="PART_CODE" maxlength="200" value="<c:out value="${ps.PART_CODE}"/>" readonly disabled>
				</td>
				<td class="right" >配件名称：</td>
				<td>
					<input class="middle_txt" type="text" id="PART_CNAME" name="PART_CNAME"  maxlength="100" value="<c:out value="${ps.PART_CNAME}"/>"/>
					<font color="red">*</font>
				</td>
				<td class="right">英文名称:</td>
				<td>
					<input class="middle_txt" type="text" id="PART_ENAME" name="PART_ENAME" maxlength="100" value="<c:out value="${ps.PART_ENAME}"/>"/>
				</td>
			</tr>
			<tr>
				<td class="right">供应商件号：</td>
				<td >
					<input class="middle_txt" type="text" id="DLR_PART_ID" name="DLR_PART_ID" maxlength="100" value="<c:out value="${ps.DLR_PART_ID}"/>"/>
				</td>
				<td class="right">配件种类：</td>
				<td>
					<script type="text/javascript">
						genSelBox("PRODUCE_STATE", <%=Constant.PART_PRODUCE_STATE%>, "${ps.PRODUCE_STATE}", true, "short_sel u-select", "", "false", '');
					</script>
				</td>
				<td class="right">配件类别：</td>
				<td>
					<script type="text/javascript">
						genSelBox("PART_CATEGORY", <%=Constant.ZT_PB_PART_CATEGORY%>, "${ps.PART_CATEGORY}",true,"short_sel u-select","onchange='chageBaseFlag(this)'");
					</script>
					<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td class="right">是否可替代：</td>
				<td >
					<script type="text/javascript">
						genSelBox("IS_REPLACED",<%=Constant.IF_TYPE%>, "${ps.IS_REPLACED}",true,"short_sel u-select","");
					</script> 
				</td>
<!-- 				<td class="right">所属大类：</td> -->
<!-- 				<td> -->
<!-- 					<script type="text/javascript"> -->
<%-- 						genSelBox("POSITION", <%=Constant.PART_BASE_POSITION%>, "${ps.POSITION}", true, "short_sel u-select", "", "false", ''); --%>
<!-- 					</script> -->
<!-- 				</td> -->
<!-- 				<td class="right">材料属性：</td> -->
<!-- 				<td> -->
<!-- 					<script type="text/javascript"> -->
<%-- 						genSelBox("PART_MATERIAL", <%=Constant.PART_BASE_MATERIAL%>, "${ps.PART_MATERIAL}", true, "short_sel u-select", "", "false", ''); --%>
<!-- 					</script> -->
<!-- 				</td> -->
<!-- 			</tr> -->
<!-- 			<tr> -->
				<td class="right">单位：</td>
				<td>
					<select id="UOM" class="u-select" name="UOM">
						<option value="">-请选择-</option>
						<c:forEach items="${unit}" var="unit">
							<option value="${unit.fixName }" <c:if test="${unit.fixName eq ps.UNIT}">selected</c:if>>${unit.fixName }</option>
						</c:forEach>
					</select>
					<script>checkDropListItem(document.getElementById("UOM"), '${ps.UNIT}');</script>
				</td>
<!-- 				<td class="right">配件品牌：</td> -->
<!-- 				<td> -->
<!-- 					<script type="text/javascript"> -->
<%-- 						genSelBox("PRODUCE_FAC", <%=Constant.YIELDLY%>, "${ps.PRODUCE_FAC}", true, "short_sel u-select", "", "false", ''); --%>
<!-- 					</script> -->
<!-- 				</td> -->
<!-- 				<td class="right">是否常备：</td> -->
<!-- 				<td> -->
<!-- 					<script type="text/javascript"> -->
<%-- 						genSelBox("IS_PERMANENT", <%=Constant.IF_TYPE%>, "${ps.IS_PERMANENT}", true, "short_sel u-select", "", "false", ''); --%>
<!-- 					</script> -->
<!-- 					<font color="red">*</font> -->
<!-- 				</td> -->
				<td class="right" >车系：</td>
				<td>
					<input type="text" class="middle_txt" id="MODEL_CODE" name="MODEL_CODE" maxlength="30" value="${ps.MODEL_CODE}"/>
				</td>
			</tr>
			<tr>
				<td class="right">适用车系：</td>
				<td colspan="5" align="left">
					<input type="hidden" class="normal_txt" id="SERIES_ID" name="SERIES_ID" value="${ps.SERIES_ID}"/>
					<input type="hidden" class="normal_txt" id="SERIES_NAME" name="SERIES_NAME" value="${ps.SERIES_NAME}"/>
					<c:if test="${not empty carInfo.carSeriesList}">
						<c:forEach items="${carInfo.carSeriesList}" var="var" varStatus="status" step="1">
							<label class="u-checkbox">
								<input type="checkbox" value="${var.FIX_NAME}" name="carSeries" id="carSeries_${var.FIX_VALUE}" 
								onclick="joinTypes(this.name, 'SERIES_ID', 'SERIES_NAME');" 
								<c:if test="${not empty carInfo.carSeriesChkeds}">
									<c:forEach items="${carInfo.carSeriesChkeds}" var="chekVar" varStatus="status" step="1">
										<c:if test="${chekVar eq var.FIX_VALUE}">checked</c:if>
									</c:forEach>
								</c:if>
								/>
								<span></span>
							</label>		
							<label class="u-label">${var.FIX_NAME}</label>&nbsp
						</c:forEach>
					</c:if>
				</td>
			</tr>
			<tr>
				<td class="right">适用车型：</td>
				<td colspan="5" align="left">
					<input type="hidden" class="normal_txt" id="MODEL_ID" name="MODEL_ID" value="${ps.MODEL_ID}"/>
					<input type="hidden" class="normal_txt" id="MODEL_NAME" name="MODEL_NAME" value="${ps.MODEL_NAME}"/>
					<c:if test="${not empty carInfo.carTypeList}">
						<c:forEach items="${carInfo.carTypeList}" var="var" varStatus="status" step="1">
							<label class="u-checkbox">
								<input type="checkbox" value="${var.FIX_NAME}" name="carModel" id="carModel_${var.FIX_VALUE}" 
								onclick="joinTypes(this.name, 'MODEL_ID', 'MODEL_NAME');" 
								<c:if test="${not empty carInfo.carTypeChkeds}">
									<c:forEach items="${carInfo.carTypeChkeds}" var="chekVar" varStatus="status" step="1">
										<c:if test="${chekVar eq var.FIX_VALUE}">checked</c:if>
									</c:forEach>
								</c:if>
								/>
								<span></span>
							</label>
							<label class="u-label">${var.FIX_NAME}</label>
						</c:forEach>
					</c:if>
				</td>
			</tr>
			<tr>
				<td class="right">最小采购数量：</td>
				<td>
					<input type="text"  class="middle_txt" id="MIN_PURCHASE" name="MIN_PURCHASE" onkeyup="removeNotNum(this)" 
					onchange="removeNotNum(this)" maxlength="10" value="${ps.MIN_SALE}"/>
				</td>
				<td class="right">最小销售数量：</td>
				<td>
					<input type="text"  class="middle_txt" id="MIN_SALE" name="MIN_SALE" onkeyup="removeNotNum(this)" 
					onchange="removeNotNum(this)" maxlength="10" value="${ps.MIN_PURCHASE}"/>
				</td>
				<td class="right">最大销售量：</td>
				<td>
					<input type="text"  class="middle_txt" id="MAX_SALE_VOLUME" name="MAX_SALE_VOLUME" onkeyup="removeNotNum(this)" 
					onchange="removeNotNum(this)" maxlength="10" value="${ps.MAX_SALE_VOLUME}"/>
				</td>
			</tr>
			<tr>
				<td class="right">长：</td>
				<td>
					<input type="text" class="middle_txt" id="LENGTH" name="LENGTH" datatype="1,is_digit,10" maxlength="10"
						value="<c:out value="${ps.LENGTH}"/>"/>&nbsp;mm
				</td>
				<td class="right">宽：</td>
				<td>
					<input type="text" class="middle_txt" id="WIDTH" name="WIDTH" datatype="1,is_digit,10" maxlength="10"
						value="<c:out value="${ps.WIDTH}"/>"/>&nbsp;mm
				</td>
				<td class="right">高：</td>
				<td>
					<input type="text" class="middle_txt" id="HEIGHT" name="HEIGHT" datatype="1,is_digit,10" maxlength="10"
						value="<c:out value="${ps.HEIGHT}"/>"/>&nbsp;mm
				</td>
			</tr>
			<tr>
				<td class="right">净重：</td>
				<td>
					<input type="text" class="middle_txt" id="WEIGHT" name="WEIGHT" maxlength="10"
						value="<c:out value="${ps.WEIGHT}"/>"/>&nbsp;g
				</td>
				<td class="right">体积：</td>
				<td>
					<input type="text" class="middle_txt" id="VOLUME" name="VOLUME" maxlength="10"
						value="<c:out value='${ps.VOLUME}'/>"/>&nbsp;mm3
				</td>
				<td class="right">采购方式：</td>
				<td>
					<script type="text/javascript">
						genSelBox("PRODUCE_FAC",<%=Constant.PURCHASE_WAY%>,"${ps.PRODUCE_FAC}",true,"short_sel u-select","");
					</script>
				</td>
			</tr><tr>
				<td class="right">单车用量：</td>
				<td>
					<input type="text" class="middle_txt" id="VEHICLE_VOLUME" name="VEHICLE_VOLUME" onchange="validateNum(this)" 
					maxlength="10" value="${ps.VEHICLE_VOLUME}"/>
				</td>
				<td class="right">装配：</td>
				<td>
					<script type="text/javascript">
						genSelBox("PART_FIT",<%=Constant.ZT_PB_PART_FIT%>,"${ps.PART_FIT}",true,"short_sel u-select","");
					</script>
				</td>
				<td class="right">包装规格：</td>
				<td>
					<input type="text" class="middle_txt" id="PACK_SPECIFICATION" name="PACK_SPECIFICATION" maxlength="10" value="${ps.PACK_SPECIFICATION}"/>
				</td>
			<tr>
			</tr>
				<td class="right">是否停用：</td>
				<td>
					<script type="text/javascript">
						genSelBox("IS_PART_DISABLE",<%=Constant.IF_TYPE%>,"${ps.IS_PART_DISABLE}",true,"short_sel u-select","");
					</script>
					<font color="red">*</font>
				</td>
				<td class="right">是否售完停用：</td>
				<td>	
					<script type="text/javascript">
						genSelBox("IS_SALE_DISABLE",<%=Constant.IF_TYPE%>,"${ps.IS_SALE_DISABLE}",true,"short_sel u-select","");
					</script>
				</td>
				<td class="right">是否停止装车：</td>
				<td>
					<script type="text/javascript">
						genSelBox("IS_STOP_LOAD",<%=Constant.IF_TYPE%>,"${ps.IS_STOP_LOAD}",true,"short_sel u-select","");
					</script>
				</td>
			</tr>
			</tr>
				<td class="right">停用日期：</td>
				<td>
					<input class="middle_txt" readonly="readonly"  type="text" id="PART_DISABLE_DATE" name="PART_DISABLE_DATE" 
					group="staReportDate,endReportDate" datatype="1,is_date,10" value="${ps.PART_DISABLE_DATE_F }"/>
					<input class="time_ico" type="button" />
				</td>
				<td class="right">售完停用日期：</td>
				<td>	
					<input class="middle_txt" readonly="readonly"  type="text" id="SALE_DISABLE_DATE" name="SALE_DISABLE_DATE" 
					group="staReportDate,endReportDate" datatype="1,is_date,10" value="${ps.SALE_DISABLE_DATE_F }"/>
					<input class="time_ico" type="button" />
				</td>
				<td class="right">停止装车日期：</td>
				<td>
					<input class="middle_txt" readonly="readonly"  type="text" id="STOP_LOAD_DATE" name="STOP_LOAD_DATE" 
					group="staReportDate,endReportDate" datatype="1,is_date,10" value="${ps.STOP_LOAD_DATE_F }"/>
					<input class="time_ico" type="button" />
				</td>
			</tr>
			<tr>
				<td class="right">是否校验防伪：</td>
				<td>
					<script type="text/javascript">
						genSelBox("IS_SECURITY",<%=Constant.IF_TYPE%>,"${ps.IS_SECURITY}",true,"short_sel u-select","");
					</script>
				</td>
				<td class="right">是否协议包装：</td>
				<td>
					<script type="text/javascript">
						genSelBox("IS_PROTOCOL_PACK",<%=Constant.IF_TYPE%>,"${ps.IS_PROTOCOL_PACK}",true,"short_sel u-select","onchange='chageBaseFlag(this)'");
					</script>
					<font color="red">*</font>
				</td>
				<td class="right">是否批次管理：</td>
				<td>
					<script type="text/javascript">
						var disableStr = ""
						if("${ps.IS_PROTOCOL_PACK}" == <%=Constant.IF_TYPE_YES%>){
							disableStr = "disabled"
						}
						genSelBox("IS_MAG_BATCH",<%=Constant.IF_TYPE%>,"${ps.IS_MAG_BATCH}",true,"short_sel u-select",disableStr);
					</script>
					<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td class="right">是否委托发货：</td>
				<td>
					<script type="text/javascript">
						genSelBox("IS_ENTRUSR_PACK",<%=Constant.IF_TYPE%>,"${ps.IS_ENTRUSR_PACK}",true,"short_sel u-select","");
					</script>
				</td>
				<td class="right">3C标识：</td>
				<td colspan="3">
					<script type="text/javascript">
						genSelBox("CCC_FLAG",<%=Constant.IF_TYPE%>,"${ps.CCC_FLAG}",true,"short_sel u-select","");
					</script>
				</td>
			</tr>
			<tr>
				<td class="right">备注：</td>
				<td colspan="5">
				<textarea id="REMARK" class="form-control remark align" name="REMARK" cols="100" rows="4" maxlength="200"
						value="<c:out value="${ps.REMARK}"/>">${ps.REMARK}</textarea>
				</td>
			</tr>
		</table>
		<table class="table_edit" width="100%">
			<tr>
				<td align="center">
					<input type="button" name="saveBtn1" id="saveBtn1" value="保存" onclick="savePart();"
						class="normal_btn"/>
					<input type="button" name="backBtn" id="backBtn" value="关闭" onclick="javascript:goback();"
						class="normal_btn u-cancel"/>
				</td>
			</tr>
		</table>
    </form>
</div>
</body>
</html>
