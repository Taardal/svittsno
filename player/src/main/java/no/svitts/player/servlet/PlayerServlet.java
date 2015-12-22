package no.svitts.player.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PlayerServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getHeader("path");
        System.out.println("path header: " + path);
        String path1 = request.getParameter("p");
        System.out.println("path parameter: " + path1);
        String movie = "\\\\TAARDAL-SERVER\\Misc\\Downloads\\Complete\\forthebirds.mkv";
        String vlc = "C:\\Progra~1\\VideoLAN\\VLC\\vlc.exe";
        String mpc = "C:\\Progra~2\\K-Lite Codec Pack\\MPC-HC64\\mpc-hc64.exe";
        String command = "\"" + mpc + "\" \"\\\\taardal-server\\" + path1 + "\"";
        System.out.println("Executing command: " + command);
        Runtime.getRuntime().exec(command);
    }

}
