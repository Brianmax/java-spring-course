package com.codigo.codigo;

import com.codigo.codigo.controller.TareController;
import com.codigo.codigo.repository.TareaRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class CodigoApplication {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Ingrese el valor con el cual vamos a dividir");
		int b = sc.nextInt();
		try {
			int a = 100 / b;
        }
		catch (Exception e) {
			System.out.println("Error en la division");
			System.out.println(e.getMessage());
		}
		SpringApplication.run(CodigoApplication.class, args);

	}
}
