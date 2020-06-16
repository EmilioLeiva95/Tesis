package lector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import objetos.tabla;
import objetos.columna;

public class lector {
	public static void main(String [] arg) {
	      File archivo = null;
	      FileReader fr = null;
	      BufferedReader br = null;

	      try {
	         archivo = new File ("./src/main/resources/script/script.sql");
	         fr = new FileReader (archivo);
	         br = new BufferedReader(fr);
	  	     List<tabla> tablas;
	  	     List<columna> columnas;
	         // Lectura del fichero
	         String linea;
	         String[] splitlinea = null;
	         while((linea=br.readLine())!=null) {
	        	splitlinea = linea.split(" ");
	         	comparadorTablaColumna(splitlinea);
	         	comparadorTipoDato(splitlinea);
	         }
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

	private static void comparadorTablaColumna(String[] splitlinea) {
		for(int i = 0; i < splitlinea.length; i++) {
     		if(splitlinea[i].equals("TABLE") && splitlinea[i-1].equals("CREATE")) {
     			 System.out.println(splitlinea[i+1]);
     			 i=splitlinea.length;
     		}else {
     			if(splitlinea[i].matches("\"(.*)") && !splitlinea[i-1].equals("TABLE") && !splitlinea[i-2].equals("ALTER") && !splitlinea[i-1].contentEquals("REFERENCES")) {
         			 System.out.println("  "+splitlinea[i]);
         			 i=splitlinea.length;
     			}	
     		}
		}
	}
		
	private static void comparadorTipoDato(String[] splitlinea) {
		String[] tiposDatos= {"INT","LONG","INTEGER","TINYINT","SMALLINT","BIGINT","REAL","DOUBLE","FLOAT",      
								"DECIMAL","NUMERIC","CHAR","VARCHAR","LONGVARCHAR","DATE","TIME",
								"TIMESTAMP","BOOLEAN","BIT","SERIAL","int","long","integer","tinyint","smallint","bigint",      
								"decimal","numeric","char","varchar","longvarchar","date","time",
								"timestamp","boolean","bit","real","double","float","serial"};
		for(int i = 0; i<splitlinea.length; i++) {
			for(int j = 0; j<tiposDatos.length; j++) {
				if(splitlinea[i].contains(tiposDatos[j])) {
					System.out.println("tipo de dato:"+tiposDatos[j]);
        			 i=splitlinea.length;
        			 j=tiposDatos.length;
				}
			}
        }
		
	}
}
