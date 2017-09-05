package com.infoservice.dms.chana.common;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import com.infoservice.de.executor.DEMsgIdGenerator;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;

/**
 * @author zhb
 *
 */
public class GeneratorDEMsgId implements DEMsgIdGenerator {
	private POFactory factory=POFactoryBuilder.getInstance();
	public String generatorMsgId() {
		
		Long id = this.getSeqDBid("DE_MSG_INFO");
		//Long id =22222222223735L;
		System.out.println("==========返回MSGID======" + id);
		return id.toString();
	}

	/**
	 * 描述： 参数tabName 即表名 如:TM_USER 返回值：表对应的SEQ_ID 14位
	 */
	public  long getSeqDBid(String tabName) {
		Long dbId = 0L;
		List<Object> inParameter = new ArrayList<Object>();// 输入参数
		List outParameter = new ArrayList();// 输出参数
		
		inParameter.add(tabName);
		outParameter.add(Types.BIGINT);
		outParameter = factory.callProcedure("F_GETSEQDBID", inParameter, outParameter);
		dbId = (Long) outParameter.get(0);
	/*	try{
			POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
		List<Object> inParameter = new ArrayList<Object>();// 输入参数
		List outParameter = new ArrayList();// 输出参数
		
		inParameter.add(tabName);
		outParameter.add(Types.BIGINT);
		outParameter = factory.callProcedure("F_GETSEQDBID", inParameter, outParameter);
		dbId = (Long) outParameter.get(0);
		POContext.endTxn(true);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			POContext.cleanTxn();
		}*/
		return dbId.longValue();
	}

}