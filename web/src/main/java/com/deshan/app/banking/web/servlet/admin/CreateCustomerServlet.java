package com.deshan.app.banking.web.servlet.admin;

import com.deshan.app.banking.mail.ActivationMail;
import com.deshan.app.banking.model.AccountStatus;
import com.deshan.app.banking.model.Customer;
import com.deshan.app.banking.model.UserRole;
import com.deshan.app.banking.provider.MailServiceProvider;
import com.deshan.app.banking.service.CustomerService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "CreateCustomerServlet", urlPatterns = "/admin/create-customer")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"ADMIN"}))
public class CreateCustomerServlet extends HttpServlet {

    @EJB
    private CustomerService customerService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String contactNumber = request.getParameter("contactNumber");

        try {
            if (customerService.getCustomerByEmail(email) != null) {
                request.setAttribute("error", "A customer with this email address already exists.");
                request.getRequestDispatcher("/admin/create_customer.jsp").forward(request, response);
                return;
            }

            String verificationCode = UUID.randomUUID().toString();
            Customer newCustomer = new Customer();
            newCustomer.setFullName(fullName);
            newCustomer.setEmail(email);
            newCustomer.setContactNumber(contactNumber);
            newCustomer.setVerificationCode(verificationCode);
            newCustomer.setUserRole(UserRole.USER);
            newCustomer.setStatus(AccountStatus.INACTIVE);

            customerService.addCustomer(newCustomer);

            ActivationMail mail = new ActivationMail(email, verificationCode);
            MailServiceProvider.getInstance().sendMail(mail);

            request.setAttribute("success", "Customer account for " + fullName + " created successfully! An activation email has been sent.");
            request.getRequestDispatcher("/admin/create_customer.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "An unexpected error occurred while creating the account.");
            request.getRequestDispatcher("/admin/create_customer.jsp").forward(request, response);
        }
    }
}