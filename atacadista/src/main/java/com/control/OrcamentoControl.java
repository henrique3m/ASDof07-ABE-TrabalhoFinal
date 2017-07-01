package com.control;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.springframework.hateoas.Link;
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
import com.model.solicitacao.Solicitacao;

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
		for(Orcamento orc : orcamentos) {
			if(!orc.hasLink(Link.REL_SELF)){
				orc.add(linkTo(methodOn(OrcamentoControl.class).ById(orc.getCodorcamento())).withSelfRel());
			}
			if (!orc.hasLink(Link.REL_PREVIOUS)) {
				orc.add(linkTo(methodOn(OrcamentoControl.class).ReprovaOrcamento(orc.getCodorcamento())).withRel(Link.REL_PREVIOUS));
			}
			if (!orc.hasLink(Link.REL_NEXT)) {
				orc.add(linkTo(methodOn(OrcamentoControl.class).AprovaOrcamento(orc.getCodorcamento())).withRel(Link.REL_NEXT));	
			}
			for(ItemSolicitacao it : orc.getItens()){
				if(!it.getProd().hasLink(Link.REL_SELF)){
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
		if(!orc.hasLink(Link.REL_SELF)){
			orc.add(linkTo(methodOn(OrcamentoControl.class).ById(orc.getCodorcamento())).withSelfRel());
		}
		if (!orc.hasLink(Link.REL_PREVIOUS)) {
			orc.add(linkTo(methodOn(OrcamentoControl.class).ReprovaOrcamento(orc.getCodorcamento())).withRel(Link.REL_PREVIOUS));
		}
		if (!orc.hasLink(Link.REL_NEXT)) {
			orc.add(linkTo(methodOn(OrcamentoControl.class).AprovaOrcamento(orc.getCodorcamento())).withRel(Link.REL_NEXT));	
		}
		for(ItemSolicitacao it : orc.getItens()){
			if(!it.getProd().hasLink(Link.REL_SELF)){
				it.getProd().add(linkTo(methodOn(ProdutoControl.class).ById(it.getProd().getCod())).withSelfRel());
			}
		}

        return new ResponseEntity<Orcamento>(orc, HttpStatus.OK);
    }
	
	
	/********************************************************/
	/********************************************************/
	/******************solicitaOrcamento*********************/
	/********************************************************/
	/********************************************************/
	
	
	
	@ApiOperation(value = "Inclui nova solicitacao de orcamento")
	@RequestMapping(method = RequestMethod.POST, value ="/solicitaorcamento")
    public ResponseEntity<Orcamento> SolicitaOrcamento(@RequestBody SolicitacaoOrcamento sO)  {
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
		if (!orc.hasLink(Link.REL_SELF)) {
			orc.add(linkTo(methodOn(OrcamentoControl.class).ById(orc.getCodorcamento())).withSelfRel());
			
		}
		if (!orc.hasLink(Link.REL_PREVIOUS)) {
			orc.add(linkTo(methodOn(OrcamentoControl.class).ReprovaOrcamento(orc.getCodorcamento())).withRel(Link.REL_PREVIOUS));
		}
		if (!orc.hasLink(Link.REL_NEXT)) {
			orc.add(linkTo(methodOn(OrcamentoControl.class).AprovaOrcamento(orc.getCodorcamento())).withRel(Link.REL_NEXT));	
		}
        return new ResponseEntity<Orcamento>(orc, HttpStatus.OK);
    }
	
	
	/********************************************************/
	/********************************************************/
	/******************AprovaOrcamento*********************/
	/********************************************************/
	/********************************************************/
	
	@ApiOperation(value = "Aprova orcamento enviado ao Lojista")
	@RequestMapping(method = RequestMethod.PUT, value ="/{id}/efetuarpedido")
    public ResponseEntity<Solicitacao> AprovaOrcamento(@PathVariable("id") int cod) {
		Orcamento o = Orcamento.ById(cod);
		Solicitacao sol = new Solicitacao(o);
		Orcamento.Cancela(o.getCodorcamento());
		if(!sol.hasLink(Link.REL_PREVIOUS)){
			sol.add(linkTo(methodOn(SolicitacaoControl.class).Cancela(sol.getCod())).withRel(Link.REL_PREVIOUS));
		}
		if(!sol.hasLink(Link.REL_SELF)){
			sol.add(linkTo(methodOn(SolicitacaoControl.class).ById(sol.getCod())).withSelfRel());
		}
		for(ItemSolicitacao it : sol.getItens()){
			if(!it.getProd().hasLink(Link.REL_SELF)){
				it.getProd().add(linkTo(methodOn(ProdutoControl.class).ById(it.getProd().getCod())).withSelfRel());
			}
		}
        return new ResponseEntity<Solicitacao>(sol, HttpStatus.OK);
    }
	
	
	
	/********************************************************/
	/********************************************************/
	/******************ReprovaOrcamento*********************/
	/********************************************************/
	/**
	 * @throws JSONException ******************************************************/
	
	
	@ApiOperation(value = "Reprova orcamento enviado ao Lojista")
	@RequestMapping(method = RequestMethod.DELETE, value ="/{id}/reprovar")
    public ResponseEntity<String> ReprovaOrcamento(@PathVariable("id") int cod) {
		Orcamento.Cancela(cod);
        return new ResponseEntity<String>("O Orcamento foi reprovado e o pedido cancelado.", HttpStatus.OK);
    }
	
	
	

}
