package com.model.orcamento;

import java.util.List;

public class SolicitacaoOrcamento {
	private int cliente;
	private List<ItemSolOrc> itens;
	private String callback;
	

	public String getCallback() {
		return callback;
	}
	public void setCallback(String callback) {
		this.callback = callback;
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
	public List<ItemSolOrc> getItens() {
		return itens;
	}
	public void AddItem(ItemSolOrc i){
		itens.add(i);
	}
	
	
	
}
