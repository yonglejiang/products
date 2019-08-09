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

	//�޸�   ssss
	File tmpDir = null;// ��ʼ���ϴ��ļ�����ʱ���Ŀ¼
	File saveDir = null;// ��ʼ���ϴ��ļ���ı���Ŀ¼

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
				DiskFileItemFactory dff = new DiskFileItemFactory();// �����ö���
				dff.setRepository(tmpDir);// ָ���ϴ��ļ�����ʱĿ¼
				dff.setSizeThreshold(1024000);// ָ�����ڴ��л������ݴ�С,��λΪbyte
				ServletFileUpload sfu = new ServletFileUpload(dff);// �����ö���
				sfu.setFileSizeMax(5000000);// ָ�������ϴ��ļ������ߴ�
				sfu.setSizeMax(10000000);// ָ��һ���ϴ�����ļ����ܳߴ�
				FileItemIterator fii = sfu.getItemIterator(request);
				while (fii.hasNext()) {

					FileItemStream fis = fii.next();// �Ӽ����л��һ���ļ���
					if (!fis.isFormField() && fis.getName().length() > 0) {
						System.out.println("fff");
						String fileName = fis.getName().substring(fis.getName().lastIndexOf("\\") + 1);// ����ϴ��ļ����ļ���
						System.out.println(fileName);
						BufferedInputStream in = new BufferedInputStream(fis.openStream());// ����ļ�������
						BufferedOutputStream out = new BufferedOutputStream(
								new FileOutputStream(new File(saveDir + "\\" + fileName)));// ����ļ������
						Streams.copy(in, out, true);// ��ʼ���ļ�д����ָ�����ϴ��ļ���
					}
				}
				response.getWriter().println("File upload successfully!!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
