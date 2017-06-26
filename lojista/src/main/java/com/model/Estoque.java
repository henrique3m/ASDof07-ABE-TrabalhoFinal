package com.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

public class Estoque extends ResourceSupport {
	private int cod;
	private static List<ItemEstoque> Itens = new ArrayList();

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

	public static List<ItemEstoque> getItens() {
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
	

}
