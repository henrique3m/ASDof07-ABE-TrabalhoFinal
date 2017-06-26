package com.model;

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
	
	@Override
	public String toString(){
		return "Codigo: " + cod + "\nDescricao: " + desc + "\nPreco: R$" + preco;
	}
	

}
