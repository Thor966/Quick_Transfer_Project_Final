package com.ag.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/ReceiveFileServlet")
public class ReceiveFileServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Directory where files are stored
        String uploadPath = getServletContext().getRealPath("") + File.separator + "shared_files";
        File folder = new File(uploadPath);
        File[] files = folder.listFiles();

        if (files != null && files.length > 0) {
            File file = files[0]; // There should only be one file in the directory

            // Dynamically determine the MIME type
            String mimeType = getServletContext().getMimeType(file.getName());
            if (mimeType == null) {
                mimeType = "application/octet-stream"; // Default binary type
            }

            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", "attachment;filename=\"" + file.getName() + "\"");

            // Stream the file content to the client
            try (FileInputStream fis = new FileInputStream(file);
                 OutputStream out = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            response.getWriter().write("No files available for download.");
        }
    }
}
