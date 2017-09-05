<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>demo</title>
<script language="javascript" type="text/javascript">
    function goback() {
        parent.window._hide();
    }
    function genSelBoxExpMy(id, type, selectedKey, setAll, _class_, _script_, nullFlag, expStr) {
        var str = "";
        var arr;
        if (expStr.indexOf(",")> 0)
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
                str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '" + codeData[i].codeDesc + "'>" + codeData[i].codeDesc + "</option>";
            }
        }
        str += "</select>";

        document.write(str);
    }


</script>
</head>
<body>
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：基础信息管理 &gt;配件基础信息维护&gt; 配件主数据维护&gt; 查看</div>
    <form name="fm" id="fm">
		<table class="table_query">
			<tr>
				<td class="right" width="89px;">件号：</td>
				<td>
					<input class="middle_txt" type="text" readonly disabled value="<c:out value="${ps.PART_CODE}"/>">
				</td>
				<td class="right">配件名称：</td>
				<td>
					<input class="middle_txt" type="text" readonly disabled value="<c:out value="${ps.PART_CNAME}"/>">
				</td>
				<td class="right">英文名称:</td>
				<td>
					<input class="middle_txt" type="text" readonly disabled value="<c:out value="${ps.PART_ENAME}"/>"/>
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
						genSelBoxExp("PRODUCE_STATE", <%=Constant.PART_PRODUCE_STATE%>, "${ps.PRODUCE_STATE}", true, "short_sel u-select", "disabled", "false", '');
					</script>
				</td>
				<td class="right">备件类别：</td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("PART_CATEGORY", <%=Constant.ZT_PB_PART_CATEGORY%>, "${ps.PART_CATEGORY}",true,"short_sel u-select","disabled","false",'');
					</script>
				</td>
			</tr>
			<tr>
<!-- 				<td class="right">是否常备：</td> -->
<!-- 				<td> -->
<!-- 					<script type="text/javascript"> -->
<%-- 						genSelBoxExp("IS_PERMANENT", <%=Constant.PART_BASE_FLAG%>, "${ps.IS_PERMANENT}", true, "short_sel u-select", "disabled", "false", ''); --%>
<!-- 					</script> -->
<!-- 					<font color="red">*</font></td> -->
<!-- 				</td> -->
				<td class="right">是否可替代：</td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("IS_REPLACED",<%=Constant.PART_BASE_FLAG%>, "${ps.IS_REPLACED}",true,"short_sel u-select","disabled","false",'');
					</script> 
				</td>
<!-- 				<td class="right">所属大类：</td> -->
<!-- 				<td> -->
<!-- 					<script type="text/javascript"> -->
<%-- 						genSelBoxExp("POSITION", <%=Constant.PART_BASE_POSITION%>, "${ps.POSITION}", true, "short_sel u-select", "disabled", "false", ''); --%>
<!-- 					</script> -->
<!-- 				</td> -->
<!-- 				<td class="right">材料属性：</td> -->
<!-- 				<td> -->
<!-- 					<script type="text/javascript"> -->
<%-- 						genSelBoxExp("PART_MATERIAL", <%=Constant.PART_BASE_MATERIAL%>, "${ps.PART_MATERIAL}", true, "short_sel u-select", "disabled", "false", ''); --%>
<!-- 					</script> -->
<!-- 				</td> -->
<!-- 			</tr> -->
<!-- 			<tr> -->
				<td class="right">单位：</td>
				<td>
					<input type="text"  class="middle_txt" readonly disabled value="${ps.UNIT}"/>
				</td>
<!-- 				<td class="right">配件品牌：</td> -->
<!-- 				<td> -->
<!-- 					<script type="text/javascript"> -->
<%-- 						genSelBoxExp("PRODUCE_FAC", <%=Constant.YIELDLY%>, "${ps.PRODUCE_FAC}", true, "short_sel u-select", "disabled", "false", ''); --%>
<!-- 					</script> -->
<!-- 				</td> -->

				<td class="right">车系：</td>
				<td>
					<input type="text" class="middle_txt" id="MODEL_CODE" name="MODEL_CODE" maxlength="30" readonly disabled value="${ps.MODEL_CODE}"/>
				</td>
			</tr>
			<tr>
				<td class="right">适用车系：</td>
				<td colspan="5" class="left">
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
								disabled
								/>
								<span></span>
							</label>
							<label class="u-label">${var.FIX_NAME}</label>
						</c:forEach>
					</c:if>
				</td>
			</tr>
			<tr>
				<td class="right">适用车型：</td>
				<td colspan="5" class="left">
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
								disabled
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
					<input type="text"  class="middle_txt" readonly disabled value="${ps.MIN_SALE}"/>
				</td>
				<td class="right">最小销售数量：</td>
				<td>
					<input type="text"  class="middle_txt" readonly disabled value="${ps.MIN_PURCHASE}"/>
				</td>
				<td class="right">最大销售量：</td>
				<td>
					<input type="text"  class="middle_txt" readonly disabled value="${ps.MAX_SALE_VOLUME}"/>
				</td>
			</tr>
			<tr>
				<td class="right">长：</td>
				<td>
					<input type="text" class="middle_txt" readonly disabled value="<c:out value="${ps.LENGTH}"/>"/>&nbsp;mm
				</td>
				<td class="right">宽：</td>
				<td>
					<input type="text" class="middle_txt" readonly disabled value="<c:out value="${ps.WIDTH}"/>"/>&nbsp;mm
				</td>
				<td class="right">高：</td>
				<td>
					<input type="text" class="middle_txt" readonly disabled value="<c:out value="${ps.HEIGHT}"/>"/>&nbsp;mm
				</td>
			</tr>
			<tr>
				<td class="right">净重：</td>
				<td>
					<input type="text" class="middle_txt" readonly disabled value="<c:out value="${ps.WEIGHT}"/>"/>&nbsp;g
				</td>
				<td class="right">体积：</td>
				<td>
					<input type="text" class="middle_txt" readonly disabled value="<c:out value='${ps.VOLUME}'/>"/>&nbsp;mm3
				</td>
				<td class="right">采购方式：</td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("PRODUCE_FAC",<%=Constant.PURCHASE_WAY%>,"${ps.PRODUCE_FAC}",true,"short_sel u-select","disabled","false",'');
					</script>
				</td>
			</tr>
			<tr>
				<td class="right">单车用量：</td>
				<td>
					<input type="text" class="middle_txt" readonly disabled value="${ps.VEHICLE_VOLUME}"/>
				</td>
				<td class="right">装配：</td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("PART_FIT",<%=Constant.ZT_PB_PART_FIT%>,"${ps.PART_FIT}",true,"short_sel u-select","disabled","false",'');
					</script>
				</td>
				<td class="right">包装规格：</td>
				<td>
					<input type="text" class="middle_txt" readonly disabled value="${ps.PACK_SPECIFICATION}"/>
				</td>
			<tr>
			</tr>
				<td class="right">是否停用：</td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("IS_PART_DISABLE",<%=Constant.PART_BASE_FLAG%>,"${ps.IS_PART_DISABLE}",true,"short_sel u-select","disabled","false",'');
					</script>
				</td>
				<td class="right">是否售完停用：</td>
				<td>	
					<script type="text/javascript">
						genSelBoxExp("IS_SALE_DISABLE",<%=Constant.PART_BASE_FLAG%>,"${ps.IS_SALE_DISABLE}",true,"short_sel u-select","disabled","false",'');
					</script>
				</td>
				<td class="right">是否停止装车：</td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("IS_STOP_LOAD",<%=Constant.PART_BASE_FLAG%>,"${ps.IS_STOP_LOAD}",true,"short_sel u-select","disabled","false",'');
					</script>
				</td>
			</tr>
			</tr>
				<td class="right">停用日期：</td>
				<td>
					<input class="middle_txt" readonly="readonly"  type="text" id="PART_DISABLE_DATE" name="PART_DISABLE_DATE" 
					group="staReportDate,endReportDate" datatype="1,is_date,10" value="${ps.PART_DISABLE_DATE_F }" readonly disabled/>
				</td>
				<td class="right">售完停用日期：</td>
				<td>	
					<input class="middle_txt" readonly="readonly"  type="text" id="SALE_DISABLE_DATE" name="SALE_DISABLE_DATE" 
					group="staReportDate,endReportDate" datatype="1,is_date,10" value="${ps.SALE_DISABLE_DATE_F }" readonly disabled/>
				</td>
				<td class="right">停止装车日期：</td>
				<td>
					<input class="middle_txt" readonly="readonly"  type="text" id="STOP_LOAD_DATE" name="STOP_LOAD_DATE" 
					group="staReportDate,endReportDate" datatype="1,is_date,10" value="${ps.STOP_LOAD_DATE_F }" readonly disabled/>
				</td>
			</tr>
			<tr>
				<td class="right">是否校验防伪：</td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("IS_SECURITY",<%=Constant.PART_BASE_FLAG%>,"${ps.IS_SECURITY}",true,"short_sel u-select","disabled","false",'');
					</script>
				</td>
				<td class="right">是否协议包装：</td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("IS_PROTOCOL_PACK",<%=Constant.PART_BASE_FLAG%>,"${ps.IS_PROTOCOL_PACK}",true,"short_sel u-select","disabled","false",'');
					</script>
					<font color="red">*</font>
				</td>
				<td class="right">是否批次包装：</td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("IS_MAG_BATCH",<%=Constant.PART_BASE_FLAG%>,"${ps.IS_MAG_BATCH}",true,"short_sel u-select","disabled","false",'');
					</script>
					<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td class="right">是否委托发货：</td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("IS_ENTRUSR_PACK",<%=Constant.PART_BASE_FLAG%>,"${ps.IS_ENTRUSR_PACK}",true,"short_sel u-select","disabled","false",'');
					</script>
				</td>
				<td class="right">3C标识：</td>
				<td colspan="3">
					<script type="text/javascript">
						genSelBoxExp("CCC_FLAG",<%=Constant.PART_BASE_FLAG%>,"${ps.CCC_FLAG}",true,"short_sel u-select","disabled","false",'');
					</script>
				</td>
			</tr>
			<tr class="table_query">
				<td class="right">维护日期：</td>
				<td><input class="middle_txt" value="${ps.DEFEND_DATE}" readonly disabled/></td>
				<td class="right">最近修改日期：</td>
				<td><input class="middle_txt" value="${ps.UPDATE_DATE_F}" readonly disabled/></td>
				<td class="right"></td>
				<td></td>
			</tr>
			<tr>
				<td class="right">备注：</td>
				<td colspan="5">
					<textarea id="REMARK" class="form-control remark align" name="REMARK" cols="120" rows="4" maxlength="200"
							readonly>${ps.REMARK}</textarea>
				</td>
			</tr>

		</table>	
	<table width="100%" class="sel-buttons">
	    <tr>
	        <td align="center"><input type="button" name="saveBtn" id="saveBtn" value="关 闭" onclick="javascript:goback();" class="normal_btn u-cancel"/></td>
	    </tr>
	</table>
</form>
</body>
</html>
