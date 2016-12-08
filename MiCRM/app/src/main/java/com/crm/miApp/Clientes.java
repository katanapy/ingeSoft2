package com.crm.miApp;

import com.crm.miApp.db.SQLite;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class Clientes extends Fragment {

	private SQLite sqlite;

	@Override
	// se crea el metodo para lanzar la vista del fragento
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.persona, container, false);

		Bundle bundle = this.getArguments();
		if (bundle != null) {
			int i = bundle.getInt("id", 0);

			// si es mayor a cero es porque viene para una edicion
			if (i > 0) {
				// Abre conexion a sqlite
				sqlite = new SQLite(getActivity());
				sqlite.abrir();

				// obtiene registros e imprimir en el listview
				Cursor cursor = sqlite.getPersonasByID(i);

				if (cursor.getCount() > 0) {
					cursor.moveToFirst();

					EditText editTextID = (EditText) view
							.findViewById(R.id.editTextID);
					editTextID.setText("ID = ["
							+ cursor.getString(cursor.getColumnIndex("id"))
							+ "]");

					EditText editTextNombre = (EditText) view
							.findViewById(R.id.editTextNombre);
					editTextNombre.setText(cursor.getString(cursor
							.getColumnIndex("Nombre")));

					EditText editTextTelefono = (EditText) view
							.findViewById(R.id.editTextTelefono);
					editTextTelefono.setText(cursor.getString(cursor
							.getColumnIndex("Telefono")));

					EditText editTextEmail = (EditText) view
							.findViewById(R.id.editTextEmail);
					editTextEmail.setText(cursor.getString(cursor
							.getColumnIndex("Email")));
				}

			}
		}

		return view;
	}
}