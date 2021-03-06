package com.crm.miApp;

import com.crm.miApp.db.SQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("deprecation")
@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity implements
		Cajon.NavigationDrawerCallbacks {

	private Cajon mNavigationDrawerFragment;
	private CharSequence mTitle;
	// conexion a la base de datos
	private SQLite sqlite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// crea la base de datos y abre la conexion
		sqlite = new SQLite(this);

		mNavigationDrawerFragment = (Cajon) getSupportFragmentManager()
				.findFragmentById(R.id.navegacionCajon);
		mTitle = getTitle();
		mNavigationDrawerFragment.setUp(R.id.navegacionCajon,
				(DrawerLayout) findViewById(R.id.cajon_layout));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Inflate the menu; this adds items to the action bar if it is
			// present.
			getMenuInflater().inflate(R.menu.main, menu);
			// restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if (id == R.id.item4) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private static final String ARG_SECTION_NUMBER = "numero_seccion";

		public static PlaceholderFragment newInstance(int numeroSeccion) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, numeroSeccion);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			// TextView textView = (TextView)
			// rootView.findViewById(R.id.seccion);
			// textView.setText("Este es el contenido de la Seccion "+Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			// TODO Auto-generated method stub
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}

	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		Fragment fragment;
		FragmentManager fragmentManager = getSupportFragmentManager();
		// fragmentManager.beginTransaction().replace(R.id.container,
		// PlaceholderFragment.newInstance(position+1)).commit();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);

		switch (position) {
		default:
		case 0:
			fragment = new Home();
			actionBar.setTitle("Home");
			break;
		case 1:
			fragment = new RegistrosClientes();
			actionBar.setTitle("Seleccione un Cliente");
			break;
		case 2:
			fragment = new Organizacion();
			actionBar.setTitle("Pedidos");
			break;
		}
		fragmentManager.beginTransaction().replace(R.id.container, fragment)
				.commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.section1);
			break;
		case 2:
			mTitle = getString(R.string.section2);
			break;
		case 3:
			mTitle = getString(R.string.section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	/*** Metodos para Personas ***/

	public void guardarPersona(View view) {
		Toast mensaje = Toast.makeText(this, "Pedido guardado con Exito", Toast.LENGTH_SHORT);
		mensaje.show();
		//guardarProducto();
		//aceptarPersona();
	}

	private void guardarProducto() {
		/**
		 * EN productosPedido: la cantidad del mismo en getcantidad es la cantidad pedida,
		 * el precioactual es el precio unitario, y el total debe calcularse antes de insertar
		 */
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date fecha = new Date();
			int maxventa;
			String sentencia;
			Cursor maxven = sqlite.obtenerUltimoRegistroVentas();
			maxven.moveToFirst();
			maxventa = maxven.getInt(maxven.getColumnIndex("max(id_venta)")) + 1;
			ContentValues valores = new ContentValues();
			valores.put("id_venta", maxventa);

			// obtiene el ID del cliente si es que el registro ya existe y estamos editando
			EditText tID = (EditText) findViewById(R.id.editTextID);
			String idClienteSeleccionado = tID.getText().toString();

			valores.put("id_cliente", idClienteSeleccionado);
			valores.put("fecha", sdf.format(fecha));

			//obtiene el total del pedido
			TextView totalped = (TextView) findViewById(R.id.totalpedido);

			valores.put("monto_total", totalped.getText().toString());

			/*db.insert("ventas", null, valores);
			for (int i = 0; i < carga_productos.productosPedido.size(); i++) {
				Producto prodActual = carga_productos.productosPedido.get(i);
				valores = new ContentValues();
				valores.put("id_venta", maxventa);
				valores.put("id_producto", prodActual.getId());
				valores.put("cantidad", prodActual.getCantidad());
				valores.put("precio", (prodActual.getCantidad() * prodActual.getPrecioActual()));
				db.insert("detalle_ventas", null, valores);
				sentencia = "UPDATE producto SET cantidad = cantidad - " + prodActual.getCantidad() +
						" WHERE id_producto = " + prodActual.getId();
				db.execSQL(sentencia);
			}*/
		}
		catch (Exception e){

		}
	}


	public void aceptarPersona() {

		// obtiene los valores ingresados
		EditText tNombre = (EditText) findViewById(R.id.editTextNombre);
		EditText tTelefono = (EditText) findViewById(R.id.editTextTelefono);
		EditText tEmail = (EditText) findViewById(R.id.editTextEmail);

		String nombre = tNombre.getText().toString();
		int telefono = "".equalsIgnoreCase(tTelefono.getText().toString()) ? 0
				: Integer.parseInt(tTelefono.getText().toString());
		String email = tEmail.getText().toString();

		// obtiene el ID si es que el registro ya existe y estamos editando
		EditText tID = (EditText) findViewById(R.id.editTextID);
		String idPersonaEditada = tID.getText().toString();

		// Registra en la base de datos
		sqlite.abrir();

		boolean respuestaExitosa = false;

		if (!"".equalsIgnoreCase(idPersonaEditada)) {
			// si el campo ID esta cargado, llamar a Editar
			respuestaExitosa = sqlite.modificarRegistroPersonas(
					idPersonaEditada.substring(
							idPersonaEditada.indexOf("[") + 1,
							idPersonaEditada.indexOf("]")), nombre, telefono,
					email);
		} else {
			// sino llamar a Guardar
			respuestaExitosa = sqlite.agregarRegistroPersonas(nombre, telefono,
					email);
		}

		if (respuestaExitosa) {
			Toast mensaje = Toast.makeText(this, "Clientes guardada con Exito",
					Toast.LENGTH_SHORT);
			mensaje.show();

			// limpia los campos para siguiente carga
			tID.setText("");
			tNombre.setText("");
			tTelefono.setText("");
			tEmail.setText("");
		} else {
			Toast.makeText(getBaseContext(),
					"Error: Compruebe que los datos sean correctos",
					Toast.LENGTH_SHORT).show();
		}

		sqlite.cerrar();

	}

	public void cancelarPersona() {
		Toast mensaje = Toast.makeText(this, "No se guardaron los datos.",
				Toast.LENGTH_SHORT);
		mensaje.show();
	}

	public void verPersonas(View v) {
		Toast mensaje = Toast.makeText(this, "Coming soon..", Toast.LENGTH_SHORT);
		mensaje.show();
		/*Fragment fragment = new RegistrosClientes();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();*/
	}

	/*** Metodos para Organizacion ***/

	public void eliminarOrganizacion(View view) {
		AlertDialog.Builder cuadroDialogo = new AlertDialog.Builder(this);
		cuadroDialogo.setTitle("Confirmacion de guardado");
		cuadroDialogo
				.setMessage("Estas seguro que desea Eliminar el registro?");
		cuadroDialogo.setCancelable(false);

		cuadroDialogo.setPositiveButton("Aceptar",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						removerOrganizacion();
					}
				});

		cuadroDialogo.setNegativeButton("Cancelar",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						cancelarOrganizacion();
					}
				});

		cuadroDialogo.show();
	}

	public void removerOrganizacion() {

		// obtiene los valores ingresados
		EditText tNombre = (EditText) findViewById(R.id.editTextNombreOrg);
		EditText tTelefono = (EditText) findViewById(R.id.editTextTelefonoOrg);
		EditText tDireccion = (EditText) findViewById(R.id.editTextDireccionOrg);

		// obtiene el ID de la Organizacion a Eliminar
		EditText tID = (EditText) findViewById(R.id.editTextIDOrg);
		String idOrganizacionAEliminar = tID.getText().toString();

		// Registra en la base de datos
		sqlite.abrir();

		boolean respuestaExitosa = false;

		if (!"".equalsIgnoreCase(idOrganizacionAEliminar)) {

			respuestaExitosa = sqlite
					.eliminarRegistroOrganizacion(idOrganizacionAEliminar
							.substring(
									idOrganizacionAEliminar.indexOf("[") + 1,
									idOrganizacionAEliminar.indexOf("]")));

		} else {
			Toast.makeText(getBaseContext(),
					"Error: No puede eliminar un registro inexistente",
					Toast.LENGTH_SHORT).show();
		}

		if (respuestaExitosa) {
			Toast mensaje = Toast.makeText(this,
					"Organizacion Eliminada con Exito", Toast.LENGTH_SHORT);
			mensaje.show();

			// limpia los campos para siguiente carga
			tID.setText("");
			tNombre.setText("");
			tTelefono.setText("");
			tDireccion.setText("");
		} else {
			Toast.makeText(getBaseContext(),
					"Error: Compruebe que los datos sean correctos",
					Toast.LENGTH_SHORT).show();
		}

		sqlite.cerrar();
	}

	public void guardarOrganizacion(View view) {
		aceptarOrganizacion();
	}

	public void aceptarOrganizacion() {
		// obtiene los valores ingresados
		EditText tNombre = (EditText) findViewById(R.id.editTextNombreOrg);
		EditText tTelefono = (EditText) findViewById(R.id.editTextTelefonoOrg);
		EditText tDireccion = (EditText) findViewById(R.id.editTextDireccionOrg);

		String nombre = tNombre.getText().toString();
		int telefono = "".equalsIgnoreCase(tTelefono.getText().toString()) ? 0
				: Integer.parseInt(tTelefono.getText().toString());
		String direccion = tDireccion.getText().toString();

		// obtiene el ID si es que el registro ya existe y estamos editando
		EditText tID = (EditText) findViewById(R.id.editTextIDOrg);
		String idOrganizacionAEditar = tID.getText().toString();
		
		// Registra en la base de datos
		sqlite.abrir();

		boolean respuestaExitosa = false;

		if (!"".equalsIgnoreCase(idOrganizacionAEditar)) {
			// si el campo ID esta cargado, llamar a Editar

			//saco los caracteres que rodean al ID
			idOrganizacionAEditar = idOrganizacionAEditar.substring(
					idOrganizacionAEditar.indexOf("[") + 1,
					idOrganizacionAEditar.indexOf("]"));
			
			respuestaExitosa = sqlite.modificarRegistroOrganizacion(
					idOrganizacionAEditar, nombre, telefono, direccion);
		} else {
			// sino llamar a Guardar
			respuestaExitosa = sqlite.agregarRegistroOrganizacion(nombre,
					telefono, direccion);
		}

		if (respuestaExitosa) {
			Toast mensaje = Toast.makeText(this,
					"Organizacion guardada con Exito", Toast.LENGTH_SHORT);
			mensaje.show();

			// limpia los campos para siguiente carga
			tID.setText("");
			tNombre.setText("");
			tTelefono.setText("");
			tDireccion.setText("");
		} else {
			Toast.makeText(getBaseContext(),
					"Error: Compruebe que los datos sean correctos",
					Toast.LENGTH_SHORT).show();
		}

		sqlite.cerrar();

	}

	public void cancelarOrganizacion() {
		Toast mensaje = Toast.makeText(this, "No se guardaron los datos.",
				Toast.LENGTH_SHORT);
		mensaje.show();
	}

	public void verOrganizaciones(View v) {
		Fragment fragment = new RegistrosOrganizacion();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("Registros de Organizaciones");

		FragmentManager fragmentManager = getSupportFragmentManager();

		fragmentManager.beginTransaction().replace(R.id.container, fragment)
				.commit();
	}

}
