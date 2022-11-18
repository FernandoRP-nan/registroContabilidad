package com.example.calculadoraprecios;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    public Button btnAñadir, btnFinalizarOrden;
    public ListView list;
    public ListView listCarrito;
    public ListView listResumen;
    public EditText nProducto;
    public EditText pProducto;
    public List<String> listaProductos = new ArrayList<>();
    public List<String> listaCarrito = new ArrayList<>();
    public List<String> listaResumen = new ArrayList<>();
    public ArrayAdapter<String> adaptadorL;
    public ArrayAdapter<String> adaptadorC;
    public ArrayAdapter<String> adaptadorR;
    public float total=0, totalResumen=0;
    public int clientes=0;
    public TextView totalI, totalR;
    public ArrayList<Float> precioslista = new ArrayList<Float>();
    public ArrayList<Float> preciosCarrito = new ArrayList<Float>();

    /*
    * Lo que la aplicación necesita es que tienes que aprender de forma práctica como usar SQLite,
    * después de ello, aplicarlo en la aplicación, sin olvidar que entre ello vas a programar de
    * forma eficiente las listas con el spinner.
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carrito);

        btnAñadir=findViewById(R.id.generar);
        listCarrito=findViewById(R.id.carrito);

        cargarProducto();
        cargarResumen();


    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            nProducto = (EditText) findViewById(R.id.nombreProducto);
            pProducto = (EditText) findViewById(R.id.precioProducto);
            btnAñadir = findViewById(R.id.generar);

            // get the content of both the edit text
            String emailInput = nProducto.getText().toString();
            String passwordInput = pProducto.getText().toString();

            // check whether both the fields are empty or not
            btnAñadir.setEnabled(!emailInput.isEmpty() && !passwordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

    };


    public void cambioAjuste(View view){
        //
        setContentView(R.layout.menu);
        list=findViewById(R.id.listaR);
        list.setAdapter(adaptadorL);
        nProducto = (EditText) findViewById(R.id.nombreProducto);
        pProducto = (EditText) findViewById(R.id.precioProducto);
        btnAñadir = findViewById(R.id.generar);

        nProducto.addTextChangedListener(textWatcher);
        pProducto.addTextChangedListener(textWatcher);



        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int posicion = i;

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MainActivity.this);
                dialogo1.setTitle("Importante");
                dialogo1.setMessage("¿ Elimina este producto ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        listaProductos.remove(posicion);
                        precioslista.remove(posicion);
                        adaptadorL.notifyDataSetChanged();
                        guardarProductos();
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dialogo1.show();

                return false;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int posicion = i;

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MainActivity.this);
                dialogo1.setTitle(" Agregar al carrito ");
                //dialogo1.setMessage(" Agregar al carrito ");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        total=total+precioslista.get(posicion).floatValue();
                        preciosCarrito.add(precioslista.get(posicion).floatValue());
                        listaCarrito.add(listaProductos.get(posicion).toString());
                        adaptadorC = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, listaCarrito);
                        listCarrito.setAdapter(adaptadorC);
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dialogo1.show();

            }
        });

    }

    public void cambioMenu(View view){

        setContentView(R.layout.carrito);

        totalI=findViewById(R.id.totaL);



        totalI.setText("$"+Float. toString(total));

        listCarrito=findViewById(R.id.carrito);

        adaptadorC = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCarrito);
        System.out.println(listaCarrito);
        listCarrito.setAdapter(adaptadorC);

        revisarBtnFinalizar();

        listCarrito.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int posicion = i;

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MainActivity.this);
                dialogo1.setTitle("Importante");
                dialogo1.setMessage("¿ Eliminar este producto ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        total=total-preciosCarrito.get(posicion).floatValue();
                        preciosCarrito.remove(posicion);
                        listaCarrito.remove(posicion);
                        adaptadorC.notifyDataSetChanged();
                        totalI.setText("$"+Float. toString(total));

                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dialogo1.show();

                return false;
            }
        });


    }



    public void añadirProducto (View v){
        System.out.println("si pasó por aquí");

        nProducto = (EditText) findViewById(R.id.nombreProducto);
        pProducto = (EditText) findViewById(R.id.precioProducto);
        list=findViewById(R.id.listaR);



        String texto =nProducto.getText().toString().trim()+" $"+pProducto.getText().toString().trim();

        precioslista.add(Float.valueOf(pProducto.getText().toString()));

        nProducto.getText().clear();
        pProducto.getText().clear();

        listaProductos.add(texto);
        adaptadorL = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaProductos);
        list.setAdapter(adaptadorL);

        guardarProductos();

    }

    public void finalizarOrden(View v){
        //resumen
        listResumen=findViewById(R.id.listaR);



        clientes++;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentDateandTime = simpleDateFormat.format(new Date());

        totalResumen=totalResumen+total;



        listaResumen.add(currentDateandTime+"\nCliente "+clientes+":\n"+String.join(", ", listaCarrito) + " \n Total: $"+Float. toString(total));
        adaptadorR = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, listaResumen);
        listCarrito.setAdapter(adaptadorC);

        //limpieza
        total=0;
        adaptadorC.clear();
        adaptadorC.notifyDataSetChanged();
        totalI=findViewById(R.id.totaL);
        totalI.setText("$"+Float. toString(total));

        guardarResumen();
        revisarBtnFinalizar();

    }

    public void limpiarResumen(View V){

        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MainActivity.this);
        dialogo1.setTitle(" ¿Desea borrar el resumen? ");
        //dialogo1.setMessage(" Agregar al carrito ");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                totalResumen=0;
                clientes=0;

                totalR = findViewById(R.id.tResumen);
                totalR.setText("Venta del día $"+Float. toString(totalResumen));

                listaResumen.removeAll(listaResumen);
                adaptadorR.notifyDataSetChanged();

                guardarResumen();

            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
            }
        });
        dialogo1.show();


    }

    public void cambioAjustes(View V){
        setContentView(R.layout.previsualizacion);

        listResumen=findViewById(R.id.listaR);
        totalR = findViewById(R.id.tResumen);

        totalR.setText("Venta del día $"+Float. toString(totalResumen));

        adaptadorR = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaResumen);
        System.out.println(listaCarrito);
        listResumen.setAdapter(adaptadorR);

        listResumen.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int posicion = i;

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MainActivity.this);
                dialogo1.setTitle("Importante");
                dialogo1.setMessage("¿ Elimina esta orden ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        totalResumen=totalResumen-precioslista.get(posicion).floatValue();
                        listaResumen.remove(posicion);
                        adaptadorR.notifyDataSetChanged();
                        totalR.setText("Venta del día $"+Float. toString(totalResumen));
                        guardarResumen();
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dialogo1.show();

                return false;
            }
        });
    }

    public void guardarProductos(){
        String guardar = String.join("\n", listaProductos);

        String directorio= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath();

        System.out.println("pasó por aquí sisi");

        /** FORMA 1 DE ESCRITURA **/
        FileWriter fichero = null;
        try {

            fichero = new FileWriter(directorio+"/productos.txt");

            PrintWriter pw = new PrintWriter(fichero); pw.write(""); //pw.flush(); //pw.close();

            System.out.println(guardar);
            System.out.println("uwu");

            fichero.write(guardar);

            fichero.close();

        } catch (Exception ex) {
            System.out.println("Mensaje de la excepción: " + ex.getMessage());
        }


    }

    public void cargarProducto(){

        String directorio= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath();

        // Fichero del que queremos leer
        File fichero = new File(directorio+"/productos.txt");
        Scanner s = null;

        try {
            // Leemos el contenido del fichero
            System.out.println("... Leemos el contenido del fichero ...");
            s = new Scanner(fichero);
            // Leemos linea a linea el fichero
            while (s.hasNextLine()) {
                String linea = s.nextLine(); 	// Guardamos la linea en un String
                listaProductos.add(linea);
                //linea.indexOf("$");

                System.out.println(linea.indexOf("$"));
                System.out.println(precioslista);
                String precio = linea.substring(linea.indexOf("$")+1,linea.length());
                precioslista.add(Float.parseFloat(precio));
                System.out.println("si pas+o xd");
                System.out.println(linea.indexOf("$"));
                System.out.println(linea);      // Imprimimos la linea
            }
            adaptadorL = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaProductos);
            list.setAdapter(adaptadorL);


        } catch (Exception ex) {
            System.out.println("Mensaje: " + ex.getMessage());
        } finally {
            // Cerramos el fichero tanto si la lectura ha sido correcta o no
            try {
                if (s != null)
                    s.close();
            } catch (Exception ex2) {
                System.out.println("Mensaje 2: " + ex2.getMessage());
            }
        }
    }

    public void guardarResumen(){
        String guardar = String.join("\n", listaResumen);

        String directorio= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath();

        System.out.println("pasó por aquí sisi");

        /** FORMA 1 DE ESCRITURA **/
        FileWriter fichero = null;
        try {

            fichero = new FileWriter(directorio+"/resumen.txt");

            PrintWriter pw = new PrintWriter(fichero); pw.write(""); //pw.flush(); //pw.close();

            System.out.println(guardar);
            System.out.println("uwu");

            fichero.write(guardar);

            fichero.close();

        } catch (Exception ex) {
            System.out.println("Mensaje de la excepción: " + ex.getMessage());
        }


    }

    public void cargarResumen(){

        String directorio= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath();

        // Fichero del que queremos leer
        File fichero = new File(directorio+"/resumen.txt");
        Scanner s = null;

        try {
            // Leemos el contenido del fichero
            System.out.println("... Leemos el contenido del fichero ...");
            s = new Scanner(fichero);
            String linea1="",linea2;
            // Leemos linea a linea el fichero
            while (s.hasNextLine()) {
                linea2 = s.nextLine();
                linea1 = linea1+"\n"+linea2; 	// Guardamos la linea en un String
                if (linea2.contains("Total:")){
                    listaResumen.add(linea1);
                    System.out.println(linea1);
                    String precio = linea2.substring(linea2.indexOf("$")+1,linea2.length());
                    totalResumen=totalResumen+Float.parseFloat(precio);
                    System.out.println("cargo resumen: "+totalResumen);

                    linea1="";

                }

            }
            adaptadorL = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaProductos);
            list.setAdapter(adaptadorL);


        } catch (Exception ex) {
            System.out.println("Mensaje: " + ex.getMessage());
        } finally {
            // Cerramos el fichero tanto si la lectura ha sido correcta o no
            try {
                if (s != null)
                    s.close();
            } catch (Exception ex2) {
                System.out.println("Mensaje 2: " + ex2.getMessage());
            }
        }
    }


    public void revisarBtnFinalizar(){
        btnFinalizarOrden=findViewById(R.id.finalizarOrden);

        if(listaCarrito.isEmpty()){

            btnFinalizarOrden.setEnabled(false);

        }else{
            btnFinalizarOrden.setEnabled(true);
        }
    }



}