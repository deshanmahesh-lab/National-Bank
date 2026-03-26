package com.deshan.app.banking.web.servlet.customer;

import com.deshan.app.banking.exception.InsufficientFundsException;
import com.deshan.app.banking.service.TransactionService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "VerifyOtpServlet", urlPatterns = "/customer/verify-otp")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"USER"}))
public class VerifyOtpServlet extends HttpServlet {

    @EJB
    private TransactionService transactionService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String userOtp = request.getParameter("otp");

        if (session == null || userOtp == null) {
            response.sendRedirect(request.getContextPath() + "/customer/transfer?error=sessionExpired");
            return;
        }

        String sessionOtp = (String) session.getAttribute("otp");

        if (sessionOtp != null && sessionOtp.equals(userOtp)) {
            try {
                Long fromAccountId = (Long) session.getAttribute("fromAccountId");
                String toAccountNumber = (String) session.getAttribute("toAccountNumber");
                double amount = (double) session.getAttribute("amount");

                transactionService.transferFunds(fromAccountId, toAccountNumber, amount);

                session.removeAttribute("otp");
                session.removeAttribute("fromAccountId");
                session.removeAttribute("toAccountNumber");
                session.removeAttribute("amount");

                response.sendRedirect(request.getContextPath() + "/customer/dashboard?transfer=success");

            } catch (InsufficientFundsException e) {
                request.setAttribute("error", e.getMessage());
                request.getRequestDispatcher("/customer/transfer.jsp").forward(request, response);
            } catch (Exception e) {
                request.setAttribute("error", "The transaction could not be completed.");
                request.getRequestDispatcher("/customer/transfer.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Invalid OTP. Please try again.");
            request.getRequestDispatcher("/customer/verify_otp.jsp").forward(request, response);
        }
    }
}