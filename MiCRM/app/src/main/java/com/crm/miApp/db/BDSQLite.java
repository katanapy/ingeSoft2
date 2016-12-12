package com.crm.miApp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDSQLite extends SQLiteOpenHelper {

	//nombre de la base de datos
	private static final String DATABASE = "BDMiCRM";
	
	//version de la base de datos
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
	
	//Instruccin SQL para crear las tablas
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

		db.execSQL("CREATE TABLE IF NOT EXISTS ventas (id_venta INTEGER PRIMARY KEY," +
				"id_cliente INTEGER, fecha TEXT, monto_total INTEGER," +
				"FOREIGN KEY (id_cliente) REFERENCES Personas(id));");

		db.execSQL("CREATE TABLE IF NOT EXISTS detalle_ventas (id_venta INTEGER, " +
				"id_producto INTEGER, cantidad INTEGER, precio INTEGER);");

		db.execSQL("CREATE TABLE IF NOT EXISTS producto (id_producto INTEGER PRIMARY KEY," +
				"nombre_prod TEXT, fecha_venc TEXT, precio_actual INTEGER, unidad_medida TEXT," +
				"cantidad INTEGER);");

		Cursor cuenta = db.rawQuery("SELECT count(*) from Personas", null);
		cuenta.moveToFirst();
		int c = cuenta.getInt(cuenta.getColumnIndex("count(*)"));
		if (c == 0) {
			ContentValues valores = new ContentValues();
			valores.put("id", "1");
			valores.put("Nombre", "Juan Pérez");
			valores.put("Telefono", "0981 123 456");
			valores.put("Email", "juan@perez.com");
			db.insert("Personas", null, valores);
			valores = new ContentValues();
			valores.put("id", "2");
			valores.put("Nombre", "La Empresa S.A.");
			valores.put("Telefono", "021 1 800 00");
			valores.put("Email", "l@empre.sa");
			db.insert("Personas", null, valores);
			valores = new ContentValues();
			valores.put("id_producto", "1");
			valores.put("nombre_prod", "Coca Pepsi");
			valores.put("fecha_venc", "31/12/2016");
			valores.put("precio_actual", "7500");
			valores.put("unidad_medida", "litros (lts)");
			valores.put("cantidad", "10");
			db.insert("producto", null, valores);
			valores = new ContentValues();
			valores.put("id_producto", "2");
			valores.put("nombre_prod", "Lomito completo");
			valores.put("fecha_venc", "01/09/2018");
			valores.put("precio_actual", "15000");
			valores.put("unidad_medida", "gramos (grs)");
			valores.put("cantidad", "50");
			db.insert("producto", null, valores);
		}

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
