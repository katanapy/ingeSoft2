package com.crm.micrm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDSQLite extends SQLiteOpenHelper {

	//nombre de la base de datos
	private static final String DATABASE = "BDMiCRM";
	
	//versión de la base de datos
	private static final int VERSION = 3;
	
	//nombre tabla y campos de tabla
	public final String tablaPersonas = "Personas";
	public final String idPersonas = "id";
	public final String nombrePersonas = "Nombre";
	public final String telefonoPersonas = "Telefono";
	public final String emailPersonas = "Email";
	
	public final String tablaOrganizacion = "Organizacion";
	public final String idOrganizacion = "id";
	public final String nombreOrganizacion = "Nombre";
	public final String direccionOrganizacion = "Direccion";
	public final String telefonoOrganizacion = "Telefono";
	
	//Instrucción SQL para crear las tablas	
	private final String sqlPersonas = 
			"CREATE TABLE Personas ( " +
			"id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + 
	        "Nombre TEXT NULL, " + 
	        "Telefono INTEGER NULL, "+ 
	        "Email TEXT NULL )";

	private final String sqlOrganizacion = 
			"CREATE TABLE Organizacion ( " +
			"id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + 
	        "Nombre TEXT NULL, " + 
	        "Direccion TEXT NULL, "+ 
	        "Telefono INTEGER NULL )";
	
	/**
	 * Constructor de clase
	 * */
	public BDSQLite(Context context) {		
		super( context, DATABASE, null, VERSION );		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {		
		 db.execSQL( sqlPersonas );		 
		 db.execSQL( sqlOrganizacion );
	}

	@Override
	public void onUpgrade( SQLiteDatabase db,  int oldVersion, int newVersion ) {		
		if ( newVersion > oldVersion )
		{
			//elimina tabla
			db.execSQL( "DROP TABLE IF EXISTS " + tablaPersonas );
			db.execSQL( "DROP TABLE IF EXISTS " + tablaOrganizacion );
			
			//y luego creamos la nueva tabla
			db.execSQL( sqlPersonas );
			db.execSQL( sqlOrganizacion );
		}
	}
	
	public String getDatabaseName(){
		return DATABASE;
	}
	
}
