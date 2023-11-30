package com.mkenlo.adminweb.web;

import com.mkenlo.adminweb.data.model.Customer;
import com.mkenlo.adminweb.data.model.Order;
import com.mkenlo.adminweb.data.repository.CustomerRepository;
import com.mkenlo.adminweb.data.repository.OrderRepository;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public CustomerController(CustomerRepository customerRepository, OrderRepository orderRepository){
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }


    @GetMapping
    public String getAllUsers(Model model){
        Iterable<Customer> customersIterable = this.customerRepository.findAll();
        List<Customer> customers = new ArrayList<>();
        customersIterable.forEach(customers::add);
        customers.sort(new Comparator<Customer>() {
            @Override
            public int compare(Customer o1, Customer o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        model.addAttribute("customers", customers);
        model.addAttribute("module", "customers");
        return "customers";
    }

    @GetMapping(path="/{id}")
    public String getUser(@PathVariable("id")long customerId, Principal principal, Model model){
        Optional<Customer> customer = this.customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
        model.addAttribute("customer", customer.get());

        List<Order> orders = new ArrayList<>();
        if( principal instanceof UsernamePasswordAuthenticationToken){
            AtomicBoolean auth = new AtomicBoolean(false);
            Collection<GrantedAuthority> authorities = ((UsernamePasswordAuthenticationToken) principal).getAuthorities();
            authorities.forEach(authority ->{
                if(authority.getAuthority().equals("ROLE_ADMIN")){
                    auth.set(true);
                }
            });
            if(auth.get()){
                Iterable<Order> ordersIterable = this.orderRepository.findAllByCustomerId(customer.get().getId());
                ordersIterable.forEach(orders::add);
            }
        }


        //ordersIterable.forEach(orders::add);
        model.addAttribute("orders", orders);
        model.addAttribute("module", "customers");
        return "detailed_customer";
    }
}
