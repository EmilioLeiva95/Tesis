package lector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

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
	  	     
	         // Lectura del fichero
	         String linea;
	         String[] splitlinea = null;
	         while((linea=br.readLine())!=null) {
	        	splitlinea = linea.split(" ");
	         	comparadorTablaColumna(splitlinea,tablas,columnas);
	         	comparadorTipoDato(splitlinea,tablas,columnas,tipo);
	         	comparadorLlavePrimaria(splitlinea,tablas,columnas);
	         	comparadorNulidad(splitlinea,tablas,columnas);
	         	comparadorLlaveForanea(splitlinea,tablas,columnas);
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
		for(int i = 0; i < splitlinea.length; i++) {
     		if(splitlinea[i].contains("ALTER") && splitlinea[i+1].equals("TABLE")) {
     			System.out.println("=============LLAVE FORANEA==========");
     			System.out.println("TABLA REFERENCIA: "+splitlinea[i+2].replaceAll("\"", ""));
     			System.out.println("COLUMNA REFERENCIA: "+splitlinea[i+6].replaceAll("\"", "").replaceAll("\\(", "").replaceAll("\\)", ""));
     			System.out.println("TABLA: "+splitlinea[i+8].replaceAll("\"", ""));
     			System.out.println("COLUMNA: "+splitlinea[i+9].replaceAll("\"", "").replaceAll("\\(", "").replaceAll("\\)", ""));
     			i = splitlinea.length;
     		}
		}
	}

	private static void comparadorNulidad(String[] splitlinea, List<tabla> tablas, List<columna> columnas) {
		for(int i = 0; i < splitlinea.length; i++) {
     		if(splitlinea[i].contains("NULL") && splitlinea[i-1].equals("NOT")) {
     			System.out.println("-NO PUEDE SER NULO-");
     			 i=splitlinea.length;
     		}
		}
	}

	private static void comparadorLlavePrimaria(String[] splitlinea, List<tabla> tablas, List<columna> columnas) {
		for(int i = 0; i < splitlinea.length; i++) {
     		if(splitlinea[i].equals("PRIMARY") && splitlinea[i+1].contains("KEY")) {
     			System.out.println("-LLAVE PRIMARIA-");
     			 i=splitlinea.length;
     		}
		}
	}

	private static void comparadorTablaColumna(String[] splitlinea, List<tabla> tablas, List<columna> columnas) {
		for(int i = 0; i < splitlinea.length; i++) {
			tabla tablaNueva = new tabla();
			columna columnaNueva = new columna();
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
	     			i=splitlinea.length;
//         			if(columnas == null) {
//         				columnaNueva.setIdColumna(1);
//         			}else {
//         				columnaNueva.setIdColumna(columnas.size()+1);
//         			}
//         			columnaNueva.setDescripcion(splitlinea[i]);
//         			columnaNueva.setTabla(tablas.get(tablas.));
     			}	
     		}
		}
	}
		
	private static void comparadorTipoDato(String[] splitlinea, List<tabla> tablas, List<columna> columnas,List<tipo> tipo) {
		String[] tiposDatos= {"INT","LONG","INTEGER","TINYINT","SMALLINT","BIGINT","REAL","DOUBLE","FLOAT",      
								"DECIMAL","NUMERIC","CHAR","VARCHAR","LONGVARCHAR","VAR","DATE","TIME",
								"TIMESTAMP","BOOLEAN","BIT","SERIAL",};
		for(int i = 0; i<splitlinea.length; i++) {
			tipo tipoNuevo = new tipo();
			for(int j = 0; j<tiposDatos.length; j++) {
				if(splitlinea[i].contains(tiposDatos[j])) {
					System.out.println("TIPO:"+tiposDatos[j]);
					if(tipo == null) {
	     				tipoNuevo.setIdTipo(1);
	     			}else {
	     				tipoNuevo.setIdTipo(tipo.size()+1);
	     			}
	     			tipoNuevo.setDescripcion(tiposDatos[j]);
	     			tipo.add(tipoNuevo);
        			 i=splitlinea.length;
        			 j=tiposDatos.length;
				}
			}
        }
		
	}
}
