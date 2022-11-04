package com.Omer.Account;

import com.Omer.Account.model.Customer;
import com.Omer.Account.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;

@SpringBootApplication
public class AccountApplication implements CommandLineRunner {

	// Bu nesneyi customer eklemek için kullanıdk normalde böyle bir kullanım yok fakat bizim uygulamamızda ki account işlemleri lar var olan customerlar üzerinden olduğu için el ile veritabanına eklemek yerine bu yöntemi kullandık.
	// h2 db kullanıyoruz
	private final CustomerRepository customerRepository;

	public AccountApplication(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(AccountApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception // program ayağı kalkareken çalışacak olan metod
	{
		Customer customer= customerRepository.save(new Customer("","Omer","Faruk",new HashSet<>()));
		//Customer customer2= customerRepository.save(new Customer("","Taha","Yasin",new HashSet<>())); // ikinci bir customer oluşturmak istenir ve öyle bir senaryo da düşünülürse

		System.out.println(customer.getId());
	}




}
