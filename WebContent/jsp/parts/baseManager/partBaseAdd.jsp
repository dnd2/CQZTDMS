<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>demo</title>
<script language="javascript" type="text/javascript">
//设置超链接  begin      
function checkDropListItem(obj,itemvalue)
{
    /*
     默认选中下拉列表框中的具有某个值的一项
     obj：下拉列表框对象
     itemvalue：该项值
     */
    for(i=0;i<obj.options.length;i++)
    {
        if(obj.options[i].value==itemvalue)
            obj.selectedIndex=i;
    }
}

function goback(){
	parent.window._hide();
}
	
function add(){
	if(""==$("#PART_CODE")[0].value){
		layer.msg("请填写件号信息!");
		return;
	}
	if(""==$("#PART_CNAME")[0].value){
		layer.msg("请填写配件名称!");
		return;
	}
	if(""==$("#PRODUCE_STATE")[0].value){
		layer.msg("请选择配件种类!");
		return;
	}
	if(""==$("#PART_CATEGORY")[0].value){
		layer.msg("请选择配件类别!");
		return;
	}
	if(""==$("#MIN_PURCHASE")[0].value || $("#MIN_PURCHASE")[0].value < 1){
		layer.msg("请填写最小采购数量!");
		return;
	}
	if(""==$("#MIN_SALE")[0].value || $("#MIN_SALE")[0].value < 1){
		layer.msg("请填写最小销售数量!");
		return;
	}
	if(""==$("#MAX_SALE_VOLUME")[0].value || $("#MAX_SALE_VOLUME")[0].value < 1){
		layer.msg("请填写最大销售量!");
		return;
	}
       if($("#MIN_PURCHASE")[0].value > $("#MAX_SALE_VOLUME")[0].value){
		layer.msg("最小销售数量不能大于最大销售量!");
		return;
       }
       if(""==$("#IS_PROTOCOL_PACK")[0].value){
		layer.msg("请选择是否协议包装!");
		return;
	}
       if(""==$("#IS_MAG_BATCH")[0].value){
		layer.msg("请选择是否批次管理!");
		return;
	}
       if(""==$("#IS_PART_DISABLE")[0].value){
		layer.msg("请选择是否停用!");
		return;
	}

// 		if(submitForm('fm')){
// 			if(confirm("确定新增？")){
				sendAjax('<%=contextPath%>/parts/baseManager/PartBaseQuery/validate.json?PART_CODE='+$("#PART_CODE").val(),showResult,'fm');
// 			}
// 		}
}
    function showResult(json){
    	if(json.error!=''){
    		layer.msg(json.error);
    	}else{
    		addPart();
    	}
    }
    function addPart(){
		sendAjax('<%=contextPath%>/parts/baseManager/PartBaseQuery/partBaseSave.json',veiwParts,'fm');
	}
	function veiwParts(json){
		if(json.succeed != null && json.succeed == "succeed") {
			layer.msg('新增成功!');
// 		    parentContainer.__extQuery__(1);
			__parent().__extQuery__(1);
			parent._hide();
		}else {
			layer.msg('系统异常请联系管理员!');
		}
	}
	
// 验证正整数
function validateNum(obj){
 	var re = /^[1-9]+[0-9]*]*$/;
    if (!re.test(obj.value)) {
        layer.msg("请输入正整数!");
        obj.value = "";
        return;
    }
}

//移除非数字字符
function removeNotNum(obj){
	var str = $(obj).value.replace(/\D/g,'');
	$(obj).val() = str;
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

$(function() {
 
});
 
</script>
</head>
<body>
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt;  基础信息管理 &gt;配件基础信息维护&gt; 配件主数据维护 &gt;新增</div>
	<form name="fm" id="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="${curPage}" />
			<table class="table_query">
				<tr>
					<td class="right">件号：</td>
					<td>
						<input class="middle_txt" type="text" id="PART_CODE" name="PART_CODE"  maxlength="100"/>
						<font color="red">*</font>
					</td>
					<td class="right" align="right">配件名称：</td>
					<td>
						<input class="middle_txt" type="text" id="PART_CNAME" name="PART_CNAME"  maxlength="100"/>
						<font color="red">*</font>
					</td>
					<td class="right"class="table_edit_3Col_label_7Letter" align="right">英文名称:</td>
					<td >
						<input class="middle_txt" type="text" id="PART_ENAME" name="PART_ENAME" maxlength="100"/>
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td class="right">供应商件号：</td>
					<td >
						<input class="middle_txt" type="text" id="DLR_PART_ID" name="DLR_PART_ID" maxlength="100"/>
					</td>
					<td class="right">配件种类：</td>
					<td >
						<script type="text/javascript">
							genSelBox("PRODUCE_STATE",<%=Constant.PART_PRODUCE_STATE%>,"",true,"","");
						</script>
						<font color="red">*</font>
					</td>
					<td class="right"align="right">配件类别：</td>
					<td >
						<script type="text/javascript">
							genSelBox("PART_CATEGORY",<%=Constant.ZT_PB_PART_CATEGORY%>,"<%=Constant.ZT_PB_PART_CATEGORY_FITTING%>",true,"","onchange='chageBaseFlag(this)'");
						</script>
					</td>
				</tr>
				<tr >
					<td class="right"class="table_edit_3Col_label_7Letter" align="right">是否可替代：</td>
					<td >
						<script type="text/javascript">
							genSelBox("IS_REPLACED",<%=Constant.IF_TYPE%>,"<%=Constant.IF_TYPE_NO%>",true,"","");
						</script> 
					</td>
<!-- 					<td class="right">材料属性：</td> -->
<!-- 					<td > -->
<!-- 						<script type="text/javascript"> -->
<%-- 							genSelBox("PART_MATERIAL",<%=Constant.PART_BASE_MATERIAL%>,"",true,"",""); --%>
<!-- 						</script> -->
<!-- 					</td> -->
<!-- 					<td class="right"align="right">所属大类：</td> -->
<!-- 					<td> -->
<!-- 						<script type="text/javascript"> -->
<%-- 							genSelBox("POSITION",<%=Constant.PART_BASE_POSITION%>,"<%=Constant.PART_BASE_POSITION_ENGINE3%>",true,"",""); --%>
<!-- 						</script> -->
<!-- 					</td> -->
<!-- 				</tr> -->
<!-- 				</tr> -->
					<td class="right">单位：</td>
					<td>
						<select class="u-select" id="UOM" name="UOM">
						<option value="">-请选择-</option>
							<c:forEach items="${ps.unitList}" var="unit">
								<option  value="${unit.FIX_NAME }" >${unit.FIX_NAME }</option>
							</c:forEach>
						</select>
					</td>
					<td class="right">车系：</td>
					<td>
						<input type="text" class="middle_txt" id="MODEL_CODE" name="MODEL_CODE" maxlength="30" value=""/>
					</td>
<!-- 					<td class="right">配件品牌：</td> -->
<!-- 					<td ><script type="text/javascript"> -->
<%-- 							genSelBox("PRODUCE_FAC",<%=Constant.YIELDLY%>,"",true,"",""); --%>
<!-- 						</script> -->
<!-- 					</td> -->
<!-- 					<td class="right">是否常备：</td> -->
<!-- 					<td > -->
<!-- 						<script type="text/javascript"> -->
<%-- 							genSelBox("IS_PERMANENT",<%=Constant.IF_TYPE%>,"<%=Constant.IF_TYPE_NO%>",true,"",""); --%>
<!-- 						</script> -->
<!-- 						<font color="red">*</font> -->
<!-- 					</td> -->
				</tr>
				<tr>
					<td class="right"align="right">适用车系：</td>
					<td colspan="5" align="left">
						<input type="hidden" class="normal_txt" id="SERIES_ID" name="SERIES_ID" value="" />
						<input type="hidden" class="normal_txt" id="SERIES_NAME" name="SERIES_NAME" value="" />
						<c:if test="${not empty ps.carSeriesList}">
							<c:forEach items="${ps.carSeriesList}" var="seriesVar" varStatus="status" step="1">
								<label class="u-checkbox">
									<input type="checkbox" value="${seriesVar.FIX_NAME}" name="carSeries" id="carSeries_${seriesVar.FIX_VALUE}"
								onclick="joinTypes(this.name, 'SERIES_ID', 'SERIES_NAME');" />
									<span></span>
								</label>
								<label class="u-label">${seriesVar.FIX_NAME}</label>
							</c:forEach>
						</c:if>
					</td>
				</tr>
				<tr>
					<td class="right"align="right">适用车型：</td>
					<td colspan="5" align="left">
						<input type="hidden" class="normal_txt" id="MODEL_ID" name="MODEL_ID" value="" />
						<input type="hidden" class="normal_txt" id="MODEL_NAME" name="MODEL_NAME" value="" />
						<c:if test="${not empty ps.carModelList}">
							<c:forEach items="${ps.carModelList}" var="modelVar" varStatus="brandsta" step="1">
								<label class="u-checkbox">
									<input type="checkbox" value="${modelVar.FIX_NAME}" name="carModel" id="carModel_${modelVar.FIX_VALUE}"
								onclick="joinTypes(this.name, 'MODEL_ID', 'MODEL_NAME');" />
									<span></span>
								</label>
								<label class="u-label" for="">${modelVar.FIX_NAME}</label>
							</c:forEach>
						</c:if>
					</td>
				</tr>
				<tr>
				<tr>
					<td class="right">最小采购数量：</td>
					<td >
						<input  type="text"  class="middle_txt" id="MIN_PURCHASE" name="MIN_PURCHASE" onkeyup="removeNotNum(this)" onchange="removeNotNum(this)" maxlength="10" value="1"/>
					</td>
					<td class="right">最小销售数量：</td>
					<td >
						<input  type="text"  class="middle_txt" id="MIN_SALE" name="MIN_SALE" onkeyup="removeNotNum(this)" onchange="removeNotNum(this)" maxlength="10" value="1"/>
					</td>
					<td class="right">最大销售量：</td>
					<td >
						<input  type="text"  class="middle_txt" id="MAX_SALE_VOLUME" name="MAX_SALE_VOLUME" onkeyup="removeNotNum(this)" onchange="removeNotNum(this)" maxlength="10" value="1"/>
					</td>
				</tr>
				<tr>
					<!-- 张磊7.5修改 -->
					<td class="right">长：</td>
					<td >
						<input type="text" class="middle_txt" id="LENGTH" name="LENGTH"  datatype="1,is_digit,10"  maxlength="10"/>
						&nbsp;mm </td>
					<td class="right">宽：</td>
					<td >
						<input type="text" class="middle_txt" id="WIDTH" name="WIDTH"  datatype="1,is_digit,10"  maxlength="10"/>
						&nbsp;mm </td>
					<td class="right">高：</td>
					<td >
						<input type="text" class="middle_txt" id="HEIGHT" name="HEIGHT"  datatype="1,is_digit,10"  maxlength="10"/>
						&nbsp;mm </td>
				</tr>
				<tr>
					<td class="right">净重：</td>
					<td >
						<input type="text" class="middle_txt" id="WEIGHT" name="WEIGHT" maxlength="10"/>
						&nbsp;g </td>
					<td class="right">体积：</td>
					<td >
						<input type="text" class="middle_txt" id="VOLUME" name="VOLUME" maxlength="10" />
						&nbsp;mm3 </td>
					<td class="right">采购方式：</td>
					<td >
						<script type="text/javascript">
							genSelBox("PRODUCE_FAC",<%=Constant.PURCHASE_WAY%>,"",true,"","");
						</script>
					</td>
				</tr>
				<!-- 张磊 2017-07-05 修改   begin   -->
				<tr>
					<td class="right">单车用量：</td>
					<td >
						<input type="text" class="middle_txt" id="VEHICLE_VOLUME" name="VEHICLE_VOLUME" onchange="validateNum(this)" maxlength="10"/>
					</td>
					<td class="right">装配：</td>
					<td >
						<script type="text/javascript">
							genSelBox("PART_FIT",<%=Constant.ZT_PB_PART_FIT%>,"",true,"","");
						</script>
					</td>
					<td class="right">包装规格：</td>
					<td >
						<input  type="text" class="middle_txt" id="PACK_SPECIFICATION" name="PACK_SPECIFICATION" maxlength="10"/>
					</td>
					<!-- <td class="right">代用件号：</td>
					<td >
						<input  type="text"  class="middle_txt" id="OTHER_PART_ID" name="OTHER_PART_ID" />
					</td> -->
				<tr>
				</tr>
					<td class="right">是否停用：</td>
					<td >
						<script type="text/javascript">
							genSelBox("IS_PART_DISABLE",<%=Constant.IF_TYPE%>,"<%=Constant.IF_TYPE_NO%>",true,"","");
						</script>
						<font color="red">*</font>
					</td>
					<td class="right">是否售完停用：</td>
					<td >	
						<script type="text/javascript">
							genSelBox("IS_SALE_DISABLE",<%=Constant.IF_TYPE%>,"<%=Constant.IF_TYPE_NO%>",true,"","");
						</script>
					</td>
					<td class="right">是否停止装车：</td>
					<td >
						<script type="text/javascript">
							genSelBox("IS_STOP_LOAD",<%=Constant.IF_TYPE%>,"<%=Constant.IF_TYPE_NO%>",true,"","");
						</script>
					</td>
				</tr>
				</tr>
					<td class="right">停用日期：</td>
					<td >
						<input class="middle_txt" readonly="readonly" type="text" id="PART_DISABLE_DATE" name="PART_DISABLE_DATE" group="staReportDate,endReportDate" datatype="1,is_date,10"/>
						<input class="time_ico" type="button" />
					</td>
					<td class="right">售完停用日期：</td>
					<td >	
						<input class="middle_txt" readonly="readonly"  type="text" id="SALE_DISABLE_DATE" name="SALE_DISABLE_DATE" group="staReportDate,endReportDate" datatype="1,is_date,10"/>
						<input class="time_ico" type="button" />
					</td>
					<td class="right">停止装车日期：</td>
					<td >
						<input class="middle_txt" readonly="readonly"  type="text" id="STOP_LOAD_DATE" name="STOP_LOAD_DATE" group="staReportDate,endReportDate" datatype="1,is_date,10"/>
						<input class="time_ico" type="button" />
					</td>
				</tr>
				<tr>
					<td class="right">是否校验防伪：</td>
					<td >
						<script type="text/javascript">
							genSelBox("IS_SECURITY",<%=Constant.IF_TYPE%>,"",true,"","");
						</script>
					</td>
					<td class="right">是否协议包装：</td>
					<td >
						<script type="text/javascript">
							genSelBox("IS_PROTOCOL_PACK",<%=Constant.IF_TYPE%>,"",true,"", "onchange='chageBaseFlag(this)'");
						</script>
						<font color="red">*</font>
					</td>
					<td class="right">是否批次管理：</td>
					<td >
						<script type="text/javascript">
							genSelBox("IS_MAG_BATCH",<%=Constant.IF_TYPE%>,"",true,"","");
						</script>
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td class="right">是否委托发货：</td>
					<td >
						<script type="text/javascript">
							genSelBox("IS_ENTRUSR_PACK",<%=Constant.IF_TYPE%>,"",true,"","");
						</script>
					</td>
					<td class="right">是否3C标识：</td>
					<td >
						<script type="text/javascript">
							genSelBox("CCC_FLAG",<%=Constant.IF_TYPE%>,"<%=Constant.IF_TYPE_YES%>",true,"","");
						</script>
					</td>
				</tr>
				<tr>
					<td class="right"align="right">备注：</td>
					<td colspan="5">
						<textarea class="form-control remark align" cols="120" rows="4" id="REMARK" name="REMARK" maxlength="200"></textarea>
					</td>
				</tr>
			</table>
			<table class="table_edit tb-button-set">
				<tr>
					<td align="center">
						<input type="button" name="saveBtn" id="saveBtn" value="保存" onclick="add()"  class="normal_btn"/>
						<input type="button" name="saveBtn" id="backBtn" value="返 回" onclick="goback()"  class="normal_btn"/>
				</td>
			</tr>
		</table>
  </form>
</div>
</body>
</html>