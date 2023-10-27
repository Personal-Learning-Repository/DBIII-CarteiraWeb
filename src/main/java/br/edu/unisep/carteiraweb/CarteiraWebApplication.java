package br.edu.unisep.carteiraweb;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;

@SpringBootApplication
public class CarteiraWebApplication {

	//TODO Dockerizar
	//TODO Paging nos controllers
	//TODO Webflux

	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());
		SpringApplication.run(CarteiraWebApplication.class, args);
	}

}
