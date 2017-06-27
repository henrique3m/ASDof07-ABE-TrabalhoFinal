package com.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

public class Solicitacao extends ResourceSupport {
	
	private int cod;
	
	private  List<ItemSolicitacao> Itens = new ArrayList();
	private int codClinte;
	private String state, callback;

	
	public Solicitacao(int cod, int codClinte, String state, String callback) {
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
//				String a = State.valueOf(state.toUpperCase().replaceAll(" ", "_"));
//				 switch (a){
//				 case SOLICITADO:
//					 this.state = State.SOLICITADO;
//				 case EM_FABRICACAO:
//					 this.state = State.EM_FABRICACAO;
//				 case FINALIZADO:
//					 this.state = State.FINALIZADO;
//				 case DESPACHADO:
//					 this.state = State.DESPACHADO;
//				 case CANCELADO:
//					 this.state = State.CANCELADO;
//				 default:
//					 throw new IllegalArgumentException("Valor invalido para estado da Solicitacao.");
//				 }

			}
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
		return Itens;
	}

	public void addItem(ItemSolicitacao item) {
		if(item == null) {
			throw new IllegalArgumentException("Nao e permitido atribuir um Item de Estoque vazio ao Estoque.");
		}
		else {
			Itens.add(item);
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
//		State a = State.valueOf(state.toUpperCase());
//		 switch (a){
//		 case SOLICITADO:
//			 this.state = State.SOLICITADO;
//		 case EM_FABRICACAO:
//			 this.state = State.EM_FABRICACAO;
//		 case FINALIZADO:
//			 this.state = State.FINALIZADO;
//		 case DESPACHADO:
//			 this.state = State.DESPACHADO;
//		 case CANCELADO:
//			 this.state = State.CANCELADO;
//		 default:
//			 throw new IllegalArgumentException("Valor invalido para estado da Solicitacao.");
//		 }
	}


	public String getCallback() {
		return callback;
	}


	public void setCallback(String callback) {
		this.callback = callback;
	}

}
