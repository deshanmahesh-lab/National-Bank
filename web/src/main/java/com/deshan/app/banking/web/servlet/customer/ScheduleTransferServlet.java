package com.deshan.app.banking.web.servlet.customer;

import com.deshan.app.banking.model.Account;
import com.deshan.app.banking.model.Customer;
import com.deshan.app.banking.model.ScheduledTransaction;
import com.deshan.app.banking.service.AccountService;
import com.deshan.app.banking.service.CustomerService;
import com.deshan.app.banking.service.ScheduledTransactionService;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet(name = "ScheduleTransferServlet", urlPatterns = "/customer/schedule-transfer")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"USER"}))
public class ScheduleTransferServlet extends HttpServlet {

    @EJB
    private CustomerService customerService;

    @EJB
    private AccountService accountService;

    @EJB
    private ScheduledTransactionService scheduledTransactionService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Principal principal = request.getUserPrincipal();
        Customer customer = customerService.getCustomerByEmail(principal.getName());
        List<Account> userAccounts = accountService.getAccountsByCustomerId(customer.getId());

        request.setAttribute("userAccounts", userAccounts);
        request.getRequestDispatcher("/customer/schedule_transfer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long fromAccountId = Long.parseLong(request.getParameter("fromAccountId"));
            String toAccountNumber = request.getParameter("toAccountNumber");
            double amount = Double.parseDouble(request.getParameter("amount"));
            String scheduledDateTime = request.getParameter("scheduledDateTime");

            if (amount <= 0) {
                request.setAttribute("error", "Transfer amount must be positive.");
                doGet(request, response);
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date scheduledDate = sdf.parse(scheduledDateTime);

            if (scheduledDate.before(new Date())) {
                request.setAttribute("error", "Scheduled time cannot be in the past.");
                doGet(request, response);
                return;
            }

            Account sourceAccount = accountService.getAccountById(fromAccountId);

            ScheduledTransaction newScheduledTx = new ScheduledTransaction();
            newScheduledTx.setSourceAccount(sourceAccount);
            newScheduledTx.setDestinationAccountNumber(toAccountNumber);
            newScheduledTx.setAmount(amount);
            newScheduledTx.setScheduledTimestamp(scheduledDate);
            newScheduledTx.setStatus("PENDING");

            scheduledTransactionService.scheduleTransfer(newScheduledTx);

            response.sendRedirect(request.getContextPath() + "/customer/dashboard?schedule=success");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred. Please check your input and try again.");
            doGet(request, response);
        }
    }
}