/**
 * Created with IntelliJ IDEA.
 * User: Yu
 * Date: 13-5-9
 * Time: 下午3:03
 * To change this template use File | Settings | File Templates.
 */
//刷新行号
function refreshMtTable(mtId, strType) {
    if (strType == "SEQ") {
        var oSeq = eval("document.all." + mtId + "_SEQ");
        if (oSeq != null && oSeq.length != null) {
            for (var i = 0; i < oSeq.length; i++) {
                oSeq[i].innerText = (i + 1);
            }
        }
    }
}