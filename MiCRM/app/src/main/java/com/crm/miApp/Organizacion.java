package com.crm.miApp;


import com.crm.miApp.db.SQLite;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class Organizacion extends Fragment {
    
	private SQLite sqlite;
	
	@Override
    //se crea el metodo para lanzar la vista del fragento
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.organizacion, container, false);
        
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int id = bundle.getInt("id", 0);
        
            //si es mayor a cero es porque viene para una edicion
            if( id>0 ){
            	// Abre conexion a sqlite
        		sqlite = new SQLite(getActivity());
        		sqlite.abrir();
        		
				// obtiene registros e imprimir en el listview
				Cursor cursor = sqlite.getOrganizacionByID(id);

				if (cursor.getCount() > 0) {
					cursor.moveToFirst();

					EditText editTextIDOrg = (EditText) view
							.findViewById(R.id.editTextIDOrg);
					editTextIDOrg.setText("ID = ["
							+ cursor.getString(cursor.getColumnIndex("id"))
							+ "]");

					EditText editTextNombreOrg = (EditText) view
							.findViewById(R.id.editTextNombreOrg);
					editTextNombreOrg.setText(cursor.getString(cursor
							.getColumnIndex("Nombre")));

					EditText editTextTelefonoOrg = (EditText) view
							.findViewById(R.id.editTextTelefonoOrg);
					editTextTelefonoOrg.setText(cursor.getString(cursor
							.getColumnIndex("Telefono")));

					EditText editTextDireccionOrg = (EditText) view
							.findViewById(R.id.editTextDireccionOrg);
					editTextDireccionOrg.setText(cursor.getString(cursor
							.getColumnIndex("Direccion")));
				}
            }
         }
        
        return view;
    }
}