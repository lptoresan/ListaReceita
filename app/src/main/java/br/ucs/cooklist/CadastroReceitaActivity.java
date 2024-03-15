package br.ucs.cooklist;

import static android.provider.MediaStore.Images.Media.getBitmap;
import static br.ucs.cooklist.helper.ImageHelper.loadToImageView;
import static br.ucs.cooklist.helper.ImageHelper.toBase64;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.GetContent;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

import br.ucs.cooklist.adapter.IngredienteAdapter;
import br.ucs.cooklist.helper.DatabaseHelper;
import br.ucs.cooklist.model.Ingrediente;
import br.ucs.cooklist.model.Receita;

public class CadastroReceitaActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 123;
    private static final int CAMERA_REQUEST_CODE = 321;
    private ImageView imgViewCadastroReceita;
    private EditText inputNomeReceita;
    private EditText inputDescricaoReceita;
    private Button btnAdicionarIngrediente;
    private RecyclerView rcIngredientes;
    private Button btnSalvarReceita;
    private Button btnCancelarReceita;
    private IngredienteAdapter ingredienteAdapter;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<String> galleryLauncher;
    private DatabaseHelper databaseHelper;
    private Receita receita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_receita);
        init();
    }

    private void init() {
        imgViewCadastroReceita = findViewById(R.id.imgViewCadastroReceita);
        inputNomeReceita = findViewById(R.id.inputNomeReceita);
        inputDescricaoReceita = findViewById(R.id.inputDescricaoReceita);
        btnAdicionarIngrediente = findViewById(R.id.btnAdicionarIngrediente);
        rcIngredientes = findViewById(R.id.rcIngredientes);
        btnSalvarReceita = findViewById(R.id.btnSalvarReceita);
        btnCancelarReceita = findViewById(R.id.btnCancelarReceita);

        ingredienteAdapter = new IngredienteAdapter(this);
        rcIngredientes.setAdapter(ingredienteAdapter);
        rcIngredientes.setLayoutManager(new LinearLayoutManager(this));

        cameraLauncher = registerForActivityResult(new StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                loadToImageView(CadastroReceitaActivity.this, imageBitmap, imgViewCadastroReceita);
            }
        });

        galleryLauncher = registerForActivityResult(new GetContent(), result -> {
            if (result != null) {
                try {
                    Bitmap imageBitmap = getBitmap(getContentResolver(), result);
                    imgViewCadastroReceita.setImageBitmap(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        addEvents();
        carregarReceita();
    }

    private void addEvents() {
        imgViewCadastroReceita.setOnClickListener(view -> {
            showImageOptionsPopup();
        });

        btnSalvarReceita.setOnClickListener(view -> {
            if (receita == null) {
                receita = new Receita();
            }

            receita.setNomeReceita(inputNomeReceita.getText().toString());
            receita.setDesReceita(inputDescricaoReceita.getText().toString());
            Drawable dr = ((ImageView) imgViewCadastroReceita).getDrawable();
            Bitmap bmp = ((BitmapDrawable) dr.getCurrent()).getBitmap();
            receita.setBase64Receita(toBase64(bmp));

            receita.getLstIngredientes().clear(); // limpa os ingredientes para salvar posteriormente
            ingredienteAdapter.getLstIngredientes().forEach(ingrediente -> receita.getLstIngredientes().add(ingrediente));

            if (receita.getCodReceita() == null) {
                getDatabaseHelper().inserirReceita(receita);
            } else {
                getDatabaseHelper().atualizarReceita(receita);
            }

            iniciarActivityLista();
        });

        btnCancelarReceita.setOnClickListener(view -> iniciarActivityLista());

        btnAdicionarIngrediente.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Ingrediente");

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("OK", (dialog, which) -> {
                String text = input.getText().toString();

                if (text == null || text.trim().isEmpty()) {
                    return;
                }

                Ingrediente ingrediente = new Ingrediente();
                ingrediente.setDescIngrediente(text);
                ingredienteAdapter.addItem(ingrediente);
                dialog.dismiss();
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });
    }

    /**
     * Em desuso, pois passar o objeto receita pode causar problemas, devido ao tamanho do objeto.
     */
    private void _carregarReceita() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        if (!extras.containsKey("receita")) {
            return;
        }

        receita = new Receita().fromBundle(extras.getBundle("receita"));

        if (receita.getBase64Receita() != null && !receita.getBase64Receita().trim().isEmpty()) {
            loadToImageView(this, receita.getBase64Receita(), imgViewCadastroReceita);
        }

        inputNomeReceita.setText(receita.getNomeReceita());
        inputDescricaoReceita.setText(receita.getDesReceita());
        receita.getLstIngredientes().forEach(ingrediente -> {
            ingredienteAdapter.addItem(ingrediente);
        });
    }

    private void carregarReceita() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        if (!extras.containsKey("codReceita")) {
            // TODO log: não encontrou key receita
            return;
        }

        Integer codReceita = extras.getInt("codReceita");
        receita = getDatabaseHelper().buscarReceita(codReceita);

        if (receita.getBase64Receita() != null && !receita.getBase64Receita().trim().isEmpty()) {
            loadToImageView(this, receita.getBase64Receita(), imgViewCadastroReceita);
        }

        inputNomeReceita.setText(receita.getNomeReceita());
        inputDescricaoReceita.setText(receita.getDesReceita());
        receita.getLstIngredientes().forEach(ingrediente -> {
            ingredienteAdapter.addItem(ingrediente);
        });
    }

    private void showImageOptionsPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.popup_options);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnLoadImage = dialog.findViewById(R.id.btnLoadImage);
        Button btnCaptureImage = dialog.findViewById(R.id.btnCaptureImage);

        btnLoadImage.setOnClickListener(view -> {
            dialog.dismiss();
            loadImageFromGallery();
        });

        btnCaptureImage.setOnClickListener(view -> {
            dialog.dismiss();
            startCamera();
        });
    }

    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Verifique se há um aplicativo de câmera disponível
        if (intent.resolveActivity(getPackageManager()) != null) {
            cameraLauncher.launch(intent);
        } else {
            Toast.makeText(this, "Nenhum aplicativo de câmera encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImageFromGallery() {
        galleryLauncher.launch("image/*");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            try {
                Bitmap imageBitmap = getBitmap(this.getContentResolver(), imageUri);
                loadToImageView(CadastroReceitaActivity.this, imageBitmap, imgViewCadastroReceita);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(this);
        }
        return databaseHelper;
    }

    private void iniciarActivityLista() {
        Intent intent = new Intent(CadastroReceitaActivity.this, MainActivity.class);
        startActivity(intent);
    }
}