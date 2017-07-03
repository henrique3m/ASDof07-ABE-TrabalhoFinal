package com.control;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.json.JSONException;
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
	private static int HTTP_COD_SUCESSO = 200;
	
	
	/*******************************************/
	/*******************************************/
	/****************GETALL*********************/
	/*******************************************/
	/*******************************************/
	
	
	@ApiOperation(value = "Consulta de todas as solicitacoes.",tags={ "Solicitacao", })
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
	
	
	
	
	@ApiOperation(value = "Consulta solicitacao por ID.",tags={ "Solicitacao", })
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

	
	
	@ApiOperation(value = "Cancela Solicitacao do Lojista",tags={ "Solicitacao", })
	@RequestMapping(method = RequestMethod.DELETE, value ="/{id}/cancelar")
    public ResponseEntity<String> Cancela(@PathVariable("id") int cod) {
		Solicitacao.Cancela(cod);
        return new ResponseEntity<String>("O Orcamento foi reprovado e o pedido cancelado.", HttpStatus.OK);
    }
	
	
	
	@ApiOperation(value = "Altera o Status da Solicitacao.",tags={ "Solicitacao", })
	@RequestMapping(method = RequestMethod.PUT, value ="/{id}/alterastatus/{status}")
    public ResponseEntity<String> Cancela(@PathVariable("id") int cod, @PathVariable("status") String newStatus) {
		Solicitacao sol = Solicitacao.AlteraStatus(cod, newStatus);
		String resp = "O status da Solicitacao " + cod + " foi alterado para " + newStatus;
		String api = "http://localhost:8090/notificacao/solicitacao/alteracaostatus";
		
		
		String jSol = "{\"cod\": " + sol.getCod() + ", \"lastStatus\": \"" + sol.getState() + 
				"\", \"newStatus\": \"" + newStatus + "\", \"url\": \"" + 
				linkTo(methodOn(SolicitacaoControl.class).ById(cod)) + "\"}";
		
		System.out.println(jSol);
				
		
		resp += "\nEnviando notificacao para o Lojista.";
		resp += "\nAPI Utilizada: ";
		resp += api;
		try {
		URL url = new URL(api);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");

		OutputStream os = conn.getOutputStream();
		os.write(jSol.getBytes());
		os.flush();

		if (conn.getResponseCode() != HTTP_COD_SUCESSO) {
			throw new RuntimeException("Failed : HTTP error code : "
				+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		
		String json = "";
		String output;
		while ((output = br.readLine()) != null) {
			json +="\n"+output;
		}
		
		conn.disconnect();
		resp += "\n";
		resp += "\n";
		resp += "\nRetorno da API do Lojista: ";
		resp += json;
		
		
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
        return new ResponseEntity<String>(resp, HttpStatus.OK);
    }

}
