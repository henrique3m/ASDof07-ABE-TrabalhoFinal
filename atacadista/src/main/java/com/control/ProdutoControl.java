package com.control;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
	
	private static List<Produto> produtos = getProdutos();
	
	
	
	private static List<Produto> getProdutos(){
		JSONObject jsonObject;
		List<Produto> produtos = new ArrayList<Produto>();
		
		try {
			//obtem informacoes armazenadas no arquivo.json
			
			FileInputStream fileInputStream = new FileInputStream("produto.json");
			StringBuffer stringBuffer = new StringBuffer("");
			String json = "";
		    int x;
		    while((x = fileInputStream.read())!=-1)
		    {
		        stringBuffer.append((char)x);
		    }
		    json = stringBuffer.toString();
			//jsonObject = (JSONObject) parser.parse(new FileReader("estoque.json"));
			jsonObject = new JSONObject(json);
	
			JSONArray jProds = jsonObject.getJSONArray("produtos");
			
			
			for (int i=0; i < jProds.length(); i++) {
				 
				JSONObject jPro = jProds.getJSONObject(i);
				Produto prod =  new Produto(jPro.getInt("cod"), jPro.getString("desc"), jPro.getDouble("preco"));
				produtos.add(prod);
				
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
		return produtos;
	}

	@ApiOperation(value = "Consulta de produto por ID.")
	@RequestMapping(method = RequestMethod.GET, value ="/{id}")
    public ResponseEntity<Produto> ById(@PathVariable("id") int cod) {

		Produto prod = null;
		for(Produto p : produtos){
			if (p.getCod() == cod){
				if (!p.hasLinks()){
					p.add(linkTo(methodOn(ProdutoControl.class).ById(cod)).withSelfRel());
				}
				prod = p;
			}
			
			
		}
        return new ResponseEntity<Produto>(prod, HttpStatus.OK);
    }

}
