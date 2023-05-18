package com.curso.app;

import java.util.ServiceLoader;

import com.curso.diccionario.SuministradorDeDiccionarios;
import com.curso.diccionario.Diccionario;

public class App {

	public static void main(String[] args) {
		// Validar argumentos
		if(args.length != 2) {
			System.err.println("Idioma y/o palabra a buscar no suministrados");
			System.err.println("Uso: java ... com.curso.app.App <IDIOMA> <PALABRA>");
			System.exit(1);
		}
		final String idioma  = args[0];
		final String palabra = args[1];
		
		// Obtener un Diccionario para el idioma > "idioma"
		Diccionario miDiccionario = null;
		Iterable<SuministradorDeDiccionarios> suministradores = ServiceLoader.load(SuministradorDeDiccionarios.class);
		for(SuministradorDeDiccionarios suministrador:suministradores) {
			if(suministrador.tienesDiccionario(idioma)) {
				miDiccionario = suministrador.getDiccionario(idioma).orElseThrow();
																	// Caso que no GENERA EXCEPTION: BUG Implementación de la librería de diccionarios que se esté usando
				break; 		// Paro de buscar diccionario... Ya tengo uno
			}
		}
		if(miDiccionario == null) {
			System.err.println("No se ha encontrado un diccionario válido para el idioma solicitado.");
			System.err.println("Por favor, pruebe con otro idioma o añada un suministrador de diccionarios que incluya el idioma solicitado.");
			System.exit(2);
		}
		System.out.println("Diccionario cargado");
		// Buscamos la palabra
		if( miDiccionario.existe(palabra) ) {
			System.out.println("La palabra: " + palabra + " existe en el diccionario");
			miDiccionario.getSignificados(palabra).orElseThrow().forEach( significado -> System.out.println("- " + significado));
			// Si no quisiera poner guion: miDiccionario.getSignificados(palabra).orElseThrow().forEach( System.out::println);
		} else {
			System.out.println("La palabra: " + palabra + " NO existe en el diccionario");
			System.out.println("Alternativas:");
			var alternativas = miDiccionario.getAlternativas(palabra);
			alternativas.forEach( alternativa -> System.out.println("- " + alternativa));
		}
	}

}

