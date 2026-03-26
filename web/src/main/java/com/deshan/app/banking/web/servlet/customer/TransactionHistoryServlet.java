package com.deshan.app.banking.web.servlet.customer;

import com.deshan.app.banking.model.Account;
import com.deshan.app.banking.model.Customer;
import com.deshan.app.banking.model.Transaction;
import com.deshan.app.banking.service.AccountService;
import com.deshan.app.banking.service.CustomerService;
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
import java.security.Principal;
import java.util.List;

@WebServlet(name = "TransactionHistoryServlet", urlPatterns = "/customer/history")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"USER"}))
public class TransactionHistoryServlet extends HttpServlet {

    @EJB
    private AccountService accountService;

    @EJB
    private TransactionService transactionService;

    @EJB
    private CustomerService customerService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountIdStr = request.getParameter("accountId");
        Principal principal = request.getUserPrincipal();

        if (accountIdStr == null || accountIdStr.isEmpty() || principal == null) {
            response.sendRedirect(request.getContextPath() + "/customer/dashboard?error=missingData");
            return;
        }

        try {
            Long accountId = Long.parseLong(accountIdStr);
            Customer customer = customerService.getCustomerByEmail(principal.getName());

            boolean isOwner = accountService.getAccountsByCustomerId(customer.getId())
                    .stream()
                    .anyMatch(account -> account.getId().equals(accountId));

            if (!isOwner) {
                response.sendRedirect(request.getContextPath() + "/customer/dashboard?error=unauthorizedAccess");
                return;
            }

            Account account = accountService.getAccountById(accountId);
            List<Transaction> transactions = transactionService.getTransactionHistory(accountId);

            request.setAttribute("account", account);
            request.setAttribute("transactions", transactions);

            request.getRequestDispatcher("/customer/transactions.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/customer/dashboard?error=invalidAccountId");
        }
    }
}