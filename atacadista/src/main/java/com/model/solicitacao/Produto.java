package com.model.solicitacao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.hateoas.ResourceSupport;


public class Produto extends ResourceSupport {
	private int cod;
	private double preco; 
	private String desc;

	public Produto(int cod, String desc, double preco) {
		if(cod < 1) {
			throw new IllegalArgumentException("Nao e permitido atribuir o valor menor que 1 ao codigo do produto.");
		}
		else {
			if(preco < 0) {
				throw new IllegalArgumentException("Nao e permitido atribuir o valor menor que 0 ao preco do produto.");
			}
			else {
				this.preco = preco;
				this.cod = cod;
				this.desc = desc;
			}
			
		}
	}

	public int getCod() {
		return cod;
	}

	public void setCod(int cod) {
		if(cod < 1) {
			throw new IllegalArgumentException("Nao e permitido atribuir o valor menor que 1 ao codigo do produto.");
		}
		else {
			this.cod = cod;
		}
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		if(preco < 0) {
			throw new IllegalArgumentException("Nao e permitido atribuir o valor menor que 0 ao preco do produto.");
		}
		else {
			this.preco = preco;
		}
		
	} 
	
	
	public static List<Produto> GetProdutos(){
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
	
	
	
	public static Produto ById(int cod) {

		Produto prod = null;
		for(Produto p : GetProdutos()){
			if (p.getCod() == cod){
				prod = p;
			}
			
			
		}
        return prod;
    }
	
	
	
	@Override
	public String toString(){
		return "Codigo: " + cod + "\nDescricao: " + desc + "\nPreco: R$" + preco;
	}
	

}
