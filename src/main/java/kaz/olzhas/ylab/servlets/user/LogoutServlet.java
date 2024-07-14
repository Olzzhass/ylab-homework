package kaz.olzhas.ylab.servlets.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaz.olzhas.ylab.dto.ResponseMessage;
import kaz.olzhas.ylab.state.Authentication;

import java.io.IOException;

@WebServlet("/user/logout")
public class LogoutServlet extends HttpServlet {

    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        Authentication authentication = (Authentication) req.getSession().getAttribute("authentication");

        if (authentication == null || !authentication.isAuth()) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            objectMapper.writeValue(resp.getWriter(), new ResponseMessage("User not authenticated or not admin"));
            return;
        }

        authentication.setAuth(false);
        authentication.setUsername(null);

        req.getSession().invalidate();

        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), new ResponseMessage("Logged out successfully"));
    }
}
