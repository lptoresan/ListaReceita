package br.ucs.cooklist.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.ucs.cooklist.R;
import br.ucs.cooklist.model.Ingrediente;

public class IngredienteViewHolder extends RecyclerView.ViewHolder {
    private TextView txtIngrediente;
    private Button btnRemoverIngrediente;
    private View itemView;
    private Runnable actionRemove;

    public IngredienteViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        init();
    }

    public void setActionRemove(Runnable actionRemove) {
        this.actionRemove = actionRemove;
    }

    private void init() {
        txtIngrediente = itemView.findViewById(R.id.txtIngrediente);
        btnRemoverIngrediente = itemView.findViewById(R.id.btnRemoverIngrediente);

        btnRemoverIngrediente.setOnClickListener(event -> {
            if (actionRemove != null) {
                actionRemove.run();
            }
        });
    }

    public void bind(Ingrediente ingrediente) {
        txtIngrediente.setText(ingrediente.getDescIngrediente());
    }


}
