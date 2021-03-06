package fasb.edu.br.exemplowebservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Detalhe extends AppCompatActivity {

    List<Modelos> myModelos = new ArrayList<>();
    ArrayAdapter<Modelos> adapter;
    ListView lista ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);

        Intent id = getIntent();

        final int idFab = id.getIntExtra("id_fab", 0);

        lista = (ListView) findViewById(R.id.detalhes);


        new Thread() {
            @Override
            public void run() {
                JSONObject modDados = null;
                ConexaoURL conexaoURL = new ConexaoURL();
                modDados = conexaoURL.GetUrlJson("http://nynoteste.esy.es/dados.php?tabela=modelos&filtro="+ idFab);

                try {
                    JSONArray dados = new JSONArray( modDados.getJSONArray("Dados").toString() ) ;

                    for (int i = 0; i<dados.length(); i++){
                        JSONObject d = dados.getJSONObject(i);

                        if ( d != null) {
                            myModelos.add( new Modelos(
                                                    d.getInt("id"),
                                                    null,  //por não precisa mostrar essa informação.
                                                    d.getString("descricao") ) );
                        }
                    }

                    adapter = new ArrayAdapter<Modelos>(getApplication(),R.layout.support_simple_spinner_dropdown_item, myModelos);

                    lista.post(new Runnable() {
                        @Override
                        public void run() {
                            lista.setAdapter(adapter);
                        }
                    });

                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Modelos m = myModelos.get(position);

                Toast.makeText(getApplication(), m.getDescricao(), Toast.LENGTH_SHORT );
            }
        });

    }
}
