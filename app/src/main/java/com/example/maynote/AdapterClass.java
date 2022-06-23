package com.example.maynote;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.ViewHolder> {

    public List<ModelClassNota> notaList;

    AdapterClass(List<ModelClassNota> notaList) {
        this.notaList = notaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String titulo = notaList.get(position).getTitle();
        String descricao = notaList.get(position).getDescription();
        String data = notaList.get(position).getDate();

        holder.setData(titulo,descricao,data);
    }

    @Override
    public int getItemCount() {
        return notaList.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, description, date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textNotasTitle);
            description = itemView.findViewById(R.id.textNotasDescription);
            date = itemView.findViewById(R.id.textNotasDate);
        }

        public void setData(String titulo, String descricao, String data) {
            title.setText(titulo);
            description.setText(descricao);
            date.setText(data);
        }
    }
}
