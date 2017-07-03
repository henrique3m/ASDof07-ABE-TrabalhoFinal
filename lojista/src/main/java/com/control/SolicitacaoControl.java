package com.control;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.model.Orcamento;
import com.model.Solicitacao;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/notificacao")
public class SolicitacaoControl {
	
	
	@ApiOperation(value = "Comunica orcamento gerado pelo Atacadista.",tags={ "Notificacao", })
	@RequestMapping(method = RequestMethod.POST, value ="/comunicarorcamento")
    public ResponseEntity<String> ComunicarOrcamento(@RequestBody Orcamento sO) {
		
		String resp = "O orcamento " + sO.getCod() + " gerado na data " + sO.getData() + " foi recebido.";
		resp += "\nURL: " + sO.getUrl();
        return new ResponseEntity<String>(resp, HttpStatus.OK);
    }
	
	
	@ApiOperation(value = "Comunica a alteracao de status de uma solicitacao.",tags={ "Notificacao", })
	@RequestMapping(method = RequestMethod.POST, value ="/solicitacao/alteracaostatus")
    public ResponseEntity<String> ComunicaAlteracaoStatus(@RequestBody Solicitacao so) {
		
		String resp = "A alteracao da solicitacao " + so.getCod() + " do status " + so.getLastStatus() + 
				" para " + so.getNewStatus() + " foi recebida.";
		resp += "\nURL: " + so.getUrl(); 
        return new ResponseEntity<String>(resp, HttpStatus.OK);
    }


}
