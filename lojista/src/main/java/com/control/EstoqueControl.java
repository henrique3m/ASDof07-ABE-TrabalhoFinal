package com.control;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.model.Estoque;
import com.model.ItemEstoque;


import io.swagger.annotations.ApiOperation;



@RestController
@RequestMapping("/estoque")
public class EstoqueControl {
	
	
	
	@ApiOperation(value = "Consulta de produtos no estoque.",tags={ "Estoque", })
	@RequestMapping(method = RequestMethod.GET, value ="/getItens")
    public ResponseEntity<List<ItemEstoque>> GetItens() {
		List<ItemEstoque> itens = new ArrayList<ItemEstoque>();
		itens = Estoque.GetItens();
		for(ItemEstoque i : itens) {
			if(!i.getProd().hasLinks()){
				i.getProd().add(linkTo(methodOn(ProdutoControl.class).GetProduto(i.getProd().getCod())).withSelfRel());
			}
		}

        return new ResponseEntity<List<ItemEstoque>>(itens, HttpStatus.OK);
    }

}
