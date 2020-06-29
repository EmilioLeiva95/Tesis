package lector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import objetos.tabla;
import objetos.Netbeanstopostgres;
import objetos.columna;
import objetos.tipo;
import objetos.plantilla;

public class lector {
	static List<tabla> tablasGlobal = new ArrayList();
	static List<columna> columnasGlobal = new ArrayList();
	static List<tipo> tipoGlobal = new ArrayList();
	static Netbeanstopostgres con = new Netbeanstopostgres();
	public static void main(String [] arg) {
	      File archivo = null;
	      FileReader fr = null;
	      BufferedReader br = null;
	      con.connect();
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
		        Integer Pfecha = 0;
		        Integer ocultar = 0;
		        Integer predeterminados = 0;
		        tabla tablaAux = new tabla();
		        columna columnaAux = new columna();
		        while((linea=br.readLine())!=null) {
		        	splitlinea = linea.split(" ");
		        	validadorRequeridos(splitlinea, plantillas);
		        	ocultar = validadorOcultos(splitlinea,ocultar);
		        	tablaAux = ingresarOcultos(splitlinea,plantillas,ocultar,tablaAux);
		        	validadorFechaHora(splitlinea, plantillas, Pfecha);
		        	predeterminados = validadorPredeterminados(splitlinea,predeterminados);
		        	tablaAux = ingresarTablaPredeterminado(splitlinea,predeterminados,tablaAux);
		        	columnaAux = ingresarColumnaPredeterminado(splitlinea,predeterminados,columnaAux,tablaAux);
		        	ingresarPredeterminado(splitlinea,predeterminados,plantillas,columnaAux,tablaAux);
		        	validadorPedeterminados(splitlinea, plantillas);
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
	
	private static columna ingresarColumnaPredeterminado(String[] splitlinea, Integer predeterminados, columna columnaAux, tabla tablaAux) {
		if(predeterminados == 1 && splitlinea.length >= 3) {
			if(splitlinea[splitlinea.length-3].contains("<COLUMNA>") && splitlinea[splitlinea.length-1].contains("<COLUMNA>")) {
				if(!splitlinea[splitlinea.length-3].equals("<COLUMNA>") || !splitlinea[splitlinea.length-1].equals("<COLUMNA>")) {
	 				System.out.println("ERROR: 2 SINTAXIS INCORRECTA EN ETIQUETA COLUMNA ");
	 			 }
				for(columna i : columnasGlobal) {
	 				 if(i.getDescripcion().equals(splitlinea[splitlinea.length-2]) && i.getTabla().getDescripcion().equals(tablaAux.getDescripcion())) {
	 					columnaAux = i;
	 				 }
	 			 }
			}
			if(splitlinea[splitlinea.length-2].contains("<COLUMNA>") && splitlinea[splitlinea.length-1].contains("<COLUMNA>")) {
 				System.out.println("ERROR: 3 SINTAXIS INCORRECTA EN ETIQUETA COLUMNA ");
 			 }
			return columnaAux;
		}
		return null;
	}
	
	private static void ingresarPredeterminado(String[] splitlinea, Integer predeterminados, List<plantilla> plantillas, columna columnaAux, tabla tablaAux) {
		if(predeterminados == 1) {
			Integer contador = 0;
			String aux = "";
			plantilla plantillaNueva = new plantilla();
			plantillaNueva.setIdplantilla(plantillas.size());
			for(int i = 0; i < splitlinea.length; i++) {
				if(contador == 1 && !splitlinea[i].equals("<D>")) {
					aux = aux+splitlinea[i]+" ";
				}
				if(contador == 2) {
					plantillaNueva.setAlmacenar(aux);
					contador = contador+1;
					aux = "";
				}
				if(contador == 3) {
					aux = aux+splitlinea[i]+" ";
				}
				if(contador == 3 && splitlinea[splitlinea.length-1].equals("<D>")) {
					plantillaNueva.setMostrar(aux);
					plantillaNueva.setTabla(tablaAux);
					plantillaNueva.setColumna(columnaAux);
					plantillaNueva.setTipo(9);
					plantillas.add(plantillaNueva);
					contador = contador+1;
				}
				if(contador == 4) {
					System.out.println("ERROR: 4 SINTAXIS INCORRECTA EN ETIQUETA D ");
				}
				if(splitlinea[i].equals("<D>") && splitlinea.length <= i+1) {
					contador=contador+1;
					if(splitlinea[i-1].equals("<D>") ) {
						System.out.println("ERROR: 5 SINTAXIS INCORRECTA EN ETIQUETA D ");
					}
				}
			}
		}
	}

	private static tabla ingresarTablaPredeterminado(String[] splitlinea, Integer predeterminados, tabla tablaAux) {
		if(predeterminados == 1) {
			if(splitlinea.length >= 3) {
				if(splitlinea[splitlinea.length-3].contains("<TABLA>") && splitlinea[splitlinea.length-1].contains("<TABLA>")) {
					if(!splitlinea[splitlinea.length-3].equals("<TABLA>") || !splitlinea[splitlinea.length-1].equals("<TABLA>")) {
		 				System.out.println("ERROR: 6 SINTAXIS INCORRECTA EN ETIQUETA TABLA ");
		 			 }  
					for(tabla i : tablasGlobal) {
		 				 if(i.getDescripcion().equals(splitlinea[splitlinea.length-2])) {
		 					tablaAux = i;
		 				 }
		 			 }
		 		}
				if(splitlinea[splitlinea.length-2].contains("<TABLA>") && splitlinea[splitlinea.length-1].contains("<TABLA>")) {
	 				System.out.println("ERROR: 7 SINTAXIS INCORRECTA EN ETIQUETA TABLA ");
	 			 }
			}
		}
		return tablaAux;
	}

	private static Integer validadorPredeterminados(String[] splitlinea, Integer predeterminados) {
		if(splitlinea[0].contains("<PREDETERMINADO>")) {
			predeterminados=predeterminados+1;
			if(splitlinea.length > 1 || !splitlinea[0].equals("<PREDETERMINADO>")) {
 				System.out.println("ERROR: 8 SINTAXIS INCORRECTA EN ETIQUETA PREDETERMINADO");
 			 }
		}
		if(predeterminados == 3) {
			System.out.println("ERROR: 14 INGRESO UN TERCER PREDETERMINADO");
		}
		return predeterminados;
	}

	private static tabla ingresarOcultos(String[] splitlinea, List<plantilla> plantillas, Integer ocultar,tabla tablaAux) {
		if(ocultar == 1) {
			plantilla plantillaNueva = new plantilla();
			plantillaNueva.setIdplantilla(plantillas.size());
			if(splitlinea.length >= 3) {
				if(splitlinea[splitlinea.length-3].contains("<TABLA>") && splitlinea[splitlinea.length-1].contains("<TABLA>")) {
					if(!splitlinea[splitlinea.length-3].equals("<TABLA>") || !splitlinea[splitlinea.length-1].equals("<TABLA>")) {
		 				System.out.println("ERROR: 9 SINTAXIS INCORRECTA EN ETIQUETA TABLA ");
		 			 } 
					for(tabla i : tablasGlobal) {
		 				 if(i.getDescripcion().equals(splitlinea[splitlinea.length-2])) {
		 					tablaAux = i;
		 				 }
		 			 }
		 		}
				if(splitlinea[splitlinea.length-2].contains("<TABLA>") && splitlinea[splitlinea.length-1].contains("<TABLA>")) {
	 				System.out.println("ERROR: 10 SINTAXIS INCORRECTA EN ETIQUETA TABLA ");
	 			 }
				if(splitlinea[splitlinea.length-3].contains("<COLUMNA>") && splitlinea[splitlinea.length-1].contains("<COLUMNA>")) {
					if(!splitlinea[splitlinea.length-3].equals("<COLUMNA>") || !splitlinea[splitlinea.length-1].equals("<COLUMNA>")) {
		 				System.out.println("ERROR: 11 SINTAXIS INCORRECTA EN ETIQUETA COLUMNA ");
		 			 }
					for(columna i : columnasGlobal) {
		 				 if(i.getDescripcion().equals(splitlinea[splitlinea.length-2]) && i.getTabla().getDescripcion().equals(tablaAux.getDescripcion())) {
		 					plantillaNueva.setTabla(tablaAux);
		 					plantillaNueva.setColumna(i);
		 					plantillaNueva.setTipo(6);
		 					plantillas.add(plantillaNueva);
		 				 }
		 			 }
				}
				if(splitlinea[splitlinea.length-2].contains("<COLUMNA>") && splitlinea[splitlinea.length-1].contains("<COLUMNA>")) {
	 				System.out.println("ERROR: 12 SINTAXIS INCORRECTA EN ETIQUETA COLUMNA ");
	 			 }
			}
		}
		return tablaAux;
	}

	private static void validadorPedeterminados(String[] splitlinea, List<plantilla> plantillas) {
		// TODO Auto-generated method stub
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
					if(splitlinea[i].contains("<FECHA>") && splitlinea[i+2].equals("<FECHA>")) {
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
					if(splitlinea[i].contains("<HORA>") && splitlinea[i+2].equals("<HORA>")) {
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
		if(splitlinea[0].contains("<OCULTAR>")) {
 			 ocultar=ocultar+1;
 			 if(splitlinea.length > 1 || !splitlinea[0].equals("<OCULTAR>")) {
 				System.out.println("ERROR: 1 SINTAXIS INCORRECTA EN ETIQUETA OCULTAR");
 			 }
 		}
		if(ocultar == 3) {
			System.out.println("ERROR: 13 INGRESO UN TERCER OCULTAR");
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




