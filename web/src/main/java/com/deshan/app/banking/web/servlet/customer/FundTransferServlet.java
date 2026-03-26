package com.deshan.app.banking.web.servlet.customer;

import com.deshan.app.banking.mail.OtpMail;
import com.deshan.app.banking.model.Account;
import com.deshan.app.banking.model.Customer;
import com.deshan.app.banking.provider.MailServiceProvider;
import com.deshan.app.banking.service.AccountService;
import com.deshan.app.banking.service.CustomerService;
import com.deshan.app.banking.util.OtpUtil;
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
import java.security.Principal;
import java.util.List;

@WebServlet(name = "FundTransferServlet", urlPatterns = "/customer/transfer")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"USER"}))
public class FundTransferServlet extends HttpServlet {

    @EJB
    private CustomerService customerService;

    @EJB
    private AccountService accountService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Principal principal = request.getUserPrincipal();
        Customer customer = customerService.getCustomerByEmail(principal.getName());
        List<Account> userAccounts = accountService.getAccountsByCustomerId(customer.getId());
        request.setAttribute("userAccounts", userAccounts);
        request.getRequestDispatcher("/customer/transfer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Principal principal = request.getUserPrincipal();
            Customer customer = customerService.getCustomerByEmail(principal.getName());

            Long fromAccountId = Long.parseLong(request.getParameter("fromAccountId"));
            String toAccountNumber = request.getParameter("toAccountNumber");
            double amount = Double.parseDouble(request.getParameter("amount"));

            if (amount <= 0) {
                request.setAttribute("error", "Transfer amount must be positive.");
                doGet(request, response);
                return;
            }

            Account fromAccount = accountService.getAccountById(fromAccountId);
            if (fromAccount.getBalance() < amount) {
                request.setAttribute("error", "Insufficient funds for this transfer.");
                doGet(request, response);
                return;
            }

            String otp = OtpUtil.generateOtp();
            OtpMail mail = new OtpMail(customer.getEmail(), otp);
            MailServiceProvider.getInstance().sendMail(mail);

            HttpSession session = request.getSession();
            session.setAttribute("otp", otp);
            session.setAttribute("fromAccountId", fromAccountId);
            session.setAttribute("toAccountNumber", toAccountNumber);
            session.setAttribute("amount", amount);

            response.sendRedirect(request.getContextPath() + "/customer/verify_otp.jsp");

        } catch (Exception e) {
            request.setAttribute("error", "An unexpected error occurred. Please check the details and try again.");
            doGet(request, response);
        }
    }
}