var __layerIdex;
/**
 * 首页新闻附件上传公用弹出框
 */
function showUploadWin() {
	__layerIdex = layer.open({
	  title: '附件上传',
	  type: 2, 
	  maxmin: false,
	  area: ['500px', '300px'],
	  content: globalContextPath +'/dialog/upload/uploadCommon.jsp'
	}); 
}

function closeUploadWin() {
	if(__layerIdex) {
		layer.close(__layerIdex);
	}
}

/**
 * 回传已上传成功的附件到功能页面
 * html中固定设置id为fileUploadTab的容器
 * @param obj
 */
function addUploadRow(obj) {
	$("#fileUploadTab").append("<span id='sp"+obj.cells[1].childNodes[3].value+"'>" +
				obj.cells[1].childNodes[0].outerHTML + 
				obj.cells[1].childNodes[3].outerHTML + 
				"<i class='icon-remove' style='color:red;margin-left:-6px;' onclick=\"delUploadFile('"+obj.cells[1].childNodes[3].value+"');\"></i></span>&nbsp;&nbsp;");
}

function editUploadRow(fjid, fileName, fileUrl) {
	$("#fileUploadTab").append("<span id='sp"+fjid+"'>" +
			"<a target='_blank' href='"+fileUrl+"' title='"+fileName+"'>"+fileName+"</a>" + 
			"<input type='hidden' name='fjid' value='"+fjid+"'/>" + 
			"<i class='icon-remove' style='color:red;margin-left:-6px;' onclick=\"delUploadFile('"+fjid+"');\"></i></span>&nbsp;&nbsp;");
}

function viewUploadRow(fjid, fileName, fileUrl) {
	$("#fileUploadTab").append("<span id='sp"+fjid+"'>" +
			"<a target='_blank' href='"+fileUrl+"' title='"+fileName+"'>"+fileName+"</a>" + 
			"<input type='hidden' name='fjid' value='"+fjid+"'/>" + 
			"</span>&nbsp;&nbsp;");
}

function delUploadFile(fileId) {
	$("#sp"+fileId).remove();
}