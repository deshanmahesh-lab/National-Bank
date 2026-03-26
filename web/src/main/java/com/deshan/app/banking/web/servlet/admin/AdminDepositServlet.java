package com.deshan.app.banking.web.servlet.admin;

import com.deshan.app.banking.service.TransactionService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "AdminDepositServlet", urlPatterns = "/admin/deposit")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"ADMIN"}))
public class AdminDepositServlet extends HttpServlet {

    @EJB
    private TransactionService transactionService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountIdStr = request.getParameter("accountId");
        String amountStr = request.getParameter("amount");
        String customerId = request.getParameter("customerId");

        try {
            Long accountId = Long.parseLong(accountIdStr);
            double amount = Double.parseDouble(amountStr);

            if (amount <= 0) {
                response.sendRedirect(request.getContextPath() + "/admin/manage-accounts?customerId=" + customerId + "&error=invalidAmount");
                return;
            }

            transactionService.adminDeposit(accountId, amount);
            response.sendRedirect(request.getContextPath() + "/admin/manage-accounts?customerId=" + customerId + "&deposit=success");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/manage-accounts?customerId=" + customerId + "&error=invalidInput");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/manage-accounts?customerId=" + customerId + "&error=depositFailed");
        }
    }
}