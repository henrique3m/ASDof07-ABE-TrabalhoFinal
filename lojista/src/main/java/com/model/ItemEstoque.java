package com.model;

public class ItemEstoque {
	private Produto prod;
	private int qtd;
	
	public ItemEstoque(Produto prod, int qtd) {
		if(prod == null) {
			throw new IllegalArgumentException("Nao e permitido atribuir o valor NULL ao Produto do Item de Estoque.");
		}
		else {
			if(qtd < 0) {
				throw new IllegalArgumentException("Nao e permitido atribuir o "
						+ "valor menor que 0 a quantidade do Produto do Item de Estoque.");
			}
			else {
				this.prod = prod;
				this.qtd = qtd;
			}
		}
		
			
	}
	public Produto getProd() {
		return prod;
	}
	public void setProd(Produto prod) {
		if(prod == null) {
			throw new IllegalArgumentException("Nao e permitido atribuir o valor NULL ao Produto do Item de Estoque.");
		}
		else {
			this.prod = prod;
		}
	}
	public int getQtd() {
		return qtd;
	}
	public void setQtd(int qtd) {
		if(qtd < 1) {
			throw new IllegalArgumentException("Nao e permitido atribuir o "
					+ "valor menor que 0 a quantidade do Produto do Item de Estoque.");
		}
		else {
			this.qtd = qtd;
		}
	}

}
