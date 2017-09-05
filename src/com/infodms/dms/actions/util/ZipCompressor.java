package com.infodms.dms.actions.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCompressor {
	
	/**
	 * 压缩文件
	 * @param zipfile
	 * @param files
	 * @throws Exception
	 */
	public static void compress(File zipfile, File files) throws Exception
	{
		if(files.exists()) {
			CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(zipfile), new CRC32());
			ZipOutputStream zos = new ZipOutputStream(cos);
			compress(zos, files);
			zos.close();
		}
	}
	
	/**
	 * 压缩文件
	 * @param zos
	 * @param files
	 * @throws Exception
	 */
	public static void compress(ZipOutputStream zos, File files) throws Exception
	{
		
		if (files.isDirectory())
		{
			// 如果待压缩的是目录，递归调用压缩方法
			File[] fileList = files.listFiles();
			for (File liftFile : fileList)
			{
				compress(zos, liftFile);
			}
		}
		else
		{
			FileInputStream fis = new FileInputStream(files);
			System.out.println("压缩：" + files.getPath());
			compress(zos, fis, files.getPath().substring(files.getPath().indexOf("\\")+1));
		}
	}
	
	/**
	 * 压缩文件
	 * @param zos 压缩流
	 * @param fis 输入流
	 * @param fileName 放置在压缩包的路径:tmp/2012/*
	 * @throws Exception
	 */
	public static void compress(ZipOutputStream zos, FileInputStream fis, String fileName) throws Exception
	{
		ZipEntry entry = new ZipEntry(fileName);
		zos.putNextEntry(entry);
		
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		int count;
		byte data[] = new byte[1024];

		while ((count = bis.read(data, 0, 1024)) != -1)
		{
			zos.write(data, 0, count);
		}
	}
	
	/**
	 * 压缩文件
	 * @param zos 压缩流
	 * @param fis 输入流
	 * @param fileName 放置在压缩包的路径:tmp/2012/*
	 * @throws Exception
	 */
	public static void compress(ZipOutputStream zos, BufferedInputStream bis, String fileName) throws Exception
	{
		ZipEntry entry = new ZipEntry(fileName);
		zos.putNextEntry(entry);
		
		int count;
		byte data[] = new byte[1024];
		
		while ((count = bis.read(data, 0, 1024)) != -1)
		{
			zos.write(data,0,count);
		}
	}
	
	public static void main(String[] args)
	{
		ZipCompressor zipCompressor = new ZipCompressor();
		File zipFile = new File("D:\\1101.ZIP");
		File files = new File("D:\\测试.Dat");
		try
		{
			zipCompressor.compress(zipFile, files);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
