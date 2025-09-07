package com.tcc.gelato;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@SpringBootApplication
@EnableScheduling
public class GelatoApplication {
    /**
	 * Roda aplicação do servidor.
	 */
	public static void main(String[] args) throws RuntimeException {
		SpringApplication application = new SpringApplication(GelatoApplication.class);

		Properties segredos;
		try {
			segredos = readProperties("src/main/resources/secret.properties");
		} catch (IOException | ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)+
				" \tGelatoApplication#main(String[]): \tPropriedades secretas encontradas!");
        application.setDefaultProperties(segredos);
		application.run(args);
	}

	/**
	 * Lê um arquivo de propriedades
	 * @param path O caminho até o arquivo
	 * @return As propriedades isoladas
	 * @throws ParseException Caso uma linha não seja declarada de forma válida
	 */
	private static Properties readProperties(String path) throws IOException, ParseException {
		FileReader reader;
		try {
			reader = new FileReader(path);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("Arquivo de propriedades secretas não foi encontrado");
		}
		BufferedReader buffered = new BufferedReader(reader);

		Properties properties = new Properties();

		String linha;
		int numero_linha = 0;
		while ((linha = buffered.readLine()) != null) {
			numero_linha++;
			if (linha.isBlank()) continue; // linha vazia
			if (linha.startsWith("#")) continue; // comentário

			String[] chave_valor = linha.split("=");
			if (chave_valor.length!=2) throw new ParseException("Chave valor inválido...",numero_linha);

			properties.put(chave_valor[0],chave_valor[1]);
		}
		return properties;
    }
}
