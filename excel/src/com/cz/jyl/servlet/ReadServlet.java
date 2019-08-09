package com.cz.jyl.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.util.Streams;

@WebServlet("/read.do")
public class ReadServlet extends HttpServlet {

	//修改   ssss
	File tmpDir = null;// 初始化上传文件的临时存放目录
	File saveDir = null;// 初始化上传文件后的保存目录

	public void init() throws ServletException {
		super.init();
		String tmpPath = "G:\\tmpdir";
		String savePath = "G:\\updir";
		tmpDir = new File(tmpPath);
		saveDir = new File(savePath);
		if (!tmpDir.isDirectory())
			tmpDir.mkdir();
		if (!saveDir.isDirectory())
			saveDir.mkdir();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		excute1(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		excute1(request, response);
	}

	public void excute1(HttpServletRequest request, HttpServletResponse response) {
		try {
			String filename=request.getParameter("file1");
			System.out.println(filename);
			if (ServletFileUpload.isMultipartContent(request)) {
				DiskFileItemFactory dff = new DiskFileItemFactory();// 创建该对象
				dff.setRepository(tmpDir);// 指定上传文件的临时目录
				dff.setSizeThreshold(1024000);// 指定在内存中缓存数据大小,单位为byte
				ServletFileUpload sfu = new ServletFileUpload(dff);// 创建该对象
				sfu.setFileSizeMax(5000000);// 指定单个上传文件的最大尺寸
				sfu.setSizeMax(10000000);// 指定一次上传多个文件的总尺寸
				FileItemIterator fii = sfu.getItemIterator(request);
				while (fii.hasNext()) {

					FileItemStream fis = fii.next();// 从集合中获得一个文件流
					if (!fis.isFormField() && fis.getName().length() > 0) {
						System.out.println("fff");
						String fileName = fis.getName().substring(fis.getName().lastIndexOf("\\") + 1);// 获得上传文件的文件名
						System.out.println(fileName);
						BufferedInputStream in = new BufferedInputStream(fis.openStream());// 获得文件输入流
						BufferedOutputStream out = new BufferedOutputStream(
								new FileOutputStream(new File(saveDir + "\\" + fileName)));// 获得文件输出流
						Streams.copy(in, out, true);// 开始把文件写到你指定的上传文件夹
					}
				}
				response.getWriter().println("File upload successfully!!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
