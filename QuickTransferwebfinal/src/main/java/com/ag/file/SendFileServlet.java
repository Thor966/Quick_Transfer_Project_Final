package com.ag.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@SuppressWarnings("serial")
@WebServlet("/SendFileServlet")
@MultipartConfig
public class SendFileServlet extends HttpServlet {
    
    private static final String UPLOAD_DIR = "shared_files"; // Folder to store uploaded files
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("file");
        String fileName = filePart.getSubmittedFileName();
        
        // Directory to store the uploaded file
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
        File directory = new File(uploadPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Save the new file
        File file = new File(uploadPath + File.separator + fileName);
        System.out.println("Saving file to: " + file.getAbsolutePath());

        try (InputStream input = filePart.getInputStream(); OutputStream output = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            long totalBytesRead = 0;
            long fileSize = filePart.getSize();
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                // Send progress updates to the frontend via response
                int progress = (int) ((totalBytesRead * 100) / fileSize);
                response.setHeader("X-Progress", String.valueOf(progress)); // Send progress in the header
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("File uploaded successfully.");
    }
}
