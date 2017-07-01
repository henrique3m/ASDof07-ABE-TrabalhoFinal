package com.model;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.hateoas.ResourceSupport;

public class Estoque extends ResourceSupport {
	private int cod;
	private static List<ItemEstoque> Itens = new ArrayList<ItemEstoque>();

	public Estoque(int cod) {
		if(cod < 1) {
			throw new IllegalArgumentException("Nao e permitido atribuir o valor menor que 1 ao codigo do Estoque.");
		}
		else {
			this.cod = cod;
		}
	}

	public int getCod() {
		return cod;
	}

	public void setCod(int cod) {
		if(cod < 1) {
			throw new IllegalArgumentException("Nao e permitido atribuir o valor menor que 1 ao codigo do Estoque.");
		}
		else {
			this.cod = cod;
		}
	}

	public List<ItemEstoque> getItens() {
		return Itens;
	}

	public void addItem(ItemEstoque item) {
		if(item == null) {
			throw new IllegalArgumentException("Nao e permitido atribuir um Item de Estoque vazio ao Estoque.");
		}
		else {
			Itens.add(item);
		}
	}
	
	
	public static Estoque GetEstoque(){
		Estoque estoque = new Estoque(1);
		JSONObject jsonObject;
		
		try {
			//obtem informacoes armazenadas no arquivo.json
			
			@SuppressWarnings("resource")
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
	

}
