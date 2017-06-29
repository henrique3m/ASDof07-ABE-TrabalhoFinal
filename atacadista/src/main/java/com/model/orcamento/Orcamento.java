package com.model.orcamento;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.model.solicitacao.ItemSolicitacao;

public class Orcamento extends ResourceSupport{
	 private int codorcamento, cliente;
     private double valorTotal = 0;
     private String callback, data;
     private List<ItemSolicitacao> itens = new ArrayList();
     
     

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
	
	public Orcamento(int codorcamento, int cliente, String callback) {
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
}
