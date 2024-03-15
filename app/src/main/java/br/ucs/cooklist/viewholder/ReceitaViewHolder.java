package br.ucs.cooklist.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.ucs.cooklist.R;
import br.ucs.cooklist.helper.ImageHelper;
import br.ucs.cooklist.model.Receita;

public class ReceitaViewHolder extends RecyclerView.ViewHolder {
    private ImageView imgViewReceitaLista;
    private TextView txtNomeReceitaLista;
    private TextView txtDesReceitaLista;
    private View itemView;

    public ReceitaViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        init();
    }

    public View getItemView() {
        return itemView;
    }

    private void init() {
        txtNomeReceitaLista = itemView.findViewById(R.id.txtNomeReceitaLista);
        txtDesReceitaLista = itemView.findViewById(R.id.txtDesReceitaLista);
        imgViewReceitaLista = itemView.findViewById(R.id.imgViewReceitaLista);
    }

    public void bind(Receita receita) {
        txtNomeReceitaLista.setText(receita.getNomeReceita());
        txtDesReceitaLista.setText(receita.getDesReceita());

        if (receita.getBase64Receita() != null && !receita.getBase64Receita().trim().isEmpty()) {
            ImageHelper.loadToImageView(itemView.getContext(), receita.getBase64Receita(), imgViewReceitaLista);
        }
    }

}
