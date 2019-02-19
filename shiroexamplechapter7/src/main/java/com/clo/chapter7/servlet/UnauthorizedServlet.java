package com.clo.chapter7.servlet;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "unauthorizedServlet", urlPatterns = "/unauthorized")
public class UnauthorizedServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("subject", SecurityUtils.getSubject());
        req.getRequestDispatcher("/WEB-INF/jsp/unauthorized.jsp").forward(req, resp);
    }
}
