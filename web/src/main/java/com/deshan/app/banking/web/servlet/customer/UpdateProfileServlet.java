package com.deshan.app.banking.web.servlet.customer;

import com.deshan.app.banking.model.Customer;
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
import java.security.Principal;

@WebServlet(name = "UpdateProfileServlet", urlPatterns = "/customer/update-profile")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"USER"}))
public class UpdateProfileServlet extends HttpServlet {

    @EJB
    private CustomerService customerService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Principal principal = request.getUserPrincipal();
        Customer customer = customerService.getCustomerByEmail(principal.getName());
        request.setAttribute("customer", customer);
        request.getRequestDispatcher("/customer/update_profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Principal principal = request.getUserPrincipal();
            Customer customer = customerService.getCustomerByEmail(principal.getName());

            String fullName = request.getParameter("fullName");
            String contactNumber = request.getParameter("contactNumber");

            customer.setFullName(fullName);
            customer.setContactNumber(contactNumber);

            customerService.updateCustomer(customer);

            response.sendRedirect(request.getContextPath() + "/customer/dashboard?update=success");
        } catch (Exception e) {
            request.setAttribute("error", "An unexpected error occurred while updating your profile.");
            doGet(request, response);
        }
    }
}