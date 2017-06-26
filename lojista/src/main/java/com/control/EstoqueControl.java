package com.control;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.model.Estoque;
import com.model.ItemEstoque;
import com.model.Produto;

import io.swagger.annotations.ApiOperation;



@RestController
@RequestMapping("/estoque")
public class EstoqueControl {
	
	private static Estoque estoque = GetEstoque();
	
	
	
	private static Estoque GetEstoque(){
		Estoque estoque = new Estoque(1);
		JSONObject jsonObject;
		
		try {
			//obtem informacoes armazenadas no arquivo.json
			
			FileInputStream fileInputStream = new FileInputStream("estoque.json");
			StringBuffer stringBuffer = new StringBuffer("");
			String json = "";
		    int x;
		    while((x = fileInputStream.read())!=-1)
		    {
		        stringBuffer.append((char)x);
		    }
		    json = stringBuffer.toString();

			jsonObject = new JSONObject(json);
			JSONObject est = jsonObject.getJSONObject("estoque");
			JSONArray itensEstoque = est.getJSONArray("itensEstoque");
			
			
			for (int i=0; i < itensEstoque.length(); i++) {
				 
				JSONObject it = itensEstoque.getJSONObject(i);
				JSONObject pro = it.getJSONObject("produto");
				ItemEstoque item =  new ItemEstoque(
						new Produto(pro.getInt("cod"), pro.getString("desc"), pro.getDouble("preco")), 
						it.getInt("qtd"));
				estoque.addItem(item);
				
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
		return estoque;
	}

	@ApiOperation(value = "Consulta de produtos no estoque.")
	@RequestMapping(method = RequestMethod.GET, value ="/getItens")
    public ResponseEntity<List<ItemEstoque>> GetItens() {
		List<ItemEstoque> itens = Estoque.getItens();
		for(ItemEstoque i : itens) {
			if(!i.getProd().hasLinks()){
				i.getProd().add(linkTo(methodOn(ProdutoControl.class).GetProduto(i.getProd().getCod())).withSelfRel());
			}
		}

        return new ResponseEntity<List<ItemEstoque>>(itens, HttpStatus.OK);
    }

}
