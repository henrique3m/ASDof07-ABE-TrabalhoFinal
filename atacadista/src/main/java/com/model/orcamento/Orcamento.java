package com.model.orcamento;



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
import com.model.solicitacao.ItemSolicitacao;
import com.model.solicitacao.Produto;

public class Orcamento extends ResourceSupport{
	 private int codorcamento, cliente;
     private double valorTotal = 0;
     private String callback, data;
     private List<ItemSolicitacao> itens = new ArrayList<ItemSolicitacao>();
     
	public Orcamento(){
	    	 
	     }

	public Orcamento(int codorcamento, int cliente, double valorTotal, String data, String callback, List<ItemSolicitacao> itens) {
		if(codorcamento < 1) {
			throw new IllegalArgumentException("Nao e permitido atribuir o "
					+ "valor menor que 1 ao codigo do Orcamento.");
		}
		else {
			if(cliente < 1) {
				throw new IllegalArgumentException("Nao e permitido atribuir o "
						+ "valor menor que 1 ao codigo do Cliente.");
			}
			else {
				if(valorTotal <= 0) {
					throw new IllegalArgumentException("Nao e permitido atribuir o "
							+ "valor menor ou igual a 0 Valor Total do Orcamento.");
				}
				else {
					this.valorTotal = valorTotal;
					this.cliente = cliente;
					this.codorcamento = codorcamento;
					this.data = data;
					this.callback = callback;
					this.itens = itens;
				}
			}
		}

		
	}
	
	public Orcamento(int codorcamento, int cliente, String data, String callback) {
		if(codorcamento < 1) {
			throw new IllegalArgumentException("Nao e permitido atribuir o "
					+ "valor menor que 1 ao codigo do Orcamento.");
		}
		else {
			if(cliente < 1) {
				throw new IllegalArgumentException("Nao e permitido atribuir o "
						+ "valor menor que 1 ao codigo do Cliente.");
			}
			else {
				this.cliente = cliente;
				this.codorcamento = codorcamento;
				this.data = data;
				this.callback = callback;
			}
		}
		
	}
	
	public int getCodorcamento() {
		return codorcamento;
	}
	public void setCodorcamento(int codorcamento) {
		if(codorcamento < 1) {
			throw new IllegalArgumentException("Nao e permitido atribuir o "
					+ "valor menor que 1 ao codigo do Orcamento.");
		}
		else {
			this.codorcamento = codorcamento;
		}
	}
	public int getCliente() {
		return cliente;
	}
	public void setCliente(int cliente) {
		if(cliente < 1) {
			throw new IllegalArgumentException("Nao e permitido atribuir o "
					+ "valor menor que 1 ao codigo do Cliente.");
		}
		else {
			this.cliente = cliente;
		}
	}
	public double getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(double valorTotal) {
		if(valorTotal <= 0) {
			throw new IllegalArgumentException("Nao e permitido atribuir o "
					+ "valor menor ou igual a 0 Valor Total do Orcamento.");
		}
		else {
			this.valorTotal = valorTotal;
		}
	}
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getCallback() {
		return callback;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}
	public List<ItemSolicitacao> getItens() {
		return itens;
	}
	public void addItem(ItemSolicitacao i) {
		if (i == null) {
			throw new IllegalArgumentException("Nao e permitido atribuir um"
					+ "item vazio a lista de itens.");
		}
		else{
			itens.add(i);
			valorTotal += i.getValorItem();
		}
	}
	
	
	public static int getNewId(){
		int last=0;
		for(Orcamento c : GetOrcamentos()) {
			if (last < c.getCodorcamento()){
				last = c.getCodorcamento();
			}
		}
		return last +1;
	}
	
	private static List<Orcamento> GetOrcamentos(){
		List<Orcamento> orcamentos = new ArrayList<Orcamento>();
		JSONObject jsonObject;
		
		try {
			//obtem informacoes armazenadas no arquivo.json
			
			@SuppressWarnings("resource")
			FileInputStream fileInputStream = new FileInputStream("Orcamentos.json");
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
				JSONArray jOrcs = jsonObject.getJSONArray("orcamentos");
				
				for (int i=0; i < jOrcs.length(); i++) {
					JSONObject jOrc = jOrcs.getJSONObject(i);
					System.out.println(jOrc);
					List<ItemSolicitacao> itens = new ArrayList<ItemSolicitacao>();
					JSONArray jItensSol =  jOrc.getJSONArray("itens");
					for (int y=0; y < jItensSol.length(); y++) {
						JSONObject jItem = jItensSol.getJSONObject(y);
						System.out.println(jItem);
						JSONObject jProd = jItem.getJSONObject("prod");
						ItemSolicitacao item = new ItemSolicitacao(jItem.getInt("cod"),
								new Produto(jProd.getInt("cod"), jProd.getString("desc"), jProd.getDouble("preco")), 
								jItem.getInt("qtd"), jItem.getDouble("valorItem"), jItem.getString("obs"));
						itens.add(item);
					}
					Orcamento orc = new Orcamento(
							jOrc.getInt("codorcamento"), jOrc.getInt("cliente"), jOrc.getDouble("valorTotal"), 
							jOrc.getString("data"), jOrc.getString("callback"), itens);
					
					
					orcamentos.add(orc);
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
		return orcamentos;
	}
	
	
	
	public static List<Orcamento> GetAll() {
		List<Orcamento> orcamentos = GetOrcamentos();
        return orcamentos;
    }
	
	public static Orcamento ById(int cod) {
		Orcamento orc = null;
		for(Orcamento c : GetOrcamentos()) {
			if(c.getCodorcamento() == cod){
				orc = c;
			}
		}
		return orc;
	}
	
	public void save(){
		List<Orcamento> orcamentos = GetOrcamentos();
		orcamentos.add(this);
		SaveAll(orcamentos);
        
	}
	
	public static void SaveAll(List<Orcamento> orcamentos){
		Gson g = new Gson();
		String sOrcs = g.toJson(orcamentos);
		JSONArray orcs ;
		JSONObject jOrcs = new JSONObject();
		try {
			orcs = new JSONArray(sOrcs);
			jOrcs.put("orcamentos", orcs);
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
        try {
        	FileWriter file = new FileWriter("orcamentos.json");
			file.write(jOrcs.toString());
			file.flush();
	        file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
	}
	
	public static void Cancela(int cod) {
		List<Orcamento> orcamentos = GetOrcamentos();
		int i = 0;
		for(Orcamento o : orcamentos){
			if(o.codorcamento == cod) {
				orcamentos.remove(i);
				break;
			}
			i +=1;
		}
		SaveAll(orcamentos);
	}
	
	
}
