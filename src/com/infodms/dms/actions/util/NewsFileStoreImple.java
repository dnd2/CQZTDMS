package com.infodms.dms.actions.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.infoservice.filestore.DataNode;
import com.infoservice.filestore.FileStoreException;
import com.infoservice.filestore.NameNode;
import com.infoservice.filestore.config.NameNodeInfo;
import com.infoservice.filestore.impl.FileStoreImpl;
import com.infoservice.filestore.impl.NameNodeImpl;
import com.infoservice.filestore.utils.IdUtil2;
import com.infoservice.filestore.config.ConfigParser;
import com.infoservice.filestore.config.XmlConfigParser;

public class NewsFileStoreImple extends FileStoreImpl {

	private static Random rand = new Random(System.currentTimeMillis());
	private static Logger logger = LogManager.getLogger(NewsFileStoreImple.class);
	private HashMap<String, NameNode> idMap = new HashMap<String, NameNode>();
	private HashMap<String, NameNode> hashMap = new HashMap<String, NameNode>();
	private LinkedList<String> validNodes = new LinkedList<String>();
	private static HashMap<String, NameNodeInfo> nameNodeMap = null;

	private static NewsFileStoreImple nfs;
	
	public static NewsFileStoreImple getInstance() throws FileStoreException {
	    if (nfs == null) {
	      synchronized (NewsFileStoreImple.class) {
	        if (nfs == null) {
	          ConfigParser xmlconf = new XmlConfigParser();
	          nameNodeMap = xmlconf.getParseResult();
	          nfs = new NewsFileStoreImple(nameNodeMap);
	        }
	      }
	    }
	    return nfs;
	 }
	
	
	public NewsFileStoreImple(HashMap<String, NameNodeInfo> nameNodeMap) throws FileStoreException {
		super(nameNodeMap);
	    for (NameNodeInfo obj : nameNodeMap.values()) {
	      NameNode nnode = new NameNodeImpl(obj);
	      if (this.hashMap.containsKey(nnode.getHash()))
	        throw new FileStoreException("duplicated hash !");
	      this.idMap.put(obj.getId(), nnode);
	      this.hashMap.put(nnode.getHash(), nnode);
	      if (nnode.canWrite())
	        this.validNodes.addLast(nnode.getHash());
	    }
	}
	
	 static
	 {
	    System.setProperty("jcifs.smb.client.dfs.disabled", "true");
	 }

	private String[] retrieveFileId(String fileName) throws FileStoreException {
		if (fileName.length() < IdUtil2.ID_LEN) {
			throw new FileStoreException("文件长度小于32! " + fileName);
		}
		return IdUtil2.retrieve(fileName.substring(0, IdUtil2.ID_LEN));
	}

	private NameNode getNameNodeByHash(String hash) {
		return (NameNode) this.hashMap.get(hash);
	}

	/**
	 * 重写getDomainURL方法以便在新闻上传时独立一个news文件夹
	 * */
	public String getDomainURL(String fileName, String subFile) throws FileStoreException {
		String[] nodes = retrieveFileId(fileName);

		NameNode nnode = getNameNodeByHash(nodes[0]);
		if (nnode == null) {
			logger.debug("name node no exist! hash=" + nodes[0] + "  "	+ fileName);
			throw new FileStoreException("invalide name node! " + fileName);
		}

		DataNode dnode = nnode.getDataNodeByHash(nodes[1]);
		if (dnode == null) {
			logger.debug("data node not exist! hash=" + nodes[1] + "  "	+ fileName);
			throw new FileStoreException("invalide data node! " + fileName);
		}

		StringBuilder sbd = new StringBuilder();
		sbd.append(nnode.getDomain());
		sbd.append("/" + dnode.getId());
		//sbd.append("/" + nodes[2]); //新闻去掉日期文件夹目录
		sbd.append("/" + subFile);
		sbd.append("/" + fileName);
		return sbd.toString();
	}
	
	  private NameNode getNameNodeById(String id) {
	    return (NameNode)this.idMap.get(id);
	  }

	  private NameNode getRandomNameNode() {
	    String key = (String)this.validNodes.get(rand.nextInt(this.validNodes.size()));
	    return (NameNode)this.hashMap.get(key);
	  }

	  /**
	   * 重写FileStore的write方法，以便新闻文件上传时调用
	   * */
	  public String write(String nameNodeId, String dataNodeId, String fileName, InputStream in, String subFile)
	    throws FileStoreException
	  {
	    NameNode nnode = null;
	    DataNode dnode = null;

	    if ((fileName == null) || (fileName.length() == 0)) {
	      throw new FileStoreException("文件名不能为空");
	    }

	    if (nameNodeId != null)
	      nnode = getNameNodeById(nameNodeId);
	    else {
	      nnode = getRandomNameNode();
	    }
	    if (nnode == null) {
	      throw new FileStoreException("not found name node :" + nameNodeId);
	    }
	    if (dataNodeId != null)
	      dnode = nnode.getDataNodeById(dataNodeId);
	    else {
	      dnode = nnode.getRandomDataNode();
	    }
	    if (dnode == null) {
	      throw new FileStoreException("找不到data node :" + nameNodeId + "->" + dataNodeId + " 或者该datanode不可写入");
	    }
	    String fname = IdUtil2.genFileId(nnode.getHash(), dnode.getHash(), fileName);
	    //String[] tmp = IdUtil2.retrieve(fname);

	    dnode.write("/" + subFile, fname, in);
	    return fname;
	  }

	  public byte[] read(String fileName) throws FileStoreException {
	    String[] nodes = retrieveFileId(fileName);
	    NameNode nnode = getNameNodeByHash(nodes[0]);
	    if (nnode == null) {
	      throw new FileStoreException(fileName + "文件找不到或该DataNode不可读取");
	    }

	    DataNode dnode = nnode.getDataNodeByHash(nodes[1]);
	    if (dnode == null) {
	      logger.debug("name node no exist! hash=" + nodes[0] + "  " + fileName);
	      throw new FileStoreException(fileName + "文件找不到或该DataNode不可读取");
	    }

	    return dnode.read("/" + nodes[2], fileName);
	  }

	  public InputStream getInputStream(String fileName) throws FileStoreException {
	    String[] nodes = retrieveFileId(fileName);

	    NameNode nnode = getNameNodeByHash(nodes[0]);
	    if (nnode == null) {
	      logger.debug("name node no exist! hash=" + nodes[0] + "  " + fileName);
	      throw new FileStoreException(fileName + "文件找不到或该DataNode不可读取");
	    }

	    DataNode dnode = nnode.getDataNodeByHash(nodes[1]);
	    if (dnode == null) {
	      throw new FileStoreException(fileName + "文件找不到或该DataNode不可读取");
	    }

	    return dnode.getInputStream("/" + nodes[2], fileName);
	  }

	  public boolean delete(String fileName) throws FileStoreException {
	    String[] nodes = retrieveFileId(fileName);

	    NameNode nnode = getNameNodeByHash(nodes[0]);
	    if (nnode == null) {
	      logger.debug("name node no exist! hash=" + nodes[0] + "  " + fileName);
	      throw new FileStoreException(fileName + "文件找不到或该DataNode不可删除");
	    }

	    DataNode dnode = nnode.getDataNodeByDeleteHash(nodes[1]);
	    if (dnode == null) {
	      throw new FileStoreException(fileName + "文件找不到或该DataNode不可删除");
	    }

	    return dnode.delete("/" + nodes[2], fileName);
	  }

	  public String getDomainURL(String fileName) throws FileStoreException {
	    String[] nodes = retrieveFileId(fileName);

	    NameNode nnode = getNameNodeByHash(nodes[0]);
	    if (nnode == null) {
	      logger.debug("name node no exist! hash=" + nodes[0] + "  " + fileName);
	      throw new FileStoreException("invalide name node! " + fileName);
	    }

	    DataNode dnode = nnode.getDataNodeByHash(nodes[1]);
	    if (dnode == null) {
	      logger.debug("data node not exist! hash=" + nodes[1] + "  " + fileName);
	      throw new FileStoreException("invalide data node! " + fileName);
	    }

	    StringBuilder sbd = new StringBuilder();
	    sbd.append(nnode.getDomain());
	    sbd.append("/" + dnode.getId());
	    sbd.append("/" + nodes[2]);
	    sbd.append("/" + fileName);
	    return sbd.toString();
	  }

	  public String copy(String fname, String nameNodeId, String dataNodeId) throws FileStoreException {
	    InputStream in = null;
	    try {
	      in = getInputStream(fname);
	      return write(nameNodeId, dataNodeId, fname, in);
	    } catch (FileStoreException e) {
	      throw e;
	    } catch (Exception e) {
	      throw new FileStoreException("copy file error! " + fname + " to " + nameNodeId + "->" + dataNodeId, e);
	    } finally {
	      try {
	        if (in != null)
	          in.close();
	      } catch (Exception e) {
	        throw new FileStoreException("copy file error! " + fname + " to " + nameNodeId + "->" + dataNodeId, e);
	      }
	    }
	  }

}
