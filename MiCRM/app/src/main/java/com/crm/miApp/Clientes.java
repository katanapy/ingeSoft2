package com.crm.miApp;

import com.crm.miApp.db.SQLite;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Clientes extends Fragment {

	private SQLite sqlite;
	protected Spinner productoSpin;
	protected Spinner cantidad;
	protected TextView totalped;
	int cantida;
	int stockini;
	protected DecimalFormatSymbols dfs;
	protected DecimalFormat formateador;

	public List<Producto> productoList = new ArrayList<Producto>();
	public int posicionSeleccionada;
	public Producto productoSelecionado = new Producto();
	public List<Integer> cant = new ArrayList<Integer>();

	@Override
	// se crea el metodo para lanzar la vista del fragento
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.persona, container, false);
		dfs = new DecimalFormatSymbols(Locale.getDefault());
		dfs.setGroupingSeparator('.');
		formateador = new DecimalFormat("###,###", dfs);

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
					editTextID.setVisibility(View.INVISIBLE);

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

					cargarProductos();

					//Inicializa el spinner a partir del spinner definido en la vista
					productoSpin = (Spinner) view.findViewById(R.id.comboproductos);
					totalped = (TextView) view.findViewById(R.id.totalpedido);
					cantidad = (Spinner) view.findViewById(R.id.combocantidad);

					ArrayAdapter<Producto> dataAdapter = new ArrayAdapter<Producto>(getActivity(), android.R.layout.simple_spinner_item, productoList);
					dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

					productoSpin.setAdapter(dataAdapter);
					productoSpin.setOnItemSelectedListener(

							new AdapterView.OnItemSelectedListener() {
								public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

									String item = parent.getItemAtPosition(position).toString();
									cant.clear();

									// Muestra el elemento seleccionado y pone los datos de contacto en la pantalla
									if (item != null) {
										Toast.makeText(parent.getContext(), "Seleccionó: " +
												item, Toast.LENGTH_SHORT).show();
									}else{
										Toast.makeText(parent.getContext(), "Seleccione un producto", Toast.LENGTH_SHORT).show();
									}
									//preciounitario.setText("Gs. " + formateador.format(productoList.get(position).getPrecioActual()));
									productoSelecionado = new Producto();
									productoSelecionado.setCantidad(productoList.get(position).getCantidad());
									productoSelecionado.setId(productoList.get(position).getId());
									productoSelecionado.setFechaVencimiento(productoList.get(position).getFechaVencimientol());
									productoSelecionado.setNombre(productoList.get(position).getNombre());
									productoSelecionado.setPrecioActual(productoList.get(position).getPrecioActual());
									productoSelecionado.setUnidadMedida(productoList.get(position).getUnidadMedida());
									posicionSeleccionada = position;
									stockini = productoSelecionado.getCantidad();
									if (stockini == 0){
										cant.add(0);
									}
									else {
										for (int i = 1; i <= productoList.get(position).getCantidad(); i++) {
											cant.add(i);
										}
									}
									ArrayAdapter <Integer> adapter = new ArrayAdapter<Integer>(getActivity(),
											android.R.layout.simple_spinner_item, cant);
									adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
									cantidad.setAdapter(adapter);
									cantidad.setOnItemSelectedListener(
											new AdapterView.OnItemSelectedListener() {
												public void onItemSelected(
														AdapterView<?> parent, View view, int position, long id) {
													cantida = cant.get(position);
													if (cantidad.getSelectedItem() == null){
														totalped.setText("Gs. " +
																formateador.format((productoSelecionado.getPrecioActual()
																		* 0)));
													}
													else{
														productoSelecionado.setCantidad(cantida);
														totalped.setText("Gs. " +
																formateador.format((productoSelecionado.getPrecioActual()
																		* cantida)));
													}
												}

												public void onNothingSelected(AdapterView<?> parent) {

												}
											});
								}

								public void onNothingSelected(AdapterView<?> parent) {

								}
							});


				}

			}
		}

		return view;
	}

	/**
	 * Carga los productos existentes en una lista para su posterior uso
	 */
	public void cargarProductos(){
		//Recupera los datos de la bd y pone el cursor en la primera fila de resultados
		Cursor c = sqlite.consultarRegistrosProductos();
		c.moveToFirst();
		Producto aux = new Producto();
		aux.setId(0);
		productoList.add(aux);
		//Guarda los productos en una lista
		for (int i = 1; i <= c.getCount(); i++){
			Producto producto = new Producto();
			producto.setId(c.getInt(c.getColumnIndex("id_producto")));
			producto.setNombre(c.getString(c.getColumnIndex("nombre_prod")));
			producto.setFechaVencimiento(c.getString(c.getColumnIndex("fecha_venc")));
			producto.setPrecioActual(c.getInt(c.getColumnIndex("precio_actual")));
			producto.setUnidadMedida(c.getString(c.getColumnIndex("unidad_medida")));
			producto.setCantidad(c.getInt(c.getColumnIndex("cantidad")));
			productoList.add(i, producto);
			c.moveToNext();//salta a la siguiente fila de resultados
		}
		c.close();
	}
}