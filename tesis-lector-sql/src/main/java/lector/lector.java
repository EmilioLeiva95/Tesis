package lector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import objetos.tabla;
import objetos.columna;
import objetos.tipo;
import objetos.plantilla;



public class lector {
	static List<tabla> tablasGlobal = new ArrayList();
	static List<columna> columnasGlobal = new ArrayList();
	static List<tipo> tipoGlobal = new ArrayList();
	public static void main(String [] arg) {
	      File archivo = null;
	      FileReader fr = null;
	      BufferedReader br = null;
	      lectorScript(archivo,fr,br);
	      lectorPlantilla(archivo,fr,br);
	}

	private static void lectorPlantilla(File archivo, FileReader fr, BufferedReader br) {
		archivo = new File ("./src/main/resources/plantilla/plantilla.aur");
		 try {
				fr = new FileReader (archivo);
		        br = new BufferedReader(fr);
		        List<plantilla> plantillas = new ArrayList();
		        String linea;
		        String[] splitlinea = null;
		        Integer BPredet = 0;
		        Integer Pfecha = 0;
		        Integer ocultar = 0;
		        tabla tablaAux = new tabla();
		        while((linea=br.readLine())!=null) {
		        	splitlinea = linea.split(" ");
		        	validadorRequeridos(splitlinea, plantillas);
		        	ocultar = validadorOcultos(splitlinea,ocultar);
		        	tablaAux = ingresarOcultos(splitlinea,plantillas,ocultar,tablaAux);
		        	Pfecha=validadorFechaHora(splitlinea, plantillas, Pfecha);
		        	BPredet = validadorPredeterminados(splitlinea, plantillas, BPredet);
		        }
		        System.out.println("probando");
		 }catch(Exception e){
	         e.printStackTrace();
	      }finally{
	         try{                    
	            if( null != fr ){   
	               fr.close();     
	            }                  
	         }catch (Exception e2){ 
	            e2.printStackTrace();
	         }
	      }
	}

	private static tabla ingresarOcultos(String[] splitlinea, List<plantilla> plantillas, Integer ocultar,tabla tablaAux) {
		if(ocultar == 1) {
			plantilla plantillaNueva = new plantilla();
			plantillaNueva.setIdplantilla(plantillas.size());
			if(splitlinea.length >= 3) {
				if(splitlinea[splitlinea.length-3].contains("<TABLA>") && splitlinea[splitlinea.length-1].contains("<TABLA>")) {
		 			 for(tabla i : tablasGlobal) {
		 				 if(i.getDescripcion().equals(splitlinea[splitlinea.length-2])) {
		 					tablaAux = i;
		 				 }
		 			 }
		 		}
				if(splitlinea[splitlinea.length-3].contains("<COLUMNA>") && splitlinea[splitlinea.length-1].contains("<COLUMNA>")) {
					for(columna i : columnasGlobal) {
		 				 if(i.getDescripcion().equals(splitlinea[splitlinea.length-2]) && i.getTabla().getDescripcion().equals(tablaAux.getDescripcion())) {
		 					plantillaNueva.setTabla(tablaAux);
		 					plantillaNueva.setColumna(i);
		 					plantillaNueva.setTipo(6);
		 					plantillas.add(plantillaNueva);
		 				 }
		 			 }
				}
			}
		}
		return tablaAux;
	}

	private static Integer validadorPredeterminados(String[] splitlinea, List<plantilla> plantillas, Integer BPredet) {
//		Pattern pattern1 = Pattern.compile("\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b");
//		Matcher m = pattern1.matcher(splitlinea[2]);
		if(splitlinea[0].contains("<BD>") && BPredet == 0) {
			plantilla plantillaNueva = new plantilla();
				for(int i = 0; i < splitlinea.length; i++) {
					if((splitlinea.length==3)&&(splitlinea[i].contains("<BD>") && splitlinea[i+2].equals("<BD>"))) {				
							if(plantillas == null) {
			     				plantillaNueva.setIdplantilla(1);
			     			}else {
			     				plantillaNueva.setIdplantilla(plantillas.size()+1);
			     			}
							plantillaNueva.setTipo(1);
			     			plantillaNueva.setDescripcion(splitlinea[i+1]);
			     			plantillas.add(plantillaNueva);
			     			i=splitlinea.length;
			     			BPredet = 1;				     				
			}else {
				System.out.println("ERROR EN ETIQUETA DE BASE DE DATOS, VERIFIQUE SI LA ETIQUETA DE <BD> ESTA BIEN ESCRITA ESTA BIEN ESCRITA O SI HAY VALOR DE BASE DE DATOS");
				System.exit(0);
			}
		}
		}else {
			if(splitlinea[0].contains("<BD>") && BPredet != 0) {
				System.out.println("ERROR EN ETIQUETA DUPLICADA DE BASE DE DATOS");
				System.exit(0); 
			}
		}
		
		if(splitlinea[0].contains("<CONTRA>") && BPredet == 1) {
			plantilla plantillaNueva = new plantilla();
				for(int i = 0; i < splitlinea.length; i++) {
					if((splitlinea.length==3)&&splitlinea[i].contains("<CONTRA>") && splitlinea[i+2].equals("<CONTRA>")) {				
							if(plantillas == null) {
			     				plantillaNueva.setIdplantilla(1);
			     			}else {
			     				plantillaNueva.setIdplantilla(plantillas.size()+1);
			     			}
							plantillaNueva.setTipo(2);
			     			plantillaNueva.setDescripcion(splitlinea[i+1]);
			     			plantillas.add(plantillaNueva);
			     			i=splitlinea.length;
			     			BPredet = 2;				     				
			}else {
				System.out.println("ERROR EN ETIQUETA DE CONTRASE&tilde;A DE BASE DE DATOS, VERIFIQUE SI LA ETIQUETA DE <CONTRA> ESTA BIEN ESCRITA O SI HAY VALOR DE CONTRASE&tilde;A DE BASE DE DATOS");
				System.exit(0);
			}
		}
		}else {
			if(splitlinea[0].contains("<CONTRA>") && BPredet != 1) {
				System.out.println("ERROR EN ETIQUETA DUPLICADA DE CONTRASE&tilde;A DE BASE DE DATOS");
				System.exit(0); 
			}
		}
		
		if(splitlinea[0].contains("<USUARIO>") && BPredet == 2) {
			plantilla plantillaNueva = new plantilla();
				for(int i = 0; i < splitlinea.length; i++) {
					if(splitlinea[i].contains("<USUARIO>") && splitlinea[i+2].equals("<USUARIO>")) {				
							if(plantillas == null) {
			     				plantillaNueva.setIdplantilla(1);
			     			}else {
			     				plantillaNueva.setIdplantilla(plantillas.size()+1);
			     			}
							plantillaNueva.setTipo(3);
			     			plantillaNueva.setDescripcion(splitlinea[i+1]);
			     			plantillas.add(plantillaNueva);
			     			i=splitlinea.length;
			     			BPredet = 3;				     				
			}else {
				System.out.println("ERROR EN ETIQUETA DE USUARIO DE BASE DE DATOS, VERIFIQUE SI LA ETIQUETA DE <USUARIO> ESTA BIEN ESCRITA O SI HAY VALOR DE USUARIO DE BASE DE DATOS");
				System.exit(0);
			}
		}
		}else {
			if(splitlinea[0].contains("<USUARIO>") && BPredet != 2) {
				System.out.println("ERROR EN ETIQUETA DUPLICADA DE USUARIO DE BASE DE DATOS");
				System.exit(0); 
			}
		}
		
		if(splitlinea[0].contains("<IP>") && BPredet == 3) {
			plantilla plantillaNueva = new plantilla();
				for(int i = 0; i < splitlinea.length; i++) {
					if((splitlinea.length==3)&&splitlinea[i].contains("<IP>") && splitlinea[i+2].equals("<IP>")) {				
							if(plantillas == null) {
			     				plantillaNueva.setIdplantilla(1);
			     			}else {
			     				plantillaNueva.setIdplantilla(plantillas.size()+1);
			     			}
							plantillaNueva.setTipo(4);
			     			plantillaNueva.setDescripcion(splitlinea[i+1]);
			     			plantillas.add(plantillaNueva);
			     			i=splitlinea.length;
			     			BPredet = 4;				     				
			}else {
				System.out.println("ERROR EN ETIQUETA DE IP DE CONEXION DE BASE DE DATOS, VERIFIQUE SI LA ETIQUETA DE <IP> ESTA BIEN ESCRITA O SI HAY VALOR DE IP DE CONEXION DE BASE DE DATOS");
				System.exit(0);
			}
		}
		}else {
			if(splitlinea[0].contains("<IP>") && BPredet != 3) {
				System.out.println("ERROR EN ETIQUETA DUPLICADA DE IP DE CONEXION DE BASE DE DATOS");
				System.exit(0); 
			}
		}
		
		if(splitlinea[0].contains("<PUERTO>") && BPredet == 4) {
			plantilla plantillaNueva = new plantilla();
				for(int i = 0; i < splitlinea.length; i++) {
					if((splitlinea.length==3)&&splitlinea[i].contains("<PUERTO>") && splitlinea[i+2].equals("<PUERTO>")) {				
							if(plantillas == null) {
			     				plantillaNueva.setIdplantilla(1);
			     			}else {
			     				plantillaNueva.setIdplantilla(plantillas.size()+1);
			     			}
							plantillaNueva.setTipo(5);
			     			plantillaNueva.setDescripcion(splitlinea[i+1]);
			     			plantillas.add(plantillaNueva);
			     			i=splitlinea.length;
			     			BPredet = 4;				     				
			}else {
				System.out.println("ERROR EN ETIQUETA DE PUERTO DE CONEXION DE BASE DE DATOS, VERIFIQUE SI LA ETIQUETA DE <PUERRTO> ESTA BIEN ESCRITA O SI HAY VALOR DE PUERTO DE CONEXION DE BASE DE DATOS");
				System.exit(0);
			}
		}
		}else {
			if(splitlinea[0].contains("<PUERTO>") && BPredet != 4) {
				System.out.println("ERROR EN ETIQUETA DUPLICADA DE PUERTO DE CONEXION DE BASE DE DATOS");
				System.exit(0); 
			}
		}
		
		return BPredet;
	}

	private static Integer validadorFechaHora(String[] splitlinea, List<plantilla> plantillas, Integer Pfecha) {
		Integer tipoF=0;
		Integer tipoH=0;
		String[] tiposFecha= {"DD-MM-YYYY","MM-DD-YYYY","DD-MM-YY","MM-DD-YY","DD-MMM-YYYY","MMM-DD-YYYY","DD-MMM-YY",
				"MMM-DD-YY","DD/MM/YYYY","MM/DD/YYYY","DD/MM/YY","MM/DD/YY","DD/MMM/YYYY","MMM/DD/YYYY","DD/MMM/YY","MMM/DD/YY"};
		String[] tiposHora= {"HH:MM","HH:MM:SS","HH:MM_A","HH:MM:SS_A"};
		if(splitlinea[0].contains("<FECHA>") && Pfecha == 0) {
			 
			plantilla plantillaNueva = new plantilla();
				for(int i = 0; i < splitlinea.length; i++) {
					if((splitlinea.length==3)&&splitlinea[i].contains("<FECHA>") && splitlinea[i+2].equals("<FECHA>")) {
						for(int j = 0; j<tiposFecha.length; j++) {
						if(splitlinea[1].contentEquals(tiposFecha[j])) {
							if(plantillas == null) {
			     				plantillaNueva.setIdplantilla(1);
			     			}else {
			     				plantillaNueva.setIdplantilla(plantillas.size()+1);
			     			}
							plantillaNueva.setTipo(7);
			     			plantillaNueva.setDescripcion(splitlinea[i+1]);
			     			plantillas.add(plantillaNueva);
			     			i=splitlinea.length;
			     			Pfecha = 1;	
			     			tipoF=1;
						}
						}
						if(tipoF==0) {
							System.out.println("ERROR EN ETIQUETA DE FECHA, INGRESE UN FORMATO DE FECHA VALIDO");
							break;
						}		
			}else {
				System.out.println("ERROR EN ETIQUETA DE FECHA, VERIFIQUE SI LA ETIQUETA DE SALIDA DE FECHA ESTA BIEN ESCRITA O SI HAY VALOR DE FECHA");
				break;
			}
		}
		}else {
			if(splitlinea[0].contains("<FECHA>") && Pfecha == 1) {
				System.out.println("EL SEGUNDO REGISTRO DE FECHA HA SIDO IGNORADO");	
			}
		}
		if(splitlinea[0].contains("<HORA>") && (Pfecha == 1 || Pfecha == 0)) {
			
	
		if(Pfecha == 0) {
			System.out.println("NO HAY REGISTRO DE FECHA, SE TOMARAN LAS FECHAS POR DEFECTO");
			}
		
			plantilla plantillaNueva = new plantilla();
				for(int i = 0; i < splitlinea.length; i++) {
					if((splitlinea.length==3)&&splitlinea[i].contains("<HORA>") && splitlinea[i+2].equals("<HORA>")) {
						for(int j = 0; j<tiposHora.length; j++) {
							if(splitlinea[1].contentEquals(tiposHora[j])) {
								if(plantillas == null) {
				     				plantillaNueva.setIdplantilla(1);
				     			}else {
				     				plantillaNueva.setIdplantilla(plantillas.size()+1);
				     			}
								plantillaNueva.setTipo(8);
				     			plantillaNueva.setDescripcion(splitlinea[i+1]);
				     			plantillas.add(plantillaNueva);
				     			i=splitlinea.length;
				     			Pfecha = 2;	
				     			tipoH=1;
							}	
							}
						if(tipoH==0) {
							System.out.println("ERROR EN ETIQUETA DE HORA, INGRESE UN FORMATO DE HORA VALIDO");
							break;
						}
			
			}else {
				System.out.println("ERROR EN ETIQUETA DE HORA, VERIFIQUE SI LA ETIQUETA DE SALIDA DE FECHA ESTA BIEN ESCRITA O SI HAY VALOR DE HORA");
				break;
			}
		}
		}else {
			if(splitlinea[0].contains("<HORA>") && Pfecha == 2) {
				System.out.println("EL SEGUNDO REGISTRO DE HORA HA SIDO IGNORADO");		
			}	
		}
		if(splitlinea[0].contains("<PREDETERMINADO>") && Pfecha == 0) {
			System.out.println("NO HAY REGISTRO DE FECHA NI DE HORA, SE TOMARAN LAS HORAS Y FECHAS POR DEFECTO");
		
		}
		if(splitlinea[0].contains("<PREDETERMINADO>") && Pfecha == 1) {
			System.out.println("NO HAY REGISTRO DE HORA, SE TOMARAN LAS HORAS POR DEFECTO");
		Pfecha=10;
		}
		return Pfecha;
	}
	
	private static Integer validadorOcultos(String[] splitlinea, Integer ocultar ) {
		if(splitlinea[0].contains("<OCULTAR>") && (ocultar == 1 || ocultar == 0)) {
 			 ocultar=ocultar+1;
 		}
		return ocultar;
	}
	private static void validadorRequeridos(String[] splitlinea, List<plantilla> plantillas) {
		// TODO Auto-generated method stub
	}

	private static void lectorScript(File archivo, FileReader fr, BufferedReader br) {
		archivo = new File ("./src/main/resources/script/script.sql");
		List<String> ar = new ArrayList();
		List<tabla> tablas = new ArrayList();
		List<columna> columnas = new ArrayList();
		List<tipo> tipo = new ArrayList();
        try {
			fr = new FileReader (archivo);
	        br = new BufferedReader(fr);
	        // Lectura del fichero
	        String linea;
	        String[] splitlinea = null;
	        while((linea=br.readLine())!=null) {
		       	columna columnaNueva = new columna();
		       	splitlinea = linea.split(" ");
	        	comparadorTablaColumna(splitlinea,tablas,columnas,columnaNueva);
	        	comparadorLlavePrimaria(splitlinea,columnaNueva);
	        	comparadorNulidad(splitlinea,columnaNueva);
	        	comparadorTipoDato(splitlinea,tipo,ar,columnaNueva);
	        	comparadorLlaveForanea(splitlinea,columnas);
	        	if(columnaNueva.getIdColumna() != null) {
	        		columnas.add(columnaNueva);
	        	}
	        }
	        tablasGlobal = tablas;
	        columnasGlobal = columnas;
	        tipoGlobal = tipo;
        }catch(Exception e){
	         e.printStackTrace();
	      }finally{
	         try{                    
	            if( null != fr ){   
	               fr.close();     
	            }                  
	         }catch (Exception e2){ 
	            e2.printStackTrace();
	         }
	      }
	}

	private static void comparadorLlaveForanea(String[] splitlinea, List<columna> columnas) {
		columna columnaForaneaAux = new columna();
		columna columnaReferenciaAux = new columna();
		columna columnaForanea = new columna();
		columna columnaReferencia = new columna();
		boolean banForanea = false;
		boolean banReferencia = false;
		String[] nuevaForanea = new String[4];
		for(int i = 0; i < splitlinea.length; i++) {
     		if(splitlinea[i].contains("ALTER") && splitlinea[i+1].equals("TABLE")) {
     			nuevaForanea[0] = splitlinea[i+2].replaceAll("\"", "");
     			nuevaForanea[1] = splitlinea[i+6].replaceAll("\"", "").replaceAll("\\(", "").replaceAll("\\)", "");
     			nuevaForanea[2] = splitlinea[i+8].replaceAll("\"", "");
     			nuevaForanea[3] = splitlinea[i+9].replaceAll("\"", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(";", "");
     			i = splitlinea.length;
     		}
		}
		if(nuevaForanea[0] != null) {
			for(int i = 0; i < columnas.size(); i++) {
				columnaForaneaAux = columnas.get(i);
				columnaReferenciaAux = columnas.get(i);
				if(columnaForaneaAux.getDescripcion().equals(nuevaForanea[3]) && columnaForaneaAux.getTabla().getDescripcion().equals(nuevaForanea[2]) && !banForanea) {
					banForanea = true;
					columnaForanea = columnaForaneaAux;
				}
				if(columnaReferenciaAux.getDescripcion().equals(nuevaForanea[1]) && columnaReferenciaAux.getTabla().getDescripcion().equals(nuevaForanea[0]) && !banReferencia) {
					banReferencia = true;
					columnaReferencia = columnaReferenciaAux;
				}
			}
			columnaForanea.setColumnaReferencia(columnaReferencia);
			columnaForanea.setLlaveForanea(true);
			colocarColumna(columnaForanea,columnas);
		}
	}

	private static void colocarColumna(columna columnaForanea, List<columna> columnas) {
		int contador = columnaForanea.getIdColumna();
		columna columnaAux = new columna();
		for(int i = 0; i < columnas.size(); i++) {
			if(i == contador-1) {
				columnas.remove(i);
				columnas.add(columnaForanea);
			}else {
				if(i > contador-1) {
					columnaAux = columnas.get(contador-1);
					columnas.remove(contador-1);
					columnas.add(columnaAux);
				}
			}
		}
	}

	private static void comparadorNulidad(String[] splitlinea, columna columnaNueva) {
		for(int i = 0; i < splitlinea.length; i++) {
     		if(splitlinea[i].contains("NULL") && splitlinea[i-1].equals("NOT")) {
     			 i=splitlinea.length;
     			columnaNueva.setNuleable(false);
     		}else {
     			columnaNueva.setNuleable(true);
     		}
		}
	}

	private static void comparadorLlavePrimaria(String[] splitlinea, columna columnaNueva) {
		for(int i = 0; i < splitlinea.length; i++) {
     		if(splitlinea[i].equals("PRIMARY") && splitlinea[i+1].contains("KEY")) {
     			columnaNueva.setLlavePrimaria(true);
     			 i=splitlinea.length;
     		}else {
     			columnaNueva.setLlavePrimaria(false);
     		}
		}
	}

	private static void comparadorTablaColumna(String[] splitlinea, List<tabla> tablas, List<columna> columnas, columna columnaNueva) {
		for(int i = 0; i < splitlinea.length; i++) {
			tabla tablaNueva = new tabla();
     		if(splitlinea[i].equals("TABLE") && splitlinea[i-1].equals("CREATE")) {
     			if(tablas == null) {
     				tablaNueva.setIdTabla(1);
     			}else {
     				tablaNueva.setIdTabla(tablas.size()+1);
     			}
     			tablaNueva.setDescripcion(splitlinea[i+1].replaceAll("\"", ""));
     			tablas.add(tablaNueva);
     			i=splitlinea.length;
     		}else {
     			if(splitlinea[i].matches("\"(.*)") && !splitlinea[i-1].equals("TABLE") && !splitlinea[i-2].equals("ALTER") && !splitlinea[i-1].contentEquals("REFERENCES")) {
         			if(columnas == null) {
         				columnaNueva.setIdColumna(1);
         			}else {
         				columnaNueva.setIdColumna(columnas.size()+1);
         			}
         			columnaNueva.setDescripcion(splitlinea[i].replaceAll("\"", ""));
         			columnaNueva.setTabla(tablas.get(tablas.size()-1));
         			i=splitlinea.length;
     			}	
     		}
		}
	}
		
	private static void comparadorTipoDato(String[] splitlinea,List<tipo> tipo,List<String> ar, columna columnaNueva) {
		String[] tiposDatos= {"LONG","INTEGER","SMALLINT","BIGINT","REAL","DOUBLE","FLOAT",      
								"DECIMAL","NUMERIC","DATE","TIMESTAMP","BOOLEAN","BIT","SERIAL",};
		Pattern pattern1 = Pattern.compile("^CHAR.*");
		Pattern pattern2 = Pattern.compile("^VARCHAR");
		Pattern pattern3 = Pattern.compile("VAR(?!CHAR)");
		Pattern pattern4 = Pattern.compile("^INT(?!EGER)");
		Pattern pattern5 = Pattern.compile("^TIME(?!STAMP)");
		boolean ver,ver2,ver3,ver4,ver5,ver6;
		for(int i = 0; i<splitlinea.length; i++) {
			tipo tipoNuevo = new tipo();
			Matcher m = pattern1.matcher(splitlinea[i]);
			Matcher m2 = pattern2.matcher(splitlinea[i]);
			Matcher m3 = pattern3.matcher(splitlinea[i]);
			Matcher m4 = pattern4.matcher(splitlinea[i]);
			Matcher m5 = pattern5.matcher(splitlinea[i]);
			for(int j = 0; j<tiposDatos.length; j++) {
				if(splitlinea[i].contains(tiposDatos[j])) {
					ver=ar.contains(tiposDatos[j]);
					if(ver==false){
						if(tipo == null) {			
							tipoNuevo.setIdTipo(1);
						}else {	     
							tipoNuevo.setIdTipo(tipo.size()+1);
						}
						ar.add(tiposDatos[j]);
						tipoNuevo.setDescripcion(tiposDatos[j]);
						tipo.add(tipoNuevo);
						columnaNueva.setTipo(tipoNuevo);
						i=splitlinea.length;
						j=tiposDatos.length;
					}
				}else{
					if(m.find()) {
						ver2=ar.contains("CHAR");
						if(ver2==false){
							if(tipo == null) {
			     				tipoNuevo.setIdTipo(1);
			     			}else {
			     				tipoNuevo.setIdTipo(tipo.size()+1);
			     			}
							ar.add("CHAR");
			     			tipoNuevo.setDescripcion("CHAR");
			     			tipo.add(tipoNuevo);
			     			columnaNueva.setTipo(tipoNuevo);
						}
					}else{
						if(m2.find()) {
							ver3=ar.contains("VARCHAR");
							if(ver3==false){
								if(tipo == null) {
				     				tipoNuevo.setIdTipo(1);
				     			}else {
				     				tipoNuevo.setIdTipo(tipo.size()+1);
				     			}
								ar.add("VARCHAR");
				     			tipoNuevo.setDescripcion("VARCHAR");
				     			tipo.add(tipoNuevo);
				     			columnaNueva.setTipo(tipoNuevo);
				     		}
						}else{
							if(m3.find()) {
								ver4=ar.contains("VAR");
								if(ver4==false){
									if(tipo == null) {
					     				tipoNuevo.setIdTipo(1);
					     			}else {
					     				tipoNuevo.setIdTipo(tipo.size()+1);
					     			}
									ar.add("VAR");
					     			tipoNuevo.setDescripcion("VAR");
					     			tipo.add(tipoNuevo);
					     			columnaNueva.setTipo(tipoNuevo);
				     			}
							}else {
								if(m4.find()) {
									ver5=ar.contains("INT");
									if(ver5==false){
										if(tipo == null) {
						     				tipoNuevo.setIdTipo(1);
						     			}else {
						     				tipoNuevo.setIdTipo(tipo.size()+1);
						     			}
										ar.add("INT");
						     			tipoNuevo.setDescripcion("INT");
						     			tipo.add(tipoNuevo);
						     			columnaNueva.setTipo(tipoNuevo);
						     		}
								}else {
									if(m5.find()) {
										ver6=ar.contains("TIME");
										if(ver6==false){
											if(tipo == null) {
							     				tipoNuevo.setIdTipo(1);
							     			}else {
							     				tipoNuevo.setIdTipo(tipo.size()+1);
							     			}
											ar.add("TIME");
							     			tipoNuevo.setDescripcion("TIME");
							     			tipo.add(tipoNuevo);
							     			columnaNueva.setTipo(tipoNuevo);
										}
									}else {
										for(int k=0;k<ar.size();k++) {
											String aux=ar.get(k);
											if(splitlinea[i].equals(aux)) {
												tipo tipoAux = new tipo();
												tipoAux.setIdTipo(k);
												tipoAux.setDescripcion(aux);
												columnaNueva.setTipo(tipoAux);
											}
										}
									}
								}
							}
						}
					}				
				}	
			}
		}   
	}
}

