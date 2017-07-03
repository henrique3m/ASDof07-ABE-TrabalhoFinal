package com.control;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.model.ExecutaFluxo;

import io.swagger.annotations.ApiOperation;

@SpringBootApplication
@RestController
@RequestMapping("/fluxocompleto")
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
	
	@ApiOperation(value = "Executa o fluxo descrito no enunciado do Trabalho Final.",tags={ "ExecutaFluxo", })
	@RequestMapping(method = RequestMethod.GET, value ="/executafluxo")
    public ResponseEntity<String> ById() {
		String resp= ExecutaFluxo.Executar();
        return new ResponseEntity<String>(resp, HttpStatus.OK);
    }

}
