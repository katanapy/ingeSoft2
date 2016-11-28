package com.crm.micrm;

import java.util.ArrayList;

import com.crm.micrm.db.SQLite;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.FragmentManager;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class RegistrosPersonas extends Fragment implements OnItemClickListener {

	//
	private ListView listView;
	private ArrayAdapter<String> adaptador;
	private SQLite sqlite;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.activity_registros_org, container,
				false);

		listView = (ListView) view.findViewById(R.id.lstRegistros);

		// Abre conexion a sqlite
		sqlite = new SQLite(getActivity());
		sqlite.abrir();

		// obtiene registros e imprimir en el listview
		Cursor cursor = sqlite.consultarRegistrosPersonas();
		ArrayList<String> ListaDatos = sqlite.getFormatList(cursor);
		adaptador = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, ListaDatos);
		listView.setAdapter(adaptador);
		listView.setOnItemClickListener(this);

		if (ListaDatos.size() == 0) {
			Toast.makeText(getActivity().getBaseContext(),
					"No existen registros", Toast.LENGTH_SHORT).show();
		}
		// cerrar cursor
		cursor.close();

		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//
		Object object = listView.getItemAtPosition(position);
		// Se extrae el ID = [X]
		int posicionInicial = object.toString().indexOf("[") + 1;
		int posicionFinal = object.toString().indexOf("]", posicionInicial);
		String resultado = object.toString().substring(posicionInicial,
				posicionFinal);

		//prepara el ID para enviarselo como extra al fragmento
		Bundle bundle = new Bundle();
		bundle.putInt("id", Integer.valueOf(resultado) );
		
		// Retorno a la vista de Personas
		Fragment fragment = new Persona();
		fragment.setArguments(bundle);
		
		ActionBar actionBar = ((ActionBarActivity) getActivity())
				.getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("Personas");

		FragmentManager fragmentManager = getActivity()
				.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.addToBackStack(null);
		fragmentManager.beginTransaction().replace(R.id.container, fragment)
				.commit();

	}

}
