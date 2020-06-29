package com.example.algamoney.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("algamoney")// nome da configuração
public class AlgamoneyApiProperty {
	
	private String originPermitida = "http://localhost:8080";
	

	public String getOriginPermitida() {
		return originPermitida;
	}

	public void setOriginPermitida(String originPermitida) {
		this.originPermitida = originPermitida;
	}

	private final Seguranca seguranca = new Seguranca();

	public Seguranca getSeguranca() { // criou somente o set porque o objeto criado é do tipo final
		return seguranca;
	}

	public static class Seguranca { // agrupando por temas semelhantes, tudo que for segurança, tendo outro tema
									// cria-se outra classe

		private boolean enableHttps;

		public boolean isEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}

	}

}
