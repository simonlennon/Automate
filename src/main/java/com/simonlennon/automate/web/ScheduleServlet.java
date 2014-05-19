package com.simonlennon.automate.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by simon.lennon on 30/01/14.
 */
public class ScheduleServlet extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {

        String device = request.getParameter("device");

        if("pond".equals(device)){

            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            File f = new File("pondschedule.xml");
            BufferedReader reader = new BufferedReader(new FileReader(f));
            BufferedWriter writer = new BufferedWriter(response.getWriter());

            String line = null;
            while((line=reader.readLine())!=null){
                writer.write(line);
            }

            writer.flush();
            writer.close();
            reader.close();

        } else {
            throw new ServletException("Could not identify device to get file for:"+device);
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, java.io.IOException {

        String data = request.getParameter("data");
        String device = request.getParameter("device");

        if (data == null || data.trim().length() == 0) {
            throw new ServletException("Could not find data to write in the request");
        }



        if("pond".equals(device)){
            File f = new File("pondschedule.xml");
            FileWriter writer = new FileWriter(f);
            writer.write(data);
            writer.close();

            response.sendRedirect("save_complete.jsp");

        } else {
            throw new ServletException("Could not identify device to save file for:"+device);
        }
    }


}
