package no.svitts.player.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PlayerServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("p");
        String vlc = "C:\\Progra~1\\VideoLAN\\VLC\\vlc.exe";
        String mpc = "C:\\Progra~2\\K-Lite Codec Pack\\MPC-HC64\\mpc-hc64.exe";
        String command = "\"" + mpc + "\" \"\\\\" + path + "\"";
        System.out.println("Executing command: " + command);
        Runtime.getRuntime().exec(command);
    }

}
