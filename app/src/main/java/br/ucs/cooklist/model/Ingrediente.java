package br.ucs.cooklist.model;

import android.os.Bundle;

import java.io.Serializable;

public class Ingrediente implements Serializable {

    private static final long serialVersionUID = 1106482153767152032L;
    private static final String KEY_COD_INGREDIENTE = "codIngrediente";
    private static final String KEY_DES_INGREDIENTE = "desIngrediente";
    private static final String KEY_COD_RECEITA = "codReceita";

    private Integer codIngrediente;
    private String descIngrediente;
    private Integer codReceita;

    public Integer getCodIngrediente() {
        return codIngrediente;
    }

    public void setCodIngrediente(Integer codIngrediente) {
        this.codIngrediente = codIngrediente;
    }

    public String getDescIngrediente() {
        return descIngrediente;
    }

    public void setDescIngrediente(String descIngrediente) {
        this.descIngrediente = descIngrediente;
    }

    public Integer getCodReceita() {
        return codReceita;
    }

    public void setCodReceita(Integer codReceita) {
        this.codReceita = codReceita;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();

        if (getCodIngrediente() != null) {
            bundle.putInt(KEY_COD_INGREDIENTE, getCodIngrediente());
        }

        if (getDescIngrediente() != null) {
            bundle.putString(KEY_DES_INGREDIENTE, getDescIngrediente());
        }

        if (getCodReceita() != null) {
            bundle.putInt(KEY_COD_RECEITA, getCodReceita());
        }

        return bundle;
    }

    public Ingrediente fromBundle(Bundle bundle) {
        if (bundle.containsKey(KEY_COD_INGREDIENTE)) {
            this.setCodIngrediente(bundle.getInt(KEY_COD_INGREDIENTE));
        }

        if (bundle.containsKey(KEY_DES_INGREDIENTE)) {
            this.setDescIngrediente(bundle.getString(KEY_DES_INGREDIENTE));
        }

        if (bundle.containsKey(KEY_COD_RECEITA)) {
            this.setCodIngrediente(bundle.getInt(KEY_COD_RECEITA));
        }

        return this;
    }


}
