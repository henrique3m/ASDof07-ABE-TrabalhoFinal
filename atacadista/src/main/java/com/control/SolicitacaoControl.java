package com.control;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.model.ItemSolicitacao;
import com.model.Solicitacao;
import com.model.Produto;

import io.swagger.annotations.ApiOperation;



@RestController
@RequestMapping("/solicitacao")
public class SolicitacaoControl {
	
	private static List<Solicitacao> solicitacoes = GetSolicitacoes();
	
	
	
	private static List<Solicitacao> GetSolicitacoes(){
		List<Solicitacao> solicitacoes = new ArrayList<Solicitacao>();
		JSONObject jsonObject;
		
		try {
			//obtem informacoes armazenadas no arquivo.json
			
			FileInputStream fileInputStream = new FileInputStream("Solicitacoes.json");
			StringBuffer stringBuffer = new StringBuffer("");
			String json = "";
		    int x;
		    while((x = fileInputStream.read())!=-1)
		    {
		        stringBuffer.append((char)x);
		    }
		    json = stringBuffer.toString();

			jsonObject = new JSONObject(json);
			JSONArray jSols = jsonObject.getJSONArray("solicitacoes");
			
			for (int i=0; i < jSols.length(); i++) {
				JSONObject jSol = jSols.getJSONObject(i).getJSONObject("solicitacao");
				System.out.println(jSol);
				
				Solicitacao sol = new Solicitacao(
						jSol.getInt("codSolicitacao"), jSol.getInt("cliente"), jSol.getString("estado"), jSol.getString("callback"));
				JSONArray jItensSol =  jSol.getJSONArray("itensSolicitacao");
				for (int y=0; y < jItensSol.length(); y++) {
					JSONObject jItem = jItensSol.getJSONObject(y).getJSONObject("item");
					System.out.println(jItem);
					JSONObject jProd = jItem.getJSONObject("produto");
					ItemSolicitacao item = new ItemSolicitacao(jItem.getInt("codItem"),
							new Produto(jProd.getInt("cod"), jProd.getString("desc"), jProd.getDouble("preco")), 
							jItem.getInt("qtd"), jItem.getString("obs"));
					sol.addItem(item);
				}
				
				solicitacoes.add(sol);
			}
		} 
		//Trata as exceptions que podem ser lancadas no decorrer do processo
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return solicitacoes;
	}

	@ApiOperation(value = "Consulta de produtos no estoque.")
	@RequestMapping(method = RequestMethod.GET, value ="/getAll")
    public ResponseEntity<List<Solicitacao>> GetAll() {
		for(Solicitacao s : solicitacoes) {
			if(!s.hasLinks()){
				s.add(linkTo(methodOn(SolicitacaoControl.class).ById(s.getCod())).withSelfRel());
			}
		}

        return new ResponseEntity<List<Solicitacao>>(solicitacoes, HttpStatus.OK);
    }
	
	@ApiOperation(value = "Consulta de produtos no estoque.")
	@RequestMapping(method = RequestMethod.GET, value ="/{id}")
    public ResponseEntity<Solicitacao> ById(@PathVariable("id") int cod) {
		Solicitacao sol = null;
		for(Solicitacao s : solicitacoes) {
			if(s.getCod() == cod){
				sol = s;
				if(!sol.hasLinks()){
					sol.add(linkTo(methodOn(SolicitacaoControl.class).ById(sol.getCod())).withSelfRel());
				}
			}
		}

        return new ResponseEntity<Solicitacao>(sol, HttpStatus.OK);
    }

}
