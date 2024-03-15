package br.ucs.cooklist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.ucs.cooklist.R;
import br.ucs.cooklist.interfaces.RecyclerViewActions;
import br.ucs.cooklist.model.Receita;
import br.ucs.cooklist.viewholder.ReceitaViewHolder;

public class ReceitaAdapter extends RecyclerView.Adapter<ReceitaViewHolder> {
    private List<Receita> lstReceitas;
    private Context context;

    private RecyclerViewActions actions;

    public ReceitaAdapter(Context context) {
        this.context = context;
    }

    public List<Receita> getLstReceitas() {
        if (lstReceitas == null) {
            lstReceitas = new ArrayList<>();
        }

        return lstReceitas;
    }

    @NonNull
    @Override
    public ReceitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReceitaViewHolder(LayoutInflater.from(context).inflate(R.layout.item_receita, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReceitaViewHolder holder, int position) {
        holder.bind(getLstReceitas().get(position));
        holder.getItemView().setOnClickListener(view -> {
            if (actions != null) {
                actions.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getLstReceitas().size();
    }

    public void setActions(RecyclerViewActions actions) {
        this.actions = actions;
    }
}
