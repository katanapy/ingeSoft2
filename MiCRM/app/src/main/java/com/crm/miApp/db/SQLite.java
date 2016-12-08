package com.crm.miApp.db;

//
import java.util.ArrayList;
import android.annotation.SuppressLint;
//
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLite {

	private static BDSQLite BDsqlite;
	private SQLiteDatabase db;

	/** Constructor de clase */
	public SQLite(Context context) {
		BDsqlite = new BDSQLite(context);
	}

	/** Abre conexion a base de datos */
	@SuppressLint("NewApi")
	public void abrir() {
		Log.i("SQLite",
				"Se abre conexion a la base de datos "
						+ BDsqlite.getDatabaseName());
		db = BDsqlite.getWritableDatabase();
	}

	/** Cierra conexion a la base de datos */
	@SuppressLint("NewApi")
	public void cerrar() {
		Log.i("SQLite",
				"Se cierra conexion a la base de datos "
						+ BDsqlite.getDatabaseName());
		BDsqlite.close();
	}

	public boolean agregarRegistroPersonas(String nombre, int telefono,
			String email) {
		if (nombre.length() > 0) {
			ContentValues Values = new ContentValues();
			Values.put(BDsqlite.nombrePersonas, nombre);
			Values.put(BDsqlite.telefonoPersonas, telefono);
			Values.put(BDsqlite.emailPersonas, email);
			Log.i("SQLite", "Nuevo registro ");
			return (db.insert(BDsqlite.tablaPersonas, null, Values) != -1) ? true
					: false;
		} else {
			return false;
		}
	}

	public boolean modificarRegistroPersonas(String idPersona, String nombre,
			int telefono, String email) {
		if (nombre.length() > 0) {
			ContentValues Values = new ContentValues();
			Values.put(BDsqlite.nombrePersonas, nombre);
			Values.put(BDsqlite.telefonoPersonas, telefono);
			Values.put(BDsqlite.emailPersonas, email);
			Log.i("SQLite", "Modificando registro [" + idPersona + "]");
			return (db.update(BDsqlite.tablaPersonas, Values,
					"id=" + idPersona, null) != -1) ? true : false;
		} else {
			return false;
		}
	}

	public boolean eliminarRegistroPersonas(String idPersona) {

		Log.i("SQLite", "Eliminando registro [" + idPersona + "]");
		return (db.delete(BDsqlite.tablaPersonas, "id=" + idPersona, null) != -1) ? true
				: false;
	}

	public Cursor getPersonasByID(int id) {
		return db.query(BDsqlite.tablaPersonas, new String[] {
				BDsqlite.idPersonas, BDsqlite.nombrePersonas,
				BDsqlite.telefonoPersonas, BDsqlite.emailPersonas },
				BDsqlite.idPersonas + " =? ",
				new String[] { String.valueOf(id) }, null, null, null);
	}

	public Cursor consultarRegistrosPersonas() {
		return db.query(BDsqlite.tablaPersonas, new String[] {
				BDsqlite.idPersonas, BDsqlite.nombrePersonas,
				BDsqlite.telefonoPersonas, BDsqlite.emailPersonas }, null,
				null, null, null, null);
	}

	/* metodos para tabla Organizacion */

	public boolean agregarRegistroOrganizacion(String nombre, int telefono,
			String direccion) {
		if (nombre.length() > 0) {
			ContentValues Values = new ContentValues();
			Values.put(BDsqlite.nombreOrganizacion, nombre);
			Values.put(BDsqlite.telefonoOrganizacion, telefono);
			Values.put(BDsqlite.direccionOrganizacion, direccion);
			Log.i("SQLite", "Nuevo registro ");
			return (db.insert(BDsqlite.tablaOrganizacion, null, Values) != -1) ? true
					: false;
		} else {
			return false;
		}
	}

	public boolean modificarRegistroOrganizacion(String idOrganizacion,
			String nombre, int telefono, String direccion) {
		if (nombre.length() > 0) {
			ContentValues Values = new ContentValues();
			Values.put(BDsqlite.nombreOrganizacion, nombre);
			Values.put(BDsqlite.telefonoOrganizacion, telefono);
			Values.put(BDsqlite.direccionOrganizacion, direccion);
			Log.i("SQLite", "Modificando registro [" + idOrganizacion + "]");
			return (db.update(BDsqlite.tablaOrganizacion, Values, "id="
					+ idOrganizacion, null) != -1) ? true : false;
		} else {
			return false;
		}
	}

	public boolean eliminarRegistroOrganizacion(String idOrganizacion) {

		Log.i("SQLite", "Eliminando registro [" + idOrganizacion + "]");
		return (db.delete(BDsqlite.tablaOrganizacion, "id=" + idOrganizacion,
				null) != -1) ? true : false;
	}

	public Cursor getOrganizacionByID(int id) {
		return db.query(BDsqlite.tablaOrganizacion,
				new String[] { BDsqlite.idOrganizacion,
						BDsqlite.nombreOrganizacion,
						BDsqlite.telefonoOrganizacion,
						BDsqlite.direccionOrganizacion },
				BDsqlite.idOrganizacion + " =? ",
				new String[] { String.valueOf(id) }, null, null, null);
	}

	public Cursor consultarRegistrosOrganizacion() {
		return db.query(BDsqlite.tablaOrganizacion,
				new String[] { BDsqlite.idPersonas,
						BDsqlite.nombreOrganizacion,
						BDsqlite.direccionOrganizacion,
						BDsqlite.telefonoOrganizacion }, null, null, null,
				null, null);
	}

	/**
	 * Dado un Cursor con los registros de la base de datos, da formato y
	 * retorna resultado
	 * 
	 * @return ArrayList<String>
	 * */
	public ArrayList<String> getFormatList(Cursor cursor) {
		ArrayList<String> listaDatos = new ArrayList<String>();
		String item = "";
		if (cursor.moveToFirst()) {
			do {
				item += "ID: [" + cursor.getInt(0) + "]\r\n";
				item += "Nombre: " + cursor.getString(1) + "\r\n";
				item += "Telefono: " + cursor.getString(2) + "\r\n";
				item += "Email: " + cursor.getString(3) + "";
				listaDatos.add(item);
				item = "";

			} while (cursor.moveToNext());
		}
		return listaDatos;
	}

}
