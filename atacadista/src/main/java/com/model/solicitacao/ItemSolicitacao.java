package com.model.solicitacao;

public class ItemSolicitacao {
	private Produto prod;
	private int cod, qtd;
	private double valorItem;
	private String obs;
	
	
	public ItemSolicitacao(int cod, Produto prod, int qtd, double valorItem, String obs) {
		if(cod < 1) {
			throw new IllegalArgumentException("Nao e permitido atribuir o "
					+ "valor menor que 1 ao codigo do Item de Solicitacao.");
		}
		else {
			if(prod == null) {
			throw new IllegalArgumentException("Nao e permitido atribuir o valor NULL ao Produto do Item de Estoque.");
			}
			else {
				if(qtd < 0) {
					throw new IllegalArgumentException("Nao e permitido atribuir o "
							+ "valor menor que 0 a quantidade do Produto do Item de Estoque.");
				}
				else {
					this.cod = cod;
					this.prod = prod;
					this.qtd = qtd;
					this.valorItem = valorItem;
					this.obs = obs;
				}
			}
		}
			
	}
	
	public int getCod() {
		return cod;
	}
	public void setCod(int cod) {
		if(cod < 1) {
			throw new IllegalArgumentException("Nao e permitido atribuir o "
					+ "valor menor que 0 ao codigo do Item de Solicitacao.");
		}
		else {
			this.cod = cod;
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
	public double getValorItem() {
		return valorItem;
	}

	public void setValorItem(double valorItem) {
		this.valorItem = valorItem;
	}

	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}

}
