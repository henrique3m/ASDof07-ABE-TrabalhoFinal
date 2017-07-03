package com.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




public class ExecutaFluxo {
	private static int HTTP_COD_SUCESSO = 200;
	public static String Executar(){
	
		String resp = "Iniciando Fluxo:";
		String json="";
		
		resp += "\nLojista percebe estoque baixo (quantidade abaixo de 5 unidades.):";		
		resp += "\nAPI Utilizada: http://localhost:8090/estoque/getItens";
		try {
			URL url = new URL("http://localhost:8090/estoque/getItens");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != HTTP_COD_SUCESSO) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			String output;
			
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				json +="\n"+output;
			}
			br = null;


			conn.disconnect();
		
			String prodSol="{ \"cliente\": 1, \"callback\": \"http://localhost:8090/notificacao/comunicarorcamento\", \"itens\":[{ITENS}]}";
			JSONArray jObjs = new JSONArray(json);
			String sItens ="";
			String emBaixa="";
			boolean change = false;
			for (int i=0; i < jObjs.length(); i++) {
				JSONObject it = jObjs.getJSONObject(i);
				JSONObject pro = it.getJSONObject("prod");
				if (it.getInt("qtd")<5){
					change = true;
					sItens += "{ \"prod\": "+ pro.getInt("cod") + 
							", \"qtd\": " + (30-it.getInt("qtd")) + ", \"obs\": \"observacao" + pro.getInt("cod") + "\"},";
					emBaixa += "\n";
					emBaixa += "\n	Produto: " + pro.getInt("cod");
					emBaixa += "\n		Quantidade: " + it.getInt("qtd");
					emBaixa += "\n";
				}
				
			}
			if(!change) {
				return resp += "\nLojista nao possui estoque em baixa.";
			}
			sItens += ",";
			sItens = sItens.replace(",,","");
			prodSol = prodSol.replace("{ITENS}", sItens);
			resp += emBaixa;
			resp += "\n";
			resp += "\n";
			resp += "\n";
			resp += "\n";
			resp += "\nLojista solicita produtos ao Atacadista.";
			
			resp += "\nAPI Utilizada: http://localhost:8091/orcamento/solicitaorcamento";
			resp += "\nJSON enviado para realizar a solicitacao:";
			resp += "\n" + prodSol;
			
			url = new URL("http://localhost:8091/orcamento/solicitaorcamento");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			OutputStream os = conn.getOutputStream();
			os.write(prodSol.getBytes());
			os.flush();

			if (conn.getResponseCode() != HTTP_COD_SUCESSO) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
			}

			 br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			System.out.println("Output from Server .... \n");
			json = "";
			while ((output = br.readLine()) != null) {
				json +="\n"+output;
			}
			
			conn.disconnect();
			JSONObject jProd = new JSONObject(prodSol);
			String callback = jProd.getString("callback");
			resp += "\n";
			resp += "\n";
			resp += "\n";
			resp += "\n";
			resp += "\nAtacadista envia numero, data do orcamento e URI para consulta.";
			resp += "\nAPI Utilizada: " + callback;
			JSONObject jOrc = new JSONObject(json);
			String comOrc = "{\"cod\": " + jOrc.getInt("codorcamento") + 
					", \"data\": \"" + jOrc.getString("data") + "\", \"url\": \"" + 
					jOrc.getJSONObject("_links").getJSONObject("self").getString("href") + "\"}";
			
			resp += "\nJSON enviado para realizar a notificacao: ";
			resp += "\n" + comOrc;
			
			url = new URL(callback);
			conn = null;
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			
			os = null;
			os = conn.getOutputStream();
			os.write(comOrc.getBytes());
			os.flush();

			if (conn.getResponseCode() != HTTP_COD_SUCESSO) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
			}

			 br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			System.out.println("Output from Server .... \n");
			String str = "";
			while ((output = br.readLine()) != null) {
				str +="\n"+output;
			}
			
			conn.disconnect();
			
			resp += "\n";
			resp += "\n";
			resp += "\n";
			resp += "\n";
			resp += "\nMensagem retonada pela API do Lojista:";
			resp += "\n" + str;

			
			JSONObject jComOrc = new JSONObject(comOrc);
			String pescOrc = jComOrc.getString("url");
			resp += "\n";
			resp += "\n";
			resp += "\n";
			resp += "\n";
			resp += "\nLojista acessa os dados do Orcamento.";
			resp += "\nAPI Utilizada: " + pescOrc;
					
			url = new URL(pescOrc);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != HTTP_COD_SUCESSO) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			output = "";
			json = "";
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				json +="\n"+output;
			}
			br = null;
			
			System.out.println(json);

			conn.disconnect();
			

			resp += "\nDados retornados pela API.";
			resp += json;
			
			
			jOrc = new JSONObject(json);
			
			String apro = jOrc.getJSONObject("_links").getJSONObject("next").getString("href");
			String repro = jOrc.getJSONObject("_links").getJSONObject("prev").getString("href");
			
			resp += "\n";
			resp += "\n";
			resp += "\n";
			resp += "\n";
			resp += "\nLojista aprova o orcamento.";
			resp += "\nAPI Utilizada:";
			resp += apro;
			
			
			
			url = new URL(apro);
			conn = null;
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/json");

			if (conn.getResponseCode() != HTTP_COD_SUCESSO) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
			}

			 br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			System.out.println("Output from Server .... \n");
			json = "";
			while ((output = br.readLine()) != null) {
				json +="\n"+output;
			}
			
			conn.disconnect();
			
			resp += "\n";
			resp += "\n";
			resp += "\n";
			resp += "\n";
			resp += "\nJSON retornado pelo Atacadista apos o Orcamento ser aprovado:";
			resp += "\n" + json;
			
			JSONObject jSol = new JSONObject(json);
			int codsol = jSol.getInt("cod");
			List<String> status = new ArrayList<String>(
				Arrays.asList("Em fabricacao", "Despachado", "Finalizado")
			);
			for(String s : status){
				resp += "\n";
				resp += "\n";
				resp += "\n";
				resp += "\n";
				resp += "\nAtacadista altera o status da Solicitacao para " + s + ":";
				
				
				String api = "http://localhost:8091/solicitacao/" + codsol + "/alterastatus/" + s;
				
				
				resp += "\nAPI Utilizada:";
				resp += api;
				
				
				api = api.replace(" ", "%20");
				url = new URL(api);
				conn = null;
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("PUT");
				conn.setRequestProperty("Content-Type", "application/json");

				if (conn.getResponseCode() != HTTP_COD_SUCESSO) {
					throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
				}

				 br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				System.out.println("Output from Server .... \n");
				json = "";
				while ((output = br.readLine()) != null) {
					json +="\n"+output;
				}
				
				conn.disconnect();
				
				resp += "\nRetorno da API do Atacadista:";
				resp += "\n" + json;
				
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
		return resp;
	}
}
