package net.zupbank.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.zupbank.springboot.exception.ResourceNotFoundException;
import net.zupbank.springboot.model.Customer;
import net.zupbank.springboot.repository.CustomerRepository;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {

	@Autowired
	private CustomerRepository customerRepository;
	
	// get customers
	@GetMapping("/customers")
	public List<Customer> getAllCustomer() {
		return this.customerRepository.findAll();
	}
	
	// get customer by id
	@GetMapping("/customers/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable(value = "id") Long customerId)
		throws ResourceNotFoundException {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Custumer not found for this id :: " + customerId));
				return ResponseEntity.ok().body(customer);
	}
	
	// save customer
	@PostMapping("/customers")
	public Customer createCustomer(@RequestBody Customer customer) {
		return this.customerRepository.save(customer);
	}
	
	// update customer
	@PutMapping("/customers/{id}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable(value = "id") Long customerId,
			@Validated @RequestBody Customer customerDetails) throws ResourceNotFoundException {
		
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found for this id :: " + customerId));
	
		customer.setName(customerDetails.getName());
		customer.setCpf(customerDetails.getCpf());
		customer.setBirthDate(customerDetails.getBirthDate());
		customer.setEmail(customerDetails.getEmail());
		
		return ResponseEntity.ok(this.customerRepository.save(customer));
	}
	
	// delete customer
	@DeleteMapping("/customers/{id}")
	public Map<String, Boolean> deleteCustomer(@PathVariable(value = "id") Long customerId) throws ResourceNotFoundException {
		
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found for this id :: " + customerId));
	
		customerRepository.delete(customer);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		
		return response;
	}
}
