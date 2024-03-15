package br.ucs.cooklist.model;

import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Receita implements Serializable {
    private static final long serialVersionUID = -6276021861030425055L;

    private static final String KEY_COD_RECEITA = "codReceita";
    private static final String KEY_NOME_RECEITA = "nomeReceita";
    private static final String KEY_DES_RECEITA = "desReceita";
    private static final String KEY_BASE64_RECEITA = "base64";
    private static final String KEY_LST_INGREDIENTES = "ingredientes";

    private Integer codReceita;
    private String nomeReceita;
    private String desReceita;
    private String base64Receita;
    private List<Ingrediente> lstIngredientes;

    public Receita() {
    }

    public Integer getCodReceita() {
        return codReceita;
    }

    public void setCodReceita(Integer codReceita) {
        this.codReceita = codReceita;
    }

    public String getNomeReceita() {
        return nomeReceita;
    }

    public void setNomeReceita(String nomeReceita) {
        this.nomeReceita = nomeReceita;
    }

    public String getDesReceita() {
        return desReceita;
    }

    public void setDesReceita(String desReceita) {
        this.desReceita = desReceita;
    }

    public List<Ingrediente> getLstIngredientes() {
        if (lstIngredientes == null) {
            lstIngredientes = new ArrayList<>();
        }
        return lstIngredientes;
    }

    public void setLstIngredientes(List<Ingrediente> lstIngredientes) {
        this.lstIngredientes = lstIngredientes;
    }

    public String getBase64Receita() {
        return base64Receita;
    }

    public void setBase64Receita(String base64Receita) {
        this.base64Receita = base64Receita;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();

        if (getCodReceita() != null) {
            bundle.putInt(KEY_COD_RECEITA, getCodReceita());
        }

        if (getNomeReceita() != null) {
            bundle.putString(KEY_NOME_RECEITA, getNomeReceita());
        }

        if (getDesReceita() != null) {
            bundle.putString(KEY_DES_RECEITA, getDesReceita());
        }

        if (getBase64Receita() != null) {
            bundle.putString(KEY_BASE64_RECEITA, getBase64Receita());
        }

        ArrayList<Bundle> ingredientes = new ArrayList<>();
        getLstIngredientes().forEach(ingrediente -> ingredientes.add(ingrediente.toBundle()));
        bundle.putParcelableArrayList(KEY_LST_INGREDIENTES, ingredientes);

        return bundle;
    }

    public Receita fromBundle(Bundle bundle) {
        if (bundle.containsKey(KEY_COD_RECEITA)) {
            this.setCodReceita(bundle.getInt(KEY_COD_RECEITA));
        }

        if (bundle.containsKey(KEY_NOME_RECEITA)) {
            this.setNomeReceita(bundle.getString(KEY_NOME_RECEITA));
        }

        if (bundle.containsKey(KEY_DES_RECEITA)) {
            this.setDesReceita(bundle.getString(KEY_DES_RECEITA));
        }

        if (bundle.containsKey(KEY_BASE64_RECEITA)) {
            this.setBase64Receita(bundle.getString(KEY_BASE64_RECEITA));
        }

        if (bundle.containsKey(KEY_LST_INGREDIENTES)) {
            List<Bundle> bundles = bundle.getParcelableArrayList(KEY_LST_INGREDIENTES);
            bundles.forEach(bIngrediente -> getLstIngredientes().add(new Ingrediente().fromBundle(bIngrediente)));
        }

        return this;
    }

}
