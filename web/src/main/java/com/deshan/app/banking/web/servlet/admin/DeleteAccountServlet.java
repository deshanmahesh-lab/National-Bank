package com.deshan.app.banking.web.servlet.admin;

import com.deshan.app.banking.service.AccountService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "DeleteAccountServlet", urlPatterns = "/admin/delete-account")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"ADMIN"}))
public class DeleteAccountServlet extends HttpServlet {

    @EJB
    private AccountService accountService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountIdStr = request.getParameter("accountId");
        String customerId = request.getParameter("customerId");

        try {
            Long accountId = Long.parseLong(accountIdStr);
            accountService.closeAccount(accountId);
            response.sendRedirect(request.getContextPath() + "/admin/manage-accounts?customerId=" + customerId + "&close=success");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/manage-accounts?customerId=" + customerId + "&error=closeFailed");
        }
    }
}