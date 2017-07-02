package com.control;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import com.model.Produto;


@RestController
@RequestMapping("/estoque/produto")
public class ProdutoControl {
	

	@ApiOperation(value = "Consulta de produto por ID.", hidden=true)
	@RequestMapping(method = RequestMethod.GET, value ="/{id}")
    public ResponseEntity<Produto> GetProduto(@PathVariable("id") int cod) {

		Produto prod = Produto.GetProduto(cod);
		if (!prod.hasLinks()){
			prod.add(linkTo(methodOn(ProdutoControl.class).GetProduto(cod)).withSelfRel());
		}
        return new ResponseEntity<Produto>(prod, HttpStatus.OK);
    }

}
