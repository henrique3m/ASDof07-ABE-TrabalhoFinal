package com.model.solicitacao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.hateoas.ResourceSupport;

import com.google.gson.Gson;
import com.model.orcamento.Orcamento;

public class Solicitacao extends ResourceSupport {
	
	private int cod;
	
	private  List<ItemSolicitacao> itens = new ArrayList<ItemSolicitacao>();
	private int codClinte;
	private String state, callback;
	private double valorTotal;

	
	public Solicitacao(int cod, int codClinte, String state, double valorTotal, String callback) {
		if(cod < 1) {
			throw new IllegalArgumentException("Nao e permitido atribuir o valor menor que 1 ao codigo do Estoque.");
		}
		else {
			if(codClinte < 1) {
				throw new IllegalArgumentException("Nao e permitido atribuir o valor menor que 1 ao codigo do Estoque.");
			}
			else {
				this.cod = cod;
				this.codClinte = codClinte;
				this.state = state;
				this.callback = callback;
				this.valorTotal = valorTotal;

			}
		}
	}
	
	public Solicitacao(Solicitacao s){
		if (s == null) {
			throw new IllegalArgumentException("Orcamento esta vazio.");
		}
		else{
			this.cod = s.getCod();
			this.codClinte = s.getCodClinte();
			this.state = s.getState();
			this.callback = s.getCallback();
			this.valorTotal = s.getValorTotal();
			this.itens = s.getItens();
		}
		
	}
	

	public Solicitacao(Orcamento o){
		if (o == null) {
			throw new IllegalArgumentException("Orcamento esta vazio.");
		}
		else{
			this.cod = getNewId();
			this.codClinte = o.getCliente();
			this.state = "Solicitado";
			this.callback = o.getCallback();
			this.valorTotal = o.getValorTotal();
			this.itens = o.getItens();
			
			this.save();
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

	public List<ItemSolicitacao> getItens() {
		return itens;
	}

	public void addItem(ItemSolicitacao item) {
		if(item == null) {
			throw new IllegalArgumentException("Nao e permitido atribuir um Item de Estoque vazio ao Estoque.");
		}
		else {
			itens.add(item);
		}
	}

	public int getCodClinte() {
		return codClinte;
	}

	public void setCodClinte(int codClinte) {
		if(codClinte < 1) {
			throw new IllegalArgumentException("Nao e permitido atribuir o valor menor que 1 ao codigo do Estoque.");
		}
		else {
			this.codClinte = codClinte;
		}
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}


	public double getValorTotal() {
		return valorTotal;
	}


	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}


	public String getCallback() {
		return callback;
	}


	public void setCallback(String callback) {
		this.callback = callback;
	}


	
	
	
	
	public static List<Solicitacao> GetSolicitacoes(){
		List<Solicitacao> solicitacoes = new ArrayList<Solicitacao>();
		JSONObject jsonObject;
		
		try {
			//obtem informacoes armazenadas no arquivo.json
			
			@SuppressWarnings("resource")
			FileInputStream fileInputStream = new FileInputStream("Solicitacoes.json");
			StringBuffer stringBuffer = new StringBuffer("");
			String json = "";
		    int x;
		    while((x = fileInputStream.read())!=-1)
		    {
		        stringBuffer.append((char)x);
		    }
		    json = stringBuffer.toString();
		    if (json.length() > 0) {
				jsonObject = new JSONObject(json);
				JSONArray jSols = jsonObject.getJSONArray("solicitacoes");
				
				for (int i=0; i < jSols.length(); i++) {
					JSONObject jSol = jSols.getJSONObject(i);
					
					Solicitacao sol = new Solicitacao(
							jSol.getInt("cod"), jSol.getInt("codClinte"), jSol.getString("state"), 
							jSol.getDouble("valorTotal"), jSol.getString("callback"));
					JSONArray jItensSol =  jSol.getJSONArray("itens");
					for (int y=0; y < jItensSol.length(); y++) {
						JSONObject jItem = jItensSol.getJSONObject(y);
						System.out.println(jItem);
						JSONObject jProd = jItem.getJSONObject("prod");
						ItemSolicitacao item = new ItemSolicitacao(jItem.getInt("cod"),
								new Produto(jProd.getInt("cod"), jProd.getString("desc"), jProd.getDouble("preco")), 
								jItem.getInt("qtd"), jItem.getDouble("valorItem"), jItem.getString("obs"));
						sol.addItem(item);
					}
					
					solicitacoes.add(sol);
				}
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
	
	
	public static int getNewId(){
		int last=0;
		for(Solicitacao s : GetSolicitacoes()) {
			if (last < s.getCod()){
				last = s.getCod();
			}
		}
		return last +1;
	}
		
	public void save(){
		List<Solicitacao> solicitacoes = GetSolicitacoes();
		solicitacoes.add(this);
		SaveAll(solicitacoes);
        
	}
	
	public static void SaveAll(List<Solicitacao> solicitacoes) {
		Gson g = new Gson();
		String sSols = g.toJson(solicitacoes);
		JSONArray sols = null;
		try {
			sols = new JSONArray(sSols);
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		JSONObject jSols = new JSONObject();
		try {
			jSols.put("solicitacoes", sols);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
        try {
        	FileWriter file = new FileWriter("Solicitacoes.json");
			file.write(jSols.toString());
			file.flush();
	        file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
	}
	
	public static void Cancela(int cod) {
		List<Solicitacao> solicitacoes = GetSolicitacoes();
		int i = 0;
		for(Solicitacao s : solicitacoes){
			if(s.getCod() == cod) {
				solicitacoes.remove(i);
				break;
			}
			i +=1;
		}
		SaveAll(solicitacoes);
	}
	
	public static Solicitacao AlteraStatus(int cod, String newStatus){
		List<Solicitacao> solicitacoes = GetSolicitacoes();
		Solicitacao sol=null;
		for (Solicitacao s : solicitacoes){
			if (s.getCod() == cod) {
			
				sol = new Solicitacao(s);

				s.setState(newStatus);
				SaveAll(solicitacoes);
				return sol;
			}
		}
		throw new IllegalArgumentException("Nenhuma solicitacao foi encontrada para o código " + cod);
	}
}
