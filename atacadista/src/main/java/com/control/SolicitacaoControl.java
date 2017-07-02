package com.control;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.model.solicitacao.ItemSolicitacao;
import com.model.solicitacao.Solicitacao;

import io.swagger.annotations.ApiOperation;



@RestController
@RequestMapping("/solicitacao")
public class SolicitacaoControl {
	
	
	
	/*******************************************/
	/*******************************************/
	/****************GETALL*********************/
	/*******************************************/
	/*******************************************/
	
	
	@ApiOperation(value = "Consulta de todas as solicitações.",tags={ "Solicitação", })
	@RequestMapping(method = RequestMethod.GET, value ="/getall")
    public ResponseEntity<List<Solicitacao>> GetAll() {
		List<Solicitacao> solicitacoes = Solicitacao.GetSolicitacoes();
		for(Solicitacao s : solicitacoes) {
			if(!s.hasLink(Link.REL_PREVIOUS)){
				s.add(linkTo(methodOn(SolicitacaoControl.class).Cancela(s.getCod())).withRel(Link.REL_PREVIOUS));
			}
			if(!s.hasLink(Link.REL_SELF)){
				s.add(linkTo(methodOn(SolicitacaoControl.class).ById(s.getCod())).withSelfRel());
			}
			for(ItemSolicitacao it : s.getItens()){
				if(!it.getProd().hasLink(Link.REL_SELF)){
					it.getProd().add(linkTo(methodOn(ProdutoControl.class).ById(it.getProd().getCod())).withSelfRel());
				}
			}
		}

        return new ResponseEntity<List<Solicitacao>>(solicitacoes, HttpStatus.OK);
    }
	
	
	
	
	/*******************************************/
	/*******************************************/
	/******************ByID*********************/
	/*******************************************/
	/*******************************************/
	
	
	
	
	@ApiOperation(value = "Consulta solicitação por ID.",tags={ "Solicitação", })
	@RequestMapping(method = RequestMethod.GET, value ="/{id}")
    public ResponseEntity<Solicitacao> ById(@PathVariable("id") int cod) {
		Solicitacao sol = null;
		for(Solicitacao s : Solicitacao.GetSolicitacoes()) {
			if(s.getCod() == cod){
				sol = s;
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
			}
		}

        return new ResponseEntity<Solicitacao>(sol, HttpStatus.OK);
    }

	
	
	@ApiOperation(value = "Cancela Solicitação do Lojista",tags={ "Solicitação", })
	@RequestMapping(method = RequestMethod.DELETE, value ="/{id}/cancelar")
    public ResponseEntity<String> Cancela(@PathVariable("id") int cod) {
		Solicitacao.Cancela(cod);
        return new ResponseEntity<String>("O Orcamento foi reprovado e o pedido cancelado.", HttpStatus.OK);
    }
	

}
