package com.bolivia.sqliteextreme;
//

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bolivia.sqliteextreme.db.SQLite;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Calendar;

//
//

public class MainActivity extends Activity implements OnClickListener{

	//Variables para fecha
    private int mYear;
    private int mMonth;
    private int mDay;
	String rotacion;
    //
    private TextView txtFechaNac;
    private EditText txtName;
	private EditText txtName1;
    EditText AAAAB;
    private Spinner sPaises;
    private Button btnRegistrar;
    private Button btnCancelar;
    private Button btnRegistros;
    private CheckBox chIdioma;
    private RadioGroup radioGroup;
	int requestCode;
	int resultCode;
	EditText et33;
	EditText et44;
	int b;
	int c;
	Intent intent;
	//
    private SQLite sqlite;

	//DatePickerDialog
	private DatePickerDialog.OnDateSetListener mDateSetListener =
	        new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					 mYear = year;
		             mMonth = monthOfYear;
		             mDay = dayOfMonth;
		             txtFechaNac.setText( ((mDay<10)?"0"+mDay:mDay) + "/" + ((mMonth<10)?"0"+mMonth:mMonth) + "/" + mYear );
				}
	        };



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//
		chIdioma = (CheckBox) findViewById( R.id.chIdioma );
		txtName = (EditText) findViewById( R.id.etName);
		txtName1 = (EditText) findViewById( R.id.etName1);
		//txtName2 =(EditText) findViewById(R.id.AAAAB);
		btnRegistrar = (Button) findViewById(R.id.btnRegistrar );
		btnRegistrar.setOnClickListener( this );
		btnCancelar = (Button) findViewById(R.id.btnCancelar );
		btnCancelar.setOnClickListener( this );
		btnRegistros = (Button) findViewById(R.id.btnRegistros );
		btnRegistros.setOnClickListener( this );
		radioGroup = (RadioGroup) findViewById( R.id.gpSexo );
		txtFechaNac = (TextView) findViewById(R.id.txtFechaNac );
		txtFechaNac.setOnClickListener( this );
		int requestCode;
		int resultCode;
		Intent intent;

		//Se llena el Spinner con los nombres de paises
		String[] paises = getResources().getStringArray(R.array.paises);
		sPaises = (Spinner) findViewById(R.id.sPaises );
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, paises );
		sPaises.setAdapter(adapter);
		//Obtiene fecha actual y coloca en el textview
		Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        //muestra la fecha de la forma 00/00/0000
        txtFechaNac.setText( ((mDay<10)?"0"+mDay:mDay) + "/" + ((mMonth<10)?"0"+mMonth:mMonth) + "/" + mYear );
        //base de datos
        sqlite = new SQLite( this );
		sqlite.abrir();
		//

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		String edit1 = txtName.getText().toString();
		String edit2 = txtName1.getText().toString();
		outState.putString("STRING1", edit1);
		outState.putString("STRING2", edit2);


		//Se Instancia el botón de Scan
		Button scanBtn = (Button) findViewById(R.id.buttonhkjhkjUltimo);
//Se Instancia el Campo de Texto para el nombre del formato de código de barra
		TextView formatTxt = (TextView) findViewById(R.id.etName);
//Se Instancia el Campo de Texto para el contenido  del código de barra
		TextView contentTxt = (TextView) findViewById(R.id.etName1);
//Se agrega la clase MainActivityold3.java como Listener del evento click del botón de Scan
		scanBtn.setOnClickListener(this);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		String edit1 = savedInstanceState.getString("STRING1");
		String edit2 = savedInstanceState.getString("STRING2");
		txtName.setText(edit1);
		txtName1.setText(edit2);
	}
String l4;
	String l5;
	@Override
	protected void onPause() {
		l4=txtName1.getText().toString();
		l5=txtName1.getText().toString();

		super.onPause();
	}
	public void onResume() {
		super.onResume();
		if (l4==null){txtName.setText("Hola: " + l4+".");}
		if (l5==null){txtName1.setText("hola2: "+ l5+ ".");}



		//.setText(lll);
				}

	@Override
	public void onClick(View v) {
		switch ( v.getId() )
		{

			case R.id.btnRegistrar:
				int radioButtonID = radioGroup.getCheckedRadioButtonId();
				View radioButton = radioGroup.findViewById( radioButtonID );
				int index = radioGroup.indexOfChild( radioButton );
				//Registra en la base de datos				
				if ( sqlite.addRegistro( txtName.getText().toString(),
						            txtFechaNac.getText().toString(),
						            sPaises.getSelectedItem().toString(),
						            ( index == 0)?"Hombre":"Mujer" ,
						            ( chIdioma.isChecked() )?"Si":"No" ) )
				{
					//recupera ID de ultimo registro y pasa como parametro
					int id = sqlite.getUltimoID();
					Bundle bundle = new Bundle();
					bundle.putInt("id", id);
					Intent intent = new Intent( MainActivity.this, RegistroActivity.class );
					intent.putExtras( bundle );
					startActivity( intent );
				}
				else
				{
					Toast.makeText(getBaseContext(), "Error: Compruebe que los datos sean correctos"  ,Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.btnCancelar: sqlite.cerrar(); finish(); break;
			case R.id.btnRegistros:
				Intent iRegs = new Intent( MainActivity.this, RegistrosActivity.class );
				startActivity( iRegs );
				break;
			case R.id.txtFechaNac: verDatePicker(); break;
		}
	}

	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		EditText et3 = (EditText) findViewById(R.id.txtName);
		EditText et4 = (EditText) findViewById(R.id.editTextlalala1);

		if (requestCode == 1){
			if(resultCode == RESULT_OK){
				String result=data.getStringExtra("lalala");
				et3.setText( result);
				String result1=data.getStringExtra("lalala1");
				et4.setText( result1);
				Toast.makeText(getApplicationContext(), result1, Toast.LENGTH_SHORT).show();

			}
			if (resultCode == RESULT_CANCELED) {
				//Write your code if there's no result
				Toast.makeText(getApplicationContext(), "Nothing Returned!", Toast.LENGTH_SHORT).show();
			}
		}

	}
*/

	/**
	 * Metodo para mostrar un DatePickerDialog
	 * */
	public void verDatePicker()
	{
		DatePickerDialog d = new DatePickerDialog( this , mDateSetListener, mYear, mMonth, mDay );
		d.show();
	}


	public void onClickLALALA(View view) {
		int b=0;
		String atob=null;
		atob=txtName.getText().toString();
		Intent intent = new Intent(MainActivity.this, ScannerToData.class);
		if (atob !=null){intent.putExtra("enviodelunoaldos", atob);}
		else
		{Toast.makeText(getApplicationContext(), "lalala pasa a b: " + atob, Toast.LENGTH_LONG).show();}
		//if (txtName !=null) {intent.putExtra("enviodel1aldos", txtName;
		startActivityForResult(intent, 1);

	}
	public void onClickLALALA1(View view) {
		String btoc=null;
		int b = 1;

		btoc=txtName1.getText().toString();
		Intent intent = new Intent(MainActivity.this, ScannerToData1.class);
		//if (btoc!=null){intent.putExtra("enviodelunobaldosc", btoc);}
		//else {Toast.makeText(getApplicationContext(), "Se pasa a b: " + btoc, Toast.LENGTH_SHORT).show();}
		intent.putExtra("enviodelunobaldosc", btoc);
		intent.putExtra("1",b);
		Toast.makeText(getApplicationContext(), "lalala1 pasa a b: " + btoc, Toast.LENGTH_LONG).show();
		startActivityForResult(intent, 1);
	}

	public void performScanlalala(View view) {
        			com.google.zxing.integration.android.IntentIntegrator scanIntegrator =
				new com.google.zxing.integration.android.IntentIntegrator(this);
		scanIntegrator.initiateScan();
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent,b);
	}

	public void performScanlalala44(View view) {
		c=0;
		c=5;
		com.google.zxing.integration.android.IntentIntegrator scanIntegrator =
				new com.google.zxing.integration.android.IntentIntegrator(this);
		scanIntegrator.initiateScan();
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent,b);
		//setContentView(R.layout.activity_main);
	}




	public void onActivityResult(int requestCode, int resultCode, Intent intent, int b) {
	//retrieve result of scanning - instantiate ZXing object
	IntentResult scanningResult =
			com.google.zxing.integration.android.IntentIntegrator.parseActivityResult(requestCode, resultCode, intent, b);
	//get content from Intent Result
	String scanContent = scanningResult.getContents();
	String resultadofinal = scanningResult.getContents();
	//int bfinal=
	//int rrr=
	//get format name of data scanned
	//String scanFormat = scanningResult.getFormatName();
	//formatTxt.setText("FORMAT: " + scanFormat);

	//txtName1.setText(scanContent);
	//if (Resuli==1){Toast.makeText(getApplicationContext(), ": " + scanContent, Toast.LENGTH_LONG).show();}
		//String resul44b = data.getStringExtra("result44");
	if (b==2){Toast.makeText(getApplicationContext(), ": " + scanContent, Toast.LENGTH_LONG).show();
		et33.setText(": " + scanContent);
	} else {Toast.makeText(getApplicationContext(), "NO B33 ", Toast.LENGTH_LONG).show();}

	if (b==5){Toast.makeText(getApplicationContext(), ": " + scanContent, Toast.LENGTH_LONG).show();
		et44.setText(": " + scanContent);}

	//else {Toast.makeText(getApplicationContext(), "NO B44 ", Toast.LENGTH_LONG).show();}
	//Toast.makeText(getApplicationContext(), "b= "b , Toast.LENGTH_LONG).show();
	//   Intent resultIntent = new Intent();
	//   resultIntent.putExtra("result", "Getting Smile Back!!");


}
	public void onClickultimo(View view) {


	}
}
