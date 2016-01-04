
package com.bolivia.sqliteextreme;

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
import com.bolivia.sqliteextreme.android.IntentIntegrator;
import com.bolivia.sqliteextreme.android.IntentResult;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
//

public class MainActivity extends Activity implements OnClickListener, Collection<String> {

    //Variables para fecha
    private int mYear;
    private int mMonth;
    private int mDay;
    //
    private TextView txtFechaNac;
    private EditText txtName;
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
                    txtFechaNac.setText(((mDay < 10) ? "0" + mDay : mDay) + "/" + ((mMonth < 10) ? "0" + mMonth : mMonth) + "/" + mYear);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonlalala;
        Button buttonlalala1;
        TextView formatTxt, contentTxt;
        //

        chIdioma = (CheckBox) findViewById(R.id.chIdioma);
        txtName = (EditText) findViewById(R.id.txtName);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(this);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(this);
        btnRegistros = (Button) findViewById(R.id.btnRegistros);
        btnRegistros.setOnClickListener(this);
        radioGroup = (RadioGroup) findViewById(R.id.gpSexo);
        txtFechaNac = (TextView) findViewById(R.id.txtFechaNac);
        txtFechaNac.setOnClickListener(this);

        //Se llena el Spinner con los nombres de paises
        String[] paises = getResources().getStringArray(R.array.paises);
        sPaises = (Spinner) findViewById(R.id.sPaises);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, paises);
        sPaises.setAdapter(adapter);
        //Obtiene fecha actual y coloca en el textview
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        //muestra la fecha de la forma 00/00/0000
        txtFechaNac.setText(((mDay < 10) ? "0" + mDay : mDay) + "/" + ((mMonth < 10) ? "0" + mMonth : mMonth) + "/" + mYear);
        //base de datos
        sqlite = new SQLite(this);
        sqlite.abrir();
        //



        setContentView(R.layout.activity_main);
        //Boton en el XML
        Button bt_scan = (Button) findViewById(R.id.buttonlalala);
        Button bt_scan1 = (Button) findViewById(R.id.buttonlalala1);

        //Añadimos Listener, al clickar...
        bt_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lanzamos la activity del escaner
                //IntentIntegrator.initiateScan();
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.initiateScan();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegistrar:
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                View radioButton = radioGroup.findViewById(radioButtonID);
                int index = radioGroup.indexOfChild(radioButton);
                //Registra en la base de datos
                if (sqlite.addRegistro(txtName.getText().toString(),
                        txtFechaNac.getText().toString(),
                        sPaises.getSelectedItem().toString(),
                        (index == 0) ? "Hombre" : "Mujer",
                        (chIdioma.isChecked()) ? "Si" : "No")) {
                    //recupera ID de ultimo registro y pasa como parametro
                    int id = sqlite.getUltimoID();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", id);
                    Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "Error: Compruebe que los datos sean correctos", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCancelar:
                sqlite.cerrar();
                finish();
                break;
            case R.id.btnRegistros:
                Intent iRegs = new Intent(MainActivity.this, RegistrosActivity.class);
                startActivity(iRegs);
                break;
            case R.id.txtFechaNac:
                verDatePicker();
                break;
        }
    }

    /**
     * Metodo para mostrar un DatePickerDialog
     */
    public void verDatePicker() {
        DatePickerDialog d = new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
        d.show();
    }


    //Marcamos lo que queremos que haga una vez haya leido el código
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case IntentIntegrator.REQUEST_CODE:
            {
                if (resultCode == RESULT_CANCELED){
                }
                else
                {
                    //Recogemos los datos   que nos envio el código Qr/codigo de barras
                    IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    String UPCScanned = scanResult.getContents();
                    //cOMO ES SOLO UN EJEMPLO LO SACAREMOS POR PANTALLA.
                    Toast.makeText(getApplicationContext(),UPCScanned,Toast.LENGTH_LONG
                    ).show();
                }
                break;
            }
        }
    }

    @Override
    public boolean add(String object) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends String> collection) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean contains(Object object) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterator<String> iterator() {
        return null;
    }

    @Override
    public boolean remove(Object object) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return null;
    }
}