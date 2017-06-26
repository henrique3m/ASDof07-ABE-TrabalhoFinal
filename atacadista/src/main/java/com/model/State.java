package com.model;

public enum State {
	SOLICITADO("SOLICITADO"),
	EM_FABRICACAO("EM FABRICACAO"),
	FINALIZADO("FINALIZADO"),
	DESPACHADO("DESPACHADO"),
	CANCELADO("CANCELADO");
	private final String state;
	State(String state){
		this.state = state;
	}
	public String getState(){
		return state;
	}
}
