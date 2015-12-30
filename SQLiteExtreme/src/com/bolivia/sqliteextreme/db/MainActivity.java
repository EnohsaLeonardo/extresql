
package com.bolivia.sqliteextreme.db;
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

import com.bolivia.sqliteextreme.R;
import com.bolivia.sqliteextreme.RegistroActivity;
import com.bolivia.sqliteextreme.RegistrosActivity;

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
    private Spinner sPaises;
    private Button btnRegistrar;
    private Button btnCancelar;
    private Button btnRegistros;
    private CheckBox chIdioma;
    private RadioGroup radioGroup;
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
        txtName1 = (EditText) findViewById( R.id.etName1 );
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar );
        btnRegistrar.setOnClickListener( this );
        btnCancelar = (Button) findViewById(R.id.btnCancelar );
        btnCancelar.setOnClickListener( this );
        btnRegistros = (Button) findViewById(R.id.btnRegistros );
        btnRegistros.setOnClickListener( this );
        radioGroup = (RadioGroup) findViewById( R.id.gpSexo );
        txtFechaNac = (TextView) findViewById(R.id.txtFechaNac );
        txtFechaNac.setOnClickListener( this );

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



    public void onResume() {
        super.onResume();


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
                    Intent intent = new Intent( MainActivityviejo.this, RegistroActivity.class );
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
                Intent iRegs = new Intent( MainActivityviejo.this, RegistrosActivity.class );
                startActivity(iRegs);
                break;
            //case R.id.txtFechaNac: verDatePicker(); break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EditText et3 = (EditText) findViewById(R.id.etName);
        EditText et4 = (EditText) findViewById(R.id.etName1);

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

    }}



/**
 * Metodo para mostrar un DatePickerDialog
 * *//*

	public void verDatePicker()
	{
		DatePickerDialog d = new DatePickerDialog( this , mDateSetListener, mYear, mMonth, mDay );
		d.show();
	}


	public void onClickLALALA(View view) {
		Intent intent = new Intent(MainActivityold.this, ScannerToData.class);
		startActivityForResult(intent, 1);
	}
	public void onClickLALALA1(View view) {
		Intent intent = new Intent(MainActivityold.this, ScannerToData1.class);
		startActivityForResult(intent, 1);
	}

}
*/
