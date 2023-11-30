package com.mkenlo.adminweb.data.repository;

import com.mkenlo.adminweb.data.model.Customer;
import com.mkenlo.adminweb.data.model.Order;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testGetAllOrders(){
        Iterable<Order> orders = orderRepository.findAll();

        assert(IterableUtil.sizeOf(orders)==3);
    }

    @Test
    public void testGetAllCustomers(){
        Iterable<Customer> customers = customerRepository.findAll();
        assert(IterableUtil.sizeOf(customers)==7);
    }
}
