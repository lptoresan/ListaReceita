package br.ucs.cooklist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.ucs.cooklist.R;
import br.ucs.cooklist.model.Ingrediente;
import br.ucs.cooklist.viewholder.IngredienteViewHolder;

public class IngredienteAdapter extends RecyclerView.Adapter<IngredienteViewHolder> {
    private List<Ingrediente> lstIngredientes;
    private Context context;

    public IngredienteAdapter(Context context) {
        this.context = context;
    }

    public List<Ingrediente> getLstIngredientes() {
        if (lstIngredientes == null) {
            lstIngredientes = new ArrayList<>();
        }

        return lstIngredientes;
    }

    @NonNull
    @Override
    public IngredienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IngredienteViewHolder(LayoutInflater.from(context).inflate(R.layout.item_ingrediente, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IngredienteViewHolder holder, int position) {
        holder.bind(getLstIngredientes().get(position));
        holder.setActionRemove(() -> {
            this.getLstIngredientes().remove(position);
            this.notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return getLstIngredientes().size();
    }

    public void addItem(Ingrediente ingrediente) {
        this.getLstIngredientes().add(ingrediente);
        this.notifyDataSetChanged();
    }

}
