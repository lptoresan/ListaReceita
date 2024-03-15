package br.ucs.cooklist.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.ucs.cooklist.model.Ingrediente;
import br.ucs.cooklist.model.Receita;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "cookbook_ucs";

    public static final String TABLE_RECEITAS = "cookbook_recipe";
    public static final String COL_ID_RECEITA = "id";
    public static final String COL_NOME_RECEITA = "nome";
    public static final String COL_DES_RECEITA = "des_receita";
    public static final String COL_DES_IMG_RECEITA = "des_img";

    public static final String TABLE_INGREDIENTES = "cookbook_ingredients";
    public static final String COL_ID_INGREDIENTE = "id";
    public static final String COL_DES_INGREDIENTE = "descricao";
    public static final String COL_ID_RECEITA_INGREDIENTE = "id_receita";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação da tabela Receitas
        String createTableReceitas = "CREATE TABLE " + TABLE_RECEITAS + " (" + COL_ID_RECEITA + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NOME_RECEITA + " TEXT, " + COL_DES_RECEITA + " TEXT, " + COL_DES_IMG_RECEITA + " TEXT" + ")";
        db.execSQL(createTableReceitas);

        // Criação da tabela Ingredientes
        String createTableIngredientes = "CREATE TABLE " + TABLE_INGREDIENTES + " (" + COL_ID_INGREDIENTE + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_DES_INGREDIENTE + " TEXT, " + COL_ID_RECEITA_INGREDIENTE + " INTEGER, " + "FOREIGN KEY(" + COL_ID_RECEITA_INGREDIENTE + ") REFERENCES " + TABLE_RECEITAS + "(" + COL_ID_RECEITA + ")" + ")";
        db.execSQL(createTableIngredientes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECEITAS);
        onCreate(db);
    }

    public void inserirReceita(Receita receita) {
        SQLiteDatabase db = getWritableDatabase();
        long receitaId = -1;

        try {
            // Inserir a receita na tabela Receitas
            ContentValues receitaValues = new ContentValues();
            receitaValues.put(COL_NOME_RECEITA, receita.getNomeReceita());
            receitaValues.put(COL_DES_RECEITA, receita.getDesReceita());
            receitaValues.put(COL_DES_IMG_RECEITA, receita.getBase64Receita());
            receitaId = db.insert(TABLE_RECEITAS, null, receitaValues);

            // Inserir os ingredientes relacionados à receita na tabela Ingredientes
            List<Ingrediente> ingredientes = receita.getLstIngredientes();
            for (Ingrediente ingrediente : ingredientes) {
                ContentValues ingredienteValues = new ContentValues();
                ingredienteValues.put(COL_DES_INGREDIENTE, ingrediente.getDescIngrediente());
                ingredienteValues.put(COL_ID_RECEITA_INGREDIENTE, receitaId);
                db.insert(TABLE_INGREDIENTES, null, ingredienteValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public List<Receita> buscarReceitas() {
        List<Receita> receitas = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        try {
            // Consultar todas as receitas
            String query = "SELECT * FROM " + TABLE_RECEITAS;
            Cursor cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()) {
                // Obter os dados da receita
                int codReceita = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_RECEITA));
                String nomeReceita = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME_RECEITA));
                String desReceita = cursor.getString(cursor.getColumnIndexOrThrow(COL_DES_RECEITA));
                String desImg = cursor.getString(cursor.getColumnIndexOrThrow(COL_DES_IMG_RECEITA));

                // Criar um objeto Receita com os dados obtidos
                Receita receita = new Receita();
                receita.setCodReceita(codReceita);
                receita.setNomeReceita(nomeReceita);
                receita.setDesReceita(desReceita);
                receita.setBase64Receita(desImg);

                // Consultar os ingredientes associados à receita atual
                String ingredientesQuery = "SELECT * FROM " + TABLE_INGREDIENTES + " WHERE " + COL_ID_RECEITA_INGREDIENTE + " = " + codReceita;
                Cursor ingredientesCursor = db.rawQuery(ingredientesQuery, null);

                while (ingredientesCursor.moveToNext()) {
                    // Obter os dados do ingrediente
                    int codIngrediente = ingredientesCursor.getInt(ingredientesCursor.getColumnIndexOrThrow(COL_ID_INGREDIENTE));
                    String descIngrediente = ingredientesCursor.getString(ingredientesCursor.getColumnIndexOrThrow(COL_DES_INGREDIENTE));

                    // Criar um objeto Ingrediente com os dados obtidos e adicioná-lo à lista de ingredientes da receita
                    Ingrediente ingrediente = new Ingrediente();
                    ingrediente.setCodIngrediente(codIngrediente);
                    ingrediente.setDescIngrediente(descIngrediente);
                    ingrediente.setCodReceita(codReceita);
                    receita.getLstIngredientes().add(ingrediente);
                }

                ingredientesCursor.close();

                // Adicionar a receita à lista de receitas
                receitas.add(receita);
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return receitas;
    }

    public Receita buscarReceita(Integer codReceita) {
        SQLiteDatabase db = getReadableDatabase();
        Receita receita = null;

        try {
            // Consultar a receita pelo código
            String query = "SELECT * FROM " + TABLE_RECEITAS + " WHERE " + COL_ID_RECEITA + " = " + codReceita;
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                // Obter os dados da receita
                String nomeReceita = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME_RECEITA));
                String desReceita = cursor.getString(cursor.getColumnIndexOrThrow(COL_DES_RECEITA));
                String desImg = cursor.getString(cursor.getColumnIndexOrThrow(COL_DES_IMG_RECEITA));

                // Criar um objeto Receita com os dados obtidos
                receita = new Receita();
                receita.setCodReceita(codReceita);
                receita.setNomeReceita(nomeReceita);
                receita.setDesReceita(desReceita);
                receita.setBase64Receita(desImg);

                // Consultar os ingredientes associados à receita
                String ingredientesQuery = "SELECT * FROM " + TABLE_INGREDIENTES + " WHERE " + COL_ID_RECEITA_INGREDIENTE + " = " + codReceita;
                Cursor ingredientesCursor = db.rawQuery(ingredientesQuery, null);

                while (ingredientesCursor.moveToNext()) {
                    // Obter os dados do ingrediente
                    int codIngrediente = ingredientesCursor.getInt(ingredientesCursor.getColumnIndexOrThrow(COL_ID_INGREDIENTE));
                    String descIngrediente = ingredientesCursor.getString(ingredientesCursor.getColumnIndexOrThrow(COL_DES_INGREDIENTE));

                    // Criar um objeto Ingrediente com os dados obtidos e adicioná-lo à lista de ingredientes da receita
                    Ingrediente ingrediente = new Ingrediente();
                    ingrediente.setCodIngrediente(codIngrediente);
                    ingrediente.setDescIngrediente(descIngrediente);
                    ingrediente.setCodReceita(codReceita);
                    receita.getLstIngredientes().add(ingrediente);
                }

                ingredientesCursor.close();
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return receita;
    }

    public void atualizarReceita(Receita receita) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            // Atualizar a receita na tabela Receitas
            ContentValues receitaValues = new ContentValues();
            receitaValues.put(COL_NOME_RECEITA, receita.getNomeReceita());
            receitaValues.put(COL_DES_RECEITA, receita.getDesReceita());
            receitaValues.put(COL_DES_IMG_RECEITA, receita.getBase64Receita());

            String params[] = new String[1];
            params[0] = receita.getCodReceita().toString();

            db.update(TABLE_RECEITAS, receitaValues, COL_ID_RECEITA + " = ?", params);

            // Remover os ingredientes antigos da receita na tabela Ingredientes
            db.delete(TABLE_INGREDIENTES, COL_ID_RECEITA_INGREDIENTE + " = ?", params);

            // Inserir os novos ingredientes da receita na tabela Ingredientes
            List<Ingrediente> ingredientes = receita.getLstIngredientes();
            for (Ingrediente ingrediente : ingredientes) {
                ContentValues ingredienteValues = new ContentValues();
                ingredienteValues.put(COL_DES_INGREDIENTE, ingrediente.getDescIngrediente());
                ingredienteValues.put(COL_ID_RECEITA_INGREDIENTE, receita.getCodReceita());
                db.insert(TABLE_INGREDIENTES, null, ingredienteValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void reset() {
        this.onUpgrade(getWritableDatabase(), 0, 1);
    }


}