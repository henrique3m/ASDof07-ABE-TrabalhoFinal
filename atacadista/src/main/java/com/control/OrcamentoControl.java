package com.control;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.model.orcamento.ItemSolOrc;
import com.model.orcamento.Orcamento;
import com.model.orcamento.SolicitacaoOrcamento;
import com.model.solicitacao.ItemSolicitacao;
import com.model.solicitacao.Produto;

import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/orcamento")
public class OrcamentoControl {
	
	
	
	/*******************************************/
	/*******************************************/
	/****************GETALL*********************/
	/*******************************************/
	/*******************************************/
	
	
	@ApiOperation(value = "Consulta de todas os orcamentos.")
	@RequestMapping(method = RequestMethod.GET, value ="/getall")
    public ResponseEntity<List<Orcamento>> GetAll() {
		List<Orcamento> orcamentos = Orcamento.GetAll();
		for(Orcamento c : orcamentos) {
			if(!c.hasLinks()){
				c.add(linkTo(methodOn(OrcamentoControl.class).ById(c.getCodorcamento())).withSelfRel());
			}
			for(ItemSolicitacao it : c.getItens()){
				if(!it.getProd().hasLinks()){
					it.getProd().add(linkTo(methodOn(ProdutoControl.class).ById(it.getProd().getCod())).withSelfRel());
				}
			}
		}

        return new ResponseEntity<List<Orcamento>>(orcamentos, HttpStatus.OK);
    }
	
	
	
	
	/*******************************************/
	/*******************************************/
	/******************ByID*********************/
	/*******************************************/
	/*******************************************/
	
	
	
	
	@ApiOperation(value = "Consulta de orcamento por ID.")
	@RequestMapping(method = RequestMethod.GET, value ="/{id}")
    public ResponseEntity<Orcamento> ById(@PathVariable("id") int cod) {
		Orcamento orc = Orcamento.ById(cod);
		if(!orc.hasLinks()){
			orc.add(linkTo(methodOn(OrcamentoControl.class).ById(orc.getCodorcamento())).withSelfRel());
		}
		for(ItemSolicitacao it : orc.getItens()){
			if(!it.getProd().hasLinks()){
				it.getProd().add(linkTo(methodOn(ProdutoControl.class).ById(it.getProd().getCod())).withSelfRel());
			}
		}

        return new ResponseEntity<Orcamento>(orc, HttpStatus.OK);
    }
	
	
	/********************************************************/
	/********************************************************/
	/******************solicitaOrcamento*********************/
	/********************************************************/
	
	
	
	@ApiOperation(value = "Inclui nova solicitacao de orcamento")
	@RequestMapping(method = RequestMethod.POST, value ="/solicitaorcamento")
    public ResponseEntity<Orcamento> solicitaOrcamento(@RequestBody SolicitacaoOrcamento sO) throws JSONException {
		int codItem = 1;
		
		//Pega a data corrente.
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String hoje = sdf.format(cal.getTime());
		
		Orcamento orc = new Orcamento(Orcamento.getNewId(), sO.getCliente(), hoje, sO.getCallback());
		for(ItemSolOrc i : sO.getItens()) {
			Produto p = Produto.ById(i.getProd());
			double valor = p.getPreco() * i.getQtd();
			orc.addItem(new ItemSolicitacao(codItem, p, 
					i.getQtd(), valor, i.getObs()));
			
			codItem +=1;
		}
		orc.save();
        return new ResponseEntity<Orcamento>(orc, HttpStatus.OK);
    }
	
	
	
	

}
