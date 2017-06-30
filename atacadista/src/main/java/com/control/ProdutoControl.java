package com.control;



import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.model.solicitacao.Produto;

import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/produto")
public class ProdutoControl {
		

	@ApiOperation(value = "Consulta de produto por ID.")
	@RequestMapping(method = RequestMethod.GET, value ="/{id}")
    public ResponseEntity<Produto> ById(@PathVariable("id") int cod) {
		Produto p = Produto.ById(cod);
		if (!p.hasLinks()){
			p.add(linkTo(methodOn(ProdutoControl.class).ById(cod)).withSelfRel());
		}
        return new ResponseEntity<Produto>(p, HttpStatus.OK);
    }
	

}
