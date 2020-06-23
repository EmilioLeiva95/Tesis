package lector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import objetos.tabla;
import objetos.columna;
import objetos.tipo;

public class lector {
	public static void main(String [] arg) {
	      File archivo = null;
	      FileReader fr = null;
	      BufferedReader br = null;

	      
	      try {
	         archivo = new File ("./src/main/resources/script/script.sql");
	         fr = new FileReader (archivo);
	         br = new BufferedReader(fr);
	  	     List<tabla> tablas = new ArrayList();
	  	     List<columna> columnas = new ArrayList();
	  	     List<tipo> tipo = new ArrayList();
	  	     List<String> ar = new ArrayList();
	  	     
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
	         	comparadorLlaveForanea(splitlinea,tablas,columnas);
	         	if(columnaNueva.getIdColumna() != null) {
	         		columnas.add(columnaNueva);
	         	}
	         }
	         System.out.println("=============FIN PROCESO==========");
	      }
	      catch(Exception e){
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

	private static void comparadorLlaveForanea(String[] splitlinea, List<tabla> tablas, List<columna> columnas) {
		columna columnaForaneaAux = new columna();
		columna columnaReferenciaAux = new columna();
		columna columnaForanea = new columna();
		columna columnaReferencia = new columna();
		boolean banForanea = false;
		boolean banReferencia = false;
		String[] nuevaForanea = new String[4];
		for(int i = 0; i < splitlinea.length; i++) {
     		if(splitlinea[i].contains("ALTER") && splitlinea[i+1].equals("TABLE")) {
     			System.out.println("=============LLAVE FORANEA==========");
     			System.out.println("TABLA REFERENCIA: "+splitlinea[i+2].replaceAll("\"", ""));
     			nuevaForanea[0] = splitlinea[i+2].replaceAll("\"", "");
     			System.out.println("COLUMNA REFERENCIA: "+splitlinea[i+6].replaceAll("\"", "").replaceAll("\\(", "").replaceAll("\\)", ""));
     			nuevaForanea[1] = splitlinea[i+6].replaceAll("\"", "").replaceAll("\\(", "").replaceAll("\\)", "");
     			System.out.println("TABLA: "+splitlinea[i+8].replaceAll("\"", ""));
     			nuevaForanea[2] = splitlinea[i+8].replaceAll("\"", "");
     			System.out.println("COLUMNA: "+splitlinea[i+9].replaceAll("\"", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(";", ""));
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
     			System.out.println("-NO PUEDE SER NULO-");
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
     			System.out.println("-LLAVE PRIMARIA-");
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
     			System.out.println("========================================="); 
     			System.out.println("TABLA:"+splitlinea[i+1]);
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
	 				System.out.println("----------------------------------------"); 
	 				System.out.println("COLUMNA:"+splitlinea[i]);
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
					System.out.println("TIPO:"+tiposDatos[j]);
					
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
        			 j=tiposDatos.length;}
				}else {
					
					if(m.find()) {
						System.out.println("TIPO:CHAR");
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
							System.out.println("TIPO:VARCHAR");
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
			     			columnaNueva.setTipo(tipoNuevo);}
						}else{
							if(m3.find()) {
								System.out.println("TIPO:VAR");
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
									System.out.println("TIPO:INT");
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
					     			columnaNueva.setTipo(tipoNuevo);}
								}else {
									if(m5.find()) {
										System.out.println("TIPO:TIME");
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
						     			columnaNueva.setTipo(tipoNuevo);}
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

