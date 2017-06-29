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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.model.orcamento.Orcamento;
import com.model.orcamento.SolicitacaoOrcamento;
import com.model.solicitacao.ItemSolicitacao;
import com.model.solicitacao.Produto;

import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/orcamento")
public class OrcamentoControl {
	
	
private static List<Orcamento> orcamentos = GetOrcamentos();
	
	
	
	private static List<Orcamento> GetOrcamentos(){
		List<Orcamento> orcamentos = new ArrayList<Orcamento>();
		JSONObject jsonObject;
		
		try {
			//obtem informacoes armazenadas no arquivo.json
			
			FileInputStream fileInputStream = new FileInputStream("Orcamentos.json");
			StringBuffer stringBuffer = new StringBuffer("");
			String json = "";
		    int x;
		    while((x = fileInputStream.read())!=-1)
		    {
		        stringBuffer.append((char)x);
		    }
		    json = stringBuffer.toString();

			jsonObject = new JSONObject(json);
			JSONArray jOrcs = jsonObject.getJSONArray("orcamentos");
			
			for (int i=0; i < jOrcs.length(); i++) {
				JSONObject jOrc = jOrcs.getJSONObject(i).getJSONObject("orcamento");
				System.out.println(jOrc);
				List<ItemSolicitacao> itens = new ArrayList();
				JSONArray jItensSol =  jOrc.getJSONArray("itens");
				for (int y=0; y < jItensSol.length(); y++) {
					JSONObject jItem = jItensSol.getJSONObject(y).getJSONObject("item");
					System.out.println(jItem);
					JSONObject jProd = jItem.getJSONObject("produto");
					ItemSolicitacao item = new ItemSolicitacao(jItem.getInt("codItem"),
							new Produto(jProd.getInt("cod"), jProd.getString("desc"), jProd.getDouble("preco")), 
							jItem.getInt("qtd"), jItem.getDouble("valoritem"), jItem.getString("obs"));
					itens.add(item);
				}
				Orcamento orc = new Orcamento(
						jOrc.getInt("codorcamento"), jOrc.getInt("cliente"), jOrc.getDouble("valorTotal"), 
						jOrc.getString("data"), jOrc.getString("callback"), itens);
				
				
				orcamentos.add(orc);
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
		return orcamentos;
	}
	
	
	
	
	/*******************************************/
	/*******************************************/
	/****************GETALL*********************/
	/*******************************************/
	/*******************************************/
	
	
	@ApiOperation(value = "Consulta de todas as solicitacoes.")
	@RequestMapping(method = RequestMethod.GET, value ="/getall")
    public ResponseEntity<List<Orcamento>> GetAll() {
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
	
	
	
	
	@ApiOperation(value = "Consulta de solicitacao por ID.")
	@RequestMapping(method = RequestMethod.GET, value ="/{id}")
    public ResponseEntity<Orcamento> ById(@PathVariable("id") int cod) {
		Orcamento orc = null;
		for(Orcamento c : orcamentos) {
			if(c.getCodorcamento() == cod){
				orc = c;
				if(!orc.hasLinks()){
					orc.add(linkTo(methodOn(OrcamentoControl.class).ById(orc.getCodorcamento())).withSelfRel());
				}
				for(ItemSolicitacao it : c.getItens()){
					if(!it.getProd().hasLinks()){
						it.getProd().add(linkTo(methodOn(ProdutoControl.class).ById(it.getProd().getCod())).withSelfRel());
					}
				}
			}
		}

        return new ResponseEntity<Orcamento>(orc, HttpStatus.OK);
    }
	
	
	/********************************************************/
	/********************************************************/
	/******************solicitaOrcamento*********************/
	/********************************************************/
	/**
	 * @throws JSONException ******************************************************/
	
	
	
	@ApiOperation(value = "Consulta de produtos no estoque.")
	@RequestMapping(method = RequestMethod.POST, value ="/solicitaorcamento")
    public ResponseEntity<Orcamento> solicitaOrcamento(@RequestBody SolicitacaoOrcamento sO) throws JSONException {
		Orcamento orc;

        return new ResponseEntity<Orcamento>(orc, HttpStatus.OK);
    }
	
	
	

}
